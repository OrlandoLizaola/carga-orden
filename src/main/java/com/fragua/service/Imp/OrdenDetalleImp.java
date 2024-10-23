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

	@Override
	public JSONObject OrdenDetalleInt(String orden) {
		JSONObject orderDetail = null;
		String jsonPayload = "{\"orden\": \"" + orden + "\"}";

		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = propiedades.getProperty("web.service.url25");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
				log.info(response.getBody());
				orderDetail = new JSONObject(response.getBody());
			}
		} catch (Exception e) {
			log.info("Error al obtener los detalles de la orden: " + e.getMessage());
		}
		return orderDetail;
	}

	@Override
	public int ordenDetalle(JSONObject orden) {
		int status = 0;
		JSONObject orderDetail = OrdenDetalleInt(orden.get("SalesOrderNo").toString());

		status = orderDetail.optInt("status", 0); // Usa optInt para evitar excepciones

		return status;
	}

}
