/**
 * NetworkLinksPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.networklinks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.networklinks.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class NetworkLinksPlugin extends ThreadedBasePlugIn{
	
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	private boolean buttonAdded;
	
	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.networklinks.language.RouteEngine_NetworkLinksi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("networkLinks",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}
	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("networkLinks","routeengine.networklinks.iconfile"));
	}
	
	public String getName(){
    	String name = I18N.get("networkLinks","routeengine.networklinks.plugintitle");
    	return name;
    }
	public boolean execute(PlugInContext context) throws Exception{
		if(context.getLayerViewPanel() == null){
			return false;
		}
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() != 2){
			System.out.println("prueba");
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.exactly2features"));
			return false;
		}
		
		Feature nodeFeatureStart = (Feature) featuresselected.get(0);
		Feature nodeFeatureEnd = (Feature) featuresselected.get(1);
		String networkNameStart = "";
		String idNodeStart = "";
		String networkNameEnd = "";
		String idNodeEnd = "";
		try{
			//Comprobar que los dos son
			networkNameStart = nodeFeatureStart.getString("networkName");
			networkNameEnd = nodeFeatureEnd.getString("networkName");
			idNodeStart = nodeFeatureStart.getString("nodeId");
			idNodeEnd = nodeFeatureEnd.getString("nodeId");
			if(networkNameStart.equals(networkNameEnd)){
				context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.samenetwork"));
				return false;
			}
		}catch (Exception e){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.notrecognizeddata"));
			return false;
			//throw new Exception("Not recognized Data. Select two painted network nodes.");
		}
		
		
		LocalGISNetworkManager nManager = (LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network networkStart = nManager.getNetwork(networkNameStart);
		DynamicGraph graphStart = null;
		if(networkStart == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.networknotloaded"));
			return false;
		}
		if(networkStart.getGraph() instanceof BasicGraph){
			AllInMemoryManager manager = new AllInMemoryManager();
			manager.setGraph(networkStart.getGraph());
			graphStart = new DynamicGraph(manager);
		}else{
			graphStart = (DynamicGraph)networkStart.getGraph();
		}
		Node nodeStart = graphStart.getNode(Integer.parseInt(idNodeStart));
		if(nodeStart == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.nodenotfound"));
			return false;
		}
		Network networkEnd = nManager.getNetwork(networkNameEnd);
		if(networkEnd == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.networknotloaded"));
			return false;
		}
		DynamicGraph graphEnd = null;
		if(networkEnd.getGraph() instanceof BasicGraph){
			AllInMemoryManager manager = new AllInMemoryManager();
			manager.setGraph(networkEnd.getGraph());
			graphEnd = new DynamicGraph(manager);
		}else{
			graphEnd = (DynamicGraph)networkEnd.getGraph();
		}
		Node nodeEnd = graphEnd.getNode(Integer.parseInt(idNodeEnd));
		if(nodeEnd == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.nodenotfound"));
			return false;
		}
		NetworkLink networkLink = new NetworkLink(networkStart,nodeStart,networkEnd,nodeEnd, new SequenceUIDGenerator());
		RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
		Connection connection = null;
		boolean isInserted;
		try{
			connection = connectionFactory.getConnection();
			isInserted = nManager.addNetworkLink(networkStart, connection, networkLink);
		}finally{
			ConnectionUtilities.closeConnection(connection);
		}
		if(isInserted)
			paintNetworkLinks(networkLink,context);
		else{
			//throw new Exception("Network Link Already Inserted");
			context.getLayerViewPanel().getContext().warnUser(I18N.get("networkLinks","routeengine.networklinks.alreadyinserted"));
			return false;
		}
		return true;
	}
	public void addButton(final ToolboxDialog toolbox)
	{
		if (!buttonAdded )
		{
//			toolbox.addToolBar();
			NetworkLinksPlugin explode = new NetworkLinksPlugin();                 
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
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))
		.add(checkFactory.createExactlyNFeaturesMustHaveSelectedItemsCheck(2));
	}
	
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		
		
		
        
	}
	public void paintNetworkLinks(NetworkLink nk,PlugInContext context) {
		
		FeatureCollection edgesFeatureCol = null;
		Layer edgesLayer = context.getLayerManager().getLayer(I18N.get("networkLinks","routeengine.networklinks.layer"));
		if(edgesLayer == null){
			edgesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
			edgesFeatureCol.getFeatureSchema().addAttribute("idEje",AttributeType.INTEGER);
			edgesFeatureCol.getFeatureSchema().addAttribute("idNodoA",AttributeType.INTEGER);
			edgesFeatureCol.getFeatureSchema().addAttribute("idNodoB",AttributeType.INTEGER);
			edgesFeatureCol.getFeatureSchema().addAttribute("networkNameA",AttributeType.STRING);
			edgesFeatureCol.getFeatureSchema().addAttribute("networkNameB",AttributeType.STRING);
			edgesLayer = context.addLayer(
					I18N.get("networkLinks","routeengine.networklinks.category"),
					I18N.get("networkLinks","routeengine.networklinks.layer"),
					edgesFeatureCol);
			BasicStyle bs = new BasicStyle();
			bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
			bs.setLineWidth(6);
			bs.setEnabled(true);
			edgesLayer.addStyle(bs);
			
		}
			
		
		
		GeometryFactory fact1 = new GeometryFactory();
		
		Feature link = new BasicFeature(edgesLayer.getFeatureCollectionWrapper().getFeatureSchema());
		Coordinate[] coords = new Coordinate[]{((XYNode)nk.getNodeA()).getCoordinate(),((XYNode)nk.getNodeB()).getCoordinate()};
		LineString ls = (LineString) fact1.createLineString(coords);

		link.setGeometry(ls);
		link.setAttribute("idEje", new Integer(nk.getID()));
		link.setAttribute("idNodoA", new Integer(nk.getNodeA().getID()));
		link.setAttribute("idNodoB", new Integer(nk.getNodeB().getID()));
		link.setAttribute("networkNameA", nk.getNetworkA().getName());
		link.setAttribute("networkNameB", nk.getNetworkB().getName());

		//edgesFeatureCol.add(link);
		edgesLayer.getFeatureCollectionWrapper().add(link);
	}
}
