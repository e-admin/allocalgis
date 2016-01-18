package com.geopista.ui.plugin.routeenginetools.networkfactorydialogs;


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class StreetFeaturePropertiesDialog extends BasicFeaturePropertiesDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1989079554956684643L;
	private JPanel panelStreetNetwork;
	private JComboBox velocidadViaComboBox;
	private JComboBox tipoCallejeroComboBox;
	private JComboBox sentidoCirculacionComboBox;

	
	public StreetFeaturePropertiesDialog(GeopistaLayer layer, String title, PlugInContext context) {
		super(layer, title, context);
		// TODO Auto-generated constructor stub

		this.setSize(460, 300);

		this.setResizable(false);

		this.initialize();
	}
	
	private void initialize(){
		
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

			panelStreetNetwork.add(new JLabel("Velocidad de la Via:"), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			panelStreetNetwork.add(getVelocidadViaComboBox(), 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			panelStreetNetwork.add(new JLabel("Sentido de Circulacion:"), 
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
	
	public JComboBox getSentidoCirculacionComboBox() {
		// TODO Auto-generated method stub
		if (sentidoCirculacionComboBox == null){
			List listaTiposDireccion = new ArrayList();
			listaTiposDireccion.add(StreetTrafficRegulation.BIDIRECTIONAL);
			listaTiposDireccion.add(StreetTrafficRegulation.DIRECT);
			listaTiposDireccion.add(StreetTrafficRegulation.INVERSE);
			listaTiposDireccion.add(StreetTrafficRegulation.FORBIDDEN);

			sentidoCirculacionComboBox= new JComboBox(new Vector(listaTiposDireccion));
			
		}
		return sentidoCirculacionComboBox;
	}
	
	public JComboBox getTipoCallejeroComboBox() {
		// TODO Auto-generated method stub
		if (tipoCallejeroComboBox == null){
			tipoCallejeroComboBox = new JComboBox(new Vector(new ArrayList()));
			tipoCallejeroComboBox.setEnabled(true);
			
			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("SIN TIPO");
			
			for (int i = 0; i < this.getLayer().getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(this.getLayer().getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}
			
			com.geopista.app.utilidades.EdicionUtils.cargarLista(tipoCallejeroComboBox, attributesLayerList);

		}
		return tipoCallejeroComboBox;
	}

	public JComboBox getVelocidadViaComboBox() {
		// TODO Auto-generated method stub
		if (velocidadViaComboBox == null){
			velocidadViaComboBox = new JComboBox(new Vector(new ArrayList()));
			velocidadViaComboBox.setEnabled(true);
			
			ArrayList attributesLayerList = new ArrayList();
			attributesLayerList.add("50 Km/h");
			
			for (int i = 0; i < this.getLayer().getFeatureCollectionWrapper().getFeatureSchema().getAttributeCount(); i++){
				attributesLayerList.add(this.getLayer().getFeatureCollectionWrapper().getFeatureSchema().getAttributeName(i));
			}
			
			EdicionUtils.cargarLista(velocidadViaComboBox, attributesLayerList);
		}
		return velocidadViaComboBox;
	}
	
	protected boolean isInputValid() {
		boolean basicNetworkValid = super.isInputValid();
		return basicNetworkValid; 
	}
}
