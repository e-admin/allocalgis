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
