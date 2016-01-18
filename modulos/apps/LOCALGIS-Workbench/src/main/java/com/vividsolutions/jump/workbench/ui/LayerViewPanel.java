package com.vividsolutions.jump.workbench.ui;
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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.io.datasource.IGeopistaConnection;
import com.geopista.ui.plugin.wfs.CoordinateConversion;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FenceLayerFinder;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerEventType;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.DummyTool;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;
import com.vividsolutions.jump.workbench.ui.renderer.java2D.Java2DConverter;
import com.vividsolutions.jump.workbench.ui.renderer.style.PinEqualCoordinatesStyle;


//<<TODO:FIX>> One user (GK) gets an infinite repaint loop (the map moves around
//chaotically) when the LayerViewPanel is put side by side with the LayerTreePanel
//in a GridBagLayout. Something to do with determining the size, I think --
//the problem doesn't occur when the size is well defined (as when the two
//panels are in a GridLayout or SplitPane). [Jon Aquino]

/**
 * Be sure to call #dispose() when the LayerViewPanel is no longer
 * needed.
 */
public class LayerViewPanel extends JPanel implements LayerListener,
    LayerManagerProxy, SelectionManagerProxy, ILayerViewPanel {
    
    private ToolTipWriter toolTipWriter = new ToolTipWriter(this);
    BorderLayout borderLayout1 = new BorderLayout();
    private ILayerManager layerManager;
    private CursorTool currentCursorTool = new DummyTool();
    private Viewport viewport = new Viewport(this);
    private boolean viewportInitialized = false;
    private java.awt.Point lastClickedPoint;
    private ArrayList listeners = new ArrayList();
    private LayerViewPanelContext context;
    private RenderingManager renderingManager = new RenderingManager(this);
    private FenceLayerFinder fenceLayerFinder;
    private SelectionManager selectionManager;
    private Blackboard blackboard = new Blackboard();
    
    private PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);
    
    private double appliedScale = 0;

    //empty constructor for java2xml
    public LayerViewPanel()
    {
    
    }
    public LayerViewPanel(ILayerManager layerManager,
        LayerViewPanelContext context) {
        init(layerManager, context);
    }
    
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getAppliedScale()
	 */
    @Override
	public double getAppliedScale (){
    	return appliedScale;
    }
    
    public int getWidth(){
    	return super.getWidth();
    }
    
    public int getHeight(){
    	return super.getHeight();
    }
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#setAppliedScale(double)
	 */
    @Override
	public void setAppliedScale(double appScale){
    	this.appliedScale = appScale;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#init(com.vividsolutions.jump.workbench.model.LayerManager, com.vividsolutions.jump.workbench.ui.LayerViewPanelContext)
	 */
	@Override
	public void init(ILayerManager layerManager, LayerViewPanelContext context)
	{
		//Errors occur if the LayerViewPanel is sized to 0. [Jon Aquino] 
        setMinimumSize(new Dimension(100, 100));

        //Set toolTipText to null to disable, "" to use default (i.e. show all attributes),
        //or a custom template. [Jon Aquino]
        setToolTipText("");
        GUIUtil.fixClicks(this);

        try {
            this.context = context;
            this.layerManager = layerManager;
            selectionManager = new SelectionManager(this, this);
            fenceLayerFinder = new FenceLayerFinder(this);

            //Immediately register with the LayerManager because #getLayerManager will
            //be called right away (when #setBackground is called in #jbInit) [Jon Aquino]
            layerManager.addLayerListener(this);

            try {
                jbInit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        mouseLocationChanged(e);
                    }

                    public void mouseMoved(MouseEvent e) {
                        mouseLocationChanged(e);
                    }

                    private void mouseLocationChanged(MouseEvent e) {
                        try {
                            Point2D p = getViewport().toModelPoint(e.getPoint());
                            seleccionAutomaticaMunicipio(p);
                            fireCursorPositionChanged(format(p.getX()),
                                format(p.getY()));
                        } catch (Throwable t) {
                            LayerViewPanel.this.context.handleThrowable(t);
                        }
                    }
                });
        } catch (Throwable t) {
            if (context!=null)
            	context.handleThrowable(t);
        }
	}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getToolTipWriter()
	 */
	@Override
	public ToolTipWriter getToolTipWriter() {
        return toolTipWriter;
    }

    //In Java 1.3, if you try and do a #mouseClicked or a #mouseDragged on an
    //inactive internal frame, it won't work. [Jon Aquino]
    //In Java 1.4, the #mouseDragged will work, but not the #mouseClicked.
    //See the Sun Java Bug Database, ID 4398733. The evaluation for Bug ID 4256525
    //states that the fix is scheduled for the Java release codenamed Tiger.
    //[Jon Aquino]
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getToolTipText(java.awt.event.MouseEvent)
	 */
    @Override
	public String getToolTipText(MouseEvent event) {
        return toolTipWriter.write(getToolTipText(), event.getPoint());
    }

    public static List components(Geometry g) {
        if (!(g instanceof GeometryCollection)) {
            return Arrays.asList(new Object[] { g });
        }

        GeometryCollection c = (GeometryCollection) g;
        ArrayList components = new ArrayList();

        for (int i = 0; i < c.getNumGeometries(); i++) {
            components.addAll(components(c.getGeometryN(i)));
        }

        return components;
    }

    /**
     * Workaround for the fact that GeometryCollection#intersects is not
     * currently implemented.
     */
    public static boolean intersects(Geometry a, Geometry b) {
        GeometryFactory factory = new GeometryFactory(a.getPrecisionModel(),
                a.getSRID());
        List aComponents = components(a);
        List bComponents = components(b);

        for (Iterator i = aComponents.iterator(); i.hasNext();) {
            Geometry aComponent = (Geometry) i.next();
            Assert.isTrue(!(aComponent instanceof GeometryCollection));

            //Collapse to point as workaround for JTS defect: #contains doesn't work for
            //polygons and  zero-length vectors. [Jon Aquino]
            aComponent = collapseToPointIfPossible(aComponent, factory);

            for (Iterator j = bComponents.iterator(); j.hasNext();) {
                Geometry bComponent = (Geometry) j.next();
                Assert.isTrue(!(bComponent instanceof GeometryCollection));
                bComponent = collapseToPointIfPossible(bComponent, factory);

                if (aComponent.intersects(bComponent)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Geometry collapseToPointIfPossible(Geometry g,
        GeometryFactory factory) {
        if (!g.isEmpty() && PinEqualCoordinatesStyle.coordinatesEqual(g)) {
            g = factory.createPoint(g.getCoordinate());
        }

        return g;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#visibleLayerToFeaturesInFenceMap()
	 */
    @Override
	public Map visibleLayerToFeaturesInFenceMap() {
        Map visibleLayerToFeaturesInFenceMap = visibleLayerToFeaturesInFenceMap(getFence());
        visibleLayerToFeaturesInFenceMap.remove(new FenceLayerFinder(this).getLayer());

        return visibleLayerToFeaturesInFenceMap;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#visibleLayerToFeaturesInFenceMap(com.vividsolutions.jts.geom.Geometry)
	 */
    @Override
	public Map visibleLayerToFeaturesInFenceMap(Geometry fence) {
        Map map = new HashMap();

        for (Iterator i = getLayerManager().iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();

            if (!layer.isVisible()) {
                continue;
            }

            HashSet features = new HashSet();

			if (layer.getFeatureCollectionWrapper() != null){
	            for (Iterator j = layer.getFeatureCollectionWrapper()
	                                   .query(fence.getEnvelopeInternal()).iterator();
	                    j.hasNext();) {
	                Feature candidate = (Feature) j.next();

	                if (intersects(candidate.getGeometry(), fence)) {
	                    features.add(candidate);
	                }
	            }
				
			}

            if (!features.isEmpty()) {
                map.put(layer, features);
            }
        }

        return map;
    }

    public static JPopupMenu popupMenu() {
        
        JPopupMenu popupMenu = null;
        
        if(AppContext.getApplicationContext().getMainFrame() instanceof WorkbenchGuiComponent)
        {
        	WorkbenchGuiComponent workbenchGuiComponent = (WorkbenchGuiComponent) AppContext.getApplicationContext().getMainFrame();
            popupMenu = workbenchGuiComponent.getLayerViewPopupMenu();
        }
        
        return popupMenu;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#setCurrentCursorTool(com.vividsolutions.jump.workbench.ui.cursortool.CursorTool)
	 */
    @Override
	public void setCurrentCursorTool(CursorTool currentCursorTool) {
    	if (currentCursorTool==this.currentCursorTool)
    		return; // Avoid to change cursortool when currentCursorTool is the same than previous
    	
        this.currentCursorTool.deactivate();
        removeMouseListener(this.currentCursorTool);
        removeMouseMotionListener(this.currentCursorTool);
        this.currentCursorTool = currentCursorTool;
        currentCursorTool.activate(this);
        setCursor(currentCursorTool.getCursor());
        addMouseListener(currentCursorTool);
        addMouseMotionListener(currentCursorTool);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#setViewportInitialized(boolean)
	 */
    @Override
	public void setViewportInitialized(boolean viewportInitialized) {
        this.viewportInitialized = viewportInitialized;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getCurrentCursorTool()
	 */
    @Override
	public CursorTool getCurrentCursorTool() {
        return currentCursorTool;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getLastClickedPoint()
	 */
    @Override
	public java.awt.Point getLastClickedPoint() {
        return lastClickedPoint;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getViewport()
	 */
    @Override
	public Viewport getViewport() {
        return viewport;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getJava2DConverter()
	 */
    @Override
	public Java2DConverter getJava2DConverter() {
        return viewport.getJava2DConverter();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getFence()
	 */
    @Override
	public Geometry getFence() {
        return fenceLayerFinder.getFence();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getLayerManager()
	 */
    @Override
	public ILayerManager getLayerManager() {
        return layerManager;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#featuresChanged(com.vividsolutions.jump.workbench.model.FeatureEvent)
	 */
    @Override
	public void featuresChanged(FeatureEvent e) {
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#categoryChanged(com.vividsolutions.jump.workbench.model.CategoryEvent)
	 */
    @Override
	public void categoryChanged(CategoryEvent e) {
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#layerChanged(com.vividsolutions.jump.workbench.model.LayerEvent)
	 */
    @Override
	public void layerChanged(LayerEvent e) {
        try {
            if (e.getType() == LayerEventType.METADATA_CHANGED) {
                return;
            }

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            //Invoke later because other layers may be created in a few 
                            //moments. [Jon Aquino]        
                            initializeViewportIfNecessary();
                        } catch (Throwable t) {
                            context.handleThrowable(t);
                        }
                    }
                });

            if ((e.getType() == LayerEventType.ADDED) ||
                    (e.getType() == LayerEventType.REMOVED) ||
                    (e.getType() == LayerEventType.APPEARANCE_CHANGED)) {
                renderingManager.render(e.getLayerable(),true);
            } else if (e.getType() == LayerEventType.VISIBILITY_CHANGED) {
                renderingManager.render(e.getLayerable(), false);
            } else {
                Assert.shouldNeverReachHere();
            }
        } catch (Throwable t) {
            context.handleThrowable(t);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#createBlankPanelImage()
	 */
    @Override
	public Image createBlankPanelImage() {
        //The pixels will be transparent because we're creating a BufferedImage
        //from scratch instead of calling #createImage. [Jon Aquino]
    
        return new BufferedImage(getWidth()>0?getWidth():1, getHeight()>0?getHeight():1,
            BufferedImage.TYPE_INT_ARGB);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#repaint()
	 */
    @Override
	public void repaint() {
        if (renderingManager == null || getLayerManager()==null) {
            //It's null during initialization [Jon Aquino]
            superRepaint();

            return;
        }

        renderingManager.renderAll();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#superRepaint()
	 */
    @Override
	public void superRepaint() {
        super.repaint();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#paintComponent(java.awt.Graphics)
	 */
    @Override
	public void paintComponent(Graphics g) {
        try {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintComponent(g);
            erase((Graphics2D) g);
            renderingManager.copyTo((Graphics2D) g);

            //g may not be the same as the result of #getGraphics; it may be an
            //off-screen buffer. [Jon Aquino]
            firePainted(g);
        } catch (Throwable t) {
            context.handleThrowable(t);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#erase(java.awt.Graphics2D)
	 */
    @Override
	public void erase(Graphics2D g) {
        fill(g, Color.white);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#fill(java.awt.Graphics2D, java.awt.Color)
	 */
    @Override
	public void fill(Graphics2D g, Color color) {
        g.setColor(color);

        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, getWidth(),
                getHeight());
        g.fill(r);
    }

    void jbInit() throws Exception {
        this.setBackground(Color.white);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    this_mouseReleased(e);
                }
            });
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    this_componentResized(e);
                }
            });
        this.setLayout(borderLayout1);
    }

    void this_componentResized(ComponentEvent e) {
        try {
            viewport.update();
            
        } catch (Throwable t) {
            context.handleThrowable(t);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getContext()
	 */
    @Override
	public LayerViewPanelContext getContext() {
        return context;
    }

    void this_mouseReleased(MouseEvent e) {
        lastClickedPoint = e.getPoint();

        if (currentCursorTool.isRightMouseButtonUsed()) {
            return;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            //Custom workbenches might not add any items to the LayerViewPanel popup menu.
            //[Jon Aquino]            
        	if (((WorkbenchGuiComponent)context).getLayerViewPopupMenu().getSubElements().length == 0) {
                return;
            }
           
        	((WorkbenchGuiComponent)context).getLayerViewPopupMenu().show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * When the first layer is added, zoom to its extent.
     */
    private void initializeViewportIfNecessary()
        throws NoninvertibleTransformException {
        if (!viewportInitialized && (layerManager.size() > 0) &&
                (layerManager.getEnvelopeOfAllLayers().getWidth() > 0)) {
            setViewportInitialized(true);
            viewport.zoomToFullExtent();

            //Return here because #zoomToFullExtent will eventually cause a call to
            //#paintComponent [Jon Aquino]
            return;
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#addListener(com.vividsolutions.jump.workbench.ui.LayerViewPanelListener)
	 */
    @Override
	public void addListener(LayerViewPanelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#removeListener(com.vividsolutions.jump.workbench.ui.LayerViewPanelListener)
	 */
    @Override
	public void removeListener(LayerViewPanelListener listener) {
        listeners.remove(listener);
    }
    /**
     * Borrado de los listeners para limpieza.
      @baseSatec Destruccion del elemento.    
     */
    public void removeAllListener() {
        listeners.clear();
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#format(double)
	 */
    @Override
	public String format(double d) {
        double pixelWidthInModelUnits = viewport.getEnvelopeInModelCoordinates()
                                                .getWidth() / getWidth();

        return format(d, pixelWidthInModelUnits);
    }

    protected String format(double d, double pixelWidthInModelUnits) {
        int precisionInDecimalPlaces = (int) Math.max(0, //because if pixelWidthInModelUnits > 1, the negative log will be negative
                Math.round( //not floor, which brings 0.999 down to 0
                    (-Math.log(pixelWidthInModelUnits)) / Math.log(10)));
        precisionInDecimalPlaces++;

        //An extra decimal place, for good measure [Jon Aquino]
        String formatString = "#.";

        for (int i = 0; i < precisionInDecimalPlaces; i++) {
            formatString += "#";
        }

        return new DecimalFormat(formatString).format(d);
    }

	
    private void firePainted(Graphics graphics) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            LayerViewPanelListener l = (LayerViewPanelListener) i.next();
            l.painted(graphics);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#fireSelectionChanged()
	 */
    @Override
	public void fireSelectionChanged() {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            LayerViewPanelListener l = (LayerViewPanelListener) i.next();
            if (l != null)
            	l.selectionChanged();
        }
    }

    private void fireCursorPositionChanged(String x, String y) {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            LayerViewPanelListener l = (LayerViewPanelListener) i.next();
            l.cursorPositionChanged(x, y);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getRenderingManager()
	 */
    @Override
	public RenderingManager getRenderingManager() {
        return renderingManager;
    }

    //Not sure where this method should reside. [Jon Aquino]
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#featuresWithVertex(java.awt.geom.Point2D, double, java.util.Collection)
	 */
    @Override
	public Collection featuresWithVertex(Point2D viewPoint,
        double viewTolerance, Collection features)
        throws NoninvertibleTransformException {
        Point2D modelPoint = viewport.toModelPoint(viewPoint);
        double modelTolerance = viewTolerance / viewport.getScale();
        Envelope searchEnvelope = new Envelope(modelPoint.getX() -
                modelTolerance, modelPoint.getX() + modelTolerance,
                modelPoint.getY() - modelTolerance,
                modelPoint.getY() + modelTolerance);
        Collection featuresWithVertex = new ArrayList();

        for (Iterator j = features.iterator(); j.hasNext();) {
            Feature feature = (Feature) j.next();

            if (geometryHasVertex(feature.getGeometry(), searchEnvelope)) {
                featuresWithVertex.add(feature);
            }
        }

        return featuresWithVertex;
    }

    private boolean geometryHasVertex(Geometry geometry, Envelope searchEnvelope) {
        Coordinate[] coordinates = geometry.getCoordinates();

        for (int i = 0; i < coordinates.length; i++) {
            if (searchEnvelope.contains(coordinates[i])) {
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        if(null!=renderingManager) renderingManager.dispose();
        if(null!=selectionManager) selectionManager.dispose();
        if(null!=layerManager) layerManager.removeLayerListener(this);
        layerManager=null;
        context=null;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#flash(java.awt.Shape, java.awt.Color, java.awt.Stroke, int)
	 */
    @Override
	public void flash(final Shape shape, Color color, Stroke stroke,
        final int millisecondDelay) {
        final Graphics2D graphics = (Graphics2D) getGraphics();
        graphics.setColor(color);
        graphics.setXORMode(Color.white);
        graphics.setStroke(stroke);

        try {
            GUIUtil.invokeOnEventThread(new Runnable() {
                    public void run() {
                        try {
                            graphics.draw(shape);

                            //Use sleep rather than Timer (which could allow a third party to paint
                            //the panel between my XOR draws, messing up the XOR). Hopefully the user
                            //won't Alt-Tab away and back! [Jon Aquino]
                            Thread.sleep(millisecondDelay);
                            graphics.draw(shape);
                        } catch (Throwable t) {
                        	if (!(t.getMessage()!=null) && (t.getMessage().startsWith("Unable to Stroke")))
                        		getContext().handleThrowable(t);
                        }
                    }
                });
        } catch (Throwable t) {
            getContext().handleThrowable(t);
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getSelectionManager()
	 */
    @Override
	public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getBlackboard()
	 */
    @Override
	public Blackboard getBlackboard() {
        return blackboard;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#flash(com.vividsolutions.jts.geom.GeometryCollection)
	 */
    @Override
	public void flash(final GeometryCollection geometryCollection)
        throws NoninvertibleTransformException {
        flash(getViewport().getJava2DConverter().toShape(geometryCollection),
            Color.red,
            new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
            100);
    }
    
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getNormalizedScale(double)
	 */
	@Override
	public double getNormalizedScale(double scale) {
		double ppm = (Double.parseDouble(UserPreferenceStore.
				getUserPreference(UserPreferenceConstants.DEFAULT_SCREEN_WEIGHT, "30.5", true))/100)
				/ Toolkit.getDefaultToolkit().getScreenSize().width;
		//Para mi monitor: 0.375/1280;
		//ppm = 0.375/1280;
		return viewport.getNormalizedScaleForPPM(scale, ppm);
	}
	
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#getScaleFromNormalized(double)
	 */
	@Override
	public double getScaleFromNormalized (double normScale)
	{
		double ppm = (Double.parseDouble(UserPreferenceStore.
				getUserPreference(UserPreferenceConstants.DEFAULT_SCREEN_WEIGHT, "30.5", true))/100)
				/ Toolkit.getDefaultToolkit().getScreenSize().width;
		ppm = 1/ppm;
			
		return viewport.getScaleForNSnPPM(normScale, ppm);
				
	}
	
	
	static HashMap geometriaMunicipio=new HashMap();
	
	private void seleccionAutomaticaMunicipio(Point2D p){
        //Comprueba en qué municipio nos encontramos y cambia el ComboBox de municipios
        //si hemos seleccionado selección automática
		try{
			if (AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SEL_MUNI_AUTO)!=null && ((Boolean)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.SEL_MUNI_AUTO)).booleanValue()){
		        List listaMunicipios = AppContext.getAlMunicipios();
		        int n = listaMunicipios.size();
		        String sridDefecto = "";
		    	Point point = new GeometryFactory().createPoint(new Coordinate(p.getX(),p.getY()));
		    	Iterator itLayers = getLayerManager().getLayers().iterator();
		    	
		    	Layer layer = null;
		    	boolean continuar = false;
		    	while (itLayers.hasNext() && !continuar){
		    		layer = (Layer)itLayers.next();
		    		if (layer.getDataSourceQuery() != null){
		    			//Solo gestionamos capas de LOCALGIS
		    			if (layer.getDataSourceQuery().getDataSource().getConnection() instanceof IGeopistaConnection)
		    				continuar = true;
		    		}
		    	}
		    	//Solo para capas localgis
		    	if (continuar)  {		    	
			    	System.out.println("Capa seleccionAutomatica:"+layer.getName());
			    	CoordinateSystem inCoord = this.getLayerManager().getCoordinateSystem();
				    IGeopistaConnection geopistaConnection = (IGeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();
				    DriverProperties driverProperties = geopistaConnection.getDriverProperties();
				    driverProperties.put("filtrogeometrico",point);
				    driverProperties.put("srid_destino",inCoord.getEPSGCode());
				    geopistaConnection.setDriverProperties(driverProperties);
	
				    //Creamos una coleccion para almacenar las excepciones que se producen
				    Collection exceptions = new ArrayList();
				    //Miro en que srid por defecto se almacenan las features en la base de datos
				    sridDefecto = geopistaConnection.getSRIDDefecto(true, -1);
				   
			    	CoordinateSystem outCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));
		            if (inCoord.getEPSGCode() != outCoord.getEPSGCode() &&  inCoord.getDatum().equalsIgnoreCase(outCoord.getDatum())){
		            	//No se aplica la rejilla porque el sistema va a estar en ETRS89: transformacion matematica
		            	CoordinateConversion.instance().reproject(point, inCoord, outCoord);
		            	point.setSRID(Integer.parseInt(sridDefecto));
		            }
			        for (int i=0;i<n;i++){
			        	Municipio municipio = (Municipio)listaMunicipios.get(i);
			        	Geometry geom=null;
			        	if (geometriaMunicipio.get(municipio.getId())!=null){
			        		geom=(Geometry)geometriaMunicipio.get(municipio.getId());
			        	}
			        	else{
			        		geom =  geopistaConnection.obtenerGeometriaMunicipio(municipio.getId());		        	
			        		geometriaMunicipio.put(municipio.getId(), geom);
			        	}
			        	
			        	//System.out.println("Geometria Municipio:"+municipio.getId()+geom);
			        	//El municipio podría no tener geometría
			        	if (geom!=null){
					        geom.setSRID(Integer.parseInt(sridDefecto));
					        
				        	if (geom.contains(point)){
				        		JComboBox jComboBox = (JComboBox)AppContext.getApplicationContext().getBlackboard().get(UserPreferenceConstants.MUNI_COMBO);
				        		jComboBox.setSelectedIndex(i);
				        		AppContext.getApplicationContext().getBlackboard().put("NOMBRE_MUNICIPIO", municipio.getNombreOficial());
				        		AppContext.getApplicationContext().getBlackboard().put("NOMBRE_PROVINCIA", municipio.getProvincia().getNombreOficial());
				        		break;
				        	}
			        	}
			        }
				}
			}
        } catch (Throwable t) {
            LayerViewPanel.this.context.handleThrowable(t);
        }

	}
	
	
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.ILayerViewPanel#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		if (propertySupport!=null)
			propertySupport.firePropertyChange(propertyName, oldValue, newValue);
	}
    
}
