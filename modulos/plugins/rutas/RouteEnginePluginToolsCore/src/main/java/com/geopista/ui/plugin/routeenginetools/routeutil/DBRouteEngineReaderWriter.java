/**
 * DBRouteEngineReaderWriter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.sql.DataSource;

import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;

import com.geopista.app.AppContext;
import com.localgis.route.datastore.LocalGISResultSet;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * An implementation of GraphReaderWriter used for reading and writing graph
 * objects to and from a database. <BR>
 * <BR>
 * Upon reading, the database is queried using the getQuery() template method,
 * and a representation of the objects to be modelled by the graph are returned
 * through a standard ResultSet. From each tuple in the result set, the object
 * is recreated via the template method readInternal(ResultSet). The object is
 * then passed to an underlying graph generator and the graph components used to
 * model the object are constructed.<BR>
 * <BR>
 * Upon writing, the graph is read component by component based on set
 * properties. As each component is processed, it is passed to the perspective
 * template methods writeNode(Statement,Node) and writeEdge(Statement,Edge). The
 * methods then execute a statement to create the database representation of the
 * graph component.
 * 
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/extension/graph
 *         /src/main/java/org/geotools/graph/io/standard/DBReaderWriter.java $
 */
public class DBRouteEngineReaderWriter extends MiDBReaderWriter {
	private WithOutConnectionPanel panel = null;
	private GeometryFactory fact = new GeometryFactory();

	public DBRouteEngineReaderWriter(PanelToSaveInDataStore panel) {
		this.panel = panel;
//		setProperties();

	}

	public DBRouteEngineReaderWriter(PanelToLoadFromDataStore panel) {
		this.panel = panel;
//		setProperties();
	}

	protected String getQuery() {

		String sql1 = "(SELECT id_subred FROM subredes WHERE nombre= '"
				+ ((PanelToLoadFromDataStore) panel).getSubredSelected()
				+ "' )";

		String sql = "SELECT tramos.*, (SELECT AsText(geometria) FROM nodos WHERE "
				+ "(nodos.id_subred = tramos.id_subred_nodoA AND nodos.id_nodo= tramos.id_nodoA)) AS geomNodoA ,"
				+ "(SELECT AsText(geometria) FROM nodos WHERE (nodos.id_subred = tramos.id_subred_nodoB AND nodos.id_nodo= tramos.id_nodoB))AS geomNodoB "
				+ "FROM tramos  WHERE tramos.id_subred=" + sql1;

		return sql;
		/*
		 * SELECT tramos.*,(SELECT AsText (geometria) FROM nodos WHERE
		 * (tramos.id_nodoA=nodos.id_nodo AND
		 * tramos.id_subred_nodoA=nodos.id_subred))AS geomNodoA, (SELECT AsText
		 * (geometria) FROM nodos WHERE (tramos.id_nodoB=nodos.id_nodo AND
		 * tramos.id_subred_nodoB=nodos.id_subred))AS geomNodoB FROM tramos
		 * WHERE tramos.id_subred=25
		 */
	}

	
	protected Object readInternal(ResultSet rs) {
		try {

			LocalGISResultSet rrs = new LocalGISResultSet();

			int id_subred = rs.getInt("id_subred");
			rrs.setId_subNet(id_subred);
			int id_edge = rs.getInt("id_edge");
			rrs.setId_edge(id_edge);
			int id_nodoA = rs.getInt("id_nodoA");
			rrs.setId_nodeA(id_nodoA);
			int id_subred_nodoA = rs.getInt("id_subred_nodoA");
			rrs.setId_subNet_nodeA(id_subred_nodoA);
			int id_nodoB = rs.getInt("id_nodoB");
			rrs.setId_nodeB(id_nodoB);
			int id_subred_nodoB = rs.getInt("id_subred_nodoB");
			rrs.setId_subNet_nodeB(id_subred_nodoB);
			double coste = rs.getDouble("long_tramo");
			rrs.setImpedance(coste);
			String textnodoA = rs.getString("geomNodoA");
			String textnodoB = rs.getString("geomNodoB");
			rrs.setIdFeature(rs.getInt("id_feature"));
			rrs.setIdLayer(rs.getInt("id_layer"));
			
//			rrs.setObject(rs.getObject("geometry"));
			
			WKTReader reader = new WKTReader(new GeometryFactory());
//			Point geomNodoA = (Point) reader.read(textnodoA);
//			Point geomNodoB = (Point) reader.read(textnodoB);
//			rrs.setPointNodeA((org.opengis.geometry.primitive.Point) geomNodoA.getCoordinate());
//			rrs.setPointNodeB((org.opengis.geometry.primitive.Point) geomNodoB.getCoordinate());

			return rrs;
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} 
//		catch (ParseException e) {
//			throw new RuntimeException(e);
//		}
		
	}
	
	
	
	
//	protected Object readInternal(ResultSet rs) throws Exception {
//		RouteResultSet rrs = new RouteResultSet();
//
//		int id_subred = rs.getInt("id_subred");
//		rrs.setId_subred(id_subred);
//		int id_edge = rs.getInt("id_edge");
//		rrs.setId_edge(id_edge);
//		int id_nodoA = rs.getInt("id_nodoA");
//		rrs.setId_nodoA(id_nodoA);
//		int id_subred_nodoA = rs.getInt("id_subred_nodoA");
//		rrs.setId_subred_nodoA(id_subred_nodoA);
//		int id_nodoB = rs.getInt("id_nodoB");
//		rrs.setId_nodoB(id_nodoB);
//		int id_subred_nodoB = rs.getInt("id_subred_nodoB");
//		rrs.setId_subred_nodoB(id_subred_nodoB);
//		double coste = rs.getDouble("long_tramo");
//		rrs.setCoste(coste);
//		String textnodoA = rs.getString("geomNodoA");
//		String textnodoB = rs.getString("geomNodoB");
//
//		rrs.setIdFeature(rs.getInt("id_feature"));
//		rrs.setIdLayer(rs.getInt("id_layer"));
//		
//		WKTReader reader = new WKTReader(fact);
//		Point geomNodoA = (Point) reader.read(textnodoA);
//		Point geomNodoB = (Point) reader.read(textnodoB);
//		rrs.setCoorNodoA(geomNodoA.getCoordinate());
//		rrs.setCoorNodoB(geomNodoB.getCoordinate());
//
//		// return (new LineSegment(rrs.getCoordNodoA(), rrs.getCoordNodoB()));
//		return rrs;
//	}

	protected void writeEdge( LocalGISDynamicEdge edge) {
		
		Connection conn = null;
		PreparedStatement st = null;
		try {
			String sqlid_subred = "(SELECT id_subred FROM subredes WHERE nombre ='"
				+ ((PanelToSaveInDataStore) panel).getNombreenBase() + "')";
			// System.out.println(sqlid_subred);
			String sqlA = "(SELECT id_subred FROM nodos WHERE id_nodo="
				+ edge.getNodeA().getID() + " AND id_subred=" + sqlid_subred
				+ ")";
			// System.out.println(sqlA);
			String sqlB = "(SELECT id_subred FROM nodos WHERE id_nodo="
				+ edge.getNodeB().getID() + " AND id_subred=" + sqlid_subred
				+ ")";
			// System.out.println(sqlB);
			String sql = "INSERT INTO tramos VALUES (" + sqlid_subred + ","
			+ edge.getID() + "," + edge.getNodeA().getID() + "," + sqlA
			+ "," + edge.getNodeB().getID() + "," + sqlB + ","
			+ ((LocalGISDynamicEdge) edge).getCost() + ","
			+ ((ILocalGISEdge) edge).getIdFeature() + ","
			+ ((ILocalGISEdge) edge).getIdLayer() +  ",'"
			+ ((LocalGISDynamicEdge) edge).getObject().toString()
			+ "')";
			// System.out.println(sql);

			conn = getConnection();
			st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try{st.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	protected void writeNode( Node node) {
		
		PreparedStatement st = null;
		Connection conn = null;
		try{
		String name = ((PanelToSaveInDataStore) panel).getNombreenBase();

		String sql1 = "(SELECT id_subred FROM subredes WHERE nombre='" + name
				+ "')";
		// System.out.println(sql1);
		/*
		 * ResultSet rs = null; String subred_id = null; try { rs =
		 * st.executeQuery(sql1); if(rs.next()) { subred_id =
		 * rs.getString("id_subred"); } else {
		 * System.out.println("No hay subredes con ese nombre"); } } catch
		 * (SQLException e1) { e1.printStackTrace(); }
		 */
		String sql = "INSERT INTO nodos VALUES(" + sql1 + "," + node.getID()
				+ ",PointFromText('"
				+ fact.createPoint(((XYNode) node).getCoordinate()) + "'))";
		// System.out.println(sql);

		
		conn = getConnection();
		st = conn.prepareStatement(sql);
		st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try{st.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}

	}

	/*
	 * protected void setProperties(){
	 * 
	 * setProperty(this.DRIVERCLASS, "org.postgresql.Driver");
	 * setProperty(this.DRIVERURL,"jdbc:postgresql://"); setProperty(this.PORT,
	 * panel.getConnectionDescriptor().); setProperty(this.SERVER, "localhost");
	 * setProperty(this.DBNAME,"mibase" ); setProperty(this.USERNAME,
	 * "postgres"); setProperty(this.PASSWORD, "201175"); }
	 */

	protected void setProperties() {
//		ParameterList params = panel.getConnectionDescriptor()
//				.getParameterList();
//
//		setProperty(DRIVERCLASS, "org.postgresql.Driver");
//		setProperty(DRIVERURL, "jdbc:postgresql://");
//
//		setProperty(PORT, Integer.toString(params.getParameterInt("Port")));
//
//		setProperty(SERVER, params.getParameterString("Server"));
//		setProperty(DBNAME, panel.getConnectionDescriptor().getName());
//		setProperty(USERNAME, params.getParameterString("User"));
//		setProperty(PASSWORD, params.getParameterString("Password"));

	}
	
	public void write(Graph g){
		// get database connection
		
		final Graph grafo = g;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{   
				new Thread(new Runnable()
				{
					public void run()
					{
						int i = 1;
						// write nodes if property set
						// if (getProperty(NODES) != null) {
						for (Iterator itr = grafo.getNodes().iterator(); 
						itr.hasNext();) {
							if (progressDialog != null){
							progressDialog.report("Guardando nodos en la Base de datos ("
									+ i + " de " + Integer.toString((grafo.getNodes().size() + 1)) + ")." );
							}
							writeNode( (Node) itr.next());
							i ++;
						}
						// write edges if property set
						// if (getProperty(EDGES) != null) {
						i = 1;
						for (Iterator itr = grafo.getEdges().iterator(); itr.hasNext();) {
							if (progressDialog != null){
								progressDialog.report("Guardando edges en la Base de datos ("
										+ i + " de " + Integer.toString((grafo.getEdges().size() + 1)) + ")." );
							}
							writeEdge( (LocalGISDynamicEdge) itr.next());
							i++;
						}
					}
				}).start();
			}
		});
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	public void releaseResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchRemoveEdge(Statement arg0, Edge arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchRemoveNode(Statement arg0, Node arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchUpdateEdge(Statement st, Edge e) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchUpdateNode(Statement st, Node node)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchWriteEdge(Statement st, Edge edge)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addBatchWriteNode(Statement st, Node node)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected DataSource createDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createTables() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTables() throws SQLException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existTables() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String getQueryForCountingNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForReadingASectionOfGraphInBBox(BoundingBox bbox) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryForReadingASectionOfGraphRelatedToCoordinate(
			Point point, double tolerance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForReadingASectionOfGraphRelatedWithEdgeID(
			Integer edgeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryForReadingASectionOfGraphRelatedWithID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForReadingEdgesByDistance(Geometry point,double radius, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForReadingEntireGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryForReadingNetworkId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForReadingNodesByDistance(Geometry coord,double radius, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForRemovingSubGraph(Graph arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetworkName(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpTables() throws SQLException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getQueryForNearestPointToEdge(Point arg0, int arg1,
			String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForNearestPointToEdgeRatio(Point arg0, int arg1,
			String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getQueryForCountingEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Geometry getFeature(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}


}