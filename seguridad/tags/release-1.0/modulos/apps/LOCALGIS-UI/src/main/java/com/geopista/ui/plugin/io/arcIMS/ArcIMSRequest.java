/*
 * 
 * Created on 01-jun-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.io.arcIMS;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;
import com.vividsolutions.wms.WMService;

import cz.vsb.gisak.jump.plugin.arcims.ArcIMSConnector;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ArcIMSRequest extends MapRequest
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(ArcIMSRequest.class);

	private String	url;
	private String	serviceName;


	/**
	 * @param service
	 */
	public ArcIMSRequest(String servicename, String url)
	{
	this.url=url;
	this.serviceName=servicename;
	this.setFormat("");
	this.setImageSize(100,100);
	}
		
	public URL getURL() throws MalformedURLException
	{
	BoundingBox bb =getBoundingBox();
	
	Envelope envelope = new Envelope(bb.getMaxX(),bb.getMinX(),bb.getMaxY(),bb.getMinY()); 
	    
	        ArcIMSConnector arcims = new ArcIMSConnector(url);
	        URL imageURL = null;
	        
	          imageURL = new URL(arcims.getImageURL(envelope, getImageWidth(),getImageHeight()));
			if (logger.isDebugEnabled())
				logger.debug("getURL() - Image URL: "
								+ imageURL);
				
	return imageURL;
	}
	public Image getImage() throws MalformedURLException
	{
	Image image;
	try
		{
		image=Toolkit.getDefaultToolkit().createImage( getURL() );
		}
	catch (MalformedURLException e)
		{
		image = IconLoader.icon("World.gif").getImage();
			
		e.printStackTrace();
		}
	
	return image;
	}
	public void setFormat(String format) throws IllegalArgumentException
	{
	
	}
	public String getFormat()
	{
	
	return "";
	}
}
