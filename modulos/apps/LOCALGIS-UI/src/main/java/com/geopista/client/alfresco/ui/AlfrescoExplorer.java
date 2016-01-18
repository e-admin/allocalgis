/**
 * AlfrescoExplorer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.client.alfresco.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.util.Constants;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.client.alfresco.AlfrescoConstants;
import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.ui.panel.DynamicTreePanel;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.document.CementerioDocumentClient;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.protocol.licencias.CAnexo;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CSolicitudLicencia;
import com.geopista.utils.alfresco.beans.FeatureBean;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.beans.AlfrescoNode;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Interfaz gráfica de gestión de Alfresco
 */
public class AlfrescoExplorer extends JDialog {

	/**
	 * Variables de objetos gráficos
	 */
	private JPanel generalPanel = null;
	private JPanel filePanel = null;
	private JPanel directoryPanel = null;
	private JSplitPane buttonSplitPane = null;
	private DynamicTreePanel dynamicTreePanel = null;
	private JButton btnCreateDirectory = null;
	private JButton btnRenameDirectory = null;
	private JButton btnRemoveDirectory = null;
	private JButton btnAnnexeDocument = null;
	private JButton btnAddFile = null;
	private JButton btnDownloadDocument = null;
	private JButton btnPreviewDocument = null;
	//private JButton btnDeanexateDocument = null;
	private JFileChooser fileChooser = null;
	
	/**
	 * Variables
	 */
	private DefaultMutableTreeNode rootNode = null;
	private AlfrescoDocumentClient alfrescoDocumentClient = null;
	private Object [] features = null;
	private Object element = null;
	private String idMunicipality = null;
	private String app = null;
	private ArrayList<String> documentsUuid = null;
	
	
	/**
	 * Constructor
	 * @param owner: JFrame padre
	 */
	public AlfrescoExplorer(JFrame owner) {
		super(owner);
		init();
	}
	
	/**
	 * Constructor
	 * @param owner: Formulario padre
	 * @param features: Array con las geometrías seleccionadas	 
	 * @param documentsUuid: Array con los uuid de los documentos asociados a las features	 
	 * @param idMunicipality: Identificador de municipio (entidad)
	 * @param app: Nombre del tipo de aplicación documental LocalGIS
	 */
	public AlfrescoExplorer(JFrame owner, Object [] features, ArrayList<String> documents, String idMunicipality, String app) {
		super(owner);
		this.features = features;
		this.idMunicipality = idMunicipality;
		this.app = app;
		this.documentsUuid = getAlfrescoDocumentsUuid(documents);
		init();    
	}
	
	/**
	 * Constructor
	 * @param owner: Formulario padre	 
	 * @param inventarioElement: Elemento del inventario
	 * @param documentsUuid: Array con los uuid de los documentos asociados a las features
	 * @param idMunicipality: Identificador de municipio (entidad)
	 * @param app: Nombre del tipo de aplicación documental LocalGIS
	 */
	public AlfrescoExplorer(JFrame owner, Object element, ArrayList<String> documents, String idMunicipality, String app) {
		super(owner); 
		this.element = element;
		this.idMunicipality = idMunicipality;
		this.app = app;
		this.documentsUuid = getAlfrescoDocumentsUuid(documents);
		init();			    	
	}
	
	/**
	 * Recupera los uuid de los documentos de Alfresco asociados a la feature
	 * @param documentsUuid: Lista de todos los documentos asociados a la feature
	 * @return ArrayList<String>: Lista de los uuid de los documentos de Alfresco asociados a la feature
	 */
	public ArrayList<String> getAlfrescoDocumentsUuid(ArrayList<String> documentsUuid){
		ArrayList<String> alfrescoDocuments = new ArrayList<String>();
		Iterator<String> it = documentsUuid.iterator();
		while(it.hasNext()){
			String uuid = it.next();
			if(!uuid.startsWith(idMunicipality)){
				alfrescoDocuments.add(uuid);
			}
		}		
		return alfrescoDocuments;
	}
	
	/**
	 * Inicializa la aplicación
	 */
	private void init() {	
		final AlfrescoExplorer alfrescoExplorer = this;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
	    progressDialog.allowCancellationRequests();
	    progressDialog.setTitle(AppContext.getApplicationContext().getI18nString("alfresco.message.loadingFiles"));
	    progressDialog.report(AppContext.getApplicationContext().getI18nString("alfresco.message.loadingFiles"));
	    progressDialog.addComponentListener(new ComponentAdapter(){
	    	public void componentShown(ComponentEvent e){
	    		new Thread(new Runnable(){
	    			public void run(){
						alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.ALFRESCO_WEBAPP_NAME +
				            ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
						
						setTitle(AppContext.getApplicationContext().getI18nString("alfresco.title"));
						try {
							setTitle(getTitle() + " (" + alfrescoDocumentClient.getMunicipalityName(idMunicipality) + ")");
						} catch (Exception e) {
							System.out.println(e);
						}
						setModal(true);
						setResizable(false);
						setPreferredSize(new Dimension(700, 500));		
						setLocationRelativeTo(null);
						setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						
						try {            
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
						} catch (Exception e) {}
								
						rootNode = getRootDirectoryNode(idMunicipality, app);
						
						if(rootNode == null || rootNode.getUserObject() == null){
							progressDialog.setVisible(false);
							JOptionPane.showMessageDialog(alfrescoExplorer, AppContext.getApplicationContext().getI18nString("alfresco.error.permission"));	
							dispatchEvent(new WindowEvent(alfrescoExplorer.getOwner(), WindowEvent.WINDOW_CLOSING));
							setVisible(false);		
							dispose();
						}	
						else{
							dynamicTreePanel = getDynamicTreePanel();
							btnCreateDirectory = getBtnCreateDirectory();
							btnRenameDirectory = getBtnRenameDirectory();
							btnRemoveDirectory = getBtnRemoveDirectory();
							btnAnnexeDocument = getBtnAnnexeDocument();
							btnAddFile = getBtnAddFile();
							btnDownloadDocument = getBtnDownloadDocument();
							btnPreviewDocument = getBtnPreviewDocument();
							directoryPanel = getDirectoryPanel();
							filePanel = getFilePanel();
							buttonSplitPane = getButtonSplitPane();
							generalPanel = getGeneralPanel();
								
							getContentPane().add(getGeneralPanel());
							pack();		
							progressDialog.setVisible(false);
							setVisible(true);							
						}						
					}
				}).start();
			}
		});
		if (AppContext.getApplicationContext().getMainFrame() == null) // sin ventana de referencia
			GUIUtil.centreOnScreen(progressDialog);
		else
			GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);	
	}

	/**
	 * Devuelve el nodo padre
	 * @param entity: Nombre de la entidad
	 * @param app: Nombre de la aplicación
	 * @return DefaultMutableTreeNode: Nodo padre de la ruta relativa a la entidad y a la aplicción de LocalGIS
	 */
	private DefaultMutableTreeNode getRootDirectoryNode(String entity, String app){
		rootNode = new DefaultMutableTreeNode();
		Node node = null;
		try {
			node = alfrescoDocumentClient.initializeRelativeDirectoryPathAndAccess(entity, app);
		} catch (Exception e) {
			System.out.println(e);
		}
		if(node != null){
			String propName = AlfrescoManagerUtils.getPropertyFromNode(node,
						Constants.PROP_NAME);	
			rootNode.setUserObject(new AlfrescoNode(propName, node.getReference().getUuid()));				
		}
		return rootNode;
	}

	/**
	 * Asigna a la variable rootNode el árbol de nodos de directorios relativo
	 * @param parentNode: Nodo padre
	 */
	private void getTreeDirectories(DefaultMutableTreeNode parentNode) {		
		try {
			rootNode = alfrescoDocumentClient.getTreeDirectories(parentNode);			 
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Devuelve el nodo de árbol seleccionado en el árbol de directorios
	 * @return DefaultMutableTreeNode: Nodo de árbol seleccionado del árbol de directorios
	 */
	private DefaultMutableTreeNode getTreeSelectedDefaultMutableNode(){
		return (DefaultMutableTreeNode) getDynamicTreePanel().getDragAndDropTree().getLastSelectedPathComponent();
	}
	
	/**
	 * Devuelve el nodo Alfresco del directorio seleccionado en el árbol de directorios
	 * @return AlfrescoNode: Nodo Alfresco seleccionado del árbol de directorios
	 */
	private AlfrescoNode getTreeSelectedAlfrescoNode(){
		return (AlfrescoNode) getTreeSelectedDefaultMutableNode().getUserObject();
	}
	
	/**
	 * Devuelve clave unívoca del directorio seleccionado en el árbol de directorios
	 * @return AlfrescoKey: Clave unívoca del nodo del árbol de directorios seleccionado
	 */
	private AlfrescoKey getTreeSelectedAlfrescoKey(){
		return new AlfrescoKey(getTreeSelectedAlfrescoNode().getUuid(), AlfrescoKey.KEY_UUID);
	}	
	
	/**
	 * Mueve un nodo de Alfresco
	 * @return boolean: Resultado del movimiento del nodo de Alfresco
	 */
	private boolean moveNode(DefaultMutableTreeNode dropNode, DefaultMutableTreeNode destinyNode){
		AlfrescoNode alfrescoDestinyNode = ((AlfrescoNode)destinyNode.getUserObject());
		AlfrescoNode alfrescoDropNode = ((AlfrescoNode)dropNode.getUserObject());
		try {
			return alfrescoDocumentClient.moveNode(new AlfrescoKey(alfrescoDestinyNode.getUuid(),AlfrescoKey.KEY_UUID), new AlfrescoKey(alfrescoDropNode.getUuid(),AlfrescoKey.KEY_UUID));
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}
	
	/**
	 * Crea un directorio dentro del directorio seleccionado en la árbol de directorios
	 */
	private void createNewDirectory(){		
		String newDirectory = JOptionPane.showInputDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.input.directory"), "", JOptionPane.PLAIN_MESSAGE);
		if(newDirectory!=null){
			Node node = null;
			try {
				node = alfrescoDocumentClient.addDirectoryFromParent(getTreeSelectedAlfrescoKey(), newDirectory);
			} catch (Exception e) {
				System.out.println(e);
			}	
			if(node!=null){
				DefaultMutableTreeNode newDefaultMutableTreeNode = new DefaultMutableTreeNode();
				newDefaultMutableTreeNode.setUserObject(new AlfrescoNode(AlfrescoManagerUtils.getPropertyFromNode(node, Constants.PROP_NAME), node.getReference().getUuid()));
				getTreeSelectedDefaultMutableNode().add(newDefaultMutableTreeNode);
				
				((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).nodeStructureChanged(getTreeSelectedDefaultMutableNode());
				
				repaintTree();
				
				getDynamicTreePanel().getDragAndDropTree().setSelectionPath(new TreePath(rootNode.getPath()));
				
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.createDirectory"));
			}
			else
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.createDirectory"));
		}
	}
	
	
	/**
	 * Renombra un directorio dentro del directorio seleccionado en la árbol de directorios
	 */
	private void renameDirectory(){	
		if(!getTreeSelectedAlfrescoNode().getUuid().equals(((AlfrescoNode)rootNode.getUserObject()).getUuid())){
			String newName = (String) JOptionPane.showInputDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.input.renameDirectory"), "", JOptionPane.PLAIN_MESSAGE, null, null, getTreeSelectedAlfrescoNode().getName());
			if(newName!=null){
				try {
					if(alfrescoDocumentClient.renameNode(getTreeSelectedAlfrescoKey(), newName)){
						getTreeSelectedAlfrescoNode().setName(newName);
						((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).nodeChanged(getTreeSelectedDefaultMutableNode());
						repaintTree();
						JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.renameDirectory"));
					}
					else
						JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.renameDirectory"));
				} catch (Exception e) {
					System.out.println(e);
				}				
			}
		}
		else{
			JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.canNotRenameDirectory"));
		}
	}
	
	/**
	 * Elimina un directorio seleccionado en la árbol de directorios
	 */
	private void removeDirectory(){	
		if(!getTreeSelectedAlfrescoNode().getUuid().equals(((AlfrescoNode)rootNode.getUserObject()).getUuid())){
			int removeResult = JOptionPane.showConfirmDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.confirm.removeDirectory") + " (" + getTreeSelectedAlfrescoNode().getName() + ")", "", JOptionPane.YES_NO_OPTION);
			if(removeResult==JOptionPane.YES_OPTION){
				try {
					if(alfrescoDocumentClient.removeNode(getTreeSelectedAlfrescoKey())){
						try{	
							//((DefaultMutableTreeNode) getDynamicTreePanel().getDragAndDropTree().getLastSelectedPathComponent()).getParent()
							//((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).
							((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).removeNodeFromParent((DefaultMutableTreeNode) getDynamicTreePanel().getDragAndDropTree().getLastSelectedPathComponent());
							repaintTree();							
							//getDynamicTreePanel().getDragAndDropTree().setSelectionPath(rootNode.getp);
						}catch(Exception ex){}						
						JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.removeDirectory"));
					}
					else
						JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.removeDirectory"));
				} catch (Exception e) {
					System.out.println(e);
				}				
			}
		}
		else{
			JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.canNotRenameDirectory"));
		}
	}
	
	/**
	 * Asocia un documento seleccionado de la tabla de documentos a la/s geometría/s referenciada
	 */
	private void annexeAlfrescoDocument(){		
		AlfrescoNode documentNode = (AlfrescoNode) getDynamicTreePanel().getDragTable().getValueAt(getDynamicTreePanel().getDragTable().getSelectedRow(),0);
		
		String tempFilePath=System.getProperty("java.io.tmpdir");
		String name = documentNode.getName();
		try {
			alfrescoDocumentClient.downloadFile(new AlfrescoKey(documentNode.getUuid(), AlfrescoKey.KEY_UUID), tempFilePath, name);
		} catch (Exception e) {
			System.out.println(e);
		}		
		
				
	    try {
	    	DocumentBean documentAux = null;
	    	if(features != null || (element != null && app.equals(AlfrescoConstants.APP_INVENTORY))){	
	    		DocumentBean document = new DocumentBean();
	    		document.setId(documentNode.getUuid());
	    		document.setFileName(tempFilePath + name);
	    		document.setContent(null);
	    		document.setFechaEntradaSistema(new Date());	    		
	    		
	    		DocumentClient documentClient = new DocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
	                    ServletConstants.DOCUMENT_SERVLET_NAME);
	    		if(features != null){
	    			documentAux = documentClient.attachDocument(features, document);
	    		}
	    		else{
	    			documentAux = documentClient.attachInventarioDocument(element, document);
	    		}
	    		
		    	correctAnnexationResult(documentNode.getUuid());
	    	}
	    	else if(element != null && (app.equals(AlfrescoConstants.APP_MAJORWORKLICENSE) || app.equals(AlfrescoConstants.APP_MINORWORKLICENSE) || app.equals(AlfrescoConstants.APP_ACTIVITYLICENSE) || app.equals(AlfrescoConstants.APP_NONQUALIFIEDACTIVITYLICENSE) || app.equals(AlfrescoConstants.APP_OCUPATIONLICENSE))){	    			
	    		CSolicitudLicencia cSolicitudLicencia = new CSolicitudLicencia();
	    		cSolicitudLicencia.setIdSolicitud((Long)element);	    		
	    		Vector anexos = new Vector();
	    		CAnexo anexo = new CAnexo();
	    		anexo.setIdAnexo(documentNode.getUuid());
	    		anexo.setFileName(name);
	    		anexo.setPath(tempFilePath + name);	  
	    		//anexo.setTipoAnexo(0);
	    		anexo.setContent(null);
	    		anexos.add(anexo);
	    		cSolicitudLicencia.setAnexos(anexos);  
	    		COperacionesLicencias.insertaAnexos(cSolicitudLicencia);	    		
		    	correctAnnexationResult(documentNode.getUuid());
	    	}
	    	else if(element != null && app.equals(AlfrescoConstants.APP_CEMENTERY)){	    			
	    		CementerioDocumentClient documentClient = new CementerioDocumentClient(AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_SERVER_URL) + WebAppConstants.CEMENTERIOS_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME +
	                    ServletConstants.CEMENTERIO_DOCUMENT_SERVLET_NAME);
	    		    		
	    		DocumentBean document = new DocumentBean();
	    		document.setId(documentNode.getUuid());
	    		document.setFileName(tempFilePath + name);
	    		document.setContent(null);
	    		document.setFechaEntradaSistema(new Date());
	    		
			    document = ((CementerioDocumentClient)documentClient).attachDocument((ElemCementerioBean) element, document);
				
		    	correctAnnexationResult(documentNode.getUuid());
	    	}
	    	else{
	    		JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.annexe"));	
	    	}
		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.annexe"));	
		}				
	}
	
	/**
	 * Resultado tras un anexado correcto
	 */
	private void correctAnnexationResult(String uuid){		
		getDynamicTreePanel().getChildFiles(getTreeSelectedDefaultMutableNode());
		documentsUuid.add(uuid);
		repaintTree();
		JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.annexe"));	
	}
	
	/**
	 * Repinta el árbol de directorios
	 */
	private void repaintTree(){
		dynamicTreePanel.setParentsUuid(getParentsUuid(documentsUuid));
		dynamicTreePanel.setParentsPathUuid(getParentsPathUuid(documentsUuid));
		dynamicTreePanel.invalidate();
		dynamicTreePanel.revalidate();
		dynamicTreePanel.repaint();
	}
	
	
	/**
	 * Sube a Alfresco un documento del sistema local
	 */
	private void uploadLocalDocument(){		
		if(getFileChooser(JFileChooser.FILES_ONLY).showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			Node node = null;
			try {
				node = alfrescoDocumentClient.addFileFromParent(getTreeSelectedAlfrescoKey(), getFileChooser().getSelectedFile());
			} catch (Exception e) {
				System.out.println(e);
			}		
			if(node!=null){
				getDynamicTreePanel().getChildFiles(getTreeSelectedDefaultMutableNode());
				
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.uploadFile"));
			}
			else
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.uploadFile"));
		}
	}
	
	/**
	 * Descarga un documento seleccionado de la tabla de documentos
	 */
	private void downloadAlfrescoDocument(){
		if(getFileChooser(JFileChooser.DIRECTORIES_ONLY).showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			AlfrescoNode node = (AlfrescoNode) getDynamicTreePanel().getDragTable().getValueAt(getDynamicTreePanel().getDragTable().getSelectedRow(),0);
			//if(alfrescoManager.downloadFile(new AlfrescoKey(node.getUuid(), AlfrescoKey.KEY_UUID), getFileChooser().getSelectedFile().getAbsolutePath()))
			try {
				alfrescoDocumentClient.downloadFile(new AlfrescoKey(node.getUuid(), AlfrescoKey.KEY_UUID), getFileChooser().getSelectedFile().getAbsolutePath(), node.getName());
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.ok.downloadFile"));			
			} catch (Exception e) {
				System.out.println(e);
				JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.downloadFile"));
			}
		}
	}
	
	/**
	 * Previsualiza el documento seleccionado de la tabla de documentos
	 */
	private void previewAlfrescoDocument(){
		AlfrescoNode node = (AlfrescoNode) getDynamicTreePanel().getDragTable().getValueAt(getDynamicTreePanel().getDragTable().getSelectedRow(),0);
		try {
			String tempFilePath=System.getProperty("java.io.tmpdir");
			if(alfrescoDocumentClient.downloadFile(new AlfrescoKey(node.getUuid(), AlfrescoKey.KEY_UUID), tempFilePath, node.getName())){
				//if (CUtilidadesComponentes.isWindows()){
               
                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + tempFilePath + "\\" + node.getName() + "\"");

                //}
				return;
			}
		} catch (Exception e) {
			System.out.println(e);			
		}
		JOptionPane.showMessageDialog(this, AppContext.getApplicationContext().getI18nString("alfresco.error.previewFile"));
	} 

	/**
	 * Inicializa una instancia de directoriesTreePanel y la devuelve
	 * @return DynamicTreePanel: TreePanel directoriesTreePanel
	 */
	private DynamicTreePanel getDynamicTreePanel() {
		if (dynamicTreePanel == null) {		
			getTreeDirectories(rootNode);			
			dynamicTreePanel = new DynamicTreePanel(new DefaultTreeModel(
					rootNode)){
				public boolean moveDirectory(DefaultMutableTreeNode dropNode, DefaultMutableTreeNode destinyNode){	
					return moveNode(dropNode, destinyNode);
				};
				public boolean moveDocument(DefaultMutableTreeNode dropNode, DefaultMutableTreeNode destinyNode){	
					return moveNode(dropNode, destinyNode);
				};		
				public void activateDirectoryButtons() {
					getBtnCreateDirectory().setEnabled(true);
					getBtnRenameDirectory().setEnabled(true);
				};
				public void getChildFiles(DefaultMutableTreeNode parentNode) {
					final DefaultMutableTreeNode finalParentNode = parentNode;
					final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
				    progressDialog.allowCancellationRequests();
				    progressDialog.setTitle(AppContext.getApplicationContext().getI18nString("alfresco.message.loadingFiles"));
				    progressDialog.report(AppContext.getApplicationContext().getI18nString("alfresco.message.loadingFiles"));
				    progressDialog.addComponentListener(new ComponentAdapter(){
				    	public void componentShown(ComponentEvent e){
				    		new Thread(new Runnable(){
				    			public void run(){
				    				try {				                                
										if(app.equals(AlfrescoConstants.APP_GENERAL)){
											FeatureBean [] featureBeans = new FeatureBean[features.length];
											//int i = 0;
											for(GeopistaFeature feature : ((GeopistaFeature [])features)){
												featureBeans [0] = new FeatureBean(feature.getLayer().getId_LayerDataBase(), feature.getSystemId());
												    			//i++;
											}											    		
											getDynamicTreePanel().getDragTable().setModel(alfrescoDocumentClient.getChildFiles(new AlfrescoKey(((AlfrescoNode)finalParentNode.getUserObject()).getUuid(), AlfrescoKey.KEY_UUID), featureBeans));
												    		
										}
												    	//else if(element != null){
										else if(app.equals(AlfrescoConstants.APP_INVENTORY) || app.equals(AlfrescoConstants.APP_MAJORWORKLICENSE) || app.equals(AlfrescoConstants.APP_MINORWORKLICENSE) || app.equals(AlfrescoConstants.APP_ACTIVITYLICENSE) || app.equals(AlfrescoConstants.APP_NONQUALIFIEDACTIVITYLICENSE) || app.equals(AlfrescoConstants.APP_OCUPATIONLICENSE) || app.equals(AlfrescoConstants.APP_CEMENTERY)){
											getDynamicTreePanel().getDragTable().setVisible(false);
											DefaultTableModel tableModel = alfrescoDocumentClient.getChildFiles(new AlfrescoKey(((AlfrescoNode)finalParentNode.getUserObject()).getUuid(), AlfrescoKey.KEY_UUID), new Object [] { element });
											if(tableModel != null){
												getDynamicTreePanel().getDragTable().setModel(tableModel);
											}
								    	}
										getDynamicTreePanel().getDragTable().setTableColumnModel();
										getDynamicTreePanel().getDragTable().setVisible(true);
										
										if(getTreeSelectedDefaultMutableNode()==null || getTreeSelectedDefaultMutableNode().getParent()==null || getTreeSelectedDefaultMutableNode().getChildCount()>0 || getDynamicTreePanel().getDragTable().getRowCount()>0){
											getBtnRemoveDirectory().setEnabled(false);	
										}
										else{
											getBtnRemoveDirectory().setEnabled(true);
										}
								//						if(getDynamicTreePanel().getDragTable().getColumnModel().getColumnCount()>0){
	//													TableColumn col = getDynamicTreePanel().getDragTable().getColumnModel().getColumn(1);
	//													col.setPreferredWidth(24);
	//													col.setWidth(24);
	//													col.setMaxWidth(24);
	//													col.setMinWidth(24);
														//getDynamicTreePanel().getDragTable().setVisible(true);
								//						}
									} catch (Exception e) {
										System.out.println(e);
										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(),
																AppContext.getApplicationContext()
																		.getI18nString("SQLError.Titulo"),
																AppContext.getApplicationContext()
																		.getI18nString("SQLError.Aviso"),
																StringUtil.stackTrace(e));
									} finally {
										progressDialog.setVisible(false);
									}
				    			}
				    			}).start();
				            }
		                });
		            if (AppContext.getApplicationContext().getMainFrame() == null) // sin ventana de referencia
		                GUIUtil.centreOnScreen(progressDialog);
		            else
		                GUIUtil.centreOnWindow(progressDialog);
		            progressDialog.setVisible(true);
				};
				@Override
				public void workDone(){
					repaintTree();
				}
				@Override
				public void activateFileButtons() {
					getBtnAnnexeDocument().setEnabled(true);
					getBtnDownloadDocument().setEnabled(true);
					getBtnPreviewDocument().setEnabled(true);
				}
				@Override
				public void deactivateFileButtons() {
					getBtnAnnexeDocument().setEnabled(false);
					getBtnDownloadDocument().setEnabled(false);
					getBtnPreviewDocument().setEnabled(false);
				}
			};
			repaintTree();
		}
		
		return dynamicTreePanel;
	}
	
	/**
	 * Devuelve un set con los uuid de la ruta de directorios padre de los documentos asociados a la feature
	 * @param documents: Lista de uuid los documentos asociados a la feature
	 * @return ArrayList<String>: Set con los uuid de la ruta de directorios padre de los documentos asociados a la feature 
	 */
	private HashSet<String> getParentsPathUuid(ArrayList<String> documents){
		HashSet<String> uuidFolders = new HashSet<String>();
		Iterator<String> itDocuments = documents.iterator();
		while(itDocuments.hasNext()){
			try {
				String uuid = itDocuments.next();
				Node parentNode = null;
				do{
					parentNode = alfrescoDocumentClient.getParentNode(new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID));
					if(parentNode != null){
						uuid = parentNode.getReference().getUuid();
						uuidFolders.add(uuid);
					}					
				}while(parentNode != null);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return uuidFolders;
	}
	
	/**
	 * Devuelve el uuid del padre de los documentos asociados a la feature
	 * @param documents: Lista de uuid los documentos asociados a la feature
	 * @return ArrayList<String>: Lista con los path de los documentos asociados a la feature 
	 */
	private HashSet<String> getParentsUuid(ArrayList<String> documents){
		HashSet<String> uuidFolders = new HashSet<String>();
		Iterator<String> itDocuments = documents.iterator();
		while(itDocuments.hasNext()){
			try {
				String uuid = itDocuments.next();
					Node parentNode = alfrescoDocumentClient.getParentNode(new AlfrescoKey(uuid, AlfrescoKey.KEY_UUID));
					if(parentNode != null){
						uuid = parentNode.getReference().getUuid();
						uuidFolders.add(uuid);
					}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return uuidFolders;
	}

	/**
	 * Inicializa una instancia de generalPanel y la devuelve
	 * @return JPanel: Panel generalPanel
	 */
	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			generalPanel = new JPanel();
			generalPanel.setBorder(BorderFactory.createEmptyBorder());
			generalPanel.setLayout(new BorderLayout());
			generalPanel.add(getDynamicTreePanel(), BorderLayout.NORTH);
			generalPanel.add(getButtonSplitPane(), BorderLayout.SOUTH);
		}
		return generalPanel;
	}

	/**
	 * Inicializa una instancia de directoryPanel y la devuelve
	 * @return JPanel: Panel directoryPanel
	 */
	private JPanel getDirectoryPanel() {
		if (directoryPanel == null) {
			directoryPanel = new JPanel();
			directoryPanel.add(getBtnCreateDirectory());
			directoryPanel.add(getBtnRenameDirectory());
			directoryPanel.add(getBtnRemoveDirectory());
		}
		return directoryPanel;
	}

	/**
	 * Inicializa una instancia de filePanel y la devuelve
	 * @return JPanel: Panel filePanel
	 */
	private JPanel getFilePanel() {
		if (filePanel == null) {
			filePanel = new JPanel();
			filePanel.add(getBtnAddFile());
			filePanel.add(getBtnAnnexeDocument());
			filePanel.add(getBtnDownloadDocument());
			filePanel.add(getBtnPreviewDocument());
		}
		return filePanel;
	}

	/**
	 * Inicializa una instancia de buttonSplitPane y la devuelve
	 * @return JSplitPane: SplitPane buttonSplitPane
	 */
	private JSplitPane getButtonSplitPane() {
		if (buttonSplitPane == null) {
			buttonSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					getDirectoryPanel(), getFilePanel());
			//buttonSplitPane.setMinimumSize(new Dimension(700, 35));
			buttonSplitPane.setPreferredSize(new Dimension(700, 35));
			buttonSplitPane.setSize(new Dimension(700, 35));
			buttonSplitPane.setDividerSize(0);
			buttonSplitPane.setContinuousLayout(true);
			buttonSplitPane.setDividerLocation(350);	
			buttonSplitPane.setEnabled(false);
		}
		return buttonSplitPane;
	}

	/**
	 * Inicializa una instancia de btnCreateDirectory y la devuelve
	 * @return JButton: Botón btnCreateDirectory
	 */
	private JButton getBtnCreateDirectory() {
		if (btnCreateDirectory == null) {
			btnCreateDirectory = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.create"));
			btnCreateDirectory.setEnabled(false);
			btnCreateDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createNewDirectory();
				}				
			});
		}
		return btnCreateDirectory;
	}
	
	/**
	 * Inicializa una instancia de btnRenameDirectory y la devuelve
	 * @return JButton: Botón btnRenameDirectory
	 */
	private JButton getBtnRenameDirectory() {
		if (btnRenameDirectory == null) {
			btnRenameDirectory = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.rename"));
			btnRenameDirectory.setEnabled(false);
			btnRenameDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					renameDirectory();
				}				
			});
		}
		return btnRenameDirectory;
	}
	
	/**
	 * Inicializa una instancia de btnRemoveDirectory y la devuelve
	 * @return JButton: Botón btnRemoveDirectory
	 */
	private JButton getBtnRemoveDirectory() {
		if (btnRemoveDirectory == null) {
			btnRemoveDirectory = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.remove"));
			btnRemoveDirectory.setEnabled(false);
			btnRemoveDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeDirectory();
				}				
			});
		}
		return btnRemoveDirectory;
	}

	/**
	 * Inicializa una instancia de btnAnnexeDocument y la devuelve
	 * @return JButton: Botón btnAnnexeDocument
	 */
	private JButton getBtnAnnexeDocument() {
		if (btnAnnexeDocument == null) {
			btnAnnexeDocument = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.annexe"));
			btnAnnexeDocument.setEnabled(false);
			btnAnnexeDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					annexeAlfrescoDocument();
				}
			});		
		}
		return btnAnnexeDocument;
	}

	/**
	 * Inicializa una instancia de btnAddFile y la devuelve
	 * @return JButton: Botón btnAddFile
	 */
	private JButton getBtnAddFile() {
		if (btnAddFile == null) {
			btnAddFile = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.uploadFile"));
			btnAddFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					uploadLocalDocument();
				}
			});			
		}
		return btnAddFile;
	}

	/**
	 * Inicializa una instancia de btnDownloadDocument y la devuelve
	 * @return JButton: Botón btnDownloadDocument
	 */
	private JButton getBtnDownloadDocument() {
		if (btnDownloadDocument == null) {
			btnDownloadDocument = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.downloadFile"));
			btnDownloadDocument.setEnabled(false);
			btnDownloadDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					downloadAlfrescoDocument();
				}
			});
		}
		return btnDownloadDocument;
	}
	
	/**
	 * Inicializa una instancia de btnPreviewDocument y la devuelve
	 * @return JButton: Botón btnPreviewDocument
	 */
	private JButton getBtnPreviewDocument() {
		if (btnPreviewDocument == null) {
			btnPreviewDocument = new JButton(AppContext.getApplicationContext().getI18nString("alfresco.button.previewFile"));
			btnPreviewDocument.setEnabled(false);
			btnPreviewDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					previewAlfrescoDocument();
				}
			});
		}
		return btnPreviewDocument;
	}
	
	/**
	 * Devuelve un selector
	 * @param: fileSelectionMode: modo de selección
	 * @return JFileChooser: Selector
	 */
	private JFileChooser getFileChooser(int fileSelectionMode) {
		if(fileChooser == null)
			fileChooser = new JFileChooser();	
		fileChooser.setFileSelectionMode(fileSelectionMode);
		return fileChooser;
	}
	
	/**
	 * Devuelve un selector de ficheros y carpetas
	 * @return JFileChooser: Selector de ficheros y carpetas
	 */
	private JFileChooser getFileChooser() {		
		return getFileChooser(JFileChooser.FILES_AND_DIRECTORIES);
	};

}
