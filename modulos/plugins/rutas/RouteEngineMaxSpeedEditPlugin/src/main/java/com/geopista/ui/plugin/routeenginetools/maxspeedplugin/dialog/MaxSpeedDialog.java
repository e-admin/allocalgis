/**
 * MaxSpeedDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.maxspeedplugin.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;


import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.ui.plugin.routeenginetools.routeutil.CharacterLengthControler;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalNetworkDAO;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class MaxSpeedDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -408793835259680161L;
	
	public static String KILOMETRES_HOUR = "Km/h";
	public static String METERS_SECOND = "m/s";
	public static String MILES_HOUR = "mph";
	public static String MILES_SECOND = "mps";
	
	private JPanel rootPanel = null;
	private OKCancelPanel okCancelPanel = null;
	private JTextField maxSpeedField = null;
	private JComboBox speedUnitsComboBox = null;
	
	private PlugInContext context = null;
	protected LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
	
	private String lastUnitSelected = null;
	
	private static Logger LOGGER = Logger.getLogger(MaxSpeedDialog.class);
	
	public MaxSpeedDialog(PlugInContext context, String title, double speed){
		super(context.getWorkbenchFrame(), title, true);
		this.context = context;
		this.setSize(450, 400);
		this.setLocationRelativeTo(context.getWorkbenchFrame());
		this.initialize();
		this.setResizable(false);
		this.setEnabled(true);
		this.pack();
		this.getMaxSpeedField().setText(String.valueOf(speed));
		this.setVisible(true);
	}


	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

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
	
	
	
	
	private JPanel getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
			
			rootPanel.add(new JLabel(I18N.get("maxspeedplugin","routeengine.maxspeed.maxspeedlabel")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			rootPanel.add(this.getMaxSpeedField(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));


			rootPanel.add(this.getSpeedUnitsComboBox(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
		}
		return rootPanel;
	}


	public JComboBox getSpeedUnitsComboBox() {
		// TODO Auto-generated method stub
		if (speedUnitsComboBox == null){
			speedUnitsComboBox = new JComboBox();
			ArrayList list = new ArrayList();
			list.add(this.METERS_SECOND);
			list.add(this.KILOMETRES_HOUR);
			list.add(this.MILES_HOUR);
			list.add(this.MILES_SECOND);
			EdicionUtils.cargarLista(speedUnitsComboBox, list);
			speedUnitsComboBox.setSelectedItem(this.KILOMETRES_HOUR);
			this.lastUnitSelected = this.KILOMETRES_HOUR;
			// TODO Cambiar si se activa el cambio de unidades de velocidad
			this.speedUnitsComboBox.setEnabled(false);
			speedUnitsComboBox.addActionListener(new ActionListener()
            {
            	public void actionPerformed(ActionEvent e)
            	{   
            		onSpeedUnitsComboBoxIndexSelected(e);
            	}
            });
		}
		return speedUnitsComboBox;
	}
	
	private void onSpeedUnitsComboBoxIndexSelected(ActionEvent e) {
		// TODO Auto-generated method stub
		if (speedUnitsComboBox.getSelectedIndex() >=0){
			if (!lastUnitSelected.equals(speedUnitsComboBox.getSelectedItem())){
				// Hacer Cambio de Unidades.
				
			}
		}
	}
	
	public double getNominalMaxSpeed(){
		if (!this.getMaxSpeedField().getText().equals("")){
			try{
				double a = 1000.0;
				double b = 3600.0;
			return Double.parseDouble(this.getMaxSpeedField().getText()) * a / b;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				e.printStackTrace();
				LOGGER.error(e);
				return 0;
			}
		}else{
			return 0;
		}
	}


	public JTextField getMaxSpeedField() {
		if (maxSpeedField == null){
			maxSpeedField = new JTextField();
			//maxSpeedField.setDocument(new CharacterLengthControler(maxSpeedField, 10));
			maxSpeedField.setHorizontalAlignment(JTextField.RIGHT);
		}
		return maxSpeedField;
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
		return true; 
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

}
