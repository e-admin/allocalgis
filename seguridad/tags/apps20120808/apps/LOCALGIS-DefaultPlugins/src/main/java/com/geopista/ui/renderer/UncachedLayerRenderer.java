/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
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
 * 
 * www.geopista.com
 * 
 * Created on 15-jun-2004 by juacas
 *
 * 
 */
package com.geopista.ui.renderer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

/**
 * @author juacas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UncachedLayerRenderer implements Renderer
{
	 private Layer layer;
	 private LayerViewPanel panel;
	 private SimpleFeatureCollectionRenderer simpleFeatureCollectionRenderer;
	 private Renderer currentFeatureCollectionRenderer;

	/**
	 * @param layer
	 * @param panel
	 */
	public UncachedLayerRenderer(Layer layer, LayerViewPanel panel) {
	
		this.layer = layer;
	    this.panel = panel;
		simpleFeatureCollectionRenderer = new SimpleFeatureCollectionRenderer(layer, panel);
		currentFeatureCollectionRenderer = simpleFeatureCollectionRenderer;
		
		  Map layerToFeaturesMap = layerToFeaturesMap();
	        Collection styles = styles();
	        simpleFeatureCollectionRenderer.setLayerToFeaturesMap(layerToFeaturesMap);
	        simpleFeatureCollectionRenderer.setStyles(styles);
	        currentFeatureCollectionRenderer=simpleFeatureCollectionRenderer;
	}
	 public Runnable createRunnable() {
      
        return currentFeatureCollectionRenderer.createRunnable();
    }
	 protected Collection styles() {
        //new ArrayList to avoid ConcurrentModificationExceptions. [Jon Aquino]
        ArrayList styles = new ArrayList(layer.getStyles());
        styles.remove(layer.getVertexStyle());
        styles.remove(layer.getLabelStyle());

        //Move to last. [Jon Aquino]
        styles.add(layer.getVertexStyle());
        styles.add(layer.getLabelStyle());

        return styles;
    }

    protected Map layerToFeaturesMap() {
        Envelope viewportEnvelope = panel.getViewport()
                                         .getEnvelopeInModelCoordinates();

        return Collections.singletonMap(layer,
            layer.getFeatureCollectionWrapper().query(viewportEnvelope));
    }
    public void clearImageCache() {
       
        simpleFeatureCollectionRenderer.clearImageCache();
    }

    public boolean isRendering() {
        return currentFeatureCollectionRenderer.isRendering();
    }

    public Object getContentID() {
        return currentFeatureCollectionRenderer.getContentID();
    }

    public void copyTo(Graphics2D graphics) {
        currentFeatureCollectionRenderer.copyTo(graphics);
    }
    public void cancel() {
      
        simpleFeatureCollectionRenderer.cancel();
    }
}
