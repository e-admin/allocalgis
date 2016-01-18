package com.geopista.server.database.cuadros;

import java.io.Serializable;

public class CuadrosMPTBean implements Serializable{
	
	private int id;
	private String namejrxml;
	private String nombre;
	private String tabla;
	private String tablasec;
	
	public String getNamejrxml() {
		return namejrxml;
	}
	public void setNamejrxml(String namejrxml) {
		this.namejrxml = namejrxml;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getTablasec() {
		return tablasec;
	}
	public void setTablasec(String tablasec) {
		this.tablasec = tablasec;
	}
	
	
}
