/**
 * GenerateZebraCrossingAutoPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.pavementfactory;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graphable;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.network.Network;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.PMRSideWalkFactory;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * Plugin para la generacion de pasos de cebra de forma automï¿½tica
 * @author miriamperez
 *
 */

public class GenerateZebraCrossingAutoPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

	private static final String SEARCH_TOLERANCE_FIELDNAME = "Tolerancia de la búsqueda de aceras (unidades de mapa)";
	private PlugInContext context = null;
	private boolean zebraButtonCrossingAdded = false;
	private String PASOS_PEATONES = "Paso_de_peatones";
	private String ACERAS = "aceraspmr";
	private static Logger LOGGER = Logger.getLogger(GenerateZebraCrossingAutoPlugIn.class);
	private PMRSideWalkFactory fact;
	private LocalGISNetworkDAO nDAO;
	private Connection connection;
	private StringBuilder stBuilder = new StringBuilder();
	private DynamicGraph graph = null;
	private String sNetworkName = "";
	private String sTipoPaso = "";
	private GeneratePavementDialog dialog;

/*	public boolean execute(PlugInContext context) throws Exception {
		this.context= context;
		this.fact = new PMRSideWalkFactory(context);
		this.nDAO = new LocalGISNetworkDAO();
		if (AppContext.getApplicationContext().isLogged()){
			this.connection = AppContext.getApplicationContext().getConnection();
		}
		GeneratePavementDialog dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context);
		NetworkManager networkMgr = FuncionesAuxiliares.getNetworkManager(context);
		Network network = networkMgr.getNetwork(dialog.getNombreRedTextField().getText());
		if (network == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "DebeCargarRed"));
			return false;
		}
		DynamicGraph graph = (DynamicGraph)network.getGraph();
		insertZebraCrossing(graph);
		return false;
	}*/

	public boolean execute(PlugInContext context) throws Exception {
		
		this.context= context;
		this.fact = new PMRSideWalkFactory(context, true);
		this.nDAO = new LocalGISNetworkDAO();
		if (AppContext.getApplicationContext().isLogged()){
			this.connection = AppContext.getApplicationContext().getConnection();
		}
		dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context, getDescription(),true, false);
		dialog.addPositiveDoubleField(SEARCH_TOLERANCE_FIELDNAME, 5, 10);
		
		dialog.setVisible(true);
		if (!dialog.wasOKPressed()) return false;
//		if (dialog.getSelectedNetwork().equals("")) return false;
		sTipoPaso = dialog.getComboBox(GeneratePavementDialog.TIPO_PASO_CEBRA_FIELDNAME).getSelectedItem().toString();
		GeopistaLayer systemLayer = (GeopistaLayer)this.context.getLayerManager().getLayer("aceraspmr");
		if (systemLayer == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "CargarCapaAceras"));
			return false;
		}
		String subred=dialog.getSubredseleccionada();
		String red=dialog.getRedSeleccionada();
		Network net=NetworkModuleUtil.getSubNetwork(NetworkModuleUtilWorkbench.getNetworkManager(context),
								red, subred);
		this.graph=(DynamicGraph) net.getGraph();
//		try{
//			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
//			DBRouteServerReaderWriter db = null;
//		GraphGenerator graphGenerator = null;
//
//		graphGenerator = NetworkModuleUtil.getLocalGISStreetGraphGenerator();
//			db = new LocalGISStreetRouteReaderWriter(connectionFactory, true);
//
//			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
//
//			// OJO usa una de las partes de la seleccion de redes Esta seccion se eliminará previsiblemnte
//			this.sNetworkName = dialog.getRedSeleccionada();//tSelectedNetwork();
//			db.setNetworkName(sNetworkName);
//			
//			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
//	        graph = new DynamicGraph(manager);
//		}catch(Exception e){
//			LOGGER.error(e);
//		}
		if (graph == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "IntroducirNombreValido"));
			return false;
		}
		new TaskMonitorManager().execute(this, context);
//
//		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
//        progressDialog.setTitle(I18N.get("genred", "GenerandoPasosCebra"));
//        progressDialog.report(I18N.get("genred", "GenerandoPasosCebra"));
//        progressDialog.addComponentListener(new ComponentAdapter() {
//            public void componentShown(ComponentEvent e) {
//                new Thread(new Runnable(){
//                    public void run()
//                    {
//                        try
//                        {
//                    		insertZebraCrossing((DynamicGraph)graph, progressDialog);
//                        }catch(Exception e)
//                        {
//                        }finally
//                        {
//                            progressDialog.setVisible(false);
//                        }
//                    }
//                }).start();
//            }
//        });
//        GUIUtil.centreOnWindow(progressDialog);
//        progressDialog.setVisible(true);
        return true;
    }

	private String getDescription()
	{
	   return "Trocea y complementa las redes para insertar los pasos de zebra por proximidad a los puntos en los que se hayan marcado en la capa de datos de campo.";
	}

	public void run(TaskMonitor monitor, PlugInContext context)
	{
	    insertZebraCrossing(graph, monitor);
	}
	/**
	 * Aï¿½ado los pasos de peatones al grafo
	 * @param graph
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertZebraCrossing(DynamicGraph graph,TaskMonitor taskMonitor)
	{
		try{
//			ArrayList globalEdgesToAppend = new ArrayList();
//			ArrayList globalEdgesToRemove = new ArrayList();
//			ArrayList globalNodesToAppend = new ArrayList();
			
			stBuilder=new StringBuilder("=========== Ejecución GenerateZebraPlugIn: "+new Date()+"====================").append("\n\r");
			
			Collection featuresWithSelectedItems = this.context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
			int zebraFeatures = featuresWithSelectedItems.size();
			if (zebraFeatures == 0){
				JOptionPane.showMessageDialog(null, I18N.get("genred", "SeleccionarAlgunPunto"));
				return;
			}

		Iterator featuresIt = featuresWithSelectedItems.iterator();
			
		
		int idEntidad=AppContext.getIdEntidad();
		int idMunicipio = AppContext.getIdMunicipio();
// BUG JPC las secuencias no se deciden en la base de datos sino en el workbench
//	        SequenceUIDGenerator sequenceEdge = new SequenceUIDGenerator();
//	        sequenceEdge.setSeq(nDAO.getNextDatabaseIdEdge(connection, AppContext.getIdEntidad()));
//	        SequenceUIDGenerator sequenceNode = new SequenceUIDGenerator();
//	        sequenceNode.setSeq(nDAO.getNextDatabaseIdNode(connection, AppContext.getIdEntidad()));
		SequenceUIDGenerator sequenceEdge = NetworkModuleUtil.getUIDGenerator(graph, idEntidad);
		SequenceUIDGenerator sequenceNode = sequenceEdge;
	        int srid = Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode());
	        double tolerance=dialog.getDouble(SEARCH_TOLERANCE_FIELDNAME);
	        int done=0;
		while (featuresIt.hasNext()){
			GeopistaFeature feature = (GeopistaFeature)featuresIt.next();
			taskMonitor.report(++done, zebraFeatures, "Procesando muestras de campo: Pasos de cebra.");
			System.out.println("Observacion Paso de Cebra: "+ feature.getAttribute("Observaciones"));
			Point point   = GeographicNodeUtil.createPoint(feature.getGeometry().getCoordinate(), fact.getCRS());
			List edgesList = graph.getEdgesNearTo(point, tolerance, 15);
			if (edgesList.size() < 2) 
				{
				stBuilder.append("No se ha encontrado ninguna pareja de aceras cerca del paso de cebra:"+ feature);
				LOGGER.warn("Zebra sample not related to any sideway."+ feature);
				continue;
				}
			PMRLocalGISStreetDynamicEdge localGISStreetDynamicEdgeA = (PMRLocalGISStreetDynamicEdge)NodeUtils.equivalentTo((Graphable) edgesList.get(0));	 
				
				
// Busca un arco relacionado con el mismo eje de calle que el más cercano.
				PMRLocalGISStreetDynamicEdge localGISStreetDynamicEdgeB=null;
				boolean skipThis=false;
				// si hay un paso de cebra entre los más próximos evitamos procesar tramos ya cortados.
				for(int i=0; i<edgesList.size();i++)
					{
					    Edge edge= (Edge) NodeUtils.equivalentTo((Graphable) edgesList.get(i));
					    if (edge instanceof ZebraDynamicEdge)
						{
						    skipThis=true;
						    break;
						}
					}
				if (skipThis)
				    {
				 // ya hay un paso de cebra en este punto
				stBuilder.append( "Hay otro paso de zebra a menos de "+tolerance+" unidades del punto "+point.getDirectPosition()+". Se omite esta medida de paso de cebra:"+ feature);
				LOGGER.warn("Zebra detected (Ignored) nearer than "+tolerance+" units from this point."+point.getDirectPosition());	
				// TODO hacer informe de elemento saltado.
				    continue;
				    }
			// localiza el tramo pareja de la primera acera.	
				for(int i=1; i<edgesList.size();i++)
				{
				    Edge edge= (Edge) NodeUtils.equivalentTo((Graphable) edgesList.get(i));
					if (!(edge instanceof PMRLocalGISStreetDynamicEdge))
						continue;
					PMRLocalGISStreetDynamicEdge localGISStreetOther= (PMRLocalGISStreetDynamicEdge)edge;
					
					if ( localGISStreetDynamicEdgeA.getRelatedToId()== localGISStreetOther.getRelatedToId())
					{
						localGISStreetDynamicEdgeB=localGISStreetOther; // found!
						break;
					}
				}
				
				if (localGISStreetDynamicEdgeB==null) // Two sidewalks from the same street was not found!
				{
				    stBuilder.append( "Se han encontrado aceras de distintas calles para el paso de cebra:"+ feature);
					LOGGER.warn("Zebra near sidewalks of different streets. Two sidewalks from the same street was not found!");
					continue;
				}
				 List edgesToRemove = new ArrayList();
				 List edgesToAppend = new ArrayList();
				 List nodesToAppend = new ArrayList();
				 
				LineString edgeGeom1=(LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(localGISStreetDynamicEdgeA, context);
//NO hace falta		localGISStreetDynamicEdgeA.setGeometry(edgeGeom1);
				Coordinate coordToZebra1 = coordToZebra(edgeGeom1, feature.getGeometry().getCoordinate());
				// divide el arco localGISStreetDynamicEdgeA por el punto próximo a la coordenada
				Node nodeToZebra1 = splitEdge (localGISStreetDynamicEdgeA, coordToZebra1, sequenceEdge,sequenceNode,
								edgesToRemove,edgesToAppend,nodesToAppend);
				
				//localGISStreetDynamicEdgeA = localGISStreetDynamicEdgeB;
				// No ha podido dividir el eje
				if(nodeToZebra1==null) 
				    continue;
					
				LineString edgeGeom2=(LineString)NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(localGISStreetDynamicEdgeB,context);
//					localGISStreetDynamicEdgeA.setGeometry(edgeGeom2);
				Coordinate coordToZebra2 = coordToZebra(edgeGeom2, feature.getGeometry().getCoordinate());
				// divide el arco localGISStreetDynamicEdgeB por el punto próximo a la coordenada
				Node nodeToZebra2 = splitEdge (localGISStreetDynamicEdgeB, coordToZebra2, sequenceEdge,sequenceNode,
								edgesToRemove,edgesToAppend,nodesToAppend);
					
				if(nodeToZebra2==null) 
				    continue;
				// Suponemos que los elementos pertecen a la misma red networkId
				ZebraDynamicEdge zebraEdge = new ZebraDynamicEdge(sTipoPaso,nodeToZebra1, nodeToZebra2,localGISStreetDynamicEdgeA.getIdNetworkNodeA(),localGISStreetDynamicEdgeB.getIdNetworkNodeA(),sequenceEdge,null);
				
				zebraEdge.setGeometry(calculateGeomForZebra(zebraEdge));
				zebraEdge.setImpedanceBidirecccional(zebraEdge.getGeometry().getLength());
				zebraEdge.setRelatedToId(localGISStreetDynamicEdgeA.getRelatedToId());
				nodeToZebra1.add(zebraEdge);
				nodeToZebra2.add(zebraEdge);
				edgesToAppend.add(zebraEdge);
				nodesToAppend.add(nodeToZebra1);
				nodesToAppend.add(nodeToZebra2);
				
				recreatePavement( edgesToRemove, edgesToAppend, nodesToAppend);
				
//				globalEdgesToAppend.addAll(edgesToAppend);
//				globalEdgesToRemove.addAll(edgesToRemove);
//				globalNodesToAppend.addAll(nodesToAppend);
				
				// JPC aÃ±adir al grafo antes de permitir cancelar
				for (Object object : edgesToRemove)
				    {
					graph.getMemoryManager().removeGraphable((Graphable) object);
				    }
				graph.getMemoryManager().appendGraph(new BasicGraph(nodesToAppend, edgesToAppend));
				   
				
//				((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).removeGraphableFromMemory(edgesToRemove);
//					BasicGraph newBasicGraph = new BasicGraph(nodesToAppend, edgesToAppend);
//					graph.getMemoryManager().appendGraphSilently(newBasicGraph);
//					writeToDatabase(graph);
					if (taskMonitor.isCancelRequested())
						return;
			}

			

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private Geometry calculateGeomForZebra(ZebraDynamicEdge zebraEdge) {
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		LineString lineString = factory.createLineString(
			new Coordinate[]
				{((XYNode)zebraEdge.getNodeA()).getCoordinate(),
				((XYNode)zebraEdge.getNodeB()).getCoordinate()});
		return lineString;
	}

	private Coordinate coordToZebra (LineString lineString, Coordinate zebraCrossing){
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		DistanceOp distanceOp = new DistanceOp(factory.createPoint(zebraCrossing), lineString);
		Coordinate[] coordinateArray = distanceOp.nearestPoints();
		if (coordinateArray[0] == zebraCrossing)
			return coordinateArray[1];
		else
			return coordinateArray[0];
	}

	private LineString getLineStringForEdge(PMRLocalGISStreetDynamicEdge edge) {
/*		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)edge.getNodeA()).getCoordinate(),((XYNode)edge.getNodeB()).getCoordinate()});
		return lineString;*/
		return (LineString) edge.getGeometry();
	}

/**
 * divide el arco localGISStreetDynamicEdgeA por el punto próximo a la coordenada
 * @param edge
 * @param zebraCrossing
 * @param sequenceEdge
 * @param sequenceNode
 * @return
 */
	private Node splitEdge(PMRLocalGISStreetDynamicEdge edge, Coordinate zebraCrossing, UniqueIDGenerator sequenceEdge, 
				UniqueIDGenerator sequenceNode , List edgesToRemove,List edgesToAppend,List nodesToAppend)
	{
		try{
			LineString origGeom = (LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
			Node cutPointNode = new DynamicGeographicNode(GeographicNodeUtil.createPoint(zebraCrossing,fact.getCRS()),sequenceNode );
			PMRLocalGISStreetDynamicEdge pmrEdge1 = new PMRLocalGISStreetDynamicEdge(edge.getRelatedTo(),edge.getIdNetworkNodeA(),edge.getIdNetworkNodeB(), edge.getSide(), edge.getNodeA(), cutPointNode,sequenceEdge,null);
			PMRLocalGISStreetDynamicEdge pmrEdge2 = new PMRLocalGISStreetDynamicEdge(edge.getRelatedTo(),edge.getIdNetworkNodeA(),edge.getIdNetworkNodeB(), edge.getSide(), cutPointNode, edge.getNodeB(),sequenceEdge,null);
			

			LineString linestring1 = cutLinestring(origGeom, origGeom.getCoordinateN(0), zebraCrossing);
			LineString linestring2 = cutLinestring(origGeom, origGeom.getCoordinateN(origGeom.getNumPoints()-1), zebraCrossing);

			double percent1=linestring1.getLength()/origGeom.getLength();
			
			pmrEdge1.setGeometry(linestring1);
			pmrEdge2.setGeometry(linestring2);
			
			pmrEdge1 = copyPropertiesToEdge(edge,pmrEdge1, percent1);
			pmrEdge2 = copyPropertiesToEdge(edge,pmrEdge2, 1-percent1);
			
			edgesToRemove.add(edge);
			edgesToAppend.add(pmrEdge1);
			edgesToAppend.add(pmrEdge2);
			nodesToAppend.add(cutPointNode);
			cutPointNode.add(pmrEdge1);
			cutPointNode.add(pmrEdge2);
			Node oldNode = pmrEdge1.getOtherNode(cutPointNode);
			oldNode.remove(edge);
			oldNode.add(pmrEdge1);
			oldNode = pmrEdge2.getOtherNode(cutPointNode);
			oldNode.remove(edge);
			oldNode.add(pmrEdge2);
			return cutPointNode;
		}catch(Exception e){
			LOGGER.error(e);
			return null;
		}
	}
	/**
	 *
	 * @param ln1
	 * @param fixed punto de uno de los extremos a mantener
	 * @param cutCoord punto de corte
	 * @return
	 */
	protected static LineString cutLinestring(LineString ln1, Coordinate fixed, Coordinate cutCoord)
	{
		LineString newLineString1;
		LengthIndexedLine li1=new LengthIndexedLine(ln1);
		double cutIndex=li1.indexOf(cutCoord);
		double fixedIndex1=li1.indexOf(fixed);
		// correct the originally expected corner
		if (fixedIndex1==li1.getStartIndex()) // start of linestring
		{
			newLineString1=(LineString) li1.extractLine(li1.getStartIndex(), cutIndex);
		}
		else
		{
			newLineString1=(LineString) li1.extractLine(cutIndex, li1.getEndIndex());
		}
		return newLineString1;
	}
	private PMRLocalGISStreetDynamicEdge copyPropertiesToEdge(PMRLocalGISStreetDynamicEdge oldEdge,PMRLocalGISStreetDynamicEdge newEdge, double percent)
	{
		newEdge.setIdFeature(oldEdge.getIdFeature());
		newEdge.setIdLayer(oldEdge.getIdLayer());
		newEdge.setTransversalSlope(oldEdge.getTransversalSlope());
		newEdge.setWidth(oldEdge.getWidth());
		newEdge.setObstacleHeight(oldEdge.getObstacleHeight());
		newEdge.setRelatedToId(oldEdge.getRelatedToId());
		
		EdgeImpedance impedanceAB = oldEdge.getImpedance(oldEdge.getNodeA());
		EdgeImpedance impedanceBA = oldEdge.getImpedance(oldEdge.getNodeB());
		if (oldEdge.getNodeA().equals(newEdge.getNodeA()))
		{
			newEdge.setImpedanceAToB(impedanceAB.clone(percent));
			newEdge.setImpedanceBToA(impedanceBA.clone(percent));
		}
		else
		{
			newEdge.setImpedanceAToB(impedanceBA.clone(percent));
			newEdge.setImpedanceBToA(impedanceAB.clone(percent));
		}	
		
		
		return newEdge;
	}


	public Icon getIcon() {
        return IconLoader.icon("cebraAuto.gif");
    }

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!zebraButtonCrossingAdded  )
		{
			GenerateZebraCrossingAutoPlugIn explode = new GenerateZebraCrossingAutoPlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			zebraButtonCrossingAdded = true;
		}
	}

//	private void writeToDatabase(DynamicGraph graph){
//		DynamicGraph resultGraph = null;
//		LocalGISRouteReaderWriter db = null;
////		this.getEdgesLayerModifications(graph, context, subred);
//
////		System.out.println("Tiempo en actualizar el grafo:" + (System.currentTimeMillis() - timeInit) );
//		Connection conn = null;
//		try {
//
//			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
//			if (!this.edgesToAppend.isEmpty()){
//				if (edgesToAppend.toArray()[0] instanceof LocalGISStreetDynamicEdge){
//					db = new LocalGISStreetRouteReaderWriter(routeConnection, true);
//				} else {
//					db = new LocalGISRouteReaderWriter(routeConnection);
//				}
//			}
//
//
////			LocalGISStreetRouteReaderWriter streetDb = new LocalGISStreetRouteReaderWriter(routeConnection);
//
//			db.setNetworkName(sNetworkName);
////			streetDb.setNetworkName(panel.getNombreenBase());
//			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
//			conn = connectionFactory.getConnection();
//			if (graph != null) {
//				//Primero borramos los edges
//				for (Iterator<Edge> itr = this.edgesToRemove.iterator();
//				itr.hasNext();) {
//					Edge edge = itr.next();
//					db.deleteEdge(edge);
//				}
//
//				for (Iterator<Node> itr = this.nodesToAppend.iterator();
//				itr.hasNext();) {
//					Node node = itr.next();
//					db.writeNode(node ,conn);
//				}
//
//				Iterator<Edge> itEdge = edgesToAppend.iterator();
//				List edgesListRebuilt = new ArrayList();
//				while (itEdge.hasNext()){
//					Edge e = itEdge.next();
//					if (e instanceof LocalGISStreetDynamicEdge){
//						db.writeEdge(e,conn);
//						((LocalGISStreetRouteReaderWriter)db).writeStreetData(e, conn);
//						edgesListRebuilt.add(e);
//					}
//				}
//				edgesToAppend = edgesListRebuilt;
//
///*				SpatialAllInMemoryExternalSourceMemoryManager mman  = new SpatialAllInMemoryExternalSourceMemoryManager(db);
//				mman.setGraph(graph);
//				resultGraph = new DynamicGraph(mman);*/
//
//				getFeaturesFromGraph(edgesToAppend, "APPEND");
//				getFeaturesFromGraph(edgesToRemove, "REMOVE");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			LOGGER.error(e);
//			throw new RuntimeException(e);
//		}finally{
//			ConnectionUtilities.closeConnection(conn);
//		}
//
//	}

	private void rebuildNode(Node node,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		node.setID(nDAO.getNextDatabaseIdNode(connection));
	}
	private void rebuildEdge(Edge edge,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		edge.setID(nDAO.getNextDatabaseIdEdge(connection));
	}

	/**
	 * Intenta actualizar la capa de sistema de representación visual del grafo.
	 * @param edgesToRemove
	 * @param edgesToAppend
	 * @param nodesToAppend
	 * @throws ElementNotFoundException 
	 */
	private void recreatePavement(List<Edge> edgesToRemove,List edgesToAppend,List nodesToAppend) throws ElementNotFoundException
	{

        List featuresList = new ArrayList();
		
		GeopistaLayer systemLayer = (GeopistaLayer) context.getLayerManager().getLayer("aceraspmr");// NetworkModuleUtil.loadSystemLayerInWorkbench("aceraspmr",context);
		int indexMunicipio = systemLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("id_municipio");
		int indexGeometry = systemLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("GEOMETRY");
		
	        //Remove features
	      for(Edge edge:edgesToRemove)
		  {
		      Feature featureToRemove = NetworkModuleUtilWorkbench.findFeatureForEdge(edge, context);
		      systemLayer.getFeatureCollectionWrapper().remove(featureToRemove);
//	        	Geometry lineString = NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
//	        	Envelope envelope = new Envelope();
//	        	envelope.expandToInclude(lineString.getEnvelopeInternal());
//		        List featuresListToRemove = systemLayer.getFeatureCollectionWrapper().query(envelope);
//		        Iterator featuresIterator = featuresListToRemove.iterator();
//		        while (featuresIterator.hasNext()){
//		        	GeopistaFeature feature = (GeopistaFeature)featuresIterator.next();
//		        	if (Integer.valueOf(feature.getSystemId()) == edge.getIdFeature())
//		        		{
//		        		systemLayer.getFeatureCollectionWrapper().remove(feature);
//		        		break;
//		        		}
//		        }
	        }
	        //Add features
		GeopistaFeature featureBase = new BasicFeature(systemLayer.getFeatureCollectionWrapper().getFeatureSchema());
	        int n = edgesToAppend.size();
	        for (int i=0;i<n;i++){
			GeopistaFeature feature = (GeopistaFeature)featureBase.clone();
	        	PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)edgesToAppend.get(i);
	        	Geometry lineString = NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
			feature.setGeometry(lineString);
			feature.setNew(true);
			feature.setLayer(systemLayer);
			feature.setAttribute(indexMunicipio, AppContext.getIdMunicipio());
			feature.setAttribute(indexGeometry, lineString);
			featuresList.add(feature);
			edge.setIdLayer(feature.getLayer().getId_LayerDataBase());
	        }
	        // AÃ±ade elementos a la capa y deja que el workbench los actualice
	        systemLayer.getFeatureCollectionWrapper().addAll(featuresList);
		
	        // Asigna los identificadores de feature a los elementos del grafo
		
			int i = 0;
			Iterator itEdges = edgesToAppend.iterator();
			while (itEdges.hasNext()){
				PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)itEdges.next();
				GeopistaFeature feature = (GeopistaFeature)featuresList.get(i);
				edge.setIdFeature(Integer.valueOf(feature.getSystemId()));
				i++;
			}
		
	}

	/**
	 * Actualizo las features del grafo
	 * @param edgesList
	 * @return
	 */
	private void getFeaturesFromGraph(List edgesList, String sOperation)
	{
		Iterator edgesIterator = edgesList.iterator();
		List featuresToReturn = new ArrayList();
//		Iterator layersIt = context.getLayerManager().getLayers().iterator();
//		while (layersIt.hasNext()){
//			graphLayer = (GeopistaLayer)layersIt.next();
//			if (graphLayer.getName().startsWith(sNetworkName))
//				break;
//		}
		// Nombres de los atributos de una capa que no sea LocalGIS.
		String attributeWidth = "width";
		String attributeTransSlope ="transversal_slope";
		String attributeLongSlope = "longitudinal_slope";
		String attributeObstacleHeight = "obstacle_height";
		GeopistaLayer graphLayer = (GeopistaLayer)this.context.getLayerManager().getLayer(this.ACERAS);
		FeatureSchema schema = graphLayer.getFeatureCollectionWrapper().getFeatureSchema();
		if (schema instanceof GeopistaSchema)
			{
			 GeopistaSchema featureSchema =  (GeopistaSchema) schema;
			 attributeWidth = featureSchema.getAttributeByColumn("width");
			 attributeTransSlope = featureSchema.getAttributeByColumn("transversal_slope");
			 attributeLongSlope = featureSchema.getAttributeByColumn("longitudinal_slope");
			 attributeObstacleHeight = featureSchema.getAttributeByColumn("obstacle_height");
			
				if (attributeWidth==null ||attributeTransSlope==null|| attributeLongSlope==null || attributeObstacleHeight==null)
				{
					throw new IllegalArgumentException("Incompatible Layer. Need attributes:"+attributeWidth+","+attributeTransSlope+","+attributeLongSlope+","+attributeObstacleHeight);
				}
			}
		if (sOperation.equals("REMOVE")){
			while (edgesIterator.hasNext()){
				PMRLocalGISStreetDynamicEdge e = (PMRLocalGISStreetDynamicEdge)edgesIterator.next();
				Envelope env = this.getLineStringForEdge((PMRLocalGISStreetDynamicEdge)e).getEnvelopeInternal();
				Iterator featuresList = graphLayer.getFeatureCollectionWrapper().queryIterator(env);
				while (featuresList.hasNext()){
					GeopistaFeature feature = (GeopistaFeature)featuresList.next();
					if (Integer.valueOf(feature.getAttribute("id").toString()) == ((PMRLocalGISStreetDynamicEdge)e).getID()){
						featuresToReturn.add(feature);
						break;
					}
				}
			}
		}else{
			while (edgesIterator.hasNext()){
				PMRLocalGISStreetDynamicEdge e = (PMRLocalGISStreetDynamicEdge)edgesIterator.next();
				
				GeopistaFeature feature = new BasicFeature(schema);
				

				feature.setAttribute(attributeWidth, ((PMRLocalGISStreetDynamicEdge) e).getWidth());
				feature.setAttribute(attributeTransSlope, ((PMRLocalGISStreetDynamicEdge) e).getTransversalSlope());
				feature.setAttribute(attributeLongSlope, ((PMRLocalGISStreetDynamicEdge) e).getLongitudinalSlope());
//				feature.setAttribute("ladoAcera", ((PMRLocalGISStreetDynamicEdge) e).getsEdgeType());		
				feature.setAttribute(attributeObstacleHeight, ((PMRLocalGISStreetDynamicEdge) e).getObstacleHeight());
				feature.setGeometry(this.getLineStringForEdge((PMRLocalGISStreetDynamicEdge)e));
				feature.setLayer(graphLayer);
				featuresToReturn.add(feature);
			}

		}
		if (sOperation.equals("APPEND"))
			graphLayer.getFeatureCollectionWrapper().addAll(featuresToReturn);
		else
			graphLayer.getFeatureCollectionWrapper().removeAll(featuresToReturn);
	}
}