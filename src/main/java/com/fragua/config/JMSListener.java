package com.fragua.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.fragua.service.CargaOrdenInt;

@Component
@Slf4j
public class JMSListener {

	@Autowired
	private CargaOrdenInt servicio;

	/**
	 * Escucha mensajes de una cola JMS y procesa cada mensaje recibido.
	 * Este método es invocado automáticamente cuando se recibe un mensaje en la cola especificada.
	 * Registra el contenido del mensaje y lo envía al servicio para su procesamiento.
	 * @param peticionXml el mensaje en formato String recibido de la cola
	 * return void
	 */
	@JmsListener(destination = "${spring.artemis.embedded.queues}", containerFactory = "jmsListenerContainerFactory")
	public void onMessage(String peticionXml) {
		log.info("Mensaje recibido de la cola: {}", peticionXml);

		try {
			log.debug("Iniciando el procesamiento de la orden.");
			servicio.procesarOrden(peticionXml);
			log.info("Orden procesada exitosamente.");
		} catch (Exception e) {
			log.error("Error al procesar la orden: {}", e.getMessage(), e);
		}
	}

}
