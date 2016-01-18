package com.gestorfip.ws.servidor;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.gestorfip.ws.beans.configuracion.CRSGestor;
import com.gestorfip.ws.beans.configuracion.ConfiguracionGestor;
import com.gestorfip.ws.beans.configuracion.VersionesUER;
import com.gestorfip.ws.beans.tramite.ui.CaracteresDeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoRegimenRegimenesBean;
import com.gestorfip.ws.beans.tramite.ui.CondicionUrbanisticaCasoRegimenesBean;
import com.gestorfip.ws.beans.tramite.ui.DeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.DeterminacionReguladoraBean;
import com.gestorfip.ws.beans.tramite.ui.EntidadBean;
import com.gestorfip.ws.beans.tramite.ui.FipBean;
import com.gestorfip.ws.beans.tramite.ui.GrupoAplicacionBean;
import com.gestorfip.ws.beans.tramite.ui.OperacionDeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.RegulacionesEspecificasBean;
import com.gestorfip.ws.beans.tramite.ui.TipoOperacionDeterminacionBean;
import com.gestorfip.ws.beans.tramite.ui.TipoOperacionEntidadBean;
import com.gestorfip.ws.beans.tramite.ui.TramiteBean;
import com.gestorfip.ws.beans.tramite.ui.ValoresReferenciaBean;
import com.gestorfip.ws.utils.ConnectionUtilities;
import com.gestorfip.ws.utils.Constants;
import com.gestorfip.ws.xml.beans.importacion.migracionasistida.ConfLayerBean;
import com.gestorfip.ws.xml.beans.tramite.unidad.UnidadBean;
import com.gestorfip.ws.xml.parser.FIP1Handler;


public class OperacionesBBDD {

	static Logger logger = Logger.getLogger(OperacionesBBDD.class);
	//static Connection conn = null;
	
	private static RegulacionesEspecificasBean[] lstRegulacionesEspecificasBean = null;
	
	static Vector<File> results = new Vector<File>(); // Para almacenar el arbol de documentos

	/** Se obtiene el fichero de configuracion.
	 * @param keyProperty
	 * @return
	 * @throws Exception
	 */
	public static String obtenerPropiedadesConfiguracion(String keyProperty) throws Exception {
		InputStream inputStream = OperacionesBBDD.class.getClassLoader().getResourceAsStream(Constants.GESTORFIP_PROPERTIES_FILE);
			
		if(inputStream == null){
			inputStream = ClassLoader.getSystemResourceAsStream(Constants.GESTORFIP_PROPERTIES_FILE);
			if(inputStream == null ){
				logger.error("BD configuration file not found.");
				throw new Exception ();
			}
		}
		
		Class.forName("org.postgresql.Driver");
		Properties props = new Properties();

		props.load(inputStream);
		
		String valorPropiedad = props.getProperty(keyProperty);
		
		return valorPropiedad;
	}
	
    /** Obtiene el datasource de conexion a la BBDD
     * @return
     * @throws NamingException
     */
    public static DataSource getDataSource() throws NamingException 
    {
        Context initContext = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        return (DataSource)envContext.lookup("postgresql");
    }

	
	/**
	 * Connects to the Geopista DataBase
	 * @throws Exception
	 */

    
    /**
     * 
     */
    public static void gestionCapasFip1(){
//    	logger.info("gestionCapasFip1 - Inicio");
    	Connection conn = null;
    	try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
		} catch (Exception e) {
			logger.error("Error while connecting to the DB:");
			logger.error(e.getMessage());
			e.printStackTrace();
			//return Constants.DB_CONNEXION_ERROR_CODE;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,null,null);
		}
//    	logger.info("gestionCapasFip1 - Fin");
    }
	
	/**
	 * Insert FIP1 data into database
	 * @param idAmbito
	 * @param dateConsolidacionFipConsole
	 * @param handler
	 * @param idMunicipioLG
	 * @param lstLayersStyles
	 * @return
	 * @throws SQLException
	 */
	public static String insertarFIP1( int idAmbito, FIP1Handler handler, 
			int idMunicipioLG, ConfLayerBean[] lstConfLayerBean, int idEntidad ) throws SQLException { 
//		logger.info("insertarFIP1 - Inicio");
		String result = "";
		Connection conn = null;
		//Statement st = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();

			GestionLayersBBDD.gestionLayersMigracionAsistida(conn, lstConfLayerBean, idMunicipioLG, idEntidad);

			result = handler.getFip1().loadFip1IntoDB(conn, idAmbito, idMunicipioLG,  lstConfLayerBean);

		} catch (Exception e) {
			logger.error("Error while connecting to the DB:");
			logger.error(e.getMessage());
			e.printStackTrace();
			conn.rollback();
			return Constants.DB_CONNEXION_ERROR_CODE;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,null,null);
		}
//		logger.info("insertarFIP1 - Fin");
		return result;
	}
	
	
	/** Obtiene las determinaciones del fip asociadas al tramite
	 * @param idTramite
	 * @return
	 */
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasFip(int idTramite){
		
//		logger.info("obtenerDeterminacionesAsociadasFip - Inicio");
//		
//		logger.info("obtenerDeterminacionesAsociadasFip - idTramite="+idTramite);
//		
		long timeInicio = System.currentTimeMillis();
		
		DeterminacionBean[] lstDeterminacionBean = null;
		DeterminacionBean[] lstDeterminacionBean_ordenadoEtiqueta = null;
		String sSQL = "";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			st = conn.createStatement();
			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = " +idTramite;
			
			rs = st.executeQuery(sSQL);
			int totalDeter=0;
			while (rs.next()) {
				totalDeter = rs.getInt("total");
			}

			lstDeterminacionBean = new DeterminacionBean[totalDeter];
			lstDeterminacionBean_ordenadoEtiqueta = new DeterminacionBean[totalDeter];
			HashMap hashDeterBean = new HashMap();
			
			int i=0;
			
			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = " +idTramite+ " ORDER BY etiqueta ASC";
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));
				deterBean.setNombre(rs.getString("nombre"));
				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				//lstDeterminacionBean[i] = deterBean;
				lstDeterminacionBean_ordenadoEtiqueta[i] = deterBean;
				hashDeterBean.put(deterBean.getId(), deterBean);
				
				
				i++;
			}
			rs.close();
			st.close();
	
			hashDeterBean = obtenerDatosValoresReferenciaDeterminacion(conn, hashDeterBean, idTramite);
			
			hashDeterBean = obtenerDatosGrupoAplicacion(conn, hashDeterBean, idTramite);
						
			hashDeterBean = obtenerDatosDeterminacionReguladora(conn,hashDeterBean, idTramite);
		
			hashDeterBean =  obtenerDatosRegulacionesEspecificas(conn,hashDeterBean, idTramite);
				
			hashDeterBean = obtenerDatosOperacionDeterminacion(conn,hashDeterBean, idTramite);

			
			int index_aux =0;
			if(lstDeterminacionBean_ordenadoEtiqueta != null){
				for(int h=0; h<lstDeterminacionBean_ordenadoEtiqueta.length; h++){
					int id = lstDeterminacionBean_ordenadoEtiqueta[h].getId();
					lstDeterminacionBean[index_aux] = (DeterminacionBean)hashDeterBean.get(id);
					index_aux ++;
				}
				
			}
					
		} catch (Exception e) {
			logger.error("Exception al obtener las Determinaciones asociadas a un tramite:");
			logger.error(e);
			e.printStackTrace();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerDeterminacionesAsociadasFip - Fin");
		long timeFin = System.currentTimeMillis();
		return lstDeterminacionBean;
		
	}
	
	
	/**
	 * @param idTramite
	 * @return
	 */
	public static DeterminacionBean[] obtenerArbolDeterminacionesAsociadasTramite(int idTramite){
		
//		logger.info("obtenerArbolDeterminacionesAsociadasTramite - Inicio");
//		
//		logger.info("obtenerArbolDeterminacionesAsociadasTramite - idTramite="+idTramite);

		
		DeterminacionBean[] lstDeterminacionBean = null;
		DeterminacionBean[] lstDeterminacionBean_ordenadoEtiqueta = null;
		String sSQL = "";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			st = conn.createStatement();
			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = " +idTramite;
			
			rs = st.executeQuery(sSQL);
			int totalDeter=0;
			while (rs.next()) {
				totalDeter = rs.getInt("total");
			}
			
			lstDeterminacionBean = new DeterminacionBean[totalDeter];
			lstDeterminacionBean_ordenadoEtiqueta = new DeterminacionBean[totalDeter];
			
			int i=0;
			
			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = " +idTramite+ " ORDER BY etiqueta ASC";
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));
				deterBean.setNombre(rs.getString("nombre"));
				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				lstDeterminacionBean[i] = deterBean;
				//lstDeterminacionBean_ordenadoEtiqueta[i] = deterBean;
				
				i++;
			}
			rs.close();
			st.close();
				
					
		} catch (Exception e) {
			logger.error("Exception al obtener el arbol de Determinaciones asociadas a un tramite:");
			logger.error(e);
			e.printStackTrace();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerArbolDeterminacionesAsociadasTramite - Fin");
		long timeFin = System.currentTimeMillis();
		return lstDeterminacionBean;
		
	}
	

	/** Obtiene los datos de las determinaciones operadora y operada
	 * @param conn
	 * @param hashDeterminacionBean
	 * @param idTramite
	 * @return
	 */
	private static HashMap obtenerDatosOperacionDeterminacion(Connection conn , HashMap hashDeterminacionBean, int idTramite){
//		logger.info("obtenerDatosOperacionDeterminacion - Inicio");
		 
		String sSQL = "";
		try {
			OperacionDeterminacionBean operacionDeterminacion  =null; 
			Statement st = conn.createStatement();
			sSQL = "SELECT operdet.id, operdet.tipo, operdet.orden, operdet.texto, operdet.operadora_determinacionid, operdet.operada_determinacionid, operdet.tramite " +
					"FROM gestorfip.tramite_operacionesdeterminaciones AS operdet ,gestorfip.tramite AS tram " +
					"WHERE operdet.tramite = tram.id AND tram.id="+idTramite+" ORDER BY operdet.operada_determinacionid";
			ResultSet rs = st.executeQuery(sSQL);
			int index=0;
			while (rs.next()) {
				
				operacionDeterminacion = new OperacionDeterminacionBean();		
				  
				operacionDeterminacion.setId(rs.getInt("id"));
				operacionDeterminacion.setTipo(rs.getInt("tipo"));
				operacionDeterminacion.setOrden(rs.getLong("orden"));     
				operacionDeterminacion.setTexto(rs.getString("texto"));
				operacionDeterminacion.setOperadora_determinacionid(rs.getInt("operadora_determinacionid"));
				//operadaDeterminacionid = rs.getInt("operada_determinacionid");
				operacionDeterminacion.setTramite(rs.getInt("tramite"));
				
				DeterminacionBean deterOperada = new DeterminacionBean();
				deterOperada.setId(rs.getInt("operada_determinacionid"));
				operacionDeterminacion.setOperada_determinacion(deterOperada);
				
				if(((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion() == null){
					
					((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).setLstOperacionDeterminacion(new OperacionDeterminacionBean[1]); 
					((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion()[0] = operacionDeterminacion;
				}
				else{
					((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).setLstOperacionDeterminacion(Arrays.copyOf(
							((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion(), 
							((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion().length+1));
				
					((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion()[
					     ((DeterminacionBean)hashDeterminacionBean.get(operacionDeterminacion.getOperadora_determinacionid())).getLstOperacionDeterminacion().length-1] = operacionDeterminacion;
				}	
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las regulaciones especificas:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}	
		
//		logger.info("obtenerDatosOperacionDeterminacion - Fin");
		return hashDeterminacionBean;
	
	}
	
	/** Obtiene los datos de las regulaciones especificas
	 * @param conn
	 * @param hashDeterminacionBean
	 * @param idTramite
	 * @return
	 */
	private static HashMap obtenerDatosRegulacionesEspecificas(Connection conn ,HashMap hashDeterminacionBean, int idTramite){
//		logger.info("obtenerDatosRegulacionesEspecificas - Inicio");

		String sSQL = "";
		try {
			Statement st = conn.createStatement();
			
			sSQL = "SELECT detregesp.id, detregesp.orden, detregesp.nombre,  detregesp.texto,  detregesp.madre, detregesp.determinacion  " +
					"FROM gestorfip.tramite_determinacion_regulacionesespecificas AS detregesp, " +
					"gestorfip.tramite_determinaciones AS det ,gestorfip.tramite AS tram " +
					"WHERE detregesp.determinacion = det.id AND det.tramite=tram.id AND tram.id="+idTramite+" ORDER BY determinacion";
			ResultSet rs = st.executeQuery(sSQL);
			int index=0;
			while (rs.next()) {
				
				RegulacionesEspecificasBean regulacionEspecifica = new RegulacionesEspecificasBean();
				
				regulacionEspecifica.setId(rs.getInt("id"));
				regulacionEspecifica.setOrden(rs.getInt("orden"));
				regulacionEspecifica.setNombre(rs.getString("nombre"));
				regulacionEspecifica.setTexto(rs.getString("texto"));
				regulacionEspecifica.setMadre(rs.getInt("madre"));
				regulacionEspecifica.setDeterminacion(rs.getInt("determinacion"));	
				
				if(((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas() == null){
					
					((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).setLstRegulacionesEspecificas(new RegulacionesEspecificasBean[1]); 
					((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas()[0] = regulacionEspecifica;
				}
				else{
					((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).setLstRegulacionesEspecificas(Arrays.copyOf(
							((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas(), 
							((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas().length+1));
				
					((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas()[
					     ((DeterminacionBean)hashDeterminacionBean.get(regulacionEspecifica.getDeterminacion())).getLstRegulacionesEspecificas().length-1] = regulacionEspecifica;
				}	
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las regulaciones especificas:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}

//		logger.info("obtenerDatosRegulacionesEspecificas - Fin");
		return hashDeterminacionBean;
	}

	
	/** Obtienen los datos de los grupos de aplicacion
	 * @param conn
	 * @param hashDeterminacionBean
	 * @param idTramite
	 * @return
	 */
	private static HashMap obtenerDatosGrupoAplicacion( Connection conn ,HashMap hashDeterminacionBean, int idTramite){
//		logger.info("obtenerDatosGrupoAplicacion - Inicio");
		
		String sSQL = "";
		try {
			Statement st = conn.createStatement();
			sSQL = "SELECT detgrupo.id, detgrupo.determinacion, detgrupo.determinacionid  " +
					" FROM gestorfip.tramite_determinacion_gruposaplicacion AS detgrupo, gestorfip.tramite_determinaciones AS det ," +
					" gestorfip.tramite AS tram WHERE detgrupo.determinacion = det.id AND det.tramite=tram.id AND tram.id= "+idTramite+" ORDER BY determinacion";
			ResultSet rs = st.executeQuery(sSQL);
			int index=0;
			while (rs.next()) {
				
				GrupoAplicacionBean grupoAplic = new GrupoAplicacionBean();
				grupoAplic.setId(rs.getInt("id"));
				grupoAplic.setDeterminacionid(rs.getInt("determinacionid"));
				grupoAplic.setDeterminacion(rs.getInt("determinacion"));
				
				if(((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion() == null){
					
					((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).setLstGrupoAplicacion(new GrupoAplicacionBean[1]); 
					((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion()[0] = grupoAplic;
				}
				else{
					((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).setLstGrupoAplicacion(Arrays.copyOf(
							((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion(), 
							((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion().length+1));
				
					((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion()[
					     ((DeterminacionBean)hashDeterminacionBean.get(grupoAplic.getDeterminacion())).getLstGrupoAplicacion().length-1] = grupoAplic;
				}	
				
			
			}
		} catch (Exception e) {
			logger.error("Exception al obtener los grupos de aplicacion:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}

//		logger.info("obtenerDatosGrupoAplicacion - Fin");
		return hashDeterminacionBean;
	}
	
	
	/** Obtiene los datos de las determinaciones reguladoras
	 * @param conn
	 * @param hashDeterminacionBean
	 * @param idTramite
	 * @return
	 */
	private static HashMap obtenerDatosDeterminacionReguladora(Connection conn, HashMap hashDeterminacionBean, int idTramite){
		logger.info("obtenerDatosDeterminacionReguladora - Inicio");
		
		String sSQL = "";
		try {
			Statement st = conn.createStatement();
			sSQL = "SELECT detreg.id, detreg.determinacion, detreg.determinacionid  FROM gestorfip.tramite_determinacion_determinacionesreguladoras AS" +
			" detreg,gestorfip.tramite_determinaciones AS det ,gestorfip.tramite AS tram WHERE detreg.determinacion = det.id " +
			"AND det.tramite=tram.id AND tram.id="+idTramite +" ORDER BY determinacion";
			ResultSet rs = st.executeQuery(sSQL);
			int index=0;
			while (rs.next()) {
				
				DeterminacionReguladoraBean deterReguladora = new DeterminacionReguladoraBean();
				deterReguladora.setId(rs.getInt("id"));
				deterReguladora.setDeterminacionid(rs.getInt("determinacionid"));
				deterReguladora.setDeterminacion(rs.getInt("determinacion"));		
				
				if(((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora() == null){
					
					((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).setLstDeterminacionReguladora(new DeterminacionReguladoraBean[1]); 
					((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora()[0] = deterReguladora;
				}
				else{
					((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).setLstDeterminacionReguladora(Arrays.copyOf(
							((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora(), 
							((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora().length+1));
				
					((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora()[
					     ((DeterminacionBean)hashDeterminacionBean.get(deterReguladora.getDeterminacion())).getLstDeterminacionReguladora().length-1] = deterReguladora;
				}	
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las determinaciones reguladoras:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}

//		logger.info("obtenerDatosDeterminacionReguladora - Fin");
		return hashDeterminacionBean;
	}
	
	
	/**
	 * @param conn
	 * @param hashDeterminacionBean
	 * @param idTramite
	 * @return
	 */
	private static HashMap obtenerDatosValoresReferenciaDeterminacion(Connection conn ,HashMap hashDeterminacionBean, int idTramite){
//		logger.info("obtenerDatosValoresReferenciaDeterminacion - Inicio");
		
		String sSQL = "";
		try {
			Statement st = conn.createStatement();
			sSQL = "SELECT detvalref.id, detvalref.determinacion, detvalref.determinacionid  " +
					 	" FROM gestorfip.tramite_determinacion_valoresreferencia AS detvalref," +
					 	"gestorfip.tramite_determinaciones AS det ,gestorfip.tramite AS tram " +
					 	"WHERE detvalref.determinacion = det.id AND det.tramite=tram.id AND tram.id="+idTramite +" ORDER BY determinacion";
			ResultSet rs = st.executeQuery(sSQL);
			int index=0;
			while (rs.next()) {
				
				ValoresReferenciaBean valRef = new ValoresReferenciaBean();
				valRef.setId(rs.getInt("id"));
				valRef.setDeterminacionid(rs.getInt("determinacionid"));
				valRef.setDeterminacion(rs.getInt("determinacion"));
				
				if(((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia() == null){
					
					((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).setLstValoresReferencia(new ValoresReferenciaBean[1]); 
					((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia()[0] = valRef;
				}
				else{
					((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).setLstValoresReferencia(Arrays.copyOf(
							((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia(), 
							((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia().length+1));
				
					((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia()[
					     ((DeterminacionBean)hashDeterminacionBean.get(valRef.getDeterminacion())).getLstValoresReferencia().length-1] = valRef;
				}	
				
			
			}
		} catch (Exception e) {
			logger.error("Exception al obtener los datos de referencia de la determinacion:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}
				
//		logger.info("obtenerDatosValoresReferenciaDeterminacion - Fin");
		return hashDeterminacionBean;

	}
	

	public static TramiteBean obtenerDatosTramite(int idTramite){
//		logger.info("obtenerDatosTramite - Inicio");
		TramiteBean tramiteBean = null;
		String sSQL = "";
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			Statement st = conn.createStatement();
		
			sSQL = "SELECT * FROM gestorfip.tramite WHERE id=" + idTramite;
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
				tramiteBean = new TramiteBean();
				tramiteBean.setId(rs.getInt("id"));
				tramiteBean.setCodigo(rs.getString("codigo"));
				tramiteBean.setTexto(rs.getString("texto"));
				tramiteBean.setFip(rs.getInt("fip"));
			}
			
			rs.close();
			st.close();
			
		} catch (Exception e) {
			logger.error("Exception al obtener los datos del tramite:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,null,rs);
		}
		
//		logger.info("obtenerDatosTramite - Fin");
		return tramiteBean;
	}
	
	public static TramiteBean[] obtenerTramitesAsociadosFip(Connection conn, int idFip){
//		logger.info("obtenerTramitesAsociadosFip - Inicio");
		String sSQL = "";
		PreparedStatement pst = null;
		TramiteBean[] lstTramiteBean = null;
		ResultSet rs = null;
		
		try {

			// Carga en BD de los datos
			//if(conn == null || conn.isClosed()) connectToBD();
			
			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite WHERE fip=?";
			
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idFip);
			
			 rs = pst.executeQuery();
			int totalTramites = 0;
			while (rs.next()) {
				totalTramites = rs.getInt("total");
			}
			
			lstTramiteBean = new TramiteBean[totalTramites];
			
			sSQL = "SELECT * FROM gestorfip.tramite WHERE fip=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idFip);
			
			rs = pst.executeQuery();
			int index =0;
			while (rs.next()) {
				TramiteBean tramiteBean = new TramiteBean();
				tramiteBean.setId(rs.getInt("id"));
				tramiteBean.setCodigo(rs.getString("codigo"));
				tramiteBean.setTexto(rs.getString("texto"));
				tramiteBean.setFip(rs.getInt("fip"));
				
				lstTramiteBean[index] = tramiteBean;
				index ++;
			}	
			
		}
		catch (Exception e) {
			logger.error("Exception al guardar los datos de los tramites asociados al fip:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

//		logger.info("obtenerTramitesAsociadosFip - Fin");
		return lstTramiteBean;
	}
	
	public static TramiteBean obtenerTramitesEncargadoAsociadosFip(int idFip){
//		logger.info("obtenerTramitesEncargadoAsociadosFip - Inicio");
		String sSQL = "";
		PreparedStatement pst = null;
		TramiteBean tramiteBean = new TramiteBean();
		ResultSet rs = null;
		ResultSet rs2 = null;
		Connection conn = null;
		try {

			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.tramite WHERE fip=? " +
					"AND id NOT IN (SELECT tramite FROM gestorfip.catalogosistematizado) " +
					"AND id NOT IN (SELECT tramite FROM gestorfip.planeamientovigente)";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idFip);

			rs = pst.executeQuery();
			int index =0;
			while (rs.next()) {
				
				tramiteBean.setId(rs.getInt("id"));
				tramiteBean.setCodigo(rs.getString("codigo"));
				tramiteBean.setTexto(rs.getString("texto"));
				tramiteBean.setFip(rs.getInt("fip"));
			}	
			
			sSQL = "SELECT nombre FROM gestorfip.planeamientoencargado WHERE fip=? ";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idFip);
			rs2 = pst.executeQuery();
			while (rs2.next()) {
				tramiteBean.setNombre(rs2.getString("nombre"));
			}
		}
		catch (Exception e) {
			logger.error("Exception al obtenerTramitesEncargadoAsociadosFip:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerTramitesEncargadoAsociadosFip - Fin");
		return tramiteBean;
	}

	public static boolean buscarSiTramiteEsCatalogoSistematizado(Connection conn, int idTramite){
//		logger.info("buscarSiTramiteEsCatalogoSistematizado - Inicio");
		boolean isCatalogoSistematizado = false;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// Carga en BD de los datos
			//if(conn == null || conn.isClosed()) connectToBD();
			
			sSQL = "SELECT * FROM gestorfip.catalogosistematizado WHERE tramite=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idTramite);
			
			 rs = pst.executeQuery();
			if(rs.next()){
				isCatalogoSistematizado = true;
			}
			
			
		} catch (Exception e) {
			logger.error("Exception al buscar si el tramite es de catalogo sistematizado:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		/*finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}*/
//		logger.info("buscarSiTramiteEsCatalogoSistematizado - Fin");
		return isCatalogoSistematizado;
	}
	
	
	public static boolean buscarSiTramiteEsPlaneamientoVigente(Connection conn, int idTramite){
//		logger.info("buscarSiTramiteEsPlaneamientoVigente - Inicio");
		boolean isCatalogoSistematizado = false;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// Carga en BD de los datos
			//if(conn == null || conn.isClosed()) connectToBD();
			
			sSQL = "SELECT * FROM gestorfip.planeamientovigente WHERE tramite=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idTramite);
			
			 rs = pst.executeQuery();
			if(rs.next()){
				isCatalogoSistematizado = true;
			}
		} catch (Exception e) {
			logger.error("Exception al buscar si el tramite es de planeamiento vigente:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		/*finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}*/
//		logger.info("buscarSiTramiteEsPlaneamientoVigente - Fin");
		return isCatalogoSistematizado;
		
	}
	
	
	public static FipBean obtenerDatosIdentificacionFip(int idFip){
//		logger.info("obtenerDatosIdentificacionFip - Inicio");
		FipBean fipBean = null;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.fip WHERE id=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idFip);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				fipBean = new FipBean();
				fipBean.setId(rs.getInt("id"));
				fipBean.setPais(rs.getString("pais"));
				fipBean.setVersion(rs.getString("version"));
				fipBean.setSrs(rs.getString("srs"));
				//fipBean.setFecha(rs.getDate("fecha"));
			}
		} catch (Exception e) {
			logger.error("Exception al obtener los datos identificadotes del fip:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDatosIdentificacionFip - Fin");
		return fipBean;
	}
	
	
	
	public static FipBean obtenerDatosFip(int idFip){
//		logger.info("obtenerDatosFip - Inicio");
		FipBean fipBean = null;
		 
		TramiteBean tramiteCatalogoSistematizado = null;
		TramiteBean tramitePlaneamientoEncargado = null;
		TramiteBean tramitePlaneamientoVigente = null;;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.fip WHERE id=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idFip);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				fipBean = new FipBean();
				fipBean.setId(rs.getInt("id"));
				fipBean.setPais(rs.getString("pais"));
				fipBean.setVersion(rs.getString("version"));
				fipBean.setSrs(rs.getString("srs"));
			}
			
			TramiteBean[] lstTramiteBean = obtenerTramitesAsociadosFip(conn, idFip);
			if(lstTramiteBean != null){
				for(int i=0; i<lstTramiteBean.length; i++){
					TramiteBean tramite = lstTramiteBean[i];
					
					
					if(buscarSiTramiteEsCatalogoSistematizado(conn, tramite.getId())){
						// el tramite es catalogo sistematizado
						tramiteCatalogoSistematizado = tramite;
						
					}
					else if(buscarSiTramiteEsPlaneamientoVigente(conn, tramite.getId())){
						tramitePlaneamientoVigente = tramite;
					}
					else{
						//el tramite es planeamiento encargado
						tramitePlaneamientoEncargado  = tramite;
					}
				}
			}
			fipBean.setTramiteCatalogoSistematizado(tramiteCatalogoSistematizado);
			fipBean.setTramitePlaneamientoEncargado(tramitePlaneamientoEncargado);
			fipBean.setTramitePlaneamientoVigente(tramitePlaneamientoVigente);
			
		} catch (Exception e) {
			logger.error("Exception al obtener los datos del fip:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDatosFip - Fin");
		return fipBean;
	}
	
	
	
	public static TipoOperacionDeterminacionBean[] obtenerLstTiposOperacionDeterminaciones(int idFip ,Logger logger) throws Exception  {
//		logger.info("obtenerLstTiposOperacionDeterminaciones - Inicio");
		TipoOperacionDeterminacionBean[] lstTipoOperacionBean = null;
		String sSQL = "";
		int numTiposOperacion = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			st = conn.createStatement();
			
			sSQL="SELECT COUNT(*) AS total FROM gestorfip.diccionario_tiposoperaciondeterminacion " +
					" WHERE fip=" + idFip;
			
			rs = st.executeQuery(sSQL);
			while (rs.next()) {
				numTiposOperacion = rs.getInt("total");		
			}
			
			lstTipoOperacionBean = new TipoOperacionDeterminacionBean[numTiposOperacion];
			
			sSQL="SELECT * FROM gestorfip.diccionario_tiposoperaciondeterminacion" +
					" WHERE fip=" + idFip;
			
			rs = st.executeQuery(sSQL);
		
			int i = 0;
			while (rs.next()) {
				TipoOperacionDeterminacionBean tob = new TipoOperacionDeterminacionBean();
				tob.setId(rs.getInt("id"));
				tob.setCodigo(rs.getString("codigo"));
				tob.setNombre(rs.getString("nombre"));
				tob.setFip(rs.getInt("fip"));
				lstTipoOperacionBean[i] = tob;
				i++;
			}
			
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception al obtener la lista de tipos de operaciones de determinaciones:");
			logger.error(e);
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerLstTiposOperacionDeterminaciones - Fin");
		return lstTipoOperacionBean;
	}
	
	public static TipoOperacionEntidadBean[] obtenerLstTiposOperacionEntidades(int idFip , Logger logger) throws Exception  {
//		logger.info("obtenerLstTiposOperacionEntidades - Inicio");
		TipoOperacionEntidadBean[] lstTipoOperacionBean = null;
		String sSQL = "";
		int numTiposOperacion = 0;
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			 st = conn.createStatement();
			
			sSQL="SELECT COUNT(*) AS total FROM gestorfip.diccionario_tiposoperacionentidad " +
					" WHERE fip=" + idFip;
			
			 rs = st.executeQuery(sSQL);
			while (rs.next()) {
				numTiposOperacion = rs.getInt("total");		
			}
			
			lstTipoOperacionBean = new TipoOperacionEntidadBean[numTiposOperacion];
			
			sSQL="SELECT * FROM gestorfip.diccionario_tiposoperacionentidad " +
					" WHERE fip=" + idFip;
			
			rs = st.executeQuery(sSQL);
		
			int i = 0;
			while (rs.next()) {
				TipoOperacionEntidadBean tob = new TipoOperacionEntidadBean();
				tob.setId(rs.getInt("id"));
				tob.setCodigo(rs.getString("codigo"));
				tob.setNombre(rs.getString("nombre"));
				tob.setFip(rs.getInt("fip"));
				lstTipoOperacionBean[i] = tob;
				i++;
			}
			
			rs.close();
			st.close();

		} catch (Exception e) {
			logger.error("Exception al obtener la lista de tipos de operaciones de entidades:");
			logger.error(e);
			e.printStackTrace();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerLstTiposOperacionEntidades - Fin");
		return lstTipoOperacionBean;
	}
	
	
	
	public static int obtenerValorSecuenciaCliente (String nombreSecuencia){
//		logger.info("obtenerValorSecuencia - Fin");
		Connection conn = null;
		int secuencia = -1;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			secuencia = obtenerValorSecuencia(conn, nombreSecuencia);
			
		} catch (Exception e) {
			logger.error("Exception al obtener un valor de la secuencia -- nombreSecuencia="+nombreSecuencia);
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//ConnectionUtilities.closeConnection(conn,null,null);
		}
		finally{
			ConnectionUtilities.closeConnection(conn,null,null);
		}
//		logger.info("obtenerValorSecuencia - Fin");
		return secuencia;
	}
	
	public static int obtenerValorSecuencia (Connection conn, String nombreSecuencia){
//		logger.info("obtenerValorSecuencia - Inicio");
		int secuencia = -1;
		PreparedStatement pst = null;
		ResultSet rs = null;
		//Connection conn = null;
		try {
			// Conexion a la BD
			//conn = getDataSource().getConnection();;
			//if(conn == null || conn.isClosed()) throw new Exception ();
			
			logger.info("obtenerValorSecuencia - nombreSecuencia="+nombreSecuencia);
			secuencia = secuencia(conn, nombreSecuencia, pst, rs);
			logger.info("obtenerValorSecuencia - secuencia="+secuencia);
			//ConnectionUtilities.closeConnection(conn,pst,rs);
			
		} catch (Exception e) {
			logger.error("Exception al obtener un valor de la secuencia -- nombreSecuencia="+nombreSecuencia);
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerValorSecuencia - Fin");
		return secuencia;
	}
	
	
	private static int secuencia(Connection conn, String nombreSecuencia, PreparedStatement pst, ResultSet rs) throws SQLException{
//		logger.info("secuencia - Inicio");
		int secuencia = 0;
		
		pst = conn.prepareStatement("SELECT nextval(?)");
	
		pst.setString(1, nombreSecuencia);
		rs = pst.executeQuery();
		while (rs.next()){
			secuencia = rs.getInt(1);
		}
//		logger.info("secuencia - Fin");
		return secuencia;
	}
	
	
	/*******************************************************************************
	 * 							ENTIDADES
	 *******************************************************************************/

	public static EntidadBean[] obtenerArbolEntidadesAsociadasTramite(int idTramite){
//		logger.info("obtenerArbolEntidadesAsociadasTramite - Inicio");
		EntidadBean[] lstEntidadBean = null;
		String sSQL = "";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			 st = conn.createStatement();
			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_entidades " +
					" WHERE tramite = " +idTramite;
							
			 rs = st.executeQuery(sSQL);
			int totalDeter=0;
			while (rs.next()) {
				totalDeter = rs.getInt("total");
			}
			
			lstEntidadBean = new EntidadBean[totalDeter];
			
			int i=0;
			
			sSQL = "SELECT * FROM gestorfip.tramite_entidades WHERE tramite = " +idTramite +
						" ORDER BY nombre ASC";
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
			
				EntidadBean entBean = new EntidadBean();
				entBean.setId(rs.getInt("id"));
				entBean.setCodigo(rs.getString("codigo"));
				entBean.setClave(rs.getString("clave"));
				entBean.setNombre(rs.getString("nombre"));
				entBean.setEtiqueta(rs.getString("etiqueta"));
				entBean.setSuspendida(rs.getBoolean("suspendida"));
				entBean.setBase_entidadid(rs.getInt("base_entidadid"));
				entBean.setMadre(rs.getInt("madre"));
				entBean.setTramite(rs.getInt("tramite"));
				entBean.setIdFeature(rs.getInt("idfeature"));
				entBean.setIdLayer(rs.getInt("idlayer"));

				lstEntidadBean[i] = entBean;
				
				i++;
			}
			rs.close();
			st.close();
			
		} catch (Exception e) {
			logger.error("Exception al obtener el arbol de Entidades asociadas a un tramite:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerArbolEntidadesAsociadasTramite - Fin");
		return lstEntidadBean;
	}

	
	/****************************************************************************************
	 * 							 FIN ENTIDADES
	 ***************************************************************************************/

	
	/***************************************************************************************
	 * 							DETERMINACIONES-ENTIDADES
	 **************************************************************************************/
	
	public static EntidadBean[] obtenerEntidadesAsociadasToDeterminacion(int idDeterminacion){
//		logger.info("obtenerEntidadesAsociadasToDeterminacion - Inicio");
		EntidadBean[] lstEntidadesBean = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.tramite_condicionesurbanisticas " +
					" WHERE codigodeterminacion_determinacionid = ?";
			
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idDeterminacion);
			
			rs = pst.executeQuery();
			
			ArrayList lstIdsEntidadesAsociadas = new ArrayList();
			while (rs.next()) {
				lstIdsEntidadesAsociadas.add(new Integer(rs.getInt("codigoentidad_entidadid")));
			}
			
			lstEntidadesBean = new EntidadBean[lstIdsEntidadesAsociadas.size()];
			
			for(int i=0; i<lstIdsEntidadesAsociadas.size(); i++){
				
				int idEntidad = ((Integer)lstIdsEntidadesAsociadas.get(i)).intValue();
				

				sSQL = "SELECT * FROM gestorfip.tramite_entidades " +
				" WHERE id = ?";
				
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, idEntidad);
				rs = pst.executeQuery();
				
				while (rs.next()) {
					
					EntidadBean entBean = new EntidadBean();
					entBean.setId(rs.getInt("id"));
					entBean.setCodigo(rs.getString("codigo"));
					entBean.setClave(rs.getString("clave"));
					entBean.setNombre(rs.getString("nombre"));
					entBean.setEtiqueta(rs.getString("etiqueta"));
					entBean.setSuspendida(rs.getBoolean("suspendida"));
					entBean.setBase_entidadid(rs.getInt("base_entidadid"));
					entBean.setMadre(rs.getInt("madre"));
					entBean.setTramite(rs.getInt("tramite"));
					entBean.setIdFeature(rs.getInt("idfeature"));
					entBean.setIdLayer(rs.getInt("idlayer"));
					
					lstEntidadesBean[i] = entBean;
				}
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las entidades asociadas a una determinacion:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
//		logger.info("obtenerEntidadesAsociadasToDeterminacion - Fin");
		return lstEntidadesBean;
	}
	
	
	
	public static DeterminacionBean[] obtenerDeterminacionesByTipoCaracterDeterminacionEnCondicionesUrbanisticas(
			int idFip, int idTramiteCatalogoSistematizado, int idTramitePlaneamientoVigente,
			int idTramitePlaneamientoEncargado,	String caracterDeterminacionProperty){
//		logger.info("obtenerDeterminacionesDeTipoCaracterDeterminacion - Inicio");
		DeterminacionBean[] lstDeterminacionesBean = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs= null;
		Connection conn = null;
		try {
			
			String caracterDeterminacion = obtenerPropiedadesConfiguracion(caracterDeterminacionProperty);

			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.diccionario_caracteresdeterminacion " +
			" WHERE fip = ? AND codigo =?";
			
			pst = conn.prepareStatement(sSQL);
			
			pst.setInt(1, idFip);
			pst.setString(2, caracterDeterminacion);
			
			rs = pst.executeQuery();
			int idCaracterDeterminacionValorReferencia=0;
			while (rs.next()) {
				idCaracterDeterminacionValorReferencia = rs.getInt("id");
			}

			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_determinaciones " +
			" WHERE tramite IN (?, ?, ?) AND caracterid =?";
			
			pst = conn.prepareStatement(sSQL);
			
			pst.setInt(1, idTramiteCatalogoSistematizado);
			pst.setInt(2, idTramitePlaneamientoVigente);
			pst.setInt(3, idTramitePlaneamientoEncargado);
			pst.setInt(4, idCaracterDeterminacionValorReferencia);
			
			rs = pst.executeQuery();

			int totalDeterminaciones = 0;
			while (rs.next()) {
				totalDeterminaciones = rs.getInt("total");
			}
			
			lstDeterminacionesBean = new DeterminacionBean[totalDeterminaciones];

			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
			" WHERE tramite = ? AND caracterid =? ORDER BY etiqueta";
			
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idTramiteCatalogoSistematizado);
			pst.setInt(2, idCaracterDeterminacionValorReferencia);
			
			rs = pst.executeQuery();
			
			int index=0;
			while (rs.next()) {
				
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));

				deterBean.setNombre("CS - "+rs.getString("apartado")+" - "+rs.getString("nombre"));

				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				lstDeterminacionesBean[index] = deterBean;
				index ++;
			}
			
			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
			" WHERE tramite = ? AND caracterid =? ORDER BY etiqueta";
			
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idTramitePlaneamientoVigente);
			pst.setInt(2, idCaracterDeterminacionValorReferencia);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));

				deterBean.setNombre("PV - "+rs.getString("apartado")+" - "+rs.getString("nombre"));

				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				lstDeterminacionesBean[index] = deterBean;
				index ++;
			}
			
			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
			" WHERE tramite = ? AND caracterid =? ORDER BY etiqueta";
			
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idTramitePlaneamientoEncargado);
			pst.setInt(2, idCaracterDeterminacionValorReferencia);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));

				deterBean.setNombre("PE - "+rs.getString("apartado")+" - "+rs.getString("nombre"));

				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				lstDeterminacionesBean[index] = deterBean;
				index ++;
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las entidades asociadas a una determinacion:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDeterminacionesDeTipoCaracterDeterminacion - Fin");
		return lstDeterminacionesBean;
	}
	
	/**
	 * @param idTramiteCatalogoSistematizado
	 * @param idTramitePlaneamientoVigente
	 * @param idTramitePlaneamientoEncargado
	 * @param lstDeterminaciones
	 * @return
	 */
	public static DeterminacionBean[] obtenerDetValoresReferenciaCondicionUrbanistica(
			int idTramiteCatalogoSistematizado, int idTramitePlaneamientoVigente,
			int idTramitePlaneamientoEncargado, DeterminacionBean[] lstDeterminaciones){
		
//		logger.info("obtenerDetValoresReferenciaCondicionUrbanistica - Inicio");
		DeterminacionBean[] lstDeterminacionesValRef = null;
		
		StringBuffer sSQL = new StringBuffer();
		PreparedStatement pst = null;
		ResultSet rs= null;
		Connection conn = null;
		try {
			
			
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();

			sSQL.append( "SELECT * from gestorfip.tramite_determinaciones where id IN ( " +
				       " SELECT determinacionid from gestorfip.tramite_determinacion_valoresreferencia where determinacion  IN " +
				       " (");
			sSQL.append(lstDeterminaciones[0].getId());
			
			for (int i=1; i<lstDeterminaciones.length; i++ ){
				sSQL.append(",");
				DeterminacionBean det = lstDeterminaciones[i];
				sSQL.append(det.getId());
				
			}
		
			sSQL.append("))");
				
			pst = conn.prepareStatement(sSQL.toString());
				
			rs = pst.executeQuery();
			
			int index=0;
			while (rs.next()) {
			
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));
				deterBean.setTramite(rs.getInt("tramite"));
				if(deterBean.getTramite() == idTramiteCatalogoSistematizado){
					deterBean.setNombre("CS - "+rs.getString("apartado")+" - "+rs.getString("nombre"));
				}
				else if(deterBean.getTramite() == idTramitePlaneamientoVigente){
					deterBean.setNombre("PV - "+rs.getString("apartado")+" - "+rs.getString("nombre"));
				}
				else if(deterBean.getTramite() == idTramitePlaneamientoEncargado){
					deterBean.setNombre("PE - "+rs.getString("apartado")+" - "+rs.getString("nombre"));
				}

				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				

				//lstDeterminacionesValRef[index] = deterBean;
				
				
				if(lstDeterminacionesValRef == null){
					
					lstDeterminacionesValRef = new DeterminacionBean[1];
					lstDeterminacionesValRef[0] = deterBean;
				
				}
				else{
					lstDeterminacionesValRef = (DeterminacionBean[]) Arrays.copyOf(
							lstDeterminacionesValRef, lstDeterminacionesValRef.length+1);
				
					lstDeterminacionesValRef[lstDeterminacionesValRef.length-1] = deterBean;
				}	
				
			}
			
			
		} catch (Exception e) {
			logger.error("Exception al obtener las entidades asociadas a una determinacion:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDeterminacionesDeTipoCaracterDeterminacion - Fin");
		return lstDeterminacionesValRef;
	}
	
	
	public static DeterminacionBean[]  obtenerDetAplicablesEntidad(int idEntidad){
//		logger.info("obtenerDetAplicablesEntidad - Inicio");
		DeterminacionBean[] lstDeterminacionesBean = null;
		
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs= null;
		Connection conn = null;
		try {
		
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			String caracterDeterminacionGrupoEntidades = obtenerPropiedadesConfiguracion("caracterDeterminacion_grupoentidades");
			
			sSQL = "SELECT determinacionid FROM gestorfip.tramite_determinacion_gruposaplicacion "+
					"WHERE determinacionid IN(SELECT valorreferencia_determinacionid FROM gestorfip.tramite_condicionurbanistica_caso_regimenes "+
					"WHERE caso IN  (SELECT id from gestorfip.tramite_condicionurbanistica_casos WHERE condicionurbanistica IN  ( " +
					"SELECT id FROM gestorfip.tramite_condicionesurbanisticas WHERE codigoentidad_entidadid = ?)))";
			
			pst = conn.prepareStatement(sSQL);
			
			pst.setInt(1, idEntidad);
			rs = pst.executeQuery();




			while (rs.next()) {
			
				DeterminacionBean det = new DeterminacionBean();
				det.setId(rs.getInt("determinacionid"));
			
				if(lstDeterminacionesBean == null){
					
					lstDeterminacionesBean = new DeterminacionBean[1];
					lstDeterminacionesBean[0] = det;
				
				}
				else{
					lstDeterminacionesBean = (DeterminacionBean[]) Arrays.copyOf(
							lstDeterminacionesBean, lstDeterminacionesBean.length+1);
				
					lstDeterminacionesBean[lstDeterminacionesBean.length-1] = det;
				}	
			}
			
			
		} catch (Exception e) {
			logger.error("Exception al obtener las determinaciones aplicables a una determinacion:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDetAplicablesEntidad - Fin");
		return lstDeterminacionesBean;
	}
			
		
	public static CondicionUrbanisticaCasoRegimenesBean[] 
	                    obtenerRegimenesCasoCondicionUrbanistica(int idCaso){
//		logger.info("obtenerRegimenesCasoCondicionUrbanistica - Inicio");
		CondicionUrbanisticaCasoRegimenesBean[] lstCUCR = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			
			
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();

			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_condicionurbanistica_caso_regimenes " +
			" WHERE caso = ?";
			
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idCaso);
			
			rs = pst.executeQuery();
			
			int totalCasoRegimenes = -1;
			while (rs.next()) {
				
				totalCasoRegimenes = rs.getInt("total");
			}
			
			lstCUCR = new CondicionUrbanisticaCasoRegimenesBean[totalCasoRegimenes];
			
			sSQL = "SELECT *FROM gestorfip.tramite_condicionurbanistica_caso_regimenes " +
			" WHERE caso = ?";
			
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idCaso);
			
			rs = pst.executeQuery();
			
			int indexCUCR = 0;
			while (rs.next()) {
				CondicionUrbanisticaCasoRegimenesBean cucr = new CondicionUrbanisticaCasoRegimenesBean();
				cucr.setId(rs.getInt("id"));
				cucr.setCaso(rs.getInt("caso"));
				cucr.setComentario(rs.getString("comentario"));
				cucr.setDeterminacionregimen_determinacionid(rs.getInt("determinacionregimen_determinacionid"));
				cucr.setValorreferencia_determinacionid(rs.getInt("valorreferencia_determinacionid"));
				cucr.setCasoaplicacion_casoid(rs.getInt("casoaplicacion_casoid"));
				cucr.setValor(rs.getString("valor"));
				cucr.setSuperposicion(rs.getInt("superposicion"));
				
				lstCUCR[indexCUCR] = cucr;
				indexCUCR ++;
				
			}
			
		}
		 catch (Exception e) {
			logger.error("Exception al obtener los regimenes asociados al caso de la condicion urbanistica:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerRegimenesCasoCondicionUrbanistica - Fin");
		return lstCUCR;
	}

	
	public static boolean eliminarCasosCondicionesUrbanisticas(int[] lstIdCasos) {
//		logger.info("eliminarCasosCondicionesUrbanisticas - Inicio");
		boolean estado= false;

		PreparedStatement pst = null;
		boolean autoCommit = true;
		String sSQL = "";
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			
			if(lstIdCasos != null && lstIdCasos.length!=0 &&
					lstIdCasos != null && lstIdCasos.length!=0){
				
				for(int i=0; i<lstIdCasos.length; i++){
					sSQL = "DELETE FROM gestorfip.tramite_condicionurbanistica_casos " +
							" WHERE id = ?";
					
					pst = conn.prepareStatement(sSQL);
		
					pst.setInt(1, lstIdCasos[i]);

					pst.executeUpdate();
				}
			}
			conn.commit();
			estado = true;
			

		} catch (Exception e) {
			logger.error("Exception al eliminar los casos condiciones urbanisticas");
			logger.error(e);
			System.out.println(e);
			estado = false;	
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("eliminarCasosCondicionesUrbanisticas - Fin");
		return estado;
	}
	
	/***************************************************************************************
	 * 							FIN ASOCIACION DETERMINACIONES-ENTIDADES
	 **************************************************************************************/

	/***************************************************************************************
	 * 							VALIDACIONES
	 **************************************************************************************/
	/**
	 * Devuelve la lista de los caracteres de determinaciones del diccionario perteneciendo al fip dado.
	 * Usada por el Editor Fip para efectuar la validacion de las determinaciones.
	 * @param fip: el id del fip que contiene el diccionario
	 * @return una Hashtable que contiene los ids y literales de todos los carecteres de determinacion contenidos en el diccionario 
	 */
	public Hashtable<Integer, String> getDiccionarioCaracteresDeterminacion(int fip) {
//		logger.info("getDiccionarioCaracteresDeterminacion - Inicio");
		PreparedStatement pst = null;
		String sSQL = "SELECT * FROM gestorfip.diccionario_caracteresdeterminacion WHERE fip = ?";
		ResultSet rs = null;
		Hashtable<Integer, String> results = new Hashtable<Integer, String>();
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			sSQL = "";
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, fip);
			rs = pst.executeQuery(sSQL);
			while(rs.next()) {
				results.put(rs.getInt("id"), rs.getString("nombre"));
			}
			logger.info("getDiccionarioCaracteresDeterminacion - Fin");
			return results;
		} catch (Exception e) {
			logger.error("Exception al obtener los caracteres de determinaciones");
			logger.error(e);
			e.printStackTrace();
			return null;
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
	}
	
	/*******************************************************************************
	 * 							UNIDADES
	 *******************************************************************************/
	
		
	
	/**********************************************************************************************
	 * 				 INICIO LOURED
	 * 
	 **********************************************************************************************/
	

	public static String obtenerFechaConsolidacionFip(int idAmbito) throws Exception{
		String fecha= null;
	
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT fechaconsolidacion FROM gestorfip.fip WHERE idambito=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idAmbito);
			
			rs = pst.executeQuery();
			ArrayList<String> lstFechas = new ArrayList<String>();
			while (rs.next()) {
				lstFechas.add(rs.getString("fechaconsolidacion"));
			}
			
			Date dateBBDD = null;
			if(lstFechas.size() > 0){
				dateBBDD = new Date(0);
				dateBBDD = dateBBDD.valueOf(lstFechas.get(0));
			}
			for (int i=1; i<lstFechas.size(); i++){
				Date dateBBDDAux =  new Date(0);
				dateBBDDAux = dateBBDDAux.valueOf(lstFechas.get(i));
				if(dateBBDDAux.compareTo(dateBBDD) >0 ){
					
					dateBBDD = dateBBDDAux;
				}
			}
			
			if(dateBBDD != null){
				fecha = dateBBDD.toString();
			}
			
			
		} catch (Exception e) {
			logger.error("Exception al obtener la fechas de los fips del ambito:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}

		return fecha;
	}
	
	public static FipBean obtenerUltimoFip(String codAmbito) throws Exception{
		
		ArrayList<FipBean> lstFipBean = obtenerLstFips(codAmbito);
		FipBean fiplast =null;
		if(lstFipBean != null && !lstFipBean.isEmpty()){
			fiplast = lstFipBean.get(0);
			Date date = new Date(0);
			date = date.valueOf(fiplast.getFechaConsolidacion().toString());

			
			for(int i=1; i<lstFipBean.size(); i++ ){
				FipBean fipAux = lstFipBean.get(i);
				Date dateAux = new Date(0);
				dateAux = dateAux.valueOf(fipAux.getFechaConsolidacion().toString());
				
				if(dateAux.compareTo(date) > 0){
					fiplast = fipAux;
				}
			}
		}
		
		if(fiplast != null){
			
			TramiteBean tpv = new TramiteBean();
			obtenerTramitesPV(fiplast, tpv);
			
			TramiteBean tcs = new TramiteBean();
			tcs.setId(obtenerTramitesCS(fiplast, tpv.getId()));
			
			fiplast.setTramiteCatalogoSistematizado(tcs);
			fiplast.setTramitePlaneamientoVigente(tpv);
			
		}
		
		return fiplast;
	}
	
	public static int obtenerTramitesCS(FipBean fip, int idPV) throws Exception{
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		int idTramite = -1 ;
		ArrayList lstTramites = new ArrayList();
		try {
		
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT gestorfip.tramite.id FROM gestorfip.tramite WHERE gestorfip.tramite.fip = ?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, fip.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				lstTramites.add(rs.getInt("id"));
			}
			
			for (int i = 0; i < lstTramites.size(); i++) {
				if(((Integer)lstTramites.get(i)).intValue() != idPV){
					idTramite = ((Integer)lstTramites.get(i)).intValue();
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception al obtener el tramite:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
		return idTramite;
		
	}
	
	public static void obtenerTramitesPV(FipBean fip, TramiteBean tpv) throws Exception{
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		int idTramite = -1 ;
		HashMap<Integer, String> tramite = new HashMap<Integer, String>();
		try {
		
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT gestorfip.tramite.id,gestorfip.tramite.codigo FROM gestorfip.tramite, gestorfip.planeamientovigente " +
					"WHERE gestorfip.tramite.fip=? AND gestorfip.tramite.id = gestorfip.planeamientovigente.tramite";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, fip.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				tpv.setId(rs.getInt("id"));
				tpv.setCodigo(rs.getString("codigo"));
			}
			
		} catch (Exception e) {
			logger.error("Exception al obtener el tramite:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
	}
	
	
	public static String obtenerAmbito(String ambito) throws Exception{
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		String nombreAmbito = null;
		try {
		
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT nombre FROM gestorfip.diccionario_ambitos WHERE codigo=?";
			pst = conn.prepareStatement(sSQL);
		
			pst.setString(1, ambito);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				nombreAmbito = rs.getString("nombre");
			}
			
		} catch (Exception e) {
			logger.error("Exception al obtener el tramite:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
		return nombreAmbito;
	}
	
	
	public static ArrayList obtenerLstFips(String codAmbito) throws Exception{
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		FipBean fipBean = null;
		ArrayList<FipBean> lstFipBean = new ArrayList<FipBean>();
		try {
		
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.fip WHERE idambito=? AND id = (SELECT max(id) FROM gestorfip.fip WHERE idambito=?) ";
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, Integer.valueOf(codAmbito));
			pst.setInt(2, Integer.valueOf(codAmbito));
			rs = pst.executeQuery();
			
			while (rs.next()) {
				fipBean = new FipBean();
				fipBean.setId(rs.getInt("id"));
				fipBean.setPais(rs.getString("pais"));
				fipBean.setVersion(rs.getString("version"));
				fipBean.setSrs(rs.getString("srs"));
				fipBean.setFecha(rs.getString("fecha"));
				fipBean.setFechaConsolidacion(rs.getString("fechaconsolidacion"));
				fipBean.setIdAmbito(rs.getInt("idambito"));
				lstFipBean.add(fipBean);
			}
			
			//String ambito = UtilsWS.completarConCerosIzq(String.valueOf(idAmbito), 6);
			for(int i=0; i<lstFipBean.size(); i++){
				((FipBean)lstFipBean.get(i)).setMunicipio(obtenerAmbito(codAmbito));
			}
			
		} catch (Exception e) {
			logger.error("Exception al obtener los fips del ambito:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		
		return lstFipBean;
	}
	
	
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasFip_GestorPlaneamiento(int idFip, int idTramite){
		
//		logger.info("obtenerDeterminacionesAsociadasFip_GestorPlaneamiento - Inicio");
		
		long timeInicio = System.currentTimeMillis();
		//int idTramite = 24;
		DeterminacionBean[] lstDeterminacionBean_ordenadoEtiqueta = null;
		DeterminacionBean[] lstDeterminacionBean = null;
		String sSQL = "";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			st = conn.createStatement();

			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = "+idTramite;
			
			rs = st.executeQuery(sSQL);
			int totalDeter=0;
			while (rs.next()) {
				totalDeter = rs.getInt("total");
			}

			lstDeterminacionBean = new DeterminacionBean[totalDeter];
			lstDeterminacionBean_ordenadoEtiqueta = new DeterminacionBean[totalDeter];
			HashMap hashDeterBean = new HashMap();
			
			int i=0;

			sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
					"WHERE tramite = "+idTramite + " ORDER BY apartado";
			
			
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
				DeterminacionBean deterBean = new DeterminacionBean();
				deterBean.setId(rs.getInt("id"));
				deterBean.setCodigo(rs.getString("codigo"));
				deterBean.setCaracter(rs.getInt("caracterid"));
				deterBean.setApartado(rs.getString("apartado"));
				deterBean.setNombre(rs.getString("nombre"));
				deterBean.setEtiqueta(rs.getString("etiqueta"));
				deterBean.setSuspendida(rs.getBoolean("suspendida"));
				deterBean.setTexto(rs.getString("texto"));
				deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
				deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
				deterBean.setMadre(rs.getInt("madre"));
				deterBean.setTramite(rs.getInt("tramite"));

				//lstDeterminacionBean[i] = deterBean;
				lstDeterminacionBean_ordenadoEtiqueta[i] = deterBean;
				hashDeterBean.put(deterBean.getId(), deterBean);
				
				i++;
			}
			rs.close();
			st.close();				
			
			hashDeterBean = obtenerDatosValoresReferenciaDeterminacion(conn, hashDeterBean, idTramite);
			
			hashDeterBean = obtenerDatosGrupoAplicacion(conn, hashDeterBean, idTramite);
			
			hashDeterBean =  obtenerDatosRegulacionesEspecificas(conn,hashDeterBean, idTramite);
			
			
			int index_aux =0;
			if(lstDeterminacionBean_ordenadoEtiqueta != null){
				for(int h=0; h<lstDeterminacionBean_ordenadoEtiqueta.length; h++){
					int id = lstDeterminacionBean_ordenadoEtiqueta[h].getId();
					lstDeterminacionBean[index_aux] = (DeterminacionBean)hashDeterBean.get(id);
					index_aux ++;
				}
				
			}

		} catch (Exception e) {
			logger.error("Exception al obtener las Determinaciones asociadas a un tramite:");
			logger.error(e);
			e.printStackTrace();
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerDeterminacionesAsociadasFip_GestorPlaneamiento - Fin");
		long timeFin = System.currentTimeMillis();
		return lstDeterminacionBean;
		
	}


	public static EntidadBean[] obtenerEntidadesAsociadasFip_GestorPlaneamiento(int idFip, int idTramite){
//		logger.info("obtenerEntidadesAsociadasFip_GestorPlaneamiento - Inicio");

		//int idTramite = 24;
		EntidadBean[] lstEntidadBean = null;
		String sSQL = "";
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			 st = conn.createStatement();
	//			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_entidades " +
	//			"WHERE tramite = (SELECT id FROM gestorfip.tramite WHERE fip = "+idFip+" AND tipo = 'PV')";
			sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_entidades " +
					" WHERE tramite = " +idTramite;
							
			
			 rs = st.executeQuery(sSQL);
			int totalDeter=0;
			while (rs.next()) {
				totalDeter = rs.getInt("total");
			}
			
			lstEntidadBean = new EntidadBean[totalDeter];
			
			int i=0;
			
	//		sSQL = "SELECT * FROM gestorfip.tramite_entidades " +
	//				"WHERE tramite = (SELECT id FROM gestorfip.tramite WHERE fip = "+idFip+" AND tipo = 'PV') 
	//					" ORDER BY nombre ASC";
			
			sSQL = "SELECT * FROM gestorfip.tramite_entidades " +
					" WHERE tramite = " +idTramite +
						" ORDER BY nombre ASC";
			rs = st.executeQuery(sSQL);
			
			while (rs.next()) {
			
				EntidadBean entBean = new EntidadBean();
				entBean.setId(rs.getInt("id"));
				entBean.setCodigo(rs.getString("codigo"));
				entBean.setClave(rs.getString("clave"));
				entBean.setNombre(rs.getString("nombre"));
				entBean.setEtiqueta(rs.getString("etiqueta"));
				entBean.setSuspendida(rs.getBoolean("suspendida"));
				entBean.setBase_entidadid(rs.getInt("base_entidadid"));
				entBean.setMadre(rs.getInt("madre"));
				entBean.setTramite(rs.getInt("tramite"));
				entBean.setIdFeature(rs.getInt("idfeature"));
				entBean.setIdLayer(rs.getInt("idlayer"));
	
				lstEntidadBean[i] = entBean;
				
				i++;
			}
			rs.close();
			st.close();
			
			for(int j=0; j< lstEntidadBean.length; j++){

				EntidadBean entBean = (EntidadBean)lstEntidadBean[j];
				String codigoDetCU = obtenerDetAsocToEntidadGrupoEntidades(entBean.getId());
				entBean.setCodigoValRefCU(codigoDetCU);
				
			}
			
		} catch (Exception e) {
			logger.error("Exception al obtener las Entidades asociadas a un tramite:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
		}
		finally{
			ConnectionUtilities.closeConnection(conn,st,rs);
		}
//		logger.info("obtenerEntidadesAsociadasFip_GestorPlaneamiento - Fin");
		return lstEntidadBean;
	}
	
	

	public static String obtenerDetAsocToEntidadGrupoEntidades(int idEntidad){
//		logger.info("obtenerDetAsocToEntidadGrupoEntidades - Inicio");
		String codigoDet = "";
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT id FROM gestorfip.tramite_determinaciones " +
				   "WHERE codigo=? AND id IN ( " +
				   		"SELECT codigodeterminacion_determinacionid FROM gestorfip.tramite_condicionesurbanisticas "+
                               "WHERE codigoentidad_entidadid = ?)";
			
			pst = conn.prepareStatement(sSQL);
			pst.setString(1, Constants.GRUPO_ENTIDADES);
			pst.setInt(2, idEntidad);
			
			rs = pst.executeQuery();
			int idDetGrupoEntidad = -1;
			ArrayList lstIdsDeterminacionesAsociadas = new ArrayList();
			while (rs.next()) {
				
				idDetGrupoEntidad = new Integer(rs.getInt("id")).intValue();
			}

			//se obtiene la condicion urbanistica para obtener el valor de referencia
			if(idDetGrupoEntidad != -1){

				sSQL = " SELECT codigo FROM gestorfip.tramite_determinaciones WHERE id IN "+
							"(SELECT valorreferencia_determinacionid FROM gestorfip.tramite_condicionurbanistica_caso_regimenes WHERE caso IN " +
								"(SELECT id FROM gestorfip.tramite_condicionurbanistica_casos WHERE condicionurbanistica IN "+
									"(SELECT id FROM gestorfip.tramite_condicionesurbanisticas WHERE codigodeterminacion_determinacionid = ? AND codigoentidad_entidadid = ?)))";
				
				pst = conn.prepareStatement(sSQL);
				
				pst.setInt(1, idDetGrupoEntidad);
				pst.setInt(2, idEntidad);
				rs = pst.executeQuery();

				while (rs.next()) {
					codigoDet = rs.getString("codigo");
				}

			}
				
			
		} catch (Exception e) {
			logger.error("Exception al obtener el grupo de aplicacion asociado a una entidad.:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDetAsocToEntidadGrupoEntidades - Fin");

		return codigoDet;
	}
	
	
	
	public static DeterminacionBean[] obtenerDeterminacionesAsociadasToEntidad(int idEntidad){
//		logger.info("obtenerDeterminacionesAsociadasToEntidad - Inicio");
		DeterminacionBean[] lstDeterminacionesBean = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			// Carga en BD de los datos
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT * FROM gestorfip.tramite_condicionesurbanisticas " +
					" WHERE codigoentidad_entidadid = ?";
			
			pst = conn.prepareStatement(sSQL);
		
			pst.setInt(1, idEntidad);
			
			rs = pst.executeQuery();
			
			ArrayList lstIdsDeterminacionesAsociadas = new ArrayList();
			while (rs.next()) {
				
				lstIdsDeterminacionesAsociadas.add(new Integer(rs.getInt("codigodeterminacion_determinacionid")));
			}
			
			lstDeterminacionesBean = new DeterminacionBean[lstIdsDeterminacionesAsociadas.size()];
			
			for(int i=0; i<lstIdsDeterminacionesAsociadas.size(); i++){
				
				int idDeterminacion = ((Integer)lstIdsDeterminacionesAsociadas.get(i)).intValue();
				
				sSQL = "SELECT * FROM gestorfip.tramite_determinaciones " +
				" WHERE id = ?";
				
				pst = conn.prepareStatement(sSQL);
				
				pst.setInt(1, idDeterminacion);
				
				rs = pst.executeQuery();
				
				while (rs.next()) {
					
					DeterminacionBean deterBean = new DeterminacionBean();
					deterBean.setId(rs.getInt("id"));
					deterBean.setCodigo(rs.getString("codigo"));
					deterBean.setCaracter(rs.getInt("caracterid"));
					deterBean.setApartado(rs.getString("apartado"));
					deterBean.setNombre(rs.getString("nombre"));
					deterBean.setEtiqueta(rs.getString("etiqueta"));
					deterBean.setSuspendida(rs.getBoolean("suspendida"));
					deterBean.setTexto(rs.getString("texto"));
					deterBean.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
					deterBean.setBase_determinacionid(rs.getInt("base_determinacionid"));
					deterBean.setMadre(rs.getInt("madre"));
					deterBean.setTramite(rs.getInt("tramite"));
	
					
					lstDeterminacionesBean[i] = deterBean;
				}
			}
			
			
		} catch (Exception e) {
			logger.error("Exception al obtener las entidades asociadas a una determinacion:");
			logger.error(e);
			e.printStackTrace();
			System.out.println(e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDeterminacionesAsociadasToEntidad - Fin");
		return lstDeterminacionesBean;
	}
	
	public static CondicionUrbanisticaCasoBean[] 
            obtenerCasosCondicionUrbanistica(int idDeterminacion, int idEntidad){
//		logger.info("obtenerCasosCondicionUrbanistica - Inicio");
		CondicionUrbanisticaCasoBean[] lstCUC = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs= null;
		Connection conn = null;
		try {
			
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			sSQL = "SELECT id FROM gestorfip.tramite_condicionesurbanisticas " +
			"WHERE codigodeterminacion_determinacionid = ? AND" +
			" codigoentidad_entidadid = ?";
			
			pst = conn.prepareStatement(sSQL);
			pst.setInt(1, idDeterminacion);
			pst.setInt(2,idEntidad);
			
			 rs = pst.executeQuery();
			
			int idCondicionUrbanistica = -1;
			while (rs.next()) {
				idCondicionUrbanistica = rs.getInt("id");
			}
			
			
			if(idCondicionUrbanistica != -1){
				sSQL = "SELECT COUNT(*) AS total FROM gestorfip.tramite_condicionurbanistica_casos " +
				"WHERE condicionurbanistica = ?";
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, idCondicionUrbanistica);
				
				rs = pst.executeQuery();
				
				int totalCasos = -1;
				while (rs.next()) {
					totalCasos = rs.getInt("total");
				}
				
				lstCUC = new CondicionUrbanisticaCasoBean[totalCasos];
				
				sSQL = "SELECT * FROM gestorfip.tramite_condicionurbanistica_casos " +
				"WHERE condicionurbanistica = ?";
				
				pst = conn.prepareStatement(sSQL);
				pst.setInt(1, idCondicionUrbanistica);
				
				
				rs = pst.executeQuery();
				
				int indexCUC = 0;
				while (rs.next()) {
					CondicionUrbanisticaCasoBean cuc = new CondicionUrbanisticaCasoBean();
					
					cuc.setId( rs.getInt("id"));
					cuc.setCondicionurbanistica( rs.getInt("condicionurbanistica"));
					cuc.setCodigo( rs.getString("codigo"));
					cuc.setNombre( rs.getString("nombre"));
					lstCUC[indexCUC] = cuc;
					indexCUC ++;
				}
			}
			
		}
			
		catch (Exception e) {
			logger.error("Exception al obtener los casos de la condicion urbanistica:");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerCasosCondicionUrbanistica - Fin");
		return lstCUC;
	}
	
	public static CondicionUrbanisticaCasoRegimenRegimenesBean 
			obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica(int idRegimen){
//		logger.info("obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica - Inicio");
		CondicionUrbanisticaCasoRegimenRegimenesBean cucrr = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		Connection conn = null;
		try {
		
		
		conn = getDataSource().getConnection();;
		if(conn == null || conn.isClosed()) throw new Exception ();
		
		sSQL = "SELECT * FROM gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos " +
		" WHERE regimen = ?";
		
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idRegimen);
		rs= pst.executeQuery();
		
		while (rs.next()) {
		
			cucrr = new CondicionUrbanisticaCasoRegimenRegimenesBean();
			
			cucrr.setId(rs.getInt("id"));
			cucrr.setOrden(rs.getInt("orden"));
			cucrr.setNombre(rs.getString("nombre"));
			cucrr.setTexto(rs.getString("texto"));
			cucrr.setPadre(rs.getInt("padre"));
			cucrr.setRegimen(rs.getInt("regimen"));
		
		}
		
		}
		catch (Exception e) {
		logger.error("Exception al obtener el regimen especifico del regimen asociados al caso de la condicion urbanistica:");
		logger.error(e);
		System.out.println(e);
		e.printStackTrace();
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		}
		finally{
		ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerRegimenEspecificoRegimenCasoCondicionUrbanistica - Fin");
		return cucrr;
	}
	
	public static DeterminacionBean obtenerDatosDeterminacion(int idDet){
//		logger.info("obtenerDatosDeterminacion - Inicio");
		DeterminacionBean det = null;
		
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		Connection conn = null;
		try {
		
		
		conn = getDataSource().getConnection();;
		if(conn == null || conn.isClosed()) throw new Exception ();
		
		sSQL = "SELECT * FROM gestorfip.tramite_determinaciones WHERE id = ?";
		
		pst = conn.prepareStatement(sSQL);
		pst.setInt(1, idDet);
		rs= pst.executeQuery();
		
		while (rs.next()) {
		
			det = new DeterminacionBean();
			det.setId(rs.getInt("id"));
			det.setCodigo(rs.getString("codigo"));
			det.setCaracter(rs.getInt("caracterid"));
			det.setApartado(rs.getString("apartado"));
			det.setNombre(rs.getString("nombre"));
			det.setEtiqueta(rs.getString("etiqueta"));
			det.setSuspendida(rs.getBoolean("suspendida"));
			det.setTexto(rs.getString("texto"));
			det.setUnidad_determinacionid(rs.getInt("unidad_determinacionid"));
			det.setBase_determinacionid(rs.getInt("base_determinacionid"));
			det.setMadre(rs.getInt("madre"));
			det.setTramite(rs.getInt("tramite"));
		
		}
		
		}
		catch (Exception e) {
		logger.error("Exception al obtener la determinacion:");
		logger.error(e);
		System.out.println(e);
		e.printStackTrace();
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		}
		finally{
		ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerDatosDeterminacion - Fin");
		return det;
	}
	
	
	public static CaracteresDeterminacionBean[] obtenerCaracteresDeterminaciones(){
//		logger.info("obtenerCararteresDeterminaciones - Inicio");
		CaracteresDeterminacionBean[] lstCaracteresDeterminacionBean = null;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();;
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			pst = conn
			.prepareStatement("SELECT COUNT(*) FROM gestorfip.diccionario_caracteresdeterminacion ");

			rs = pst.executeQuery();
			
			int total = 0;
			while (rs.next()) {
				total = rs.getInt(1);
			}
			lstCaracteresDeterminacionBean = new CaracteresDeterminacionBean[total];
			
			pst = conn.prepareStatement("SELECT * FROM gestorfip.diccionario_caracteresdeterminacion " +
					"ORDER BY nombre");
			rs = pst.executeQuery();
			int index = 0;
			while (rs.next()) {
				lstCaracteresDeterminacionBean[index] = new CaracteresDeterminacionBean();
				lstCaracteresDeterminacionBean[index].setId(rs.getInt("id"));
				lstCaracteresDeterminacionBean[index].setCodigo(rs.getString("codigo"));
				lstCaracteresDeterminacionBean[index].setNombre(rs.getString("nombre"));
				lstCaracteresDeterminacionBean[index].setAplicaciones_max(rs.getInt("aplicaciones_max"));
				lstCaracteresDeterminacionBean[index].setAplicaciones_min(rs.getInt("aplicaciones_min"));
				index ++;
			}

			
			//ConnectionUtilities.closeConnection(conn,pst,rs);
			
		} catch (Exception e) {
			logger.error("Exception al obtener un valor de la secuencia");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerCararteresDeterminaciones - Fin");
		return lstCaracteresDeterminacionBean;
	}
	
	
	public static String  obtenerAmbitoTrabajo(int idMunicipio) throws Exception{
//		logger.info("obtenerAmbitoTrabajo - Inicio");
		String codAmbito = null ;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			pst = conn
					.prepareStatement("SELECT codambito FROM gestorfip.relacionambitomunicipio WHERE idmunicipio = ?");

			pst.setInt(1, idMunicipio);
			rs= pst.executeQuery();
			
			
			while (rs.next()) {
				codAmbito = rs.getString("codambito");
			}
		} catch (Exception e) {
			logger.error("Exception al obtener el codigo de ambito del municipio");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
	
//		logger.info("obtenerAmbitoTrabajo - Fin");
		return codAmbito;
	}

	
	
	public static CRSGestor[]  obtenerCRS() throws Exception{
//		logger.info("obtenerVersionesConsolaUER - Inicio");
		CRSGestor[] lstCrs = null;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			pst = conn.prepareStatement("SELECT * FROM gestorfip.crs_system");
			rs= pst.executeQuery();

			while (rs.next()) {
				CRSGestor crs = new CRSGestor() ;
				crs.setId(rs.getInt("id"));
				crs.setCrs(rs.getInt("crs"));
				
				if(lstCrs == null){
					lstCrs = new CRSGestor[1];
					lstCrs[0] = crs;
				}
				else{
					lstCrs = (CRSGestor[]) Arrays.copyOf(lstCrs, lstCrs.length+1);
					lstCrs[lstCrs.length-1] = crs;
				}	
			}
		} catch (Exception e) {
			logger.error("Exception al obtener los crs de consola");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerVersionesConsolaUER - Fin");
		return lstCrs;
	}
	
	
	
	public static VersionesUER[]  obtenerVersionesConsolaUER() throws Exception{
//		logger.info("obtenerVersionesConsolaUER - Inicio");
		VersionesUER[] lstVersionesUER = null;
		String sSQL = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			pst = conn.prepareStatement("SELECT * FROM gestorfip.versiones_uer");
			rs= pst.executeQuery();

			while (rs.next()) {
				VersionesUER version = new VersionesUER() ;
				version.setId(rs.getInt("id"));
				version.setVersion(rs.getDouble("version"));
				
				if(lstVersionesUER == null){
					lstVersionesUER = new VersionesUER[1];
					lstVersionesUER[0] = version;
				}
				else{
					lstVersionesUER = (VersionesUER[]) Arrays.copyOf(
							lstVersionesUER, lstVersionesUER.length+1);
					lstVersionesUER[lstVersionesUER.length-1] = version;
				}	
			}
		} catch (Exception e) {
			logger.error("Exception al obtener las versiones de consola");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("obtenerVersionesConsolaUER - Fin");
		return lstVersionesUER;
	}
	
	public static ConfiguracionGestor  obtenerConfigVersionConsolaUER() throws Exception{
//		logger.info("obtenerConfigVersionConsolaUER - Inicio");
		ConfiguracionGestor config = null ;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			pst = conn.prepareStatement("SELECT * FROM gestorfip.config");
			rs= pst.executeQuery();

			while (rs.next()) {
				config = new ConfiguracionGestor();
				config.setId(rs.getInt("id"));
				config.setIdVersion(rs.getInt("id_version"));
			}
		} catch (Exception e) {
			logger.error("Exception al obtener el la version configurada");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
	
//		logger.info("obtenerConfigVersionConsolaUER - Fin");
		return config;
	}
	
	public static void guardarConfiguracionGestorFip(ConfiguracionGestor configGestor) throws Exception{
		
//		logger.info("guardarConfiguracionGestorFip - Inicio");
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		boolean update = false;
	
		try {
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();
			
			
			boolean existConfig = false;
			pst = conn.prepareStatement("SELECT * FROM gestorfip.config WHERE id = ?");
			pst.setInt(1, configGestor.getId());
			rs= pst.executeQuery();
			while (rs.next()) {
				existConfig = true;
			}
			
			if(existConfig){
				pst = conn.prepareStatement("UPDATE gestorfip.config SET id_version = ?, id_crs = ? WHERE id = ?  ");
				pst.setInt(1, configGestor.getIdVersion());
				pst.setInt(2, configGestor.getIdCrs());
				pst.setInt(3, configGestor.getId());
				
			}
			else{
				pst = conn.prepareStatement("INSERT INTO  gestorfip.config (id, id_version, id_crs) VALUES( ?, ?, ?)");
				pst.setInt(1, 0);
				pst.setInt(2, configGestor.getIdVersion());
				pst.setInt(3, configGestor.getIdCrs());
			}
			pst.executeUpdate();
			
		} catch (Exception e) {
			logger.error("Exception al guardar la configuracion del gestor fip");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
//		logger.info("guardarConfiguracionGestorFip - Fin");

	}
	
	
	public static UnidadBean obtenerUnidadDeterminacion(int idDet) throws Exception{
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		UnidadBean unidad = null;
	
		try {
			
			// Conexion a la BD
			conn = getDataSource().getConnection();
			if(conn == null || conn.isClosed()) throw new Exception ();

			pst = conn.prepareStatement("SELECT * FROM gestorfip.tramite_unidades WHERE gestorfip.tramite_unidades.determinacionid = (select unidad_determinacionid from gestorfip.tramite_determinaciones where id = ?)");
			pst.setInt(1, idDet);
			rs= pst.executeQuery();
			while (rs.next()) {
				unidad = new UnidadBean();
				unidad.setAbreviatura(rs.getString("abreviatura"));
				unidad.setDeterminacion(rs.getString("definicion"));
			}
	
			
		} catch (Exception e) {
			logger.error("Exception al obtener los datos de la unidad");
			logger.error(e);
			System.out.println(e);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			ConnectionUtilities.closeConnection(conn,pst,rs);
		}
		return unidad;
	}
	
}
