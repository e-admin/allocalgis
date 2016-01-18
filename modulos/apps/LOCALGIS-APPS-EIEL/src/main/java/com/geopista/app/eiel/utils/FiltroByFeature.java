/**
 * FiltroByFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.NoFilterException;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;

;

public class FiltroByFeature {

	private AppContext aplicacion;
	private LocalGISEIELClient eielClient;

	// Metodo que deben implementar aquellas clases de las que se quiera
	// realizar
	// filtros
	private static String DEFAULT_METHOD_SQL = "getFilterSQL";
	public static String DEFAULT_METHOD_SQL_FEATURE = "getFilterSQLByFeature";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(FiltroByFeature.class);

	public HashMap<String, String> generateSQLFilterFeaturesSeleccionadas(
			String nodo, Object elementoSeleccionado, String locale) throws NoFilterException {

		aplicacion = (AppContext) AppContext.getApplicationContext();
		HashMap listaSQL = new HashMap();

		try {

			// *******************************************************************
			// Revisamos en primer lugar si hay features seleccionadas en el
			// panel
			// *******************************************************************
			HashMap<String, Collection> listaFeaturesPorCapa = getFeaturesSelected();

			// *******************************************************************
			// Si el usuario ha seleccionado un elemento o ha seleccionado
			// varias en el editor empezamos a recuperar
			// su informacion
			// *******************************************************************
			if ((listaFeaturesPorCapa.size() > 0)
					|| (elementoSeleccionado != null)) {

				eielClient = new LocalGISEIELClient(
						aplicacion
								.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.EIEL_WEBAPP_NAME 
								+ ServletConstants.EIEL_SERVLET_NAME);
				HashMap<String, Object> listaNodosByName = eielClient
						.getNodosEIELByName(nodo, locale);

				if (elementoSeleccionado != null) {
					String sql = reflectXMLMethod(elementoSeleccionado,
							DEFAULT_METHOD_SQL);
					if (sql != null)
						listaSQL.put(nodo, sql);
				} else if (listaFeaturesPorCapa.size() > 0) {
					Iterator<String> it = listaFeaturesPorCapa.keySet()
							.iterator();
					while (it.hasNext()) {
						String capa = it.next();
						logger.info("Capa:" + capa);
						LCGNodoEIEL nodoEIEL = (LCGNodoEIEL) listaNodosByName
								.get(capa);

						// Si es nulo verificamos unas traducciones especiales
						// de capas
						if (nodoEIEL == null) {
							String traduccionEspecial = (String) InitEIEL.traduccionesEspeciales
									.get(capa);
							if (traduccionEspecial != null) {
								nodoEIEL = (LCGNodoEIEL) listaNodosByName
										.get(traduccionEspecial);
							}
						}

						if (nodoEIEL != null) {

							String bean = nodoEIEL.getBean();
							// logger.info("BEAN:"+bean);
							if (bean != null) {
								HashSet hash = (HashSet) listaFeaturesPorCapa
										.get(capa);
								int numElementos = 1;
								Iterator it2 = hash.iterator();
								String sql = "";
								while (it2.hasNext()) {
									GeopistaFeature feature = (GeopistaFeature) it2
											.next();
									if (nodoEIEL != null) {

										// logger.info("BEAN:"+bean);
										if (bean != null) {
											Object obj = Class.forName(bean)
													.newInstance();
											String sqlData = reflectXMLMethod(
													obj,
													DEFAULT_METHOD_SQL_FEATURE,
													feature);
											if (sqlData != null)
												if (numElementos < hash.size())
													sql += sqlData + " or ";
												else
													sql += sqlData;

											numElementos++;
										} else {
											logger.error("El bean para el nodo:"
													+ nodoEIEL.getClave()
													+ " no existe");
										}
									}
								}

								listaSQL.put(nodoEIEL.getClave(), sql);
							} else {
								logger.error("El bean para el nodo:"
										+ nodoEIEL.getClave() + " no existe");
							}
						}

					}
				}

			} /*else {
				logger.info("No se han seleccionado elementos en el mapa");
			}*/

		} catch (NoFilterException e){
			throw e;
		}
		catch (Exception e) {			
			e.printStackTrace();
		}
		return listaSQL;
	}

	public String getSql(HashMap<String, Object> listaNodosByName,
			String nodoSeleccionado, GeopistaFeature feature) {

		String sqlData = null;

		try {
			LCGNodoEIEL nodoEIEL = (LCGNodoEIEL) listaNodosByName
					.get(nodoSeleccionado);

			if (nodoEIEL != null) {
				String bean = nodoEIEL.getBean();
				if (bean != null) {
					Object obj = Class.forName(bean).newInstance();
					sqlData = reflectXMLMethod(obj, DEFAULT_METHOD_SQL_FEATURE,
							feature);

				}
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return sqlData;
	}

	/**
	 * Devuelve las features seleccionadas en cada capa
	 * 
	 * @return
	 */
	private HashMap<String, Collection> getFeaturesSelected() {

		HashMap<String, Collection> featuresporCapa = new HashMap<String, Collection>();
		Collection featuresSeleccionadas = null;

		LayerManager layerManager = GeopistaEditorPanel.getEditor()
				.getLayerManager();

		List capasVisibles = layerManager.getVisibleLayers(true);

		Iterator capasVisiblesIter = capasVisibles.iterator();

		while (capasVisiblesIter.hasNext()) {
			Layer capaActual = (Layer) capasVisiblesIter.next();
			logger.debug("Capa Actual:" + capaActual.getName());

			if (capaActual instanceof GeopistaLayer) {
				GeopistaLayer geopistaLayer = (GeopistaLayer) capaActual;

				// El identificador de capa para captaciones por ejemplo es "AA
				String idCapa = geopistaLayer.getSystemId();
				logger.debug("Capa:" + geopistaLayer.getSystemId());

				featuresSeleccionadas = GeopistaEditorPanel.getEditor()
						.getLayerViewPanel().getSelectionManager()
						.getFeaturesWithSelectedItems(capaActual);
				if (featuresSeleccionadas.size() > 0)
					featuresporCapa.put(idCapa, featuresSeleccionadas);

				/*
				 * Iterator featuresSeleccionadasIter =
				 * featuresSeleccionadas.iterator(); while
				 * (featuresSeleccionadasIter.hasNext()) {
				 * 
				 * Feature localFeature = (Feature)
				 * featuresSeleccionadasIter.next(); GeopistaFeature
				 * geopistaFeature=((GeopistaFeature)localFeature); String
				 * systemId = ((GeopistaFeature)localFeature).getSystemId();
				 * System.out.println("Identificador feature:"+systemId);
				 * 
				 * }
				 */
			}
		}
		return featuresporCapa;
	}

	private String reflectXMLMethod(Object objectInvoke, String strMethod) throws Exception {

		String itemValue = null;
		try {

			if (objectInvoke != null) {
				Method method = objectInvoke.getClass().getMethod(strMethod,
						new Class[0]);
				itemValue = (String) method.invoke(objectInvoke);
			}

		} 
		catch (Exception e) {
		
			if (e instanceof NoFilterException){
				throw e;
			}
			logger.error(
					"No se ha podido acceder al método "
							+ objectInvoke.getClass() + "." + strMethod, e);
		}
		return itemValue;
	}

	private String reflectXMLMethod(Object objectInvoke, String strMethod,
			Object parameter) {

		String itemValue = null;
		try {

			if (objectInvoke != null) {
				Class[] argumentTypes = { Object.class };
				Method method = objectInvoke.getClass().getMethod(strMethod,
						argumentTypes);
				Object[] arguments = { parameter };
				itemValue = (String) method.invoke(objectInvoke, arguments);
			}

		} catch (Exception e) {
			logger.error("No se ha podido acceder al método "
					+ objectInvoke.getClass() + "." + strMethod);
		}
		return itemValue;
	}

}
