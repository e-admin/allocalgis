package com.geopista.protocol.cementerios;

import java.io.Serializable;

/**
 * Clase que implementa un objeto de tipo Tarifa
 */
public class TarifaBean extends ElemCementerioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int tipo_unidad = -1;
	private int tipo_calculo = -1;
	private String concepto;
	private int tipo_tarifa;
	private String precio;
	private int id_tarifa = -1;
	

	public int getId_tarifa() {
		return id_tarifa;
	}
	public void setId_tarifa(int id_tarifa) {
		this.id_tarifa = id_tarifa;
	}
	public int getTipo_calculo() {
		return tipo_calculo;
	}
	public void setTipo_calculo(int tipo_calculo) {
		this.tipo_calculo = tipo_calculo;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public int getTipo_unidad() {
		return tipo_unidad;
	}
	public void setTipo_unidad(int tipo_unidad) {
		this.tipo_unidad = tipo_unidad;
	}
	public int getTipo_tarifa() {
		return tipo_tarifa;
	}
	public void setTipo_tarifa(int tipo_tarifa) {
		this.tipo_tarifa = tipo_tarifa;
	}
	
	
}
