/**
 * LocalGISAllinMemoryManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.manager;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.io.GraphReaderWriter;
import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.traverse.GraphVisitor;
import org.uva.route.manager.SpatialMemoryManager;
import org.uva.route.network.Network;
import org.uva.routeserver.managers.AllInMemoryExternalSourceGraphMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.io.LocalRouteReaderWriter;
import com.localgis.route.graph.io.NetworkPropertiesReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class LocalGISAllinMemoryManager extends	AllInMemoryExternalSourceGraphMemoryManager implements SpatialMemoryManager
{
    /**
     * Adaptor for the implementation of spatial searches
     */
    	LocalGISSpatialMemoryAdaptor adaptor = new LocalGISSpatialMemoryAdaptor(this);
	private WeakReference<TaskMonitor> taskMonitor;
	protected PlugInContext context=null;
	public LocalGISAllinMemoryManager(GraphReaderWriter store)
	{
		super(store);
	}

	public void commit() throws IOException
	{
		// desactivamos el autocommit porque el driver LocalGIS no lo soporta
	    if (getStore() instanceof LocalGISRouteReaderWriter)
		{
		    LocalGISRouteReaderWriter lgStore = (LocalGISRouteReaderWriter) getStore();
		    lgStore.setTaskMonitor(getTaskMonitor());
		}
	    super.commit();
	}
private TaskMonitor getTaskMonitor()
	{
	   return taskMonitor.get();
	}

/**
 * Comprueba si este memory manager estÃ¡ gestionando grafo en base de datos.
 * @return
 */
	public boolean isLinkedToDatabase()
	{
	    GraphReaderWriter readerWriter = getExternalStoreAdapter().getStore();
	    if (readerWriter instanceof LocalGISRouteReaderWriter)
		{
		    // No hace falta comprobar la conexión. Asumimos qeu la persistencia de red apunta a la base de datos
		    return true;
		}
	    return false;
	}
	
	public boolean isLinkedToFile()
	{
	    GraphReaderWriter readerWriter = getExternalStoreAdapter().getStore();
	    if (readerWriter instanceof LocalRouteReaderWriter)
		{
		    // No hace falta comprobar la conexión. Asumimos qeu la persistencia de red apunta a fichero
		    return true;
		}
	    return false;
	}
/**
 * Cambia el readerwriter y lo prepara para persistir el grafo en la base de datos
 * Averigua si es {@link LocalGISStreetRouteReaderWriter} o {@link LocalGISRouteReaderWriter}
 * con el primer Edge del grafo TODO Unificar ReaderWriters.
 * 
 * @param dataBaseNetworkName
 * @throws SQLException 
 */
public void linkToDataBase(String dataBaseNetworkName,Network thisNetwork) throws IOException
    {
	try
	    {
		boolean wasLinkedToDatabase = this.isLinkedToDatabase();
		boolean changeOfNetworkName = wasLinkedToDatabase && (getStoreNetworkName() != dataBaseNetworkName);
		if (!wasLinkedToDatabase || changeOfNetworkName)// en caso contrario no hace nada. El trabajo estÃ¡ hecho
		    {
			
			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			LocalGISRouteReaderWriter db = null;
			boolean isPMR=false;
			// determina si activa el flag PMR
				Map<String, Object> properties = thisNetwork.getProperties();
				NetworkProperty propertyPMR = (NetworkProperty) properties.get("PMR");
				String value = propertyPMR!=null?propertyPMR.getValue("PMR"):null;
				if ("true".equals(value))
				    isPMR=true;
			
			// selecciona el tipo de reader
//			if (!this.getEdges().isEmpty() && this.getEdges().iterator().next() instanceof LocalGISStreetDynamicEdge)
//			    {
				db = new LocalGISStreetRouteReaderWriter(routeConnection,isPMR);
//			    } else
//			    {
//				db = new LocalGISRouteReaderWriter(routeConnection);
//			    }
			
			db.setProperty(AbstractReaderWriter.GENERATOR, NetworkModuleUtilWorkbench.getLocalGISStreetBasicLineGraphGenerator());
			//
			
			db.setNetworkName(dataBaseNetworkName);
			
			setStore(db);
		    }
	    } catch (SQLException e)
	    {
		throw new IOException(e);
	    }

    }
/**
 * Cambia el readerwriter y lo prepara para persistir el grafo en fichero local
 * Averigua si es {@link LocalGISStreetRouteReaderWriter} o {@link LocalGISRouteReaderWriter}
 * con el primer Edge del grafo TODO Unificar ReaderWriters.
 * 
 * @param dataBaseNetworkName
 * @throws SQLException 
 */
public void linktoFile(String networkName)
{

	String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
	File dir = new File(base,"networks");
	if(! dir.exists() ){
		dir.mkdirs();
	}

	String folderPath = dir.getPath();
	File networkDir = new File(folderPath,networkName);
	if(! networkDir.exists() ){
		networkDir.mkdirs();
	}

	File networkFile = new File(networkDir,networkName);
	
    LocalRouteReaderWriter db = new LocalRouteReaderWriter(networkFile.getPath());
   
    db.setProperty(AbstractReaderWriter.GENERATOR,NetworkModuleUtilWorkbench.getLocalGISStreetBasicLineGraphGenerator());
    setStore(db);
}
public String getStoreNetworkName()
    {
	if (getStore() instanceof LocalGISRouteReaderWriter)
	    return ((LocalGISRouteReaderWriter) getStore()).getNetworkName();
	else if (getStore() instanceof NetworkPropertiesReaderWriter)
	    return ((LocalRouteReaderWriter) getStore()).getFileName();
	else
	    return null;
    }
public int getStoreNetworkId()
{
	if (getStore() instanceof LocalGISRouteReaderWriter)
	    return ((LocalGISRouteReaderWriter) getStore()).getNetworkId();
	else 
	    return 0;
}
/**
 * Hook to notify operations
 * @param monitor
 */
public void setTaskMonitor(TaskMonitor monitor)
{
   this.taskMonitor=new WeakReference<TaskMonitor>(monitor);    
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List getNodesNearTo(Point point, double tolerance, int num)
{
    return adaptor.getNodesNearTo(point, tolerance, num);
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List<Edge> getEdgesNearTo(Point point, double tolerance, int num)
{
    return adaptor.getEdgesNearTo(point, tolerance, num);
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List queryEdges(BoundingBox bbox, GraphVisitor visitor, int num)
{
    return adaptor.queryEdges(bbox, visitor, num);
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List queryNodes(BoundingBox bbox, GraphVisitor visitor, int num)
{
    return adaptor.queryNodes(bbox, visitor, num);
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List<Node> getNodesNearTo(Geometry where, double tolerance, int num)
{
    return adaptor.getNodesNearTo(where, tolerance, num);
}
/**
 * NECESITA FIJAR EL CONTEXT con {@link #setContext(PlugIn)} para poder buscar las geometrías de los {@link LocalGISStreetDynamicEdge}
 */
public List<Edge> getEdgesNearTo(Geometry where, double tolerance, int num)
{
    return adaptor.getEdgesNearTo(where, tolerance, num);
}

public void setContext(PlugInContext context)
{
    this.context = context;
    if (adaptor!=null)
	adaptor.setContext(context);
}





	
}