/**
 * ReloadDomains.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administrador;

import com.geopista.server.administradorCartografia.AdministradorCartografiaServlet;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-nov-2004
 * Time: 11:54:15
 */
public class ReloadDomains extends  Thread{
   org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ReloadDomains.class);
   public ReloadDomains()
   {
      start();
   }
   public void  run()
   {
       try
       {
           AdministradorCartografiaServlet.reloadDomains();
       }catch(Exception e)
       {
              java.io.StringWriter sw=new java.io.StringWriter();
              java.io.PrintWriter pw=new java.io.PrintWriter(sw);
              e.printStackTrace(pw);
              logger.error("Error al recargar los dominios:"+sw.toString());
       }
   }
}