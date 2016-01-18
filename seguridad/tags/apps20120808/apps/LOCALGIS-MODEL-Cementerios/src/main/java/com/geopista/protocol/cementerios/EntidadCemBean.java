package com.geopista.protocol.cementerios;

import java.io.Serializable;

/**
 * Clase que implementa un objeto de tipo cementerio 
 */
public class EntidadCemBean extends ElemCementerioBean implements Serializable {
	
    private String nombre;
    private String descripcion;
    private String idMunicipio;

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(String idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

}
