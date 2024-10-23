package com.fragua.dto;

import lombok.Setter;
import lombok.Getter;
import java.util.List;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fragua.model.LineaOrden;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDto {

	private String sucursal;
	
    private String sucursalTraspaso;
    
    private String idPaqueteria;
    
    private String idOrden;
    
    private Integer repartidor;
    
    private Integer pedido;
    
    private Double cobroPorEntrega;
    
    private ClienteDto cliente;
    
    private Integer status;
    
    private String tipo;
    
    private String notas;
    
    private List<LineaOrden> lineas;
    
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

}
