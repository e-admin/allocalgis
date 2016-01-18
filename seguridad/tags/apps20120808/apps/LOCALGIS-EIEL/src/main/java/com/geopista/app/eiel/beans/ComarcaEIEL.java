package com.geopista.app.eiel.beans;

import java.sql.Date;

public class ComarcaEIEL {

	private Integer idMunicipio = null;
	
	private String codComarca = null;
	
	private String nombreComarca = null;
	
	private Double hectareas = null;
	
	private Double perimetro = null;
	
	private String codMunicCapital1 = null;
	
	private String codMunicCapital2 = null;
	
	private Date fechaRevision = null;
	
	private String observaciones = null;
	
	private Integer usoUtm = null;

	private VersionEiel version = null;
	
	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getCodComarca() {
		return codComarca;
	}

	public void setCodComarca(String codComarca) {
		this.codComarca = codComarca;
	}

	public String getNombreComarca() {
		return nombreComarca;
	}

	public void setNombreComarca(String nombreComarca) {
		this.nombreComarca = nombreComarca;
	}

	public Double getHectareas() {
		return hectareas;
	}

	public void setHectareas(Double hectareas) {
		this.hectareas = hectareas;
	}

	public Double getPerimetro() {
		return perimetro;
	}

	public void setPerimetro(Double perimetro) {
		this.perimetro = perimetro;
	}

	public String getCodMunicCapital1() {
		return codMunicCapital1;
	}

	public void setCodMunicCapital1(String codMunicCapital1) {
		this.codMunicCapital1 = codMunicCapital1;
	}

	public String getCodMunicCapital2() {
		return codMunicCapital2;
	}

	public void setCodMunicCapital2(String codMunicCapital2) {
		this.codMunicCapital2 = codMunicCapital2;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getUsoUtm() {
		return usoUtm;
	}

	public void setUsoUtm(Integer usoUtm) {
		this.usoUtm = usoUtm;
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
