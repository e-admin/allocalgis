package com.geopista.server.catastro.servicioWebCatastro;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 30-mar-2007
 * Time: 14:25:47
 * To change this template use File | Settings | File Templates.
 */

/**
 * Bean que implementa la unidad de registro de las parcelas para la exportacion masiva, cuando un expediente
 * no ha llegado a finalizado y se comunica a catastro su existencia. Se crea este objeto que luego sera parseado con
 * java2xml para crear el fichero fin entrada.
 * */

public class UnidadRegistroFinca
{
    private String codigoDelegacion;
    private String codigoMunicipio;
    private String refCatas1;
    private String refCatas2;

    public UnidadRegistroFinca()
    {

    }

    public String getCodigoDelegacion()
    {
        return codigoDelegacion;
    }

    public void setCodigoDelegacion(String codigoDelegacion)
    {
        this.codigoDelegacion = codigoDelegacion;
    }

    public String getCodigoMunicipio()
    {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio)
    {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getRefCatas1()
    {
        return refCatas1;
    }

    public void setRefCatas1(String refCatas1)
    {
        this.refCatas1 = refCatas1;
    }

    public String getRefCatas2()
    {
        return refCatas2;
    }

    public void setRefCatas2(String refCatas2)
    {
        this.refCatas2 = refCatas2;
    }
}
