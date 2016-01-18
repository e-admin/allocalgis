/**
 * SeriePrintPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;


import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.dialogs.ImprimirSerieVistaPreliminarPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.bean.ConfigPrintPlugin;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.plugin.print.PrintLayoutPlugIn;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ConfigOptionsPrintPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaPrintJPanel03;
import com.geopista.util.SelectActionPrintPanel;
import com.geopista.util.SelectPrintItemsPanel;
import com.geopista.util.SelectSheetLayerPrintPanel;
import com.geopista.util.UtilsPrintPlugin;
import com.geopista.util.printPanel01;
import com.geopista.util.printPanel02;
import com.geopista.util.printPanel03;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.WorkbenchToolBar;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;



public class SeriePrintPlugIn  extends GeopistaPrintPlugIn {

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	private String toolBarCategory = "SeriePrintPlugIn.category";

	public static final String IMPRIMIR_BOOK = "SeriePrintPlugIn.ImprimirBook";
	public final static String DEFAULT_LAYER_ITERACION = aplicacion.getString("SeriePrintPlugIn.PlantillaImpresionSerie");

	private boolean isInitialize = false;
	private MultiInputDialog dialog;

	
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
		return IconLoader.icon("printSerie.gif");
	}
	
	public String getName() {
		return "SeriesPrintPlugin";
	}    

	public static WizardComponent addPanels(WizardComponent wizardComponent, WorkbenchContext context)
	{
		Layer selectLayer = null;
		try
		{
			wizardComponent.addPanels(new WizardPanel[] {new SelectSheetLayerPrintPanel("SelectSheetLayerPrintPanel","printPanel01",context),
					new printPanel01("printPanel01","printPanel02",context.createPlugInContext()),
					new printPanel02("printPanel02","GeopistaPrintJPanel03",context.createPlugInContext()),
					new GeopistaPrintJPanel03("GeopistaPrintJPanel03","ImprimirSerieVistaPreliminarPanel"),
					new ImprimirSerieVistaPreliminarPanel("ImprimirSerieVistaPreliminarPanel",null,context.createPlugInContext())});

		}catch(Exception e)
		{

		}
		return wizardComponent;
	}

	public boolean execute(PlugInContext context) throws Exception 
	{
		if (!isInitialize)
			initLoadPugin ();
		
		reportNothingToUndoYet(context);
		
		Blackboard blackboardPrint = aplicacion.getBlackboard();
		blackboardPrint.put(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN, new ConfigPrintPlugin(new PrintLayoutFrame(context)));
		
		WizardDialog wizard = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), UtilsPrintPlugin.getMessageI18N("PrintPlugIn.PrintMapSerieTitle"), context.getErrorHandler());
		
		//Establecer panel siguiente para opciones segun accion: crear plantilla, modificar plantilla, imprimir
		String[] arrayAccionSgte = {null, "SeleccionarPantilla", "DatosGenerales"};
		
		//Configuracion wizard
		wizard.init(new WizardPanel[] {
				new SelectActionPrintPanel("DatosAccion", arrayAccionSgte, context),
				new printPanel01("DatosGenerales", "DatosEscala",context),
				new printPanel02("DatosEscala", "DatosIteracion",context),
				new SelectSheetLayerPrintPanel("DatosIteracion", "DatosSelectEltosImpresion", context.getWorkbenchContext()),
				new SelectPrintItemsPanel("DatosSelectEltosImpresion", "DatosOpcionesImpresion", context),
				new ConfigOptionsPrintPanel("DatosOpcionesImpresion", "VistaPreliminar", context),
				new ImprimirSerieVistaPreliminarPanel("VistaPreliminar","SeleccionarPantilla",context),
				new printPanel03("SeleccionarPantilla", null, context) 
		});
		
		//Set size after #init, because #init calls #pack. [Jon Aquino]
		wizard.setSize(600,350);
		GUIUtil.centreOnWindow(wizard);
		wizard.setVisible(true);
		
		if (!wizard.wasFinishPressed()) {
			return false;
		}
		
		//Elimianmos elementos para no cargar memoria
		blackboardPrint.put(UtilsPrintPlugin.KEY_CONFIG_SERIE_PRINT_PLUGIN, null);

		return true;
	}
	
	public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(2));
	}
}
