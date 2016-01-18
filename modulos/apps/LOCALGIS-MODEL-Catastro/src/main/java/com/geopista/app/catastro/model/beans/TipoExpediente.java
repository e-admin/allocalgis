/**
 * TipoExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
