/**
 * ProyectosDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.sugerencias;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.I18N;



public class ProyectosDialog extends JDialog{
	
	private JButton cancelButton;
	private JButton aceptButton;
	private JComboBox proyectNameJC;  
	private JLabel proyectLabel;
	private boolean canceled = true;

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel errorLabel = null;
	private List listProyectos = null;

	
	private I18N i18n = I18N.getInstance();
	
	public ProyectosDialog(Frame f, List listProyectos)  {
		super(f);		
		setTitle(getExternalizedString("SystemProyectPanel.proyectCode"));
		this.listProyectos = listProyectos;
		initialize();
	}

	private void initialize(){
		
		proyectLabel = new JLabel();
		proyectNameJC = createCombo();
		aceptButton = new JButton();
		cancelButton = new JButton();

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton();
			}
		});

		aceptButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				acceptButton();
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setName(getExternalizedString("Login Dialog")); 
		setSize(new java.awt.Dimension(400,167));

		this.setContentPane(getJPanel());
		this.setResizable(false);  

		proyectLabel.setText(getExternalizedString("SystemProyectPanel.proyectTitle"));

		proyectLabel.setPreferredSize(new java.awt.Dimension(50,15));
		proyectNameJC.setFocusCycleRoot(false);  
		aceptButton.setText(getExternalizedString("Aceptar")); 
		cancelButton.setText(getExternalizedString("Cancelar"));
		cancelButton.setSelected(true); 
	}

 
	private JPanel getJPanel() {
		if (jPanel == null) {
			errorLabel = new JLabel();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 5;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(10,0,0,0);
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 6;
			errorLabel.setText("");
			jPanel.add(proyectNameJC, gridBagConstraints1);
			jPanel.add(proyectLabel, gridBagConstraints5);
			jPanel.add(getJPanel1(), gridBagConstraints7); 
			jPanel.add(errorLabel, gridBagConstraints11);
		}
		return jPanel;
	}
	
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			java.awt.FlowLayout flowLayout8 = new FlowLayout();
			jPanel1 = new JPanel();
			jPanel1.setLayout(flowLayout8);
			flowLayout8.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel1.add(aceptButton, null);
			jPanel1.add(cancelButton, null);
		}
		return jPanel1;
	}
	
	public void setProyecto(String proyecto)
	{
		proyectNameJC.setSelectedItem(proyecto);
	}
	public String getProyecto()
	{
		String sProyecto = (String)proyectNameJC.getSelectedItem();
		return sProyecto;

	}


	private void cancelButton()
	{
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	private void acceptButton()
	{
		canceled=false;  
		this.setVisible(false);
		canceled=false;
		this.dispose();
	}

	public boolean isCanceled()
	{
		return this.canceled;
	}


	/**
	 * @return Returns the errorLabel.
	 */
	
	 public String getErrorLabel()
	 {
		 return errorLabel.getText();
	 }
	 
	 /**
	  * @param errorLabelText The errorLabel to set.
	  */
	 
	 public void setErrorLabel(String errorLabelText)
	 {
		 this.errorLabel.setText(errorLabelText);
	 }



	 private String getExternalizedString(String key) {
		 return ((AppContext) AppContext.getApplicationContext()).getI18nString(key);
	 }
	 
	    /**
	     * Crea un comboBox con los proyectos
	     */
	    private JComboBox createCombo(){
	    	JComboBox jc = new JComboBox();
	    		Iterator itProyect = listProyectos.iterator();
		    	while(itProyect.hasNext()){
		    		Proyecto proyecto = (Proyecto)itProyect.next();
			        String nombreProyecto = proyecto.getId()+"-"+proyecto.getNombre();
			        jc.addItem(nombreProyecto);
		    	}

	    	return jc;
	    }
}
