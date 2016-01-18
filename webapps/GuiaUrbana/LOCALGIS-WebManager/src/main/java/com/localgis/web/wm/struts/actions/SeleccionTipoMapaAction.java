/**
 * SeleccionTipoMapaAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SeleccionTipoMapaAction extends Action {
	
	private static final String GESTIONCAPAS = "capas";
	private static final String GESTIONMAPAS = "mapas";
	private static final String MENU = "menu";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			 {
	   	Integer idEntidad = (Integer) request.getSession().getAttribute("idEntidad");
		if (idEntidad == null) {
			ActionForward actionForward = mapping.findForward("entidadNoSelected");
			return actionForward;
		}
		String gestion = (String) request.getParameter("gestion");
		ActionForward forward = new ActionForward();
		if (gestion!=null) {
			if (gestion.equals(GESTIONCAPAS)) {
				request.setAttribute("gestion", gestion);
				forward = mapping.findForward(GESTIONCAPAS);
				return forward;
			}
			else if (gestion.equals(GESTIONMAPAS)) {
					request.setAttribute("gestion", gestion);
					forward = mapping.findForward(GESTIONMAPAS);
					return forward;
			}
			else {
				forward = mapping.findForward(MENU);
				return forward;
			}
			
		}
		else {
			forward = mapping.findForward(MENU);
			return forward;
		}
		
	}//fin del método execute
}//fin de la clase
