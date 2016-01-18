/**
 * GeopistaPrintPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;


import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.plugin.print.PrintLayoutPlugIn;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.UtilsPrintPlugin;
import com.geopista.util.printPanel01;
import com.geopista.util.printPanel03;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;





public class GeopistaPrintPlugIn extends ThreadedBasePlugIn {



	public static final String IMPRIMIRJOB = "ImprimirJob";
	public static final String IMPRIMIRPF = "ImprimirPf";
	public static final String IMPRIMIRMAPDOCUMENT = "ImprimirMapDocument";
	public static final String IMPRIMIRACCION = "ImprimirAccion";
	public static final String CARACTERISTICASIMPRESION = "CaracteristicasImpresion";

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	private Blackboard blackboardPrint = aplicacion.getBlackboard();

	private boolean isInitialize = false;
	private String toolBarCategory = "GeopistaPrintPlugIn.category";


	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
	}
	public String getName() {
		return "Print";
	}   

	public static WizardComponent addPanels(WizardComponent wizardComponent, WorkbenchContext context)
	{
		I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
		wizardComponent.addPanels(new WizardPanel[] {
				new printPanel01("printPanel01","printPanel03",context.createPlugInContext()),
				new printPanel03("printPanel03",null,context.createPlugInContext())
		});
		return wizardComponent;
	}

	public boolean execute(PlugInContext context) throws Exception {
		if (!isInitialize)
			initLoadPugin ();
		
		reportNothingToUndoYet(context);

		//Establecer datos configuracion para opcion impresion
		ConfigPrintPlugin config = new ConfigPrintPlugin(new PrintLayoutFrame(context));
		config.establecerAccionPlugin(false, false, true, false);
		config.setDatosEscalas(UtilsPrintPlugin.getDatosEscalasDisponibles());
		config.setIdEscala("-2"); //Escala vista actual en el editor
		config.setLstCuadriculas(new ArrayList());
		
		Blackboard blackboardPrint = aplicacion.getBlackboard();
		blackboardPrint.put(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN, config);
		
		WizardDialog wizard = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), UtilsPrintPlugin.getMessageI18N("PrintPlugIn.PrintMapTitle"), context.getErrorHandler());
		wizard.init(new WizardPanel[] {
				new printPanel01("DatosGenerales", "SeleccionarPantilla",context),
				new printPanel03("SeleccionarPantilla", null, context)
		});

		//Set size after #init, because #init calls #pack. [Jon Aquino]
		wizard.setSize(600,450);
		GUIUtil.centreOnWindow(wizard);
		wizard.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		wizard.setVisible(true);
		if (!wizard.wasFinishPressed()) {
			return false;
		}

		//Elimianmos elementos para no cargar memoria
		blackboardPrint.put(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN, null);
				
		return true;
	}
	

	public void run(TaskMonitor monitor, PlugInContext context) throws
	Exception {
		//
		// String action =
		// (String)blackboard.get(GeopistaPrintPlugIn.IMPRIMIRACCION);
		//          
		// if(action!=null)
		// {
		// if(action.equals("print"))
		// {
		// Book mapDocument = (Book)
		// blackboard.get(GeopistaPrintPlugIn.IMPRIMIRMAPDOCUMENT);
		// PageFormat pf = (PageFormat)
		// blackboard.get(GeopistaPrintPlugIn.IMPRIMIRPF);
		// PrinterJob job = (PrinterJob)
		// blackboard.get(GeopistaPrintPlugIn.IMPRIMIRJOB);
		// job.setPageable(mapDocument);
		// job.print();
		// }
		// }
		// blackboard.put("geopistaPrintPlugInPrintDialog",null);
	}

	public void initialize(PlugInContext context) throws Exception {
		initLoadPugin ();
		loadPluginInToolBar(context);
	}
	
	public void initLoadPugin () throws Exception {
		UtilsPrintPlugin.inicializarIdiomaPluginImpresion();
		I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
		isInitialize = true;
	}
	
	private void loadPluginInToolBar (PlugInContext context) throws Exception {
		String pluginCategory = aplicacion.getString(toolBarCategory);
		WorkbenchToolBar toolbar = ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory);
		toolbar.setTaskMonitorManager(new TaskMonitorManager());
		toolbar.addPlugIn(getIcon(), this, createEnableCheck(context.getWorkbenchContext()), context.getWorkbenchContext());
	}

	public ImageIcon getIcon() {
		return IconLoader.icon("print.gif");
	}
	
	public Blackboard getBlackboard() {
		return blackboardPrint;
	}
}
