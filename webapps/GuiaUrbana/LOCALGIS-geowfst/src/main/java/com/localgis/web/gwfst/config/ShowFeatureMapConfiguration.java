/**
 * ShowFeatureMapConfiguration.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Clase de configuración
 */
public class ShowFeatureMapConfiguration {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(ShowFeatureMapConfiguration.class);

    /**
     * Fichero de configuracion principal
     */
    private static final String PROPERTY_FILE = "showFeatureMap.properties";

    /**
     * Propiedades de configuracion
     */
    public static final String PROPERTY_WMSMANAGER_URL_HOST = "url.wmsManager";    
    public static final String PROPERTY_PROCEDUREWS_URL_HOST = "url.procedureWS";
    public static final String PROPERTY_LOCALGISWFSG_URL_HOST = "url.localgisWFSG";
    public static final String PROPERTY_GEOSERVER_URL_HOST = "geoserver.url.host";
    public static final String PROPERTY_GEOSERVER_URL_USER = "geoserver.url.user";
    public static final String PROPERTY_GEOSERVER_URL_PASSWORD = "geoserver.url.password";
    public static final String PROPERTY_GEOSERVER_DATASOURCE_NAME = "geoserver.datasource.name";
    //public static final String PROPERTY_GEOSERVER_DATASOURCE_HOST= "geoserver.datasource.host";
    //public static final String PROPERTY_GEOSERVER_DATASOURCE_PORT = "geoserver.datasource.port";
    //public static final String PROPERTY_GEOSERVER_DATASOURCE_DATABASE = "geoserver.datasource.database";
    public static final String PROPERTY_GEOSERVER_DATASOURCE_SCHEMA = "geoserver.datasource.schema";
    //public static final String PROPERTY_GEOSERVER_DATASOURCE_USER = "geoserver.datasource.user";
    //public static final String PROPERTY_GEOSERVER_DATASOURCE_PASSWORD = "geoserver.datasource.password";
    public static final String PROPERTY_DOMAIN = "domain";    
    public static final String PROPERTY_DB_CONNECTION = "db.connection";
    
    /**
     * Properties para cargar configuracion
     */
    private static Properties properties;
    
    /**
     * Inicialización de la configuración
     */
    static {
        try {
            initialize();
        } catch (LocalgisInitiationException e) {
            logger.fatal("No se ha podido iniciar la configuracion", e);
        }
    }
    
    /**
     * Inicialización
     * @throws LocalgisInitiationException
     */
    private static void initialize() throws LocalgisInitiationException{
        properties = new Properties();
        InputStream inputStream = ShowFeatureMapConfiguration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(PROPERTY_FILE);
            if (inputStream == null) {
                throw new LocalgisInitiationException("No existe el fichero de configuracion");
            }
        }
        
        try {
        properties.load(inputStream);
        inputStream.close();
        } catch (IOException e) {
            throw new LocalgisInitiationException("Error al leer el fichero de configuracion");
        }
    }
    
    /**
     * Este método obtiene un parametro del fichero de configuracion se devuelve un String
     * @param property: Propiedad del fichero de configuración
     * @return String: con el valor de la propiedad obtenida
     * @throws LocalgisConfigurationException: Si no se encuentra la propiedad
     */
    public static String getPropertyString(String property) throws LocalgisConfigurationException {
        if (properties == null) {
            throw new LocalgisConfigurationException("No se ha iniciado correctamente la configuracion");
        }
        return properties.getProperty(property,"");
    }
}
