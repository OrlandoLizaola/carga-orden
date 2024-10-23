package com.fragua.service;

import java.io.IOException;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fragua.dto.OrdenDto;

public interface CargaOrdenInt {

    public JsonNode mapearAtributos(JsonNode jsonNode, OrdenDto ordenDto);

    public JsonNode convertToJson(String xmlData) throws IOException;

    public void procesarOrden(String peticionXml);

    public void insertToMaster(JSONObject orden, int pedido, String tipo);

    public void insertFechasMaster(JSONObject orden, int pedido, String tipo);

    public void insertarPagosToMaster(JSONObject orden, int pedido);

    public JSONObject finOrderTransferInDB(String ordenoms, String orden_ot, String almacen, boolean insAlm);
    
}
