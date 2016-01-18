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


import java.awt.print.Book;
import java.awt.print.PrinterJob;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.dialogs.ImprimirSerieVistaPreliminarPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaPrintJPanel03;
import com.geopista.util.I18NUtils;
import com.geopista.util.SelectSheetLayerPrintPanel;
import com.geopista.util.printPanel01;
import com.geopista.util.printPanel02;
import com.geopista.util.printPanel03;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;



public class SeriePrintPlugIn  extends GeopistaPrintPlugIn {

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  private String toolBarCategory = "SeriePrintPlugIn.category";
  
	
	public static final String IMPRIMIRBOOK = "SeriePrintPlugIn.ImprimirBook";
  public static final String CAPAPLANTILLA = "SeriePrintPlugIn.CapaPlantilla";
	public final static String LAYER = aplicacion.getString("SeriePrintPlugIn.PlantillaImpresionSerie");  
	private MultiInputDialog dialog;

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
           .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createAtLeastNLayersMustExistCheck(2));
    }
    public String getName() {
        return aplicacion.getI18nString("SeriesPrintPlugin");
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

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);


      	Blackboard blackboardPrint = aplicacion.getBlackboard();
        
        if(blackboardPrint.get("geopistaSeriePrintPlugInPrintDialog")==null)
        {

            WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                    aplicacion.getI18nString("SeriePrintPlugIn.PrintMapDialogTitle"), context.getErrorHandler());
            d.init(new WizardPanel[] {new SelectSheetLayerPrintPanel("SelectSheetLayerPrintPanel","printPanel01",context.getWorkbenchContext()),
                                      new printPanel01("printPanel01","2",context),
//                                      new printPanel02("printPanel02","GeopistaPrintJPanel03",context),
//                                      new GeopistaPrintJPanel03("GeopistaPrintJPanel03","ImprimirSerieVistaPreliminarPanel"),
//                                      new ImprimirSerieVistaPreliminarPanel("ImprimirSerieVistaPreliminarPanel",null,context)
                                      new printPanel03("2",null,context) 
                                      });

            //Set size after #init, because #init calls #pack. [Jon Aquino]
            d.setSize(600,350);
            GUIUtil.centreOnWindow(d);
            d.setVisible(true);
            getBlackboard().put(SeriePrintPlugIn.CAPAPLANTILLA,null);
            if (!d.wasFinishPressed()) {
                return false;
            }
        }

        return true;

    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {

          String action = (String)getBlackboard().get(SeriePrintPlugIn.IMPRIMIRACCION);
          getBlackboard().put("geopistaSerirPrintPlugInPrintDialog",null);
          if(action!=null)
          {
            if(action.equals("print"))
            { 
              Book book =(Book) getBlackboard().get(SeriePrintPlugIn.IMPRIMIRBOOK);

              PrinterJob job = (PrinterJob) getBlackboard().get(SeriePrintPlugIn.IMPRIMIRJOB);
              job.setPageable(book);
              job.print();
            }
          }
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), I18NUtils.i18n_getname("ImprimirSerie"), true);

        dialog.setSideBarImage(IconLoader.icon("printSerie.gif"));
        dialog.setSideBarDescription(
            aplicacion.getI18nString("ImprimirSerieFeature"));
        String fieldName = LAYER;
        Layer capaParcelas = capaParcelas=(Layer)context.getCandidateLayer(0);
        
        
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, capaParcelas, null, context.getLayerManager());
        GUIUtil.centreOnWindow(dialog);
    }

    public void initialize(PlugInContext context) throws Exception {
       String pluginCategory = aplicacion.getString(toolBarCategory);
      ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).setTaskMonitorManager(new TaskMonitorManager());
      ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
      		getIcon(), this,
      		createEnableCheck(context.getWorkbenchContext()),
			context.getWorkbenchContext());
       
    }

    public ImageIcon getIcon() {
        return IconLoader.icon("printSerie.gif");
    }
}
