/**
 * ViaOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

public class ViaOT {

	private long codigoINE = 0;
	private long codigoCatastro = 0;
	private String tipoViaNormalizadoCatastro = null;
	private String tipoViaINE = null;
	private int posicionTipoVia = 0;
	private String nombreViaINE = null;
	private String nombreViaCortoINE = null;
	private String nombreCatastro = null;
	private long idMunicipio = 0;
	private double longitud = 0;
	
	public long getCodigoINE() {
		return codigoINE;
	}
	public void setCodigoINE(long codigoINE) {
		this.codigoINE = codigoINE;
	}
	public long getCodigoCatastro() {
		return codigoCatastro;
	}
	public void setCodigoCatastro(long codigoCatastro) {
		this.codigoCatastro = codigoCatastro;
	}
	public String getTipoViaNormalizadoCatastro() {
		return tipoViaNormalizadoCatastro;
	}
	public void setTipoViaNormalizadoCatastro(String tipoViaNormalizadoCatastro) {
		this.tipoViaNormalizadoCatastro = tipoViaNormalizadoCatastro;
	}
	public String getTipoViaINE() {
		return tipoViaINE;
	}
	public void setTipoViaINE(String tipoViaINE) {
		this.tipoViaINE = tipoViaINE;
	}
	public int getPosicionTipoVia() {
		return posicionTipoVia;
	}
	public void setPosicionTipoVia(int posicionTipoVia) {
		this.posicionTipoVia = posicionTipoVia;
	}
	public String getNombreViaINE() {
		return nombreViaINE;
	}
	public void setNombreViaINE(String nombreViaINE) {
		this.nombreViaINE = nombreViaINE;
	}
	public String getNombreViaCortoINE() {
		return nombreViaCortoINE;
	}
	public void setNombreViaCortoINE(String nombreViaCortoINE) {
		this.nombreViaCortoINE = nombreViaCortoINE;
	}
	public String getNombreCatastro() {
		return nombreCatastro;
	}
	public void setNombreCatastro(String nombreCatastro) {
		this.nombreCatastro = nombreCatastro;
	}
	public long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}
