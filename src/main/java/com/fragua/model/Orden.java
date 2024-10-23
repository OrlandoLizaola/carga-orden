package com.fragua.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Felipe_Pulido
 */
@XmlRootElement
public class Orden {
    
    private String sucursal;
    private String sucursalTraspaso;
    private Integer idPaqueteria;
    private String idOrden;
    private Integer repartidor;
    private Integer pedido;
    private Double cobroPorEntrega;
    private Cliente cliente;
    private Integer status;
    private String tipo;
    private String notas;
    private List<LineaOrden> lineas = new ArrayList();
    private List<NotesOrden> notesOrden = new ArrayList();
    private String fimpresion;
    private String fprogramada;
    private String fembarque;
    private String faviso;
    private String fcreacion;
    private String fcambio;
    private String hora;
    private String service;
    private String tentrega;
    private String carrierid;
    private String subfijo;
    private String ordenOriginal;
    private Integer release;
    private String cambio;
    private String msi;
    private String importe;
    private String fpago;
    private String canal;



    public Orden() {
    }

    public String getOrdenOriginal() {
        return ordenOriginal;
    }

    @XmlElement
    public void setOrdenOriginal(String ordenOriginal) {
        this.ordenOriginal = ordenOriginal;
    }

    public String getHora() {
        return hora;
    }
    
    @XmlElement
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSubfijo() {
        return subfijo;
    }

    public void setSubfijo(String subfijo) {
        this.subfijo = subfijo;
    }

    
    public String getCarrierid() {
        return carrierid;
    }

    @XmlElement
    public void setCarrierid(String carrierid) {
        this.carrierid = carrierid;
    }

    public String getTentrega() {
        return tentrega;
    }
    
    @XmlElement
    public void setTentrega(String tentrega) {
        this.tentrega = tentrega;
    }

    public String getService() {
        return service;
    }

    @XmlElement
    public void setService(String service) {
        this.service = service;
    }

    
	
    public String getSucursalTraspaso() {
        return sucursalTraspaso;
    }

    @XmlElement
    public void setSucursalTraspaso(String sucursalTraspaso) {
        this.sucursalTraspaso = sucursalTraspaso;
    }

    public Integer getIdPaqueteria() {
        return idPaqueteria;
    }

    @XmlElement
    public void setIdPaqueteria(Integer idPaqueteria) {
        this.idPaqueteria = idPaqueteria;
    }

    public Integer getRepartidor() {
        return repartidor;
    }

    @XmlElement
    public void setRepartidor(Integer repartidor) {
        this.repartidor = repartidor;
    }

    public Integer getPedido() {
        return pedido;
    }

    @XmlElement
    public void setPedido(Integer pedido) {
        this.pedido = pedido;
    }

    public Double getCobroPorEntrega() {
        return cobroPorEntrega;
    }

    @XmlElement
    public void setCobroPorEntrega(Double cobroPorEntrega) {
        this.cobroPorEntrega = cobroPorEntrega;
    }

    public String getNotas() {
        return notas;
    }

    @XmlElement
    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getIdOrden() {
        return idOrden;
    }

    @XmlElement
    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getSucursal() {
        return sucursal;
    }

    @XmlElement
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @XmlElement
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getStatus() {
        return status;
    }

    @XmlElement
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    @XmlElement
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public List<LineaOrden> getLineas() {
        return lineas;
    }

    @XmlElement
    public void setLineas(List<LineaOrden> lineas) {
        this.lineas = lineas;
    }

    public List<NotesOrden> getNotesOrden() {
        return notesOrden;
    }
    
    @XmlElement
    public void setNotesOrden(List<NotesOrden> notes) {
        this.notesOrden = notes;
    }
    
    public String getFimpresion() {
        return fimpresion;
    }

    @XmlElement
    public void setFimpresion(String fimpresion) {
        this.fimpresion = fimpresion;
    }

    public String getFcreacion() {
        return fcreacion;
    }

    @XmlElement
    public void setFcreacion(String fcreacion) {
        this.fcreacion = fcreacion;
    }

    public String getFcambio() {
        return fcambio;
    }

    @XmlElement
    public void setFcambio(String fcambio) {
        this.fcambio = fcambio;
    }

    public String getFprogramada() {
        return fprogramada;
    }

    @XmlElement
    public void setFprogramada(String fprogramada) {
        this.fprogramada = fprogramada;
    }
    
    public String getFembarque() {
        return fembarque;
    }

    @XmlElement
    public void setFembarque(String fembarque) {
        this.fembarque = fembarque;
    }

    public String getFaviso() {
        return faviso;
    }

    @XmlElement
    public void setFaviso(String faviso) {
        this.faviso = faviso;
    }

    public Integer getRelease() {
        return release;
    }

    @XmlElement
    public void setRelease(Integer release) {
        this.release = release;
    }

    public String getCambio() {
        return cambio;
    }

    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    public String getMsi() {
        return msi;
    }

    public void setMsi(String msi) {
        this.msi = msi;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getFpago() {
        return fpago;
    }

    public void setFpago(String fpago) {
        this.fpago = fpago;
    }
    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    @Override
    public String toString() {
        return "Orden{" +
                "sucursal='" + sucursal + '\'' +
                ", sucursalTraspaso='" + sucursalTraspaso + '\'' +
                ", idPaqueteria=" + idPaqueteria +
                ", idOrden='" + idOrden + '\'' +
                ", repartidor=" + repartidor +
                ", pedido=" + pedido +
                ", cobroPorEntrega=" + cobroPorEntrega +
                ", cliente=" + cliente +
                ", status=" + status +
                ", tipo='" + tipo + '\'' +
                ", notas='" + notas + '\'' +
                ", lineas=" + lineas +
                ", notesOrden=" + notesOrden +
                ", fimpresion='" + fimpresion + '\'' +
                ", fprogramada='" + fprogramada + '\'' +
                ", fembarque='" + fembarque + '\'' +
                ", faviso='" + faviso + '\'' +
                ", fcreacion='" + fcreacion + '\'' +
                ", fcambio='" + fcambio + '\'' +
                ", hora='" + hora + '\'' +
                ", service='" + service + '\'' +
                ", tentrega='" + tentrega + '\'' +
                ", carrierid='" + carrierid + '\'' +
                ", subfijo='" + subfijo + '\'' +
                ", ordenOriginal='" + ordenOriginal + '\'' +
                ", release=" + release +
                ", cambio='" + cambio + '\'' +
                ", msi='" + msi + '\'' +
                ", importe='" + importe + '\'' +
                ", fpago='" + fpago + '\'' +
                ", canal='" + canal + '\'' +
                '}';
    }
}