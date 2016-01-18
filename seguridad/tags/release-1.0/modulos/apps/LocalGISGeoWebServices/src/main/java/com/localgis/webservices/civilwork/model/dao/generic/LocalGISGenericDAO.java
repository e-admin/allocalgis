package com.localgis.webservices.civilwork.model.dao.generic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISGenericDAO {
	
	private static Logger logger = Logger.getLogger(LocalGISNotesDAO.class);
	
	public void addWarning(Connection connection, LocalGISNote warning) throws SQLException{
		Integer warningType = null;
		if(warning instanceof LocalGISIntervention){
			warningType = WarningTypes.INTERVENTION;
		}else{
			warningType = WarningTypes.NOTE;
		}
		String sqlQuery = "INSERT INTO civil_work_warnings(id_warning,description,start_warning,user_creator,id_municipio,id_type) VALUES " +
							"(?,?,?,?,?,?)";

		if(logger.isDebugEnabled())logger.debug("query layerfeature list = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled()){
				logger.debug("Starting Statement");
				logger.debug("Inserting warning with id = "+ warning.getId());
				logger.debug("Inserting warning "+ warning.getId() + " -> description = " + warning.getId());
				logger.debug("Inserting warning "+ warning.getId() + " start_warning = " + warning.getStartWarning());
				logger.debug("Inserting warning "+ warning.getId() + " user_creator = " + warning.getUserCreator());
				logger.debug("Inserting warning "+ warning.getId() + " id_municipio = " + warning.getIdMunicipio());
				logger.debug("Inserting warning "+ warning.getId() + " id_type = " + warningType);
			}
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,warning.getId());
			if(warning.getDescription() != null && !warning.getDescription().trim().equals(""))
				preparedStatement.setString(2, warning.getDescription());
			else
				preparedStatement.setNull(2, java.sql.Types.VARCHAR);
			preparedStatement.setDate(3,new Date(warning.getStartWarning().getTime().getTime()));
			preparedStatement.setInt(4, warning.getUserCreator());
			preparedStatement.setInt(5, warning.getIdMunicipio());
			preparedStatement.setInt(6, warningType);
			
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	public void addLayerFeatureReferences(Connection connection,ArrayList<LayerFeatureBean> layerFeatureBeans, Integer idWarning) throws SQLException {
		Iterator<LayerFeatureBean> it = layerFeatureBeans.iterator();
		while(it.hasNext()){
			addLayerFeatureReference(connection, it.next(), idWarning);
		}
		
	}
	private void addLayerFeatureReference(Connection connection,LayerFeatureBean layerFeatureBean,Integer idWarning) throws SQLException{
		String sqlQuery = "INSERT INTO civil_work_layer_feature_reference(id_warning,id_layer,id_feature) VALUES (?,?,?)";
		if(logger.isDebugEnabled())logger.debug("add layerfeature = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			preparedStatement.setInt(2, layerFeatureBean.getIdLayer());
			preparedStatement.setInt(3,layerFeatureBean.getIdFeature());
			if(logger.isDebugEnabled())logger.debug("Inserting layerFeature -> idWarning = " + idWarning +" idLayer = " +layerFeatureBean.getIdLayer() + " idFeature = " + layerFeatureBean.getIdFeature());
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}
	public void addWarningDocuments(Connection connection, Integer idWarning,
			ArrayList<Document> documents) throws SQLException {
		Iterator<Document> it = documents.iterator();
		while(it.hasNext()){
			addWarningDocument(connection, idWarning, it.next());
		}
		
	}
	// TODO: Se debe implementar para evaluar si es de Oracle o Postgres
	protected void addWarningDocument(Connection connection, Integer idWarning,Document document) throws SQLException{
		// Implements this method to generate postgres or oracle query
		throw new NotImplementedException();
	}
	public LocalGISNote getWarning(Connection connection,Integer idWarning,Integer idMunicipio,Integer idUser) throws SQLException, ConfigurationException {
		LocalGISNote note = null;
		String sqlQuery = 
			"SELECT " +
				"civil_work_warnings.description AS DESCRIPTION, " +
				"civil_work_warnings.start_warning AS START_WARNING " +
			"FROM " +
				"civil_work_warnings " +
			"WHERE " +
				"civil_work_warnings.id_warning = ? AND " +
				"civil_work_warnings.id_municipio = ? AND " +
				"civil_work_warnings.user_creator = ? ";
				
																

		if(logger.isDebugEnabled())logger.debug("getNote() = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idWarning);
			preparedStatement.setInt(2, idMunicipio);
			preparedStatement.setInt(3, idUser);
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			rs = preparedStatement.executeQuery();
	        if (rs.next()){
	        	String descripcion = rs.getString("DESCRIPTION");
	        	GregorianCalendar fechaAlta = new GregorianCalendar();
	        	fechaAlta.setTime(rs.getDate("START_WARNING"));
	        	if(logger.isDebugEnabled()){
	        		logger.debug("Closing Statement & resultset");
	        	}
	        	note = new LocalGISNote(idWarning,idUser,idMunicipio,descripcion,fechaAlta,null,null);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return note;
	}
	
	public ArrayList<LayerFeatureBean> getLayerFeatureReferences(
			Connection connection, Integer idWarning) throws SQLException {
		String sqlQuery = "SELECT * FROM civil_work_layer_feature_reference WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("query get Feature References = " + sqlQuery);
		ArrayList<LayerFeatureBean> layerFeatureBeans = new ArrayList<LayerFeatureBean>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idWarning);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
	        while (rs.next()){
	        	Integer idLayer = rs.getInt("id_layer");
	        	Integer idFeature = rs.getInt("id_feature");
	        	LayerFeatureBean layerFeatureBean = new LayerFeatureBean(idLayer,idFeature);
	        	if(logger.isDebugEnabled())logger.debug("Inserting layerFeature -> idWarning = " + idWarning +" idLayer = " +layerFeatureBean.getIdLayer() + " idFeature = " + layerFeatureBean.getIdFeature());
	        	layerFeatureBeans.add(layerFeatureBean);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return layerFeatureBeans;
	}
	
	public void removeWarning(Connection connection, Integer idWarning) throws SQLException {
		String sqlQuery = "DELETE FROM civil_work_warnings WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Delete complete warning = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	
	public void removeDocument(Connection connection, Integer idDocument) throws SQLException {
		String sqlQuery = "DELETE FROM civil_work_warnings WHERE id_document = ?";
		if(logger.isDebugEnabled())logger.debug("Delete document = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idDocument);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	
	public void removeWarningDocuments(Connection connection, Integer idWarning) throws SQLException {
		String sqlQuery = "DELETE FROM civil_work_documents WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Remove documents = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	
	public void removeLayerFeatureReferences(Connection connection,
			Integer idWarning) throws SQLException {
		String sqlQuery = "DELETE FROM civil_work_layer_feature_reference WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Delete layer_feature_references = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,idWarning);
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	public void setWarning(Connection connection,LocalGISNote note) throws SQLException {
		String sqlQuery = "UPDATE civil_work_warnings SET " +
			"description = ?," +
			"start_warning = ? " +
			"WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("Update warning = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(note.getDescription() != null && !note.getDescription().trim().equals("")){
				preparedStatement.setString(1, note.getDescription());
			}else{
				preparedStatement.setNull(1, java.sql.Types.VARCHAR);
			}
			preparedStatement.setDate(2,new java.sql.Date(note.getStartWarning().getTimeInMillis()));
			preparedStatement.setInt(3,note.getId());
			if(logger.isDebugEnabled())logger.debug("setting warning to database : idWarning = "+ note.getId() + " description -> "+note.getDescription()+" start_warning -> " + note.getStartWarning() + " user_creator -> " + note.getUserCreator());
			preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	
	public static String buildQuery(String sqlQuery, ArrayList<String> conditions) {
		if(conditions != null && conditions.size() > 0){
			Iterator<String> condition = conditions.iterator();
			while (condition.hasNext()){
				sqlQuery += condition.next();
				if(condition.hasNext())
					sqlQuery += " AND "; 
			}
		}
		return sqlQuery;
	}
	public static String buildLayerFeatureReferenceQuery(String sqlQuery, ArrayList<LayerFeatureBean> features) {
		if(features != null && !features.isEmpty()){
			Iterator<LayerFeatureBean> it = features.iterator();
			String lfCondition = " AND (";
			while(it.hasNext()){
				LayerFeatureBean feature = it.next();
				lfCondition += "(civil_work_layer_feature_reference.id_layer = " + feature.getIdLayer() + " AND civil_work_layer_feature_reference.id_feature = " + feature.getIdFeature() + " )";
				if(it.hasNext())
					lfCondition += " OR ";
			}
			sqlQuery+=lfCondition + ") ";
		}
		return sqlQuery;
		
	}
	public static void buildStatement(PreparedStatement preparedStatement,Hashtable<Integer, Object> preparedStatementSets) throws SQLException {
		int i = 1;
		while(preparedStatementSets.get(new Integer(i)) != null){
			if(preparedStatementSets.get(new Integer(i)) instanceof String)
				preparedStatement.setString(i, (String)preparedStatementSets.get(new Integer(i)));
			if(preparedStatementSets.get(new Integer(i)) instanceof Integer)
				preparedStatement.setInt(i, (Integer)preparedStatementSets.get(new Integer(i)));
			if(preparedStatementSets.get(new Integer(i)) instanceof Date)
				preparedStatement.setDate(i, new java.sql.Date(((Date)preparedStatementSets.get(new Integer(i))).getTime()));
			if(preparedStatementSets.get(new Integer(i)) instanceof Calendar){
				preparedStatement.setDate(i, new java.sql.Date(((Calendar)preparedStatementSets.get(new Integer(i))).getTimeInMillis()));
			}
			if(preparedStatementSets.get(new Integer(i)) instanceof Double)
				preparedStatement.setDouble(i, (Double)preparedStatementSets.get(new Integer(i)));
			i++;
		}
		
	}
	public static byte[] getDocumentThumbnail(Document document){
		return document.getFichero();
	}
	public static String buildOrderByColumns(ArrayList<OrderByColumn> orderColumns) {
		Iterator<OrderByColumn> it  = orderColumns.iterator();
		String result = "";
		while(it.hasNext()){
			if(result.equals(""))
				result = " ORDER BY ";
			OrderByColumn column = it.next();
			result+=column.toString();
			if(it.hasNext())
				result+=",";
		}
		return result;
	}
}
