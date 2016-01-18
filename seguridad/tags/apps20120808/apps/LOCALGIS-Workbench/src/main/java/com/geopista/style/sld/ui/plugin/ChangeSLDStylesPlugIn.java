/*
 * Created on 12-may-2004
 */
package com.geopista.style.sld.ui.plugin;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.style.sld.model.SLDFactory;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.controller.Action;
import es.enxenio.util.controller.ActionForward;
import es.enxenio.util.controller.FrontController;
import es.enxenio.util.controller.FrontControllerFactory;
import es.enxenio.util.controller.Request;
import es.enxenio.util.ui.PanelContainer;
import es.enxenio.util.ui.UIFactory;

/**
 * @author Enxenio, S.L.
 */
public class ChangeSLDStylesPlugIn extends AbstractPlugIn {
	private final static String LAST_TAB_KEY = ChangeSLDStylesPlugIn.class.getName() +
		" - LAST TAB";

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private String toolBarCategory = "ChangeSLDStylesPlugIn.category";

	public ChangeSLDStylesPlugIn() {
	}

	public boolean execute(PlugInContext context) throws Exception {
		final Layer layer = (Layer) context.getSelectedLayer(0);

		SLDStyle theStyle = (SLDStyle)layer.getStyle(SLDStyle.class);
		if (theStyle==null)// No se ha inicializado con estilo SLD
		{
			//TODO: 
			
			// Nuevo ArrayList para que no haya ConcurrentModificationException 
			// en el hilo de refresco gráfico
			Iterator styleList = new ArrayList(layer.getStyles()).iterator();
			while (styleList.hasNext())
			{
				Object style = styleList.next();
				BasicStyle oldStyle;
				if (style instanceof BasicStyle)
				{
					oldStyle = (BasicStyle) style;
					theStyle = new SLDStyleImpl(oldStyle.getFillColor(), oldStyle
							.getLineColor(), layer.getName());
					layer.removeStyle(oldStyle);
				}
				
			}
			if (theStyle == null) 
			    theStyle = SLDFactory.createDefaultSLDStyle(layer.getName());
			layer.addStyle(theStyle);
			layer.getLabelStyle().setEnabled(false);
			layer.getVertexStyle().setEnabled(false);
		}
		PanelContainer pcd = UIFactory.createPanelContainer(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), true);

		Request theRequest = FrontControllerFactory.createRequest();
		theRequest.setAttribute("SLDStyle", theStyle);
		theRequest.setAttribute("Layer", layer);
		theRequest.setAttribute("LayerName", layer.getName());
		FrontController fc =  FrontControllerFactory.getFrontController();
		Action theAction = fc.getAction("QuerySLDStyle"); 
		ActionForward theActionForward = theAction.doExecute(theRequest);		
		pcd.forward(theActionForward, theRequest);

		boolean firingEvents = layer.getLayerManager().isFiringEvents();
		layer.getLayerManager().setFiringEvents(false);
		try {
		} finally {
			layer.getLayerManager().setFiringEvents(firingEvents);
		}
		layer.fireAppearanceChanged();

		return true;
	}

	public void initialize(PlugInContext context) throws Exception {
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(this.getIcon(),
			this,
			createEnableCheck(context.getWorkbenchContext()),
			context.getWorkbenchContext());
		ConfigurationParametersManager.addConfigurationFile("GeoPista.properties");
		
		 FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	      
	      JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
	                                                        .getGuiComponent()
	                                                        .getLayerNamePopupMenu();
	      featureInstaller.addPopupMenuItem(layerNamePopupMenu,
	            this, aplicacion.getI18nString(this.getName()) + "...", false,
	            GUIUtil.toSmallIcon(this.getIcon()),
	            this.createEnableCheck(context.getWorkbenchContext()));
		
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("SLDStyle.gif");
	}

	public MultiEnableCheck createEnableCheck(
		final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
									 .add(checkFactory.createExactlyOneLayerMustBeSelectedCheck());
	}
}



