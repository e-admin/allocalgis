/**
 * DescriptionUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.geotools.graph.util.geom.GeometryUtil;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.graph.structure.proxy.ProxyGeographicNode;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.util.NodeUtils;

import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.EdgesGeometryProducer;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.CardinalDirections;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.RouteStretchInterface;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.TurnRouteStreetchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.VirtualInfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import es.uva.idelab.route.algorithm.SidewalkEdge;

public class DescriptionUtils
{
	// variables estaticas que determinan grados para las orientaciones
	// del primer tramo de la ruta.
	// ï¿½En quï¿½ calle empieza la ruta? ï¿½En quï¿½ Direccion? Norte/Sur... etc
	public static final int NORTH_START = 80;
	public static final int NORTH_END = 100;
	public static final int WEST_START = 170;
	public static final int WEST_END =  190;
	public static final int SOUTH_START = 260;
	public static final int SOUTH_END = 280;
	public static final int EAST_START = 350;
	public static final int EAST_END = 10;
    public static HashMap<String, Network> getConfiguratorNetworks(NetworkManager networkManager)
    {
        HashMap<String, Network> configuatorNetworks = new HashMap<String, Network>();
        CalcRutaConfigFileReaderWriter configProperties = new CalcRutaConfigFileReaderWriter();
        
        String redes[] = configProperties.getRedesNames();
        for(int i = 0; i < redes.length; i++){
        	configuatorNetworks.put(redes[i], ((LocalGISNetworkManager)networkManager).getAllNetworks().get(redes[i]) );
        }
        return configuatorNetworks;
    }

    public static String getColumnsDescriptorFromTramos(int idRelatedTramo, int idLayerTramos, String column, InfoRouteDAOInterface routeDao)
    {
        Connection con = routeDao.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        String sqlQuery = "SELECT * FROM network_edges where id_edge="+idRelatedTramo;
	String sqlQueryDef=null;
        try {
        	
        	preparedStatement = con.prepareStatement(sqlQuery);
        	rs = preparedStatement.executeQuery ();
        	while(rs.next())
        	{ 
        		 int idLayer=rs.getInt("id_layer");
        		 if (idLayer==idLayerTramos)
        		 {
        			 int idFeature=rs.getInt("id_feature");
    
        			sqlQueryDef = "SELECT "+column+" FROM tramosvia where id="+idFeature;
				PreparedStatement statement = con.prepareStatement(sqlQueryDef);
        			 rs =statement.executeQuery();
        			 if (rs.next())
        			 {
        				 return rs.getString(column);
        			 }
        			 else
        				 return "\"sin nombre\"";
        			 
        		 }
        		
        	}

        	preparedStatement.close();
        	rs.close();
		throw new IllegalStateException("Arco no está asociado a capa tramos.");
        } catch (SQLException e) {
            	System.out.println("Error al obtener nombre desde tramos: QUERY:"+sqlQueryDef);
        	e.printStackTrace();
        } finally {
        	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
        }
    
        return "[No identificada]";
    }

    public static String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column, InfoRouteDAOInterface routeDao)
    {
        if(column == null || column.equals("SIN TIPO")) return "";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        
        Connection con = routeDao.getConnection();
        try {
        	String sqlQuery = "SELECT " + column  +" FROM " + NetworkModuleUtil.getLayerIDFromIntIdLayer(con, idLayer) + " resultTable where resultTable.id = " + idFeature ;
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

    public static String getTypeStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr, InfoRouteDAOInterface routeDao)
    {
    	String typeColumnDescriptor = "";
    	String typeDescription = "calle";
    	if (edge instanceof EquivalentEdge)
	    {
		edge = (Edge) ((EquivalentEdge) edge).getEquivalentTo();
	    }
    	if (edge instanceof PMRLocalGISStreetDynamicEdge)
    	    {
    		typeDescription = ((PMRLocalGISStreetDynamicEdge)edge).getsEdgeType();
    	    }
    	else
    	if (edge instanceof LocalGISStreetDynamicEdge){
    		try{
    			LocalGISStreetDynamicEdge streetEdge = (LocalGISStreetDynamicEdge) edge;
    			Network network =  NetworkModuleUtil.getSubNetwork(networkMgr, subred, null);
			NetworkProperty networkProperty = (NetworkProperty) network.getProperties().
    					get("TypeColumnDescriptor");
			typeColumnDescriptor = networkProperty==null?null:networkProperty.getValue(Integer.toString(streetEdge.getIdLayer()));
    
    			if (typeColumnDescriptor != null && !typeColumnDescriptor.equals(""))
    			    {
    				typeDescription = getColumnsDescriptorFromIdLayer(streetEdge.getIdLayer(), streetEdge.getIdFeature(), typeColumnDescriptor,routeDao);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return typeDescription;
    }

    public static String getRedNameFromSubRed(String subred, NetworkManager networkMgr) {
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
/**
 * Calcula las descripciones de los arcos de la ruta sin tener en cuenta los recortes de geometrías en el inicio ni el final
 * @param routePath
 * @param endNodeInfo 
 * @param startNodeInfo 
 * @param geometryFactory
 * @param netManager
 * @param filteredNetworks 
 * @param geomProducer
 * @param routeDao
 * @return
 */
    public static ArrayList<RouteStretchInterface> buildPathDescriptionBeans(Path routePath, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo, GeometryFactory geometryFactory, 
    		NetworkManager netManager,
    		Collection<Network> filteredNetworks, EdgesGeometryProducer geomProducer, 
    		InfoRouteDAOInterface routeDao)
    	{
    	    ArrayList<RouteStretchInterface> stretchtBeans = new ArrayList<RouteStretchInterface>();
    	    
    	    if (routePath!= null && !routePath.isEmpty() && routePath.isValid())
    		{
    		
    	    	List edges = routePath.getEdges();
    	    	if (edges!=null && !edges.isEmpty())
    	    	    {
    	    		// Generamos los InfoRouteStretchBean de los edges del path
    	    		InfoRouteStretchBean infoRouteStretchBean = null;
    	    		Node stage = routePath.getFirst();
    	    		stage=(Node) NodeUtils.unwrapProxies(stage);
   // TODO: Los arcos están almacenados en sentido inverso en el Path
    	    		for(int i=edges.size()-1; i >= 0; i--)
 //   	    		for(int i=0;i<edges.size(); i++)
    	    		    {
    	    		//Recorremos la lista de edges para obtener descripciones, geometrias, etc.
    	    		
    	    		    
    	    		Edge currentEdge = (Edge) edges.get(i);
    	    		if (currentEdge!=null)	   
    	    		{
    	    				// Si el tramo es un ProxyEdge cogemos el getGraphable().
    	    				currentEdge=(Edge)NodeUtils.unwrapProxies(currentEdge);
    	    				    
    	    				Edge origEdge=currentEdge; // Con los tramos cortados no coincide
    	    				
    	    				if(currentEdge instanceof EquivalentEdge)
    	    				    {
    	    					EquivalentEdge equivalentEdge = (EquivalentEdge) origEdge;
    	    					origEdge =(Edge) equivalentEdge.getEquivalentTo();
    	    				    }
    //	    				System.out.println(origEdge.getID());
    	    				
    	    				if(origEdge instanceof NetworkLink){
    	    					UniqueIDGenerator idGen = new SequenceUIDGenerator();
    	    					origEdge = new LocalGISDynamicEdge(origEdge.getNodeA(), origEdge.getNodeB(), idGen);
    	    					((ILocalGISEdge) origEdge).setIdLayer(-1);
    	    					((ILocalGISEdge) origEdge).setIdFeature(origEdge.getID());
    	    					((ILocalGISEdge) origEdge).setEdgeLength(((ProxyGeographicNode)origEdge.getNodeA()).getPosition().distance(((ProxyGeographicNode)origEdge.getNodeB()).getPosition()));
    	    					continue;
    	    				}
    	    				
					//			
    	    			
    	    				int routeNodes = routePath.size();
					if (i==0 && routeNodes>2)
    	    				    { //  del extremo final
    	    					VirtualInfoRouteStretchBean endVirtualInfoRouteStretchBean = buildEndVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, netManager,filteredNetworks, geomProducer, routeDao); 
    	    					infoRouteStretchBean=endVirtualInfoRouteStretchBean;
    	    				    }
    	    				else
    	    				if (i==edges.size()-1 && routeNodes>2)
    	    				    {//  del extremo inicial
    	    	    				VirtualInfoRouteStretchBean startVirtualInfoRouteStretchBean = buildStartVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, netManager, filteredNetworks,geomProducer,routeDao);
    	    					infoRouteStretchBean=startVirtualInfoRouteStretchBean;
    	    				    }
    	    				else
					    {
						Geometry geom;
						
						if (routeNodes == 2) // un único tramo TODO Obtener detalle de la ruta ahora está sobre simplificado
						    {
//							double[] start = startNodeInfo.getPoint().getDirectPosition().getCoordinate();
//							Coordinate startCoord=new Coordinate(start[0],start[1]);
//							double[] end = endNodeInfo.getPoint().getDirectPosition().getCoordinate();
//							Coordinate endCoord=new Coordinate(end[0],end[1]);
//							Coordinate[] coordinates= new Coordinate[]{startCoord,endCoord}; 
//							geom= geometryFactory.createLineString(coordinates);
							VirtualInfoRouteStretchBean startBean = buildStartVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, netManager, filteredNetworks,geomProducer,routeDao);
	    	    					VirtualInfoRouteStretchBean endBean = buildEndVirtualInfoStretchBean(startNodeInfo, endNodeInfo, routePath, stretchtBeans, geometryFactory, netManager,filteredNetworks, geomProducer, routeDao); 
							Geometry geomStart=startBean.getGeometries().get(0);
							Geometry geomEnd=endBean.getGeometries().get(0);
							ArrayList<Coordinate> coords1 = (ArrayList<Coordinate>) Arrays.asList(geomStart.getCoordinates());
							ArrayList<Coordinate> coords2 = (ArrayList<Coordinate>) Arrays.asList(geomEnd.getCoordinates());
							ArrayList<Coordinate> common=new ArrayList<Coordinate>();
							for (Coordinate coordinate : coords1)
							    {
								if (coords2.contains(coordinate))
								    common.add(coordinate);
							    }
							geom=geometryFactory.createLineString((Coordinate[]) common.toArray());
							
						    } else
						    {
						// Geometria de un arco interior
							geom = geomProducer.getEdgeGeometry(currentEdge, origEdge, routePath);
							// orientation
							if (geom != null && !currentEdge.getNodeA().equals(stage))
							    {
								geom = geom.reverse();
							    }
						    }
						//
						// Intentamos obtener el tipo y la descripcion de la calle que represnta el edge.
						ArrayList<String> descriptions = getTypeAndDescriptionStreet(currentEdge, netManager, null, routeDao);
						String actualType = "";
						String actualName = "";
						if (descriptions != null && !descriptions.isEmpty() && descriptions.size() == 2)
						    {
							actualType = descriptions.get(0);
							actualName = descriptions.get(1);
						    }

						long geomLenght = Math.round(geom.getLength());
						long lenght = 0;
						Double time = null;
						if (currentEdge instanceof EdgeWithCost)
						    {
							EdgeWithCost edgeWithCost = (EdgeWithCost) currentEdge;
							lenght = Math.round(edgeWithCost.getCost(stage, 1));
							double nominalMaxSpeed = 0;

							if (origEdge instanceof LocalGISStreetDynamicEdge)
							    {
								LocalGISStreetDynamicEdge street = (LocalGISStreetDynamicEdge) origEdge;
								nominalMaxSpeed = street.getNominalMaxSpeed();
							    }
							if (nominalMaxSpeed != 0)
							    {
								time = edgeWithCost.getCost(stage, nominalMaxSpeed);
							    }
						    } else
						    {
							lenght = geomLenght;
						    }

						// JPC if (infoRouteStretchBean == null || actualName==null || actualName.equals("") || actualType==null ||
						// JPC !actualType.equals(infoRouteStretchBean.getTypeStreet()) ||
						// JPC !actualName.equals(infoRouteStretchBean.getNameStreet()) )
						// JPC {

						// JPC if (infoRouteStretchBean!=null){// almaceno anterior bean
						// JPC result.add(infoRouteStretchBean);
						// JPC }
						// genero nuevo bean para el siguiente paso
						infoRouteStretchBean = new InfoRouteStretchBean();
						infoRouteStretchBean.addEdgeToCollection(currentEdge);
						infoRouteStretchBean.addGeometryToCollection(geom);
						infoRouteStretchBean.setTypeStreet(actualType);
						infoRouteStretchBean.setNameStreet(actualName);

						infoRouteStretchBean.setTime(time);
						infoRouteStretchBean.setLengthStreetMeters(lenght);
						/*
						 * No fusiona tramos de la misma denominación } else{
						 * 
						 * infoRouteStretchBean.addGeometryToCollection(geom); infoRouteStretchBean.setLengthStreetMeters(
						 * infoRouteStretchBean.getLenthStreetMeters() + lenght); Double prevTime = infoRouteStretchBean.getTime(); if
						 * (prevTime==null && time!=null) prevTime=0d; if (prevTime!=null && time!=null)
						 * infoRouteStretchBean.setTime(prevTime+time); else infoRouteStretchBean.setTime(null);//Si alguno de los
						 * tramos tiene un tiempo desconocido, // se marca el tiempo acumulado como desconocido
						 * 
						 * 
						 * if (currentEdge != null) { infoRouteStretchBean.addEdgeToCollection(currentEdge); } }
						 */
					    }
    	    			stretchtBeans.add(infoRouteStretchBean);
    	    			// Obtiene el siguiente nodo del camino
    	    			stage=currentEdge.getOtherNode(stage);
    	    			}
    	    		else
    	    		    {
    	    			throw new IllegalStateException("Path malformed!");
    	    		    }
    	    		}
//    	    			result.add(infoRouteStretchBean);
      	    	}
    	    }
    	    return stretchtBeans;
    	}
/**
 * 
 * @param actualEdge
 * @param netManager
 * @param networks subset of networks for searching edges in. Null means all networks in networkmanager
 * @param routeDao
 * @return
 */
    public static ArrayList<String> getTypeAndDescriptionStreet(Edge actualEdge, NetworkManager netManager,Collection<Network> networks, InfoRouteDAOInterface routeDao)
    {
	if (networks==null)
	    networks=netManager.getNetworks().values();
        ArrayList<String> result = new ArrayList<String>();
        String subred = "";
        if (netManager!=null)
            {
		Iterator<Network> nets = networks.iterator();
        	while (nets.hasNext()){
        		BasicNetwork bnet = (BasicNetwork) nets.next();
        		if (bnet.getGraph().getEdges().contains(actualEdge)){
        			subred = bnet.getName();
        			break;
        		}
        	}
        	
        	
        	try{
        		if (actualEdge!= null && subred!=null && !subred.equals("") && netManager!=null)
        		    {
        			String typeDescriptionResult = getTypeStreetDescriptor(actualEdge, subred , netManager,routeDao);
        			result.add(typeDescriptionResult);
        		} else{
        			result.add("");
        		}
        	}catch (Exception e) {
        	    	e.printStackTrace();
        		result.add("");
        	}
        	
        	try{
        		if (actualEdge!= null && subred!=null && !subred.equals("") && netManager!=null){
        			String typeDescriptionResult = getNameStreetDescriptor(actualEdge, subred, netManager,routeDao);
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
/**
 * Contiene código específico para el proyecto MODELO PMR
 * TODO refactorizar la localización de capas de geometría y descripciones
 * @param edge
 * @param subred
 * @param networkMgr
 * @param routeDao
 * @return
 */
    public static String getNameStreetDescriptor(Edge edge, String subred, NetworkManager networkMgr, InfoRouteDAOInterface routeDao)
    {
        String columnDescriptor = "";
        String nameStreet = "";
        if (edge instanceof EquivalentEdge)
            {
        	edge = (Edge) ((EquivalentEdge) edge).getEquivalentTo();
            }
        
        if (edge instanceof PMRLocalGISStreetDynamicEdge)
        	{
        		// Específico para aceras TODO generalizar para otras capas
        		int idRelatedTramo=((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId();
        		if (DescriptionUtils.idLayerTramos==-1)
        		    {
        			DescriptionUtils.idLayerTramos= routeDao.getIdLayerFromName("mdl_tramos_vias");
        			if (DescriptionUtils.idLayerTramos==-1)
        			    DescriptionUtils.idLayerTramos= routeDao.getIdLayerFromName("tramosvia");
        		    }
        		// TODO Obtener el id de la capa de TramosdeCalle
        		nameStreet= getColumnsDescriptorFromTramos(idRelatedTramo,DescriptionUtils.idLayerTramos, "denominacion",routeDao);
        	}
        else
        if (edge instanceof ILocalGISEdge){
        	try{
    
        		int idLayer = ((ILocalGISEdge)edge).getIdLayer();
//        		String redNameFromSubRed = getRedNameFromSubRed(subred,networkMgr);
        		Network network = NetworkModuleUtil.getSubNetwork(networkMgr, subred,null);
//			Network network = networkMgr.getNetwork(redNameFromSubRed);
        		NetworkProperty networkProperty = (NetworkProperty) network.getProperties().get("ColumnDescriptor");
        		columnDescriptor = networkProperty!=null?networkProperty.getValue(Integer.toString(idLayer)):null;
        		// Código específico para PMR
        		if (columnDescriptor==null && edge instanceof PMRLocalGISStreetDynamicEdge)
        		    {
        			columnDescriptor="denominacion";
        		    }
        		
        		if (columnDescriptor != null && !columnDescriptor.equals(""))
        		{
        		    	int idFeature = ((ILocalGISEdge) edge).getIdFeature();
        			nameStreet = routeDao.getColumnsDescriptorFromIdLayer(idLayer, idFeature, columnDescriptor);
        			if (nameStreet.equals(""))
        			    {
        				throw new RuntimeException("Name for the Edge "+edge+"not found. Layer:"+idLayer+" Feature:"+idFeature+" Attr:"+columnDescriptor);
        			    }
        		}
        	} catch (Exception e) {
        	    	e.printStackTrace();
        		nameStreet = "Sin nombre (Feat:"+Integer.toString(((ILocalGISEdge) edge).getIdFeature())+")";
        	}
        }
        return nameStreet;
    }
/**
 * Identificador de la capa de tramos para esta instalación.
 * Se averigua mediante prueba y error la primera vez que se usa
 * @see 
 */
    public static int idLayerTramos=-1;
    
public static String getTextualDescription( RouteStretchInterface value,List<RouteStretchInterface> list, ResourceBundle rb)
{
    StringBuilder htmlText = new StringBuilder();//rb.getString("routeengine.route.description.dialog.no.stretch.info"));
    
    if (value instanceof TurnRouteStreetchBean)
        {
    	if (((TurnRouteStreetchBean) value).getTurnNode() != null)
    	    {
    		htmlText
    				//.append("<ul>")
    				.append(((TurnRouteStreetchBean) value).getDescription())
    				//.append("</html></ul>")
    				;
    	    } else
    	    {
    		htmlText.append(((TurnRouteStreetchBean) value).getDescription());
    	    }
        } else if (value instanceof InfoRouteStretchBean)
        {
    	InfoRouteStretchBean info = (InfoRouteStretchBean) value;

    	//htmlText.append( "<ul>");

    	String typeStreet = info.getTypeStreet();
    	if (typeStreet != null && !typeStreet.equals(""))
    	    {
    		if (typeStreet.equals("EDGE"))
    		    {
    			htmlText.append( rb.getString("routeengine.route.description.dialog.info1"));
    			htmlText.append( " por la acera"); // TODO localizar texto i18n
    			String ladoMsg = "";
    			// determina lado TODO refactor metodo
    			Collection<Edge> sidewalks = info.getEdges();
    			for (Edge edge : sidewalks)
    			    {
    				if (edge instanceof PMRLocalGISStreetDynamicEdge)
    				    {
    					PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge) edge;
    					int side = pmrEdge.getSide();
    					if (side == SidewalkEdge.LEFT)
    					    ladoMsg = " de la izquierda";
    					else if (side == SidewalkEdge.RIGHT)
    					    ladoMsg = " de la derecha";
    					break;
    				    }
    			    }

    			htmlText.append( ladoMsg);
    		    } else if (typeStreet.equals("ZEBRA"))
    		    {
    			htmlText.append( "Cruce por el paso de cebra "); // TODO localizar texto i18n
    		    } else
    		    {
    			htmlText.append( "Continúe en el mismo sentido "); // TODO localizar texto i18n
    		    }
    	    } else
    	    {
    		if (info.hasStreetEdges())
    		    {
    			htmlText.append( " " ).append(rb.getString("routeengine.route.description.dialog.info1_2")).append(" ");
    			htmlText.append(rb.getString("routeengine.route.description.default.street.type")).append( " ");
    		    }
    	    }

    	if (info.getNameStreet() != null && !info.getNameStreet().equals(""))
    	    {

    		htmlText.append(" en la calle ").append(info.getNameStreet()).append(" ");
    	    }
    	DecimalFormat doubleFormatter = new DecimalFormat("#0.0");

    	long lenthStreetMeters = info.getLenthStreetMeters();
    	if (lenthStreetMeters >= 0)
    	    {
    		StringBuilder distanceHtml=new StringBuilder();
    		if (lenthStreetMeters > 600)
    		    {
    			try
    			    {
    				distanceHtml.append(doubleFormatter.format(lenthStreetMeters / 1000.0))
    					.append( " ").append(rb.getString("routeengine.route.description.dialog.kilometres"));
    			    } catch (ArithmeticException e)
    			    {
    				distanceHtml.append( (lenthStreetMeters / 1000.0) ).append(" ").append(rb.getString("routeengine.route.description.dialog.kilometres"));
    			    }
    		    } else
    		    {
    			distanceHtml.append(lenthStreetMeters).append(" ").append(rb.getString("routeengine.route.description.dialog.metres"));
    		    }

    		htmlText.append(" ").append( rb.getString("routeengine.route.description.dialog.info2")).append(distanceHtml).append(".");

    	    }

    	// Muestra resumen de lo obstáculos y anchura.
    	Collection<Edge> edges = info.getEdges();
    	double minWidth = -1;
    	double minTransversalSlope = 0;
    	double minLongitudinalSlope = 0;
    	for (Edge edge : edges)
    	    {
    		if (edge instanceof EquivalentEdge)
    		    {
    			edge = (Edge) ((EquivalentEdge) edge).getEquivalentTo();
    		    }

    		if (edge instanceof PMRLocalGISStreetDynamicEdge)
    		    {
    			PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge) edge;
    			minWidth = Math.max(minWidth, pmrEdge.getWidth());
    			minTransversalSlope = Math.max(minTransversalSlope, pmrEdge.getTransversalSlope());
    			minLongitudinalSlope = Math.max(minLongitudinalSlope, pmrEdge.getLongitudinalSlope());
    		    }
    		if (edge instanceof ZebraDynamicEdge)
    		    {
    			minWidth = 4; // Evita informar del ancho en los pasos de cebra
    		    }
    	    }
    	// htmlText+="<ul>";
    	if (minWidth < 2 && minWidth>0) // aviso
    	    {
    		htmlText
    		//.append("<p>")
    		.append("Cuidado se estrecha un poco (").append(doubleFormatter.format(minWidth)).append(" m.)\n")
    		//.append("</p>")
    		;
    	    }
    	if (minTransversalSlope > 0) // aviso
    	    {
    		htmlText
//    			.append("<p>")
    			.append("Hay inclinación lateral (").append(doubleFormatter.format(minTransversalSlope)).append(" grados.\n)")
//    			.append("</p>")
    			;
    	    }
    	if (minLongitudinalSlope > 0) // aviso
    	    {
    		htmlText
//    			.append("<p>")
    			.append("Hay cierta pendiente (").append(doubleFormatter.format(minLongitudinalSlope)).append(" grados.)\n")
//    			.append("</p>")
    			;
    	    }
    	// htmlText+="</ul>";

    	if (info instanceof VirtualInfoRouteStretchBean)
    	    {
    		VirtualInfoRouteStretchBean vInfo = (VirtualInfoRouteStretchBean) info;

    		if (vInfo.getStretchCardinalDirection() != null)
    		    {
    			CardinalDirections cardinalDirection = vInfo.getStretchCardinalDirection();
//    			htmlText.append("<br>");
    			//htmlText.append("\n");
    				if (cardinalDirection.equals(CardinalDirections.NORTH))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.north"));
    			    } else if (cardinalDirection.equals(CardinalDirections.NORTHWEST))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.northwest"));
    			    } else if (cardinalDirection.equals(CardinalDirections.WEST))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append( rb.getString("routeengine.route.description.dialog.west"));
    			    } else if (cardinalDirection.equals(CardinalDirections.SOUTHWEST))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.southwest"));
    			    } else if (cardinalDirection.equals(CardinalDirections.SOUTH))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append( rb.getString("routeengine.route.description.dialog.south"));
    			    } else if (cardinalDirection.equals(CardinalDirections.SOUTHEAST))
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.southeast"));
    			    } else if (cardinalDirection.equals(CardinalDirections.EAST))
    			    {
    				htmlText.append(rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.east"));
    			    } else if (cardinalDirection.equals(CardinalDirections.NORTHEAST))
    			    {
    				htmlText.append(rb.getString("routeengine.route.description.dialog.info3"))
    					.append(rb.getString("routeengine.route.description.dialog.northeast"));
    			    } else
    			    {
    				htmlText.append( rb.getString("routeengine.route.description.dialog.info3"))
    					.append( rb.getString("routeengine.route.description.dialog.no.cardinaldirection.defined"));
    			    }
    		    }

    		
    		int i=list.indexOf(info);
    		if (i<0)
    		    throw new IllegalArgumentException("BeanInfo not in passed array of beaninfos.");
    		else
    		if (i + 1 < list.size()) // utiliza el siguiente paso del camino
    		    {
    			RouteStretchInterface nextValue = list.get(i + 1);
    			if (nextValue != null && nextValue instanceof InfoRouteStretchBean)
    			    {
    				InfoRouteStretchBean nextInfo = (InfoRouteStretchBean) nextValue;

    				if (nextInfo.getNameStreet() != null && !nextInfo.getNameStreet().equals(""))
    				    {
    					String typeNextStreet = nextInfo.getTypeStreet();
    					if ("EDGE".equals(typeNextStreet))
    					    {
    						htmlText.append( " hacia la calle "); // TODO i18n
    					    } else if ("ZEBRA".equals(typeNextStreet))
    					    {
    						htmlText.append( " hacia el paso de cebra "); // TODO i18n
    					    } else
    					    {
    						if (nextInfo.hasStreetEdges())
    						    {
    							htmlText.append(rb.getString("routeengine.route.description.default.street.type")).append(" ");
    						    }
    					    }
    					htmlText.append(nextInfo.getNameStreet());
    				    }
    			    }
    		    }

    	    }

//    	htmlText.append("</ul>");
        }
    return htmlText.toString();
}

public static ArrayList<RouteStretchInterface> buildInfoRouteStretchBeanList(Path routePath, VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
		NetworkManager netManager, Collection<Network> filteredNetworks,EdgesGeometryProducer geomProducer, InfoRouteDAOInterface routeDao, ResourceBundle rb)
	{
		GeometryFactory geometryFactory = new GeometryFactory();
		
		
		ArrayList<RouteStretchInterface> stretchtBeans=null;
		if(routePath.size() == 0){
//			startVirtualInfoRouteStretchBean = buildEmptyVirtualInfoStretchBean(startNodeInfo, endNodeInfo, netManager,filteredNetworks,routeDao,true);
//			endVirtualInfoRouteStretchBean = buildEmptyVirtualInfoStretchBean(startNodeInfo, endNodeInfo, netManager,filteredNetworks,routeDao,false);
			stretchtBeans=new ArrayList<RouteStretchInterface>();
		}else{
		    
			
			stretchtBeans = buildPathDescriptionBeans(routePath,startNodeInfo,endNodeInfo,geometryFactory, netManager,filteredNetworks, geomProducer,routeDao);
		}
		

//		if (startVirtualInfoRouteStretchBean != null){
//			if (stretchtBeans!=null && !stretchtBeans.isEmpty()){
//				InfoRouteStretchBean initialStretchBean = (InfoRouteStretchBean) stretchtBeans.get(0);
//				if (!initialStretchBean.getNameStreet().equals("")){
//					if (initialStretchBean.getTypeStreet().equals(startVirtualInfoRouteStretchBean.getTypeStreet()) && 
//							initialStretchBean.getNameStreet().equals(startVirtualInfoRouteStretchBean.getNameStreet())){
//
//						if (startVirtualInfoRouteStretchBean.addInfoRouteStretchBean(initialStretchBean)){
//							stretchtBeans.remove(initialStretchBean);
//						}
//
//					}
//				}
//			}
//			// Se aï¿½ade la informacion del primer tramo virtual a los elementos de informacion de ruta.
//			stretchtBeans.add(0,startVirtualInfoRouteStretchBean);
//		}
//
//		if (endVirtualInfoRouteStretchBean != null) {
//			if (stretchtBeans!=null && !stretchtBeans.isEmpty()){
//				InfoRouteStretchBean lastStretchBean = (InfoRouteStretchBean) stretchtBeans.get(stretchtBeans.size()-1);
//				if (lastStretchBean!=null && lastStretchBean.getNameStreet()!= null && !lastStretchBean.getNameStreet().equals(""))
//				    {
//					if (lastStretchBean.getTypeStreet().equals(endVirtualInfoRouteStretchBean.getTypeStreet()) && 
//							lastStretchBean.getNameStreet().equals(endVirtualInfoRouteStretchBean.getNameStreet()))
//					    {
//
//						if (lastStretchBean instanceof VirtualInfoRouteStretchBean)
//						    {
//							((VirtualInfoRouteStretchBean)lastStretchBean).addInfoRouteStretchBean(endVirtualInfoRouteStretchBean);
//							endVirtualInfoRouteStretchBean = null;
//						}else if (endVirtualInfoRouteStretchBean.addInfoRouteStretchBean(lastStretchBean)){
//							stretchtBeans.remove(lastStretchBean);
//						}
//					}
//				}
//			}
//			// Se aï¿½ade la informacion del ï¿½ltimo tramo virtual.
//			if (endVirtualInfoRouteStretchBean != null){
//				stretchtBeans.add(stretchtBeans.size(), endVirtualInfoRouteStretchBean);
//			}
//		}

		if (routePath.getFirst()!=null){
			stretchtBeans.add(0,new TurnRouteStreetchBean(routePath.getFirst(),rb.getString("routeengine.route.description.routestart")));
		}
		if (routePath.getLast()!=null){
			stretchtBeans.add(stretchtBeans.size(),new TurnRouteStreetchBean(routePath.getLast(),rb.getString("routeengine.route.description.routeend")));
		}
		
		
		correctGeometryOrientationInStretchBeans(routePath, stretchtBeans);
		return stretchtBeans;
}

public static VirtualInfoRouteStretchBean buildEndVirtualInfoStretchBean(
			VirtualNodeInfo startNodeInfo, VirtualNodeInfo endNodeInfo,
			Path routePath, ArrayList<RouteStretchInterface> stretchtBeans,
			GeometryFactory geometryFactory, NetworkManager netManager, 
			Collection<Network> configuredNetworks,EdgesGeometryProducer geomProducer, InfoRouteDAOInterface routeDao)
	{
//	    NetworkManager netManager = ((LocalGISNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(pluginContext));
//	    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());	
		if (configuredNetworks==null)
		    configuredNetworks=netManager.getNetworks().values();
		
		Geometry geom = null;
		VirtualInfoRouteStretchBean result = null;
		if (geometryFactory == null){
			geometryFactory = new GeometryFactory();
		}
		
		if (endNodeInfo != null){
			result = new VirtualInfoRouteStretchBean(endNodeInfo);
			geom = null;
			Node endNode=(Node)NodeUtils.unwrapProxies(routePath.getLast());
			
			
			if (routePath.size() > 1)
			    {
				
				List edges = routePath.getEdges();
				Edge lastEquivalentEdge= (Edge) edges.get(0);
				Node otherNode=lastEquivalentEdge.getOtherNode(endNode);
				
				if (otherNode.equals(lastEquivalentEdge.getNodeA()))
				    geom = endNodeInfo.getLinestringAtoV();
				else
				if (otherNode.equals(lastEquivalentEdge.getNodeB()))
				    geom = endNodeInfo.getLinestringVtoB().reverse();
				else
				    throw new IllegalStateException("El nodo del extremo no pertenece al arco final.");
				
//				Geometry lastEdgeGeometry = stretchtBeans.get(stretchtBeans.size()-1).getGeometries().get(stretchtBeans.get(stretchtBeans.size()-1).getGeometries().size()-1);
//				
//				if (lastEdgeGeometry.distance(endNodeInfo.getLinestringAtoV()) >
//				lastEdgeGeometry.distance(endNodeInfo.getLinestringVtoB()) ){
//					geom = endNodeInfo.getLinestringVtoB();
//				}else{
//					geom = endNodeInfo.getLinestringAtoV();
//				}
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
				result.setLengthStreetMeters(Math.round(geom.getLength()));
			}
			
			
			
			Edge actualEdge = endNodeInfo.getEdge();
			if (actualEdge instanceof ILocalGISEdge){
				ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, netManager,configuredNetworks,routeDao);
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

public static VirtualInfoRouteStretchBean buildEmptyVirtualInfoStretchBean(
			VirtualNodeInfo startVirtualNodeInfo, VirtualNodeInfo endVirtualNodeInfo,NetworkManager netManager,
			Collection<Network> configuredNetworks,
			InfoRouteDAOInterface routeDao, boolean isStart)
	{
	    
//	    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
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
			result.setLengthStreetMeters(Math.round(startGeom.getLength()));
		}else{
			result.addGeometryToCollection(endGeom);
			result.setLengthStreetMeters(Math.round(endGeom.getLength()));
		}
		
		buildStartInfoRouteCardinalOrientation(result);
		
		Edge actualEdge = null;
		if(isStart)
			actualEdge = startVirtualNodeInfo.getEdge();
		else
			actualEdge = endVirtualNodeInfo.getEdge();
		if (actualEdge instanceof ILocalGISEdge)
		    {
			ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, netManager,configuredNetworks, routeDao);
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

public static VirtualInfoRouteStretchBean buildStartVirtualInfoStretchBean(
			VirtualNodeInfo startVirtualNodeInfo, VirtualNodeInfo endVirtualNodeInfo, 
			Path routePath, ArrayList<RouteStretchInterface> stretchtBeans,
			GeometryFactory geoFactory, NetworkManager netManager, Collection<Network> configuredNetworks ,
			EdgesGeometryProducer geomProducer, InfoRouteDAOInterface routeDao) 
	{
//	    InfoRouteDAOInterface routeDao = new InfoRouteDAOPostgreSQLImplementation(new GeopistaRouteConnectionFactoryImpl());
		VirtualInfoRouteStretchBean result = null;
		Geometry geom = null;
		if (geoFactory == null){
			geoFactory = new GeometryFactory();
		}
		
		if (startVirtualNodeInfo != null )
		    {
			
			//primero de todo intentamos obtener la geometrï¿½a original.
			result = new VirtualInfoRouteStretchBean(startVirtualNodeInfo);
			
			boolean aToV =  false;
			Node startNode=(Node)NodeUtils.unwrapProxies(routePath.getFirst());
			List edges = routePath.getEdges();
			if (routePath.size() > 1)
			    {
				Edge firstEquivalentEdge= (Edge) edges.get(edges.size()-1);
				Node otherNode=firstEquivalentEdge.getOtherNode(startNode);
				if (otherNode.equals(firstEquivalentEdge.getNodeA()))
				    geom = startVirtualNodeInfo.getLinestringAtoV().reverse();
				else
				if (otherNode.equals(firstEquivalentEdge.getNodeB()))
				    geom = startVirtualNodeInfo.getLinestringVtoB();
				else
				    throw new IllegalStateException("El nodo del extremo no pertenece al arco inicial.");
			} else
			    { // Un solo tramo BUG este caso es imposible no puede haber un unico nodo
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
				
				// Reorienta geometria
				if (geom != null){
					Coordinate firstCoordinate = null;
					if (aToV){
						firstCoordinate = ((XYNode)startVirtualNodeInfo.getEdge().getNodeA()).getCoordinate();
					} else{
						firstCoordinate = ((XYNode)startVirtualNodeInfo.getEdge().getNodeB()).getCoordinate();
					}
					
					if (firstCoordinate != null)
					    {
						Coordinate[] coordinates = ((LineString)geom).getGeometryN(0).getCoordinates();
						double distance1 = coordinates[0].distance(firstCoordinate);
						double distance2 = coordinates[coordinates.length-1].distance(firstCoordinate);

						if (distance1 < distance2)
						    {
							geom = GeometryUtil.reverseGeometry(geom, true);
						    }
					}
				} // fin un solo tramo
				
			}
			result.addGeometryToCollection(geom);
			result.setLengthStreetMeters(Math.round(geom.getLength()));
			
			buildStartInfoRouteCardinalOrientation(result);		
			
			Edge actualEdge = startVirtualNodeInfo.getEdge();
			if (actualEdge instanceof ILocalGISEdge){

				ArrayList<String> descriptions = getTypeAndDescriptionStreet(actualEdge, netManager,configuredNetworks,routeDao);
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

public static void correctGeometryOrientationInStretchBeans(Path path, ArrayList<RouteStretchInterface> listStretchBeans){
	if (listStretchBeans!= null && !listStretchBeans.isEmpty()){
		for(int i=0; i < listStretchBeans.size(); i++){
			InfoRouteStretchBean actualStretchBean = (InfoRouteStretchBean) listStretchBeans.get(i);
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

public static long getAngleBetweenCoordinates(Coordinate coord1, Coordinate coord2){
		int angulo = -1;
		try{
			// Usamos el ï¿½ngulo entre dos vectores... El angulo entre el vector que representa el primer trozo de lineString y
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

public static void buildStartInfoRouteCardinalOrientation(VirtualInfoRouteStretchBean infoRouteStretchBean)
	{
		// Obtenemos la primra geometrï¿½a del startInfoRoute
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
						infoRouteStretchBean.setDegreeOrientation(getAngleBetweenCoordinates(
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
				if (angle >= DescriptionUtils.NORTH_START && angle <= DescriptionUtils.NORTH_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTH);
				} else if (angle > DescriptionUtils.NORTH_END && angle < DescriptionUtils.WEST_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTHWEST);
				} else if (angle >= DescriptionUtils.WEST_START && angle <= DescriptionUtils.WEST_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.WEST);
				} else if (angle > DescriptionUtils.WEST_END && angle < DescriptionUtils.SOUTH_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTHWEST);
				} else if (angle >= DescriptionUtils.SOUTH_START && angle < DescriptionUtils.SOUTH_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTH);
				} else if (angle > DescriptionUtils.SOUTH_END && angle < DescriptionUtils.EAST_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.SOUTHEAST);
				} else if (angle > DescriptionUtils.EAST_END && angle < DescriptionUtils.NORTH_START){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.NORTHEAST);
				} else if (angle >= DescriptionUtils.EAST_START || angle <= DescriptionUtils.EAST_END){
					infoRouteStretchBean.setStretchCardinalDirection(CardinalDirections.EAST);
				} 
			}
		}
	}

}
