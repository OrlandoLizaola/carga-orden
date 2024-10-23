package com.fragua.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JSONExtractor {

    public static String extractField(JSONObject jsonObject, String fieldName) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (key.equals(fieldName)) {
                return value.toString();
            }

            if (value instanceof JSONObject) {
                String result = extractField((JSONObject) value, fieldName);
                if (result != null) {
                    return result; 
                }
            }

            if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object element = array.get(i);
                    if (element instanceof JSONObject) {
                        String result = extractField((JSONObject) element, fieldName);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

}
