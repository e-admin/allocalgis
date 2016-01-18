package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
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

	protected JComboBox getDatalistComboBox() throws Throwable {
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
	private ArrayList<String> datasetNames() throws Exception {

		listadesubredesenbase.add("Actualiza la lista");
		return listadesubredesenbase;

	}

	private void initialize() {

		try {
			addRow("Subredes en Base: ", getDatalistComboBox(), null, false);
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

	public void actualizarlista() throws Throwable, Exception, SQLException,
			NamingException {
		listaredesComboBox.removeAllItems();

		System.out.println("inserto en la lista las subredes de la base");
		final ArrayList<String> listadesubredesenbase = new ArrayList<String>();
		final DBRouteEngineReaderWriter db = new DBRouteEngineReaderWriter(this);
		Connection conn = db.getConnection();
		String sql = " SELECT network_name FROM networks ";
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			String subred = rs.getString("network_name");
			listadesubredesenbase.add(subred);
		}
		listaredesComboBox.setModel(new DefaultComboBoxModel(
				sortByString(listadesubredesenbase.toArray())));
		rs.close();
		st.close();
		conn.close();

	}

	public String getSubredSelected() {

		return listaredesComboBox.getSelectedItem().toString();

	}

	public void actionPerformed(ActionEvent e) {
//		if (e.equals(getConnectionComboBox())) {
//			System.out.println("cambio");
//		}

	}

}
