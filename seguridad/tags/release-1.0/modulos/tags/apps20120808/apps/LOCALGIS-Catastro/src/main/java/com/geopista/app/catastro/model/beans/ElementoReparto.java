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
