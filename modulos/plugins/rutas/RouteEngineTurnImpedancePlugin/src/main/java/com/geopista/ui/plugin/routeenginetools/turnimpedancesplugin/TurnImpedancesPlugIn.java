/**
 * TurnImpedancesPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGraphable;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.impedance.TurnImpedances;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.dialog.TurnImpedancesDialog;
import com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.build.UIDgenerator.FixedValueUIDGenerator;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class TurnImpedancesPlugIn extends ThreadedBasePlugIn{

	private PlugInContext context = null;
	private TurnImpedancesDialog dialog = null;
	private ILayerViewPanel panel = null;
	private boolean turnImpedancesModifierButtonAdded = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private int nodeId = -1;
	private GeopistaLayer selectedLayer = null;
	private Node selectedNode = null;
	private Network selectedNetwork = null;
	

	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;
		this.panel = context.getLayerViewPanel();
		
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() != 1){
			panel.getContext().warnUser(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.exactlyonefeature"));
			return false;
		}
		GeopistaFeature feature = (GeopistaFeature) featuresselected.get(0);
		if (feature.getSchema().hasAttribute(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.nodeidattributename"))){

			Object selectedNodeId = feature.getAttribute(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.nodeidattributename")); 
			if (selectedNodeId!= null){
				this.nodeId = (Integer) selectedNodeId;
			}else{
				// si no conseguimos el nodeId salimos y no continua el plugin
				return false;
			}
			
			Collection<GeopistaLayer> layers = context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems();
			for (int i=0; i < layers.size(); i++){
				if (((GeopistaLayer)layers.toArray()[i]).getFeatureCollectionWrapper().getFeatures().contains(featuresselected.get(0))){
					this.selectedLayer  = (GeopistaLayer) layers.toArray()[i];
				}
			}
			

			String netName="";
			
			if(selectedLayer.getSystemId().startsWith("Nodos-")){
				String[] a = selectedLayer.getSystemId().split("-");
				netName = a[1];
				//System.out.println(a.length);
				for (int i=2; i < a.length; i++){
					netName = netName + "-" + a[i];
				}
			} else{
				netName = selectedLayer.getSystemId();
			}
			
			selectedNode  = searchNodeByIdAndNetworkName(context, netName, this.nodeId);
			selectedNetwork = searchNetworkByName(context, netName);

			if ( selectedNode != null){
				((DynamicGraphable)selectedNode).getGraphableForSaving();
				this.dialog = new TurnImpedancesDialog(this.context, 
						I18N.get("turnimpedancesplugin","routeengine.turnimpedances.dialogtitle"),
						selectedNode, selectedNetwork );
			} else{
				JOptionPane.showMessageDialog(null, 
						I18N.get("turnimpedancesplugin","routeengine.turnimpedances.error.message.notnodefound") + netName +"'");
				return false;
			}

			if (dialog.wasOKPressed()){
				//LocalGISTurnImpedance turnImpedances = (LocalGISTurnImpedance).getObject();
				NetworkManager nMan = NetworkModuleUtilWorkbench.getNetworkManager(context);
				Network network = nMan.getNetwork(netName);
				Node existentNode = dialog.getNode();
				if(network.getGraph() instanceof DynamicGraph && ((DynamicGraph)network.getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
					LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
					RouteConnectionFactory rcf = new GeopistaRouteConnectionFactoryImpl();
					Connection connection = null;
					try{
						connection = rcf.getConnection();
						int networkId=nDAO.getNetworkId(netName, connection);
						nDAO.clearImpedanceMatrix(netName,networkId, this.dialog.getNode().getID(), connection);
						nDAO.setImpedanceMatrix(netName,networkId, this.dialog.getNode(), connection);

						UniqueIDGenerator uidGen = new FixedValueUIDGenerator(existentNode.getID());
						GeographicNodeWithTurnImpedances nodeTurn = new GeographicNodeWithTurnImpedances(
								((GeographicNode)this.dialog.getNode()).getPosition(), 
								(TurnImpedances) this.dialog.getNode().getObject(), 
								uidGen);
						((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).replaceNodeInstance(existentNode, nodeTurn);
						/*
						ArrayList<DynamicEdge> edges = new ArrayList<DynamicEdge>();
//						Iterator<Node> iterNodes = dialog.getNode().getRelatedNodes().iterator();
						ArrayList<Node> nodes = new ArrayList<Node>();
//						Iterator<Edge> iterEdges = this.dialog.getNode().getEdges().iterator();
						
//						while(iterEdges.hasNext()){
//							Edge edge = iterEdges.next();
//							if (edge.getNodeA().equals(this.dialog.getNode())){
//								((DynamicEdge) edge).setNodeA(nodeTurn);
//							} else if (edge.getNodeB().equals(this.dialog.getNode())){
//								((DynamicEdge) edge).setNodeB(nodeTurn);
//							}
//							edges.add((DynamicEdge) edge);
//						}
						Collection<Node> relatedNodes=existentNode.getRelatedNodes();
						for (Node node : relatedNodes){
							Edge edg= existentNode.getEdge(node);
							nodeTurn.add(edg);
						}
						
						nodes.add(nodeTurn);
//						nodeTurn.getRelatedGraphables().addAll(edges);
//						nodeTurn.getRelatedGraphables().addAll(this.dialog.getNode().getRelatedNodes());
						Graph gr=new BasicGraph(nodes, edges);

						 try{
							 
							 //((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).activeLoadedFlag();
							 //((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).removeGraphable(this.dialog.getNode());
							 //((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).appendGraphSilently(gr);						 
						 }catch (Exception e){
							 e.printStackTrace();
						 }*/

					}finally{
						ConnectionUtilities.closeConnection(connection);
					}
				} else if (network.getGraph() instanceof DynamicGraph && ((DynamicGraph)network.getGraph()).getMemoryManager() instanceof AllInMemoryManager){
					
					GeographicNodeWithTurnImpedances nodeTurn;
					if (! (existentNode instanceof GeographicNodeWithTurnImpedances)) 
					{
						UniqueIDGenerator uidGen = new FixedValueUIDGenerator(existentNode.getID());
						nodeTurn = new GeographicNodeWithTurnImpedances(
								((GeographicNode)this.dialog.getNode()).getPosition(), 
								(TurnImpedances) this.dialog.getNode().getObject(), 
								uidGen);
						((DynamicGraph)network.getGraph()).getMemoryManager().replaceNodeInstance(existentNode, nodeTurn);
					}
					else
					{
						nodeTurn=(GeographicNodeWithTurnImpedances) existentNode;
					}
					

					//TODO Acabar de implementar
					nodeTurn.setTurnImpedances((TurnImpedances) this.dialog.getNode().getObject());
					
//					ArrayList<DynamicEdge> edges = new ArrayList<DynamicEdge>();
//					ArrayList<Node> nodes = new ArrayList<Node>();
//
//					Collection<Node> relatedNodes=dialog.getNode().getRelatedNodes();
//					for (Node node : relatedNodes){
//						Edge edg= dialog.getNode().getEdge(node);
//						nodeTurn.add(edg);
//					}
//
//					nodes.add(nodeTurn);
//					Graph gr=new BasicGraph(nodes, edges);
//					try{
//						((AllInMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).activeLoadedFlag();
//						((AllInMemoryManager)((DynamicGraph)network.getGraph()).getMemoryManager()).appendGraph(gr);						 
//					}catch (Exception e){
//						e.printStackTrace();
//					}
				}
				return true;
			}
		}
		
		return false;
	}
	


	private ArrayList<Edge> searchNodesWithNodeIdB(ArrayList<Edge> edges, int id) {
		ArrayList<Edge> resultado = new ArrayList<Edge>();
		Iterator<Edge> it = edges.iterator();
		Edge edge = null;
		while(it.hasNext()){
			edge = it.next();
			if (edge.getNodeB().getID() == nodeId){
				resultado.add(edge);
			}
		}
		return resultado;
	}

	private ArrayList<Edge> searchNodesWithNodeIdA(ArrayList<Edge> edges, int nodeId) {
		ArrayList<Edge> resultado = new ArrayList<Edge>();
		Iterator<Edge> it = edges.iterator();
		Edge edge = null;
		while(it.hasNext()){
			edge = it.next();
			if (edge.getNodeA().getID() == nodeId){
				if (((LocalGISStreetDynamicEdge)edge).getTrafficRegulation().equals(StreetTrafficRegulation.DIRECT) ||
						((LocalGISStreetDynamicEdge)edge).getTrafficRegulation().equals(StreetTrafficRegulation.BIDIRECTIONAL)){
					resultado.add(edge);	
				}
				
			}
		}
		return resultado;
	}

	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {

		
	}
	
//	private ArrayList<Edge> searchEdgesWithNodeId(PlugInContext context, int nodeId){
//		ArrayList<Edge> resultado = new ArrayList<Edge>();
//		NetworkManager myNetManager = FuncionesAuxiliares.getNetworkManager(context);
//		Iterator<Network> it = myNetManager.getNetworks().values().iterator();
//		while (it.hasNext()){
//			ArrayList<Edge> edges =	searchEdgesWithNodeIdRecursive(it.next(), nodeId);
//			if (!edges.isEmpty()){
//				resultado.addAll(edges);
//			}
//		}		
//		return resultado;
//	}
	
//	private ArrayList<Edge> searchEdgesWithNodeIdRecursive(Network network, int nodeId){
//		ArrayList<Edge> resultado = new ArrayList<Edge>();
//		if (network.getSubnetworks()!= null && !network.getSubnetworks().isEmpty()){
//			Iterator<Network> netsIterator = network.getSubnetworks().values().iterator();
//			while(netsIterator.hasNext()){
//				ArrayList<Edge> edges = searchEdgesWithNodeIdRecursive(netsIterator.next(), nodeId);
//				resultado.addAll(edges);
//			}
//		} else{
//			if(network != null){
//				Iterator<Edge> it = network.getGraph().getEdges().iterator();
//				Edge actualEdge = null;
//				while(it.hasNext()){
//					actualEdge = it.next();
//					if (actualEdge.getNodeA().getID()==nodeId || actualEdge.getNodeB().getID()==nodeId){
//						resultado.add(actualEdge);
//					}
//				}
//			}
//		}
//		
//		return resultado;
//	}
	
//	private ArrayList<Edge> searchEdgesWithNodeA(PlugInContext context,
//			String netName, int id) {
//		Network selectedNetwork = null;
//		Edge selectedEdge = null;
//		ArrayList<Edge> resultado = new ArrayList<Edge>();
//		selectedNetwork = searchNetworkByName(context,netName);
//		if (selectedNetwork != null){
//			Iterator<Edge> edges = selectedNetwork.getGraph().getEdges().iterator();
//			Edge actualEdge = null;
//			while(edges.hasNext()){
//				actualEdge = edges.next();
//				if (actualEdge.getID() == id){
//					resultado.add(actualEdge);
//					break;
//				}
//			}
//		}
//		return resultado;
//	}
	
	private Node searchNodeByIdAndNetworkName(PlugInContext context,
			String netName, int id) {
		Network selectedNetwork = null;
		Node selectedNode = null;
		selectedNetwork = searchNetworkByName(context,netName);
		if (selectedNetwork != null){
			Iterator<Node> edges = selectedNetwork.getGraph().getNodes().iterator();
			Node actualNode = null;
			while(edges.hasNext()){
				actualNode = edges.next();
				if (actualNode.getID() == id){
					selectedNode = actualNode;
					break;
				}
			}
		}
		return selectedNode;
	}

	/**
	 * @param context
	 * @param netName
	 * @param selectednetwNetwork
	 * @return
	 */
	private Network searchNetworkByName(PlugInContext context,
			String netName) {
		
		Network net1 = NetworkModuleUtilWorkbench.getNetworkManager(context).getNetwork(netName);
		Iterator<Network> it = NetworkModuleUtilWorkbench.getNetworkManager(context).getNetworks().values().iterator();
		while (it.hasNext()){
			Network net = it.next();
			if (net.getName().equals(netName)){
				return  net;
			} else if (!net.getSubnetworks().isEmpty()){
				Network resultado = searchForNetworkRecursive(net, netName);
				if (resultado != null){
					return resultado;
				}
			}
		}
		return null;
	}
	
	private Network searchForNetworkRecursive(Network actualNetwork, String netName) {

		if (actualNetwork.getSubnetworks().isEmpty()){
			if (actualNetwork.getName().equals(netName)){
				return  actualNetwork;
			}
		} else{
			Iterator<Network> iterator = actualNetwork.getSubnetworks().values().iterator();
			while (iterator.hasNext()){
				Network resultado = searchForNetworkRecursive(iterator.next(), netName);
				if (resultado != null){
					return resultado;
				}
			}
		}
		return null;
	}
	
	
	
	
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.iconfile"));
	}
	
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.language.RouteEngine_TurnImpedancesi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("turnimpedancesplugin",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))
		.add(checkFactory.createExactlyNFeaturesMustBeSelectedCheck(1));
	}
	
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!turnImpedancesModifierButtonAdded )
		{
//			toolbox.addToolBar();
			TurnImpedancesPlugIn explode = new TurnImpedancesPlugIn();			
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			turnImpedancesModifierButtonAdded  = true;
		}
	}
}
