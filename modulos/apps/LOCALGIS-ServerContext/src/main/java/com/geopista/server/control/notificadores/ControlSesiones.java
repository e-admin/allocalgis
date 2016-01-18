/**
 * ControlSesiones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.notificadores;

import java.util.Date;
import java.util.Enumeration;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.LocalgisSerlvetContextListener;
import com.localgis.server.SessionsContextShared;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 06-oct-2004
 * Time: 17:20:18
 */
public class ControlSesiones extends  Thread{
   org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ControlSesiones.class);
   
   //Ampliamos la fecha de sesion para que las cargas masivas que duran mucho tiempo se puedan
   //completar
   //public static final int MINUTOS_SESION=600;//10 HORAS

    //public static final int MINUTOS_SESION=60*48;//48 HORAS
    
    
   public ControlSesiones()
   {
      start();
   }
   public void  run()
   {
       //primero cerramos todas las sesiones de base de datos que esten abiertas.
       try
       {
           COperacionesControl.cerrarSesiones();
       }catch(Exception e)
       {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("Error en el controlador de sesiones:"+sw.toString());
       }
       logger.info("Arrancando el controlador de sesiones");
       while(true)
       {
           try
           {
               Thread.sleep(300000);
               cerrarSesiones();

           }catch(Exception e)
           {
                 java.io.StringWriter sw=new java.io.StringWriter();
                 java.io.PrintWriter pw=new java.io.PrintWriter(sw);
                 e.printStackTrace(pw);
                 logger.error("Error en el controlador de sesiones:"+sw.toString());
           }

       }
   }
   public void cerrarSesiones()
   {
	   PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(LocalgisSerlvetContextListener.getServletContext(), "UserSessions");    
   	
       //logger.info("Controlando sesiones"); 
       for (Enumeration<ISesion> e=PasarelaAdmcar.listaSesiones.getLista().elements();e.hasMoreElements();)
       {
           Sesion sesion=(Sesion) e.nextElement();

           long fechaTopeConexion=(sesion.getFechaConexion().getTime()+(Constantes.TTL_USUARIOS*60*1000));
           long fechaActual=new java.util.Date().getTime();
           logger.info("Controlando la sesion de: "+ sesion.getUserPrincipal().getName()+ " desde IP:"+sesion.getIP()+" en la aplicacion "+sesion.getIdApp()+". Identificador de sesion:"+ sesion.getIdSesion()+" Fecha Tope:"+new Date(fechaTopeConexion)+" Id Entidad:"+sesion.getIdEntidad()+"/"+sesion.getIdCurrentEntidad()+" Municipio:"+sesion.getIdMunicipio()+"/"+sesion.getIdCurrentMunicipio());
           if (fechaTopeConexion<fechaActual)
           {
               logger.info("Fecha Conexion:"+fechaTopeConexion+ " Fecha actual: "+ fechaActual);
               logger.warn("Borramos la sesion del usuario: "+ sesion.getIdUser());
               PasarelaAdmcar.listaSesiones.delete(sesion);
               COperacionesControl.grabarLogout(sesion.getIdSesion());
           }

       }
       SessionsContextShared.getContextShared().setSharedAttribute(LocalgisSerlvetContextListener.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);

   }
}
