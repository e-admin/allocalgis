/**
 * DatosRegistroExpedientes.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
