package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;

public class DatosFisicosUC implements Serializable
{   
    /**
     * Año de construcción
     */
    private Integer anioConstruccion;
    
    /**
     * Exactitud en el año de construcción
     */
    private String indExactitud;
    
    
    
    /**
     * Longitud de la fachada.
     * 
     */
    private Float longFachada;
    
    /**
     * Superficie del suelo ocupada por la unidad constructiva
     */
    private Long supOcupada;
    
    
    /**
     * Constructor por defecto 
     *
     */
    public DatosFisicosUC()
    {
        
    }
    
    
    /**
     * @return Returns the anioConstruccion.
     */
    public Integer getAnioConstruccion()
    {
        return anioConstruccion;
    }
    
    
    /**
     * @param anioConstruccion The anioConstruccion to set.
     */
    public void setAnioConstruccion(Integer anioConstruccion)
    {
        this.anioConstruccion = anioConstruccion;
    }
    
    
    /**
     * @return Returns the indExactitud.
     */
    public String getIndExactitud()
    {
        return indExactitud;
    }
    
    
    /**
     * @param indExactitud The indExactitud to set.
     */
    public void setIndExactitud(String indExactitud)
    {
        this.indExactitud = indExactitud;
    }
    
    
    /**
     * @return Returns the longFachada.
     */
    public Float getLongFachada()
    {
        return longFachada;
    }
    
    
    /**
     * @param longFachada The longFachada to set.
     */
    public void setLongFachada(Float longFachada)
    {
        this.longFachada = longFachada;
    }
    
    
    /**
     * @return Returns the supOcupada.
     */
    public Long getSupOcupada()
    {
        return supOcupada;
    }
    
    
    /**
     * @param supOcupada The supOcupada to set.
     */
    public void setSupOcupada(Long supOcupada)
    {
        this.supOcupada = supOcupada;
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
