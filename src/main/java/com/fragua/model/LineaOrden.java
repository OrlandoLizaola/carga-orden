package com.fragua.model;

public class LineaOrden {
    
    private Integer idOrden;
    private Integer sucursal;
    private Integer numLinea;
    private Integer producto;
    private Double precioVenta;
    private Double precioLista;
    private Double cantidad;
    private Double precioBase;
    private String status;
    private Integer pedido;
    private String descripcion;
    private String cup;
    private Double pedidas;
    private Double surtidas;
    private String productClass;
    private String peso;
    private String uom;
    private String promocion;
    private String cargo;
    private String fpago;
    
    public LineaOrden(Integer idOrden, Integer sucursal, Integer numLinea, Integer producto, Double precioVenta, Double precioLista, Double cantidad, Double precioBase, String status, Integer pedido, String descripcion,String cup,Double pedidas,Double surtidas, String productClass, String peso, String uom, String cargo) {
        this.idOrden = idOrden;
        this.sucursal = sucursal;
        this.numLinea = numLinea;
        this.producto = producto;
        this.precioVenta = precioVenta;
        this.precioLista = precioLista;
        this.cantidad = cantidad;
        this.precioBase = precioBase;
        this.status = status;
        this.pedido = pedido;
        this.descripcion = descripcion;
        this.cup = cup;
        this.pedidas = pedidas;
        this.surtidas = surtidas;
        this.productClass = productClass;
        this.peso = peso;
        this.uom = uom;
        this.cargo = cargo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public Double getSurtidas() {
        return surtidas;
    }

    public void setSurtidas(Double surtidas) {
        this.surtidas = surtidas;
    }

    public Double getPedidas() {
        return pedidas;
    }

    public void setPedidas(Double pedidas) {
        this.pedidas = pedidas;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LineaOrden() {
    	
    }

    public Integer getPedido() {
        return pedido;
    }

    public void setPedido(Integer pedido) {
        this.pedido = pedido;
    }
    
    public Integer getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Integer idOrden) {
        this.idOrden = idOrden;
    }

    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal(Integer sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getPrecioLista() {
        return precioLista;
    }

    public void setPrecioLista(Double precioLista) {
        this.precioLista = precioLista;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFpago() {
        return fpago;
    }

    public void setFpago(String fpago) {
        this.fpago = fpago;
    }
}