package com.geopista.util.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.geopista.app.LCGIII_ContextConstants;

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
	
	/**
	 * Constructor protegido
	 */
	protected UserPreferenceStore(){		
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
     * @return
     */
    public String getUserPreference(String key, String defaultValue, boolean tryToCreate)
    {
    	Preferences pref = Preferences.userRoot().node(LCGIII_ContextConstants.GEOPISTA_PACKAGE);
        String value = pref.get(key, null);
        if (value == null && tryToCreate) 
        {
            try
            {                    
                if (config!=null)
                {   
                    try
                    {
                        value = config.getString(key);
                    }
                    catch(MissingResourceException e)
                    {
                        value = null;
                    }                    
                }                
                if (value == null)
                    value = config.getString(key);
                if (value != null) 
                    setUserPreference(key, value);
            } catch (MissingResourceException e)
            {
                setUserPreference(key,defaultValue);
				logger.error("AbstractUserPreferenceStore : getUserPreference: " + e);
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
    public void setUserPreference(String key, String value)
    {
        Preferences pref = Preferences.userRoot().node(LCGIII_ContextConstants.GEOPISTA_PACKAGE);
        if(value==null) 
        	pref.remove(key);
		else{
			try {
				if(pref.nodeExists(key)){
					pref.remove(key);
				}
			} catch (BackingStoreException e) {
				logger.error("AbstractUserPreferenceStore : setUserPreference: " + e);
			}
        	pref.put(key, value);
		}
    }
    
}
