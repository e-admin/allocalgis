/**
 * CWorkflowThread.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.licencias.workflow;

import com.geopista.server.licencias.teletramitacion.CTeletramitacion;

/**
 * @author SATEC
 * @version $Revision: 1.2 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/01/12 12:10:18 $
 *          $Name:  $
 *          $RCSfile: CWorkflowThread.java,v $
 *          $Revision: 1.2 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CWorkflowThread extends Thread {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CWorkflowThread.class);

	public CWorkflowThread() {
		start();
	}


	public void run() {
        int i=0;
        boolean chequearTeletramitacion=true;
		while (true) {

			try {
                if (chequearTeletramitacion)
                   chequearTeletramitacion=CTeletramitacion.checkRegistroTelematico();
                if (i>=10)
                {

                    CWorkflow.checkExpedientes();
                    i=0;
                }
                i++;
                if (!CWorkflow.getModoDebug())
                {
                    //logger.info("Durmiendo: "+CConstantesWorkflow.segundosWorkflowThread*1000);
                    Thread.sleep(CConstantesWorkflow.segundosWorkflowThread*1000);
                }    
                else
                {
                    logger.warn("ATENCION SISTEMA DE WORKFLOW EN MODO DEBUG: "+CConstantesWorkflow.segundosWorkflowThread*10);
                    Thread.sleep(CConstantesWorkflow.segundosWorkflowThread*10);
                }
			} catch (Exception ex){
				logger.error("Exception: "+ex.toString());
			}

		}

	}

}
