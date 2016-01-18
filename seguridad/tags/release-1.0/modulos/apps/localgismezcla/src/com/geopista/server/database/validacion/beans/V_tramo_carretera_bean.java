package com.geopista.server.database.validacion.beans;


public class V_tramo_carretera_bean {

	  String provincia="-";
	  String cod_carrt="-";
	  String municipio="-";
	  double pk_inicial;
	  double pk_final;
	  String titular="-";
	  String gestion="-";
	  String senaliza="-";
	  String firme="-";
	  String estado="-";
	  double ancho;	
	  double longitud;
	  int pasos_nive;	
	  String dimensiona="-";
	  String muy_sinuos="-";
	  String pte_excesi="-";
	  String fre_estrec="-";
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCod_carrt() {
		return cod_carrt;
	}
	public void setCod_carrt(String codCarrt) {
		cod_carrt = codCarrt;
	}
	public double getPk_inicial() {
		return pk_inicial;
	}
	public void setPk_inicial(double pkInicial) {
		pk_inicial = pkInicial;
	}
	public double getPk_final() {
		return pk_final;
	}
	public void setPk_final(double pkFinal) {
		pk_final = pkFinal;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getSenaliza() {
		return senaliza;
	}
	public void setSenaliza(String senaliza) {
		this.senaliza = senaliza;
	}
	public String getFirme() {
		return firme;
	}
	public void setFirme(String firme) {
		this.firme = firme;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public double getAncho() {
		return ancho;
	}
	public void setAncho(double ancho) {
		this.ancho = ancho;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public int getPasos_nive() {
		return pasos_nive;
	}
	public void setPasos_nive(int pasosNive) {
		pasos_nive = pasosNive;
	}
	public String getDimensiona() {
		return dimensiona;
	}
	public void setDimensiona(String dimensiona) {
		this.dimensiona = dimensiona;
	}
	public String getMuy_sinuos() {
		return muy_sinuos;
	}
	public void setMuy_sinuos(String muySinuos) {
		muy_sinuos = muySinuos;
	}
	public String getPte_excesi() {
		return pte_excesi;
	}
	public void setPte_excesi(String pteExcesi) {
		pte_excesi = pteExcesi;
	}
	public String getFre_estrec() {
		return fre_estrec;
	}
	public void setFre_estrec(String freEstrec) {
		fre_estrec = freEstrec;
	}
	
	
}
