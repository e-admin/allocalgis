/**
 * LayerInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.protocol.control.ISesion;

public class LayerInfo {
	
	private static final String selectLayerQueryName = "selectLayerQuery";
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private String name;
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public LayerInfo(String name) {
		setName(name);
	}




	public Hashtable getQueries() {
		Connection connection;
		try {
			ISesion iSesion = (ISesion)aplicacion.getBlackboard().get(UserPreferenceConstants.SESION_KEY);
			connection = aplicacion.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectLayerQueryName);
			preparedStatement.setString(1, getName());
	        preparedStatement.setInt(2, AppContext.getIdMunicipio());
			ResultSet resultSet = preparedStatement.executeQuery();
			String oldTableName = "";
			Hashtable hashtable = new Hashtable();
			Vector v = null;
			boolean first = true;
			while (resultSet.next()) {				
				String tableName = resultSet.getString(1);
				if (first||!tableName.equals(oldTableName)) {
					v = new Vector();
					hashtable.put(tableName, v);
					first = false;
				}
				String fieldName = resultSet.getString(2);
				v.add(fieldName);
				oldTableName = tableName;
			}
			return hashtable;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
