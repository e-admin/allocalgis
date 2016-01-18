package com.localgis.webservices.civilwork.model.dao.postgres.warnings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.types.DocumentTypes;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISGenericDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;

public class PostgresDAO {
	private static Logger logger = Logger.getLogger(PostgresDAO.class);
	
	
	public Integer getNextIdDocumentSequence(Connection connection) throws SQLException {
		String sqlQuery = "SELECT NEXTVAL('civil_work_id_document_sequence')";
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

		String sqlQuery = "SELECT NEXTVAL('civil_work_id_warning_sequence')";
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
		String sqlQuery = "INSERT INTO civil_work_documents(id_warning,id_document,document_type,document_extension,document_name,document_file) " +
		  "VALUES(?,?,?,?,?,?)";
		if(logger.isDebugEnabled())logger.debug("query insert warning document = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			preparedStatement.setInt(2,document.getIdDocumento());
			preparedStatement.setString(3, document.getTipo());
			preparedStatement.setString(4, document.getExtension());
			preparedStatement.setString(5, document.getNombre());
			preparedStatement.setBytes(6, document.getFichero());
			if(logger.isDebugEnabled())logger.debug("Inserting new Document : idWarning = " + idWarning + " idDocumento = " + document.getIdDocumento() + " nombre = " + document.getNombre() + " tipo = " + document.getTipo() + " extension = " + document.getExtension());
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	protected void addWarningDocumentThumbnail(Connection connection, Document document) throws SQLException{
		String sqlQuery = "INSERT INTO civil_work_document_thumbnail(id_document,document_thumbnail) " +
		  "VALUES(?,?)";
		if(logger.isDebugEnabled())logger.debug("query insert warning document thumbnail = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,document.getIdDocumento());
			byte[] thumbnail = LocalGISGenericDAO.getDocumentThumbnail(document);
			preparedStatement.setBytes(2, thumbnail);
			if(logger.isDebugEnabled())logger.debug("Inserting new Document Thumbnail : idDocument = " + document.getIdDocumento());
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	/*protected void buildOrderBy(String query, ArrayList<WarningsOrderColumns> orderedColumns){
		if(orderedColumns != null && orderedColumns.size() > 0){
			String orderByQuery = " ORDER BY ";
			Iterator<WarningsOrderColumns> orderByColumnsIterator = orderedColumns.iterator();
			while(orderByColumnsIterator.hasNext()){
				orderByQuery += orderByColumnsIterator.next().toString();
				if(orderByColumnsIterator.hasNext())
					orderByQuery += ",";
			}
		}else{
			query += " ORDER BY next_warning ASC";
		}
	}*/
	protected String buildQuery(String sqlQuery, ArrayList<String> conditions) {
		if(conditions != null && conditions.size() > 0){
			Iterator<String> condition = conditions.iterator();
			while (condition.hasNext()){
				sqlQuery += " AND " + condition.next();
			}
		}
		return sqlQuery;
	}
	protected void buildStatement(PreparedStatement preparedStatement,Hashtable<Integer, Object> preparedStatementSets) throws SQLException {
		int i = 1;
		while(preparedStatementSets.get(new Integer(i)) != null){
			if(preparedStatementSets.get(new Integer(i)) instanceof String)
				preparedStatement.setString(i, (String)preparedStatementSets.get(new Integer(i)));
			if(preparedStatementSets.get(new Integer(i)) instanceof Integer)
				preparedStatement.setInt(i, (Integer)preparedStatementSets.get(new Integer(i)));
			if(preparedStatementSets.get(new Integer(i)) instanceof Date)
				preparedStatement.setDate(i, new java.sql.Date(((Date)preparedStatementSets.get(new Integer(i))).getTime()));
			if(preparedStatementSets.get(new Integer(i)) instanceof Double)
				preparedStatement.setDouble(i, (Double)preparedStatementSets.get(new Integer(i)));
			i++;
		}
		
	}
	protected GregorianCalendar getCalendar(java.sql.Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	public Document getWarningDocument(Connection connection, Integer idDocument,
			Boolean returnCompleteDocuments) throws SQLException {
			Document document = null;
			String sqlQuery = "SELECT * FROM civil_work_documents WHERE id_document = ?";
			if(logger.isDebugEnabled())logger.debug("get Document from database = " + sqlQuery);
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try{
				if(logger.isDebugEnabled())logger.debug("Starting Statement");
				preparedStatement = connection.prepareStatement(sqlQuery);
				preparedStatement.setInt(1, idDocument);
				if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
				rs = preparedStatement.executeQuery();
				while (rs.next()){
					String nombreFichero = rs.getString("document_name");
					DocumentTypes tipo = getTipo(rs.getString("document_type"));
					String extension = rs.getString("document_extension");
					byte[] fichero = rs.getBytes("document_file");
					document = new Document(nombreFichero,tipo,extension,fichero);
					if(logger.isDebugEnabled())logger.debug("Getting Document from database : idDocumento = " + document.getIdDocumento() + " nombre = " + document.getNombre() + " tipo = " + document.getTipo() + " extension = " + document.getExtension());
			    }
			}finally{
				if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
				ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			}
			return document;
		
	}
	private DocumentTypes getTipo(String tipo) {
		DocumentTypes tipos = null;
		if(DocumentTypes.IMAGEN.toString().equals(tipo))
			tipos = DocumentTypes.IMAGEN;
		if(DocumentTypes.DOC.toString().equals(tipo))
			tipos = DocumentTypes.DOC;
		if(DocumentTypes.PDF.toString().equals(tipo))
			tipos = DocumentTypes.PDF;
		if(DocumentTypes.TXT.toString().equals(tipo))
			tipos = DocumentTypes.TXT;
		return tipos;
	}
	public ArrayList<Document> getWarningDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments) throws SQLException {
		ArrayList<Document> documents = new ArrayList<Document>();
		String sqlQuery = "SELECT * FROM civil_work_documents WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("query get documents = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idWarning);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				String nombreFichero = rs.getString("document_name");
				DocumentTypes tipo = getTipo(rs.getString("document_type"));
				String extension = rs.getString("document_extension");
				byte[] fichero = null;
				if(returnCompleteDocuments){
					fichero = rs.getBytes("document_file");
				}
				if (nombreFichero == null){
					nombreFichero = "";
				}
				if (tipo == null){
					tipo = DocumentTypes.DOC;
				}
				if (extension == null){
					extension = "";
				}
				
				if(logger.isDebugEnabled())logger.debug("Building Document : name = " + nombreFichero + " type = " + tipo.toString() + " extension = " + extension + " completeDocument = " + returnCompleteDocuments );
				Document document = new Document(nombreFichero,tipo,extension,fichero);
				documents.add(document);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return documents;
	}
	public void getWarningDocumentThumbnail(
			Connection connection, Document document) throws SQLException {
		String sqlQuery = "SELECT * FROM civil_work_document_thumbnail WHERE id_document = ?";
		if(logger.isDebugEnabled())logger.debug("get thumbnail from database = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, document.getIdDocumento());
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				byte[] thumbnail = rs.getBytes("document_thumbnail");
				document.setThumbnail(thumbnail);
				if(logger.isDebugEnabled())logger.debug("thumbnail returned");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
	}
}
