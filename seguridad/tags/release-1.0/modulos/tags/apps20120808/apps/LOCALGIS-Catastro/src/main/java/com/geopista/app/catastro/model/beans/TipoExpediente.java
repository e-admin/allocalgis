package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 13-mar-2007
 * Time: 16:15:56
 * To change this template use File | Settings | File Templates.
 */

/**
 * Bean que encapsula todos los datos de los tipos de Expediente.
 * */

public class TipoExpediente implements Serializable
{
    private String codigoTipoExpediente;
    private String convenio;

    public TipoExpediente()
    {

    }

    public String getCodigoTipoExpediente()
    {
        return codigoTipoExpediente;
    }

    public void setCodigoTipoExpediente(String codigoTipoExpediente)
    {
        this.codigoTipoExpediente = codigoTipoExpediente;
    }

    public String getConvenio()
    {
        return convenio;
    }

    public void setConvenio(String convenio)
    {
        this.convenio = convenio;
    }
}
