package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.swing.DefaultComboBoxModel;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.WorkbenchContext;

public class PanelToLoadFromDataLocalStore extends PanelToLoadFromDataStore {

	/**
	 * 
	 */

	// dummy constructor for JBuilder - do not use!!!
	public PanelToLoadFromDataLocalStore() {
		super(null);
	}

	public PanelToLoadFromDataLocalStore(final WorkbenchContext context) {
		super(context);
//		superinitialize();
	}


	@Override
	public void actualizarlista() throws Throwable, Exception, SQLException,
			NamingException {
		getDatalistComboBox().removeAllItems();

		System.out.println("inserto en la lista las subredes de la base");
		final ArrayList<String> listadesubredesenbase = new ArrayList<String>();
//		final DBRouteEngineReaderWriter db = new DBRouteEngineReaderWriter(this);
//		Connection conn = db.getConnection();
//		String sql = " SELECT network_name FROM networks ";
//		PreparedStatement st = conn.prepareStatement(sql);
//		ResultSet rs = st.executeQuery();
		
		String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
		File dir = new File(base,"networks");
		if(! dir.exists() ){
			dir.mkdirs();
		}
		
		if (dir.isDirectory()){
			File[] l = dir.listFiles();
			for (int i=0; i < l.length; i++){
				if (l[i].isDirectory()){
					String subred = l[i].getName();
					listadesubredesenbase.add(subred);
				}
			}
			
		}

//		while (rs.next()) {
//			String subred = rs.getString("nombre");
//			listadesubredesenbase.add(subred);
//		}
		
		getDatalistComboBox().setModel(new DefaultComboBoxModel(
				sortByString(listadesubredesenbase.toArray())));

	}


}
