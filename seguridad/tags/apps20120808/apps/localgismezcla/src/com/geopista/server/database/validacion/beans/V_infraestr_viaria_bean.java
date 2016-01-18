package com.geopista.server.database.validacion.beans;


public class V_infraestr_viaria_bean {
	
	  String provincia="-";
	  String municipio="-";
	  String entidad="-";
	  String nucleo="-";
	  String tipo_infr="-";
	  String estado="-";
	  int longitud;
	  int superficie;
	  int viv_afecta;
	
	public String getNucleo() {
		return nucleo;
	}
	public void setNucleo(String nucleo) {
		this.nucleo = nucleo;
	}
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
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getTipo_infr() {
		return tipo_infr;
	}
	public void setTipo_infr(String tipoInfr) {
		tipo_infr = tipoInfr;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getLongitud() {
		return longitud;
	}
	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}
	public int getSuperficie() {
		return superficie;
	}
	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}
	public int getViv_afecta() {
		return viv_afecta;
	}
	public void setViv_afecta(int vivAfecta) {
		viv_afecta = vivAfecta;
	}
	

}
