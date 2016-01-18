package com.geopista.protocol.cementerios;

import java.io.Serializable;


/**
 * Clase que implementa un objeto de tipo Bloque
 */
public class BloqueBean extends ElemCementerioBean implements Serializable{
	
	private int id_bloque = -1;
	private int tipo_unidades;
	private int numFilas ;
	private int numColumnas;
	private String descripcion;
	private int idElemCementerio;
	private int id_feature;
	
	
	
	public int getId_feature() {
		return id_feature;
	}
	public void setId_feature(int id_feature) {
		this.id_feature = id_feature;
	}
	public int getIdElemCementerio() {
		return idElemCementerio;
	}
	public void setIdElemCementerio(int idElemCementerio) {
		this.idElemCementerio = idElemCementerio;
	}
	public int getId_bloque() {
		return id_bloque;
	}
	public void setId_bloque(int id_bloque) {
		this.id_bloque = id_bloque;
	}
	public int getTipo_unidades() {
		return tipo_unidades;
	}
	public void setTipo_unidades(int tipo_unidades) {
		this.tipo_unidades = tipo_unidades;
	}
	public int getNumFilas() {
		return numFilas;
	}
	public void setNumFilas(int numFilas) {
		this.numFilas = numFilas;
	}
	public int getNumColumnas() {
		return numColumnas;
	}
	public void setNumColumnas(int numColumnas) {
		this.numColumnas = numColumnas;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

	
		
}
