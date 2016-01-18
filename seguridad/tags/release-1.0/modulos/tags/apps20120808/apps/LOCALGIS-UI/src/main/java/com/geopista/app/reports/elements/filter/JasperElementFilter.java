package com.geopista.app.reports.elements.filter;

import net.sf.jasperreports.engine.JRElement;

public interface JasperElementFilter {
	
	public boolean matches(JRElement element);
	
}
