/**
 * ValidacionExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.sql.Connection;
import com.geopista.app.catastro.intercambio.exception.DataExceptionCatastro;
import com.geopista.app.catastro.model.beans.*;
import com.vividsolutions.jump.I18N;

public class ValidacionExpediente {
	
    private Expediente expediente;
    
    
    public ValidacionExpediente(Expediente expediente)
    {
    	this.expediente = expediente;
    }
    

    public StringBuffer validacionesExpediente(Connection conn, Expediente expediente)throws DataExceptionCatastro
    {
    	StringBuffer sbVal = null;
    	if (expediente.getFechaAlteracion() != null){
	        if(!expediente.getFechaAlteracion().before(expediente.getFechaRegistro()))
	        {
	        	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.J20")+"\n\n\r");
	        }
    	}
    	if (expediente.getFechaMovimiento() != null){
	        if(!expediente.getFechaMovimiento().after(expediente.getFechaRegistro()))
	        {
	        	if(sbVal == null){
	        		sbVal = new StringBuffer();
	        	}
	        	sbVal.append(I18N.get("ValidacionMensajesError","Error.J20")+"\n\n\r");
	        }
    	}
        return sbVal;
    }

}
