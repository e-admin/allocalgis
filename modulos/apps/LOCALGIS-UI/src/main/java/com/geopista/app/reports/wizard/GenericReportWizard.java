/**
 * GenericReportWizard.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.app.reports.wizard.panels.SeleccionInformeWizardPanel;
import com.geopista.util.ApplicationContext;

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
