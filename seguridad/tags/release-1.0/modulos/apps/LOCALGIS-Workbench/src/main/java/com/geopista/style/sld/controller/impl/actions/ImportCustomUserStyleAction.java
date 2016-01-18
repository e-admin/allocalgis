/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.controller.impl.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.NamedLayer;
import org.deegree.graphics.sld.StyledLayerDescriptor;
import org.deegree_impl.graphics.sld.SLDFactory;
import org.deegree_impl.graphics.sld.StyleFactory;

import com.geopista.app.AppContext;
import com.geopista.style.sld.model.SLDStyle;

import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.controller.Session;
import es.enxenio.util.controller.impl.FrontControllerImpl;
import es.enxenio.util.controller.impl.SessionImpl;

/**
 * @author enxenio s.l.
 *
 */
public class ImportCustomUserStyleAction implements Action {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	/* (non-Javadoc)
	 * @see com.geopista.style.sld.controller.Action#doExecute(com.geopista.style.sld.controller.Request)
	 */
	public ActionForward doExecute(Request request) {
		/*Recuperamos las instancias del FrontController y de la Session*/
		FrontController frontController = FrontControllerImpl.getInstance();
		Request theRequest = FrontControllerFactory.createRequest();
		Session session = SessionImpl.getInstance();
		
	    try{
    		File styleFile = new File((String)request.getAttribute("FileExport"));
    		InputStreamReader isr = new FileReader(styleFile);
    		StyledLayerDescriptor sld = SLDFactory.createSLD(isr);
    		SLDStyle theStyle = (SLDStyle)(FrontControllerFactory.getSession()).getAttribute("SLDStyle");
    		List userStyles = theStyle.getStyles();
    		Collections.addAll(userStyles, ((NamedLayer)sld.getNamedLayers()[0]).getStyles());
    		theStyle.setStyles(userStyles);  
			/*Metemos los parámetros en la Request*/
			theRequest.setAttribute("SLDStyle", theStyle);
			theRequest.setAttribute("Layer", request.getAttribute("Layer"));
			theRequest.setAttribute("LayerName", request.getAttribute("LayerName"));
			/*Redirigimos a otra acción del controlador*/
			Action action = frontController.getAction("QuerySLDStyle");
			ActionForward forward = action.doExecute(theRequest);
			return forward;
		} catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.FicheroNoExiste"));		
            return null;
		} catch(Exception e) {
            JOptionPane.showMessageDialog(null, aplicacion.getI18nString("SLDStyle.ErrorImportar"));		
            return null;
		}
	}
	
	private FeatureTypeStyle clone(FeatureTypeStyle fts) {
		
		FeatureTypeStyle newFts = StyleFactory.createFeatureTypeStyle(fts.getName(),fts.getTitle(),
			fts.getAbstract(),fts.getFeatureTypeName(),fts.getRules());
		return newFts;
	}
}