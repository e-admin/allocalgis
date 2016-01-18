/**
 * RepartoXMLHandler.java
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
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;



public class RepartoXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //RepartoCatastro que se esta procesando
    private RepartoCatastro actual;
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
    private ExpedienteXMLHandler handlerExpediente = null;  
    private ElementoRepartoXMLHandler handlerElementoReparto = null; 
    Expediente exp = new Expediente();
    private boolean existeCspr;
    private boolean existeCcc;
    private boolean existeNoec;
    private String cspr;
    private String ccc;
    private String noec;
    private String pctrep;
    private String car;
    private boolean dentroIrepl;
    private String noecRepartido;
    private String etiqXMLorigen;
    
    private ArrayList lstBienes;
    private ArrayList lstLocales;
    
    private ElementoReparto elemento;
    
    public RepartoXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList lstReparto, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = lstReparto;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;      
                
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        if((localName.equals("reparf"))||(localName.equals("reparcs"))){
        	
        	this.actual = new RepartoCatastro();
            this.lstBienes = new ArrayList();
            this.lstLocales = new ArrayList();
            this.actual.setLstBienes(lstBienes);
            this.actual.setLstLocales(lstLocales); 
            instancias.add(actual);
            
        }        	
        
        if (localName.equals("irepl"))
            dentroIrepl = true;        
        
        else if (localName.equals("cspr")){
            existeCspr = true;              
        }
        else if (localName.equals("ccc")){
            existeCcc = true;              
        }
        else if (localName.equals("noec")){
            existeNoec = true;              
        }   
            
        
        else if (localName.equals("exp")){
            handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        
        
        if (((existeCspr && existeCcc) || existeNoec)){

        	if (localName.equals("lerep") ){

        		elemento = new ElementoReparto();
        		if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
        			//Expediente situaciones finales
        			handlerElementoReparto = new ElementoRepartoXMLHandler(parser, this, lstLocales, localName);
        		}
        		else{
        			handlerElementoReparto = new ElementoRepartoXMLHandler(parser, this, lstLocales, lstBienes, localName);
        		}
        		parser.setContentHandler(handlerElementoReparto);
        		elemento.setExpediente(exp);
        	}
        	else if (localName.equals("lcrep")){

        		elemento = new ElementoReparto();
        		if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
        			//Expediente situaciones finales
        			handlerElementoReparto = new ElementoRepartoXMLHandler(parser, this, lstBienes, localName);
        		}
        		else{
        			handlerElementoReparto = new ElementoRepartoXMLHandler(parser, this, lstLocales, lstBienes, localName);
        		}
        		
        		parser.setContentHandler(handlerElementoReparto);
        		elemento.setExpediente(exp);

        	}
        	
        }
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
    	if (localName.equals("pc1"))
            ref1=valor.toString();
        else if (localName.equals("pc2"))
            ref2=valor.toString();
        else if (localName.equals("cspr"))            
            cspr = valor.toString();
        else if (localName.equals("ccc"))            
            ccc = valor.toString();       
        else if (localName.equals("noec"))  
        {
            if (dentroIrepl){
                noecRepartido = valor.toString();                
            }
            else
                noec = valor.toString();
        }   
        else if (localName.equals("pctrep")) {           
            pctrep = valor.toString();            
        }
        else if (localName.equals("car")){            
            car = valor.toString();            
        }
                
        else if (localName.equals("ner"))
        {
            existeCcc = false;
            existeCspr = false;
            existeNoec = false;          
        }
        else if (localName.equals("cd"))
        	actual.setCodDelegacion(valor.toString());
        
        else if (localName.equals("cmc"))
        	actual.setCodMunicipio(valor.toString());
        
        else if (localName.equals("tmov"))
        	actual.setTIPO_MOVIMIENTO(valor.toString());
           
        else if (localName.equals("irepc"))
        	actual.setTipoReparto("AC");
        else if (localName.equals("irepl"))
        	actual.setTipoReparto("AL");
        
        if (((existeCspr && existeCcc) || existeNoec)
        		)
        {     
            
            if(noec != null){
                actual.setNumOrdenConsRepartir(noec);
            }
            if (noecRepartido !=null)
            {                
                if (ref1!=null && ref2!=null && !ref1.trim().equals("") && !ref2.trim().equals("") )
                	elemento.setId(ref1 + ref2 + noecRepartido);
                lstLocales.add(elemento);
                dentroIrepl = false;
                noecRepartido = null;
            }                    
            
            if (cspr != null){
                actual.setCodSubparcelaElementoRepartir(cspr);
            }
            if (ccc != null)
                actual.setCalifCatastralElementoRepartir(ccc);
            if (car != null){
                
                if (ref1!=null && ref2!=null && !ref1.trim().equals("") && !ref2.trim().equals("") )
                	elemento.setId(ref1 + ref2 + car);
                lstBienes.add(elemento);
                car = null;
            }
            
        
            if (ref1!=null && ref2!=null && !ref1.trim().equals("") && !ref2.trim().equals("") )
            {                                
                actual.setIdOrigen(new ReferenciaCatastral(ref1,ref2));
            }
        
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
