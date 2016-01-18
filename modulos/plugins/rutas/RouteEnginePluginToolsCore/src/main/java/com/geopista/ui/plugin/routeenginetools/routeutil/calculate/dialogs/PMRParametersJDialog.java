/**
 * PMRParametersJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
