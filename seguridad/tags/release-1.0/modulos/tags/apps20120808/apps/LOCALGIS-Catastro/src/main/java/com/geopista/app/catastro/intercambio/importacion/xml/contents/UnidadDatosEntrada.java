package com.geopista.app.catastro.intercambio.importacion.xml.contents;

import java.util.ArrayList;


public class UnidadDatosEntrada
{
    /*
     * Lista obligatoria de bienes inmuebles
     */
    private ArrayList lstBienesInmuebles;
    
    
    public UnidadDatosEntrada()
    {
        
    }
   

    /**
     * @return Returns the lstBienesInmuebles.
     */
    public ArrayList getLstBienesInmuebles()
    {
        return lstBienesInmuebles;
    }

    /**
     * @param lstBienesInmuebles The lstBienesInmuebles to set.
     */
    public void setLstBienesInmuebles(ArrayList lstBienesInmuebles)
    {
        this.lstBienesInmuebles = lstBienesInmuebles;
    }

   
}
