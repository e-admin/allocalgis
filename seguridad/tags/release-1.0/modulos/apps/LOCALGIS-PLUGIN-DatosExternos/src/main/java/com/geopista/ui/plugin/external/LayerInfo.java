package com.geopista.ui.plugin.external;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.geopista.app.AppContext;
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
			ISesion iSesion = (ISesion)aplicacion.getBlackboard().get(AppContext.SESION_KEY);
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
