/**
 * ReportUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRImage;

import org.agil.core.jump.coverage.CoverageLayer;
import org.agil.core.jump.coverage.CoverageLayerRenderer;

import com.geopista.ui.renderer.UncachedLayerRenderer;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.FeatureSelection;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.renderer.FeatureSelectionRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;
import com.vividsolutions.jump.workbench.ui.renderer.WMSLayerRenderer;

public class ReportUtils {

	public static Image printMap(int x, int y, LayerViewPanel layerViewPanel) {
		int w = layerViewPanel.getWidth();
		int h = layerViewPanel.getHeight();
//        Envelope newenvelope = getPreferredPrintEnvelope(x, y, layerViewPanel);
        BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
//       Envelope oldenvelope= layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
     
        BufferedImage image2 = new BufferedImage(layerViewPanel.getWidth(),
                layerViewPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        layerViewPanel.paintComponent((Graphics2D) image2.getGraphics());
	
        // escala llenando todo el thumbnail recortando si es necesario
        double scalex=((double)w)/x;
        double scaley=((double)h)/y;
        double scale=Math.min(scalex,scaley);
        int newW = (int)(w/scale);
		int newH = (int)(h/scale);
		Image scaledImage=image2.getScaledInstance(newW,newH,BufferedImage.SCALE_SMOOTH);
        int newX = (int)(x/2-w/scale/2);
		int newY = (int)( y/2-h/scale/2);
		graphics.drawImage(scaledImage,newX,newY,newW,newH,Color.WHITE,null);
        //print(graphics, layerViewPanel.getLayerManager().getVisibleLayerables(Layerable.class, false), newenvelope, x, y);
		
        return image;
    }

	public static Image printMap(LayerViewPanel layerViewPanel, JRImage jrImage, Envelope printEnvelope) {
		int imageHeight = jrImage.getHeight();
		int imageWidth = jrImage.getWidth();
		
		try {
			layerViewPanel.getViewport().zoom(printEnvelope);
		} catch (NoninvertibleTransformException e1) {
			// Nada
		}
		
		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) resultImage.getGraphics();
		
		LayerManager layerManager = (LayerManager) layerViewPanel.getLayerManager();
		
		final Throwable[] throwable = new Throwable[] { null };
		LayerViewPanel printSizeLayerViewPanel = new LayerViewPanel(layerManager,
				new LayerViewPanelContext(){
            public void setStatusMessage(String message){
            }
            public void warnUser(String warning){
            }
            public void handleThrowable(Throwable t){
                throwable[0] = t;
            }
        });
		
		printSizeLayerViewPanel.setSize(imageWidth, imageHeight);
		try {
			printSizeLayerViewPanel.getViewport().zoom(printEnvelope);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		paintBackground(graphics, imageWidth, imageHeight);

		ArrayList layersReversed = new ArrayList(layerManager.getVisibleLayers(true));
	    Collections.reverse(layersReversed);

	    for (Iterator i = layersReversed.iterator(); i.hasNext();){
	    	Layerable layer = (Layerable) i.next();
	    	Renderer renderer = null;
	    	if (layer instanceof Layer){
	    		renderer = new UncachedLayerRenderer((Layer) layer,
	    				printSizeLayerViewPanel);
	    	}
	    	else if (layer instanceof CoverageLayer){
	    		renderer = new CoverageLayerRenderer((CoverageLayer) layer,
	    				printSizeLayerViewPanel);
	    	}
	    	else if (layer instanceof WMSLayer){
	    		renderer = new WMSLayerRenderer((WMSLayer) layer,
	    				printSizeLayerViewPanel);
	    	}

	    	// Wait for rendering to complete rather than running it in a
	    	// separate thread. [Jon Aquino]
	    	Runnable runnable = renderer.createRunnable();
	    	if (runnable != null){
	    		runnable.run();
	    	}

	    	// I hope no ImageObserver is needed. Set to null. [Jon Aquino]
	    	// renderer.copyTo(graphics);
	    	//printSizeLayerViewPanel.getRenderingManager().copyTo(graphics);
	    	
	    	MediaTracker mediaTracker = new MediaTracker(printSizeLayerViewPanel);
	    	renderer.copyTo(graphics);
	    	try {
				mediaTracker.waitForAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // Copiamos la seleccion	    
	    Collection layersWithSelection = layerViewPanel.getSelectionManager().getLayersWithSelectedItems();
	    Iterator layersIterator = layersWithSelection.iterator();
	    FeatureSelection featureSelection = printSizeLayerViewPanel.getSelectionManager().getFeatureSelection();
	    featureSelection.unselectItems();
	    while (layersIterator.hasNext()){
	    	Layer layer = (Layer) layersIterator.next();
	    	Collection featuresWithSelectedItems = 
	    		layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems(layer);
	    	
	    	Iterator featuresIterator = featuresWithSelectedItems.iterator();
	    	while (featuresIterator.hasNext()){
	    		Feature feature = (Feature) featuresIterator.next();	    		
	    		featureSelection.selectItems(layer, feature);
	    	}
	    }
	    
	    Renderer selectionRenderer = new FeatureSelectionRenderer(printSizeLayerViewPanel);	    
    	printSizeLayerViewPanel.getRenderingManager().copyTo(graphics);
	    
	    // Impresion de escala
	    Rectangle2D scaleRectangle = new Rectangle2D.Double(
	    		0, new Integer(imageHeight-20).doubleValue(),
	    		new Integer(imageWidth).doubleValue(), new Integer(20).doubleValue());
	    graphics.setColor(Color.white);
	    graphics.fill(scaleRectangle);
	    Line2D scaleBorderLine = new Line2D.Double(
	    		0, new Integer(imageHeight-20).doubleValue(),
	    		new Integer(imageWidth).doubleValue(), new Integer(imageHeight-20).doubleValue());
	    graphics.setColor(Color.black);
	    graphics.draw(scaleBorderLine);
	    Font scaleFont = new Font("Dialog", Font.PLAIN, 10);
	    //String scaleText = getReportPrintScale(jrImage, printEnvelope);
	    String scaleText = ReportUtils.getReportViewerScale(layerViewPanel);
	    TextLayout scaleTextLayout = new TextLayout(scaleText, scaleFont, graphics.getFontRenderContext());
	    scaleTextLayout.draw(graphics, 5, imageHeight - 5);

	    return resultImage;
    }
	
	// Nuevo
	public static Image printMap(LayerViewPanel layerViewPanel, int imageHeight, int imageWidth, Envelope printEnvelope) {
		
		try {
			layerViewPanel.getViewport().zoom(printEnvelope);
		} catch (NoninvertibleTransformException e1) {
			// Nada
		}
		
		BufferedImage resultImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) resultImage.getGraphics();
		
		LayerManager layerManager = (LayerManager) layerViewPanel.getLayerManager();
		
		final Throwable[] throwable = new Throwable[] { null };
		LayerViewPanel printSizeLayerViewPanel = new LayerViewPanel(layerManager,
				new LayerViewPanelContext(){
            public void setStatusMessage(String message){
            }
            public void warnUser(String warning){
            }
            public void handleThrowable(Throwable t){
                throwable[0] = t;
            }
        });
		
		printSizeLayerViewPanel.setSize(imageWidth, imageHeight);
		try {
			printSizeLayerViewPanel.getViewport().zoom(printEnvelope);
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		paintBackground(graphics, imageWidth, imageHeight);

		ArrayList layersReversed = new ArrayList(layerManager.getVisibleLayers(true));
	    Collections.reverse(layersReversed);

	    for (Iterator i = layersReversed.iterator(); i.hasNext();){
	    	Layerable layer = (Layerable) i.next();
	    	Renderer renderer = null;
	    	if (layer instanceof Layer){
	    		renderer = new UncachedLayerRenderer((Layer) layer,
	    				printSizeLayerViewPanel);
	    	}
	    	else if (layer instanceof CoverageLayer){
	    		renderer = new CoverageLayerRenderer((CoverageLayer) layer,
	    				printSizeLayerViewPanel);
	    	}
	    	else if (layer instanceof WMSLayer){
	    		renderer = new WMSLayerRenderer((WMSLayer) layer,
	    				printSizeLayerViewPanel);
	    	}

	    	// Wait for rendering to complete rather than running it in a
	    	// separate thread. [Jon Aquino]
	    	Runnable runnable = renderer.createRunnable();
	    	if (runnable != null){
	    		runnable.run();
	    	}

	    	// I hope no ImageObserver is needed. Set to null. [Jon Aquino]
	    	// renderer.copyTo(graphics);
	    	//printSizeLayerViewPanel.getRenderingManager().copyTo(graphics);
	    	
	    	MediaTracker mediaTracker = new MediaTracker(printSizeLayerViewPanel);
	    	renderer.copyTo(graphics);
	    	try {
				mediaTracker.waitForAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // Copiamos la seleccion	    
	    Collection layersWithSelection = layerViewPanel.getSelectionManager().getLayersWithSelectedItems();
	    Iterator layersIterator = layersWithSelection.iterator();
	    FeatureSelection featureSelection = printSizeLayerViewPanel.getSelectionManager().getFeatureSelection();
	    featureSelection.unselectItems();
	    while (layersIterator.hasNext()){
	    	Layer layer = (Layer) layersIterator.next();
	    	Collection featuresWithSelectedItems = 
	    		layerViewPanel.getSelectionManager().getFeaturesWithSelectedItems(layer);
	    	
	    	Iterator featuresIterator = featuresWithSelectedItems.iterator();
	    	while (featuresIterator.hasNext()){
	    		Feature feature = (Feature) featuresIterator.next();	    		
	    		featureSelection.selectItems(layer, feature);
	    	}
	    }
	    
	    Renderer selectionRenderer = new FeatureSelectionRenderer(printSizeLayerViewPanel);	    
    	printSizeLayerViewPanel.getRenderingManager().copyTo(graphics);
	    
	    // Impresion de escala
	    Rectangle2D scaleRectangle = new Rectangle2D.Double(
	    		0, new Integer(imageHeight-20).doubleValue(),
	    		new Integer(imageWidth).doubleValue(), new Integer(20).doubleValue());
	    graphics.setColor(Color.white);
	    graphics.fill(scaleRectangle);
	    Line2D scaleBorderLine = new Line2D.Double(
	    		0, new Integer(imageHeight-20).doubleValue(),
	    		new Integer(imageWidth).doubleValue(), new Integer(imageHeight-20).doubleValue());
	    graphics.setColor(Color.black);
	    graphics.draw(scaleBorderLine);
	    Font scaleFont = new Font("Dialog", Font.PLAIN, 10);
	    //String scaleText = getReportPrintScale(jrImage, printEnvelope);
	    String scaleText = ReportUtils.getReportViewerScale(layerViewPanel);
	    TextLayout scaleTextLayout = new TextLayout(scaleText, scaleFont, graphics.getFontRenderContext());
	    scaleTextLayout.draw(graphics, 5, imageHeight - 5);

	    return resultImage;
    }
	// Borrar
	
    private static void paintBackground(Graphics2D graphics, int extentW, int extentH) {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, extentW, extentH);
    }

	static String getReportViewerScale(LayerViewPanel layerViewPanel){
		int panelWidth = layerViewPanel.getWidth();
		Viewport viewPort = layerViewPanel.getViewport();
		double modelWidth = viewPort.getEnvelopeInModelCoordinates().getWidth();
		// Calculo la escala horizontal en centimetros
		//double horizontalScale = modelWidth * 100 / (INCH_TO_CM / SCREENRES * panelWidth);
		double horizontalScale = 1.0;
	    
		String viewerScale = "Escala 1:" + new Double(horizontalScale).intValue();
		return viewerScale;
	}
}
