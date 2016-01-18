/**
 * PingAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PingAction extends Action {
	
	protected Logger log = Logger.getLogger(PingAction.class);
	
	public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res) {

		String ping = req.getParameter("ping");

		log.debug("Ping recibido en el servidor");
		if (ping != null) {
			try {
				res.getWriter().print("pong");
			} catch (IOException e) {
				log.error("Error al realizar la escribir la respuesta del servidor");
			}
		}
        return null;
    }
}
