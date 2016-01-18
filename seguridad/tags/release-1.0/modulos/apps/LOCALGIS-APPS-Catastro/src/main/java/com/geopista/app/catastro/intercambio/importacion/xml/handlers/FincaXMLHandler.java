package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;


public class FincaXMLHandler extends DefaultHandler 
{
    //valor contenido entre las etiquetas de un elemento
    private StringBuffer valor = new StringBuffer();
    
    //El parser, para luego devolver el control al
    //Handler anterior.
    private XMLReader parser;
    //El Handler al que queremos volver
    private DefaultHandler handlerToReturn;
    //La finca donde meter los datos que leamos.
    private FincaCatastro finca;
    
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    
    String ref1 = new String();
    String ref2 = new String();

    private ExpedienteXMLHandler handlerExpediente;
    private DireccionLocalizacionXMLHandler handlerDireccion;
    private String etiqXMLorigen;
    
    public FincaXMLHandler (XMLReader parser, DefaultHandler handler,
            FincaCatastro finca, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.finca = finca;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            finca.setDatosExpediente(exp);
            handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("dt")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            finca.setDirParcela(dir);
            handlerDireccion = new DireccionLocalizacionXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
        
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        //idf
        if (localName.equals("cn"))
            finca.setBICE(valor.toString());
        else if (localName.equals("pc1"))
            ref1 = valor.toString();
        else if (localName.equals("pc2"))
            ref2 = valor.toString();
        
        //idfcat
        else if (localName.equals("cd"))
            finca.setCodDelegacionMEH(valor.toString());
        else if (localName.equals("cmc"))
            finca.setCodMunicipioDGC(valor.toString());
        
        
        //ldt - literal del domicilio tributario
        else if (localName.equals("ldt"))
            finca.getDirParcela().setDireccionNoEstructurada(valor.toString());
        //ltp - literal del tipo de finca
        //else if (localName.equals("ltp"))
        //    finca.setTipoFinca(valor);
        
        
        //dff
        else if (localName.equals("ss"))
            finca.getDatosFisicos().setSupFinca(Long.valueOf(valor.toString()));
        else if (localName.equals("sct"))
            finca.getDatosFisicos().setSupTotal(Long.valueOf(valor.toString()));
        else if (localName.equals("ssr"))
            finca.getDatosFisicos().setSupSobreRasante(Long.valueOf(valor.toString()));
        else if (localName.equals("sbr"))
            finca.getDatosFisicos().setSupBajoRasante(Long.valueOf(valor.toString()));
        else if (localName.equals("sc"))
            finca.getDatosFisicos().setSupCubierta(Long.valueOf(valor.toString()));
        else if (localName.equals("coordx"))
            //finca.getDatosFisicos().setXCoord(Integer.valueOf(valor.toString()));
        	finca.getDatosFisicos().setXCoord(Float.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("coordy"))
            //finca.getDatosFisicos().setYCoord(Integer.valueOf(valor.toString()));
        	finca.getDatosFisicos().setYCoord(Float.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("srs")){
        	finca.getDatosFisicos().setSRS(valor.toString());
        }
        
        //dval - Datos de valoración de la finca
        else if (localName.equals("aapv"))
            finca.getDatosEconomicos().setAnioAprobacion(Integer.valueOf(valor.toString()));
        else if (localName.equals("cfcvc"))
            finca.getDatosEconomicos().setCodigoCalculoValor(Integer.valueOf(valor.toString()));
        else if (localName.equals("cpo"))
            finca.getDatosEconomicos().getPoligonoCatastralValor().setCodPoligono(valor.toString());
        else if (localName.equals("mr"))
            finca.getDatosEconomicos().setIndModalidadReparto(valor.toString());
        else if (localName.equals("iipvc"))
            finca.getDatosEconomicos().setIndInfraedificabilidad(valor.toString());
        else if (localName.equals("tmov"))
        	finca.setTIPO_MOVIMIENTO_DELETE(valor.toString());
        
        
        //si hemos llegado al final de la etiqueta
        else if (localName.equals(etiqXMLorigen))
        {     
            if (finca!=null){
                ArrayList lstFincas = (ArrayList)blackboard.get("ListaFincas");
                if(lstFincas!=null)
                {
                    lstFincas.add(finca.getRefFinca());
                    blackboard.put("ListaFincas", lstFincas);
                    if (blackboard.get("ListaTotalFincasImportadas")!=null)
                    {
                        ArrayList lstFincastotal = 
                            (ArrayList)blackboard.get("ListaTotalFincasImportadas");
                        if (lstFincastotal!=null)
                            lstFincastotal.addAll(lstFincas);
                        blackboard.put("ListaTotalFincasImportadas", lstFincastotal);
                    }
                    
                }
                
            }            
            
            parser.setContentHandler (handlerToReturn);
        }
        
        if (ref1!=null && ref2!=null 
                && !ref1.trim().equals("") && !ref2.trim().equals(""))
        {
            ReferenciaCatastral refCat = new ReferenciaCatastral(ref1, ref2);
            finca.setRefFinca(refCat);
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
