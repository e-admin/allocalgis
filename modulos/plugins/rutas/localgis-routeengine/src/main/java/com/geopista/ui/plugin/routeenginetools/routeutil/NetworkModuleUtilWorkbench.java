/**
 * NetworkModuleUtilWorkbench.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.InterNetworker;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.network.LocalGISInterNetworker;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class NetworkModuleUtilWorkbench {

	private static final String NETWORK_MANAGER_BLACKBOARD_KEY = "RedesDefinidas";
	public static NetworkManager getNetworkManager(Blackboard blackboard) {
		NetworkManager networkMgr = (NetworkManager) blackboard.get(NETWORK_MANAGER_BLACKBOARD_KEY);
		if (networkMgr == null) {
			networkMgr = new LocalGISNetworkManager();
			blackboard.put(NETWORK_MANAGER_BLACKBOARD_KEY,
					networkMgr);
			InterNetworker networker = new LocalGISInterNetworker(new LocalGISRouteReaderWriter(new GeopistaRouteConnectionFactoryImpl()));
			networker.addNetworkManager(networkMgr);
			networkMgr.setInterNetworker(networker);
		}
		
		return networkMgr;
	}

	public static NetworkManager getNetworkManager(PlugInContext context)
	{
	    WorkbenchContext workbenchContext = context.getWorkbenchContext();
	    NetworkManager networkMgr = getNetworkManager(workbenchContext);
	   	return networkMgr;
	    }

	public static NetworkManager getNetworkManager(WorkbenchContext workbenchContext)
	{
	    return getNetworkManager(workbenchContext.getBlackboard());
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
	
	String layerID = NetworkModuleUtil.getQueryFromIdLayer(conn, ((ILocalGISEdge) edge).getIdLayer());
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
	if (layer==null)
	    {
		throw new RuntimeException((Exception)exceptions.iterator().next());
	    }
	DataSourceQuery dataSourceQuery = new DataSourceQuery();
		dataSourceQuery.setQuery(urlLayer.toString());
		dataSourceQuery.setDataSource(serverDataSource);
		layer.setDataSourceQuery(dataSourceQuery);
	return (GeopistaLayer)layer;
	}

	public static IGeopistaLayer loadSystemLayerInWorkbench(String systemId,PlugInContext context)
	{
	IGeopistaLayer layer=loadSystemLayer(systemId, context);
	context.getLayerManager().addLayer(layer.getSystemId(), layer);
	return layer;
	}

	public static IGeopistaLayer loadSystemLayerReferencedByEdge(ILocalGISEdge localGisEdge, PlugInContext context)
	{
	IGeopistaLayer layer;
	layer = getOriginalLayer(localGisEdge, context); // TODO obtener únicamente la feature no toda la capa
	context.getLayerManager().addLayer(layer.getSystemId(), layer);
	return layer;
	}

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
				continue;
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
				geom =  NetworkModuleUtil.getEdgeLineStringFromGeometry(localgisEdge.getFeature().getGeometry());
			    } else if (context != null)
			    {
				Feature feature = findFeatureForEdge(theEdge, context);
				if (feature != null)
				    {
					//context.getWorkbenchGuiComponent().warnUser("Feature obtenida de capa local");
					geom =  NetworkModuleUtil.getEdgeLineStringFromGeometry(feature.getGeometry());
				    }
			    }
		    }
		if (geom != null && geom instanceof LineString) // reorienta geometria si hace falta
		    {
			geom =  NetworkModuleUtil.checkLineStringOrientation(theEdge, (LineString) geom);
		    }
		return geom;
	    }

	public static Feature getEdgeFeatureFromDatabase(ILocalGISEdge localGisEdge, PlugInContext context)
	{
	IGeopistaLayer layer = null;
	int idLayer = localGisEdge.getIdLayer();
	IGeopistaLayer lastUsedLayer = lastLayer.get();
	List workbenchLayers = context.getWorkbenchContext().getLayerManager().getLayers();
	if (
		lastIdLayer != idLayer
		|| 
		(lastUsedLayer!=null && lastUsedLayer.getId_LayerDataBase()!=idLayer)
		||		
		(lastUsedLayer!=null  && !workbenchLayers.contains(lastUsedLayer))
	
	) // se pide una capa nueva
	    {
		// rescata mini cache
		layer = lastUsedLayer;
	
		if (layer == null // trata de cargarla porque no hay referencia en weakreference
		|| 
		lastUsedLayer.getId_LayerDataBase()!=idLayer   // la capa cacheada no tiene el mismo id
		||
		!workbenchLayers.contains(layer)
		) // la cache es una capa con el mismo id pero de otro LayerViewPanel
		    {
			// try to locate loaded systemlayer
			GeopistaRouteConnectionFactoryImpl fact = new GeopistaRouteConnectionFactoryImpl();
			Connection con = fact.getConnection();
	
			String layerName = NetworkModuleUtil.getLayerIDFromIntIdLayer(con, idLayer);
			layer = (GeopistaLayer) context.getLayerManager().getLayer(layerName);
			if (layer == null && !"".equals(layerName))
			    {
				layer = loadSystemLayerReferencedByEdge(localGisEdge, context);
			    }
			lastIdLayer = idLayer;
			lastLayer = new WeakReference<IGeopistaLayer>(layer);
		    }
	    }
	else if (lastIdLayer==idLayer && workbenchLayers.contains(lastUsedLayer))
	{
		layer=lastUsedLayer;
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

	protected static int lastIdLayer = -1;
	protected static WeakReference<IGeopistaLayer> lastLayer = new WeakReference<IGeopistaLayer>(null);
	static GeopistaLayer cachedLayerForSearch = null;
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
			geom =  NetworkModuleUtil.checkLineStringOrientation(theEdge, (LineString) geom);
		    }
	    }
	return geom;
	}

	/**
	 * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
	 * 
	 * @return
	 */
	public static GraphGenerator getLocalGISStreetGraphGenerator()
	{
	return NetworkModuleUtil.getLocalGISStreetGraphGenerator(AppContext.getIdEntidad());
	}

	/**
	 * Crea un {@link GraphGenerator} para la entidad actual según {@link AppContext#getIdEntidad()}
	 * 
	 * @return
	 */
	public static GraphGenerator getLocalGISStreetBasicLineGraphGenerator()
	{
	return NetworkModuleUtil.getLocalGISStreetBasicLineGraphGenerator(AppContext.getIdEntidad());
	}

}
