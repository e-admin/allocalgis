package com.geopista.app.catastro.model.beans;

public class EstructuraDB
{
    /**
     * Patrón alfanumérico único
     */
    private String patron=new String();
    /**
     * Descripción del patrón
     */
    private String descripcion = new String();
    
    public EstructuraDB()
    {
        
    }

    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion()
    {
        return descripcion;
    }

    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    /**
     * @return Returns the patron.
     */
    public String getPatron()
    {
        return patron;
    }

    /**
     * @param patron The patron to set.
     */
    public void setPatron(String patron)
    {
        this.patron = patron;
    }
    
    public boolean equals (Object o)
    {
        if (!(o instanceof EstructuraDB))
            return false;
        
        if (((EstructuraDB)o).getPatron().equalsIgnoreCase(this.patron))
        {
            setDescripcion(((EstructuraDB)o).getDescripcion());
            return true;
        }            
        else 
            return false;
    }    
}
