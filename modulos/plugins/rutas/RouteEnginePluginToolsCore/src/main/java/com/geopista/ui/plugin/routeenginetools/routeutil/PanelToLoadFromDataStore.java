/**
 * PanelToLoadFromDataStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;

public class PanelToLoadFromDataStore extends WithOutConnectionPanel implements
		ActionListener {

	/**
	 * 
	 */
	private JComboBox listaredesComboBox = null;
	ArrayList<String> listadesubredesenbase = new ArrayList<String>();

	// dummy constructor for JBuilder - do not use!!!
	public PanelToLoadFromDataStore() {
		super(null);
	}

	public PanelToLoadFromDataStore(final WorkbenchContext context) {
		super(context);

		initialize();

	}

	public String getDatasetName() {
		return listaredesComboBox.getSelectedItem() != null ? ((String) listaredesComboBox
				.getSelectedItem()).trim()
				: null;
	}

	protected JComboBox getDatalistComboBox()
	{
		if (listaredesComboBox == null) {
			listaredesComboBox = new JComboBox();
			listaredesComboBox.setModel(new DefaultComboBoxModel(
					sortByString(datasetNames().toArray())));
			listaredesComboBox.setPreferredSize(new Dimension(
					MAIN_COLUMN_WIDTH, (int) listaredesComboBox
							.getPreferredSize().getHeight()));
			listaredesComboBox.setAutoscrolls(true);
			listaredesComboBox.setEditable(false);

		}
		return listaredesComboBox;
	}

	// Pone el valor inicial al combox
	protected ArrayList<String> datasetNames() 
	{
		listadesubredesenbase.add("Actualiza la lista");
		return listadesubredesenbase;
	}

	private void initialize() {

		try {
			addRow("Subredes en disponibles: ", getDatalistComboBox(), null, false);
			actualizarlista();
//			getConnectionComboBox().addItemListener(new ItemListener() {
//
//				public void itemStateChanged(final ItemEvent arg0) {
//					if (arg0.getStateChange() == ItemEvent.SELECTED) {
//						try {
//							actualizarlista();
//						} catch (final SQLException e) {
//
//							e.printStackTrace();
//						} catch (final NamingException e) {
//
//							e.printStackTrace();
//						} catch (final Exception e) {
//
//							e.printStackTrace();
//						} catch (final Throwable e) {
//
//							e.printStackTrace();
//						}
//					}
//				}
//
//			});
		} catch (final Throwable e) {

			e.printStackTrace();
		}

	}

	public void actualizarlista() throws  SQLException 
    {
	listaredesComboBox.removeAllItems();

	System.out.println("inserto en la lista las subredes de la base");
	final ArrayList<String> listadesubredesenbase = new ArrayList<String>();
	final DBRouteEngineReaderWriter db = new DBRouteEngineReaderWriter(this);
	Connection conn = db.getConnection();
	if (conn != null)
	    {
		String sql = " SELECT network_name FROM networks ";
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();

		while (rs.next())
		    {
			String subred = rs.getString("network_name");
			listadesubredesenbase.add(subred);
		    }
		listaredesComboBox.setModel(new DefaultComboBoxModel(sortByString(listadesubredesenbase.toArray())));
		rs.close();
		st.close();
		conn.close();
	    }

    }

	public String getSubredSelected() {

		return listaredesComboBox.getSelectedItem().toString().trim();

	}

	public void actionPerformed(ActionEvent e) {
//		if (e.equals(getConnectionComboBox())) {
//			System.out.println("cambio");
//		}

	}

}
