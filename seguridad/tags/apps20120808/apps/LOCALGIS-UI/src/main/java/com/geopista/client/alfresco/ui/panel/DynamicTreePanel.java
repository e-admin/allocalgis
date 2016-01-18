package com.geopista.client.alfresco.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-04-2012
 * @version 1.0
 * @ClassComments Panel de árbol de directorios y listado de ficheros
 */
public class DynamicTreePanel extends JPanel {
	
	/**
	 * Variables
	 */
    private DragAndDropTree dragAndDropTree = null;
    private DragTable dragTable = null;
	private JScrollPane directoriesTreePanelScroller = null;
	private JScrollPane childFilesTableScroller = null;
    private JSplitPane contentSplitPane = null;        
    private DragSource dragSource;	
    private DropTarget dropTarget; 
    private DefaultMutableTreeNode dropTargetNode = null; 
    private DefaultMutableTreeNode draggedNode = null;
    
    /**
     * Constructor
     * @param model: Modelo con el árbol de directorios
     */
    public DynamicTreePanel(DefaultTreeModel model) {
    	init(model);    	
    }

    /**
     * Inicializa el panel
     * @param model: Modelo con el árbol de directorios
     */
    private void init(DefaultTreeModel model){
    	
    	dragAndDropTree = getDragAndDropTree(model);
    	dragTable = getDragTable();
    	directoriesTreePanelScroller = getDirectoriesTreePanelScroller();
		childFilesTableScroller = getChildFilesTableScroller();
		contentSplitPane = getContentSplitPane();
		
        setLayout(new BorderLayout());
        add(getContentSplitPane(), BorderLayout.CENTER);    	
    }
    
    /**
     * Inicializa una instancia del árbol de directorios y lo devuelve
     * @param model: Modelo con el árbol de directorios
     * @return JTree: Árbol de directorios
     */
    private DragAndDropTree getDragAndDropTree(DefaultTreeModel model) {
    	if(dragAndDropTree == null){
    		dragAndDropTree = new DragAndDropTree(model);
        	//invisibilizar directorio raiz
        	//tree.setRootVisible( false ); 
        	dragAndDropTree.setRootVisible( true ); 
        	dragAndDropTree.setRequestFocusEnabled(true);
        	dragAndDropTree.setShowsRootHandles( true );
            dragAndDropTree.putClientProperty( "JTree.lineStyle", "Angled" );
            dragAndDropTree.addTreeSelectionListener(
    			new TreeSelectionListener() {
    				@Override
    				public void valueChanged(TreeSelectionEvent e) {
    					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();		
		    			if (parentNode != null && parentNode.getUserObject() != null){
		    				getChildFiles(parentNode);
		    			}
    				}
    			});
    	}
    	return dragAndDropTree;
    }
    
    /**
     * Devuelve la instancia del árbol
     * @return DragAndDropJTree: Árbol de directorios
     */
    public DragAndDropTree getDragAndDropTree() {    	
    	return dragAndDropTree;
    }
    
    /**
     * Devuelve la instancia de la tabla de documentos
     * @return DragJTable: Tabla de documentos
     */
    public DragTable getDragTable() {    
    	if(dragTable == null){
    		dragTable = new DragTable();
    	}    	
    	return dragTable;
    }
    
	/**
	 * Inicializa una instancia de directoriesTreePanelScroller y la devuelve
	 * @return JScrollPane: ScrollPane directoriesTreePanelScroller
	 */
	private JScrollPane getDirectoriesTreePanelScroller() {
		if (directoriesTreePanelScroller == null) {
			directoriesTreePanelScroller = new JScrollPane(
					getDragAndDropTree());
		}
		return directoriesTreePanelScroller;
	}
	
	/**
	 * Inicializa una instancia de childFilesTableScroller y la devuelve
	 * @return JScrollPane: ScrollPane childFilesTableScroller
	 */
	private JScrollPane getChildFilesTableScroller() {
		if (childFilesTableScroller == null) {
			childFilesTableScroller = DragTable
					.createScrollPaneForTable(getDragTable());
			//childFilesTableScroller.setBackground(Color.WHITE);
			childFilesTableScroller.getViewport().setBackground(Color.white);
		}
		return childFilesTableScroller;
	}
	
	/**
	 * Inicializa una instancia de contentSplitPane y la devuelve
	 * @return JSplitPane: SplitPane contentSplitPane
	 */
    private JSplitPane getContentSplitPane() {
		if (contentSplitPane == null) {
			contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					getDirectoriesTreePanelScroller(),
					getChildFilesTableScroller());
			contentSplitPane.setDividerSize(4);
			contentSplitPane.setContinuousLayout(true);
			contentSplitPane.setDividerLocation(298);
			contentSplitPane.setEnabled(false);
		}
		return contentSplitPane;
	}   
    
    /**
	 * Asigna a la tabla de ficheros el modelo
	 * @param parentNode: Nodo padre
	 */
	public void getChildFiles(DefaultMutableTreeNode parentNode) {}
	
	/**
	 * Tabla con el listado de ficheros que pueden ser desplazados a otro directorio 
	 */
	public class DragTable extends JTable{

		/**
		 * Variables
		 */
		private DragTableCellRenderer dragTableCellRenderer = new DragTableCellRenderer();
		
		/**
		 * Constructor
		 */
		public DragTable(){
			setShowHorizontalLines(false);
			setShowVerticalLines(false);
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setColumnModel(new DefaultTableColumnModel() {
				public void moveColumn(int columnIndex, int newIndex) {
					if (columnIndex == 0 || newIndex == 0)
						return;
					super.moveColumn(columnIndex, newIndex);
				}
				
				
			});
			setBackground(Color.WHITE);
			setDragEnabled(true);		
			final DragTable dragTable = this;
			setTransferHandler(new TransferHandler() {
				
				  public boolean canImport(TransferSupport support) {
					return false;
			      }
			      
			      @Override
			      public int getSourceActions(JComponent c) {
			         return TransferHandler.COPY_OR_MOVE;
			      }
			      
			      @Override
			      protected Transferable createTransferable(JComponent c) {
			         assert (c == dragTable);
			         DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode();
			         defaultMutableTreeNode.setUserObject((DefaultMutableTreeNode)dragTable.getModel().getValueAt(dragTable.getSelectedRow(), 0));
			         defaultMutableTreeNode.setAllowsChildren(false);
			         defaultMutableTreeNode.setParent(null);
			         draggedNode = defaultMutableTreeNode;
					 return new DataHandler(defaultMutableTreeNode, dragAndDropTree.dataFlavor.getMimeType());
			      }
			
			      @Override
			      protected void exportDone(JComponent c, Transferable t, int act) {
			         if (act == TransferHandler.MOVE) {
			        	 dragTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			         }
			      }

			});
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
		public DragTableCellRenderer getDragTableCellRenderer(){
			return dragTableCellRenderer;
		}
		
		class DragTableCellRenderer extends DefaultTableCellRenderer{							
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,  
						                                                       boolean hasFocus, int row, int column){ 
					JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
					//label.setText(null);  
//					Icon icon = new ImageIcon("src/com/geopista/client/alfresco/ui/image/star.png");
//					label.setIcon(icon);					
//					if(value instanceof Color){
//						if(((Color)value).equals(Color.green))
//							label.setToolTipText("Anexado Propio");
//						else if(((Color)value).equals(Color.orange))
//							label.setToolTipText("Anexado Ajeno");
//						else{ //if(((Color)value).equals(Color.red))
//							label.setToolTipText("Sin Anexar");
//							value = Color.red;
//						}
//						//else
//						//	value = Color.white;
//						label.setBackground((Color)value);
//					}
			    	if(value instanceof Color){
			    		Icon icon = null;
						if(((Color)value).equals(Color.green)){
							icon = new ImageIcon(DynamicTreePanel.class.getClassLoader().getResource("com/geopista/client/alfresco/ui/image/green_doc.png"));
							label.setToolTipText("Anexado Propio");
						}
						else if(((Color)value).equals(Color.orange)){
							icon = new ImageIcon(DynamicTreePanel.class.getClassLoader().getResource("com/geopista/client/alfresco/ui/image/orange_doc.png"));
							label.setToolTipText("Anexado Ajeno");
						}
						else{ //if(((Color)value).equals(Color.red))
							icon = new ImageIcon(DynamicTreePanel.class.getClassLoader().getResource("com/geopista/client/alfresco/ui/image/red_doc.png"));
							label.setToolTipText("Sin Anexar");
							value = Color.red;
						}
						//else
						//	value = Color.white;
						label.setIcon(icon);
					}
					return label;  
				} 					
		}
		
	}
	
	/**
	 * Mueve un directorio a otro directorio padre (El método debe sobreescribirse en su clase invocadora)
	 * @param dropDirectory: Directorio a mover
	 * @param destinyDirectory: Directorio de destino
	 * @return boolean: Resultado del movimiento del directorio
	 */
	public boolean moveDirectory(DefaultMutableTreeNode dropDirectory, DefaultMutableTreeNode destinyDirectory){		
		return true;
	};
	
	/**
	 * Mueve un documento a un nuevo directorio (El método debe sobreescribirse en su clase invocadora)
	 * @param dropDocument: Documento a mover 
	 * @param destinyDirectory: Directorio de destino 
	 * @return boolean: Resultado del movimiento del documento
	 */
	public boolean moveDocument(DefaultMutableTreeNode dropDocument, DefaultMutableTreeNode destinyDirectory){		
		return true;
	};
	
    public class DragAndDropTree extends JTree  implements DragSourceListener, DropTargetListener, DragGestureListener {
    	
		/**
		 * Variables
		 */
	    protected DataFlavor dataFlavor = new DataFlavor (DefaultMutableTreeNode.class, "DefaultMutableTreeNode");
	    private DataFlavor[] supportedFlavors = { dataFlavor };
	
	    /**
	     * Constructor
	     * @param model: Modelo del árbol de directorios
	     */
		public DragAndDropTree (DefaultTreeModel model) {   
			super();
			setCellRenderer (new DnDTreeCellRenderer()); 
			setModel (model);      
			dragSource = new DragSource(); 
			dragSource.createDefaultDragGestureRecognizer (this, DnDConstants.ACTION_MOVE, this);
			dropTarget = new DropTarget (this, this);
		}

		@Override
		public String convertValueToText(Object value, boolean selected,
                 boolean expanded, boolean leaf, int row,
                 boolean hasFocus) {			
			 return ((TreeNode)value).toString();
		}                           
  
		@Override
		public void dragGestureRecognized (DragGestureEvent dge) {
			System.out.println ("dragGestureRecognized");
			Point clickPoint = dge.getDragOrigin();
			TreePath path = getPathForLocation (clickPoint.x, clickPoint.y);
			if (path == null)
				return;
			draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
			Transferable trans = new RJLTransferable (draggedNode);
			dragSource.startDrag (dge, Cursor.getDefaultCursor(), trans, this);
		}
		
		@Override
		public void dragDropEnd (DragSourceDropEvent dsde) {	
			System.out.println ("dragDropEnd()");
			dragDropEnd();
		}
		
		private void dragDropEnd(){
			dropTargetNode = null;
			draggedNode = null;
			repaint();
		}
		
		@Override
		public void dragEnter (DragSourceDragEvent dsde) {
			int action = dsde.getDropAction();
			if(action == DnDConstants.ACTION_MOVE) 
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            else 
            	dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
		}
		
		@Override
		public void dragExit (DragSourceEvent dse) {}
		
		@Override
		public void dragOver (DragSourceDragEvent dsde) {
			int action = dsde.getDropAction();
			if(action == DnDConstants.ACTION_MOVE) 
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            else 
            	dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
		}
		
		@Override
		public void dropActionChanged (DragSourceDragEvent dsde) {
			int action = dsde.getDropAction();
			if(action == DnDConstants.ACTION_MOVE) 
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            else 
            	dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
		}
		
		@Override
		public void dragEnter (DropTargetDragEvent dtde) {	
			System.out.println ("dragEnter"); 			
			dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);	
			System.out.println ("accepted dragEnter");
		}
		
		@Override
		public void dragExit (DropTargetEvent dte) {}
		
		@Override
		public void dragOver (DropTargetDragEvent dtde) {
			System.out.println ("dragOver"); 
			Point dragPoint = dtde.getLocation();
			TreePath path = getPathForLocation (dragPoint.x, dragPoint.y);
			if (path == null)
				dropTargetNode = null; 
			else        
				dropTargetNode = (DefaultMutableTreeNode) path.getLastPathComponent();	
			repaint();
		}
		
		@Override
		public void drop (DropTargetDropEvent dtde) {
			Point dropPoint = dtde.getLocation();
			TreePath path = getPathForLocation (dropPoint.x, dropPoint.y);
			System.out.println ("drop path is " + path);
			boolean dropped = false;
			try {
				dtde.acceptDrop (DnDConstants.ACTION_MOVE);
				//DESCOMENTAR SI NO FUNCIONA
				//Object dropObject = dtde.getTransferable().getTransferData(dataFlavor); 	
	            //if (dropObject instanceof DefaultMutableTreeNode) {
	            if (draggedNode instanceof DefaultMutableTreeNode) {
					//PONER FILTRO DIRECTORIO O FICHERO
	            	DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) draggedNode;
	            	if(dropNode.getAllowsChildren()){
						DefaultMutableTreeNode destinyNode = (DefaultMutableTreeNode) path.getLastPathComponent();
						if(!dropNode.isNodeDescendant(destinyNode) && !destinyNode.isNodeChild(dropNode)){	
							if(moveDirectory(dropNode, destinyNode)){
								((DefaultTreeModel)getModel()).removeNodeFromParent(dropNode); 				
								((DefaultTreeModel)getModel()).insertNodeInto(dropNode, destinyNode, destinyNode.getChildCount());
								dropped = true;
							}
						}
					}	            
	            	else{
	            		//SOLICITUD DE EXISTENCIA DEL NODO DENTRO DE UN PADRE (POR DEFECTO "SI") PARA QUE NO PUEDA MOVERSE A UN DIRECTORIO CON UN DOCUMENTO DE MISMO NOMBRE
	            		DefaultMutableTreeNode destinyNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	            		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
	            		if(!selectedNode.getUserObject().equals(destinyNode.getUserObject())){
	            			if(moveDocument(dropNode, destinyNode)){
		            			setSelectionPath(path);						
		            			dropped = true;
		            			dragDropEnd();
		            		}
	            		}
	            	}
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
			dtde.dropComplete (dropped);
			
		}
		
		@Override
		public void dropActionChanged (DropTargetDragEvent dtde) {}
		
		/**
		 * Clase paara la transferencia en DragAndDrop (Impementación de la interfaz Transferable)
		 */
		class RJLTransferable implements Transferable { 
	    	
			/**
			 * Variables
			 */
	    	Object object; 
	    	
	    	/**
	    	 * Constructor
	    	 * @param o: Objeto a desplazar
	    	 */	    	
	    	public RJLTransferable (Object o) { 
	    		object = o; 
	    	} 
	    	
	    	/**
	    	 * Devuelve el objeto desplazado
	    	 * @param df: Tipo de información desplazada
	    	 * return Object: Objeto desplazado
	    	 */	    	
	    	public Object getTransferData(DataFlavor df) 
	    		throws UnsupportedFlavorException, IOException { 
	    		if (isDataFlavorSupported (df)) 
	    			return object; 
	    		else 
	    			throw new UnsupportedFlavorException(df); 
	    	} 
	    	
	    	/**
	    	 * Devuelve el resultado del soporte del tipo de información desplazada
	    	 * @param df: Tipo de información desplazada
	    	 * return Object: Resultado del soporte del tipo de información desplazada
	    	 */	   
	    	public boolean isDataFlavorSupported (DataFlavor df) { 	    	
	    			return df.equals(dataFlavor); 
	    	}
	    		
	    	/**
	    	 * Devuelve el conjunto de tipo de información desplazada soportada
	    	 * return DataFlavor []: Conjunto de tipo de información desplazada soportada
	    	 */	   
	    	public DataFlavor[] getTransferDataFlavors () {
	    			return supportedFlavors;
	    	}	
	    }
	      
	    /**
	     * Clase para el renderizado del árbol de directorios
	     */
	    class DnDTreeCellRenderer extends DefaultTreeCellRenderer {
	    	
			private boolean isTargetNode;
			
			public DnDTreeCellRenderer() {
				super(); 
				init();
			}
		
			private void init(){
				//CAMBIAR RUTAS ABSOLUTAS POR RELATIVAS
				Icon openIcon = new ImageIcon(DynamicTreePanel.class.getClassLoader().getResource("com/geopista/client/alfresco/ui/image/folder_open.png")); 
		    	//Icon leafIcon = new ImageIcon("src/main/java/com/geopista/app/alfresco/ui/image/folder_close.png");
		    	Icon closedIcon = new ImageIcon(DynamicTreePanel.class.getClassLoader().getResource("com/geopista/client/alfresco/ui/image/folder_close.png")); 
		    	setOpenIcon(openIcon);
		    	//setLeafIcon(leafIcon);
		    	setLeafIcon(openIcon);
		    	setClosedIcon(closedIcon);
			}
			
			public Component getTreeCellRendererComponent (JTree tree,
										Object value, 
										boolean isSelected, 
										boolean isExpanded, 
										boolean isLeaf, 
										int row, 
										boolean hasFocus) {
				isTargetNode = (value == dropTargetNode);
				return super.getTreeCellRendererComponent (tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
			}
			
			public void paintComponent (Graphics g) {
				super.paintComponent(g);

				if (isTargetNode){
					DefaultMutableTreeNode destinyNode = (DefaultMutableTreeNode) dropTargetNode;
					DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) draggedNode;
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) dragAndDropTree.getLastSelectedPathComponent();
					if((dropNode.getAllowsChildren() && !dropNode.isNodeDescendant(destinyNode) && !destinyNode.isNodeChild(dropNode)) || (!dropNode.getAllowsChildren() && !destinyNode.getUserObject().equals(selectedNode.getUserObject()))) {					
						g.setColor(new Color(0, 29, 255, 100));
						g.fillRect(17, 0, getSize().width-17, getSize().height-1); 		
					} 
				}
				else
					g.setColor(new Color(0, 29, 255, 0));		
			} 
	    }	
		
	}
    
}




