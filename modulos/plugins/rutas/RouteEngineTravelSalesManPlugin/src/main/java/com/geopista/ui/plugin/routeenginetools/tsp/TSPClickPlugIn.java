/**
 * TSPClickPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.tsp;

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

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.tsp.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.tsp.utils.RouteEngineTSPDrawPointTool;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.ln.ExternalInfoRouteLN;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class TSPClickPlugIn extends AbstractPlugIn{

	
	private static HashMap<Integer,VirtualNodeInfo> nodesInfo = null;
	private NetworkManager networkMgr;
	private static FeatureCollection nodesFeatureCol;
	private static CalcRutaConfigFileReaderWriter configProperties;
	private static PlugInContext context;
	private static HashMap<String, Network> configuatorNetworks;
	private static Layer sourcePointLayer = null;

	private boolean tspClikButtonAdded = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	
	public boolean execute(PlugInContext context) throws Exception {
		if(context.getLayerViewPanel() == null)
			return false;
		this.networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		configProperties =new CalcRutaConfigFileReaderWriter();
		if(configProperties.getRedesNames() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptyconfiguration"));
			return false;
		}
		this.context = context;	
		nodesInfo = new HashMap<Integer,VirtualNodeInfo>();
		configuatorNetworks = new HashMap<String, Network>();
		
		nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);
		
		if (context.getLayerManager().getLayer("Puntos para TSP") != null){
			context.getLayerManager().remove(sourcePointLayer);
		}
		
		sourcePointLayer = context.addLayer("Puntos TSP",
				"Puntos para TSP",
				nodesFeatureCol);

//		LabelStyle labelStyle = new LabelStyle();
//		labelStyle.setAttribute("nodeId");
//		labelStyle.setColor(Color.black);
//		labelStyle.setScaling(false);
//		labelStyle.setEnabled(true);
//		sourcePointLayer.addStyle(labelStyle);


		if (networkMgr == null){
			context.getLayerViewPanel().getContext().warnUser("Error en el NetworkManager.");
			return false;
		}

		if (networkMgr.getNetworks().isEmpty()){
			context.getLayerViewPanel().getContext().warnUser("No hay redes cargadas en el NetworkManager");
			return false;
		}
		
		
		if (configProperties.getRedesNames().length <= 0){
			context.getLayerViewPanel().getContext().warnUser("Error en la configuracion. Inicie el configurador de rutas");
		}
		
		String redes[] = configProperties.getRedesNames();
		for(int i = 0; i < redes.length; i++){
			configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) networkMgr).getAllNetworks().get(redes[i]) );
		}
		for(int m = 0; m < configuatorNetworks.values().size(); m++){
			if (configuatorNetworks.values().toArray()[m] == null){
				context.getLayerViewPanel().getContext().warnUser("Error en la configuracion. Inicie el configurador de rutas");
				return false;
			}
		}
		
		
		ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
		//		toolbox.add();
		//		
		//		RouteEngineDrawPointTool.createCursor(IconLoader.icon("bandera.gif").getImage());
		context.getLayerViewPanel().setCurrentCursorTool(RouteEngineTSPDrawPointTool.create(toolbox.getContext()));
		
		return false;
	}
	/**
	 * @return the nodesInfo
	 */
	public static HashMap<Integer,VirtualNodeInfo> getNodesInfo() {
		return nodesInfo;
	}

	/**
	 * @param nodesInfo the nodesInfo to set
	 */
	public static void setNodesInfo(HashMap<Integer,VirtualNodeInfo> nodesInfo) {
		TSPClickPlugIn.nodesInfo = nodesInfo;
	}
	
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("tsp","routeengine.calcularruta.iconfileclick"));
	}
	
	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.tsp.language.RouteEngine_TSPi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("tsp",bundle);


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
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));

	}
	
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!tspClikButtonAdded  )
		{
//			toolbox.addToolBar();
			TSPClickPlugIn explode = new TSPClickPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			tspClikButtonAdded = true;
		}
	}
	


	public static UndoableCommand createAddCommand(Point point,
			RouteEngineTSPDrawPointTool routeEngineTSPDrawPointTool) {
		// TODO Auto-generated method stub
		final Point p = point;

		return new UndoableCommand(routeEngineTSPDrawPointTool.getName()) {
			public void execute() {
					addSourceToRoute(p, false);
			}

			public void unexecute() {

			}
		};
	}
	
	private static void addSourceToRoute(Point p, boolean b) {
		// TODO Auto-generated method stub
		String[] redes = configProperties.getRedesNames();
		GeometryFactory fact = new GeometryFactory();
		try {
			if (redes != null){

				NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);

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
					VirtualNodeInfo nodeInfo = null;
					try{
						nodeInfo = externalInfoRouteLN.getVirtualNodeInfo(new GeopistaRouteConnectionFactoryImpl(), 
								primitivePoint, 
								networkMgr, 
								redes[i], 
								100);
					}catch (Exception e){
						e.printStackTrace();
					}
					if (nodeInfo != null){
						virtualNodesInfo.add(nodeInfo);
					}
				}
				if(virtualNodesInfo.size() == 0)
					return;
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



				//				((LocalGISNetworkManager)networkMgr).addNewVirtualNode(networkMgr.getNetwork(selectedNodeInfo.getNetworkName()).getGraph()
				//						, selectedNodeInfo.getEdge()
				//						, selectedNodeInfo.getRatio()
				//						, this);
				nodesInfo.put(selectedNodeInfo.hashCode(),selectedNodeInfo);
				//				
				Coordinate coord = selectedNodeInfo.getLinestringVtoB().getCoordinateN(0);
				Point geom_nodes = fact.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				//				feature.setAttribute("nodeId", new Integer(node.getID()));
				feature.setAttribute("nodeId",selectedNodeInfo.hashCode());
				sourcePointLayer.getFeatureCollectionWrapper().add(feature);

			} else {

			}
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		   		
	}

}
