package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;


import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.io.DBRouteServerReaderWriter;


//import org.uva.route.graph.build.dynamic.DynamicGraphGenerator;
//import org.uva.route.graph.build.optxy.OptXYLineGraphGenerator;
//import org.uva.route.graph.structure.opt.MiOptEdge;

import com.geopista.app.AppContext;
import com.localgis.route.graph.build.dynamic.LocalGISGraphGenerator;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;

public abstract class MiDBReaderWriter extends DBRouteServerReaderWriter {
	/** JDBC driver class name key **/
	public static final String DRIVERCLASS = null;

	/** JDBC driver url **/
	public static final String DRIVERURL = "DRIVERURL";

	/** Database server key **/
	public static final String SERVER = "SERVER";

	/** Database port key **/
	public static final String PORT = "PORT";

	/** Database name key **/
	public static final String DBNAME = "DBNAME";

	/** User name key **/
	public static final String USERNAME = "USERNAME";

	/** Table key **/
	public static final String TABLENAME = "TABLENAME";

	/** password **/
	public static final String PASSWORD = "PASSWORD";

	/**
	 * Performs a graph read by querying the database and processing each tuple
	 * returned in the query. As each tuple is processed, the graph components
	 * represented by the tuple are created by an underlying GraphGenerator.
	 * 
	 * @see org.geotools.graph.io.GraphReaderWriter#read()
	 */
	public Graph read() {
		// get underlying generator
		// OptXYLineGraphGenerator generator = (OptXYLineGraphGenerator) getProperty(AbstractReaderWriter.GENERATOR);

		LocalGISGraphGenerator generator = new LocalGISGraphGenerator();
//		generator.setGraphBuilder(new DynamicGraphBuilder());
		
		// create database connection
		Connection conn;
		try {
			conn = getConnection();

			PreparedStatement st = conn.prepareStatement(getQuery());
			System.out.println(getQuery());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				generator.add(readInternal(rs));
				// System.out.println(((RouteResultSet)readInternal(rs)).getId_nodoA());
				// System.out.println(((RouteResultSet)readInternal(rs)).getId_nodoB());
			}

			// close database connection
			rs.close();
			st.close();

			if (generator.getGraph() == null) {
				return null;
			} else {
				return generator.getGraph();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Performs a write on the graph out to the database. If the NODES property
	 * is set, the nodes of the graph will be written, and if the EDGES property
	 * is set, the edges of the graph will be written.
	 * 
	 * * @see GraphGenerator#write()
	 * @param context 
	 */

	public void write(Graph g){
		// get database connection
		
		// write nodes if property set
		// if (getProperty(NODES) != null) {
		for (Iterator itr = g.getNodes().iterator(); 
		itr.hasNext();) {
			
			writeNode( (Node) itr.next());
		}
		// write edges if property set
		// if (getProperty(EDGES) != null) {
		for (Iterator itr = g.getEdges().iterator(); itr.hasNext();) {
			writeEdge( (LocalGISDynamicEdge) itr.next());
		}

	}

	public Connection getConnection(){
		// read database + driver properties
//		String driverclass = (String) getProperty(DRIVERCLASS);
//		String driverurl = (String) getProperty(DRIVERURL);
//		String server = (String) getProperty(SERVER);
//		String port = (String) getProperty(PORT);
//		String dbname = (String) getProperty(DBNAME);
//		String username = (String) getProperty(USERNAME);
//		String password = (String) getProperty(PASSWORD);
//
//		ManageableDataSource daoFactory = null;
//
//		try {
//			daoFactory = PostgisDataStoreFactory.getDefaultDataSource(server,
//					username, password, Integer.parseInt(port), dbname, 5, 0,
//					true);
//		} catch (DataSourceException e) {
//			e.printStackTrace();
//		}
//		Connection conn = null;
//		try {
//			conn = daoFactory.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		Connection conn = null;
		if (AppContext.getApplicationContext().isLogged()){
			
			try {
				Connection connection = AppContext.getApplicationContext().getConnection();
				if (connection != null){
					conn = connection;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return conn;

	}

	/**
	 * Template method used to write a node into the database.
	 * 
	 * @param st
	 *            Statement used to execute write statement.
	 * @param node
	 *            Node to write.
	 */
	protected void writeNode(Node node) {
	}

	/**
	 * Template method used to write an edge into the database.
	 * 
	 * @param edge
	 *            Edge to write.
	 */
	protected void writeEdge(Edge edge) {
	}

	/**
	 * Template method which returns the query to execute in order to read a
	 * graph from the database.
	 * 
	 * @return SQL query.
	 */
	protected abstract String getQuery();

	/**
	 * Template method used to create the object represented by a tuple returned
	 * by the database query.
	 * 
	 * @param rs
	 *            ResultSet of query.
	 * 
	 * @return Object represented by current tuple of result set.
	 */
	protected abstract Object readInternal(ResultSet rs);

}
