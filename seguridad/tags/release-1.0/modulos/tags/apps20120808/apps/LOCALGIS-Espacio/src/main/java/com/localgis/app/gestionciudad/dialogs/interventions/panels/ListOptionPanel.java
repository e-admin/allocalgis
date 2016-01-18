/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;

/**
 * @author javieraragon
 */
public class ListOptionPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8268296317506382974L;

	private ButtonGroup listOptionButtonGroup = new ButtonGroup();;
	private JRadioButton listAllRadioButton = null;
	private JRadioButton listNotesRadioButton = null;
	private JRadioButton listUrbanActionRadioButton = null;
	private JRadioButton listInfrastructureRadioButton = null;
	private JRadioButton listUrbanTracksRadioButton = null;
	
	private JComboBox orderListByComboBox = null;
	
	private JButton listOptionButton = null;
	
	private JPanel actionTypesPanel = null;
	private JPanel orderByPanel = null;
	private JPanel buttonsPanel = null;

	
	public ListOptionPanel(){
		super(new GridBagLayout());
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		initialize();
	}


	private void initialize() {	
		this.setBorder(BorderFactory.createTitledBorder
				(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listoptionstitle"), 
						TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
		
		this.add(this.getActionTypesPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		this.add(this.getOrderByPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		this.add(this.getButtonsPanel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	
	private JPanel getActionTypesPanel(){
		if (actionTypesPanel == null){
			actionTypesPanel = new JPanel(new GridBagLayout());
			actionTypesPanel.add(this.getListAllRadioButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			actionTypesPanel.add(this.getListNotesRadioButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			actionTypesPanel.add(this.getListUrbanActionRadioButton(), 
					new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			actionTypesPanel.add(this.getListInfrastructureRadioButton(), 
					new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			actionTypesPanel.add(this.getListUrbanTracksRadioButton(), 
					new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return actionTypesPanel;
	}
	
	
	private JPanel getOrderByPanel(){
		if (orderByPanel == null){
			orderByPanel = new JPanel(new GridBagLayout());
			orderByPanel.add(new JLabel(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.orderbylaber")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			orderByPanel.add(this.getOrderListByComboBox(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return orderByPanel;
	}
	
	
	private JPanel getButtonsPanel(){
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel(new GridBagLayout());
			buttonsPanel.add(getListOptionButton(), 
					new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return buttonsPanel;
	}
		
	
	private JRadioButton getListAllRadioButton(){
		if (listAllRadioButton == null){
			listAllRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listallradiobutton"));
			listOptionButtonGroup.add(listAllRadioButton);
		}
		return listAllRadioButton;
	}
	
	
	private JRadioButton getListNotesRadioButton(){
		if ( listNotesRadioButton == null){
			listNotesRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listnotesradiobutton"));
			listOptionButtonGroup.add(listNotesRadioButton);
		}
		return listNotesRadioButton;
	}
	
	
	private JRadioButton getListUrbanActionRadioButton(){
		if ( listUrbanActionRadioButton == null){
			listUrbanActionRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listurbanactionradiobutton"));
			listOptionButtonGroup.add(listUrbanActionRadioButton);
		}
		return listUrbanActionRadioButton;
	}
	
	
	private JRadioButton getListInfrastructureRadioButton(){
		if (listInfrastructureRadioButton == null){
			listInfrastructureRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listinfraestructureactionradiobutton"));
			listOptionButtonGroup.add(listInfrastructureRadioButton);
		}
		return listInfrastructureRadioButton;
	}
	
	
	private JRadioButton getListUrbanTracksRadioButton(){
		if (listUrbanTracksRadioButton == null){
			listUrbanTracksRadioButton = new JRadioButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listurbantracksactionsradiobutton"));
			listOptionButtonGroup.add(listUrbanTracksRadioButton);
		}
		return listUrbanTracksRadioButton;
	}
	
	
	private JComboBox getOrderListByComboBox(){
		if (orderListByComboBox == null){
			orderListByComboBox = new JComboBox();
			orderListByComboBox.addItem(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.creationdate"));
			orderListByComboBox.addItem(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.nextadvisedate"));
			orderListByComboBox.addItem(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.idAction"));
		}
		return orderListByComboBox;
	}
	
	
	private JButton getListOptionButton(){
		if (listOptionButton == null){
			listOptionButton = new JButton(I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.panel.listoption.orderactuationby.listoptionbuttonname"));
			listOptionButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onListOptionButtonDo();
				}
			});
		}
		return listOptionButton;
	}
	
	
	private void onListOptionButtonDo() {
		// TODO Auto-generated method stub
		// realizar operaciones para ordenar y listar según las opciones elegidas
	}

}
