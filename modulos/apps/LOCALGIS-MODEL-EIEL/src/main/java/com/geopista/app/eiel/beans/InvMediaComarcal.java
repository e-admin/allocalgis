/**
 * InvMediaComarcal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

public class InvMediaComarcal {

	private String idComarca = null;
	
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
	
	public String getIdComarca() {
		return idComarca;
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

	public void setIdComarca(String idComarca) {
		this.idComarca = idComarca;
	}

	public String getNomAyto() {
		return nomAyto;
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
