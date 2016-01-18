package com.geopista.server.catastro.ws;

import java.util.ArrayList;

import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.servicioWebCatastro.beans.CabeceraWSCatastro;
import com.geopista.server.database.COperacionesDGC;

public class CatastroWS {
	
	
	 public String buildCreacionExpedienteRequest(Expediente expediente, COperacionesDGC geoConn,
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 CabeceraWSCatastro cabecera = geoConn.creaCabeceraWS( expediente, ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_REQUEST, ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		
		 datosXML.append(PeticionWSCatastro.crearCuerpoCreacionExpedienteRequest(expediente));
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_REQUEST, ConstantesRegistroExp.TAG_CREACION_EXPEDIENTE_IN));
		 
		 return datosXML.toString();
	 }
	 
	 
	 public String buildConsultaCatastroRequest(Expediente expediente, COperacionesDGC geoConn,
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 
		 CabeceraWSCatastro cabecera = geoConn.creaCabeceraWS(expediente, ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest( ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_REQUEST, ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 
		 expediente.setFechaMovimiento(null);
		 expediente.setHoraMovimiento(null);
		 datosXML.append(PeticionWSCatastro.crearCuerpoConsultaCatastroRequest(expediente));
		
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_REQUEST, ConstantesRegistroExp.TAG_CONSULTA_CATASTRO_IN));
		 
		 
		 return datosXML.toString();
	 }
	 
	 public String buildActualizaCatastroRequest(Expediente expediente, COperacionesDGC geoConn,
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 expediente.setExistenciaInformacionGrafica("N");
		 
		 CabeceraWSCatastro cabecera = geoConn.creaCabeceraWS( expediente, ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_REQUEST, ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 

		 ImportacionOperations oper = new ImportacionOperations();
		 ArrayList arrayXmlExp = oper.getXmlParcelasFX_CCExp(expediente);

		 
		 datosXML.append(PeticionWSCatastro.crearCuerpoActualizaCatastroRequest(expediente, arrayXmlExp));
		 
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_REQUEST, ConstantesRegistroExp.TAG_ACTUALIZA_CATASTRO_IN));
		 
		 return datosXML.toString();
	 }
	 
	 public String buildConsultaEstadoExpedienteRequest(Expediente expediente, COperacionesDGC geoConn,
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 
		 CabeceraWSCatastro cabecera = geoConn.creaCabeceraWS( expediente, ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST, ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_IN));
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 
		 datosXML.append(PeticionWSCatastro.crearCuerpoConsultaEstadoExpedienteRequest(expediente));
		 
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST, ConstantesRegistroExp.TAG_CONSULTA_ESTADO_EXPEDIENTE_IN));
		 
		 return datosXML.toString();
	 }

}
