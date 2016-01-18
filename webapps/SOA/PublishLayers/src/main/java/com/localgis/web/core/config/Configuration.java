/**
 * Configuration.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;

public class Configuration {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(Configuration.class);

    /**
     * Constantes relacionadas con la resolucion de los mapas. En principio
     * trabajamos en unidades metricas y a una resolucion de 72 ppp
     */
    public static final double INCHES_PER_UNIT = 39.3701;
    public static final double DOTS_PER_INCH = 72;

    /*
     * Escala mínima se refiere a la escala más alejada a la que se muestra
     * algo. Escala máxima se refiere a la escala más cercana a la que se
     * muestra algo. Puede llevar a confusión, porque al no usar las escalas de
     * forma normalizada (haciendo la division 1 / algo), las escalas minimas
     * tienen un valor mayor que las máximas. Sin embargo, si hiciéramos el
     * inverso estaría resuelto. En el mapserver parece que estos conceptos
     * están al reves. A lo que ellos llaman maxescala nosotros le llamamos
     * minscale
     */
    // Escala mas alejada a la que se muestran las capas en general
    public static final double MIN_SCALE_LAYERS = 13107200;
    
    // Escala más alejada a la que se muestran las provincias
    public static final double MIN_SCALE_PROVINCIAS = 52428800;
        
    // Escala mas cercana a la que se muestran las provincias
    public static final double MAX_SCALE_PROVINCIAS = 102400;
    
    // Escala mas alejada a la que se muestran los municipios
    public static final double MIN_SCALE_MUNICIPIOS = 1638400;
    
    // Escala mas cercana a la que se muestran los municipios
    public static final double MAX_SCALE_MUNICIPIOS = 102400;
    
    // Numero de niveles de zoom
    public static final int ZOOM_LEVELS = 17;

    /*
     * Constantes para definir el bbox de España
     */
    public static final double DEFAULT_SPAIN_MINX = -1178301.3735306475;
    public static final double DEFAULT_SPAIN_MINY = 2849375.421096337;
    public static final double DEFAULT_SPAIN_MAXX = 1069687.9321801807;
    public static final double DEFAULT_SPAIN_MAXY = 5045568.449258616;
    
    /**
     * Fichero de configuracion principal
     */
    private static final String PROPERTY_FILE = "localgis.properties";

    /**
     * Constantes relacionadas con el mapserver
     */
    public static final String MAPSERVER_CONFIG_FILE_SKELETON = "localgis_skeleton.map";
    public static final String MAPSERVER_CONFIG_FILE_SKELETON_STATIC = "localgis_skeleton_static.map";
    public static final String MAPSERVER_CONFIG_FILE_SKELETON_EXTRA = "localgis_skeleton_extra.map";
    public static final String MAPSERVER_CONFIG_FILE_DIRECTORY = "/apps/localgis/layers";
    public static final String MAPSERVER_CONFIG_FILE_PREFIX = "localgis_";
    public static final String MAPSERVER_CONFIG_FILE_PREFIX_PUBLIC = "localgis_public_";
    public static final String MAPSERVER_CONFIG_FILE_PREFIX_PRIVATE = "localgis_private_";
    public static final String MAPSERVER_CONFIG_FILE_EXTENSION = "map";
    public static final String MAPSERVER_STYLES_FILE_DIRECTORY = "/htdocs";
    public static final String MAPSERVER_STYLES_FILE_PREFIX_PUBLIC = "styles_localgis_public_";
    public static final String MAPSERVER_STYLES_FILE_PREFIX_PRIVATE = "styles_localgis_private_";
    public static final String MAPSERVER_STYLES_FILE_EXTENSION = "xml";
    public static final String MAPSERVER_CONFIG_FILE_ORTOFOTO_LAYER_NAME = "lcg_ortofoto";
    public static final String MAPSERVER_CONFIG_FILE_PROVINCIAS_LAYER_NAME = "lcg_provincias";
    public static final String MAPSERVER_CONFIG_FILE_MUNICIPIOS_LAYER_NAME = "lcg_municipios";
    public static final String MAPSERVER_CONFIG_FILE_POLYGON_REPORTS_LAYER_NAME = "lcg_polygon_reports";
    public static final String MAPSERVER_CONFIG_FILE_LINE_REPORTS_LAYER_NAME = "lcg_line_reports";
    public static final String MAPSERVER_CONFIG_FILE_POINT_REPORTS_LAYER_NAME = "lcg_point_reports";
    public static final String MAPSERVER_CONFIG_FILE_REPORTS_TABLE_NAME = "tableName";
    public static final String MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_COLUMN_NAME = "identifierColumnName";
    public static final String MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_VALUE = "identifierValue";
    public static final String MAPSERVER_CONFIG_FILE_PARCELAS_DATA_ORACLESPATIAL = "GEOMETRY from (SELECT parcelas.oid as id_localgis, Parcelas.*, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID=\\$ID_MUNICIPIO\\$ AND Fecha_baja IS NULL ) USING UNIQUE id_localgis SRID NULL";
    public static final String MAPSERVER_CONFIG_FILE_PARCELAS_DATA_POSTGIS = "geometry_localgis from (SELECT parcelas.\"GEOMETRY\" as geometry_localgis, parcelas.oid as id_localgis, Parcelas.*, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID=\\$ID_MUNICIPIO\\$ AND Fecha_baja IS NULL ) AS parcelas USING UNIQUE id_localgis USING SRID=\\$SRID\\$";
    public static final String MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_ORACLESPATIAL = "GEOMETRY_\\$SRID\\$ from (SELECT municipios.\"GEOMETRY_\\$SRID\\$\", municipios.rowid as id_localgis from localgisguiaurbana.municipios municipios ) USING UNIQUE id_localgis SRID \\$SRID\\$";
    public static final String MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_POSTGIS = "geometry_localgis from (SELECT transform(municipios.\"GEOMETRY\", \\$SRID\\$) as geometry_localgis, municipios.oid as id_localgis from municipios ) AS lcg_municipios USING UNIQUE id_localgis USING SRID=\\$SRID\\$";
    public static final String MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_ORACLESPATIAL = "GEOMETRY_\\$SRID\\$ from (SELECT provincias.\"GEOMETRY_\\$SRID\\$\", provincias.rowid as id_localgis, provincias.nombreoficial as nombreoficial from localgisguiaurbana.provincias provincias ) USING UNIQUE id_localgis SRID \\$SRID\\$";
    public static final String MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_POSTGIS = "geometry_localgis from (SELECT transform(provincias.\"GEOMETRY\", \\$SRID\\$) as geometry_localgis, provincias.oid as id_localgis, provincias.nombreoficial as nombreoficial from provincias ) AS lcg_provincias USING UNIQUE id_localgis USING SRID=\\$SRID\\$";
    
    /**
     * Propiedades de configuracion de la base de datos
     */
    public static final String PROPERTY_DB_CONNECTIONTYPE = "db.connectiontype";
    public static final String PROPERTY_DB_USERNAME = "db.username";
    public static final String PROPERTY_DB_PASSWORD = "db.password";
    public static final String PROPERTY_DB_HOST = "db.host";
    public static final String PROPERTY_DB_PORT = "db.port";
    public static final String PROPERTY_DB_NAME = "db.name";
    public static final String PROPERTY_DB_ORACLESERVICE = "db.oracleservice";

    /**
     * Propiedades de configuracion JDBC
     */
    public static final String PROPERTY_JDBC_DRIVER = "jdbc.driver";

    /**
     * Propiedades de configuracin del wms de configuracion
     */
    public static final String PROPERTY_WMSCONFIGURATOR_CLASS = "wmsconfigurator.class";
    
    /**
     * Propiedades de configuracion de las locales
     */
    public static final String PROPERTY_LOCALES_AVAILABLE = "locales.available";
    public static final String PROPERTY_LOCALES_DEFAULT = "locales.default";

    /**
     * Propiedades de configuracion del servidor de mapas
     */
    public static final String PROPERTY_MAPSERVER_ENCODING = "mapserver.encoding";
    public static final String PROPERTY_MAPSERVER_DIRECTORY = "mapserver.directory";
    public static final String PROPERTY_MAPSERVER_URL_SERVER = "mapserver.url.server";
    public static final String PROPERTY_MAPSERVER_URL_DIRECTORY = "mapserver.url.directory";
    public static final String PROPERTY_MAPSERVER_URL_STYLES_DIRECTORY = "mapserver.url.stylesDirectory";
    public static final String PROPERTY_MAPSERVER_URL_FILE = "mapserver.url.file";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_ORTOFOTOS_DIRECTORY = "mapserver.configFile.ortofotosDirectory";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_OUTLINECOLOR = "mapserver.configFile.provinciasOutlinecolor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_ATTRIBUTE = "mapserver.configFile.provinciasNameAttribute";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_COLOR = "mapserver.configFile.provinciasNameColor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_SIZE = "mapserver.configFile.provinciasNameSize";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_MUNICIPIOS_OUTLINECOLOR = "mapserver.configFile.municipiosOutlinecolor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR = "mapserver.configFile.reports.polygonSelectedOutlineColor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_FILL_COLOR = "mapserver.configFile.reports.polygonSelectedFillColor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_LINE_SELECTED_COLOR = "mapserver.configFile.reports.lineSelectedColor";
    public static final String PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POINT_SELECTED_COLOR = "mapserver.configFile.reports.pointSelectedColor";

    /**
     * Propiedades de configuracion de los sistemas de proyeccion
     */
    public static final String PROPERTY_SPAIN_BBOX_EPSG_CODE = "spain.bbox.epsgCode";
    public static final String PROPERTY_SPAIN_MINX = "spain.minX";
    public static final String PROPERTY_SPAIN_MINY = "spain.minY";
    public static final String PROPERTY_SPAIN_MAXX = "spain.maxX";
    public static final String PROPERTY_SPAIN_MAXY = "spain.maxY";
    
    /**
     * Propiedades de configuracion de los informes
     */
    public static final String PROPERTY_REPORTS_LAYERS = "reports.layers";
    public static final String PROPERTY_REPORTS_LAYER_PARCELAS = "reports.layerParcelas";
    
    /**
     * Propiedades de configuracion de la guia urbana
     */
    public static final String PROPERTY_GUIAURBANA_PUBLIC_MAP = "guiaurbana.urlPublicMap";
    public static final String PROPERTY_GUIAURBANA_PRIVATE_MAP = "guiaurbana.urlPrivateMap";
    
    public static final String PROPERTY_URL_WPS_RUTAS = "wpsRutas";
    /**
     * Variables para sustituir en la configuracion del servidor de mapas
     */
    public static final String VAR_MAPSERVER_LAYERS = "\\$LAYERS_MAPSERVER\\$";
    public static final String VAR_MAPSERVER_MAX_EXTENT = "\\$MAX_EXTENT\\$";
    public static final String VAR_MAPSERVER_ENCODING = "\\$ENCODING\\$";
    public static final String VAR_MAPSERVER_PROJECTION = "\\$PROJECTION\\$";
    public static final String VAR_MAPSERVER_SRID = "\\$SRID\\$";
    public static final String VAR_MAPSERVER_ONLINE_RESOURCE = "\\$ONLINE_RESOURCE\\$";
    public static final String VAR_MAPSERVER_ID_MUNICIPIO = "\\$ID_MUNICIPIO\\$";
    public static final String VAR_MAPSERVER_CONNECTIONTYPE = "\\$MAPSERVER_CONNECTIONTYPE\\$";
    public static final String VAR_MAPSERVER_CONNECTION = "\\$MAPSERVER_CONNECTION\\$";
    public static final String VAR_MAPSERVER_ORTOFOTOS_DIRECTORY = "\\$ORTOFOTOS_DIRECTORY\\$";
    public static final String VAR_MAPSERVER_ORTOFOTO_LAYER_NAME = "\\$ORTOFOTO_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_ORTOFOTO_EXTENSION = "\\$ORTOFOTO_EXTENSION\\$";
    public static final String VAR_MAPSERVER_ORTOFOTO_PROJECTION = "\\$ORTOFOTO_PROJECTION\\$";
    public static final String VAR_MAPSERVER_ORTOFOTO_SRID = "\\$ORTOFOTO_SRID\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_LAYER_NAME = "\\$PROVINCIAS_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_OUTLINECOLOR = "\\$PROVINCIAS_OUTLINECOLOR\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_MAX_EXTENT = "\\$PROVINCIAS_MAX_EXTENT\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_NAME_ATTRIBUTE = "\\$PROVINCIAS_NAME_ATTRIBUTE\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_NAME_COLOR = "\\$PROVINCIAS_NAME_COLOR\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_NAME_SIZE = "\\$PROVINCIAS_NAME_SIZE\\$";
    public static final String VAR_MAPSERVER_MUNICIPIOS_LAYER_NAME = "\\$MUNICIPIOS_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_POLYGON_REPORTS_LAYER_NAME = "\\$POLYGON_REPORTS_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_LINE_REPORTS_LAYER_NAME = "\\$LINE_REPORTS_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_POINT_REPORTS_LAYER_NAME = "\\$POINT_REPORTS_LAYER_NAME\\$";
    public static final String VAR_MAPSERVER_REPORTS_TABLE_NAME = "\\$REPORTS_TABLE_NAME\\$";
    public static final String VAR_MAPSERVER_REPORTS_IDENTIFIER_COLUMN_NAME = "\\$REPORTS_IDENTIFIER_COLUMN_NAME\\$";
    public static final String VAR_MAPSERVER_REPORTS_IDENTIFIER_VALUE = "\\$REPORTS_IDENTIFIER_VALUE\\$";
    public static final String VAR_MAPSERVER_MUNICIPIOS_OUTLINECOLOR = "\\$MUNICIPIOS_OUTLINECOLOR\\$";
    public static final String VAR_MAPSERVER_MUNICIPIOS_MIN_SCALE = "\\$MUNICIPIOS_MIN_SCALE\\$";
    public static final String VAR_MAPSERVER_MUNICIPIOS_MAX_SCALE = "\\$MUNICIPIOS_MAX_SCALE\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_MIN_SCALE = "\\$PROVINCIAS_MIN_SCALE\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_MAX_SCALE = "\\$PROVINCIAS_MAX_SCALE\\$";
    public static final String VAR_MAPSERVER_MUNICIPIOS_DATA = "\\$MUNICIPIOS_DATA\\$";
    public static final String VAR_MAPSERVER_PROVINCIAS_DATA = "\\$PROVINCIAS_DATA\\$";
    public static final String VAR_MAPSERVER_PARCELAS_DATA = "\\$PARCELAS_DATA\\$";
    public static final String VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR = "\\$REPORTS_POLYGON_SELECTED_OUTLINE_COLOR\\$";
    public static final String VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_FILL_COLOR = "\\$REPORTS_POLYGON_SELECTED_FILL_COLOR\\$";
    public static final String VAR_MAPSERVER_REPORTS_LINE_SELECTED_COLOR = "\\$REPORTS_LINE_SELECTED_COLOR\\$";
    public static final String VAR_MAPSERVER_REPORTS_POINT_SELECTED_COLOR = "\\$REPORTS_POINT_SELECTED_COLOR\\$";
    
    /**
     * Properties para cargar configuracion
     */
    private static Properties properties;
    
    static {
        try {
            initialize();
        } catch (LocalgisInitiationException e) {
            logger.info("No se ha podido iniciar la configuracion. Se inicializa a vacia", e);
            properties = new Properties();
        }
    }
    
    private static void initialize() throws LocalgisInitiationException{
        /*
         * Obtenemos la configuracion de un fichero de propiedades que sera obligatorio tener
         */
        properties = new Properties();
        InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        if (inputStream == null) {
            inputStream = ClassLoader.getSystemResourceAsStream(PROPERTY_FILE);
            if (inputStream == null) {
            	logger.error("No existe el fichero de configuracion");
                throw new LocalgisInitiationException("No existe el fichero de configuracion");
            }
        }
        
        try {
        properties.load(inputStream);
        inputStream.close();
        } catch (IOException e) {
        	logger.error("Error al leer el fichero de configuracion");
            throw new LocalgisInitiationException("Error al leer el fichero de configuracion");
        }
    }

    /**
     * Este método establece un parametro del fichero de configuracion
     * 
     * @param property
     *            Propiedad del fichero de configuración
     * @param value
     *            Valor a establecer
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error de configuracion
     */
    public static void setPropertyString(String property, String value) throws LocalgisConfigurationException {
        if (properties == null) {
        	logger.error("No se ha iniciado correctamente la configuracion");
            throw new LocalgisConfigurationException("No se ha iniciado correctamente la configuracion");
        }
        properties.setProperty(property, value);
    }
    
    /**
     * Este método obtiene un parametro del fichero de configuracion Se devuelve
     * un String
     * 
     * @param property
     *            Propiedad del fichero de configuración
     * @return String con el valor de la propiedad obtenida
     * @throws LocalgisConfigurationException
     *             Si no se encuentra la propiedad
     */
    public static String getPropertyString(String property) throws LocalgisConfigurationException {
        if (properties == null) {
        	logger.error("No se ha iniciado correctamente la configuracion");
            throw new LocalgisConfigurationException("No se ha iniciado correctamente la configuracion");
        }
        String value = properties.getProperty(property);
        if(value == null){
            logger.error("No se ha encontrado el parametro de configuracion: "+property);
            throw new LocalgisConfigurationException("No se ha encontrado el parametro de configuracion: "+property+" en el fichero de configuracion");
        }
        return value;
    }
}
