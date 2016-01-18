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
 * Created on 03-nov-2004 by juacas
 *
 * 
 */
package com.geopista;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import com.vividsolutions.jump.workbench.WorkbenchPropertiesFile;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;

/**
 * Lee propiedades de configuración desde dentro de un JAR
 * @author juacas
 *
 */
public class GeopistaXMLProperties extends WorkbenchPropertiesFile
{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
	.getLog(GeopistaXMLProperties.class);

	/**
	 * Intenta cargar un recurso de paquete y si no intenta interpretarlo como un fichero
	 * @param file
	 * @param errorHandler
	 * @throws JDOMException
	 */
	public GeopistaXMLProperties(String file, ErrorHandler errorHandler)
	throws JDOMException {
		URL propertiesURL;
		try
		{
			propertiesURL = GeopistaXMLProperties.class.getResource("/"+file);
			if (propertiesURL==null) 
				propertiesURL= new URL("file",null,file);
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(propertiesURL);
			this.root = document.getRootElement();
			this.errorHandler = errorHandler;

		} catch (MalformedURLException e)
		{

			logger.error("GeopistaXMLProperties(String, ErrorHandler)", e);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GeopistaXMLProperties(ArrayList<String> files, ErrorHandler errorHandler) throws JDOMException {
		URL propertiesURL;
		try
		{
			Document document;
			Iterator<String> it = files.iterator();
			while (it.hasNext())
			{
				String file = it.next();
				propertiesURL = GeopistaXMLProperties.class.getResource("/"+file);
				if (propertiesURL==null) 
					propertiesURL= new URL("file",null,file);

				SAXBuilder builder = new SAXBuilder();
				document = builder.build(propertiesURL);
				
				if (this.root == null)
				{
					this.root = document.getRootElement();
				}
				else
				{	
					List<Element> children = document.getRootElement().getChildren();
					List<Element> lst = new ArrayList<Element>();
					for (int i =0; i< children.size(); i++)
					{
						lst.add((Element)((Element)children.get(i).clone()).detach());
					}
					this.root.addContent(lst);
				}
			}	
			
			this.errorHandler = errorHandler;

		} catch (MalformedURLException e)
		{

			logger.error("GeopistaXMLProperties(String, ErrorHandler)", e);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
