/**
 * DireccionOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;


/**
 * 
 * Objeto transferencia para el intercambio de datos entre los objectos de acceso
 * a datos y la logica de negocios relacionados con las direciones de titulares. 
 *  
 */

public class DireccionOT {
	
	private int codProvinciaINE = 0;
	private int codMunicipioDGC = 0;
	private int codMunicipioINE = 0;
	private int primerNumero = 0;
	private String primeraLetra = null;
	private int segundoNumero = 0;
	private String segundaLetra = null;
	private String escalera = null;
	private double kilometro = 0;
	private String bloque = null;
	private String planta = null;
	private String puerta = null;
	private String dirNoEstructurada = null;
	private String codigoPostal = null;
	private long apartadoCorreos = 0;
	private ViaOT via = new ViaOT();
	
	public int getCodProvinciaINE() {
		return codProvinciaINE;
	}
	public void setCodProvinciaINE(int codProvinciaINE) {
		this.codProvinciaINE = codProvinciaINE;
	}
	public int getCodMunicipioDGC() {
		return codMunicipioDGC;
	}
	public void setCodMunicipioDGC(int codMunicipioDGC) {
		this.codMunicipioDGC = codMunicipioDGC;
	}
	public int getCodMunicipioINE() {
		return codMunicipioINE;
	}
	public void setCodMunicipioINE(int codMunicipioINE) {
		this.codMunicipioINE = codMunicipioINE;
	}
	public int getPrimerNumero() {
		return primerNumero;
	}
	public void setPrimerNumero(int primerNumero) {
		this.primerNumero = primerNumero;
	}
	public String getPrimeraLetra() {
		return primeraLetra;
	}
	public void setPrimeraLetra(String primeraLetra) {
		this.primeraLetra = primeraLetra;
	}
	public int getSegundoNumero() {
		return segundoNumero;
	}
	public void setSegundoNumero(int segundoNumero) {
		this.segundoNumero = segundoNumero;
	}
	public String getSegundaLetra() {
		return segundaLetra;
	}
	public void setSegundaLetra(String segundaLetra) {
		this.segundaLetra = segundaLetra;
	}
	public String getEscalera() {
		return escalera;
	}
	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}
	public double getKilometro() {
		return kilometro;
	}
	public void setKilometro(double kilometro) {
		this.kilometro = kilometro;
	}
	public String getBloque() {
		return bloque;
	}
	public void setBloque(String bloque) {
		this.bloque = bloque;
	}
	public String getPlanta() {
		return planta;
	}
	public void setPlanta(String planta) {
		this.planta = planta;
	}
	public String getPuerta() {
		return puerta;
	}
	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}
	public String getDirNoEstructurada() {
		return dirNoEstructurada;
	}
	public void setDirNoEstructurada(String dirNoEstructurada) {
		this.dirNoEstructurada = dirNoEstructurada;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public long getApartadoCorreos() {
		return apartadoCorreos;
	}
	public void setApartadoCorreos(long apartadoCorreos) {
		this.apartadoCorreos = apartadoCorreos;
	}
	public ViaOT getVia() {
		return via;
	}
	public void setVia(ViaOT via) {
		this.via = via;
	}

}
