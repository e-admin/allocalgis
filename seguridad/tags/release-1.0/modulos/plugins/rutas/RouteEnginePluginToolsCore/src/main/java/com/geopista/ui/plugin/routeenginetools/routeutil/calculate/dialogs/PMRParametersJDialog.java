package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs;

import javax.swing.JDialog;

/**
 * Muestra un diálogo para seleccionar las opciones en el cálculo de rutas de movilidad reducida
 * @author miriamperez
 *
 */
 
public class PMRParametersJDialog extends JDialog {
	private PMRParametersJPanel panel;
	
	public PMRParametersJPanel getPanel() {
		return panel;
	}

	public void setPanel(PMRParametersJPanel panel) {
		this.panel = panel;
	}

	public PMRParametersJDialog(){
		panel = new PMRParametersJPanel(this);
		this.getContentPane().add(panel);
		this.setSize(500, 400);
		this.setLocation(500, 300);
		this.setModal(true);
		this.setVisible(true);
	}

}
