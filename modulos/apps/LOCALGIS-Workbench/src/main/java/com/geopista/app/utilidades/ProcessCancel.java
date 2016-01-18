/**
 * ProcessCancel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Verificacion de Cancelacion del Proceso
 * @author satec
 *
 */
public class ProcessCancel extends Thread{
	
	
	private static final Log logger=LogFactory.getLog(ProcessCancel.class);
    
	boolean processContinue=true;
	TaskMonitor monitor;
	
	public ProcessCancel(TaskMonitor monitor){
		this.monitor=monitor;
		//this.method=method;
	}
	public void terminateProcess(){
		processContinue=false;
	}
	public void run(){

		//logger.info("Iniciando proceso de carga");
		
		while (processContinue){
			try {
				
				if (monitor.isCancelRequested()){
					logger.info("Solicitud de cancelacion recibida");
					((TaskMonitorDialog)monitor).dispose();					
					processContinue=false;
				}
				else
					Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	
		}
	}
}	