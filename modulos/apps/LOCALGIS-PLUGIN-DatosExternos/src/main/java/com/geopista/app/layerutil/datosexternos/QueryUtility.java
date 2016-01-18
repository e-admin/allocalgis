/**
 * QueryUtility.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.datosexternos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.schema.column.ColumnRow;
import com.geopista.feature.Table;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.ui.plugin.external.ConnectionUtility;
import com.geopista.ui.plugin.external.ExternalDataSource;

public class QueryUtility {

    /**
     * Conexión a base de datos
     */
    public Connection conn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
	public static final String FICHERO_CONFIG = "/com/geopista/app/layerutil/datosexternos/datosExternos.properties";
	
	//private static final String OPCION_SINTAMANIO = "sinTamanio";
	//private static final String OPCION_SINPRECISION= "sinPrecision";
	//private static final String OPCION_CONPRECISION= "conPrecision";
	
	private static final String CATEGORY= ".category";
	private static final String NUMBER= "number";
	private static final String TEXT= "text";
	private static final String NONE= "none";
	
	public static final String GEOPISTA_PACKAGE = "/com/geopista/app";
	
	
	public QueryUtility() {
		super();    
	    try{
	        conn = getDBConnection();
	    }
	    catch(Exception e){ 
	        e.printStackTrace();
	    }   
	}

    /**
     * Obtiene una conexión a la base de datos
     * @return Conexión a la base de datos
     * @throws SQLException
     */
    private static Connection getDBConnection () throws SQLException
    {        
        Connection con=  aplicacion.getConnection();
        con.setAutoCommit(false);
        return con;
    } 
    
	public Table obtenerTabla(String nombreTabla){
    	Boolean exito = Boolean.FALSE;
    	TablesDBOperations operaciones = new TablesDBOperations();

    	Table tabla = null;
    	try {
    		
			List tablas = operaciones.obtenerListaTablasBD();
			Iterator it = tablas.iterator();
			
			while (it.hasNext() && !exito){
				tabla = (Table) it.next();
				if (tabla.getDescription().equals(nombreTabla)){					
					exito = Boolean.TRUE;
					tabla.setColumns(operaciones.obtenerListaColumnasBD(nombreTabla));
				}	        
			}			
  		} catch (DataException e) {
			e.printStackTrace();
		} 

		return tabla;  	
 
	}
	
	public Table obtenerTabla(int id_tabla){
    	Boolean exito = Boolean.FALSE;
    	TablesDBOperations operaciones = new TablesDBOperations();
    	
    	Table tabla = null;
    	try {
    		
			List tablas = operaciones.obtenerListaTablasBD();
			Iterator it = tablas.iterator();
			
			while (it.hasNext() && !exito){
				tabla = (Table) it.next();
				if (tabla.getIdTabla()==id_tabla){					
					exito = Boolean.TRUE;
					tabla.setColumns(operaciones.obtenerListaColumnasBD(tabla.getDescription()));
				}	        
			}			
  		} catch (DataException e) {
			e.printStackTrace();
		} 

		return tabla;  	
 
	}
	
	public void creaTabla(String nombreTabla){
		TablesDBOperations operaciones = new TablesDBOperations();
		
		Table tabla = new Table();
		tabla.setDescription(nombreTabla);
		tabla.setGeometryType(0);
		tabla.setExternal(1);
		try {
			operaciones.crearTablaBD(tabla);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean creaColumnas(String select, String nombreTabla, ExternalDataSource externalDataSource){
	
		boolean resultado = false;
		
		Properties ficheroTraduccionBD = leeFicheroTraduccionBD();
		TablesDBOperations operaciones = new TablesDBOperations();
		ColumnRow columnaRow = new ColumnRow();
		Statement stmt;
		ResultSet rs = null;
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(select);
			ResultSetMetaData rsmd = rs.getMetaData();
	
			if (rsmd!=null){
							
				String tipoColumna = null;
				int numColumnas = rsmd.getColumnCount();

	    		/*
	    		 *  Determinamos a traves de la información de preferencia 
	    		 *  el tipo de base de datos de geopista para el fichero
	    		 *  de configuración:
	    		 *  
	    		 */
				
				Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
				String url = pref.get(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL, "");
				String bbdd = "pgsql";
	    		if (url.indexOf("jdbc:oracle")>=0) 
	    				bbdd = "oracle";
	    		
				for (int i = 1; i <= numColumnas; i++){
					columnaRow = new ColumnRow();
					columnaRow.getColumnaBD().setName(rsmd.getColumnName(i));
					tipoColumna = rsmd.getColumnTypeName(i).toUpperCase();
			
					String tipoCol = ficheroTraduccionBD.getProperty(tipoColumna+"."+bbdd);
					String category = ficheroTraduccionBD.getProperty(tipoColumna+"."+bbdd+CATEGORY);				

					tipoCol = tipoCol.split(" ")[0];
					columnaRow.getColumnaBD().setType(tipoCol);
					
					if(category.equalsIgnoreCase(NUMBER)){
						//Si precision es 0 no fijamos tamaño de columna
						if (rsmd.getPrecision(i) > 0) {
							columnaRow.getColumnaBD().setLength(rsmd.getPrecision(i));
							if (rsmd.getScale(i) > 0)
								columnaRow.getColumnaBD().setPrecission(rsmd.getScale(i));
						}
						//else{
						//	columnaRow.getColumnaBD().setLength(0);
						//}
					}
					else if(category.equalsIgnoreCase(TEXT)){
						columnaRow.getColumnaBD().setLength(rsmd.getColumnDisplaySize(i));
					}
					//else{
					//	columnaRow.getColumnaBD().setLength(0);	
					//}		
					
					columnaRow.getColumnaBD().setDefaultValue("");
					columnaRow.getColumnaBD().setDescription("");
					
					columnaRow.getColumnaSistema().setDescription(columnaRow.getColumnaBD().getName());
					
					resultado = operaciones.crearColumnaBD(obtenerTabla(nombreTabla), columnaRow);
		        }
			}
			return resultado;
			//	System.out.println("sql: "+create.toString());
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return resultado;
		} catch (DataException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return resultado;
		}finally{
			try {
				rs.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public ExternalDataSource getDataSource(int id_datasource){
		ExternalDataSource externalDS = new ExternalDataSource();
		
		PreparedStatement s = null;
		ResultSet r = null;
		    
		try {
			// SELECT nombre, driver, cadena_conexion, username, password FROM datasource where id_datasource=?;
			s = conn.prepareStatement("LMobtenerDataSource");
			s.setInt(1, id_datasource);
			r = s.executeQuery();
			if (r.next()){		
                externalDS.setConnectString(r.getString("cadena_conexion"));
                externalDS.setDriver(r.getString("driver"));
                externalDS.setId(id_datasource);
                externalDS.setName(r.getString("nombre"));
                try {
                	EncriptarPassword encrypt = new EncriptarPassword(r.getString("password"));
					externalDS.setPassword(encrypt.desencriptar());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                externalDS.setUserName(r.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return externalDS;
	}
	
	public static String devuelveFilaFichero(String rutaFichero, String clave, String bbdd){
		
		String fila = "";
		File archivo = null;
		FileReader fr = null;
		
		try {
        	archivo = new File(rutaFichero);
        	String linea;
        	fr = new FileReader (archivo);
        	BufferedReader br = new BufferedReader(fr);
        	linea=br.readLine();
        	while(linea!=null){
        		if (!linea.equals("")){
        			String primeraLetra = linea.substring(0, 1);
        			if (!primeraLetra.equals("#")&&(linea.indexOf(bbdd)>0))
        				if (linea.split("=")[1].toUpperCase().equals(clave.toUpperCase())){
        					fila = linea.split("=")[0];
        					return fila;
        				}       					
        		}
            	linea=br.readLine();
        	}
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			try {                
				if( fr != null){  
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}     
		}
		return fila;
	}

	public Properties leeFicheroTraduccionBD() {
		try {
			Properties prop = new Properties();
			prop.load(this.getClass().getResourceAsStream(FICHERO_CONFIG));
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	
}
