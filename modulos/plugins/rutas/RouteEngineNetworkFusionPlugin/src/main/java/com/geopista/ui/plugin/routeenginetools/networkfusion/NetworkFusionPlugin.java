/**
 * NetworkFusionPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networkfusion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.manager.AbstractMemoryManager;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.NetworkFusionFactoryAttachNetworksDialog;
import com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs.NetworkFusionFactorySelectNetworksDialog;
import com.geopista.ui.plugin.routeenginetools.networkfusion.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.localgis.route.graph.structure.basic.GraphType;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ReaderWriterType;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class NetworkFusionPlugin extends AbstractPlugIn{
	
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean buttonAdded;
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.networkfusion.language.RouteEngine_NetworkFusioni18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("networkFusion",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("networkFusion","routeengine.networkfusion.iconfile"));
	}
	
	public String getName(){
    	String name = I18N.get("networkFusion","routeengine.networkfusion.plugintitle");
    	return name;
    }
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!buttonAdded )
		{
//			toolbox.addToolBar();
			NetworkFusionPlugin explode = new NetworkFusionPlugin();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			buttonAdded = true;
		}
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
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}
	@Override
	
	public boolean execute(PlugInContext context)throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		NetworkManager manager = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Map<String,Network> networks = manager.getNetworks();
		Set<String> keys = networks.keySet();
		Iterator<String> it = keys.iterator();
		ArrayList<NetworkBean> networkList = new ArrayList<NetworkBean>();
		while (it.hasNext()){
			//Definicion de Network
			Network network = networks.get(it.next());
			NetworkBean networkBean = null; 
			//TODO: Eliminar con el nuevo codigo la definicion de subnetworks
			Map<String,Network> subnetworks =  network.getSubnetworks();
			if(subnetworks.size() > 0){
				Set<String> keys2 = subnetworks.keySet();
				Iterator<String> it2 = keys2.iterator();
				while(it2.hasNext()){
					String key = it2.next();
					Network subnetwork = subnetworks.get(key);
					networkBean = buildNetworkBean(subnetwork);
					if(networkBean.getgType() != null && networkBean.getRwType() != null && (networkBean.getNetwork().getGraph() instanceof DynamicGraph && ((DynamicGraph)networkBean.getNetwork().getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager))
		            	networkList.add(networkBean);
				}
			}else{
				networkBean = buildNetworkBean(network);
				if(networkBean.getgType() != null && networkBean.getRwType() != null && (networkBean.getNetwork().getGraph() instanceof DynamicGraph && ((DynamicGraph)networkBean.getNetwork().getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager))
	            	networkList.add(networkBean);
			}
            
		}
		if(networkList.size() < 2){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkFusion","routeengine.networkfusion.needMoreNetworksToInitialize"));
			/*JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),I18N.get("networkFusion","routeengine.networkfusion.needMoreNetworksToInitialize"),
                    context.getWorkbenchGuiComponent().getTitle(),
                    JOptionPane.WARNING_MESSAGE);*/
			return false;
		}
        reportNothingToUndoYet(context);
        WizardDialog dialog = new WizardDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),
        I18N.get("networkFusion","routeengine.networkfusion.wizardDialog"), context.getErrorHandler());
        dialog.init(new WizardPanel[] {new NetworkFusionFactorySelectNetworksDialog(networkList),new NetworkFusionFactoryAttachNetworksDialog(context)});
        dialog.setSize(650,550);
        dialog.getContentPane().remove(dialog.getInstructionTextArea());
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        return true;
	}
	private NetworkBean buildNetworkBean(Network network) {
		NetworkBean networkBean = new NetworkBean(network);
		Graph graph = network.getGraph();
        ReaderWriterType accessType = null;
        GraphType graphType = null;
        if(graph instanceof BasicGraph){
        	accessType = ReaderWriterType.MEMORY;

        	
       	 	Iterator<Edge> itEdge = network.getGraph().getEdges().iterator();
       	 	Edge edge = null; 
       	 	if(itEdge.hasNext())
       	 		edge = itEdge.next();
            if(edge instanceof LocalGISStreetDynamicEdge)
            	 graphType = GraphType.STREET;
            else if (edge instanceof ILocalGISEdge)
            	 graphType = GraphType.GENERIC;
        }
        if(graph instanceof DynamicGraph){
       	 	AbstractMemoryManager abstractMemoryManager = (AbstractMemoryManager)((DynamicGraph)graph).getMemoryManager();
            if(abstractMemoryManager instanceof SpatialAllInMemoryExternalSourceMemoryManager){
           	 	accessType = ReaderWriterType.DATABASE;
           	 	Iterator<Edge> itEdge = network.getGraph().getEdges().iterator();
	            Edge edge = itEdge.next();
	            if(edge instanceof LocalGISStreetDynamicEdge)
	            	 graphType = GraphType.STREET;
	            else
	            	 graphType = GraphType.GENERIC;
	             
            }else{
            	accessType = ReaderWriterType.MEMORY;
           	 	Iterator<Edge> itEdge = network.getGraph().getEdges().iterator();
	            Edge edge = itEdge.next();
	            if(edge instanceof LocalGISStreetDynamicEdge)
	            	 graphType = GraphType.STREET;
	            else
	            	 graphType = GraphType.GENERIC;
            }
        }
        networkBean.setgType(graphType);
        networkBean.setRwType(accessType);
        return networkBean;
	}
}
