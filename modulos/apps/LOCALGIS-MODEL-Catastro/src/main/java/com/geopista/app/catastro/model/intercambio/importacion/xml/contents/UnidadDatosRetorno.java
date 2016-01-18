/**
 * UnidadDatosRetorno.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.intercambio.importacion.xml.contents;

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
