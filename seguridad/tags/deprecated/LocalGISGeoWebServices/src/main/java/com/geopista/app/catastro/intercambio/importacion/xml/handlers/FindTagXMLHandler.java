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

