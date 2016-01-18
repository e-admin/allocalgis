/**
 * ArcIMSRequest.java
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

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.wms.BoundingBox;
import com.vividsolutions.wms.MapRequest;

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
