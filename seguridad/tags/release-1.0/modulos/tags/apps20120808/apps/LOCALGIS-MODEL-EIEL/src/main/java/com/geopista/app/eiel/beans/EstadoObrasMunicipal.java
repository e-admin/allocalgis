package com.geopista.app.eiel.beans;

public class EstadoObrasMunicipal {

	private Integer idMunicipio = null;
	
	private String nomAyto = null;
	
	private Integer anuladas = null;
	
	private Integer autorizadas = null;
	
	private Integer revocadas = null;
	
	private Integer contratadas = null;
	
	private Integer enEjecucion = null;
	
	private Integer liquidadas = null;
	
	private Integer total = null;

	private VersionEiel version = null;
	
	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getNomAyto() {
		return nomAyto;
	}

	public void setNomAyto(String nomAyto) {
		this.nomAyto = nomAyto;
	}

	public Integer getAnuladas() {
		return anuladas;
	}

	public void setAnuladas(Integer anuladas) {
		this.anuladas = anuladas;
	}

	public Integer getRevocadas() {
		return revocadas;
	}

	public void setRevocadas(Integer revocadas) {
		this.revocadas = revocadas;
	}

	public Integer getContratadas() {
		return contratadas;
	}

	public void setContratadas(Integer contratadas) {
		this.contratadas = contratadas;
	}

	public Integer getEnEjecucion() {
		return enEjecucion;
	}

	public void setEnEjecucion(Integer enEjecucion) {
		this.enEjecucion = enEjecucion;
	}

	public Integer getLiquidadas() {
		return liquidadas;
	}

	public void setLiquidadas(Integer liquidadas) {
		this.liquidadas = liquidadas;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getAutorizadas() {
		return autorizadas;
	}

	public void setAutorizadas(Integer autorizadas) {
		this.autorizadas = autorizadas;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
	
	
	
	
}
