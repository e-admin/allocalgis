/**
 * ReloadExternalLayerTask.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.reload;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import com.geopista.app.SimpleAppContext;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.ui.plugin.external.CapasExtendidas;
import com.geopista.ui.plugin.external.CapasExtendidasDAO;
import com.vividsolutions.jump.I18N;

public class ReloadExternalLayerTask  implements Runnable {

	private static Logger log = Logger.getLogger(ReloadExternalLayerTask.class);
	private CapasExtendidas capaExtendidaSimple;
	
	
	public ReloadExternalLayerTask(CapasExtendidas capaExtendidaSimple){
		super();
		this.capaExtendidaSimple=capaExtendidaSimple;
	}
	
	public void run() {
		Connection conn=null;
		int typeOfConnection = 0;
			
		try {
			
			long tiempoInicioCapa = System.currentTimeMillis();
			try {
				conn = CPoolDatabase.getSimpleConnection();
			} catch (Exception e) {
			}
			if (conn == null) {
				conn = SimpleAppContext.getJDBCConnection();
				typeOfConnection = 1;
			}
			
			
			
			log.info("Iniciando proceso recarga de layer:"+capaExtendidaSimple.getId_layer());
			CapasExtendidasDAO capasExtendidasDAO = new CapasExtendidasDAO();
			CapasExtendidas capaExtendidaCompuesta = capasExtendidasDAO.getCapaExtendida(conn,capaExtendidaSimple.getId_layer());
			String nombreTabla = capaExtendidaCompuesta.getNombreTabla();
			log.info("Nombre de tabla a indexar:"+nombreTabla);
			
			//Primero borramos los datos que tuviera la tabla
			
			if (capasExtendidasDAO.vaciarCapaExtendida(conn,nombreTabla)){
				int resultado = capasExtendidasDAO.cargarDatosCapaExtendida(conn,capaExtendidaCompuesta);			

        		if (resultado == 1){
        			log.info("Capa "+nombreTabla+" actualizada correctamente");
	        	}
	        	else{
	        		if (resultado == 2){
	        			log.info("Capa "+nombreTabla+" actualizada correctamente pero sin datos");
	        		}
	        		else{
	        			log.info("Capa "+nombreTabla+" no actualizada correctamente");
	        		}
	        	}
			}
			
			
			long totalTiempoCapa =	 System.currentTimeMillis() - tiempoInicioCapa;
			 log.info("Tiempo Proceso Capa :"+	nombreTabla+"-> "+totalTiempoCapa+" ms");

		}catch (Exception sqle) {
			log.error("Error: "+sqle.getMessage());
		} finally {
			try {
				conn.commit();
			} catch (SQLException e) {
			}
			safeClose(null, null, conn);
			if (typeOfConnection==0)
				CPoolDatabase.releaseConexion();
		}


	}

	public static boolean safeClose(ResultSet rs, Statement statement,
			Connection connection) {

		try {
			if (rs != null)
				rs.close();
		} catch (Exception ex2) {
		}
		try {
			if (statement != null)
				statement.close();
		} catch (Exception ex2) {
		}
		try {
			if (connection!=null)
				connection.close();

		} catch (Exception ex2) {
		}

		return true;
	}
}
