package com.geopista.protocol.datosPersonales;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 22-dic-2004
 * Time: 13:25:38
 */
public class DatosPersonales {

    String nif;
    String nombreApellidos;
    String codigo_postal;
    Integer apartado_correos;
    Integer codigoProvincia;
    Integer codigoMunicipio;
   	String entidadMenor;
	Integer codigoVia;
	String tipoVia;
    String nombreVia;
    String bloque;
    String planta;
    String escalera;
    String puerta;
    String primerNumero;
    String primeraLetra;
    String segundoNumero;
    String segundoLetra;
    Integer kilometro;
    String direccionNoEstrucutrada;
    
    
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombreApellidos() {
		return nombreApellidos;
	}

	public void setNombreApellidos(String nombreApellidos) {
		this.nombreApellidos = nombreApellidos;
	}

	 public String getCodigo_postal() {
		return codigo_postal;
	}

	public void setCodigo_postal(String codigoPostal) {
		codigo_postal = codigoPostal;
	}

	public Integer getApartado_correos() {
		return apartado_correos;
	}

	public void setApartado_correos(Integer apartadoCorreos) {
		apartado_correos = apartadoCorreos;
	}

	 public String getEntidadMenor() {
			return entidadMenor;
		}

		public void setEntidadMenor(String entidadMenor) {
			this.entidadMenor = entidadMenor;
		}


	public void setCodigoVia(Integer codigoVia) {
		this.codigoVia = codigoVia;
	}
    public Integer getCodigoVia() {
		return codigoVia;
	}


	public String getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}

	public String getNombreVia() {
		return nombreVia;
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
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

	public String getEscalera() {
		return escalera;
	}

	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getPrimerNumero() {
		return primerNumero;
	}

	public void setPrimerNumero(String primerNumero) {
		this.primerNumero = primerNumero;
	}

	public String getPrimeraLetra() {
		return primeraLetra;
	}

	public void setPrimeraLetra(String primeraLetra) {
		this.primeraLetra = primeraLetra;
	}

	public String getSegundoNumero() {
		return segundoNumero;
	}

	public void setSegundoNumero(String segundoNumero) {
		this.segundoNumero = segundoNumero;
	}

	public String getSegundoLetra() {
		return segundoLetra;
	}

	public void setSegundoLetra(String segundoLetra) {
		this.segundoLetra = segundoLetra;
	}

	public Integer getKilometro() {
		return kilometro;
	}

	public void setKilometro(Integer kilometro) {
		this.kilometro = kilometro;
	}

	public String getDireccionNoEstrucutrada() {
		return direccionNoEstrucutrada;
	}

	public void setDireccionNoEstrucutrada(String direccionNoEstrucutrada) {
		this.direccionNoEstrucutrada = direccionNoEstrucutrada;
	}
	 public Integer getCodigoProvincia() {
		return codigoProvincia;
	}

	public void setCodigoProvincia(Integer codigoProvincia) {
		this.codigoProvincia = codigoProvincia;
	}

	public Integer getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Integer codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}
}
