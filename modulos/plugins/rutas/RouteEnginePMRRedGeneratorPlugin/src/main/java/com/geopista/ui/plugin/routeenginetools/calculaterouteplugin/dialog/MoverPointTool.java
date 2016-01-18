/**
 * MoverPointTool.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.CalcularRutaDragClickPlugIn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.util.CoordinateArrays;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.ui.EditTransaction;
import com.vividsolutions.jump.workbench.ui.cursortool.Animations;
import com.vividsolutions.jump.workbench.ui.cursortool.DragTool;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

public class MoverPointTool extends DragTool {
    public final static int TOLERANCE = 5;

    private EnableCheckFactory checkFactory;
    Coordinate newVertex = null;
    Coordinate endVertex = null;
    SegmentContext selectedSegment = null;
    Layer selectedLayer = null;

    public MoverPointTool(EnableCheckFactory checkFactory) {
        this.checkFactory = checkFactory;
        setColor(new Color(194, 179, 205));
        setStrokeWidth(5);
        allowSnapping();
    }
    public SegmentContext getSelectedSegment(){
    	return selectedSegment;
    }
    public Cursor getCursor() {
        return createCursor(IconLoader.icon("MoveVertexCursor3.gif").getImage());
    }

    public Icon getIcon() {
        return IconLoader.icon("MoveVertex.gif");
    }

    protected void gestureFinished() throws Exception {
        reportNothingToUndoYet();

        SegmentContext segment = selectedSegment;
        if (segment == null) {
            getPanel().getContext().warnUser("No selected line segments here");
            coordinates.clear();
            return;
        }
        
        EditTransaction transaction =
            new EditTransaction(
                Arrays.asList(new Feature[] { segment.getFeature()}),
                getName(),
                segment.getLayer(),
                isRollingBackInvalidEdits(),
                false,
                getPanel());
//        transaction.setGeometry(
//                0,
//                new GeometryEditor().insertVertex(
//                    transaction.getGeometry(0),
//                    segment.getSegment().p0,
//                    segment.getSegment().p1,
//                    newVertex));
        

        try {
        	Animations.drawExpandingRing(
        			getPanel().getViewport().toViewPoint(modelClickLastCoordinate()),
        			false,
        			Color.green,
        			getPanel(),
        			null);
        } catch (Throwable t) {
        	getPanel().getContext().warnUser(t.toString());
        }
            
//        //#execute(UndoableCommand) will be called. [Jon Aquino]
//        execute(null);
//        moveVertices(getModelSource(), getModelDestination());
        
        execute(
        		CalcularRutaDragClickPlugIn.createAddCommand(
        				new GeometryFactory().createPoint(modelClickLastCoordinate()), this
        		));
        
        
        coordinates.clear();
    }

    public void mousePressed(final MouseEvent e) {
        try {
            if (!check(checkFactory.createAtLeastNLayersMustBeEditableCheck(1))) {
                return;
            }
            if (!check(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1))) {
                return;
            }
            if (!check(checkFactory.createAtLeastNItemsMustBeSelectedCheck(1))) {
                return;
            }
            if (!check(checkFactory.createAtLeastNLayersMustBeEditableCheck(1))) {
                return;
            }
            
            add(snap(e.getPoint()));
            HashMap layerToFeaturesInRangeMap = layerToFeaturesInRangeMap();
            if (layerToFeaturesInRangeMap.isEmpty()) {
                getPanel().getContext().warnUser("No selected editable items here");
                coordinates.clear();
                return;
            }
            SegmentContext segment =
                findSegment(layerToFeaturesInRangeMap, modelClickCoordinate());
            if (segment == null) {
                getPanel().getContext().warnUser("No selected line segments here");
                coordinates.clear();
                return;
            }
            
            newVertex = newVertex(segment.getSegment(), modelClickCoordinate());
            selectedSegment = segment;
          
            Animations.drawExpandingRing(
            		getPanel().getViewport().toViewPoint(newVertex),
            		true,
            		Color.green,
            		getPanel(),
            		null);

            super.mousePressed(e);
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }
    
    private boolean nearSelectionHandle(Point2D p) throws NoninvertibleTransformException {
        final Envelope buffer = vertexBuffer(getPanel().getViewport().toModelCoordinate(p));
        final boolean[] result = new boolean[] { false };
        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
            i.hasNext();
            ) {
            Layer layer = (Layer) i.next();
            if (!layer.isEditable()) {
                continue;
            }
            for (Iterator j = getPanel().getSelectionManager().getSelectedItems(layer).iterator();
                j.hasNext();
                ) {
                Geometry item = (Geometry) j.next();
                item.apply(new CoordinateFilter() {
                    public void filter(Coordinate coord) {
                        if (buffer.contains(coord)) {
                            result[0] = true;
                        }
                    }
                });
            }
        }
        return result[0];
    }

    private Envelope vertexBuffer(Coordinate c) throws NoninvertibleTransformException {
        double tolerance = TOLERANCE / getPanel().getViewport().getScale();
        return vertexBuffer(c, tolerance);
    }

    public void moveVertices(Coordinate initialLocation, Coordinate finalLocation)
        throws Exception {
//        final Envelope oldVertexBuffer = vertexBuffer(initialLocation);
//        final Coordinate newVertex = finalLocation;
//        ArrayList transactions = new ArrayList();
//        for (Iterator i = getPanel().getSelectionManager().getLayersWithSelectedItems().iterator();
//            i.hasNext();
//            ) {
//            Layer layerWithSelectedItems = (Layer) i.next();
//            if (!layerWithSelectedItems.isEditable()) {
//                continue;
//            }
//            transactions.add(createTransaction(layerWithSelectedItems, oldVertexBuffer, newVertex));
//        }
//        EditTransaction.commit(transactions);
    }

    private EditTransaction createTransaction(
        Layer layer,
        final Envelope oldVertexBuffer,
        final Coordinate newVertex) {
        return EditTransaction.createTransactionOnSelection(new EditTransaction.SelectionEditor() {
            public Geometry edit(Geometry geometryWithSelectedItems, Collection selectedItems) {
//                for (Iterator j = selectedItems.iterator(); j.hasNext();) {
//                    Geometry item = (Geometry) j.next();
//                    edit(item);
//                }
                return geometryWithSelectedItems;
            }
            private void edit(Geometry selectedItem) {
//                selectedItem.apply(new CoordinateFilter() {
//                    public void filter(Coordinate coordinate) {
//                        if (oldVertexBuffer.contains(coordinate)) {
//                            coordinate.x = newVertex.x;
//                            coordinate.y = newVertex.y;
//                        }
//                    }
//                });
            }
        }, getPanel(), getPanel().getContext(), getName(), layer, isRollingBackInvalidEdits(), false);
    }

    protected Shape getShape(Point2D source, Point2D destination) throws Exception {
        double radius = 20;

        return new Ellipse2D.Double(
            destination.getX() - (radius / 2),
            destination.getY() - (radius / 2),
            radius,
            radius);
    }

    private Envelope vertexBuffer(Coordinate vertex, double tolerance) {
        return new Envelope(
            vertex.x - tolerance,
            vertex.x + tolerance,
            vertex.y - tolerance,
            vertex.y + tolerance);
    }
    private HashMap layerToFeaturesInRangeMap() throws NoninvertibleTransformException {
        HashMap layerToFeaturesInRangeMap = new HashMap();
        for (Iterator i = getPanel().getLayerManager().getEditableLayers().iterator();
            i.hasNext();
            ) {
            Layer editableLayer = (Layer) i.next();
            Collection featuresInRange = featuresInRange(modelClickCoordinate(), editableLayer);
            if (!featuresInRange.isEmpty()) {
                layerToFeaturesInRangeMap.put(editableLayer, featuresInRange);
            }
        }
        return layerToFeaturesInRangeMap;
    }
    private Coordinate modelClickCoordinate() throws NoninvertibleTransformException {
        return (Coordinate) getCoordinates().get(0);
    }
    public void mouseReleased(MouseEvent e) {
    	try {
			add(snap(e.getPoint()));
		} catch (NoninvertibleTransformException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		super.mouseReleased(e);
    }
    private Coordinate modelClickLastCoordinate() throws NoninvertibleTransformException {
    	
        return (Coordinate) getCoordinates().get(getCoordinates().size()-1);
    }
    private Collection featuresInRange(Coordinate modelClickCoordinate, Layer layer) {
        Point modelClickPoint = new GeometryFactory().createPoint(modelClickCoordinate);

        Collection featuresWithSelectedItems =
            getPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
        if (featuresWithSelectedItems.isEmpty()) {
            return new ArrayList();
        }
        ArrayList featuresInRange = new ArrayList();
        for (Iterator i = featuresWithSelectedItems.iterator(); i.hasNext();) {
            Feature candidate = (Feature) i.next();
            if (modelClickPoint.distance(candidate.getGeometry()) <= modelRange()) {
                featuresInRange.add(candidate);
            }
        }
        return featuresInRange;
    }
    private double modelRange() {
        return PIXEL_RANGE / getPanel().getViewport().getScale();
    }
    private static final int PIXEL_RANGE = 10;
    public List getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }
    private List coordinates = new ArrayList();
    protected void add(Coordinate c) {
        coordinates.add(c);
    }

    public static class SegmentContext {
        public SegmentContext(Layer layer, Feature feature, LineSegment segment) {
            this.layer = layer;
            this.feature = feature;
            this.segment = segment;
        }
        private LineSegment segment;
        private Feature feature;
        private Layer layer;
        public Feature getFeature() {
            return feature;
        }

        public Layer getLayer() {
            return layer;
        }

        public LineSegment getSegment() {
            return segment;
        }

    }
    
    private SegmentContext findSegment(Map layerToFeaturesMap, Coordinate target) {
        for (Iterator i = layerToFeaturesMap.keySet().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            Collection features = (Collection) layerToFeaturesMap.get(layer);
            SegmentContext segmentContext = findSegment(layer, features, target);
            if (segmentContext != null) {
                return segmentContext;
            }
        }
        return null;
    }
    private SegmentContext findSegment(Layer layer, Collection features, Coordinate target) {
        Assert.isTrue(layer.isEditable());
        for (Iterator i = features.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();
            for (Iterator j =
                getPanel().getSelectionManager().getSelectedItems(layer, feature).iterator();
                j.hasNext();
                ) {
                Geometry selectedItem = (Geometry) j.next();
                LineSegment segment = segmentInRange(selectedItem, target);
                if (segment != null) {
                    return new SegmentContext(layer, feature, segment);
                }
            }
        }
        return null;
    }
    private LineSegment segmentInRange(Geometry geometry, Coordinate target) {

        LineSegment closest = null;
        List coordArrays = CoordinateArrays.toCoordinateArrays(geometry, false);
        for (Iterator i = coordArrays.iterator(); i.hasNext();) {
            Coordinate[] coordinates = (Coordinate[]) i.next();
            for (int j = 1; j < coordinates.length; j++) { //1
                LineSegment candidate = new LineSegment(coordinates[j - 1], coordinates[j]);
                if (candidate.distance(target) > modelRange()) {
                    continue;
                }
                if ((closest == null) || (candidate.distance(target) < closest.distance(target))) {
                    closest = candidate;
                }
            }
        }
        return closest;
    }
    private Coordinate newVertex(LineSegment segment, Coordinate target) {
        Coordinate closestPoint = segment.closestPoint(target);
        if (!closestPoint.equals(segment.p0) && !closestPoint.equals(segment.p1)) {
            return closestPoint;
        }

        double threshold = 6 / getPanel().getViewport().getScale();
        if (segment.getLength() < threshold) {
            return CoordUtil.average(segment.p0, segment.p1);
        }

        double offset = 3 / getPanel().getViewport().getScale();
        Coordinate unitVector =
            closestPoint.equals(segment.p0)
                ? CoordUtil.divide(CoordUtil.subtract(segment.p1, segment.p0), segment.getLength())
                : CoordUtil.divide(CoordUtil.subtract(segment.p0, segment.p1), segment.getLength());
        return CoordUtil.add(closestPoint, CoordUtil.multiply(offset, unitVector));
    }
    
    public void cancelGesture() {

        super.cancelGesture();
        coordinates.clear();
    }
    
}
