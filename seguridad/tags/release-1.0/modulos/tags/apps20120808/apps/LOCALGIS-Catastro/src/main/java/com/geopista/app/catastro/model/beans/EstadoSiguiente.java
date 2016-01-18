package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 02-feb-2007
 * Time: 13:14:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * Clase que encapsula todos los datos de los siguiente estados de la aplicacion.
 * */

public class EstadoSiguiente implements Serializable
{
    private long estadoActual;
    private long estadoSiguiente;
    private String modo;

    public EstadoSiguiente()
    {

    }

    public long getEstadoActual()
    {
        return estadoActual;
    }

    public void setEstadoActual(long estadoActual)
    {
        this.estadoActual = estadoActual;
    }

    public long getEstadoSiguiente()
    {
        return estadoSiguiente;
    }

    public void setEstadoSiguiente(long estadoSiguiente)
    {
        this.estadoSiguiente = estadoSiguiente;
    }

    public String getModo()
    {
        return modo;
    }

    public void setModo(String modo)
    {
        this.modo = modo;
    }
}
