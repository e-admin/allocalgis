/**
 * TitularOT.java
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
 * a datos y la logica de negocios que contienen la informacion disponible
 * de los titulares. 
 *  
 */

import java.util.Date;


public class TitularOT {

    private ExpedienteOT expediente = new ExpedienteOT();        
    private String nif;
    private String razonSocial;
    private String ausenciaNIF;    
    private DireccionOT direccion = new DireccionOT();    
    private BienInmuebleOT bienInmueble = new BienInmuebleOT();
    private String nifConyuge; 
    private String complementoTitularidad;
    private Date fechaAlteracion;
    private String nifCb;   
    private String codigoEntidadMenor = null;
	private String codigoEntidadColaboradora = null;
	
	public ExpedienteOT getExpediente() {
		return expediente;
	}
	public void setExpediente(ExpedienteOT expediente) {
		this.expediente = expediente;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getAusenciaNIF() {
		return ausenciaNIF;
	}
	public void setAusenciaNIF(String ausenciaNIF) {
		this.ausenciaNIF = ausenciaNIF;
	}
	public DireccionOT getDireccion() {
		return direccion;
	}
	public void setDireccion(DireccionOT direccion) {
		this.direccion = direccion;
	}
	public BienInmuebleOT getBienInmueble() {
		return bienInmueble;
	}
	public void setBienInmueble(BienInmuebleOT bienInmueble) {
		this.bienInmueble = bienInmueble;
	}
	public String getNifConyuge() {
		return nifConyuge;
	}
	public void setNifConyuge(String nifConyuge) {
		this.nifConyuge = nifConyuge;
	}
	public String getComplementoTitularidad() {
		return complementoTitularidad;
	}
	public void setComplementoTitularidad(String complementoTitularidad) {
		this.complementoTitularidad = complementoTitularidad;
	}
	public Date getFechaAlteracion() {
		return fechaAlteracion;
	}
	public void setFechaAlteracion(Date fechaAlteracion) {
		this.fechaAlteracion = fechaAlteracion;
	}
	public String getNifCb() {
		return nifCb;
	}
	public void setNifCb(String nifCb) {
		this.nifCb = nifCb;
	}
	public String getCodigoEntidadMenor() {
		return codigoEntidadMenor;
	}
	public void setCodigoEntidadMenor(String codigoEntidadMenor) {
		this.codigoEntidadMenor = codigoEntidadMenor;
	}
	public String getCodigoEntidadColaboradora() {
		return codigoEntidadColaboradora;
	}
	public void setCodigoEntidadColaboradora(String codigoEntidadColaboradora) {
		this.codigoEntidadColaboradora = codigoEntidadColaboradora;
	}
	
}
