/**
 * ConfigurationManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigurationManager {

    Logger log = Logger.getLogger(ConfigurationManager.class);

    private static final String configFile = "config.properties";

    private static Properties propSerializator = new Properties();
    private static Properties propApplication = new Properties();
//    private static Properties propEntityLifeTime = new Properties();

    static {
        new ConfigurationManager();
    }

    public ConfigurationManager() {
        //propApplication = new Properties();
        //propSerializator = new Properties();

        loadApplicationProperties();
        loadSerializatorProperties();
//        loadEntityLifeTimeProperties();

//        log.debug("Propiedad baseDir:" + getApplicationProperty("baseDir"));
//        log.debug("Propiedad limitDate:" + getApplicationProperty("limitDate"));
    }

    protected void loadApplicationProperties() {
        try {
            propApplication = new Properties();

            log.debug("[loadApplicationProperties()] Loading application properties...");

            InputStream isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configFile);

            String configPath = null;
            if (isProps != null) {
                propApplication.load(isProps);
//                configPath = propApplication.getProperty("servidormovilidad_config.path");
                isProps.close();

//                isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configPath);
//                propApplication.load(isProps);
//                isProps.close();
            }

            log.debug("Cargadas propiedades desde: " + configPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    protected void loadEntityLifeTimeProperties() {
//        try {
//            propEntityLifeTime = new Properties();
//
//            log.debug("[loadApplicationProperties()] Loading entity properties...");
//
//            InputStream isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configFile);
//
//            String configPath = null;
//            if (isProps != null) {
//                propEntityLifeTime.load(isProps);
//                configPath = propEntityLifeTime.getProperty("entity_lifetime_config.path");
//                isProps.close();
//
//                isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configPath);
//                propEntityLifeTime.load(isProps);
//                isProps.close();
//            }
//
//            log.debug("Cargadas propiedades de tiempo de vida de entidades desde: " + configPath);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    protected void loadSerializatorProperties() {
        try {
            propSerializator = new Properties();

            log.debug("[loadApplicationProperties()] Loading serializator properties...");

            InputStream isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configFile);

            if (isProps != null) {
                propSerializator.load(isProps);
                String configPath = propSerializator.getProperty("serializator_config.path");
                isProps.close();

                isProps = ConfigurationManager.class.getClassLoader().getResourceAsStream(configPath);
                if (isProps!=null){
                	propSerializator.load(isProps);
                	isProps.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSerializatorProperty(String sPropiedad) {
        String sRet = "";
        sRet = propSerializator.getProperty(sPropiedad);
        if (sRet == null) {
            sRet = "";
        }
        return sRet;
    }

    public static String getApplicationProperty(String sPropiedad) {
        String sRet = null;
        sRet = propApplication.getProperty(sPropiedad);

        return sRet;
    }

//    public static String getEntityLifeTimeProperty(String sPropiedad) {
//        String sRet = propEntityLifeTime.getProperty(sPropiedad);
//        return sRet;
//    }
}
