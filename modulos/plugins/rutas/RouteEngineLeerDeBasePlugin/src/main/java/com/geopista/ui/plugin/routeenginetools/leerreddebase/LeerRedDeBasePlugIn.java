/**
 * LeerRedDeBasePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.leerreddebase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.routeenginetools.leerreddebase.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.leerreddelocal.ReadNetworkAbstractPlugin;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;

import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsDraw;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataStore;
import com.localgis.route.datastore.LocalGISStreetResultSet;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class LeerRedDeBasePlugIn extends ReadNetworkAbstractPlugin 
{
	
  
	public String getName() {
		return I18N
				.get("leerredbase","AddDatastoreLayerPlugIn");
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("leerredbase","routeengine.leerred.iconfile"));
	}
	 protected PanelToLoadFromDataStore createPanel(PlugInContext context) {
		return new PanelToLoadFromDataStore(context.getWorkbenchContext());
	}
	protected Layer createLayer(DynamicGraph graph, String subredName,final PlugInContext context) throws Exception 
	{
	   
		if (graph != null)
		    {
		    	//registerNetworkInNetworkManager(graph, context, subredName);
		    	return NetworkModuleUtilToolsDraw.createGraphLayer(graph, context, subredName, "Red de BBDD");
		} else{
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
					"Error al leer Red de la Base de Datos. No se ha encontrado ninguna Red en la Base de Datos");
			return null;
		}
	}

	@Deprecated
	public static void registerNetworkInNetworkManager(DynamicGraph graph, final PlugInContext context, String subred) throws SQLException,
		NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException
	{
	   
	    
	    			NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
	    
	    			if (((BasicNetworkManager)networkMgr).getInterNetworker() == null )
	    			{
	    				BasicInterNetworker binNet = new BasicInterNetworker();
	    				binNet.setNetworkManager(networkMgr);
	    				networkMgr.setInterNetworker(binNet);
	    			}
	    
	    			Network parent = networkMgr.putNetwork("RedBaseDatos", null);
	    			RouteConnectionFactory routeConnectionFactory = new GeopistaRouteConnectionFactoryImpl();
	    		
	    
	    		((LocalGISNetworkManager) networkMgr).putNetworkWithProperties( subred, graph, routeConnectionFactory);
	    			
	    // MOVER A MÃ©todo propio
	    return;
	}

	
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.leerreddebase.language.RouteEngine_LeerRedBasei18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("leerredbase",bundle);

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(
				this.getIcon(),
                this,
                createEnableCheck(context.getWorkbenchContext()),
                context.getWorkbenchContext());

	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(workbenchContext);
		return new MultiEnableCheck()
				.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
				.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
				.add(checkFactory.createTaskWindowMustBeActiveCheck())
				.add(checkFactory.checkIsOnline())
				;
	}

//	protected Network linkGraphToStore(PanelToLoadFromDataStore panel,
//		TaskMonitor monitor, PlugInContext context) throws IOException  
//{     
//TODO JPC Generalizar para que StreetNetwork sea una especialización de la red normal
//	if (isStreetNetwork(panel.getSubredSelected()))
//	    {
//		graphGenerator = NetworkModuleUtil.getLocalGISStreetGraphGenerator();
//				db = new LocalGISStreetRouteReaderWriter(connectionFactory, bPMRGraph);
//	    } else
//	    {
//
//		graphGenerator = NetworkModuleUtil.getBasicLineGraphGenerator();
//				db = new LocalGISRouteReaderWriter(connectionFactory);
//			}
//
//			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
//
//			db.setNetworkName(panel.getSubredSelected());
//            //TODO: RUBENGOMEZ -- Si se agregan los nodos directamente al cargar, reescribe el nodo y lo cambia de id.
//            if(graphGenerator instanceof LocalGISStreetGraphGenerator)
//        	{
//            	if (db instanceof LocalGISStreetRouteReaderWriter){
//            		ArrayList<Integer> idNodesWithTurnImpedances = ((LocalGISStreetRouteReaderWriter)db).getNodesWithTurnImpedances();
//            		if (idNodesWithTurnImpedances!= null && !idNodesWithTurnImpedances.isEmpty()){
//            			for(int i=0; i < idNodesWithTurnImpedances.size(); i++){
//            				Node simpleNode=null;
//            				try{
//            					simpleNode = ((DynamicGraph) graph).getNode(idNodesWithTurnImpedances.get(i));
//            				}catch (ElementNotFoundException e) {
//							}catch (Exception e) {
//								e.printStackTrace();
//							}
//            				if (simpleNode != null){
//            					// Existe el nodo con TurnImpedances
//            					LocalGISTurnImpedance turnImpedances =((LocalGISStreetRouteReaderWriter)db).getNodeTurnImpedances(simpleNode.getID());
//            					if (turnImpedances != null){
//            						UniqueIDGenerator uidGen = new FixedValueUIDGenerator(simpleNode.getID());
//            						GeographicNodeWithTurnImpedances nodeTurn = new GeographicNodeWithTurnImpedances(
//            								((GeographicNode)simpleNode).getPosition(),
//            								turnImpedances,
//            								uidGen);
//
//            						 try
//            						 {
//            							 if(simpleNode instanceof ProxyNode)
//            								 simpleNode = (Node) NodeUtils.unwrapProxies(simpleNode);
//            							 if(!(simpleNode instanceof GeographicNodeWithTurnImpedances))
//            								 ((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)graph).getMemoryManager()).replaceNodeInstance(simpleNode, nodeTurn);
//            						 }
//            						 catch (Exception e)
//            						 {
//            						 // TODO Auto-generated catch block
//            						 e.printStackTrace();
//            						 }
//            					}
//            				}
//            			}
//            		}
//            	}
//            	else
//		    {
//			throw new IllegalStateException("Database connection: " + db + " is not suitable to:" + graphGenerator);
//		    }
//            }
//
//            graph.getEdges();
//            return null; 
//	}

	
	private boolean isStreetNetwork(String networkName) {
		boolean resultado = false;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs=null;

		try{
		    RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn= connectionFactory.getConnection();
		    LocalGISNetworkDAO dao=new LocalGISNetworkDAO();
		    Hashtable<Integer, LocalGISStreetResultSet> streetProperties = dao.getStreetDataList(networkName, conn);
		    return !streetProperties.isEmpty();
		}catch (SQLException e)
		{
			throw new RuntimeException(e);
		}finally{
			ConnectionUtilities.closeConnection(conn, st, rs);
		}
	}

//	private int getIdNetwork(String networkName) {
//		boolean resultado = false;
//		Connection conn = null;
//		PreparedStatement st = null;
//		ResultSet rs=null;
//
//		try{
//
//			String sqlid_subred = "(SELECT id_network FROM networks WHERE network_name='"
//				+ networkName + "')";
//
//			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
//			conn= connectionFactory.getConnection();
//			st= conn.prepareStatement(sqlid_subred);
//			rs = st.executeQuery();
//
//			// process resultset and create list of nodes
//			while (rs.next())
//			{
//				return rs.getInt("id_network");
//			}
//
//		}catch (SQLException e)
//		{
//			e.printStackTrace();
//		}finally{
//			ConnectionUtilities.closeConnection(conn, st, rs);
//		}
//
//		return 0;
//	}
	public void linkGraphToStoreAndCreateLayer(String networkName, TaskMonitor monitor, PlugInContext context) throws Exception
	{
	   
	if (!AppContext.getApplicationContext().isLogged()){
			AppContext.getApplicationContext().login();
		}
	
	monitor.allowCancellationRequests();
	monitor.report("Cargando grafo de base de datos");
	Network net=linkGraphToStore(false,true,networkName, monitor, context);
	/*
	 * Ahora aqui representaremos del grafo sus arcos
	 */
	if (net!=null)
	    {
		monitor.report(I18N.get("jump.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn.Creating-layer"));
		createLayer((DynamicGraph) net.getGraph(),  networkName, context);
		context.getWorkbenchGuiComponent().warnUser("Se ha cargado desde servidor la red :"+net.getName());
	    }
	else
	    {
		context.getWorkbenchGuiComponent().warnUser("No se han cargado redes.");
	    }
	}

	
}