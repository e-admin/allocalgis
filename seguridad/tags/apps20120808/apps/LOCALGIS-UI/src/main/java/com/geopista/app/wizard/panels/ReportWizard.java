package com.geopista.app.wizard.panels;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.reports.exceptions.ReportProcessingException;
import com.geopista.app.reports.maps.MapImageFactory;
import com.geopista.app.reports.parameters.ReportParametersManager;
import com.geopista.app.reports.parameters.editors.ReportParametersEditor;
import com.geopista.app.reports.wizard.panels.ReportWizardPanel;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardComponent;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;

public abstract class ReportWizard extends WizardComponent {
	
	protected JasperReport report;
	
	protected JRParameter[] reportParameters;
	
	protected List parametersEditorsList;
	
	ApplicationContext appContext = AppContext.getApplicationContext();
	
	protected Map filledParametersMap;
	 
	protected boolean allPanelsLoaded; 
	
	protected boolean acceso;
	
	public ReportWizard(ApplicationContext appContext, String title){
		super(appContext, title, null);
		filledParametersMap = new HashMap();
		allPanelsLoaded = false;
		
		acceso = false;
		checkNeededPermissions();
	}
	
	protected abstract JasperReport getPredefinedReport() throws ReportNotFoundException;
	
	protected abstract ArrayList getPreSelectionPanels();
	
	protected abstract ArrayList getPostSelectionPanels();
	
	public JasperReport getReport(){
		return report;
	}
	
	public void setReport(JasperReport report){
		this.report = report;
		MapImageFactory.init(report);
		
		ReportParametersManager parametersManager = ReportParametersManager.getInstance();
		
		ArrayList panelsList = new ArrayList();
		parametersEditorsList = parametersManager.getParametersEditors(report);
		
		for (int i = 0; i < parametersEditorsList.size(); i++){
			ReportParametersEditor reportParametersEditor =
				(ReportParametersEditor) parametersEditorsList.get(i);
			
			List parameterEditionPanelList =
				reportParametersEditor.getParameterEditionPanels();
			
			panelsList.addAll(parameterEditionPanelList);
		}
		
		allPanelsLoaded = true;
		
		addPanels(panelsList);
	}
	
	public void init() throws ReportNotFoundException{
		if (getPreSelectionPanels() != null){
			addPanels(getPreSelectionPanels());
		}
		
		// Comprobamos si el wizard tiene un informe predefinido.
		JasperReport predefinedReport = getPredefinedReport();
		if (predefinedReport != null){
			setReport(predefinedReport);
		}
	}
	
	protected void linkPanels(List panelsList){
		for (int i = 0; i < panelsList.size(); i++){
			ReportWizardPanel panel = (ReportWizardPanel) panelsList.get(i);
			
			panel.setID(String.valueOf(i));
			
			if (i < panelsList.size() - 1){
				panel.setNextID(String.valueOf(i+1));
			}
			else if (!allPanelsLoaded){				
				panel.setNextID("panelsPending");
			}
			else {
				panel.setNextID(null);
			}
		}
	}
	
	public void addPanels(List panelsList) {
		if (panelsList == null || panelsList.size() == 0){
			if (allPanelsLoaded){
				ReportWizardPanel wizardPanel = (ReportWizardPanel) 
				allWizardPanels.get(allWizardPanels.size() - 1);

				wizardPanel.setNextID(null);
			}
			
			return;
		}
		
		if (allWizardPanels == null || allWizardPanels.size() == 0){
			linkPanels(panelsList);
			
			ReportWizardPanel[] reportWizardPanels =
				new ReportWizardPanel[panelsList.size()]; 
			panelsList.toArray(reportWizardPanels);
			
			init(reportWizardPanels);
			
			for (int i = 0; i < panelsList.size(); i++) {	
				ReportWizardPanel wizardPanel = (ReportWizardPanel) panelsList.get(i);
				wizardPanel.setReportWizard(this);	
			}
		}
		else {
			List totalPanels = new ArrayList();

			for(int i = 0; i < allWizardPanels.size(); i++){
				totalPanels.add(allWizardPanels.get(i));
			}
			
			for (int i = 0; i < panelsList.size(); i++){
				totalPanels.add(panelsList.get(i));
			}
			
			allWizardPanels = new ArrayList(totalPanels);
			
			linkPanels(allWizardPanels);
			checkIDs(allWizardPanels);
			
			for (int i = 0; i < panelsList.size(); i++) {	
				ReportWizardPanel wizardPanel = (ReportWizardPanel) panelsList.get(i);
				centerPanel.add(wizardPanel, wizardPanel.getID());
				wizardPanel.setReportWizard(this);	
			}
		}
	}

	public void fillParameter(String parameter, Object value) {
		filledParametersMap.put(parameter, value);		
	}

	public void finish() {		
		for (int i = 0; i < parametersEditorsList.size(); i++){
			ReportParametersEditor parametersEditor = 
				(ReportParametersEditor) parametersEditorsList.get(i);
			Map editorFilledParameters = parametersEditor.getFilledParameters();
			filledParametersMap.putAll(editorFilledParameters);
		}
		
		try {
			ReportsManager.getInstance().previewReport(report,filledParametersMap);
		} catch (ReportProcessingException e) {
			String texto = appContext.getI18nString("informes.wizard.error.rellenandoinforme");
			JOptionPane.showMessageDialog(this, texto);
		}
	}
		
	public void init(WizardPanel[] wizardPanels) {
		allWizardPanels = Arrays.asList(wizardPanels);

		for (int i = 0; i < wizardPanels.length; i++) {
			centerPanel.add((Component) wizardPanels[i], wizardPanels[i].getID());
			wizardPanels[i].setWizardContext(this);
		}

		completedWizardPanels = new ArrayList();
		wizardPanels[0].enteredFromLeft(dataMap);
		setCurrentWizardPanel(wizardPanels[0]);
		appContext.getMainFrame().pack();
	}

	public void fillParameters(Map filledParametersToAdd) {
		if (filledParametersToAdd != null && filledParametersToAdd.size() != 0){
			filledParametersMap.putAll(filledParametersToAdd);
		}
	}
	
	protected void checkNeededPermissions(){
		ApplicationContext permiso = AppContext.getApplicationContext();
		
//		GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.InfReferencia.GeneradorInforme");
//	    acceso = permiso.checkPermission(geopistaPerm,"Informacion de Referencia");
		
		GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.Expedir.Informe.Urbanistico");
		acceso = permiso.checkPermission(geopistaPerm, "Planeamiento");
	}

	public boolean acceso(){
		return acceso;
	}
}
