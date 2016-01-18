/**
 * OperacionesEntidades.java
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
import com.gestorfip.app.planeamiento.beans.panels.tramite.DocumentoPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DocumentosPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentoEntidadBean;
import com.gestorfip.app.planeamiento.beans.tramite.DocumentosBean;
import com.gestorfip.app.planeamiento.beans.tramite.EntidadBean;
import com.gestorfip.app.planeamiento.beans.tramite.OperacionEntidadBean;
import com.gestorfip.app.planeamiento.ws.cliente.ClientGestorFip;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class OperacionesEntidades {
	
	
	private static EntidadBean[] lstEntidadesAlmacenaje = null;
	private static EntidadBean[] lstEntidadesAltaAlmacenaje = null;
	private static EntidadBean[] lstEntidadesBajaAlmacenaje = null;
	
	static EntidadBean[] lstEntCatalogoSistematizado = null;
	static EntidadBean[] lstEntPlaneamientoVigente = null; 

	
	public static HashMap hashEntidadesCS = new HashMap();
	public static HashMap hashEntidadesPV = new HashMap();
	public static HashMap hashEntidadesPE = new HashMap();
	
	
	
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

	
	public static FipPanelBean consultaEntidadesAsociadasFip_GestorPlaneamiento(FipPanelBean fip, 
						ApplicationContext aplicacion, TaskMonitorDialog progressDialog, boolean isActualizar_CS_PV){
		
		try {

			EntidadBean[] lstEntidadesCatalogoSistematizado = null;
			lstEntidadesCatalogoSistematizado=	ClientGestorFip.obtenerEntidadesAsociadasFip_GestorPlaneamiento(aplicacion, 
									fip.getId(), fip.getTramiteCatalogoSistematizado().getId());

			lstEntCatalogoSistematizado = lstEntidadesCatalogoSistematizado;

			if(progressDialog.isCancelRequested())
				return null;
			
			EntidadBean[] lstEntidadesPlaneamientoVigente = null;

			lstEntidadesPlaneamientoVigente = ClientGestorFip.obtenerEntidadesAsociadasFip_GestorPlaneamiento(aplicacion, 
								fip.getId(), fip.getTramitePlaneamientoVigente().getId());
			lstEntPlaneamientoVigente = lstEntidadesPlaneamientoVigente;

			if(progressDialog.isCancelRequested())
				return null;

			if(progressDialog.isCancelRequested())
				return null;

			gestionArbolEntidades(lstEntCatalogoSistematizado,
									lstEntPlaneamientoVigente,
									null, 
									fip, isActualizar_CS_PV);

			return fip;
			
		} catch (Exception e) {

			e.printStackTrace();
			
			return null;
		}
	}	
	
	public static FipPanelBean consultaArbolEntidadesAsociadasFip(FipPanelBean fip, 
			ApplicationContext aplicacion, TaskMonitorDialog progressDialog){
		
		try {
		EntidadBean[] lstEntidadesCatalogoSistematizado = null;
		if(fip.getTramiteCatalogoSistematizado() != null){
			lstEntidadesCatalogoSistematizado=	ClientGestorFip.obtenerArbolEntidadesAsociadasTramite(aplicacion, 
									fip.getTramiteCatalogoSistematizado().getId(), false);
			
			lstEntCatalogoSistematizado = lstEntidadesCatalogoSistematizado;
		}
		else{
			lstEntidadesCatalogoSistematizado = lstEntCatalogoSistematizado;
		}
		
		if(progressDialog.isCancelRequested())
			return null;
		
		EntidadBean[] lstEntidadesPlaneamientoVigente = null;
		if(fip.getTramitePlaneamientoVigente() != null){
				lstEntidadesPlaneamientoVigente = ClientGestorFip.obtenerArbolEntidadesAsociadasTramite(aplicacion, 
									fip.getTramitePlaneamientoVigente().getId(), false);
				lstEntPlaneamientoVigente = lstEntidadesPlaneamientoVigente;
		}
		else{
			lstEntidadesPlaneamientoVigente = lstEntPlaneamientoVigente;
		}
		
		if(progressDialog.isCancelRequested())
			return null;
		
		EntidadBean[] lstEntidadesPlaneamientoEncargado = null;
		if(fip.getTramitePlaneamientoEncargado() != null){
			lstEntidadesPlaneamientoEncargado = ClientGestorFip.obtenerArbolEntidadesAsociadasTramite(aplicacion,
									fip.getTramitePlaneamientoEncargado().getId(), true);
		}
		
		if(progressDialog.isCancelRequested())
			return null;
		
		
		if(progressDialog.isCancelRequested())
			return null;
		
		gestionArbolEnt(lstEntidadesCatalogoSistematizado,
								lstEntidadesPlaneamientoVigente,
								lstEntidadesPlaneamientoEncargado, 
								fip);
		
		if(lstEntidadesCatalogoSistematizado != null){
			for(int i=0; i <lstEntidadesCatalogoSistematizado.length; i++){
				if(lstEntidadesCatalogoSistematizado[i] != null){
					hashEntidadesCS.put(lstEntidadesCatalogoSistematizado[i].getId(),lstEntidadesCatalogoSistematizado[i]);	
				}
			}
		}
		
		if(lstEntidadesPlaneamientoVigente != null){
			for(int i=0; i <lstEntidadesPlaneamientoVigente.length; i++){
				if(lstEntidadesPlaneamientoVigente[i] != null){
					hashEntidadesPV.put(lstEntidadesPlaneamientoVigente[i].getId(),lstEntidadesPlaneamientoVigente[i]);	
				}
			}
		}
		
		if(lstEntidadesPlaneamientoEncargado != null){
			for(int i=0; i <lstEntidadesPlaneamientoEncargado.length; i++){
				if(lstEntidadesPlaneamientoEncargado[i] != null){
					hashEntidadesPE.put(lstEntidadesPlaneamientoEncargado[i].getId(),lstEntidadesPlaneamientoEncargado[i]);	
				}
			}
		}
		
		return fip;
		
		} catch (Exception e) {
		
		e.printStackTrace();
		
		return null;
		}
	}

	private static EntidadPanelBean[] cloneEntidadBeanToEntidadPanelBean(EntidadBean[] lstEntidades){
		
		EntidadPanelBean[] lstEntidadesPanel = 
					new EntidadPanelBean[lstEntidades.length];
		
		for(int i=0; i<lstEntidades.length; i++){
			if(lstEntidades[i] != null){
			lstEntidadesPanel[i] = new EntidadPanelBean();
				
				lstEntidadesPanel[i].setId(lstEntidades[i].getId());
				lstEntidadesPanel[i].setCodigo(lstEntidades[i].getCodigo());
				lstEntidadesPanel[i].setClave(lstEntidades[i].getClave());
				lstEntidadesPanel[i].setNombre(lstEntidades[i].getNombre());
				lstEntidadesPanel[i].setEtiqueta(lstEntidades[i].getEtiqueta());
				lstEntidadesPanel[i].setSuspendida(lstEntidades[i].isSuspendida());
				lstEntidadesPanel[i].setBase_entidadid(lstEntidades[i].getBase_entidadid());
				lstEntidadesPanel[i].setMadre(lstEntidades[i].getMadre());
				lstEntidadesPanel[i].setTramite(lstEntidades[i].getTramite());
				lstEntidadesPanel[i].setIdLayer(lstEntidades[i].getIdLayer());
				lstEntidadesPanel[i].setIdFeature(lstEntidades[i].getIdFeature());
				lstEntidadesPanel[i].setModificable(lstEntidades[i].isModificable());
				lstEntidadesPanel[i].setCodigoValRefCU(lstEntidades[i].getCodigoValRefCU());
				
			}
		}
		
		return  lstEntidadesPanel;
	}
	
	
	private static EntidadPanelBean[] gestionArbolEntidadesTramite(EntidadBean[] lstEntidades, 
			EntidadPanelBean[] lstEntidadesPanelEncargado,
			EntidadPanelBean[] lstEntidadesPanelCatalogoSistematizado,
			EntidadPanelBean[] lstEntidadesPanelPlaneamientoVigente){
		
		EntidadPanelBean[] lstEntidadesPanel = null;
		HashMap hmEntidades = new HashMap();
		HashMap hmEntidadesPlanEncargado = new HashMap();
		HashMap hmEntidadesCatalogoSistematizado = new HashMap();
		HashMap hmEntidadesPlaneamientoVigente = new HashMap();
		
		ArrayList arbolEntidades = new ArrayList();
		EntidadPanelBean[] lstArbolEntidades = null;
		
		
		if(lstEntidadesPanelEncargado != null){
			for(int i=0; i<lstEntidadesPanelEncargado.length; i++){
				if(lstEntidadesPanelEncargado[i] != null){
					hmEntidadesPlanEncargado.put(lstEntidadesPanelEncargado[i].getId(), lstEntidadesPanelEncargado[i]);
				}
			}
		}
		
		if(lstEntidadesPanelCatalogoSistematizado != null){
			for(int i=0; i<lstEntidadesPanelCatalogoSistematizado.length; i++){
				if(lstEntidadesPanelCatalogoSistematizado[i] != null){
					hmEntidadesCatalogoSistematizado.put(lstEntidadesPanelCatalogoSistematizado[i].getId(), lstEntidadesPanelCatalogoSistematizado[i]);
				}
			}
		}
		
		if(lstEntidadesPanelPlaneamientoVigente != null){
			for(int i=0; i<lstEntidadesPanelPlaneamientoVigente.length; i++){
				if(lstEntidadesPanelPlaneamientoVigente[i] != null){
					hmEntidadesPlaneamientoVigente.put(lstEntidadesPanelPlaneamientoVigente[i].getId(), lstEntidadesPanelPlaneamientoVigente[i]);
				}
			}
		}
		if( lstEntidadesPanelEncargado  == null){
			
			if(lstEntidadesPanelPlaneamientoVigente != null){
				lstEntidadesPanel = lstEntidadesPanelPlaneamientoVigente;
				hmEntidades = hmEntidadesPlaneamientoVigente;
			}
			else if(lstEntidadesPanelCatalogoSistematizado != null){
				lstEntidadesPanel = lstEntidadesPanelCatalogoSistematizado;
				hmEntidades = hmEntidadesCatalogoSistematizado;
			}
			
		}
		else{
			lstEntidadesPanel = lstEntidadesPanelEncargado;
			hmEntidades = hmEntidadesPlanEncargado;
		}
		
		for(int j=0; j<lstEntidadesPanel.length; j++){

			if(lstEntidadesPanel[j] != null){
				if(lstEntidadesPanel[j].getMadre()== 0){
					// es una determinacion que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
					arbolEntidades.add(lstEntidadesPanel[j]);
	
				}
				else{
					//es una determinacion hija
					
					((EntidadPanelBean)hmEntidades.get(lstEntidadesPanel[j].getMadre()))
								.addEntidadHija((EntidadPanelBean)lstEntidadesPanel[j]);
				}
			
			
				// Gestion de la Base
				gestionBase(lstEntidades[j], hmEntidades, hmEntidadesCatalogoSistematizado, lstEntidadesPanel[j]);
				
				//gestion Documentos
				//gestionDocumentos(lstEntidades[j].getLstDocumento(), lstEntidadesPanel[j], lstDocumentosAsocTramite);
				
				// gestion Operacion
				gestionOperacion(lstEntidades[j].getLstOperacionEntidad(), lstEntidadesPanel[j]);
				
			}
			
			
		}
		
		lstArbolEntidades = new EntidadPanelBean[arbolEntidades.size()];
		for(int h=0; h<arbolEntidades.size(); h++){
			lstArbolEntidades[h] = (EntidadPanelBean)arbolEntidades.get(h);
		}
		
		return lstArbolEntidades;
	}
	
	private static EntidadPanelBean[] gestionArbolEntTramite(EntidadBean[] lstEntidades, 
			EntidadPanelBean[] lstEntidadesPanelEncargado,
			EntidadPanelBean[] lstEntidadesPanelCatalogoSistematizado,
			EntidadPanelBean[] lstEntidadesPanelPlaneamientoVigente){
		
		EntidadPanelBean[] lstEntidadesPanel = null;
		HashMap hmEntidades = new HashMap();
		HashMap hmEntidadesPlanEncargado = new HashMap();
		HashMap hmEntidadesCatalogoSistematizado = new HashMap();
		HashMap hmEntidadesPlaneamientoVigente = new HashMap();
		
		ArrayList arbolEntidades = new ArrayList();
		EntidadPanelBean[] lstArbolEntidades = null;
		
		
		if(lstEntidadesPanelEncargado != null){
			for(int i=0; i<lstEntidadesPanelEncargado.length; i++){
				if(lstEntidadesPanelEncargado[i] != null){
					hmEntidadesPlanEncargado.put(lstEntidadesPanelEncargado[i].getId(), lstEntidadesPanelEncargado[i]);
				}
			}
		}
		
		if(lstEntidadesPanelCatalogoSistematizado != null){
			for(int i=0; i<lstEntidadesPanelCatalogoSistematizado.length; i++){
				if(lstEntidadesPanelCatalogoSistematizado[i] != null){
					hmEntidadesCatalogoSistematizado.put(lstEntidadesPanelCatalogoSistematizado[i].getId(), lstEntidadesPanelCatalogoSistematizado[i]);
				}
			}
		}
		
		if(lstEntidadesPanelPlaneamientoVigente != null){
			for(int i=0; i<lstEntidadesPanelPlaneamientoVigente.length; i++){
				if(lstEntidadesPanelPlaneamientoVigente[i] != null){
					hmEntidadesPlaneamientoVigente.put(lstEntidadesPanelPlaneamientoVigente[i].getId(), lstEntidadesPanelPlaneamientoVigente[i]);
				}
			}
		}
		if( lstEntidadesPanelEncargado  == null){
			
			if(lstEntidadesPanelPlaneamientoVigente != null){
				lstEntidadesPanel = lstEntidadesPanelPlaneamientoVigente;
				hmEntidades = hmEntidadesPlaneamientoVigente;
			}
			else if(lstEntidadesPanelCatalogoSistematizado != null){
				lstEntidadesPanel = lstEntidadesPanelCatalogoSistematizado;
				hmEntidades = hmEntidadesCatalogoSistematizado;
			}
			
		}
		else{
			lstEntidadesPanel = lstEntidadesPanelEncargado;
			hmEntidades = hmEntidadesPlanEncargado;
		}
		
		for(int j=0; j<lstEntidadesPanel.length; j++){

			if(lstEntidadesPanel[j] != null){
				if(lstEntidadesPanel[j].getMadre()== 0){
					// es una determinacion que no tiene ningun padre. por lo tanto es la raiz de trï¿½mite
					arbolEntidades.add(lstEntidadesPanel[j]);
	
				}
				else{
					//es una determinacion hija
					
					((EntidadPanelBean)hmEntidades.get(lstEntidadesPanel[j].getMadre()))
								.addEntidadHija((EntidadPanelBean)lstEntidadesPanel[j]);
				}
			
				
			}
			
			
		}
		
		lstArbolEntidades = new EntidadPanelBean[arbolEntidades.size()];
		for(int h=0; h<arbolEntidades.size(); h++){
			lstArbolEntidades[h] = (EntidadPanelBean)arbolEntidades.get(h);
		}
		
		return lstArbolEntidades;
	}
		
	private static void gestionArbolEntidades(
			EntidadBean[] lstEntidadesCatalogoSistematizado, 
			EntidadBean[] lstEntidadesPlaneamientoVigente,
			EntidadBean[] lstEntidadesPlaneamientoEncargado,
			FipPanelBean fip ,boolean isActualizar_CS_PV){
		
		EntidadPanelBean[] lstArbolEntidadesCatalogoSistematizado = null;
		EntidadPanelBean[] lstArbolEntidadesPlaneamientoVigente = null;
		EntidadPanelBean[] lstArbolEntidadesPlaneamientoEncargado = null;
		
		EntidadPanelBean[] lstEntidadesPanelCatalogoSistematizado = null;
		EntidadPanelBean[] lstEntidadesPanelPlaneamientoVigente = null;
		EntidadPanelBean[] lstEntidadesPanelPlaneamientoEncargado = null;
		
		
		if(lstEntidadesCatalogoSistematizado != null){

			 lstEntidadesPanelCatalogoSistematizado = cloneEntidadBeanToEntidadPanelBean(lstEntidadesCatalogoSistematizado);
			 
			 if(isActualizar_CS_PV){
				 lstArbolEntidadesCatalogoSistematizado = 
						gestionArbolEntidadesTramite(lstEntidadesCatalogoSistematizado,
															null, lstEntidadesPanelCatalogoSistematizado, null);
				 
				 fip.setLstArbolEntidadesCatalogoSistematizado(lstArbolEntidadesCatalogoSistematizado);
			 }
		}
		if(lstEntidadesPlaneamientoVigente != null){

			 lstEntidadesPanelPlaneamientoVigente = cloneEntidadBeanToEntidadPanelBean(lstEntidadesPlaneamientoVigente);
			 
			 if(isActualizar_CS_PV){
				 lstArbolEntidadesPlaneamientoVigente = 
						gestionArbolEntidadesTramite(lstEntidadesPlaneamientoVigente,
														null, lstEntidadesPanelCatalogoSistematizado, lstEntidadesPanelPlaneamientoVigente);
				 
				 fip.setLstArbolEntidadesPlaneamientoVigente(lstArbolEntidadesPlaneamientoVigente);
			 }

		}

		if(lstEntidadesPlaneamientoEncargado != null){

			 lstEntidadesPanelPlaneamientoEncargado = cloneEntidadBeanToEntidadPanelBean(lstEntidadesPlaneamientoEncargado);
			 
				lstArbolEntidadesPlaneamientoEncargado =
					gestionArbolEntidadesTramite(lstEntidadesPlaneamientoEncargado,
													lstEntidadesPanelPlaneamientoEncargado,
													lstEntidadesPanelCatalogoSistematizado,
													lstArbolEntidadesPlaneamientoVigente);
				fip.setLstArbolEntidadesPlaneamientoEncargado(lstArbolEntidadesPlaneamientoEncargado);
		}
	}
	
	
	private static void gestionArbolEnt(
			EntidadBean[] lstEntidadesCatalogoSistematizado, 
			EntidadBean[] lstEntidadesPlaneamientoVigente,
			EntidadBean[] lstEntidadesPlaneamientoEncargado,
			FipPanelBean fip){
		
		
		EntidadPanelBean[] lstArbolEntidadesCatalogoSistematizado = null;
		EntidadPanelBean[] lstArbolEntidadesPlaneamientoVigente = null;
		EntidadPanelBean[] lstArbolEntidadesPlaneamientoEncargado = null;
		
		EntidadPanelBean[] lstEntidadesPanelCatalogoSistematizado = null;
		EntidadPanelBean[] lstEntidadesPanelPlaneamientoVigente = null;
		EntidadPanelBean[] lstEntidadesPanelPlaneamientoEncargado = null;
		
		
		if(lstEntidadesCatalogoSistematizado != null){
			 lstEntidadesPanelCatalogoSistematizado = cloneEntidadBeanToEntidadPanelBean(lstEntidadesCatalogoSistematizado);

			 lstArbolEntidadesCatalogoSistematizado = 
					gestionArbolEntTramite(lstEntidadesCatalogoSistematizado,
														null, lstEntidadesPanelCatalogoSistematizado, null);
			 
			 fip.setLstArbolEntidadesCatalogoSistematizado(lstArbolEntidadesCatalogoSistematizado);

		}
		if(lstEntidadesPlaneamientoVigente != null){
			 lstEntidadesPanelPlaneamientoVigente = cloneEntidadBeanToEntidadPanelBean(lstEntidadesPlaneamientoVigente);
			 
			 lstArbolEntidadesPlaneamientoVigente = 
				 gestionArbolEntTramite(lstEntidadesPlaneamientoVigente,
													null, lstEntidadesPanelCatalogoSistematizado, lstEntidadesPanelPlaneamientoVigente);
			 
			 fip.setLstArbolEntidadesPlaneamientoVigente(lstArbolEntidadesPlaneamientoVigente);
			 
		}
		
		if(lstEntidadesPlaneamientoEncargado != null){
			 lstEntidadesPanelPlaneamientoEncargado = cloneEntidadBeanToEntidadPanelBean(lstEntidadesPlaneamientoEncargado);
			 
				lstArbolEntidadesPlaneamientoEncargado =
					gestionArbolEntTramite(lstEntidadesPlaneamientoEncargado,
													lstEntidadesPanelPlaneamientoEncargado,
													lstEntidadesPanelCatalogoSistematizado,
													lstArbolEntidadesPlaneamientoVigente);
				fip.setLstArbolEntidadesPlaneamientoEncargado(lstArbolEntidadesPlaneamientoEncargado);
		}

	}
	
	private static void gestionOperacion(OperacionEntidadBean[] operacionEntidad,
			EntidadPanelBean lstEntidadesPanel){

		lstEntidadesPanel.setLstOperacionEntidad(operacionEntidad);
	
	}
	

	private static DocumentoPanelBean[] cloneDocumentosBeanToDocumentosPanelBean(
			DocumentoEntidadBean[] lstDocumentos,   DocumentosBean[] lstDocumentosAsocTramite){
		
		DocumentoPanelBean[] lstDocumentosEntidadPanel = null;
		
		if(lstDocumentos != null){
			
			lstDocumentosEntidadPanel = 
  				new DocumentoPanelBean[lstDocumentos.length];
			
			for(int i=0; i<lstDocumentos.length; i++){
				
				if(lstDocumentos[i] != null){
					lstDocumentosEntidadPanel[i] = new DocumentoPanelBean();
					
					lstDocumentosEntidadPanel[i].setId(lstDocumentos[i].getId());
					lstDocumentosEntidadPanel[i].setDocumentoid(lstDocumentos[i].getDocumentoid());
					lstDocumentosEntidadPanel[i].setDeterminacion(lstDocumentos[i].getDeterminacion());
					if(lstDocumentos[i] != null){
						
						if(lstDocumentosAsocTramite != null && lstDocumentosAsocTramite.length != 0){
							for(int j=0; j<lstDocumentosAsocTramite.length; j++){
								
								if(lstDocumentos[i].getDocumentoid() == lstDocumentosAsocTramite[j].getId()){
									lstDocumentosEntidadPanel[i].setNombre(lstDocumentosAsocTramite[j].getNombre());
								}
							}
						}
					}
				}
			}
		}

		return lstDocumentosEntidadPanel;
	}
	
	public static void gestionDocumentos(DocumentoEntidadBean[] lstDocumentos,
			EntidadPanelBean entidadPanel,
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
			
			entidadPanel.setLstDocumentosPanelBean(docPanelBean);
			
		}
	}
	
	private static void gestionBase(EntidadBean entidad,
			HashMap hmEntidad,HashMap hmEntidadesCatalogoSistematizado, EntidadPanelBean entidadPanel){
	
		if(	entidadPanel.getBase_entidadid() != 0){
			
			if(entidadPanel.getBase_entidadid() != 0){
				//es una base
				EntidadPanelBean baseEntidadBean = new EntidadPanelBean();
				
				baseEntidadBean = ((EntidadPanelBean)hmEntidadesCatalogoSistematizado.get(entidadPanel.getBase_entidadid()));
				
				((EntidadPanelBean)hmEntidad.get(entidad.getId()))
						.addBaseEntidad(baseEntidadBean);
			}
		}
	}
	
	
	public static void guardarDatosEntidades(EntidadPanelBean[] listEntidadesArbol){

		guardarDatosEntidadesModificadasBean(listEntidadesArbol);
		guardarDatosEntidadesAltaBean(listEntidadesArbol);
		guardarDatosEntidadesBajaBean(listEntidadesArbol);
	}
	
	public static void guardarDatosEntidadesGraficas(EntidadPanelBean[] listEntidadesArbol){

		guardarDatosEntidadesGraficasModificadasBean(listEntidadesArbol);
	}

	public static void guardarDatosEntidadesGraficasModificadasBean(EntidadPanelBean[] listEntidadesArbol){
		
		
		for(int i=0; i< listEntidadesArbol.length; i++){
			EntidadPanelBean entidadPanel = listEntidadesArbol[i];
			
			if(entidadPanel.isModificada()){
				if(lstEntidadesAlmacenaje == null){
					lstEntidadesAlmacenaje = new EntidadBean[1];
					lstEntidadesAlmacenaje[0] = cloneDatosEntidadesGraficosPaneles(entidadPanel);
				}
				else{
					lstEntidadesAlmacenaje = (EntidadBean[]) 
								Arrays.copyOf(lstEntidadesAlmacenaje,	lstEntidadesAlmacenaje.length+1);
					lstEntidadesAlmacenaje[lstEntidadesAlmacenaje.length-1] = cloneDatosEntidadesGraficosPaneles(entidadPanel);
				}
				
			
			}
			if(listEntidadesArbol[i].getLstEntidadesHijas() != null &&
					listEntidadesArbol[i].getLstEntidadesHijas().length != 0){
				
				guardarDatosEntidadesGraficasModificadasBean(listEntidadesArbol[i].getLstEntidadesHijas());
			}	
		}
	}
	
	public static void guardarDatosEntidadesAltaBean(EntidadPanelBean[] listEntidadesArbol){
		
		for(int i=0; i< listEntidadesArbol.length; i++){
			EntidadPanelBean entidadPanel = listEntidadesArbol[i];
			
			if(entidadPanel.isNueva() && !entidadPanel.isEliminada()	){
				
				if(lstEntidadesAltaAlmacenaje == null){
					lstEntidadesAltaAlmacenaje = new EntidadBean[1];
					lstEntidadesAltaAlmacenaje[0] = cloneDatosPaneles(entidadPanel);
				}
				else{
					lstEntidadesAltaAlmacenaje = (EntidadBean[]) 
								Arrays.copyOf(lstEntidadesAltaAlmacenaje,	lstEntidadesAltaAlmacenaje.length+1);
					lstEntidadesAltaAlmacenaje[lstEntidadesAltaAlmacenaje.length-1] = cloneDatosPaneles(entidadPanel);
				}
				
				
			}
			if(listEntidadesArbol[i].getLstEntidadesHijas() != null &&
					listEntidadesArbol[i].getLstEntidadesHijas().length != 0){
				
				guardarDatosEntidadesAltaBean(listEntidadesArbol[i].getLstEntidadesHijas());
			}	
		}
	}
	
	public static void guardarDatosEntidadesBajaBean(EntidadPanelBean[] listEntidadesArbol){
		
		for(int i=0; i< listEntidadesArbol.length; i++){
			EntidadPanelBean detEntidad = listEntidadesArbol[i];
			
			if(!detEntidad.isNueva() && detEntidad.isEliminada()	){
				
				if(lstEntidadesBajaAlmacenaje == null){
					lstEntidadesBajaAlmacenaje = new EntidadBean[1];
					lstEntidadesBajaAlmacenaje[0] = cloneDatosPaneles(detEntidad);
				}
				else{
					lstEntidadesBajaAlmacenaje = (EntidadBean[]) 
								Arrays.copyOf(lstEntidadesBajaAlmacenaje,	lstEntidadesBajaAlmacenaje.length+1);
					lstEntidadesBajaAlmacenaje[lstEntidadesBajaAlmacenaje.length-1] = cloneDatosPaneles(detEntidad);
				}
				
				
			}
			if(listEntidadesArbol[i].getLstEntidadesHijas() != null &&
					listEntidadesArbol[i].getLstEntidadesHijas().length != 0){
				
				guardarDatosEntidadesBajaBean(listEntidadesArbol[i].getLstEntidadesHijas());
			}	
		}
	}
	
	public static void guardarDatosEntidadesModificadasBean(EntidadPanelBean[] listEntidadesArbol){
		
		
		for(int i=0; i< listEntidadesArbol.length; i++){
			EntidadPanelBean entidadPanel = listEntidadesArbol[i];
			
			if(entidadPanel.isModificada() &&
					(!entidadPanel.isNueva() && !entidadPanel.isEliminada())){
				if(lstEntidadesAlmacenaje == null){
					lstEntidadesAlmacenaje = new EntidadBean[1];
					lstEntidadesAlmacenaje[0] = cloneDatosPaneles(entidadPanel);
				}
				else{
					lstEntidadesAlmacenaje = (EntidadBean[]) 
								Arrays.copyOf(lstEntidadesAlmacenaje,	lstEntidadesAlmacenaje.length+1);
					lstEntidadesAlmacenaje[lstEntidadesAlmacenaje.length-1] = cloneDatosPaneles(entidadPanel);
				}
				
			
			}
			if(listEntidadesArbol[i].getLstEntidadesHijas() != null &&
					listEntidadesArbol[i].getLstEntidadesHijas().length != 0){
				
				guardarDatosEntidadesModificadasBean(listEntidadesArbol[i].getLstEntidadesHijas());
			}	
		}
	}

	private static EntidadBean cloneDatosEntidadesGraficosPaneles(EntidadPanelBean entidadPanelBean){
		
		EntidadBean entidadGraficaBean = new EntidadBean();
		
		entidadGraficaBean.setId(entidadPanelBean.getId());
		
		entidadGraficaBean.setIdFeature(entidadPanelBean.getIdFeature());
		entidadGraficaBean.setIdLayer(entidadPanelBean.getIdLayer());
				
		return  entidadGraficaBean;
	}
	
	private static EntidadBean cloneDatosPaneles(EntidadPanelBean entidadPanelBean){
		
		EntidadBean entidadBean = new EntidadBean();
		
		entidadBean.setId(entidadPanelBean.getId());
		entidadBean.setCodigo(entidadPanelBean.getCodigo());
		entidadBean.setClave(entidadPanelBean.getClave());
		entidadBean.setNombre(entidadPanelBean.getNombre());
		if(entidadPanelBean.getEtiqueta() != null &&
				!entidadPanelBean.getEtiqueta().equals("null")){
			entidadBean.setEtiqueta(entidadPanelBean.getEtiqueta());
		}
		else{
			entidadBean.setEtiqueta("");
		}
		entidadBean.setSuspendida(entidadPanelBean.isSuspendida());
		entidadBean.setMadre(entidadPanelBean.getMadre());
		entidadBean.setTramite(entidadPanelBean.getTramite());
		
		if(entidadPanelBean.getBaseBean() != null && 
				entidadPanelBean.getBaseBean().getEntidad() != null &&
				entidadPanelBean.getBaseBean().getEntidad().getId() != 0){
			entidadBean.setBase_entidadid(entidadPanelBean.getBaseBean().getEntidad().getId());
		}
		
		
		if(entidadPanelBean.getLstDocumentosPanelBean() != null){

			DocumentoEntidadBean[] lstDocumentoAlta = null;
			DocumentoEntidadBean[] lstDocumentoBaja = null;
			
			
			int index = 0;
			
			// Documentos para dar de alta
			// grupo aplicacion para dar de alta
			if(entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta() != null){
				index = entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta().length;
				for(int i=0; i<index; i++){
				
					DocumentoPanelBean documentoAlta = 
						entidadPanelBean.getLstDocumentosPanelBean().getLstDocumentoAsocAlta()[i];
		
					if(lstDocumentoAlta == null){
						
						lstDocumentoAlta = new DocumentoEntidadBean[1];
						lstDocumentoAlta[0] = cloneDocumentoPanelBeanToDocumentoBean(
								documentoAlta, entidadPanelBean, 
								entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta()[i]);
						
					}
					else{
						lstDocumentoAlta = (DocumentoEntidadBean[]) 
									Arrays.copyOf(lstDocumentoAlta,	lstDocumentoAlta.length+1);
					
						lstDocumentoAlta[lstDocumentoAlta.length-1] = cloneDocumentoPanelBeanToDocumentoBean(
								documentoAlta, entidadPanelBean, 
								entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocAlta()[i]);
					}
				}
			}
			
			entidadBean.setLstDocumentoAlta(lstDocumentoAlta);
		
		
			// Documentos para dar de baja
			if(entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar() != null){
				index = entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar().length;
				for(int i=0; i<index; i++){
	
					DocumentoEntidadBean documentoBaja = new DocumentoEntidadBean();
					
					documentoBaja.setId(entidadPanelBean.getLstDocumentosPanelBean().getLstIDAsocEliminar()[i]);
	
					if(lstDocumentoBaja == null){
			
						lstDocumentoBaja = new DocumentoEntidadBean[1];
						lstDocumentoBaja[0] = documentoBaja;
					
					}
					else{
	
						lstDocumentoBaja = (DocumentoEntidadBean[]) 
									Arrays.copyOf(lstDocumentoBaja,	lstDocumentoBaja.length+1);
					
						lstDocumentoBaja[lstDocumentoBaja.length-1] = documentoBaja;
					}
					
				}
			}
			
			entidadBean.setLstDocumentoBaja(lstDocumentoBaja);
		}
		
		
		//Operacion
		if(entidadPanelBean.getLstOperacionEntidad() != null){
			
			entidadBean.setLstOperacionEntidad(entidadPanelBean.getLstOperacionEntidad());
		}
		
		
		return  entidadBean;
	}
	
	private static DocumentoEntidadBean cloneDocumentoPanelBeanToDocumentoBean(
			DocumentoPanelBean documentoAsoc ,
			EntidadPanelBean entidadPanelBeanTrabajo,
			int idDocAsoc){
		
		DocumentoEntidadBean documentoBean = new DocumentoEntidadBean();
		
		documentoBean.setId(idDocAsoc);
		documentoBean.setDeterminacion(entidadPanelBeanTrabajo.getId());
		documentoBean.setDocumentoid(documentoAsoc.getDocumentoid());
	
		return documentoBean;
	}

	public static EntidadBean[] getLstEntidadesAlmacenaje() {
		return lstEntidadesAlmacenaje;
	}

	public static void setLstEntidadesAlmacenaje(
			EntidadBean[] lstEntidadesAlmacenaje) {
		OperacionesEntidades.lstEntidadesAlmacenaje = lstEntidadesAlmacenaje;
	}

	public static EntidadBean[] getLstEntidadesAltaAlmacenaje() {
		return lstEntidadesAltaAlmacenaje;
	}

	public static void setLstEntidadesAltaAlmacenaje(
			EntidadBean[] lstEntidadesAltaAlmacenaje) {
		OperacionesEntidades.lstEntidadesAltaAlmacenaje = lstEntidadesAltaAlmacenaje;
	}

	public static EntidadBean[] getLstEntidadesBajaAlmacenaje() {
		return lstEntidadesBajaAlmacenaje;
	}

	public static void setLstEntidadesBajaAlmacenaje(
			EntidadBean[] lstEntidadesBajaAlmacenaje) {
		OperacionesEntidades.lstEntidadesBajaAlmacenaje = lstEntidadesBajaAlmacenaje;
	}
}
