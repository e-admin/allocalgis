/**
 * InformeUrbanisticoReportWizard.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.reports.ReportsManager;
import com.geopista.app.reports.exceptions.ReportNotFoundException;
import com.geopista.util.ApplicationContext;

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
