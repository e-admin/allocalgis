package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;


import java.util.ArrayList;
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
import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.io.DBRouteServerReaderWriter;
import org.uva.route.graph.structure.dynamic.DynamicGeographicNode;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.util.GeographicNodeUtil;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.genredplugin.PMRGenerarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.PMRStreetNetworkFactoryDialog;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.build.dynamic.LocalGISStreetBasicLineGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphBuilder;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.route.weighter.PMRProperties;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datastore.ConnectionManager;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

import es.uva.idelab.route.algorithm.BlocksAlgorithm;
import es.uva.idelab.route.algorithm.GeometrySideWalkFactory;
import es.uva.idelab.route.algorithm.SidewalkEdge;
import es.uva.idelab.route.algorithm.SidewalkFactory;

public class PMRStreetNetworkFactoryPlugIn extends PMRGenerarRedPlugIn {

	private boolean genRedButtonAdded = false;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private PlugInContext context = null;
	private PMRProperties pmrProperties = new PMRProperties();
	private String CALLEJERO = "Callejero";

	private static Logger LOGGER = Logger.getLogger(PMRStreetNetworkFactoryPlugIn.class);

	public boolean execute(PlugInContext context) throws Exception {
		if (!AppContext.getApplicationContext().isLogged())
			AppContext.getApplicationContext().login();
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		PMRStreetNetworkFactoryDialog dialog = new PMRStreetNetworkFactoryDialog(
				context.getWorkbenchFrame(), "", context);

		basicDialog = dialog;

		super.execute(context);

		return false;

	}



	/**
	 * @param context
	 * @param linegenerator
	 * @param networkProperties
	 */
	private boolean generarRedPMR(PlugInContext context, GraphGenerator linegenerator, HashMap<String, Object> networkProperties) {

		ConnectionManager.instance(context.getWorkbenchContext());

		

		if (!AppContext.getApplicationContext().isLogged()){
			AppContext.getApplicationContext().login();			
		}
		try {
			RouteConnectionFactory connectionFactory = new GeopistaRouteConnectionFactoryImpl();
			DBRouteServerReaderWriter db = null;
			GeographicGraphGenerator graphGenerator = null;
			
			graphGenerator = new LocalGISStreetGraphGenerator();
			int inicio = AppContext.getIdEntidad()*100000;
			int fin = (AppContext.getIdEntidad()+1)*100000;
			db = new LocalGISStreetRouteReaderWriter(connectionFactory, false, inicio, fin);
			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);
			db.setNetworkName(basicDialog.getsRedOrigen());
			
			SpatialAllInMemoryExternalSourceMemoryManager manager = new SpatialAllInMemoryExternalSourceMemoryManager(db);
			Graph graph = new DynamicGraph(manager);
			SidewalkFactory fact = getSidewalkFactory();
			if (((PMRSideWalkFactory)fact).getCache() == null){
				JOptionPane.showMessageDialog(null, I18N.get("genred","routeengine.capaValida"));
				return false;
			}
	        BlocksAlgorithm blocks = new BlocksAlgorithm(fact);
	        blocks.run(graph);
	        Iterator itEdges = blocks.getSidewalks().iterator();
	        List nodesList = new ArrayList();
        	Map<SidewalkEdge, LineString> sidewalkGeoms = ((GeometrySideWalkFactory)fact).getSidewalkGeoms();

	        while (itEdges.hasNext()){
	        	
					PMRLocalGISStreetDynamicEdge sidewalk = (PMRLocalGISStreetDynamicEdge)itEdges.next();
				try {
					LineString geom = ((PMRSideWalkFactory)fact).findGeometryFor(sidewalk);
					sidewalk.setGeom(geom);
					sidewalk.setEdgeLength(geom.getLength());
/*				System.out.println("sidewalk ID "+sidewalk.getID());
					System.out.println("sidewalk Related "+sidewalk.getRelatedTo().getID());
					System.out.println("geom "+geom.toText());
					System.out.println("");*/
					linegenerator.getGraphBuilder().addEdge(sidewalk);
					if (!nodesList.contains(sidewalk.getNodeA()))
						nodesList.add(sidewalk.getNodeA());
					if (!nodesList.contains(sidewalk.getNodeB()))
						nodesList.add(sidewalk.getNodeB());
				} catch (Exception e) {// Some geometries generate unexpected exceptions when processed.
					LOGGER.warn("Sidewalk geometry error. Skipping."+sidewalk, e);
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
		} catch (Exception e) {

			e.printStackTrace();
		}
		return false;
	}
	

    protected SidewalkFactory getSidewalkFactory()
    {
	    	PMRSideWalkFactory fact = new PMRSideWalkFactory(this.context);
	        SequenceUIDGenerator ids = new SequenceUIDGenerator();
	        ids.setSeq(AppContext.getIdEntidad()*100000);
	        SequenceUIDGenerator edges_ids = new SequenceUIDGenerator();
	        edges_ids.setSeq(AppContext.getIdEntidad()*100000);
	        fact.setUIDGenerator(ids);
	        fact.setUIDGeneratorForEdges(edges_ids);
	        return fact;
    }
	
	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
		run(monitor, context);
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
	throws Exception {

		String nombrered = basicDialog.getNombreRedTextField().getText();

		//Recojo las caracteristicas de la minusvalia

		GraphGenerator linegenerator = null;
		linegenerator = new LocalGISStreetBasicLineGenerator(new LocalGISStreetGraphBuilder());
		Network subnet = new BasicNetwork(nombrered);
		Graph graph = null;
		LineString lstring = null;
		HashMap<String, Object> networkProperties = new HashMap<String, Object>();

		if (generarRedPMR(context, linegenerator, networkProperties) == false)
			return;


		RouteConnectionFactory routeConnection = new GeopistaRouteConnectionFactoryImpl();
		LocalGISStreetRouteReaderWriter	db = new LocalGISStreetRouteReaderWriter(routeConnection, true); 
		db.setNetworkName(nombrered);
		DynamicGraph dynamicGraph = new DynamicGraph(new LocalGISAllinMemoryManager(db));
		dynamicGraph.getMemoryManager().appendGraph(linegenerator.getGraph());
		
		networksLayerGeneratorFromGraph(context, nombrered, dynamicGraph, networkProperties);
		
		basicDialog.dispose();
	}


}