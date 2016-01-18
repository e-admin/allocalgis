package com.geopista.app.catastro.intercambio.importacion.xml.handlers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.intercambio.importacion.xml.contents.UnidadDatosRetorno;
import com.geopista.app.catastro.model.beans.DatosRegistroExpedientes;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Fichero;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Parsea una Lista de Unidades de Datos del Fin de Retorno  
 * 
 * @author COTESA
 *
 */
public class FinRetornoXMLHandler extends DefaultHandler 
{   
    //Lista de instancias
    //private ArrayList instancias;
    
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
    private ExpedienteXMLHandler handlerExpediente;
    
    private SituacionXMLHandler handlerSituacion;
    
    private EntidadGeneradora entidadGeneradora;    

    private TaskMonitorDialog progressDialog;
    
    private Fichero fichero;
    
    private int n=0;
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    private boolean toImport = false;
    
    public FinRetornoXMLHandler (XMLReader parser, TaskMonitorDialog progressDialog, 
            Fichero fich, boolean toImport)
    {
        this.parser = parser;
        //this.instancias = v;
        this.progressDialog = progressDialog;
        this.fichero = fich;
        this.toImport = toImport;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {  
        valor = new StringBuffer();
        
        if (localName.equals("cabecera"))
        {
            entidadGeneradora = new EntidadGeneradora();
        }           
        else if (localName.equals("udsa")){
            blackboard.put("ListaBienes", new ArrayList());
            blackboard.put("ListaFincas", new ArrayList());
            
            //creamos la nueva instancia de UnidadDatosRetorno
            actual = new UnidadDatosRetorno ();
            //y la añadimos a la lista que las almacena
            //instancias.add (actual);
        }
        else if (localName.equals("exp")){
            //creamos una instancia de Expediente
            Expediente expediente = new Expediente (); 
            actual.setExpediente(expediente);
            handlerExpediente = new ExpedienteXMLHandler( parser, this, expediente, localName);
            parser.setContentHandler( handlerExpediente );
            if (expediente.getEntidadGeneradora()==null)
                expediente.setEntidadGeneradora(entidadGeneradora);
                        
        }
        else if (localName.equals("lelems")){
            
            //creamos una instancia de Expediente
            //ArrayList situaciones = new ArrayList();
            //actual.setLstSituaciones(situaciones);
           
            handlerSituacion = 
                new SituacionXMLHandler( parser, this, localName, progressDialog,
                        n, toImport);
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
        {
            if (fichero!=null)
                fichero.setCodigoEntidadGeneradora(new Integer(valor.toString()).intValue());
            entidadGeneradora.setCodigo(new Integer(valor.toString()).intValue());
        }
        
        else if(localName.equals("udsa"))
        {
            if (fichero!=null)
                fichero.setEntidadGeneradora(entidadGeneradora);
            
            DatosRegistroExpedientes regExp = new DatosRegistroExpedientes();
            regExp.setExpediente(actual.getExpediente());
            
            //TODO Parámetros que pueden venir vacios en el fichero fin
            //pero que se necesitan en catastro temporal
            if (actual.getExpediente().getIdMunicipio()==0)
                actual.getExpediente().setIdMunicipio(AppContext.getIdMunicipio());
            if (actual.getExpediente().getFechaAlteracion()==null)
                actual.getExpediente().setFechaAlteracion(new Date());
            if (actual.getExpediente().getIdEstado()==0)
                actual.getExpediente().setIdEstado(Expediente.SINCRONIZADO);
            
            regExp.setLstBienes((ArrayList)blackboard.get("ListaBienes"));
            regExp.setLstFincas((ArrayList)blackboard.get("ListaFincas"));
               
            ArrayList lstRegExp = (ArrayList)blackboard.get("ListaDatosRegistro");
            lstRegExp.add(regExp);
            blackboard.put("ListaDatosRegistro",lstRegExp);
            
        }
        else if (localName.equals("ffi") && fichero!=null)
        {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date date = (Date)formatter.parse(valor.toString());
                fichero.setFechaGeneracion(date);
                fichero.setFechaIntercambio(new Date());
            } catch (ParseException e)
            {   
                e.printStackTrace();
            }
        }
        else if (localName.equals("fip") && fichero!=null)
        {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date date = (Date)formatter.parse(valor.toString());
                fichero.setFechaInicioPeriodo(date);
            } catch (ParseException e)
            {   
                e.printStackTrace();
            }
        }
        else if (localName.equals("ffp") && fichero!=null)
        {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date date = (Date)formatter.parse(valor.toString());
                fichero.setFechaFinPeriodo(date);
            } catch (ParseException e)
            {   
                e.printStackTrace();
            }
        }
        else if (localName.equals("tfi") && fichero!=null)
          ;  
        
        else if (localName.equals("dfi") && fichero!=null)
            fichero.setDescripcion(valor.toString());
       
        else if (localName.equals("ced") && fichero!=null)
            fichero.setDescripcion(valor.toString());
       
    }
    
   
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
    public UnidadDatosRetorno getUnidadDatosRetorno()
    {
        return actual;
    }
    public Expediente getExpediente()
    {
        return actual.getExpediente();
    }
    
}

