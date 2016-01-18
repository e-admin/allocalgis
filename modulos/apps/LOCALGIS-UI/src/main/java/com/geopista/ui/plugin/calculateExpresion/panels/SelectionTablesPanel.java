/**
 * SelectionTablesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.panels;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

public class SelectionTablesPanel extends JPanel{
	
	ApplicationContext appContext=AppContext.getApplicationContext();
	
	private JComboBox destinoJComboBox;
	private JList	origenJList;
	private JList	origenSelectedJList;
	private JRadioButton prov_mun_ent_pobl_JRadioButton;
	private JRadioButton prov_mun_JRadioButton;
	private JRadioButton prov_JRadioButton;
	private ButtonGroup buttonGroup;
	private JButton aceptarJButton;
	private JButton cancelarJButton;
	private JButton anadirJButton;
	private JButton eliminarJButton;
	
	private JPanel origenPanel;
	private JPanel destinoPanel;
	private JPanel relacionPanel;
	private JPanel botoneraPanel;

	

	public SelectionTablesPanel() {
		super();
		initialize();
		busquedaTablasEiel();
	}
	
	private  void initialize() {
		
		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		
		this.add(getOrigenPanel());
		this.add(getDestinoPanel());
		this.add(getRelacionPanel());
		this.add(getBotoneraPanel());
	}
	
	private void busquedaTablasEiel(){
		
	}
	
	public JList getOrigenSelectedJList() {
		return origenSelectedJList;
	}

	public void setOrigenSelectedJList(JList origenSelectedJList) {
		this.origenSelectedJList = origenSelectedJList;
	}

	
	public JComboBox getDestinoJComboBox() {
		return destinoJComboBox;
	}

	public void setDestinoJComboBox(JComboBox destinoJComboBox) {
		this.destinoJComboBox = destinoJComboBox;
	}


	public JList getOrigenJList() {
		return origenJList;
	}

	public void setOrigenJList(JList origenJList) {
		this.origenJList = origenJList;
	}

	public JRadioButton getProv_mun_ent_pobl_JRadioButton() {
		return prov_mun_ent_pobl_JRadioButton;
	}

	public void setProv_mun_ent_pobl_JRadioButton(
			JRadioButton prov_mun_ent_pobl_JRadioButton) {
		this.prov_mun_ent_pobl_JRadioButton = prov_mun_ent_pobl_JRadioButton;
	}

	public JRadioButton getProv_mun_JRadioButton() {
		return prov_mun_JRadioButton;
	}

	public void setProv_mun_JRadioButton(JRadioButton prov_mun_JRadioButton) {
		this.prov_mun_JRadioButton = prov_mun_JRadioButton;
	}

	public JRadioButton getProv_JRadioButton() {
		return prov_JRadioButton;
	}

	public void setProv_JRadioButton(JRadioButton prov_JRadioButton) {
		this.prov_JRadioButton = prov_JRadioButton;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}
	
	public JButton getAceptarJButton() {
		if(aceptarJButton == null){
			aceptarJButton = new JButton();
			aceptarJButton.setEnabled(true);
			aceptarJButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			aceptarJButton.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.guardarExpresion"));
			aceptarJButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					//saveFile();
				}
			}); 
			
		}
		return aceptarJButton;
	}

	public void setAceptarJButton(JButton aceptarJButton) {
		this.aceptarJButton = aceptarJButton;
	}

	public JButton getCancelarJButton() {
		if(cancelarJButton == null){
			cancelarJButton = new JButton();
			cancelarJButton.setEnabled(true);
			cancelarJButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			cancelarJButton.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.guardarExpresion"));
			cancelarJButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					
				}
			}); 
			
		}
		return cancelarJButton;
	}

	public void setCancelarJButton(JButton cancelarJButton) {
		this.cancelarJButton = cancelarJButton;
	}

	
	
	
	
	public JPanel getOrigenPanel() {
		if(origenPanel == null){
			origenPanel = new JPanel();
			
		}
		return origenPanel;
	}

	public void setOrigenPanel(JPanel origenPanel) {
		this.origenPanel = origenPanel;
	}

	public JPanel getDestinoPanel() {
		if(destinoPanel == null){
			destinoPanel = new JPanel();
			
		}
		return destinoPanel;
	}

	public void setDestinoPanel(JPanel destinoPanel) {
		this.destinoPanel = destinoPanel;
	}

	public JPanel getRelacionPanel() {
		if(relacionPanel == null){
			relacionPanel = new JPanel();
			
		}
		return relacionPanel;
	}

	public void setRelacionPanel(JPanel relacionPanel) {
		this.relacionPanel = relacionPanel;
	}
	
	public JButton getAnadirJButton() {

		return anadirJButton;
	}

	public void setAnadirJButton(JButton anadirJButton) {
		this.anadirJButton = anadirJButton;
	}

	public JButton getEliminarJButton() {
		return eliminarJButton;
	}

	public void setEliminarJButton(JButton eliminarJButton) {
		this.eliminarJButton = eliminarJButton;
	}

	public JPanel getBotoneraPanel() {
		if(botoneraPanel == null){
			botoneraPanel = new JPanel();
			
		}
		return botoneraPanel;
	}

	public void setBotoneraPanel(JPanel botoneraPanel) {
		this.botoneraPanel = botoneraPanel;
	}

}
