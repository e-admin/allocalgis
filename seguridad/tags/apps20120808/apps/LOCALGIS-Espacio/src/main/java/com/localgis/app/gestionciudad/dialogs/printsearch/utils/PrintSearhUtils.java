package com.localgis.app.gestionciudad.dialogs.printsearch.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

public class PrintSearhUtils {

	@SuppressWarnings("unchecked")
	public static void inicializarIdiomaPrintSearchPanels(){
		if (I18N.plugInsResourceBundle.get("printinterventionspanel") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.localgis.app.gestionciudad.dialogs.printsearch.language.GestionCiudad_PrintSearchPaneli18n",loc,UtilidadesAvisosPanels.class.getClassLoader());
			I18N.plugInsResourceBundle.put("printinterventionspanel",bundle);
		}
	}	
		
}
