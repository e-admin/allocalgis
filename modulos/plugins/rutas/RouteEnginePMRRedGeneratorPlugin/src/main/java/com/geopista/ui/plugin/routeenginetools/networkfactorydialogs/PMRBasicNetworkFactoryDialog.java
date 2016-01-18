/**
 * PMRBasicNetworkFactoryDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class PMRBasicNetworkFactoryDialog extends BasicNetworkFactoryDialog  implements ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5224329718344856621L;

	protected PanelToSelectMemoryNetworks redesPanel;


	public PMRBasicNetworkFactoryDialog(Frame frame, String title, PlugInContext context){
		super(frame, title, context);
	}

	
protected JPanel getPanelPrincipal()
    {

	rootPanel = new JPanel(new GridBagLayout());
	// redesPanel = new PanelToLoadFromDataStore(context.getWorkbenchContext());
//	redesPanel = new PanelToLoadFromDataLocalStore(context.getWorkbenchContext());
	redesPanel = new PanelToSelectMemoryNetworks(context);
	rootPanel.add(redesPanel, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5),
		0, 0));

	rootPanel.add(getFeaturesSelectPanel(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
		new Insets(0, 5, 0, 5), 0, 0));

	/*
	 * rootPanel.add(getDescriptionSelectPanel(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new
	 * Insets(0, 5, 0, 5), 0, 0));
	 * 
	 * rootPanel.add(getDirectionSelectPanel(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new
	 * Insets(0, 5, 0, 5), 0, 0));
	 */
	return rootPanel;
    }

	public String getsRedOrigen()
	{
	    if ("".equals(redesPanel.getSubredseleccionada()))
		return redesPanel.getRedSeleccionada();
	    else
		return redesPanel.getSubredseleccionada();
	}


	
	
	public JPanel getFeaturesSelectPanel() {
		// TODO Auto-generated method stub
		if (featuresSelectPanel == null){
			featuresSelectPanel = new JPanel(new GridBagLayout());

			featuresSelectPanel.setBorder(BorderFactory.createTitledBorder
					(null, "Datos de la red que se generará", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

/*			featuresSelectPanel.add(getFeatureRadioButton() , 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add(getLayerRadioButton() , 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add(new JLabel("Nombre de la red de partida"), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add( getNombreRedOrigenTextField() , 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));*/

			JPanel auxPanel = new JPanel(new GridBagLayout());

			auxPanel.add(new JLabel("Nombre de la red:") , 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			auxPanel.add(getNombreRedTextField() , 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));

			featuresSelectPanel.add( auxPanel, 
					new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 20));

		}
		return featuresSelectPanel;
	}

	protected boolean isInputValid() {

//BUG campo vacío		boolean insertedNetworkName = !this.nombreRedTextField.getText().equals("");
		boolean insertedNetworkName = !"".equals(this.nombreRedTextField.getText());
		if (!insertedNetworkName){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Inserte el nombre de la red");
			return false;
		}


		return insertedNetworkName; 
	}
}
