/**
 * ComunidadBienesCatastroServerXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;


public class ComunidadBienesCatastroServerXMLHandler extends DefaultHandler 
{  
    //Lista de instancias
    private ArrayList instancias;
    //ComunidadBienes que se esta procesando
    private ComunidadBienes actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    private DireccionLocalizacionCatastroServerXMLHandler handlerDireccion;
    private String etiqXMLorigen;
   
    
    public ComunidadBienesCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        //comprobamos si empezamos un elemento cbi
        if (localName.equals("cbi")){
            //creamos la nueva instancia 
            actual = new ComunidadBienes ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }        
        else if (localName.equals("df")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            actual.setDomicilio(dir);
            handlerDireccion = new DireccionLocalizacionCatastroServerXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        if (localName.equals("nifcb"))
            actual.setNif(valor.toString());
        else if (localName.equals("nomcb"))
            actual.setRazonSocial(valor.toString());          
        else if (localName.equals(etiqXMLorigen))
            parser.setContentHandler (handlerToReturn);
        
    }
    
   
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
