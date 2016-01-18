/**
 * GestorPlaneamientoUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import com.vividsolutions.jump.I18N;

public class GestorPlaneamientoUtils {

	@SuppressWarnings("unchecked")
	public static void inicializarIdiomaGestorPlaneamientoPanels(){
		if (I18N.plugInsResourceBundle.get("LocalGISGestorFipAsociacionDeterminacionesEntidades") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.language.GestorFip_GestorPlaneamientoPaneli18n",loc,GestorPlaneamientoUtils.class.getClassLoader());
			I18N.plugInsResourceBundle.put("LocalGISGestorFipAsociacionDeterminacionesEntidades",bundle);
		}
	}	
		
}
