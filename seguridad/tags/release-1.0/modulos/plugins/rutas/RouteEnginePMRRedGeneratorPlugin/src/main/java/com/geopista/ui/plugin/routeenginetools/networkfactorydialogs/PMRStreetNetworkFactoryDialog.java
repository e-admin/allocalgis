package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.uva.routeserver.street.StreetTrafficRegulation;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class PMRStreetNetworkFactoryDialog extends PMRBasicNetworkFactoryDialog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7515232092196481759L;

	public PMRStreetNetworkFactoryDialog(Frame frame, String title, PlugInContext context) {
		super(frame, title, context);
		// TODO Auto-generated constructor stub

		this.setSize(460, 260);

		this.setResizable(false);
	}


}
