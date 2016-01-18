/*
 * Created on 26-jul-2004
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
public class InsertCustomUserStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		/*Recuperamos las instancias del Frontcontroller y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		UserStyle userStyle = sldFacade.createDefaultUserStyle();
		/*Metemos en la Session los atributos del UserStyle*/
		String styleName = null;
		if (userStyle.getName()!=null) {
			styleName = new String(userStyle.getName());
		}
		String styleTitle = null;
		if (userStyle.getTitle()!=null) {
			styleTitle = new String(userStyle.getTitle());
		} 
		String styleAbstract = null;
		if (userStyle.getAbstract()!= null) {
			styleAbstract = new String(userStyle.getAbstract());
		}
		Boolean isDefault = new Boolean(userStyle.isDefault());
		session.setAttribute("StyleName",styleName);
		session.setAttribute("StyleTitle",styleTitle);
		session.setAttribute("StyleAbstract",styleAbstract);
		session.setAttribute("IsDefault",isDefault);
		/*Añadimos en la Session el UserStyle creado por defecto*/
		session.setAttribute("UserStyle",userStyle);
		/*Creamos un nuevo UserStyle*/
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(0,new Integer(1));
		session.setAttribute("Insert",inserts);
		/*Creamos en la Session la que será la lista de FeatureTypeStyles*/
		ArrayList ftsList = new ArrayList();
		session.setAttribute("FtsList",ftsList);
		/*Creamos la lista de ScaleRanges y la metemos en la Session*/
		List scaleRangeList = new ArrayList();
		session.setAttribute("ScaleRangeList",scaleRangeList);
		session.removeAttribute("ScaleRangePosition");
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		pagesVisited.add("DisplayStyles");
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción*/
		ActionForward forward = frontController.getForward("InsertUpdateCustomStyle");
		return forward;
	}

}
