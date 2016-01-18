/**
 * ReportParametersManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.parameters.editors.GenericParameterEditor;
import com.geopista.app.reports.parameters.editors.MapParameterEditor;
import com.geopista.app.reports.parameters.editors.PredefinedParametersEditor;
import com.geopista.app.reports.parameters.filter.JasperParameterFilter;
import com.geopista.app.reports.parameters.filter.MapImageParameterFilter;
import com.geopista.app.reports.parameters.filter.PredefinedParameterFilter;
import com.geopista.app.reports.parameters.filter.SystemDefinedParameterFilter;

public class ReportParametersManager {
	
	private static ReportParametersManager instance = null;
	
	public static synchronized ReportParametersManager getInstance(){
		if (instance != null){
			return instance;
		}
		
		instance = new ReportParametersManager();		
		return instance;
	}
	
	private ReportParametersManager(){
		
	}
	
	public List getParametersEditors(JasperReport report){		
		JRParameter[] parameters = report.getParameters();
		List  parametersList = new ArrayList(Arrays.asList(parameters));
		
		List parameterEditors = new ArrayList(); 
		
		SystemDefinedParameterFilter systemDefinedParameterFilter = new SystemDefinedParameterFilter();
		List systemDefinedParametersList = findParameters(parametersList, systemDefinedParameterFilter);
		parametersList.removeAll(systemDefinedParametersList);
		
		PredefinedParameterFilter predefinedParameterFilter = new PredefinedParameterFilter();
		List predefinedParametersList = findParameters(parametersList, predefinedParameterFilter);
		parametersList.removeAll(predefinedParametersList);
		
		MapImageParameterFilter mapImageParameterFilter = new MapImageParameterFilter();
		List mapImageParametersList = findParameters(parametersList, mapImageParameterFilter);		
		for (int i = 0; i < mapImageParametersList.size(); i++){
			JRParameter parameter = (JRParameter) mapImageParametersList.get(i);
			MapParameterEditor mapParameterEditor = new MapParameterEditor(report, parameter);
			/*
             * Comprobamos si el parametro se esta usando realmente para evitar
             * preguntar por parametros que no se usan
             */
			List listMapImages = ReportsManager.getInstance().findInteractiveMapImagesReferencingParameter(report, parameter, null);
			if (listMapImages != null && listMapImages.size() > 0) {
			    parameterEditors.add(mapParameterEditor);
			}
		}		
		parametersList.removeAll(mapImageParametersList);
		
		GenericParameterEditor genericParameterEditor = new GenericParameterEditor(parametersList);
		parameterEditors.add(genericParameterEditor);
		
		PredefinedParametersEditor predefinedParametersEditor = new PredefinedParametersEditor();
		parameterEditors.add(predefinedParametersEditor);
		
		return parameterEditors;
	}
	
	private List findParameters(List parametersList,
			JasperParameterFilter parameterFilter){		
		List filteredParametersList = new ArrayList();
		
		for (int i = 0; i < parametersList.size(); i++){
			JRParameter parameter = (JRParameter) parametersList.get(i);
			if (parameterFilter.matches(parameter)){
				filteredParametersList.add(parameter);
			}
		}
		
		return filteredParametersList;
	}
}
