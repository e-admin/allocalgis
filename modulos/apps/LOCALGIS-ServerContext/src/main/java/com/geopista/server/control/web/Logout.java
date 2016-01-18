/**
 * Logout.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.control.web;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.plus.jaas.JAASUserPrincipal;

import admcarApp.PasarelaAdmcar;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.control.ListaSesiones;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.LoggerHttpServlet;
import com.geopista.server.database.COperacionesControl;
import com.localgis.server.SessionsContextShared;



/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 01-sep-2004
 * Time: 10:03:57
 */

public class Logout extends LoggerHttpServlet
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logout.class);
    public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	 super.doPost(request);
         //El identificador de sesión debe venir como el nombre del user principal
         JAASUserPrincipal userPrincipal = (JAASUserPrincipal)request.getUserPrincipal();
         CResultadoOperacion resultado=logout(userPrincipal,request);
         
         //request.getSession().invalidate();
         response.setContentType ("text/html");
         Writer writer = response.getWriter();
         writer.write (resultado.buildResponse());
         writer.flush();
         writer.close();
     }
    public CResultadoOperacion logout (JAASUserPrincipal userPrincipal,HttpServletRequest request)
    {
        if (userPrincipal==null) return new CResultadoOperacion(false,"El usuario no ha sido autenticado por JAAS");
        String  sIdSesion= userPrincipal.getName();
        
        PasarelaAdmcar.listaSesiones = (ListaSesiones) SessionsContextShared.getContextShared().getSharedAttribute(this.getServletContext(), "UserSessions");    
        
        Sesion sSesion=PasarelaAdmcar.listaSesiones.getSesion(sIdSesion);

        if (sSesion==null)
        {
             logger.debug("Sesion no encontrada:"+sIdSesion);
             return new CResultadoOperacion(false, "Sesión no encontrada");
        }
       /* logger.info("Para saber si un usuario tiene un determinado permiso:");
        logger.info("ES MIEMBRO:" +sSesion.getRoleGroup().isMember(new GeopistaPrincipal("Geopista.Catastro.Importar.fichero.FINURB")));
      /*Para ver todos los permisos
        for (Enumeration e= sSesion.getRoleGroup().members();e.hasMoreElements();)
        {
           logger.info(((Principal)e.nextElement()).getName());
        }

     /*   //Para ver los permisos de un determinado perfiel
        try
        {
            System.out.println("Buscando el PERFil 13");
            GeopistaAcl acl=sSesion.getPerfil(13);
           for (Enumeration e= acl.getPermissions();e.hasMoreElements();)
           {
               System.out.println("Permiso: "+((GeopistaPermission)e.nextElement()).getName());
           }
            //Para comprobar si tiene un permiso determinado
            if (acl.checkPermission(new GeopistaPermission("Geopista.Layer.Escribir")))
                System.out.println("El usuario tiene el permiso");
            else
                System.out.println("El usuario no tiene el permiso");
        }catch(Exception e)
        {
            StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               e.printStackTrace(pw);
            System.out.println("------------ERROR 2:"+sw.toString());
        } */
        //
        if (PasarelaAdmcar.listaSesiones.delete(sIdSesion)==null)
            logger.info("La sesion:"+sIdSesion+" no existe");
        if(!sSesion.getUserPrincipal().getName().equals("SSOADMIN")){
	        try
	        {   //grabamos la conexion en la lista de conexciones
	        	
	        		COperacionesControl.grabarLogout(sIdSesion);
	        }catch(Exception e)
	        {
	            logger.error("Error al grabar el login "+e.toString());
	        }
	        logger.info("Usuario:"+sSesion.getUserPrincipal().getName()+" con sesion:"+sIdSesion+" se ha desconectado");
        }
        
        SessionsContextShared.getContextShared().setSharedAttribute(this.getServletContext(), "UserSessions", PasarelaAdmcar.listaSesiones);
          
        logger.info("Sesion de "+sSesion.getUserPrincipal().getName()+ " borrada con exito. IP Origen:"+request.getRemoteAddr());
        return new CResultadoOperacion(true, "Sesion de "+sSesion.getUserPrincipal().getName()+ " borrada con exito");
    }






    /* ------------------------------------------------ */
    /** Create user for test
     * @exception ServletException
     */
    public void init ()
        throws ServletException
    {


    }


    /* ------------------------------------------------ */
    /** Destroy servlet, drop tables.
     */
    public void destroy ()
    {
     }


}
