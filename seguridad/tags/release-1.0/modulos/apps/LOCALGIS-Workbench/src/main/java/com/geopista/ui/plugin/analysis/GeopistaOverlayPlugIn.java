package com.geopista.ui.plugin.analysis;


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

import javax.swing.JComboBox;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.tools.AttributeMapping;
import com.vividsolutions.jump.tools.OverlayEngine;
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

/**
 *
 * Creates a new layer containing intersections of all pairs of
 * features from two input layers.  Splits {@link
 * com.vividsolutions.jts.geom.MultiPolygon Multipolygons} and {@link
 * com.vividsolutions.jts.geom.GeometryCollection
 * GeometryCollections}, and filters out non-Polygons.
 */

public class GeopistaOverlayPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

    private final static String POLYGON_OUTPUT =aplicacion.getI18nString("GeopistaOverlayPlugIn.LimitarSalidaPoligonos");
    private final static String FIRST_LAYER = aplicacion.getI18nString("GeopistaOverlayPlugIn.PrimeraCapa");
    private final static String SECOND_LAYER = aplicacion.getI18nString("GeopistaOverlayPlugIn.SegundaCapa");
    private final static String TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER = aplicacion.getI18nString("GeopistaOverlayPlugIn.TransferirAtributosPrimeraCapa");
    private final static String TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER = aplicacion.getI18nString("GeopistaOverlayPlugIn.TransferirAtributosSegundaCapa");
    private MultiInputDialog dialog;
    private OverlayEngine overlayEngine;
    
    public GeopistaOverlayPlugIn() {
    }

    public boolean execute(PlugInContext context) throws Exception {
        overlayEngine = prompt(context);

        return overlayEngine != null;
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
                    2)));
    }
 

    private OverlayEngine prompt(PlugInContext context) {
        //Unlike ValidatePlugIn, here we always call #initDialog because we want
        //to update the layer comboboxes. [Jon Aquino]
        initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return null;
        }

        OverlayEngine e = new OverlayEngine();
        e.setAllowingPolygonsOnly(dialog.getBoolean(POLYGON_OUTPUT));
        e.setSplittingGeometryCollections(dialog.getBoolean(POLYGON_OUTPUT));

        return e;
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                getName(), true);
        dialog.setSideBarImage(IconLoader.icon("Overlay.gif"));
        dialog.setSideBarDescription(aplicacion.getI18nString(
            "GeopistaOverlayPlugIn.mensajeInterseccion"));
        String fieldName = FIRST_LAYER;
        JComboBox addLayerComboBox = dialog.addLayerComboBox(fieldName, (Layer)context.getCandidateLayer(0), null, context.getLayerManager());
        String fieldName1 = SECOND_LAYER;
        JComboBox addLayerComboBox1 = dialog.addLayerComboBox(fieldName1, (Layer)context.getCandidateLayer(1), null, context.getLayerManager());
        dialog.addCheckBox(POLYGON_OUTPUT, true, aplicacion.getI18nString("GeopistaOverlayPlugIn.DescomponerMultipoligonos"));
        dialog.addCheckBox(TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER,
            true);
        dialog.addCheckBox(TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER,
            true);
        GUIUtil.centreOnWindow(dialog);
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
        FeatureCollection a = dialog.getLayer(FIRST_LAYER).getFeatureCollectionWrapper();
        FeatureCollection b = dialog.getLayer(SECOND_LAYER)
                                    .getFeatureCollectionWrapper();
        FeatureCollection overlay = overlayEngine.overlay(a, b, mapping(a, b),
                monitor);
        context.addLayer(StandardCategoryNames.WORKING, aplicacion.getI18nString("GeopistaOverlayPlugIn.Interseccion"), overlay);
    }

    private AttributeMapping mapping(FeatureCollection a, FeatureCollection b) {
        return new AttributeMapping(dialog.getBoolean(
                TRANSFER_ATTRIBUTES_FROM_FIRST_LAYER) ? a.getFeatureSchema()
                                                      : new FeatureSchema(),
            dialog.getBoolean(TRANSFER_ATTRIBUTES_FROM_SECOND_LAYER)
            ? b.getFeatureSchema() : new FeatureSchema());
    }
}
