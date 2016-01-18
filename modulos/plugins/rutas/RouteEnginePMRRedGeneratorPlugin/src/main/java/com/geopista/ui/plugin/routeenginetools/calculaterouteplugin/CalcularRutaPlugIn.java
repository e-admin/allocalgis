/**
 * CalcularRutaPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.uva.geotools.graph.path.Path;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.path.AStarShortestPathFinder;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.graph.structure.proxy.ProxyGeographicNode;
import org.uva.route.graph.traverse.standard.IDELabAStarIterator.AStarFunctions;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkLink;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.algorithms.XYNodesAStarFunctions;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.server.administradorCartografia.FilterLeaf;
import com.geopista.ui.cursortool.AbstractCursorTool;
import com.geopista.ui.plugin.routeenginetools.CombinedSchemaCalculator;
import com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigDialog;
import com.geopista.ui.plugin.routeenginetools.routeconfig.CalcRutaConfigFileReaderWriter;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.route.weighter.LocalGISStreetWeighter;
import com.localgis.route.weighter.LocalGISWeighter;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.IdEdgeNetworkBean;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.HTMLFrame;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * 
 * @author javier aragon
 * 
 */
public class CalcularRutaPlugIn extends AbstractPlugIn implements ActionListener,
ItemListener, ThreadedPlugIn {

	private boolean calculateRouteButtonAdded = false;	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static ArrayList<Point> puntosRuta = new ArrayList<Point>();

	MultiInputDialog dial;
	JTextField Coste, idb, ida;
	JCheckBox jnodos;
	Set<String> redesSet;
	Vector<String> subredSet;
	PlugInContext context;
	LocalGISWeighter weighter;
	Node source, end;
	int numNodosVisitados = 0;
	NetworkManager networkMgr;
	private JCheckBox generarReportLayer;
	private int templeado;


	private CalcRutaConfigFileReaderWriter configProperties = null;


	public boolean execute(PlugInContext context) throws Exception {


		if (context.getLayerViewPanel() == null){
			return false;
		}
		this.networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		configProperties =new CalcRutaConfigFileReaderWriter();
		if(configProperties.getRedesNames() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptyconfiguration"));
			return false;
		}
		redesSet = networkMgr.getNetworks().keySet();// obtener lista de redes ya
		subredSet = new Vector<String>();
		this.context = context;

		String[] redes = configProperties.getRedesNames();
		String subred = "";

		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());
		if(featuresselected.size() != 2){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.twoselectednodes"));
			return false;
		}
		Feature featureorigen = (Feature) featuresselected.get(0);
		Feature featuredestino = (Feature) featuresselected.get(1);
		int nodo_start = 0;
		int nodo_end = 0;
		String networkOrigen = "";
		String networkDestino = "";
		try{
			networkOrigen = featureorigen.getString("networkName");
			networkDestino = featuredestino.getString("networkName");
			nodo_start = featureorigen.getInteger(1);
			nodo_end = featuredestino.getInteger(1);
		}catch (Exception e){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.wronglayers"));
			return false;
		}
		Network networkStart = null;
		Network networkEnd = null;

		if(comprobarSeleccion(redes,networkOrigen) && comprobarSeleccion(redes,networkOrigen)){
			networkStart = networkMgr.getNetwork(networkOrigen);
			networkEnd = networkMgr.getNetwork(networkDestino);
		}else{
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.notconfignetwork"));
			return false;
		}


		source = ((DynamicGraph)networkStart.getGraph()).getNode(nodo_start);
		end = ((DynamicGraph)networkEnd.getGraph()).getNode(nodo_end);
		if (source == null || end == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.emptynodes"));
			return false;
		}
		new TaskMonitorManager().execute(this, context);

//		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
//		final PlugInContext runContext = this.context;
//		progressDialog.setTitle("TaskMonitorDialog.Wait");
//		progressDialog.report(I18N.get("calcruta","routeengine.calcruta.taskdialogmessage.calculate"));
//		progressDialog.addComponentListener(new ComponentAdapter()
//		{
//			public void componentShown(ComponentEvent e)
//			{   
//				new Thread(new Runnable()
//				{
//					public void run()
//					{
//						try
//						{   
//							run(progressDialog, runContext);
//						} 
//						catch (Exception e)
//						{
//							e.printStackTrace();
//						} 
//						finally
//						{
//							progressDialog.setVisible(false);
//						}
//					}
//				}).start();
//			}
//		});
//		GUIUtil.centreOnWindow(progressDialog);
//		progressDialog.setVisible(true);

		return false;

	}

	private boolean comprobarSeleccion(String[] redes, String networkOrigen) {
		for(int i = 0;i < redes.length ; i++){
			if(networkOrigen.trim().equals(redes[i].trim()))
				return true;
		}
		return false;
	}

	/**
	 * @param context
	 * @param subred
	 * @param Algoritmo
	 * @param path
	 * @param coste_ruta
	 */
	private void generarInformeRuta(PlugInContext context, 
			String Algoritmo, Path path, double coste_ruta) {
		context.getWorkbenchFrame().getOutputFrame().addText(
				I18N.get("calcruta","routeengine.calcularruta.reportmessage.titlelabel"));

		writeRoutePayhInformation(context.getWorkbenchFrame().getOutputFrame(), path.getEdges(), path);

	}

	private void writeRoutePayhInformation(HTMLFrame outputFrame, List<Edge> edges, Path p) {
		// TODO Auto-generated method stub
		int i = 0;
		String lastDescription = null;
		String actualDescription = "";
		Edge actualEdgeA = null;
		ILocalGISEdge actualEdge = null;
		ILocalGISEdge lastEdge = null;
		double longitud = 0;
		String subred = "";

		for(int m = edges.size()-1; m >=0 ; m--){

			actualEdgeA = edges.get(m);
			if (actualEdgeA instanceof ProxyEdge){
				actualEdgeA = (Edge) ((ProxyEdge)actualEdgeA).getGraphable();
			}
			if(actualEdgeA instanceof NetworkLink){
				UniqueIDGenerator idGen = new SequenceUIDGenerator();
				actualEdge = new LocalGISDynamicEdge(actualEdgeA.getNodeA(), actualEdgeA.getNodeB(), idGen);
				actualEdge.setIdLayer(-1);
				actualEdge.setIdFeature(actualEdgeA.getID());
				actualEdge.setEdgeLength(((ProxyGeographicNode)actualEdgeA.getNodeA()).getPosition().distance(((ProxyGeographicNode)actualEdgeA.getNodeB()).getPosition()));

				outputFrame.append("--- CAMBIO DE RED ---");

				lastDescription = null;
				actualDescription = "";
				actualEdgeA = null;
				actualEdge = null;
				lastEdge = null;
				longitud = 0;
				continue;
			}
			actualEdge = (ILocalGISEdge)actualEdgeA;

			Iterator<Network> nets = ((LocalGISNetworkManager)this.networkMgr).getAllNetworks().values().iterator();
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
					columnDescriptor = ((NetworkProperty) networkMgr.getNetwork(subred).getProperties().get("ColumnDescriptor")).getValue(Integer.toString(((ILocalGISEdge) actualEdge).getIdLayer()));

					if (columnDescriptor != null && !columnDescriptor.equals("")){
						actualDescription = getColumnsDescriptorFromIdLayer(((ILocalGISEdge) actualEdge).getIdLayer(), ((ILocalGISEdge) actualEdge).getIdFeature(), columnDescriptor);
					}
				} catch (Exception e) {
					// TODO: handle exception
					actualDescription = Integer.toString(((ILocalGISEdge) actualEdge).getIdFeature());
				}

				if (lastDescription == null){
					lastDescription = actualDescription;
				}

				if (actualDescription == null){
					actualDescription = "";
				}

				if ( (actualDescription != null && actualDescription.equals(lastDescription)) ){
					longitud = longitud + ((ILocalGISEdge) actualEdge).getEdgeLength() ;
				} 

				if (!actualDescription.equals(lastDescription) && longitud > 0){

					outputFrame.append("<ul>");
if (actualEdge instanceof ZebraDynamicEdge)
{
	outputFrame.append((i + 1 ) + " - Cruce la calle en el paso de cebra.");
	outputFrame.append(((ZebraDynamicEdge)actualEdge).getDescription());

}
else
{
					outputFrame.append((i + 1 ) + " - Siga la " + getTypeStreetDescriptor((ILocalGISEdge) actualEdge,subred) + " <b>" + lastDescription + "</b> ");
					outputFrame.addText("durante " 
							+ (int) longitud
							+ " metros.");
}
					int giro = (int) getAnguloEntreEdges(new ILocalGISEdge[]{(ILocalGISEdge) actualEdge,(ILocalGISEdge) lastEdge}, p);

					if (giro != 0){
						outputFrame.append("    Gire a la ");
						if (giro > 0){
							outputFrame.append("<b> izquierda. </b>");
						} else {
							outputFrame.append("<b> derecha. </b>");
						}
					}
					outputFrame.addText(" y tome la calle.");


					longitud = ((ILocalGISEdge) actualEdge).getEdgeLength();
					i++;

					if (actualEdge instanceof LocalGISStreetDynamicEdge){
						((LocalGISStreetDynamicEdge)actualEdge).getNodeA();
					}
					else {
						((LocalGISDynamicEdge)actualEdge).getNodeA();
					}

					outputFrame.append("</ul>");
				}

			}

			lastDescription = actualDescription;
			lastEdge = actualEdge;

		}

		if (actualDescription.equals(lastDescription)){
			outputFrame.append("<ul>");
			if (actualEdge instanceof ILocalGISEdge){
				outputFrame.append((i + 1 ) + " - Siga la "+ getTypeStreetDescriptor((ILocalGISEdge) actualEdge,subred) + " <b>" + lastDescription + "</b> ");
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

		outputFrame.append("<ul><b>Ha llegado a su destino</b> </ul><br>");
	}


	private int lastIdLayer = -1;
	private IGeopistaLayer layer = null;
	private boolean sqlthrowed = false;

	public double getAnguloEntreEdges(ILocalGISEdge[] edges, Path p){

		GeopistaFeature primerFeature = null;
		GeopistaFeature segundaFeature = null;
		double angulo = 0;

		for (int i = 0; i < edges.length; i++){

			if (lastIdLayer != ((ILocalGISEdge) edges[i]).getIdLayer()){

				layer = getOriginalLayer((Edge) edges[i]);
				if (layer != null){
					lastIdLayer = layer.getId_LayerDataBase();
				} else{
					lastIdLayer = ((ILocalGISEdge) edges[i]).getIdLayer();
				}

			}

			ArrayList<Object> originalFeatures = new ArrayList<Object>();

			if (layer != null){
				originalFeatures = new ArrayList<Object>(layer.getFeatureCollectionWrapper().
						getWrappee().getFeatures());
			}

			Iterator it = originalFeatures.iterator();

			Feature featureedges = null;
			GeopistaFeature f2 = null;
			while (it.hasNext()){
				f2 = (GeopistaFeature) it.next();
				if (Integer.parseInt(f2.getSystemId()) == ((ILocalGISEdge)edges[i]).getIdFeature()){
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

	private String getTypeStreetDescriptor(ILocalGISEdge edge, String subred){
		String typeColumnDescriptor = "";
		String typeDescription = "calle";
		if (edge instanceof LocalGISStreetDynamicEdge){
			try{
				typeColumnDescriptor = ((NetworkProperty) networkMgr.getNetwork((subred)).getProperties().
						get("TypeColumnDescriptor")).getValue(
								Integer.toString(edge.getIdLayer())
						);

				if (typeColumnDescriptor != null && !typeColumnDescriptor.equals("")){
					typeDescription = getColumnsDescriptorFromIdLayer(edge.getIdLayer(), edge.getIdFeature(), typeColumnDescriptor);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return typeDescription;
	}

	private String getRedNameFromSubRed(String subred) {
		// TODO Auto-generated method stub
		Iterator it = this.networkMgr.getNetworks().keySet().iterator();
		while(it.hasNext()){
			String redName = (String) it.next(); 
			Iterator it2 = this.networkMgr.getNetwork(redName).getSubnetworks().keySet().iterator();
			while(it2.hasNext()){
				if (this.networkMgr.getNetwork(redName).getSubNetwork((String) it2.next()) != null){
					return redName;
				}
			}
		}
		return null;
	}

	private String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column){

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
//			e.printStackTrace();
			this.sqlthrowed  = true;
			return "";
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return "";
	}

	private Set<String> getNetworksLoadedIntoLayreManager() {
		// TODO Auto-generated method stub

		return null;
	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.language.RouteEngine_CalcularRutai18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("calcruta",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);

	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))

		;

	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();// getSource recoge que objeto fue pulsado

	}

	public void itemStateChanged(ItemEvent e) {
		boolean isSelected;
		isSelected = (e.getStateChange() == ItemEvent.SELECTED);
		if (e.getItemSelectable() == jnodos) {
			if (isSelected == true) {

				ida.setEnabled(false);
				idb.setEnabled(false);

			} else {

				ida.setEnabled(true);
				idb.setEnabled(true);
			}
		}
	}

	public void dibujarCamino(String Algoritmo, Path p,
			double coste_ruta) {
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
	CombinedSchemaCalculator schemaCalculator = new CombinedSchemaCalculator();
		for (Iterator<Object> iter_edges = edges_camino.iterator(); iter_edges
		.hasNext();) {
			Edge edge_camino = (Edge) iter_edges.next();
			if(edge_camino instanceof ProxyEdge)
				edge_camino = (LocalGISStreetDynamicEdge)((ProxyEdge)edge_camino).getGraphable();
		edgesFeatureCol = schemaCalculator.getUpdatedFeatureSchema(edgesFeatureCol, edge_camino);

			if(edge_camino instanceof NetworkLink){
				//continue;
			}else if (lastIdLayer != ((ILocalGISEdge) edge_camino).getIdLayer()){

				layer = getOriginalLayer(edge_camino);
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
			while (it.hasNext()){
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

		Layer edgesLayer = context.addLayer(Algoritmo, 
				I18N.get("calcruta","routeengine.calcularruta.pathedgeslayername")
				+ p.getLast().getID() + "-" + p.getFirst().getID(),
				edgesFeatureCol);

		BasicStyle bs = new BasicStyle();
		bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
		bs.setLineWidth(4);
		bs.setEnabled(true);

		edgesLayer.addStyle(bs);



		Layer nodesLayer = context.addLayer(Algoritmo,
				I18N.get("calcruta","routeengine.calcularruta.pathnodeslayername")
				+ p.getLast().getID() + "-" + p.getFirst().getID(),
				nodesFeatureCol);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.setAttribute("nodeId");
		labelStyle.setColor(Color.black);
		labelStyle.setScaling(false);
		labelStyle.setEnabled(true);
		nodesLayer.addStyle(labelStyle);

	}// fin del metodo dibujarcaminos

	/**
	 * @param edge_camino
	 * @return
	 */
	private IGeopistaLayer getOriginalLayer(Edge edge_camino) {
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

	public void dibujarNodosVisitados(
			List<Graphable> nodosvisitados, String Algoritmo) {
		FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
		.createBlankFeatureCollection();
		nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",
				AttributeType.INTEGER);
		nodesFeatureCol.getFeatureSchema().addAttribute("contador",
				AttributeType.INTEGER);
		GeometryFactory fact = new GeometryFactory();

		for (int i = 0; i < nodosvisitados.size(); i++) {
			XYNode node = (XYNode) nodosvisitados.get(i);
			Coordinate coord = node.getCoordinate();
			Point geom_nodes = fact.createPoint(coord);
			Feature feature = new BasicFeature(nodesFeatureCol
					.getFeatureSchema());
			feature.setGeometry(geom_nodes);
			if (node == source || node == end)
				feature.setAttribute("nodeId", new Integer(node.getID()));
			feature.setAttribute("contador", new Integer(i));
			nodesFeatureCol.add(feature);
		}

		// Creo capas con los nodos
		Layer nodesLayer = context.addLayer(Algoritmo, I18N.get("calcruta","routeengine.calcularruta.visitednodeslayername")

				+ Algoritmo, nodesFeatureCol);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.setAttribute("nodeId");
		labelStyle.setColor(Color.black);
		labelStyle.setScaling(false);
		labelStyle.setEnabled(true);
		nodesLayer.addStyle(labelStyle);

	}


	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("calcruta","routeengine.calcularruta.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!calculateRouteButtonAdded)
		{
			toolbox.addToolBar();
			CalcularRutaPlugIn explode = new CalcularRutaPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			calculateRouteButtonAdded = true;
		}
	}

	public boolean anniadirBottonConfigurator(){

		try{
			JPanel jpanel = new JPanel(new GridLayout()); 
			EnableableToolBar toolbarilla = new EnableableToolBar();

			JButton configuratorButton = new JButton("Configurar");

			configuratorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onConfiguratorButtonDo();
				}
			});

			jpanel.add(new JLabel(),  
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 0, 0));

			jpanel.add(configuratorButton,  
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 0, 0));

			jpanel.setSize(200, 5);

			this.dial.addRow(jpanel);



		} catch (Exception e) {
			// TODO: handle exception
			// cualquier tipo de excepcion para pruebas, cambiar si corresponde
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private void onConfiguratorButtonDo() {
		// TODO Auto-generated method stub
		CalcRutaConfigDialog dialog = getConfigDialog();

		this.configProperties.loadPropertiesFromConfigFile();

		this.dial.setEnabled(true);
		this.dial.setVisible(true);

	}

	private CalcRutaConfigDialog getConfigDialog(){
		CalcRutaConfigDialog configDialog = new CalcRutaConfigDialog(this.context.getWorkbenchFrame(), this.networkMgr, this.dial);
		configDialog.pack();
		configDialog.setVisible(true);

		if (configDialog.finished()){
			return configDialog;
		}

		return null;
	}

	public String getQueryFromIdLayer(Connection con, int idLayer) {

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
			this.sqlthrowed = true;
			return "";
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return unformattedQuery;

	}

	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub
		monitor.report(I18N.get("calcruta","routeengine.calcruta.taskdialogmessage.infofactoring"));

		Path path = new Path();
		double coste_ruta = 0.0;
		List<Graphable> nodosvisitados = new ArrayList<Graphable>();
		ArrayList<IdEdgeNetworkBean> temporal = (ArrayList<IdEdgeNetworkBean>) aplicacion.getBlackboard().get("temporalincidents");
		if (configProperties.getTipoVehiculo().equals("En Coche")){
			if(temporal == null)
				weighter = new LocalGISStreetWeighter();
			else
				weighter = new LocalGISStreetWeighter(temporal,networkMgr);
		} else{
			if(temporal == null)
				weighter = new LocalGISWeighter();
			else
				weighter = new LocalGISWeighter(temporal,networkMgr);
		}

		AStarFunctions afunc=null;
		
		afunc = new XYNodesAStarFunctions((XYNode) end,new LocalGISWeighter());

//		afunc = new XYNodesAStarFunctions((XYNode) end,weighter);

		int tinicial = (int) System.currentTimeMillis();

		AStarShortestPathFinder pfAstar = new AStarShortestPathFinder(source, end, afunc, null, null);
		pfAstar.calculate();

		path = pfAstar.getPath();
		if(!path.isValid()){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("calcruta","routeengine.calcularruta.errormessage.pathnotfound"));
			return;
		}

		coste_ruta = pfAstar.getCost(end);
		int tfinal = (int) System.currentTimeMillis();
		templeado = tfinal - tinicial;
		nodosvisitados = pfAstar.getVisitedNodes();

		String Algoritmo =  I18N.get("calcruta","routeengine.calcularruta.astaralgorithm");

		numNodosVisitados = nodosvisitados.size();

		/*
		 * permite dibujar los nodos guardados en una coleccion Lo hemos
		 * utilizAdo para observar la diferencia entre los nodos que
		 * visita el algoritmo dijkstra y A*
		 */

		dibujarCamino(Algoritmo, path, coste_ruta);

		context.getWorkbenchFrame().getOutputFrame()
		.createNewDocument();
		context.getWorkbenchFrame().getOutputFrame().show();

		// texto en la ventana de salida
		generarInformeRuta(context, Algoritmo, path, coste_ruta);

		return;
	}

	public static UndoableCommand createAddCommand(Point point,AbstractCursorTool tool) {
		// TODO Auto-generated method stub
		final Point p = point;

		return new UndoableCommand(tool.getName()) {
			public void execute() {
				puntosRuta.add(p);
			}
			public void unexecute() {
				puntosRuta.remove(p);
			}
		};

	}

}
