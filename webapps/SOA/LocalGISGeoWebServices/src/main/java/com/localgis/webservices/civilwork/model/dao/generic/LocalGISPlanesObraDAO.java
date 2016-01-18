/**
 * LocalGISPlanesObraDAO.java
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
import com.localgis.app.gestionciudad.beans.LocalGISPlanesObra;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.webservices.civilwork.model.dao.ILocalGISPlanesObraDAO;
import com.localgis.webservices.civilwork.model.ot.StatisticalDataOT;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.InterventionColumn;
import com.localgis.webservices.civilwork.util.OrderByColumn;
import com.localgis.webservices.civilwork.util.PlanesObraColumn;

public class LocalGISPlanesObraDAO implements ILocalGISPlanesObraDAO{
	
	private static Logger logger = Logger.getLogger(LocalGISPlanesObraDAO.class);
	
	@Override
	public void addPlanesObra(Connection connection, LocalGISPlanesObra warning) throws SQLException {
		
		logger.debug("Adding PlanesObra");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addWarning(connection, warning);
		
	}
	
	@Override
	public void addPlanesObraLayerFeatureReferences(Connection connection,
			ArrayList<LayerFeatureBean> layerFeatureBean, Integer idWarning)
			throws SQLException {
		logger.debug("Adding PlanesObra Layer Feature References");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.addLayerFeatureReferences(connection, layerFeatureBean, idWarning);
		
	}
	
	@Override
	public void addPlanesObraDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void addPlanesObraDocument(Connection connection, Integer idWarning,
			Document document) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public LocalGISPlanesObra getPlanesObra(Connection connection, Integer idWarning,
			Integer idMunicipio, Integer idUser, Integer idEntidad) throws SQLException, ConfigurationException {
		logger.debug("Obtaining PlanesObra");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		LocalGISPlanesObra planesObra = null;
		LocalGISNote note = dao.getWarning(connection, idWarning, idMunicipio, idUser, idEntidad);
		if(note != null)
			planesObra = new LocalGISPlanesObra(note);
		return planesObra;
	}

	@Override
	public Document getPlanesObraDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<Document> getPlanesObraDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<LayerFeatureBean> getPlanesObraLayerFeatureReferences(
			Connection connection, Integer idWarning) throws SQLException {
		logger.debug("Obtaining layer feature references");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		return dao.getLayerFeatureReferences(connection, idWarning);
	}

	@Override
	public void removePlanesObra(Connection connection, Integer idWarning)
			throws SQLException {
		logger.debug("Deleting PlanesObra");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarning(connection, idWarning);
		
	}

	@Override
	public void removePlanesObraDocument(Connection connection, Integer idDocument)
			throws SQLException {
		logger.debug("Deleting document");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeDocument(connection, idDocument);
		
	}

	@Override
	public void removePlanesObraDocuments(Connection connection, Integer idWarning)
			throws SQLException {
		logger.debug("Deleting all PlanesObra documents");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeWarningDocuments(connection, idWarning);
		
	}

	@Override
	public void removePlanesObraLayerFeatureReferences(Connection connection,
			Integer idWarning) throws SQLException {
		logger.debug("Deleting feature references from PlanesObra");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.removeLayerFeatureReferences(connection, idWarning);
		
	}

	@Override
	public void setPlanesObra(Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException {
		logger.debug("setting PlanesObra");
		LocalGISGenericDAO dao = new LocalGISGenericDAO();
		dao.setWarning(connection, planesObra);
		
	}

	@Override
	public ArrayList<LocalGISPlanesObra> getPlanesObraList(Connection connection,
			String description, Calendar fromStart, Calendar toStart,Calendar fromAprobacion,
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
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public int getNumPlanesObra(Connection connection, String description, Calendar fromStart, Calendar toStart,
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
			Calendar toFechaAprobacionPrincipado, Calendar fromFechaAprobacionPrincipado, Integer idEntidad) throws SQLException {
		int numPlanesObra = 0;
		String sqlQuery = 
			"SELECT " +
				"COUNT(DISTINCT(civil_work_warnings.id_warning)) AS NUM_PLANESOBRA " +
			"FROM " +
				"civil_work_warnings " +
			"LEFT JOIN civil_work_planes_envio ON civil_work_warnings.id_warning = civil_work_planes_envio.id_warning "+
			"LEFT JOIN civil_work_planes_respuesta ON civil_work_warnings.id_warning = civil_work_planes_respuesta.id_warning "+
			"LEFT JOIN civil_work_layer_feature_reference ON civil_work_warnings.id_warning = civil_work_layer_feature_reference.id_warning "+
			"WHERE ";
		Hashtable<Integer,Object> preparedStatementSets = new Hashtable<Integer,Object>();
		ArrayList<String> conditions = new ArrayList<String>();
		conditions.add("civil_work_warnings.id_type = ?");
		preparedStatementSets.put(new Integer(conditions.size()), WarningTypes.PLANESOBRA);
		conditions.add("civil_work_warnings.user_creator = ?");
		preparedStatementSets.put(new Integer(conditions.size()), userId);
		if (idEntidad != 0){
			conditions.add(" civil_work_warnings.id_municipio in (select id_municipio from entidades_municipios where id_entidad = ?)");
			preparedStatementSets.put(new Integer(conditions.size()), idEntidad);
		}
		else{
			conditions.add(" civil_work_warnings.id_municipio = ?");
			preparedStatementSets.put(new Integer(conditions.size()), idMunicipio);
		}
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
		
		if(plan != null){
			conditions.add(" civil_work_planes_envio.plan = ?");
			preparedStatementSets.put(new Integer(conditions.size()), plan);
		}
		if(anios != null && !anios.equals("")){
			conditions.add(" civil_work_planes_envio.anios = ?");
			preparedStatementSets.put(new Integer(conditions.size()), anios);
		}
		if(nombre != null && !nombre.equals("")){
			conditions.add(" civil_work_planes_envio.nombre = ?");
			preparedStatementSets.put(new Integer(conditions.size()), nombre);
		}
		if(paraje != null && !paraje.equals("")){
			conditions.add(" civil_work_planes_envio.paraje = ?");
			preparedStatementSets.put(new Integer(conditions.size()), paraje);
		}
		if(presupuestoEstimado != null){
			conditions.add(" civil_work_planes_envio.presupuesto_estimado = ?");
			preparedStatementSets.put(new Integer(conditions.size()), presupuestoEstimado);
		}
		if(presupuestoDefinitivo != null){
			conditions.add(" civil_work_planes_envio.presupuesto_definitivo = ?");
			preparedStatementSets.put(new Integer(conditions.size()), presupuestoDefinitivo);
		}
		if(existeProyecto != null && !existeProyecto.equals("")){
			conditions.add(" civil_work_planes_envio.existe_proyecto = ?");
			preparedStatementSets.put(new Integer(conditions.size()), existeProyecto);
		}
		if(infraestructura != null){
			conditions.add(" civil_work_planes_envio.infraestructura = ?");
			preparedStatementSets.put(new Integer(conditions.size()), infraestructura);
		}
		if(obraNueva != null && !obraNueva.equals("")){
			conditions.add(" civil_work_planes_envio.obra_nueva = ?");
			preparedStatementSets.put(new Integer(conditions.size()), obraNueva);
		}
		if(estudioAmbiental != null && !estudioAmbiental.equals("")){
			conditions.add(" civil_work_planes_envio.estudio_ambiental = ?");
			preparedStatementSets.put(new Integer(conditions.size()), estudioAmbiental);
		}
		if(datosEIEL != null){
			conditions.add(" civil_work_planes_envio.datos_eiel = ?");
			preparedStatementSets.put(new Integer(conditions.size()), datosEIEL);
		}
		if(permisos != null){
			conditions.add(" civil_work_planes_envio.permisos = ?");
			preparedStatementSets.put(new Integer(conditions.size()), permisos);
		}
		if(personaContacto != null && !personaContacto.equals("")){
			conditions.add(" civil_work_planes_envio.persona_contacto = ?");
			preparedStatementSets.put(new Integer(conditions.size()), personaContacto);
		}
		if(telefonoContacto != null && !telefonoContacto.equals("")){
			conditions.add(" civil_work_planes_envio.telefono_contacto = ?");
			preparedStatementSets.put(new Integer(conditions.size()), telefonoContacto);
		}
		if(fromAprobacion != null){
			conditions.add(" civil_work_planes_envio.fecha_aprobacion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromAprobacion);
		}	
		if(toAprobacion != null){
			conditions.add(" civil_work_planes_envio.fecha_aprobacion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toAprobacion);
		}
		
		if(destinatario != null && !destinatario.equals("")){
			conditions.add(" civil_work_planes_respuesta.destinatario = ?");
			preparedStatementSets.put(new Integer(conditions.size()), destinatario);
		}
		if(recibi != null && !recibi.equals("")){
			conditions.add(" civil_work_planes_respuesta.recibi = ?");
			preparedStatementSets.put(new Integer(conditions.size()), recibi);
		}
		if(fromFechaRecibi != null){
			conditions.add(" civil_work_planes_respuesta.fecha_recibi >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaRecibi);
		}	
		if(toFechaRecibi != null){
			conditions.add(" civil_work_planes_respuesta.fecha_recibi <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaRecibi);
		}
		if(supervision != null && !supervision.equals("")){
			conditions.add(" civil_work_planes_respuesta.supervision = ?");
			preparedStatementSets.put(new Integer(conditions.size()), supervision);
		}
		if(directorProyecto != null && !directorProyecto.equals("")){
			conditions.add(" civil_work_planes_respuesta.director_proyecto = ?");
			preparedStatementSets.put(new Integer(conditions.size()), directorProyecto);
		}
		if(autorProyecto != null && !autorProyecto.equals("")){
			conditions.add(" civil_work_planes_respuesta.director_proyecto = ?");
			preparedStatementSets.put(new Integer(conditions.size()), autorProyecto);
		}
		if(directorObra != null && !directorObra.equals("")){
			conditions.add(" civil_work_planes_respuesta.director_obra = ?");
			preparedStatementSets.put(new Integer(conditions.size()), directorObra);
		}
		if(empresaAdjudicataria != null && !empresaAdjudicataria.equals("")){
			conditions.add(" civil_work_planes_respuesta.empresa_adjudicataria = ?");
			preparedStatementSets.put(new Integer(conditions.size()), empresaAdjudicataria);
		}
		if(fromFechaResolucion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_resolucion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaResolucion);
		}	
		if(toFechaResolucion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_resolucion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaResolucion);
		}
		if(presupuestoAdjudicacion != null){
			conditions.add(" civil_work_planes_respuesta.presupuesto_adjudicacion = ?");
			preparedStatementSets.put(new Integer(conditions.size()), presupuestoAdjudicacion);
		}
		if(coordinadorSegSalud != null && !coordinadorSegSalud.equals("")){
			conditions.add(" civil_work_planes_respuesta.coordinador_seg_salud = ?");
			preparedStatementSets.put(new Integer(conditions.size()), coordinadorSegSalud);
		}
		if(fromActaReplanteo != null){
			conditions.add(" civil_work_planes_respuesta.acta_replanteo >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromActaReplanteo);
		}	
		if(toActaReplanteo != null){
			conditions.add(" civil_work_planes_respuesta.acta_replanteo <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toActaReplanteo);
		}
		if(fromFechaComienzo != null){
			conditions.add(" civil_work_planes_respuesta.fecha_comienzo >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaComienzo);
		}	
		if(toFechaComienzo != null){
			conditions.add(" civil_work_planes_respuesta.fecha_comienzo <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaComienzo);
		}
		if(fromFechaFinalizacion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_finalizacion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaFinalizacion);
		}	
		if(toFechaFinalizacion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_finalizacion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaFinalizacion);
		}
		if(fromProrrogas != null){
			conditions.add(" civil_work_planes_respuesta.prorrogas >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromProrrogas);
		}	
		if(toProrrogas != null){
			conditions.add(" civil_work_planes_respuesta.prorrogas <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toProrrogas);
		}
		if(fromActaRecepcion != null){
			conditions.add(" civil_work_planes_respuesta.acta_recepcion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromActaRecepcion);
		}	
		if(toActaRecepcion != null){
			conditions.add(" civil_work_planes_respuesta.acta_recepcion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toActaRecepcion);
		}
		if(certificacionFinal != null){
			conditions.add(" civil_work_planes_respuesta.certificacion_final = ?");
			preparedStatementSets.put(new Integer(conditions.size()), certificacionFinal);
		}
		if(fromResolucionCertificacion != null){
			conditions.add(" civil_work_planes_respuesta.resolucion_certificacion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromResolucionCertificacion);
		}	
		if(toResolucionCertificacion != null){
			conditions.add(" civil_work_planes_respuesta.resolucion_certificacion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toResolucionCertificacion);
		}
		if(fromInformacionCambiosEIEL != null){
			conditions.add(" civil_work_planes_respuesta.informacioncambioseiel >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromInformacionCambiosEIEL);
		}	
		if(toInformacionCambiosEIEL != null){
			conditions.add(" civil_work_planes_respuesta.informacioncambioseiel <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toInformacionCambiosEIEL);
		}
		if(fromReformados != null){
			conditions.add(" civil_work_planes_respuesta.reformados >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromReformados);
		}	
		if(toReformados != null){
			conditions.add(" civil_work_planes_respuesta.reformados <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toReformados);
		}
		if(liquidacion != null){
			conditions.add(" civil_work_planes_respuesta.liquidacion = ?");
			preparedStatementSets.put(new Integer(conditions.size()), liquidacion);
		}
		if(fromFechaLiquidacion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_liquidacion >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaLiquidacion);
		}	
		if(toFechaLiquidacion != null){
			conditions.add(" civil_work_planes_respuesta.fecha_liquidacion <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaLiquidacion);
		}
		if(detalles != null && !detalles.equals("")){
			conditions.add(" UPPER(TRANSLATE(civil_work_planes_respuesta.detalles,'ÁÉÍÓÚáéíóú','AEIOUaeiou')) LIKE UPPER(TRANSLATE(?,'ÁÉÍÓÚáéíóú','AEIOUaeiou'))");
			preparedStatementSets.put(new Integer(conditions.size()), "%"+detalles+"%");
		}
		if(fromFechaAprobacionPrincipado != null){
			conditions.add(" civil_work_planes_respuesta.fecha_aprobacion_principado >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaAprobacionPrincipado);
		}	
		if(toFechaAprobacionPrincipado != null){
			conditions.add(" civil_work_planes_respuesta.fecha_aprobacion_principado <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaAprobacionPrincipado);
		}
		if(fromFechaSolicitud != null){
			conditions.add(" civil_work_planes_respuesta.fecha_solicitud >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromFechaSolicitud);
		}	
		if(toFechaSolicitud != null){
			conditions.add(" civil_work_planes_respuesta.fecha_solicitud <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toFechaSolicitud);
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
	        	numPlanesObra = rs.getInt("NUM_PLANESOBRA");
		    }
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return numPlanesObra;
	}
	

	@Override
	public Integer getNextIdDocumentPlanesObraSequence(Connection connection)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public Integer getNextIdPlanesObraSequence(Connection connection)
			throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public void addPlanesObraSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException, ConfigurationException {

		addPlanesObraEnvioSpecificInfo(connection, planesObra);
		addPlanesObraRespuestaSpecificInfo(connection, planesObra);
		addPlanesObraAfectadosSpecificInfo(connection, planesObra);
		addPlanesObraAdjuntaSpecificInfo(connection, planesObra);
	}	
	
	private void addPlanesObraAdjuntaSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException, ConfigurationException {
		if (planesObra.getDocumentosAdjuntos() != null){
			int longitud = planesObra.getDocumentosAdjuntos().length;
			for (int i=0; i < longitud; i++){
				String sqlQuery = "INSERT INTO civil_work_planes_adjunta(" +
						"id_warning," +
						"tipo" +
						") VALUES (?, ?)";
		
				if(logger.isDebugEnabled())logger.debug("query addPlanesObraAdjuntaSpecificInfo = " + sqlQuery);
				PreparedStatement preparedStatement = null;
				try{
					if(logger.isDebugEnabled())logger.debug("Starting Statement");
					preparedStatement = connection.prepareStatement(sqlQuery);
					preparedStatement.setInt(1,planesObra.getId());	
					preparedStatement.setString(2, planesObra.getDocumentosAdjuntos()[i]);
		
					if(logger.isDebugEnabled()){
						logger.debug("Inserting PlanesObraAdjuntaSpecificInfo with id = "+ planesObra.getId());
					}
			        preparedStatement.executeUpdate();
				}finally{
					if(logger.isDebugEnabled())logger.debug("Closing Statement");
					ConnectionUtilities.closeConnection(null, preparedStatement, null);
				}
			}
		}
	}
	
	private void addPlanesObraAfectadosSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException, ConfigurationException {
		if (planesObra.getServiciosAfectados() != null){
			int longitud = planesObra.getServiciosAfectados().length;
			for (int i=0; i < longitud; i++){
				String sqlQuery = "INSERT INTO civil_work_planes_afectados(" +
						"id_warning," +
						"servicio" +
						") VALUES (?, ?)";
		
				if(logger.isDebugEnabled())logger.debug("query addPlanesObraAfectadosSpecificInfo = " + sqlQuery);
				PreparedStatement preparedStatement = null;
				try{
					if(logger.isDebugEnabled())logger.debug("Starting Statement");
					preparedStatement = connection.prepareStatement(sqlQuery);
					preparedStatement.setInt(1,planesObra.getId());	
					preparedStatement.setString(2, planesObra.getServiciosAfectados()[i]);
		
					if(logger.isDebugEnabled()){
						logger.debug("Inserting PlanesObraAfectadosSpecificInfo with id = "+ planesObra.getId());
					}
			        preparedStatement.executeUpdate();
				}finally{
					if(logger.isDebugEnabled())logger.debug("Closing Statement");
					ConnectionUtilities.closeConnection(null, preparedStatement, null);
				}
			}
		}
	}

	private void addPlanesObraRespuestaSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException, ConfigurationException {

		String sqlQuery = "INSERT INTO civil_work_planes_respuesta(" +
				"id_warning," +
				"destinatario," +
				"recibi," +
				"fecha_recibi," +
				"supervision," +
				"director_proyecto," +
				"autor_proyecto," +
				"director_obra," +
				"empresa_adjudicataria," +
				"fecha_resolucion," +
				"presupuesto_adjudicacion," +
				"coordinador_seg_salud," +
				"acta_replanteo," +
				"fecha_comienzo," +
				"fecha_finalizacion," +
				"prorrogas," +
				"acta_recepcion," +
				"certificacion_final," +
				"resolucion_certificacion," +
				"informacioncambioseiel," +
				"reformados," +
				"liquidacion," +
				"fecha_liquidacion," +
				"detalles," +
				"fecha_aprobacion_principado," +
				"fecha_solicitud" +
				") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if(logger.isDebugEnabled())logger.debug("query addPlanesObraRespuestaSpecificInfo = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,planesObra.getId());		
			preparedStatement.setString(2, planesObra.getDestinatario());
			preparedStatement.setString(3, planesObra.getRecibi());
			if(planesObra.getFechaRecibi() != null)
				preparedStatement.setDate(4, new java.sql.Date(planesObra.getFechaRecibi().getTimeInMillis()));
			else
				preparedStatement.setDate(4, null);
			preparedStatement.setString(5, planesObra.getSupervision());
			preparedStatement.setString(6, planesObra.getDirectorProyecto());
			preparedStatement.setString(7, planesObra.getAutorProyecto());
			preparedStatement.setString(8, planesObra.getDirectorObra());
			preparedStatement.setString(9, planesObra.getEmpresaAdjudicataria());
			if(planesObra.getFechaResolucion() != null)
				preparedStatement.setDate(10, new java.sql.Date(planesObra.getFechaResolucion().getTimeInMillis()));
			else
				preparedStatement.setDate(10, null);
			preparedStatement.setDouble(11, planesObra.getPresupuestoAdjudicacion());
			preparedStatement.setString(12, planesObra.getCoordinadorSegSalud());
			if(planesObra.getActaReplanteo() != null)
				preparedStatement.setDate(13, new java.sql.Date(planesObra.getActaReplanteo().getTimeInMillis()));
			else
				preparedStatement.setDate(13, null);
			if(planesObra.getFechaComienzo() != null)
				preparedStatement.setDate(14, new java.sql.Date(planesObra.getFechaComienzo().getTimeInMillis()));
			else
				preparedStatement.setDate(14, null);
			if(planesObra.getFechaFinalizacion() != null)
				preparedStatement.setDate(15, new java.sql.Date(planesObra.getFechaFinalizacion().getTimeInMillis()));
			else
				preparedStatement.setDate(15, null);
			if(planesObra.getProrrogas() != null)
				preparedStatement.setDate(16, new java.sql.Date(planesObra.getProrrogas().getTimeInMillis()));
			else
				preparedStatement.setDate(16, null);
			if(planesObra.getActaRecepcion() != null)
				preparedStatement.setDate(17, new java.sql.Date(planesObra.getActaRecepcion().getTimeInMillis()));
			else
				preparedStatement.setDate(17, null);
			preparedStatement.setDouble(18, planesObra.getCertificacionFinal());
			if(planesObra.getResolucionCertificacion() != null)
				preparedStatement.setDate(19, new java.sql.Date(planesObra.getResolucionCertificacion().getTimeInMillis()));
			else
				preparedStatement.setDate(19, null);
			if(planesObra.getInformacionCambiosEIEL() != null)
				preparedStatement.setDate(20, new java.sql.Date(planesObra.getInformacionCambiosEIEL().getTimeInMillis()));
			else
				preparedStatement.setDate(20, null);
			if(planesObra.getReformados() != null)
				preparedStatement.setDate(21, new java.sql.Date(planesObra.getReformados().getTimeInMillis()));
			else
				preparedStatement.setDate(21, null);
			preparedStatement.setDouble(22, planesObra.getLiquidacion());
			if(planesObra.getFechaLiquidacion() != null)
				preparedStatement.setDate(23, new java.sql.Date(planesObra.getFechaLiquidacion().getTimeInMillis()));
			else
				preparedStatement.setDate(23, null);
			preparedStatement.setString(24, planesObra.getDetalles());
			if(planesObra.getFechaAprobacionPrincipado() != null)
				preparedStatement.setDate(25, new java.sql.Date(planesObra.getFechaAprobacionPrincipado().getTimeInMillis()));
			else
				preparedStatement.setDate(25, null);
			if(planesObra.getFechaSolicitud() != null)
				preparedStatement.setDate(26, new java.sql.Date(planesObra.getFechaSolicitud().getTimeInMillis()));
			else
				preparedStatement.setDate(26, null);
			if(logger.isDebugEnabled()){
				logger.debug("Inserting PlanesObraRespuestaSpecificInfo with id = "+ planesObra.getId());
			}
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}
	
	private void addPlanesObraEnvioSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException, ConfigurationException {

		String sqlQuery = "INSERT INTO civil_work_planes_envio(" +
				"id_warning," +
				"plan," +
				"anios," +
				"nombre," +
				"paraje," +
				"presupuesto_estimado," +
				"presupuesto_definitivo," +
				"existe_proyecto," +
				"infraestructura," +
				"obra_nueva, " +
				"estudio_ambiental," +
				"datos_eiel," +
				"permisos," +
				"persona_contacto," +
				"telefono_contacto," +
				"fecha_aprobacion" +
				") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		if(logger.isDebugEnabled())logger.debug("query addPlanesObraEnvioSpecificInfo = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1,planesObra.getId());		
			preparedStatement.setString(2, planesObra.getPlan());
			preparedStatement.setString(3, planesObra.getAnios());
			preparedStatement.setString(4, planesObra.getNombre());
			preparedStatement.setString(5, planesObra.getParaje());
			preparedStatement.setDouble(6, planesObra.getPresupuestoEstimado());
			preparedStatement.setDouble(7, planesObra.getPresupuestoDefinitivo());
			preparedStatement.setString(8, planesObra.getExisteProyecto());
			preparedStatement.setString(9, planesObra.getInfraestructura());
			preparedStatement.setString(10, planesObra.getObraNueva());
			preparedStatement.setString(11, planesObra.getEstudioAmbiental());
			preparedStatement.setString(12, planesObra.getDatosEIEL());
			preparedStatement.setString(13, planesObra.getPermisos());
			preparedStatement.setString(14, planesObra.getPersonaContacto());
			preparedStatement.setString(15, planesObra.getTelefonoContacto());
			if(planesObra.getFechaAprobacion() != null)
				preparedStatement.setDate(16, new java.sql.Date(planesObra.getFechaAprobacion().getTimeInMillis()));
			else
				preparedStatement.setDate(16, null);
			
			if(logger.isDebugEnabled()){
				logger.debug("Inserting PlanesObraEnvioSpecificInfo with id = "+ planesObra.getId());
			}
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}

	@Override
	public void setPlanesObraSpecificInfo(Connection connection,
			LocalGISPlanesObra planesObra) throws SQLException {
		
		setPlanesObraEnvioSpecificInfo(connection, planesObra);
		setPlanesObraRespuestaSpecificInfo(connection, planesObra);
		setPlanesObraAfectadosSpecificInfo(connection, planesObra);
		setPlanesObraAdjuntaSpecificInfo(connection, planesObra);
	}	
	
	private void setPlanesObraAdjuntaSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException{
		if (planesObra.getDocumentosAdjuntos() != null){

			String sqlQuery = "DELETE FROM civil_work_planes_adjunta WHERE id_warning = ?";
	
			if(logger.isDebugEnabled())logger.debug("query setPlanesObraAdjuntaSpecificInfo = " + sqlQuery);
			PreparedStatement preparedStatement = null;
			try{
				if(logger.isDebugEnabled())logger.debug("Starting Statement");
				preparedStatement = connection.prepareStatement(sqlQuery);
				preparedStatement.setInt(1, planesObra.getId());	
				
		        preparedStatement.executeUpdate();
		        addPlanesObraAdjuntaSpecificInfo(connection, planesObra);
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(logger.isDebugEnabled())logger.debug("Closing Statement");
				ConnectionUtilities.closeConnection(null, preparedStatement, null);
			}

		}
	}
	
	private void setPlanesObraAfectadosSpecificInfo(
			Connection connection, LocalGISPlanesObra planesObra)
			throws SQLException{
		if (planesObra.getServiciosAfectados() != null){

			String sqlQuery = "DELETE FROM civil_work_planes_afectados WHERE id_warning = ?";
	
			if(logger.isDebugEnabled())logger.debug("query setPlanesObraAfectadosSpecificInfo = " + sqlQuery);
			PreparedStatement preparedStatement = null;
			try{
				if(logger.isDebugEnabled())logger.debug("Starting Statement");
				preparedStatement = connection.prepareStatement(sqlQuery);
				preparedStatement.setInt(1, planesObra.getId());	
				
		        preparedStatement.executeUpdate();
		        addPlanesObraAfectadosSpecificInfo(connection, planesObra);
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(logger.isDebugEnabled())logger.debug("Closing Statement");
				ConnectionUtilities.closeConnection(null, preparedStatement, null);
			}

		}
	}
	
	private void setPlanesObraEnvioSpecificInfo(Connection connection,
			LocalGISPlanesObra planesObra) throws SQLException {
		String sqlQuery = "UPDATE civil_work_planes_envio SET " +
		"plan = ?," +
		"anios = ?," +
		"nombre = ?," +
		"paraje = ?, " +
		"presupuesto_estimado = ?, " +
		"presupuesto_definitivo = ?, " +
		"existe_proyecto = ?, " +
		"infraestructura = ?, " +
		"obra_nueva = ?, " +
		"estudio_ambiental = ?, " +
		"datos_eiel = ?, " +
		"permisos = ?, " +
		"persona_contacto = ?, " +
		"telefono_contacto = ?, " +
		"fecha_aprobacion = ? " +
		"WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("setPlanesObraEnvioSpecificInfo = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
		
			preparedStatement.setString(1, planesObra.getPlan());
			preparedStatement.setString(2, planesObra.getAnios());
			preparedStatement.setString(3, planesObra.getNombre());
			preparedStatement.setString(4, planesObra.getParaje());
			preparedStatement.setDouble(5, planesObra.getPresupuestoEstimado());
			preparedStatement.setDouble(6, planesObra.getPresupuestoDefinitivo());
			preparedStatement.setString(7, planesObra.getExisteProyecto());
			preparedStatement.setString(8, planesObra.getInfraestructura());
			preparedStatement.setString(9, planesObra.getObraNueva());
			preparedStatement.setString(10, planesObra.getEstudioAmbiental());
			preparedStatement.setString(11, planesObra.getDatosEIEL());
			preparedStatement.setString(12, planesObra.getPermisos());
			preparedStatement.setString(13, planesObra.getPersonaContacto());
			preparedStatement.setString(14, planesObra.getTelefonoContacto());
			if(planesObra.getFechaAprobacion() != null)
				preparedStatement.setDate(15, new java.sql.Date(planesObra.getFechaAprobacion().getTimeInMillis()));
			else
				preparedStatement.setNull(15, java.sql.Types.DATE);
			preparedStatement.setInt(16,planesObra.getId());
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}
	
	private void setPlanesObraRespuestaSpecificInfo(Connection connection,
			LocalGISPlanesObra planesObra) throws SQLException {
		String sqlQuery = "UPDATE civil_work_planes_respuesta SET " +
		"destinatario = ?," +
		"recibi = ?," +
		"fecha_recibi = ?," +
		"supervision = ?, " +
		"director_proyecto = ?, " +
		"autor_proyecto = ?, " +
		"director_obra = ?, " +
		"empresa_adjudicataria = ?, " +
		"fecha_resolucion = ?, " +
		"presupuesto_adjudicacion = ?, " +
		"coordinador_seg_salud = ?, " +
		"acta_replanteo = ?, " +
		"fecha_comienzo = ?, " +
		"fecha_finalizacion = ?, " +
		"prorrogas = ?, " +
		"acta_recepcion = ?, " +
		"certificacion_final = ?, " +
		"resolucion_certificacion = ?, " +
		"informacioncambioseiel = ?, " +
		"reformados = ?, " +
		"liquidacion = ?, " +
		"fecha_liquidacion = ?, " +
		"detalles = ?, " +
		"fecha_aprobacion_principado = ?, " +
		"fecha_solicitud = ? " +
		"WHERE id_warning = ?";
		if(logger.isDebugEnabled())logger.debug("setPlanesObraRespuestaSpecificInfo = " + sqlQuery);
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sqlQuery);
	
			preparedStatement.setString(1, planesObra.getDestinatario());
			preparedStatement.setString(2, planesObra.getRecibi());
			if(planesObra.getFechaRecibi() != null)
				preparedStatement.setDate(3, new java.sql.Date(planesObra.getFechaRecibi().getTimeInMillis()));
			else
				preparedStatement.setNull(3, java.sql.Types.DATE);
			preparedStatement.setString(4, planesObra.getSupervision());
			preparedStatement.setString(5, planesObra.getDirectorProyecto());
			preparedStatement.setString(6, planesObra.getAutorProyecto());
			preparedStatement.setString(7, planesObra.getDirectorObra());
			preparedStatement.setString(8, planesObra.getEmpresaAdjudicataria());
			if(planesObra.getFechaResolucion() != null)
				preparedStatement.setDate(9, new java.sql.Date(planesObra.getFechaResolucion().getTimeInMillis()));
			else
				preparedStatement.setNull(9, java.sql.Types.DATE);
			preparedStatement.setDouble(10, planesObra.getPresupuestoAdjudicacion());
			preparedStatement.setString(11, planesObra.getCoordinadorSegSalud());
			if(planesObra.getActaReplanteo() != null)
				preparedStatement.setDate(12, new java.sql.Date(planesObra.getActaReplanteo().getTimeInMillis()));
			else
				preparedStatement.setNull(12, java.sql.Types.DATE);
			if(planesObra.getFechaComienzo() != null)
				preparedStatement.setDate(13, new java.sql.Date(planesObra.getFechaComienzo().getTimeInMillis()));
			else
				preparedStatement.setNull(13, java.sql.Types.DATE);
			if(planesObra.getFechaFinalizacion() != null)
				preparedStatement.setDate(14, new java.sql.Date(planesObra.getFechaFinalizacion().getTimeInMillis()));
			else
				preparedStatement.setNull(14, java.sql.Types.DATE);
			if(planesObra.getProrrogas() != null)
				preparedStatement.setDate(15, new java.sql.Date(planesObra.getProrrogas().getTimeInMillis()));
			else
				preparedStatement.setNull(15, java.sql.Types.DATE);
			if(planesObra.getActaRecepcion() != null)
				preparedStatement.setDate(16, new java.sql.Date(planesObra.getActaRecepcion().getTimeInMillis()));
			else
				preparedStatement.setNull(16, java.sql.Types.DATE);
			preparedStatement.setDouble(17, planesObra.getCertificacionFinal());
			if(planesObra.getResolucionCertificacion() != null)
				preparedStatement.setDate(18, new java.sql.Date(planesObra.getResolucionCertificacion().getTimeInMillis()));
			else
				preparedStatement.setNull(18, java.sql.Types.DATE);
			if(planesObra.getInformacionCambiosEIEL() != null)
				preparedStatement.setDate(19, new java.sql.Date(planesObra.getInformacionCambiosEIEL().getTimeInMillis()));
			else
				preparedStatement.setNull(19, java.sql.Types.DATE);
			if(planesObra.getReformados() != null)
				preparedStatement.setDate(20, new java.sql.Date(planesObra.getReformados().getTimeInMillis()));
			else
				preparedStatement.setNull(20, java.sql.Types.DATE);
			preparedStatement.setDouble(21, planesObra.getLiquidacion());
			if(planesObra.getFechaLiquidacion() != null)
				preparedStatement.setDate(22, new java.sql.Date(planesObra.getFechaLiquidacion().getTimeInMillis()));
			else
				preparedStatement.setNull(22, java.sql.Types.DATE);
			preparedStatement.setString(23, planesObra.getDetalles());	
			if(planesObra.getFechaAprobacionPrincipado() != null)
				preparedStatement.setDate(24, new java.sql.Date(planesObra.getFechaAprobacionPrincipado().getTimeInMillis()));
			else
				preparedStatement.setDate(24, null);
			if(planesObra.getFechaSolicitud() != null)
				preparedStatement.setDate(25, new java.sql.Date(planesObra.getFechaSolicitud().getTimeInMillis()));
			else
				preparedStatement.setDate(25, null);
			preparedStatement.setInt(26,planesObra.getId());			
			
	        preparedStatement.executeUpdate();
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement");
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
		
	}

	public static void buildOrderColumns(String sqlQuery,
			ArrayList<PlanesObraColumn> orderColumns) {
		if(orderColumns != null){
			Iterator<PlanesObraColumn> it = orderColumns.iterator();
			sqlQuery += " ORDER BY ";
			while(it.hasNext()){
				sqlQuery += it.next();
				if(it.hasNext())
					sqlQuery += ",";
			}
		}
	}


	@Override
	public ArrayList<StatisticalDataOT> getStatistics(Connection connection, Integer idEntidad) throws SQLException {
		throw new NotImplementedException();
		
	}	
	
}
