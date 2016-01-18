package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;

public class DatosFisicosSuelo implements Serializable
{    
    
    
    /**
     * Longitud de la fachada en centímetros.
     */
    //private Integer longFachada;
	private Float longFachada;
    
    /**
     * Fondo del elemento de suelo (en metros)
     */
    
    private Integer fondo;
    
    /**
     * Superficie del elemento Suelo en metros cuadarados
     */
    private Long supOcupada;
    /**
     * Puede tomar los valores FA, FD, DR, IZ o SI.
     */
    private String tipoFachada;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosFisicosSuelo()
    {
        
    }    
    
    /**
     * @return Returns the fondo.
     */
    public Integer getFondo()
    {
        return fondo;
    }
    
    
    /**
     * @param fondo The fondo to set.
     */
    public void setFondo(Integer fondo)
    {
        this.fondo = fondo;
    }
    
    
    /**
     * @return Returns the longFachada.
     */
    //public Integer getLongFachada()
    public Float getLongFachada()
    {
        return longFachada;
    }
    
    
    /**
     * @param longFachada The longFachada to set.
     */
    //public void setLongFachada(Integer longFachada)
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
     * @return Returns the tipoFachada.
     */
    public String getTipoFachada()
    {
        return tipoFachada;
    }
    
    
    /**
     * @param tipoFachada The tipoFachada to set.
     */
    public void setTipoFachada(String tipoFachada)
    {
        this.tipoFachada = tipoFachada;
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
