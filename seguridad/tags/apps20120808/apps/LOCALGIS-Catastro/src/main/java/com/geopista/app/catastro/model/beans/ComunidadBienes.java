package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


public class ComunidadBienes extends Persona implements Serializable {
   
    private String complementoTitularidad;
    private String fechaAlteracion;
    
    private BienInmuebleCatastro bienInmueble;
    
    public ComunidadBienes()
    {
        
    }  
    public ComunidadBienes(String nifCB)
    {
        super(nifCB);
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
    
    
    public boolean equals(Object o)
    {
        if (!(o instanceof ComunidadBienes) || o==null
                || ((ComunidadBienes)o).getNif()==null)
            return false;
        
        if (((ComunidadBienes)o).getNif().equalsIgnoreCase(this.getNif()))
            return true;
        else 
            return false;
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
