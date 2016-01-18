package com.geopista.ui.plugin.georeference;

import java.util.List;

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
