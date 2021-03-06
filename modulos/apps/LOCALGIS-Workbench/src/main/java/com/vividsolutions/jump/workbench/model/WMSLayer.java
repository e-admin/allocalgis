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

package com.vividsolutions.jump.workbench.model;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.renderer.RenderingManager;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;
import com.vividsolutions.wms.WMService;

/**
 * A Layerable that retrieves images from a Web Map Server.
 */
public class WMSLayer extends AbstractLayerable implements Cloneable,IWMSLayer {
    private String format;
    protected /*static*/ List  layerNames = new ArrayList();
    private Integer Id;
    private String srs;
    private int alpha = 255;
    /**Tabla hash con los estilos seleccionados por el usuario para cada capa*/
    protected /*static*/ HashMap  selectedStyles;

	private WMService service;

	private String wmsVersion = WMService.WMS_1_0_0;
	private boolean isTemplate;

	
	
	/**
	 * Called by Java2XML
	 */
	public WMSLayer() {
	}

	public WMSLayer(ILayerManager layerManager, String serverURL, String srs,
			List layerNames, String format, String version, HashMap selectedStyles, String wmsName) throws IOException {
		this(layerManager, initializedService(serverURL, version,true), srs, layerNames,
				format,selectedStyles, wmsName);
	}

	public WMSLayer(ILayerManager layerManager, String serverURL, String srs,
			List layerNames, String format, String version, HashMap selectedStyles, String wmsName,boolean inicializar) throws IOException {
		this(layerManager, initializedService(serverURL, version,inicializar), srs, layerNames,
				format,selectedStyles, wmsName);
	}

	
	
	private static WMService initializedService(String serverURL, String version,boolean inicializar)
			throws IOException {
		WMService initializedService = new WMService(serverURL,version);
		
		if (inicializar)
			initializedService.initialize();
		return initializedService;
	}

	public WMSLayer(ILayerManager layerManager, WMService initializedService,
			String srs, List layerNames, String format,HashMap selectedStyles, String wmsName) throws IOException {
		this(layerManager, initializedService, srs, layerNames, format, initializedService.getVersion(),selectedStyles, wmsName);
	}
	
	public WMSLayer(ILayerManager layerManager, WMService initializedService,
			String srs, List layerNames, String format) throws IOException {
		this(layerManager, initializedService, srs, layerNames, format, initializedService.getVersion());
	}

	public WMSLayer(ILayerManager layerManager, WMService initializedService,
			String srs, List layerNames, String format, String version){
	    super((String) layerNames.get(0), layerManager);
		setService(initializedService);
		setSRS(srs);
		this.layerNames = new ArrayList(layerNames);
		setFormat(format);
		getBlackboard().put(
				RenderingManager.USE_MULTI_RENDERING_THREAD_QUEUE_KEY, true);
		getBlackboard().put(LayerNameRenderer.USE_CLOCK_ANIMATION_KEY, true);
		this.wmsVersion = version;
	}
	
	public WMSLayer(ILayerManager layerManager, String serverURL, String srs,
			List layerNames, String format, String version) throws IOException {
		this(layerManager, initializedService(serverURL, version,true), srs, layerNames,
				format);
	}	
	
	public WMSLayer(ILayerManager layerManager, WMService initializedService,
			String srs, List layerNames, String format, String version,HashMap selectedStyles, String wmsName){
	    super(wmsName, layerManager);
		setService(initializedService);
		setSRS(srs);
		this.layerNames = new ArrayList(layerNames);
		setFormat(format);
		getBlackboard().put(
				RenderingManager.USE_MULTI_RENDERING_THREAD_QUEUE_KEY, true);
		getBlackboard().put(LayerNameRenderer.USE_CLOCK_ANIMATION_KEY, true);
		this.wmsVersion = version;
		setSelectedStyles(selectedStyles);
	}
	
	private void setService(WMService service) {
		this.service = service;
		this.serverURL = service.getServerUrl();
	}

	public int getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            0-255 (255 is opaque)
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public Image createImage(ILayerViewPanel panel) throws IOException {
		Image image = createRequest(panel).getImage();
		MediaTracker mt = new MediaTracker(new JButton());
		mt.addImage(image, 0);

		try {
			mt.waitForID(0);
		} catch (InterruptedException e) {
			Assert.shouldNeverReachHere();
		}

		return image;
	}

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

	public BoundingBox toBoundingBox(String srs, Envelope e) {
		return new BoundingBox(srs, e.getMinX(), e.getMinY(), e.getMaxX(), e
				.getMaxY());
	}

	public MapRequest createRequest(ILayerViewPanel panel) throws IOException {
		MapRequest request = getService().createMapRequest();
		request.setBoundingBox(toBoundingBox(srs, panel.getViewport()
				.getEnvelopeInModelCoordinates()));
		request.setFormat(format);
		request.setImageWidth(panel.getWidth());
		request.setImageHeight(panel.getHeight());
		request.setLayers(layerNames);
		request.setTransparent(true);
		request.setLayerStyles(selectedStyles);
		return request;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void addLayerName(String layerName) {
		layerNames.add(layerName);
	}

	public List getLayerNames() {
		return Collections.unmodifiableList(layerNames);
	}

	public void setSRS(String srs) {
		this.srs = srs;
	}

	public String getSRS() {
		return srs;
	}

	public Object clone() throws java.lang.CloneNotSupportedException {
		WMSLayer clone = (WMSLayer) super.clone();
		clone.layerNames = new ArrayList(this.layerNames);
		selectedStyles=clone.getSelectedStyles();
		return clone;
	}

	public void removeAllLayerNames() {
		layerNames.clear();
	}
	
	/*public void removeAllSelectedStyles(){
		selectedStyles.clear();
	}*/

	private Blackboard blackboard = new Blackboard();

	private String serverURL;

	public Blackboard getBlackboard() {
		return blackboard;
	}

	public WMService getService() throws IOException {
		if (service == null) {
			Assert.isTrue(serverURL != null);
			setService(initializedService(serverURL,wmsVersion,true));
		}
		return service;
	}

	public String getServerURL() {
		//Called by Java2XML [Jon Aquino 2004-02-23]
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		//Called by Java2XML [Jon Aquino 2004-02-23]
		this.serverURL = serverURL;
	}
    public String getWmsVersion() {
        return wmsVersion;
    }
    public void setWmsVersion(String wmsVersion) {
        this.wmsVersion = wmsVersion;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

	public HashMap getSelectedStyles() {
		return selectedStyles;
	}

	public /*static */void setSelectedStyles(HashMap ss) {
		selectedStyles = ss;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	
    
    
    
}