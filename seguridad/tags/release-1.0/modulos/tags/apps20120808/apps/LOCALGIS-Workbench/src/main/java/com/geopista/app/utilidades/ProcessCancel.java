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