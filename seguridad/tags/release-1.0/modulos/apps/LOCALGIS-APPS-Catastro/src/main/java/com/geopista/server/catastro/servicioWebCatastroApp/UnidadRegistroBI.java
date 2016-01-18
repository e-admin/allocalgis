package com.geopista.server.catastro.servicioWebCatastroApp;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 30-mar-2007
 * Time: 14:26:03
 * To change this template use File | Settings | File Templates.
 */

/**
 * Bean que implementa la unidad de registro de los bienes inmuebles para la exportacion masiva, cuando un expediente
 * no ha llegado a finalizado y se comunica a catastro su existencia. Se crea este objeto que luego sera parseado con
 * java2xml para crear el fichero fin entrada.
 * */

public class UnidadRegistroBI
{
    private String codigoDelegacion;
    private String codigoMunicipio;
    private String refCatas1;
    private String refCatas2;
    private String cargo;
    private String digitoControl1;
    private String digitoControl2;

    /**
     * Constructor de la clase.
     * */
    public UnidadRegistroBI()
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

    public String getCargo()
    {
        return cargo;
    }

    public void setCargo(String cargo)
    {
        this.cargo = cargo;
    }

    public String getDigitoControl1()
    {
        return digitoControl1;
    }

    public void setDigitoControl1(String digitoControl1)
    {
        this.digitoControl1 = digitoControl1;
    }

    public String getDigitoControl2()
    {
        return digitoControl2;
    }

    public void setDigitoControl2(String digitoControl2)
    {
        this.digitoControl2 = digitoControl2;
    }
}
