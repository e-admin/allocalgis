/**
 * SeleccionInformeWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.jasperreports.engine.JasperReport;

import com.geopista.app.AppContext;
import com.geopista.app.reports.ReportInfo;
import com.geopista.app.reports.ReportsManager;

public class SeleccionInformeWizardPanel extends ReportWizardPanel {
	
	protected String reportName;

	private JCheckBox checkMostrarSubinformes;
	private JLabel lblSeleccionInforme;
	private JList listInformesDisponibles;
	private JScrollPane scrollpnlInformesDisponibles;
	
	private List filteredReportList;
	private List unFilteredReportList;
	
	private boolean filterSubreports;
	
	// Contexto de la aplicaicon geopista
	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	public SeleccionInformeWizardPanel(){
		super();
		try {
			initForm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enteredPanelFromLeft(Map dataMap) {
		try {
			init();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void exitingToRight() throws Exception {
		JasperReport report = ReportsManager.getInstance().loadReport(reportName);
		reportWizard.setReport(report);
	}

	public boolean isInputValid() {
		if (reportWizard.acceso() && reportName != null){
			return true;
		}
		else {
			return false;
		}
	}
	
	private void init(){
		filterSubreports = true;
		filteredReportList = null;
		unFilteredReportList = null;
	}
			
	protected void initForm() throws Exception {
		BorderLayout borderLayout = new BorderLayout();
		pnlGeneral.setLayout(borderLayout);
		
		JPanel northPanel = new JPanel();
		BorderLayout northPanelBorderLayout = new BorderLayout();
		northPanel.setLayout(northPanelBorderLayout);
		pnlGeneral.add(northPanel, BorderLayout.NORTH);
		
		JPanel panelSubReportFilter = new JPanel();
		FlowLayout flowLayoutSubReportFilter = new FlowLayout();
		flowLayoutSubReportFilter.setAlignment(FlowLayout.LEFT);
		panelSubReportFilter.setLayout(flowLayoutSubReportFilter);
		northPanel.add(panelSubReportFilter, BorderLayout.SOUTH);
		
		JPanel pnlInstructions = new JPanel();
		FlowLayout flowLayoutInstructions = new FlowLayout();
		flowLayoutInstructions.setAlignment(FlowLayout.LEFT);
		pnlInstructions.setLayout(flowLayoutInstructions);
		northPanel.add(pnlInstructions, BorderLayout.NORTH);
		
		lblSeleccionInforme = new JLabel();
		lblSeleccionInforme.setText(aplicacion.getI18nString("informes.wizard.seleccioninforme.seleccioninforme"));
		pnlInstructions.add(lblSeleccionInforme);
		
		checkMostrarSubinformes = new JCheckBox();		
		checkMostrarSubinformes.setText(aplicacion.getI18nString("informes.wizard.seleccioninforme.mostrarsubinformes"));		
		checkMostrarSubinformes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				checkFilterSubreportChanged();				
			}			
		});
		panelSubReportFilter.add(checkMostrarSubinformes);
		
		scrollpnlInformesDisponibles = new JScrollPane();	
		scrollpnlInformesDisponibles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pnlGeneral.add(scrollpnlInformesDisponibles, BorderLayout.CENTER);
		
		ReportInfo[] reportsInfo = getReportsInfo();		
		listInformesDisponibles = new JList(reportsInfo);
		scrollpnlInformesDisponibles.getViewport().setView(listInformesDisponibles);
		listInformesDisponibles.setCellRenderer(new ReportInfoCellRenderer());
		
		ListSelectionModel informesDiposniblesSelectionModel = listInformesDisponibles.getSelectionModel();
		informesDiposniblesSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		informesDiposniblesSelectionModel.addListSelectionListener(new ListSelectionHandler());	
	}
	
	protected ReportInfo[] getReportsInfo(){
		ReportInfo[] reportsInfo = null;
		
		if (filterSubreports){
			if (filteredReportList == null){
				filteredReportList = ReportsManager.getInstance().getReportInfoList(true);
			}
			reportsInfo = new ReportInfo[filteredReportList.size()];
			filteredReportList.toArray(reportsInfo);
		}
		else {
			if (unFilteredReportList == null){
				unFilteredReportList = ReportsManager.getInstance().getReportInfoList(false);
			}
			reportsInfo = new ReportInfo[unFilteredReportList.size()];
			unFilteredReportList.toArray(reportsInfo);
		}
		
		return reportsInfo;
	}
	
	protected void selectionChanged(){
		listInformesDisponibles.getSelectedValue();
		ReportInfo reportInfo = (ReportInfo)
			listInformesDisponibles.getSelectedValue();
		
		if (reportInfo!=null)
			reportName = reportInfo.getFilename();
		
		getWizardContext().inputChanged();
	}
	
	protected void checkFilterSubreportChanged(){
		filterSubreports = !checkMostrarSubinformes.isSelected();

		ReportInfo[] reportsInfo = getReportsInfo();		
		listInformesDisponibles.setListData(reportsInfo);
	}
	
	class ListSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {				        
			selectionChanged();
		}
	}
	
	public class ReportInfoCellRenderer implements ListCellRenderer {

		JTextArea labelDescription;
		JTextArea labelFilename;
		JPanel panel;	
		
		final Color EVEN_BACKGROUND_COLOR = new Color(235,235,235);
		final Color ODD_BACKGROUND_COLOR = new Color(255,255,255);
		
		public ReportInfoCellRenderer(){
			panel = new JPanel();
			panel.setSize(495, 385);
			GridLayout gridLayout = new GridLayout(2,1);
			panel.setLayout(gridLayout);
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			labelDescription = new JTextArea();
			panel.add(labelDescription);
			
			labelFilename = new JTextArea();
			panel.add(labelFilename);
		}
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			ReportInfo reportInfo = (ReportInfo) value;
			labelDescription.setText(reportInfo.getDescription());
			labelFilename.setText("Fichero: " + reportInfo.getFilename());
			labelDescription.setBorder(new EmptyBorder(new Insets(3,3,2,3)));
			labelFilename.setBorder(new EmptyBorder(new Insets(0,3,3,3)));
			
			if (isSelected){
				labelDescription.setBackground(Color.blue);
				labelDescription.setForeground(Color.white);
				labelFilename.setBackground(Color.blue );
				labelFilename.setForeground(Color.white);
			}
			else {
				labelDescription.setForeground(Color.black);
				labelFilename.setForeground(Color.black);
				if (index%2 == 0){
					labelDescription.setBackground(ODD_BACKGROUND_COLOR);					
					labelFilename.setBackground(ODD_BACKGROUND_COLOR);
				}
				else {
					labelDescription.setBackground(EVEN_BACKGROUND_COLOR);
					labelFilename.setBackground(EVEN_BACKGROUND_COLOR);
				}
			}
			
			return panel;
		}
    }
}
