/**
 * SigmConnection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server.sigm;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndName;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;
import com.localgis.web.gwfst.ws.sigm.dwr.Sigm;

public class SigmConnection {

	/**
	 * Logger
	 */
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(SigmConnection.class);

	/**
	 * Recupera toda la información de un expediente de SiGM
	 * @param oos: OutputStream resultado
	 * @param url: Url de SiGM
	 * @param idEntidad: Identificador de entidad de LocalGIS
     * @param nombreCapa: Identificador de la capa de LocalGIS
     * @param idFeature: Identificador de expediente de SiGM
	 * @throws Exception
	 */
	public void getInfoAll(ObjectOutputStream oos, String url,
			Integer idEntidad, String nombreCapa, String idFeature)
			throws Exception {
		try {
			oos.writeObject((PropertyAndValue[]) Sigm.getInfoAll(url,
					idEntidad, nombreCapa, idFeature));
		} catch (Exception e) {
			logger.error("getInfoAll: ", e);
			oos.writeObject(null);
			throw e;
		}
	}

	/**
	 * Recupera expedientes coincidentes con valores de búsqueda
	 * @param oos: OutputStream resultado
	 * @param url: Url de SiGM
	 * @param idEntidad: Identificador de entidad de LocalGIS
	 * @param featureType: Tipo
	 * @param searchPropertyAndValues: Atributos y sus valores para la búsqueda
	 * @throws Exception
	 */
	public void getSearchAll(ObjectOutputStream oos, String url,
			Integer idEntidad, String featureType,
			PropertyAndValue[] searchPropertyAndValues) throws Exception {
		try {
			oos.writeObject((String[]) Sigm.getSearchAll(url, idEntidad,
					featureType, searchPropertyAndValues));
		} catch (Exception e) {
			logger.error("getInfoAll: ", e);
			oos.writeObject(null);
			throw e;
		}
	}

	/**
	 * Recupera la información de un atributo de un expediente
	 * @param oos: OutputStream resultado
	 * @param url: Url de SiGM
	 * @param idEntidad: Identificador de entidad de LocalGIS
     * @param nombreCapa: Identificador de la capa de LocalGIS
     * @param idFeature: Identificador de expediente de SiGM
	 * @param property: Nombre de la propiedad
	 * @throws Exception
	 */
	public void getInfoByPrimaryKey(ObjectOutputStream oos, String url,
			Integer idEntidad, String nombreCapa, String idFeature,
			String property) throws Exception {
		try {
			oos.writeObject((String) Sigm.getInfoByPrimaryKey(url, idEntidad,
					nombreCapa, idFeature, property));
		} catch (Exception e) {
			logger.error("getInfoAll: ", e);
			oos.writeObject(null);
			throw e;
		}
	}
	
	/**
	 * Recupera todos los atributos de un expediente de SiGM
	 * @param oos: OutputStream resultado
     * @param nombreCapa: Identificador de la capa de LocalGIS
	 * @throws Exception
	 */
	public void getPropertyAndName(ObjectOutputStream oos, String nombreCapa)
			throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();  
			oos.writeObject((PropertyAndName[]) Sigm.getPropertyAndName(localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().getProceduresByLayerName(nombreCapa).getProcedureType()));
		} catch (Exception e) {
			logger.error("getPropertyAndName: ", e);
			oos.writeObject(null);
			throw e;
		}
	}
	
	/**
	 * Devuelve el listado de procedimiento
	 * @param oos: OutputStream resultado
	 * @throws Exception
	 */
	public void getAllProcedures(ObjectOutputStream oos) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			oos.writeObject((List<Procedure>) localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().getAllProcedures());
		} catch (Exception e) {
			logger.error("getAllProperties: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Añade un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedure: Procedimiento
	 * @throws Exception
	 */
	public void insertProcedure(ObjectOutputStream oos, Procedure procedure) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().insertProcedure(procedure);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("insertProcedure: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Actualiza un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedure: Procedimiento
	 * @throws Exception
	 */
	public void updateProcedure(ObjectOutputStream oos, Procedure procedure) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().updateProcedure(procedure);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("updateProcedure: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Elimina un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void deleteProcedure(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().deleteProcedure(procedureId);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("deleteProcedure: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Devuelve el listado de por defecto de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void getProcedureDefaults(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();   
			oos.writeObject((ProcedureDefaults) localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().getProcedureDefaults(procedureId));
		} catch (Exception e) {
			logger.error("getProcedureDefaults: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Añade los por defecto de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureDefaults: Por defecto del procedimiento
	 * @throws Exception
	 */
	public void insertProcedureDefaults(ObjectOutputStream oos, ProcedureDefaults procedureDefaults) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().insertProcedureDefaults(procedureDefaults);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("insertProcedureDefaults: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Actualiza los por defecto un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureDefaults: Por defecto del procedimiento
	 * @throws Exception
	 */
	public void updateProcedureDefaults(ObjectOutputStream oos, ProcedureDefaults procedureDefaults) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().updateProcedureDefaults(procedureDefaults);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("updateProcedureDefaults: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Elimina los por defecto un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void deleteProcedureDefaults(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().deleteProcedureDefaults(procedureId);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("deleteProcedureDefaults: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Devuelve el listado de atributos de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void getProcedureProperties(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();   
			oos.writeObject((List<ProcedureProperty>) localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().getProcedureProperties(procedureId));
		} catch (Exception e) {
			logger.error("getProcedureProperties: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Devuelve un mapa con property como key y los atributos de un procedimiento como value
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void getProcedurePropertiesMap(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();   
			oos.writeObject((HashMap<String,ProcedureProperty>) localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().getProcedurePropertiesMap(procedureId));
		} catch (Exception e) {
			logger.error("getProcedureProperties: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Añade una propiedad a un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureProperty: Propiedad de un procedimiento
	 * @throws Exception
	 */
	public void insertProcedureProperty(ObjectOutputStream oos, ProcedureProperty procedureProperty) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().insertProcedureProperty(procedureProperty);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("insertProcedureProperty: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Actualiza una propiedad de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureProperty: Propiedad de un procedimiento
	 * @throws Exception
	 */
	public void updateProcedureProperty(ObjectOutputStream oos, ProcedureProperty procedureProperty) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().updateProcedureProperty(procedureProperty);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("updateProcedureProperty: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Elimina la propiedad de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedurePropertyId: Identificador de la propiedad del procedimiento
	 * @throws Exception
	 */
	public void deleteProcedureProperty(ObjectOutputStream oos, ProcedurePropertyKey procedurePropertyId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().deleteProcedureProperty(procedurePropertyId);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("deleteProcedureProperties: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Añade las propiedad a un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureProperties: Mapa con el property y los objetos ProcedureProperty con las propiedades de un procedimiento
	 * @throws Exception
	 */
	public void insertProcedureProperties(ObjectOutputStream oos, HashMap<String, ProcedureProperty> procedureProperties) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().insertProcedureProperties(procedureProperties);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("insertProcedureProperty: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Actualiza una propiedad de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador de un procedimiento
	 * @param procedureProperties: Mapa con el property y los objetos ProcedureProperty con las propiedades de un procedimiento
	 * @throws Exception
	 */
	public void updateProcedureProperties(ObjectOutputStream oos, String procedureId, HashMap<String, ProcedureProperty> procedureProperties) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().updateProcedureProperties(procedureId, procedureProperties);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("updateProcedureProperty: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
	
	/**
	 * Elimina las propiedades de un procedimiento
	 * @param oos: OutputStream resultado
	 * @param procedureId: Identificador del procedimiento
	 * @throws Exception
	 */
	public void deleteProcedureProperties(ObjectOutputStream oos, String procedureId) throws Exception {
		try {
			LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
			localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager().deleteProcedureProperties(procedureId);
			oos.writeObject(true);
		} catch (Exception e) {
			logger.error("deleteProcedureProperties: ", e);
			oos.writeObject(null);
			throw e;
		}		
	}
}
