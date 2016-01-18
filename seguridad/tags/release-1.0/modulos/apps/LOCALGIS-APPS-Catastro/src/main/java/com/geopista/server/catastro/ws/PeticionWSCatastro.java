package com.geopista.server.catastro.ws;

import java.util.ArrayList;

import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.servicioWebCatastro.beans.CabeceraWSCatastro;
import com.vividsolutions.jump.util.java2xml.Java2XMLCatastro;

public class PeticionWSCatastro
{
	 public static String creaControl( CabeceraWSCatastro cabecera) throws Exception {
		 
		String cabeceraXML="";
		if(cabecera!=null){
			Java2XMLCatastro parser = new Java2XMLCatastro();
			
			try {
				String parserCabecera = parser.write(cabecera,"control");
				cabeceraXML = cabeceraXML + parserCabecera;
			}
			catch(Exception e){
				e.printStackTrace();
				throw e;
			}
		}
		
		return cabeceraXML;
	 }
	 
	 public static String creaRequest(String nameTagRequest, String nameTagIn) throws Exception {
		 
		String cabeceraXML="";
		cabeceraXML = "<"+nameTagIn+" xmlns=\"http://www.catastro.meh.es/\">\n";
		cabeceraXML = cabeceraXML + 
			"<"+nameTagRequest + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
					"xsi:schemaLocation=\"http://www.catastro.meh.es/ http://www.catastro.meh.es/ws/esquemas/"+nameTagRequest+".xsd\">\n";

		return cabeceraXML;
	 }
	 
	 public static String crearCierreRequest(String nameXML, String nameTagIn){

		 String cierreRequest ="</"+nameXML+">\n";
		 cierreRequest = cierreRequest + "</"+nameTagIn+">";
		 return cierreRequest;

	 }
	 

	 public static String crearCuerpoCreacionExpedienteRequest(Expediente expediente) throws Exception{

    	String cuerpoCreacionExpedienteRequest = null;
    	
        try{

            if(expediente!=null) {
                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                
                StringBuffer creacionExpedienteRequest = new StringBuffer("<uden>\n");
                Java2XMLCatastro parser = new Java2XMLCatastro();
                String parserExp ="";

                parserExp = parser.write(expediente,"exp");
                creacionExpedienteRequest.append(parserExp);
                
                creacionExpedienteRequest.append("<lelem>\n");

            	creacionExpedienteRequest.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

            	creacionExpedienteRequest.append("<cd>");
            	creacionExpedienteRequest.append(expediente.getEntidadGeneradora().getCodigo());
                creacionExpedienteRequest.append("</cd>\n");

                creacionExpedienteRequest.append("<cmc>");                        
                creacionExpedienteRequest.append(expediente.getEntidadGeneradora().getDescripcion());
                creacionExpedienteRequest.append("</cmc>\n");

                creacionExpedienteRequest.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
    
                creacionExpedienteRequest.append("</lelem>\n</uden>\n");
                
                cuerpoCreacionExpedienteRequest = creacionExpedienteRequest.toString();
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        
        return cuerpoCreacionExpedienteRequest;
    }
	 
	 
	 public static String crearCuerpoConsultaCatastroRequest( Expediente expediente) throws Exception{

    	String cuerpoConsultaCatastroRequest = null;
    	
        try{

            if(expediente!=null) {
            	expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
                
                StringBuffer consultaEstadoRequest = new StringBuffer("<pregunta>\n");
                Java2XMLCatastro parser = new Java2XMLCatastro();
                String parserExp ="";

                parserExp = parser.write(expediente,"exp");
                consultaEstadoRequest.append(parserExp);
             
               
                for(int i=0; i< expediente.getListaReferencias().size(); i++){
                	boolean escribeDatos = false;
                	if(expediente.getListaReferencias().get(i) instanceof FincaCatastro){
	          	  	
                		if(((FincaCatastro)expediente.getListaReferencias().get(i)).getIdentificadorDialogo() == null || 
                				((FincaCatastro)expediente.getListaReferencias().get(i)).getIdentificadorDialogo().equals("")){
                			escribeDatos = true;
                		}
	          	  	}
                	else if (expediente.getListaReferencias().get(i) instanceof BienInmuebleCatastro){
                		if(((BienInmuebleCatastro)expediente.getListaReferencias().get(i)).getIdentificadorDialogo() == null ||
                				((BienInmuebleCatastro)expediente.getListaReferencias().get(i)).getIdentificadorDialogo().equals("")){
                			escribeDatos = true;
                		}
                	}
                	if(escribeDatos){
		                consultaEstadoRequest.append("<elem>\n");
		
		                consultaEstadoRequest.append("<locat>\n");
		
		                consultaEstadoRequest.append("<cd>");
		                consultaEstadoRequest.append(expediente.getEntidadGeneradora().getCodigo());
		                consultaEstadoRequest.append("</cd>\n");
		
		          	  	consultaEstadoRequest.append("<cmc>");                        
		          	  	consultaEstadoRequest.append(expediente.getEntidadGeneradora().getDescripcion());
		          	  	consultaEstadoRequest.append("</cmc>\n");
		
		          	  	consultaEstadoRequest.append("</locat>\n");
	
		          	  	
		          	  	if(expediente.getListaReferencias().get(i) instanceof FincaCatastro)
		          	  	{
		          	  		consultaEstadoRequest.append("<idf>\n");
		          	  		FincaCatastro finca = (FincaCatastro)expediente.getListaReferencias().get(i);
		          	  		if(finca.getBICE() !=null && !finca.getBICE().equals("")){
		          	  			consultaEstadoRequest.append("<cn>"+finca.getBICE()+"</cn>\n");
		          	  		}
		          	  		consultaEstadoRequest.append("<pc>\n");
		          	  		consultaEstadoRequest.append("<pc1>").append(finca.getRefFinca().getRefCatastral1()).append("</pc1>\n");
	      	  				consultaEstadoRequest.append("<pc2>").append(finca.getRefFinca().getRefCatastral2()).append("</pc2>\n");
	      	  				consultaEstadoRequest.append("</pc>\n");
	      	  				
	      	  				consultaEstadoRequest.append("</idf>\n");
		          	  	}
		          	  	else if(expediente.getListaReferencias().get(i) instanceof BienInmuebleCatastro)
		          	  	{
		          	  		BienInmuebleCatastro bien = (BienInmuebleCatastro)expediente.getListaReferencias().get(i);
		          	  		consultaEstadoRequest.append("<idbi>\n");
		          	  		
		          	  		consultaEstadoRequest.append("<cn>"+bien.getClaseBienInmueble()+"</cn>\n");
		          	  		
			          	  	consultaEstadoRequest.append("<rc>\n");
		          	  		consultaEstadoRequest.append("<pc1>").append(bien.getIdBienInmueble().getParcelaCatastral().getRefCatastral1()).append("</pc1>\n");
		  	  				consultaEstadoRequest.append("<pc2>").append(bien.getIdBienInmueble().getParcelaCatastral().getRefCatastral2()).append("</pc2>\n");
		  	  				consultaEstadoRequest.append("<car>").append(bien.getIdBienInmueble().getNumCargo()).append("</car>\n");
		  	  				consultaEstadoRequest.append("<cc1>").append(bien.getIdBienInmueble().getDigControl1()).append("</cc1>\n");
		  	  				consultaEstadoRequest.append("<cc2>").append(bien.getIdBienInmueble().getDigControl2()).append("</cc2>\n");
		  	  				consultaEstadoRequest.append("</rc>\n");
		          	  		
		          	  		consultaEstadoRequest.append("</idbi>\n");
		          	  	}
		          	  	
		          	  consultaEstadoRequest.append("</elem>\n");
                	
                	}
                }

          	  	consultaEstadoRequest.append("</pregunta>\n");
                
                cuerpoConsultaCatastroRequest = consultaEstadoRequest.toString();
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        
        return cuerpoConsultaCatastroRequest;
    }

	 
	 public static String crearCuerpoActualizaCatastroRequest(Expediente expediente, ArrayList arrayXmlExp) throws Exception{

		 StringBuffer cuerpoActualizaCatastoRequest = null;
		  try{

	            if(expediente!=null && arrayXmlExp!=null) {
	                expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_FINAL);

	                cuerpoActualizaCatastoRequest = new StringBuffer("<pregunta>\n<luden>\n<uden>\n");
	                Java2XMLCatastro parser = new Java2XMLCatastro();
	                String parserExp ="";

	                if(arrayXmlExp.size()>1 && arrayXmlExp.get(0) instanceof ArrayList && arrayXmlExp.get(1) instanceof ArrayList) {

	                    ArrayList xmlParcelasExp = (ArrayList)arrayXmlExp.get(0);
	                    ArrayList xmlFX_CC= (ArrayList) arrayXmlExp.get(1);
	                    ArrayList xmlIMG = null;
	                    if (arrayXmlExp.size()>2){
	                    	xmlIMG = (ArrayList) arrayXmlExp.get(2);
	                    }
	                    
	                    expediente.setExistenciaInformacionGrafica("N");

//	                    if(xmlFX_CC.isEmpty())
//	                        expediente.setExistenciaInformacionGrafica("N");
//	                    else
//	                        expediente.setExistenciaInformacionGrafica("S");

	                    parserExp = parser.write(expediente,"exp");
	                    cuerpoActualizaCatastoRequest.append(parserExp);
	                    cuerpoActualizaCatastoRequest.append("<lelem>\n");


	                    for(int j = 0; j<xmlParcelasExp.size();j++){

	                    	cuerpoActualizaCatastoRequest.append((String)xmlParcelasExp.get(j));

	                    	// de momento no esta contemplada la inclusion de los datos cartograficos
//	                        String fxcc = (String)xmlFX_CC.get(j);
//	                        String img = null;
//	                        if (xmlIMG != null && xmlIMG.size()>j){
//	                        	img = (String)xmlIMG.get(j);
//	                        }
//
//	                        if(fxcc!=null){
//	                            int p = cuerpoActualizaCatastoRequest.lastIndexOf("</elemf>");
//	                            cuerpoActualizaCatastoRequest = cuerpoActualizaCatastoRequest.delete(p, cuerpoActualizaCatastoRequest.length());
//	                            cuerpoActualizaCatastoRequest.append(fxcc);
//	                            if (img != null){
//	                            	cuerpoActualizaCatastoRequest.append(img);
//	                            }
//	                            cuerpoActualizaCatastoRequest.append("</elemf>\n");
//	                        }
//	                        else if (img != null){
//	                        	int p = cuerpoActualizaCatastoRequest.lastIndexOf("</elemf>");
//	                        	cuerpoActualizaCatastoRequest = cuerpoActualizaCatastoRequest.delete(p, cuerpoActualizaCatastoRequest.length());                            
//	                        	cuerpoActualizaCatastoRequest.append(img);
//	                        	cuerpoActualizaCatastoRequest.append("</elemf>\n");
//	                        }                        
	                        
	                    }
	                }
	                else {
	                    expediente.setTipoDeIntercambio(Expediente.TIPO_INTERCAMBIO_REGISTRO);
	                    expediente.setFechaAlteracion(expediente.getFechaRegistro());                   

	                    parserExp = parser.write(expediente,"exp");
	                    cuerpoActualizaCatastoRequest.append(parserExp);

	                    cuerpoActualizaCatastoRequest.append("<lelem>\n");

	                    if(arrayXmlExp.isEmpty()){
	                    	cuerpoActualizaCatastoRequest.append("<elemr>\n<bicreg>\n<idreg>\n<locat>\n");

	                        cuerpoActualizaCatastoRequest.append("<cd>");
	                        cuerpoActualizaCatastoRequest.append(expediente.getEntidadGeneradora().getCodigo());
	                        cuerpoActualizaCatastoRequest.append("</cd>");

	                        cuerpoActualizaCatastoRequest.append("<cmc>");                        
	                        cuerpoActualizaCatastoRequest.append(expediente.getDireccionPresentador().getCodigoMunicipioDGC());
	                        cuerpoActualizaCatastoRequest.append("</cmc>");

	                        cuerpoActualizaCatastoRequest.append("</locat>\n</idreg>\n</bicreg>\n</elemr>\n");
	                    }
	                    else{
	                        for(int i = 0; i<arrayXmlExp.size();i++){
	                            parserExp = parser.write(arrayXmlExp.get(i),"elemr");
	                            cuerpoActualizaCatastoRequest.append(parserExp);
	                        }
	                    }
	                }

	                cuerpoActualizaCatastoRequest.append("</lelem>\n</uden>\n</luden>\n</pregunta>\n");
	            }
	        }
	        catch(Exception e){
	            e.printStackTrace();
	            throw e;
	        }
		 
	        return cuerpoActualizaCatastoRequest.toString();
	    }
	 
	 
	 public static String crearCuerpoConsultaEstadoExpedienteRequest(Expediente expediente) throws Exception{
		 String  cuerpoConsultaEstadoExpedienteRequest= null;
	    	
	        try{
	        	  if(expediente!=null) {
	        		  StringBuffer consultaEstadoExpedienteRequest = new StringBuffer("<pregunta>\n");
	        		  
	        		  consultaEstadoExpedienteRequest.append("<expec>\n");
	        		  
	        		  consultaEstadoExpedienteRequest.append("<aexpec>");
	        		  consultaEstadoExpedienteRequest.append(expediente.getAnnoExpedienteAdminOrigenAlteracion());
	        		  consultaEstadoExpedienteRequest.append("</aexpec>\n");
	        		  
	        		  consultaEstadoExpedienteRequest.append("<rexpec>");
	        		  consultaEstadoExpedienteRequest.append(expediente.getReferenciaExpedienteAdminOrigen());
	        		  consultaEstadoExpedienteRequest.append("</rexpec>\n");
	        		  
	        		  consultaEstadoExpedienteRequest.append("<eoa>");
	        		  consultaEstadoExpedienteRequest.append(expediente.getCodigoDescriptivoAlteracion());

	        		  consultaEstadoExpedienteRequest.append("</eoa>\n");
	        		  consultaEstadoExpedienteRequest.append("</expec>\n");
	        		  
	        		  consultaEstadoExpedienteRequest.append("</pregunta>\n");
	        		  
	        		  
	        		  // pruebas con datos de un  expediente cerrado
//	        		  consultaEstadoExpedienteRequest.append("<expec>\n");
//	        		  
//	        		  consultaEstadoExpedienteRequest.append("<aexpec>");
//	        		  consultaEstadoExpedienteRequest.append("2007");
//	        		  consultaEstadoExpedienteRequest.append("</aexpec>\n");
//	        		  
//	        		  consultaEstadoExpedienteRequest.append("<rexpec>");
//	        		  consultaEstadoExpedienteRequest.append("0000000033741");
//	        		  consultaEstadoExpedienteRequest.append("</rexpec>\n");
//	        		  
//	        		  consultaEstadoExpedienteRequest.append("<eoa>");
//	        		  consultaEstadoExpedienteRequest.append("801");
//
//	        		  consultaEstadoExpedienteRequest.append("</eoa>\n");
//	        		  consultaEstadoExpedienteRequest.append("</expec>\n");
//	        		  
//	        		  consultaEstadoExpedienteRequest.append("</pregunta>\n");
	        		  
	        		  
	        		  cuerpoConsultaEstadoExpedienteRequest = consultaEstadoExpedienteRequest.toString();
	        	  }
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        	throw e;
	        }
	        
	        return cuerpoConsultaEstadoExpedienteRequest;
	 }


}
