package com.geopista.app.catastro.model.beans;

import java.util.ArrayList;

public class DatosRegistroExpedientes
{
    private Expediente expediente;
    private ArrayList lstBienes;
    private ArrayList lstFincas;
    
    public DatosRegistroExpedientes(){
        
    }

    /**
     * @return Returns the expediente.
     */
    public Expediente getExpediente()
    {
        return expediente;
    }

    /**
     * @param expediente The expediente to set.
     */
    public void setExpediente(Expediente expediente)
    {
        this.expediente = expediente;
    }

    /**
     * @return Returns the lstBienes.
     */
    public ArrayList getLstBienes()
    {
        return lstBienes;
    }

    /**
     * @param lstBienes The lstBienes to set.
     */
    public void setLstBienes(ArrayList lstBienes)
    {
        this.lstBienes = lstBienes;
    }

    /**
     * @return Returns the lstFincas.
     */
    public ArrayList getLstFincas()
    {
        return lstFincas;
    }

    /**
     * @param lstFincas The lstFincas to set.
     */
    public void setLstFincas(ArrayList lstFincas)
    {
        this.lstFincas = lstFincas;
    }
    
}
