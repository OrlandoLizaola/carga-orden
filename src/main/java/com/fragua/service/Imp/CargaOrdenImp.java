package com.fragua.service.Imp;

import org.json.JSONObject;
import org.json.XML;
import lombok.extern.slf4j.Slf4j;
import com.fragua.service.CargaOrdenInt;
import com.fragua.service.OrdenDetalleInt;
import com.fragua.utils.JSONExtractor;
import com.fragua.utils.JsonTransformWs;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
@Service
public class CargaOrdenImp implements CargaOrdenInt {

	@Autowired
	private Environment propiedades;

	@Autowired
	private OrdenDetalleInt ordenDetalleInt;

	@Autowired
	private JsonTransformWs jsonTransformWs;


	/**
	 * Procesa una orden recibida en formato XML.
	 * Este método convierte el XML en un objeto JSON, extrae información relevante,
	 * y realiza varias operaciones dependiendo del tipo de orden.
	 * Incluye la inserción de la orden en la base de datos.
	 *
	 * @param peticionXml el mensaje en formato String que contiene la información de la orden
	 */
	@Override
	public void procesarOrden(String peticionXml) {
		log.info("Iniciando método procesarOrden");

		try {
			log.debug("XML de petición recibido: {}", peticionXml);
			JSONObject jsonObject = XML.toJSONObject(peticionXml);

			String tipo = JSONExtractor.extractField(jsonObject, "ReceivingNode").equals("") ? "O" : "T";
			String ordenOriginal = JSONExtractor.extractField(jsonObject, "ParentSalesOrderNo");
			String idOrden = JSONExtractor.extractField(jsonObject, "SalesOrderNo");
			String sucursal = JSONExtractor.extractField(jsonObject, "ReceivingNode");

			log.info("Tipo de orden: {}, Orden original: {}, ID de orden: {}, Sucursal: {}", tipo, ordenOriginal, idOrden, sucursal);

			JSONObject ordenJson = finOrderTransferInDB(tipo.equals("T") ? ordenOriginal : idOrden, "", sucursal, false);
			log.info("Llamada al servicio findOrderTransferInDB: {}", ordenJson);

			Integer pedido = Integer.parseInt(ordenJson.get("pedido").toString());

			if (!ordenJson.isEmpty()) {
				jsonObject.put("status", 0);

				if (tipo.equals("T")) {
					log.info("Insertando a Master para tipo 'T'");
					insertToMaster(jsonObject, pedido, tipo);
					insertarPagosToMaster(jsonObject, pedido);
					insertFechasMaster(jsonObject, pedido, tipo);
				} else {
					int status = ordenDetalleInt.ordenDetalle(ordenJson);
					log.info("Estado de la orden: {}", status);

					if (status == Integer.parseInt(propiedades.getProperty("orden.status"))) {
						log.info("Estado de la orden es correcto, vinculando a Master");
						insertToMaster(jsonObject, pedido, tipo);
						insertarPagosToMaster(jsonObject, pedido);
						insertFechasMaster(jsonObject, pedido, tipo);
						log.info("Vinculando carrier con ordenoms");
						updateOrdenOriginal(jsonObject);
					} else {
						log.warn("Estado de la orden no coincide, insertando a Master");
						insertToMaster(jsonObject, pedido, tipo);
						insertarPagosToMaster(jsonObject, pedido);
						insertFechasMaster(jsonObject, pedido, tipo);
					}
				}
			} else {
				log.info("La orden no existe en la base de datos");
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				String url = propiedades.getProperty("web.service.url1");
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
				JSONObject envWS = new JSONObject(response.getBody());

				if (envWS.has("type") && envWS.getString("type").equals("application/json")) {
					log.info("Número de folio obtenido desde WebService");
					int ordenfg = envWS.getInt("folio");
					jsonObject.put("pedido", ordenfg);
					log.info("Número de folio obtenido: {}", ordenfg);

					log.info("Consultando Order Details para obtener campos faltantes");
					boolean insertOrdenOriginal = jsonObject.get("tipo").equals("T");

					int status = ordenDetalleInt.ordenDetalle(ordenJson);
					log.info("Estado de la orden después de consultar detalles: {}", status);

					if (insertOrdenOriginal) {
						log.info("Enviando orden a WebService para ser insertada en Master");
						insertToMaster(jsonObject, pedido, tipo);
						insertarPagosToMaster(jsonObject, pedido);
						insertFechasMaster(jsonObject, pedido, tipo);
					} else {
						if (status != Integer.valueOf(propiedades.getProperty("orden.status"))) {
							log.info("Enviando orden a WebService para ser insertada en Master");
							insertToMaster(jsonObject, pedido, tipo);
							insertarPagosToMaster(jsonObject, pedido);
							insertFechasMaster(jsonObject, pedido, tipo);
						} else {
							jsonObject.put("status", propiedades.getProperty("orden.status"));
							log.info("Enviando orden a WebService para ser insertada en Master y no ser sincronizada en sucursal, ya que cuenta con una línea en backordered");
							insertToMaster(jsonObject, pedido, tipo);
							insertarPagosToMaster(jsonObject, pedido);
							insertFechasMaster(jsonObject, pedido, tipo);
						}
					}
				}
			}

			log.info("Se manda a llamar al servicio parcheBeetractToDeliveryMan");
			log.info("ID de orden: {}", idOrden);
			parcheBeetractToDeliveryMan(idOrden);

		} catch (Exception e) {
			log.error("Error en el procesamiento de la orden: {}", e.getMessage(), e);
		}

		log.info("Terminando método procesarOrden");
	}

	/**
	 * Inserta fechas en el sistema a partir de la información de una orden.
	 * Este método construye una solicitud HTTP POST para enviar datos de una orden a un servicio web.
	 * Se extraen y codifican varios campos de la orden, incluidos el número de orden de ventas, el pedido,
	 * el tipo y el tipo de entrega. Además, se registran mensajes informativos sobre el proceso.
	 *
	 * @param orden  un objeto {@link JSONObject} que contiene la información de la orden
	 * @param pedido un entero que representa el identificador del pedido
	 * @param tipo   un string que indica el tipo de operación
	 */
	@Override
	public void insertFechasMaster(JSONObject orden, int pedido, String tipo) {
		log.info("Iniciando proceso para insertar fechas de la orden: {}", orden.get("SalesOrderNo").toString());

		try {
			String url = propiedades.getProperty("web.service.url21");
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			map.add("ordenoms", URLEncoder.encode(orden.get("SalesOrderNo").toString(), StandardCharsets.UTF_8));
			map.add("ordenfg", URLEncoder.encode(String.valueOf(pedido), StandardCharsets.UTF_8));
			map.add("tipo", URLEncoder.encode(String.valueOf(tipo), StandardCharsets.UTF_8));
			map.add("tipoentrega",
					URLEncoder.encode(
							String.valueOf(JSONExtractor.extractField(orden, "FulfillmentType").equals("S") ? 1 : 2),
							StandardCharsets.UTF_8));

			log.debug("Datos cargados para insertar en tabla de fechas: {}", map);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info("[RESPONSE][INSERT_FECHAS]: Respuesta del servicio: {}", response);
			log.info("Finalizado proceso para insertar fechas");

		} catch (Exception e) {
			log.error("Error al insertar fechas: {}", e.getMessage(), e);
		}

		log.debug("Orden procesada: {}", orden.toString());
	}

	/**
	 * Inserta los pagos asociados a una orden en el sistema.
	 * Este método construye una solicitud HTTP POST para enviar datos de pagos
	 * a un servicio web. Se extraen varios campos de la orden y se codifican
	 * adecuadamente. Luego, se envía la solicitud y se registra la respuesta.
	 *
	 * @param orden  un objeto {@link JSONObject} que contiene la información de la orden
	 * @param pedido un entero que representa el identificador del pedido
	 */
	@Override
	public void insertarPagosToMaster(JSONObject orden, int pedido) {
		log.info("Iniciando proceso para insertar pagos de la orden: {}", orden.get("SalesOrderNo").toString());

		try {
			String url = propiedades.getProperty("web.service.url14");
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

			map.add("ordenoms",
					URLEncoder.encode(JSONExtractor.extractField(orden, "SalesOrderNo"), StandardCharsets.UTF_8));
			map.add("ordenfg", URLEncoder.encode(String.valueOf(pedido), StandardCharsets.UTF_8));
			map.add("tipo",
					URLEncoder.encode(JSONExtractor.extractField(orden, "SalesOrderNo"), StandardCharsets.UTF_8));
			map.add("fpago", URLEncoder.encode("24", StandardCharsets.UTF_8));
			map.add("importe", URLEncoder.encode(
					(JSONExtractor.extractField(orden, "importeTotal") == null) ? "0"
							: JSONExtractor.extractField(orden, "importeTotal"), StandardCharsets.UTF_8));

			log.debug("Datos cargados para insertar en tabla de pagos: {}", map);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info("[RESPONSE][INSERT_PAGOS]: Respuesta del servicio: {}", response);
			log.info("Finalizado proceso para insertar pagos");
		} catch (Exception e) {
			log.error("Error al insertar pagos: {}", e.getMessage(), e);
		}

		log.debug("Orden procesada: {}", orden.toString());
	}

	/**
	 * Inserta un pedido, sus productos y el cliente asociado en el sistema.
	 * Este método prepara y envía una solicitud HTTP POST con los detalles del pedido
	 * a un servicio web. Se extraen y transforman varios campos de la orden, y se
	 * registra el proceso de inserción junto con la respuesta del servicio.
	 *
	 * @param orden  un objeto {@link JSONObject} que contiene la información del pedido
	 * @param pedido un entero que representa el identificador del pedido
	 * @param tipo   un string que indica el tipo de operación
	 */
	@Override
	public void insertToMaster(JSONObject orden, int pedido, String tipo) {
		log.info("Iniciando proceso para insertar pedido, productos y cliente de la orden: {}",
				JSONExtractor.extractField(orden, "SalesOrderNo"));

		try {
			Boolean isRelease = Integer.parseInt(JSONExtractor.extractField(orden, "ReleaseNo")) != 1;

			JSONObject jsonPayload = jsonTransformWs.getObjectOrderMaster(orden, false, isRelease);
			jsonPayload.put("ordenoms", JSONExtractor.extractField(orden, "SalesOrderNo"));
			jsonPayload.put("ordenfg", pedido);
			jsonPayload.put("tipo", tipo);
			jsonPayload.put("tipoentrega", JSONExtractor.extractField(orden, "FulfillmentType").equals("S") ? 1 : 2);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(jsonPayload.toString(), headers);

			String url = propiedades.getProperty("web.service.url1");

			log.debug("Preparando solicitud para insertar pedido: {}", jsonPayload.toString());

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			log.info("[REQUEST][INSERT_PEDIDO]: Enviando inserción de pedido: {}", jsonPayload.toString());
			log.info("[RESPONSE][INSERT_PEDIDO]: Respuesta del servicio: {}", response.getBody());
			log.info("Finalizado proceso para insertar pedido, productos y cliente");

		} catch (Exception ex) {
			log.error("Error al insertar pedido: {}", ex.getMessage(), ex);
		}

		log.debug("Orden procesada: {}", orden.toString());
	}

	/**
	 * Finaliza la transferencia de una orden en la base de datos.
	 * Este método construye una URL para enviar una solicitud HTTP GET a un servicio web.
	 * Dependiendo del valor del parámetro `insAlm`, se añaden parámetros adicionales
	 * a la URL. Se espera que la respuesta sea un objeto JSON que contenga información sobre la orden.
	 *
	 * @param ordenoms el número de orden de la venta
	 * @param orden_ot el número de orden de traslado
	 * @param almacen  el código del almacén
	 * @param insAlm   indica si se deben incluir parámetros adicionales en la URL
	 * @return un objeto {@link JSONObject} que representa la respuesta del servicio si contiene la clave "orden", o null si no
	 */
	@Override
	public JSONObject finOrderTransferInDB(String ordenoms, String orden_ot, String almacen, boolean insAlm) {
		log.info("Iniciando proceso para finalizar la transferencia de la orden: {}", ordenoms);

		try {
			RestTemplate restTemplate = new RestTemplate();

			// Construir la URL dependiendo del valor de insAlm
			String url = propiedades.getProperty("web.service.url24") + ordenoms;
			if (insAlm) {
				url += "&almacen=" + almacen + "&ordenot=" + orden_ot;
			}

			log.debug("URL construida para la solicitud: {}", url);

			// Enviar la solicitud y recibir la respuesta
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			log.info("Respuesta recibida de la URL: {}", response.getBody());

			// Comprobar si la respuesta es JSON
			JSONObject json = new JSONObject(response.getBody());
			if (json.has("orden") && !json.get("orden").toString().isEmpty()) {
				log.info("Orden encontrada en la respuesta: {}", json.get("orden"));
				return json; // Retornar el objeto JSON si contiene "orden"
			} else {
				log.warn("La respuesta no contiene 'orden' o está vacía.");
			}

		} catch (Exception e) {
			log.error("Error al finalizar la transferencia de la orden: {}", e.getMessage(), e);
		}

		log.info("Proceso finalizado sin encontrar una orden válida para: {}", ordenoms);
		return null;
	}

	/**
	 * Actualiza la información de una orden original en el sistema.
	 * Este método construye un payload JSON a partir de los datos de la orden
	 * y envía una solicitud HTTP PUT a un servicio web para actualizar la
	 * información de la orden original. Se registran los mensajes de inicio
	 * del proceso y de respuesta del servicio.
	 *
	 * @param orden un objeto {@link JSONObject} que contiene la información de la orden a actualizar
	 */
	private void updateOrdenOriginal(JSONObject orden) {
		log.info("Iniciando actualización de la orden original: {}", orden.toString());

		try {
			log.debug("Transformando la orden a formato para el servicio.");
			JSONObject jsonPayload = jsonTransformWs.getObjectOrderMaster(orden, true, false);
			log.debug("Payload transformado: {}", jsonPayload.toString());

			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url9");
			log.debug("URL del servicio: {}", url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(jsonPayload.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info("Respuesta del servicio al actualizar la orden: {}", response.getBody());

		} catch (Exception e) {
			log.error("Error al actualizar la orden: {}", e.getMessage(), e);
		}
	}

	/**
	 * Envía una solicitud para aplicar un parche a la orden de entrega.
	 * Este método crea un objeto JSON que contiene el número de orden OMS y
	 * envía una solicitud HTTP POST a un servicio web para aplicar el parche.
	 * Se registran mensajes que indican el inicio y la finalización del proceso,
	 * así como la respuesta del servicio.
	 *
	 * @param ordenoms el número de orden OMS que se va a procesar
	 */
	private void parcheBeetractToDeliveryMan(String ordenoms) {
		log.info("Inicio de consumo de servicio parcheBeetractToDeliveryMan para la orden: {}", ordenoms);

		try {
			JSONObject xmlBackup = new JSONObject();
			xmlBackup.put("ordenOMS", ordenoms);
			log.debug("Payload preparado para el servicio: {}", xmlBackup.toString());

			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url26");
			log.debug("URL del servicio: {}", url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(xmlBackup.toString(), headers);
			log.debug("Enviando solicitud al servicio.");

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			log.info("Respuesta del servicio: {}", response.getBody());
		} catch (Exception e) {
			log.error("Error en parcheBeetractToDeliveryMan: {}", e.getMessage(), e);
		} finally {
			log.info("Fin de consumo de servicio parcheBeetractToDeliveryMan");
		}
	}
}