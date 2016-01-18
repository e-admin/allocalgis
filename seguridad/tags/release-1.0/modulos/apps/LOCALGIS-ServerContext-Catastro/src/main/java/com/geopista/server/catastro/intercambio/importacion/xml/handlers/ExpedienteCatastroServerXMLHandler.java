package com.geopista.server.catastro.intercambio.importacion.xml.handlers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.TipoExpediente;

public class ExpedienteCatastroServerXMLHandler extends DefaultHandler 
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
     * El Expediente donde meter los datos leidos
     */
    private Expediente expediente;
    
    private String etiqXMLorigen;

    private DireccionLocalizacionCatastroServerXMLHandler handlerDireccion;
    
    public ExpedienteCatastroServerXMLHandler (XMLReader parser, DefaultHandler handler,
            Expediente expediente, String etiqXMLorigen)
    {
        this.parser = parser;
        this.handlerToReturn = handler;
        this.expediente = expediente;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {
        valor = new StringBuffer();
        
        if (localName.equals("dfn") && expediente!=null){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            expediente.setDireccionPresentador(dir);
            handlerDireccion = new DireccionLocalizacionCatastroServerXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        
        //expediente.
        
        if (localName.equals("aexpg"))
            // JAVIER expediente.setAnnoExpedienteGerencia(Integer.parseInt(valor.toString()));
            expediente.setAnnoExpedienteGerencia(Integer.valueOf(valor.toString()));
        else if (localName.equals("rexpg"))
            expediente.setNumeroExpediente(valor.toString());
        else if (localName.equals("ero"))
            //JAVIER expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.parseInt(valor.toString()));
            expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf(valor.toString()));
        else if (localName.equals("aexpec"))
            expediente.setAnnoExpedienteAdminOrigenAlteracion(Integer.parseInt(valor.toString()));
        else if (localName.equals("rexpec"))
            expediente.setReferenciaExpedienteAdminOrigen(valor.toString());

        
        else if (localName.equals("fac") )
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                expediente.setFechaAlteracion(df.parse(valor.toString()));
            } catch (ParseException e)
            {
                e.printStackTrace();
                expediente.setFechaAlteracion(new Date());
            }
        }
        else if (localName.equals("fre"))
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                expediente.setFechaRegistro(df.parse(valor.toString()));
            } catch (ParseException e)
            {
                e.printStackTrace();
                expediente.setFechaRegistro(new Date());
            }
        }
        
        else if (localName.equals("fce"))
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                expediente.setFechaDeCierre(df.parse(valor.toString()));
            } catch (ParseException e)
            {
                e.printStackTrace();
                expediente.setFechaDeCierre(new Date());
            }
        }
        
        //Tipo de expediente (motivo del movimiento) CIBI, MOVM, 902N...
        else if (localName.equals("texp"))
        {
            TipoExpediente tipoExpediente = new TipoExpediente();
            tipoExpediente.setCodigoTipoExpediente(valor.toString());
            expediente.setTipoExpediente(tipoExpediente);
        }
            
        //tipo de intercambio: V/F/R
        else if (localName.equals("tint"))
            expediente.setTipoDeIntercambio(valor.toString());
        
        
        //    npn
        else if (localName.equals("cp"))
            expediente.setCodProvinciaNotaria(valor.toString());
        else if (localName.equals("cpb"))
            expediente.setCodPoblacionNotaria(valor.toString());
        else if (localName.equals("cnt"))
            expediente.setCodNotaria(valor.toString());
        else if (localName.equals("aprt"))
           //JAVIER expediente.setAnnoProtocoloNotarial(Integer.parseInt(valor.toString()));
            expediente.setAnnoProtocoloNotarial(valor.toString()); 
        else if (localName.equals("prt"))
            expediente.setProtocoloNotarial(valor.toString());
                        
        
        else if (localName.equals("doco"))
            expediente.setTipoDocumentoOrigenAlteracion(valor.toString());
        //Información del documento origen de la alteración
        else if (localName.equals("idoco"))
            expediente.setInfoDocumentoOrigenAlteracion(valor.toString());
        
        //Código de la descripción de la alteración catastral
        else if (localName.equals("cdeac"))
            expediente.setCodigoDescriptivoAlteracion(valor.toString());
        
        //dec - declarante
        else if (localName.equals("nif"))
            expediente.setNifPresentador(valor.toString());
        else if (localName.equals("nom"))
            expediente.setNombreCompletoPresentador(valor.toString());
        
        else if (localName.equals("nbu"))
            expediente.setNumBienesInmueblesUrbanos(Integer.parseInt(valor.toString()));
        else if (localName.equals("nbr"))
            expediente.setNumBienesInmueblesRusticos(Integer.parseInt(valor.toString()));
        else if (localName.equals("nbce"))
            expediente.setNumBienesInmueblesCaractEsp(Integer.parseInt(valor.toString()));
        
        //si hemos llegado al final de la etiqueta
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