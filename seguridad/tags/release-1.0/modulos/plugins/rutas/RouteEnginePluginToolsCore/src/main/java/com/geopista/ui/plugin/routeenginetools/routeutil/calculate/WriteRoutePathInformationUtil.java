package com.geopista.ui.plugin.routeenginetools.routeutil.calculate;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
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
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.CardinalDirections;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.VirtualInfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl.InfoRouteDAOPostgreSQLImplementation;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;




public class WriteRoutePathInformationUtil{
	
	
	private static HashMap<String, Network> getConfiguratorNetworks(PlugInContext context){
		HashMap<String, Network> configuatorNetworks = new HashMap<String, Network>();
		CalcRutaConfigFileReaderWriter configProperties = new CalcRutaConfigFileReaderWriter();
		
		String redes[] = configProperties.getRedesNames();
		for(int i = 0; i < redes.length; i++){
			configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) FuncionesAuxiliares.getNetworkManager(context)).getAllNetworks().get(redes[i]) );
		}
		return configuatorNetworks;
	}
	
	public static void writeRoutePathInformation(HTMLFrame outputFrame, List<Edge> edges, Path p, PlugInContext context,
			VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo) {
		// TODO Auto-generated method stub
		int i = 0;
		String lastDescription = null;
		String actualDescription = "";
		Edge actualEdgeA = null;
		Edge actualEdge = null;
		Edge lastEdge = null;
		double longitud = 0;
		String subred = "";
		NetworkManager netManger = ((LocalGISNetworkManager) FuncionesAuxiliares.getNetworkManager(context));

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
				try{
					actualEdgeA = (Edge) ((ProxyEdge)actualEdgeA).getGraphable();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(actualEdgeA instanceof NetworkLink){
				UniqueIDGenerator idGen = new SequenceUIDGenerator();
				actualEdge = new LocalGISDynamicEdge(actualEdgeA.getNodeA(), actualEdgeA.getNodeB(), idGen);
				((ILocalGISEdge) actualEdge).setIdLayer(-1);
				((ILocalGISEdge) actualEdge).setIdFeature(actualEdgeA.getID());
				((ILocalGISEdge) actualEdge).setEdgeLength(((ProxyGeographicNode)actualEdgeA.getNodeA()).getPosition().distance(((ProxyGeographicNode)actualEdgeA.getNodeB()).getPosition()));

				outputFrame.append("--- CAMBIO DE RED ---");
				continue;
			}
			actualEdge = (Edge)actualEdgeA;

			
			
			Iterator<Network> nets = getConfiguratorNetworks(context).values().iterator();
			while (nets.hasNext()){
				BasicNetwork bnet = (BasicNetwork) nets.next();
				if (bnet.getGraph().getEdges().contains(actualEdge)){
					subred = bnet.getName();
					break;
				}
			}


			if (actualEdge instanceof ILocalGISEdge){

				String columnDescriptor = "";
				try{
					columnDescriptor = ((NetworkProperty) FuncionesAuxiliares.getNetworkManager(context).getNetwork(subred).getProperties().get("ColumnDescriptor")).getValue(Integer.toString(((ILocalGISEdge) actualEdge).getIdLayer()));

					if (columnDescriptor != null && !columnDescriptor.equals("")){
						actualDescription = getColumnsDescriptorFromIdLayer(((ILocalGISEdge) actualEdge).getIdLayer(), ((ILocalGISEdge) actualEdge).getIdFeature(), columnDescriptor);
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

				outputFrame.append("<ul>");

				outputFrame.append("- Siga la " + getTypeStreetDescriptor(actualEdge,subred, netManger) + " <b>" + lastDescription + "</b> ");
				outputFrame.addText("durante " 
						+ (int) longitud
						+ " metros.");

				int giro = (int) getAnguloEntreEdges(new Edge[]{(Edge) actualEdge,(Edge) lastEdge}, p, startNodeInfo, endNodeInfo, context);

				if (giro != 0){
					outputFrame.append("    Gire a la ");
					if (giro > 0){
						outputFrame.append("<b> izquierda. </b>");
					} else {
						outputFrame.append("<b> derecha. </b>");
					}
				}
				outputFrame.addText(" y tome la calle.");

				if(actualEdge instanceof ILocalGISEdge){
					longitud = ((ILocalGISEdge) actualEdge).getEdgeLength();
				} else if (actualEdge instanceof DynamicEdge){
					longitud = ((DynamicEdge) actualEdge).getCost(actualEdge.getNodeA(), 1);
				}
				i++;

				outputFrame.append("</ul>");
			}

			lastDescription = actualDescription;
			lastEdge = actualEdge;


		} 

		if (actualDescription.equals(lastDescription)){
			outputFrame.append("<ul>");
			if (actualEdge instanceof ILocalGISEdge){
				outputFrame.append("- Siga la "+ getTypeStreetDescriptor( actualEdge,subred, netManger) + " <b>" + lastDescription + "</b> ");
				if (longitud <= 0){
					longitud = ((ILocalGISEdge) lastEdge).getEdgeLength();
				}
				outputFrame.addText("durante " 
						+ (int) longitud
						+ " metros.");
				lastDescription = "";		

			}

			i++;



			outputFrame.append("</ul>");
		}

		outputFrame.append("<ul>");

		outputFrame.append("- Siga la Calle ");
		outputFrame.addText("durante " 
				+ (int) longitud
				+ " metros.");

		outputFrame.append("</ul>");
		outputFrame.append("<ul><b>Ha llegado a su destino</b> </ul><br>");
		
		
		
		int minutos = (int) (((RoutePath)p).getTotalCost() / 60) % 60 ;
		int horas = (int) (((RoutePath)p).getTotalCost() / 60 / 60);
		if (horas >0 || minutos>0){
			context.getWorkbenchFrame().getOutputFrame().addText("El tiempo estimado de la ruta es: " + horas + " Horas " + minutos + " Minutos");
		} else if (horas <= 0 && minutos <=0){
			context.getWorkbenchFrame().getOutputFrame().addText("El tiempo estimado de la ruta es: Menos de 1 minuto");
		}
	}
	
	
	private static int lastIdLayer = -1;
	private static IGeopistaLayer layer = null;
//	private static double distanciaInicio;
//	private static double distanciaFinal;
	public static double getAnguloEntreEdges(Edge[] edges, Path p, VirtualNodeInfo startNodeInfo,
			VirtualNodeInfo endNodeinfo,PlugInContext context){

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

			if ( iedge !=null ){
				if (lastIdLayer != iedge.getIdLayer()){

					layer = getOriginalLayer((Edge) iedge, context);
					if (layer != null){
						lastIdLayer = layer.getId_LayerDataBase();
					} else{
						lastIdLayer = ((ILocalGISEdge) edges[i]).getIdLayer();
					}
				}

				if (layer != null){
					originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
							getWrappee().getFeatures());
				}


				Iterator<Object> it = originalFeatures.iterator();

				Feature featureedges = null;
				GeopistaFeature f2 = null;
				while (it.hasNext()){
					f2 = (GeopistaFeature) it.next();
					if (Integer.parseInt(f2.getSystemId()) == iedge.getIdFeature()){
						if (i == 0){
							primerFeature = f2;
							break;
						} else if (i == 1){
							segundaFeature = f2;
							break;
						}
					}
				}
			}

		}


		LineString primerLineString = null;
		LineString segundoLineString = null;
		if (primerFeature != null && segundaFeature != null){

			if (primerFeature.getGeometry() instanceof LineString){
				primerLineString = (LineString) primerFeature.getGeometry();
			}else if (primerFeature.getGeometry() instanceof MultiLineString){
				Coordinate c0= primerFeature.getGeometry().
				getGeometryN(primerFeature.getGeometry().getNumGeometries()-1).
				getCoordinates()[0];
				Coordinate c1=primerFeature.getGeometry().
				getGeometryN(primerFeature.getGeometry().getNumGeometries()).
				getCoordinates()[1];
				Coordinate[] coordmls =new Coordinate[] {
						c0, 
						c1};

				CoordinateSequence seq= new CoordinateArraySequence(coordmls);
				primerLineString=new LineString(seq, new GeometryFactory());
			}


			if (segundaFeature.getGeometry() instanceof LineString){
				segundoLineString = (LineString) segundaFeature.getGeometry();
			}else if (segundaFeature.getGeometry() instanceof MultiLineString){
				Coordinate c0= segundaFeature.getGeometry().
				getGeometryN(segundaFeature.getGeometry().getNumGeometries()-1).
				getCoordinates()[0];
				Coordinate c1=segundaFeature.getGeometry().
				getGeometryN(segundaFeature.getGeometry().getNumGeometries()).
				getCoordinates()[1];
				Coordinate[] coordmls =new Coordinate[] {
						c0, 
						c1};

				CoordinateSequence seq= new CoordinateArraySequence(coordmls);
				segundoLineString=new LineString(seq, new GeometryFactory());
			}


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
		layerID = getQueryFromIdLayer(conn, ((ILocalGISEdge) edge_camino).getIdLayer());
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
	
	
	private static String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column){
		if(column == null || column.equals("SIN TIPO")) return "";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
		Connection con = connectionFactory.getConnection();
		try {
			String sqlQuery = "SELECT " + column  +" FROM " + getQueryFromIdLayer(con, idLayer) + " resultTable where resultTable.id = " + idFeature ;
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getString(column);
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return "";
	}
	
	private static String getTypeStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr){
		String typeColumnDescriptor = "";
		String typeDescription = "calle";
		if (edge instanceof LocalGISStreetDynamicEdge){
			try{
				LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) edge;
				typeColumnDescriptor = ((NetworkProperty) networkMgr.getNetwork(getRedNameFromSubRed(subred,networkMgr)).getProperties().
						get("TypeColumnDescriptor")).getValue(
								Integer.toString(streetEdge.getIdLayer())
						);

				if (typeColumnDescriptor != null && !typeColumnDescriptor.equals("")){
					typeDescription = getColumnsDescriptorFromIdLayer(streetEdge.getIdLayer(), streetEdge.getIdFeature(), typeColumnDescriptor);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return typeDescription;
	}
	
	
	private static String getRedNameFromSubRed(String subred, NetworkManager networkMgr) {
		// TODO Auto-generated method stub
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
	
	

	
	
	


	

	

}