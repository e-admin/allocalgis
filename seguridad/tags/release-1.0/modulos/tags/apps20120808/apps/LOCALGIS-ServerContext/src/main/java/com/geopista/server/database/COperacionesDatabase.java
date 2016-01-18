package com.geopista.server.database;

/** 
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

import com.geopista.app.AppContext;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.licencias.*;
import com.geopista.protocol.licencias.actividad.DatosActividad;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.estados.CEstadoResolucion;
import com.geopista.protocol.licencias.tipos.*;
import com.geopista.server.licencias.workflow.CConstantesWorkflow;
import com.geopista.server.licencias.workflow.CWorkflowLine;
import com.geopista.server.licencias.workflow.CWorkflowLiterales;
import com.geopista.server.licencias.teletramitacion.CConstantesTeletramitacion;
import com.geopista.server.licencias.teletramitacion.CTeletramitacion;
import com.localgis.sigem.client.ConsultaExpedientesWebService;
import com.localgis.sigem.client.ConsultaExpedientesWebServiceServiceClient;

import org.eclipse.jetty.plus.jaas.JAASRole;

import ieci.tecdoc.sgm.ct.ws.server.RetornoLogico;

import java.io.*;
import java.security.Principal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.*;

/**
 * @author SATEC
 * @version $Revision: 1.12 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/02/27 10:32:09 $
 *          $Name:  $
 *          $RCSfile: COperacionesDatabase.java,v $
 *          $Revision: 1.12 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class COperacionesDatabase {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesDatabase.class);


	private static boolean safeClose(ResultSet rs, Statement statement, Connection connection) {        

		try {

			if (connection!=null && !connection.isClosed())

				connection.commit();                     

		} catch (SQLException e) {               

			logger.error("Can't close connection",e);

		}
		try {

			if (rs!=null)
				rs.close();

		} catch (Exception e) {
			logger.error("Can't close result set",e);
		}
		try { 

			if (statement!=null)
				statement.close();

		} catch (Exception e) {
			logger.error("Can't close connection",e);
		}

		try {

			if (connection!=null && !connection.isClosed())
			{
				connection.close();
				CPoolDatabase.releaseConexion();   
			}

		} catch (SQLException e) {
			logger.error("Can't close connection",e);
		}  
		return true;

	}

	private static boolean safeClose(ResultSet rs, Statement statement, PreparedStatement preparedStatement, Connection connection) {

		try {

			if (preparedStatement!=null)
				preparedStatement.close();

		} catch (Exception e) {
			logger.error("Can't close prepared statement",e);
		}
		return safeClose(rs, statement, connection);

	}



	public static CPersonaJuridicoFisica getPersonaJuridicaByNif(String nifcif, String id_municipio) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

			String sql = "SELECT ID_PERSONA FROM PERSONA_JURIDICO_FISICA WHERE (id_municipio="+id_municipio+" or id_municipio=0) AND UPPER(A.dni_cif)=UPPER('" + nifcif + "')";

			logger.debug("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("Persona no encontrada");
				safeClose(rs, statement, connection);
				return null;
			}

			long idPersona = rs.getLong("ID_PERSONA");
			logger.debug("idPersona: " + idPersona);

			CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(idPersona);
			logger.debug("persona: " + persona);


			safeClose(rs, statement, connection);
			return persona;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


	public static CDatosNotificacion getDatosNotificacionByIdPersonaByIdSolicitud(long idPersona, long idSolicitud, Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

			String sql = "SELECT * FROM DATOS_NOTIFICACION WHERE ID_PERSONA=" + idPersona + " and ID_SOLICITUD='" + idSolicitud+"'";
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("Datos de Notificacion");
				safeClose(rs, statement, null);
				return null;
			}

			CDatosNotificacion datos = new CDatosNotificacion();

			safeClose(rs, statement, null);
			return datos;

		} catch (Exception ex) {

			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


	public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(long idPersona)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
		    String sql = "select A.*,B.* from PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B " +
                         "where A.ID_PERSONA=" + idPersona + " and A.ID_PERSONA=B.ID_PERSONA " +
                         " order by B.ID_SOLICITUD DESC";
			logger.debug("SQL=" + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				logger.info("Persona no encontrada:"+idPersona);
				safeClose(rs, statement, connection);
				return null;
			}

			//CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), rs.getString("OBSERVACION"));
            CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), "");
			logger.debug("viaNotificacion.getObservacion(): " + viaNotificacion.getObservacion());
			CDatosNotificacion datosNotificacion = new CDatosNotificacion(rs.getString("DNI_CIF"),
					viaNotificacion,
					rs.getString("FAX"),
					rs.getString("TELEFONO"),
					rs.getString("MOVIL"),
					rs.getString("EMAIL"),
					rs.getString("TIPO_VIA"),
					rs.getString("NOMBRE_VIA"),
					rs.getString("NUMERO_VIA"),
					rs.getString("PORTAL"),
					rs.getString("PLANTA"),
					rs.getString("ESCALERA"),
					rs.getString("LETRA"),
					rs.getString("CPOSTAL"),
					rs.getString("MUNICIPIO"),
					rs.getString("PROVINCIA"),
					rs.getString("NOTIFICAR"));
			CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), datosNotificacion);
			persona.setIdPersona(rs.getLong("ID_PERSONA"));
			safeClose(rs, statement, connection);
			logger.debug("persona.getIdPersona(): " + persona.getIdPersona());
			return persona;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al obtener los datos: " + ex.toString());
			return null;

		}

	}

    public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(ResultSet rs) {

            try {

                logger.debug("Inicio.");
                if (rs==null || rs.getString("ID_PERSONA")==null) {logger.info("Persona no encontrada");
                    return null;
                }
                CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), null);
                persona.setIdPersona(rs.getLong("ID_PERSONA"));
                logger.debug("persona.getIdPersona(): " + persona.getIdPersona());
                return persona;

            } catch (Exception ex) {
                logger.error("Exception al cargar los datos de persona: " ,ex);
                return null;

            }

        }

	public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(String nifCif, Connection connection, String id_municipio) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

			String sql = "select A.* FROM PERSONA_JURIDICO_FISICA A where (id_municipio="+id_municipio+" or id_municipio=0) AND UPPER(A.dni_cif)=UPPER('" + nifCif + "')";
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("Persona no encontrada");
				safeClose(rs, statement, null);
				return null;
			}

			CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), null);
			persona.setIdPersona(rs.getLong("ID_PERSONA"));


			safeClose(rs, statement, null);
			logger.debug("persona.getIdPersona(): " + persona.getIdPersona());
			return persona;

		} catch (Exception ex) {

			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}
    public static CWorkflowLine getWorkflowLine(long idEstado, int idTipoLicencia,Connection connection)
    {
        //Connection connection=null;
        try
        {
                //connection = CPoolDatabase.getConnection();
                if (connection == null) {
                    logger.warn("Cannot get connection");
                    return null;
                }
                CWorkflowLine line=getWorkflowLine(connection, idEstado, idTipoLicencia);
                safeClose(null, null, null);
        	    return line;
        }catch (Exception ex) {
            safeClose(null, null, null);
        	logger.error("Exception: " , ex);
			return null;
		}
    }
	public static CWorkflowLine getWorkflowLine(Connection connection, long idEstado, int idTipoLicencia)
    {
		Statement statement = null;
		ResultSet rs = null;
	    try
        {
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
			String sql = "select * FROM WORKFLOW WHERE ID_ESTADO=" + idEstado + " and id_tipo_licencia="+idTipoLicencia+" and ID_NEXTESTADO=" + idEstado;
			logger.debug("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				logger.info("WorkflowLine no encontrado para el estado. idEstado: " + idEstado);
				safeClose(rs, statement, null);
				return null;
			}
			CWorkflowLine workflowLine = new CWorkflowLine(rs.getInt("ID_ESTADO"), rs.getInt("ID_NEXTESTADO"), rs.getInt("PLAZO"), rs.getInt("ID_PLAZOESTADO"), rs.getString("EVENT_TEXT"), rs.getString("HITO_TEXT"), rs.getString("NOTIF_TEXT"));
			safeClose(rs, statement, null);
			return workflowLine;
		} catch (Exception ex) {
			safeClose(rs, statement, null);
			logger.error("Exception: " , ex);
			return null;
		}
	}


	public static synchronized long getTableSequence()
    {
    	try {Thread.sleep(10);} catch (Exception ex) {}
		long sequence = new Date().getTime();
		logger.debug("sequence: " + sequence);
		return sequence;
	}


	public static CPersonaJuridicoFisica insertUpdatePersonaJuridicoFisica(Connection connection, CPersonaJuridicoFisica personaJuridicoFisica, String idMunicipio) throws Exception {

		PreparedStatement preparedStatement = null;
		Statement statement = null;
		int muni = 0;
		if (idMunicipio != null){
			muni = Integer.parseInt(idMunicipio);
		}
		ResultSet rs = null;

		try {
			logger.debug("Inicio.");

			if (personaJuridicoFisica == null) {
				logger.info("PersonaJuridicoFisica no insertada/modificada. personaJuridicoFisica: " + personaJuridicoFisica);
				return null;

			}

			if (personaJuridicoFisica.getDniCif() != null) {
				personaJuridicoFisica.setDniCif(personaJuridicoFisica.getDniCif().toUpperCase());
			}

			logger.info("personaJuridicoFisica.getDniCif(): " + personaJuridicoFisica.getDniCif());
			CPersonaJuridicoFisica aux = getPersonaJuridicaFromDatabase(personaJuridicoFisica.getDniCif(),connection, idMunicipio);

			if (aux != null) {
				logger.info("Persona already exists. Updating.");
				personaJuridicoFisica.setIdPersona(aux.getIdPersona());
				preparedStatement = connection.prepareStatement("UPDATE PERSONA_JURIDICO_FISICA SET " +
						"DNI_CIF=?, NOMBRE=?,APELLIDO1=?,APELLIDO2=?,COLEGIO=?,VISADO=?," +
						"TITULACION=?, id_municipio=? WHERE ID_PERSONA=" + aux.getIdPersona());

				preparedStatement.setString(1, personaJuridicoFisica.getDniCif());
				preparedStatement.setString(2, personaJuridicoFisica.getNombre());
				preparedStatement.setString(3, personaJuridicoFisica.getApellido1());
				preparedStatement.setString(4, personaJuridicoFisica.getApellido2());
				preparedStatement.setString(5, personaJuridicoFisica.getColegio());
				preparedStatement.setString(6, personaJuridicoFisica.getVisado());
				preparedStatement.setString(7, personaJuridicoFisica.getTitulacion());
				preparedStatement.setInt(8, muni);

				preparedStatement.execute();


			} else {

				logger.info("Persona does not exists. Inserting.");


				long secuencia = getTableSequence();
				personaJuridicoFisica.setIdPersona(secuencia);

				preparedStatement = connection.prepareStatement("insert into PERSONA_JURIDICO_FISICA(ID_PERSONA,DNI_CIF,NOMBRE,APELLIDO1,APELLIDO2,COLEGIO,VISADO,TITULACION, id_municipio) VALUES(?,?,?,?,?,?,?,?,?)");
				preparedStatement.setLong(1, secuencia);
				preparedStatement.setString(2, personaJuridicoFisica.getDniCif());
				preparedStatement.setString(3, personaJuridicoFisica.getNombre());
				preparedStatement.setString(4, personaJuridicoFisica.getApellido1());
				preparedStatement.setString(5, personaJuridicoFisica.getApellido2());
				preparedStatement.setString(6, personaJuridicoFisica.getColegio());
				preparedStatement.setString(7, personaJuridicoFisica.getVisado());
				preparedStatement.setString(8, personaJuridicoFisica.getTitulacion());
				preparedStatement.setInt(9, muni);
				preparedStatement.execute();

			}

			safeClose(rs, statement, preparedStatement, null);
			return personaJuridicoFisica;

		} catch (Exception ex) {
			safeClose(rs, statement, preparedStatement, null);
			throw ex;


		}


	}


    private static void deleteTecnicosSolicitud(Connection connection, long idSolicitud) throws Exception {

        PreparedStatement preparedStatement= null;

        try {

            String sql = "delete from TECNICOS where ID_SOLICITUD=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, idSolicitud);

            preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }

    }


	private static Vector insertaAnexosSolicitud(Connection connection, long idSolicitud, Vector anexos) throws Exception {

        PreparedStatement preparedStatement= null;
		try {

			String sql = "INSERT INTO ANEXO ( ID_SOLICITUD, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION ) VALUES (?,?,?,?,?)";

			if (anexos == null) {
				logger.info("Anexos no insertados. anexos: " + anexos);
				return null;
			}


			Enumeration enumeration = anexos.elements();
			while (enumeration.hasMoreElements()) {

				long sequenceAnexo = getTableSequence();
               	CAnexo anexo = (CAnexo) enumeration.nextElement();
                anexo.setIdAnexo(sequenceAnexo);
				logger.debug("anexo.getContent(): " + anexo.getContent());

				String fileName = new String(anexo.getFileName());
				logger.debug("fileName: " + fileName);

				String path = "anexos" + File.separator + "licencias" + File.separator + idSolicitud + File.separator;
				logger.debug("path: " + path);


				try {

					if (!new File(path).exists()) {
						new File(path).mkdirs();
					}

					FileOutputStream out = new FileOutputStream(path + fileName);
					out.write(anexo.getContent());
					out.close();
					logger.info("Anexo created. path + fileName: " + path + fileName);

				} catch (Exception ex) {
					logger.error("Exception: " + ex.toString());
				}


				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1, idSolicitud);
				preparedStatement.setLong(2, sequenceAnexo);
                if (anexo.getTipoAnexo() != null){
				    preparedStatement.setInt(3, anexo.getTipoAnexo().getIdTipoAnexo());
                }else preparedStatement.setNull(3, java.sql.Types.INTEGER);
				preparedStatement.setString(4, fileName);
				preparedStatement.setString(5, anexo.getObservacion());

				preparedStatement.execute();
                safeClose(null, null, preparedStatement, null);
			}

		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
        return anexos;
	}


    private static void insertaDocumentacionEntregada(Connection connection, long idSolicitud, Vector documentacion) throws Exception {

        PreparedStatement preparedStatement= null;
        try {
            /** primero borramos todas las entradas */
            String sql = "DELETE FROM DOCUMENTACION_SOLICITUD WHERE ID_SOLICITUD=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, idSolicitud);
            preparedStatement.execute();
            connection.commit();
            safeClose(null, null, preparedStatement, null);

            /** insertamos la documenatcion entregada */
            sql = "INSERT INTO DOCUMENTACION_SOLICITUD (ID_DOCUMENTO, ID_SOLICITUD, PATRON_DOMINIO) VALUES (?,?,?)";

            if (documentacion == null) {
                logger.info("No existe docuementación");
                return;
            }

            Enumeration enumeration = documentacion.elements();
            while (enumeration.hasMoreElements()) {

                long sequence= getTableSequence();
                String patron= (String) enumeration.nextElement();

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, sequence);
                preparedStatement.setLong(2, idSolicitud);
                preparedStatement.setString(3, patron);
                preparedStatement.execute();
                safeClose(null, null, preparedStatement, null);
            }

            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }
    }


    public static boolean modificaAnexosSolicitud(long idSolicitud, Vector anexos, Mejora mejora, Alegacion alegacion)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return false;
            }
            if (anexos != null) {
                String path= "";
                long idComesFrom= -1;
                if (mejora != null){
                    /* anexos de mejora. Solo cuando el expediente esta en estado EsperaMejoras. */
                    path= "anexos" + File.separator + "licencias" + File.separator + idSolicitud + File.separator + "mejoras" + File.separator + mejora.getIdMejora() + File.separator;
                    idComesFrom= mejora.getIdMejora();
                }else if (alegacion != null){
                    /* anexos de alegacion. Solo cuando el expediente esta en estado EsperaAlegaciones. */
                    path= "anexos" + File.separator + "licencias" + File.separator + idSolicitud + File.separator + "alegacion" + File.separator + alegacion.getIdAlegacion() + File.separator;
                    idComesFrom= alegacion.getIdAlegacion();
                }else{
                    /* anexos de solicitud. En cualquier estado del expediente. */
                    path= "anexos" + File.separator + "licencias" + File.separator + idSolicitud + File.separator;
                }
                logger.info("Path de los anexos="+path);

                for (Enumeration e= anexos.elements(); e.hasMoreElements();){

                    CAnexo anexo= (CAnexo)e.nextElement();
                    logger.info("anexo.getFileName(): " + anexo.getFileName() + ". anexo.getEstado():" + anexo.getEstado());
                    switch (anexo.getEstado()){

                        case CConstantesComando.CMD_ANEXO_ADDED:
                            logger.info("Insertando anexo para:"+idSolicitud);
                            try {
                                if (!new File(path).exists()) {
                                    new File(path).mkdirs();
                                }
                                FileOutputStream out = new FileOutputStream(path + anexo.getFileName());
                                out.write(anexo.getContent());
                                out.close();
                                logger.info("Anexo created. path + fileName: " + path + anexo.getFileName());
                                /* NOTA: debido al conflicto generado al eliminar y annadir el mismo archivo (2 entradas para el mismo anexo en BD),
                                antes de insertar comprobamos que ya exista un anexo con el mismo URL_FICHERO */
                                //hay que especificar de donde viene el anexo
                                if (mejora!=null)
                                    anexo.setComesFrom("MEJORA");
                                if (alegacion!=null)
                                    anexo.setComesFrom("ALEGACION");
                                siExisteBorrarAnexo(idSolicitud, idComesFrom, anexo);
                                anexo.setIdAnexo(getTableSequence());
                                String sql = "INSERT INTO ANEXO ( ID_SOLICITUD, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION, COMES_FROM ) VALUES (?,?,?,?,?,?)";
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setLong(1, idSolicitud);
                                //preparedStatement.setLong(2, getTableSequence());
                                preparedStatement.setLong(2, anexo.getIdAnexo());
                                if (anexo.getTipoAnexo() != null){
                                                   preparedStatement.setInt(3, anexo.getTipoAnexo().getIdTipoAnexo());
                                }else preparedStatement.setNull(3, java.sql.Types.INTEGER);
			                    preparedStatement.setString(4, anexo.getFileName());
                                preparedStatement.setString(5, anexo.getObservacion());
                                if (anexo.getComesFrom() != null)
                                    preparedStatement.setString(6, anexo.getComesFrom());
                                else preparedStatement.setNull(6, java.sql.Types.VARCHAR);
                                preparedStatement.execute();

                                if (anexo.getComesFrom() != null||mejora!=null||alegacion!=null){
                                    if ((anexo.getComesFrom()!=null&&anexo.getComesFrom().equalsIgnoreCase("MEJORA"))
                                            ||mejora!=null){
                                        anexo.setComesFrom("MEJORA");
                                        insertarAnexoMejora(connection, anexo, mejora);
                                    }else if ((anexo.getComesFrom()!=null&&anexo.getComesFrom().equalsIgnoreCase("ALEGACION"))
                                            || alegacion!=null){
                                        anexo.setComesFrom("ALEGACION");
                                        insertarAnexoAlegacion(connection, anexo, alegacion);
                                    }
                                }
                                connection.commit();
                                safeClose(null, preparedStatement, null);
                            } catch (Exception ex) {
                                safeClose(null, null, preparedStatement, null);
                                logger.error("Exception: " ,ex);
                            }
                            logger.info("Anexo added.");
                        break;
                        case CConstantesComando.CMD_ANEXO_DELETED:
                            try {
                                if (anexo.getComesFrom() != null){
                                    if (anexo.getComesFrom().equalsIgnoreCase("MEJORA")){
                                        deleteAnexoMejora(connection, anexo, idSolicitud);
                                    }else if (anexo.getComesFrom().equalsIgnoreCase("ALEGACION")){
                                        deleteAnexoAlegacion(connection, anexo, idSolicitud);
                                    }
                                }

                                /** Un anexo que tenga que ser borrado de BD es porque tiene id */
                                if (anexo.getIdAnexo() != -1){
                                    //String sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND URL_FICHERO=?";
                                    String sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND ID_ANEXO=?";
                                    logger.info("DELETE FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud + " AND ID_ANEXO=" + anexo.getIdAnexo());

                                    preparedStatement = connection.prepareStatement(sql);
                                    preparedStatement.setLong(1, idSolicitud);
                                    //preparedStatement.setString(2, anexo.getFileName());
                                    preparedStatement.setLong(2, anexo.getIdAnexo());
                                    preparedStatement.execute();
                                    connection.commit();
                                    safeClose(null, null, preparedStatement, null);

                                    File file = new File(path + anexo.getFileName());
                                    if (!file.exists()) {
                                        logger.warn("File not found. path+anexo.getFileName(): " + path + anexo.getFileName());
                                        continue;

                                    }
                                    file.delete();
                                    logger.info("Anexo deleted.");

                                    /*
                                    File directory = new File(path);
                                    File[] listFiles= directory.listFiles();
                                    for(int i=0; i < listFiles.length; i++){
                                        File f= listFiles[i];
                                        if (f.isFile()){
                                            if (f.getName().equals(anexo.getFileName())){
                                                f.delete();
                                                logger.info("Anexo deleted.");
                                            }
                                        }
                                    }
                                    */
                                }

                            } catch (Exception ex) {
                                safeClose(null, null, preparedStatement, null);
                                logger.error("Exception: " + ex.toString());
                            }


                            break;


                        default:
                            logger.info("Not modified. anexo.getFileName(): " + anexo.getFileName());
                            /** Actulizamos el tipo y la descripcion por si han cambiado */
                            updateTipoAnexoSolicitud(connection, idSolicitud, anexo);
                    }

                }
            }

            safeClose(rs, statement, preparedStatement, connection);
            return true;

        } catch (Exception ex) {

            safeClose(rs, statement, preparedStatement, connection);
            logger.error("Exception: " ,ex);
            return false;

        }


    }


    private static boolean siExisteBorrarAnexo(long idSolicitud, long idComesFrom, CAnexo anexo) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet rs = null;

        try {

            logger.debug("Inicio.");


            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return false;
            }

            /* NOTA: debido al conflicto generado al eliminar y annadir el mismo archivo (2 entradas para el mismo anexo en BD),
               antes de insertar comprobamos que ya exista un anexo con el mismo URL_FICHERO, y si es asi,
               borramos la entrada de BD.
               NOTA: no puede haber 2 anexos con el mismo nombre para la misma solicitud, la misma mejora y la misma alegacion. */

            String sql= "";
            if (anexo.getComesFrom() != null){
                if (anexo.getComesFrom().equalsIgnoreCase("MEJORA")){
                    sql= "SELECT A.* FROM ANEXO A, ANEXOS_MEJORA M WHERE A.ID_SOLICITUD=" + idSolicitud +
                         " AND A.URL_FICHERO='"+anexo.getFileName()+ "'" +
                         " AND A.COMES_FROM='"+anexo.getComesFrom()+ "'" +
                         " AND A.ID_SOLICITUD=M.ID_SOLICITUD" +
                         " AND A.ID_ANEXO=M.ID_ANEXO" +
                         " AND M.ID_MEJORA="+idComesFrom;
                    logger.info("SQL="+sql);
                }else if (anexo.getComesFrom().equalsIgnoreCase("ALEGACION")){
                    sql= "SELECT A.* FROM ANEXO A, ANEXOS_ALEGACION L WHERE A.ID_SOLICITUD=" + idSolicitud +
                         " AND A.URL_FICHERO='"+anexo.getFileName()+ "'" +
                         " AND A.COMES_FROM='"+anexo.getComesFrom()+ "'" +
                         " AND A.ID_SOLICITUD=L.ID_SOLICITUD" +
                         " AND A.ID_ANEXO=L.ID_ANEXO" +
                         " AND L.ID_ALEGACION="+idComesFrom;
                }
            }else{
                sql= "SELECT * FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud +" AND URL_FICHERO='"+anexo.getFileName()+ "'";
            }

            logger.debug("SQL="+sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                CAnexo encontrado= new CAnexo();
                encontrado.setIdAnexo(rs.getLong("ID_ANEXO"));
                encontrado.setComesFrom(rs.getString("COMES_FROM"));

                /** comprobamos que el anexo sea de MEJORA o de ALEGACION */
                if (encontrado.getComesFrom() != null){
                    if (encontrado.getComesFrom().equalsIgnoreCase("MEJORA")){
                        /** borramos de ANEXOS_MEJORA */
                        deleteAnexoMejora(connection, encontrado, idSolicitud);
                    }else if (encontrado.getComesFrom().equalsIgnoreCase("ALEGACION")){
                        /** borramos de ANEXOS_ALEGACION */
                        deleteAnexoAlegacion(connection, encontrado, idSolicitud);
                    }
                }
                /** borramos de ANEXO */
                //sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND URL_FICHERO=?";
                sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND ID_ANEXO=?";
                logger.info("DELETE FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud + " AND ID_ANEXO=" + encontrado.getIdAnexo());

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, idSolicitud);
                preparedStatement.setLong(2, encontrado.getIdAnexo());
                preparedStatement.execute();
                connection.commit();
            }

            safeClose(rs, statement, preparedStatement, connection);
            return true;

        } catch (Exception ex) {

            safeClose(rs, statement, preparedStatement, connection);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;

        }

    }

    private static void updateTipoAnexoSolicitud(Connection connection, long idSolicitud, CAnexo anexo) throws Exception {

        PreparedStatement preparedStatement= null;
        try {

            String sql = "UPDATE ANEXO SET ID_TIPO_ANEXO=?, OBSERVACION=? WHERE ID_SOLICITUD=" + idSolicitud + " and ID_ANEXO=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, anexo.getTipoAnexo().getIdTipoAnexo());
            preparedStatement.setString(2, anexo.getObservacion());
            preparedStatement.setLong(3, anexo.getIdAnexo());
            preparedStatement.execute();
            connection.commit();
            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }
    }



	private static void deleteReferenciasCatastrales(Connection connection, long idSolicitud) throws Exception {
		PreparedStatement preparedStatement= null;
        try {
			String sql = "DELETE FROM PARCELARIO WHERE ID_SOLICITUD=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, idSolicitud+"");

			preparedStatement.execute();
			safeClose(null, null, preparedStatement, null);
		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
	}


	private static void insertaReferenciasCatastrales(Connection connection, long secuencia, Vector referenciasCatastrales, String idMunicipio, String vu) throws Exception {
		PreparedStatement preparedStatement= null;

        try {

			long tiempoInicial = new Date().getTime();

			String sql = "INSERT INTO PARCELARIO (ref_catastral,id_solicitud,tipo_via,nombre_via,numero,letra,bloque,escalera,planta,puerta, codigo_postal) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			if (referenciasCatastrales == null) {
				logger.info("referenciasCatastrales no insertadas. referenciasCatastrales: " + referenciasCatastrales);
				return;
			}
			Enumeration enumeration = referenciasCatastrales.elements();
			while (enumeration.hasMoreElements()) {
				CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) enumeration.nextElement();

                /** Comprobamos si el expediente viene de ventanilla única. Solo necesario en la creacion. */
                if ((vu != null) && (vu.equalsIgnoreCase("1"))){
                    Hashtable hash = new Hashtable();
                    hash.put("c.referencia_catastral", referenciaCatastral.getReferenciaCatastral());
                    Vector v= getSearchedAddresses(hash, idMunicipio);
                    if ((v != null) && (v.size() > 0)){
                        CReferenciaCatastral refCatastral= (CReferenciaCatastral) v.elementAt(0);

                        referenciaCatastral.setTipoVia(refCatastral.getTipoVia());
                        referenciaCatastral.setNombreVia(refCatastral.getNombreVia());
                        referenciaCatastral.setPrimerNumero(refCatastral.getPrimerNumero());
                        referenciaCatastral.setPrimeraLetra(refCatastral.getPrimeraLetra());
                        referenciaCatastral.setBloque(refCatastral.getBloque());
                        referenciaCatastral.setEscalera(refCatastral.getEscalera());
                        referenciaCatastral.setPlanta(refCatastral.getPlanta());
                        referenciaCatastral.setPuerta(refCatastral.getPuerta());
                        referenciaCatastral.setCPostal(refCatastral.getCPostal());
                    }
                }
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, referenciaCatastral.getReferenciaCatastral());
				preparedStatement.setLong(2, secuencia);
                if (referenciaCatastral.getTipoVia() != null){
				    preparedStatement.setString(3, referenciaCatastral.getTipoVia());
                }else{
                    preparedStatement.setNull(3, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getNombreVia() != null){
				    preparedStatement.setString(4, referenciaCatastral.getNombreVia());
                }else{
                    preparedStatement.setNull(4, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getPrimerNumero() != null){
				    preparedStatement.setString(5, referenciaCatastral.getPrimerNumero());
                }else{
                    preparedStatement.setNull(5, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getPrimeraLetra() != null){
    				preparedStatement.setString(6, referenciaCatastral.getPrimeraLetra());
                }else{
                    preparedStatement.setNull(6, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getBloque() != null){
				    preparedStatement.setString(7, referenciaCatastral.getBloque());
                }else{
                    preparedStatement.setNull(7, java.sql.Types.VARCHAR);
                }
                if(referenciaCatastral.getEscalera() != null){
				    preparedStatement.setString(8, referenciaCatastral.getEscalera());
                }else{
                    preparedStatement.setNull(8, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getPlanta() != null){
				    preparedStatement.setString(9, referenciaCatastral.getPlanta());
                }else{
                    preparedStatement.setNull(9, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getPuerta() != null){
				    preparedStatement.setString(10, referenciaCatastral.getPuerta());
                }else{
                    preparedStatement.setNull(10, java.sql.Types.VARCHAR);
                }
                if (referenciaCatastral.getCPostal() != null){
				    preparedStatement.setString(11, referenciaCatastral.getCPostal());
                }else{
                    preparedStatement.setNull(11, java.sql.Types.VARCHAR);
                }

				preparedStatement.execute();
				safeClose(null, null, preparedStatement, null);
			}

			long tiempoFinal = new Date().getTime();
			logger.debug("tiempoFinal-tiempoInicial: " + (tiempoFinal - tiempoInicial));


		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
		}
	}


	public static CResultadoOperacion crearExpedienteLicencias(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia, Principal userPrincipal, String idMunicipio) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet rs = null;
		int muni = 0;
		if (idMunicipio != null){
			muni = Integer.parseInt(idMunicipio);
		}
		
		try {

			logger.debug("Inicio.");
			logger.info("expedienteLicencia.getVU(): " + expedienteLicencia.getVU());

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}


			connection.setAutoCommit(false);
			CPersonaJuridicoFisica propietario = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPropietario(), idMunicipio);
			connection.commit();
			CPersonaJuridicoFisica representante = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getRepresentante(), idMunicipio);
			connection.commit();
            Vector tecnicos= new Vector();
            if (solicitudLicencia.getTecnicos() != null){
                for (Enumeration e= solicitudLicencia.getTecnicos().elements(); e.hasMoreElements();){
                    CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)e.nextElement();
                    tecnico = insertUpdatePersonaJuridicoFisica(connection, tecnico, idMunicipio);
                    connection.commit();
                    tecnicos.add(tecnico);
                }
            }


            CPersonaJuridicoFisica promotor = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPromotor(), idMunicipio);
			connection.commit();

			long secuencia = getTableSequence();

			//****************************************
			//** Insertamos en SOLICITUD_LICENCIA
			//****************************************************
			preparedStatement = connection.prepareStatement("INSERT INTO SOLICITUD_LICENCIA ( ID_SOLICITUD, ID_TIPO_LICENCIA, ID_TIPO_OBRA, PROPIETARIO, REPRESENTANTE, TECNICO, PROMOTOR, NUM_ADMINISTRATIVO, CODIGO_ENTRADA, UNIDAD_TRAMITADORA, UNIDAD_DE_REGISTRO, MOTIVO, ASUNTO, FECHA, FECHA_ENTRADA, TASA, TIPO_VIA_AFECTA, NOMBRE_VIA_AFECTA, NUMERO_VIA_AFECTA, PORTAL_AFECTA, PLANTA_AFECTA, LETRA_AFECTA, CPOSTAL_AFECTA, MUNICIPIO_AFECTA, PROVINCIA_AFECTA, OBSERVACIONES,NOMBRE_COMERCIAL,ID_MUNICIPIO, OBSERVACIONES_DOC_ENTREGADA, FECHA_RESOLUCION, IMPUESTO, FECHA_LIMITE_OBRA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setInt(2, solicitudLicencia.getTipoLicencia().getIdTipolicencia());


			preparedStatement.setInt(3, solicitudLicencia.getTipoObra().getIdTipoObra());


			preparedStatement.setLong(4, propietario.getIdPersona());

			if (representante != null) {
				preparedStatement.setLong(5, representante.getIdPersona());
			} else
				preparedStatement.setLong(5, 0);
            /*
			if (tecnico != null) {
				preparedStatement.setLong(6, tecnico.getIdPersona());
			} else {
				preparedStatement.setNull(6, java.sql.Types.LONGVARBINARY);
			}
            */
            preparedStatement.setLong(6, 0);

			if (promotor != null) {
				preparedStatement.setLong(7, promotor.getIdPersona());
			} else {
				preparedStatement.setLong(7, 0);
			}

			preparedStatement.setString(8, solicitudLicencia.getNumAdministrativo());
			preparedStatement.setString(9, solicitudLicencia.getCodigoEntrada());
			preparedStatement.setString(10, solicitudLicencia.getUnidadTramitadora());
			preparedStatement.setString(11, solicitudLicencia.getUnidadDeRegistro());
			preparedStatement.setString(12, solicitudLicencia.getMotivo());
			preparedStatement.setString(13, solicitudLicencia.getAsunto());
			preparedStatement.setTimestamp(14, new Timestamp(solicitudLicencia.getFecha().getTime()));
			preparedStatement.setTimestamp(15, new Timestamp(new Date().getTime()));

			preparedStatement.setDouble(16, solicitudLicencia.getTasa());
            if (solicitudLicencia.getTipoViaAfecta() != null){
			    preparedStatement.setString(17, solicitudLicencia.getTipoViaAfecta());
            }else{
                preparedStatement.setNull(17, java.sql.Types.VARCHAR);
            }
            if (solicitudLicencia.getNombreViaAfecta() != null){
			    preparedStatement.setString(18, solicitudLicencia.getNombreViaAfecta());
            }else{
                preparedStatement.setNull(18, java.sql.Types.VARCHAR);
            }
            if (solicitudLicencia.getNumeroViaAfecta() != null){
			    preparedStatement.setString(19, solicitudLicencia.getNumeroViaAfecta());
            }else{
                preparedStatement.setNull(19, java.sql.Types.VARCHAR);
            }
            if(solicitudLicencia.getPortalAfecta() != null){
			    preparedStatement.setString(20, solicitudLicencia.getPortalAfecta());
            }else{
                preparedStatement.setNull(20, java.sql.Types.VARCHAR);
            }
            if (solicitudLicencia.getPlantaAfecta() != null){
			    preparedStatement.setString(21, solicitudLicencia.getPlantaAfecta());
            }else{
                preparedStatement.setNull(21, java.sql.Types.VARCHAR);
            }
            if (solicitudLicencia.getLetraAfecta() != null){
			    preparedStatement.setString(22, solicitudLicencia.getLetraAfecta());
            }else{
                preparedStatement.setNull(22, java.sql.Types.VARCHAR);
            }
            if (solicitudLicencia.getCpostalAfecta() != null){
			    preparedStatement.setString(23, solicitudLicencia.getCpostalAfecta());
            }else{
                preparedStatement.setNull(23, java.sql.Types.VARCHAR);
            }
			preparedStatement.setString(24, solicitudLicencia.getMunicipioAfecta());
			preparedStatement.setString(25, solicitudLicencia.getProvinciaAfecta());
			preparedStatement.setString(26, solicitudLicencia.getObservaciones());
			preparedStatement.setString(27, solicitudLicencia.getNombreComercial());
			preparedStatement.setInt(28, muni);
            preparedStatement.setString(29, solicitudLicencia.getObservacionesDocumentacionEntregada());
            if (solicitudLicencia.getFechaResolucion() != null) {
                preparedStatement.setTimestamp(30, new Timestamp(solicitudLicencia.getFechaResolucion().getTime()));
            } else {
                preparedStatement.setNull(30, java.sql.Types.TIMESTAMP);
            }
            preparedStatement.setDouble(31, solicitudLicencia.getImpuesto());
            if (solicitudLicencia.getFechaLimiteObra() != null) {
                preparedStatement.setTimestamp(32, new Timestamp(solicitudLicencia.getFechaLimiteObra().getTime()));
            } else {
                preparedStatement.setNull(32, java.sql.Types.TIMESTAMP);
            }

			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

            /** Insertamos los datos de actividad */
            if (solicitudLicencia.getDatosActividad() != null){
                insertarDatosActividad(connection, secuencia, solicitudLicencia.getDatosActividad());
            }

            /** Insertamos la lista de tecnicos */
            // NOTA: Una licencia de obra Mayor, puede tener varios tecnicos y no uno solo como se contemplaba
            //       hasta ahora en el modelo de datos. Para ello ha sido necesario crear la tabla tecnicos
            /**/
            if (tecnicos != null){
                insertaTecnicosSolicitud(connection, secuencia, tecnicos);
            }

			//****************************************
			//** Insertamos los anexos
			//****************************************************
			insertaAnexosSolicitud(connection, secuencia, solicitudLicencia.getAnexos());

            //**********************************************************
            //** Insertamos la documentacion entregada para una licencia
            //**********************************************************
            insertaDocumentacionEntregada(connection, secuencia, solicitudLicencia.getDocumentacionEntregada());


			//****************************************
			//** Insertamos las referencias catastrales
			//****************************************************
			insertaReferenciasCatastrales(connection, secuencia, solicitudLicencia.getReferenciasCatastrales(), idMunicipio, expedienteLicencia.getVU());



			//****************************************
			//** Insertamos los datos de notificacion de las personas juridicas
			//****************************************************
			insertUpdateDatosNotificacion(connection, propietario, secuencia);
            connection.commit();
			if (representante != null) {
				insertUpdateDatosNotificacion(connection, representante, secuencia);
                connection.commit();
			}
            /*
			if (tecnico != null) {
				insertUpdateDatosNotificacion(connection, tecnico, secuencia);
			}
            */
            for (Enumeration enumerationElement = tecnicos.elements(); enumerationElement.hasMoreElements();){
                CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)enumerationElement.nextElement();
                insertUpdateDatosNotificacion(connection, tecnico, secuencia);
                connection.commit();
            }
			if (promotor != null) {
				insertUpdateDatosNotificacion(connection, promotor, secuencia);
                connection.commit();
			}


			String numExpediente = "E_" + secuencia;
			expedienteLicencia.setNumExpediente(numExpediente);
			//****************************************
			//** Insertamos en EXPEDIENTE
			//****************************************************
			preparedStatement = connection.prepareStatement("INSERT INTO EXPEDIENTE_LICENCIA(ID_SOLICITUD,NUM_EXPEDIENTE,ID_TRAMITACION,ID_FINALIZACION,ID_ESTADO,SERVICIO_ENCARGADO,ASUNTO,SILENCIO_ADMINISTRATIVO,FORMA_INICIO,NUM_FOLIOS,FECHA_APERTURA,RESPONSABLE,PLAZO_RESOLUCION,HABILES,NUM_DIAS,OBSERVACIONES,VU,CNAE,app_string,BLOQUEADO) VALUES(?,?,0,null,?,?,null,0,?,null,?,?,?,0,null,'',?,?,?,'0')");
			preparedStatement.setLong(1, secuencia);
			preparedStatement.setString(2, numExpediente);
			preparedStatement.setInt(3, expedienteLicencia.getEstado().getIdEstado());
            preparedStatement.setString(4, expedienteLicencia.getServicioEncargado());
            preparedStatement.setString(5,expedienteLicencia.getFormaInicio());
			preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
            preparedStatement.setString(7, expedienteLicencia.getResponsable());
			preparedStatement.setNull(8, java.sql.Types.TIMESTAMP);
			preparedStatement.setString(9, expedienteLicencia.getVU());
			preparedStatement.setString(10, expedienteLicencia.getCNAE());
			preparedStatement.setString(11, CConstantesComando.APP_LICENCIAS);
			preparedStatement.execute();

			connection.commit();
			connection.setAutoCommit(true);
			

			expedienteLicencia.setIdSolicitud(secuencia);
			//******************************************************
			//** Insercion de historico, notif., eventos, etc
			//*******************************************

			String userPrincipalName = "";

			if (userPrincipal == null) {
				userPrincipalName = "teletramitacion";
			} else {
				userPrincipalName = userPrincipal.getName();
			}

			insertHistoricoExpediente(expedienteLicencia, userPrincipalName, true,connection);
			insertEventoExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                                  true, false,connection);

			//******************************************************
			//** Ventanilla única
			//*******************************************
			if (expedienteLicencia.getVU().equals("1")) {
				COperacionesDatabase.inicializarConsultaTelematica(expedienteLicencia, solicitudLicencia);
			}
			safeClose(rs, null, preparedStatement, connection);
			return new CResultadoOperacion(true, numExpediente);

		} catch (Exception ex) {

			safeClose(rs, null, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}
		finally{
			safeClose(rs, null, preparedStatement, connection);
		}

	}


	public static Vector getTiposLicencia() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}


			String sql = "SELECT * FROM TIPO_LICENCIA ORDER BY ID_TIPO_LICENCIA ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposLicencia = new Vector();
			while (rs.next()) {

				CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				tiposLicencia.add(tipoLicencia);

			}

			safeClose(rs, statement, connection);
			//rs.close();
			//statement.close();
			//connection.close();


			return tiposLicencia;

		} catch (Exception ex) {
			/*try {
				rs.close();
			} catch (Exception ex2) {
			}
			try {
				statement.close();
			} catch (Exception ex2) {
			}
			try {
				connection.close();
			} catch (Exception ex2) {
			}*/
			safeClose(rs, statement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	private static String getConditions(Hashtable hash) {

		try {
			String conditions = "";

			if (hash == null) {
				return "";
			}

			Enumeration enumerationElement = hash.keys();

			while (enumerationElement.hasMoreElements()) {

				conditions += " and ";

				String field = (String) enumerationElement.nextElement();
				String value = (String) hash.get(field);

				logger.debug("field: " + field);
				logger.debug("value: " + value);


				if (field.startsWith("BETWEEN*")) {
					try {

						logger.debug("field: " + field);
						logger.debug("value: " + value);

						StringTokenizer st = new StringTokenizer(field);
						st.nextToken("*");
						String dateField = st.nextToken("*");
						logger.debug("dateField: " + dateField);

						st = new StringTokenizer(value);
						String desdeField = st.nextToken("*");
						logger.debug("desdeField: " + desdeField);

						String hastaField = st.nextToken("*");
						logger.debug("hastaField: " + hastaField);

						conditions += "" + dateField + " between date '" + desdeField + "' and date '" + hastaField + "'";
						continue;
					} catch (Exception ex) {
						logger.error("Exception: " + ex.toString());
						continue;
					}
				}
				//parche para obtener las licencias por un listado de ids
				else if (field.startsWith("A.ID_SOLICITUD IN")) {
					conditions += "A.ID_SOLICITUD IN (" + value + ")";
					continue;
				}
				if (value != null && value.length() > 0){
                    // Para los ID, busqueda exacta.
                    if ((field.indexOf("TIPO_LICENCIA") != -1) || (field.indexOf("TIPO_OBRA") != -1) || (field.indexOf("ID_ESTADO") != -1))
                        conditions += " upper(" + field + "::text) = upper('" + value + "')";
                    else conditions += " upper(" + field + "::text) like upper('%" + value + "%')";
                }
				else{
					//conditions += "" + field + " like '" + value + "%'";
                    conditions += "((upper(" + field + "::text) like upper('" + value + "%')) or (" + field + " is null))";
                }

			}

			return conditions;

		} catch (Exception ex) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return "";

		}

	}

	public static Vector getSearchedExpedientes(Hashtable hash, Enumeration permisos,
                                                String idMunicipio, Vector tiposLicencia) {

		Vector expedientes = getSearchedExpedientes(hash, idMunicipio, tiposLicencia);
		logger.info("expedientes.size(): " + expedientes.size());

		return expedientes;

	}

    /*
	public static Vector getEstadosPermitidos(CExpedienteLicencia expedienteLicencia, Enumeration userPerms, int idTipoLicencia)
    {
	    try
        {
            return  getEstadosDisponibles(expedienteLicencia, idTipoLicencia);

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();
		}

	}
    */

    public static Vector getEstadosPermitidos(CExpedienteLicencia expedienteLicencia, Enumeration userPerms, int idTipoLicencia)
    {
        try
        {
            Hashtable permisosUsuario= new Hashtable();
            while (userPerms.hasMoreElements()) {
                JAASRole permiso = (JAASRole) userPerms.nextElement();
                if (!permisosUsuario.containsKey(permiso.toString())){
                    permisosUsuario.put(permiso.toString(), "");
                }
            }
            Vector estadosPermitidos= new Vector();
            Vector estadosDisponibles= getEstadosDisponibles(expedienteLicencia, idTipoLicencia);
            for (int i = 0; i < estadosDisponibles.size(); i++) {
                CEstado estado= (CEstado) estadosDisponibles.elementAt(i);
                if ((estado.getDescPermiso() == null) || (estado.getDescPermiso().equals("")) || (permisosUsuario.get(estado.getDescPermiso()) != null)){
                    estadosPermitidos.add(estado);
                }
            }
            return estadosPermitidos;

        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();
        }

    }



	public static Vector getSearchedExpedientes(Hashtable hash, String idMunicipio,Vector tiposLicencia)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}
     		String conditions = getConditions(hash);
    		String sql = "select E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA, " +
                    "E.ID_TIPO_LICENCIA as E_ID_TIPO_LICENCIA, E.ID_TIPO_OBRA as E_ID_TIPO_OBRA, " +
                    "E.FECHA_LIMITE_OBRA, A.*,F.DNI_CIF AS NIFPROPIETARIO " +
                   	"from expediente_licencia A " +
					"JOIN solicitud_licencia E " +
					"ON a.ID_SOLICITUD=e.ID_SOLICITUD " +
					"JOIN persona_juridico_fisica F " +
					"ON e.propietario=f.id_persona " +
					" where " +
                    //ASO cambia el nombre de la aplicacion por el tipo de obra MAYOR O MENOR
                    //" A.APP_STRING='" + CConstantesComando.APP_LICENCIAS + "' " +
                    //" (E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MAYOR+ " OR  "+
                    //"  E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MENOR+ ") "+
				    (dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and e.id_municipio='" + idMunicipio + "' "):
                        (idMunicipio == null ? "" : "and E.id_municipio='" + idMunicipio + "' "))
                  	 + conditions + " order by FECHA_APERTURA DESC";


			logger.info("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector expedientes = new Vector();
			while (rs.next()) {
				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
				CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
                       tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
				//Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));
                //ASO elimina
				logger.debug("despues de getNotificacionesExpediente");
				CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
						rs.getLong("ID_SOLICITUD"),
						tipoTramitacion,
						tipoFinalizacion,
						rs.getString("SERVICIO_ENCARGADO"),
						rs.getString("ASUNTO"),
						rs.getString("SILENCIO_ADMINISTRATIVO"),
						rs.getString("FORMA_INICIO"),
						rs.getInt("NUM_FOLIOS"),
						rs.getTimestamp("FECHA_APERTURA"),
						rs.getString("RESPONSABLE"),
						rs.getDate("PLAZO_RESOLUCION"),
						rs.getString("HABILES"),
						rs.getInt("NUM_DIAS"),
						rs.getString("OBSERVACIONES"),
						estado,
						null);
				expediente.setVU(rs.getString("VU"));
				expediente.setPlazoEvent(rs.getString("PLAZO_EVENT"));
				expediente.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
				expediente.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
				expediente.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));
				expediente.setTipoLicenciaDescripcion(new Integer(rs.getInt("E_ID_TIPO_LICENCIA")).toString());
                expediente.setPatronTipoObraSolicitud(new Integer(rs.getInt("E_ID_TIPO_OBRA")).toString());
				expediente.setNifPropietario(rs.getString("NIFPROPIETARIO"));
				expediente.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
				expediente.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
				expediente.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));
                expediente.setBloqueado(rs.getString("BLOQUEADO"));
                expediente.setTipoLicenciaDescripcion(rs.getString("E_ID_TIPO_LICENCIA"));
                ///
                ///solicitud del expediente:
                CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("E_ID_TIPO_LICENCIA"), "", "");
                CTipoObra tipoObra = new CTipoObra(rs.getInt("E_ID_TIPO_OBRA"), "", "");
                CSolicitudLicencia solicitudLicencia = new com.geopista.protocol.licencias.CSolicitudLicencia();
                solicitudLicencia.setTipoLicencia(tipoLicencia);
                solicitudLicencia.setTipoObra(tipoObra);
                solicitudLicencia.setFechaLimiteObra(rs.getDate("FECHA_LIMITE_OBRA"));
                expediente.setSolicitud(solicitudLicencia);
				expedientes.add(expediente);
			}
			//safeClose(rs, statement, connection);
			logger.debug("expedientes: " + expedientes);
			return expedientes;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al buscar expedientes: " ,ex);
			return new Vector();
		}
		finally{
			safeClose(rs, statement, connection);
		}
	}

    private static String getConditionsPlus(Hashtable hash)
    {
        String sCondicion="";
        if (hash==null) return sCondicion;
        for(Enumeration e=hash.keys();e.hasMoreElements();)
        {
           sCondicion=sCondicion +" and "+ (String)e.nextElement();
        }
        return sCondicion;
    }
    private static PreparedStatement setCampos(PreparedStatement ps, Hashtable hash) throws Exception
    {
            if (hash==null) return ps;
            int i=1;
            for (Enumeration e=hash.elements();e.hasMoreElements();)
            {
                Parametro parametro=(Parametro)e.nextElement();

                if (parametro.isTipoString())
                    ps.setString(i,(String)parametro.getValor());
                else if (parametro.isTipoDate())
                    ps.setDate(i,new java.sql.Date(((Date)parametro.getValor()).getTime()));
                else
                   logger.warn("No se ha encontrado EL TIPO de : "+parametro.getValor());
                i++;
            }
            return ps;
    }
    public static Vector getSearchedReferenciasPlanos(Hashtable hash, String idMunicipio,Vector tiposLicencia)
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }
            String conditions = getConditionsPlus(hash);
            String sql = "select distinct(parcelario.ref_catastral) from expediente_licencia A " +
                    "                    JOIN solicitud_licencia E " +
                    "                   ON a.ID_SOLICITUD=e.ID_SOLICITUD " +
                    "                    JOIN parcelario   " +
                    "          ON a.ID_SOLICITUD=parcelario.ID_SOLICITUD " +
                    " where " +
                    (dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and id_municipio='" + idMunicipio + "' "):
                        (idMunicipio == null ? "" : "and id_municipio='" + idMunicipio + "' "))
                       + conditions ;
            ps = connection.prepareStatement(sql);
            ps= setCampos(ps,hash);
            rs = ps.executeQuery();
            Vector referencias = new Vector();
            while (rs.next()) {
                CReferenciaCatastral ref= new CReferenciaCatastral();
                ref.setReferenciaCatastral(rs.getString("ref_catastral"));
                referencias.add(ref);
            }
            safeClose(rs, ps, connection);
            return referencias;
        } catch (Exception ex) {
            safeClose(rs, ps, connection);
            logger.error("Exception al buscar expedientes: " ,ex);
            return new Vector();
        }
    }

    public static Vector getSearchedExpedientesPlanos(Hashtable hash, String idMunicipio,Vector tiposLicencia)
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }
            String conditions = getConditionsPlus(hash);
            String sql = "select E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA,  " +
                    "E.ID_TIPO_LICENCIA as E_ID_TIPO_LICENCIA, E.ID_TIPO_OBRA as E_ID_TIPO_OBRA, A.*,F.DNI_CIF AS NIFPROPIETARIO " +
                       "from expediente_licencia A " +
                    "JOIN solicitud_licencia E " +
                    "ON a.ID_SOLICITUD=e.ID_SOLICITUD " +
                    "JOIN persona_juridico_fisica F " +
                    "ON e.propietario=f.id_persona " +
                    " where " +
                    (dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and E.id_municipio='" + idMunicipio + "' "):
                        (idMunicipio == null ? "" : "and E.id_municipio='" + idMunicipio + "' "))
                       + conditions + " order by FECHA_APERTURA DESC";
            logger.info("SQL:"+sql);
            ps = connection.prepareStatement(sql);
            ps= setCampos(ps,hash);
            rs = ps.executeQuery();
            Vector expedientes = new Vector();
            while (rs.next()) {
                CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
                CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
                       tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
                CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
                        rs.getLong("ID_SOLICITUD"),
                        tipoTramitacion,
                        tipoFinalizacion,
                        rs.getString("SERVICIO_ENCARGADO"),
                        rs.getString("ASUNTO"),
                        rs.getString("SILENCIO_ADMINISTRATIVO"),
                        rs.getString("FORMA_INICIO"),
                        rs.getInt("NUM_FOLIOS"),
                        rs.getTimestamp("FECHA_APERTURA"),
                        rs.getString("RESPONSABLE"),
                        rs.getDate("PLAZO_RESOLUCION"),
                        rs.getString("HABILES"),
                        rs.getInt("NUM_DIAS"),
                        rs.getString("OBSERVACIONES"),
                        estado,
                        null);
                expediente.setVU(rs.getString("VU"));
                expediente.setPlazoEvent(rs.getString("PLAZO_EVENT"));
                expediente.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
                expediente.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
                expediente.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));
                expediente.setTipoLicenciaDescripcion(new Integer(rs.getInt("E_ID_TIPO_LICENCIA")).toString());
                expediente.setPatronTipoObraSolicitud(new Integer(rs.getInt("E_ID_TIPO_OBRA")).toString());
                expediente.setNifPropietario(rs.getString("NIFPROPIETARIO"));
                expediente.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                expediente.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                expediente.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));
                expediente.setBloqueado(rs.getString("BLOQUEADO"));
                expedientes.add(expediente);
            }
            safeClose(rs, ps, connection);
            logger.debug("expedientes: " + expedientes);
            return expedientes;
        } catch (Exception ex) {
            safeClose(rs, ps, connection);
            logger.error("Exception al buscar expedientes: " ,ex);
            return new Vector();
        }
    }
    public static String dameTipos(String campo, Vector tiposLicencia)
    {
        if (campo==null || tiposLicencia==null || tiposLicencia.size()==0)
            return null;
        String condicion=" (";
        for (Enumeration e=tiposLicencia.elements();e.hasMoreElements();)
        {
            CTipoLicencia auxTipo= (CTipoLicencia) e.nextElement();
            condicion= condicion+campo+"="+auxTipo.getIdTipolicencia();
            if (e.hasMoreElements())
                condicion=condicion +" OR ";
        }
        condicion=condicion+") ";
        return condicion;
    }

	public static Vector getSearchedReferenciasCatastrales(Hashtable hash, String idMunicipio)
    {

    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}
			String conditions = getConditions(hash);
            /* PREVIO ADAPTACION GEOPISTA POR CAMBIOS EN TABLA PARCELAS
            String sql = "select distinct parcelas.referencia_catastral as ID, vias.tipovianormalizadocatastro, vias.nombrecatastro, "+
                         "parcelas.primer_numero, parcelas.primera_letra, parcelas.bloque, parcelas.escalera, parcelas.planta, parcelas.puerta, parcelas.codigo_postal "+
                         "from parcelas LEFT JOIN vias " +
                         //"ON parcelas.id_via=vias.id " +
                         "ON parcelas.id_via=vias.codigocatastro " +
                         "where parcelas.referencia_catastral IS NOT NULL "+
                         "and parcelas.id_municipio= '"+ idMunicipio+"' "+conditions + " "+
                         "order by parcelas.referencia_catastral asc";
            */
			
			String sql = "select parcelas.referencia_catastral as ID, vias.tipovianormalizadocatastro, vias.nombrecatastro, " +
					"parcelas.primer_numero, parcelas.primera_letra, parcelas.bloque, bien_inmueble.escalera, bien_inmueble.planta, " +
					"bien_inmueble.puerta, parcelas.codigo_postal from parcelas " +
					"left join vias on parcelas.id_via=vias.codigocatastro and vias.id_municipio='" + idMunicipio + 
					"' left join bien_inmueble ON parcelas.referencia_catastral=bien_inmueble.parcela_catastral " +
					"where parcelas.referencia_catastral IS NOT NULL and parcelas.id_municipio='"+ idMunicipio+"' " + conditions + " "+
                         "order by parcelas.referencia_catastral asc";			
            
			
//			String sql1 = "select distinct parcelas.referencia_catastral as ID, vias.tipovianormalizadocatastro, vias.nombrecatastro, "+
//                         "parcelas.primer_numero, parcelas.primera_letra, parcelas.bloque, bien_inmueble.escalera, bien_inmueble.planta, bien_inmueble.puerta, parcelas.codigo_postal "+
//                         "from parcelas LEFT JOIN vias ON parcelas.id_via=vias.codigocatastro " +
//                         "LEFT JOIN bien_inmueble ON parcelas.referencia_catastral=bien_inmueble.parcela_catastral " +
//                         "where parcelas.referencia_catastral IS NOT NULL "+
//                         "and parcelas.id_municipio= '"+ idMunicipio+"' "+conditions + " "+
//                         "order by parcelas.referencia_catastral asc";

			logger.info("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector referenciasCatastrales = new Vector();
			while (rs.next()) {
				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(rs.getString("ID"),
						rs.getString("TIPOVIANORMALIZADOCATASTRO"),
						rs.getString("NOMBRECATASTRO"),
						rs.getString("PRIMER_NUMERO"),
						rs.getString("PRIMERA_LETRA"),
						rs.getString("BLOQUE"),
						rs.getString("ESCALERA"),
						rs.getString("PLANTA"),
						rs.getString("PUERTA"));
                referenciaCatastral.setCPostal(rs.getInt("CODIGO_POSTAL")!=0?new Integer(rs.getInt("CODIGO_POSTAL")).toString():"");
				referenciasCatastrales.add(referenciaCatastral);
			}
			safeClose(rs, statement, connection);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
			return referenciasCatastrales;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al obtener las referencias catastrales: " ,ex);
			return new Vector();
		}
	}


	public static CExpedienteLicencia searchExpedienteLicencia(String numExpediente)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
			String sql = "select * from expediente_licencia " +
                    "where NUM_EXPEDIENTE='" + numExpediente + "' order by FECHA_APERTURA DESC";
			logger.info("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
				      tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
				//ASO Elimina
                //Vector notificaciones = getNotificacionesExpediente(numExpediente);
				CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
						rs.getLong("ID_SOLICITUD"),
						tipoTramitacion,
						tipoFinalizacion,
						rs.getString("SERVICIO_ENCARGADO"),
						rs.getString("ASUNTO"),
						rs.getString("SILENCIO_ADMINISTRATIVO"),
						rs.getString("FORMA_INICIO"),
						rs.getInt("NUM_FOLIOS"),
						rs.getTimestamp("FECHA_APERTURA"),
						rs.getString("RESPONSABLE"),
						rs.getDate("PLAZO_RESOLUCION"),
						rs.getString("HABILES"),
						rs.getInt("NUM_DIAS"),
						rs.getString("OBSERVACIONES"),
						estado,
						null);
				safeClose(rs, statement, connection);
				return expediente;
			}
			safeClose(rs, statement, connection);
			return null;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Error al obtener la información del expediente "+ numExpediente+ ":" ,ex );
			return null;
		}
	}
	
	public static Double getTasaSolicitudLicencia(String idSolicitud, Connection connection){
    	//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
			
			String sql = "select TASA from solicitud_licencia " +
                    "where ID_SOLICITUD='" + idSolicitud + "'";
			
			logger.info("sql: " + sql);
			
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			
			if (rs.next()) {
				return new Double(rs.getDouble("TASA"));						
	        }
			safeClose(rs, statement, null);
			return null;

		} catch (Exception ex) {
			safeClose(rs, statement, null);
			logger.error("Error al obtener la información de la solicitud "+ idSolicitud + ":" ,ex );
			return null;
		}
		finally{
			safeClose(rs, statement, null);
		}
	}
	
	public static Double getImpuestoSolicitudLicencia(String idSolicitud,Connection connection){
    	//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}
			
			String sql = "select IMPUESTO from solicitud_licencia " +
                    "where ID_SOLICITUD='" + idSolicitud + "'";
			
			logger.info("sql: " + sql);
			
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			
			if (rs.next()) {
				return new Double(rs.getDouble("IMPUESTO"));						
	        }
			safeClose(rs, statement, null);
			return null;

		} catch (Exception ex) {
			safeClose(rs, statement, null);
			logger.error("Error al obtener la información de la solicitud "+ idSolicitud + ":" ,ex );
			return null;
		}
	}
	
    /*********************************************************************
     * Se recogen aquellos expedientes cuyo estado tenga un plazo de
     * expiración.
     * Parametros
     *    todos - Si es true se devuelven todos los expedientes si es
     * false solo aquellos en los cuales el campo plazo_event sea distinto
     * de null.
    */
	public static Vector getSearchedExpedientesExpirables(boolean todos, String idMunicipio, Vector tiposLicencia)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}
            //ASO AÑADE COGER SOLO AQUELLOS QUE SEAN DE OBRA MAYOR O MENOR
			String sql = "select expediente_licencia.*, solicitud_licencia.id_tipo_licencia from " +
                        "expediente_licencia , workflow, solicitud_licencia where " +
                        "expediente_licencia.id_estado <> "+CConstantesComando.ESTADO_DURMIENTE+" and "+
                        //"(solicitud_licencia.id_tipo_licencia='" +CConstantesComando.TIPO_OBRA_MAYOR +"' or " +
                        //"solicitud_licencia.id_tipo_licencia='" +CConstantesComando.TIPO_OBRA_MENOR +"') and " +
                        (dameTipos("solicitud_licencia.id_tipo_licencia", tiposLicencia)!=null?
                        dameTipos("solicitud_licencia.id_tipo_licencia", tiposLicencia)+ (idMunicipio == null ? " and " : " and id_municipio='" + idMunicipio + "' and "):
                        (idMunicipio == null ? "" : " id_municipio='" + idMunicipio + "' and "))+

                        "workflow.plazo is not null and " +
                        "expediente_licencia.id_solicitud=solicitud_licencia.id_solicitud and " +
                        // AÑADO
                        "workflow.id_estado=workflow.id_nextestado and " +
                    
                        "workflow.id_estado=expediente_licencia.id_estado and " +
                        "workflow.id_tipo_licencia=solicitud_licencia.id_tipo_licencia";
			if (!todos)
				sql = sql +" AND expediente_licencia.PLAZO_EVENT IS NULL";
			logger.info("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector expedientes = new Vector();
			while (rs.next()) {
				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
                       tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
                CExpedienteLicencia expedienteLicencia = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
						rs.getLong("ID_SOLICITUD"),
						tipoTramitacion,
						tipoFinalizacion,
						rs.getString("SERVICIO_ENCARGADO"),
						rs.getString("ASUNTO"),
						rs.getString("SILENCIO_ADMINISTRATIVO"),
						rs.getString("FORMA_INICIO"),
						rs.getInt("NUM_FOLIOS"),
						rs.getTimestamp("FECHA_APERTURA"),
						rs.getString("RESPONSABLE"),
						rs.getDate("PLAZO_RESOLUCION"),
						rs.getString("HABILES"),
						rs.getInt("NUM_DIAS"),
						rs.getString("OBSERVACIONES"),
						estado,
						null);
				expedienteLicencia.setVU(rs.getString("VU"));
				expedienteLicencia.setPlazoEvent(rs.getString("PLAZO_EVENT"));
				expedienteLicencia.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
				expedienteLicencia.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
				expedienteLicencia.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));
                expedienteLicencia.setTipoLicenciaDescripcion(rs.getString("id_tipo_licencia"));
                expedienteLicencia.setBloqueado(rs.getString("BLOQUEADO"));
				expedientes.add(expedienteLicencia);
			}
			safeClose(rs, statement, connection);
			logger.debug("expedientes: " + expedientes);
			return expedientes;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception, al obtener los expedientes expirables, Todos="+todos+" : " , ex);
			return new Vector();
		}
	}



    public static Vector getSearchedExpedientesSilencioAdmin(Hashtable hash, String idMunicipio,Vector tiposLicencia)
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            logger.debug("Inicio.");
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }
             String conditions = getConditions(hash);
            String sql = "select E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA, " +
                    "E.ID_TIPO_LICENCIA as E_ID_TIPO_LICENCIA, E.ID_TIPO_OBRA as E_ID_TIPO_OBRA, " +
                    "E.FECHA_LIMITE_OBRA, A.*,F.DNI_CIF AS NIFPROPIETARIO, E.ID_MUNICIPIO " +
                    "from expediente_licencia A, solicitud_licencia E, persona_juridico_fisica F, workflow " +
                    "where a.ID_SOLICITUD=e.ID_SOLICITUD and " +
                    "e.propietario=f.id_persona and " +
                    "e.id_solicitud=a.id_solicitud and " +
                    "workflow.plazo is null and " +
                    "workflow.id_estado=workflow.id_nextestado and " +
                    "workflow.id_estado=a.id_estado and " +
                    "workflow.id_tipo_licencia=e.id_tipo_licencia and " +
                    //ASO cambia el nombre de la aplicacion por el tipo de obra MAYOR O MENOR
                    //" A.APP_STRING='" + CConstantesComando.APP_LICENCIAS + "' " +
                    //" (E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MAYOR+ " OR \n"+
                    //"  E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MENOR+ ") "+
                    (dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        dameTipos("E.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and id_municipio='" + idMunicipio + "' "):
                        (idMunicipio == null ? "" : "and id_municipio='" + idMunicipio + "' "))
                       + conditions + " order by FECHA_APERTURA DESC";


            logger.info("sql: " + sql);


            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            Vector expedientes = new Vector();
            while (rs.next()) {
                CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
                CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
                       tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
                //Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));
                //ASO elimina
                logger.debug("despues de getNotificacionesExpediente");
                CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
                        rs.getLong("ID_SOLICITUD"),
                        tipoTramitacion,
                        tipoFinalizacion,
                        rs.getString("SERVICIO_ENCARGADO"),
                        rs.getString("ASUNTO"),
                        rs.getString("SILENCIO_ADMINISTRATIVO"),
                        rs.getString("FORMA_INICIO"),
                        rs.getInt("NUM_FOLIOS"),
                        rs.getTimestamp("FECHA_APERTURA"),
                        rs.getString("RESPONSABLE"),
                        rs.getDate("PLAZO_RESOLUCION"),
                        rs.getString("HABILES"),
                        rs.getInt("NUM_DIAS"),
                        rs.getString("OBSERVACIONES"),
                        estado,
                        null);
                expediente.setVU(rs.getString("VU"));
                expediente.setPlazoEvent(rs.getString("PLAZO_EVENT"));
                expediente.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
                expediente.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
                expediente.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));
                expediente.setTipoLicenciaDescripcion(new Integer(rs.getInt("E_ID_TIPO_LICENCIA")).toString());
                expediente.setPatronTipoObraSolicitud(new Integer(rs.getInt("E_ID_TIPO_OBRA")).toString());
                expediente.setNifPropietario(rs.getString("NIFPROPIETARIO"));
                expediente.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                expediente.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                expediente.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));
                expediente.setBloqueado(rs.getString("BLOQUEADO"));
                expediente.setTipoLicenciaDescripcion(rs.getString("E_ID_TIPO_LICENCIA"));
                ///
                ///solicitud del expediente:
                CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("E_ID_TIPO_LICENCIA"), "", "");
                CTipoObra tipoObra = new CTipoObra(rs.getInt("E_ID_TIPO_OBRA"), "", "");
                CSolicitudLicencia solicitudLicencia = new com.geopista.protocol.licencias.CSolicitudLicencia();
                solicitudLicencia.setTipoLicencia(tipoLicencia);
                solicitudLicencia.setTipoObra(tipoObra);
                solicitudLicencia.setFechaLimiteObra(rs.getDate("FECHA_LIMITE_OBRA"));
                solicitudLicencia.setIdMunicipio(rs.getString("ID_MUNICIPIO"));
                solicitudLicencia.setReferenciasCatastrales(getReferenciasCatastrales(expediente.getIdSolicitud(),connection));
                expediente.setSolicitud(solicitudLicencia);
                expedientes.add(expediente);
            }
            safeClose(rs, statement, connection);
            logger.debug("expedientes: " + expedientes);
            return expedientes;
        } catch (Exception ex) {
            safeClose(rs, statement, connection);
            logger.error("Exception al buscar expedientes: " ,ex);
            return new Vector();
        }
    }



	public static Vector getSearchedAddresses(Hashtable hash, String idMunicipio)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}
    		String conditions = getConditions(hash);
			/*  Adaptacion Geopista por cambios en tabla parcelas
            String sql = "select distinct c.referencia_catastral as ID, b.tipovianormalizadocatastro, b.nombrecatastro, "+
                     "c.primer_numero, c.primera_letra, c.bloque, c.escalera, c.planta, c.puerta, c.codigo_postal "+
					"from parcelas c " +
					"LEFT JOIN vias b ON c.id_via=b.codigocatastro" +
					" where c.referencia_catastral IS NOT NULL and c.id_municipio='" + idMunicipio + "' " + conditions + " " +
					"order by c.referencia_catastral asc";
            */
            String sql = "select distinct c.referencia_catastral as ID, b.tipovianormalizadocatastro, b.nombrecatastro, "+
                     "c.primer_numero, c.primera_letra, c.bloque, bi.escalera, bi.planta, bi.puerta, c.codigo_postal "+
					"from parcelas c " +
					"LEFT JOIN vias b ON c.id_via=b.codigocatastro and b.id_municipio='"  + idMunicipio + "' " +
                    "LEFT JOIN bien_inmueble bi ON c.referencia_catastral=bi.parcela_catastral" +
					" where c.referencia_catastral IS NOT NULL and c.id_municipio='" + idMunicipio + "' " + conditions + " " +
					"order by c.referencia_catastral asc";
			logger.info("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector referenciasCatastrales = new Vector();
			while (rs.next()) {
				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(rs.getString("ID"),
						rs.getString("TIPOVIANORMALIZADOCATASTRO"),
						rs.getString("NOMBRECATASTRO"),
						rs.getString("PRIMER_NUMERO"),
						rs.getString("PRIMERA_LETRA"),
						rs.getString("BLOQUE"),
						rs.getString("ESCALERA"),
						rs.getString("PLANTA"),
						rs.getString("PUERTA"));
                referenciaCatastral.setCPostal(rs.getInt("CODIGO_POSTAL")!=0?new Integer(rs.getInt("CODIGO_POSTAL")).toString():"");
				referenciasCatastrales.add(referenciaCatastral);
			}
			safeClose(rs, statement, connection);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
			return referenciasCatastrales;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al buscar referencias por direccion : " ,ex);
			return new Vector();
		}
	}

	public static Vector getSearchedAddressesByNumPolicia(Hashtable hash,String idMunicipio) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String conditions = getConditions(hash);
			String sql=""+
			"select a.id,b.tipovianormalizadocatastro,b.nombrecatastro,a.rotulo from numeros_policia a  " +
					"LEFT JOIN vias b  " +
					"ON a.id_via=b.id " +
					"where b.id_municipio="+idMunicipio+conditions+" ";



			logger.info("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector referenciasCatastrales = new Vector();

			while (rs.next()) {

				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(rs.getString("id"),
						rs.getString("tipovianormalizadocatastro"),
						rs.getString("nombrecatastro"),
						rs.getString("rotulo"),
						"",
						"",
						"",
						"",
						"");

				referenciasCatastrales.add(referenciaCatastral);

			}
			safeClose(rs, statement, connection);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());

			return referenciasCatastrales;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getSearchedPersonas(Hashtable hash, String id_municipio) {
        Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

	        String sql = "select A.*,B.* from PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B " +
                         "where  A.ID_PERSONA=B.ID_PERSONA and (A.id_municipio=" + id_municipio +" OR A.id_municipio=0) "+
                         getConditions(hash) +" order by A.APELLIDO1,A.ID_PERSONA,B.ID_SOLICITUD DESC";

			logger.info("SQL=" + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
            Vector personas = new Vector();
            String sOldId=null;
			while (rs.next())
            {
                if (sOldId==null || !sOldId.equals(rs.getString("ID_PERSONA")))
                {
                    CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), "");
                    logger.debug("viaNotificacion.getObservacion(): " + viaNotificacion.getObservacion());
                    CDatosNotificacion datosNotificacion = new CDatosNotificacion(rs.getString("DNI_CIF"),
                        viaNotificacion,
                        rs.getString("FAX"),
                        rs.getString("TELEFONO"),
                        rs.getString("MOVIL"),
                        rs.getString("EMAIL"),
                        rs.getString("TIPO_VIA"),
                        rs.getString("NOMBRE_VIA"),
                        rs.getString("NUMERO_VIA"),
                        rs.getString("PORTAL"),
                        rs.getString("PLANTA"),
                        rs.getString("ESCALERA"),
                        rs.getString("LETRA"),
                        rs.getString("CPOSTAL"),
                        rs.getString("MUNICIPIO"),
                        rs.getString("PROVINCIA"),
                        rs.getString("NOTIFICAR"));
                    CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), datosNotificacion);
                    persona.setIdPersona(rs.getLong("ID_PERSONA"));
                    personas.add(persona);
                }
                sOldId=rs.getString("ID_PERSONA");
            }
			safeClose(rs, statement, connection);
			return personas;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al obtener los datos: " + ex.toString());
			return new Vector();
		}
	}


	private static Vector getAnexosSolicitud(long idSolicitud,Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			//String sql = "select a.*,b.descripcion AS TIPO_ANEXO_DESCRIPCION,b.observacion AS TIPO_ANEXO_OBSERVACION from anexo a,tipo_anexo b where id_solicitud=" + idSolicitud + " and a.ID_TIPO_ANEXO=b.ID_TIPO_ANEXO";
            String sql = "select * from anexo where id_solicitud="+ idSolicitud + " and comes_from is null";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector anexos = new Vector();
			while (rs.next()) {

				//CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), rs.getString("TIPO_ANEXO_DESCRIPCION"), rs.getString("TIPO_ANEXO_OBSERVACION"));
                CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), "", "");
				CAnexo anexo = new CAnexo(tipoAnexo, rs.getString("URL_FICHERO"), rs.getString("OBSERVACION"));
				anexo.setIdAnexo(rs.getLong("ID_ANEXO"));

				anexos.add(anexo);

			}

			safeClose(rs, statement,null);
			logger.info("anexos: " + anexos);
			return anexos;

		} catch (Exception ex) {
			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


    /****************
     * Documentacion Obligatoria entregada
     */
    private static Vector getDocumentacionEntregada(long idSolicitud) {

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            logger.debug("Inicio.");


            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }


            String sql = "select PATRON_DOMINIO from DOCUMENTACION_SOLICITUD " +
                         "where ID_SOLICITUD=" + idSolicitud;

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            Vector v = new Vector();
            while (rs.next()) {
                v.add(rs.getString("PATRON_DOMINIO"));
            }

            safeClose(rs, statement, connection);
            return v;

        } catch (Exception ex) {
            safeClose(rs, statement, connection);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();

        }

    }


	private static Vector getNotificacionesExpediente(String numExpediente,Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}

			//String sql = "SELECT A.*,B.DESCRIPCION,B.OBSERVACION FROM NOTIFICACION A,ESTADO_NOTIFICACION B where A.ID_ESTADO=B.ID_ESTADO AND A.NUM_EXPEDIENTE='" + numExpediente + "'";
            /*
			String sql = "SELECT A.*,B.DESCRIPCION,B.OBSERVACION, " +
					"C.ID_TIPO_NOTIFICACION AS C_ID_TIPO_NOTIFICACION, C.DESCRIPCION AS C_DESCRIPCION, " +
					"C.OBSERVACION AS C_OBSERVACION " +
					"FROM NOTIFICACION A,ESTADO_NOTIFICACION B, TIPO_NOTIFICACION C " +
					"where A.ID_ESTADO=B.ID_ESTADO AND A.ID_TIPO_NOTIFICACION=C.ID_TIPO_NOTIFICACION AND A.NUM_EXPEDIENTE='" + numExpediente + "' " +
					"ORDER BY A.PLAZO_VENCIMIENTO ASC";
           */
            String sql = "SELECT A.* " +
                    "FROM NOTIFICACION A, PERSONA_JURIDICO_FISICA J " +
                    "where A.ID_PERSONA=J.ID_PERSONA and " +
                    "A.NUM_EXPEDIENTE='" + numExpediente + "' " +
                    "ORDER BY A.PLAZO_VENCIMIENTO ASC";

			logger.info("SQL: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector notificaciones = new Vector();
			while (rs.next()) {

				CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(rs.getInt("ID_ESTADO"), "", "");
				CTipoNotificacion tipoNotificacion = new CTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"), "", "");
				CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"),connection);

				CNotificacion notificacion = new CNotificacion(rs.getLong("ID_NOTIFICACION"),
						tipoNotificacion,
						rs.getLong("ID_ALEGACION"),
						rs.getLong("ID_MEJORA"),
						persona,
						rs.getInt("NOTIFICADA_POR"),
						rs.getTimestamp("FECHA_CREACION"),
						rs.getTimestamp("FECHA_NOTIFICACION"),
						rs.getString("RESPONSABLE"),
						rs.getTimestamp("PLAZO_VENCIMIENTO"),
						rs.getString("HABILES"),
						rs.getInt("NUM_DIAS"),
						rs.getString("OBSERVACIONES"),
						estadoNotificacion,
						rs.getTimestamp("FECHA_REENVIO"),
						rs.getString("PROCEDENCIA"));

                notificacion.setEntregadaA(rs.getString("ENTREGADA_A"));

				notificaciones.add(notificacion);

			}

			safeClose(rs, statement, null);

			logger.debug("notificaciones: " + notificaciones);

			return notificaciones;

		} catch (Exception ex) {

			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	private static Vector getReferenciasCatastrales(long idSolicitud, Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "select * from parcelario where id_solicitud='" + idSolicitud+"'";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector referenciasCatastrales = new Vector();
			while (rs.next()) {

				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(rs.getString("ref_catastral"),
						rs.getString("tipo_via"),
						rs.getString("nombre_via"),
						rs.getString("numero"),
						rs.getString("letra"),
						rs.getString("bloque"),
						rs.getString("escalera"),
						rs.getString("planta"),
						rs.getString("puerta"));
                referenciaCatastral.setCPostal(rs.getString("codigo_postal"));


				referenciasCatastrales.add(referenciaCatastral);

			}

			safeClose(rs, statement, null);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
			return referenciasCatastrales;

		} catch (Exception ex) {
			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}

    /**
     * Función que recoge todos los datos del expediente
     *
     * @param numExpediente
     * @param idMunicipio
     * @return
     */
	public static CResultadoOperacion getExpedienteLicencia(String numExpediente, String idMunicipio, String locale, Vector tiposLicencia)
    {
    	Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
    	try {
	        if (numExpediente == null){
                logger.warn("numExpediente is null");
                return new CResultadoOperacion(false, "numExpediente is null");
            }
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
    		}
    		//*******************************************************
			//** Se obtiene idSolicitud para ese expediente
			//********************************************
			String sql = "select A.* from expediente_licencia A, solicitud_licencia S " +
                    "where num_expediente='" + numExpediente + "' and " +
                    "S.id_solicitud=A.id_solicitud and ";
                    if (idMunicipio != null){
                        sql+= "S.id_municipio=" + idMunicipio + " and ";
                    }
                    sql+="A.app_string='" + CConstantesComando.APP_LICENCIAS +"'";
            if (tiposLicencia != null){
                for (int i=0; i < tiposLicencia.size(); i++){
                    CTipoLicencia tipoLicencia= (CTipoLicencia)tiposLicencia.get(i);
                    if (tiposLicencia.size() > 1){
                        if (i == 0){
                            sql+= " and (";
                        }else{
                            sql+= " or ";
                        }
                    }else{
                        sql+= " and ";
                    }

                    sql+= "S.id_tipo_licencia=" + tipoLicencia.getIdTipolicencia();
                }
                if (tiposLicencia.size() > 1){
                    sql+= ")";
                }
            }

            logger.info("SQL="+sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				safeClose(rs, statement, connection);
				logger.info("Expediente no encontrado. numExpediente: " + numExpediente);
				return new CResultadoOperacion(false, "Expediente no encontrado.");
			}
			CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
			CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
            CTipoFinalizacion tipoFinalizacion=null;
            if (rs.getString("ID_FINALIZACION")!=null)
                   tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
			/** Notificaciones */
			Vector notificaciones = getNotificacionesExpediente(numExpediente,connection);
			/** Eventos del expediente */
			Vector eventos = getEventosExpediente(numExpediente, locale,connection);
			/** Historico */
			Vector historico = getHistoricoExpediente(numExpediente, locale,connection);
			CExpedienteLicencia expedienteLicencia = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
					rs.getLong("ID_SOLICITUD"),
					tipoTramitacion,
					tipoFinalizacion,
					rs.getString("SERVICIO_ENCARGADO"),
					rs.getString("ASUNTO"),
					rs.getString("SILENCIO_ADMINISTRATIVO"),
					rs.getString("FORMA_INICIO"),
					rs.getInt("NUM_FOLIOS"),
					rs.getDate("FECHA_APERTURA"),
					rs.getString("RESPONSABLE"),
					rs.getDate("PLAZO_RESOLUCION"),
					rs.getString("HABILES"),
					rs.getInt("NUM_DIAS"),
					rs.getString("OBSERVACIONES"),
					estado,
					notificaciones);
			expedienteLicencia.setVU(rs.getString("VU"));
			logger.info("expedienteLicencia.getVU(): " + expedienteLicencia.getVU());
			expedienteLicencia.setPlazoEvent(rs.getString("PLAZO_EVENT"));
			expedienteLicencia.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
			expedienteLicencia.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
			expedienteLicencia.setCNAE(rs.getString("CNAE"));
			expedienteLicencia.setEventos(eventos);
			expedienteLicencia.setHistorico(historico);
            expedienteLicencia.setBloqueado(rs.getString("BLOQUEADO"));
            /** Recogemos la Alegacion del expediente, si la hay */
            Alegacion alegacion= getAlegacion(connection, expedienteLicencia.getNumExpediente());
            expedienteLicencia.setAlegacion(alegacion);
			long idSolicitud = expedienteLicencia.getIdSolicitud();
			Vector vExpediente = new Vector();
			vExpediente.add(expedienteLicencia);
			logger.info("idSolicitud: " + idSolicitud);
			String idMunicipioCondition = "";
			if (idMunicipio != null) {
				idMunicipioCondition = " and A.ID_MUNICIPIO=" + idMunicipio;
			}
            sql = "SELECT A.* FROM SOLICITUD_LICENCIA A WHERE ID_SOLICITUD='" + idSolicitud + "'" + idMunicipioCondition;
            logger.info("SQL="+sql);
            safeClose(null, statement, null);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				logger.info("Solicitud no encontrada. idSolicitud: " + idSolicitud);
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Solicitud no encontrada.");
			}
			CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs.getLong("PROPIETARIO"), idSolicitud,connection);
			CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"), idSolicitud,connection);
			//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"), idSolicitud);
            Vector vTecnicos= getTecnicosFromDatabase(idSolicitud,connection);
			CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"), idSolicitud,connection);
			//CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), rs.getString("TIPO_LICENCIA_DESCRIPCION"), rs.getString("TIPO_LICENCIA_OBSERVACION"));
            CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), "", "");
			CTipoObra tipoObra = new CTipoObra(rs.getInt("ID_TIPO_OBRA"), "", "");
            Vector anexos= getAnexosSolicitud(idSolicitud,connection);
			Vector referenciasCatastales = getReferenciasCatastrales(idSolicitud,connection);
			CSolicitudLicencia solicitudLicencia = new com.geopista.protocol.licencias.CSolicitudLicencia(tipoLicencia,
					tipoObra,
					propietario,
					representante,
					null,
					promotor,
					rs.getString("NUM_ADMINISTRATIVO"),
					rs.getString("CODIGO_ENTRADA"),
					rs.getString("UNIDAD_TRAMITADORA"),
					rs.getString("UNIDAD_DE_REGISTRO"),
					rs.getString("MOTIVO"),
					rs.getString("ASUNTO"),
					rs.getTimestamp("FECHA"),
					rs.getDouble("TASA"),
					rs.getString("TIPO_VIA_AFECTA"),
					rs.getString("NOMBRE_VIA_AFECTA"),
					rs.getString("NUMERO_VIA_AFECTA"),
					rs.getString("PORTAL_AFECTA"),
					rs.getString("PLANTA_AFECTA"),
					rs.getString("LETRA_AFECTA"),
					rs.getString("CPOSTAL_AFECTA"),
					rs.getString("MUNICIPIO_AFECTA"),
					rs.getString("PROVINCIA_AFECTA"),
					rs.getString("OBSERVACIONES"),
					anexos,
					referenciasCatastales);
			solicitudLicencia.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
			solicitudLicencia.setNombreComercial(rs.getString("NOMBRE_COMERCIAL"));
            solicitudLicencia.setObservacionesDocumentacionEntregada(rs.getString("OBSERVACIONES_DOC_ENTREGADA"));
            solicitudLicencia.setDocumentacionEntregada(getDocumentacionEntregada(idSolicitud));
            solicitudLicencia.setTecnicos(vTecnicos);
            solicitudLicencia.setImpuesto(rs.getDouble("IMPUESTO"));
            solicitudLicencia.setFechaResolucion(rs.getTimestamp("FECHA_RESOLUCION"));
            solicitudLicencia.setFechaLimiteObra(rs.getTimestamp("FECHA_LIMITE_OBRA"));

            /* Recogemos las mejoras de la solicitud */
            solicitudLicencia.setMejoras(getMejorasSolicitud(connection, solicitudLicencia.getIdSolicitud()));
            /* Recogemos los informes del expediente*/
            expedienteLicencia.setInformes(getInformesExpediente(connection, expedienteLicencia.getNumExpediente()));
            /*Recogemos la resolucion*/
            expedienteLicencia.setResolucion(getResolucion(connection, expedienteLicencia.getNumExpediente()));
            /** datos de actividad */
            solicitudLicencia.setDatosActividad(getDatosActividad(connection, idSolicitud));

			Vector vSolicitud = new Vector();
			vSolicitud.add(solicitudLicencia);
			safeClose(rs, statement, connection);
			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitud);
			resultadoOperacion.setExpedientes(vExpediente);
			return resultadoOperacion;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al recoger el expediente : "+numExpediente,ex);
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}
	}


	public static Vector getViasNotificacion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM VIA_NOTIFICACION ORDER BY ID_VIA_NOTIFICACION ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector viasNotificacion = new Vector();
			while (rs.next()) {

				CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), rs.getString("OBSERVACION"));
				viasNotificacion.add(viaNotificacion);

			}

			safeClose(rs, statement, connection);

			return viasNotificacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getTiposFinalizacion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM TIPO_FINALIZACION ORDER BY ID_FINALIZACION ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposFinalizacion = new Vector();
			while (rs.next()) {

				CTipoFinalizacion tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				tiposFinalizacion.add(tipoFinalizacion);

			}

			safeClose(rs, statement, connection);

			return tiposFinalizacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getTiposNotificacion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM TIPO_NOTIFICACION ORDER BY ID_TIPO_NOTIFICACION ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposNotificacion = new Vector();
			while (rs.next()) {

				CTipoNotificacion tipoNotificacion = new CTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				tiposNotificacion.add(tipoNotificacion);

			}

			safeClose(rs, statement, connection);

			return tiposNotificacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getEstadosNotificacion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM ESTADO_NOTIFICACION ORDER BY ID_ESTADO ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector estadosNotificacion = new Vector();
			while (rs.next()) {

				CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				estadosNotificacion.add(estadoNotificacion);

			}

			safeClose(rs, statement, connection);

			return estadosNotificacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getEstadosResolucion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM ESTADO_RESOLUCION ORDER BY ID_ESTADO ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector estadosResolucion = new Vector();
			while (rs.next()) {

				CEstadoResolucion estadoResolucion = new CEstadoResolucion(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				estadosResolucion.add(estadoResolucion);

			}

			safeClose(rs, statement, connection);

			return estadosResolucion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getNotificaciones(Hashtable hash) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}

			String numExpediente = (String) hash.get("NUM_EXPEDIENTE");
			logger.info("numExpediente: " + numExpediente);

			if ((numExpediente == null) || (numExpediente.trim().equals(""))) {
				numExpediente = "";
			}

			//String sql = "SELECT A.*,B.DESCRIPCION,B.OBSERVACION FROM NOTIFICACION A,ESTADO_NOTIFICACION B where a.ID_ESTADO=b.ID_ESTADO" + conditions;
			String sql = "SELECT A.*,B.DESCRIPCION,B.OBSERVACION, " +
					"C.ID_TIPO_NOTIFICACION AS C_ID_TIPO_NOTIFICACION, C.DESCRIPCION AS C_DESCRIPCION, " +
					"C.OBSERVACION AS C_OBSERVACION " +
					"FROM NOTIFICACION A,ESTADO_NOTIFICACION B, TIPO_NOTIFICACION C " +
					"where A.ID_ESTADO=B.ID_ESTADO AND A.ID_TIPO_NOTIFICACION=C.ID_TIPO_NOTIFICACION " +
					"AND A.NUM_EXPEDIENTE='" + numExpediente + "'";
			logger.debug("sql: " + sql);

			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector notificaciones = new Vector();
			while (rs.next()) {

				CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				CTipoNotificacion tipoNotificacion = new CTipoNotificacion(rs.getInt("C_ID_TIPO_NOTIFICACION"), rs.getString("C_DESCRIPCION"), rs.getString("C_OBSERVACION"));
				CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"),connection);

				CNotificacion notificacion = new CNotificacion(rs.getLong("ID_NOTIFICACION"),
						tipoNotificacion,
						rs.getLong("ID_ALEGACION"),
						rs.getLong("ID_MEJORA"),
						persona,
						rs.getInt("NOTIFICADA_POR"),
						rs.getDate("FECHA_CREACION"),
						rs.getDate("FECHA_NOTIFICACION"),
						rs.getString("RESPONSABLE"),
						rs.getDate("PLAZO_VENCIMIENTO"),
						rs.getString("HABILES"),
						rs.getInt("NUM_DIAS"),
						rs.getString("OBSERVACIONES"),
						estadoNotificacion,
						rs.getDate("FECHA_REENVIO"),
						rs.getString("PROCEDENCIA"));

                notificacion.setEntregadaA(rs.getString("ENTREGADA_A"));

				notificaciones.add(notificacion);

			}

			safeClose(rs, statement, connection);
			logger.info("notificaciones: " + notificaciones);

			return notificaciones;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getParcelario(Hashtable hash, String idMunicipio) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String conditions = getConditions(hash);

			String sql = "SELECT A.* FROM PARCELARIO A,SOLICITUD_LICENCIA B WHERE A.REF_CATASTRAL IS NOT NULL AND A.ID_SOLICITUD=B.ID_SOLICITUD and b.id_municipio='" + idMunicipio + "' " + conditions;
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector referenciasCatastrales = new Vector();
			while (rs.next()) {

				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral();
				referenciaCatastral.setReferenciaCatastral(rs.getString("REF_CATASTRAL"));

				referenciasCatastrales.add(referenciaCatastral);

			}

			safeClose(rs, statement, connection);
			logger.debug("referenciasCatastrales: " + referenciasCatastrales);

			return referenciasCatastrales;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getTiposTramitacion() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}


			String sql = "SELECT * FROM TIPO_TRAMITACION ORDER BY ID_TRAMITACION ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposTramitacion = new Vector();
			while (rs.next()) {

				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"), rs.getString("PLAZO_ENTREGA"));
				tiposTramitacion.add(tipoTramitacion);

			}

			safeClose(rs, statement, connection);

			return tiposTramitacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getTiposObra() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}


			String sql = "SELECT * FROM TIPO_OBRA ORDER BY ID_TIPO_OBRA ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposObra = new Vector();
			while (rs.next()) {

				CTipoObra tipoObra = new CTipoObra(rs.getInt("ID_TIPO_OBRA"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				tiposObra.add(tipoObra);

			}

			safeClose(rs, statement, connection);


			return tiposObra;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getTiposAnexo() {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");
            //****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}


			String sql = "SELECT * FROM TIPO_ANEXO ORDER BY ID_TIPO_ANEXO ASC";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector tiposAnexo = new Vector();
			while (rs.next()) {

				CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"));
				tiposAnexo.add(tipoAnexo);

			}

			safeClose(rs, statement, connection);


			return tiposAnexo;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);


			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getEstadosDisponibles(CExpedienteLicencia expedienteLicencia, int idTipoLicencia)
    {
        Connection connection=null;
		Statement statement = null;
		ResultSet rs = null;
		try {
            connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

            /** Este es el caso de la creacion, donde aun no existe expediente. */
			int id_estado = 0;
			if (expedienteLicencia != null) {
				id_estado = expedienteLicencia.getEstado().getIdEstado();
				logger.info("expedienteLicencia.getSilencioTriggered(): " + expedienteLicencia.getSilencioTriggered());
			}

            String sql="";
			/** Caso en que sea durmiente por silencio administrativo */
            /* NOTA: no entiendo que el comportamiento sea distinto si el expediente ha pasado a estado
            DURMIENTE por el sistema que si lo ha hecho por el usuario */
            /*
			if ((id_estado == CConstantesComando.ESTADO_DURMIENTE) && (expedienteLicencia != null) && (expedienteLicencia.getSilencioTriggered() != null) && (expedienteLicencia.getSilencioTriggered().equals("1"))) {
				sql = "select * from estado where id_estado=" + CConstantesComando.ESTADO_DURMIENTE;

			} else if (id_estado == CConstantesComando.ESTADO_DURMIENTE) {
                sql = "select * from estado";

			} else {
				sql = "select b.* from workflow a,estado b where " +
                      "a.id_tipo_licencia= "+ idTipoLicencia+" and "+
                      "a.id_nextestado=b.id_estado and a.id_estado=" + id_estado;
			}*/
            if (id_estado == CConstantesComando.ESTADO_DURMIENTE){
                sql = "select * from estado where id_estado=" + CConstantesComando.ESTADO_DURMIENTE;

            } else {
                sql = "select b.* from workflow a,estado b where " +
                      "a.id_tipo_licencia= "+ idTipoLicencia+" and "+
                      "a.id_nextestado=b.id_estado and a.id_estado=" + id_estado;
            }

            logger.info(".....SQL="+sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector estados = new Vector();
			while (rs.next()) {
				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"), rs.getInt("STEP"));
				estados.add(estado);
			}

            /** Annadimos los permisos necesarios para ejecutar cada uno de los estados disponibles */
            estados= getEstadosConPermisosNecesarios(connection, estados, idTipoLicencia);

			safeClose(rs, statement, connection);
			return estados;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception: " ,ex);
			return new Vector();
		}
	}

    public static Vector getEstadosConPermisosNecesarios(Connection conn, Vector estados, int idTipoLicencia){
        Vector estadosPermisos= new Vector();

		Statement statement = null;
		ResultSet rs = null;
		try {
            if (estados != null){
                for (int i=0; i<estados.size(); i++){
                    CEstado estado= (CEstado)estados.get(i);
                    /*
                    String sql = "select w.idperm, usrgrouperm.def from workflow w LEFT JOIN usrgrouperm ON w.idperm=usrgrouperm.idperm where " +
                            "w.id_tipo_licencia= " + idTipoLicencia + " and "+
                            "w.id_nextestado=w.id_estado and w.id_estado=" + estado.getIdEstado();
                            */
                    String sql = "select w.idperm, usrgrouperm.def from workflow w, usrgrouperm " +
                            "where w.idperm=usrgrouperm.idperm and " +
                            "w.id_tipo_licencia= " + idTipoLicencia + " and "+
                            "w.id_nextestado=w.id_estado and w.id_estado=" + estado.getIdEstado();

                    logger.info("SQL="+sql);
                    statement = conn.createStatement();
                    rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        estado.setIdPermiso(rs.getLong("IDPERM"));
                        estado.setDescPermiso(rs.getString("DEF"));
                    }
                    estadosPermisos.add(estado);
                    safeClose(rs, statement, null);
                }

                safeClose(rs, statement, null);
            }

			return estadosPermisos;

		} catch (Exception ex) {
			safeClose(rs, statement, null);
			logger.error("Exception: " ,ex);
			return new Vector();
		}
    }


	public static Vector getEstados(Vector tiposLicencia) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

            /** Todos los estados de las licencias de obra y actividad estan en el mismo
             * dominio (ESTADOS), por lo que tenemos que sacar los estados de cada tipo de
             * licencia de la tabla de workflow.
             */
            String sql= "select distinct a.id_estado from workflow a where " +
                    "a.id_nextestado=a.id_estado " +

                    (dameTipos("a.id_tipo_licencia", tiposLicencia)!=null?
                       " AND "+ dameTipos("a.id_tipo_licencia", tiposLicencia):"");

            logger.info("sql="+sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector estados = new Vector();
			while (rs.next()) {
				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));
				estados.add(estado);
			}

			/*rs.close();
			statement.close();
			connection.close();*/
			connection.commit();
			safeClose(rs, statement, connection);


			return estados;

		} catch (Exception ex) {
			/*try {
				rs.close();
			} catch (Exception ex2) {
			}
			try {
				statement.close();
			} catch (Exception ex2) {
			}
			try {
				connection.close();
			} catch (Exception ex2) {
			}*/
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static boolean updateEstadoExpediente(int idEstado, String numExpediente,
                                                 boolean silencio, Integer idFinalizacion) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}


			if (silencio) {
                if (idFinalizacion==null)
				    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=?,SILENCIO_TRIGGERED='1',PLAZO_EVENT=null, SILENCIO_EVENT=null WHERE NUM_EXPEDIENTE=?");
                else
                    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=?,SILENCIO_TRIGGERED='1',PLAZO_EVENT=null, SILENCIO_EVENT=null, ID_FINALIZACION=? WHERE NUM_EXPEDIENTE=?");

			} else {
                if (idFinalizacion==null)
    				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=?, PLAZO_EVENT=null, SILENCIO_EVENT=null WHERE NUM_EXPEDIENTE=?");
                else
                    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=?, PLAZO_EVENT=null, SILENCIO_EVENT=null, ID_FINALIZACION=? WHERE NUM_EXPEDIENTE=?");
    		}

			preparedStatement.setInt(1, idEstado);
			preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            if (idFinalizacion==null)
                preparedStatement.setString(3, numExpediente);
            else
            {
                preparedStatement.setInt(3, idFinalizacion.intValue());
                preparedStatement.setString(4, numExpediente);
            }
			preparedStatement.execute();
            connection.commit();

			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}


	public static boolean activateEventExpediente(String numExpediente, boolean silencioEvent) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			if (silencioEvent) {
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET SILENCIO_EVENT=? WHERE NUM_EXPEDIENTE=?");
			} else {
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET PLAZO_EVENT=? WHERE NUM_EXPEDIENTE=?");
			}

			preparedStatement.setString(1, "1");
			preparedStatement.setString(2, numExpediente);
			preparedStatement.executeUpdate();
            connection.commit();

			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}


	public static boolean insertHistoricoExpediente(CExpedienteLicencia expedienteLicencia, String user, boolean cambioEstado, Connection connection) {

//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		Statement statement = null;
//		ResultSet rs = null;
//
//		try {
//
//			logger.debug("Inicio.");
//
//			//****************************************
//			//** Obtener una conexion de la base de datos
//			//****************************************************
//			connection = CPoolDatabase.getConnection();
//			if (connection == null) {
//				logger.warn("Cannot get connection");
//				return false;
//			}
//
//			long secuencia = getTableSequence();
//
//			logger.debug("expedienteLicencia.getIdSolicitud(): " + expedienteLicencia.getIdSolicitud());
//			logger.debug("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());
//
//			preparedStatement = connection.prepareStatement("INSERT INTO HISTORICO(ID_HISTORICO,ID_SOLICITUD,NUM_EXPEDIENTE,ID_ESTADO,FECHA,USUARIO,APUNTE,SISTEMA) VALUES (?,?,?,?,?,?,?,?)");
//
//			preparedStatement.setLong(1, secuencia);
//			preparedStatement.setLong(2, expedienteLicencia.getIdSolicitud());
//			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
//			preparedStatement.setInt(4, expedienteLicencia.getEstado().getIdEstado());
//			preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
//			preparedStatement.setString(6, user);
//            /*
//			String aux = "";
//			try {
//				aux = CConstantesWorkflow.literalesPermisosUsuarioShort[expedienteLicencia.getEstado().getIdEstado()];
//			} catch (Exception ex) {
//
//			}
//
//			preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_GENERICO + aux);
//            */
//            if (cambioEstado)
//                preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_CAMBIO_ESTADO);
//            else preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_GENERICO);
//
//            preparedStatement.setString(8, "1");
//            preparedStatement.execute();
//
//            connection.commit();
//			safeClose(rs, statement, preparedStatement, connection);
//			return true;
//
//		} catch (Exception ex) {
//
//			safeClose(rs, statement, preparedStatement, connection);
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			ex.printStackTrace(pw);
//			logger.error("Exception: " + sw.toString());
//			return false;
//
//		}
		
		String tipoEntrada;
		if (cambioEstado){
			tipoEntrada = CWorkflowLiterales.HISTORICO_CAMBIO_ESTADO;
		}
		else {
			tipoEntrada = CWorkflowLiterales.HISTORICO_GENERICO;
		}
		
		return insertHistoricoExpediente(expedienteLicencia, user, tipoEntrada, null,connection);
	}
	
	public static boolean insertHistoricoExpediente(CExpedienteLicencia expedienteLicencia,
			String user, String tipoEntrada, List parametrosEntrada, Connection connection) {

		//Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			long secuencia = getTableSequence();

			logger.debug("expedienteLicencia.getIdSolicitud(): " + expedienteLicencia.getIdSolicitud());
			logger.debug("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());

			preparedStatement = connection.prepareStatement("INSERT INTO HISTORICO(ID_HISTORICO,ID_SOLICITUD,NUM_EXPEDIENTE,ID_ESTADO,FECHA,USUARIO,APUNTE,SISTEMA) VALUES (?,?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, expedienteLicencia.getIdSolicitud());
			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
			preparedStatement.setInt(4, expedienteLicencia.getEstado().getIdEstado());
			preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
			preparedStatement.setString(6, user);

			String apunte;
			if (tipoEntrada.equals(CWorkflowLiterales.HISTORICO_CAMBIO_TASA)){
				apunte = CWorkflowLiterales.HISTORICO_CAMBIO_TASA;
				preparedStatement.setString(8, "2");				
			}
			else if (tipoEntrada.equals(CWorkflowLiterales.HISTORICO_CAMBIO_IMPUESTO)){			
				apunte = CWorkflowLiterales.HISTORICO_CAMBIO_IMPUESTO;
				preparedStatement.setString(8, "2");				
			}
			else if (tipoEntrada.equals(CWorkflowLiterales.HISTORICO_CAMBIO_ESTADO)){			
				apunte = CWorkflowLiterales.HISTORICO_CAMBIO_ESTADO;
				preparedStatement.setString(8, "1");				
			}
            else {
            	apunte = CWorkflowLiterales.HISTORICO_GENERICO;            	
            	preparedStatement.setString(8, "1");
            }
			
			if (parametrosEntrada != null){
				for (int i = 0; i < parametrosEntrada.size(); i++){
					apunte += "{" + parametrosEntrada.get(i) + "}";
				}			
			}
			preparedStatement.setString(7, apunte);
			
			preparedStatement.execute();
            connection.commit();
			safeClose(rs, statement, preparedStatement, null);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, null);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}


	public static boolean insertNotificacionExpediente(CExpedienteLicencia expedienteLicencia, int idTipoLicencia, long idPersona, String dnicif, int tipoNotificacion, CSolicitudLicencia solicitud, boolean esTitular)
    {
    	Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;
    	try {
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}
    		long secuencia = getTableSequence();
    		preparedStatement = connection.prepareStatement("INSERT INTO NOTIFICACION(ID_NOTIFICACION,ID_TIPO_NOTIFICACION,NUM_EXPEDIENTE,ID_PERSONA,ID_SOLICITUD,FECHA_CREACION,PLAZO_VENCIMIENTO,ID_ESTADO,PROCEDENCIA) VALUES (?,?,?,?,?,?,?,?,?)");
			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, tipoNotificacion);
			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
			preparedStatement.setLong(4, idPersona);
			preparedStatement.setLong(5, expedienteLicencia.getIdSolicitud());
			preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedStatement.setTimestamp(7, new Timestamp(new Date().getTime() + (1 * 24 * 60 * 60 * 1000)));
			preparedStatement.setLong(8, 0);
			preparedStatement.setNull(9, java.sql.Types.VARCHAR);
    		logger.debug("secuencia: " + secuencia);
			logger.debug("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());
			logger.debug("idPersona: " + idPersona);
			logger.debug("expedienteLicencia.getIdSolicitud(): " + expedienteLicencia.getIdSolicitud());
			logger.debug("expedienteLicencia.getEstado().getIdEstado(): " + expedienteLicencia.getEstado().getIdEstado());
    		preparedStatement.execute();
     		//*******************************************
			//** Ventanilla unica
			//*******************************************
			logger.info("******EXPEDIENTE.VU=" + expedienteLicencia.getVU());
			if ((expedienteLicencia.getVU().equals("1")) && (esTitular)) {
				logger.info("********* Insertamos NOTIFICACION TELEMATICA");
                /** NOTA: Ventanilla Unica, no es multilenguaje. */
                String cproc= "GeoPISTA";
                if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MAYOR){
                    cproc+= ". Licencia de Obra Mayor.";
                }else if (idTipoLicencia == CConstantesComando.TIPO_OBRA_MENOR){
                    cproc+= ". Licencia de Obra Menor.";
                }else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD){
                    cproc+= ". Licencia de Actividad Calificada.";
                }else if (idTipoLicencia == CConstantesComando.TIPO_ACTIVIDAD_NO_CALIFICADA){
                    cproc+= ". Licencia de Actividad No Calificada.";
                }
				CWorkflowLine workflowLine = COperacionesDatabase.getWorkflowLine(connection,expedienteLicencia.getEstado().getIdEstado(), idTipoLicencia);
				String notif_text = workflowLine.getNotifText();
                String contenido= "";
                if (tipoNotificacion == CConstantesComando.notificacionMejoraDatos){
                    contenido= "<p>El expediente número <b>"+expedienteLicencia.getNumExpediente() + "</b>, presentado por</p><p><b>D./Dña. " + solicitud.getPropietario().getNombre() + "</b> con D.N.I. "+ solicitud.getPropietario().getDniCif() + ",</p><p>ha pasado a estado de <b>Mejora de Datos</b>.</p><p><b>Motivos:</b></p><p>" + solicitud.getObservacionesDocumentacionEntregada() + "</p>";
                }else if (tipoNotificacion == CConstantesComando.notificacionEsperaAlegacion){
                    contenido= "<p>El expediente número <b>"+expedienteLicencia.getNumExpediente() + "</b>, presentado por</p><p><b>D./Dña. " + solicitud.getPropietario().getNombre() + "</b> con D.N.I. "+ solicitud.getPropietario().getDniCif() + ",</p><p>ha pasado a estado de <b>Espera de Alegaciones</b>.</p><p><b>Observaciones:</b></p><p>" + solicitud.getObservacionesDocumentacionEntregada() + "</p>";
                }else if (tipoNotificacion == CConstantesComando.notificacionResolucionDenegacion){
                    contenido= "<p>El expediente número <b>"+expedienteLicencia.getNumExpediente() + "</b>, presentado por</p><p><b>D./Dña. " + solicitud.getPropietario().getNombre() + "</b> con D.N.I. "+ solicitud.getPropietario().getDniCif() + ",</p><p>ha sido <b>Denegada</b>.</p><p><b>Observaciones:</b></p><p>" + expedienteLicencia.getResolucion().getAsunto() + "</p>";
                }else if (tipoNotificacion == CConstantesComando.notificacionResolucionAprobacion){
                    if (expedienteLicencia.getEstado().getIdEstado() == CConstantesComando.ESTADO_EJECUCION){
                        contenido= "<p>El expediente número <b>"+expedienteLicencia.getNumExpediente() + "</b>, presentado por</p><p><b>D./Dña. " + solicitud.getPropietario().getNombre() + "</b> con D.N.I. "+ solicitud.getPropietario().getDniCif() + ",</p><p>ha sido <b>Aprobada</b> y se encuentra en estado de <b>Ejecución</b>.</p><p><b>Observaciones:</b></p><p>" + expedienteLicencia.getResolucion().getAsunto() + "</p>";
                    }else{
                        contenido= "<p>El expediente número <b>"+expedienteLicencia.getNumExpediente() + "</b>, presentado por</p><p><b>D./Dña. " + solicitud.getPropietario().getNombre() + "</b> con D.N.I. "+ solicitud.getPropietario().getDniCif() + ",</p><p>ha sido <b>Aprobada</b>.</p><p><b>Observaciones:</b></p><p>" + expedienteLicencia.getResolucion().getAsunto() + "</p>";
                    }
                }
                String cnom= CTeletramitacion.crearFichNotif(notif_text, contenido, CConstantesTeletramitacion.VU_NOTIF_CRUTAACC);
                COperacionesDatabase.insertarNotifTelematico(expedienteLicencia.getNumExpediente(), dnicif, cnom, notif_text, notif_text, notif_text, CConstantesTeletramitacion.VU_NOTIF_CRUTAACC, null, cproc);
			}
            connection.commit();
    		safeClose(rs, statement, preparedStatement, connection);
			return true;
		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			logger.error("Exception: ", ex);
			return false;
		}
	}



	public static boolean insertEventoExpediente(CExpedienteLicencia expedienteLicencia, int idTipoLicencia,boolean informativo, boolean silencioEvent, Connection connection) {


		CWorkflowLine workflowLine = COperacionesDatabase.getWorkflowLine(expedienteLicencia.getEstado().getIdEstado(),idTipoLicencia, connection);
		String content = workflowLine.getEventText();

		return insertEventoExpediente(expedienteLicencia, content, informativo, silencioEvent);
	}

	public static boolean insertEventoExpediente(CExpedienteLicencia expedienteLicencia, String content, boolean informativo, boolean silencioEvent) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			connection.setAutoCommit(true);
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			long secuencia = getTableSequence();

			preparedStatement = connection.prepareStatement("INSERT INTO EVENTOS(ID_EVENTO,ID_SOLICITUD,NUM_EXPEDIENTE,REVISADO,CONTENT,FECHA, ID_ESTADO)VALUES (?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, expedienteLicencia.getIdSolicitud());
			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(4, "0");
			preparedStatement.setString(5, content);
			preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
            preparedStatement.setInt(7, expedienteLicencia.getEstado().getIdEstado());

			preparedStatement.execute();
			//**********************************
			//** Si es informativo no haremos nada
			//*********************************************
			if (!informativo) {
				activateEventExpediente(expedienteLicencia.getNumExpediente(), silencioEvent);
				logger.debug("Evento no informativo.Actualizando expediente.");
			}

			connection.commit();
			safeClose(rs, statement, preparedStatement, connection);

			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}

	private static void checkCambioEstado(Connection connection, CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia, Principal userPrincipal) throws Exception {
		PreparedStatement preparedStatement= null;
        try {
			int estadoAntiguo = searchExpedienteLicencia(expedienteLicencia.getNumExpediente()).getEstado().getIdEstado();
			int estadoNuevo = expedienteLicencia.getEstado().getIdEstado();

			if (estadoAntiguo == estadoNuevo) {
				logger.debug("No hubo cambio de estado. estadoAntiguo: " + estadoAntiguo + ". estadoNuevo: " + estadoNuevo);
                /** Insertamos historico GENERICO por modificación en el expediente (no es por cambio de estado). */
                COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), false, connection);
				return;
			}

			if (estadoAntiguo == CConstantesComando.ESTADO_DURMIENTE)
            {
                /* Caso en el que el expediente se pueda reabrir. Por aquí no va a pasar, porque
                tanto si el expediente ha pasado a DURMIENTE por acción del usuario, por expiración de plazos
                del workflow o por silencio administrativo, los estados posibles a los que puede pasar un expediente
                en estado DURMIENTE es NINGUNO (se queda donde está). */
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET FECHA_CAMBIO_ESTADO=?,PLAZO_EVENT=NULL,SILENCIO_EVENT=NULL,FECHA_APERTURA=?,SILENCIO_TRIGGERED=? WHERE NUM_EXPEDIENTE=?");
				preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
				preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime() + (CConstantesWorkflow.diasSilencioAdministrativo * 60 * 1000)));
				preparedStatement.setString(3, "0");
				preparedStatement.setString(4, expedienteLicencia.getNumExpediente());
			} else {
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET FECHA_CAMBIO_ESTADO=?,PLAZO_EVENT=NULL,SILENCIO_EVENT=NULL WHERE NUM_EXPEDIENTE=?");
				preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
				preparedStatement.setString(2, expedienteLicencia.getNumExpediente());
			}


			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);
			logger.info("Hubo cambio de estado. Fecha actualizada. Expediente: "+expedienteLicencia.getNumExpediente()+ " Nuevo estado: "+estadoNuevo);


			switch (estadoNuevo) {
                //2
				case CConstantesComando.ESTADO_MEJORA_DATOS:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),true, false,connection);
					COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionMejoraDatos, solicitudLicencia, true);
					if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
						COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionMejoraDatos, solicitudLicencia, false);
					}
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);

                    /** insertamos una entrada en la tabla MEJORA en BD */
                    Mejora mejora= new Mejora();
                    mejora.setFecha(new Date());
                    mejora.setIdSolicitud(solicitudLicencia.getIdSolicitud());
                    mejora.setNumExpediente(expedienteLicencia.getNumExpediente());
                    mejora.setObservaciones(expedienteLicencia.getObservaciones());
                    insertarMejora(connection, mejora);
					break;
                //3
				case CConstantesComando.ESTADO_SOLICITUD_INFORMES:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            true, false,connection);
 					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //4
				case CConstantesComando.ESTADO_ESPERA_INFORMES:
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //5
				case CConstantesComando.ESTADO_EMISION_INFORME_RESOLUCION:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia,
                                       solicitudLicencia.getTipoLicencia().getIdTipolicencia(), true, false,connection);
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //6
				case CConstantesComando.ESTADO_ESPERA_ALEGACIONES:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),true, false,connection);
					COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionEsperaAlegacion, solicitudLicencia, true);
					if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
						COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                                solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionEsperaAlegacion, solicitudLicencia, false);
					}
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);

                    /** insertamos una entrada en la tabla ALEGACION en BD */
                    Alegacion alegacion= new Alegacion();
                    alegacion.setFecha(new Date());
                    alegacion.setIdSolicitud(solicitudLicencia.getIdSolicitud());
                    alegacion.setNumExpediente(expedienteLicencia.getNumExpediente());
                    alegacion.setObservaciones("");
                    insertarAlegacion(connection, alegacion);

					break;
                //7
				case CConstantesComando.ESTADO_ACTUALIZACION_INFORME_RESOLUCION:
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //8
				case CConstantesComando.ESTADO_EMISION_PROPUESTA_RESOLUCION:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),true, false,connection);
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;

                //10
				case CConstantesComando.ESTADO_FORMALIZACION_LICENCIA:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(), true, false,connection);
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //9
				case CConstantesComando.ESTADO_NOTIFICACION_DENEGACION:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),true, false,connection);
                    COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionDenegacion, solicitudLicencia, true);
					if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
						COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                                solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionDenegacion, solicitudLicencia, false);
					}
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //11
				case CConstantesComando.ESTADO_NOTIFICACION_APROBACION:
					COperacionesDatabase.insertEventoExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),true, false,connection);
                    COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, true);
					if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
						COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                                solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, false);
					}
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);

					break;
                //12
				case CConstantesComando.ESTADO_EJECUCION:
					COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia, solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                            solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, true);
					if ((solicitudLicencia.getRepresentante() != null)  && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
						COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,solicitudLicencia.getTipoLicencia().getIdTipolicencia(),
                                 solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, false);
					}
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
                //13
				case CConstantesComando.ESTADO_DURMIENTE:
					COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
                    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_FINALIZACION=? WHERE NUM_EXPEDIENTE=?");
                    preparedStatement.setInt(1, CConstantesComando.TIPO_FINALIZACION_EXPRESA);
                    preparedStatement.setString(2, expedienteLicencia.getNumExpediente());
                    preparedStatement.execute();
                    connection.commit();
					break;


				default:
                    COperacionesDatabase.insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true,connection);
					break;
			}

			//********************************
			//** VU
			//********************************
			if (expedienteLicencia.getVU().equals("1")) {
				COperacionesDatabase.insertHitoTelematico(expedienteLicencia,
                        solicitudLicencia.getTipoLicencia().getIdTipolicencia());
			}

            safeClose(null, null, preparedStatement, null);

		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}

	}
	
	private static void checkCambioTasa(Connection connection, CSolicitudLicencia solicitudLicencia,
			CExpedienteLicencia expedienteLicencia, Principal userPrincipal) throws Exception {
		PreparedStatement preparedStatement= null;
        try {
			Double tasaAnterior = getTasaSolicitudLicencia(String.valueOf(solicitudLicencia.getIdSolicitud()),connection); 
			if (tasaAnterior != null && tasaAnterior.doubleValue() != solicitudLicencia.getTasa()){
				ArrayList parametrosEntrada = new ArrayList();
				DecimalFormat decimalFormat = new DecimalFormat("0.00");				
				parametrosEntrada.add(decimalFormat.format(tasaAnterior.doubleValue()));
				parametrosEntrada.add(decimalFormat.format(solicitudLicencia.getTasa()));
				insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(),
						CWorkflowLiterales.HISTORICO_CAMBIO_TASA, parametrosEntrada,connection);
			}		   

		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
	}
	
	private static void checkCambioImpuesto(Connection connection, CSolicitudLicencia solicitudLicencia,
			CExpedienteLicencia expedienteLicencia, Principal userPrincipal) throws Exception {
		PreparedStatement preparedStatement= null;
        try {
			Double impuestoAnterior = getImpuestoSolicitudLicencia(String.valueOf(solicitudLicencia.getIdSolicitud()),connection); 
			if (impuestoAnterior != null && impuestoAnterior.doubleValue() != solicitudLicencia.getImpuesto()){
				ArrayList parametrosEntrada = new ArrayList();
				DecimalFormat decimalFormat = new DecimalFormat("0.00");				
				parametrosEntrada.add(decimalFormat.format(impuestoAnterior.doubleValue()));
				parametrosEntrada.add(decimalFormat.format(solicitudLicencia.getImpuesto()));
				insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(),
						CWorkflowLiterales.HISTORICO_CAMBIO_IMPUESTO, parametrosEntrada,connection);
			}	
		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
	}



	/**
	 * MODIFICACION
	 */
	public static CResultadoOperacion modificarExpedienteLicencias(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia, Principal userPrincipal, String idMunicipio) {

		Connection connection= null;
		PreparedStatement preparedStatement= null;
		Statement statement = null;
		ResultSet rs= null;
		boolean removeRepresentante= false;
		boolean addRepresentante= false;
        /*
		boolean removeTecnico= false;
		boolean addTecnico= false;
        */
		boolean removePromotor= false;
		boolean addPromotor= false;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}
			connection.setAutoCommit(false);

            int estadoAntiguo = searchExpedienteLicencia(expedienteLicencia.getNumExpediente()).getEstado().getIdEstado();
            //**********************************************
            //** Actualizamos todos los eventos anteriores que no
            //** estuviesen actualizados de forma automática. Se supone que si un expediente
            //** se modifica ha sido revisado por alguien
            //***************************************************
             preparedStatement = connection.prepareStatement("UPDATE EVENTOS SET " +
							"REVISADO_POR=?, REVISADO=1 WHERE REVISADO=0::text AND num_expediente=?");
  		     preparedStatement.setString(1, userPrincipal.getName());
			 preparedStatement.setString(2, expedienteLicencia.getNumExpediente());
			 preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

            /** ACTUALIZAR PERSONAS JURIDICO FISICAS */
            /** Asi conseguimos que el propietario tenga id_persona, necesario para las notificaciones. */
			CPersonaJuridicoFisica propietario= insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPropietario(), idMunicipio);
            propietario.setDatosNotificacion(solicitudLicencia.getPropietario().getDatosNotificacion());
            solicitudLicencia.setPropietario(propietario);
			if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getIdPersona() != -1)) {
				/** la solicitud tiene representante y este se encuentra en BD */
				CPersonaJuridicoFisica representante= insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getRepresentante(), idMunicipio);
                representante.setDatosNotificacion(solicitudLicencia.getRepresentante().getDatosNotificacion());
                solicitudLicencia.setRepresentante(representante);
			} else if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getIdPersona() == -1)) {
				/** la solicitud tiene representante y este no se ha asignado pulsando la lupa (no sabemos si ya existe en BD)*/
				CPersonaJuridicoFisica representante= insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getRepresentante(), idMunicipio);
				/** le asignamos los nuevos datos de notificacion */
				representante.setDatosNotificacion(solicitudLicencia.getRepresentante().getDatosNotificacion());
				solicitudLicencia.setRepresentante(representante);
				addRepresentante = true;
			} else if (solicitudLicencia.getRepresentante() == null) {
				/** la solicitud no tiene representante */
				removeRepresentante = true;
			}


            if (solicitudLicencia.getTecnicos() != null){
                Vector vTecnicos= new Vector();
                for (Enumeration e= solicitudLicencia.getTecnicos().elements(); e.hasMoreElements(); ){
                    CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)e.nextElement();
                    if (tecnico.getIdPersona() != -1) {
                        //** la solicitud tiene tecnico y este se encuentra en BD
                        CPersonaJuridicoFisica auxTecnico= insertUpdatePersonaJuridicoFisica(connection, tecnico, idMunicipio);
                        /** le asignamos los datos de notificacion. */
                        auxTecnico.setDatosNotificacion(tecnico.getDatosNotificacion());
                        vTecnicos.add(auxTecnico);
                    } else if (tecnico.getIdPersona() == -1) {
                        //** la solicitud tiene tecnico y este no se ha asignado pulsando la lupa (no sabemos si ya existe en BD)
                        CPersonaJuridicoFisica auxTecnico= insertUpdatePersonaJuridicoFisica(connection, tecnico, idMunicipio);
                        /** le asignamos los nuevos datos de notificacion */
                        auxTecnico.setDatosNotificacion(tecnico.getDatosNotificacion());
                        vTecnicos.add(auxTecnico);
                    }
                }
                solicitudLicencia.setTecnicos(vTecnicos);
            }


			if ((solicitudLicencia.getPromotor() != null) && (solicitudLicencia.getPromotor().getIdPersona() != -1)) {
				/** la solicitud tiene promotor y este se encuentra en BD */
				CPersonaJuridicoFisica auxPromotor= insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPromotor(), idMunicipio);
                auxPromotor.setDatosNotificacion(solicitudLicencia.getPromotor().getDatosNotificacion());
                solicitudLicencia.setPromotor(auxPromotor);
			} else if ((solicitudLicencia.getPromotor() != null) && (solicitudLicencia.getPromotor().getIdPersona() == -1)) {
				/** la solicitud tiene promotor y este no se ha asignado pulsando la lupa (no sabemos si ya existe en BD)*/
				CPersonaJuridicoFisica auxPromotor= insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPromotor(), idMunicipio);
				/** le asignamos los nuevos datos de notificacion */
				auxPromotor.setDatosNotificacion(solicitudLicencia.getPromotor().getDatosNotificacion());
				solicitudLicencia.setPromotor(auxPromotor);
				addPromotor = true;
			} else if (solicitudLicencia.getPromotor() == null) {
				/** la solicitud no tiene promotor */
				removePromotor = true;
			}

            /** CHEQUEAR CAMBIO DE ESTADO */
            checkCambioEstado(connection, solicitudLicencia, expedienteLicencia, userPrincipal);
            checkCambioTasa(connection, solicitudLicencia, expedienteLicencia, userPrincipal);
            checkCambioImpuesto(connection, solicitudLicencia, expedienteLicencia, userPrincipal);


			//****************************************
			//** Insertamos en SOLICITUD_LICENCIA
			//****************************************************
			preparedStatement = connection.prepareStatement("UPDATE SOLICITUD_LICENCIA SET " +
					"ID_TIPO_LICENCIA=?, ID_TIPO_OBRA=?, NUM_ADMINISTRATIVO=?, " +
					"UNIDAD_TRAMITADORA=?, UNIDAD_DE_REGISTRO=?, MOTIVO=?, ASUNTO=?, " +
					"TASA=?, TIPO_VIA_AFECTA=?, NOMBRE_VIA_AFECTA=?, " +
					"NUMERO_VIA_AFECTA=?, PORTAL_AFECTA=?, PLANTA_AFECTA=?, LETRA_AFECTA=?, CPOSTAL_AFECTA=?, " +
					"OBSERVACIONES=?, REPRESENTANTE=?,NOMBRE_COMERCIAL=?, TECNICO=?, PROMOTOR=?, IMPUESTO=?, OBSERVACIONES_DOC_ENTREGADA=?, FECHA_RESOLUCION=?, FECHA_LIMITE_OBRA=?, PROPIETARIO=? WHERE ID_SOLICITUD=" + solicitudLicencia.getIdSolicitud());

			preparedStatement.setInt(1, solicitudLicencia.getTipoLicencia().getIdTipolicencia());
			preparedStatement.setInt(2, solicitudLicencia.getTipoObra().getIdTipoObra());
			preparedStatement.setString(3, solicitudLicencia.getNumAdministrativo());
			preparedStatement.setString(4, solicitudLicencia.getUnidadTramitadora());
			preparedStatement.setString(5, solicitudLicencia.getUnidadDeRegistro());
			preparedStatement.setString(6, solicitudLicencia.getMotivo());
			preparedStatement.setString(7, solicitudLicencia.getAsunto());
			preparedStatement.setDouble(8, new Double(solicitudLicencia.getTasa()).doubleValue());
			preparedStatement.setString(9, solicitudLicencia.getTipoViaAfecta());
			preparedStatement.setString(10, solicitudLicencia.getNombreViaAfecta());
			preparedStatement.setString(11, solicitudLicencia.getNumeroViaAfecta());
			preparedStatement.setString(12, solicitudLicencia.getPortalAfecta());
			preparedStatement.setString(13, solicitudLicencia.getPlantaAfecta());
			preparedStatement.setString(14, solicitudLicencia.getLetraAfecta());
			preparedStatement.setString(15, solicitudLicencia.getCpostalAfecta());
			preparedStatement.setString(16, solicitudLicencia.getObservaciones());
			if (!removeRepresentante)
				preparedStatement.setLong(17, solicitudLicencia.getRepresentante().getIdPersona());
			else
				preparedStatement.setNull(17, java.sql.Types.NUMERIC);

			preparedStatement.setString(18, solicitudLicencia.getNombreComercial());
            /*
			if (!removeTecnicos)
				preparedStatement.setLong(19, solicitudLicencia.getTecnico().getIdPersona());
			else
				preparedStatement.setNull(19, java.sql.Types.LONGVARBINARY);
            */
            preparedStatement.setNull(19, java.sql.Types.NUMERIC);

			if (!removePromotor)
				preparedStatement.setLong(20, solicitudLicencia.getPromotor().getIdPersona());
			else
				preparedStatement.setNull(20, java.sql.Types.NUMERIC);

            preparedStatement.setDouble(21, new Double(solicitudLicencia.getImpuesto()).doubleValue());
            preparedStatement.setString(22, solicitudLicencia.getObservacionesDocumentacionEntregada());
            if (solicitudLicencia.getFechaResolucion() != null)
                preparedStatement.setTimestamp(23, new Timestamp(solicitudLicencia.getFechaResolucion().getTime()));
            else  preparedStatement.setNull(23, java.sql.Types.TIMESTAMP);
            if (solicitudLicencia.getFechaLimiteObra() != null)
                preparedStatement.setTimestamp(24, new Timestamp(solicitudLicencia.getFechaLimiteObra().getTime()));
            else  preparedStatement.setNull(24, java.sql.Types.TIMESTAMP);
            preparedStatement.setLong(25, solicitudLicencia.getPropietario().getIdPersona());


			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

            updateDatosActividad(connection, solicitudLicencia.getIdSolicitud(), solicitudLicencia.getDatosActividad());

			deleteReferenciasCatastrales(connection, solicitudLicencia.getIdSolicitud());

            /** La modificacion de expediente aunque este haya sido creado por Ventanilla Unica, se
             * realiza a traves de las aplicaciones cliente de GeoPISTA. Por lo tanto pasamos como valor
             * del parametro vu 0, ya que en modificación la dirección de una referencia catastral se
             * rellena automaticamente al pinchar sobre ella en el mapa.
             */
			insertaReferenciasCatastrales(connection, solicitudLicencia.getIdSolicitud(), solicitudLicencia.getReferenciasCatastrales(), null, "0");

            /** Tecnicos */
            deleteTecnicosSolicitud(connection, solicitudLicencia.getIdSolicitud());
            insertaTecnicosSolicitud(connection, solicitudLicencia.getIdSolicitud(), solicitudLicencia.getTecnicos());

            //****************************************
			//** Insertamos los datos de notificacion de las personas juridicas
			//****************************************************
			insertUpdateDatosNotificacion(connection, solicitudLicencia.getPropietario(), solicitudLicencia.getIdSolicitud());
			if (addRepresentante) {
				/** comprobamos si el usuario introducido sin pulsar lupa, tiene datos de notificacion para esa solicitud */
				if (getDatosNotificacionByIdPersonaByIdSolicitud(solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getIdSolicitud(),connection) == null) {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
				} else {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
				}
			} else {
				insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
			}

            /*
			if (addTecnico) {
				System.out.println("************************** ANADIMOS TECNICO ");
				//** comprobamos si el usuario introducido sin pulsar lupa, tiene datos de notificacion para esa solicitud
				if (getDatosNotificacionByIdPersonaByIdSolicitud(solicitudLicencia.getTecnico().getIdPersona(), solicitudLicencia.getIdSolicitud()) == null) {
					System.out.println("************************ NO hay datos de notificacion para PERSONA=" + solicitudLicencia.getTecnico().getIdPersona() + " y SOLICITUD=" + solicitudLicencia.getIdSolicitud());
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getTecnico(), solicitudLicencia.getIdSolicitud());
				} else {
					System.out.println("************************ HAY datos de notificacion para PERSONA=" + solicitudLicencia.getTecnico().getIdPersona() + " y SOLICITUD=" + solicitudLicencia.getIdSolicitud());
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getTecnico(), solicitudLicencia.getIdSolicitud());
				}
			} else
				insertUpdateDatosNotificacion(connection, solicitudLicencia.getTecnico(), solicitudLicencia.getIdSolicitud());
            */
            if (solicitudLicencia.getTecnicos() != null){
                for (Enumeration e= solicitudLicencia.getTecnicos().elements(); e.hasMoreElements();){
                    CPersonaJuridicoFisica tecnico= (CPersonaJuridicoFisica)e.nextElement();
                    insertUpdateDatosNotificacion(connection, tecnico, solicitudLicencia.getIdSolicitud());
                }
            }

			if (addPromotor) {
				/** comprobamos si el usuario introducido sin pulsar lupa, tiene datos de notificacion para esa solicitud */
				if (getDatosNotificacionByIdPersonaByIdSolicitud(solicitudLicencia.getPromotor().getIdPersona(), solicitudLicencia.getIdSolicitud(),connection) == null) {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getPromotor(), solicitudLicencia.getIdSolicitud());
				} else {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getPromotor(), solicitudLicencia.getIdSolicitud());
				}
			} else
				insertUpdateDatosNotificacion(connection, solicitudLicencia.getPromotor(), solicitudLicencia.getIdSolicitud());


			//****************************************
			//** Actualizamos NOTIFICACIONES
			//****************************************
			Vector notificaciones = expedienteLicencia.getNotificaciones();
			if (notificaciones != null) {
				Enumeration enumerationElement = notificaciones.elements();
				while (enumerationElement.hasMoreElements()) {
					CNotificacion notificacion = (CNotificacion) enumerationElement.nextElement();

                    preparedStatement = connection.prepareStatement("UPDATE NOTIFICACION SET " +
                            "ID_ESTADO=?, RESPONSABLE=?, FECHA_NOTIFICACION=?, FECHA_REENVIO=?, ENTREGADA_A=? WHERE ID_NOTIFICACION=" + notificacion.getIdNotificacion() +
                            " AND NUM_EXPEDIENTE='" + expedienteLicencia.getNumExpediente() + "'");
                    preparedStatement.setInt(1, notificacion.getEstadoNotificacion().getIdEstado());
                    preparedStatement.setString(2, notificacion.getResponsable());
                    if (notificacion.getFechaNotificacion() == null){
                        preparedStatement.setNull(3, java.sql.Types.TIMESTAMP);
                    }
                    else{
                        preparedStatement.setTimestamp(3, new Timestamp(notificacion.getFechaNotificacion().getTime()));
                    }
                    if (notificacion.getFecha_reenvio() == null){
                        preparedStatement.setNull(4, java.sql.Types.TIMESTAMP);
                    }else{
                        preparedStatement.setTimestamp(4, new Timestamp(notificacion.getFecha_reenvio().getTime()));
                    }
                    if (notificacion.getEntregadaA() == null){
                        preparedStatement.setNull(5, java.sql.Types.VARCHAR);
                    }
                    else{
                        preparedStatement.setString(5, notificacion.getEntregadaA());
                    }
                    preparedStatement.execute();
                    safeClose(null, null, preparedStatement, null);

				}
			}

	    	//****************************************
			//** Actualizamos HISTORICO
			//****************************************
			Vector historico = expedienteLicencia.getHistorico();
			if (historico != null) {
				Enumeration enumerationElement = historico.elements();
				while (enumerationElement.hasMoreElements()) {
					CHistorico h = (CHistorico) enumerationElement.nextElement();

					if (h.getHasBeen() == CConstantesComando.CMD_HISTORICO_MODIFIED) {
						preparedStatement = connection.prepareStatement("UPDATE HISTORICO SET " +
								"USUARIO=?, APUNTE=? WHERE ID_HISTORICO=" + h.getIdHistorico());

						preparedStatement.setString(1, userPrincipal.getName());
						preparedStatement.setString(2, h.getApunte());
						preparedStatement.execute();
                        safeClose(null, null, preparedStatement, null);

						logger.debug("*******************ACTUALIZAMOS HISTORICO CON ID=" + h.getIdHistorico());

					} else if (h.getHasBeen() == CConstantesComando.CMD_HISTORICO_ADDED) {
						insertarHistorico(h, userPrincipal);
					} else if (h.getHasBeen() == CConstantesComando.CMD_HISTORICO_DELETED) {
						borrarHistorico(h);
					}
				}
			}
            /*
            logger.info("...................................................................");
            logger.info("................EXPEDIENTE="+expedienteLicencia.getNumExpediente());
            logger.info("................ESTADO="+expedienteLicencia.getEstado().getIdEstado());
            logger.info("................DATE="+new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            */
            /*
            logger.info("................fechaCambioEstadoFormatted: " + fechaCambioEstadoFormatted);
            logger.info("...................................................................");
            */
			//****************************************
			//** Actualizamos en EXPEDIENTE
			//****************************************
			preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET " +
					"ID_TRAMITACION=?,  ID_ESTADO=?, SERVICIO_ENCARGADO=?, ASUNTO=?, " +
					"SILENCIO_ADMINISTRATIVO=?, FORMA_INICIO=?, NUM_FOLIOS=?, RESPONSABLE=?, " +
					"OBSERVACIONES=?,CNAE=? WHERE NUM_EXPEDIENTE='" + expedienteLicencia.getNumExpediente() + "'");
			preparedStatement.setInt(1, expedienteLicencia.getTipoTramitacion().getIdTramitacion());
			preparedStatement.setInt(2, expedienteLicencia.getEstado().getIdEstado());
			preparedStatement.setString(3, expedienteLicencia.getServicioEncargado());
			preparedStatement.setString(4, expedienteLicencia.getAsunto());
			preparedStatement.setString(5, expedienteLicencia.getSilencioAdministrativo());
			preparedStatement.setString(6, expedienteLicencia.getFormaInicio());
			preparedStatement.setInt(7, expedienteLicencia.getNumFolios());
			preparedStatement.setString(8, expedienteLicencia.getResponsable());
			preparedStatement.setString(9, expedienteLicencia.getObservaciones());
			preparedStatement.setString(10, expedienteLicencia.getCNAE());
			preparedStatement.execute();

			safeClose(null, null, preparedStatement, null);


			//**********************************************************
            //** Insertamos la documentacion entregada para una licencia
            //**********************************************************
            insertaDocumentacionEntregada(connection, solicitudLicencia.getIdSolicitud(), solicitudLicencia.getDocumentacionEntregada());

            //****************************************
            //** Actualizamos ANEXOS
            //****************************************
			modificaAnexosSolicitud(solicitudLicencia.getIdSolicitud(), solicitudLicencia.getAnexos(), null, null);

            //***************************************
            //** MEJORAS
            //***************************************
            if (expedienteLicencia.getEstado().getIdEstado() == CConstantesComando.ESTADO_MEJORA_DATOS ||
                estadoAntiguo==CConstantesComando.ESTADO_MEJORA_DATOS){
                updateMejorasSolicitud(connection, solicitudLicencia.getMejoras(), solicitudLicencia.getIdSolicitud());
            }
            expedienteLicencia.setIdSolicitud(solicitudLicencia.getIdSolicitud());
            expedienteLicencia.setSolicitud(solicitudLicencia);
            actualizaInformes(connection, expedienteLicencia);
            if (expedienteLicencia.getEstado().getIdEstado()==CConstantesComando.ESTADO_EMISION_PROPUESTA_RESOLUCION ||
                estadoAntiguo==CConstantesComando.ESTADO_EMISION_PROPUESTA_RESOLUCION){
                actualizaResolucion(connection, expedienteLicencia);
                /* actualizamos subparcelas */
                 Resolucion resolucion= expedienteLicencia.getResolucion();
                 if ((resolucion != null) && (resolucion.isFavorable()))
                 {
                    if (solicitudLicencia.getTipoLicencia().getIdTipolicencia() == CConstantesComando.TIPO_OBRA_MAYOR)
                    {
                        if ((solicitudLicencia.getTipoObra().getIdTipoObra() == CConstantesComando.tipoObraDemolicion))
                            actualizarSubparcelas(connection, solicitudLicencia.getReferenciasCatastrales(), idMunicipio, CConstantesComando.SOLAR);
                        if ((solicitudLicencia.getTipoObra().getIdTipoObra() == CConstantesComando.tipoObraNuevaPlanta))
                            actualizarSubparcelas(connection, solicitudLicencia.getReferenciasCatastrales(), idMunicipio, CConstantesComando.NUEVA_PLANTA);
                    }
                }
            }

            //***************************************
            //** ALEGACION
            //***************************************
            if (expedienteLicencia.getEstado().getIdEstado() == CConstantesComando.ESTADO_ESPERA_ALEGACIONES||
                estadoAntiguo==CConstantesComando.ESTADO_ESPERA_ALEGACIONES){
                updateAlegacionExpediente(connection, expedienteLicencia.getAlegacion(), solicitudLicencia.getIdSolicitud());
            }

            connection.commit();
            

			return new CResultadoOperacion(true, "");

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}
		finally{
			safeClose(rs, statement, connection);
		}

	}
    /**
     * El único cambio que se puede producir es que se borre algún informe
     *
     * @param connection
     * @param expediente
     */
    private static void actualizaInformes(Connection connection, CExpedienteLicencia expediente)
    {
        try
        {
           if (connection ==null || expediente==null ) return;
           Vector informesOld = getInformesExpediente(connection, expediente.getNumExpediente());
           if (informesOld==null || informesOld.size()==0) return; //nada que actualizar
           for(Enumeration e=informesOld.elements();e.hasMoreElements();)
           {
               Informe oldInforme= (Informe)e.nextElement();
               oldInforme.setIdSolicitud(expediente.getIdSolicitud());
               if  (expediente.getInforme(oldInforme.getId())==null)
                    borrarInforme(connection, oldInforme);
           }

        }catch(Exception e)
        {
            logger.error("Excepcion al actualizar los expedientes: ", e);
        }
    }
      /**
     * Actualizamos la resolucion
     *
     * @param connection
     * @param expediente
     */
    private static void actualizaResolucion(Connection connection, CExpedienteLicencia expediente)
    {
        try
        {
           if (connection ==null || expediente==null || expediente.getResolucion()==null) return;
           if (getResolucion(connection,expediente.getNumExpediente())==null)
               addResolucion(connection,expediente);
           else
               updateResolucion(connection,expediente);

        }catch(Exception e)
        {
            logger.error("Excepcion al actualizar la resolucion: ", e);
        }
    }
    private static void addResolucion(Connection connection, CExpedienteLicencia expediente)
    {
        try
        {
            if (connection ==null || expediente==null || expediente.getResolucion()==null) return;
            String sql="insert into resolucion (id_solicitud, num_expediente, a_favor, " +
                    "fecha_resolucion,organo_aprobacion, coletilla,asunto) values (?,?,?,?,?,?,?)";
            logger.info("Sql:"+sql);
            PreparedStatement pst=connection.prepareStatement(sql);
            pst.setLong(1,expediente.getIdSolicitud());
            pst.setString(2,expediente.getNumExpediente());
            Resolucion resolucion=expediente.getResolucion();
            pst.setString(3,resolucion.isFavorable()?"1":"0");
            pst.setDate(4, (resolucion.getFecha()==null?null:new java.sql.Date(resolucion.getFecha().getTime())));
            pst.setString(5,resolucion.getOrgano());
            pst.setString(6,resolucion.getColetilla());
            pst.setString(7,resolucion.getAsunto());
            pst.execute();
            safeClose(null, null, pst, null);
        }catch(Exception e)
        {
            logger.error("Excepcion al añadir la resolución: ", e);
        }
   }
       private static void updateResolucion(Connection connection, CExpedienteLicencia expediente)
    {
        PreparedStatement pst=null;
        try
        {
            if (connection ==null || expediente==null || expediente.getResolucion()==null) return;
            String sql="update resolucion set a_favor=?, fecha_resolucion=?, organo_aprobacion=?,coletilla=?, " +
                    "asunto=? where num_expediente=?";
            logger.info("Sql:"+sql);
            pst = connection.prepareStatement(sql);
            Resolucion resolucion=expediente.getResolucion();
            pst.setString(1,resolucion.isFavorable()?"1":"0");
            pst.setDate(2, (resolucion.getFecha()==null?null:new java.sql.Date(resolucion.getFecha().getTime())));
            pst.setString(3,resolucion.getOrgano());
            pst.setString(4,resolucion.getColetilla());
            pst.setString(5,resolucion.getAsunto());
            pst.setString(6,expediente.getNumExpediente());
            pst.execute();
            safeClose(null, null, pst, null);
        }catch(Exception e)
        {
            safeClose(null, null, pst, null);
            logger.error("Excepcion al actualizar la resolución: ", e);
        }
   }

    private static void borrarInforme(Connection connection, Informe informe)
    {
        try
        {
            if (connection == null || informe==null) return;
            String sql="delete from informe_preceptivo where id_informe='"+informe.getId()+"'";
            Statement st=connection.createStatement();
            st.execute(sql);
            connection.commit();
            safeClose(null,st,null);
            //intentamos borrar el fichero
            String path = "anexos" + File.separator + "licencias" + File.separator + informe.getIdSolicitud()+
                    File.separator+ "informes"+ File.separator+informe.getFichero();
		    File fileDelete= new File(path);
            if (fileDelete!=null)
                fileDelete.delete();
        }catch(Exception e)
        {
            logger.error("Excepcion al borrar el informe: ", e);
        }

    }
	private static void insertUpdateDatosNotificacion(Connection connection, CPersonaJuridicoFisica personaJuridicoFisica, long idSolicitud) throws Exception {
        PreparedStatement preparedStatement= null;
		try {

			if (personaJuridicoFisica == null) {
				logger.info("No se insertan los datos de notificaciones ya que no hay persona");
				return;
			}

			CDatosNotificacion datosNotificacion = personaJuridicoFisica.getDatosNotificacion();

            /* Comprobamos que no existan datos de notificacion para la misma persona en la misma solicitud */
			CDatosNotificacion datosNotificacionSistema = getDatosNotificacionByIdPersonaByIdSolicitud(personaJuridicoFisica.getIdPersona(), idSolicitud, connection);
			if (datosNotificacionSistema == null) {

				logger.info("No se encuentran los datos de notificacion. Inserting.");
                String sql = "INSERT INTO DATOS_NOTIFICACION ( ID_PERSONA, ID_SOLICITUD, ID_VIA_NOTIFICACION, FAX, TELEFONO, MOVIL, EMAIL, TIPO_VIA, NOMBRE_VIA, NUMERO_VIA, PORTAL, PLANTA, ESCALERA, LETRA, CPOSTAL, MUNICIPIO, PROVINCIA, NOTIFICAR ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);

                logger.debug("datosNotificacion.getViaNotificacion().getIdViaNotificacion(): " + datosNotificacion.getViaNotificacion().getIdViaNotificacion());
                preparedStatement.setLong(1, personaJuridicoFisica.getIdPersona());
                preparedStatement.setLong(2, idSolicitud);
                preparedStatement.setInt(3, datosNotificacion.getViaNotificacion().getIdViaNotificacion());
                preparedStatement.setString(4, datosNotificacion.getFax());
                preparedStatement.setString(5, datosNotificacion.getTelefono());
                preparedStatement.setString(6, datosNotificacion.getMovil());
                preparedStatement.setString(7, datosNotificacion.getEmail());
                preparedStatement.setString(8, datosNotificacion.getTipoVia());
                preparedStatement.setString(9, datosNotificacion.getNombreVia());
                preparedStatement.setString(10, datosNotificacion.getNumeroVia());
                preparedStatement.setString(11, datosNotificacion.getPortal());
                preparedStatement.setString(12, datosNotificacion.getPlanta());
                preparedStatement.setString(13, datosNotificacion.getEscalera());
                preparedStatement.setString(14, datosNotificacion.getLetra());
                preparedStatement.setString(15, datosNotificacion.getCpostal());
                preparedStatement.setString(16, datosNotificacion.getMunicipio());
                preparedStatement.setString(17, datosNotificacion.getProvincia());
                preparedStatement.setString(18, datosNotificacion.getNotificar());

    			logger.info("SQL Ejecutando query insertUpdateDatosNotificacion: "+((org.enhydra.jdbc.standard.StandardXAPreparedStatement)preparedStatement).ps.toString());

                preparedStatement.execute();
                safeClose(null, null, preparedStatement, null);
                logger.info("DatosNotificacion insertado. personaJuridicoFisica.getDniCif(): " + personaJuridicoFisica.getDniCif());

			} else {
				logger.info("Se encuentran los datos de notificacion. Updating.");


				String sql = "UPDATE DATOS_NOTIFICACION SET " +
						"ID_VIA_NOTIFICACION=?, FAX=?, TELEFONO=?, MOVIL=?, EMAIL=?, " +
						"TIPO_VIA=?, NOMBRE_VIA=?, NUMERO_VIA=?, PORTAL=?, PLANTA=?, " +
						"ESCALERA=?, LETRA=?, CPOSTAL=?, MUNICIPIO=?, PROVINCIA=?, " +
						"NOTIFICAR=? WHERE ID_SOLICITUD=" + idSolicitud + " AND ID_PERSONA=" + personaJuridicoFisica.getIdPersona();

				logger.info("SQL=" + sql);

				preparedStatement = connection.prepareStatement(sql);

				preparedStatement.setInt(1, datosNotificacion.getViaNotificacion().getIdViaNotificacion());
				preparedStatement.setString(2, datosNotificacion.getFax());
				preparedStatement.setString(3, datosNotificacion.getTelefono());
				preparedStatement.setString(4, datosNotificacion.getMovil());
				preparedStatement.setString(5, datosNotificacion.getEmail());
				preparedStatement.setString(6, datosNotificacion.getTipoVia());
				preparedStatement.setString(7, datosNotificacion.getNombreVia());
				preparedStatement.setString(8, datosNotificacion.getNumeroVia());
				preparedStatement.setString(9, datosNotificacion.getPortal());
				preparedStatement.setString(10, datosNotificacion.getPlanta());
				preparedStatement.setString(11, datosNotificacion.getEscalera());
				preparedStatement.setString(12, datosNotificacion.getLetra());
				preparedStatement.setString(13, datosNotificacion.getCpostal());
				preparedStatement.setString(14, datosNotificacion.getMunicipio());
				preparedStatement.setString(15, datosNotificacion.getProvincia());
				preparedStatement.setString(16, datosNotificacion.getNotificar());

				preparedStatement.execute();
                safeClose(null, null, preparedStatement, null);


			}

		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
		}
	}

	/**
	 * INFORMES
	 */
	public static CResultadoOperacion getSolicitudesExpedientesInforme(Hashtable hash, String idMunicipio, Vector tiposLicencia) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vSolicitudes = new Vector();
		Vector vExpedientes = new Vector();

		try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}

			String conditions = getConditions(hash);
			logger.debug(".....................conditions=" + conditions);
			String sql = "select SOLICITUD_LICENCIA.*,PERSONA_JURIDICO_FISICA.*," +
					"EXPEDIENTE_LICENCIA.NUM_EXPEDIENTE as EX_NUMEXP, EXPEDIENTE_LICENCIA.ID_SOLICITUD as EX_IDSOL, " +
					"EXPEDIENTE_LICENCIA.ID_TRAMITACION as EX_IDTRA, EXPEDIENTE_LICENCIA.ID_FINALIZACION as EX_IDFIN, " +
					"EXPEDIENTE_LICENCIA.ID_ESTADO as EX_IDEST, EXPEDIENTE_LICENCIA.SERVICIO_ENCARGADO as EX_SERVICIO, " +
					"EXPEDIENTE_LICENCIA.ASUNTO as EX_ASUNTO, EXPEDIENTE_LICENCIA.SILENCIO_ADMINISTRATIVO as EX_SILENCIO, " +
					"EXPEDIENTE_LICENCIA.FORMA_INICIO as EX_INICIO, EXPEDIENTE_LICENCIA.NUM_FOLIOS as EX_NUMFOLIOS, " +
					"EXPEDIENTE_LICENCIA.FECHA_APERTURA as EX_FECHAAPERTURA, EXPEDIENTE_LICENCIA.RESPONSABLE as EX_RESPONSABLE, " +
					"EXPEDIENTE_LICENCIA.PLAZO_RESOLUCION as EX_PLAZO, EXPEDIENTE_LICENCIA.HABILES as EX_HABILES, " +
					"EXPEDIENTE_LICENCIA.NUM_DIAS as EX_NUMDIAS, EXPEDIENTE_LICENCIA.OBSERVACIONES as EX_OBSERV, " +
					"EXPEDIENTE_LICENCIA.ID_ESTADO as EST_IDEST, " +
					"EXPEDIENTE_LICENCIA.ID_TRAMITACION as TTRA_IDTRA, " +
					"EXPEDIENTE_LICENCIA.ID_FINALIZACION as TFIN_IDFIN, " +
					"TIPO_LICENCIA.ID_TIPO_LICENCIA as TLICEN_IDLICEN, TIPO_LICENCIA.DESCRIPCION as TLICEN_DESC, " +
					"TIPO_LICENCIA.OBSERVACION as TLICEN_OBSERV, " +
					"SOLICITUD_LICENCIA.ID_TIPO_OBRA as TOBRA_IDOBRA " +
					"from solicitud_licencia, expediente_licencia, tipo_licencia, persona_juridico_fisica, estado " +
					"where  EXPEDIENTE_LICENCIA.ID_SOLICITUD=SOLICITUD_LICENCIA.ID_SOLICITUD and " +
					"SOLICITUD_LICENCIA.ID_TIPO_LICENCIA=TIPO_LICENCIA.ID_TIPO_LICENCIA and " +
					"SOLICITUD_LICENCIA.PROPIETARIO=PERSONA_JURIDICO_FISICA.ID_PERSONA and " +
                    "EXPEDIENTE_LICENCIA.ID_ESTADO=ESTADO.ID_ESTADO " +

                    (dameTipos("SOLICITUD_LICENCIA.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("SOLICITUD_LICENCIA.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and SOLICITUD_LICENCIA.id_municipio='" + idMunicipio + "' "):
                     (idMunicipio == null ? "" : " and SOLICITUD_LICENCIA.id_municipio='" + idMunicipio + "' ")) +

					conditions + " order by EXPEDIENTE_LICENCIA.FECHA_APERTURA ASC";

			logger.debug("SQL=" + sql);
			System.out.println("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					// Solicitud
					// Tipo Licencia
					CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("TLICEN_IDLICEN"), "", "");
					// Tipo Obra
					CTipoObra tipoObra = new CTipoObra(rs.getInt("TOBRA_IDOBRA"), "", "");
					// Propietario
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					// Representante
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					// Tecnico
					//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"));
					// Promotor
					//CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"));
					// Anexos
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					// Referencias catastrales
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
							tipoObra,
							propietario,
							null,
							null,
							null,
							rs.getString("NUM_ADMINISTRATIVO"),
							rs.getString("CODIGO_ENTRADA"),
							rs.getString("UNIDAD_TRAMITADORA"),
							rs.getString("UNIDAD_DE_REGISTRO"),
							rs.getString("MOTIVO"),
							rs.getString("ASUNTO"),
							rs.getTimestamp("FECHA"),
							rs.getDouble("TASA"),
							rs.getString("TIPO_VIA_AFECTA"),
							rs.getString("NOMBRE_VIA_AFECTA"),
							rs.getString("NUMERO_VIA_AFECTA"),
							rs.getString("PORTAL_AFECTA"),
							rs.getString("PLANTA_AFECTA"),
							rs.getString("LETRA_AFECTA"),
							rs.getString("CPOSTAL_AFECTA"),
							rs.getString("MUNICIPIO_AFECTA"),
							rs.getString("PROVINCIA_AFECTA"),
							rs.getString("OBSERVACIONES"),
							null,
							null);

					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					vSolicitudes.add(i, solicitud);
					// Expediente

					// Estado
					CEstado estado = new CEstado(rs.getInt("EST_IDEST"), "", "", 0);
					// Tipo Tramitacion
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("TTRA_IDTRA"), "", "", "");
					// Tipo Finalizacion
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("EX_IDFIN")!=null)
                        tipoFinalizacion = new CTipoFinalizacion(rs.getInt("EX_IDFIN"), "", "");

					//Vector notificaciones = getNotificacionesExpediente(rs.getString("EX_NUMEXP"));

					CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("EX_NUMEXP"),
							rs.getLong("EX_IDSOL"),
							tipoTramitacion,
							tipoFinalizacion,
							rs.getString("EX_SERVICIO"),
							rs.getString("EX_ASUNTO"),
							rs.getString("EX_SILENCIO"),
							rs.getString("EX_INICIO"),
							rs.getInt("EX_NUMFOLIOS"),
							rs.getTimestamp("EX_FECHAAPERTURA"),
							rs.getString("EX_RESPONSABLE"),
							rs.getDate("EX_PLAZO"),
							rs.getString("EX_HABILES"),
							rs.getInt("EX_NUMDIAS"),
							rs.getString("EX_OBSERV"),
							estado,
							null);
					vExpedientes.add(i, expediente);
				} while (rs.next());
			} else {
				return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
			}

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitudes);
			resultadoOperacion.setExpedientes(vExpedientes);

			safeClose(rs, statement, connection);
			return resultadoOperacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}

	}


	public static Vector getPlantillas(String aplicacion) {

		try {

			logger.debug("Inicio getPlantillas.");

			Vector vPlantillas = new Vector();

			// It is also possible to filter the list of returned files.

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith(".jrxml"));
				}
			};
            String path = "";
            if (aplicacion != null){
                if (aplicacion.equalsIgnoreCase("ACTIVIDAD"))
                    path = CConstantesPaths.PATH_PLANTILLAS_ACTIVIDAD;
                else if (aplicacion.equalsIgnoreCase("LICENCIAS"))
                    path = CConstantesPaths.PLANTILLAS_PATH;
            }
            /*
            Connection con = CPoolDatabase.getConnection();
            
            String _path = null;
            
            String entorno = null;
            if (CPoolDatabase.isPostgres(con)) {
                entorno = "postgres/";
            }
            else {
                entorno = "oracle/";
            }
            _path = path + entorno;
            
			File dir = new File(_path);
			*/
            File dir = new File(path);
			if (dir.isDirectory()) {
				File[] children = dir.listFiles(filter);
				if (children == null) {
					// Either dir does not exist or is not a directory
				} else {
					/*
                    String _dname = null;
                    String bdContext = null;
                    if (CPoolDatabase.isPostgres(con)) {
                        _dname = "P_";
                        bdContext = "public";
                    }
                    else {
                        _dname = "O_";
                        //bdContext = "GEOPISTA";
                        bdContext = con.getMetaData().getUserName();
                    }
                    */
					for (int i = 0; i < children.length; i++) {
						// Get filename of file or directory
						File file = children[i];
                        // FRAN
                        String fname = file.getName();
                        long ftam = file.length();
                        FileInputStream fis = new FileInputStream(file);
                        byte data[] = new byte[(int)ftam];
                        fis.read(data);
                        String sdata = new String(data);
                      //  String sdef = sdata.replaceAll(CConstantesComando.PATRON_SUSTITUIR_BBDD, bdContext);
                        BufferedWriter bw = new BufferedWriter(new FileWriter(path+fname));
                      //  bw.write(sdef);
                        bw.write(sdata);
                        //FileOutputStream fos = new FileOutputStream(_path+dname);
                        //fos.write(sdef.getBytes());
                        //fos.flush();
                        //fos.close();
                        bw.flush();
                        bw.close();
                        //int pos = sdata.indexOf();
                        // FRAN
						CPlantilla plantilla = new CPlantilla(fname);//file.getName());
						vPlantillas.addElement(plantilla);
					}
				}
			}
			return vPlantillas;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();
		}
	}



	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}


	public static Vector getNotificacionesMenu(Hashtable hash, String idMunicipio, Vector tiposLicencia) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vNotificaciones = new Vector();

		try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}

			String conditions = getConditions(hash);
            String sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
                    "E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.BLOQUEADO AS BLOQUEADO, " +
                    "A.ID_NOTIFICACION, A.ID_ALEGACION, A.ID_MEJORA, A.NOTIFICADA_POR, " +
                    "A.FECHA_CREACION, A.FECHA_NOTIFICACION, A.RESPONSABLE, A.PLAZO_VENCIMIENTO, A.FECHA_REENVIO, A.PROCEDENCIA, " +
                    "A.HABILES, A.NUM_DIAS, A.OBSERVACIONES as A_OBSERVACIONES, A.ENTREGADA_A, A.ID_ESTADO as A_ID_ESTADO, A.ID_TIPO_NOTIFICACION " +
                    "from solicitud_licencia S, expediente_licencia E, " +
                    "persona_juridico_fisica J, " +
                    "notificacion A " +
                    "where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "A.ID_PERSONA=J.ID_PERSONA and S.ID_SOLICITUD=A.ID_SOLICITUD " +
                  //ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad 
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                      " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                     (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' ")) + 

                      conditions + " order by PLAZO_VENCIMIENTO ASC";
			logger.info("sql: -" + sql + "-");
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			int i = 0;
			if (rs.next()) {
				do {
					CTipoNotificacion tipoNotificacion = new CTipoNotificacion();
					tipoNotificacion.setIdTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"));

					CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion();
					estadoNotificacion.setIdEstado(rs.getInt("A_ID_ESTADO"));

					CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"),connection);

					CTipoLicencia tipoLicencia = new CTipoLicencia();
					tipoLicencia.setIdTipolicencia(rs.getInt("ID_TIPO_LICENCIA"));

                    CTipoObra tipoObra= new CTipoObra();
                    tipoObra.setIdTipoObra(rs.getInt("ID_TIPO_OBRA"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia();
					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					solicitud.setTipoLicencia(tipoLicencia);
                    solicitud.setTipoObra(tipoObra);
                    solicitud.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                    solicitud.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                    solicitud.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));

					CEstado estado = new CEstado();
					estado.setIdEstado(rs.getInt("ID_ESTADO"));

					CExpedienteLicencia expediente = new CExpedienteLicencia();
					expediente.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
					expediente.setEstado(estado);
                    expediente.setBloqueado(rs.getString("BLOQUEADO"));

					CNotificacion notificacion = new CNotificacion(rs.getLong("ID_NOTIFICACION"),
							tipoNotificacion,
							rs.getLong("ID_ALEGACION"),
							rs.getLong("ID_MEJORA"),
							persona,
							rs.getInt("NOTIFICADA_POR"),
							rs.getDate("FECHA_CREACION"),
							rs.getDate("FECHA_NOTIFICACION"),
							rs.getString("RESPONSABLE"),
							rs.getDate("PLAZO_VENCIMIENTO"),
							rs.getString("HABILES"),
							rs.getInt("NUM_DIAS"),
							rs.getString("OBSERVACIONES"),
							estadoNotificacion,
							rs.getDate("FECHA_REENVIO"),
							rs.getString("PROCEDENCIA"));

					notificacion.setExpediente(expediente);
					notificacion.setSolicitud(solicitud);
                    notificacion.setEntregadaA(rs.getString("ENTREGADA_A"));
					vNotificaciones.add(i, notificacion);

				} while (rs.next());
			}

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setVector(vNotificaciones);

			safeClose(rs, statement, connection);
			return vNotificaciones;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();
		}

	}

    public static CResultadoOperacion insertarInforme(Informe informe, Hashtable fileUpload)
    {
        System.out.println("Intentando insertar el informe:"+informe.getFichero());
        if (fileUpload==null||fileUpload.size()<=0)
        {
            return new CResultadoOperacion(false, "Fichero no encontrado");
        }
        //Primero grabamos el fichero físico
        CAnexo anexo = (CAnexo) fileUpload.elements().nextElement();
        String fileName = new String(anexo.getFileName());
	    String path = "anexos" + File.separator + "licencias" + File.separator + informe.getIdSolicitud()+ File.separator+ "informes"+ File.separator;
        PreparedStatement ps = null;
		try {
			if (!new File(path).exists()) new File(path).mkdirs();
			FileOutputStream out = new FileOutputStream(path + fileName);
			out.write(anexo.getContent());
			out.close();
			logger.info("Informe created. path + fileName: " + path + fileName);
        } catch (Exception ex) {
			logger.error("Error al grabar el informe: ", ex);
            return new CResultadoOperacion(false,"Error al grabar el informe: "+ex.toString());
        }
        try
        {
                informe.setId(new Long(getTableSequence()).toString());
                Connection connection = null;
                //PreparedStatement ps = null;
                connection = CPoolDatabase.getConnection();
                if (connection == null) {
                    logger.warn("Cannot get connection");
                    return new CResultadoOperacion(false,"Error al obtenera la conexión con la Base de datos");
                }
                String sql="insert into informe_preceptivo (id_informe, id_solicitud,  num_expediente, id_tipo_informe," +
                        "url_fichero, fecha_llegada, observacion, acuerdo)  " +
                        "values (?,?,?,?,?,?,?,?)";
                logger.info(sql);
                System.out.println("Insertando la observación: "+informe.getObservaciones());
                ps = connection.prepareStatement(sql);
                ps.setString(1,informe.getId());
				ps.setLong(2, informe.getIdSolicitud());
				ps.setString(3, informe.getNumExpediente());
                ps.setString(4,informe.getTipo());
                ps.setString(5,fileName);
                ps.setDate(6, new java.sql.Date(informe.getFecha().getTime()));
                ps.setString(7, informe.getObservaciones());
                ps.setString(8, (informe.isFavorable()?"1":"0"));
				ps.execute();
                connection.commit();
                safeClose(null, null, ps, null);
                Vector auxVector= new Vector();
                auxVector.add(informe);
                CResultadoOperacion result = new CResultadoOperacion(true, "Informe insertado con exito");
                result.setVector(auxVector);
                return result;
        } catch (Exception ex) {
                safeClose(null, null, ps, null);
                try{new File(path + fileName).delete();}catch(Exception e){}
                logger.error("Error al insertar el informe en la base de datos: ", ex);
                return new CResultadoOperacion(false,"Error al insertar el informe en la base de datos: "+ex.toString());
        }
    }




	public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(long idPersona, long idSolicitud, Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

			String sql = "select A.*,B.* " +
					"FROM PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B, SOLICITUD_LICENCIA S, VIA_NOTIFICACION V " +
					"where A.ID_PERSONA=" + idPersona + " and A.ID_PERSONA=B.ID_PERSONA and " +
					"B.ID_SOLICITUD=S.ID_SOLICITUD and B.ID_SOLICITUD=" + idSolicitud + " and " +
					"B.ID_VIA_NOTIFICACION=V.ID_VIA_NOTIFICACION";

			logger.debug("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				safeClose(rs, statement, connection);
				return null;
			}

			CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), "");

			CDatosNotificacion datosNotificacion = new CDatosNotificacion(rs.getString("DNI_CIF"),
					viaNotificacion,
					rs.getString("FAX"),
					rs.getString("TELEFONO"),
					rs.getString("MOVIL"),
					rs.getString("EMAIL"),
					rs.getString("TIPO_VIA"),
					rs.getString("NOMBRE_VIA"),
					rs.getString("NUMERO_VIA"),
					rs.getString("PORTAL"),
					rs.getString("PLANTA"),
					rs.getString("ESCALERA"),
					rs.getString("LETRA"),
					rs.getString("CPOSTAL"),
					rs.getString("MUNICIPIO"),
					rs.getString("PROVINCIA"),
					rs.getString("NOTIFICAR"));

			CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), datosNotificacion);
			persona.setIdPersona(rs.getLong("ID_PERSONA"));


			safeClose(rs, statement, null);
			return persona;

		} catch (Exception ex) {

			safeClose(rs, statement, null);
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


    /*********************
     * TECNICOS
     */
    public static Vector getTecnicosFromDatabase(long idSolicitud,Connection connection) {

        //Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {

            logger.debug("Inicio.");
            Vector vTecnicos= new Vector();

            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            //connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }
            String sql= "select A.*,B.*,T.PATRON_PERFIL " +
                    "from PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B, SOLICITUD_LICENCIA S, TECNICOS T " +
                    "where A.ID_PERSONA=T.ID_PERSONA and " +
                    "A.ID_PERSONA=B.ID_PERSONA and " +
                    "S.ID_SOLICITUD=B.ID_SOLICITUD and " +
                    "S.ID_SOLICITUD=T.ID_SOLICITUD and " +
                    "S.ID_SOLICITUD="+idSolicitud;

            logger.info("SQL=" + sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (!rs.next()) {
                safeClose(rs, statement, null);
                return new Vector();
            }
            do{
                CViaNotificacion viaNotificacion = new CViaNotificacion(rs.getInt("ID_VIA_NOTIFICACION"), "");

                CDatosNotificacion datosNotificacion = new CDatosNotificacion(rs.getString("DNI_CIF"),
                        viaNotificacion,
                        rs.getString("FAX"),
                        rs.getString("TELEFONO"),
                        rs.getString("MOVIL"),
                        rs.getString("EMAIL"),
                        rs.getString("TIPO_VIA"),
                        rs.getString("NOMBRE_VIA"),
                        rs.getString("NUMERO_VIA"),
                        rs.getString("PORTAL"),
                        rs.getString("PLANTA"),
                        rs.getString("ESCALERA"),
                        rs.getString("LETRA"),
                        rs.getString("CPOSTAL"),
                        rs.getString("MUNICIPIO"),
                        rs.getString("PROVINCIA"),
                        rs.getString("NOTIFICAR"));

                CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), datosNotificacion);
                persona.setIdPersona(rs.getLong("ID_PERSONA"));
                persona.setPatronPerfil(rs.getString("PATRON_PERFIL"));
                vTecnicos.add(persona);
            }while (rs.next());

            safeClose(rs, statement, null);
            return vTecnicos;

        } catch (Exception ex) {

            safeClose(rs, statement, null);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();
        }

    }


	/**
	 * EVENTOS
	 */
	public static CResultadoOperacion getEventos(Hashtable hash, String idMunicipio, Vector tiposLicencia, String locale) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vSolicitudes = new Vector();
		Vector vExpedientes = new Vector();
		Vector vEventos = new Vector();

		try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}

			String conditions = getConditions(hash);
			String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.BLOQUEADO AS BLOQUEADO, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, "+
                    "persona_juridico_fisica J, " +
                	//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    " E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE "+
                  //ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad 
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                     (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

               		conditions + " order by V_FECHA ASC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.BLOQUEADO AS BLOQUEADO, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, "+
                    "persona_juridico_fisica J, " +
                	//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    " E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE "+
                  //ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad 
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                     (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

               		conditions + " order by V_FECHA ASC";
            }
			logger.info("SQL=" + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Tipo Licencia */
					CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), "", "");
					/** Tipo Obra */
					CTipoObra tipoObra = new CTipoObra(rs.getInt("ID_TIPO_OBRA"), "", "");
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Tecnico */
					//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"));
					/** Promotor */
					//CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
							tipoObra,
							propietario,
							null,
							null,
							null,
							rs.getString("NUM_ADMINISTRATIVO"),
							rs.getString("CODIGO_ENTRADA"),
							rs.getString("UNIDAD_TRAMITADORA"),
							rs.getString("UNIDAD_DE_REGISTRO"),
							rs.getString("MOTIVO"),
							rs.getString("ASUNTO"),
							rs.getTimestamp("FECHA"),
							rs.getDouble("TASA"),
							rs.getString("TIPO_VIA_AFECTA"),
							rs.getString("NOMBRE_VIA_AFECTA"),
							rs.getString("NUMERO_VIA_AFECTA"),
							rs.getString("PORTAL_AFECTA"),
							rs.getString("PLANTA_AFECTA"),
							rs.getString("LETRA_AFECTA"),
							rs.getString("CPOSTAL_AFECTA"),
							rs.getString("MUNICIPIO_AFECTA"),
							rs.getString("PROVINCIA_AFECTA"),
							rs.getString("OBSERVACIONES"),
							null,
							null);

					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                           tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");

					//Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));
                     CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
							rs.getLong("ID_SOLICITUD"),
							tipoTramitacion,
							tipoFinalizacion,
							rs.getString("E_SERVICIO_ENCARGADO"),
							rs.getString("E_ASUNTO"),
							rs.getString("E_SILENCIO_ADMINISTRATIVO"),
							rs.getString("E_FORMA_INICIO"),
							rs.getInt("E_NUM_FOLIOS"),
							rs.getDate("FECHA_APERTURA"),
							rs.getString("E_RESPONSABLE"),
							rs.getDate("E_PLAZO_RESOLUCION"),
							rs.getString("E_HABILES"),
							rs.getInt("E_NUM_DIAS"),
							rs.getString("E_OBSERVACIONES"),
							estado,
							null);
                    expediente.setBloqueado(rs.getString("BLOQUEADO"));
					vExpedientes.add(i, expediente);

					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
                    CEstado e= new CEstado();
                    e.setIdEstado(rs.getInt("V_ID_ESTADO"));
                    evento.setEstado(e);
                    //evento.setContent(rs.getString("CONTENT"));
                    /** Recogemos el content del evento de la tabla dictionary (Multilenguaje) */
                    //evento.setContent(getTraduccion(connection, rs.getString("CONTENT"), locale));
                    evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));

					vEventos.add(i, evento);
				} while (rs.next());
			} else {
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
			}
			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitudes);
			resultadoOperacion.setExpedientes(vExpedientes);
			resultadoOperacion.setVector(vEventos);
			safeClose(rs, statement, connection);
			return resultadoOperacion;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al recoger los eventos: " , ex);
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}
	}


	public static CResultadoOperacion getUltimosEventos(Hashtable hash, String idMunicipio, Vector tiposLicencia, String locale) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vSolicitudes = new Vector();
		Vector vExpedientes = new Vector();
		Vector vEventos = new Vector();

		try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}

			String conditions = getConditions(hash);
			System.out.println(".....................conditions=" + conditions);
			String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"S.ID_TIPO_OBRA AS TIPO_OBRA_ID_TIPO_OBRA, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, persona_juridico_fisica J, " +
                    //"eventos V " +
    				"eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +

                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

       				conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"S.ID_TIPO_OBRA AS TIPO_OBRA_ID_TIPO_OBRA, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, persona_juridico_fisica J, " +
                    //"eventos V " +
    				"eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +

                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                        " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

       				conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Tipo Licencia */
					CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), "", "");
					/** Tipo Obra */
					CTipoObra tipoObra = new CTipoObra(rs.getInt("ID_TIPO_OBRA"), "", "");
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Tecnico */
					//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"));
					/** Promotor */
					//CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
							tipoObra,
							propietario,
							null,
							null,
							null,
							rs.getString("NUM_ADMINISTRATIVO"),
							rs.getString("CODIGO_ENTRADA"),
							rs.getString("UNIDAD_TRAMITADORA"),
							rs.getString("UNIDAD_DE_REGISTRO"),
							rs.getString("MOTIVO"),
							rs.getString("ASUNTO"),
							rs.getTimestamp("FECHA"),
							rs.getDouble("TASA"),
							rs.getString("TIPO_VIA_AFECTA"),
							rs.getString("NOMBRE_VIA_AFECTA"),
							rs.getString("NUMERO_VIA_AFECTA"),
							rs.getString("PORTAL_AFECTA"),
							rs.getString("PLANTA_AFECTA"),
							rs.getString("LETRA_AFECTA"),
							rs.getString("CPOSTAL_AFECTA"),
							rs.getString("MUNICIPIO_AFECTA"),
							rs.getString("PROVINCIA_AFECTA"),
							rs.getString("OBSERVACIONES"),
							null,
							null);

					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                           tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");

					//Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

					CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
							rs.getLong("ID_SOLICITUD"),
							tipoTramitacion,
							tipoFinalizacion,
							rs.getString("E_SERVICIO_ENCARGADO"),
							rs.getString("E_ASUNTO"),
							rs.getString("E_SILENCIO_ADMINISTRATIVO"),
							rs.getString("E_FORMA_INICIO"),
							rs.getInt("E_NUM_FOLIOS"),
							rs.getDate("FECHA_APERTURA"),
							rs.getString("E_RESPONSABLE"),
							rs.getDate("E_PLAZO_RESOLUCION"),
							rs.getString("E_HABILES"),
							rs.getInt("E_NUM_DIAS"),
							rs.getString("E_OBSERVACIONES"),
							estado,
							null);
					vExpedientes.add(i, expediente);

					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
                    CEstado e= new CEstado();
                    e.setIdEstado(rs.getInt("V_ID_ESTADO"));
                    evento.setEstado(e);
                    //evento.setContent(rs.getString("CONTENT"));
                    /** Recogemos el content del evento de la tabla dictionary (Multilenguaje) */
                    //evento.setContent(getTraduccion(connection, rs.getString("CONTENT"), locale));
                    evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));

					vEventos.add(i, evento);

				} while (rs.next());
			} else {
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
			}

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitudes);
			resultadoOperacion.setExpedientes(vExpedientes);
			resultadoOperacion.setVector(vEventos);
			safeClose(rs, statement, connection);
			return resultadoOperacion;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception error al cargar los eventos: " , ex);
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}
	}


	public static CResultadoOperacion getEventosSinRevisar(Hashtable hash, String idMunicipio, Vector tiposLicencia, String locale)
    {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vSolicitudes = new Vector();
		Vector vExpedientes = new Vector();
		Vector vEventos = new Vector();
    	try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}

			String conditions = getConditions(hash);
			System.out.println(".....................conditions=" + conditions);
			String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E,  persona_juridico_fisica J, " +
					//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
					"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' "+
					//ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO AS V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E,  persona_juridico_fisica J, " +
					//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
					"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' "+
					//ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }
    		logger.info("SQL=" + sql);
    		statement = connection.createStatement();
			rs = statement.executeQuery(sql);
    		int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Tipo Licencia */
					CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("ID_TIPO_LICENCIA"), "", "");
					/** Tipo Obra */
					CTipoObra tipoObra = new CTipoObra(rs.getInt("ID_TIPO_OBRA"), "", "");
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Tecnico */
					//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"));
					/** Promotor */
					//CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
							tipoObra,
							propietario,
							null,
							null,
							null,
							rs.getString("NUM_ADMINISTRATIVO"),
							rs.getString("CODIGO_ENTRADA"),
							rs.getString("UNIDAD_TRAMITADORA"),
							rs.getString("UNIDAD_DE_REGISTRO"),
							rs.getString("MOTIVO"),
							rs.getString("ASUNTO"),
							rs.getTimestamp("FECHA"),
							rs.getDouble("TASA"),
							rs.getString("TIPO_VIA_AFECTA"),
							rs.getString("NOMBRE_VIA_AFECTA"),
							rs.getString("NUMERO_VIA_AFECTA"),
							rs.getString("PORTAL_AFECTA"),
							rs.getString("PLANTA_AFECTA"),
							rs.getString("LETRA_AFECTA"),
							rs.getString("CPOSTAL_AFECTA"),
							rs.getString("MUNICIPIO_AFECTA"),
							rs.getString("PROVINCIA_AFECTA"),
							rs.getString("OBSERVACIONES"),
							null,
							null);
					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                           tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");

					//Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

					CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
							rs.getLong("ID_SOLICITUD"),
							tipoTramitacion,
							tipoFinalizacion,
							rs.getString("E_SERVICIO_ENCARGADO"),
							rs.getString("E_ASUNTO"),
							rs.getString("E_SILENCIO_ADMINISTRATIVO"),
							rs.getString("E_FORMA_INICIO"),
							rs.getInt("E_NUM_FOLIOS"),
							rs.getDate("FECHA_APERTURA"),
							rs.getString("E_RESPONSABLE"),
							rs.getDate("E_PLAZO_RESOLUCION"),
							rs.getString("E_HABILES"),
							rs.getInt("E_NUM_DIAS"),
							rs.getString("E_OBSERVACIONES"),
							estado,
							null);
					vExpedientes.add(i, expediente);
					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
                    //evento.setContent(rs.getString("CONTENT"));
                    /** Recogemos el content del evento de la tabla dictionary (Multilenguaje) */
                    //evento.setContent(getTraduccion(connection, rs.getString("CONTENT"), locale));
                    evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));
                    CEstado e= new CEstado();
                    e.setIdEstado(rs.getInt("V_ID_ESTADO"));
                    evento.setEstado(e);
					vEventos.add(i, evento);
				} while (rs.next());
			} else {
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
			}

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitudes);
			resultadoOperacion.setExpedientes(vExpedientes);
			resultadoOperacion.setVector(vEventos);
			safeClose(rs, statement, connection);
			return resultadoOperacion;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception al recoger los eventos sin revisar: ", ex);
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}
	}


	private static Vector getEventosExpediente(String numExpediente, String locale, Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}

			String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "SELECT ID_EVENTO, NUM_EXPEDIENTE, REVISADO, REVISADO_POR, CONTENT, FECHA, DICTIONARY.TRADUCCION " +
					//"FROM EVENTOS " +
                    "from EVENTOS LEFT JOIN DICTIONARY ON EVENTOS.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where NUM_EXPEDIENTE='" + numExpediente + "' " +
                    "ORDER BY FECHA DESC";
            }
            else {
                sql = "SELECT ID_EVENTO, NUM_EXPEDIENTE, REVISADO, REVISADO_POR, CONTENT, FECHA, DICTIONARY.TRADUCCION " +
					//"FROM EVENTOS " +
                    "from EVENTOS LEFT JOIN DICTIONARY ON EVENTOS.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where NUM_EXPEDIENTE='" + numExpediente + "' " +
                    "ORDER BY FECHA DESC";
            }

			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector eventos = new Vector();
			while (rs.next()) {

				CExpedienteLicencia expediente = new CExpedienteLicencia();
				expediente.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
				CEvento evento = new CEvento();
				evento.setIdEvento(rs.getLong("ID_EVENTO"));
				evento.setRevisado(rs.getString("REVISADO"));
				evento.setRevisadoPor(rs.getString("REVISADO_POR"));
				evento.setTipoEvento("FALTA_TABLA_EN_BD");
				evento.setFechaEvento(rs.getTimestamp("FECHA"));
				//evento.setContent(rs.getString("CONTENT"));
                /** Recogemos el content del evento de la tabla dictionary (Multilenguaje) */
                //evento.setContent(getTraduccion(connection, rs.getString("CONTENT"), locale));
                evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));
				evento.setExpediente(expediente);
				/*
				System.out.println("FECHA="+ evento.getFechaEvento().toString());
				System.out.println("SELECTED="+evento.getNumExpediente());
				System.out.println("REVISADO="+evento.getRevisado());
				System.out.println("REVISADO_POR="+evento.getRevisadoPor());
				System.out.println("CONTENT="+evento.getContent());
				*/
				eventos.add(evento);

			}

			safeClose(rs, statement,null);

			logger.debug("eventos: " + eventos);

			return eventos;

		} catch (Exception ex) {

			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}

	/**
	 * HISTORICO
	 */

	public static CResultadoOperacion getHistorico(Hashtable hash, String idMunicipio, Vector tiposLicencia, String locale) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Vector vSolicitudes = new Vector();
		Vector vExpedientes = new Vector();
		Vector vHistorico = new Vector();

		try {
			//*******************************************
			//** Obtener una conexion de la base de datos
			//*******************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}

			String conditions = getConditions(hash);
			System.out.println(".....................conditions=" + conditions);
            String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.BLOQUEADO AS BLOQUEADO, " +
					"E.ID_ESTADO AS ID_ESTADO , "+
					"E.ID_TRAMITACION AS ID_TRAMITACION, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION,  " +
					"S.ID_TIPO_LICENCIA AS L_ID_TIPO_LICENCIA, " +
					"S.ID_TIPO_OBRA AS TIPO_OBRA_ID_TIPO_OBRA, " +
					"H.ID_HISTORICO, H.ID_ESTADO AS H_ID_ESTADO, H.FECHA AS H_FECHA, " +
					"H.USUARIO AS H_USUARIO, H.APUNTE AS H_APUNTE, H.SISTEMA AS H_SISTEMA " +
                    //"DICTIONARY.TRADUCCION, DICTIONARY.ID_VOCABLO " +
					"from solicitud_licencia S, expediente_licencia E,  persona_juridico_fisica J, " +
					//"historico H " +
                    "historico H "+//LEFT JOIN DICTIONARY ON H.APUNTE=DICTIONARY.ID_VOCABLO " +
                    //"and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=H.ID_SOLICITUD and " +
					"E.NUM_EXPEDIENTE=H.NUM_EXPEDIENTE "+
					//ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

					conditions + " order by H_FECHA ASC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.BLOQUEADO AS BLOQUEADO, " +
					"E.ID_ESTADO AS ID_ESTADO , "+
					"E.ID_TRAMITACION AS ID_TRAMITACION, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION,  " +
					"S.ID_TIPO_LICENCIA AS L_ID_TIPO_LICENCIA, " +
					"S.ID_TIPO_OBRA AS TIPO_OBRA_ID_TIPO_OBRA, " +
					"H.ID_HISTORICO, H.ID_ESTADO AS H_ID_ESTADO, H.FECHA AS H_FECHA, " +
					"H.USUARIO AS H_USUARIO, H.APUNTE AS H_APUNTE, H.SISTEMA AS H_SISTEMA " +
                    //"DICTIONARY.TRADUCCION, DICTIONARY.ID_VOCABLO " +
					"from solicitud_licencia S, expediente_licencia E,  persona_juridico_fisica J, " +
			        "historico H "+// LEFT JOIN DICTIONARY ON H.APUNTE=to_char(DICTIONARY.ID_VOCABLO) " +
                    //"and DICTIONARY.LOCALE='"+locale+"' " +
					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=H.ID_SOLICITUD and " +
					"E.NUM_EXPEDIENTE=H.NUM_EXPEDIENTE "+
					//ISTC680847   -   No se listan los eventos en la GESTION DE EVENTOS en Lic. de Obra y Actividad
                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and S.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and S.id_municipio='" + idMunicipio + "' ")) +

					conditions + " order by H_FECHA ASC";
            }
			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
            logger.info("Sentencia ejecutada corretamente");

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Tipo Licencia */
					CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("L_ID_TIPO_LICENCIA"), "", "");
					/** Tipo Obra */
					CTipoObra tipoObra = new CTipoObra(rs.getInt("TIPO_OBRA_ID_TIPO_OBRA"), "", "");
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Tecnico */
					//CPersonaJuridicoFisica tecnico = getPersonaJuridicaFromDatabase(rs.getLong("TECNICO"));
					/** Promotor */
					//CPersonaJuridicoFisica promotor = getPersonaJuridicaFromDatabase(rs.getLong("PROMOTOR"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
							tipoObra,
							propietario,
							null,
							null,
							null,
							rs.getString("NUM_ADMINISTRATIVO"),
							rs.getString("CODIGO_ENTRADA"),
							rs.getString("UNIDAD_TRAMITADORA"),
							rs.getString("UNIDAD_DE_REGISTRO"),
							rs.getString("MOTIVO"),
							rs.getString("ASUNTO"),
							rs.getTimestamp("FECHA"),
							rs.getDouble("TASA"),
							rs.getString("TIPO_VIA_AFECTA"),
							rs.getString("NOMBRE_VIA_AFECTA"),
							rs.getString("NUMERO_VIA_AFECTA"),
							rs.getString("PORTAL_AFECTA"),
							rs.getString("PLANTA_AFECTA"),
							rs.getString("LETRA_AFECTA"),
							rs.getString("CPOSTAL_AFECTA"),
							rs.getString("MUNICIPIO_AFECTA"),
							rs.getString("PROVINCIA_AFECTA"),
							rs.getString("OBSERVACIONES"),
							null,
							null);

					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "","", 0);
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "","", "");
					/** Tipo Finalizacion */
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                           tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");
					//Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

					CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
							rs.getLong("ID_SOLICITUD"),
							tipoTramitacion,
							tipoFinalizacion,
							rs.getString("E_SERVICIO_ENCARGADO"),
							rs.getString("E_ASUNTO"),
							rs.getString("E_SILENCIO_ADMINISTRATIVO"),
							rs.getString("E_FORMA_INICIO"),
							rs.getInt("E_NUM_FOLIOS"),
							rs.getDate("FECHA_APERTURA"),
							rs.getString("E_RESPONSABLE"),
							rs.getDate("E_PLAZO_RESOLUCION"),
							rs.getString("E_HABILES"),
							rs.getInt("E_NUM_DIAS"),
							rs.getString("E_OBSERVACIONES"),
							estado,
							null);
                    expediente.setBloqueado(rs.getString("BLOQUEADO"));
					vExpedientes.add(i, expediente);

					CHistorico historico = new CHistorico();
					historico.setIdHistorico(rs.getLong("ID_HISTORICO"));
					historico.setUsuario(rs.getString("H_USUARIO"));
					historico.setFechaHistorico(rs.getTimestamp("H_FECHA"));
                    if (rs.getString("H_SISTEMA").equalsIgnoreCase("1")){
                        // historico.setApunte((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
                        historico.setApunte(getTraduccion(connection,rs.getString("H_APUNTE"),locale));
                    }else{
                        historico.setApunte(rs.getString("H_APUNTE"));
                    }
					historico.setSistema(rs.getString("H_SISTEMA"));
					historico.setEstado(getEstado(rs.getInt("H_ID_ESTADO")));

                    try
                    { if (rs.getString("H_APUNTE").equalsIgnoreCase(CWorkflowLiterales.HISTORICO_GENERICO))
                        historico.setGenerico(1);
                    }catch(Exception ex){}

					vHistorico.add(i, historico);

				} while (rs.next());
			} else {
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
			}

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitudes);
			resultadoOperacion.setExpedientes(vExpedientes);
			resultadoOperacion.setVector(vHistorico);
            logger.info("Resultado devuelto");
			safeClose(rs, statement, connection);
			return resultadoOperacion;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());
		}
		finally{
			safeClose(rs, statement, connection);
		}

	}

    private static String getTraduccion(Connection conn, String id_vocablo, String locale){
        ResultSet rs=null;
        PreparedStatement ps=null;
        String traduccion= "";
        try{
            ps=conn.prepareStatement("select traduccion from dictionary where locale='"
            +locale+"' and id_vocablo="+id_vocablo);
            rs=ps.executeQuery();
            if (rs.next()){
                traduccion= rs.getString("traduccion");
            }

            safeClose(rs,ps, null);
            return traduccion;
        }
        catch(Exception e){
            safeClose(rs,ps, null);
            return traduccion;
        }
    }
    
	public static CEstado getEstado(int idEstado) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return null;
			}

			String sql = "select * from estado where id_estado=" + idEstado;
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			CEstado estado = new CEstado();
			if (rs.next()) {
				estado.setIdEstado(idEstado);
				estado.setObservacion(rs.getString("OBSERVACION"));
				estado.setDescripcion(rs.getString("DESCRIPCION"));
				estado.setStep(rs.getInt("STEP"));
			}

			/*rs.close();
			statement.close();
			connection.close();*/
			connection.commit();
			safeClose(rs, statement, connection);


			return estado;

		} catch (Exception ex) {
			/*try {
				rs.close();
			} catch (Exception ex2) {
			}
			try {
				statement.close();
			} catch (Exception ex2) {
			}
			try {
				connection.close();
			} catch (Exception ex2) {
			}*/
			safeClose(rs, statement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CEstado();

		}

	}

	private static Vector getHistoricoExpediente(String numExpediente, String locale, Connection connection) {

		//Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}

            String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
			    sql = "SELECT * FROM HISTORICO LEFT JOIN DICTIONARY ON DICTIONARY.ID_VOCABLO::text = substring(HISTORICO.apunte FROM '([0-9]*)')" +
                	"and DICTIONARY.LOCALE='"+locale+"' " +
                	"where NUM_EXPEDIENTE='" + numExpediente + "' " +
                	"ORDER BY FECHA DESC";
            }
            else {
                sql = "SELECT * FROM HISTORICO LEFT JOIN DICTIONARY ON" +
                		" ( (to_char(DICTIONARY.ID_VOCABLO) = substring(HISTORICO.APUNTE from 0 for position('{' in HISTORICO.APUNTE))) " +
                		" OR (to_char(DICTIONARY.id_vocablo)=HISTORICO.apunte) ) " + 
                        "and DICTIONARY.LOCALE='"+locale+"'  where NUM_EXPEDIENTE='" + numExpediente + "' " +
                        "ORDER BY FECHA DESC";
            }
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector historicos = new Vector();
			while (rs.next()) {

				CEstado estado = getEstado(rs.getInt("ID_ESTADO"));
				CSolicitudLicencia solicitud = new CSolicitudLicencia();
				solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
				CExpedienteLicencia expediente = new CExpedienteLicencia();
				expediente.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));

				CHistorico historico = new CHistorico();
				historico.setIdHistorico(rs.getLong("ID_HISTORICO"));
				historico.setUsuario(rs.getString("USUARIO"));
				historico.setFechaHistorico(rs.getTimestamp("FECHA"));
				if (rs.getString("SISTEMA").equalsIgnoreCase("2")){
					String apunte;
                	if (rs.getString("TRADUCCION") != null && rs.getString("APUNTE").indexOf('{') != -1){
                		String origApunte  = rs.getString("APUNTE");
                		String remainingValues = origApunte.substring(origApunte.indexOf('{'), origApunte.length());
                		int nextValueFirstIndex;
                		ArrayList valuesToInsert = new ArrayList();
                		while ((nextValueFirstIndex = remainingValues.indexOf('{')) != -1){
                			int nextValueLastIndex = remainingValues.indexOf('}', nextValueFirstIndex); 
                			String value = remainingValues.substring(nextValueFirstIndex+1, nextValueLastIndex);
                			remainingValues = remainingValues.substring(nextValueLastIndex);
                			valuesToInsert.add(value);
                		}
                		
                		apunte = rs.getString("TRADUCCION");
                		for (int i = 0; i < valuesToInsert.size(); i++){
                			apunte = apunte.replaceFirst("\\{[0-9]\\}",
                					(String) valuesToInsert.get(i));
                		}
                		
                		historico.setApunte(apunte);
                	}
                	else {
                		historico.setApunte(rs.getString("APUNTE"));
                	}
				}
				else if (rs.getString("SISTEMA").equalsIgnoreCase("1")){
                	historico.setApunte((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));                	
                }else{
				    historico.setApunte(rs.getString("APUNTE"));
                }
				historico.setSistema(rs.getString("SISTEMA"));

                if (new Long(rs.getLong("ID_VOCABLO")).toString().equalsIgnoreCase(CWorkflowLiterales.HISTORICO_GENERICO)){
                    historico.setGenerico(1);
                }

				historico.setEstado(estado);
				historico.setSolicitud(solicitud);
				historico.setExpediente(expediente);
				historicos.add(historico);
			}

			safeClose(rs, statement, null);

			return historicos;

		} catch (Exception ex) {

			safeClose(rs, statement, null);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
	}


	public static boolean insertarHistorico(CHistorico historico, Principal userPrincipal) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			long secuencia = getTableSequence();
			preparedStatement = connection.prepareStatement("INSERT INTO HISTORICO(ID_HISTORICO,ID_SOLICITUD,NUM_EXPEDIENTE,ID_ESTADO,FECHA,USUARIO,APUNTE, SISTEMA) VALUES (?,?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, historico.getSolicitud().getIdSolicitud());
			preparedStatement.setString(3, historico.getExpediente().getNumExpediente());
			preparedStatement.setInt(4, historico.getExpediente().getEstado().getIdEstado());
			preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
			preparedStatement.setString(6, userPrincipal.getName());
			preparedStatement.setString(7, historico.getApunte());
			preparedStatement.setString(8, new Integer(historico.getSistema()).toString());

			preparedStatement.execute();
			logger.debug("*******************INSERTAMOS HISTORICO CON ID=" + secuencia);

			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}

	public static boolean borrarHistorico(CHistorico historico) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			preparedStatement = connection.prepareStatement("DELETE FROM HISTORICO WHERE ID_HISTORICO=?");

			preparedStatement.setLong(1, historico.getIdHistorico());
			preparedStatement.execute();
			logger.debug("*******************BORRAMOS NOTIFICACION CON ID=" + historico.getIdHistorico());
			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}
	}


	//************************************************************************
	//** Métodos de ventanilla única
	//***************************************************************************
	public static boolean insertHitoTelematico(CExpedienteLicencia expedienteLicencia, int idTipoLicencia) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");

            //****************************************************
            //** Obtener una conexion de la base de datos GeoPISTA
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return false;
            }

            CWorkflowLine workflowLine = getWorkflowLine(connection,expedienteLicencia.getEstado().getIdEstado(),idTipoLicencia);
            String descripcion = workflowLine.getHitoText();
            safeClose(null, null, connection);

			//***************************************************************
			//** Obtener una conexion de la base de datos de Ventanilla Unica
			//***************************************************************
			connection = CPoolDatabase.getConnnection("teletramitacion");
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}
			connection.setAutoCommit(false);

			String sql = "INSERT INTO PVUCTHITOESTEXP ( CNUMEXP , CGUID, CFECHA, CDESCR) VALUES ( ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(2, ""+getTableSequence());
			preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			preparedStatement.setString(4, descripcion);
			preparedStatement.execute();
			connection.commit();
			logger.info("Hito telemático insertado.");

			connection.setAutoCommit(true);

			safeClose(rs, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, preparedStatement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}

	}


	public static boolean inicializarConsultaTelematica(CExpedienteLicencia expedienteLicencia, CSolicitudLicencia solicitudLicencia) {

		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnnection("teletramitacion");
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}

			connection.setAutoCommit(false);

			String sql = "INSERT INTO PVUCTINFOEXP ( CNUM , CPROC, CFHINICIO, CNUMREGINI, CFHREGINI) VALUES ( ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(2, "Teletramitación. Expediente registrado.");
			preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
            preparedStatement.setString(4, "1");
			preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

			sql = "INSERT INTO PVUCTINTEXP ( CNUMEXP , CNIF, CPRINCIPAL) VALUES ( ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(2, solicitudLicencia.getPropietario().getDniCif());
			preparedStatement.setString(3, "1");
			preparedStatement.execute();

			connection.commit();
			connection.setAutoCommit(true);

			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}


	}

	public static boolean changeEstadoTelematico(CSolicitudLicencia solicitudLicencia, int estado) {

		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnnection("teletramitacion");
			if (connection == null) {
				logger.warn("Cannot get connection");
				return false;
			}


			preparedStatement = connection.prepareStatement("update PVURTINFOREG set CESTADO=? WHERE CNUMERO=?");
			preparedStatement.setInt(1, estado);
			preparedStatement.setString(2, solicitudLicencia.getNumAdministrativo());
			preparedStatement.execute();

            connection.commit();
			safeClose(rs, statement, preparedStatement, connection);
			return true;

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}

	}

	public static long insertarNotifTelematico(String numExp, String nifPropietario, String cnom, String ctitulo, String ctitulode, String ctituloar, String crutaacc, String sHito, String cproc) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			logger.debug("Inicio.");


			//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnnection("teletramitacion");
			if (connection == null) {
				logger.warn("Cannot get connection");
				return 0;
			}

			connection.setAutoCommit(false);

			String sql = "INSERT INTO PVUNTINFONOTIF (CGUID, CNIFDEST, CNUMREG, CFHREG, CNUMEXP, CPROC, CINFOAUX, CAVISADA, CENTREGADA, CDIRCE, CASUNTOMCE, CTEXTOMCE, CTITULO, CTITULODE, CTITULOAR) VALUES ( ?, ?, ?, ?, ?, ?, ?, 0, 'N', ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);

			long secuencia = getTableSequence();
			String cnumexp = "";
			if (numExp == null)
				cnumexp = "ET_" + secuencia;
			else
				cnumexp = numExp;

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setString(2, nifPropietario);
			preparedStatement.setLong(3, secuencia);
			preparedStatement.setTimestamp(4, new Timestamp(new Date().getTime()));
			preparedStatement.setString(5, cnumexp);
			//preparedStatement.setString(6, "CPROC");
            preparedStatement.setString(6, cproc);
			preparedStatement.setString(7, cnom);
			preparedStatement.setString(8, "CDIRCE");
			preparedStatement.setString(9, "CASUNTOMCE");
			preparedStatement.setString(10, "CTEXTOMCE");
			preparedStatement.setString(11, ctitulo);
			preparedStatement.setString(12, ctitulode + "_" + cnumexp);
			preparedStatement.setString(13, ctituloar);

			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

			sql = "INSERT INTO PVUNTFICHNOTIF (CGUID, CGUIDNOTIF, CTITULO) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, secuencia);
			preparedStatement.setString(3, ctitulo + "_" + cnumexp);

			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

			sql = "INSERT INTO PVUAFINFOFICH (CGUID, CRUTAACC, CNOM, CFLAGS) VALUES (?, ?, ?, 0)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, secuencia);
			preparedStatement.setString(2, crutaacc);
			preparedStatement.setString(3, cnom);

			preparedStatement.execute();

			logger.info("Notificacion insertada.");

            connection.commit();
			connection.setAutoCommit(true);
			safeClose(rs, preparedStatement, connection);
			return secuencia;

		} catch (Exception ex) {

			safeClose(rs, preparedStatement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return 0;

		}

	}


    /**
     * Notificaciones Pendientes
     */
    public static Vector getNotificacionesPendientes(String idMunicipio, Vector tiposLicencia) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Vector vNotificaciones = new Vector();

        try {
            //*******************************************
            //** Obtener una conexion de la base de datos
            //*******************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }

            String sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
                    "E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.ID_ESTADO as ID_ESTADO, " +
                    "A.ID_NOTIFICACION, A.ID_ALEGACION, A.ID_MEJORA, A.NOTIFICADA_POR, A.ENTREGADA_A, " +
                    "A.FECHA_CREACION, A.FECHA_NOTIFICACION, A.RESPONSABLE, A.PLAZO_VENCIMIENTO, A.FECHA_REENVIO, A.PROCEDENCIA, " +
                    "A.HABILES, A.NUM_DIAS, A.OBSERVACIONES as A_OBSERVACIONES, A.ID_ESTADO as A_ID_ESTADO, A.ID_TIPO_NOTIFICACION as A_ID_TIPO_NOTIFICACION " +
                    "from solicitud_licencia S, expediente_licencia E, tipo_licencia L, " +
                    "persona_juridico_fisica J, " +
                    "notificacion A " +
                    "where" +
                    //14-04-2005 ASO añade la condicion de que el estado del expediente no sea durmiente
                    " E.ID_ESTADO !="+ CConstantesComando.ESTADO_DURMIENTE + " and "+
                    " E.ID_SOLICITUD=S.ID_SOLICITUD and S.ID_TIPO_LICENCIA=L.ID_TIPO_LICENCIA and " +
                    "A.ID_PERSONA=J.ID_PERSONA and S.ID_SOLICITUD=A.ID_SOLICITUD and " +
                    "A.ID_ESTADO != " + CConstantesComando.CMD_NOTIFICACION_NOTIFICADA +

                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                      " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and s.id_municipio='" + idMunicipio + "' "):
                       (idMunicipio == null ? "" : " and s.id_municipio='" + idMunicipio + "' ")) +

                    " order by PLAZO_VENCIMIENTO DESC";


            logger.info("sql: -" + sql + "-");

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            int i = 0;
            if (rs.next()) {
                do {
                    /** Tipo Licencia */
                    CTipoLicencia tipoLicencia= new CTipoLicencia();
                    tipoLicencia.setIdTipolicencia(rs.getInt("ID_TIPO_LICENCIA"));

                    CTipoNotificacion tipoNotificacion = new CTipoNotificacion();
                    tipoNotificacion.setIdTipoNotificacion(rs.getInt("A_ID_TIPO_NOTIFICACION"));

                    CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion();
                    estadoNotificacion.setIdEstado(rs.getInt("A_ID_ESTADO"));

                    CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"),connection);

                    CSolicitudLicencia solicitud = new CSolicitudLicencia();
                    solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
                    solicitud.setTipoLicencia(tipoLicencia);
                    solicitud.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                    solicitud.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                    solicitud.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));                    

                    CEstado estado = new CEstado();
                    estado.setIdEstado(rs.getInt("ID_ESTADO"));

                    CExpedienteLicencia expediente = new CExpedienteLicencia();
                    expediente.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    expediente.setEstado(estado);

                    CNotificacion notificacion = new CNotificacion(rs.getLong("ID_NOTIFICACION"),
                            tipoNotificacion,
                            rs.getLong("ID_ALEGACION"),
                            rs.getLong("ID_MEJORA"),
                            persona,
                            rs.getInt("NOTIFICADA_POR"),
                            rs.getDate("FECHA_CREACION"),
                            rs.getDate("FECHA_NOTIFICACION"),
                            rs.getString("RESPONSABLE"),
                            rs.getDate("PLAZO_VENCIMIENTO"),
                            rs.getString("HABILES"),
                            rs.getInt("NUM_DIAS"),
                            rs.getString("OBSERVACIONES"),
                            estadoNotificacion,
                            rs.getDate("FECHA_REENVIO"),
                            rs.getString("PROCEDENCIA"));

                    notificacion.setEntregadaA(rs.getString("ENTREGADA_A"));

                    notificacion.setExpediente(expediente);
                    notificacion.setSolicitud(solicitud);
                    vNotificaciones.add(i, notificacion);

                } while (rs.next());
            }

            CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
            resultadoOperacion.setVector(vNotificaciones);

            safeClose(rs, statement, connection);
            return vNotificaciones;

        } catch (Exception ex) {

            safeClose(rs, statement, connection);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();
        }

    }

    /**
     * Eventos Pendientes
     */

    public static CResultadoOperacion getEventosPendientes(String idMunicipio, Enumeration userPerms,
                                                           Vector tiposLicencia, String locale) {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        Vector vSolicitudes = new Vector();
        Vector vExpedientes = new Vector();
        Vector vEventos = new Vector();

        try {
            //*******************************************
            //** Obtener una conexion de la base de datos
            //*******************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new CResultadoOperacion(false, "Cannot get connection");
            }

            String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
                    "E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.ID_ESTADO as ID_ESTADO, " +
                    "V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO as V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
                    "from solicitud_licencia S, expediente_licencia E, tipo_licencia L, " +
                    "persona_juridico_fisica J, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "where " +
                    "E.ID_ESTADO !="+ CConstantesComando.ESTADO_DURMIENTE + " and "+
                    "E.ID_SOLICITUD=S.ID_SOLICITUD and S.ID_TIPO_LICENCIA=L.ID_TIPO_LICENCIA and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and "+
                    "V.REVISADO != " + CConstantesComando.CMD_EVENTO_REVISADO +
                    "::text and DICTIONARY.LOCALE='"+locale+"'" +

                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and s.id_municipio='" + idMunicipio + "' "):
                                       (idMunicipio == null ? "" : " and s.id_municipio='" + idMunicipio + "' ")) +

                    " order by V_FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, E.ID_FINALIZACION AS ID_FINALIZACION, " +
                    "E.ID_ESTADO AS ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.ID_ESTADO as ID_ESTADO, " +
                    "V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, V.ID_ESTADO as V_ID_ESTADO, " +
                    "DICTIONARY.TRADUCCION " +
                    "from solicitud_licencia S, expediente_licencia E, tipo_licencia L, " +
                    "persona_juridico_fisica J, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "where " +
                    "E.ID_ESTADO !="+ CConstantesComando.ESTADO_DURMIENTE + " and "+
                    "E.ID_SOLICITUD=S.ID_SOLICITUD and S.ID_TIPO_LICENCIA=L.ID_TIPO_LICENCIA and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and "+
                    "V.REVISADO != " + CConstantesComando.CMD_EVENTO_REVISADO +
                    "::text and DICTIONARY.LOCALE='"+locale+"'" +

                    (dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)!=null?
                                       " AND "+ dameTipos("S.ID_TIPO_LICENCIA", tiposLicencia)+ (idMunicipio == null ? "" : "and s.id_municipio='" + idMunicipio + "' "):
                                       (idMunicipio == null ? "" : " and s.id_municipio='" + idMunicipio + "' ")) +

                    " order by V_FECHA DESC";
            }

            logger.debug("SQL=" + sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            int i = 0;
            if (rs.next()) {
                do {
                    /** Estado */
                    CEstado estado = new CEstado();
                    estado.setIdEstado(rs.getInt("ID_ESTADO"));

                    /** Solicitud */
                    /** Tipo Licencia */
                    CTipoLicencia tipoLicencia= new CTipoLicencia();
                    tipoLicencia.setIdTipolicencia(rs.getInt("ID_TIPO_LICENCIA"));
                    CPersonaJuridicoFisica propietario= new CPersonaJuridicoFisica();
                    propietario.setIdPersona(rs.getLong("id_persona"));
                    propietario.setDniCif(rs.getString("dni_cif"));
                    propietario.setNombre(rs.getString("nombre"));
                    propietario.setApellido1(rs.getString("apellido1"));
                    propietario.setApellido2(rs.getString("apellido2"));

                    CSolicitudLicencia solicitud = new CSolicitudLicencia(tipoLicencia,
                            null,
                            propietario,
                            null,
                            null,
                            null,
                            rs.getString("NUM_ADMINISTRATIVO"),
                            rs.getString("CODIGO_ENTRADA"),
                            rs.getString("UNIDAD_TRAMITADORA"),
                            rs.getString("UNIDAD_DE_REGISTRO"),
                            rs.getString("MOTIVO"),
                            rs.getString("ASUNTO"),
                            rs.getTimestamp("FECHA"),
                            rs.getDouble("TASA"),
                            rs.getString("TIPO_VIA_AFECTA"),
                            rs.getString("NOMBRE_VIA_AFECTA"),
                            rs.getString("NUMERO_VIA_AFECTA"),
                            rs.getString("PORTAL_AFECTA"),
                            rs.getString("PLANTA_AFECTA"),
                            rs.getString("LETRA_AFECTA"),
                            rs.getString("CPOSTAL_AFECTA"),
                            rs.getString("MUNICIPIO_AFECTA"),
                            rs.getString("PROVINCIA_AFECTA"),
                            rs.getString("OBSERVACIONES"),
                            null,
                            null);

                    solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
                    vSolicitudes.add(i, solicitud);
                    /** Expediente */

                    CExpedienteLicencia expediente = new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
                          rs.getLong("ID_SOLICITUD"),
                           null,
                           null,
                            rs.getString("E_SERVICIO_ENCARGADO"),
                            rs.getString("E_ASUNTO"),
                            rs.getString("E_SILENCIO_ADMINISTRATIVO"),
                            rs.getString("E_FORMA_INICIO"),
                            rs.getInt("E_NUM_FOLIOS"),
                            rs.getDate("FECHA_APERTURA"),
                            rs.getString("E_RESPONSABLE"),
                            rs.getDate("E_PLAZO_RESOLUCION"),
                            rs.getString("E_HABILES"),
                            rs.getInt("E_NUM_DIAS"),
                            rs.getString("E_OBSERVACIONES"),
                            null,
                            null);
                    expediente.setEstado(estado);

                    CEvento evento = new CEvento();
                    evento.setIdEvento(rs.getLong("ID_EVENTO"));
                    CEstado estadoEvento= new CEstado();
                    estadoEvento.setIdEstado(rs.getInt("V_ID_ESTADO"));
                    evento.setEstado(estadoEvento);
                    evento.setRevisado(rs.getString("REVISADO_POR"));
                    evento.setRevisado(rs.getString("REVISADO"));
                    evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
                    //evento.setContent(rs.getString("CONTENT"));
                    /** Recogemos el content del evento de la tabla dictionary (Multilenguaje) */
                    //evento.setContent(getTraduccion(connection, rs.getString("CONTENT"), locale));
                    evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));
                    evento.setExpediente(expediente);
                    vEventos.add(i, evento);

                } while (rs.next());
            } else {
                safeClose(rs, statement, connection);
                return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
            }

            CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
            resultadoOperacion.setSolicitudes(vSolicitudes);
            resultadoOperacion.setExpedientes(vExpedientes);
            resultadoOperacion.setVector(vEventos);

            safeClose(rs, statement, connection);
            return resultadoOperacion;

        } catch (Exception ex) {

            safeClose(rs, statement, connection);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new CResultadoOperacion(false, ex.toString());
        }
        finally{
			safeClose(rs, statement, connection);
		}

    }

    private static void insertaTecnicosSolicitud(Connection connection, long idSolicitud, Vector tecnicos) throws Exception {

        PreparedStatement preparedStatement= null;

        try {

            String sql = "INSERT INTO TECNICOS (ID, ID_SOLICITUD, ID_PERSONA, PATRON_PERFIL) VALUES (?,?,?,?)";

            if (tecnicos == null) {
                return;
            }


            Enumeration enumeration = tecnicos.elements();
            while (enumeration.hasMoreElements()) {

                long sequence= getTableSequence();
                CPersonaJuridicoFisica t= (CPersonaJuridicoFisica)enumeration.nextElement();

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, sequence);
                preparedStatement.setLong(2, idSolicitud);
                preparedStatement.setLong(3, t.getIdPersona());
                preparedStatement.setString(4, t.getPatronPerfil());

                preparedStatement.execute();
                safeClose(null, null, preparedStatement, null);
            }
            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }

    }

    private static void insertarDatosActividad(Connection connection, long idSolicitud, DatosActividad datosActividad) throws Exception {

        PreparedStatement preparedStatement= null;

        try {

            /** SOLO se inserta un registro en ACTIVIDAD si existen datos de Actividad para la licencia */
            if (datosActividad == null) {
                return;
            }

            String sql = "INSERT INTO ACTIVIDAD (ID_ACTIVIDAD, ID_SOLICITUD, ALQUILER, AFORO, VENTILACION, DESC_ELEM_ALMACENAJE, ANTECEDENTE_OBRA, NUM_OPERARIOS, ALTURA_TECHOS) VALUES (?,?,?,?,?,?,?,?,?)";

            long sequence= getTableSequence();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, sequence);
            preparedStatement.setLong(2, idSolicitud);
            preparedStatement.setString(3, (new Integer(datosActividad.getAlquiler())).toString());
            preparedStatement.setInt(4, datosActividad.getAforo());
            if (datosActividad.getDescripcionVentilacion() != null){
                preparedStatement.setString(5, datosActividad.getDescripcionVentilacion());
            }else preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            if (datosActividad.getDescripcionAlmacenaje() != null){
                preparedStatement.setString(6, datosActividad.getDescripcionAlmacenaje());
            }else preparedStatement.setNull(6, java.sql.Types.VARCHAR);
            if (datosActividad.getNumExpedienteObra() != null){
                preparedStatement.setString(7, datosActividad.getNumExpedienteObra());
            }else preparedStatement.setNull(7, java.sql.Types.VARCHAR);
            preparedStatement.setInt(8, datosActividad.getNumeroOperarios());
            preparedStatement.setDouble(9, datosActividad.getAlturaTechos());

            preparedStatement.execute();

            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }

    }

    public static boolean tieneDatosActividad(Connection connection, long idSolicitud) {
        Statement statement = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                logger.warn("connection is null");
                return false;
            }

            String sql = "SELECT * FROM ACTIVIDAD WHERE ID_SOLICITUD=" + idSolicitud;
            logger.debug("sql: " + sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (!rs.next()) {
                logger.info("Datos de Actividad no encontrados");
                safeClose(rs, statement, null);
                return false;
            }
            safeClose(rs, statement, null);

            return true;
        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }
    }

    public static DatosActividad getDatosActividad(Connection connection, long idSolicitud) {

        Statement statement = null;
        ResultSet rs = null;

        try {

            logger.debug("Inicio.");


            if (connection == null) {
                logger.warn("connection is null");
                return null;
            }

            String sql = "SELECT * FROM ACTIVIDAD WHERE ID_SOLICITUD=" + idSolicitud;
            logger.debug("sql: " + sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (!rs.next()) {
                logger.info("Datos de Actividad no encontrados");
                safeClose(rs, statement, null);
                return null;
            }

            DatosActividad datosActividad= new DatosActividad();
            datosActividad.setAforo(rs.getInt("AFORO"));
            datosActividad.setAlturaTechos(rs.getDouble("ALTURA_TECHOS"));
            datosActividad.setDescripcionAlmacenaje(rs.getString("DESC_ELEM_ALMACENAJE"));
            datosActividad.setDescripcionVentilacion(rs.getString("VENTILACION"));
            datosActividad.setAlquiler(rs.getInt("ALQUILER"));
            datosActividad.setIdActividad(rs.getLong("ID_ACTIVIDAD"));
            datosActividad.setIdSolicitud(idSolicitud);
            datosActividad.setNumeroOperarios(rs.getInt("NUM_OPERARIOS"));
            datosActividad.setNumExpedienteObra(rs.getString("ANTECEDENTE_OBRA"));

            safeClose(rs, statement, null);
            return datosActividad;

        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }

    }


    public static void updateDatosActividad(Connection connection, long idSolicitud, DatosActividad datosActividad) throws Exception{
        PreparedStatement ps=null;
        try
        {
            if (connection == null) {
                logger.warn("connection is null");
                return;
            }

            /** comprobamos que existan datos de actividad para la licencia de actividad */
            if (!tieneDatosActividad(connection, idSolicitud)){
                /** insertamos datos de actividad */
                insertarDatosActividad(connection, idSolicitud, datosActividad);
                safeClose(null, ps, null);
                return;
            }

            String sql = "UPDATE ACTIVIDAD SET ALQUILER=?, AFORO=?, VENTILACION=?, DESC_ELEM_ALMACENAJE=?, ANTECEDENTE_OBRA=?, NUM_OPERARIOS=?, ALTURA_TECHOS=? WHERE ID_SOLICITUD="+idSolicitud;

            if (datosActividad != null){
                ps= connection.prepareStatement(sql);

                ps.setString(1, (new Integer(datosActividad.getAlquiler()).toString()));
                ps.setInt(2, datosActividad.getAforo());
                ps.setString(3, datosActividad.getDescripcionVentilacion());
                ps.setString(4, datosActividad.getDescripcionAlmacenaje());
                ps.setString(5, datosActividad.getNumExpedienteObra());
                ps.setInt(6, datosActividad.getNumeroOperarios());
                ps.setDouble(7, datosActividad.getAlturaTechos());

                ps.execute();
            }

            safeClose(null, ps, null);

        } catch (Exception ex){
            safeClose(null, ps, null);
            throw ex;
        }
    }


    public static Mejora insertarMejora(Connection conn, Mejora mejora) throws Exception
    {
        boolean cerrarConexion = false;
        PreparedStatement ps = null;
        try {
            if (mejora == null ) return null;
            if (conn==null)
            {
                conn=CPoolDatabase.getConnection();
                cerrarConexion=true;
                if(conn==null)
                {
                    logger.error("No se ha podido obtener una conexión valida");
                    return null;
                }
            }
            String sql = "insert into mejora (id_mejora, id_solicitud, num_expediente, observacion, fecha) values (?,?,?,?,?)";
            logger.info("SQL insertando mejora:"+sql);
            mejora.setIdMejora(getTableSequence());
            ps = conn.prepareStatement(sql);
            ps.setLong(1, mejora.getIdMejora());
            ps.setLong(2, mejora.getIdSolicitud());
            ps.setString(3, mejora.getNumExpediente());
            ps.setString(4, mejora.getObservaciones());
            ps.setTimestamp(5, new java.sql.Timestamp(mejora.getFecha()!=null?mejora.getFecha().getTime():
                                                                              new Date().getTime()));
            ps.execute();
            conn.commit();
            if (cerrarConexion) safeClose(null, ps, conn);
            else safeClose(null, ps, null);
        } catch (Exception ex) {
            if (cerrarConexion) safeClose(null, ps, conn);
            else safeClose(null, ps, null);
            throw ex;
        }
        return mejora;
    }

    public static Alegacion insertarAlegacion(Connection conn, Alegacion alegacion) throws Exception
    {
        boolean cerrarConexion = false;
        PreparedStatement ps=null;
        try {
            if (alegacion == null ) return null;
            if (conn==null)
            {
                conn=CPoolDatabase.getConnection();
                cerrarConexion=true;
                if(conn==null)
                {
                    logger.error("No se ha podido obtener una conexión valida");
                    return null;
                }
            }
            String sql = "insert into alegacion (id_alegacion, id_solicitud, num_expediente, observacion, fecha) values (?,?,?,?,?)";
            alegacion.setIdAlegacion(getTableSequence());
            ps = conn.prepareStatement(sql);
            ps.setLong(1, alegacion.getIdAlegacion());
            ps.setLong(2, alegacion.getIdSolicitud());
            ps.setString(3,alegacion.getNumExpediente());
            ps.setString(4, alegacion.getObservaciones());
            ps.setTimestamp(5, new java.sql.Timestamp((alegacion.getFecha()!=null?alegacion.getFecha().getTime():
                                                   new Date().getTime())));
            ps.execute();
            conn.commit();
            if (cerrarConexion) safeClose(null, ps, conn);
            else safeClose(null, ps, null);
        } catch (Exception ex) {
            if (cerrarConexion) safeClose(null, ps, conn);
            else safeClose(null, ps, null);
            throw ex;
        }
        return alegacion;
    }

    /** MEJORAS Y ALEGACIONES */

    /** MEJORAS */

    private static void insertarAnexoMejora(Connection conn, CAnexo anexo, Mejora mejora)  throws Exception
    {
        PreparedStatement ps=null;
        try{

            if (conn == null) {
                logger.warn("Cannot get connection");
                throw new Exception("Cannot get connection");
            }
            if (mejora != null){

                String sql = "insert into anexos_mejora (num_expediente, id_mejora, id_solicitud, id_anexo) values (?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, mejora.getNumExpediente());
                ps.setLong(2, mejora.getIdMejora());
                ps.setLong(3, mejora.getIdSolicitud());
                ps.setLong(4, anexo.getIdAnexo());
                ps.execute();
            }

            safeClose(null, ps, null);
        } catch (Exception ex){
            safeClose(null, ps, null);
            throw ex;
        }
    }


    public static Vector getMejorasSolicitud(Connection conn, long idSolicitud) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;

        try {
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            if (conn == null) {
                logger.warn("Cannot get connection");
                //return new Vector();
                return null;
            }

            String sql = "SELECT * FROM mejora WHERE id_solicitud=" + idSolicitud + " ORDER BY fecha DESC";
            logger.info(".......SQL=" + sql);

            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            /* NUEVO*/
            Vector mejoras= null;
            if (rs.next()){
                mejoras = new Vector();
                do{
                    Mejora mejora= new Mejora();
                    mejora.setIdMejora(rs.getLong("ID_MEJORA"));
                    mejora.setIdSolicitud(idSolicitud);
                    mejora.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    mejora.setObservaciones(rs.getString("OBSERVACION"));
                    mejora.setFecha(rs.getDate("FECHA"));
                    mejora.setAnexos(getAnexosMejora(conn, mejora.getIdMejora()));

                    mejoras.add(mejora);
                }while(rs.next());

            }
            /**/
            safeClose(rs, statement, null);
            return mejoras;

        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            //return new Vector();
            return null;

        }

    }

    public static Vector getInformesExpediente(Connection conn, String numExpediente) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;
        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return null;
            }

            String sql = "select id_informe, id_tipo_informe, url_fichero, fecha_llegada, observacion, acuerdo from informe_preceptivo where num_expediente='"+numExpediente+"'";
            logger.info(".......SQL=" + sql);
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            Vector informes= new Vector();
            while (rs.next())
            {
                    Informe informe= new Informe();
                    informe.setId(rs.getString("id_informe"));
                    informe.setTipo(rs.getString("id_tipo_informe"));
                    informe.setFichero(rs.getString("url_fichero"));
                    informe.setFecha(rs.getDate("fecha_llegada"));
                    informe.setObservaciones(rs.getString("observacion"));
                    informe.setFavorable(rs.getString("acuerdo")!=null&&rs.getString("acuerdo").equals("1"));
                    System.out.println("Observacion: "+rs.getString("observacion"));
                    informes.add(informe);
            }
            
            safeClose(rs, statement, null);
            return informes;
        } catch (Exception ex) {
            safeClose(rs, statement, null);
            logger.error("Exception al recoger los informes: ", ex);
            return null;
        }
    }

    public static Resolucion getResolucion(Connection conn, String numExpediente) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;
        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return null;
            }

            String sql = "select a_favor, fecha_resolucion,organo_aprobacion, coletilla,asunto " +
                    " from resolucion where num_expediente='"+numExpediente+"'";
            logger.info("SQL=" + sql);
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            Resolucion resolucion= null;
            if (rs.next())
            {
                  resolucion = new Resolucion();
                  resolucion.setFavorable(rs.getString("a_favor")!=null&&rs.getString("a_favor").equals("1"));
                  resolucion.setFecha(rs.getDate("fecha_resolucion"));
                  resolucion.setOrgano(rs.getString("organo_aprobacion"));
                  resolucion.setColetilla(rs.getString("coletilla"));
                  resolucion.setAsunto(rs.getString("asunto"));
            }
            safeClose(rs, statement, null);
            return resolucion;
        } catch (Exception ex) {
            safeClose(rs, statement, null);
            logger.error("Exception al recoger los informes: ", ex);
            return null;
        }
     }


    public static Vector getAnexosMejora(Connection conn, long idMejora) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;

        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }

            String sql = "SELECT anexo.* FROM anexo, anexos_mejora WHERE anexo.id_anexo=anexos_mejora.id_anexo and id_mejora=" + idMejora;
            logger.info("sql=" + sql);

            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            Vector anexos = new Vector();
            while (rs.next()) {

                CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), "", "");
                CAnexo anexo = new CAnexo(tipoAnexo, rs.getString("URL_FICHERO"), rs.getString("OBSERVACION"));
                anexo.setIdAnexo(rs.getLong("ID_ANEXO"));

                anexos.add(anexo);

            }

            safeClose(rs, statement, null);
            return anexos;

        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();

        }

    }

    private static void updateMejorasSolicitud(Connection conn, Vector mejoras, long idSolicitud)  throws Exception
    {
        PreparedStatement ps=null;
        try
        {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return;
            }


            String sql = "UPDATE mejora SET observacion=? WHERE id_mejora=?";
            if (mejoras != null){
                for (Enumeration e= mejoras.elements(); e.hasMoreElements();){
                    Mejora mejora= (Mejora)e.nextElement();

                    ps= conn.prepareStatement(sql);
                    ps.setString(1, mejora.getObservaciones());
                    ps.setLong(2, mejora.getIdMejora());
                    ps.execute();
                    safeClose(null, null, ps, null);

                    modificaAnexosSolicitud(idSolicitud, mejora.getAnexos(), mejora, null);
                }
            }


            safeClose(null, ps, null);

        } catch (Exception ex){
            safeClose(null, ps, null);
            throw ex;
        }
    }

    private static void deleteAnexoMejora(Connection conn, CAnexo anexo, long idSolicitud) throws Exception {

        PreparedStatement preparedStatement=null;

        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                throw new Exception("Cannot get connection");
            }

            String sql = "DELETE FROM ANEXOS_MEJORA WHERE ID_SOLICITUD=? AND ID_ANEXO=?";

            if ((anexo != null) && (anexo.getIdAnexo() != -1)){
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setLong(1, idSolicitud);
                preparedStatement.setLong(2, anexo.getIdAnexo());
                preparedStatement.execute();
            }
            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }
    }


    /** ALEGACION */

    public static Alegacion getAlegacion(Connection conn, String numExpediente) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;

        try {
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            if (conn == null) {
                logger.warn("Cannot get connection");
                return null;
            }

            String sql = "SELECT * FROM alegacion WHERE num_expediente='" + numExpediente + "'";
            logger.debug("sql: " + sql);

            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            if (!rs.next()) {
                safeClose(rs, statement, null);
                return null;
            }
            Alegacion alegacion= new Alegacion();
            alegacion.setIdAlegacion(rs.getLong("ID_ALEGACION"));
            alegacion.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
            alegacion.setNumExpediente(numExpediente);
            alegacion.setObservaciones(rs.getString("OBSERVACION"));
            alegacion.setFecha(rs.getDate("FECHA"));

            alegacion.setAnexos(getAnexosAlegacion(conn, alegacion.getIdAlegacion()));

            safeClose(rs, statement, null);
            return alegacion;

        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;

        }

    }


    public static Vector getAnexosAlegacion(Connection conn, long idAlegacion) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;

        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return new Vector();
            }

            String sql = "SELECT anexo.* FROM anexo, anexos_alegacion WHERE anexo.id_anexo=anexos_alegacion.id_anexo and id_alegacion=" + idAlegacion;
            logger.info("sql=" + sql);

            statement = conn.createStatement();
            rs = statement.executeQuery(sql);

            Vector anexos = new Vector();
            while (rs.next()) {

                CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), "", "");
                CAnexo anexo = new CAnexo(tipoAnexo, rs.getString("URL_FICHERO"), rs.getString("OBSERVACION"));
                anexo.setIdAnexo(rs.getLong("ID_ANEXO"));

                anexos.add(anexo);

            }

            safeClose(rs, statement, null);
            return anexos;

        } catch (Exception ex) {

            safeClose(rs, statement, null);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();

        }

    }

    private static void insertarAnexoAlegacion(Connection conn, CAnexo anexo, Alegacion alegacion)  throws Exception
    {
        PreparedStatement ps=null;
        try
        {
            if (conn == null) {
                logger.warn("Cannot get connection");
                throw new Exception("Cannot get connection");
            }
            if (alegacion != null){
                String sql = "insert into anexos_alegacion (num_expediente, id_alegacion, id_solicitud, id_anexo) values (?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, alegacion.getNumExpediente());
                ps.setLong(2, alegacion.getIdAlegacion());
                ps.setLong(3, alegacion.getIdSolicitud());
                ps.setLong(4, anexo.getIdAnexo());
                ps.execute();
            }

            safeClose(null, ps, null);

        } catch (Exception ex){
            safeClose(null, ps, null);
            throw ex;
        }
    }


    private static void deleteAnexoAlegacion(Connection conn, CAnexo anexo, long idSolicitud) throws Exception {

        PreparedStatement preparedStatement=null;

        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                throw new Exception("Cannot get connection");
            }

            String sql = "DELETE FROM ANEXOS_ALEGACION WHERE ID_SOLICITUD=? AND ID_ANEXO=?";

            if ((anexo != null) && (anexo.getIdAnexo() != -1)){
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setLong(1, idSolicitud);
                preparedStatement.setLong(2, anexo.getIdAnexo());
                preparedStatement.execute();
            }
            safeClose(null, null, preparedStatement, null);

        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
            throw ex;
        }
    }

    private static void updateAlegacionExpediente(Connection conn, Alegacion alegacion, long idSolicitud)  throws Exception
    {
        PreparedStatement ps=null;
        try
        {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return;
            }


            String sql = "UPDATE alegacion SET observacion=? WHERE id_alegacion=?";
            if (alegacion != null){
                ps= conn.prepareStatement(sql);
                ps.setString(1, alegacion.getObservaciones());
                ps.setLong(2, alegacion.getIdAlegacion());
                ps.execute();

                modificaAnexosSolicitud(idSolicitud, alegacion.getAnexos(), null, alegacion);
            }

            safeClose(null, ps, null);

        } catch (Exception ex){
            safeClose(null, ps, null);
            throw ex;
        }
    }

    /** BLOQUEAR EXPEDIENTE  */
    public static CResultadoOperacion bloquearExpediente(String numExpediente, boolean bloquear){
        Connection connection= null;
        PreparedStatement preparedStatement= null;
        Statement statement = null;
        ResultSet rs= null;
        try{

            if (numExpediente == null){
                logger.warn("numExpediente is null");
                return new CResultadoOperacion(false, "numExpediente is null");                
            }
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new CResultadoOperacion(false, "Cannot get connection");
            }
            connection.setAutoCommit(false);

            preparedStatement= connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET BLOQUEADO=? WHERE NUM_EXPEDIENTE='"+numExpediente+"'");
            if (bloquear){
                 preparedStatement.setString(1, "1");
            }else{
                preparedStatement.setString(1, "0");
            }
            preparedStatement.execute();
            connection.commit();
            safeClose(rs, statement, preparedStatement, connection);

            return new CResultadoOperacion(true, "");

        }catch (Exception ex){
            safeClose(rs, statement, preparedStatement, connection);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new CResultadoOperacion(false, ex.toString());
        }
    }

    public static CResultadoOperacion desbloquearExpedientes(){
        Connection connection= null;
        PreparedStatement preparedStatement= null;
        Statement statement = null;
        ResultSet rs= null;
        try{

            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return new CResultadoOperacion(false, "Cannot get connection");
            }
            connection.setAutoCommit(false);

            preparedStatement= connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET BLOQUEADO='0'");
            preparedStatement.execute();
            connection.commit();
            safeClose(rs, statement, preparedStatement, connection);

            return new CResultadoOperacion(true, "");

        }catch (Exception ex){
        	safeClose(rs, statement, preparedStatement, connection);
            logger.error("Exception: " ,ex);
            return new CResultadoOperacion(false, ex.toString());
        }
        finally{
			safeClose(rs, statement, connection);
		}
    }
    public static void actualizaResolucion(CExpedienteLicencia expedienteLicencia)
    {
        Connection connection= null;
        try{
             connection = CPoolDatabase.getConnection();
             if (connection == null) return;
             actualizaResolucion(connection, expedienteLicencia);
             connection.commit();
        }catch (Exception ex){
            logger.error("Exception: ", ex);
        }
        finally{
			safeClose(null, null, connection);
		}
        
        safeClose(null,null,connection);
    }

    public static void actualizarSubparcelas(Connection conn, Vector referencias, String idMunicipio, String valor){
        String sSQL= "UPDATE subparcelas SET constru='"+valor+"' WHERE referencia_catastral=? AND id_municipio='"+idMunicipio+"'";
        PreparedStatement ps= null;
		try {
            /* Para cada una de las referencias catastrales sacamos MASA+parcela que corresponde a los siete
            primeros digitos de la referencia catastral */
            if (referencias == null) return;
            for (int i=0; i<referencias.size(); i++){
                if ((CReferenciaCatastral)referencias.get(i) == null) continue;
                ps= conn.prepareStatement(sSQL);
                String masa= ((CReferenciaCatastral)referencias.get(i)).getReferenciaCatastral().substring(0, 7);
                ps.setString(1, masa);
                ps.execute();
                ps.close();
            }
        }catch (Exception e){
        }finally{
            try{ps.close();}catch(Exception e){};
        }
    }


    public static void actualizarSubparcelas(Vector referencias, String idMunicipio, String valor){
        Connection connection= null;
        try{
             connection= CPoolDatabase.getConnection();
             if (connection == null) return;
             connection.setAutoCommit(false);
             actualizarSubparcelas(connection, referencias, idMunicipio, valor);
             connection.commit();
        }catch (Exception ex){
        }finally{
            try{connection.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }
    }

    /**
     * Desbloqueo de todos los bienes de inventario de BD
     * @throws Exception
     */
    public static void desbloquearBienesInventario() throws Exception{
        String sSQL= "UPDATE BIENES_INVENTARIO SET BLOQUEADO=?";

        PreparedStatement ps= null;
        Connection conn= null;
		try {
            conn= CPoolDatabase.getConnection();
            conn.setAutoCommit(false);
            ps= conn.prepareStatement(sSQL);
            ps.setNull(1, java.sql.Types.VARCHAR);
            ps.execute();
            conn.commit();
        }catch (Exception e){
            throw e;
        }finally{
            try{ps.close();}catch(Exception e){};
            try{conn.close();CPoolDatabase.releaseConexion();}catch(Exception e){}
        }
    }

    public static CResultadoOperacion checkExpedientePublicado(CExpedienteLicencia expedienteLicencia, String idMunicipio) throws Exception {

    	Connection connection= null;
    	PreparedStatement preparedStatement= null;
    	ResultSet rs = null;

    	try{

    		int estadoAntiguo = searchExpedienteLicencia(expedienteLicencia.getNumExpediente()).getEstado().getIdEstado();
    		int estadoNuevo = expedienteLicencia.getEstado().getIdEstado();

    		if (estadoNuevo != estadoAntiguo){

    			connection = CPoolDatabase.getConnection();
    			if (connection == null) {
    				logger.warn("Cannot get connection");
    				return new CResultadoOperacion(false, "Cannot get connection");
    			}
    			connection.setAutoCommit(false);

    			String sql = "SELECT id_sigem FROM expediente_licencia WHERE num_expediente='" + expedienteLicencia.getNumExpediente() + "'";

    			preparedStatement = (PreparedStatement) connection.createStatement();
    			rs = preparedStatement.executeQuery(sql);

    			if (!rs.next()) {
    				int idSigem = rs.getInt("id_sigem");
    				if(idSigem == 1){

    					sql = "SELECT url FROM url_servicios " +
    					"LEFT JOIN servicios ON servicios.servicio='SIGEM' WHERE url_servicios.id_servicio=servicios.id " +
    					"AND (url_servicios.id_municipio=" + idMunicipio + " OR url_servicios.id_municipio=0)";

    					preparedStatement = (PreparedStatement) connection.createStatement();
    					rs = preparedStatement.executeQuery(sql);

    					if (!rs.next()) {
    						String url = rs.getString("url");
    						if(url!=null && !url.equals("")){

    							ConsultaExpedientesWebServiceServiceClient client = new ConsultaExpedientesWebServiceServiceClient(url);
    							ConsultaExpedientesWebService service = client.getConsultaExpedientesWebService();

    							String descripcionEstado = null;
    							if (expedienteLicencia.getEstado() != null){
    								descripcionEstado = expedienteLicencia.getEstado().getDescripcion();
    							}
    							RetornoLogico respuesta = service.actualizarEstadoLocalGIS(expedienteLicencia.getNumExpediente(), descripcionEstado);

    							if (respuesta.getErrorCode() != null){
    								safeClose(rs, preparedStatement, null);
    								logger.error("Code: " + respuesta.getErrorCode());
    								return new CResultadoOperacion(false, "ERROR. Código: " + respuesta.getErrorCode());
    							}
    							else{
    								safeClose(rs, preparedStatement, null);
    								return new CResultadoOperacion(true, "Código: " + 
    										respuesta.getReturnCode() + ". Valor: " + 
    										respuesta.getValor());
    							}
    						}
    						else{
    							safeClose(rs, preparedStatement, null);
    							logger.warn("Cannot get web service");
    							return new CResultadoOperacion(false, "Servicio Web no disponible");
    						}
    					}
    					safeClose(rs, preparedStatement, null);
    					logger.warn("Cannot get web service");
    					return new CResultadoOperacion(false, "Servicio Web no disponible");
    				}
    				else{
    					safeClose(rs, preparedStatement, null);
    					return new CResultadoOperacion(true, "ExpedienteNoPublicado");
    				}
    			}
    			safeClose(rs, preparedStatement, null);
    			return new CResultadoOperacion(true, "ExpedienteNoExistente");

    		}
    		else{				
    			return new CResultadoOperacion(true, "ExpedienteMismoEstado");
    		}
    	} catch (Exception ex) {

    		safeClose(rs, preparedStatement, connection);
    		StringWriter sw = new StringWriter();
    		PrintWriter pw = new PrintWriter(sw);
    		ex.printStackTrace(pw);
    		return new CResultadoOperacion(false, ex.toString());

    	}
    	finally{
    		safeClose(rs, preparedStatement, connection);
    	}
    }


	/**
	 * ACTUALIZAR ID_SIGEM
	 */
	public static CResultadoOperacion actualizarIdSigem(CExpedienteLicencia expedienteLicencia) {

		Connection connection= null;
		PreparedStatement preparedStatement= null;
		Statement statement = null;
		ResultSet rs= null;

		try {

			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}
			connection.setAutoCommit(false);
            
			preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET " +
					"ID_SIGEM=1 WHERE NUM_EXPEDIENTE='" + expedienteLicencia.getNumExpediente() + "'");
					
			preparedStatement.execute();
			connection.commit();

			safeClose(rs, statement, preparedStatement, connection);

			return new CResultadoOperacion(true, "");

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}

	}
	
	/**
	 * OBTENER ID_SIGEM
	 */
	public static CResultadoOperacion obtenerIdSigem(CExpedienteLicencia expedienteLicencia) {

		Connection connection= null;
		PreparedStatement preparedStatement= null;
		Statement statement = null;
		ResultSet rs= null;

		try {

			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}
			connection.setAutoCommit(false);
            
			String sql = "SELECT id_sigem FROM expediente_licencia WHERE num_expediente='" + expedienteLicencia.getNumExpediente() + "'";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {
				
				int idSigem = rs.getInt("id_sigem");
				if (idSigem == 0){
					safeClose(rs, statement, null);
					return new CResultadoOperacion(true, "ExpedienteNoPublicado");
				}
				else{
					safeClose(rs, statement, null);
					return new CResultadoOperacion(true, "ExpedientePublicado");
				}
				
			}

			safeClose(rs, statement, preparedStatement, connection);

			return new CResultadoOperacion(false, "ERROR");

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}

	}
	
	/**
	 * Publicar Expediente de Licencias en SIGEM
	 */
	public static CResultadoOperacion publicarExpedienteSigem(CExpedienteLicencia expedienteLicencia, CSolicitudLicencia solicitudLicencia, String idMunicipio) {

		Connection connection= null;
		PreparedStatement preparedStatement= null;
		Statement statement = null;
		ResultSet rs= null;

		try {

			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new CResultadoOperacion(false, "Cannot get connection");
			}
			connection.setAutoCommit(false);

			String sql = "SELECT url FROM url_servicios " +
			"LEFT JOIN servicios ON servicios.servicio='SIGEM' WHERE url_servicios.id_servicio=servicios.id " +
			"AND (url_servicios.id_municipio=" + idMunicipio + " OR url_servicios.id_municipio=0)";

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {
				String url = rs.getString("url");
				if(url!=null && !url.equals("")){

					ConsultaExpedientesWebServiceServiceClient client = new ConsultaExpedientesWebServiceServiceClient(url);
					ConsultaExpedientesWebService service = client.getConsultaExpedientesWebService();
					
					String descripcionEstado = null;
					if (expedienteLicencia.getEstado()!= null){
						descripcionEstado = expedienteLicencia.getEstado().getDescripcion();
					}
					String fechaApertura = null;
					if (expedienteLicencia.getFechaApertura() != null){
						fechaApertura = expedienteLicencia.getFechaApertura().toString();
					}
					
					RetornoLogico retorno = service.publicarExpedienteLocalGIS(expedienteLicencia.getNumExpediente(),
							solicitudLicencia.getPropietario().getDniCif(), expedienteLicencia.getTipoLicenciaDescripcion(), 
							descripcionEstado, fechaApertura);
			        
					if (retorno.getValor()!= null){
						Boolean resultado = new Boolean(retorno.getValor());
						if (resultado.booleanValue()){
							safeClose(rs, preparedStatement, null);
							logger.error("Code: " + retorno.getErrorCode());
							return new CResultadoOperacion(false, "ERROR. Código: " + retorno.getErrorCode());
						}
						else{
							safeClose(rs, preparedStatement, null);
							return new CResultadoOperacion(true, retorno.getValor());
						}
					}
										
				}
				else{
					safeClose(rs, preparedStatement, null);
					logger.warn("Cannot get web service");
					return new CResultadoOperacion(false, "Servicio Web no disponible");
				}
			}
			safeClose(rs, preparedStatement, null);
			logger.warn("Cannot get web service");
			return new CResultadoOperacion(false, "Servicio Web no disponible");

		} catch (Exception ex) {

			safeClose(rs, statement, preparedStatement, connection);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.toString());

		}
		finally{
    		safeClose(rs, preparedStatement, connection);
    	}

	}
	


}
