/**
 * NotificacionAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.action;

import java.util.Date;

import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.server.database.COperacionesDatabase;

public class NotificacionAction extends DefaultAction{
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NotificacionAction.class);
	
    public NotificacionAction() {
    	super();
    	
    }
    
    /**
     * Ejecuta la acción.
     * @param rctx Contexto de ejecución de la regla
     * @param attContext Atributos con información extra, utilizados dentro de 
     * la ejecución de la regla.
     * @return true si la ejecución termina correctamente, false en caso 
     * contrario.
     * @throws ActionException si ocurre algún error.
     */
    public boolean execute(String attContext, CExpedienteLicencia expedienteLicencia, int idTipoLicencia, CSolicitudLicencia solicitud) 
    {
    	
    	COperacionesDatabase.insertNotificacionExpediente(attContext, expedienteLicencia, idTipoLicencia, solicitud);
    	return true;
    }
	public static synchronized long getTableSequence()
    {
    	try {Thread.sleep(10);} catch (Exception ex) {}
		long sequence = new Date().getTime();
		logger.debug("sequence: " + sequence);
		return sequence;
	}
}
