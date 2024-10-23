package com.fragua.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LineaOrdenDto {
	
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

}
