package com.geopista.app.reports.parameters.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRReport;

import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.elements.filter.InteractiveMapImageReferencingParameterFilter;
import com.geopista.app.reports.maps.MapImageConstants;
import com.geopista.app.reports.maps.MapImageExpressionManager;
import com.geopista.app.reports.maps.MapImageSettings;
import com.geopista.app.reports.wizard.panels.SeleccionEntidadReportWizardPanel;
import com.geopista.app.reports.wizard.panels.SeleccionEscalaReportWizardPanel;

public class MapParameterEditor implements ReportParametersEditor {
	
	private JRReport report;
	private JRParameter parameter;
	private List editionPanelsList;
	private List mapImagesList;
	private String parameterValue;
		
	public MapParameterEditor(JRReport report, JRParameter parameter){
		this.report = report;
		this.parameter = parameter;
		
		editionPanelsList = new ArrayList();
		SeleccionEntidadReportWizardPanel seleccionEntidadPanel = 
			new SeleccionEntidadReportWizardPanel(this);
		editionPanelsList.add(seleccionEntidadPanel);
		
		/*
         * Buscamos los elementos map que usan el parametro con escala 
         * manual para añadir un panel que se encargue de preguntar por 
         * dicha escala
         */
		mapImagesList =
			ReportsManager.getInstance().findInteractiveMapImagesReferencingParameter(report, parameter, MapImageConstants.SCALE_TYPE_INTERACTIVE);
		
		for (int i  = 0; i < mapImagesList.size(); i++){
			JRImage image = (JRImage) mapImagesList.get(i);
			SeleccionEscalaReportWizardPanel seleccionEscalaPanel =
				new SeleccionEscalaReportWizardPanel(this, image);
			editionPanelsList.add(seleccionEscalaPanel);
		}
		
		parameterValue = null;
	}
	
	public List getParameterEditionPanels() {
		return editionPanelsList;
	}

	public void fillParameter(String value){
		this.parameterValue = value;
	}
	
	public String getFilledParameterValue() {
		return parameterValue;
	}
	
	public JRParameter getParameter(){
		return parameter;
	}

	public Map getFilledParameters() {
		HashMap parameterValueMap = new HashMap();
		parameterValueMap.put(parameter.getName(), parameterValue);
		
		return parameterValueMap;
	}
}
