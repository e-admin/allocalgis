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
package com.geopista.ui.plugin.calculateExpresion;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.calculateExpresion.panels.CalculatorExpresionPanel;
import com.geopista.ui.plugin.calculateExpresion.panels.SelectionTablesPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * GeopistaFeatureCalculateMultiplePlugIn
 * Plugin que permite editar múltiples features simultáneamente
 * utilizando expresiones lógicas que permiten el calcular un
 * campo en referencia a los valores de otros.
 */

public class GeopistaFeatureCalculateExpresionsPlugIn extends AbstractPlugIn
{
   ApplicationContext appContext=AppContext.getApplicationContext();
   static public final ImageIcon ICON=IconLoader.icon("Calculate.gif");
	 public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
            .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())            
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createExactlyNLayersMustBeSelectedCheck(1));


    }


  
  public void initialize(PlugInContext context) throws Exception
  {
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(appContext.getString("CalculateFeature.category")).addPlugIn(
            getIcon(), this,
            createEnableCheck(context.getWorkbenchContext()),
        context.getWorkbenchContext());


        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

       
        featureInstaller.addPopupMenuItem(popupMenu, this,
        		GeopistaUtil.i18n_getname(getName()), false,
				GUIUtil.toSmallIcon(ICON),
				createEnableCheck(context.getWorkbenchContext()));
      
    
  }
  /**
   * execute (PlugInContext context)
   * Al ejecutarse el plugin se muestra la calculadora de expresiones matemáticas
   * que permite realizar opraciones sobre los diferentes 
   * 
   * @param context : Contexto del PlugIn
   * @return Devuelve false si no se ha podido ejecutar la operación, true en caso contrario
   */
  public boolean execute (PlugInContext context) throws Exception
  {
      //Creamos el dialogo
      JDialog d=new JDialog();
      
//     SelectionTablesPanel selectionTablePanel = new SelectionTablesPanel();
//     selectionTablePanel.setVisible(true);
//     d.getContentPane().add(selectionTablePanel);
//     
		  //Creamos el panel Calculate y le pasamos el contexto 
      CalculatorExpresionPanel calculatorPanel = new CalculatorExpresionPanel(context);
      calculatorPanel.setVisible(true);
       d.getContentPane().add(calculatorPanel);
		
      
      d.setSize(800,600);
      d.setModal(true);
      d.setTitle(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn"));
      d.show();

     
      execute(new UndoableCommand(getName()) {
                public void execute() {
                    
                }

                public void unexecute() {
                    
                }
            }, context);

      
      return false;
  }
  
   public ImageIcon getIcon() {
        return ICON;
    }
    
   public String getName() {
        return "GeopistaFeatureCalculateMultiple";
    }
  
}
