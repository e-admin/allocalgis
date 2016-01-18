package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Persona;


public class InfoTributariaXMLHandler extends DefaultHandler 
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
	private RepresentanteXMLHandler handlerRepresentante;
	private String etiqXMLorigen;
	
	
	public InfoTributariaXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
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
			handlerRepresentante = new RepresentanteXMLHandler( parser, this, repres, localName);
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
