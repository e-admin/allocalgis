package com.geopista.app.reports.parameters.filter;

import net.sf.jasperreports.engine.JRParameter;

public class PredefinedParameterFilter implements JasperParameterFilter {

	public boolean matches(JRParameter parameter) {
		if (parameter.isSystemDefined()){
			return false;
		}
	
		String parameterName = parameter.getName();		
		if (!parameterName.equals("ID_ENTIDAD")
				&& !parameterName.equals("SUBREPORT_DIR")
				&& !parameterName.equals("locale")
				&& !parameterName.equals("IMPRIMIR_IMAGENES")
				&& !parameterName.equals("id_municipio")
				&& !parameterName.equals("MAP_IMAGE_FACTORY")){
			return false;
		}
		
		return true;
	}
	
	
}
