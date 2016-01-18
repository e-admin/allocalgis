/**
 * CabeceraWSCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.ArrayList;
import java.util.Date;

import com.geopista.app.AppContext;

/**
 * Clase que implementa el bean cabecera de la creacion del xml de finEntrada de catastro. Se crea el objeto y
 * con el java2xml se parsea facilmente a xml.
 * */

public class CabeceraWSCatastro
{
    private String tipoEntidadGeneradora;
    private int codigoDelegacion;
    private String MunicipioODiputacion;
    private String nombreEntidadGeneradora;
    private Date fechaGeneracionFichero;
    private String horaGeneracionFichero;
    private String tipoFichero;
    private String nifPersona;
    private String nombrePersona; 
    
    private ArrayList lstIdentificadorDialogo;

	private boolean actualizaCatastro;

	/** Constante tipo de XML de CreacionExpedienteRequest */
    public static String TIPO_XML_CREACION_EXPEDIENTE = "WTXE";
    
    /** Constante tipo de de XML de ConsultaCatastroRequest */
    public static String TIPO_XML_CONSULTA_CATASTRO = "WTCE";
    
    /** Constante tipo de de XML de ActualizaCatastroRequest */
    public static String TIPO_XML_ACTUALIZA_CATASTRO = "WTAE";
    
    /** Constante tipo de de XML de ConsultaEstadoExpedienteRequest */
    public static String TIPO_XML_CONSULTA_ESTADO_EXPEDIENTE = "WTEE";
    
    /**
     * Constructor de la clase.
     * */
    public CabeceraWSCatastro(){

    }

    public String getTipoEntidadGeneradora() {
        return tipoEntidadGeneradora;
    }

    public void setTipoEntidadGeneradora(String tipoEntidadGeneradora) {
        this.tipoEntidadGeneradora = tipoEntidadGeneradora;
    }

    public int getCodigoDelegacion(){
        return codigoDelegacion;
    }

    public void setCodigoDelegacion(int codigoDelegacion) {
        this.codigoDelegacion = codigoDelegacion;
    }

    public String getMunicipioODiputacion() {
        return MunicipioODiputacion;
    }

    public void setMunicipioODiputacion(String municipioODiputacion) {
        MunicipioODiputacion = municipioODiputacion;
    }

    public String getNombreEntidadGeneradora() {
        return nombreEntidadGeneradora;
    }

    public void setNombreEntidadGeneradora(String nombreEntidadGeneradora){
        this.nombreEntidadGeneradora = nombreEntidadGeneradora;
    }

    public Date getFechaGeneracionFichero() {
        return fechaGeneracionFichero;
    }

    public void setFechaGeneracionFichero(Date fechaGeneracionFichero) {
        this.fechaGeneracionFichero = fechaGeneracionFichero;
    }

    public String getHoraGeneracionFichero() {
        return horaGeneracionFichero;
    }

    public void setHoraGeneracionFichero(String horaGeneracionFichero)  {
        this.horaGeneracionFichero = horaGeneracionFichero;
    }

    public String getTipoFichero(){
        return tipoFichero;
    }

    public void setTipoFichero(String tipoFichero){
        this.tipoFichero = tipoFichero;
    }

	public String getNifPersona() {
		return nifPersona;
	}

	public void setNifPersona(String nifPersona) {
		this.nifPersona = nifPersona;
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	
	public boolean isActualizaCatastro() {
		return actualizaCatastro;
	}

	public void setActualizaCatastro(boolean actualizaCatastro) {
		this.actualizaCatastro = actualizaCatastro;
	}
	

    public ArrayList getLstIdentificadorDialogo() {
		return lstIdentificadorDialogo;
	}

	public void setLstIdentificadorDialogo(ArrayList lstIdentificadorDialogo) {
		this.lstIdentificadorDialogo = lstIdentificadorDialogo;
	}

	public boolean isNotCatastroTemporal(){
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return true;
		}
		
		//return false;
	}
}
