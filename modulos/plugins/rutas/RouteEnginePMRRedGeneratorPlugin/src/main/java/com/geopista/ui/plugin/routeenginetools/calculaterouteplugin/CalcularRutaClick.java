/**
 * CalcularRutaClick.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.path.PathNotFoundException;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.proxy.ProxyNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.geopista.ui.plugin.routeenginetools.CombinedSchemaCalculator;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog.RouteEngineDrawPointTool;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.dibujarredplugin.DibujarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.WriteRoutePathInfoWithDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.WriteRoutePathInformationUtil;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.ln.ExternalInfoRouteLN;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.weighter.LocalGISPMRWeighter;
import com.localgis.route.weighter.LocalGISStreetWeighter;
import com.localgis.route.weighter.LocalGISWeighter;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.IdEdgeNetworkBean;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * 
 * @author javier aragon
 * 
 */
public class CalcularRutaClick extends ThreadedBasePlugIn {

	private boolean calculateRouteButtonAdded = false;	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static java.awt.List listaDePuntos;
	protected static ArrayList<VirtualNodeInfo> puntosRuta = null;

	static PlugInContext context;

	int numNodosVisitados = 0;
	static NetworkManager networkMgr;
	private int templeado;
	private ToolboxDialog toolbox;
	protected static String layerNameToPaint;
	protected static boolean isFirst;
	private static Node source, end;
	private static Layer sourcePointLayer = null;
	private static Layer destinationPointLayer = null;
	private static FeatureCollection nodesFeatureCol = null;
	private static HashMap<String, Network> configuatorNetworks = null;

	private static CalcRutaConfigFileReaderWriter configProperties = null;


	public boolean execute(PlugInContext context) throws Exception {
		layerNameToPaint = null;
		if(context.getLayerViewPanel() == null)
			return false;
		this.networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		configProperties =new CalcRutaConfigFileReaderWriter();
		if(configProperties.getRedesNames() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptyconfiguration"));
			return false;
		}
		this.context = context;	
		puntosRuta = new ArrayList<VirtualNodeInfo>();
		configuatorNetworks = new HashMap<String, Network>();


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


		//ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
		EnableCheckFactory checkFactory = new EnableCheckFactory(toolbox.getContext());
		context.getLayerViewPanel().setCurrentCursorTool(RouteEngineDrawPointTool.create(toolbox.getContext()));
//		context.getWorkbenchGuiComponent().getToolBar().updateEnabledState();

		nodesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",AttributeType.INTEGER);
		//Almacena el id de la ruta. Solo el del nodo inicial. Si es final y no es inicial de ninguno, no habra siguiente nodo.
		nodesFeatureCol.getFeatureSchema().addAttribute("routeId",AttributeType.INTEGER);
		//nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",AttributeType.INTEGER);

		// Cambiado a un solo modelo. Los puntos de ruta estan ordenados segun el calculo de ruta apropiado
		sourcePointLayer = context.addLayer("PuntosRuta","Puntos de ruta",nodesFeatureCol);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.setAttribute("routeId");
		labelStyle.setColor(Color.black);		
		labelStyle.setScaling(false);
		labelStyle.setEnabled(true);
		sourcePointLayer.addStyle(labelStyle);
		destinationPointLayer = context.addLayer("PuntosRuta",
				"Puntos de destino",
				nodesFeatureCol);

		destinationPointLayer.addStyle(labelStyle);

		return false;

	}

	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {

		run(monitor, context);
	}

	/**
	 * @param context
	 * @param subred
	 * @param Algoritmo
	 * @param path
	 * @param coste_ruta
	 */
	private static void generarInformeRuta(PlugInContext context,
			Path path, double coste_ruta) {
		progressDialog.report(I18N.get("calcruta","routeengine.calcularruta.reportmessage.routeinformation"));
		if(context.getWorkbenchFrame()!=null){
			context.getWorkbenchFrame().getOutputFrame().addText(I18N.get("calcruta","routeengine.calcularruta.reportmessage.titlelabel"));
			WriteRoutePathInformationUtil.writeRoutePathInformation(context.getWorkbenchFrame().getOutputFrame(), path.getEdges(), path, context,					startNodeInfo, endNodeinfo);
		}
		
		
		if(context.getWorkbenchFrame()!=null && layerNameToPaint == null)
			WriteRoutePathInfoWithDialog.showInfoRouteDialog(path, startNodeInfo, endNodeinfo, context);

	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.language.RouteEngine_CalcularRutai18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("calcruta",bundle);

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

		;

	}



	@SuppressWarnings("unchecked")
	public static void dibujarCamino(String algoritmo, Path p,double coste_ruta,Integer numRuta) {
		int lastPosition = 0;
		
		
		FeatureCollection edgesFeatureCol = null;
		//				.createBlankFeatureCollection();
		FeatureCollection nodesFeatureCol = null;
		if(layerNameToPaint != null)
		    {
			// TODO:Comprobar si devuelve todas las features de la capa o no.
			
			nodesFeatureCol = getLayerToPaint(context.getLayerManager().getLayers(),"Nodos Camino"+layerNameToPaint).getFeatureCollectionWrapper().getWrappee();
			edgesFeatureCol = getLayerToPaint(context.getLayerManager().getLayers(),"Arcos Camino"+layerNameToPaint).getFeatureCollectionWrapper().getWrappee();
			if(isFirst){// Si es el primero de las dos rutas, elimina todas las features de la capa anteriores.
				refreshActualRouteData(edgesFeatureCol,nodesFeatureCol,numRuta);
				
			}
			if(edgesFeatureCol.getFeatures().size()-1>0)
				lastPosition = edgesFeatureCol.getFeatures().size()-1;
		}else{
			nodesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
			nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",AttributeType.INTEGER);
			nodesFeatureCol.getFeatureSchema().addAttribute("routeId",AttributeType.INTEGER);
		}
		if(numRuta == null){
			if(sourcePointLayer.getFeatureCollectionWrapper().getFeatures().size() == 2)
				numRuta = 1;
			else{
				numRuta = getMaxRouteId(sourcePointLayer.getFeatureCollectionWrapper().getFeatures())-1;
			}
		}
		GeometryFactory fact1 = new GeometryFactory();
		List<Object> edges_camino = p.getEdges();
		GeopistaLayer layer = null;

		for (Iterator<Node> iter_nodes = ((RoutePath)p).iterator(); iter_nodes.hasNext();) {
			Node node = (Node) iter_nodes.next();
			if(node instanceof ProxyNode)
				node = (Node)NodeUtils.unwrapProxies(node);
			if(!(node instanceof VirtualNode)){
				Coordinate coord = ((XYNode) node).getCoordinate();
				Point geom_nodes = fact1.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol
						.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				feature.setAttribute("nodeId", new Integer(node.getID()));
				feature.setAttribute("routeId", numRuta);
				nodesFeatureCol.add(feature);
			}
		}

	// creo capas con los arcos
	Collections.reverse(edges_camino);
	int pos_inicio = 0, pos_fin = 1, tramo = edges_camino.size();
	int lastIdLayer = -1;
	Feature startFeature = null;
	CombinedSchemaCalculator schemaCalc = new CombinedSchemaCalculator();
// Obtengo los elementos interiores
	List<Edge> descripcionCamino=new ArrayList(edges_camino);
	descripcionCamino.remove(0);
	descripcionCamino.remove(descripcionCamino.size()-1);
	
//	Obtiene representacion de los edges interiores
	DibujarRedPlugIn delegate = new DibujarRedPlugIn();
	edgesFeatureCol=schemaCalc.getUpdatedFeatureSchema(edgesFeatureCol, null);
	schemaCalc.addAttributeIfNotPresent(edgesFeatureCol, "routeId", AttributeType.INTEGER);
	edgesFeatureCol = delegate.getEdgeFeatures("Arcos Camino"+layerNameToPaint, descripcionCamino, edgesFeatureCol,context);
	
// Dibuja tramos intermedios excluyendo los extremos
//	for (int i = 1;i<edges_camino.size()-1;i++){
//        		Edge edge_camino = (Edge) edges_camino.get(i);
//        		if (edge_camino instanceof ProxyEdge)
//        		    {
//        			edge_camino = (Edge) NodeUtils.unwrapProxies(edge_camino);
//        		    }
//		// adapt schema to edge
//		edgesFeatureCol = schemaCalc.getUpdatedFeatureSchema(edgesFeatureCol, edge_camino);
//		schemaCalc.addAttributeIfNotPresent(edgesFeatureCol, "routeId", AttributeType.INTEGER);
//		//
//		// if (edgesFeatureCol == null){
//		// if (edge_camino instanceof ILocalGISEdge){
//		// edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
//		// } else if (edge_camino instanceof LocalGISStreetDynamicEdge || edge_camino instanceof PMRLocalGISStreetDynamicEdge){
//		// edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
//		// if (edge_camino instanceof PMRLocalGISStreetDynamicEdge)
//		// edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
//		// } else if (edge_camino instanceof NetworkLink){
//		// FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
//		// .createBlankFeatureCollection();
//		//
//		// networkLinkFeatureCol.getFeatureSchema().addAttribute("idEje",
//		// AttributeType.INTEGER);
//		// networkLinkFeatureCol.getFeatureSchema().addAttribute("coste",
//		// AttributeType.DOUBLE);
//		// edgesFeatureCol = networkLinkFeatureCol;
//		// } else{
//		// FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
//		// .createBlankFeatureCollection();
//		//
//		// networkLinkFeatureCol.getFeatureSchema().addAttribute("idEje",
//		// AttributeType.INTEGER);
//		// networkLinkFeatureCol.getFeatureSchema().addAttribute("coste",
//		// AttributeType.DOUBLE);
//		// edgesFeatureCol = networkLinkFeatureCol;
//		// }
//		// edgesFeatureCol.getFeatureSchema().addAttribute("routeId",AttributeType.INTEGER);
//		// }
//			
//			Feature featureedge=NetworkModuleUtilWorkbench.findFeatureForEdge(edge_camino,context);
//
//			if (featureedge != null){
//				Feature newFeatureEdges = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//				newFeatureEdges.setGeometry(featureedge.getGeometry());
//				newFeatureEdges.setAttribute("routeId", numRuta);
//				edgesFeatureCol.add(newFeatureEdges);
//				
//			} else {
//				// la feature original no se ha encontrado.... se agrega el arco como feature original.
//
//				featureedge = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//
//				Coordinate[] coords = NodeUtils.CoordenadasArco(
//						edge_camino, p, pos_inicio, pos_fin);
//				LineString ls = (LineString) fact1.createLineString(coords);
//
//				featureedge.setGeometry(ls);
//				featureedge.setAttribute("routeId", numRuta);
//				edgesFeatureCol.add(featureedge);
//			}
//			if(startFeature == null)startFeature = featureedge;
//			pos_inicio++;
//			pos_fin++;
//			if (pos_fin == p.size() + 1) {
//				pos_fin = 0;
//			}
//			tramo--;
//		}// fin del for de edges
		// annadir edges virtuales de los extremos de la ruta al camino dibujado
		if (p != null && p.isValid()){
			addBothEndsFeaturesToCollection(edgesFeatureCol, p, numRuta, lastPosition, startFeature, schemaCalc);
			
			
		}

		if(edgesFeatureCol != null){
			if(layerNameToPaint != null){
				Layer edgesLayer = getLayerToPaint(context.getLayerManager().getLayers(),"Arcos Camino"+layerNameToPaint);
				edgesLayer.setFeatureCollection(edgesFeatureCol);
			}else{
				Layer edgesLayer = context.addLayer(algoritmo, 
						I18N.get("calcruta","routeengine.calcularruta.pathedgeslayername"),
						edgesFeatureCol);
	
				BasicStyle bs = new BasicStyle();
				bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
				bs.setLineWidth(4);
				bs.setEnabled(true);
	
				edgesLayer.addStyle(bs);
			}
		}

		Layer nodesLayer = null;
		if(layerNameToPaint==null){
			nodesLayer = context.addLayer(algoritmo,
				I18N.get("calcruta","routeengine.calcularruta.pathnodeslayername"),
				nodesFeatureCol);
		}else{
			nodesLayer = getLayerToPaint(context.getLayerManager().getLayers(),"Nodos Camino"+layerNameToPaint);
			nodesLayer.setFeatureCollection(nodesFeatureCol);
		}
		if(layerNameToPaint!=null && !isFirst){
			 Layer edges = context.getLayerManager().getLayer("Arcos Camino"+layerNameToPaint);
			 context.getLayerViewPanel().getSelectionManager().getFeatureSelection().unselectItems();
			 context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(
					 edges, 
					 edgesFeatureCol.getFeatures());
		}
	}// fin del metodo dibujarcaminos

	public static void addBothEndsFeaturesToCollection(FeatureCollection edgesFeatureCol, Path p, Integer numRuta, int lastPosition, Feature startFeature,
		CombinedSchemaCalculator schemaCalc)
	{
	    Geometry geomEnd=null;
	    List pathEdges = p.getEdges();
	    Edge lastEdg=(Edge)  NodeUtils.unwrapProxies((Graphable) pathEdges.get(0));
	    Node lastNode = (Node) NodeUtils.unwrapProxies(p.getLast());
	    Node nextNode = (Node) NodeUtils.unwrapProxies(lastEdg.getOtherNode(lastNode));// Nodo más interno
	    VirtualNodeInfo lastNodeInfo = endNodeinfo;
	    if (lastNodeInfo.getEdge().getNodeA()==nextNode) // orientado hacia A
		{
		    geomEnd=lastNodeInfo.getLinestringAtoV().reverse();
		}
	    else
		{
		    geomEnd=lastNodeInfo.getLinestringVtoB();
		}
	    
	    // otro extremo
	    Geometry geomInit=null;
	    Edge firstEdg=(Edge)  NodeUtils.unwrapProxies((Graphable) pathEdges.get(pathEdges.size()-1));
	    Node firstNode = (Node) NodeUtils.unwrapProxies(p.getFirst());
	    nextNode = (Node) NodeUtils.unwrapProxies(firstEdg.getOtherNode(firstNode));// Nodo más interno
	    VirtualNodeInfo firstNodeInfo = startNodeInfo;
	    
	    if (firstNodeInfo.getEdge().getNodeA()==nextNode) // orientado hacia A
		{
		    geomInit=firstNodeInfo.getLinestringAtoV().reverse();
		}
	    else
		{
		    geomInit=firstNodeInfo.getLinestringVtoB();
		}
	    
	 
	    Feature startVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
	    startVirtualNodeFeature.setGeometry(geomInit);
	    startVirtualNodeFeature.setAttribute("routeId", numRuta);
	    edgesFeatureCol.add(startVirtualNodeFeature);

	    Feature endVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
	    endVirtualNodeFeature.setGeometry(geomEnd);
	    endVirtualNodeFeature.setAttribute("routeId", numRuta);
	    edgesFeatureCol.add(endVirtualNodeFeature);
	    
	    
//	    boolean debug=true;
//	    if (debug)
//		{
//	    if (p.getEdges().size() > 2){
//	        	
//	    	Feature firstFeature = null;
//	    	Feature lastFeature = null;
//	    	lastFeature = (Feature) edgesFeatureCol.getFeatures().get(edgesFeatureCol.size() -1);
//	    	if(lastPosition>0)
//	    		firstFeature = startFeature;
//	    	else//TODO: No es la feature 0. Si no hay elementos si, si hay no.
//	    		firstFeature = (Feature) edgesFeatureCol.getFeatures().get(0);
//
//	    	if (firstFeature.getGeometry().distance(firstNodeInfo.getLinestringAtoV()) >
//	    	firstFeature.getGeometry().distance(firstNodeInfo.getLinestringVtoB()) ){
//	    		fInit = firstNodeInfo.getLinestringVtoB();
//	    	}else{
//	    		fInit = firstNodeInfo.getLinestringAtoV();
//	    	}
//
//	    	if (lastFeature.getGeometry().distance(lastNodeInfo.getLinestringAtoV()) >
//	    	lastFeature.getGeometry().distance(lastNodeInfo.getLinestringVtoB()) ){
//	    		fEnd = lastNodeInfo.getLinestringVtoB();
//	    	}else{
//	    		fEnd = lastNodeInfo.getLinestringAtoV();
//	    	}
//
//
//	    } else {
//	    	if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringAtoV()) == 0){
//	    		fInit = firstNodeInfo.getLinestringAtoV();
//	    		fEnd = lastNodeInfo.getLinestringAtoV();
//	    	} else if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringVtoB()) == 0){
//	    		fInit = firstNodeInfo.getLinestringAtoV();
//	    		fEnd = lastNodeInfo.getLinestringVtoB();
//	    	} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringAtoV()) == 0){
//	    		fInit = firstNodeInfo.getLinestringVtoB();
//	    		fEnd = lastNodeInfo.getLinestringAtoV();
//	    	} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringVtoB()) == 0){
//	    		fInit = firstNodeInfo.getLinestringVtoB();
//	    		fEnd = lastNodeInfo.getLinestringVtoB();
//	    	}
//	    	
//	    // if (edgesFeatureCol == null){
//	    Edge firstEdge = firstNodeInfo.getEdge();
//	    // adapt schema to edge
//	    edgesFeatureCol = schemaCalc.getUpdatedFeatureSchema(edgesFeatureCol, firstEdge);
//	    edgesFeatureCol=schemaCalc.addAttributeIfNotPresent(edgesFeatureCol, "routeId", AttributeType.INTEGER);
//	    // if (firstEdge instanceof ILocalGISEdge){
//	    // edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
//	    // } else if (firstEdge instanceof LocalGISStreetDynamicEdge){
//	    // edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
//	    // } else if (firstEdge instanceof NetworkLink){
//	    // FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
//	    // .createBlankFeatureCollection();
//	    //
//	    // networkLinkFeatureCol.getFeatureSchema().addAttribute("idEje",
//	    // AttributeType.INTEGER);
//	    // networkLinkFeatureCol.getFeatureSchema().addAttribute("coste",
//	    // AttributeType.DOUBLE);
//	    // edgesFeatureCol = networkLinkFeatureCol;
//	    // } else{
//	    // FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
//	    // .createBlankFeatureCollection();
//	    //
//	    // networkLinkFeatureCol.getFeatureSchema().addAttribute("idEje",
//	    // AttributeType.INTEGER);
//	    // networkLinkFeatureCol.getFeatureSchema().addAttribute("coste",
//	    // AttributeType.DOUBLE);
//	    // edgesFeatureCol = networkLinkFeatureCol;
//	    // }
//	    // edgesFeatureCol.getFeatureSchema().addAttribute("routeId",AttributeType.INTEGER);
//	    // }
//	    	
//	    }
//
//	    Feature startVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//	    startVirtualNodeFeature.setGeometry(fInit);
//	    startVirtualNodeFeature.setAttribute("routeId", numRuta);
//	    edgesFeatureCol.add(startVirtualNodeFeature);
//
//	    Feature endVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
//	    endVirtualNodeFeature.setGeometry(fEnd);
//	    endVirtualNodeFeature.setAttribute("routeId", numRuta);
//	    edgesFeatureCol.add(endVirtualNodeFeature);
//		}
	}

	
	private static Integer getMaxRouteId(List features) {
		Integer routeId = 0;
		ArrayList<Feature> featureList = new ArrayList<Feature>(features);
		Iterator<Feature> it = featureList.iterator();
		while(it.hasNext()){
			Feature feature = it.next();
			Integer actualRouteId = (Integer)feature.getAttribute("routeId");
			if(actualRouteId > routeId)
				routeId = actualRouteId;
		}
		return routeId;
	}

	/**
	 * Metodo que elimina un determinado tramo de ruta, y agrega uno al resto para que coincida correctamente el orden de las rutas.
	 * 	Si no hay numero, no hace nada, ya que presupone que no hay tramos
	 * 	Si hay numero, realiza la siguiente operacion. 
	 * 	si hay solo 1, elimina los tramos de 1
	 *  si hay mas de 1, elimina los tramos de la ruta seleccionada y suma 2 a las superiores
	 *  	seleccionado 1 -> original 1-2 -> destino 3
	 *  	seleccionado 2 -> original 1-2 -> destino 1
	 *  	seleccionado 1 -> original 1-2-3 -> destino 3-4
	 *  	seleccionado 2 -> original 1-2-3 -> destino 1-4
	 *  	Cuando se agregue el nuevo tramo, agregara en donde estaba la ruta 1 , dos rutas nuevas. Por eso necesita dos
	 * @param edgesFeatureCol Coleccion de tramos
	 * @param nodesFeatureCol2 
	 * @param numRuta ruta a eliminar
	 */
	@SuppressWarnings("unchecked")
	private static void refreshActualRouteData(FeatureCollection edgesFeatureCol,FeatureCollection nodesFeatureCol2, Integer numRuta) {
		if(numRuta == null)
			return;
		ArrayList<Feature> feat = new ArrayList<Feature> (edgesFeatureCol.getFeatures());
		Iterator<Feature> it = feat.iterator();
		ArrayList<Feature> featuresToDelete = new ArrayList<Feature>();
		while(it.hasNext()){
			Feature actFeat = it.next();
			Integer routeId = (Integer)actFeat.getAttribute("routeId");
			if(routeId.equals(numRuta))
				featuresToDelete.add(actFeat);
			if(routeId > numRuta)
				actFeat.setAttribute("routeId", routeId+1);
		}
		edgesFeatureCol.removeAll(featuresToDelete);
		
		ArrayList<Feature> featNodes = new ArrayList<Feature> (nodesFeatureCol2.getFeatures());
		Iterator<Feature> itNodes = featNodes.iterator();
		ArrayList<Feature> featureNodesToDelete = new ArrayList<Feature>();
		while(itNodes.hasNext()){
			Feature actFeat = itNodes.next();
			Integer routeId = (Integer)actFeat.getAttribute("routeId");
			if(routeId.equals(numRuta))
				featureNodesToDelete.add(actFeat);
			if(routeId > numRuta)
				actFeat.setAttribute("routeId", routeId+1);
		}
		nodesFeatureCol2.removeAll(featureNodesToDelete);
	}

	private static Layer getLayerToPaint(List<Layer> layers, String layerName) {
		Iterator<Layer> it = layers.iterator();
		while(it.hasNext()){
			Layer actualLayer = it.next();
			if(actualLayer.getName().equals(layerName))
				return actualLayer;
		}
		return null;
	}

	
	public void dibujarNodosVisitados(
			List<Graphable> nodosvisitados, String Algoritmo) {
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);
		nodesFeatureCol.getFeatureSchema().addAttribute("contador",
				AttributeType.INTEGER);
		GeometryFactory fact = new GeometryFactory();

		for (int i = 0; i < nodosvisitados.size(); i++) {
			XYNode node = (XYNode) nodosvisitados.get(i);
			Coordinate coord = node.getCoordinate();
			Point geom_nodes = fact.createPoint(coord);
			Feature feature = new BasicFeature(nodesFeatureCol
					.getFeatureSchema());
			feature.setGeometry(geom_nodes);
			if (node == source || node == end)
				feature.setAttribute("nodeId", new Integer(node.getID()));
			feature.setAttribute("contador", new Integer(i));
			nodesFeatureCol.add(feature);
		}

		// Creo capas con los nodos
		Layer nodesLayer = context.addLayer(Algoritmo, I18N.get("calcruta","routeengine.calcularruta.visitednodeslayername")

				+ Algoritmo, nodesFeatureCol);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.setAttribute("nodeId");
		labelStyle.setColor(Color.black);
		labelStyle.setScaling(false);
		labelStyle.setEnabled(true);
		nodesLayer.addStyle(labelStyle);
		
	}


	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("calcruta","routeengine.calcularruta.iconfileclick"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!calculateRouteButtonAdded)
		{
			//			toolbox.addToolBar();
//FIX JPC		CalcularRutaClick explode = new CalcularRutaClick();                 
//			toolbox.addPlugIn(explode, null, explode.getIcon());
//			toolbox.finishAddingComponents();
//			toolbox.validate();
//			calculateRouteButtonAdded = true;
		    this.toolbox=toolbox;
			toolbox.addPlugIn(this, null, this.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			calculateRouteButtonAdded = true;
		}
	}



	public static String getQueryFromIdLayer(Connection con, int idLayer) {

		String unformattedQuery = "";
		String sqlQuery = "select name from layers where id_layer = " + idLayer;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			if (con != null){
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setInt(1,idLayer);
				rs = preparedStatement.executeQuery ();

				if(rs.next()){ 
					unformattedQuery = rs.getString("name");
				}
				preparedStatement.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return unformattedQuery;

	}


	private static TaskMonitorDialog progressDialog = null;

	public static UndoableCommand createAddCommand(Point point,AbstractCursorTool tool) {
		// TODO Auto-generated method stub
		final Point p = point;

		return new UndoableCommand(tool.getName()) {
			public void execute() {
				if (puntosRuta.size() == 0){
					addSourceToRoute(p, false);
					// Reactiva el CursorTool
					context.getLayerViewPanel().setCurrentCursorTool(RouteEngineDrawPointTool.create(context.getWorkbenchContext()));

				} else {
					addSourceToRoute(p, true);
					// Reactiva el CursorTool
					context.getLayerViewPanel().setCurrentCursorTool(RouteEngineDrawPointTool.create(context.getWorkbenchContext()));

					progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					final PlugInContext runContext = context;
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
										CalculateRouteTwoLastNodes(null);
										// Reactiva el CursorTool
										context.getLayerViewPanel().setCurrentCursorTool(RouteEngineDrawPointTool.create(context.getWorkbenchContext()));

									} 
									catch (Exception e)
									{
										throw new RuntimeException(e);
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
			}

			

			public void unexecute() {
				listaDePuntos.remove(p.toString());
				puntosRuta.remove(p);
			}
		};

	}
	private static void addSourceToRoute(Point p, boolean b) {
		addSourceToRoute(p,b,false,null);
		
	}
	protected static VirtualNodeInfo startNodeInfo = null;
	private static VirtualNodeInfo endNodeinfo = null;
	protected static void CalculateRouteTwoLastNodes(Integer routeSelected) throws ElementNotFoundException {
		LocalGISWeighter weighter=null;
		LocalGISNetworkManager netMngr = (LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context);
// FIX JPC inutil try		try{
			netMngr.endUseOfVirtualNodes(CalcularRutaClick.class);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}

		if (netMngr != null){

			startNodeInfo = puntosRuta.get(puntosRuta.size() - 2);
			source = ((LocalGISNetworkManager)netMngr).addNewVirtualNode(
					(DynamicGraph) netMngr.getNetwork(startNodeInfo.getNetworkName()).getGraph(),
					startNodeInfo.getEdge(),
					startNodeInfo.getRatio(), 
					CalcularRutaClick.class);

			endNodeinfo = puntosRuta.get(puntosRuta.size() - 1);
			end = ((LocalGISNetworkManager)netMngr).addNewVirtualNode(
					(DynamicGraph) netMngr.getNetwork(endNodeinfo.getNetworkName()).getGraph(),
					endNodeinfo.getEdge(),
					endNodeinfo.getRatio(), 
					CalcularRutaClick.class);

			if (source != null && end != null){
				double coste_ruta = 0.0;
				try {
					RoutePath path = new RoutePath();
					List<Graphable> nodosvisitados = new ArrayList<Graphable>();
					ArrayList<IdEdgeNetworkBean> temporal = (ArrayList<IdEdgeNetworkBean>) aplicacion.getBlackboard().get("temporalincidents");
					if (configProperties.getTipoVehiculo().equals("En Coche")){
						if(temporal == null)
							weighter = new LocalGISStreetWeighter();
						else
							weighter = new LocalGISStreetWeighter(temporal,netMngr);
					} else{
						if(temporal == null)
							weighter = new LocalGISWeighter();
						else
							weighter = new LocalGISWeighter(temporal,netMngr);
					}
					if (configProperties.getDisabilityType() != null && !configProperties.getDisabilityType().equals("")) // TODO diferenciar disability de ignorar rebajes de bordillo
					{
						weighter = new LocalGISPMRWeighter(Double.parseDouble(configProperties.getPavementWidth())/100, Double.parseDouble(configProperties.getTransversalSlope()), Double.parseDouble(configProperties.getLongitudinalSlope()),true);
					}

					int tinicial = (int) System.currentTimeMillis();
					
					path = netMngr.findEvaluatedShortestPath(source, end, weighter);
					

					//					AStarShortestPathFinder pfAstar = new AStarShortestPathFinder(source, end, afunc);
					//					pfAstar.calculate();


					//					path = pfAstar.getPath();

					//					coste_ruta = pfAstar.getCost(end);
					int tfinal = (int) System.currentTimeMillis();
//					JOptionPane.showMessageDialog(null, tfinal- tinicial);
					String Algoritmo =  I18N.get("calcruta","routeengine.calcularruta.astaralgorithm");

					/*
					 * permite dibujar los nodos guardados en una coleccion Lo hemos
					 * utilizAdo para observar la diferencia entre los nodos que
					 * visita el algoritmo dijkstra y A*
					 */

					if (path != null && !path.isEmpty()){
						dibujarCamino(Algoritmo, path, coste_ruta,routeSelected);
						if(context.getWorkbenchFrame() != null){
							context.getWorkbenchFrame().getOutputFrame().createNewDocument();
							context.getWorkbenchFrame().getOutputFrame().show();
						}
					}
					if(context.getWorkbenchFrame()!= null)
						generarInformeRuta(context, path, coste_ruta);

				} catch (PathNotFoundException e) {
					JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(), I18N.get("calcruta","routeengine.calcularruta.errormessage.pathnotfound"));
					//ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), I18N.get("calcruta","routeengine.calcularruta.taskdialogmessage"), I18N.get("calcruta","routeengine.calcularruta.errormessage.pathnotfound"), e.printStackTrace());
				} catch (ElementNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
				    netMngr.endUseOfVirtualNodes(CalcularRutaClick.class);
				    if (weighter!=null)
					{
					HTMLFrame output = context.getWorkbenchFrame().getOutputFrame();
					ArrayList<String> msgs = weighter.getReports();
					if (msgs.size()>0)
						{
							output.createNewDocument();
							output.append("<p>Lista de incidentes encontrados durante el cálculo de las rutas:<p>");
							output.append("<ul>");
							for (String msg : msgs) {
								output.append("<li>");
								output.append(msg);
								output.append("</li>");
							}
							output.append("</ul>");
						}
					}
				}

				// texto en la ventana de salida


			}
		}
	}

	public static Set<Graphable> buildForbiddenGraphables(
			VirtualNodeInfo startNodeInfo2, VirtualNodeInfo endNodeinfo2) {
		Set<Graphable> forbidden = new HashSet<Graphable>();
		forbidden.add(startNodeInfo2.getEdge());
		forbidden.add(endNodeinfo2.getEdge());
		return forbidden;
	}

	private static void addEndPointToRoute() {
		// TODO Auto-generated method stub

	}

	protected static void addSourceToRoute(Point p, boolean isEndPoint,boolean writeMiddleValue,Integer routeId) {
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
					}catch(RuntimeException e){
					}
					if (nodeInfo != null){
						virtualNodesInfo.add(nodeInfo);
					}
				}
				if(virtualNodesInfo.size() == 0){
					return;
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



				//				((LocalGISNetworkManager)networkMgr).addNewVirtualNode(networkMgr.getNetwork(selectedNodeInfo.getNetworkName()).getGraph()
				//						, selectedNodeInfo.getEdge()
				//						, selectedNodeInfo.getRatio()
				//						, this);
				puntosRuta.add(selectedNodeInfo);
				//				
				Coordinate coord = selectedNodeInfo.getLinestringVtoB().getCoordinateN(0);
				Point geom_nodes = fact.createPoint(coord);
				Feature feature = new BasicFeature(nodesFeatureCol.getFeatureSchema());
				feature.setGeometry(geom_nodes);
				if(routeId == null){
					if (isEndPoint){
						if(getMaxRouteId(sourcePointLayer.getFeatureCollectionWrapper().getFeatures()) == 1)
							feature.setAttribute("routeId", 2);
						else
							feature.setAttribute("routeId", getMaxRouteId(sourcePointLayer.getFeatureCollectionWrapper().getFeatures())+1);
					} else{
						feature.setAttribute("routeId", 1);
					}
					sourcePointLayer.getFeatureCollectionWrapper().add(feature);
				}else{
					if(writeMiddleValue){
						refactorRouteId(sourcePointLayer,routeId);
						feature.setAttribute("routeId", routeId+1);
						sourcePointLayer.getFeatureCollectionWrapper().add(feature);
					}
				}
				
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

	private static void refactorRouteId(Layer sourcePointLayer2, Integer routeId) {
		ArrayList<Feature> data = new ArrayList<Feature>(sourcePointLayer2.getFeatureCollectionWrapper().getFeatures());
		Iterator<Feature> dataIt = data.iterator();
		while(dataIt.hasNext()){
			Feature feature = dataIt.next();
			Integer actualRouteId = (Integer)feature.getAttribute("routeId");
			if(actualRouteId > routeId)
				feature.setAttribute("routeId",actualRouteId+1);
		}
	}

	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub

	}
}
