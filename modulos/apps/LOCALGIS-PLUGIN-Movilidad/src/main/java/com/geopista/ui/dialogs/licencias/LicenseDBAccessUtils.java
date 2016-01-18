/**
 * LicenseDBAccessUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.licencias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.app.licencias.CConstantesLicencias;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.security.SecurityManager;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.Utils;

/**
 * Clase de acceso a la base de datos para la gestión de licencias
 * @author irodriguez
 *
 */
public class LicenseDBAccessUtils {
	
	private static Logger log = Logger.getLogger(LicenseDBAccessUtils.class);
	public static final String NUM_EXP_PREFIX = "E_";

	
	/**
	 * Añade la información sobre licencias de la BBDD a los elementos de tipo LicenciasSVG
	 * @param parseLicencias
	 * @param locale 
	 * @param urlPrefix 
	 * @throws Exception 
	 */
	public void addDataBaseInfoLicenses(Map<String, List<LicenseMetadataSVG>> parseLicencias, String urlLicencias, String locale) throws Exception{
	    LicenseMetadataSVG licSVG = null;
	    List<LicenseMetadataSVG> listLic = null;
		Collection<List<LicenseMetadataSVG>> values = parseLicencias.values();

		List<LicenseMetadataSVG> quickLicensesList = new ArrayList<LicenseMetadataSVG>(); //almacenamos para un acceso rapido
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			listLic = (List<LicenseMetadataSVG>) iterator.next();
			for (Iterator iterator2 = listLic.iterator(); iterator2.hasNext();) {
				licSVG = (LicenseMetadataSVG) iterator2.next();
				//añadimos solamente las licencias
				if(Utils.isInArray(Constants.TIPOS_LICENCIAS, licSVG.getNombreMetadato())){
//					conditionValues += "'" + licenciasSVG.getIdFeatStr() + "',";
					quickLicensesList.add(licSVG);	
				}
			}
		}

		SecurityManager.setsUrl(urlLicencias);
		if(!SecurityManager.isOnlyLogged()){
			//SecurityManager.login(Constants.LICENSES_USER, Constants.LICENSES_PWD, CConstantesLicencias_LCGIII.idApp);
			throw new Exception("No se ha realizado login");
		}
		CConstantesComando.servletLicenciasUrl = urlLicencias+"/CServletLicencias"; //esta cte la utiliza luego

		
		//obtenemos los expedientes
		Vector<CTipoLicencia> tiposLicencia = null;
	    List<CExpedienteLicencia> expedientesLicencia = new ArrayList<CExpedienteLicencia>();
		CResultadoOperacion resOp = null;
		Vector expedientes = null;
		CExpedienteLicencia cExpLic = null;
		Vector solicitudes = null;
		CSolicitudLicencia cSolLic = null;
		for (Iterator iterator = quickLicensesList.iterator(); iterator.hasNext();) {
			licSVG = (LicenseMetadataSVG) iterator.next();
			tiposLicencia = new Vector<CTipoLicencia>();
			if(licSVG.getNombreMetadato().equals(Constants.LIC_OBRA_MAYOR)){
				tiposLicencia.add(new CTipoLicencia(CConstantesLicencias.ObraMayor,"",""));
			}
			else if(licSVG.getNombreMetadato().equals(Constants.LIC_OBRA_MENOR)){
			    tiposLicencia.add(new CTipoLicencia(CConstantesLicencias.ObraMenor,"",""));
			}
			else if(licSVG.getNombreMetadato().equals(Constants.LIC_ACTIVIDAD)){
			    tiposLicencia.add(new CTipoLicencia(CConstantesLicencias.Actividades,"",""));
			}
			else { //si es otro tipo de feature (como inv de patrimonio) pasamos a la siguiente
				continue;
			}
			
			resOp = COperacionesLicencias.getExpedienteLicencia(NUM_EXP_PREFIX+licSVG.getIdFeatStr(), locale, tiposLicencia);
			if(resOp!=null){
				expedientes = resOp.getExpedientes();
				if(expedientes!=null && expedientes.size() > 0){
					cExpLic =  (CExpedienteLicencia) expedientes.get(0);
					cExpLic.setLocale(locale);
					solicitudes = resOp.getSolicitudes();
					if(solicitudes!=null && solicitudes.size() > 0){
						cSolLic =  (CSolicitudLicencia) solicitudes.get(0);
						cExpLic.setSolicitud(cSolLic);
					}
					expedientesLicencia.add(cExpLic);
				}
			}
		}
		updateLicenseInfo(quickLicensesList, expedientesLicencia);

	}
	
	
	/**
	 * Añadimos la informacion de actividades contaminantes.
	 * @param parseLicencias
	 * @param urlContaminantes
	 * @param locale
	 * @throws Exception
	 */
	public void addDataBaseInfoContaminantes(Map<String, 
						List<LicenseMetadataSVG>> parseLicencias, String locale) throws Exception{
	    
		LicenseMetadataSVG elementSVG = null;
	    List<LicenseMetadataSVG> listLic = null;
		Collection<List<LicenseMetadataSVG>> values = parseLicencias.values();
		
		//almacenamos para un acceso rapido
		List<LicenseMetadataSVG> quickElementList = new ArrayList<LicenseMetadataSVG>(); 
				
		//collection para hacer la llamada al cliente.
		List<GeopistaFeature> geopistaFeatureList = new ArrayList<GeopistaFeature>(); 
		GeopistaFeature gFeature = null;
		GeopistaLayer gLayer = null;
		
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			listLic = (List<LicenseMetadataSVG>) iterator.next();
			for (Iterator iterator2 = listLic.iterator(); iterator2.hasNext();) {
				elementSVG = (LicenseMetadataSVG) iterator2.next();
				
				//Buscamos todas las features de las que tenemos que buscar informacion
				if(Utils.isInArray(Constants.TIPOS_CONTAMINANTES, 
							elementSVG.getNombreMetadato())){
					quickElementList.add(elementSVG);
					gFeature = new GeopistaFeature();
					gLayer = new GeopistaLayer();
					//con estos datos nos vale para que pase el cliente
					gLayer.setSystemId(elementSVG.getNombreMetadato());
					gFeature.setLayer(gLayer);
					gFeature.setSystemId(elementSVG.getIdFeatStr());
					geopistaFeatureList.add(gFeature);
				}
			}
		}
		
		
		/*ApplicationContext app= AppContext.getApplicationContext();
		com.geopista.app.contaminantes.init.Constantes.url= app.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)+"contaminantes/";
		OperacionesContaminantes operacionContaminantes=new OperacionesContaminantes(com.geopista.app.contaminantes.init.Constantes.url);
		operacionContaminantes.getVertederos();*/
				
	}
	

	/**
	 * Rellenamos el expediente para cada licencia
	 * @param quickLicensesList
	 * @param expedientesLicencia
	 */
	private void updateLicenseInfo(List<LicenseMetadataSVG> quickLicensesList, List<CExpedienteLicencia> expedientesLicencia) {
		CExpedienteLicencia expedienteLicencia = null;
		String expId = null;
		LicenseMetadataSVG licSVG = null;
		for (Iterator iterator = expedientesLicencia.iterator(); iterator.hasNext();) {
			expedienteLicencia = (CExpedienteLicencia) iterator.next();
			expId = String.valueOf(expedienteLicencia.getIdSolicitud());
			for (Iterator iterator2 = quickLicensesList.iterator(); iterator2.hasNext();) {
				licSVG = (LicenseMetadataSVG) iterator2.next();
				if(licSVG.getIdFeatStr().equals(expId)){
					licSVG.setExpLicencia(expedienteLicencia);
				}
			}
		}
		
	}

	/**
	 * Añade la información sobre inventario de patrimonio de la BBDD a los elementos de tipo LicenciasSVG
	 * @param parseLicencias
	 * @param urlInventario
	 * @param locale
	 * @throws Exception 
	 */
	public void addDataBaseInfoInventary(Map<String, List<LicenseMetadataSVG>> parseLicencias, String urlInventario, String locale) throws Exception {
	   
		LicenseMetadataSVG licSVG = null;
	    List<LicenseMetadataSVG> listLic = null;
		Collection<List<LicenseMetadataSVG>> values = parseLicencias.values();
		List<LicenseMetadataSVG> quickLicensesList = new ArrayList<LicenseMetadataSVG>(); //almacenamos para un acceso rapido
		List<GeopistaFeature> geopistaFeatureList = new ArrayList<GeopistaFeature>(); //collection para hacer la llamada al cliente de inventario
		GeopistaFeature gFeature = null;
		GeopistaLayer gLayer = null;
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			listLic = (List<LicenseMetadataSVG>) iterator.next();
			for (Iterator iterator2 = listLic.iterator(); iterator2.hasNext();) {
				licSVG = (LicenseMetadataSVG) iterator2.next();
				//añadimos solamente inventario
				if(Utils.isInArray(Constants.TIPOS_INVENTARIO, licSVG.getNombreMetadato())){
					quickLicensesList.add(licSVG);
					gFeature = new GeopistaFeature();
					gLayer = new GeopistaLayer();
					//con estos datos nos vale para que pase el cliente
					gLayer.setSystemId(licSVG.getNombreMetadato());
					gFeature.setLayer(gLayer);
					gFeature.setSystemId(licSVG.getIdFeatStr());
					geopistaFeatureList.add(gFeature);
				}
			}
		}
	
		//obtenemos todos los inventarios para las features indicadas
		Object[] features = (Object[]) geopistaFeatureList.toArray();
		Collection<BienBean> listaBienes =  getListaBienesInventario(urlInventario, features);

		//rellenamos lo que falta en los ficheros
		BienBean bienBean = null;
		Object[] idFeatures = null;
		for (Iterator iterator = listaBienes.iterator(); iterator.hasNext();) {
			bienBean = (BienBean) iterator.next();
			for (Iterator iterator2 = quickLicensesList.iterator(); iterator2.hasNext();) {
				licSVG = (LicenseMetadataSVG) iterator2.next();
				idFeatures = bienBean.getIdFeatures();
				if(idFeatures!=null){
					for (int i = 0; i < idFeatures.length; i++) {
						if(licSVG.getIdFeatStr().equals(idFeatures[i])){
							//if (!bienBean.isVersionado())
								licSVG.addInventario(bienBean);
						}
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Obtiene un listado de todos el inventario de patrimonio para las features seleccionadas
	 * @param urlInventario
	 * @param features 
	 * @return
	 * @throws Exception 
	 */
	private Collection<BienBean> getListaBienesInventario(String urlInventario, Object[] features) throws Exception {
		int action;
		String superpatron = null;
		String patron = null;
		String cadena = "";
		Collection filtro = null;
		
		
		//obtenemos los inventarios de patrimonio. Recorremos todas
		//las posibilidades
		InventarioClient inventarioClient = new InventarioClient(urlInventario);
		Iterator<String> actionIt = Const.MULTIACCIONES_PATRONES.keySet().iterator();
		Collection<BienBean> listaBienes = new ArrayList<BienBean>();
		Collection bienesInventario = null;
		BienBean bien = null;
		for (Iterator iterator = Const.SUPERPATRONES.iterator(); iterator.hasNext();) {
			superpatron = (String) iterator.next();
			actionIt = Const.MULTIACCIONES_PATRONES.keySet().iterator();
			while (actionIt.hasNext()) {
				patron = (String) actionIt.next();
				action = Const.MULTIACCIONES_PATRONES.get(patron);
				try {

					bienesInventario = inventarioClient.getBienesInventario(action, superpatron, patron, cadena, filtro, features, null);
					if(bienesInventario!=null && bienesInventario.size()>0){
						//añadimos el superpatron a fuego
						for (Iterator iterator2 = bienesInventario.iterator(); iterator2.hasNext();) {
							bien = (BienBean) iterator2.next();
							//bien.setSuperPatron(Const.SUPERPATRONES_NOMBRE.get(superpatron));
							bien.setSuperPatron(superpatron);
						}
						//añadimos los bienes a la lista
						listaBienes.addAll(bienesInventario);
					}
				}catch (Exception e) {
					log.error("Error al obtener los bienes de inventario de patrimonio. " + e.getMessage(), e);
					throw e;
				}
			}
		}
		
		return listaBienes;
	}
	
}
