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
 * Created on 23-may-2004 by juacas
 *
 * 
 */
package com.geopista.model;

import java.awt.Image;
import java.awt.MediaTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.DynamicFeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;
import com.vividsolutions.wms.WMService;

/**
 * @author juacas
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class DynamicLayer extends GeopistaLayer 
{
    private String format;
    private String srs;
    private String url;
    private String version;
    private String time;
	private WMService service;
    protected /*static*/ List  layerNames = new ArrayList();
    private int alpha = 255;
    protected /*static*/ HashMap  selectedStyles;
    private DynamicFeatureCollectionWrapper dynamicFeatureCollectionWrapper;
    private FeatureCollectionWrapper featureCollectionWrapper;
    private boolean firingEvents = true;


    /**
     * 
     */
    public DynamicLayer()
        {
            super();
            // TODO Auto-generated constructor stub
        }


	public DynamicLayer(String url, String srs, String format, String version, String time, List layerNames, HashMap selectedStyles){
        super();
		try{
			this.url = url;
			this.srs = srs;
			this.format = format;
			this.version = version;
			this.layerNames = layerNames;
			this.time = time;
			setSelectedStyles(selectedStyles);
			getBlackboard().put(
					RenderingManager.USE_MULTI_RENDERING_THREAD_QUEUE_KEY, true);
			getBlackboard().put(LayerNameRenderer.USE_CLOCK_ANIMATION_KEY, true);
			setService(initializedService(url, version));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#createImage(com.vividsolutions.jump.workbench.ui.LayerViewPanel)
	 */
	
	public Image createImage(LayerViewPanel panel) throws IOException {
		final Image image = createRequest(panel).getImage();

		try {
			MediaTracker mt = new MediaTracker(new JButton());
			mt.addImage(image, 0);
			mt.waitForID(0);
		} catch (InterruptedException e) {
			Assert.shouldNeverReachHere();
    	} 
		return image;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getEnvelope()
	 */
	
	public Envelope getEnvelope()
	{
	BoundingBox bb;
	try
		{
		bb = getService().getCapabilities().getTopLayer().getBoundingBox();
		return bb==null?null:new Envelope(bb.getMinX(),bb.getMinY(),bb.getMaxX(),bb.getMaxY());
		}
	catch (IOException e)
		{
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	return null;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#toBoundingBox(java.lang.String, com.vividsolutions.jts.geom.Envelope)
	 */
	
	public BoundingBox toBoundingBox(String srs, Envelope e) {
		return new BoundingBox(srs, e.getMinX(), e.getMinY(), e.getMaxX(), e
				.getMaxY());
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#createRequest(com.vividsolutions.jump.workbench.ui.LayerViewPanel)
	 */
	
	public MapRequest createRequest(LayerViewPanel panel) throws IOException {
		MapRequest request = getService().createMapRequest();
		request.setBoundingBox(toBoundingBox(srs, panel.getViewport()
				.getEnvelopeInModelCoordinates()));
		request.setFormat(format);
		request.setImageWidth(panel.getWidth());
		request.setImageHeight(panel.getHeight());
		request.setLayers(layerNames);
		request.setTransparent(true);
		request.setLayerStyles(selectedStyles);		
		request.setTime(time);
		if (this.getRevisionActual() == -1)
			time = null;
		//Layername style
		//request.setLayerStyles(getStyles());
		return request;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getService()
	 */
	
	public WMService getService() throws IOException {
		if (service == null) {
			Assert.isTrue(url != null);
			setService(initializedService(url,version));
		}
		return service;
	}
	
	private static WMService initializedService(String serverURL, String version)
	throws IOException {
		WMService initializedService = new WMService(serverURL,version);
		initializedService.initialize();
		return initializedService;
	}
	
	private void setService(WMService service) {
		this.service = service;
		this.url = service.getServerUrl();
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#isFiringEvents()
	 */
	
	public boolean isFiringEvents(){
		return this.firingEvents;
	}
	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setFiringEvents(boolean)
	 */
	
	public void setFiringEvents(boolean firingEvents){
		this.firingEvents = firingEvents;		
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getAlpha()
	 */
	
	public int getAlpha() {
		return alpha;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setAlpha(int)
	 */
	
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getSelectedStyles()
	 */
	
	public HashMap getSelectedStyles() {
		return selectedStyles;
	}

	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setSelectedStyles(java.util.HashMap)
	 */
	
	public /*static */void setSelectedStyles(HashMap ss) {
		selectedStyles = ss;
	}
	
    /* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getDynamicFeatureCollectionWrapper()
	 */
    
	public DynamicFeatureCollectionWrapper getDynamicFeatureCollectionWrapper() {
        return dynamicFeatureCollectionWrapper;
    }	

    /* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getFeatureCollectionWrapper()
	 */
    
	public FeatureCollectionWrapper getFeatureCollectionWrapper() {
        return featureCollectionWrapper;
    }	


    /* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setFeatureCollectionWrapper(com.vividsolutions.jump.feature.DynamicFeatureCollectionWrapper)
	 */
    
	public void setFeatureCollectionWrapper(
    		DynamicFeatureCollectionWrapper featureCollectionWrapper) {
        this.dynamicFeatureCollectionWrapper = featureCollectionWrapper;
    }
    /* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setFeatureCollectionWrapper(com.vividsolutions.jump.feature.FeatureCollectionWrapper)
	 */
    
	public void setFeatureCollectionWrapper(
    		FeatureCollectionWrapper featureCollectionWrapper) {
        this.featureCollectionWrapper = featureCollectionWrapper;
    }


	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#getTime()
	 */
	
	public String getTime() {
		return time;
	}


	/* (non-Javadoc)
	 * @see com.geopista.model.IDynamicLayer#setTime(java.lang.String)
	 */
	
	public void setTime(String time) {
		this.time = time;
	}
}
