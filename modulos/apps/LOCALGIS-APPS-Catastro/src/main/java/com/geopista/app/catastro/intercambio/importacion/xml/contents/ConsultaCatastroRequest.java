/**
 * ConsultaCatastroRequest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.xml.contents;

import java.util.ArrayList;

import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosRetorno;

public class ConsultaCatastroRequest
{
    
    /**
     * Entidad generadora del fichero de salida
     */
    private UnidadDatosRetorno unidadDatosRetorno;
    
    /**
     * Finca de catastro obligatoria
     */
    
    private ArrayList lstErrores;
   
    public ConsultaCatastroRequest()
    {
        
    }

	/**
	 * @return the lstErrores
	 */
	public ArrayList getLstErrores() {
		return lstErrores;
	}

	/**
	 * @param lstErrores the lstErrores to set
	 */
	public void setLstErrores(ArrayList lstErrores) {
		this.lstErrores = lstErrores;
	}

	/**
	 * @return the unidadDatosRetorno
	 */
	public UnidadDatosRetorno getUnidadDatosRetorno() {
		return unidadDatosRetorno;
	}

	/**
	 * @param unidadDatosRetorno the unidadDatosRetorno to set
	 */
	public void setUnidadDatosRetorno(UnidadDatosRetorno unidadDatosRetorno) {
		this.unidadDatosRetorno = unidadDatosRetorno;
	}

    /**
     * @return Returns the entidadGeneradora.
     */
    }
