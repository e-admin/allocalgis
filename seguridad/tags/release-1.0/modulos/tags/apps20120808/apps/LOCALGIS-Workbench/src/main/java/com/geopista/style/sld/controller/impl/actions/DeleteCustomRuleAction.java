/*
 * Created on 26-jul-2004
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
public class DeleteCustomRuleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
	
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		String styleName = (String)request.getAttribute("StyleName");
		session.setAttribute("StyleName",styleName);
		int position = ((Integer)request.getAttribute("Position")).intValue();
		String symbolizerType = (String)request.getAttribute("SymbolizerType");
		ScaleRange scaleRange = (ScaleRange)session.getAttribute("ScaleRange");
		List ruleList = getRuleList(scaleRange,symbolizerType);
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		session.setAttribute("SymbolizerType",symbolizerType);
		/*Llamamos al caso de uso del modelo*/
		sldFacade.deleteRule(position,(ArrayList)ruleList);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("InsertUpdateCustomStyle");
		return forward;

	}
	
	private List getRuleList(ScaleRange scaleRange,String symbolizerType) {
		
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
