/**
 * VentanaError.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

	public void setVisible() {
		// TODO Cambiar de dialogo... esto estï¿½ mal hecho
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
