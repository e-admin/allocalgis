/**
 * ChangeLayerCoordinateSystem.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 10-oct-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.tools;


import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/** 
 * Implements a {@link com.vividsolutions.jump.workbench.plugin.PlugIn
 * PlugIn} that allows the user to change coordinate systems.
 *
 */
public class ChangeLayerCoordinateSystem extends ThreadedBasePlugIn {

	private Layer selLayer = null;
	private CoordinateSystem destination = null;


    public boolean execute(PlugInContext context) throws Exception {
//		//Don't make this plug-in undoable -- it's a lot of data to store in memory [Jon Aquino]
		//context.getLayerManager().getUndoableEditReceiver().reportIrreversibleChange();
		
		selLayer = context.getSelectedLayer(0);
		if (selLayer==null)
			return true;
		destination = (CoordinateSystem) JOptionPane
		.showInputDialog((Component)context.getWorkbenchGuiComponent(),
				AppContext.getApplicationContext().getI18nString("SelectCoordinateReferenceSystem")+" "+ 
				selLayer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem()
				+ " "+AppContext.getApplicationContext().getI18nString("Coordinate.to")+":",
				AppContext.getApplicationContext().getI18nString("CoordinateReferenceSystem"),
				JOptionPane.PLAIN_MESSAGE, null, new ArrayList(
						CoordinateSystemRegistry
						.instance(context.getWorkbenchContext()
								.getBlackboard())
								.getCoordinateSystems()).toArray(),
								selLayer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem());

		if (destination == null) {
			return false;
		}

		if (selLayer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem() == destination) {
			return true;
		}

		return true;
	}
	public void initialize(PlugInContext context) throws Exception {

		FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());


		featureInstaller.addMainMenuItem(this, new String[]{MenuNames.EDIT,"CoordSys"},
				AppContext.getApplicationContext().getI18nString(this.getName()), false,null,
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
	    .add(checkFactory.checkGeopistaLayerCheck())
		.add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
	}


	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		if (Reprojector.instance().wouldChangeValues(selLayer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem(),
				destination)) {
			//Two-phase commit [Jon Aquino]
			ArrayList transactions = new ArrayList();

			{
				Layer layer = (Layer) selLayer;
				EditTransaction transaction = new EditTransaction(layer.getFeatureCollectionWrapper()
						.getFeatures(),
						getName(), layer, isRollingBackInvalidEdits(context),
						false, context.getLayerViewPanel());

				for (int j = 0; j < transaction.size(); j++) {
					monitor.report("Cambiando coordenadas de '" + layer.getName() + "'  (" + j + " de " + transaction.size() + ").");
					Reprojector.instance().reproject(transaction.getGeometry(j),
							selLayer.getFeatureCollectionWrapper().getFeatureSchema().getCoordinateSystem(),
							destination);
				}

				transactions.add(transaction);
			}

			EditTransaction.commit(transactions);
			selLayer.getFeatureCollectionWrapper().getFeatureSchema()
			.setCoordinateSystem(destination);
			DataSourceQuery dsq = selLayer.getDataSourceQuery();
			if (dsq!=null)
				dsq.getDataSource().getProperties().put(DataSource.COORDINATE_SYSTEM_KEY,destination.getName());

			if (context.getLayerViewPanel() != null) {
				context.getLayerViewPanel().getViewport().zoomToFullExtent();
			}
		}
	}

}
