/**
 * UnidadDatosEntradaXMLHandler.java
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

import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosEntrada;
import com.geopista.app.catastro.model.intercambio.importacion.xml.handlers.BienInmuebleJuridicoXMLHandler;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Salida  
 * 
 * @author COTESA
 *
 */
public class UnidadDatosEntradaXMLHandler extends DefaultHandler 
{   
    //vector de instancias
    private ArrayList instancias;
    
    //UnidadDatosEntrada que se esta procesando
    private UnidadDatosEntrada actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    // El Handler al que queremos volver
    //private DefaultHandler handlerToReturn;
    
  
    private BienInmuebleJuridicoXMLHandler handlerBIJ;
  
	private ArrayList lstBienesInmuebles;
    
    public UnidadDatosEntradaXMLHandler (XMLReader parser, ArrayList v)
    {
        this.parser = parser;
        this.instancias = v;
        //this.handlerToReturn = handlerToReturn;
    }
    
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {        
        valor = new StringBuffer();
        
        //comprobamos si empezamos un elemento udsa
        if (localName.equals("uden")){
            //creamos la nueva instancia 
            actual = new UnidadDatosEntrada ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }
        
        //Lista de bienes inmuebles
        else if (localName.equals("lelem")){
            
            lstBienesInmuebles = new ArrayList();
            actual.setLstBienesInmuebles(lstBienesInmuebles);  
            handlerBIJ = new BienInmuebleJuridicoXMLHandler( parser, this, 
            		lstBienesInmuebles, localName);
            parser.setContentHandler( handlerBIJ );
        }
        
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {       
       
    }
        
    /**
     Los parametros que recibe es la localizacion de los carateres del elemento.
     */
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }    
}

