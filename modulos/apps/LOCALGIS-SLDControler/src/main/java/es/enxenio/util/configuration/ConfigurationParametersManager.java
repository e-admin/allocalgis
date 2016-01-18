/**
 * ConfigurationParametersManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.enxenio.util.configuration;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;

import es.enxenio.util.exceptions.MissingConfigurationParameterException;

public final class ConfigurationParametersManager {

	private static final String JNDI_PREFIX = "java:comp/env/";

    private static final String CONFIGURATION_FILE =
        "configuration.properties";    

	private static boolean usesJNDI;
    private static Map parameters;
	
	static {

		try {
		
			/* Read property file (if exists).*/	
			Class configurationParametersManagerClass = 
            	ConfigurationParametersManager.class;
        	ClassLoader classLoader =
            	configurationParametersManagerClass.getClassLoader();
        	InputStream inputStream =
            	classLoader.getResourceAsStream(CONFIGURATION_FILE);
        	Properties properties = new Properties();
        	properties.load(inputStream);
        	inputStream.close();
			
			/* We have been able to read the file. */
			usesJNDI = false;
			System.out.println("*** Using '" + CONFIGURATION_FILE +
				"' file for configuration ***");

			/* 
			 * We use a "HashMap" instead of a "HashTable" because HashMap's
			 * methods are *not* synchronized (so they are faster), and the 
			 * parameters are only read.
			 */
			parameters = new HashMap(properties);
			
		} catch (Exception e) { 

			/* We assume configuration with JNDI. */
			usesJNDI = true;
			//System.out.println("*** Using JNDI for configuration ***");
			
			/* 		
			 * We use a synchronized map because it will be filled by using a 
			 * lazy strategy.
			 */			 
			parameters = Collections.synchronizedMap(new HashMap());
			
		}
		
	}
   	
	private ConfigurationParametersManager() {}
	
    public static String getParameter(String name) 
		throws MissingConfigurationParameterException {
	
		String value = (String) parameters.get(name);

		if (value == null) {
		
			if (usesJNDI) {

				try {
					InitialContext initialContext = new InitialContext();

            		value = (String) initialContext.lookup(
						JNDI_PREFIX + name);
					parameters.put(name, value);
				} catch (Exception e) {
					throw new MissingConfigurationParameterException(name);
				}

			} else {

	        	throw new MissingConfigurationParameterException(name);

			}

		}
		
		return value;
								
    }	
    
    public static void addConfigurationFile(String newConfigurationFile) {
    
    	try {
			Class configurationParametersManagerClass = 
				ConfigurationParametersManager.class;
			ClassLoader classLoader =
				configurationParametersManagerClass.getClassLoader();
			InputStream inputStream =
				classLoader.getResourceAsStream(newConfigurationFile);
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
			HashMap newParameters = new HashMap(properties);
			parameters.putAll(newParameters);
    	} catch (Exception e) {
    		e.printStackTrace();  		 
    	}
    }
}
