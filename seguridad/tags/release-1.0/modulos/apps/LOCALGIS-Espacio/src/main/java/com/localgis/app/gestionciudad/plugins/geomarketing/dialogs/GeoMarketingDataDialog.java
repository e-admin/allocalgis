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


	private com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2 geoMarketingData = null;
	private GeopistaLayer geopistaLayer = null;

	
	public GeoMarketingDataDialog(PlugInContext context, com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2 geoMarketingData2, GeopistaLayer selectedLayer){
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
