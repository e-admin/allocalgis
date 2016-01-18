package com.geopista.server.database.validacion.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ValidacionesMPTBean implements Serializable{
	
	private int id;
	private String nombre;
	private String tabla;
	private ArrayList lstvalidacuadros = new ArrayList();
	
	
	public ArrayList getLstvalidacuadros() {
		return lstvalidacuadros;
	}
	public void setLstvalidacuadros(ArrayList lstvalidacuadros) {
		this.lstvalidacuadros = lstvalidacuadros;
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
	
	
}
