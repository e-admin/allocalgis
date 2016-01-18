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

package com.vividsolutions.jump.workbench.ui.plugin.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;


public class UnionPlugIn extends AbstractPlugIn implements ThreadedPlugIn {
    private final static String LAYER = "Layer";
    private MultiInputDialog dialog;

    public UnionPlugIn() {
    }

    /*
      public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(
            this, "Tools", "Find Unaligned Segments...", null, new MultiEnableCheck()
          .add(context.getCheckFactory().createWindowWithLayerNamePanelMustBeActiveCheck())
            .add(context.getCheckFactory().createAtLeastNLayersMustExistCheck(1)));
      }
    */
    public boolean execute(PlugInContext context) throws Exception {
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Jon Aquino]
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return false;
        }

        return true;
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), "Union", true);

        //dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        dialog.setSideBarDescription(
            "Creates a new layer containing the union of all the features in the input layer");
        String fieldName = LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, (Layer)context.getCandidateLayer(0), null, context.getLayerManager());
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
        FeatureCollection a = dialog.getLayer(LAYER).getFeatureCollectionWrapper();
        FeatureCollection union = union(monitor, a);
        context.addLayer(StandardCategoryNames.WORKING, "Union", union);
    }

    private FeatureCollection union(TaskMonitor monitor, FeatureCollection fc) {
        monitor.allowCancellationRequests();
        monitor.report("Computing Union...");

        List unionGeometryList = new ArrayList();

        Geometry currUnion = null;
        int size = fc.size();
        int count = 1;

        for (Iterator i = fc.iterator(); i.hasNext();) {
            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();

            if (currUnion == null) {
                currUnion = geom;
            } else {
                currUnion = currUnion.union(geom);
            }

            monitor.report(count++, size, "features");
        }

        unionGeometryList.add(currUnion);

        return FeatureDatasetFactory.createFromGeometry(unionGeometryList);
    }
}
