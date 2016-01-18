/**
 * GenericParameterEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.parameters.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;

import com.geopista.app.reports.wizard.panels.EditorGenericoWizardPanel;

public class GenericParameterEditor implements ReportParametersEditor {
	
	private List parameterList;
	private HashMap parameterValueMap;
	
	public GenericParameterEditor(List parameterList){
		if (parameterList != null){
			this.parameterList = parameterList;
		}
		else {
			this.parameterList = new ArrayList();
		}
		parameterValueMap = new HashMap();
	}
	
	public Map getFilledParameters(){
		return parameterValueMap;
	}
	
	public Object getFilledParameterValue(JRParameter parameter) {
		return parameterValueMap.get(parameter.getName());
	}
	
	public List getParameterEditionPanels() {
		ArrayList editionPanelList = new ArrayList();
		
		if (parameterList.size() == 0){
			return editionPanelList;
		}
		
		EditorGenericoWizardPanel editionPanel = new EditorGenericoWizardPanel();
		List remainingParameters = editionPanel.loadParameters(parameterList);
		editionPanelList.add(editionPanel);		
		while (remainingParameters.size() > 0){
			editionPanel = new EditorGenericoWizardPanel();			
			editionPanelList.add(editionPanel);	
			remainingParameters = editionPanel.loadParameters(remainingParameters);
		}
		
		return editionPanelList;
	}
	
	public void fillParameterValue(JRParameter parameter, Object value){
		parameterValueMap.put(parameter.getName(), value);
	}
}
