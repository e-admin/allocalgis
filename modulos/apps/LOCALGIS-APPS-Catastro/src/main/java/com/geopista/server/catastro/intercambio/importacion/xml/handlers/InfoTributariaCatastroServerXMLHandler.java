/**
 * InfoTributariaCatastroServerXMLHandler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.intercambio.importacion.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Persona;


public class InfoTributariaCatastroServerXMLHandler extends DefaultHandler 
{  
	
	//valor contenido entre las etiquetas de un elemento
	private StringBuffer valor = new StringBuffer();
	
	//Como queremos pasar el control del proceso de un
	//Handler a otro, necesitamos tener el parser para
	//asignarle el Handler que necesite en cada instante.
	private XMLReader parser;
	//El Handler al que queremos volver
	private DefaultHandler handlerToReturn;
	
	private BienInmuebleCatastro biCat;
	private RepresentanteCatastroServerXMLHandler handlerRepresentante;
	private String etiqXMLorigen;
	
	
	public InfoTributariaCatastroServerXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
			BienInmuebleCatastro biCat, String etiqXMLorigen)
	{
		this.parser = parser;
		this.handlerToReturn = handlerToReturn;		
		this.biCat = biCat;
		this.etiqXMLorigen = etiqXMLorigen;
	}
	
	
	public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
	throws SAXException 
	{
		valor = new StringBuffer();
        
		//Representante 
		if (localName.equals("rep")&& biCat !=null){
			
			Persona repres = new Persona();
			biCat.setRepresentante(repres);
			repres.setBienInmueble(biCat);
			handlerRepresentante = new RepresentanteCatastroServerXMLHandler( parser, this, repres, localName);
			parser.setContentHandler( handlerRepresentante ); 
		}
	}
	
	
	public void endElement (String namespaceURI, String localName, String rawName)
	throws SAXException
	{       
		
        //dcbl
        if (localName.equals("vb"))
            biCat.getDatosBaseLiquidable().setValorBase(Double.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("cvb"))
            biCat.getDatosBaseLiquidable().setProcedenciaValorBase(valor.toString());
        else if (localName.equals("evcr"))
            biCat.getDatosBaseLiquidable().setEjercicioIBI(Integer.valueOf(valor.toString()));
        else if (localName.equals("vcr"))
            biCat.getDatosBaseLiquidable().setValorCatastralIBI(Double.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("ert"))
            biCat.getDatosBaseLiquidable().setEjercicioRevTotal(Integer.valueOf(valor.toString()));
        else if (localName.equals("erp"))
            biCat.getDatosBaseLiquidable().setEjercicioRevParcial(Integer.valueOf(valor.toString()));
        else if (localName.equals("per"))
            biCat.getDatosBaseLiquidable().setPeriodoTotal(Integer.valueOf(valor.toString()));
       
				
		
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
