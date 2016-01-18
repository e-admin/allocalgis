package com.geopista.ui.dialogs.eiel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.FilterEIEL;
import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.dialogs.beans.eiel.EIELLayerBean;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.Utils;

/**
 * Clase de acceso a la base de datos para la gestión de eiel
 * 
 * @author irodriguez
 * 
 */
public class EIELDBAccessUtils {

	private static Logger log = Logger.getLogger(EIELDBAccessUtils.class);
	public static final String NUM_EXP_PREFIX = "E_";

	/**
	 * Obtiene un listado de todos los elementos EIEL para las features
	 * seleccionadas
	 * 
	 * @param urlEIEL
	 * @param features
	 * @return
	 * @throws Exception
	 */
	private Collection getListaEIEL(String urlEIEL,
			ArrayList<GeopistaFeature> features, String layerName, ArrayList<LCGCampoCapaTablaEIEL> relacionFields, String tableCod) throws Exception {		
		LocalGISEIELClient eielClient = new LocalGISEIELClient(urlEIEL);		
		Collection listaEIEL = new ArrayList();
		Collection elementoEIEL = null;		
		FilterEIEL filterEIEL = new FilterEIEL();
		Iterator it = features.iterator();
		while (it.hasNext()) {
			GeopistaFeature feature = (GeopistaFeature) it.next();
			if(feature.getLayer().getSystemId().equals(layerName)){
				try {
					String filter = filterEIEL.getFilterSQLByFeature(feature, relacionFields);
					if(filter!=null && filter.length()>0)
						filter += " and revision_expirada=9999999999 ";
					if(filter!=null)
						elementoEIEL = eielClient.getLstElementos(filter,tableCod, false);
					//	elementoEIEL = eielClient.getLstElementos(filter,layerName, false);
					if (elementoEIEL != null && elementoEIEL.size() > 0) {
						listaEIEL.addAll(elementoEIEL);						
					}
			
				} catch (Exception e) {
					log.error(
							"Error al obtener los elementos de EIEL. "
									+ e.getMessage(), e);
					throw e;
				}
			}
		}
		return listaEIEL;
	}

	/**
	 * Añade la información sobre inventario de patrimonio de la BBDD a los
	 * elementos de tipo EIELSVG
	 * 
	 * @param parseEIEL
	 * @param urlInventario
	 * @param locale
	 * @throws Exception
	 */
	public void addDataBaseInfoEIEL(
			Map<String, List<EIELMetadataSVG>> parseEIEL, String urlEIEL,
			String locale, String layerName, ArrayList<GeopistaFeature> features, HashMap<String, EIELLayerBean> capasBean) throws Exception {
		
		if (Utils.isInArray(Constants.TIPOS_EIEL,layerName)){
			EIELMetadataSVG eielSVG = null;
			List<EIELMetadataSVG> listEIEL = null;
			Collection<List<EIELMetadataSVG>> values = parseEIEL.values();
			List<EIELMetadataSVG> quickEIELList = new ArrayList<EIELMetadataSVG>();
			Iterator iterator = values.iterator();
			while(iterator.hasNext()){
				listEIEL = (List<EIELMetadataSVG>) iterator.next();
				Iterator iterator2 = listEIEL.iterator(); 
				while(iterator2.hasNext()){
					eielSVG = (EIELMetadataSVG) iterator2.next();
					// añadimos solamente eiel
					if (layerName.equals(eielSVG.getNombreMetadato())) {
						quickEIELList.add(eielSVG);					
					}
				}
			}
			// obtenemos todos los elementos de eiel para las features indicadas	
			ArrayList<LCGCampoCapaTablaEIEL> relacionFields = capasBean.get(layerName).getRelacionFields();
			Collection listaEIEL = this.getListaEIEL(urlEIEL, features, layerName, relacionFields, capasBean.get(layerName).getCodEIEL());			
			Object eielBean = null;
			iterator = listaEIEL.iterator();
			while(iterator.hasNext()){
				eielBean = iterator.next();
				Iterator iterator2 = quickEIELList.iterator();
				while(iterator2.hasNext()){
					eielSVG = (EIELMetadataSVG) iterator2.next();
					if (eielBean != null) {
						try {		
							boolean equals = true;
							Iterator iterator3 = relacionFields.iterator();
							while(iterator3.hasNext()){		
								LCGCampoCapaTablaEIEL lcgCampoCapaTabla = (LCGCampoCapaTablaEIEL) iterator3.next();				
								Method method = eielBean.getClass().getMethod(lcgCampoCapaTabla.getMethod(), new Class[0]);		
								if (!method.invoke(eielBean).toString().equals(eielSVG.getAttributes().get(lcgCampoCapaTabla.getCampoCapa()))){
									equals = false;	
									break;
								}
							}
							if(equals){
								eielSVG.addEIEL(eielBean);
							}						
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public static String getMethodFromRelacionField(ArrayList<LCGCampoCapaTablaEIEL> relacionFields, String dbField){
		Iterator it = relacionFields.iterator();
		while(it.hasNext()){
			LCGCampoCapaTablaEIEL lcgCampoCapaTabla = (LCGCampoCapaTablaEIEL) it.next();
			if(lcgCampoCapaTabla.getCampoBD().equals(dbField))
				return lcgCampoCapaTabla.getMethod();
		}		
		return null;
	}

}
