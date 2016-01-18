package com.geopista.app.catastro.intercambio.importacion.xml.handlers;



import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
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
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
    	valor = new StringBuffer();
    	if (localName.equals("irepc") || localName.equals("irepl")){

    		elementoReparto = new ElementoReparto();
    		this.lstElementosRepartos.add(elementoReparto);

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
    	}
    	else if (localName.equals("pctrep")){
    		pctrep = valor.toString();
    		elementoReparto.setPorcentajeReparto(new Float(pctrep.replaceAll(",", ".")).floatValue());
    	}
    	else if (localName.equals("noec")){
    		noec = valor.toString();
    		elementoReparto.setNumCargo(noec);
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