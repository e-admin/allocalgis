/**
 * PostgresLocalGISInterventionsDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.postgres.warnings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISGenericDAO;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class PostgresLocalGISInterventionsDAO extends LocalGISInterventionsDAO{
	
	private static Logger logger = Logger.getLogger(PostgresLocalGISInterventionsDAO.class);
	
	@Override
	public void addInterventionDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocumentThumbnail(connection, document);
		
	}

	@Override
	public void addInterventionDocument(Connection connection,
			Integer idWarning, Document document)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocument(connection, idWarning, document);
		
	}
	@Override
	public Document getInterventionDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getWarningDocument(connection, idDocument,returnCompleteDocuments);
	}

	@Override
	public ArrayList<Document> getInterventionDocuments(
			Connection connection, Integer idWarning,
			Boolean returnCompleteDocuments) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
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
		ArrayList<LocalGISIntervention> interventions = new ArrayList<LocalGISIntervention>();
		String sqlQuery = 
			"SELECT " +
				"civil_work_warnings.id_warning AS ID_WARNING, " + 
				"civil_work_warnings.description AS DESCRIPTION," +
				"civil_work_warnings.start_warning AS START_WARNING," +
				"civil_work_warnings.user_creator AS USER_CREATOR," +
				"civil_work_warnings.id_municipio AS ID_MUNICIPIO," +
				"civil_work_intervention.actuation_type AS ACTUATION_TYPE," +
				"civil_work_intervention.intervention_type AS INTERVENTION_TYPE," +
				"civil_work_intervention.pattern AS PATTERN," +
				"civil_work_intervention.next_warning AS NEXT_WARNING," +
				"civil_work_intervention.foreseen_budget AS FORESEEN_BUDGET," +
				"civil_work_intervention.work_percentage AS WORK_PERCENTAGE," +
				"civil_work_intervention.causes AS CAUSES," +
				"civil_work_intervention.assigned_user AS ASSIGNED_USER," +
				"civil_work_intervention.ended_work AS ENDED_WORK," +
				"civil_work_intervention.priority AS PRIORITY " +
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
			conditions.add("civil_work_warnings.start_warning >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromStart);
		}	
		if(toStart != null){
			conditions.add("civil_work_warnings.start_warning <= ?");
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
			preparedStatementSets.put(new Integer(conditions.size()), causes);
		}
		conditions.add("civil_work_warnings.id_municipio = ?");
		preparedStatementSets.put(new Integer(conditions.size()), idMunicipio);
		if(isAdministrator){
			sqlQuery += "(civil_work_intervention.assigned_user = "+ userId +" OR civil_work_warnings.user_creator = "+userId + ") AND ";
		}else{
			conditions.add("civil_work_intervention.assigned_user = ?");
			preparedStatementSets.put(new Integer(conditions.size()), userId);
		}
		
		sqlQuery = LocalGISGenericDAO.buildQuery(sqlQuery,conditions);
		
		sqlQuery = LocalGISGenericDAO.buildLayerFeatureReferenceQuery(sqlQuery,features);
		sqlQuery += " GROUP BY "+ 
					"civil_work_warnings.id_warning,"+ 
					"civil_work_warnings.description ,"+
					"civil_work_warnings.start_warning ,"+
					"civil_work_warnings.user_creator ,"+
					"civil_work_warnings.id_municipio ,"+
					"civil_work_intervention.actuation_type,"+
					"civil_work_intervention.intervention_type,"+
					"civil_work_intervention.pattern,"+
					"civil_work_intervention.next_warning,"+
					"civil_work_intervention.foreseen_budget,"+
					"civil_work_intervention.work_percentage,"+
					"civil_work_intervention.causes,"+
					"civil_work_intervention.ended_work,"+
					"civil_work_intervention.priority,"+
					"civil_work_intervention.assigned_user";
		sqlQuery += LocalGISGenericDAO.buildOrderByColumns(orderColumns);
		if(startElement != null){
			sqlQuery += " OFFSET " + startElement;
		}
		if(range != null){
			sqlQuery += " LIMIT " + range;
		}
		if(logger.isDebugEnabled())logger.debug("Query Warning List = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			LocalGISGenericDAO.buildStatement(preparedStatement,preparedStatementSets);
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			rs = preparedStatement.executeQuery();
	        while (rs.next()){
	        	Integer actualIdWarning = rs.getInt("ID_WARNING");
				String actualDescription = rs.getString("DESCRIPTION");
				Date actualStartDate = rs.getDate("START_WARNING");
				Calendar start = new GregorianCalendar();
				start.setTime(actualStartDate);
				Calendar eWork = null;
				Date endedWork = rs.getDate("ENDED_WORK");
				if(endedWork!= null){
					eWork = new GregorianCalendar();
					eWork.setTime(endedWork);
				}
				Integer actualUserCreator = Integer.parseInt(rs.getString("USER_CREATOR"));
				Integer actualIdMunicipio = rs.getInt("ID_MUNICIPIO");
				String actualActuationType = rs.getString("ACTUATION_TYPE");
				String actualInterventionType = rs.getString("INTERVENTION_TYPE");
				String actualPattern = rs.getString("PATTERN");
				Integer priority = rs.getInt("PRIORITY");
				Calendar next = null;
				if(rs.getDate("NEXT_WARNING") != null){
					Date actualNextWarning = rs.getDate("NEXT_WARNING");
					next = new GregorianCalendar();
					next.setTime(actualNextWarning);
				}
				Double actualForeseenBudget = rs.getDouble("FORESEEN_BUDGET");
				Double actualWorkPercentage = rs.getDouble("WORK_PERCENTAGE");
				String actualCauses = rs.getString("CAUSES");
				Integer actualAssignedUser = rs.getInt("ASSIGNED_USER");
	        	LocalGISIntervention intervention = new LocalGISIntervention(actualIdWarning,actualDescription,start,actualUserCreator,actualIdMunicipio,
	        			actualActuationType,actualInterventionType,actualPattern,next,actualForeseenBudget,actualWorkPercentage,actualCauses,
	        			actualAssignedUser);
	        	intervention.setEndedWork(eWork);
	        	intervention.setPriority(priority);
	        	interventions.add(intervention);
		    }
		} catch (ConfigurationException e) {
			logger.error(e);
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return interventions;
	}

	@Override
	public Integer getNextIdDocumentInterventionSequence(Connection connection)
			throws SQLException {
		PostgresDAO dao = new PostgresDAO();
		return dao.getNextIdDocumentSequence(connection);
	}

	@Override
	public Integer getNextIdInterventionSequence(Connection connection)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getNextIdWarningSequence(connection);
	}
	@Override
	public ArrayList<StatisticalDataOT> getStatistics(Connection connection, Integer idEntidad) throws SQLException {
		// Generacion de estadisticas de intervenciones
		ArrayList<StatisticalDataOT> statistics = new ArrayList<StatisticalDataOT>();
		String sqlQuery = 
			"SELECT " +
				"RESULT.ID_MUNICIPIO as idmunicipio," +
				"RESULT.ACTUATION_TYPE as actuationType," +
				"RESULT.INTERVENTION_TYPE as interventionType," +
				"COUNT(RESULT.SOLVED_TIME) AS numInterventions," +
				"SUM(RESULT.SOLVED_TIME)/COUNT(RESULT.SOLVED_TIME) AS media," +
				"ROUND(STDDEV(RESULT.SOLVED_TIME),3) AS standardDeviation " +
			"FROM ( " +
				"SELECT " +
					"civil_work_warnings.id_municipio AS ID_MUNICIPIO, " +
					"civil_work_intervention.actuation_type AS ACTUATION_TYPE, " +
					"civil_work_intervention.intervention_type AS INTERVENTION_TYPE, " +	
					"civil_work_intervention.ended_work - civil_work_warnings.start_warning AS SOLVED_TIME " +
				"FROM " + 
					"civil_work_warnings " + 
				"INNER JOIN civil_work_intervention ON civil_work_warnings.id_warning = civil_work_intervention.id_warning " +
				"WHERE civil_work_intervention.ended_work IS NOT NULL AND civil_work_warnings.id_municipio in (select id_municipio from entidades_municipios where id_entidad = ?)) " +
			"RESULT " +
			"GROUP BY RESULT.ID_MUNICIPIO,RESULT.ACTUATION_TYPE,RESULT.INTERVENTION_TYPE " +
			"ORDER BY idmunicipio,actuationType,interventionType";
		
		if(logger.isDebugEnabled())logger.debug("Query Warning List = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			preparedStatement.setInt(1,idEntidad);
			rs = preparedStatement.executeQuery();
	        while (rs.next()){
	        	StatisticalDataOT statistic = new StatisticalDataOT();
	        	statistic.setActuationType(rs.getString("actuationType"));
	        	statistic.setInterventionType(rs.getString("interventionType"));
	        	statistic.setIdMunicipio(rs.getInt("idmunicipio"));
				statistic.setNumInterventions(rs.getInt("numInterventions"));
				statistic.setMedia(rs.getDouble("media"));
				statistic.setStdDeviation(rs.getDouble("standardDeviation"));
				statistics.add(statistic);
		    }
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return statistics;
		
		
	}
}
