package com.geopista.ui.plugin.routeenginetools.leerreddelocal;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.uva.geotools.graph.io.standard.AbstractReaderWriter;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.route.graph.build.dynamic.GeographicGraphGenerator;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicInterNetworker;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.route.util.NodeUtils;
import org.uva.routeserver.managers.AllInMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.ui.plugin.routeenginetools.leerreddelocal.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataLocalStore;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToLoadFromDataStore;
import com.localgis.route.graph.build.dynamic.LocalGISGraphGenerator;
import com.localgis.route.graph.build.dynamic.LocalGISStreetGraphGenerator;
import com.localgis.route.graph.io.LocalRouteReaderWriter;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.localgis.route.network.NetworkProperty;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.OKCancelDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.datastore.WithOutConnectionPanel;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class LeerRedDeLocalPlugIn extends ThreadedBasePlugIn {


	Graph graph;
	private Layer capadebase;
	private boolean saveRedLocalButtonAdded;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private OKCancelDialog dialog;

	public boolean execute(final PlugInContext context) {

		OKCancelDialog dlg = getDialog(context);
		dlg.setVisible(true);

		return dlg.wasOKPressed();
	}

	private Layer createLayer(final PanelToLoadFromDataStore panel,
			final PlugInContext context, HashMap<String, Object> networkProperties) throws Exception {


		if (graph != null){
			NetworkManager networkMgr = FuncionesAuxiliares
			.getNetworkManager(context);

			if (((BasicNetworkManager)networkMgr).getInterNetworker() == null ){
				BasicInterNetworker binNet = new BasicInterNetworker();
				binNet.setNetworkManager(networkMgr);
				networkMgr.setInterNetworker(binNet);
			}


			Network parent = networkMgr.putNetwork("RedLocal", null);

			// Pasar grafo a dynamicgraph
			AllInMemoryManager memmgr = new AllInMemoryManager();
			memmgr.setGraph(graph);
			DynamicGraph subnetGraph = new DynamicGraph(memmgr);

			BasicNetwork subNet = new BasicNetwork(panel.getSubredSelected());
			subNet.setGraph(subnetGraph);
			Collection edges = subnetGraph.getEdges();

			for (int i = 0; i < networkProperties.keySet().size(); i++){
				String key = (String) networkProperties.keySet().toArray()[i];
				NetworkProperty value = (NetworkProperty) networkProperties.get(key);
				parent.getProperties().put(key, value);
			}

			parent.getSubnetworks().put(panel.getSubredSelected(),subNet);


			GeometryFactory fact = new GeometryFactory();
			FeatureCollection edgesFeatureCol = null;
			// creo capas con los arcos
			for (Iterator iter_edges = edges.iterator(); iter_edges.hasNext();) {
				Edge edge = (Edge) iter_edges.next();
				Coordinate[] coords = NodeUtils.CoordenadasArco(edge,
						null, 0, 0);

				if (edgesFeatureCol == null){
					if (edge instanceof LocalGISDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
					}else if(edge instanceof LocalGISStreetDynamicEdge || edge instanceof PMRLocalGISStreetDynamicEdge){
						edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
						if(edge instanceof PMRLocalGISStreetDynamicEdge)
							edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
					}else if (edge instanceof DynamicEdge) {
						edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection();
					}else if (edge instanceof Edge){
						edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
					}
				}

//				if (edge instanceof LocalGISDynamicEdge){
//					if (edgesFeatureCol == null){
//						edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
//					}
//					Feature feature = new BasicFeature(edgesFeatureCol
//							.getFeatureSchema());
//					feature.setAttribute("idEje", new Integer(edge.getID()));
//					feature.setAttribute("coste", ((LocalGISDynamicEdge) edge).getCost());
//					LineString geom_edge = fact.createLineString(coords);
//					feature.setGeometry(geom_edge);
//					edgesFeatureCol.add(feature);
//				} else if (edge instanceof LocalGISStreetDynamicEdge){
//					if (edgesFeatureCol == null){
//						edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
//					}
//					Feature feature = new BasicFeature(edgesFeatureCol
//							.getFeatureSchema());
//					feature.setAttribute("idEje", new Integer(edge.getID()));
//					feature.setAttribute("coste", ((LocalGISStreetDynamicEdge) edge).getCost());
//
//					LineString geom_edge = fact.createLineString(coords);
//					feature.setGeometry(geom_edge);
//					edgesFeatureCol.add(feature);
//				}
				Feature feature = new BasicFeature(edgesFeatureCol
						.getFeatureSchema());
				feature.setAttribute("idEje", new Integer(edge.getID()));
				feature.setAttribute("coste", new Double(
						((EdgeWithCost) edge).getCost()));
				feature.setAttribute("idNodoA", edge.getNodeA().getID());
				feature.setAttribute("idNodoB", edge.getNodeB().getID());
				if (edge instanceof DynamicEdge){
					feature.setAttribute("impedanciaAB", ((DynamicEdge) edge).getImpedance(edge.getNodeA()).getCost(1));
					feature.setAttribute("impedanciaBA", ((DynamicEdge) edge).getImpedance(edge.getNodeB()).getCost(1));
					feature.setAttribute("costeEjeDinamico", ((DynamicEdge) edge).getCost());
				}
				if (edge instanceof ILocalGISEdge){
					feature.setAttribute("longitudEje", ((ILocalGISEdge) edge).getEdgeLength());
					feature.setAttribute("idFeature", ((ILocalGISEdge) edge).getIdFeature());
					feature.setAttribute("idCapa", ((ILocalGISEdge) edge).getIdLayer());
				}
				if (edge instanceof LocalGISStreetDynamicEdge){
					feature.setAttribute("regulacionTrafico", ((LocalGISStreetDynamicEdge) edge).getTrafficRegulation().toString());
					feature.setAttribute("maxVelocidadNominal", RedondearVelocidad(((LocalGISStreetDynamicEdge) edge).getNominalMaxSpeed() * 3600 / 1000));
					feature.setAttribute("pintadaRegulacionTrafico", 0);
					if (edge instanceof PMRLocalGISStreetDynamicEdge){
						feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) edge).getWidth());
						feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) edge).getTransversalSlope());
						feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) edge).getLongitudinalSlope());
						feature.setAttribute("tipoEje", ((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType());
						feature.setAttribute("ejeRelacionadoConId", ((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId());
						feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) edge).getObstacleHeight());
						if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.LEFT)
							feature.setAttribute("ladoAcera", "L");
						else if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.RIGHT)
							feature.setAttribute("ladoAcera", "R");
						if (edge instanceof ZebraDynamicEdge)
							feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
					}
				}

				LineString geom_edge = fact.createLineString(coords);
				feature.setGeometry(geom_edge);
				edgesFeatureCol.add(feature);

			}
			Layer edgesLayer = context.addLayer("Red de Local ", panel
					.getSubredSelected(), edgesFeatureCol);
			LabelStyle labelStylenew = new LabelStyle();
			labelStylenew.setAttribute("coste");
			labelStylenew.setColor(Color.red);
			labelStylenew.setScaling(false);
			edgesLayer.addStyle(labelStylenew);
			return edgesLayer;
		} else{
			JOptionPane.showMessageDialog(AppContext.getApplicationContext().getMainFrame(),
			"Error al leer Red del Fichero. No se ha encontrado ninguna Red en el sistema");
			return null;
		}

	}

	private double RedondearVelocidad(double speed)
	{
		try{
			return Math.round(speed*Math.pow(10,2))/Math.pow(10,2);
		}catch (Exception e) {
			return speed;
		}
	}

	public void initialize(PlugInContext context) throws Exception {

		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.leerreddelocal.language.RouteEngine_LeerRedLocali18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("leerredlocal",bundle);

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(
				this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEADMINISTRATION")).addPlugIn(
						this.getIcon(),
						this,
						createEnableCheck(context.getWorkbenchContext()),
						context.getWorkbenchContext());

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
						.add(checkFactory.createTaskWindowMustBeActiveCheck());
	}

	protected boolean leerGrafodeBase(PanelToLoadFromDataStore panel,
			TaskMonitor monitor, PlugInContext context) throws Exception {

		monitor.allowCancellationRequests();
		monitor.report("Cargando grafo de fichero local");
		HashMap<String, Object> netProperties = new HashMap<String, Object>();

		try {

			String localNetworkName = panel.getSubredSelected();

			String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
			File dir = new File(base,"networks");
			if(! dir.exists() ){
				dir.mkdirs();
			}

			String folderPath = dir.getPath();
			File networkDir = new File(folderPath,localNetworkName);
			if(! networkDir.exists() ){
				networkDir.mkdirs();
			}

			File networkFile = new File(networkDir.getPath(),localNetworkName);

			LocalRouteReaderWriter db = new LocalRouteReaderWriter(networkFile.getPath());
			graph = db.read();
				GeographicGraphGenerator graphGenerator = null;
			netProperties = db.readNetworkProperties();

			if (!graph.getEdges().isEmpty()){
				if (graph.getEdges().toArray()[0] instanceof LocalGISDynamicEdge){
					graphGenerator = new LocalGISGraphGenerator();
				} else if (graph.getEdges().toArray()[0] instanceof LocalGISDynamicEdge){
					graphGenerator = new LocalGISStreetGraphGenerator();
				}
			}

			db.setProperty(AbstractReaderWriter.GENERATOR, graphGenerator);


		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		/*
		 * Ahora aqui representaremos del grafo sus arcos
		 */
		monitor
		.report(I18N
				.get("jump.workbench.ui.plugin.datastore.AddDatastoreLayerPlugIn.Creating-layer"));

		createLayer(panel, context, netProperties);
		return true;

	}

	private boolean isStreetNetwork(String networkName) {
		boolean resultado = false;


		return resultado;
	}

	private int getIdNetwork(String networkName) {

		return 0;
	}

	protected boolean leeroguardarGrafoenBase(WithOutConnectionPanel panel,
			TaskMonitor monitor, PlugInContext context) throws Exception {
		return leerGrafodeBase((PanelToLoadFromDataStore) panel, monitor, context);
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(
				I18N.get("leerredlocal","routeengine.leerred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!saveRedLocalButtonAdded)
		{
			toolbox.addToolBar();
			LeerRedDeLocalPlugIn explode = new LeerRedDeLocalPlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			saveRedLocalButtonAdded = true;
		}
	}

	private OKCancelDialog getDialog(PlugInContext context) {
		dialog = createDialog(context);
		return dialog;
	}

	protected PanelToLoadFromDataLocalStore createPanel(PlugInContext context) {
		return new PanelToLoadFromDataLocalStore(context.getWorkbenchContext());
	}

	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub
		try {
			leeroguardarGrafoenBase((WithOutConnectionPanel) dialog
					.getCustomComponent(), monitor, context);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private OKCancelDialog createDialog(PlugInContext context) {

		WithOutConnectionPanel conPannel = createPanel(context);
		OKCancelDialog dialog = new OKCancelDialog(context.getWorkbenchFrame(),
				getName(),
				true,
				conPannel,
				new OKCancelDialog.Validator() {
			public String validateInput(Component component) {
				return ((WithOutConnectionPanel) component).validateInput();
			}
		});
		dialog.pack();
		GUIUtil.centreOnWindow(dialog);

		return dialog;
	}

}