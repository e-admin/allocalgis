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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
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

	private static final String TAG_RESOURCES = "resources";
	private static final String TAG_JAR = "jar";
	private static final String ATTRIBUTE_HREF = "href";
	private static final String NOMBRE_AUTO_IMPORTER_PLUGIN = "_autoimporterplugin";
	
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
		URL propertiesURL_plugin;
		try
		{
			Document document;
			Document document_plugin;
			Document document_jnlp;
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
		
			// gestion de insertar plugins de forma automatica
			RuntimeMXBean RuntimemxBean = ManagementFactory.getRuntimeMXBean();
            HashMap arguments = (HashMap) RuntimemxBean.getSystemProperties();
            

           String jnlp_origFilenameArg = (String)arguments.get("jnlpx.origFilenameArg");          
           if(jnlp_origFilenameArg != null){
	            ArrayList lstPlugin = new ArrayList<String>();
	            File fileJNLPLocation = new File(jnlp_origFilenameArg);
	            
	            SAXBuilder builder_aux = new SAXBuilder();
	            document_jnlp =  builder_aux.build(fileJNLPLocation);
	            
	            List<Element> children_aux = document_jnlp.getRootElement().getChildren();
	            List<Element> lst_aux = new ArrayList<Element>();
				for (int i =0; i< children_aux.size(); i++)
				{
					if(((Element)((Element)children_aux.get(i).clone()).detach()).getName().equals(TAG_RESOURCES)){
						
						List<Element> childrenJAR = children_aux.get(i).getChildren();
						for (int j =0; j< childrenJAR.size(); j++)
						{
							if(((Element)((Element)childrenJAR.get(j).clone()).detach()).getName().equals(TAG_JAR)){
							
								String hrefAttribute = (String)childrenJAR.get(j).getAttributeValue(ATTRIBUTE_HREF);
								
								if(hrefAttribute.toLowerCase().contains(NOMBRE_AUTO_IMPORTER_PLUGIN)){
	
									String [] hrefAttr = hrefAttribute.split("/");
									lstPlugin.add(hrefAttr[hrefAttr.length-1]);
								}
								
							}
						}
						
					}
				}
				if(!lstPlugin.isEmpty()){
					for (int h = 0; h < lstPlugin.size(); h++) {
						String nameJar = (String)lstPlugin.get(h);
						try{
	
							propertiesURL_plugin = GeopistaXMLProperties.class.getResource("/"+nameJar.substring(0, nameJar.length() -4)+"-properties.xml");							
							if (propertiesURL_plugin==null) 
								propertiesURL_plugin= new URL("file",null,(String)lstPlugin.get(h));
	
							SAXBuilder builder = new SAXBuilder();
							document_plugin = builder.build(propertiesURL_plugin);
							if (this.root == null)
							{
								this.root = document_plugin.getRootElement();
							}
							else
							{	
								List<Element> children = document_plugin.getRootElement().getChildren();
								List<Element> lst = new ArrayList<Element>();
								for (int i =0; i< children.size(); i++)
								{
									lst.add((Element)((Element)children.get(i).clone()).detach());
									
								}
							
								this.root.addContent(lst);
								
								
							}
				}
						catch (FileNotFoundException fnfe) {
							fnfe.printStackTrace();
			}	
			
					}
				}
           }
         
			// fin gestion de insertar plugins de forma automatica
			

			this.errorHandler = errorHandler;

		} catch (MalformedURLException e)
		{

			logger.error("GeopistaXMLProperties(String, ErrorHandler)", e);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
