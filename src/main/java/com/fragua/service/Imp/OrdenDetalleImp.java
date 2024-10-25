package com.fragua.service.Imp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fragua.service.OrdenDetalleInt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdenDetalleImp implements OrdenDetalleInt {

	@Autowired
	private Environment propiedades;

	/**
	 * Obtiene los detalles de una orden mediante una llamada a un servicio web.
	 *
	 * @param orden el identificador de la orden cuyas detalles se desean obtener.
	 * @return un objeto JSONObject que contiene los detalles de la orden, o null si ocurre un error.
	 */
	@Override
	public JSONObject OrdenDetalleInt(String orden) {
		log.debug("Iniciando la obtención de detalles de la orden: {}", orden);
		JSONObject orderDetail = null;
		String jsonPayload = "{\"orden\": \"" + orden + "\"}";
		log.info("Payload JSON enviado: {}", jsonPayload);

		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url25");
			log.debug("URL del servicio: {}", url);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
			log.debug("Realizando la solicitud POST con el payload: {}", jsonPayload);

			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
			log.debug("Respuesta recibida: {}", response);

			if (response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
				log.info("Contenido de la respuesta: {}", response.getBody());
				orderDetail = new JSONObject(response.getBody());
			} else {
				log.warn("El tipo de contenido de la respuesta no es JSON: {}", response.getHeaders().getContentType());
			}
		} catch (Exception e) {
			log.error("Error al obtener los detalles de la orden: {}", e.getMessage(), e);
		}
		return orderDetail;
	}

	/**
	 * Obtiene el estado de los detalles de una orden a partir de un objeto JSON que representa la orden.
	 *
	 * @param orden un objeto JSONObject que contiene información sobre la orden, incluyendo el número de orden de venta.
	 * @return un entero que representa el estado de la orden; devuelve 0 si no se encuentra el estado.
	 */
	@Override
	public int ordenDetalle(JSONObject orden) {
		log.debug("Iniciando el procesamiento de la orden: {}", orden);
		int status = 0;

		try {
			String salesOrderNo = orden.get("SalesOrderNo").toString();
			log.info("Número de orden de venta recibido: {}", salesOrderNo);

			JSONObject orderDetail = OrdenDetalleInt(salesOrderNo);

			if (orderDetail != null) {
				status = orderDetail.optInt("status", 0); // Usa optInt para evitar excepciones
				log.info("Estado de la orden obtenido: {}", status);
			} else {
				log.warn("No se obtuvieron detalles para la orden: {}", salesOrderNo);
			}
		} catch (Exception e) {
			log.error("Error al procesar la orden: {}", e.getMessage(), e);
		}

		return status;
	}
}