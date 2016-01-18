package com.geopista.app.eiel.beans;

public class InvMediaMunicipal {

	private Integer idMunicipio = null;
	
	private String nomAyto = null;
	
	private Double prto2005 = null;
	
	private Double prto2006 = null;
	
	private Double prto2007 = null;
	
	private Double prto2008 = null;
	
	private Double prto2009 = null;
	
	private Double liquidadas = null;
	
	private Double totalInv = null;
	
	private Double invHab = null;
	
	private Double invMedAnual = null;

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

	public Double getPrto2005() {
		return prto2005;
	}

	public void setPrto2005(Double prto2005) {
		this.prto2005 = prto2005;
	}

	public Double getPrto2006() {
		return prto2006;
	}

	public void setPrto2006(Double prto2006) {
		this.prto2006 = prto2006;
	}

	public Double getPrto2007() {
		return prto2007;
	}

	public void setPrto2007(Double prto2007) {
		this.prto2007 = prto2007;
	}

	public Double getPrto2008() {
		return prto2008;
	}

	public void setPrto2008(Double prto2008) {
		this.prto2008 = prto2008;
	}

	public Double getPrto2009() {
		return prto2009;
	}

	public void setPrto2009(Double prto2009) {
		this.prto2009 = prto2009;
	}

	public Double getLiquidadas() {
		return liquidadas;
	}

	public void setLiquidadas(Double liquidadas) {
		this.liquidadas = liquidadas;
	}

	public Double getTotalInv() {
		return totalInv;
	}

	public void setTotalInv(Double totalInv) {
		this.totalInv = totalInv;
	}

	public Double getInvHab() {
		return invHab;
	}

	public void setInvHab(Double invHab) {
		this.invHab = invHab;
	}

	public Double getInvMedAnual() {
		return invMedAnual;
	}

	public void setInvMedAnual(Double invMedAnual) {
		this.invMedAnual = invMedAnual;
	}

	public void setNomAyto(String nomAyto) {
		this.nomAyto = nomAyto;
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
