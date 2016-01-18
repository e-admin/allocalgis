package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.geopista.app.catastro.model.beans.ImagenCatastro;


public class ImagenXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //ImagenCatastro que se esta procesando
    private ImagenCatastro actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    
    String ref1 = new String();
    String ref2 = new String();
        
    private String etiqXMLorigen;
        
    public ImagenXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList lstImagenes, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = lstImagenes;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;      
                
    }
    
    public ImagenXMLHandler (XMLReader parser, ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;   
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        if (localName.equals("img")){        	
        	this.actual = new ImagenCatastro();
        	instancias.add(this.actual);
        }
     
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
    	
    	if (localName.equals("nom")){
    		this.actual.setNombre(valor.toString());
    	}
    	else if (localName.equals("frmt")){
    		this.actual.setExtension(valor.toString());
    	}
    	else if (localName.equals("tdo")){
    		this.actual.setTipoDocumento(valor.toString());
    	}
    	else if (localName.equals("foto")){
    		this.actual.setFoto(valor.toString());    	
        }        
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
        
    }
    
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
