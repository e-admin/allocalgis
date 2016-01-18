package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosRetorno;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Retorno  
 * 
 * @author COTESA
 *
 */
public class UnidadDatosCatastroServerRetornoXMLHandler extends DefaultHandler 
{   
    //Lista de instancias
    //private ArrayList instancias;
	private ApplicationContext application = AppContext.getApplicationContext();    
	private Blackboard blackboard = application.getBlackboard();
    //UnidadDatosRetorno que se esta procesando
    private UnidadDatosRetorno actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    
    //Este es el Handler que procesara los expedientes que 
    //contenga el documento
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
    
    private SituacionCatastroServerXMLHandler handlerSituacion;
    
    private EntidadGeneradora entidadGeneradora;    

    private TaskMonitorDialog progressDialog;
    
    private int n=0;
    private boolean toImport = false;

    
    public UnidadDatosCatastroServerRetornoXMLHandler (XMLReader parser, TaskMonitorDialog progressDialog, boolean toImport)
    {
        this.parser = parser;
        //this.instancias = v;
        this.progressDialog = progressDialog;
        this.toImport = toImport;
        
        actual = new UnidadDatosRetorno ();
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {  
        valor = new StringBuffer();
        
        /*if (localName.equals("cabecera"))
        {
            entidadGeneradora = new EntidadGeneradora();
        }           
        else if (localName.equals("udsa")){
            //creamos la nueva instancia de UnidadDatosRetorno
            actual = new UnidadDatosRetorno ();
            //y la añadimos a la lista que las almacena
            //instancias.add (actual);
        }*/
        if (localName.equals("exp")){
            //creamos una instancia de Expediente
            Expediente expediente = new Expediente (); 
            actual.setExpediente(expediente);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, expediente, localName);
            parser.setContentHandler( handlerExpediente );
            /*if (expediente.getEntidadGeneradora()==null)
                expediente.setEntidadGeneradora(entidadGeneradora);*/
        }
        else if (localName.equals("lelems")){
            
            //creamos una instancia de Expediente
            //ArrayList situaciones = new ArrayList();
            //actual.setLstSituaciones(situaciones);
        	blackboard.put("UnidadesInsertadas", new Integer(0));
            blackboard.put("UnidadesNoInsertadas",new Integer(0));
           
            handlerSituacion = 
                new SituacionCatastroServerXMLHandler( parser, this, localName, progressDialog, n, toImport);
            parser.setContentHandler( handlerSituacion );
        }
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        if (localName.equals("lelems")){
            actual.getLstSituaciones().add(valor);
        }
        else if (localName.equals("teg") && entidadGeneradora!=null)
            entidadGeneradora.setTipo(valor.toString());
        else if (localName.equals("neg") && entidadGeneradora!=null)
            entidadGeneradora.setNombre(valor.toString());
        else if (localName.equals("cd") && entidadGeneradora!=null)
            entidadGeneradora.setCodigo(new Integer(valor.toString()).intValue());
    }
    
   
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}

