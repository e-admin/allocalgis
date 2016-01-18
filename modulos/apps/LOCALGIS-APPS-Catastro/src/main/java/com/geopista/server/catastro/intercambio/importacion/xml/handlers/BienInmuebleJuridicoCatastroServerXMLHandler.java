/**
 * BienInmuebleJuridicoCatastroServerXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;


public class BienInmuebleJuridicoCatastroServerXMLHandler extends DefaultHandler 
{  
    //vector de instancias
    private ArrayList instancias;
    //BienInmuebleJuridico que se esta procesando
    private BienInmuebleJuridico actual;
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //Como queremos pasar el control del proceso de un
    //Handler a otro, necesitamos tener el parser para
    //asignarle el Handler que necesite en cada instante.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    
    private BienInmuebleCatastroServerXMLHandler handlerBien;
    private InfoTributariaCatastroServerXMLHandler handlerInfoTributaria;
    private TitularCatastroServerXMLHandler handlerTitular;
    private ExpedienteCatastroServerXMLHandler handlerExpediente;
	//private RepresentanteXMLHandler handlerRepresentante;
    private ComunidadBienesCatastroServerXMLHandler handlerCB;
    
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();

    BienInmuebleCatastro biCat = null;

    private String etiqXMLorigen;
    
    public BienInmuebleJuridicoCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
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
        
        //comprobamos si empezamos un elemento bij
        if (localName.equals("bij") || localName.equals("bicreg")
               || localName.equals("bicf")){
            //creamos la nueva instancia 
            actual = new BienInmuebleJuridico ();
            //y la añadimos al Vector qie las almacena
            instancias.add(actual);
        }
        
        //Bien inmueble
        else if (localName.equals("bi") || localName.equals("idreg"))
        {
            
            biCat = new BienInmuebleCatastro();
            actual.setBienInmueble(biCat);
            handlerBien = new BienInmuebleCatastroServerXMLHandler( parser, this, biCat, localName);
            parser.setContentHandler( handlerBien );
        }

        /*
        //Este cacho habra q hacerlo en la parte de InfoTributariaXMLHandler pasandole el bien bicat
        //Representante o sujeto pasivo
        else if (localName.equals("sft") && biCat !=null){
            
            Representante repres = new Representante();
            biCat.setRepresentante(repres);
            handlerRepresentante = new RepresentanteXMLHandler( parser, this, repres);
            parser.setContentHandler( handlerRepresentante ); 
        }
        */
        //Información tributaria
        else if (localName.equals("inft")){
            
            //InformacionTributaria infoTrib = new InformacionTributaria();
            //actual.setInfoTributaria(infoTrib);
            handlerInfoTributaria = new InfoTributariaCatastroServerXMLHandler(parser, this, biCat, localName);
            parser.setContentHandler( handlerInfoTributaria ); 
        }
        
        //Expediente fisico-económico
        else if (localName.equals("exp")){
            
            Expediente expediente = new Expediente();
            actual.setDatosExpediente(expediente);
            handlerExpediente = new ExpedienteCatastroServerXMLHandler( parser, this, expediente, localName);
            parser.setContentHandler( handlerExpediente );
        }
        
        //Lista de titulares
        else if (localName.equals("ltit")||localName.equals("lsf")){
            
            ArrayList lstTitulares = new ArrayList();
            ArrayList lstComBienes = new ArrayList();
            actual.setLstTitulares(lstTitulares); 
            actual.setLstComBienes(lstComBienes);
            handlerTitular = new TitularCatastroServerXMLHandler( parser, this, lstTitulares, lstComBienes, localName);
            parser.setContentHandler( handlerTitular );
        }
        
        
        //Lista de comunidades de bienes
        else if (localName.equals("lcbi")){
            
            ArrayList lstComBienes = new ArrayList();
            actual.setLstComBienes(lstComBienes);            
            handlerCB = new ComunidadBienesCatastroServerXMLHandler( parser, this, lstComBienes, localName);
            parser.setContentHandler( handlerCB );
        }
        
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        if (localName.equals(etiqXMLorigen))
        {
            Iterator it = instancias.iterator();
            ArrayList lstIdBienes = new ArrayList();
            while (it.hasNext())
            {
                Object o = it.next();
                if (o instanceof BienInmuebleJuridico)
                    lstIdBienes.add(((BienInmuebleJuridico)o).getBienInmueble().getIdBienInmueble());
            }
            blackboard.put("ListaBienes", lstIdBienes);
            
            
            parser.setContentHandler (handlerToReturn);
        }
         //JAVIER
        else if(localName.equals("tmov")){
            biCat.setTipoMovimiento(valor.toString());
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
