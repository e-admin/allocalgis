/**
 * BasicFeaturePropertiesDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;

import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class BasicFeaturePropertiesDialog  extends JDialog implements ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3894100022903417186L;
	private JPanel rootPanel;
	private JPanel descriptionPanel;
	private JComboBox description;
	private OKCancelPanel okCancelPanel;
	private GeopistaLayer layer = null;
	private JPanel directionSelectPanel;
	private JRadioButton bidirectionRadioButton;
	private ButtonGroup directionButtonGroup = new ButtonGroup();
	private JRadioButton uniqueDirectionAtoBRadioButton;
	private JRadioButton uniqueDirectionBtoARadioButton;
	private JComboBox impedanciaBA;
	private JComboBox impedanciaAB;
	
	public GeopistaLayer getLayer(){
		return layer;
	}
	
	public void setLayer(GeopistaLayer layer){
		this.layer = layer;
	}

	public BasicFeaturePropertiesDialog(GeopistaLayer layer, String title, PlugInContext context){
		super(context.getWorkbenchFrame(), title, true);
		this.layer = layer;

		this.setSize(440, 200);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());

		this.initialize();
	}
	
	private void initialize(){
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		this.add(this.getPanelPrincipal(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));

		//CUIDADO! Se deja un hueco en medio para poder insertar el panel con las opciones de
		// StreetNetwotrk... si se quieren extender para mas dialogos. modificar.

		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private JPanel getPanelPrincipal(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(getDescriptionSelectPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			rootPanel.add(getDirectionSelectPanel(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return rootPanel;
	}
	
	public JPanel getDescriptionSelectPanel() {
		// TODO Auto-generated method stub
		if (descriptionPanel == null){
			descriptionPanel = new JPanel(new GridBagLayout());

			descriptionPanel.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("genred","routeengine.genred.dialog.networkdescription"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

			descriptionPanel.add(new JLabel(I18N.get("genred","routeengine.genred.dialog.descriptionfield")) ,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			descriptionPanel.add(getDescriptionComboBox() , 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));
		}
		return descriptionPanel;
	}
	
	public JComboBox getDescriptionComboBox() {
		// TODO Auto-generated method stub
		if (description == null){
			description = new JComboBox(new Vector(new ArrayList()));
			description.setEnabled(true);
			
			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("SIN DESCRIPCION");
			
			for (int i = 0; i < this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(((GeopistaSchema)this.layer.getFeatureCollectionWrapper().getFeatureSchema()).getColumnByAttribute(this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i)));
				
			}
			
			EdicionUtils.cargarLista(description, attributesLayerList);
		}
		return description;
	}
	
	public JPanel getDirectionSelectPanel() {
		// TODO Auto-generated method stub
		if (this.directionSelectPanel == null){
			this.directionSelectPanel = new JPanel(new GridBagLayout());
			this.directionSelectPanel.setBorder(BorderFactory.createTitledBorder
					(null, I18N.get("genred","routeengine.genred.dialog.impedanciastittle"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

			JPanel radiobuttondirectionSelectPanel = new JPanel(new GridBagLayout());

			radiobuttondirectionSelectPanel.add(getBidirectionRadioButton(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			radiobuttondirectionSelectPanel.add(getUniqueDirectionAtoBRadioButton(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			radiobuttondirectionSelectPanel.add( getUniqueDirectionBtoARadioButton(), 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			this.directionSelectPanel.add( radiobuttondirectionSelectPanel, 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));


			JPanel comboboxesdirectionSelectPanel = new JPanel(new GridBagLayout());
			comboboxesdirectionSelectPanel.add( new JLabel(I18N.get("genred","routeengine.genred.dialog.impedanciascomboAB")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			comboboxesdirectionSelectPanel.add( getImpedanciaAB(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));

			comboboxesdirectionSelectPanel.add( new JLabel(I18N.get("genred","routeengine.genred.dialog.impedanciascomboBA")), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			comboboxesdirectionSelectPanel.add( getImpedanciaBA(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));

			this.directionSelectPanel.add( comboboxesdirectionSelectPanel, 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

		}
		return this.directionSelectPanel;
	}
	
	public JRadioButton getBidirectionRadioButton() {
		// TODO Auto-generated method stub
		if (bidirectionRadioButton == null){
			this.bidirectionRadioButton = new JRadioButton(
					I18N.get("genred","routeengine.genred.dialog.bidirectionalbutton")
			);
			this.directionButtonGroup.add(this.bidirectionRadioButton);

			this.bidirectionRadioButton.addItemListener(this);

			this.bidirectionRadioButton.setSelected(true);
		}

		return this.bidirectionRadioButton;
	}


	public JRadioButton getUniqueDirectionAtoBRadioButton() {
		// TODO Auto-generated method stub
		if (uniqueDirectionAtoBRadioButton == null){
			this.uniqueDirectionAtoBRadioButton = new JRadioButton(
					I18N.get("genred","routeengine.genred.dialog.unidirectionalAB")
			);
			this.directionButtonGroup.add(this.uniqueDirectionAtoBRadioButton);

			this.uniqueDirectionAtoBRadioButton.addItemListener(this);
		}

		return this.uniqueDirectionAtoBRadioButton;
	}


	public JRadioButton getUniqueDirectionBtoARadioButton() {
		// TODO Auto-generated method stub
		if (uniqueDirectionBtoARadioButton == null){
			this.uniqueDirectionBtoARadioButton = new JRadioButton(
					I18N.get("genred","routeengine.genred.dialog.unidirectionalBA")
			);
			this.directionButtonGroup.add(this.uniqueDirectionBtoARadioButton);

			this.uniqueDirectionBtoARadioButton.addItemListener(this);
		}

		return this.uniqueDirectionBtoARadioButton;
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

	
	
	public JComboBox getImpedanciaBA() {
		// TODO Auto-generated method stub
		if (this.impedanciaBA == null){
			this.impedanciaBA = new JComboBox(new Vector(
					new ArrayList()
			));
			impedanciaBA.setEnabled(false);
			
			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("Longitud");
			
			for (int i = 0; i < this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}
			
			EdicionUtils.cargarLista(impedanciaBA, attributesLayerList);

		}
		
		return this.impedanciaBA;
	}


	public JComboBox getImpedanciaAB() {
		// TODO Auto-generated method stub
		if (this.impedanciaAB == null){
			this.impedanciaAB = new JComboBox(new Vector(
					new ArrayList()
			));
			impedanciaAB.setEnabled(false);
			
			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("Longitud");
			
			for (int i = 0; i < this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(this.layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}
			
			EdicionUtils.cargarLista(impedanciaAB, attributesLayerList);

		}
		return this.impedanciaAB;
	}
	
	

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getItemSelectable() == this.bidirectionRadioButton){
			if (this.bidirectionRadioButton.isSelected()){
					this.impedanciasComboBoxController(true, true);
			}
		} else 
			if (e.getItemSelectable() == this.uniqueDirectionAtoBRadioButton){
				if (this.uniqueDirectionAtoBRadioButton.isSelected()){
						this.impedanciasComboBoxController(true, false);
				}
			} else	
				if (e.getItemSelectable() == this.uniqueDirectionBtoARadioButton){
							this.impedanciasComboBoxController(false, true);
					}  
	}
	
	

	public void impedanciasComboBoxController(boolean impedanciaAB, boolean impedanciaBA) {

		this.getImpedanciaAB().setEnabled(impedanciaAB);
		this.getImpedanciaBA().setEnabled(impedanciaBA);

	}
}
