package com.geopista.ui.plugin.external;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import nickyb.sqleonardo.querybuilder.QueryModel;

public class DesignQueryHelper{
	

	
	public DesignQueryHelper() {

	}


	public boolean isQueryModelEmpty(QueryModel queryModel) {
		
		return queryModel.toString().indexOf("FROM")<0;
	}

	public boolean isQueryModelCorrect(QueryModel queryModel, Connection connection) {
		try {
			Statement statement = connection.createStatement();
			statement.executeQuery(queryModel.toString());
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		

	}
}
