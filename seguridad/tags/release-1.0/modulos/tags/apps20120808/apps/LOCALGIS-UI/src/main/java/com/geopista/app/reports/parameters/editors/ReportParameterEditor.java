package com.geopista.app.reports.parameters.editors;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRParameter;

import com.geopista.ui.wizard.ReportParameterEditorWizardPanel;

public interface ReportParameterEditor { 

	public ReportParameterEditorWizardPanel newParameterEditorPanel(ArrayList reportParameters);
	
	public boolean matchesParameter(JRParameter parameterName);
	
	public ArrayList getListOfRelatedMatchedParameters(String parameterName);	
}
