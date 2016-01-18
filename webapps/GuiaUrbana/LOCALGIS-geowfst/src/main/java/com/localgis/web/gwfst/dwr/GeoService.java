/**
 * GeoService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.dwr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.dao.LocalgisMunicipioDAO;
import com.localgis.web.core.dao.sqlmap.LocalgisMunicipioDAOImpl;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapManagerImpl;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.gwfst.config.ShowFeatureMapConfiguration;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Servicio con utilidades GIS
 */
public class GeoService {

	/**
	 * Logger
	 */
	private static Log logger = LogFactory.getLog(GeoService.class);
	
	/**
	 * Devuelve el nombre y el número de la calle mas cercana al elemento geométrico 
	 * @param featureCenterLon: Centro de la longitud del elemento geométrico
	 * @param featureCenterLat: Centro de la latitud del elemento geométrico
	 * @param srid: Proyección
	 * @param idMunicipio: Identificador de municipio
	 * @return String: El nombre y el número de la calle mas cercana al elemento geométrico 
	 */
    public static String getNearestStreet(String featureCenterLon, String featureCenterLat, String srid, String idMunicipio) {
    	String streetNameNumber = null;
    	
    	Connection connection = null;
    	PreparedStatement preparedStatement = null;
    	ResultSet rsSQL=null;
    	try{
    		logger.debug("Inicio getNearestStreet");
    		String driver = Configuration.getPropertyString(Configuration.PROPERTY_JDBC_DRIVER);
    		String urlConnection = "jdbc:" + ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_DB_CONNECTION) + "://"
    		+ Configuration.getPropertyString(Configuration.PROPERTY_DB_HOST) + ":" + Configuration.getPropertyString(Configuration.PROPERTY_DB_PORT)
    		+ "/" + Configuration.getPropertyString(Configuration.PROPERTY_DB_NAME);
    		String user = Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME);
    		String password = Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD);
    		Class.forName(driver);
    	    connection = DriverManager.getConnection(urlConnection, user, password);
    	    
    	    if (connection == null){
    	    	logger.warn("No se puede obtener la conexión");
    	    }

    	    String sql = "select (v.nombreviaine || ' ' || np.rotulo) as calle, st_distance(st_transform(geomfromtext('POINT(" + featureCenterLon + " " + featureCenterLat + ")'," + srid + "),4230), v.\"GEOMETRY\") as distancia"
        	+ " from vias v join numeros_policia np on(v.id=np.id_via)" 
        	+ " where v.id_municipio=" + idMunicipio + " and v.\"GEOMETRY\" is not null and v.nombreviaine is not null"
        	+ " order by distancia asc limit 1";
    	    
    	    preparedStatement = connection.prepareStatement(sql);
    	    rsSQL=preparedStatement.executeQuery();
    	    rsSQL.next();
    	    streetNameNumber = rsSQL.getString("calle");
    	    logger.error("RESULTADO: " + streetNameNumber);
    	}
    	catch(Exception e){
    		logger.error("ERROR al actualizar el rol:"+e.getMessage());
    		try {connection.rollback();} catch (Exception ex2) {}
    	}
    	finally{
    		try{preparedStatement.close();} catch (Exception ex2) {}
    		try{connection.close(); rsSQL.close();} catch (Exception ex2) {}
    	}    	   
        return streetNameNumber;
    }
    
	
	/**
	 * Devuelve el código INE completo del municipio en donde se encuentra una geometría 
	 * @param srid: Proyección
	 * @param geometry: Geometría
	 * @return String: El código INE completo del municipio
	 */
    public static String getMunicipioByGeometry(String srid, String geometry, String idMunicipio) {
    	LocalgisManagerBuilder localgisManagerBuilder = null;
		try {
			localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();		
	    	LocalgisMapManager localgismapmanager = localgisManagerBuilder.getLocalgisMapManager();
	    	GeopistaMunicipio geopistaMunicipio = localgismapmanager.selectMunicipioByGeometry(srid, geometry);
	    	if(geopistaMunicipio != null){
	    		return geopistaMunicipio.getIdProvincia() + geopistaMunicipio.getIdIne();
	    	}
		} catch (LocalgisConfigurationException | LocalgisInitiationException e) {
			logger.error(e);
		}
    	return idMunicipio;
    }
	
}
