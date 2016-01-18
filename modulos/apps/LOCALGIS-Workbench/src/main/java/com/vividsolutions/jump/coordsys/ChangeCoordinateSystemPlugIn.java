/**
 * ChangeCoordinateSystemPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.coordsys;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/** 
 * Implements a {@link com.vividsolutions.jump.workbench.plugin.PlugIn
 * PlugIn} that allows the user to change coordinate systems.
 *
 */
public class ChangeCoordinateSystemPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        //Don't make this plug-in undoable -- it's a lot of data to store in memory [Jon Aquino]
        context.getLayerManager().getUndoableEditReceiver()
               .reportIrreversibleChange();
        
        CoordinateSystem destination = (CoordinateSystem) JOptionPane
                .showInputDialog((Component)context.getWorkbenchGuiComponent(),
                        AppContext.getApplicationContext().getI18nString("SelectCoordinateReferenceSystem"), 
                        AppContext.getApplicationContext().getI18nString("CoordinateReferenceSystem"),
                        JOptionPane.PLAIN_MESSAGE, null, new ArrayList(
                                CoordinateSystemRegistry
                                        .instance(context.getWorkbenchContext()
                                                .getBlackboard())
                                        .getCoordinateSystems())
                                .toArray(),
                        context.getLayerManager().getCoordinateSystem());

        if (destination == null) {
            return false;
        }

        if (context.getLayerManager().getCoordinateSystem() == destination) {
            return true;
        }

        if (Reprojector.instance().wouldChangeValues(context.getLayerManager()
                                                                .getCoordinateSystem(),
                    destination)) {
            //Two-phase commit [Jon Aquino]
            ArrayList transactions = new ArrayList();

            for (Iterator i = context.getLayerManager().iterator();
                    i.hasNext();) {
                Layer layer = (Layer) i.next();
                EditTransaction transaction = new EditTransaction(layer.getFeatureCollectionWrapper()
                                                                       .getFeatures(),
                        getName(), layer, isRollingBackInvalidEdits(context),
                        false, context.getLayerViewPanel());

                for (int j = 0; j < transaction.size(); j++) {
                    Reprojector.instance().reproject(transaction.getGeometry(j),
                        context.getLayerManager().getCoordinateSystem(),
                        destination);
                }

                transactions.add(transaction);
            }

            EditTransaction.commit(transactions);
        }
        // cambia 

        for (Iterator i = context.getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            layer.getFeatureCollectionWrapper().getFeatureSchema()
                 .setCoordinateSystem(destination);
            DataSourceQuery dsq = layer.getDataSourceQuery();
            if (dsq!=null)
            	dsq.getDataSource().getProperties().put(DataSource.COORDINATE_SYSTEM_KEY,destination.getName());
        }

        context.getLayerManager().setCoordinateSystem(destination);
        
      
        if (context.getLayerViewPanel() != null) {
            context.getLayerViewPanel().getViewport().zoomToFullExtent();
        }

        return true;
    }
    public void initialize(PlugInContext context) throws Exception {

        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
	    
	  
	    featureInstaller.addMainMenuItem(this, new String[]{MenuNames.EDIT,"CoordSys"},
	            AppContext.getApplicationContext().getI18nString(this.getName()),false, null,
	            this.createEnableCheck(context.getWorkbenchContext()));

	    JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getIWorkbench().getGuiComponent().getLayerNamePopupMenu();
		featureInstaller.addPopupMenuItem(layerNamePopupMenu,this, AppContext.getApplicationContext().getI18nString(this.getName()) + "...", 
				false,null, this.createEnableCheck(context.getWorkbenchContext()));
    }
    
    public EnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
	    EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
	    return new MultiEnableCheck()
	    	.add(checkFactory.createNoSelectedSystemLayerCheck())
	        .add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
	        .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

}
