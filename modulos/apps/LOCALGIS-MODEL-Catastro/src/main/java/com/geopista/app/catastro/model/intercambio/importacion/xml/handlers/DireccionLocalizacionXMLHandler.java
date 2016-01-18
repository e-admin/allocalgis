/**
 * DireccionLocalizacionXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.intercambio.importacion.xml.handlers;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;

public class DireccionLocalizacionXMLHandler extends DefaultHandler 
{
    /**
     * Valor contenido entre las etiquetas de un elemento
     */
    private StringBuffer valor = new StringBuffer();
    
    /**
     * El parser, para luego devolver el control al Handler del que se proviene
     */
    private XMLReader parser;
    
    /**
     * El Handler al que queremos volver
     */
    private DefaultHandler handlerToReturn;
    
    /**
     * La DireccionLocalizacion donde meter los datos leidos
     */
    private DireccionLocalizacion dirLocalizacion;

    /**
     * Inicio de la etiqueta XML que indica que se trata de una dirección de localización
     */
    private String etiqXMLorigen;
    
    public DireccionLocalizacionXMLHandler (XMLReader parser, DefaultHandler handler,
            DireccionLocalizacion dirLocalizacion, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.dirLocalizacion = dirLocalizacion;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {   
        //loine
        if (localName.equals("cp"))
            dirLocalizacion.setProvinciaINE(valor.toString());
        else if (localName.equals("cm"))
            dirLocalizacion.setMunicipioINE(valor.toString());
        
        //cmc
        else if (localName.equals("cmc"))
            dirLocalizacion.setCodigoMunicipioDGC(valor.toString());
        //np
        else if (localName.equals("np"))
            dirLocalizacion.setNombreProvincia(valor.toString());
        //nm
        else if (localName.equals("nm"))
            dirLocalizacion.setNombreMunicipio(valor.toString());
        //nem
        else if (localName.equals("nem"))
            dirLocalizacion.setNombreEntidadMenor(valor.toString());
        
        //lourb
          //dir
        //TODO: cv es "([0-9]|NOAPL)+", el NOAPL no está contemplado!
        else if (localName.equals("cv"))
            dirLocalizacion.setCodigoVia(Integer.parseInt(valor.toString()));
        else if (localName.equals("tv"))
            dirLocalizacion.setTipoVia(valor.toString());
        else if (localName.equals("nv"))
           dirLocalizacion.setNombreVia(valor.toString());
        else if (localName.equals("pnp"))
           dirLocalizacion.setPrimerNumero(Integer.parseInt(valor.toString()));
        else if (localName.equals("plp"))
           dirLocalizacion.setPrimeraLetra(valor.toString());
        else if (localName.equals("snp"))
           dirLocalizacion.setSegundoNumero(Integer.parseInt(valor.toString()));
        else if (localName.equals("slp"))
           dirLocalizacion.setSegundaLetra(valor.toString());
        else if (localName.equals("km"))
           dirLocalizacion.setKilometro(ImportarUtils.strToDouble(valor.toString().replaceAll(",",".")));
        else if (localName.equals("td"))
           dirLocalizacion.setDireccionNoEstructurada(valor.toString());
        
          //bl
        else if (localName.equals("bl"))
           dirLocalizacion.setBloque(valor.toString());
        
          //loint
        else if (localName.equals("bq"))
           dirLocalizacion.setBloque(valor.toString());
        else if (localName.equals("es"))
           dirLocalizacion.setEscalera(valor.toString());
        else if (localName.equals("pt"))
           dirLocalizacion.setPlanta(valor.toString());
        else if (localName.equals("pu"))
           dirLocalizacion.setPuerta(valor.toString());
          
          //dp
        else if (localName.equals("dp"))
           dirLocalizacion.setCodigoPostal(valor.toString());
          //dm
        else if (localName.equals("dm"))
           dirLocalizacion.setDistrito(valor.toString());
        
       
        
        //lorus
        else if (localName.equals("cma"))
            dirLocalizacion.setCodMunOrigenAgregacion(valor.toString());        
        else if (localName.equals("czc"))
            dirLocalizacion.setCodZonaConcentracion(valor.toString());
        else if (localName.equals("npa"))
            dirLocalizacion.setNombreParaje(valor.toString());
        else if (localName.equals("cpaj"))
            dirLocalizacion.setCodParaje(valor.toString());
        else if (localName.equals("cpo"))
            dirLocalizacion.setCodPoligono(valor.toString());
        else if (localName.equals("cpa"))
            dirLocalizacion.setCodParcela(valor.toString());        
        
        
       
        //si hemos llegado al final de la etiqueta que creo el objeto DireccionLocalizacion
        else if (localName.equals(etiqXMLorigen)){
            parser.setContentHandler (handlerToReturn);
        }
       
    }
    
    
    /*
     Los parametros que recibe es la localizacion de los carateres del elemento.
     */
    public void characters (char[] ch, int start, int end) throws SAXException
    {
        //creamos un String con los caracteres del elemento y le quitamos 
        //los espacios en blanco que pueda tener en los extremos.
        valor.append(new String (ch, start, end).trim()); 
    }
    
}