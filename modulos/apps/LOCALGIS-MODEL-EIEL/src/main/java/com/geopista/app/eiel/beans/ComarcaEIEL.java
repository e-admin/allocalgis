/**
 * ComarcaEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
