/**
 * MaxSpeedEditPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.maxspeedplugin;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.maxspeedplugin.dialog.MaxSpeedDialog;
import com.geopista.ui.plugin.routeenginetools.maxspeedplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Category;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * @author javieraragon
 *
 */
public class MaxSpeedEditPlugIn extends ThreadedBasePlugIn{
	
	private TaskMonitor monitor = null;
	private PlugInContext context = null;
	private boolean maxSpeedModifierButtonAdded = false ;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private	MaxSpeedDialog dialog = null;
	private ILayerViewPanel panel = null;
	private int edgeId = -1;
	private GeopistaFeature selectedStreetFeature = null;
	private GeopistaLayer selectedLayer = null;

	
	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;
		
		this.panel = context.getLayerViewPanel();
		
		// Sï¿½lo una feature puede estar seleccionada
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		
		if(featuresselected.size() != 1){
			panel.getContext().warnUser(I18N.get("maxspeedplugin","routeengine.maxspeed.exactlyonefeature"));
			return false;
		}
		
		
		if (featuresselected == null || featuresselected.isEmpty()){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("maxspeedplugin","routeengine.maxspeed.errormessage.selectedfeatures"));
			return false;
		}
		GeopistaFeature feature = (GeopistaFeature) featuresselected.get(0);
		
		
		
		if (feature.getSchema().hasAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.speedattributename"))){
			
			//Antes de mostrar el dialog, guardamos el idnodo para posteriormente
			//Guardar datos.
			//I18N.get("routeengine.maxspeed.edgeidattributename")
			
			if (feature.getSchema().hasAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.edgeidattributename"))){
				if (feature.getAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.edgeidattributename"))!= null){
					this.edgeId = (Integer) feature.getAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.edgeidattributename"));
				}
			} else{
				// si no conseguimos el edgeId salimos y no continua el plugin
				this.dialog.dispose();
				return false;
			}
			
			this.selectedStreetFeature = feature;
			
			Collection<GeopistaLayer> layers = context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems();
			for (int i=0; i < layers.size(); i++){
				if (((GeopistaLayer)layers.toArray()[i]).getFeatureCollectionWrapper().getFeatures().contains(featuresselected.get(0))){
					this.selectedLayer  = (GeopistaLayer) layers.toArray()[i];
				}
			}
			
			double featureMaxSpeed = 0;
			if (feature.getAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.speedattributename")) != null){
				featureMaxSpeed = (Double) feature.getAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.speedattributename"));
			}
			
			this.dialog = new MaxSpeedDialog(this.context, 
					I18N.get("maxspeedplugin","routeengine.maxspeed.dialogtitle"), 
					featureMaxSpeed);

			if (dialog.wasOKPressed()){
				final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		    	final PlugInContext runContext = this.context;
		    	progressDialog.setTitle("TaskMonitorDialog.Wait");
		        progressDialog.report(I18N.get("maxspeedplugin","routeengine.maxspeed.taskmonitormessage"));
		    	progressDialog.addComponentListener(new ComponentAdapter()
		    	{
		    		public void componentShown(ComponentEvent e)
		    		{   
		    			new Thread(new Runnable()
		    			{
		    				public void run()
		    				{
		    					try
		    					{   
		    						withTaskMonitorDo(progressDialog, runContext);
		    					} 
		    					catch (Exception e)
		    					{
		    						e.printStackTrace();
		    					} 
		    					finally
		    					{
		    						progressDialog.setVisible(false);
		    					}
		    				}
		    			}).start();
		    		}
		    	});
		    	GUIUtil.centreOnWindow(progressDialog);
		    	progressDialog.setVisible(true);
		    	return false;
			}

		} else{
			panel.getContext().warnUser(I18N.get("maxspeedplugin","routeengine.maxspeed.nonstreetnetworkerrormessage"));
		}
		return false;
	}
	
	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
			run(monitor, context);
	}



	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		this.monitor = monitor;
		monitor.report(I18N.get("maxspeedplugin","routeengine.maxspeed.savingdatainfomessage"));
		
		if (this.edgeId != -1){
			// Comprobamos que el id de tramo sea distinto de -1 (valor por defecto)
			if (this.selectedLayer != null){
				// Comprobamos que haya una layer (no sea null)
				if (this.selectedStreetFeature != null){
					// Comprobamos que la feature seleccionanda no sea null
					if (selectedLayer != null && !selectedLayer.getName().equals("") ){
						
						String netName="";
						if(selectedLayer.getName().startsWith("Arcos-")){
							netName = selectedLayer.getName().split("-")[1];
						} else{
							netName = selectedLayer.getName();
						}
	
						Edge selectedEdge = searchEdgeByIdAndNetworkName(context, netName);
						
						if (selectedEdge instanceof LocalGISStreetDynamicEdge){
							//Se actualiza el StreetEdge
							((LocalGISStreetDynamicEdge)selectedEdge).setNominalMaxSpeed(
									dialog.getNominalMaxSpeed());
							// se actualiza el valor de la feature
							this.selectedStreetFeature.setAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.speedattributename"), dialog.getNominalMaxSpeed() * 3600/1000);
						}
						NetworkManager nManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
						Network selectedNetwork = null;
						Network actualNetwork = nManager.getNetwork(netName);
						// intentamos insercion en la base de datos de la velocidad de la vía
						Category categoryLayer = context.getLayerManager().getCategory(selectedLayer);
						if (!categoryLayer.getName().equals("")){
							if(actualNetwork.getGraph() instanceof DynamicGraph && ((DynamicGraph)actualNetwork.getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
							//if (categoryLayer.getName().equals("Dibujar")){
								/*BasicGraph bGraph = new BasicGraph();
								bGraph.getEdges().add(selectedEdge);
								
								
								SpatialAllInMemoryExternalSourceMemoryManager memoryManager = new SpatialAllInMemoryExternalSourceMemoryManager(new LocalGISStreetRouteReaderWriter(routeConnection));
								memoryManager.setGraph(bGraph);
								DynamicGraph dataBaseUpdateGraph = new DynamicGraph(memoryManager);
								
								memoryManager.appendGraph(dataBaseUpdateGraph);*/
								RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
								LocalGISNetworkDAO lnDAO = new LocalGISNetworkDAO();
								Connection connection = null;
								try{
									connection = routeConnection.getConnection();
									lnDAO.updateStreetData(netName, (LocalGISStreetDynamicEdge)selectedEdge, connection);
								}finally{
									ConnectionUtilities.closeConnection(connection);
								}
							}
						}
					}
				}
				
			}
		}
		this.dialog.dispose();
	}

	/**
	 * @param context
	 * @param netName
	 */
	private Edge searchEdgeByIdAndNetworkName(PlugInContext context,
			String netName) {
		Network selectedNetwork = null;
		Edge selectedEdge = null;
		selectedNetwork = ((NetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(this.context)).getNetwork(netName);
		if (selectedNetwork != null){
			Iterator<Edge> edges = selectedNetwork.getGraph().getEdges().iterator();
			Edge actualEdge = null;
			while(edges.hasNext()){
				actualEdge = edges.next();
				if (actualEdge.getID() == this.edgeId){
					selectedEdge = actualEdge;
				}
			}
		}
		return selectedEdge;
	}

	/**
	 * @param context
	 * @param netName
	 * @param selectednetwNetwork
	 * @return
	 */
	private Network serachNetworkByname(PlugInContext context,
			String netName) {
		Network finalNetwork = null;
		Iterator<Network> it = NetworkModuleUtilWorkbench.getNetworkManager(context).getNetworks().values().iterator();
		while (it.hasNext()){
			Network net = it.next();
			if (net.getName().equals(netName)){
				finalNetwork =  net;
			} else if (!net.getSubnetworks().isEmpty()){
				finalNetwork = searchForNetworkRecursive(net, netName);
			}
		}
		return finalNetwork;
	}
	
	private Network searchForNetworkRecursive(Network actualNetwork, String netName) {
		// TODO Auto-generated method stub
		Network resultado = null;
		if (actualNetwork.getSubnetworks().isEmpty()){
			if (actualNetwork.getName().equals(netName)){
				resultado = actualNetwork;
			}
		} else{
			Iterator<Network> iterator = actualNetwork.getSubnetworks().values().iterator();
			while (iterator.hasNext()){
				resultado=  searchForNetworkRecursive(iterator.next(), netName);
				if (resultado != null){
					return resultado;
				}
			}
		}
		return resultado;
	}
	
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("maxspeedplugin","routeengine.maxspeed.iconfile"));
	}
	
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.maxspeedplugin.language.RouteEngine_MaxSpeedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("maxspeedplugin",bundle);

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
		if (!maxSpeedModifierButtonAdded )
		{
			toolbox.addToolBar();
			MaxSpeedEditPlugIn explode = new MaxSpeedEditPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			maxSpeedModifierButtonAdded = true;
		}
	}

}
