/**
 * TestConnection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.security;

import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.security.connect.ConnectionStatus;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.util.config.UserPreferenceStore;




/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-sep-2004
 * Time: 12:39:36
 */
public class TestConnection extends Thread{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TestConnection.class);
    private long timeToSleep=10000;
    IConnection iconnection;
    boolean stop=false;
    
    private static int numThreads=0;
    
    private boolean firstAttempt=true;
    
    ArrayList listeners=new ArrayList();


    public TestConnection() {
    	numThreads++;
        start();
    }

    
    public void run()
    {
        try {
        	
        	//Solo dejamos ejecutar un hilo
        	if (numThreads>1){
        		numThreads--;
        		return;
        	}
			do
			{
				//System.out.println("Numero de Threads:"+numThreads);
			    try{Thread.currentThread();
				Thread.sleep(timeToSleep);}catch(Exception e){
			    	e.printStackTrace();
			    };
			    //System.out.println(timeToSleep);
			    boolean bConectado=SecurityManager.isConnected();
			    				
			    if (bConectado)
			    	firstAttempt=false;
			    
			    String extraInfo=null;
			    if (SecurityManager.getUserPrincipal()!=null){
			    	logger.debug("Comprobando la conexión para el usuario:"+SecurityManager.getUserPrincipal().getName()+" ->Id de Thread:"+Thread.currentThread().getId());
			    	extraInfo="Usuario:"+SecurityManager.getUserPrincipal().getName()+" ->Id de Thread:"+Thread.currentThread().getId();
			    }
			    else{
			    	logger.debug("Comprobando la conexión ->Id de Thread:"+Thread.currentThread().getId());
			    	extraInfo="->Id de Thread:"+Thread.currentThread().getId();
			    }
			    SecurityManager.callHeartBeat(extraInfo);
			   
			    

			    if (iconnection!=null)
			    {
			    	//logger.debug("bConectado:"+bConectado);
			    	//logger.debug("SecurityManager.isConnected():"+SecurityManager.isConnected());
			    	
			    	//Si estabamos conectados antes del Heartbeat y ahora ya no lo estamos, 
			    	//marcamos la sesion como desconectada.
			        if(bConectado&&!SecurityManager.isConnected()){
			        	Iterator it=listeners.iterator();
			        	int numListeners=listeners.size();
			        	logger.info("Marcando la sesion como desconectado. Numero de listeners:"+numListeners);
			        	while (it.hasNext()){			        		
			        		IConnection iconnectionTemp=(IConnection)it.next();
			        		iconnectionTemp.disconnect();
			        		

			        	}
			        }
			        //Si no estabamos conectados antes del Heartbeat y ahora si lo estamos marcamos
			        //la sesion como conectado.
			        else if(!bConectado&&SecurityManager.isConnected()){
			            
			            Iterator it=listeners.iterator();
			            int numListeners=listeners.size();
			        	logger.info("Marcando la sesion como conectado. Numero de listeners:"+numListeners+" Id Sesion:"+SecurityManager.getIdSesion());
			        	while (it.hasNext()){
			        		IConnection iconnectionTemp=(IConnection)it.next();
			        		iconnectionTemp.connect();
			        	}
			        	
			        	//Si se ha desconectado el identificador de sesion es nulo por lo que lo volvemos a setear.
			        	if (!firstAttempt){
			        	   String SSOIdSesion = UserPreferenceStore.getUserPreference(UserPreferenceConstants.SSO_SESSION_ID,"",false);
			        	   SecurityManager.setIdSesion(SSOIdSesion);
			        	}
				
			        	//Verificamos si el usuario esta conectado o no en el servidor solo en el caso de que nos hayamos
			        	//previamente conectado. Si no no tiene sentido todavia.
			        	solicitarAutenticacion();
			        	

			        }
			        //Estando conectado la sesion puede caducar en el servidor. Controlamos este caso.
			        //para que en el caso que caduque volver a solicitar login/password
			        else if (bConectado && SecurityManager.isConnected()){
			        	
			        	//Solo verificamos cuando el usuario esta logueado. 
			        	if (SecurityManager.isLogged())
			        		solicitarAutenticacion();
			        	
			        }
			        /*else if (bConectado && SecurityManager.isConnected()){
			        	
			        	if (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged())){
			        		logger.error("Previamente conectado. Conectando de nuevo. Valores de conexion: Entidad:"+SecurityManager.initSM().idEntidad+" - Municipio:"+SecurityManager.initSM().idMunicipio+" - Logged:"+SecurityManager.initSM().isLoggedNS()+ " IdSesion:"+SecurityManager.getIdSesion());
			        		
			        		//2013-06-27 No se si es necesario realizar un logout. 
			    			if (SecurityManager.getIdSesion()!=null)
			    				SecurityManager.logout();
			    			
			        		if (SSOAuthManager.isSSOActive())
				        		SSOAuthManager.ssoAuthManager(null);
			        		
			        		if (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged())){
			        			AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
			        			if (!aplicacion.isPartialLogged()){
			        				iconnection.connect();
					        		 logger.debug("Marcando la sesion como conectado");

			        			}
			        			
			        		}
			        	}
			        	
			        }
			        //Si previamente estaba conectado y ya no lo esta porque se ha perdido la
			        //sesion en el servidor, volvemos a pedir la contraseña.
			        /*else if(bConectado&& (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged()))){
			        	
			        	if (SSOAuthManager.isSSOActive())
			        		SSOAuthManager.ssoAuthManager(null);
			        	else
			        		iconnection.disconnect();
			        }*/
			    }

			}
			while(true && stop==false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Solicitamos la autenticacion al cliente
     */
    private void solicitarAutenticacion(){
    	
    	
    	if (!firstAttempt){
        	if (SSOAuthManager.isSSOActive()){		
        		String SSOIdSesion = UserPreferenceStore.getUserPreference(UserPreferenceConstants.SSO_SESSION_ID,"",false);
        		//logger.info("Verificando sesion en memoria:"+SecurityManager.getIdSesion()+" y la almacenada en local:"+SSOIdSesion);
            	
        		if (!SSOAuthManager.isSessionValid()){			        			
        			logger.error("La sesion almacenada en local:"+SSOIdSesion+" ya no existe en el servidor. Es preciso volver a autenticarse");
        			
        			AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    				if (!aplicacion.isPartialLogged()) {
    					
    					try {
    						
							ConnectionStatus status=(ConnectionStatus)aplicacion.getBlackboard().get(UserPreferenceConstants.CONNECT_STATUS);
							if (status!=null){
								//Establecemos el identificador de sesion a null.
								SSOAuthManager.invalidateSession();		
								AppContext.releaseResources();
								SecurityManager.unLogged();
								status.transparentLogin();
							}
						} catch (Exception e) {
							logger.error("Error al realizar el login transparente",e);
						}
    				}
        		}
        		else{
        			//logger.info("La sesion almacenada en local:"+SSOIdSesion+" todavia existe en el servidor. No Es preciso volver a autenticarse");
        			if (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged())){
        				SecurityManager.setLogged(true);
        			}
        		}
        	}
    	}
    	else{
    		logger.info("Primera verificacion de conexion. No verificamos estado de la sesion");	
    	}
    	firstAttempt=false;
    }
    
    public void finish(){
    	stop=true;
    }
    public IConnection getIconnection() {
        return iconnection;
    }

    public void setIconnection(IConnection iconnection) {
        this.iconnection = iconnection;
        listeners.add(iconnection);
    }

    public long getTimeToSleep() {
        return timeToSleep;
    }

    public void setTimeToSleep(long timeToSleep) {
        this.timeToSleep = timeToSleep;
    }
}
