/**
 * 
 */
package com.geopista.app.reports.elements.filter;


import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRImage;

public class ImageKeyElementFilter implements JasperElementFilter {
	
	String imageKey;
	
	public ImageKeyElementFilter(String imageKey){
		this.imageKey = imageKey;
	}

	public boolean matches(JRElement element) {
		if (element instanceof JRImage && element.getKey().equals(imageKey)){
			return true;
		}
		return false;
	}		
}