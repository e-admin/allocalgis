/**
 * UnidadRegistroFinca.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
