package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

public class Provincia implements Serializable {
    /**
     * Identificador de la provincia (dos posiciones)
     */
    private String idProvincia = new String();
    
    /**
     * Nombre oficial de la provincia
     */
    private String nombreOficial=new String();
    
    public Provincia (){
        
    }

    /**
     * @return Returns the idProvincia.
     */
    public String getIdProvincia()
    {
        return idProvincia;
    }

    /**
     * @param idProvincia The idProvincia to set.
     */
    public void setIdProvincia(String idProvincia)
    {
        this.idProvincia = idProvincia;
    }

    /**
     * @return Returns the nombreOficial.
     */
    public String getNombreOficial()
    {
        return nombreOficial;
    }

    /**
     * @param nombreOficial The nombreOficial to set.
     */
    public void setNombreOficial(String nombreOficial)
    {
        this.nombreOficial = nombreOficial;
    }
    
    public boolean equals (Object o)
    {
        if (!(o instanceof Provincia))
            return false;
        
        if (((Provincia)o).getIdProvincia()!=null && 
                ((Provincia)o).getIdProvincia().equalsIgnoreCase(this.idProvincia))
        {
//            setNombreOficial(((Provincia)o).getNombreOficial());
            return true;
        }            
        else 
            return false;
    }    
}
