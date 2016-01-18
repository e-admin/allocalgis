/**
 * FxccXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.intercambio.importacion.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.FX_CC;


public class FxccXMLHandler extends DefaultHandler 
{
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //El parser, para luego devolver el control al
    //Handler anterior.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    //El FXCC donde meter los datos que leamos.
    private FX_CC fxcc;
    
    private String etiqXMLorigen;
   
    
    public FxccXMLHandler (XMLReader parser, DefaultHandler handler,
            FX_CC fxcc, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.fxcc = fxcc;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public FxccXMLHandler (XMLReader parser, FX_CC fxcc, String etiqXMLorigen)
    {
        this.parser = parser;
        this.fxcc = fxcc;
        this.etiqXMLorigen = etiqXMLorigen;
    }
   
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {        
        if (localName.equals("dxf"))
        {
            fxcc.setDXF(valor.toString());
        }   
        else if (localName.equals("asc"))
        {           
            fxcc.setASC(valor.toString());            
        }
        //si hemos llegado al final de la etiqueta
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
        
    }
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
//        valor.append(new String (ch, start, end).trim());       
    	valor.append(new String (ch, start, end));       
    }    
}
