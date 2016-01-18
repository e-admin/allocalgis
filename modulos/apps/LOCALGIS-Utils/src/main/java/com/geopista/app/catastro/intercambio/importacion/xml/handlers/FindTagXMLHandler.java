/**
 * FindTagXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.xml.handlers;


import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Salida  
 * 
 * @author COTESA
 *
 */
public class FindTagXMLHandler extends DefaultHandler 
{   
    /**
     * Comprueba si existe la etiqueta
     */
    private ArrayList foundTag;
    /**
     * Etiqueta a buscar
     */
    private String tagToFind;

    private XMLReader parser;
    
   
    
    public FindTagXMLHandler (XMLReader parser, ArrayList foundTag, String tagToFind)
    {
        this.parser = parser;
        this.foundTag = foundTag;     
        this.tagToFind = tagToFind;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {        
        if (localName.equals(tagToFind)){
            foundTag.add(localName);
        }
    }
}

