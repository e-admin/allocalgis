package com.geopista.server.civilwork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.server.database.CPoolDatabase;
import com.geopista.util.ConnectionUtilities;


public class CivilWorkThread extends Thread {

	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CivilWorkThread.class);

	public CivilWorkThread() {
		start();
	}


	public void run() {
		Connection connection = null;
		while (true) {
			try {
				//Posibilidad de actualizar la info del fichero
				//updateConfiguration();
				connection = CPoolDatabase.getConnection();
				if(CivilWorkConfiguration.timePriorityUpgrade != 0){
					// Actualizar lista de registros no caducados
					updateRegistry(connection);
					// Actualizar prioridad
					updatePriority(connection);
				}
			} catch (Exception ex){
				logger.error("Exception: "+ex.toString());
			}finally{
				if(logger.isDebugEnabled())logger.debug("Civil work priority updater done. Sleeping: "+CivilWorkConfiguration.timeCivilWorkThread*60000 +" minutes");
				ConnectionUtilities.closeConnection(connection);
				CPoolDatabase.releaseConexion();
				try {
					Thread.sleep(CivilWorkConfiguration.timeCivilWorkThread*60000);
				} catch (InterruptedException e) {
					logger.error("Interrupted Exception",e);
				}
				
			}
		}

	}


	private void updatePriority(Connection connection) throws SQLException {
		// obtener caducados sin registro
		ArrayList<Integer> listToUpdatePriority = getWarningsToUpdateWithNoRegistry(connection);
		// Obtener caducados con registro
		ArrayList<Integer> listToUpdateWithRegistryPriority = getWarningsToUpdateWithRegistry(connection);
		Iterator<Integer> it = listToUpdatePriority.iterator();
		while(it.hasNext())
			insertRegistry(connection,it.next());
		Iterator<Integer> it2 = listToUpdateWithRegistryPriority.iterator();
		while(it2.hasNext())
			updateRegistry(connection,it2.next());
		listToUpdatePriority.addAll(listToUpdateWithRegistryPriority);
		Iterator<Integer> it3 = listToUpdatePriority.iterator();
		while(it3.hasNext())
			updateWarningPriority(connection,it3.next());
	}


	private void updateWarningPriority(Connection connection, Integer idWarning) throws SQLException {
		String sqlQuery = "UPDATE CIVIL_WORK_INTERVENTION SET PRIORITY = PRIORITY-1 WHERE ID_WARNING = ?";
		PreparedStatement preparedStatement = null;
		int results = 0;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			results = preparedStatement.executeUpdate();
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Warnings priority updated! . " + results + " elements updated ");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,null);
		}
		
	}


	private void updateRegistry(Connection connection, Integer idWarning) throws SQLException {
		String sqlQuery = "UPDATE CIVIL_WORK_REGISTRY SET DATE_UPDATED = current_date WHERE ID_WARNING = ?";
		PreparedStatement preparedStatement = null;
		int results = 0;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			results = preparedStatement.executeUpdate();
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Registry date updated. " + results + " elements updated in registry.");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,null);
		}
		
	}
	

	private void insertRegistry(Connection connection,
			Integer idWarning) throws SQLException {
		String sqlQuery = "INSERT INTO CIVIL_WORK_REGISTRY(ID_WARNING,DATE_UPDATED) VALUES(?,current_date)";
		PreparedStatement preparedStatement = null;
		int results = 0;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			results = preparedStatement.executeUpdate();
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Registry data inserted. " + results + " new elements inserted.");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,null);
		}
		
	}


	private ArrayList<Integer> getWarningsToUpdateWithRegistry(
			Connection connection) throws SQLException {
		String warningsToUpdateFromRegistry ="select registry.id_warning from civil_work_intervention intervention " +
				"inner join CIVIL_WORK_REGISTRY registry on registry.id_warning = intervention.id_warning " +
				"where current_date - registry.date_updated >= ? and intervention.priority > 1 ";
		PreparedStatement preparedStatement = null;
		ArrayList<Integer> resultId = new ArrayList<Integer>(); 
		ResultSet resultSet = null;
		try{
			preparedStatement = connection.prepareStatement(warningsToUpdateFromRegistry);
			preparedStatement.setInt(1,CivilWorkConfiguration.timePriorityUpgrade);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				resultId.add(resultSet.getInt("id_warning"));
			}
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Obtained " + resultId.size() + " elements to update with previous info");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,resultSet);
		}
		return resultId;
	}


	private ArrayList<Integer> getWarningsToUpdateWithNoRegistry(Connection connection) throws SQLException {
		String selectRegistry = "SELECT ID_WARNING FROM CIVIL_WORK_REGISTRY";
		String warningsToUpdateFromRegistry ="select id_warning from civil_work_intervention where current_date - next_warning >= ? and priority > 1 AND ID_WARNING NOT IN ("+selectRegistry+") AND ENDED_WORK IS NULL";
		PreparedStatement preparedStatement = null;
		ArrayList<Integer> resultId = new ArrayList<Integer>(); 
		ResultSet resultSet = null;
		try{
			preparedStatement = connection.prepareStatement(warningsToUpdateFromRegistry);
			preparedStatement.setInt(1,CivilWorkConfiguration.timePriorityUpgrade);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				resultId.add(resultSet.getInt("id_warning"));
			}
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Obtained " + resultId.size() + " elements to update with no previous info");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,resultSet);
		}
		return resultId;
	}


	private void updateRegistry(Connection connection) throws SQLException{
		// Eliminacion de actuaciones del registro que no cumplan los requisitos minimos de activacion;
		String warningsToDeleteFromRegistry ="select id_warning from civil_work_intervention where current_date - next_warning >= ? and priority != 1";
		String deleteRegistry = "DELETE FROM CIVIL_WORK_REGISTRY WHERE ID_WARNING NOT IN ("+warningsToDeleteFromRegistry+")";
		PreparedStatement preparedStatement = null;
		int results = 0;
		try{
			preparedStatement = connection.prepareStatement(deleteRegistry);
			preparedStatement.setInt(1,CivilWorkConfiguration.timePriorityUpgrade);
			results = preparedStatement.executeUpdate();
			if(logger.isDebugEnabled())logger.debug("CivilWorkThread: Registry updated. " + results + " elements deleted");
		}finally{
			ConnectionUtilities.closeConnection(null,preparedStatement,null);
		}
	}




}
