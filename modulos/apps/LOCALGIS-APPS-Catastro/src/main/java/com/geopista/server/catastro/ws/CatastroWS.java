/**
 * CatastroWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.catastro.ws;

import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.servicioWebCatastro.beans.CabeceraWSCatastro;
import com.geopista.app.catastro.utils.ConstantesCatastro;
import com.localgis.util.COperacionesDGCUtil;

public class CatastroWS {
	
	
	 public String buildCreacionExpedienteRequest(Expediente expediente, 
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 CabeceraWSCatastro cabecera = COperacionesDGCUtil.creaCabeceraWS( expediente, ConstantesCatastro.TAG_CREACION_EXPEDIENTE_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesCatastro.TAG_CREACION_EXPEDIENTE_REQUEST, ConstantesCatastro.TAG_CREACION_EXPEDIENTE_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		
		 datosXML.append(PeticionWSCatastro.crearCuerpoCreacionExpedienteRequest(expediente));
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesCatastro.TAG_CREACION_EXPEDIENTE_REQUEST, ConstantesCatastro.TAG_CREACION_EXPEDIENTE_IN));
		 
		 return datosXML.toString();
	 }
	 
	 
	 public String buildConsultaCatastroRequest(Expediente expediente, 
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 expediente.setExistenciaInformacionGrafica(null);
		 CabeceraWSCatastro cabecera = COperacionesDGCUtil.creaCabeceraWS(expediente, ConstantesCatastro.TAG_CONSULTA_CATASTRO_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest( ConstantesCatastro.TAG_CONSULTA_CATASTRO_REQUEST, ConstantesCatastro.TAG_CONSULTA_CATASTRO_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 
		 expediente.setFechaMovimiento(null);
		 expediente.setHoraMovimiento(null);
		 datosXML.append(PeticionWSCatastro.crearCuerpoConsultaCatastroRequest(expediente));
		
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesCatastro.TAG_CONSULTA_CATASTRO_REQUEST, ConstantesCatastro.TAG_CONSULTA_CATASTRO_IN));
		 
		 
		 return datosXML.toString();
	 }
	 
	 public String buildActualizaCatastroRequest(Expediente expediente, 
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 expediente.setExistenciaInformacionGrafica("N");
		 
		 CabeceraWSCatastro cabecera = COperacionesDGCUtil.creaCabeceraWS( expediente, ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_REQUEST, ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_IN));
		 
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 

		if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//si el expediente es de situaciones finales
			 ImportacionOperations oper = new ImportacionOperations();
			 ArrayList arrayXmlExp = oper.getXmlParcelasFX_CCExp(expediente);

		 
			 datosXML.append(PeticionWSCatastro.crearCuerpoActualizaCatastroRequest(expediente, arrayXmlExp));
		 }
		 else{
			//si el expediente es de variaciones
			 ArrayList lstExp = new ArrayList();
			 lstExp.add(expediente);
			 AppContext.getApplicationContext().getBlackboard().put("catastroTemporal", false);
			 String cuerpo = ConstantesRegExp.clienteCatastro.obtenerCuerpoFinEntradaOVC(expediente, ConstantesCatastro.modoTrabajo, 
					 ConstantesCatastro.tipoConvenio,lstExp );
			 
			 datosXML.append("<pregunta>\n");
			 datosXML.append("<luden>\n");
			 datosXML.append(cuerpo);
			 datosXML.append("</luden>\n");
			 datosXML.append("</pregunta>\n");
		 }
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_REQUEST, ConstantesCatastro.TAG_ACTUALIZA_CATASTRO_IN));
		 
		 return datosXML.toString();
	 }
	 
	 public String buildConsultaEstadoExpedienteRequest(Expediente expediente, 
			 String nombreSolicitante, String nifSolicitante) throws Exception{

		 StringBuffer datosXML = new StringBuffer();
		 
		 CabeceraWSCatastro cabecera = COperacionesDGCUtil.creaCabeceraWS( expediente, ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST,
				 nombreSolicitante, nifSolicitante);
		 
		 datosXML.append(PeticionWSCatastro.creaRequest(ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST, ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_IN));
		 datosXML.append(PeticionWSCatastro.creaControl(cabecera));
		 
		 datosXML.append(PeticionWSCatastro.crearCuerpoConsultaEstadoExpedienteRequest(expediente));
		 
		 datosXML.append(PeticionWSCatastro.crearCierreRequest(ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_REQUEST, ConstantesCatastro.TAG_CONSULTA_ESTADO_EXPEDIENTE_IN));
		 
		 return datosXML.toString();
	 }

}
