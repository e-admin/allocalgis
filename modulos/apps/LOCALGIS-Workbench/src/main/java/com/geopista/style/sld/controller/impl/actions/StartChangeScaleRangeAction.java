/**
 * StartChangeScaleRangeAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 20-ene-2006
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.List;

import com.geopista.style.sld.model.ScaleRange;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;

/**
 * @author enxenio s.l.
 *
 */
public class StartChangeScaleRangeAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		String styleName = (String)request.getAttribute("StyleName");
		session.setAttribute("StyleName",styleName);
		int position = ((Integer)request.getAttribute("Position")).intValue();
		List scaleRangeList = (List) session.getAttribute("ScaleRangeList");
		int size = scaleRangeList.size();
		ScaleRange scaleRange = null;
		if ((position>=0)&&(position<size)) {
			scaleRange = (ScaleRange) scaleRangeList.get(position);
		}
		session.setAttribute("ScaleRange",scaleRange);
		session.setAttribute("MinScale",scaleRange.getMinScale().toString());
		session.setAttribute("MaxScale",scaleRange.getMaxScale().toString());
		session.removeAttribute("ScaleRangePosition");
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(1,new Integer(0));
		session.setAttribute("Insert",inserts);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		pagesVisited.add("InsertUpdateCustomStyle");
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("InsertUpdateScaleRange");
		return forward;
	}
}
