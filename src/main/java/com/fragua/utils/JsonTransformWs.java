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

	// Obtener json para insertar en master
	public JSONObject getObjectOrderMaster(JSONObject orden, boolean almacen, boolean ordenExistente)
			throws ParseException, JsonProcessingException {

		log.info("Inciando metodo getObjectOrderMaster");
		log.info(orden.toString());

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

		log.info("INFO- Antes de NotesOrden");
		ObjectMapper objectMapper = new ObjectMapper();
		log.info(JSONExtractor.extractField(orden, "NotesOrden"));
		// JSONArray arrayNotes = convertToJsonArray(JSONExtractor.extractField(orden,
		// "NotesOrden"));
		log.info("INFO INFO");
		List<NotesOrden> listaOrden = objectMapper.readValue(new JSONArray().toString(),
				new TypeReference<List<NotesOrden>>() {
				});
		log.info("INFO INFO INFO");

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
		if (Integer.parseInt(JSONExtractor.extractField(orden, "status")) != 8) {
			order.put("status",
					(JSONExtractor.extractField(orden, "ShipNode").substring(0, 2).equals("SU")) ? "6" : "A"); // 6 para sucursal, A para almacen
		} else {
			order.put("status", "8"); // 6 para sucursal, A para almacen
		}
		order.put("cargoservicio", JSONExtractor.extractField(orden, "TotalCharges"));
		order.put("subfijo", JSONExtractor.extractField(orden, ""));
		order.put("torden", 1); // 1 venta, 2 devolucion
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
		client.put("sincronizado", (Integer.parseInt(JSONExtractor.extractField(orden, "status")) != 17 ? 0 : 0));

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
			producto.put("negadas_wms", 0); // Desconcido
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
					(Integer.parseInt(JSONExtractor.extractField(orden, "status")) != 17 ? 0 : 11));
			producto.put("cargo", JSONExtractor.extractField(orden, "Cargo"));
			producto.put("release", JSONExtractor.extractField(orden, "ReleaseNo"));
			productoArray.put(producto);
		});

		order.put("cliente", client);
		order.put("productos", productoArray);
		order.put("sincronizado", (Integer.parseInt(JSONExtractor.extractField(orden, "status")) != 17 ? 0 : 11));
		order.put("exist", ordenExistente);
		orderArray.put(order);
		json.put("pedido", orderArray);
		json.accumulate("requestType", "pedidoEcomm");

		return json;

	}

	public static JSONArray convertToJsonArray(String jsonString) throws JSONException {
		// Primero, intentamos parsear el string como un JSONArray
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			return jsonArray; // Si se puede convertir directamente en JSONArray, lo devolvemos
		} catch (JSONException e) {
			// Si ocurre una excepción, intentamos como JSONObject
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				// Si es un JSONObject, lo convertimos a un JSONArray
				JSONArray jsonArray = new JSONArray();
				jsonArray.put(jsonObject); // Agregamos el JSONObject al JSONArray
				return jsonArray;
			} catch (JSONException ex) {
				// Si también falla aquí, propagamos la excepciónthrow
				new JSONException("Invalid JSON string: " + jsonString);
				return null;
			}

		}
	}

	public String getCambio(List<NotesOrden> notes) {
		String cambio = "0";
		for (int i = 0; i <= notes.size() - 1; i++) {
			if (notes.get(i).getReasonCode().equals("EFECTIVO")) {
				cambio = notes.get(i).getNoteText();
				break;
			}
		}

		cambio = cambio.replace(",", ".");
		if (cambio.contains(".")) {
			cambio = cambio.replaceAll("[^0-9]", "");
			cambio = cambio.substring(0, cambio.length() - 2) + "."
					+ cambio.substring(cambio.length() - 2, cambio.length());
		} else {
			cambio = cambio.replaceAll("[^0-9]", "");
		}
		return cambio;
	}

	public String getNotes(JSONObject jsonObject) {

		JSONArray jsonArrayNotes = convertToJsonArray(JSONExtractor.extractField(jsonObject, "Notes"));
		String noteName = "";
		for (int i = 0; i <= jsonArrayNotes.length() - 1; i++) {

			JSONObject note = jsonArrayNotes.getJSONObject(i);

			if (!JSONExtractor.extractField(note, "ReasonCode").equals("EFECTIVO")
					|| !JSONExtractor.extractField(note, "ReasonCode").equals("CAMBIO")
					|| !JSONExtractor.extractField(note, "ReasonCode").equals("CREDITO FG")
					|| !JSONExtractor.extractField(note, "ReasonCode").equals("TARJETA DE CREDITO")) {
				noteName += "," + JSONExtractor.extractField(note, "NoteText");
				break;
			}
		}
		return noteName;
	}

	public LocalDateTime parseoFecha(String fecha) {

		String format = "yyyy-MM-dd'T'HH:mm:ss";

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(fecha, dateTimeFormatter);
			return localDateTime;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
