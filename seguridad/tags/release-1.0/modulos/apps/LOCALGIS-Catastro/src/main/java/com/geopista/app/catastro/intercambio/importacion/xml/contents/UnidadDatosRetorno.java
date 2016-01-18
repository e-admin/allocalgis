package com.geopista.app.catastro.intercambio.importacion.xml.contents;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.Expediente;

public class UnidadDatosRetorno
{
    /**
     * Expediente de la Gerencia
     */
    private Expediente expediente;
    
    /**
     * Lista de situaciones en catastro, bien de expedientes finalizados
     * o bien de expedientes no finalizados
     */
    private ArrayList lstSituaciones;
    
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
     * @return Returns the lstSituaciones.
     */
    public ArrayList getLstSituaciones()
    {
        return lstSituaciones;
    }
    /**
     * @param lstSituaciones The lstSituaciones to set.
     */
    public void setLstSituaciones(ArrayList lstSituaciones)
    {
        this.lstSituaciones = lstSituaciones;
    }
    
    
    
}
