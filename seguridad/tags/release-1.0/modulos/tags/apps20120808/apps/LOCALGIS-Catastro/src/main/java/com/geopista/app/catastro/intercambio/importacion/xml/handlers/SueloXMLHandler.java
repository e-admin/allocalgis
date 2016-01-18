package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;


public class SueloXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //SituacionCatastral que se esta procesando
    private SueloCatastro actual;
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
    String numOrden = new String();
    private ExpedienteXMLHandler handlerExpediente;
    private String etiqXMLorigen;
    
    public SueloXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
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
        valor = new StringBuffer ();
                
        //comprobamos si empezamos un elemento suelocs o suelocf
        if (localName.equals("suelocs") || localName.equals("suelocf")){
            //creamos la nueva instancia 
            actual = new SueloCatastro ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }   
        else if (localName.equals("exp")){
                
                Expediente exp = new Expediente();
                actual.setDatosExpediente(exp);
                handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
                parser.setContentHandler( handlerExpediente );
            }   
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        //idsu
        if (localName.equals("pc1"))
            ref1=valor.toString();
        else if (localName.equals("pc2"))
            ref2=valor.toString();
        else if (localName.equals("subp")){
            numOrden=valor.toString();
            actual.setNumOrden(valor.toString());
        }
        else if (localName.equals("cd"))
        	actual.setCodDelegacion(valor.toString());
        else if (localName.equals("cmc"))
        	actual.setCodMunicipioDGC(valor.toString());
                    
        //dfsu
        else if (localName.equals("tipof"))
            actual.getDatosFisicos().setTipoFachada(valor.toString());
        else if (localName.equals("long"))
            //actual.getDatosFisicos().setLongFachada(Integer.valueOf(valor.toString()));
        	actual.getDatosFisicos().setLongFachada(Float.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("fondo"))
            actual.getDatosFisicos().setFondo(Integer.valueOf(valor.toString()));       
        else if (localName.equals("sup"))
            actual.getDatosFisicos().setSupOcupada(Long.valueOf(valor.toString()));
         
        //dvs
        else if (localName.equals("cvpv"))
            actual.getDatosEconomicos().setCodViaPonencia(valor.toString());
        else if (localName.equals("ctvpv")){
        	PonenciaTramos tramo = new PonenciaTramos();
        	tramo.setCodTramo(valor.toString());
            actual.getDatosEconomicos().setCodTramoPonencia(valor.toString());
            actual.getDatosEconomicos().setTramos(tramo);
        }
        else if (localName.equals("zv"))
            actual.getDatosEconomicos().getZonaValor().setCodZonaValor(valor.toString());
        else if (localName.equals("zu"))
            actual.getDatosEconomicos().getZonaUrbanistica().setCodZona(valor.toString());
        else if (localName.equals("ctv"))
            actual.getDatosEconomicos().setCodTipoValor(valor.toString());       
        
        //ccvs coeficientes corrextorse del valor del suelo
        else if (localName.equals("nfach"))
            actual.getDatosEconomicos().setNumFachadas(String.valueOf(valor.toString()));  
        else if (localName.equals("ilf"))
            actual.getDatosEconomicos().setCorrectorLongFachada(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("ifir"))
            actual.getDatosEconomicos().setCorrectorFormaIrregular(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("ide"))
            actual.getDatosEconomicos().setCorrectorDesmonte(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("isdm"))
            actual.getDatosEconomicos().setCorrectorSupDistinta(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("iit"))
            actual.getDatosEconomicos().setCorrectorInedificabilidad(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("ivpp"))
            actual.getDatosEconomicos().setCorrectorVPO(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
       
        //cccsc (coeficientes correctores conjuntos del valor del suelo y las construcciones) 
        else if (localName.equals("vccad"))
            actual.getDatosEconomicos().setCorrectorAprecDeprec(Float.valueOf(valor.toString().replaceAll(",",".")));  
        else if (localName.equals("iadf"))
            actual.getDatosEconomicos().setCorrectorDeprecFuncional(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("vcccs"))
            actual.getDatosEconomicos().setCorrectorCargasSingulares(Float.valueOf(valor.toString().replaceAll(",",".")));  
       
        else if (localName.equals("icce"))
            actual.getDatosEconomicos().setCorrectorSitEspeciales(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));  
        else if (localName.equals("iccunl"))
            actual.getDatosEconomicos().setCorrectorNoLucrativo(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true)); 
       
        
        
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
        
        if (ref1!=null && ref2!=null && numOrden!=null 
                && !ref1.trim().equals("") && !ref2.trim().equals("") && !numOrden.trim().equals(""))
        {
            actual.setIdSuelo(ref1+ref2+numOrden);
            ReferenciaCatastral refCat = new ReferenciaCatastral(ref1, ref2);
            actual.setRefParcela(refCat);
            ref1=null;
            ref2=null;
            numOrden=null;
        }
        
    }    
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
