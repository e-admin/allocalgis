package com.geopista.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.localgis.server.SessionsContextShared;
/**
 * Esta clase sirve para añadir la variable sesion al logger
 * del thread.
 * Para que aparezca el valor en los ficheros hay que poner
 * [%X{sesion}] en los ficheros log4j
 * @author angeles
 *
 */

public class LoggerHttpServlet  extends HttpServlet {

	private static final long serialVersionUID = -1892116666098667101L;

	public void doPost (HttpServletRequest request)
	     throws ServletException, IOException
	{
		try{
			ISesion sesion=sesion(request);
			if (sesion!=null)
				org.apache.log4j.MDC.put("sesion","User:"+sesion.getUserPrincipal().getName());
		}catch (Exception ex){}
	}
    
	private ISesion sesion(HttpServletRequest request){
		try{
	        ISesion sRet=null;
	        JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
	        if (userPrincipal!=null){
	            String  sIdSesion= userPrincipal.getName();
	            
	            PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
		    	
	            sRet=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);
	        }
	        return sRet;
		}catch(Exception ex){
			return null;
		}
	}

}
