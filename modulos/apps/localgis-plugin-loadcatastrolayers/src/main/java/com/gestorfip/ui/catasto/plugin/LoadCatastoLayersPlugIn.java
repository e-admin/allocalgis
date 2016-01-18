package com.gestorfip.ui.catasto.plugin;

/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.server.administradorCartografia.ACLayerFamily;
import com.geopista.server.administradorCartografia.ObjectNotFoundException;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.LoadSystemLayers;
import com.geopista.util.exception.CancelException;
import com.gestorfip.ui.catasto.images.IconLoader;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

public class LoadCatastoLayersPlugIn extends ThreadedBasePlugIn {

	
	private static final Log	logger	= LogFactory.getLog(LoadCatastoLayersPlugIn.class);

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	private Blackboard blackboard = aplicacion.getBlackboard();

	public LoadCatastoLayersPlugIn() {
	}

	public void initialize(PlugInContext context) throws Exception {

		 context.getFeatureInstaller().addMainMenuItem(this,
                 new String[] { "LoadCatastroLayers" },
                 getName(),
                 false,
                 null,
                 null);
		 
		 context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar("LoadCatastroLayers").addPlugIn(
					getIcon(), this,
					createEnableCheck(context.getWorkbenchContext()),
					context.getWorkbenchContext());
		 
			JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();

	        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	        featureInstaller.addPopupMenuItem(popupMenu,
	                this, AppContext.getApplicationContext().getI18nString(this.getName()), false, null,
	                createEnableCheck(context.getWorkbenchContext()));

	}

	public boolean execute(PlugInContext context) throws Exception {

		if (!aplicacion.isLogged()) {

			aplicacion.setProfile("Geopista");
			aplicacion.login();
		}

		if (aplicacion.isLogged()) {

//			WizardDialog d = new WizardDialog(GeopistaUtil.getFrame(context
//					.getWorkbenchGuiComponent()), aplicacion
//					.getI18nString("LoadSystemLayerDialog"), context
//					.getErrorHandler());
//			d.init(new WizardPanel[] { new LoadCatastroLayersPanel01(
//					"LoadCatastroLayersPanel01", null, context) });
//
//			// Set size after #init, because #init calls #pack. [Jon Aquino]
//			d.setSize(550, 450);
//			GUIUtil.centreOnWindow(d);
//			d.setVisible(true);
//			if (!d.wasFinishPressed()) {
//				return false;
//			}

			return true;
		} else {
			return false;
		}
	}

	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {

		String nameLayerFamily=null;
		try{
			ACLayerFamily layerFamilyCatastro = new ACLayerFamily();
			ACLayerFamily layerFamilyNumPolicia = new ACLayerFamily();
			ACLayerFamily layerFamilyParcelario = new ACLayerFamily();
			
			layerFamilyCatastro.setName("Catastro");
			layerFamilyCatastro.setId(8);
			
			layerFamilyNumPolicia.setName("Numeros de Policia");
			layerFamilyNumPolicia.setId(12);
			
			layerFamilyParcelario.setName("Parcelario");
			layerFamilyParcelario.setId(13);
			
			ArrayList layerFamiliesIDsList = new ArrayList();
			layerFamiliesIDsList.add(layerFamilyCatastro);
			layerFamiliesIDsList.add(layerFamilyNumPolicia);
			layerFamiliesIDsList.add(layerFamilyParcelario);
			
			LoadSystemLayers.loadLayers(layerFamiliesIDsList, context, monitor);
				
		}
		catch (CancelException e1){
      	  JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa cancelada");
		}
		 catch (Exception e)
         {
			logger.error("run(TaskMonitor, PlugInContext)", e);
             Throwable errorCause = e.getCause();
             if(errorCause instanceof PermissionException)
             {
             	String error=errorCause.getMessage();
                 JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
                 		aplicacion.getI18nString("LoadSystemLayersPlugIn.NoPermisosCargarCapa")  + nameLayerFamily+"\n Error detallado:"+error);
             }
             else
             {
                 if(errorCause!=null)
                 {
                     Throwable subErrorCause = errorCause.getCause();
                     if(subErrorCause instanceof ObjectNotFoundException)
                     {

                         JOptionPane.showMessageDialog(aplicacion.getMainFrame(),subErrorCause.getMessage() + " " + aplicacion.getI18nString("GeopistaLoadMapPlugIn.DarAltaMunicipio"));
                     }
                     else
                     {
                         ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("LoadSystemLayersPlugIn.ProblemasCargarCapa") + " " + nameLayerFamily, aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + nameLayerFamily, StringUtil
                             .stackTrace(e));
                     }
                 }
                 else
                 {
                     ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("LoadSystemLayersPlugIn.ProblemasCargarCapa") + " " +nameLayerFamily, aplicacion.getI18nString("GeopistaLoadMapPlugIn.ProblemasCargarMapa") + " " + nameLayerFamily, StringUtil
                             .stackTrace(e));
                 }
             }
				logger.error("run(TaskMonitor, PlugInContext)", e);
         }
	}

	public static MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
				workbenchContext);

		return new MultiEnableCheck().add(checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck());

	}
	  public ImageIcon getIcon() {
			return IconLoader.icon("loadCatastro.gif");
		}

}
