package com.geopista.app.reports.parameters.filters;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;

import com.geopista.plugin.Constantes;

public class MapImageParameterFilter implements JasperParameterFilter {

	public boolean matches(JRParameter parameter) {
		if(!parameter.isSystemDefined() &&
				parameter.getValueClass().equals(String.class)){
			JRPropertiesMap properties = parameter.getPropertiesMap();
			String isMapValue = properties.getProperty(Constantes.IS_MAP_PROPERTY); 
			if (isMapValue != null &&  !isMapValue.equals("")){
				return true;
			}
		}
		return false;
	}

}
