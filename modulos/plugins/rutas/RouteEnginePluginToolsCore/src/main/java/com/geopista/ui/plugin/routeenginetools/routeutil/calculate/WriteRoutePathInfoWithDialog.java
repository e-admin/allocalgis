/**
 * WriteRoutePathInfoWithDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;

import com.geopista.app.AppContext;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.routeenginetools.CombinedSchemaCalculator;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.RouteStretchInterface;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.TurnRouteStreetchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl.InfoRouteDAOPostgreSQLImplementation;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs.InfoRouteDialog;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.DescriptionUtils;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;

public class WriteRoutePathInfoWithDialog {

	public static void showInfoRouteDialog(Path routePath, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
			PlugInContext context){
		
		inicializarIdiomaAvisosPanels();
		
		ArrayList<RouteStretchInterface> list;
		try {
		    GeometryFactory geometryFactory=new GeometryFactory();
		    LocalGISNetworkManager netManager = ((LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context));
		    WorkBenchGeometryProducer geomProducer = new WorkBenchGeometryProducer(context,geometryFactory);
		    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());    
		    ResourceBundle rb = (ResourceBundle)I18N.plugInsResourceBundle.get("routedescription");
		    Collection<Network> configuredNetworks = WriteRoutePathInfoWithDialog.getConfiguratorNetworks(netManager).values();

			list = DescriptionUtils.buildInfoRouteStretchBeanList(routePath, startNodeInfo, endNodeInfo, netManager,configuredNetworks,geomProducer,routeDao,rb);
			
			
			if (list!=null && !list.isEmpty()){
				
				//Dibujamos las capas con los arcos y con los nodos de la ruta			
				@SuppressWarnings("unused")
				InfoRouteDialog dialog = new InfoRouteDialog(context, I18N.get("routedescription","routeengine.route.description.dialog.title"),list);
			} else{
				dibujarCamino("", routePath, 0, context, startNodeInfo, endNodeInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			// Al menos se intenta pintar las ruta con los edges
			dibujarCamino("", routePath, 0, context, startNodeInfo, endNodeInfo);
			
			ErrorDialog.show(context.getWorkbenchGuiComponent().getMainFrame(), 
					I18N.get("routedescription","routeengine.route.description.error.info.notbuild.title"), 
					I18N.get("routedescription","routeengine.route.description.error.info.notbuild.message"), 
					StringUtil.stackTrace(e)
					);
		}
	}
	
	public static void showInfoRouteDialog(ArrayList<Path> routePaths, VirtualNodeInfo virtualNodes[],
			PlugInContext context){
		
		inicializarIdiomaAvisosPanels();
		
		
		try {
		    GeometryFactory geometryFactory=new GeometryFactory();
		    LocalGISNetworkManager netManager = ((LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context));
		    WorkBenchGeometryProducer geomProducer = new WorkBenchGeometryProducer(context,geometryFactory);
		    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());    
		    ResourceBundle rb = (ResourceBundle)I18N.plugInsResourceBundle.get("routedescription");
		    Collection<Network> configuredNetworks = WriteRoutePathInfoWithDialog.getConfiguratorNetworks(netManager).values();

			ArrayList<RouteStretchInterface> list = new ArrayList<RouteStretchInterface>();
			if (routePaths!=null && !routePaths.isEmpty()){
				Iterator<Path> pathsIt = routePaths.iterator();
				int i = 0;
				while(pathsIt.hasNext())
				    {
					list.add(new TurnRouteStreetchBean(null,"<html><b>"+ I18N.get("routedescription","routeengine.route.description.route.description") +" " + (i+1) + "</b></html>"));
					
					VirtualNodeInfo startNodeInfo = null;
					if (virtualNodes != null && virtualNodes.length>0 && i<virtualNodes.length){
							startNodeInfo = virtualNodes[i];
					}
					
					VirtualNodeInfo endNodeInfo = null;
					if (virtualNodes!=null && virtualNodes.length>0 && (i+1)<virtualNodes.length ){
						endNodeInfo = virtualNodes[i+1];
					}
					
					
					
					ArrayList<RouteStretchInterface> actualist = DescriptionUtils.buildInfoRouteStretchBeanList(pathsIt.next(), startNodeInfo, endNodeInfo, netManager,configuredNetworks, geomProducer,routeDao, rb);
					
					
					// aï¿½adimos al primer elemento del tipo "Ruta 1" todas las geometrias,
					// para que cuando se haga click sobre la lista de informacion haga zoom a 
					// toda la ruta.
					if (actualist!=null && !actualist.isEmpty()){
						Iterator<RouteStretchInterface> it = actualist.iterator();
						InfoRouteStretchBean routePahtIDescriptior = (InfoRouteStretchBean) list.get(list.size()-1);
						while(it.hasNext()){
							InfoRouteStretchBean actualInfoRouteStretchBean = (InfoRouteStretchBean) it.next();
							if (actualInfoRouteStretchBean!= null){
								ArrayList<Geometry> geoms = actualInfoRouteStretchBean.getGeometries();
								if (geoms!=null && !geoms.isEmpty()){
									routePahtIDescriptior.addAllGeometriesToCollection(geoms);
								}
							}
						}
					}
					
					list.addAll(actualist);
					i++;
				}
				
				
				if (list!=null && !list.isEmpty()){
					@SuppressWarnings("unused")
					InfoRouteDialog dialog = new InfoRouteDialog(context, I18N.get("routedescription","routeengine.route.description.dialog.title"),list);
				}	
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			ErrorDialog.show(context.getWorkbenchGuiComponent().getMainFrame(), 
					I18N.get("routedescription","routeengine.route.description.error.info.notbuild.title"), 
					I18N.get("routedescription","routeengine.route.description.error.info.notbuild.message"), 
					StringUtil.stackTrace(e)
					);
		}
	}
	
	
	
	
	

//	public static ArrayList<InfoRouteStretchBean> buildEdgesInfoStretchBeans(Path routePath, GeometryFactory geometryFactory, NetworkManager netManager, EdgesGeometryProducer geomProducer)
//	{
//
//	    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());    
//	    return DescriptionUtils.buildPathDescriptionBeans(routePath, geometryFactory, netManager, geomProducer, routeDao);
//	}

	public static HashMap<String, Network> getConfiguratorNetworks(NetworkManager networkManager)
	{
	    	
		HashMap<String, Network> configuatorNetworks = new HashMap<String, Network>();
		CalcRutaConfigFileReaderWriter configProperties = new CalcRutaConfigFileReaderWriter();
		
		String redes[] = configProperties.getRedesNames();
		for(int i = 0; i < redes.length; i++){
			
			configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) networkManager).getAllNetworks().get(redes[i]) );
		}
		return configuatorNetworks;
	}
	
	
	@Deprecated
	public static ArrayList<String> getTypeAndDescriptionStreet(Edge actualEdge, PlugInContext context)
	{	
		NetworkManager netManager = ((LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context));
		InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
		Collection<Network> configuredNetworks = getConfiguratorNetworks(netManager).values();
		return DescriptionUtils.getTypeAndDescriptionStreet(actualEdge, netManager,configuredNetworks, routeDao);
	}

	public static Geometry getOriginalGeometryFromEdgeIdLayerIdFeature(ILocalGISEdge edge, int SRID){
		
		try{
			InfoRouteDAOInterface routeDAO = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());	
			return routeDAO.getLinestringFromIdEdge(routeDAO.getGeopistaSQLConnection(), edge, SRID);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	
	@Deprecated
	private static String getTypeStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr)
	{
	    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
	    return getTypeStreetDescriptor(edge, subred, networkMgr, routeDao);
	}

	public static String getTypeStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr, InfoRouteDAOInterface routeDao)
	{
	    String typeColumnDescriptor = "";
	    String typeDescription = "";
	    
	    if (edge instanceof EquivalentEdge)
	        {
	    	edge = (Edge) ((EquivalentEdge) edge).getEquivalentTo();
	        }
	    
	    
	    if (edge instanceof PMRLocalGISStreetDynamicEdge)
	        {
	    	PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge) edge;
	    	return pmrEdge.getsEdgeType();
	        }
	    else
	    if (edge instanceof LocalGISStreetDynamicEdge)
	        {
	    	// TODO Revisar esto no se en que caso de uso se utilizará
	    	try{
	    		LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) edge;
	    		typeColumnDescriptor = ((NetworkProperty) networkMgr.getNetwork(getRedNameFromSubRed(subred,networkMgr)).getProperties().
	    				get("TypeColumnDescriptor")).getValue(
	    						Integer.toString(streetEdge.getIdLayer())
	    				);

	    		if (typeColumnDescriptor != null && !typeColumnDescriptor.equals(""))
	    		    {
	    			int muni=AppContext.getIdMunicipio();
	    			typeDescription = routeDao.getColumnsDescriptorFromIdLayer(streetEdge.getIdLayer(), streetEdge.getIdFeature(), typeColumnDescriptor );
	    		}
	    	} catch (Exception e) {
	    		return typeDescription;
	    	}
	    }
	    return typeDescription;
	}
	
	
	@Deprecated
	public static String getNameStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr)
	{
	    	InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
		return DescriptionUtils.getNameStreetDescriptor(edge, subred, networkMgr, routeDao);
	}

	@SuppressWarnings("unchecked")
	public
	static String getRedNameFromSubRed(String subred, NetworkManager networkMgr) {
		String result = networkMgr.getNetwork(subred).getName();
		if (result!=null && !result.equals("")){
			return result;
		}
	
		Iterator it = networkMgr.getNetworks().keySet().iterator();
		while(it.hasNext()){
			String redName = (String) it.next(); 
			Iterator it2 = networkMgr.getNetwork(redName).getSubnetworks().keySet().iterator();
			while(it2.hasNext()){
				if (networkMgr.getNetwork(redName).getSubNetwork((String) it2.next()) != null){
					return redName;
				}
			}
		}
		return null;
	}
	
	
	
	
	
	//////////// DIBUJAR CAMINO //////////////////////////////////////////////////////////////////////////////////////
	// TODO Fusionar con CalcularRutaClick.dibujarCamino
	public static void dibujarCamino(String Algoritmo, Path p,
			double coste_ruta, PlugInContext context, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeinfo ) {
		FeatureCollection edgesFeatureCol = null;
		//				.createBlankFeatureCollection();
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);


		GeometryFactory fact1 = new GeometryFactory();
		List<Object> edges_camino = p.getEdges();
		GeopistaLayer layer = null;

		for (Iterator<Node> iter_nodes = p.iterator(); iter_nodes.hasNext();) {
			Node node = (Node) iter_nodes.next();
			Coordinate coord = ((XYNode) node).getCoordinate();
			Point geom_nodes = fact1.createPoint(coord);
			Feature feature = new BasicFeature(nodesFeatureCol
					.getFeatureSchema());
			feature.setGeometry(geom_nodes);
			feature.setAttribute("nodeId", new Integer(node.getID()));
			nodesFeatureCol.add(feature);
		}

		// creo capas con los arcos
		int pos_inicio = 0, pos_fin = 1, tramo = edges_camino.size();
		int lastIdLayer = 0;
	CombinedSchemaCalculator schemaCalculator = new CombinedSchemaCalculator();

		for (int i = 1;i<edges_camino.size()-1;i++){
			Edge edge_camino = (Edge) edges_camino.get(i);
			if (edge_camino instanceof ProxyEdge){
				edge_camino = (Edge) ((ProxyEdge)edge_camino).getGraphable();
			}
		edgesFeatureCol = schemaCalculator.getUpdatedFeatureSchema(edgesFeatureCol, edge_camino);

		Feature featureedges = NetworkModuleUtilWorkbench.findFeatureForEdge(edge_camino,context);
//			if(edge_camino instanceof NetworkLink){
//				//continue;
//			}else if (!(edge_camino instanceof ILocalGISEdge)){
//				//continue
//			}else if (lastIdLayer != ((ILocalGISEdge) edge_camino).getIdLayer()){
//
//				layer = getOriginalLayer(edge_camino, context);
//				if (layer != null){
//					lastIdLayer = layer.getId_LayerDataBase();
//				} else{
//					lastIdLayer = ((ILocalGISEdge) edge_camino).getIdLayer();
//				}
//			}
//
//			ArrayList<Object> originalFeatures = new ArrayList<Object>();
//
//			if (layer != null && !(edge_camino instanceof NetworkLink)){
//				originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
//						getWrappee().getFeatures());
//			}
//
//			Iterator it = originalFeatures.iterator();
//
//			Feature featureedges = null;
//			GeopistaFeature f2 = null;
//			while (it.hasNext()&&edge_camino instanceof ILocalGISEdge){
//				f2 = (GeopistaFeature) it.next();
//				if (Integer.parseInt(f2.getSystemId()) == ((ILocalGISEdge)edge_camino).getIdFeature()){
//					featureedges = f2;
//					break;
//				}
//			}

			if (featureedges != null){
				edgesFeatureCol.add(featureedges);
			} else {
				// la feature original no se ha encontrado.... se agrega el arco como feature original.

				featureedges = new BasicFeature(edgesFeatureCol.getFeatureSchema());

				Coordinate[] coords = NodeUtils.CoordenadasArco(
						edge_camino, p, pos_inicio, pos_fin);
				LineString ls = (LineString) fact1.createLineString(coords);

				featureedges.setGeometry(ls);

				edgesFeatureCol.add(featureedges);
			}
			pos_inicio++;
			pos_fin++;
			if (pos_fin == p.size() + 1) {
				pos_fin = 0;
			}
			tramo--;
		}// fin del for de edges

		// aniadir edges virtuales al camino dibujado
		if (p != null && p.isValid()){
			Node firstNode = p.getFirst();
			Node lastNode = p.getLast();
			VirtualNodeInfo firstNodeInfo = startNodeInfo;
			VirtualNodeInfo lastNodeInfo = endNodeinfo;
			LineString fInit = null;
			LineString fEnd = null;

			if (p.getEdges().size() > 2){
				Feature firstFeature = null;
				Feature lastFeature = null;
				lastFeature = (Feature) edgesFeatureCol.getFeatures().get(0);
				firstFeature = (Feature) edgesFeatureCol.getFeatures().get(edgesFeatureCol.size() -1);

				if (firstFeature.getGeometry().distance(firstNodeInfo.getLinestringAtoV()) >
				firstFeature.getGeometry().distance(firstNodeInfo.getLinestringVtoB()) ){
					fInit = firstNodeInfo.getLinestringVtoB();
				}else{
					fInit = firstNodeInfo.getLinestringAtoV();
				}

				if (lastFeature.getGeometry().distance(lastNodeInfo.getLinestringAtoV()) >
				lastFeature.getGeometry().distance(lastNodeInfo.getLinestringVtoB()) ){
					fEnd = lastNodeInfo.getLinestringVtoB();
				}else{
					fEnd = lastNodeInfo.getLinestringAtoV();
				}


			} else {
				if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringAtoV()) == 0){
					fInit = firstNodeInfo.getLinestringAtoV();
					fEnd = lastNodeInfo.getLinestringAtoV();
				} else if (firstNodeInfo.getLinestringAtoV().distance(lastNodeInfo.getLinestringVtoB()) == 0){
					fInit = firstNodeInfo.getLinestringAtoV();
					fEnd = lastNodeInfo.getLinestringVtoB();
				} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringAtoV()) == 0){
					fInit = firstNodeInfo.getLinestringVtoB();
					fEnd = lastNodeInfo.getLinestringAtoV();
				} else if (firstNodeInfo.getLinestringVtoB().distance(lastNodeInfo.getLinestringVtoB()) == 0){
					fInit = firstNodeInfo.getLinestringVtoB();
					fEnd = lastNodeInfo.getLinestringVtoB();
				}
			}

			Feature startVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
			startVirtualNodeFeature.setGeometry(fInit);
			edgesFeatureCol.add(startVirtualNodeFeature);

			Feature endVirtualNodeFeature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
			endVirtualNodeFeature.setGeometry(fEnd);
			edgesFeatureCol.add(endVirtualNodeFeature);
			
			
		}

		if(edgesFeatureCol != null){
			Layer edgesLayer = context.addLayer(Algoritmo, 
					I18N.get("calcruta","routeengine.calcularruta.pathedgeslayername"),
					edgesFeatureCol);

			BasicStyle bs = new BasicStyle();
			bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
			bs.setLineWidth(4);
			bs.setEnabled(true);

			edgesLayer.addStyle(bs);
		}
		Layer nodesLayer = context.addLayer(Algoritmo,
				I18N.get("calcruta","routeengine.calcularruta.pathnodeslayername"),
				nodesFeatureCol);
		//		LabelStyle labelStyle = new LabelStyle();
		//		labelStyle.setAttribute("nodeId");
		//		labelStyle.setColor(Color.black);
		//		labelStyle.setScaling(false);
		//		labelStyle.setEnabled(true);
		//		nodesLayer.addStyle(labelStyle);

	}// fin del mï¿½todo dibujarcaminos
	
	
	
	
	
	private static IGeopistaLayer getOriginalLayer(Edge edge_camino, PlugInContext context) {
		IGeopistaLayer layer;
		GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
		Map properties = new HashMap();
		//Introducimos el mapa Origen
		properties.put("mapadestino",(GeopistaMap) context.getTask());
		//Introducimos el fitro geometrico si es distinto de null, si se introduce null falla
		//properties.put("filtrogeometrico",null);
		//Introducimos el FilterNode
		properties.put("nodofiltro",FilterLeaf.equal("1",new Integer(1)));
		//Introducimos el srid del mapa destino
		properties.put("srid_destino", Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode()));

		serverDataSource.setProperties(properties);
		GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();

		//Creamos una coleccion para almacenar las excepciones que se producen
		Collection exceptions = new ArrayList();
		//preparamos la url de la layer
		String layerID = "tramosvia";
		RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
		InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
		
		Connection conn = routeConnection.getConnection();
		layerID = routeDao.getSystemLayerIdFromIntIdLayer(((ILocalGISEdge) edge_camino).getIdLayer());
		URL urlLayer = null;
		try {
			urlLayer = new URL("geopistalayer://default/"+ layerID);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		geopistaConnection.executeQuery(urlLayer.toString(),exceptions,null);
		layer = geopistaConnection.getLayer(); 

		return layer;
	}

	
	@SuppressWarnings("unchecked")
	public static void inicializarIdiomaAvisosPanels(){
		if (I18N.plugInsResourceBundle.get("routedescription") == null){
			Locale loc=I18N.getLocaleAsObject();    
			ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.routeutil.calculate.language.RouteEngine_RouteDescriptioni18n",loc,WriteRoutePathInfoWithDialog.class.getClassLoader());
			I18N.plugInsResourceBundle.put("routedescription",bundle);
		}
	}	
}
