/**
 * LocalGISInterventionsDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.webservices.civilwork.model.dao.ILocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.InterventionColumn;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISInterventionsDAO implements ILocalGISInterventionsDAO {

	private static Logger logger = Logger.getLogger(LocalGISInterventionsDAO.class);
	
	@Override
	public void addIntervention(Connection connection,
			LocalGISIntervention warning) throws SQLException {
		
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addWarning(connection, warning);
		
	}

	@Override
	public void addInterventionDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		throw new NotImplementedException();
		
	}

	@Override
	public void addInterventionDocument(Connection connection,
			Integer idWarning, Document documents)
			throws SQLException {
		throw new NotImplementedException();
		
	}

	@Override
	public void addInterventionLayerFeatureReferences(Connection connection,
			ArrayList<LayerFeatureBean> layerFeatureBean, Integer idWarning)
			throws SQLException {
		logger.debug("Adding intervention Layer Feature References");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addLayerFeatureReferences(connection, layerFeatureBean, idWarning);
		
	}

	@Override
	public LocalGISIntervention getIntervention(Connection connection,
			Integer idWarning, Integer idMunicipio, Integer idUser)
			throws SQLException, ConfigurationException {
		logger.debug("Obtaining note");
		LocalGISIntervention intervention = null;
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		LocalGISNote note = dao.getWarning(connection, idWarning, idMunicipio, idUser);
		if(note != null)
			intervention = new LocalGISIntervention(note);
		return intervention;
	}

	@Override
	public Document getInterventionDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<Document> getInterventionDocuments(
			Connection connection, Integer idWarning,
			Boolean returnCompleteDocuments) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<LayerFeatureBean> getInterventionLayerFeatureReferences(
			Connection connection, Integer idWarning) throws SQLException {
		logger.debug("getting intervention Layer Feature References");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		return dao.getLayerFeatureReferences(connection, idWarning);
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
		throw new NotImplementedException();
	}

	@Override
	public Integer getNextIdInterventionSequence(Connection connection)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public int getNumInterventions(Connection connection, String description,
			Calendar fromStart, Calendar toStart,Calendar fromNext, Calendar toNext, Integer idMunicipio, Integer userId,
			ArrayList<LayerFeatureBean> features, String actuationType,
			String interventionType, Double foreseenBudgetFrom,Double foreseenBudgetTo,
			Double workPercentageFrom,Double workPercentageTo, String causes,Boolean isAdministrator) throws SQLException {
		
		int numInterventions = 0;
		String sqlQuery = 
			"SELECT " +
				"COUNT(DISTINCT(civil_work_warnings.id_warning)) AS NUM_INTERVENTIONS " +
			"FROM " +
				"civil_work_warnings " +
			"LEFT JOIN civil_work_intervention ON civil_work_warnings.id_warning = civil_work_intervention.id_warning " +
			"LEFT JOIN civil_work_layer_feature_reference ON civil_work_warnings.id_warning = civil_work_layer_feature_reference.id_warning " +
			"WHERE ";
		Hashtable<Integer,Object> preparedStatementSets = new Hashtable<Integer,Object>();
		ArrayList<String> conditions = new ArrayList<String>();
		conditions.add("civil_work_warnings.id_type = ?");
		preparedStatementSets.put(new Integer(conditions.size()), WarningTypes.INTERVENTION);
		
		if(description != null && !description.equals("")){
			conditions.add("UPPER(TRANSLATE(civil_work_warnings.description,'ÁÉÍÓÚáéíóú','AEIOUaeiou')) LIKE UPPER(TRANSLATE(?,'ÁÉÍÓÚáéíóú','AEIOUaeiou'))");
			preparedStatementSets.put(new Integer(conditions.size()), "%"+description+"%");
		}
		if(fromStart != null){
			conditions.add("civil_work_intervention.start_warning >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromStart);
		}	
		if(toStart != null){
			conditions.add("civil_work_intervention.start_warning <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toStart);
		}
		if(fromNext != null){
			conditions.add("civil_work_intervention.next_warning >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromNext);
		}	
		if(toNext != null){
			conditions.add("civil_work_intervention.next_warning <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toNext);
		}
		if(actuationType != null && !actuationType.equals("")){
			conditions.add("civil_work_intervention.actuation_type = ?");
			preparedStatementSets.put(new Integer(conditions.size()), actuationType);
		}
		if(interventionType != null && !interventionType.equals("")){
			conditions.add("civil_work_intervention.intervention_type = ?");
			preparedStatementSets.put(new Integer(conditions.size()), interventionType);
		}
		if(foreseenBudgetFrom != null){
			conditions.add("civil_work_intervention.foreseen_budget >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), foreseenBudgetFrom);
		}
		if(foreseenBudgetTo != null){
			conditions.add("civil_work_intervention.foreseen_budget <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), foreseenBudgetTo);
		}
		if(workPercentageFrom != null){
			conditions.add("civil_work_intervention.work_percentage >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), workPercentageFrom);
		}
		if(workPercentageTo != null){
			conditions.add("civil_work_intervention.work_percentage <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), workPercentageTo);
		} 
		if(causes != null && !causes.equals("")){
			conditions.add("UPPER(TRANSLATE(civil_work_intervention.causes,'ÁÉÍÓÚáéíóú','AEIOUaeiou')) LIKE UPPER(TRANSLATE(?,'ÁÉÍÓÚáéíóú','AEIOUaeiou'))");
			preparedStatementSets.put(new Integer(conditions.size()), "%"+causes+"%");
		}
		conditions.add("civil_work_warnings.id_municipio = ?");
		preparedStatementSets.put(new Integer(conditions.size()), idMunicipio);
		if(isAdministrator){
			sqlQuery += "(civil_work_intervention.assigned_user = "+ userId +" OR civil_work_warnings.user_creator = "+userId + ") AND ";
		}else{
			conditions.add("(civil_work_intervention.assigned_user = ?");
			preparedStatementSets.put(new Integer(conditions.size()), userId);
		}
		
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
	        	numInterventions = rs.getInt("NUM_INTERVENTIONS");
		    }
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return numInterventions;
	}

	@Override
	public void removeIntervention(Connection connection, Integer idWarning)
			throws SQLException {
		logger.debug("removing intervention");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarning(connection, idWarning);
		
	}

	@Override
	public void removeInterventionDocument(Connection connection,
			Integer idDocument) throws SQLException {
		logger.debug("removing document");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeDocument(connection, idDocument);
		
	}

	@Override
	public void removeInterventionDocuments(Connection connection,
			Integer idWarning) throws SQLException {
		logger.debug("removing intervention documents");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarningDocuments(connection, idWarning);
		
	}

	@Override
	public void removeInterventionLayerFeatureReferences(Connection connection,
			Integer idWarning) throws SQLException {
		logger.debug("removing layer - features intervention");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeLayerFeatureReferences(connection, idWarning);
		
	}

	@Override
	public void setIntervention(Connection connection,
			LocalGISIntervention intervention) throws SQLException {
		logger.debug("setting intervention basic data");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.setWarning(connection,intervention);
	}

	@Override
	public void addInterventionSpecificInfo(
			Connection connection, LocalGISIntervention intervention)
			throws SQLException, ConfigurationException {

		String sqlQuery = "INSERT INTO civil_work_intervention(" +
				"id_warning," +
				"actuation_type," +
				"intervention_type," +
				"pattern," +
				"next_warning," +
				"foreseen_budget," +
				"work_percentage," +
				"causes," +
				"assigned_user, " +
				"ended_work, " +
				"priority) " +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		if(logger.isDebugEnabled())logger.debug("query interventionSpecificInfo list = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,intervention.getId());		
			preparedStatement.setString(2, intervention.getActuationType());
			preparedStatement.setString(3, intervention.getInterventionType());
			preparedStatement.setString(4, intervention.getPattern());
			if(intervention.getNextWarning() != null)
				preparedStatement.setDate(5,new java.sql.Date(intervention.getNextWarning().getTimeInMillis()));
			else
				preparedStatement.setNull(5, java.sql.Types.DATE);
			if(intervention.getForeseenBudget() != null)
				preparedStatement.setDouble(6, intervention.getForeseenBudget());
			else
				preparedStatement.setNull(6, java.sql.Types.DOUBLE);
			if(intervention.getWorkPercentage() != null)
				preparedStatement.setDouble(7, intervention.getWorkPercentage());
			else
				preparedStatement.setNull(7, java.sql.Types.DOUBLE);
			if(intervention.getCauses() != null && !intervention.getCauses().trim().equals(""))
				preparedStatement.setString(8, intervention.getCauses());
			else
				preparedStatement.setNull(8,java.sql.Types.VARCHAR);
			preparedStatement.setInt(9, intervention.getAssignedUser());
			if(intervention.getEndedWork() != null)
				preparedStatement.setDate(10,new java.sql.Date(intervention.getEndedWork().getTimeInMillis()));
			else
				preparedStatement.setNull(10, java.sql.Types.DATE);
			if(intervention.getPriority() != null)
				preparedStatement.setInt(11,intervention.getPriority());
			else
				preparedStatement.setNull(11, java.sql.Types.INTEGER);
			if(logger.isDebugEnabled()){
				logger.debug("Inserting intervention with id = "+ intervention.getId());
				logger.debug("Inserting intervention "+ intervention.getId() + " actuation_type = " + intervention.getActuationType());
				logger.debug("Inserting intervention "+ intervention.getId() + " intervention_type = " + intervention.getInterventionType());
				logger.debug("Inserting intervention "+ intervention.getId() + " pattern = " + intervention.getPattern());
				logger.debug("Inserting intervention "+ intervention.getId() + " next_warning = " + intervention.getNextWarning());
				logger.debug("Inserting intervention "+ intervention.getId() + " foreseen_budget = " + intervention.getForeseenBudget());
				logger.debug("Inserting intervention "+ intervention.getId() + " work_percentage = " + intervention.getWorkPercentage());
				logger.debug("Inserting intervention "+ intervention.getId() + " causes = " + intervention.getCauses());
				logger.debug("Inserting intervention "+ intervention.getId() + " assigned_user = " + intervention.getAssignedUser());
				logger.debug("Inserting intervention "+ intervention.getId() + " endedWork = " + intervention.getEndedWork());
				logger.debug("Inserting intervention "+ intervention.getId() + " priority = " + intervention.getPriority());
			}
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}

	@Override
	public void setInterventionSpecificInfo(Connection connection,
			LocalGISIntervention intervention) throws SQLException {
		String sqlQuery = "UPDATE civil_work_intervention SET " +
		"actuation_type = ?," +
		"intervention_type = ?," +
		"pattern = ?," +
		"next_warning = ?," +
		"foreseen_budget = ?," +
		"work_percentage = ?," +
		"causes = ?," +
		"assigned_user = ?, " +
		"ended_work = ?, " +
		"priority = ? " +
		"WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Update warning = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, intervention.getActuationType());
			preparedStatement.setString(2, intervention.getInterventionType());
			preparedStatement.setString(3, intervention.getPattern());
			preparedStatement.setDate(4,new java.sql.Date(intervention.getNextWarning().getTimeInMillis()));
			preparedStatement.setDouble(5, intervention.getForeseenBudget());
			preparedStatement.setDouble(6, intervention.getWorkPercentage());
			if(intervention.getCauses() != null && !intervention.getCauses().trim().equals(""))
				preparedStatement.setString(7, intervention.getCauses());
			else
				preparedStatement.setNull(7,java.sql.Types.VARCHAR);
			preparedStatement.setInt(8, intervention.getAssignedUser());
			if(intervention.getEndedWork() != null)
				preparedStatement.setDate(9,new java.sql.Date(intervention.getEndedWork().getTimeInMillis()));
			else
				preparedStatement.setNull(9, java.sql.Types.DATE);
			if(intervention.getPriority() != null)
				preparedStatement.setInt(10,intervention.getPriority());
			else
				preparedStatement.setNull(10, java.sql.Types.INTEGER);
			if(logger.isDebugEnabled()){
				logger.debug("Inserting intervention with id = "+ intervention.getId());
				logger.debug("Inserting intervention "+ intervention.getId() + " actuation_type = " + intervention.getActuationType());
				logger.debug("Inserting intervention "+ intervention.getId() + " intervention_type = " + intervention.getInterventionType());
				logger.debug("Inserting intervention "+ intervention.getId() + " pattern = " + intervention.getPattern());
				logger.debug("Inserting intervention "+ intervention.getId() + " next_warning = " + intervention.getNextWarning());
				logger.debug("Inserting intervention "+ intervention.getId() + " foreseen_budget = " + intervention.getForeseenBudget());
				logger.debug("Inserting intervention "+ intervention.getId() + " work_percentage = " + intervention.getWorkPercentage());
				logger.debug("Inserting intervention "+ intervention.getId() + " causes = " + intervention.getCauses());
				logger.debug("Inserting intervention "+ intervention.getId() + " assigned_user = " + intervention.getAssignedUser());
				logger.debug("Inserting intervention "+ intervention.getId() + " endedWork = " + intervention.getEndedWork());
				logger.debug("Inserting intervention "+ intervention.getId() + " priority = " + intervention.getPriority());
			}
			preparedStatement.setInt(11,intervention.getId());
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}

	public static void buildOrderColumns(String sqlQuery,
			ArrayList<InterventionColumn> orderColumns) {
		if(orderColumns != null){
			Iterator<InterventionColumn> it = orderColumns.iterator();
			sqlQuery += " ORDER BY ";
			while(it.hasNext()){
				sqlQuery += it.next();
				if(it.hasNext())
					sqlQuery += ",";
			}
		}
	}
	@Override
	public void changeWarningDate(Connection connection, Integer idWarning,Calendar calendar) throws SQLException{
		String sqlQuery = "UPDATE civil_work_intervention SET " +
		"next_warning = ? " +
		"WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Update warning = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(calendar != null){
				preparedStatement.setDate(1, new java.sql.Date(calendar.getTimeInMillis()));
			}else{
				preparedStatement.setNull(1, java.sql.Types.DATE);
			}
			preparedStatement.setInt(2, idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}

	@Override
	public ArrayList<StatisticalDataOT> getStatistics(Connection connection, Integer idEntidad) throws SQLException {
		throw new NotImplementedException();
		
	}

	@Override
	public void removeNetworkIncident(Connection connection,Integer idNetwork, Integer idWarning)
			throws SQLException {
		String sqlQuery = "DELETE FROM NETWORK_INCIDENTS WHERE " +
				"ID_NETWORK = ? AND " +
				"ID_WARNING = ?";
		if(logger.isDebugEnabled())logger.debug("Delete Network incident = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idNetwork);
			preparedStatement.setInt(2, idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}

	@Override
	public Integer getNetworkId(Connection connection,
			String propertyNetworkName) throws SQLException {
		String sqlQuery = "SELECT ID_NETWORK FROM NETWORKS WHERE " +
		"NETWORK_NAME = ?";
		if(logger.isDebugEnabled())logger.debug("Get Network Name = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idNetwork = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, propertyNetworkName);
			rs = preparedStatement.executeQuery();
			if(rs.next())
				idNetwork = rs.getInt("ID_NETWORK");
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idNetwork;
	}

	@Override
	public void addNetworkIncident(Connection connection, Integer idNetwork, Integer idEdge,
			Integer idWarning,LocalGISIncident incident,String description,Integer incidentType) throws SQLException {
		
		String sqlQuery = "INSERT INTO NETWORK_INCIDENTS(" +
				"ID_NETWORK," +
				"ID_EDGE," +
				"INCIDENT_TYPE," +
				"DESCRIPTION," +
				"DATE_START," +
				"DATE_END," +
				"PATTERN," +
				"ID_WARNING) " +
		"VALUES (?,?,?,?,?,?,?,?)";
		if(logger.isDebugEnabled())logger.debug("Get Network Name = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idNetwork);
			preparedStatement.setInt(2, idEdge);
			preparedStatement.setInt(3, incidentType);
			preparedStatement.setString(4, description); 
			if(incident.getDateStart() != null)
				preparedStatement.setDate(5,new java.sql.Date(incident.getDateStart().getTime()));
			else
				preparedStatement.setNull(5, java.sql.Types.DATE);
			if(incident.getDateEnd() != null)
				preparedStatement.setDate(6,new java.sql.Date(incident.getDateEnd().getTime()));
			else
				preparedStatement.setNull(6, java.sql.Types.DATE);
			preparedStatement.setString(7,incident.toString());
			preparedStatement.setInt(8,idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}

	@Override
	public Integer getEdgeFromFeature(Connection connection, Integer idNetwork,
			LayerFeatureBean layerFeatureBean) throws SQLException {
		String sqlQuery = "SELECT ID_EDGE FROM NETWORK_EDGES WHERE ID_NETWORK = ? AND ID_LAYER = ? AND ID_FEATURE = ?";
		if(logger.isDebugEnabled())logger.debug("Get Network Edge = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idEdge = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idNetwork);
			preparedStatement.setInt(2, layerFeatureBean.getIdLayer());
			preparedStatement.setInt(3, layerFeatureBean.getIdFeature());
			rs = preparedStatement.executeQuery();
			if(rs.next())
				idEdge = rs.getInt("ID_EDGE");
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idEdge;
	}

	@Override
	public Integer getNetworkIncidentType(Connection connection, Integer id)
			throws SQLException {
		String sqlQuery = "select incident_type from network_incidents where id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Get Network Edge = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer incidentType = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if(rs.next())
				incidentType = rs.getInt("incident_type");
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return incidentType;
	}

}
