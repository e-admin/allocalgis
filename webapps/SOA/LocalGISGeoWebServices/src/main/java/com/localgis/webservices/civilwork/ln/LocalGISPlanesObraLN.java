/**
 * LocalGISPlanesObraLN.java
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
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.app.gestionciudad.beans.LayerFeatureBean;
import com.localgis.app.gestionciudad.beans.LocalGISPlanesObra;
import com.localgis.webservices.civilwork.model.dao.DAOFactory;
import com.localgis.webservices.civilwork.model.dao.ILocalGISNotesDAO;
import com.localgis.webservices.civilwork.model.dao.ILocalGISPlanesObraDAO;
import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class LocalGISPlanesObraLN {

	public ArrayList<LocalGISPlanesObra> getPlanesObraList(String description, Calendar fromStart, Calendar toStart,Calendar fromAprobacion,
			Calendar toAprobacion, Integer startElement, Integer range, Integer idMunicipio, Integer userId,
			ArrayList<OrderByColumn> orderColumns, ArrayList<LayerFeatureBean> features, 
			String plan, String anios, String nombre, String paraje, Double presupuestoEstimado, 
			Double presupuestoDefinitivo, String existeProyecto, String infraestructura, String obraNueva, 
			String estudioAmbiental, String datosEIEL, String permisos, String personaContacto, 
			String telefonoContacto, String[] documentosAdjuntos, 
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar toFechaRecibi, Calendar fromFechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra, String empresaAdjudicataria, 
			Calendar toFechaResolucion, Calendar fromFechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar toActaReplanteo, Calendar fromActaReplanteo, 
			Calendar toFechaComienzo,  Calendar fromFechaComienzo, 	Calendar toFechaFinalizacion, 
			Calendar fromFechaFinalizacion, Calendar toProrrogas, Calendar fromProrrogas, 
			Calendar toActaRecepcion, Calendar fromActaRecepcion, Double certificacionFinal, 
			Calendar toResolucionCertificacion, Calendar fromResolucionCertificacion, 
			Calendar toInformacionCambiosEIEL, Calendar fromInformacionCambiosEIEL, Calendar toReformados, 
			Calendar fromReformados, Double liquidacion, Calendar toFechaLiquidacion, 
			Calendar fromFechaLiquidacion, String detalles, Calendar toFechaSolicitud, Calendar fromFechaSolicitud, 
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws ACException, NamingException {
		if(logger.isDebugEnabled())logger.debug("reading warning list");
		Connection connection = null;
		ArrayList<LocalGISPlanesObra> planesObra = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.planesObraQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_CONSULTA + " perm not found");
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			planesObra = planesObraDAO.getPlanesObraList(connection, description, fromStart, toStart, fromAprobacion, toAprobacion, startElement, range, 
					idMunicipio, userId, orderColumns, features, plan, anios, nombre, paraje, presupuestoEstimado, presupuestoDefinitivo, existeProyecto, 
					infraestructura, obraNueva, estudioAmbiental, datosEIEL, permisos, personaContacto, telefonoContacto, documentosAdjuntos, serviciosAfectados, 
					destinatario, recibi, toFechaRecibi, fromFechaRecibi, supervision, directorProyecto, autorProyecto, directorObra, empresaAdjudicataria, 
					toFechaResolucion, fromFechaResolucion, presupuestoAdjudicacion, coordinadorSegSalud, toActaReplanteo, fromActaReplanteo, 
					toFechaComienzo, fromFechaComienzo, toFechaFinalizacion, fromFechaFinalizacion, toProrrogas, fromProrrogas, 
					toActaRecepcion, fromActaRecepcion, certificacionFinal, toResolucionCertificacion, fromResolucionCertificacion, 
					toInformacionCambiosEIEL, fromInformacionCambiosEIEL, toReformados, fromReformados, liquidacion, toFechaLiquidacion, 
					fromFechaLiquidacion, detalles, toFechaSolicitud, fromFechaSolicitud, 
					toFechaAprobacionPrincipado, fromFechaAprobacionPrincipado, idEntidad);
			Iterator<LocalGISPlanesObra> it = planesObra.iterator();
			while (it.hasNext()){
				LocalGISPlanesObra planObra = it.next();
				//ArrayList<Document> documents = notesDAO.getNoteDocuments(connection, note.getId(), false);
				//note.setListaDeDocumentos(documents.toArray(new Document[documents.size()]));
				ArrayList<LayerFeatureBean> layerFeatureBeans = planesObraDAO.getPlanesObraLayerFeatureReferences(connection, planObra.getId());
				if(layerFeatureBeans != null && layerFeatureBeans.size()>0)
					planObra.setFeatureRelation(layerFeatureBeans.toArray(new LayerFeatureBean[layerFeatureBeans.size()]));
			}
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return planesObra;
	}
	public int getNumPlanesObra(String description, Calendar fromStart, Calendar toStart,
			Integer startElement, Integer idMunicipio, Integer userId,	ArrayList<LayerFeatureBean> features, 
			Calendar fromAprobacion, Calendar toAprobacion, String plan, String anios, String nombre, String paraje, Double presupuestoEstimado, 
			Double presupuestoDefinitivo, String existeProyecto, String infraestructura, String obraNueva, 
			String estudioAmbiental, String datosEIEL, String permisos, String personaContacto, 
			String telefonoContacto, String[] documentosAdjuntos, 
			String[] serviciosAfectados, String destinatario, String recibi,
			Calendar toFechaRecibi, Calendar fromFechaRecibi, String supervision, String directorProyecto,
			String autorProyecto, String directorObra, String empresaAdjudicataria, 
			Calendar toFechaResolucion, Calendar fromFechaResolucion, Double presupuestoAdjudicacion,
			String coordinadorSegSalud, Calendar toActaReplanteo, Calendar fromActaReplanteo, 
			Calendar toFechaComienzo,  Calendar fromFechaComienzo, 	Calendar toFechaFinalizacion, 
			Calendar fromFechaFinalizacion, Calendar toProrrogas, Calendar fromProrrogas, 
			Calendar toActaRecepcion, Calendar fromActaRecepcion, Double certificacionFinal, 
			Calendar toResolucionCertificacion, Calendar fromResolucionCertificacion, 
			Calendar toInformacionCambiosEIEL, Calendar fromInformacionCambiosEIEL, Calendar toReformados, 
			Calendar fromReformados, Double liquidacion, Calendar toFechaLiquidacion, 
			Calendar fromFechaLiquidacion, String detalles, Calendar toFechaSolicitud, Calendar fromFechaSolicitud, 
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws ACException, NamingException {
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
				if(!userValidationDAO.planesObraQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_CONSULTA + " perm not found");
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			numAvisos = planesObraDAO.getNumPlanesObra(connection, description, fromStart, toStart, startElement, idMunicipio, userId, features, fromAprobacion, toAprobacion, 
					plan, anios, nombre, paraje, presupuestoEstimado, presupuestoDefinitivo, existeProyecto, infraestructura, obraNueva, 
					estudioAmbiental, datosEIEL, permisos, personaContacto, telefonoContacto, documentosAdjuntos, 
					serviciosAfectados, destinatario, recibi, toFechaRecibi, fromFechaRecibi, supervision, directorProyecto,
					autorProyecto, directorObra, empresaAdjudicataria, toFechaResolucion, fromFechaResolucion, presupuestoAdjudicacion,
					coordinadorSegSalud, toActaReplanteo, fromActaReplanteo, toFechaComienzo, fromFechaComienzo, toFechaFinalizacion, 
					fromFechaFinalizacion, toProrrogas, fromProrrogas, toActaRecepcion, fromActaRecepcion, certificacionFinal, 
					toResolucionCertificacion, fromResolucionCertificacion, toInformacionCambiosEIEL, fromInformacionCambiosEIEL, 
					toReformados, fromReformados, liquidacion, toFechaLiquidacion, fromFechaLiquidacion, detalles, toFechaSolicitud, fromFechaSolicitud, 
					toFechaAprobacionPrincipado, fromFechaAprobacionPrincipado, idEntidad);

		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return numAvisos;
	}
	@SuppressWarnings("unchecked")
	public void addPlanesObra(LocalGISPlanesObra warning,Integer userId) throws ACException, NamingException{
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
				if(!userValidationDAO.planesObraNew(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ALTA + " perm not found");
			connection.setAutoCommit(false);
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			warning.setId(planesObraDAO.getNextIdPlanesObraSequence(connection));
			planesObraDAO.addPlanesObra(connection, warning);
			planesObraDAO.addPlanesObraSpecificInfo(connection, warning);
			if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0)
				planesObraDAO.addPlanesObraLayerFeatureReferences(connection, new ArrayList(Arrays.asList(warning.getFeatureRelation())), warning.getId());
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

	public void removePlanesObra(Integer idWarning,Integer userId) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("delete warning");
		Connection connection = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.planesObraDelete(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ELIMINAR + " perm not found");
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			planesObraDAO.removePlanesObra(connection, idWarning);
		}catch (SQLException e){
			logger.error("Error deleting warning",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
	}
	public LocalGISPlanesObra getPlanesObra(Integer idWarning,Boolean returnCompleteDocuments,Integer municipioId,Integer userId, Integer idEntidad) throws ACException, NamingException{
		if(logger.isDebugEnabled())logger.debug("get warning");
		Connection connection = null;
		LocalGISPlanesObra warning = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
			if(logger.isDebugEnabled())logger.debug("Opening connection");
			connection = daoFactory.getConnection();
			if(logger.isDebugEnabled())logger.debug("Test User Perm");
			IUserValidationDAO userValidationDAO = new UserValidationDAO();
			if(!userValidationDAO.administration(connection, userId))
				if(!userValidationDAO.planesObraQuery(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_CONSULTA + " perm not found");
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			try {
				warning = planesObraDAO.getPlanesObra(connection, idWarning, municipioId, userId, idEntidad);
			} catch (ConfigurationException e) {
				logger.error("Error obtaining note.",e);
			}
			if(warning != null){
				//ArrayList<Document> documents = notesDAO.getNoteDocuments(connection, warning.getId(), true);

				//warning.setListaDeDocumentos(documents.toArray(new Document[documents.size()]));
				ArrayList<LayerFeatureBean> layerFeatureBeans = planesObraDAO.getPlanesObraLayerFeatureReferences(connection, idWarning);
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
	public void setPlanesObra(LocalGISPlanesObra warning,Integer userId) throws ACException, NamingException{

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
				if(!userValidationDAO.planesObraNew(connection, userId))
					throw new ACException(UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ALTA + " perm not found");
			connection.setAutoCommit(false);
			ILocalGISPlanesObraDAO planesObraDAO = daoFactory.getLocalGISPlanesObraDAO();
			planesObraDAO.setPlanesObra(connection, warning);
			planesObraDAO.setPlanesObraSpecificInfo(connection, warning);
			planesObraDAO.removePlanesObraLayerFeatureReferences(connection, warning.getId());
			//notesDAO.removeNoteDocuments(connection, warning.getId());
			if(warning.getFeatureRelation() != null && warning.getFeatureRelation().length > 0)
				planesObraDAO.addPlanesObraLayerFeatureReferences(connection, new ArrayList(Arrays.asList(warning.getFeatureRelation())), warning.getId());
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



	private static Logger logger = Logger.getLogger(LocalGISPlanesObraLN.class);
}
