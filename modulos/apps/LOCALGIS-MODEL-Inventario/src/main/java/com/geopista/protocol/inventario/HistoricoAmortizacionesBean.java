/**
 * HistoricoAmortizacionesBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

public class HistoricoAmortizacionesBean implements Serializable{
	
	
	private long idBien;
	private String numInventario=null;
	private String nombre=null;
	private String tipoBien=null;
	private String tipoBienDenominacion=null;
	private Date fechaAdquisicion=null;
	private CuentaAmortizacion cuentaAmortizacion=null;
	private CuentaContable cuentaContable=null;


	private double costeAdq=0.0;
	private double valorAmorAnio=0.0;
	private double valorAmorPorc=0.0;
	private int anio=0000;
	
	
	
	public long getIdBien() {
		return idBien;
	}
	public void setIdBien(long idBien) {
		this.idBien = idBien;
	}
	public String getNumInventario() {
		return numInventario;
	}
	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipoBien;
	}
	public void setTipo(String tipo) {
		this.tipoBien = tipo;
		if(tipo.equals(Const.INMUEBLES_URBANOS))
				setTipoBienDenominacion("Inmuebles Urbano");
		else if(tipo.equals(Const.INMUEBLES_RUSTICOS))
			setTipoBienDenominacion("Inmuebles Rustico");
		else if(tipo.equals(Const.MUEBLES))
			setTipoBienDenominacion("Muebles");
		else if(tipo.equals(Const.VEHICULOS))
			setTipoBienDenominacion("Vehículos");
	}
	public Date getFechaAdquisicion() {
		return fechaAdquisicion;
	}
	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}
	public double getCoste() {
		return costeAdq;
	}
	public void setCoste(double coste) {
		this.costeAdq = coste;
	}
	public double getValorAmorAnio() {
		return valorAmorAnio;
	}
	public void setValorAmorAnio(double valorAmor) {
		this.valorAmorAnio = valorAmor;
	}
	public CuentaAmortizacion getCuentaAmortizacion() {
		return cuentaAmortizacion;
	}
	public void setCuentaAmortizacion(CuentaAmortizacion cuentaAmortizacion) {
		this.cuentaAmortizacion = cuentaAmortizacion;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio=anio;
	}
	public CuentaContable getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(CuentaContable cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	
	public double getValorAmorPorc() {
		return valorAmorPorc;
	}
	public void setValorAmorPorc(double valorAmorPorc) {
		this.valorAmorPorc = valorAmorPorc;
	}
	public String getTipoBienDenominacion() {
		return tipoBienDenominacion;
	}
	public void setTipoBienDenominacion(String tipoBienDenominacion) {
		this.tipoBienDenominacion = tipoBienDenominacion;
	}
}
