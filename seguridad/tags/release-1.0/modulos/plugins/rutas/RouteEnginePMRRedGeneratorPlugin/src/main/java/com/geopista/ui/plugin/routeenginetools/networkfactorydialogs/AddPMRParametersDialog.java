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
