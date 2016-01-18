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
		String sqlQuery = "SELECT civil_work_id_document_sequence.NEXTVAL AS NEXTVAL FROM DUAL";
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

		String sqlQuery = "SELECT civil_work_id_warning_sequence.NEXTVAL AS NEXTVAL FROM DUAL";
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
