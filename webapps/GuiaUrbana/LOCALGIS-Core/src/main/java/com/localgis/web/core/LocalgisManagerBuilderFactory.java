/**
 * LocalgisManagerBuilderFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.wm.util.FileUtil;
import com.localgis.web.core.wms.WMSConfigurator;

/**
 * Factoría para crear instancias LocalgisManagerBuilder
 * @author albegarcia
 *
 */
public class LocalgisManagerBuilderFactory {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisManagerBuilderFactory.class);

    /**
     * Fichero de configuracion de framework de persistencia
     */
    private static final String RESOURCE_DAO_XML = "com/localgis/web/core/dao/dao.xml";
    
    /**
     * Locales por defecto soportadas
     */
    private static final String DEFAULT_LOCALES_AVAILABLE = "es_ES,ca_ES,gl_ES,eu_ES,va_ES";
    private static final String DEFAULT_LOCALES_DEFAULT = "es_ES";

    /**
     * Propiedad conexion JDBC para ibatis
     */
    private static final String JDBC_URL_CONNECTION = "jdbc.urlConnection";
    
    /**
     * Método para crear un LocalgisManagerBuilder a partir de las locales
     * pasadas como parametros. Este método es bastante pesado por lo que se
     * recomienda instanciar una sola vez el LocalgisManagerBuilder
     * 
     * @param locale
     *            Locales
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error de configuracion
     * @throws LocalgisInitiationException
     *             Si ocurre algun error en la inicializacion
     */
    public static LocalgisManagerBuilder createLocalgisManagerBuilder(String locale) throws LocalgisConfigurationException, LocalgisInitiationException {
    	return createLocalgisManagerBuilder(locale,true);
    }
    public static LocalgisManagerBuilder createLocalgisManagerBuilder(String locale,boolean useJDBCEnvironment) throws LocalgisConfigurationException, LocalgisInitiationException {
        try {
            Collection availableLocales = getAvailableLocales();

            String localeSelected = getLocaleSelected(locale, availableLocales);

            /*
             * Obtención de la cadena de conexion jdbc a partir de la configuracion en funcion del connectiontype (postgis o oracle)
             */
            String connectionType = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_CONNECTIONTYPE);
            String dbHost = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_HOST);
            String dbPort = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_PORT);
            String dbName = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_NAME);
            String jdbcUrlConnection;
            int connectionTypeInt;
            if (connectionType.equals("postgis")) {
                jdbcUrlConnection = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;
                connectionTypeInt = WMSConfigurator.POSTGIS_CONNECTION;
            } else if (connectionType.equals("oraclespatial")) {
                jdbcUrlConnection = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+dbName;
               connectionTypeInt = WMSConfigurator.ORACLESPATIAL_CONNECTION;
            } else {
                logger.error("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
                throw new LocalgisConfigurationException("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
        	}
            
            /*
             * Creacion del Dao Manager de iBATIS
             */
            Reader reader = Resources.getResourceAsReader(RESOURCE_DAO_XML);
            Properties propertiesDaoManager = new Properties();
            if (useJDBCEnvironment)
            	propertiesDaoManager.put(Configuration.PROPERTY_JDBC_ENVIRONMENT, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_JDBC_ENVIRONMENT));
            propertiesDaoManager.put(Configuration.PROPERTY_DB_CONNECTIONTYPE, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_CONNECTIONTYPE));
            propertiesDaoManager.put(Configuration.PROPERTY_JDBC_DRIVER, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_JDBC_DRIVER));
            propertiesDaoManager.put(JDBC_URL_CONNECTION, jdbcUrlConnection);
            propertiesDaoManager.put(Configuration.PROPERTY_DB_USERNAME, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_USERNAME));
            propertiesDaoManager.put(Configuration.PROPERTY_DB_PASSWORD, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_PASSWORD));

            DaoManager daoManager = DaoManagerBuilder.buildDaoManager(reader, propertiesDaoManager);
            
            String wmsConfiguratorClassStr = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_WMSCONFIGURATOR_CLASS);
            Class wmsConfiguratorClass = Class.forName(wmsConfiguratorClassStr);
            Constructor constructorWMSConfigurator = wmsConfiguratorClass.getConstructor(new Class[] {DaoManager.class, String.class, int.class});
            WMSConfigurator wmConfigurator = (WMSConfigurator)constructorWMSConfigurator.newInstance(new Object[] {daoManager, localeSelected, new Integer(connectionTypeInt)});
            
            FileUtil.activeErrorFile();
            
            return new LocalgisManagerBuilder(daoManager, wmConfigurator, localeSelected, availableLocales);

        } catch (Exception e) {
            logger.error("Error instanciando manager de localgis.", e);
            throw new LocalgisInitiationException("Error instanciando manager de localgis.",e);
        }
    }

    
    public static DaoManager getDaoManager() throws LocalgisConfigurationException, LocalgisInitiationException {
        try {
            /*
             * Obtención de la cadena de conexion jdbc a partir de la configuracion en funcion del connectiontype (postgis o oracle)
             */
            String connectionType = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_CONNECTIONTYPE);
            String dbHost = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_HOST);
            String dbPort = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_PORT);
            String dbName = Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_NAME);
            String jdbcUrlConnection;
            int connectionTypeInt;
            if (connectionType.equals("postgis")) {
                jdbcUrlConnection = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;
                connectionTypeInt = WMSConfigurator.POSTGIS_CONNECTION;
            } else if (connectionType.equals("oraclespatial")) {
                jdbcUrlConnection = "jdbc:oracle:thin:@"+dbHost+":"+dbPort+":"+dbName;
                connectionTypeInt = WMSConfigurator.ORACLESPATIAL_CONNECTION;
            } else {
                logger.error("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
                throw new LocalgisConfigurationException("El parametro \""+ Configuration.PROPERTY_DB_CONNECTIONTYPE+"\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
            }
            
            /*
             * Creacion del Dao Manager de iBATIS
             */
            Reader reader = Resources.getResourceAsReader(RESOURCE_DAO_XML);
            Properties propertiesDaoManager = new Properties();
            propertiesDaoManager.put(Configuration.PROPERTY_JDBC_ENVIRONMENT, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_JDBC_ENVIRONMENT));
            propertiesDaoManager.put(Configuration.PROPERTY_DB_CONNECTIONTYPE, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_CONNECTIONTYPE));
            propertiesDaoManager.put(Configuration.PROPERTY_JDBC_DRIVER, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_JDBC_DRIVER));
            propertiesDaoManager.put(JDBC_URL_CONNECTION, jdbcUrlConnection);
            propertiesDaoManager.put(Configuration.PROPERTY_DB_USERNAME, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_USERNAME));
            propertiesDaoManager.put(Configuration.PROPERTY_DB_PASSWORD, Configuration.getPropertyStringWithFilter(Configuration.PROPERTY_DB_PASSWORD));

            DaoManager daoManager = DaoManagerBuilder.buildDaoManager(reader, propertiesDaoManager);
                       
            return(daoManager);

        } catch (Exception e) {
            logger.error("Error devolviendo DAO Manager.", e);
            throw new LocalgisInitiationException("Error instanciando manager de localgis.",e);
        }
    }
    
    
    /**
     * Método para crear un LocalgisManagerBuilder
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisInitiationException Si ocurre algun error en la inicializacion
     */
    public static LocalgisManagerBuilder createLocalgisManagerBuilder() throws LocalgisConfigurationException, LocalgisInitiationException {
        return LocalgisManagerBuilderFactory.createLocalgisManagerBuilder(null);
    }
    
    /**
     * Método para crear un LocalgisManagerBuilder
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisInitiationException Si ocurre algun error en la inicializacion
     */
    public static LocalgisManagerBuilder createLocalgisManagerBuilder(boolean useJDBCEnvironment) throws LocalgisConfigurationException, LocalgisInitiationException {
        return LocalgisManagerBuilderFactory.createLocalgisManagerBuilder(null,useJDBCEnvironment);
    }

    /**
     * Devuelve las locales a utilizar a partir de las locales que se pasan como
     * parametro. Si no se pasa ninguna utilizamos la del sistema, siempre que
     * la del sistema este dentro de las locales disponibles. Si no obtenemos la
     * de por defecto
     * 
     * @param locale
     *            La locale que se quiere seleccionar
     * @param availableLocales
     *            La locale que se quiere seleccionar
     * @return La locale que se desea utilizar. Se devolverá siempre que esté
     *         dentro de las locales disponibles
     */
    private static String getLocaleSelected(String locale, Collection availableLocales) {
        /*
         * Configuracion de la locale a utilizar. Si no se pasa ninguna
         * utilizamos la del sistema, siempre que la del sistema este dentro
         * de las locales disponibles. Si no obtenemos la de por defecto
         */
        if (locale == null) {
            locale = Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
         }
        if (availableLocales.contains(locale)) {
            return locale;
        } else {
            String localeDefault;
            try {
                localeDefault = Configuration.getPropertyString(Configuration.PROPERTY_LOCALES_DEFAULT);
            } catch (LocalgisConfigurationException e) {
                localeDefault = DEFAULT_LOCALES_DEFAULT;
            }
            return localeDefault;
        }
    }

    /**
     * Devuelve las locales disponibles
     *@return Las locales disponibles como una coleccion de String 
     */
    private static Collection getAvailableLocales() {
        /*
         * Configuracion de la locale a utilizar. Si no se pasa ninguna
         * utilizamos la del sistema, siempre que la del sistema este dentro
         * de las locales disponibles. Si no obtenemos la de por defecto
         */
        String localesAvailable;
        try {
            localesAvailable = Configuration.getPropertyString(Configuration.PROPERTY_LOCALES_AVAILABLE);
        } catch (LocalgisConfigurationException e) {
            localesAvailable = DEFAULT_LOCALES_AVAILABLE;
        }
        String[] locales = localesAvailable.split(",");
        Collection result = new ArrayList();
        for (int i = 0; i < locales.length; i++) {
            result.add(locales[i]);
        }
        return result;
    }

}
