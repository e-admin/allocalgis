/**
 * LocalgisWebManagerConfiguration.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

public class LocalgisWebManagerConfiguration {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(LocalgisWebManagerConfiguration.class);

    /**
     * Fichero de configuracion principal
     */
    private static final String PROPERTY_FILE = "localgiswebmanager.properties";

    /**
     * Propiedades de configuracion
     */
    public static final String PROPERTY_GUIAURBANA_DYNAMIC_CSS = "guiaurbana.dynamicCSS";
    
    public static final String PROPERTY_GUIAURBANA_STATIC_CSS = "guiaurbana.staticCSS";

    public static final String PROPERTY_GUIAURBANA_DYNAMIC_CSS_OPENLAYERS = "guiaurbana.dynamicCSSOpenLayers";

    public static final String PROPERTY_GUIAURBANA_DYNAMIC_CSS_SAMPLE= "guiaurbana.dynamicCSSSample";

    public static final String PROPERTY_GUIAURBANA_PATH_ESCUDOS= "guiaurbana.path.escudos";

    public static final String PROPERTY_GUIAURBANA_CABECERA_ACTUAL= "guiaurbana.cabeceraActual";

    /**
     * Properties para cargar configuracion
     */
    private static Properties properties;
    
    static {
        try {
            initialize();
        } catch (LocalgisInitiationException e) {
            logger.fatal("No se ha podido iniciar la configuracion", e);
        }
    }
    
    private static void initialize() throws LocalgisInitiationException{
        /*
         * Obtenemos la configuracion de un fichero de propiedades que sera obligatorio tener
         */
        properties = new Properties();
        InputStream inputStream = LocalgisWebManagerConfiguration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(PROPERTY_FILE);
            if (inputStream == null) {
                throw new LocalgisInitiationException("No existe el fichero de configuracion: "+PROPERTY_FILE);
            }
        }
        
        try {
        properties.load(inputStream);
        inputStream.close();
        } catch (IOException e) {
            throw new LocalgisInitiationException("Error al leer el fichero de configuracion: "+PROPERTY_FILE);
        }
    }
    
    /**
     * Este mï¿½todo obtiene un parametro del fichero de configuracion Se devuelve
     * un String
     * 
     * @param property
     *            Propiedad del fichero de configuraciï¿½n
     * @return String con el valor de la propiedad obtenida
     * @throws LocalgisConfigurationException
     *             Si no se encuentra la propiedad
     */
    public static String getPropertyString(String property) throws LocalgisConfigurationException {
        if (properties == null) {
            throw new LocalgisConfigurationException("No se ha iniciado correctamente la configuracion");
        }
        String value = properties.getProperty(property);
        if(value == null){
            logger.error("No se ha encontrado el parametro de configuracion: "+property+" en el fichero de configuracion");
            throw new LocalgisConfigurationException("No se ha encontrado el parametro de configuracion: "+property+" en el fichero de configuracion");
        }
        return value;
    }
}
