/**
 * CapasExtendidasDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import net.sourceforge.jtds.jdbc.DateTime;

import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.server.database.COperacionesQueryCache;



public class CapasExtendidasDAO {

	private final static String listCapasExtendidas = "DBElistCapasExtendidas"; 
	private final static String getCapaExtendida = "DBEgetCapaExtendida";
	private final static String vaciarCapaExtendida = "DBEvaciarCapaExtendida";
	private final static String contieneDatosCapaExtendida = "DBEcontieneDatosCapaExtendida";
	
	private static final String TIPORESULT_INT = "int";
	private static final String TIPORESULT_STRING = "string";
	private static final String TIPORESULT_BOOLEAN = "boolean";
	
	private static Logger log = Logger.getLogger(CapasExtendidasDAO.class);
	
	private Locale locale;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	// jdbc:pista:http://%HOST_IP%:8081/geopista/CServletDB
	// com.geopista.sql.GEOPISTADriver
	private Connection getConnection() {
		try {
			Connection connection = aplicacion.getConnection(); 
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CapasExtendidasDAO() {
		locale = Locale.getDefault();
	}
	
	public Vector listCapasExtendidas() {
		Connection connection = getConnection();
		return listCapasExtendidas(connection,listCapasExtendidas,true);
	}
	public Vector listCapasExtendidas(Connection connection) {
			return listCapasExtendidas(connection,COperacionesQueryCache.getQueryCache(connection,listCapasExtendidas),false);
	}
	
	public Vector listCapasExtendidas(Connection connection,String sql,boolean closeConnection) {
		//Connection connection = getConnection();
		PreparedStatement preparedStatement = null;
		try {
			/*
			  SELECT distinct(l.id_layer), d.traduccion 
			  FROM layers l, attributes a, columns c, tables t, dictionary d
			  WHERE l.id_layer = a.id_layer AND a.id_column = c.id AND c.id_table = t.id_table
			  AND d.id_vocablo = l.id_name AND t.external = 1 AND d.locale = ?				
			*/
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, locale.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector vector = new Vector();
			while (resultSet.next()) {
				int id_layer = resultSet.getInt("id_layer");
				String nombreTraducido = resultSet.getString("traduccion");

				CapasExtendidas capaExtendida = new CapasExtendidas();
				capaExtendida.setId_layer(id_layer);
				capaExtendida.setNombreTraducido(nombreTraducido);
				vector.add(capaExtendida);
			}
			return vector;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (closeConnection){
				if (connection!=null) {
					closeConnection(connection);
				}
			}
		}		
		
	}
	
	public CapasExtendidas getCapaExtendida(int id_layer) {
		Connection connection = getConnection();
		return getCapaExtendida(connection,getCapaExtendida,true,id_layer);
	}
	public CapasExtendidas getCapaExtendida(Connection connection,int id_layer) {
			return getCapaExtendida(connection,COperacionesQueryCache.getQueryCache(connection,getCapaExtendida),false,id_layer);
	}
	
	public CapasExtendidas getCapaExtendida(Connection connection,String sql,boolean closeConnection,int id_layer) {
		//Connection connection = getConnection();
		CapasExtendidas capaExtendida = new CapasExtendidas();
		PreparedStatement preparedStatement = null;
		try {
			/*
			  SELECT distinct(t.id_table), t.name, dt.fetchquery, d.*
			  FROM layers l, attributes a, columns c, tables t, datasource_tables dt, datasource d
			  WHERE l.id_layer = a.id_layer AND a.id_column = c.id AND c.id_table = t.id_table
			  AND t.id_table = dt.id_table AND dt.id_datasource = d.id_datasource AND t.external = 1 
			  AND l.id_layer = ?		
			*/
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id_layer);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ExternalDataSource externalDS = new ExternalDataSource();
				
				externalDS.setId(resultSet.getInt("id_datasource"));
				externalDS.setConnectString(resultSet.getString("cadena_conexion"));
				externalDS.setDriver(resultSet.getString("driver"));
				externalDS.setName(resultSet.getString("nombre"));
				EncriptarPassword encrypt = new EncriptarPassword(resultSet.getString("password"));
				externalDS.setPassword(encrypt.desencriptar());
				externalDS.setUserName(resultSet.getString("username"));
				
				capaExtendida.setExternalDS(externalDS);
				capaExtendida.setId_layer(id_layer);
				capaExtendida.setId_tabla(resultSet.getInt("id_table"));
				capaExtendida.setNombreTabla(resultSet.getString("name"));
				capaExtendida.setFetchQuery(resultSet.getString("fetchquery"));
				return capaExtendida;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (preparedStatement!=null){
					preparedStatement.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (closeConnection){
				if (connection!=null) {
					closeConnection(connection);
				}
			}
		}		
		
	}

	public boolean vaciarCapaExtendida(String nombreTabla) {
		Connection connection = getConnection();
		return vaciarCapaExtendida(connection,nombreTabla,true);
	}
	public boolean vaciarCapaExtendida(Connection connection,String nombreTabla) {
			return vaciarCapaExtendida(connection,nombreTabla,false);
	}
	
	public boolean vaciarCapaExtendida(Connection connection,String nombreTabla,boolean closeConnection) {
		//Connection connection = null;
		PreparedStatement ps = null;
        try {
        	//connection = getConnection();
        	ps = connection.prepareStatement("TRUNCATE \""+nombreTabla+"\"");
        	//ps.setString(1, nombreTabla);
			int resultSet = ps.executeUpdate();
            return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (ps!=null){
					ps.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (closeConnection){
				if (connection!=null) {
					closeConnection(connection);
				}
			}
		}	
	}
	
	public boolean contieneDatosCapaExtendida(String nombreTabla) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement("SELECT * FROM \""+nombreTabla+"\"");
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()){	
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;	
		} finally {
			try {
				if (ps!=null){
					ps.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			if (connection!=null) {
				closeConnection(connection);
			}
		}	
	}
	
	
	public int cargarDatosCapaExtendida(CapasExtendidas capaExtendida) {
		Connection connection = getConnection();
		return cargarDatosCapaExtendida(connection,capaExtendida,true);
	}
	
	public int cargarDatosCapaExtendida(Connection conn,CapasExtendidas capaExtendida) {
		return cargarDatosCapaExtendida(conn,capaExtendida,false);
	}
	
	public int cargarDatosCapaExtendida(Connection conn,CapasExtendidas capaExtendida,boolean closeconnection){
		Connection externalconnection = ConnectionUtility.getConnection(capaExtendida.getExternalDS());
		int numRegistros=0;
		
		if (externalconnection!=null){
			Statement stExternalDS = null;
			int resultado = 2;
			try {
				stExternalDS = externalconnection.createStatement();
				ResultSet resultSet = stExternalDS.executeQuery(capaExtendida.getFetchQuery());				
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int numColumnas = rsmd.getColumnCount();
				if (rsmd!=null){
				
					int contador = 0;
					StringBuffer query = new StringBuffer();
					while (resultSet.next()) {
						numRegistros++;
						StringBuffer queryTMP = new StringBuffer();
						query.append("INSERT INTO \"")
						.append(capaExtendida.getNombreTabla())
						.append("\" ("); 
						for (int i = 1; i <= numColumnas; i++){						
							query.append("\"")
		            		.append(rsmd.getColumnName(i))
		            		.append("\"");
							if (i<numColumnas)
								query.append(", ");

				    		/*
				    		 *  Determinamos a traves de la información de preferencia 
				    		 *  el tipo de base de datos de geopista para el fichero
				    		 *  de configuración:
				    		 *  
				    		 */
							
	
							
						//	String fila = QueryUtility.devuelveFilaFichero(QueryUtility.FICHERO_CONFIG, rsmd.getColumnTypeName(i), bbdd);
							
							String nombreColumna = rsmd.getColumnName(i);

							Object objeto = resultSet.getObject(nombreColumna);
							
							// Añadimos las comillas si el dato sacado es 1 String:
							if ((objeto instanceof String) || (objeto instanceof DateTime) || (objeto instanceof Date)){
								queryTMP.append("'")
								.append(objeto)
								.append("'");	
							}
							else{
								queryTMP.append(objeto);
							}
							if (i<numColumnas)
								queryTMP.append(", ");

						}
						
							
			        	query.append(") VALUES(")
			        	.append(queryTMP.toString())
			        	.append(");");
			        	//System.out.println("QUERY:"+query.toString());
			        	contador++;
						if (contador>=10){
							// Lanzamos la consulta cuando tenemos 20 insert:
							 lanzarConsulta(conn,query.toString(),closeconnection);
							 query = new StringBuffer();
							 contador=0;
						}
					resultado = 1;
					}
					// Lanzamos la consulta si nos ha quedado algún insert sin lanzar anteriormente:
					if ((query!=null)&&(!(query.toString().equals("")))){
						lanzarConsulta(conn,query.toString(),closeconnection);
					}
					log.info("Numero de registos cargados de la tabla:"+capaExtendida.getNombreTabla()+" "+numRegistros);
				}
				return resultado;
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			} finally {
				try {
					if (stExternalDS != null){
						stExternalDS.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (externalconnection!=null) {
					closeConnection(externalconnection);
				}
			}
		}
		else{
			return 0;
		}
	}
	
	
	
	
	public boolean lanzarConsulta(Connection connection,String query,boolean closeconnection) {

		if (connection==null)
			connection = getConnection();
		PreparedStatement ps = null;
		try {		
			ps = connection.prepareStatement(query);
			int iResult = ps.executeUpdate();
	    	  
			if (iResult>0){	
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps!=null){
					ps.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (closeconnection){
				if (connection!=null) {
					closeConnection(connection);
				}
			}
		}
		return false;
	}
}
