/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Code is based on code from cz.vsb.gisak.GVSB_View.java class AXLRequester @author petrjanik@centrum.cz
 *
 * $Id: ArcIMSConnector.java,v 0.1 20041110 
 *
 * Copyright (C) 2004 Jan Ruzicka jan.ruzicka@vsb.cz
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
 */

/**
 *
 * <p>Title: ArcIMSConnector</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Jan Ruzicka jan.ruzicka@vsb.cz
 * @version 0.1
 */

package cz.vsb.gisak.jump.plugin.arcims;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*; //utils for collections
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import com.vividsolutions.jts.geom.*;

public class ArcIMSConnector {
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory
												.getLog(ArcIMSConnector.class);

  private String serviceURL;
  public ArcIMSConnector(String url) {
	serviceURL = url;
  }

  private String getRequestServices() {
    /*
    <GETCLIENTSERVICES/>
    */
	if (logger.isDebugEnabled())
		{
		logger
				.debug("getRequestServices() - Creating AXL MapServer request for Get Services List");
		}
    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getRequestServices()" + ex);
		}
    }
    Document doc=builder.newDocument();
  
    Element request = doc.createElement ("GETCLIENTSERVICES");
    doc.appendChild(request);
       
    TransformerFactory tff=TransformerFactory.newInstance();
    Transformer tf = null;
    String ss = "";
    try {
      tf = tff.newTransformer();
      StringWriter sw = new StringWriter();
      tf.transform(new DOMSource(doc), new StreamResult(sw));
      ss = sw.toString();
    }
    catch (TransformerConfigurationException ex1) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestServices()" + ex1);
				}
    }
    catch (TransformerException ex2) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestServices()" + ex2);
				}
    }
    
    return ss;
 
  }

  private String getRequestGetServiceInfo() {
    /*
     <GET_SERVICE_INFO fields="false" envelope="false" renderer="false" extensions="true" />
    */
	if (logger.isDebugEnabled())
		{
		logger
				.debug("getRequestGetServiceInfo() - Creating AXL MapServer request for Service Info");
		}
    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getRequestGetServiceInfo()" + ex);
		}
    }
    Document doc=builder.newDocument();

    Element arcxml = doc.createElement ("ARCXML");
    arcxml.setAttribute("version","1.0");
    doc.appendChild(arcxml);

    Element request = doc.createElement ("REQUEST");
    arcxml.appendChild(request);

    Element getinfo = doc.createElement ("GET_SERVICE_INFO");
    getinfo.setAttribute("fields","false");
    getinfo.setAttribute("envelope","false");
    getinfo.setAttribute("renderer","false");
    getinfo.setAttribute("extensions","true");

    request.appendChild(getinfo);
    
    TransformerFactory tff=TransformerFactory.newInstance();
    Transformer tf = null;
    String ss = "";
    try {
      tf = tff.newTransformer();
      StringWriter sw = new StringWriter();
      tf.transform(new DOMSource(doc), new StreamResult(sw));
      ss = sw.toString();
    }
    catch (TransformerConfigurationException ex1) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestGetServiceInfo()" + ex1);
				}
    }
    catch (TransformerException ex2) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestGetServiceInfo()" + ex2);
				}
    }

    return ss;

  }

  private String getRequestGetMap (Envelope envelope, int width, int height) {
	if (logger.isDebugEnabled())
		{
		logger
				.debug("getRequestGetMap(Envelope, int, int) - Creating AXL MapServer request");
		}
    /** One parameter (XML with parameters) must be sent:
     XML structure:
     <ARCXML version="1.0">
      <REQUEST>
       <GET_IMAGE>
        <PROPERTIES>
     <ENVELOPE minx="-122.5" miny="37.8" maxx="-122.4" maxy="37.9"/>
     <IMAGESIZE width="800" height="600"/>
     <BACKGROUND transcolor="255,255,255" color="255,255,255"/>
        </PROPERTIES>
       <GET_IMAGE>
      <REQUEST>
     </ARCXML>
    */
   /* Extend the following code */
    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getRequestGetMap(Envelope, int, int)" + ex);
		}
    }
    Document doc=builder.newDocument();

    Element arcxml = doc.createElement ("ARCXML");
    arcxml.setAttribute("version","1.0");
    doc.appendChild(arcxml);

    Element request = doc.createElement ("REQUEST");
    arcxml.appendChild(request);

    Element getimage = doc.createElement ("GET_IMAGE");
    request.appendChild(getimage);

    Element properties = doc.createElement ("PROPERTIES");
    getimage.appendChild(properties);

    Element envelopeElement = doc.createElement ("ENVELOPE");
    envelopeElement.setAttribute("minx",""+envelope.getMinX());
    envelopeElement.setAttribute("miny",""+envelope.getMinY());
    envelopeElement.setAttribute("maxx",""+envelope.getMaxX());
    envelopeElement.setAttribute("maxy",""+envelope.getMaxY());
    properties.appendChild(envelopeElement);

    Element imagesize = doc.createElement ("IMAGESIZE");
    imagesize.setAttribute("width",""+width);
    imagesize.setAttribute("height",""+height);
    properties.appendChild(imagesize);

    Element background = doc.createElement ("BACKGROUND");
    background.setAttribute("transcolor","255,255,255");
    background.setAttribute("color","255,255,255");
    properties.appendChild(background);

    TransformerFactory tff=TransformerFactory.newInstance();
    Transformer tf = null;
    String ss = "";
    try {
      tf = tff.newTransformer();
      StringWriter sw = new StringWriter();
      tf.transform(new DOMSource(doc), new StreamResult(sw));
      ss = sw.toString();
    }
    catch (TransformerConfigurationException ex1) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestGetMap(Envelope, int, int)" + ex1);
				}
    }
    catch (TransformerException ex2) {
			if (logger.isDebugEnabled())
				{
				logger.debug("getRequestGetMap(Envelope, int, int)" + ex2);
				}
    }

    return ss;

  }

  private String getImageURL(String response) {
    String atr = "";
	if (logger.isDebugEnabled())
		{
		logger
				.debug("getImageURL(String) - Returning image URL from AXL MapServer");
		}

    /*Common response:
     <?xml version="1.0">
      <ARCXML version="1.0">
       <RESPONSE>
        <IMAGE>
     <ENVELOPE minx="-122.5" miny="37.8" maxx="-122.4" maxy="37.9"/>
     <OUTPUT file="F:\MAP_A14829.jpg" url="http://localhost/maps/MAP_A14829.jpg"/>
        </IMAGE>
     </RESPONSE>
     </ARCXML>
     */
    /* Use GVSB_XMLParser or write your own parser based on DocumentBuilderFactory*/

    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = dbf.newDocumentBuilder();
      Document result = builder.parse(new StringBufferInputStream(response));
      Element output=(Element)result.getElementsByTagName("OUTPUT").item(0);
      atr = output.getAttribute("url");
    }
    catch (ParserConfigurationException ex) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getImageURL(String) - ParserEx" + ex);
		}
    }
    catch (IOException ex1) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getImageURL(String) - IOe" + ex1);
		}
    }
    catch (org.xml.sax.SAXException ex1) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getImageURL(String) - SAXe" + ex1);
		}
    }

    return atr;
  }

   private ArrayList getServices(String response) {
	if (logger.isDebugEnabled())
		{
		logger
				.debug("getServices(String) - Returning list of services from AXL MapServer");
		}
    ArrayList servicesList = new ArrayList();
    DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = dbf.newDocumentBuilder();
      Document result = builder.parse(new StringBufferInputStream(response));
      String atr = "";
      int servicesCount;
      int servicesCount2;
      servicesCount = result.getElementsByTagName("SERVICE").getLength();
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices(String) - Services count: " + servicesCount);
		}
      servicesCount2=0;
      for (int i=0; i<servicesCount; i++) {
        Element output=(Element)result.getElementsByTagName("SERVICE").item(i);
        String type = output.getAttribute("TYPE");
        String access = output.getAttribute("ACCESS");
        type = type.toLowerCase().trim();
        access = access.toLowerCase().trim();
        atr = output.getAttribute("NAME");
		if (logger.isDebugEnabled())
			{
			logger
					.debug("getServices(String) - Type and access of the service "
							+ atr + type + " " + access);
			}
        if ((type.equals("imageserver")) && (access.equals("public"))) {
          servicesList.add(atr);
          servicesCount2++;
        }
      }
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices(String) - Image and public services count: "
				+ servicesCount2);
		}
    }
    catch (ParserConfigurationException ex) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices(String) - ParserEx" + ex);
		}
    }
    catch (IOException ex1) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices(String) - IOe" + ex1);
		}
    }
    catch (org.xml.sax.SAXException ex1) {
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices(String) - SAXe" + ex1);
		}
    }

    return servicesList;
  }
  
  public ArrayList getServices() {
    String su = getRequestServices();
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices()" + su);
		}
    URLStreamer us = new URLStreamer();
    String suOut = us.runPostOnServerlet(su, serviceURL + "ServiceName=catalog");
	if (logger.isDebugEnabled())
		{
		logger.debug("getServices()" + suOut);
		}
    ArrayList ser;
    ser = getServices(suOut);
    return ser;
  }

  public String getImageURL(Envelope envelope, int width, int height) {
    String su = getRequestGetMap(envelope, width, height);

    URLStreamer us = new URLStreamer();
    String suOut = us.runPostOnServerlet(su, serviceURL);
	if (logger.isDebugEnabled())
		{
		logger.debug("getImageURL(Envelope, int, int)" + suOut);
		}

    String imageURL = getImageURL(suOut);
	if (logger.isDebugEnabled())
		{
		logger.debug("getImageURL(Envelope, int, int)" + imageURL);
		}

    return imageURL;
  }
}


