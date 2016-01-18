
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.qa.ValidationError;
import com.vividsolutions.jump.qa.Validator;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.renderer.style.RingVertexStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.VertexStyle;


public class ValidateSelectedLayersPlugIn extends AbstractPlugIn
    implements ThreadedPlugIn {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
    private final static String CHECK_BASIC_TOPOLOGY = aplicacion.getI18nString("plugin.validatelayer.comprobarTopologia");
    private final static String CHECK_POLYGON_ORIENTATION = aplicacion.getI18nString("plugin.validatelayer.comprobarPoligono");
    private final static String CHECK_LINESTRINGS_SIMPLE = aplicacion.getI18nString("plugin.validatelayer.comprobarLinea");
    private final static String CHECK_POLYGONS_HAVE_NO_HOLES = aplicacion.getI18nString("plugin.validatelayer.noHuecos");;
    private final static String CHECK_NO_REPEATED_CONSECUTIVE_POINTS = aplicacion.getI18nString("plugin.validatelayer.noRepetidos");
    private final static String CHECK_MIN_SEGMENT_LENGTH = aplicacion.getI18nString("plugin.validatelayer.comprobarSegmento");
    private final static String CHECK_MIN_ANGLE = aplicacion.getI18nString("plugin.validatelayer.comprobarAngulo");
    private final static String MIN_SEGMENT_LENGTH = aplicacion.getI18nString("plugin.validatelayer.minimoSegmento");   
    private final static String MIN_ANGLE = aplicacion.getI18nString("plugin.validatelayer.minimoAngulo");
    private final static String MIN_POLYGON_AREA = aplicacion.getI18nString("plugin.validatelayer.minimaSuperficie");   
    private final static String CHECK_MIN_POLYGON_AREA = aplicacion.getI18nString("plugin.validatelayer.comprobarArea");
    private final static String DISALLOW_POINTS = aplicacion.getI18nString("plugin.validatelayer.noPuntos");
    private final static String DISALLOW_LINESTRINGS = aplicacion.getI18nString("plugin.validatelayer.noLineas");
    private final static String DISALLOW_POLYGONS = aplicacion.getI18nString("plugin.validatelayer.noPoligonos");
    private final static String DISALLOW_MULTIPOINTS = aplicacion.getI18nString("plugin.validatelayer.noMultipuntos");
    private final static String DISALLOW_MULTILINESTRINGS = aplicacion.getI18nString("plugin.validatelayer.noMultilineas");
    private final static String DISALLOW_MULTIPOLYGONS = aplicacion.getI18nString("plugin.validatelayer.noMultipoligonos");
    private final static String DISALLOW_GEOMETRYCOLLECTIONS = aplicacion.getI18nString("plugin.validatelayer.noColecciones");
    private static final String ERROR = "ERROR";
    private static final String SOURCE_FID = "SOURCE_FID";
    private static final String GEOMETRY = "GEOMETRY";
    private MultiInputDialog dialog;
    private FeatureSchema schema;
    private GeometryFactory geometryFactory = new GeometryFactory();
    private Color GOLD = new Color(255, 192, 0, 150);
    private Validator validator;

    public ValidateSelectedLayersPlugIn() {
        initFeatureSchema();
    }

    public boolean execute(PlugInContext context) throws Exception {
        validator = prompt(context);

        return validator != null;
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
        //Call #getSelectedLayers before #clear, because #clear will surface
        //output window. [Jon Aquino]
        Layer[] selectedLayers = (Layer[])context.getSelectedLayers();
        context.getOutputFrame().createNewDocument();
        context.getOutputFrame().addHeader(1, aplicacion.getI18nString("plugin.validatelayer.errores"));

        for (int i = 0;
                (i < selectedLayers.length) && !monitor.isCancelRequested();
                i++) {
            validate(selectedLayers[i], validator, context, monitor);
        }
    }

    private void initFeatureSchema() {
        schema = new FeatureSchema();
        schema.addAttribute(ERROR, AttributeType.STRING);
        schema.addAttribute(SOURCE_FID, AttributeType.INTEGER);
        schema.addAttribute(GEOMETRY, AttributeType.GEOMETRY);
    }

    private Validator prompt(PlugInContext context) {
        if (dialog == null) {
            initDialog(context);
        }

        dialog.setVisible(true);

        if (!dialog.wasOKPressed()) {
            return null;
        }

        Validator validator = new Validator();
        validator.setCheckingBasicTopology(dialog.getBoolean(
                CHECK_BASIC_TOPOLOGY));
        validator.setCheckingNoRepeatedConsecutivePoints(dialog.getBoolean(
                CHECK_NO_REPEATED_CONSECUTIVE_POINTS));
        validator.setCheckingLineStringsSimple(dialog.getBoolean(
                CHECK_LINESTRINGS_SIMPLE));
        validator.setCheckingPolygonOrientation(dialog.getBoolean(
                CHECK_POLYGON_ORIENTATION));
        validator.setCheckingNoHoles(dialog.getBoolean(
                CHECK_POLYGONS_HAVE_NO_HOLES));
        validator.setCheckingMinSegmentLength(dialog.getBoolean(
                CHECK_MIN_SEGMENT_LENGTH));
        validator.setCheckingMinAngle(dialog.getBoolean(CHECK_MIN_ANGLE));
        validator.setCheckingMinPolygonArea(dialog.getBoolean(
                CHECK_MIN_POLYGON_AREA));
        validator.setMinSegmentLength(dialog.getDouble(MIN_SEGMENT_LENGTH));
        validator.setMinAngle(dialog.getDouble(MIN_ANGLE));
        validator.setMinPolygonArea(dialog.getDouble(MIN_POLYGON_AREA));

        ArrayList disallowedGeometryClasses = new ArrayList();

        if (dialog.getBoolean(DISALLOW_POINTS)) {
            disallowedGeometryClasses.add(Point.class);
        }

        if (dialog.getBoolean(DISALLOW_LINESTRINGS)) {
            disallowedGeometryClasses.add(LineString.class);
        }

        if (dialog.getBoolean(DISALLOW_POLYGONS)) {
            disallowedGeometryClasses.add(Polygon.class);
        }

        if (dialog.getBoolean(DISALLOW_MULTIPOINTS)) {
            disallowedGeometryClasses.add(MultiPoint.class);
        }

        if (dialog.getBoolean(DISALLOW_MULTILINESTRINGS)) {
            disallowedGeometryClasses.add(MultiLineString.class);
        }

        if (dialog.getBoolean(DISALLOW_MULTIPOLYGONS)) {
            disallowedGeometryClasses.add(MultiPolygon.class);
        }

        if (dialog.getBoolean(DISALLOW_GEOMETRYCOLLECTIONS)) {
            disallowedGeometryClasses.add(GeometryCollection.class);
        }

        validator.setDisallowedGeometryClasses(disallowedGeometryClasses);

        return validator;
    }

    private void validate(final Layer layer, final Validator validator,
        PlugInContext context, TaskMonitor monitor) {
        List validationErrors = validator.validate(layer.getFeatureCollectionWrapper()
                                                        .getFeatures(), monitor);

        if (!validationErrors.isEmpty()) {
            addLayer(toLayer("Error Locations - " + layer.getName(),
                    toLocationFeatures(validationErrors, layer), layer, true,
                    context), context);
            addLayer(toLayer("Bad Features - " + layer.getName(),
                    toFeatures(validationErrors, layer), layer, false, context),
                context);
        }

        outputSummary(context, layer, validationErrors);
    }

    private void outputSummary(PlugInContext context, Layer layer,
        List validationErrors) {
        context.getOutputFrame().addHeader(2, aplicacion.getI18nString("plugin.validatelayer.capa")+" " + layer.getName());

        if (validationErrors.isEmpty()) {
            context.getOutputFrame().addText(aplicacion.getI18nString("plugin.validatelayer.noerrores"));

            return;
        }

        CollectionMap descriptionToErrorMap = new CollectionMap();

        for (Iterator i = validationErrors.iterator(); i.hasNext();) {
            ValidationError error = (ValidationError) i.next();
            descriptionToErrorMap.addItem(error.getMessage(), error);
        }

        for (Iterator i = descriptionToErrorMap.keySet().iterator();
                i.hasNext();) {
            String message = (String) i.next();
            context.getOutputFrame().addField(message + ":",
                descriptionToErrorMap.getItems(message).size() + "");
        }
    }

    private List toFeatures(List validationErrors, Layer sourceLayer) {
        ArrayList features = new ArrayList();

        for (Iterator i = validationErrors.iterator(); i.hasNext();) {
            ValidationError error = (ValidationError) i.next();
            features.add(toFeature(error, sourceLayer,
                    (Geometry) error.getFeature().getGeometry().clone()));
        }

        return features;
    }

    private List toLocationFeatures(List validationErrors, Layer sourceLayer) {
        ArrayList features = new ArrayList();

        for (Iterator i = validationErrors.iterator(); i.hasNext();) {
            ValidationError error = (ValidationError) i.next();
            Geometry geometry = geometryFactory.createPoint(error.getLocation());
            features.add(toFeature(error, sourceLayer, geometry));
        }

        return features;
    }

    private Feature toFeature(ValidationError error, Layer sourceLayer,
        Geometry geometry) {
        Feature ringFeature = new BasicFeature(schema);
        ringFeature.setAttribute(SOURCE_FID,
            new Integer(error.getFeature().getID()));
        ringFeature.setAttribute(ERROR, error.getMessage());
        ringFeature.setGeometry(geometry);

        return ringFeature;
    }

    private void addLayer(Layer errorLayer, PlugInContext context) {
        context.getLayerManager().addLayer(StandardCategoryNames.QA, errorLayer);
    }

    private Layer toLayer(String name, List features, Layer sourceLayer,
        boolean ringVertices, PlugInContext context) {
        boolean firingEvents = context.getLayerManager().isFiringEvents();
        context.getLayerManager().setFiringEvents(false);

        try {
            FeatureDataset errorFeatureCollection = new FeatureDataset(features,
                    schema);
            Layer errorLayer = new Layer(name, GOLD, errorFeatureCollection,
                    context.getLayerManager());

            if (ringVertices) {
            	errorLayer.getBasicStyle().setEnabled(false);
                changeVertexToRing(errorLayer);
            }

            showVertices(errorLayer);

            return errorLayer;
        } finally {
            context.getLayerManager().setFiringEvents(firingEvents);
        }
    }

    private void changeVertexToRing(Layer errorLayer) {
        boolean firingEvents = errorLayer.getLayerManager().isFiringEvents();
        errorLayer.getLayerManager().setFiringEvents(false);

        try {
            //Many parties assume that a layer always has a VertexStyle. Therefore,
            //disable events while we make the switch. [Jon Aquino]
            errorLayer.removeStyle(errorLayer.getStyle(VertexStyle.class));
            errorLayer.addStyle(new RingVertexStyle());
            errorLayer.getBasicStyle().setLineWidth(5);
        } finally {
            errorLayer.getLayerManager().setFiringEvents(firingEvents);
        }

        errorLayer.fireAppearanceChanged();
    }

    private void showVertices(Layer errorLayer) {
        errorLayer.getVertexStyle().setEnabled(true);
        errorLayer.fireAppearanceChanged();
    }

    private void initDialog(PlugInContext context) {
        dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()),
                aplicacion.getI18nString("plugin.validatelayer.capasSeleccionadas"), true);
        dialog.setSideBarImage(IconLoader.icon("Validate.gif"));
        dialog.setSideBarDescription(aplicacion.getI18nString("plugin.validatelayer.descripcion"));
        dialog.addLabel("<HTML><B>"+aplicacion.getI18nString("plugin.validatelayer.validacionMetrica")+"</B></HTML>");
        dialog.addSeparator();
        dialog.addCheckBox(CHECK_BASIC_TOPOLOGY, true, "Test");
        dialog.addCheckBox(CHECK_NO_REPEATED_CONSECUTIVE_POINTS,
            false);
        dialog.addCheckBox(CHECK_POLYGON_ORIENTATION,
            false, aplicacion.getI18nString("plugin.validatelayer.orientarPoligonos"));
        dialog.addCheckBox(CHECK_MIN_SEGMENT_LENGTH, false);
        dialog.addPositiveDoubleField(MIN_SEGMENT_LENGTH, 0.001,
            5);
        dialog.addCheckBox(CHECK_MIN_ANGLE, false);
        dialog.addPositiveDoubleField(MIN_ANGLE, 1, 5);
        dialog.addCheckBox(CHECK_MIN_POLYGON_AREA, false);
        dialog.addPositiveDoubleField(MIN_POLYGON_AREA, 0.001,
            5);
        dialog.addCheckBox(CHECK_LINESTRINGS_SIMPLE, false,
                aplicacion.getI18nString("plugin.validatelayer.lineasSimples"));
        dialog.startNewColumn();
        dialog.addLabel("<HTML><B>"+aplicacion.getI18nString("plugin.validatelayer.validacionTipos")+"</B></HTML>");
        dialog.addSeparator();
        dialog.addCheckBox(DISALLOW_POINTS, false);
        dialog.addCheckBox(DISALLOW_LINESTRINGS, false);
        dialog.addCheckBox(DISALLOW_POLYGONS, false);
        dialog.addCheckBox(DISALLOW_MULTIPOINTS, false);
        dialog.addCheckBox(DISALLOW_MULTILINESTRINGS,
            false);
        dialog.addCheckBox(DISALLOW_MULTIPOLYGONS, false);
        dialog.addCheckBox(CHECK_POLYGONS_HAVE_NO_HOLES,
            false);
        dialog.addCheckBox(DISALLOW_GEOMETRYCOLLECTIONS,
            false, aplicacion.getI18nString("plugin.validatelayer.noSubtipos"));
        GUIUtil.centreOnWindow(dialog);
    }
}
