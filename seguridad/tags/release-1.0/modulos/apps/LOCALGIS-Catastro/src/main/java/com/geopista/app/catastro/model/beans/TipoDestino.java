package com.geopista.app.catastro.model.beans;

public class TipoDestino extends EstructuraDB
{
    /**
     * Identificador del destino
     */
    private int id;
   
        
    public TipoDestino()
    {
        
    }
    
    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }
}
