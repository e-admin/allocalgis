package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Expediente;


public class CultivoCatastroServerXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //Cultivo que se esta procesando
    private Cultivo actual;
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
    
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
    private String etiqXMLorigen;
    
    public CultivoCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn,
    		ArrayList v, String etiqXMLorigen)
    {
        this.parser = parser;
        this.instancias = v;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        //comprobamos si empezamos un elemento sprcs o sprcf
        if (localName.equals("sprcs")
                || localName.equals("sprcf")){
            //creamos la nueva instancia 
            actual = new Cultivo ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }  
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            actual.setDatosExpediente(exp);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
       
        //idspr
        if (localName.equals("pc1"))
            ref1=valor.toString();
        else if (localName.equals("pc2"))
            ref2=valor.toString();
               
        else if (localName.equals("cn"))
            actual.setTipoSuelo(valor.toString());
        else if (localName.equals("cspr"))
            actual.setCodSubparcela(valor.toString());        
        else if (localName.equals("ccc"))        
            actual.getIdCultivo().setCalifCultivo(valor.toString());
           
            
        
        else if (localName.equals("cd"))
            actual.setCodDelegacionMEH(valor.toString());
        else if (localName.equals("cmc"))
            actual.setCodMunicipioDGC(valor.toString());
        
        //dspr: (datos de subparcela) 
        else if (localName.equals("car"))
            actual.getIdCultivo().setNumOrden(valor.toString());
        
        
        else if (localName.equals("modl"))
            actual.setCodModalidadReparto(valor.toString());
        else if (localName.equals("tspr"))
            actual.setTipoSubparcela(valor.toString());
        else if (localName.equals("dcc"))
            actual.setDenominacionCultivo(valor.toString());
        else if (localName.equals("ip"))
            actual.setIntensidadProductiva(Integer.valueOf(valor.toString()));
        else if (localName.equals("ssp"))
            actual.setSuperficieParcela(Long.valueOf(valor.toString()));        
        else if (localName.equals("vsp"))
            actual.setValorCatastral(Double.valueOf(valor.toString().replaceAll(",", ".")));
                
        //bspr
        else if (localName.equals("cbspr"))
            actual.setCodBonificacion(valor.toString());   
        else if (localName.equals("efb"))
            actual.setEjercicioFinBonificacion(Integer.valueOf(valor.toString()));   
        
        
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
        
        if (ref1!=null && ref2!=null 
                && !ref1.trim().equals("") && !ref2.trim().equals("") )
        {
            actual.getIdCultivo().setParcelaCatastral(ref1+ref2);
            
            ref1=null;
            ref2=null;
        }             
        
    }   
    
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
