package com.geopista.app.reports.parameters.filter;

import net.sf.jasperreports.engine.JRParameter;

public interface JasperParameterFilter {
	
	public boolean matches(JRParameter parameter);
}
