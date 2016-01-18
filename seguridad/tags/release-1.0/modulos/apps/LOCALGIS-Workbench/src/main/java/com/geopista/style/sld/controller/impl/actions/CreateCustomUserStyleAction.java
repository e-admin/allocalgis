/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.List;

import org.deegree.graphics.sld.UserStyle;

import com.geopista.style.sld.model.SLDFacade;
import com.geopista.style.sld.model.SLDFactory;

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
public class CreateCustomUserStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		/*Recogemos los parámetros de la Request*/
		String styleName = (String) request.getAttribute("StyleName");
		String styleTitle = styleName;
		String styleAbstract = styleName;
		Boolean isDefault = new Boolean(false);
		ArrayList ftsList = (ArrayList) session.getAttribute("FtsList");
		List inserts = (List)session.getAttribute("Insert");
		Integer insert = (Integer)inserts.get(0);
		ArrayList userStyleList = (ArrayList)session.getAttribute("UserStyleList");
		UserStyle userStyle = (UserStyle)session.getAttribute("UserStyle");
		List scaleRangeList = (List)session.getAttribute("ScaleRangeList");		
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		userStyle = sldFacade.updateCustomUserStyle(styleName,styleTitle,styleAbstract,
			isDefault,ftsList,scaleRangeList,insert,userStyle,userStyleList);
		session.setAttribute("UserStyle",userStyle);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		int size = pagesVisited.size();
		String lastPageVisited = (String) pagesVisited.remove(size-1);
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward(lastPageVisited);
		return forward;
	}
}
