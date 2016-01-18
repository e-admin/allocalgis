/**
 * CreateThematicRuleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.Rule;
import org.deegree_impl.services.wfs.filterencoding.OperationDefines;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.ScaleRange;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;
import es.enxenio.util.exceptions.InternalErrorException;

/**
 * @author enxenio s.l.
 *
 */
public class CreateThematicRuleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		/*Recogemos los parámetros de la Request*/
		String propertyName = (String) request.getAttribute("PropertyName");
		List valueList = (List) request.getAttribute("ValueList");
		List styleList = (List) request.getAttribute("StyleList");
		String mainRuleName = (String) request.getAttribute("mainRuleName");
		/*parámetros que necesitamos y que están almacenados en la Session*/
		ScaleRange scaleRange = (ScaleRange)session.getAttribute("ScaleRange");
		/*Recuperamos la leyenda de la Session*/
		List inserts = (List)session.getAttribute("Insert");
		Integer insert = (Integer)inserts.get(2);
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Entramos en un bucle, para cada valor creamos una regla con un filtro*/
		Iterator valueListIterator = valueList.iterator();
		Iterator styleListIterator = styleList.iterator();
		int cont = 0;
		while ((valueListIterator.hasNext())&&(styleListIterator.hasNext())) {
			String value = (String)valueListIterator.next();
			HashMap style = (HashMap) styleListIterator.next();
			List expressions = new ArrayList();
			expressions.add(value);
			Integer operationID = new Integer(OperationDefines.PROPERTYISEQUALTO);
			String ruleName = mainRuleName+" "+cont;
			/*Llamamos al caso de uso del modelo*/
			Rule rule = sldFacade.createRule(ruleName,style,propertyName,operationID,expressions,
				insert,scaleRange);
			cont++;
		}
		session.setAttribute("ScaleRange",scaleRange);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		int size = pagesVisited.size();
		String lastPageVisited = (String) pagesVisited.remove(size-1);
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward(lastPageVisited);
		return forward;
		
	}
}
