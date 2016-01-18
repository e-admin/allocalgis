package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.uva.route.network.Network;
import org.uva.route.network.NetworkManager;
import org.uva.route.network.basic.BasicNetwork;
import org.uva.route.network.basic.BasicNetworkManager;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.FuncionesAuxiliares;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.io.LocalNetworkDAO;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class DeleteNetworksDialog extends JDialog {

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

	public DeleteNetworksDialog(PlugInContext context, String title){
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
		// TODO Auto-generated method stub
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}


	private JPanel getRootPanel() {
		// TODO Auto-generated method stub
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(new JScrollPane(this.getNetworksTree()), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 150, 0));

			rootPanel.add(this.getBotoneraPanel(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 80));
		}
		return rootPanel;
	}



	private JTree getNetworksTree() {
		// TODO Auto-generated method stub
		if (this.networksTree == null){

			//			Object[] hierarchy =
			//			{ "javax.swing",
			//					"javax.swing.border",
			//					"javax.swing.colorchooser",
			//					"javax.swing.event",
			//					"javax.swing.filechooser",
			//					new Object[] { "javax.swing.plaf",
			//					"javax.swing.plaf.basic",
			//					"javax.swing.plaf.metal",
			//			"javax.swing.plaf.multi" },
			//			"javax.swing.table",
			//			new Object[] { "javax.swing.text",
			//					new Object[] { "javax.swing.text.html",
			//			"javax.swing.text.html.parser" },
			//			"javax.swing.text.rtf" },
			//			"javax.swing.tree",
			//			"javax.swing.undo" };
			//			hierarchy = getRedesHierarchy();		

			this.networksTree = new JTree(getRootMutableModel());
			networksTree.setSize(650, 170);
			networksTree.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
					networksTreeSelection(treeSelectionEvent);
				}
			}
			);

		}
		return networksTree;
	}

	private DefaultMutableTreeNode getRootMutableModel() {
		// TODO Auto-generated method stub
		if (rootMutableModel == null){

			rootMutableModel = new DefaultMutableTreeNode("Redes del sistema");

			BasicNetworkManager myNetManager = (BasicNetworkManager) FuncionesAuxiliares.getNetworkManager(context);
			Collection<Network> networks = myNetManager.getNetworks().values();
			Iterator<Network> networksIterator = networks.iterator();
			while(networksIterator.hasNext()){
				Network net = networksIterator.next();
				if (!net.getSubnetworks().isEmpty()){
					rootMutableModel.add( processNetworkHierarchyRecrusive(net.getSubnetworks().values().toArray(),
							net.getName()));

				} else{
					rootMutableModel.add( new DefaultMutableTreeNode(net.getName()));
				}
			}
		}
		return rootMutableModel;
	}

	private void networksTreeSelection(
			TreeSelectionEvent treeSelectionEvent) {
		// TODO Auto-generated method stub
		this.getDeleteDataBaseButton().setEnabled(false);
		this.getDeleteLocalButton().setEnabled(false);
		
		if (treeSelectionEvent.getNewLeadSelectionPath() != null){
			TreePath path = treeSelectionEvent.getNewLeadSelectionPath();
			if (path.getPathCount() > 0){
				if (path.getPathComponent(0).toString().equals("Redes del sistema")){
					if ( path.getPathCount() > 1 && path.getPathComponent(1).toString().equals("RedBaseDatos")){
						if (existsNetworkInDataBase(path.getLastPathComponent().toString())){
							this.getDeleteDataBaseButton().setEnabled(true);
							this.getDeleteLocalButton().setEnabled(false);
						}
					} else if (path.getPathCount() > 1 && path.getPathComponent(1).toString().equals("RedLocal")){
						if (existsNetworkInLocalFile(path.getLastPathComponent().toString())){
							this.getDeleteDataBaseButton().setEnabled(false);
							this.getDeleteLocalButton().setEnabled(true);
						}
					}
				}
			}
		}
	}

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
		NetworkManager myNetworkManager = FuncionesAuxiliares.getNetworkManager(context);
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
		NetworkManager myNetworkManager = FuncionesAuxiliares.getNetworkManager(context);
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

	private DefaultMutableTreeNode processNetworkHierarchyRecrusive(Object[] hierarchy, String ancestorName) {

		//		DefaultMutableTreeNode node = new DefaultMutableTreeNode(((BasicNetwork) hierarchy[0]).getName());
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(ancestorName);
		DefaultMutableTreeNode child;
		for(int i=0; i<hierarchy.length; i++) {
			Object nodeSpecifier = hierarchy[i];
			if (!((BasicNetwork)nodeSpecifier).getSubnetworks().values().isEmpty())  // Ie node with children
				child = processNetworkHierarchyRecrusive(((BasicNetwork)nodeSpecifier).getSubnetworks().values().toArray(), ((BasicNetwork)nodeSpecifier).getName() );
			else
				child = new DefaultMutableTreeNode(((BasicNetwork)nodeSpecifier).getName()); // Ie Leaf
			node.add(child);
		}
		return(node);
	}

	private JPanel getBotoneraPanel() {
		// TODO Auto-generated method stub
		if (botoneraPanel == null){
			botoneraPanel = new JPanel(new GridBagLayout());

			botoneraPanel.add(this.getDeleteDataBaseButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(this.getDeleteLocalButton(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(this.getDeleteMemoryButton(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(this.getEraseAllInMemoryButton(), 
					new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));


		}
		return botoneraPanel;
	}


	private JButton getDeleteDataBaseButton() {
		// TODO Auto-generated method stub
		if (deleteDataBaseButton == null){
			deleteDataBaseButton = new JButton("Eliminar de BBDD");
			deleteDataBaseButton.setEnabled(false);

			deleteDataBaseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Recogemos la informacion del panel actual
					onDeleteDataBaseButtonDo();
				}
			}
			);
		}
		return deleteDataBaseButton;
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
					// ¿Cómo se muestran los mensajes en la layerviewpanel?
					networksTree.setSelectionRow(0);
				} else{
					// TODO mostrar mensaje de no borrado.
					// ¿Cómo se muestran los mensajes en la layerviewpanel?
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
			NetworkManager myNetworkManager = FuncionesAuxiliares.getNetworkManager(context);
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
		"¿Desea realmente eliminar todas las redes de memoria?");

		if (confirmDialog.wasOKPressed()){
			NetworkManager myNetworkManager = FuncionesAuxiliares.getNetworkManager(context);
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
		return okCancelPanel.wasOKPressed();
	}

	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}



}
