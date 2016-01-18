package com.geopista.app.catastro.model.beans;

/**
 * Tipo de tipología de local para mostrar en los desplegables
 * 
 * @author cotesa
 *
 */
public class TipologiaLocal extends EstructuraDB
{
    /**
     * Identificador de la tipología del local
     */
    private int id;
    
    /**
     * Año de las normas origen de la tipología
     */
    private int anioNormas;
    
    public TipologiaLocal()
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

    /**
     * @return Returns the anioNormas.
     */
    public int getAnioNormas()
    {
        return anioNormas;
    }

    /**
     * @param anioNormas The anioNormas to set.
     */
    public void setAnioNormas(int anioNormas)
    {
        this.anioNormas = anioNormas;
    }    
}
