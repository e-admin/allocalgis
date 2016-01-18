/**
 * LoadParametersPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.loadparametersplugin;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.genredplugin.PMRGenerarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.pavementfactory.GeneratePavementDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISRouteReaderWriter;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import edu.emory.mathcs.backport.java.util.Arrays;
import es.uva.idelab.route.algorithm.SidewalkEdge;


/**
 * Plugin para la carga de parametros de las aceras
 * @author miriamperez
 *
 */

public class LoadParametersPlugIn extends PMRGenerarRedPlugIn {

	public static final String LAYER_PARAMETERS_TOLOAD_FIELDNAME = "Capa con los parametros a cargar";
	private static final String NUMEROS_POLICIA_FIELDNAME = "Capa con los portales";
	private static final String TOLERANCIA_FIELDNAME = "Tolerancia de busqueda";
	private boolean loadParButtonAdded = false;
	private PlugInContext context = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
//    private double distance = 10;
    private String networkName;
//    private PMRSideWalkFactory fact = null;
    private Connection connection = null;
	private LocalGISNetworkDAO dao =  new LocalGISNetworkDAO();
	private static Logger LOGGER = Logger.getLogger(LoadParametersPlugIn.class);
//	private static String PARAMETROS = "Tramos_calle";
	private static String NUMEROS_POLICIA = "numeros_policia";
//	private static String BARRERAS = "Barreras_urbanisticas";
//	private static String OBSTACULOS_ALTURA = "Obstaculos_en_altura_paso";
//	private List calculatedEdgesList = new ArrayList();
	private DynamicGraph graph;
	private StringBuilder stBuilder = new StringBuilder();
	private String retornoDeCarro = System.getProperty("line.separator");
	private GeneratePavementDialog dialog;
	private Layer layerNumPolicia;
	private GeopistaLayer layerParams;

	public boolean execute(PlugInContext context)
	{
//		calculatedEdgesList = new ArrayList();
		if(context.getLayerViewPanel() == null)
			{
			    context.getWorkbenchGuiComponent().warnUser("No hay un mapa abierto en el escritorio.");
			    return false;
			}
// BUG Es necesario el login?? mejor hacerlo independiente dle estado 
//		if (AppContext.getApplicationContext().isLogged())
//			this.connection = AppContext.getApplicationContext().getConnection();
		this.context = context;
// Factory de aceras para ????
//		this.fact = new PMRSideWalkFactory(context, true);
	
//                layerNumPolicia = context.getLayerViewPanel().getLayerManager().getLayer(NUMEROS_POLICIA);
//		if (layerNumPolicia == null)
//		    {
//			JOptionPane.showMessageDialog(null, I18N.get("genred","CargarCapaPolicia"), "", JOptionPane.ERROR_MESSAGE); 
//			return false;
//		    }
//		if (context.getLayerViewPanel().getLayerManager().getLayer(PARAMETROS) == null){
//			JOptionPane.showMessageDialog(null, I18N.get("genred","CargarCapaParametros"), "", JOptionPane.ERROR_MESSAGE); 
//			return false;
//		}
		layerNumPolicia = context.getLayerManager().getLayer(NUMEROS_POLICIA);
		if (layerNumPolicia == null)
		    {
			JOptionPane.showMessageDialog(null, I18N.get("genred","CargarCapaPolicia"), "", JOptionPane.WARNING_MESSAGE);
			//return false; //Permite continuar con otras capas no "oficiales"
		    }
		
		dialog = new GeneratePavementDialog(context.getWorkbenchFrame(), "", context, getDescription(),false,false);
		dialog.setSideBarImage(getIcon());
		String toolTipTextParametros = "Capa con los parámetros tomados en campo sobre las características de las aceras.";
		dialog.addLabel(toolTipTextParametros);
		dialog.addLayerComboBox(LAYER_PARAMETERS_TOLOAD_FIELDNAME, null, toolTipTextParametros, context.getLayerManager());
		
		Layer numPolLayer=context.getLayerManager().getLayer(NUMEROS_POLICIA);
		String toolTipTextCapaPortales = "Capa con los portales oficiales que se usará para obtener la orientación de las aceras mediadas en campo.";
		dialog.addLabel(toolTipTextCapaPortales);
		dialog.addLayerComboBox(NUMEROS_POLICIA_FIELDNAME, numPolLayer, toolTipTextCapaPortales, context.getLayerManager());
		
		dialog.addLabel("Distancia para buscar proximidades (EN UNIDADES DEL MAPA)");
		dialog.addNonNegativeDoubleField(TOLERANCIA_FIELDNAME, 20, 8);

		dialog.setVisible(true);		
		if (!dialog.wasOKPressed()) 
		    return false;
		

// OJO usa una de las partes de la seleccion de redes Esta seccion se eliminará previsiblemnte

		if (dialog.getRedSeleccionada().equals(""))
		    {
			context.getWorkbenchGuiComponent().warnUser("No ha seleccionado ninguna red para cargar datos en ella.");
			return false;
		    }
		
		 this.networkName = dialog.getRedSeleccionada();
		 layerParams = (GeopistaLayer) dialog.getLayer(this.LAYER_PARAMETERS_TOLOAD_FIELDNAME);
//		 paramsIndexed = new IndexedFeatureCollection(layerParams.getFeatureCollectionWrapper());
//		 numPolIndexed = new IndexedFeatureCollection(layerNumPolicia.getFeatureCollectionWrapper());

/**
 * Intenta cargar red de base de datos en este momento.
 * JPC Cambiado para seleccionar de memoria.
 */
//	try{
//		RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
//		DBRouteServerReaderWriter db = null;
//		GraphGenerator graphGenerator = null;
//	
//		graphGenerator = NetworkModuleUtil.getLocalGISStreetGraphGenerator();
//		db = new LocalGISStreetRouteReaderWriter(connectionFactory, true);
//		db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
//		db.setNetworkName(networkName);
//		SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
//	        graph = new DynamicGraph(manager);
//		}catch(Exception e){
//			LOGGER.error(e);
//		}
//		
//		if (graph == null){
//			JOptionPane.showMessageDialog(null, I18N.get("genred", "IntroducirNombreValido"));
//			return false;
//		}
		NetworkManager networkMgr=NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network net=NetworkModuleUtil.getSubNetwork(networkMgr, this.networkName,null);
		
		final PlugInContext runContext = this.context;
	
		
//	    }
//	else{
//	    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
//		progressDialog.setTitle("TaskMonitorDialog.Wait");
//		progressDialog.report(I18N.get("genred","AddCaracteristicas"));
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
//							withTaskMonitorDo(progressDialog, runContext);
//						}
//						catch(Exception e)
//						{
//						    
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
//	}
		new TaskMonitorManager().execute(this,this.context);
		return false;
	}


	private String getDescription()
	{
	   return "Traspasa información disponible en la capa de parámtros a los arcos de la red indicada por proximidad." +
	   		" Identifica el lado de la calle según lo números de portal de la capa de \""+NUMEROS_POLICIA+"\"";
	}


	public void run(TaskMonitor monitor, PlugInContext runContext)
	{
	 try
	 {
	    context.getOutputFrame().createNewDocument();
	    String netName=dialog.getRedSeleccionada();
	    String subnetName=dialog.getSubredseleccionada();
	    NetworkManager netMgr = NetworkModuleUtilWorkbench.getNetworkManager(runContext);
	    Network net=NetworkModuleUtil.getSubNetwork(netMgr, netName, subnetName);
	    graph=(DynamicGraph) net.getGraph();
	    
	    
	    	List<Edge> processed= setMeasures(runContext,graph,monitor);
//		setObstacles(runContext,graph,OBSTACULOS_ALTURA,monitor);
//		setObstacles(runContext,graph,BARRERAS,monitor);
		//writeToDatabase(graph, calculatedEdgesList,monitor);
	    	monitor.report("Almacenando los cambios en el grafo.");
	    	
	    	/**
	    	 * Informe de elementos no procesados
	    	 */
	    	Collection edges= graph.getEdges();
	    	List totalEdges=new ArrayList(edges); // copia a una nueva colección porque geEdges devuelve la interna y no se debe modificar
	    	boolean re = totalEdges.removeAll(processed);
	    	int counter=1;
	    	for(Object edgeObj:totalEdges)
	    	    {
	    		Edge edge = (Edge)edgeObj;
	    		String logMsg=Integer.toString(counter++)+")-No procesado Arco"+edge;
	    		stBuilder.append(logMsg+retornoDeCarro);
//			context.getOutputFrame().append("<p>"+logMsg+"</p>");
	    	    }
	    
		NetworkModuleUtil.logActivityToFile(stBuilder);
//		try
//		    {
			// Permite al Localgis memory manager informar de las operaciones de almacenamiento
			LocalGISAllinMemoryManager lgMemMgr = NetworkModuleUtil.castToLocalGISAllinMemoryManager(graph);
			if (lgMemMgr!=null)
			    {
				lgMemMgr.setTaskMonitor(monitor);
			    }
			//graph.commit();
//		    } catch (IOException e1)
//		    {
//			throw new RuntimeException(e1);
//		    }
		    
	 }
	 finally
	 {
//		context.getOutputFrame().append("<pre>"+stBuilder.toString()+"</pre>");
//	     context.getOutputFrame().surface();
	 }

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
//			LoadParametersPlugIn explode = new LoadParametersPlugIn();                 
			toolbox.addPlugIn(this, null, this.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			loadParButtonAdded = true;
		}
	}
	
	
	/**
	 * Devuelve una lï¿½nea desde el extremo de la acera al nï¿½mero de policï¿½a. Esto se hace para asegurar que se cogen portales del mismo lado
	 */
	private LineString getLineFromNumToPavement(Coordinate nodePavement, Coordinate numCoordinate){
		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
		DistanceOp distanceOp = new DistanceOp(factory.createPoint(nodePavement), factory.createPoint(numCoordinate));
		Coordinate[] coordinateArray = distanceOp.nearestPoints();		
		LineString lineString = factory.createLineString(coordinateArray);
		return lineString;
	}
	
	private Coordinate getCenter(GeopistaFeature feature) {
	    Geometry geom=feature.getGeometry();
	    com.vividsolutions.jts.geom.Point point = geom.getInteriorPoint();
	    return point.getCoordinate();
//		LineString lineString = (LineString)feature.getGeometry();
//    	Coordinate coord1 = lineString.getCoordinates()[0];
//    	Coordinate coord2 = lineString.getCoordinates()[1];
//    	return new Coordinate(Math.abs((coord1.x+coord2.x)/2),Math.abs((coord1.y+coord2.y)/2));
	}

	
	/**
	 * 
	 * Asigna las medidas adecuadas a las aceras
	 * @param context
	 * @param graph
	 * @param monitor
	 * @return lista de edge procesados
	 */
	private List<Edge> setMeasures(PlugInContext context,DynamicGraph graph, TaskMonitor monitor)
	{
	    List<Edge> processedEdges=new ArrayList<Edge>();
		GeopistaLayer paramsLyr = (GeopistaLayer)dialog.getLayer(LAYER_PARAMETERS_TOLOAD_FIELDNAME);
		this.layerParams=paramsLyr;
		Iterator featureIt = paramsLyr.getFeatureCollectionWrapper().getFeatures().iterator();
		LocalGISAllinMemoryManager memMgr = NetworkModuleUtil.castToLocalGISAllinMemoryManager(graph);
		double tolerance=dialog.getDouble(TOLERANCIA_FIELDNAME);
		int i=0;
		int total=paramsLyr.getFeatureCollectionWrapper().size();
		while (featureIt.hasNext())
		    {
			i++;
			monitor.report(i, total, "Analizando feature de parámetros.");
			if (monitor.isCancelRequested())
			    {
				return processedEdges;
			    }
			GeopistaFeature feature = (GeopistaFeature)featureIt.next();
		try{
//			if (feature.getID()==1612) // Debug code TODO retirar
//			    {
//				feature.getID();
//			    }
//			else
//			    {continue;}
			Geometry measureGeom=feature.getGeometry();	
			// para evitar las esquinas utilizo un punto interior
			if (measureGeom instanceof LineString)
			    {
				LineString ls=((LineString)measureGeom);
				int numPoints = ls.getNumPoints();
				if (numPoints==2)
				    {
					measureGeom=ls.getCentroid();
				    }
				else
				    {
					measureGeom=ls.getPointN(numPoints/2); // un punto intermedio cualquiera lejos de la esquina
				    }
			    }
			else
			    {
				measureGeom=measureGeom.getCentroid();
			    }
			memMgr.setContext(context);
	
		List<Edge> edgesList = memMgr.getEdgesNearTo(measureGeom, tolerance,10);
		
			
			if (edgesList.size()==0)
				{
				String logMsg = "La feature "+feature.getSystemId()+"FID:"+feature.getID()+" que tiene medidas de aceras, no encuentra ningún eje cercanos."+retornoDeCarro;
//				context.getOutputFrame().append("<p>"+logMsg+"</p>");
				stBuilder.append(logMsg);
				continue;
				}		
	
	// Elimino los arcos que hagan esquina con el más proximo para evitar casos de digitalizaciónd e segmentos cortos
	
	
	Map<Edge,Integer> sides=Collections.emptyMap();
	Edge edge0 = edgesList.get(0);
	Edge edge1=null;
	boolean touched1=false;
	boolean touched0=false;
	
	//////////////////////Pasar a método  removeCorneredEdges()
	// TODO hacer la eliminación de esquinas una opción para que este plugin sea más utilizable en otros casos.
	List<Edge> arcosSinEsquinas=new ArrayList<Edge>();
	arcosSinEsquinas.add(edgesList.get(0));
	Node node0A=edge0.getNodeA();
	Node node0B=edge0.getNodeB();
	for(int in=1;in<edgesList.size();in++)
	    {
		Edge ed=edgesList.get(in);
		if (ed.getOtherNode(node0A)==null && ed.getOtherNode(node0B)==null)
		    {
			arcosSinEsquinas.add(ed);
		    }
		else
		    {
			String logMsg = "El Arco "+ed.getID()+" se ha descartado de las cercanías de la feature "+feature.getSystemId()+"FID:"+feature.getID()+" por formar esquina con el candidato más próximo: "+edge0+retornoDeCarro;
			stBuilder.append(logMsg);
		    }
	    }
	
	////////////////////////////
	edgesList=arcosSinEsquinas;
	edgesList.removeAll(processedEdges); // eliminar los ya procesados
	if (edgesList.size()==0)
		{
		String logMsg = "La feature "+feature.getSystemId()+"FID:"+feature.getID()+" no encuentra ningún eje cercanos que no haya sido procesado previamente o que forme una esquina próxima."+retornoDeCarro;
//		context.getOutputFrame().append("<p>"+logMsg+"</p>");
		stBuilder.append(logMsg);
		continue;
		}
//			La conexión se comparte via campo de clase
			try
			    {
				this.connection = AppContext.getApplicationContext().getConnection();
			    } catch (SQLException e)
			    {
				throw new RuntimeException(e);
			    }
			//Calculo el lado (izq o derecho) que corresponde a cada edge
			//long t1 = System.currentTimeMillis();
			
	if (edgesList.size()>1)
	    {
		
			edge1 = (PMRLocalGISStreetDynamicEdge)edgesList.get(1);
			String logMsg = "El Arco "+edge1.getID()+" se ha procesado como pareja de "+edge0.getID()+"para la feature "+feature.getSystemId()+"FID:"+feature.getID()+retornoDeCarro;
			stBuilder.append(logMsg);
			sides= calculateSides(edge0, edge1);
			touched1=copyProperties(sides,(ILocalGISEdge) edge1, feature,monitor);
			processedEdges.add(edge1);
	    }	
	String logMsg = "El Arco "+edge0.getID()+" se ha procesado para la feature "+feature.getSystemId()+"FID:"+feature.getID()+retornoDeCarro;
	stBuilder.append(logMsg);		
		touched0=copyProperties(sides, (ILocalGISEdge) edge0, feature, monitor);
		processedEdges.add(edge0);
		
		try
		{
		if (touched0)
		    graph.touch(edge0);
		
		if (touched1)
		    graph.touch(edge1);
		} catch (ElementNotFoundException e)
		    {
			throw new RuntimeException(e);
		    }
		}catch(Exception e)
		{
		    String logMsg = "La feature "+feature.getSystemId()+"FID:"+feature.getID()+" ha provocado un error durante el análisis:"+e.getMessage()+retornoDeCarro;
//		    context.getOutputFrame().append("<p>"+logMsg+"</p>");
		    stBuilder.append(logMsg);
		    e.printStackTrace();
		}
	}
		return processedEdges;
	}
		
	/**
	 * Almacena los datos de obstï¿½culos y barreras
	 * @param context
	 * @param monitor 
	 * @param network
	 */
//	private void setObstacles(PlugInContext context,DynamicGraph graph, String sLayer, TaskMonitor monitor)
//	{
//		GeopistaLayer layer = (GeopistaLayer)context.getLayerViewPanel().getLayerManager().getLayer(sLayer);
//		Iterator featureIt = layer.getFeatureCollectionWrapper().getFeatures().iterator();
//		int i=0;
//		int total=layer.getFeatureCollectionWrapper().size();
//		while (featureIt.hasNext()){
//		    i++;
//			monitor.report(i, total, "Registrando obstáculos: "+sLayer);
//			GeopistaFeature feature = (GeopistaFeature)featureIt.next();
//			Point measurePoint = GeographicNodeUtil.createPoint(feature.getGeometry().getCoordinate(), fact.getCRS());
//			List edgesList = graph.getEdgesNearTo(measurePoint, distance, 1);
//			if (edgesList.size()< 1) continue;
//			PMRLocalGISStreetDynamicEdge edge1 = (PMRLocalGISStreetDynamicEdge)edgesList.get(0);
//			if  (sLayer.equals(OBSTACULOS_ALTURA)){
//				int alturaIndex = feature.getSchema().getAttributeIndex("Altura");
//				Object altura = feature.getAttribute(alturaIndex);
//				if (altura != null && altura.toString().equals("")){
//					edge1.setObstacleHeight(formatParameter(altura,""));
//					replaceEdgeProperties(calculatedEdgesList, edge1,OBSTACULOS_ALTURA);
//				}
//			}else{
//				edge1.setWidth(0);
//				replaceEdgeProperties(calculatedEdgesList, edge1,BARRERAS);
//			}
//		}
//	}
	
//	private void replaceEdgeProperties(List edgesList, PMRLocalGISStreetDynamicEdge edge, String sTipo  ){
//		int index = edgesList.indexOf(edge);
//		if (index<0)
//			edgesList.add(edge);
//		else{
//			PMRLocalGISStreetDynamicEdge newEdge = (PMRLocalGISStreetDynamicEdge)edgesList.get(index);
//			if (sTipo.equals(OBSTACULOS_ALTURA)){
//				newEdge.setObstacleHeight(edge.getObstacleHeight());
//			}else{
//				//newEdge.setWidth(0);
//			}
//			edgesList.set(index, newEdge);
//		}
//	}
		
	protected boolean copyProperties(Map<Edge, Integer> sides, ILocalGISEdge edge, GeopistaFeature featureParametros1, TaskMonitor monitor){	
		//Inserto las mediciones en cada lado de la acera
	    Map<String,String> mapping= new HashMap<String,String>();
	    Integer side = sides.get(edge);
	
	    mapping.put("Altura","alturaObstaculo");
	if (side!=null)
	{
	if (side == SidewalkEdge.RIGHT )
	    {
	    mapping.put("Ancho_minimo_lado_derecho","anchuraAcera");
	    mapping.put("Pend_trans_mayor_2_por_ciento_lado_derecho","pendienteTransversal");
	    mapping.put("pendienteTransversal","pendienteTransversal"); // ejemplo de mapeo adicional para admitir tambien este nombre de columna
	    mapping.put("Pend_long_mayor_6_por_ciento_lado_derecho","pendienteLongitudinal");   
	    }
	else if (side == SidewalkEdge.LEFT)
	    {
		mapping.put("Ancho_minimo_lado_izquierdo","anchuraAcera");
		mapping.put("pendienteTransversal","pendienteTransversal");
		mapping.put("Pend_trans_mayor_2_por_ciento_lado_izquierdo","pendienteTransversal");
		mapping.put("Pend_long_mayor_6_por_ciento_lado_izquierdo","pendienteLongitudinal");
	    }
	if (edge instanceof PMRLocalGISStreetDynamicEdge) {
		PMRLocalGISStreetDynamicEdge pmrEdge = (PMRLocalGISStreetDynamicEdge) edge;
		pmrEdge.setSide(side);
	}
	}
	    boolean touched=false;
	    for (Map.Entry<String,String> equivalence : mapping.entrySet())
		{
		    String featAttName = equivalence.getKey();
		    Integer index=getAttributeIndex(featureParametros1,featAttName);
		    if (index!=null)
			{
			    String attName = equivalence.getValue();
			    Object attValue = featureParametros1.getAttribute(index);
			    // parche anti chapuzas
			    boolean modified=false;
			   try
			   {
			     modified= edge.setAttribute(attName,attValue);
			   }
			   catch (NumberFormatException e)
			   {
				String logStr="<p>WARN:  : Feature"+featureParametros1.getID()+"["+featAttName+"]="+attValue+"  no es convertible en un Double. Se ha cambiado la ',' por '.' como medida de emergencia. REVISAR DATOS!.";
				stBuilder.append(logStr+retornoDeCarro);
//				context.getOutputFrame().append(logStr);
			       modified= edge.setAttribute(attName,((String)attValue).replaceAll(",","."));
			   }
			    if (!modified)
				{
				String logStr="<p>WARN: Arco :"+edge.getID()+" no tiene atributo:"+attName+" valor de la feature "+featureParametros1.getID()+"["+featAttName+"]=valor("+attValue+") no asignado.";
				stBuilder.append(logStr+retornoDeCarro);
//				context.getOutputFrame().append(logStr);
				}
			    touched=touched|modified;
			}
		}
	    
//		
//		Integer pendienteTransDindex = featureParametros1.getSchema().getAttributeIndex("Pend_trans_mayor_2_por_ciento_lado_derecho");
//		Integer pendienteTransI = featureParametros1.getSchema().getAttributeIndex("Pend_trans_mayor_2_por_ciento_lado_izquierdo");
//		Integer pendienteLongD = featureParametros1.getSchema().getAttributeIndex("Pend_long_mayor_6_por_ciento_lado_derecho");
//		Integer pendienteLongI = featureParametros1.getSchema().getAttributeIndex("Pend_long_mayor_6_por_ciento_lado_izquierdo");
//		Object altura = feature.getAttribute(alturaIndex);
//		
//		Integer alturaIndex = getAttributeIndex(featureParametros1,"Altura");
//		if (alturaIndex != null)
//		    {
//			edge.setAttribute("alturaObstaculo",formatParameter(altura,""));
//		    }
//		String attributeName = "Ancho_minimo_lado_derecho";
//		Integer anchoD = getAttributeIndex(featureParametros1, attributeName);
//		Integer anchoI = featureParametros1.getSchema().getAttributeIndex("Ancho_minimo_lado_izquierdo");
//		Object sAnchoI = anchoI==null?null:featureParametros1.getAttribute(anchoI);
//		Object sAnchoD = anchoD==null?null:featureParametros1.getAttribute(anchoD);
//		Object sPendienteTransI = pendienteTransI==null?null:featureParametros1.getAttribute(pendienteTransI);
//		Object sPendienteLongD = pendienteLongD==null?null:featureParametros1.getAttribute(pendienteLongD);
//		Object sPendienteLongI =pendienteLongI==null?null: featureParametros1.getAttribute(pendienteLongI);
//		Integer side = sides.get(edge);
//		if (side == SidewalkEdge.RIGHT )
//		    {
//		    if (pendienteTransDindex!=null)
//			{
//			Object sPendienteTransD = featureParametros1.getAttribute(pendienteTransDindex);
//			edge.setAttribute("pendienteTransversal",formatParameter(sPendienteTransD,"TransSlope"));
//			}
//		    
//			((PMRLocalGISStreetDynamicEdge)edge).setWidth(formatParameter(sAnchoD,"width"));
//			((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(formatParameter(sPendienteLongD,"LongSlope"));
//		}else
//		    if (side== SidewalkEdge.LEFT)
//			{
//			((PMRLocalGISStreetDynamicEdge)edge).setWidth(formatParameter(sAnchoI,"width"));
//			((PMRLocalGISStreetDynamicEdge)edge).setTransversalSlope(formatParameter(sPendienteTransI,"TransSlope"));
//			((PMRLocalGISStreetDynamicEdge)edge).setLongitudinalSlope(formatParameter(sPendienteLongI,"LongSlope"));
//		}
		
//		if (!calculatedEdgesList.contains(edge))
//			calculatedEdgesList.add(edge);
	    return touched;
	}


	private Integer getAttributeIndex(GeopistaFeature featureParametros1, String attributeName)
	{
	    try
		{
		    return featureParametros1.getSchema().getAttributeIndex(attributeName);
		} catch (IllegalArgumentException e)
		{
		   return null;
		}
	}

	/**
	 * Determino los nï¿½meros de policï¿½a mï¿½s cercanos a un edge
	 */
	
	private int getNearestNum(Edge edge, Edge otherEdge, String extremo)
	{
		List numbersList = new ArrayList();
		List newNumbersList = new ArrayList();
		int numPolicia = -1; 
		boolean useDatabase=true;
		try{
			int srid = context.getLayerManager().getCoordinateSystem().getEPSGCode();
			String systemId = this.layerParams.getSystemId();
			
			if (useDatabase)
			    {
			// BUG Esta query a la base de datos incapacita el algoritmo hacer local!
				numbersList = findStreetNumberList(edge, extremo, srid);
			    }
			else
			    {
				numbersList=findStreetNumberListLocal(edge, extremo, srid);
			    }
			
			Iterator numbersIterator = numbersList.iterator();
			LineString line1 = NetworkModuleUtil.getEdgeLineStringFromGeometry(NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge,context));
			LineString line2 = NetworkModuleUtil.getEdgeLineStringFromGeometry(NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(otherEdge,context));
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
/**
 *  * Busca los números de policía de la calle afectada a partir de features de localgis
 * más próximos a un extremo de la acera A o B
 * @param edge
 * @param extremo
 * @param srid
 * @return  array de arrays Object[]{id,x,y,"rotulo",feature};
 */
	public List findStreetNumberListLocal(Edge edge, String extremo, int srid)
	{
	    List numberList=new ArrayList();
	    Layer streetNumbersLayer = dialog.getLayer(NUMEROS_POLICIA_FIELDNAME);
	    double tolerance=  dialog.getDouble(TOLERANCIA_FIELDNAME);
	    PMRLocalGISStreetDynamicEdge lgEdge=(PMRLocalGISStreetDynamicEdge)edge;
	    ILocalGISEdge street=(ILocalGISEdge)lgEdge.getRelatedTo();
	    Integer streetIdFeature = lgEdge.getRelatedToId();
	    Feature streetFeature = NetworkModuleUtilWorkbench.findFeatureForEdge(street, context);
	    Integer idVia= (Integer) streetFeature.getAttribute("id_via");
	    
	    XYNode node;
	    if ("A".equals(extremo))
		{
		    node= (XYNode)lgEdge.getNodeA();		
		}
	    else if ("B".equals(extremo))
		{
		    node= (XYNode)lgEdge.getNodeB();
		}
	    else
		throw new IllegalArgumentException(extremo);
	    
	    final Coordinate coord = node.getCoordinate();
	    Envelope envelope = new Envelope(coord);
	    envelope.expandBy(tolerance); 
	    // primera aproximación a la búsqueda
	    List features = streetNumbersLayer.getFeatureCollectionWrapper().query(envelope);
	    // filtrado de elementos que cumplan la condición de pertenecer a la misma idVia
	    for(int i=0;i<features.size();i++)
		{
		    Feature f=(Feature)features.get(i);
		    if (f.getAttribute("id_via")==idVia)
			{
			    // crea la estructura y la añade
			    Coordinate fCoordinate = f.getGeometry().getCentroid().getCoordinate();
			    Object[] result=new Object[]{
				    f.getID(),
				    fCoordinate.x,
				    fCoordinate.y,
				    f.getAttribute("rotulo"),
				    f				    
			    	};
			    numberList.add(result);
			}
		}
	    // ordena array por distancia
	    Object[] numArray = numberList.toArray();
	    Arrays.sort(numArray, new Comparator<Object[]>()
	        {

		    public int compare(Object[] o1, Object[] o2)
		    {
			Coordinate coord1=new Coordinate((Double)o1[1],(Double) o1[2]);
			Coordinate coord2=new Coordinate((Double)o2[1], (Double) o2[2]);
			double dist1=coord1.distance(coord);
			double dist2=coord2.distance(coord);
			return (int) (dist1-dist2);
		    }
	        });
	    return Arrays.asList(numArray);
	}
/**
 * Busca los números de policía de la calle afectada 
 * más próximos a un extremo de la acera A o B
 * @param edge
 * @param extremo
 * @param srid
 * @return  array de arrays Object[]{rs.getInt("id"),rs.getDouble("x"),rs.getDouble("y"),rs.getString("rotulo")};
 * @throws SQLException
 * @throws NoSuchAuthorityCodeException
 * @throws IOException
 * @throws FactoryException
 */
	public List findStreetNumberList(Edge edge, String extremo, int srid) throws SQLException, NoSuchAuthorityCodeException, IOException, FactoryException
	{
	    List numbersList;
	    numbersList = dao.getNumbersList((ILocalGISEdge) edge,"tramosvia", srid, extremo, connection);
	    return numbersList;
	}
	
	private LineString getLineStringForEdge(PMRLocalGISStreetDynamicEdge edge) {
//		GeometryFactory factory = AppContext.getApplicationContext().getGeometryFactory();
//		LineString lineString = factory.createLineString(new Coordinate[]{((XYNode)edge.getNodeA()).getCoordinate(),((XYNode)edge.getNodeB()).getCoordinate()});
//		return lineString;
	    return (LineString) NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);
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
	// TODO Analizar el funcionamiento No se entiende.
	// TODO eliminar el campo calculatedSide del Edge NO forma parte del modelo.
	// usar un Map
	private Map<Edge,Integer> calculateSides(Edge edge1, Edge edge2)
	{
		//Numero de portal para el nodoA
		int portal1 = getNearestNum(edge1, edge2,"A");
		//Numero de portal para el nodoB
		int portal2 = getNearestNum(edge1, edge2,"B");
		
		Map<Edge,Integer> sideMap=new HashMap<Edge,Integer>();
		if (((isImpar(portal1) && !isImpar(portal2)) || (!isImpar(portal1) && isImpar(portal2))) || portal1 == portal2){
			//Numero de portal para el nodoA
			portal1 = getNearestNum(edge2, edge1,"A");
			//Numero de portal para el nodoB
			portal2 = getNearestNum(edge2, edge1,"B");
		}
		if (((isImpar(portal1) && !isImpar(portal2)) || (!isImpar(portal1) && isImpar(portal2))) || portal1 == portal2){
			stBuilder.append("No se puede determinar la acera izquierda y derecha de acuerdo a los portales del eje ").append(edge1.getID()).append(retornoDeCarro);
			return sideMap;
		}
			
		if (portal1 < portal2){
			if (isImpar(portal1)){ //impar
			    	sideMap.put(edge1,SidewalkEdge.LEFT);
			    	sideMap.put(edge2,SidewalkEdge.RIGHT);
//				edge1.setCalculatedSide(SidewalkEdge.LEFT);
//				edge2.setCalculatedSide(SidewalkEdge.RIGHT);
			}else{
			    	sideMap.put(edge2,SidewalkEdge.LEFT);
			    	sideMap.put(edge1,SidewalkEdge.RIGHT);
//				edge2.setCalculatedSide(SidewalkEdge.LEFT);
//				edge1.setCalculatedSide(SidewalkEdge.RIGHT);
			}
		}else if (portal1 > portal2){
			if (!isImpar(portal1)){ //par
			    	sideMap.put(edge2,SidewalkEdge.LEFT);
			    	sideMap.put(edge1,SidewalkEdge.RIGHT);
//				edge2.setCalculatedSide(SidewalkEdge.LEFT);
//				edge1.setCalculatedSide(SidewalkEdge.RIGHT);
			}else{
			    	sideMap.put(edge1,SidewalkEdge.LEFT);
			    	sideMap.put(edge2,SidewalkEdge.RIGHT);
//				edge1.setCalculatedSide(SidewalkEdge.LEFT);
//				edge2.setCalculatedSide(SidewalkEdge.RIGHT);
			}	
		}else{
		
		    stBuilder.append("No se puede determinar la acera izquierda y derecha en función de los portales del eje "+edge1.getID()+" y "+edge2.getID()+retornoDeCarro);
		}
		return sideMap;
	}
	
	private boolean isImpar(int num){
	    return num%2==1;
//		if (((num/2)*2) == num)
//			return false;
//		else
//			return true;
	}
}