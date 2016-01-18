/**
 * GeopistaDeleteSelectedItemsPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;



import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.AttributeTab;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.GeometryEditor;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


//Say "delete" for features but "remove" for layers; otherwise, "delete layers" may
//sound to a user that we're actually deleting the file from the disk. [Jon Aquino]
public class GeopistaDeleteSelectedItemsPlugIn extends AbstractPlugIn {

      private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    Layer layerWithSelectedItems = null;


    public GeopistaDeleteSelectedItemsPlugIn() {}

    private GeometryEditor geometryEditor = new GeometryEditor();

    public boolean execute(final PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        ArrayList transactions = new ArrayList();
        for (Iterator i =
            /*((SelectionManagerProxy) context.getActiveInternalFrame())
                .getSelectionManager()
                .getLayersWithSelectedItems()
                .iterator();
            i.hasNext();*/
        	context.getWorkbenchContext().getLayerViewPanel().getSelectionManager()            
            .getLayersWithSelectedItems()
            .iterator();
        i.hasNext();
            ) {
            layerWithSelectedItems = (Layer) i.next();
            transactions.add(
                EditTransaction
                .createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(
                Geometry geometryWithSelectedItems,
                Collection selectedItems) {
                Geometry g = geometryWithSelectedItems;
                for (Iterator i = selectedItems.iterator(); i.hasNext();) {
                    Geometry selectedItem = (Geometry) i.next();
                    g = geometryEditor.remove(g, selectedItem);
                }
                return g;
            }
        },
            ((SelectionManagerProxy) context.getWorkbenchContext().getLayerViewPanel()),

            (LayerViewPanelContext) context.getWorkbenchGuiComponent(),

            getName(),
            layerWithSelectedItems,
            isRollingBackInvalidEdits(context),
            true));
        }
        return EditTransaction.commit(transactions);
    }


    private void setAttributesOf(Feature feature, Layer layer) {
    
    if(feature instanceof GeopistaFeature)
    {
	    GeopistaFeature geopistaFeature = (GeopistaFeature) feature;
	    if(geopistaFeature.getSchema() instanceof GeopistaSchema)
	    {
		    GeopistaSchema geopistaSchema = (GeopistaSchema) geopistaFeature.getSchema();
		    
		    String attributeName = geopistaSchema.getAttributeByColumn("fecha_baja");
		    
		    feature.setAttribute(attributeName, new Date());
		
		        layer.getLayerManager().fireFeaturesChanged(
		            Arrays.asList(new Feature[] { feature }),
		            FeatureEventType.ATTRIBUTES_MODIFIED,
		            layer);
	    	}
    	}
    }

    private void setAttributesNull(Feature feature, Layer layer) {
        if(feature instanceof GeopistaFeature)
        {
            GeopistaFeature geopistaFeature = (GeopistaFeature) feature;
	        if(geopistaFeature.getSchema() instanceof GeopistaSchema)
		    {
		        GeopistaSchema geopistaSchema = (GeopistaSchema) geopistaFeature.getSchema();
		        
		        String attributeName = geopistaSchema.getAttributeByColumn("fecha_baja");
		        
		        feature.setAttribute(attributeName, null);
		
		        layer.getLayerManager().fireFeaturesChanged(
		            Arrays.asList(new Feature[] { feature }),
		            FeatureEventType.ATTRIBUTES_MODIFIED,
		            layer);
		    }
        }
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck()
            .add(checkFactory.createWindowWithSelectionManagerMustBeActiveCheck())
            .add(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1))
            .add(checkFactory.createSelectedItemsLayersMustBeEditableCheck());
    }
    public void initialize(PlugInContext context) throws Exception {
        

        JPopupMenu popupMenu = context.getWorkbenchGuiComponent().getLayerViewPopupMenu();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

        AttributeTab.addPopupMenuItem(context.getWorkbenchContext(),
            this, aplicacion.getI18nString(this.getName()),
            false, null,
            GeopistaDeleteSelectedItemsPlugIn.createEnableCheck(context.getWorkbenchContext()));

        featureInstaller.addPopupMenuItem(popupMenu, this,
            aplicacion.getI18nString(this.getName()), false, null,
            GeopistaDeleteSelectedItemsPlugIn.createEnableCheck(context.getWorkbenchContext()));
        super.initialize(context);
        registerDeleteKey(context.getWorkbenchContext());
    }

    private void registerDeleteKey(final WorkbenchContext context) {
        context.getIWorkbench().getGuiComponent().addKeyboardShortcut(KeyEvent.VK_DELETE,
            0, this, createEnableCheck(context));
    }

}
