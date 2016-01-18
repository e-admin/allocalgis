package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Persona;


public class RepresentanteXMLHandler extends DefaultHandler 
{   
    //Representante que se esta procesando
    private Persona representante;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;

    private DireccionLocalizacionXMLHandler handlerDireccion;
    private String etiqXMLorigen;
	private ExpedienteXMLHandler handlerExpediente;
    
    
    public RepresentanteXMLHandler (XMLReader parser, DefaultHandler handler,
            Persona rep, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.representante = rep;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        if (localName.equals("df")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            representante.setDomicilio(dir);
            handlerDireccion = new DireccionLocalizacionXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }     
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            representante.setExpediente(exp);
            handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }  
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
       
        //idr
        if (localName.equals("nif"))
            representante.setNif(valor.toString());
        else if (localName.equals("nom"))
            representante.setRazonSocial(valor.toString());
       
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
