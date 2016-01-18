package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.EdicionUtils;

import com.geopista.feature.GeopistaSchema;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerNameRenderer;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class BasicNetworkFactoryDialog extends JDialog  implements ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5224329718344856621L;

	protected JRadioButton featureRadioButton = null;
	protected JRadioButton layerRadioButton = null; 
	protected JRadioButton bidirectionRadioButton = null;
	protected JRadioButton uniqueDirectionAtoBRadioButton = null;
	protected JRadioButton uniqueDirectionBtoARadioButton = null;
	protected JComboBox layerComboBox= null;
	protected JComboBox impedanciaBA = null;
	protected JComboBox impedanciaAB = null;
	protected JComboBox description = null;
	protected JTextField nombreRedTextField = null;

	protected ButtonGroup featuresButtonGroup = new ButtonGroup();
	protected ButtonGroup directionButtonGroup = new ButtonGroup();


	protected JPanel rootPanel = null; 
	protected JPanel featuresSelectPanel = null;
	protected JPanel directionSelectPanel = null;
	protected JPanel descriptionPanel = null;
	protected OKCancelPanel okCancelPanel = null;


	protected PlugInContext context;


	public BasicNetworkFactoryDialog(Frame frame, String title, PlugInContext context){
		super(frame, title, true);
		this.context = context;

		this.setSize(440, 375);
		this.setLocationRelativeTo(AppContext.getApplicationContext().getMainFrame());

		this.initialize();

	}


	private void initialize() {
		// TODO Auto-generated method stub
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



	protected JPanel getPanelPrincipal(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(getFeaturesSelectPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

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

	protected JPanel getDescriptionSelectPanel() {
		// TODO Auto-generated method stub
		if (descriptionPanel == null){
			descriptionPanel = new JPanel(new GridBagLayout());

			descriptionPanel.setBorder(BorderFactory.createTitledBorder
					(null, "Decripciones de la red", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

			descriptionPanel.add(new JLabel("Campo descripcion:") ,
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


	public JTextField getNombreRedTextField() {
		// TODO Auto-generated method stub
		if (nombreRedTextField == null){
			nombreRedTextField = new JTextField(20);
		}
		return nombreRedTextField;
	}


	public JComboBox getDescriptionComboBox() {
		// TODO Auto-generated method stub
		if (description == null){
			description = new JComboBox(new Vector(new ArrayList()));
			description.setEnabled(true);
		}
		return description;
	}


	public JPanel getFeaturesSelectPanel() {
		// TODO Auto-generated method stub
		if (featuresSelectPanel == null){
			featuresSelectPanel = new JPanel(new GridBagLayout());

			featuresSelectPanel.setBorder(BorderFactory.createTitledBorder
					(null, "Seleccion de features", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));

			featuresSelectPanel.add(getFeatureRadioButton() , 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add(getLayerRadioButton() , 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add(new JLabel(I18N.get("genred","routeengine.genred.selectlayer.comboboxname")), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			featuresSelectPanel.add( getLayerComboBox() , 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

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


	public JRadioButton getFeatureRadioButton() {
		// TODO Auto-generated method stub
		if (this.featureRadioButton == null){
			this.featureRadioButton = new JRadioButton(
					I18N.get("genred","routeengine.genred.features.radiobuttonname")
			);
			this.featuresButtonGroup.add(this.featureRadioButton);

			//featureRadioButton.addItemListener(this);

			this.featureRadioButton.setSelected(true);
		}		
		return this.featureRadioButton;
	}

	public JRadioButton getLayerRadioButton() {
		// TODO Auto-generated method stub
		if (layerRadioButton == null){
			this.layerRadioButton = new JRadioButton(
					I18N.get("genred","routeengine.genred.alllayer.radiobuttonname")
			);
			this.featuresButtonGroup.add(this.layerRadioButton);

			layerRadioButton.addItemListener(this);
		}

		return this.layerRadioButton;
	}

	public JComboBox getLayerComboBox() {
		// TODO Auto-generated method stub
		if (this.layerComboBox == null){

			List layerList = new ArrayList();
			layerList.add(null);
			layerList.addAll(context.getLayerManager().getLayers());

			this.layerComboBox = new JComboBox(new Vector(layerList));
			layerComboBox.setEnabled(true);

			this.layerComboBox.setRenderer(new  LayerNameRenderer() );

			layerComboBox.addItemListener(this);

		}
		return this.layerComboBox;
	}


	public JPanel getDirectionSelectPanel() {
		// TODO Auto-generated method stub
		if (this.directionSelectPanel == null){
			this.directionSelectPanel = new JPanel(new GridBagLayout());
			this.directionSelectPanel.setBorder(BorderFactory.createTitledBorder
					(null, "Impedancias", 
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
			comboboxesdirectionSelectPanel.add( new JLabel("Impedancia AB"), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			comboboxesdirectionSelectPanel.add( getImpedanciaAB(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));

			comboboxesdirectionSelectPanel.add( new JLabel("Impedancia BA"), 
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
					"bidireccional"
			);
			this.directionButtonGroup.add(this.bidirectionRadioButton);

			this.bidirectionRadioButton.addItemListener(this);

			this.bidirectionRadioButton.setSelected(true);
			
			this.bidirectionRadioButton.setEnabled(true);
		}

		return this.bidirectionRadioButton;
	}


	public JRadioButton getUniqueDirectionAtoBRadioButton() {
		// TODO Auto-generated method stub
		if (uniqueDirectionAtoBRadioButton == null){
			this.uniqueDirectionAtoBRadioButton = new JRadioButton(
					"unidireccional de A-B"
			);
			this.directionButtonGroup.add(this.uniqueDirectionAtoBRadioButton);

			this.uniqueDirectionAtoBRadioButton.addItemListener(this);
			
			this.uniqueDirectionAtoBRadioButton.setEnabled(true);
		}

		return this.uniqueDirectionAtoBRadioButton;
	}


	public JRadioButton getUniqueDirectionBtoARadioButton() {
		// TODO Auto-generated method stub
		if (uniqueDirectionBtoARadioButton == null){
			this.uniqueDirectionBtoARadioButton = new JRadioButton(
					"unidireccional de B-A"
			);
			this.directionButtonGroup.add(this.uniqueDirectionBtoARadioButton);

			this.uniqueDirectionBtoARadioButton.addItemListener(this);
			
			this.uniqueDirectionBtoARadioButton.setEnabled(true);
			
		}

		return this.uniqueDirectionBtoARadioButton;
	}

	public JComboBox getImpedanciaBA() {
		// TODO Auto-generated method stub
		if (this.impedanciaBA == null){
			this.impedanciaBA = new JComboBox(new Vector(
					new ArrayList()
			));
			impedanciaBA.setEnabled(true);

		}
		return this.impedanciaBA;
	}


	public JComboBox getImpedanciaAB() {
		// TODO Auto-generated method stub
		if (this.impedanciaAB == null){
			this.impedanciaAB = new JComboBox(new Vector(
					new ArrayList()
			));
			impedanciaAB.setEnabled(true);

		}
		return this.impedanciaAB;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);

		if (e.getItemSelectable() == this.layerRadioButton) {
//			if (isSelected == true) {
//				this.layerComboBox.setEnabled(true);
//				this.getBidirectionRadioButton().setEnabled(true);
//				this.getUniqueDirectionAtoBRadioButton().setEnabled(true);
//				this.getUniqueDirectionBtoARadioButton().setEnabled(true);
//				this.getImpedanciaAB().setEnabled(true);
//				this.getImpedanciaBA().setEnabled(true);
//			} else {
//				this.layerComboBox.setEnabled(false);
//				this.getBidirectionRadioButton().setEnabled(false);
//				this.getUniqueDirectionAtoBRadioButton().setEnabled(false);
//				this.getUniqueDirectionBtoARadioButton().setEnabled(false);
//				this.getImpedanciaAB().setEnabled(false);
//				this.getImpedanciaBA().setEnabled(false);
//				
//				this.getLayerComboBox().setSelectedIndex(-1);
//			}
		} 	

		if (e.getItemSelectable() == this.getFeatureRadioButton()){
			if (this.getFeatureRadioButton().isSelected()){
				this.getDescriptionComboBox().setEnabled(false);
				this.getDescriptionComboBox().setSelectedIndex(-1);
			} else{
				descripcionComboBoxController(true, true);
			}
		}


		if (e.getItemSelectable() == this.bidirectionRadioButton){
			if (this.bidirectionRadioButton.isSelected()){
				if (this.layerComboBox.getSelectedIndex() >= 0){
					this.impedanciasComboBoxController(true, true, false);
				}
			}
		} else 
			if (e.getItemSelectable() == this.uniqueDirectionAtoBRadioButton){
				if (this.uniqueDirectionAtoBRadioButton.isSelected()){
					if (this.getLayerComboBox().getSelectedIndex() >= 0){
						this.impedanciasComboBoxController(true, false, false);
					}
				}
			} else	
				if (e.getItemSelectable() == this.uniqueDirectionBtoARadioButton){
					if (this.uniqueDirectionBtoARadioButton.isSelected()){
						if (this.getLayerComboBox().getSelectedIndex() >= 0){
							this.impedanciasComboBoxController(false, true, false);
						}
					}
				}  


		if (e.getItemSelectable() == this.getLayerComboBox()){
			if (this.getLayerComboBox().getSelectedIndex() >= 0){
				if (this.getBidirectionRadioButton().isSelected()){
					this.impedanciasComboBoxController(true, true, true);
				}else if (this.getUniqueDirectionAtoBRadioButton().isSelected()){
					this.impedanciasComboBoxController(true, false, true);
				}else if (this.getUniqueDirectionBtoARadioButton().isSelected()){
					this.impedanciasComboBoxController(false, true, true);
				}

				this.descripcionComboBoxController(true, true);
			} else{
				this.descripcionComboBoxController(false, false);
				this.impedanciasComboBoxController(false, false, false);
			}
		} 
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

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	public void impedanciasComboBoxController(boolean impedanciaAB, boolean impedanciaBA, boolean cargar ) {

		if ((impedanciaAB || impedanciaBA) && cargar){

			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("Longitud");

			Layer selectedLayer = context.getLayerManager().getLayer(
					context.getLayerManager().getLayers().indexOf(
							this.getLayerComboBox().getSelectedItem()
					)
			);

			for (int i = 0; i < selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}

			if (impedanciaAB && impedanciaBA){
				EdicionUtils.cargarLista(this.getImpedanciaAB(), attributesLayerList);
				EdicionUtils.cargarLista(this.getImpedanciaBA(), attributesLayerList);
			} else if (impedanciaAB){
				EdicionUtils.cargarLista(this.getImpedanciaAB(), attributesLayerList);
			} else if (impedanciaBA){
				EdicionUtils.cargarLista(this.getImpedanciaBA(), attributesLayerList);
			}

		}

		this.getImpedanciaAB().setEnabled(impedanciaAB);
		this.getImpedanciaBA().setEnabled(impedanciaBA);

	}


	private void descripcionComboBoxController(boolean descripcionController, boolean cargar) {
		// TODO Auto-generated method stub
		if (this.getLayerComboBox().getSelectedIndex() >= 0){
			Layer selectedLayer = context.getLayerManager().getLayer(
					context.getLayerManager().getLayers().indexOf(
							this.getLayerComboBox().getSelectedItem()
					)
			);
// BUG: No permitia descrpciones de capas locales
//			if ( 
//				!((GeopistaLayer)selectedLayer).isLocal() && 
//				!this.getFeatureRadioButton().isSelected())
			    {

				if (descripcionController && cargar){

					ArrayList attributesLayerList = new ArrayList();
					attributesLayerList.add("SIN DESCRIPCION");

					FeatureSchema featureSchema = selectedLayer.getFeatureCollectionWrapper().getFeatureSchema();
					for (int i = 0; i < featureSchema.getAttributeCount(); i++){
						if (featureSchema instanceof GeopistaSchema)
						    {
							GeopistaSchema geopistaSchema = (GeopistaSchema)featureSchema;
							attributesLayerList.add(geopistaSchema.getColumnByAttribute(featureSchema.getAttributeName(i)));
						    }
						
					}

					
					EdicionUtils.cargarLista(this.description, attributesLayerList);
				}

				this.description.setEnabled(descripcionController);
			} 
//			    else {
//				this.description.setEnabled(true);
//				this.description.setSelectedIndex(-1);
//			}
		}
	}


	public void showDialog(boolean visible){
		this.setVisible(visible);
	}

	protected boolean isInputValid() {
		boolean selectedfeatures = this.featureRadioButton.isSelected() || 
		(layerRadioButton.isSelected() && layerComboBox.getSelectedIndex() >0);
		if (!selectedfeatures){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Selecione las features para formar la red. (de features seleccionadas o de capa)");
			return false;
		}

		boolean insertedNetworkName = !this.nombreRedTextField.getText().equals("");
		if (!insertedNetworkName){
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "Inserte el nombre de la red");
			return false;
		}


		return selectedfeatures && insertedNetworkName; 
	}
}
