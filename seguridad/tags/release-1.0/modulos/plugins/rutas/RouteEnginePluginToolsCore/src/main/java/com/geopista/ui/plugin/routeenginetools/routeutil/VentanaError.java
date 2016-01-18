package com.geopista.ui.plugin.routeenginetools.routeutil;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/**
 * 
 * @author Fernando Create a window and it shows an error message.
 */
public class VentanaError {
	MultiInputDialog Error;

	public VentanaError(PlugInContext context) {
		Error = new MultiInputDialog(context.getWorkbenchFrame(), "ERROR", true);

	}

	public boolean OK() {
		return Error.wasOKPressed();

	}

	public void addText(String text) {
		Error.addLabel(text);
	}
	
	public void addTextField (String fieldName, String initialValue, int approxWidthInChars, 
			EnableCheck[] enableChecks, String toolTipText){
		Error.addTextField(fieldName, initialValue, approxWidthInChars, enableChecks, toolTipText);
	}

	public void mostrar() {
		// TODO Cambiar de dialogo... esto está mal hecho
		// 
		((JPanel)((JPanel)((JLayeredPane)((JRootPane)Error.getComponents()[0]).
				getComponents()[1]).
					getComponents()[0]).
						getComponents()[2]).setVisible(false);

		
		Error.setVisible(true);

	}
	
	public String getTextField(String fieldName){
		return Error.getText(fieldName);
	}

}
