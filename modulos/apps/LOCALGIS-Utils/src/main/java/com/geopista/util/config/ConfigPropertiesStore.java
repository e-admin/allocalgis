/**
 * ConfigPropertiesStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.config;

import java.util.ResourceBundle;

import com.geopista.consts.config.ConfigConstants;
import com.geopista.model.config.AbstractPropertiesStore;

public class ConfigPropertiesStore extends AbstractPropertiesStore{
		
		/**
		 * Logger
		 */
		protected static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ConfigPropertiesStore.class);
	    	
		/**
		 * Instancia
		 */
		private static ConfigPropertiesStore instance = new ConfigPropertiesStore();
		
		/**
		 * Constructor protegido
		 */
		protected ConfigPropertiesStore() {		
			config = initializeConfig();
		}
		
		/**
		 * Getter de la instancia única
		 */
		public static ConfigPropertiesStore getConfigPropertiesStore() {
			return instance;
		}
			
		/**
		 * Devuelve un objeto de recursos con el contenido de un properties (localgis)
		 * @return ResourceBundle: Objeto de recursos con el contenido del properties
		 */
		private static ResourceBundle initializeConfig() {
			return initializeConfig(ConfigConstants.PROPERTIES_NAME);
		}

	}

