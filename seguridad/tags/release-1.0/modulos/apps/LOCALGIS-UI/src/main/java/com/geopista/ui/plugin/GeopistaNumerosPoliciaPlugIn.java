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
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.geopista.app.patrimonio.estructuras.Estructuras;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.ui.dialogs.GeopistaNumerosPoliciaDialog;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * GeopistaNumerosPoliciaPlugIn
 * Plugin que permite realizar búsquedas por números de policía
 * si la capa números de policía está cargada
 */

public class GeopistaNumerosPoliciaPlugIn extends AbstractPlugIn 
{
   private ApplicationContext appContext=AppContext.getApplicationContext();
   private String toolBarCategory = "GeopistaNumerosPoliciaPlugIn.category";
      
   public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {

        GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
            .add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
            .add(checkFactory.createLayerNumerosPoliciaBeExistCheck())
            .add(checkFactory.createLayerStreetBeExistCheck())
            ;

  }

  public String getName() {
        return appContext.getI18nString("GeopistaNumerosPoliciaPlugInDescription");
  }

  public boolean execute(PlugInContext context) throws Exception {

      List nameDomain= new LinkedList();
       
      //no deja introducir en un a lista un objeto LitaEstructuras
      ListaEstructuras listaTiposViaIni= new ListaEstructuras("Tipos de via normalizados del INE");
      nameDomain.add(listaTiposViaIni);
      
    //En este punto cargo los domains de Estructuras
      while (!Estructuras.isCargada())
      {
          if (!Estructuras.isIniciada()) 
              Estructuras.cargarEstructuras(nameDomain);
          
          try {Thread.sleep(500);}catch(Exception e){}
      }
      
    JDialog d = new GeopistaNumerosPoliciaDialog(context,appContext.getMainFrame());
      //JDialog d = new GeopistaNumerosPoliciaDialog(context);
    return true;
  }
  public void initialize(PlugInContext context) throws Exception {
      
       String pluginCategory = appContext.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(
          		getIcon(), this,
          		createEnableCheck(context.getWorkbenchContext()),
            context.getWorkbenchContext());
        

    }

  public ImageIcon getIcon() {
        return IconLoader.icon("numerospolicia.gif");
  }
}