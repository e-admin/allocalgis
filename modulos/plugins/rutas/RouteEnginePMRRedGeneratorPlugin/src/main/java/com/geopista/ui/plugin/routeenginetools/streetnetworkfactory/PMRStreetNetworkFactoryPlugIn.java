/**
 * PMRStreetNetworkFactoryPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.routeserver.ElementNotFoundException;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.dibujarredplugin.DibujarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.genredplugin.PMRGenerarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.PMRStreetNetworkFactoryDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.network.NetworkProperty;
import com.localgis.route.weighter.PMRProperties;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import es.uva.idelab.route.algorithm.BlocksAlgorithm;
import es.uva.idelab.route.algorithm.GeometrySideWalkFactory;
import es.uva.idelab.route.algorithm.SidewalkEdge;
import es.uva.idelab.route.algorithm.SidewalkFactory;

public class PMRStreetNetworkFactoryPlugIn extends PMRGenerarRedPlugIn {

	private static final String DISPLACEMENT_FIELDNAME = "Distancia de la acera al eje de calle";
	private boolean genRedButtonAdded = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private PlugInContext context = null;
	private PMRProperties pmrProperties = new PMRProperties();
	private String CALLEJERO = "Callejero";
	protected PMRStreetNetworkFactoryDialog basicDialog;
	private static Logger LOGGER = Logger.getLogger(PMRStreetNetworkFactoryPlugIn.class);

	public boolean execute(PlugInContext context) 
	{
//		if (!AppContext.getApplicationContext().isLogged())
//			AppContext.getApplicationContext().login();
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		PMRStreetNetworkFactoryDialog dialog = new PMRStreetNetworkFactoryDialog(context.getWorkbenchFrame(), "Generación de aceras.",getPluginDescription(), context);
		dialog.addPositiveDoubleField(DISPLACEMENT_FIELDNAME, 2, 8);
		basicDialog = dialog;
		dialog.setVisible(true);
		if (dialog.wasOKPressed())
		   {
		       processActions();
		       return true;
		   }
		else
		    return false;

	}


	/**
	 * Lanza el proceso de generacion de redes en un hilo. Implementar el metodo {@link #run(TaskMonitor, PlugInContext)}.
	 */
	protected void processActions()
	{
	    final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    final PlugInContext runContext = this.context;
	    progressDialog.setTitle("TaskMonitorDialog.Wait");
	    progressDialog.report(I18N.get("genred","routeengine.genred.taskmonitormessage"));
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
	    				    withTaskMonitorDo(progressDialog, runContext);
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
	}
	



	protected String getPluginDescription()
	{
	   return "Se generaran las topologias de aceras utilizando como base la red de calles y la capa de geometrias seleccionadas.";
	}



	/**
	 * La red de origen debe estar cargada en memoria previamente.
	 * @param context
	 * @param linegenerator objeto donde se carga el grafo con los elementos nuevos creados.
	 * @param networkProperties
	 * 
	 */
	private boolean generarRedPMR(PlugInContext context, GraphGenerator linegenerator, HashMap<String, Object> networkProperties, TaskMonitor monitor)
	{	
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			GraphGenerator graphGenerator = NetworkModuleUtilWorkbench.getLocalGISStreetGraphGenerator();
			String sourceNetName = getDialogNetworkName(); // TODO pasar la red como parámetro
			String targetNetName = basicDialog.getText(PMRStreetNetworkFactoryDialog.TARGET_NETWORK_NAME_FIELDNAME);
			NetworkManager netMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
			Network net=netMgr.getNetwork(sourceNetName);
			Network sidewalksNet=netMgr.getNetwork(targetNetName);
			Graph graph=net.getGraph();
			if (graph.getEdges().size()==0)
			    {
				context.getWorkbenchFrame().warnUser("La red "+sourceNetName+" no contiene elementos.");
				return false;
			    }
//			DBRouteServerReaderWriter db = null;
//			int inicio = AppContext.getIdEntidad()*100000;
//			int fin = (AppContext.getIdEntidad()+1)*100000;
//			db = new LocalGISStreetRouteReaderWriter(connectionFactory, false, inicio, fin);
//			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
//			db.setNetworkName(((PMRBasicNetworkFactoryDialog)basicDialog).getsRedOrigen());	
//			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
//			Graph graph = new DynamicGraph(manager);
		monitor.report("Construyendo índice de geometrías.");
		Graph sidewalsGraph = sidewalksNet!=null?sidewalksNet.getGraph():null;
		Collection<Edge> prevSidewalkEdges = sidewalsGraph!=null?sidewalsGraph.getEdges():Collections.EMPTY_LIST;
		
		SidewalkFactory fact = getSidewalkFactory(sidewalsGraph);
		if (((PMRSideWalkFactory)fact).getCache() == null) //Comprueba que tiene el indice de features formado
		    {
			JOptionPane.showMessageDialog(null, I18N.get("genred","routeengine.capaValida"));
			return false;
		    }
			
	        BlocksAlgorithm blocks = new BlocksAlgorithm(fact);
	        
	        /**
		 * Obtiene un subconjunto del grafo para procesar aparte
		 */
		if (basicDialog.getBoolean(PMRStreetNetworkFactoryDialog.SOURCE_NETWORK_SELECTED_FEATURES))
		    {
			Collection<Feature> selected = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
			Collection<Edge> selectedEdges = getSubgraphWithSelectedFeatures(graph,selected);
			if (selectedEdges.size()==0)
			    {
				JOptionPane.showMessageDialog(context.getWorkbenchFrame(), "No se han encontrado arcos para procesar. Asegúrese de que las Features seleccionadas tienen un atributo llamado idEje","No hay elementos",JOptionPane.ERROR_MESSAGE);
				return false;
			    }
			// register previous sidewalks
			for(Edge edge:prevSidewalkEdges)
			    {
				if (edge instanceof PMRLocalGISStreetDynamicEdge)
				    {
					try
					    {
						PMRLocalGISStreetDynamicEdge sidewalk = (PMRLocalGISStreetDynamicEdge) edge;
						int edgeId = sidewalk.getRelatedToId();
						Edge relatedEdge = graph.getEdge(edgeId);
						if (sidewalk.getSide()==SidewalkEdge.LEFT)
						    blocks.registerLeftSidewalk(relatedEdge, sidewalk);
						else
						    blocks.registerRightSidewalk(relatedEdge, sidewalk);
						((GeometrySideWalkFactory)fact).registerGeometry(sidewalk,(LineString) sidewalk.getGeometry());
					    } catch (ElementNotFoundException e)
					    {
						LOGGER.error(e);
					    }
				    }
			    }
			// Run only selected edges
			blocks.run(graph, selectedEdges);
		    }
		else
		    {   
			blocks.run(graph);
		    }
	        Collection<SidewalkEdge> sidewalks = blocks.getSidewalks();
	        List nodesList = new ArrayList();
        	Map<SidewalkEdge, LineString> sidewalkGeoms = ((GeometrySideWalkFactory)fact).getSidewalkGeoms();
        	// sidewalks creados en este paso.
        	List<SidewalkEdge> newSidewalks= new ArrayList<SidewalkEdge>(sidewalks);
        	newSidewalks.removeAll(prevSidewalkEdges);
        	
			context.getWorkbenchFrame().getOutputFrame().createNewDocument();
	
        for (SidewalkEdge edge:sidewalks)
	    {
		PMRLocalGISStreetDynamicEdge sidewalk = (PMRLocalGISStreetDynamicEdge) edge;
		try
		    {
			LineString geom = ((PMRSideWalkFactory) fact).findGeometryFor(sidewalk);
			sidewalk.setGeometry(geom);
//			if (geom.getLength()==0d) // Ignora los resultados de longitud 0
//			{
//				String msg = "La acera correspondiente al arco "+sidewalk.getRelatedToId()+" no es correcta y se ignorará.";
//				LOGGER.error(msg);
//				context.getWorkbenchFrame().getOutputFrame().append(msg);
//				context.getWorkbenchFrame().warnUser("Error al generar la acera. Vea el panel de avisos!");
//				continue;
//			}
			sidewalk.setEdgeLength(geom.getLength());
		    } catch (Exception e)
		    {// Some geometries may generate unexpected exceptions when processed.
			LOGGER.warn("Sidewalk geometry error. No geometry defined." + sidewalk, e);
			String msg = "La acera correspondiente al arco "+sidewalk.getRelatedToId()+" no es correcta y se ignorará.";
			LOGGER.error(msg);
			context.getWorkbenchFrame().getOutputFrame().append(msg);
			context.getWorkbenchFrame().warnUser("Error al generar la acera. Vea el panel de avisos!");
		    }
			/*
			 * System.out.println("sidewalk ID "+sidewalk.getID()); System.out.println("sidewalk Related "+sidewalk.getRelatedTo().getID());
			 * System.out.println("geom "+geom.toText()); System.out.println("");
			 */
			if (newSidewalks.contains(sidewalk))
			   {
			       linegenerator.getGraphBuilder().addEdge(sidewalk);
			       if (!nodesList.contains(sidewalk.getNodeA()))
			    	   nodesList.add(sidewalk.getNodeA());
			       if (!nodesList.contains(sidewalk.getNodeB()))
			    	   nodesList.add(sidewalk.getNodeB());
			   }
		    
	    }
	        Iterator itNodesList = nodesList.iterator();
	        while (itNodesList.hasNext()){
	        	Node node = (Node)itNodesList.next();
	        	Coordinate coord = ((DynamicGeographicNode)node).getCoordinate();
	    		((DynamicGeographicNode)node).setPosition(GeographicNodeUtil.createPoint(coord, ((PMRSideWalkFactory)fact).getCRS()));
	        	linegenerator.getGraphBuilder().addNode(node);
	        }
	        return true;
	}
/**
 * Extrae los elementos del grafo que están representados en las features seleccionadas
 * @param graph
 * @param selected
 * @return
 */
private Collection<Edge> getSubgraphWithSelectedFeatures(Graph graph, Collection<Feature> selected)
	{
	    ArrayList<Edge> selectedEges= new ArrayList<Edge>();
	    for (Feature feature:selected)
		{
		    try
			{
			    FeatureSchema featureSchema = feature.getSchema();
			    if (featureSchema.hasAttribute("idEje"))
			    {
			        Integer idEje= (Integer)feature.getAttribute("idEje");
			        Edge edge=graph.getEdge(idEje);
			        selectedEges.add(edge);
			    }
			} catch (ElementNotFoundException e)
			{
			    LOGGER.warn(e);
			}
		}
	    return selectedEges;
	}


/**
 * Devuelve el nombre de la red seleccionada por el usuario en los desplegables de redes del dialogo
 * @return
 */
    private String getDialogNetworkName()
	{
	PanelToSelectMemoryNetworks panelToSelectMemoryNetworks = (PanelToSelectMemoryNetworks)this.basicDialog.getComponent(PMRStreetNetworkFactoryDialog.SOURCE_NETWORK_FIELDNAME);
	String subred=panelToSelectMemoryNetworks.getSubredseleccionada();
	if ("".equals(subred))
	    return panelToSelectMemoryNetworks.getRedSeleccionada();
	else
	    return subred;
	}


/**
 * 
 * @param sidewalsGraph
 * @return
 */
    protected SidewalkFactory getSidewalkFactory(Graph sidewalsGraph)
    {
	Layer sourceLayer=basicDialog.getLayer(PMRStreetNetworkFactoryDialog.SOURCE_LAYER_FIELDNAME);
	PMRSideWalkFactory fact = new PMRSideWalkFactory(sourceLayer, this.context);
	 
	fact.setDisplacement(basicDialog.getDouble(DISPLACEMENT_FIELDNAME));
//	SequenceUIDGenerator ids = NetworkModuleUtil.getUIDGenerator();
//	SequenceUIDGenerator edges_ids = NetworkModuleUtil.getUIDGenerator(AppContext.getIdEntidad());
	SequenceUIDGenerator ids;
	if (sidewalsGraph!=null)
	    ids = NetworkModuleUtil.getUIDGenerator(sidewalsGraph, AppContext.getIdEntidad());
	else
	    ids = NetworkModuleUtil.getUIDGenerator(AppContext.getIdEntidad());
	
	fact.setUIDGenerator(ids);
	fact.setUIDGeneratorForEdges(ids);
	return fact;
	
    }
	

/*	private boolean setImpedanceToLocalGisDynamicEdge(Geometry mls, Feature elementFeature,
			PMRLocalGISStreetDynamicEdge lgDynamicEdge){

		if (basicDialog.getBidirectionRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));
			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else if (basicDialog.getUniqueDirectionAtoBRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));

		} else if (basicDialog.getUniqueDirectionBtoARadioButton().isSelected()){

			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else{
			return false;
		}

		return true;
	}*/


	/**
	 * @param mls
	 * @param e
	 * @param element 
	 */
/*	private EdgeImpedance createNewLocalGisSimpleimpedance(Geometry mls,
			JComboBox impedanceComboBox, Feature elementFeature) {

		double cost = 0;

		if (impedanceComboBox.getSelectedIndex() >= 0){
			if (impedanceComboBox.getSelectedItem().equals("Longitud")){
				if (mls instanceof LineString){
					cost = mls.getLength();
				} 
				else if (mls instanceof MultiLineString){
					for (int i = 0; i < mls.getNumGeometries(); i++) {  
						cost = cost + mls.getGeometryN(i).getLength();
					}
				}
			} else {
				if (elementFeature != null){
					cost = 	Double.parseDouble(
							(String)elementFeature.getAttribute(
									(String) impedanceComboBox.getSelectedItem()
							)
					);
				}
			}
		}

		return new SimpleImpedance(cost);
	}


	private ArrayList<Feature> getOriginalFeatures (String nombreCapa){
		ArrayList<Feature> result = null;

		if (nombreCapa == null || nombreCapa.equals("")){
			result = (ArrayList<Feature>) context.getLayerViewPanel().getSelectionManager()
			.getFeaturesWithSelectedItems();
		}else{
			result = (ArrayList<Feature>) context.getLayerManager().getLayer(nombreCapa).getFeatureCollectionWrapper()
			.getWrappee().getFeatures();
		}

		return result;

	}*/

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();    
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.genredplugin.language.RouteEngine_GenRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("genred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);
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
		return IconLoader.icon("genPMRred.gif");
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!genRedButtonAdded  )
		{
//			toolbox.addToolBar();
			PMRStreetNetworkFactoryPlugIn explode = new PMRStreetNetworkFactoryPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			genRedButtonAdded = true;
		}
	}


	public void run(TaskMonitor monitor, PlugInContext context)
	{

		String nombrered = basicDialog.getText(PMRStreetNetworkFactoryDialog.TARGET_NETWORK_NAME_FIELDNAME);

		/**
		 * Comprueba conflicto de nombre target
		 */
		NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network parent=null;
		
		Network network = networkMgr.getNetwork(nombrered);
		if (network != null)
		    {
			String title = I18N.get("genred", "routeengine.genred.errormessage.usedname");
			String message = I18N.get("genred", "routeengine.genred.errormessage.overwritename"); // TODO Actualizar texto en properties
			message="La red ya existe, ¿desea modificar la red existente?";
			int response = JOptionPane.showConfirmDialog(context.getWorkbenchFrame(), message,title,JOptionPane.OK_CANCEL_OPTION);
			
//JPC: permite reutilizar la red
//			VentanaError ventanaerror = new VentanaError(context);
//			
//			ventanaerror.addText(title);
//			
//			ventanaerror.addText(message);
//			ventanaerror.setVisible();
			
			if (response==JOptionPane.OK_OPTION)
			    {
				//networkMgr.detachNetwork(nombrered);
			    }
			else
			    {
				return;
			    }
		    }
		
		
		  {
			
			
			GraphGenerator linegenerator = NetworkModuleUtilWorkbench.getLocalGISStreetBasicLineGraphGenerator();
			HashMap<String, Object> networkProperties = new HashMap<String, Object>();
			networkProperties.put("PMR", new NetworkProperty("PMR", "true"));
			
		
			if (generarRedPMR(context, linegenerator, networkProperties, monitor) == false)
				return;
			// grafo con los nuevos elementos
			Graph graph = linegenerator.getGraph();
			
			if (network==null)
			    {
				NetworkModuleUtil.addNewNetwork(networkMgr, nombrered, graph, networkProperties);
			    }
			else
			    {
				Graph oldGraph = network.getGraph();
				if (oldGraph instanceof DynamicGraph)
				    {
					DynamicGraph dynGraph = (DynamicGraph) oldGraph;
					try
					    {
						dynGraph.getMemoryManager().appendGraph(graph);
					    } catch (IOException e)
					    {
						throw new RuntimeException(e);
					    }
					
				    }
				else
				    {
					throw new IllegalStateException("Can't modify this read-only graph!");
				    }
			    }
			
			networksLayerGeneratorFromGraph(context,nombrered,graph,networkProperties);
		    }
		
/**
 * dasfadfafdafafadda
 */
//		RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
//		LocalGISStreetRouteReaderWriter	db = new LocalGISStreetRouteReaderWriter(routeConnection, true); 
//		db.setNetworkName(nombrered);
//		DynamicGraph dynamicGraph = new DynamicGraph(new LocalGISAllinMemoryManager(db));
//		dynamicGraph.getMemoryManager().appendGraph(linegenerator.getGraph());
//		
//		networksLayerGeneratorFromGraph(context, nombrered, dynamicGraph, networkProperties);
		
		basicDialog.dispose();
	}
	
	/**
	 * No muestra ni calcula particiones sólo dibuja el resultado
	 */
	 public void networksLayerGeneratorFromGraph(PlugInContext context, String nombrered, Graph graph, HashMap<String, Object> networkProperties)
	    {
		// guardo la red y su grafo
		NetworkManager networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network parent= networkMgr.getNetwork(nombrered);
		
		
		if (basicDialog.getBoolean(PMRStreetNetworkFactoryDialog.DRAW_LAYER_FIELDNAME))
		    {
			// NetworkModuleUtil.createGraphLayer(graph, context, subred, categoryName) // TODO usar la librería
			DibujarRedPlugIn delegate=new DibujarRedPlugIn();
			delegate.createEdgesLayer(context, nombrered,  parent.getGraph().getEdges());
		    }

	    }

}