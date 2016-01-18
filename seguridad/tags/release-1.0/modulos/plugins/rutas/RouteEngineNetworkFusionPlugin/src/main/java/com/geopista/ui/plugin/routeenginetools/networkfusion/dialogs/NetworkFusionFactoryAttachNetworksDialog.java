package com.geopista.ui.plugin.routeenginetools.networkfusion.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Graph;
import org.uva.geotools.graph.structure.Node;
import org.uva.geotools.graph.structure.basic.BasicGraph;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.graph.build.UIDgenerator.UniqueIDGenerator;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.routeserver.managers.SpatialAllInMemoryExternalSourceMemoryManager;
import org.uva.routeserver.street.Incident;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.networkfusion.beans.NetworkBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalGISStreetRouteReaderWriter;
import com.localgis.route.graph.structure.basic.GraphType;
import com.localgis.route.graph.structure.basic.LocalGISDynamicEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.network.LocalGISNetworkManager;
import com.localgis.route.network.NetworkProperty;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class NetworkFusionFactoryAttachNetworksDialog extends JPanel implements WizardPanel
{
	//Objetos para fusion de redes
	private ArrayList<NetworkBean> networksToFusion;
	// objetos generales
	private WizardContext wizardContext;
	
	// objetos visuales
	
	private JPanel jPanelInfo;
	private JPanel jPanelData;
	private JComboBox jComboBoxNetworkName;
	private JRadioButton jButtonSelectCombo;
	private JRadioButton jButtonSelectTextArea;
	private JTextField jTextfieldNetworkName;
	private PlugInContext context;
	private static AppContext app = (AppContext) AppContext.getApplicationContext();
	public TaskMonitorDialog progressDialog;
    private DynamicGraph graph;
    private Network parentNetworkToFusion;
    private NetworkBean actualNetworkBean;
    public NetworkFusionFactoryAttachNetworksDialog(PlugInContext context) {
    	this.context = context; 
    	initialize();
    }
    private void buildNetworkNameSelector(){
    	jComboBoxNetworkName = new JComboBox();
    	
    }
    private JRadioButton buildRadioButton(boolean isSelected,String name){
    	JRadioButton button = new JRadioButton();
    	button.setEnabled(true);
    	button.setText(name);
    	button.setSelected(isSelected);
    	return button;
    }
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
    	
    	buildNetworkNameSelector();
    	jComboBoxNetworkName.setEnabled(false);
    	jButtonSelectCombo = buildRadioButton(false, I18N.get("networkFusion","routeengine.networkfusion.selectParentNetworkToFusion"));
    	jButtonSelectCombo.addItemListener(new ItemListener(){
    		
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					jComboBoxNetworkName.setEnabled(true);
			    } else {
			    	jComboBoxNetworkName.setEnabled(false);
			    }
				wizardContext.inputChanged();
			}
    		
    	});
    	jButtonSelectTextArea = buildRadioButton(true, I18N.get("networkFusion","routeengine.networkfusion.buildNewNetwork"));
    	jButtonSelectTextArea.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					jTextfieldNetworkName.setEnabled(true);
			    } else {
			    	jTextfieldNetworkName.setEnabled(false);
			    }
				wizardContext.inputChanged();
			}
    		
    	});
    	ButtonGroup buttonGroupOption = new ButtonGroup();
    	buttonGroupOption.add(jButtonSelectCombo);
    	buttonGroupOption.add(jButtonSelectTextArea);
    	jTextfieldNetworkName = new JTextField(30);
    	jTextfieldNetworkName.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				wizardContext.inputChanged();
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				wizardContext.inputChanged();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				wizardContext.inputChanged();
				
			}});
    	jTextfieldNetworkName.setEnabled(true);
    	this.setLayout(new GridBagLayout());
    	this.setSize(new Dimension(600, 550));
    	this.setPreferredSize(new Dimension(600, 550));
    	       
    	
    	this.add(getJPanelInfo(), 
				new GridBagConstraints(0,0,1,1, 1, 0.05,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));

		this.add(getJPanelDatos(), 
				new GridBagConstraints(0,1,1,1, 1, 0.95,GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(5,5,5,5),0,0));
    		
    }
    private Component getJPanelDatos() {   	
    	
    	if (jPanelData == null){
    		
    		jPanelData   = new JPanel();

    		jPanelData.setLayout(new GridBagLayout());
    		
    		jPanelData.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkFusion","routeengine.networkfusion.attachPanelData"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

    		jPanelData.add(jButtonSelectCombo, 
    				new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,60,0,0),0,0));

    		jPanelData.add(jComboBoxNetworkName, 
    				new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(0,0,0,60),0,0));
            
    		jPanelData.add(jButtonSelectTextArea, 
    				new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.BOTH, new Insets(0,60,0,0),0,0));

    		jPanelData.add(jTextfieldNetworkName, 
    				new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.CENTER,
    						GridBagConstraints.HORIZONTAL, new Insets(0,0,0,60),0,0));

    		
    	}
    	return jPanelData;
	}
	private Component getJPanelInfo() {
		if (jPanelInfo == null){

			jPanelInfo    = new JPanel();
			jPanelInfo.setBorder(BorderFactory.createTitledBorder
					(null,I18N.get("networkFusion","routeengine.networkfusion.borderInfo"), 
							TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12),Color.BLUE));

			JTextArea jTextAreaInfo = new JTextArea(I18N.get("networkFusion","routeengine.networkfusion.textAreaFusionInfo"));
			jTextAreaInfo.setEnabled(false);
			jTextAreaInfo.setFont(new JLabel().getFont());
			jTextAreaInfo.setOpaque(false);
			jTextAreaInfo.setDisabledTextColor(Color.black);
			jTextAreaInfo.setEditable(false);
			jTextAreaInfo.setWrapStyleWord(true);
			jPanelInfo.add(jTextAreaInfo);
		}
		return jPanelInfo;
	}

	/**
     * Called when the user presses Next on this panel's previous panel
     */
    public void enteredFromLeft(Map dataMap)
    { 
    	jComboBoxNetworkName.removeAllItems();
    	networksToFusion = (ArrayList<NetworkBean>) wizardContext.getData("networksToFusion");
    	Iterator<NetworkBean> it = networksToFusion.iterator();
    	while(it.hasNext()){
    		NetworkBean network = it.next();
    		jComboBoxNetworkName.addItem(network.getNetworkName());
    	}
    }
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {     
    	
        String finalNetworkName = ""; 
        Hashtable<String,NetworkBean> networksMap = new Hashtable<String,NetworkBean>();
        final Map<String,Object> finalProperties = new HashMap<String,Object>();
    	Iterator<NetworkBean> it = networksToFusion.iterator();
    	while(it.hasNext()){
    		NetworkBean actualNetwork = it.next();
    		networksMap.put(actualNetwork.getNetworkName(), actualNetwork);
    	}
        if(jComboBoxNetworkName.isEnabled()){
        	// Fusionar a esta network
        	System.out.println("Fusionando a base de datos");
        	int i = JOptionPane.showConfirmDialog(this,I18N.get("networkFusion","routeengine.networkfusion.closeattachednetworks"),
        			I18N.get("networkFusion","routeengine.networkfusion.confirm"),                          
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        	if(i == 0)
        		exiting();
        	//}
        	finalNetworkName = (String)jComboBoxNetworkName.getSelectedItem();
        	NetworkBean parentNetwork = networksMap.get(finalNetworkName);
        	if(parentNetwork.getgType().equals(GraphType.GENERIC)){
        		// Mensaje. Los datos de callejero no se anexarán a la red final
        	}else{
        		Iterator<NetworkBean> networksToFusionIterator = networksToFusion.iterator();
        		while(networksToFusionIterator.hasNext()){
        			NetworkBean nBean = networksToFusionIterator.next();
        			fusionProperties(nBean.getNetwork().getProperties(),finalProperties);
        			if(nBean.getgType().equals(GraphType.GENERIC)){
        				JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),
        						I18N.get("networkFusion","routeengine.networkfusion.incompatibleFusion"),
                                context.getWorkbenchGuiComponent().getTitle(),
                                JOptionPane.ERROR_MESSAGE);
        				return;
        			}
        		}
        		replaceProperties(finalProperties,parentNetwork.getNetwork());
        	}
        	parentNetworkToFusion = parentNetwork.getNetwork();
        	if(parentNetwork.getNetwork().getGraph() instanceof BasicGraph || 
        			(parentNetwork.getNetwork().getGraph() instanceof DynamicGraph && 
        				!(((DynamicGraph)parentNetwork.getNetwork().getGraph()).getMemoryManager() instanceof SpatialAllInMemoryExternalSourceMemoryManager))){
        		Iterator<NetworkBean> networksToFusionIterator = networksToFusion.iterator();
        		while(networksToFusionIterator.hasNext()){
        			NetworkBean actualNetworkBean = networksToFusionIterator.next();
        			// Para un basic graph, solo se pueden adjuntar los edges y los nodes nuevos. pero no creo que sea efectivo.
        			if(!actualNetworkBean.equals(parentNetwork)){
        				((BasicGraph)parentNetwork.getNetwork().getGraph()).setEdges(actualNetworkBean.getNetwork().getGraph().getEdges());
        				((BasicGraph)parentNetwork.getNetwork().getGraph()).setNodes(actualNetworkBean.getNetwork().getGraph().getNodes());
        			}
        		}
        	}else{
        		graph = (DynamicGraph) parentNetwork.getNetwork().getGraph();
        		Iterator<NetworkBean> networksToFusionIterator = networksToFusion.iterator();
        		Map finalNetworkProperties = parentNetwork.getNetwork().getProperties();
        		while(networksToFusionIterator.hasNext()){
        			actualNetworkBean = networksToFusionIterator.next();
        			//TODO: Fusionar las network properties para guardarlaso o lo que sea
        			if(!actualNetworkBean.equals(parentNetwork)){
        				try {
        					progressDialog = new TaskMonitorDialog(app.getMainFrame(),
        			                context.getErrorHandler());
        			        progressDialog.setTitle(I18N.get("networkFusion","routeengine.networkfusion.fusionOp"));
        			        progressDialog.report(I18N.get("networkFusion","routeengine.networkfusion.fusionMsg") + actualNetworkBean.getNetworkName());
        			        progressDialog.addComponentListener(new ComponentAdapter() {
        			            public void componentShown(ComponentEvent e) {
        			                //Wait for the dialog to appear before starting the task. Otherwise
        			                //the task might possibly finish before the dialog appeared and the
        			                //dialog would never close. [Jon Aquino]
        			                new Thread(new Runnable(){
        			                    public void run()
        			                    {
        			                    	Connection connection = null;
        			                        try
        			                        {
        			                        	RouteConnectionFactory connFactory = new GeopistaRouteConnectionFactoryImpl();
        			                        	connection = connFactory.getConnection();
        			                        	reindexGraph(actualNetworkBean.getNetwork().getGraph(),connection);
        			                        	graph.getMemoryManager().appendGraph(actualNetworkBean.getNetwork().getGraph());
        			                        	commitChanges(actualNetworkBean.getNetwork(),parentNetworkToFusion,connection);
        			                        	closeNetwork(actualNetworkBean.getNetwork());
        			                        }catch(Exception e){
        			                        	e.printStackTrace();
        			                        
        			                        }finally
        			                        {
        			                        	ConnectionUtilities.closeConnection(connection);
        			                            progressDialog.setVisible(false);
        			                        } 
        			                    }

										private void closeNetwork(Network network) {
											LocalGISNetworkManager nManager = (LocalGISNetworkManager)FuncionesAuxiliares.getNetworkManager(context);
											nManager.detachNetwork(network.getName());
										}

										private void commitChanges(Network networkToAttach,Network networkParent,Connection connection) throws Exception {
											LocalGISNetworkDAO nDAO = new LocalGISNetworkDAO();
											if(((SpatialAllInMemoryExternalSourceMemoryManager)((DynamicGraph)networkToAttach.getGraph()).getMemoryManager()).getStore() instanceof LocalGISStreetRouteReaderWriter)
												nDAO.write(networkToAttach.getGraph(),networkParent.getName(),true, connection);
											else
												nDAO.write(networkToAttach.getGraph(),networkParent.getName(),false, connection);
										}

										private void reindexGraph(Graph graph,Connection connection) throws SQLException {
											ArrayList<Node> nodes = new ArrayList<Node>(graph.getNodes());
											ArrayList<Edge> edges = new ArrayList<Edge>(graph.getEdges());
											Iterator<Node> itNodes = nodes.iterator();
											while(itNodes.hasNext()){
												rebuildNode(itNodes.next(),connection);
											}
											Iterator<Edge> itEdges = edges.iterator();
											while(itEdges.hasNext()){
												rebuildEdge(itEdges.next(), connection);
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
        			                }).start();
        			            }
        			        });
        			        GUIUtil.centreOnWindow(progressDialog);
        			        progressDialog.setVisible(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        		}
        	}
        }
        else{
        	System.out.println("Fusionando a dinámica");
        	NetworkManager nman = FuncionesAuxiliares.getNetworkManager(context);
        	
    		if(nman.getNetwork("dynamic") != null){
    			BasicNetwork container = (BasicNetwork)nman.getNetwork("dynamic");
    			Map<String,Network> subnetworks = container.getSubnetworks();
    			if(subnetworks.get(finalNetworkName) != null){
    				JOptionPane.showMessageDialog(
    						(Component) context.getWorkbenchGuiComponent(),
    						I18N.get("networkFusion","routeengine.networkfusion.networkAlreadyExists" + finalNetworkName),
                            context.getWorkbenchGuiComponent().getTitle(),
                            JOptionPane.ERROR_MESSAGE);
    				//TODO: Cancelar la salida, no salir sin hacer nada.
    				return;
    			}
    			
    		}
    		Iterator<NetworkBean> networksToFusionIterator = networksToFusion.iterator();
    		
    		while(networksToFusionIterator.hasNext()){
    			NetworkBean nBean = networksToFusionIterator.next();
    			fusionProperties(nBean.getNetwork().getProperties(),finalProperties);
    			if(nBean.getgType().equals(GraphType.GENERIC)){
    				JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),
    						I18N.get("networkFusion","routeengine.networkfusion.incompatibleFusion"),
                            context.getWorkbenchGuiComponent().getTitle(),
                            JOptionPane.ERROR_MESSAGE);
    				return;
    			}
    		}
    			
        	try {

        		parentNetworkToFusion = new BasicNetwork(jTextfieldNetworkName.getText());
				progressDialog = new TaskMonitorDialog(app.getMainFrame(),
		                context.getErrorHandler());
		        progressDialog.setTitle(I18N.get("networkFusion","routeengine.networkfusion.fusionOp"));
		        progressDialog.report(I18N.get("networkFusion","routeengine.networkfusion.fusionOp"));
		        progressDialog.addComponentListener(new ComponentAdapter() {
		            public void componentShown(ComponentEvent e) {
		                //Wait for the dialog to appear before starting the task. Otherwise
		                //the task might possibly finish before the dialog appeared and the
		                //dialog would never close. [Jon Aquino]
		                new Thread(new Runnable(){
		                    public void run()
		                    {
		                        try
		                        {
		                        	Map<String,Object> properties = null;
						        	Iterator<NetworkBean> networksToFusionIterator = networksToFusion.iterator();
						        	ArrayList nodeList = new ArrayList();
						        	ArrayList edgeList = new ArrayList();
						        	//Graph graph = null;
						        	GraphType finalType = GraphType.STREET;
						    		while(networksToFusionIterator.hasNext()){
						    			NetworkBean actualNetworkBean = networksToFusionIterator.next();
						    			if(actualNetworkBean.getgType().equals(GraphType.GENERIC)){
						    				finalType = actualNetworkBean.getgType();
						    			}
						    			//actualNetworkBean.getNetwork().getGraph().getMemoryManager().appendGraph(actualNetworkBean.getNetwork().getGraph());
						    			nodeList.addAll(actualNetworkBean.getNetwork().getGraph().getNodes());
						    			edgeList.addAll(actualNetworkBean.getNetwork().getGraph().getEdges());
						    			actualNetworkBean.getNetwork().getProperties();
						    		}
						    		
						    		Hashtable htEdges = new Hashtable();
						    		Hashtable htNodes = new Hashtable();
						    		ArrayList finalListEdges = new ArrayList();
						    		ArrayList finalListNodes = new ArrayList();
						    		Iterator itEdges = edgeList.iterator();
						    		while(itEdges.hasNext()){
						    			Edge edge = (Edge)itEdges.next();
						    			if(finalType.equals(GraphType.GENERIC) && edge instanceof LocalGISStreetDynamicEdge){
						    				LocalGISStreetDynamicEdge dEdge = (LocalGISStreetDynamicEdge) edge;
						    				//TODO: Comprobar si es necesario usar otro id generator
						    				UniqueIDGenerator idGen = new SequenceUIDGenerator();
						    				LocalGISDynamicEdge newEdge = new LocalGISDynamicEdge(dEdge.getNodeA(), dEdge.getNodeB(),idGen);
						    				newEdge.setEdgeLength(dEdge.getEdgeLength());
						    				newEdge.setIdFeature(dEdge.getIdFeature());
						    				newEdge.setIdLayer(dEdge.getIdLayer());
						    				//TODO COmprobar las impedancias, antes se obtenian y ponian con from,to... y ahora sólo necesita from.
						    				newEdge.setImpedanceAToB(dEdge.getImpedance(dEdge.getNodeA()));
						    				newEdge.setImpedanceBToA(dEdge.getImpedance(dEdge.getNodeB()));
						    				Set key = dEdge.getIncidents();
						    				Iterator itKey = key.iterator();
						    				while(itKey.hasNext()){
						    					newEdge.putIncident((Incident)itKey.next());
						    				}
						    			}else{
						    				htEdges.put(Integer.toString(edge.getID()), edge);
						    			}
						    		}
						    		Iterator itNodes = nodeList.iterator();
						    		while(itNodes.hasNext()){
						    			Node node = (Node)itNodes.next();
						    			htNodes.put(Integer.toString(node.getID()), node);
						    		}
						    		BasicGraph graph = new BasicGraph();
						    		Enumeration edgeEnum = htEdges.elements();
						    		while(edgeEnum.hasMoreElements()){
						    			finalListEdges.add(edgeEnum.nextElement());
						    		}
						    		
						    		Enumeration nodeEnum = htNodes.elements();
						    		while(nodeEnum.hasMoreElements()){
						    			finalListNodes.add(nodeEnum.nextElement());
						    		}
						    		graph.setEdges(finalListEdges);
						    		graph.setNodes(finalListNodes);
						    		NetworkManager nman = FuncionesAuxiliares.getNetworkManager(context);
						    		if(nman.getNetwork("dynamic") == null)
						    			nman.putNetwork("dynamic",null);
						    		Network parent = nman.getNetwork("dynamic");
						    		((LocalGISNetworkManager) nman).putSubNetwork(parent, parentNetworkToFusion.getName(), graph);
						    		parentNetworkToFusion = ((LocalGISNetworkManager) nman).getNetwork(parentNetworkToFusion.getName());

						    		replaceProperties(finalProperties,parentNetworkToFusion);
						    		
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		
        }
    }
    private void replaceProperties(Map<String, Object> finalProperties,
			Network network) {
    	if(network == null)
    		System.out.println("network a null");
    	if(network.getProperties() != null && network.getProperties().size()>0)
    		network.getProperties().clear();
    	else
    		System.out.println("properties a null");
    	if(finalProperties.isEmpty())
    		System.out.println("properties acumuladas a null");
		network.getProperties().putAll(finalProperties);
	}
	private void fusionProperties(Map<String, Object> properties,Map<String, Object> finalProperties) {
		Set<String> keys  = properties.keySet();
		Iterator<String> it = keys.iterator();
		System.out.println("Fusionando properties");
		while(it.hasNext()){
			//Clave a revistar
			
			String actualKey = it.next();
			System.out.println("Revisando " +actualKey);
			if(finalProperties.get(actualKey) != null){
				System.out.println("Clave encontrada.");
				NetworkProperty finalProperty = (NetworkProperty)finalProperties.get(actualKey);
				NetworkProperty actualProperty = (NetworkProperty)properties.get(actualKey);
				Iterator<String> actualPropertyKey = actualProperty.getKeys().iterator();
				while(actualPropertyKey.hasNext()){
					
					String pkey = actualPropertyKey.next();
					System.out.println("Buscando entrada " + pkey);
					if(finalProperty.getValue(pkey) == null){
						System.out.println("Clave no encontrada, agregando... " + pkey + ":" + actualProperty.getValue(pkey));
						finalProperty.setNetworkManagerProperty(pkey, actualProperty.getValue(pkey));
					}else{
						System.out.println("Clave encontrada, no se agregará ");
					}
				}
			}else{
				System.out.println("Clave no encontrada. Agregando a finalProperties la property completa");
				System.out.println("Agregando"+actualKey);
				finalProperties.put(actualKey, properties.get(actualKey));
			}
		}
		
	}
	/**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }

    public void remove(InputChangedListener listener)
    {
        
    }

    public String getTitle()
    {
      return I18N.get("networkFusion","routeengine.networkfusion.selectNetworksTitle");
    }
    public String getInstructions()
    {
    	return I18N.get("networkFusion","routeengine.networkfusion.fusionInstructions");
    }
    /**
     * Este metodo valida y activa o no el boton de next
     */
    public boolean isInputValid()
    {
    	if(jComboBoxNetworkName.isEnabled())
    		return true;
    	if(jTextfieldNetworkName.isEnabled() && jTextfieldNetworkName.getText().length()>0)
    		return true;
    	return false;
    }
    public void setWizardContext(WizardContext wd)
    {
        wizardContext =wd;
    }
    public String getID()
    {
      return "fusionNetworks";
    }
    public void setNextID(String nextID)
    {
       
    }
    public String getNextID()
    {
      return null;
    }
    public void exiting()
    {
    	
    }
}
