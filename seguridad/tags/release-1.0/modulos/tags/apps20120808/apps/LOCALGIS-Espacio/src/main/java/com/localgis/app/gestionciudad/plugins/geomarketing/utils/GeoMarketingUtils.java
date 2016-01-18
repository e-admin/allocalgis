package com.localgis.app.gestionciudad.plugins.geomarketing.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import com.vividsolutions.jump.I18N;

public class GeoMarketingUtils {
	
	
	//public static final String GEOMARKETING_OPERATIONS = "Operaciones de GeoMarketing";
	
	@SuppressWarnings("unchecked")
	public static void inicializarIdiomaGeoMarketing(){
		if (I18N.plugInsResourceBundle.get("geomarketing") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.localgis.app.gestionciudad.plugins.geomarketing.language.LocalGISGeoMarketingPlugInI18n",loc,GeoMarketingUtils.class.getClassLoader());
			I18N.plugInsResourceBundle.put("geomarketing",bundle);
		}
	}

}
