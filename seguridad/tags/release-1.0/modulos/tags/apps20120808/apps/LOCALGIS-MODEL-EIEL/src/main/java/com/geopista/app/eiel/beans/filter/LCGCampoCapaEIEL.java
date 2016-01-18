package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGCampoCapaEIEL implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	
	private String claveGrupo;
	private String campoBD;
	private String tagTraduccion;
	private String traduccion;
	private int tipoBD;
	private String dominio;
	private String tabla;
	private String tagTraduccionGrupo;
	private String traduccionGrupo;
	private String metodo;
	
	private boolean aplicaInformes;
	private boolean aplicaMovilidad;
	
	
	public String getClaveGrupo() {
		return claveGrupo;
	}
	public void setClaveGrupo(String claveGrupo) {
		this.claveGrupo = claveGrupo;
	}
	public String getCampoBD() {
		return campoBD;
	}
	public void setCampoBD(String campoBD) {
		this.campoBD = campoBD;
	}
	public String getTagTraduccion() {
		return tagTraduccion;
	}
	public void setTagTraduccion(String tagTraduccion) {
		this.tagTraduccion = tagTraduccion;
	}
	public int getTipoBD() {
		return tipoBD;
	}
	public void setTipoBD(int tipoBD) {
		this.tipoBD = tipoBD;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	
	
	public String getTraduccion() {
		return traduccion;
	}
	public void setTraduccion(String traduccion) {
		this.traduccion = traduccion;
	}
	
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getTagTraduccionGrupo() {
		return tagTraduccionGrupo;
	}
	public void setTagTraduccionGrupo(String tagTraduccionGrupo) {
		this.tagTraduccionGrupo = tagTraduccionGrupo;
	}
	public String getTraduccionGrupo() {
		return traduccionGrupo;
	}
	public void setTraduccionGrupo(String traduccionGrupo) {
		this.traduccionGrupo = traduccionGrupo;
	}
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public boolean isAplicaInformes() {
		return aplicaInformes;
	}
	public void setAplicaInformes(boolean aplicaInformes) {
		this.aplicaInformes = aplicaInformes;
	}
	public boolean isAplicaMovilidad() {
		return aplicaMovilidad;
	}
	public void setAplicaMovilidad(boolean aplicaMovilidad) {
		this.aplicaMovilidad = aplicaMovilidad;
	}

	
	
}
