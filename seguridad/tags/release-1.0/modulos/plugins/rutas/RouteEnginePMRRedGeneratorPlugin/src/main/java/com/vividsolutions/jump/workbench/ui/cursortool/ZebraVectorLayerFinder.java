
package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.RouteArrowLineStyle;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.AbstractVectorLayerFinder;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

public class ZebraVectorLayerFinder extends AbstractVectorLayerFinder {

    private final static double SMALL_ANGLE = 10;
    private final static double SMALL_LENGTH = 10;
	private LayerManagerProxy layerManagerProxy;
	private LayerViewPanel layerViewPanel;
    public static final Color COLOR = Color.ORANGE;
    private static final int lineWidth = 4;
    
    public ZebraVectorLayerFinder(LayerManagerProxy layerManagerProxy, LayerViewPanel layerViewPanel) {
        super("Pasos de Cebra", layerManagerProxy, COLOR);
        this.layerManagerProxy = layerManagerProxy;
        this.layerViewPanel = layerViewPanel;
    }    

    protected void applyStyles(Layer layer) {
    	layer.addStyle(new Line(layerViewPanel.getViewport(),
				(Graphics2D) layerViewPanel.getGraphics()));
    	layer.getBasicStyle().setRenderingLinePattern(true);
        layer.getBasicStyle().setRenderingFill(true);
        layer.getBasicStyle().setLineColor(COLOR);
        layer.getBasicStyle().setFillColor(COLOR);
        layer.getBasicStyle().setLineWidth(4);
        layer.setDrawingLast(true);
    }
    
    public Layer createLayer() {
        FeatureSchema schema = new FeatureSchema();
        schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);

        FeatureDataset dataset = new FeatureDataset(schema);
        Layer layer =
            new GeopistaLayer(
                this.getLayerName(),
                COLOR,
                dataset,
                this.layerManagerProxy.getLayerManager()) {
            public boolean isFeatureCollectionModified() {
                    //Prevent save prompt. [Jon Aquino]
            	return false;
            }
        };   
        ((GeopistaLayer) layer).setLocal(true);

        boolean firingEvents = layerManagerProxy.getLayerManager().isFiringEvents();
        //Can't fire events because this Layerable hasn't been added to the
        //LayerManager yet. [Jon Aquino]
        layerManagerProxy.getLayerManager().setFiringEvents(false);

        try {
            applyStyles(layer);
        } finally {
            layerManagerProxy.getLayerManager().setFiringEvents(firingEvents);
        }

        layerManagerProxy.getLayerManager().addLayer(StandardCategoryNames.SYSTEM, layer);

        return layer;
    }
    
    public static class Line extends RouteArrowLineStyle {
        public Line(Viewport viewport, Graphics2D graphics2d) {
            super("Line", IconLoader.icon("linecapbutt.JPG"), 30D, 15D, viewport, graphics2d);
        }
        public void paint(Feature feature,Graphics2D g,Viewport viewport){
        	try{
        	LineString line = (LineString) feature.getGeometry();

            g.setColor(COLOR);
            g.setStroke(new BasicStroke(lineWidth));
            GeneralPath directArrowHead = (GeneralPath)viewport.getJava2DConverter().toShape(line);
            g.draw(directArrowHead);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
    }
}
