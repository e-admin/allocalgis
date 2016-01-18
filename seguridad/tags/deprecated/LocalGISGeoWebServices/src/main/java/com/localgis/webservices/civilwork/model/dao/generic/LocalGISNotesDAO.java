package com.localgis.webservices.civilwork.model.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.webservices.civilwork.model.dao.ILocalGISNotesDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISNotesDAO implements ILocalGISNotesDAO{
	
	private static Logger logger = Logger.getLogger(LocalGISNotesDAO.class);
	
	@Override
	public void addNote(Connection connection, LocalGISNote warning) throws SQLException {
		
		logger.debug("Adding note");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addWarning(connection, warning);
		
	}
	
	@Override
	public void addNoteLayerFeatureReferences(Connection connection,
			ArrayList<LayerFeatureBean> layerFeatureBean, Integer idWarning)
			throws SQLException {
		logger.debug("Adding note Layer Feature References");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addLayerFeatureReferences(connection, layerFeatureBean, idWarning);
		
	}
	
	@Override
	public void addNoteDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void addNoteDocument(Connection connection, Integer idWarning,
			Document document) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public LocalGISNote getNote(Connection connection, Integer idWarning,
			Integer idMunicipio, Integer idUser) throws SQLException, ConfigurationException {
		logger.debug("Obtaining note");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		return dao.getWarning(connection, idWarning, idMunicipio, idUser);
	}

	@Override
	public Document getNoteDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<Document> getNoteDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<LayerFeatureBean> getNoteLayerFeatureReferences(
			Connection connection, Integer idWarning) throws SQLException {
		logger.debug("Obtaining layer feature references");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		return dao.getLayerFeatureReferences(connection, idWarning);
	}

	@Override
	public void removeNote(Connection connection, Integer idWarning)
			throws SQLException {
		logger.debug("Deleting note");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarning(connection, idWarning);
		
	}

	@Override
	public void removeNoteDocument(Connection connection, Integer idDocument)
			throws SQLException {
		logger.debug("Deleting document");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeDocument(connection, idDocument);
		
	}

	@Override
	public void removeNoteDocuments(Connection connection, Integer idWarning)
			throws SQLException {
		logger.debug("Deleting all note documents");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarningDocuments(connection, idWarning);
		
	}

	@Override
	public void removeNoteLayerFeatureReferences(Connection connection,
			Integer idWarning) throws SQLException {
		logger.debug("Deleting feature references from note");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeLayerFeatureReferences(connection, idWarning);
		
	}

	@Override
	public void setNote(Connection connection, LocalGISNote note)
			throws SQLException {
		logger.debug("setting note");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.setWarning(connection, note);
		
	}

	@Override
	public ArrayList<LocalGISNote> getNoteList(Connection connection,
			String description, Calendar from, Calendar to,
			Integer startElement, Integer range, Integer idMunicipio,
			Integer userId, ArrayList<OrderByColumn> orderColumns,
			ArrayList<LayerFeatureBean> features) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public int getNumNotes(Connection connection, String description,
			Calendar from, Calendar to, Integer idMunicipio, Integer userId,
			ArrayList<LayerFeatureBean> features) throws SQLException {
		int numNotes = 0;
		String sqlQuery = 
			"SELECT " +
				"COUNT(DISTINCT(civil_work_warnings.id_warning)) AS NUM_NOTES " +
			"FROM " +
				"civil_work_warnings " +
			"LEFT JOIN civil_work_layer_feature_reference ON civil_work_warnings.id_warning = civil_work_layer_feature_reference.id_warning " +
			"WHERE ";
		Hashtable<Integer,Object> preparedStatementSets = new Hashtable<Integer,Object>();
		ArrayList<String> conditions = new ArrayList<String>();
		conditions.add("civil_work_warnings.id_type = ?");
		preparedStatementSets.put(new Integer(conditions.size()), WarningTypes.NOTE);
		conditions.add("civil_work_warnings.user_creator = ?");
		preparedStatementSets.put(new Integer(conditions.size()), userId);
		if(description != null && !description.equals("")){
			conditions.add("UPPER(TRANSLATE(description,'ÁÉÍÓÚáéíóú','AEIOUaeiou')) LIKE UPPER(TRANSLATE(?,'ÁÉÍÓÚáéíóú','AEIOUaeiou'))");
			preparedStatementSets.put(new Integer(conditions.size()), description);
		}
		if(from != null){
			conditions.add("start_warning >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), from);
		}	
		if(to != null){
			conditions.add("start_warning <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), to);
		}
		conditions.add("id_municipio = ?");
		preparedStatementSets.put(new Integer(conditions.size()), idMunicipio);
		sqlQuery = LocalGISGenericDAO.buildQuery(sqlQuery,conditions);
		
		sqlQuery = LocalGISGenericDAO.buildLayerFeatureReferenceQuery(sqlQuery,features);
		
		if(logger.isDebugEnabled())logger.debug("Query Warning List = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			LocalGISGenericDAO.buildStatement(preparedStatement,preparedStatementSets);
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			rs = preparedStatement.executeQuery();
	        if (rs.next()){
	        	numNotes = rs.getInt("NUM_NOTES");
		    }
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return numNotes;
	}

	

	@Override
	public Integer getNextIdDocumentNoteSequence(Connection connection)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public Integer getNextIdNoteSequence(Connection connection)
			throws SQLException {
		throw new NotImplementedException();
	}

}
