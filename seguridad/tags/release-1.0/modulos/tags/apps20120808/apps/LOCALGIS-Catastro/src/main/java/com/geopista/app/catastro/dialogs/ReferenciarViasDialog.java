package com.geopista.app.catastro.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class ReferenciarViasDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2823273573109280018L;
	
	ReferenciarViasPanel referenciarPanel = null;
	OKCancelPanel okCancelPanel = null;
	
	public ReferenciarViasDialog(){
		super(AppContext.getApplicationContext().getMainFrame(),"Referenciación de Vías",true);
		
		initialize();
		this.pack();
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		
	}



	private ReferenciarViasPanel getReferenciarPanel(){
		if (referenciarPanel == null){
//			referenciarPanel = new ReferenciarViasPanel(title, title, null);
		}
		return referenciarPanel;
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
		return true; 
	}


	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private boolean allDataIscorrect() {
		
		
		JOptionPane.showMessageDialog(this, "Radio no válido. Debe estar compuesto sólo de caracteres numéricos separados por un punto '.'");
		return true;
	}

}
