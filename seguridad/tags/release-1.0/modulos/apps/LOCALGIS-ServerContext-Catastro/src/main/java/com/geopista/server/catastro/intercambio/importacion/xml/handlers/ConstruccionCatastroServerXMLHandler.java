package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;


public class ConstruccionCatastroServerXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //ConstruccionCatastro que se esta procesando
    private ConstruccionCatastro actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
    
    String ref1 = new String();
    String ref2 = new String();
    String numOrden = new String();
    private DireccionLocalizacionCatastroServerXMLHandler handlerDireccion;
    private String etiqXMLorigen;
    
    public ConstruccionCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, ArrayList v, String etiqXMLorigen)
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
        
        //comprobamos si empezamos un elemento conscs o cons
        if (localName.equals("conscs") || localName.equals("cons")
                || localName.equals("conscf")){
            //creamos la nueva instancia 
            actual = new ConstruccionCatastro ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }    
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            actual.setDatosExpediente(exp);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("dt")
                || localName.equals("dte")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            actual.setDomicilioTributario(dir);
            handlerDireccion = new DireccionLocalizacionCatastroServerXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {       
        //idconscat
            if (localName.equals("pc1"))
                ref1=valor.toString();
            else if (localName.equals("pc2"))
                ref2=valor.toString();
            else if (localName.equals("noec")){
                numOrden=valor.toString();
                actual.setNumOrdenConstruccion(valor.toString());
            }
            
            else if (localName.equals("cd"))
                actual.setCodDelegacionMEH(valor.toString());
            else if (localName.equals("cmc"))
                actual.setCodMunicipio(valor.toString());
            
            //Literal del código de destino (uso del elemento constructivo)
            else if (localName.equals("lcd"))
                actual.getDatosEconomicos().setCodUsoPredominante(valor.toString());
            
            
            
            //dfcons
            else if (localName.equals("cuc"))
                actual.getDatosFisicos().setCodUnidadConstructiva(valor.toString());
            
                //cargo
            else if (localName.equals("car"))
                actual.setNumOrdenBienInmueble(valor.toString());
                //modalidad del reparto
            else if (localName.equals("modl"))
                actual.getDatosEconomicos().setCodModalidadReparto(valor.toString());
            
            else if (localName.equals("cdes"))
                actual.getDatosFisicos().setCodDestino(valor.toString());
            
            //refor/tr
            else if (localName.equals("tr"))
                actual.getDatosFisicos().setTipoReforma(valor.toString());
            //refor/ar
            else if (localName.equals("ar"))
                actual.getDatosFisicos().setAnioReforma(Integer.valueOf(valor.toString()));
            
            else if (localName.equals("aec"))
                actual.getDatosFisicos().setAnioAntiguedad(Integer.valueOf(valor.toString()));
            
            else if (localName.equals("ili"))
                actual.getDatosFisicos().setLocalInterior(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
            
            else if (localName.equals("stl"))
                actual.getDatosFisicos().setSupTotal(Long.valueOf(valor.toString()));
            else if (localName.equals("spt"))
                actual.getDatosFisicos().setSupTerrazasLocal(Long.valueOf(valor.toString()));
            else if (localName.equals("sil"))
                actual.getDatosFisicos().setSupImputableLocal(Long.valueOf(valor.toString()));
            
            //dvcons   - Datos de valoración de la construcción          
            else if (localName.equals("tip"))
                actual.getDatosEconomicos().setTipoConstruccion(valor.toString());
            else if (localName.equals("usop"))
                actual.getDatosEconomicos().setCodUsoPredominante(valor.toString());
            else if (localName.equals("cat"))
                actual.getDatosEconomicos().setCodCategoriaPredominante(valor.toString());
            else if (localName.equals("ctv"))
                actual.getDatosEconomicos().setCodTipoValor(valor.toString());
            
                //ccvscc - Coeficientes correctores conjuntos del valor del suelo y las construcciones
            else if (localName.equals("vccad"))
                actual.getDatosEconomicos().setCorrectorApreciación(Float.valueOf(valor.toString().replaceAll(",",".")));
            else if (localName.equals("iacli"))
                actual.getDatosEconomicos().setCorrectorVivienda(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
            
            
            else if (localName.equals(etiqXMLorigen)){
                parser.setContentHandler (handlerToReturn);
            }
            
            if (ref1!=null && ref2!=null && numOrden!=null 
                    && !ref1.trim().equals("") && !ref2.trim().equals("") && !numOrden.trim().equals(""))
            {
            	//if((numOrden!=null)&&!(numOrden.trim().equals("")))
            		actual.setIdConstruccion(ref1+ref2+numOrden);
            	
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
