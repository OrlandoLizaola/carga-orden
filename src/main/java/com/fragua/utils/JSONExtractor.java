package com.fragua.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JSONExtractor {

    /**
     * Extrae el valor de un campo específico de un objeto JSON.
     * Este método busca recursivamente en un objeto JSON y en cualquier objeto
     * anidado o array que contenga, devolviendo el primer valor encontrado que
     * coincide con el nombre del campo especificado.
     *
     * @param jsonObject el objeto JSON del cual extraer el campo
     * @param fieldName  el nombre del campo a buscar
     * @return el valor del campo como cadena, o null si no se encuentra
     */
    public static String extractField(JSONObject jsonObject, String fieldName) {
        log.debug("Iniciando la extracción del campo: {}", fieldName);

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            log.debug("Procesando clave: {}, valor: {}", key, value);

            if (key.equals(fieldName)) {
                log.info("Campo encontrado: {}, valor: {}", fieldName, value);
                return value.toString();
            }

            if (value instanceof JSONObject) {
                log.debug("Valor es un JSONObject, realizando llamada recursiva.");
                String result = extractField((JSONObject) value, fieldName);
                if (result != null) {
                    return result;
                }
            }

            if (value instanceof JSONArray) {
                log.debug("Valor es un JSONArray, iterando sobre elementos.");
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object element = array.get(i);
                    if (element instanceof JSONObject) {
                        log.debug("Elemento en índice {} es un JSONObject, realizando llamada recursiva.", i);
                        String result = extractField((JSONObject) element, fieldName);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }

        log.warn("Campo no encontrado: {}", fieldName);
        return null;
    }
}