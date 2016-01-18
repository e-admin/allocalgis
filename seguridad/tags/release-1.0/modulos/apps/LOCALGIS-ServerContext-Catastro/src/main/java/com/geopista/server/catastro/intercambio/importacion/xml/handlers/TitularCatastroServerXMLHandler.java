package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.server.catastro.intercambio.importacion.utils.ImportarUtilsServerCatastro;


public class TitularCatastroServerXMLHandler extends DefaultHandler 
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
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
    private DireccionLocalizacionCatastroServerXMLHandler handlerDireccion;
    private String etiqXMLorigen;
    
    private boolean inConyuge = false;
   
    
    public TitularCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public TitularCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
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
            
            
            if (handlerToReturn instanceof BienInmuebleJuridicoCatastroServerXMLHandler)
            {
            	actual.setBienInmueble(((BienInmuebleJuridicoCatastroServerXMLHandler)handlerToReturn).biCat);
            	actual.getDerecho().setIdBienInmueble(
            			((BienInmuebleJuridicoCatastroServerXMLHandler)handlerToReturn).biCat.getIdBienInmueble());
            }
            
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }      
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            actual.setExpediente(exp);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("df")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            actual.setDomicilio(dir);
            handlerDireccion = new DireccionLocalizacionCatastroServerXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
        else if (localName.equals("cony"))
            inConyuge  = true;
      //Lista de comunidades de bienes
        else if (localName.equals("lcbi")){
                        
        	ComunidadBienesCatastroServerXMLHandler handlerCB = new ComunidadBienesCatastroServerXMLHandler( parser, handlerToReturn, this.instancias2, localName);
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
            actual.getDerecho().setPorcentajeDerecho(ImportarUtilsServerCatastro.strToFloat(valor.toString().replaceAll(",",".")));
        
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
