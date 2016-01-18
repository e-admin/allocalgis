package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 26-ene-2007 12:35:54
 */
public class Persona implements Serializable
{
    
    private Expediente expediente = new Expediente();    
    
    private String nif;
    /**
     * Nombre y apellidos 
     */
    private String razonSocial;
    /**
     * Puede tomar los valores F (Física), J (Jurídica) o E (Entidades).
     */
    private String ausenciaNIF;
    
    private DireccionLocalizacion domicilio = new DireccionLocalizacion();
    
    private String codEntidadMenor;
    
    private BienInmuebleCatastro bienInmueble;// = new BienInmuebleCatastro();
    
    /**
     * Constructor por defecto
     */
    public Persona(){
        
    }    

    /**
     * 
     * @param nif
     */
    public Persona(String nif){
        this.nif = nif;
    }
    
    /**
     * @return Returns the ausenciaNIF.
     */
    public String getAusenciaNIF()
    {
        return ausenciaNIF;
    }
    
    /**
     * @param ausenciaNIF The ausenciaNIF to set.
     */
    public void setAusenciaNIF(String ausenciaNIF)
    {
        this.ausenciaNIF = ausenciaNIF;
    }
    
    /**
     * @return Returns the codEntidadMenor.
     */
    public String getCodEntidadMenor()
    {
        return codEntidadMenor;
    }
    
    /**
     * @param codEntidadMenor The codEntidadMenor to set.
     */
    public void setCodEntidadMenor(String codEntidadMenor)
    {
        this.codEntidadMenor = codEntidadMenor;
    }
    
    /**
     * @return Returns the domicilio.
     */
    public DireccionLocalizacion getDomicilio()
    {
        return domicilio;
    }
    
    /**
     * @param domicilio The domicilio to set.
     */
    public void setDomicilio(DireccionLocalizacion domicilio)
    {
        this.domicilio = domicilio;
    }
    
    /**
     * @return Returns the nif.
     */
    public String getNif()
    {
        return nif;
    }
    
    /**
     * @param nif The nif to set.
     */
    public void setNif(String nif)
    {
        this.nif = nif;
    }
    
    /**
     * @return Returns the razonSocial.
     */
    public String getRazonSocial()
    {
        return razonSocial;
    }
    
    /**
     * @param razonSocial The razonSocial to set.
     */
    public void setRazonSocial(String razonSocial)
    {
        this.razonSocial = razonSocial;
    }
    

    /**
     * @return Returns the expediente.
     */
    public Expediente getExpediente()
    {
        return expediente;
    }
    
    /**
     * @param expediente The expediente to set.
     */
    public void setExpediente(Expediente expediente)
    {
        this.expediente = expediente;
    }
    
    /**
     * @return Returns the bienInmueble.
     */
    public BienInmuebleCatastro getBienInmueble()
    {
        return bienInmueble;
    }
    
    /**
     * @param bienInmueble The bienInmueble to set.
     */
    public void setBienInmueble(BienInmuebleCatastro bienInmueble)
    {
        this.bienInmueble = bienInmueble;
    }
    
    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
}
