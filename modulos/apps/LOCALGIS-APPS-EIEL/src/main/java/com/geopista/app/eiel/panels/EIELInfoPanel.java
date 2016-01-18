/**
 * EIELInfoPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.app.eiel.utils.HideableTreeModel;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.vividsolutions.jump.I18N;

public class EIELInfoPanel extends JPanel implements TreeSelectionListener {

	private JScrollPane jScrollPane = null;
	private EIELDominioPanelTree jEIELPanelTree = null;
	private JTree jTree = null;
	private HideableTreeModel model = null;
	private DefaultMutableTreeNode preNodeInfo;
	private DomainNode resource = null;
	private ArrayList actionListeners= new ArrayList();
	private DomainNode nodoSelected = null;
	private DefaultMutableTreeNode nodeArbol = null;
	private String patronSelected = null;
	private String traduccionSelected = null;
	private boolean isEditable;
	private String nodeParent;
	
	
	private static HashMap positionsNodetree=new HashMap();
     

	private EIELDominioPanelTree getJEIELPanelTree()
	{
		if (jEIELPanelTree ==null)
		{
			jEIELPanelTree = new EIELDominioPanelTree(TreeSelectionModel.SINGLE_TREE_SELECTION);
			jTree = jEIELPanelTree.getTree();
			model = (HideableTreeModel)jTree.getModel();			
			jTree.addTreeSelectionListener( new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
					dominioJTreeValueChanged(treeSelectionEvent,null);
				}
			});
			
			//Controlamos el Double Click
			MouseListener ml = new MouseAdapter() {
			     public void mousePressed(MouseEvent e) {
			         int selRow = jTree.getRowForLocation(e.getX(), e.getY());
			         TreePath selPath = jTree.getPathForLocation(e.getX(), e.getY());
			         if(selRow != -1) {
			             /*if(e.getClickCount() == 1) {
			                 mySingleClick(selRow, selPath);
			             }
			             else */if(e.getClickCount() == 2) {
			            	 selectedTree(jTree,ConstantesLocalGISEIEL.EVENT_DOUBLE_CLICK);
			             }
			         }
			     }
			 };
			 jTree.addMouseListener(ml);

		}

		return jEIELPanelTree;
	}
	
	public JTree getTree(){
		return jTree;
	}
	
	public void dominioJTreeValueChanged(TreeSelectionEvent e,String event) {
        if (e==null || !(e.getSource() instanceof JTree)) return;
        JTree arbol= (JTree)e.getSource();
        selectedTree(arbol,null);               
    }
	
	public void selectedTree(JTree arbol,String event ){
		 DefaultMutableTreeNode node= (DefaultMutableTreeNode)arbol.getLastSelectedPathComponent();
	        nodeArbol=node;
	        if (node == null) return;
	        
	        Object nodeInfo= node.getUserObject();
	        nodoSelected= null;
	        if (nodeInfo == null) return;
	        if (nodeInfo instanceof DomainNode) {
	            if (node.isLeaf()){
	                	nodeParent = node.getParent().toString();
	                    patronSelected = ((DomainNode)nodeInfo).getPatron();
	                    traduccionSelected = ((DomainNode)nodeInfo).toString();
	                    nodoSelected= (DomainNode)nodeInfo;
	                    fireActionPerformed(null,event);
	            }else{
	                
//	            	patronSelected = null;
	            	patronSelected = ((DomainNode)nodeInfo).getPatron();
	                traduccionSelected = ((DomainNode)nodeInfo).toString();
	            	//nodoSelected= (DomainNode)nodeInfo;
	                fireActionPerformed(null,event);
	            }
	        }else{
	            /** seleccionada la raiz del arbol */
	            if (arbol.getSelectionPath().getParentPath() == null){
	            	patronSelected = null;
	                fireActionPerformed(null,event);
	            }

	        }
	}
	
	
	public String getNodeParent() {
		return nodeParent;
	}

	public String getPatronSelected(){
		return patronSelected;
	}
	
	public String getTraduccionSelected(){
		return traduccionSelected;
	}
	
	public DomainNode getNodeSelected(){
		return nodoSelected;
	}
	
	public DefaultMutableTreeNode getNodeArbol(){
		return nodeArbol;
	}
	
	public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    public void fireActionPerformed(ActionEvent e,String event) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            if (e==null)
            	l.actionPerformed(new ActionEvent(this, 0, event));
            else
            	l.actionPerformed(e);
        }
    }
	
	private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJEIELPanelTree());

        }
        return jScrollPane;
    }
		
	private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        
        this.setLayout(new GridBagLayout());
                
        this.add(getJScrollPane(), 
                new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));   
        
        getJEIELPanelTree().getTree().addTreeSelectionListener(this);
    
    }
		
	public EIELInfoPanel(boolean isEditable)
    {
        super();
        setName("EIELInfoPanel");
        this.isEditable = isEditable;
        initialize();
        
    }
	
	public void valueChanged(TreeSelectionEvent e) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();

        if (node == null)
        {
            setPreNodeInfo(node);
            return;
        }

        Object nodeInfo = node.getUserObject();
        
        setPreNodeInfo(node);

        if (nodeInfo instanceof DomainNode)
        {            
        	resource = getJEIELPanelTree().fillObject(node);

            jTree.expandPath(jTree.getSelectionPath());

            if (resource!=null)
                ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).setUserObject(resource);
            jTree.repaint();
            
            if (((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject() instanceof DomainNode){
            	DomainNode resourceDescriptor = (DomainNode)((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject();
            	
            }

        }

	}
	
	public void setPreNodeInfo(DefaultMutableTreeNode preNodeInfo) {
		this.preNodeInfo = preNodeInfo;
	}
	
	public DefaultMutableTreeNode getPreNodeInfo() {
		return preNodeInfo;
	}
	
	  /**
     * Buscamos la posicion del elemento seleccionado.
     * @param node
     * @param nodoSeleccionado
     * @return
     */
    public int findPositionNodeTree(String nodoSeleccionado) {
    	int position=-1;
    	
    	TreeModel model=jTree.getModel();
    	
    	Object node = model.getRoot();
    	
    	contadorPosiciones=0;
    	if (positionsNodetree.size()==0)    	
    		createPositionNodeTree(node);
    	
    	expandAll(jTree);
    	if (positionsNodetree.get(nodoSeleccionado)!=null){
    		position=(Integer)positionsNodetree.get(nodoSeleccionado);
    	}

    	return position;
    }
    
    public void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
          tree.expandRow(row);
          row++;
          }
        }
    
    private int contadorPosiciones;
    
    /**
     * Creamos las posiciones de los elementos
     * @param node
     * @param nodoSeleccionado
     */
    private void createPositionNodeTree(Object node) {

		TreeModel model =jTree.getModel();
		if (node != null) {
			TreePath p = new TreePath(node);

			if (node instanceof DefaultMutableTreeNode)
				jEIELPanelTree.fillObject((DefaultMutableTreeNode)node);

			int count = model.getChildCount(node);
			
			//((HideableNode) node).getUserObject().getlHijos()
			
			for (int i = 0; i < count; i++) {
				contadorPosiciones++;
				Object newNode = model.getChild(node, i);

				Object nodeInfo = ((HideableNode) newNode).getUserObject();
				if (nodeInfo != null) {
					if (nodeInfo instanceof DomainNode) {
						if (((HideableNode) newNode).isLeaf()) {
							String patronSelected = ((DomainNode) nodeInfo).getPatron();
							//System.out.println("Patron1:" + patronSelected);
							positionsNodetree.put(patronSelected,contadorPosiciones);
						} else {
							String patronSelected = ((DomainNode) nodeInfo).getPatron();
							//System.out.println("Patron2:" + patronSelected);
						}
					}
					createPositionNodeTree(newNode);
				}
			}
		}
	}
    

}
