/**
 * PanelToLoadFromDataLocalStore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

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
	public void actualizarlista() throws SQLException
	{
		getDatalistComboBox().removeAllItems();

		System.out.println("inserto en la lista las subredes del disco Local");
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

		getDatalistComboBox().setModel(new DefaultComboBoxModel(
				sortByString(listadesubredesenbase.toArray())));

	}


}
