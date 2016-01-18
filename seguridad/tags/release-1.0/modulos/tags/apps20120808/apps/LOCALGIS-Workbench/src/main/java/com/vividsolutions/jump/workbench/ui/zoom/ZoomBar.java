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
package com.vividsolutions.jump.workbench.ui.zoom;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicSliderUI;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.geom.LineSegmentEnvelopeIntersector;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.CoordinateArrays;
import com.vividsolutions.jump.util.MathUtil;
import com.vividsolutions.jump.workbench.model.*;
import com.vividsolutions.jump.workbench.ui.*;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.IncrementChooser;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.MetricSystem;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.RoundQuantity;
import com.vividsolutions.jump.workbench.ui.plugin.scalebar.ScaleBarRenderer;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.DynamicLayer;

public class ZoomBar extends JPanel {
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private int totalGeometries() {
        int totalGeometries = 0;
        for (Iterator i = layerViewPanel().getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getFeatureCollectionWrapper() != null)
            	totalGeometries += layer.getFeatureCollectionWrapper().size();
        }
        return totalGeometries;
    }

    private Envelope lastGoodEnvelope = null;
    private WorkbenchGuiComponent frame;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JSlider slider = new JSlider();
    private JLabel label = new JLabel();
    private JPanel labels=new JPanel();
    private JLabel scalelabel=new JLabel();
    
    private IncrementChooser incrementChooser = new IncrementChooser();
    private Collection metricUnits = new MetricSystem(1).createUnits();
    public ZoomBar(
	    boolean showingSliderLabels,
	    boolean showingRightSideLabel,
	    WorkbenchGuiComponent frame)
	    throws NoninvertibleTransformException {
	    this.frame = frame;
	    this.showingSliderLabels = showingSliderLabels;
	    slider.addComponentListener(new ComponentAdapter() {
	        public void componentResized(ComponentEvent e) {
	            try {
	                updateComponents();
	            } catch (NoninvertibleTransformException x) {
	                //Eat it. [Jon Aquino]
	            }
	        }
	    });
	    if (showingSliderLabels) {
	        //Add a dummy label so that ZoomBars added to Toolboxes are
	        //packed properly. [Jon Aquino]
	        Hashtable labelTable = new Hashtable();
	        labelTable.put(new Integer(0), new JLabel(" "));
	        slider.setLabelTable(labelTable);
	    }
	    try {
	        jbInit();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    if (!showingRightSideLabel) {
	        remove(label);
	    }
	    label.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 3 && SwingUtilities.isRightMouseButton(e)) {
	                viewBlackboard().put(USER_DEFINED_MIN_SCALE, null);
	                viewBlackboard().put(USER_DEFINED_MAX_SCALE, null);
	                clearModelCaches();
	            }
	        }
	    });
	    slider.addMouseMotionListener(new MouseMotionAdapter() {
	        //Use #mouseDragged rather than JSlider#stateChanged because we
	        //are interested in user-initiated slider changes, not programmatic
	        //slider changes. [Jon Aquino]
	        public void mouseDragged(MouseEvent e) {
	            try {
	                layerViewPanel().erase((Graphics2D) layerViewPanel().getGraphics());
	                drawWireframe();
	                ScaleBarRenderer scaleBarRenderer =
	                    (ScaleBarRenderer) layerViewPanel().getRenderingManager().getRenderer(
	                        ScaleBarRenderer.CONTENT_ID);
	                if (scaleBarRenderer != null) {
	                    scaleBarRenderer.paint(
	                        (Graphics2D) layerViewPanel().getGraphics(),
	                        getScale());
	                }
	                updateLabel();
	            } catch (NoninvertibleTransformException x) {
	                //Eat it. [Jon Aquino]
	            }
	        }
	    });
	    if (slider.getUI() instanceof BasicSliderUI) {
	        slider.addMouseMotionListener(new MouseMotionAdapter() {
	            public void mouseMoved(MouseEvent e) {
	                if (layerViewPanel() == dummyLayerViewPanel) {
	                    return;
	                }
	                try {
	                    slider.setToolTipText(
	                        aplicacion.getI18nString("ZoomBar.ZoomTo")+" "
	                            + chooseGoodIncrement(
	                                toScale(
	                                    ((BasicSliderUI) slider.getUI()).valueForXPosition(
	                                        e.getX())))
	                                .toString());
	                } catch (NoninvertibleTransformException x) {
	                    slider.setToolTipText("Zoom");
	                }
	            }
	        });
	    }
	    labels.setPreferredSize(new Dimension(60, slider.getHeight()));
	    labels.setLayout(new BoxLayout(labels,BoxLayout.Y_AXIS));
	    labels.add(label);
	    labels.add(scalelabel);
	    
	    label.setPreferredSize(new Dimension(80, label.getHeight()));
	    slider.addKeyListener(new KeyAdapter() {
	        public void keyReleased(KeyEvent e) {
	            try {
	                if (e.getKeyCode() == KeyEvent.VK_LEFT
	                    || e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                    gestureFinished();
	                }
	            } catch (NoninvertibleTransformException t) {
	                layerViewPanel().getContext().handleThrowable(t);
	            }
	        }
	    });
	    slider.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            if (!slider.isEnabled()) {
	                return;
	            }
	            layerViewPanel().getRenderingManager().setPaintingEnabled(false);
	        }
	        public void mouseReleased(MouseEvent e) {
	            try {
	                gestureFinished();
	            } catch (NoninvertibleTransformException t) {
	                layerViewPanel().getContext().handleThrowable(t);
	            }
	        }
	    });
	    
	    GUIUtil
	        .addInternalFrameListener(
	            (JDesktopPane)frame.getDesktopPane(), // si se usa como Bean no tiene sentido el modelo InternalFrame por lo que asumo un castException [Juan Pablo]
	            GUIUtil.toInternalFrameListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            installListenersOnCurrentPanel();
	            try {
	                updateComponents();
	            } catch (NoninvertibleTransformException x) {
	                //Eat it. [Jon Aquino]
	            }
	        }
	    }));
	    installListenersOnCurrentPanel();
	    updateComponents();
	}

    private void installListenersOnCurrentPanel() {
        installViewListeners();
        installModelListeners();
    }

    private void installViewListeners() {
        //Use hash code to uniquely identify this zoom bar (there may be other
        //zoom bars) [Jon Aquino]
        String VIEW_LISTENERS_INSTALLED_KEY =
            Integer.toHexString(hashCode()) + " - VIEW LISTENERS INSTALLED";
        if (viewBlackboard().get(VIEW_LISTENERS_INSTALLED_KEY) != null) {
            return;
        }
        layerViewPanel().getViewport().addListener(new ViewportListener() {
            public void zoomChanged(Envelope modelEnvelope) {
                if (!viewBlackboard().get(CENTRE_LOCKED_KEY, false)) {
                    viewBlackboard().put(CENTRE_KEY, null);
                }
                viewBlackboard().put(SCALE_KEY, null);
                try {
                    if (layerViewPanel().getViewport().getScale() < getMinScale()) {
                        viewBlackboard().put(
                            USER_DEFINED_MIN_SCALE,
                            layerViewPanel().getViewport().getScale());
                    }
                    if (layerViewPanel().getViewport().getScale() > getMaxScale()) {
                        viewBlackboard().put(
                            USER_DEFINED_MAX_SCALE,
                            layerViewPanel().getViewport().getScale());
                    }
                    updateComponents();
                } catch (NoninvertibleTransformException e) {
                    //Eat it. [Jon Aquino]
                }
            }
        });
        viewBlackboard().put(VIEW_LISTENERS_INSTALLED_KEY, new Object());
    }

    private void installModelListeners() {
        //Use hash code to uniquely identify this zoom bar (there may be other
        //zoom bars) [Jon Aquino]
        String MODEL_LISTENERS_INSTALLED_KEY =
            Integer.toHexString(hashCode()) + " - MODEL LISTENERS INSTALLED";
        if (viewBlackboard().get(MODEL_LISTENERS_INSTALLED_KEY) != null) {
            return;
        }
        layerViewPanel().getLayerManager().addLayerListener(new LayerListener() {
            public void categoryChanged(CategoryEvent e) {}
            public void featuresChanged(FeatureEvent e) {
                if (e.getType() == FeatureEventType.ADDED
                    || e.getType() == FeatureEventType.DELETED
                    || e.getType() == FeatureEventType.GEOMETRY_MODIFIED) {
                    clearModelCaches();
                }
            }
            public void layerChanged(LayerEvent e) {
                if (e.getType() == LayerEventType.ADDED || e.getType() == LayerEventType.REMOVED) {
                    clearModelCaches();
                }
            }
        });
        viewBlackboard().put(MODEL_LISTENERS_INSTALLED_KEY, new Object());
    }

    private void queueComponentUpdate() {
        componentUpdateTimer.restart();
    }
    /** Coalesces component updates */
    private Timer componentUpdateTimer =
        GUIUtil.createRestartableSingleEventTimer(200, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                updateComponents();
            } catch (NoninvertibleTransformException x) {
                //Eat it. [Jon Aquino]
            }
        }
    });

    public void updateComponents() throws NoninvertibleTransformException {
        if (layerViewPanel() == dummyLayerViewPanel) {
            setComponentsEnabled(false);
            return;
        }
        setComponentsEnabled(true);
        //Must set slider value *before* updating the label on the right. [Jon Aquino]
        //I'm currently hiding the label on the right, to save real estate. [Jon Aquino]
        slider.setValue(
            toSliderValue(
                viewBlackboard().get(SCALE_KEY, layerViewPanel().getViewport().getScale())));
        updateLabel();
        updateSliderLabels();
    }

    private void gestureFinished() throws NoninvertibleTransformException {
        if (!slider.isEnabled()) {
            return;
        }
        try {
            viewBlackboard().put(CENTRE_LOCKED_KEY, true);
            try {
                layerViewPanel().getViewport().zoom(proposedModelEnvelope());
            } finally {
                viewBlackboard().put(CENTRE_LOCKED_KEY, false);
            }
        } finally {
            layerViewPanel().getRenderingManager().setPaintingEnabled(true);
        }
    }
    private Envelope proposedModelEnvelope() throws NoninvertibleTransformException {
        Coordinate centre =
            (Coordinate) viewBlackboard().get(
                CENTRE_KEY,
                EnvelopeUtil.centre(
                    layerViewPanel().getViewport().getEnvelopeInModelCoordinates()));
        double width = layerViewPanel().getWidth() / getScale();
        double height = layerViewPanel().getHeight() / getScale();
        Envelope proposedModelEnvelope =
            new Envelope(
                centre.x - (width / 2),
                centre.x + (width / 2),
                centre.y - (height / 2),
                centre.y + (height / 2));
        if (proposedModelEnvelope.getWidth() == 0 || proposedModelEnvelope.getHeight() == 0) {
            //We're zoomed waaay out! Avoid infinite scale. [Jon Aquino]
            proposedModelEnvelope = lastGoodEnvelope;
        } else {
            lastGoodEnvelope = proposedModelEnvelope;
        }
        return proposedModelEnvelope;
    }
    private double getScale() throws NoninvertibleTransformException {
        return toScale(slider.getValue());
    }
    private Stroke stroke = new BasicStroke(1);
    private void drawWireframe() throws NoninvertibleTransformException {
        Graphics2D g = (Graphics2D) layerViewPanel().getGraphics();
        g.setColor(Color.lightGray);
        g.setStroke(stroke);
        g.draw(getWireFrame());
    }
    private static final String SEGMENT_CACHE_KEY = ZoomBar.class.getName() + " - SEGMENT CACHE";
    private void clearModelCaches() {
        //Use LayerManager blackboard for segment cache, so that multiple
        //views can share it. [Jon Aquino]
        modelBlackboard().put(SEGMENT_CACHE_KEY, null);
        modelBlackboard().put(MIN_EXTENT_KEY, null);
        modelBlackboard().put(MAX_EXTENT_KEY, null);
        //It's expensive to recompute these cached values, so queue the call
        //to #updateComponents [Jon Aquino]
        queueComponentUpdate();
    }

    private LineSegmentEnvelopeIntersector lineSegmentEnvelopeIntersector =
        new LineSegmentEnvelopeIntersector();

    private Shape getWireFrame() throws NoninvertibleTransformException {
        AffineTransform transform =
            Viewport.modelToViewTransform(
                getScale(),
                new Point2D.Double(
                    proposedModelEnvelope().getMinX(),
                    proposedModelEnvelope().getMinY()),
                layerViewPanel().getSize().getHeight());
        Envelope proposedModelEnvelope = proposedModelEnvelope();
        GeneralPath wireFrame = new GeneralPath();
        ArrayList segments = new ArrayList(getSegmentCache());
        segments.addAll(toSegments(randomOnScreenGeometries()));
        for (Iterator i = segments.iterator(); i.hasNext();) {
            Coordinate[] coordinates = (Coordinate[]) i.next();
            boolean drawing = false;
            for (int j = 1; j < coordinates.length; j++) {
                if (!lineSegmentEnvelopeIntersector
                    .touches(coordinates[j - 1], coordinates[j], proposedModelEnvelope)) {
                    drawing = false;
                    continue;
                }
                if (!drawing) {
                    Point2D p1 =
                        transform.transform(
                            new Point2D.Double(coordinates[j - 1].x, coordinates[j - 1].y),
                            null);
                    wireFrame.moveTo((float) p1.getX(), (float) p1.getY());
                }
                Point2D p2 =
                    transform.transform(
                        new Point2D.Double(coordinates[j].x, coordinates[j].y),
                        null);
                wireFrame.lineTo((float) p2.getX(), (float) p2.getY());
                drawing = true;
            }
        }
        return wireFrame;
    }

    private Collection getSegmentCache() throws NoninvertibleTransformException {
        //Use LayerManager blackboard for segment cache, so that multiple
        //views can share it. [Jon Aquino]
        if (modelBlackboard().get(SEGMENT_CACHE_KEY) == null) {
            modelBlackboard().put(SEGMENT_CACHE_KEY, toSegments(randomGeometries()));
        }
        return (Collection) modelBlackboard().get(SEGMENT_CACHE_KEY);
    }
    private Collection toSegments(Collection geometries) {
        ArrayList segments = new ArrayList();
        for (Iterator i = geometries.iterator(); i.hasNext();) {
            Geometry geometry = (Geometry) i.next();
            segments.addAll(CoordinateArrays.toCoordinateArrays(geometry, false));
        }
        return segments;
    }
    private static final int RANDOM_ONSCREEN_GEOMETRIES = 100;
    private static final int RANDOM_GEOMETRIES = 100;

    private Collection randomGeometries(int maxSize, List features) {
        if (features.size() <= maxSize) {
            return FeatureUtil.toGeometries(features);
        }
        ArrayList randomGeometries = new ArrayList();
        for (int j = 0; j < maxSize; j++) {
            randomGeometries.add(
                ((Feature) features.get((int) (Math.random() * features.size()))).getGeometry());
        }
        return randomGeometries;
    }

    private Collection randomOnScreenGeometries() {
        ArrayList randomOnScreenGeometries = new ArrayList();
        if (totalGeometries() == 0 ) {
            return randomOnScreenGeometries;
        }
        for (Iterator i = layerViewPanel().getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getFeatureCollectionWrapper() == null)
                return randomOnScreenGeometries;
            randomOnScreenGeometries.addAll(
                randomGeometries(
                    RANDOM_ONSCREEN_GEOMETRIES
                        * layer.getFeatureCollectionWrapper().size()
                        / totalGeometries(),
                    layer.getFeatureCollectionWrapper().query(
                        layerViewPanel().getViewport().getEnvelopeInModelCoordinates())));
        }
        return randomOnScreenGeometries;
    }

    private Collection randomGeometries() {
        ArrayList randomGeometries = new ArrayList();
        if (totalGeometries() == 0) {
            return randomGeometries;
        }
        for (Iterator i = layerViewPanel().getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            if (layer.getFeatureCollectionWrapper() != null){
	            randomGeometries.addAll(
	                randomGeometries(
	                    RANDOM_GEOMETRIES
	                        * layer.getFeatureCollectionWrapper().size()
	                        / totalGeometries(),
	                    layer.getFeatureCollectionWrapper().getFeatures()));
            }
        }
        return randomGeometries;
    }

    private int toSliderValue(double scale) throws NoninvertibleTransformException {
        return slider.getMaximum()
            - (int) (slider.getMaximum()
                * (MathUtil.base10Log(scale) - MathUtil.base10Log(getMinScale()))
                / (MathUtil.base10Log(getMaxScale()) - MathUtil.base10Log(getMinScale())));
    }
    private double getMinExtent() throws NoninvertibleTransformException {
        if (modelBlackboard().get(MIN_EXTENT_KEY) == null) {
            double smallSegmentLength = chooseSmallSegmentLength(getSegmentCache());
            //-1 smallSegmentLength means there is no data or the data are all
            //points (i.e. no segments). [Jon Aquino]
            if (smallSegmentLength == -1) {
                return -1;
            }
            modelBlackboard().put(MIN_EXTENT_KEY, smallSegmentLength);
        }
        Assert.isTrue(modelBlackboard().getDouble(MIN_EXTENT_KEY) > 0);
        return modelBlackboard().getDouble(MIN_EXTENT_KEY);
    }

    private double chooseSmallSegmentLength(Collection segmentCache) {
        int segmentsChecked = 0;
        double smallSegmentLength = -1;
        for (Iterator i = segmentCache.iterator(); i.hasNext();) {
            Coordinate[] coordinates = (Coordinate[]) i.next();
            for (int j = 1; j < coordinates.length; j++) {
                double segmentLength = coordinates[j].distance(coordinates[j - 1]);
                segmentsChecked++;
                if (segmentLength > 0
                    && (smallSegmentLength == -1 || segmentLength < smallSegmentLength)) {
                    smallSegmentLength = segmentLength;
                }
                if (segmentsChecked > 100) {
                    break;
                }
            }
            if (segmentsChecked > 100) {
                break;
            }
        }
        return smallSegmentLength;
    }

    private double getMaxExtent() throws NoninvertibleTransformException {
        if (modelBlackboard().get(MAX_EXTENT_KEY) == null) {
            if (getSegmentCache().isEmpty()) {
                return -1;
            }
            modelBlackboard().put(
                MAX_EXTENT_KEY,
                layerViewPanel().getLayerManager().getEnvelopeOfAllLayers().getWidth());
        }
        return modelBlackboard().getDouble(MAX_EXTENT_KEY);
    }

    private double getMaxScale() throws NoninvertibleTransformException {
        double maxScale =
            (getMinExtent() == -1 || getMinExtent() == 0)
                ? 1E3
                : (1000 * layerViewPanel().getWidth() / getMinExtent());
        if (viewBlackboard().get(USER_DEFINED_MAX_SCALE) != null) {
            return Math.max(maxScale, viewBlackboard().getDouble(USER_DEFINED_MAX_SCALE));
        }
        return maxScale;
    }

    private double getMinScale() throws NoninvertibleTransformException {
        double minScale =
            (getMaxExtent() == -1 || getMaxExtent() == 0)
                ? 1E-3
                : (0.001 * layerViewPanel().getWidth() / getMaxExtent());
        if (viewBlackboard().get(USER_DEFINED_MIN_SCALE) != null) {
            return Math.min(minScale, viewBlackboard().getDouble(USER_DEFINED_MIN_SCALE));
        }
        return minScale;
    }

    private double toScale(int sliderValue) throws NoninvertibleTransformException {
        return Math.pow(
            10,
            ((slider.getMaximum() - sliderValue)
                * (MathUtil.base10Log(getMaxScale()) - MathUtil.base10Log(getMinScale()))
                / slider.getMaximum())
                + MathUtil.base10Log(getMinScale()));
    }
    private void setComponentsEnabled(boolean componentsEnabled) {
        slider.setEnabled(componentsEnabled);
        label.setEnabled(componentsEnabled);
    }
    private static final String SCALE_KEY = ZoomBar.class.getName() + " - SCALE";
    private static final String CENTRE_KEY = ZoomBar.class.getName() + " - CENTRE";
    //Store centre-locked flag on blackboard rather than field because there could
    //be several zoom bars [Jon Aquino]
    private static final String CENTRE_LOCKED_KEY = ZoomBar.class.getName() + " - CENTRE LOCKED";
    private static final String MIN_EXTENT_KEY = ZoomBar.class.getName() + " - MIN EXTENT";
    private static final String USER_DEFINED_MIN_SCALE =
        ZoomBar.class.getName() + " - USER DEFINED MIN SCALE";
    private static final String USER_DEFINED_MAX_SCALE =
        ZoomBar.class.getName() + " - USER DEFINED MAX SCALE";
    private static final String MAX_EXTENT_KEY = ZoomBar.class.getName() + " - MAX EXTENT";

    private Blackboard viewBlackboard() {
        return layerViewPanel().getBlackboard();
    }
    private Blackboard modelBlackboard() {        
        return layerViewPanel().getLayerManager().getBlackboard();
    }
    private final LayerViewPanel dummyLayerViewPanel = new LayerViewPanel(new LayerManager(), new LayerViewPanelContext() {

		public void setStatusMessage(String message) {
		}

		public void warnUser(String warning) {
		}

		public void handleThrowable(Throwable t) {
		}
    });
    private LayerViewPanel layerViewPanel() {
        if (!(frame.getActiveInternalFrame() instanceof LayerViewPanelProxy)) {
            return dummyLayerViewPanel;
        }
        return ((LayerViewPanelProxy) frame.getActiveInternalFrame()).getLayerViewPanel();
    }
    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        label.setText(" ");
        slider.setPaintLabels(true);
        slider.setToolTipText("Zoom");
        slider.setMaximum(1000);
        this.add(slider, BorderLayout.CENTER);
        this.add(labels, BorderLayout.EAST);
    }

    private void updateLabel() throws NoninvertibleTransformException {
        //Inexpensive. [Jon Aquino]
        label.setText(chooseGoodIncrement(getScale()).toString());
        scalelabel.setText("(1:"+ NumberFormat.getIntegerInstance().format(getNormalizedScale()) + ")");
    }

    private RoundQuantity chooseGoodIncrement(double scale) {
        return incrementChooser.chooseGoodIncrement(
            metricUnits,
            layerViewPanel().getWidth() / scale);
    }
    
    double SCREENRES = Toolkit.getDefaultToolkit().getScreenResolution(); //72 dpi or 96 dpi or ..     
    double INCHTOCM = 2.54; //cm
    
private double getNormalizedScale()
{
Viewport port = layerViewPanel().getViewport();
int panelWidth = port.getPanel().getWidth(); //pixel
double modelWidth = port.getEnvelopeInModelCoordinates().getWidth(); //m

double horizontalScale = modelWidth*100 / (this.INCHTOCM / this.SCREENRES * panelWidth);
return horizontalScale;
}
    private Font sliderLabelFont = new Font("Dialog", Font.PLAIN, 10);
    private boolean showingSliderLabels;
    private void updateSliderLabels() throws NoninvertibleTransformException {
        //Expensive if the data cache has been cleared. [Jon Aquino]
        if (!showingSliderLabels) {
            return;
        }
        if (!(slider.getUI() instanceof BasicSliderUI)) {
            return;
        }
        Hashtable labelTable = new Hashtable();
        final int LABEL_WIDTH = 60;
        int lastLabelPosition = -2 * LABEL_WIDTH;
        for (int i = 0; i < slider.getWidth(); i++) {
            if (i < (lastLabelPosition + LABEL_WIDTH)) {
                continue;
            }
            int sliderValue = ((BasicSliderUI) slider.getUI()).valueForXPosition(i);
            JLabel label = new JLabel(chooseGoodIncrement(toScale(sliderValue)).toString());
            label.setFont(sliderLabelFont);
            labelTable.put(new Integer(sliderValue), label);
            lastLabelPosition = i;
        }
        if (labelTable.isEmpty()) {
            //Get here during initialization. [Jon Aquino]
            return;
        }
        slider.setLabelTable(labelTable);
    }

}
