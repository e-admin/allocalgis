/**
 * LocalGISNetworkDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.ConfigurationException;

import org.apache.log4j.Logger;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.postgis.PGgeometry;
import org.postgis.jts.JtsGeometry;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.datastore.RouteResultSet;
import org.uva.route.graph.structure.VirtualNode;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraphable;
import org.uva.route.graph.structure.geographic.GeographicNode;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.impedance.SimpleImpedance;
import org.uva.route.network.NetworkLink;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.street.Incident;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.sql.GEOPISTAConnection;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.util.UserCancellationException;
import com.localgis.route.datastore.LocalGISResultSet;
import com.localgis.route.datastore.LocalGISStreetResultSet;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphBuilder;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISIncident;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.localgis.route.graph.structure.basic.NetworkEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.TurnImpedance;
import com.localgis.route.graph.structure.basic.VirtualNodeInfo;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.JtsPostgisOperations;
import com.localgis.util.LocalGISGeometryBuilder;
import com.localgis.util.NetworkEdgesComparator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.task.TaskMonitor;


public class LocalGISNetworkDAO {
	private boolean PMRGraph = false;
	private WeakReference<TaskMonitor> taskMonitor;
	public boolean isPMRGraph() {
		return PMRGraph;
	}
	public void setPMRGraph(boolean pMRGraph) {
		PMRGraph = pMRGraph;
	}


	public String getQueryForReadingNetworkId(String networkName) {
		return "(SELECT id_network FROM networks WHERE network_name ='" + networkName+ "')";
	}
	protected String getQueryForReadingEntireGraph(String networkName) {

		StringBuilder sql1 = new StringBuilder("(").append(getQueryForReadingNetworkId(networkName)).append(")");
		StringBuilder sql = new StringBuilder();
/*		if (this.PMRGraph){
			sql = new StringBuilder("SELECT network_edges.*, p.*, (SELECT AsText(node_geometry) FROM network_nodes WHERE ");
		sql.append("(network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS geomNodoA ," );
		sql.append("(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS sridNodoA ," );
		sql.append("(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS geomNodoB ," );
		sql.append("(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS sridNodoB, astext(a.\"GEOMETRY\") as geom " );
		sql.append("FROM network_edges, network_street_properties p,aceraspmr a WHERE network_edges.id_network=").append(sql1).append(" AND network_edges.id_network_nodeA = network_edges.id_network_nodeB " );
		sql.append(" and network_edges.id_network = p.id_network and network_edges.id_edge = p.id_edge and network_edges.id_feature=a.id");
		}
		else{*/
		sql = new StringBuilder("SELECT network_edges.*,");
		sql.append(" p.*,");
		sql.append("(SELECT AsText(node_geometry) FROM network_nodes WHERE ");
		sql.append("(network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS geomNodoA ," );
		sql.append("(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS sridNodoA ," );
		sql.append("(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS geomNodoB ," );
		sql.append("(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS sridNodoB " );
		//sql.append("FROM network_edges, network_street_properties p WHERE network_edges.id_network=").append(sql1).append(" AND network_edges.id_network_nodeA = network_edges.id_network_nodeB " );
		sql.append("FROM network_edges, network_street_properties p WHERE network_edges.id_network=").append(sql1);
		//sql.append(" and network_edges.id_network = p.id_network and network_edges.id_edge = p.id_edge");
		sql.append(" and network_edges.id_network = p.id_network and network_edges.id_edge = p.id_edge");
		
//		}
//		if (LOGGER.isDebugEnabled())LOGGER.debug("getQueryForReadingEntireGraph() - " + sql);
		return sql.toString();
	}
	/**
	 * Query que devuelve la estructura de un networklink a partir de la tabla
	 * network_edges con todos los que tengan un nodo de una red y el otro de otra red
	 * y que uno de los dos sean la reda buscada.
	 * @param networkName
	 * @return
	 */
	protected String getQueryForReadingNetworkLinks(String networkName) {

		String sql1 = "(" + getQueryForReadingNetworkId(networkName) + ")";

//		String sql = "SELECT " +
//				"network_edges.*, " +
//				"(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS geomNodoA ," +
//				"(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS sridNodoA ," +
//				"(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS geomNodoB ," +
//				"(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS sridNodoB " +
//				"FROM network_edges WHERE network_edges.id_network=" + sql1 + " AND network_edges.id_network_nodeA != network_edges.id_network_nodeB";
		String sql = "SELECT " +
			"network_edges.*, " +
			"(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS geomNodoA ," +
			"(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodeA)) AS sridNodoA ," +
			"(SELECT AsText(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS geomNodoB ," +
			"(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeB)) AS sridNodoB " +
			"FROM network_edges WHERE (network_edges.id_network_nodeA=" + sql1 + "OR network_edges.id_network_nodeB=" + sql1 + ") AND network_edges.id_network_nodeA != network_edges.id_network_nodeB";

//		if (LOGGER.isDebugEnabled())LOGGER.debug("getQueryForReadingEntireGraph() - " + sql);
		return sql;
	}
	public ArrayList <RouteResultSet> readNetworkLinks(String networkName,Connection connection) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException{
		ArrayList <RouteResultSet> results = new ArrayList<RouteResultSet>();
//		LOGGER.debug("Realizando consulta : " + getQueryForReadingEntireGraph(networkName));
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
		    int i=0;
			preparedStatement = connection.prepareStatement(getQueryForReadingNetworkLinks(networkName));
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
			    i++;
				RouteResultSet rss = null;
				rss = (LocalGISResultSet)readInternal(rs, false,connection);
				results.add(rss);
			}
			LOGGER.debug("Query ejecutada. Recibiendo NEtworkLinks:"+i+" registros.");

		} finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	public List getNumbersList(ILocalGISEdge edge,String sTramosVia, int srid,String extremo, Connection connection) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException{
		
		
		
		List <Object[]> results = new ArrayList<Object[]>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
//		StringBuffer tramosQuery = new StringBuffer("select l.name from layers l, network_edges n, network_street_properties s where l.id_layer=n.id_layer and s.id_edge_related=n.id_edge and s.id_network <> n.id_network and s.id_edge=? limit 1");
//		preparedStatement = connection.prepareStatement(tramosQuery.toString());
//		preparedStatement.setInt(1,id);
//		rs = preparedStatement.executeQuery();
//		String sTramosVia = "";
//		if (rs.next()) {
//			sTramosVia = rs.getString("name");
//		}
		StringBuffer sql = null;
		int edgeId = edge.getID();
		try {
			sql = new StringBuffer("select n.id,x(transform(n.\"GEOMETRY\",?)) as x,y(transform(n.\"GEOMETRY\",?)) as y, n.rotulo, distance(transform(n.\"GEOMETRY\",?),transform(nodes.node_geometry,?)) from numeros_policia n, ");
			sql = sql.append("network_nodes nodes, "+sTramosVia+" t, network_street_properties s, network_edges e where n.id_via=t.id_via and ");
			sql = sql.append("s.id_edge_related=e.id_edge and e.id_feature=t.id ");
			if (extremo.equals("A"))
				sql = sql.append("and nodes.id_node=e.id_nodea ");
			else
				sql = sql.append("and nodes.id_node=e.id_nodeb ");
			sql = sql.append("and s.id_edge=? and s.id_network=? order by distance limit 10");
			preparedStatement = connection.prepareStatement(sql.toString());
			preparedStatement.setInt(1,srid);
			preparedStatement.setInt(2,srid);
			preparedStatement.setInt(3,srid);
			preparedStatement.setInt(4,srid);
			
			preparedStatement.setInt(5,edgeId);// Para los edges normales idNetworkNodeA y B coinciden
			preparedStatement.setInt(6,edge.getIdNetworkNodeA());
			
			System.out.println("idseleect "+edgeId);
//			LOGGER.debug("Ejecutando query: "+sql.toString());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Object[] objectArray = new Object[]{rs.getInt("id"),rs.getDouble("x"),rs.getDouble("y"),rs.getString("rotulo")};
				results.add(objectArray);
			}
			
			
			
		}catch(Exception e){
			LOGGER.error(e);
			e.printStackTrace();
			LOGGER.error(sql.toString()+ " "+srid+" "+edgeId);
			return null;
		} finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return results;
	}

	public Graph readNetwork(String networkName,GraphGenerator generator,Connection connection) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException{
		return this.readNetwork(networkName, generator, connection, -1, -1);
	}
	public Graph readNetwork(String networkName,GraphGenerator generator,Connection connection, int inicioId, int finId) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException{

		long l = System.currentTimeMillis();
		ArrayList <RouteResultSet> results = new ArrayList<RouteResultSet>();
//		LOGGER.debug("Realizando consulta : " + getQueryForReadingEntireGraph(networkName));
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			StringBuilder sb = new StringBuilder(getQueryForReadingEntireGraph(networkName));
			if (inicioId != -1)
				sb.append( " and network_edges.id_edge >=").append(inicioId).append(" and network_edges.id_edge<").append(finId);
			preparedStatement = connection.prepareStatement(sb.toString());
			rs = preparedStatement.executeQuery();
//			LOGGER.debug("Query ejecutada. Anexando datos:");
			int i = 0;
			while (rs.next()) {
				RouteResultSet rss = null;
				if(generator.getGraphBuilder() instanceof LocalGISStreetGraphBuilder)
					rss = (LocalGISStreetResultSet)readInternal(rs, true,connection);
				else
					rss = (LocalGISResultSet)readInternal(rs, false,connection);
				results.add(rss);
			}
		
		} finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
//		LOGGER.debug("Datos anexados. Cerrando conexiï¿½n.");
		Iterator<RouteResultSet> it = results.iterator();
		int i=0;
		// BuildIncidents Hashmap con id_edge incidents
		Hashtable<Integer,ArrayList<LocalGISIncident>> incidents = getIncidentList(networkName,connection);
		// BuildImpedanceMatrix idNode - impedanceMatrix
		Hashtable<Integer,LocalGISTurnImpedance> impedanceMatrix = getImpedanceMatrixList(networkName,connection);
		Hashtable<Integer,LocalGISStreetResultSet> streetData = getStreetDataList(networkName,connection);
		while(it.hasNext()){
			RouteResultSet rset = it.next();
			ArrayList<LocalGISIncident> incidentList = incidents.get(new Integer(rset.getId_edge()));
			if(incidentList != null){
				Iterator<LocalGISIncident> incidentsIterator = incidentList.iterator();
				while (incidentsIterator.hasNext()){
					((LocalGISResultSet)rset).setIncident(incidentsIterator.next());
				}
			}
			//getIncidents(networkName,rset,connection);// TODO BUG mejor comprobar por la instancia de rset
		if (generator.getGraphBuilder() instanceof LocalGISStreetGraphBuilder)
		    {
			if (streetData!=null)
			{
			// TODO: Si se produce error en este trozo del codigo, hay informacion incoherente. Tal vez deberia lanzar un mensaje.
			LocalGISStreetResultSet streetDataPunctual = streetData.get(new Integer(rset.getId_edge()));
			if (streetDataPunctual != null)
			    {
				((LocalGISStreetResultSet) rset).setNominalMaxSpeed(streetDataPunctual.getNominalMaxSpeed());
				((LocalGISStreetResultSet) rset).setStreetTrafficRegulation(streetDataPunctual.getStreetTrafficRegulation());
				((LocalGISStreetResultSet) rset).setTurnImpedanceNodeA(impedanceMatrix.get(new Integer(rset.getId_nodoA())));
				((LocalGISStreetResultSet) rset).setTurnImpedanceNodeB(impedanceMatrix.get(new Integer(rset.getId_nodoB())));
			    }
			}
			
		    }
		if (this.PMRGraph){
				((LocalGISStreetResultSet)rset).setbPMRGraph(true);
				((LocalGISStreetResultSet)rset).setTransversalSlope(((LocalGISStreetResultSet)rset).getTransversalSlope());
				((LocalGISStreetResultSet)rset).setLongitudinalSlope(((LocalGISStreetResultSet)rset).getLongitudinalSlope());
				((LocalGISStreetResultSet)rset).setWidth(((LocalGISStreetResultSet)rset).getWidth());
				((LocalGISStreetResultSet)rset).setRelatedToId(((LocalGISStreetResultSet)rset).getRelatedToId());
				((LocalGISStreetResultSet)rset).setCalculatedSide(((LocalGISStreetResultSet)rset).getCalculatedSide());
				((LocalGISStreetResultSet)rset).setsEdgeType(((LocalGISStreetResultSet)rset).getsEdgeType());
				if (((LocalGISStreetResultSet)rset).getsType() != null)
					((LocalGISStreetResultSet)rset).setsType(((LocalGISStreetResultSet)rset).getsType());
				((LocalGISStreetResultSet)rset).setGeom(((LocalGISStreetResultSet)rset).getGeom());
			}
			generator.add(rset);
		}
//		LOGGER.debug("Cargados " + i + " en total.");
//		LOGGER.debug("Red cargada en " + Math.round((System.currentTimeMillis() - l)/1000) + " segundos.");
		return generator.getGraph();
	}
	private Hashtable<Integer, LocalGISTurnImpedance> getImpedanceMatrixList(
			String networkName, Connection connection) {
		Hashtable<Integer,LocalGISTurnImpedance> turnImpedanceList = new Hashtable<Integer,LocalGISTurnImpedance>();
		//
		String sqlQuery=
			"SELECT * "+
			"FROM network_impedance_matrix " +
			"WHERE id_network_node = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	//connection = getConnection();
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando matriz de impedancias en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Matriz de impedancias encontrada.");
		    	Integer idEdgeStart = rs.getInt("id_edge_start");
		    	Integer idEdgeEnd = rs.getInt("id_edge_end");
		    	double impedance = rs.getDouble("impedance");
		    	Integer idNode = rs.getInt("id_node");
		    	if(turnImpedanceList.get(idNode) == null){
		    		LocalGISTurnImpedance turnImpedance = new LocalGISTurnImpedance();
		    		turnImpedance.setTurnImpedance(idEdgeStart, idEdgeEnd, impedance);
		    		turnImpedanceList.put(idNode, turnImpedance);
		    	}else{
		    		LocalGISTurnImpedance turnImpedance = turnImpedanceList.get(idNode);
		    		turnImpedance.setTurnImpedance(idEdgeStart, idEdgeEnd, impedance);
		    	}

		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return turnImpedanceList;
	}
	private Hashtable<Integer,ArrayList<LocalGISIncident>> getIncidentList(String networkName, Connection connection) {
		Hashtable<Integer,ArrayList<LocalGISIncident>>incidentList = new Hashtable<Integer,ArrayList<LocalGISIncident>>();
		String sqlQuery=
			"SELECT * "+
			"FROM network_incidents " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando incidentes en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISIncident incident = null;
				try {
					incident = new LocalGISIncident(rs.getString("pattern"));
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Error processing incident!",e);
					e.printStackTrace();
				}
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Incidente encontrado.");
		    	if(rs.getInt("incident_type") == 1)
		    		incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
		    	if(rs.getInt("incident_type") == 2)
		    		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
		    	incident.setDescription(rs.getString("description"));
		    	//incident.setDateStart(rs.getDate("date_start"));
		    	//incident.setDateEnd(rs.getDate("date_end"));
		    	//incident.setDateEnd();
		    	Integer idEdge = new Integer(rs.getInt("id_edge"));
		    	if(incidentList.get(idEdge) == null){
		    		ArrayList<LocalGISIncident> incidents = new ArrayList<LocalGISIncident>();
		    		incidents.add(incident);
		    		incidentList.put(idEdge, incidents);
		    	}else{
		    		ArrayList<LocalGISIncident> incidents = incidentList.get(idEdge);
		    		incidents.add(incident);
		    	}
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return incidentList;
	}
	public Map<String,NetworkProperty> readNetworkProperties(String networkName,Connection connection) throws SQLException {
		Map<String,NetworkProperty> networkProperties = new HashMap<String,NetworkProperty>();
		String sqlQuery=
				"SELECT * "+
				"FROM network_properties " +
				"WHERE id_network = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando las propiedades de la red "+ networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades encontradas");
		    	String name = rs.getString("group_name");
		    	NetworkProperty property = null;

		    	if(property != networkProperties.get(name)){
		    		property = (NetworkProperty)networkProperties.get(name);
		    	}else{
		    		property = new NetworkProperty();
		    	}
		    	property.setNetworkManagerProperty(rs.getString("property_key"), rs.getString("property_value"));
		    	networkProperties.put(name, property);
		    }

		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return networkProperties;
	}
	
	
	public void writeNetworkProperties(String networkName,
										Map<String,Object> networkProperties,
										Connection connection) throws SQLException
	{
		String sql = "DELETE FROM network_properties where id_network = ? ";
		PreparedStatement st = null;
		try{
			st=connection.prepareStatement(sql);
			st.setLong(1, Long.valueOf(getQueryForReadingNetworkId(networkName)));
			
			st.executeUpdate();
		}finally{
			ConnectionUtilities.closeConnection(null,st,null);
		}
		setNetworkProperties(networkName, networkProperties, connection);
	}
	
	
	
	private void setNetworkProperties(String networkName,Map<String,Object> networkProperties,Connection connection) throws SQLException {

		PreparedStatement st = null;

		Iterator<String> it = networkProperties.keySet().iterator();
		String networkPropertyName = null;
//		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de la red " + networkName + " en base de datos ");
		while (it.hasNext()){

			networkPropertyName = it.next();
			NetworkProperty netProp =  (NetworkProperty) networkProperties.get(networkPropertyName);

			for(int i=0; i < netProp.getKeys().size(); i++){

				String sqlQueryForInserProperties = "INSERT INTO network_properties values("
					 + this.getQueryForReadingNetworkId(networkName)
					 + ", '" + networkPropertyName + "'"
					 + ", '" + netProp.getKeys().get(i) + "'"
					 + ", '" + netProp.getValue(netProp.getKeys().get(i)) + "'"
					 +")";

				try {
					st = connection.prepareStatement(sqlQueryForInserProperties);
					st.executeUpdate();
				} finally {
					ConnectionUtilities.closeConnection(null, st, null);
				}

			}

		}
	}
/*	public void getStreetData(String networkName,LocalGISStreetResultSet rss,Connection connection) throws SQLException {
		if (rss instanceof PMRLocalGISStreetResultSet){
			getPMRStreetData(networkName,(PMRLocalGISStreetResultSet)rss,connection);
			return;
		}

		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + rss.getId_edge();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	rss.setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
		    }else{
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Datos no encontrados. Estableciendo velocidad maxima por de fecto , 50km/h.");
		    	// TODO: Establecer una velocidad maxima para la via por defecto. Donde, en xml o plano. Al ser un municipio, se deberia usar 50, ya que suele ser parte urbana.
		    	// Establecido 12 m/s, ya que la longitud es en metros.
		    	rss.setNominalMaxSpeed(12);
		    }
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	}
	public Hashtable<Integer,LocalGISStreetResultSet> getStreetDataList(String networkName,Connection connection) throws SQLException {
		//TODO: Se puede crear un objeto mas ligero. De momento uso este para almacenar estos datos.
		Hashtable<Integer,LocalGISStreetResultSet> streetData = new Hashtable<Integer,LocalGISStreetResultSet>();
		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISStreetResultSet rss = new LocalGISStreetResultSet();
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	rss.setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	Integer idEdge = new Integer(rs.getInt("id_edge"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
		    	streetData.put(idEdge,rss);

		    }
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return streetData;
	}
	public void getStreetData(String networkName,Edge edge,Connection connection) throws SQLException {

		if (edge instanceof PMRLocalGISStreetDynamicEdge){
			getPMRStreetData(networkName,edge,connection);
			return;
		}

		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + edge.getID();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	((LocalGISStreetDynamicEdge)edge).setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
		    }else{
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Datos no encontrados. Estableciendo velocidad maxima por de fecto , 50km/h.");
		    	// TODO: Establecer una velocidad maxima para la via por defecto. Donde, en xml o plano. Al ser un municipio, se deberia usar 50, ya que suele ser parte urbana.
		    	// Establecido 12 m/s, ya que la longitud es en metros.
		    	((LocalGISStreetDynamicEdge)edge).setNominalMaxSpeed(12);
		    }
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	}
	public void updateStreetData(String networkName,LocalGISStreetDynamicEdge edge,Connection connection) throws SQLException, Exception {
		if (edge instanceof PMRLocalGISStreetDynamicEdge){
			updateStreetData(networkName,edge,connection);
			return;
		}
		Integer networkId = getNetworkId(networkName,connection);
		PreparedStatement st = null;

		if (LOGGER.isDebugEnabled()) LOGGER.debug("Actualizando propiedades de calle de la red " + networkName + " en base de datos ");

		String sqlQueryForInsertStreetProperties = "UPDATE network_street_properties set maxspeed = ?,traffic_orientation = ? WHERE id_network = ? AND id_edge = ?";

		try {
			st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
			st.setInt(3, networkId);
			st.setInt(4, edge.getID());
			st.setDouble(1, edge.getNominalMaxSpeed());
			st.setString(2, edge.getTrafficRegulation().toString());
			st.executeUpdate();
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	public void setStreetData(String networkName,LocalGISStreetDynamicEdge edge,Connection connection) throws SQLException, Exception {
		if (edge instanceof PMRLocalGISStreetDynamicEdge){
			setPMRStreetData(networkName,(PMRLocalGISStreetDynamicEdge)edge,connection);
			return;
		}
		Integer networkId = getNetworkId(networkName,connection);
		PreparedStatement st = null;

		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de calle de la red " + networkName + " en base de datos ");

		String sqlQueryForInsertStreetProperties = "INSERT INTO network_street_properties values(?,?,?,?)";

		try {
			st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
			st.setInt(1, networkId);
			st.setInt(2, edge.getID());
			st.setDouble(3, edge.getNominalMaxSpeed());
			st.setString(4, edge.getTrafficRegulation().toString());
			st.executeUpdate();
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}*/
	public void getIncidents(String networkName,Edge edge,Connection connection) {
		String sqlQuery=
			"SELECT * "+
			"FROM network_incidents " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + edge.getID();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando incidentes en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISIncident incident = new LocalGISIncident(rs.getString("pattern"));
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Incidente encontrado.");

		    	if(rs.getInt("incident_type") == 0)
		    		incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
		    	if(rs.getInt("incident_type") == 1)
		    		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
		    	incident.setDescription(rs.getString("description"));
		    	((ILocalGISEdge) edge).putIncident(incident);
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(connection, preparedStatement, rs);
	    }
	}
	public void getIncidents(String networkName,RouteResultSet rset,Connection connection) {
		String sqlQuery=
			"SELECT * "+
			"FROM network_incidents " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + rset.getId_edge();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando incidentes en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISIncident incident = new LocalGISIncident(rs.getString("pattern"));
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Incidente encontrado.");
		    	if(rs.getInt("incident_type") == 0)
		    		incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
		    	if(rs.getInt("incident_type") == 1)
		    		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
		    	incident.setDescription(rs.getString("description"));
		    	((LocalGISResultSet)rset).setIncident(incident);
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
	}
	public void getIncidents(int networkId,RouteResultSet rset,Connection connection) {
		String sqlQuery=
			"SELECT * "+
			"FROM network_incidents " +
			"WHERE id_network = "+networkId + " " +
			"AND id_edge = " + rset.getId_edge();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando incidentes en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISIncident incident = new LocalGISIncident(rs.getString("pattern"));
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Incidente encontrado.");
		    	if(rs.getInt("incident_type") == LocalGISIncident.PATH_DISABLED)
		    		incident.setIncidentType(LocalGISIncident.PATH_DISABLED);
		    	if(rs.getInt("incident_type") == LocalGISIncident.PATH_CLOSED_TO_VEHICLES)
		    		incident.setIncidentType(LocalGISIncident.PATH_CLOSED_TO_VEHICLES);
		    	incident.setDescription(rs.getString("description"));
		    	((LocalGISResultSet)rset).setIncident(incident);
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(connection, preparedStatement, rs);
	    }
	}
	public void setIncidents(String networkName,int networkId,ILocalGISEdge edge,Connection connection) throws SQLException
	{

//		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de calle de la red " + networkName + " en base de datos ");
		Set<Incident> incidents = edge.getIncidents();
		Iterator<Incident> it = incidents.iterator();
//		Integer networkId = getNetworkId(networkName,connection);
		clearIncidents(networkId,((DynamicEdge)edge).getID(),connection);
		while (it.hasNext()){
			LocalGISIncident incident = (LocalGISIncident)it.next();
			DynamicEdge localgisEdge = (DynamicEdge)edge;
			setIncident(networkId,localgisEdge.getID(),incident,connection);
		}

	}
	public Integer getNetworkId(String networkName,Connection connection) throws SQLException
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		Integer networkId = null;
		try {
			//String sqlQueryForInsertIncidents = this.getQueryForReadingNetworkId(networkName);
			st = connection.prepareStatement("SELECT id_network FROM networks WHERE network_name =?");
			st.setString(1, networkName);
			rs = st.executeQuery();
			if(rs.next())
				networkId = rs.getInt(1);
		}finally {
			ConnectionUtilities.closeConnection(null, st, rs);
		}
		return networkId;
	}
	private void clearIncidents(Integer networkId, int id,Connection connection){
		PreparedStatement st = null;
		try {

			String sqlQueryForDeleteIncidents = "DELETE FROM network_incidents WHERE id_network = ? AND id_edge = ?";
			st = connection.prepareStatement(sqlQueryForDeleteIncidents);
			st.setInt(1, networkId);
			st.setInt(2, id);
			st.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}
	}
	private void setIncident(Integer networkId, int id,LocalGISIncident incident,Connection connection) {

		PreparedStatement st = null;
		try {
			String sqlQueryForInsertIncidents = "INSERT INTO network_incidents values(?,?,?,?,null,null,?)";
			st = connection.prepareStatement(sqlQueryForInsertIncidents);
			st.setInt(1, networkId);
			st.setInt(2, id);
			st.setInt(3, incident.getIncidentType());
			st.setString(4, incident.getDescription());
			st.setString(5, incident.toString());
			st.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}

	public LocalGISTurnImpedance getImpedanceMatrix(String networkName,int id_Node,Connection connection) {

		//Connection connection = null;
		LocalGISTurnImpedance turnImpedance = new LocalGISTurnImpedance();
		String sqlQuery=
			"SELECT * "+
			"FROM network_impedance_matrix " +
			"WHERE id_node = ? ";
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	//connection = getConnection();
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando matriz de impedancias en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
	    	preparedStatement.setInt(1, id_Node);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Matriz de impedancias encontrada.");
		    	Integer idEdgeStart = rs.getInt("id_edge_start");
		    	Integer idEdgeEnd = rs.getInt("id_edge_end");
		    	double impedance = rs.getDouble("impedance");
		    	turnImpedance.setTurnImpedance(idEdgeStart, idEdgeEnd, impedance);
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		if(turnImpedance.getSize() == 0)
			return null;
		return turnImpedance;
	}
	public void setImpedanceMatrix(String networkName,int networkId, Node node,Connection connection) throws SQLException, Exception {
		if(node.getObject() instanceof LocalGISTurnImpedance){
			LocalGISTurnImpedance turnImpedances = (LocalGISTurnImpedance)node.getObject();
			ArrayList<TurnImpedance> impedances = turnImpedances.getTurnImpedances();
			Iterator<TurnImpedance> it = impedances.iterator();
			while(it.hasNext()){
				TurnImpedance impedance = it.next();
				setImpedanceMatrix(networkName,networkId,node.getID(),impedance,connection);
			}
		}
	}

	private void setImpedanceMatrix(String networkName,int networkId,int idNode,TurnImpedance impedance,Connection connection) throws SQLException, Exception{

		PreparedStatement st = null;
//		Integer networkId = getNetworkId(networkName,connection);
		try {
			//TODO: Cambio de modelo de base de datos. Ya no se necesitan los networks de los edges.
			// Puede que no sea necesario ni el network node
			String sqlQueryForImpedanceMatrix = "INSERT INTO network_impedance_matrix values(?,?,?,?,?,?,?)";

			st = connection.prepareStatement(sqlQueryForImpedanceMatrix);
			st.setInt(1, 0);
			st.setInt(2, 0);
			st.setInt(3, impedance.getIdEdgeStart());
			st.setInt(4, impedance.getIdEdgeEnd());
			st.setDouble(5,impedance.getImpedance());
			st.setInt(6, networkId);
			st.setInt(7, idNode);
			st.executeUpdate();
		} finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	protected RouteResultSet readInternal(ResultSet rs,boolean isStreet,Connection connection) throws IOException, SQLException, NoSuchAuthorityCodeException, FactoryException {

		RouteResultSet rrs = null;
		
		if(isStreet){
			rrs = new LocalGISStreetResultSet();
		}else{
			rrs = new LocalGISResultSet();
		}

			int id_subred = rs.getInt("id_network");
			rrs.setId_subNet(id_subred);
			int id_edge = rs.getInt("id_edge");
			rrs.setId_edge(id_edge);
			int id_nodoA = rs.getInt("id_nodeA");
			rrs.setId_nodeA(id_nodoA);
			int id_subred_nodoA = rs.getInt("id_network_nodeA");
			rrs.setId_subNet_nodeA(id_subred_nodoA);
			int id_nodoB = rs.getInt("id_nodeB");
			rrs.setId_nodeB(id_nodoB);
			int id_subred_nodoB = rs.getInt("id_network_nodeB");
			rrs.setId_subNet_nodeB(id_subred_nodoB);
			double length = rs.getDouble("edge_length");
			((LocalGISResultSet)rrs).setLength(length);
			((LocalGISResultSet)rrs).setIdFeature(rs.getInt("id_feature"));
			((LocalGISResultSet)rrs).setIdLayer(rs.getInt("id_layer"));
			((LocalGISResultSet)rrs).setImpedanceAToB(rs.getDouble("impedanceatob"));
			((LocalGISResultSet)rrs).setImpedanceBToA(rs.getDouble("impedancebtoa"));
			Point geomnodoa =  (Point) JtsGeometry.geomFromString(rs.getString("geomnodoa"));
			if (rs.getInt("sridnodoa") <= 0){
				geomnodoa.setSRID(25830);// TODO AsignaciÃ³n por defecto
			}else{
				geomnodoa.setSRID(rs.getInt("sridnodoa"));
			}
			CoordinateReferenceSystem crsNodeA = CRS.decode("EPSG:" + geomnodoa.getSRID());
			Point geomnodob =  (Point) JtsGeometry.geomFromString(rs.getString("geomnodob"));
			if (rs.getInt("sridnodob") <= 0){
				//TODO: Es para que no falle, pero ya sera incorrecto, a no ser que sea del tipo 23030
				geomnodob.setSRID(25830);
			}else{
				geomnodob.setSRID(rs.getInt("sridnodob"));
			}

			CoordinateReferenceSystem crsNodeB = CRS.decode("EPSG:" + geomnodob.getSRID());
			rrs.setPointNodeA(GeographicNodeUtil.createISOPoint(geomnodoa,crsNodeA));
			rrs.setPointNodeB(GeographicNodeUtil.createISOPoint(geomnodob,crsNodeB));
			if (this.isPMRGraph()){
				((LocalGISStreetResultSet)rrs).setLongitudinalSlope(rs.getDouble("longitudinal_slope"));
				((LocalGISStreetResultSet)rrs).setTransversalSlope(rs.getDouble("transversal_slope"));
				((LocalGISStreetResultSet)rrs).setWidth(rs.getDouble("width"));
				((LocalGISStreetResultSet)rrs).setRelatedToId(rs.getInt("id_edge_related"));
				((LocalGISStreetResultSet)rrs).setCalculatedSide(rs.getInt("calculated_side"));
				((LocalGISStreetResultSet)rrs).setsEdgeType(rs.getString("edge_type"));
				if (rs.getString("zebra_type") != null)
					((LocalGISStreetResultSet)rrs).setsType(rs.getString("zebra_type"));
				
			}
		

		return rrs;

	}
	
	public Geometry getEdgeGeometry(PMRLocalGISStreetDynamicEdge edge, int srid, int idMunicipio,Connection connection){
		ResultSet rs=null;
		PreparedStatement st2=null;
		Geometry geom = null;
		String query="";
		try{
			//Obtengo la geometrï¿½a del edge
			query="select astext(transform(\"GEOMETRY\",?)) as geom from aceraspmr where id =? and id_municipio=?";
			st2=connection.prepareStatement(query);
			st2.setInt(1, srid);
			st2.setInt(2, edge.getIdFeature());
			st2.setInt(3, idMunicipio);
			rs=st2.executeQuery();
			if (rs.next()){
				geom = (Geometry)JtsGeometry.geomFromString(rs.getString("geom"));
			}
			rs.close();
			st2.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(query);
		}
		return geom;
	}
	
	public List<Integer> getNodesNearTo(org.opengis.geometry.primitive.Point point, double radius, int num,String networkName,Connection conn) throws IOException
	{

		List<Integer> nodesidentifiernearto=new ArrayList<Integer>();
		String query=getQueryForReadingNodesByDistance(point, radius, num,networkName);
		ResultSet rs=null;
		PreparedStatement st=null;
		try
		{
			st=conn.prepareStatement(query);
			rs=st.executeQuery();
			while (rs.next())
			{
				int nodeID=rs.getInt("id_node");
				nodesidentifiernearto.add(nodeID);
			}

		}
		catch (SQLException e)
		{
			throw new IOException(e);
		}finally{
			ConnectionUtilities.closeConnection(null, st, rs);
		}
		return nodesidentifiernearto;
	}
	protected String getQueryForReadingNodesByDistance(Geometry point, double radius, int limit,String networkName)
	{
		String srid=null;//LocalGISGeometryBuilder.guessSRIDCode(point);
//		String geom = "transform(node_geometry," + srid + ")";
		String geom = "node_geometry";

		String orig = LocalGISGeometryBuilder.getWKTSQLSectionGeometry(point, srid);
//		String orig = "PointFromText('POINT("+coords[0]+" "+coords[1]+")',"+srid+")";
		String distance = "distance("+geom+","+orig+")";
			String sql_nodesnearto=
				"SELECT *, " +
				distance + " AS distance "+
				"FROM network_nodes " +
				"WHERE id_network = "+getQueryForReadingNetworkId(networkName)+ " " +
				"AND "+distance+" < "+radius+ " " +
				"AND " + geom + " && expand("+ orig + "::geometry,"+radius+") "+
				"order by distance";
		if(limit!=0)
		{
			return sql_nodesnearto+" LIMIT "+limit;
		}
		return sql_nodesnearto;


	}
	protected String getQueryForReadingNodesByDistance(org.opengis.geometry.primitive.Point point, double radius, int limit,String networkName)
	{
		String srid=LocalGISGeometryBuilder.guessSRIDCode(point);
		String geom = "transform(node_geometry," + srid + ")";
		String orig = LocalGISGeometryBuilder.getWKTSQLSectionGeometry(point, srid);
//		String orig = "PointFromText('POINT("+coords[0]+" "+coords[1]+")',"+srid+")";
		String distance = "distance("+geom+","+orig+")";
			String sql_nodesnearto=
				"SELECT *, " +
				distance + " AS distance "+
				"FROM network_nodes " +
				"WHERE id_network = "+getQueryForReadingNetworkId(networkName)+ " " +
				"AND "+distance+" < "+radius+ " " +
				"AND " + geom + " && expand("+ orig + "::geometry,"+radius+") "+
				"order by distance";
		if(limit!=0)
		{
			return sql_nodesnearto+" LIMIT "+limit;
		}
		return sql_nodesnearto;


	}
	public String getQueryForReadingASectionOfGraphRelatedWithID(int id, String networkName)
	{
		if (networkName==null)
			throw new IllegalStateException("NetworkName is not set.");


		String sql = getQueryForReadingEntireGraph(networkName) + " AND (( "
				+ "network_edges.id_nodea=" + id + ") OR (" + "network_edges.id_nodeb=" + id
				+ "))";

		return sql;
	}

	public Graph queryDataBaseForGraph(String query,boolean isStreet,Connection connection)throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException
	{
		PreparedStatement st=null;
		ResultSet rs=null;
	GraphGenerator generator = null;
		try
		{
			if(isStreet)
		    generator = NetworkModuleUtilWorkbench.getLocalGISStreetGraphGenerator();
			else
		    generator = NetworkModuleUtil.getBasicLineGraphGenerator();
			st=connection.prepareStatement(query);
			rs=st.executeQuery();
			while (rs.next())
					generator.add(readInternal(rs,isStreet,connection));
			return generator.getGraph();
		}
		finally
		{
			ConnectionUtilities.closeConnection(null, st, rs);
		}
	}
	public void registerNetworkInDatabase(String nombreRed,Connection conn)	throws SQLException {
	PreparedStatement st=null;
	try{
	    String sql = "INSERT INTO networks VALUES(DEFAULT,'"
			+ nombreRed + "',"				
			+ "'')";
	
	st = conn.prepareStatement(sql);
	st.executeUpdate();
	}
	finally{
		ConnectionUtilities.closeConnection(null, st,null);
	}
	}
	public void write(Graph g,String networkName,int networkId,boolean isStreet,Connection connection) throws SQLException, Exception{
	    
		writeNodes(g.getNodes(),networkName,networkId,isStreet,connection);
		
//		for (Iterator<Node> itr = g.getNodes().iterator();itr.hasNext();) {
//			Node node = itr.next();
//			writeNode( node,networkName,isStreet,connection);
//			if(node.getObject() != null && node.getObject() instanceof LocalGISTurnImpedance){
//				setImpedanceMatrix(networkName, node, connection);
//			}
//			}
//		

		writeEdges(g.getEdges(), networkName, networkId, isStreet, connection);
	}
	void writeEdges(Collection<Edge> edges, String networkName, int networkId, boolean isStreet, Connection conn) throws SQLException
	{
	    	Connection connection=conn;
		// Encapsula la conexión para agrupar queries
		if (conn instanceof GEOPISTAConnection)
		    {
			connection=new BatchConnectionFacade((GEOPISTAConnection) conn, 5);
		    }
		int i = 1;
		for (Edge e:edges) 
		    {
			if (taskMonitor.get() != null)
			    taskMonitor.get().report("Guardando Tramos (" + i + " de " + edges.size() + ").");
			if (this.taskMonitor.get().isCancelRequested())
			    throw new UserCancellationException();
		//Almacena los datos bÃ¡sicos de un Edge. Las extensiones del modelo en otras tablas hay que hacerlas con otros mÃ©todos posteriores.	
			e=(Edge)NodeUtils.equivalentTo(e);
			writeEdge( e,networkName,networkId,connection);
			
			
				
			i++;
		    }
		connection.commit();
	}
	/**
	 * Almacena los datos bÃ¡sicos de un Edge. Las extensiones del modelo en otras tablas hay que hacerlas con otros mÃ©todos posteriores.
	 * @param edge
	 * @param networkName
	 * @param networkId
	 * @param connection
	 * @throws SQLException
	 */
	public void writeEdge( Edge edge,String networkName, int networkId,Connection connection) throws SQLException
	{
		PreparedStatement st = null;
		String networkAIdQuery = "";
		String networkBIdQuery = "";
		String sqlid_subred = "";
		try {
			if(edge instanceof NetworkLink){
				NetworkLink nEdge = (NetworkLink)edge;
				sqlid_subred = getQueryForReadingNetworkId(networkName);
				
				networkAIdQuery = getQueryForReadingNetworkId(nEdge.getNetworkA().getName());
				networkBIdQuery = getQueryForReadingNetworkId(nEdge.getNetworkB().getName());
			}else{
//				sqlid_subred = getQueryForReadingNetworkId(networkName);
			    	sqlid_subred=Integer.toString(networkId);
				networkAIdQuery = sqlid_subred;
				networkBIdQuery = sqlid_subred;
			}
			StringBuilder sqlA=null;
			StringBuilder sqlB=null;
			if (edge instanceof ILocalGISEdge)
			    {
				int idNetworkNodeA = ((ILocalGISEdge)edge).getIdNetworkNodeA();
				if (idNetworkNodeA==-1)// redId no especificada Asumimos igual que NetworkId
					idNetworkNodeA=networkId;
				sqlA = new StringBuilder(Integer.toString(idNetworkNodeA));
				int idNetworkNodeB = ((ILocalGISEdge)edge).getIdNetworkNodeB();
				if (idNetworkNodeB==-1)// redId no especificada Asumimos igual que NetworkId
					idNetworkNodeB=networkId;
				sqlB = new StringBuilder(Integer.toString(idNetworkNodeB));
			    }
			else
			    {
				sqlA = new StringBuilder("(SELECT id_network FROM network_nodes WHERE id_node=").append(edge.getNodeA().getID()).append(" AND id_network=").append(networkAIdQuery).append(")");
				sqlB = new StringBuilder("(SELECT id_network FROM network_nodes WHERE id_node=").append(edge.getNodeB().getID()).append(" AND id_network=").append(networkBIdQuery).append(")");
			    }
			StringBuilder sql = new StringBuilder("INSERT INTO network_edges VALUES (").append(sqlid_subred).append(",");

			sql.append(edge.getID()).append(",").append(edge.getNodeA().getID()).append(",").append(sqlA).append(",").append(edge.getNodeB().getID()).append(",").append(sqlB);
			if(edge instanceof NetworkLink){
				sql.append(",0");
			}
			if(!(edge instanceof NetworkLink)){
				sql.append(",").append(((ILocalGISEdge )edge).getEdgeLength()).append(",").append(((ILocalGISEdge )edge).getIdFeature()).append(",").append(((ILocalGISEdge )edge).getIdLayer());
			}
			if(edge instanceof LocalGISStreetDynamicEdge){
				sql.append(",").append(((SimpleImpedance) ((LocalGISStreetDynamicEdge )edge).getImpedance(edge.getNodeA())).getImpedance()).append(",").append(((SimpleImpedance) ((LocalGISStreetDynamicEdge )edge).getImpedance(edge.getNodeB())).getImpedance());
			}
			sql.append(")");

			st = connection.prepareStatement(sql.toString());
			st.executeUpdate();
			
			
			if (edge instanceof ILocalGISEdge)
		    {
				if (edge instanceof LocalGISStreetDynamicEdge)
				    {
					writeStreetProperties((LocalGISStreetDynamicEdge)edge, networkId, connection);
				    }	
				Set incidents = ((ILocalGISEdge) edge).getIncidents();
				if (!incidents.isEmpty())
				    writeNetworkEdgeIncidences(edge,networkName,networkId,connection);
		    }
			else
			{ // El modelo de datos exige que haya un registro en esta tabla para carga
				String sqlStrProp = "INSERT INTO network_street_properties values ((SELECT id_network from networks where network_name = ? ),?,?,?)";
				st = connection.prepareStatement(sqlStrProp);
				st.setString(1, networkName);
				st.setInt(2, edge.getID());
				st.setDouble(3, 0);// nominal Street TODO probar rutas
				st.setString(4, "BIDIRECTIONAL");
				st.executeUpdate();
			}
			
		} finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	public void writeNetworkEdgeIncidences (Edge edge,String networkName,int networkId, Connection connection) throws SQLException
	{
		setIncidents(networkName,networkId, (ILocalGISEdge) edge,connection);
	}
	public void writeNode( Node node,String networkName,int networkId, boolean isStreet,Connection connection)throws SQLException
	{

		PreparedStatement st = null;


		try{
			StringBuilder  sql1 = new StringBuilder ("(").append(getQueryForReadingNetworkId(networkName)).append(")");

			// TODO Modificacion para los grafos con nodos de giro:
			// - Obtener el nodo Padre?
			// - Saltarnos el nodo?
			// ï¿½Nodos clonados?
			node=(Node) NodeUtils.unwrapProxies(node);
			node=(Node) NodeUtils.equivalentTo(node);
			// Imprimos la informacion de un nodo.
			if (node instanceof GeographicNodeWithTurnImpedances){
				((DynamicGraphable)node).getGraphableForSaving();
			}

			Point a = null;
			if (node instanceof DynamicGeographicNode){
				a = LocalGISGeometryBuilder.getJTSPointFromPrimitivePoint(((DynamicGeographicNode) node).getPosition());
			} else if (node instanceof GeographicNodeWithTurnImpedances){
				a = LocalGISGeometryBuilder.getJTSPointFromPrimitivePoint(((GeographicNodeWithTurnImpedances) node).getPosition());
			}

			if (a != null){
				StringBuilder sql = new StringBuilder("INSERT INTO network_nodes VALUES(").append(sql1).append(",").append(node.getID());
				sql.append(",transform(GeomFromText('").append(a).append("',").append(a.getSRID()).append("),").append(a.getSRID()).append("))");

				st = connection.prepareStatement(sql.toString());
				st.executeUpdate();
			if (isStreet)
				setImpedanceMatrix(networkName,networkId,node,connection);
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	/**
	 * Intento de aceleración de operaciones. Uso de un acumulador de queries
	 * @param nodes
	 * @param networkName
	 * @param isStreet
	 * @param connection
	 * @throws SQLException
	 */
	public void writeNodes(Collection< Node> nodes,String networkName,int networkId,boolean isStreet,Connection conn)throws SQLException
    {
	if (nodes.size() == 0)
	    return;
	Connection connection = conn;
	PreparedStatement st = null;
	// Encapsula la conexión para agrupar queries
	if (connection instanceof GEOPISTAConnection)
	    {
		connection = new BatchConnectionFacade((GEOPISTAConnection) connection, 10);
	    }

	try
	    {
		int i = 0;

		for (Node node : nodes)
		    {
			i++;
			if (taskMonitor.get() != null)
			    this.taskMonitor.get().report("Guardando Nodos (" + i + " de " + nodes.size() + ").");
			if (this.taskMonitor.get().isCancelRequested())
			    throw new UserCancellationException();
			// StringBuilder sql1 = new StringBuilder ("(").append(getQueryForReadingNetworkId(networkName)).append(")");
			StringBuilder sql1 = new StringBuilder(Integer.toString(networkId));

			// TODO Modificacion para los grafos con nodos de giro:
			// - Obtener el nodo Padre?
			// - Saltarnos el nodo?
			// Â¿Nodos clonados?
			node=(Node) NodeUtils.equivalentTo(node);
			if (node instanceof VirtualNode)
			    {
				continue;
			    }
			// Imprimos la informacion de un nodo.
			if (node instanceof GeographicNodeWithTurnImpedances)
			    {
				((DynamicGraphable) node).getGraphableForSaving();
			    }
			Point a = null;
			if (node instanceof DynamicGeographicNode)
			    {
				a = LocalGISGeometryBuilder.getJTSPointFromPrimitivePoint(((DynamicGeographicNode) node).getPosition());
			    } else if (node instanceof GeographicNodeWithTurnImpedances)
			    {
				a = LocalGISGeometryBuilder.getJTSPointFromPrimitivePoint(((GeographicNodeWithTurnImpedances) node).getPosition());
			    }
			if (a != null)
			    {
				StringBuilder sql = new StringBuilder("INSERT INTO network_nodes VALUES(").append(sql1).append(",").append(node.getID());
				sql.append(",transform(GeomFromText('").append(a).append("',").append(a.getSRID()).append("),").append(a.getSRID())
					.append("))");

				st = connection.prepareStatement(sql.toString());
				st.executeUpdate();

			    }
		    }
		// Guarda propiedades de Street (no soporta Batch)
		if (isStreet)
		    for (Node node : nodes)
			{
			    setImpedanceMatrix(networkName, networkId, node, conn);
			}

	    } catch (SQLException e)
	    {
		LOGGER.error(e);
	    } catch (Exception e)
	    {
		LOGGER.error(e);
	    }
	finally
	    {
		connection.commit();
		ConnectionUtilities.closeConnection(null, st, null);
	    }

    }
/*	private void writeStreetProperties(LocalGISStreetDynamicEdge edge, String sqlIDSubred,Connection connection) throws SQLException, Exception{
		if (edge instanceof PMRLocalGISStreetDynamicEdge){
			writePMRStreetProperties((PMRLocalGISStreetDynamicEdge)edge,sqlIDSubred,connection);
			return;
		}

		PreparedStatement st = null;
		try {
			String sql = "INSERT INTO network_street_properties values (" +
								sqlIDSubred  + ", " +
								edge.getID() + ", " +
								edge.getNominalMaxSpeed() + ", '" +
								edge.getTrafficRegulation() + "')";
			st = connection.prepareStatement(sql);
			st.executeUpdate();
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}
	}*/
	protected String getBatchQueryUpdateEdge(String networkName, Edge e)
		{

		String idsubred_nodoA="(SELECT id_network FROM network_nodes WHERE id_node=" + e.getNodeA().getID()
			+ " AND id_network=(" + getQueryForReadingNetworkId(networkName) + "))";

		String idsubred_nodoB="(SELECT id_network FROM network_nodes WHERE id_node=" + e.getNodeB().getID()
			+ " AND id_network=(" + getQueryForReadingNetworkId(networkName) + "))";

		String sql="UPDATE network_edges SET " +
			"id_nodea = " + e.getNodeA().getID() + "," +
			"id_network_nodea = " + idsubred_nodoA + "," +
			"id_nodeb = " + e.getNodeB().getID() + "," +
			"id_network_nodeb = " + idsubred_nodoB + ",";
		if(e instanceof ILocalGISEdge){
			sql +=
				"edge_length = " +((ILocalGISEdge)e).getEdgeLength() + "," +
				"id_feature = " +((ILocalGISEdge)e).getIdFeature() + "," +
				"id_layer = " +((ILocalGISEdge)e).getIdLayer() + "," +
				"impedanceatob = " +((LocalGISDynamicEdge)e).getCost(e.getNodeA(),1) + "," +
				"impedancebtoa = " +((LocalGISDynamicEdge)e).getCost(e.getNodeB(),1);
		}else{
			sql +=
				"edge_length = " +((LocalGISStreetDynamicEdge)e).getEdgeLength() + "," +
				"id_feature = " +((LocalGISStreetDynamicEdge)e).getIdFeature() + "," +
				"id_layer = " +((LocalGISStreetDynamicEdge)e).getIdLayer() + "," +
				"impedanceatob = " +((LocalGISStreetDynamicEdge)e).getCost(e.getNodeA(),1) + "," +
				"impedancebtoa = " +((LocalGISStreetDynamicEdge)e).getCost(e.getNodeB(),1);
		}
			sql += " WHERE id_network="
			+ getQueryForReadingNetworkId(networkName) + " AND id_edge= " + e.getID();
		return sql;

	}
	private static Logger LOGGER = Logger.getLogger(LocalGISNetworkDAO.class);
	public void updateQuery(String sql,Connection connection) throws SQLException
	{
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement(sql);
			st.executeUpdate();
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}
	}

	public void update(Graph g,LocalGISRouteReaderWriter rw,Connection connection)throws Exception
    {
	int networkId = rw.getNetworkId();
	for (Iterator<Node> itr = g.getNodes().iterator(); itr.hasNext();)
	    {
		Node node = itr.next();
		if (node instanceof XYNode)
		    {
			// TODO: Metodo eliminado. Puenteado para que sea directo.
			// XYNode nodeToStore=(XYNode) rw.getActualGraphableToStore(node);
			XYNode nodeToStore = (XYNode) node;
			if (nodeToStore != null)
			    {
				String sql = getBatchQueryUpdateNode(rw.getNetworkName(), node);
				updateQuery(sql, connection);
				updateNetworkImpedanceMatrix(rw.getNetworkName(),networkId, node, connection);
			    } else
			    {
				if (rw instanceof LocalGISStreetRouteReaderWriter)
				    writeNode(node, rw.getNetworkName(), networkId, true, connection);
				else
				    writeNode(node, rw.getNetworkName(), networkId, false, connection);
			    }
		    } else
		    { // Can not handle other types of nodes
		      // store them without geometry?
			if (LOGGER.isDebugEnabled())
			    {
				LOGGER.debug("update(Graph) node not georreferenced:" + node); //$NON-NLS-1$
			    }
		    }
	    }
	for (Iterator<Edge> itr = g.getEdges().iterator(); itr.hasNext();)
	    {
		Edge edge = itr.next();
		String sql = getBatchQueryUpdateEdge(rw.getNetworkName(), edge);
		if (edgeExists(rw.getNetworkName(), edge, connection))
		    {
			updateQuery(sql, connection);
			this.writeNetworkEdgeIncidences(edge, rw.getNetworkName(), networkId, connection);
		    } else
		    writeEdge(edge, rw.getNetworkName(), networkId, connection);
	    }

    }

	private void updateNetworkImpedanceMatrix(String networkName,int networkId, Node node,Connection connection) throws Exception {
//		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de calle de la red " + networkName + " en base de datos ");

		clearImpedanceMatrix(networkName,networkId,node.getID(),connection);
		setImpedanceMatrix(networkName,networkId, node, connection);

	}
	public void clearImpedanceMatrix(String networkName, int networkId, int id,Connection connection) throws SQLException {
		PreparedStatement preparedStatement = null;
		try{
		    // TODO BUG no comprueba networkId Podría borrar datos de otras redes!!
			String query = "DELETE FROM network_impedance_matrix where " +
			"id_node = " + id;
//			LOGGER.debug(query);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();
		}finally{
			ConnectionUtilities.closeConnection(null, preparedStatement, null);
		}
	}
	public boolean edgeExists(String networkName, Edge edge,Connection connection) throws SQLException, Exception{
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			st = connection.prepareStatement("SELECT * FROM network_edges where id_edge = " + edge.getID() + " AND id_network = " + getQueryForReadingNetworkId(networkName));
			rs = st.executeQuery();
			if(rs.next())
				exists = true;
		}finally {
			ConnectionUtilities.closeConnection(null, st, rs);
		}
		return exists;
	}

	public String getBatchQueryUpdateNode(String networkName, Node node) {
		String sql;
		if (node instanceof XYNode)
		{
			double coordinates[];
			String srid="4326";//Every geometry should have an SRID. Choose WGS84 by default.

			if (node instanceof GeographicNode)
			{
				srid= LocalGISGeometryBuilder.guessSRIDCode(((GeographicNode)node).getPosition());
				coordinates=((GeographicNode)node).getPosition().getDirectPosition().getCoordinate();
			}
			else
			{
			XYNode xyNode=(XYNode)node;
			coordinates=new double[]{xyNode.getCoordinate().x,xyNode.getCoordinate().y};
			}
			sql = "UPDATE network_nodes SET id_node = "+node.getID()+" ,node_geometry = "
					+ getWKTSectionPoint(coordinates, srid)
					+ " WHERE id_network = "+
					getQueryForReadingNetworkId(networkName)+" AND id_node="+node.getID();
		}
		else
		{
		// Store Node with NULL geometry
			sql = "UPDATE network_nodes SET id_node = "+node.getID()+" ,node_geometry = null"
			+ " WHERE id_network = "+
			getQueryForReadingNetworkId(networkName)+" AND id_node="+node.getID();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("updateNode(Statement, Node) - " + sql); //$NON-NLS-1$
		}
		return sql;

	}
	private String getWKTSectionPoint(double[] coords, String srid)
	{
		return "PointFromText('POINT( "+coords[0]+" "+coords[1]+")',"+srid+")";
	}

	public String getQueryForReadingASectionOfGraphRelatedWithEdgeID(
			String networkName, Integer edgeId) {
		String sql = "SELECT network_edges.*,"
			+ "(SELECT astext(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodea)) AS geomnodoa, "
			+ "(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeA AND network_nodes.id_node= network_edges.id_nodea)) AS sridnodoa, "
			+ "(SELECT astext(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeb)) AS geomnodob, "
			+ "(SELECT srid(node_geometry) FROM network_nodes WHERE (network_nodes.id_network = network_edges.id_network_nodeB AND network_nodes.id_node= network_edges.id_nodeb)) AS sridnodob "
			+ "FROM network_edges WHERE (network_edges.id_network="+ getQueryForReadingNetworkId(networkName) + ") AND "
			+ "(network_edges.id_edge=" + edgeId + ")";
		return sql;
	}
	public ArrayList<Integer> getNodesWithImpedance(String networkName, Connection connection) {
		ArrayList<Integer> idNodes = new ArrayList<Integer>();
		String sqlQuery=
			"SELECT id_node "+
			"FROM network_impedance_matrix group by id_node";
		// Quitamos la condicion para que sea un idnetwork concreto.
//			" " +
//			"WHERE id_network_node = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
	    	//connection = getConnection();
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Buscando matriz de impedancias en el tramo");
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Matriz de impedancias encontrada.");
		    	Integer idNode = rs.getInt("id_node");
		    	idNodes.add(idNode);
		    }
		} catch (SQLException e) {
			LOGGER.error(e);
		} catch (Exception e) {
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return idNodes;

	}
	public boolean deleteNetworkFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM networks where id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteNetworkEdgesFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_edges WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteEdgesById(Edge edge,int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
		try{
		    String query = "DELETE FROM network_edges WHERE id_edge= " +  edge.getID()+" and id_network="+idnetwork;
			preparedStatement = connection.prepareStatement(query);
			int n = preparedStatement.executeUpdate();
			if (n > 0){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteStreetPropertiesById(Edge edge,int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
		try{
		    String query = "DELETE FROM network_street_properties WHERE id_edge= " +  edge.getID()+" and id_network="+idnetwork;
			preparedStatement = connection.prepareStatement(query);
			int n = preparedStatement.executeUpdate();
			if (n > 0){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}

	public boolean deleteNetworkNodesFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_nodes WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteNetworkPropertiesFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_properties WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteNetworkStreetPropertiesFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_street_properties WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteNetworkImpedanceMatrixFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_impedance_matrix WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public boolean deleteNetworkIncidentsFromDataBaseById(int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
		try{
		    String query = "DELETE FROM network_incidents WHERE id_network = " + idnetwork;
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	/**
	 * Devuelve la query que obtiene toda una capa sin filtro por municipio
	 * @param idLayer
	 * @param srid EPSG code para transformar las geometrías.
	 * @return
	 */
	public String getQueryFromIdLayer(Connection con, int idLayer,String srid)
	{
		String unformattedQuery = "";
		String sqlQuery = "select selectquery FROM queries WHERE id_layer = ?";
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando en base de datos");
	    	preparedStatement = con.prepareStatement(sqlQuery);
	    	preparedStatement.setInt(1,idLayer);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Tabla encontrada. "+rs.getString("selectquery"));
		    	unformattedQuery = rs.getString("selectquery");
		    }
		    preparedStatement.close();
	    	rs.close();

		} catch (SQLException e) {
			LOGGER.error("Error obtaining layer query",e);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);

        }
		return formatQuery(unformattedQuery,srid);
	}
	private String formatQuery(String unformattedQuery,String srid) {
		// Sustituyo el ?M por un is not null. Siempre debe haber un valor de id de municipio.
		unformattedQuery = unformattedQuery.replaceAll("\\?M","\\?m").replaceAll("= {0,}\\?m", " IS NOT NULL");
		unformattedQuery = unformattedQuery.replaceAll(" IN "," in ").replaceAll("\\?M","\\?m").replaceAll("in {0,}\\( {0,}\\?m {0,}\\)", " IS NOT NULL");
		unformattedQuery = unformattedQuery.replaceAll("\\?T","\\?t").replaceAll("\\?t",srid);
		return unformattedQuery;
	}
	public ArrayList<String> getQueryLayersFromNetworkname(Connection conn,
			String networkName,String srid) {
		ArrayList<Integer> idLayers = getIdLayersFromNetworkName(conn,networkName);
		ArrayList<String> queries = new ArrayList<String>();
		Iterator<Integer> it = idLayers.iterator();
		while(it.hasNext()){
			String query = getQueryFromIdLayer(conn, it.next(), srid);
			if(query != null && !query.equals(""))
				queries.add(query);
		}
		return queries;
	}
	private ArrayList<Integer> getIdLayersFromNetworkName(Connection con,
			String networkName) {
		String sqlQuery = "SELECT id_layer FROM network_edges WHERE id_network ="+getQueryForReadingNetworkId(networkName)+" GROUP BY id_layer";
		ArrayList<Integer> idLayers = new ArrayList<Integer>();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando en base de datos "+sqlQuery);
	    	preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery();
		    while(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Tabla encontrada. "+rs.getInt(1));
		    	Integer id = rs.getInt(1);
		    	if(id != null)
		    		idLayers.add(id);
		    }
		    preparedStatement.close();
	    	rs.close();

		} catch (SQLException e) {
			LOGGER.error("Error obtaining layer query",e);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);

        }
		return idLayers;
	}
	public ArrayList<NetworkEdge> getNearestEdges(Connection conn,
			org.opengis.geometry.primitive.Point point, double radius, int num,
			String geometryQuery,String networkName) 
		{
		ArrayList<NetworkEdge> networkEdges = new ArrayList<NetworkEdge>();
		String srid=LocalGISGeometryBuilder.guessSRIDCode(point);
		String geom = "layers.\"GEOMETRY\"";
		String orig = LocalGISGeometryBuilder.getWKTSQLSectionGeometry(point, srid);
		String distance = "distance("+geom+","+orig+")";
		String netNameClause="";
		if (networkName!=null)
		    netNameClause="AND id_network = " + getQueryForReadingNetworkId(networkName);
		    
		String sqlQuery = "select id_edge, id_network," + distance + " as distance from network_edges, " +
						"("+geometryQuery+") layers " +
						"WHERE layers.id = network_edges.id_feature " +
						netNameClause + " " +
						"AND "+distance+" < "+radius+ " " +
						"AND " + geom + " && expand("+ orig + "::geometry,"+radius+") "+
						"order by distance";
		if(num!=0)
		{
			sqlQuery +=" LIMIT "+num;
		}
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando en base de datos");
	    	preparedStatement = conn.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery();
		    while(rs.next()){
		    	NetworkEdge nEdge = new NetworkEdge();
		    	nEdge.setIdEdge(rs.getInt("id_edge"));
		    	nEdge.setDistance(rs.getDouble("distance"));
		    	nEdge.setNetworkId(rs.getInt("id_network"));
		    	networkEdges.add(nEdge);
		    }

		} catch (SQLException e) {
			e.printStackTrace();
	    	System.out.println(e.getMessage()+" "+sqlQuery);
			LOGGER.error("Error obtaining layer query",e);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);

        }
		return networkEdges;
	}
	/**
	 * Metodo que devuelve un Linestring con un srid determinado a partir del id de feature y del id de Layer
	 * @param idFeature
	 * @param idLayer
	 * @param srid
	 * @return
	 */
	public MultiLineString getMultiLinestringFromQueryAndIdFeature(Connection con,
			String query, int idFeature,int srid,String geometryColumn) {
		MultiLineString result = null;
		String sqlQuery = "SELECT AsText(" + geometryColumn + ") as geom FROM (" + query + ") resultTable WHERE resultTable.id = ?";
//		LOGGER.debug(sqlQuery);
//		LOGGER.debug("id = " + idFeature);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando en base de datos");
	    	preparedStatement = con.prepareStatement(sqlQuery);
	    	preparedStatement.setInt(1,idFeature);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Tabla encontrada.");
//		    	LOGGER.debug("Result : " +rs.getString("geom"));
		    	PGgeometry geo = new PGgeometry(rs.getString("geom"));
		    	Geometry geom = JtsPostgisOperations.convert2JTS(geo);
		    	if(geom instanceof LineString){
		    		LineString[] lineStringArray = new LineString[]{(LineString)geom};
		    		result = LocalGISGeometryBuilder.getMultiLineStringFromLineStringArray(lineStringArray, srid);
		    	}else{
		    		result = (MultiLineString)JtsPostgisOperations.convert2JTS(geo);
		    	}
		    	result.setSRID(srid);
		    }
		    preparedStatement.close();
	    	rs.close();

		} catch (SQLException e) {
			LOGGER.error(e);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);

	    }

		return result;
	}
	/**
	 * Calcula las dos geometrías que se dividen en el punto más cercano a point
	 * TODO refactorizar utilizando la función JTS de representacion lineal de linestrings
	 * @param virtualNodeInfo
	 * @param nearestEdge
	 * @param point
	 * @param connection
	 */
	public void setSplittedLineStrings(VirtualNodeInfo virtualNodeInfo,Edge nearestEdge, org.opengis.geometry.primitive.Point point,Connection connection){
    	double[] coords = point.getDirectPosition().getCoordinate();
    	Coordinate coordinate = new Coordinate(coords[0],coords[1]);
    	String sridString = LocalGISGeometryBuilder.guessSRIDCode(point);
		int srid = Integer.parseInt(sridString);
    	GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),srid);
    	LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
    	String query = networkDAO.getQueryFromIdLayer(connection, ((ILocalGISEdge)nearestEdge).getIdLayer(), sridString);
    	MultiLineString mls = networkDAO.getMultiLinestringFromQueryAndIdFeature(connection, query, ((ILocalGISEdge)nearestEdge).getIdFeature(), srid, LocalGISGeometryBuilder.getGeometryColumn(((ILocalGISEdge)nearestEdge).getIdLayer()));
    	ArrayList<Coordinate> coordinates = LocalGISGeometryBuilder.getCoordinates(mls.getCoordinates());
    	//TODO: Comprobar si la geometria devuelta estï¿½ al revï¿½s que el tramo construido
		/*if (!((link.getStartNode().getGeometry().getFirstPoint()[0] == link.getGeometry().getOrdinatesArray()[0]) && (link.getStartNode().getGeometry().getFirstPoint()[1] == link.getGeometry().getOrdinatesArray()[1]))){
			Collections.reverse(coordinates);
		}*/
		LineString lineaRutaTemporal = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]));
        Coordinate origCoordinate = new Coordinate(point.getDirectPosition().getCoordinate()[0],point.getDirectPosition().getCoordinate()[1]);
        com.vividsolutions.jts.geom.Point pointOrigCoordinate = geometryFactory.createPoint(origCoordinate);
        Coordinate[] coordMasCercano = DistanceOp.closestPoints(lineaRutaTemporal, pointOrigCoordinate);
        com.vividsolutions.jts.geom.Point puntoMasCercano = geometryFactory.createPoint(coordMasCercano[0]);
        double distanciaPunto = DistanceOp.distance(lineaRutaTemporal,puntoMasCercano);
        int position =0;

        for (int i = 0; i < coordinates.size()-1; i++)
        {

            Coordinate actualCoord = (Coordinate) coordinates.get(i);
            Coordinate nextCoord = (Coordinate) coordinates.get(i+1);

            if(LocalGISGeometryBuilder.estaEnElSegmentoJTS(geometryFactory,actualCoord,nextCoord,coordMasCercano[0],distanciaPunto))
            {
            	position = i;
            	i = coordinates.size();
            }
        }
        // Ojo, debe haber al menos un segmento para que funcione.
        AbstractList<Coordinate> list = (AbstractList<Coordinate>) coordinates.subList(0,position + 1);
        AbstractList<Coordinate> list2 = (AbstractList<Coordinate>) coordinates.subList(position + 1,coordinates.size());
        coordinates = new ArrayList<Coordinate>(list);
        coordinates.add(coordMasCercano[0]);
        LineString ls = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]));
        LineString ls2 = null;
        list2.add(0,coordMasCercano[0]);
        if(list2.size() > 1){
        	ls2 = geometryFactory.createLineString((Coordinate[]) list2.toArray(new Coordinate[list2.size()]));
        }
        else
        	ls2 = ls;
        virtualNodeInfo.setVirtualNodePoint(puntoMasCercano);
        virtualNodeInfo.setLinestringAtoV(ls);
        virtualNodeInfo.setLinestringVtoB(ls2);
        virtualNodeInfo.setRatio(ls.getLength() / lineaRutaTemporal.getLength());
        virtualNodeInfo.setPoint(GeographicNodeUtil.createISOPoint(puntoMasCercano,point.getCoordinateReferenceSystem()));

	}
	public List<NetworkEdge> getNetworkEdgesNearTo(Connection connection,String networkName,org.opengis.geometry.primitive.Point point, double radius, int num) throws IOException
	{
		ArrayList<NetworkEdge> networkEdges = new ArrayList<NetworkEdge>();

		String srid = LocalGISGeometryBuilder.guessSRIDCode(point);
		// Primero, obtener la lista de layers
		// Bucle para obtener un array de String con las queries
		ArrayList<String> queryList = getQueryLayersFromNetworkname(connection,networkName,srid);
		if (queryList.size()==0)
			throw new RuntimeException("No se pueden buscar los arcos cercanos. Quizás no existe el nombre de red "+networkName);
		// Bucle con cada query para obtener la lista de edges con distancia
		Iterator<String> it = queryList.iterator();

		while(it.hasNext()){
			String actualQuery = it.next();
			networkEdges.addAll(getNearestEdges(connection,point,radius,num,actualQuery,networkName));
		}
		// Obtener el o los edges con la distancia mas corta ordenados en el list
		Collections.sort(networkEdges, new NetworkEdgesComparator());
		return networkEdges;
	}
	public boolean deleteNetworkFromDataBaseByNetworkName (String networkName, Connection conn) {

		boolean resultado = false;
		int idnetwork = -1;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    /*delete from networks;
	    delete from network_edges;
	    delete from network_nodes;
	    delete from network_properties;
	    delete from network_street_properties;
	    delete from network_impedance_matrix;
	    delete from network_incidents;*/
	    try{
	    	String query = "SELECT * FROM networks WHERE network_name = '" + networkName + "'";
//	    	LOGGER.debug(query);
	    	preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idnetwork = rs.getInt("id_network");
//				LOGGER.debug("Borrando network con id = " + idnetwork);
			}
			if (idnetwork > 0){
				resultado = deleteNetworkFromDataBaseById(idnetwork, conn);
//				LOGGER.debug("Resultado de borrado = " + resultado);
				//TODO: Modificar en base de datos para que elimine las redes enteras con ON DELETE CASCADE
				deleteNetworkEdgesFromDataBaseById(idnetwork, conn);
				deleteNetworkNodesFromDataBaseById(idnetwork, conn);
				deleteNetworkPropertiesFromDataBaseById(idnetwork, conn);
				deleteNetworkStreetPropertiesFromDataBaseById(idnetwork, conn);
				deleteNetworkImpedanceMatrixFromDataBaseById(idnetwork, conn);
				deleteNetworkIncidentsFromDataBaseById(idnetwork, conn);
			}
	    }catch (SQLException e) {
			// TODO: handle exception
	    	LOGGER.error(e);
	    	e.printStackTrace();
	    	resultado = false;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
	    	resultado = false;
	    	e.printStackTrace();
		}
		return resultado;
	}
	public boolean deleteEdge (Edge edge,String networkName, Connection conn) {

		boolean resultado = false;
		int idnetwork = -1;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try{
	    	String query = "SELECT * FROM networks WHERE network_name = '" + networkName + "'";
//	    	LOGGER.debug(query);
	    	preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idnetwork = rs.getInt("id_network");
//				LOGGER.debug("Borrando network con id = " + idnetwork);
			}
			if (idnetwork > 0){
				resultado = this.deleteStreetPropertiesById(edge, idnetwork, conn);
				resultado = this.deleteEdgesById(edge, idnetwork, conn);
			}
	    }catch (SQLException e) {
			// TODO: handle exception
	    	LOGGER.error(e);
	    	e.printStackTrace();
	    	resultado = false;
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
	    	resultado = false;
	    	e.printStackTrace();
		}
		return resultado;
	}

	private String getQueryToDeleteNetworkById(int networkId) {
		// TODO Auto-generated method stub
		return "delete from networks where id=" + networkId;
	}

	private String getQueryForReadingNetworkById(int networkId){
		return "select * from networks where id=" + networkId;
	}


	public boolean existsNetworkIntoDataBaseByNetworkId(int networkId, Connection conn){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try{
	    	String query = getQueryForReadingNetworkById(networkId);
	    	preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
	    }catch (SQLException e) {
			// TODO: handle exception
	    	LOGGER.error(e);
	    	resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			resultado = false;
		}
		return resultado;
	}

	public boolean existsNetworkIntoDataBaseByNetworkName(String networkName, Connection conn){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try{
	    	String query = getQueryForReadingNetworkId(networkName);
	    	preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				resultado = true;
			}
	    }catch (SQLException e) {
			// TODO: handle exception
	    	LOGGER.error(e);
	    	resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			resultado = false;
		}
		return resultado;
	}
	public int getNextDatabaseIdNode(Connection connection) throws SQLException {
		int result = 0;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    String query = "SELECT NEXTVAL('network_node_sequence')";
	    try{
	    	preparedStatement = connection.prepareStatement(query);
	    	rs = preparedStatement.executeQuery();
	    	if (rs.next()){
	    		result = rs.getInt(1);
	    	}
	    }finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return result;
	}
	public int getNextDatabaseIdEdge(Connection connection) throws SQLException {
		int result = 0;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    String query = "SELECT NEXTVAL('network_edge_sequence')";
	    try{
		    preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getInt(1);
			}
		}finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return result;
	}
	public int getNextDatabaseIdNode(Connection connection, int idEntidad) throws SQLException {
		int result = 0;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    String query = "SELECT max(id_node)+1 from network_nodes where id_node>= "+idEntidad*100000 +"and id_node< "+(idEntidad+1)*100000;
	    try{
	    	preparedStatement = connection.prepareStatement(query);
	    	rs = preparedStatement.executeQuery();
	    	if (rs.next()){
	    		result = rs.getInt(1);
	    	}
	    }finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return result;
	}
	public int getNextDatabaseIdEdge(Connection connection, int idEntidad) throws SQLException {
		int result = 0;
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    String query = "SELECT max(id_edge)+1 from network_edges where id_edge>= "+idEntidad*100000 +"and id_edge< "+(idEntidad+1)*100000;
	    try{
		    preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getInt(1);
			}
		}finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return result;
	}
	public String getQueryForReadingNetworkName(int networkId) {
		return "()";
	}
	public String getNetworkName(int idNetworkNodeA, Connection connection) throws SQLException {
		String result = "";
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    String query = "SELECT network_name FROM networks WHERE id_network = " + idNetworkNodeA;
	    try{
		    preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getString("network_name");
			}
		}finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return result;
	}
	public Hashtable<Integer,String> getNetworkNames(Connection connection) throws SQLException {

		Hashtable<Integer,String> ht = new Hashtable<Integer,String>();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    // BUG era
//	    String query = "SELECT network_id,network_name FROM networks";
	    String query = "SELECT id_network,network_name FROM networks";
	    try{
		    preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()){

				ht.put(new Integer(rs.getInt("id_network")),rs.getString("network_name"));
			}
		}finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return ht;
	}
	public void removeNetworkLinks(String networkName, Connection connection) throws SQLException {
		String sqlQuery = "DELETE FROM network_edges WHERE id_network_nodea != id_network_nodeb AND id_network = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    try{
		    preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.executeUpdate();
		}finally{
	    	ConnectionUtilities.closeConnection(null, preparedStatement, null);
	    }

	}

	public void getStreetData(String networkName,LocalGISStreetResultSet rss,Connection connection) throws SQLException {

		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + rss.getId_edge();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	rss.setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
	    		rss.setIrregularPaving(rs.getBoolean("irregular_pavement"));
	    		rss.setWidth(rs.getDouble("width"));
	    		rss.setLongitudinalSlope(rs.getDouble("longitudinal_slope"));
	    		rss.setTransversalSlope(rs.getDouble("transversal_slope"));
		    }else{
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Datos no encontrados. Estableciendo velocidad maxima por de fecto , 50km/h.");
		    	// TODO: Establecer una velocidad maxima para la via por defecto. Donde, en xml o plano. Al ser un municipio, se deberia usar 50, ya que suele ser parte urbana.
		    	// Establecido 12 m/s, ya que la longitud es en metros.
		    	rss.setNominalMaxSpeed(12);
		    }
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	}
	public Hashtable<Integer,LocalGISStreetResultSet> getStreetDataList(String networkName,Connection connection) throws SQLException {
		//TODO: Se puede crear un objeto mas ligero. De momento uso este para almacenar estos datos.
		Hashtable<Integer,LocalGISStreetResultSet> streetData = new Hashtable<Integer,LocalGISStreetResultSet>();
		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName);
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    while(rs.next()){
		    	LocalGISStreetResultSet rss = new LocalGISStreetResultSet();
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	rss.setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	Integer idEdge = new Integer(rs.getInt("id_edge"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		rss.setStreetTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
	    		try
			    {// PMR fields
				rss.setIrregularPaving(rs.getInt("irregular_pavement") == 1);
				rss.setWidth(rs.getDouble("width"));
				rss.setLongitudinalSlope(rs.getDouble("longitudinal_slope"));
				rss.setTransversalSlope(rs.getDouble("transversal_slope"));
				rss.setRelatedToId(rs.getInt("id_edge_related"));
				rss.setCalculatedSide(rs.getInt("calculated_side"));
				rss.setsEdgeType(rs.getString("edge_type"));
				if (rss.getsEdgeType() != null && rss.getsEdgeType().equals("ZEBRA"))
				    {
					rss.setsType(rs.getString("zebra_type"));
				    }
			    } catch (Exception e)
			    {
				LOGGER.debug(e);
			    }
		    	streetData.put(idEdge,rss);
		    }
	    }catch(Exception e){
			e.printStackTrace();
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }
		return streetData;
	}
	public void getStreetData(String networkName,Edge edge,Connection connection) throws SQLException {

		String sqlQuery=
			"SELECT * "+
			"FROM network_street_properties " +
			"WHERE id_network = "+getQueryForReadingNetworkId(networkName) + " " +
			"AND id_edge = " + edge.getID();
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Cargando las propiedades de la calle para la red " + networkName);
	    	preparedStatement = connection.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Propiedades de la calle encontradas.");
		    	((LocalGISStreetDynamicEdge)edge).setNominalMaxSpeed(rs.getDouble("maxspeed"));
		    	// en este orden BIDIRECTIONAL,DIRECT,INVERSE,FORBIDDEN
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.BIDIRECTIONAL.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.BIDIRECTIONAL);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.DIRECT.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.DIRECT);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.INVERSE.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.INVERSE);
		    	if(rs.getString("traffic_orientation").equals(StreetTrafficRegulation.FORBIDDEN.toString()))
		    		((LocalGISStreetDynamicEdge)edge).setTrafficRegulation(StreetTrafficRegulation.FORBIDDEN);
		    	if (edge instanceof PMRLocalGISStreetDynamicEdge){
			    	((PMRLocalGISStreetDynamicEdge)edge).setWidth(rs.getDouble("width"));
			    	((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(rs.getDouble("longitudinal_slope"));
			    	((PMRLocalGISStreetDynamicEdge)edge).setTransversalSlope(rs.getDouble("transversal_slope"));
			    	if (((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType().equals("EDGE")){
			    		((PMRLocalGISStreetDynamicEdge) edge).setEdgeType("EDGE");
			    	}else{
						((ZebraDynamicEdge) edge).setEdgeType("ZEBRA");
						if (((ZebraDynamicEdge) edge).getsType().equals("SIN REBAJE"))
							((ZebraDynamicEdge) edge).setType("SIN REBAJE");
						else
							((ZebraDynamicEdge) edge).setType("CON REBAJE");
			    	}
				}
		    }else{
		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Datos no encontrados. Estableciendo velocidad maxima por de fecto , 50km/h.");
		    	// TODO: Establecer una velocidad maxima para la via por defecto. Donde, en xml o plano. Al ser un municipio, se deberia usar 50, ya que suele ser parte urbana.
		    	// Establecido 12 m/s, ya que la longitud es en metros.
		    	((LocalGISStreetDynamicEdge)edge).setNominalMaxSpeed(12);
		    }
	    } finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
	    }

	}
	public void updateStreetData(String networkName,LocalGISStreetDynamicEdge edge,Connection connection) throws SQLException, Exception {
		Integer networkId = getNetworkId(networkName,connection);
		PreparedStatement st = null;

//		if (LOGGER.isDebugEnabled()) LOGGER.debug("Actualizando propiedades de calle de la red " + networkName + " en base de datos ");

		String sqlQueryForInsertStreetProperties = "";
		try {
			if (edge instanceof PMRLocalGISStreetDynamicEdge){
				sqlQueryForInsertStreetProperties = "UPDATE network_street_properties set maxspeed = ?,traffic_orientation = ?,irregular_pavement = ?,transversal_slope = ?,longitudinal_slope = ?,width = ?,edge_type =?,obstacle_height=?,id_edge_related=?,calculated_side=?,zebra_type=? WHERE id_network = ? AND id_edge = ?";
				st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
				st.setInt(12, networkId);
				st.setInt(13, edge.getID());
				st.setDouble(1, edge.getNominalMaxSpeed());
				st.setString(2, edge.getTrafficRegulation().toString());
				if (((PMRLocalGISStreetDynamicEdge)edge).getIrregularPavement())
					st.setInt(3, 1);
				else
					st.setInt(3, 0);
				st.setDouble(4, ((PMRLocalGISStreetDynamicEdge)edge).getTransversalSlope());
				st.setDouble(5, ((PMRLocalGISStreetDynamicEdge)edge).getLongitudinalSlope());
				st.setDouble(6, ((PMRLocalGISStreetDynamicEdge)edge).getWidth());
				st.setString(7, ((PMRLocalGISStreetDynamicEdge)edge).getsEdgeType());
				st.setDouble(8, ((PMRLocalGISStreetDynamicEdge)edge).getObstacleHeight());
				st.setInt(9, ((PMRLocalGISStreetDynamicEdge)edge).getRelatedToId());
				st.setInt(10, ((PMRLocalGISStreetDynamicEdge)edge).getCalculatedSide());
				if (edge instanceof ZebraDynamicEdge)
					st.setString(11, ((ZebraDynamicEdge)edge).getsType());
				else
					st.setNull(11, java.sql.Types.VARCHAR);
				st.executeUpdate();
			}else{
				sqlQueryForInsertStreetProperties = "UPDATE network_street_properties set maxspeed = ?,traffic_orientation = ? WHERE id_network = ? AND id_edge = ?";
				st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
				st.setInt(3, networkId);
				st.setInt(4, edge.getID());
				st.setDouble(1, edge.getNominalMaxSpeed());
				st.setString(2, edge.getTrafficRegulation().toString());
				st.executeUpdate();
			}
		}catch(Exception e){
			LOGGER.error(e);
	    	System.out.println(e.getMessage()+" "+sqlQueryForInsertStreetProperties);
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	public void setStreetData(String networkName,int networkId,LocalGISStreetDynamicEdge edge,Connection connection) throws SQLException
	{
//		Integer networkId = getNetworkId(networkName,connection);
		PreparedStatement st = null;

//		if (LOGGER.isDebugEnabled()) LOGGER.debug("Guardando propiedades de calle de la red " + networkName + " en base de datos ");


		try {
			if (edge instanceof PMRLocalGISStreetDynamicEdge){
				String sqlQueryForInsertStreetProperties = "INSERT INTO network_street_properties ";
				sqlQueryForInsertStreetProperties += "(id_network, id_edge, maxspeed, traffic_orientation, irregular_pavement, ";
            	sqlQueryForInsertStreetProperties += "transversal_slope, longitudinal_slope, width,edge_type,obstacle_height,id_edge_related,calculated_side,zebra_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
				st.setInt(1, networkId);
				st.setInt(2, edge.getID());
				st.setDouble(3, edge.getNominalMaxSpeed());
				st.setString(4, edge.getTrafficRegulation().toString());
				if (((PMRLocalGISStreetDynamicEdge)edge).getIrregularPavement())
					st.setInt(5, 1);
				else
					st.setInt(5, 0);
				st.setDouble(6, ((PMRLocalGISStreetDynamicEdge)edge).getTransversalSlope());
				st.setDouble(7, ((PMRLocalGISStreetDynamicEdge)edge).getLongitudinalSlope());
				st.setDouble(8, ((PMRLocalGISStreetDynamicEdge)edge).getWidth());
				st.setString(9, ((PMRLocalGISStreetDynamicEdge)edge).getsEdgeType());
				st.setDouble(10, ((PMRLocalGISStreetDynamicEdge)edge).getObstacleHeight());
				st.setInt(11, ((PMRLocalGISStreetDynamicEdge)edge).getRelatedToId());
				st.setInt(12, ((PMRLocalGISStreetDynamicEdge)edge).getCalculatedSide());
				if (edge instanceof ZebraDynamicEdge)
					st.setString(13, ((ZebraDynamicEdge)edge).getsType());
				else
					st.setNull(13, java.sql.Types.VARCHAR);
				st.executeUpdate();
			}else{
				String sqlQueryForInsertStreetProperties = "INSERT INTO network_street_properties values(?,?,?,?)";
				st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
				st.setInt(1, networkId);
				st.setInt(2, edge.getID());
				st.setDouble(3, edge.getNominalMaxSpeed());
				st.setString(4, edge.getTrafficRegulation().toString());
				st.executeUpdate();

			}
		}catch(Exception e){
			LOGGER.error(e);
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}

	}
	private void writeStreetProperties(LocalGISStreetDynamicEdge edge, int networkId,Connection connection) throws SQLException
	{

		PreparedStatement st = null;
		try {
			if (edge instanceof PMRLocalGISStreetDynamicEdge){
				String sqlQueryForInsertStreetProperties = "INSERT INTO network_street_properties ";
				sqlQueryForInsertStreetProperties += "(id_network, id_edge, maxspeed, traffic_orientation, irregular_pavement, ";
            	sqlQueryForInsertStreetProperties += "transversal_slope, longitudinal_slope, width,edge_type,obstacle_height,id_edge_related,calculated_side, zebra_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				st = connection.prepareStatement(sqlQueryForInsertStreetProperties);
				st.setInt(1, networkId);
				st.setInt(2, edge.getID());
				st.setDouble(3, edge.getNominalMaxSpeed());
				st.setString(4, edge.getTrafficRegulation().toString());
				if (((PMRLocalGISStreetDynamicEdge)edge).getIrregularPavement())
					st.setInt(5, 1);
				else
					st.setInt(5, 0);
				st.setDouble(6, ((PMRLocalGISStreetDynamicEdge)edge).getTransversalSlope());
				st.setDouble(7, ((PMRLocalGISStreetDynamicEdge)edge).getLongitudinalSlope());
				st.setDouble(8, ((PMRLocalGISStreetDynamicEdge)edge).getWidth());
				st.setString(9, ((PMRLocalGISStreetDynamicEdge)edge).getsEdgeType());
				st.setDouble(10, ((PMRLocalGISStreetDynamicEdge)edge).getObstacleHeight());
				st.setInt(11, ((PMRLocalGISStreetDynamicEdge)edge).getRelatedToId());
				st.setInt(12, ((PMRLocalGISStreetDynamicEdge)edge).getCalculatedSide());
				if (edge instanceof ZebraDynamicEdge)
					st.setString(13, ((ZebraDynamicEdge)edge).getsType());
				else
					st.setNull(13, java.sql.Types.VARCHAR);
				st.executeUpdate();
			}else{
				String sql = "INSERT INTO network_street_properties values (?,?,?,?)";
				st = connection.prepareStatement(sql);
				st.setInt(1, networkId);
				st.setInt(2, edge.getID());
				st.setDouble(3, edge.getNominalMaxSpeed());
				st.setString(4, edge.getTrafficRegulation().toString());
				st.executeUpdate();
			}
		}finally {
			ConnectionUtilities.closeConnection(null, st, null);
		}
	}
	public int deleteNodes(Collection<Node> nodes, int networkId, Connection connection) {
	    if (nodes.isEmpty())
		return 0;
	    
		StringBuilder sb= new StringBuilder();
		boolean first=true;
		int n = 0;
		for (Node node : nodes)
		{
			if (!first)
				{
				sb.append(',');
				}
			sb.append(node.getID());
			first=false;
		}

		boolean resultado = false;
		PreparedStatement preparedStatement = null;
		try{
		    StringBuilder query = new StringBuilder("DELETE FROM network_nodes WHERE id_node in (").append(sb).append(") ");
		    query.append(" and id_network =").append(networkId);
			preparedStatement = connection.prepareStatement(query.toString());
			n = preparedStatement.executeUpdate();
			if (n > 0){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return n;
	}

	public int deleteEdges(Collection<Edge> edges, int networkId, Connection connection) {
	    if (edges.isEmpty())
		return 0;
		int n = 0;
		StringBuilder sb = getIdsFromCollection(edges);

		boolean resultado = false;
		PreparedStatement preparedStatement = null;
		try{
		    StringBuilder query = new StringBuilder("DELETE FROM network_edges WHERE id_edge in (").append(sb).append(") ");
		    query.append(" and id_network =").append(networkId);
			preparedStatement = connection.prepareStatement(query.toString());
			n = preparedStatement.executeUpdate();
			if (n > 0){
				resultado = true;
				deleteStreetPropertiesByIds(sb,networkId, connection);
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return n;
	}
	private StringBuilder getIdsFromCollection(Collection<Edge> edges) {
		StringBuilder sb= new StringBuilder();
		boolean first=true;
		for (Edge edge : edges)
		{
			if (!first)
				{
				sb.append(',');
				}
			sb.append(edge.getID());
			first=false;
		}
		return sb;
	}
	public boolean deleteStreetPropertiesByIds(StringBuilder sbIds,int idnetwork, Connection connection){
		boolean resultado = false;
		PreparedStatement preparedStatement = null;
		try{
			StringBuilder query = new StringBuilder("DELETE FROM network_street_properties WHERE id_edge in ( " ).append(sbIds);
			query.append(") and id_network=").append(idnetwork);
			preparedStatement = connection.prepareStatement(query.toString());
			int n = preparedStatement.executeUpdate();
			if (n > 0){
				resultado = true;
			}
		}catch (SQLException e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e);
			resultado = false;
		}
		return resultado;
	}
	public void setTaskMonitor(TaskMonitor taskMonitor)
	{
	    this.taskMonitor=new WeakReference<TaskMonitor>(taskMonitor);
	}
}
