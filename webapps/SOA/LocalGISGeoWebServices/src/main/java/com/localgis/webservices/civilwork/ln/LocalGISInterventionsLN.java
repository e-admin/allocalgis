/**
 * LocalGISInterventionsLN.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.ln;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.app.gestionciudad.beans.Document;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.types.DocumentTypes;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.webservices.civilwork.model.dao.DAOFactory;
import com.localgis.webservices.civilwork.model.dao.ILocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.GeneralUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISInterventionsLN {

	public ArrayList<LocalGISIntervention> getInterventionList(String description, String associatedAction, Calendar fromStart, Calendar toStart,Calendar fromNext,Calendar toNext, Integer startElement,Integer range,Integer idMunicipio,Integer userId,String actuationType,String interventionType,Double foreseenBudgetFrom, Double foreseenBudgetTo,Double workPercentageFrom,Double workPercentageTo,String causes,ArrayList<LayerFeatureBean> features,ArrayList<OrderByColumn> orderColumns) throws ACException, NamingException {
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Connection connection = null;
		ArrayList<LocalGISIntervention> avisos = null;
        try {
        	Boolean isAdministrator = false;
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId)){
            	if(!userValidationDAO.warningsQuery(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA + " perm not found");
            }else{
            	isAdministrator = true;
            }
            ILocalGISInterventionsDAO interventionsDAO = daoFactory.getLocalGISInterventionsDAO();
            avisos = interventionsDAO.getInterventionList(connection, description, fromStart, toStart, fromNext, toNext, startElement, range, idMunicipio, userId, orderColumns, features, actuationType, interventionType, foreseenBudgetFrom, foreseenBudgetTo, workPercentageFrom, workPercentageTo, causes, isAdministrator);
            Iterator<LocalGISIntervention> it = avisos.iterator();
            while (it.hasNext()){
            	LocalGISIntervention aviso = it.next();
            	ArrayList<Document> documents = interventionsDAO.getInterventionDocuments(connection, aviso.getId(), false);
            	//TODO: Comprobar que almacena aunque no tenga documentos.
            	if(documents.size() > 0 )
            		aviso.setListaDeDocumentos(documents.toArray(new Document[documents.size()]));
            	ArrayList<LayerFeatureBean> layerFeatureBeans = interventionsDAO.getInterventionLayerFeatureReferences(connection, aviso.getId());
            	if(layerFeatureBeans != null && layerFeatureBeans.size()>0){
            		aviso.setFeatureRelation(layerFeatureBeans.toArray(new LayerFeatureBean[layerFeatureBeans.size()]));
            		aviso.setIncidentNetworkType(interventionsDAO.getNetworkIncidentType(connection,aviso.getId()));
            	}
            }
        }catch (SQLException e){
        	logger.error("Error reading warning list",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
        return avisos;
	}
	public int getNumInterventions(String description, String associatedAction, Calendar fromStart, Calendar toStart,Calendar fromNext,Calendar toNext, Integer startElement,Integer range,Integer idMunicipio,Integer userId,String actuationType,String interventionType,Double foreseenBudgetFrom, Double foreseenBudgetTo,Double workPercentageFrom,Double workPercentageTo,String causes,ArrayList<LayerFeatureBean> features) throws ACException, NamingException {
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Connection connection = null;
		int avisos = 0;
        try {
        	Boolean isAdministrator = false;
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId)){
            	if(!userValidationDAO.warningsQuery(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA + " perm not found");
            }else{
            	isAdministrator = true;
            }
            ILocalGISInterventionsDAO interventionsDAO = daoFactory.getLocalGISInterventionsDAO();
            avisos = interventionsDAO.getNumInterventions(connection, description, fromStart, toStart, fromNext, toNext, idMunicipio, userId, features, actuationType, interventionType, foreseenBudgetFrom, foreseenBudgetTo, workPercentageFrom, workPercentageTo, causes, isAdministrator);
            
        }catch (SQLException e){
        	logger.error("Error reading warning list",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
        return avisos;
	}
	public void addIntervention(LocalGISIntervention warning,Integer userId,Integer sendToNetwork) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("Inserting warning");
		Connection connection = null;
		Boolean autoCommit = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            autoCommit = connection.getAutoCommit();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsNew(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_ALTA + " perm not found");
            connection.setAutoCommit(false);
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            warning.setId(warningsDAO.getNextIdInterventionSequence(connection));
            warningsDAO.addIntervention(connection, warning);
            warningsDAO.addInterventionSpecificInfo(connection, warning);
            

            if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0){
            	warningsDAO.addInterventionLayerFeatureReferences(connection, new ArrayList<LayerFeatureBean>(Arrays.asList(warning.getFeatureRelation())), warning.getId());
            	Integer idNetwork = warningsDAO.getNetworkId(connection,getPropertyNetworkName());
                if(idNetwork != null){
                	if((warning.getStartWarning() != null || (warning.getPattern() != null && !warning.getPattern().equals(""))) && idNetwork != null && sendToNetwork > 0 && sendToNetwork < 3){
                    	for(int i = 0;i<warning.getFeatureRelation().length;i++){
                    		Integer idEdge = warningsDAO.getEdgeFromFeature(connection,idNetwork,warning.getFeatureRelation()[i]);
                    		if(idEdge != null){
                    			try{
                    				LocalGISIncident incident = getIncident(warning.getStartWarning(),warning.getEndedWork(),warning.getPattern());
                    				warningsDAO.addNetworkIncident(connection,idNetwork,idEdge,warning.getId(),incident,warning.getDescription(),sendToNetwork);
                    			}catch(ConfigurationException e){
                    				logger.error("Error pattern trasnform. Network incident not inserted!",e);
                    			}
                    		}
                    	}
                    }
                }
            }
            /*if(warning.getListaDeDocumentos() != null && warning.getListaDeDocumentos().length>0){
            	for(int i = 0;i<warning.getListaDeDocumentos().length;i++){
            		Document document = warning.getListaDeDocumentos()[i];
            		document.setIdDocumento(warningsDAO.getNextIdInterventionSequence(connection));
            		if(document.getTipo().equals(DocumentTypes.IMAGEN)){
            			document.setThumbnail(GeneralUtilities.buildThumbnail(document.getFichero()));
            			warningsDAO.addInterventionDocumentThumbnail(connection, document);
            			}
            	}
            }*/
        } catch (SQLException e) {
        	logger.error("Insert warning error",e);
        	try {
        		logger.error("Starting rollback");
				connection.rollback();
				logger.error("Rollback successfull!");
			} catch (SQLException e1) {
				logger.error("Rollback error",e1);
			}
			
		} catch (ConfigurationException e) {
			logger.error(e);
		}finally {
			try {
				connection.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				logger.error("Error setting AutoCommit",e);
			}
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
	}

	private LocalGISIncident getIncident(Calendar startWarning,Calendar endedWork, String pattern) throws ConfigurationException  {
		LocalGISIncident incident = null;
		if(pattern == null || pattern.equals("")){
			incident = new LocalGISIncident();
		}else{
			incident = new LocalGISIncident(pattern);
		}
		if(startWarning != null)
			incident.setDateStart(startWarning.getTime());
		if(endedWork != null){
			incident.setDateEnd(endedWork.getTime());
		}
		return incident;
	}
	private String getPropertyNetworkName() {
		String networkName = null;
        Context initCtx;
		try {
			initCtx = new InitialContext( );
			networkName = (String) initCtx.lookup("java:comp/env/networkName");
		} catch (NamingException e) {
			logger.error("Error reading networkName",e);
		}
        	
        
		return networkName;
	}
	public void setWarning(LocalGISIntervention warning,Integer userId,Integer sendToNetwork) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("Updating warning");
		Connection connection = null;
		Boolean autoCommit = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            autoCommit = connection.getAutoCommit();
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsModify(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_MODIFICACION + " perm not found");
            
            connection.setAutoCommit(false);
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            warningsDAO.setIntervention(connection, warning);
            warningsDAO.setInterventionSpecificInfo(connection, warning);
            //warningsDAO.removeUserWarnings(connection, warning.getIdAviso());
            warningsDAO.removeInterventionLayerFeatureReferences(connection, warning.getId());
            if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0){
            	warningsDAO.addInterventionLayerFeatureReferences(connection, new ArrayList<LayerFeatureBean>(Arrays.asList(warning.getFeatureRelation())), warning.getId());
            	Integer idNetwork = warningsDAO.getNetworkId(connection,getPropertyNetworkName());
                if(idNetwork != null){
                	if((warning.getStartWarning() != null || (warning.getPattern() != null && !warning.getPattern().equals(""))) && idNetwork != null && sendToNetwork > 0 && sendToNetwork < 3){
                    	for(int i = 0;i<warning.getFeatureRelation().length;i++){
                    		Integer idEdge = warningsDAO.getEdgeFromFeature(connection,idNetwork,warning.getFeatureRelation()[i]);
                    		if(idEdge != null){
                    			try{
                    				LocalGISIncident incident = getIncident(warning.getStartWarning(),warning.getEndedWork(),warning.getPattern());
                    				warningsDAO.addNetworkIncident(connection,idNetwork,idEdge,warning.getId(),incident,warning.getDescription(),sendToNetwork);
                    			}catch(ConfigurationException e){
                    				logger.error("Error pattern trasnform. Network incident not inserted!",e);
                    			}
                    		}
                    	}
                    }
                }
            }
            //warningsDAO.removeInterventionDocuments(connection, warning.getId());
//            warningsDAO.addUserWarnings(connection, warning, users);
            /*if(warning.getListaDeDocumentos() != null && warning.getListaDeDocumentos().length>0){
            	for(int i = 0;i<warning.getListaDeDocumentos().length;i++){
            		Document document = warning.getListaDeDocumentos()[i];
            		document.setIdDocumento(warningsDAO.getNextIdDocumentInterventionSequence(connection));
            		if(document.getTipo() == null)document.setTipo(DocumentTypes.PDF);
            		if(document.getTipo().equals(DocumentTypes.IMAGEN)){
            			document.setThumbnail(GeneralUtilities.buildThumbnail(document.getFichero()));
            			warningsDAO.addInterventionDocumentThumbnail(connection, document);
            		}
            		warningsDAO.addInterventionDocument(connection, warning.getId(), document);
            	}
            }*/
        } catch (SQLException e) {
        	logger.error("Insert warning error",e);
        	try {
        		logger.error("Starting rollback");
				connection.rollback();
				logger.error("Rollback successfull!");
			} catch (SQLException e1) {
				logger.error("Rollback error",e1);
			}
			
		}finally {
			try {
				connection.setAutoCommit(autoCommit);
			} catch (SQLException e) {
				logger.error("Error setting AutoCommit",e);
			}
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
	}
	public void removeWarning(Integer idWarning,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("delete warning");
		Connection connection = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsDelete(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_ELIMINAR + " perm not found");
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            warningsDAO.removeIntervention(connection, idWarning);
            Integer idNetwork = warningsDAO.getNetworkId(connection,getPropertyNetworkName());
            if(idNetwork != null)
            	warningsDAO.removeNetworkIncident(connection,idNetwork,idWarning);
        }catch (SQLException e){
        	logger.error("Error deleting warning",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
	}
	public LocalGISIntervention getWarning(Integer idWarning,Boolean returnCompleteDocuments,Integer municipioId,Integer userId ) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("get warning");
		Connection connection = null;
		LocalGISIntervention warning = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsQuery(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA + " perm not found");
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            warning = warningsDAO.getIntervention(connection, idWarning, municipioId, userId);
            if(warning != null){
            	ArrayList<Document> documentos = warningsDAO.getInterventionDocuments(connection, idWarning, true);
            	warning.setListaDeDocumentos(documentos.toArray(new Document[documentos.size()]));
            	ArrayList<LayerFeatureBean> layerFeatureBeans = warningsDAO.getInterventionLayerFeatureReferences(connection, warning.getId());
            	if(layerFeatureBeans != null && layerFeatureBeans.size()>0){
            		warning.setFeatureRelation(layerFeatureBeans.toArray(new LayerFeatureBean[layerFeatureBeans.size()]));
            		warning.setIncidentNetworkType(warningsDAO.getNetworkIncidentType(connection,warning.getId()));
            	}
            }
        }catch (SQLException e){
        	logger.error("Error deleting warning",e);
        } catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
        return warning;
	}
	public void changeWarningDate(Integer idWarning,Calendar calendar,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("Postpone warning");
		Connection connection = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsPostpone(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_POSTPONER + " perm not found");
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            //TODO: Generar metodo para cambiar fecha 
            warningsDAO.changeWarningDate(connection, idWarning, calendar);
        }catch (SQLException e){
        	logger.error("Error deleting warning",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
	}
	public void cancelWarning(Integer idWarning,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("Cancel warning");
		Connection connection = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsDiscard(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_DESCARTAR + " perm not found");
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            //TODO: Generar metodo para cancelar 
            warningsDAO.changeWarningDate(connection, idWarning,null);
        }catch (SQLException e){
        	logger.error("Error deleting warning",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        }
	}
	public ArrayList<StatisticalDataOT> getStatistics(Integer userId,Integer idEntidad) throws NamingException, ACException{
		if(logger.isDebugEnabled())logger.debug("Cancel warning");
		Connection connection = null;
		ArrayList<StatisticalDataOT> statistics = null;
        try {
            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            if(logger.isDebugEnabled())logger.debug("Opening connection");
            connection = daoFactory.getConnection();
            if(logger.isDebugEnabled())logger.debug("Test User Perm");
            IUserValidationDAO userValidationDAO = new UserValidationDAO();
            if(!userValidationDAO.administration(connection, userId))
            	if(!userValidationDAO.warningsQuery(connection, userId))
            		throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA + " perm not found");
            ILocalGISInterventionsDAO warningsDAO = daoFactory.getLocalGISInterventionsDAO();
            statistics = warningsDAO.getStatistics(connection, idEntidad);
        }catch (SQLException e){
        	logger.error("Error generating statistics",e);
        }finally {
            ConnectionUtilities.closeConnection(connection);
            if(logger.isDebugEnabled())logger.debug("Closing connection");
        } 
        return statistics;
	}
	private static Logger logger = Logger.getLogger(LocalGISInterventionsLN.class);
}
