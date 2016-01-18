package com.localgis.webservices.civilwork.ln;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import javax.naming.ConfigurationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISNote;
import com.localgis.webservices.civilwork.model.dao.DAOFactory;
import com.localgis.webservices.civilwork.model.dao.ILocalGISNotesDAO;
import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISNotesLN {

	public ArrayList<LocalGISNote> getLocalGISNotesList(String description, Calendar from, Calendar to, Integer startElement,Integer range,Integer idMunicipio,Integer userId,ArrayList<OrderByColumn> orderColumns,ArrayList<LayerFeatureBean> features) throws ACException, NamingException {
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Connection connection = null;
		ArrayList<LocalGISNote> notes = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_CONSULTA + " perm not found");
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			notes = notesDAO.getNoteList(connection, description, from, to, startElement, range, idMunicipio, userId, orderColumns, features);
			Iterator<LocalGISNote> it = notes.iterator();
			while (it.hasNext()){
				LocalGISNote note = it.next();
				//ArrayList<Document> documents = notesDAO.getNoteDocuments(connection, note.getId(), false);
				//note.setListaDeDocumentos(documents.toArray(new Document[documents.size()]));
				ArrayList<LayerFeatureBean> layerFeatureBeans = notesDAO.getNoteLayerFeatureReferences(connection, note.getId());
				if(layerFeatureBeans != null && layerFeatureBeans.size()>0)
					note.setFeatureRelation(layerFeatureBeans.toArray(new LayerFeatureBean[layerFeatureBeans.size()]));
			}
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return notes;
	}
	public int getNumNotes(String description, Calendar from, Calendar to,Integer idMunicipio,Integer userId,ArrayList<LayerFeatureBean> features) throws ACException, NamingException {
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Connection connection = null;
		int numAvisos = 0;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_CONSULTA + " perm not found");
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			numAvisos = notesDAO.getNumNotes(connection, description, from, to, idMunicipio, userId, features);


		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return numAvisos;
	}
	@SuppressWarnings("unchecked")
	public void addNote(LocalGISNote warning,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("Inserting warning");
		Connection connection = null;
		Boolean autoCommit = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			autoCommit = connection.getAutoCommit();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesNew(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_ALTA + " perm not found");
			connection.setAutoCommit(false);
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			warning.setId(notesDAO.getNextIdNoteSequence(connection));
			notesDAO.addNote(connection, warning);

			if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0)
				notesDAO.addNoteLayerFeatureReferences(connection, new ArrayList(Arrays.asList(warning.getFeatureRelation())), warning.getId());
			/*if(warning.getListaDeDocumentos() != null && warning.getListaDeDocumentos().length>0){
				for(int i = 0;i<warning.getListaDeDocumentos().length;i++){
					Document document = warning.getListaDeDocumentos()[i];
					document.setIdDocumento(notesDAO.getNextIdDocumentNoteSequence(connection));
					if(document.getTipo().equals(DocumentTypes.IMAGEN)){
						document.setThumbnail(GeneralUtilities.buildThumbnail(document.getFichero()));
						notesDAO.addNoteDocumentThumbnail(connection, document);
					}
				}
			}*/
			connection.commit();
			logger.error("Commit successfull!");
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

	public void removeNote(Integer idWarning,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("delete warning");
		Connection connection = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesDelete(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_ELIMINAR + " perm not found");
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			notesDAO.removeNote(connection, idWarning);
		}catch (SQLException e){
			logger.error("Error deleting warning",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
	}
	public LocalGISNote getNote(Integer idWarning,Boolean returnCompleteDocuments,Integer municipioId,Integer userId ) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("get warning");
		Connection connection = null;
		LocalGISNote warning = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_CONSULTA + " perm not found");
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			try {
				warning = notesDAO.getNote(connection, idWarning, municipioId, userId);
			} catch (ConfigurationException e) {
				logger.error("Error obtaining note.",e);
			}
			if(warning != null){
				//ArrayList<Document> documents = notesDAO.getNoteDocuments(connection, warning.getId(), true);

				//warning.setListaDeDocumentos(documents.toArray(new Document[documents.size()]));
				ArrayList<LayerFeatureBean> layerFeatureBeans = notesDAO.getNoteLayerFeatureReferences(connection, idWarning);
				if(layerFeatureBeans != null && layerFeatureBeans.size()>0)
					warning.setFeatureRelation(layerFeatureBeans.toArray(new LayerFeatureBean[layerFeatureBeans.size()]));
			}
		}catch (SQLException e){
			logger.error("Error deleting warning",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return warning;
	}

	@SuppressWarnings("unchecked")
	public void setNote(LocalGISNote warning,Integer userId) throws ACException, NamingException{

		if(logger.isDebugEnabled())logger.debug("Modifying warning");
		Connection connection = null;
		Boolean autoCommit = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			autoCommit = connection.getAutoCommit();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.notesNew(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_ALTA + " perm not found");
			connection.setAutoCommit(false);
			ILocalGISNotesDAO notesDAO = daoFactory.getLocalGISNotesDAO();
			notesDAO.setNote(connection, warning);
			notesDAO.removeNoteLayerFeatureReferences(connection, warning.getId());
			//notesDAO.removeNoteDocuments(connection, warning.getId());
			if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0)
				notesDAO.addNoteLayerFeatureReferences(connection, new ArrayList(Arrays.asList(warning.getFeatureRelation())), warning.getId());
			/*if(warning.getListaDeDocumentos() != null && warning.getListaDeDocumentos().length>0){
				for(int i = 0;i<warning.getListaDeDocumentos().length;i++){
					Document document = warning.getListaDeDocumentos()[i];
					document.setIdDocumento(notesDAO.getNextIdDocumentNoteSequence(connection));
					if(document.getTipo().equals(DocumentTypes.IMAGEN)){
						document.setThumbnail(GeneralUtilities.buildThumbnail(document.getFichero()));
						notesDAO.addNoteDocumentThumbnail(connection, document);
					}
					notesDAO.addNoteDocument(connection, warning.getId(), document);
				}
			}*/
			connection.commit();
			logger.error("Commit successfull!");
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



	private static Logger logger = Logger.getLogger(LocalGISNotesLN.class);
}
