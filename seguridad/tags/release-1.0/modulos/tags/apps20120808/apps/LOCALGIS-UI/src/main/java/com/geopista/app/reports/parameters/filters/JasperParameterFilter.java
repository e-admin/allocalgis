package com.geopista.app.reports.parameters.filters;

import net.sf.jasperreports.engine.JRParameter;

public interface JasperParameterFilter {
	
	public boolean matches(JRParameter parameter);
}
