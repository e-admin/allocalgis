package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


public class Titular extends Persona implements Serializable {       
    private String nifConyuge; 
    private String complementoTitularidad;
    private String fechaAlteracion;
    private String nifCb;
    
    private Derecho derecho = new Derecho();
    
    
    
    /**
     * Constructor por defecto
     */
    public Titular()
    {
        
    }    
    
   
    /**
     * @return Returns the complementoTitularidad.
     */
    public String getComplementoTitularidad()
    {
        return complementoTitularidad;
    }
    
    /**
     * @param complementoTitularidad The complementoTitularidad to set.
     */
    public void setComplementoTitularidad(String complementoTitularidad)
    {
        this.complementoTitularidad = complementoTitularidad;
    }
    
    
    /**
     * @return Returns the fechaAlteracion.
     */
    public String getFechaAlteracion()
    {
        return fechaAlteracion;
    }
    
    /**
     * @param fechaAlteracion The fechaAlteracion to set.
     */
    public void setFechaAlteracion(String fechaAlteracion)
    {
        this.fechaAlteracion = fechaAlteracion;
    }
    
    
    
    /**
     * @return Returns the nifConyuge.
     */
    public String getNifConyuge()
    {
        return nifConyuge;
    }
    
    /**
     * @param nifConyuge The nifConyuge to set.
     */
    public void setNifConyuge(String nifConyuge)
    {
        this.nifConyuge = nifConyuge;
    }
       
    /**
     * @return Returns the nifCb.
     */
    public String getNifCb()
    {
        return nifCb;
    }


    /**
     * @param nifCb The nifCb to set.
     */
    public void setNifCb(String nifCb)
    {
        this.nifCb = nifCb;
    }


    /**
     * @return Returns the derecho.
     */
    public Derecho getDerecho() {
        return derecho;
    }
    
    /**
     * @param derecho The derecho to set.
     */
    public void setDerecho(Derecho derecho) {
        this.derecho = derecho;
    }
    
    public Titular clone(Titular titular)
    {
    	Titular titularActual = new Titular();
    	
    	titularActual.setAusenciaNIF(titular.getAusenciaNIF());
    	//titularActual.setBienInmueble(titular.getBienInmueble().clone(titular.getBienInmueble()));
    	titularActual.setCodEntidadMenor(titular.getCodEntidadMenor());
    	titularActual.setComplementoTitularidad(titular.getComplementoTitularidad());
    	titularActual.setDerecho(titular.getDerecho());
    	titularActual.setDomicilio(titular.getDomicilio());
    	titularActual.setExpediente(titular.getExpediente());
    	titularActual.setFechaAlteracion(titular.getFechaAlteracion());
    	titularActual.setNif(titular.getNif());
    	titularActual.setNifCb(titular.getNifCb());
    	titularActual.setNifConyuge(titular.getNifConyuge());
    	titularActual.setRazonSocial(titular.getRazonSocial());
    	
    	return titularActual;
    }
    
    
}
