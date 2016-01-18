package com.geopista.server.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.contaminantes.Arbolado;
import com.geopista.protocol.contaminantes.CAnexo;
import com.geopista.protocol.contaminantes.CPlantilla;
import com.geopista.protocol.contaminantes.Contaminante;
import com.geopista.protocol.contaminantes.Historico;
import com.geopista.protocol.contaminantes.Inspeccion;
import com.geopista.protocol.contaminantes.Inspector;
import com.geopista.protocol.contaminantes.NumeroPolicia;
import com.geopista.protocol.contaminantes.PeticionBusquedaHistorico;
import com.geopista.protocol.contaminantes.Residuo;
import com.geopista.protocol.contaminantes.Vertedero;
import com.geopista.protocol.control.Sesion;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.SRIDDefecto;

/* REFACTORIZACION ORACLE
import oracle.sql.STRUCT;
import oracle.sdoapi.sref.SRManager;
import oracle.sdoapi.sref.SpatialReference;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.adapter.AdapterSDO;
import oracle.sdoapi.geom.GeometryFactory;
import org.geotools.data.oracle.attributeio.AdapterJTS;
*/

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


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 14-oct-2004
 * Time: 16:19:13
 */
public class COperacionesContaminantes {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesContaminantes.class);

//	public static SRID srid = null;
    public static SRIDDefecto srid = null;

	public static CResultadoOperacion getListaInspectores(String sIdMunicipio) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de listar los inspectores");
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select id_responsable, apellido1, apellido2, nombre, " +
					" empresa,puesto, direccion, telefono, otrosdatos from" +
					" responsable_inspector where id_municipio=?");
			preparedStatement.setLong(1, Long.valueOf(sIdMunicipio));
			rsSQL = preparedStatement.executeQuery();

			Vector auxVector = new Vector();
			while (rsSQL.next()) {
				Inspector aux = new Inspector();
				aux.setId(String.valueOf(rsSQL.getLong("id_responsable")));
				aux.setApellido1(rsSQL.getString("apellido1"));
				aux.setApellido2(rsSQL.getString("apellido2"));
				aux.setNombre(rsSQL.getString("nombre"));
                aux.setEmpresa(rsSQL.getString("empresa"));
				aux.setDireccion(rsSQL.getString("direccion"));
				aux.setTelefono(rsSQL.getString("telefono"));
				aux.setPuesto(rsSQL.getString("puesto"));
				aux.setOtrosdatos(rsSQL.getString("otrosdatos"));
				auxVector.add(aux);
			}
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);

		} catch (SQLException e) {
			
			logger.error("ERROR al crear la lista de inspectores:" ,e);
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
                if (value!=null && value.length()>0)
                   conditions += " UPPER(" + field + ") like UPPER('%" + value + "%') ";
                else
				    conditions += "" + field + " like '" + value + "%'";

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


	public static Vector getSearchedActividadesContaminantes(Hashtable hash, String idMunicipio) {

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
			String sql = "select id,id_tipo_actividad,id_razon_estudio,num_administrativo,asunto,fecha_inicio,fecha_fin,tipo_via_afecta,nombre_via_afecta,numero_via_afecta,cpostal_afecta,id_municipio from actividad_contaminante where id_municipio=" + idMunicipio + conditions + " order by fecha_inicio desc";


			logger.info("sql: " + sql);


			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			Vector actividadesContaminantes = new Vector();

			while (rs.next()) {

				Contaminante contaminante = new Contaminante(rs.getString("id"),
						String.valueOf(rs.getLong("id_tipo_actividad")),
						String.valueOf(rs.getLong("id_razon_estudio")),
						rs.getString("num_administrativo"),
						rs.getString("asunto"),
						rs.getTimestamp("fecha_inicio"),
						rs.getTimestamp("fecha_fin"),
						rs.getString("tipo_via_afecta"),
						rs.getString("nombre_via_afecta"),
						rs.getString("numero_via_afecta"),
						rs.getString("cpostal_afecta"),
						String.valueOf(rs.getLong("id_municipio")));

				actividadesContaminantes.add(contaminante);


			}
			safeClose(rs, statement, connection);
			logger.debug("actividadesContaminantes: " + actividadesContaminantes);

			return actividadesContaminantes;

		} catch (Exception ex) {
			safeClose(rs, statement, connection);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new Vector();

		}

	}


	public static CResultadoOperacion actualizarInspector(Inspector inspector) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de actualizar el inspector:" + inspector.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("update responsable_inspector set apellido1=?, apellido2=?," +
					"  nombre=?, empresa=?, puesto=? , direccion=?, telefono=?, otrosdatos=? " +
					"  where id_responsable=?");
			preparedStatement.setString(1, inspector.getApellido1());
			preparedStatement.setString(2, inspector.getApellido2());
			preparedStatement.setString(3, inspector.getNombre());
			preparedStatement.setString(4, inspector.getEmpresa());
			preparedStatement.setString(5, inspector.getPuesto());
			preparedStatement.setString(6, inspector.getDireccion());
			preparedStatement.setString(7, inspector.getTelefono());
			preparedStatement.setString(8, inspector.getOtrosdatos());
			preparedStatement.setLong(9, Long.valueOf(inspector.getId()));
			preparedStatement.executeUpdate();
			Vector auxVector = new Vector();
			auxVector.add(inspector);
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);
			connection.commit();

		} catch (SQLException e) {
			
			logger.error("ERROR al actualizar el inspector:" ,e);
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


    public static CResultadoOperacion actualizarActividad(Contaminante actividad, Hashtable fileUploaded) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsSQL = null;
        CResultadoOperacion resultado;

        Vector vAnexosActualizados= null;
        Vector vInspeccionesActualizadas= new Vector();

        try {
            logger.debug("Inicio de actualizar la actividad:" + actividad.getId());
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("No se puede obtener la conexión");
                return new CResultadoOperacion(false, "No se puede obtener la conexión");
            }
            //connection.setAutoCommit(false);

            //Actualizamos la actividad
            preparedStatement = connection.prepareStatement("update actividad_contaminante set id_tipo_actividad=?, id_razon_estudio=?," +
                    "  num_administrativo=?, asunto=?, fecha_inicio=? , fecha_fin=?, tipo_via_afecta=?, " +
                    "  nombre_via_afecta=? , numero_via_afecta=?, cpostal_afecta=? " +
                    "  where id=?");
            preparedStatement.setLong(1, Long.valueOf(actividad.getId_tipo()));
            preparedStatement.setLong(2, Long.valueOf(actividad.getId_razon()));
            preparedStatement.setString(3, actividad.getNumeroAdm());
            preparedStatement.setString(4, actividad.getAsunto());
            if (actividad.getfInicio()!=null)
                preparedStatement.setDate(5, new java.sql.Date(actividad.getfInicio().getTime()));
            else
                preparedStatement.setNull(5,java.sql.Types.DATE);
            if (actividad.getfFin()!=null)
                preparedStatement.setDate(6, new java.sql.Date(actividad.getfFin().getTime()));
            else
                preparedStatement.setNull(6,java.sql.Types.DATE);

            preparedStatement.setString(7, actividad.getTipovia());
            preparedStatement.setString(8, actividad.getNombrevia());
            preparedStatement.setString(9, actividad.getNumerovia());
            preparedStatement.setString(10, actividad.getCpostal());
            preparedStatement.setString(11, actividad.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            safeClose(null, preparedStatement, null);

            /** actutalizamos infractores
             *
             */
            preparedStatement = connection.prepareStatement("delete from r_contaminante_infractor where id_actividad='"+
                    actividad.getId()+"'");
            preparedStatement.execute();
            if (actividad.getInfractores()!=null && actividad.getInfractores().size()>0)
            {
                for (Enumeration e=actividad.getInfractores().elements();e.hasMoreElements();)
                {
                     CPersonaJuridicoFisica persona=(CPersonaJuridicoFisica)e.nextElement();
                     insertUpdatePersonaJuridicoFisica(connection, persona, actividad.getIdMunicipio());
                     safeClose(null, preparedStatement, null);
                     preparedStatement = connection.prepareStatement("insert into  r_contaminante_infractor " +
                             "(id_actividad, id_persona) values ('"+actividad.getId()+"','"+persona.getIdPersona()+"')");
                     preparedStatement.execute();

                }
            }
            connection.commit();

             /** actualizamos las inspecciones */
            if (actividad.getInspecciones()!=null)
            {
                for (Enumeration e=actividad.getInspecciones().elements();e.hasMoreElements();)
                {
                    Inspeccion inspeccion= (Inspeccion)e.nextElement();
                    if (inspeccion.getId()==null)
                    {
                        long lIdInspeccion = CPoolDatabase.getNextValue("INSPECCION", "ID");
                        inspeccion.setId(new Long(lIdInspeccion).toString());
                    }

                    if (inspeccion.getEstado() == com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_DELETED){
                        /** borramos la inspeccion y sus anexos */
                        /** borramos los anexos */
                        Vector vAnexos= inspeccion.getAnexos();
                        if (inspeccion.getAnexos() != null){
                            for (Enumeration e1=inspeccion.getAnexos().elements();e1.hasMoreElements();){
                                CAnexo anexo= (CAnexo)e1.nextElement();
                                try {
                                    String sql = "DELETE FROM ANEXO WHERE ID_INSPECCION=? AND URL_FICHERO=?";
                                    logger.info("DELETE FROM ANEXO WHERE ID_INSPECCION=" + inspeccion.getId() + " AND URL_FICHERO=" + anexo.getFileName());

                                    safeClose(null, preparedStatement, null);
                                    preparedStatement = connection.prepareStatement(sql);
                                    preparedStatement.setLong(1, new Long(inspeccion.getId()).longValue());
                                    preparedStatement.setString(2, anexo.getFileName());
                                    preparedStatement.execute();
                                    connection.commit();

                                    String path = "anexos" + File.separator + "contaminantes" + File.separator + inspeccion.getId() + File.separator;
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
                            }
                        }

                        /** borramos la inspeccion */
                        safeClose(null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("delete from inspeccion where id='"+inspeccion.getId()+"'");
                        preparedStatement.execute();

                    }else if (inspeccion.getEstado() == com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_ADDED){
                        safeClose(null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("insert into inspeccion (id,id_actividad, id_responsable,num_folios," +
                                "fecha_inicio,fecha_fin,fecha_inicio_rec_datos, fecha_fin_rec_datos, num_dias_rec_datos," +
                                "puntos_fijos_medicion, puntos_moviles_medicion, sustancias_contaminantes, concentracion_min," +
                                "concentracion_max, es_zona_latente, motivos_zona_latente, es_zona_saturada, motivos_zona_saturada," +
                                "factores_de_riesgo, medidas_a_adoptar, resultados,observaciones) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        preparedStatement.setLong(1,Long.valueOf(inspeccion.getId()));
                        preparedStatement.setLong(2,Long.valueOf(actividad.getId()));
                        preparedStatement.setLong(3,Long.parseLong(inspeccion.getId_res()));
                        if (inspeccion.getNfolios()==null)
                            preparedStatement.setNull(4,java.sql.Types.NUMERIC);
                        else
                            preparedStatement.setInt(4,inspeccion.getNfolios().intValue());
                        if (inspeccion.getFinicio()==null)
                            preparedStatement.setNull(5,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(5,new  java.sql.Date(inspeccion.getFinicio().getTime()));
                        if (inspeccion.getFfin()==null)
                            preparedStatement.setNull(6,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(6,new  java.sql.Date(inspeccion.getFfin().getTime()));
                        if (inspeccion.getFinidatos()==null)
                            preparedStatement.setNull(7,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(7,new  java.sql.Date(inspeccion.getFinidatos().getTime()));
                        if (inspeccion.getFfindatos()==null)
                            preparedStatement.setNull(8,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(8,new  java.sql.Date(inspeccion.getFfindatos().getTime()));
                        if (inspeccion.getNrec()==null)
                            preparedStatement.setInt(9,java.sql.Types.NUMERIC);
                        else
                            preparedStatement.setInt(9,inspeccion.getNrec().intValue());
                        preparedStatement.setString(10,inspeccion.getPfijos());
                        preparedStatement.setString(11,inspeccion.getPmoviles());
                        preparedStatement.setString(12,inspeccion.getSustancias());
                        preparedStatement.setString(13,inspeccion.getCmin());
                        preparedStatement.setString(14,inspeccion.getCmax());
                        preparedStatement.setInt(15,(inspeccion.isZlatente()?1:0));
                        preparedStatement.setString(16,inspeccion.getMotlantente());
                        preparedStatement.setInt(17,(inspeccion.isZsaturada()?1:0));
                        preparedStatement.setString(18,inspeccion.getMotsaturada());
                        preparedStatement.setString(19,inspeccion.getFriesgo());
                        preparedStatement.setString(20,inspeccion.getMedidas());
                        preparedStatement.setString(21,inspeccion.getResultados());
                        preparedStatement.setString(22,inspeccion.getObs());
                        preparedStatement.execute();
                        connection.commit();

                        /** insertamos los anexos de la nueva inspeccion */
                        if (inspeccion.getAnexos() != null){
                            for (Enumeration e1=inspeccion.getAnexos().elements();e1.hasMoreElements();){
                                CAnexo anexo= (CAnexo)e1.nextElement();
                                try {

                                    String path = "anexos" + File.separator + "contaminantes" + File.separator + inspeccion.getId() + File.separator;
                                    logger.debug("path: " + path);

                                    if (!new File(path).exists()) {
                                        new File(path).mkdirs();
                                    }

                                    FileOutputStream out = new FileOutputStream(path + anexo.getFileName());
                                    /* 
                                    out.write(anexo.getContent());
                                    out.close();
                                    logger.info("Anexo created. path + fileName: " + path + anexo.getFileName());
                                    */

                                    CAnexo anexoUploaded= (CAnexo)fileUploaded.get(anexo.getPath());
                                    if (anexoUploaded != null){
                                        out.write(anexoUploaded.getContent());
                                        logger.info("Anexo created. path + fileName: " + path + anexo.getFileName());
                                    }
                                    out.close();                                    

                                } catch (Exception ex) {
                                    logger.error("Exception: " + ex.toString());
                                }

                                try{
                                    /* NOTA: debido al conflicto generado al eliminar y annadir el mismo archivo (2 entradas para el mismo anexo en BD),
                                    antes de insertar comprobamos que ya exista un anexo con el mismo URL_FICHERO */
                                    siExisteBorrarAnexo(new Long(inspeccion.getId()).longValue(), anexo);

                                    String sql = "INSERT INTO ANEXO ( ID_INSPECCION, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION ) VALUES (?,?,?,?,?)";
                                    safeClose(null, preparedStatement, null);
                                    preparedStatement = connection.prepareStatement(sql);
                                    preparedStatement.setLong(1, new Long(inspeccion.getId()).longValue());
                                    preparedStatement.setLong(2, getTableSequence());
                                    preparedStatement.setLong(3, anexo.getTipoAnexo().getIdTipoAnexo());
                                    preparedStatement.setString(4, anexo.getFileName());
                                    preparedStatement.setString(5, anexo.getObservacion());
                                    preparedStatement.execute();
                                    connection.commit();
                                }catch(Exception ex){
                                    logger.error("Exception: " + ex.toString());
                                }
                            }
                        }
                        inspeccion.setEstado(-1);
                        vInspeccionesActualizadas.addElement(inspeccion);

                    }else if (inspeccion.getEstado() == com.geopista.protocol.contaminantes.CConstantes.CMD_INSPECCION_MODIFIED){
                        /** actualizamos la inspeccion */
                        safeClose(null, preparedStatement, null);
                        preparedStatement = connection.prepareStatement("update inspeccion set id_responsable=?,num_folios=?," +
                                "fecha_inicio=?,fecha_fin=?,fecha_inicio_rec_datos=?, fecha_fin_rec_datos=?, num_dias_rec_datos=?," +
                                "puntos_fijos_medicion=?, puntos_moviles_medicion=?, sustancias_contaminantes=?, concentracion_min=?," +
                                "concentracion_max=?, es_zona_latente=?, motivos_zona_latente=?, es_zona_saturada=?, motivos_zona_saturada=?," +
                                "factores_de_riesgo=?, medidas_a_adoptar=?, resultados=?,observaciones=? where id=? and id_actividad=?");
                        preparedStatement.setLong(1,Long.parseLong(inspeccion.getId_res()));
                        if (inspeccion.getNfolios()==null)
                            preparedStatement.setInt(2,java.sql.Types.NUMERIC);
                        else
                            preparedStatement.setInt(2,inspeccion.getNfolios().intValue());
                        if (inspeccion.getFinicio()==null)
                            preparedStatement.setNull(3,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(3,new  java.sql.Date(inspeccion.getFinicio().getTime()));
                        if (inspeccion.getFfin()==null)
                            preparedStatement.setNull(4,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(4,new  java.sql.Date(inspeccion.getFfin().getTime()));
                        if (inspeccion.getFinidatos()==null)
                            preparedStatement.setNull(5,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(5,new  java.sql.Date(inspeccion.getFinidatos().getTime()));
                        if (inspeccion.getFfindatos()==null)
                            preparedStatement.setNull(6,java.sql.Types.DATE);
                        else
                            preparedStatement.setDate(6,new  java.sql.Date(inspeccion.getFfindatos().getTime()));
                        if (inspeccion.getNrec()==null)
                            preparedStatement.setInt(7,java.sql.Types.NUMERIC);
                        else
                            preparedStatement.setInt(7,inspeccion.getNrec().intValue());
                        preparedStatement.setString(8,inspeccion.getPfijos());
                        preparedStatement.setString(9,inspeccion.getPmoviles());
                        preparedStatement.setString(10,inspeccion.getSustancias());
                        preparedStatement.setString(11,inspeccion.getCmin());
                        preparedStatement.setString(12,inspeccion.getCmax());
                        preparedStatement.setInt(13,(inspeccion.isZlatente()?1:0));
                        preparedStatement.setString(14,inspeccion.getMotlantente());
                        preparedStatement.setInt(15,(inspeccion.isZsaturada()?1:0));
                        preparedStatement.setString(16,inspeccion.getMotsaturada());
                        preparedStatement.setString(17,inspeccion.getFriesgo());
                        preparedStatement.setString(18,inspeccion.getMedidas());
                        preparedStatement.setString(19,inspeccion.getResultados());
                        preparedStatement.setString(20,inspeccion.getObs());
                        preparedStatement.setLong(21,Long.valueOf(inspeccion.getId()));
                        preparedStatement.setLong(22,Long.valueOf(actividad.getId()));
                        preparedStatement.executeUpdate();
                        connection.commit();

                        /** actualizamos los anexos de la inspeccion */
                        vAnexosActualizados= actualizarAnexosInspeccion(inspeccion, fileUploaded);

                        inspeccion.setAnexos(vAnexosActualizados);
                        inspeccion.setEstado(-1);
                        vInspeccionesActualizadas.addElement(inspeccion);
                    }else{
                        vInspeccionesActualizadas.addElement(inspeccion);
                    }

                }
            }
            if (vInspeccionesActualizadas.size() > 0)
                actividad.setInspecciones(vInspeccionesActualizadas);
            else actividad.setInspecciones(null);

            Vector auxVector = new Vector();
            auxVector.add(actividad);
            resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
            resultado.setVector(auxVector);
            //connection.commit();

        } catch (Exception e) {
            logger.error("ERROR al actualizar la actividad:" ,e);
            resultado = new CResultadoOperacion(false, e.getMessage());
            /*
            try {
                connection.rollback();
            } catch (Exception ex2) {} */
        } finally {
            safeClose(rsSQL, preparedStatement, connection);
        }
        return resultado;
    }


    public static Vector actualizarAnexosInspeccion(Inspeccion inspeccion, Hashtable filesUploaded){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        ResultSet rs = null;

        Vector vAux= new Vector();
        try{
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return null;
            }

            if (inspeccion.getAnexos() != null){
                for (Enumeration e1=inspeccion.getAnexos().elements();e1.hasMoreElements();)
                {
                    CAnexo anexo= (CAnexo)e1.nextElement();
                    switch (anexo.getEstado()) {

                        case com.geopista.protocol.contaminantes.CConstantes.CMD_ANEXO_ADDED:

                            try {

                                String path = "anexos" + File.separator + "contaminantes" + File.separator + inspeccion.getId() + File.separator;
                                logger.debug("CMD_ANEXO_ADDED.- path: " + path);

                                if (!new File(path).exists()) {
                                    new File(path).mkdirs();
                                }

                                FileOutputStream out = new FileOutputStream(path + anexo.getFileName());
                                /** recogemos el contenido del file uploaded */
                                CAnexo anexoUploaded= (CAnexo)filesUploaded.get(anexo.getPath());
                                if (anexoUploaded != null){
                                    out.write(anexoUploaded.getContent());
                                }
                                out.close();
                                logger.info("CMD_ANEXO_ADDED.- Anexo created. path + fileName: " + path + anexo.getFileName());

                            } catch (Exception ex) {
                                logger.error("CMD_ANEXO_ADDED.- Exception: " + ex.toString());
                            }

                            try{
                                long id= getTableSequence();

                                String sql = "INSERT INTO ANEXO ( ID_INSPECCION, ID_ANEXO, ID_TIPO_ANEXO , URL_FICHERO , OBSERVACION ) VALUES (?,?,?,?,?)";
                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setLong(1, new Long(inspeccion.getId()).longValue());
                                preparedStatement.setLong(2, id);
                                preparedStatement.setLong(3, anexo.getTipoAnexo().getIdTipoAnexo());
                                preparedStatement.setString(4, anexo.getFileName());
                                preparedStatement.setString(5, anexo.getObservacion());
                                preparedStatement.execute();
                                safeClose(null, preparedStatement, null);
                                connection.commit();

                                anexo.setEstado(-1);
                                anexo.setIdAnexo(id);
                                vAux.addElement(anexo);

                            }catch(Exception ex){
                                logger.error("CMD_ANEXO_ADDED.- Exception: " + ex.toString());
                            }

                            break;

                        case com.geopista.protocol.contaminantes.CConstantes.CMD_ANEXO_DELETED:

                            try {
                                String sql = "DELETE FROM ANEXO WHERE ID_INSPECCION=? AND URL_FICHERO=?";
                                logger.info("DELETE FROM ANEXO WHERE ID_INSPECCION=" + inspeccion.getId() + " AND URL_FICHERO='" + anexo.getFileName()+"'");

                                preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setLong(1, new Long(inspeccion.getId()).longValue());
                                preparedStatement.setString(2, anexo.getFileName());
                                preparedStatement.execute();
                                connection.commit();
                                safeClose(null, preparedStatement, null);

                                String path = "anexos" + File.separator + "contaminantes" + File.separator + inspeccion.getId() + File.separator;
                                logger.debug("CMD_ANEXO_DELETED.-path: " + path);

                                File file = new File(path + anexo.getFileName());
                                if (!file.exists()) {
                                    logger.warn("CMD_ANEXO_DELETED.- File not found. path+anexo.getFileName(): " + path + anexo.getFileName());
                                    continue;

                                }else{
                                    file.delete();
                                    logger.info("CMD_ANEXO_DELETED.- Anexo deleted.");
                                }
                            } catch (Exception ex) {
                                logger.error("CMD_ANEXO_DELETED.- Exception: " + ex.toString());
                            }

                        break;

                    default:
                        logger.info("Not modified. anexo.getFileName(): " + anexo.getFileName());
                        /** Actulizamos el tipo y la descripcion por si han cambiado */
                        updateTipoAnexoSolicitud(connection, new Long(inspeccion.getId()).longValue(), anexo);

                        vAux.addElement(anexo);
                    }
                }
            }
            safeClose(rs, statement, preparedStatement, connection);
            if (vAux.size() > 0)
                return vAux;
            else return null;

        }catch(Exception e){
            safeClose(rs, statement, preparedStatement, connection);
            
            logger.error("Exception: " ,e);
            return null;
        }

    }

    private static boolean safeClose(ResultSet rs, Statement statement, PreparedStatement preparedStatement, Connection connection) {

        try {
           // connection.rollback();
        } catch (Exception ex2) {
        }
        try {
            rs.close();
        } catch (Exception ex2) {
        }
        try {
            statement.close();
        } catch (Exception ex2) {
        }
        try {
            preparedStatement.close();
        } catch (Exception ex2) {
        }
        try {
            connection.close();
            CPoolDatabase.releaseConexion();
        } catch (Exception ex2) {
        }

        return true;
    }

    public static synchronized long getTableSequence() {

        try {

            Thread.sleep(10);

        } catch (Exception ex) {

        }

        long sequence = new java.util.Date().getTime();
        logger.debug("sequence: " + sequence);

        return sequence;

    }


	public static CResultadoOperacion actualizarVertedero(Vertedero vertedero) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de actualizar el vertedero:" + vertedero.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);

			preparedStatement = connection.prepareStatement("update vertedero set tipo=?, titularidad=?," +
					"  gestion_administrativa=?, problemas_existentes=?, capacidad=? , grado_ocupacion=?, " +
					"posibilidad_ampliacion=?, estado_conservacion=? , vida_util=? " +
					"  where ID=?");
			preparedStatement.setString(1, vertedero.getTipo());
			preparedStatement.setString(2, vertedero.getTitularidad());
			preparedStatement.setString(3, vertedero.getgAdm());
			preparedStatement.setString(4, vertedero.getpExistentes());
			if (vertedero.getCapacidad() == null)
				preparedStatement.setString(5, null);
			else
				preparedStatement.setLong(5, vertedero.getCapacidad().longValue());
			if (vertedero.getgOcupacion() == null)
				preparedStatement.setString(6, null);
			else
				preparedStatement.setFloat(6, vertedero.getgOcupacion().floatValue());

			preparedStatement.setString(7, vertedero.getPosiAmplia());
			preparedStatement.setString(8, vertedero.getEstado());
			if (vertedero.getVidaUtil() == null)
				preparedStatement.setString(9, null);
			else
				preparedStatement.setInt(9, vertedero.getVidaUtil().intValue());
			preparedStatement.setString(10, vertedero.getId());
			preparedStatement.executeUpdate();
            safeClose(null, preparedStatement, null);
			preparedStatement = connection.prepareStatement("delete from residuo_municipal where id_vertedero='" + vertedero.getId() + "'");
			preparedStatement.execute();
			if (vertedero.getResiduos() != null) {
				for (Enumeration e = vertedero.getResiduos().elements(); e.hasMoreElements();) {
					Residuo residuo = (Residuo) e.nextElement();
					if (residuo.getId() == null || residuo.getId().length() <= 0) {
						long lIdResiduo = CPoolDatabase.getNextValue("RESIDUO_MUNICIPAL", "ID_CONTENEDOR");
						residuo.setId(new Long(lIdResiduo).toString());
					}

                    safeClose(null, preparedStatement, null);
					preparedStatement = connection.prepareStatement("insert into residuo_municipal " +
							" (id_contenedor, id_vertedero , tipo ,ratio, situacion, media_recoleccion_diaria ," +
							" media_recoleccion_anual) values (?,?,?,?,?,?,?)");
					preparedStatement.setString(1, residuo.getId());
					preparedStatement.setString(2, vertedero.getId());
					preparedStatement.setString(3, residuo.getTipo());
					if (residuo.getRatio() == null)
						preparedStatement.setString(4, null);
					else
						preparedStatement.setFloat(4, residuo.getRatio().floatValue());
					preparedStatement.setString(5, residuo.getSituacion());
					if (residuo.getDiaria() == null)
						preparedStatement.setString(6, null);
					else
						preparedStatement.setLong(6, residuo.getDiaria().longValue());
					if (residuo.getAnual() == null)
						preparedStatement.setString(7, null);
					else
						preparedStatement.setLong(7, residuo.getAnual().longValue());
					preparedStatement.execute();
				}
			}
			Vector auxVector = new Vector();
			auxVector.add(vertedero);
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);
			connection.commit();

		} catch (Exception e) {
			logger.error("ERROR al actualizar el vertedero:" ,e);
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


	public static CResultadoOperacion deleteVertedero(Vertedero vertedero) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
	    try {
			logger.info("Inicio.");

			connection = CPoolDatabase.getConnection();

			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);


			preparedStatement = connection.prepareStatement("delete from residuo_municipal where id_vertedero='" + vertedero.getId() + "'");
			preparedStatement.execute();

            safeClose(null, preparedStatement, null);
			preparedStatement = connection.prepareStatement("delete from vertedero where ID=?");
			preparedStatement.setString(1, vertedero.getId());
			preparedStatement.execute();

			connection.commit();
			safeClose(resultSet, preparedStatement, connection);
			return new CResultadoOperacion(true, "");

		} catch (Exception e) {
			safeClose(resultSet, preparedStatement, connection);
			logger.error("ERROR al borrar el vertedero:" ,e);
			return new CResultadoOperacion(false, e.getMessage());
		}
	}


	public static CResultadoOperacion deleteActividad(Contaminante contaminante) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
	    try {
			logger.info("Inicio.");

			connection = CPoolDatabase.getConnection();

			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);

            /** borramos los anexos */
            if (contaminante.getInspecciones() != null){
                for (Enumeration e= contaminante.getInspecciones().elements(); e.hasMoreElements();){
                    Inspeccion inspeccion=(Inspeccion)e.nextElement();
                    if (inspeccion.getAnexos() != null){
                        /** borramos los anexos en BD y todos los ficheros que existan en la carpeta para cada inspeccion */
                        try {
                            String sql = "DELETE FROM ANEXO WHERE ID_INSPECCION=?";
                            logger.info("DELETE FROM ANEXO WHERE ID_INSPECCION=" + inspeccion.getId());

                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setLong(1, new Long(inspeccion.getId()).longValue());
                            preparedStatement.execute();
                            connection.commit();
                            safeClose(null, preparedStatement, null);

                            String path = "anexos" + File.separator + "contaminantes" + File.separator + inspeccion.getId() + File.separator;
                            logger.debug("ANEXOS_DELETED.-path: " + path);

                            //File file = new File(path + anexo.getFileName());
                            File file = new File(path);
                            if (!file.exists()) {
                                logger.warn("ANEXOS_DELETED.- File not found. path: " + path);
                                continue;

                            }else{
                                file.delete();
                                logger.info("ANEXOS_DELETED.- Anexo deleted.");
                            }
                        } catch (Exception ex) {
                            logger.error("ANEXOS_DELETED.- Exception: " + ex.toString());
                        }
                        preparedStatement.close();
                    }
                }
            }                                                       

            String id=contaminante.getId();
            safeClose(null, preparedStatement, null);
			preparedStatement = connection.prepareStatement("delete from inspeccion where id_actividad=" + id);
			preparedStatement.execute();

            safeClose(null, preparedStatement, null);
			preparedStatement = connection.prepareStatement("delete from actividad_contaminante where id=?");
			preparedStatement.setString(1, id);
			preparedStatement.execute();



			connection.commit();
			safeClose(resultSet, preparedStatement, connection);
			return new CResultadoOperacion(true, "");

		} catch (Exception e) {
			safeClose(resultSet, preparedStatement, connection);
			logger.error("ERROR al borrar el vertedero:",e);
			return new CResultadoOperacion(false, e.getMessage());
		}
	}


	public static CResultadoOperacion actualizarArbolado(Arbolado arbolado) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de actualizar el arbolado:" + arbolado.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}

			logger.info("arbolado.getId(): " + arbolado.getId());
			logger.info("arbolado.getObs(): " + arbolado.getObs());
			logger.info("arbolado.getExt(): " + arbolado.getExt());

			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("update zonas_arboladas set observaciones=?, extension_verde=?," +
                    " fecha_plantacion=?, fecha_ultima_tala=?, plantado_por=?, id_tipo=? where id=?");
			preparedStatement.setString(1, arbolado.getObs());
			preparedStatement.setFloat(2, arbolado.getExt());
            preparedStatement.setTimestamp(3, (arbolado.getFechaPlanta()==null?null: new java.sql.Timestamp(arbolado.getFechaPlanta().getTime())));
            preparedStatement.setTimestamp(4, (arbolado.getFechaUltimaTala()==null?null: new java.sql.Timestamp(arbolado.getFechaUltimaTala().getTime())));
            preparedStatement.setString(5, arbolado.getPlantadoPor());
			preparedStatement.setString(6, arbolado.getIdTipo());
			preparedStatement.setString(7, arbolado.getId());
			preparedStatement.execute();

			Vector auxVector = new Vector();
			auxVector.add(arbolado);
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);
			connection.commit();

		} catch (SQLException e) {
			logger.error("ERROR al actualizar el arbolado:",e);
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


	public static boolean safeClose(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {

		try {
			resultSet.close();
		} catch (Exception ex2) {
		}
		try {
			preparedStatement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}
		return true;

	}

	private static boolean safeClose(ResultSet resultSet, Statement statement, Connection connection) {

		try {
			resultSet.close();
		} catch (Exception ex2) {
		}
		try {
			statement.close();
		} catch (Exception ex2) {
		}
		try {
			connection.close();
			CPoolDatabase.releaseConexion();
		} catch (Exception ex2) {
		}
		return true;

	}


	public static CResultadoOperacion crearInspector(Inspector inspector, String id_municipio) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de crear el inspector:" + inspector.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			long lIdInspector = CPoolDatabase.getNextValue("RESPONSABLE_INSPECTOR", "ID_RESPONSABLE");
			inspector.setId(new Long(lIdInspector).toString());
			preparedStatement = connection.prepareStatement("insert into responsable_inspector (id_responsable," +
					" apellido1, apellido2, nombre, empresa, puesto, direccion, telefono, otrosdatos, " +
					" id_municipio) values (?,?,?,?,?,?,?,?,?,?)");
			preparedStatement.setLong(1, Long.valueOf(inspector.getId()));
			preparedStatement.setString(2, inspector.getApellido1());
			preparedStatement.setString(3, inspector.getApellido2());
			preparedStatement.setString(4, inspector.getNombre());
			preparedStatement.setString(5, inspector.getEmpresa());
			preparedStatement.setString(6, inspector.getPuesto());
			preparedStatement.setString(7, inspector.getDireccion());
			preparedStatement.setString(8, inspector.getTelefono());
			preparedStatement.setString(9, inspector.getOtrosdatos());
			preparedStatement.setLong(10, Long.valueOf(id_municipio));
			preparedStatement.executeUpdate();
			Vector auxVector = new Vector();
			auxVector.add(inspector);
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);
			connection.commit();

		} catch (Exception e) {
			logger.error("ERROR al crear el inspector:",e);
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

/*
	public static CResultadoOperacion crearArbolado(Arbolado arbolado, String id_arbolado) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de crear el inspector:" + arbolado.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			long lIdArbolado = CPoolDatabase.getNextValue("ZONA_ARBOLADA", "ID_ARBOLADO");
			arbolado.setId(new Long(lIdArbolado).toString());
			preparedStatement = connection.prepareStatement("insert into ZONA_ARBOLADA (id_arbolado, extension_verde, observaciones) values (?,?,?)");
			preparedStatement.setString(1, arbolado.getId());
			preparedStatement.setFloat(2, arbolado.getExt());
			preparedStatement.setString(3, arbolado.getObs());
			preparedStatement.execute();
			Vector auxVector = new Vector();
			auxVector.add(arbolado);
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(auxVector);
			connection.commit();

		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("ERROR al crear el inspector:" + sw.toString());
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

	public static CResultadoOperacion borrarInspector(Inspector inspector) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de borrar el inspector:" + inspector.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("delete from responsable_inspector " +
					" where id_responsable=?");
			preparedStatement.setLong(1, Long.valueOf(inspector.getId()));
			preparedStatement.execute();
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			connection.commit();

		} catch (Exception e) {
			logger.error("ERROR al borrar el inspector:" ,e);
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


	public static CResultadoOperacion borrarArbolado(Arbolado arbolado) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de borrar el arbolado:" + arbolado.getId());
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("delete from ZONAS_ARBOLADAS where id=?");
			preparedStatement.setString(1, arbolado.getId());
			preparedStatement.execute();
			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			connection.commit();

		} catch (Exception e) {
			logger.error("ERROR al borrar el inspector:" ,e);
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


	public static CResultadoOperacion getPlantillas(String path) {

		try {

			Vector vPlantillas = new Vector();
			CResultadoOperacion resultado;

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
//						CPlantilla plantilla = new CPlantilla(file.getName());
//						vPlantillas.addElement(plantilla);
                        // FRAN
                        String fname = file.getName();
                        // String dname = _dname + fname;
                        long ftam = file.length();
                        FileInputStream fis = new FileInputStream(file);
                        byte data[] = new byte[(int)ftam];
                        fis.read(data);
                        String sdata = new String(data);
                        //String sdef = sdata.replaceAll(CConstantesComando.PATRON_SUSTITUIR_BBDD, bdContext);
                        FileOutputStream fos = new FileOutputStream(path+fname);
                        //fos.write(sdef.getBytes());
                        fos.write(sdata.getBytes());
                        fos.flush();
                        fos.close();
                        //int pos = sdata.indexOf();
                        // FRAN
						CPlantilla plantilla = new CPlantilla(fname);//file.getName());
						vPlantillas.addElement(plantilla);
					}
				}
			}

			resultado = new CResultadoOperacion(true, "Operación ejecutada correctamente");
			resultado.setVector(vPlantillas);

			return resultado;

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, ex.getMessage());
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
    public static CResultadoOperacion getDireccionMasCercana(String idContaminante, String idMunicipio)
     {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de conseguir la dirección mas cercana:" + idContaminante);
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
//            if (srid==null) srid=new SRID(Const.SRID_PROPERTIES);
            if (srid == null)
                srid = new SRIDDefecto(Const.SRID_DEFECTO);
			
            if (!CPoolDatabase.isPostgres(connection))
                // REFACTORIZACION ORACLE return getDireccionMasCercanaOracle(connection, idContaminante, new Integer(idMunicipio).intValue());
                return com.geopista.server.administradorCartografia.OracleConnection.getDireccionMasCercanaContaminantesOracle(connection, idContaminante, new Integer(idMunicipio).intValue(), srid.getSRID());
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select asText(centroid(\"GEOMETRY\")) as centro from actividad_contaminante where id=?");
			preparedStatement.setString(1, idContaminante);
            rsSQL=preparedStatement.executeQuery();
            if (!rsSQL.next())
            {
                resultado= new CResultadoOperacion(false, "No se ha encontrado la geometria");
            }
            else
            {
                String centro = rsSQL.getString("centro");
                safeClose(null, preparedStatement, null);
                preparedStatement = connection.prepareStatement("select distance (\"GEOMETRY\",geometryfromtext(?,"+srid.getSRID()+")) as "+
                                " distancia, id, id_via, rotulo from numeros_policia where id_municipio =? order by distancia");
                preparedStatement.setString(1, centro);
                preparedStatement.setLong(2, Long.valueOf(idMunicipio));
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
                    safeClose(null, preparedStatement, null);
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
			logger.error("ERROR al obtener la direccion mas cercana:" ,e);
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

    /* REFACTORIZACION ORACLE
    public static CResultadoOperacion getDireccionMasCercanaOracle(Connection connection, String idContaminante, int idMunicipio)
     {
		PreparedStatement preparedStatement = null;
		ResultSet rsSQL = null;
		CResultadoOperacion resultado;
		try {
			logger.debug("Inicio de conseguir la dirección mas cercana Oracle:" + idContaminante);
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
				return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select GEOMETRY from actividad_contaminante where id=?");
            ////
            SRManager manager = OraSpatialManager.getSpatialReferenceManager((oracle.jdbc.OracleConnection)((org.enhydra.jdbc.core.CoreConnection)connection).con);
            SpatialReference sr= manager.retrieve(srid.getSRID(idMunicipio));
            GeometryFactory gFact= OraSpatialManager.getGeometryFactory(sr);
            AdapterSDO adaptersdo= new AdapterSDO(gFact,(oracle.jdbc.OracleConnection) ((org.enhydra.jdbc.core.CoreConnection)connection).con);
            AdapterJTS adapterJTS= new AdapterJTS(gFact);
            ///
			preparedStatement.setString(1, idContaminante);
            rsSQL=preparedStatement.executeQuery();
            if (!rsSQL.next())
            {
                resultado= new CResultadoOperacion(false, "No se ha encontrado la geometria");
            }
            else
            {
                com.vividsolutions.jts.geom.Geometry jts  =(com.vividsolutions.jts.geom.Geometry) adapterJTS.exportGeometry(com.vividsolutions.jts.geom.Geometry.class,
                adaptersdo.importGeometry(rsSQL.getObject("GEOMETRY")));
                preparedStatement.close();

                String sql="select np.id as id, np.id_via as id_via, np.rotulo as rotulo from "+
                "numeros_policia np where np.id_municipio =? "+
                "and (MDSYS.SDO_NN(geometry,?, 'sdo_num_res=1')= 'TRUE')";
                safeClose(null, preparedStatement, null);
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, idMunicipio);
                preparedStatement.setObject(2, new OracleConnection(srid).getGeoObject(idMunicipio,jts, connection ));
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
                    preparedStatement.close();
                    safeClose(null, preparedStatement, null);
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
    */

    private static boolean siExisteBorrarAnexo(long idInspeccion, CAnexo anexo) {

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

            String sql= "SELECT * FROM ANEXO WHERE ID_INSPECCION=" + idInspeccion +" AND URL_FICHERO='"+anexo.getFileName()+ "'";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {

                sql = "DELETE FROM ANEXO WHERE ID_INSPECCION=? AND URL_FICHERO=?";
                logger.info("DELETE FROM ANEXO WHERE ID_INSPECCION=" + idInspeccion + " AND URL_FICHERO=" + anexo.getFileName());

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, idInspeccion);
                preparedStatement.setString(2, anexo.getFileName());
                preparedStatement.execute();
                connection.commit();

            }

            safeClose(rs, statement, preparedStatement, connection);
            return true;

        } catch (Exception ex) {

            safeClose(rs, statement, preparedStatement, connection);
            logger.error("Exception: " ,ex);
            return false;

        }

    }

    private static void updateTipoAnexoSolicitud(Connection connection, long idInspeccion, CAnexo anexo) throws Exception {

        PreparedStatement preparedStatement= null;
        try {

            String sql = "UPDATE ANEXO SET ID_TIPO_ANEXO=?, OBSERVACION=? WHERE ID_INSPECCION=" + idInspeccion + " and ID_ANEXO=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, anexo.getTipoAnexo().getIdTipoAnexo());
            preparedStatement.setString(2, anexo.getObservacion());
            preparedStatement.setLong(3, anexo.getIdAnexo());
            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();

        } catch (Exception ex) {
            try{
                preparedStatement.close();
            }catch(Exception e){}
            throw ex;
        }
    }
    public static CResultadoOperacion insertarHistorico(Historico historico, Sesion sesion) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        CResultadoOperacion resultado;
        try {
              logger.debug("Inicio insertar historico");
              connection = CPoolDatabase.getConnection();
              if (connection == null) {
                  logger.warn("No se puede obtener la conexión");
                  return new CResultadoOperacion(false, "No se puede obtener la conexión");
              }
              connection.setAutoCommit(false);
               ps = connection.prepareStatement("insert into historicomedioambiental (id_historico, id_elemento," +
                      " fecha, usuario, accion, tipo_medioambiental, apunte, sistema, id_municipio) " +
                      " values (?,?,?,?,?,?,?,?,?)");
                historico.setId_historico(new java.util.Date().getTime());
                ps.setLong(1,historico.getId_historico());
                ps.setInt(2,historico.getId_elemento());
                historico.setFecha(new java.util.Date());
                ps.setTimestamp(3, new java.sql.Timestamp(historico.getFecha().getTime()));
                ps.setString(4,sesion.getUserPrincipal().getName());
                ps.setInt(5,historico.getAccion());
                ps.setInt(6,historico.getTipo_medioambiental());
                ps.setString(7,historico.getApunte());
                ps.setInt(8,historico.getSistema());
                ps.setLong(9, Long.valueOf(sesion.getIdMunicipio()));
                ps.execute();
                connection.commit();
                Vector auxVector= new Vector();
                auxVector.add(historico);
                resultado= new CResultadoOperacion(true, "Funcion inserción realizada correctamente");
                resultado.setVector(auxVector);
            } catch (Exception e) {
                resultado= new CResultadoOperacion(false, e.getMessage());
                logger.error("EXCEPTION:" ,e);
            } finally {
            safeClose(null, ps, connection);
            }
            return resultado;
        }
    //Solo se puede modificar el apunte
     public static CResultadoOperacion modificarHistorico(Historico historico) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        CResultadoOperacion resultado;
        try {
              logger.debug("Inicio modificar historico");
              connection = CPoolDatabase.getConnection();
              if (connection == null) {
                  logger.warn("No se puede obtener la conexión para modificar el historico");
                  return new CResultadoOperacion(false, "No se puede obtener la conexión");
              }
              connection.setAutoCommit(false);
               ps = connection.prepareStatement("update historicomedioambiental set apunte=? where id_historico=?");
                ps.setString(1,historico.getApunte());
                ps.setLong(2,historico.getId_historico());
                ps.execute();
                connection.commit();
                resultado= new CResultadoOperacion(true, "Funcion modificacion realizada correctamente");
            } catch (Exception e) {
                resultado= new CResultadoOperacion(false, e.getMessage());
                logger.error("EXCEPTION:" ,e);
            } finally {
            safeClose(null, ps, connection);
            }
            return resultado;
        }
     //Solo se puede modificar el apunte
     public static CResultadoOperacion borrarHistorico(Historico historico) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        CResultadoOperacion resultado;
        try {
              logger.debug("Inicio borrar historico");
              connection = CPoolDatabase.getConnection();
              if (connection == null) {
                  logger.warn("No se puede obtener la conexión para borrar el historico");
                  return new CResultadoOperacion(false, "No se puede obtener la conexión");
              }
              connection.setAutoCommit(false);
               ps = connection.prepareStatement("delete from historicomedioambiental where id_historico=?");
                ps.setLong(1,historico.getId_historico());
                ps.execute();
                connection.commit();
                resultado= new CResultadoOperacion(true, "Funcion borrado realizada correctamente");
            } catch (Exception e) {
                resultado= new CResultadoOperacion(false, e.getMessage());
                logger.error("EXCEPTION:" ,e);
            } finally {
            safeClose(null, ps, connection);
            }
            return resultado;
        }
    public static CResultadoOperacion getListaHistorico(PeticionBusquedaHistorico peticion, Sesion sesion) throws Exception
    {
               Connection connection = null;
               PreparedStatement preparedStatement = null;
               ResultSet rsSQL=null;
               CResultadoOperacion resultado;
               try
               {
                   logger.debug("Inicio de listar metadatos");
                   connection = CPoolDatabase.getConnection();
                   if (connection == null)
                   {
                          logger.warn("No se puede obtener la conexión");
                           return new CResultadoOperacion(false, "No se puede obtener la conexión");
                   }
                   try{connection.setAutoCommit(false);}catch(Exception e){};
                   String sql= "Select id_historico,  id_elemento, "+
                           "  fecha ,usuario , accion, tipo_medioambiental, " +
                           "  apunte,  sistema from historicomedioambiental where id_municipio= ?";

                   if (peticion.getAccion()!=null)
                       sql=sql+" and accion = ? ";
                   if (peticion.getTipo()!=null)
                       sql=sql+" and tipo_medioambiental=? ";
                   if (peticion.getFechaDesde()!=null)
                       sql=sql+" and fecha>? ";
                   if (peticion.getFechaHasta()!=null)
                                    sql=sql+" and fecha<? ";

                   sql=sql+" order by fecha desc";
                   preparedStatement = connection.prepareStatement(sql);
                   preparedStatement.setLong(1,Long.valueOf(sesion.getIdMunicipio()));
                   int iPara=2;
                   if (peticion.getAccion()!=null)
                   {
                       preparedStatement.setInt(iPara,peticion.getAccion().intValue());
                       iPara++;
                   }
                   if (peticion.getTipo()!=null)
                   {
                       preparedStatement.setInt(iPara,peticion.getTipo().intValue());
                       iPara++;
                   }
                   if (peticion.getFechaDesde()!=null)
                   {
                       preparedStatement.setDate(iPara,new java.sql.Date(peticion.getFechaDesde().getTime()));
                       iPara++;
                   }
                   if (peticion.getFechaHasta()!=null)
                  {
                      preparedStatement.setDate(iPara,new java.sql.Date(peticion.getFechaHasta().getTime()));
                      iPara++;
                  }

                   rsSQL=preparedStatement.executeQuery();
                   Vector auxVector = new Vector();
                   while (rsSQL.next())
                   {
                         Historico historico = new Historico();
                         historico.setId_historico(rsSQL.getLong("id_historico"));
                         historico.setId_elemento((rsSQL.getString("id_elemento")!=null?rsSQL.getInt("id_elemento"):-1));
                         historico.setFecha(rsSQL.getTimestamp("fecha"));
                         historico.setNombre_Usuario(rsSQL.getString("usuario"));
                         historico.setAccion(rsSQL.getInt("accion"));
                         historico.setTipo_medioambiental(rsSQL.getInt("tipo_medioambiental"));
                         historico.setApunte(rsSQL.getString("apunte"));
                         historico.setSistema(rsSQL.getInt("sistema"));
                         auxVector.add(historico);
                   }

                   resultado=new CResultadoOperacion(true, "Operación ejecutada correctamente");
                   resultado.setVector(auxVector);

             }catch(SQLException e)
             {
                  logger.error("ERROR al listar parcialmente los metadatos:",e);
                  resultado=new CResultadoOperacion(false, e.getMessage());
                  try {connection.rollback();} catch (Exception ex2) {}
             }finally{
                    safeClose(rsSQL,preparedStatement, connection);
                   }
                  return resultado;
              }


        public static CPersonaJuridicoFisica insertUpdatePersonaJuridicoFisica(Connection connection, CPersonaJuridicoFisica personaJuridicoFisica, String idMunicipio) throws Exception
        {
            PreparedStatement preparedStatement = null;
            Statement statement = null;
            ResultSet rs = null;
            try {
                if (personaJuridicoFisica == null) {
                    logger.info("PersonaJuridicoFisica no insertada/modificada. personaJuridicoFisica: " + personaJuridicoFisica);
                    return null;
                }

                if (personaJuridicoFisica.getDniCif() != null) {
                    personaJuridicoFisica.setDniCif(personaJuridicoFisica.getDniCif().toUpperCase());
                }

                logger.info("personaJuridicoFisica.getDniCif(): " + personaJuridicoFisica.getDniCif());
                CPersonaJuridicoFisica aux = COperacionesDatabase.getPersonaJuridicaFromDatabase(personaJuridicoFisica.getDniCif(), connection, idMunicipio);

                if (aux != null) {
                    logger.info("Persona already exists. Updating.");
                    personaJuridicoFisica.setIdPersona(aux.getIdPersona());
                    preparedStatement = connection.prepareStatement("UPDATE PERSONA_JURIDICO_FISICA SET " +
                            "DNI_CIF=?, NOMBRE=?,APELLIDO1=?,APELLIDO2=?, id_municipio=? " +
                            " WHERE ID_PERSONA=" + aux.getIdPersona());

                    preparedStatement.setString(1, personaJuridicoFisica.getDniCif());
                    preparedStatement.setString(2, personaJuridicoFisica.getNombre());
                    preparedStatement.setString(3, personaJuridicoFisica.getApellido1());
                    preparedStatement.setString(4, personaJuridicoFisica.getApellido2());
                    preparedStatement.setLong(5, Long.valueOf(idMunicipio));
                    preparedStatement.execute();


                } else {

                    logger.info("Persona does not exists. Inserting.");
                    long secuencia = getTableSequence();
                    personaJuridicoFisica.setIdPersona(secuencia);
                    preparedStatement = connection.prepareStatement("insert into PERSONA_JURIDICO_FISICA(ID_PERSONA,DNI_CIF,NOMBRE,APELLIDO1,APELLIDO2, id_municipio) VALUES(?,?,?,?,?,?)");
                    preparedStatement.setLong(1, secuencia);
                    preparedStatement.setString(2, personaJuridicoFisica.getDniCif());
                    preparedStatement.setString(3, personaJuridicoFisica.getNombre());
                    preparedStatement.setString(4, personaJuridicoFisica.getApellido1());
                    preparedStatement.setString(5, personaJuridicoFisica.getApellido2());
                    preparedStatement.setLong(6, Long.valueOf(idMunicipio));
                    preparedStatement.execute();

                }

                safeClose(rs, statement, preparedStatement, null);
                return personaJuridicoFisica;

            } catch (Exception ex) {
                safeClose(rs, statement, preparedStatement, null);
                throw ex;
            }


	}

}
