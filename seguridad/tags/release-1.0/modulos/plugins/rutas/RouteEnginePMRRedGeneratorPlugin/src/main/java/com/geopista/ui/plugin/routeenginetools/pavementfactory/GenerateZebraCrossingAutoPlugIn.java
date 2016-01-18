package com.geopista.ui.plugin.routeenginetools.pavementfactory;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.graph.structure.impedance.EdgeImpedance;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.PMRSideWalkFactory;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.geopista.ui.plugin.wfs.CoordinateConversion;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

/**
 * Plugin para la generacion de pasos de cebra de forma automática
 * @author miriamperez
 *
 */

public class GenerateZebraCrossingAutoPlugIn extends AbstractPlugIn {

	private PlugInContext context = null;
	private boolean zebraButtonCrossingAdded = false;
	private String PASOS_PEATONES = "Paso_de_peatones";
	private String ACERAS = "aceraspmr";
	private static Logger LOGGER = Logger.getLogger(GenerateZebraCrossingAutoPlugIn.class);
	private static double distance = 20;
	private PMRSideWalkFactory fact;
	private LocalGISNetworkDAO nDAO;
	private Connection connection;
	private List edgesToRemove = new ArrayList();
	private List edgesToAppend = new ArrayList();
	private List nodesToAppend = new ArrayList();
	private DynamicGraph graph = null;
	private String sNetworkName = "";
	private String sTipoPaso = "";

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
		edgesToRemove = new ArrayList();
		edgesToAppend = new ArrayList();
		nodesToAppend = new ArrayList();
		this.context= context;
		this.fact = new PMRSideWalkFactory(context, true);
		this.nDAO = new LocalGISNetworkDAO();
		if (AppContext.getApplicationContext().isLogged()){
			this.connection = AppContext.getApplicationContext().getConnection();
		}
		GeneratePavementDialog dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context, true);
		if (!dialog.wasOKPressed()) return false;
		if (dialog.getNombreRedTextField().getText().equals("")) return false;
		sTipoPaso = dialog.getTipoPasos().getSelectedItem().toString();
		GeopistaLayer systemLayer = (GeopistaLayer)this.context.getLayerManager().getLayer("aceraspmr");
		if (systemLayer == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "CargarCapaAceras"));
			return false;
		}
		try{
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			DBRouteServerReaderWriter db = null;
			GeographicGraphGenerator graphGenerator = null;

			graphGenerator = new LocalGISStreetGraphGenerator();
			db = new LocalGISStreetRouteReaderWriter(connectionFactory, true);

			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);

			this.sNetworkName = dialog.getNombreRedTextField().getText();
			db.setNetworkName(sNetworkName);
			
			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
	        graph = new DynamicGraph(manager);
		}catch(Exception e){
			LOGGER.error(e);
		}
		if (graph == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred", "IntroducirNombreValido"));
			return false;
		}


		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        progressDialog.setTitle(I18N.get("genred", "GenerandoPasosCebra"));
        progressDialog.report(I18N.get("genred", "GenerandoPasosCebra"));
        progressDialog.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                    		insertZebraCrossing((DynamicGraph)graph, progressDialog);
                        }catch(Exception e)
                        {
                        }finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        return true;
    }

	/**
	 * Añado los pasos de peatones al grafo
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void insertZebraCrossing(DynamicGraph graph,TaskMonitorDialog taskMonitor){
		try{
			ArrayList globalEdgesToAppend = new ArrayList();
			ArrayList globalEdgesToRemove = new ArrayList();
			ArrayList globalNodesToAppend = new ArrayList();
			
			Iterator featuresIt = null;

			Collection featuresWithSelectedItems = this.context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
			int zebraFeatures = featuresWithSelectedItems.size();
			if (zebraFeatures == 0){
				JOptionPane.showMessageDialog(null, I18N.get("genred", "SeleccionarAlgunPunto"));
				return;
			}else{
				featuresIt = featuresWithSelectedItems.iterator();
			}
			Edge edgeTest = null;
	        SequenceUIDGenerator sequenceEdge = new SequenceUIDGenerator();
	        sequenceEdge.setSeq(nDAO.getNextDatabaseIdEdge(connection, AppContext.getIdEntidad()));
	        SequenceUIDGenerator sequenceNode = new SequenceUIDGenerator();
	        sequenceNode.setSeq(nDAO.getNextDatabaseIdNode(connection, AppContext.getIdEntidad()));
	        int srid = Integer.valueOf(context.getLayerManager().getCoordinateSystem().getEPSGCode());
	        int idMunicipio = AppContext.getIdMunicipio();
	        int done=0;
			while (featuresIt.hasNext()){
				GeopistaFeature feature = (GeopistaFeature)featuresIt.next();
				taskMonitor.report(++done, zebraFeatures, "Procesando muestras de campo.");
				System.out.println("Observacion: "+ feature.getAttribute("Observaciones"));
				Point point   = GeographicNodeUtil.createPoint(feature.getGeometry().getCoordinate(), fact.getCRS());
				List edgesList = graph.getEdgesNearTo(point, distance, 5);
				if (edgesList.size() < 2) 
					{
					LOGGER.warn("Zebra sample not related to any sideway."+ feature);
					continue;
					}
				PMRLocalGISStreetDynamicEdge localGISStreetDynamicEdgeA = (PMRLocalGISStreetDynamicEdge)edgesList.get(0);
				 
				
				if (localGISStreetDynamicEdgeA instanceof ZebraDynamicEdge)
				{
					LOGGER.warn("Zebra detected (Ignored) at this point."+point);
					continue;
				}

				PMRLocalGISStreetDynamicEdge localGISStreetDynamicEdgeB=null;
				for(int i=1; i<edgesList.size();i++)
				{
					if (!(edgesList.get(i) instanceof PMRLocalGISStreetDynamicEdge))
						continue;
					PMRLocalGISStreetDynamicEdge localGISStreetOther= (PMRLocalGISStreetDynamicEdge)edgesList.get(i);
					if (localGISStreetDynamicEdgeA.getRelatedToId()== localGISStreetOther.getRelatedToId())
					{
						localGISStreetDynamicEdgeB=localGISStreetOther; // found!
						break;
					}
				}
				
				if (localGISStreetDynamicEdgeB==null) // Two sidewalks from the same street was not found!
				{
					LOGGER.warn("Zebra near sidewalks of different streets. Two sidewalks from the same street was not found!");
					continue;
				}
/*				if (this.edgesToRemove.contains(edge)) {
					System.out.println("YA BORRADO");
					continue;
				}*/
				
				
				
					LineString edgeGeom1=(LineString)nDAO.getEdgeGeometry(localGISStreetDynamicEdgeA, srid, idMunicipio, connection);
					localGISStreetDynamicEdgeA.setGeom(edgeGeom1);
	
					Coordinate coordToZebra1 = coordToZebra(edgeGeom1, feature.getGeometry().getCoordinate());
					Node nodeToZebra1 = splitEdge (localGISStreetDynamicEdgeA, coordToZebra1, sequenceEdge,sequenceNode);
					localGISStreetDynamicEdgeA = localGISStreetDynamicEdgeB;
					if(nodeToZebra1==null) continue;
					
					LineString edgeGeom2=(LineString)nDAO.getEdgeGeometry(localGISStreetDynamicEdgeA, srid, idMunicipio, connection);
					localGISStreetDynamicEdgeA.setGeom(edgeGeom2);
					Coordinate coordToZebra2 = coordToZebra(edgeGeom2, feature.getGeometry().getCoordinate());
					Node nodeToZebra2 = splitEdge (localGISStreetDynamicEdgeA, coordToZebra2, sequenceEdge,sequenceNode);
					
					if(nodeToZebra2==null) continue;
					
					ZebraDynamicEdge zebraEdge = new ZebraDynamicEdge(sTipoPaso,nodeToZebra1, nodeToZebra2,sequenceEdge);
					zebraEdge.setGeom(calculateGeomForZebra(zebraEdge));
					zebraEdge.setRelatedToId(localGISStreetDynamicEdgeA.getRelatedToId());
					nodeToZebra1.add(zebraEdge);
					nodeToZebra2.add(zebraEdge);
					edgesToAppend.add(zebraEdge);
					edgeTest = zebraEdge;
					recreatePavement();
					globalEdgesToAppend.addAll(edgesToAppend);
					globalEdgesToRemove.addAll(edgesToRemove);
					globalNodesToAppend.addAll(nodesToAppend);
					((SpatialAllInMemoryExternalSourceMemoryManager)graph.getMemoryManager()).removeGraphableFromMemory(edgesToRemove);
					BasicGraph newBasicGraph = new BasicGraph(nodesToAppend, edgesToAppend);
					graph.getMemoryManager().appendGraphSilently(newBasicGraph);
					writeToDatabase(graph);
					if (taskMonitor.isCancelRequested())
						return;
					edgesToAppend.clear();
					edgesToRemove.clear();
					nodesToAppend.clear();
			}

			

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private Geometry calculateGeomForZebra(ZebraDynamicEdge zebraEdge) {
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)zebraEdge.getNodeA()).getCoordinate(),((XYNode)zebraEdge.getNodeB()).getCoordinate()});
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
		return (LineString) edge.getGeom();
	}


	private Node splitEdge(PMRLocalGISStreetDynamicEdge edge, Coordinate zebraCrossing, UniqueIDGenerator sequenceEdge, UniqueIDGenerator sequenceNode ){
		try{
			LineString origGeom = (LineString) edge.getGeom();
			Node cutPointNode = new DynamicGeographicNode(GeographicNodeUtil.createPoint(zebraCrossing,fact.getCRS()),sequenceNode );
			PMRLocalGISStreetDynamicEdge pmrEdge1 = new PMRLocalGISStreetDynamicEdge(edge.getRelatedTo(), edge.getSide(), edge.getNodeA(), cutPointNode,sequenceEdge);
			PMRLocalGISStreetDynamicEdge pmrEdge2 = new PMRLocalGISStreetDynamicEdge(edge.getRelatedTo(), edge.getSide(), cutPointNode, edge.getNodeB(),sequenceEdge);
			

			LineString linestring1 = cutLinestring(origGeom, origGeom.getCoordinateN(0), zebraCrossing);
			LineString linestring2 = cutLinestring(origGeom, origGeom.getCoordinateN(origGeom.getNumPoints()-1), zebraCrossing);

			double percent1=linestring1.getLength()/origGeom.getLength();
			
			pmrEdge1.setGeom(linestring1);
			pmrEdge2.setGeom(linestring2);
			
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

	private void writeToDatabase(DynamicGraph graph){
		DynamicGraph resultGraph = null;
		LocalGISRouteReaderWriter db = null;
//		this.getEdgesLayerModifications(graph, context, subred);

//		System.out.println("Tiempo en actualizar el grafo:" + (System.currentTimeMillis() - timeInit) );
		Connection conn = null;
		try {

			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			if (!this.edgesToAppend.isEmpty()){
				if (edgesToAppend.toArray()[0] instanceof LocalGISStreetDynamicEdge){
					db = new LocalGISStreetRouteReaderWriter(routeConnection, true);
				} else {
					db = new LocalGISRouteReaderWriter(routeConnection);
				}
			}


//			LocalGISStreetRouteReaderWriter streetDb = new LocalGISStreetRouteReaderWriter(routeConnection);

			db.setNetworkName(sNetworkName);
//			streetDb.setNetworkName(panel.getNombreenBase());
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn = connectionFactory.getConnection();
			if (graph != null) {
				//Primero borramos los edges
				for (Iterator<Edge> itr = this.edgesToRemove.iterator();
				itr.hasNext();) {
					Edge edge = itr.next();
					db.deleteEdge(edge);
				}

				for (Iterator<Node> itr = this.nodesToAppend.iterator();
				itr.hasNext();) {
					Node node = itr.next();
					db.writeNode(node ,conn);
				}

				Iterator<Edge> itEdge = edgesToAppend.iterator();
				List edgesListRebuilt = new ArrayList();
				while (itEdge.hasNext()){
					Edge e = itEdge.next();
					if (e instanceof LocalGISStreetDynamicEdge){
						db.writeEdge(e,conn);
						((LocalGISStreetRouteReaderWriter)db).writeStreetData(e, conn);
						edgesListRebuilt.add(e);
					}
				}
				edgesToAppend = edgesListRebuilt;

/*				SpatialAllInMemoryExternalSourceMemoryManager mman  = new SpatialAllInMemoryExternalSourceMemoryManager(db);
				mman.setGraph(graph);
				resultGraph = new DynamicGraph(mman);*/

				getFeaturesFromGraph(edgesToAppend, "APPEND");
				getFeaturesFromGraph(edgesToRemove, "REMOVE");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
			throw new RuntimeException(e);
		}finally{
			ConnectionUtilities.closeConnection(conn);
		}

	}

	private void rebuildNode(Node node,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		node.setID(nDAO.getNextDatabaseIdNode(connection));
	}
	private void rebuildEdge(Edge edge,Connection connection) throws SQLException {
		LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
		edge.setID(nDAO.getNextDatabaseIdEdge(connection));
	}

	private void recreatePavement(){

        List featuresList = new ArrayList();
		try{
			GeopistaLayer systemLayer = (GeopistaLayer)this.context.getLayerManager().getLayer("aceraspmr");
			int indexMunicipio = systemLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("id_municipio");
			int indexGeometry = systemLayer.getFeatureCollectionWrapper().getFeatureSchema().getAttributeIndex("GEOMETRY");

	        //Remove features
	        int n = this.edgesToRemove.size();
	        for (int i=0;i<n;i++){
	        	PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)this.edgesToRemove.get(i);
	        	LineString lineString = getLineStringForEdge(edge);
	        	Envelope envelope = new Envelope();
	        	envelope.expandToInclude(lineString.getEnvelopeInternal());
		        List featuresListToRemove = systemLayer.getFeatureCollectionWrapper().query(envelope);
		        Iterator featuresIterator = featuresListToRemove.iterator();
		        while (featuresIterator.hasNext()){
		        	GeopistaFeature feature = (GeopistaFeature)featuresIterator.next();
		        	if (Integer.valueOf(feature.getSystemId()) == edge.getIdFeature())
		        		{
		        		systemLayer.getFeatureCollectionWrapper().remove(feature);
		        		break;
		        		}
		        }
	        }
	        //Add features
			GeopistaFeature featureBase = new BasicFeature(systemLayer.getFeatureCollectionWrapper().getFeatureSchema());
	        n = this.edgesToAppend.size();
	        for (int i=0;i<n;i++){
				GeopistaFeature feature = (GeopistaFeature)featureBase.clone();
	        	PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)this.edgesToAppend.get(i);
	        	LineString lineString = getLineStringForEdge(edge);
				feature.setGeometry(lineString);
				feature.setNew(true);
				feature.setLayer(systemLayer);
				feature.setAttribute(indexMunicipio, AppContext.getIdMunicipio());
				feature.setAttribute(indexGeometry, lineString);
				featuresList.add(feature);
				edge.setIdLayer(feature.getLayer().getId_LayerDataBase());
	        }
	        systemLayer.getFeatureCollectionWrapper().addAll(featuresList);
		}catch(Exception e){
			LOGGER.error(e);
		}finally{
		}
		try{
			int i = 0;
			Iterator itEdges = this.edgesToAppend.iterator();
			while (itEdges.hasNext()){
				PMRLocalGISStreetDynamicEdge edge = (PMRLocalGISStreetDynamicEdge)itEdges.next();
				GeopistaFeature feature = (GeopistaFeature)featuresList.get(i);
				edge.setIdFeature(Integer.valueOf(feature.getSystemId()));
				i++;
			}
		}catch(Exception e){
			LOGGER.error(e);
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