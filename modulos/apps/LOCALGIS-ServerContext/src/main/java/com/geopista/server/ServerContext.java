/**
 * ServerContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import com.geopista.server.administrador.init.Constantes;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.app.sigm.SigmConstants;
import com.geopista.consts.config.ConfigConstants;
import com.geopista.protocol.CConstantesComando;
import com.geopista.server.civilwork.CivilWorkConfiguration;
import com.geopista.server.licencias.teletramitacion.CConstantesTeletramitacion;
import com.geopista.server.licencias.workflow.CConstantesWorkflow;
import com.geopista.util.config.ConfigPropertiesStore;
import com.localgis.server.SessionsContextShared;

public class ServerContext extends HttpServlet {
	
	/**
	 * Logger
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServerContext.class);
	
	/**
	 * Variables
	 */
	Timer timer;
	private static HashMap<String,String> config=null;
	//private static ConfigPropertiesStore config=null;
			
	/**
	 * Constructor
	 */
	public ServerContext(){		
	}
	
	/**
	 * Inicializa la configuración
	 */
	public void init(){
		timer = new Timer();
		timer.schedule(new task(), 20 * 1000,20 * 1000);		
	}

	/**
	 * Clase tarea
	 */
	class task extends TimerTask {
		public void run() {
			initConfig();
	    }
	}
	
	public static HashMap<String, String> getConfig(){
		return config;
	}
	
	/*public static ConfigPropertiesStore getConfig(){
		return config;
	}*/
	
	/**
	 * Recupera la configuración
	 */
	private void initConfig(){
		try{
			//logger.info("Verificando configuracion de la aplicacion:"+getServletContext().getServletContextName());
			
			Object configObject=null;
			if (SessionsContextShared.getContextShared()!=null){
				configObject=SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "Config");
			}
			//config = ((HashMap) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "Config"));
			if(configObject!= null){	
				config=(HashMap)configObject;
				//Sustituir en codigo Constantes..... por config.get(ConfigConstants.....)
				Constantes.SUPERUSER = config.get(ConfigConstants.ADMINISTRACION_SUPERUSER);
				Constantes.TEST_MODE = Boolean.valueOf(config.get(ConfigConstants.ADMINISTRACION_TESTMODE));
				
				Constantes.TTL_USUARIOS = Integer.valueOf(config.get(ConfigConstants.USUARIOS_TTL));
		
				ConstantesLocalGISPlantillas.DIR_PLANTILLAS = config.get(ConfigConstants.PLANTILLAS_DIR);
				
				CConstantesTeletramitacion.VU_NOTIF_CRUTAACC = config.get(ConfigConstants.VU_NOTIFCRUTAACC);
				
				CConstantesComando.MaxMemorySize = Integer.valueOf(config.get(ConfigConstants.FILEUPLOAD_MAXMEMORYSIZE));
				CConstantesComando.MaxRequestSize = Integer.valueOf(config.get(ConfigConstants.FILEUPLOAD_MAX_REQUEST_SIZE));
				
				CConstantesWorkflow.diasSilencioAdministrativo = Integer.valueOf(config.get(ConfigConstants.WORKFLOW_DIASSILENCIOADMINISTRATIVO));
				CConstantesWorkflow.diasActivacionEvento = Integer.valueOf(config.get(ConfigConstants.WORKFLOW_DIASACTIVACIONEVENTO));
		
				CConstantesComando.documentosPath = config.get(ConfigConstants.DOCUMENTOS_PATH);
						
				CConstantesComando.REPORTS_PORT = Integer.valueOf(config.get(ConfigConstants.REPORTS_PORT));
				
				CivilWorkConfiguration.timeCivilWorkThread = Integer.valueOf(config.get(ConfigConstants.CIVILWORK_TIMETHREAD));
				CivilWorkConfiguration.timePriorityUpgrade = Integer.valueOf(config.get(ConfigConstants.CIVILWORK_TIMEPRIORITYUPGRADE));
				
				SigmConstants.SIGM_URL = config.get(ConfigConstants.SIGM_URL);
				
				ConstantesLocalGISPlantillas.RESOURCES_PATH = ((String)SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "ResourcesPath"));
				ConstantesLocalGISPlantillas.PATH_PLANTILLAS = ConstantesLocalGISPlantillas.RESOURCES_PATH+ConstantesLocalGISPlantillas.PATH_PLANTILLAS;
				
				timer.cancel(); 
				timer.purge();
				logger.info("Configuracion de la aplicacion:"+getServletContext().getServletContextName()+ " cargada");

			}	
			else
				logger.info("Configuracion de la aplicacion "+getServletContext().getServletContextName()+" todavia no disponible");
		}
		catch(Exception ex){
			logger.info("Error al recuperar la configuracion de la aplicacion: "+getServletContext().getServletContextName());
			logger.error(ex);
			System.out.println(ex);
		}		
	}
	
//	private void initConfig(){
//		try{
//			config = ((HashMap<String,String>) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "Config"));
//			if(config!= null && config.size()>0){			
//				//Sustituir en codigo Constantes..... por config.get(ConfigConstants.....)
//				Constantes.SUPERUSER = config.get(ConfigConstants.ADMINISTRACION_SUPERUSER);
//				Constantes.TEST_MODE = Boolean.valueOf(config.get(ConfigConstants.ADMINISTRACION_TESTMODE));
//				
//				Constantes.TTL_USUARIOS = Integer.valueOf(config.get(ConfigConstants.USUARIOS_TTL));
//		
//				ConstantesLocalGISPlantillas.DIR_PLANTILLAS = config.get(ConfigConstants.PLANTILLAS_DIR);
//				
//				CConstantesTeletramitacion.VU_NOTIF_CRUTAACC = config.get(ConfigConstants.VU_NOTIFCRUTAACC);
//				
//				CConstantesComando.MaxMemorySize = Integer.valueOf(config.get(ConfigConstants.FILEUPLOAD_MAXMEMORYSIZE));
//				CConstantesComando.MaxRequestSize = Integer.valueOf(config.get(ConfigConstants.FILEUPLOAD_MAX_REQUEST_SIZE));
//				
//				CConstantesWorkflow.diasSilencioAdministrativo = Integer.valueOf(config.get(ConfigConstants.WORKFLOW_DIASSILENCIOADMINISTRATIVO));
//				CConstantesWorkflow.diasActivacionEvento = Integer.valueOf(config.get(ConfigConstants.WORKFLOW_DIASACTIVACIONEVENTO));
//		
//				CConstantesComando.documentosPath = config.get(ConfigConstants.DOCUMENTOS_PATH);
//						
//				CConstantesComando.REPORTS_PORT = Integer.valueOf(config.get(ConfigConstants.REPORTS_PORT));
//				
//				CivilWorkConfiguration.timeCivilWorkThread = Integer.valueOf(config.get(ConfigConstants.CIVILWORK_TIMETHREAD));
//				CivilWorkConfiguration.timePriorityUpgrade = Integer.valueOf(config.get(ConfigConstants.CIVILWORK_TIMEPRIORITYUPGRADE));
//				
//				SigmConstants.SIGM_URL = config.get(ConfigConstants.SIGM_URL);
//				
//				ConstantesLocalGISPlantillas.RESOURCES_PATH = ((String)SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "ResourcesPath"));
//				ConstantesLocalGISPlantillas.PATH_PLANTILLAS = ConstantesLocalGISPlantillas.RESOURCES_PATH+ConstantesLocalGISPlantillas.PATH_PLANTILLAS;
//				
//				timer.cancel(); 
//				timer.purge();
//			}	
//		}
//		catch(Exception ex){
//			logger.error(ex);
//			System.out.println(ex);
//		}		
//	}
	
}
