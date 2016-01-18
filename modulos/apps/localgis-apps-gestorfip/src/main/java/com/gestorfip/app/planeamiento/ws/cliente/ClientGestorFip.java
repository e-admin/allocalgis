/**
 * ClientGestorFip.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.ws.cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.diccionario.CaracteresDeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.FipPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.utils.ConstantesGestorFIP;
import com.gestorfip.app.planeamiento.ws.cliente.utils.CloneObjectWSToAplicacion;

import es.gestorfip.serviciosweb.ServicesStub;
import es.gestorfip.serviciosweb.ServicesStub.CRSGestor;
import es.gestorfip.serviciosweb.ServicesStub.CaracteresDeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.CondicionUrbanisticaCasoRegimenesBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfLayerBean;
import es.gestorfip.serviciosweb.ServicesStub.ConfiguracionGestor;
import es.gestorfip.serviciosweb.ServicesStub.DeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.EntidadBean;
import es.gestorfip.serviciosweb.ServicesStub.FipBean;
import es.gestorfip.serviciosweb.ServicesStub.TipoOperacionDeterminacionBean;
import es.gestorfip.serviciosweb.ServicesStub.TipoOperacionEntidadBean;
import es.gestorfip.serviciosweb.ServicesStub.TramiteBean;
import es.gestorfip.serviciosweb.ServicesStub.UnidadBean;
import es.gestorfip.serviciosweb.ServicesStub.VersionesUER;


public class ClientGestorFip extends javax.swing.JPanel {

	
	static Logger logger = Logger.getLogger(ClientGestorFip.class);
	static int WS_TIMEOUT = 300000; // default value
	static int WS_TIMEOUTFIP = 300000; // default value
	
	static String targetEndPoint = "";


	/**********************************************************************************************
	 * 				TRAMITES
	 **********************************************************************************************/

	
	/**********************************************************************************************
	 * 				FICHEROS
	 **********************************************************************************************/
	
	
	public static String getTargetEndPoint() {
		return targetEndPoint;
	}

	public static void setTargetEndPoint(String targetEndPoint) {
		ClientGestorFip.targetEndPoint = targetEndPoint + "/localgis-gestorfip-ws/services/Services/";
	}

	/**********************************************************************************************
	 * 				DETERMINACIONES
	 **********************************************************************************************/
	
	
	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @param fbted: Filtro de b�squeda
	 * @param ccaaSeleccionada
	 * @return ArrayList: Lista que contiene los beans representando las comunidades
	 * @throws Exception
	 */
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[]  
	                        obtenerDeterminacionesAsociadasFip(
	                        		ApplicationContext app, int idTramite, boolean isModificable)
			throws Exception {
		Date fecha = new Date(System.currentTimeMillis());
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDeterminacionesAsociadasFip request = null;
		ServicesStub.ObtenerDeterminacionesAsociadasFipResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDeterminacionesAsociadasFip();
			request.setIdTramite(idTramite);
			// invocamos al web service
			response = customer.obtenerDeterminacionesAsociadasFip(request);
			DeterminacionBean[] str = response.get_return();
			
			fecha = new Date(System.currentTimeMillis());
	
			if (str != null) {
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				for (int i = 0; i < str.length; i++) {
					
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSValoresReferenciaBeanToValoresReferenciaBean(detBean, str[i]);			
						CloneObjectWSToAplicacion.cloneWSGrupoAplicacionBeanToGrupoAplicacionBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSDeterminacionesReguladorasBeanToDeterminacionesReguladorasBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSRegulacionesEspecificasBeanToRegulacionesEspecificasBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSDocumentosDeterminacionBeanToDocumentosDeterminacionBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSOperacionDeterminacionBeanToOperacionDeterminacionBean(detBean, str[i]);
						lstDeterminaciones[i] = detBean;
					}
	
				}
			}
		
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las determinaciones asociadas a un fip");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		
		return lstDeterminaciones;
		
	}
	
	
	
	

	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[]  
	                        obtenerArbolDeterminacionesAsociadasTramite(ApplicationContext app, int idTramite, boolean isModificable)
			throws Exception {
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerArbolDeterminacionesAsociadasTramite request = null;
		ServicesStub.ObtenerArbolDeterminacionesAsociadasTramiteResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerArbolDeterminacionesAsociadasTramite();
			request.setIdTramite(idTramite);
			// invocamos al web service
			response = customer.obtenerArbolDeterminacionesAsociadasTramite(request);
			DeterminacionBean[] str = response.get_return();
			

			if (str != null) {
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				for (int i = 0; i < str.length; i++) {
					
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						
						lstDeterminaciones[i] = detBean;
					}
	
				}
			}
	
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las determinaciones asociadas a un tramite");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		
		return lstDeterminaciones;
		
	}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.TramiteBean
	obtenerTramitesEncargadoAsociadosFip(int idFip, ApplicationContext app)
		throws Exception {
		
		com.gestorfip.app.planeamiento.beans.tramite.TramiteBean tramite = 
					new com.gestorfip.app.planeamiento.beans.tramite.TramiteBean();
		
		ServicesStub customer = null;
		ServicesStub.ObtenerTramitesEncargadoAsociadosFip request = null;
		ServicesStub.ObtenerTramitesEncargadoAsociadosFipResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			
			
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerTramitesEncargadoAsociadosFip();
			request.setIdFip(idFip);
			
			// invocamos al web service
			response = customer.obtenerTramitesEncargadoAsociadosFip(request);
			TramiteBean str = response.get_return();
			
			
			if (str != null) {
				tramite.setId(str.getId());
				tramite.setCodigo(str.getCodigo());
				tramite.setTexto(str.getTexto());
				tramite.setNombre(str.getNombre());
				
			}
			
		}
		 catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de tramites");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return tramite;

		}


	/**
	 * 
	 * @param nombreSecuencia: nombre de la secuencia a obtener
	 * @return int: valor de la secuencia
	 * @throws RemoteException 
	 * @throws Exception
	 */
	public static int obtenerValorSecuencia(String nombreSecuencia,
												ApplicationContext app) throws Exception{
	
		int valorSecuencia = -1;
		ServicesStub customer = null;
		ServicesStub.ObtenerValorSecuencia request = null;
		ServicesStub.ObtenerValorSecuenciaResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerValorSecuencia();
			request.setNombreSecuencia(nombreSecuencia);
			
			// invocamos al web service
			response = customer.obtenerValorSecuencia(request);
			valorSecuencia = response.get_return();
			
		}
		catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener el valor de la secuencia -" +nombreSecuencia);
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		catch (Exception exception) {
			System.err.println(exception.toString());
			logger.error("Exception al obtener el valor de la secuencia -" +nombreSecuencia);
			logger.error(exception.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw exception;
			
		}
		return valorSecuencia;
	}	

	
	/**********************************************************************************************
	 * 				ENTIDADES
	 **********************************************************************************************/
	

	
	public static com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[]  
	                 obtenerArbolEntidadesAsociadasTramite(ApplicationContext app, int idTramite, boolean isModificable)
			throws Exception {
		
		com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[] lstEntidades = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerArbolEntidadesAsociadasTramite request = null;
		ServicesStub.ObtenerArbolEntidadesAsociadasTramiteResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerArbolEntidadesAsociadasTramite();
			request.setIdTramite(idTramite);
			
			// invocamos al web service
			response = customer.obtenerArbolEntidadesAsociadasTramite(request);
			EntidadBean[] str = response.get_return();
			

			if (str != null) {
				lstEntidades = 
					new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[str.length];
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
				
						com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean();
						CloneObjectWSToAplicacion.cloneWSEntidadBeanToEntidadBean(entBean, str[i]);
						lstEntidades[i] = entBean;
					}
				}
			}

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener el arbol de  entidades asociadas a un tramite");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		
		return lstEntidades;
		
	}
	
		/**
	 * 
	 * @param idccaa: identificador de la comunidad autonoma
	 * @return boolean : estado correcto o fallido del almacenaje de datos
	 * @throws Exception
	 */
	
	
	/**********************************************************************************************
	 * 				FIN ENTIDADES GRAFICAS
	 **********************************************************************************************/
	
	public static ArrayList obtenerTiposOperacionesDeterminaciones(
			int idFip,	ApplicationContext app) throws Exception{
		
		ArrayList lstTiposOperaciones = new ArrayList();
		
		ServicesStub customer = null;
		ServicesStub.ObtenerLstTiposOperacionesDeterminaciones request = null;
		ServicesStub.ObtenerLstTiposOperacionesDeterminacionesResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerLstTiposOperacionesDeterminaciones();
			request.setIdFip(idFip);
			
			// invocamos al web service
			response = customer.obtenerLstTiposOperacionesDeterminaciones(request);
			TipoOperacionDeterminacionBean[] str = response.get_return();
			
			
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					
					com.gestorfip.app.planeamiento.beans.tramite.TipoOperacionesDeterminacionBean tob = 
							new com.gestorfip.app.planeamiento.beans.tramite.TipoOperacionesDeterminacionBean();
					tob.setId(str[i].getId());
					tob.setCodigo(str[i].getCodigo());
					tob.setNombre(str[i].getNombre());
					tob.setFip(str[i].getFip());
					
					lstTiposOperaciones.add(tob);
					
				}
			
			}
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de los tipos de operaciones de determinaciones");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstTiposOperaciones;
		
	}
	
	
	public static ArrayList obtenerTiposOperacionesEntidades(
			int  idFip,	ApplicationContext app) throws Exception{
		
		ArrayList lstTiposOperaciones = new ArrayList();
		
		ServicesStub customer = null;
		ServicesStub.ObtenerLstTiposOperacionesEntidades request = null;
		ServicesStub.ObtenerLstTiposOperacionesEntidadesResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerLstTiposOperacionesEntidades();
			request.setIdFip(idFip);
			
			// invocamos al web service
			response = customer.obtenerLstTiposOperacionesEntidades(request);
			TipoOperacionEntidadBean[] str = response.get_return();
			
			
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					
					com.gestorfip.app.planeamiento.beans.tramite.TipoOperacionesEntidadBean tob = 
							new com.gestorfip.app.planeamiento.beans.tramite.TipoOperacionesEntidadBean();
					tob.setId(str[i].getId());
					tob.setCodigo(str[i].getCodigo());
					tob.setNombre(str[i].getNombre());
					tob.setFip(str[i].getFip());
					
					lstTiposOperaciones.add(tob);
					
				}
			
			}
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de los tipos de operaciones de determinaciones");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstTiposOperaciones;
		
	}

	/***************************************************************************************
	 * 							DETERMINACIONES-ENTIDADES
	 **************************************************************************************/
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[] 
	              obtenerEntidadesAsociadasToDeterminacion(int idDeterminacion ,	
			ApplicationContext app) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[] lstEntidades = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerEntidadesAsociadasToDeterminacion request = null;
		ServicesStub.ObtenerEntidadesAsociadasToDeterminacionResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerEntidadesAsociadasToDeterminacion();
			
			request.setIdDeterminacion(idDeterminacion);
			// invocamos al web service
			response = customer.obtenerEntidadesAsociadasToDeterminacion(request);
			EntidadBean[] str = response.get_return();
			

			if (str != null) {
				
				lstEntidades = 
					new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
				
					com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entBean = 
						new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean();

					CloneObjectWSToAplicacion.cloneWSEntidadBeanToEntidadBean(entBean, str[i]);
					  
					lstEntidades[i] = entBean;
					}
				}
				
			}
			
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de Entidades asociadas a una Determinacion");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstEntidades;
	}

	/***************************************************************************************
	 * 							FIN ASOCIACION DETERMINACIONES-ENTIDADES
	 **************************************************************************************/
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] 
	                  obtenerDeterminacionesByTipoCaracterValorReferencia_EnCondicionesUrbanisticas(FipPanelBean fip ,	
	            		  ApplicationContext app, String caracterDeterminacion, DeterminacionPanelBean[] lstDeterminaciones) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminacionesValRef = null;
		 
		ServicesStub customer = null;
		ServicesStub.ObtenerDetValoresReferenciaCondicionUrbanistica request = null;
		ServicesStub.ObtenerDetValoresReferenciaCondicionUrbanisticaResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		
		try { 
			DeterminacionBean[] lstDeterminacionesWS = null;
			
			for(int i=0; i<lstDeterminaciones.length; i++){
				DeterminacionBean strDet = new DeterminacionBean();
				
				strDet.setId(lstDeterminaciones[i].getId());
				if(lstDeterminacionesWS == null){
					
					lstDeterminacionesWS = new DeterminacionBean[1];
					lstDeterminacionesWS[0] = strDet;
				
				}
				else{
					lstDeterminacionesWS = (DeterminacionBean[]) Arrays.copyOf(lstDeterminacionesWS, 
							lstDeterminacionesWS.length+1);
				
					lstDeterminacionesWS[lstDeterminacionesWS.length-1] = strDet;
				}
			}
				
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDetValoresReferenciaCondicionUrbanistica();
			
			int idCatalogoSistematizado = 0;
			int idPlaneamientoVigente = 0;
			int idPlaneamientoEncargado = 0;
			
			if(fip.getTramiteCatalogoSistematizado() != null){
				idCatalogoSistematizado = fip.getTramiteCatalogoSistematizado().getId();
			}
			if(fip.getTramitePlaneamientoVigente() != null){
				idPlaneamientoVigente = fip.getTramitePlaneamientoVigente().getId();
			}
			if(fip.getTramitePlaneamientoEncargado() != null){
				idPlaneamientoEncargado = fip.getTramitePlaneamientoEncargado().getId(); 
			}
			
			request.setIdTramiteCatalogoSistematizado(idCatalogoSistematizado);
			request.setIdTramitePlaneamientoVigente(idPlaneamientoVigente);
			request.setIdTramitePlaneamientoEncargado(idPlaneamientoEncargado);
			request.setLstDeterminaciones(lstDeterminacionesWS);
			// invocamos al web service
			response = customer.obtenerDetValoresReferenciaCondicionUrbanistica(request);
			DeterminacionBean[] str = response.get_return();
			

			if (str != null) {
				
				lstDeterminacionesValRef = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						  
						lstDeterminacionesValRef[i] = detBean;
					}
				}
			}
			
				
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de Determinaciones de caracter determinacion unidad");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstDeterminacionesValRef;
	}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] 
	                    obtenerDetAplicablesEntidad(int idEntidad,  ApplicationContext app) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDetAplicablesEntidad request = null;
		ServicesStub.ObtenerDetAplicablesEntidadResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try { 
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDetAplicablesEntidad();
			request.setIdEntidad(idEntidad);

			response = customer.obtenerDetAplicablesEntidad(request);
			DeterminacionBean[] str = response.get_return();

			if (str != null) {
				
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						  
						lstDeterminaciones[i] = detBean;
					}
				}
			}
			
        } catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
   			logger.error("Exception al obtener la lista de Determinaciones aplicables a la entidad");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
			"Se ha pruducido un error en la comunicación con el servidor", "Error",
			JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		return lstDeterminaciones;
	}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] 
	                  obtenerDeterminacionesByTipoCaracterDeterminacion_EnCondicionesUrbanisticas(FipPanelBean fip ,	
	            		  ApplicationContext app, String caracterDeterminacion) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		 
		ServicesStub customer = null;
		ServicesStub.ObtenerDetByTipoCaracterCondUrban request = null;
		ServicesStub.ObtenerDetByTipoCaracterCondUrbanResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try { 
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDetByTipoCaracterCondUrban();
			
			int idCatalogoSistematizado = 0;
			int idPlaneamientoVigente = 0;
			int idPlaneamientoEncargado = 0;
			
			if(fip.getTramiteCatalogoSistematizado() != null){
				idCatalogoSistematizado = fip.getTramiteCatalogoSistematizado().getId();
			}
			if(fip.getTramitePlaneamientoVigente() != null){
				idPlaneamientoVigente = fip.getTramitePlaneamientoVigente().getId();
			}
			if(fip.getTramitePlaneamientoEncargado() != null){
				idPlaneamientoEncargado = fip.getTramitePlaneamientoEncargado().getId(); 
			}
			
			request.setIdFip(fip.getId());
			request.setIdTramiteCatalogoSistematizado(idCatalogoSistematizado);
			request.setIdTramitePlaneamientoVigente(idPlaneamientoVigente);
			request.setIdTramitePlaneamientoEncargado(idPlaneamientoEncargado);
			request.setCaracterDeterminacionProperty(caracterDeterminacion);
			// invocamos al web service
			response = customer.obtenerDetByTipoCaracterCondUrban(request);
			DeterminacionBean[] str = response.get_return();
			

			if (str != null) {
				
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						  
						lstDeterminaciones[i] = detBean;
					}
				}
			}
			
				
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de Determinaciones de caracter determinacion unidad");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstDeterminaciones;
	}

	
	/**********************************************************************************************
	 * 				 INICIO LOURED
	 * 
	 **********************************************************************************************/
	
	
	public static String ObtenerFechaFipXML(String xml, String encoding, ApplicationContext app)throws Exception {
		
		String fecha = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerFechaFipXML request = null;
		ServicesStub.ObtenerFechaFipXMLResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));
		try {
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			// Establecemos los parametros de la invocacion
			request = new ServicesStub.ObtenerFechaFipXML();
			request.setXmlFichFIP(xml);
			request.setEncoding(encoding);
		
			response = customer.obtenerFechaFipXML(request);
			if(response != null){
				fecha = response.get_return();
			}
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la fecha del xml");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha producido un error en la comunicación con el servidor: \n"+excepcionDeInvocacion.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return fecha;
	}
	
	
	public static ConfLayerBean[] obtenerDatosMigracionAsistida(String xml, String encoding, ApplicationContext app)throws Exception {
		
		ConfLayerBean[] lstConfLayerBean = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerDatosMigracionAsistida request = null;
		ServicesStub.ObtenerDatosMigracionAsistidaResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));
		try {
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			// Establecemos los parametros de la invocacion
			request = new ServicesStub.ObtenerDatosMigracionAsistida();
			request.setXmlFichFIP(xml);
			request.setEncoding(encoding);
		
			response = customer.obtenerDatosMigracionAsistida(request);
			if(response != null){
				lstConfLayerBean = response.get_return();
			}
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la configuracion de los datos de Migracion");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstConfLayerBean;
	}
	
	/**
	 * 
	 * @param xml: xml del fichero FIP1
	 * @param app: Contexto de aplicacion
	 * @return String: the response code with the associated error message
	 * @throws Exception
	 */
	public static String insertarFIP1(int idAmbito, String xml, String nameFileFip, 
			String encoding, ApplicationContext app, int idEntidad) throws Exception {

		ServicesStub customer = null;
		ServicesStub.InsertarFIP1 request = null;
		ServicesStub.InsertarFIP1Response response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		int idMunicipio = new Integer(app.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)).intValue();

		try {
			// Creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			// Establecemos los parametros de la invocacion
			request = new ServicesStub.InsertarFIP1();
			request.setXmlFichFIP(xml);
			request.setNameFileFip(nameFileFip);
			request.setIdAmbito(idAmbito);
			request.setIdMunicipioLG(idMunicipio);
			request.setIdEntidad(idEntidad);
			ConfLayerBean[] lstConfLayerBean = (ConfLayerBean[]) app.getBlackboard().get(ConstantesGestorFIP.GESTORFIP_DATA_MIGRACION_ASISTIDA);
			request.setLstConfLayerBean(lstConfLayerBean);
			request.setEncoding(encoding);
			// Invocamos al web service
			response = customer.insertarFIP1(request);

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al insertar el Fip1");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}

		return response.get_return();
	}
	
	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @return String: fecha del fip
	 * @throws Exception
	 */
	public static String obtenerFechaConsolidacionFip(int idAmbito, ApplicationContext app) throws Exception {

		String fecha = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerFechaConsolidacionFip request = null;
		ServicesStub.ObtenerFechaConsolidacionFipResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		try {
			// Creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			// Establecemos los parametros de la invocacion
			request = new ServicesStub.ObtenerFechaConsolidacionFip();
			request.setIdAmbito(idAmbito);
			// Invocamos al web service
			response = customer.obtenerFechaConsolidacionFip(request);
			fecha = response.get_return();

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la ultima fecha del Fip1 en la BBDD");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		return fecha;
	}
	
	
	public static ArrayList obtenerLstFips(String codAmbito, ApplicationContext app) throws Exception{
		
		ArrayList<FipPanelBean> lstFips = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerLstFips request = null;
		ServicesStub.ObtenerLstFipsResponse response  = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));
		
		try { 
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerLstFips();
			request.setCodAmbito(codAmbito);
		
			// invocamos al web service
			response = customer.obtenerLstFips(request);
			FipBean[] str = response.get_return();
		
			if (str != null) {
				lstFips = new ArrayList<FipPanelBean>();
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
						FipPanelBean fipPanelBean =  new FipPanelBean();
				
						fipPanelBean.setId(str[i].getId());
						fipPanelBean.setPais(str[i].getPais());
						fipPanelBean.setSrs(str[i].getSrs());
						fipPanelBean.setVersion(str[i].getVersion());
						fipPanelBean.setFecha(str[i].getFecha());
						fipPanelBean.setFechaConsolidacion(str[i].getFechaConsolidacion());
						fipPanelBean.setMunicipio(str[i].getMunicipio());	
						
						TramitePanelBean tcs = new TramitePanelBean();
						tcs.setId(str[i].getTramiteCatalogoSistematizado().getId());
						TramitePanelBean tpv = new TramitePanelBean();
						tpv.setId(str[i].getTramitePlaneamientoVigente().getId());
						tpv.setCodigo(str[i].getTramitePlaneamientoVigente().getCodigo());
						fipPanelBean.setTramiteCatalogoSistematizado(tcs);
						fipPanelBean.setTramitePlaneamientoVigente(tpv);
											
						lstFips.add(fipPanelBean);
					}
				}
			}
		
		
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de Fips");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",JOptionPane.ERROR_MESSAGE);
		throw excepcionDeInvocacion;
		}
		
		return lstFips;
	}
	
	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @param fbted: Filtro de b�squeda
	 * @param ccaaSeleccionada
	 * @return ArrayList: Lista que contiene los beans representando las comunidades
	 * @throws Exception
	 */
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[]  
	                        obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(
	                        		ApplicationContext app, int idFip, int idTramite)
			throws Exception {
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDeterminacionesAsociadasFip_GestorPlaneamiento request = null;
		ServicesStub.ObtenerDeterminacionesAsociadasFip_GestorPlaneamientoResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDeterminacionesAsociadasFip_GestorPlaneamiento();
			request.setIdFip(idFip);
			request.setIdTramite(idTramite);
			// invocamos al web service
			response = customer.obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(request);
			DeterminacionBean[] str = response.get_return();

			if (str != null) {
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				for (int i = 0; i < str.length; i++) {
					
					if(str[i] != null){
						com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
							new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();

						CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSGrupoAplicacionBeanToGrupoAplicacionBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSValoresReferenciaBeanToValoresReferenciaBean(detBean, str[i]);
						CloneObjectWSToAplicacion.cloneWSRegulacionesEspecificasBeanToRegulacionesEspecificasBean(detBean, str[i]);
						lstDeterminaciones[i] = detBean;
					}
	
				}
			}
		
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las determinaciones asociadas a un fip de condiciones urbanisticas");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		return lstDeterminaciones;
		
	}
	
	
	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @param fbted: Filtro de b�squeda
	 * @param ccaaSeleccionada
	 * @return ArrayList: Lista que contiene los beans representando las comunidades
	 * @throws Exception
	 */
	public static com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[]  
	                        obtenerEntidadesAsociadasFip_GestorPlaneamiento(ApplicationContext app, int idFip, int idTramite)
			throws Exception {
		
		com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[] lstEntidades = null;

		ServicesStub customer = null;
		ServicesStub.ObtenerEntidadesAsociadasFip_GestorPlaneamiento request = null;
		ServicesStub.ObtenerEntidadesAsociadasFip_GestorPlaneamientoResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerEntidadesAsociadasFip_GestorPlaneamiento();
			//request.setIdTramite(param)f(idFip);
			request.setIdTramite(idTramite);
			request.setIdFip(idFip);
			// invocamos al web service
			response = customer.obtenerEntidadesAsociadasFip_GestorPlaneamiento(request);
			EntidadBean[] str = response.get_return();

			if (str != null) {
				lstEntidades = 
					new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean[str.length];
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
				
					com.gestorfip.app.planeamiento.beans.tramite.EntidadBean entBean = 
						new com.gestorfip.app.planeamiento.beans.tramite.EntidadBean();

					/*boolean isModificable = false;
					if(str[i].getTramite() == idTramite){
						isModificable = true;
					}*/
					
					CloneObjectWSToAplicacion.cloneWSEntidadBeanToEntidadBean(entBean, str[i]);
//					CloneObjectWSToAplicacion.cloneWSDocumentosEntidadBeanToDocumentosEntidadBean(entBean, str[i]);
//					CloneObjectWSToAplicacion.cloneWSOperacionEntidadBeanToOperacionEntidadBean(entBean, str[i]);
//											  
					lstEntidades[i] = entBean;
					}
				}
			}
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las entidades asociadas a un tramite");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		catch (Exception e) {
			System.err.println(e.toString());
			logger.error("Exception al obtener las entidades asociadas a un tramite");
			logger.error(e.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw e;
		}

		return lstEntidades;
		
	}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] 
            obtenerDeterminacionesAsociadasToEntidad(int idEntidad ,	
          		  ApplicationContext app) throws Exception{

		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[] lstDeterminaciones = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDeterminacionesAsociadasToEntidad request = null;
		ServicesStub.ObtenerDeterminacionesAsociadasToEntidadResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDeterminacionesAsociadasToEntidad();
			
			request.setIdEntidad(idEntidad);
			// invocamos al web service
			response = customer.obtenerDeterminacionesAsociadasToEntidad(request);
			DeterminacionBean[] str = response.get_return();
			
	
			if (str != null) {
				
				lstDeterminaciones = 
					new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
				
					com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = 
						new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean();
	
					CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str[i]);
					  
					lstDeterminaciones[i] = detBean;
					}
				}
				
			}	
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la lista de Determinaciones asociadas a una Entidad");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstDeterminaciones;
	}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean[] 
            obtenerCasosCondicionUrbanistica(int idDeterminacion, int idEntidad ,	
		ApplicationContext app) throws Exception{
	
		com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean[] lstcuc = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerCasosCondicionUrbanistica request = null;
		ServicesStub.ObtenerCasosCondicionUrbanisticaResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerCasosCondicionUrbanistica();
			
			request.setIdDeterminacion(idDeterminacion);
			request.setIdEntidad(idEntidad);
			// invocamos al web service
			response = customer.obtenerCasosCondicionUrbanistica(request);
			CondicionUrbanisticaCasoBean[] str = response.get_return();
			
	
			if (str != null && str[0] != null) {
				
				lstcuc = 
					new com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean[str.length];
				
				for (int i = 0; i < str.length; i++) {
					if(str[i] != null){
				
					com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean cuc = 
						new com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoBean();
	
					CloneObjectWSToAplicacion.
							cloneWSCondicionUrbanisticaCasoBeanToCondicionUrbanisticaCasoBean(cuc, str[i]);
					  
					lstcuc[i] = cuc;
					}
				}
			}
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los casos de la condicion urbanistica");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstcuc;
	}
	
	public static com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean[] 
            obtenerRegimenesCasoCondicionUrbanistica(int idCaso ,	
	ApplicationContext app) throws Exception{

	com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean[] lstcucr = null;
	
	ServicesStub customer = null;
	ServicesStub.ObtenerRegimenesCasoCondicionUrbanistica request = null;
	ServicesStub.ObtenerRegimenesCasoCondicionUrbanisticaResponse response = null;
	HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
	WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
	try {
		
		// creamos el soporte
		customer = new ServicesStub(targetEndPoint);
		// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
		customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
		// establecemos el parametro de la invocacion
		request = new ServicesStub.ObtenerRegimenesCasoCondicionUrbanistica();
		
		request.setIdCaso(idCaso);
		// invocamos al web service
		response = customer.obtenerRegimenesCasoCondicionUrbanistica(request);
		CondicionUrbanisticaCasoRegimenesBean[] str = response.get_return();
		

		if (str != null) {
			
			lstcucr = 
				new com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean[str.length];
			
			for (int i = 0; i < str.length; i++) {
				if(str[i] != null){
			
					com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean cucr = 
					new com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenesBean();

					CloneObjectWSToAplicacion.
						cloneWSCondicionUrbanisticaCasoRegimenesBeanToCondicionUrbanisticaCasoRegimenesBean(cucr, str[i]);
					
					//se utiliza para que se muestre en la tabla de regimenes
					// ya que ninguno de sus campos es obligatorio
					cucr.setNombre(String.valueOf(i+1));
				  
					lstcucr[i] = cucr;
				}
			}
			
		}
		
		
		
	} catch (RemoteException excepcionDeInvocacion) {
		System.err.println(excepcionDeInvocacion.toString());
		logger.error("Exception al obtener los regimenes condicion urbanistica");
		logger.error(excepcionDeInvocacion.getMessage());
		JOptionPane.showMessageDialog(app.getMainFrame(),
				"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstcucr;
	}

	
	public static com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean 
      obtenerRegimenRegimenesEspecificosCasoCondicionUrbanistica(int idRegimen ,	
		ApplicationContext app) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerRegimenEspecificoRegimenCasoCondicionUrbanistica request = null;
		ServicesStub.ObtenerRegimenEspecificoRegimenCasoCondicionUrbanisticaResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerRegimenEspecificoRegimenCasoCondicionUrbanistica();
			
			request.setIdRegimen(idRegimen);
			// invocamos al web service
			response = customer.obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(request);
			CondicionUrbanisticaCasoRegimenRegimenesBean str = response.get_return();
			
		
			if (str != null) {
			  				
				cucrr = new 
					com.gestorfip.app.planeamiento.beans.tramite.CondicionUrbanisticaCasoRegimenRegimenesBean(); 
				
				CloneObjectWSToAplicacion.
					cloneWSCondicionUrbanisticaCasoRegimenRegimenesBeanToCondicionUrbanisticaCasoRegimenRegimenesBean(cucrr, str);
		
			}
			
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los regimenes especificos de la condicion urbanistica");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
						JOptionPane.ERROR_MESSAGE);
				throw excepcionDeInvocacion;
			}
			
			return cucrr;
		}
	
	
	public static com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean 
				obtenerDatosDeterminacion(int idDet ,ApplicationContext app) throws Exception{
		
		com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean detBean = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerDatosDeterminacion request = null;
		ServicesStub.ObtenerDatosDeterminacionResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerDatosDeterminacion();
			
			request.setIdDet(idDet);
			// invocamos al web service
			response = customer.obtenerDatosDeterminacion(request);
			DeterminacionBean str = response.get_return();
			
		
			if (str != null) {
			  				
				detBean = new com.gestorfip.app.planeamiento.beans.tramite.DeterminacionBean(); 
				
				CloneObjectWSToAplicacion.cloneWSDeterminacionBeanToDeterminacionBean(detBean, str);
				
			}
			
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los regimenes especificos de la condicion urbanistica");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
						JOptionPane.ERROR_MESSAGE);
				throw excepcionDeInvocacion;
			}
			
			return detBean;
		}

	
	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @param fip: id del fip
	 * @param ccaaSeleccionada
	 * @return ArrayList: Lista que contiene los beans representando las comunidades
	 * @throws Exception
	 */
	public static CaracteresDeterminacionPanelBean[] obtenerCararteresDeterminaciones(ApplicationContext app,
												int idFip)
			throws Exception {
		
		
		CaracteresDeterminacionPanelBean [] lstCaracteresDeterminacionPanelBean = null;
		
		ServicesStub customer = null;
		ServicesStub.ObtenerCaracteresDeterminacionesResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);

			// invocamos al web service
			response = customer.obtenerCaracteresDeterminaciones();
			CaracteresDeterminacionBean[] str = response.get_return();
			if (str != null) {
				lstCaracteresDeterminacionPanelBean = new CaracteresDeterminacionPanelBean[str.length];
				for(int i=0; i<str.length; i++ ){
					
				
					lstCaracteresDeterminacionPanelBean[i] = new CaracteresDeterminacionPanelBean();
					lstCaracteresDeterminacionPanelBean[i].setId(str[i].getId());
					lstCaracteresDeterminacionPanelBean[i].setCodigo(str[i].getCodigo());
					lstCaracteresDeterminacionPanelBean[i].setNombre(str[i].getNombre());
					lstCaracteresDeterminacionPanelBean[i].setAplicaciones_min(str[i].getAplicaciones_min());
					lstCaracteresDeterminacionPanelBean[i].setAplicaciones_max(str[i].getAplicaciones_max());
				}
			}
				
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los datos del tramite");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return lstCaracteresDeterminacionPanelBean;
		
	}
	
	
	public static String obtenerAmbitoTrabajo(ApplicationContext app, int idMunicipio)throws Exception {
		
	//	HashMap<String, String> codigosAmbitos = new HashMap<String, String>();
		String codAmbito = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerAmbitoTrabajo request = null;
		ServicesStub.ObtenerAmbitoTrabajoResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {

			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerAmbitoTrabajo();
			
			request.setIdMunicipio(idMunicipio);
			response = customer.obtenerAmbitoTrabajo(request);
			String str = response.get_return();
			if (str != null) {
				codAmbito = str;

			}
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los datos del tramite");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
		
		return codAmbito;
	}
	
	
	
	

	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @return String: fecha del fip
	 * @throws Exception
	 */
	public static VersionesUER[] obtenerVersionesConsolaUER( ApplicationContext app) throws Exception {
	
		VersionesUER[] versiones;
		ServicesStub customer = null;
		ServicesStub.ObtenerVersionesConsolaUERResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);

			// invocamos al web service
			response = customer.obtenerVersionesConsolaUER();
			versiones = response.get_return();
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las versiones disponibles de la consola UER");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor, Contacte con su administrador", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
	
		return versiones;
	}
	
	public static CRSGestor[] obtenerCrsGestor( ApplicationContext app) throws Exception {
		
		CRSGestor[] lstCrs;
		ServicesStub customer = null;
		ServicesStub.ObtenerCRSResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);

			// invocamos al web service
			response = customer.obtenerCRS();
			lstCrs = response.get_return();
			
			
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener las versiones disponibles de la consola UER");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor, Contacte con su administrador", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
	
		return lstCrs;
	}
	
	

	/**
	 * 
	 * @param app: Contexto de aplicacion
	 * @return String: fecha del fip
	 * @throws Exception
	 */
	public static ConfiguracionGestor obtenerConfigVersionConsolaUER(ApplicationContext app) throws Exception {
	
		ConfiguracionGestor configGestor = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerConfigVersionConsolaUERResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		try {
			// Creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUTFIP);
			// Establecemos los parametros de la invocacionto);
			// Invocamos al web service
			response = customer.obtenerConfigVersionConsolaUER();
			configGestor = response.get_return();

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la configuracion del GestorFip");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
	
		return configGestor;
	}
	
	
	public static ConfiguracionGestor guardarConfiguracionGestorFip(ConfiguracionGestor config , ApplicationContext app) throws Exception {
		
		ConfiguracionGestor configGestor = null;
		ServicesStub customer = null;
		ServicesStub.GuardarConfiguracionGestorFip request = null;
		ServicesStub.GuardarConfiguracionGestorFipResponse response = null;
		
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUTFIP = Integer.parseInt((String)hm.get("ws.timeoutFip"));

		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.GuardarConfiguracionGestorFip();
			
			request.setConfigGestor(config);
			response = customer.guardarConfiguracionGestorFip(request);
			boolean str = response.get_return();
			

		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener la configuracion del GestorFip");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw excepcionDeInvocacion;
		}
	
		return configGestor;
	}
	
	public static UnidadBean obtenerUnidadDeterminacion(int idDet ,ApplicationContext app) throws Exception{
	
		UnidadBean unidad = null;
		ServicesStub customer = null;
		ServicesStub.ObtenerUnidadDeterminacion request = null;
		ServicesStub.ObtenerUnidadDeterminacionResponse response = null;
		HashMap hm = (HashMap)app.getBlackboard().get(ConstantesGestorFIP.PROPERTIES_GESTORFIP);
		WS_TIMEOUT = Integer.parseInt((String)hm.get("ws.timeout"));
		try {
			
			// creamos el soporte
			customer = new ServicesStub(targetEndPoint);
			// Incrementamos el timeout a 2mins ya que la carga de datos puede tardarse
			customer._getServiceClient().getOptions().setTimeOutInMilliSeconds(WS_TIMEOUT);
			// establecemos el parametro de la invocacion
			request = new ServicesStub.ObtenerUnidadDeterminacion();
			
			request.setIdDet(idDet);
			// invocamos al web service
			response = customer.obtenerUnidadDeterminacion(request);
			unidad = response.get_return();
		
		
		} catch (RemoteException excepcionDeInvocacion) {
			System.err.println(excepcionDeInvocacion.toString());
			logger.error("Exception al obtener los regimenes especificos de la condicion urbanistica");
			logger.error(excepcionDeInvocacion.getMessage());
			JOptionPane.showMessageDialog(app.getMainFrame(),
					"Se ha pruducido un error en la comunicación con el servidor", "Error",
						JOptionPane.ERROR_MESSAGE);
				throw excepcionDeInvocacion;
		}
	
	return unidad;
}
	
}
