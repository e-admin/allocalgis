/**
 * ElementsByGeometryLN.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.ln;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.webservices.geomarketing.model.dao.DAOFactory;
import com.localgis.webservices.geomarketing.model.dao.ILocalGISGeoMarketingDAO;
import com.localgis.webservices.geomarketing.model.dao.IUserValidationDAO;
import com.localgis.webservices.geomarketing.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.geomarketing.model.ot.GeoMarketingOT;
import com.localgis.webservices.geomarketing.model.ot.GeomarketingFeatureOT;
import com.localgis.webservices.geomarketing.model.ot.NameDomainOT;
import com.localgis.webservices.geomarketing.model.ot.PortalStepRelationOT;
import com.localgis.webservices.geomarketing.model.ot.PostalDataOT;
import com.localgis.webservices.geomarketing.model.ot.RangeData;
import com.localgis.webservices.util.ConnectionUtilities;

public class ElementsByGeometryLN {
	
	private static Logger logger = Logger.getLogger(ElementsByGeometryLN.class);
	public int getNumElementsByGeometry(String wktGeometry,String srid,Integer idLayer,Integer idEntidad,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Integer numElements = 0; 
		Connection connection = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
	        ILocalGISGeoMarketingDAO geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();
	        String tableName = geoMarketingDAO.getTableQuery(connection, idLayer, idEntidad,srid);
	        numElements = geoMarketingDAO.getNumElements(connection,wktGeometry,srid,tableName);
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return numElements;
	}
	public GeoMarketingOT getGeomarketingDataByGeometry(ArrayList<Integer> ranges,String wktGeometry,String srid,Integer idEntidad,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		GeoMarketingOT geoMarketingOT = new GeoMarketingOT(); 
		Connection connection = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
	        ILocalGISGeoMarketingDAO geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();
	        String tableName = geoMarketingDAO.getNumHabitantsQuery(idEntidad,srid);
	        geoMarketingOT.setNumHabitants(geoMarketingDAO.getNumElements(connection,wktGeometry,srid,tableName));
	        tableName = geoMarketingDAO.getNumMalesQuery(idEntidad,srid);
	        geoMarketingOT.setNumMales(geoMarketingDAO.getNumElements(connection,wktGeometry,srid,tableName));
	        tableName = geoMarketingDAO.getNumFemalesQuery(idEntidad,srid);
	        geoMarketingOT.setNumFemales(geoMarketingDAO.getNumElements(connection,wktGeometry,srid,tableName));
	        Collections.sort(ranges);
	        ArrayList<RangeData> rangeList = analizeRanges(ranges);
	        geoMarketingDAO.buildRangeQuery(connection, wktGeometry, idEntidad, srid, rangeList);
	        geoMarketingOT.setRanges(rangeList.toArray(new RangeData[rangeList.size()]));
	        geoMarketingOT.setLevelStudies(geoMarketingDAO.getLevelStudies(connection,wktGeometry,idEntidad,srid));
	        geoMarketingOT.setSpanishHabitants(geoMarketingDAO.getSpanishHabitants(connection,wktGeometry,idEntidad,srid));
	        geoMarketingOT.setForeignHabitants(geoMarketingDAO.getForeignHabitants(connection,wktGeometry,idEntidad,srid));
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return geoMarketingOT;
	}
	private ArrayList<RangeData> analizeRanges(ArrayList<Integer> ranges) {
		Iterator<Integer> rangeIterator = ranges.iterator();
        ArrayList<RangeData> rangeList = new ArrayList<RangeData>(); 
        Integer prev = null;
        while(rangeIterator.hasNext()){
        	RangeData rango = new RangeData();
        	Integer value = rangeIterator.next();
        	if(rangeList.size() == 0){
        		if(value == 0){
        			rango.setStartRange(value);
        			if(rangeIterator.hasNext()){
        				rango.setEndRange(rangeIterator.next());
        			}
        		}else{
        			rango.setEndRange(value);
        		}
        	}else{
        		rango.setStartRange(prev);
        		rango.setEndRange(value);
        	}
        	prev = rango.getEndRange();
        	rangeList.add(rango);
        }
        RangeData finalRange = new RangeData();
        finalRange.setStartRange(prev);
        rangeList.add(finalRange);
        return rangeList;
	}
	public ArrayList<GeoMarketingOT> getGeomarketingDataFromIdLayer(Integer idLayer, String attributeName,String locale,Integer userId,Integer idEntidad,ArrayList<Integer> ranges) throws NamingException, ACException{
		Connection connection = null;
		NameDomainOT columnName = null;
		ArrayList<GeomarketingFeatureOT> features = null;
		ArrayList<GeoMarketingOT> results = new ArrayList<GeoMarketingOT>();
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        ILocalGISGeoMarketingDAO geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();
	        columnName = geoMarketingDAO.getColumnNameFromAttNameAndLocale(connection, attributeName, locale);
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.readLayer(connection, userId,idLayer))
	        	throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_GEOPISTA_LAYER_LEER + " perm not found");        
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
	        String layerQuery = geoMarketingDAO.getTableQuery(connection, idLayer, idEntidad, "4230");
	        features = geoMarketingDAO.getFeaturesFromLayerOrderedbyAttribute(connection, layerQuery, columnName.getColumnName());
	        Hashtable<String,String> codebookDomain = new Hashtable<String,String>();
			if(columnName.getDomainId() != null){
				codebookDomain = geoMarketingDAO.getCodeBookDomainData(connection, columnName.getDomainId(), locale);
			}
			Iterator<GeomarketingFeatureOT> it = features.iterator();
			while (it.hasNext()){
				GeomarketingFeatureOT feature = it.next();
				if(codebookDomain.get(feature.getAttributeName()) != null){
					feature.setAttributeName(codebookDomain.get(feature.getAttributeName()));
				}
			}
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		
        Iterator<GeomarketingFeatureOT> it = features.iterator();
        while(it.hasNext()){
        	GeomarketingFeatureOT feature = it.next();
        	GeoMarketingOT geomarketing = getGeomarketingDataByGeometry(ranges, feature.getWktGeometry(), "4230", idEntidad, userId);
        	geomarketing.setAttributeName(feature.getAttributeName());
        	geomarketing.setMunicipio(feature.getMunicipio());
        	geomarketing.setIdFeature(feature.getId());
        	results.add(geomarketing);
        }
		return results;
	}
	public ArrayList<GeoMarketingOT> getGeomarketingDataAndElementsFromIdLayer(Integer idLayer,Integer idLayerElements, String attributeName,String locale,Integer userId,Integer idEntidad,ArrayList<Integer> ranges) throws NamingException, ACException{
		Connection connection = null;
		ArrayList<GeomarketingFeatureOT> features = null;
		ArrayList<GeoMarketingOT> results = new ArrayList<GeoMarketingOT>();
		String layerQuery = null;
		NameDomainOT columnName = null;
		ILocalGISGeoMarketingDAO geoMarketingDAO = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();
	        columnName = geoMarketingDAO.getColumnNameFromAttNameAndLocale(connection, attributeName, locale);
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.readLayer(connection, userId,idLayer))
	        	throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_GEOPISTA_LAYER_LEER + " perm not found");        
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
	        layerQuery = geoMarketingDAO.getTableQuery(connection, idLayer, idEntidad, "4230");
	        features = geoMarketingDAO.getFeaturesFromLayerOrderedbyAttribute(connection, layerQuery, columnName.getColumnName());
	        Hashtable<String,String> codebookDomain = new Hashtable<String,String>();
			if(columnName.getDomainId() != null){
				codebookDomain = geoMarketingDAO.getCodeBookDomainData(connection, columnName.getDomainId(), locale);
			}
			Iterator<GeomarketingFeatureOT> it = features.iterator();
			while (it.hasNext() && codebookDomain.size()>0){
				GeomarketingFeatureOT feature = it.next();
				if(feature.getAttributeName() != null && codebookDomain.get(feature.getAttributeName()) != null){
					feature.setAttributeName(codebookDomain.get(feature.getAttributeName()));
				}
			}
		}catch (SQLException e){
			logger.warn("Error reading warning list. Trying commas to find columnName",e);
			try {
				features = geoMarketingDAO.getFeaturesFromLayerOrderedbyAttribute(connection, layerQuery, "\""+columnName.getColumnName()+"\"");
			} catch (SQLException e1) {
				logger.error("Error reading warning list",e);
			}
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
        Iterator<GeomarketingFeatureOT> it = features.iterator();
        int i = 0;
        while(it.hasNext()){
        	System.out.println("Processing " + ++i + " Elements");
        	GeomarketingFeatureOT feature = it.next();
        	GeoMarketingOT geomarketing = getGeomarketingDataByGeometry(ranges, feature.getWktGeometry(), "4230", idEntidad, userId);
        	geomarketing.setExternalValue(getNumElementsByGeometry(feature.getWktGeometry(), "4230", idLayerElements, idEntidad, userId));
        	geomarketing.setAttributeName(feature.getAttributeName());
        	geomarketing.setMunicipio(feature.getMunicipio());
        	geomarketing.setIdFeature(feature.getId());
        	results.add(geomarketing);
        }
		return results;
	}
	public ArrayList<PortalStepRelationOT> getRelatedPortalsByIdVia(Integer userId,Integer idEntidad,Integer idFeatureStreet,ArrayList<Integer> idFeatureStep) throws NamingException, ACException{
		Connection connection = null;
		ArrayList<PortalStepRelationOT> relations = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        ILocalGISGeoMarketingDAO geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();

	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
            relations = geoMarketingDAO.getNearestStepFromPortal(connection, idEntidad, idFeatureStreet, idFeatureStep);
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
        
		return relations;
	}
	public ArrayList<PostalDataOT> getPostalDataFromTramo(Integer userId,Integer idEntidad,Integer idFeatureStreet,ArrayList<Integer> idFeatureStep) throws NamingException, ACException{
		Connection connection = null;
		ArrayList<PostalDataOT> results = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        ILocalGISGeoMarketingDAO geoMarketingDAO = daoFactory.getLocalGISGeoMarketingDAO();

	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
	        if(!userValidationDAO.obtenerDatosGenerales(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES + " perm not found");
            ArrayList<PortalStepRelationOT> relations = geoMarketingDAO.getNearestStepFromPortal(connection, idEntidad, idFeatureStreet, idFeatureStep);
            
            results = geoMarketingDAO.getPostalDataFromIdPortales(connection, getIdPortalFromObject(relations));
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
        
		return results;
	}
	private ArrayList<Integer> getIdPortalFromObject(
			ArrayList<PortalStepRelationOT> relations) {
		ArrayList<Integer> idPortales = new ArrayList<Integer>();
		Iterator<PortalStepRelationOT> objects = relations.iterator();
		while (objects.hasNext()){
			idPortales.add(objects.next().getIdPortal());
		}
		return idPortales;
	}
}
