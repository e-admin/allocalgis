/*
 * 
 * Created on 01-jun-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.io.arcIMS;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.geopista.model.WMSLayerImpl;
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
