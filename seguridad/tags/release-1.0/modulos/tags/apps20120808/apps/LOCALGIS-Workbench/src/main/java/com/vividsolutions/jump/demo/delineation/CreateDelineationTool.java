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

package com.vividsolutions.jump.demo.delineation;

import java.awt.*;
import java.awt.geom.*;
import java.util.Collection;

import javax.swing.Icon;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.ui.cursortool.NClickTool;


/**
 *  Creates a new delineation feature.
 */
public class CreateDelineationTool extends NClickTool {
    private final static String GEOMETRY_ATTRIBUTE_NAME = "GEOMETRY";
    private boolean sourceAnchored;
    private boolean destinationAnchored;
    private GeometryFactory factory = new GeometryFactory();

    public CreateDelineationTool(Collection snapPolicies) {
        super(2);
        getSnapManager().addPolicies(snapPolicies);
        setColor(DelineationUtil.TOOL_COLOR);
        setStrokeWidth(DelineationUtil.TOOL_STROKE_WIDTH);
    }

    public Icon getIcon() {
        return DelineationUtil.ICON;
    }

    protected void gestureFinished() throws NoninvertibleTransformException {
        //Don't want viewport to change at this stage. [Jon Aquino]
        getPanel().setViewportInitialized(true);

        Feature feature = createFeature(getModelSource(), getModelDestination());
        delineationLayer().getFeatureCollectionWrapper().add(feature);
    }

    public Cursor getCursor() {
        return DelineationUtil.CURSOR;
    }

    protected Shape getShape(Point2D source, Point2D destination) {
        return new Line2D.Double(source, destination);
    }

    protected void add(Coordinate c) {
        super.add(c);

        if (getCoordinates().size() == 1) {
            sourceAnchored = getSnapManager().wasSnapCoordinateFound();
        }

        if (getCoordinates().size() == 2) {
            destinationAnchored = getSnapManager().wasSnapCoordinateFound();
        }
    }

    private Feature createFeature(Coordinate source, Coordinate destination)
        throws NoninvertibleTransformException {
        Geometry geometry = factory.createLineString(new Coordinate[] {
                    source, destination
                });
        Feature feature = new BasicFeature(delineationLayer().getFeatureCollectionWrapper()
                                          .getFeatureSchema());
        feature.setAttribute(DelineationUtil.SOURCE_SNAPPED_ATTRIBUTE_NAME,
            DelineationUtil.toString(sourceAnchored));
        feature.setAttribute(DelineationUtil.DESTINATION_SNAPPED_ATTRIBUTE_NAME,
            DelineationUtil.toString(destinationAnchored));
        feature.setGeometry(geometry);

        return feature;
    }

    /**
     *  Returns the delineation layer, creating it if necessary, with the
     *  appropriate styles.
     */
    private ILayer delineationLayer() {
        ILayer delineationLayer = getPanel().getLayerManager().getLayer(DelineationUtil.DELINEATION_LAYER_NAME);

        if (delineationLayer != null) {
            return delineationLayer;
        }

        FeatureSchema schema = new FeatureSchema();
        schema.addAttribute(DelineationUtil.SOURCE_SNAPPED_ATTRIBUTE_NAME,
            AttributeType.STRING);
        schema.addAttribute(DelineationUtil.DESTINATION_SNAPPED_ATTRIBUTE_NAME,
            AttributeType.STRING);
        schema.addAttribute(GEOMETRY_ATTRIBUTE_NAME, AttributeType.GEOMETRY);

        FeatureCollection featureCollection = new FeatureDataset(schema);
        Layer layer;
        boolean firingEvents = getPanel().getLayerManager().isFiringEvents();
        getPanel().getLayerManager().setFiringEvents(false);

        try {
            layer = new Layer(DelineationUtil.DELINEATION_LAYER_NAME,
                    Color.blue, featureCollection, getPanel().getLayerManager());

            //Replace the default vertex style with our special red-green vertices. [Jon Aquino]
            layer.removeStyle(layer.getVertexStyle());
            layer.addStyle(new DelineationVertexStyle());
            layer.getBasicStyle().setLineWidth(4);
            layer.getVertexStyle().setEnabled(true);
        } finally {
            getPanel().getLayerManager().setFiringEvents(firingEvents);
        }

        getPanel().getLayerManager().addLayer(StandardCategoryNames.WORKING,
            layer);

        return layer;
    }
}
