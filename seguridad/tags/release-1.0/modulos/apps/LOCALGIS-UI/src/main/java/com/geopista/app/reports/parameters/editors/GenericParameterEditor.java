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
