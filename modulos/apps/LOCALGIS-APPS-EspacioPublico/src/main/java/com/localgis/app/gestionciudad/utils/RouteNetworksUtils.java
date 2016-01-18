/**
 * RouteNetworksUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.routeserver.ElementNotFoundException;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.util.ApplicationContext;
import com.localgis.route.graph.build.UIDgenerator.FixedValueUIDGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @author javieraragon
 *
 */
public class RouteNetworksUtils {
	
	public static boolean leerGrafodeBase(String networkName,
			TaskMonitor monitor, PlugInContext context) throws Exception {

		monitor.allowCancellationRequests();
		monitor.report("Cargando grafo de base de datos");

//		ConnectionManager.instance(context.getWorkbenchContext());
		DynamicGraph graph = null;
		

		if (!AppContext.getApplicationContext().isLogged()){
			AppContext.getApplicationContext().login();			
		}
		try {
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			DBRouteServerReaderWriter db = null;
			GeographicGraphGenerator graphGenerator = null;
			
			if (isStreetNetwork(networkName)){
				graphGenerator = new LocalGISStreetGraphGenerator(null);
				db = new LocalGISStreetRouteReaderWriter(connectionFactory);
				
			} else{

				graphGenerator = new LocalGISGraphGenerator(null);
				db = new LocalGISRouteReaderWriter(connectionFactory);
			}
				
			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);

			db.setNetworkName(networkName);
			
			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
            graph = new DynamicGraph(manager);
            
            if(graphGenerator instanceof LocalGISStreetGraphGenerator){
            	if (db instanceof LocalGISStreetRouteReaderWriter){
            		ArrayList<Integer> idNodesWithTurnImpedances = ((LocalGISStreetRouteReaderWriter)db).getNodesWithTurnImpedances();
            		if (idNodesWithTurnImpedances!= null && !idNodesWithTurnImpedances.isEmpty()){
            			for(int i=0; i < idNodesWithTurnImpedances.size(); i++){
            				Node simpleNode=null;
            				try{
            					simpleNode = ((DynamicGraph) graph).getNode(idNodesWithTurnImpedances.get(i));
            				}catch (ElementNotFoundException e) {
							}catch (Exception e) {
								e.printStackTrace();
							}
            				if (simpleNode != null){
            					// Existe el nodo con TurnImpedances
            					LocalGISTurnImpedance turnImpedances =((LocalGISStreetRouteReaderWriter)db).getNodeTurnImpedances(simpleNode.getID());
            					if (turnImpedances != null){
            						UniqueIDGenerator uidGen = new FixedValueUIDGenerator(simpleNode.getID());
            						GeographicNodeWithTurnImpedances nodeTurn = new GeographicNodeWithTurnImpedances(
            								((GeographicNode)simpleNode).getPosition(), 
            								turnImpedances, 
            								uidGen);
            						((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)graph).getMemoryManager()).replaceNodeInstance(simpleNode, nodeTurn);
            						
            							 						 
            						
            					}
            				}
            			}
            		}
            	}
            }

            
            // Cargamos el grado en el networkmanager
            if (graph != null){
				NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
				
				if (((BasicNetworkManager)networkMgr).getInterNetworker() == null ){
					BasicInterNetworker binNet = new BasicInterNetworker();
					binNet.setNetworkManager(networkMgr);
					networkMgr.setInterNetworker(binNet);
				}
				
				
				Network parent = networkMgr.putNetwork("RedBaseDatos", null);
				RouteConnectionFactory routeConnectionFactory = new GeopistaRouteConnectionFactoryImpl();
				((LocalGISNetworkManager) networkMgr).putNetworkWithProperties(networkName, graph, routeConnectionFactory);
			}


		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		/*
		 * Ahora aqui representaremos del grafo sus arcos
		 */
		//monitor.report(I18N.get("leerredbase","routeengine.leerred.creatinglayer"));

		return true;

	}
	
	
	private static boolean isStreetNetwork(String networkName) {
		boolean resultado = false; 
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;
		
		try{
					
			String sql = "SELECT * FROM network_street_properties where id_network = " + getIdNetwork(networkName); //+ getIdNetwork(networkName);
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
			st= conn.prepareStatement(sql);
			st.execute();
			rs = st.getResultSet();
			// process resultset and create list of nodes
			while (rs.next())
			{
				return true;	
			}
			
		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
		}
		
		return resultado;
	}

	
	private static int getIdNetwork(String networkName) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;
		
		try{
			
			String sqlid_subred = "(SELECT id_network FROM networks WHERE network_name='"
				+ networkName + "')";
			
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
			st= conn.prepareStatement(sqlid_subred);
			rs = st.executeQuery();
			
			// process resultset and create list of nodes
			while (rs.next())
			{
				return rs.getInt("id_network");
			}
			
		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
		}
		
		return 0;
	}
}
