/**
 * CalcularRutaDragClickPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog.MoverPointTool;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog.MoverPointTool.SegmentContext;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.ln.ExternalInfoRouteLN;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.weighter.LocalGISWeighter;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.feature.BasicFeature;

public class CalcularRutaDragClickPlugIn extends CalcularRutaClick{

	public static final double NEAR_SEARCH_TOLERANCE = 5;
	private boolean calculateRouteDragButtonAdded = false;
	private static NetworkManager networkMgr;
	private static CalcRutaConfigFileReaderWriter configProperties;
	private static PlugInContext context;
	private static Layer routePoints;
	private static Layer edgesSelectedLayer = null;
	private static Layer nodesSelectedLayer = null;
	private static Geometry lastNodeGeometry;
	private static Geometry firstNodeGeometry;
	private static HashMap<String, Network> configuatorNetworks;	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	static LocalGISWeighter weighter;

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.language.RouteEngine_CalcularRutai18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("calcruta",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);

	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!calculateRouteDragButtonAdded)
		{
			//			toolbox.addToolBar();
			CalcularRutaDragClickPlugIn explode = new CalcularRutaDragClickPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			calculateRouteDragButtonAdded = true;
		}
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("calcruta","routeengine.calcularruta.iconfiledragclick"));
	}



	public boolean execute(PlugInContext ctxt) throws Exception {
		networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(ctxt);
		configProperties =new CalcRutaConfigFileReaderWriter();
		if(configProperties.getRedesNames() == null){
			ctxt.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptyconfiguration"));
			return false;
		}
		configuatorNetworks = new HashMap<String, Network>();
		context = ctxt;	
		//		puntosRuta = new ArrayList<VirtualNodeInfo>();
		//		configuatorNetworks = new HashMap<String, Network>();


		if (networkMgr == null){
			ctxt.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.networkmanageterror"));
			return false;
		}

		if (networkMgr.getNetworks().isEmpty()){
			ctxt.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.nonetworksloaded"));
			return false;
		}

		Layer[] layersSelected = ctxt.getLayerNamePanel().getSelectedLayers();
		for (int i = 0; i < layersSelected.length; i++){
			if (layersSelected[i].getName().startsWith("Arcos Camino")){
				edgesSelectedLayer = layersSelected[i];
			}
		}

		if (edgesSelectedLayer == null){
			warnUserMessage("Seleccione una feature de una capa de Caminos de la ruta que desea modificar.");
			return false;
		}


		String a[] = edgesSelectedLayer.getName().split("Arcos Camino");
		if (a.length > 1){
			layerNameToPaint = a[1];
			if (a[1].equals(" ") || a[1].equals("")){
				nodesSelectedLayer  = ctxt.getLayerManager().getLayer("Nodos Camino");
				
			} else {
				for (Iterator<Layer> i = ctxt.getLayerManager().iterator(); i.hasNext();) {
					Layer layer = i.next();
					if (layer.getName().equals("Nodos Camino" + a[1])) {
						nodesSelectedLayer = layer;
					}
					if (layer.getName().equals("Puntos de ruta"+ a[1])){//Mal. No se sabe todavia el elemento seleccionado.
						routePoints =  ctxt.getLayerManager().getLayer("Puntos de ruta"+ a[1]);
						firstNodeGeometry = (Point)((BasicFeature)layer.getFeatureCollectionWrapper().getFeatures().get(0)).getAttribute(0);
						lastNodeGeometry = (Point)((BasicFeature)layer.getFeatureCollectionWrapper().getFeatures().get(1)).getAttribute(0);
					}
				}
				if(routePoints == null)findRoutePoints(a[1]);
			}
		} else{
			layerNameToPaint = "";
			routePoints =  ctxt.getLayerManager().getLayer("Puntos de ruta");
			nodesSelectedLayer  = ctxt.getLayerManager().getLayer("Nodos Camino");
			firstNodeGeometry = (Point)((BasicFeature)ctxt.getLayerManager().getLayer("Puntos de ruta").getFeatureCollectionWrapper().getFeatures().get(0)).getAttribute(0);
			lastNodeGeometry = (Point)((BasicFeature)ctxt.getLayerManager().getLayer("Puntos de ruta").getFeatureCollectionWrapper().getFeatures().get(1)).getAttribute(0);
		
		}

		if (nodesSelectedLayer == null){
			warnUserMessage(ctxt, "No se ha encontrado la capa con los nodos de la ruta");
		}

		// Seleccionar la ruta
		ctxt.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(
				edgesSelectedLayer, 
				edgesSelectedLayer.getFeatureCollectionWrapper().getFeatures());

		/*firstNodeGeometry = ((Feature)nodesSelectedLayer.getFeatureCollectionWrapper().getFeatures().get(
				nodesSelectedLayer.getFeatureCollectionWrapper().getFeatures().size() -1)).getGeometry();
		lastNodeGeometry = ((Feature)nodesSelectedLayer.getFeatureCollectionWrapper().getFeatures().get(0)).getGeometry();*/

		ToolboxDialog toolbox = new ToolboxDialog(ctxt.getWorkbenchContext());
		EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
		ctxt.getLayerViewPanel().setCurrentCursorTool(new MoverPointTool(checkFactory));

		return false;

	}

	private void findRoutePoints(String value) {
		if(value == "")
			return;
		Integer id = new Integer(value.substring(value.indexOf("(")+1,value.indexOf(")")));
		Integer newId = id-1;
		if(newId == 0)
			value = "";
		else
			value = value.replaceAll(id.toString(),newId.toString());
		for (Iterator<Layer> i = context.getLayerManager().iterator(); i.hasNext();) {
			Layer layer = i.next();
			if (layer.getName().equals("Puntos de ruta"+ value)){
				routePoints =  context.getLayerManager().getLayer("Puntos de ruta"+ value);
			}
		}
		if(routePoints == null)
			findRoutePoints(value);
		
	}

	private void warnUserMessage(PlugInContext context, String message){
		context.getLayerViewPanel().getContext().warnUser(message);
	}

	private void warnUserMessage(String message){
		if (context != null){
			context.getLayerViewPanel().getContext().warnUser(message);
		}
	}

	
	private static TaskMonitorDialog progressDialog = null;
	
	public static UndoableCommand createAddCommand(Point point,MoverPointTool tool) {
		final Point p = point;
		final SegmentContext segment = tool.getSelectedSegment();
		return new UndoableCommand(tool.getName()) {
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		    	progressDialog.setTitle("TaskMonitorDialog.Wait");
		        progressDialog.report(I18N.get("calcruta","routeengine.calcularruta.taskdialogmessage"));
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
		    						CalculateRouteTwoLastNodes(p,segment);
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
			}

			@Override
			public void unexecute() {
				// TODO Auto-generated method stub
			}
		};
	}

	protected static void CalculateRouteTwoLastNodes(Point middlePoint, SegmentContext segment) {
		try {
			Integer routeSelected = (Integer)segment.getFeature().getAttribute("routeId");
			isFirst = true;
			firstNodeGeometry = getNodeGeometry(routeSelected);
			lastNodeGeometry = getNodeGeometry(routeSelected + 1);
			puntosRuta = new ArrayList<VirtualNodeInfo>();
			addSourceToRoute((Point)firstNodeGeometry,false, false, routeSelected);
			addSourceToRoute(middlePoint,true, false, routeSelected);
			CalculateRouteTwoLastNodes(routeSelected);
			isFirst = false;
			puntosRuta = new ArrayList<VirtualNodeInfo>();
			addSourceToRoute(middlePoint,false, true, routeSelected);
			addSourceToRoute((Point)lastNodeGeometry,true, false, routeSelected);
			CalculateRouteTwoLastNodes(routeSelected+1);
			//updateNodesRouteSelected(middlePoint,routeSelected);
		} catch (ElementNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void updateNodesRouteSelected(Point middlePoint,Integer routeSelected) {
		ArrayList<Feature> features = new ArrayList<Feature>(routePoints.getFeatureCollectionWrapper().getFeatures());
		Iterator<Feature> it = features.iterator();
		while(it.hasNext()){
			Feature actFeat = it.next();
			Integer nodeRouteId = (Integer)actFeat.getAttribute("routeId");
			if(!it.hasNext())
				actFeat.setAttribute("routeId",nodeRouteId+1);
		}
	}

	private static Geometry getNodeGeometry(Integer routeSelected) {
		ArrayList<Feature> features = new ArrayList<Feature>(routePoints.getFeatureCollectionWrapper().getFeatures());
		Iterator<Feature> it = features.iterator();
		while(it.hasNext()){
			Feature actFeat = it.next();
			Integer nodeRouteId = (Integer)actFeat.getAttribute("routeId");
			if(nodeRouteId.equals(routeSelected))
				return (Geometry)actFeat.getAttribute(0);
		}
		return null;
	}

	private static VirtualNodeInfo getVirtualnodeInfo(Point p) {
		String[] redes = configProperties.getRedesNames();
		VirtualNodeInfo nodeInfo = null;
		try {
			if (redes != null){

				NetworkManager networkMgr = NetworkModuleUtilWorkbench
				.getNetworkManager(context);

				CoordinateSystem coordSys =context.getLayerManager().getCoordinateSystem();				
				if (coordSys != null){
					p.setSRID(coordSys.getEPSGCode());
				}
				CoordinateReferenceSystem crs = CRS.decode("EPSG:"+coordSys.getEPSGCode());
				org.opengis.geometry.primitive.Point primitivePoint = GeographicNodeUtil.createISOPoint(p,crs);

				ExternalInfoRouteLN externalInfoRouteLN = new ExternalInfoRouteLN();
				ArrayList<VirtualNodeInfo> virtualNodesInfo = new ArrayList<VirtualNodeInfo>();
				for(int i = 0; i < redes.length; i++){

					configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) networkMgr).getAllNetworks().get(redes[i]) );
					nodeInfo = externalInfoRouteLN.getVirtualNodeInfo(new GeopistaRouteConnectionFactoryImpl(), 
							primitivePoint, 
							networkMgr, 
							redes[i], 
							NEAR_SEARCH_TOLERANCE);
					if (nodeInfo != null){
						virtualNodesInfo.add(nodeInfo);
					}
				}
				Iterator<VirtualNodeInfo> it = virtualNodesInfo.iterator();
				double lastDistante = -1;
				VirtualNodeInfo selectedNodeInfo = null;
				while(it.hasNext()){
					VirtualNodeInfo vNodeinfo = it.next();
					if (lastDistante == -1 || lastDistante > vNodeinfo.getDistance()){
						selectedNodeInfo = vNodeinfo;
						lastDistante = vNodeinfo.getDistance();
					}
				}
				nodeInfo = selectedNodeInfo;

			} else {

			}
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		   		
		return nodeInfo;
	}
}
