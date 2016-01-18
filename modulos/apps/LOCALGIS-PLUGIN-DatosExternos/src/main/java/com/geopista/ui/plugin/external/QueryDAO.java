/**
 * QueryDAO.java
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
import java.util.Vector;

import com.geopista.app.AppContext;

public class QueryDAO {

	
	private final static String dataSourceIdColumnName = "id_datasource";
	private final static String queryIdColumnName = "id_query";
	private final static String nameColumnName = "nombre";
	private final static String textColumnName = "query";
	private final static String internalLayerColumnName = "campo_interno_capa";	
	private final static String internalAttributeColumnName =  "campo_interno_atributo";
	private final static String externalTableColumnName = "campo_externo_tabla";
	private final static String externalColumnColumnName = "campo_externo_columna";	

	private final static String migration = "";
	private final static String listQueriesByDataSourceNameQuery = "listQueriesByDataSourceNameQuery" + migration; 
	private final static String listQueriesByDataSourceIdQuery = "listQueriesByDataSourceIdQuery" + migration;
	private final static String insertQueryQuery = "insertQueryQuery" + migration;
	
	
	private final static String findQueryQuery = "findQueryQuery" + migration;
	private final static String deleteQueryQuery = "deleteQueryQuery" + migration;
	private final static String updateQueryQuery= "updateQueryQuery" + migration;
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private final static String nextValQuery = "nextValQueryQueryPostgres" + migration;
	
	private Connection getConnection() {
		try {
			Connection connection = aplicacion.getConnection(); 
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public QueryDAO() {

	}
	
	public Vector listQueriesByDataSourceName(String dataSourceName) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(listQueriesByDataSourceNameQuery);
			preparedStatement.setString(1, dataSourceName);
			preparedStatement.setInt(2, AppContext.getIdMunicipio());
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector vector = new Vector();
			while (resultSet.next()) {
				int dataSourceId = resultSet.getInt(dataSourceIdColumnName);
				int queryId= resultSet.getInt(queryIdColumnName);
				String name = resultSet.getString(nameColumnName);
				String text = resultSet.getString(textColumnName);
				String internalLayer = resultSet.getString(internalLayerColumnName);	
				String internalAttribute =  resultSet.getString(internalAttributeColumnName);
				String externalTable = resultSet.getString(externalTableColumnName);
				String externalColumn = resultSet.getString(externalColumnColumnName);	

				Query query = new Query();
				query.setDataSourceId(dataSourceId);
				query.setQueryId(queryId);
				query.setName(name);
				query.setText(text);
				query.setInternalLayer(internalLayer);
				query.setInternalAttribute(internalAttribute);
				query.setExternalTable(externalTable);
				query.setExternalColumn(externalColumn);
				vector.add(query);
			}
			return vector;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public Vector listQueriesByDataSourceId(int id) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(listQueriesByDataSourceIdQuery);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector vector = new Vector();
			while (resultSet.next()) {
				int dataSourceId = resultSet.getInt(dataSourceIdColumnName);
				int queryId= resultSet.getInt(queryIdColumnName);
				String name = resultSet.getString(nameColumnName);
				String text = resultSet.getString(textColumnName);
				String internalLayer = resultSet.getString(internalLayerColumnName);	
				String internalAttribute =  resultSet.getString(internalAttributeColumnName);
				String externalTable = resultSet.getString(externalTableColumnName);
				String externalColumn = resultSet.getString(externalColumnColumnName);	
				Query query = new Query();
				query.setDataSourceId(dataSourceId);
				query.setQueryId(queryId);
				query.setName(name);
				query.setText(text);
				query.setInternalLayer(internalLayer);
				query.setInternalAttribute(internalAttribute);
				query.setExternalTable(externalTable);
				query.setExternalColumn(externalColumn);
				vector.add(query);
			}
			return vector;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public void insertQuery(Query query,int dataSourceId) {
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(nextValQuery);
			ResultSet resultSet = statement.executeQuery();
			int nextval = 0;
			if (resultSet.next()) {
				nextval = resultSet.getInt(1);
				nextval++;
				PreparedStatement preparedStatement = connection.prepareStatement(insertQueryQuery);
				preparedStatement.setInt(1, nextval);
				preparedStatement.setInt(2, dataSourceId);
				preparedStatement.setString(3, query.getName());
				preparedStatement.setString(4, query.getText());			
				preparedStatement.setString(5, query.getInternalLayer());
				preparedStatement.setString(6, query.getInternalAttribute());
				preparedStatement.setString(7, query.getExternalTable());
				preparedStatement.setString(8, query.getExternalColumn());
				preparedStatement.execute();	
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public void insertQuery(Query query,String dataSourceName) {
		ExternalDataSourceDAO externalDataSourceDAO = new ExternalDataSourceDAO();
		ExternalDataSource externalDataSource = externalDataSourceDAO.find(dataSourceName);
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(nextValQuery);
			ResultSet resultSet = statement.executeQuery();
			int nextval = 0;
			if (resultSet.next()) {
				nextval = resultSet.getInt(1);
				nextval++;
				PreparedStatement preparedStatement = connection.prepareStatement(insertQueryQuery);
				preparedStatement.setInt(1,nextval);
				preparedStatement.setInt(2, externalDataSource.getId());
				preparedStatement.setString(3, query.getName());
				preparedStatement.setString(4, query.getText());			
				preparedStatement.setString(5, query.getInternalLayer());
				preparedStatement.setString(6, query.getInternalAttribute());
				preparedStatement.setString(7, query.getExternalTable());
				preparedStatement.setString(8, query.getExternalColumn());
				preparedStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}		
	}
	
	public void deleteQuery(int queryId) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQueryQuery);
			preparedStatement.setInt(1, queryId);
			preparedStatement.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public Query findQuery(String queryName,String dataSourceName) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(findQueryQuery);
			preparedStatement.setString(1, queryName);
			preparedStatement.setString(2, dataSourceName);
			preparedStatement.setInt(3, AppContext.getIdMunicipio());

			ResultSet resultSet = preparedStatement.executeQuery();	
			if (resultSet.next()) {
				int dataSourceId = resultSet.getInt(dataSourceIdColumnName);
				int queryId= resultSet.getInt(queryIdColumnName);
				String name = resultSet.getString(nameColumnName);
				String text = resultSet.getString(textColumnName);
				String internalLayer = resultSet.getString(internalLayerColumnName);	
				String internalAttribute =  resultSet.getString(internalAttributeColumnName);
				String externalTable = resultSet.getString(externalTableColumnName);
				String externalColumn = resultSet.getString(externalColumnColumnName);	

				Query query = new Query();
				query.setDataSourceId(dataSourceId);
				query.setQueryId(queryId);
				query.setName(name);
				query.setText(text);
				query.setInternalLayer(internalLayer);
				query.setInternalAttribute(internalAttribute);
				query.setExternalTable(externalTable);
				query.setExternalColumn(externalColumn);
				return query;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
		return null;
	}
	
	public void updateQuery(Query query) {
		
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateQueryQuery);
			preparedStatement.setString(1, query.getName());
			preparedStatement.setString(2, query.getText());
			preparedStatement.setString(3, query.getInternalLayer());
			preparedStatement.setString(4, query.getInternalAttribute());
			preparedStatement.setString(5, query.getExternalTable());
			preparedStatement.setString(6, query.getExternalColumn());
			preparedStatement.setInt(7, query.getQueryId());
			preparedStatement.executeUpdate();	

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public static void main(String[] args) {
		QueryDAO queryDAO = new QueryDAO();
		Query query = new Query();
		
		query.setName("Oracle");
		query.setText("Adolfo");
		query.setInternalLayer("internalLayer");
		query.setInternalAttribute("internalAttribute");
		query.setExternalTable("externalTable");
		query.setExternalColumn("externalColumn");
		String dataSourceName = "Adolfo";
		queryDAO.insertQuery(query, dataSourceName);
		Vector v = queryDAO.listQueriesByDataSourceName(dataSourceName);
		//System.out.println(v.size());
		query = queryDAO.findQuery("Oracle", dataSourceName);
		query.setInternalLayer("Adolfo");
		query.setInternalAttribute("Pedro");
		query.setText("2");
		queryDAO.updateQuery(query);
	}	 
}
