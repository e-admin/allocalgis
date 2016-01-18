/**
 * GeopistaUnionPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.analysis;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;

import com.geopista.app.AppContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class GeopistaUnionPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private final static String LAYER = aplicacion.getI18nString("GeopistaUnionPlugIn.Layer");
    private MultiInputDialog dialog;

    public GeopistaUnionPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {

      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
      EnableCheckFactory checkFactory = new EnableCheckFactory(context.getWorkbenchContext());
           featureInstaller.addMainMenuItem(this,
            new String[] { aplicacion.getI18nString("Tools"), aplicacion.getI18nString("Analysis") },
            aplicacion.getI18nString(this.getName()) + "...", false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(checkFactory.createAtLeastNLayersMustExistCheck(
                    1)));
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
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), aplicacion.getI18nString("GeopistaUnionPlugIn.Union"), true);

        //dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        dialog.setSideBarDescription(
            aplicacion.getI18nString("GeopistaUnionPlugIn.CrearCapaUnion"));
        String fieldName = LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, (Layer)context.getCandidateLayer(0), null, context.getLayerManager());
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
        FeatureCollection a = dialog.getLayer(LAYER).getFeatureCollectionWrapper();
        FeatureCollection union = union(monitor, a);
        context.addLayer(StandardCategoryNames.WORKING, aplicacion.getI18nString("GeopistaUnionPlugIn.Union"), union);
    }

    private FeatureCollection union(TaskMonitor monitor, FeatureCollection fc) {
        monitor.allowCancellationRequests();
        monitor.report(aplicacion.getI18nString("GeopistaUnionPlugIn.RealizandoUnion"));

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

            monitor.report(count++, size, aplicacion.getI18nString("GeopistaUnionPlugIn.features"));
        }

        unionGeometryList.add(currUnion);

        return FeatureDatasetFactory.createFromGeometry(unionGeometryList);
    }
}
