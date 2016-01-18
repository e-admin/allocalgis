
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

package com.vividsolutions.jump.workbench.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.JInternalFrame;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.driver.DriverManager;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.LayerNamePanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.editor.TaskComponent;

/**
 * Passed to PlugIns to enable them to access the rest of the JUMP Workbench.
 * @see PlugIn
 */
public class PlugInContext implements LayerManagerProxy {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(PlugInContext.class);

    private ITask task;
    private LayerNamePanel layerNamePanel;
    private LayerViewPanel layerViewPanel;
    private WorkbenchContext workbenchContext;
    private EnableCheckFactory checkFactory;
    private FeatureInstaller featureInstaller;
    private LayerManagerProxy layerManagerProxy;

    public PlugInContext(
        WorkbenchContext workbenchContext,
        ITask task,
        LayerManagerProxy layerManagerProxy,
        LayerNamePanel layerNamePanel,
        LayerViewPanel layerViewPanel) {
        this.workbenchContext = workbenchContext;
        this.task = task;
        this.layerManagerProxy = layerManagerProxy;
        this.layerNamePanel = layerNamePanel;
        this.layerViewPanel = layerViewPanel;
        checkFactory = new EnableCheckFactory(workbenchContext);
        featureInstaller = new FeatureInstaller(workbenchContext);
    }

    public DriverManager getDriverManager() {
        return workbenchContext.getDriverManager();
    }

    public ErrorHandler getErrorHandler() {
        return workbenchContext.getErrorHandler();
    }

    public WorkbenchContext getWorkbenchContext() {
        return workbenchContext;
    }

    /**
     *@return the ith layer clicked on the layer-list panel,
     * or null if the user hasn't clicked an ith layer
     */
    public Layer getSelectedLayer(int i) {
        Layer[] selectedLayers = getSelectedLayers();
 
        
        if (selectedLayers.length > i) {
            return selectedLayers[i];
        }

        return null;
    }

    /**
     * @return the ith selected layer, or if there is none, the ith layer
     */
    public ILayer getCandidateLayer(int i) {
        Layer lyr = (Layer)getSelectedLayer(i);

        if (lyr != null) {
            return lyr;
        }

        return getLayerManager().getLayer(i);
    }

    //<<TODO:DESIGN>> Return List instead of array [Jon Aquino]
    public Layer[] getSelectedLayers() {
        return getLayerNamePanel()==null?null:getLayerNamePanel().getSelectedLayers();
    }
public Layerable[] getSelectedLayerables()
{

	return getLayerNamePanel().getSelectedLayerables();
}
    public Envelope getSelectedLayerEnvelope() {
        return ((Layer)getSelectedLayer(0)).getFeatureCollectionWrapper().getEnvelope();
    }

    public ITask getTask() {
        return task;
    }

    public LayerNamePanel getLayerNamePanel() {
        return layerNamePanel;
    }

    public LayerManager getLayerManager() {
        return (LayerManager)layerManagerProxy.getLayerManager();
    }

    public LayerViewPanel getLayerViewPanel() {
        return layerViewPanel;
    }
    public WorkbenchFrame getWorkbenchFrame() {
    try
		{
		if (workbenchContext.getIWorkbench().getGuiComponent() instanceof WorkbenchFrame) return (WorkbenchFrame) workbenchContext
				.getIWorkbench().getGuiComponent();
		else return (WorkbenchFrame) AppContext.getApplicationContext()
				.getMainFrame();
		}
	catch (Exception e)
		{
		if (logger.isDebugEnabled())
			{
			logger
					.debug("getWorkbenchFrame() - No existe un WorkbenchFrame en el contexto actual.");
			}

		return null;
		}
    }
    public WorkbenchGuiComponent getWorkbenchGuiComponent() {
        return workbenchContext.getIWorkbench().getGuiComponent();
    }

    public Layer addLayer(
        String categoryName,
        String layerName,
        FeatureCollection featureCollection) {
        return getLayerManager().addLayer(categoryName, layerName, featureCollection);
    }

    public HTMLFrame getOutputFrame() {
        return workbenchContext.getIWorkbench().getGuiComponent().getOutputFrame();
    }

    public JInternalFrame getActiveInternalFrame() {
        return workbenchContext.getIWorkbench().getGuiComponent().getActiveInternalFrame();
    }
    public TaskComponent getActiveTaskComponent() {
    TaskComponent tcomp= workbenchContext.getIWorkbench().getGuiComponent().getActiveTaskComponent();
    return tcomp;
}
    public EnableCheckFactory getCheckFactory() {
        return checkFactory;
    }

    public FeatureInstaller getFeatureInstaller() {
        return featureInstaller;
    }

}
