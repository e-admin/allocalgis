/**
 * UtilsI18N.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.module.utilitys;

import java.util.Locale;
import java.util.ResourceBundle;

public class UtilsI18N {

	private static String DEFAULT_APLICATION_RESOURCES = "AplicationResources";
	private static String DEFAULT_LOCALE = "es";
	
	
	/**
	 * SOBRECARGA: Obtencion de literales mediante configuracion especifica o valores por defecto
	 * @param keyMensaje
	 * @return
	 */
	public static String getMessageResource (String keyMensaje) {
		return getMessageResource(keyMensaje, null);
	}
	public static String getMessageResource (String keyMensaje, String locale) {
		return getMessageResource(null, keyMensaje, locale);
	}
	public static String getMessageResource (String nameResource, String keyMensaje, String locale) {
		String texto = null;
		ResourceBundle resource = null;
		try {
			resource = getResourceBundle (nameResource, locale);
			texto = resource.getString(keyMensaje);
		} catch (Exception e) {
			//Retornar key enviado NO lanzamos excepcion
			texto = keyMensaje;
			//throw new RuntimeException(e);
		}
		
		return texto;
	}
	
	/**
	 * Obtiene fichero resources
	 * @param locale
	 */
	private static ResourceBundle getResourceBundle (String nameResource, String locale) {
		ResourceBundle literales = null;
		try {
			if (locale == null)
				locale = DEFAULT_LOCALE;
			if (nameResource == null || nameResource.equals(""))
				nameResource = DEFAULT_APLICATION_RESOURCES;
			literales = ResourceBundle.getBundle(DEFAULT_APLICATION_RESOURCES, getLocale(locale));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return literales;
	}
	
	
	
	/**
	 * Obtiene locale segun cadena indicada. Formato tratado: lenguaje_pais_variante
	 * @param cadenaLocale
	 * @return
	 */
	private static Locale getLocale (String cadenaLocale) {
		Locale locale = null;
		String[] partesLocale = (cadenaLocale != null)? cadenaLocale.split("_") : null;
		if (partesLocale != null) {
			String lenguaje = (partesLocale.length > 0)? partesLocale[0] : "";
			String pais 	= (partesLocale.length > 1)? partesLocale[1] : "";
			String variant 	= (partesLocale.length > 2)? partesLocale[2] : "";
			locale = new Locale (lenguaje, pais, variant);
		}
		return locale;
	}
}
