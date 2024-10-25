package com.fragua.service;

import org.json.JSONObject;

public interface CargaOrdenInt {

    public void procesarOrden(String peticionXml);

    public void insertToMaster(JSONObject orden, int pedido, String tipo);

    public void insertFechasMaster(JSONObject orden, int pedido, String tipo);

    public void insertarPagosToMaster(JSONObject orden, int pedido);

    public JSONObject finOrderTransferInDB(String ordenoms, String orden_ot, String almacen, boolean insAlm);
    
}
