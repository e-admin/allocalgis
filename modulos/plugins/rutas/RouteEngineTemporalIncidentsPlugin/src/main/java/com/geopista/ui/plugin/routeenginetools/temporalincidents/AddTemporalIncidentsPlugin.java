/**
 * AddTemporalIncidentsPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.temporalincidents;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.AllInMemoryManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.temporalincidents.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.util.IdEdgeNetworkBean;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class AddTemporalIncidentsPlugin extends ThreadedBasePlugIn{
	
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean buttonAdded;
	
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.temporalincidents.language.RouteEngine_TemporalIncidentsi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("temporalIncidents",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);

	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("temporalIncidents","routeengine.temporalincidents.iconfile.add"));
	}
	
	public String getName(){
    	String name = I18N.get("temporalIncidents","routeengine.temporalincidents.plugintitle.add");
    	return name;
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
		.add(checkFactory.createExactlyNFeaturesMustHaveSelectedItemsCheck(1));
	}
	
	public boolean execute(PlugInContext context)throws Exception {
		
		if(context.getLayerViewPanel() == null){
			return false;
		}
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() != 1){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.exactly1feature"));
			return false;
		}
		Feature edgeFeature = (Feature) featuresselected.get(0);
		//TODO: Cambiar la llamada
		String networkName = "";
		String idEdge = "";
		try{
			idEdge = edgeFeature.getString("idEje");
			networkName = edgeFeature.getString("networkName");
		}catch (Exception e){
			if((networkName == null || networkName.equals("")) && idEdge != null){
				try {
					if(context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems().size() == 1)
						networkName = ((GeopistaLayer)context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems().iterator().next()).getName();
				}catch(Exception e2){
					//Interrumpir ejecucion
					//JOptionPane.showMessageDialog(app.getMainFrame(),I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.general"));
					context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.general"));
					return false;
					
				}
			}
			if(networkName == null || networkName.equals("") || idEdge == null || idEdge.equals("")){
				context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.general"));
				return false;
			}
				
		}
		
		
		NetworkManager nManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network network = nManager.getNetwork(networkName);
		// en caso de que estï¿½ en raiz y contenga el grafo
		/*if(network == null || network.getGraph() == null){
			//Seguimos buscando
			Map<String,Network> networksContainer = nManager.getNetworks();
			ArrayList<Network> networks = new ArrayList<Network>(networksContainer.values());
			Iterator<Network> actualParentNetwork = networks.iterator();
			while(actualParentNetwork.hasNext() && network == null){
				Network actualNetwork = actualParentNetwork.next();
				//TODO: No funciona porque al crearla mete espacios. hago bucle y lo busco
				Map<String,Network> subnetworks = actualNetwork.getSubnetworks();
				Set<String> keysSubnetworks  = subnetworks.keySet();
				Iterator<String> subNetworksIterator = keysSubnetworks.iterator();
				while(subNetworksIterator.hasNext() && network == null){
					String subnetworkName = subNetworksIterator.next();
					if(networkName.equals(subnetworkName.trim())){
						network = actualNetwork.getSubNetwork(subnetworkName);
					}
				}
				//network = actualNetwork.getSubNetwork(networkName);
			}
		}*/
		if(network == null){
			//JOptionPane.showMessageDialog(app.getMainFrame(),I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.notloaded"));
			context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.notloaded"));
			return false;
		}
		ILocalGISEdge edgeToModify = null;
		//Buscamos el edge actual. (Habrï¿½a que actualizarlo)
		if(network.getGraph() instanceof DynamicGraph){
			// Es un tipo dinamico. Se actualiza automaticamente por medio 
			DynamicGraph graph = (DynamicGraph)network.getGraph();
			if(graph.getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
				if(((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).getStore() instanceof LocalGISRouteReaderWriter){
					edgeToModify = (ILocalGISEdge)((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).getEdge(Integer.parseInt(idEdge));
					//((BasicExternalSourceMemoryManager)graph.getMemoryManager()).commit();
				}
			// Else para AllInMemory Manger (una red creada dinamicamente)
			} else if (graph.getMemoryManager() instanceof AllInMemoryManager){
				try{
					edgeToModify = (ILocalGISEdge) ((AllInMemoryManager)graph.getMemoryManager()).getEdge(Integer.parseInt(idEdge));
				}catch (Exception e) {
				}
			}
		}else{
			ArrayList edges = new ArrayList(network.getGraph().getEdges());
			AllInMemoryManager manager = new AllInMemoryManager();
			manager.setGraph(network.getGraph());
			int id = Integer.parseInt(idEdge);
			edgeToModify = (ILocalGISEdge)manager.getEdge(id);
			
		}
		if (edgeToModify!=null){
			if(app.getBlackboard().get("temporalincidents") == null){
				ArrayList<IdEdgeNetworkBean> temporalIncidents = new ArrayList<IdEdgeNetworkBean>();
				IdEdgeNetworkBean nBean = new IdEdgeNetworkBean(networkName,((Edge)edgeToModify).getID());
				temporalIncidents.add(nBean);
				app.getBlackboard().put("temporalincidents",temporalIncidents);
			}else{
				ArrayList<IdEdgeNetworkBean> temporalIncidents = (ArrayList<IdEdgeNetworkBean>)app.getBlackboard().get("temporalincidents");
				IdEdgeNetworkBean nBean = new IdEdgeNetworkBean(networkName,((Edge)edgeToModify).getID());
				temporalIncidents.add(nBean);
			}
			JOptionPane.showMessageDialog(context.getWorkbenchFrame(), I18N.get("temporalIncidents","routeengine.temporalincidents.add.added"));
			context.getLayerViewPanel().getContext().setStatusMessage(I18N.get("temporalIncidents","routeengine.temporalincidents.add.added"));
			//JOptionPane.showMessageDialog(app.getMainFrame(),I18N.get("temporalIncidents","routeengine.temporalincidents.add.added"));
			return true;
		}else{
			return false;
		}
	}
	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!buttonAdded )
		{
//			toolbox.addToolBar();
			AddTemporalIncidentsPlugin explode = new AddTemporalIncidentsPlugin();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			buttonAdded = true;
		}
	}
	
}
