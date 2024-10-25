package com.fragua.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fragua.model.NotesOrden;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JsonTransformWs {

	/**
	 * Crea un objeto JSON que representa una orden a partir de la información proporcionada en un objeto JSON existente.
	 * Este método extrae datos de la orden, del cliente y de los productos, y los organiza en un
	 * formato JSON específico que se utilizará para su posterior procesamiento. Se realizan conversiones
	 * de fecha y se manejan notas y cambios asociados a la orden.
	 * @param orden el objeto JSON que contiene la información de la orden original.
	 * @param almacen un valor booleano que indica si se está utilizando información del almacén.
	 * @param ordenExistente un valor booleano que indica si la orden ya existe en la base de datos.
	 * @return un objeto JSONObject que contiene la información estructurada de la orden, cliente y productos.
	 * @throws ParseException si hay un error al parsear las fechas.
	 * @throws JsonProcessingException si hay un error al procesar la conversión a JSON.
	 */
	public JSONObject getObjectOrderMaster(JSONObject orden, boolean almacen, boolean ordenExistente)
			throws ParseException, JsonProcessingException {

		log.info("Iniciando método getObjectOrderMaster");
		log.info("Orden recibida: {}", orden.toString());

		JSONObject json = new JSONObject();
		JSONObject order = new JSONObject();
		JSONObject client = new JSONObject();
		JSONArray orderArray = new JSONArray();
		JSONArray productoArray = new JSONArray();

		SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		SimpleDateFormat formatOriginal = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatHour = new SimpleDateFormat("HHmm");
		SimpleDateFormat formatHour2 = new SimpleDateFormat("HH:mm");
		Date today = new Date();

		log.info("INFO - Antes de NotesOrden");
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			log.debug("Extrayendo NotesOrden: {}", JSONExtractor.extractField(orden, "NotesOrden"));
			List<NotesOrden> listaOrden = objectMapper.readValue(new JSONArray().toString(),
					new TypeReference<List<NotesOrden>>() {
					});
			log.info("Lista de notas orden extraída, tamaño: {}", listaOrden.size());

			JSONArray arraySALines = convertToJsonArray(JSONExtractor.extractField(orden, "SALines"));

			// Datos Orden
			order.put("pedidoid", JSONExtractor.extractField(orden, "Pedido"));
			order.put("orden", JSONExtractor.extractField(orden, "SalesOrderNo"));
			order.put("forden", JSONExtractor.extractField(orden, "OrderDate").substring(0,
					JSONExtractor.extractField(orden, "OrderDate").length() - 8) + formatHour2.format(today));
			order.put("pedido", JSONExtractor.extractField(orden, "Pedido"));
			order.put("fecha",
					(!almacen) ? JSONExtractor.extractField(orden, "OrderDate")
							: JSONExtractor.extractField(orden, "OrderDate").substring(0,
							JSONExtractor.extractField(orden, "OrderDate").length() - 9));
			order.put("hora", formatHour.format(today));
			order.put("almacen", JSONExtractor.extractField(orden, "ShipNode"));
			order.put("fasurtir",
					(JSONExtractor.extractField(orden, "ExtnDateDeliveryApproximate").equals("")
							? formatOriginal.format(parseoFecha(JSONExtractor.extractField(orden, "ReqDeliveryDate"))) + "T"
							+ formatHour2.format(DateUtils.addHours(today, 1))
							: JSONExtractor.extractField(orden, "ExtnDateDeliveryApproximate").substring(0,
							JSONExtractor.extractField(orden, "ExtnDateDeliveryApproximate").length() - 5)));
			order.put("origen", "J");
			order.put("concepto", "D");
			order.put("tipo", JSONExtractor.extractField(orden, "ReceivingNode").equals("") ? "O" : "T");
			order.put("tentrega", JSONExtractor.extractField(orden, "FulfillmentType"));
			order.put("sucursal", (!almacen) ? JSONExtractor.extractField(orden, "ShipNode")
					: JSONExtractor.extractField(orden, "ReceivingNode"));
			order.put("sucdestino", JSONExtractor.extractField(orden, "ReceivingNode"));
			order.put("nprodtos", arraySALines.length());
			order.put("servicioid", JSONExtractor.extractField(orden, "ExtnServiceId"));
			order.put("carrierid", JSONExtractor.extractField(orden, "SCAC"));
			order.put("carrier", ""); // Desconocido
			order.put("falta", JSONExtractor.extractField(orden, "OrderDate"));
			order.put("fmod", formatDay.format(today));

			// Estatus de la orden
			int status = Integer.parseInt(JSONExtractor.extractField(orden, "status"));
			if (status != 8) {
				order.put("status", (JSONExtractor.extractField(orden, "ShipNode").substring(0, 2).equals("SU")) ? "6" : "A");
				log.debug("Estado de la orden asignado: {}", order.get("status"));
			} else {
				order.put("status", "8");
				log.debug("Estado de la orden asignado: 8");
			}

			order.put("cargoservicio", JSONExtractor.extractField(orden, "TotalCharges"));
			order.put("subfijo", JSONExtractor.extractField(orden, ""));
			order.put("torden", 1); // 1 venta, 2 devolución
			order.put("ordenoriginal", JSONExtractor.extractField(orden, ""));
			order.put("notas", getNotes(orden));
			order.put("cambio",
					(listaOrden.size() > 0) ? getCambio(listaOrden) : JSONExtractor.extractField(orden, "cambio"));
			order.put("release", JSONExtractor.extractField(orden, "ReleaseNo"));
			order.put("msi", JSONExtractor.extractField(orden, "msi"));
			order.put("importe", JSONExtractor.extractField(orden, "importe"));

			// Datos Cliente
			client.put("ordenfg", JSONExtractor.extractField(orden, "ordenfg"));
			client.put("ordenoms", JSONExtractor.extractField(orden, "SalesOrderNo"));
			client.put("tipo", JSONExtractor.extractField(orden, "ReceivingNode").equals("") ? "O" : "T");
			client.put("clienteid", JSONExtractor.extractField(orden, "PersonID"));
			client.put("nombre", JSONExtractor.extractField(orden, "FirstName"));
			client.put("nombremedio", JSONExtractor.extractField(orden, "MiddleName"));
			client.put("apellido", JSONExtractor.extractField(orden, "LastName"));
			client.put("sufijo", JSONExtractor.extractField(orden, "Suffix"));
			client.put("titulo", "");
			client.put("pais", JSONExtractor.extractField(orden, "Country"));
			client.put("entidad", JSONExtractor.extractField(orden, "State"));
			client.put("ciudad", JSONExtractor.extractField(orden, "City"));
			client.put("direccion1", JSONExtractor.extractField(orden, "AddressLine1"));
			client.put("direccion2", JSONExtractor.extractField(orden, "AddressLine2") + " int "
					+ JSONExtractor.extractField(orden, "AddressLine3"));
			client.put("compania", JSONExtractor.extractField(orden, "Company"));
			client.put("departamento", JSONExtractor.extractField(orden, "Department"));
			client.put("titulotrabajo", JSONExtractor.extractField(orden, "JobTitle"));
			client.put("telefono", JSONExtractor.extractField(orden, "DayPhone"));
			client.put("telefonomovil", JSONExtractor.extractField(orden, "OtherPhone"));
			client.put("telefonodia", JSONExtractor.extractField(orden, "MobilePhone"));
			client.put("telefononoche", JSONExtractor.extractField(orden, "EveningPhone"));
			client.put("email1", JSONExtractor.extractField(orden, "EmailID"));
			client.put("email2", JSONExtractor.extractField(orden, "AlternateEmailID"));
			client.put("rfc", "");
			client.put("latitude", "0.0");
			client.put("longitude", "0.0");
			client.put("cp", JSONExtractor.extractField(orden, "ZipCode"));
			client.put("sincronizado", (status != 17 ? 0 : 0));

			JSONArray lineas = convertToJsonArray(JSONExtractor.extractField(orden, "SALines"));

			// Datos Productos
			lineas.forEach((pr) -> {
				JSONObject producto = new JSONObject();

				producto.put("id", String.valueOf(JSONExtractor.extractField(orden, "Pedido"))
						+ String.valueOf(JSONExtractor.extractField(orden, "Producto")));
				producto.put("pedidoid", JSONExtractor.extractField(orden, "Pedido"));
				producto.put("ordenoms", JSONExtractor.extractField(orden, "SalesOrderNo"));
				producto.put("producto", JSONExtractor.extractField(orden, "Producto"));
				producto.put("linea", JSONExtractor.extractField(orden, "NumLinea"));
				producto.put("cup", JSONExtractor.extractField(orden, "Cup"));
				producto.put("pdescrip", JSONExtractor.extractField(orden, "Descripcion"));
				producto.put("pedidas", JSONExtractor.extractField(orden, "Pedidas"));
				producto.put("negadas_alm", 0); // Desconocido
				producto.put("negadas_wms", 0); // Desconocido
				producto.put("surtidas", 0);
				producto.put("orden", JSONExtractor.extractField(orden, "SalesOrderNo"));
				producto.put("cantidad", JSONExtractor.extractField(orden, "Cantidad"));
				producto.put("cant_surtido", 0d); // Desconocido
				producto.put("cant_cancelado", 0d); // Desconocido
				producto.put("cant_devuelto", 0d); // Desconocido
				producto.put("precio_lista", JSONExtractor.extractField(orden, "PrecioLista"));
				producto.put("precio_base", JSONExtractor.extractField(orden, "PrecioBase"));
				producto.put("precio_venta", JSONExtractor.extractField(orden, "PrecioVenta"));
				producto.put("iva_tipo", 0); // Desconocido
				producto.put("iva_tasa", 0d); // Desconocido
				producto.put("iva_importe", 0d); // Desconocido
				producto.put("productclass", JSONExtractor.extractField(orden, "ProductClass"));
				producto.put("peso", JSONExtractor.extractField(orden, "ProductClass"));
				producto.put("uom", JSONExtractor.extractField(orden, ""));
				producto.put("promocion", JSONExtractor.extractField(orden, "promocion"));
				producto.put("sincronizado",
						(status != 17 ? 0 : 11));
				producto.put("cargo", JSONExtractor.extractField(orden, "Cargo"));
				producto.put("release", JSONExtractor.extractField(orden, "ReleaseNo"));
				productoArray.put(producto);
			});

			order.put("cliente", client);
			order.put("productos", productoArray);
			order.put("sincronizado", (status != 17 ? 0 : 11));
			order.put("exist", ordenExistente);
			orderArray.put(order);
			json.put("pedido", orderArray);
			json.accumulate("requestType", "pedidoEcomm");

			log.info("Objeto de pedido creado exitosamente.");
		} catch (Exception e) {
			log.error("Error al crear objeto de pedido: {}", e.getMessage(), e);
			throw e; // Re-lanzamos la excepción después de registrarla
		}

		return json;
	}

	/**
	 * Convierte una cadena JSON a un JSONArray.
	 * Este método intenta parsear la cadena proporcionada como un JSONArray.
	 * Si la conversión falla y la cadena representa un JSONObject, este se agrega a un
	 * nuevo JSONArray y se devuelve. Si ambos intentos fallan, se lanza una excepción
	 * indicando que la cadena JSON es inválida.
	 * @param jsonString la cadena JSON que se desea convertir
	 * @return un JSONArray que contiene los elementos de la cadena proporcionada,
	o un JSONArray con el JSONObject si la cadena era un JSONObject
	 * @throws JSONException si la cadena no es un JSON válido
	 */
	public static JSONArray convertToJsonArray(String jsonString) throws JSONException {
		log.debug("Iniciando la conversión de string JSON a JSONArray: {}", jsonString);

		try {
			// Intentamos parsear el string como un JSONArray
			JSONArray jsonArray = new JSONArray(jsonString);
			log.info("Conversión exitosa a JSONArray.");
			return jsonArray; // Devolvemos el JSONArray si se convierte correctamente
		} catch (JSONException e) {
			log.warn("No se pudo convertir el string a JSONArray, intentando como JSONObject: {}", e.getMessage());

			// Si ocurre una excepción, intentamos como JSONObject
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				// Si es un JSONObject, lo convertimos a un JSONArray
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(jsonObject); // Agregamos el JSONObject al JSONArray
				log.info("Conversión exitosa de JSONObject a JSONArray.");
				return jsonArray;
			} catch (JSONException ex) {
				log.error("Error al convertir el string a JSONObject: {}", ex.getMessage(), ex);
				throw new JSONException("Invalid JSON string: " + jsonString);
			}
		}
	}

	/**
	 * Obtiene el valor de cambio asociado a notas con el código de razón "EFECTIVO".
	 * Este método recorre una lista de objetos `NotesOrden` y busca la primera nota
	 * cuyo código de razón sea "EFECTIVO". Si se encuentra, se formatea el texto
	 * de la nota para asegurar que representa un valor numérico correcto, utilizando
	 * un punto como separador decimal. Si no se encuentra ninguna nota con el
	 * código "EFECTIVO", se devuelve "0".
	 * @param notes la lista de notas a procesar
	 * @return el valor de cambio como cadena formateada, o "0" si no se encuentra
	ninguna nota válida
	 */
	public String getCambio(List<NotesOrden> notes) {
		log.debug("Iniciando el procesamiento de notas para calcular el cambio.");

		String cambio = "0";
		try {
			for (int i = 0; i < notes.size(); i++) {  // Cambiado <= por < para evitar IndexOutOfBounds
				log.debug("Procesando nota en índice {}: {}", i, notes.get(i));

				if (notes.get(i).getReasonCode().equals("EFECTIVO")) {
					cambio = notes.get(i).getNoteText();
					log.info("Cambio encontrado: {}", cambio);
					break;
				}
			}

			cambio = cambio.replace(",", ".");
			if (cambio.contains(".")) {
				cambio = cambio.replaceAll("[^0-9]", "");
				cambio = cambio.substring(0, cambio.length() - 2) + "."
						+ cambio.substring(cambio.length() - 2);
				log.debug("Cambio formateado con decimales: {}", cambio);
			} else {
				cambio = cambio.replaceAll("[^0-9]", "");
				log.debug("Cambio sin decimales: {}", cambio);
			}
		} catch (Exception e) {
			log.error("Error al procesar el cambio: {}", e.getMessage(), e);
		}

		log.info("Cambio final: {}", cambio);
		return cambio;
	}

	/**
	 * Obtiene una cadena de notas filtrando por códigos de razón específicos.
	 * Este método recorre una lista de objetos `NotesOrden` y agrega el texto de la nota
	 * a la cadena de salida si el código de razón no es uno de los siguientes:
	 * "EFECTIVO", "CAMBIO", "CREDITO FG" o "TARJETA DE CREDITO". La primera nota que
	 * cumple esta condición se agrega y el ciclo se interrumpe.
	 * @param notes la lista de notas a procesar
	 * @return una cadena con el texto de la primera nota que no coincide con los códigos de razón excluidos,
	o una cadena vacía si no se encuentra ninguna nota válida
	 */
	public String getNotes(JSONObject jsonObject) {
		log.debug("Iniciando el procesamiento de notas para el objeto JSON: {}", jsonObject);

		JSONArray jsonArrayNotes = convertToJsonArray(JSONExtractor.extractField(jsonObject, "Notes"));
		String noteName = "";

		try {
			for (int i = 0; i <= jsonArrayNotes.length() - 1; i++) {
				JSONObject note = jsonArrayNotes.getJSONObject(i);
				String reasonCode = JSONExtractor.extractField(note, "ReasonCode");
				log.debug("Procesando nota con ReasonCode: {}", reasonCode);

				if (!reasonCode.equals("EFECTIVO") &&
						!reasonCode.equals("CAMBIO") &&
						!reasonCode.equals("CREDITO FG") &&
						!reasonCode.equals("TARJETA DE CREDITO")) {

					noteName += "," + JSONExtractor.extractField(note, "NoteText");
					log.info("Nota añadida: {}", JSONExtractor.extractField(note, "NoteText"));
					break;
				}
			}
		} catch (Exception e) {
			log.error("Error al procesar las notas: {}", e.getMessage(), e);
		}

		log.info("Notas procesadas: {}", noteName);
		return noteName;
	}

	/**
	 * Convierte una cadena de texto en formato de fecha y hora a un objeto LocalDateTime.
	 * Este método utiliza el formato específico "yyyy-MM-dd'T'HH:mm:ss" para analizar la cadena
	 * proporcionada. Si la cadena no coincide con el formato esperado, se captura la excepción
	 * y se devuelve null.
	 * @param fecha la cadena que representa la fecha y hora en formato "yyyy-MM-dd'T'HH:mm:ss"
	 * @return un objeto LocalDateTime que representa la fecha y hora, o null si la cadena no es válida
	 */
	public LocalDateTime parseoFecha(String fecha) {
		String format = "yyyy-MM-dd'T'HH:mm:ss";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

		log.debug("Iniciando el parseo de la fecha: {}", fecha);

		try {
			LocalDateTime localDateTime = LocalDateTime.parse(fecha, dateTimeFormatter);
			log.info("Fecha parseada exitosamente: {}", localDateTime);
			return localDateTime;
		} catch (Exception e) {
			log.error("Error al parsear la fecha '{}': {}", fecha, e.getMessage(), e);
			return null;
		}
	}

}
