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

package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.agil.core.jump.coverage.CoverageLayer;
import org.agil.core.jump.coverage.CoverageLayerRenderer;

import pirolPlugIns.utilities.RasterImageSupport.IRasterImageLayer;
import pirolPlugIns.utilities.RasterImageSupport.RasterImageRenderer;

import com.geopista.model.DynamicLayer;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.OrderedMap;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 *  Es la clase encargada de dibujar. Mantiene referencias a: Un panel de
 *  dibujo (LayerViewPanel) Una cola de threads. Cada thread dibuja una capa
 *  de cartografia. Solo se puede dibujar 1 a la vez Un temporizador encargado
 *  de repintar cada segundo (TODO EL CUELLO DE BOTELLA PUEDE ESTAR AQUI)
 *
 *@author    Chris Seguin
 */
public class RenderingManager implements IRenderingManager {
    private LayerViewPanel panel;
    
    
	private int maxFeatures=100; //this variable will be used for 
	 //LayerRenderer.class which extends
	 //FeatureCollectionRenderer.class
	 //default in FeatureCollectionRenderer is 100 features.

private Map contentIDToRendererMap = new OrderedMap();
    private OrderedMap contentIDToLowRendererFactoryMap = new OrderedMap();
    private OrderedMap contentIDToHighRendererFactoryMap = new OrderedMap();

    //There's no performance advantage to rendering dozens of non-WMS layers in parallel.
    //In fact, it will make the GUI less responsive. [Jon Aquino]
    private ThreadQueue defaultRendererThreadQueue = new ThreadQueue(1);

    //WMS processing is done on the server side, so allow WMS queries to be done
    //in parallel. But not too many, as each Thread consumes 1 MB of memory
    //(see http://mindprod.com/jglossthread.html). The Threads may pile up if
    //the server is down. [Jon Aquino]
    private ThreadQueue wmsRendererThreadQueue = new ThreadQueue(20);

    //250 ms wasn't as good as 1 s because less got painted on each repaint,
    //making rendering appear to be slower. [Jon Aquino]
    private Timer repaintTimer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            for (Iterator i = contentIDToRendererMap.values().iterator();
                i.hasNext();
                ) {
                Renderer renderer = (Renderer) i.next();
                if (renderer.isRendering()) {
                    repaintPanel();
                    return;
                }
            }

            repaintTimer.stop();
            repaintPanel();
        }
    });
    private boolean paintingEnabled = true;

    public RenderingManager(final LayerViewPanel panel) {
        this.panel = panel;
        repaintTimer.setCoalesce(true);
        putAboveLayerables(
            SelectionBackgroundRenderer.CONTENT_ID,
            new Renderer.Factory() {
            public Renderer create() {
                return new SelectionBackgroundRenderer(panel);
            }
        });
        putAboveLayerables(
            FeatureSelectionRenderer.CONTENT_ID,
            new Renderer.Factory() {
            public Renderer create() {
                return new FeatureSelectionRenderer(panel);
            }
        });
        putAboveLayerables(
            LineStringSelectionRenderer.CONTENT_ID,
            new Renderer.Factory() {
            public Renderer create() {
                return new LineStringSelectionRenderer(panel);
            }
        });
        putAboveLayerables(
            PartSelectionRenderer.CONTENT_ID,
            new Renderer.Factory() {
            public Renderer create() {
                return new PartSelectionRenderer(panel);
            }
        });
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#putBelowLayerables(java.lang.Object, com.vividsolutions.jump.workbench.ui.renderer.Renderer.Factory)
	 */
    @Override
	public void putBelowLayerables(
        Object contentID,
        Renderer.Factory factory) {
        contentIDToLowRendererFactoryMap.put(contentID, factory);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#putAboveLayerables(java.lang.Object, com.vividsolutions.jump.workbench.ui.renderer.Renderer.Factory)
	 */
    @Override
	public void putAboveLayerables(
        Object contentID,
        Renderer.Factory factory) {
        contentIDToHighRendererFactoryMap.put(contentID, factory);
    }
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#renderAll()
	 */
    @Override
	public void renderAll() {
    renderAll(false);
    }
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#renderAll(boolean)
	 */
    @Override
	public void renderAll(boolean clearImageCache) {
        defaultRendererThreadQueue.clear();
        wmsRendererThreadQueue.clear();

        for (Iterator i = contentIDs().iterator(); i.hasNext();) {
            Object contentID = i.next();
            render(contentID,clearImageCache);
        }
    }

    protected List contentIDs() {
        ArrayList contentIDs = new ArrayList();
        contentIDs.addAll(contentIDToLowRendererFactoryMap.keyList());
        for (Iterator i =
            panel.getLayerManager().reverseIterator(Layerable.class);
            i.hasNext();
            ) {
            Layerable layerable = (Layerable) i.next();
            contentIDs.add(layerable);
        }

        contentIDs.addAll(contentIDToHighRendererFactoryMap.keyList());

        return contentIDs;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#getRenderer(java.lang.Object)
	 */
    @Override
	public Renderer getRenderer(Object contentID) {
    	if (contentIDToRendererMap == null)
    		return null;
        return (Renderer) contentIDToRendererMap.get(contentID);
    }

    private void setRenderer(Object contentID, Renderer renderer) {
        contentIDToRendererMap.put(contentID, renderer);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#render(java.lang.Object)
	 */
    @Override
	public void render(Object contentID) {
        render(contentID, false);
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#render(java.lang.Object, boolean)
	 */
    @Override
	public void render(Object contentID, boolean clearImageCache) {         
    	
        if (getRenderer(contentID) == null) {
            setRenderer(contentID, createRenderer(contentID));
        }

        if (getRenderer(contentID).isRendering()) {
            getRenderer(contentID).cancel();

            //It might not cancel immediately, so create a new Renderer [Jon Aquino]
            setRenderer(contentID, createRenderer(contentID));
        }

        if (clearImageCache) {
            getRenderer(contentID).clearImageCache();
        }
        Runnable runnable = getRenderer(contentID).createRunnable();
        if (runnable != null) {
            //Before I would create threads that did nothing. Now I never do 
            //that -- I just return null. A dozen threads that do nothing make the 
            //system sluggish. [Jon Aquino]
            (
                (contentID instanceof WMSLayer)
                    ? wmsRendererThreadQueue
                    : defaultRendererThreadQueue).add(
                runnable);
        }

        if (!repaintTimer.isRunning()) {
            repaintPanel();
            repaintTimer.start();
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#repaintPanel()
	 */
    @Override
	public void repaintPanel() {
        if (!paintingEnabled) {
            return;
        }

        panel.superRepaint();
    }
//  [sstein: 20.01.2006]
	// Start: added by Ole
	// everything is static to make it useable before a LayerManager instance
	// (containing a RenderingManager) is created
	// which is the case, at the time the PlugIns are initialized and to have one map
	// for all RenderingManager
	public static Renderer.ContentDependendFactory getRenderFactoryForLayerable(Class clss){
	    if (layerableClassToRendererFactoryMap.containsKey(clss)){
	        return (Renderer.ContentDependendFactory)layerableClassToRendererFactoryMap.get(clss); 
	        }
	    return null;
	  }	
	public static void putRendererForLayerable(Class clss,
	  							Renderer.ContentDependendFactory rendererFactory) {
		if (!layerableClassToRendererFactoryMap.containsKey(clss)) {
			layerableClassToRendererFactoryMap.put(clss, rendererFactory);
		}
	}
	// End: added by Ole*
	//[sstein: 20.01.2006]	added for Ole
	protected static HashMap layerableClassToRendererFactoryMap = new HashMap();

    protected Renderer createRenderer(Object contentID) {
        if (contentID instanceof DynamicLayer) {
        	return new DynamicLayerRenderer((DynamicLayer) contentID, panel);
        }
        if (contentID instanceof Layer) {
            return new LayerRenderer((Layer) contentID, panel);
        }

        if (contentID instanceof WMSLayer) {
            return new WMSLayerRenderer((WMSLayer) contentID, panel);
        }
        //[sstein: 20.01.2006] Start: added by Ole
	    if(RenderingManager.getRenderFactoryForLayerable(contentID.getClass())!=null){
	          return RenderingManager.getRenderFactoryForLayerable(contentID.getClass()).create(contentID); }
        //End: added by Ole*
        if (contentIDToLowRendererFactoryMap.containsKey(contentID)) {
            return (
                (
                    Renderer
                        .Factory) contentIDToLowRendererFactoryMap
                        .get(
                    contentID))
                .create();
        }
        if (contentIDToHighRendererFactoryMap.containsKey(contentID)) {
            return (
                (
                    Renderer
                        .Factory) contentIDToHighRendererFactoryMap
                        .get(
                    contentID))
                .create();
        }
		if (contentID instanceof CoverageLayer) {
			return new CoverageLayerRenderer((CoverageLayer) contentID, panel);
		}
		if (contentID instanceof IRasterImageLayer) {
			return new RasterImageRenderer((IRasterImageLayer )contentID, panel);
		}
        Assert.shouldNeverReachHere();
        return null;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#setPaintingEnabled(boolean)
	 */
    @Override
	public void setPaintingEnabled(boolean paintingEnabled) {
        this.paintingEnabled = paintingEnabled;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#copyTo(java.awt.Graphics2D)
	 */
    @Override
	public void copyTo(Graphics2D destination) {
        for (Iterator i = contentIDs().iterator(); i.hasNext();) {
            Object contentID = i.next();

            if (getRenderer(contentID) != null) {
                getRenderer(contentID).copyTo(destination);
            } 
        }
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#getDefaultRendererThreadQueue()
	 */
    @Override
	public ThreadQueue getDefaultRendererThreadQueue() {
        return defaultRendererThreadQueue;
    }

    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#dispose()
	 */
    @Override
	public void dispose() {
        repaintTimer.stop();
        defaultRendererThreadQueue.dispose();
        wmsRendererThreadQueue.dispose();
        //The ThreadSafeImage cached in each Renderer consumes 1 MB of memory,
        //according to OptimizeIt [Jon Aquino]
       destroy();
    }
    /* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.renderer.IRenderingManager#destroy()
	 */
    @Override
	public void destroy()
    {
        if (contentIDToRendererMap!=null)
        {        	       
            for (Iterator it=contentIDToRendererMap.values().iterator();it.hasNext(); )
            {
                try
                {
                	Object obj=it.next();
                	//System.out.println("Clase a destruir:"+obj.getClass().getName());
                	if (obj instanceof LayerRenderer) {
                    	((LayerRenderer)obj).destroy();											
					}
                	
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            contentIDToRendererMap.clear();
        }
        if (contentIDToHighRendererFactoryMap!=null)
            contentIDToHighRendererFactoryMap.clear();
        if (contentIDToLowRendererFactoryMap!=null)
            contentIDToLowRendererFactoryMap.clear();
        contentIDToRendererMap=null;
        contentIDToHighRendererFactoryMap=null;
        contentIDToLowRendererFactoryMap=null;

    }
}
