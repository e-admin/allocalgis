/**
 * PredefinedParameterFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
