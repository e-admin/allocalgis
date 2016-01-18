package com.geopista.security;

import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.security.sso.SSOAuthManager;



/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */


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
			    logger.debug("Comprobando la conexión...Id de Thread:"+Thread.currentThread().getId());
			    SecurityManager.callHeartBeat();
			   
			    if (iconnection!=null)
			    {
			    	//logger.debug("bConectado:"+bConectado);
			    	//logger.debug("SecurityManager.isConnected():"+SecurityManager.isConnected());
			        if(bConectado&&!SecurityManager.isConnected()){
			        	Iterator it=listeners.iterator();
			        	while (it.hasNext()){
			        		IConnection iconnectionTemp=(IConnection)it.next();
			        		iconnectionTemp.disconnect();
			        	}
			        }
			        else if(!bConectado&&SecurityManager.isConnected()){
			            //iconnection.connect();
			            Iterator it=listeners.iterator();
			        	while (it.hasNext()){
			        		IConnection iconnectionTemp=(IConnection)it.next();
			        		iconnectionTemp.connect();
			        	}
			        }
			        else if (bConectado && SecurityManager.isConnected()){
			        	
			        	if (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged())){
			        		if (SSOAuthManager.isSSOActive())
				        		SSOAuthManager.ssoAuthManager(null);
			        		if (!SecurityManager.isLogged() && (SecurityManager.isPreviouslyLogged())){
			        			AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
			        			if (!aplicacion.isPartialLogged()){
			        				iconnection.connect();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
