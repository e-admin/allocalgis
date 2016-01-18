/**
 * CalleOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

import java.sql.Date;

public class CalleOT {

	private Integer codigoINEMunicipio = null;
	private Integer codigoDGC = null;
	private String denominacion = null;
	private String codigoTipoVia = null;
	private String denominacionNormalizada = null;
	private Date fechaGrabacionAyto = null;
	private Date fechaGrabacionCierre = null;
	private Date fechaEjecucion = null;
	private String claseVia = null;
	private Integer idacl = null;
	
	public Integer getCodigoINEMunicipio() {
		return codigoINEMunicipio;
	}
	public void setCodigoINEMunicipio(Integer codigoINEMunicipio) {
		this.codigoINEMunicipio = codigoINEMunicipio;
	}
	public Integer getCodigoDGC() {
		return codigoDGC;
	}
	public void setCodigoDGC(Integer codigoDGC) {
		this.codigoDGC = codigoDGC;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public String getCodigoTipoVia() {
		return codigoTipoVia;
	}
	public void setCodigoTipoVia(String codigoTipoVia) {
		this.codigoTipoVia = codigoTipoVia;
	}
	public String getDenominacionNormalizada() {
		return denominacionNormalizada;
	}
	public void setDenominacionNormalizada(String denominacionNormalizada) {
		this.denominacionNormalizada = denominacionNormalizada;
	}
	public Date getFechaGrabacionAyto() {
		return fechaGrabacionAyto;
	}
	public void setFechaGrabacionAyto(Date fechaGrabacionAyto) {
		this.fechaGrabacionAyto = fechaGrabacionAyto;
	}
	public Date getFechaGrabacionCierre() {
		return fechaGrabacionCierre;
	}
	public void setFechaGrabacionCierre(Date fechaGrabacionCierre) {
		this.fechaGrabacionCierre = fechaGrabacionCierre;
	}
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	public String getClaseVia() {
		return claseVia;
	}
	public void setClaseVia(String claseVia) {
		this.claseVia = claseVia;
	}
	public Integer getIdacl() {
		return idacl;
	}
	public void setIdacl(Integer idacl) {
		this.idacl = idacl;
	}	
	
}
