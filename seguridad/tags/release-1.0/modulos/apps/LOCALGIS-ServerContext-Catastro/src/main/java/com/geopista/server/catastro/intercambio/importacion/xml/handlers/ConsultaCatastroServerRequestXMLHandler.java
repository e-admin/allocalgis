package com.geopista.server.catastro.intercambio.importacion.xml.handlers;


import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.geopista.app.catastro.model.beans.ErrorXML;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Retorno  
 * 
 * @author COTESA
 *
 */
public class ConsultaCatastroServerRequestXMLHandler extends DefaultHandler 
{   
    //Lista de instancias
    private ArrayList instancias;
    
    //UnidadDatosRetorno que se esta procesando
    //private ConsultaCatastroRequest consultaCatastroRequest;
    //private UnidadDatosRetorno unidadDatosRetorno;
    private ErrorXML error;
    
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    
    //Este es el Handler que procesara los expedientes que 
    //contenga el documento
    private UnidadDatosCatastroServerRetornoXMLHandler handlerUnidadDatosRetorno;
    
    private TaskMonitorDialog progressDialog;
    
    //private ExpedienteXMLHandler handlerExpediente;
    
    //private SituacionXMLHandler handlerSituacion;
    
    //private EntidadGeneradora entidadGeneradora;
    
    public ConsultaCatastroServerRequestXMLHandler (XMLReader parser, ArrayList v, TaskMonitorDialog progressDialog)
    {
        this.parser = parser;
        this.instancias = v;
        this.progressDialog = progressDialog;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {  
        valor = new StringBuffer();
        
        /*if(localName.equals("ConsultaCatastroResponse")){
        	consultaCatastroRequest = new ConsultaCatastroRequest();
        	instancias.add(consultaCatastroRequest);
        }*/
        if (localName.equals("err")||localName.equals("uderr"))
        {
            error = new ErrorXML();
            instancias.add(error);
        }           
        else if (localName.equals("udsa")){
            //creamos la nueva instancia de UnidadDatosRetorno
            //unidadDatosRetorno = new UnidadDatosRetorno ();
            //consultaCatastroRequest.setUnidadDatosRetorno(unidadDatosRetorno);
            //y la añadimos a la lista que las almacena
            handlerUnidadDatosRetorno = new UnidadDatosCatastroServerRetornoXMLHandler( parser, progressDialog, true);
            parser.setContentHandler( handlerUnidadDatosRetorno );            
        }       
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        if (localName.equals("cod") && error!=null)
            error.setCodigo(valor.toString());
        else if (localName.equals("des") && error!=null)
            error.setDescripcion(valor.toString());                

    }
    
   
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}

