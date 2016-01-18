/**
 * CreacionExpedientesResponseXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.CreacionExpedientesResponse;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.intercambio.importacion.xml.handlers.ExpedienteXMLHandler;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class CreacionExpedientesResponseXMLHandler extends DefaultHandler  {

	//Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
	
	private TaskMonitorDialog progressDialog;
    
    private CreacionExpedientesResponse creacionExpedientesResponse;
    
    private boolean toImport = false;
    
  //Este es el Handler que procesara los expedientes que 
    //contenga el documento
    private ExpedienteXMLHandler handlerExpediente;
    
  //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    private EntidadGeneradora entidadGeneradora; 
    
    public CreacionExpedientesResponseXMLHandler ()
    {
    }
	
	public CreacionExpedientesResponseXMLHandler (XMLReader parser, TaskMonitorDialog progressDialog,  
			CreacionExpedientesResponse creacionExpedientesResponse, boolean toImport)
    {
		
        this.parser = parser;
        this.creacionExpedientesResponse = creacionExpedientesResponse;
        this.progressDialog = progressDialog;
        this.toImport = toImport;
    }
	
	 public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
	    throws SAXException 
	    {  
	        valor = new StringBuffer();
	        
	        if (localName.equals("CreacionExpedientesResponse"))
	        {
	        	creacionExpedientesResponse = new CreacionExpedientesResponse();
	        } 
	        else if (localName.equals("control"))
	        {
	            entidadGeneradora = new EntidadGeneradora();
	        }           
	        else if (localName.equals("exp")){
	            //creamos una instancia de Expediente
	            Expediente expediente = new Expediente (); 

	            handlerExpediente = new ExpedienteXMLHandler( parser, this, expediente, localName);
	            parser.setContentHandler( handlerExpediente );
	            if (expediente.getEntidadGeneradora()==null)
	                expediente.setEntidadGeneradora(entidadGeneradora);
	                        
	        }
	       
	    }
	 
	 
	 public void endElement (String namespaceURI, String localName, String rawName)
	    throws SAXException
	    {
		 	
		 	if (localName.equals("control"))
		 		creacionExpedientesResponse.setEntidadGeneradora(entidadGeneradora);
		 	else if (localName.equals("teg"))
	            entidadGeneradora.setTipo(valor.toString());
	        else if (localName.equals("neg"))
	            entidadGeneradora.setNombre(valor.toString());
	        else if (localName.equals("cd"))
	        {
	            if (creacionExpedientesResponse!=null)
	                creacionExpedientesResponse.setCodigoEntidadGeneradora(new Integer(valor.toString()).intValue());
	            entidadGeneradora.setCodigo(new Integer(valor.toString()).intValue());
	        }
	        else if (localName.equals("ffi"))
	        {
	            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	            try
	            {
	                Date date = (Date)formatter.parse(valor.toString());
	                creacionExpedientesResponse.setFechaGeneracion(date);
	            } catch (ParseException e)
	            {   
	                e.printStackTrace();
	            }
	        }
	        else if (localName.equals("hfi"))
	        {
	            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	            try
	            {
	                Date date = (Date)formatter.parse(valor.toString());
	                creacionExpedientesResponse.setFechaGeneracion(date);
	            } catch (ParseException e)
	            {   
	                e.printStackTrace();
	            }
	        }
	       
	        else if (localName.equals("tfi"))
	        	creacionExpedientesResponse.setTipo(valor.toString());  
	        

	        else if (localName.equals("ced"))
	            creacionExpedientesResponse.setCodigoEnvio(Integer.valueOf(valor.toString()));
	       
	    }
	    
	   
	 
	 
}
