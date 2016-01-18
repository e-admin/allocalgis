/**
 * UserPreferenceConstants.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app;

public class UserPreferenceConstants {

	/**
	 * Ruta de las variables del registro del sistema
	 */
    public static final String LOCALGIS_SYSTEM_RESGISTRY_PATH = "/com/geopista/app";
    
    /**
     * Configuracion por defecto
     */
	public static final String DEFAULT_ENTITYID = "geopista.DefaultEntityId";
    public static final String DEFAULT_LOCALE_KEY = "geopista.user.language";
	public static final String DEFAULT_LOGIN_USERNAME= "geopista.user.login";
	public static final String DEFAULT_MUNICIPALITY_ID = "geopista.DefaultCityId";
	public static final String DEFAULT_PROTOCOL = "protocol";
	
	/**
	 * Urls Generales
	 */
	public static final String LOCALGIS_SERVER_ADMCAR_SERVLET_URL = "geopista.conexion.administradorcartografia";
	public static final String LOCALGIS_SERVER_ADMCAR_URL = "geopista.conexion.servidor";
	public static final String LOCALGIS_SERVER_PRINCIPAL_URL = "localgis.conexion.principal";
	public static final String UPDATE_SERVER_URL = "UPDATE_SERVER_URL";
	public static final String MAPSERVER_URL = "localgis.url.mapserver";
	public static final String TOMCAT_URL = "geopista.url.tomcat";
	
	/**
	 * Servidor LocalGIS
	 */
	public static final String LOCALGIS_SERVER_URL = "geopista.conexion.serverADMCAR";
	public static final String LOCALGIS_SERVER_HOST = "geopista.conexion.serverIP"; //REVISAR
	public static final String LOCALGIS_SERVER_PORT = "geopista.conexion.port"; //REVISAR
	public static final String LOCALGIS_SERVER_HEARTBEAT = "geopista.conexion.heartbeat";
	
	/**
	 * Base de datos LocalGIS
	 */
	public static final String LOCALGIS_DATABASE_URL = "geopista.conexion.url";
	public static final String LOCALGIS_DATABASE_DB_NAME = "nombre.bd";
	public static final String LOCALGIS_DATABASE_DRIVER = "geopista.conexion.driver";	
	public static final String LOCALGIS_DATABASE_HOST = "geopista.conexion.DDBBIp";	 //REVISAR
	public static final String LOCALGIS_DATABASE_PORT = "geopista.conexion.port";	 //REVISAR
	public static final String LOCALGIS_DATABASE_USERNAME = "geopista.conexion.user";	
	public static final String LOCALGIS_DATABASE_PASSWORD = "geopista.conexion.pass";	
	
	/**
	 * Conexiï¿½n directa a la base de datos LocalGIS
	 */
	public static final String LOCALGIS_DATABASE_DIRECTCONNECTION_URL = "conexion.url";	
	public static final String LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER = "conexion.driver";	
	public static final String LOCALGIS_DATABASE_DIRECTCONNECTION_USER = "conexion.user";	
	public static final String LOCALGIS_DATABASE_DIRECTCONNECTION_PASS = "conexion.pass";	
	public static final String LOCALGIS_DATABASE_DIRECTCONNECTION_URL_DB = "conexion.urlbd";
	
	/**
	 * Conexiï¿½n a la base de datos de Jasper
	 */
	//public static final String LOCALGIS_DATABASE_JASPER_URL = "conexion.url.jasper";
	//public static final String  REPORTUSER_KEY = "conexion.user";
	//public static final String  REPORTPASS_KEY = "conexion.pass";
	//public static final String  REPORTURLJASPER_KEYBD = "conexion.urlbd.jasper";
	//public static final String  REPORTURLJASPER_KEY = "conexion.url.jasper";
	//public static final String	DDBB_NOMBRE_BD_JASPER = "nombre.bd.jasper";
	//public static final String DEFAULT_REPORTPORT = "8083";

	
	
	/**
	 * Mantis
	 */
	public static final String MANTIS_URL = "geopista.url.mantis";
    public static final String MANTIS_USERNAME = "geopista.user.mantis";
    public static final String MANTIS_PASSWORD = "geopista.password.mantis";    
    public static final String DEFAULT_MANTIS_URL = "http://pamod-app2.c.ovd.interhost.com:9090";
    public static final String DEFAULT_MANTIS_USERNAME = "cau";
    public static final String DEFAULT_MANTIS_PASSWORD = "caumodelo";

    
    
    /**
     * IU por defecto
     */
    public static final String DEFAULT_MENU_FONT = "menu.fuente";
    public static final String DEFAULT_MENU_SIZE = "menu.tamanno";
    public static final String DEFAULT_MENU_STYLE = "menu.estilo";
    
    public static final String DEFAULT_SCREEN_WEIGHT = "ancho.monitor";
    public static final String DEFAULT_FEATURE_SELECTED_COLOR = "color.feature.seleccion";
    
    public static final String DEFAULT_UNIT_EQUIVALENCE = "unidad.equivalence";
    public static final String DEFAULT_UNIT_NAME = "unidad.nombre";
    
    
    /**
     * Ultimo login
     */
    public static final String LOCALGIS_LOGIN_LAST_USERNAME = "LAST_LOGIN";
    public static final String LOCALGIS_LOGIN_LAST_PASSWORD = "LAST_PASS";
    
    
    /**
     * SSO
     */
	public static final String SSO_SESSION_ID = "sso.sessionid";
    
	
	
    
    public static final String PREFERENCES_LAST_IMAGE_FOLDER_KEY = "geopista.user.last_image_folder";
    
    public static final String PREFERENCES_DATA_PATH_KEY = "ruta.base.mapas";
    public static final String DEFAULT_DATA_PATH = "c:\\LocalGIS\\Datos\\";
    
    public static final String REPORT_DIR_NAME = "informes";
    public static final String PREFERENCES_AVAILABLE_LANGUAGES_KEY	= "geopista.AvailableLanguages";
    
    public static final String PREFERENCES_JRE15_HOME_KEY = "jre15.home";
    public static final String JRE15_DEFAULT_HOME = "c:\\Geopista\\jre15";
    
    public static final String PREFERENCES_APPICON_KEY = "geopista.user.appicon";
    public static final String DEFAULT_ENTITY_ID = "default.entityid";
    public static final String DEFAULT_UPDATE_SERVER_URL = "http://www.geopista.com/software";

    public static final String DDBB_SERVER_IP = "geopista.conexion.DDBBIp";
    
    public static final String LISTA_MNE= "geopista.user.mne";
    public static final String LISTA_WFS_G= "geopista.user.wfs-g";
    
    public static final String SESION_KEY = "sesion";
    
    public static final String MUNI_COMBO = "comboMunicipios";
    public static final String MAPAS_COMBO = "comboMapas";
    
    public static final String ENTIDADES = "listaEntidades";
    
    public static final String NUMERO_FICHEROS_LICENCIAS_XML="numero.ficheros.licencias.xml";
    
    public static final String SEL_MUNI_AUTO="SeleccionAutomaticaMunicipio";
    
    public static final String VERSION="version";
    
    public static final String IMPORTACIONES="importaciones";
    
    public static final String SRID_INICIAL="srid_inicial";
    
    public static final String PERMISOS="permisos";
    
    public static final String CONNECT_STATUS="connect_status";
    
    public static final String idAppType="idAppType";
    
    /**
     * keys para guardar en el registro la variables del plugin del buscador callejero
     */
    public static final String BUSCADOR_CALLEJERO_AVANZADO = "buscador.callejero.avanzado";
    public static final String BUSCADOR_CALLEJERO_LITERAL = "buscador.callejero.literal";
    public static final String BUSCADOR_CALLEJERO_NOMBREVIA = "buscador.callejero.nombrevia";
    public static final String BUSCADOR_CALLEJERO_NUMERO = "buscador.callejero.numero";
    public static final String BUSCADOR_CALLEJERO_CAPA_NOMBREVIA = "buscador.callejero.capa.nombrevia";
    public static final String BUSCADOR_CALLEJERO_CAPA_NUMERO = "buscador.callejero.capa.numero";
    public static final String BUSCADOR_CALLEJERO_ATRIBUTO_NOMBREVIA = "buscador.callejero.atributo.nombrevia";
    public static final String BUSCADOR_CALLEJERO_ATRIBUTO_NUMERO = "buscador.callejero.atributo.numero";
    public static final String BUSCADOR_CALLEJERO_RELACION_NOMBREVIA = "buscador.callejero.relacion.nombrevia";
    public static final String BUSCADOR_CALLEJERO_RELACION_NUMERO = "buscador.callejero.relacion.numero";


	public static final String TRUST_CERT_STORE_PATH = "geopista.security.truststore_path";
	
	
	//Gestor Fip    
	public static final String URL_CONSOLEUER_WS = "consoleuerws.url";
    public static final String URL_IGN_WS = "conversorign.url";
    
    
	
}
