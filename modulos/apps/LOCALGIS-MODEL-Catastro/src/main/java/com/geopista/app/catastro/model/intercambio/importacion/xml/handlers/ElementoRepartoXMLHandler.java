/**
 * ElementoRepartoXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.intercambio.importacion.xml.handlers;



import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.ElementoReparto;

public class ElementoRepartoXMLHandler extends DefaultHandler 
{
    /**
     * Valor contenido entre las etiquetas de un elemento
     */
    private StringBuffer valor = new StringBuffer();
    
    /**
     * El parser, para luego devolver el control al Handler del que se proviene
     */
    private XMLReader parser;
    
    /**
     * El Handler al que queremos volver
     */
    private DefaultHandler handlerToReturn;
    
    /**
     * El ElementoReparto donde meter los datos leidos
     */
    private ElementoReparto elementoReparto = null;
    
    private String etiqXMLorigen;

    private DireccionLocalizacionXMLHandler handlerDireccion;
    
    private ArrayList lstElementosRepartos = new ArrayList();
    private ArrayList lstElementosRepartosBi = new ArrayList();
    
    public ElementoRepartoXMLHandler (XMLReader parser, DefaultHandler handler,
            ElementoReparto elementoReparto, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.elementoReparto = elementoReparto;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public ElementoRepartoXMLHandler (XMLReader parser, DefaultHandler handler,
            ArrayList lstElementos, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.lstElementosRepartos = lstElementos;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public ElementoRepartoXMLHandler (XMLReader parser, DefaultHandler handler,
            ArrayList lstElementos,ArrayList lstElementos1, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.lstElementosRepartos = lstElementos;
        this.lstElementosRepartosBi = lstElementos1;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
    	valor = new StringBuffer();
    	if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
	    	if (localName.equals("irepc") || localName.equals("irepl")){
	
	    		elementoReparto = new ElementoReparto();
	    		this.lstElementosRepartos.add(elementoReparto);
	
	    	}
    	}
    	else{
    		
    		if (localName.equals("irepl")){
    			
	    		elementoReparto = new ElementoReparto();
	    		this.lstElementosRepartos.add(elementoReparto);
	
	    	}
    		else if (localName.equals("irepc")){
    			
	    		elementoReparto = new ElementoReparto();
	    		this.lstElementosRepartosBi.add(elementoReparto);
	
	    	}
    	}
    	
    }
    
    private String car = null;
    private String pctrep = null;
    private String noec = null;
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
    	if (localName.equals("car")){
    		car = valor.toString();
    		elementoReparto.setNumCargo(car);
    		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
    			//expediente de variaciones
    			//se marca que el objeto no es una construccion
    			//si se recibe el tag car
    			elementoReparto.setEsConstruccion(false);
    		
    		}
    	}
    	else if (localName.equals("pctrep")){
    		pctrep = valor.toString();
    		elementoReparto.setPorcentajeReparto(new Float(pctrep.replaceAll(",", ".")).floatValue());
    	}
    	else if (localName.equals("noec")){
    		noec = valor.toString();
    		elementoReparto.setNumCargo(noec);
    		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
        		//expediente de variaciones
    			//se marca que el objeto es una construccion
    			//si se recibe el tag pctrep
    			elementoReparto.setEsConstruccion(true);
    		}
    	}    	
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
       
    }
    
    
    /*
     Los parametros que recibe es la localizacion de los carateres del elemento.
     */
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}