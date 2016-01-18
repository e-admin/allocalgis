/**
 * CreateScaleRangeListAction.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.ScaleRange;
import com.geopista.style.sld.model.impl.ScaleRangeImpl;

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
public class CreateScaleRangeListAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Recuperamos el FeatureTypeStyle de la Session*/
		List featureTypeStyleList = (List)session.getAttribute("FtsList");
		FeatureTypeStyle featureTypeStyle = (FeatureTypeStyle)featureTypeStyleList.get(0);
		/*Llamamos al caso de uso del modelo*/
		List scaleRangeList = sldFacade.getScaleRangeList(featureTypeStyle);
		/*Insertamos en la sesión la lista de objetos ScaleRange*/
		/*Clonamos la lista de ScaleRange por si el usuario decide deshacer los cambios que haya hecho*/		
		List newScaleRangeList = new ArrayList();
		Iterator scaleRangeListIterator = scaleRangeList.iterator();
		while (scaleRangeListIterator.hasNext()) {
			ScaleRange scaleRange = (ScaleRange)scaleRangeListIterator.next();
			ScaleRange newScaleRange = clone(scaleRange);
			newScaleRangeList.add(newScaleRange);
		}
		session.setAttribute("ScaleRangeList",newScaleRangeList);
		/*Redirigimos a otra acción del controlador*/
		Request newRequest = FrontControllerFactory.createRequest();
		newRequest.setAttribute("StyleName",session.getAttribute("StyleName"));
		newRequest.setAttribute("Position",new Integer(0));
		Action action = frontController.getAction("SelectScaleRange");
		ActionForward forward = action.doExecute(newRequest);
		return forward;
	}
	
	private ScaleRange clone(ScaleRange scaleRange) {
		
		ScaleRange newScaleRange = new ScaleRangeImpl(scaleRange.getMinScale(), scaleRange.getMaxScale(),
			scaleRange.getPointList(), scaleRange.getLineList(),
			scaleRange.getPolygonList(),scaleRange.getTextList());
		return newScaleRange;
	}

}
