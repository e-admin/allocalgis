/**
 * ElementoReparto.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;



public class ElementoReparto implements Serializable
{
	 
    /**
     * Identificador del elemento al que se reparte
     */
    private String id;
    
    /**
     * Datos de expediente
     */
    private Expediente expediente;
    
    
    /**
     * Numero de cargo del elemento
     */
    private String numCargo;    
        
    /**
     * Porcentaje a repartir
     */
    private float porcentajeReparto;

    /**
     * Parametro para java2xml, para distinguir uno de otro en la generacion
     * */
    private boolean esConstruccion;
    
        
    /**
     * Constructor por defecto
     *
     */
    public ElementoReparto()
    {
        
    }


	/**
	 * @return the expediente
	 */
	public Expediente getExpediente() {
		return expediente;
	}


	/**
	 * @param expediente the expediente to set
	 */
	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the numCargo
	 */
	public String getNumCargo() {
		return numCargo;
	}


	/**
	 * @param numCargo the numCargo to set
	 */
	public void setNumCargo(String numCargo) {
		this.numCargo = numCargo;
	}


	/**
	 * @return the porcentajeReparto
	 */
	public float getPorcentajeReparto() {
		return porcentajeReparto;
	}


	/**
	 * @param porcentajeReparto the porcentajeReparto to set
	 */
	public void setPorcentajeReparto(float porcentajeReparto) {
		this.porcentajeReparto = porcentajeReparto;
	}

    public boolean isEsConstruccion()
    {
        return esConstruccion;
    }

    public void setEsConstruccion(boolean esConstruccion)
    {
        this.esConstruccion = esConstruccion;
    }

    public Boolean esConstruccion()
    {
        return new Boolean(esConstruccion);
    }

    public Boolean esCargo()
    {
        return new Boolean(!esConstruccion);        
    }
    
    
    }
