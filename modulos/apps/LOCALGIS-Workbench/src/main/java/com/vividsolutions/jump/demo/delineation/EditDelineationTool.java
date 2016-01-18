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

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Collection;

import javax.swing.Icon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;


/**
 *  Enables the user to move a vertex of an existing delineation. Thus, the user
 *  can take an unanchored vertex and anchor it to a point on a coastline.
 */
public class EditDelineationTool extends DragTool {
    private final static int TOLERANCE = 5;
    private Feature delineationBeingEdited;
    private String snappedAttributeName;
    private Coordinate stationaryVertex;
    private Coordinate vertexToMove;

    public EditDelineationTool(Collection snapPolicies) {
        getSnapManager().addPolicies(snapPolicies);
        setColor(DelineationUtil.TOOL_COLOR);
        setStrokeWidth(DelineationUtil.TOOL_STROKE_WIDTH);
    }

    public Icon getIcon() {
        return DelineationUtil.ICON;
    }

    public Cursor getCursor() {
        return DelineationUtil.CURSOR;
    }

    public void mousePressed(MouseEvent e) {
        try {
            delineationBeingEdited = findDelineationWithVertexAt(e.getPoint());

            if (delineationBeingEdited == null) {
                //If we're not over a delineation's vertex, proceed no further.
                //DragTool will not handle the drag because we are not calling
                //DragTool#mousePressed. [Jon Aquino]
                return;
            }

            super.mousePressed(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected Shape getShape(Point2D source, Point2D destination)
        throws NoninvertibleTransformException {
        return new Line2D.Double(getPanel().getViewport().toViewPoint(new Point2D.Double(
                    stationaryVertex.x, stationaryVertex.y)), destination);
    }

    private ILayer delineationLayer() {
        return getPanel().getLayerManager().getLayer(DelineationUtil.DELINEATION_LAYER_NAME);
    }

    private Feature findDelineationWithVertexAt(Point2D viewPoint)
        throws Exception {
        if ((delineationLayer() == null) || !delineationLayer().isVisible()) {
            return null;
        }

        Collection delineationsWithVertex = getPanel().featuresWithVertex(viewPoint,
                TOLERANCE,
                delineationLayer().getFeatureCollectionWrapper().query(getPanel()
                                                                    .getViewport()
                                                                    .getEnvelopeInModelCoordinates()));

        if (delineationsWithVertex.isEmpty()) {
            return null;
        }

        //Pick the first delineation we find. [Jon Aquino]
        Feature delineation = (Feature) delineationsWithVertex.iterator().next();
        Coordinate[] coordinates = delineation.getGeometry().getCoordinates();
        DelineationUtil.checkDelineationCoordinates(coordinates);

        Coordinate c = getPanel().getViewport().toModelCoordinate(viewPoint);

        if (coordinates[0].distance(c) < coordinates[1].distance(c)) {
            stationaryVertex = coordinates[1];
            vertexToMove = coordinates[0];
            snappedAttributeName = DelineationUtil.SOURCE_SNAPPED_ATTRIBUTE_NAME;
        } else {
            stationaryVertex = coordinates[0];
            vertexToMove = coordinates[1];
            snappedAttributeName = DelineationUtil.DESTINATION_SNAPPED_ATTRIBUTE_NAME;
        }

        return delineation;
    }

    private void moveVertex(Feature delineation, Coordinate finalLocation)
        throws Exception {
        vertexToMove.setCoordinate(finalLocation);
        delineation.getGeometry().geometryChanged();
        delineation.setAttribute(snappedAttributeName,
            DelineationUtil.toString(getSnapManager().wasSnapCoordinateFound()));
    }

    protected void gestureFinished() throws Exception {
        moveVertex(delineationBeingEdited, getModelDestination());
        delineationLayer().fireAppearanceChanged();
    }

}
