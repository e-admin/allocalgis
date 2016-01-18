package com.geopista.server.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.SortedMap;


import com.geopista.server.eiel.constantes.ConstantesServerGISEIEL;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.FeatureEIELSimple;
import com.geopista.app.eiel.beans.GenericFieldEIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.VersionEiel;
import com.geopista.app.eiel.beans.WorkflowEIEL;
import com.geopista.app.eiel.beans.filter.CampoFiltroModel;
import com.geopista.protocol.control.Sesion;
import com.geopista.server.administradorCartografia.Const;

public class COperacionesAlfanumericasEIEL {

	
	protected String revisionExpiradaNula = Const.REVISION_EXPIRADA+" =9999999999  ";
	//protected String revisionExpiradaTemporal = Const.REVISION_EXPIRADA+" =8999999999  ";
	
	protected String versionActual = "(select coalesce(max(revision),1) from versionesAlfa where fecha<localtimestamp)";
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(COperacionesAlfanumericasEIEL.class);
	
	public static boolean safeClose(ResultSet rs, Statement statement,
			Connection connection) {

		try {
			connection.commit();
		} catch (Exception ex2) {
		}
		try {
			if (rs!=null)
				rs.close();
		} catch (Exception ex2) {
		}
		try {
			if (statement!=null)
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

	private static boolean safeClose(ResultSet rs, Statement statement,
			PreparedStatement preparedStatement, Connection connection) {

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
	 /***************************************/
    /**
     * Actualiza la tabla versiones alfanumericas
     */
	protected void beforeRequest(Connection conn, String idUser, String tabla, String tipo) throws Exception {
		beforeRequest(conn, true, idUser, tabla, tipo);		
	}
	protected void beforeRequest(Connection conn,boolean sig, String idUser, String tabla, String tipo) throws Exception {
		PreparedStatement ps = null;
		String sSQL ;
		sSQL  = "insert into versionesAlfa (revision,id_autor,fecha,id_table_versionada, tipocambio) values (nextval('\"seq_versionesalfa\"'),?,(select date_trunc('second', localtimestamp)),?,?);";
    	ps = conn.prepareStatement(sSQL);
    	ps.setInt(1, Integer.valueOf(idUser));
    	ps.setString(2, tabla);
    	ps.setString(3, tipo);
    	if (logger.isDebugEnabled()){
    		logger.debug("Añadiendo a la tabla VersionesAlfa: "+sSQL);
    		logger.debug("Parametros: User: "+idUser+" Tabla:"+tabla+"Modo: "+tipo);
    	}
		ps.execute();
		ps.close();
	}
	
	public ArrayList obtenerListaEmisarios(String filtro, String idMunicipio,boolean noGeoReferenciado, boolean version, Object element, int idMunicipioSeleccionado)  throws Exception{

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList lstDatos = new ArrayList();

		try {

			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				return null;
			}
 
			String sql = "select eiel_t_saneam_tem.revision_actual, eiel_t_saneam_tem.revision_expirada,eiel_t_saneam_tem.clave,eiel_t_saneam_tem.codprov,eiel_t_saneam_tem.codmunic, "
					+"eiel_t_saneam_tem.tramo_em,eiel_t_saneam_tem.titular,eiel_t_saneam_tem.gestor, eiel_t_saneam_tem.estado, "
					+"eiel_t_saneam_tem.material,eiel_t_saneam_tem.sist_impulsion,eiel_t_saneam_tem.tipo_red_interior, "
					+" eiel_t_saneam_tem.fecha_inst,eiel_t_saneam_tem.observ,eiel_t_saneam_tem.fecha_revision,eiel_t_saneam_tem.estado_revision,eiel_t_saneam_tem.bloqueado ";
			if(noGeoReferenciado)	
				sql+=", ST_IsValid(eiel_c_saneam_tem.\"GEOMETRY\")AS valida ";
			
			if (version)
				sql+=", versionesalfa.revision, iuseruserhdr.name, versionesalfa.fecha, versionesalfa.tipocambio  ";
			sql+=" from eiel_t_saneam_tem ";
			if(noGeoReferenciado)
				sql+="left join eiel_c_saneam_tem on eiel_t_saneam_tem.clave=eiel_c_saneam_tem.clave AND eiel_t_saneam_tem.codprov=eiel_c_saneam_tem.codprov AND eiel_t_saneam_tem.codmunic=eiel_c_saneam_tem.codmunic AND eiel_t_saneam_tem.tramo_em=eiel_c_saneam_tem.tramo_em ";
			if(version){
				sql+="left join versionesalfa on eiel_t_saneam_tem.revision_actual = versionesalfa.revision  ";
				sql+="left join iuseruserhdr on iuseruserhdr.id = versionesalfa.id_autor  ";
			}
			
			if (idMunicipioSeleccionado==0){
				sql+=" where ((eiel_t_saneam_tem.tramo_em,eiel_t_saneam_tem.codprov,eiel_t_saneam_tem.codmunic) IN ("
						+ "select eiel_tr_saneam_tem_pobl.tramo_em, eiel_tr_saneam_tem_pobl.codprov_tem, eiel_tr_saneam_tem_pobl.codmunic_tem from eiel_tr_saneam_tem_pobl where "
						+ "eiel_tr_saneam_tem_pobl.codprov_pobl='"
						+ idMunicipio.substring(0, 2)
						+ "' and eiel_tr_saneam_tem_pobl.codmunic_pobl='"
						+ idMunicipio.substring(2, 5)
						+ "'"
						+ ")"
						+ "OR "
						+ "eiel_t_saneam_tem.codprov='"
						+ idMunicipio.substring(0, 2)
						+ "' and eiel_t_saneam_tem.codmunic='"
						+ idMunicipio.substring(2, 5) + "')";
			}
			else{
				sql+=" where (eiel_t_saneam_tem.codprov='"+ String.valueOf(idMunicipioSeleccionado).substring(0, 2)
						+ "' and eiel_t_saneam_tem.codmunic='"+ String.valueOf(idMunicipioSeleccionado).substring(2, 5) + "')";	
			}

			if (filtro != null && !filtro.equals("")) {
				sql = sql + "  and " + filtro;
			}
			if (element!=null){
				EmisariosEIEL obj = (EmisariosEIEL)element;
				sql += " and eiel_t_saneam_tem.clave = '"+obj.getClave()+"'";
				sql += " and eiel_t_saneam_tem.codprov = '"+obj.getCodINEProvincia()+"'";
				sql += " and eiel_t_saneam_tem.codmunic = '"+obj.getCodINEMunicipio()+"'";
				sql += " and eiel_t_saneam_tem.tramo_em = '"+obj.getCodOrden()+"'";
			}

			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			VersionEiel versionEiel;
			while (rs.next()) {

				EmisariosEIEL emisario = new EmisariosEIEL();

				emisario.setRevisionActual(rs.getLong("revision_actual"));
				emisario.setRevisionExpirada(rs.getLong("revision_expirada"));
				if (String.valueOf(emisario.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_TEMPORAL)){
					emisario.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_TEMPORAL);
				}
				else if (String.valueOf(emisario.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE)){
					emisario.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE);
				}
				else if (String.valueOf(emisario.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_VALIDA)){
					emisario.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_VALIDO);
				}
				
				emisario.setClave(rs.getString("clave"));
				emisario.setCodINEProvincia(rs.getString("codprov"));
				emisario.setCodINEMunicipio(rs.getString("codmunic"));
				emisario.setCodOrden(rs.getString("tramo_em"));
				emisario.setTitularidad(rs.getString("titular"));
				emisario.setGestion(rs.getString("gestor"));
				emisario.setSistema(rs.getString("sist_impulsion"));
				emisario.setEstado(rs.getString("estado"));
				emisario.setMaterial(rs.getString("material"));
				emisario.setTipo_red(rs.getString("tipo_red_interior"));
				emisario.setObservaciones(rs.getString("observ"));
				emisario.setEstado_Revision(new Integer(rs
						.getInt("estado_revision")));
				emisario.setFechaRevision(rs.getDate("fecha_revision"));
				emisario.setFecha_inst(rs.getDate("fecha_inst"));
				emisario.setBloqueado(rs.getString("bloqueado"));

				//Si se hace una consulta de version, se obtienen los datos de la version
				if (version){
					versionEiel = new VersionEiel();
					versionEiel.setIdVersion(rs.getInt("revision"));
					versionEiel.setFecha(rs.getTimestamp("fecha"));
					versionEiel.setUsuario(rs.getString("name"));
					versionEiel.setAccion(rs.getString("tipocambio"));
					
					emisario.setVersion(versionEiel);
				}
					
					
				if(noGeoReferenciado){
					if(!rs.getBoolean("valida"))
						lstDatos.add(emisario);
				}else
					lstDatos.add(emisario);

			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			throw ex;
		}
		finally{
			safeClose(rs, statement, connection);
		}

		return lstDatos;
	}
	
	protected Collection obtenerFeaturesEmisarios(EmisariosEIEL object,
			String idMunicipio) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Collection lstDatos = new ArrayList();

		try {

			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				return null;
			}

			String sql = "select id,revision_expirada from eiel_c_saneam_tem where eiel_c_saneam_tem.codprov='"
					+ idMunicipio.substring(0, 2)
					+ "' and eiel_c_saneam_tem.codmunic='"
					+ idMunicipio.substring(2, 5)
					+ "' and eiel_c_saneam_tem.clave ='"
					+ object.getClave()
					+ "' and eiel_c_saneam_tem.tramo_em='"
					+ object.getCodOrden()
					+ "'";

			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				long revision_expirada=0;				
				try {revision_expirada=rs.getLong("revision_expirada");	} catch (Exception e) {	}
				FeatureEIELSimple feature=new FeatureEIELSimple(id,String.valueOf(revision_expirada));
				lstDatos.add(feature);;

			}

		} catch (Exception ex) {


			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
		}
		finally{
			safeClose(rs, statement, connection);
		}

		return lstDatos;
	}

	protected EmisariosEIEL bloquearEmisarios(EmisariosEIEL object,
			boolean bloqueado, Sesion userSesion) throws Exception {

		if (object == null)
			return null;

		String sSQL = "update eiel_t_saneam_tem set bloqueado=? where clave=? and codprov=? and codmunic=? and tramo_em=?";

		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = CPoolDatabase.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sSQL);
			if (bloqueado)
				ps.setString(1, userSesion.getUserPrincipal().getName());
			else
				ps.setNull(1, java.sql.Types.VARCHAR);
			ps.setString(2, object.getClave());
			ps.setString(3, object.getCodINEProvincia());
			ps.setString(4, object.getCodINEMunicipio());
			ps.setString(5, object.getCodOrden());
			ps.execute();
			conn.commit();
			object.setBloqueado(bloqueado ? userSesion.getUserPrincipal()
					.getName() : null);

		} catch (Exception e) {
			throw e;
		}
		finally{
			safeClose(null, ps, conn);
		}
		return object;
	}
	
	protected String bloqueadoEmisarios(EmisariosEIEL object) throws Exception {

		if (object == null)
			return null;

		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection conn = null;
		String bloqueado = null;
		String sSQL = "select bloqueado from eiel_t_saneam_tem where clave=? and codprov=? and codmunic=? and tramo_em=?";

		try {

			conn = CPoolDatabase.getConnection();
			preparedStatement = conn.prepareStatement(sSQL);

			preparedStatement.setString(1, object.getClave());
			preparedStatement.setString(2, object.getCodINEProvincia());
			preparedStatement.setString(3, object.getCodINEMunicipio());
			preparedStatement.setString(4, object.getCodOrden());
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				bloqueado = rs.getString("bloqueado");
			}

		} catch (Exception e) {
			throw e;
		}
		finally{
			safeClose(rs, preparedStatement, conn);
		}
		return bloqueado;
	}

	protected void eliminarEmisario(EmisariosEIEL object, Sesion userSesion) throws Exception {

		Connection conn = null;
		try {
			conn = CPoolDatabase.getConnection();
			//Se actualiza la tabla versionesAlfa
			beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tem", ConstantesServerGISEIEL.OPERACION_VERSIONADO_DELETE);
			//Se actualiza el registro existente
			updateRevEmisarios(conn, versionActual, revisionExpiradaNula, object);
			
			//Se crea un nuevo registro
			insertRevEmisarios(conn, versionActual, versionActual, object);

		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			safeClose(null, null, conn);
		}
	}
	
	private void updateRevEmisarios(Connection conn, String version,
			String revision, EmisariosEIEL object) throws Exception {
		PreparedStatement ps = null;

		String sSQLUpdate = "update eiel_t_saneam_tem set revision_expirada="
				+ version
				+ " where clave=? and codprov=? and codmunic=? and tramo_em=? and "
				+ revision;
		ps = conn.prepareStatement(sSQLUpdate);
		ps.setString(1, object.getClave());
		ps.setString(2, object.getCodINEProvincia());
		ps.setString(3, object.getCodINEMunicipio());
		ps.setString(4, object.getCodOrden());
		ps.execute();
		ps.close();
	}
	
	 private void insertRevEmisarios(Connection conn, String version, String revision, EmisariosEIEL object) throws Exception {
	    	PreparedStatement ps = null;		
			String sSQL = "insert into eiel_t_saneam_tem (clave, codprov, codmunic, tramo_em, "
				+" titular, gestor, estado, material,sist_impulsion, tipo_red_interior,fecha_inst,"				
				+ " observ, fecha_revision, estado_revision, "
				+" bloqueado, "+Const.REVISION_ACTUAL+", "+Const.REVISION_EXPIRADA+") "
				+ "values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?, "+version +","+revision+")";
			
			ps = conn.prepareStatement(sSQL);
			ps.setString(1, object.getClave());
			ps.setString(2, object.getCodINEProvincia());
			ps.setString(3, object.getCodINEMunicipio());
			ps.setString(4, object.getCodOrden());
			
			if (object.getTitularidad() != null
					&& !object.getTitularidad().equals(""))
				ps.setString(5, object.getTitularidad());
			else
				ps.setNull(5, java.sql.Types.VARCHAR);

			if (object.getGestion() != null && !object.getGestion().equals(""))
				ps.setString(6, object.getGestion());
			else
				ps.setNull(6, java.sql.Types.VARCHAR);

			if (object.getEstado() != null && !object.getEstado().equals(""))
				ps.setString(7, object.getEstado());
			else
				ps.setNull(7, java.sql.Types.VARCHAR);
			
			if (object.getMaterial() != null && !object.getMaterial().equals(""))
				ps.setString(8, object.getMaterial());
			else
				ps.setNull(8, java.sql.Types.VARCHAR);
			
			if (object.getSistema() != null && !object.getSistema().equals(""))
				ps.setString(9, object.getSistema());
			else
				ps.setNull(9, java.sql.Types.VARCHAR);		

			if (object.getTipo_red() != null
					&& !object.getTipo_red().equals(""))
				ps.setString(10, object.getTipo_red());
			else
				ps.setNull(10, java.sql.Types.VARCHAR);

			if (object.getFecha_inst() != null && !object.getFecha_inst().equals("") )
				ps.setDate(11, object.getFecha_inst());
			else
				ps.setNull(11, java.sql.Types.DATE);
			
			if (object.getObservaciones() != null
					&& !object.getObservaciones().equals(""))
				ps.setString(12, object.getObservaciones());
			else
				ps.setNull(12, java.sql.Types.VARCHAR);

			if (object.getFechaRevision() != null && !object.getFechaRevision().equals("") )
				ps.setDate(13, object.getFechaRevision());
			else
				ps.setNull(13, java.sql.Types.DATE);
			
			if (object.getEstado_Revision() != null)
				ps.setInt(14, object.getEstado_Revision().intValue());
			else
				ps.setNull(14, java.sql.Types.INTEGER);

			if (object.getBloqueado() != null)
				ps.setString(15, object.getBloqueado());
			else
				ps.setNull(15, java.sql.Types.VARCHAR);

			ps.execute();
			ps.close();
			
	    }
	 
	 protected void insertarEmisario(EmisariosEIEL object, Sesion userSesion) throws Exception {

			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String sSQL = null;
			boolean actualizar = false;

			try {

				sSQL = "select * from eiel_t_saneam_tem where clave=? and codprov=? and codmunic=? and tramo_em=? and " +revisionExpiradaNula;

				conn = CPoolDatabase.getConnection();
				ps = conn.prepareStatement(sSQL);
				ps.setString(1, object.getClave());
				ps.setString(2, object.getCodINEProvincia());
				ps.setString(3, object.getCodINEMunicipio());
				ps.setString(4, object.getCodOrden());
				rs = ps.executeQuery();

				
				String tabla="eiel_t_saneam_tem";
				if (rs.next()) {
					
					//Insertar una nueva version en la tabla versionesAlfa solo si es el objeto actual
					if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
						beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_UPDATE);
					
					//Si es una actualizacion temporal no actualizamos la versión
					if ((object.isEstadoValido()) || (object.isEstadoPublicable()))					
						updateRevEmisarios(conn, versionActual, revisionExpiradaNula, object);
					
				} else {
					//La primera vez insertamos una entrada en la tabla de versiones
					if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
						beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_INSERT);
				}
				
				if (object.isEstadoValido()){	
					insertRevEmisarios(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
				}
				else{					
					if (object.isEstadoTemporal()){
						eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_TEMPORAL);					
						insertRevEmisarios(conn, "-1", ConstantesServerGISEIEL.REVISION_TEMPORAL, object);
					}
					else if (object.isEstadoAutoPublicable()){
						eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD);					
						insertRevEmisarios(conn, "-1", ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD, object);
					}
					else if (object.isEstadoPublicable()){
						eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE);	
						insertRevEmisarios(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
					}
				}
				/*if (rs.next()) {
//					Insertar una nueva version en la tabla versionesAlfa
					beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tem", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_UPDATE);
					//Se actualiza la linea que hay
					updateRevEmisarios(conn, versionActual, revisionExpiradaNula, object);
				} else {
					beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tem", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_INSERT);
				}
				insertRevEmisarios(conn, versionActual, "9999999999", object);
				*/
			} catch (Exception e) {
				conn.rollback();
				throw e;
			} finally {
				safeClose(rs, ps, conn);
			}

		}
	  /**
	     * Inserta en la tabla eiel_t_abast_tcn
	     * @param conn
	     * @param version
	     * @param revision
	     * @param object
	     * @throws Exception
	     */
	    private void insertRevTramosConduccion(Connection conn, String version, String revision, TramosConduccionEIEL object) throws Exception {
	    	PreparedStatement ps = null;		
			String sSQL = "insert into eiel_t_abast_tcn (clave, codprov, codmunic, tramo_cn, "
				+ " titular, gestor,estado,material, sist_trans, "
				+ " fecha_inst,observ, fecha_revision, estado_revision, "
				+" bloqueado, "+Const.REVISION_ACTUAL+", "+Const.REVISION_EXPIRADA+") "
				+ " values (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,"+version +","+revision+")";
			
			ps = conn.prepareStatement(sSQL);
			ps.setString(1, object.getClave());
			ps.setString(2, object.getCodINEProvincia());
			ps.setString(3, object.getCodINEMunicipio());
			ps.setString(4, object.getTramo_cn());
			
			if (object.getTitular() != null
					&& !object.getTitular().equals(""))
				ps.setString(5, object.getTitular());
			else
				ps.setNull(5, java.sql.Types.VARCHAR);

			if (object.getGestor() != null && !object.getGestor().equals(""))
				ps.setString(6, object.getGestor());
			else
				ps.setNull(6, java.sql.Types.VARCHAR);
			
			if (object.getEstado() != null && !object.getEstado().equals(""))
				ps.setString(7, object.getEstado());
			else
				ps.setNull(7, java.sql.Types.VARCHAR);

			if (object.getMaterial() != null && !object.getMaterial().equals(""))
				ps.setString(8, object.getMaterial());
			else
				ps.setNull(8, java.sql.Types.VARCHAR);
			
			if (object.getSist_trans() != null && !object.getSist_trans().equals(""))
				ps.setString(9, object.getSist_trans());
			else
				ps.setNull(9, java.sql.Types.VARCHAR);			

			if (object.getFechaInstalacion() != null && !object.getFechaInstalacion().equals("") )
				ps.setDate(10, object.getFechaInstalacion());
			else
				ps.setNull(10, java.sql.Types.DATE);
			
			if (object.getObservaciones() != null && !object.getObservaciones().equals("") )
				ps.setString(11, object.getObservaciones());
			else
				ps.setNull(11, java.sql.Types.VARCHAR);
			
			if (object.getFecha_revision() != null && !object.getFecha_revision().equals("") )
				ps.setDate(12, object.getFecha_revision());
			else
				ps.setNull(12, java.sql.Types.DATE);

			if (object.getEstado_revision() != null)
				ps.setInt(13, object.getEstado_revision().intValue());
			else
				ps.setNull(13, java.sql.Types.INTEGER);

			if (object.getBloqueado() != null)
				ps.setString(14, object.getBloqueado());
			else
				ps.setNull(14, java.sql.Types.VARCHAR);

			ps.execute();
			ps.close();
			
	    }
	    
	    /**
	     * Inserta en la tabla eiel_t_abast_ca
	     * @param conn
	     * @param version
	     * @param revision
	     * @param object
	     * @throws Exception
	     */
	    private void insertRevColector(Connection conn, String version, String revision, ColectorEIEL object) throws Exception {
	    	PreparedStatement ps = null;		
			String sSQL = "insert into eiel_t_saneam_tcl (clave, codprov, codmunic, tramo_cl, "
				+ " titular, gestor, estado, material,sist_impulsion,tipo_red_interior, tip_interceptor,"
				+ " fecha_inst,observ, fecha_revision, estado_revision,"
				+" bloqueado, "+Const.REVISION_ACTUAL+", "+Const.REVISION_EXPIRADA+") "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "+version +","+revision+")";
			
			ps = conn.prepareStatement(sSQL);
			ps.setString(1, object.getClave());
			ps.setString(2, object.getCodINEProvincia());
			ps.setString(3, object.getCodINEMunicipio());
			ps.setString(4, object.getCodOrden());
			if (object.getTitularidad() != null
					&& !object.getTitularidad().equals(""))
				ps.setString(5, object.getTitularidad());
			else
				ps.setNull(5, java.sql.Types.VARCHAR);

			if (object.getGestion() != null && !object.getGestion().equals(""))
				ps.setString(6, object.getGestion());
			else
				ps.setNull(6, java.sql.Types.VARCHAR);

			if (object.getEstado() != null && !object.getEstado().equals(""))
				ps.setString(7, object.getEstado());
			else
				ps.setNull(7, java.sql.Types.VARCHAR);
			
			if (object.getMaterial() != null && !object.getMaterial().equals(""))
				ps.setString(8, object.getMaterial());
			else
				ps.setNull(8, java.sql.Types.VARCHAR);
			
			if (object.getSist_impulsion()!= null && !object.getSist_impulsion().equals(""))
				ps.setString(9, object.getSist_impulsion());
			else
				ps.setNull(9, java.sql.Types.VARCHAR);
			
			if (object.getTipo_red() != null
					&& !object.getTipo_red().equals(""))
				ps.setString(10, object.getTipo_red());
			else
				ps.setNull(10, java.sql.Types.VARCHAR);

			if (object.getTip_interceptor()!= null
					&& !object.getTip_interceptor().equals(""))
				ps.setString(11, object.getTip_interceptor());
			else
				ps.setNull(11, java.sql.Types.VARCHAR);
			
			if (object.getFecha_inst() != null && !object.getFecha_inst().equals("") )
				ps.setDate(12, object.getFecha_inst());
			else
				ps.setNull(12, java.sql.Types.DATE);
			
			if (object.getObservaciones() != null && !object.getObservaciones().equals("") )
				ps.setString(13, object.getObservaciones());
			else
				ps.setNull(13, java.sql.Types.VARCHAR);
			
			if (object.getFechaRevision() != null && !object.getFechaRevision().equals("") )
				ps.setDate(14, object.getFechaRevision());
			else
				ps.setNull(14, java.sql.Types.DATE);

			if (object.getEstado_Revision() != null)
				ps.setInt(15, object.getEstado_Revision().intValue());
			else
				ps.setNull(15, java.sql.Types.INTEGER);

			if (object.getBloqueado() != null)
				ps.setString(16, object.getBloqueado());
			else
				ps.setNull(16, java.sql.Types.VARCHAR);

			ps.execute();
			ps.close();
			
	    }
	    /**
		 * Inserta/Actualiza en la tabla eiel_t_abast_tcn
		 * @param object
		 * @param userSesion
		 * @throws Exception
		 */
		protected void insertarTramoConduccion(TramosConduccionEIEL object, Sesion userSesion) throws Exception {

				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				String sSQL = null;
				boolean actualizar = false;

				try {

					sSQL = "select * from eiel_t_abast_tcn where clave=? and codprov=? and codmunic=? and tramo_cn=? and " +revisionExpiradaNula;

					conn = CPoolDatabase.getConnection();
					ps = conn.prepareStatement(sSQL);
					ps.setString(1, object.getClave());
					ps.setString(2, object.getCodINEProvincia());
					ps.setString(3, object.getCodINEMunicipio());
					ps.setString(4, object.getTramo_cn());
					rs = ps.executeQuery();

					
					String tabla="eiel_t_abast_tcn";
					if (rs.next()) {
						
						//Insertar una nueva version en la tabla versionesAlfa solo si es el objeto actual
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
							beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_UPDATE);
						
						//Si es una actualizacion temporal no actualizamos la versión
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))					
							updateRevConduccion(conn, versionActual, revisionExpiradaNula, object);
						
					} else {
						//La primera vez insertamos una entrada en la tabla de versiones
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
							beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_INSERT);
					}
					
					if (object.isEstadoValido()){	
						insertRevTramosConduccion(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
					}
					else{					
						if (object.isEstadoTemporal()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_TEMPORAL);					
							insertRevTramosConduccion(conn, "-1", ConstantesServerGISEIEL.REVISION_TEMPORAL, object);
						}
						else if (object.isEstadoAutoPublicable()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD);					
							insertRevTramosConduccion(conn, "-1", ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD, object);
						}
						else if (object.isEstadoPublicable()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE);	
							insertRevTramosConduccion(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
						}
					}
					/*if (rs.next()) {
//						Insertar una nueva version en la tabla versionesAlfa
						beforeRequest(conn, userSesion.getIdUser(),"eiel_t_abast_tcn", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_UPDATE);
						//Se actualiza la linea que hay
						updateRevConduccion(conn, versionActual, revisionExpiradaNula, object);
					} else {
						beforeRequest(conn, userSesion.getIdUser(),"eiel_t_abast_tcn", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_INSERT);
					}
					insertRevTramosConduccion(conn, versionActual, "9999999999", object);*/

				} catch (Exception e) {
					conn.rollback();
					throw e;
				} finally {
					safeClose(rs, ps, conn);
				}

			}
		 /**
		 * Inserta/Actualiza en la tabla eiel_t_saneam_tcl
		 * @param object
		 * @param userSesion
		 * @throws Exception
		 */
		protected void insertarColector(ColectorEIEL object, Sesion userSesion) throws Exception {

				Connection conn = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				String sSQL = null;
				boolean actualizar = false;

				try {

					sSQL = "select * from eiel_t_saneam_tcl where clave=? and codprov=? and codmunic=? and tramo_cl=? and " +revisionExpiradaNula;

					conn = CPoolDatabase.getConnection();
					ps = conn.prepareStatement(sSQL);
					ps.setString(1, object.getClave());
					ps.setString(2, object.getCodINEProvincia());
					ps.setString(3, object.getCodINEMunicipio());
					ps.setString(4, object.getCodOrden());
					rs = ps.executeQuery();

					
					String tabla="eiel_t_saneam_tcl";
					if (rs.next()) {
						
						//Insertar una nueva version en la tabla versionesAlfa solo si es el objeto actual
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
							beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_UPDATE);
						
						//Si es una actualizacion temporal no actualizamos la versión
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))					
							updateRevColectores(conn, versionActual, revisionExpiradaNula, object);
						
					} else {
						//La primera vez insertamos una entrada en la tabla de versiones
						if ((object.isEstadoValido()) || (object.isEstadoPublicable()))	
							beforeRequest(conn, userSesion.getIdUser(),tabla, ConstantesServerGISEIEL.OPERACION_VERSIONADO_INSERT);
					}
					
					if (object.isEstadoValido()){	
						insertRevColector(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
					}
					else{					
						if (object.isEstadoTemporal()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_TEMPORAL);					
							insertRevColector(conn, "-1", ConstantesServerGISEIEL.REVISION_TEMPORAL, object);
						}
						else if (object.isEstadoAutoPublicable()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD);					
							insertRevColector(conn, "-1", ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD, object);
						}
						else if (object.isEstadoPublicable()){
							eliminarVersionAnterior(conn,tabla,object,ConstantesServerGISEIEL.REVISION_PUBLICABLE);	
							insertRevColector(conn, versionActual, ConstantesServerGISEIEL.REVISION_VALIDA, object);
						}
					}
					/*
					if (rs.next()) {
//						Insertar una nueva version en la tabla versionesAlfa
						beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tcl", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_UPDATE);
						//Se actualiza la linea que hay
						updateRevColectores(conn, versionActual, revisionExpiradaNula, object);
					} else {
						beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tcl", ConstantesLocalGISEIEL.OPERACION_VERSIONADO_INSERT);
					}
					insertRevColector(conn, versionActual, "9999999999", object);
					*/
				} catch (Exception e) {
					conn.rollback();
					throw e;
				} finally {
					safeClose(rs, ps, conn);
				}

			}
		/**
		 * Actualiza el registro de la tabla eiel_t_abast_ca
		 */

		private void updateRevConduccion(Connection conn, String version,
				String revision, TramosConduccionEIEL object) throws Exception {
			PreparedStatement ps = null;

			String sSQLUpdate = "update eiel_t_abast_tcn set revision_expirada="
					+ version
					+ " where clave=? and codprov=? and codmunic=? and tramo_cn=? and "
					+ revision;
			ps = conn.prepareStatement(sSQLUpdate);
			ps.setString(1, object.getClave());
			ps.setString(2, object.getCodINEProvincia());
			ps.setString(3, object.getCodINEMunicipio());
			ps.setString(4, object.getTramo_cn());
			ps.execute();
			ps.close();
		}
		/**
		 * Actualiza el registro de la tabla eiel_t_abast_ca
		 */

		private void updateRevColectores(Connection conn, String version,
				String revision, ColectorEIEL object) throws Exception {
			PreparedStatement ps = null;

			String sSQLUpdate = "update eiel_t_saneam_tcl set revision_expirada="
					+ version
					+ " where clave=? and codprov=? and codmunic=? and tramo_cl=? and "
					+ revision;
			ps = conn.prepareStatement(sSQLUpdate);
			ps.setString(1, object.getClave());
			ps.setString(2, object.getCodINEProvincia());
			ps.setString(3, object.getCodINEMunicipio());
			ps.setString(4, object.getCodOrden());
			ps.execute();
			ps.close();
		}
		
		protected TramosConduccionEIEL bloquearTramoConduccion(TramosConduccionEIEL object,
				boolean bloqueado, Sesion userSesion) throws Exception {

			if (object == null)
				return null;

			String sSQL = "update eiel_t_abast_tcn set bloqueado=? where clave=? and codprov=? and codmunic=? and tramo_cn=?";

			PreparedStatement ps = null;
			Connection conn = null;
			try {
				conn = CPoolDatabase.getConnection();
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sSQL);
				if (bloqueado)
					ps.setString(1, userSesion.getUserPrincipal().getName());
				else
					ps.setNull(1, java.sql.Types.VARCHAR);
				ps.setString(2, object.getClave());
				ps.setString(3, object.getCodINEProvincia());
				ps.setString(4, object.getCodINEMunicipio());
				ps.setString(5, object.getTramo_cn());
				ps.execute();
				conn.commit();
				object.setBloqueado(bloqueado ? userSesion.getUserPrincipal()
						.getName() : null);

			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally{
				safeClose(null, ps, conn);
			}
			return object;
		}
		
		protected String bloqueadoTramoConduccion(TramosConduccionEIEL object) throws Exception {

			if (object == null)
				return null;

			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			Connection conn = null;
			String bloqueado = null;
			String sSQL = "select bloqueado from eiel_t_saneam_tem where clave=? and codprov=? and codmunic=? and tramo_em=?";

			try {

				conn = CPoolDatabase.getConnection();
				preparedStatement = conn.prepareStatement(sSQL);

				preparedStatement.setString(1, object.getClave());
				preparedStatement.setString(2, object.getCodINEProvincia());
				preparedStatement.setString(3, object.getCodINEMunicipio());
				preparedStatement.setString(4, object.getTramo_cn());
				rs = preparedStatement.executeQuery();

				while (rs.next()) {
					bloqueado = rs.getString("bloqueado");
				}

			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally{
				safeClose(rs, preparedStatement, conn);
			}
			return bloqueado;
		}

		protected ColectorEIEL bloquearColector(ColectorEIEL object,
				boolean bloqueado, Sesion userSesion) throws Exception {

			if (object == null)
				return null;

			String sSQL = "update eiel_t_saneam_tcl set bloqueado=? where clave=? and codprov=? and codmunic=? and tramo_cl=?";

			PreparedStatement ps = null;
			Connection conn = null;
			try {
				conn = CPoolDatabase.getConnection();
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sSQL);
				if (bloqueado)
					ps.setString(1, userSesion.getUserPrincipal().getName());
				else
					ps.setNull(1, java.sql.Types.VARCHAR);
				ps.setString(2, object.getClave());
				ps.setString(3, object.getCodINEProvincia());
				ps.setString(4, object.getCodINEMunicipio());
				ps.setString(5, object.getCodOrden());
				ps.execute();
				conn.commit();
				object.setBloqueado(bloqueado ? userSesion.getUserPrincipal()
						.getName() : null);

			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally{
				safeClose(null, ps, conn);
			}
			return object;
		}
		
		protected String bloqueadoColector(ColectorEIEL object) throws Exception {

			if (object == null)
				return null;

			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			Connection conn = null;
			String bloqueado = null;
			String sSQL = "select bloqueado from eiel_t_saneam_tem where clave=? and codprov=? and codmunic=? and tramo_em=?";

			try {

				conn = CPoolDatabase.getConnection();
				preparedStatement = conn.prepareStatement(sSQL);

				preparedStatement.setString(1, object.getClave());
				preparedStatement.setString(2, object.getCodINEProvincia());
				preparedStatement.setString(3, object.getCodINEMunicipio());
				preparedStatement.setString(4, object.getCodOrden());
				rs = preparedStatement.executeQuery();

				while (rs.next()) {
					bloqueado = rs.getString("bloqueado");
				}

			} catch (Exception e) {
				conn.rollback();
				throw e;
			}
			finally{
				safeClose(rs, preparedStatement, conn);
			}
			return bloqueado;
		}
		
		public ArrayList obtenerListaColector(String filtro, String idMunicipio,boolean noGeoReferenciado, boolean version, Object element, int idMunicipioSeleccionado) throws Exception{

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			ArrayList lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select eiel_t_saneam_tcl.revision_actual, eiel_t_saneam_tcl.revision_expirada,eiel_t_saneam_tcl.clave,eiel_t_saneam_tcl.codprov,eiel_t_saneam_tcl.codmunic, "
						+"eiel_t_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.titular,eiel_t_saneam_tcl.gestor, eiel_t_saneam_tcl.estado, "
						+"eiel_t_saneam_tcl.material,eiel_t_saneam_tcl.sist_impulsion,eiel_t_saneam_tcl.tipo_red_interior, eiel_t_saneam_tcl.tip_interceptor,"
						+" eiel_t_saneam_tcl.fecha_inst,eiel_t_saneam_tcl.observ,eiel_t_saneam_tcl.fecha_revision,eiel_t_saneam_tcl.estado_revision,eiel_t_saneam_tcl.bloqueado ";
				if(noGeoReferenciado)	
					sql+=", ST_IsValid(eiel_c_saneam_tcl.\"GEOMETRY\")AS valida ";
				
				if (version)
					sql+=", versionesalfa.revision, iuseruserhdr.name, versionesalfa.fecha, versionesalfa.tipocambio  ";
				sql+=" from eiel_t_saneam_tcl ";
				if(noGeoReferenciado)
					sql+="left join eiel_c_saneam_tcl on eiel_t_saneam_tcl.clave=eiel_c_saneam_tcl.clave AND eiel_t_saneam_tcl.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_saneam_tcl.codmunic=eiel_c_saneam_tcl.codmunic AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl ";
				if(version){
					sql+="left join versionesalfa on eiel_t_saneam_tcl.revision_actual = versionesalfa.revision  ";
					sql+="left join iuseruserhdr on iuseruserhdr.id = versionesalfa.id_autor  ";
				}
				if (idMunicipioSeleccionado==0){
					sql+=" where ((eiel_t_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.codprov,eiel_t_saneam_tcl.codmunic) IN ("
							+ "select eiel_tr_saneam_tcl_pobl.tramo_cl, eiel_tr_saneam_tcl_pobl.codprov_tcl, eiel_tr_saneam_tcl_pobl.codmunic_tcl from eiel_tr_saneam_tcl_pobl where "
							+ "eiel_tr_saneam_tcl_pobl.codprov_pobl='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_tr_saneam_tcl_pobl.codmunic_pobl='"
							+ idMunicipio.substring(2, 5)
							+ "'"
							+ ")"
							+ "OR "
							+ "eiel_t_saneam_tcl.codprov='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_t_saneam_tcl.codmunic='"
							+ idMunicipio.substring(2, 5) + "')";
				}
				else{
					sql+=" where (eiel_t_saneam_tcl.codprov='"+ String.valueOf(idMunicipioSeleccionado).substring(0, 2)
							+ "' and eiel_t_saneam_tcl.codmunic='"+ String.valueOf(idMunicipioSeleccionado).substring(2, 5) + "')";	
				}

				if (filtro != null && !filtro.equals("")) {
					sql = sql + "  and " + filtro;
				}
				if (element!=null){
					ColectorEIEL obj = (ColectorEIEL)element;
					sql += " and eiel_t_saneam_tcl.clave = '"+obj.getClave()+"'";
					sql += " and eiel_t_saneam_tcl.codprov = '"+obj.getCodINEProvincia()+"'";
					sql += " and eiel_t_saneam_tcl.codmunic = '"+obj.getCodINEMunicipio()+"'";
					sql += " and eiel_t_saneam_tcl.tramo_cl = '"+obj.getCodOrden()+"'";
				}

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				VersionEiel versionEiel;
				while (rs.next()) {

					ColectorEIEL colector = new ColectorEIEL();
					
					colector.setRevisionActual(rs.getLong("revision_actual"));
					colector.setRevisionExpirada(rs.getLong("revision_expirada"));
					if (String.valueOf(colector.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_TEMPORAL)){
						colector.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_TEMPORAL);
					}
					else if (String.valueOf(colector.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE)){
						colector.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE);
					}
					else if (String.valueOf(colector.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_VALIDA)){
						colector.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_VALIDO);
					}

					colector.setClave(rs.getString("clave"));
					colector.setCodINEProvincia(rs.getString("codprov"));
					colector.setCodINEMunicipio(rs.getString("codmunic"));
					colector.setCodOrden(rs.getString("tramo_cl"));
					colector.setTitularidad(rs.getString("titular"));
					colector.setGestion(rs.getString("gestor"));
					colector.setEstado(rs.getString("estado"));
					colector.setMaterial(rs.getString("material"));
					colector.setSist_impulsion(rs.getString("sist_impulsion"));
					colector.setTipo_red(rs.getString("tipo_red_interior"));
					colector.setTip_interceptor(rs.getString("tip_interceptor"));
					colector.setFecha_inst(rs.getDate("fecha_inst"));
					colector.setObservaciones(rs.getString("observ"));
					colector.setEstado_Revision(new Integer(rs
							.getInt("estado_revision")));
					colector.setFechaRevision(rs.getDate("fecha_revision"));
					colector.setBloqueado(rs.getString("bloqueado"));

					//Si se hace una consulta de version, se obtienen los datos de la version
					if (version){
						versionEiel = new VersionEiel();
						versionEiel.setIdVersion(rs.getInt("revision"));
						versionEiel.setFecha(rs.getTimestamp("fecha"));
						versionEiel.setUsuario(rs.getString("name"));
						versionEiel.setAccion(rs.getString("tipocambio"));
						
						colector.setVersion(versionEiel);
					}
						
						
					if(noGeoReferenciado){
						if(!rs.getBoolean("valida"))
							lstDatos.add(colector);
					}else
						lstDatos.add(colector);

				}



			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				throw ex;
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		
		protected Collection obtenerFeaturesColector(ColectorEIEL object,
				String idMunicipio) {

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			Collection lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select id,revision_expirada from eiel_c_saneam_tcl where eiel_c_saneam_tcl.codprov='"
						+ idMunicipio.substring(0, 2)
						+ "' and eiel_c_saneam_tcl.codmunic='"
						+ idMunicipio.substring(2, 5)
						+ "' and eiel_c_saneam_tcl.clave ='"
						+ object.getClave()
						+ "' and eiel_c_saneam_tcl.tramo_cl='"
						+ object.getCodOrden()
						+ "'";

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();

				while (rs.next()) {

					int id = rs.getInt("id");
					long revision_expirada=0;				
					try {revision_expirada=rs.getLong("revision_expirada");	} catch (Exception e) {	}
					FeatureEIELSimple feature=new FeatureEIELSimple(id,String.valueOf(revision_expirada));
					lstDatos.add(feature);;

				}
			} catch (Exception ex) {

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		
		public ArrayList obtenerListaTramoConduccion(String filtro, String idMunicipio,boolean noGeoReferenciado, boolean version, Object element, int idMunicipioSeleccionado) throws Exception{

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			ArrayList lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select eiel_t_abast_tcn.revision_actual, eiel_t_abast_tcn.revision_expirada,eiel_t_abast_tcn.clave,eiel_t_abast_tcn.codprov,eiel_t_abast_tcn.codmunic, "
						+"eiel_t_abast_tcn.tramo_cn,eiel_t_abast_tcn.titular,eiel_t_abast_tcn.gestor, eiel_t_abast_tcn.estado, "
						+"eiel_t_abast_tcn.material,eiel_t_abast_tcn.sist_trans, "
						+" eiel_t_abast_tcn.fecha_inst,eiel_t_abast_tcn.observ,eiel_t_abast_tcn.fecha_revision,eiel_t_abast_tcn.estado_revision,eiel_t_abast_tcn.bloqueado ";
				if(noGeoReferenciado)	
					sql+=", ST_IsValid(eiel_c_abast_tcn.\"GEOMETRY\")AS valida ";
				
				if (version)
					sql+=", versionesalfa.revision, iuseruserhdr.name, versionesalfa.fecha, versionesalfa.tipocambio  ";
				sql+=" from eiel_t_abast_tcn ";
				if(noGeoReferenciado)
					sql+="left join eiel_c_abast_tcn on eiel_t_abast_tcn.clave=eiel_c_abast_tcn.clave AND eiel_t_abast_tcn.codprov=eiel_c_abast_tcn.codprov AND eiel_t_abast_tcn.codmunic=eiel_c_abast_tcn.codmunic AND eiel_t_abast_tcn.tramo_cn=eiel_c_abast_tcn.tramo_cn ";
				if(version){
					sql+="left join versionesalfa on eiel_t_abast_tcn.revision_actual = versionesalfa.revision  ";
					sql+="left join iuseruserhdr on iuseruserhdr.id = versionesalfa.id_autor  ";
				}
				if (idMunicipioSeleccionado==0){
					sql+=" where ((eiel_t_abast_tcn.tramo_cn,eiel_t_abast_tcn.codprov,eiel_t_abast_tcn.codmunic) IN ("
							+ "select eiel_tr_abast_tcn_pobl.tramo_tcn, eiel_tr_abast_tcn_pobl.codprov_tcn, eiel_tr_abast_tcn_pobl.codmunic_tcn from eiel_tr_abast_tcn_pobl where "
							+ "eiel_tr_abast_tcn_pobl.codprov_pobl='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_tr_abast_tcn_pobl.codmunic_pobl='"
							+ idMunicipio.substring(2, 5)
							+ "'"
							+ ")"
							+ "OR "
							+ "eiel_t_abast_tcn.codprov='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_t_abast_tcn.codmunic='"
							+ idMunicipio.substring(2, 5) + "')";
				}
				else{
					sql+=" where (eiel_t_abast_tcn.codprov='"+ String.valueOf(idMunicipioSeleccionado).substring(0, 2)
							+ "' and eiel_t_abast_tcn.codmunic='"+ String.valueOf(idMunicipioSeleccionado).substring(2, 5) + "')";
				}

				if (filtro != null && !filtro.equals("")) {
					sql = sql + "  and " + filtro;
				}
				if (element!=null){
					TramosConduccionEIEL obj = (TramosConduccionEIEL)element;
					sql += " and eiel_t_abast_tcn.clave = '"+obj.getClave()+"'";
					sql += " and eiel_t_abast_tcn.codprov = '"+obj.getCodINEProvincia()+"'";
					sql += " and eiel_t_abast_tcn.codmunic = '"+obj.getCodINEMunicipio()+"'";
					sql += " and eiel_t_abast_tcn.tramo_cn = '"+obj.getTramo_cn()+"'";
				}

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				VersionEiel versionEiel;
				while (rs.next()) {

					TramosConduccionEIEL tramoConduccion = new TramosConduccionEIEL();
					
					tramoConduccion.setRevisionActual(rs.getLong("revision_actual"));
					tramoConduccion.setRevisionExpirada(rs.getLong("revision_expirada"));
					if (String.valueOf(tramoConduccion.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_TEMPORAL)){
						tramoConduccion.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_TEMPORAL);
					}
					else if (String.valueOf(tramoConduccion.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE)){
						tramoConduccion.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE);
					}
					else if (String.valueOf(tramoConduccion.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_VALIDA)){
						tramoConduccion.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_VALIDO);
					}
					

					tramoConduccion.setClave(rs.getString("clave"));
					tramoConduccion.setCodINEProvincia(rs.getString("codprov"));
					tramoConduccion.setCodINEMunicipio(rs.getString("codmunic"));
					tramoConduccion.setTramo_cn(rs.getString("tramo_cn"));
					tramoConduccion.setTitular(rs.getString("titular"));
					tramoConduccion.setGestor(rs.getString("gestor"));
					tramoConduccion.setEstado(rs.getString("estado"));
					tramoConduccion.setMaterial(rs.getString("material"));
					tramoConduccion.setSist_trans(rs.getString("sist_trans"));
					tramoConduccion.setObservaciones(rs.getString("observ"));
					tramoConduccion.setEstado_revision(new Integer(rs.getInt("estado_revision")));
					tramoConduccion.setFecha_revision(rs.getDate("fecha_revision"));
					tramoConduccion.setFechaInstalacion(rs.getDate("fecha_inst"));
					tramoConduccion.setBloqueado(rs.getString("bloqueado"));

					//Si se hace una consulta de version, se obtienen los datos de la version
					if (version){
						versionEiel = new VersionEiel();
						versionEiel.setIdVersion(rs.getInt("revision"));
						versionEiel.setFecha(rs.getTimestamp("fecha"));
						versionEiel.setUsuario(rs.getString("name"));
						versionEiel.setAccion(rs.getString("tipocambio"));
						
						tramoConduccion.setVersion(versionEiel);
					}
						
						
					if(noGeoReferenciado){
						if(!rs.getBoolean("valida"))
							lstDatos.add(tramoConduccion);
					}else
						lstDatos.add(tramoConduccion);

				}

			} catch (Exception ex) {

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				throw ex;
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		
		protected Collection obtenerFeaturesTramoConduccion(TramosConduccionEIEL object,
				String idMunicipio) {

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			Collection lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select id,revision_expirada from eiel_c_abast_tcn where eiel_c_abast_tcn.codprov='"
						+ idMunicipio.substring(0, 2)
						+ "' and eiel_c_abast_tcn.codmunic='"
						+ idMunicipio.substring(2, 5)
						+ "' and eiel_c_abast_tcn.clave ='"
						+ object.getClave()
						+ "' and eiel_c_abast_tcn.tramo_cn='"
						+ object.getTramo_cn()
						+ "'";

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();

				while (rs.next()) {

					int id = rs.getInt("id");
					long revision_expirada=0;				
					try {revision_expirada=rs.getLong("revision_expirada");	} catch (Exception e) {	}
					FeatureEIELSimple feature=new FeatureEIELSimple(id,String.valueOf(revision_expirada));
					lstDatos.add(feature);

				}

			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		


		protected void eliminarTramoConduccion(TramosConduccionEIEL object, Sesion userSesion) throws Exception {

			Connection conn = null;
			try {
				conn = CPoolDatabase.getConnection();
				//Se actualiza la tabla versionesAlfa
				beforeRequest(conn, userSesion.getIdUser(),"eiel_t_abast_tcn", ConstantesServerGISEIEL.OPERACION_VERSIONADO_DELETE);
				//Se actualiza el registro existente
				updateRevConduccion(conn, versionActual, revisionExpiradaNula, object);
				
				//Se crea un nuevo registro
				insertRevTramosConduccion(conn, versionActual, versionActual, object);

			} catch (Exception e) {
				conn.rollback();
				throw e;
			} finally {
				safeClose(null, null, conn);
			}
		}
		protected void eliminarColector(ColectorEIEL object, Sesion userSesion) throws Exception {

			Connection conn = null;
			try {
				conn = CPoolDatabase.getConnection();
				//Se actualiza la tabla versionesAlfa
				beforeRequest(conn, userSesion.getIdUser(),"eiel_t_saneam_tcl", ConstantesServerGISEIEL.OPERACION_VERSIONADO_DELETE);
				//Se actualiza el registro existente
				updateRevColectores(conn, versionActual, revisionExpiradaNula, object);
				
				//Se crea un nuevo registro
				insertRevColector(conn, versionActual, versionActual, object);

			} catch (Exception e) {
				conn.rollback();
				throw e;
			} finally {
				safeClose(null, null, conn);
			}
		}
		
		
		/**
		 * Eliminamos la version anterior del elemento.
		 * @param conn
		 * @param object
		 * @throws Exception
		 */
		protected void eliminarVersionAnterior(Connection conn,String tabla, WorkflowEIEL object,String revision) throws Exception{
			eliminarVersionAnterior(conn,tabla,null,object,revision);
		}

		/**
		 * 
		 * @param conn
		 * @param tabla
		 * @param elementoUso Indica si el elemento es de tipo uso para lo cual necesita de un tratamiento especial
		 * @param object
		 * @param revision
		 * @throws Exception
		 */
		protected void eliminarVersionAnterior(Connection conn,String tabla, String filtroUso,WorkflowEIEL object,String revision)
				throws Exception {

			String tablaUsos=null;
			
			if (tabla==null){
				tabla=object.getNombreTablaAlfanumerica();
				tablaUsos=object.getNombreTablaAlfanumericaUsos();
			}
			String sqlBusqueda=object.getFilterSQL();
			if (filtroUso!=null)
				sqlBusqueda+=filtroUso;
			String sSQL=null;
			if (revision.equals(ConstantesServerGISEIEL.REVISION_TEMPORAL))
				sSQL = "delete from "+tabla+	" where "+sqlBusqueda+" and (revision_expirada='"+revision+"' or revision_expirada='"+ConstantesServerGISEIEL.REVISION_PUBLICABLE+"')";
			else if (revision.equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE) && object.getEstadoValidacionAnterior()==ConstantesServerGISEIEL.ESTADO_PUBLICABLE_MOVILIDAD)
				sSQL = "delete from "+tabla+	" where "+sqlBusqueda+" and revision_expirada='"+ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD+"'";
			else
				sSQL = "delete from "+tabla+	" where "+sqlBusqueda+" and revision_expirada='"+revision+"'";
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean conectado=true;
			
			if (conn==null){
				conectado=false;
				conn = CPoolDatabase.getConnection();
			}
			
			try {
				ps = conn.prepareStatement(sSQL);
				ps.execute();

			} catch (Exception e) {
				if (!conectado)
					conn.rollback();
				else
					throw e;
			} finally {
				if (!conectado)
					safeClose(null, ps, conn);
				else
					ps.close();
			}
			
			if (tablaUsos!=null){
				eliminarVersionAnterior(conn,tablaUsos, null,object,revision);
			}
		}		
		
		public ArrayList obtenerListaAgrupaciones6000(String filtro,
				String idMunicipio, boolean noGeoReferenciados, boolean version,
				Object element, int idMunicipioSeleccionado) throws Exception {
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			ArrayList lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select eiel_t_entidades_agrupadas.* ";
												  
				if (version)
					sql+=", versionesalfa.revision, iuseruserhdr.name, versionesalfa.fecha, versionesalfa.tipocambio  ";
				sql+=" from eiel_t_entidades_agrupadas ";

				if(version){
					sql+="left join versionesalfa on eiel_t_entidades_agrupadas.revision_actual = versionesalfa.revision  ";
					sql+="left join iuseruserhdr on iuseruserhdr.id = versionesalfa.id_autor  ";
				}
				sql+="where eiel_t_entidades_agrupadas.codmunicipio='"
						+ idMunicipio.substring(2, 5) + "'";

				if (filtro != null && !filtro.equals("")) {
					sql = sql + " and " + filtro;
				}
				if (element!=null){
					EntidadesAgrupadasEIEL obj = (EntidadesAgrupadasEIEL)element;
					sql += " and eiel_t_entidades_agrupadas.codentidad = '"+obj.getCodEntidad()+"'";
					sql += " and eiel_t_entidades_agrupadas.codnucleo = '"+obj.getCodNucleo()+"'";
					sql += " and eiel_t_entidades_agrupadas.codentidad_agrupada = '"+obj.getCodEntidad_agrupada()+"'";
					sql += " and eiel_t_entidades_agrupadas.codnucleo_agrupado = '"+obj.getCodNucleo_agrupado()+"'";
				}
				

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				VersionEiel versionEiel;
				while (rs.next()) {

					EntidadesAgrupadasEIEL entAgr = new EntidadesAgrupadasEIEL();
					
					entAgr.setRevisionActual(rs.getLong("revision_actual"));
					entAgr.setRevisionExpirada(rs.getLong("revision_expirada"));
					if (String.valueOf(entAgr.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_TEMPORAL)){
						entAgr.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_TEMPORAL);
					}
					else if (String.valueOf(entAgr.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE)){
						entAgr.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE);
					}
					else if (String.valueOf(entAgr.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE_MOVILIDAD)){
						entAgr.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE_MOVILIDAD);
					}
					else if (String.valueOf(entAgr.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_VALIDA)){
						entAgr.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_VALIDO);
					}

					entAgr.setCodINEMunicipio(rs.getString("codmunicipio"));
					entAgr.setCodINEEntidad(rs.getString("codentidad"));
					entAgr.setCodINENucleo(rs.getString("codnucleo"));
					entAgr.setCodINEEntidad_agrupada(rs.getString("codentidad_agrupada"));
					entAgr.setCodINENucleo_agrupado(rs.getString("codnucleo_agrupado"));
					
					if (version){
						versionEiel = new VersionEiel();
						versionEiel.setIdVersion(rs.getInt("revision"));
						versionEiel.setFecha(rs.getDate("fecha"));
						versionEiel.setUsuario(rs.getString("name"));
						versionEiel.setAccion(rs.getString("tipocambio"));
						
						entAgr.setVersion(versionEiel);
					}
					
					lstDatos.add(entAgr);

				}
				
			} catch (Exception ex) {

				logger.error("Error al ejecutar la sentencia:"+ex);

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				throw ex;
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		
		public ArrayList obtenerListaElementosGenericos(String filtro, String idMunicipio,
											boolean noGeoReferenciado, boolean version, Object element, 
											int idMunicipioSeleccionado, String nombreTabla, ArrayList camposEspecificos) throws Exception{

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			ArrayList lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}
								
				String sql = "select "+nombreTabla+".*";
				if(noGeoReferenciado)	
					sql+=", ST_IsValid("+nombreTabla+".\"GEOMETRY\")AS valida ";
				
				//if (version)
				//	sql+=", versionesalfa.revision, iuseruserhdr.name, versionesalfa.fecha, versionesalfa.tipocambio  ";
				sql+=" from "+nombreTabla;
				//if(noGeoReferenciado)
				//	sql+="left join eiel_c_saneam_tcl on eiel_t_saneam_tcl.clave=eiel_c_saneam_tcl.clave AND eiel_t_saneam_tcl.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_saneam_tcl.codmunic=eiel_c_saneam_tcl.codmunic AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl ";
				//if(version){
				//	sql+="left join versionesalfa on eiel_t_saneam_tcl.revision_actual = versionesalfa.revision  ";
				//	sql+="left join iuseruserhdr on iuseruserhdr.id = versionesalfa.id_autor  ";
				//}
				/*if (idMunicipioSeleccionado==0){
					sql+=" where ((eiel_t_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.codprov,eiel_t_saneam_tcl.codmunic) IN ("
							+ "select eiel_tr_saneam_tcl_pobl.tramo_cl, eiel_tr_saneam_tcl_pobl.codprov_tcl, eiel_tr_saneam_tcl_pobl.codmunic_tcl from eiel_tr_saneam_tcl_pobl where "
							+ "eiel_tr_saneam_tcl_pobl.codprov_pobl='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_tr_saneam_tcl_pobl.codmunic_pobl='"
							+ idMunicipio.substring(2, 5)
							+ "'"
							+ ")"
							+ "OR "
							+ "eiel_t_saneam_tcl.codprov='"
							+ idMunicipio.substring(0, 2)
							+ "' and eiel_t_saneam_tcl.codmunic='"
							+ idMunicipio.substring(2, 5) + "')";
				}
				else{
					sql+=" where (eiel_t_saneam_tcl.codprov='"+ String.valueOf(idMunicipioSeleccionado).substring(0, 2)
							+ "' and eiel_t_saneam_tcl.codmunic='"+ String.valueOf(idMunicipioSeleccionado).substring(2, 5) + "')";	
				}*/
				
				sql+=" where "+nombreTabla+".id_municipio="+idMunicipio;

				if (filtro != null && !filtro.equals("")) {
					sql = sql + "  and " + filtro;
				}
				
				/*if (element!=null){
					ColectorEIEL obj = (ColectorEIEL)element;
					sql += " and eiel_t_saneam_tcl.clave = '"+obj.getClave()+"'";
					sql += " and eiel_t_saneam_tcl.codprov = '"+obj.getCodINEProvincia()+"'";
					sql += " and eiel_t_saneam_tcl.codmunic = '"+obj.getCodINEMunicipio()+"'";
					sql += " and eiel_t_saneam_tcl.tramo_cl = '"+obj.getCodOrden()+"'";
				}*/

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();
				VersionEiel versionEiel;
				while (rs.next()) {

					WorkflowEIEL elemento = new WorkflowEIEL();
					
					elemento.setRevisionActual(rs.getLong("revision_actual"));
					elemento.setRevisionExpirada(rs.getLong("revision_expirada"));
					if (String.valueOf(elemento.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_TEMPORAL)){
						elemento.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_TEMPORAL);
					}
					else if (String.valueOf(elemento.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_PUBLICABLE)){
						elemento.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_PUBLICABLE);
					}
					else if (String.valueOf(elemento.getRevisionExpirada()).equals(ConstantesServerGISEIEL.REVISION_VALIDA)){
						elemento.setEstadoValidacion(ConstantesServerGISEIEL.ESTADO_VALIDO);
					}

					//elemento.setClave(rs.getString("clave"));
					elemento.setIdElemento(rs.getLong("id"));
					elemento.setCodINEProvincia(rs.getString("codprov"));
					elemento.setCodINEMunicipio(rs.getString("codmunic"));
					
					LinkedHashMap  elementos=new LinkedHashMap ();
					Iterator it=camposEspecificos.iterator();
					while (it.hasNext()){
						GenericFieldEIEL genericField=(GenericFieldEIEL)it.next();
						switch(genericField.getType()){
		 	        	case CampoFiltroModel.VARCHAR_CODE:{
		 	        		elementos.put(genericField.getNombrecampo(),(rs.getString(genericField.getNombrecampo())));
		 	        		break;
		 	        	}
		 	        	case CampoFiltroModel.NUMERIC_CODE:{
		 	        		elementos.put(genericField.getNombrecampo(),(rs.getLong(genericField.getNombrecampo())));
		 	        		break;
		 	        	}
		 	        	default:
		 	        		break;		 	         	        	
		 	         }
					}
					elemento.setDatosAdicionales(elementos);

					//Si se hace una consulta de version, se obtienen los datos de la version
					/*if (version){
						versionEiel = new VersionEiel();
						versionEiel.setIdVersion(rs.getInt("revision"));
						versionEiel.setFecha(rs.getTimestamp("fecha"));
						versionEiel.setUsuario(rs.getString("name"));
						versionEiel.setAccion(rs.getString("tipocambio"));
						
						elemento.setVersion(versionEiel);
					}*/
						
						
					if(noGeoReferenciado){
						if(!rs.getBoolean("valida"))
							lstDatos.add(elemento);
					}else
						lstDatos.add(elemento);

				}



			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				throw ex;
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
		
		
		protected Collection obtenerFeaturesGenericas(WorkflowEIEL object,String idMunicipio,String nombreTabla) {

			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			Collection lstDatos = new ArrayList();

			try {

				connection = CPoolDatabase.getConnection();
				if (connection == null) {
					return null;
				}

				String sql = "select id,revision_expirada from "+nombreTabla+" where "
						+ "id_municipio='"+idMunicipio
						+ "' and "+nombreTabla+".id="
						+ object.getIdElemento()
						+ "";

				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();

				while (rs.next()) {

					int id = rs.getInt("id");
					long revision_expirada=0;				
					try {revision_expirada=rs.getLong("revision_expirada");	} catch (Exception e) {	}
					FeatureEIELSimple feature=new FeatureEIELSimple(id,String.valueOf(revision_expirada));
					lstDatos.add(feature);

				}

			} catch (Exception ex) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
			}
			finally{
				safeClose(rs, statement, connection);
			}

			return lstDatos;
		}
}

