/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.List;

import org.deegree.graphics.sld.Rule;
import org.deegree.services.wfs.filterencoding.Filter;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.ScaleRange;

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
public class UpdateCustomRuleAction implements Action {

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
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		List ruleList = getScaleRangeRuleList(scaleRange,symbolizerType);
		Rule rule = sldFacade.getRule(position,(ArrayList)ruleList);
		/*Redirigimos a otra acción*/
		/*Metemos en la Session los atributos asociados a la Rule*/
		String ruleName = null;
		if (rule.getName()!=null) {
			ruleName = new String(rule.getName());
		}
		session.setAttribute("RuleName",ruleName);
		Filter filter = null;
		if (rule.getFilter() != null) {
			filter = rule.getFilter();
		}
		session.setAttribute("RuleFilter",filter);
		/*Añadimos en la Session la Regla creada por defecto*/
		session.setAttribute("Rule",rule);
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(2,new Integer(0));
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
