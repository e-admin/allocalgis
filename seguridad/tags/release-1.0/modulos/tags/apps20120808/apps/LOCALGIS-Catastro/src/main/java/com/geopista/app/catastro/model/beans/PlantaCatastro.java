package com.geopista.app.catastro.model.beans;


import java.io.Serializable;
import java.util.ArrayList;

public class PlantaCatastro implements Serializable
{
	
    private String nombre;
    private int numPlantasReales;
    private ArrayList lstUsos = new ArrayList();
       
    
    /**
     * Constructor por defecto
     *
     */
    public PlantaCatastro(String nombre, int numPlantasReales)
    {       
        this.numPlantasReales = numPlantasReales;       
        this.nombre = nombre;
    }

    public PlantaCatastro()
    {
    }

    /**
     * @return Returns the codigoUso.
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * @param codigoUso The codigoUso to set.
     */
    public void setNombre(String codigoUso)
    {
        this.nombre = codigoUso;
    }

    /**
     * @return Returns the superficieDestinada.
     */
    public int getNumPlantasReales()
    {
        return numPlantasReales;
    }

    /**
     * @param superficieDestinada The superficieDestinada to set.
     */
    public void setNumPlantasReales(int numplantas)
    {
        this.numPlantasReales = numplantas;
    }

    /**
     * @return Returns the lstUsos.
     */
    public ArrayList getLstUsos()
    {
        return lstUsos;
    }

    /**
     * @param lstUsos The lstUsos to set.
     */
    public void setLstUsos(ArrayList lstUsos)
    {
        this.lstUsos = lstUsos;
    }
    
}

