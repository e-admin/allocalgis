/**
 * 
 */
package com.localgis.app.gestionciudad.dialogs.interventions.dialogs;


import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class TextAreaEditOkCandelDialog extends JDialog{
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
	
	private TextArea editTextArea = null;
	
	private Dimension textAreaDimension = null;
	
	public TextAreaEditOkCandelDialog(Frame parentFrame, String tittle, boolean modal, ArrayList<String> strings, Dimension textAreaDimension){
		super(parentFrame, tittle, modal);
		
		this.textAreaDimension = textAreaDimension;
		UtilidadesAvisosPanels.inicializarIdiomaAvisosPanels();
		this.initialize();
		
		this.pack();
		this.setVisible(true);
	}
	
	
	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		
		this.add(getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	
	private JPanel getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
		}
		return rootPanel;
	}
	
	
	private TextArea getEditTextArea(){
		if (editTextArea == null){
			if (textAreaDimension == null){
				editTextArea = new TextArea("",2,20,
						editTextArea.SCROLLBARS_HORIZONTAL_ONLY);
			} else{
				editTextArea = new TextArea("",this.textAreaDimension.height,
						this.textAreaDimension.width,
						editTextArea.SCROLLBARS_HORIZONTAL_ONLY);
			}
			
			editTextArea.setEditable(true);
			editTextArea.setEnabled(true);
		}
		return editTextArea;
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
		if (this.getEditTextArea()!=null && this.getEditTextArea().getText()!=null
				&& !this.getEditTextArea().getText().equals("")){
			return true;	
		} else{
			JOptionPane.showMessageDialog(null,I18N.get("avisospanels","localgisgestionciudad.interfaces.avisos.intervention.textarea.edit.notvalid"));
			return false;
		}
		
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}
	
	public ArrayList<String> getStringTextArea(){
		ArrayList<String> resultado = new ArrayList<String>();
		String [] lineas = this.getEditTextArea().getText().split("\n");
		if (lineas!=null && lineas.length>0){
			for(int i = 0; i < lineas.length; i++){
				resultado.add(lineas[i]);
			}
		}
		
		if (resultado!=null && !resultado.isEmpty()){
			return resultado;
		}
		
		return null;
	}
	
	
	
	
}
