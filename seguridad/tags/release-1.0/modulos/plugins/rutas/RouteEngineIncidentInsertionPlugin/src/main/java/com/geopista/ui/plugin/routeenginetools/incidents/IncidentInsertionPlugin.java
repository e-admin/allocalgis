package com.geopista.ui.plugin.routeenginetools.incidents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import java.util.Set;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.incidents.dialogs.IncidentInsertionFactoryAttachIncidentsDialog;
import com.geopista.ui.plugin.routeenginetools.incidents.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class IncidentInsertionPlugin extends ThreadedBasePlugIn{
	
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean incidentInsertionButtonAdded = false;
	
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.incidents.language.RouteEngine_Incidentsi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("networkIncidents",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("networkIncidents","routeengine.incidents.iconfile"));
	}
	
	public String getName(){
    	String name = I18N.get("networkIncidents","routeengine.incidents.plugintitle");
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
	
	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() != 1){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkIncidents","routeengine.incidents.exactlyonefeature"));
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
					context.getLayerViewPanel().getContext().warnUser(I18N.get("networkIncidents","routeengine.incidents.layererror"));
					return false;
					
				}
			}
			if(networkName == null || networkName.equals("") || idEdge == null || idEdge.equals("")){
				context.getLayerViewPanel().getContext().warnUser(I18N.get("temporalIncidents","routeengine.temporalincidents.add.error.general"));
				return false;
			}
		}
		
		
		NetworkManager nManager = FuncionesAuxiliares.getNetworkManager(context);
		Network network = nManager.getNetwork(networkName);
		// en caso de que esté en raiz y contenga el grafo
		if(network == null || network.getGraph() == null){
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
		}
		if(network == null)
			//network no encontrada. No está cargada la red.
		;
		
		
		ILocalGISEdge edgeToModify = null;
		//Buscamos el edge actual. (Habría que actualizarlo)
		if(network.getGraph() instanceof BasicGraph){
			// Si es un tipo BasicGraph, solo hay que modificarlo al vuelo,
			ArrayList edges = new ArrayList(network.getGraph().getEdges());
			Iterator<Edge> it = edges.iterator();
			
			while(it.hasNext() && edgeToModify == null){
				Edge actualEdge = it.next();
				if(actualEdge.getID() == Integer.parseInt(idEdge))
					edgeToModify = (ILocalGISEdge)actualEdge;
			}
		}
		if(network.getGraph() instanceof DynamicGraph){
			// Es un tipo dinamico. Se actualiza automaticamente por medio 
			DynamicGraph graph = (DynamicGraph)network.getGraph();
			if(graph.getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager){
				if(((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).getStore() instanceof LocalGISRouteReaderWriter){
					edgeToModify = (ILocalGISEdge)((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).getEdge(Integer.parseInt(idEdge));
					//((BasicExternalSourceMemoryManager)graph.getMemoryManager()).commit();
				}
			}else{
				edgeToModify = (ILocalGISEdge)graph.getEdge(Integer.parseInt(idEdge));
				/*ArrayList edges = new ArrayList(network.getGraph().getEdges());
				Iterator<Edge> it = edges.iterator();
				
				while(it.hasNext() && edgeToModify == null){
					Edge actualEdge = it.next();
					if(actualEdge.getID() == Integer.parseInt(idEdge))
						edgeToModify = (ILocalGISEdge)actualEdge;
				}*/
			}
		}
		Set keys = null;
		if(edgeToModify instanceof LocalGISDynamicEdge)
			keys = ((LocalGISDynamicEdge)edgeToModify).getIncidents();
		else
			keys = ((LocalGISStreetDynamicEdge)edgeToModify).getIncidents();
		Iterator<LocalGISIncident> it  = keys.iterator();
		ArrayList<LocalGISIncident> incidentList = new ArrayList<LocalGISIncident>();
		while (it.hasNext()){
			incidentList.add(it.next());
		}
        reportNothingToUndoYet(context);
        WizardDialog dialog = new WizardDialog(GeopistaUtil.getFrame(context.getWorkbenchGuiComponent()),
        /*I18N.get("networkFusion","routeengine.networkfusion.wizardDialog")*/"", context.getErrorHandler());
        dialog.init(new WizardPanel[] {new IncidentInsertionFactoryAttachIncidentsDialog(incidentList,(Network)network,edgeToModify)});
        dialog.setSize(650,550);
        dialog.getContentPane().remove(dialog.getInstructionTextArea());
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        
        return false;
	}
	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!incidentInsertionButtonAdded  )
		{
//			toolbox.addToolBar();
			IncidentInsertionPlugin explode = new IncidentInsertionPlugin();			
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			incidentInsertionButtonAdded  = true;
		}
	}
}
