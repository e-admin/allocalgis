/**
 * 
 */
package com.geopista.app.reports.elements.filter;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRImage;

import com.geopista.app.reports.maps.MapImageExpressionManager;

public class MapImageElementFilter implements JasperElementFilter {

	public boolean matches(JRElement element) {
		if (element instanceof JRImage){
			JRImage image = (JRImage) element;
			if (MapImageExpressionManager.isMapImageExpression(image.getExpression().getText())){
				return true;
			}
		}
		return false;
	}
	
}