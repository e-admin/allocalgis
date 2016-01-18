/*
 * Created on 17-sep-2004
 *
 */
package com.geopista.style.filtereditor.controller.actions;

import javax.swing.tree.MutableTreeNode;

import com.geopista.style.filtereditor.model.impl.PropertyName;

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
public class ChangePropertyNameAction implements Action {

	/* (non-Javadoc)
	 * @see es.enxenio.util.controller.Action#doExecute(es.enxenio.util.controller.Request)
	 */
	public ActionForward doExecute(Request request) {

		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Session session = SessionImpl.getInstance();
		MutableTreeNode node = (MutableTreeNode)request.getAttribute("Node");
		PropertyName propertyName = (PropertyName)node;
		/*Insertamos en la Session el PropertyName recuperado de la Request*/
		session.setAttribute("PropertyName",propertyName);
		session.setAttribute("InsertExpression",new Integer(0));
		/*Redirigimos a otra acción*/
		Action action = frontController.getAction("GetFeatureAttributesMap");
		ActionForward forward = action.doExecute(null);
		return forward;
	}
}
