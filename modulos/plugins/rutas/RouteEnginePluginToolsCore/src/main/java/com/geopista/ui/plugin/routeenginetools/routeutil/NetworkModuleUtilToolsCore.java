/**
 * NetworkModuleUtilToolsCore.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;

import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.NetworkPropertiesReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

public class NetworkModuleUtilToolsCore extends NetworkModuleUtil
{

   
   
    public static EnableCheck createEnableNetworkSelectedCheck(final MultiInputDialog diag, final String fieldname)
    {
	return new EnableCheck()
	    {
		@Override
		public String check(JComponent component)
		{
		    PanelToSelectMemoryNetworks panel = (PanelToSelectMemoryNetworks) diag.getComponent(fieldname);
		    JLabel label = (JLabel) diag.getLabel(fieldname);
		    if ("".equals(panel.getRedSeleccionada()) && "".equals(panel.getSubredseleccionada()))
			{
			    return "Debe seleccionar alguna red en " + label.getText() + ".";
			} else
			return null;
		}
	    };
    }
    
    

    /**
     * Se enlaza la base de datos con la red Se cargan los elementos que haya en la base de datos. se fusionan lso elementos antiguos de la red renumerándose Se
     * guardan los elementos antiguos en la base de datos.
     * 
     * Las propiedades de la red se mezclan con las existentes. Tiene prioridad las prioridades de la red en memoria.
     * 
     * @param nuevoNombreParaRed
     * @param selectedNet
     * @param monitor
     * @param toFile
     *            TODO
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static boolean linkNetworkToStore(String nuevoNombreParaRed, Network selectedNet, Operations operation, boolean toFile, TaskMonitor monitor)
	    throws IOException
    {
	boolean renumbering = false; // disable graphable renumbering

	// Si la red ya está conectada a la base de datos y gestionada por LocalgisAllInMemoryManager
	Graph selectedGraph = selectedNet.getGraph();
	LocalGISAllinMemoryManager lgMemmgr = NetworkModuleUtil.castToLocalGISAllinMemoryManager(selectedGraph);
	if (lgMemmgr == null)
	    { // Sustituye el memoryManager
		DynamicGraph dynGraph = NetworkModuleUtil.getNewInMemoryGraph(selectedGraph);
		selectedNet.setGraph(dynGraph);
		lgMemmgr = (LocalGISAllinMemoryManager) dynGraph.getMemoryManager();
	    }

	lgMemmgr.setTaskMonitor(monitor);
	boolean linkedToDatabase = lgMemmgr.isLinkedToDatabase();
	if (operation==Operations.WRITE ||
		(!linkedToDatabase && toFile == false) || (!lgMemmgr.isLinkedToFile() && toFile == true)
		|| !nuevoNombreParaRed.equals(lgMemmgr.getStoreNetworkName()))
	    {
		if (operation==Operations.LOAD)
		    {
			if (!toFile) // precarga las propiedades para conocer los parámetros que necesita LocalgisRouteReadeWriter
			    {
			try
			    {
			
				RouteConnectionFactory fact = new GeopistaRouteConnectionFactoryImpl();
				Map<String, NetworkProperty> props = NetworkModuleUtil.readNetworkPropertiesFromDB(nuevoNombreParaRed, fact.getConnection());
				selectedNet.addProperties(props);
			    } catch (SQLException e)
			    {
				throw new IOException(e);
			    }
			    }
		    }
		// preserva contenido actual. Usamos un nuevo ArrayList porque la colección se modifica al cargar la red
		Collection<Edge> prevEdges = new ArrayList<Edge>(lgMemmgr.getEdges());
		Collection<Node> prevNodes = new ArrayList<Node>(lgMemmgr.getNodes());
		BasicGraph previousGraph = new BasicGraph(prevNodes, prevEdges);
		if (toFile)
		    {
			lgMemmgr.linktoFile(nuevoNombreParaRed);
		    } else
		    {

			// modifica la red para que pase a utilizar un readerwriter de red
			lgMemmgr.linkToDataBase(nuevoNombreParaRed, selectedNet);
			// Asigna el networkId a los elementos previos
			int networkId = lgMemmgr.getStoreNetworkId();
			NetworkModuleUtil.updateLocalGISEdgesWithNetworkId(previousGraph, networkId);
		    }

		// load properties
		if (lgMemmgr.getStore() instanceof NetworkPropertiesReaderWriter)
		    {
			NetworkPropertiesReaderWriter lgNetPropsStore = (NetworkPropertiesReaderWriter) lgMemmgr.getStore();
			Map<String, NetworkProperty> props = lgNetPropsStore.readNetworkProperties();// NetworkModuleUtil.readNetworkPropertiesFromDB(netWorkName,connectionFactory.getConnection()

			Map<String, Object> propsPrev = selectedNet.getProperties();
			if (props != null && (operation == Operations.LOAD_MERGING)) // mezcla las propiedades
			    {
				selectedNet.addProperties(props);
				if (propsPrev != null) // asegura que las propiedades locales quedan por encima
				    {
					selectedNet.addProperties(propsPrev);
				    }
			    } else if (props != null && operation == Operations.LOAD)
			    {
				selectedNet.getProperties().clear();
				selectedNet.addProperties(props);
			    }

			if (operation == Operations.WRITE)
			    {
				lgNetPropsStore.writeNetworkProperties(selectedNet.getProperties());
			    }
		    }

		if (operation == Operations.WRITE)// clear contents
		    {
			lgMemmgr.eraseGraph();
		    }
		Collection<Edge> loadedEdges = Collections.EMPTY_LIST;
		Collection<Node> loadedNodes = Collections.EMPTY_LIST;
		if (operation == Operations.LOAD || operation == Operations.LOAD_MERGING)
		    {
			monitor.report("Cargando contenido pre-existente de la red.");
			// carga el contenido de la base de datos si existe
			loadedEdges = lgMemmgr.getEdges();
			loadedNodes = lgMemmgr.getNodes();
		    }
		// AÃ±ade los elementos previos para forzar la grabacion con el commit

		if (operation == Operations.LOAD_MERGING && renumbering)
		    if (!prevEdges.isEmpty()) // proceso de copia de elementos
			{

			    BasicGraph justLoadedGraph = new BasicGraph(loadedNodes, loadedEdges);
			    // renumerar todos los elementos y fijar el UIDGenerator para el futuro.
			    SequenceUIDGenerator uid = renumberElements(previousGraph, justLoadedGraph);
			    lgMemmgr.setUIDGenerator(uid);

			}
		lgMemmgr.getUpdateMonitor().reset(); // todos los elementos se deben actualizar
		lgMemmgr.appendGraph(previousGraph);// Add the nodes that was previously in the graph
		lgMemmgr.commit();
	    } else
	    {// por petición del usuario
		lgMemmgr.commit();
	    }
	// Regenera los networklinks
	selectedNet.getNetworkManager().scanForNetworkLinksFor(selectedNet);
	// Asegura que el nombre de la red es el nuevo
	//if (!selectedNet.getName().equals(nuevoNombreParaRed))
//  {
	NetworkManager networkManager = selectedNet.getNetworkManager();
	if (networkManager.getNetwork(selectedNet.getName())!=null)
		networkManager.detachNetwork(selectedNet.getName()); // elimina cualquier network con el mismo nombre antiguo
	if(networkManager.getNetwork(nuevoNombreParaRed)!=null)
	    networkManager.detachNetwork(nuevoNombreParaRed);// elimina cualquier otra red con esta denominación
	//networkManager.detachNetwork(selectedNet.getName());
	selectedNet.setName(nuevoNombreParaRed);// renombra la red cargada
	networkManager.putNetwork(selectedNet);
//  }

return true;

    }

    /**
     * @param edge
     * @return
     */
    public static IGeopistaLayer getOriginalLayer(Edge edge, PlugInContext context)
    {
	IGeopistaLayer layer;
	edge = (Edge) NodeUtils.unwrapProxies(edge);
	GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
	Map properties = new HashMap();
	// Introducimos el mapa Origen
	properties.put("mapadestino", (GeopistaMap) context.getTask());
	// Introducimos el fitro geometrico si es distinto de null, si se introduce null falla
	// properties.put("filtrogeometrico",null);
	// Introducimos el FilterNode
	properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
	// Introducimos el srid del mapa destino
	properties.put("srid_destino", Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode()));
	serverDataSource.setProperties(properties);
	GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();
	// preparamos la url de la layer
	Connection conn = new GeopistaRouteConnectionFactoryImpl().getConnection();
	
	String layerID = getQueryFromIdLayer(conn, ((ILocalGISEdge) edge).getIdLayer());
	layer = NetworkModuleUtilWorkbench.loadSystemLayer(layerID, context);

	return layer;
    }
    

    
    
    
    
    /**
     * Borra todos los eleentos de la red
     * 
     * @param userSelectedNetworkName
     * @throws SQLException
     */
    public static void clearNetwork(String networkName) throws SQLException
    {
	RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
	LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
	Connection connection = routeConnection.getConnection();
	Integer idnetwork = dao.getNetworkId(networkName, connection);
	dao.deleteNetworkEdgesFromDataBaseById(idnetwork, connection);
	dao.deleteNetworkNodesFromDataBaseById(idnetwork, connection);
	ConnectionUtilities.closeConnection(connection);
    }


  
}
