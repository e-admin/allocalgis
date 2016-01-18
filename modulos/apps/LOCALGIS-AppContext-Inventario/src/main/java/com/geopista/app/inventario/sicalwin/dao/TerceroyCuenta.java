/**
 * TerceroyCuenta.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.sicalwin.dao;


public class TerceroyCuenta {

	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TerceroyCuenta.class);
	
	private String idTercero;
	private String numDocumento;
	private String tipoDocumento;
	private String alias;
	private String nombre;
	private String domicilio;
	private String poblacion;
	private String codPostal;
	private String provincia;
	private String telefono;
	private String fax;
	private String tipoTercero;
	private String relacionEntidad;
	private String sectorIndustrial;
	private String actividadEconomica;
	private String formaPago;
	private String comprobarCompensaciones;
	private String gastosTransferencia;
	private String observaciones;
	private String embargado;
	private String correoElectronico;
	private String nombreCompleto;
	private String apellido1;
	private String apellido2;
	
	public String getIdTercero() {
		return idTercero;
	}
	public void setIdTercero(String idTercero) {
		this.idTercero = idTercero;
	}
	public String getNumDocumento() {
		return numDocumento;
	}
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String pobliacion) {
		this.poblacion = pobliacion;
	}
	public String getCodPostal() {
		return codPostal;
	}
	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getTipoTercero() {
		return tipoTercero;
	}
	public void setTipoTercero(String tipoTercero) {
		this.tipoTercero = tipoTercero;
	}
	public String getRelacionEntidad() {
		return relacionEntidad;
	}
	public void setRelacionEntidad(String relacionEntidad) {
		this.relacionEntidad = relacionEntidad;
	}
	public String getSectorIndustrial() {
		return sectorIndustrial;
	}
	public void setSectorIndustrial(String sectorIndustrial) {
		this.sectorIndustrial = sectorIndustrial;
	}
	public String getActividadEconomica() {
		return actividadEconomica;
	}
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getComprobarCompensaciones() {
		return comprobarCompensaciones;
	}
	public void setComprobarCompensaciones(String comprobarCompensaciones) {
		this.comprobarCompensaciones = comprobarCompensaciones;
	}
	public String getGastosTransferencia() {
		return gastosTransferencia;
	}
	public void setGastosTransferencia(String gastosTransferencia) {
		this.gastosTransferencia = gastosTransferencia;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getEmbargado() {
		return embargado;
	}
	public void setEmbargado(String embargado) {
		this.embargado = embargado;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public void load(String[] valores) {
		try {
			logger.info("Numero de valores:"+valores.length);
			this.idTercero=valores[0];
			this.numDocumento=valores[1];
			this.tipoDocumento=valores[2];
			this.alias=valores[3];
			this.nombre=valores[4];
			this.domicilio=valores[5];
			this.poblacion=valores[6];
			this.codPostal=valores[7];
			this.provincia=valores[8];
			this.telefono=valores[9];
			this.fax=valores[10];
			this.tipoTercero=valores[11];
			this.relacionEntidad=valores[12];
			this.sectorIndustrial=valores[13];
			this.actividadEconomica=valores[14];
			this.formaPago=valores[15];
			this.comprobarCompensaciones=valores[16];
			this.gastosTransferencia=valores[17];
			this.observaciones=valores[18];
			this.embargado=valores[19];
			
			//Solo me suelen llegar 20 valores (De 0 a 19)
			this.correoElectronico=valores[20];
			
			this.nombreCompleto=valores[21];
			this.apellido1=valores[22];
			this.apellido2=valores[23];
		} catch (Exception e) {			
			//e.printStackTrace();
		}
		
	}
	@Override
	public String toString() {
		return "TerceroyCuenta [idTercero=" + idTercero + ", numDocumento="
				+ numDocumento + ", tipoDocumento=" + tipoDocumento
				+ ", alias=" + alias + ", nombre=" + nombre + "]";
	}
	
}
