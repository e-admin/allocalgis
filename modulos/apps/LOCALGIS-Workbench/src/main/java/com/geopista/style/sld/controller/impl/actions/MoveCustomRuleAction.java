/**
 * MoveCustomRuleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 01-sep-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.List;

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
public class MoveCustomRuleAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.webgis.client.desktop.style.controller.Action#doExecute(es.enxenio.webgis.client.desktop.style.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		int initialPosition = ((Integer)request.getAttribute("InitialPosition")).intValue();
		int finalPosition = ((Integer)request.getAttribute("FinalPosition")).intValue();
		String symbolizerType = (String)request.getAttribute("SymbolizerType");
		ScaleRange scaleRange = (ScaleRange)session.getAttribute("ScaleRange");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		List ruleList = getScaleRangeRuleList(scaleRange,symbolizerType);
		sldFacade.moveRule(initialPosition,finalPosition,(ArrayList)ruleList);
		/*Redirigimos a otra acción*/
		session.setAttribute("SymbolizerType",symbolizerType);
		ActionForward forward = frontController.getForward("InsertUpdateCustomStyle");
		return forward;
	}
	
	private List getScaleRangeRuleList(ScaleRange scaleRange,String symbolizerType) {
		
		List ruleList = null;
		if ((symbolizerType.toLowerCase()).equals("point")) {
			ruleList = scaleRange.getPointList();
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			ruleList = scaleRange.getLineList();
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			ruleList = scaleRange.getPolygonList();
		}
		else if ((symbolizerType.toLowerCase()).equals("text")) {
			ruleList = scaleRange.getTextList();
		}
		return ruleList;
	}
}
