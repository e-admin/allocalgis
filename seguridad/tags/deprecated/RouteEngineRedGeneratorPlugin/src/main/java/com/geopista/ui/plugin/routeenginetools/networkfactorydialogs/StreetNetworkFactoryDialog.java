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

import com.geopista.app.utilidades.EdicionUtils;
import com.geopista.model.GeopistaLayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class StreetNetworkFactoryDialog extends BasicNetworkFactoryDialog {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7515232092196481759L;

	private JComboBox tipoCallejeroComboBox = null;
	private JComboBox velocidadViaComboBox = null;
	private JComboBox sentidoCirculacionComboBox = null;

	private JPanel panelStreetNetwork = null;

	public StreetNetworkFactoryDialog(Frame frame, String title, PlugInContext context) {
		super(frame, title, context);
		// TODO Auto-generated constructor stub

		this.setSize(460, 460);

		this.setResizable(false);

		this.initialize();
	}

	public void initialize(){


		this.add(getPanelSreetNetwork(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}


	private JPanel getPanelSreetNetwork(){
		if (panelStreetNetwork == null){

			panelStreetNetwork = new JPanel(new GridBagLayout());
			panelStreetNetwork.setBorder(BorderFactory.createTitledBorder
					(null, "Propiedades de callejero", 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 

			panelStreetNetwork.add(new JLabel("Tipo de Calle:"), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(getTipoCallejeroComboBox(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(new JLabel("Velocidad de la Vía:"), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(getVelocidadViaComboBox(), 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(new JLabel("Sentido de Circulación:"), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(getSentidoCirculacionComboBox(), 
					new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return panelStreetNetwork;
	}

	public JComboBox getTipoCallejeroComboBox() {
		// TODO Auto-generated method stub
		if (tipoCallejeroComboBox == null){
			tipoCallejeroComboBox = new JComboBox(new Vector(new ArrayList()));
			tipoCallejeroComboBox.setEnabled(false);

		}
		return tipoCallejeroComboBox;
	}

	public JComboBox getVelocidadViaComboBox() {
		// TODO Auto-generated method stub
		if (velocidadViaComboBox == null){
			velocidadViaComboBox = new JComboBox(new Vector(new ArrayList()));
			velocidadViaComboBox.setEnabled(false);
		}
		return velocidadViaComboBox;
	}

	public JComboBox getSentidoCirculacionComboBox() {
		// TODO Auto-generated method stub
		if (sentidoCirculacionComboBox == null){
			List listaTiposDireccion = new ArrayList();
			listaTiposDireccion.add(StreetTrafficRegulation.BIDIRECTIONAL);
			listaTiposDireccion.add(StreetTrafficRegulation.DIRECT);
			listaTiposDireccion.add(StreetTrafficRegulation.INVERSE);
			listaTiposDireccion.add(StreetTrafficRegulation.FORBIDDEN);

			sentidoCirculacionComboBox= new JComboBox(new Vector(listaTiposDireccion));
			
			sentidoCirculacionComboBox.setEnabled(false);
			sentidoCirculacionComboBox.setSelectedIndex(-1);
		}
		return sentidoCirculacionComboBox;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		super.itemStateChanged(e);

		boolean isSelected = (e.getStateChange() == ItemEvent.SELECTED);

		if (e.getItemSelectable() == this.getFeatureRadioButton()){
			if (this.getFeatureRadioButton().isSelected()){
				this.getTipoCallejeroComboBox().setEnabled(false);
				this.getTipoCallejeroComboBox().setSelectedIndex(-1);
				
				this.getSentidoCirculacionComboBox().setEnabled(false);
				this.getSentidoCirculacionComboBox().setSelectedIndex(-1);
			} else{
				tipoCallejeroComboBoxController(true, true);
				velocidadViaComboBoxController(true, true);
				
				this.getSentidoCirculacionComboBox().setEnabled(true);
				this.getSentidoCirculacionComboBox().setSelectedIndex(0);
			}
		}

		if (e.getItemSelectable() == this.getLayerComboBox()){
			if (this.getLayerComboBox().getSelectedIndex() >= 0){
				this.tipoCallejeroComboBoxController(true, true);
				this.velocidadViaComboBoxController(true, true);
				
				this.getSentidoCirculacionComboBox().setEnabled(true);
				this.getSentidoCirculacionComboBox().setSelectedIndex(0);
				
			} else{
				this.tipoCallejeroComboBoxController(false, false);
				
				this.getSentidoCirculacionComboBox().setEnabled(false);
				this.getSentidoCirculacionComboBox().setSelectedIndex(-1);
			}
		}
		
	}

	private void velocidadViaComboBoxController(boolean velocidadController, boolean cargar) {
		// TODO Auto-generated method stub
		
		ArrayList attributesLayerList = new ArrayList();
		attributesLayerList.add("50 Km/h");
		
		if (this.getLayerComboBox().getSelectedIndex() >= 0){
			Layer selectedLayer = context.getLayerManager().getLayer(
					context.getLayerManager().getLayers().indexOf(
							this.getLayerComboBox().getSelectedItem()
					)
			);
			
		
//			if ( !((GeopistaLayer)selectedLayer).isLocal() && 
//					!this.getFeatureRadioButton().isSelected()){
				if (velocidadController && cargar){
					//			selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().
					for (int i = 0; i < selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
						attributesLayerList.add(selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
					}
					
					this.velocidadViaComboBox.setEnabled(velocidadController);
				}
						
//			}
		}
		EdicionUtils.cargarLista(this.velocidadViaComboBox, attributesLayerList);
	}

	private void tipoCallejeroComboBoxController(boolean tipoCallejeroController, boolean cargar) {
		// TODO Auto-generated method stub

		if (this.getLayerComboBox().getSelectedIndex() >= 0){
			Layer selectedLayer = context.getLayerManager().getLayer(
					context.getLayerManager().getLayers().indexOf(
							this.getLayerComboBox().getSelectedItem()
					)
			);

			//TODO ï¿½por quï¿½ se ignoran las capas locales?
//			if(!((GeopistaLayer)selectedLayer).isLocal() && !this.getFeatureRadioButton().isSelected() )
//			if(!this.getFeatureRadioButton().isSelected() )
//			{
				if (tipoCallejeroController && cargar){

					ArrayList attributesLayerList = new ArrayList();
					attributesLayerList.add("SIN TIPO");

					for (int i = 0; i < selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
						attributesLayerList.add(selectedLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
					}

					EdicionUtils.cargarLista(this.tipoCallejeroComboBox, attributesLayerList);
				}

				this.tipoCallejeroComboBox.setEnabled(tipoCallejeroController);
			}
//			else
//			{
//				this.tipoCallejeroComboBox.setEnabled(false);
//				this.tipoCallejeroComboBox.setSelectedIndex(-1);
//			}
//		}
	}


	protected boolean isInputValid() {
		boolean basicNetworkValid = super.isInputValid();

		return basicNetworkValid; 
	}

}
