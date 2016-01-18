/**
 * WriteRoutePathInformationUtil.java
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
import java.util.Map;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.graph.structure.proxy.ProxyGeographicNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;

import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl.InfoRouteDAOPostgreSQLImplementation;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.DescriptionUtils;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;




public class WriteRoutePathInformationUtil{
	
	@Deprecated
	public static HashMap<String, Network> getConfiguratorNetworks(PlugInContext context)
	{
		LocalGISNetworkManager networkManager = (LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context);

		return DescriptionUtils.getConfiguratorNetworks(networkManager);
	}

	public static void writeRoutePathInformation(HTMLFrame outputFrame, List<Edge> edges, Path p, PlugInContext context,
			VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo)
	{
		
		NetworkManager netManger = ((LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context));
		
		StringBuilder outputHtml = getRoutePathTextualInformacion(edges, p, startNodeInfo, endNodeInfo, netManger, new GeopistaRouteConnectionFactoryImpl(),context);
		
		outputFrame.append(outputHtml.toString());
		int minutos = (int) (((RoutePath)p).getTotalCost() / 60) % 60 ;
		int horas = (int) (((RoutePath)p).getTotalCost() / 60 / 60);
		if (horas >0 || minutos>0){
		    outputFrame.addText("El tiempo estimado de la ruta es: " + horas + " Horas " + minutos + " Minutos");
		} else if (horas <= 0 && minutos <=0){
		    outputFrame.addText("El tiempo estimado de la ruta es: Menos de 1 minuto");
		}
	}

	private static int lastIdLayer = -1;
	private static GeopistaLayer layer = null;
//	private static double distanciaInicio;
//	private static double distanciaFinal;
	public static double getAnguloEntreEdges(Edge[] edges, Path p, VirtualNodeInfo startNodeInfo,
			VirtualNodeInfo endNodeinfo,PlugInContext context)
	{

		GeopistaFeature primerFeature = null;
		GeopistaFeature segundaFeature = null;
		double angulo = 0;

		for (int i = 0; i < edges.length; i++){

			ArrayList<Object> originalFeatures = new ArrayList<Object>();
			ILocalGISEdge iedge = null;
			if (!(edges[i] instanceof ILocalGISEdge)){
				if (edges[i] instanceof DynamicEdge){
					if (startNodeInfo!=null && startNodeInfo.getEdge()!=null && startNodeInfo.getEdge() instanceof ILocalGISEdge){
						if (startNodeInfo.getEdge().getNodeA().getID()==edges[i].getNodeA().getID() || 
								startNodeInfo.getEdge().getNodeB().getID()==edges[i].getNodeB().getID() ||
								startNodeInfo.getEdge().getNodeA().getID()==edges[i].getNodeB().getID() ||
								startNodeInfo.getEdge().getNodeB().getID()==edges[i].getNodeA().getID() ){
							iedge = (ILocalGISEdge) startNodeInfo.getEdge();
						}
					} else if (endNodeinfo!=null && endNodeinfo.getEdge()!=null && endNodeinfo.getEdge() instanceof ILocalGISEdge){
						if (endNodeinfo.getEdge().getNodeA().getID()==edges[i].getNodeA().getID() || 
								endNodeinfo.getEdge().getNodeB().getID()==edges[i].getNodeB().getID() ||
								endNodeinfo.getEdge().getNodeA().getID()==edges[i].getNodeB().getID() ||
								endNodeinfo.getEdge().getNodeB().getID()==edges[i].getNodeA().getID() ){
							iedge = (ILocalGISEdge) endNodeinfo.getEdge();
						}
					}
				}
			}else if (edges[i] instanceof ILocalGISEdge){
				iedge = (ILocalGISEdge) edges[i];
			}
/**
 * Localiza una geometría adecuada para cada edge
 */
			if (i==0)
			{
			    primerFeature=(GeopistaFeature) NetworkModuleUtilWorkbench.findFeatureForEdge(iedge, context);
			}
			else if (i==1)
			{
			segundaFeature=(GeopistaFeature) NetworkModuleUtilWorkbench.findFeatureForEdge(iedge, context);
			}
//			if ( iedge !=null ){
//				if (lastIdLayer != iedge.getIdLayer()){
//
//					layer = getOriginalLayer((Edge) iedge, context);
//					if (layer != null){
//						lastIdLayer = layer.getId_LayerDataBase();
//					} else{
//						lastIdLayer = ((ILocalGISEdge) edges[i]).getIdLayer();
//					}
//				}
//
//				if (layer != null){
//					originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
//							getWrappee().getFeatures());
//				}
//
//
//				Iterator<Object> it = originalFeatures.iterator();
//
//				Feature featureedges = null;
//				GeopistaFeature f2 = null;
//				while (it.hasNext()){
//					f2 = (GeopistaFeature) it.next();
//					if (Integer.parseInt(f2.getSystemId()) == iedge.getIdFeature()){
//						if (i == 0){
//							primerFeature = f2;
//							break;
//						} else if (i == 1){
//							segundaFeature = f2;
//							break;
//						}
//					}
//				}
//			}

		}


		LineString primerLineString = null;
		LineString segundoLineString = null;
		if (primerFeature != null && segundaFeature != null){

		    primerLineString=NetworkModuleUtil.getEdgeLineStringFromGeometry(primerFeature.getGeometry());
		    segundoLineString=NetworkModuleUtil.getEdgeLineStringFromGeometry(segundaFeature.getGeometry());
//			if (primerFeature.getGeometry() instanceof LineString){
//				primerLineString = (LineString) primerFeature.getGeometry();
//			}else if (primerFeature.getGeometry() instanceof MultiLineString){
//				Coordinate c0= primerFeature.getGeometry().
//				getGeometryN(primerFeature.getGeometry().getNumGeometries()-1).
//				getCoordinates()[0];
//				Coordinate c1=primerFeature.getGeometry().
//				getGeometryN(primerFeature.getGeometry().getNumGeometries()).
//				getCoordinates()[1];
//				Coordinate[] coordmls =new Coordinate[] {
//						c0, 
//						c1};
//
//				CoordinateSequence seq= new CoordinateArraySequence(coordmls);
//				primerLineString=new LineString(seq, new GeometryFactory());
//			}

//
//			if (segundaFeature.getGeometry() instanceof LineString){
//				segundoLineString = (LineString) segundaFeature.getGeometry();
//			}else if (segundaFeature.getGeometry() instanceof MultiLineString){
//				Coordinate c0= segundaFeature.getGeometry().
//				getGeometryN(segundaFeature.getGeometry().getNumGeometries()-1).
//				getCoordinates()[0];
//				Coordinate c1=segundaFeature.getGeometry().
//				getGeometryN(segundaFeature.getGeometry().getNumGeometries()).
//				getCoordinates()[1];
//				Coordinate[] coordmls =new Coordinate[] {
//						c0, 
//						c1};
//
//				CoordinateSequence seq= new CoordinateArraySequence(coordmls);
//				segundoLineString=new LineString(seq, new GeometryFactory());
//			}


			double x1 = -1;
			double y1 = -1;			
			double x2 = -1;
			double y2 = -1;

			if (primerLineString != null && segundoLineString != null){
				x1 = primerLineString.getGeometryN(0).getCoordinates()[0].x;
				y1 = primerLineString.getGeometryN(0).getCoordinates()[0].y;

				x2 = primerLineString.getGeometryN(0).getCoordinates()[1].x;
				y2 = primerLineString.getGeometryN(0).getCoordinates()[1].y;

				int[] vector1 = new int[2];
				vector1[0] = (int) (x2 - x1);
				vector1[1] = (int) (y2 - y1);

				x1 = segundoLineString.getGeometryN(0).getCoordinates()[0].x;
				y1 = segundoLineString.getGeometryN(0).getCoordinates()[0].y;						
				x2 = segundoLineString.getGeometryN(0).getCoordinates()[segundoLineString.getGeometryN(0).getCoordinates().length -1].x;
				y2 = segundoLineString.getGeometryN(0).getCoordinates()[segundoLineString.getGeometryN(0).getCoordinates().length -1].y;

				int[] vector2 = new int[2];
				vector2[0] = (int) (x2 - x1);
				vector2[1] = (int) (y2 - y1);


				angulo = Math.toDegrees(Math.acos(vector2[0]/
						Math.sqrt(Math.pow(vector2[0],2) + Math.pow(vector2[1],2) )));

				if (vector2[1] < 0){
					angulo = -angulo;
				}

			}
		}


		return angulo;
	}
	/**
	 * 
	 * @param edge_camino
	 * @param context
	 * @return
	 * @deprecated use {@link NetworkModuleUtil#findFeatureForEdge(Edge, PlugInContext)}
	 */
	@Deprecated
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
		Connection conn = routeConnection.getConnection();
		layerID = NetworkModuleUtil.getLayerIDFromIntIdLayer(conn, ((ILocalGISEdge) edge_camino).getIdLayer());
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
	/**
	 * Especifico para PMR
	 * @param idRelatedTramo
	 * @return
	 */
	public static String getColumnsDescriptorFromTramos(int idRelatedTramo, int idLayerTramos, String column)
	{
	    InfoRouteDAOPostgreSQLImplementation routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
	    return DescriptionUtils.getColumnsDescriptorFromTramos(idRelatedTramo, idLayerTramos, column, routeDao);
	}

	
	// Duplicado 
	// TODO Pedir todas en un turno.
	public static String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column)
	{
	    InfoRouteDAOPostgreSQLImplementation routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
	    return DescriptionUtils.getColumnsDescriptorFromIdLayer(idLayer, idFeature, column, routeDao);
	}
	
	

	   public static StringBuilder getRoutePathTextualInformacion(List<Edge> edges, Path p, VirtualNodeInfo startNodeInfo,	VirtualNodeInfo endNodeInfo, 
		    NetworkManager netManager, RouteConnectionFactory connectionFactory, PlugInContext context)
	    	{
		    InfoRouteDAOPostgreSQLImplementation routeDao = new InfoRouteDAOPostgreSQLImplementation(connectionFactory);

	    	    StringBuilder outputHtml=new StringBuilder();
	    	    int i = 0;
	    	    String lastDescription = null;
	    	    String actualDescription = "";
	    	    Edge actualEdgeA = null;
	    	    Edge actualEdge = null;
	    	    Edge lastEdge = null;
	    	    double longitud = 0;
	    	    String subred = "";
	    	    
	    
	    	    //		outputFrame.append("<ul>");
	    	    //
	    	    //		outputFrame.append((0 ) + " - Siga la Calle ");
	    	    //		outputFrame.addText("durante " 
	    	    //				+ (int) distanciaInicio
	    	    //				+ " metros.");
	    	    //		
	    	    //		outputFrame.append("</ul>");
	    
	    
	    	    for(int m = edges.size()-1; m>=0; m--){
	    
	    	    	actualEdgeA = edges.get(m);
	    	    	if (actualEdgeA instanceof ProxyEdge){
	    //FIX JPC inutil try				try{
	    	    			actualEdgeA = (Edge) ((ProxyEdge)actualEdgeA).getGraphable();
	    //				}catch (Exception e) {
	    //					e.printStackTrace();
	    //				}
	    	    	}
	    	    	if (actualEdgeA instanceof EquivalentEdge) {
	    	    		EquivalentEdge equiv = (EquivalentEdge) actualEdgeA;
	    	    		actualEdgeA=(Edge) equiv.getEquivalentTo();
	    	    	}
	    	    	if(actualEdgeA instanceof NetworkLink){
	    	    		UniqueIDGenerator idGen = new SequenceUIDGenerator();
	    	    		actualEdge = new LocalGISDynamicEdge(actualEdgeA.getNodeA(), actualEdgeA.getNodeB(), idGen);
	    	    		((ILocalGISEdge) actualEdge).setIdLayer(-1);
	    	    		((ILocalGISEdge) actualEdge).setIdFeature(actualEdgeA.getID());
	    	    		((ILocalGISEdge) actualEdge).setEdgeLength(((ProxyGeographicNode)actualEdgeA.getNodeA()).getPosition().distance(((ProxyGeographicNode)actualEdgeA.getNodeB()).getPosition()));
	    
	    	    		outputHtml.append("--- CAMBIO DE RED ---");
	    	    		continue;
	    	    	}
	    	    	actualEdge = (Edge)actualEdgeA;
	    	    	
	    	    	Iterator<Network> nets = DescriptionUtils.getConfiguratorNetworks(netManager).values().iterator();
	    	    	while (nets.hasNext()){
	    	    		BasicNetwork bnet = (BasicNetwork) nets.next();
	    	    		if (bnet.getGraph().getEdges().contains(actualEdge)){
	    	    			subred = bnet.getName();
	    	    			break;
	    	    		}
	    	    	}
	    
	    
	    	    	if (actualEdge instanceof ILocalGISEdge)
	    	    	{
	    
	    	    	String columnDescriptor = "";
	    	    	try{
	    	    		Map<String, Object> properties = netManager.getNetwork(subred).getProperties();
	    	    		if (actualEdge instanceof PMRLocalGISStreetDynamicEdge)
	    	    			{
	    	    				// Específico para aceras
	    	    				int idRelatedTramo=((PMRLocalGISStreetDynamicEdge) actualEdge).getRelatedToId();
	    	    				// TODO Obtener el id de la capa de TramosdeCalle
	    	    				actualDescription= DescriptionUtils.getColumnsDescriptorFromTramos(idRelatedTramo,12083, "denominacion",routeDao);
	    	    			}
	    	    			else
	    	    			{
	    	    			NetworkProperty networkProperty = (NetworkProperty) properties.get("ColumnDescriptor");
	    	    			if (networkProperty!=null)
	    	    				columnDescriptor = networkProperty.getValue(Integer.toString(((ILocalGISEdge) actualEdge).getIdLayer()));
	    	    			
	    	    			if (columnDescriptor != null && !columnDescriptor.equals("")){
	    	    				actualDescription = DescriptionUtils.getColumnsDescriptorFromIdLayer(((ILocalGISEdge) actualEdge).getIdLayer(), ((ILocalGISEdge) actualEdge).getIdFeature(), columnDescriptor,routeDao);
	    	    				}
	    	    			}
	    	    		} catch (Exception e) {
	    	    			// TODO: handle exception
	    	    			actualDescription = Integer.toString(((ILocalGISEdge) actualEdge).getIdFeature());
	    	    		}
	    	    	} else if (actualEdge instanceof DynamicEdge){
	    	    		actualDescription = "calle";
	    	    	}
	    
	    
	    	    	if (lastDescription == null){
	    	    		lastDescription = actualDescription;
	    	    	}
	    
	    	    	if (actualDescription == null){
	    	    		actualDescription = "";
	    	    	}
	    
	    	    	if ( (actualDescription != null && actualDescription.equals(lastDescription)) ){
	    	    		if (actualEdge instanceof ILocalGISEdge){
	    	    			longitud = longitud + ((ILocalGISEdge) actualEdge).getEdgeLength() ;
	    	    		} else if (actualEdge instanceof EdgeWithCost){
	    	    			longitud = longitud + ((EdgeWithCost) actualEdge).getCost(actualEdge.getNodeA(), 1);
	    	    		}
	    	    	} 
	    
	    	    	if (!actualDescription.equals(lastDescription) && longitud > 0){
	    
	    	    		outputHtml.append("<ul>");
	    
	    	    		outputHtml.append("- Siga la " + DescriptionUtils.getTypeStreetDescriptor(actualEdge,subred, netManager,routeDao) + " <b>" + lastDescription + "</b> ");
	    	    		outputHtml.append("durante " 
	    	    				+ (int) longitud
	    	    				+ " metros.");
	    // TODO JPC Gestionar el funcionamiento sin conexión a base de datos
	    	    		int giro = (int) getAnguloEntreEdges(new Edge[]{(Edge) actualEdge,(Edge) lastEdge}, p, startNodeInfo, endNodeInfo, context);
	    
	    	    		if (giro != 0){
	    	    			outputHtml.append("    Gire a la ");
	    	    			if (giro > 0){
	    	    				outputHtml.append("<b> izquierda. </b>");
	    	    			} else {
	    	    				outputHtml.append("<b> derecha. </b>");
	    	    			}
	    	    		}
	    	    		//outputFrame.addText(" y tome la calle.");
	    
	    	    		if(actualEdge instanceof ILocalGISEdge){
	    	    			longitud = ((ILocalGISEdge) actualEdge).getEdgeLength();
	    	    		} else if (actualEdge instanceof DynamicEdge){
	    	    			longitud = ((DynamicEdge) actualEdge).getCost(actualEdge.getNodeA(), 1);
	    	    		}
	    	    		i++;
	    
	    	    		outputHtml.append("</ul>");
	    	    	}
	    
	    	    	lastDescription = actualDescription;
	    	    	lastEdge = actualEdge;
	    
	    
	    	    } 
	    
	    	    if (actualDescription.equals(lastDescription)){
	    	    	outputHtml.append("<ul>");
	    	    	if (actualEdge instanceof ILocalGISEdge){
	    	    		outputHtml.append("- Siga la "+ DescriptionUtils.getTypeStreetDescriptor( actualEdge,subred, netManager,routeDao) + " <b>" + lastDescription + "</b> ");
	    	    		if (longitud <= 0){
	    	    			longitud = ((ILocalGISEdge) lastEdge).getEdgeLength();
	    	    		}
	    	    		outputHtml.append("durante " 
	    	    				+ (int) longitud
	    	    				+ " metros.");
	    	    		lastDescription = "";		
	    
	    	    	}
	    
	    	    	i++;
	    
	    
	    
	    	    	outputHtml.append("</ul>");
	    	    }
	    
	    	    outputHtml.append("<ul>");
	    
	    	    outputHtml.append("- Siga la Calle ");
	    	    outputHtml.append("durante " 
	    	    		+ (int) longitud
	    	    		+ " metros.");
	    
	    	    outputHtml.append("</ul>");
	    	    outputHtml.append("<ul><b>Ha llegado a su destino</b> </ul><br>");
	    	    return outputHtml;
	    	}
	
	


	

	

}