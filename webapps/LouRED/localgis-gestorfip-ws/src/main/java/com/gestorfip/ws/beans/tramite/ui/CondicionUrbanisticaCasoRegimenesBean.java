package com.gestorfip.ws.beans.tramite.ui;

public class CondicionUrbanisticaCasoRegimenesBean {
	
	  private int id;
	  private String comentario; 
	  private String valor;
	  private int superposicion;
	  private int valorreferencia_determinacionid;
	  private int determinacionregimen_determinacionid;
	  private int casoaplicacion_casoid;
	  private int caso;
	  
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public int getSuperposicion() {
		return superposicion;
	}
	
	public void setSuperposicion(int superposicion) {
		this.superposicion = superposicion;
	}
	
	public int getValorreferencia_determinacionid() {
		return valorreferencia_determinacionid;
	}
	
	public void setValorreferencia_determinacionid(
			int valorreferenciaDeterminacionid) {
		valorreferencia_determinacionid = valorreferenciaDeterminacionid;
	}
	
	public int getDeterminacionregimen_determinacionid() {
		return determinacionregimen_determinacionid;
	}
	
	public void setDeterminacionregimen_determinacionid(
			int determinacionregimenDeterminacionid) {
		determinacionregimen_determinacionid = determinacionregimenDeterminacionid;
	}
	
	public int getCasoaplicacion_casoid() {
		return casoaplicacion_casoid;
	}
	
	public void setCasoaplicacion_casoid(int casoaplicacionCasoid) {
		casoaplicacion_casoid = casoaplicacionCasoid;
	}
	
	public int getCaso() {
		return caso;
	}
	
	public void setCaso(int caso) {
		this.caso = caso;
	} 

}
