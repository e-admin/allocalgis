/**
 * UserPreferenceStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.geopista.app.UserPreferenceConstants;

public class UserPreferenceStore {

	/**
	 * Logger
	 */
	protected static org.apache.log4j.Logger logger;
	
	/**
	 * Variables
	 */
	private static final UserPreferenceStore instance = new UserPreferenceStore();
	protected static ResourceBundle config;
	protected static HashMap<String, String> substitutionMap;
	
	/**
	 * Constructor protegido
	 */
	protected UserPreferenceStore(){
		config = initializeProperties("GeoPista");
		
		substitutionMap = new HashMap<String, String>();
		substitutionMap.put("%HOST_IP%", UserPreferenceConstants.LOCALGIS_SERVER_HOST);
		substitutionMap.put("%RUTA_BASE%", UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY);
		substitutionMap.put("%DDBB_SERVER_IP%",UserPreferenceConstants.DDBB_SERVER_IP);
		substitutionMap.put("%URL_TOMCAT%", UserPreferenceConstants.TOMCAT_URL);
		substitutionMap.put("%HOST_ADMCAR%", UserPreferenceConstants.LOCALGIS_SERVER_URL);
	}
		
    /**
     * Obtiene el recurso de configuración por defecto del sistema
     * @param string
     * @return ResourceBundle
     */
    private ResourceBundle initializeProperties(String string)
    {
        return ResourceBundle.getBundle(string);
    }
    
	/**
	 * Recupera el almacén de configuraciones en el registro del sistema
	 * @return UserPreferenceStore: El almacén de configuraciones en el registro del sistema
	 */
	public static UserPreferenceStore getUserPreferenceStore(){
		return instance;
	}	
	
	/**
     * Obtiene la preferencia del usuario. Si no se ha fijado antes se extrae
     * de la configuración de sistema y se fija para futuras consultas.
     * 
     * @param key
     * @param defaultValue
     * @param tryToCreate indica si hay que copiar la propiedad de configuraciones generales
     * @return String
     */
    public static String getUserPreference(String key, String defaultValue, boolean tryToCreate)
    {
    	//if (key.equals("geopista.DefaultCityId"))
		//	return String.valueOf(AppContext.getIdMunicipio());
    	
    	Preferences pref = Preferences.userRoot().node(UserPreferenceConstants.LOCALGIS_SYSTEM_RESGISTRY_PATH);
        String value = pref.get(key, null);
       
        if (value == null && tryToCreate) 
        {
            try
            {                    
                if (config!=null) {   
                    try {
                        value = config.getString(key);
                    }
                    catch(MissingResourceException e) {
                        value = defaultValue;
                    }                    
                }
                if (value != null) 
                	setUserPreference(key, value);
            } catch (MissingResourceException e) {
                setUserPreference(key,defaultValue);
				logger.error("AbstractUserPreferenceStore : getUserPreference: ",e);
                return defaultValue;
            }
        }
        
        
        return value == null ? defaultValue : value;
    }
    
    
    
    /**
     * Fija una preferencia local de este usuario
     * Usa un registro local para configuraciones
     * @param key
     * @param value
     */    
    public static void setUserPreference(String key, String value)
    {
        Preferences pref = Preferences.userRoot().node(UserPreferenceConstants.LOCALGIS_SYSTEM_RESGISTRY_PATH);
        if(value==null) 
        	pref.remove(key);
		else{
			try {
				if(pref.nodeExists(key)){
					pref.remove(key);
				}
			} catch (BackingStoreException e) {
				logger.error("AbstractUserPreferenceStore : setUserPreference: ",e);
			}
        	pref.put(key, value);
		}
    }
    
    //----------------------------
    //-Comentar en AppContext y sustituir llamadas AppContext.getApplicationContext() y equivalentes por UserPreferenceStore
    
    /**
     * Version de un solo parámetro que devuelve null por defecto.
     * Obtiene la propiedad de configuracion para la aplicacion actual
     * Busca primero en las preferencias del usuario local.
     * Despues busca en la configuración general.
     * Realiza la sustitución de patrones fijados:
     *
     * @param host_ip_key2
     * @param string
     * @return String
     */
    public String getString(String paramId)
    {
        return getString(paramId, null);
    }
    
    /**
     * Realiza la sustitución de variables concretas:
     * - %HOST_IP% por el valor de geopista.conexion.serverIP
     * - %RUTA_BASE% por el valor de ruta.base.mapas
     * -etc
     * 
     * @param value expresion en la que hacer la sustitucion
     * @return expresion con las sustituciones
     */
    public String substitutions(String value, HashMap<String, String> sMap)
    {
        if (value==null) return null;
        Iterator<Entry<String, String>> subst=sMap.entrySet().iterator();
        while (subst.hasNext()) // Make all substitutions
        {
           try{

        	   Entry<String, String> element = subst.next();
               String newValue=UserPreferenceStore.getUserPreference((String) element.getValue(),
                    config.getString((String) element.getValue()),
                    false);
                if (value!=null)
                    value=value.replaceAll((String) element.getKey(), newValue);
            }catch(Exception e)
            {
                logger.error("Error al hacer la subsitucion:",e);
            }
        }

        return value;
    }
    
    public String substitutions(String value)
    {
        return substitutions(value, substitutionMap);
    }
        
    private String subtitutionsPath(String value)
    {        
        File finalPath = null;
        if (value==null) return null;        
        
        String basePathValue=UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
                config.getString(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY),
                false);
        
        value=value.replaceAll("%RUTA_BASE%", "");
        finalPath = new File(basePathValue,value);	        
        
        return finalPath!=null?finalPath.getAbsolutePath():null;
    }
    
    /**
     * Obtiene la propiedad de configuracion para la aplicacion actual
     * Busca primero en las preferencias del usuario local.
     * Despues busca en la configuración general.
     * Realiza la sustitución de patrones fijados:
     *
     * @param host_ip_key2
     * @param string
     * @return String
     */
    public String getString(String paramId, String defaultString)
    {
        String value = null;
        try
        {
            paramId = paramId.trim();// limpia la clave
            value = UserPreferenceStore.getUserPreference(paramId, null,false);
//            if (rBCustomConfiguration!=null && value == null)
//            {   
//                try
//                {
//                    value = rBCustomConfiguration.getString(paramId);
//                }
//                catch(MissingResourceException mre)
//                {
//                    value = null;
//                }
//                
//            }
            if (value == null && config != null)
                value = config.getString(paramId);
            if (value == null)
                value = defaultString;
            return substitutions(value);
        } catch (RuntimeException e)
        {
            // Si hay algún problema notifica en consola y deja el término sin traducir
            if (logger.isDebugEnabled())
            {
                
                logger.debug("getString(String) - TODO: Configurar: " + paramId);
            }
            return defaultString;
        }
    }
    
    /**
     * Obtiene direcciones o URLs haciendo sustituciones de comodines
     */
    public String getPath(String paramId)
    {
        return getPath(paramId, null);
    }
    
    public String getPath(String paramId, String defaultString)
    {
        String value = null;
        try
        {
            paramId = paramId.trim();// limpia la clave
            value = UserPreferenceStore.getUserPreference(paramId, null,false);
//            if (rBCustomConfiguration!=null && value == null)
//                value = rBCustomConfiguration.getString(paramId);
            if (value == null && config != null)
                value = config.getString(paramId);
            if (value == null)
                value = defaultString;
            return subtitutionsPath(value);
        } catch (RuntimeException e)
        {
            // Si hay algún problema notifica en consola y deja el término sin traducir
            if (logger.isDebugEnabled())
            {
                
                logger.debug("getString(String) - TODO: Configurar: " + paramId);
            }
            return defaultString;
        }
    }
    
}
