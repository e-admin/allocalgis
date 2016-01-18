/**
 * LoadSystemLayers.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin;

import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.geopista.app.AppContext;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.model.LayerFamily;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACLayerFamily;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.server.administradorCartografia.IAdministradorCartografiaClient;
import com.geopista.util.UserCancellationException;
import com.geopista.util.exception.CancelException;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.geopista.app.administrador.init.Constantes;


public class LoadSystemLayers {
	
	private static final Log	logger	= LogFactory.getLog(LoadSystemLayers.class);
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	public static void loadLayers(ArrayList layerFamiliesIDsList,  PlugInContext context, TaskMonitor monitor) throws ACException, Exception{
		
		String nameLayerFamily=null;
		String sUrlPrefix = aplicacion.getString(Constantes.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
		AdministradorCartografiaClient administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);
		
		Iterator layerFamiliesIDsListIter = layerFamiliesIDsList.iterator();
		
		ArrayList listaErrPerm = new ArrayList();
		
		while (layerFamiliesIDsListIter.hasNext()) {
			ACLayerFamily idLayerFamily = (ACLayerFamily) layerFamiliesIDsListIter
					.next();

			nameLayerFamily=idLayerFamily.getName();
			monitor.report(aplicacion
					.getI18nString("LoadSystemLayers.LayerFamily")
					+ " " + idLayerFamily.getName());
			String[] layerIDs = administradorCartografiaClient
					.getLayerIDs(idLayerFamily.getId());

			for (int n = layerIDs.length - 1; n >= 0; n--) 
			    {
				String layerID= layerIDs[n];
				try
				    {
					logger.debug("Loading system Layer:"+layerID);
					monitor.report(layerIDs.length-n,layerIDs.length,aplicacion.getI18nString("LoadSystemLayers.Cargando")+":"+layerID);
					IGeopistaLayer layer = loadSystemLayer(layerID, nameLayerFamily, monitor, context, listaErrPerm);
					if (layer != null
					    		&& !layer.getSystemId().equalsIgnoreCase("error")) {
					    	context.getLayerManager().addLayer(idLayerFamily.getName(),
					    			layer);
					    	((LayerFamily) context.getLayerManager().getCategory(
					    			idLayerFamily.getName()))
					    			.setSystemLayerFamily(true);
					    	((LayerFamily) context.getLayerManager().getCategory(
					    			idLayerFamily.getName())).setSystemId(String
					    			.valueOf(idLayerFamily.getId()));
					    } else {
					    	JOptionPane.showMessageDialog((Component) context
					    			.getWorkbenchGuiComponent(), aplicacion
					    			.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerID);
					    }
				    } catch (InterruptedException e)
				    {
					break; // cancel operations
				    }
			    }

		}
		if (listaErrPerm.size() > 0) {
			JOptionPane.showMessageDialog((Component) context
					.getWorkbenchGuiComponent(), (listaErrPerm.size())
					+ " capa(s) no han sido abiertas por falta de permisos");
		}
	
	}
	
	

	public static IGeopistaLayer loadSystemLayer(String layerID, String nameLayerFamily, TaskMonitor monitor, PlugInContext context,
		ArrayList listaErrPerm) throws InterruptedException
	{
	    IGeopistaLayer layer=null;

	    
	    boolean layerRepeated = false;
	    List currentLayers = context.getLayerManager().getLayers();
	    Iterator currentLayersIterator = currentLayers.iterator();
	    while (currentLayersIterator.hasNext()) {
	    	Layer currentLayer = (Layer) currentLayersIterator.next();
	    	if (currentLayer instanceof GeopistaLayer) {
	    		String currentSystemId = ((GeopistaLayer) currentLayer)
	    				.getSystemId();
	    		if (currentSystemId.trim().equals(layerID)) {
	    			layerRepeated = true;
	    			break;
	    		}
	    	}
	    }

	    if (layerRepeated)
	        return layer;

	    try {
	    	GeopistaServerDataSource serverDataSource = new GeopistaServerDataSource();
	    	Map properties = new HashMap();
	    	// Introducimos el mapa Origen
	    	properties.put("mapadestino", (GeopistaMap) context
	    			.getTask());
	    	// Introducimos el fitro geometrico si es distinto de null,
	    	// si se introduce null falla
	    	// properties.put("filtrogeometrico",null);
	    	// Introducimos el FilterNode
	    	properties.put("nodofiltro", FilterLeaf.equal("1",
	    			new Integer(1)));
	    	
	    	// Introducimos el srid del mapa destino. Importante para
	    	// cuando se crear un mapa en el editor.
	    	try {
	    		properties.put("srid_destino", Integer.valueOf(context
	    				.getLayerManager().getCoordinateSystem()
	    				.getEPSGCode()));
	    	} catch (Exception e1) {							
	    	}

	    	serverDataSource.setProperties(properties);
	    	GeopistaConnection geopistaConnection = (GeopistaConnection) serverDataSource
	    			.getConnection();

	    	
	    	// Creamos una coleccion para almacenar las excepciones que
	    	// se producen
	    	Collection exceptions = new ArrayList();
	    	// preparamos la url de la layer
	    	URL urlLayer = new URL("geopistalayer://default/"
	    			+ layerID);
	    	// URL urlLayer = new URL("geopistalayer:"+layerIDs[n]);

	    	if (monitor.isCancelRequested())
	    		throw new UserCancellationException();
	    	geopistaConnection.executeQuery(urlLayer.toString(),
	    			exceptions, monitor);

	    	boolean cancelado=false;
	    	if (exceptions.size() > 0) {
	    		// En caso de que sea una excepcion de permisos,
	    		Iterator recorreExcepcion = exceptions.iterator();
	    		while (recorreExcepcion.hasNext()) {
	    			// Revisar el efecto del continue
	    			Exception e = (Exception) recorreExcepcion.next();
	    			if (e instanceof CancelException){
	    				JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa/capa cancelada");
	    				cancelado=true;;
	    			}
	    			else if (e.getCause()
	    					.getLocalizedMessage()
	    					.toString()
	    					.equals(
	    							"PermissionException: Geopista.Layer.Leer")) {
	    				listaErrPerm.add(e);
	    			} 
	    			else if (e instanceof CancelException){
	    				JOptionPane.showMessageDialog(aplicacion.getMainFrame(),"Carga de mapa/capa cancelada");
	    				cancelado=true;
	    			}
	    			else {
	    				JOptionPane
	    						.showMessageDialog(
	    								(Component) context
	    										.getWorkbenchGuiComponent(),
	    								aplicacion
	    										.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerID);
	    			}
	    		}
	    		
	    		if (cancelado)
	    			throw new UserCancellationException();
	    		return layer;
	    	}
	    	layer = geopistaConnection.getLayer();
	    	DataSourceQuery dataSourceQuery = new DataSourceQuery();
	    	dataSourceQuery.setQuery(urlLayer.toString());
	    	dataSourceQuery.setDataSource(serverDataSource);
	    	layer.setDataSourceQuery(dataSourceQuery);

	    	// layer=administradorCartografiaClient.loadLayer((GeopistaMap)
	    	// context.getTask()
	    	// ,layerIDs[n],UserPreferenceStore.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY,"es_ES",true),null,FilterLeaf.equal("1",new
	    	// Integer(1)));
	    } catch (Exception e) {
	    	JOptionPane.showMessageDialog((Component) context
	    			.getWorkbenchGuiComponent(), aplicacion
	    			.getI18nString("LoadSystemLayers.CapaErronea")+" Familia:"+nameLayerFamily+" Capa:"+layerID);
	    	return layer;
	    }

	   
	    return layer;
	}


}
