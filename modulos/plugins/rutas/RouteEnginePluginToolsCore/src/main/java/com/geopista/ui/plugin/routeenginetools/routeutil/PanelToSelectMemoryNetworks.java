/**
 * PanelToSelectMemoryNetworks.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;

import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PanelToSelectMemoryNetworks extends WithOutConnectionPanel implements ActionListener
{
    protected JComboBox comboSubredes, listaredes;
	protected final PlugInContext context;

	public PanelToSelectMemoryNetworks(final PlugInContext context) {
		super(context.getWorkbenchContext());// se coloca el combo de la
		// conexion
		this.context = context;
		initialize();

	}

	protected void initialize() {
	    	
	    	this.setBorder(BorderFactory.createTitledBorder
			(null, "Seleccione una red de memoria.",TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
		addRow("Redes disponibles", getRedesComboBox(), null, false);
		addRow("Lista de subredes", getSubredesComboBox(), null, false);
	}

	public void actionPerformed(final ActionEvent e) {
		final Object obj = e.getSource();// getSource recoge quï¿½ objeto fue
		// pulsado
		// cogemos el nombre de la red que es seleccionado
		if (obj.equals(listaredes)) {
			final NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
			@SuppressWarnings("unused")
			final Set<String> subredesSet = (Set<String>) networkMgr.getNetwork(listaredes.getSelectedItem().toString()).getSubnetworks().keySet();

		}
	}

	protected JComboBox getSubredesComboBox() {
		if (comboSubredes == null)
			comboSubredes = new JComboBox();
		final NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		
		
		if (listaredes.getSelectedItem() != null)
		    {
			Set<String> subredesSet = networkMgr.getNetwork(listaredes
					.getSelectedItem().toString()).getSubnetworks().keySet();// obtener lista de redes ya
			Object[] array2 = subredesSet.toArray();
			Object[] sortedArray = sortByString(array2);
			Object[] array = sortedArray;
			Vector<Object> listaSubredes =  new Vector(Arrays.asList(array));
			listaSubredes.add(0, "");
			
			comboSubredes.setModel(new DefaultComboBoxModel(listaSubredes));
		}
		// calculadas
		

		comboSubredes.setPreferredSize(new Dimension(MAIN_COLUMN_WIDTH/2,
				(int) comboSubredes.getPreferredSize().getHeight()));
		comboSubredes.setEditable(false);

		return comboSubredes;
	}

	protected JComboBox getRedesComboBox() {
		if (listaredes == null)
			listaredes = new JComboBox();
		final NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		listaredes.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					try {

						final Set<String> subredesSet = networkMgr
								.getNetwork(listaredes.getSelectedItem()
										.toString()).getSubnetworks().keySet();// obtener lista de redes
						// ya calculadas
						ArrayList<String> opciones=new ArrayList<String>();
						opciones.add(0,"");
						opciones.addAll(subredesSet);
						Object[] subredesArray = opciones.toArray();
						comboSubredes.setModel(new DefaultComboBoxModel(sortByString(subredesArray)));
						if (comboSubredes.getModel().getSize()>0)
						    comboSubredes.setSelectedIndex(0);

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
		listaredes.setMinimumSize(new Dimension(MAIN_COLUMN_WIDTH/2,
				(int) listaredes.getPreferredSize().getHeight()));
		listaredes.setPreferredSize(new Dimension(MAIN_COLUMN_WIDTH/2,
				(int) listaredes.getPreferredSize().getHeight()));

		return listaredes;

	}
/**
 * Devuelve "" si no se ha seleccionado nada
 * @return
 * @deprecated
 */
	public String getRedSeleccionada() {
		Object selectedItem = listaredes.getSelectedItem();
		return selectedItem==null?"":selectedItem.toString().trim();

	}
	/**
	 * Devuelve la red seleccionada en el campo subred o en el de red por ese orden
	 * @return
	 */
	public Network getSelectedNetwork()
	{
	    final NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
	    Object subred= comboSubredes.getSelectedItem();
	   if (subred!=null) subred=networkMgr.getNetwork((String)subred); // TODO usar directamente Network en el modelo del combo
	    if (subred!=null && subred instanceof Network)
		return (Network)subred;
	    Object red= listaredes.getSelectedItem();
	   if(red!=null) red=networkMgr.getNetwork((String)red); // TODO usar directamente Network en el modelo
	    if (red!=null && red instanceof Network)
		return (Network)red;
	    return null;
	}
	/**
	 * Devuelve "" si no se ha seleccionado nada
	 * @return
	 * @deprecated use {@link #getSelectedNetwork()}
	 */
	public String getSubredseleccionada() {
		if (comboSubredes != null){
			if (comboSubredes.getSelectedItem() != null){
				return comboSubredes.getSelectedItem().toString().trim();
			}
		}
		return "";
	
	}
	/**
	 * Intenta marcar como seleccionado el elemento en uno u otro campo.
	 * @param networkname
	 */
	public void setSelected(String networkname)
	 {
	 listaredes.setSelectedItem(networkname);
	 comboSubredes.setSelectedItem(networkname);
	 }
}
