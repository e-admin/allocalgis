/**
 * LocalizacionOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

public class LocalizacionOT {
	
	private Integer primerNumero = null;
	private String primeraLetra = null;
	private Integer segundoNumero = null;
	private String segundaLetra = null;
	private Double kilometro = null;
	private String bloque = null;
	private Integer codigoPostal = null;
	private String nombreMunicipio = null;
	private String nombreProvincia = null;
	private String nombreVia = null;
	
	public Integer getPrimerNumero() {
		return primerNumero;
	}
	public void setPrimerNumero(Integer primerNumero) {
		this.primerNumero = primerNumero;
	}
	public String getPrimeraLetra() {
		return primeraLetra;
	}
	public void setPrimeraLetra(String primeraLetra) {
		this.primeraLetra = primeraLetra;
	}
	public Integer getSegundoNumero() {
		return segundoNumero;
	}
	public void setSegundoNumero(Integer segundoNumero) {
		this.segundoNumero = segundoNumero;
	}
	public String getSegundaLetra() {
		return segundaLetra;
	}
	public void setSegundaLetra(String segundaLetra) {
		this.segundaLetra = segundaLetra;
	}
	public Double getKilometro() {
		return kilometro;
	}
	public void setKilometro(Double kilometro) {
		this.kilometro = kilometro;
	}
	public String getBloque() {
		return bloque;
	}
	public void setBloque(String bloque) {
		this.bloque = bloque;
	}
	public Integer getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getNombreMunicipio() {
		return nombreMunicipio;
	}
	public void setNombreMunicipio(String nombreMunicipio) {
		this.nombreMunicipio = nombreMunicipio;
	}
	public String getNombreProvincia() {
		return nombreProvincia;
	}
	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}
	public String getNombreVia() {
		return nombreVia;
	}
	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}
}
