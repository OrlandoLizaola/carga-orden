package com.fragua.service.Imp;

import java.util.Iterator;
import org.json.JSONObject;
import org.json.XML;
import com.fragua.dto.OrdenDto;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fragua.service.CargaOrdenInt;
import com.fragua.service.OrdenDetalleInt;
import com.fragua.utils.JSONExtractor;
import com.fragua.utils.JsonTransformWs;
import java.io.IOException;
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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
	 * Mapea los atributos de un objeto JSON a un DTO de orden. Este métodoverifica
	 * que todos los campos del objeto JSON sean no nulos, no vacíos y no nulos en
	 * términos de tipo. Si se encuentra un campo que no cumple con estas
	 * condiciones, se lanza una excepción {@link IllegalArgumentException}.
	 * 
	 * @param jsonNode el objeto JSON que contiene los atributos a mapear
	 * @param ordenDto el objeto DTO de orden al que se le asignarán los atributos
	 * @return el objeto JSON original, si todos los campos son válidos
	 * @throws IllegalArgumentException si algún campo del JSON es nulo o vacío
	 */
	@Override
	public JsonNode mapearAtributos(JsonNode jsonNode, OrdenDto ordenDto) {
		Iterator<String> iterator = jsonNode.fieldNames();

		while (iterator.hasNext()) {
			String key = iterator.next();
			JsonNode valueNode = jsonNode.get(key);

			if (valueNode == null || valueNode.isNull() || (valueNode.isTextual() && valueNode.asText().isEmpty())) {
				throw new IllegalArgumentException("El campo '" + key + "' es nulo o vacio.");
			}

		}

		return jsonNode;
	}

	/**
	 * Convierte un string de datos XML a un objeto JSON. * * 
	 * Este método utiliza un {@link XmlMapper} para leer los datos XML y convertirlos en un objeto
	 * {@link JsonNode}. Luego, mapea los atributos * del objeto JSON a un DTO de
	 * orden utilizando el método {@link #mapearAtributos(JsonNode, OrdenDto)}.
	 * @param xmlData el string que contiene los datos en formato XML 
	 * @return un objeto {@link JsonNode} que representa los datos convertidos a JSON
	 * @throws IOException si ocurre un error durante la lectura del XML
	 */
	@Override
	public JsonNode convertToJson(String xmlData) throws IOException {
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode jsonNode = xmlMapper.readTree(xmlData);
		OrdenDto ordenDto = new OrdenDto();
		JsonNode jsonNodeDto = mapearAtributos(jsonNode, ordenDto);
		return jsonNodeDto;
	}

	/**
     * Procesa una orden recibida en formato XML.
     *
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
			JSONObject jsonObject = XML.toJSONObject(peticionXml);

			String tipo = JSONExtractor.extractField(jsonObject, "ReceivingNode").equals("") ? "O" : "T";
			String ordenOriginal = JSONExtractor.extractField(jsonObject, "ParentSalesOrderNo");
			String idOrden = JSONExtractor.extractField(jsonObject, "SalesOrderNo");
			String sucursal = JSONExtractor.extractField(jsonObject, "ReceivingNode");

			JSONObject ordenJson = finOrderTransferInDB( tipo.equals("T") ? ordenOriginal
			 : idOrden, "", sucursal, false);
			log.info("Llamada al servicio findOrderTransferInDB" + ordenJson);
			/*JSONObject ordenJson = new JSONObject();
			// Datos generales
			ordenJson.put("sucursal", "SU1217");
			ordenJson.put("sucursalTraspaso", "");
			ordenJson.put("idPaqueteria", "");
			ordenJson.put("idOrden", "1109402444054");
			ordenJson.put("repartidor", 0);
			ordenJson.put("pedido", 0);
			ordenJson.put("cobroPorEntrega", 0);
			ordenJson.put("status", 0);
			ordenJson.put("tipo", "T");
			ordenJson.put("notas", "");

			// Cliente
			JSONObject cliente = new JSONObject();
			cliente.put("addressLine1", "Avenida Enrique Díaz de León");
			cliente.put("addressLine2", "1043");
			cliente.put("addressLine3", "44100");
			cliente.put("addressLine4", "Lomas de Atemajac");
			cliente.put("addressLine5", "");
			cliente.put("addressLine6", "");
			cliente.put("alternateEmailID", "");
			cliente.put("beeper", "");
			cliente.put("city", "Zapopan");
			cliente.put("company", "");
			cliente.put("country", "México");
			cliente.put("dayFaxNo", "");
			cliente.put("dayPhone", "7711780897");
			cliente.put("department", "");
			cliente.put("eMailID", "");
			cliente.put("eveningFaxNo", "");
			cliente.put("eveningPhone", "");
			cliente.put("firstName", "Ana Karen");
			cliente.put("jobTitle", "");
			cliente.put("lastName", "Alvarez");
			cliente.put("middleName", "Rodriguez");
			cliente.put("mobilePhone", "");
			cliente.put("otherPhone", "");
			cliente.put("state", "Jalisco");
			cliente.put("suffix", "");
			cliente.put("title", "");
			cliente.put("zipCode", "44100");
			cliente.put("personID", 0);

			// Añadir cliente al objeto principal
			ordenJson.put("cliente", cliente);

			// Líneas
			JSONArray lineas = new JSONArray();
			JSONObject linea = new JSONObject();
			linea.put("idOrden", 1300881686);
			linea.put("sucursal", "");
			linea.put("numLinea", 1);

			// Añadir línea al arreglo de líneas
			lineas.put(linea);
			ordenJson.put("lineas", lineas);*/

			Integer pedido = Integer.parseInt(ordenJson.get("pedido").toString());

			if (!ordenJson.isEmpty()) {

				// NOTA: ADD field status with value 0
				jsonObject.put("status", 0);

				if (tipo.equals("T")) {
					insertToMaster(jsonObject, pedido, tipo);
					insertarPagosToMaster(jsonObject, pedido);
					insertFechasMaster(jsonObject, pedido, tipo);
				} else {

					int status = ordenDetalleInt.ordenDetalle(ordenJson);

					if (status == Integer.parseInt(propiedades.getProperty("orden.status"))) {
						insertToMaster(jsonObject, pedido, tipo);
						insertarPagosToMaster(jsonObject, pedido);
						insertFechasMaster(jsonObject, pedido, tipo);

						log.info("Vinculando carrier con ordenoms");
						updateOrdenOriginal(jsonObject);

					} else {

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

				// Verificar si el tipo de mensaje es JSON
				if (envWS.has("type") && envWS.getString("type").equals("application/json")) {
					log.info("Numero De Folio Obtenido desde WebService");
					int ordenfg = envWS.getInt("folio"); // Asumiendo que el folio está en el JSON

					// Establecer el folio en la orden
					log.info("Numero De Folio Obtenido: " + ordenfg);
					jsonObject.put("pedido", ordenfg);

					log.info("Consultando Order Details para obtener campos faltantes");

					// NOTA: que campos devuelve el servicio de finOrderTransferInDB para saber si
					// devuelve los campos con sus nombres correctos
					boolean insertOrdenOriginal = jsonObject.get("tipo").equals("T") ? true : false;

					int status = ordenDetalleInt.ordenDetalle(ordenJson);

					// 5. Lógica para insertar la orden - R
					if (insertOrdenOriginal) {
						log.info("Enviando Orden A WebService Para Ser Insertada En Master");
						insertToMaster(jsonObject, pedido, tipo);
						insertarPagosToMaster(jsonObject, pedido);
						insertFechasMaster(jsonObject, pedido, tipo);
					} else {
						if (status != Integer.valueOf(propiedades.getProperty("orden.status"))) {
							log.info("Enviando Orden A WebService Para Ser Insertada En Master");
							insertToMaster(jsonObject, pedido, tipo);
							insertarPagosToMaster(jsonObject, pedido);
							insertFechasMaster(jsonObject, pedido, tipo);
						} else {
							jsonObject.put("status", propiedades.getProperty("orden.status"));
							log.info(
									"Enviando orden a webservice para ser insertada en master y no ser sincronizada en sucursal ya que cuenta con una linea en backordered");
							insertToMaster(jsonObject, pedido, tipo);
							insertarPagosToMaster(jsonObject, pedido);
							insertFechasMaster(jsonObject, pedido, tipo);
						}
					}
				}
			}

			log.info("Se manda a llamar al servicio parcheBeetractToDeliveryMan");
			log.info("Se imprime idOrden" + idOrden);
			parcheBeetractToDeliveryMan(idOrden);
			// insertXmlRethinkdb(orden.getIdOrden(),orden.getTipo(),xml); - Revisar
			// dudas!!!!
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
		}
		log.info("Terminando método procesarOrden");
	}

	@Override
	public void insertFechasMaster(JSONObject orden, int pedido, String tipo) {
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

			log.info("Iniciando Proceso para Insertar Fechas de la orden " + orden.get("SalesOrderNo").toString());
			log.info("Datos cargados para insertar en tabla de fechas");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info("[RESPONSE][INSERT_FECHAS]: Respuesta servicio: " + response);
			log.info("Finalizado Proceso para Insertar Fechas");

		} catch (Exception e) {
			log.info("Error al insertar fechas: " + e.getMessage());
		}
		log.info(orden.toString());
	}

	@Override
	public void insertarPagosToMaster(JSONObject orden, int pedido) {

		try {
			String url = propiedades.getProperty("web.service.url14");
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("ordenoms",
					URLEncoder.encode(JSONExtractor.extractField(orden, "SalesOrderNo"), StandardCharsets.UTF_8));
			map.add("ordenfg", URLEncoder.encode(String.valueOf(pedido), StandardCharsets.UTF_8));
			map.add("tipo",
					URLEncoder.encode(JSONExtractor.extractField(orden, "SalesOrderNo"), StandardCharsets.UTF_8));
			// DUDA: las formas de pago son por producto o por orden
			map.add("fpago", URLEncoder.encode("24", StandardCharsets.UTF_8));
			map.add("importe", URLEncoder.encode((JSONExtractor.extractField(orden, "importeTotal") == null) ? "0"
					: JSONExtractor.extractField(orden, "importeTotal"), StandardCharsets.UTF_8));

			log.info("Iniciando Proceso para Insertar Pagos de la orden " + orden.get("SalesOrderNo").toString());
			log.info("Datos cargados para insertar en tabla fpagos");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> entity = new HttpEntity<>(map.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info("[RESPONSE][INSERT_PAGOS]: Respuesta servicio: " + response);
			log.info("Finalizado Proceso para Insertar Pagos");
		} catch (Exception e) {
			log.error("Error al insertar pagos: " + e.getMessage());
		}
		log.info(orden.toString());

	}

	@Override
	public void insertToMaster(JSONObject orden, int pedido, String tipo) {
		try {

			log.info("Iniciando Proceso para Insertar Pedido, Productos y Cliente de la orden "
					+ JSONExtractor.extractField(orden, "SalesOrderNo"));
			Boolean isRelease = Integer.parseInt(JSONExtractor.extractField(orden, "ReleaseNo")) != 1 ? true : false;

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
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			log.info("[REQUEST][INSERT_PEDIDO]: Enviando insercion de pedido: " + jsonPayload.toString());
			log.info("[RESPONSE][INSERT_PEDIDO]: Respuesta servicio: " + response.getBody());
			log.info("Finalizado Proceso para Insertar Pedido, Productos y Cliente");

		} catch (Exception ex) {
			log.info("Error al insertar pedido: " + ex.getMessage());
		}
		log.info(orden.toString());
	}

	@Override
	public JSONObject finOrderTransferInDB(String ordenoms, String orden_ot, String almacen, boolean insAlm) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			// Construir la URL dependiendo del valor de insAlm
			String url = propiedades.getProperty("web.service.url24") + ordenoms;
			if (insAlm) {
				url += "&almacen=" + almacen + "&ordenot=" + orden_ot;
			}
			// Enviar la solicitud y recibir la respuesta
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			// Imprimir la respuesta en consola
			log.info(url + " " + response.getBody());
			// Comprobar si la respuesta es JSON
			JSONObject json = new JSONObject(response.getBody());
			if (json.has("orden") && !json.get("orden").toString().isEmpty()) {
				return json; // Retornar el objeto JSON si contiene "orden"
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void updateOrdenOriginal(JSONObject orden) {
		try {
			log.info("Actualizando orden original");

			JSONObject jsonPayload = jsonTransformWs.getObjectOrderMaster(orden, true, false);

			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url9");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(jsonPayload.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

			log.info(response.getBody());

		} catch (Exception e) {
			log.info("Error al actualizar la orden: " + e.getMessage());
		}
	}

	private void parcheBeetractToDeliveryMan(String ordenoms) {
		try {
			log.info("Inicio de consumo de servicio parcheBeetractToDeliveryMan " + ordenoms);

			JSONObject xmlBackup = new JSONObject();
			xmlBackup.put("ordenOMS", ordenoms);

			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url26");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(xmlBackup.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			log.info("Respuesta del servicio: " + response.getBody());
			log.info("Fin de consumo de servicio parcheBeetractToDeliveryMan");
		} catch (Exception e) {
			log.info("Error en parcheBeetractToDeliveryMan: " + e.getMessage());
		}
	}

}
