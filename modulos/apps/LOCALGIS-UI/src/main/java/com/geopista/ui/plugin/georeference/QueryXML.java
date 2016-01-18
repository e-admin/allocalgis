/**
 * QueryXML.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.georeference;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Dentro de esta clase crearemos cada query que se realiza para llamar al WFS
 * y obtener las coordenadas para georeferenciar las direcciones.
 * @author jvaca
 *
 */
public class QueryXML extends Element
{
    public QueryXML(String nameCall,String num,String idMunicipio){
        
        super("Query",Namespace.getNamespace("wfs","http://www.opengis.net/wfs"));
        setAttribute("typeName","geocodificacion");
        addContent(createQuery( nameCall, num, idMunicipio));
    }
    
    public Element createQuery(String nameCall,String num,String idMunicipio){

            
            Namespace OGC_NAMESPACE = Namespace.getNamespace("ogc", "http://www.opengis.net/ogc");
            
            Element FilterXml=new Element("Filter",OGC_NAMESPACE);
            Element andXml=new Element("And",OGC_NAMESPACE);
            FilterXml.addContent(andXml);

            Element isEqualXML = new Element("PropertyIsEqualTo",OGC_NAMESPACE);
            andXml.addContent(isEqualXML);
            isEqualXML.addContent(new Element("PropertyName",OGC_NAMESPACE).setText("/geocodificacion/municipality"));
            isEqualXML.addContent(new Element("Literal",OGC_NAMESPACE).setText(idMunicipio));
            
            
            Element isLikeXML=new Element("PropertyIsLike",OGC_NAMESPACE);
            isLikeXML.setAttribute("wildCard","*")
                     .setAttribute("singleChar","?")
                     .setAttribute("escape","\\");
                     
            andXml.addContent(isLikeXML);
            isLikeXML.addContent(new Element("PropertyName",OGC_NAMESPACE).setText("/geocodificacion/streetType"));
            isLikeXML.addContent(new Element("Literal",OGC_NAMESPACE).setText("*"));
            
            if(num!=null){
                Element isEqualXML2=new Element("PropertyIsEqualTo",OGC_NAMESPACE);
                andXml.addContent(isEqualXML2);
                isEqualXML2.addContent(new Element("PropertyName",OGC_NAMESPACE).setText("/geocodificacion/streetNumber"));
                isEqualXML2.addContent(new Element("Literal",OGC_NAMESPACE).setText(num));
            }
	        Element isEqual2=new Element("PropertyIsEqualTo",OGC_NAMESPACE);             
	        andXml.addContent(isEqual2);
	        isEqual2.addContent(new Element("PropertyName",OGC_NAMESPACE).setText("/geocodificacion/street"));
	        isEqual2.addContent(new Element("Literal",OGC_NAMESPACE).setText(nameCall));

        return FilterXml;
    }
}
