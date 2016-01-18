/**
 * UnidadConstructivaCatastroServerXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;


public class UnidadConstructivaCatastroServerXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //SituacionCatastral que se esta procesando
    private UnidadConstructivaCatastro actual;
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
    String numUC = new String();
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
    private DireccionLocalizacionCatastroServerXMLHandler handlerDireccion;
    private String etiqXMLorigen;
    
    public UnidadConstructivaCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn,
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
        
        //comprobamos si empezamos un elemento uccs o uccf
        if (localName.equals("uccs") || localName.equals("uccf")){
            //creamos la nueva instancia 
            actual = new UnidadConstructivaCatastro ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }      
        else if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            actual.setDatosExpediente(exp);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("dt")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            actual.setDirUnidadConstructiva(dir);
            handlerDireccion = new DireccionLocalizacionCatastroServerXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
       
        //iducat
        if (localName.equals("pc1"))
            ref1=valor.toString();
        else if (localName.equals("pc2"))
            ref2=valor.toString();
        else if (localName.equals("cuc")){
            numUC=valor.toString();
            actual.setCodUnidadConstructiva(valor.toString());
        }
        else if (localName.equals("cn"))
            actual.setTipoUnidad(valor.toString());
        
        else if (localName.equals("cd"))
            actual.setCodDelegacionMEH(valor.toString());
        else if (localName.equals("cmc"))
            actual.setCodMunicipioDGC(valor.toString());
          
        
        //dfuc
        else if (localName.equals("ac"))
            actual.getDatosFisicos().setAnioConstruccion(Integer.valueOf(valor.toString()));
        else if (localName.equals("iacons"))
            actual.getDatosFisicos().setIndExactitud(valor.toString());
        else if (localName.equals("so"))
            actual.getDatosFisicos().setSupOcupada(Long.valueOf(valor.toString()));
        else if (localName.equals("lf"))
            actual.getDatosFisicos().setLongFachada(Float.valueOf(valor.toString().replaceAll(",",".")));
        
        
        //dvuc
        //   fpon
        else if (localName.equals("cvpv"))
            actual.getDatosEconomicos().setCodViaPonencia(valor.toString());
        else if (localName.equals("ctvpv"))
        {
            PonenciaTramos pt = new PonenciaTramos();
            pt.setCodTramo(valor.toString());
            actual.getDatosEconomicos().setTramoPonencia(pt);
        }
        else if (localName.equals("zv"))
        {
            PonenciaZonaValor pz = new PonenciaZonaValor();
            pz.setCodZonaValor(valor.toString());
            actual.getDatosEconomicos().setZonaValor(pz);
        }
        
        //   ccvsuc
        else if (localName.equals("nf"))
            actual.getDatosEconomicos().setNumFachadas(valor.toString());
        else if (localName.equals("ilf"))
            actual.getDatosEconomicos().setCorrectorLongFachada(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
        //   ccvuc
        else if (localName.equals("ccec"))
            actual.getDatosEconomicos().setCorrectorConservacion(valor.toString());
        //ccvscuc
        else if (localName.equals("iccdf"))
            actual.getDatosEconomicos().setCorrectorDepreciacion(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
        else if (localName.equals("vcccs"))
            actual.getDatosEconomicos().setCoefCargasSingulares(Float.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("iccsce"))
            actual.getDatosEconomicos().setCorrectorSitEspeciales(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
        else if (localName.equals("iccunl"))
            actual.getDatosEconomicos().setCorrectorNoLucrativo(valor.toString().equalsIgnoreCase("N")? new Boolean(false):new Boolean(true));
        
                
        
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
        
        if (ref1!=null && ref2!=null && numUC!=null 
                && !ref1.trim().equals("") && !ref2.trim().equals("") && !numUC.trim().equals(""))
        {
            actual.setIdUnidadConstructiva(ref1+ref2+numUC);
            ReferenciaCatastral refCat = new ReferenciaCatastral(ref1, ref2);
            actual.setRefParcela(refCat);
            ref1=null;
            ref2=null;
            numUC=null;
        }
        
    }
    
    
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}
