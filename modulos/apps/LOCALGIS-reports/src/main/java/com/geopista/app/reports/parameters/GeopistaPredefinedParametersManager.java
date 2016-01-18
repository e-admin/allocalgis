/**
 * GeopistaPredefinedParametersManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.parameters;

import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.engine.JRParameter;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.reports.ReportsContext;

public class GeopistaPredefinedParametersManager {
	
	private AppContext geopistaContext = 
		(AppContext) AppContext.getApplicationContext();  
	
	private HashSet predefinedParameters;
	
	private static GeopistaPredefinedParametersManager instance = null;
	
	protected GeopistaPredefinedParametersManager() {
		predefinedParameters = new HashSet();
		
		predefinedParameters.add("idMunicipio");
		predefinedParameters.add("nombreMunicipio");
		predefinedParameters.add("SUBREPORT_DIR");
	}
	
	public static GeopistaPredefinedParametersManager getInstance(){
		if (instance != null){
			return instance;
		}
		
		instance = new GeopistaPredefinedParametersManager();
		
		return instance;
	}
	
	public Set getGeopistaPredefinedParametersList(){
		return predefinedParameters;
	}
	
	public Object fillGeopistaPredefinedParameter(JRParameter reportParameter){
		String parameterName = reportParameter.getName();
		
		if (parameterName.equalsIgnoreCase("idMunicipio")){
			return fillIdMunicipio();
		}
		else if (parameterName.equalsIgnoreCase("nombreMunicipio")){
			return fillNombreMunicipio();
		}
		else if (parameterName.equalsIgnoreCase("SUBREPORT_DIR")){
			return fillSubreportDir();
		}
		else {
			return null;
		}
	}
	
	private Integer fillIdMunicipio(){
		return new Integer(geopistaContext.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
	}
	
	private String fillNombreMunicipio(){
		return "TODO: MUNICIPIO";
	}
	
	private String fillSubreportDir(){
		return ReportsContext.getInstance().getBaseCompiledReportsPath();
	}

	public boolean isGeopistaPredefinedParameter(JRParameter parameter) {
		return predefinedParameters.contains(parameter.getName());
	}
}
