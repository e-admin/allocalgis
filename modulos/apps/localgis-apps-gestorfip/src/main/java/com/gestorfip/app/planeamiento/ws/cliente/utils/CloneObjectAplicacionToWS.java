/**
 * CloneObjectAplicacionToWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.ws.cliente.utils;


import java.util.Arrays;

import com.gestorfip.app.planeamiento.beans.panels.tramite.AdscripcionesPanelBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.DeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.DeterminacionReguladoraBean;
import es.gestorfip.serviciosweb.ServicesStub.DocumentoDeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.DocumentoEntidadBean;
import es.gestorfip.serviciosweb.ServicesStub.EntidadBean;
import es.gestorfip.serviciosweb.ServicesStub.GrupoAplicacionBean;
import es.gestorfip.serviciosweb.ServicesStub.OperacionDeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.OperacionEntidadBean;
import es.gestorfip.serviciosweb.ServicesStub.RegulacionesEspecificasBean;
import es.gestorfip.serviciosweb.ServicesStub.ValoresReferenciaBean;

public class CloneObjectAplicacionToWS {

	
	public static void cloneDeterminacionBeanToWSDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		str.setId(detBean.getId());
		str.setCodigo(detBean.getCodigo());
		str.setCaracter(detBean.getCaracter());
		str.setApartado(detBean.getApartado());
		str.setNombre(detBean.getNombre());
		str.setEtiqueta(detBean.getEtiqueta());
		str.setSuspendida(detBean.isSuspendida());
		str.setTexto(detBean.getTexto());
		str.setUnidad_determinacionid(detBean.getUnidad_determinacionid());
		str.setBase_determinacionid(detBean.getBase_determinacionid());
		str.setMadre(detBean.getMadre());
		str.setTramite(detBean.getTramite());

	}
	
	public static void cloneOperacionDeterminacionBeanToWSOperacionDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean[] lstOperacionDeterminacion,
			DeterminacionBean str){
		OperacionDeterminacionBean[] lstOperacionDeterminacionWS = null;
		
		if(lstOperacionDeterminacion != null){
			for(int i=0; i<lstOperacionDeterminacion.length; i++){
				
				
				OperacionDeterminacionBean oper = new OperacionDeterminacionBean();
				
				
				oper.setId(lstOperacionDeterminacion[i].getId());
				oper.setOperadora_determinacionid(lstOperacionDeterminacion[i].getOperadora_determinacionid());
				oper.setOrden(lstOperacionDeterminacion[i].getOrden());
				oper.setTexto(lstOperacionDeterminacion[i].getTexto());
				oper.setTipo(lstOperacionDeterminacion[i].getTipo());
				oper.setTramite(lstOperacionDeterminacion[i].getTramite());
				
				DeterminacionBean operadaDetBean = new DeterminacionBean();
				operadaDetBean.setId(lstOperacionDeterminacion[i].getOperada_determinacion().getId());
				oper.setOperada_determinacion(operadaDetBean);
				
				
				if(lstOperacionDeterminacionWS == null){
					
					lstOperacionDeterminacionWS = new OperacionDeterminacionBean[1];
					lstOperacionDeterminacionWS[0] = oper;
				
				}
				else{
					lstOperacionDeterminacionWS = (OperacionDeterminacionBean[]) Arrays.copyOf(
							lstOperacionDeterminacionWS, lstOperacionDeterminacionWS.length+1);
				
					lstOperacionDeterminacionWS[lstOperacionDeterminacionWS.length-1] = 
						(OperacionDeterminacionBean)oper;
				}	
				
				
				
				
				/*
				str.setOperacionDeterminacion(new OperacionDeterminacionBean());
			
				str.getOperacionDeterminacion().setId(lstOperacionDeterminacion[i].getId());
				str.getOperacionDeterminacion().setOperadora_determinacionid(lstOperacionDeterminacion[i].getOperadora_determinacionid());
				str.getOperacionDeterminacion().setOrden(lstOperacionDeterminacion[i].getOrden());
				str.getOperacionDeterminacion().setTexto(lstOperacionDeterminacion[i].getTexto());
				str.getOperacionDeterminacion().setTipo(lstOperacionDeterminacion[i].getTipo());
				str.getOperacionDeterminacion().setTramite(lstOperacionDeterminacion[i].getTramite());
				
				DeterminacionBean operadaDetBean = new DeterminacionBean();
				operadaDetBean.setId(lstOperacionDeterminacion[i].getOperada_determinacion().getId());
				str.getOperacionDeterminacion().setOperada_determinacion(operadaDetBean);
				*/
			}
			str.setLstOperacionDeterminacion(lstOperacionDeterminacionWS);
			
		}
	}
	
	public static void cloneValoresReferenciaBeanToWSValoresReferenciaBean(
								com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
								DeterminacionBean str){
		
		if(detBean.getLstValoresReferencia() != null){
			ValoresReferenciaBean[] lstValRef = 
				new ValoresReferenciaBean[detBean.getLstValoresReferencia().length];
			for(int i=0; i<detBean.getLstValoresReferencia().length; i++){
				
				ValoresReferenciaBean valRef = 	new ValoresReferenciaBean();
				
				valRef.setId(detBean.getLstValoresReferencia()[i].getId());
				valRef.setDeterminacionid(detBean.getLstValoresReferencia()[i].getDeterminacionid());
				valRef.setDeterminacion(detBean.getLstValoresReferencia()[i].getDeterminacion());
				
				lstValRef[i] = valRef;
			}
			str.setLstValoresReferencia(lstValRef);
		}
		
		if(detBean.getLstValoresReferenciaAlta() != null){
			ValoresReferenciaBean[] lstValRef = 
				new ValoresReferenciaBean[detBean.getLstValoresReferenciaAlta().length];
			for(int i=0; i<detBean.getLstValoresReferenciaAlta().length; i++){
				
				ValoresReferenciaBean valRef = 	new ValoresReferenciaBean();
				
				valRef.setId(detBean.getLstValoresReferenciaAlta()[i].getId());
				valRef.setDeterminacionid(detBean.getLstValoresReferenciaAlta()[i].getDeterminacionid());
				valRef.setDeterminacion(detBean.getLstValoresReferenciaAlta()[i].getDeterminacion());
				
				lstValRef[i] = valRef;
			}
			str.setLstValoresReferenciaAlta(lstValRef);
		}
		
		if(detBean.getLstValoresReferenciaBaja() != null){
			ValoresReferenciaBean[] lstValRef = 
				new ValoresReferenciaBean[detBean.getLstValoresReferenciaBaja().length];
			for(int i=0; i<detBean.getLstValoresReferenciaBaja().length; i++){
				
				ValoresReferenciaBean valRef = 	new ValoresReferenciaBean();
				
				valRef.setId(detBean.getLstValoresReferenciaBaja()[i].getId());
				
				lstValRef[i] = valRef;
			}
			str.setLstValoresReferenciaBaja(lstValRef);
		}
		
	}
	
	public static void cloneGrupoAplicacionBeanToWSGrupoAplicacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		
		if(detBean.getLstGrupoAplicacion() != null){
			GrupoAplicacionBean[] lstGrupAplic = 
				new GrupoAplicacionBean[detBean.getLstGrupoAplicacion().length];
			for(int i=0; i<detBean.getLstGrupoAplicacion().length; i++){
				
				GrupoAplicacionBean grupoAplic = new GrupoAplicacionBean();
				
				grupoAplic.setId(detBean.getLstGrupoAplicacion()[i].getId());
				grupoAplic.setDeterminacionid(detBean.getLstGrupoAplicacion()[i].getDeterminacionid());
				grupoAplic.setDeterminacion(detBean.getLstGrupoAplicacion()[i].getDeterminacion());
				
				lstGrupAplic[i] = grupoAplic;
			}
			str.setLstGrupoAplicacion(lstGrupAplic);
		}
		
		if(detBean.getLstGrupoAplicacionAlta() != null){
			GrupoAplicacionBean[] lstGrupAplic = 
				new GrupoAplicacionBean[detBean.getLstGrupoAplicacionAlta().length];
			for(int i=0; i<detBean.getLstGrupoAplicacionAlta().length; i++){
				
				GrupoAplicacionBean grupoAplic = new GrupoAplicacionBean();
				
				grupoAplic.setId(detBean.getLstGrupoAplicacionAlta()[i].getId());
				grupoAplic.setDeterminacionid(detBean.getLstGrupoAplicacionAlta()[i].getDeterminacionid());
				grupoAplic.setDeterminacion(detBean.getLstGrupoAplicacionAlta()[i].getDeterminacion());
				
				lstGrupAplic[i] = grupoAplic;
			}
			str.setLstGrupoAplicacionAlta(lstGrupAplic);
		}
		
		if(detBean.getLstGrupoAplicacionBaja() != null){
			GrupoAplicacionBean[] lstGrupAplic = 
				new GrupoAplicacionBean[detBean.getLstGrupoAplicacionBaja().length];
			for(int i=0; i<detBean.getLstGrupoAplicacionBaja().length; i++){
				
				GrupoAplicacionBean grupoAplic = new GrupoAplicacionBean();
				
				grupoAplic.setId(detBean.getLstGrupoAplicacionBaja()[i].getId());
				
				lstGrupAplic[i] = grupoAplic;
			}
			str.setLstGrupoAplicacionBaja(lstGrupAplic);
		}

	}
	
	public static void cloneOperacionEntidadBeanToWSOperacionEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean[] lstOperacionEntidad,
			EntidadBean str){
		
		OperacionEntidadBean[] lstOperacionEntidadWS = null;

		if(lstOperacionEntidad != null){
			for(int i=0; i<lstOperacionEntidad.length; i++){
				
				
				OperacionEntidadBean oper = new OperacionEntidadBean();
				
				//str.setOperacionEntidad(new OperacionEntidadBean());
				
				oper.setId(lstOperacionEntidad[i].getId());
				oper.setOperadora_entidadid(lstOperacionEntidad[i].getOperadora_entidadid());
				oper.setOrden(lstOperacionEntidad[i].getOrden());
				oper.setTexto(lstOperacionEntidad[i].getTexto());
				oper.setTipo(lstOperacionEntidad[i].getTipo());
				oper.setTramite(lstOperacionEntidad[i].getTramite());
				
				EntidadBean operadaEntBean = new EntidadBean();
				operadaEntBean.setId(lstOperacionEntidad[i].getOperada_entidad().getId());
				oper.setOperada_entidad(operadaEntBean);
				
				oper.setPropiedadesadscripcion_cuantia(
						lstOperacionEntidad[i].getPropiedadesadscripcion_cuantia());
				oper.setPropiedadesadscripcion_texto(
						lstOperacionEntidad[i].getPropiedadesadscripcion_texto());
				oper.setPropiedadesadscripcion_unidad_determinacionid(
						lstOperacionEntidad[i].getPropiedadesadscripcion_unidad_determinacionid());
				oper.setPropiedadesadscripcion_tipo_determinacionid(
						lstOperacionEntidad[i].getPropiedadesadscripcion_tipo_determinacionid());
				
				
				if(lstOperacionEntidadWS == null){
					
					lstOperacionEntidadWS = new OperacionEntidadBean[1];
					lstOperacionEntidadWS[0] = oper;
				
				}
				else{
					lstOperacionEntidadWS = (OperacionEntidadBean[]) Arrays.copyOf(
							lstOperacionEntidadWS, lstOperacionEntidadWS.length+1);
				
					lstOperacionEntidadWS[lstOperacionEntidadWS.length-1] = 
						(OperacionEntidadBean)oper;
				}	
				
			}
			str.setLstOperacionEntidad(lstOperacionEntidadWS);
		}
	}
	
	
	public static void cloneDocumentosEntidaBeanToWSDocumentosEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entiBean,
			EntidadBean str){


		if(entiBean.getLstDocumentoAlta() != null){
			DocumentoEntidadBean[] lstDocumento = 
				new DocumentoEntidadBean[entiBean.getLstDocumentoAlta().length];
			for(int i=0; i<entiBean.getLstDocumentoAlta().length; i++){
				
				DocumentoEntidadBean documento = new DocumentoEntidadBean();
				
				documento.setId(entiBean.getLstDocumentoAlta()[i].getId());
				documento.setDocumentoid(entiBean.getLstDocumentoAlta()[i].getDocumentoid());
				documento.setDeterminacion(entiBean.getLstDocumentoAlta()[i].getDeterminacion());
				
				lstDocumento[i] = documento;
			}
			str.setLstDocumetosAlta(lstDocumento);
		}
		
		if(entiBean.getLstDocumentoBaja() != null){
			DocumentoEntidadBean[] lstDocumento = 
				new DocumentoEntidadBean[entiBean.getLstDocumentoBaja().length];
			for(int i=0; i<entiBean.getLstDocumentoBaja().length; i++){
				
				DocumentoEntidadBean documento = new DocumentoEntidadBean();
				
				documento.setId(entiBean.getLstDocumentoBaja()[i].getId());
				
				lstDocumento[i] = documento;
			}
			str.setLstDocumetosBaja(lstDocumento);
		}
		
	}
	
	public static void cloneDocumentosDeterminacionBeanToWSDocumentosDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){


		if(detBean.getLstDocumentoAlta() != null){
			DocumentoDeterminacionBean[] lstDocumento = 
				new DocumentoDeterminacionBean[detBean.getLstDocumentoAlta().length];
			for(int i=0; i<detBean.getLstDocumentoAlta().length; i++){
				
				DocumentoDeterminacionBean documento = new DocumentoDeterminacionBean();
				
				documento.setId(detBean.getLstDocumentoAlta()[i].getId());
				documento.setDocumentoid(detBean.getLstDocumentoAlta()[i].getDocumentoid());
				documento.setDeterminacion(detBean.getLstDocumentoAlta()[i].getDeterminacion());
				
				lstDocumento[i] = documento;
			}
			str.setLstDocumetosAlta(lstDocumento);
		}
		
		if(detBean.getLstDocumentoBaja() != null){
			DocumentoDeterminacionBean[] lstDocumento = 
				new DocumentoDeterminacionBean[detBean.getLstDocumentoBaja().length];
			for(int i=0; i<detBean.getLstDocumentoBaja().length; i++){
				
				DocumentoDeterminacionBean documento = new DocumentoDeterminacionBean();
				
				documento.setId(detBean.getLstDocumentoBaja()[i].getId());
				
				lstDocumento[i] = documento;
			}
			str.setLstDocumetosBaja(lstDocumento);
		}
		
	}
	
	public static void cloneDeterminacionesReguladorasBeanToWSDeterminacionesReguladorasBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		
		if(detBean.getLstDeterminacionReguladora() != null){
			DeterminacionReguladoraBean[] lstDeterRegula = 
				new DeterminacionReguladoraBean[detBean.getLstDeterminacionReguladora().length];
			for(int i=0; i<detBean.getLstDeterminacionReguladora().length; i++){
				
				DeterminacionReguladoraBean deterRegula = new DeterminacionReguladoraBean();
				
				deterRegula.setId(detBean.getLstDeterminacionReguladora()[i].getId());
				deterRegula.setDeterminacionid(detBean.getLstDeterminacionReguladora()[i].getDeterminacionid());
				deterRegula.setDeterminacion(detBean.getLstDeterminacionReguladora()[i].getDeterminacion());
				
				lstDeterRegula[i] = deterRegula;
			}
			str.setLstDeterminacionReguladora(lstDeterRegula);
		}
		
		if(detBean.getLstDeterminacionReguladoraAlta() != null){
			DeterminacionReguladoraBean[] lstDeterRegula = 
				new DeterminacionReguladoraBean[detBean.getLstDeterminacionReguladoraAlta().length];
			for(int i=0; i<detBean.getLstDeterminacionReguladoraAlta().length; i++){
				
				DeterminacionReguladoraBean deterRegula = new DeterminacionReguladoraBean();
				
				deterRegula.setId(detBean.getLstDeterminacionReguladoraAlta()[i].getId());
				deterRegula.setDeterminacionid(detBean.getLstDeterminacionReguladoraAlta()[i].getDeterminacionid());
				deterRegula.setDeterminacion(detBean.getLstDeterminacionReguladoraAlta()[i].getDeterminacion());
				
				lstDeterRegula[i] = deterRegula;
			}
			str.setLstDeterminacionReguladoraAlta(lstDeterRegula);
		}
		
		if(detBean.getLstDeterminacionReguladoraBaja() != null){
			DeterminacionReguladoraBean[] lstGrupAplic = 
				new DeterminacionReguladoraBean[detBean.getLstDeterminacionReguladoraBaja().length];
			for(int i=0; i<detBean.getLstDeterminacionReguladoraBaja().length; i++){
				
				DeterminacionReguladoraBean deterRegula = new DeterminacionReguladoraBean();
				
				deterRegula.setId(detBean.getLstDeterminacionReguladoraBaja()[i].getId());
				
				lstGrupAplic[i] = deterRegula;
			}
			str.setLstDeterminacionReguladoraBaja(lstGrupAplic);
		}
		
	}
	
	public static void cloneRegulacionesEspecificasBeanWSToRegulacionesEspecificasBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){
		
		if(detBean.getLstRegulacionesEspecificasModif() != null){
			RegulacionesEspecificasBean[] lstRegulacionesEspecificas =
				new RegulacionesEspecificasBean[detBean.getLstRegulacionesEspecificasModif().length];
			
			for(int i=0; i<detBean.getLstRegulacionesEspecificasModif().length; i++){
				
				RegulacionesEspecificasBean regulacionEspecifica =	new RegulacionesEspecificasBean();
				
				//if(str.getLstRegulacionesEspecificas()[i] != null){
					regulacionEspecifica.setId(detBean.getLstRegulacionesEspecificasModif()[i].getId());
					regulacionEspecifica.setOrden(detBean.getLstRegulacionesEspecificasModif()[i].getOrden());
					regulacionEspecifica.setNombre(detBean.getLstRegulacionesEspecificasModif()[i].getNombre());
					regulacionEspecifica.setTexto(detBean.getLstRegulacionesEspecificasModif()[i].getTexto());
					regulacionEspecifica.setMadre(detBean.getLstRegulacionesEspecificasModif()[i].getMadre());
					regulacionEspecifica.setDeterminacion(detBean.getLstRegulacionesEspecificasModif()[i].getDeterminacion());
				
					lstRegulacionesEspecificas[i] = regulacionEspecifica;
				//}
			}
			
			str.setLstRegulacionesEspecificasModif(lstRegulacionesEspecificas);
		}
		
		if(detBean.getLstRegulacionesEspecificasAlta() != null){
			RegulacionesEspecificasBean[] lstRegulacionesEspecificas =
				new RegulacionesEspecificasBean[detBean.getLstRegulacionesEspecificasAlta().length];
			
			for(int i=0; i<detBean.getLstRegulacionesEspecificasAlta().length; i++){
				
				RegulacionesEspecificasBean regulacionEspecifica =	new RegulacionesEspecificasBean();
				
				//if(str.getLstRegulacionesEspecificas()[i] != null){
					regulacionEspecifica.setId(detBean.getLstRegulacionesEspecificasAlta()[i].getId());
					regulacionEspecifica.setOrden(detBean.getLstRegulacionesEspecificasAlta()[i].getOrden());
					regulacionEspecifica.setNombre(detBean.getLstRegulacionesEspecificasAlta()[i].getNombre());
					regulacionEspecifica.setTexto(detBean.getLstRegulacionesEspecificasAlta()[i].getTexto());
					regulacionEspecifica.setMadre(detBean.getLstRegulacionesEspecificasAlta()[i].getMadre());
					regulacionEspecifica.setDeterminacion(detBean.getLstRegulacionesEspecificasAlta()[i].getDeterminacion());
				
					lstRegulacionesEspecificas[i] = regulacionEspecifica;
				//}
			}
			
			str.setLstRegulacionesEspecificasAlta(lstRegulacionesEspecificas);
		}

		if(detBean.getLstRegulacionesEspecificasBaja() != null){
			RegulacionesEspecificasBean[] lstRegulacionesEspecificas =
				new RegulacionesEspecificasBean[detBean.getLstRegulacionesEspecificasBaja().length];
			
			for(int i=0; i<detBean.getLstRegulacionesEspecificasBaja().length; i++){
				
				RegulacionesEspecificasBean regulacionEspecifica =	new RegulacionesEspecificasBean();
				
				//if(str.getLstRegulacionesEspecificas()[i] != null){
					regulacionEspecifica.setId(detBean.getLstRegulacionesEspecificasBaja()[i].getId());
					/*regulacionEspecifica.setOrden(detBean.getLstRegulacionesEspecificasBaja()[i].getOrden());
					regulacionEspecifica.setNombre(detBean.getLstRegulacionesEspecificasBaja()[i].getNombre());
					regulacionEspecifica.setTexto(detBean.getLstRegulacionesEspecificasBaja()[i].getTexto());
					regulacionEspecifica.setMadre(detBean.getLstRegulacionesEspecificasBaja()[i].getMadre());
					regulacionEspecifica.setDeterminacion(detBean.getLstRegulacionesEspecificasBaja()[i].getDeterminacion());
				*/
					lstRegulacionesEspecificas[i] = regulacionEspecifica;
				//}
			}
			
			str.setLstRegulacionesEspecificasBaja(lstRegulacionesEspecificas);
		}
	}	

	public static void cloneEntidadBeanToWSEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entidadBean,
			EntidadBean str){

		str.setId(entidadBean.getId());
		str.setCodigo(entidadBean.getCodigo());
		str.setClave(entidadBean.getClave());
		str.setNombre(entidadBean.getNombre());
		str.setEtiqueta(entidadBean.getEtiqueta());
		str.setSuspendida(entidadBean.isSuspendida());
		str.setBase_entidadid(entidadBean.getBase_entidadid());
		str.setMadre(entidadBean.getMadre());
		str.setTramite(entidadBean.getTramite());
		str.setIdFeature(entidadBean.getIdFeature());
		str.setIdLayer(entidadBean.getIdLayer());

	}
	
	/*************************************************************************************
	 * 						CONDICIONES URBANISTICAS
	 *************************************************************************************/

	public static void cloneCondicionUrbanisticaCasoBeanToWSCondicionUrbanisticaCasoBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean condicionUrbanisticaCaso,
			CondicionUrbanisticaCasoBean str){

		str.setId(condicionUrbanisticaCaso.getId());
		str.setNombre(condicionUrbanisticaCaso.getNombre());
		str.setCodigo(condicionUrbanisticaCaso.getCodigo());
		str.setCondicionurbanistica(condicionUrbanisticaCaso.getCondicionurbanistica());
		

	}
	
	public static void cloneCondicionUrbanisticaCasoRegimenesEspecificosBeanToWSCondicionUrbanisticaCasoRegimenesEspecificosBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean condicionUrbanisticaCasoRegimenRegimenes,
			CondicionUrbanisticaCasoRegimenRegimenesBean  str){
		

		str.setId(condicionUrbanisticaCasoRegimenRegimenes.getId());
		str.setNombre(condicionUrbanisticaCasoRegimenRegimenes.getNombre());
		str.setOrden(condicionUrbanisticaCasoRegimenRegimenes.getOrden());
		str.setPadre(condicionUrbanisticaCasoRegimenRegimenes.getPadre());
		str.setRegimen(condicionUrbanisticaCasoRegimenRegimenes.getRegimen());
		str.setTexto(condicionUrbanisticaCasoRegimenRegimenes.getTexto());
		
		
	}
	
	public static void cloneCondicionUrbanisticaCasoRegimenesBeanToWSCondicionUrbanisticaCasoRegimenesBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean condicionUrbanisticaCasoRegimenes,
			CondicionUrbanisticaCasoRegimenesBean  str){

		str.setId(condicionUrbanisticaCasoRegimenes.getId());
		str.setComentario(condicionUrbanisticaCasoRegimenes.getComentario());
		str.setValor(condicionUrbanisticaCasoRegimenes.getValor());
		str.setSuperposicion(condicionUrbanisticaCasoRegimenes.getSuperposicion());
		str.setValorreferencia_determinacionid(condicionUrbanisticaCasoRegimenes.getValorreferencia_determinacionid());
		str.setDeterminacionregimen_determinacionid(condicionUrbanisticaCasoRegimenes.getDeterminacionregimen_determinacionid());
		str.setCasoaplicacion_casoid(condicionUrbanisticaCasoRegimenes.getCasoaplicacion_casoid());
		str.setCaso(condicionUrbanisticaCasoRegimenes.getCaso());
		
		
	}
		

	/*************************************************************************************
	 * 						FIN CONDICIONES URBANISTICAS
	 *************************************************************************************/
	

	
}



