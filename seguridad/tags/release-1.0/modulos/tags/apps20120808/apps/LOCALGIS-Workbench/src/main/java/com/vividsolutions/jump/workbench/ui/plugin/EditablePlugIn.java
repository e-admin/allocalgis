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

package com.vividsolutions.jump.workbench.ui.plugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.AttributeTab;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;

public class EditablePlugIn extends AbstractPlugIn {
   
    private EditingPlugIn editingPlugIn;
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        boolean makeEditable = ! ((Layer)context.getSelectedLayer(0)).isEditable();
        for (Iterator i = Arrays.asList(context.getSelectedLayers()).iterator(); i.hasNext(); ) {
        	GeopistaLayer selectedLayer = (GeopistaLayer) i.next();
//            boolean permiso = operaciones.obtenerPermisoCapa(selectedLayer.getSystemId(),SecurityManager.getPrincipal().getName(),GeopistaPermission.LEER_CAPA);
//            selectedLayer.setEditable(makeEditable && permiso);
            selectedLayer.setEditable(makeEditable);
            if (selectedLayer.isVersionable() && selectedLayer.getRevisionActual() != -1){
            	context.getLayerViewPanel().getContext().setStatusMessage("Capa "+selectedLayer.getName()+" no se puede poner como editable. Revision Actual:"+selectedLayer.getRevisionActual());
            	selectedLayer.setEditable(false);
            }
            else{            	
            	context.getLayerViewPanel().getContext().setStatusMessage("Capa "+selectedLayer.getName()+" fijada como editable. Revision Actual:"+selectedLayer.getRevisionActual());
            }
/*            if (selectedLayer.isDinamica())
            	selectedLayer.setEditable(false);
*/        }
        if (editingPlugIn!=null && makeEditable && !editingPlugIn.getToolbox(context.getWorkbenchContext()).isVisible()) {
            editingPlugIn.execute(context);
        }
        return true;
    }

    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
            .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1))
            .add(new EnableCheck() {
            public String check(JComponent component) {
            	boolean editable = ((Layer)workbenchContext.createPlugInContext().getSelectedLayer(0)).isEditable();
/*        		if (((Layer)workbenchContext.createPlugInContext().getSelectedLayer(0)).isDinamica())
        			editable = false;*/
                ((JCheckBoxMenuItem) component).setSelected(editable);
                return null;
            }
        });
    }

    public EditablePlugIn(EditingPlugIn editingPlugIn) {
        this.editingPlugIn = editingPlugIn;
    }
    
    public EditablePlugIn() {
     
    }

    public void initialize(PlugInContext context) throws Exception {

      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
      
      JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench()
                                                        .getGuiComponent()
                                                        .getLayerNamePopupMenu();

      featureInstaller.addPopupMenuItem(layerNamePopupMenu,
            this, GeopistaFunctionUtils.i18n_getname(this.getName()), true,
            null,
            this.createEnableCheck(context.getWorkbenchContext()));
            
      AttributeTab.addPopupMenuItem(context.getWorkbenchContext(), this,
    		  GeopistaFunctionUtils.i18n_getname(this.getName()), true, null,
            this.createEnableCheck(context.getWorkbenchContext()));
    }

    
    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con = AppContext.getApplicationContext().getConnection();
        con.setAutoCommit(false);
        return con;
    }  

}
