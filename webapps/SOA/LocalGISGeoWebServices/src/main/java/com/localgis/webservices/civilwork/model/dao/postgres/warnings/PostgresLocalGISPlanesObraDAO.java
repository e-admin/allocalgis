/**
 * PostgresLocalGISPlanesObraDAO.java
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
import com.localgis.app.gestionciudad.beans.LocalGISIntervention;
import com.localgis.app.gestionciudad.beans.LocalGISPlanesObra;
import com.localgis.app.gestionciudad.beans.types.WarningTypes;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISGenericDAO;
import com.localgis.webservices.civilwork.model.dao.generic.LocalGISPlanesObraDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.OrderByColumn;

public class PostgresLocalGISPlanesObraDAO extends LocalGISPlanesObraDAO{
	
	private static Logger logger = Logger.getLogger(PostgresLocalGISPlanesObraDAO.class);
	
	@Override
	public void addPlanesObraDocumentThumbnail(Connection connection,
			Document document) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocumentThumbnail(connection, document);
	}

	@Override
	public void addPlanesObraDocument(Connection connection, Integer idWarning,
			Document document) throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		pDAO.addWarningDocument(connection, idWarning, document);
	}
	@Override
	public Document getPlanesObraDocument(Connection connection,
			Integer idDocument, Boolean returnCompleteDocuments)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getWarningDocument(connection, idDocument,returnCompleteDocuments);
	}

	@Override
	public ArrayList<Document> getPlanesObraDocuments(Connection connection,
			Integer idWarning, Boolean returnCompleteDocuments)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getWarningDocuments(connection, idWarning,returnCompleteDocuments);
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
		ArrayList<LocalGISPlanesObra> planesObra = new ArrayList<LocalGISPlanesObra>();
		String sqlQuery = 
			"SELECT civil_work_warnings.id_warning, "+
				"civil_work_warnings.description, "+
				"civil_work_warnings.start_warning, "+
				"civil_work_warnings.user_creator, "+
				"civil_work_warnings.id_municipio, "+
				"civil_work_planes_envio.plan, "+
				"civil_work_planes_envio.anios, "+
				"civil_work_planes_envio.nombre, "+
				"civil_work_planes_envio.paraje, "+
				"civil_work_planes_envio.presupuesto_estimado, "+
				"civil_work_planes_envio.presupuesto_definitivo, "+
				"civil_work_planes_envio.existe_proyecto, "+
				"civil_work_planes_envio.infraestructura, "+
				"civil_work_planes_envio.presupuesto_definitivo, "+
				"civil_work_planes_envio.existe_proyecto, "+
				"civil_work_planes_envio.infraestructura, "+
				"civil_work_planes_envio.obra_nueva, "+
				"civil_work_planes_envio.estudio_ambiental, "+
				"civil_work_planes_envio.datos_eiel, "+
				"civil_work_planes_envio.permisos, "+
				"civil_work_planes_envio.persona_contacto, "+
				"civil_work_planes_envio.telefono_contacto, "+
				"civil_work_planes_envio.fecha_aprobacion, "+
				"civil_work_planes_respuesta.destinatario, "+
				"civil_work_planes_respuesta.recibi, "+
				"civil_work_planes_respuesta.fecha_recibi, "+
				"civil_work_planes_respuesta.supervision, "+
				"civil_work_planes_respuesta.director_proyecto, "+
				"civil_work_planes_respuesta.autor_proyecto, "+
				"civil_work_planes_respuesta.director_obra, "+
				"civil_work_planes_respuesta.empresa_adjudicataria, "+
				"civil_work_planes_respuesta.fecha_resolucion, "+
				"civil_work_planes_respuesta.presupuesto_adjudicacion, "+
				"civil_work_planes_respuesta.coordinador_seg_salud, "+
				"civil_work_planes_respuesta.acta_replanteo, "+
				"civil_work_planes_respuesta.fecha_comienzo, "+
				"civil_work_planes_respuesta.fecha_finalizacion, "+
				"civil_work_planes_respuesta.prorrogas, "+
				"civil_work_planes_respuesta.acta_recepcion, "+
				"civil_work_planes_respuesta.certificacion_final, "+
				"civil_work_planes_respuesta.resolucion_certificacion, "+
				"civil_work_planes_respuesta.informacioncambioseiel, "+
				"civil_work_planes_respuesta.reformados, "+
				"civil_work_planes_respuesta.liquidacion, "+
				"civil_work_planes_respuesta.fecha_liquidacion, "+
				"civil_work_planes_respuesta.detalles, "+
				"civil_work_planes_respuesta.fecha_aprobacion_principado, "+
				"civil_work_planes_respuesta.fecha_solicitud, "+
				"municipios.nombreoficial as municipio "+
			"FROM civil_work_warnings "+
			"inner join municipios on municipios.id_provincia::text||municipios.id_ine::text = civil_work_warnings.id_municipio::text "+
			"LEFT JOIN civil_work_planes_envio ON civil_work_warnings.id_warning = civil_work_planes_envio.id_warning "+
			"LEFT JOIN civil_work_planes_respuesta ON civil_work_warnings.id_warning = civil_work_planes_respuesta.id_warning "+
			"LEFT JOIN civil_work_layer_feature_reference ON civil_work_warnings.id_warning = civil_work_layer_feature_reference.id_warning "+
			"WHERE ";
		Hashtable<Integer,Object> preparedStatementSets = new Hashtable<Integer,Object>();
		ArrayList<String> conditions = new ArrayList<String>();
		conditions.add(" civil_work_warnings.id_type = ?");
		preparedStatementSets.put(new Integer(conditions.size()), WarningTypes.PLANESOBRA);
		conditions.add(" civil_work_warnings.user_creator = ?");
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
			conditions.add(" UPPER(TRANSLATE(civil_work_warnings.description,'ÁÉÍÓÚáéíóú','AEIOUaeiou')) LIKE UPPER(TRANSLATE(?,'ÁÉÍÓÚáéíóú','AEIOUaeiou'))");
			preparedStatementSets.put(new Integer(conditions.size()), "%"+description+"%");
		}
		if(fromStart != null){
			conditions.add(" civil_work_warnings.start_warning >= ?");
			preparedStatementSets.put(new Integer(conditions.size()), fromStart);
		}	
		if(toStart != null){
			conditions.add(" civil_work_warnings.start_warning <= ?");
			preparedStatementSets.put(new Integer(conditions.size()), toStart);
		}
		
		if(plan != null && !plan.equals("")){
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
		if(infraestructura != null && !infraestructura.equals("")){
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
		if (datosEIEL != null && !datosEIEL.equals("")){
			conditions.add(" civil_work_planes_envio.datos_eiel = ?");
			preparedStatementSets.put(new Integer(conditions.size()), datosEIEL);
		}
		if (permisos != null && !permisos.equals("")){
			conditions.add(" civil_work_planes_envio.permisos = ?");
			preparedStatementSets.put(new Integer(conditions.size()), permisos);
		}
		if (personaContacto != null && !personaContacto.equals("")){
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
		sqlQuery += " GROUP BY "+ 
					"civil_work_warnings.id_warning, "+ 
					"civil_work_warnings.description, "+
					"civil_work_warnings.start_warning, "+
					"civil_work_warnings.user_creator, "+
					"municipios.nombreoficial, "+
					"civil_work_warnings.id_municipio, "+
					"civil_work_planes_envio.plan, " +
					"civil_work_planes_envio.anios, " +
					"civil_work_planes_envio.nombre, " +
					"civil_work_planes_envio.paraje, " +
					"civil_work_planes_envio.presupuesto_estimado, " +
					"civil_work_planes_envio.presupuesto_definitivo, " +
					"civil_work_planes_envio.existe_proyecto, " +
					"civil_work_planes_envio.infraestructura, " +
					"civil_work_planes_envio.obra_nueva, " +
					"civil_work_planes_envio.estudio_ambiental, " +
					"civil_work_planes_envio.datos_eiel, " +
					"civil_work_planes_envio.permisos, " +
					"civil_work_planes_envio.persona_contacto, " +
					"civil_work_planes_envio.telefono_contacto, " +
					"civil_work_planes_envio.fecha_aprobacion, " +
					"civil_work_planes_respuesta.destinatario, " +
					"civil_work_planes_respuesta.recibi, " +
					"civil_work_planes_respuesta.supervision, " +
					"civil_work_planes_respuesta.fecha_recibi, " +
					"civil_work_planes_respuesta.director_proyecto, " +
					"civil_work_planes_respuesta.autor_proyecto, " +
					"civil_work_planes_respuesta.director_obra, " +
					"civil_work_planes_respuesta.empresa_adjudicataria, " +
					"civil_work_planes_respuesta.fecha_resolucion, " +
					"civil_work_planes_respuesta.presupuesto_adjudicacion, " +
					"civil_work_planes_respuesta.coordinador_seg_salud, " +
					"civil_work_planes_respuesta.acta_replanteo, " +
					"civil_work_planes_respuesta.fecha_comienzo, " +
					"civil_work_planes_respuesta.fecha_finalizacion, " +
					"civil_work_planes_respuesta.prorrogas, " +
					"civil_work_planes_respuesta.acta_recepcion, " +
					"civil_work_planes_respuesta.certificacion_final, " +
					"civil_work_planes_respuesta.resolucion_certificacion, " +
					"civil_work_planes_respuesta.informacioncambioseiel, " +
					"civil_work_planes_respuesta.reformados, " +
					"civil_work_planes_respuesta.liquidacion, " +
					"civil_work_planes_respuesta.fecha_liquidacion, " +
					"civil_work_planes_respuesta.detalles, " +
					"civil_work_planes_respuesta.fecha_aprobacion_principado, " +
					"civil_work_planes_respuesta.fecha_solicitud ";
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
		PreparedStatement preparedStatement2 = null;
		ResultSet rs2 = null;		
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			LocalGISGenericDAO.buildStatement(preparedStatement,preparedStatementSets);
			if(logger.isDebugEnabled())logger.debug("Starting Resultset");
			rs = preparedStatement.executeQuery();
	        while (rs.next()){
	        	Integer actualIdWarning = rs.getInt("id_warning");
				String actualDescription = rs.getString("description");
				Date actualStartDate = rs.getDate("start_warning");
				Calendar actualStart = new GregorianCalendar();
				actualStart.setTime(actualStartDate);
				Integer actualUserCreator = Integer.parseInt(rs.getString("user_creator"));
				Integer actualIdMunicipio = rs.getInt("id_municipio");
				String municipio = rs.getString("municipio");
				String actualPlan = rs.getString("plan");
				String actualAnios = rs.getString("anios");
				String actualNombre = rs.getString("nombre");
				String actualParaje = rs.getString("paraje");
				Double actualPresEstimado = rs.getDouble("presupuesto_estimado");
				Double actualPresDefinitivo = rs.getDouble("presupuesto_definitivo");
				String actualExisteProyecto = rs.getString("existe_proyecto");
				String actualInfraestructura = rs.getString("infraestructura");
				String actualObraNueva = rs.getString("obra_nueva");
				String actualEstudioAmbiental = rs.getString("estudio_ambiental");
				String actualDatosEIEL = rs.getString("datos_eiel");
				String actualPermisos = rs.getString("permisos");
				String actualPersonaContacto = rs.getString("persona_contacto");
				String actualTelefonoContacto = rs.getString("telefono_contacto");
				Calendar actualFechaAprobacion = null;
				Date actualFechaAprobacionDate = rs.getDate("fecha_aprobacion");
				if (actualFechaAprobacionDate!= null){
					actualFechaAprobacion = new GregorianCalendar();
					actualFechaAprobacion.setTime(actualFechaAprobacionDate);
				}
				
				String actualDestinatario = rs.getString("destinatario");
				String actualRecibi = rs.getString("recibi");
				Calendar actualFechaRecibi = null;
				Date actualFechaRecibiDate = rs.getDate("fecha_recibi");
				if (actualFechaRecibiDate!= null){
					actualFechaRecibi = new GregorianCalendar();
					actualFechaRecibi.setTime(actualFechaRecibiDate);
				}
				String actualSupervision = rs.getString("supervision");
				String actualDirectorProy = rs.getString("director_proyecto");
				String actualAutorProy = rs.getString("autor_proyecto");
				String actualDirectorObra = rs.getString("director_obra");
				String actualEmpresaAdjud = rs.getString("empresa_adjudicataria");
				Calendar actualFechaResolucion = null;
				Date actualFechaResolucionDate = rs.getDate("fecha_resolucion");
				if (actualFechaResolucionDate!= null){
					actualFechaResolucion = new GregorianCalendar();
					actualFechaResolucion.setTime(actualFechaResolucionDate);
				}
				Double actualPresuAdjud = rs.getDouble("presupuesto_adjudicacion");
				String actualCoordSegSalud = rs.getString("coordinador_seg_salud");
				Calendar actualActaReplanteo = null;
				Date actualActaReplanteoDate = rs.getDate("acta_replanteo");
				if (actualActaReplanteoDate!= null){
					actualActaReplanteo = new GregorianCalendar();
					actualActaReplanteo.setTime(actualActaReplanteoDate);
				}
				Calendar actualFechaComienzo = null;
				Date actualFechaComienzoDate = rs.getDate("fecha_comienzo");
				if (actualFechaComienzoDate!= null){
					actualFechaComienzo = new GregorianCalendar();
					actualFechaComienzo.setTime(actualFechaComienzoDate);
				}
				Calendar actualFechaFinalizacion = null;
				Date actualFechaFinalizacionDate = rs.getDate("fecha_finalizacion");
				if (actualFechaComienzoDate!= null){
					actualFechaFinalizacion = new GregorianCalendar();
					actualFechaFinalizacion.setTime(actualFechaFinalizacionDate);
				}
				Calendar actualProrrogas = null;
				Date actualProrrogasDate = rs.getDate("prorrogas");
				if (actualProrrogasDate!= null){
					actualProrrogas = new GregorianCalendar();
					actualProrrogas.setTime(actualProrrogasDate);
				}
				Calendar actualActaRecepcion = null;
				Date actualActaRecepcionDate = rs.getDate("acta_recepcion");
				if (actualActaRecepcionDate!= null){
					actualActaRecepcion = new GregorianCalendar();
					actualActaRecepcion.setTime(actualActaRecepcionDate);
				}
				Double actualCertificacionFinal = rs.getDouble("certificacion_final");
				Calendar actualResolucionCert = null;
				Date actualResolucionCertDate = rs.getDate("resolucion_certificacion");
				if (actualResolucionCertDate!= null){
					actualResolucionCert = new GregorianCalendar();
					actualResolucionCert.setTime(actualResolucionCertDate);
				}
				Calendar actualInformacionCambiosEIEL = null;
				Date actualInformacionCambiosEIELDate = rs.getDate("informacioncambioseiel");
				if (actualInformacionCambiosEIELDate!= null){
					actualInformacionCambiosEIEL = new GregorianCalendar();
					actualInformacionCambiosEIEL.setTime(actualInformacionCambiosEIELDate);
				}
				Calendar actualReformados = null;
				Date actualReformadosDate = rs.getDate("reformados");
				if (actualReformadosDate!= null){
					actualReformados = new GregorianCalendar();
					actualReformados.setTime(actualActaRecepcionDate);
				}
				Double actualLiquidacion = rs.getDouble("liquidacion");
				Calendar actualFechaLiquidacion = null;
				Date actualFechaLiquidacionDate = rs.getDate("fecha_liquidacion");
				if (actualFechaLiquidacionDate!= null){
					actualFechaLiquidacion = new GregorianCalendar();
					actualFechaLiquidacion.setTime(actualFechaLiquidacionDate);
				}
				String actualDetalles = rs.getString("detalles");
				
				Calendar actualFechaAprobacionPrincipado = null;
				Date actualFechaAprobacionPrincipadoDate = rs.getDate("fecha_aprobacion_principado");
				if (actualFechaAprobacionPrincipadoDate!= null){
					actualFechaAprobacionPrincipado = new GregorianCalendar();
					actualFechaAprobacionPrincipado.setTime(actualFechaAprobacionPrincipadoDate);
				}
				
				Calendar actualFechaSolicitud = null;
				Date actualFechaSolicitudDate = rs.getDate("fecha_solicitud");
				if (actualFechaSolicitudDate!= null){
					actualFechaSolicitud = new GregorianCalendar();
					actualFechaSolicitud.setTime(actualFechaSolicitudDate);
				}
				
				String sqlQuery2 = "SELECT tipo FROM civil_work_planes_adjunta where id_warning = ? ";
				preparedStatement2 = connection.prepareStatement(sqlQuery2);
				preparedStatement2.setInt(1, actualIdWarning);
				rs2 = preparedStatement2.executeQuery();
				ArrayList<String> actualDocumentosAdjuntos = new ArrayList<String>();
				while (rs2.next()){
					actualDocumentosAdjuntos.add(rs2.getString("tipo"));
				}
				
				sqlQuery2 = "SELECT servicio FROM civil_work_planes_afectados where id_warning = ? ";
				preparedStatement2 = null;
				rs2 = null;
				preparedStatement2 = connection.prepareStatement(sqlQuery2);
				preparedStatement2.setInt(1, actualIdWarning);
				rs2 = preparedStatement2.executeQuery();
				ArrayList<String> actualServiciosAfectados = new ArrayList<String>();
				while (rs2.next()){
					actualServiciosAfectados.add(rs2.getString("servicio"));
				}

				String[] actualDocu = (String[]) actualDocumentosAdjuntos.toArray(new String[actualDocumentosAdjuntos.size()]);
				String[] actualServ = (String[]) actualServiciosAfectados.toArray(new String[actualServiciosAfectados.size()]);
				
	        	LocalGISPlanesObra planObra = new LocalGISPlanesObra(actualIdWarning, actualUserCreator, actualIdMunicipio, actualStart, actualDescription, 
	        			actualPlan, actualAnios, actualNombre, actualParaje, actualPresEstimado, actualPresDefinitivo, actualExisteProyecto, actualInfraestructura,
	        			actualObraNueva, actualEstudioAmbiental, actualDatosEIEL, actualPermisos, actualPersonaContacto, actualTelefonoContacto, actualFechaAprobacion,
	        			actualDocu, actualServ, actualDestinatario, actualRecibi, actualFechaRecibi, actualSupervision,
	        			actualDirectorProy, actualAutorProy, actualDirectorObra, actualEmpresaAdjud, actualFechaResolucion, actualPresuAdjud, actualCoordSegSalud, 
	        			actualActaReplanteo, actualFechaComienzo, actualFechaFinalizacion, actualProrrogas, actualActaRecepcion, actualCertificacionFinal, 
	        			actualResolucionCert, actualInformacionCambiosEIEL, actualReformados, actualLiquidacion, actualFechaLiquidacion, actualDetalles, 
	        			actualFechaSolicitud, actualFechaAprobacionPrincipado, idEntidad, municipio);
	        	planesObra.add(planObra);
		    }

		} catch (ConfigurationException e) {
			logger.error(e);
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
			ConnectionUtilities.closeConnection(null, preparedStatement2, rs2);
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
		}
		return planesObra;
	}
	
	@Override
	public Integer getNextIdDocumentPlanesObraSequence(Connection connection)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getNextIdDocumentSequence(connection);
	}

	@Override
	public Integer getNextIdPlanesObraSequence(Connection connection)
			throws SQLException {
		PostgresDAO pDAO = new PostgresDAO();
		return pDAO.getNextIdWarningSequence(connection);
	}
}

