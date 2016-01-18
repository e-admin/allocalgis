package com.geopista.app.catastro.servicioWebCatastro.utils;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.app.catastro.intercambio.importacion.xml.contents.BienInmuebleJuridico;
import com.geopista.app.catastro.intercambio.importacion.xml.contents.UnidadDatosIntercambio;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FX_CC;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.IdCultivo;
import com.geopista.app.catastro.model.beans.NumeroFincaRegistral;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RegistroPropiedad;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.economicos.DatosBaseLiquidableBien;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosBien;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosConstruccion;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosFinca;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosSuelo;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosUC;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosConstruccion;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosFinca;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosSuelo;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosUC;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaPoligono;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaTramos;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaUrbanistica;
import com.geopista.app.catastro.model.datos.ponencia.PonenciaZonaValor;
import com.geopista.app.catastro.servicioWebCatastro.IdentificadorDerecho;
import com.geopista.app.catastro.servicioWebCatastro.beans.ControlWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosErrorWS;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosWSResponseBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.ErrorWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorDialogo;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorFincaRegistral;
import com.geopista.app.catastro.servicioWebCatastro.beans.IdentificadorWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.InformacionWSFichero;
import com.geopista.app.catastro.servicioWebCatastro.beans.OrigenWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.RespuestaWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.UDSAWSBean;
import com.geopista.app.catastro.servicioWebCatastro.beans.UnidadErrorElementoWSBean;



public class ParsearDatosWS {
	
	private static final String VALOR_SI = "S";
	private static final String VALOR_NO = "N";
	
	private static Expediente exp;

	public static DatosErrorWS ParsearDatosWSError (Element [] elements){
		DatosErrorWS datosError = new DatosErrorWS();
		
		NodeList listaNodes = elements[0].getChildNodes();
	     for (int i = 0; i < listaNodes.getLength(); i++) {
	    	 Node childNode = listaNodes.item(i);
	    	 if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("IdPeticion")){
	    		 if(childNode.getFirstChild() != null){
	    			 datosError.setIdPeticion(childNode.getFirstChild().getNodeValue());
	    		 }
	    		 
	    	 }
	    	 else if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("TimeStamp")){
	    		 datosError.setTimeStamp(childNode.getFirstChild().getNodeValue());
	    	 }
	    	 else if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("Estado")){
	    		 NodeList listaDatosEstado = childNode.getChildNodes();
	    		 for (int h = 0; h < listaDatosEstado.getLength(); h++) {
	    			 Node childNodeEstado = listaDatosEstado.item(h);
	    			 if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("CodigoEstado")){
	    				 datosError.setCodigoEstado(childNodeEstado.getFirstChild().getNodeValue());
			    	 }
			    	 else if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("LiteralError")){
			    		 datosError.setMensaje(childNodeEstado.getFirstChild().getNodeValue());
			    	 }
	    			 
	    		 }
	    	
	    	 }
	     }
		return datosError;
	}
	
	
	 public DatosWSResponseBean parsearDatosWSResponse(Document doc, Expediente exp) throws ParserConfigurationException, SAXException, IOException{
		 this.exp = exp;
		 return parsearDatos(doc);
	 }
	 

	 private static DatosWSResponseBean parsearDatos(Document doc) throws ParserConfigurationException, SAXException, IOException{
		 
		 DatosWSResponseBean datosWSResponse = new DatosWSResponseBean();
		 
		// Document doc = stringToDocument(xml);
		 
		 Element datosTramitador = doc.getDocumentElement();
	     NodeList listaNodes = datosTramitador.getChildNodes();
	     for (int i = 0; i < listaNodes.getLength(); i++) {
	 
	    	 Node childNode = listaNodes.item(i);
             if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("control")){
            	
            	 datosWSResponse.setControl(parsearDatosControl(childNode));
             }
             else if(childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("respuesta")){
            	 
            	 datosWSResponse.setRespuesta(parsearDatosRespuesta(childNode)); 
             }	    	 
	     }
	     
	     return datosWSResponse;
	 }
	 
	 
	 
	 private static RespuestaWSBean parsearDatosRespuesta(Node node ){

		 RespuestaWSBean respuesta = new RespuestaWSBean();
    	 
    	 NodeList listaDatosRespuesta = node.getChildNodes();
    	 for (int h = 0; h < listaDatosRespuesta.getLength(); h++) {
    		 
    		 Node nodeRespuesta = listaDatosRespuesta.item(h);
    		 if (nodeRespuesta.getNodeType() == Node.ELEMENT_NODE && nodeRespuesta.getNodeName().equals("exp")){

    			 respuesta.setExpediente(parsearDatosExpediente(nodeRespuesta));
    			 respuesta.getExpediente().setIdExpediente( exp.getIdExpediente());
             }
    		 else if (nodeRespuesta.getNodeType() == Node.ELEMENT_NODE && nodeRespuesta.getNodeName().equals("udsa")){
    			 respuesta.setUdsa(parsearDatosUDSA(nodeRespuesta));
             }
    		 else if (nodeRespuesta.getNodeType() == Node.ELEMENT_NODE && nodeRespuesta.getNodeName().equals("est")){
    			 respuesta.setEstado(nodeRespuesta.getFirstChild().getNodeValue());
             }
    		 else if (nodeRespuesta.getNodeType() == Node.ELEMENT_NODE && nodeRespuesta.getNodeName().equals("listaerrores")){
    			 respuesta.setLstUnidadError(parsarDatosErrores(nodeRespuesta));
             }
    	 }      
    	 return respuesta;
		 
	 }
	 
	 private static ControlWSBean parsearDatosControl(Node node){

		 ControlWSBean control = new ControlWSBean();
    	 
    	 
    	 NodeList listaDatosControl = node.getChildNodes();
    	 for (int h = 0; h < listaDatosControl.getLength(); h++) {
    		 
    		 Node nodeControl = listaDatosControl.item(h);
    		 if (nodeControl.getNodeType() == Node.ELEMENT_NODE && nodeControl.getNodeName().equals("ieg")) {
    			 //Identificación de la entidad generadora
    			 EntidadGeneradora entidadGeneradora = new EntidadGeneradora();
    			 NodeList listaDatosIeg = nodeControl.getChildNodes();
    			 for (int j = 0; j < listaDatosIeg.getLength(); j++) {
    				 Node nodeIeg = listaDatosIeg.item(j);
    				 
    				 if (nodeIeg.getNodeType() == Node.ELEMENT_NODE && nodeIeg.getNodeName().equals("teg")){
    					 entidadGeneradora.setTipo(nodeIeg.getFirstChild().getNodeValue());    
                     }
    				 else if (nodeIeg.getNodeType() == Node.ELEMENT_NODE && nodeIeg.getNodeName().equals("ceg")){
    					
    					 NodeList listaDatosCeg = nodeIeg.getChildNodes();
    					 for (int m = 0; m < listaDatosCeg.getLength(); m++) {
    						 Node nodeCeg = listaDatosCeg.item(m);
    	            		 if (nodeCeg.getNodeType() == Node.ELEMENT_NODE && nodeCeg.getNodeName().equals("cd"))
    	                     {
    	            			 entidadGeneradora.setCodigo(new Integer(nodeCeg.getFirstChild().getNodeValue()));    
    	                     }
    					 }
                     }
    				 else if (nodeIeg.getNodeType() == Node.ELEMENT_NODE && nodeIeg.getNodeName().equals("neg")){
    					 entidadGeneradora.setNombre(nodeIeg.getFirstChild().getNodeValue());
                     }
    			 }
    			 control.setEntidadGeneradora(entidadGeneradora);
    			 
             }
    		 else if (nodeControl.getNodeType() == Node.ELEMENT_NODE && nodeControl.getNodeName().equals("ifi")){
    			 //Informacion del fichero
    			 InformacionWSFichero informacionFichero = new InformacionWSFichero();
    			 
    			 NodeList listaDatosIfi = nodeControl.getChildNodes();
    			 for (int j = 0; j < listaDatosIfi.getLength(); j++) {
    				 Node nodeIfi = listaDatosIfi.item(j);
    				 if (nodeIfi.getNodeType() == Node.ELEMENT_NODE && nodeIfi.getNodeName().equals("ffi")){
    					 informacionFichero.setFechaGeneracion(nodeIfi.getFirstChild().getNodeValue()); 
                     }
    				 else if (nodeIfi.getNodeType() == Node.ELEMENT_NODE && nodeIfi.getNodeName().equals("hfi")){
    					 informacionFichero.setHoraGeneracion(nodeIfi.getFirstChild().getNodeValue());
                     }
    				 else if (nodeIfi.getNodeType() == Node.ELEMENT_NODE && nodeIfi.getNodeName().equals("tfi")){
    					 informacionFichero.setTipo(nodeIfi.getFirstChild().getNodeValue());
    				 }
    			 }
    			 
    			 control.setInformacionFichero(informacionFichero);
             }
    		 else if (nodeControl.getNodeType() == Node.ELEMENT_NODE && nodeControl.getNodeName().equals("liddf")){
    			 //Identificador del diálogo basado en fincas
    				
    			 ArrayList lstIdentificadoresDialogo = new ArrayList();
    			 NodeList listaDatosLiddf = nodeControl.getChildNodes();
    			 for (int j = 0; j < listaDatosLiddf.getLength(); j++) {
    				 boolean isFinca = false;
        			 boolean isBien = false;
    				
    				 Node nodeLiddf = listaDatosLiddf.item(j);
    				 if (nodeLiddf.getNodeType() == nodeLiddf.ELEMENT_NODE && nodeLiddf.getNodeName().equals("iddf")){   				    	 

        				 IdentificadorDialogo identificadorDialogo = new IdentificadorDialogo();   				 
        				 
	    				 NodeList listaDatosIdff = nodeLiddf.getChildNodes();
	        			 for (int g = 0; g < listaDatosIdff.getLength(); g++) {

	        				 Node nodeIddf = listaDatosIdff.item(g);
		    				 if (nodeIddf.getNodeType() == Node.ELEMENT_NODE && nodeIddf.getNodeName().equals("idd")){
		    					 identificadorDialogo.setIdentificadorDialogo(nodeIddf.getFirstChild().getNodeValue());
		                     }
		    				 else if (nodeIddf.getNodeType() == Node.ELEMENT_NODE && nodeIddf.getNodeName().equals("idfcat")){
		    					 FincaCatastro finca = parsearDatosFincaCatastral(nodeIddf); 
		    					 identificadorDialogo.setFincaBien(finca);
		                     }
		    				 else if (nodeIddf.getNodeType() == Node.ELEMENT_NODE && nodeIddf.getNodeName().equals("idbicat")){
		    					 BienInmuebleCatastro bien =  new BienInmuebleCatastro();
		    					 bien = parsearDatosIdentificacionBienInmueble(nodeIddf, bien);
		    					 identificadorDialogo.setFincaBien(bien);
		    				 }
		    				 else if (nodeIddf.getNodeType() == Node.ELEMENT_NODE && nodeIddf.getNodeName().equals("lexp")){
		    					 ArrayList lstExpedientes = new ArrayList();
		    					 Expediente exp = null;
		    					 NodeList listaDatosLexp= nodeIddf.getChildNodes();
		    	    			 for (int k = 0; k < listaDatosLexp.getLength(); k++) {
		
		    	    				 Node nodeLexp = listaDatosLexp.item(k);
		    	    				 if (nodeLexp.getNodeType() == Node.ELEMENT_NODE && nodeLexp.getNodeName().equals("exp")){
		    	    				
		    	    					 exp = parsearDatosExpediente(nodeLexp);
		    	    					 lstExpedientes.add(exp);
		    	    				 } 
		    	    			 }
		    	    			 identificadorDialogo.setLstExpedientes(lstExpedientes);
		    				 }
		    				 
        				 }
	        			 lstIdentificadoresDialogo.add(identificadorDialogo);
        			 } 
    				 control.setLstIdentificadoresDialogo(lstIdentificadoresDialogo);
    			 }	 
             }
    		 else if (nodeControl.getNodeType() == Node.ELEMENT_NODE && nodeControl.getNodeName().equals("cde")){
    			 //Codigo de envio
    			 control.setCodigoEnvio(nodeControl.getFirstChild().getNodeValue());
             }
    	 }	  
    	 
    	 return control;
	 }
	 
	 
	 private static UDSAWSBean parsearDatosUDSA(Node node ){
		 UDSAWSBean udsa = new UDSAWSBean();
		 NodeList listaDatosUdsa = node.getChildNodes();
    	 for (int h = 0; h < listaDatosUdsa.getLength(); h++) {
    		 
    		 Node nodeUdsa = listaDatosUdsa.item(h);
    		 if (nodeUdsa.getNodeType() == Node.ELEMENT_NODE && nodeUdsa.getNodeName().equals("exp")){
    			 //Expediente de la gerencia
    			 udsa.setExpediente(parsearDatosExpediente(nodeUdsa)); 
    		 }
    		 else if (nodeUdsa.getNodeType() == Node.ELEMENT_NODE && nodeUdsa.getNodeName().equals("lelems")){
    			 
    			 ArrayList lstUnidadDatosIntercambio = new ArrayList();
    			 //Lista de situaciones
    			 NodeList listaDatosLelems = nodeUdsa.getChildNodes();
    	    	 for (int k = 0; k < listaDatosLelems.getLength(); k++) {
    	    		 
    	    		 Node nodeLelems = listaDatosLelems.item(k);
    	    		 if (nodeLelems.getNodeType() == Node.ELEMENT_NODE && nodeLelems.getNodeName().equals("elemsf")){
    	    			 
    	    			 UnidadDatosIntercambio unidadDatosIntercambio = new UnidadDatosIntercambio();
    	    			 
    	    			 NodeList listaDatosElemsf = nodeLelems.getChildNodes();
    	    	    	 for (int p = 0; p < listaDatosElemsf.getLength(); p++) {
    	    	    		 
    	    	    		 Node nodeElemsf = listaDatosElemsf.item(p);
    	    	    		 if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("fincacs")){
    	    	    			 FincaCatastro finca =  parsearDatosFinca(nodeElemsf);
    	    	    			 unidadDatosIntercambio.setFincaCatastro(finca);
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("lsuelocs")){
    	    	    			 //finca.setLstSuelos(parsearDatosSuelos(nodeElemsf));;
    	    	    			 unidadDatosIntercambio.setLstSuelos(parsearDatosSuelos(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("luccs")){
    	    	    			 unidadDatosIntercambio.setLstUCs(parsearDatosUnidadesConstructivas(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("lbics")){
    	    	    			 unidadDatosIntercambio.setLstBienesInmuebles(parsearDatosBienInmueble(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("lconscs")){
    	    	    			 unidadDatosIntercambio.setLstConstrucciones(parsearDatosConstrucciones(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("lsprcs")){
    	    	    			 unidadDatosIntercambio.setLstCultivos(parsearDatosCultivos(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("lreparcs")){
    	    	    			 unidadDatosIntercambio.setLstRepartos(parsearDatosRepartos(nodeElemsf));
    	    	    		 }
    	    	    		 else if (nodeElemsf.getNodeType() == Node.ELEMENT_NODE && nodeElemsf.getNodeName().equals("fxcc")){
    	    	    			 unidadDatosIntercambio.setFxcc(parsearDatosFXCC(nodeElemsf));
    	    	    		 }
    	    	    	 }
    	    	    	 lstUnidadDatosIntercambio.add(unidadDatosIntercambio);
    	    	    	 
    	    		 } 
    	    	 }
    	    	 udsa.setLstUnidadDatosIntercambio(lstUnidadDatosIntercambio);
    		 }	 
    	 }
		 return udsa;
	 }
	 
	 private static ArrayList parsarDatosErrores(Node node){
		 
		 ArrayList<UnidadErrorElementoWSBean> lstUnidadError = new ArrayList<UnidadErrorElementoWSBean>();
		 
		 NodeList listaErrores = node.getChildNodes();
		 for (int i = 0; i < listaErrores.getLength(); i++) {
			 
			 Node nodeListaErrores = listaErrores.item(i);
    		 if (nodeListaErrores.getNodeType() == Node.ELEMENT_NODE && nodeListaErrores.getNodeName().equals("uderr")){
    			 UnidadErrorElementoWSBean unidadErrorElemento = new UnidadErrorElementoWSBean();
            	 
            	 NodeList listaDatosError = nodeListaErrores.getChildNodes();
            	 for (int h = 0; h < listaDatosError.getLength(); h++) {
            		 
            		 Node nodeErrores = listaDatosError.item(h);
            		 if (nodeErrores.getNodeType() == Node.ELEMENT_NODE && nodeErrores.getNodeName().equals("ident")){
            			 IdentificadorWSBean identificador = new IdentificadorWSBean();
            			 
                    	 NodeList listaDatosIdentificador = nodeErrores.getChildNodes();
                    	 for (int j = 0; j < listaDatosIdentificador.getLength(); j++) {
                    		 Node nodeIdenficador = listaDatosIdentificador.item(j);
                    		 if (nodeIdenficador.getNodeType() == Node.ELEMENT_NODE && nodeIdenficador.getNodeName().equals("tipo")){
                    			 identificador.setTipo(nodeIdenficador.getFirstChild().getNodeValue());
                             }
                    		 else if (nodeIdenficador.getNodeType() == Node.ELEMENT_NODE && nodeIdenficador.getNodeName().equals("origen")){
                    			 OrigenWSBean origen = new OrigenWSBean();
                    			 NodeList listaDatosOrigen = nodeIdenficador.getChildNodes();
                    			 
                            	 for (int m = 0; m < listaDatosOrigen.getLength(); m++) {
                            		 Node nodeOrigen = listaDatosOrigen.item(m);
                            		 if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("exp")){
                            			 origen.setExpediente(parsearDatosExpediente(nodeOrigen));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("finca")){
                            			 origen.setFinca(parsearDatosFincaOrigen(nodeOrigen));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("suelo")){
                            			 SueloCatastro suelo = new SueloCatastro();
                            			 origen.setSuelo(parsearDatosSueloOrigen(nodeOrigen, suelo));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("uc")){
                            			 UnidadConstructivaCatastro unidadConstructiva = new UnidadConstructivaCatastro();
                            			 origen.setUnidadConstructiva(parsearDatosUnidadConstructivaOrigen(nodeOrigen, unidadConstructiva));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("bi")){
                            			 BienInmuebleCatastro bienInmueble = new BienInmuebleCatastro();
                            			 origen.setBienInmueble(parsearDatosIdentificacionBienInmuebleOrigen(nodeOrigen, bienInmueble));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("cons")){
                            			 ConstruccionCatastro construccion = new ConstruccionCatastro();
                            			 origen.setConstruccion(parsearDatosConstruccionOrigen(nodeOrigen, construccion));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("spr")){
                            			 Cultivo cultivo = new Cultivo();
                            			 origen.setCultivo(parsearDatosCultivoOrigen(nodeOrigen, cultivo));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("repar")){
                            			 RepartoCatastro reparto = new RepartoCatastro();
                            			 origen.setReparto(parsearDatosRepartoOrigen(nodeOrigen, reparto));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("dcho")){
                            			 origen.setDerecho(parsearDatosIdentificadorDerecho(nodeOrigen));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("pnot")){
                            			 Expediente expe = new Expediente();
                            			 origen.setExpediente(parsearDatosProtocoloNotarialOrigen(nodeOrigen, expe));
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("freg")){
                            			 IdentificadorFincaRegistral idenFincaRegistral = new IdentificadorFincaRegistral();
                            			 NodeList listaDatosFreg = nodeOrigen.getChildNodes();
                                    	 for (int t = 0; t< listaDatosFreg.getLength(); t++) {
                                    		 Node nodeFreg = listaDatosFreg.item(t);
                                    		 if (nodeFreg.getNodeType() == Node.ELEMENT_NODE && nodeFreg.getNodeName().equals("nfr")){
                                    			 idenFincaRegistral.setNumeroFincaRegistral(parsearDatosFincaRegistral(nodeFreg));
                                    		 }
                                    		 else if (nodeFreg.getNodeType() == Node.ELEMENT_NODE && nodeFreg.getNodeName().equals("expg")){
                                    			 Expediente exp = new Expediente();
                                    			 idenFincaRegistral.setExpediente(parsearDatosExpedienteGerencia(nodeFreg, exp));
                                    		 }
                                    	 }
                                     }
                            		 else if (nodeOrigen.getNodeType() == Node.ELEMENT_NODE && nodeOrigen.getNodeName().equals("cde")){
                            			 origen.setIdentificador(Integer.valueOf(nodeOrigen.getFirstChild().getNodeValue()));
                                     }
                            	 }
                            	 identificador.setOrigen(origen);
                             }
                    	 }	
                    	 unidadErrorElemento.setIdentificador(identificador);
                     }
            		 
            		 else if (nodeErrores.getNodeType() == Node.ELEMENT_NODE && nodeErrores.getNodeName().equals("lerr")){
            			
            			 ArrayList<ErrorWSBean> lstErrores = new ArrayList<ErrorWSBean>();
            			 
            			 NodeList listaError = nodeErrores.getChildNodes();
                    	 for (int d = 0; d < listaError.getLength(); d++) {
                    		 Node nodeLerr = listaError.item(d);
                    		 
                    		 if (nodeLerr.getNodeType() == Node.ELEMENT_NODE && nodeLerr.getNodeName().equals("err")){
                    			 ErrorWSBean error = new ErrorWSBean();
		                    	 NodeList listaErr = nodeLerr.getChildNodes();
		                    	 for (int p = 0; p < listaErr.getLength(); p++) {
		                    		 Node nodeErr = listaErr.item(p);
		                    		 
		                    		 if (nodeErr.getNodeType() == Node.ELEMENT_NODE && nodeErr.getNodeName().equals("cod")){
		                    			 if(nodeErr.getFirstChild() != null){
		                    				 error.setCodigo(nodeErr.getFirstChild().getNodeValue());
		                    		 	}
		                             }
		                    		 else if (nodeErr.getNodeType() == Node.ELEMENT_NODE && nodeErr.getNodeName().equals("des")){
		                    			 error.setDescripcion( nodeErr.getFirstChild().getNodeValue());
		                             }
		                    		 else if (nodeErr.getNodeType() == Node.ELEMENT_NODE && nodeErr.getNodeName().equals("te")){
		                    			 error.setTipo(nodeErr.getFirstChild().getNodeValue());
		                             }
		                    	 }
		                    	 lstErrores.add(error);
                    		 }
                    	 }
                    	 unidadErrorElemento.setLstErrores(lstErrores);
            		 }
            	 }
            	 lstUnidadError.add(unidadErrorElemento);
             }	 
 
		 }
		 return lstUnidadError;
	 }
	 
	 private static IdentificadorDerecho parsearDatosIdentificadorDerecho(Node node){
		 IdentificadorDerecho identificadorDerecho = new IdentificadorDerecho();
		 
		 String pc1 = "";
		 String pc2 = "";
		 Persona representante = new Persona();
		 Derecho derecho = new Derecho();
		 IdBienInmueble idBienInmueble = new IdBienInmueble();
		 
		 NodeList listaDatosDcho = node.getChildNodes();
		 for (int j = 0; j < listaDatosDcho.getLength(); j++) {

			 Node nodeDatosDcho = listaDatosDcho.item(j);
    		 if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("pc1")){
    			 pc1 = nodeDatosDcho.getFirstChild().getNodeValue();
    		 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("pc2")){
    			 pc2= nodeDatosDcho.getFirstChild().getNodeValue();
    		 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("car")){
    			 idBienInmueble.setNumCargo(nodeDatosDcho.getFirstChild().getNodeValue());
    		 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("cc1")){
    			 idBienInmueble.setDigControl1(nodeDatosDcho.getFirstChild().getNodeValue());
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("cc2")){
    			 idBienInmueble.setDigControl2(nodeDatosDcho.getFirstChild().getNodeValue());
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("cdr")){
    			 derecho.setCodDerecho(nodeDatosDcho.getFirstChild().getNodeValue());
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("nif")){
    			 representante.setNif(nodeDatosDcho.getFirstChild().getNodeValue());
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("nom")){
    			 representante.setRazonSocial(nodeDatosDcho.getFirstChild().getNodeValue());
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("locat")){
    			 
    			 NodeList listaDatosLocat= nodeDatosDcho.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 identificadorDerecho.setCodigoDelegacion(nodeLocat.getFirstChild().getNodeValue());
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 identificadorDerecho.setCodigoMunicipio(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
				 
			 }
    		 else if (nodeDatosDcho.getNodeType() == Node.ELEMENT_NODE && nodeDatosDcho.getNodeName().equals("exp")){
				 identificadorDerecho.setExpediente(parsearDatosExpediente(node));
			 }
 
		 }
		 idBienInmueble.setParcelaCatastral(pc1+pc2);
		 identificadorDerecho.setRepresentante(representante);
		 derecho.setIdBienInmueble(idBienInmueble);
		 identificadorDerecho.setDerecho(derecho);
		 
		 return identificadorDerecho;
	 }
	 
	 
	 private static Expediente parsearDatosExpedienteGerencia(Node node, Expediente expediente){
		 
		 NodeList listaDatosExpg = node.getChildNodes();
		 for (int j = 0; j < listaDatosExpg.getLength(); j++) {

			 Node nodeDatosExpg = listaDatosExpg.item(j);
    		 if (nodeDatosExpg.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpg.getNodeName().equals("aexpg"))
             {
    			 expediente.setAnnoExpedienteGerencia(Integer.valueOf(nodeDatosExpg.getFirstChild().getNodeValue()));
             }
    		 else if (nodeDatosExpg.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpg.getNodeName().equals("rexpg"))
             {
    			 expediente.setReferenciaExpedienteGerencia(nodeDatosExpg.getFirstChild().getNodeValue());
             }
    		 else if (nodeDatosExpg.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpg.getNodeName().equals("ero"))
             {
    			 expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(Integer.valueOf(nodeDatosExpg.getFirstChild().getNodeValue()));
             }
		 }
		 
		 return expediente;
	 }
		 
	 private static Expediente parsearDatosProtocoloNotarialOrigen(Node node, Expediente expediente){

		 NodeList listaDatosPnot = node.getChildNodes();
		 for (int i = 0; i < listaDatosPnot.getLength(); i++) {
			 
			 Node nodeDatosPnot = listaDatosPnot.item(i);
			 
			 if (nodeDatosPnot.getNodeType() == Node.ELEMENT_NODE && nodeDatosPnot.getNodeName().equals("npn")){
				 
				 NodeList listaDatosNpn = nodeDatosPnot.getChildNodes();
				 for (int h = 0; h < listaDatosNpn.getLength(); h++) {
					 Node nodeDatosNpn = listaDatosPnot.item(h);
					 
					 if (nodeDatosNpn.getNodeType() == Node.ELEMENT_NODE && nodeDatosNpn.getNodeName().equals("not")){
						 NodeList listaDatosNot = nodeDatosNpn.getChildNodes();
						 for (int t = 0; t< listaDatosNot.getLength(); t++) {
		    				 
		    				 Node nodeDatosNot = listaDatosNot.item(t);
		    				 if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cp")) {
		    	    			 expediente.setCodProvinciaNotaria(nodeDatosNot.getFirstChild().getNodeValue());
		    	             }
		    				 else if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cpb")){
		    					 expediente.setCodPoblacionNotaria(nodeDatosNot.getFirstChild().getNodeValue());
		    	             }
		    				 else if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cnt")){
		    					 expediente.setCodNotaria(nodeDatosNot.getFirstChild().getNodeValue());
		    	             }
		    			 }
		             }
					 else if (nodeDatosNpn.getNodeType() == Node.ELEMENT_NODE && nodeDatosNpn.getNodeName().equals("pn")){
						 NodeList listaDatosPn = nodeDatosNpn.getChildNodes();
						 for (int t = 0; t < listaDatosPn.getLength(); t++) {
		    				 
		    				 Node nodeDatosExpec = listaDatosPn.item(t);
		    				 if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("aprt")){
		    	    			 expediente.setAnnoProtocoloNotarial(nodeDatosExpec.getFirstChild().getNodeValue());
		    	             }
		    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("prt")){
		    					 expediente.setProtocoloNotarial(nodeDatosExpec.getFirstChild().getNodeValue());
		    	             }
		
		    			 }
		             }
					 else if (nodeDatosNpn.getNodeType() == Node.ELEMENT_NODE && nodeDatosNpn.getNodeName().equals("expg")){
            			 expediente = parsearDatosExpedienteGerencia(nodeDatosNpn, exp);
            		 }
				
				 }
			 }
		 }
		 return expediente;
	 }
	 
	 private static Expediente parsearDatosProtocoloNotarial(Node node, Expediente expediente){

		 NodeList listaDatosNpn = node.getChildNodes();
		 for (int h = 0; h < listaDatosNpn.getLength(); h++) {
			 
			 Node nodeDatosNpn = listaDatosNpn.item(h);
			 if (nodeDatosNpn.getNodeType() == Node.ELEMENT_NODE && nodeDatosNpn.getNodeName().equals("not")){
				 NodeList listaDatosNot = nodeDatosNpn.getChildNodes();
				 for (int t = 0; t< listaDatosNot.getLength(); t++) {
    				 
    				 Node nodeDatosNot = listaDatosNot.item(t);
    				 if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cp")) {
    	    			 expediente.setCodProvinciaNotaria(nodeDatosNot.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cpb")){
    					 expediente.setCodPoblacionNotaria(nodeDatosNot.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosNot.getNodeType() == Node.ELEMENT_NODE && nodeDatosNot.getNodeName().equals("cnt")){
    					 expediente.setCodNotaria(nodeDatosNot.getFirstChild().getNodeValue());
    	             }
    			 }
             }
			 else if (nodeDatosNpn.getNodeType() == Node.ELEMENT_NODE && nodeDatosNpn.getNodeName().equals("pn")){
				 NodeList listaDatosPn = nodeDatosNpn.getChildNodes();
				 for (int t = 0; t < listaDatosPn.getLength(); t++) {
    				 
    				 Node nodeDatosExpec = listaDatosPn.item(t);
    				 if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("aprt")){
    	    			 expediente.setAnnoProtocoloNotarial(nodeDatosExpec.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("prt")){
    					 expediente.setProtocoloNotarial(nodeDatosExpec.getFirstChild().getNodeValue());
    	             }

    			 }
             }
		 }
		 return expediente;
	 }
		 
	 private static Expediente parsearDatosExpediente(Node node){
		 Expediente expediente = new Expediente();
		 
		 NodeList listaDatosExpediente = node.getChildNodes();
		 for (int i = 0; i < listaDatosExpediente.getLength(); i++) {
			 
			 Node nodeDatosExpediente = listaDatosExpediente.item(i);
			 			 
			 if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("tint")){
    			 expediente.setTipoDeIntercambio(nodeDatosExpediente.getFirstChild().getNodeValue());
             }
			 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("texp")){	
				 TipoExpediente tipoExpediente = new TipoExpediente();
				 tipoExpediente.setCodigoTipoExpediente(nodeDatosExpediente.getFirstChild().getNodeValue());
				 expediente.setTipoExpediente(tipoExpediente);
             }
			 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("fac")){	
				 expediente.setFechaAlteracion(Date.valueOf(nodeDatosExpediente.getFirstChild().getNodeValue()));
             }
			 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("expg")){
				 
				 parsearDatosExpedienteGerencia(nodeDatosExpediente, expediente);

             }
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("expec")){
    			 NodeList listaDatosExpec = nodeDatosExpediente.getChildNodes();
    			 for (int j = 0; j < listaDatosExpec.getLength(); j++) {
    				 
    				 Node nodeDatosExpec = listaDatosExpec.item(j);
    				 if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("aexpec")){
    	    			 expediente.setAnnoExpedienteAdminOrigenAlteracion(Integer.valueOf(nodeDatosExpec.getFirstChild().getNodeValue()));
    	             }
    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("rexpec")){
    					 expediente.setReferenciaExpedienteAdminOrigen(nodeDatosExpec.getFirstChild().getNodeValue());
    					 //expediente.setReferenciaExpedienteAdminOrigen(String.valueOf(exp.getIdExpediente()));
    	             }
    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("eoa")){
    					 expediente.setCodigoDescriptivoAlteracion(nodeDatosExpec.getFirstChild().getNodeValue());
    	             }
    			 }
             }
			 /*
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("expa")){
    			 NodeList listaDatosExpec = nodeDatosExpediente.getChildNodes();
    			 for (int j = 0; j < listaDatosExpec.getLength(); j++) {
    				 
    				 Node nodeDatosExpec = listaDatosExpec.item(j);
    				 if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("aexpg"))
    	             {
    	    			 expediente.setAnnoExpedienteAdminOrigenAlteracion(Integer.valueOf(nodeDatosExpec.getFirstChild().getNodeValue()));
    	             }
    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("rexpg"))
    	             {
    					 expediente.setReferenciaExpedienteAdminOrigen(nodeDatosExpec.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosExpec.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpec.getNodeName().equals("ero"))
    	             {
    					 expediente.setCodigoDescriptivoAlteracion(nodeDatosExpec.getFirstChild().getNodeValue());
    	             }
    			 }
             }
             */
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("fre")){
    			 expediente.setFechaRegistro(Date.valueOf(nodeDatosExpediente.getFirstChild().getNodeValue()));
             }

    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("inr")){
    			
    			 NodeList listaDatosInr = nodeDatosExpediente.getChildNodes();
    			 for (int j = 0; j < listaDatosInr.getLength(); j++) {
    				 
    				 Node nodeDatosInr = listaDatosInr.item(j);
    				 if (nodeDatosInr.getNodeType() == Node.ELEMENT_NODE && nodeDatosInr.getNodeName().equals("npn")){
    					 
    					 expediente = parsearDatosProtocoloNotarial(nodeDatosInr, expediente);
    		
    	             } 
    			 }
             }
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("decl")){

    			 NodeList listaDatosDecl = nodeDatosExpediente.getChildNodes();
    			 for (int j = 0; j < listaDatosDecl.getLength(); j++) {
    				 
    				 Node nodeDatosDecl = listaDatosDecl.item(j);
    				 if (nodeDatosDecl.getNodeType() == Node.ELEMENT_NODE && nodeDatosDecl.getNodeName().equals("doco"))
    	             {
    					 expediente.setTipoDocumentoOrigenAlteracion(nodeDatosDecl.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosDecl.getNodeType() == Node.ELEMENT_NODE && nodeDatosDecl.getNodeName().equals("idoco"))
    	             {
    					 expediente.setInfoDocumentoOrigenAlteracion(nodeDatosDecl.getFirstChild().getNodeValue());
    	             }
    				 else if (nodeDatosDecl.getNodeType() == Node.ELEMENT_NODE && nodeDatosDecl.getNodeName().equals("nbi"))
    	             {
    					 NodeList listaDatosNbi = nodeDatosDecl.getChildNodes();
    					 for (int t = 0; t < listaDatosNbi.getLength(); t++) {
    	    				 
    	    				 Node nodeDatosNbi = listaDatosNbi.item(t);
    	    				 if (nodeDatosNbi.getNodeType() == Node.ELEMENT_NODE && nodeDatosNbi.getNodeName().equals("nbu"))
    	    	             {
    	    	    			 expediente.setNumBienesInmueblesUrbanos(Integer.valueOf(nodeDatosNbi.getFirstChild().getNodeValue()));
    	    	             }
    	    				 else if (nodeDatosNbi.getNodeType() == Node.ELEMENT_NODE && nodeDatosNbi.getNodeName().equals("nbr"))
    	    	             {
    	    					 expediente.setNumBienesInmueblesRusticos(Integer.valueOf(nodeDatosNbi.getFirstChild().getNodeValue()));
    	    	             }
    	    				 else if (nodeDatosNbi.getNodeType() == Node.ELEMENT_NODE && nodeDatosNbi.getNodeName().equals("nbce"))
    	    	             {
    	    					 expediente.setNumBienesInmueblesCaractEsp(Integer.valueOf(nodeDatosNbi.getFirstChild().getNodeValue()));
    	    	             }

    	    			 }
    	             }
    			 }	 
             }
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("cdeac")){
    			 expediente.setCodigoDescriptivoAlteracion(nodeDatosExpediente.getFirstChild().getNodeValue());
             }
    		 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("deac")){
    			 expediente.setDescripcionAlteracion(nodeDatosExpediente.getFirstChild().getNodeValue());
		 	 }
			 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("dec")){
				 NodeList listaDatosDec = nodeDatosExpediente.getChildNodes();
    			 for (int j = 0; j < listaDatosDec.getLength(); j++) {
    				 
    				 Node nodeDatosDec = listaDatosDec.item(j);
    				 if (nodeDatosDec.getNodeType() == Node.ELEMENT_NODE && nodeDatosDec.getNodeName().equals("idp"))
    	             {
    					 NodeList listaDatosIdp = nodeDatosDec.getChildNodes();
    					 for (int t = 0; t < listaDatosIdp.getLength(); t++) {
    	    				 
    	    				 Node nodeDatosIdp = listaDatosIdp.item(t);
    	    				 if (nodeDatosIdp.getNodeType() == Node.ELEMENT_NODE && nodeDatosIdp.getNodeName().equals("nif"))
    	    	             {
    	    	    			 expediente.setNifPresentador(nodeDatosIdp.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosIdp.getNodeType() == Node.ELEMENT_NODE && nodeDatosIdp.getNodeName().equals("nom"))
    	    	             {
    	    					 expediente.setNombreCompletoPresentador(nodeDatosIdp.getFirstChild().getNodeValue());
    	    	             }


    	    			 }
    	             }
    				 else if (nodeDatosDec.getNodeType() == Node.ELEMENT_NODE && nodeDatosDec.getNodeName().equals("dfn"))
    	             {
    					 DireccionLocalizacion direccion = new DireccionLocalizacion();
    					 NodeList listaDatosDfn = nodeDatosDec.getChildNodes();
    					 for (int t = 0; t < listaDatosDfn.getLength(); t++) {
    	    				 
    	    				 Node nodeDatosDfn = listaDatosDfn.item(t);
    	    				 if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("loine"))
    	    	             {
    	    					 NodeList listaDatosLoine = nodeDatosDfn.getChildNodes();
    	    					 for (int p = 0; p < listaDatosLoine.getLength(); p++) {
    	    	    				 
    	    	    				 Node nodeDatosLoine = listaDatosLoine.item(p);
    	    	    				 if (nodeDatosLoine.getNodeType() == Node.ELEMENT_NODE && nodeDatosLoine.getNodeName().equals("cp"))
    	    	    	             {
    	    	    					 direccion.setProvinciaINE(nodeDatosLoine.getFirstChild().getNodeValue());
    	    	    	             }
    	    	    				 else if (nodeDatosLoine.getNodeType() == Node.ELEMENT_NODE && nodeDatosLoine.getNodeName().equals("cm"))
    	    	    	             {
    	    	    					 expediente.setCodigoINEmunicipio(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	    	             }
    	    	    			 }
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("cmc"))
    	    	             {
    	    					 direccion.setCodigoMunicipioDGC(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("np"))
    	    	             {
    	    					 direccion.setNombreProvincia(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("nm"))
    	    	             {
    	    					 direccion.setNombreMunicipio(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("nem"))
    	    	             {
    	    					 direccion.setNombreEntidadMenor(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("dir"))
    	    	             {
    	    					 direccion = parsearDatosDireccion(nodeDatosDfn, direccion);
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("loint"))
    	    	             {
    	    					 direccion.setCodigoMunicipioDGC(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }
    	    				 else if (nodeDatosDfn.getNodeType() == Node.ELEMENT_NODE && nodeDatosDfn.getNodeName().equals("pos"))
    	    	             {
    	    					 direccion.setCodigoMunicipioDGC(nodeDatosDfn.getFirstChild().getNodeValue());
    	    	             }

    	    			 }
    					 expediente.setDireccionPresentador(direccion);
    	             }
    			 }	 
             }
			 else if (nodeDatosExpediente.getNodeType() == Node.ELEMENT_NODE && nodeDatosExpediente.getNodeName().equals("fce")){
				 expediente.setFechaDeCierre(Date.valueOf(nodeDatosExpediente.getFirstChild().getNodeValue()));
             } 		 
		 }	 
		 return expediente;
	 }
	 
	 private static DireccionLocalizacion parsearDatosDireccion(Node node, DireccionLocalizacion direccion){
		 NodeList listaDatosDir = node.getChildNodes();
		 for (int p = 0; p < listaDatosDir.getLength(); p++) {
			 
			 Node nodeDatosDir = listaDatosDir.item(p);
			 if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("cv"))
             {
				 direccion.setCodigoVia(Integer.valueOf(nodeDatosDir.getFirstChild().getNodeValue()));
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("tv"))
             {
				 direccion.setTipoVia(nodeDatosDir.getFirstChild().getNodeValue());
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("nv"))
             {
				 direccion.setNombreVia(nodeDatosDir.getFirstChild().getNodeValue());
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("pnp"))
             {
				 direccion.setPrimerNumero(Integer.valueOf(nodeDatosDir.getFirstChild().getNodeValue()));
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("plp"))
             {
				 direccion.setPrimeraLetra(nodeDatosDir.getFirstChild().getNodeValue());
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("snp"))
             {
				 direccion.setSegundoNumero(Integer.valueOf(nodeDatosDir.getFirstChild().getNodeValue()));
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("slp"))
             {
				 direccion.setSegundaLetra(nodeDatosDir.getFirstChild().getNodeValue());
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("km"))
             {
				 direccion.setKilometro(Double.valueOf(nodeDatosDir.getFirstChild().getNodeValue().replace(",", ".")));
             }
			 else if (nodeDatosDir.getNodeType() == Node.ELEMENT_NODE && nodeDatosDir.getNodeName().equals("td"))
             {
				 direccion.setDireccionNoEstructurada(nodeDatosDir.getFirstChild().getNodeValue());
             }
		 }
		 
		 return direccion;
	 }
	 
	 private static DireccionLocalizacion parsearDatosLocalizacionUrbana(Node node, DireccionLocalizacion direccionLocalizacion){

		 NodeList listaDatosLourb= node.getChildNodes();
		 for (int h = 0; h < listaDatosLourb.getLength(); h++) {
			 Node nodeLourb = listaDatosLourb.item(h);
			 if (nodeLourb.getNodeType() == Node.ELEMENT_NODE && nodeLourb.getNodeName().equals("dir")){
				 direccionLocalizacion = parsearDatosDireccion(nodeLourb, direccionLocalizacion);
			 }
			 else if (nodeLourb.getNodeType() == Node.ELEMENT_NODE && nodeLourb.getNodeName().equals("bl")){
				 direccionLocalizacion.setBloque(nodeLourb.getFirstChild().getNodeValue());
			 }
			 else if (nodeLourb.getNodeType() == Node.ELEMENT_NODE && nodeLourb.getNodeName().equals("loint")){
				 NodeList listaDatosLoint= nodeLourb.getChildNodes();
				 for (int t = 0; t < listaDatosLoint.getLength(); t++) {
					 Node nodeLoint = listaDatosLoint.item(t);
					 if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("bq")){
						 direccionLocalizacion.setBloque(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("es")){
						 direccionLocalizacion.setEscalera(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("pt")){
						 direccionLocalizacion.setPlanta(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("pu")){
						 direccionLocalizacion.setPuerta(nodeLoint.getFirstChild().getNodeValue());
					 }
				 }
			 }
			 else if (nodeLourb.getNodeType() == Node.ELEMENT_NODE && nodeLourb.getNodeName().equals("dp")){
				 direccionLocalizacion.setCodigoPostal(nodeLourb.getFirstChild().getNodeValue());
			 }
			 else if (nodeLourb.getNodeType() == Node.ELEMENT_NODE && nodeLourb.getNodeName().equals("dm")){
				 direccionLocalizacion.setDistrito(nodeLourb.getFirstChild().getNodeValue());
			 }
		 }	 
		 
		 return direccionLocalizacion;
	 }
	 
	 private static DireccionLocalizacion parsearDatosLocalizacionRustica(Node node, DireccionLocalizacion direccionLocalizacion){
		 
		 NodeList listaDatosLorus= node.getChildNodes();
		 for (int h = 0; h < listaDatosLorus.getLength(); h++) {
			 Node nodeLorus = listaDatosLorus.item(h);
			 
			 if (nodeLorus.getNodeType() == Node.ELEMENT_NODE && nodeLorus.getNodeName().equals("cma")){
				 direccionLocalizacion.setCodMunOrigenAgregacion(nodeLorus.getFirstChild().getNodeValue());
			 }
			 else if (nodeLorus.getNodeType() == Node.ELEMENT_NODE && nodeLorus.getNodeName().equals("czc")){
				 direccionLocalizacion.setCodZonaConcentracion(nodeLorus.getFirstChild().getNodeValue());
			 }
			 else if (nodeLorus.getNodeType() == Node.ELEMENT_NODE && nodeLorus.getNodeName().equals("cpp")){
				 NodeList listaDatosCpp= nodeLorus.getChildNodes();
				 for (int q = 0; q < listaDatosCpp.getLength(); q++) {
					 Node nodeCpp = listaDatosCpp.item(q);
					 if (nodeCpp.getNodeType() == Node.ELEMENT_NODE && nodeCpp.getNodeName().equals("cpo")){
						 direccionLocalizacion.setCodPoligono(nodeCpp.getFirstChild().getNodeValue());
					 }
					 else if (nodeCpp.getNodeType() == Node.ELEMENT_NODE && nodeCpp.getNodeName().equals("cpa")){
						 direccionLocalizacion.setCodParcela(nodeCpp.getFirstChild().getNodeValue());
					 }
				 }
			 }
			 else if (nodeLorus.getNodeType() == Node.ELEMENT_NODE && nodeLorus.getNodeName().equals("npa")){
				 direccionLocalizacion.setNombreParaje(nodeLorus.getFirstChild().getNodeValue());
			 }
			 else if (nodeLorus.getNodeType() == Node.ELEMENT_NODE && nodeLorus.getNodeName().equals("cpaj")){
				 direccionLocalizacion.setCodParaje(nodeLorus.getFirstChild().getNodeValue());
			 }
		 }
		 
		 return direccionLocalizacion;
	 }
	 
	 private static DatosEconomicosBien parsearDatosEconomicosBien(Node node){
		DatosEconomicosBien datosEconomicosBien = new DatosEconomicosBien();
		 NodeList listaDatosDebi= node.getChildNodes();
		 for (int j = 0; j < listaDatosDebi.getLength(); j++) {
			 Node nodeDebi = listaDatosDebi.item(j);
			 if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("pav")){
				 datosEconomicosBien.setPrecioVenta(Double.valueOf(nodeDebi.getFirstChild().getNodeValue().replace(",",".")));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("afv")){
				 datosEconomicosBien.setAnioFinValoracion(Integer.valueOf(nodeDebi.getFirstChild().getNodeValue()));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("itp")){
				 datosEconomicosBien.setIndTipoPropiedad(nodeDebi.getFirstChild().getNodeValue()); 
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("noe")){
				 datosEconomicosBien.setNumOrdenHorizontal(nodeDebi.getFirstChild().getNodeValue());
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("avc")){
				 datosEconomicosBien.setAnioValorCat(Integer.valueOf(nodeDebi.getFirstChild().getNodeValue()));		 
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("vcat")){
				 datosEconomicosBien.setValorCatastral(Double.valueOf(nodeDebi.getFirstChild().getNodeValue().replace(",",".")));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("vcs")){
				 datosEconomicosBien.setValorCatastralSuelo(Double.valueOf(nodeDebi.getFirstChild().getNodeValue().replace(",",".")));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("vcc")){
				 datosEconomicosBien.setValorCatastralConstruccion(Double.valueOf(nodeDebi.getFirstChild().getNodeValue().replace(",",".")));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("bl")){
				 datosEconomicosBien.setBaseLiquidable(Long.valueOf(nodeDebi.getFirstChild().getNodeValue()));		 
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("uso")){
				 datosEconomicosBien.setUso(nodeDebi.getFirstChild().getNodeValue());
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("bru")){
				 NodeList listaDatosBru= nodeDebi.getChildNodes();
				 for (int m = 0; m < listaDatosBru.getLength(); m++) {
					 Node nodeBru = listaDatosBru.item(m);
					 if (nodeBru.getNodeType() == Node.ELEMENT_NODE && nodeBru.getNodeName().equals("bvc")){
						  datosEconomicosBien.setImporteBonificacionRustica(Long.valueOf(nodeDebi.getFirstChild().getNodeValue()));
					 }
					 else  if (nodeBru.getNodeType() == Node.ELEMENT_NODE && nodeBru.getNodeName().equals("bcl")){
						 datosEconomicosBien.setClaveBonificacionRustica(nodeDebi.getFirstChild().getNodeValue());
					 }
				 }
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("sfc")){
				 datosEconomicosBien.setSuperficieCargoFincaConstruida(Long.valueOf(nodeDebi.getFirstChild().getNodeValue()));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("sfs")){
				 datosEconomicosBien.setSuperficieCargoFincaRustica(Long.valueOf(nodeDebi.getFirstChild().getNodeValue()));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("cpt")){
				 datosEconomicosBien.setCoefParticipacion(Float.valueOf(nodeDebi.getFirstChild().getNodeValue().replace(",",".")));
			 }
			 else  if (nodeDebi.getNodeType() == Node.ELEMENT_NODE && nodeDebi.getNodeName().equals("ant")){
				 datosEconomicosBien.setAnioAntiguedad(Integer.valueOf(nodeDebi.getFirstChild().getNodeValue()));
			 }
		 }
		
		return datosEconomicosBien; 
	 }
	 
	 
	 private static DireccionLocalizacion parsearDatosDomicilioTributario(Node node){
		 DireccionLocalizacion direccionLocalizacion = new DireccionLocalizacion();
		 
		 NodeList listaDatosDt= node.getChildNodes();
		 for (int j = 0; j < listaDatosDt.getLength(); j++) {
			 Node nodeDt = listaDatosDt.item(j);
			 
			 if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("loine")){

				 NodeList listaDatosLoine= nodeDt.getChildNodes();
				 for (int k = 0; k < listaDatosLoine.getLength(); k++) {
					 Node nodeLoine = listaDatosLoine.item(k);
					 if (nodeLoine.getNodeType() == Node.ELEMENT_NODE && nodeLoine.getNodeName().equals("cp")){
						 direccionLocalizacion.setProvinciaINE(nodeLoine.getFirstChild().getNodeValue());
					 }	
					 else if (nodeLoine.getNodeType() == Node.ELEMENT_NODE && nodeLoine.getNodeName().equals("cm")){
						 direccionLocalizacion.setMunicipioINE(nodeLoine.getFirstChild().getNodeValue());
					 }
				 }
             }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("cmc")){
				 direccionLocalizacion.setCodigoMunicipioDGC(nodeDt.getFirstChild().getNodeValue());
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("np")){
				 direccionLocalizacion.setNombreProvincia(nodeDt.getFirstChild().getNodeValue());		 
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("nm")){
				 direccionLocalizacion.setNombreMunicipio(nodeDt.getFirstChild().getNodeValue());
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("nem")){
				 direccionLocalizacion.setNombreEntidadMenor(nodeDt.getFirstChild().getNodeValue());
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("dir")){
				 direccionLocalizacion = parsearDatosDireccion(nodeDt, direccionLocalizacion);
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("loint")){
				 NodeList listaDatosLoint= nodeDt.getChildNodes();
				 for (int t = 0; t < listaDatosLoint.getLength(); t++) {
					 Node nodeLoint = listaDatosLoint.item(t);
					 if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("bq")){
						 direccionLocalizacion.setBloque(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("es")){
						 direccionLocalizacion.setEscalera(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("pt")){
						 direccionLocalizacion.setPlanta(nodeLoint.getFirstChild().getNodeValue());
					 }
					 else if (nodeLoint.getNodeType() == Node.ELEMENT_NODE && nodeLoint.getNodeName().equals("pu")){
						 direccionLocalizacion.setPuerta(nodeLoint.getFirstChild().getNodeValue());
					 }
				 }
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("pos")){
				 NodeList listaDatosPos= nodeDt.getChildNodes();
				 for (int t = 0; t < listaDatosPos.getLength(); t++) {
					 Node nodePos = listaDatosPos.item(t);
					 if (nodePos.getNodeType() == Node.ELEMENT_NODE && nodePos.getNodeName().equals("dp")){
						 direccionLocalizacion.setCodigoPostal(nodePos.getFirstChild().getNodeValue());
					 }
					 else  if (nodePos.getNodeType() == Node.ELEMENT_NODE && nodePos.getNodeName().equals("ac")){
						 direccionLocalizacion.setApartadoCorreos(Integer.valueOf(nodePos.getFirstChild().getNodeValue()));
					 }
				 }
			 }
			 else if (nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("locs")){
				 NodeList listaDatosLocs= nodeDt.getChildNodes();
				 for (int k = 0; k < listaDatosLocs.getLength(); k++) {
					 Node nodeLocs = listaDatosLocs.item(k);
					 if (nodeLocs.getNodeType() == Node.ELEMENT_NODE && nodeLocs.getNodeName().equals("lous")){

						 NodeList listaDatosLous= nodeLocs.getChildNodes();
						 for (int p = 0; p < listaDatosLous.getLength(); p++) {
							 Node nodeLous = listaDatosLous.item(p);
							 if (nodeLous.getNodeType() == Node.ELEMENT_NODE && nodeLous.getNodeName().equals("lourb")){

								 direccionLocalizacion = parsearDatosLocalizacionUrbana(nodeLous, direccionLocalizacion);

							 }
							 else if (nodeLous.getNodeType() == Node.ELEMENT_NODE && nodeLous.getNodeName().equals("lorus")){
								 direccionLocalizacion = parsearDatosLocalizacionRustica(nodeLous, direccionLocalizacion);
							 }
						 }	 
					 }	
					 else if (nodeLocs.getNodeType() == Node.ELEMENT_NODE && nodeLocs.getNodeName().equals("lors")){
						 NodeList listaDatosLors= nodeLocs.getChildNodes();
						 for (int h = 0; h < listaDatosLors.getLength(); h++) {
							 Node nodeLors = listaDatosLors.item(h);
							 if (nodeLors.getNodeType() == Node.ELEMENT_NODE && nodeLors.getNodeName().equals("lorus")){
								 
								 direccionLocalizacion = parsearDatosLocalizacionRustica(nodeLors, direccionLocalizacion);
								 
							 }
							 else if (nodeLors.getNodeType() == Node.ELEMENT_NODE && nodeLors.getNodeName().equals("lourb")){
								 direccionLocalizacion = parsearDatosLocalizacionUrbana(nodeLors, direccionLocalizacion);

							 }
						 }
					 }
				 }
			 }
		 }
		 return direccionLocalizacion;
	 }
	 
	 
	 private static BienInmuebleCatastro parsearDatosIdentificacionBienInmueble(Node node, BienInmuebleCatastro bien){

		 NodeList listaDatosIdbicat= node.getChildNodes();
		 for (int k = 0; k < listaDatosIdbicat.getLength(); k++) {

			 Node nodeIdbicat = listaDatosIdbicat.item(k);
			 if (nodeIdbicat.getNodeType() == Node.ELEMENT_NODE && nodeIdbicat.getNodeName().equals("cn")){
				 bien.setClaseBienInmueble(nodeIdbicat.getFirstChild().getNodeValue()); 
             }
			 else if (nodeIdbicat.getNodeType() == Node.ELEMENT_NODE && nodeIdbicat.getNodeName().equals("rc")){
				 
				 IdBienInmueble idBienInmueble = new IdBienInmueble();
				 String pc1 = "";
				 String pc2 = "";
				 
				 NodeList listaDatosRc= nodeIdbicat.getChildNodes();
				 for (int w = 0; w < listaDatosRc.getLength(); w++) {
					
					
    				 Node nodeRc = listaDatosRc.item(w);
    				 if (nodeRc.getNodeType() == Node.ELEMENT_NODE && nodeRc.getNodeName().equals("pc1")){
    					 pc1 =nodeRc.getFirstChild().getNodeValue(); 
                     }
    				 else if (nodeRc.getNodeType() == Node.ELEMENT_NODE && nodeRc.getNodeName().equals("pc2")){
    					 pc2 = nodeRc.getFirstChild().getNodeValue();
                     }
    				 else if (nodeRc.getNodeType() == Node.ELEMENT_NODE && nodeRc.getNodeName().equals("car")){
    					 idBienInmueble.setNumCargo(nodeRc.getFirstChild().getNodeValue());
                     }
    				 else if (nodeRc.getNodeType() == Node.ELEMENT_NODE && nodeRc.getNodeName().equals("cc1")){
    					 idBienInmueble.setDigControl1(nodeRc.getFirstChild().getNodeValue());
                     }
    				 else if (nodeRc.getNodeType() == Node.ELEMENT_NODE && nodeRc.getNodeName().equals("cc2")){
    					 idBienInmueble.setDigControl2(nodeRc.getFirstChild().getNodeValue());
                     }
    			 }
				 
				 idBienInmueble.setParcelaCatastral(pc1+pc2);
				 bien.setIdBienInmueble(idBienInmueble); 
             }
			 else if (nodeIdbicat.getNodeType() == Node.ELEMENT_NODE && nodeIdbicat.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeIdbicat.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 
//    					 EntidadGeneradora entidadGen = new EntidadGeneradora();
//    					 entidadGen.setCodigo(Integer.valueOf(nodeLocat.getFirstChild().getNodeValue()));
//    					 bien.getDatosExpediente().setEntidadGeneradora(entidadGen);

    					 DireccionLocalizacion direccion = new DireccionLocalizacion();
    					 direccion.setProvinciaINE(nodeLocat.getFirstChild().getNodeValue());
    					 bien.setDomicilioTributario(direccion);
    					 
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 bien.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
			 else if(nodeIdbicat.getNodeType() == Node.ELEMENT_NODE && nodeIdbicat.getNodeName().equals("desb")){
				 bien.setClaseBienInmueble(nodeIdbicat.getFirstChild().getNodeValue());
			 }
		 }

		 return bien;
	 }
	 
	 private static BienInmuebleCatastro parsearDatosIdentificacionBienInmuebleOrigen(Node node, BienInmuebleCatastro bien){

		 NodeList listaDatosIdbicat= node.getChildNodes();
		 String pc1= "";
		 String pc2 = "";
		 IdBienInmueble idBienInmueble = new IdBienInmueble();
		 
		 for (int k = 0; k < listaDatosIdbicat.getLength(); k++) {

			 Node nodeBien = listaDatosIdbicat.item(k);
			 if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("pc1")){
    			 
    			 pc1 = nodeBien.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("pc2")){
    			 
    			 pc2 = nodeBien.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("")){
    			 idBienInmueble.setNumCargo(nodeBien.getFirstChild().getNodeValue());
             }
			 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("cc1")){
				 idBienInmueble.setDigControl1(nodeBien.getFirstChild().getNodeValue());
             }
			 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("cc2")){
				 idBienInmueble.setDigControl2(nodeBien.getFirstChild().getNodeValue());
             }

    		 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeBien.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 DireccionLocalizacion direccion = new DireccionLocalizacion();
    					 direccion.setProvinciaINE(nodeLocat.getFirstChild().getNodeValue());
    					 bien.setDomicilioTributario(direccion);
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 bien.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
    		 else if (nodeBien.getNodeType() == Node.ELEMENT_NODE && nodeBien.getNodeName().equals("exp")){
    			 bien.setDatosExpediente(parsearDatosExpediente(nodeBien));
    		 } 
		 }
		 idBienInmueble.setParcelaCatastral(pc1+pc2);
		 bien.setIdBienInmueble(idBienInmueble);
		 return bien;
	 }
	 
	 
	 
	 private static FincaCatastro parsearDatosFincaCatastral(Node node){
		 FincaCatastro finca = new FincaCatastro();
		 ReferenciaCatastral referenciaCatastral = null;
		 String pc1 = "";
		 String pc2 = "";
		 NodeList listaDatosIdfcat = node.getChildNodes();
		 for (int k = 0; k < listaDatosIdfcat.getLength(); k++) {
			 Node nodeIdfcat = listaDatosIdfcat.item(k);
			 if (nodeIdfcat.getNodeType() == Node.ELEMENT_NODE && nodeIdfcat.getNodeName().equals("cn")){
				 finca.setBICE(nodeIdfcat.getFirstChild().getNodeValue()); 
             }
			 else if (nodeIdfcat.getNodeType() == Node.ELEMENT_NODE && nodeIdfcat.getNodeName().equals("pc")){
				 
				 NodeList listaDatosPc= nodeIdfcat.getChildNodes();
    			 for (int w = 0; w < listaDatosPc.getLength(); w++) {
    				
    				
    				 Node nodePc = listaDatosPc.item(w);
    				 if (nodePc.getNodeType() == Node.ELEMENT_NODE && nodePc.getNodeName().equals("pc1")){
    					 pc1 = nodePc.getFirstChild().getNodeValue(); 
                     }
    				 else if (nodePc.getNodeType() == Node.ELEMENT_NODE && nodePc.getNodeName().equals("pc2")){
    					 pc2 = nodePc.getFirstChild().getNodeValue();
                     }
    			 } 
             }
			 else if (nodeIdfcat.getNodeType() == Node.ELEMENT_NODE && nodeIdfcat.getNodeName().equals("locat")){
				 
				 NodeList listaDatosLocat= nodeIdfcat.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 finca.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue()); 
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 finca.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
		 }
		 
		 referenciaCatastral = new ReferenciaCatastral(pc1, pc2);
		 finca.setRefFinca(referenciaCatastral);

		 return finca;
	 }
	 
	 
	 private static FincaCatastro parsearDatosFincaOrigen(Node node){
		 
		 FincaCatastro finca = new FincaCatastro();
		 BienInmuebleCatastro bien = new BienInmuebleCatastro();
		 String pc1 = "";
		 String pc2 = "";
		 NodeList listaDatosFinca = node.getChildNodes();
    	 for (int j = 0; j < listaDatosFinca.getLength(); j++) {
    		 
    		 Node nodeFinca = listaDatosFinca.item(j);
    		 if (nodeFinca.getNodeType() == Node.ELEMENT_NODE && nodeFinca.getNodeName().equals("pc1")){
    			 
    			 pc1 = nodeFinca.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeFinca.getNodeType() == Node.ELEMENT_NODE && nodeFinca.getNodeName().equals("pc2")){
    			 
    			 pc2 = nodeFinca.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeFinca.getNodeType() == Node.ELEMENT_NODE && nodeFinca.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeFinca.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){

    					 DireccionLocalizacion direccion = new DireccionLocalizacion();
    					 direccion.setProvinciaINE(nodeLocat.getFirstChild().getNodeValue());
    					 bien.setDomicilioTributario(direccion);
    					 
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 bien.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
    		 else if (nodeFinca.getNodeType() == Node.ELEMENT_NODE && nodeFinca.getNodeName().equals("exp")){
    			 finca.setDatosExpediente(parsearDatosExpediente(nodeFinca));
    		 }
    	 }
    	 ReferenciaCatastral ref = new ReferenciaCatastral(pc1, pc2);
    	 finca.setRefFinca(ref);
		 return finca;
	 }
	 
	 
	 private static FincaCatastro parsearDatosFinca(Node node){
		 
		 FincaCatastro finca = new FincaCatastro();
		 BienInmuebleCatastro bien = new BienInmuebleCatastro();
		 
		 NodeList listaDatosFincacs = node.getChildNodes();
    	 for (int j = 0; j < listaDatosFincacs.getLength(); j++) {
    		 
    		 Node nodeFincacs = listaDatosFincacs.item(j);
    		 if (nodeFincacs.getNodeType() == Node.ELEMENT_NODE && nodeFincacs.getNodeName().equals("fincas")){
    			 
    			 NodeList listaDatosFincas = nodeFincacs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosFincas.getLength(); k++) {
    	    		 Node nodeFincas = listaDatosFincas.item(k);
    	    		 
    	    		 if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("idfcat")){
    	    			 finca = parsearDatosFincaCatastral(nodeFincas);
    	    		 }	
    	    		 else if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("rbice")){
        	    		 
    	    			 bien = parsearDatosIdentificacionBienInmueble(nodeFincas, bien);
    	    		 }	
    	    		 else if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("dt")){
        	    		 
    	    			 DireccionLocalizacion domicilioTributario = parsearDatosDomicilioTributario(nodeFincas);
    	    			 finca.setDirParcela(domicilioTributario);
    	    		 }	
    	    		 else if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("dff")){
    	    			 DatosFisicosFinca datosFisicos = new DatosFisicosFinca();
    	    			 NodeList listaDatosDff = nodeFincas.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosDff.getLength(); r++) {
    	    	    		 Node nodeDff = listaDatosDff.item(r);
    	    	    		 if (nodeDff.getNodeType() == Node.ELEMENT_NODE && nodeDff.getNodeName().equals("ssf")){
    	    	    			 NodeList listaDatosSsf = nodeDff.getChildNodes();
    	    	    	    	 for (int t = 0; t < listaDatosSsf.getLength(); t++) {
    	    	    	    		 Node nodeSsf = listaDatosSsf.item(t);
    	    	    	    		 
    	    	    	    		 if (nodeSsf.getNodeType() == Node.ELEMENT_NODE && nodeSsf.getNodeName().equals("ss")){
    	    	    	    			 datosFisicos.setSupFinca(Long.valueOf(nodeSsf.getFirstChild().getNodeValue()));
    	    	    	    		 }
    	    	    	    		 else if (nodeSsf.getNodeType() == Node.ELEMENT_NODE && nodeSsf.getNodeName().equals("sct")){
    	    	    	    			 datosFisicos.setSupTotal(Long.valueOf(nodeSsf.getFirstChild().getNodeValue()));
    	    	    	    		 }
    	    	    	    		 else if (nodeSsf.getNodeType() == Node.ELEMENT_NODE && nodeSsf.getNodeName().equals("ssr")){
    	    	    	    			 datosFisicos.setSupSobreRasante(Long.valueOf(nodeSsf.getFirstChild().getNodeValue()));
    	    	    	    		 }	 
    	    	    	    		 else if (nodeSsf.getNodeType() == Node.ELEMENT_NODE && nodeSsf.getNodeName().equals("sbr")){
    	    	    	    			 datosFisicos.setSupBajoRasante(Long.valueOf(nodeSsf.getFirstChild().getNodeValue()));
    	    	    	    		 }	 
    	    	    	    		 else if (nodeSsf.getNodeType() == Node.ELEMENT_NODE && nodeSsf.getNodeName().equals("sc")){
    	    	    	    			 datosFisicos.setSupCubierta(Long.valueOf(nodeSsf.getFirstChild().getNodeValue()));
    	    	    	    		 }	 
    	    	    	    	 }
    	    	    		 }
    	    	    		 else if (nodeDff.getNodeType() == Node.ELEMENT_NODE && nodeDff.getNodeName().equals("cen")){
    	    	    			 NodeList listaDatosCen = nodeDff.getChildNodes();
    	    	    	    	 for (int t = 0; t < listaDatosCen.getLength(); t++) {
    	    	    	    		 Node nodeCen = listaDatosCen.item(t);
    	    	    			 
	    	    	    			  if (nodeCen.getNodeType() == Node.ELEMENT_NODE && nodeCen.getNodeName().equals("coordx")){
		    	    	    			 datosFisicos.setXCoord(Float.valueOf(nodeCen.getFirstChild().getNodeValue().replace(",",".")));
		    	    	    		 }	 
	    	    	    			 else if (nodeCen.getNodeType() == Node.ELEMENT_NODE && nodeCen.getNodeName().equals("coordy")){
		    	    	    			 datosFisicos.setYCoord(Float.valueOf(nodeCen.getFirstChild().getNodeValue().replace(",",".")));
		    	    	    		 }	 
	    	    	    			 else if (nodeCen.getNodeType() == Node.ELEMENT_NODE && nodeCen.getNodeName().equals("srs")){
		    	    	    			 datosFisicos.setSRS(nodeCen.getFirstChild().getNodeValue());
		    	    	    		 }	 
    	    	    	    	 }
    	    	    		 }
    	    	    	 }
    	    	    	 
    	    	    	 finca.setDatosFisicos(datosFisicos);
    	    		 }	
    	    		 else if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("dval")){
    	    			 DatosEconomicosFinca datosEconomicos = new DatosEconomicosFinca();
    	    			 
    	    			 NodeList listaDatosDval = nodeFincas.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosDval.getLength(); r++) {
    	    	    		 Node nodeDval = listaDatosDval.item(r);
    	    	    		 if (nodeDval.getNodeType() == Node.ELEMENT_NODE && nodeDval.getNodeName().equals("aapv")){
    	    	    			 datosEconomicos.setAnioAprobacion(Integer.valueOf(nodeDval.getFirstChild().getNodeValue()));
    	    	    		 }
    	    	    		 else if (nodeDval.getNodeType() == Node.ELEMENT_NODE && nodeDval.getNodeName().equals("cfcvc")){
    	    	    			 datosEconomicos.setCodigoCalculoValor(Integer.valueOf(nodeDval.getFirstChild().getNodeValue()));
    	    	    		 }
    	    	    		 else if (nodeDval.getNodeType() == Node.ELEMENT_NODE && nodeDval.getNodeName().equals("cpo")){
    	    	    			 PonenciaPoligono ponenciaPoligono = new PonenciaPoligono();
    	    	    			 ponenciaPoligono.setCodPoligono(nodeDval.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeDval.getNodeType() == Node.ELEMENT_NODE && nodeDval.getNodeName().equals("mr")){
    	    	    			 datosEconomicos.setIndModalidadReparto(nodeDval.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeDval.getNodeType() == Node.ELEMENT_NODE && nodeDval.getNodeName().equals("iipvc")){
    	    	    			 datosEconomicos.setIndInfraedificabilidad(nodeDval.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
    	    	    	 finca.setDatosEconomicos(datosEconomicos);
    	    		 }	
    	    		 else if (nodeFincas.getNodeType() == Node.ELEMENT_NODE && nodeFincas.getNodeName().equals("evf")){
        	    		 PonenciaPoligono ponenciaPoligono = new PonenciaPoligono();
        	    		 NodeList listaDatosEvf = nodeFincas.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosEvf.getLength(); r++) {
    	    	    		 //ASD
    	    	    		 Node nodeEvf = listaDatosEvf.item(r);
    	    	    		 if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbc")){
    	    	    			 ponenciaPoligono.setImporteMBC(Double.valueOf(nodeEvf.getFirstChild().getNodeValue().replace(",",".")));
    	    	    		 }
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbrs")){
    	    	    			 ponenciaPoligono.setImporteMBR(Double.valueOf(nodeEvf.getFirstChild().getNodeValue().replace(",",".")));
    	    	    		 }
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbcci")){
    	    	    			
    	    	    		 }
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbrcni")){
    	    	    			
    	    	    		 }
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbrci")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("vuzc")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("vup")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("vrp")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("gb")){
    	    	    			 //ponenciaPoligono.setFlGB(Float.valueOf(nodeEvf.getFirstChild().getNodeValue()));
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("nbop")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("fbop")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("nbovm")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("fbovm")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("aantv")){
    	    	    			// ponenciaPoligono.setAnioNormas(nodeEvf.getFirstChild().getNodeValue());
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("gntv")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("fboom")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("nboom")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("cicir")){
    	    	    			
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbpe")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbpg")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mbpr")){
    	    	    			 
    	    	    		 } 
    	    	    		 else if (nodeEvf.getNodeType() == Node.ELEMENT_NODE && nodeEvf.getNodeName().equals("mcua")){
    	    	    			 
    	    	    		 } 
    	    	    	 } 
    	    		 }	
    	    	 }
    		 }
    		 else if (nodeFincacs.getNodeType() == Node.ELEMENT_NODE && nodeFincacs.getNodeName().equals("exp")){
    			 finca.setDatosExpediente(parsearDatosExpediente(nodeFincacs));
    		 }
    		 else if (nodeFincacs.getNodeType() == Node.ELEMENT_NODE && nodeFincacs.getNodeName().equals("tmovb")){
    			 finca.setTIPO_MOVIMIENTO(nodeFincacs.getFirstChild().getNodeValue());
    		 }
		 
    	 }
		 return finca;
	 }
	 
	 
	 private static SueloCatastro parsearDatosSueloOrigen(Node node, SueloCatastro suelo){
		 NodeList listaDatosSuelo = node.getChildNodes();
		 PonenciaZonaValor ponenciaZonaValor = new PonenciaZonaValor();
		 String pc1= "";
		 String pc2 = "";
    	 for (int j = 0; j < listaDatosSuelo.getLength(); j++) {
    		 
    		 
    		 Node nodeSuelo = listaDatosSuelo.item(j);
    		 if (nodeSuelo.getNodeType() == Node.ELEMENT_NODE && nodeSuelo.getNodeName().equals("pc1")){
    			 
    			 pc1 = nodeSuelo.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeSuelo.getNodeType() == Node.ELEMENT_NODE && nodeSuelo.getNodeName().equals("pc2")){
    			 
    			 pc2 = nodeSuelo.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeSuelo.getNodeType() == Node.ELEMENT_NODE && nodeSuelo.getNodeName().equals("subp")){
    			 
    			 suelo.setNumOrden( nodeSuelo.getFirstChild().getNodeValue());
    		
    		 }
    		 else if (nodeSuelo.getNodeType() == Node.ELEMENT_NODE && nodeSuelo.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeSuelo.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 suelo.setCodDelegacion(nodeLocat.getFirstChild().getNodeValue());
    					 
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 suelo.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
    		 else if (nodeSuelo.getNodeType() == Node.ELEMENT_NODE && nodeSuelo.getNodeName().equals("exp")){
    			 suelo.setDatosExpediente(parsearDatosExpediente(nodeSuelo));
    		 } 

    	 }
    	 ReferenciaCatastral ref = new ReferenciaCatastral(pc1, pc2);
    	 suelo.setRefParcela(ref);
		 return suelo;
	 }
	 
	 private static SueloCatastro parsearDatosSuelo(Node node, SueloCatastro suelo){
		 NodeList listaDatosSuelo = node.getChildNodes();
		 PonenciaZonaValor ponenciaZonaValor = new PonenciaZonaValor();
    	 for (int j = 0; j < listaDatosSuelo.getLength(); j++) {
    		 
    		 Node nodeSuelos = listaDatosSuelo.item(j);
    		 if (nodeSuelos.getNodeType() == Node.ELEMENT_NODE && nodeSuelos.getNodeName().equals("idsucat")){
    			 //Identificación del suelo con localización de la DGC    			 
    			 NodeList listaDatosIdsucat= nodeSuelos.getChildNodes();
    	    	 for (int h = 0; h < listaDatosIdsucat.getLength(); h++) {
    	    		 
    	    		 Node nodeIdsucat = listaDatosIdsucat.item(h);
    	    		 if (nodeIdsucat.getNodeType() == Node.ELEMENT_NODE && nodeIdsucat.getNodeName().equals("rsu")){
    	    			 
    	    			 String pc1 = "";
    	    			 String pc2 = "";
    	    			 NodeList listaDatosRsu= nodeIdsucat.getChildNodes();
    	    	    	 for (int k = 0; k < listaDatosRsu.getLength(); k++) {
    	    	    		 Node nodeRsu = listaDatosRsu.item(k);
    	    	    		 
    	    	    		 if (nodeRsu.getNodeType() == Node.ELEMENT_NODE && nodeRsu.getNodeName().equals("pc1")){
    	    	    			pc1 = nodeRsu.getFirstChild().getNodeValue();
    	    	    		 }
    	    	    		 else if (nodeRsu.getNodeType() == Node.ELEMENT_NODE && nodeRsu.getNodeName().equals("pc2")){
							    pc2 = nodeRsu.getFirstChild().getNodeValue();  			 
							 }
    	    	    		 else if (nodeRsu.getNodeType() == Node.ELEMENT_NODE && nodeRsu.getNodeName().equals("subp")){
								 suelo.setNumOrden(fillWithCeros(nodeRsu.getFirstChild().getNodeValue(), 4));
							 }
    	    	    	 }
    	    	    	 ReferenciaCatastral referenciaCatastral = new ReferenciaCatastral(pc1,pc2);
    	    	    	 suelo.setRefParcela(referenciaCatastral);

    	    	    	 suelo.setIdSuelo(referenciaCatastral.getRefCatastral()+suelo.getNumOrden());
    	    		 }
    	    		 else if (nodeIdsucat.getNodeType() == Node.ELEMENT_NODE && nodeIdsucat.getNodeName().equals("locat")){
    	    			 NodeList listaDatosLocat= nodeIdsucat.getChildNodes();
    	    	    	 for (int k = 0; k < listaDatosLocat.getLength(); k++) {
    	    	    		 Node nodeLocat = listaDatosLocat.item(k);
    	    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    	    			 suelo.setCodDelegacion(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    	    	    			 suelo.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
    	    		 }
    	    	 }
    		 }
    		 else if (nodeSuelos.getNodeType() == Node.ELEMENT_NODE && nodeSuelos.getNodeName().equals("dfsu")){
				 //Datos físicos del suelo
				 DatosFisicosSuelo datosFisicosSuelo = new DatosFisicosSuelo();
			 
				 NodeList listaDatosDsfu= nodeSuelos.getChildNodes();
    	    	 for (int k = 0; k < listaDatosDsfu.getLength(); k++) {
    	    		 Node nodeDsfu = listaDatosDsfu.item(k);
    	    		 
    	    		 if (nodeDsfu.getNodeType() == Node.ELEMENT_NODE && nodeDsfu.getNodeName().equals("fach")){
    	    			 NodeList listaDatosFach= nodeDsfu.getChildNodes();
    	    	    	 for (int m = 0; m < listaDatosFach.getLength(); m++) {
    	    	    		 Node nodeFach = listaDatosFach.item(m);
    	    	    		 if (nodeFach.getNodeType() == Node.ELEMENT_NODE && nodeFach.getNodeName().equals("long")){
    	    	    			 datosFisicosSuelo.setLongFachada(Float.valueOf(nodeFach.getFirstChild().getNodeValue().replace(",",".")));
    	    	    		 }
    	    	    		 else if (nodeFach.getNodeType() == Node.ELEMENT_NODE && nodeFach.getNodeName().equals("tipof")){
    	    	    			 datosFisicosSuelo.setTipoFachada(nodeFach.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
    	    		 }
    	    		 else if (nodeDsfu.getNodeType() == Node.ELEMENT_NODE && nodeDsfu.getNodeName().equals("sup")){
    	    			 datosFisicosSuelo.setSupOcupada(Long.valueOf(nodeDsfu.getFirstChild().getNodeValue()));			 
					 }
    	    		 else if (nodeDsfu.getNodeType() == Node.ELEMENT_NODE && nodeDsfu.getNodeName().equals("fondo")){
    	    			 datosFisicosSuelo.setFondo(Integer.valueOf(nodeDsfu.getFirstChild().getNodeValue()));
					 }
    	    	 }	
    	    	 suelo.setDatosFisicos(datosFisicosSuelo);
			 }
    		 else if (nodeSuelos.getNodeType() == Node.ELEMENT_NODE && nodeSuelos.getNodeName().equals("dvs")){
				 //Datos de valoracion del suelo
				 
				 DatosEconomicosSuelo datosEconomicosSuelo = new DatosEconomicosSuelo();
				 
				 
				 NodeList listaDatosDvs= nodeSuelos.getChildNodes();
    	    	 for (int k = 0; k < listaDatosDvs.getLength(); k++) {
    	    		 Node nodeDvs = listaDatosDvs.item(k);
    	    		 
    	    		 if (nodeDvs.getNodeType() == Node.ELEMENT_NODE && nodeDvs.getNodeName().equals("fpon")){
    	    			 PonenciaTramos ponenciaTramos= new PonenciaTramos();
    	    			 NodeList listaDatosFpon= nodeDvs.getChildNodes();
    	    	    	 for (int m = 0; m < listaDatosFpon.getLength(); m++) {
    	    	    		 Node nodeFpon = listaDatosFpon.item(m);
    	    	    		 if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("cvpv")){
    	    	    			 datosEconomicosSuelo.setCodViaPonencia(nodeFpon.getFirstChild().getNodeValue());
    	    	    		 }
    						 else  if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("ctvpv")){
    							 ponenciaTramos.setCodTramo(nodeFpon.getFirstChild().getNodeValue());			 
    						 }
    						 else  if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("zv")){
    							 ponenciaZonaValor.setCodZonaValor(nodeFpon.getFirstChild().getNodeValue());
    						 }
    	    	    		 
    	    	    	 }
    	    		 }
    	    		 else  if (nodeDvs.getNodeType() == Node.ELEMENT_NODE && nodeDvs.getNodeName().equals("zu")){
    	    			 PonenciaUrbanistica ponenciaUrbanistica = new PonenciaUrbanistica();
    	    			 ponenciaUrbanistica.setCodZona(nodeDvs.getFirstChild().getNodeValue());
    	    			 datosEconomicosSuelo.setZonaUrbanistica(ponenciaUrbanistica);
    	    		 }
					 else  if (nodeDvs.getNodeType() == Node.ELEMENT_NODE && nodeDvs.getNodeName().equals("ctv")){
						 datosEconomicosSuelo.setCodTipoValor(nodeDvs.getFirstChild().getNodeValue());
					 }
					 else  if (nodeDvs.getNodeType() == Node.ELEMENT_NODE && nodeDvs.getNodeName().equals("ccvs")){

						 NodeList listaDatosCcvs= nodeDvs.getChildNodes();
		    	    	 for (int r = 0; r < listaDatosCcvs.getLength(); r++) {
		    	    		 Node nodeCcvs = listaDatosCcvs.item(r);
		    	    		 
		    	    		 if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("nfach")){
		    	    			 datosEconomicosSuelo.setNumFachadas(nodeCcvs.getFirstChild().getNodeValue());
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("ilf")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorLongFachada(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorLongFachada(true);
    	    	    			 } 
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("ifir")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorFormaIrregular(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorFormaIrregular(true);
    	    	    			 }   	    			 
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("ide")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorDesmonte(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorDesmonte(true);
    	    	    			 }   	
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("isdm")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorSupDistinta(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorSupDistinta(true);
    	    	    			 }   
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("iit")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorInedificabilidad(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorInedificabilidad(true);
    	    	    			 }   
		    	    		 }
		    	    		 else if (nodeCcvs.getNodeType() == Node.ELEMENT_NODE && nodeCcvs.getNodeName().equals("ivpp")){
		    	    			 if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorVPO(false);
    	    	    			 }
    	    	    			 else if(nodeCcvs.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorVPO(true);
    	    	    			 }   
		    	    		 }
		    	    	 }
					 }
					 else  if (nodeDvs.getNodeType() == Node.ELEMENT_NODE && nodeDvs.getNodeName().equals("cccsc")){
    	    			 
						 NodeList listaDatosCccsc= nodeDvs.getChildNodes();
		    	    	 for (int r = 0; r < listaDatosCccsc.getLength(); r++) {
		    	    		 Node nodeCccsc = listaDatosCccsc.item(r);
		    	    		 if (nodeCccsc.getNodeType() == Node.ELEMENT_NODE && nodeCccsc.getNodeName().equals("vccad")){
		    	    			 datosEconomicosSuelo.setCorrectorAprecDeprec(Float.valueOf(nodeCccsc.getFirstChild().getNodeValue().replace(",",".")));
		    	    		 }
		    	    		 else if (nodeCccsc.getNodeType() == Node.ELEMENT_NODE && nodeCccsc.getNodeName().equals("iadf")){
								 if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorDeprecFuncional(false);
    	    	    			 }
    	    	    			 else if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorDeprecFuncional(true);
    	    	    			 } 
							 }
		    	    		 else if (nodeCccsc.getNodeType() == Node.ELEMENT_NODE && nodeCccsc.getNodeName().equals("vcccs")){
								 datosEconomicosSuelo.setCorrectorCargasSingulares(Float.valueOf(nodeCccsc.getFirstChild().getNodeValue().replace(",",".")));
							 }
		    	    		 else if (nodeCccsc.getNodeType() == Node.ELEMENT_NODE && nodeCccsc.getNodeName().equals("icce")){
								 if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorSitEspeciales(false);
    	    	    			 }
    	    	    			 else if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorSitEspeciales(true);
    	    	    			 } 
							 }
		    	    		 else if (nodeCccsc.getNodeType() == Node.ELEMENT_NODE && nodeCccsc.getNodeName().equals("iccunl")){
								 if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicosSuelo.setCorrectorNoLucrativo(false);
    	    	    			 }
    	    	    			 else if(nodeCccsc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicosSuelo.setCorrectorNoLucrativo(true);
    	    	    			 } 
							 }
		    	    	 }
						 
    	    		 }
    	    	 }
    	    	 suelo.setDatosEconomicos(datosEconomicosSuelo);
			 }
    		 else if (nodeSuelos.getNodeType() == Node.ELEMENT_NODE && nodeSuelos.getNodeName().equals("evs")){
				 NodeList listaDatosEvs= nodeSuelos.getChildNodes();
    	    	 for (int k = 0; k < listaDatosEvs.getLength(); k++) {
    	    		 Node nodeEvs = listaDatosEvs.item(k);
    	    		 //ASD
    	    		 if (nodeEvs.getNodeType() == Node.ELEMENT_NODE && nodeEvs.getNodeName().equals("vus")){
    	    			 ponenciaZonaValor.setValorUnitario(Float.valueOf(nodeEvs.getFirstChild().getNodeValue().replace(",",".")));
    	    		 }
    	    		 else if (nodeEvs.getNodeType() == Node.ELEMENT_NODE && nodeEvs.getNodeName().equals("icca")){
    	    				 
					 }
    	    		 else if (nodeEvs.getNodeType() == Node.ELEMENT_NODE && nodeEvs.getNodeName().equals("vtccvs")){
    	    			 
					 }
    	    		 else if (nodeEvs.getNodeType() == Node.ELEMENT_NODE && nodeEvs.getNodeName().equals("vtccvsc")){
    	    			 
					 }
    	    		 else if (nodeEvs.getNodeType() == Node.ELEMENT_NODE && nodeEvs.getNodeName().equals("gbs")){
    	    			 
					 }
    	    	 }
				
			 }
			 suelo.getDatosEconomicos().setZonaValor(ponenciaZonaValor);
    	 }
		 return suelo;
	 }
	 
	 
	 private static ArrayList<SueloCatastro> parsearDatosSuelos(Node node){
		 ArrayList<SueloCatastro> lstSuelo = new ArrayList<SueloCatastro>();
		 
		 NodeList listaDatosLsuelocs = node.getChildNodes();
    	 for (int j = 0; j < listaDatosLsuelocs.getLength(); j++) {
    		 
    		 Node nodeLsuelocs = listaDatosLsuelocs.item(j);
    		 if(nodeLsuelocs.getNodeType() == Node.ELEMENT_NODE && nodeLsuelocs.getNodeName().equals("suelocs")){
    			 SueloCatastro suelo = new SueloCatastro();

    			 NodeList listaSuelocs = nodeLsuelocs.getChildNodes();
    	    	 for (int k = 0; k < listaSuelocs.getLength(); k++) {
    			 
		    		 Node nodeSuelocs = listaSuelocs.item(k);
		    		 if (nodeSuelocs.getNodeType() == Node.ELEMENT_NODE && nodeSuelocs.getNodeName().equals("suelos")){
		    			 
		    			 suelo = parsearDatosSuelo(nodeSuelocs, suelo);
		    		 }
		    		 else if (nodeSuelocs.getNodeType() == Node.ELEMENT_NODE && nodeSuelocs.getNodeName().equals("exp")){
		    			 
		    			 Expediente exp = parsearDatosExpediente(nodeSuelocs);
		    			 suelo.setDatosExpediente(exp);
		    		 }
    	    	 }
    	    	 lstSuelo.add(suelo);
    		 }
    	 }
		 return lstSuelo;
	 }
	 
	 private static BienInmuebleCatastro parsearDatosIdentificadorBienInmueble(Node node, BienInmuebleCatastro bien){
		 
		 NodeList listaBi = node.getChildNodes();
    	 for (int r = 0; r < listaBi.getLength(); r++) {
    		 Node nodeBi = listaBi.item(r);
    		 
    		 if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("idcat")){
    			 bien = parsearDatosIdentificacionBienInmueble(nodeBi, bien);
    		 }
    		 else if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("idad")){
    			 NodeList listaIdad = nodeBi.getChildNodes();
    	    	 for (int t = 0; t < listaIdad.getLength(); t++) {
		    		 Node nodeIdad = listaIdad.item(t);
		    		 
		    		 if (nodeIdad.getNodeType() == Node.ELEMENT_NODE && nodeIdad.getNodeName().equals("nfbi")){
		    			 bien.setNumFijoInmueble(Integer.valueOf(nodeIdad.getFirstChild().getNodeValue()));
		    		 }
		    		 else  if (nodeIdad.getNodeType() == Node.ELEMENT_NODE && nodeIdad.getNodeName().equals("iia")){
		    			 bien.setIdAyuntamientoBienInmueble(nodeIdad.getFirstChild().getNodeValue());
		    		 }
		    		 else if (nodeIdad.getNodeType() == Node.ELEMENT_NODE && nodeIdad.getNodeName().equals("nfr")){   		 
		    			 bien.setNumFincaRegistral(parsearDatosFincaRegistral(nodeIdad)); 
		    		 }
    	    	 }						    		 
    		 }
			 else if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("dt")){
				 DireccionLocalizacion direccionLocalizacion = parsearDatosDomicilioTributario(nodeBi);
				 bien.setDomicilioTributario(direccionLocalizacion);
			 }
			 else if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("debi")){
				 DatosEconomicosBien datosEconomicosBien = parsearDatosEconomicosBien(nodeBi);
				 bien.setDatosEconomicosBien(datosEconomicosBien);
			 }
			 else if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("rep")){
				 Persona representante = new Persona();
				 NodeList listaRep = nodeBi.getChildNodes();
    	    	 for (int p = 0; p < listaRep.getLength(); p++) {
		    		 Node nodeRep = listaRep.item(p);
		    		 if (nodeRep.getNodeType() == Node.ELEMENT_NODE && nodeRep.getNodeName().equals("idr")){
		    			 NodeList listaIdr = nodeRep.getChildNodes();
		    	    	 for (int h = 0; h < listaIdr.getLength(); h++) {
				    		 Node nodeIdr = listaIdr.item(h);
				    		 if (nodeIdr.getNodeType() == Node.ELEMENT_NODE && nodeIdr.getNodeName().equals("nif")){
				    			 representante.setNif(nodeIdr.getFirstChild().getNodeValue());
				    		 }
				    		 else  if (nodeIdr.getNodeType() == Node.ELEMENT_NODE && nodeIdr.getNodeName().equals("nom")){
				    			 representante.setRazonSocial(nodeIdr.getFirstChild().getNodeValue());
				    		 }
		    	    	 }
		    		 }
		    		 else if (nodeRep.getNodeType() == Node.ELEMENT_NODE && nodeRep.getNodeName().equals("df")){
		    			 DireccionLocalizacion docimicilioFiscal = new DireccionLocalizacion();
		    			 docimicilioFiscal = parsearDatosDomicilioTributario(nodeRep);
		    			 representante.setDomicilio(docimicilioFiscal);
		    		 }
    	    	 }
    	    	 bien.setRepresentante(representante);
			 }
			 else if (nodeBi.getNodeType() == Node.ELEMENT_NODE && nodeBi.getNodeName().equals("evbi")){
				 NodeList listaEvbi = nodeBi.getChildNodes();
    	    	 for (int p = 0; p < listaEvbi.getLength(); p++) {
		    		 Node nodeEvi = listaEvbi.item(p);
		    		 //asd
		    		 if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("duso")){
		    			 
		    		 }
		    		 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("vcsa")){
		    			 
		    		 }
					 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("vcsrc")){
											    			 
					}
					 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("ired")){
						 
					 }
					 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("uared")){
						 
					 }
					 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("avsa")){
						 
					 }
					 else if (nodeEvi.getNodeType() == Node.ELEMENT_NODE && nodeEvi.getNodeName().equals("ivcm")){
						 
					 }
    	    	 }
			 }
    	 }
    	 return bien;
	 }
	 
	 
	 private static ArrayList<BienInmuebleJuridico> parsearDatosBienInmueble(Node node){
		 
		 ArrayList<BienInmuebleJuridico> lstBienInmuebleJuridico= new ArrayList<BienInmuebleJuridico>();
		 NodeList listaDatosIbics = node.getChildNodes();
    	 for (int j = 0; j < listaDatosIbics.getLength(); j++) {
    		 
    		 Node nodeIbics = listaDatosIbics.item(j);
    		 if(nodeIbics.getNodeType() == Node.ELEMENT_NODE && nodeIbics.getNodeName().equals("bij")){
    			 BienInmuebleJuridico bienInmuebleJuridico = new BienInmuebleJuridico();
    			 BienInmuebleCatastro bien = new BienInmuebleCatastro();

    			 NodeList listaBij = nodeIbics.getChildNodes();
    	    	 for (int k = 0; k < listaBij.getLength(); k++) {
		    		 Node nodeBij = listaBij.item(k);
		    		 if (nodeBij.getNodeType() == Node.ELEMENT_NODE && nodeBij.getNodeName().equals("bi")){ 
		    			 bien = parsearDatosIdentificadorBienInmueble(nodeBij, bien);	
		    		 }
		    		 else if (nodeBij.getNodeType() == Node.ELEMENT_NODE && nodeBij.getNodeName().equals("inft")){
		    			 NodeList listaIntf = nodeBij.getChildNodes();
		    	    	 for (int p = 0; p < listaIntf.getLength(); p++) {
				    		 Node nodeIntf = listaIntf.item(p);
				    		 if (nodeIntf.getNodeType() == Node.ELEMENT_NODE && nodeIntf.getNodeName().equals("dcbl")){
				    			 DatosBaseLiquidableBien datosBaseLiquidable = new DatosBaseLiquidableBien();
				    			 NodeList listaDcbl = nodeIntf.getChildNodes();
				    	    	 for (int h = 0; h < listaDcbl.getLength(); h++) {
						    		 Node nodeDcbl = listaDcbl.item(h);
						    		 if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("vb")){
						    			 datosBaseLiquidable.setValorBase(Double.valueOf(nodeDcbl.getFirstChild().getNodeValue().replace(",",".")));	
						    		 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("cvb")){
										 datosBaseLiquidable.setProcedenciaValorBase(nodeDcbl.getFirstChild().getNodeValue());			    			 
									 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("evcr")){
										 datosBaseLiquidable.setEjercicioIBI(Integer.valueOf(nodeDcbl.getFirstChild().getNodeValue()));
									 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("vcr")){
										 datosBaseLiquidable.setValorCatastralIBI(Double.valueOf(nodeDcbl.getFirstChild().getNodeValue().replace(",",".")));
									 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("ert")){
										 datosBaseLiquidable.setEjercicioRevTotal(Integer.valueOf(nodeDcbl.getFirstChild().getNodeValue()));
									 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("erp")){
										 datosBaseLiquidable.setEjercicioRevParcial(Integer.valueOf(nodeDcbl.getFirstChild().getNodeValue()));
									 }
						    		 else if (nodeDcbl.getNodeType() == Node.ELEMENT_NODE && nodeDcbl.getNodeName().equals("per")){
										 datosBaseLiquidable.setPeriodoTotal(Integer.valueOf(nodeDcbl.getFirstChild().getNodeValue()));
									 }
				    	    	 }
				    	    	 bien.setDatosBaseLiquidable(datosBaseLiquidable);
				    		 }
		    	    	 }
		    		 }
					 else if (nodeBij.getNodeType() == Node.ELEMENT_NODE && nodeBij.getNodeName().equals("exp")){
							    			 
		    			 Expediente exp = parsearDatosExpediente(nodeBij);
		    			 bienInmuebleJuridico.setDatosExpediente(exp);
		    		 }
					 else if (nodeBij.getNodeType() == Node.ELEMENT_NODE && nodeBij.getNodeName().equals("lsf")){
						 bienInmuebleJuridico.setLstTitulares(parsearDatosTitulares(nodeBij));
					 }
					 else if (nodeBij.getNodeType() == Node.ELEMENT_NODE && nodeBij.getNodeName().equals("lcbi")){
						 bienInmuebleJuridico.setLstComBienes(parsearDatosComunidadBienes(nodeBij));
					 }
    	    	 }
    	    	 bienInmuebleJuridico.setBienInmueble(bien);
    	    	 lstBienInmuebleJuridico.add(bienInmuebleJuridico);
    		 }
    	 }
		 return lstBienInmuebleJuridico;
	 }
	 
	 
	 private static NumeroFincaRegistral parsearDatosFincaRegistral(Node node){
		 
		 NumeroFincaRegistral numFincaRegistal = new NumeroFincaRegistral();
		 NodeList listaNfr = node.getChildNodes();
    	 for (int p = 0; p < listaNfr.getLength(); p++) {
    		 Node nodeNfr = listaNfr.item(p);
    		 if (nodeNfr.getNodeType() == Node.ELEMENT_NODE && nodeNfr.getNodeName().equals("rp")){
    			 RegistroPropiedad registroPropiedad = null;
    			 String codProvincia = "";
    			 String codRegPropiedad = "";
    			 NodeList listaRp = nodeNfr.getChildNodes();
    	    	 for (int h = 0; h < listaRp.getLength(); h++) {
		    		 Node nodeRp = listaRp.item(h);
		    		 if (nodeRp.getNodeType() == Node.ELEMENT_NODE && nodeRp.getNodeName().equals("cp")){
		    			 codProvincia = nodeRp.getFirstChild().getNodeValue();
		    		 }
		    		 else if (nodeRp.getNodeType() == Node.ELEMENT_NODE && nodeRp.getNodeName().equals("crp")){
		    			 codRegPropiedad = nodeRp.getFirstChild().getNodeValue();
		    		 }
    	    	 }
    	    	 registroPropiedad = new RegistroPropiedad(codProvincia, codRegPropiedad);
    	    	 numFincaRegistal.setRegistroPropiedad(registroPropiedad);
    		 }
    		 else if (nodeNfr.getNodeType() == Node.ELEMENT_NODE && nodeNfr.getNodeName().equals("sc")){
    			 numFincaRegistal.setSeccion(nodeNfr.getFirstChild().getNodeValue());				    			 
			}
    		 else if (nodeNfr.getNodeType() == Node.ELEMENT_NODE && nodeNfr.getNodeName().equals("fr")){
    			 numFincaRegistal.setNumFinca(nodeNfr.getFirstChild().getNodeValue());
			 }
    		 else if (nodeNfr.getNodeType() == Node.ELEMENT_NODE && nodeNfr.getNodeName().equals("sfr")){
    			 numFincaRegistal.setNumSubFinca(nodeNfr.getFirstChild().getNodeValue());
			 }
    	 }
    	 
    	 return numFincaRegistal;
	 }
	 
	 private static ArrayList<UnidadConstructivaCatastro> parsearDatosUnidadesConstructivas(Node node){
		 
		 ArrayList<UnidadConstructivaCatastro> lstUC = new ArrayList<UnidadConstructivaCatastro>();
		 
		 NodeList listaDatosLuccs = node.getChildNodes();
    	 for (int j = 0; j < listaDatosLuccs.getLength(); j++) {
    		 
    		 Node nodeLuccs = listaDatosLuccs.item(j);
    		 if(nodeLuccs.getNodeType() == Node.ELEMENT_NODE && nodeLuccs.getNodeName().equals("uccs")){
    			 UnidadConstructivaCatastro uc = new UnidadConstructivaCatastro();

    			 NodeList listaUccs = nodeLuccs.getChildNodes();
    	    	 for (int k = 0; k < listaUccs.getLength(); k++) {
    			 
		    		 Node nodeUccs = listaUccs.item(k);
		    		 if (nodeUccs.getNodeType() == Node.ELEMENT_NODE && nodeUccs.getNodeName().equals("ucs")){
		    			 
		    			 uc = parsearDatosUnidadConstructiva(nodeUccs, uc);
		    		 }
		    		 else if (nodeUccs.getNodeType() == Node.ELEMENT_NODE && nodeUccs.getNodeName().equals("exp")){
		    			 
		    			 Expediente exp = parsearDatosExpediente(nodeUccs);
		    			 uc.setDatosExpediente(exp);
		    		 }
    	    	 }
    	    	 lstUC.add(uc);
    		 }
    	 }
		 return lstUC;
	 }
	 
	 private static UnidadConstructivaCatastro parsearDatosUnidadConstructivaOrigen(Node node,  UnidadConstructivaCatastro unidadConstructiva){
		 
		 NodeList listaDatosUc = node.getChildNodes();
		 String pc1="";
		 String pc2="";
    	 for (int j = 0; j < listaDatosUc.getLength(); j++) {
    		 Node nodeUc = listaDatosUc.item(j);

    		 if (nodeUc.getNodeType() == Node.ELEMENT_NODE && nodeUc.getNodeName().equals("pc1")){
    			 
    			 pc1 = nodeUc.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeUc.getNodeType() == Node.ELEMENT_NODE && nodeUc.getNodeName().equals("pc2")){
    			 
    			 pc2 = nodeUc.getFirstChild().getNodeValue();
    		
    		 }
    		 else if (nodeUc.getNodeType() == Node.ELEMENT_NODE && nodeUc.getNodeName().equals("cuc")){
    			 
    			 unidadConstructiva.setCodUnidadConstructiva( nodeUc.getFirstChild().getNodeValue());
    		
    		 }
    		 else if (nodeUc.getNodeType() == Node.ELEMENT_NODE && nodeUc.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeUc.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {

    				 Node nodeLocat = listaDatosLocat.item(w);
    				 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    					 unidadConstructiva.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
    					 
                     }
    				 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    					 unidadConstructiva.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
                     }
    			 }
             }
    		 else if (nodeUc.getNodeType() == Node.ELEMENT_NODE && nodeUc.getNodeName().equals("exp")){
    			 unidadConstructiva.setDatosExpediente(parsearDatosExpediente(nodeUc));
    		 } 
    		 
    	 }
		 ReferenciaCatastral ref = new ReferenciaCatastral(pc1, pc2);
		 unidadConstructiva.setRefParcela(ref);
		 return unidadConstructiva;
	 }
	 
	 private static UnidadConstructivaCatastro parsearDatosUnidadConstructiva(Node node,  UnidadConstructivaCatastro unidadConstructiva){
		 
		 NodeList listaDatosUccs = node.getChildNodes();
    	 for (int j = 0; j < listaDatosUccs.getLength(); j++) {
    		 Node nodeUcs = listaDatosUccs.item(j);
    		 if (nodeUcs.getNodeType() == Node.ELEMENT_NODE && nodeUcs.getNodeName().equals("iducat")){
    			 NodeList listaDatosIducat = nodeUcs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosIducat.getLength(); k++) {
    	    		 Node nodeIducat = listaDatosIducat.item(k);
    	    		 if (nodeIducat.getNodeType() == Node.ELEMENT_NODE && nodeIducat.getNodeName().equals("ruc")){
    	    			 ReferenciaCatastral referenciaCatastral = null; 
    	    			 String pc1 = "";
    	    			 String pc2 = "";
    	    			 NodeList listaDatosRuc = nodeIducat.getChildNodes();
    	    	    	 for (int p = 0; p < listaDatosRuc.getLength(); p++) {
    	    	    		 Node nodeRuc = listaDatosRuc.item(p);
    	    	    		 
    	    	    		 if (nodeRuc.getNodeType() == Node.ELEMENT_NODE && nodeRuc.getNodeName().equals("pc1")){
    	    	    			 pc1 = nodeRuc.getFirstChild().getNodeValue();
    	    	    		 }
    	    	    		 else if (nodeRuc.getNodeType() == Node.ELEMENT_NODE && nodeRuc.getNodeName().equals("pc2")){
							     pc2 = 	nodeRuc.getFirstChild().getNodeValue();  	    			 
							 }
    	    	    		 else if (nodeRuc.getNodeType() == Node.ELEMENT_NODE && nodeRuc.getNodeName().equals("cuc")){
    	    	    			
								 unidadConstructiva.setCodUnidadConstructiva( fillWithCeros(nodeRuc.getFirstChild().getNodeValue(), 4));
							 }
    	    	    	 }
    	    	    	 referenciaCatastral = new ReferenciaCatastral(pc1, pc2);
    	    	    	 unidadConstructiva.setRefParcela(referenciaCatastral);
    	    	    	 unidadConstructiva.setIdUnidadConstructiva(referenciaCatastral.getRefCatastral()+unidadConstructiva.getCodUnidadConstructiva());
    	    		 }
    	    		 else if (nodeIducat.getNodeType() == Node.ELEMENT_NODE && nodeIducat.getNodeName().equals("cn")){
    	    			 unidadConstructiva.setTipoUnidad(nodeIducat.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if (nodeIducat.getNodeType() == Node.ELEMENT_NODE && nodeIducat.getNodeName().equals("locat")){
    	    			 NodeList listaDatosLocat= nodeIducat.getChildNodes();
    	    	    	 for (int t = 0; t < listaDatosLocat.getLength(); t++) {
    	    	    		 Node nodeLocat = listaDatosLocat.item(t);
    	    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    	    			 unidadConstructiva.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    	    	    			 unidadConstructiva.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
    	    	    			 DireccionLocalizacion direccionLocalizacion = new DireccionLocalizacion();
    	    	    			 direccionLocalizacion.setCodigoMunicipioDGC(unidadConstructiva.getCodMunicipioDGC());
    	    	    			 unidadConstructiva.setDirUnidadConstructiva(direccionLocalizacion);
    	    	    		 }
    	    	    	 }
    	    		 }
    	    	 } 
    		 }
    		 else if (nodeUcs.getNodeType() == Node.ELEMENT_NODE && nodeUcs.getNodeName().equals("dt")){
    			 DireccionLocalizacion direccionLocalizacion = parsearDatosDomicilioTributario(nodeUcs);
    			 unidadConstructiva.setDirUnidadConstructiva(direccionLocalizacion);
    		 }
    		 else if (nodeUcs.getNodeType() == Node.ELEMENT_NODE && nodeUcs.getNodeName().equals("dfuc")){
    			 DatosFisicosUC datosFisicos = new DatosFisicosUC();
    			 NodeList listaDatosDfuc = nodeUcs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosDfuc.getLength(); k++) {
    	    		 Node nodeDfuc = listaDatosDfuc.item(k);
    	    		 if (nodeDfuc.getNodeType() == Node.ELEMENT_NODE && nodeDfuc.getNodeName().equals("ac")){
    	    			 datosFisicos.setAnioConstruccion(Integer.valueOf(nodeDfuc.getFirstChild().getNodeValue()));
    	    		 }
    	    		 else if (nodeDfuc.getNodeType() == Node.ELEMENT_NODE && nodeDfuc.getNodeName().equals("iacons")){
    	    			 datosFisicos.setIndExactitud(nodeDfuc.getFirstChild().getNodeValue());
					 }
    	    		 else if (nodeDfuc.getNodeType() == Node.ELEMENT_NODE && nodeDfuc.getNodeName().equals("so")){
    	    			 datosFisicos.setSupOcupada(Long.valueOf(nodeDfuc.getFirstChild().getNodeValue()));
					 }
    	    		 else if (nodeDfuc.getNodeType() == Node.ELEMENT_NODE && nodeDfuc.getNodeName().equals("cucm")){
    	    			 //datosFisicos.set
    	    			 //ASD
					 }
    	    		 else if (nodeDfuc.getNodeType() == Node.ELEMENT_NODE && nodeDfuc.getNodeName().equals("lf")){
    	    			 datosFisicos.setLongFachada(Float.valueOf(nodeDfuc.getFirstChild().getNodeValue().replace(",",".")));
    	    		 }
    	    	 }
    	    	 unidadConstructiva.setDatosFisicos(datosFisicos);
    		 }
    		 else if (nodeUcs.getNodeType() == Node.ELEMENT_NODE && nodeUcs.getNodeName().equals("dvuc")){
    			 DatosEconomicosUC datosEconomicos = new DatosEconomicosUC();
    			 
    			 NodeList listaDatosDvuc = nodeUcs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosDvuc.getLength(); k++) {
    	    		 Node nodeDvuc = listaDatosDvuc.item(k);
    	    		 if (nodeDvuc.getNodeType() == Node.ELEMENT_NODE && nodeDvuc.getNodeName().equals("fpon")){
    	    			 PonenciaTramos ponenciaTramos = new PonenciaTramos();
    	    			 NodeList listaDatosFpon = nodeDvuc.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosFpon.getLength(); r++) {
    	    	    		 Node nodeFpon = listaDatosFpon.item(r);
    	    	    		 if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("cvpv")){
    	    	    			 ponenciaTramos.setCodVia(nodeDvuc.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("ctvpv")){
    	    	    			 ponenciaTramos.setCodTramo(nodeDvuc.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeFpon.getNodeType() == Node.ELEMENT_NODE && nodeFpon.getNodeName().equals("zv")){
    	    	    			 PonenciaZonaValor ponenciaZona = new PonenciaZonaValor();
    	    	    			 ponenciaZona.setCodZonaValor(nodeDvuc.getFirstChild().getNodeValue());
    	    	    			 datosEconomicos.setZonaValor(ponenciaZona);
    	    	    		 }
    	    	    	 }
    	    	    	 datosEconomicos.setTramoPonencia(ponenciaTramos);
    	    		 }
    	    		 else if (nodeDvuc.getNodeType() == Node.ELEMENT_NODE && nodeDvuc.getNodeName().equals("ccvsuc")){
    	    			 NodeList listaDatosCcvsuc = nodeDvuc.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosCcvsuc.getLength(); r++) {
    	    	    		 Node nodeCcvsuc = listaDatosCcvsuc.item(r);
    	    	    		 if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("nf")){
    	    	    			 datosEconomicos.setNumFachadas(nodeCcvsuc.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("ilf")){
    	    	    			 if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicos.setCorrectorLongFachada(false);
    	    	    			 }
    	    	    			 else if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicos.setCorrectorLongFachada(true);
    	    	    			 } 
    	    	    		 }
    	    	    	 }		 
					 }
    	    		 else if (nodeDvuc.getNodeType() == Node.ELEMENT_NODE && nodeDvuc.getNodeName().equals("ccvcuc")){
    	    			 NodeList listaDatosCcvcuc = nodeDvuc.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosCcvcuc.getLength(); r++) {
    	    	    		 Node nodeCcvcuc = listaDatosCcvcuc.item(r);
    	    	    		 if (nodeCcvcuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvcuc.getNodeName().equals("ccec")){
    	    	    			 datosEconomicos.setCorrectorConservacion(nodeCcvcuc.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
					 }
    	    		 else if (nodeDvuc.getNodeType() == Node.ELEMENT_NODE && nodeDvuc.getNodeName().equals("ccvscuc")){
    	    			 NodeList listaDatosCcvscuc = nodeDvuc.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosCcvscuc.getLength(); r++) {
    	    	    		 Node nodeCcvsuc = listaDatosCcvscuc.item(r);
    	    	    		 if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("iccdf")){
    	    	    			 if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicos.setCorrectorDepreciacion(false);
    	    	    			 }
    	    	    			 else if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicos.setCorrectorDepreciacion(true);
    	    	    			 } 
    	    	    		 }
    	    	    		 else if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("vcccs")){
    	    	    				 datosEconomicos.setCoefCargasSingulares(Float.valueOf(nodeCcvsuc.getFirstChild().getNodeValue().replace(",",".")));
    	    	    		 }
    	    	    		 else if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("iccsce")){
    	    	    			 
    	    	    			 if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicos.setCorrectorSitEspeciales(false);
    	    	    			 }
    	    	    			 else if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicos.setCorrectorSitEspeciales(true);
    	    	    			 } 
    	    	    		 }
    	    	    		 else if (nodeCcvsuc.getNodeType() == Node.ELEMENT_NODE && nodeCcvsuc.getNodeName().equals("iccunl")){
    	    	    			 
    	    	    			 if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 datosEconomicos.setCorrectorNoLucrativo(false);
    	    	    			 }
    	    	    			 else if(nodeCcvsuc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 datosEconomicos.setCorrectorNoLucrativo(true);
    	    	    			 } 
    	    	    		 }
    	    	    	 }
					 }
    	    	 }
    	    	 unidadConstructiva.setDatosEconomicos(datosEconomicos);
    		 }
    		 else if (nodeUcs.getNodeType() == Node.ELEMENT_NODE && nodeUcs.getNodeName().equals("evuc")){
    			 NodeList listaDatosEvux = nodeUcs.getChildNodes();
    	    	 for (int r = 0; r < listaDatosEvux.getLength(); r++) {
    	    		 Node nodeEvuc = listaDatosEvux.item(r);
    	    		 //ASD
    	    		 if (nodeEvuc.getNodeType() == Node.ELEMENT_NODE && nodeEvuc.getNodeName().equals("ddes")){
    	    			 
    	    		 }
    	    		 else if (nodeEvuc.getNodeType() == Node.ELEMENT_NODE && nodeEvuc.getNodeName().equals("vuscr")){
    	    			 
    	    		 }
    	    	 }
    		 }
    	 }
		 
		 return unidadConstructiva;
	 }
	 
	 private static FX_CC parsearDatosFXCC(Node node){
		 
		 FX_CC fxcc = new FX_CC();
		 NodeList listaDatosFxcc = node.getChildNodes();
		 for (int h = 0; h< listaDatosFxcc.getLength(); h++) {
			 Node nodeFxcc = listaDatosFxcc.item(h);
			 if(nodeFxcc.getNodeType() == Node.ELEMENT_NODE && nodeFxcc.getNodeName().equals("asc")){
				 fxcc.setASC(nodeFxcc.getFirstChild().getNodeValue());
			 }
			 else if(nodeFxcc.getNodeType() == Node.ELEMENT_NODE && nodeFxcc.getNodeName().equals("dxf")){
				 fxcc.setDXF(nodeFxcc.getFirstChild().getNodeValue());
			 }
			 	
		 }
		 
		 return fxcc;
	 }
	 
	 private static ArrayList<RepartoCatastro> parsearDatosRepartos(Node node){
		 ArrayList<RepartoCatastro> lstReparto = new ArrayList<RepartoCatastro>();

		 NodeList listaDatosLreparcs = node.getChildNodes();
		 for (int h = 0; h< listaDatosLreparcs.getLength(); h++) {
			 Node nodeLreparcs = listaDatosLreparcs.item(h);
			 if(nodeLreparcs.getNodeType() == Node.ELEMENT_NODE && nodeLreparcs.getNodeName().equals("reparcs")){

				 RepartoCatastro reparto = new RepartoCatastro();
				 
				 NodeList listaDatosReparcs = node.getChildNodes();
		    	 for (int j = 0; j < listaDatosReparcs.getLength(); j++) {
		    		 Node nodeReparcs = listaDatosReparcs.item(j);
		    		 if (nodeReparcs.getNodeType() == Node.ELEMENT_NODE && nodeReparcs.getNodeName().equals("repars")){
		    			 reparto = parsearDatosReparto(nodeReparcs, reparto);
		    		 }
		    	 }
		    	 lstReparto.add(reparto);
			 }
		 }
		 
		 return lstReparto;
	 }
	 
	 private static RepartoCatastro parsearDatosRepartoOrigen(Node node,  RepartoCatastro reparto){
		 NodeList listaDatosRepar= node.getChildNodes();
		 String pc1 = "";
		 String pc2 = "";
    	 for (int j = 0; j < listaDatosRepar.getLength(); j++) {
    		
    		 Node nodeRepar = listaDatosRepar.item(j);
		 
    		 if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("pc1")){
    			 pc1 = nodeRepar.getFirstChild().getNodeValue();
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("pc2")){
			     pc2 = 	nodeRepar.getFirstChild().getNodeValue();  	    			 
			 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("noec")){
    			 reparto.setNumOrdenConsRepartir(nodeRepar.getFirstChild().getNodeValue());
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("noecd")){
    			 reparto.setNumOrdenConsRepartir(nodeRepar.getFirstChild().getNodeValue());
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("cspr")){
    			 reparto.setCodSubparcelaElementoRepartir(nodeRepar.getFirstChild().getNodeValue());
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("ccc")){
    			 reparto.setCalifCatastralElementoRepartir(nodeRepar.getFirstChild().getNodeValue());	
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("car")){
    			 
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("locat")){
    			 NodeList listaDatosLocat= nodeRepar.getChildNodes();
    	    	 for (int w = 0; w < listaDatosLocat.getLength(); w++) {
    	    		 Node nodeLocat = listaDatosLocat.item(w);
    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    			 reparto.setCodDelegacion(nodeLocat.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmd")){
    	    			 reparto.setCodMunicipio(nodeLocat.getFirstChild().getNodeValue());
    	    		 }
    	    	 }
    		 }
    		 else if (nodeRepar.getNodeType() == Node.ELEMENT_NODE && nodeRepar.getNodeName().equals("exp")){
				 //reparto.setDatosExpediente(parsearDatosExpediente(nodeRepar));
			 } 
    	 }
	 		reparto.setIdOrigen(new ReferenciaCatastral(pc1, pc2));
    	 return reparto;
	 }
	 
	 private static RepartoCatastro parsearDatosReparto(Node node,  RepartoCatastro reparto){
		 NodeList listaDatosRepars= node.getChildNodes();
    	 for (int j = 0; j < listaDatosRepars.getLength(); j++) {
    		 Node nodeRepars = listaDatosRepars.item(j);
    		 if (nodeRepars.getNodeType() == Node.ELEMENT_NODE && nodeRepars.getNodeName().equals("idrepcat")){
    			 NodeList listaDatosIdrepcat= node.getChildNodes();
    	    	 for (int k = 0; k < listaDatosIdrepcat.getLength(); k++) {
    	    		 Node nodeIdrepcat = listaDatosIdrepcat.item(k);
    	    		 if (nodeIdrepcat.getNodeType() == Node.ELEMENT_NODE && nodeIdrepcat.getNodeName().equals("pc")){
    	    			 String pc1 = "";
    	    			 String pc2 = "";
    	    			 NodeList listaDatosPc = nodeIdrepcat.getChildNodes();
    	    	    	 for (int p = 0; p < listaDatosPc.getLength(); p++) {
    	    	    		 Node nodePc = listaDatosPc.item(p);
    	    	    		 
    	    	    		 if (nodePc.getNodeType() == Node.ELEMENT_NODE && nodePc.getNodeName().equals("pc1")){
    	    	    			 pc1 = nodePc.getFirstChild().getNodeValue();
    	    	    		 }
    						 if (nodePc.getNodeType() == Node.ELEMENT_NODE && nodePc.getNodeName().equals("pc2")){
    						     pc2 = 	nodePc.getFirstChild().getNodeValue();  	    			 
    						 }
    	    	    	 }
    	    	    	 reparto.setIdOrigen(new ReferenciaCatastral(pc1, pc2));
    	    		 }
    	    		 else if (nodeIdrepcat.getNodeType() == Node.ELEMENT_NODE && nodeIdrepcat.getNodeName().equals("ner")){
    	    			 NodeList listaDatosNer = nodeIdrepcat.getChildNodes();
    	    	    	 for (int p = 0; p < listaDatosNer.getLength(); p++) {
    	    	    		 Node nodeNer = listaDatosNer.item(p);
    	    	    		 if (nodeNer.getNodeType() == Node.ELEMENT_NODE && nodeNer.getNodeName().equals("noec")){
    	    	    			 reparto.setNumOrdenConsRepartir(nodeNer.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeNer.getNodeType() == Node.ELEMENT_NODE && nodeNer.getNodeName().equals("lerep")){
    	    	    			 NodeList listaDatosLerep = nodeNer.getChildNodes();
    	    	    	    	 for (int h = 0; h < listaDatosLerep.getLength(); h++) {
    	    	    	    		 Node nodeLerep = listaDatosLerep.item(h);
    	    	    	    		 if (nodeLerep.getNodeType() == Node.ELEMENT_NODE && nodeLerep.getNodeName().equals("irepc")){
    	    	    	    			 NodeList listaDatoIrepc = nodeNer.getChildNodes();
    	    	    	    			 //ASD
    	    	    	    	    	 for (int r = 0; r < listaDatoIrepc.getLength(); r++) {
    	    	    	    	    		 Node nodeIrepc = listaDatoIrepc.item(r);
    	    	    	    	    		 if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("car")){
    	    	    	    	    			 
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("pctrep")){
    	    	    	    	    			 reparto.setPorcentajeReparto(Float.valueOf(nodeIrepc.getFirstChild().getNodeValue().replace(",",".")));
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("exp")){
    	    	    	    	    			 ;
    	    	    	    	    		 }
    	    	    	    	    	 }
    	    	    	    		 }
    	    	    	    		 else if (nodeLerep.getNodeType() == Node.ELEMENT_NODE && nodeLerep.getNodeName().equals("irepl")){
    	    	    	    			 NodeList listaDatoIrepl = nodeNer.getChildNodes();
    	    	    	    	    	 for (int r = 0; r < listaDatoIrepl.getLength(); r++) {
    	    	    	    	    		 
    	    	    	    	    	 //ASD
    	    	    	    	    		 Node nodeIrepl = listaDatoIrepl.item(r);
    	    	    	    	    		 if (nodeIrepl.getNodeType() == Node.ELEMENT_NODE && nodeIrepl.getNodeName().equals("car")){
    	    	    	    	    			 
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepl.getNodeType() == Node.ELEMENT_NODE && nodeIrepl.getNodeName().equals("pctrep")){
    	    	    	    	    			 reparto.setPorcentajeReparto(Float.valueOf(nodeIrepl.getFirstChild().getNodeValue().replace(",",".")));
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepl.getNodeType() == Node.ELEMENT_NODE && nodeIrepl.getNodeName().equals("exp")){
    	    	    	    	    			
    	    	    	    	    		 }
    	    	    	    	    	 }
    	    	    	    		 }
    	    	    	    		 
    	    	    	    	 }
    	    	    		 }
    	    	    		 else if (nodeNer.getNodeType() == Node.ELEMENT_NODE && nodeNer.getNodeName().equals("cspr")){
    	    	    			 reparto.setCodSubparcelaElementoRepartir(nodeNer.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeNer.getNodeType() == Node.ELEMENT_NODE && nodeNer.getNodeName().equals("ccc")){
    	    	    			 reparto.setCalifCatastralElementoRepartir(nodeNer.getFirstChild().getNodeValue());	
    	    	    		 }
    	    	    		 else if (nodeNer.getNodeType() == Node.ELEMENT_NODE && nodeNer.getNodeName().equals("lcrep")){
    	    	    			 NodeList listaDatosLcrep = nodeNer.getChildNodes();
    	    	    	    	 for (int h = 0; h < listaDatosLcrep.getLength(); h++) {
    	    	    	    		 Node nodeLcrep = listaDatosLcrep.item(h);
    	    	    	    		 if (nodeLcrep.getNodeType() == Node.ELEMENT_NODE && nodeLcrep.getNodeName().equals("irepc")){
    	    	    	    			 NodeList listaDatoIrepc = nodeLcrep.getChildNodes();
    	    	    	    	    	 for (int r = 0; r < listaDatoIrepc.getLength(); r++) {
    	    	    	    	    		 Node nodeIrepc = listaDatoIrepc.item(r);
    	    	    	    	    		 //ASD
    	    	    	    	    		 if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("car")){
    	    	    	    	    			 
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("pctrep")){
    	    	    	    	    			 reparto.setPorcentajeReparto(Float.valueOf(nodeIrepc.getFirstChild().getNodeValue().replace(",",".")));
    	    	    	    	    		 }
    	    	    	    	    		 else if (nodeIrepc.getNodeType() == Node.ELEMENT_NODE && nodeIrepc.getNodeName().equals("exp")){
    	    	    	    	    	
    	    	    	    	    		 }
    	    	    	    	    	 }
    	    	    	    		 }
    	    	    	    	 }
    	    	    		 }
    	    	    	 } 
    	    		 }
    	    		 else if (nodeIdrepcat.getNodeType() == Node.ELEMENT_NODE && nodeIdrepcat.getNodeName().equals("locat")){
    	    			 NodeList listaDatosLocat= nodeIdrepcat.getChildNodes();
    	    	    	 for (int w = 0; w < listaDatosLocat.getLength(); w++) {
    	    	    		 Node nodeLocat = listaDatosLocat.item(w);
    	    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    	    			 reparto.setCodDelegacion(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmd")){
    	    	    			 reparto.setCodMunicipio(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
    	    		 }
    	    	 }
    	    		 
    		 }
    	 }
    	 return reparto;
	 }

	 private static ArrayList<ComunidadBienes> parsearDatosComunidadBienes(Node node){
	 
		 ArrayList<ComunidadBienes> lstComunidadBienes = new ArrayList<ComunidadBienes>();
		 NodeList listaDatosLcbi = node.getChildNodes();
		 for (int h = 0; h< listaDatosLcbi.getLength(); h++) {
			 Node nodeLcbi = listaDatosLcbi.item(h);
			 if(nodeLcbi.getNodeType() == Node.ELEMENT_NODE && nodeLcbi.getNodeName().equals("cbi")){
				 ComunidadBienes comunidadBien = new ComunidadBienes();
				 NodeList listaDatosCbi = nodeLcbi.getChildNodes();
		    	 for (int j = 0; j < listaDatosCbi.getLength(); j++) {
		    		 Node nodeCbi = listaDatosCbi.item(j);
		    		 if (nodeCbi.getNodeType() == Node.ELEMENT_NODE && nodeCbi.getNodeName().equals("idcbf")){
		    			 NodeList listaDatosIdcbf = nodeCbi.getChildNodes();
				    	 for (int p = 0; p < listaDatosIdcbf.getLength(); p++) {
				    		 Node nodeIdcbf = listaDatosIdcbf.item(p);
				    		 if(nodeIdcbf.getNodeType() == Node.ELEMENT_NODE && nodeIdcbf.getNodeName().equals("nifcb")){
				    			 comunidadBien.setNif(nodeIdcbf.getFirstChild().getNodeValue());
				    		 }
				    		 else if(nodeIdcbf.getNodeType() == Node.ELEMENT_NODE && nodeIdcbf.getNodeName().equals("nomcb")){
				    			 comunidadBien.setRazonSocial(nodeIdcbf.getFirstChild().getNodeValue());
				    		 }
				    	 }
		    		 }
		    		 else if (nodeCbi.getNodeType() == Node.ELEMENT_NODE && nodeCbi.getNodeName().equals("df")){
		    			 
		    			 DireccionLocalizacion domicilioTributario = parsearDatosDomicilioTributario(nodeCbi);
		    			 comunidadBien.setDomicilio(domicilioTributario);
		    		 }
		    	 }
		    	 lstComunidadBienes.add(comunidadBien);
			 }
		 }
		 return lstComunidadBienes;
	 }
	 
	 private static ArrayList<Titular> parsearDatosTitulares(Node node){
	 
		 ArrayList<Titular> lstTitular = new ArrayList<Titular>();
		 NodeList listaDatosLsf = node.getChildNodes();
		 for (int h = 0; h< listaDatosLsf.getLength(); h++) {
			 Node nodeLsf = listaDatosLsf.item(h);
			 if(nodeLsf.getNodeType() == Node.ELEMENT_NODE && nodeLsf.getNodeName().equals("sft")){
				 Titular titular = new Titular();
				 NodeList listaDatosSft = nodeLsf.getChildNodes();
		    	 for (int j = 0; j < listaDatosSft.getLength(); j++) {
		    		 Node nodeSft = listaDatosSft.item(j);
		    		 if (nodeSft.getNodeType() == Node.ELEMENT_NODE && nodeSft.getNodeName().equals("tit")){
		    			 titular = parsearDatosTitular(nodeSft, titular);
		    		 }
		    		 else if (nodeSft.getNodeType() == Node.ELEMENT_NODE && nodeSft.getNodeName().equals("exp")){
		    			 Expediente exp = parsearDatosExpediente(nodeSft);
		    			 titular.setExpediente(exp);
		    		 }
		    	 }
		    	 lstTitular.add(titular);
			 }
		 }
		 
		 return lstTitular;
	 }
	 
	 private static Titular parsearDatosTitular(Node node, Titular titular){
		 Derecho derecho = new Derecho();
		 NodeList listaDatosTit = node.getChildNodes();
		 for (int h = 0; h< listaDatosTit.getLength(); h++) {
			 Node nodeTit = listaDatosTit.item(h);
			 if (nodeTit.getNodeType() == Node.ELEMENT_NODE && nodeTit.getNodeName().equals("der")){
				
				 NodeList listaDatosDer = nodeTit.getChildNodes();
				 for (int t = 0; t< listaDatosDer.getLength(); t++) {
					 Node nodeDer = listaDatosDer.item(t);
					 if (nodeDer.getNodeType() == Node.ELEMENT_NODE && nodeDer.getNodeName().equals("cdr")){
						 derecho.setCodDerecho(nodeDer.getFirstChild().getNodeValue());
					 }
					 else if (nodeDer.getNodeType() == Node.ELEMENT_NODE && nodeDer.getNodeName().equals("pct")){
						 derecho.setPorcentajeDerecho(Float.valueOf( nodeDer.getFirstChild().getNodeValue().replace(",", ".")));
					 }
				 }
			 }
			 else if (nodeTit.getNodeType() == Node.ELEMENT_NODE && nodeTit.getNodeName().equals("ord")){
				 derecho.setOrdinalDerecho(Integer.valueOf(nodeTit.getFirstChild().getNodeValue()));
			 }
			 else if (nodeTit.getNodeType() == Node.ELEMENT_NODE && nodeTit.getNodeName().equals("idpa")){
				 NodeList listaDatosIdpa = nodeTit.getChildNodes();
				 for (int r = 0; r< listaDatosIdpa.getLength(); r++) {
					 Node nodeIdpa = listaDatosIdpa.item(r);
					 if (nodeIdpa.getNodeType() == Node.ELEMENT_NODE && nodeIdpa.getNodeName().equals("nif")){
						 titular.setNif(nodeIdpa.getFirstChild().getNodeValue());
					 }
					 else if (nodeIdpa.getNodeType() == Node.ELEMENT_NODE && nodeIdpa.getNodeName().equals("anif")){
						 titular.setAusenciaNIF(nodeIdpa.getFirstChild().getNodeValue());
					 }
					 else if (nodeIdpa.getNodeType() == Node.ELEMENT_NODE && nodeIdpa.getNodeName().equals("nom")){
						 titular.setRazonSocial(nodeIdpa.getFirstChild().getNodeValue());
					 }
				 }	
			 }
			 else if (nodeTit.getNodeType() == Node.ELEMENT_NODE && nodeTit.getNodeName().equals("df")){
				 DireccionLocalizacion direccionLocalizacion = parsearDatosDomicilioTributario(nodeTit);
				 titular.setDomicilio(direccionLocalizacion);
			 }
			 else if (nodeTit.getNodeType() == Node.ELEMENT_NODE && nodeTit.getNodeName().equals("iatit")){
				 NodeList listaDatosIatit = nodeTit.getChildNodes();
				 for (int r = 0; r< listaDatosIatit.getLength(); r++) {
					 Node nodeIatit = listaDatosIatit.item(r);
					 if (nodeIatit.getNodeType() == Node.ELEMENT_NODE && nodeIatit.getNodeName().equals("nifcy")){
						 titular.setNifConyuge(nodeIatit.getFirstChild().getNodeValue());
					 }
					 else if (nodeIatit.getNodeType() == Node.ELEMENT_NODE && nodeIatit.getNodeName().equals("nifcb")){
						 titular.setNifCb(nodeIatit.getFirstChild().getNodeValue());
					 }
					 else if (nodeIatit.getNodeType() == Node.ELEMENT_NODE && nodeIatit.getNodeName().equals("ct")){
						 titular.setComplementoTitularidad(nodeIatit.getFirstChild().getNodeValue());
					 }
				 }	
			 }
		 }
		 titular.setDerecho(derecho);
		 return titular;
	 }
	 
	 private static ArrayList<Cultivo> parsearDatosCultivos(Node node){
		 ArrayList<Cultivo> lstCultivo = new ArrayList<Cultivo>();

		 NodeList listaDatosLsprcs = node.getChildNodes();
		 for (int h = 0; h< listaDatosLsprcs.getLength(); h++) {
			 Node nodeLsprcs = listaDatosLsprcs.item(h);
			 if(nodeLsprcs.getNodeType() == Node.ELEMENT_NODE && nodeLsprcs.getNodeName().equals("sprcs")){
				 Cultivo cultivo = new Cultivo();
				 NodeList listaDatosSprcs = nodeLsprcs.getChildNodes();
		    	 for (int j = 0; j < listaDatosSprcs.getLength(); j++) {
		    		 Node nodeSprcs = listaDatosSprcs.item(j);
		    		 if (nodeSprcs.getNodeType() == Node.ELEMENT_NODE && nodeSprcs.getNodeName().equals("sprs")){
		    			 cultivo = parsearDatosCultivo(nodeSprcs, cultivo);
		    		 }
		    		 else if (nodeSprcs.getNodeType() == Node.ELEMENT_NODE && nodeSprcs.getNodeName().equals("exp")){
		    			 Expediente exp = parsearDatosExpediente(nodeSprcs);
		    			 cultivo.setDatosExpediente(exp);
		    		 }
		    	 }
		    	 lstCultivo.add(cultivo);
			 }
		 }
		 
		 return lstCultivo;
	 }
	 
	 private static Cultivo parsearDatosCultivoOrigen(Node node,  Cultivo cultivo){
		 NodeList listaDatosSpr= node.getChildNodes();
		 String pc1 ="";
		 String pc2 = "";
    	 for (int j = 0; j < listaDatosSpr.getLength(); j++) {
    		 Node nodeSpr = listaDatosSpr.item(j);
    			
			 if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("pc1")){
				 
				 pc1 = nodeSpr.getFirstChild().getNodeValue();
			
			 }
			 else if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("pc2")){
				 
				 pc2 = nodeSpr.getFirstChild().getNodeValue();
			
			 }
			 else if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("cspr")){
				 
				 cultivo.setCodSubparcela(nodeSpr.getFirstChild().getNodeValue());
			
			 }
			 else if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("ccc")){
				 
				 if(cultivo.getIdCultivo() == null){
					 cultivo.setIdCultivo(new IdCultivo());
				 }
				 cultivo.getIdCultivo().setCalifCultivo(nodeSpr.getFirstChild().getNodeValue());
			
			 }
			 else if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeSpr.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {
	
					 Node nodeLocat = listaDatosLocat.item(w);
					 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
						 cultivo.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
						 
	                 }
					 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
						 cultivo.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
	                 }
				 }
	         }
			 else if (nodeSpr.getNodeType() == Node.ELEMENT_NODE && nodeSpr.getNodeName().equals("exp")){
				 cultivo.setDatosExpediente(parsearDatosExpediente(nodeSpr));
			 } 
    	 }
 
    	 cultivo.getIdCultivo().setParcelaCatastral(pc1+pc2);
    	 return cultivo;
	 }
	 
	 
	 private static Cultivo parsearDatosCultivo(Node node,  Cultivo cultivo){
		 NodeList listaDatosSprs= node.getChildNodes();
    	 for (int j = 0; j < listaDatosSprs.getLength(); j++) {
    		 Node nodeSprs = listaDatosSprs.item(j);
    		 if (nodeSprs.getNodeType() == Node.ELEMENT_NODE && nodeSprs.getNodeName().equals("idspr")){

    			 String pc1 = "";
    			 String pc2 = "";
    			 NodeList listaDatosIdspr = nodeSprs.getChildNodes();
    	    	 for (int p = 0; p < listaDatosIdspr.getLength(); p++) {
    	    		 Node nodeIdspr = listaDatosIdspr.item(p);
    	    		 
    	    		 if (nodeIdspr.getNodeType() == Node.ELEMENT_NODE && nodeIdspr.getNodeName().equals("pc1")){
    	    			 pc1 = nodeIdspr.getFirstChild().getNodeValue();
    	    		 }
    	    		 else if (nodeIdspr.getNodeType() == Node.ELEMENT_NODE && nodeIdspr.getNodeName().equals("pc2")){
					     pc2 = 	nodeIdspr.getFirstChild().getNodeValue();  	    			 
					 }
    	    		 else if (nodeIdspr.getNodeType() == Node.ELEMENT_NODE && nodeIdspr.getNodeName().equals("cspr")){
						 cultivo.setCodSubparcela(nodeIdspr.getFirstChild().getNodeValue());
					 }
    	    		 else if (nodeIdspr.getNodeType() == Node.ELEMENT_NODE && nodeIdspr.getNodeName().equals("ccc")){
						 if(cultivo.getIdCultivo() == null){
							 cultivo.setIdCultivo(new IdCultivo());
						 }
						 cultivo.getIdCultivo().setCalifCultivo(nodeIdspr.getFirstChild().getNodeValue());
					 }
    	    		 else if (nodeIdspr.getNodeType() == Node.ELEMENT_NODE && nodeIdspr.getNodeName().equals("locat")){
						 NodeList listaDatosLocat= nodeIdspr.getChildNodes();
    	    	    	 for (int k = 0; k < listaDatosLocat.getLength(); k++) {
    	    	    		 Node nodeLocat = listaDatosLocat.item(k);
    	    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    	    			 cultivo.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmd")){
    	    	    			 cultivo.setCodMunicipioDGC(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
					 }
    	    	 }
    	    	 cultivo.getIdCultivo().setParcelaCatastral(pc1+pc2);
    		 }
    	     if (nodeSprs.getNodeType() == Node.ELEMENT_NODE && nodeSprs.getNodeName().equals("dspr")){
    	    	 if(cultivo.getIdCultivo() == null){
					 cultivo.setIdCultivo(new IdCultivo());
				 }
    	    	 NodeList listaDatosDspr= nodeSprs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosDspr.getLength(); k++) {
    	    		 Node nodeDspr = listaDatosDspr.item(k);
    	    		 if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("car")){
    	    			 cultivo.getIdCultivo().setNumOrden(nodeDspr.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("modl")){
    	    			 cultivo.setCodModalidadReparto(nodeDspr.getFirstChild().getNodeValue());
    	    		 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("tspr")){
						 cultivo.setTipoSubparcela(nodeDspr.getFirstChild().getNodeValue());		 
					 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("dcc")){
						 cultivo.setDenominacionCultivo(nodeDspr.getFirstChild().getNodeValue());
					 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("ip")){
						 cultivo.setIntensidadProductiva(Integer.valueOf(nodeDspr.getFirstChild().getNodeValue()));
					 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("ssp")){
						 cultivo.setSuperficieParcela(Long.valueOf(nodeDspr.getFirstChild().getNodeValue()));
					 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("bspr")){
						 NodeList listaDatosBspr= nodeDspr.getChildNodes();
		    	    	 for (int r = 0; r < listaDatosBspr.getLength(); r++) {
		    	    		 Node nodeBspr = listaDatosBspr.item(r);
		    	    		 if (nodeBspr.getNodeType() == Node.ELEMENT_NODE && nodeBspr.getNodeName().equals("cbspr")){
		    	    			 cultivo.setCodBonificacion(nodeBspr.getFirstChild().getNodeValue());
		    	    		 }
		    	    		 else if (nodeBspr.getNodeType() == Node.ELEMENT_NODE && nodeBspr.getNodeName().equals("efb")){
		    	    			 cultivo.setEjercicioFinBonificacion(Integer.valueOf(nodeBspr.getFirstChild().getNodeValue()));
		    	    		 }
		    	    	 }
					 }
					 else if (nodeDspr.getNodeType() == Node.ELEMENT_NODE && nodeDspr.getNodeName().equals("vsp")){
						 cultivo.setValorCatastral(Double.valueOf(nodeDspr.getFirstChild().getNodeValue().replace(",",".")));
					 }
    	    	 }
    	     }
    	     if (nodeSprs.getNodeType() == Node.ELEMENT_NODE && nodeSprs.getNodeName().equals("evspr")){
    	    	 NodeList listaDatosEvspr= nodeSprs.getChildNodes();
    	    	 for (int k = 0; k < listaDatosEvspr.getLength(); k++) {
    	    		 Node nodeEvspr = listaDatosEvspr.item(k);
    	    		 if (nodeEvspr.getNodeType() == Node.ELEMENT_NODE && nodeEvspr.getNodeName().equals("te")){
    	    			 //ASD
    	    		 }
    	    	 }
    	     }
    	 }
    	 
    	 return cultivo;
	 }
	 
	 private static ArrayList<ConstruccionCatastro> parsearDatosConstrucciones(Node node){
		 ArrayList<ConstruccionCatastro> lstConstruccion = new ArrayList<ConstruccionCatastro>();

		 NodeList listaDatosLconscs = node.getChildNodes();
		 for (int h = 0; h< listaDatosLconscs.getLength(); h++) {
			 Node nodeLconscs = listaDatosLconscs.item(h);
			 if(nodeLconscs.getNodeType() == Node.ELEMENT_NODE && nodeLconscs.getNodeName().equals("conscs")){
				 ConstruccionCatastro construccion = new ConstruccionCatastro();
				 NodeList listaDatosConscs = nodeLconscs.getChildNodes();
		    	 for (int j = 0; j < listaDatosConscs.getLength(); j++) {
		    		 Node nodeConscs = listaDatosConscs.item(j);
		    		 if (nodeConscs.getNodeType() == Node.ELEMENT_NODE && nodeConscs.getNodeName().equals("conss")){
		    			 construccion = parsearDatosConstruccion(nodeConscs, construccion);
		    		 }
		    		 else if (nodeConscs.getNodeType() == Node.ELEMENT_NODE && nodeConscs.getNodeName().equals("exp")){
		    			 Expediente exp = parsearDatosExpediente(nodeConscs);
		    			 construccion.setDatosExpediente(exp);
		    		 }
		    	 }
		    	 lstConstruccion.add(construccion);
			 }
		 }
		 
		 return lstConstruccion;
	 }
			
	 
	 private static ConstruccionCatastro parsearDatosConstruccionOrigen(Node node,  ConstruccionCatastro construccion){
		 NodeList listaDatosCons= node.getChildNodes();
		 String pc1 = "";
		 String pc2 ="";
    	 for (int j = 0; j < listaDatosCons.getLength(); j++) {

	    	 Node nodeCons = listaDatosCons.item(j);
	
			 if (nodeCons.getNodeType() == Node.ELEMENT_NODE && nodeCons.getNodeName().equals("pc1")){
				 
				 pc1 = nodeCons.getFirstChild().getNodeValue();
			
			 }
			 else if (nodeCons.getNodeType() == Node.ELEMENT_NODE && nodeCons.getNodeName().equals("pc2")){
				 
				 pc2 = nodeCons.getFirstChild().getNodeValue();
			
			 }
			 else if (nodeCons.getNodeType() == Node.ELEMENT_NODE && nodeCons.getNodeName().equals("noec")){
				 
				 construccion.setNumOrdenConstruccion( nodeCons.getFirstChild().getNodeValue());
			
			 }
			 else if (nodeCons.getNodeType() == Node.ELEMENT_NODE && nodeCons.getNodeName().equals("locat")){
				 NodeList listaDatosLocat= nodeCons.getChildNodes();
				 for (int w = 0; w < listaDatosLocat.getLength(); w++) {
	
					 Node nodeLocat = listaDatosLocat.item(w);
					 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
						 construccion.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
						 
	                 }
					 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
						 construccion.setCodMunicipio(nodeLocat.getFirstChild().getNodeValue());
	                 }
				 }
	         }
			 else if (nodeCons.getNodeType() == Node.ELEMENT_NODE && nodeCons.getNodeName().equals("exp")){
				 construccion.setDatosExpediente(parsearDatosExpediente(nodeCons));
			 } 
	 	}
    	 ReferenciaCatastral ref = new ReferenciaCatastral(pc1, pc2);
    	 construccion.setRefParcela(ref);
		 return construccion;
	 }
	 
	 private static ConstruccionCatastro parsearDatosConstruccion(Node node,  ConstruccionCatastro construccion){
		 NodeList listaDatosConss= node.getChildNodes();
    	 for (int j = 0; j < listaDatosConss.getLength(); j++) {
    		 Node nodeConss = listaDatosConss.item(j);
    		 if (nodeConss.getNodeType() == Node.ELEMENT_NODE && nodeConss.getNodeName().equals("idconscat")){
    			 NodeList listaDatosIdconscat = nodeConss.getChildNodes();
    	    	 for (int k = 0; k < listaDatosIdconscat.getLength(); k++) {
    	    		 Node nodeIdconscat = listaDatosIdconscat.item(k);
    	    		 if(nodeIdconscat.getNodeType() == Node.ELEMENT_NODE && nodeIdconscat.getNodeName().equals("rcons")){
    	    			
    	    			 ReferenciaCatastral referenciaCatastral = null; 
    	    			 String pc1 = "";
    	    			 String pc2 = "";
    	    			 NodeList listaDatosRcons = nodeIdconscat.getChildNodes();
    	    	    	 for (int p = 0; p < listaDatosRcons.getLength(); p++) {
    	    	    		 Node nodeRcons = listaDatosRcons.item(p);
    	    	    		 
    	    	    		 if (nodeRcons.getNodeType() == Node.ELEMENT_NODE && nodeRcons.getNodeName().equals("pc1")){
    	    	    			 pc1 = nodeRcons.getFirstChild().getNodeValue();
    	    	    		 }
    	    	    		 else if (nodeRcons.getNodeType() == Node.ELEMENT_NODE && nodeRcons.getNodeName().equals("pc2")){
							     pc2 = 	nodeRcons.getFirstChild().getNodeValue();  	    			 
							 }
    	    	    		 else if (nodeRcons.getNodeType() == Node.ELEMENT_NODE && nodeRcons.getNodeName().equals("noec")){
								 construccion.setNumOrdenConstruccion(nodeRcons.getFirstChild().getNodeValue());
							 }
    	    	    	 }
    	    	    	 referenciaCatastral = new ReferenciaCatastral(pc1, pc2); 
    	    	    	 construccion.setRefParcela(referenciaCatastral);
    	    	    	 construccion.setIdConstruccion(referenciaCatastral.getRefCatastral()+construccion.getNumOrdenConstruccion());
    	    		 }
    	    		 else if(nodeIdconscat.getNodeType() == Node.ELEMENT_NODE && nodeIdconscat.getNodeName().equals("locat")){
    	    		 
    	    			 NodeList listaDatosLocat= nodeIdconscat.getChildNodes();
    	    	    	 for (int t = 0; t < listaDatosLocat.getLength(); t++) {
    	    	    		 Node nodeLocat = listaDatosLocat.item(t);
    	    	    		 if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cd")){
    	    	    			 construccion.setCodDelegacionMEH(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if (nodeLocat.getNodeType() == Node.ELEMENT_NODE && nodeLocat.getNodeName().equals("cmc")){
    	    	    			 construccion.setCodMunicipio(nodeLocat.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    	 }
    	    		 }
    	    	 }
    		 }
    		 else if (nodeConss.getNodeType() == Node.ELEMENT_NODE && nodeConss.getNodeName().equals("dt")){
    			 DireccionLocalizacion direccionLocalizacion = null;
    			 NodeList listaDatosDt= nodeConss.getChildNodes();
    	    	 for (int t = 0; t < listaDatosDt.getLength(); t++) {
    	    		 Node nodeDt = listaDatosDt.item(t);
    	    		 
    	    		 if(nodeDt.getNodeType() == Node.ELEMENT_NODE && nodeDt.getNodeName().equals("lourb")){
    	    			 direccionLocalizacion = new DireccionLocalizacion();
    	    			 direccionLocalizacion = parsearDatosLocalizacionUrbana(nodeDt, direccionLocalizacion);
    	    		 }

    	    	 }
    	    	 construccion.setDomicilioTributario(direccionLocalizacion);
    			
    		 }
    		 else if (nodeConss.getNodeType() == Node.ELEMENT_NODE && nodeConss.getNodeName().equals("dfcons")){
    			 //Datos Fisicos de la construccion
    			 if(construccion.getDatosEconomicos() == null){
    				 construccion.setDatosEconomicos(new DatosEconomicosConstruccion());
    			 }
    			 
    			 if(construccion.getDatosFisicos() == null){
    				 construccion.setDatosFisicos(new DatosFisicosConstruccion());
    			 }
    			 
    			 NodeList listaDatosDfcons= nodeConss.getChildNodes();
    	    	 for (int t = 0; t < listaDatosDfcons.getLength(); t++) {
    	    		 Node nodeDfcons = listaDatosDfcons.item(t);
    	    		 
    	    		 if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("cuc")){
    	    			 construccion.getDatosFisicos().setCodUnidadConstructiva(nodeDfcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("car")){
    	    			 construccion.setNumOrdenBienInmueble(nodeDfcons.getFirstChild().getNodeValue());
    	    		 }	
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("modl")){
    	    			 construccion.getDatosEconomicos().setCodModalidadReparto(nodeDfcons.getFirstChild().getNodeValue());    			 
					 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("cdes")){
    	    			 construccion.getDatosFisicos().setCodDestino(nodeDfcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("refor")){
    	    			 NodeList listaDatosRefor= nodeDfcons.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosRefor.getLength(); r++) {
    	    	    		 Node nodeRefor = listaDatosRefor.item(r);
    	    	    		 if(nodeRefor.getNodeType() == Node.ELEMENT_NODE && nodeRefor.getNodeName().equals("tr")){
    	    	    			 construccion.getDatosFisicos().setTipoReforma(nodeRefor.getFirstChild().getNodeValue());
    	    	    		 }
    	    	    		 else if(nodeRefor.getNodeType() == Node.ELEMENT_NODE && nodeRefor.getNodeName().equals("ar")){
    	    	    			 construccion.getDatosFisicos().setAnioReforma(Integer.valueOf(nodeRefor.getFirstChild().getNodeValue()));
    	    	    		 }
    	    	    	 }
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("aec")){
    	    			 construccion.getDatosFisicos().setAnioAntiguedad(Integer.valueOf(nodeDfcons.getFirstChild().getNodeValue()));
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("ili")){
    	    			 if(nodeDfcons.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    				 construccion.getDatosFisicos().setLocalInterior(false);
    	    			 }
    	    			 else if(nodeDfcons.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    				 construccion.getDatosFisicos().setLocalInterior(true);
    	    			 } 
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("stl")){
    	    			 construccion.getDatosFisicos().setSupTotal(Long.valueOf(nodeDfcons.getFirstChild().getNodeValue()));
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("spt")){
    	    			 construccion.getDatosFisicos().setSupTerrazasLocal(Long.valueOf(nodeDfcons.getFirstChild().getNodeValue()));
    	    		 }
    	    		 else if(nodeDfcons.getNodeType() == Node.ELEMENT_NODE && nodeDfcons.getNodeName().equals("sil")){
    	    			 construccion.getDatosFisicos().setSupImputableLocal(Long.valueOf(nodeDfcons.getFirstChild().getNodeValue()));
    	    		 }
    	    	 }
    	    	
    		 }
    		 else if (nodeConss.getNodeType() == Node.ELEMENT_NODE && nodeConss.getNodeName().equals("dvcons")){
    			 if(construccion.getDatosEconomicos() == null){
    				 construccion.setDatosEconomicos(new DatosEconomicosConstruccion());
    			 }
    			 //Datos de valoración de la construcción 
    			 NodeList listaDatosDvcons= nodeConss.getChildNodes();
    	    	 for (int t = 0; t < listaDatosDvcons.getLength(); t++) {
    	    		 Node nodeDvcons = listaDatosDvcons.item(t);
    	    		 
    	    		 if(nodeDvcons.getNodeType() == Node.ELEMENT_NODE && nodeDvcons.getNodeName().equals("tip")){
    	    			 construccion.getDatosEconomicos().setTipoConstruccion(nodeDvcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDvcons.getNodeType() == Node.ELEMENT_NODE && nodeDvcons.getNodeName().equals("usop")){
    	    			 construccion.getDatosEconomicos().setCodUsoPredominante(nodeDvcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDvcons.getNodeType() == Node.ELEMENT_NODE && nodeDvcons.getNodeName().equals("cat")){
    	    			 construccion.getDatosEconomicos().setCodCategoriaPredominante(nodeDvcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDvcons.getNodeType() == Node.ELEMENT_NODE && nodeDvcons.getNodeName().equals("ctv")){
    	    			 construccion.getDatosEconomicos().setCodTipoValor(nodeDvcons.getFirstChild().getNodeValue());
    	    		 }
    	    		 else if(nodeDvcons.getNodeType() == Node.ELEMENT_NODE && nodeDvcons.getNodeName().equals("ccvscc")){
    	    			 NodeList listaDatosCcvscc= nodeDvcons.getChildNodes();
    	    	    	 for (int r = 0; r < listaDatosCcvscc.getLength(); r++) {
    	    	    		 Node nodeCcvscc = listaDatosCcvscc.item(r);
    	    	    		 if(nodeCcvscc.getNodeType() == Node.ELEMENT_NODE && nodeCcvscc.getNodeName().equals("vccad")){
    	    	    			 construccion.getDatosEconomicos().setCorrectorApreciación(Float.valueOf(nodeCcvscc.getFirstChild().getNodeValue().replace(",",".")));
    	    	    		 }
    	    	    		 else if(nodeCcvscc.getNodeType() == Node.ELEMENT_NODE && nodeCcvscc.getNodeName().equals("iacli")){
    	    	    			 if(nodeCcvscc.getFirstChild().getNodeValue().equals(VALOR_NO)){
    	    	    				 construccion.getDatosEconomicos().setCorrectorVivienda(false);
    	    	    			 }
    	    	    			 else if(nodeCcvscc.getFirstChild().getNodeValue().equals(VALOR_SI)){
    	    	    				 construccion.getDatosEconomicos().setCorrectorVivienda(true);
    	    	    			 } 
    	    	    		 }
    	    	    	 }
    	    		 }
    	    	 }
    		 }
    		 else if (nodeConss.getNodeType() == Node.ELEMENT_NODE && nodeConss.getNodeName().equals("evcons")){
    			 NodeList listaDatosEvcons= nodeConss.getChildNodes();
    	    	 for (int r = 0; r < listaDatosEvcons.getLength(); r++) {
    	    		 Node nodeEvcons = listaDatosEvcons.item(r);
    	    		 
    	    		 // ASD
    	    		 if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vrs")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vmctcc")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vmctcb")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("icca")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vtccvs")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vtccvc")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("vtccvsc")){
    	    			 
    	    		 }
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("ddes")){
    	    			 
    	    		 } 
    	    		 else if(nodeEvcons.getNodeType() == Node.ELEMENT_NODE && nodeEvcons.getNodeName().equals("gbc")){
    	    			 
    	    		 }
    	    	 }
    		 }
    	 }

		 return construccion;
	 }
	 
	 
	 /**
	     * @param xml String con el XML
	     * @return Document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	     * @throws SAXException error en xml
	     */
    private static Document stringToDocument(String xml) throws ParserConfigurationException, SAXException, IOException {

        Document doc = null;
   
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr);
        is.setEncoding("ISO-8859-1");
        doc = builder.parse(is);
      
        return doc;
    }
    
    private static String fillWithCeros(String campo,int longitud){
    	StringBuffer resultado=new StringBuffer(campo);
    	int longitudCampo=campo.length();
    	
    	int resto=longitud-longitudCampo;    	
    	for (int i=0;i<resto;i++){
    		resultado.insert(0,"0");
    	}
    	resultado.setLength(resultado.length());
    	return resultado.toString();	
    }
    
    
}
