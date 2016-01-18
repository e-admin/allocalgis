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
import javax.swing.JOptionPane;

import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.panels.GeoMarketingFeaturesListPanel;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2;
import com.vividsolutions.jump.I18N;
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
