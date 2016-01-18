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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.datosexternos;


/**
 * Bean con los datos de una layerfamily, de acuerdo a los datos contenidos en la
 * tabla <code>layerfamilies</code> de la base de datos geopista
 * 
 * @author cotesa
 *
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.feature.Table;
import com.geopista.ui.plugin.external.ExternalDataSource;


public class DataSourceTables{
      
    
	Table table;
	ExternalDataSource externalDS;
	int id_datasource_tables;
	String fetchQuery;
	
    /**
     * Conexión a base de datos
     */
    public Connection conn = null;
    
    /**
     * Contexto de la aplicación
     */
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
	public DataSourceTables() {
		super();
        try
        {
        	id_datasource_tables = -1;
            conn = getDBConnection();
        }
        catch(Exception e)
        { 
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
	
	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	public ExternalDataSource getExternalDS() {
		return externalDS;
	}
	
	public void setExternalDS(ExternalDataSource externalDS) {
		this.externalDS = externalDS;
	}
	
	public String getFetchQuery() {
		return fetchQuery;
	}
	
	public void setFetchQuery(String fetchQuery) {
		this.fetchQuery = fetchQuery;
	}

	public ArrayList getDataSourceTables(){
		ArrayList datasourceTables = new ArrayList();
	

		PreparedStatement s = null;
		ResultSet r = null;
		    
		try {
			// SELECT id, id_table, id_datasource, fetchquery FROM datasource_tables;
			s = conn.prepareStatement("LMobtenerDataSourceTables");
			s.setInt(1, AppContext.getIdMunicipio());
			r = s.executeQuery();
			while (r.next()){
				
				DataSourceTables dsTables = new DataSourceTables();
				Table tabla = new Table();
                QueryUtility query = new QueryUtility();
				ExternalDataSource externalDS = new ExternalDataSource();
				
				int id = r.getInt("id");
                int idTable = r.getInt("id_table");
                int idDataSource = r.getInt("id_datasource");
                dsTables.setFetchQuery(r.getString("fetchquery"));
                tabla = query.obtenerTabla(idTable);
                externalDS = query.getDataSource(idDataSource);
                dsTables.setTable(tabla);
                dsTables.setExternalDS(externalDS);
                dsTables.setId_datasource_tables(id);
                datasourceTables.add(dsTables);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return datasourceTables;
	}

	public void getDataSourceTables(String nombreTabla){

		PreparedStatement s = null;
		ResultSet r = null;
		    
		try {
			// SELECT id, id_table, id_datasource, fetchquery FROM datasource_tables;
			s = conn.prepareStatement("LMobtenerDataSourceTablesNombre");
			s.setInt(1, AppContext.getIdMunicipio());
			s.setString(2, nombreTabla);
			r = s.executeQuery();
			if (r.next()){
				
                QueryUtility query = new QueryUtility();
				
				int id = r.getInt("id");
                int idTable = r.getInt("id_table");
                int idDataSource = r.getInt("id_datasource");
                fetchQuery = r.getString("fetchquery");
                table = query.obtenerTabla(idTable);
                externalDS = query.getDataSource(idDataSource);
                id_datasource_tables = id;
                 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertDataSourceTables(){    
		PreparedStatement s = null;

        try {
			s = conn.prepareStatement("LMinsertarDataSourceTables");
	        
	        s.setInt(1, table.getIdTabla());
	        s.setInt(2, externalDS.getId());
	        s.setString(3, fetchQuery);
	        
	        s.executeUpdate();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
	public boolean eliminarDataSourceTablesCompleto(int id){
		eliminarDataSourceTables(id);
		TablesDBOperations operaciones = new TablesDBOperations();
		try {
			operaciones.eliminarTablaBD(table);
			return true;
		} catch (DataException e) {
			return false;
		}
	}
	
	public void eliminarDataSourceTables(int id){    
		PreparedStatement s = null;

        try {
			s = conn.prepareStatement("LMeliminarDataSourceTables");
	        s.setInt(1, id);
	        s.executeUpdate();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	
	public int getId_datasource_tables() {
		return id_datasource_tables;
	}

	public void setId_datasource_tables(int id_datasource_tables) {
		this.id_datasource_tables = id_datasource_tables;
	}

}
