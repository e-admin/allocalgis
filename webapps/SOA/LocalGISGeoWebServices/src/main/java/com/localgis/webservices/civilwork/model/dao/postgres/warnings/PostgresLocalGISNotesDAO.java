/**
 * PostgresLocalGISNotesDAO.java
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

import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISGenericDAO;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISNotesDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class PostgresLocalGISNotesDAO extends LocalGISNotesDAO{
	
	private static Logger logger = Logger.getLogger(PostgresLocalGISNotesDAO.class);
	
	@Override
	public void addNoteDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocumentThumbnail(connection, document);
	}

	@Override
	public void addNoteDocument(Connection connection, Integer idWarning,
			Document document) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocument(connection, idWarning, document);
	}
	@Override
	public Document getNoteDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getWarningDocument(connection, idDocument,returnCompleteDocuments);
	}

	@Override
	public ArrayList<Document> getNoteDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getWarningDocuments(connection, idWarning,returnCompleteDocuments);
	}
	@Override
	public ArrayList<LocalGISNote> getNoteList(Connection connection,
			String description, Calendar from, Calendar to,
			Integer startElement, Integer range, Integer idMunicipio,
			Integer userId, ArrayList<OrderByColumn> orderColumns,
			ArrayList<LayerFeatureBean> features) throws SQLException {
			ArrayList<LocalGISNote> notes = new ArrayList<LocalGISNote>();
			String sqlQuery = 
				"SELECT " +
					"civil_work_warnings.id_warning AS ID_WARNING, " + 
					"civil_work_warnings.description AS DESCRIPTION," +
					"civil_work_warnings.start_warning AS START_WARNING," +
					"civil_work_warnings.user_creator AS USER_CREATOR," +
					"civil_work_warnings.id_municipio AS ID_MUNICIPIO " +
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
				description="%"+description+"%";
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
			sqlQuery += " GROUP BY civil_work_warnings.id_warning, " + 
					"civil_work_warnings.description," +
					"civil_work_warnings.start_warning," +
					"civil_work_warnings.user_creator," +
					"civil_work_warnings.id_municipio";
			sqlQuery += LocalGISGenericDAO.buildOrderByColumns(orderColumns);
			if(startElement != null)
				sqlQuery += " OFFSET " + startElement;
			if(range != null)
				sqlQuery += " LIMIT " + range;
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
					Integer actualUserCreator = Integer.parseInt(rs.getString("USER_CREATOR"));
					Integer actualIdMunicipio = rs.getInt("ID_MUNICIPIO");
					LocalGISNote note = null;
					try {
						note = new LocalGISNote(actualUserCreator, actualIdMunicipio,start);
					} catch (ConfigurationException e) {
						logger.error(e);
					}
					note.setId(actualIdWarning);
					note.setDescription(actualDescription);
					Calendar cal = new GregorianCalendar();
					cal.setTime(actualStartDate);
					note.setStartWarning(cal);
					notes.add(note);
			    }
			}finally{
				ConnectionUtilities.closeConnection(null, preparedStatement, rs);
				if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			}
			return notes;
		
	}
	@Override
	public Integer getNextIdDocumentNoteSequence(Connection connection)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getNextIdDocumentSequence(connection);
	}

	@Override
	public Integer getNextIdNoteSequence(Connection connection)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getNextIdWarningSequence(connection);
	}
}
