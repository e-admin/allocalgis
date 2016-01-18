/**
 * ReportWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard.panels;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.reports.wizard.ReportWizard;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public abstract class ReportWizardPanel extends JPanel implements WizardPanel {
	
	protected ReportWizard reportWizard;
	
	protected String id = null; 
	
	protected String nextID = null;
	
	protected JPanel pnlImagen = new JPanel();
	protected JLabel lblImagen = new JLabel();	
	protected JPanel pnlGeneral = new JPanel (); 
	
	public ReportWizardPanel(){
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		
		FlowLayout flowLayout = new FlowLayout();
		pnlImagen.setLayout(flowLayout);	
				
		lblImagen.setBorder(BorderFactory.createLineBorder(Color.black,1));
		lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
		pnlImagen.add(lblImagen);

		pnlGeneral.setBounds(new Rectangle(135, 20, 600, 490));
		pnlGeneral.setBorder(BorderFactory.createTitledBorder(""));
		pnlGeneral.setLayout(null);
		
		this.add(pnlImagen, BorderLayout.WEST);
		this.add(pnlGeneral, BorderLayout.CENTER);
		
		//this.setSize(750,600);
	}

	public void setReportWizard(ReportWizard reportWizard){
		this.reportWizard = reportWizard;
	}

	public void add(InputChangedListener listener) {
		// Vacio		
	}
	
	public void enteredFromLeft(Map dataMap) {
		try {
			enteredPanelFromLeft(dataMap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void exiting() {
		if (nextID == null && !reportWizard.isCanceled()){
			reportWizard.finish();
		}
	}

	public String getID() {
		return id; 
	}

	public String getInstructions() {
		return "";
	}

	public String getNextID() {
		return nextID;
	}

	public String getTitle() {
		return "";
	}

	public void remove(InputChangedListener listener) {
		// Vacio		
	}

	public void setNextID(String nextID) {
		this.nextID = nextID;
	}

	public void setID(String id){
		this.id = id;
	}

	protected WizardContext getWizardContext(){
		return reportWizard;
	}
	
	public void setWizardContext(WizardContext wizardContext){ 
		if (wizardContext instanceof ReportWizard){
			reportWizard = (ReportWizard) wizardContext;
		}
	}
	
	protected abstract void enteredPanelFromLeft(Map dataMap);

	
}
