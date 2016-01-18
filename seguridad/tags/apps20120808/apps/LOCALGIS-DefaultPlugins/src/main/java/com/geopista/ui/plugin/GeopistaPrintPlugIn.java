package com.geopista.ui.plugin;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/


import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.print.PrintLayoutPlugIn;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
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
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;





public class GeopistaPrintPlugIn extends ThreadedBasePlugIn {

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	private String toolBarCategory = "GeopistaPrintPlugIn.category";
	   
	public static final String IMPRIMIRJOB = "ImprimirJob";
	public static final String IMPRIMIRPF = "ImprimirPf";
	public static final String IMPRIMIRMAPDOCUMENT = "ImprimirMapDocument";
	public static final String IMPRIMIRACCION = "ImprimirAccion";
	public static final String DESCRIPCIONMAPAIMPRIMIR = PrintLayoutPlugIn.GEOPISTA_PRINT_MAPDESCRIPTION;
	public static final String TITULOMAPAIMPRIMIR = PrintLayoutPlugIn.GEOPISTA_PRINT_MAPTITLE;
	public static final String MOSTRARLEYENDAIMPRIMIR = "MostrarLeyendaImprimir";
	public static final String ESCALASELECCIONADAIMPRIMIR = "EscalaSeleccionadaImprimir";
	public static final String ESCALAIMPRIMIRMAPA = "EscalaImprimirMapa";
	public static final String CARACTERISTICASIMPRESION = "CaracteristicasImpresion";


	private Blackboard blackboardPrint = aplicacion.getBlackboard();
	
	
	
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
    }
    public String getName() {
        return "Print";
    }   

    public static WizardComponent addPanels(WizardComponent wizardComponent, WorkbenchContext context)
    {
    	I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
        wizardComponent.addPanels(new WizardPanel[] {new printPanel01("printPanel01","printPanel03",context.createPlugInContext()),
                					new printPanel03("printPanel03",null,context.createPlugInContext())
                                   });
        return wizardComponent;
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);


        if(blackboardPrint.get("geopistaPrintPlugInPrintDialog")==null)
        {
          PrintLayoutPlugIn p = new PrintLayoutPlugIn();
          I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
            
          WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
        		   aplicacion.getI18nString("PrintMapDialogTitle")
          , context.getErrorHandler());
          d.init(new WizardPanel[] {new printPanel01("1","2",context),
                  					new printPanel03("2",null,context)
                                    });
         


          //Set size after #init, because #init calls #pack. [Jon Aquino]
          d.setSize(600,450);
          GUIUtil.centreOnWindow(d);
          d.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
          d.setVisible(true);
          if (!d.wasFinishPressed()) {
              return false;
          }
          
          return true;
        }
       

        return true;

    }

	 public void run(TaskMonitor monitor, PlugInContext context) throws
	 Exception {
	//
	// String action =
	// (String)blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRACCION);
	//          
	// if(action!=null)
	// {
	// if(action.equals("print"))
	// {
	// Book mapDocument = (Book)
	// blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRMAPDOCUMENT);
	// PageFormat pf = (PageFormat)
	// blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRPF);
	// PrinterJob job = (PrinterJob)
	// blackboardInformes.get(GeopistaPrintPlugIn.IMPRIMIRJOB);
	// job.setPageable(mapDocument);
	// job.print();
	// }
	// }
	// blackboardInformes.put("geopistaPrintPlugInPrintDialog",null);
	    }

    public void initialize(PlugInContext context) throws Exception {
       
    	I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).setTaskMonitorManager(new TaskMonitorManager());
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
      		getIcon(), this,
      		createEnableCheck(context.getWorkbenchContext()),
			context.getWorkbenchContext());
       
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("print.gif");
    }
	public Blackboard getBlackboard()
	{
		return blackboardPrint;
	}
}
