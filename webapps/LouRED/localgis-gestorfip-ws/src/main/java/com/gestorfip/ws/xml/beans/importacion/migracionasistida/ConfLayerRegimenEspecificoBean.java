package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

public class ConfLayerRegimenEspecificoBean {
	private int id;
	private String orden;
	private String nombre;
	private String texto;
	private String alias;
	private boolean selected;
	
	public String getOrden() {
		return orden;
	}
	
	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
