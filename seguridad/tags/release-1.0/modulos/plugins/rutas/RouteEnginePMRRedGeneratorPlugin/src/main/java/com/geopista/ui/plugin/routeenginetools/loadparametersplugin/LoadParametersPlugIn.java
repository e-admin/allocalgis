package com.geopista.ui.plugin.routeenginetools.loadparametersplugin;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.opengis.geometry.primitive.Point;
import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.genredplugin.PMRGenerarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.GeneratePavementDialog;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.PMRSideWalkFactory;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import es.uva.idelab.route.algorithm.SidewalkEdge;


/**
 * Plugin para la carga de parametros de las aceras
 * @author miriamperez
 *
 */

public class LoadParametersPlugIn extends PMRGenerarRedPlugIn {

	private boolean loadParButtonAdded = false;
	private PlugInContext context = null;
	private NetworkManager networkMgr;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private double distance = 10;
    private String networkName;
    private PMRSideWalkFactory fact = null;
    private Connection connection = null;
	private LocalGISNetworkDAO dao =  new LocalGISNetworkDAO();
	private static Logger LOGGER = Logger.getLogger(LoadParametersPlugIn.class);
	private static String PARAMETROS = "Tramos_calle";
	private static String NUMEROS_POLICIA = "numeros_policia";
	private static String BARRERAS = "Barreras_urbanisticas";
	private static String OBSTACULOS_ALTURA = "Obstaculos_en_altura_paso";
	private List calculatedEdgesList = new ArrayList();
	private DynamicGraph graph;
	private StringBuilder stBuilder = new StringBuilder();
	private String retornoDeCarro = System.getProperty("line.separator");

	public boolean execute(PlugInContext context) throws Exception {
		calculatedEdgesList = new ArrayList();
		if(context.getLayerViewPanel() == null)
			return false;
		if (AppContext.getApplicationContext().isLogged())
			this.connection = AppContext.getApplicationContext().getConnection();
		this.context = context;
		this.fact = new PMRSideWalkFactory(context, true);
		if (context.getLayerViewPanel().getLayerManager().getLayer(NUMEROS_POLICIA) == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred","CargarCapaPolicia"), "", JOptionPane.ERROR_MESSAGE); 
			return false;
		}
		if (context.getLayerViewPanel().getLayerManager().getLayer(PARAMETROS) == null){
			JOptionPane.showMessageDialog(null, I18N.get("genred","CargarCapaParametros"), "", JOptionPane.ERROR_MESSAGE); 
			return false;
		}
		
		GeneratePavementDialog dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context, false);
		if (!dialog.wasOKPressed()) return false;
		if (dialog.getNombreRedTextField().getText().equals("")) return false;
		this.networkName = dialog.getNombreRedTextField().getText();

		try{
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			DBRouteServerReaderWriter db = null;
			GeographicGraphGenerator graphGenerator = null;
	
			graphGenerator = new LocalGISStreetGraphGenerator();
			db = new LocalGISStreetRouteReaderWriter(connectionFactory, true);
	
			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
	
			db.setNetworkName(networkName);
	
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
		final PlugInContext runContext = this.context;
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.report(I18N.get("genred","AddCaracteristicas"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{   
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{   
							setMeasures(runContext,graph);
							setObstacles(runContext,graph,OBSTACULOS_ALTURA);
							setObstacles(runContext,graph,BARRERAS);
							writeToDatabase(graph, calculatedEdgesList);  
							try{
								String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);    
								File file = new File (localPath+"/cargaMediciones.txt");
								if (file.exists())
									file.delete();
								FileUtil.setContents(file.getAbsolutePath(), stBuilder.toString());
							}catch(Exception e){
								LOGGER.error("Error al escribir en el fichero de logs ",e);
								e.printStackTrace();
							}
						} 
						catch (Exception e)
						{
							e.printStackTrace();
						} 
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
		
		return false;
	}


	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {

	}

	

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
		if (AppContext.getApplicationContext().isLogged()){
			this.connection = AppContext.getApplicationContext().getConnection();
		}
	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(
				checkFactory
				.createWindowWithLayerManagerMustBeActiveCheck())
				.add(
						checkFactory
						.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
						.add(checkFactory.createTaskWindowMustBeActiveCheck()).add(
								checkFactory.createAtLeastNLayersMustExistCheck(1));
	}


	public ImageIcon getIcon() {
		return IconLoader.icon("loadAutoParameters.gif");
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!loadParButtonAdded  )
		{
			LoadParametersPlugIn explode = new LoadParametersPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			loadParButtonAdded = true;
		}
	}
	
	
	/**
	 * Devuelve una línea desde el extremo de la acera al número de policía. Esto se hace para asegurar que se cogen portales del mismo lado
	 */
	private LineString getLineFromNumToPavement(Coordinate nodePavement, Coordinate numCoordinate){
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		DistanceOp distanceOp = new DistanceOp(factory.createPoint(nodePavement), factory.createPoint(numCoordinate));
		Coordinate[] coordinateArray = distanceOp.nearestPoints();		
		LineString lineString = factory.createLineString(coordinateArray);
		return lineString;
	}
	
	
	private Coordinate getCenter(GeopistaFeature feature) {
		LineString lineString = (LineString)feature.getGeometry();
    	Coordinate coord1 = lineString.getCoordinates()[0];
    	Coordinate coord2 = lineString.getCoordinates()[1];
    	return new Coordinate(Math.abs((coord1.x+coord2.x)/2),Math.abs((coord1.y+coord2.y)/2));
	}

	/**
	 * Asigna las medidas adecuadas a las aceras
	 * @param context
	 * @param network
	 */
	private void setMeasures(PlugInContext context,DynamicGraph graph){
		GeopistaLayer portalesPar = (GeopistaLayer)context.getLayerViewPanel().getLayerManager().getLayer(PARAMETROS);
		Iterator featureIt = portalesPar.getFeatureCollectionWrapper().getFeatures().iterator();

		while (featureIt.hasNext()){

			GeopistaFeature feature = (GeopistaFeature)featureIt.next();
			if (!(feature.getGeometry() instanceof LineString)) continue;
			Coordinate point = getCenter(feature);
			Point measurePoint = GeographicNodeUtil.createPoint(point, fact.getCRS());
			List edgesList = graph.getEdgesNearTo(measurePoint, 100, 2);
			if (edgesList.size()< 2){
				stBuilder.append("La feature "+feature.getSystemId()+" que tiene medidas de aceras, no encuentra un eje cercano"+retornoDeCarro);
				continue;
			}
			Edge edge = (Edge)edgesList.get(0);
			//Calculo el lado (izq o derecho) que corresponde a cada edge
			
			/*PMRLocalGISStreetDynamicEdge edge0 =  (PMRLocalGISStreetDynamicEdge)edgesList.get(0);
			PMRLocalGISStreetDynamicEdge edge1 =  (PMRLocalGISStreetDynamicEdge)edgesList.get(1);
			edge0.getIdLayer();
			int idFeature0 = edge0.getIdFeature();
			GeopistaLayer tramoslayer = (GeopistaLayer) context.getLayerManager().getLayer("tramos_via_pmr");
			
			List featuresTramos = tramoslayer.getFeatureCollectionWrapper().getFeatures();
			
			Iterator featureTramosIterator = featuresTramos.iterator();
			
			while (featureTramosIterator.hasNext())
			{
				GeopistaFeature actualFeature = (GeopistaFeature) featureTramosIterator.next();
				if(Integer.valueOf(actualFeature.getSystemId())==idFeature0)
				{
					System.out.println("featureencontrada: "+actualFeature.getSystemId());
				}
			}*/
			
			
			
			
			
			//long t1 = System.currentTimeMillis();
			calculateSides((PMRLocalGISStreetDynamicEdge)edgesList.get(0),(PMRLocalGISStreetDynamicEdge)edgesList.get(1));
			
			//long t2 = System.currentTimeMillis();
			//System.out.println("t2-t1: "+(t2-t1));
			
			copyProperties((PMRLocalGISStreetDynamicEdge)edgesList.get(0), feature);
			//long t3 = System.currentTimeMillis();
			//System.out.println("t3-t2: "+(t3-t2));
			
			copyProperties((PMRLocalGISStreetDynamicEdge)edgesList.get(1), feature);
			
			//long t4 = System.currentTimeMillis();
			//System.out.println("t4-t3: "+(t4-t3));
		}
	}
		
	/**
	 * Almacena los datos de obstáculos y barreras
	 * @param context
	 * @param network
	 */
	private void setObstacles(PlugInContext context,DynamicGraph graph, String sLayer){
		GeopistaLayer layer = (GeopistaLayer)context.getLayerViewPanel().getLayerManager().getLayer(sLayer);
		Iterator featureIt = layer.getFeatureCollectionWrapper().getFeatures().iterator();
		while (featureIt.hasNext()){
			GeopistaFeature feature = (GeopistaFeature)featureIt.next();
			Point measurePoint = GeographicNodeUtil.createPoint(feature.getGeometry().getCoordinate(), fact.getCRS());
			List edgesList = graph.getEdgesNearTo(measurePoint, distance, 1);
			if (edgesList.size()< 1) continue;
			PMRLocalGISStreetDynamicEdge edge1 = (PMRLocalGISStreetDynamicEdge)edgesList.get(0);
			if  (sLayer.equals(OBSTACULOS_ALTURA)){
				int alturaIndex = feature.getSchema().getAttributeIndex("Altura");
				Object altura = feature.getAttribute(alturaIndex);
				if (altura != null && altura.toString().equals("")){
					edge1.setObstacleHeight(formatParameter(altura,""));
					replaceEdgeProperties(calculatedEdgesList, edge1,OBSTACULOS_ALTURA);
				}
			}else{
				edge1.setWidth(0);
				replaceEdgeProperties(calculatedEdgesList, edge1,BARRERAS);
			}
		}
	}
	
	private void replaceEdgeProperties(List edgesList, PMRLocalGISStreetDynamicEdge edge, String sTipo  ){
		int index = edgesList.indexOf(edge);
		if (index<0)
			edgesList.add(edge);
		else{
			PMRLocalGISStreetDynamicEdge newEdge = (PMRLocalGISStreetDynamicEdge)edgesList.get(index);
			if (sTipo.equals(OBSTACULOS_ALTURA)){
				newEdge.setObstacleHeight(edge.getObstacleHeight());
			}else{
				newEdge.setWidth(0);
			}
			edgesList.set(index, newEdge);
		}
	}
		
	private void copyProperties(PMRLocalGISStreetDynamicEdge edge, GeopistaFeature featureParametros1){	
		//Inserto las mediciones en cada lado de la acera
		int anchoD = featureParametros1.getSchema().getAttributeIndex("Ancho_minimo_lado_derecho");
		int anchoI = featureParametros1.getSchema().getAttributeIndex("Ancho_minimo_lado_izquierdo");
		int pendienteTransD = featureParametros1.getSchema().getAttributeIndex("Pend_trans_mayor_2_por_ciento_lado_derecho");
		int pendienteTransI = featureParametros1.getSchema().getAttributeIndex("Pend_trans_mayor_2_por_ciento_lado_izquierdo");
		int pendienteLongD = featureParametros1.getSchema().getAttributeIndex("Pend_long_mayor_6_por_ciento_lado_derecho");
		int pendienteLongI = featureParametros1.getSchema().getAttributeIndex("Pend_long_mayor_6_por_ciento_lado_izquierdo");
		Object sAnchoI = featureParametros1.getAttribute(anchoI);
		Object sAnchoD = featureParametros1.getAttribute(anchoD);
		Object sPendienteTransD = featureParametros1.getAttribute(pendienteTransD);
		Object sPendienteTransI = featureParametros1.getAttribute(pendienteTransI);
		Object sPendienteLongD = featureParametros1.getAttribute(pendienteLongD);
		Object sPendienteLongI = featureParametros1.getAttribute(pendienteLongI);
		if (((PMRLocalGISStreetDynamicEdge)edge).getCalculatedSide() == SidewalkEdge.RIGHT ){
			((PMRLocalGISStreetDynamicEdge)edge).setWidth(formatParameter(sAnchoD,"width"));
			((PMRLocalGISStreetDynamicEdge)edge).setTransversalSlope(formatParameter(sPendienteTransD,"TransSlope"));
			((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(formatParameter(sPendienteLongD,"LongSlope"));
		}else{
			((PMRLocalGISStreetDynamicEdge)edge).setWidth(formatParameter(sAnchoI,"width"));
			((PMRLocalGISStreetDynamicEdge)edge).setTransversalSlope(formatParameter(sPendienteTransI,"TransSlope"));
			((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(formatParameter(sPendienteLongI,"LongSlope"));
		}
		if (!calculatedEdgesList.contains(edge))
			calculatedEdgesList.add(edge);

	}

	/**
	 * Determino los números de policía más cercanos a un edge
	 */
	
	private int getNearestNum(PMRLocalGISStreetDynamicEdge edge, PMRLocalGISStreetDynamicEdge otherEdge, String extremo){
		List numbersList = new ArrayList();
		List newNumbersList = new ArrayList();
		int numPolicia = -1; 
		try{
			int srid = context.getLayerManager().getCoordinateSystem().getEPSGCode();
			numbersList = dao.getNumbersList(edge.getID(), srid, extremo, connection);
			Iterator numbersIterator = numbersList.iterator();
			LineString line1 = this.getLineStringForEdge(edge);
			LineString line2 = this.getLineStringForEdge(otherEdge);
			while (numbersIterator.hasNext()){
				Object[] objectArray = (Object[])numbersIterator.next();
				Coordinate coordinate = new Coordinate((Double)objectArray[1],(Double)objectArray[2]);
				LineString lineToNum = null;
				if (extremo.equals("A"))
					lineToNum = this.getLineFromNumToPavement(((XYNode)edge.getNodeA()).getCoordinate(), coordinate);
				else
					lineToNum = this.getLineFromNumToPavement(((XYNode)edge.getNodeB()).getCoordinate(), coordinate);
				numPolicia = this.getRotulo((String)objectArray[3]);
				if (!lineToNum.intersects(line2)){
					com.vividsolutions.jts.geom.Point portal = aplicacion.getGeometryFactory().createPoint(coordinate);
					if(line1.distance(portal) < line2.distance(portal))
						if (numPolicia > -1)
							return numPolicia;
				}
			}
			if (numPolicia == -1) return -1;
		}catch(Exception e){
			LOGGER.error(e);
		}
		return -1;
	}
	
	private LineString getLineStringForEdge(PMRLocalGISStreetDynamicEdge edge) {
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)edge.getNodeA()).getCoordinate(),((XYNode)edge.getNodeB()).getCoordinate()});
		return lineString;
	}
	
	
	/**
	 * Obtiene el rotulo de portal de una feature de la capa numeros de policia
	 * @param feature
	 * @return
	 */
	private Integer getRotulo(String rotulo){
		String numPortal = "";
		int n = rotulo.length();
		for (int i=0;i<n;i++){
			if (rotulo.charAt(i)>='0' && rotulo.charAt(i)<='9')
				numPortal += rotulo.charAt(i);
		}
		if (numPortal.equals(""))
			return -1;
		else
			return Integer.parseInt(numPortal);
	}

	/**
	 * Formatea un atributo de la medicion y lo convierte a formato numerico
	 * @param parameter
	 * @return
	 */
	private Double formatParameter(Object parameter, String sTipo){

/*		if (sTipo.equals("LongSlope")){
			if (parameter.equals(""))
				return new Double(0);
			else
				return new Double(6);
		}else if (sTipo.equals("TransSlope")){
			if (parameter.equals(""))
				return new Double(0);
			else
				return new Double(2);
		}*/

		if (parameter == null || parameter.equals(""))
			return new Double(100);
		try{
			Double valor = Double.parseDouble(((String)parameter).replaceAll(",", ".")); 
			if (sTipo.equals("width")){
				return valor*100;
			}else{
				return valor;
			}
		}catch(Exception e){
			return new Double(-1);
		}
	}

	private void writeToDatabase(DynamicGraph graph, List edgesList){
		DynamicGraph resultGraph = null;
		LocalGISRouteReaderWriter db = null;
		Connection conn = null;
		try {

			RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
			if (!edgesList.isEmpty()){
				db = new LocalGISStreetRouteReaderWriter(routeConnection, true);
			}
			db.setNetworkName(this.networkName);
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			conn = connectionFactory.getConnection();
/*			GeopistaLayer graphLayer = null;
			Iterator layersIt = context.getLayerManager().getLayers().iterator();
			List featuresToAdd = new ArrayList();
			while (layersIt.hasNext()){
				graphLayer = (GeopistaLayer)layersIt.next();
				if (graphLayer.getName().startsWith(this.networkName)){
					break;
				}
			}*/
			if (graph != null) {
				Iterator<Edge> itEdge = edgesList.iterator();
				while (itEdge.hasNext()){
					Edge e = itEdge.next();
					if (e instanceof PMRLocalGISStreetDynamicEdge){
						dao.updateStreetData(this.networkName,(LocalGISStreetDynamicEdge)e, conn);
						
						//Actualizo las features del grafo
/*						Envelope env = this.getLineStringForEdge((PMRLocalGISStreetDynamicEdge)e).getEnvelopeInternal();
						Iterator featuresList = graphLayer.getFeatureCollectionWrapper().queryIterator(env);
						while (featuresList.hasNext()){
							GeopistaFeature feature = (GeopistaFeature)featuresList.next();
							
							if (Integer.valueOf(feature.getAttribute("id").toString()) == ((PMRLocalGISStreetDynamicEdge)e).getID()){
								feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) e).getWidth());
								feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) e).getTransversalSlope());
								feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) e).getLongitudinalSlope());
								feature.setAttribute("ladoAcera", ((PMRLocalGISStreetDynamicEdge) e).getsEdgeType());
								feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) e).getObstacleHeight());
								featuresToAdd.add(feature);
								break;
							}	
						}*/
					}
				}
/*				graphLayer.getFeatureCollectionWrapper().addAll(featuresToAdd);
				SpatialAllInMemoryExternalSourceMemoryManager mman  = new SpatialAllInMemoryExternalSourceMemoryManager(db);
				mman.setGraph(graph);
				resultGraph = new DynamicGraph(mman);*/
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}finally{
			ConnectionUtilities.closeConnection(conn);
		}
	}
	
	private void calculateSides(PMRLocalGISStreetDynamicEdge edge1, PMRLocalGISStreetDynamicEdge edge2){
		//Numero de portal para el nodoA
		int portal1 = getNearestNum(edge1, edge2,"A");
		//Numero de portal para el nodoB
		int portal2 = getNearestNum(edge1, edge2,"B");
		
		if (((isImpar(portal1) && !isImpar(portal2)) || (!isImpar(portal1) && isImpar(portal2))) || portal1 == portal2){
			//Numero de portal para el nodoA
			portal1 = getNearestNum(edge2, edge1,"A");
			//Numero de portal para el nodoB
			portal2 = getNearestNum(edge2, edge1,"B");
		}
		if (((isImpar(portal1) && !isImpar(portal2)) || (!isImpar(portal1) && isImpar(portal2))) || portal1 == portal2){
			stBuilder.append("No se puede determinar la acera izquierda y derecha de acuerdo a los portales del eje "+edge1.getID()+retornoDeCarro);
			return;
		}
			
		if (portal1 < portal2){
			if (isImpar(portal1)){ //impar
				edge1.setCalculatedSide(SidewalkEdge.LEFT);
				edge2.setCalculatedSide(SidewalkEdge.RIGHT);
			}else{
				edge2.setCalculatedSide(SidewalkEdge.LEFT);
				edge1.setCalculatedSide(SidewalkEdge.RIGHT);
			}
		}else if (portal1 > portal2){
			if (!isImpar(portal1)){ //par
				edge2.setCalculatedSide(SidewalkEdge.LEFT);
				edge1.setCalculatedSide(SidewalkEdge.RIGHT);
			}else{
				edge1.setCalculatedSide(SidewalkEdge.LEFT);
				edge2.setCalculatedSide(SidewalkEdge.RIGHT);
			}	
		}else{
			stBuilder.append("No se puede determinar la acera izquierda y derecha en función de los portales del eje "+edge1.getID()+retornoDeCarro);
		}
	}
	
	private boolean isImpar(int num){
		if (((num/2)*2) == num)
			return false;
		else
			return true;
	}
}