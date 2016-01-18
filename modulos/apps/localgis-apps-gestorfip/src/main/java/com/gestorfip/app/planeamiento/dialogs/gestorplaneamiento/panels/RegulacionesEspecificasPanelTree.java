/**
 * RegulacionesEspecificasPanelTree.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.RegulacionesEspecificasPanelBean;
import com.gestorfip.app.planeamiento.dialogs.TreeRenderer;

public class RegulacionesEspecificasPanelTree extends JPanel{
	
	
	private JTree tree;    
	    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";    
    private  RegulacionesEspecificasPanelBean[] lstregulacionEspecifica = null;
    private int modoSeleccion;
    private DefaultMutableTreeNode top;
	
	public RegulacionesEspecificasPanelTree(int modoSeleccion, 
							RegulacionesEspecificasPanelBean[] lstregulacionEspecifica ){
		 super(new GridLayout(1,0));
		 this.lstregulacionEspecifica = lstregulacionEspecifica;
		 
		 DeterminacionPanelBean determinacion = new DeterminacionPanelBean();
		 top = new DefaultMutableTreeNode(determinacion);
		 this.modoSeleccion = modoSeleccion;
	        
		 
		//Crea los nodos
		 createNodes(top);
	        
		 //Crea un arbol que permite la seleccion indicada en modoSeleccion
		 HideableTreeModel ml = new HideableTreeModel( top );
		 ml.activateFilter(true);
		 tree = new JTree( ml );
	     
		 tree.getSelectionModel().setSelectionMode(modoSeleccion);
	        
		 //Aspecto del arbol (renderer)
		 tree.setCellRenderer(new TreeRenderer());  
	        
		 tree.setEditable(false);
	        
		 if (playWithLineStyle) {
			 System.out.println("line style = " + lineStyle);
			 tree.putClientProperty("JTree.lineStyle", lineStyle);
		 }
		 
		 add(tree, null);
		 tree.setSelectionRow(0);	
		


	}

	private void createNodes(DefaultMutableTreeNode top) 
    {   
		if(lstregulacionEspecifica != null && lstregulacionEspecifica.length != 0 ){
	        for(int h=0; h< lstregulacionEspecifica.length; h++){
	      //  {
	        	RegulacionesEspecificasPanelBean regulacionEspecifica = lstregulacionEspecifica[h];
	        	if(lstregulacionEspecifica[h] != null){
		        	HideableNode hideableNode = new HideableNode(regulacionEspecifica);
		        	top.add(hideableNode);
		        	createNodesHija(hideableNode, regulacionEspecifica);
	        	}
	        }        
		}   
    }
	
	private void createNodesHija(HideableNode node, RegulacionesEspecificasPanelBean regulacionEspecifica){
		
		if(regulacionEspecifica != null && regulacionEspecifica.getLstRegulacionEspecifica() != null && 
				regulacionEspecifica.getLstRegulacionEspecifica().length != 0){
     		// tiene hijas
			for(int j=0; j<regulacionEspecifica.getLstRegulacionEspecifica().length; j++){

				
				HideableNode hideableNode = new HideableNode(regulacionEspecifica.getLstRegulacionEspecifica()[j]);
				node.add(hideableNode);
				createNodesHija(hideableNode,regulacionEspecifica.getLstRegulacionEspecifica()[j]);
				
			}
     	}
		
	}

	
    public HideableNode addObject(DefaultMutableTreeNode parent,
            Object child,
            boolean shouldBeVisible) {
        HideableNode childNode = new HideableNode(child);

        ((HideableTreeModel)tree.getModel()).insertNodeInto(childNode, parent,
                parent.getChildCount());

        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public void removeObject(Object child) {
        
        ((HideableTreeModel)tree.getModel()).removeNodeFromParent((DefaultMutableTreeNode)child);
                          
        return;
    }
    
    /*public void modifObject(Object node, DeterminacionPanelBean det) {
    	
    	if(node instanceof DeterminacionPanelBean){
    		    		
    		((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(det);	  
    		tree.expandPath(tree.getSelectionPath());
    		tree.repaint();

    	}
    	
    }*/
/*	
    public RegulacionesEspecificasPanelTree rellenarArbol(RegulacionesEspecificasPanelBean regulacionEspecifica){
    	this.regulacionEspecifica = regulacionEspecifica;
    	
   
    	 createNodes(top);
    	 
    	//Crea un arbol que permite la seleccion indicada en modoSeleccion
		 HideableTreeModel ml = new HideableTreeModel( top );
		 ml.activateFilter(true);
		 tree = new JTree( ml );
	     
		 tree.getSelectionModel().setSelectionMode(modoSeleccion);
	        
		 //Aspecto del arbol (renderer)
		 tree.setCellRenderer(new TreeRenderer());  
	        
		 tree.setEditable(false);
	        
		 if (playWithLineStyle) {
			 System.out.println("line style = " + lineStyle);
			 tree.putClientProperty("JTree.lineStyle", lineStyle);
		 }
		 
		 add(tree, null);
		 tree.setSelectionRow(0);	
    	 //tree.repaint();
    	
    }*/

    /**
     * @return Returns the tree.
     */
    public JTree getTree()
    {
        return tree;
    }
    /**
     * @param tree The tree to set.
     */
    public void setTree(JTree tree)
    {
        this.tree = tree;
    }
   
    /**
     * @param enabled True si estï¿½ habilitado
     */
    public void setEnabled (boolean enabled)
    {
        tree.setEnabled(enabled);        
    }
    
    public Object getNodoSeleccionado(){
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    	Object nodeInfo = node.getUserObject();	
    	return nodeInfo;
    }
}
