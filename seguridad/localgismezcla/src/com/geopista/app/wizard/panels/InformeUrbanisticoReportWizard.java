package com.geopista.app.wizard.panels;

import java.util.ArrayList;

import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.ReportsContext;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;

import net.sf.jasperreports.engine.JasperReport;

public class InformeUrbanisticoReportWizard  extends ReportWizard {
	
	public static final String WIZARD_TITLE = "Planeamiento: Informe Urbanistico";
	
	public InformeUrbanisticoReportWizard(ApplicationContext appContext) {
		super(appContext, WIZARD_TITLE);
		
	}

	protected JasperReport getPredefinedReport() throws ReportNotFoundException {
		return ReportsManager.getInstance().loadReport("InformeUrbanistico.jrxml");
	}
	
	protected ArrayList getPreSelectionPanels(){
		return new ArrayList();
	}
	
	protected ArrayList getPostSelectionPanels(){
		return new ArrayList();
	}
		
}
