/**
 * DaoManagerFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package tests.dao;

import java.io.Reader;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.DaoManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;

public class DaoManagerFactory {

    /**
     * Fichero de configuracion de framework de persistencia
     */
    private static final String RESOURCE_DAO_XML = "com/localgis/web/core/dao/dao.xml";

    /**
     * Propiedad conexion JDBC para ibatis
     */
    private static final String JDBC_URL_CONNECTION = "jdbc.urlConnection";

    private static DaoManager daoManager;

    public static void initializeDaoManagerFactory(String connectionType, String jdbcDriver, String dbHost, int dbPort, String dbName, String username, String password) {
        if (daoManager != null) {
            throw new RuntimeException("DaoManagerFactory ya inicializado");
        }
        try {
            /*
             * Obtención de la cadena de conexion jdbc a partir de la
             * configuracion en funcion del connectiontype (postgis o oracle)
             */
            String jdbcUrlConnection;
            if (connectionType.equals("postgis")) {
                jdbcUrlConnection = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            } else if (connectionType.equals("oraclespatial")) {
                jdbcUrlConnection = "jdbc:oracle:thin:@" + dbHost + ":" + dbPort + ":" + dbName;
            } else {
                throw new LocalgisConfigurationException("El parametro \"" + connectionType + "\" no tiene un valor válido. Valores validos \"postgis\" y \"oraclespatial\".");
            }

            /*
             * Creacion del Dao Manager de iBATIS
             */
            Reader reader = Resources.getResourceAsReader(RESOURCE_DAO_XML);
            Properties propertiesDaoManager = new Properties();
            propertiesDaoManager.put(Configuration.PROPERTY_DB_CONNECTIONTYPE, connectionType);
            propertiesDaoManager.put(Configuration.PROPERTY_JDBC_DRIVER, jdbcDriver);
            propertiesDaoManager.put(JDBC_URL_CONNECTION, jdbcUrlConnection);
            propertiesDaoManager.put(Configuration.PROPERTY_DB_USERNAME, username);
            propertiesDaoManager.put(Configuration.PROPERTY_DB_PASSWORD, password);

            daoManager = DaoManagerBuilder.buildDaoManager(reader, propertiesDaoManager);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el DaoMAnager", e);
        }
    }

    public static void finalizeDaoManagerFactory() {
        if (daoManager != null) {
            throw new RuntimeException("DaoManagerFactory no inicializado");
        }
        daoManager = null;
    }

    public static DaoManager getDaoManager() {
        if (daoManager == null) {
            throw new RuntimeException("DaoManagerFactory no inicializado");
        }
        return daoManager;
    }
}
