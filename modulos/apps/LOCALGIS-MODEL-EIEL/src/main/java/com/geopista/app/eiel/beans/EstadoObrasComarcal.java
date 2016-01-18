/**
 * EstadoObrasComarcal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

public class EstadoObrasComarcal {

	private String idComarca = null;
	
	private String nomAyto = null;
	
	private Integer anuladas = null;
	
	private Integer autorizadas = null;
	
	private Integer revocadas = null;
	
	private Integer contratadas = null;
	
	private Integer enEjecucion = null;
	
	private Integer liquidadas = null;
	
	private Integer total = null;

	private VersionEiel version = null;
	
	public String getIdComarca() {
		return idComarca;
	}

	public void setIdComarca(String idComarca) {
		this.idComarca = idComarca;
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
