/**
 * DocumentsPanelUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.dialogs.documents.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

public class DocumentsPanelUtils {

	@SuppressWarnings("unchecked")
	public static void initializeDocumentsPanelLanguages(){
		if (I18N.plugInsResourceBundle.get("documentspanel") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.localgis.app.gestionciudad.dialogs.documents.language.GestionCiudad_DocumentsPaneli18n",loc,UtilidadesAvisosPanels.class.getClassLoader());
			I18N.plugInsResourceBundle.put("documentspanel",bundle);
		}
	}	
	
	
	public String getI18NDocumentsPanel(String languageLabel){
		try{
			return I18N.get("documentspanel",languageLabel);
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR I18N documents";
		}	
	}
}
