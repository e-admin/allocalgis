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

/**
 * 
 */
package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;

import com.geopista.model.DynamicLayer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 * En el caso de tener un DynamicLayer se decidirá en función del parámetro isDinamica que ha seleccionado el usuario, 
 * si se debe implementar un LayerRenderer o un DynamicRenderer.
 * @author miriamperez
 *
 */

public class DynamicLayerRenderer implements Renderer{
	private WMSLayerRenderer wmsLayerRenderer = null;
	private LayerRenderer layerRenderer = null;
	private DynamicLayer layer;
	private LayerViewPanel panel;
    private Object contentID;
	
    public DynamicLayerRenderer(Object contentID, LayerViewPanel panel) {
        this.contentID = contentID;
        this.panel = panel;
    }

    public DynamicLayerRenderer(DynamicLayer layer, LayerViewPanel panel) {
    	this.layer = layer;
		wmsLayerRenderer = new WMSLayerRenderer(layer, panel);
		layerRenderer = new LayerRenderer(layer, panel);
	}
    
		
	public void copyTo(Graphics2D graphics) {
    	wmsLayerRenderer.copyTo(graphics);
    	layerRenderer.copyTo(graphics);
	}	

    
	public WMSLayerRenderer getWMSLayerRenderer(){
		return wmsLayerRenderer;
	}
	public LayerRenderer getLayerRenderer(){
		return layerRenderer;
	}
	
	public void cancel(){
		wmsLayerRenderer.cancel();
		layerRenderer.cancel();
	}
	public Object getContentID() {
		return contentID;
	}
    public void clearImageCache() {
    	wmsLayerRenderer.clearImageCache();
    	layerRenderer.clearImageCache();
    }
    public boolean isRendering() {
        return wmsLayerRenderer.rendering;
    }
    public Runnable createRunnable() {
    	layerRenderer.createRunnable();
    	layer.setFiringEvents(true);
    	return wmsLayerRenderer.createRunnable();
    }
}
    