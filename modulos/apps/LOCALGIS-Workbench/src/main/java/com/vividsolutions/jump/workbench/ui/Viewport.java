
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

package com.vividsolutions.jump.workbench.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.io.datasource.IGeopistaConnection;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.renderer.java2D.Java2DConverter;

/**
 * Controls the area on the model being viewed by a LayerViewPanel.
 */

//<<TODO:NAMING>> Rename to Viewport [Jon Aquino]
public class Viewport implements Java2DConverter.PointConverter, IViewport {
    static private final int INITIAL_VIEW_ORIGIN_X = 0;
    static private final int INITIAL_VIEW_ORIGIN_Y = 0;
    private ArrayList listeners = new ArrayList();
    private Java2DConverter java2DConverter;
    private LayerViewPanel panel;
    
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Viewport.class);


    /**
     * Origin of view as perceived by model, that is, in model space
     */
    private Point2D viewOriginAsPerceivedByModel =
        new Point2D.Double(INITIAL_VIEW_ORIGIN_X, INITIAL_VIEW_ORIGIN_Y);
    private double scale = 1;
    private AffineTransform modelToViewTransform;
    private ZoomHistory zoomHistory;

    public Viewport(LayerViewPanel panel) {
        this.panel = panel;
        zoomHistory = new ZoomHistory(panel);
        java2DConverter = new Java2DConverter(this);
        panel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                fireZoomChanged(getEnvelopeInModelCoordinates());
            }
        });
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getPanel()
	 */
    @Override
	public LayerViewPanel getPanel() {
        return panel;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#addListener(com.vividsolutions.jump.workbench.ui.ViewportListener)
	 */
    @Override
	public void addListener(ViewportListener l) {
        listeners.add(l);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#removeListener(com.vividsolutions.jump.workbench.ui.ViewportListener)
	 */
    @Override
	public void removeListener(ViewportListener l) {
        listeners.remove(l);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getJava2DConverter()
	 */
    @Override
	public Java2DConverter getJava2DConverter() {
        return java2DConverter;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getZoomHistory()
	 */
    @Override
	public ZoomHistory getZoomHistory() {
        return zoomHistory;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#update()
	 */
    @Override
	public void update() throws NoninvertibleTransformException {
    	update(true);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#update(boolean)
	 */
    @Override
	public void update(boolean fireZoomChanged) throws NoninvertibleTransformException {
        modelToViewTransform =
            modelToViewTransform(scale, viewOriginAsPerceivedByModel, panel.getSize().height);
        panel.getRenderingManager().renderAll(true);

        if (fireZoomChanged)
        	this.fireZoomChanged(this.getEnvelopeInModelCoordinates()); // JPC: notifies viewport update

        panel.repaint();
    }

    public static AffineTransform modelToViewTransform(
        double scale,
        Point2D viewOriginAsPerceivedByModel,
        double panelHeight) {
        AffineTransform modelToViewTransform = new AffineTransform();
        modelToViewTransform.translate(0, panelHeight);
        modelToViewTransform.scale(1, -1);
        modelToViewTransform.scale(scale, scale);
        modelToViewTransform.translate(
            -viewOriginAsPerceivedByModel.getX(),
            -viewOriginAsPerceivedByModel.getY());
        return modelToViewTransform;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getScale()
	 */
    @Override
	public double getScale() {
        return scale;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#initialize(double, java.awt.geom.Point2D)
	 */
    @Override
	public void initialize(double newScale, Point2D newViewOriginAsPerceivedByModel) {
        setScale(newScale);
        viewOriginAsPerceivedByModel = newViewOriginAsPerceivedByModel;

        //Don't call #update here, because this method may be called before the
        //panel has been made visible, causing LayerViewPanel#createImage
        //to return null, causing a NullPointerException in
        //LayerViewPanel#updateImageBuffer. [Jon Aquino]
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getOriginInModelCoordinates()
	 */
    @Override
	public Point2D getOriginInModelCoordinates() {
        return viewOriginAsPerceivedByModel;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#zoom(java.awt.geom.Point2D, double, double)
	 */
    @Override
	public void zoom(
        Point2D centreOfNewViewAsPerceivedByOldView,
        double widthOfNewViewAsPerceivedByOldView,
        double heightOfNewViewAsPerceivedByOldView)
        throws NoninvertibleTransformException {
        double zoomFactor =
            Math.min(
                panel.getSize().width / widthOfNewViewAsPerceivedByOldView,
                panel.getSize().height / heightOfNewViewAsPerceivedByOldView);
        double realWidthOfNewViewAsPerceivedByOldView = panel.getSize().width / zoomFactor;
        double realHeightOfNewViewAsPerceivedByOldView = panel.getSize().height / zoomFactor;

        zoom(
            toModelEnvelope(
                centreOfNewViewAsPerceivedByOldView.getX()
                    - (0.5 * realWidthOfNewViewAsPerceivedByOldView),
                centreOfNewViewAsPerceivedByOldView.getX()
                    + (0.5 * realWidthOfNewViewAsPerceivedByOldView),
                centreOfNewViewAsPerceivedByOldView.getY()
                    - (0.5 * realHeightOfNewViewAsPerceivedByOldView),
                centreOfNewViewAsPerceivedByOldView.getY()
                    + (0.5 * realHeightOfNewViewAsPerceivedByOldView)));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toModelPoint(java.awt.geom.Point2D)
	 */
    @Override
	public Point2D toModelPoint(Point2D viewPoint) throws NoninvertibleTransformException {
        return getModelToViewTransform().inverseTransform(toPoint2DDouble(viewPoint), null);
    }

    private Point2D.Double toPoint2DDouble(Point2D p) {
        //If you pass a non-Double Point2D to an AffineTransform, the AffineTransform
        //will be done using floats instead of doubles. [Jon Aquino]
        if (p instanceof Point2D.Double) {
            return (Point2D.Double) p;
        }
        return new Point2D.Double(p.getX(), p.getY());
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toModelCoordinate(java.awt.geom.Point2D)
	 */
    @Override
	public Coordinate toModelCoordinate(Point2D viewPoint) throws NoninvertibleTransformException {
        return CoordUtil.toCoordinate(toModelPoint(viewPoint));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toViewPoint(java.awt.geom.Point2D)
	 */
    @Override
	public Point2D toViewPoint(Point2D modelPoint) throws NoninvertibleTransformException {
        return getModelToViewTransform().transform(toPoint2DDouble(modelPoint), null);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toViewPoint(com.vividsolutions.jts.geom.Coordinate)
	 */
    @Override
	public Point2D toViewPoint(Coordinate modelCoordinate) throws NoninvertibleTransformException {
        return toViewPoint(new Point2D.Double(modelCoordinate.x, modelCoordinate.y));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toModelEnvelope(double, double, double, double)
	 */
    @Override
	public Envelope toModelEnvelope(double x1, double x2, double y1, double y2)
        throws NoninvertibleTransformException {
        Coordinate c1 = toModelCoordinate(new Point2D.Double(x1, y1));
        Coordinate c2 = toModelCoordinate(new Point2D.Double(x2, y2));

        return new Envelope(c1, c2);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getModelToViewTransform()
	 */
    @Override
	public AffineTransform getModelToViewTransform() throws NoninvertibleTransformException {
        if (modelToViewTransform == null) {
            update();
        }

        return modelToViewTransform;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#setModelToViewTransform(java.awt.geom.AffineTransform)
	 */
    @Override
	public void setModelToViewTransform(AffineTransform model)  {
        modelToViewTransform = model;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getEnvelopeInModelCoordinates()
	 */
    @Override
	public Envelope getEnvelopeInModelCoordinates() {
        double widthAsPerceivedByModel = panel.getWidth() / scale;
        double heightAsPerceivedByModel = panel.getHeight() / scale;

        return new Envelope(
            viewOriginAsPerceivedByModel.getX(),
            viewOriginAsPerceivedByModel.getX() + widthAsPerceivedByModel,
            viewOriginAsPerceivedByModel.getY(),
            viewOriginAsPerceivedByModel.getY() + heightAsPerceivedByModel);
    }

    //<<TODO:IMPROVE>> Currently the zoomed image is aligned west in the viewport.
    //It should be centred. [Jon Aquino]
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#zoom(com.vividsolutions.jts.geom.Envelope)
	 */
    @Override
	public void zoom(Envelope modelEnvelope) throws NoninvertibleTransformException {
    	zoom(modelEnvelope, true);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#zoom(com.vividsolutions.jts.geom.Envelope, boolean)
	 */
    @Override
	public void zoom(Envelope modelEnvelope, boolean fireZoomChanged) throws NoninvertibleTransformException {
        if (modelEnvelope.isNull()) {
            return;
        }

        if (!zoomHistory.hasNext() && !zoomHistory.hasPrev()) {
            //When the first extent is added, first add the existing extent.
            //Must do this late because it's hard to tell when the panel is realized.
            //[Jon Aquino]
            zoomHistory.add(getEnvelopeInModelCoordinates());
        }

        setScale(
            Math.min(
                panel.getWidth() / modelEnvelope.getWidth(),
                panel.getHeight() / modelEnvelope.getHeight()));
        double xCenteringOffset = ((panel.getWidth() / scale) - modelEnvelope.getWidth()) / 2d;
        double yCenteringOffset = ((panel.getHeight() / scale) - modelEnvelope.getHeight()) / 2d;
        viewOriginAsPerceivedByModel =
            new Point2D.Double(modelEnvelope.getMinX() - xCenteringOffset, modelEnvelope.getMinY() - yCenteringOffset);
        update(fireZoomChanged);
        zoomHistory.add(modelEnvelope);
        if (fireZoomChanged)
        	fireZoomChanged(modelEnvelope);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#setScale(double)
	 */
    @Override
	public void setScale(double scale) {
        this.scale = scale;
    }

    private void fireZoomChanged(Envelope modelEnvelope) {
        try {
			for (Iterator i = listeners.iterator(); i.hasNext();) {
			    ViewportListener l = (ViewportListener) i.next();
			    l.zoomChanged(modelEnvelope);
			}
		} catch (Exception e) {
			logger.warn("Posible concurrencia (ConcurrentModificationExeption en el zoom");
		}
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#zoomToFullExtent()
	 */
    @Override
	public void zoomToFullExtent() throws NoninvertibleTransformException {
        zoom(fullExtent());
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#fullExtent()
	 */
    @Override
	public Envelope fullExtent() {
        Envelope envelope = new Envelope();
    	try{

	    	HashMap<Integer, Geometry> geometriasMunicipios=new HashMap();
	    	String sridDefecto=null;
	    	Geometry geom1=null;
	    	
	        for (Iterator i = panel.getLayerManager().getLayers().iterator();
	        		i.hasNext();)
			{
				Layer layer = (Layer) i.next();
				Envelope envelope2 = null;
//				if (layer.getEnvelope() == null || layer.getEnvelope().getHeight() == 0){/*Cambio con respecto al Core*/
				if(EnvelopeUtil.bufferByFraction(layer.getEnvelope(), 0.03)==null || EnvelopeUtil.bufferByFraction(layer.getEnvelope(), 0.03).getHeight()==0 ){
				    IGeopistaConnection geopistaConnection = (IGeopistaConnection) layer.getDataSourceQuery().getDataSource().getConnection();

				    //Miro en qué srid por defecto se almacenan las features en la base de datos
				    //Solo lo pedimos una vez
				    if (sridDefecto==null)
				    	sridDefecto = geopistaConnection.getSRIDDefecto(true, -1);

			    	CoordinateSystem outCoord = layer.getLayerManager().getCoordinateSystem();
			    	CoordinateSystem inCoord = PredefinedCoordinateSystems.getCoordinateSystem(Integer.parseInt(sridDefecto));

			    	List listaMunicipios = AppContext.getAlMunicipios();
			    	int n = listaMunicipios.size();
			    	Geometry geom = null;

			    	
			        for (int j=0;j<n;j++){
			        	Municipio municipio = (Municipio)listaMunicipios.get(j);
			        	
			        	geom1=geometriasMunicipios.get(municipio.getId());
			        	if (geom1==null)
			        		geom1 =  geopistaConnection.obtenerGeometriaMunicipio(municipio.getId());
			        	else
			        		geometriasMunicipios.put(municipio.getId(), geom1);
			        	
				        geom1.setSRID(Integer.parseInt(sridDefecto));
				        logger.info("Reproyectando capa"+layer.getName());
		            	Reprojector.instance().reproject(geom1,inCoord, outCoord);
			        	if (geom != null)
			        		geom = geom.union(geom1);
			        	else
			        		geom = geom1;
			        }
				    geom.setSRID(outCoord.getEPSGCode());
				    envelope2 = geom.getEnvelopeInternal();
				}else
					envelope2 = layer.getEnvelope();
				if (envelope2!=null)
					envelope.expandToInclude(envelope2);
		    }
    	}
    	catch (ConcurrentModificationException e1){
    		logger.warn("Posible error de concurrencia al hacer el zoom de la capa");
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
        return envelope;
//        return EnvelopeUtil.bufferByFraction(panel.getLayerManager().getEnvelopeOfAllLayers(), 0.03);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#zoomToViewPoint(java.awt.geom.Point2D, double)
	 */
    @Override
	public void zoomToViewPoint(Point2D centreOfNewViewAsPerceivedByOldView, double zoomFactor)
        throws NoninvertibleTransformException {
        double widthOfNewViewAsPerceivedByOldView = panel.getWidth() / zoomFactor;
        double heightOfNewViewAsPerceivedByOldView = panel.getHeight() / zoomFactor;
        zoom(
            centreOfNewViewAsPerceivedByOldView,
            widthOfNewViewAsPerceivedByOldView,
            heightOfNewViewAsPerceivedByOldView);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toViewPoints(java.util.Collection)
	 */
    @Override
	public Collection toViewPoints(Collection modelCoordinates)
        throws NoninvertibleTransformException {
        ArrayList viewPoints = new ArrayList();
        for (Iterator i = modelCoordinates.iterator(); i.hasNext();) {
            Coordinate modelCoordinate = (Coordinate) i.next();
            viewPoints.add(toViewPoint(modelCoordinate));
        }
        return viewPoints;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#toViewRectangle(com.vividsolutions.jts.geom.Envelope)
	 */
    @Override
	public Rectangle2D toViewRectangle(Envelope envelope) throws NoninvertibleTransformException {
        Point2D p1 = toViewPoint(new Coordinate(envelope.getMinX(), envelope.getMinY()));
        Point2D p2 = toViewPoint(new Coordinate(envelope.getMaxX(), envelope.getMaxY()));
        return new Rectangle2D.Double(
            Math.min(p1.getX(), p2.getX()),
            Math.min(p1.getY(), p2.getY()),
            Math.abs(p1.getX() - p2.getX()),
            Math.abs(p1.getY() - p2.getY()));
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getNormalizedScaleForPPM(double, double)
	 */
    @Override
	public double getNormalizedScaleForPPM(double scale, double ppm) {
    	double normalizedScale = 1/(scale*ppm) ;  //0.375 / 1280) ; //px/m
		return normalizedScale;
	}

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getNormalizedScaleForWidth(double, double)
	 */
    @Override
	public double getNormalizedScaleForWidth(double scale, double widthMeters) {
    	double ppm = this.getPanel().getWidth() / widthMeters;
    	return getNormalizedScaleForPPM(scale, ppm);
	}

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.IViewport#getScaleForNSnPPM(double, double)
	 */
	@Override
	public double getScaleForNSnPPM(double normScale, double ppm) {
		double scale = ppm/normScale ;
		return scale;
	}

}
