/**
 * OperacionesDeterminaciones.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionesReguladorasPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DocumentoPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DocumentosPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.GrupoAplicacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.RegulacionesEspecificasPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.ValoresReferenciaPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.DeterminacionReguladoraBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentoDeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentosBean;
import com.gestorfip.app.planeamiento.beans.tramite.GrupoAplicacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.OperacionDeterminacionBean;
import com.gestorfip.app.planeamiento.beans.tramite.RegulacionesEspecificasBean;
import com.gestorfip.app.planeamiento.beans.tramite.ValoresReferenciaBean;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class OperacionesDeterminaciones {
	
	
	private static DeterminacionBean[] lstDeterminacionesAlmacenaje = null;
	private static DeterminacionBean[] lstDeterminacionesAltaAlmacenaje = null;
	private static DeterminacionBean[] lstDeterminacionesBajaAlmacenaje = null;
	
	private static RegulacionesEspecificasBean[] lstReguEspecifModif = null;
	private static RegulacionesEspecificasBean[] lstReguEspecifAlta = null;
	private static RegulacionesEspecificasBean[] lstReguEspecifBaja = null;
	
	
	static DeterminacionBean[] lstDeterCatalogoSistematizado = null;
	static DeterminacionBean[] lstDeterPlaneamientoVigente = null; 

	public static HashMap hashDeterminacionesCS = new HashMap();
	public static HashMap hashDeterminacionesPV = new HashMap();
	public static HashMap hashDeterminacionesPE = new HashMap();
	
	
	public static FipPanelBean consultaDatosFip(int idFipSeleccionado, 
			ApplicationContext aplicacion){
		
		FipPanelBean fipPanel = new FipPanelBean();
		
		try {
			//fipPanel = ClientGestorFip.obtenerDatosFip(aplicacion, idFipSeleccionado);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fipPanel;
		
	}
	
	public static CaracteresDeterminacionPanelBean[] consultaCararteresDeterminaciones(int fip, 
			ApplicationContext aplicacion){
		
		CaracteresDeterminacionPanelBean[] lstCaracteresDeterminacionPanelBean = null;
		
		
		try {
			lstCaracteresDeterminacionPanelBean = ClientGestorFip.obtenerCararteresDeterminaciones(aplicacion, fip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lstCaracteresDeterminacionPanelBean;
		
	}
	

	/**
	 * @param fip
	 * @param aplicacion
	 * @param progressDialog
	 * @param isConsulta_CS_PV, false cuando no se quiere actualizar o consultar los arboles de CS y PV
	 * @return
	 */
	public static FipPanelBean consultaDeterminacionesAsociadasFip(FipPanelBean fip, 
						ApplicationContext aplicacion, TaskMonitorDialog progressDialog, boolean isActualizar_CS_PV){
		
		try {
			
			
			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado = null;
			if(fip.getTramiteCatalogoSistematizado() != null && isActualizar_CS_PV){
				lstDeterminacionesCatalogoSistematizado = ClientGestorFip.obtenerDeterminacionesAsociadasFip(aplicacion, 
												fip.getTramiteCatalogoSistematizado().getId(), false);
				lstDeterCatalogoSistematizado = lstDeterminacionesCatalogoSistematizado;
			}
			else{
				lstDeterminacionesCatalogoSistematizado = lstDeterCatalogoSistematizado;
			}
		
			if(progressDialog.isCancelRequested())
				return null;
			
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente = null; 
			if(fip.getTramitePlaneamientoVigente() != null && isActualizar_CS_PV){
				lstDeterminacionesPlaneamientoVigente = ClientGestorFip.obtenerDeterminacionesAsociadasFip(aplicacion, 
												fip.getTramitePlaneamientoVigente().getId(), false);
				lstDeterPlaneamientoVigente = lstDeterminacionesPlaneamientoVigente;
			}
			else{
				
				lstDeterminacionesPlaneamientoVigente = lstDeterPlaneamientoVigente;
			}
			
			if(progressDialog.isCancelRequested())
				return null;
			DeterminacionBean[] lstDeterminacionesPlaneamientoEncargado = null;
			if(fip.getTramitePlaneamientoEncargado() != null){
				lstDeterminacionesPlaneamientoEncargado = ClientGestorFip.obtenerDeterminacionesAsociadasFip(aplicacion, 
												fip.getTramitePlaneamientoEncargado().getId(), true);
			
			}
			if(progressDialog.isCancelRequested())
				return null;
			
			
			gestionArbolDeterminaciones(lstDeterminacionesCatalogoSistematizado,
												lstDeterminacionesPlaneamientoVigente, 
												lstDeterminacionesPlaneamientoEncargado
												, fip, isActualizar_CS_PV);			
			return fip;
			
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * @param fip
	 * @param aplicacion
	 * @param progressDialog
	 * @param isConsulta_CS_PV, false cuando no se quiere actualizar o consultar los arboles de CS y PV
	 * @return
	 */
	public static FipPanelBean consultaArbolDeterminacionesAsociadasFip(FipPanelBean fip, 
						ApplicationContext aplicacion, TaskMonitorDialog progressDialog){
		
		try {
						
			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado = null;
			if(fip.getTramiteCatalogoSistematizado() != null){
				lstDeterminacionesCatalogoSistematizado = ClientGestorFip.obtenerArbolDeterminacionesAsociadasTramite(aplicacion, 
												fip.getTramiteCatalogoSistematizado().getId(), false);
				lstDeterCatalogoSistematizado = lstDeterminacionesCatalogoSistematizado;
			}
			else{
				lstDeterminacionesCatalogoSistematizado = lstDeterCatalogoSistematizado;
			}
		
			if(progressDialog.isCancelRequested())
				return null;
			
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente = null; 
			if(fip.getTramitePlaneamientoVigente() != null){
				lstDeterminacionesPlaneamientoVigente = ClientGestorFip.obtenerArbolDeterminacionesAsociadasTramite(aplicacion, 
												fip.getTramitePlaneamientoVigente().getId(), false);
				lstDeterPlaneamientoVigente = lstDeterminacionesPlaneamientoVigente;
			}
			else{
				
				lstDeterminacionesPlaneamientoVigente = lstDeterPlaneamientoVigente;
			}
			
			if(progressDialog.isCancelRequested())
				return null;
			DeterminacionBean[] lstDeterminacionesPlaneamientoEncargado = null;
			if(fip.getTramitePlaneamientoEncargado() != null){
				lstDeterminacionesPlaneamientoEncargado = ClientGestorFip.obtenerArbolDeterminacionesAsociadasTramite(aplicacion, 
												fip.getTramitePlaneamientoEncargado().getId(), true);
			
			}
			if(progressDialog.isCancelRequested())
				return null;
			
			
			gestionArbolDet(lstDeterminacionesCatalogoSistematizado,
												lstDeterminacionesPlaneamientoVigente, 
												lstDeterminacionesPlaneamientoEncargado, fip);			
			
			
			if(lstDeterminacionesCatalogoSistematizado != null){
				for(int i=0; i <lstDeterminacionesCatalogoSistematizado.length; i++){
					if(lstDeterminacionesCatalogoSistematizado[i] != null){
						hashDeterminacionesCS.put(lstDeterminacionesCatalogoSistematizado[i].getId(),lstDeterminacionesCatalogoSistematizado[i]);	
					}
				}
			}
			
			if(lstDeterminacionesPlaneamientoVigente != null){
				for(int i=0; i <lstDeterminacionesPlaneamientoVigente.length; i++){
					if(lstDeterminacionesPlaneamientoVigente[i] != null){
						hashDeterminacionesPV.put(lstDeterminacionesPlaneamientoVigente[i].getId(),lstDeterminacionesPlaneamientoVigente[i]);	
					}
				}
			}
			
			if(lstDeterminacionesPlaneamientoEncargado != null){
				for(int i=0; i <lstDeterminacionesPlaneamientoEncargado.length; i++){
					if(lstDeterminacionesPlaneamientoEncargado[i] != null){
						hashDeterminacionesPE.put(lstDeterminacionesPlaneamientoEncargado[i].getId(),lstDeterminacionesPlaneamientoEncargado[i]);	
					}
				}
			}

			return fip;
			
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
	
//	public static FIP1LayersBean obtenerDeterminaciones_XML_CS(String xml, String encoding,
//			ApplicationContext aplicacion, TaskMonitorDialog progressDialog) throws Exception{
////		FipPanelBean fip = new FipPanelBean();
//		return ClientGestorFip.obtenerDeterminaciones_XML_CS(xml, encoding, aplicacion);
//	
//		//gestionArbolDeterminaciones_GestorFip(lstDetXMLCS, null, null,fip);
//
//		//return fip1;
//	}
	
	
	/**
	 * @param fip
	 * @param aplicacion
	 * @param progressDialog
	 * @param isConsulta_CS_PV, false cuando no se quiere actualizar o consultar los arboles de CS y PV
	 * @return
	 */
	public static FipPanelBean consultaDeterminacionesAsociadasFip_GestorPlaneamiento(FipPanelBean fip, 
						ApplicationContext aplicacion, TaskMonitorDialog progressDialog){
		
		try {

			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado = null;
			lstDeterminacionesCatalogoSistematizado = 
					ClientGestorFip.obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(aplicacion, fip.getId(),
											fip.getTramiteCatalogoSistematizado().getId());
			lstDeterCatalogoSistematizado = lstDeterminacionesCatalogoSistematizado;

			if(progressDialog.isCancelRequested())
				return null;
			
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente = null; 
			lstDeterminacionesPlaneamientoVigente = 
						ClientGestorFip.obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(aplicacion, 
												fip.getId(), fip.getTramitePlaneamientoVigente().getId());
				lstDeterPlaneamientoVigente = lstDeterminacionesPlaneamientoVigente;

			if(progressDialog.isCancelRequested())
				return null;
				
			gestionArbolDeterminaciones_GestorFip(lstDeterminacionesCatalogoSistematizado,
												lstDeterminacionesPlaneamientoVigente, 
												null
												, fip);

			return fip;
			
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
	
	
	
	private static DeterminacionPanelBean[] cloneDeterminacionBeanToDeterminacionPanelBean(DeterminacionBean[] lstDeterminaciones){
		
		DeterminacionPanelBean[] lstDeterminacionesPanel = 
					new DeterminacionPanelBean[lstDeterminaciones.length];
		
		for(int i=0; i<lstDeterminaciones.length; i++){
			if(lstDeterminaciones[i] != null){
				lstDeterminacionesPanel[i] = new DeterminacionPanelBean();
				
				lstDeterminacionesPanel[i].setId(lstDeterminaciones[i].getId());
				lstDeterminacionesPanel[i].setCodigo(lstDeterminaciones[i].getCodigo());
				lstDeterminacionesPanel[i].setCaracter(lstDeterminaciones[i].getCaracter());
				lstDeterminacionesPanel[i].setApartado(lstDeterminaciones[i].getApartado());
				lstDeterminacionesPanel[i].setNombre(lstDeterminaciones[i].getNombre());
				lstDeterminacionesPanel[i].setEtiqueta(lstDeterminaciones[i].getEtiqueta());
				lstDeterminacionesPanel[i].setSuspendida(lstDeterminaciones[i].isSuspendida());
				lstDeterminacionesPanel[i].setTexto(lstDeterminaciones[i].getTexto());
				lstDeterminacionesPanel[i].setUnidad_determinacionid(lstDeterminaciones[i].getUnidad_determinacionid());
				lstDeterminacionesPanel[i].setBase_determinacionid(lstDeterminaciones[i].getBase_determinacionid());
				lstDeterminacionesPanel[i].setMadre(lstDeterminaciones[i].getMadre());
				lstDeterminacionesPanel[i].setTramite(lstDeterminaciones[i].getTramite());
				
				lstDeterminacionesPanel[i].setModificable(lstDeterminaciones[i].isModificable());
				
			}
		}
		
		return  lstDeterminacionesPanel;
	}
	
	
	private static void buscarDeterminacionArbol(DeterminacionPanelBean[] lstDeterHijas,
			DeterminacionPanelBean deter){
		
		if(lstDeterHijas != null){
			for(int i=0; i<lstDeterHijas.length; i++){
				if(lstDeterHijas[i].getId() == deter.getMadre()){
					if(lstDeterHijas[i].getLstDeterminacionesHijas() == null){
						lstDeterHijas[i].setLstDeterminacionesHijas(new DeterminacionPanelBean[1]);
						lstDeterHijas[i].getLstDeterminacionesHijas()[0] =	deter;
					}
					else{
						lstDeterHijas[i].setLstDeterminacionesHijas( (DeterminacionPanelBean[]) 
							Arrays.copyOf(lstDeterHijas[i].getLstDeterminacionesHijas(),	lstDeterHijas[i].getLstDeterminacionesHijas().length+1));
						
						
						lstDeterHijas[i].getLstDeterminacionesHijas()[lstDeterHijas[i].getLstDeterminacionesHijas().length-1] =	deter;
					}
				}
				else{
					buscarDeterminacionArbol(
							lstDeterHijas[i].getLstDeterminacionesHijas(),deter);
				}
				
			}
		}
		
	}
	
	
	private static DeterminacionPanelBean[] gestionArbolDeterminacionesTramite(DeterminacionBean[] lstDeterminaciones, 
								DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncarcagado,
								DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado,
								DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente){
			
		DeterminacionPanelBean[] lstDeterminacionesPanel = null;
		HashMap hmDeterminaciones = new HashMap();
		HashMap hmDeterminacionesPlaneamientoEncargado = new HashMap();
		HashMap hmDeterminacionesCatalogoSistematizado = new HashMap();
		HashMap hmDeterminacionesPlaneamientoVigente = new HashMap();
		
		ArrayList arbolDeterminaciones = new ArrayList();
		DeterminacionPanelBean[] lstArbolDeterminaciones = null;
		
		if(lstDeterminacionesPanelPlaneamientoEncarcagado != null){
			for(int i=0; i<lstDeterminacionesPanelPlaneamientoEncarcagado.length; i++){
				if(lstDeterminacionesPanelPlaneamientoEncarcagado[i] != null){
					hmDeterminacionesPlaneamientoEncargado.put(lstDeterminacionesPanelPlaneamientoEncarcagado[i].getId(), lstDeterminacionesPanelPlaneamientoEncarcagado[i]);
				}
			}
		}
		
		if(lstDeterminacionesPanelCatalogoSistematizado != null){
			for(int i=0; i<lstDeterminacionesPanelCatalogoSistematizado.length; i++){
				if(lstDeterminacionesPanelCatalogoSistematizado[i] != null){
					hmDeterminacionesCatalogoSistematizado.put(lstDeterminacionesPanelCatalogoSistematizado[i].getId(),
							lstDeterminacionesPanelCatalogoSistematizado[i]);
				}
			}
		}
	
		if(lstDeterminacionesPanelPlaneamientoVigente != null){
			for(int i=0; i<lstDeterminacionesPanelPlaneamientoVigente.length; i++){
				if(lstDeterminacionesPanelPlaneamientoVigente[i] != null){
					hmDeterminacionesPlaneamientoVigente.put(lstDeterminacionesPanelPlaneamientoVigente[i].getId(), 
																lstDeterminacionesPanelPlaneamientoVigente[i]);
				}
			}
		}
		
		
		if( lstDeterminacionesPanelPlaneamientoEncarcagado  == null){
			
			if(lstDeterminacionesPanelPlaneamientoVigente != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoVigente;
				hmDeterminaciones = hmDeterminacionesPlaneamientoVigente;
			}
			else if(lstDeterminacionesPanelCatalogoSistematizado != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelCatalogoSistematizado;
				hmDeterminaciones = hmDeterminacionesCatalogoSistematizado;
			}
			
		}
		else{
			lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoEncarcagado;
			hmDeterminaciones = hmDeterminacionesPlaneamientoEncargado;
		}
		
		for(int j=0; j<lstDeterminacionesPanel.length; j++){
			if(lstDeterminacionesPanel[j] != null){
				if(lstDeterminacionesPanel[j].getMadre()== 0){
					// es una determinacion que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
					arbolDeterminaciones.add(lstDeterminacionesPanel[j]);
	
				}
				else{
					//es una determinacion hija de una determinacion 
					((DeterminacionPanelBean)hmDeterminaciones.get(lstDeterminacionesPanel[j].getMadre()))
								.addDeterminacionHija((DeterminacionPanelBean)lstDeterminacionesPanel[j]);
				}
			
			
				// Gestion de las unidad y la Base
				
				gestionUnidadesYBase(lstDeterminaciones[j], hmDeterminaciones, hmDeterminacionesCatalogoSistematizado, 
						hmDeterminacionesPlaneamientoVigente,hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
				
				//gestion valores-referencia
//				gestionValoresReferencia(lstDeterminaciones[j], hmDeterminaciones, lstDeterminacionesPanel[j]);
				gestionValoresReferencia(lstDeterminaciones[j], hmDeterminacionesCatalogoSistematizado, 
						hmDeterminacionesPlaneamientoVigente,hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
				
				
				//gestion Grupos-Aplicacion
				gestionGruposAplicacion(lstDeterminaciones[j], hmDeterminacionesCatalogoSistematizado, hmDeterminacionesPlaneamientoVigente,
										hmDeterminacionesPlaneamientoEncargado , lstDeterminacionesPanel[j]);
				
				//gestion Determinaciones-Reguladoras
				
				gestionDeterminacionesReguladoras(lstDeterminaciones[j], hmDeterminacionesCatalogoSistematizado, hmDeterminacionesPlaneamientoVigente,
						hmDeterminacionesPlaneamientoEncargado , lstDeterminacionesPanel[j]);
//				gestionDeterminacionesReguladoras(lstDeterminaciones[j], hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
				
				//gestion Regulaciones Especificas
				gestionRegulacionesEspecificas(lstDeterminaciones[j].getLstRegulacionesEspecificas(), lstDeterminacionesPanel[j]);
//				gestionRegulacionesEspecificas(lstDeterminaciones[j].getLstRegulacionesEspecificas(), lstDeterminacionesPanel[j]);
				
				//gestion Documentos
				//gestionDocumentos(lstDeterminaciones[j].getLstDocumento(), lstDeterminacionesPanel[j], lstDocumentosAsocTramite);
				
				// gestion Operacion
//				gestionOperacion(lstDeterminaciones[j].getLstOperacionDeterminacion(), lstDeterminacionesPanel[j]);
				gestionOperacion(lstDeterminaciones[j].getLstOperacionDeterminacion(), hmDeterminacionesCatalogoSistematizado, hmDeterminacionesPlaneamientoVigente,
						hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
			}
		}		
		lstArbolDeterminaciones = new DeterminacionPanelBean[arbolDeterminaciones.size()];
		for(int h=0; h<arbolDeterminaciones.size(); h++){
			lstArbolDeterminaciones[h] = (DeterminacionPanelBean)arbolDeterminaciones.get(h);
		}
		
		return lstArbolDeterminaciones;
	}
	
	
	private static DeterminacionPanelBean[] gestionArbolDetTramite(DeterminacionBean[] lstDeterminaciones, 
			DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncarcagado,
			DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado,
			DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente){

			DeterminacionPanelBean[] lstDeterminacionesPanel = null;
			HashMap hmDeterminaciones = new HashMap();
			HashMap hmDeterminacionesPlaneamientoEncargado = new HashMap();
			HashMap hmDeterminacionesCatalogoSistematizado = new HashMap();
			HashMap hmDeterminacionesPlaneamientoVigente = new HashMap();
			
			ArrayList arbolDeterminaciones = new ArrayList();
			DeterminacionPanelBean[] lstArbolDeterminaciones = null;
			
			if(lstDeterminacionesPanelPlaneamientoEncarcagado != null){
				for(int i=0; i<lstDeterminacionesPanelPlaneamientoEncarcagado.length; i++){
					if(lstDeterminacionesPanelPlaneamientoEncarcagado[i] != null){
						hmDeterminacionesPlaneamientoEncargado.put(lstDeterminacionesPanelPlaneamientoEncarcagado[i].getId(), lstDeterminacionesPanelPlaneamientoEncarcagado[i]);
					}
				}
			}
			
			if(lstDeterminacionesPanelCatalogoSistematizado != null){
				for(int i=0; i<lstDeterminacionesPanelCatalogoSistematizado.length; i++){
					if(lstDeterminacionesPanelCatalogoSistematizado[i] != null){
						hmDeterminacionesCatalogoSistematizado.put(lstDeterminacionesPanelCatalogoSistematizado[i].getId(),
					lstDeterminacionesPanelCatalogoSistematizado[i]);
					}
				}
			}
			
			if(lstDeterminacionesPanelPlaneamientoVigente != null){
				for(int i=0; i<lstDeterminacionesPanelPlaneamientoVigente.length; i++){
					if(lstDeterminacionesPanelPlaneamientoVigente[i] != null){
						hmDeterminacionesPlaneamientoVigente.put(lstDeterminacionesPanelPlaneamientoVigente[i].getId(), 
														lstDeterminacionesPanelPlaneamientoVigente[i]);
					}
				}
			}
			
			
			if( lstDeterminacionesPanelPlaneamientoEncarcagado  == null){
			
			if(lstDeterminacionesPanelPlaneamientoVigente != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoVigente;
				hmDeterminaciones = hmDeterminacionesPlaneamientoVigente;
			}
			else if(lstDeterminacionesPanelCatalogoSistematizado != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelCatalogoSistematizado;
				hmDeterminaciones = hmDeterminacionesCatalogoSistematizado;
			}
			
			}
			else{
				lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoEncarcagado;
				hmDeterminaciones = hmDeterminacionesPlaneamientoEncargado;
			}
			
			for(int j=0; j<lstDeterminacionesPanel.length; j++){
				if(lstDeterminacionesPanel[j] != null){
					if(lstDeterminacionesPanel[j].getMadre()== 0){
					// es una determinacion que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
						arbolDeterminaciones.add(lstDeterminacionesPanel[j]);
			
					}
					else{
					//es una determinacion hija de una determinacion 
					
					((DeterminacionPanelBean)hmDeterminaciones.get(lstDeterminacionesPanel[j].getMadre()))
								.addDeterminacionHija((DeterminacionPanelBean)lstDeterminacionesPanel[j]);
					}
			

				}
			}		
			lstArbolDeterminaciones = new DeterminacionPanelBean[arbolDeterminaciones.size()];
			for(int h=0; h<arbolDeterminaciones.size(); h++){
				lstArbolDeterminaciones[h] = (DeterminacionPanelBean)arbolDeterminaciones.get(h);
			}
			
		return lstArbolDeterminaciones;
		}
	
	
	private static DeterminacionPanelBean[] gestionArbolDeterminacionesTramite_CondicionesUrbanisticas(DeterminacionBean[] lstDeterminaciones, 
			DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncarcagado,
			DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado,
			DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente){

		DeterminacionPanelBean[] lstDeterminacionesPanel = null;
		HashMap hmDeterminaciones = new HashMap();
		HashMap hmDeterminacionesPlaneamientoEncargado = new HashMap();
		HashMap hmDeterminacionesCatalogoSistematizado = new HashMap();
		HashMap hmDeterminacionesPlaneamientoVigente = new HashMap();
			
		ArrayList arbolDeterminaciones = new ArrayList();
		DeterminacionPanelBean[] lstArbolDeterminaciones = null;
		
		
		if(lstDeterminacionesPanelPlaneamientoEncarcagado != null){
			for(int i=0; i<lstDeterminacionesPanelPlaneamientoEncarcagado.length; i++){
				if(lstDeterminacionesPanelPlaneamientoEncarcagado[i] != null){
					hmDeterminacionesPlaneamientoEncargado.put(lstDeterminacionesPanelPlaneamientoEncarcagado[i].getId(), lstDeterminacionesPanelPlaneamientoEncarcagado[i]);
				}
			}
		}
			
			
		if(lstDeterminacionesPanelCatalogoSistematizado != null){
			for(int i=0; i<lstDeterminacionesPanelCatalogoSistematizado.length; i++){
				if(lstDeterminacionesPanelCatalogoSistematizado[i] != null){
					hmDeterminacionesCatalogoSistematizado.put(lstDeterminacionesPanelCatalogoSistematizado[i].getId(),
							lstDeterminacionesPanelCatalogoSistematizado[i]);
				}
			}
		}
			
			
		if(lstDeterminacionesPanelPlaneamientoVigente != null){
			for(int i=0; i<lstDeterminacionesPanelPlaneamientoVigente.length; i++){
				if(lstDeterminacionesPanelPlaneamientoVigente[i] != null){
					hmDeterminacionesPlaneamientoVigente.put(lstDeterminacionesPanelPlaneamientoVigente[i].getId(), 
															lstDeterminacionesPanelPlaneamientoVigente[i]);
				}
			}
		}
			
		if( lstDeterminacionesPanelPlaneamientoEncarcagado  == null){
			if(lstDeterminacionesPanelPlaneamientoVigente != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoVigente;
				hmDeterminaciones = hmDeterminacionesPlaneamientoVigente;
			}
			else if(lstDeterminacionesPanelCatalogoSistematizado != null){
				lstDeterminacionesPanel = lstDeterminacionesPanelCatalogoSistematizado;
				hmDeterminaciones = hmDeterminacionesCatalogoSistematizado;
			}
		}
		else{
			lstDeterminacionesPanel = lstDeterminacionesPanelPlaneamientoEncarcagado;
			hmDeterminaciones = hmDeterminacionesPlaneamientoEncargado;
		}
			
			
		for(int j=0; j<lstDeterminacionesPanel.length; j++){
			if(lstDeterminacionesPanel[j] != null){
				if(lstDeterminacionesPanel[j].getMadre()== 0){
					// es una determinacion que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
					arbolDeterminaciones.add(lstDeterminacionesPanel[j]);
			
				}
				else{
					//es una determinacion hija de una determinacion 
			
					((DeterminacionPanelBean)hmDeterminaciones.get(lstDeterminacionesPanel[j].getMadre()))
						.addDeterminacionHija((DeterminacionPanelBean)lstDeterminacionesPanel[j]);
				}
				
// 				Gestion de las unidad y la Base
				
				gestionUnidadesYBase(lstDeterminaciones[j], hmDeterminaciones, hmDeterminacionesCatalogoSistematizado, 
						hmDeterminacionesPlaneamientoVigente,hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
				
				//gestion valores-referencia
				gestionValoresReferencia(lstDeterminaciones[j], hmDeterminacionesCatalogoSistematizado, 
						hmDeterminacionesPlaneamientoVigente,hmDeterminacionesPlaneamientoEncargado, lstDeterminacionesPanel[j]);
				
				
				//gestion Grupos-Aplicacion
				gestionGruposAplicacion(lstDeterminaciones[j], hmDeterminacionesCatalogoSistematizado, hmDeterminacionesPlaneamientoVigente,
										hmDeterminacionesPlaneamientoEncargado , lstDeterminacionesPanel[j]);
				
				//gestion Regulaciones Especificas
				gestionRegulacionesEspecificas(lstDeterminaciones[j].getLstRegulacionesEspecificas(), lstDeterminacionesPanel[j]);
			}
		}
			
		lstArbolDeterminaciones = new DeterminacionPanelBean[arbolDeterminaciones.size()];
		for(int h=0; h<arbolDeterminaciones.size(); h++){
			lstArbolDeterminaciones[h] = (DeterminacionPanelBean)arbolDeterminaciones.get(h);
		}
		
		return lstArbolDeterminaciones;
	}
	
	
	
	
	
	/**
	 * GEnera el arbol de determinaciones (padres/hijos)  de las determinaciones asociadas
	 * a un tramite
	 * @param lstDeterminaciones
	 * @param tramite
	 * @param lstDocumentosAsocTramite
	 * @return
	 */
	private static void gestionArbolDeterminaciones(
			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado,
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente,
			DeterminacionBean[] lstDeterminacionesPlaneamientoEncargado,
				FipPanelBean fip, boolean isActualizar_CS_PV){
		

		DeterminacionPanelBean[] lstArbolDeterminacionesCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoEncargado = null;
		
		DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncargado = null;
		
		if(lstDeterminacionesCatalogoSistematizado != null ){
			 lstDeterminacionesPanelCatalogoSistematizado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesCatalogoSistematizado);
			 
			 if(isActualizar_CS_PV){
				 lstArbolDeterminacionesCatalogoSistematizado = 
					gestionArbolDeterminacionesTramite(lstDeterminacionesCatalogoSistematizado,
														null, lstDeterminacionesPanelCatalogoSistematizado, null);
				 fip.setLstArbolDeterminacionesCatalogoSistematizado(lstArbolDeterminacionesCatalogoSistematizado);
			 }
			
		}
	
		if(lstDeterminacionesPlaneamientoVigente != null){
			 lstDeterminacionesPanelPlaneamientoVigente = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoVigente);
			 if(isActualizar_CS_PV){
				 lstArbolDeterminacionesPlaneamientoVigente = 
						gestionArbolDeterminacionesTramite(lstDeterminacionesPlaneamientoVigente,
														null, lstDeterminacionesPanelCatalogoSistematizado, lstDeterminacionesPanelPlaneamientoVigente);
				 fip.setLstArbolDeterminacionesPlaneamientoVigente(lstArbolDeterminacionesPlaneamientoVigente);
			 }
			
			 
		}
	
		
		if(lstDeterminacionesPlaneamientoEncargado != null){
			lstDeterminacionesPanelPlaneamientoEncargado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoEncargado);
			
			lstArbolDeterminacionesPlaneamientoEncargado =
				gestionArbolDeterminacionesTramite(lstDeterminacionesPlaneamientoEncargado,
													lstDeterminacionesPanelPlaneamientoEncargado,
													lstDeterminacionesPanelCatalogoSistematizado,
													lstDeterminacionesPanelPlaneamientoVigente);
			
			fip.setLstArbolDeterminacionesPlaneamientoEncargado(lstArbolDeterminacionesPlaneamientoEncargado);
		}
	
	}
	
	private static void gestionArbolDet(
			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado,
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente,
			DeterminacionBean[] lstDeterminacionesPlaneamientoEncargado,
				FipPanelBean fip){
		

		DeterminacionPanelBean[] lstArbolDeterminacionesCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoEncargado = null;
		
		DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncargado = null;
		
		if(lstDeterminacionesCatalogoSistematizado != null ){
			 lstDeterminacionesPanelCatalogoSistematizado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesCatalogoSistematizado);

				 lstArbolDeterminacionesCatalogoSistematizado = 
				gestionArbolDetTramite(lstDeterminacionesCatalogoSistematizado,
													null, lstDeterminacionesPanelCatalogoSistematizado, null);
			 fip.setLstArbolDeterminacionesCatalogoSistematizado(lstArbolDeterminacionesCatalogoSistematizado);
			
		}
	
		if(lstDeterminacionesPlaneamientoVigente != null){
			 lstDeterminacionesPanelPlaneamientoVigente = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoVigente);

			 lstArbolDeterminacionesPlaneamientoVigente = 
					gestionArbolDetTramite(lstDeterminacionesPlaneamientoVigente,
													null, lstDeterminacionesPanelCatalogoSistematizado, lstDeterminacionesPanelPlaneamientoVigente);
			 fip.setLstArbolDeterminacionesPlaneamientoVigente(lstArbolDeterminacionesPlaneamientoVigente);
		
		}
	
		
		if(lstDeterminacionesPlaneamientoEncargado != null){
			lstDeterminacionesPanelPlaneamientoEncargado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoEncargado);
			
			lstArbolDeterminacionesPlaneamientoEncargado =
				gestionArbolDetTramite(lstDeterminacionesPlaneamientoEncargado,
													lstDeterminacionesPanelPlaneamientoEncargado,
													lstDeterminacionesPanelCatalogoSistematizado,
													lstDeterminacionesPanelPlaneamientoVigente);
			
			fip.setLstArbolDeterminacionesPlaneamientoEncargado(lstArbolDeterminacionesPlaneamientoEncargado);
		}
	
	}
	
	
	/**
	 * GEnera el arbol de determinaciones (padres/hijos)  de las determinaciones asociadas
	 * a un tramite
	 * @param lstDeterminaciones
	 * @param tramite
	 * @param lstDocumentosAsocTramite
	 * @return
	 */
	private static void gestionArbolDeterminaciones_GestorFip(
			DeterminacionBean[] lstDeterminacionesCatalogoSistematizado,
			DeterminacionBean[] lstDeterminacionesPlaneamientoVigente,
			DeterminacionBean[] lstDeterminacionesPlaneamientoEncargado,
				FipPanelBean fip){
		

		DeterminacionPanelBean[] lstArbolDeterminacionesCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstArbolDeterminacionesPlaneamientoEncargado = null;
		
		DeterminacionPanelBean[] lstDeterminacionesPanelCatalogoSistematizado = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoVigente = null;
		DeterminacionPanelBean[] lstDeterminacionesPanelPlaneamientoEncargado = null;
		
		if(lstDeterminacionesCatalogoSistematizado != null ){
			 lstDeterminacionesPanelCatalogoSistematizado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesCatalogoSistematizado);
			 
			 lstArbolDeterminacionesCatalogoSistematizado = 
				gestionArbolDeterminacionesTramite_CondicionesUrbanisticas(lstDeterminacionesCatalogoSistematizado,
													null, lstDeterminacionesPanelCatalogoSistematizado, null);
			 fip.setLstArbolDeterminacionesCatalogoSistematizado(lstArbolDeterminacionesCatalogoSistematizado);
		}
		
		if(lstDeterminacionesPlaneamientoVigente != null){
			 lstDeterminacionesPanelPlaneamientoVigente = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoVigente);
				 lstArbolDeterminacionesPlaneamientoVigente = 
						gestionArbolDeterminacionesTramite_CondicionesUrbanisticas(lstDeterminacionesPlaneamientoVigente,
														null, lstDeterminacionesPanelCatalogoSistematizado, lstDeterminacionesPanelPlaneamientoVigente);
				 fip.setLstArbolDeterminacionesPlaneamientoVigente(lstArbolDeterminacionesPlaneamientoVigente);	 
		}

		if(lstDeterminacionesPlaneamientoEncargado != null){
			lstDeterminacionesPanelPlaneamientoEncargado = cloneDeterminacionBeanToDeterminacionPanelBean(lstDeterminacionesPlaneamientoEncargado);
			
			lstArbolDeterminacionesPlaneamientoEncargado =
				gestionArbolDeterminacionesTramite_CondicionesUrbanisticas(lstDeterminacionesPlaneamientoEncargado,
													lstDeterminacionesPanelPlaneamientoEncargado,
													lstDeterminacionesPanelCatalogoSistematizado,
													lstDeterminacionesPanelPlaneamientoVigente);
			
			fip.setLstArbolDeterminacionesPlaneamientoEncargado(lstArbolDeterminacionesPlaneamientoEncargado);
		}
	
	}
	
	public static void guardarDatosRegulacionesEspecificas(
			RegulacionesEspecificasPanelBean[] lstregulacionEspecificaPanel){

		lstReguEspecifModif = null;
		lstReguEspecifAlta = null;
		lstReguEspecifBaja = null;
	
		cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanModificadas(lstregulacionEspecificaPanel);
		cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanAlta(lstregulacionEspecificaPanel);
		cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanBaja(lstregulacionEspecificaPanel);
	
	}
	
	
	
	private static RegulacionesEspecificasBean cloneRegulacionesEspecificas(RegulacionesEspecificasPanelBean regulaEspecifPanel){
	
		RegulacionesEspecificasBean regulaEspecifBean = new RegulacionesEspecificasBean();
			
		regulaEspecifBean.setId(regulaEspecifPanel.getId());
		regulaEspecifBean.setOrden(regulaEspecifPanel.getOrden());
		regulaEspecifBean.setNombre(regulaEspecifPanel.getNombre());
		regulaEspecifBean.setTexto(regulaEspecifPanel.getTexto());
		regulaEspecifBean.setMadre(regulaEspecifPanel.getMadre());
		regulaEspecifBean.setDeterminacion(regulaEspecifPanel.getDeterminacion());
		
		return regulaEspecifBean;
	
	}
	private static RegulacionesEspecificasBean[]
	                           cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanBaja(RegulacionesEspecificasPanelBean[] lstregulacionEspecificaPanel){

		for(int i=0; i< lstregulacionEspecificaPanel.length; i++){
			
			RegulacionesEspecificasPanelBean regulaEspecif = lstregulacionEspecificaPanel[i];
			
			if(!regulaEspecif.isNueva() && regulaEspecif.isEliminada()	){
				if(lstReguEspecifBaja == null){
					lstReguEspecifBaja = new RegulacionesEspecificasBean[1];
					lstReguEspecifBaja[0] =	cloneRegulacionesEspecificas(regulaEspecif);
				}
				else{
					lstReguEspecifBaja = (RegulacionesEspecificasBean[]) 
								Arrays.copyOf(lstReguEspecifBaja,	lstReguEspecifBaja.length+1);
					lstReguEspecifBaja[lstReguEspecifBaja.length-1] = 	cloneRegulacionesEspecificas(regulaEspecif);
				}
				
			
			
			}
			if(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica() != null &&
					lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica().length != 0){
				
				cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanBaja(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica());
			}	
		}
		

		return lstReguEspecifBaja;
	}
	
	private static RegulacionesEspecificasBean[]
	                   cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanAlta(RegulacionesEspecificasPanelBean[] lstregulacionEspecificaPanel){

		for(int i=0; i< lstregulacionEspecificaPanel.length; i++){
			
			RegulacionesEspecificasPanelBean regulaEspecif = lstregulacionEspecificaPanel[i];
			
			if(regulaEspecif.isNueva() && !regulaEspecif.isEliminada()	){
				if(lstReguEspecifAlta == null){
					lstReguEspecifAlta = new RegulacionesEspecificasBean[1];
					lstReguEspecifAlta[0] =	cloneRegulacionesEspecificas(regulaEspecif);
				}
				else{
					lstReguEspecifAlta = (RegulacionesEspecificasBean[]) 
								Arrays.copyOf(lstReguEspecifAlta,	lstReguEspecifAlta.length+1);
					lstReguEspecifAlta[lstReguEspecifAlta.length-1] = 	cloneRegulacionesEspecificas(regulaEspecif);
				}
				
			
				
			}
			if(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica() != null &&
					lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica().length != 0){
				
				cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanAlta(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica());
			}	
		}
		

		return lstReguEspecifAlta;
	}
	
	private static RegulacionesEspecificasBean[]
	                   cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanModificadas(RegulacionesEspecificasPanelBean[] lstregulacionEspecificaPanel){

		for(int i=0; i< lstregulacionEspecificaPanel.length; i++){
			
			RegulacionesEspecificasPanelBean regulaEspecif = lstregulacionEspecificaPanel[i];
			
			if(regulaEspecif.isModificada() && !regulaEspecif.isEliminada()){
				if(lstReguEspecifModif == null){
					lstReguEspecifModif = new RegulacionesEspecificasBean[1];
					lstReguEspecifModif[0] =	cloneRegulacionesEspecificas(regulaEspecif);
				}
				else{
					lstReguEspecifModif = (RegulacionesEspecificasBean[]) 
								Arrays.copyOf(lstReguEspecifModif,	lstReguEspecifModif.length+1);
					lstReguEspecifModif[lstReguEspecifModif.length-1] = 	cloneRegulacionesEspecificas(regulaEspecif);
				}
				
			
				
			}
			if(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica() != null &&
					lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica().length != 0){
				
				cloneRegulacionesEspecificasPanelBeanToRegulacionesEspecificasBeanModificadas(lstregulacionEspecificaPanel[i].getLstRegulacionEspecifica());
			}	
		}
		

		return lstReguEspecifModif;
		
		
	}
		
	private static DocumentoPanelBean[] cloneDocumentosBeanToDocumentosPanelBean(
			DocumentoDeterminacionBean[] lstDocumentos,   DocumentosBean[] lstDocumentosAsocTramite){
		
		DocumentoPanelBean[] lstDocumentosDeterminacionesPanel = null;
		
		if(lstDocumentos != null){
			
			lstDocumentosDeterminacionesPanel = 
  				new DocumentoPanelBean[lstDocumentos.length];
			
			for(int i=0; i<lstDocumentos.length; i++){
				
				if(lstDocumentos[i] != null){
					lstDocumentosDeterminacionesPanel[i] = new DocumentoPanelBean();
					
					lstDocumentosDeterminacionesPanel[i].setId(lstDocumentos[i].getId());
					lstDocumentosDeterminacionesPanel[i].setDocumentoid(lstDocumentos[i].getDocumentoid());
					lstDocumentosDeterminacionesPanel[i].setDeterminacion(lstDocumentos[i].getDeterminacion());
					if(lstDocumentos[i] != null){
						
						if(lstDocumentosAsocTramite != null && lstDocumentosAsocTramite.length != 0){
							for(int j=0; j<lstDocumentosAsocTramite.length; j++){
								
								if(lstDocumentos[i].getDocumentoid() == lstDocumentosAsocTramite[j].getId()){
									lstDocumentosDeterminacionesPanel[i].setNombre(lstDocumentosAsocTramite[j].getNombre());
								}
							}
						}
					}
				}
			}
		}

		return lstDocumentosDeterminacionesPanel;
	}
	
	
	private static RegulacionesEspecificasPanelBean[] 
  	                  cloneRegulacionesEspecificasBeanToRegulacionesEspecificasPanelBean(
  	                		  RegulacionesEspecificasBean[] lstRegulacionesEspecificas){
  			
  		RegulacionesEspecificasPanelBean[] lstRegulacionesEspecificasPanel= null;

  		if(lstRegulacionesEspecificas != null){
  			
  			lstRegulacionesEspecificasPanel = 
  				new RegulacionesEspecificasPanelBean[lstRegulacionesEspecificas.length];
  	
  			for(int i=0; i<lstRegulacionesEspecificas.length; i++){
  				
  				if(lstRegulacionesEspecificas[i] != null){
  	
  					lstRegulacionesEspecificasPanel[i] = new RegulacionesEspecificasPanelBean();
  					
  					lstRegulacionesEspecificasPanel[i].setId(lstRegulacionesEspecificas[i].getId());
  					lstRegulacionesEspecificasPanel[i].setOrden(lstRegulacionesEspecificas[i].getOrden());
  					lstRegulacionesEspecificasPanel[i].setNombre(lstRegulacionesEspecificas[i].getNombre());
  					lstRegulacionesEspecificasPanel[i].setTexto(lstRegulacionesEspecificas[i].getTexto());
  					lstRegulacionesEspecificasPanel[i].setMadre(lstRegulacionesEspecificas[i].getMadre());
  					lstRegulacionesEspecificasPanel[i].setDeterminacion(lstRegulacionesEspecificas[i].getDeterminacion());
  				}
  			}
  		}

  		return  lstRegulacionesEspecificasPanel;
  		
  		
  	}
	
	public static void gestionDocumentos(DocumentoDeterminacionBean[] lstDocumentos,
			DeterminacionPanelBean determinacionesPanel,
			DocumentosBean[] lstDocumentosAsocTramite){
				
		if(lstDocumentos != null){
			
			DocumentoPanelBean[] lstDocumentosPanel = 
				cloneDocumentosBeanToDocumentosPanelBean(lstDocumentos, lstDocumentosAsocTramite);

			DocumentosPanelBean docPanelBean = new DocumentosPanelBean();
			docPanelBean.setLstDocumentoAsoc(lstDocumentosPanel);
			
			int[] lstIDAsoc = new int[lstDocumentosPanel.length];
			
			for(int j=0; j<lstDocumentosPanel.length; j++){
				
				lstIDAsoc[j] = lstDocumentosPanel[j].getId();
			}
			docPanelBean.setLstIDAsoc(lstIDAsoc);
			
			determinacionesPanel.setLstDocumentosPanelBean(docPanelBean);
			
		}
	}
	
	
	private static void gestionRegulacionesEspecificas(RegulacionesEspecificasBean[] lstRegulacionesEspecificas,
			DeterminacionPanelBean determinacionesPanel){
		
		RegulacionesEspecificasPanelBean[] lstRegulacionesEspecificasPanel = 
					cloneRegulacionesEspecificasBeanToRegulacionesEspecificasPanelBean(lstRegulacionesEspecificas);
		
		
		ArrayList arbolRegulacionesEspecificas = new ArrayList();
		HashMap hmRegulacionesEspecificas = new HashMap();
		RegulacionesEspecificasPanelBean[] lstArbolRegulacionesEspecificas = null;
		
		if(lstRegulacionesEspecificasPanel != null){
			
		
			for(int i=0; i<lstRegulacionesEspecificasPanel.length; i++){
				if(lstRegulacionesEspecificasPanel[i] != null){
					hmRegulacionesEspecificas.put(lstRegulacionesEspecificasPanel[i].getId(), lstRegulacionesEspecificasPanel[i]);
				}
			}
		
		
			for(int j=0; j<lstRegulacionesEspecificasPanel.length; j++){
	
				if(lstRegulacionesEspecificasPanel[j] != null){
					if(lstRegulacionesEspecificasPanel[j].getMadre()== 0){
						// es una RegulacionEspecifica que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
						arbolRegulacionesEspecificas.add(lstRegulacionesEspecificasPanel[j]);
		
					}
					else{
						//es una RegulacionEspecifica hija
					
						((RegulacionesEspecificasPanelBean)hmRegulacionesEspecificas.get(lstRegulacionesEspecificasPanel[j].getMadre()))
									.addRegulacionEspecificaHija((RegulacionesEspecificasPanelBean)lstRegulacionesEspecificasPanel[j]);
					}
				}
			}
		
			if(!arbolRegulacionesEspecificas.isEmpty()){
				lstArbolRegulacionesEspecificas  = new RegulacionesEspecificasPanelBean[arbolRegulacionesEspecificas.size()];
				for(int h=0; h<arbolRegulacionesEspecificas.size(); h++){
					lstArbolRegulacionesEspecificas[h] = (RegulacionesEspecificasPanelBean)arbolRegulacionesEspecificas.get(h);
					//determinacionesPanel.setRegulacionEspecificaPanelBean(
							//(RegulacionesEspecificasPanelBean)arbolRegulacionesEspecificas.get(h));
				}
				determinacionesPanel.setRegulacionEspecificaPanelBean(lstArbolRegulacionesEspecificas);
			}
			else{
				determinacionesPanel.setRegulacionEspecificaPanelBean(null);
			}
			
		}
		

		
	}
	
	private static void gestionUnidadesYBase(DeterminacionBean determinacion, HashMap hmDeterminaciones,
			HashMap hmDeterminacionesCatalogoSistematizado, HashMap hmDeterminacionesPlaneamientoVigente,
			HashMap hmDeterminacionesPlaneamientoEncargado, DeterminacionPanelBean lstDeterminacionesPanel){
	
		
		if(lstDeterminacionesPanel.getUnidad_determinacionid() != 0 || 
				lstDeterminacionesPanel.getBase_determinacionid() != 0){
			
			if(lstDeterminacionesPanel.getUnidad_determinacionid() != 0 ){
				//es una unidad
				DeterminacionPanelBean unidadDeterminacionBean = new DeterminacionPanelBean();
				

				if(hmDeterminacionesCatalogoSistematizado.containsKey(lstDeterminacionesPanel.getUnidad_determinacionid())){
					unidadDeterminacionBean = (DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(lstDeterminacionesPanel.getUnidad_determinacionid());
				}
				else if(hmDeterminacionesPlaneamientoVigente.containsKey(lstDeterminacionesPanel.getUnidad_determinacionid())){
					unidadDeterminacionBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoVigente.get(lstDeterminacionesPanel.getUnidad_determinacionid());
				}
				else if(hmDeterminacionesPlaneamientoEncargado.containsKey(lstDeterminacionesPanel.getUnidad_determinacionid())){
					unidadDeterminacionBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoEncargado.get(lstDeterminacionesPanel.getUnidad_determinacionid());
				}
				
				
				//unidadDeterminacionBean = ((DeterminacionPanelBean)hmDeterminaciones.get(lstDeterminacionesPanel.getUnidad_determinacionid()));

				((DeterminacionPanelBean)hmDeterminaciones.get(determinacion.getId()))
						.addUnidadDeterminacion(unidadDeterminacionBean);
		
			}
			
			if(lstDeterminacionesPanel.getBase_determinacionid() != 0){
				//es una base
				DeterminacionPanelBean baseDeterminacionBean = new DeterminacionPanelBean();
				
				baseDeterminacionBean = ((DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(lstDeterminacionesPanel.getBase_determinacionid()));
				
				((DeterminacionPanelBean)hmDeterminaciones.get(determinacion.getId()))
						.addBaseDeterminacion(baseDeterminacionBean);

				
			}
		}
		
	}
	
	private static void gestionOperacion(OperacionDeterminacionBean[] lstOperacionDeterminacion, HashMap hmDeterminacionesCatalogoSistematizado, HashMap hmDeterminacionesPlaneamientoVigente,
			HashMap hmDeterminacionesPlaneamientoEncargado,	DeterminacionPanelBean lstDeterminacionesPanel){

		
		
		if(lstOperacionDeterminacion != null){
			for(int i=0; i<lstOperacionDeterminacion.length; i++){
				
				DeterminacionPanelBean detPanelBean = null;
				if(hmDeterminacionesCatalogoSistematizado.containsKey(lstOperacionDeterminacion[i].getOperada_determinacion().getId())){
					detPanelBean = (DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(lstOperacionDeterminacion[i].getOperada_determinacion().getId());
				}
				else if(hmDeterminacionesPlaneamientoVigente.containsKey(lstOperacionDeterminacion[i].getOperada_determinacion().getId())){
					detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoVigente.get(lstOperacionDeterminacion[i].getOperada_determinacion().getId());
				}
				else if(hmDeterminacionesPlaneamientoEncargado.containsKey(lstOperacionDeterminacion[i].getOperada_determinacion().getId())){
					detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoEncargado.get(lstOperacionDeterminacion[i].getOperada_determinacion().getId());
				}


				lstOperacionDeterminacion[i].setOperada_determinacion(detPanelBean);
				
			}
		}
		
		
		
		lstDeterminacionesPanel.setLstOperacionDeterminacion(lstOperacionDeterminacion);
	
	}
	
	private static void gestionValoresReferencia(DeterminacionBean lstDeterminaciones,
			HashMap hmDeterminacionesCatalogoSistematizado, HashMap hmDeterminacionesPlaneamientoVigente,
			HashMap hmDeterminacionesPlaneamientoEncargado, DeterminacionPanelBean lstDeterminacionesPanel){
		
		if(lstDeterminaciones.getLstValoresReferencia() != null){
			
			DeterminacionPanelBean[] lstDeterminacionesAsoc = 
				new DeterminacionPanelBean[lstDeterminaciones.getLstValoresReferencia().length];
			
			int[] lstIDAsoc = new int[lstDeterminaciones.getLstValoresReferencia().length];
			
			for(int h=0; h<lstDeterminaciones.getLstValoresReferencia().length; h++){
				
				if(lstDeterminaciones.getLstValoresReferencia()[h] != null){
					int determinacionid = lstDeterminaciones.getLstValoresReferencia()[h].getDeterminacionid();
					
					DeterminacionPanelBean detPanelBean = null;
					if(hmDeterminacionesCatalogoSistematizado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoVigente.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoVigente.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoEncargado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoEncargado.get(determinacionid);
					}
					
					
				//	DeterminacionPanelBean detPanelBean = (DeterminacionPanelBean)hmDeterminaciones.get(determinacionid);
					lstDeterminacionesAsoc[h] = detPanelBean;
					
					lstIDAsoc[h] = lstDeterminaciones.getLstValoresReferencia()[h].getId();
					
					ValoresReferenciaPanelBean valRefPanel = new ValoresReferenciaPanelBean();
					valRefPanel.setLstDeterminacionesAsoc(lstDeterminacionesAsoc);
					valRefPanel.setLstIDAsoc(lstIDAsoc);
					lstDeterminacionesPanel.setValoresReferenciaPanelBean(valRefPanel);
				}
			}	
		}

	}
	
	
	private static void gestionGruposAplicacion(DeterminacionBean determinacion,
			HashMap hmDeterminacionesCatalogoSistematizado, HashMap hmDeterminacionesPlaneamientoVigente,
			HashMap hmDeterminacionesPlaneamientoEncargado, DeterminacionPanelBean lstDeterminacionesPanel){

		if(determinacion.getLstGrupoAplicacion() != null){
			
			DeterminacionPanelBean[] lstDeterminacionesAsoc = 
				new DeterminacionPanelBean[determinacion.getLstGrupoAplicacion().length];
			
			int[] lstGrupoAplicIds = new int[determinacion.getLstGrupoAplicacion().length];
			
			for(int h=0; h<determinacion.getLstGrupoAplicacion().length; h++){
				
				if(determinacion.getLstGrupoAplicacion()[h] != null){
					int determinacionid = determinacion.getLstGrupoAplicacion()[h].getDeterminacionid();
					DeterminacionPanelBean detPanelBean = null;
					if(hmDeterminacionesCatalogoSistematizado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoVigente.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoVigente.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoEncargado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoEncargado.get(determinacionid);
					}
					
					
					lstDeterminacionesAsoc[h] = detPanelBean;
					
					lstGrupoAplicIds[h] = determinacion.getLstGrupoAplicacion()[h].getId();
					
					GrupoAplicacionPanelBean grupoAplicacionPanel = new GrupoAplicacionPanelBean();
					grupoAplicacionPanel.setLstDeterminacionesAsoc(lstDeterminacionesAsoc);
					grupoAplicacionPanel.setLstIDAsoc(lstGrupoAplicIds);
					lstDeterminacionesPanel.setGrupoAplicacionPanelBean(grupoAplicacionPanel);
				}
			}
		}
	}
	
	private static void gestionDeterminacionesReguladoras(DeterminacionBean lstDeterminaciones,
			HashMap hmDeterminacionesCatalogoSistematizado, HashMap hmDeterminacionesPlaneamientoVigente,
			HashMap hmDeterminacionesPlaneamientoEncargado, DeterminacionPanelBean lstDeterminacionesPanel){
		
		if(lstDeterminaciones.getLstDeterminacionReguladora() != null){
			
			DeterminacionPanelBean[] lstDeterminacionesAsoc = 
				new DeterminacionPanelBean[lstDeterminaciones.getLstDeterminacionReguladora().length];
			
			int[] lstDeterRegulaIds = new int[lstDeterminaciones.getLstDeterminacionReguladora().length];
			
			for(int h=0; h<lstDeterminaciones.getLstDeterminacionReguladora().length; h++){
				
				if(lstDeterminaciones.getLstDeterminacionReguladora()[h] != null){
					int determinacionid = lstDeterminaciones.getLstDeterminacionReguladora()[h].getDeterminacionid();
					
					DeterminacionPanelBean detPanelBean = null;
					if(hmDeterminacionesCatalogoSistematizado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesCatalogoSistematizado.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoVigente.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoVigente.get(determinacionid);
					}
					else if(hmDeterminacionesPlaneamientoEncargado.containsKey(determinacionid)){
						detPanelBean = (DeterminacionPanelBean)hmDeterminacionesPlaneamientoEncargado.get(determinacionid);
					}
					
					
//					DeterminacionPanelBean detPanelBean = (DeterminacionPanelBean)hmDeterminaciones.get(determinacionid);
					lstDeterminacionesAsoc[h] = detPanelBean;
					
					lstDeterRegulaIds[h] = lstDeterminaciones.getLstDeterminacionReguladora()[h].getId();
					
					DeterminacionesReguladorasPanelBean determinacionReguladoraPanel = new DeterminacionesReguladorasPanelBean();
					determinacionReguladoraPanel.setLstDeterminacionesAsoc(lstDeterminacionesAsoc);
					determinacionReguladoraPanel.setLstIDAsoc(lstDeterRegulaIds);
					lstDeterminacionesPanel.setDeterminacionesReguladorasPanelBean(determinacionReguladoraPanel);
				}
			}
			
		}
		
	}
	
	
	
	
	public static void guardarDatosDeterminaciones(DeterminacionPanelBean[] listDeterminacionesArbol){

		guardarDatosDeterminacionesModificadasBean(listDeterminacionesArbol);
		guardarDatosDeterminacionesAltaBean(listDeterminacionesArbol);
		guardarDatosDeterminacionesBajaBean(listDeterminacionesArbol);
		
		lstReguEspecifModif = null;
		lstReguEspecifAlta = null;
		lstReguEspecifBaja = null;

	}

	
	public static void guardarDatosDeterminacionesAltaBean(DeterminacionPanelBean[] listDeterminacionesArbol){
		
		for(int i=0; i< listDeterminacionesArbol.length; i++){
			DeterminacionPanelBean detPanel = listDeterminacionesArbol[i];
			
			if(detPanel.isNueva() && !detPanel.isEliminada()	){
				
				if(lstDeterminacionesAltaAlmacenaje == null){
					lstDeterminacionesAltaAlmacenaje = new DeterminacionBean[1];
					lstDeterminacionesAltaAlmacenaje[0] = cloneDatosPaneles(detPanel);
				}
				else{
					lstDeterminacionesAltaAlmacenaje = (DeterminacionBean[]) 
								Arrays.copyOf(lstDeterminacionesAltaAlmacenaje,	lstDeterminacionesAltaAlmacenaje.length+1);
					lstDeterminacionesAltaAlmacenaje[lstDeterminacionesAltaAlmacenaje.length-1] = cloneDatosPaneles(detPanel);
				}
				
				
			}
			if(listDeterminacionesArbol[i].getLstDeterminacionesHijas() != null &&
					listDeterminacionesArbol[i].getLstDeterminacionesHijas().length != 0){
				
				guardarDatosDeterminacionesAltaBean(listDeterminacionesArbol[i].getLstDeterminacionesHijas());
			}	
		}
		//return lstDeterminacionesAltaAlmacenaje;
	}
	
	public static void guardarDatosDeterminacionesBajaBean(DeterminacionPanelBean[] listDeterminacionesArbol){
		
		for(int i=0; i< listDeterminacionesArbol.length; i++){
			DeterminacionPanelBean detPanel = listDeterminacionesArbol[i];
			
			if(!detPanel.isNueva() && detPanel.isEliminada()	){
				
				if(lstDeterminacionesBajaAlmacenaje == null){
					lstDeterminacionesBajaAlmacenaje = new DeterminacionBean[1];
					lstDeterminacionesBajaAlmacenaje[0] = cloneDatosPaneles(detPanel);
				}
				else{
					lstDeterminacionesBajaAlmacenaje = (DeterminacionBean[]) 
								Arrays.copyOf(lstDeterminacionesBajaAlmacenaje,	lstDeterminacionesBajaAlmacenaje.length+1);
					lstDeterminacionesBajaAlmacenaje[lstDeterminacionesBajaAlmacenaje.length-1] = cloneDatosPaneles(detPanel);
				}
				
				
			}
			if(listDeterminacionesArbol[i].getLstDeterminacionesHijas() != null &&
					listDeterminacionesArbol[i].getLstDeterminacionesHijas().length != 0){
				
				guardarDatosDeterminacionesBajaBean(listDeterminacionesArbol[i].getLstDeterminacionesHijas());
			}	
		}
		//return lstDeterminacionesBajaAlmacenaje;
	}
	
	
	
	public static void guardarDatosDeterminacionesModificadasBean(DeterminacionPanelBean[] listDeterminacionesArbol){
		
		
		for(int i=0; i< listDeterminacionesArbol.length; i++){
			DeterminacionPanelBean detPanel = listDeterminacionesArbol[i];
			
			if(detPanel.isModificada() &&
					(!detPanel.isNueva() && !detPanel.isEliminada())){
				if(lstDeterminacionesAlmacenaje == null){
					lstDeterminacionesAlmacenaje = new DeterminacionBean[1];
					lstDeterminacionesAlmacenaje[0] = cloneDatosPaneles(detPanel);
				}
				else{
					lstDeterminacionesAlmacenaje = (DeterminacionBean[]) 
								Arrays.copyOf(lstDeterminacionesAlmacenaje,	lstDeterminacionesAlmacenaje.length+1);
					lstDeterminacionesAlmacenaje[lstDeterminacionesAlmacenaje.length-1] = cloneDatosPaneles(detPanel);
				}
				
			
			}
			if(listDeterminacionesArbol[i].getLstDeterminacionesHijas() != null &&
					listDeterminacionesArbol[i].getLstDeterminacionesHijas().length != 0){
				
				guardarDatosDeterminacionesModificadasBean(listDeterminacionesArbol[i].getLstDeterminacionesHijas());
			}	
		}
		//return lstDeterminacionesAlmacenaje;
	}

	
	private static DeterminacionBean cloneDatosPaneles(DeterminacionPanelBean determinacionPanelBean){
		
		DeterminacionBean determinacionBean = new DeterminacionBean();
		determinacionBean.setId(determinacionPanelBean.getId());
		determinacionBean.setCodigo(determinacionPanelBean.getCodigo());
		determinacionBean.setCaracter(determinacionPanelBean.getCaracter());
		determinacionBean.setApartado(determinacionPanelBean.getApartado());
		determinacionBean.setNombre(determinacionPanelBean.getNombre());
		if(determinacionPanelBean.getEtiqueta() != null &&
				!determinacionPanelBean.getEtiqueta().equals("null")){
			determinacionBean.setEtiqueta(determinacionPanelBean.getEtiqueta());
		}
		else{
			determinacionBean.setEtiqueta("");
		}
		determinacionBean.setSuspendida(determinacionPanelBean.isSuspendida());
		if(determinacionPanelBean.getTexto() != null &&
				!determinacionPanelBean.getTexto().equals("null")){
			determinacionBean.setTexto(determinacionPanelBean.getTexto());
		}
		else{
			determinacionBean.setTexto("");
		}
	
		
		determinacionBean.setMadre(determinacionPanelBean.getMadre());
		determinacionBean.setTramite(determinacionPanelBean.getTramite());
		
		// Unidad
		if(determinacionPanelBean.getUnidadBean() != null && 
				determinacionPanelBean.getUnidadBean().getDeterminacion() != null &&
				determinacionPanelBean.getUnidadBean().getDeterminacion().getId() != 0){
			
			determinacionBean.setUnidad_determinacionid(
					determinacionPanelBean.getUnidadBean().getDeterminacion().getId());
			
		}
		
		// Base
		if(determinacionPanelBean.getBaseBean() != null && 
				determinacionPanelBean.getBaseBean().getDeterminacion() != null &&
				determinacionPanelBean.getBaseBean().getDeterminacion().getId() != 0){
			determinacionBean.setBase_determinacionid(determinacionPanelBean.getBaseBean().getDeterminacion().getId());
		}

		
		//Valores de referencia
		if(determinacionPanelBean.getValoresReferenciaPanelBean() != null ){
			
			ValoresReferenciaBean[] lstValoresReferencia = null;
			ValoresReferenciaBean[] lstValoresReferenciaAlta = null;
			ValoresReferenciaBean[] lstValoresReferenciaBaja = null;
			int index= -1;
			if(determinacionPanelBean.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc() != null &&
					determinacionPanelBean.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc().length != 0){
				
				index = determinacionPanelBean.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc().length;
			
				for(int i=0; i<index; i++){
	
					DeterminacionPanelBean determinacionAsoc = 
						determinacionPanelBean.getValoresReferenciaPanelBean().getLstDeterminacionesAsoc()[i];
					int idValRefAsoc =  determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsoc()[i];
						
					if(lstValoresReferencia == null){
						
						lstValoresReferencia = new ValoresReferenciaBean[1];
						lstValoresReferencia[0] = cloneValorReferenciaPanelBeanToValorReferenciaBean(
								determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					
					}
					else{
						lstValoresReferencia = (ValoresReferenciaBean[]) 
									Arrays.copyOf(lstValoresReferencia,	lstValoresReferencia.length+1);
					
						lstValoresReferencia[lstValoresReferencia.length-1] = 
							cloneValorReferenciaPanelBeanToValorReferenciaBean(
									determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					}
					
				}
			}
				
			determinacionBean.setLstValoresReferencia(lstValoresReferencia);
			

			// valores de referencia para dar de alta
			if(determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocAlta() != null){
				index = determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocAlta().length;
				for(int i=0; i<index; i++){
				
					DeterminacionPanelBean determinacionAsocAlta = 
						determinacionPanelBean.getValoresReferenciaPanelBean().getLstDeterminacionesAsocAlta()[i];
		
					if(lstValoresReferenciaAlta == null){
						
						lstValoresReferenciaAlta = new ValoresReferenciaBean[1];
						lstValoresReferenciaAlta[0] = cloneValorReferenciaPanelBeanToValorReferenciaBean(
								determinacionAsocAlta, determinacionPanelBean, 
								determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocAlta()[i]);
						
						
						
					
					}
					else{
						lstValoresReferenciaAlta = (ValoresReferenciaBean[]) 
									Arrays.copyOf(lstValoresReferenciaAlta,	lstValoresReferenciaAlta.length+1);
					
						lstValoresReferenciaAlta[lstValoresReferenciaAlta.length-1] = cloneValorReferenciaPanelBeanToValorReferenciaBean(
								determinacionAsocAlta, determinacionPanelBean, 
									determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocAlta()[i]);
					}
				}
			}
			
			determinacionBean.setLstValoresReferenciaAlta(lstValoresReferenciaAlta);
			
			// valores de referencia para dar de baja
			if(determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocEliminar() != null){
				index = determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocEliminar().length;
				for(int i=0; i<index; i++){
	
					ValoresReferenciaBean valref = new ValoresReferenciaBean();
					
					valref.setId(determinacionPanelBean.getValoresReferenciaPanelBean().getLstIDAsocEliminar()[i]);
	
					if(lstValoresReferenciaBaja == null){
			
						lstValoresReferenciaBaja = new ValoresReferenciaBean[1];
						lstValoresReferenciaBaja[0] = valref;
					
					}
					else{
	
						lstValoresReferenciaBaja = (ValoresReferenciaBean[]) 
									Arrays.copyOf(lstValoresReferenciaBaja,	lstValoresReferenciaBaja.length+1);
					
						lstValoresReferenciaBaja[lstValoresReferenciaBaja.length-1] = valref;
					}
					
				}
			}
			
			determinacionBean.setLstValoresReferenciaBaja(lstValoresReferenciaBaja);
		}
		
		
		//Determinacion Reguladora
		if((determinacionPanelBean.getDeterminacionesReguladorasPanelBean() != null)
				|| determinacionPanelBean.getDeterminacionesReguladorasPanelBean() != null &&
				determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocEliminar() != null){
			
			DeterminacionReguladoraBean[] lstDeterReguladora = null;
			DeterminacionReguladoraBean[] lstDeterReguladoraAlta = null;
			DeterminacionReguladoraBean[] lstDeterReguladoraBaja = null;
			
			int index = 0;
			if(determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc()!=null) {
				index = determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc().length;
				
				for(int i=0; i<index; i++){
					
					DeterminacionPanelBean determinacionAsoc = 
						determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc()[i];
					
					int idValRefAsoc =  determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsoc()[i];
					
					if(lstDeterReguladora == null){
						
						lstDeterReguladora = new DeterminacionReguladoraBean[1];
						lstDeterReguladora[0] = cloneDeterReguladoraPanelBeanToDeterReguladoraBean(
								determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					
					}
					else{
						lstDeterReguladora = (DeterminacionReguladoraBean[]) 
									Arrays.copyOf(lstDeterReguladora,	lstDeterReguladora.length+1);
					
						lstDeterReguladora[lstDeterReguladora.length-1] = 
							cloneDeterReguladoraPanelBeanToDeterReguladoraBean(
									determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					}
					
				}
				
			}
			determinacionBean.setLstDeterminacionReguladora(lstDeterReguladora);
			
			// Determinacion reguladora para dar de alta
			if(determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocAlta() != null){
				index = determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocAlta().length;
				for(int i=0; i<index; i++){
				
					DeterminacionPanelBean determinacionAsocAlta = 
						determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsocAlta()[i];
		
					if(lstDeterReguladoraAlta == null){
						
						lstDeterReguladoraAlta = new DeterminacionReguladoraBean[1];
						lstDeterReguladoraAlta[0] = cloneDeterReguladoraPanelBeanToDeterReguladoraBean(
								determinacionAsocAlta, determinacionPanelBean, 
								determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocAlta()[i]);
						
						
						
					
					}
					else{
						lstDeterReguladoraAlta = (DeterminacionReguladoraBean[]) 
									Arrays.copyOf(lstDeterReguladoraAlta,	lstDeterReguladoraAlta.length+1);
					
						lstDeterReguladoraAlta[lstDeterReguladoraAlta.length-1] = cloneDeterReguladoraPanelBeanToDeterReguladoraBean(
								determinacionAsocAlta, determinacionPanelBean, 
									determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocAlta()[i]);
					}
				}
			}
			
			determinacionBean.setLstDeterminacionReguladoraAlta(lstDeterReguladoraAlta);
			
			// Determinacion reguladora para dar de baja
			if(determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocEliminar() != null){
				index = determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocEliminar().length;
				for(int i=0; i<index; i++){
	
					DeterminacionReguladoraBean detregula = new DeterminacionReguladoraBean();
					
					detregula.setId(determinacionPanelBean.getDeterminacionesReguladorasPanelBean().getLstIDAsocEliminar()[i]);
	
					if(lstDeterReguladoraBaja == null){
			
						lstDeterReguladoraBaja = new DeterminacionReguladoraBean[1];
						lstDeterReguladoraBaja[0] = detregula;
					
					}
					else{
	
						lstDeterReguladoraBaja = (DeterminacionReguladoraBean[]) 
									Arrays.copyOf(lstDeterReguladoraBaja,	lstDeterReguladoraBaja.length+1);
					
						lstDeterReguladoraBaja[lstDeterReguladoraBaja.length-1] = detregula;
					}
					
				}
			}
			
			determinacionBean.setLstDeterminacionReguladoraBaja(lstDeterReguladoraBaja);
			
			
			// Regulaciones Especifica
			if(determinacionPanelBean.getRegulacionEspecificaPanelBean() != null){
				for (int h=0; h< determinacionPanelBean.getRegulacionEspecificaPanelBean().length; h++){
					
					
				}
			}
			
		}
		
		
		//Grupos de aplicacion
		
		if(determinacionPanelBean.getGrupoAplicacionPanelBean() != null){
			
			GrupoAplicacionBean[] lstGrupoAplicacion = null;
			GrupoAplicacionBean[] lstGrupoAplicacionAlta = null;
			GrupoAplicacionBean[] lstGrupoAplicacionBaja = null;
			int index = 0;
			if(determinacionPanelBean.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc() != null &&
					determinacionPanelBean.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc().length != 0){
				index = determinacionPanelBean.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc().length;
				
				for(int i=0; i<index; i++){
					
					DeterminacionPanelBean determinacionAsoc = 
						determinacionPanelBean.getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc()[i];
					
					int idValRefAsoc =  determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsoc()[i];
					
					if(lstGrupoAplicacion == null){
						
						lstGrupoAplicacion = new GrupoAplicacionBean[1];
						lstGrupoAplicacion[0] = cloneGrupoAplicacionPanelBeanToGrupoAplicacionBean(
								determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					
					}
					else{
						lstGrupoAplicacion = (GrupoAplicacionBean[]) 
									Arrays.copyOf(lstGrupoAplicacion,	lstGrupoAplicacion.length+1);
					
						lstGrupoAplicacion[lstGrupoAplicacion.length-1] = 
							cloneGrupoAplicacionPanelBeanToGrupoAplicacionBean(
									determinacionAsoc, determinacionPanelBean, idValRefAsoc);
					}
					
				}
			}
			determinacionBean.setLstGrupoAplicacion(lstGrupoAplicacion);
			
						
			// grupo aplicacion para dar de alta
			if(determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocAlta() != null){
				index = determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocAlta().length;
				for(int i=0; i<index; i++){
				
					DeterminacionPanelBean determinacionAsocAlta = 
						determinacionPanelBean.getGrupoAplicacionPanelBean().getLstDeterminacionesAsocAlta()[i];
		
					if(lstGrupoAplicacionAlta == null){
						
						lstGrupoAplicacionAlta = new GrupoAplicacionBean[1];
						lstGrupoAplicacionAlta[0] = cloneGrupoAplicacionPanelBeanToGrupoAplicacionBean(
								determinacionAsocAlta, determinacionPanelBean, 
								determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocAlta()[i]);
						
						
						
					
					}
					else{
						lstGrupoAplicacionAlta = (GrupoAplicacionBean[]) 
									Arrays.copyOf(lstGrupoAplicacionAlta,	lstGrupoAplicacionAlta.length+1);
					
						lstGrupoAplicacionAlta[lstGrupoAplicacionAlta.length-1] = cloneGrupoAplicacionPanelBeanToGrupoAplicacionBean(
								determinacionAsocAlta, determinacionPanelBean, 
									determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocAlta()[i]);
					}
				}
			}
			
			determinacionBean.setLstGrupoAplicacionAlta(lstGrupoAplicacionAlta);
			
			// grupo aplicacion para dar de baja
			if(determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocEliminar() != null){
				index = determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocEliminar().length;
				for(int i=0; i<index; i++){
	
					GrupoAplicacionBean grupoAplic = new GrupoAplicacionBean();
					
					grupoAplic.setId(determinacionPanelBean.getGrupoAplicacionPanelBean().getLstIDAsocEliminar()[i]);
	
					if(lstGrupoAplicacionBaja == null){
			
						lstGrupoAplicacionBaja = new GrupoAplicacionBean[1];
						lstGrupoAplicacionBaja[0] = grupoAplic;
					
					}
					else{
	
						lstGrupoAplicacionBaja = (GrupoAplicacionBean[]) 
									Arrays.copyOf(lstGrupoAplicacionBaja,	lstGrupoAplicacionBaja.length+1);
					
						lstGrupoAplicacionBaja[lstGrupoAplicacionBaja.length-1] = grupoAplic;
					}
					
				}
			}
			
			determinacionBean.setLstGrupoAplicacionBaja(lstGrupoAplicacionBaja);
			
		}
		
		//regulaciones especificas
		if(determinacionPanelBean.getRegulacionEspecificaPanelBean() != null &&
				determinacionPanelBean.getRegulacionEspecificaPanelBean().length != 0){
			
			
			guardarDatosRegulacionesEspecificas(determinacionPanelBean.getRegulacionEspecificaPanelBean());
			
			determinacionBean.setLstRegulacionesEspecificasAlta(lstReguEspecifAlta);
			determinacionBean.setLstRegulacionesEspecificasModif(lstReguEspecifModif);
			determinacionBean.setLstRegulacionesEspecificasBaja(lstReguEspecifBaja);
			
		}
		
		
		
		//Documentos
		if(determinacionPanelBean.getLstDocumentosPanelBean() != null){
			
			DocumentoDeterminacionBean[] lstDocumentoAlta = null;
			DocumentoDeterminacionBean[] lstDocumentoBaja = null;
			
			
			int index = 0;
			
			// Documentos para dar de alta
			// grupo aplicacion para dar de alta
			if(determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta() != null){
				index = determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta().length;
				for(int i=0; i<index; i++){
				
					DocumentoPanelBean documentoAlta = 
						determinacionPanelBean.getLstDocumentosPanelBean().getLstDocumentoAsocAlta()[i];
		
					if(lstDocumentoAlta == null){
						
						lstDocumentoAlta = new DocumentoDeterminacionBean[1];
						lstDocumentoAlta[0] = cloneDocumentoPanelBeanToDocumentoBean(
								documentoAlta, determinacionPanelBean, 
								determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta()[i]);
						
					}
					else{
						lstDocumentoAlta = (DocumentoDeterminacionBean[]) 
									Arrays.copyOf(lstDocumentoAlta,	lstDocumentoAlta.length+1);
					
						lstDocumentoAlta[lstDocumentoAlta.length-1] = cloneDocumentoPanelBeanToDocumentoBean(
								documentoAlta, determinacionPanelBean, 
									determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta()[i]);
					}
				}
			}
			
			determinacionBean.setLstDocumentoAlta(lstDocumentoAlta);
		
		
			// Documentos para dar de baja
			if(determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar() != null){
				index = determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar().length;
				for(int i=0; i<index; i++){
	
					DocumentoDeterminacionBean documentoBaja = new DocumentoDeterminacionBean();
					
					documentoBaja.setId(determinacionPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar()[i]);
	
					if(lstDocumentoBaja == null){
			
						lstDocumentoBaja = new DocumentoDeterminacionBean[1];
						lstDocumentoBaja[0] = documentoBaja;
					
					}
					else{
	
						lstDocumentoBaja = (DocumentoDeterminacionBean[]) 
									Arrays.copyOf(lstDocumentoBaja,	lstDocumentoBaja.length+1);
					
						lstDocumentoBaja[lstDocumentoBaja.length-1] = documentoBaja;
					}
					
				}
			}
			
			determinacionBean.setLstDocumentoBaja(lstDocumentoBaja);
		}
		
		
		//Operacion
		if(determinacionPanelBean.getLstOperacionDeterminacion() != null){
			
			determinacionBean.setLstOperacionDeterminacion(determinacionPanelBean.getLstOperacionDeterminacion());
		}
		
		
		return  determinacionBean;
	}
	
	private static DocumentoDeterminacionBean cloneDocumentoPanelBeanToDocumentoBean(
			DocumentoPanelBean documentoAsoc ,
			DeterminacionPanelBean determinacionPanelBeanTrabajo,
			int idDocAsoc){
		
		DocumentoDeterminacionBean documentoBean = new DocumentoDeterminacionBean();
		
		documentoBean.setId(idDocAsoc);
		documentoBean.setDeterminacion(determinacionPanelBeanTrabajo.getId());
		documentoBean.setDocumentoid(documentoAsoc.getDocumentoid());
	
		return documentoBean;
	}
	
	private static GrupoAplicacionBean cloneGrupoAplicacionPanelBeanToGrupoAplicacionBean(
			DeterminacionPanelBean determinacionAsoc ,
			DeterminacionPanelBean determinacionPanelBeanTrabajo,
			int idValRefAsoc){
		
		GrupoAplicacionBean grupoAplicacionBean = new GrupoAplicacionBean();
		
		grupoAplicacionBean.setId(idValRefAsoc);
		grupoAplicacionBean.setDeterminacion(determinacionPanelBeanTrabajo.getId());
		grupoAplicacionBean.setDeterminacionid(determinacionAsoc.getId());
	
		return grupoAplicacionBean;
	}
	
	private static DeterminacionReguladoraBean cloneDeterReguladoraPanelBeanToDeterReguladoraBean(
			DeterminacionPanelBean determinacionAsoc ,
			DeterminacionPanelBean determinacionPanelBeanTrabajo,
			int idValRefAsoc){
		
		DeterminacionReguladoraBean deterReguladoraBean = new DeterminacionReguladoraBean();
		
		deterReguladoraBean.setId(idValRefAsoc);
		deterReguladoraBean.setDeterminacion(determinacionPanelBeanTrabajo.getId());
		deterReguladoraBean.setDeterminacionid(determinacionAsoc.getId());
	
		return deterReguladoraBean;
	}
	
	
	
	private static ValoresReferenciaBean cloneValorReferenciaPanelBeanToValorReferenciaBean(
			DeterminacionPanelBean determinacionAsoc ,
			DeterminacionPanelBean determinacionPanelBeanTrabajo,
			int idValRefAsoc){
		
		ValoresReferenciaBean valorReferenciaBean = new ValoresReferenciaBean();
		
		
		valorReferenciaBean.setId(idValRefAsoc);
		valorReferenciaBean.setDeterminacion(determinacionPanelBeanTrabajo.getId());
		valorReferenciaBean.setDeterminacionid(determinacionAsoc.getId());
	
		return valorReferenciaBean;
	}
	

	public static RegulacionesEspecificasBean[] getLstReguEspecifModif() {
		return lstReguEspecifModif;
	}

	public static void setLstReguEspecifModif(
			RegulacionesEspecificasBean[] lstReguEspecifModif) {
		OperacionesDeterminaciones.lstReguEspecifModif = lstReguEspecifModif;
	}

	public static RegulacionesEspecificasBean[] getLstReguEspecifAlta() {
		return lstReguEspecifAlta;
	}

	public static void setLstReguEspecifAlta(
			RegulacionesEspecificasBean[] lstReguEspecifAlta) {
		OperacionesDeterminaciones.lstReguEspecifAlta = lstReguEspecifAlta;
	}

	public static RegulacionesEspecificasBean[] getLstReguEspecifBaja() {
		return lstReguEspecifBaja;
	}

	public static void setLstReguEspecifBaja(
			RegulacionesEspecificasBean[] lstReguEspecifBaja) {
		OperacionesDeterminaciones.lstReguEspecifBaja = lstReguEspecifBaja;
	}

	public static DeterminacionBean[] getLstDeterminacionesAlmacenaje() {
		return lstDeterminacionesAlmacenaje;
	}

	public static void setLstDeterminacionesAlmacenaje(
			DeterminacionBean[] lstDeterminacionesAlmacenaje) {
		OperacionesDeterminaciones.lstDeterminacionesAlmacenaje = lstDeterminacionesAlmacenaje;
	}

	public static DeterminacionBean[] getLstDeterminacionesAltaAlmacenaje() {
		return lstDeterminacionesAltaAlmacenaje;
	}

	public static void setLstDeterminacionesAltaAlmacenaje(
			DeterminacionBean[] lstDeterminacionesAltaAlmacenaje) {
		OperacionesDeterminaciones.lstDeterminacionesAltaAlmacenaje = lstDeterminacionesAltaAlmacenaje;
	}

	public static DeterminacionBean[] getLstDeterminacionesBajaAlmacenaje() {
		return lstDeterminacionesBajaAlmacenaje;
	}

	public static void setLstDeterminacionesBajaAlmacenaje(
			DeterminacionBean[] lstDeterminacionesBajaAlmacenaje) {
		OperacionesDeterminaciones.lstDeterminacionesBajaAlmacenaje = lstDeterminacionesBajaAlmacenaje;
	}

}
