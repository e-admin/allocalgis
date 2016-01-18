/**
 * SvgToBeans.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.global;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Clase con utilidades para parsear los svg cuadrícula y sacar la info relevante a beans
 * @author irodriguez
 *
 */
public class SvgToBeans{
	
	protected final static Logger logger = Logger.getLogger(SvgToBeans.class);
	
	/**
	 * Retorna un hash que asocia a cada fichero su encabezado
	 * @param ficherosSVG
	 * @return
	 */
	public HashMap<File, String> getSvgCellsSkeleton(List<File> ficherosSVG){
		HashMap<File, String> hashSvgCellsHeader = new HashMap<File, String>();
		String svgCellHeader = null;
		//recorro todos los ficheros sacando los encabezados
		for (int i = 0; i < ficherosSVG.size(); i++) {
			svgCellHeader = parseSvgHeader(ficherosSVG.get(i));
			hashSvgCellsHeader.put(ficherosSVG.get(i), svgCellHeader);
		}
		
		return hashSvgCellsHeader;
	}
	
	/**
	 * Obtiene la cabecera del fichero de celda SVG
	 * @param file
	 * @return
	 */
	public String parseSvgHeader(File file) {
		SAXBuilder builder = new SAXBuilder();
		String encabezadoRawText = null;
		try {
			Document doc = builder.build(file);
			Element rootElement = doc.getRootElement();
			encabezadoRawText = getRawNodeTextOpened(rootElement);
			logger.debug(encabezadoRawText);
			
		} catch (JDOMException e) {
			logger.error(e,e);
		} catch (IOException e) {
			logger.error(e,e);
		}
	    
		return encabezadoRawText;
	}
	
	/**
	 * Busca el nombre del atributo de la feature apartir de un valor presente en la capa a la que pertenece
	 * @param attributes
	 * @param gAttributeValue
	 * @return
	 */
	public String getAttributeFeature(List attributes, String gAttributeValue){
		String attKey = null;
		int attNumber = 0;
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			Attribute att = (Attribute) iterator.next();
			if(att.getValue().equals(gAttributeValue)){
				attKey = att.getName();
				attNumber = Integer.parseInt(attKey.substring(1));
				return "v"+attNumber;
			}
		}
		return null;
	}

	/**
	 * Obtiene el texto del nodo abierto
	 * @param e
	 * @return
	 */
	public String getRawNodeTextOpened(Element e){
		return getFullNodeText(e) + ">";
	}
	
	/**
	 * Obtiene el texto tal cual para el nodo indicado
	 * @param e
	 * @return
	 */
	public String getRawNodeTextClosed(Element e){
		return getFullNodeText(e) + "/>";
	}
	
	public String getFullNodeText(Element e){
		String str = "<" + e.getName() + " ";
		List attributes = e.getAttributes();
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			Attribute a = (Attribute) iterator.next();
			str += a.getName() + "=" + "\""+a.getValue()+"\" ";
		}
		return str;
	}
	
}
