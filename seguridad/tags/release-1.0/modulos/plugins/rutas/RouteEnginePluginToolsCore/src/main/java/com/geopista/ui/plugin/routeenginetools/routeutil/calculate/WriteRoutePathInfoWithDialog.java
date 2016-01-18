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
import org.uva.geotools.graph.util.geom.GeometryUtil;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.path.RoutePath;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.graph.structure.proxy.ProxyGeographicNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.util.NodeUtils;

import sun.awt.SunToolkit.InfiniteLoop;

import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.CardinalDirections;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.TurnRouteStreetchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.VirtualInfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl.InfoRouteDAOPostgreSQLImplementation;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs.InfoRouteDialog;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
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
	// variables estaticas que determinan grados para las orientaciones
	// del primer tramo de la ruta.
	// ¿En qué calle empieza la ruta? ¿En qué dirección? Norte/Sur... etc
	private static final int NORTH_START = 80;
	private static final int NORTH_END = 100;
	private static final int WEST_START = 170;
	private static final int WEST_END =  190;
	private static final int SOUTH_START = 260;
	private static final int SOUTH_END = 280;
	private static final int EAST_START = 350;
	private static final int EAST_END = 10;
	
	public static void showInfoRouteDialog(Path routePath, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
			PlugInContext context){
		
		inicializarIdiomaAvisosPanels();
		
		ArrayList<InfoRouteStretchBean> list;
		try {
			list = buildInfoRouteStretchBeanList(routePath, startNodeInfo, endNodeInfo, context);
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
			ArrayList<InfoRouteStretchBean> list = new ArrayList<InfoRouteStretchBean>();
			if (routePaths!=null && !routePaths.isEmpty()){
				Iterator<Path> pathsIt = routePaths.iterator();
				int i = 0;
				while(pathsIt.hasNext()){
					list.add(new TurnRouteStreetchBean(null,"<html><b>"+ I18N.get("routedescription","routeengine.route.description.route.description") +" " + (i+1) + "</b></html>"));
					
					VirtualNodeInfo startNodeInfo = null;
					if (virtualNodes != null && virtualNodes.length>0 && i<virtualNodes.length){
							startNodeInfo = virtualNodes[i];
					}
					
					VirtualNodeInfo endNodeInfo = null;
					if (virtualNodes!=null && virtualNodes.length>0 && (i+1)<virtualNodes.length ){
						endNodeInfo = virtualNodes[i+1];
					}
					
					
					ArrayList<InfoRouteStretchBean> actualist = buildInfoRouteStretchBeanList(pathsIt.next(), startNodeInfo, endNodeInfo, context);
					
					
					// añadimos al primer elemento del tipo "Ruta 1" todas las geometrias,
					// para que cuando se haga click sobre la lista de informacion haga zoom a 
					// toda la ruta.
					if (actualist!=null && !actualist.isEmpty()){
						Iterator<InfoRouteStretchBean> it = actualist.iterator();
						InfoRouteStretchBean routePahtIDescriptior = list.get(list.size()-1);
						while(it.hasNext()){
							InfoRouteStretchBean actualInfoRouteStretchBean = it.next();
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
	
	
	
	private static ArrayList<InfoRouteStretchBean> buildInfoRouteStretchBeanList(Path routePath, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
			PlugInContext context) throws Exception{
		
		try{
			GeometryFactory geometryFactory = new GeometryFactory();
			ArrayList<InfoRouteStretchBean> stretchtBeans = buildEdgesInfoStretchBeans(routePath, geometryFactory, context);
			VirtualInfoRouteStretchBean startVirtualInfoRouteStretchBean = null;
			VirtualInfoRouteStretchBean endVirtualInfoRouteStretchBean = null;
			if(stretchtBeans.size() == 0){
				startVirtualInfoRouteStretchBean = buildEmptyVirtualInfoStretchBean(startNodeInfo, endNodeInfo, context,true);
				endVirtualInfoRouteStretchBean = buildEmptyVirtualInfoStretchBean(startNodeInfo, endNodeInfo, context,false);
					
			}else{
				startVirtualInfoRouteStretchBean = buildStartVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, context);
				endVirtualInfoRouteStretchBean = buildEndVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, context);

			}
			

			if (startVirtualInfoRouteStretchBean != null){
				if (stretchtBeans!=null && !stretchtBeans.isEmpty()){
					InfoRouteStretchBean initialStretchBean = stretchtBeans.get(0);
					if (!initialStretchBean.getNameStreet().equals("")){
						if (initialStretchBean.getTypeStreet().equals(startVirtualInfoRouteStretchBean.getTypeStreet()) && 
								initialStretchBean.getNameStreet().equals(startVirtualInfoRouteStretchBean.getNameStreet())){

							if (startVirtualInfoRouteStretchBean.addInfoRouteStretchBean(initialStretchBean)){
								stretchtBeans.remove(initialStretchBean);
							}

						}
					}
				}
				// Se añade la información del primer tramo virtual a los elementos de información de ruta.
				stretchtBeans.add(0,startVirtualInfoRouteStretchBean);
			}

			if (endVirtualInfoRouteStretchBean != null) {
				if (stretchtBeans!=null && !stretchtBeans.isEmpty()){
					InfoRouteStretchBean lastStretchBean = stretchtBeans.get(stretchtBeans.size()-1);
					if (lastStretchBean!=null && lastStretchBean.getNameStreet()!= null && !lastStretchBean.getNameStreet().equals("")){
						if (lastStretchBean.getTypeStreet().equals(endVirtualInfoRouteStretchBean.getTypeStreet()) && 
								lastStretchBean.getNameStreet().equals(endVirtualInfoRouteStretchBean.getNameStreet())){

							if (lastStretchBean instanceof VirtualInfoRouteStretchBean){
								((VirtualInfoRouteStretchBean)lastStretchBean).addInfoRouteStretchBean(endVirtualInfoRouteStretchBean);
								endVirtualInfoRouteStretchBean = null;
							}else if (endVirtualInfoRouteStretchBean.addInfoRouteStretchBean(lastStretchBean)){
								stretchtBeans.remove(lastStretchBean);
							}
						}
					}
				}
				// Se añade la información del último tramo virtual.
				if (endVirtualInfoRouteStretchBean != null){
					stretchtBeans.add(stretchtBeans.size(), endVirtualInfoRouteStretchBean);
				}
			}

			inicializarIdiomaAvisosPanels();
			if (routePath.getFirst()!=null){
				stretchtBeans.add(0,new TurnRouteStreetchBean(routePath.getLast(),I18N.get("routedescription","routeengine.route.description.routestart")));
			}
			if (routePath.getLast()!=null){
				stretchtBeans.add(stretchtBeans.size(),new TurnRouteStreetchBean(routePath.getFirst(),I18N.get("routedescription","routeengine.route.description.routeend")));
			}
			
			
			setOrientatedInfoRouteStretchBeans(routePath, stretchtBeans);
			return stretchtBeans;

		}catch (Exception e) {
			throw e;
		}
		
		
	}
		
	
	
	private static void setOrientatedInfoRouteStretchBeans(Path path, ArrayList<InfoRouteStretchBean> listStretchBeans){
		if (listStretchBeans!= null && !listStretchBeans.isEmpty()){
			for(int i=0; i < listStretchBeans.size(); i++){
				InfoRouteStretchBean actualStretchBean = listStretchBeans.get(i);
				if (actualStretchBean instanceof TurnRouteStreetchBean || actualStretchBean instanceof VirtualInfoRouteStretchBean){
					continue;
				}
				
				ArrayList<Geometry> geometries = actualStretchBean.getGeometries();
				
				if (geometries!=null && !geometries.isEmpty()){
					
					if (i == 0){
						Coordinate nodeCoord  = ((XYNode) path.getLast()).getCoordinate();
						if (nodeCoord.distance(((LineString)geometries.get(0)).getCoordinateSequence().getCoordinate(0)) != 0){
							GeometryUtil.reverseGeometry(((LineString)geometries.get(0)), true);
						}
					} else if (i == listStretchBeans.size()-1 ){

					}
				}
			}
		}
	}
	

	private static ArrayList<InfoRouteStretchBean> buildEdgesInfoStretchBeans(
			Path routePath, GeometryFactory geometryFactory, PlugInContext context) {
		ArrayList<InfoRouteStretchBean> result = new ArrayList<InfoRouteStretchBean>();
		Geometry geom = null;
		
		if (routePath!= null && !routePath.isEmpty() && routePath.isValid()){
			if (routePath.getEdges()!=null && !routePath.getEdges().isEmpty()){
				// Generamos los InfoSouteStretchBean de los edges del path
				InfoRouteStretchBean newInfoRouteStretchBean = null;
//				for(int i=0; i < routePath.getEdges().size(); i++){
				for(int i=routePath.getEdges().size()-1; i >= 0; i--){
					//Recorremos la lista de edges para obtener descripciones, geometrias, etc.
					if (routePath.getEdges().get(i)!=null && routePath.getEdges().get(i) instanceof Edge){
						Edge actualEdge = (Edge) routePath.getEdges().get(i);
						
						// Si el tramo es un ProxyEdge cogemos el getGraphable().
						if (actualEdge instanceof ProxyEdge){
							actualEdge = (Edge) ((ProxyEdge)actualEdge).getGraphable();
						}
						System.out.println(actualEdge.getID());
						
						if(actualEdge instanceof NetworkLink){
							UniqueIDGenerator idGen = new SequenceUIDGenerator();
							actualEdge = new LocalGISDynamicEdge(actualEdge.getNodeA(), actualEdge.getNodeB(), idGen);
							((ILocalGISEdge) actualEdge).setIdLayer(-1);
							((ILocalGISEdge) actualEdge).setIdFeature(actualEdge.getID());
							((ILocalGISEdge) actualEdge).setEdgeLength(((ProxyGeographicNode)actualEdge.getNodeA()).getPosition().distance(((ProxyGeographicNode)actualEdge.getNodeB()).getPosition()));
							continue;
						}
						if(actualEdge instanceof EquivalentEdge){
							System.out.println(actualEdge.getClass().toString());
							continue;
						}
						
						
						// Si el tramo es un ILocalGisEdge intentamos obtener la geometría original
						geom = null;
						try{
						if (actualEdge instanceof ILocalGISEdge){
							int SRID = 0;
							
							try{
								SRID = context.getLayerManager().getCoordinateSystem().getEPSGCode();
								if (SRID > 0){
									geom = getOriginalGeometryFromEdgeIdLayerIdFeature((ILocalGISEdge) actualEdge, SRID);
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
							
							if (geom == null){
								// SI ha ocurrido algún error al obtener la geometría original del tramos
								// intentamos asignarle el lineString del propio tramo.
								Coordinate[] coords = NodeUtils.CoordenadasArco(
										actualEdge, 
										routePath, 
										0, 
										1);
								geom = (LineString) geometryFactory.createLineString(coords);
							}
						} else{
							Coordinate[] coords = NodeUtils.CoordenadasArco(
									actualEdge, 
									routePath, 
									0, 
									1);
							geom = (LineString) geometryFactory.createLineString(coords);
						}
						}catch (Exception e) {
							e.printStackTrace();
						}
						

						
						// Intentamos obtener el tipo y la descripcion de la calle que represnta el edge.
						ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, context);
						String actualType = "";
						String actualName = "";
						if (descriptions != null && !descriptions.isEmpty() && descriptions.size() == 2){
							actualType = descriptions.get(0);
							actualName = descriptions.get(1);
						}
						
												
						if (newInfoRouteStretchBean == null || actualName==null || actualName.equals("") || actualType==null || 
								!actualType.equals(newInfoRouteStretchBean.getTypeStreet()) || 
								!actualName.equals(newInfoRouteStretchBean.getNameStreet()) ){
							
							if (newInfoRouteStretchBean!=null){
								result.add(newInfoRouteStretchBean);
							}
							
							newInfoRouteStretchBean = new InfoRouteStretchBean();
							newInfoRouteStretchBean.addEdgeToCollection(actualEdge);
							newInfoRouteStretchBean.addGeometryToCollection(geom);
							newInfoRouteStretchBean.setTypeStreet(actualType);
							newInfoRouteStretchBean.setNameStreet(actualName);
							newInfoRouteStretchBean.setLenthStreetMeters(Math.round(geom.getLength()));
							
						} else{
							if (geom != null){
								newInfoRouteStretchBean.addGeometryToCollection(geom);
								newInfoRouteStretchBean.setLenthStreetMeters(
										newInfoRouteStretchBean.getLenthStreetMeters() + 
										Math.round(geom.getLength()) 
								);
							}
							if (actualEdge != null){
								newInfoRouteStretchBean.addEdgeToCollection(actualEdge);
							}
						}
					}
				}
				
				if(newInfoRouteStretchBean != null){
					result.add(newInfoRouteStretchBean);
				}
			}
		}
		return result;
	}



	private static HashMap<String, Network> getConfiguratorNetworks(PlugInContext context){
		HashMap<String, Network> configuatorNetworks = new HashMap<String, Network>();
		CalcRutaConfigFileReaderWriter configProperties = new CalcRutaConfigFileReaderWriter();
		
		String redes[] = configProperties.getRedesNames();
		for(int i = 0; i < redes.length; i++){
			configuatorNetworks.put(redes[i], ((LocalGISNetworkManager) FuncionesAuxiliares.getNetworkManager(context)).getAllNetworks().get(redes[i]) );
		}
		return configuatorNetworks;
	}
	
	

	private static ArrayList<String> getTypeAndDescriptionStreet(
			Edge actualEdge, PlugInContext context) {
		
		NetworkManager netManager = ((LocalGISNetworkManager) FuncionesAuxiliares.getNetworkManager(context));
		ArrayList<String> result = new ArrayList<String>();
		String subred = "";
		if (netManager!=null){
			
			Iterator<Network> nets = getConfiguratorNetworks(context).values().iterator();
			while (nets.hasNext()){
				BasicNetwork bnet = (BasicNetwork) nets.next();
				if (bnet.getGraph().getEdges().contains(actualEdge)){
					subred = bnet.getName();
					break;
				}
			}
			
			
			try{
				if (actualEdge!= null && subred!=null && !subred.equals("") && netManager!=null){
					String typeDescriptionResult = getTypeStreetDescriptor(actualEdge, subred , netManager);
					result.add(typeDescriptionResult);
				} else{
					result.add("");
				}
			}catch (Exception e) {
				result.add("");
			}
			
			try{
				if (actualEdge!= null && subred!=null && !subred.equals("") && netManager!=null){
					String typeDescriptionResult = getNameStreetDescriptor(actualEdge, subred, netManager);
					result.add(typeDescriptionResult);
				}else{
					result.add("");
				}
			} catch (Exception e) {
				result.add("");
			}
			
		}
		
		return result;
	}






	private static VirtualInfoRouteStretchBean buildEndVirtualInfoStretchBean(
			VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
			Path routePath, ArrayList<InfoRouteStretchBean> stretchtBeans,
			GeometryFactory geometryFactory, PlugInContext pluginContext) {
		
		
		Geometry geom = null;
		VirtualInfoRouteStretchBean result = null;
		if (geometryFactory == null){
			geometryFactory = new GeometryFactory();
		}
		
		if (endNodeInfo != null){
			result = new VirtualInfoRouteStretchBean(endNodeInfo);
			geom = null;
			if (routePath.getEdges()!=null && !routePath.getEdges().isEmpty() && routePath.size() > 1){
				Geometry lastEdgeGeometry = stretchtBeans.get(stretchtBeans.size()-1).getGeometries().get(stretchtBeans.get(stretchtBeans.size()-1).getGeometries().size()-1);
				
				if (lastEdgeGeometry.distance(endNodeInfo.getLinestringAtoV()) >
				lastEdgeGeometry.distance(endNodeInfo.getLinestringVtoB()) ){
					geom = endNodeInfo.getLinestringVtoB();
				}else{
					geom = endNodeInfo.getLinestringAtoV();
				}
			} else{
				if (startNodeInfo!=null){
					if (startNodeInfo.getLinestringAtoV().distance(endNodeInfo.getLinestringAtoV()) == 0){
						geom = endNodeInfo.getLinestringAtoV();
					} else if (startNodeInfo.getLinestringAtoV().distance(endNodeInfo.getLinestringVtoB()) == 0){
						geom = endNodeInfo.getLinestringVtoB();
					} else if (startNodeInfo.getLinestringVtoB().distance(endNodeInfo.getLinestringAtoV()) == 0){
						geom = endNodeInfo.getLinestringAtoV();
					} else if (startNodeInfo.getLinestringVtoB().distance(endNodeInfo.getLinestringVtoB()) == 0){
						geom = endNodeInfo.getLinestringVtoB();
					}
				} else{
					Coordinate[] coords = NodeUtils.CoordenadasArco(
							endNodeInfo.getEdge(), 
							routePath, 
							0, 
							1);
					geom = (LineString) geometryFactory.createLineString(coords);
				}
			}
			
			if (geom != null){
				result.addGeometryToCollection(geom);
				result.setLenthStreetMeters(Math.round(geom.getLength()));
			}
			
			
			
			Edge actualEdge = endNodeInfo.getEdge();
			if (actualEdge instanceof ILocalGISEdge){
				ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, pluginContext);
				String actualType = "";
				String actualName = "";
				if (descriptions != null && !descriptions.isEmpty() && descriptions.size() == 2){
					actualType = descriptions.get(0);
					actualName = descriptions.get(1);
				}

				if (actualType!=null){
					result.setTypeStreet(actualType);
				}

				if (actualName!=null){
					result.setNameStreet(actualName);
				}
			}
		}

		return result;
	}



	private static VirtualInfoRouteStretchBean buildEmptyVirtualInfoStretchBean(
			VirtualNodeInfo startVirtualNodeInfo, VirtualNodeInfo endVirtualNodeInfo,PlugInContext pluginContext,boolean isStart){
		VirtualInfoRouteStretchBean result = new VirtualInfoRouteStretchBean(startVirtualNodeInfo);
		Geometry startGeom = null;
		Geometry endGeom = null;
		
		//Comprobar la orientacion de las geometrias
		if(startVirtualNodeInfo.getLinestringAtoV().distance(endVirtualNodeInfo.getLinestringAtoV()) == 0){
			startGeom = startVirtualNodeInfo.getLinestringAtoV().reverse();
			endGeom = endVirtualNodeInfo.getLinestringAtoV();
		}else if(startVirtualNodeInfo.getLinestringAtoV().distance(endVirtualNodeInfo.getLinestringVtoB()) == 0){
			startGeom = startVirtualNodeInfo.getLinestringAtoV().reverse();
			endGeom = endVirtualNodeInfo.getLinestringVtoB().reverse();
		}if(startVirtualNodeInfo.getLinestringVtoB().distance(endVirtualNodeInfo.getLinestringAtoV()) == 0){
			startGeom = startVirtualNodeInfo.getLinestringVtoB();
			endGeom = endVirtualNodeInfo.getLinestringAtoV();
		}else if(startVirtualNodeInfo.getLinestringVtoB().distance(endVirtualNodeInfo.getLinestringVtoB()) == 0){
			startGeom = startVirtualNodeInfo.getLinestringVtoB();
			endGeom = endVirtualNodeInfo.getLinestringVtoB().reverse();
		}
		if(isStart){
			result.addGeometryToCollection(startGeom);
			result.setLenthStreetMeters(Math.round(startGeom.getLength()));
		}else{
			result.addGeometryToCollection(endGeom);
			result.setLenthStreetMeters(Math.round(endGeom.getLength()));
		}
		
		buildStartInfoRouteCardinalOrientation(result);
		
		Edge actualEdge = null;
		if(isStart)
			actualEdge = startVirtualNodeInfo.getEdge();
		else
			actualEdge = endVirtualNodeInfo.getEdge();
		if (actualEdge instanceof ILocalGISEdge){
			ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, pluginContext);
			String actualType = "";
			String actualName = "";
			if (descriptions != null && !descriptions.isEmpty() && descriptions.size() == 2){
				actualType = descriptions.get(0);
				actualName = descriptions.get(1);
			}

			if (actualType!=null){
				result.setTypeStreet(actualType);
			}

			if (actualName!=null){
				result.setNameStreet(actualName);
			}
		}
		return result;
		
	}

	private static VirtualInfoRouteStretchBean buildStartVirtualInfoStretchBean(
			VirtualNodeInfo startVirtualNodeInfo, VirtualNodeInfo endVirtualNodeInfo, 
			Path routePath, ArrayList<InfoRouteStretchBean> stretchtBeans,
			GeometryFactory geoFactory, PlugInContext pluginContext) {
		
		VirtualInfoRouteStretchBean result = null;
		Geometry geom = null;
		if (geoFactory == null){
			geoFactory = new GeometryFactory();
		}
		
		if (startVirtualNodeInfo != null ){
			
			//primero de todo intentamos obtener la geometría original.
			result = new VirtualInfoRouteStretchBean(startVirtualNodeInfo);
			
			boolean aToV =  false;
			
			if (routePath.getEdges()!=null && !routePath.getEdges().isEmpty() && routePath.size() > 1){
				Geometry firstEdgeGeometry = stretchtBeans.get(0).getGeometries().get(0);
				if (firstEdgeGeometry.distance(startVirtualNodeInfo.getLinestringAtoV()) >
				firstEdgeGeometry.distance(startVirtualNodeInfo.getLinestringVtoB()) ){
					geom = startVirtualNodeInfo.getLinestringVtoB();
				}else{
					geom = startVirtualNodeInfo.getLinestringAtoV();
					aToV =  true;
				}
			} else{
				if (endVirtualNodeInfo !=null){
					if (startVirtualNodeInfo.getLinestringAtoV().distance(endVirtualNodeInfo.getLinestringAtoV()) == 0){
						geom = startVirtualNodeInfo.getLinestringAtoV();
						aToV =  true;
					} else if (startVirtualNodeInfo.getLinestringAtoV().distance(endVirtualNodeInfo.getLinestringVtoB()) == 0){
						geom = startVirtualNodeInfo.getLinestringAtoV();
						aToV =  true;
					} else if (startVirtualNodeInfo.getLinestringVtoB().distance(endVirtualNodeInfo.getLinestringAtoV()) == 0){
						geom = startVirtualNodeInfo.getLinestringVtoB();
					} else if (startVirtualNodeInfo.getLinestringVtoB().distance(endVirtualNodeInfo.getLinestringVtoB()) == 0){
						geom = startVirtualNodeInfo.getLinestringVtoB();
					}
				} else{
					Coordinate[] coords = NodeUtils.CoordenadasArco(
							startVirtualNodeInfo.getEdge(), 
							routePath, 
							0, 
							1);
					geom = (LineString) geoFactory.createLineString(coords);
				}
			}
			
			if (geom != null){
				Coordinate firstCoordinate = null;
				if (aToV){
					firstCoordinate = ((XYNode)startVirtualNodeInfo.getEdge().getNodeA()).getCoordinate();
				} else{
					firstCoordinate = ((XYNode)startVirtualNodeInfo.getEdge().getNodeB()).getCoordinate();
				}
				
				if (firstCoordinate != null){
					if (((LineString)geom).getGeometryN(0).getCoordinates()[0].distance(firstCoordinate) == 0){
					
						geom = GeometryUtil.reverseGeometry(geom, true);
					}
				}
							
				result.addGeometryToCollection(geom);
				result.setLenthStreetMeters(Math.round(geom.getLength()));
			}
			
			
			
			
			buildStartInfoRouteCardinalOrientation(result);		
			
			
			
			Edge actualEdge = startVirtualNodeInfo.getEdge();
			if (actualEdge instanceof ILocalGISEdge){
				ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, pluginContext);
				String actualType = "";
				String actualName = "";
				if (descriptions != null && !descriptions.isEmpty() && descriptions.size() == 2){
					actualType = descriptions.get(0);
					actualName = descriptions.get(1);
				}

				if (actualType!=null){
					result.setTypeStreet(actualType);
				}

				if (actualName!=null){
					result.setNameStreet(actualName);
				}
			}
		}
		
		
		return result;
	}





	private static void buildStartInfoRouteCardinalOrientation(VirtualInfoRouteStretchBean infoRouteStretchBean) {
		// Obtenemos la primra geometría del startInfoRoute
		if (infoRouteStretchBean!=null){
			
			if (infoRouteStretchBean.getGeometries()!=null && !infoRouteStretchBean.getGeometries().isEmpty()){
				Geometry startgeometry = infoRouteStretchBean.getGeometries().get(0);
				LineString lineString = null;
				
				if (startgeometry != null){
					if (startgeometry instanceof LineString){
						lineString = (LineString) startgeometry;
					} else if (startgeometry instanceof MultiLineString){
						lineString = (LineString) ((MultiLineString)startgeometry).getGeometryN(0); 
					}
				
					
					
					
					
					
					if (lineString != null){
						infoRouteStretchBean.setDegreeOrientation(getAngleBetweenCoordiantes(
								lineString.getCoordinateN(0), 
								lineString.getCoordinateN(1)
						));
						
//						infoRouteStretchBean.setDegreeOrientation(getAngleBetweenCoordiantes(
//								lineString.getCoordinateSequence().getCoordinate(lineString.getCoordinateSequence().size()-1), 
//								lineString.getCoordinateSequence().getCoordinate(lineString.getCoordinateSequence().size()-2)
//						));
					}
				}
			}

			
			

			long angle = infoRouteStretchBean.getDegreeOrientation();
			if (angle>=0 && angle<=360){
				if (angle >= NORTH_START && angle <= NORTH_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTH);
				} else if (angle > NORTH_END && angle < WEST_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTHWEST);
				} else if (angle >= WEST_START && angle <= WEST_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.WEST);
				} else if (angle > WEST_END && angle < SOUTH_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTHWEST);
				} else if (angle >= SOUTH_START && angle < SOUTH_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTH);
				} else if (angle > SOUTH_END && angle < EAST_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTHEAST);
				} else if (angle > EAST_END && angle < NORTH_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTHEAST);
				} else if (angle >= EAST_START || angle <= EAST_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.EAST);
				} 
			}
		}
	}
	
	
	
	private static long getAngleBetweenCoordiantes(Coordinate coord1, Coordinate coord2){
		int angulo = -1;
		try{
			// Usamos el ángulo entre dos vectores... El angulo entre el vector que representa el primer trozo de lineString y
			// y el vector que representa el eje x en sentido positivo ((0,0);(0,1))
//			double resultado = (int) (Math.atan2(coord2.y-coord1.y,coord2.x-coord1.x)* 180 / Math.PI);
			
			double yPoint = coord2.y-coord1.y;
			double xPoint = coord2.x-coord1.x;
			double resultado = Math.atan2(yPoint,xPoint);
			
			if (resultado < 0){
				resultado = (2 * Math.PI) + resultado;
			}
			return Math.round(Math.toDegrees(resultado));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return angulo;
	}
	
	
	private static Geometry getOriginalGeometryFromEdgeIdLayerIdFeature(ILocalGISEdge edge, int SRID){
		
		try{
			InfoRouteDAOInterface routeDAO = new InfoRouteDAOPostgreSQLImplementation();	
			return routeDAO.getLinestringFromIdEdge(routeDAO.getGeopistaSQLConnection(), edge, SRID);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	
	
	
	private static String getTypeStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr){
		String typeColumnDescriptor = "";
		String typeDescription = "";
		InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation();
		if (edge instanceof LocalGISStreetDynamicEdge){
			try{
				LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) edge;
				typeColumnDescriptor = ((NetworkProperty) networkMgr.getNetwork(getRedNameFromSubRed(subred,networkMgr)).getProperties().
						get("TypeColumnDescriptor")).getValue(
								Integer.toString(streetEdge.getIdLayer())
						);

				if (typeColumnDescriptor != null && !typeColumnDescriptor.equals("")){
					typeDescription = routeDao.getColumnsDescriptorFromIdLayer(streetEdge.getIdLayer(), streetEdge.getIdFeature(), typeColumnDescriptor);
				}
			} catch (Exception e) {
				return typeDescription;
			}
		}
		return typeDescription;
	}
	
	
	
	private static String getNameStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr){
		String columnDescriptor = "";
		String nameStreet = "";
		InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation();
		if (edge instanceof ILocalGISEdge){
			try{

				columnDescriptor = ((NetworkProperty) networkMgr.getNetwork(getRedNameFromSubRed(subred,networkMgr)).getProperties().
						get("ColumnDescriptor")).getValue(
								Integer.toString(((ILocalGISEdge)edge).getIdLayer())
						);
				if (columnDescriptor != null && !columnDescriptor.equals("")){
					nameStreet = routeDao.getColumnsDescriptorFromIdLayer(((ILocalGISEdge) edge).getIdLayer(), ((ILocalGISEdge) edge).getIdFeature(), columnDescriptor);
				}
			} catch (Exception e) {
				nameStreet = Integer.toString(((ILocalGISEdge) edge).getIdFeature());
			}
		}
		return nameStreet;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private static String getRedNameFromSubRed(String subred, NetworkManager networkMgr) {
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
		IGeopistaLayer layer = null;

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
		for (int i = 1;i<edges_camino.size()-1;i++){
			Edge edge_camino = (Edge) edges_camino.get(i);
			if (edge_camino instanceof ProxyEdge){
				edge_camino = (Edge) ((ProxyEdge)edge_camino).getGraphable();
			}
			if (edgesFeatureCol == null){
				if (edge_camino instanceof LocalGISDynamicEdge){
					edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
				} else if (edge_camino instanceof LocalGISStreetDynamicEdge){
					edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
				} else if (edge_camino instanceof NetworkLink){
					FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
					.createBlankFeatureCollection();

					networkLinkFeatureCol.getFeatureSchema().addAttribute("edgeId",
							AttributeType.INTEGER);
					networkLinkFeatureCol.getFeatureSchema().addAttribute("cost",
							AttributeType.DOUBLE);
					edgesFeatureCol = networkLinkFeatureCol;
				} else{
					FeatureCollection networkLinkFeatureCol = AddNewLayerPlugIn
					.createBlankFeatureCollection();

					networkLinkFeatureCol.getFeatureSchema().addAttribute("edgeId",
							AttributeType.INTEGER);
					networkLinkFeatureCol.getFeatureSchema().addAttribute("cost",
							AttributeType.DOUBLE);
					edgesFeatureCol = networkLinkFeatureCol;
				}
			}
			
			if(edge_camino instanceof NetworkLink){
				//continue;
			}else if (!(edge_camino instanceof ILocalGISEdge)){
				//continue
			}else if (lastIdLayer != ((ILocalGISEdge) edge_camino).getIdLayer()){

				layer = getOriginalLayer(edge_camino, context);
				if (layer != null){
					lastIdLayer = layer.getId_LayerDataBase();
				} else{
					lastIdLayer = ((ILocalGISEdge) edge_camino).getIdLayer();
				}
			}

			ArrayList<Object> originalFeatures = new ArrayList<Object>();

			if (layer != null && !(edge_camino instanceof NetworkLink)){
				originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
						getWrappee().getFeatures());
			}

			Iterator it = originalFeatures.iterator();

			Feature featureedges = null;
			GeopistaFeature f2 = null;
			while (it.hasNext()&&edge_camino instanceof ILocalGISEdge){
				f2 = (GeopistaFeature) it.next();
				if (Integer.parseInt(f2.getSystemId()) == ((ILocalGISEdge)edge_camino).getIdFeature()){
					featureedges = f2;
					break;
				}
			}

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

		// añadir edges virtuales al camino dibujado
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

	}// fin del método dibujarcaminos
	
	
	
	
	
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
		InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation();
		
		Connection conn = routeConnection.getConnection();
		layerID = routeDao.getQueryFromIdLayer(((ILocalGISEdge) edge_camino).getIdLayer());
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
