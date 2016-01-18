/**
 * ManageNetworksDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.uva.geotools.graph.structure.Graph;
import org.uva.route.graph.structure.dynamic.DynamicGraph;
import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.network.basic.BasicNetworkManager;
import org.uva.routeserver.managers.ChangesMonitoredMemoryManager;
import org.uva.routeserver.managers.GraphMemoryManager;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalNetworkDAO;
import com.localgis.route.manager.LocalGISAllinMemoryManager;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;

public class ManageNetworksDialog extends MultiInputDialog {

    public class MyTreeCellEditor extends DefaultTreeCellEditor  {

	public MyTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
	    super(tree, renderer);
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value,
	        boolean isSelected, boolean expanded, boolean leaf, int row) {
	    return renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
	}

	public boolean isCellEditable(EventObject anEvent) {
	    return true; // Or make this conditional depending on the node
	}
    }

private static final class NetworkTreeCellRenderer extends DefaultTreeCellRenderer
    {
	private PlugInContext context;

	public NetworkTreeCellRenderer(PlugInContext context) {
		this.context=context;
	    }

	@Override
	public Component getTreeCellRendererComponent(final JTree tree, final Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
	    Object nodeValue=null;
	    Color bColor;
	    if (selected) {
	            bColor = getBackgroundSelectionColor();
		} else {
		    bColor = getBackgroundNonSelectionColor();
	            if (bColor == null) {
	                bColor = getBackground();
	            }
		}
	    if (value instanceof DefaultMutableTreeNode)
		{
		    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
		    nodeValue=treeNode.getUserObject();
		}
	    if (nodeValue instanceof Network)
		{
		    final Network network = (Network) nodeValue;
		  
		    String labelText = network.getName();
		    String statusIconText= "";
		    String datastore="";
		    String action="";
		    String storeName="";
		    Integer numEdges=Integer.valueOf(0);
		    Integer numNodes=Integer.valueOf(0);
		    boolean isDirty=false;
		   
		    Graph graph=network.getGraph();
		    if (graph instanceof DynamicGraph)
			{
			    DynamicGraph dynGraph = (DynamicGraph) graph;
			    numEdges=dynGraph.getEdgesInMemoryCount();
			    numNodes=dynGraph.getNodesInMemoryCount();
			    GraphMemoryManager memMgr = dynGraph.getMemoryManager();
			    if (memMgr instanceof ChangesMonitoredMemoryManager)
				{
				    ChangesMonitoredMemoryManager chgMemMgr = (ChangesMonitoredMemoryManager) memMgr;
				    if (chgMemMgr.getUpdateMonitor().getDeletedEdges().size()!=0 
					 ||chgMemMgr.getUpdateMonitor().getDirtyEdges().size()!=0
					 ||chgMemMgr.getUpdateMonitor().getDeletedNodes().size()!=0 
					 ||chgMemMgr.getUpdateMonitor().getDirtyNodes().size()!=0)
					{
					  isDirty=true;  
					}							
				}
			}
		    final LocalGISAllinMemoryManager lgMemMgr = NetworkModuleUtil.castToLocalGISAllinMemoryManager(graph);
		    if (lgMemMgr!=null)
			{
			    if (lgMemMgr.isLinkedToDatabase())
				{
				    statusIconText = "online.png";
				}
			    else if (lgMemMgr.isLinkedToFile())
				{
				    statusIconText = "Computer.gif";
				}
			    storeName = lgMemMgr.getStoreNetworkName();
			    if (storeName==null)
				{
				    storeName="(Mem)";
				}
			}
		    //Label
		    JPanel panel=new JPanel();
		    panel.setBackground(bColor);
		    panel.setMinimumSize(new Dimension(400,100));
		    JLabel netNameLabel=new JLabel(labelText+"(A:"+numEdges+"-N:"+numNodes+")\n"+storeName);
		    netNameLabel.setOpaque(false);
		    JLabel status=new JLabel(IconLoader.icon(statusIconText));
		    status.setOpaque(false);
		    panel.add(netNameLabel);
		    panel.add(status);
		    JButton save=new JButton(IconLoader.icon("Guardar.GIF"));
		    save.setContentAreaFilled(false);
		    save.setFocusPainted(false);
		    save.setMargin(new Insets(0, 0, 0, 0));
		    save.setContentAreaFilled(false);
		    save.setBorderPainted(false);
			panel.add(save);
			save.setEnabled(false);
		    if (isDirty)
			{
			save.setEnabled(true);
			save.addActionListener(new ActionListener()
			    {
				@Override
				public void actionPerformed(ActionEvent e)
				{
				    if(lgMemMgr!=null)
					{
					lgMemMgr.setTaskMonitor(new TaskMonitorDialog(context.getWorkbenchFrame(), context.getWorkbenchFrame()));
					}
				    
					try
					    {
						((DynamicGraph)network.getGraph()).commit();
						DefaultMutableTreeNode node = ((DefaultMutableTreeNode)value);
						tree.updateUI();
					    } catch (IOException e1)
					    {
						throw new RuntimeException(e1);
					    }							    
				}
			    });
			}
		    // Republicar
		    if (lgMemMgr.isLinkedToDatabase())
			{
			    addPublishButton(network.getName(),panel);
			}
		    // BORRAR
			JButton delete=new JButton(IconLoader.icon("eliminar.gif"));
			delete.setContentAreaFilled(false);
			delete.setFocusPainted(false);
			delete.setMargin(new Insets(0, 0, 0, 0));
			delete.setContentAreaFilled(false);
			delete.setBorderPainted(false);

			panel.add(delete);
			delete.addActionListener(new ActionListener()
			    {
				@Override
				public void actionPerformed(ActionEvent e)
				{
				   JOptionPane.showConfirmDialog(context.getWorkbenchFrame(), "Â¿Realmente quiere quitar la red "+network.getName()+" de la memoria de trabajo?");
				   NetworkModuleUtilWorkbench.getNetworkManager(context).detachNetwork(network.getName());
				   ((DefaultMutableTreeNode)value).removeFromParent();
				   tree.updateUI();
				}
			    });
		    return panel;
		}
	    else 
		if (nodeValue instanceof Map.Entry) // Redes de base de datos
		    {
			Map.Entry entry = (Map.Entry)nodeValue;
			final String netName = ((String) entry.getValue()).trim();
			final String labelText = netName;
			final String dbIconText = "online.png";
			  //Label
			JPanel panel=new JPanel();
			panel.setBackground(bColor);
			panel.setMinimumSize(new Dimension(400,100));			
			JLabel netNameLabel=new JLabel(labelText);
			netNameLabel.setOpaque(false);
			panel.add(netNameLabel);
			panel.add(new JLabel(IconLoader.icon(dbIconText)));
			// republicar
			addPublishButton( netName,   panel);
			 // BORRAR
			JButton delete=new JButton(IconLoader.icon("eliminar.gif"));
			delete.setContentAreaFilled(false);
			delete.setFocusPainted(false);
			delete.setMargin(new Insets(0, 0, 0, 0));
			delete.setContentAreaFilled(false);
			delete.setBorderPainted(false);
			JButton load=new JButton(IconLoader.icon("abrir.gif"));
			load.setContentAreaFilled(false);
			load.setFocusPainted(false);
			load.setMargin(new Insets(0, 0, 0, 0));
			load.setContentAreaFilled(false);
			load.setBorderPainted(false);
			load.setEnabled(false);
			load.setToolTipText("No implementado aún. Utilice el botón de la barra de aplicación");
			
			panel.add(delete);
			panel.add(load);
			delete.addActionListener(new ActionListener()
			    {
				@Override
				public void actionPerformed(ActionEvent e)
				{
				   JOptionPane.showConfirmDialog(context.getWorkbenchFrame(), "¿Realmente quiere borrar definitivamente la red "+netName+" de la base de datos?");
				   GeopistaRouteConnectionFactoryImpl fact=new GeopistaRouteConnectionFactoryImpl();
				   LocalGISNetworkDAO dao=new LocalGISNetworkDAO();
				   dao.deleteNetworkFromDataBaseByNetworkName(netName, fact.getConnection());
				   ((DefaultMutableTreeNode)value).removeFromParent();
				}
			    });
			load.addActionListener(new ActionListener()
			    {
				@Override
				public void actionPerformed(ActionEvent e)
				{
				   JOptionPane.showConfirmDialog(context.getWorkbenchFrame(), "Â¿Realmente quiere borrar definitivamente la red "+netName+" de la base de datos?");
				   GeopistaRouteConnectionFactoryImpl fact=new GeopistaRouteConnectionFactoryImpl();
				   LocalGISNetworkDAO dao=new LocalGISNetworkDAO();
				   dao.deleteNetworkFromDataBaseByNetworkName(netName, fact.getConnection());
				   ((DefaultMutableTreeNode)value).removeFromParent();
				}
			    });
			return panel;
		    }
	    else
	    return super.getTreeCellRendererComponent(tree, nodeValue, selected, expanded, leaf, row, hasFocus);
	}

	public void addPublishButton(final String netName, final JPanel panel)
	{
	    JButton publish= new JButton(IconLoader.icon("World2.gif"));
	    publish.setContentAreaFilled(false);
	    publish.setFocusPainted(false);
	    publish.setMargin(new Insets(0, 0, 0, 0));
	    publish.setContentAreaFilled(false);
	    publish.setBorderPainted(false);
	    publish.setToolTipText("Republish information in route");
	    
	    panel.add(publish);
	    publish.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e)
		{
		  final String baseURL=AppContext.getApplicationContext().getString("geopista.url.tomcat");
		  // query
		  final HttpClient client = AppContext.getHttpClient();
		  
		  
		TaskMonitorManager  monitor=new TaskMonitorManager();
		
			  monitor.execute(new ThreadedPlugIn() {
				
				@Override
				public void initialize(PlugInContext context) throws Exception {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public boolean execute(PlugInContext context) throws Exception {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void run(TaskMonitor monitor, PlugInContext context)
						throws Exception {
					try{
					monitor.report("Recargando la red "+netName);
					GetMethod get= new GetMethod(baseURL+"/LOCALGIS-WPS-MVN/reloadNetwork");
					get.setQueryString("networkName="+netName);
					int status=client.executeMethod(get);
					if (status==200)
					    {
						JOptionPane.showMessageDialog(panel, "Petición aceptada por el servidor de rutas.", "Recarga de red",	JOptionPane.INFORMATION_MESSAGE);
					    }
					else
					    {
						ErrorDialog.show(panel, "No se republica la red.", "Petición rechazada por el servidor de rutas. Es posible que la red no esté publicada en el servidor de rutas.", get.getResponseBodyAsString());
					    }
						} catch (HttpException e1)
					    {
						throw new RuntimeException(e1);
					    } catch (IOException e1)
					    {
						throw new RuntimeException(e1);
					    }
				}
			}, context);
			
		    
		}});
	}
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 3627779405428620048L;

	private JPanel rootPanel = null;
	private JTree networksTree = null;
	private DefaultMutableTreeNode rootMutableModel = null;
	private OKCancelPanel okCancelPanel = null;
	private JPanel botoneraPanel = null;
	private JButton deleteDataBaseButton = null;
	private JButton deleteLocalButton = null;
	private JButton deleteMemoryButton = null;
	private JButton eraseAllInMemoryButton = null;

	private PlugInContext context = null;
	protected LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
	private LocalNetworkDAO localNetworkDO = new LocalNetworkDAO();
	private GeopistaRouteConnectionFactoryImpl connectionFactory = new GeopistaRouteConnectionFactoryImpl();

	protected DefaultMutableTreeNode localNetMutableModel;

	protected DefaultMutableTreeNode availNetMutableModel;

	public ManageNetworksDialog(PlugInContext context, String title)
	{
		super(context.getWorkbenchFrame(), title, true);
		this.context = context;
		this.setSize(450, 400);
		this.setLocationRelativeTo(context.getWorkbenchFrame());
		this.initialize();
		this.setResizable(false);
		this.setEnabled(true);
		this.setVisible(true);
	}

	private void initialize() {
	  
	    addLabel("Redes disponibles en el sistema");
	    JScrollPane netsPane = new JScrollPane(this.getNetworksTree());
	    netsPane.setMinimumSize(new Dimension(800,200));
	    addRow( netsPane);
	    this.setSideBarDescription(getDescription());

	}

	public String getDescription()
	{
	    return "Panel de administración de redes del sistema. Las redes locales se eliminan de la memoria pero no se borran de su almacen. Las redes remotas se eliminan físicamente de LocalGIS.";
	}


	

	private JTree getNetworksTree() {
		// TODO Auto-generated method stub
		if (this.networksTree == null){

			this.networksTree = new JTree(getRootMutableModel());
			networksTree.setRowHeight(0);
			NetworkTreeCellRenderer renderer = new NetworkTreeCellRenderer(context);
			MyTreeCellEditor editor = new MyTreeCellEditor(networksTree, renderer);
			networksTree.setCellRenderer(renderer);
			networksTree.setCellEditor(editor);
			networksTree.setEditable(true);
		}
		return networksTree;
	}

	private DefaultMutableTreeNode getRootMutableModel() {
		if (rootMutableModel == null){
		    rootMutableModel= new DefaultMutableTreeNode("Localgis.                                          ");
		    
			localNetMutableModel = new DefaultMutableTreeNode("Redes cargadas.                                                                           ");
			rootMutableModel.add(localNetMutableModel);
			BasicNetworkManager myNetManager = (BasicNetworkManager) NetworkModuleUtilWorkbench.getNetworkManager(context);
			Collection<Network> networks = myNetManager.getNetworks().values();
			Iterator<Network> networksIterator = networks.iterator();
			while(networksIterator.hasNext()){
				Network net = networksIterator.next();
				if (!net.getSubnetworks().isEmpty()){
				    localNetMutableModel.add( processNetworkHierarchyRecrusive(net.getSubnetworks().values().toArray(),net));

				} else{
				    localNetMutableModel.add( new DefaultMutableTreeNode(net));
				}
			}
			availNetMutableModel = new DefaultMutableTreeNode("Redes disponibles.");
			rootMutableModel.add(availNetMutableModel);
			try
			    {
				LocalGISNetworkDAO dao = new LocalGISNetworkDAO();
				GeopistaRouteConnectionFactoryImpl fact=new GeopistaRouteConnectionFactoryImpl();
				Hashtable<Integer, String> names = dao.getNetworkNames(fact.getConnection());
				for (Map.Entry network : names.entrySet())
				    {
					availNetMutableModel.add(new DefaultMutableTreeNode(network));
				    }
			    } catch (SQLException e)
			    {
				e.printStackTrace();
			    }
			
		}
		return rootMutableModel;
	}

//	private void networksTreeSelection(
//			TreeSelectionEvent treeSelectionEvent) {
//		// TODO Auto-generated method stub
//		this.getDeleteDataBaseButton().setEnabled(false);
//		this.getDeleteLocalButton().setEnabled(false);
//		
//		if (treeSelectionEvent.getNewLeadSelectionPath() != null){
//			TreePath path = treeSelectionEvent.getNewLeadSelectionPath();
//			if (path.getPathCount() > 0){
//				if (path.getPathComponent(0).toString().equals("Redes del sistema")){
//					if ( path.getPathCount() > 1 && path.getPathComponent(1).toString().equals("RedBaseDatos")){
//						if (existsNetworkInDataBase(path.getLastPathComponent().toString())){
//							this.getDeleteDataBaseButton().setEnabled(true);
//							this.getDeleteLocalButton().setEnabled(false);
//						}
//					} else if (path.getPathCount() > 1 && path.getPathComponent(1).toString().equals("RedLocal")){
//						if (existsNetworkInLocalFile(path.getLastPathComponent().toString())){
//							this.getDeleteDataBaseButton().setEnabled(false);
//							this.getDeleteLocalButton().setEnabled(true);
//						}
//					}
//				}
//			}
//		}
//	}

	private boolean existsNetworkInDataBase(String networkName) {
		// TODO Auto-generated method stub
		Connection conn = connectionFactory.getConnection();
		boolean resultado = networkDAO.existsNetworkIntoDataBaseByNetworkName(networkName, conn);
		return resultado;
	}

	private boolean existsNetworkInLocalFile(String networkName) {
		// TODO Auto-generated method stub
		String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
		File dir = new File(base,"networks");
		if(! dir.exists() ){
			dir.mkdirs();
		}

		String folderPath = dir.getPath();
		File networkDir = new File(folderPath,networkName);
		if(! networkDir.exists() ){
			return false;
		}

		File networkFile = new File(networkDir.getPath(), networkName + ".network");
		if (networkFile.exists()){
			return true;
		} else {
			return false;
		}
	}

	private boolean existsNetworkInMemory(TreePath path,String networkName) {
		// TODO Auto-generated method stub
		boolean resultado = false;
		NetworkManager myNetworkManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network actualNetwork = null;
		if (path.getPathCount() > 0){
			for (int i = 0; i < path.getPathCount(); i++){
				if (actualNetwork == null){
					actualNetwork = myNetworkManager.getNetwork(path.getPathComponent(i).toString());	
				}else{
					actualNetwork = actualNetwork.getSubNetwork(path.getPathComponent(i).toString());
				}

			}
		}
		if (actualNetwork != null){
			resultado = true;
		}
		return resultado;
	}

	private boolean existsNetworkInMemory(String networkName){
		NetworkManager myNetworkManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
		Network actualNetwork = null;
		Iterator<Network> it = myNetworkManager.getNetworks().values().iterator();
		while (it.hasNext()){
			if (searchForNetworkRecursive(it.next(), networkName)){
				return true;
			}
		}
		return false;
	}

	private boolean searchForNetworkRecursive(Network actualNetwork, String netName) {
		// TODO Auto-generated method stub
		if (actualNetwork.getSubnetworks().isEmpty()){
			if (actualNetwork.getName().equals(netName)){
				return true;
			}
		} else{
			Iterator<Network> iterator = actualNetwork.getSubnetworks().values().iterator();
			while (iterator.hasNext()){
				return searchForNetworkRecursive(iterator.next(), netName);
			}
		}
		return false;
	}

	private boolean existsNetworkPropertiesInLocalFile(String networkName) {
		// TODO Auto-generated method stub
		String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
		File dir = new File(base,"networks");
		if(! dir.exists() ){
			dir.mkdirs();
		}

		String folderPath = dir.getPath();
		File networkDir = new File(folderPath,networkName);
		if(! networkDir.exists() ){
			networkDir.mkdirs();
		}

		File networkFile = new File(networkDir.getPath(), networkName + ".network" + ".properties");

		if (networkFile.exists()){
			return true;
		} else {
			return false;
		}
	}



	//	  private Object[] getRedesHierarchy() {
	//	  
	//		// TODO Auto-generated method stub
	//		  ArrayList resultado = new ArrayList();
	//		  if (FuncionesAuxiliares.getNetworkManager(context) instanceof BasicNetworkManager){
	//			  BasicNetworkManager netManager= (BasicNetworkManager) FuncionesAuxiliares.getNetworkManager(context);
	//			  BasicNetworkManager myNetManager = new BasicNetworkManager();
	//			  BasicNetwork bnet = new BasicNetwork("net1");
	//			  for (int i = 0; i < 5 ; i ++ ){
	//				  bnet.getSubnetworks().put("net1_" + i, new BasicNetwork("net1_" + i));
	//			  }
	//			  for (int m = 0; m < 5; m++){
	//				  bnet.getSubNetwork("net1_1").getSubnetworks().put("net1_1" + m, new BasicNetwork("net1_1" + m));
	//			  }
	//			  myNetManager.getNetworks().put("net1", bnet);
	//			  
	//			  Iterator<Network> it = myNetManager.getNetworks().values().iterator();
	//			  while (it.hasNext()){
	//				  Network net = it.next();
	//				  resultado.add(
	//						  recursiveNetworksHierarchy(resultado, myNetManager, net, net.getName())
	//						  );
	//			  }
	//		  }
	//		  return resultado.toArray();
	//	}

	/**
	 * @param resultado
	 * @param netManager
	 * @param it
	 * @param net
	 */
	//	private ArrayList recursiveNetworksHierarchy(ArrayList hierarchy,
	//			BasicNetworkManager netManager, Network net, String name) {
	//		
	//		ArrayList resultado = new ArrayList();
	//		if (!net.getSubnetworks().isEmpty()){
	//			Iterator<Network> itSubNets = net.getSubnetworks().values().iterator();
	//			while(itSubNets.hasNext()){
	//				BasicNetwork subnet = (BasicNetwork) itSubNets.next();
	//				resultado.add(recursiveNetworksHierarchy(new ArrayList(), netManager,subnet , name + "." + subnet.getName()));	
	//			}
	//		} 	
	//		resultado.add(name);
	//		hierarchy.add(resultado);
	//		return hierarchy;
	//	}

private DefaultMutableTreeNode processNetworkHierarchyRecrusive(Object[] hierarchy, Network ancestorNet)
{

		//		DefaultMutableTreeNode node = new DefaultMutableTreeNode(((BasicNetwork) hierarchy[0]).getName());
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(ancestorNet);
		DefaultMutableTreeNode child;
		for(int i=0; i<hierarchy.length; i++) {
			Object nodeSpecifier = hierarchy[i];
			if (!((BasicNetwork)nodeSpecifier).getSubnetworks().values().isEmpty())  // Ie node with children
				child = processNetworkHierarchyRecrusive(((BasicNetwork)nodeSpecifier).getSubnetworks().values().toArray(), ((BasicNetwork)nodeSpecifier) );
			else
				child = new DefaultMutableTreeNode(((BasicNetwork)nodeSpecifier)); // Ie Leaf
			node.add(child);
		}
		return(node);
	}

	


	

	private void onDeleteDataBaseButtonDo() {
		// TODO Auto-generated method stub
		TreePath path = this.networksTree.getSelectionPath();
		String selectedNetworkName = path.getLastPathComponent().toString();
		ConfirmDeleteNetworkDialog confirmDialog = new ConfirmDeleteNetworkDialog(
				this.context.getWorkbenchFrame(), "Eliminar Red Base Datos", 
				"¿Desea realmente eliminar la red '" + selectedNetworkName + "' de base de datos?");

		if (confirmDialog.wasOKPressed()){

			if (networkDAO.existsNetworkIntoDataBaseByNetworkName(selectedNetworkName, this.connectionFactory.getConnection())){

				boolean borrado = networkDAO.deleteNetworkFromDataBaseByNetworkName(selectedNetworkName, 
						this.connectionFactory.getConnection());

				if (borrado){
					//TODO mostrar mensaje de borrado.
					// ï¿½Cï¿½mo se muestran los mensajes en la layerviewpanel?
					networksTree.setSelectionRow(0);
				} else{
					// TODO mostrar mensaje de no borrado.
					// ï¿½Cï¿½mo se muestran los mensajes en la layerviewpanel?
					;
				}

			}
		}

	}

	private JButton getDeleteLocalButton() {
		// TODO Auto-generated method stub
		if (deleteLocalButton == null){
			deleteLocalButton = new JButton("Elminar de LOCAL");
			deleteLocalButton.setEnabled(false);

			deleteLocalButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onDeleteLocalButtonDo();
				}
			}
			);
		}
		return deleteLocalButton;
	}

	private void onDeleteLocalButtonDo() {
		// TODO Auto-generated method stub

		TreePath path = this.networksTree.getSelectionPath();
		String selectedNetworkName = path.getLastPathComponent().toString();
		ConfirmDeleteNetworkDialog confirmDialog = new ConfirmDeleteNetworkDialog(
				this.context.getWorkbenchFrame(), "Eliminar Red Local", 
				"¿Desea realmente eliminar la red '" + selectedNetworkName + "' de local?");

		if (confirmDialog.wasOKPressed()){
			if (existsNetworkInLocalFile(selectedNetworkName)){
				File dir = new File(AppContext.getApplicationContext().getString("ruta.base.mapas"),"networks");

				File networkDir = new File(dir.getPath(),selectedNetworkName);
				int maxFilesLength = networkDir.list().length;
				for (int i = maxFilesLength; i > 0; i--){
					File file = networkDir.listFiles()[i-1];
					if (file.getName().equals(selectedNetworkName + ".network")){
						file.delete();
					} else if (file.getName().equals(selectedNetworkName + ".network" + ".properties")){
						file.delete();
					}
				}
				networkDir.delete();

				//Pasar La red del grupo de RedLocal a una red normal.

				//Se eeseleciona los elementos, para desahbilitar botones.
				networksTree.setSelectionRow(0);

			}
		}

	}

	private JButton getDeleteMemoryButton() {
		// TODO Auto-generated method stub
		if (deleteMemoryButton == null){
			deleteMemoryButton = new JButton("Elminar de MEMORIA");

			deleteMemoryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onDeleteMemoryButtonDo();
				}
			}
			);
		}
		return deleteMemoryButton;
	}

	private void onDeleteMemoryButtonDo() {
		// TODO Auto-generated method stub
		boolean removed = false;
		TreePath path = this.networksTree.getSelectionPath();
		String selectedNetworkName = path.getLastPathComponent().toString();
		ConfirmDeleteNetworkDialog confirmDialog = new ConfirmDeleteNetworkDialog(
				this.context.getWorkbenchFrame(), "Eliminar Red Memoria", 
				"¿Desea realmente eliminar la red '" + selectedNetworkName + "' de memoria?");

		if (confirmDialog.wasOKPressed()){
			NetworkManager myNetworkManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
			if (existsNetworkInMemory(path, selectedNetworkName)){
				if (path.getPathCount() <= 2){
					if (myNetworkManager.getNetworks().get(selectedNetworkName) != null){
						if (myNetworkManager.getNetworks().remove(selectedNetworkName)!=null){
							removed = true;
						}
					}
				} else {
					Network actualNetwork = null;

					for (int i = 1; i < path.getPathCount()-1; i++){
						if (actualNetwork == null){
							actualNetwork = myNetworkManager.getNetwork(path.getPathComponent(i).toString());	
						}else{
							actualNetwork = actualNetwork.getSubNetwork(path.getPathComponent(i).toString());
						}
					}
					System.err.println(actualNetwork.getName());

					if (actualNetwork.getSubnetworks().remove(selectedNetworkName) != null){
						removed = true;
					}
				}

				if (removed){
					System.err.println("ELIMINADA");
					DefaultMutableTreeNode def = (DefaultMutableTreeNode)networksTree.getModel().getRoot();
					def.remove((MutableTreeNode) path.getLastPathComponent());
					networksTree.updateUI();
				}else{
					System.err.println("NO ELIMINADA");
				}
			}
		}
	}



	private JButton getEraseAllInMemoryButton() {
		// TODO Auto-generated method stub
		if (eraseAllInMemoryButton == null){
			eraseAllInMemoryButton = new JButton("Limpiar MEMORIA");
			eraseAllInMemoryButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onEraseAllInMemoryButtonDo();
				}
			}
			);
		}
		return eraseAllInMemoryButton;
	}

	private void onEraseAllInMemoryButtonDo() {
		// TODO Auto-generated method stub
		ConfirmDeleteNetworkDialog confirmDialog = new ConfirmDeleteNetworkDialog(
				this.context.getWorkbenchFrame(), "Vaciar Memoria", 
		"Â¿Desea realmente eliminar todas las redes de memoria?");

		if (confirmDialog.wasOKPressed()){
			NetworkManager myNetworkManager = NetworkModuleUtilWorkbench.getNetworkManager(context);
			Iterator<Network> it = myNetworkManager.getNetworks().values().iterator();
			
			myNetworkManager.getNetworks().values().removeAll(myNetworkManager.getNetworks().values());

			DefaultMutableTreeNode def = (DefaultMutableTreeNode)networksTree.getModel().getRoot();
			for (int i = def.getChildCount(); i >0 ; i --){
				def.remove(i-1);
			}
			networksTree.updateUI();
		}

	}

	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		return true; 
	}

	public boolean wasOKPressed() {
		return super.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}



}
