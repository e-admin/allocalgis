package com.geopista.app.catastro.intercambio.importacion.xml.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.NumeroFincaRegistral;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.RegistroPropiedad;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;


public class BienInmuebleXMLHandler extends DefaultHandler 
{     
    //BienInmuebleCatastro que se esta procesando
    private BienInmuebleCatastro biCat;
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
    
    String codProv = new String();
    String codRegistroProp = new String();    
    String seccion = new String();
    String numFinca = new String(); 
    String numSubfincaRegistral = new String();
    
    
    //boolean existeRep = false;
    private ExpedienteXMLHandler handlerExpediente;
    private DireccionLocalizacionXMLHandler handlerDireccion;
    private ConstruccionXMLHandler handlerConstruccion;
    private String etiqXMLorigen;
	private RepresentanteXMLHandler handlerRepresentante;
    
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    
    public BienInmuebleXMLHandler (XMLReader parser, DefaultHandler handlerToReturn, 
    		BienInmuebleCatastro biCat, String etiqXMLorigen)
    {
        this.parser = parser;
        this.biCat = biCat;
        this.handlerToReturn = handlerToReturn;
        this.etiqXMLorigen = etiqXMLorigen;
    }
    
    public void startElement( String namespaceURI, String localName, String qName, Attributes attr ) 
    throws SAXException 
    {   
        
        valor = new StringBuffer();
        //comprueba que vengan datos de representante y que se esten parseando en 
        //ese momento
        //if (localName.equals("rep"))        
        //    existeRep = true;  
        
       if (localName.equals("exp")){
            
            Expediente exp = new Expediente();
            biCat.setDatosExpediente(exp);
            handlerExpediente = new ExpedienteXMLHandler( parser, this, exp, localName);
            parser.setContentHandler( handlerExpediente );
        }   
        else if (localName.equals("dt")){
            
            DireccionLocalizacion dir = new DireccionLocalizacion();
            biCat.setDomicilioTributario(dir);
            handlerDireccion = new DireccionLocalizacionXMLHandler( parser, this, dir, localName);
            parser.setContentHandler( handlerDireccion );
        }   
        
        //Lista de construcciones (solo para ldc.xsd)
        else if (localName.equals("lcons")){
            
            ArrayList lstConstrucciones = new ArrayList();
            biCat.setLstConstrucciones(lstConstrucciones);            
            handlerConstruccion = new ConstruccionXMLHandler( parser, this, lstConstrucciones, localName);
            parser.setContentHandler( handlerConstruccion );
        }
        else if (localName.equals("rep")){
            
            Persona repres = new Persona();
            biCat.setRepresentante(repres);            
            handlerRepresentante = new RepresentanteXMLHandler( parser, this, repres, localName);
            parser.setContentHandler( handlerRepresentante );
        }
    }
    
    public void endElement (String namespaceURI, String localName, String rawName)
    throws SAXException
    {
        //idbi - idcat - idine - idreg
        if (localName.equals("pc1"))
            ref1=valor.toString();
        else if (localName.equals("pc2"))
            ref2=valor.toString();
        else if (localName.equals("cc1"))
            biCat.getIdBienInmueble().setDigControl1(valor.toString());
        else if (localName.equals("cc2"))
            biCat.getIdBienInmueble().setDigControl2(valor.toString());
        else if (localName.equals("car"))
            biCat.getIdBienInmueble().setNumCargo(valor.toString());
         
        else if (localName.equals("cd"))
        {
            EntidadGeneradora entGen = new EntidadGeneradora();
            entGen.setCodigo(Integer.parseInt(valor.toString()));
            biCat.getDatosExpediente().setEntidadGeneradora(entGen);
        }
        else if (localName.equals("cmc"))
            biCat.setCodMunicipioDGC(valor.toString());
        
        //idbis
        //no se recoge irc o erc
        else if (localName.equals("cn"))
            biCat.setClaseBienInmueble(valor.toString());        
        //idbic
        else if (localName.equals("lrc"))
            biCat.getIdBienInmueble().setIdBienInmueble(valor.toString()); 
                       
        //idad
        else if (localName.equals("nfbi"))
            biCat.setNumFijoInmueble(Integer.valueOf(valor.toString()));        
        else if (localName.equals("iia"))
            biCat.setIdAyuntamientoBienInmueble(valor.toString());
        //   nfr
        else if (localName.equals("cp"))
            codProv = valor.toString();
        else if (localName.equals("crp"))
        {
            codRegistroProp = valor.toString();
            biCat.setRegPropiedad(Integer.valueOf(codRegistroProp));
        }            
        else if (localName.equals("sc"))
            seccion = valor.toString();
        else if (localName.equals("fr"))
            {
            numFinca = valor.toString();
            }
        //     sfr es optativo
        else if (localName.equals("sfr"))
        {
            numSubfincaRegistral = valor.toString();
        }
        
        
        
        
        //else if (localName.equals("noedh"))
        //    biCat.setNumeroOrdenEscrituraDivisionHorizontal(valor);
        
        
        //tbi
        else if (localName.equals("tbi"))
            biCat.setClaseBienInmueble(valor.toString());
        
        //ldt
        else if (localName.equals("ldt"))
            biCat.getDomicilioTributario().setDireccionNoEstructurada(valor.toString());
        
        //inrbi - Información notarios y registradores - No recogida
        
        
        //debi
        else if (localName.equals("pvd"))
            biCat.getDatosEconomicosBien().setPrecioDeclarado(Double.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("opvd"))
            biCat.getDatosEconomicosBien().setOrigenPrecioDeclarado(valor.toString());
        else if (localName.equals("pav"))
            biCat.getDatosEconomicosBien().setPrecioVenta(Double.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("afv"))
            biCat.getDatosEconomicosBien().setAnioFinValoracion(Integer.valueOf(valor.toString()));
        else if (localName.equals("itp"))
            biCat.getDatosEconomicosBien().setIndTipoPropiedad(valor.toString());
        else if (localName.equals("noe"))
            biCat.getDatosEconomicosBien().setNumOrdenHorizontal(valor.toString());
        else if (localName.equals("avc"))
            biCat.getDatosEconomicosBien().setAnioValorCat(Integer.valueOf(valor.toString()));
        else if (localName.equals("vcat"))
        {   
            Double valorCat = ImportarUtils.strToDouble(valor.toString().replaceAll(",","."));
            biCat.getDatosEconomicosBien().setValorCatastral(valorCat);
            
            if (blackboard.get("ValorTotal")!=null)            
                blackboard.put("ValorTotal", new Long(((Long)blackboard.get("ValorTotal")).longValue() + valorCat.longValue()));
            
        }
        else if (localName.equals("vcs"))
        {
            Double valorSuelo = ImportarUtils.strToDouble(valor.toString().replaceAll(",","."));
            
            biCat.getDatosEconomicosBien().setValorCatastralSuelo(valorSuelo);
            if (blackboard.get("ValorSuelo")!=null)            
                blackboard.put("ValorSuelo", new Long(((Long)blackboard.get("ValorSuelo")).longValue() + valorSuelo.longValue()));
            
        }
        else if (localName.equals("vcc"))
        {
            Double valorConstruccion = ImportarUtils.strToDouble(valor.toString().replaceAll(",","."));
            biCat.getDatosEconomicosBien().setValorCatastralConstruccion(valorConstruccion);
            if (blackboard.get("ValorConstruccion")!=null)            
                blackboard.put("ValorConstruccion", new Long(((Long)blackboard.get("ValorConstruccion")).longValue() + valorConstruccion.longValue()));
            
        }
        else if (localName.equals("bl")) 
        {
            Long baseLiquidable = new Long(ImportarUtils.doubleToStringWithFactor(ImportarUtils.strToDouble(valor.toString().replaceAll(",",".")), 1));
            biCat.getDatosEconomicosBien().setBaseLiquidable(baseLiquidable);
            if (blackboard.get("BaseLiquidable")!=null)            
                blackboard.put("BaseLiquidable", new Long(((Long)blackboard.get("BaseLiquidable")).longValue() + baseLiquidable.longValue()));
            
        }
        else if (localName.equals("uso"))
            biCat.getDatosEconomicosBien().setUso(valor.toString());
        //    bru
        else if (localName.equals("bvc"))
            biCat.getDatosEconomicosBien().setImporteBonificacionRustica(new Long(ImportarUtils.doubleToStringWithFactor(ImportarUtils.strToDouble(valor.toString().replaceAll(",",".")), 1)));
        else if (localName.equals("bcl"))
            biCat.getDatosEconomicosBien().setClaveBonificacionRustica(valor.toString());
        else if (localName.equals("sfc"))
            biCat.getDatosEconomicosBien().setSuperficieCargoFincaConstruida(Long.valueOf(valor.toString()));
        else if (localName.equals("sfs"))
            biCat.getDatosEconomicosBien().setSuperficieCargoFincaRustica(Long.valueOf(valor.toString()));
        else if (localName.equals("cpt"))
            biCat.getDatosEconomicosBien().setCoefParticipacion(Float.valueOf(valor.toString().replaceAll(",",".")));
        else if (localName.equals("ant"))
            biCat.getDatosEconomicosBien().setAnioAntiguedad(Integer.valueOf(valor.toString()));
       
            
        //lcol - Lista de colindancias
        
        
        //lcons - Lista de construcciones        
        
        
        /*
        else if (localName.equals("rep"))        
            existeRep = false;          
       
        
        else if (existeRep && localName.equals("nif"))
            biCat.getRepresentante().setNif(valor);
        else if (existeRep && localName.equals("nom"))
            biCat.getRepresentante().setRazonSocial(valor);
        else if (existeRep && localName.equals("cp"))
            biCat.getRepresentante().getDomicilio().setCodigoProvincia(Integer.parseInt(valor));
        else if (existeRep && localName.equals("cm"))
            biCat.getRepresentante().getDomicilio().setCodigoMunicipioINE(Integer.parseInt(valor));
        else if (existeRep && localName.equals("cmc"))
            biCat.getRepresentante().getDomicilio().setCodigoMunicipioDGC(Integer.parseInt(valor));
        else if (existeRep && localName.equals("np"))
            biCat.getRepresentante().getDomicilio().setNombreProvincia(valor);
        else if (existeRep && localName.equals("nm"))
            biCat.getRepresentante().getDomicilio().setNombreMunicipio(valor);
        else if (existeRep && localName.equals("nem"))
            biCat.getRepresentante().getDomicilio().setNombreEntidadMenor(valor);
        else if (existeRep && localName.equals("cv"))
            biCat.getRepresentante().getDomicilio().setCodigoViaPublica(Integer.parseInt(valor));
        else if (existeRep && localName.equals("tv"))
            biCat.getRepresentante().getDomicilio().setTipoVia(valor);
        else if (existeRep && localName.equals("nv"))
            biCat.getRepresentante().getDomicilio().setNombreVia(valor);
        else if (existeRep && localName.equals("pnp"))
            biCat.getRepresentante().getDomicilio().setPrimerNumeroPolicia(Integer.parseInt(valor));
        else if (existeRep && localName.equals("snp"))
            biCat.getRepresentante().getDomicilio().setSegundoNumeroPolicia(Integer.parseInt(valor));
        else if (existeRep && localName.equals("plp"))
            biCat.getRepresentante().getDomicilio().setPrimeraLetra(valor);
        else if (existeRep && localName.equals("slp"))
            biCat.getRepresentante().getDomicilio().setSegundaLetra(valor);
        else if (existeRep && localName.equals("km"))
            biCat.getRepresentante().getDomicilio().setKilometro(Double.parseDouble(valor.replaceAll(".",",")));
        else if (existeRep && localName.equals("td"))
            biCat.getRepresentante().getDomicilio().setDirNoEstructurada(valor);
        else if (existeRep && localName.equals("dp"))
            biCat.getRepresentante().getDomicilio().setCodigoPostal(Integer.parseInt(valor));
       
       */
        
        else if (localName.equals(etiqXMLorigen))
        {
            parser.setContentHandler (handlerToReturn);
        }
        
        
        
        //Rellena los campos compuestos
        if (ref1!=null && ref2!=null 
                && !ref1.trim().equals("") && !ref2.trim().equals(""))
        {
            biCat.getIdBienInmueble().setParcelaCatastral(ref1+ref2);   
            ref1=null;
            ref2=null;
        }
        if (codProv!=null && codRegistroProp!=null && seccion!=null && numFinca!=null 
        		&& numSubfincaRegistral!=null && !numSubfincaRegistral.trim().equals("")
                && !codProv.trim().equals("") && !codRegistroProp.trim().equals("")
                && !seccion.trim().equals("") && !numFinca.trim().equals(""))
        {
            RegistroPropiedad rp = new RegistroPropiedad(codProv, codRegistroProp);
            NumeroFincaRegistral nfr =
                new NumeroFincaRegistral (rp, seccion, numFinca,numSubfincaRegistral);
            biCat.setNumFincaRegistral(nfr);
            
            codProv = null;
            codRegistroProp = null;
            seccion = null;
            numFinca = null;
            numSubfincaRegistral=null;
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
