package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.uva.route.network.NetworkManager;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;

@SuppressWarnings("serial")
public class PanelToSaveInDataStore extends WithOutConnectionPanel implements
		ActionListener {

	private JTextField nombreenbaseField;
	private JComboBox listasubredes, listaredes;
	private final PlugInContext context;

	public PanelToSaveInDataStore(final PlugInContext context) {
		super(context.getWorkbenchContext());// se coloca el combo de la
		// conexion
		this.context = context;
		initialize();

	}

	private void initialize() {

		addRow("Redes disponibles", getRedesComboBox(), null, false);
		addRow("Lista de subredes", getSubredesComboBox(), null, false);
		addRow("Nombre de SubRed", getFieldNombreenBase(), null, false);

		// addRow("Nombre de subred en base", getTextFieldnombreenbase(),
		// null,true);
		//
		// We are not using addRow because we want the widgets centered over the
		// OK/Cancel buttons.
		//
//		getConnectionComboBox().addActionListener(this);
	}

	public void actionPerformed(final ActionEvent e) {
		final Object obj = e.getSource();// getSource recoge qué objeto fue
		// pulsado
		// cogemos el nombre de la red que es seleccionado
		if (obj.equals(listaredes)) {
			final NetworkManager networkMgr = FuncionesAuxiliares
					.getNetworkManager(context);
			@SuppressWarnings("unused")
			final Set<String> subredesSet = (Set<String>) networkMgr.getNetwork(listaredes.getSelectedItem().toString()).getSubnetworks().keySet();

		}
	}

	protected JComboBox getSubredesComboBox() {
		if (listasubredes == null)
			listasubredes = new JComboBox();
		final NetworkManager networkMgr = FuncionesAuxiliares
				.getNetworkManager(context);
		
		
		if (listaredes.getSelectedItem() != null){
			Set<String> subredesSet = networkMgr.getNetwork(listaredes
					.getSelectedItem().toString()).getSubnetworks().keySet();// obtener lista de redes ya

			listasubredes.setModel(new DefaultComboBoxModel(
					sortByString(subredesSet.toArray())));
		}
		// calculadas
		

		listasubredes.setPreferredSize(new Dimension(MAIN_COLUMN_WIDTH,
				(int) listasubredes.getPreferredSize().getHeight()));
		listasubredes.setEditable(false);

		return listasubredes;
	}

	protected JComboBox getRedesComboBox() {
		if (listaredes == null)
			listaredes = new JComboBox();
		final NetworkManager networkMgr = FuncionesAuxiliares
				.getNetworkManager(context);
		listaredes.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					try {

						final Set<String> subredesSet = networkMgr
								.getNetwork(listaredes.getSelectedItem()
										.toString()).getSubnetworks().keySet();// obtener lista de redes
						// ya calculadas
						listasubredes.setModel(new DefaultComboBoxModel(
								sortByString(subredesSet.toArray())));
						
						listasubredes.setSelectedIndex(0);

					} catch (final Exception e) {
						e.printStackTrace();
					} catch (final Throwable e) {
						e.printStackTrace();
					}
				}
			}

		});

		listaredes.setModel(new DefaultComboBoxModel(sortByString(networkMgr
				.getNetworks().keySet().toArray())));

		listaredes.setPreferredSize(new Dimension(MAIN_COLUMN_WIDTH,
				(int) listaredes.getPreferredSize().getHeight()));

		return listaredes;

	}

	protected JTextField getFieldNombreenBase() {
		if (nombreenbaseField == null)
			nombreenbaseField = new JTextField();
		nombreenbaseField.setEditable(true);
		return nombreenbaseField;

	}

	public String getNombreenBase() {
		return nombreenbaseField.getText();
	}

	public String getRedSeleccionada() {
		return listaredes.getSelectedItem().toString();

	}

	public String getSubredseleccionada() {
		if (listasubredes != null){
			if (listasubredes.getSelectedItem() != null){
				return listasubredes.getSelectedItem().toString();
			}
		}
		return "";
	
	}
	
}
