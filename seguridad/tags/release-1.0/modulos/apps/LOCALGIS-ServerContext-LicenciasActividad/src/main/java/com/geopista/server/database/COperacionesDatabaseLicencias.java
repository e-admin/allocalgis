package com.geopista.server.database;

import ieci.tecdoc.sgm.ct.ws.server.RetornoLogico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.protocol.licencias.CDatosNotificacion;
import com.geopista.protocol.licencias.CEvento;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.CHistorico;
import com.geopista.protocol.licencias.CNotificacion;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.protocol.licencias.CPlantilla;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.protocol.licencias.CViaNotificacion;
import com.geopista.protocol.licencias.CalleAfectada;
import com.geopista.protocol.licencias.estados.CEstado;
import com.geopista.protocol.licencias.estados.CEstadoNotificacion;
import com.geopista.protocol.licencias.estados.CEstadoResolucion;
import com.geopista.protocol.licencias.tipos.CTipoAnexo;
import com.geopista.protocol.licencias.tipos.CTipoFinalizacion;
import com.geopista.protocol.licencias.tipos.CTipoLicencia;
import com.geopista.protocol.licencias.tipos.CTipoNotificacion;
import com.geopista.protocol.licencias.tipos.CTipoObra;
import com.geopista.protocol.licencias.tipos.CTipoTramitacion;
import com.geopista.protocol.ocupacion.CDatosOcupacion;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.SRIDDefecto;
import com.geopista.server.licencias.workflow.CConstantesWorkflow;
import com.geopista.server.licencias.workflow.CWorkflowLine;
import com.geopista.server.licencias.workflow.CWorkflowLiterales;
import com.localgis.sigem.client.ConsultaExpedientesWebService;
import com.localgis.sigem.client.ConsultaExpedientesWebServiceServiceClient;
/*
import com.vividsolutions.jts.io.WKTReader;
import org.mortbay.jaas.JAASRole;
*/

/* REFACTORIZACION ORACLE
import oracle.sdoapi.sref.SRManager;
import oracle.sdoapi.sref.SpatialReference;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.adapter.AdapterSDO;
import oracle.sdoapi.geom.GeometryFactory;
import oracle.sql.STRUCT;
import org.geotools.data.oracle.attributeio.AdapterJTS;
*/

/**
 * @author SATEC
 * @version $Revision: 1.8 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/09/22 10:27:00 $
 *          $Name:  $
 *          $RCSfile: COperacionesDatabaseOcupaciones.java,v $
 *          $Revision: 1.8 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class COperacionesDatabaseLicencias extends COperacionesDatabase {

	private static String _pathPlantillas = "plantillas" + File.separator + "ocupacion" + File.separator;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesDatabaseLicencias.class);

//    public static SRID srid=null;
    public static SRIDDefecto srid = null;

    public static boolean safeClose(ResultSet rs, Statement statement, Connection connection) {        

		try {

			if (connection!=null)

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

			if (connection!=null)
			{
				connection.close();
				CPoolDatabase.releaseConexion();   
			}

		} catch (SQLException e) {
			logger.error("Can't close connection",e);
		}  
		return true;

	}

	public static boolean safeClose(ResultSet rs, Statement statement, PreparedStatement preparedStatement, Connection connection) {

		try {

			if (preparedStatement!=null)
				preparedStatement.close();

		} catch (Exception e) {
			logger.error("Can't close prepared statement",e);
		}
		return safeClose(rs, statement, connection);

	}


	public static CDatosNotificacion getDatosNotificacionByIdPersonaByIdSolicitud(long idPersona, long idSolicitud) {

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

			String sql = "SELECT * FROM DATOS_NOTIFICACION WHERE ID_PERSONA=" + idPersona + " and ID_SOLICITUD=" + idSolicitud;
			logger.debug("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("Datos de Notificacion");
				safeClose(rs, statement, connection);
				return null;
			}

			CDatosNotificacion datos = new CDatosNotificacion();

			safeClose(rs, statement, connection);
			return datos;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


	public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(String nifCif, String id_municipio) {

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

			String sql = "select A.* FROM PERSONA_JURIDICO_FISICA A where (A.id_municipio=" + id_municipio +" OR A.id_municipio=0) and UPPER(A.dni_cif)=UPPER('" + nifCif + "')";
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("Persona no encontrada");
				safeClose(rs, statement, connection);
				return null;
			}

			CPersonaJuridicoFisica persona = new CPersonaJuridicoFisica(rs.getString("DNI_CIF"), rs.getString("NOMBRE"), rs.getString("APELLIDO1"), rs.getString("APELLIDO2"), rs.getString("COLEGIO"), rs.getString("VISADO"), rs.getString("TITULACION"), null);
			persona.setIdPersona(rs.getLong("ID_PERSONA"));

			safeClose(rs, statement, connection);
			logger.debug("persona.getIdPersona(): " + persona.getIdPersona());
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


	public static CWorkflowLine getWorkflowLine(long idEstado) {

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

			String sql = "select * FROM WORKFLOW WHERE ID_ESTADO=" + idEstado + " and ID_NEXTESTADO=" + idEstado + " and id_tipo_licencia=-1";
			logger.debug("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				logger.info("WorkflowLine no encontrado para el estado. idEstado: " + idEstado);
				safeClose(rs, statement, connection);
				return null;
			}

			CWorkflowLine workflowLine = new CWorkflowLine(rs.getInt("ID_ESTADO"), rs.getInt("ID_NEXTESTADO"), rs.getInt("PLAZO"), rs.getInt("ID_PLAZOESTADO"), rs.getString("EVENT_TEXT"), rs.getString("HITO_TEXT"), rs.getString("NOTIF_TEXT"));


			safeClose(rs, statement, connection);
			return workflowLine;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


	public static synchronized long getTableSequence() {

		try {

			Thread.sleep(10);

		} catch (Exception ex) {

		}

		long sequence = new Date().getTime();
		logger.debug("sequence: " + sequence);

		return sequence;

	}


	public static CPersonaJuridicoFisica insertUpdatePersonaJuridicoFisica(Connection connection, CPersonaJuridicoFisica personaJuridicoFisica, String idMunicipio) throws Exception {

		PreparedStatement preparedStatement = null;
		Statement statement = null;
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
			CPersonaJuridicoFisica aux = getPersonaJuridicaFromDatabase(personaJuridicoFisica.getDniCif(), idMunicipio);

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
				preparedStatement.setString(8, idMunicipio);
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
				preparedStatement.setString(9, idMunicipio);
				preparedStatement.execute();

			}

			safeClose(rs, statement, preparedStatement, null);
			return personaJuridicoFisica;

		} catch (Exception ex) {
			safeClose(rs, statement, preparedStatement, null);
			throw ex;


		}


	}




	private static void insertaAnexosSolicitud(Connection connection, long idSolicitud, Vector anexos) throws Exception {
        PreparedStatement preparedStatement= null;
		try {

			String sql = "INSERT INTO ANEXO ( ID_SOLICITUD, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION ) VALUES (?,?,?,?,?)";

			if (anexos == null) {
				logger.info("Anexos no insertados. anexos: " + anexos);
				return;
			}


			Enumeration enumeration = anexos.elements();
			while (enumeration.hasMoreElements()) {

				long sequenceAnexo = getTableSequence();
				CAnexo anexo = (CAnexo) enumeration.nextElement();
				logger.debug("anexo.getContent(): " + anexo.getContent());

				String fileName = new String(anexo.getFileName());
				logger.debug("fileName: " + fileName);

				String anexosDir = "anexos" + File.separator;
				String ocupacionesDir = "ocupacion" + File.separator;
				String solicitudDir = idSolicitud + File.separator;


				try {

					if (!new File(anexosDir).exists()) {
						new File(anexosDir).mkdirs();
					}

					if (!new File(anexosDir + ocupacionesDir).exists()) {
						new File(anexosDir + ocupacionesDir).mkdirs();
					}

					if (!new File(anexosDir + ocupacionesDir + solicitudDir).exists()) {
						new File(anexosDir + ocupacionesDir + solicitudDir).mkdirs();
					}

					FileOutputStream out = new FileOutputStream(anexosDir + ocupacionesDir + solicitudDir + fileName);
					out.write(anexo.getContent());
					out.close();
					logger.info("Anexo created: " + anexosDir + ocupacionesDir + solicitudDir + fileName);

				} catch (Exception ex) {
					logger.error("Exception: " + ex.toString());
				}


				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1, idSolicitud);
				preparedStatement.setLong(2, sequenceAnexo);
				preparedStatement.setInt(3, anexo.getTipoAnexo().getIdTipoAnexo());
				preparedStatement.setString(4, fileName);
				preparedStatement.setString(5, anexo.getObservacion());

				preparedStatement.execute();
                safeClose(null,null, preparedStatement, null);
			}


		} catch (Exception ex) {
            safeClose(null,null, preparedStatement, null);
			throw ex;
		}


	}


	private static boolean modificaAnexosSolicitud(long idSolicitud, Vector anexos) {

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

			if (anexos != null) {
				for (int i = 0; i < anexos.size(); i++) {
					CAnexo anexo = (CAnexo) anexos.elementAt(i);
					logger.info("anexo.getFileName(): " + anexo.getFileName() + ". anexo.getEstado():" + anexo.getEstado());

					switch (anexo.getEstado()) {

						case CConstantesComando.CMD_ANEXO_ADDED:


							try {

								String path = "anexos" + File.separator + "ocupacion" + File.separator + idSolicitud + File.separator;
								logger.debug("path: " + path);

								if (!new File(path).exists()) {
									new File(path).mkdirs();
								}

								FileOutputStream out = new FileOutputStream(path + anexo.getFileName());
								out.write(anexo.getContent());
								out.close();
								logger.info("Anexo created. path + fileName: " + path + anexo.getFileName());

							} catch (Exception ex) {
								logger.error("Exception: " + ex.toString());
							}


							try {

                                /* NOTA: debido al conflicto generado al eliminar y annadir el mismo archivo (2 entradas para el mismo anexo en BD),
                                antes de insertar comprobamos que ya exista un anexo con el mismo URL_FICHERO */
                                siExisteBorrarAnexo(idSolicitud, anexo);

								String sql = "INSERT INTO ANEXO ( ID_SOLICITUD, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION ) VALUES (?,?,?,?,?)";
								preparedStatement = connection.prepareStatement(sql);
								preparedStatement.setLong(1, idSolicitud);
								preparedStatement.setLong(2, getTableSequence());
								preparedStatement.setLong(3, anexo.getTipoAnexo().getIdTipoAnexo());
								preparedStatement.setString(4, anexo.getFileName());
								preparedStatement.setString(5, anexo.getObservacion());
								preparedStatement.execute();
								connection.commit();
                                safeClose(null, null, preparedStatement, null);

							} catch (Exception ex) {
								logger.error("Exception: " + ex.toString());
							}

							logger.info("Anexo added.");
							break;


						case CConstantesComando.CMD_ANEXO_DELETED:


							try {

								String sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND URL_FICHERO=?";
								logger.info("DELETE FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud + " AND URL_FICHERO=" + anexo.getFileName());

								preparedStatement = connection.prepareStatement(sql);
								preparedStatement.setLong(1, idSolicitud);
								preparedStatement.setString(2, anexo.getFileName());
								preparedStatement.execute();
								connection.commit();
                                safeClose(null, null, preparedStatement, null);

								String path = "anexos" + File.separator + "ocupacion" + File.separator + idSolicitud + File.separator;
								logger.debug("path: " + path);

								File file = new File(path + anexo.getFileName());
								if (!file.exists()) {
									logger.warn("File not found. path+anexo.getFileName(): " + path + anexo.getFileName());
									continue;

								}
								file.delete();
								logger.info("Anexo deleted.");

							} catch (Exception ex) {
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
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;

		}


	}

    private static boolean siExisteBorrarAnexo(long idSolicitud, CAnexo anexo) {

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
               NOTA: no puede haber 2 anexos con el mismo nombre para la misma solicitud. */

            String sql= "SELECT * FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud +" AND URL_FICHERO='"+anexo.getFileName()+ "'";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {

                sql = "DELETE FROM ANEXO WHERE ID_SOLICITUD=? AND URL_FICHERO=?";
                logger.info("DELETE FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud + " AND URL_FICHERO='" + anexo.getFileName() + "'");

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, idSolicitud);
                preparedStatement.setString(2, anexo.getFileName());
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
            preparedStatement.setString(3, anexo.getIdAnexo());
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


	private static void insertaReferenciasCatastrales(Connection connection, long secuencia, Vector referenciasCatastrales) throws Exception {
        PreparedStatement preparedStatement= null;

		try {

			long tiempoInicial = new Date().getTime();

			String sql = "INSERT INTO PARCELARIO (ref_catastral,id_solicitud,tipo_via,nombre_via,numero,letra,bloque,escalera,planta,puerta) VALUES (?,?,?,?,?,?,?,?,?,?)";
			if (referenciasCatastrales == null) {
				logger.info("referenciasCatastrales no insertadas. referenciasCatastrales: " + referenciasCatastrales);
				return;
			}
			Enumeration enumeration = referenciasCatastrales.elements();
			while (enumeration.hasMoreElements()) {
              	CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) enumeration.nextElement();
                logger.info("insertando en el parcelario: "+ referenciaCatastral.getReferenciaCatastral());

				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, referenciaCatastral.getReferenciaCatastral());
				preparedStatement.setLong(2, secuencia);
				preparedStatement.setString(3, referenciaCatastral.getTipoVia());
				preparedStatement.setString(4, referenciaCatastral.getNombreVia());
				preparedStatement.setString(5, referenciaCatastral.getPrimerNumero());
				preparedStatement.setString(6, referenciaCatastral.getPrimeraLetra());
				preparedStatement.setString(7, referenciaCatastral.getBloque());
				preparedStatement.setString(8, referenciaCatastral.getEscalera());
				preparedStatement.setString(9, referenciaCatastral.getPlanta());
				preparedStatement.setString(10, referenciaCatastral.getPuerta());

				preparedStatement.execute();
				safeClose(null, null, preparedStatement, null);

			}

			long tiempoFinal = new Date().getTime();
			logger.debug("tiempoFinal-tiempoInicial: " + (tiempoFinal - tiempoInicial));
            safeClose(null, null, preparedStatement, null);

		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
	}


	public static CResultadoOperacion crearExpedienteLicencias(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia, Principal userPrincipal, String idMunicipio) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet rs = null;

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


			CDatosOcupacion datosOcupacion = solicitudLicencia.getDatosOcupacion();

			connection.setAutoCommit(false);
			CPersonaJuridicoFisica propietario = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPropietario(), idMunicipio);
			connection.commit();
			CPersonaJuridicoFisica representante = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getRepresentante(), idMunicipio);
			connection.commit();
			CPersonaJuridicoFisica tecnico = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.dameTecnico(), idMunicipio);
			connection.commit();
			CPersonaJuridicoFisica promotor = insertUpdatePersonaJuridicoFisica(connection, solicitudLicencia.getPromotor(), idMunicipio);
			connection.commit();

			long secuencia = getTableSequence();

			//****************************************
			//** Insertamos en SOLICITUD_LICENCIA
			//****************************************************
			preparedStatement = connection.prepareStatement("INSERT INTO SOLICITUD_LICENCIA ( ID_SOLICITUD, ID_TIPO_LICENCIA, ID_TIPO_OBRA, PROPIETARIO, REPRESENTANTE, TECNICO, PROMOTOR, NUM_ADMINISTRATIVO, CODIGO_ENTRADA, UNIDAD_TRAMITADORA, UNIDAD_DE_REGISTRO, MOTIVO, ASUNTO, FECHA, FECHA_ENTRADA, TASA, TIPO_VIA_AFECTA, NOMBRE_VIA_AFECTA, NUMERO_VIA_AFECTA, PORTAL_AFECTA, PLANTA_AFECTA, LETRA_AFECTA, CPOSTAL_AFECTA, MUNICIPIO_AFECTA, PROVINCIA_AFECTA, OBSERVACIONES,NOMBRE_COMERCIAL, ID_MUNICIPIO ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);

			if (solicitudLicencia.getTipoLicencia() == null) {
				preparedStatement.setInt(2, -1);
			} else {
				preparedStatement.setInt(2, solicitudLicencia.getTipoLicencia().getIdTipolicencia());
			}


			if (solicitudLicencia.getTipoObra() == null) {
				preparedStatement.setInt(3, -1);
			} else {
				preparedStatement.setInt(3, solicitudLicencia.getTipoObra().getIdTipoObra());
			}


			preparedStatement.setLong(4, propietario.getIdPersona());

			if (representante != null) {
				preparedStatement.setLong(5, representante.getIdPersona());
			} else
				preparedStatement.setString(5, null);

			if (tecnico != null) {
				preparedStatement.setLong(6, tecnico.getIdPersona());
			} else {
				preparedStatement.setString(6, null);
			}

			if (promotor != null) {
				preparedStatement.setLong(7, promotor.getIdPersona());
			} else {
				preparedStatement.setString(7, null);
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
			preparedStatement.setString(17, solicitudLicencia.getTipoViaAfecta());
			preparedStatement.setString(18, solicitudLicencia.getNombreViaAfecta());
			preparedStatement.setString(19, solicitudLicencia.getNumeroViaAfecta());
			preparedStatement.setString(20, solicitudLicencia.getPortalAfecta());
			preparedStatement.setString(21, solicitudLicencia.getPlantaAfecta());
			preparedStatement.setString(22, solicitudLicencia.getLetraAfecta());
			preparedStatement.setString(23, solicitudLicencia.getCpostalAfecta());
			preparedStatement.setString(24, solicitudLicencia.getMunicipioAfecta());
			preparedStatement.setString(25, solicitudLicencia.getProvinciaAfecta());
			preparedStatement.setString(26, solicitudLicencia.getObservaciones());
			preparedStatement.setString(27, solicitudLicencia.getNombreComercial());
            preparedStatement.setString(28, idMunicipio);
			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);




			//****************************************
			//** Insertamos los anexos
			//****************************************************
			insertaAnexosSolicitud(connection, secuencia, solicitudLicencia.getAnexos());




			//****************************************
			//** Insertamos las referencias catastrales
			//****************************************************
			insertaReferenciasCatastrales(connection, secuencia, solicitudLicencia.getReferenciasCatastrales());



			//****************************************
			//** Insertamos los datos de notificacion de las personas juridicas
			//****************************************************
			insertUpdateDatosNotificacion(connection, propietario, secuencia);
            connection.commit();
			if (representante != null) {
				insertUpdateDatosNotificacion(connection, representante, secuencia);
                connection.commit();
			}
			if (tecnico != null) {
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
            //ASO cambia la sentencia de inserción
			preparedStatement = connection.prepareStatement("INSERT INTO EXPEDIENTE_LICENCIA(ID_SOLICITUD,NUM_EXPEDIENTE, " +
                    "ID_TRAMITACION,ID_FINALIZACION,ID_ESTADO,SERVICIO_ENCARGADO,ASUNTO,SILENCIO_ADMINISTRATIVO,FORMA_INICIO, " +
                    "NUM_FOLIOS,FECHA_APERTURA,RESPONSABLE,PLAZO_RESOLUCION,HABILES,NUM_DIAS,OBSERVACIONES,VU,CNAE,app_string) " +
                    "VALUES(?,?,0,null,?,?,null,0,?,1,?,?,null,0,null,null,?,?,?)");
			preparedStatement.setLong(1, secuencia);
			preparedStatement.setString(2, numExpediente);
			preparedStatement.setInt(3, expedienteLicencia.getEstado().getIdEstado());
            preparedStatement.setString(4, expedienteLicencia.getServicioEncargado());
            preparedStatement.setString(5, expedienteLicencia.getFormaInicio());
			preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
            preparedStatement.setString(7, expedienteLicencia.getResponsable());
            //ASO ELIMINA EL PLAZO DE RESOLUCION
            //preparedStatement.setTimestamp(8, new Timestamp(new Date().getTime()));
			preparedStatement.setString(8, expedienteLicencia.getVU());
			preparedStatement.setString(9, expedienteLicencia.getCNAE());
			preparedStatement.setString(10, CConstantesComando.APP_OCUPACIONES);
			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);



			//****************************************
			//** Insertamos los datos de la ocupacion
			//****************************************************
            //* CONCESIONES: insertamos la ocupacion y luego actualizamos para la ocupacion insertada los datos de la concesion */
            long idOcupacion= getTableSequence();
			preparedStatement = connection.prepareStatement("INSERT INTO datos_ocupacion ( id_ocupacion, id_solicitud, tipo_ocupacion, necesita_obra, num_expediente, hora_inicio, hora_fin, num_mesas,num_sillas,afecta_acera, afecta_calzada, afecta_aparcamiento,area_ocupacion,m2_acera,m2_calzada,m2_aparcamiento) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			preparedStatement.setLong(1, idOcupacion);
			preparedStatement.setLong(2, secuencia);
			preparedStatement.setInt(3, datosOcupacion.getTipoOcupacion());
            if (datosOcupacion.getNecesitaObra() != null)
			    preparedStatement.setString(4, datosOcupacion.getNecesitaObra());
            else preparedStatement.setNull(4, java.sql.Types.VARCHAR);

            if ((datosOcupacion.getNumExpediente() == null) || (datosOcupacion.getNumExpediente().trim().equalsIgnoreCase(""))){
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            }else preparedStatement.setString(5, datosOcupacion.getNumExpediente());

			if (datosOcupacion.getHoraInicio() != null) {
				preparedStatement.setTimestamp(6, new Timestamp(datosOcupacion.getHoraInicio().getTime()));
			} else {
				preparedStatement.setNull(6, java.sql.Types.TIMESTAMP);
			}

			if (datosOcupacion.getHoraFin() != null) {
				preparedStatement.setTimestamp(7, new Timestamp(datosOcupacion.getHoraFin().getTime()));
			} else {
				preparedStatement.setNull(7, java.sql.Types.TIMESTAMP);
			}

            if (datosOcupacion.getNumMesas() != -1)
			    preparedStatement.setInt(8, datosOcupacion.getNumMesas());
            else preparedStatement.setNull(8, java.sql.Types.INTEGER);
            if (datosOcupacion.getNumSillas() != -1)
			    preparedStatement.setInt(9, datosOcupacion.getNumSillas());
            else preparedStatement.setNull(9, java.sql.Types.INTEGER);
            if (datosOcupacion.getAfectaAcera() != null)
			    preparedStatement.setString(10, datosOcupacion.getAfectaAcera());
            else preparedStatement.setNull(10, java.sql.Types.VARCHAR);
            if (datosOcupacion.getAfectaCalzada() != null)
			    preparedStatement.setString(11, datosOcupacion.getAfectaCalzada());
            else preparedStatement.setNull(11, java.sql.Types.VARCHAR);
            if (datosOcupacion.getAfectaAparcamiento() != null)
			    preparedStatement.setString(12, datosOcupacion.getAfectaAparcamiento());
            else preparedStatement.setNull(12, java.sql.Types.VARCHAR);
            if (datosOcupacion.getAreaOcupacion() != -1)
			    preparedStatement.setDouble(13, datosOcupacion.getAreaOcupacion());
            else preparedStatement.setNull(13, java.sql.Types.DOUBLE);
            if (datosOcupacion.getM2acera() != -1)
			    preparedStatement.setDouble(14, datosOcupacion.getM2acera());
            else preparedStatement.setNull(14, java.sql.Types.DOUBLE);
            if (datosOcupacion.getM2calzada() != -1)
			    preparedStatement.setDouble(15, datosOcupacion.getM2calzada());
            else preparedStatement.setNull(15, java.sql.Types.DOUBLE);
            if (datosOcupacion.getM2aparcamiento() != -1)
			    preparedStatement.setDouble(16, datosOcupacion.getM2aparcamiento());
            else preparedStatement.setNull(16, java.sql.Types.DOUBLE);
			preparedStatement.execute();
            connection.commit();
            safeClose(null, null, preparedStatement, null);

            /** actualizamos los datos de la concesion en la ocupacion insertada */
            /** Lo hacemos cuando ya se ha hecho commit de lo anterior, o de lo contrario
             * Exception: org.postgresql.util.PSQLException: ERROR: transacción abortada, las consultas serán ignoradas hasta el fin de bloque de transacción */
            try{
                preparedStatement= connection.prepareStatement("UPDATE datos_ocupacion SET fecha_inicio=?, fecha_fin=? WHERE id_ocupacion=? AND id_solicitud=?");
                if (datosOcupacion.getFechaInicio() != null)
                    preparedStatement.setTimestamp(1, new Timestamp(datosOcupacion.getFechaInicio().getTime()));
                else preparedStatement.setNull(1, java.sql.Types.TIMESTAMP);
                if (datosOcupacion.getFechaFin() != null)
                    preparedStatement.setTimestamp(2, new Timestamp(datosOcupacion.getFechaFin().getTime()));
                else preparedStatement.setNull(2, java.sql.Types.TIMESTAMP);
                preparedStatement.setLong(3, idOcupacion);
                preparedStatement.setLong(4, secuencia);
                preparedStatement.execute();
            }catch(Exception e){safeClose(null, preparedStatement, null);}

			connection.commit();
			connection.setAutoCommit(true);
			safeClose(rs, preparedStatement, connection);

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

			insertHistoricoExpediente(expedienteLicencia, userPrincipalName, true);
			insertEventoExpediente(expedienteLicencia, CConstantesComando.TIPO_OCUPACION,
                    true, false);

			//******************************************************
			//** Ventanilla única
			//*******************************************
			if (expedienteLicencia.getVU().equals("1")) {
				COperacionesDatabaseLicencias.inicializarConsultaTelematica(expedienteLicencia, solicitudLicencia);
			}

            CResultadoOperacion resultadoOperacion= new CResultadoOperacion(true, numExpediente);
            /** Necesitamos el idSolicitud para ShowMaps.updateFeature */
            Vector vExpediente= new Vector();
            vExpediente.add(expedienteLicencia);
            resultadoOperacion.setExpedientes(vExpediente);
            return resultadoOperacion;

		} catch (Exception ex) {

			safeClose(rs, preparedStatement, connection);
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

				conditions += "upper(" + field + "::text) like upper('" + value + "%')";

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

	public static Vector getSearchedExpedientes(Hashtable hash, Enumeration permisos, Sesion sesion) {

		Vector expedientes = getSearchedExpedientes(hash,sesion);
		logger.info("expedientes.size(): " + expedientes.size());
		return expedientes;

	}

	public static Vector getSearchedExpedientes(Hashtable hash, Sesion sesion) {

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
           //ASO quita el tipo de finalizacion
           String sql= "select h.tipo_ocupacion,h.necesita_obra,h.num_expediente as num_expediente_licencia_obra,h.hora_inicio,h.hora_fin,h.num_mesas,h.num_sillas,h.afecta_acera,h.afecta_calzada,h.afecta_aparcamiento,h.area_ocupacion,h.m2_acera,h.m2_calzada,h.m2_aparcamiento,E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA,E.ID_TIPO_LICENCIA,A.*,F.DNI_CIF AS NIFPROPIETARIO, A.ID_FINALIZACION " +
                        "from expediente_licencia A, datos_ocupacion H,  solicitud_licencia E, persona_juridico_fisica F " +
                        "where a.ID_SOLICITUD=e.ID_SOLICITUD and " +
                        "e.propietario=f.id_persona and h.id_solicitud=e.id_solicitud and "+
                        "A.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' " + conditions + " and e.id_municipio='"
                        +sesion.getIdMunicipio()+"' order by FECHA_APERTURA DESC";
			logger.info("sql: " + sql);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector expedientes = new Vector();

			while (rs.next()) {

				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "","");
                CTipoFinalizacion tipoFinalizacion=null;
                if (rs.getString("ID_FINALIZACION")!=null)
            	      tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

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
                
                expediente.setBloqueado(rs.getString("BLOQUEADO"));

				CDatosOcupacion datosOcupacion = new CDatosOcupacion();
				datosOcupacion.setTipoOcupacion(rs.getInt("TIPO_OCUPACION"));
				datosOcupacion.setNecesitaObra(rs.getString("NECESITA_OBRA"));
				datosOcupacion.setNumExpediente(rs.getString("num_expediente_licencia_obra"));
				datosOcupacion.setHoraInicio(rs.getTimestamp("HORA_INICIO"));
				datosOcupacion.setHoraFin(rs.getTimestamp("HORA_FIN"));
				datosOcupacion.setNumMesas(rs.getInt("num_mesas"));
				datosOcupacion.setNumSillas(rs.getInt("num_sillas"));
				datosOcupacion.setAfectaAcera(rs.getString("AFECTA_ACERA"));
				datosOcupacion.setAfectaCalzada(rs.getString("AFECTA_CALZADA"));
				datosOcupacion.setAfectaAparcamiento(rs.getString("AFECTA_APARCAMIENTO"));
				datosOcupacion.setAreaOcupacion(rs.getDouble("area_ocupacion"));
				datosOcupacion.setM2acera(rs.getDouble("m2_acera"));
				datosOcupacion.setM2calzada(rs.getDouble("m2_calzada"));
				datosOcupacion.setM2aparcamiento(rs.getDouble("m2_aparcamiento"));
				expediente.setDatosOcupacion(datosOcupacion);


				expediente.setVU(rs.getString("VU"));
				expediente.setPlazoEvent(rs.getString("PLAZO_EVENT"));
				expediente.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
				expediente.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
				expediente.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));

				expediente.setNifPropietario(rs.getString("NIFPROPIETARIO"));

				expediente.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
				expediente.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
				expediente.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));

				expedientes.add(expediente);


			}
			safeClose(rs, statement, connection);
			logger.debug("expedientes: " + expedientes);

			return expedientes;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}
		finally{
			safeClose(rs, statement, connection);
		}

	}



    public static Vector getSearchedLicenciasObra(Hashtable hash, Enumeration permisos, String idMunicipio) {

        Vector expedientes = getSearchedLicenciasObra(hash, idMunicipio);
        logger.info("expedientesLicenciasObra.size(): " + expedientes.size());

        return expedientes;

    }


    public static Vector getSearchedLicenciasObra(Hashtable hash, String idMunicipio) {

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
            /*
            String sql = "select E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA,E.ID_TIPO_LICENCIA as E_ID_TIPO_LICENCIA, E.ID_TIPO_OBRA as E_ID_TIPO_OBRA,A.*,F.DNI_CIF AS NIFPROPIETARIO,G.DESCRIPCION AS DESCTIPOLICENCIA,B.DESCRIPCION AS ESTADO_DESCRIPCION,B.OBSERVACION AS ESTADO_OBSERVACION,B.STEP AS ESTADO_STEP,C.DESCRIPCION AS TIPO_TRAMITACION_DESCRIPCION,C.OBSERVACION AS TIPO_TRAMITACION_OBSERVACION,C.PLAZO_ENTREGA AS TIPO_TRAMITACION_PLAZO_ENTREGA  " +
                    "from expediente_licencia A " +
                    "LEFT JOIN estado B " +
                    "ON a.id_estado=b.id_estado " +
                    "JOIN tipo_tramitacion C " +
                    "ON a.ID_TRAMITACION=c.ID_TRAMITACION " +
                    "JOIN solicitud_licencia E " +
                    "ON a.ID_SOLICITUD=e.ID_SOLICITUD " +
                    "JOIN persona_juridico_fisica F " +
                    "ON e.propietario=f.id_persona " +
                    "JOIN tipo_licencia G " +
                    "ON e.id_tipo_licencia=g.id_tipo_licencia where A.APP_STRING='" + CConstantesComando.APP_LICENCIAS + "' " +
                    //09-06-2005 charo annade la condicion de que las licencias sean solo de obraMayor y obraMenor
                    " and (E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MAYOR+ " OR "+
                    "E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MENOR+ ") "+                    
                    (idMunicipio == null ? "" : "and id_municipio='" + idMunicipio + "' ") + conditions + " order by FECHA_APERTURA DESC";
            */
            String sql = "select E.TIPO_VIA_AFECTA,E.NOMBRE_VIA_AFECTA,E.NUMERO_VIA_AFECTA,E.ID_TIPO_LICENCIA as E_ID_TIPO_LICENCIA, E.ID_TIPO_OBRA as E_ID_TIPO_OBRA,A.*,F.DNI_CIF AS NIFPROPIETARIO,G.DESCRIPCION AS DESCTIPOLICENCIA  " +
                    "from expediente_licencia A " +
                    "JOIN solicitud_licencia E " +
                    "ON a.ID_SOLICITUD=e.ID_SOLICITUD " +
                    "JOIN persona_juridico_fisica F " +
                    "ON e.propietario=f.id_persona " +
                    "JOIN tipo_licencia G " +
                    "ON e.id_tipo_licencia=g.id_tipo_licencia where A.APP_STRING='" + CConstantesComando.APP_LICENCIAS + "' " +
                    //09-06-2005 charo annade la condicion de que las licencias sean solo de obraMayor y obraMenor
                    " and (E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MAYOR+ " OR "+
                    "E.ID_TIPO_LICENCIA="+CConstantesComando.TIPO_OBRA_MENOR+ ") "+
                    (idMunicipio == null ? "" : "and e.id_municipio='" + idMunicipio + "' ") + conditions + " order by FECHA_APERTURA DESC";


            logger.info("sql= " + sql);


            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            Vector expedientes = new Vector();

            while (rs.next()) {

                CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));
                CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion = null;
                if (rs.getString("ID_FINALIZACION")!=null)
                    tipoFinalizacion=new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

                Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));
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
                        notificaciones);

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

                expedientes.add(expediente);


            }
            safeClose(rs, statement, connection);
            logger.debug("expedientes: " + expedientes);

            return expedientes;

        } catch (Exception ex) {
            safeClose(rs, statement, connection);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return new Vector();

        }

    }




	public static Vector getSearchedReferenciasCatastrales(Hashtable hash) {

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


			String sql = "" +
					" select a.id,b.tipovianormalizadocatastro,a.denominacion,a.numvia from tramosvia a " +
					"LEFT JOIN vias b " +
					"ON a.id=b.id ";

			logger.info("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector referenciasCatastrales = new Vector();

			while (rs.next()) {

				CReferenciaCatastral referenciaCatastral = new CReferenciaCatastral(rs.getString("id"),
						rs.getString("tipovianormalizadocatastro"),
						rs.getString("denominacion"),
						rs.getString("numvia"),
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


	public static CExpedienteLicencia searchExpedienteLicencia(String numExpediente) {

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


			String sql = "select A.* from expediente_licencia A " +
                         "where A.NUM_EXPEDIENTE='" + numExpediente + "' order by FECHA_APERTURA DESC";

			logger.debug("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			if (rs.next()) {


				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
				CTipoFinalizacion tipoFinalizacion = null;
                if (rs.getString("ID_FINALIZACION")!=null)
                       tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"),null, null);

				Vector notificaciones = getNotificacionesExpediente(numExpediente);

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
						notificaciones);

				safeClose(rs, statement, connection);
				return expediente;
			}

			safeClose(rs, statement, connection);
			return null;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}

	}


	public static Vector getSearchedExpedientesExpirables(boolean todos) {

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

			String sql = "";

			if (todos) {
				sql = "select A.* from expediente_licencia A " +
                        "where A.ID_ESTADO IN (select ID_ESTADO from workflow where PLAZO IS NOT NULL) ORDER BY FECHA_APERTURA DESC";
			} else {
				sql = "select A.* from expediente_licencia A " +
                      "where A.ID_ESTADO IN (select ID_ESTADO from workflow where PLAZO IS NOT NULL) AND A.PLAZO_EVENT IS NULL ORDER BY FECHA_APERTURA DESC";
			}

			logger.debug("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector expedientes = new Vector();
			while (rs.next()) {


				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));
				CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
                CTipoFinalizacion tipoFinalizacion = null;
                if (rs.getString("ID_FINALIZACION")!=null)
                    tipoFinalizacion=new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

				Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

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
						notificaciones);

				expedienteLicencia.setVU(rs.getString("VU"));
				expedienteLicencia.setPlazoEvent(rs.getString("PLAZO_EVENT"));
				expedienteLicencia.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
				expedienteLicencia.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));

				expedienteLicencia.setFechaCambioEstado(rs.getTimestamp("FECHA_CAMBIO_ESTADO"));

				expedientes.add(expedienteLicencia);
			}

			safeClose(rs, statement, connection);
			logger.debug("expedientes: " + expedientes);


			return expedientes;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getSearchedAddresses(Hashtable hash,String idMunicipio)
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
			logger.error("Exception al buscar en numeros de policia: " + ex);
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


	private static Vector getAnexosSolicitud(long idSolicitud) {

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

			String sql = "SELECT * FROM ANEXO WHERE ID_SOLICITUD=" + idSolicitud;
            logger.info("SQL="+sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector anexos = new Vector();
			while (rs.next()) {

				CTipoAnexo tipoAnexo = new CTipoAnexo(rs.getInt("ID_TIPO_ANEXO"), "", "");
				CAnexo anexo = new CAnexo(tipoAnexo, rs.getString("URL_FICHERO"), rs.getString("OBSERVACION"));
				anexo.setIdAnexo(String.valueOf(rs.getLong("ID_ANEXO")));

				anexos.add(anexo);
			}

			safeClose(rs, statement, connection);
			logger.info("anexos: " + anexos);
			return anexos;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}

	private static Vector getNotificacionesExpediente(String numExpediente) {

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

			//String sql = "SELECT A.*,B.DESCRIPCION,B.OBSERVACION FROM NOTIFICACION A,ESTADO_NOTIFICACION B where A.ID_ESTADO=B.ID_ESTADO AND A.NUM_EXPEDIENTE='" + numExpediente + "'";

			String sql = "SELECT A.*  " +
					"FROM NOTIFICACION A  " +
					"where A.NUM_EXPEDIENTE='" + numExpediente + "' " +
					"ORDER BY A.PLAZO_VENCIMIENTO ASC";

			logger.debug("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector notificaciones = new Vector();
			while (rs.next()) {

				CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(rs.getInt("ID_ESTADO"), "", "");
				CTipoNotificacion tipoNotificacion = new CTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"), "", "");
				CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"));

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

			safeClose(rs, statement, connection);

			logger.debug("notificaciones: " + notificaciones);

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


	private static Vector getReferenciasCatastrales(Connection connection, long idSolicitud)
    {
    	Statement statement = null;
		ResultSet rs = null;
		try {
			if (connection == null) {
				logger.warn("Cannot get connection");
				return new Vector();
			}
    		String sql = "select parcelario.*, geometria_ocupacion.area from parcelario, geometria_ocupacion " +
                    " where parcelario.ref_catastral= geometria_ocupacion.id and parcelario.id_solicitud='" + idSolicitud+"'";

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

                Double area= null;
                try{
                    area=rs.getString("area")==null?null: new Double(rs.getString("area"));
                }catch (Exception e){logger.error("ERROR al recoger el area de la referencia catastral.");}

                referenciaCatastral.setArea(area);
				referenciasCatastrales.add(referenciaCatastral);
			}
			safeClose(rs, statement, null);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());
			return referenciasCatastrales;
		} catch (Exception ex) {
			safeClose(rs, statement, connection);
			logger.error("Exception: ", ex);
			return new Vector();
		}
	}
    public static Vector getCallesAfectadas(Connection conn, String numExpediente) throws Exception
    {
        Statement statement = null;
        ResultSet rs = null;
        try {
            if (conn == null) {
                logger.warn("Cannot get connection");
                return null;
            }
            String sql = "select id_afectada, id_solicitud, num_expediente, " +
                    "tipoviaine, nombre, numero, comentario, id_via from calle_afectada where num_expediente='"+numExpediente+"'";
            logger.info("SQL=" + sql);
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            Vector calles= new Vector();
            while (rs.next())
            {
                    CalleAfectada afectada= new CalleAfectada();
                    afectada.setId(rs.getString("id_afectada"));
                    afectada.setIdSolicitud(rs.getLong("id_solicitud"));
                    afectada.setNumExpediente(rs.getString("num_expediente"));
                    afectada.setTipoViaIne(rs.getString("tipoviaine"));
                    afectada.setNombre(rs.getString("nombre"));
                    /*
                    afectada.setNumero((rs.getString("numero")==null?
                                       null: new Integer(rs.getInt("numero"))));
                    */
                    afectada.setNumero((rs.getString("numero")==null?
                                       null: rs.getString("numero")));

                    afectada.setComentario(rs.getString("comentario"));
                    afectada.setIdVia((rs.getString("id_via")==null?
                                       null: new Integer(rs.getInt("id_via"))));
                    calles.add(afectada);
            }

            safeClose(rs, statement, null);
            return calles;
        } catch (Exception ex) {
            safeClose(rs, statement, null);
            logger.error("Exception al recoger los informes: ", ex);
            return null;
        }
    }
     private static void actualizaCallesAfectadas(Connection connection, CExpedienteLicencia expediente)
    {
        try
        {
           if (connection ==null || expediente==null ) return;
           //Borramos todos las calles afectadas previas
           borrarCallesAfectadas(connection, expediente);
           if (expediente.getCallesAfec()==null) return;
           for (Enumeration e= expediente.getCallesAfec().elements(); e.hasMoreElements();)
           {
               CalleAfectada afectada= (CalleAfectada)e.nextElement();
               afectada.setNumExpediente(expediente.getNumExpediente());
               afectada.setIdSolicitud(expediente.getIdSolicitud());
               insertaCalleAfectada(connection, afectada);
           }
        }catch(Exception e)
        {
            logger.error("Excepcion al actualizar los expedientes: ", e);
        }
    }

    public static boolean insertaCalleAfectada(Connection connection , CalleAfectada afectada)
    {
        PreparedStatement ps = null;
        try
        {
               if (connection == null) {
                   logger.warn("Cannot get connection");
                   return false;
               }
               if (afectada.getId()==null)
                    afectada.setId(new Long(getTableSequence()).toString());

                String sql="insert into calle_afectada (id_afectada, id_solicitud, " +
                        "num_expediente, tipoviaine, nombre, numero, comentario, id_via) " +
                        "values (?,?,?,?,?,?,?,?)";
                logger.info(sql);
                ps = connection.prepareStatement(sql);
                ps.setString(1,afectada.getId());
                ps.setLong(2, afectada.getIdSolicitud());
                ps.setString(3, afectada.getNumExpediente());
                ps.setString(4,afectada.getTipoViaIne());
                ps.setString(5,afectada.getNombre());
                ps.setString(6, (afectada.getNumero()!=null?afectada.getNumero().toString():
                                    null));
                ps.setString(7, afectada.getComentario());
                ps.setString(8, (afectada.getIdVia()!=null?afectada.getIdVia().toString():null));
                ps.execute();
                connection.commit();
                safeClose(null, null, ps, null);
                return true;
        } catch (Exception ex) {
                safeClose(null, null, ps, null);
                logger.error("Error al añadir calles afectadas: ", ex);
                return false;
        }
    }
    public static boolean borrarCallesAfectadas(Connection connection , CExpedienteLicencia expediente)
    {
        PreparedStatement ps = null;
        try
        {
               if (expediente==null || expediente.getNumExpediente()==null) return false;
               if (connection == null) {
                   logger.warn("Cannot get connection");
                   return false;
               }
                String sql="delete from calle_afectada where num_expediente='"+expediente.getNumExpediente()+"'";
                logger.info(sql);
                ps = connection.prepareStatement(sql);
                ps.execute();
                connection.commit();
                safeClose(null, null, ps, null);
                return true;
        } catch (Exception ex) {
                safeClose(null, null, ps, null);
                logger.error("Error al borrar calles afectadas: ", ex);
                return false;
        }
    }


	public static CResultadoOperacion getExpedienteLicencia(String numExpediente, String idMunicipio, String locale) {

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
				return new CResultadoOperacion(false, "Cannot get connection");

			}

			//*******************************************************
			//** Se obtiene idSolicitud para ese expediente
			//********************************************
            String sql = "select A.*, h.tipo_ocupacion,h.necesita_obra,h.num_expediente as num_expediente_licencia_obra,h.hora_inicio,h.hora_fin,h.num_mesas,h.num_sillas,h.afecta_acera,h.afecta_calzada,h.afecta_aparcamiento,h.area_ocupacion,h.m2_acera,h.m2_calzada,h.m2_aparcamiento " +
                                 "from expediente_licencia A, datos_ocupacion h, solicitud_licencia s " +
                                 "where a.num_expediente='" + numExpediente + "' and h.id_solicitud=s.id_solicitud and " +
                                 "s.id_solicitud=a.id_solicitud and s.id_municipio='"+idMunicipio+"' and " +
                                 "A.app_string='" + CConstantesComando.APP_OCUPACIONES + "'";


            logger.debug("SQL=" +sql);

			statement = connection.createStatement();
     	    rs= statement.executeQuery(sql);
			if (!rs.next()) {
				safeClose(rs, statement, connection);
				logger.info("Expediente no encontrado. numExpediente: " + numExpediente);
				return new CResultadoOperacion(false, "Expediente no encontrado.");

			}

			CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));
            CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
            CTipoFinalizacion tipoFinalizacion=null;
            if (rs.getString("ID_FINALIZACION")!=null)
                   tipoFinalizacion = new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), "", "");

			/** Notificaciones */
			Vector notificaciones = getNotificacionesExpediente(numExpediente);

			/** Eventos del expediente */
			Vector eventos = getEventosExpediente(numExpediente, locale);

			/** Historico */
			Vector historico = getHistoricoExpediente(numExpediente, locale);

			CExpedienteLicencia expedienteLicencia= new CExpedienteLicencia(rs.getString("NUM_EXPEDIENTE"),
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

            expedienteLicencia.setBloqueado(rs.getString("BLOQUEADO"));

			CDatosOcupacion datosOcupacion = new CDatosOcupacion();
			datosOcupacion.setTipoOcupacion(rs.getInt("TIPO_OCUPACION"));
			datosOcupacion.setNecesitaObra(rs.getString("NECESITA_OBRA"));
			datosOcupacion.setNumExpediente(rs.getString("num_expediente_licencia_obra"));
			datosOcupacion.setHoraInicio(rs.getTimestamp("HORA_INICIO"));
			datosOcupacion.setHoraFin(rs.getTimestamp("HORA_FIN"));
			datosOcupacion.setNumMesas(rs.getInt("num_mesas"));
			datosOcupacion.setNumSillas(rs.getInt("num_sillas"));
			datosOcupacion.setAfectaAcera(rs.getString("AFECTA_ACERA"));
			datosOcupacion.setAfectaCalzada(rs.getString("AFECTA_CALZADA"));
			datosOcupacion.setAfectaAparcamiento(rs.getString("AFECTA_APARCAMIENTO"));
			datosOcupacion.setAreaOcupacion(rs.getDouble("area_ocupacion"));
			datosOcupacion.setM2acera(rs.getDouble("m2_acera"));
			datosOcupacion.setM2calzada(rs.getDouble("m2_calzada"));
			datosOcupacion.setM2aparcamiento(rs.getDouble("m2_aparcamiento"));
			expedienteLicencia.setDatosOcupacion(datosOcupacion);

			logger.info("rs.getString(\"VU\"): " + rs.getString("VU"));

			expedienteLicencia.setVU(rs.getString("VU"));
			logger.info("expedienteLicencia.getVU(): " + expedienteLicencia.getVU());
			expedienteLicencia.setPlazoEvent(rs.getString("PLAZO_EVENT"));
			expedienteLicencia.setSilencioEvent(rs.getString("SILENCIO_EVENT"));
			expedienteLicencia.setSilencioTriggered(rs.getString("SILENCIO_TRIGGERED"));
			expedienteLicencia.setCNAE(rs.getString("CNAE"));
			expedienteLicencia.setEventos(eventos);
			expedienteLicencia.setHistorico(historico);
            /* Recogemos las calles afectadas*/
            expedienteLicencia.setCallesAfec(getCallesAfectadas(connection, expedienteLicencia.getNumExpediente()));


			long idSolicitud = expedienteLicencia.getIdSolicitud();
			Vector vExpediente = new Vector();
			vExpediente.add(expedienteLicencia);

			logger.info("idSolicitud: " + idSolicitud);


			//*******************************************************
			//** Se obtiene la solicitud para ese expediente
			//********************************************
			sql = "SELECT A.* FROM SOLICITUD_LICENCIA A WHERE ID_SOLICITUD=" + idSolicitud;

            safeClose(rs, statement, null);
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if (!rs.next()) {
				logger.info("Solicitud no encontrada. idSolicitud: " + idSolicitud);
				safeClose(rs, statement, connection);
				return new CResultadoOperacion(false, "Solicitud no encontrada.");

			}


			CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs.getLong("PROPIETARIO"), idSolicitud);
			CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"), idSolicitud);
			Vector anexos = getAnexosSolicitud(idSolicitud);

			Vector referenciasCatastales = getReferenciasCatastrales(connection,idSolicitud);

			CSolicitudLicencia solicitudLicencia = new com.geopista.protocol.licencias.CSolicitudLicencia(null,
					null,
					propietario,
					representante,
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
					anexos,
					referenciasCatastales);


			solicitudLicencia.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
			solicitudLicencia.setNombreComercial(rs.getString("NOMBRE_COMERCIAL"));
			Vector vSolicitud = new Vector();
			vSolicitud.add(solicitudLicencia);

			safeClose(rs, statement, connection);

            /** Recogemos los datos de la concesion, si realmente la BD tiene estos datos */
            /** Lo hacemos cuando ya se ha hecho commit de lo anterior, o de lo contrario
             * Exception: org.postgresql.util.PSQLException: ERROR: transacción abortada, las consultas serán ignoradas hasta el fin de bloque de transacción */
            try{
                String query= "SELECT d.fecha_inicio, d.fecha_fin FROM datos_ocupacion d, solicitud_licencia s, expediente_licencia e WHERE d.id_solicitud=s.id_solicitud AND e.id_solicitud=s.id_solicitud AND e.num_expediente='"+numExpediente+"'";
                connection= CPoolDatabase.getConnection();
                if (connection != null) {
                    statement = connection.createStatement();
                    rs= statement.executeQuery(query);
                    if (rs.next()) {
                        CExpedienteLicencia expediente= (CExpedienteLicencia)vExpediente.get(0);
                        datosOcupacion= expediente.getDatosOcupacion();
                        datosOcupacion.setFechaInicio(rs.getTimestamp("FECHA_INICIO"));
                        datosOcupacion.setFechaFin(rs.getTimestamp("FECHA_FIN"));
                    }
                }
            }catch (Exception e){safeClose(rs, statement, connection);}

            safeClose(rs, statement, connection);

			CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
			resultadoOperacion.setSolicitudes(vSolicitud);
			resultadoOperacion.setExpedientes(vExpediente);


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
			String sql = "SELECT A.* " +
					" FROM NOTIFICACION A " +
					"where  " +
					" A.NUM_EXPEDIENTE='" + numExpediente;
			logger.info("sql: " + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector notificaciones = new Vector();
			while (rs.next()) {

				CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion(rs.getInt("ID_ESTADO"), "", "");
				CTipoNotificacion tipoNotificacion = new CTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"), "", "");
				CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"));

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


	public static Vector getParcelario(Hashtable hash) {

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

			String sql = "SELECT A.* FROM PARCELARIO A,SOLICITUD_LICENCIA B WHERE A.REF_CATASTRAL IS NOT NULL AND A.ID_SOLICITUD=B.ID_SOLICITUD" + conditions;
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

	public static Vector getEstadosPermitidos(CExpedienteLicencia expedienteLicencia, Enumeration userPerms) {

		try {
            /** Debido a que no hay workflow definido para la aplicación de ocupaciones,
             * los estados permitidos, son los estados disponibles.
             */
			Vector estadosDisponibles = getEstadosDisponibles(expedienteLicencia);
			return estadosDisponibles;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();
		}

	}


	public static Vector getEstadosDisponibles(CExpedienteLicencia expedienteLicencia) {

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

			int id_estado = 0;


			//***************************************
			//** Este es el caso de la creacion, donde aun no existe expediente.
			//******************************************************

			if (expedienteLicencia != null) {
				id_estado = expedienteLicencia.getEstado().getIdEstado();
				logger.info("expedienteLicencia.getSilencioTriggered(): " + expedienteLicencia.getSilencioTriggered());
			}


			String sql = "";
			//***************************************
			//** Caso en que sea durmiente por silencio administrativo
			//******************************************************

			logger.info("id_estado: " + id_estado);
			if (expedienteLicencia != null) logger.info("expedienteLicencia.getSilencioTriggered(): " + expedienteLicencia.getSilencioTriggered());

			if ((id_estado == CConstantesComando.ESTADO_DURMIENTE_OCUPACION) && (expedienteLicencia != null) && (expedienteLicencia.getSilencioTriggered() != null) && (expedienteLicencia.getSilencioTriggered().equals("1"))) {

				sql = "select * from estado_ocupacion where id_estado=" + CConstantesComando.ESTADO_DURMIENTE_OCUPACION;

			} else if (id_estado == CConstantesComando.ESTADO_DURMIENTE_OCUPACION) {

				sql = "select * from estado_ocupacion";

			} else {

				sql = "select b.* from workflow a,estado_ocupacion b where a.id_nextestado=b.id_estado and a.id_estado=" + id_estado;
			}


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector estados = new Vector();
			while (rs.next()) {

				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"), rs.getInt("STEP"));
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
			} */

			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static Vector getEstados() {

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

			String sql = "select * from estado_ocupacion order by id_estado asc";
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			Vector estados = new Vector();
			while (rs.next()) {

				CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), rs.getString("DESCRIPCION"), rs.getString("OBSERVACION"), rs.getInt("STEP"));
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


	public static boolean updateEstadoExpediente(int idEstado, String numExpediente, boolean silencio) {

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
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=?,SILENCIO_TRIGGERED='1' WHERE NUM_EXPEDIENTE=?");
			} else {
				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET ID_ESTADO=?,FECHA_CAMBIO_ESTADO=? WHERE NUM_EXPEDIENTE=?");
			}

			preparedStatement.setInt(1, idEstado);
			preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));

			preparedStatement.setString(3, numExpediente);
			preparedStatement.execute();


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
			preparedStatement.execute();


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


	public static boolean insertHistoricoExpediente(CExpedienteLicencia expedienteLicencia, String user, boolean cambioEstado) {

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

			logger.debug("expedienteLicencia.getIdSolicitud(): " + expedienteLicencia.getIdSolicitud());
			logger.debug("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());

			preparedStatement = connection.prepareStatement("INSERT INTO HISTORICO(ID_HISTORICO,ID_SOLICITUD,NUM_EXPEDIENTE,ID_ESTADO,FECHA,USUARIO,APUNTE, SISTEMA) VALUES (?,?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, expedienteLicencia.getIdSolicitud());
			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
			preparedStatement.setInt(4, expedienteLicencia.getEstado().getIdEstado());
			preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
			preparedStatement.setString(6, user);
            /*
			String aux = "";
			try {
				aux = CConstantesWorkflow.literalesPermisosUsuarioShortOcupaciones[expedienteLicencia.getEstado().getIdEstado()];
			} catch (Exception ex) {

			}
			preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_GENERICO + aux);
            */
            if (cambioEstado){
                preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_CAMBIO_ESTADO);
            }else{
                preparedStatement.setString(7, CWorkflowLiterales.HISTORICO_GENERICO);
            }
            preparedStatement.setString(8, "1");
			preparedStatement.execute();

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




	public static boolean insertEventoExpediente(CExpedienteLicencia expedienteLicencia, int idTipoLicencia,boolean informativo, boolean silencioEvent) {
		
		Connection connection = null;
		connection = CPoolDatabase.getConnection();
		CWorkflowLine workflowLine = COperacionesDatabase.getWorkflowLine(expedienteLicencia.getEstado().getIdEstado(),idTipoLicencia, connection);
		safeClose(null, null, null, connection);
        if (workflowLine!=null)
        	return insertEventoExpediente(expedienteLicencia, workflowLine.getEventText(), informativo, silencioEvent);
        return false;
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

			preparedStatement = connection.prepareStatement("INSERT INTO EVENTOS(ID_EVENTO,ID_SOLICITUD,NUM_EXPEDIENTE,REVISADO,CONTENT,FECHA,ID_ESTADO)VALUES (?,?,?,?,?,?,?)");

			preparedStatement.setLong(1, secuencia);
			preparedStatement.setLong(2, expedienteLicencia.getIdSolicitud());
			preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(4, "0");
			preparedStatement.setString(5, content);
			preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedStatement.setInt(7,expedienteLicencia.getEstado().getIdEstado());

			preparedStatement.execute();
			//**********************************
			//** Si es informativo no haremos nada
			//*********************************************
			if (!informativo) {
				activateEventExpediente(expedienteLicencia.getNumExpediente(), silencioEvent);
				logger.debug("Evento no informativo.Actualizando expediente.");
			}

			//connection.commit();
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
                insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), false);
				return;
			}

			if (estadoAntiguo == CConstantesComando.ESTADO_DURMIENTE_OCUPACION) {

				preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET FECHA_CAMBIO_ESTADO=?,PLAZO_EVENT=NULL,SILENCIO_EVENT=NULL,FECHA_APERTURA=?,SILENCIO_TRIGGERED=? WHERE NUM_EXPEDIENTE=?");

				preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
				preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime() + (CConstantesWorkflow.diasSilencioAdministrativo * 60 * 1000)));
				preparedStatement.setString(3, "0");
				preparedStatement.setString(4, expedienteLicencia.getNumExpediente());
			} else {
                if (estadoNuevo!=CConstantesComando.ESTADO_DURMIENTE_OCUPACION){
				    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET FECHA_CAMBIO_ESTADO=?,PLAZO_EVENT=NULL WHERE NUM_EXPEDIENTE=?");
				    preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
				    preparedStatement.setString(2, expedienteLicencia.getNumExpediente());
                }else{
                    preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET FECHA_CAMBIO_ESTADO=?,PLAZO_EVENT=NULL, ID_FINALIZACION=? WHERE NUM_EXPEDIENTE=?");
				    preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                    preparedStatement.setInt(2,CConstantesComando.TIPO_FINALIZACION_EXPRESA);
				    preparedStatement.setString(3, expedienteLicencia.getNumExpediente());
                }
			}
			preparedStatement.execute();
            logger.info("Hubo cambio de estado. Fecha actualizada.");

            switch (estadoNuevo){
                case CConstantesComando.ESTADO_DURMIENTE_OCUPACION:
                    try{
                        COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia, CConstantesComando.TIPO_OCUPACION, solicitudLicencia.getPropietario().getIdPersona(), solicitudLicencia.getPropietario().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, true);
                         if ((solicitudLicencia.getRepresentante() != null) && (solicitudLicencia.getRepresentante().getDatosNotificacion().getNotificar().equalsIgnoreCase("1"))) {
                             COperacionesDatabase.insertNotificacionExpediente(expedienteLicencia,CConstantesComando.TIPO_OCUPACION,  solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getRepresentante().getDniCif(), CConstantesComando.notificacionResolucionAprobacion, solicitudLicencia, false);
                        }
                    }catch(Exception ex){}

                    insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true);
                    insertEventoExpediente(expedienteLicencia, CConstantesComando.TIPO_OCUPACION,
                            true, false);

                    break;

                default:
                    insertHistoricoExpediente(expedienteLicencia, userPrincipal.getName(), true);
                    insertEventoExpediente(expedienteLicencia, CConstantesComando.TIPO_OCUPACION,
                            true, false);
                    break;
            }

            //********************************
            //** VU
            //**************************************
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


	/**
	 * MODIFICACION
	 */
	public static CResultadoOperacion modificarExpedienteLicencias(CSolicitudLicencia solicitudLicencia, CExpedienteLicencia expedienteLicencia, Principal userPrincipal) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;
		boolean removeRepresentante = false;
		boolean addRepresentante = false;

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
            //**********************************************
            //** ASO   5-mayo-2005
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

            String idMunicipio = solicitudLicencia.getIdMunicipio();
			/** ACTUALIZAR TITULAR Y REPRESENTANTE */
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

            /** CHEQUEAR CAMBIO DE ESTADO */
            checkCambioEstado(connection, solicitudLicencia, expedienteLicencia, userPrincipal);


			//****************************************
			//** Insertamos en SOLICITUD_LICENCIA
			//****************************************************
			preparedStatement = connection.prepareStatement("UPDATE SOLICITUD_LICENCIA SET " +
					"ID_TIPO_LICENCIA=?, ID_TIPO_OBRA=?, NUM_ADMINISTRATIVO=?, " +
					"UNIDAD_TRAMITADORA=?, UNIDAD_DE_REGISTRO=?, MOTIVO=?, ASUNTO=?, " +
					"TASA=?, TIPO_VIA_AFECTA=?, NOMBRE_VIA_AFECTA=?, " +
					"NUMERO_VIA_AFECTA=?, PORTAL_AFECTA=?, PLANTA_AFECTA=?, LETRA_AFECTA=?, CPOSTAL_AFECTA=?, " +
					"OBSERVACIONES=?, REPRESENTANTE=?,NOMBRE_COMERCIAL=?, TECNICO=?, PROMOTOR=?, PROPIETARIO=? WHERE ID_SOLICITUD=" + solicitudLicencia.getIdSolicitud());

			if (solicitudLicencia.getTipoLicencia() != null)
				preparedStatement.setInt(1, solicitudLicencia.getTipoLicencia().getIdTipolicencia());
			else
				preparedStatement.setInt(1, -1);

			if (solicitudLicencia.getTipoObra() != null)
				preparedStatement.setInt(2, solicitudLicencia.getTipoObra().getIdTipoObra());
			else
				preparedStatement.setInt(2, -1);

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
				preparedStatement.setString(17, null);

			preparedStatement.setString(18, solicitudLicencia.getNombreComercial());

			preparedStatement.setString(19, null);

			preparedStatement.setString(20, null);
            preparedStatement.setLong(21, solicitudLicencia.getPropietario().getIdPersona());

			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);


			//****************************************
			//** Actualizamos los datos de la ocupacion
			//****************************************************
			CDatosOcupacion datosOcupacion = solicitudLicencia.getDatosOcupacion();
			preparedStatement = connection.prepareStatement("UPDATE datos_ocupacion SET tipo_ocupacion=?, hora_inicio=?, hora_fin=?, afecta_acera=?, afecta_calzada=?, afecta_aparcamiento=?, num_expediente=?, m2_acera=?, m2_calzada=?, m2_aparcamiento=?, area_ocupacion=?, num_mesas=?, num_sillas=?, necesita_obra=? WHERE id_solicitud=" + solicitudLicencia.getIdSolicitud());
			preparedStatement.setInt(1, datosOcupacion.getTipoOcupacion());

			if (datosOcupacion.getHoraInicio() != null) {
				preparedStatement.setTimestamp(2, new Timestamp(datosOcupacion.getHoraInicio().getTime()));
			} else {
				preparedStatement.setNull(2, java.sql.Types.TIMESTAMP);
			}

			if (datosOcupacion.getHoraFin() != null) {
				preparedStatement.setTimestamp(3, new Timestamp(datosOcupacion.getHoraFin().getTime()));
			} else {
				preparedStatement.setNull(3, java.sql.Types.TIMESTAMP);
			}

			preparedStatement.setString(4, datosOcupacion.getAfectaAcera());
			preparedStatement.setString(5, datosOcupacion.getAfectaCalzada());
			preparedStatement.setString(6, datosOcupacion.getAfectaAparcamiento());
            if (datosOcupacion.getNumExpediente().trim().equalsIgnoreCase("")){
                preparedStatement.setNull(7, java.sql.Types.VARCHAR);
            }else preparedStatement.setString(7, datosOcupacion.getNumExpediente());
			preparedStatement.setDouble(8, datosOcupacion.getM2acera());
			preparedStatement.setDouble(9, datosOcupacion.getM2calzada());
			preparedStatement.setDouble(10, datosOcupacion.getM2aparcamiento());
			preparedStatement.setDouble(11, datosOcupacion.getAreaOcupacion());
			preparedStatement.setInt(12, datosOcupacion.getNumMesas());
			preparedStatement.setInt(13, datosOcupacion.getNumSillas());
			preparedStatement.setString(14, datosOcupacion.getNecesitaObra());
			preparedStatement.execute();
            safeClose(null, null, preparedStatement, null);

			//****************************************
			//** Actualizamos las referencias catastrales
			//****************************************************
			deleteReferenciasCatastrales(connection, solicitudLicencia.getIdSolicitud());

			insertaReferenciasCatastrales(connection, solicitudLicencia.getIdSolicitud(), solicitudLicencia.getReferenciasCatastrales());


			//****************************************
			//** Insertamos los datos de notificacion de las personas juridicas
			//****************************************************
			insertUpdateDatosNotificacion(connection, solicitudLicencia.getPropietario(), solicitudLicencia.getIdSolicitud());
			if (addRepresentante) {
				/** comprobamos si el usuario introducido sin pulsar lupa, tiene datos de notificacion para esa solicitud */
				if (getDatosNotificacionByIdPersonaByIdSolicitud(solicitudLicencia.getRepresentante().getIdPersona(), solicitudLicencia.getIdSolicitud()) == null) {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
				} else {
					insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
				}
			} else {
				insertUpdateDatosNotificacion(connection, solicitudLicencia.getRepresentante(), solicitudLicencia.getIdSolicitud());
			}


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

			//****************************************
			//** Actualizamos en EXPEDIENTE
			//****************************************
            //ASO instroduce el plazo de vencimiento y los días
			preparedStatement = connection.prepareStatement("UPDATE EXPEDIENTE_LICENCIA SET " +
					"ID_TRAMITACION=?, ID_ESTADO=?, SERVICIO_ENCARGADO=?, ASUNTO=?, " +
					"SILENCIO_ADMINISTRATIVO=?, FORMA_INICIO=?, NUM_FOLIOS=?, RESPONSABLE=?, " +
					"OBSERVACIONES=?,CNAE=?, PLAZO_RESOLUCION=?, NUM_DIAS=? WHERE NUM_EXPEDIENTE='" + expedienteLicencia.getNumExpediente() + "'");
			preparedStatement.setInt(1, expedienteLicencia.getTipoTramitacion().getIdTramitacion());
            //preparedStatement.setInt(2, expedienteLicencia.getTipoFinalizacion().getIdFinalizacion());
			preparedStatement.setInt(2, expedienteLicencia.getEstado().getIdEstado());
			preparedStatement.setString(3, expedienteLicencia.getServicioEncargado());
			preparedStatement.setString(4, expedienteLicencia.getAsunto());
			preparedStatement.setString(5, expedienteLicencia.getSilencioAdministrativo());
			preparedStatement.setString(6, expedienteLicencia.getFormaInicio());
			preparedStatement.setInt(7, expedienteLicencia.getNumFolios());
			preparedStatement.setString(8, expedienteLicencia.getResponsable());
			preparedStatement.setString(9, expedienteLicencia.getObservaciones());
			preparedStatement.setString(10, expedienteLicencia.getCNAE());
            preparedStatement.setTimestamp(11, (expedienteLicencia.getPlazoVencimiento()==null?null:new Timestamp(expedienteLicencia.getPlazoVencimiento().getTime())));
            preparedStatement.setInt(12, expedienteLicencia.getNumDias());
			preparedStatement.execute();

            actualizaCallesAfectadas(connection, expedienteLicencia);

			connection.commit();

            /** actualizamos los datos de ocupacion para las concesiones */
            /** Lo hacemos cuando ya se ha hecho commit de lo anterior, o de lo contrario
             * Exception: org.postgresql.util.PSQLException: ERROR: transacción abortada, las consultas serán ignoradas hasta el fin de bloque de transacción */
            try{
                safeClose(null, null, preparedStatement, null);
                preparedStatement= connection.prepareStatement("UPDATE datos_ocupacion SET fecha_inicio=?, fecha_fin=? WHERE id_solicitud=" + solicitudLicencia.getIdSolicitud());
                if (datosOcupacion.getFechaInicio() != null)
                    preparedStatement.setTimestamp(1, new Timestamp(datosOcupacion.getFechaInicio().getTime()));
                else preparedStatement.setNull(1, java.sql.Types.TIMESTAMP);
                if (datosOcupacion.getFechaFin() != null)
                    preparedStatement.setTimestamp(2, new Timestamp(datosOcupacion.getFechaFin().getTime()));
                else preparedStatement.setNull(2, java.sql.Types.TIMESTAMP);
                preparedStatement.execute();
                connection.commit();
            }catch(Exception e){
                safeClose(null, null, preparedStatement, null);
            }

            safeClose(rs, statement, preparedStatement, connection);

            //********************************************
			//** ANEXOS
			//********************************************
			modificaAnexosSolicitud(solicitudLicencia.getIdSolicitud(), solicitudLicencia.getAnexos());

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

	private static void insertUpdateDatosNotificacion(Connection connection, CPersonaJuridicoFisica personaJuridicoFisica, long idSolicitud) throws Exception {

        PreparedStatement preparedStatement= null;

        try {
			if (personaJuridicoFisica == null) {
				logger.info("No se insertan los datos de notificaciones ya que no hay persona");
				return;
			}

			CDatosNotificacion datosNotificacion = personaJuridicoFisica.getDatosNotificacion();
			CDatosNotificacion datosNotificacionSistema = getDatosNotificacionByIdPersonaByIdSolicitud(personaJuridicoFisica.getIdPersona(), idSolicitud);
			if (datosNotificacionSistema == null) {
				logger.info("No se encuentran los datos de notificacion. Inserting.");


				String sql = "INSERT INTO DATOS_NOTIFICACION ( ID_PERSONA, ID_SOLICITUD, ID_VIA_NOTIFICACION, FAX, TELEFONO, MOVIL, EMAIL, TIPO_VIA, NOMBRE_VIA, NUMERO_VIA, PORTAL, PLANTA, ESCALERA, LETRA, CPOSTAL, MUNICIPIO, PROVINCIA, NOTIFICAR ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);

				preparedStatement.setLong(1, personaJuridicoFisica.getIdPersona());
				preparedStatement.setLong(2, idSolicitud);
                if (datosNotificacion.getViaNotificacion() != null)
				    preparedStatement.setInt(3, datosNotificacion.getViaNotificacion().getIdViaNotificacion());
                else preparedStatement.setNull(3, java.sql.Types.INTEGER);
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
                if (datosNotificacion.getNotificar() != null)
				    preparedStatement.setString(18, datosNotificacion.getNotificar());
                else preparedStatement.setNull(18, java.sql.Types.VARCHAR);

				preparedStatement.execute();

				logger.info("DatosNotificacion insertado. personaJuridicoFisica.getDniCif(): " + personaJuridicoFisica.getDniCif());


			} else {
				logger.info("Se encentran los datos de notificacion. Updating.");


				String sql = "UPDATE DATOS_NOTIFICACION SET " +
						"ID_VIA_NOTIFICACION=?, FAX=?, TELEFONO=?, MOVIL=?, EMAIL=?, " +
						"TIPO_VIA=?, NOMBRE_VIA=?, NUMERO_VIA=?, PORTAL=?, PLANTA=?, " +
						"ESCALERA=?, LETRA=?, CPOSTAL=?, MUNICIPIO=?, PROVINCIA=?, " +
						"NOTIFICAR=? WHERE ID_SOLICITUD=" + idSolicitud + " AND ID_PERSONA=" + personaJuridicoFisica.getIdPersona();

				logger.debug("SQL=" + sql);

				preparedStatement = connection.prepareStatement(sql);
                if (datosNotificacion.getViaNotificacion() != null)
				    preparedStatement.setInt(1, datosNotificacion.getViaNotificacion().getIdViaNotificacion());
                else preparedStatement.setNull(1, java.sql.Types.INTEGER);
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
                if (datosNotificacion.getNotificar() != null)
				    preparedStatement.setString(16, datosNotificacion.getNotificar());
                else preparedStatement.setNull(16, java.sql.Types.VARCHAR);

				preparedStatement.execute();
			}

            safeClose(null, null, preparedStatement, null);


		} catch (Exception ex) {
            safeClose(null, null, preparedStatement, null);
			throw ex;
		}
	}

	/**
	 * INFORMES
	 */

	public static CResultadoOperacion getSolicitudesExpedientesInforme(Hashtable hash) {
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
					"EXPEDIENTE_LICENCIA.ID_FINALIZACION as TFIN_IDFIN, "+
                    "EXPEDIENTE_LICENCIA.ID_TRAMITACION as TTRA_IDTRA, "+
                    "EXPEDIENTE_LICENCIA.ID_ESTADO as EST_IDEST, " +
                    "datos_ocupacion.tipo_ocupacion, datos_ocupacion.necesita_obra, datos_ocupacion.num_expediente as num_expediente_licencia_obra, " +
                    "datos_ocupacion.hora_inicio, datos_ocupacion.hora_fin, datos_ocupacion.num_mesas, " +
                    "datos_ocupacion.num_sillas, datos_ocupacion.afecta_acera, datos_ocupacion.afecta_calzada, " +
                    "datos_ocupacion.afecta_aparcamiento, datos_ocupacion.area_ocupacion, " +
                    "datos_ocupacion.m2_acera, datos_ocupacion.m2_calzada, datos_ocupacion.m2_aparcamiento " +
					"from solicitud_licencia, expediente_licencia, persona_juridico_fisica, datos_ocupacion, estado_ocupacion " +
					"where EXPEDIENTE_LICENCIA.ID_ESTADO=ESTADO_OCUPACION.ID_ESTADO and " +
					"EXPEDIENTE_LICENCIA.ID_SOLICITUD=SOLICITUD_LICENCIA.ID_SOLICITUD and " +
					"SOLICITUD_LICENCIA.PROPIETARIO=PERSONA_JURIDICO_FISICA.ID_PERSONA and " +
					"SOLICITUD_LICENCIA.ID_SOLICITUD=DATOS_OCUPACION.ID_SOLICITUD " +
					conditions + " order by EXPEDIENTE_LICENCIA.FECHA_APERTURA ASC";

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					// Solicitud
					// Tipo Licencia
					//CTipoLicencia tipoLicencia = new CTipoLicencia(rs.getInt("TLICEN_IDLICEN"), rs.getString("TLICEN_DESC"), rs.getString("TLICEN_OBSERV"));
					// Tipo Obra
					//CTipoObra tipoObra = new CTipoObra(rs.getInt("TOBRA_IDOBRA"), rs.getString("TOBRA_DESC"), rs.getString("TOBRA_OBSERV"));
					// Propietario
                    CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);

					//CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs.getLong("PROPIETARIO"));
					// Representante
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					// Anexos
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					// Referencias catastrales
					//Vector referenciasCatastales = getReferenciasCatastrales(connection,rs.getLong("ID_SOLICITUD"));

                    // Datos de ocupacion
                    CDatosOcupacion datosOcupacion = new CDatosOcupacion();
                    datosOcupacion.setTipoOcupacion(rs.getInt("TIPO_OCUPACION"));
                    datosOcupacion.setNecesitaObra(rs.getString("NECESITA_OBRA"));
                    datosOcupacion.setNumExpediente(rs.getString("num_expediente_licencia_obra"));
                    datosOcupacion.setHoraInicio(rs.getTimestamp("HORA_INICIO"));
                    datosOcupacion.setHoraFin(rs.getTimestamp("HORA_FIN"));
                    datosOcupacion.setNumMesas(rs.getInt("num_mesas"));
                    datosOcupacion.setNumSillas(rs.getInt("num_sillas"));
                    datosOcupacion.setAfectaAcera(rs.getString("AFECTA_ACERA"));
                    datosOcupacion.setAfectaCalzada(rs.getString("AFECTA_CALZADA"));
                    datosOcupacion.setAfectaAparcamiento(rs.getString("AFECTA_APARCAMIENTO"));
                    datosOcupacion.setAreaOcupacion(rs.getDouble("area_ocupacion"));
                    datosOcupacion.setM2acera(rs.getDouble("m2_acera"));
                    datosOcupacion.setM2calzada(rs.getDouble("m2_calzada"));
                    datosOcupacion.setM2aparcamiento(rs.getDouble("m2_aparcamiento"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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
                    solicitud.setDatosOcupacion(datosOcupacion);
					vSolicitudes.add(i, solicitud);
					// Expediente

					// Estado
					CEstado estado = new CEstado(rs.getInt("EST_IDEST"));
					// Tipo Tramitacion
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("TTRA_IDTRA"), "", "", "");
					// Tipo Finalizacion
                    CTipoFinalizacion tipoFinalizacion=null;
                    if (rs.getString("TFIN_IDFIN")!=null)
					    tipoFinalizacion = new CTipoFinalizacion(rs.getInt("TFIN_IDFIN"), null, null);

					Vector notificaciones = getNotificacionesExpediente(rs.getString("EX_NUMEXP"));

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
							notificaciones);
                    expediente.setDatosOcupacion(datosOcupacion);
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


	public static Vector getPlantillas() {

		try {

			logger.debug("Inicio getPlantillas.");

			Vector vPlantillas = new Vector();

			// It is also possible to filter the list of returned files.
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (name.endsWith(".jrxml"));
				}
			};
			/*
            Connection con = CPoolDatabase.getConnection();
            String _path = null;
            String entorno = null;
            if (CPoolDatabase.isPostgres(con)) {
                entorno = "postgres";
            }
            else {
                entorno = "oracle";
            }
            _path = _pathPlantillas + entorno + File.separator;
			File dir = new File(_path);
			*/
			String path = _pathPlantillas; 
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
//						File file = children[i];
//						CPlantilla plantilla = new CPlantilla(file.getName());
//						vPlantillas.addElement(plantilla);
                        File file = children[i];
                        // FRAN
                        String fname = file.getName();
                     //   String dname = _dname + fname;
                        long ftam = file.length();
                        FileInputStream fis = new FileInputStream(file);
                        byte data[] = new byte[(int)ftam];
                        fis.read(data);
                        String sdata = new String(data);
                      //  String sdef = sdata.replaceAll(CConstantesComando.PATRON_SUSTITUIR_BBDD, bdContext);
//                        FileOutputStream fos = new FileOutputStream(_path+dname);
//                        fos.write(sdef.getBytes());
//                        fos.flush();
//                        fos.close();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(_pathPlantillas+fname));
                        //bw.write(sdef);
                        bw.write(sdata);
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


		public static Vector getNotificacionesMenu(Hashtable hash) {
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
            "E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
            "E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
            "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
            "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
            "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
            "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
            "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
            "E.ID_ESTADO AS ID_ESTADO,  " +
            "E.ID_TRAMITACION AS ID_TRAMITACION,  " +
            "D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
            "A.ID_NOTIFICACION, A.ID_TIPO_NOTIFICACION, A.ID_ALEGACION, A.ID_MEJORA, A.NOTIFICADA_POR," +
            "A.FECHA_CREACION, A.FECHA_NOTIFICACION, A.RESPONSABLE, A.PLAZO_VENCIMIENTO, A.FECHA_REENVIO, A.PROCEDENCIA, " +
            "A.HABILES, A.NUM_DIAS, A.OBSERVACIONES as A_OBSERVACIONES, " +
            "B.ID_ESTADO as B_ID_ESTADO " +
            "from solicitud_licencia S, expediente_licencia E,  " +
            " datos_ocupacion D, persona_juridico_fisica J, " +
            "notificacion A, estado_notificacion B " +
            "where  " +
            // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
            " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
            " E.ID_SOLICITUD=S.ID_SOLICITUD and " +
            "S.ID_SOLICITUD=D.ID_SOLICITUD and " +
            "A.ID_PERSONA=J.ID_PERSONA and S.ID_SOLICITUD=A.ID_SOLICITUD and " +
            "A.ID_ESTADO=B.ID_ESTADO " +
            conditions + " order by FECHA_CREACION";


			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					CTipoNotificacion tipoNotificacion = new CTipoNotificacion();
					tipoNotificacion.setIdTipoNotificacion(rs.getInt("ID_TIPO_NOTIFICACION"));

					CEstadoNotificacion estadoNotificacion = new CEstadoNotificacion();
					estadoNotificacion.setIdEstado(rs.getInt("B_ID_ESTADO"));

					//CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs);
                    CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia();
					solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
                    solicitud.setTipoLicencia(null);

                    // 15-06-2004 charo annade a la solicitud datos de emplazamiento y ref. catastrales.
                    // Necesarios para mostrar el emplazamiento.
                    solicitud.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                    solicitud.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                    solicitud.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));
                    solicitud.setReferenciasCatastrales(getReferenciasCatastrales(connection,rs.getLong("ID_SOLICITUD")));

					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
					solicitud.setDatosOcupacion(datosOcupacion);

					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"));

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


	public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(long idPersona, long idSolicitud) {

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
            /*
			String sql = "select A.*,B.* " +
					"FROM PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B, SOLICITUD_LICENCIA S, VIA_NOTIFICACION V " +
					"where A.ID_PERSONA=" + idPersona + " and A.ID_PERSONA=B.ID_PERSONA and " +
					"B.ID_SOLICITUD=S.ID_SOLICITUD and B.ID_SOLICITUD=" + idSolicitud + " and " +
					"B.ID_VIA_NOTIFICACION=V.ID_VIA_NOTIFICACION";
           */
            String sql = "select A.*,B.* " +
                    "FROM PERSONA_JURIDICO_FISICA A, DATOS_NOTIFICACION B, SOLICITUD_LICENCIA S " +
                    "where A.ID_PERSONA=" + idPersona + " and A.ID_PERSONA=B.ID_PERSONA and " +
                    "B.ID_SOLICITUD=S.ID_SOLICITUD and B.ID_SOLICITUD=" + idSolicitud;

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

	/**
	 * EVENTOS
	 */
	public static CResultadoOperacion getEventos(Hashtable hash, String idMunicipio, String locale) {
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
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					"persona_juridico_fisica J, " +
					"datos_ocupacion D, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
                    conditions + " order by V_FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					"persona_juridico_fisica J, " +
					"datos_ocupacion D, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by V_FECHA DESC";
            }

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(connection, rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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
					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
					solicitud.setDatosOcupacion(datosOcupacion);
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("E_ID_ESTADO"));
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("E_ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
					CTipoFinalizacion tipoFinalizacion =null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                            tipoFinalizacion=new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

					Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

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
							notificaciones);
					vExpedientes.add(i, expediente);

					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
					evento.setContent((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
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


	public static CResultadoOperacion getUltimosEventos(Hashtable hash, String idMunicipio, String locale) {
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
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					" persona_juridico_fisica J, datos_ocupacion D, " +
					//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION, " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					" persona_juridico_fisica J, datos_ocupacion D, " +
					//"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(connection,rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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
					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
					solicitud.setDatosOcupacion(datosOcupacion);
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("E_ID_ESTADO"));
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("E_ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
                    CTipoFinalizacion tipoFinalizacion = null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                            tipoFinalizacion=new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

					Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

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
							notificaciones);
					vExpedientes.add(i, expediente);

					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
					evento.setContent((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
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

    public static CPersonaJuridicoFisica getPersonaJuridicaFromDatabase(ResultSet rs)
    {
        try
        {
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

	public static CResultadoOperacion getEventosSinRevisar(Hashtable hash, String idMunicipio, String locale) {
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
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION,  " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					" persona_juridico_fisica J, " +
					"datos_ocupacion D, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    "E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }
            else {
                sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION,  " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "DICTIONARY.TRADUCCION " +
					"from solicitud_licencia S, expediente_licencia E, " +
					" persona_juridico_fisica J, " +
					"datos_ocupacion D, " +
                    //"eventos V " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +

					"where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    "E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=V.ID_SOLICITUD and " +
					//"E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' " +
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and V.REVISADO='0' and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by E.NUM_EXPEDIENTE, V.FECHA DESC";
            }

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(connection,rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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
					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
					solicitud.setDatosOcupacion(datosOcupacion);
					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("E_ID_ESTADO"));
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("E_ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
					CTipoFinalizacion tipoFinalizacion = null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                           tipoFinalizacion= new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"), null, null);

					Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

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
							notificaciones);
					vExpedientes.add(i, expediente);

					CEvento evento = new CEvento();
					evento.setIdEvento(rs.getLong("ID_EVENTO"));
					evento.setRevisado(rs.getString("REVISADO_POR"));
					evento.setRevisado(rs.getString("REVISADO"));
					evento.setTipoEvento("FALTA_TABLA_EN_BD");
					evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
					evento.setContent((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
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


	private static Vector getEventosExpediente(String numExpediente, String locale) {

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

			String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "SELECT ID_EVENTO, NUM_EXPEDIENTE, REVISADO, REVISADO_POR, CONTENT, FECHA, DICTIONARY.TRADUCCION " +
					//"FROM EVENTOS " +
                    "from EVENTOS LEFT JOIN DICTIONARY ON EVENTOS.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where NUM_EXPEDIENTE='" + numExpediente + "' ORDER BY FECHA DESC";
            }
            else {
                sql = "SELECT ID_EVENTO, NUM_EXPEDIENTE, REVISADO, REVISADO_POR, CONTENT, FECHA, DICTIONARY.TRADUCCION " +
					//"FROM EVENTOS " +
                    "from EVENTOS LEFT JOIN DICTIONARY ON EVENTOS.CONTENT=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
					"where NUM_EXPEDIENTE='" + numExpediente + "' ORDER BY FECHA DESC";
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
				evento.setContent((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));

				evento.setExpediente(expediente);
				eventos.add(evento);

			}

			safeClose(rs, statement, connection);

			logger.debug("eventos: " + eventos);

			return eventos;

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
	 * HISTORICO
	 */

	public static CResultadoOperacion getHistorico(Hashtable hash, String idMunicipio, String locale) {
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
		/*	String sql = "select S.*,J.*," +
					"E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
					"E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
					"E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
					"E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
					"E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
					"E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
					"E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
					"E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
					"EST.ID_ESTADO AS ID_ESTADO, EST.DESCRIPCION AS ESTADO_DESCRIPCION, " +
					"EST.OBSERVACION AS ESTADO_OBSERVACION, EST.STEP AS ESTADO_STEP, " +
					"T.ID_TRAMITACION AS ID_TRAMITACION, T.DESCRIPCION AS T_DESCRIPCION, " +
					"T.OBSERVACION AS T_OBSERVACION, T.PLAZO_ENTREGA AS T_PLAZO_ENTREGA, " +
					"E.ID_FINALIZACION AS ID_FINALIZACION,  " +
					"D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
					"H.ID_HISTORICO, H.ID_ESTADO AS H_ID_ESTADO, H.FECHA AS H_FECHA, " +
					"H.USUARIO AS H_USUARIO, H.APUNTE AS H_APUNTE, H.SISTEMA AS H_SISTEMA " +
					"from solicitud_licencia S, expediente_licencia E, estado_ocupacion EST, tipo_tramitacion T, " +
					" datos_ocupacion D, persona_juridico_fisica J, " +
					"historico H " +
					"where E.ID_ESTADO=EST.ID_ESTADO and E.ID_TRAMITACION=T.ID_TRAMITACION and " +
					" E.ID_SOLICITUD=S.ID_SOLICITUD and " +
					"S.ID_SOLICITUD=D.ID_SOLICITUD and " +
					"S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=H.ID_SOLICITUD and " +
					"E.NUM_EXPEDIENTE=H.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
					conditions + " order by H_FECHA ASC";*/


            String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
                    "E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
                    "E.ID_ESTADO AS ID_ESTADO, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, " +
                    "E.ID_FINALIZACION AS ID_FINALIZACION,  " +
                    "D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
                    "H.ID_HISTORICO, H.ID_ESTADO AS H_ID_ESTADO, H.FECHA AS H_FECHA, " +
                    "H.USUARIO AS H_USUARIO, H.APUNTE AS H_APUNTE, H.SISTEMA AS H_SISTEMA " +
                    //"DICTIONARY.TRADUCCION, DICTIONARY.ID_VOCABLO " +
                    "from solicitud_licencia S, expediente_licencia E, " +
                    " datos_ocupacion D, persona_juridico_fisica J, " +
                    //"historico H " +
                    "historico H "+//LEFT JOIN DICTIONARY ON H.APUNTE=DICTIONARY.ID_VOCABLO " +
                    //"and DICTIONARY.LOCALE='"+locale+"' " +
                    "where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=H.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=H.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
                    conditions + " order by H_FECHA ASC";
            }
            else {
                sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
                    "E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
                    "E.ID_ESTADO AS ID_ESTADO, " +
                    "E.ID_TRAMITACION AS ID_TRAMITACION, " +
                    "E.ID_FINALIZACION AS ID_FINALIZACION,  " +
                    "D.TIPO_OCUPACION AS D_TIPO_OCUPACION, " +
                    "H.ID_HISTORICO, H.ID_ESTADO AS H_ID_ESTADO, H.FECHA AS H_FECHA, " +
                    "H.USUARIO AS H_USUARIO, H.APUNTE AS H_APUNTE, H.SISTEMA AS H_SISTEMA " +
                    //"DICTIONARY.TRADUCCION, DICTIONARY.ID_VOCABLO " +
                    "from solicitud_licencia S, expediente_licencia E, " +
                    " datos_ocupacion D, persona_juridico_fisica J, " +
                    //"historico H " +
                    "historico H " + //LEFT JOIN DICTIONARY ON H.APUNTE=to_char(DICTIONARY.ID_VOCABLO) " +
                    //"and DICTIONARY.LOCALE='"+locale+"' " +

                    "where E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
                    "S.PROPIETARIO=J.ID_PERSONA and S.ID_SOLICITUD=H.ID_SOLICITUD and " +
                    "E.NUM_EXPEDIENTE=H.NUM_EXPEDIENTE and S.id_municipio='" + idMunicipio + "' " +
                    conditions + " order by H_FECHA ASC";
            }

			logger.info("SQL=" + sql);

			statement = connection.createStatement();
			rs = statement.executeQuery(sql);

			int i = 0;
			if (rs.next()) {
				do {
					/** Solicitud */
					/** Propietario */
					CPersonaJuridicoFisica propietario = getPersonaJuridicaFromDatabase(rs);
					/** Representante */
					//CPersonaJuridicoFisica representante = getPersonaJuridicaFromDatabase(rs.getLong("REPRESENTANTE"));
					/** Anexos */
					//Vector anexos = getAnexosSolicitud(rs.getLong("ID_SOLICITUD"));
					/** Referencias catastrales */
					//Vector referenciasCatastales = getReferenciasCatastrales(rs.getLong("ID_SOLICITUD"));

					CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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
					CDatosOcupacion datosOcupacion = new CDatosOcupacion();
					datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
					solicitud.setDatosOcupacion(datosOcupacion);

					vSolicitudes.add(i, solicitud);
					/** Expediente */

					/** Estado */
					CEstado estado = new CEstado(rs.getInt("ID_ESTADO"), "", "", 0);
					/** Tipo Tramitacion */
					CTipoTramitacion tipoTramitacion = new CTipoTramitacion(rs.getInt("ID_TRAMITACION"), "", "", "");
					/** Tipo Finalizacion */
					CTipoFinalizacion tipoFinalizacion = null;
                    if (rs.getString("ID_FINALIZACION")!=null)
                        tipoFinalizacion=new CTipoFinalizacion(rs.getInt("ID_FINALIZACION"),null, null);

					Vector notificaciones = getNotificacionesExpediente(rs.getString("NUM_EXPEDIENTE"));

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
							notificaciones);
					vExpedientes.add(i, expediente);

					CHistorico historico = new CHistorico();
					historico.setIdHistorico(rs.getLong("ID_HISTORICO"));
					historico.setUsuario(rs.getString("H_USUARIO"));
					historico.setFechaHistorico(rs.getTimestamp("H_FECHA"));
                    if (rs.getString("H_SISTEMA").equalsIgnoreCase("1")){
					    //historico.setApunte((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
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

			String sql = "select * from estado_ocupacion where id_estado=" + idEstado;
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

	private static Vector getHistoricoExpediente(String numExpediente, String locale) {

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

            String sql = null;
            if (CPoolDatabase.isPostgres(connection)) {
                sql = "SELECT * FROM HISTORICO LEFT JOIN DICTIONARY ON HISTORICO.APUNTE=DICTIONARY.ID_VOCABLO::text " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
                    "where NUM_EXPEDIENTE='" + numExpediente + "' " +
                    "ORDER BY FECHA DESC";
            }
            else {
                sql = "SELECT * FROM HISTORICO LEFT JOIN DICTIONARY ON HISTORICO.APUNTE=to_char(DICTIONARY.ID_VOCABLO) " +
                    "and DICTIONARY.LOCALE='"+locale+"' " +
                    "where NUM_EXPEDIENTE='" + numExpediente + "' " +
                    "ORDER BY FECHA DESC";
            }

			logger.debug("sql: " + sql);

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
                if (rs.getString("SISTEMA").equalsIgnoreCase("1")){
                   historico.setApunte((rs.getString("TRADUCCION")==null?"":rs.getString("TRADUCCION")));
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

			safeClose(rs, statement, connection);

			return historicos;

		} catch (Exception ex) {

			safeClose(rs, statement, connection);

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
	public static boolean insertHitoTelematico(CExpedienteLicencia expedienteLicencia) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
        PreparedStatement preparedStatement= null;

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

			CWorkflowLine workflowLine = getWorkflowLine(expedienteLicencia.getEstado().getIdEstado());
			String descripcion = workflowLine.getHitoText();

			connection.setAutoCommit(false);

			String sql = "INSERT INTO PVUCTHITOESTEXP ( CNUMEXP , CGUID, CFECHA, CDESCR) VALUES ( ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, expedienteLicencia.getNumExpediente());
			preparedStatement.setString(2, "1");
			preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			preparedStatement.setString(4, descripcion);
			preparedStatement.execute();
			connection.commit();
			logger.info("Hito estado insertado.");


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

	public static boolean insertarNotifTelematico(String numExp, String nifPropietario, String cnom, String ctitulo, String ctitulode, String ctituloar, String crutaacc) {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
        PreparedStatement preparedStatement= null;

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
			preparedStatement.setString(6, "CPROC");
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

    /**
     * Notificaciones Pendientes
     */
    public static Vector getNotificacionesPendientes(String idMunicipio) {
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
                    "E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
                    "E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, " +
                    "E.ID_ESTADO AS E_ID_ESTADO, " +
                    "A.ID_NOTIFICACION, A.ID_ALEGACION, A.ID_MEJORA, A.NOTIFICADA_POR," +
                    "A.FECHA_CREACION, A.FECHA_NOTIFICACION, A.RESPONSABLE, A.PLAZO_VENCIMIENTO, A.FECHA_REENVIO, A.PROCEDENCIA, " +
                    "A.HABILES, A.NUM_DIAS, A.OBSERVACIONES as A_OBSERVACIONES, " +
                    "A.ID_TIPO_NOTIFICACION, A.ID_ESTADO as A_ID_ESTADO, D.TIPO_OCUPACION as D_TIPO_OCUPACION " +
                    "from solicitud_licencia S, expediente_licencia E, " +
                    "persona_juridico_fisica J, datos_ocupacion D, " +
                    "notificacion A " +
                    "where "+
                    //14-04-2005 ASO añade la condicion de que el estado del expediente no sea durmiente
                    /**27-09-2005 charo comenta: Solo se inserta notificacion si el estado del expediente pasa
                     * a estado durmiente. Eliminamos la condicion o de lo contrario, nunca tendriamos
                     * notificaciones pendientes. */
                    //" E.ID_ESTADO !="+ CConstantesComando.ESTADO_DURMIENTE_OCUPACION + " and "+
                    "E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "A.ID_PERSONA=J.ID_PERSONA and S.ID_SOLICITUD=A.ID_SOLICITUD and " +
                    "S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +
                    "S.id_municipio='" + idMunicipio + "' and " +
                    "A.ID_ESTADO != " + CConstantesComando.CMD_NOTIFICACION_NOTIFICADA +
                    " order by PLAZO_VENCIMIENTO DESC";
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

                    CPersonaJuridicoFisica persona = getPersonaJuridicaFromDatabase(rs.getLong("ID_PERSONA"), rs.getLong("ID_SOLICITUD"));

                    CSolicitudLicencia solicitud = new CSolicitudLicencia();
                    solicitud.setIdSolicitud(rs.getLong("ID_SOLICITUD"));
                    CDatosOcupacion datosOcupacion= new CDatosOcupacion();
                    datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
                    solicitud.setDatosOcupacion(datosOcupacion);
                    solicitud.setTipoViaAfecta(rs.getString("TIPO_VIA_AFECTA"));
                    solicitud.setNombreViaAfecta(rs.getString("NOMBRE_VIA_AFECTA"));
                    solicitud.setNumeroViaAfecta(rs.getString("NUMERO_VIA_AFECTA"));

                    CEstado estado = new CEstado();
                    estado.setIdEstado(rs.getInt("E_ID_ESTADO"));

                    CExpedienteLicencia expediente = new CExpedienteLicencia();
                    expediente.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    expediente.setEstado(estado);
                    expediente.setDatosOcupacion(datosOcupacion);

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

    public static CResultadoOperacion getEventosPendientes(String idMunicipio, Enumeration userPerms, String locale) {
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

            String sql = "select S.*,J.*," +
                    "E.NUM_EXPEDIENTE AS NUM_EXPEDIENTE, E.ID_SOLICITUD AS E_ID_SOLICITUD, " +
                    "E.ID_TRAMITACION AS E_ID_TRAMITACION, E.ID_FINALIZACION AS E_ID_FINALIZACION, " +
                    "E.ID_ESTADO AS E_ID_ESTADO, E.SERVICIO_ENCARGADO AS E_SERVICIO_ENCARGADO, " +
                    "E.ASUNTO AS E_ASUNTO, E.SILENCIO_ADMINISTRATIVO AS E_SILENCIO_ADMINISTRATIVO, " +
                    "E.FORMA_INICIO AS E_FORMA_INICIO, E.NUM_FOLIOS AS E_NUM_FOLIOS, " +
                    "E.FECHA_APERTURA AS FECHA_APERTURA, E.RESPONSABLE AS E_RESPONSABLE, " +
                    "E.PLAZO_RESOLUCION AS E_PLAZO_RESOLUCION, E.HABILES AS E_HABILES, " +
                    "E.NUM_DIAS AS E_NUM_DIAS, E.OBSERVACIONES AS E_OBSERVACIONES, E.ID_ESTADO as E_ID_ESTADO, " +
                    "V.ID_EVENTO, V.REVISADO, V.REVISADO_POR, V.CONTENT, V.FECHA AS V_FECHA, " +
                    "V.ID_ESTADO as V_ID_ESTADO, D.TIPO_OCUPACION as D_TIPO_OCUPACION, " +
                    "DICTIONARY.TRADUCCION " +
                    "from solicitud_licencia S, expediente_licencia E, " +
                    "persona_juridico_fisica J, " +
                    "datos_ocupacion D, " +
                    //"eventos V, " +
                    "eventos V LEFT JOIN DICTIONARY ON V.CONTENT=DICTIONARY.ID_VOCABLO::text " +
                    "where " +
                     //14-04-2005 ASO añade la condicion de que el estado del expediente no sea durmiente " +
                    " E.ID_ESTADO !="+ CConstantesComando.ESTADO_DURMIENTE_OCUPACION + " and " +
                    "E.ID_SOLICITUD=S.ID_SOLICITUD and " +
                    "S.PROPIETARIO=J.ID_PERSONA and " +
                    "S.ID_SOLICITUD=D.ID_SOLICITUD and " +
                    // 15-06-2005 charo inserta condicion tipo de licencia de ocupacion
                    " E.APP_STRING='" + CConstantesComando.APP_OCUPACIONES + "' and " +                                         
                    "E.NUM_EXPEDIENTE=V.NUM_EXPEDIENTE and " +
                    "S.id_municipio='" + idMunicipio + "' and " +
                    "V.REVISADO != " + CConstantesComando.CMD_EVENTO_REVISADO +
                    "::text and DICTIONARY.LOCALE='"+locale+"'" +
                    " order by V_FECHA DESC";

            logger.info("SQL=" + sql);

            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            int i = 0;
            if (rs.next()) {
                do {
                    /** Estado */
                    CEstado estado = new CEstado();
                    estado.setIdEstado(rs.getInt("E_ID_ESTADO"));


                    CTipoLicencia tipoLicencia= new CTipoLicencia();
                    tipoLicencia.setIdTipolicencia(rs.getInt("ID_TIPO_LICENCIA"));
                    CPersonaJuridicoFisica propietario= new CPersonaJuridicoFisica();
                    propietario.setIdPersona(rs.getLong("id_persona"));
                    propietario.setDniCif(rs.getString("dni_cif"));
                    propietario.setNombre(rs.getString("nombre"));
                    propietario.setApellido1(rs.getString("apellido1"));
                    propietario.setApellido2(rs.getString("apellido2"));
                    CSolicitudLicencia solicitud = new CSolicitudLicencia(null,
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

                    CDatosOcupacion datosOcupacion= new CDatosOcupacion();
                    datosOcupacion.setTipoOcupacion(rs.getInt("D_TIPO_OCUPACION"));
                    solicitud.setDatosOcupacion(datosOcupacion);

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
                            estado,
                            null);

                    expediente.setDatosOcupacion(datosOcupacion);
                    vExpedientes.add(i, expediente);

                    CEvento evento = new CEvento();
                    evento.setIdEvento(rs.getLong("ID_EVENTO"));
                    CEstado estadoEvento= new CEstado();
                    estadoEvento.setIdEstado(rs.getInt("V_ID_ESTADO"));
                    evento.setEstado(estadoEvento);
                    evento.setRevisado(rs.getString("REVISADO_POR"));
                    evento.setRevisado(rs.getString("REVISADO"));
                    evento.setFechaEvento(rs.getTimestamp("V_FECHA"));
                    //evento.setContent(rs.getString("CONTENT"));
                    evento.setContent((rs.getString("TRADUCCION")==null)?"":rs.getString("TRADUCCION"));
                    evento.setExpediente(expediente);
                    vEventos.add(i, evento);

                } while (rs.next());
            } else {
//                safeClose(rs, statement, connection);
                return new CResultadoOperacion(false, "Búsqueda finalizada sin resultados");
            }

            CResultadoOperacion resultadoOperacion = new CResultadoOperacion(true, "");
            resultadoOperacion.setSolicitudes(vSolicitudes);
            resultadoOperacion.setExpedientes(vExpedientes);
            resultadoOperacion.setVector(vEventos);

//            safeClose(rs, statement, connection);
            return resultadoOperacion;

        } catch (Exception ex) {

//            safeClose(rs, statement, connection);

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
    /* REFACTORIZACION ORACLE
    public static CResultadoOperacion getDireccionMasCercanaOracle(Connection connection,String geometria, String idMunicipio)
        {
                PreparedStatement preparedStatement = null;
                ResultSet rsSQL = null;
                CResultadoOperacion resultado;
                try {
                    logger.debug("Inicio de conseguir la dirección mas cercana:" + geometria);
                    if (connection == null) {
                        logger.warn("No se puede obtener la conexión");
                        return new CResultadoOperacion(false, "No se puede obtener la conexión");
                    }

                    connection.setAutoCommit(false);

                    String sql="select np.id as id, np.id_via as id_via, np.rotulo as rotulo from "+
                    "numeros_policia np where np.id_municipio =? "+
                    "and (MDSYS.SDO_NN(geometry,?, 'sdo_num_res=1')= 'TRUE')";

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, idMunicipio);
                    //añadimos la geometria
                    SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)connection).con);
                    SpatialReference sr= manager.retrieve(srid.getSRID(new Integer(idMunicipio).intValue()));
                    GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);
                    AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)connection).con);
                    AdapterJTS adapterJTS= new AdapterJTS(gFact);
                    ///
                    WKTReader wktReader = new WKTReader();
                    oracle.sdoapi.geom.Geometry geometry =  adapterJTS.importGeometry(wktReader.read(geometria));
                    Object exportedStruct = adaptersdo.exportGeometry(STRUCT.class,geometry);
                    preparedStatement.setObject(2,exportedStruct );
                    rsSQL=preparedStatement.executeQuery();
                    if (!rsSQL.next())
                    {
                            resultado= new CResultadoOperacion(false, "No existe ningún número de policia cercano");
                            logger.info("No existe ningún número de policia cercano ");
                    }
                    else
                    {
                        String rotulo=rsSQL.getString("rotulo");
                        String id=rsSQL.getString("id");
                        String idVia =rsSQL.getString("id_via");
                        safeClose(null, null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("select tipovianormalizadocatastro, nombrecatastro from vias where ID=?");
                        preparedStatement.setString(1, idVia);
                        rsSQL=preparedStatement.executeQuery();
                        if (!rsSQL.next())
                        {
                                resultado= new CResultadoOperacion(false, "No se ha encontrado los datos de la vía");
                                logger.info("no se ha encontrado datos para el id_via "+idVia);
                        }
                        else
                        {
                            NumeroPolicia numeroPolicia = new NumeroPolicia();
                            numeroPolicia.setId(id);
                            numeroPolicia.setId_via(idVia);
                            numeroPolicia.setNombrevia(rsSQL.getString("nombrecatastro"));
                            numeroPolicia.setRotulo(rotulo);
                            numeroPolicia.setTipovia(rsSQL.getString("tipovianormalizadocatastro"));
                            resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
                            Vector aux= new Vector();
                            aux.add(numeroPolicia);
                            resultado.setVector(aux);
                        }

                     }
                    connection.commit();

                } catch (Exception e) {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("ERROR al obtener la direccion mas cercana:" + sw.toString());
                    resultado = new CResultadoOperacion(false, e.getMessage());
                    try {
                        connection.rollback();
                    } catch (Exception ex2) {
                    }
                } finally {
                    safeClose(rsSQL, preparedStatement, connection);
                }
                return resultado;
            }
    */
    public static CResultadoOperacion getDireccionMasCercana(String geometria, String idMunicipio)
    {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet rsSQL = null;
            CResultadoOperacion resultado;
            try {
                logger.debug("Inicio de conseguir la dirección mas cercana:" + geometria);
                connection = CPoolDatabase.getConnection();
                if (connection == null) {
                    logger.warn("No se puede obtener la conexión");
                    return new CResultadoOperacion(false, "No se puede obtener la conexión");
                }
//                if (srid==null) srid=new SRID(Const.SRID_PROPERTIES);
            if (srid == null)
                srid = new SRIDDefecto(Const.SRID_DEFECTO);
                
                if (!CPoolDatabase.isPostgres(connection))
                        //REFACTORIZACION ORACLE return getDireccionMasCercanaOracle(connection, geometria, idMunicipio);
                        return com.geopista.server.administradorCartografia.OracleConnection.getDireccionMasCercanaOracle(connection, geometria, idMunicipio, srid.getSRID());
                connection.setAutoCommit(false);
//                if (srid==null) srid=new SRID(Const.SRID_PROPERTIES);
                preparedStatement = connection.prepareStatement("select asText(centroid(GeometryFromText(?,"+srid.getSRID()+"))) as centro from geometria_ocupacion");
                preparedStatement.setString(1, geometria);
                rsSQL=preparedStatement.executeQuery();
                if (!rsSQL.next())
                {
                    resultado= new CResultadoOperacion(false, "No se ha encontrado la geometria");
                }
                else
                {
                    String centro = rsSQL.getString("centro");
                    safeClose(null, null, preparedStatement, null);
                    preparedStatement = connection.prepareStatement("select distance (\"GEOMETRY\",geometryfromtext(?,"+srid.getSRID()+")) as "+
                                    " distancia, id, id_via, rotulo from numeros_policia where id_municipio =? order by distancia");
                    preparedStatement.setString(1, centro);
                    preparedStatement.setString(2, idMunicipio);
                    rsSQL=preparedStatement.executeQuery();
                    if (!rsSQL.next())
                    {
                            resultado= new CResultadoOperacion(false, "No existe ningún número de policia cercano");
                            logger.info("No existe ningún número de policia cercano ");
                    }
                    else
                    {
                        String rotulo=rsSQL.getString("rotulo");
                        String id=rsSQL.getString("id");
                        String idVia =rsSQL.getString("id_via");
                        safeClose(null, null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("select tipovianormalizadocatastro, nombrecatastro from vias where ID=?");
                        preparedStatement.setString(1, idVia);
                        rsSQL=preparedStatement.executeQuery();
                        if (!rsSQL.next())
                        {
                                resultado= new CResultadoOperacion(false, "No se ha encontrado la geometria");
                                logger.info("no se ha encontrado datos para el id_via "+idVia);
                        }
                        else
                        {
                            NumeroPolicia numeroPolicia = new NumeroPolicia();
                            numeroPolicia.setId(id);
                            numeroPolicia.setId_via(idVia);
                            numeroPolicia.setNombrevia(rsSQL.getString("nombrecatastro"));
                            numeroPolicia.setRotulo(rotulo);
                            numeroPolicia.setTipovia(rsSQL.getString("tipovianormalizadocatastro"));
                            resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
                            Vector aux= new Vector();
                            aux.add(numeroPolicia);
                            resultado.setVector(aux);
                        }

                     }
                }
                connection.commit();

            } catch (Exception e) {
                java.io.StringWriter sw = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                e.printStackTrace(pw);
                logger.error("ERROR al obtener la direccion mas cercana:" + sw.toString());
                resultado = new CResultadoOperacion(false, e.getMessage());
                try {
                    connection.rollback();
                } catch (Exception ex2) {
                }
            } finally {
                safeClose(rsSQL, preparedStatement, connection);
            }
            return resultado;
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


    public static CResultadoOperacion deleteGeometriaOcupacion(String idFeature) {
        Connection connection = null;
        PreparedStatement preparedStatement= null;

        connection = CPoolDatabase.getConnection();
        if (connection == null) {
            logger.warn("Cannot get connection");
            return new CResultadoOperacion(false, "Cannot get connection");
        }

        try {
            String sql= "DELETE FROM GEOMETRIA_OCUPACION WHERE ID="+idFeature;
            preparedStatement= connection.prepareStatement(sql);
            preparedStatement.execute();
            connection.commit();
            safeClose(null, null, preparedStatement, connection);
            return new CResultadoOperacion(true, "");
        } catch (Exception ex) {
            safeClose(null, null, preparedStatement, connection);
            return new CResultadoOperacion(false, "Error al borrar de GEOMETRIA_OCUPACION");
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
			safeClose(rs, preparedStatement, null);
		}

	}

}
