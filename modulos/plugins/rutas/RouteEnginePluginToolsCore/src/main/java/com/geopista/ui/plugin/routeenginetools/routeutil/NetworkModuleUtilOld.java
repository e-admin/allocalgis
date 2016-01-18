/**
 * NetworkModuleUtilOld.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.io.IOException;
import java.lang.ref.WeakReference;
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

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.batik.bridge.UpdateManager;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.managers.ChangesMonitoredMemoryManager;
import org.uva.routeserver.managers.GraphMemoryManager;
import org.uva.routeserver.managers.UpdateMonitor;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.localgis.route.graph.build.LocalGisBasicLineGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISStreetBasicLineGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
@Deprecated
public class NetworkModuleUtilOld
{
    public static boolean comprobarTablaTramos(Connection conn)
    {
	String tableTramos = "select * from network_edges limit 1";
	PreparedStatement tramosPreparedStatement = null;
	try
	    {
		tramosPreparedStatement = conn.prepareStatement(tableTramos);
		tramosPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			tramosPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    public static boolean comprobarTablaNodos(Connection conn)
    {
	// TODO Auto-generated method stub
	String tableNodos = "select * from network_nodes limit 1";
	PreparedStatement nodosPreparedStatement = null;
	try
	    {
		nodosPreparedStatement = conn.prepareStatement(tableNodos);
		nodosPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			nodosPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    public static boolean comprobarTablaSubRedes(Connection conn)
    {
	String tableSubredes = "select * from networks limit 1";
	PreparedStatement subredesPreparedStatement = null;
	try
	    {
		subredesPreparedStatement = conn.prepareStatement(tableSubredes);
		subredesPreparedStatement.executeQuery();
	    } catch (SQLException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return false;
	    }
	finally
	    {
		try
		    {
			subredesPreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
	return true;
    }

    /**
     * Crea el modelo de soporte para redes si es necesario Solo para depuración o versiones previas a Localgis 3.0
     * 
     * @deprecated 3.0
     * @param conn
     */
    public static void createNetworkModel(Connection conn)
    {
	boolean tableSubredesExist = NetworkModuleUtilOld.comprobarTablaSubRedes(conn);
	boolean tableNodosExist = NetworkModuleUtilOld.comprobarTablaNodos(conn);
	boolean tableTramosExist = NetworkModuleUtilOld.comprobarTablaTramos(conn);
	if (!tableSubredesExist)
	    {
		crearTablaSubredes(conn);
	    }
	if (!tableNodosExist)
	    {
		crearTablaNodos(conn);
	    }
	if (!tableTramosExist)
	    {
		crearTablaTramos(conn);
	    }
    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaSubredes(Connection conn)
    {
	PreparedStatement subredesCreatePreparedStatement = null;
	String createTablesubredes = "CREATE TABLE networks(id_network serial NOT NULL," + "network_name character(20) NOT NULL unique,"
		+ "comment character(100)," + "CONSTRAINT networks_pkey PRIMARY KEY (id_network) );" + "ALTER TABLE networks OWNER TO geopista;";
	try
	    {
		subredesCreatePreparedStatement = conn.prepareStatement(createTablesubredes);
		subredesCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		throw new RuntimeException("No puedo crear tabla para soporte de redes.");
	    }
	finally
	    {
		try
		    {
			subredesCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }

    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaNodos(Connection conn)
    {

	PreparedStatement nodosCreatePreparedStatement = null;
	String tablanodos = "CREATE TABLE network_nodes(" + "id_network integer NOT NULL," + "id_node integer NOT NULL," + "node_geometry geometry,"
		+ "CONSTRAINT network_nodes_pkey PRIMARY KEY (id_network, id_node));" + "ALTER TABLE network_nodes OWNER TO geopista";

	try
	    {
		nodosCreatePreparedStatement = conn.prepareStatement(tablanodos);
		nodosCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		throw new RuntimeException("No puedo crear tabla para soporte de nodos.");
	    }
	finally
	    {
		try
		    {
			nodosCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }
    }

    /**
     * TODO Los modelos de datos se deben instalar desde el servidor y no en una aplicación de cliente
     * 
     * @deprecated
     */
    @Deprecated
    public static void crearTablaTramos(Connection conn)
    {
	PreparedStatement tramosCreatePreparedStatement = null;
	String tablatramos = "CREATE TABLE network_edges(" + "id_network integer NOT NULL," + "id_edge integer NOT NULL," + "id_nodea integer NOT NULL,"
		+ "id_network_nodea integer NOT NULL," + "id_nodeb integer NOT NULL," + "id_network_nodwb integer NOT NULL,"
		+ "edge_length double precision NOT NULL," + "id_layer int4," + "id_feature numeric(8),"
		+ "CONSTRAINT network_edges_pkey PRIMARY KEY (id_network, id_edge));" + "ALTER TABLE network_edges OWNER TO geopista";

	try
	    {
		tramosCreatePreparedStatement = conn.prepareStatement(tablatramos);
		tramosCreatePreparedStatement.execute();
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		try
		    {
			tramosCreatePreparedStatement.close();
		    } catch (Exception e)
		    {
		    }
		;
		try
		    {
			conn.close();
		    } catch (Exception e)
		    {
		    }
		;
	    }

    }

    public static final int _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY = 100000;

    /**
     * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
     * 
     * @return
     */
    public static GraphGenerator getBasicLineGraphGenerator()
    {
	return getBasicLineGraphGenerator(AppContext.getIdEntidad());
    }

    /**
     * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
     * 
     * @return
     */
    public static GraphGenerator getLocalGISStreetGraphGenerator()
    {
	return getLocalGISStreetGraphGenerator(AppContext.getIdEntidad());
    }

    /**
     * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
     * 
     * @return
     */
    public static GraphGenerator getLocalGISStreetBasicLineGraphGenerator()
    {
	return getLocalGISStreetBasicLineGraphGenerator(AppContext.getIdEntidad());
    }

    /**
     * Crea un LocalGisBasicLineGraphGenerator con un plan de numeración de elementos basado en el código de entidad Reserva
     * {@link #_MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY}={@value #_MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY} elementos en cada entidad.
     * 
     * @param entidad
     * @return
     */
    private static GraphGenerator getBasicLineGraphGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISGraphBuilder(uidGenerator);
	LocalGisBasicLineGraphGenerator generator = new LocalGisBasicLineGraphGenerator(graphBuilder);
	return generator;
    }

    private static GraphGenerator getLocalGISStreetGraphGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISStreetGraphBuilder(uidGenerator);
	LocalGISStreetGraphGenerator generator = new LocalGISStreetGraphGenerator(graphBuilder);
	return generator;

    }

    private static GraphGenerator getLocalGISStreetBasicLineGraphGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = getUIDGenerator(entidad);
	LocalGISGraphBuilder graphBuilder = new LocalGISStreetGraphBuilder(uidGenerator);
	LocalGISStreetBasicLineGenerator generator = new LocalGISStreetBasicLineGenerator(graphBuilder);
	return generator;

    }

    public static SequenceUIDGenerator getUIDGenerator(int entidad)
    {
	SequenceUIDGenerator uidGenerator = new SequenceUIDGenerator();
	uidGenerator.setSeq(entidad * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY);
	uidGenerator.setMaxId((entidad + 1) * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY - 1);
	return uidGenerator;
    }

    /**
     * Devuelve la geometría con esta prioridad:
     * 1- geometria almacenada en el edge
     * 2- geometria de la feature almacenada
     * 3- geometria de una capa del workbench
     * 4- representación mediante un segmento
     * 
     * @param edge
     * @return null o la geometria. Si es un linestring se orienta en la direccion del eje
     */
    public static Geometry getBestGeometryFromEdgeLocal(Edge edge, PlugInContext context)
    {
	Edge theEdge = (Edge) NodeUtils.unwrapProxies(edge);
	Geometry geom = null;
	if (theEdge instanceof EquivalentEdge)
	    theEdge = (Edge) ((EquivalentEdge) theEdge).getEquivalentTo();
	
	geom = findLocalGeometry(theEdge, context);

	if (geom == null)
	    {
		Coordinate[] coords = NodeUtils.CoordenadasArco(theEdge, null, 0, 0);
		GeometryFactory fact = new GeometryFactory();
		geom = fact.createLineString(coords);
	    
		if (geom != null && geom instanceof LineString) // reorienta geometria si hace falta
		    {
			geom = NetworkModuleUtilOld.checkLineStringOrientation(theEdge, (LineString) geom);
		    }
	    }
	return geom;
    }
/**
 * Busca la geometría local
 * * Devuelve la geometría con esta prioridad:
     * 1- geometria almacenada en el edge
     * 2- geometria de la feature almacenada
     * 3- geometria de una capa del workbench
 * @param theEdge
 * @param context
 * @return devuelve null si no encuentra la geometria. Si es un linestring se orienta en la direccion del edge
 */
    public static Geometry findLocalGeometry(Edge edge,  PlugInContext context)
    {
	Edge theEdge = (Edge) NodeUtils.unwrapProxies(edge);
	if (theEdge instanceof EquivalentEdge)
	    theEdge = (Edge) ((EquivalentEdge) theEdge).getEquivalentTo();
	Geometry geom = null;
	if (theEdge instanceof ILocalGISEdge)
	    {
		ILocalGISEdge localgisEdge = (ILocalGISEdge) theEdge;
		if (localgisEdge.getGeometry() != null)
		    {
			geom = localgisEdge.getGeometry();
		    } else if (localgisEdge.getFeature() != null)
		    {
			geom = getEdgeLineStringFromGeometry(localgisEdge.getFeature().getGeometry());
		    } else if (context != null)
		    {
			Feature feature = findFeatureForEdge(theEdge, context);
			if (feature != null)
			    {
				//context.getWorkbenchGuiComponent().warnUser("Feature obtenida de capa local");
				geom = getEdgeLineStringFromGeometry(feature.getGeometry());
			    }
		    }
	    }
	if (geom != null && geom instanceof LineString) // reorienta geometria si hace falta
	    {
		geom = NetworkModuleUtilOld.checkLineStringOrientation(theEdge, (LineString) geom);
	    }
	return geom;
    }

    /**
     * Reduce la geometria a un linestrng adecuado para representar un Edge
     * 
     * @param geometryObject
     * @return
     * @throws IllegalArgumentException
     *             si la geometria no se puede simplificar adecuadamente
     */
    public static LineString getEdgeLineStringFromGeometry(Geometry geometryObject)
    {
	LineString lineStringFromGeometry = null;
	if (geometryObject instanceof MultiLineString)
	    {
		MultiLineString mls = (MultiLineString) geometryObject;
		if (!(mls.isEmpty()))
		    {

			Coordinate[] coordmls = mls.getCoordinates();// FIX No se debe sobre-simplificar a un linestring lineal!

			// Coordinate[] coordmls= null;
			// //add ls built for the first and last coordinate.
			// Coordinate c0=mls.getGeometryN(0).getCoordinate();
			// Coordinate c2=mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates()[
			// mls.getGeometryN(mls.getNumGeometries()-1).getCoordinates().length -1
			// ];
			// coordmls=new Coordinate[] {c0, c2};
			CoordinateSequence seq = new CoordinateArraySequence(coordmls);
			lineStringFromGeometry = new LineString(seq, new GeometryFactory());
		    }

	    } else if (geometryObject instanceof LineString)
	    {
		lineStringFromGeometry = (LineString) geometryObject;
	    } else
	    {
		throw new IllegalArgumentException("Geometry " + geometryObject + " is not compatible with Linestring.");
	    }
	return lineStringFromGeometry;
    }

    /**
     * Crea un Graph con gestión de LocalGIS
     * 
     * @param graph
     * @return
     */
    public static DynamicGraph getNewInMemoryGraph(Graph graph)
    {
	try
	    {
		LocalGISAllinMemoryManager memmgr = new LocalGISAllinMemoryManager(new NullStore());
		memmgr.setAutoCommit(false);
		DynamicGraph subnetGraph = new DynamicGraph(memmgr);
		if (graph != null)
		    subnetGraph.getMemoryManager().appendGraph(graph);
		return subnetGraph;
	    } catch (IOException e)
	    {
		throw new RuntimeException(e);
	    }
    }

    public static Network addNewNetwork(NetworkManager networkMgr, String nombrered, Graph graph, Map<String, Object> networkProperties)
    {
	/*
	 * TODO hacer cambios de basicInternetWorker a null cuando obtienes FuncionesAuxiliares.getNetworManager()
	 */
	if (((BasicNetworkManager) networkMgr).getInterNetworker() == null)
	    {
		BasicInterNetworker binNet = new BasicInterNetworker();
		binNet.setNetworkManager(networkMgr);
		networkMgr.setInterNetworker(binNet);
	    }

	Network parent = networkMgr.putNetwork(nombrered, graph);

	// Cargo las propiedades de la red.
	for (int i = 0; i < networkProperties.keySet().size(); i++)
	    {
		String key = (String) networkProperties.keySet().toArray()[i];
		NetworkProperty value = (NetworkProperty) networkProperties.get(key);
		parent.getProperties().put(key, value);
	    }
	return parent;
    }

    /**
     * Busca la feature del Edge con estas prioridades. 1) campo feature 2) capas locales del workbench 3) capas del administrador de cartografía Se asigna la
     * feature al edge tras la búsqueda
     * 
     * @param edge
     * @return
     */
    public static Feature findFeatureForEdge(Edge edge, PlugInContext context)
    {
	edge = (Edge) NodeUtils.unwrapProxies(edge);
	if (edge instanceof NetworkLink)
	    {
		return null;
	    } else if (edge instanceof ILocalGISEdge)
	    {
		ILocalGISEdge localGisEdge = (ILocalGISEdge) edge;

		// primero la feature registrada
		Feature feature = localGisEdge.getFeature();
		if (feature != null)
		    return feature;
		// segundo busco en el workbench
		feature = searchEdgeFeatureInWorkbench(localGisEdge, context);
		if (feature != null)
		    {
			localGisEdge.setFeature(feature);
			return feature;
		    }

		// tercero la feature de la base de datos
		if (AppContext.getApplicationContext().isOnline())
		    {
			feature = getEdgeFeatureFromDatabase(localGisEdge, context);
			// TODO cache Layer o obtiene solo Feature
			if (feature != null)
			    {
				localGisEdge.setFeature(feature);
				return feature;
			    }
		    }
	    }
	return null;
    }

    protected static int lastIdLayer = -1;
    protected static WeakReference<IGeopistaLayer> lastLayer = new WeakReference<IGeopistaLayer>(null);

    /**
     * Obtiene una capa de la base de datos y la cachea en un {@link WeakReference} Busca la feature con el Id adecuado
     * 
     * @param localGisEdge
     * @param context
     * @return
     */
    public static Feature getEdgeFeatureFromDatabase(ILocalGISEdge localGisEdge, PlugInContext context)
    {
	IGeopistaLayer layer = null;
	int idLayer = localGisEdge.getIdLayer();
	if (lastIdLayer != idLayer || 
	  (lastLayer.get()!=null && !context.getWorkbenchContext().getLayerManager().getLayers().contains(lastLayer.get()))
	  
	) // se pide una capa nueva
	    {
		// rescata mini cache
		layer = lastLayer.get();

		if (layer == null // trata de cargarla porque no hay referencia en weakreference
		|| !context.getWorkbenchContext().getLayerManager().getLayers().contains(layer)) // la cache es una capa con el mismo id pero de otro LayerViewPanel
		    {
			// try to locate loaded systemlayer
			GeopistaRouteConnectionFactoryImpl fact = new GeopistaRouteConnectionFactoryImpl();
			Connection con = fact.getConnection();

			String layerName = NetworkModuleUtilOld.getLayerIDFromIntIdLayer(con, idLayer);
			layer = (GeopistaLayer) context.getLayerManager().getLayer(layerName);
			if (layer == null && !"".equals(layerName))
			    {
				layer = loadSystemLayerReferencedByEdge(localGisEdge, context);
			    }
			lastIdLayer = idLayer;
			lastLayer = new WeakReference<IGeopistaLayer>(layer);
		    }
	    }
	// Busca el elemento
	if (layer != null)
	    {
		ArrayList<Object> originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().getWrappee().getFeatures());

		Iterator it = originalFeatures.iterator();
		Feature featureedge = null;
		GeopistaFeature f2 = null;
		while (it.hasNext())
		    {
			f2 = (GeopistaFeature) it.next();
			if (Integer.parseInt(f2.getSystemId()) == localGisEdge.getIdFeature())
			    {
				return f2;
			    }
		    }
	    }
	// BUG JPC ignoro esta funcionalidad
	// else
	// {
	// lastIdLayer = localGisEdge.getIdLayer();
	// }
	return null;
    }

    public static IGeopistaLayer loadSystemLayerReferencedByEdge(ILocalGISEdge localGisEdge, PlugInContext context)
    {
	IGeopistaLayer layer;
	layer = getOriginalLayer(localGisEdge, context); // TODO obtener únicamente la feature no toda la capa
	context.getLayerManager().addLayer(layer.getSystemId(), layer);
	return layer;
    }
    public static IGeopistaLayer loadSystemLayerInWorkbench(String systemId,PlugInContext context)
    {
	IGeopistaLayer layer=loadSystemLayer(systemId, context);
	context.getLayerManager().addLayer(layer.getSystemId(), layer);
	return layer;
    }
    static GeopistaLayer cachedLayerForSearch = null;

    public static Feature searchEdgeFeatureInWorkbench(ILocalGISEdge localGisEdge, PlugInContext context)
    {
try{
	List layers = context.getLayerManager().getLayers();
	GeopistaLayer geopistaLyr = null;
	List<GeopistaLayer> layerToSearch = new ArrayList<GeopistaLayer>();
	boolean bypassCache=false;
	if (bypassCache==false && localGisEdge.getIdFeature()!=0 && localGisEdge.getIdLayer()!=0 &&
		cachedLayerForSearch != null
		&& ( (cachedLayerForSearch.isLocal() && cachedLayerForSearch.getId_layer() == localGisEdge.getIdLayer())
			|| cachedLayerForSearch.getId_LayerDataBase() == localGisEdge.getIdLayer())
		&& layers.contains(cachedLayerForSearch))
	    {
		geopistaLyr = cachedLayerForSearch;
	    }

	if (geopistaLyr != null)
	    {
		layerToSearch.add(geopistaLyr); // Busca en la cacheada
	    } else
	    {
		if (localGisEdge.getIdLayer() == 0)// no esta registrada una capa de base de datos buscamos en las locales
		    {
			for (Object lyrObj : context.getLayerManager().getLayers())
			    {
				if (lyrObj instanceof GeopistaLayer)
				    {
					GeopistaLayer lyr = (GeopistaLayer) lyrObj;
					if (lyr.isLocal())
					    layerToSearch.add(lyr);
				    }
			    }
		    } else
		    // busca la capa de sistema indicada
		    {
			for (Object lyr : layers)
			    {
				if (lyr instanceof GeopistaLayer)
				    {
					GeopistaLayer lgLyr = (GeopistaLayer) lyr;
					if (lgLyr.getId_LayerDataBase() == localGisEdge.getIdLayer())
					    {
						cachedLayerForSearch = geopistaLyr = lgLyr;
						layerToSearch.add(lgLyr);
					    }
				    }
			    }
		    }
	    }

	// Busca en todas las candidatas
	for (GeopistaLayer layer : layerToSearch)
	    {
		List featureList = layer.getFeatureCollectionWrapper().getFeatures();
		// determina posible presencia del atributo idEje
		Integer idEjeIndex=null; 
		try{
		    idEjeIndex=layer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("idEje");
		}catch(IllegalArgumentException ex)
		{
		// no existe el atributo    
		}
		for (Object ftr : featureList)
		    {
			Feature feature = (Feature) ftr;
			if (feature instanceof GeopistaFeature && !layer.isLocal() && !((GeopistaFeature) feature).isTempID())
			    {
				GeopistaFeature geopFeat = (GeopistaFeature) feature;
				if (!geopFeat.isTempID())
				    {
					if (Integer.parseInt(geopFeat.getSystemId()) == localGisEdge.getIdFeature())
					    {
						cachedLayerForSearch = layer;
						return feature;
					    }
				    }
			    } else
			    {
				if (idEjeIndex==null)
				    continue;
				Object featIdObj=feature.getAttribute(idEjeIndex);
				
				if (feature.getID() == localGisEdge.getIdFeature() ||
					(idEjeIndex!=null && featIdObj instanceof Integer &&
					localGisEdge.getID()==((Integer)featIdObj))
					)
				    {
					cachedLayerForSearch = layer;
					return feature;
				    }
				
			    }

		    }

	    }
}catch(IllegalArgumentException ex)
{
    ex.printStackTrace();// una feature no tiene identificador válido?
}
	return null;
	
    }

    /**
     * @param edge
     * @return
     */
    public static IGeopistaLayer getOriginalLayer(Edge edge, PlugInContext context)
    {
	IGeopistaLayer layer;
	edge = (Edge) NodeUtils.unwrapProxies(edge);
	GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
	Map properties = new HashMap();
	// Introducimos el mapa Origen
	properties.put("mapadestino", (GeopistaMap) context.getTask());
	// Introducimos el fitro geometrico si es distinto de null, si se introduce null falla
	// properties.put("filtrogeometrico",null);
	// Introducimos el FilterNode
	properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
	// Introducimos el srid del mapa destino
	properties.put("srid_destino", Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode()));
	serverDataSource.setProperties(properties);
	GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();
	// preparamos la url de la layer
	Connection conn = new GeopistaRouteConnectionFactoryImpl().getConnection();
	
	String layerID = getQueryFromIdLayer(conn, ((ILocalGISEdge) edge).getIdLayer());
	layer = loadSystemLayer(layerID, context);

	return layer;
    }

    public static IGeopistaLayer loadSystemLayer(String layerID, PlugInContext context)
    {
	GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
	Map properties = new HashMap();
	// Introducimos el mapa Origen
	properties.put("mapadestino", (GeopistaMap) context.getTask());
	// Introducimos el fitro geometrico si es distinto de null, si se introduce null falla
	// properties.put("filtrogeometrico",null);
	// Introducimos el FilterNode
	properties.put("nodofiltro", FilterLeaf.equal("1", new Integer(1)));
	// Introducimos el srid del mapa destino
	properties.put("srid_destino", Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode()));
	serverDataSource.setProperties(properties);
	IGeopistaLayer layer;
	URL urlLayer = null;
	try
	    {
		urlLayer = new URL("geopistalayer://default/" + layerID);
	    } catch (MalformedURLException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	Collection exceptions=new ArrayList();
	GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource.getConnection();
	geopistaConnection.executeQuery(urlLayer.toString(), exceptions, null);
	layer = geopistaConnection.getLayer();
	DataSourceQuery dataSourceQuery = new DataSourceQuery();
    	dataSourceQuery.setQuery(urlLayer.toString());
    	dataSourceQuery.setDataSource(serverDataSource);
    	layer.setDataSourceQuery(dataSourceQuery);
	return layer;
    }

    public static String getQueryFromIdLayer(Connection con, int idLayer)
    {

	String unformattedQuery = "";
	String sqlQuery = "select name from layers where id_layer = " + idLayer;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try
	    {
		if (con != null)
		    {
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			rs = preparedStatement.executeQuery();

			if (rs.next())
			    {
				unformattedQuery = rs.getString("name");
			    }
			preparedStatement.close();
			rs.close();
		    }
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	return unformattedQuery;

    }

    public static enum Operations {
	LOAD, WRITE, LOAD_MERGING
    };

    /**
     * Se enlaza la base de datos con la red Se cargan los elementos que haya en la base de datos. se fusionan lso elementos antiguos de la red renumerándose Se
     * guardan los elementos antiguos en la base de datos.
     * 
     * Las propiedades de la red se mezclan con las existentes. Tiene prioridad las prioridades de la red en memoria.
     * 
     * @param nuevoNombreParaRed
     * @param selectedNet
     * @param monitor
     * @param toFile
     *            TODO
     * @return
     * @throws SQLException
     * @throws IOException
     */
//    @SuppressWarnings("unchecked")
//    public static boolean linkNetworkToStore(String nuevoNombreParaRed, Network selectedNet, Operations operation, boolean toFile, TaskMonitor monitor)
//	    throws IOException
//    {
//	boolean renumbering = false; // disable graphable renumbering
//
//	// Si la red ya está conectada a la base de datos y gestionada por LocalgisAllInMemoryManager
//	Graph selectedGraph = selectedNet.getGraph();
//	LocalGISAllinMemoryManager lgMemmgr = NetworkModuleUtilOld.castToLocalGISAllinMemoryManager(selectedGraph);
//	if (lgMemmgr == null)
//	    { // Sustituye el memoryManager
//		DynamicGraph dynGraph = NetworkModuleUtilOld.getNewInMemoryGraph(selectedGraph);
//		selectedNet.setGraph(dynGraph);
//		lgMemmgr = (LocalGISAllinMemoryManager) dynGraph.getMemoryManager();
//	    }
//
//	lgMemmgr.setTaskMonitor(monitor);
//	boolean linkedToDatabase = lgMemmgr.isLinkedToDatabase();
//	if (operation==Operations.WRITE ||
//		(!linkedToDatabase && toFile == false) || (!lgMemmgr.isLinkedToFile() && toFile == true)
//		|| !nuevoNombreParaRed.equals(lgMemmgr.getStoreNetworkName()))
//	    {
//		if (operation==Operations.LOAD)
//		    {
//			if (!toFile) // precarga las propiedades para conocer los parámetros que necesita LocalgisRouteReadeWriter
//			    {
//			try
//			    {
//			
//				RouteConnectionFactory fact = new GeopistaRouteConnectionFactoryImpl();
//				Map<String, Object> props = NetworkModuleUtilOld.readNetworkPropertiesFromDB(nuevoNombreParaRed, fact.getConnection());
//				selectedNet.addProperties(props);
//			    } catch (SQLException e)
//			    {
//				throw new IOException(e);
//			    }
//			    }
//		    }
//		// preserva contenido actual. Usamos un nuevo ArrayList porque la colección se modifica al cargar la red
//		Collection<Edge> prevEdges = new ArrayList<Edge>(lgMemmgr.getEdges());
//		Collection<Node> prevNodes = new ArrayList<Node>(lgMemmgr.getNodes());
//		BasicGraph previousGraph = new BasicGraph(prevNodes, prevEdges);
//		if (toFile)
//		    {
//			lgMemmgr.linktoFile(nuevoNombreParaRed);
//		    } else
//		    {
//
//			// modifica la red para que pase a utilizar un readerwriter de red
//			lgMemmgr.linkToDataBase(nuevoNombreParaRed, selectedNet);
//			// Asigna el networkId a los elementos previos
//			int networkId = lgMemmgr.getStoreNetworkId();
//			NetworkModuleUtilOld.updateLocalGISEdgesWithNetworkId(previousGraph, networkId);
//		    }
//
//		// load properties
//		if (lgMemmgr.getStore() instanceof NetworkPropertiesReaderWriter)
//		    {
//			NetworkPropertiesReaderWriter lgNetPropsStore = (NetworkPropertiesReaderWriter) lgMemmgr.getStore();
//			Map<String, Object> props = lgNetPropsStore.readNetworkProperties();// NetworkModuleUtil.readNetworkPropertiesFromDB(netWorkName,connectionFactory.getConnection()
//
//			Map<String, Object> propsPrev = selectedNet.getProperties();
//			if (props != null && (operation == Operations.LOAD_MERGING)) // mezcla las propiedades
//			    {
//				selectedNet.addProperties(props);
//				if (propsPrev != null) // asegura que las propiedades locales quedan por encima
//				    {
//					selectedNet.addProperties(propsPrev);
//				    }
//			    } else if (props != null && operation == Operations.LOAD)
//			    {
//				selectedNet.getProperties().clear();
//				selectedNet.addProperties(props);
//			    }
//
//			if (operation == Operations.WRITE)
//			    {
//				lgNetPropsStore.writeNetworkProperties(selectedNet.getProperties());
//			    }
//		    }
//
//		if (operation == Operations.WRITE)// clear contents
//		    {
//			lgMemmgr.eraseGraph();
//		    }
//		Collection<Edge> loadedEdges = Collections.EMPTY_LIST;
//		Collection<Node> loadedNodes = Collections.EMPTY_LIST;
//		if (operation == Operations.LOAD || operation == Operations.LOAD_MERGING)
//		    {
//			monitor.report("Cargando contenido pre-existente de la red.");
//			// carga el contenido de la base de datos si existe
//			loadedEdges = lgMemmgr.getEdges();
//			loadedNodes = lgMemmgr.getNodes();
//		    }
//		// AÃ±ade los elementos previos para forzar la grabacion con el commit
//
//		if (operation == Operations.LOAD_MERGING && renumbering)
//		    if (!prevEdges.isEmpty()) // proceso de copia de elementos
//			{
//
//			    BasicGraph justLoadedGraph = new BasicGraph(loadedNodes, loadedEdges);
//			    // renumerar todos los elementos y fijar el UIDGenerator para el futuro.
//			    SequenceUIDGenerator uid = renumberElements(previousGraph, justLoadedGraph);
//			    lgMemmgr.setUIDGenerator(uid);
//
//			}
//		lgMemmgr.getUpdateMonitor().reset(); // todos los elementos se deben actualizar
//		lgMemmgr.appendGraph(previousGraph);// Add the nodes that was previously in the graph
//		lgMemmgr.commit();
//	    } else
//	    {// por petición del usuario
//		lgMemmgr.commit();
//	    }
//	// Asegura que el nombre de la red es el nuevo
//if (!selectedNet.getName().equals(nuevoNombreParaRed))
//    {
//	NetworkManager networkManager = selectedNet.getNetworkManager();
//	if(networkManager.getNetwork(nuevoNombreParaRed)!=null)
//	    networkManager.detachNetwork(nuevoNombreParaRed);// elimina cualquier otra red con esta denominación
//	selectedNet.setName(nuevoNombreParaRed);// renombra la red cargada
//	networkManager.putNetwork(selectedNet);
//    }
//
//return true;
//
//    }

    /**
     * Cambia los identificadores del grafo toBeRenumberedGraph El {@link UpdateManager} que pueda tener el grafo puede contener elementos duplicados al haber
     * cambiado la clave principal de los objetos, pero no se hace nada al respecto WARNING!.
     * 
     * @param referenceGraph
     * @param toBeRenumberedGraph
     * @return {@link SequenceUIDGenerator} con la numeración ajustada para continuar trabajando en el grafo.
     */
    public static SequenceUIDGenerator renumberElements(Graph referenceGraph, Graph toBeRenumberedGraph)
    {
	SequenceUIDGenerator uid = getUIDGenerator(toBeRenumberedGraph, AppContext.getIdEntidad());

	Collection<Node> previousNodes = referenceGraph.getNodes();
	Collection<Edge> previousEdges = referenceGraph.getEdges();

	for (Node node : previousNodes)
	    {
		node.setID(uid.getNodeUniqueID());
	    }
	for (Edge edge : previousEdges)
	    {
		edge.setID(uid.getEdgeUniqueID());
	    }
	return uid;
    }

    /**
     * Calcula la secuencia con el máximo identificador presente en el grafo
     * 
     * @param selectedGraph
     * @return
     */
    public static SequenceUIDGenerator getUIDGenerator(Graph selectedGraph, int entidad)
    {
	int max = entidad * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY;
	for (Edge edge : selectedGraph.getEdges())
	    {
		max = Math.max(max, edge.getID());
	    }
	for (Node node : selectedGraph.getNodes())
	    {
		max = Math.max(max, node.getID());
	    }
	SequenceUIDGenerator uidGenerator = new SequenceUIDGenerator();
	uidGenerator.setSeq(max + 1);
	uidGenerator.setMaxId((entidad + 1) * _MAX_NETWORK_ELEMENTS_PER_LOCALGIS_ENTITY - 1);
	return uidGenerator;
    }

    /**
     * Extrae el memorymanager de tip {@link LocalGISAllinMemoryManager}
     * 
     * @param graph
     * @return null si no es un grafo gestionado por {@link LocalGISAllinMemoryManager}
     */
    public static LocalGISAllinMemoryManager castToLocalGISAllinMemoryManager(Graph graph)
    {

	if (graph instanceof DynamicGraph)
	    {
		DynamicGraph dynGraph = (DynamicGraph) graph;
		GraphMemoryManager memMgr = dynGraph.getMemoryManager();
		if (memMgr instanceof LocalGISAllinMemoryManager)
		    {
			return (LocalGISAllinMemoryManager) memMgr;
		    }
	    }
	return null;
    }

    /**
     * Guarda las propiedades en el almacenamiento
     * 
     * @param selectedNet
     */
    public static void writeNetworkProperties(Network selectedNet) throws IOException
    {
	LocalGISAllinMemoryManager lgMgr = castToLocalGISAllinMemoryManager(selectedNet.getGraph());
	if (lgMgr != null)
	    if (lgMgr.getStore() instanceof LocalGISRouteReaderWriter)
		((LocalGISRouteReaderWriter) lgMgr.getStore()).writeNetworkProperties(selectedNet.getProperties());
    }
    /**
     * Lee las propiedades del almacenamiento usando el REaderWriter
     * No modifica las que tenga el parametro selectedNet
     * @param selectedNet
     * @throws IOException
     */
//    public static  Map<String, Object> readNetworkProperties(Network selectedNet) throws IOException
//    {
//	LocalGISAllinMemoryManager lgMgr = castToLocalGISAllinMemoryManager(selectedNet.getGraph());
//	if (lgMgr != null)
//	    if (lgMgr.getStore() instanceof LocalGISRouteReaderWriter)
//		return ((LocalGISRouteReaderWriter) lgMgr.getStore()).readNetworkProperties();
//   
//    return null;
//    }
//    public static Map<String, Object> readNetworkPropertiesFromDB(String netWorkName, Connection connection) throws SQLException
//    {
//	LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
//
//	return dao.readNetworkProperties(netWorkName, connection);
//    }

    /**
     * Borra todos los eleentos de la red
     * 
     * @param userSelectedNetworkName
     * @throws SQLException
     */
    public static void clearNetwork(String networkName) throws SQLException
    {
	RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
	LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
	Connection connection = routeConnection.getConnection();
	Integer idnetwork = dao.getNetworkId(networkName, connection);
	dao.deleteNetworkEdgesFromDataBaseById(idnetwork, connection);
	dao.deleteNetworkNodesFromDataBaseById(idnetwork, connection);
	ConnectionUtilities.closeConnection(connection);
    }

//    public static Layer createGraphLayer(DynamicGraph graph, final PlugInContext context, String subred, String categoryName) throws SQLException,
//	    NoSuchAuthorityCodeException, IOException, FactoryException, ElementNotFoundException
//    {
//
//	FeatureCollection edgesFeatureCol = getFeatureCollectionForEdges(subred, graph, context);
//
//	// // creo capas con los arcos
//	// for (Iterator iter_edges = edges.iterator(); iter_edges.hasNext();) {
//	// Edge edge = (Edge) iter_edges.next();
//	// Coordinate[] coords = NodeUtils.CoordenadasArco(edge,
//	// null, 0, 0);
//	//
//	// if (edgesFeatureCol == null){
//	// if (edge instanceof ILocalGISEdge){
//	// edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
//	// }else if(edge instanceof LocalGISStreetDynamicEdge){
//	// edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
//	// System.out.println("Carga de atributos PMR "+(edge instanceof PMRLocalGISStreetDynamicEdge));
//	// if(edge instanceof PMRLocalGISStreetDynamicEdge)
//	// edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
//	// }else if (edge instanceof DynamicEdge) {
//	// edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection();
//	// }else if (edge instanceof Edge){
//	// edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
//	// }
//	// }
//	//
//	//
//	//
//	// Feature feature = new BasicFeature(edgesFeatureCol
//	// .getFeatureSchema());
//	// feature.setAttribute("idEje", new Integer(edge.getID()));
//	// feature.setAttribute("coste", new Double(
//	// ((EdgeWithCost) edge).getCost()));
//	// feature.setAttribute("idNodoA", edge.getNodeA().getID());
//	// feature.setAttribute("idNodoB", edge.getNodeB().getID());
//	// if (edge instanceof DynamicEdge){
//	// feature.setAttribute("impedanciaAB", ((DynamicEdge) edge).getImpedance(edge.getNodeA()).getCost(1));
//	// feature.setAttribute("impedanciaBA", ((DynamicEdge) edge).getImpedance(edge.getNodeB()).getCost(1));
//	// feature.setAttribute("costeEjeDinamico", ((DynamicEdge) edge).getCost());
//	// }
//	// if (edge instanceof ILocalGISEdge){
//	// feature.setAttribute("longitudEje", ((ILocalGISEdge) edge).getEdgeLength());
//	// feature.setAttribute("idFeature", ((ILocalGISEdge) edge).getIdFeature());
//	// feature.setAttribute("idCapa", ((ILocalGISEdge) edge).getIdLayer());
//	// }
//	// if (edge instanceof LocalGISStreetDynamicEdge){
//	// feature.setAttribute("regulacionTrafico", ((LocalGISStreetDynamicEdge) edge).getTrafficRegulation().toString());
//	// feature.setAttribute("maxVelocidadNominal", RedondearVelocidad(((LocalGISStreetDynamicEdge) edge).getNominalMaxSpeed() * 3600.0 / 1000.0));
//	// feature.setAttribute("pintadaRegulacionTrafico", 0);
//	// if (edge instanceof PMRLocalGISStreetDynamicEdge){
//	// feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) edge).getWidth());
//	// feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) edge).getTransversalSlope());
//	// feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) edge).getLongitudinalSlope());
//	// feature.setAttribute("tipoEje", ((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType());
//	// feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) edge).getObstacleHeight());
//	// feature.setAttribute("ejeRelacionadoConId", ((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId());
//	// if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.LEFT)
//	// feature.setAttribute("ladoAcera", "L");
//	// else if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.RIGHT)
//	// feature.setAttribute("ladoAcera", "R");
//	// if (edge instanceof ZebraDynamicEdge)
//	// feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
//	// }
//	// }
//	//
//	// LineString geom_edge = fact.createLineString(coords);
//	// feature.setGeometry(geom_edge);
//	// edgesFeatureCol.add(feature);
//	//
//	// }
//	//
//	// if (edgesFeatureCol == null){
//	// edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
//	// }
//
//	Layer edgesLayer = context.addLayer(categoryName, subred + " (Repr.Grafo)", edgesFeatureCol);
//	LabelStyle labelStylenew = new LabelStyle();
//	labelStylenew.setAttribute("coste");
//	labelStylenew.setColor(Color.red);
//	labelStylenew.setScaling(false);
//	// labelStyle.setEnabled(true);
//	edgesLayer.addStyle(labelStylenew);
//	return edgesLayer;
//    }

//    public static FeatureCollection getFeatureCollectionForEdges(String subred, Graph graph, PlugInContext context)
//    {
//	Collection edges = graph.getEdges();
//	// Reutiliza el dibujado de capas por delegación
//	DibujarRedPlugIn supportDraw = new DibujarRedPlugIn();
//	FeatureCollection edgesFeatureCol = supportDraw.getEdgeFeatures(null, subred, edges, null, context);
//	return edgesFeatureCol;
//    }

    public static String getLayerIDFromIntIdLayer(Connection con, int idLayer)
    {

	String unformattedQuery = "";
	String sqlQuery = "select name from layers where id_layer = " + idLayer;
	PreparedStatement preparedStatement = null;
	ResultSet rs = null;

	try
	    {
		if (con != null)
		    {
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			rs = preparedStatement.executeQuery();

			if (rs.next())
			    {
				unformattedQuery = rs.getString("name");
			    }
			preparedStatement.close();
			rs.close();
		    }
	    } catch (SQLException e)
	    {
		e.printStackTrace();
	    }
	finally
	    {
		ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	return unformattedQuery;

    }

    /**
     * Comprueba la orientació³n del {@link LineString} y devuelve la geometria acorde al {@link Edge}
     * 
     * @param edge
     * @param lineString
     * @return
     */
    public static LineString checkLineStringOrientation(Edge edge, LineString lineString)
    {
	edge = (Edge) NodeUtils.unwrapProxies(edge);
	XYNode xyNodeA = (XYNode) ((edge.getNodeA() instanceof XYNode) ? edge.getNodeA() : null);
	XYNode xyNodeB = (XYNode) ((edge.getNodeB() instanceof XYNode) ? edge.getNodeB() : null);

	Coordinate coordNodeA = xyNodeA != null ? xyNodeA.getCoordinate() : null;
	Coordinate coordNodeB = xyNodeB != null ? xyNodeB.getCoordinate() : null;
	// Calcula la distancia de un extremo
	Coordinate lineStringStartCoord = lineString.getCoordinateN(0);
	Coordinate lineStringEndCoord = lineString.getCoordinateN(lineString.getNumPoints() - 1);
	boolean needReverse = false;
	if (coordNodeB == null && coordNodeA != null) // use only coordNodeA
	    {
		double distanceToStart = lineStringStartCoord.distance(coordNodeA);
		double distanceToEnd = lineStringEndCoord.distance(coordNodeA);
		if (distanceToStart < distanceToEnd)
		    needReverse = true;
	    } else if (coordNodeB != null && coordNodeA == null) // use only coordNodeB
	    {
		double distanceToStart = lineStringStartCoord.distance(coordNodeB);
		double distanceToEnd = lineStringEndCoord.distance(coordNodeB);
		if (distanceToStart > distanceToEnd)
		    needReverse = true;
	    }
	if (coordNodeB != null && coordNodeA != null) // use best match for both coords
	    {
		double distanceNodeA = lineStringStartCoord.distance(coordNodeA);
		double distanceNodeB = lineStringStartCoord.distance(coordNodeB);
		if (distanceNodeA > distanceNodeB)
		    needReverse = true;
	    }

	if (!needReverse)
	    return lineString;
	else
	    {

		// Class<? extends LineString> clasz = lineString.getClass();
		// Method[] methods = clasz.getMethods();
		// Method method=clasz.getMethod("reverse", null);
		// LineString rev=(LineString) method.invoke(lineString, null);
		LineString rev = (LineString) lineString.reverse(); // JPC misterioso BUG que hace que no se encuentre el mÃ©todo Â¿Â¿Â¿Â¿????
		return rev;

	    }
    }

    public static Network getSubNetwork(NetworkManager networkMgr, String red, String subredName)
    {
	Network selectedNet = null;
	if (red != null && !red.equals(""))
	    {
		if (subredName != null && !subredName.equals(""))
		    {
			selectedNet = networkMgr.getNetwork(red).getSubNetwork(subredName);
		    } else
		    {
			selectedNet = networkMgr.getNetwork(red);
		    }
	    }
	return selectedNet;
    }

    public static void addDirtyGraphablesIsSupported(Graph graph, List<? extends Graphable> edgesWithFeaturesList)
    {
	if (graph instanceof DynamicGraph)
	    {
		GraphMemoryManager memmgr = (((DynamicGraph) graph).getMemoryManager());
		if (memmgr instanceof ChangesMonitoredMemoryManager)
		    {
			UpdateMonitor memMonitor = null;
			ChangesMonitoredMemoryManager monitoredMemmgr = (ChangesMonitoredMemoryManager) memmgr;
			memMonitor = monitoredMemmgr.getUpdateMonitor();
			memMonitor.addDirtyGraphables(edgesWithFeaturesList);
		    }
	    }
    }

    public static EnableCheck createEnableNetworkSelectedCheck(final MultiInputDialog diag, final String fieldname)
    {
	return new EnableCheck()
	    {
		@Override
		public String check(JComponent component)
		{
		    
		    PanelToSelectMemoryNetworks panel = (PanelToSelectMemoryNetworks) diag.getComponent(fieldname);
		    JLabel label = (JLabel) diag.getLabel(fieldname);
		    if ("".equals(panel.getRedSeleccionada()) && "".equals(panel.getSubredseleccionada()))
			{
			    return "Debe seleccionar alguna red en " + label.getText() + ".";
			} else
			return null;
		}
	    };
    }

    /**
     * Actualiza todos los {@link LocalGISStreetDynamicEdge} para fijar la networkId de los nodeA y nodeB
     * 
     * @param thisNetwork
     * @param networkId
     */
    public static void updateLocalGISEdgesWithNetworkId(Graph graph, int networkId)
    {
	// Graph graph = thisNetwork.getGraph();
	for (Object edges : graph.getEdges())
	    {
		if (edges instanceof LocalGISStreetDynamicEdge)
		    {
			LocalGISStreetDynamicEdge lgEdge = (LocalGISStreetDynamicEdge) edges;
			lgEdge.setIdNetworkNodeA(networkId);
			lgEdge.setIdNetworkNodeB(networkId);
		    }
	    }
    }

//    public static NetworkManager getNetworkManager(PlugInContext context)
//    {
//        WorkbenchContext workbenchContext = context.getWorkbenchContext();
//        NetworkManager networkMgr = NetworkModuleUtilOld.getNetworkManager(workbenchContext);
//       	return networkMgr;
//        }

//    public static NetworkManager getNetworkManager(WorkbenchContext workbenchContext)
//    {
//        return NetworkModuleUtilOld.getNetworkManager(workbenchContext.getBlackboard());
//    }

//    public static NetworkManager getNetworkManager(Blackboard blackboard) {
//    	NetworkManager networkMgr = (NetworkManager) blackboard.get("RedesDefinidas");
//    	if (networkMgr == null) {
//    		networkMgr = new LocalGISNetworkManager();
//    		blackboard.put("RedesDefinidas",
//    				networkMgr);
//    		InterNetworker networker = new LocalGISInterNetworker();
//    		networker.addNetworkManager(networkMgr);
//    		networkMgr.setInterNetworker(networker);
//    	}
//    	
//    	return networkMgr;
//    }

}
