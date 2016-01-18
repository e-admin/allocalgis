/**
 * DibujarRedPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.dibujarredplugin;

import java.awt.Color;
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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.line.XYNode;
import org.uva.route.graph.structure.EdgeWithCost;
import org.uva.route.graph.structure.geographic.GeographicNodeWithTurnImpedances;
import org.uva.route.graph.structure.phantom.EquivalentNode;
import org.uva.route.graph.structure.phantom.basic.EquivalentEdge;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.util.NodeUtils;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.ui.plugin.routeenginetools.CombinedSchemaCalculator;
import com.geopista.ui.plugin.routeenginetools.dibujarredplugin.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.EdgesFeatureCollections;
import com.geopista.ui.plugin.routeenginetools.routeutil.MiEnableCheckFactory;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilToolsCore;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.routeutil.PanelToSelectMemoryNetworks;
import com.geopista.ui.plugin.routeenginetools.routeutil.routelabelstyle.RouteArrowLineStyle;
import com.geopista.ui.plugin.toolboxnetwork.GeopistaNetworkEditingPlugIn;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
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
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.plugin.AddNewLayerPlugIn;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

import edu.emory.mathcs.backport.java.util.Arrays;

public class DibujarRedPlugIn extends ThreadedBasePlugIn  {

	private boolean drawRedButtonAdded = false ;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    static final Logger LOG = Logger.getLogger(DibujarRedPlugIn.class);
    private static final String SOURCE_NETWORK_FIELDNAME = "SourceNetwork";
	JComboBox listaredes, listasubredes;
	MultiInputDialog dial;
	JCheckBox jchecknodos, jcheckarcos;
	protected PlugInContext context;
	Vector<String> subredSet;
	NetworkManager networkMgr;

	private ErrorHandler errorHandler;


	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.plugin.AbstractPlugIn#execute(com.vividsolutions.jump.workbench.plugin.PlugInContext)
	 */
	public boolean execute(PlugInContext context) throws Exception {

		this.taskFrame = (TaskComponent) context.getActiveTaskComponent();
		this.context=context;
		this.networkMgr = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Set<String> redesSet = networkMgr.getNetworks().keySet();// obtener list de redes
	if (redesSet.isEmpty())
	    {
		context.getWorkbenchFrame().warnUser("No hay redes cargadas.");
		return false;
	    }
		// ya calculadas
		subredSet = new Vector<String>();
		

		// Dialogo para dibujar una red
		dial = new MultiInputDialog(context.getWorkbenchFrame(),
				I18N.get("drawred","routeengine.drawred.plugintitle"),
				true);
		
		dial.addRow(SOURCE_NETWORK_FIELDNAME,new JLabel(I18N.get("drawred","routeengine.drawred.redesfieldname")),new PanelToSelectMemoryNetworks(context),null,I18N.get("drawred","routeengine.drawred.redeslabel"));
		dial.addEnableChecks(SOURCE_NETWORK_FIELDNAME, Arrays.asList(new EnableCheck[]{NetworkModuleUtilToolsCore.createEnableNetworkSelectedCheck(dial,SOURCE_NETWORK_FIELDNAME)}));
		dial.setSideBarDescription(getDescription());
		

		//this.anniadirBotonResaltarAndZoomFeatures();

		String checkNodeFieldName = I18N.get("drawred","routeengine.drawred.nodosfieldname");
		jchecknodos = dial.addCheckBox(checkNodeFieldName, false);// aÃ±ade una opcion para  seleccionar o no una capa
		String checkEdgesFieldName = I18N.get("drawred","routeengine.drawred.arcosfieldname");
		jcheckarcos = dial.addCheckBox(checkEdgesFieldName, false);// aÃ±ade una opcion para  seleccionar o no una capa
		final JCheckBox checkNode=jchecknodos;
		final JCheckBox checkEdge=jcheckarcos;
		
		EnableCheck[] bothNotFalseEnableCheck = new EnableCheck[]{new EnableCheck() {
			
			@Override
			public String check(JComponent component) 
			{
				if (!checkNode.isSelected() && !checkEdge.isSelected())
					return "Debe seleccionar el tipo de elementos del grafo que se quiere transformar en capa.";
				else
					return null;
			}
			}
		};

		dial.addEnableChecks(checkEdgesFieldName, Arrays.asList(bothNotFalseEnableCheck));

//		dial.setResizable(true);
//		dial.setSize(400,900);

		// TODO Cambiar de dialogo... esto estï¿½ mal hecho
		//
//		((JPanel)((JPanel)((JLayeredPane)((JRootPane)dial.getComponents()[0]).
//				getComponents()[1]).
//				getComponents()[0]).
//				getComponents()[2]).setVisible(false);

		dial.setVisible(true);

		if (dial.wasOKPressed()) {

	    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    	final PlugInContext runContext =context;
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

	private String getDescription()
	{
	    return "Genera un esquema del modelo de grafo. ¡ATENCIÓN! Es únicamente una representación para analizar el modelo del grafo." +
	    		" Cualquier modificación no tiene una repercusión en el grafo a no ser que esté activada la herramienta Feature->Arco." +
	    		" La geometría se calcula utilizando la mejor información disponible en el escritorio pero puede aparecer simplificada.";
	}

	private void withTaskMonitorDo(TaskMonitorDialog monitor, PlugInContext context) throws Exception {
			run(monitor, context);
	}

	public void initialize(PlugInContext context) throws Exception {
		Locale loc=I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.dibujarredplugin.language.RouteEngine_DrawRedi18n",loc,this.getClass().getClassLoader());
		I18N.plugInsResourceBundle.put("drawred",bundle);

		GeopistaNetworkEditingPlugIn geopistaNetworkEditingPlugIn = (GeopistaNetworkEditingPlugIn) (context.getWorkbenchContext().getBlackboard().get(GeopistaNetworkEditingPlugIn.KEY));
		geopistaNetworkEditingPlugIn.addAditionalPlugIn(this);

	}

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
		MiEnableCheckFactory checkFactory = new MiEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createNetworksMustBeLoadedCheck())
	// .add(checkFactory.createAtLeastNLayersMustExistCheck(1)) // BUG No es necesario
	;
	}

//	public void actionPerformed(ActionEvent e) {
//
//		Object obj = e.getSource();// getSource recoge quï¿½ objeto fue pulsado
//		// cogemos el nombre de la red que es seleccionado
//		if (obj.equals(listaredes)) {
//
//			String redseleccionada = (String) listaredes.getSelectedItem();
//
//			JComboBox list = dial.getComboBox(
//					I18N.get("drawred","routeengine.drawred.subredesfieldname")
//			);
//
//			list.removeAllItems();
//			Set<String> subredes = networkMgr.getNetwork(redseleccionada).getSubnetworks().keySet();
//
//			for (Iterator<String> iterator = subredes.iterator(); iterator
//			.hasNext();) {
//				String nombre_subred = (String) iterator.next();
//				list.addItem(nombre_subred);
//			}
//			listasubredes.setEnabled(true);
//			dial.setSize(new Dimension(200, 200));
//			dial.pack();
//		}
//
//	}

	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("drawred","routeengine.drawred.iconfile"));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!drawRedButtonAdded )
		{
//			toolbox.addToolBar();
//			DibujarRedPlugIn explode = new DibujarRedPlugIn();
			toolbox.addPlugIn(this, null, this.getIcon());
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
					// sï¿½lo habrï¿½a que obtener la capa correspondiente y recorrer las features.

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

		// Creo una coleccion de features para los nodos y otra para los
		// Arcos y defino
		// los atributos que deseo que tenga
		PanelToSelectMemoryNetworks sourceNetField = (PanelToSelectMemoryNetworks)dial.getComponent(SOURCE_NETWORK_FIELDNAME);
//		String subred=sourceNetField.getSubredseleccionada();
//		String red=sourceNetField.getRedSeleccionada();
		
//		Network net=NetworkModuleUtil.getSubNetwork(networkMgr, red, subred);
		Network net=sourceNetField.getSelectedNetwork();

		Graph g_subred = net.getGraph();
		

		if (g_subred != null){
			Collection<Edge> edges = (Collection<Edge>) g_subred.getEdges();
			Collection<Node> nodes = (Collection<Node>) g_subred.getNodes();

		if (jchecknodos.isSelected() == true)
		    {
			createNodesLayer(context, net.getName(), nodes);
		    }

		if (jcheckarcos.isSelected() == true)
		    {
			createEdgesLayer(context, net.getName(), edges);
		    }// fin jcheckarcos
	    }
    }



    public void createNodesLayer(PlugInContext context, String netName, Collection<Node> nodes)
    {
	// Creo capas con los nodos
	FeatureCollection nodesFeatureCol = AddNewLayerPlugIn.createBlankFeatureCollection();
	nodesFeatureCol.getFeatureSchema().addAttribute("nodeId", AttributeType.INTEGER);
	nodesFeatureCol.getFeatureSchema().addAttribute("networkName", AttributeType.STRING);
	nodesFeatureCol.getFeatureSchema().addAttribute("nodeType", AttributeType.STRING);
	GeometryFactory fact = new GeometryFactory();

	for (Iterator<Node> iter_nodes = nodes.iterator(); iter_nodes.hasNext();)
	    {
		Node node = (Node) iter_nodes.next();
		if (!(node instanceof EquivalentNode))
		    {
			Coordinate coord = null;
			if (node instanceof GeographicNodeWithTurnImpedances)
			    {
				coord = ((GeographicNodeWithTurnImpedances) node).getCoordinate();
			    } else
			    {
				coord = ((XYNode) node).getCoordinate();
			    }
			Point geom_nodes = fact.createPoint(coord);
			Feature feature = new BasicFeature(nodesFeatureCol.getFeatureSchema());
			feature.setGeometry(geom_nodes);
			feature.setAttribute("nodeId", new Integer(node.getID()));
			feature.setAttribute("networkName", netName);
			feature.setAttribute("nodeType", node.getClass().getName());
			nodesFeatureCol.add(feature);
		    }
	    }

	// Creo capa con los nodos
	Layer nodesLayer = context.addLayer(getLayerCategoryName(), getLayerName(0) + netName, nodesFeatureCol);
	LabelStyle labelStyle = new LabelStyle();
	labelStyle.setAttribute("nodeId");
	labelStyle.setColor(Color.black);
	labelStyle.setScaling(false);
	labelStyle.setEnabled(true);
	nodesLayer.addStyle(labelStyle);
    }

    public void createEdgesLayer(PlugInContext context, String netName, Collection<Edge> edges)
    {
// TODO utilizar NetworkModuleUtil.createGraphLayer  para reutilización
	FeatureCollection edgesFeatureCol = getEdgeFeatures(netName, edges, null, context);
	// Layer edgesLayer = context.addLayer(
	// getLayerCategoryName(),
	// getLayerName(1) + subred, edgesFeatureCol);
	Layer edgesLayer = context.getLayerViewPanel().getLayerManager().addLayer(getLayerCategoryName(), getLayerName(1) + "-"+ netName +"(Repr.Grafo)", edgesFeatureCol);

	// RouteArrowLineStyle routeStyle = new RouteArrowLineStyle("name",
	// ArrowIconLoader.biDirectArrowIcon(), 10,
	// 10, true);
	// edgesLayer.addStyle(routeStyle);

	RouteArrowLineStyle.BiDirect flecha = new RouteArrowLineStyle.BiDirect((Viewport)context.getLayerViewPanel().getViewport(), (Graphics2D) ((LayerViewPanel)context
		.getLayerViewPanel()).getGraphics());
	flecha.getIcon();
	edgesLayer.addStyle(flecha);

	// RouteArrowLineStyle bs = new RouteArrowLineStyle();
	// bs.setLineColor(edgesLayer.getBasicStyle().getLineColor());
	// bs.setLineWidth(4);
	// bs.setEnabled(true);
	// RouteArrowLineStyle labelStyle = new RouteArrowLineStyle();
	// labelStyle.setAttribute(ArrowIconLoader.biDirectArrowIcon());
	// labelStyle.setColor(Color.red);
	// labelStyle.setScaling(false);
	// labelStyle.setEnabled(true);
	// edgesLayer.addStyle(labelStyle);

    }

    public FeatureCollection getEdgeFeatures(String netName, Collection<Edge> edges, FeatureCollection addTo, PlugInContext context)
    {

	// creo capas con los arcos
	CombinedSchemaCalculator schema = new CombinedSchemaCalculator();
	FeatureCollection edgesFeatureCol = schema.getUpdatedFeatureSchema(addTo, null);

	for (Iterator<Edge> iter_edges = edges.iterator(); iter_edges.hasNext();)
	    {
		Edge edge = (Edge) iter_edges.next();
		edge = (Edge) NodeUtils.unwrapProxies(edge);
		if (LOG.isDebugEnabled())
		    {
			if (edge instanceof EquivalentEdge)
			    {
				LOG.debug("Equivalent Edge:" + edge);
			    }
			if (edge instanceof LocalGISStreetDynamicEdge)
			    {
				if (((LocalGISStreetDynamicEdge) edge).isCloned())
				    {
					LOG.debug("Edge " + edge + " ESTA CLONADO");
				    }
			    }
		    }

		if (!(edge instanceof EquivalentEdge))
		    {
			if ((edge instanceof LocalGISStreetDynamicEdge && !((LocalGISStreetDynamicEdge) edge).isCloned()) || edge instanceof ILocalGISEdge)
			    {
				// FIX Modificado para que se complementen los esquemas si les faltan atributos en casos de grafos con elementos variados
				edgesFeatureCol = schema.getUpdatedFeatureSchema(edgesFeatureCol, edge);

				Feature feature = new BasicFeature(edgesFeatureCol.getFeatureSchema());
				feature.setAttribute("idEje", new Integer(edge.getID()));
				if (edge instanceof EdgeWithCost)
				    feature.setAttribute("coste", new Double(((EdgeWithCost) edge).getCost()));
				feature.setAttribute("idNodoA", edge.getNodeA().getID());
				feature.setAttribute("idNodoB", edge.getNodeB().getID());
				
				feature.setAttribute("networkName", netName);
				   
				// rellena la feature con los atributos adecuados para el edge
				EdgesFeatureCollections.copyAttributeValues(edge, feature);
				Geometry geom_edge = NetworkModuleUtilWorkbench.getBestGeometryFromEdgeLocal(edge, context);

				feature.setGeometry(geom_edge);
				edgesFeatureCol.add(feature);
				// JPC: Deshabilitado. La capa original es la que debe tener las geometrías.
				// apunta el id de feature en el edge para que pueda usarse en el resto de procesados
				// if (edge instanceof ILocalGISEdge)
				// {
				// ILocalGISEdge lgEdge = (ILocalGISEdge) edge;
				// lgEdge.setFeature(feature);
				// lgEdge.setIdFeature(feature.getID());
				// }
			    }
		    }
	    }
	return edgesFeatureCol;
    }

}// fin de clase
