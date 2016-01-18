package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

public class UsoCatastro implements Serializable
{
	
    private String codigoUso;
    private long superficieDestinada;
       
    
    /**
     * Constructor por defecto
     *
     */
    public UsoCatastro(String codigoUso, long superficieDestinada)
    {       
        this.superficieDestinada = superficieDestinada;       
        this.codigoUso = codigoUso;
    }

    public UsoCatastro()
    {
    }

    /**
     * @return Returns the codigoUso.
     */
    public String getCodigoUso()
    {
        return codigoUso;
    }

    /**
     * @param codigoUso The codigoUso to set.
     */
    public void setCodigoUso(String codigoUso)
    {
        this.codigoUso = codigoUso;
    }

    /**
     * @return Returns the superficieDestinada.
     */
    public long getSuperficieDestinada()
    {
        return superficieDestinada;
    }

    /**
     * @param superficieDestinada The superficieDestinada to set.
     */
    public void setSuperficieDestinada(long superficieDestinada)
    {
        this.superficieDestinada = superficieDestinada;
    }
}
