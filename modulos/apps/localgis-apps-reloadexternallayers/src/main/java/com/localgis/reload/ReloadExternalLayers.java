/**
 * ReloadExternalLayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.reload;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.SimpleAppContext;
import com.geopista.server.database.CPoolDatabase;
import com.geopista.ui.plugin.external.CapasExtendidas;
import com.geopista.ui.plugin.external.CapasExtendidasDAO;
import com.geopista.ui.plugin.external.ConnectionUtility;

public class ReloadExternalLayers {

	private static Logger log = Logger.getLogger(ReloadExternalLayers.class);

	private static SimpleAppContext acontext = new SimpleAppContext();

	private static Connection conn = null;
	private static int typeOfConnection = 0;
	
	private static int INDEXER_WORKERS=1;
	
	public ReloadExternalLayers(){
		
	}
	

	private static int threadsRunning(List<Thread> tg) {
		int running = 0;
		for (Thread thread : tg) {
			if (thread.isAlive()) {
				running++;
			} else {

			}
		}
		return running;
	}

	public void reload() {

		long tiempoInicioTotal = 0;

		try {

			AppContext.setHeartBeat(false);
			
			//Cargamos los driver externos que vamos a necesitar.
			Hashtable drivers = ConnectionUtility.findAllDrivers();
			ConnectionUtility.loadDrivers(drivers);
			
			
			try {
				conn = CPoolDatabase.getSimpleConnection();
				log.info("Using jetty datasource");
			} catch (Exception e) {
			}
			if (conn == null) {
				conn = SimpleAppContext.getJDBCConnection();
				typeOfConnection = 1;
				log.info("Using external datasource");
			}
			

			tiempoInicioTotal = System.currentTimeMillis();
			
			CapasExtendidasDAO capasExtendidasDAO = new CapasExtendidasDAO();
            Vector vectorCapasExtendidas = null;

            vectorCapasExtendidas = capasExtendidasDAO.listCapasExtendidas(conn);
            
            log.info("Numero de capas externas:"+vectorCapasExtendidas.size());

			List<Thread> tg = new ArrayList<Thread>();

			int running = 0;

			for (int i=0;i<vectorCapasExtendidas.size();i++){

				
				CapasExtendidas capaExtendidaSimple=(CapasExtendidas)vectorCapasExtendidas.elementAt(i);
			

					try {
						ReloadExternalLayerTask p1 = new ReloadExternalLayerTask(capaExtendidaSimple);


						while (threadsRunning(tg) > INDEXER_WORKERS) {
							//System.out.print(".");
							try {
								Thread.sleep(200);
							} catch (Exception ex) {
							}
						}
						Thread t1 = new Thread(p1);
						t1.start();
						tg.add(t1);
			

					} catch (Exception ex) {
						log.error(ex.getMessage());
						ex.printStackTrace();
					}
			}
			
			for (Thread thread : tg) {
				thread.join();
			}

		} catch (Exception sqle) {
			log.error("Error: " + sqle.getMessage());

		}  finally {
			safeClose(null, null, conn);
			if (typeOfConnection == 0)
				CPoolDatabase.releaseConexion();

			long totalTiempoTotal = System.currentTimeMillis()	- tiempoInicioTotal;
			log.info("Tiempo Proceso Recarga de capa "	+ totalTiempoTotal+ " milisegundos");

		}

	}
	
	public static boolean safeClose(ResultSet rs, Statement statement,Connection connection) {
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
			connection.close();

		} catch (Exception ex2) {
		}

		return true;
	}


	public static void main(String args[]) {
		log.info("########################## INICIO RECARGA CAPAS EXTERNAS #####################");
		try {
		
			new ReloadExternalLayers().reload();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		log.info("RECARGA DE CAPAS EXTERNAS FINALIZADA");
	}
}
