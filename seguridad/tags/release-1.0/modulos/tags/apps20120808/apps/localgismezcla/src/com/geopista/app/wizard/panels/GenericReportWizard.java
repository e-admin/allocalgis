package com.geopista.app.wizard.panels;

import java.util.ArrayList;

import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.wizard.panels.SeleccionInformeWizardPanel;
import com.geopista.util.ApplicationContext;

import net.sf.jasperreports.engine.JasperReport;

public class GenericReportWizard extends ReportWizard {
	
	public final static String TITLE = "Asistente para Generación de Informes";

	public GenericReportWizard(ApplicationContext appContext) {
		super(appContext, TITLE);
	}

	protected ArrayList getPostSelectionPanels() {
		return new ArrayList();
	}

	protected ArrayList getPreSelectionPanels() {
		ArrayList panels = new ArrayList();
		// Añadimos el panel de seleccion de informe generico
		panels.add(new SeleccionInformeWizardPanel());
		return panels;
	}

	protected JasperReport getPredefinedReport() throws ReportNotFoundException {
		// No hay informe predefinido.
		return null;
	}

}
