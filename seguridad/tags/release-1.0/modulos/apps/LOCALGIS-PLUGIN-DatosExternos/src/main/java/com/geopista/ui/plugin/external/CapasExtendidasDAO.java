package com.geopista.ui.plugin.external;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import net.sourceforge.jtds.jdbc.DateTime;

import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.EncriptarPassword;

public class CapasExtendidasDAO {

	private final static String listCapasExtendidas = "DBElistCapasExtendidas"; 
	private final static String getCapaExtendida = "DBEgetCapaExtendida";
	private final static String vaciarCapaExtendida = "DBEvaciarCapaExtendida";
	private final static String contieneDatosCapaExtendida = "DBEcontieneDatosCapaExtendida";
	
	private static final String TIPORESULT_INT = "int";
	private static final String TIPORESULT_STRING = "string";
	private static final String TIPORESULT_BOOLEAN = "boolean";
	
	private static EncriptarPassword encrypt = new EncriptarPassword();
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
		PreparedStatement preparedStatement = null;
		try {
			/*
			  SELECT distinct(l.id_layer), d.traduccion 
			  FROM layers l, attributes a, columns c, tables t, dictionary d
			  WHERE l.id_layer = a.id_layer AND a.id_column = c.id AND c.id_table = t.id_table
			  AND d.id_vocablo = l.id_name AND t.external = 1 AND d.locale = ?				
			*/
			preparedStatement = connection.prepareStatement(listCapasExtendidas);
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
			if (connection!=null) {
				closeConnection(connection);
			}
		}		
		
	}
	
	public CapasExtendidas getCapaExtendida(int id_layer) {
		Connection connection = getConnection();
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
			preparedStatement = connection.prepareStatement(getCapaExtendida);
			preparedStatement.setInt(1, id_layer);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ExternalDataSource externalDS = new ExternalDataSource();
				
				externalDS.setId(resultSet.getInt("id_datasource"));
				externalDS.setConnectString(resultSet.getString("cadena_conexion"));
				externalDS.setDriver(resultSet.getString("driver"));
				externalDS.setName(resultSet.getString("nombre"));
				externalDS.setPassword(encrypt.undoEncrip(resultSet.getString("password")));
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
			if (connection!=null) {
				closeConnection(connection);
			}
		}		
		
	}

	public boolean vaciarCapaExtendida(String nombreTabla) {
		Connection connection = null;
		PreparedStatement ps = null;
        try {
        	connection = getConnection();
        	ps = connection.prepareStatement("TRUNCATE \""+nombreTabla+"\"");
        	ps.setString(1, nombreTabla);
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
			if (connection!=null) {
				closeConnection(connection);
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
	
	public int cargarDatosCapaExtendida(CapasExtendidas capaExtendida){
		Connection connection = ConnectionUtility.getConnection(capaExtendida.getExternalDS());
		
		if (connection!=null){
			Statement stExternalDS = null;
			int resultado = 2;
			try {
				stExternalDS = connection.createStatement();
				ResultSet resultSet = stExternalDS.executeQuery(capaExtendida.getFetchQuery());				
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int numColumnas = rsmd.getColumnCount();
				if (rsmd!=null){
					int contador = 0;
					StringBuffer query = new StringBuffer();
					while (resultSet.next()) {

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
							 lanzarConsulta(query.toString());
							 query = new StringBuffer();
							 contador=0;
						}
					resultado = 1;
					}
					// Lanzamos la consulta si nos ha quedado algún insert sin lanzar anteriormente:
					if ((query!=null)&&(!(query.toString().equals("")))){
						lanzarConsulta(query.toString());
					}
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
				if (connection!=null) {
					closeConnection(connection);
				}
			}
		}
		else{
			return 0;
		}
	}
	
	public boolean lanzarConsulta(String query){
		Connection connection = getConnection();
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
			if (connection!=null) {
				closeConnection(connection);
			}
		}
		return false;
	}
}
