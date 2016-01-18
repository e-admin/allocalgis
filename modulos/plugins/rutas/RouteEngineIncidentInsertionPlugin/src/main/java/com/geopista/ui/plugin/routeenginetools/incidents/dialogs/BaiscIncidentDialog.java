/**
 * BaiscIncidentDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.incidents.dialogs;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;


import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.ui.components.DateField;
import com.geopista.ui.plugin.routeenginetools.incidents.images.IconLoader;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class BaiscIncidentDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173471090269810692L;

	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
	
	private LocalGISIncident incident = null;
	private static Logger LOGGER = Logger.getLogger(BaiscIncidentDialog.class);
	
	private JComboBox incidentTypeComboBox = null;
	private DateField startDateField = null;
	private DateField finishDateField = null;
	private JCheckBox startDateCheckBox = null;
	private JCheckBox finishDateCheckBox = null;
	private TextArea descriptionTextField = null;
	private JButton recurrenceButton = null;
	
	
	public BaiscIncidentDialog(Frame parentPanel, String title, LocalGISIncident incident){
        
		super(parentPanel, title, true);
		if (incident != null){
			this.incident = incident;
			this.loadIncidentData(incident);
		} else{
			this.incident = new LocalGISIncident();
		}
		this.setSize(350, 300);
		this.setLocationRelativeTo(parentPanel);
		this.initialize();
		this.setResizable(false);
		this.setEnabled(true);
		this.setVisible(true);
	}
	
	private void loadIncidentData(LocalGISIncident incident) {
		// TODO Auto-generated method stub
		if (incident != null){
			if (incident.getIncidentType() == 2){
				this.getIncidentTypeComboBox().setSelectedIndex(1);
			} else if (incident.getIncidentType() == 1){
				this.getIncidentTypeComboBox().setSelectedIndex(0);
			}
			
			if (incident.getDateStart() != null){
				this.getStartDateField().setDate(incident.getDateStart());
				this.getStartDateCheckBox().setSelected(true);
				this.getStartDateField().setEnabled(true);
			}
			
			if(incident.getDateEnd() != null){
				this.getFinishDateField().setDate(incident.getDateEnd());
				this.getFinishDateCheckBox().setSelected(true);
				this.getStartDateField().setEnabled(true);
			}
			
			this.getDescriptionTextField().setText(incident.getDescription());
		}
	}



	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	
	
	
	
	private JPanel getRootPanel() {
		// TODO Auto-generated method stub
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
						
			JPanel incidentDataPanel = new JPanel(new GridBagLayout());
			incidentDataPanel.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidentproperties"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

			incidentDataPanel.add(this.getTypePanel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			incidentDataPanel.add(this.getDatesPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			incidentDataPanel.add(this.getRecurrenceButton(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			rootPanel.add(incidentDataPanel, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 20));
			
			rootPanel.add(this.getDescriptionPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		
		}
		return rootPanel;
	}
	
	

	private JPanel getDescriptionPanel() {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder
				(null, I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidentproperties"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
		
		panel.add(this.getDescriptionTextField(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		return panel;
	}

	private JPanel getDatesPanel() {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(this.getStartDateCheckBox(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_END, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel.add(new JLabel(I18N.get("networkIncidents","routeengina.incidents.commonsdialog.startdatelabel")), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel.add(this.getStartDateField(), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		panel.add(this.getFinishDateCheckBox(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_END, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel.add(new JLabel(I18N.get("networkIncidents","routeengina.incidents.commonsdialog.enddatelabel")), 
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel.add(this.getFinishDateField(), 
				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		return panel;
	}

	private DateField getFinishDateField() {
		// TODO Auto-generated method stub
		if (finishDateField == null){
			finishDateField = new DateField(new Date(), 0);
			finishDateField.setEnabled(false);
		}
		return finishDateField;
	}

	private JCheckBox getFinishDateCheckBox() {
		// TODO Auto-generated method stub
		if (finishDateCheckBox == null){
			finishDateCheckBox = new JCheckBox();
			finishDateCheckBox.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (finishDateCheckBox.isSelected()){
						getFinishDateField().setEnabled(true);
					} else{
						getFinishDateField().setEditable(false);
					}
				}
				
			});			
		}
		return finishDateCheckBox;
	}

	private DateField getStartDateField() {
		// TODO Auto-generated method stub
		if (startDateField == null){
			startDateField = new DateField(new Date(),0);
			startDateField.setEnabled(false);
		}
		return startDateField;
	}

	private JCheckBox getStartDateCheckBox() {
		// TODO Auto-generated method stub
		if (startDateCheckBox == null){
			startDateCheckBox = new JCheckBox();
			startDateCheckBox.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (startDateCheckBox.isSelected()){
						getStartDateField().setEnabled(true);
					} else{
						getStartDateField().setEditable(false);
					}
				}
				
			});			
		}
		return startDateCheckBox;
	}
	
	private TextArea getDescriptionTextField() {
		// TODO Auto-generated method stub
		if (descriptionTextField == null){
			descriptionTextField =  new TextArea(I18N.get("networkIncidents","routeengine.incidents.basicdialog.textarea"),2,20,
			        TextArea.SCROLLBARS_VERTICAL_ONLY );
			descriptionTextField.enableInputMethods(true);
			
			descriptionTextField.addFocusListener(new FocusListener(){

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					if (descriptionTextField.getText().equals(I18N.get("networkIncidents","routeengine.incidents.basicdialog.textarea"))){
						descriptionTextField.selectAll();
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
		return descriptionTextField;
	}

	private JPanel getTypePanel() {
		// TODO Auto-generated method stub
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidenttypelabel")), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		panel.add(this.getIncidentTypeComboBox(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		return panel;
	}

	private JComboBox getIncidentTypeComboBox() {
		// TODO Auto-generated method stub
		if (incidentTypeComboBox == null){
			incidentTypeComboBox = new JComboBox();
			ArrayList<String> tipos = new ArrayList<String>();
			tipos.add(I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidenttypeclosed"));
			tipos.add(I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidenttypeclosedtovehicles"));
						
			EdicionUtils.cargarLista(incidentTypeComboBox, tipos);
			
		}
		return incidentTypeComboBox;
	}
	
	private JButton getRecurrenceButton(){
		if (this.recurrenceButton == null){
			recurrenceButton = new JButton(I18N.get("networkIncidents","routeengine.incidents.basicdialog.recurrencebuttonlabel"));
			recurrenceButton.setIcon(IconLoader.icon(I18N.get("networkIncidents","routeengine.incidents.basicdialog.recurrencebuttonicon")));
			recurrenceButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					onRecurrenceButtonDo();
				}				
			});
		}
		return recurrenceButton;
	}
	
	private void onRecurrenceButtonDo() {
		// TODO Auto-generated method stub
		RecursiveIncidentDialog dialog = new RecursiveIncidentDialog(this,
				I18N.get("networkIncidents","routeengine.incidents.recurrencedialog.tittle"), 
				this.incident.getRecursivencident());
		if (dialog.wasOKPressed()){
			this.incident.setReursiveIncident(dialog.getRecursiveIncident());
		}
		
	}
	
	
	public LocalGISIncident getIncident() {
		// TODO Auto-generated method stub
		return this.incident;
	}

	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}
	
	protected boolean isInputValid() {
		if (!allDataIscorrect()){
			return false;
		}
		return setDataToIncident(); 
	}

	private boolean setDataToIncident() {
		// TODO Auto-generated method stub
		
		if (this.getIncidentTypeComboBox() != null && this.getIncidentTypeComboBox().getSelectedItem() != null){
			if (this.getIncidentTypeComboBox().getSelectedItem().equals(I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidenttypeclosed"))){
				this.incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
			} else if(this.getIncidentTypeComboBox().getSelectedItem().equals(I18N.get("networkIncidents","routeengine.incidents.basicdialog.incidenttypeclosedtovehicles"))){
				this.incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
			}
		} else{
			this.incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
		}
		
		if (this.getStartDateCheckBox().isSelected()){
			if (this.getStartDateField() != null && this.getStartDateField().getDate() != null){
				incident.setDateStart(this.getStartDateField().getDate());
			}
		} 
		
		if (this.getFinishDateCheckBox().isSelected()){
			if (this.getFinishDateField()!= null && this.getFinishDateField().getDate()!=null){
				incident.setDateEnd(this.getFinishDateField().getDate());
			}
		}
		
		if (this.descriptionTextField != null && this.descriptionTextField.getText() != null){
			this.incident.setDescription(this.getDescriptionTextField().getText());
		}
		
		
		System.err.println(this.incident.toString());
		return true;
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private boolean allDataIscorrect() {
		// TODO Auto-generated method stub

		return true;
	}

	
	
}
