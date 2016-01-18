/**
 * InsertCustomRuleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.List;

import org.deegree.graphics.sld.Rule;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;
import es.enxenio.util.exceptions.InternalErrorException;


/**
 * @author enxenio s.l.
 *
 */
public class InsertCustomRuleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		String styleName = (String)request.getAttribute("StyleName");
		session.setAttribute("StyleName",styleName);
		session.removeAttribute("RuleFilter");
		String symbolizerType = (String)request.getAttribute("SymbolizerType");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		Rule rule = sldFacade.createDefaultRule();
		/*Metemos en la Session los atributos asociados a la Rule*/
		String ruleName = null;
		if (rule.getName()!=null) {
			ruleName = new String(rule.getName());
		}
		session.setAttribute("RuleName",ruleName);
		/*Añadimos en la Session la Regla creada por defecto*/
		session.setAttribute("Rule",rule);
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(2,new Integer(1));
		session.setAttribute("Insert",inserts);
		session.setAttribute("SymbolizerType",symbolizerType);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		pagesVisited.add("InsertUpdateCustomStyle");
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción del controlador*/
		Action action = frontController.getAction("GetFeatureAttributes");
		Request newRequest = FrontControllerFactory.createRequest();
		String layerName = (String)session.getAttribute("LayerName");
		newRequest.setAttribute("LayerName",layerName);
		action.doExecute(newRequest);
		ActionForward forward = null;
		if ((symbolizerType.toLowerCase()).equals("point")) {
			forward = frontController.getForward("InsertUpdatePointRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			forward = frontController.getForward("InsertUpdateLineRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			forward = frontController.getForward("InsertUpdatePolygonRule");
		}
		else if ((symbolizerType.toLowerCase()).equals("text")) {
			forward = frontController.getForward("InsertUpdateTextRule");
		}
		return forward;
	}

}
