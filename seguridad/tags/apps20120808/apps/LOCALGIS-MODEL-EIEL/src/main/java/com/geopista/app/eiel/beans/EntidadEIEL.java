package com.geopista.app.eiel.beans;

public class EntidadEIEL {

	private String codProvincia = null;
	
	private String codMunicipio = null;
	
	private String codEntidad = null;

	private VersionEiel version = null;
	
	public String getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}

	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
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
