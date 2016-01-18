/**
 * SeletedLayerGeomarketingDataDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;

import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GeoMarketingOT2;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.panels.GeoMarketingFeaturesListPanel;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class SeletedLayerGeomarketingDataDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9124632457706478295L;

	private GeoMarketingFeaturesListPanel listAndDataPanel= null;
	private OKCancelPanel okCancelPanel = null;
	
	private GeoMarketingOT2[] geoMarKetingDataArray = null;
	private Layer selectedLayer = null;
	private Layer geoMarketingLayer = null;

	private PlugInContext context = null;
	
	
	public SeletedLayerGeomarketingDataDialog(Frame parentFrame, String title ,
			GeoMarketingOT2[] geoDataArray, Layer selectedLayer, Layer geoMarketingLayer,
			PlugInContext context){
		super(parentFrame, title, false);
		
		this.context  = context;
		this.selectedLayer = selectedLayer;
		this.geoMarKetingDataArray = geoDataArray;
		this.geoMarketingLayer  = geoMarketingLayer;
		
		this.initialize();
		
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
	
	
	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		
		this.add(getListAndDataPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	
	private GeoMarketingFeaturesListPanel getListAndDataPanel(){
		if (listAndDataPanel == null){
			listAndDataPanel = new GeoMarketingFeaturesListPanel(this.selectedLayer,this.geoMarketingLayer,this.geoMarKetingDataArray, this, this.context);
		}
		return listAndDataPanel;
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
		//TODO implementar si hiciese falta validar datos al salir del formulario
		return true;
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}
}
