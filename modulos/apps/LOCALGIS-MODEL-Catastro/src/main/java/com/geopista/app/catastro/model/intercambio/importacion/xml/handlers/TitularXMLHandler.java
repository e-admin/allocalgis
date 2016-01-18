/**
 * TitularXMLHandler.java
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
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Titular;


public class TitularXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    private ArrayList instancias2;
    //Titular que se esta procesando
    private Titular actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    private ExpedienteXMLHandler handlerExpediente;
    private DireccionLocalizacionXMLHandler handlerDireccion;
    private String etiqXMLorigen;
    
    private boolean inConyuge = false;
   
    
    public TitularXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public TitularXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList v, ArrayList w, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;
        this.instancias2 = w;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        //comprobamos si empezamos un elemento sft
        if (//localName.equals("sft") ||
        		localName.equals("tit")){
            //creamos la nueva instancia 
            actual = new Titular ();
            
            
            if (handlerToReturn instanceof BienInmuebleJuridicoXMLHandler)
            {
            	actual.setBienInmueble(((BienInmuebleJuridicoXMLHandler)handlerToReturn).biCat);
            	actual.getDerecho().setIdBienInmueble(
            			((BienInmuebleJuridicoXMLHandler)handlerToReturn).biCat.getIdBienInmueble());
            }
            
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }      
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            actual.setExpediente(exp);
            handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("df")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            actual.setDomicilio(dir);
            handlerDireccion = new DireccionLocalizacionXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
        else if (localName.equals("cony"))
            inConyuge  = true;
      //Lista de comunidades de bienes
        else if (localName.equals("lcbi")){
                        
        	ComunidadBienesXMLHandler handlerCB = new ComunidadBienesXMLHandler( parser, handlerToReturn, this.instancias2, localName);
            parser.setContentHandler( handlerCB );
        }
  
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        //der
        if (localName.equals("cdr"))
            actual.getDerecho().setCodDerecho(valor.toString());        
        else if (localName.equals("pct"))
            actual.getDerecho().setPorcentajeDerecho(ImportarUtils.strToFloat(valor.toString().replaceAll(",",".")));
        
        //ord
        else if (localName.equals("ord"))
            actual.getDerecho().setOrdinalDerecho(Integer.parseInt(valor.toString()));
       
        //idp - idpa - idps - idp_out
        else if (localName.equals("nif") || localName.equals("cii"))
            actual.setNif(valor.toString());    
        else if (localName.equals("anif"))
            actual.setAusenciaNIF(valor.toString());
        else if (localName.equals("nom"))
            actual.setRazonSocial(valor.toString());  
        
        //lder
        //else if (localName.equals("lder"))
        //    actual.getDerecho().setLiteralDerecho(valor);
        
        
        //ldef
        else if (localName.equals("ldef"))
            actual.getDomicilio().setDireccionNoEstructurada(valor.toString());
        
        //cony
        else if (inConyuge && localName.equals("nif"))
        {
            actual.setNifConyuge(valor.toString());
            inConyuge = false;
        }
         
        //idcbf - inforación de la comunidad de bienes
        else if (localName.equals("nifcb"))
            actual.setNifCb(valor.toString());
        else if (localName.equals("nomcb"))
            actual.setRazonSocial(valor.toString());
        
        
        //iatit - información adicional de titularidad
        else if (localName.equals("nifcy"))
            actual.setNifConyuge(valor.toString());
        else if (localName.equals("nifcb"))
            actual.setNif(valor.toString());
        else if (localName.equals("ct"))
            actual.setComplementoTitularidad(valor.toString());
        
        
        //rtit - rango de titularidad
        //else if (localName.equals("fit"))
        //    actual.setFechaInicioTitularidad(valor);
        //else if (localName.equals("fft"))
        //    actual.setFechaFinTitularidad(valor);
        
        //faj
        else if (localName.equals("faj"))
            actual.setFechaAlteracion(valor.toString());
        
        else if (localName.equals("mov")){
            actual.setTIPO_MOVIMIENTO(valor.toString());
            if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
            	//expediente de variaciones
            	if(actual.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
        				actual.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF)){
        			actual.setElementoModificado(true);
        		}
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
