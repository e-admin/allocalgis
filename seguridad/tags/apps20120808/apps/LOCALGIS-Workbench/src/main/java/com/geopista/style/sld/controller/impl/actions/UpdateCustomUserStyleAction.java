/*
 * Created on 28-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.UserStyle;
import org.deegree_impl.graphics.sld.StyleFactory;

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
public class UpdateCustomUserStyleAction implements Action {

	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		/*Recuperamos los parámetros de la Request*/
		String styleName = (String)request.getAttribute("StyleName");
		SLDFacade sldFacade = null;
		try {
			sldFacade = SLDFactory.getDelegate();
		} catch(InternalErrorException e) {
			System.err.println(e);
		}
		/*Llamamos al caso de uso del modelo*/
		ArrayList userStyleList = (ArrayList)session.getAttribute("UserStyleList");
		UserStyle userStyle = sldFacade.getUserStyle(styleName,userStyleList);
		/*Metemos en la Session los atributos del UserStyle*/
		String styleName2 = null;
		if (userStyle.getName()!=null) {
			styleName2 = new String(userStyle.getName());
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
		session.setAttribute("StyleName",styleName2);
		session.setAttribute("StyleTitle",styleTitle);
		session.setAttribute("StyleAbstract",styleAbstract);
		session.setAttribute("IsDefault",isDefault);
		/*Añadimos en la Session el UserStyle que se ha recuperado*/
		session.setAttribute("UserStyle",userStyle);
		/*Actualizamos un UserStyleExistente*/
		List inserts = (List)session.getAttribute("Insert");
		inserts.add(0,new Integer(0));
		session.setAttribute("Insert",inserts);
		/*Insertamos en la sesión una lista que contiene a los FeatureTypeStyles del UserStyle*/
		FeatureTypeStyle[] ftsArray = userStyle.getFeatureTypeStyles();
		List ftsList = Arrays.asList(ftsArray);
		/*Clonamos la lista de FeatureTypeStyles por si el usuario decide deshacer los cambios que haya hecho*/		
		ArrayList newFtsList = new ArrayList();
		Iterator ftsListIterator = ftsList.iterator();
		while (ftsListIterator.hasNext()) {
			FeatureTypeStyle fts = (FeatureTypeStyle)ftsListIterator.next();
			FeatureTypeStyle newFts = clone(fts);
			newFtsList.add(newFts);
		}
		session.setAttribute("FtsList",newFtsList);
		/*Actualizamos el parámetro PagesVisited en la Session*/
		List pagesVisited = (List)session.getAttribute("PagesVisited");
		pagesVisited.add("DisplayStyles");
		session.setAttribute("PagesVisited",pagesVisited);
		/*Redirigimos a otra acción del controlador*/
		Action action = frontController.getAction("CreateScaleRangeList");
		ActionForward forward = action.doExecute(null);
		return forward;
	}
	
	private FeatureTypeStyle clone(FeatureTypeStyle fts) {
		
		FeatureTypeStyle newFts = StyleFactory.createFeatureTypeStyle(fts.getName(),fts.getTitle(),
			fts.getAbstract(),fts.getFeatureTypeName(),fts.getRules());
		return newFts;
	}

}
