/**
 * AddPMRParametersDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.localgis.route.weighter.PMRProperties;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class AddPMRParametersDialog extends JDialog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7515232092196481759L;


	private PMRProperties pmrProperties;

	public PMRProperties getPmrProperties() {
		return pmrProperties;
	}

	public void setPmrProperties(PMRProperties pmrProperties) {
		this.pmrProperties = pmrProperties;
	}

	public AddPMRParametersDialog(Frame frame, String title, PlugInContext context,PMRProperties properties){
		super(frame, title, true);

		this.setSize(440, 200);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.pmrProperties = properties;
		this.initialize();

		this.setVisible(true);
	}
	
	public AddPMRParametersDialog(Frame frame, String title, PlugInContext context){
		super(frame, title, true);

		this.setSize(440, 200);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());
		this.initialize();

		this.setVisible(true);
	}

	public void initialize(){
		this.setLayout(new GridBagLayout());

		AddPMRParametersJPanel prmParametersJPanel  = new AddPMRParametersJPanel(this);
		this.add(prmParametersJPanel, 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
	}


	

}
