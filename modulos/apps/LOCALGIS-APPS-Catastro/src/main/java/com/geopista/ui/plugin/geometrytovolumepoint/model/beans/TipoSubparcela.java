package com.geopista.ui.plugin.geometrytovolumepoint.model.beans;

import com.geopista.app.catastro.model.beans.EstructuraDB;

public class TipoSubparcela extends EstructuraDB
{
    /**
     * Identificador del tipo de subparcela
     */
    private int id;
   
        
    public TipoSubparcela()
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
