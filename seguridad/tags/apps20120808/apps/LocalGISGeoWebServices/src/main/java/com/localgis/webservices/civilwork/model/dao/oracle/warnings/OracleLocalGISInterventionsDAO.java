package com.localgis.webservices.civilwork.model.dao.oracle.warnings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class OracleLocalGISInterventionsDAO extends LocalGISInterventionsDAO{
	
	
	@Override
	public void addInterventionDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		pDAO.addWarningDocumentThumbnail(connection, document);
		
	}

	@Override
	public void addInterventionDocument(Connection connection,
			Integer idWarning, Document document)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		pDAO.addWarningDocument(connection, idWarning, document);
		
	}
	@Override
	public Document getInterventionDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getWarningDocument(connection, idDocument,returnCompleteDocuments);
	}

	@Override
	public ArrayList<Document> getInterventionDocuments(
			Connection connection, Integer idWarning,
			Boolean returnCompleteDocuments) throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getWarningDocuments(connection, idWarning,returnCompleteDocuments);
	}
	
	@Override
	public ArrayList<LocalGISIntervention> getInterventionList(
			Connection connection, String description, Calendar fromStart,
			Calendar toStart,Calendar fromNext,
			Calendar toNext, Integer startElement, Integer range,
			Integer idMunicipio, Integer userId,
			ArrayList<OrderByColumn> orderColumns,
			ArrayList<LayerFeatureBean> features, String actuationType,
			String interventionType, Double foreseenBudgetFrom, Double foreseenBudgetTo,
			Double workPercentageFrom,Double workPercentageTo, String causes,Boolean isAdministrator) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public Integer getNextIdDocumentInterventionSequence(Connection connection)
			throws SQLException {
		OracleDAO dao = new OracleDAO();
		return dao.getNextIdDocumentSequence(connection);
	}

	@Override
	public Integer getNextIdInterventionSequence(Connection connection)
			throws SQLException {
		OracleDAO pDAO = new OracleDAO();
		return pDAO.getNextIdWarningSequence(connection);
	}
}
