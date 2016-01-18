/**
 * TrafficRegulationEditPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.trafficregulationplugin;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.network.Network;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.trafficregulationplugin.images.IconLoader;
import com.localgis.route.network.LocalGISNetworkManager;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * @author javieraragon
 *
 */
public class TrafficRegulationEditPlugIn extends AbstractPlugIn{
	
	private TaskMonitor monitor = null;
	private PlugInContext context = null;
	private boolean trafficDirectionModifierButtonAdded = false ;
	private ILayerViewPanel panel = null;
	private ArrayList<Integer> edgesIdArrayList = null;
	private GeopistaFeature selectedStreetFeature = null;
	private ArrayList<GeopistaFeature> selectedArrayListStreetFeatures = null;
	private GeopistaLayer selectedLayer = null;
	private StreetTrafficRegulation trafficRegualtion = null;

	
//	public GeopistaFeature getSelectedStreetFeature(){
//		return selectedStreetFeature;
//	}
	
	@SuppressWarnings("unchecked")
	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;
		this.panel = context.getLayerViewPanel();
		
		// Sï¿½lo una feature puede estar seleccionada
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() < 1){
			panel.getContext().warnUser(I18N.get("regulacionTrafico","routeengine.trafficregulation.exactlyonefeature"));
			context.getLayerViewPanel().repaint();
			return false;
		}
		
		int edgeId = -1;
		this.selectedArrayListStreetFeatures = new ArrayList<GeopistaFeature>();
		this.edgesIdArrayList = new ArrayList<Integer>();
		GeopistaFeature feature = (GeopistaFeature) featuresselected.get(0);
		
		
		
		if (feature.getSchema().hasAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.trafficregulationattributename"))){
			
			if (feature.getSchema().hasAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.edgeidattributename"))){
				Object featureEdgeId = feature.getAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.edgeidattributename"));
				if ( featureEdgeId != null){
					edgeId = (Integer) featureEdgeId;
				}
			} else{
				// si no conseguimos el edgeId salimos y no continua el plugin
				context.getLayerViewPanel().repaint();
				return false;
			}
			
			this.selectedStreetFeature = feature;
			
			Collection<GeopistaLayer> layers = context.getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems();
			for (int i=0; i < layers.size(); i++){
				if (((GeopistaLayer)layers.toArray()[i]).getFeatureCollectionWrapper().getFeatures().contains(featuresselected.get(0))){
					this.selectedLayer  = (GeopistaLayer) layers.toArray()[i];
				}
			}
			
			Object featureTrafficRegulation = feature.getAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.trafficregulationattributename"));
			if (featureTrafficRegulation != null){
				if (!featureTrafficRegulation.equals("")){
					if (featureTrafficRegulation.equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()) ||
							featureTrafficRegulation.equals(StreetTrafficRegulation.BIDIRECTIONAL)){
						this.trafficRegualtion = StreetTrafficRegulation.BIDIRECTIONAL;
					} else if (featureTrafficRegulation.equals(StreetTrafficRegulation.DIRECT.toString()) ||
							featureTrafficRegulation.equals(StreetTrafficRegulation.DIRECT)){
						this.trafficRegualtion = StreetTrafficRegulation.DIRECT;
					} else if (featureTrafficRegulation.equals(StreetTrafficRegulation.INVERSE.toString()) ||
							featureTrafficRegulation.equals(StreetTrafficRegulation.INVERSE)){
						this.trafficRegualtion = StreetTrafficRegulation.INVERSE;
					} else{
						this.trafficRegualtion = StreetTrafficRegulation.FORBIDDEN;
					}
				} else{
					context.getLayerViewPanel().repaint();
					return false;
				}
				
			}
			
			
			
			
			

			if (edgeId != -1){
				
				selectedArrayListStreetFeatures.add(feature);
				edgesIdArrayList.add(edgeId);
				
				for(int i = 1; i < featuresselected.size(); i++ ){
					GeopistaFeature actualFeature = (GeopistaFeature) featuresselected.get(i);
					if (actualFeature.getSchema().hasAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.trafficregulationattributename"))){
						if (actualFeature.getSchema().hasAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.edgeidattributename"))){
							Object featureEdgeId = actualFeature.getAttribute(I18N.get("regulacionTrafico","routeengine.trafficregulation.edgeidattributename"));
							if ( featureEdgeId != null && featureEdgeId instanceof Integer){
								if (((Integer)featureEdgeId) > 0){
									selectedArrayListStreetFeatures.add(actualFeature);
									edgesIdArrayList.add((Integer) featureEdgeId);
								}
							}
						}
					}
				}
				
				// Comprobamos que el id de tramo sea distinto de -1 (valor por defecto)
				if (this.selectedLayer != null){
					// Comprobamos que haya una layer (no sea null)
					if (this.selectedStreetFeature != null){
						// Comprobamos que la feature seleccionanda no sea null
							final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					    	final PlugInContext runContext = this.context;
					    	progressDialog.setTitle("TaskMonitorDialog.Wait");
					        progressDialog.report(I18N.get("regulacionTrafico","routeengine.trafficregulation.trafficregulationtaskdialog"));
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
					    	context.getLayerViewPanel().repaint();
					    	return false;
					}
				}
			}

		} else{
			panel.getContext().warnUser(I18N.get("regulacionTrafico","routeengine.trafficregulation.nonstreetnetworkerrormessage"));
		}
		if(getSelectedLayer() != null)
			getSelectedLayer().fireAppearanceChanged();
		return false;
		
		
	}
	
	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
			run(monitor, context);
			context.getActiveInternalFrame().repaint();
			this.getSelectedLayer().fireAppearanceChanged();
	}

	
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		this.monitor = monitor;
		panel.getContext().warnUser(this.getClass().getName());
		monitor.report(I18N.get("regulacionTrafico","routeengine.trafficregulation.savingdatainfomessage"));
		
//		if (edgeId != -1){
			// Comprobamos que el id de tramo sea distinto de -1 (valor por defecto)
			if (this.selectedLayer != null){
				// Comprobamos que haya una layer (no sea null)
				if (this.selectedStreetFeature != null){
					// Comprobamos que la feature seleccionanda no sea null
					if (selectedLayer.getName().startsWith("Arcos-")){
						
//						String netName = selectedLayer.getName().split("-")[1];
//	
//						Edge selectedEdge = searchEdgeByIdAndNetworkName(context, netName);
//						
//						if (selectedEdge instanceof LocalGISStreetDynamicEdge){
//							//Se actualiza el StreetEdge
//							((LocalGISStreetDynamicEdge)selectedEdge).setNominalMaxSpeed(
//									dialog.getNominalMaxSpeed());
//							// se actualiza el valor de la feature
//							this.selectedStreetFeature.setAttribute(I18N.get("maxspeedplugin","routeengine.maxspeed.speedattributename"), dialog.getNominalMaxSpeed());
//						}
//						
//						Network selectedNetwork = null;
//
//						// intentamos insercion en la base de datos de la velocidad de la vï¿½a
//						Category categoryLayer = context.getLayerManager().getCategory(selectedLayer);
//						if (!categoryLayer.getName().equals("")){
//							if (categoryLayer.getName().equals("Red de BBDD")){
//								BasicGraph bGraph = new BasicGraph();
//								bGraph.getEdges().add(selectedEdge);
//								
//								RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
//								SpatialAllInMemoryExternalSourceMemoryManager memoryManager = new SpatialAllInMemoryExternalSourceMemoryManager(new LocalGISStreetRouteReaderWriter(routeConnection));
//								memoryManager.setGraph(bGraph);
//								DynamicGraph dataBaseUpdateGraph = new DynamicGraph(memoryManager);
//								
//								memoryManager.appendGraph(dataBaseUpdateGraph);
//							}
//						}
					}
				}
				
			}
		}
//		this.dialog.dispose();
//	}

	/**
	 * @param context
	 * @param netName
	 */
	@SuppressWarnings("unchecked")
	public Edge searchEdgeByIdAndNetworkName(PlugInContext context,
			String netName, int idEdge) {
		Network selectedNetwork = null;
		Edge selectedEdge = null;
		selectedNetwork = ((LocalGISNetworkManager)NetworkModuleUtilWorkbench.getNetworkManager(context)).getNetwork(netName);
		if (selectedNetwork != null){
			Iterator<Node> nodes = selectedNetwork.getGraph().getNodes().iterator();
			Node actualNode = null;
			while(nodes.hasNext()){
				actualNode = nodes.next();
				if (actualNode!=null){
					Iterator<Graphable> iterNodesGrapahbles  = actualNode.getEdges().iterator();
					while(iterNodesGrapahbles.hasNext()){
						Graphable graphable = iterNodesGrapahbles.next(); 
						if (graphable instanceof Edge && graphable.getID() == idEdge){
							Edge edge = (Edge) graphable;
							selectedEdge = edge;
						}
					}
				}
			}
		}
		return selectedEdge;
	}
	
	public ImageIcon getIcon() {
		return IconLoader.icon("");
	}
	
	@SuppressWarnings("unchecked")
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.trafficregulationplugin.language.RouteEngine_TrafficRegulationi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("regulacionTrafico",bundle);

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
		if (!trafficDirectionModifierButtonAdded )
		{
			toolbox.addToolBar();
			TrafficRegulationEditPlugIn explode = new TrafficRegulationEditPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			trafficDirectionModifierButtonAdded = true;
		}
	}
	
	public void setMonitor(TaskMonitor taskMonitor){
		this.monitor = taskMonitor;
	}
	
	public TaskMonitor getMonitor(){
		return this.monitor;
	}
	
	public void setLayerViewPanel(LayerViewPanel layerViewPanel){
		this.panel =layerViewPanel;
	}
	
	public ILayerViewPanel getLayerViewPanel(){
		return this.panel;
	}
	
	public ArrayList<GeopistaFeature> getSelectedsArrayListStreetFeature() {
		return selectedArrayListStreetFeatures;
	}
	
	public ArrayList<Integer> getSelectedsArrayListEdgesIds() {
		return edgesIdArrayList;
	}
	

	public void setSelectedStreetFeature(GeopistaFeature selectedStreetFeature) {
		this.selectedStreetFeature = selectedStreetFeature;
	}

	public GeopistaLayer getSelectedLayer() {
		return selectedLayer;
	}

	public void setSelectedLayer(GeopistaLayer selectedLayer) {
		this.selectedLayer = selectedLayer;
	}

	public StreetTrafficRegulation getTrafficRegualtion() {
		return trafficRegualtion;
	}

	public void setTrafficRegualtion(StreetTrafficRegulation trafficRegualtion) {
		this.trafficRegualtion = trafficRegualtion;
	}

}
