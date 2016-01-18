package com.geopista.app.reports.parameters.filter;

import net.sf.jasperreports.engine.JRParameter;

public class SystemDefinedParameterFilter implements JasperParameterFilter {

	public boolean matches(JRParameter parameter) {
		if (parameter.isSystemDefined()){
			return true;
		}
		return false;
	}

}
