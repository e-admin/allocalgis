/**
 * GeoMarketingDataDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GeoMarketingOT2;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.panels.GeoMarketingTextDataPanel;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class GeoMarketingDataDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8720914191643184582L;


	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;

	private GeoMarketingTextDataPanel textDataPanel = null;


	private GeoMarketingOT2 geoMarketingData = null;
	private GeopistaLayer geopistaLayer = null;

	
	public GeoMarketingDataDialog(PlugInContext context, GeoMarketingOT2 geoMarketingData2, GeopistaLayer selectedLayer){
		super(AppContext.getApplicationContext().getMainFrame(),"Informe de Datos de GeoMarketing",false);
		this.geoMarketingData = geoMarketingData2;
		this.geopistaLayer  = selectedLayer;

		initialize();
		this.pack();
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}


	private void initialize() {
		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}


	private JPanel getRootPanel(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(this.getGeoDataPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return rootPanel;
	}

	
	private JPanel getGeoDataPanel(){
		if (textDataPanel == null){
			textDataPanel = new GeoMarketingTextDataPanel(this.geoMarketingData, this.geopistaLayer);
		}
		return textDataPanel;
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
		return true;
	}

}
