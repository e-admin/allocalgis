/**
 * EntidadesPanelTree.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.dialogs.gestorplaneamiento.panels;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableTreeModel;
import com.geopista.util.ApplicationContext;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.dialogs.TreeRenderer;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class EntidadesPanelTree extends JPanel{
	
	private ApplicationContext aplicacion;
	
	private JTree tree;    
	    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";    
    private EntidadPanelBean[] listEntidades = null;
    
    private int valorSecuencia = -1;

	int selectionIndice = 0;
	TreeNode[] pathToSelection;
	private DefaultMutableTreeNode top;

	public EntidadesPanelTree(int modoSeleccion, EntidadPanelBean[] listEntidades){
		 super(new GridLayout(1,0));
		 this.aplicacion= (AppContext)AppContext.getApplicationContext();
		 
		 // Ordenando por clave
		 if(listEntidades!=null) Arrays.sort(listEntidades);
		 
		 this.listEntidades = listEntidades;
		 
		 TramitePanelBean tramite = new TramitePanelBean();

		 top = new DefaultMutableTreeNode(tramite);
	        
		 
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

	public void UpdateEntidadesPanelTree(int modoSeleccion, EntidadPanelBean[] listEntidades, int idSelection){

		selectionIndice = 0;
		
		 // Ordenando por clave
		if(listEntidades!=null) Arrays.sort(listEntidades);
		 
		 this.listEntidades = listEntidades;
		 
		 TramitePanelBean tramite = new TramitePanelBean();

		 DefaultMutableTreeNode top = new DefaultMutableTreeNode(tramite);
	        
		//Crea los nodos
		 createNodes(top, idSelection);
		
		 //Crea un arbol que permite la seleccion indicada en modoSeleccion
		 HideableTreeModel ml = new HideableTreeModel( top );
		 ml.activateFilter(true);
		 remove(tree);
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
		 if(pathToSelection!=null && pathToSelection.length!=0)
			 tree.expandPath(new TreePath(pathToSelection));
		 //tree.setSelectionRow(0);
	}
	
	private void createNodes(DefaultMutableTreeNode top) 
    {   
		if(listEntidades != null){
	        for(int i=0; i< listEntidades.length; i++)
	        {
	        	if(listEntidades[i].getId() != -1){
		        	HideableNode hideableNode = new HideableNode(listEntidades[i]);
		        	top.add(hideableNode);
		        	createNodesHija(hideableNode, listEntidades[i]);
	        	}
	        }        
		}   
    }
	
	private void createNodesHija(HideableNode node, EntidadPanelBean entidad){
		
		if(entidad != null && entidad.getLstEntidadesHijas() != null &&
				entidad.getLstEntidadesHijas().length != 0){
     		// tiene hijas
			// Ordenando por apartados
			if(entidad.getLstEntidadesHijas()!=null) Arrays.sort(entidad.getLstEntidadesHijas());
			
			for(int j=0; j<entidad.getLstEntidadesHijas().length; j++){
				HideableNode hideableNode = new HideableNode(entidad.getLstEntidadesHijas()[j]);
				node.add(hideableNode);
				createNodesHija(hideableNode,entidad.getLstEntidadesHijas()[j]);
				
			}
     	}
		
	}
	
	private void createNodes(DefaultMutableTreeNode top, int idSelection) 
    {   
		if(listEntidades != null){
	        for(int i=0; i< listEntidades.length; i++)
	        {
	        	if(listEntidades[i].getId() != -1){
		        	HideableNode hideableNode = new HideableNode(listEntidades[i]);
	        		if(listEntidades[i].getId() == idSelection) pathToSelection = (TreeNode[]) hideableNode.getPath();
		        	top.add(hideableNode);
		        	createNodesHija(hideableNode, listEntidades[i], idSelection);
	        	}
	        }        
		}   
    }
	
	private void createNodesHija(HideableNode node, EntidadPanelBean entidad, int idSelection){
		
		if(entidad != null && entidad.getLstEntidadesHijas() != null &&
				entidad.getLstEntidadesHijas().length != 0){
     		// tiene hijas
			// Ordenando por apartados
			if(entidad.getLstEntidadesHijas()!=null) Arrays.sort(entidad.getLstEntidadesHijas());
			
			for(int j=0; j<entidad.getLstEntidadesHijas().length; j++){
				HideableNode hideableNode = new HideableNode(entidad.getLstEntidadesHijas()[j]);
				if(entidad.getLstEntidadesHijas()[j].getId() == idSelection) pathToSelection = (TreeNode[]) hideableNode.getPath();
				node.add(hideableNode);
				createNodesHija(hideableNode,entidad.getLstEntidadesHijas()[j], idSelection);
				
			}
     	}
		
	}
	
	private void buscarEntidadYAnadirEntidadNueva(EntidadPanelBean[] lstEntid,
	                                    Object userObject, Object child){

		EntidadPanelBean userObjectDeterminacion = (EntidadPanelBean)userObject;
		
		for(int i=0; i<lstEntid.length; i++){
			if(lstEntid[i] != null){
				if(lstEntid[i].getId() == userObjectDeterminacion.getId()){
					//se ha encontrado la determinacion
					((EntidadPanelBean)child).setMadre(lstEntid[i].getId());
					lstEntid[i].addEntidadHija((EntidadPanelBean)child);
				}
				else if(lstEntid[i].getLstEntidadesHijas() != null &&
						lstEntid[i].getLstEntidadesHijas().length != 0){
					buscarEntidadYAnadirEntidadNueva(lstEntid[i].getLstEntidadesHijas(), userObject, child);
					
				}
			}
		}

	}
	
    public HideableNode addObject(DefaultMutableTreeNode parent,
            Object child,
            boolean shouldBeVisible) {
        HideableNode childNode = new HideableNode(child);

        ((HideableTreeModel)tree.getModel()).insertNodeInto(childNode, parent,
                parent.getChildCount());
        
     
		((EntidadPanelBean)child).setId(valorSecuencia);
		((EntidadPanelBean)child).setCodigo(GestorFipUtils.completarCodigoConCeros(String.valueOf(valorSecuencia)));
		((EntidadPanelBean)child).setNueva(true);

        
        if(parent.getUserObject() instanceof EntidadPanelBean){
        	
        	buscarEntidadYAnadirEntidadNueva(listEntidades,parent.getUserObject(), child);
        }
        else{
        	if(listEntidades == null){
    			
        		listEntidades = new EntidadPanelBean[1];
        		listEntidades[0] = (EntidadPanelBean)child;
    		
    		}
    		else{
    			listEntidades = (EntidadPanelBean[]) Arrays.copyOf(listEntidades, 
    					listEntidades.length+1);
    		
    			listEntidades[listEntidades.length-1] = (EntidadPanelBean)child;
    		}

        }
      
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public void removeObject(Object child) {
        
    	boolean eliminarDeterminacion = buscarEntidadYElimEntidad(listEntidades,
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject());
        
    	
    	EntidadPanelBean[] lstDeter = listEntidades;
    	if(eliminarDeterminacion){
    		
    	
    		((HideableTreeModel)tree.getModel()).removeNodeFromParent((DefaultMutableTreeNode)child);
    	}
        
        EntidadPanelBean[] lst = listEntidades;

    }
    
    private boolean buscarEntidadYElimEntidad(EntidadPanelBean[] lstEnti,
            Object userObject){

    	boolean eliminarEntidad = true;
    	
		EntidadPanelBean userObjectEntidad = (EntidadPanelBean)userObject;
		
		for(int i=0; i<lstEnti.length && eliminarEntidad; i++){
		
			if(lstEnti[i] != null){
				
				if(lstEnti[i].getId() == userObjectEntidad.getId()){
				
					//se ha encontrado la determinacion
					if(! buscarSiEntidadEstaAsociada(listEntidades, lstEnti[i])){
					
						
						elimEntid(lstEnti[i]);
						
						eliminarEntidad = true;
					}
					else{
						eliminarEntidad = false;
					}
				}
				else if(lstEnti[i].getLstEntidadesHijas() != null &&
					lstEnti[i].getLstEntidadesHijas().length != 0){
					eliminarEntidad = buscarEntidadYElimEntidad(lstEnti[i].getLstEntidadesHijas(), userObject);
				
				}
			}
		}
		return eliminarEntidad;
	}
    
    private void elimEntid(EntidadPanelBean entid){
    	
    	entid.setEliminada(true);
    	
    	//if(!deter.isDeterminacionNueva()){
			
			if(entid.getLstEntidadesHijas() != null &&
					entid.getLstEntidadesHijas().length != 0){
				for(int j=0; j<entid.getLstEntidadesHijas().length; j++){
					
					elimEntid(entid.getLstEntidadesHijas()[j]);
					
				}
			}
	
    }

    
    private boolean buscarSiEntidadEstaAsociada(
    								EntidadPanelBean[] lstEntidades,
    								EntidadPanelBean lstEntid){
    	
    	boolean entidadAsociada = false;
    	
    	entidadAsociada = buscarSiEntidadEstaAsociadaBase(lstEntidades, lstEntid.getId());

    	
    	if(!entidadAsociada && lstEntid.getLstEntidadesHijas()!= null &&
    			lstEntid.getLstEntidadesHijas().length != 0){
    		for(int i=0;i<lstEntid.getLstEntidadesHijas().length && !entidadAsociada; i++){
    			entidadAsociada = buscarSiEntidadEstaAsociada(lstEntidades,lstEntid.getLstEntidadesHijas()[i]);
    		}
    	}
    	
    	return entidadAsociada;
    }
    

    
    private boolean buscarSiEntidadEstaAsociadaBase(EntidadPanelBean[] lstEntid, int idEnti){
    	
    	boolean entidadAsociada = false;
    	for(int i=0; i<lstEntid.length; i++){
    		
    		
    		if(!entidadAsociada){
				if(lstEntid[i] != null && lstEntid[i].getBaseBean() != null && 
						lstEntid[i].getBaseBean().getEntidad() != null && 
						lstEntid[i].getId() != idEnti){
					
					if(lstEntid[i].getBaseBean().getEntidad().getId() == idEnti){
						
						JOptionPane.showMessageDialog(this, 
								I18N.get("LocalGISGestorFipEntidades","gestorFip.entidades.validacion.noeliminar.asociada.base"));
						return  true;
					}
					
					
				}
				if(lstEntid[i].getLstEntidadesHijas() != null && 
						lstEntid[i].getLstEntidadesHijas().length != 0){
					entidadAsociada = buscarSiEntidadEstaAsociadaBase( lstEntid[i].getLstEntidadesHijas(), idEnti);
					
				}
    		}
    		
    		
    	}
    	return entidadAsociada;
    }
    
    
    public void modifObject(Object node, EntidadPanelBean det) {
    	
    	if(node instanceof EntidadPanelBean){
    		    		
    		((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(det);	  
    		tree.expandPath(tree.getSelectionPath());
    		tree.repaint();
    		
    		buscarEntidadYModifEntidad(listEntidades,
    				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject(), det);
    		
    	}
    	
    }
	
    private void buscarEntidadYModifEntidad(EntidadPanelBean[] lstEntid,
            Object userObject, Object child){

		EntidadPanelBean userObjectEntidad = (EntidadPanelBean)userObject;
		
		for(int i=0; i<lstEntid.length; i++){
		
			if(lstEntid[i] != null){
				if(lstEntid[i].getId() == userObjectEntidad.getId()){
					//se ha encontrado la determinacion
					if(!lstEntid[i].isNueva()){
						((EntidadPanelBean)child).setModificada(true);
					}
					else{
						((EntidadPanelBean)child).setNueva(true);
					}
					lstEntid[i] =(EntidadPanelBean)child;
					
				}
				else if(lstEntid[i].getLstEntidadesHijas() != null &&
					lstEntid[i].getLstEntidadesHijas().length != 0){
					buscarEntidadYModifEntidad(lstEntid[i].getLstEntidadesHijas(), userObject, child);
				
				}
			}
		}
	
	}
    
    public void selectPaths(TreePath[] paths){
    	tree.setSelectionPaths(paths);
    }
    
    public ArrayList searchTree(JTree tree, TreePath path, String strFind, ArrayList lstPath) 
    {
    	ArrayList lstPaths = lstPath;
    	TreeNode node = (TreeNode)path.getLastPathComponent();
    	if(((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof EntidadPanelBean){
    		EntidadPanelBean epb = (EntidadPanelBean)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
    		if(epb.getNombre().toLowerCase().contains(strFind.toLowerCase()))
		    {
		    	tree.setScrollsOnExpand(true);
		    	lstPaths.add(path);
		    }
    	}
	    if(!node.isLeaf() && node.getChildCount()>=0)
	    {
	    	Enumeration e = node.children();
       		while(e.hasMoreElements())
       			lstPaths = searchTree(tree, path.pathByAddingChild(e.nextElement()), strFind, lstPaths);
	    }
	    return lstPaths;
    }
    
    public ArrayList searchTreeLayer(JTree tree, TreePath path, int idLayer, int idFeature, ArrayList lstPath) 
    {
    	ArrayList lstPaths = lstPath;
    	TreeNode node = (TreeNode)path.getLastPathComponent();
    	if(((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof EntidadPanelBean){
    		EntidadPanelBean epb = (EntidadPanelBean)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
    		if(epb.getIdLayer() == idLayer && epb.getIdFeature() == idFeature)
		    {
		    	tree.setScrollsOnExpand(true);
		    	lstPaths.add(path);
		    }
    	}
	    if(!node.isLeaf() && node.getChildCount()>=0)
	    {
	    	Enumeration e = node.children();
       		while(e.hasMoreElements())
       			lstPaths = searchTreeLayer(tree, path.pathByAddingChild(e.nextElement()), idLayer, idFeature, lstPaths);
	    }
	    return lstPaths;
    }
    

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
    
	public  EntidadPanelBean[] getListEntidades() {
		return listEntidades;
	}

	public void setListEntidades(
			EntidadPanelBean[] listDeterminaciones) {
		this.listEntidades = listDeterminaciones;
	}
	
	public int getValorSecuencia() {
		return valorSecuencia;
	}

	public void setValorSecuencia(int valorSecuencia) {
		this.valorSecuencia = valorSecuencia;
	}
}
