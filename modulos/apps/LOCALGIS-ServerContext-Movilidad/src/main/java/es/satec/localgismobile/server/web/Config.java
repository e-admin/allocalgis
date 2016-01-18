/**
 * Config.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import es.satec.localgismobile.server.Global;


public class Config extends HttpServlet {

    private static final String cvsid = "$Id: Config.java,v 1.1 2011/09/19 13:57:27 satec Exp $";


    public void init() throws ServletException {
        super.init();
        System.out.println("[Config.init()] Iniciando sistema de logs...");
        Global.setLog4jConfig();
    }
}
