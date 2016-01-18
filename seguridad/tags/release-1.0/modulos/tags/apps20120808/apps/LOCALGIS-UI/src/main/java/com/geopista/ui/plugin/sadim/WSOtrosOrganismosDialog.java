package com.geopista.ui.plugin.sadim;

import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.GeopistaNumerosPoliciaPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class WSOtrosOrganismosDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	ApplicationContext appContext = AppContext.getApplicationContext();

	public WSOtrosOrganismosDialog(PlugInContext context, JFrame owner, Properties props) {
		super(owner);

		this.setTitle(appContext.getI18nString("sadim.WSOtrosOrganismosPlugin.description"));

		this.setResizable(false);
		WSOtrosOrganismosPanel wsOtrosOrganismosPanel = new WSOtrosOrganismosPanel(context, props);
		this.getContentPane().add(wsOtrosOrganismosPanel);
		this.setSize(380, 380);
		this.setModal(true);
		this.setVisible(true);
		this.setLocation(150, 150);
	}
}