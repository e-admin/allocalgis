package com.geopista.ui.plugin.routeenginetools.dibujarredplugin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.dynamic.DynamicEdge;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.phantom.EquivalentNode;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.ui.plugin.routeenginetools.dibujarredplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.RouteArrowLineStyle;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.PMRLocalGISStreetDynamicEdge;
import com.localgis.route.graph.structure.basic.ZebraDynamicEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

public class DibujarRedPlugIn extends ThreadedBasePlugIn implements ActionListener {

	private boolean drawRedButtonAdded = false ;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	JComboBox listaredes, listasubredes;
	MultiInputDialog dial;
	JCheckBox jchecknodos, jcheckarcos;
	PlugInContext context;
	Vector<String> subredSet;
	NetworkManager networkMgr;

	private ErrorHandler errorHandler;


	public boolean execute(PlugInContext context) throws Exception {

		this.taskFrame = (TaskComponent) context.getActiveTaskComponent();

		this.networkMgr = FuncionesAuxiliares.getNetworkManager(context);
		Set<String> redesSet = networkMgr.getNetworks().keySet();// obtener list de redes
		// ya calculadas
		subredSet = new Vector<String>();
		this.context = context;

		// Dialogo para dibujar una red
		dial = new MultiInputDialog(context.getWorkbenchFrame(),
				I18N.get("drawred","routeengine.drawred.plugintitle"),
				true);

		dial.setResizable(false);
		listaredes = dial.addComboBox(
				I18N.get("drawred","routeengine.drawred.redesfieldname")
				, null,
				redesSet,
				I18N.get("drawred","routeengine.drawred.redeslabel")
		);
		listaredes.addActionListener(this);


		listasubredes = dial.addComboBox(
				I18N.get("drawred","routeengine.drawred.subredesfieldname")
				, null,
				subredSet,
				I18N.get("drawred","routeengine.drawred.subredeslabel")
		);
		listasubredes.setEnabled(false);

		this.anniadirBotonResaltarAndZoomFeatures();

		jchecknodos = dial.addCheckBox(I18N.get("drawred","routeengine.drawred.nodosfieldname")
				, false);// añade una
		// opcion
		// para
		// seleccionar
		// o no una
		// capa
		jcheckarcos = dial.addCheckBox(I18N.get("drawred","routeengine.drawred.arcosfieldname")
				, false);// añade una
		// opcion
		// para
		// seleccionar
		// o no una
		// capa
		dial.setResizable(true);
		dial.setSize(400,900);

		// TODO Cambiar de dialogo... esto está mal hecho
		//
		((JPanel)((JPanel)((JLayeredPane)((JRootPane)dial.getComponents()[0]).
				getComponents()[1]).
				getComponents()[0]).
				getComponents()[2]).setVisible(false);

		dial.setVisible(true);

		if (dial.wasOKPressed()) {

	    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    	final PlugInContext runContext = this.context;
	    	progressDialog.setTitle("TaskMonitorDialog.Wait");
	        progressDialog.report(I18N.get("drawred","routeengine.drawred.taskdialogmessage"));
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


			return true;
		}
		dial.dispose();
		return false;
	}

	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
		// TODO Auto-generated method stub
			run(monitor, context);
	}

	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.dibujarredplugin.language.RouteEngine_DrawRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("drawred",bundle);

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
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}

	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();// getSource recoge qué objeto fue pulsado
		// cogemos el nombre de la red que es seleccionado
		if (obj.equals(listaredes)) {

			String redseleccionada = (String) listaredes.getSelectedItem();

			JComboBox list = dial.getComboBox(
					I18N.get("drawred","routeengine.drawred.subredesfieldname")
			);

			list.removeAllItems();
			Set<String> subredes = networkMgr.getNetwork(redseleccionada).getSubnetworks().keySet();

			for (Iterator<String> iterator = subredes.iterator(); iterator
			.hasNext();) {
				String nombre_subred = (String) iterator.next();
				list.addItem(nombre_subred);
			}
			listasubredes.setEnabled(true);
			dial.setSize(new Dimension(200, 200));
			dial.pack();
		}

	}

	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("drawred","routeengine.drawred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!drawRedButtonAdded )
		{
//			toolbox.addToolBar();
			DibujarRedPlugIn explode = new DibujarRedPlugIn();
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			drawRedButtonAdded = true;
		}
	}

	private String getLayerCategoryName(){
		return I18N.get("drawred","routeengine.drawred.categorylayername");
	}

	private String getLayerName(int layerCase){
		switch (layerCase) {
		case 0:
			// Caso para nodos int = 0
			return I18N.get("drawred","routeengine.drawred.nodelayername");
		case 1:
			// Caso para Arcos int = 1
			return I18N.get("drawred","routeengine.drawred.edglayername");
		default:
			return "";
		}
	}

	private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn =
		new ZoomToSelectedItemsPlugIn();
	private TaskComponent taskFrame;

	public void zoomSelectedFeatures() throws NoninvertibleTransformException {
		zoomToSelectedItemsPlugIn.zoom(
				FeatureUtil.toGeometries(selectedSubRedLayerFeatures()),
				taskFrame.getLayerViewPanel());
	}

	public void flashSelectedFeatures() throws NoninvertibleTransformException {
		zoomToSelectedItemsPlugIn.flash(
				FeatureUtil.toGeometries(selectedSubRedLayerFeatures()),
				taskFrame.getLayerViewPanel());
	}

	public boolean anniadirBotonResaltarAndZoomFeatures(){

		try{
			JPanel jpanel = new JPanel(new GridLayout());
			EnableableToolBar toolbarilla = new EnableableToolBar();

			JButton resaltarButton = new JButton();

			resaltarButton.setIcon(IconLoader.icon(I18N.get("drawred","routeengine.drawred.iconfileflash")));
			resaltarButton.setMargin(new Insets(0, 0, 0, 0));
			String internationalToolTip = aplicacion.getI18nString("Flash Selected Rows");
			resaltarButton.setToolTipText(internationalToolTip);
			resaltarButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						flashSelectedFeatures();
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});


			JButton zoomButton = new JButton();
			zoomButton.setIcon(IconLoader.icon(I18N.get("drawred","routeengine.drawred.iconfilezoom")));
			zoomButton.setMargin(new Insets(0, 0, 0, 0));
			String internationalzoomToolTip = aplicacion.getI18nString("Zoom Selected Rows");
			zoomButton.setToolTipText(internationalzoomToolTip);
			zoomButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						zoomSelectedFeatures();
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});

			jpanel.add(new JLabel(""),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 100, 0));
			jpanel.add(resaltarButton,
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 0, 0));

			jpanel.add(zoomButton,
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1,
							GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE,
							new Insets(0, 5, 0, 5), 0, 0));

			jpanel.setSize(200, 5);

			this.dial.addRow(jpanel);


		} catch (Exception e) {
			// TODO: handle exception
			// cualquier tipo de excepcion para pruebas, cambiar si corresponde
			e.printStackTrace();
			mostrarVentanaMensaje(I18N.get("drawred","routeengine.drawred.erroronflashbutton"), "");
			return false;
		}

		return true;
	}

	public Collection selectedSubRedLayerFeatures() {
		ArrayList selectedFeatures = new ArrayList();

		Layer selectedLayer = null;
		if (this.listaredes.getSelectedItem() != null && !this.listaredes.getSelectedItem().equals("") ) {
			if (this.listasubredes.getSelectedItem() != null && !this.listasubredes.getSelectedItem().equals("")){
				if ( ((String)this.listaredes.getSelectedItem()).equals("RedBaseDatos") ||
						((String)this.listaredes.getSelectedItem()).equals("RedLocal")){
					// La subred que se ha elegido es una subred cargada de la base de datos.
					// sólo habría que obtener la capa correspondiente y recorrer las features.

					selectedLayer = this.context.getLayerManager().getLayer((String) this.listasubredes.getSelectedItem());
					if (selectedLayer != null){
						Iterator it2 = selectedLayer.getFeatureCollectionWrapper().getFeatures().iterator();
						while (it2.hasNext()){
							selectedFeatures.add(it2.next());
						}
					}

				} else {

					// recorremos las capas y miramos si es una capa de particiones generada.
					Iterator<Layer> it3 = this.context.getLayerManager().getLayers().iterator();
					Layer actualLayer = null;
					while (it3.hasNext()){
						actualLayer = it3.next();
						if ( actualLayer != null){
							if (actualLayer.getName().equals("Particiones-"+listaredes.getSelectedItem())){
								// es una capa de 'Particiones-'
								// buscamos la feature correspondiente.
								Iterator<Feature> itFeatures = actualLayer.getFeatureCollectionWrapper().getFeatures().iterator();
								Feature feature = null;
								while (itFeatures.hasNext()){
									feature = itFeatures.next();
									if (feature.getString("nombreSubred").equals(listasubredes.getSelectedItem())){
										selectedFeatures.add(feature);
									}
								}
							}
						}
					}

				}
			}

		}
		// Como seleccionar las features....

		return selectedFeatures;
	}


	public boolean mostrarVentanaMensaje(String mensaje, String titulo){
		if (titulo == null){
			titulo = "";
		} if (mensaje == null){
			mensaje = "";
		}

		JOptionPane.showMessageDialog(this.aplicacion.getMainFrame(),
				mensaje,
				titulo,
				JOptionPane.WARNING_MESSAGE);

		return true;
	}

	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {
		// TODO Auto-generated method stub
		monitor.report("Dibujando Red");

		// Creo una colección de features para los nodos y otra para los
		// Arcos y defino
		// los atributos que deseo que tenga
		Graph g_subred = null;
		String red = "";
		if(listaredes != null){
			if (listaredes.getSelectedItem() != null){
				red = listaredes.getSelectedItem().toString();
			}
		}
		String subred = "";
		if (listasubredes != null){
			if (listasubredes.getSelectedItem() != null){
				subred = listasubredes.getSelectedItem().toString();
			}
		}
		if (!red.equals("")){
			if (!subred.equals("")){
				g_subred = networkMgr.getNetwork(red).getSubNetwork(subred).getGraph();
			} else{
				g_subred = networkMgr.getNetwork(red).getGraph();
			}
		}

		if (g_subred != null){
			Collection<Edge> edges = (Collection<Edge>) g_subred.getEdges();
			Collection<Node> nodes = (Collection<Node>) g_subred.getNodes();


			if (jchecknodos.isSelected() == true) {
				// Creo capas con los nodos
				FeatureCollection nodesFeatureCol = AddNewLayerPlugIn
				.createBlankFeatureCollection();
				nodesFeatureCol.getFeatureSchema().addAttribute("nodeId",AttributeType.INTEGER);
				nodesFeatureCol.getFeatureSchema().addAttribute("networkName",AttributeType.STRING);
				nodesFeatureCol.getFeatureSchema().addAttribute("nodeType",AttributeType.STRING);
				GeometryFactory fact = new GeometryFactory();

				for (Iterator<Node> iter_nodes = nodes.iterator(); iter_nodes
				.hasNext();) {
					Node node = (Node) iter_nodes.next();
					if (!(node instanceof EquivalentNode)){
						Coordinate coord = null;
						if (node instanceof GeographicNodeWithTurnImpedances){
							coord = ((GeographicNodeWithTurnImpedances)node).getCoordinate();
						} else{
							coord = ((XYNode) node).getCoordinate();
						}
						Point geom_nodes = fact.createPoint(coord);
						Feature feature = new BasicFeature(nodesFeatureCol
								.getFeatureSchema());
						feature.setGeometry(geom_nodes);
						feature.setAttribute("nodeId", new Integer(node.getID()));
						feature.setAttribute("networkName",subred);
						feature.setAttribute("nodeType",node.getClass().getName());
						nodesFeatureCol.add(feature);
					}
				}

				// Creo capas con los nodos
				Layer nodesLayer = context.addLayer(getLayerCategoryName(), getLayerName(0) + subred, nodesFeatureCol);
				LabelStyle labelStyle = new LabelStyle();
				labelStyle.setAttribute("nodeId");
				labelStyle.setColor(Color.black);
				labelStyle.setScaling(false);
				labelStyle.setEnabled(true);
				nodesLayer.addStyle(labelStyle);

			}

			if (jcheckarcos.isSelected() == true) {
				FeatureCollection edgesFeatureCol = null;

				GeometryFactory fact = new GeometryFactory();
				// creo capas con los arcos
				for (Iterator<Edge> iter_edges = edges.iterator(); iter_edges
				.hasNext();) {
					Edge edge = (Edge) iter_edges.next();

					if (edge instanceof EquivalentEdge){
						System.out.println("Equivalen Edge");
					}
					if (edge instanceof LocalGISStreetDynamicEdge){
						if (((LocalGISStreetDynamicEdge)edge).isCloned()){
							System.out.println("ESTA CLONADO");
						}
					}
					if (!(edge instanceof EquivalentEdge)){
						if ((edge instanceof LocalGISStreetDynamicEdge && !((LocalGISStreetDynamicEdge)edge).isCloned())
								|| edge instanceof LocalGISDynamicEdge){

							if (edgesFeatureCol == null){
								if (edge instanceof LocalGISDynamicEdge){
									edgesFeatureCol = EdgesFeatureCollections.getLocalGISDynamicEdgeFeatureCollection();
								}else if(edge instanceof LocalGISStreetDynamicEdge){
									edgesFeatureCol = EdgesFeatureCollections.getLocalGISStreetDynamicEdgeFeatureCollection();
								}else if (edge instanceof DynamicEdge) {
									edgesFeatureCol = EdgesFeatureCollections.getDynamicEdgeFeatureCollection();
								}else if (edge instanceof Edge){
									edgesFeatureCol = EdgesFeatureCollections.getEdgeFeatureCollection();
								}
								if(edge.getClass().getName().endsWith("PMRLocalGISStreetDynamicEdge")){
									edgesFeatureCol = EdgesFeatureCollections.getPMRLocalGISStreetDynamicEdgeFeatureCollection(edgesFeatureCol);
								}
								edgesFeatureCol.getFeatureSchema().addAttribute("networkName",
										AttributeType.STRING);
							}

							Coordinate[] coords = NodeUtils.CoordenadasArco(
									edge, null, 0, 0);
							Feature feature = new BasicFeature(edgesFeatureCol
									.getFeatureSchema());
							feature.setAttribute("idEje", new Integer(edge.getID()));
							//						feature.setAttribute("coste", new Double(
							//								((EdgeWithCost) edge).getCost()));
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
								feature.setAttribute("maxVelocidadNominal", RedondearVelocidad(((LocalGISStreetDynamicEdge) edge).getNominalMaxSpeed() * 3600 /1000));
								feature.setAttribute("pintadaRegulacionTrafico", 0);
							}
							if (edge instanceof PMRLocalGISStreetDynamicEdge){
								feature.setAttribute("anchuraAcera", ((PMRLocalGISStreetDynamicEdge) edge).getWidth());
								feature.setAttribute("pendienteTransversal", ((PMRLocalGISStreetDynamicEdge) edge).getTransversalSlope());
								feature.setAttribute("pendienteLongitudinal", ((PMRLocalGISStreetDynamicEdge) edge).getLongitudinalSlope());
								feature.setAttribute("tipoEje", ((PMRLocalGISStreetDynamicEdge) edge).getsEdgeType());
								feature.setAttribute("alturaObstaculo", ((PMRLocalGISStreetDynamicEdge) edge).getObstacleHeight());
								if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.LEFT)
									feature.setAttribute("ladoAcera", "L");
								else if (((PMRLocalGISStreetDynamicEdge) edge).getCalculatedSide() == PMRLocalGISStreetDynamicEdge.RIGHT)
									feature.setAttribute("ladoAcera", "R");
								feature.setAttribute("ejeRelacionadoConId", ((PMRLocalGISStreetDynamicEdge) edge).getRelatedToId());
								if (edge instanceof ZebraDynamicEdge){
									feature.setAttribute("tipoPasoCebra", ((ZebraDynamicEdge) edge).getsType());
								}
							}


							if (!subred.equals("")){
								feature.setAttribute("networkName", subred);
							}
							else{
								feature.setAttribute("networkName", red);
							}

							LineString geom_edge = fact.createLineString(coords);
							feature.setGeometry(geom_edge);
							edgesFeatureCol.add(feature);
						}
					}
				}
//				Layer edgesLayer = context.addLayer(
//						getLayerCategoryName(),
//						getLayerName(1) + subred, edgesFeatureCol);
				Layer edgesLayer = context.getLayerViewPanel().getLayerManager().addLayer(
						getLayerCategoryName(),
						getLayerName(1) + subred, edgesFeatureCol);

//				RouteArrowLineStyle routeStyle = new RouteArrowLineStyle("name",
//						ArrowIconLoader.biDirectArrowIcon(), 10,
//		                10, true);
//				edgesLayer.addStyle(routeStyle);

				RouteArrowLineStyle.BiDirect flecha = new RouteArrowLineStyle.BiDirect(
						context.getLayerViewPanel().getViewport(),
						(Graphics2D) context.getLayerViewPanel().getGraphics());
				flecha.getIcon();
				edgesLayer.addStyle(flecha);

//				RouteArrowLineStyle bs = new RouteArrowLineStyle();
//				bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
//				bs.setLineWidth(4);
//				bs.setEnabled(true);
//				RouteArrowLineStyle labelStyle = new RouteArrowLineStyle();
//				labelStyle.setAttribute(ArrowIconLoader.biDirectArrowIcon());
//				labelStyle.setColor(Color.red);
//				labelStyle.setScaling(false);
//				labelStyle.setEnabled(true);
//				edgesLayer.addStyle(labelStyle);
			}// fin jcheckarcos

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

}// fin de clase
