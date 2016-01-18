package com.geopista.app.reports.elements.filter;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;

import com.geopista.app.reports.maps.MapImageExpressionManager;
import com.geopista.app.reports.maps.MapImageSettings;

public class MapImageReferencingParameterFilter implements JasperElementFilter {

	private JRParameter parameter;
	
	public MapImageReferencingParameterFilter(JRParameter parameter){
		this.parameter = parameter;
	}
	
	public boolean matches(JRElement element) {
		if (element instanceof JRImage){
			JRImage image = (JRImage) element;
			if (MapImageExpressionManager.isMapImageExpression(image.getExpression().getText())){
				MapImageSettings mapImageSettings =
					MapImageExpressionManager.parseExpression(image.getExpression().getText());
				
				if (mapImageSettings != null &&
						mapImageSettings.getMapSelectionIdName().equals(parameter.getName())){
					return true;
				}
			}
		}
		return false;
	}
}
