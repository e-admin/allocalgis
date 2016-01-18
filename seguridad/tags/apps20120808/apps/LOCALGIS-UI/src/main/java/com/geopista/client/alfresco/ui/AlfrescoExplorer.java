package com.geopista.client.alfresco.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import com.geopista.utils.alfresco.beans.FeatureBean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.util.Constants;

import com.geopista.app.AppContext;
import com.geopista.app.document.DocumentPanel;
import com.geopista.app.document.InfoDocumentPanel;
import com.geopista.client.alfresco.servlet.AlfrescoDocumentClient;
import com.geopista.client.alfresco.ui.panel.DynamicTreePanel;
import com.geopista.feature.GeopistaFeature;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.protocol.document.DocumentClient;
import com.geopista.utils.alfresco.manager.beans.AlfrescoKey;
import com.geopista.utils.alfresco.manager.beans.AlfrescoNode;
import com.geopista.utils.alfresco.manager.utils.AlfrescoManagerUtils;

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
	private JButton btnAnnexateDocument = null;
	private JButton btnAddFile = null;
	private JButton btnDownloadDocument = null;
	//private JButton btnDeanexateDocument = null;
	private JFileChooser fileChooser = null;
	
	/**
	 * Variables
	 */
	private DefaultMutableTreeNode rootNode = null;
	private AlfrescoDocumentClient alfrescoDocumentClient = null;
	private JPanel parentPanel = null;
	private Object [] features = null;
	private JScrollPane parentScrollPane = null;
	private Object inventarioElement = null;
	private String idMunicipality = null;
	private String app = null;
	
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
	 * @param parentPanel: Panel del padre que contiene los nombres de los documentos asociados
	 * @param features: Array con las geometrías seleccionadas
	 * @param idMunicipality: Identificador de municipio (entidad)
	 * @param app: Nombre del tipo de aplicación documental LocalGIS
	 */
	public AlfrescoExplorer(JFrame owner, JPanel parentPanel, Object [] features, String idMunicipality, String app) {
		super(owner);
		this.parentPanel = parentPanel;
		this.features = features;
		this.idMunicipality = idMunicipality;
		this.app = app;
		init();
	}
	
	/**
	 * Constructor
	 * @param owner: Formulario padre
	 * @param parentPanel: Panel del padre que contiene los nombres de los documentos asociados
	 * @param inventarioElement: Elemento del inventario
	 * @param idMunicipality: Identificador de municipio (entidad)
	 * @param app: Nombre del tipo de aplicación documental LocalGIS
	 */
	public AlfrescoExplorer(JFrame owner, JScrollPane parentScrollPane, Object inventarioElement, String idMunicipality, String app) {
		super(owner);
		this.parentScrollPane = parentScrollPane;
		this.inventarioElement = inventarioElement;
		this.idMunicipality = idMunicipality;
		this.app = app;
		init();
	}
	
	/**
	 * Inicializa la aplicación
	 */
	private void init() {	
		alfrescoDocumentClient = new AlfrescoDocumentClient(AppContext.getApplicationContext().getString(AppContext.HOST_ADMCAR) + "/" + WebAppConstants.ALFRESCO_WEBAPP_NAME +
                ServletConstants.ALFRESCO_DOCUMENT_SERVLET_NAME);
		
		setTitle("Gestor Documental - Integración con Alfresco");
		try {
			setTitle(getTitle() + " (" + alfrescoDocumentClient.getMunicipalityName(idMunicipality) + ")");
		} catch (Exception e) {
			System.out.println(e);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);		
				dispose();
			}
		});
		setModal(true);
		setResizable(false);
		setSize(new Dimension(200, 200));
		setMaximumSize(new Dimension(200, 200));		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		try {            
			//UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
		} catch (Exception e) {}
	
		rootNode = getRootDirectoryNode(idMunicipality, app);
		
		if(rootNode == null || rootNode.getUserObject() == null){
			//JOptionPane.showMessageDialog(this, "Error en los permisos. La aplicación se cerrará.");
			dispose();
			//setVisible(false);
		}	
		else{
			dynamicTreePanel = getDynamicTreePanel();
			btnCreateDirectory = getBtnCreateDirectory();
			btnRenameDirectory = getBtnRenameDirectory();
			btnAnnexateDocument = getBtnAnnexateDocument();
			btnAddFile = getBtnAddFile();
			btnDownloadDocument = getBtnDownloadDocument();
			directoryPanel = getDirectoryPanel();
			filePanel = getFilePanel();
			buttonSplitPane = getButtonSplitPane();
			generalPanel = getGeneralPanel();
				
			getContentPane().add(getGeneralPanel());
			pack();		
		}
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
		String newDirectory = JOptionPane.showInputDialog(this, "Introduzca el nombre del nuevo directorio");
		if(newDirectory!=null){
			//Node node = alfrescoManager.addDirectoryFromParent(getTreeSelectedAlfrescoKey(), newDirectory);	
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
				//getDirectoriesTreePanel().getTree().setModel(new DefaultTreeModel(rootNode));
				
				((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).nodeStructureChanged(getTreeSelectedDefaultMutableNode());
			
				JOptionPane.showMessageDialog(this, "El directorio se ha creado correctamente.");
			}
			else
				JOptionPane.showMessageDialog(this, "Error al crear el directorio.");
		}
		
		//el boton solo esta activo si un directorio esta seleccionado en el arbol directorios
		//Se llama del evento onclickbutton "crear directorio"
		//Solicita con un input, el nombre del directorio
		//llama a la clase createDirectoryFromParentUuid de alfrescomanager 
		//con el nombre del directorio y la recuperacion de la carpeta del arbol seleccionada
		//el nodo devuelto se introduce en el arbol como hijo del nodo actual seleccionado y se refresca la iu
	}
	
	
	/**
	 * Renombra un directorio dentro del directorio seleccionado en la árbol de directorios
	 */
	private void renameDirectory(){	
		String newName = JOptionPane.showInputDialog(this, "Introduzca el nuevo nombre del directorio", getTreeSelectedAlfrescoNode().getName());
		if(newName!=null){
			try {
				if(alfrescoDocumentClient.renameNode(getTreeSelectedAlfrescoKey(), newName)){
					getTreeSelectedAlfrescoNode().setName(newName);
					((DefaultTreeModel)getDynamicTreePanel().getDragAndDropTree().getModel()).nodeChanged(getTreeSelectedDefaultMutableNode());
					JOptionPane.showMessageDialog(this, "El directorio se ha renombrado correctamente.");
				}
				else
					JOptionPane.showMessageDialog(this, "Error al renombrar el directorio.");
			} catch (Exception e) {
				System.out.println(e);
			}				
		}
		
		//el boton solo esta activo si un directorio esta seleccionado en el arbol directorios
		//Se llama del evento onclickbutton "crear directorio"
		//Solicita con un input, el nombre del directorio
		//llama a la clase createDirectoryFromParentUuid de alfrescomanager 
		//con el nombre del directorio y la recuperacion de la carpeta del arbol seleccionada
		//el nodo devuelto se introduce en el arbol como hijo del nodo actual seleccionado y se refresca la iu
	}
	
	/**
	 * Asocia un documento seleccionado de la tabla de documentos a la/s geometría/s referenciada
	 */
	private void annexateAlfrescoDocument(){		
		//Crear una clase manager de integracion localgis-alfresco para llamarla desde aqui y desde la parte de localgis de edicion de atributos de una feature
		//En esa clase ira tambien la edicion (si un documento estaba en el sistema antiguo, al modificarlo este se subira a alfresco y cambiara la info de la bd)
				
		//el boton solo esta activo si un fichero esta seleccionado en la tabla de ficheros
		//poner una marca cuando un fichero esta ya asociado a la feature
		//al asociar un documento se almacenara la informacion en la bd localgis (clase: localgis-alfresco)
		//se enviaran los datos para la insercion en la bd (uuid, name,....)
		//se recupera un ok (boolean) de la clase localgis-alfresco manager		 
		//Documento sin Seleccionar de la tabla o relacionado ya -> el boton permanecera disabled

		AlfrescoNode documentNode = (AlfrescoNode) getDynamicTreePanel().getDragTable().getValueAt(getDynamicTreePanel().getDragTable().getSelectedRow(),0);
		
		String tempFilePath=System.getProperty("java.io.tmpdir");
		String name = documentNode.getName();
		try {
			alfrescoDocumentClient.downloadFile(new AlfrescoKey(documentNode.getUuid(), AlfrescoKey.KEY_UUID), tempFilePath, name);
		} catch (Exception e) {
			System.out.println(e);
		}		
		DocumentBean document = new DocumentBean();
		document.setId(documentNode.getUuid());
		document.setFileName(tempFilePath + name);
		document.setContent(null);
		document.setFechaEntradaSistema(new Date());
				
		DocumentClient documentClient = new DocumentClient(AppContext.getApplicationContext().getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                ServletConstants.DOCUMENT_SERVLET_NAME);			
	    try {
	    	DocumentBean documentAux = null;
	    	if(features != null){		
	    		documentAux = documentClient.attachDocument(features, document);
		    	if(parentPanel instanceof InfoDocumentPanel){				
					InfoDocumentPanel infoDocumentPanel = (InfoDocumentPanel) parentPanel;
					infoDocumentPanel.setFeatureDocuments(documentAux);
					getDynamicTreePanel().getChildFiles(getTreeSelectedDefaultMutableNode());
					JOptionPane.showMessageDialog(this, "El documento se ha asociado correctamente.");
				}
	    	}
	    	else if(inventarioElement != null){
	    		documentAux = documentClient.attachInventarioDocument(inventarioElement, document);
	    		if(parentScrollPane instanceof DocumentPanel){				
	    			DocumentPanel documentPanel = (DocumentPanel) parentScrollPane;
	    			documentPanel.setDocumentos(documentClient.getAttachedDocuments(inventarioElement));
	    			documentPanel.actualizarModelo();
					getDynamicTreePanel().getChildFiles(getTreeSelectedDefaultMutableNode());
					JOptionPane.showMessageDialog(this, "El documento se ha asociado correctamente.");
				}
	    	}
	    	return;
		} catch (Exception e) {
			System.out.println(e);
		}		
		JOptionPane.showMessageDialog(this, "Error al asociar el documento.");	
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
			//Node node = alfrescoManager.addFileFromParent(getTreeSelectedAlfrescoKey(), getFileChooser().getSelectedFile());		
			if(node!=null){
				getDynamicTreePanel().getChildFiles(getTreeSelectedDefaultMutableNode());
				
				JOptionPane.showMessageDialog(this, "El documento se ha subido correctamente.");
			}
			else
				JOptionPane.showMessageDialog(this, "Error al subir el documento.");
		}
		//el boton solo esta activo si un directorio esta seleccionado en el arbol directorios
		//se abre un filechooser y se elije un documento
		//solo se sube a alfresco, no se asocia automaticamente
		//se enviara la instancia de File() con el fichero seleccionado
		//se crea el fichero en alfresco, se recupera el nodo y se añade a la tabla actual (se hace de nuevo la peticion a getChildFiles())
	}
	
	/**
	 * Descarga un documento seleccionado de la tabla de documentos
	 */
	private void downloadAlfrescoDocument(){
		//el boton solo esta activo si un fichero esta seleccionado en la tabla de ficheros
		//Se abre un filechooser y se elije un directorio
		//Se enviara la direccion y el nodo con el content alfresco y se generara un File a partir de ellos (si no hay una opcion mejor con alfresco api)
		//se descarga el documento en el directorio elegido

		if(getFileChooser(JFileChooser.DIRECTORIES_ONLY).showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			AlfrescoNode node = (AlfrescoNode) getDynamicTreePanel().getDragTable().getValueAt(getDynamicTreePanel().getDragTable().getSelectedRow(),0);
			//if(alfrescoManager.downloadFile(new AlfrescoKey(node.getUuid(), AlfrescoKey.KEY_UUID), getFileChooser().getSelectedFile().getAbsolutePath()))
			try {
				alfrescoDocumentClient.downloadFile(new AlfrescoKey(node.getUuid(), AlfrescoKey.KEY_UUID), getFileChooser().getSelectedFile().getAbsolutePath(), node.getName());
				JOptionPane.showMessageDialog(this, "El documento se ha descargado correctamente.");			
			} catch (Exception e) {
				System.out.println(e);
				JOptionPane.showMessageDialog(this, "Error al descargar el documento.");
			}
		}
	}
	
	//private void preview(){} // ¿?¿?¿?¿?¿?¿?¿?¿

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
				public void getChildFiles(DefaultMutableTreeNode parentNode) {
					try {
				    	if(features != null){
				    		FeatureBean [] featureBeans = new FeatureBean[features.length];
				    		int i = 0;
				    		for(GeopistaFeature feature : ((GeopistaFeature [])features)){
				    			featureBeans [0] = new FeatureBean(feature.getLayer().getId_LayerDataBase(), feature.getSystemId());
				    			i++;
				    		}
				    		getDynamicTreePanel().getDragTable().setModel(alfrescoDocumentClient.getChildFiles(parentNode, featureBeans));
				    		
				    	}
				    	else if(inventarioElement != null)
							getDynamicTreePanel().getDragTable().setModel(alfrescoDocumentClient.getChildFiles(parentNode, new Object [] { inventarioElement }));
						if(getDynamicTreePanel().getDragTable().getColumnModel().getColumnCount()>0){
							getDynamicTreePanel().getDragTable().getColumnModel().getColumn(1).setCellRenderer(getDynamicTreePanel().getDragTable().getDragTableCellRenderer());
							TableColumn col = getDynamicTreePanel().getDragTable().getColumnModel().getColumn(1);
							col.setPreferredWidth(24);
							col.setWidth(24);
							col.setMaxWidth(24);
							col.setMinWidth(24);
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				};
			};
		}
		return dynamicTreePanel;
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
			filePanel.add(getBtnAnnexateDocument());
			filePanel.add(getBtnAddFile());
			filePanel.add(getBtnDownloadDocument());
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
			buttonSplitPane.setMinimumSize(new Dimension(50, 0));
			buttonSplitPane.setDividerSize(0);
			buttonSplitPane.setContinuousLayout(true);
			buttonSplitPane.setDividerLocation(298);
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
			btnCreateDirectory = new JButton("Crear");
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
			//CAMBIAR IDIOMA
			btnRenameDirectory = new JButton("Renombrar");
			btnRenameDirectory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					renameDirectory();
				}				
			});
		}
		return btnRenameDirectory;
	}

	/**
	 * Inicializa una instancia de btnAnnexateDocument y la devuelve
	 * @return JButton: Botón btnAnnexateDocument
	 */
	private JButton getBtnAnnexateDocument() {
		if (btnAnnexateDocument == null) {
			btnAnnexateDocument = new JButton("Anexar");
			btnAnnexateDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					annexateAlfrescoDocument();
				}
			});		
		}
		return btnAnnexateDocument;
	}

	/**
	 * Inicializa una instancia de btnAddFile y la devuelve
	 * @return JButton: Botón btnAddFile
	 */
	private JButton getBtnAddFile() {
		if (btnAddFile == null) {
			btnAddFile = new JButton("Añadir");
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
			btnDownloadDocument = new JButton("Descargar");
			btnDownloadDocument.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					downloadAlfrescoDocument();
				}
			});
		}
		return btnDownloadDocument;
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
	}

}
