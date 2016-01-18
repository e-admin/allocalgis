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

package com.geopista.model;

import java.awt.Image;
import java.util.HashMap;
import java.awt.MediaTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import com.geopista.io.datasource.wms.WMService;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;


/**
 * A Layerable that retrieves images from a Web Map Server.
 */
public class WMSLayerImpl extends com.vividsolutions.jump.workbench.model.WMSLayer implements Cloneable {
   

    /**
     *  Called by Java2XML
     */
    public WMSLayerImpl() {
    super();
    }

    public WMSLayerImpl(LayerManager layerManager, String serverURL, String srs, String wmsName,
        List layerNames, String format,HashMap selectedStyles) throws IOException {
        super(layerManager, serverURL, srs, layerNames, format,WMService.WMS_1_1_1,
        		selectedStyles, wmsName);
       
    }
    
    public WMSLayerImpl(LayerManager layerManager, String serverURL, String srs,
            List layerNames, String format) throws IOException {
            super(layerManager, serverURL, srs, layerNames, format,WMService.WMS_1_1_1);
           
        }
    
    /**
     * Overloaded to use com.geopista classes compatible with 1.1
     */
    public Image createImage(LayerViewPanel panel) throws IOException {
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
    public MapRequest createRequest(LayerViewPanel panel)
    throws IOException {
    WMService service = new WMService(getServerURL());
    service.initialize();

    MapRequest request = service.createMapRequest();
    request.setBoundingBox(toBoundingBox(getSRS(),
            panel.getViewport().getEnvelopeInModelCoordinates()));
    request.setFormat(getFormat());
    request.setImageWidth(panel.getWidth());
    request.setImageHeight(panel.getHeight());
    request.setLayers(layerNames);
    request.setTransparent(true);
    request.setLayerStyles(selectedStyles);

    return request;
}
}
