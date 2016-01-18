/**
 * CloneObjectWSToAplicacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.ws.cliente.utils;

import java.util.Arrays;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.DeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.EntidadBean;

public class CloneObjectWSToAplicacion {

	
	public static void cloneWSGrupoAplicacionBeanToGrupoAplicacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		if(str.getLstGrupoAplicacion() != null && str.getLstGrupoAplicacion().length != 0 ){

			
			com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean[] lstGrupoAplicacion =  null;
			
			for(int i=0; i<str.getLstGrupoAplicacion().length; i++){
				
				if( str.getLstGrupoAplicacion()[i] != null){
					
					com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean grupoAplicacion = 
					new com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean();

					grupoAplicacion.setId(str.getLstGrupoAplicacion()[i].getId());
					grupoAplicacion.setDeterminacionid(str.getLstGrupoAplicacion()[i].getDeterminacionid());
					grupoAplicacion.setDeterminacion(str.getLstGrupoAplicacion()[i].getDeterminacion());
					
					if(lstGrupoAplicacion == null){
						lstGrupoAplicacion = new com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean[1];
						lstGrupoAplicacion[0] = grupoAplicacion;
					}
					else{
						lstGrupoAplicacion = (com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean[])Arrays.copyOf(lstGrupoAplicacion, lstGrupoAplicacion.length+1);
						lstGrupoAplicacion[lstGrupoAplicacion.length-1] = grupoAplicacion;
					}
					
				}
			}
			
			detBean.setLstGrupoAplicacion(lstGrupoAplicacion);
			
			
			
			
//			com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean[] lstGrupoAplicacion = 
//			new com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean[str.getLstGrupoAplicacion().length];
//			
//			for(int i=0; i<str.getLstGrupoAplicacion().length; i++){
//			
//				if( str.getLstGrupoAplicacion()[i] != null){
//				com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean grupoAplicacion = 
//				new com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean();
//				
//				grupoAplicacion.setId(str.getLstGrupoAplicacion()[i].getId());
//				grupoAplicacion.setDeterminacionid(str.getLstGrupoAplicacion()[i].getDeterminacionid());
//				grupoAplicacion.setDeterminacion(str.getLstGrupoAplicacion()[i].getDeterminacion());
//				
//				lstGrupoAplicacion[i] = grupoAplicacion;
//				}
//			}
//		
//			detBean.setLstGrupoAplicacion(lstGrupoAplicacion);
		}
	}
	
	public static void cloneWSDeterminacionesReguladorasBeanToDeterminacionesReguladorasBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		
		if(str.getLstDeterminacionReguladora() != null){
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionReguladoraBean[] lstDeterminacionReguladora = 
			new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionReguladoraBean[str.getLstDeterminacionReguladora().length];
			
			for(int i=0; i<str.getLstDeterminacionReguladora().length; i++){
			
				if(str.getLstDeterminacionReguladora()[i] != null){
					com.gestorfip.app.planeamiento.beans.tramite.DeterminacionReguladoraBean determinacionReguladora = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionReguladoraBean();
					
					determinacionReguladora.setId(str.getLstDeterminacionReguladora()[i].getId());
					determinacionReguladora.setDeterminacionid(str.getLstDeterminacionReguladora()[i].getDeterminacionid());
					determinacionReguladora.setDeterminacion(str.getLstDeterminacionReguladora()[i].getDeterminacion());
					
					lstDeterminacionReguladora[i] = determinacionReguladora;
				}
			}
		
			detBean.setLstDeterminacionReguladora(lstDeterminacionReguladora);
		}
	}
	
	public static void cloneWSDeterminacionBeanToDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){

		detBean.setId(str.getId());
		detBean.setCodigo(str.getCodigo());
		detBean.setCaracter(str.getCaracter());
		detBean.setApartado(str.getApartado());
		detBean.setNombre(str.getNombre());
		detBean.setEtiqueta(str.getEtiqueta());
		detBean.setSuspendida(str.getSuspendida());
		detBean.setTexto(str.getTexto());
		detBean.setUnidad_determinacionid(str.getUnidad_determinacionid());
		detBean.setBase_determinacionid(str.getBase_determinacionid());
		detBean.setMadre(str.getMadre());
		detBean.setTramite(str.getTramite());
		
		//detBean.setModificable(isModificable);
	
	}
	
	public static void cloneWSValoresReferenciaBeanToValoresReferenciaBean(
					com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
					DeterminacionBean str){
	
	
		if(str.getLstValoresReferencia() != null){
			com.gestorfip.app.planeamiento.beans.tramite.ValoresReferenciaBean[] lstValRef = 
			new com.gestorfip.app.planeamiento.beans.tramite.ValoresReferenciaBean[str.getLstValoresReferencia().length];
			
			for(int i=0; i<str.getLstValoresReferencia().length; i++){
			
				if(str.getLstValoresReferencia()[i] != null){
					com.gestorfip.app.planeamiento.beans.tramite.ValoresReferenciaBean valRef = 
						new com.gestorfip.app.planeamiento.beans.tramite.ValoresReferenciaBean();
					
					valRef.setId(str.getLstValoresReferencia()[i].getId());
					valRef.setDeterminacionid(str.getLstValoresReferencia()[i].getDeterminacionid());
					valRef.setDeterminacion(str.getLstValoresReferencia()[i].getDeterminacion());
					
					lstValRef[i] = valRef;
				}
			}
	
			detBean.setLstValoresReferencia(lstValRef);
		}
	}
	
	public static void cloneWSOperacionDeterminacionBeanToOperacionDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){
		com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean[] lstOperDet = null;
		
		if(str.getLstOperacionDeterminacion()!= null){
			for(int i=0; i<str.getLstOperacionDeterminacion().length; i++){
				
				if(str.getLstOperacionDeterminacion()[i] != null){
					com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean operacionDeterminacion =
						new com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean();
					
					operacionDeterminacion.setId(str.getLstOperacionDeterminacion()[i].getId());
					operacionDeterminacion.setTipo(str.getLstOperacionDeterminacion()[i].getTipo());
					operacionDeterminacion.setOrden(str.getLstOperacionDeterminacion()[i].getOrden());
					operacionDeterminacion.setTexto(str.getLstOperacionDeterminacion()[i].getTexto());
					operacionDeterminacion.setOperadora_determinacionid(str.getLstOperacionDeterminacion()[i].getOperadora_determinacionid());
					operacionDeterminacion.setTramite(str.getLstOperacionDeterminacion()[i].getTramite());
					
					com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean detOperadora = 
						new com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean();
						
					detOperadora.setId(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getId());
					detOperadora.setCodigo(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getCodigo());
					detOperadora.setCaracter(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getCaracter());
					detOperadora.setApartado(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getApartado());
					detOperadora.setNombre(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getNombre());
					detOperadora.setEtiqueta(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getEtiqueta());
					detOperadora.setSuspendida(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getSuspendida());
					detOperadora.setTexto(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getTexto());
					detOperadora.setUnidad_determinacionid(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getUnidad_determinacionid());
					detOperadora.setBase_determinacionid(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getBase_determinacionid());
					detOperadora.setMadre(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getMadre());
					detOperadora.setTramite(str.getLstOperacionDeterminacion()[i].getOperada_determinacion().getTramite());
		
					operacionDeterminacion.setOperada_determinacion(detOperadora);
					
					
					if(lstOperDet == null){
						
						lstOperDet = new com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean[1];
						lstOperDet[0] = operacionDeterminacion;
					
					}
					else{
						lstOperDet = (com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean[]) Arrays.copyOf(
								lstOperDet, lstOperDet.length+1);
					
						lstOperDet[lstOperDet.length-1] = operacionDeterminacion;
					}	
				}			
			}
			detBean.setLstOperacionDeterminacion(lstOperDet);
			
		}
		
		
	}
	
	public static void cloneWSDocumentosDeterminacionBeanToDocumentosDeterminacionBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){
		
		if(str.getLstDocumetos()!= null){
			com.gestorfip.app.planeamiento.beans.tramite.DocumentoDeterminacionBean[] lstDocumentos =
				new com.gestorfip.app.planeamiento.beans.tramite.DocumentoDeterminacionBean[str.getLstDocumetos().length];
			
			for(int i=0; i<str.getLstDocumetos().length; i++){
				
				com.gestorfip.app.planeamiento.beans.tramite.DocumentoDeterminacionBean documento =
					new com.gestorfip.app.planeamiento.beans.tramite.DocumentoDeterminacionBean();
				
				if(str.getLstDocumetos()[i] != null){
					
					documento.setId(str.getLstDocumetos()[i].getId());
					documento.setDocumentoid(str.getLstDocumetos()[i].getDocumentoid());
					documento.setDeterminacion(str.getLstDocumetos()[i].getDeterminacion());
					
					lstDocumentos[i] = documento;
				}
			}
			
			detBean.setLstDocumento(lstDocumentos);
		}
	}
	
	
	
	public static void cloneWSRegulacionesEspecificasBeanToRegulacionesEspecificasBean(
			com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean,
			DeterminacionBean str){
		
		if(str.getLstRegulacionesEspecificas() != null){
			com.gestorfip.app.planeamiento.beans.tramite.RegulacionesEspecificasBean[] lstRegulacionesEspecificas =
				new com.gestorfip.app.planeamiento.beans.tramite.RegulacionesEspecificasBean[str.getLstRegulacionesEspecificas().length];
			
			for(int i=0; i<str.getLstRegulacionesEspecificas().length; i++){
				
				com.gestorfip.app.planeamiento.beans.tramite.RegulacionesEspecificasBean regulacionEspecifica =
					new com.gestorfip.app.planeamiento.beans.tramite.RegulacionesEspecificasBean();
				
				if(str.getLstRegulacionesEspecificas()[i] != null){
					regulacionEspecifica.setId(str.getLstRegulacionesEspecificas()[i].getId());
					regulacionEspecifica.setOrden(str.getLstRegulacionesEspecificas()[i].getOrden());
					regulacionEspecifica.setNombre(str.getLstRegulacionesEspecificas()[i].getNombre());
					regulacionEspecifica.setTexto(str.getLstRegulacionesEspecificas()[i].getTexto());
					regulacionEspecifica.setMadre(str.getLstRegulacionesEspecificas()[i].getMadre());
					regulacionEspecifica.setDeterminacion(str.getLstRegulacionesEspecificas()[i].getDeterminacion());
				
					lstRegulacionesEspecificas[i] = regulacionEspecifica;
				}
			}
			
			detBean.setLstRegulacionesEspecificas(lstRegulacionesEspecificas);
		}
		
		
	}
	
	
	/*************************************************************************************
	 * 						ENTIDADES
	 *************************************************************************************/
	
	public static void cloneWSEntidadBeanToEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entidadBean,
			EntidadBean str){

		entidadBean.setId(str.getId());
		entidadBean.setCodigo(str.getCodigo());
		entidadBean.setClave(str.getClave());
		entidadBean.setNombre(str.getNombre());
		entidadBean.setEtiqueta(str.getEtiqueta());
		entidadBean.setSuspendida(str.getSuspendida());
		entidadBean.setBase_entidadid(str.getBase_entidadid());
		entidadBean.setMadre(str.getMadre());
		entidadBean.setTramite(str.getTramite());
		entidadBean.setIdLayer(str.getIdLayer());
		entidadBean.setIdFeature(str.getIdFeature());
		entidadBean.setCodigoValRefCU(str.getCodigoValRefCU());
		
		//entidadBean.setModificable(isModificable);
	
	}
	
	public static void cloneWSDocumentosEntidadBeanToDocumentosEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entiBean,
			EntidadBean str){
		
		if(str.getLstDocumetos()!= null){
			com.gestorfip.app.planeamiento.beans.tramite.DocumentoEntidadBean[] lstDocumentos =
				new com.gestorfip.app.planeamiento.beans.tramite.DocumentoEntidadBean[str.getLstDocumetos().length];
			
			for(int i=0; i<str.getLstDocumetos().length; i++){
				
				com.gestorfip.app.planeamiento.beans.tramite.DocumentoEntidadBean documento =
					new com.gestorfip.app.planeamiento.beans.tramite.DocumentoEntidadBean();
				
				if(str.getLstDocumetos()[i] != null){
					
					documento.setId(str.getLstDocumetos()[i].getId());
					documento.setDocumentoid(str.getLstDocumetos()[i].getDocumentoid());
					documento.setDeterminacion(str.getLstDocumetos()[i].getDeterminacion());
					
					lstDocumentos[i] = documento;
				}
			}
			
			entiBean.setLstDocumento(lstDocumentos);
		}
	}
	
	public static void cloneWSOperacionEntidadBeanToOperacionEntidadBean(
			com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entiBean,
			EntidadBean str){
		
		if(str.getLstOperacionEntidad() != null){
			
			com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean[] lstOperacionEntidad = null;

			
			for(int i=0; i<str.getLstOperacionEntidad().length; i++){
				if(str.getLstOperacionEntidad()[i] != null){
					com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean operacionEntidad =
						new com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean();
					
					operacionEntidad.setId(str.getLstOperacionEntidad()[i].getId());
					operacionEntidad.setTipo(str.getLstOperacionEntidad()[i].getTipo());
					operacionEntidad.setOrden(str.getLstOperacionEntidad()[i].getOrden());
					operacionEntidad.setTexto(str.getLstOperacionEntidad()[i].getTexto());
					operacionEntidad.setOperadora_entidadid(str.getLstOperacionEntidad()[i].getOperadora_entidadid());
					operacionEntidad.setTramite(str.getLstOperacionEntidad()[i].getTramite());
					
					com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean entidOperada = 
						new com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean();
						
					entidOperada.setId(str.getLstOperacionEntidad()[i].getOperada_entidad().getId());
					entidOperada.setCodigo(str.getLstOperacionEntidad()[i].getOperada_entidad().getCodigo());
					entidOperada.setNombre(str.getLstOperacionEntidad()[i].getOperada_entidad().getNombre());
					entidOperada.setEtiqueta(str.getLstOperacionEntidad()[i].getOperada_entidad().getEtiqueta());
					entidOperada.setSuspendida(str.getLstOperacionEntidad()[i].getOperada_entidad().getSuspendida());
					entidOperada.setMadre(str.getLstOperacionEntidad()[i].getOperada_entidad().getMadre());
					entidOperada.setTramite(str.getLstOperacionEntidad()[i].getOperada_entidad().getTramite());
	
					operacionEntidad.setOperada_entidad(entidOperada);
					
					if(lstOperacionEntidad == null){
						
						lstOperacionEntidad = new com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean[1];
						lstOperacionEntidad[0] = operacionEntidad;
					
					}
					else{
						lstOperacionEntidad = (com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean[]) Arrays.copyOf(
								lstOperacionEntidad, lstOperacionEntidad.length+1);
					
						lstOperacionEntidad[lstOperacionEntidad.length-1] = operacionEntidad;
					}	
				}
			}
			entiBean.setLstOperacionEntidad(lstOperacionEntidad);
		}
		
	}
	
	/*************************************************************************************
	 * 						FIN ENTIDADES
	 *************************************************************************************/
	
	/*************************************************************************************
	 * 						CONDICIONES URBANISTICAS
	 *************************************************************************************/

	public static void cloneWSCondicionUrbanisticaCasoRegimenRegimenesBeanToCondicionUrbanisticaCasoRegimenRegimenesBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean cucr,
			CondicionUrbanisticaCasoRegimenRegimenesBean str){

		cucr.setId(str.getId());
		cucr.setOrden(str.getOrden());
		cucr.setNombre(str.getNombre());
		cucr.setTexto(str.getTexto());
		cucr.setPadre(str.getPadre());
		cucr.setRegimen(str.getRegimen());

	}
	
	public static void cloneWSCondicionUrbanisticaCasoRegimenesBeanToCondicionUrbanisticaCasoRegimenesBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean cucr,
			CondicionUrbanisticaCasoRegimenesBean str){

		cucr.setId(str.getId());
		cucr.setComentario(str.getComentario());
		cucr.setValor(str.getValor());
		cucr.setSuperposicion(str.getSuperposicion());
		cucr.setValorreferencia_determinacionid(str.getValorreferencia_determinacionid());
		cucr.setDeterminacionregimen_determinacionid(str.getDeterminacionregimen_determinacionid());
		cucr.setCasoaplicacion_casoid(str.getCasoaplicacion_casoid());
		cucr.setCaso(str.getCaso());
		
//		//se utiliza para que se muestre en la tabla de regimenes
//		// ya que ninguno de sus campos es obligatorio
//		cucr.setNombre("Regï¿½men_"+str.getId());

	}
	
	public static void cloneWSCondicionUrbanisticaCasoBeanToCondicionUrbanisticaCasoBean(
			com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean cuc,
			CondicionUrbanisticaCasoBean str){

		cuc.setId(str.getId());
		cuc.setCodigo(str.getCodigo());
		cuc.setCondicionurbanistica(str.getCondicionurbanistica());
		cuc.setNombre(str.getNombre());

	}
	
	
	/*************************************************************************************
	 * 						FIN CONDICIONES URBANISTICAS
	 *************************************************************************************/
	
}
