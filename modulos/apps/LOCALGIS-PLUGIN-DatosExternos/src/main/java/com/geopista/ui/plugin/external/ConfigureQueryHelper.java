/**
 * ConfigureQueryHelper.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QuerySpecification;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens.Column;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens._Expression;
import nickyb.sqleonardo.querybuilder.syntax.SQLParser;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class ConfigureQueryHelper {

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private static final String queryInternalData = "queryInternalData";
	
	public ConfigureQueryHelper() {
		super();

	}
	
	public Vector getQueries(String dataSourceName) {
		QueryDAO queryDAO = new QueryDAO();
		return queryDAO.listQueriesByDataSourceName(dataSourceName);
	}
	
	public Vector getDataSources() {
		ExternalDataSourceDAO externalDataSourceDAO = new ExternalDataSourceDAO();
		return externalDataSourceDAO.list();
	}
	

	public QueryModel designQuery(Dialog dialog,ExternalDataSource externalDataSource) {
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		if (connection!=null) {
			DesignQueryDialog designQueryDialog = new DesignQueryDialog(dialog, I18N.get("configure.fields"), true,connection);
			GUIUtil.centreOnWindow(designQueryDialog);
			designQueryDialog.setVisible(true);
			return designQueryDialog.getQueryModel();
		}
		return null;
	}
	
	public QueryModel designQuery(Dialog dialog,ExternalDataSource externalDataSource,final QueryModel queryModel) {
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		if (connection!=null) {
			
			final DesignQueryDialog designQueryDialog = new DesignQueryDialog(dialog, I18N.get("configure.fields"), true,connection);
			GUIUtil.centreOnWindow(designQueryDialog);
			Thread thread = new Thread(new Runnable() {

				public void run() {
					while (!designQueryDialog.isVisible()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					designQueryDialog.setOldQueryModel(queryModel);	
				}
				
			});
			thread.start();
			designQueryDialog.setVisible(true);
			QueryModel model = designQueryDialog.getQueryModel();
			return model;
		}
		return null;
	}
	
	private Hashtable getExternalMetaData(QueryModel queryModel) {
		 QueryExpression queryExpression = queryModel.getQueryExpression();
		 QuerySpecification querySpecification = queryExpression.getQuerySpecification();
		 _Expression[] expressions = querySpecification.getSelectList();
		 Hashtable hashtable = new Hashtable();
		 for (int i = 0; i < expressions.length; i++) {
			_Expression expression = expressions[i];
			if (expression instanceof QueryTokens.Column) {
				QueryTokens.Column column = (Column) expression;
				String tableIdentifier = column.getTable().getAlias();
				String columnName = column.getName();
				if (!hashtable.containsKey(tableIdentifier)) {
					Vector vector = new Vector();
					vector.add(null);
					vector.add(columnName);
					hashtable.put(tableIdentifier, vector);
				}
				else {
					Vector vector = (Vector) hashtable.get(tableIdentifier);
					vector.add(columnName);					
				}
			}			
		 }
		 if (hashtable.size() != 0){
			 return hashtable;
		 }
		 else {
			 return null;
		 }			 
	}

	private TreeMap getInternalMetaData() {
		try {
			
			Connection connection = aplicacion.getConnection(); 
			PreparedStatement statement = connection.prepareStatement(queryInternalData);
			ResultSet resultSet = statement.executeQuery();
			String oldTableName = "";
			TreeMap hashtable = new TreeMap();
			Vector v = null;
			boolean first = true;
			while (resultSet.next()) {
				
				String tableName = resultSet.getString(1);
				if (first||!tableName.equals(oldTableName)) {
					v = new Vector();
					hashtable.put(tableName, v);
					v.add(null);
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

	public Query configureFields(Dialog dialog,QueryModel queryModel, QueryListItem item) {
		
       	ConfigureFieldsDialog configureFields = new ConfigureFieldsDialog(dialog, I18N.get("configure.fields"), true);
       	configureFields.setModal(true);
       	configureFields.setExternalData(getExternalMetaData(queryModel));
       	configureFields.setExternalModels();
       	configureFields.setInternalData(getInternalMetaData());
       	configureFields.setInternalModels();
       	

       	if (item!=null && item.getQuery()!=null && item.getQuery().getInternalLayer()!=null){
       		configureFields.setInternalDataTablejComboBox(item.getQuery().getInternalLayer());
       	}
       	
       	if (item!=null && item.getQuery()!=null && item.getQuery().getInternalAttribute()!=null){
       		if (configureFields.isInternalDataTablejComboBoxSelected()){
       			configureFields.setInternalDataColumnjCombobBox(item.getQuery().getInternalAttribute());
       		}
       	}
       	
       	if (item!=null && item.getQuery()!=null && item.getQuery().getExternalTable()!=null){
       		configureFields.setExternalDataTablejCombobox(item.getQuery().getExternalTable());
       	}
       	
       	if (item!=null && item.getQuery()!=null && item.getQuery().getExternalColumn()!=null){
       		if (configureFields.isExternalDataTablejComboBoxSelected()){
       			configureFields.setExternalDataColumnjComboBox(item.getQuery().getExternalColumn());
       		}
       	}
       	
       	
       	
       	GUIUtil.centreOnWindow(configureFields);
       	configureFields.setVisible(true);
       	if (configureFields.isClosed()) {
       		return null;
       	}
       	else {
           	Query query = new Query();
           	query.setInternalLayer(configureFields.getInternalLayerName());
           	query.setInternalAttribute(configureFields.getInternalAttributeName());
           	query.setExternalTable(configureFields.getExternalTableName());
           	query.setExternalColumn(configureFields.getExternalColumnName());
           	query.setText(queryModel.toString());
           	return query;  
       	}
	}

	public void insertQuery(Query queryToInsert,String dataSourceName) {
		if (validateQuery(queryToInsert)) {
			QueryDAO queryDAO = new QueryDAO();
			queryDAO.insertQuery(queryToInsert, dataSourceName);
		}
	}

	public void updateQuery(Query queryToUpdate) {
		if (validateQuery(queryToUpdate)) {
			QueryDAO queryDAO = new QueryDAO();
			queryDAO.updateQuery(queryToUpdate);
		}
		
	}

	public QueryModel buildQueryModel(String text) {
		
		try {
			return SQLParser.toQueryModel(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean validateQuery(Query query) {
		if (((query.getInternalLayer()!=null)&&(!query.getInternalLayer().equals("")))
			&& ((query.getInternalAttribute()!=null)&&(!query.getInternalAttribute().equals("")))
			&& ((query.getExternalTable()!=null)&&(!query.getExternalTable().equals("")))
			&& ((query.getExternalColumn()!=null)&&(!query.getExternalColumn().equals("")))
			&& ((query.getText()!=null)&&(!query.getText().equals("")))
			&& ((query.getName()!=null)&&(!query.getName().equals("")))) {
				return true;
			}
		else {
			return false;
		}
	}
}
