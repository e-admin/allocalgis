package com.geopista.ui.wizard;

import com.geopista.app.reports.wizard.panels.ReportWizardPanel;


public abstract class ReportParameterEditorWizardPanel extends ReportWizardPanel{

	protected String parametersSuffix;
	 
	public void setParametersSuffix(String parametersSuffix){
		this.parametersSuffix = parametersSuffix;
	}
	
	public String getParametersSuffix(){
		if (parametersSuffix != null){
			return parametersSuffix;
		}
		
		return "";
	}
}
