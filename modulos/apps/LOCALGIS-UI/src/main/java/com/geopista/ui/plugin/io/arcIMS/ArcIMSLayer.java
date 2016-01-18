/**
 * ArcIMSLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 01-jun-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.io.arcIMS;

import java.io.IOException;
import java.util.Arrays;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ArcIMSLayer extends WMSLayer
{

	/**
	 * 
	 */
	public ArcIMSLayer()
	{
	super();
	// TODO Auto-generated constructor stub
	}

	/**
	 * @param layerManager
	 * @param serverURL
	 * @param serviceName
	 * @param layerNames
	 * @param format
	 * @throws IOException 
	 */
	public ArcIMSLayer(LayerManager layerManager, String serverURL, String serviceName) throws IOException
	{
	super(layerManager, serverURL, serviceName ,Arrays.asList( new String[]{serviceName}), "","ArcIMS");

	}


	public MapRequest createRequest(LayerViewPanel panel) throws IOException
	{
	ArcIMSRequest request= new ArcIMSRequest( (String) getLayerNames().get(0),getServerURL());
	 request.setBoundingBox(toBoundingBox("UTM29",
            panel.getViewport().getEnvelopeInModelCoordinates()));
    request.setFormat("");
    request.setImageWidth(panel.getWidth());
    request.setImageHeight(panel.getHeight());
    //request.setLayers(layerNames);
    request.setTransparent(true);
	return request;
	}
	  public BoundingBox toBoundingBox(String srs, Envelope e) {
      return new BoundingBox(srs, (float) e.getMinX(), (float) e.getMinY(),
          (float) e.getMaxX(), (float) e.getMaxY());
  }}
