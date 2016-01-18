/**
 * OracleDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.oracle.warnings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;

public class OracleDAO {
	private static Logger logger = Logger.getLogger(OracleDAO.class);
	
	
	public Integer getNextIdDocumentSequence(Connection connection) throws SQLException {
		String sqlQuery = "SELECT seq_civil_work_documents.NEXTVAL AS NEXTVAL FROM DUAL";
		if(logger.isDebugEnabled())logger.debug("query Document next value = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idDocument = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idDocument = new Integer(rs.getInt(1));
				if(logger.isDebugEnabled())logger.debug("Next value Document = " + idDocument);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idDocument;
	}
	public Integer getNextIdWarningSequence(Connection connection) throws SQLException {

		String sqlQuery = "SELECT seq_civil_work_warnings.NEXTVAL AS NEXTVAL FROM DUAL";
		if(logger.isDebugEnabled())logger.debug("query Warning next value = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idWarning = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idWarning = new Integer(rs.getInt(1));
				if(logger.isDebugEnabled())logger.debug("Next value warning = " + idWarning);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idWarning;
	}
	protected void addWarningDocument(Connection connection, Integer idWarning,Document document) throws SQLException{
		
		throw new NotImplementedException();
		
	}
	protected void addWarningDocumentThumbnail(Connection connection, Document document) throws SQLException{
		throw new NotImplementedException();
		
	}

	public Document getWarningDocument(Connection connection, Integer idDocument,
			Boolean returnCompleteDocuments) throws SQLException {
		throw new NotImplementedException();
		
	}
	
	public ArrayList<Document> getWarningDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments) throws SQLException {
		throw new NotImplementedException();
	}
	public void getWarningDocumentThumbnail(
			Connection connection, Document document) throws SQLException {
		throw new NotImplementedException();
	}
}
