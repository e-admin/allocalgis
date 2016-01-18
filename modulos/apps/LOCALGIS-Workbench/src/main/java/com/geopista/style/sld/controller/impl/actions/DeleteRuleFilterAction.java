/**
 * DeleteRuleFilterAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 23-sep-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;

/**
 * @author enxenio s.l.
 *
 */
public class DeleteRuleFilterAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerFactory.getFrontController();
		Session session = FrontControllerFactory.getSession();
		String pageInvocator = (String)request.getAttribute("PageInvocator");
		session.removeAttribute("RuleFilter");
		/*Redirección a una nueva interfaz*/
		ActionForward forward = frontController.getForward(pageInvocator);
		return forward;
	}

}
