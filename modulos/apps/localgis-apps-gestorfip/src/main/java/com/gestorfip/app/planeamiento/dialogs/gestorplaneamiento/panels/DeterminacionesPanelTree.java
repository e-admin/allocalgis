/**
 * DeterminacionesPanelTree.java
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
import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;
import com.gestorfip.app.planeamiento.beans.panels.tramite.TramitePanelBean;
import com.gestorfip.app.planeamiento.dialogs.TreeRenderer;
import com.gestorfip.app.planeamiento.utils.GestorFipUtils;
import com.vividsolutions.jump.I18N;

public class DeterminacionesPanelTree extends JPanel{
	
	private ApplicationContext aplicacion;
	
	private JTree tree;    
	    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";    
    private DeterminacionPanelBean[] listDeterminaciones = null;
    
    private int valorSecuencia = -1;

	int selectionIndice = 0;
	TreeNode[] pathToSelection;

	public DeterminacionesPanelTree(int modoSeleccion, DeterminacionPanelBean[] listDeterminaciones){
		 super(new GridLayout(1,0));
		 this.aplicacion= (AppContext)AppContext.getApplicationContext();
		 
		 // Ordenando por apartados
		// if(listDeterminaciones!=null) Arrays.sort(listDeterminaciones);
		 
		 this.listDeterminaciones = listDeterminaciones;
		 
		 TramitePanelBean tramite = new TramitePanelBean();

		 DefaultMutableTreeNode top = new DefaultMutableTreeNode(tramite);
	        
		 
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

	public void UpdateDeterminacionesPanelTree(int modoSeleccion, DeterminacionPanelBean[] listDeterminaciones, int idSelection){

		 selectionIndice = 0;
		 
		 // Ordenando por clave
		 //if(listDeterminaciones!=null) Arrays.sort(listDeterminaciones);
		 
		 this.listDeterminaciones = listDeterminaciones;
		
		 TramitePanelBean tramite = new TramitePanelBean();
		 tramite.setLstDeterminacionesPanelBean(listDeterminaciones);
		 DefaultMutableTreeNode top = new DefaultMutableTreeNode(tramite);
	        
		//Crea los nodos
		 createNodes(top, idSelection);
		
		 //Crea un arbol que permite la seleccion indicada en modoSeleccion
		 HideableTreeModel ml = new HideableTreeModel( top );
		 ml.activateFilter(true);
		 remove(tree);
		 tree = new JTree( ml );
		 //tree.setModel(ml);
		 tree.getSelectionModel().setSelectionMode(modoSeleccion);

		 
		 //Aspecto del arbol (renderer)
		 tree.setCellRenderer(new TreeRenderer());  
	        
		 tree.setEditable(false);
	        
		 if (playWithLineStyle) {
			 System.out.println("line style = " + lineStyle);
			 tree.putClientProperty("JTree.lineStyle", lineStyle);
		 }

		 add(tree, null);

		 if(pathToSelection!=null && pathToSelection.length!=0){
			 tree.expandPath(new TreePath(pathToSelection));
		 	 tree.setSelectionPath(new TreePath(pathToSelection));
		 }
		 //tree.setSelectionRow(0);
	}

	
	private void createNodes(DefaultMutableTreeNode top) 
    {   
		if(listDeterminaciones != null){
	        for(int i=0; i< listDeterminaciones.length; i++)
	        {
	        	if(listDeterminaciones[i].getId() != -1){
					// FIXME 
					// No incluyemos las determinaciones eliminadas
					// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
	        	if(!listDeterminaciones[i].isEliminada()) {
		        	HideableNode hideableNode = new HideableNode(listDeterminaciones[i]);
		        	top.add(hideableNode);
		        	createNodesHija(hideableNode, listDeterminaciones[i]);
	        	}
	        	}
	        }        
		}   
    }
	
	private void createNodesHija(HideableNode node, DeterminacionPanelBean determinacion){
		
		if(determinacion != null && determinacion.getLstDeterminacionesHijas() != null &&
				determinacion.getLstDeterminacionesHijas().length != 0){
			// FIXME 
			// No incluyemos las determinaciones eliminadas
			// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
			if(!determinacion.isEliminada()) {

				
     		// tiene hijas
			// Ordenando por apartados
			//	if(determinacion.getLstDeterminacionesHijas()!=null) Arrays.sort(determinacion.getLstDeterminacionesHijas());
			
			for(int j=0; j<determinacion.getLstDeterminacionesHijas().length; j++){
				// FIXME 
				// No incluyemos las determinaciones eliminadas
				// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
				if(!determinacion.getLstDeterminacionesHijas()[j].isEliminada()) {
				HideableNode hideableNode = new HideableNode(determinacion.getLstDeterminacionesHijas()[j]);
				node.add(hideableNode);
				createNodesHija(hideableNode,determinacion.getLstDeterminacionesHijas()[j]);
				}
				
			}
			}
     	}
		
	}

	
	private void createNodes(DefaultMutableTreeNode top, int idSelection) 
    {   
		if(listDeterminaciones != null){
	        for(int i=0; i< listDeterminaciones.length; i++)
	        {
	        	if(listDeterminaciones[i].getId() != -1){
					// FIXME 
					// No incluyemos las determinaciones eliminadas
					// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
	        	if(!listDeterminaciones[i].isEliminada()) {
		        	HideableNode hideableNode = new HideableNode(listDeterminaciones[i]);
	        		if(listDeterminaciones[i].getId() == idSelection) 
	        			pathToSelection = (TreeNode[]) hideableNode.getPath();
		        	top.add(hideableNode);
		        	createNodesHija(hideableNode, listDeterminaciones[i], idSelection);
	        	}
	        	}
	        }        
		}   
    }
	
	private void createNodesHija(HideableNode node, DeterminacionPanelBean determinacion, int idSelection){
		
		if(determinacion != null && determinacion.getLstDeterminacionesHijas() != null &&
				determinacion.getLstDeterminacionesHijas().length != 0){
			// FIXME 
			// No incluyemos las determinaciones eliminadas
			// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
			if(!determinacion.isEliminada()) {

				
     		// tiene hijas
			// Ordenando por apartados
				//if(determinacion.getLstDeterminacionesHijas()!=null) Arrays.sort(determinacion.getLstDeterminacionesHijas());
			
				for(int j=0; j<determinacion.getLstDeterminacionesHijas().length; j++){
					// FIXME 
					// No incluyemos las determinaciones eliminadas
					// Es normal que todavia a este nivel tenemos determinaciones a eliminar?
					if(!determinacion.getLstDeterminacionesHijas()[j].isEliminada()) {
					HideableNode hideableNode = new HideableNode(determinacion.getLstDeterminacionesHijas()[j]);
					if(determinacion.getLstDeterminacionesHijas()[j].getId() == idSelection) 
						pathToSelection = (TreeNode[]) hideableNode.getPath();
					node.add(hideableNode);
					createNodesHija(hideableNode,determinacion.getLstDeterminacionesHijas()[j], idSelection);
					}
					
				}
			}
     	}
		
	}

	
	private void buscarDeterminacionYAnadirDeterminacionNueva(DeterminacionPanelBean[] lstDeter,
	                                    Object userObject, Object child){

		DeterminacionPanelBean userObjectDeterminacion = (DeterminacionPanelBean)userObject;
		
		for(int i=0; i<lstDeter.length; i++){
			if(lstDeter[i] != null){
				if(lstDeter[i].getId() == userObjectDeterminacion.getId()){
					//se ha encontrado la determinacion
					((DeterminacionPanelBean)child).setMadre(lstDeter[i].getId());
					lstDeter[i].addDeterminacionHija((DeterminacionPanelBean)child);
				}
				else if(lstDeter[i].getLstDeterminacionesHijas() != null &&
						lstDeter[i].getLstDeterminacionesHijas().length != 0){
					buscarDeterminacionYAnadirDeterminacionNueva(lstDeter[i].getLstDeterminacionesHijas(), userObject, child);
					
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
        
		((DeterminacionPanelBean)child).setId(valorSecuencia);
		((DeterminacionPanelBean)child).setCodigo(GestorFipUtils.completarCodigoConCeros(
															String.valueOf(valorSecuencia)));
		((DeterminacionPanelBean)child).setNueva(true);

        if(parent.getUserObject() instanceof DeterminacionPanelBean){
        	
        	buscarDeterminacionYAnadirDeterminacionNueva(listDeterminaciones,parent.getUserObject(), child);
        }
        else{
        	if(listDeterminaciones == null){
    			
        		listDeterminaciones = new DeterminacionPanelBean[1];
        		listDeterminaciones[0] = (DeterminacionPanelBean)child;
    		
    		}
    		else{
    			listDeterminaciones = (DeterminacionPanelBean[]) Arrays.copyOf(listDeterminaciones, 
    					listDeterminaciones.length+1);
    		
    			listDeterminaciones[listDeterminaciones.length-1] = (DeterminacionPanelBean)child;
    		}

        }
      
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public void removeObject(Object child) {
        
    	boolean eliminarDeterminacion = buscarDeterminacionYElimDeterminacion(listDeterminaciones,
				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject());
        
    	
    	DeterminacionPanelBean[] lstDeter = listDeterminaciones;
    	if(eliminarDeterminacion){
    		
    	
    		((HideableTreeModel)tree.getModel()).removeNodeFromParent((DefaultMutableTreeNode)child);
    	}
        
        DeterminacionPanelBean[] lst = listDeterminaciones;

    }
    
    private boolean buscarDeterminacionYElimDeterminacion(DeterminacionPanelBean[] lstDeter,
            Object userObject){

    	boolean eliminarDeterminacion = true;
    	
		DeterminacionPanelBean userObjectDeterminacion = (DeterminacionPanelBean)userObject;
		
		for(int i=0; i<lstDeter.length && eliminarDeterminacion; i++){
		
			if(lstDeter[i] != null){
				
				if(lstDeter[i].getId() == userObjectDeterminacion.getId()){
				
					//se ha encontrado la determinacion
					if(! buscarSiDeterminacionEstaAsociada(listDeterminaciones, lstDeter[i])){
					
						elimDeter(lstDeter[i]);
						
						eliminarDeterminacion = true;
					}
					else{
						eliminarDeterminacion = false;
					}
				}
				else if(lstDeter[i].getLstDeterminacionesHijas() != null &&
					lstDeter[i].getLstDeterminacionesHijas().length != 0){
					eliminarDeterminacion = buscarDeterminacionYElimDeterminacion(lstDeter[i].getLstDeterminacionesHijas(), userObject);
				
				}
			}
		}
		return eliminarDeterminacion;
	}
    
    private void elimDeter(DeterminacionPanelBean deter){
    	
    	deter.setEliminada(true);
			
			if(deter.getLstDeterminacionesHijas() != null &&
					deter.getLstDeterminacionesHijas().length != 0){
				for(int j=0; j<deter.getLstDeterminacionesHijas().length; j++){
					
					elimDeter(deter.getLstDeterminacionesHijas()[j]);
					
				}
			}
    }
    
    private boolean buscarSiDeterminacionEstaAsociada(
    								DeterminacionPanelBean[] lstDeterminaciones,
    								DeterminacionPanelBean lstDeter){
    	
    	boolean determinacionAsociada = false;
    	
    	determinacionAsociada = buscarSiDeterminacionEstaAsociadaUnidad(lstDeterminaciones, lstDeter.getId());
    	
    	if(!determinacionAsociada){
    		determinacionAsociada = buscarSiDeterminacionEstaAsociadaBase(lstDeterminaciones, lstDeter.getId());
    	}
    	
    	if(!determinacionAsociada){
    		determinacionAsociada = buscarSiDeterminacionEstaAsociadaValorReferencia(lstDeterminaciones, lstDeter.getId());
    	}
    	
    	if(!determinacionAsociada){
    		determinacionAsociada = buscarSiDeterminacionEstaAsociadaDeterminacionReguladora(lstDeterminaciones, lstDeter.getId());
    	}
    	
    	if(!determinacionAsociada){
    		determinacionAsociada = buscarSiDeterminacionEstaAsociadaGrupoAplicacion(lstDeterminaciones, lstDeter.getId());
    	}
    	
    	if(!determinacionAsociada && lstDeter.getLstDeterminacionesHijas()!= null &&
    			lstDeter.getLstDeterminacionesHijas().length != 0){
    		for(int i=0;i<lstDeter.getLstDeterminacionesHijas().length && !determinacionAsociada; i++){
    			determinacionAsociada = buscarSiDeterminacionEstaAsociada(lstDeterminaciones,lstDeter.getLstDeterminacionesHijas()[i]);
    		}
    	}
    	
    	return determinacionAsociada;
    }
    
    private boolean buscarSiDeterminacionEstaAsociadaUnidad(DeterminacionPanelBean[] lstDeter, int idDeter){
    	
    	boolean determinacionAsociada = false;
    	for(int i=0; i<lstDeter.length && !determinacionAsociada; i++){
	    		
    		if(!determinacionAsociada){
				if(lstDeter[i] != null && lstDeter[i].getUnidadBean() != null && 
						lstDeter[i].getUnidadBean().getDeterminacion() != null && 
						lstDeter[i].getId() != idDeter){
					
					if(lstDeter[i].getUnidadBean().getDeterminacion().getId() == idDeter){
						
						JOptionPane.showMessageDialog(this, 
								I18N.get("LocalGISGestorFipDeterminaciones",
										"gestorFip.determinaciones.validacion.noeliminar.asociada.unidad"));
						return  true;
					}
					
					
				}
				if(lstDeter[i].getLstDeterminacionesHijas() != null && 
						lstDeter[i].getLstDeterminacionesHijas().length != 0){
					determinacionAsociada = buscarSiDeterminacionEstaAsociadaUnidad( lstDeter[i].getLstDeterminacionesHijas(), idDeter);
					
				}
    		}
    	}
    	return determinacionAsociada;
    }
    
    private boolean buscarSiDeterminacionEstaAsociadaBase(DeterminacionPanelBean[] lstDeter, int idDeter){
    	
    	boolean determinacionAsociada = false;
    	for(int i=0; i<lstDeter.length; i++){
    		
    		
    		if(!determinacionAsociada){
				if(lstDeter[i] != null && lstDeter[i].getBaseBean() != null && 
						lstDeter[i].getBaseBean().getDeterminacion() != null && 
						lstDeter[i].getId() != idDeter){
					
					if(lstDeter[i].getBaseBean().getDeterminacion().getId() == idDeter){
						
						JOptionPane.showMessageDialog(this, 
								I18N.get("LocalGISGestorFipDeterminaciones","gestorFip.determinaciones.validacion.noeliminar.asociada.base"));
						return  true;
					}
					
					
				}
				if(lstDeter[i].getLstDeterminacionesHijas() != null && 
						lstDeter[i].getLstDeterminacionesHijas().length != 0){
					determinacionAsociada = buscarSiDeterminacionEstaAsociadaBase( lstDeter[i].getLstDeterminacionesHijas(), idDeter);
					
				}
    		}

    	}
    	return determinacionAsociada;
    }
    
    private boolean buscarSiDeterminacionEstaAsociadaValorReferencia(DeterminacionPanelBean[] lstDeter, int idDeter){
    	boolean determinacionAsociada = false;
	    	for(int i=0; i<lstDeter.length; i++){
	    		
	    		if(!determinacionAsociada){
		    		if(lstDeter[i] != null && lstDeter[i].getValoresReferenciaPanelBean() != null && 
							lstDeter[i].getValoresReferenciaPanelBean().getLstDeterminacionesAsoc() != null && 
							lstDeter[i].getValoresReferenciaPanelBean().getLstDeterminacionesAsoc().length != 0 &&
							lstDeter[i].getId() != idDeter){
		    			
		    			
		    			for(int j=0; j<lstDeter[i].getValoresReferenciaPanelBean().getLstDeterminacionesAsoc().length; j++){
		    				
		    				if( lstDeter[i].getValoresReferenciaPanelBean().getLstDeterminacionesAsoc()[j] != null){
			    				if(lstDeter[i].getValoresReferenciaPanelBean().getLstDeterminacionesAsoc()[j].getId() == idDeter){
									
									JOptionPane.showMessageDialog(this, 
											I18N.get("LocalGISGestorFipDeterminaciones",
													"gestorFip.determinaciones.validacion.noeliminar.asociada.valorReferencia"));
									return  true;
								}
		    				}
		    			}

		    		}
		    		if(lstDeter[i].getLstDeterminacionesHijas() != null && 
							lstDeter[i].getLstDeterminacionesHijas().length != 0){
						determinacionAsociada = buscarSiDeterminacionEstaAsociadaValorReferencia( lstDeter[i].getLstDeterminacionesHijas(), idDeter);
						
					}
	    		}
	    		
	    	}
    	
    	return determinacionAsociada;   
    }
    
    private boolean buscarSiDeterminacionEstaAsociadaDeterminacionReguladora(DeterminacionPanelBean[] lstDeter, int idDeter){
    	boolean determinacionAsociada = false;
    	for(int i=0; i<lstDeter.length; i++){
    		
    		if(!determinacionAsociada){
	    		if(lstDeter[i] != null && lstDeter[i].getDeterminacionesReguladorasPanelBean() != null && 
						lstDeter[i].getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc() != null && 
						lstDeter[i].getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc().length != 0 &&
						lstDeter[i].getId() != idDeter){
	    			
	    			
	    			for(int j=0; j<lstDeter[i].getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc().length; j++){
	    				
	    				if( lstDeter[i].getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc()[j] != null){
		    				if(lstDeter[i].getDeterminacionesReguladorasPanelBean().getLstDeterminacionesAsoc()[j].getId() == idDeter){
								
								JOptionPane.showMessageDialog(this, 
										I18N.get("LocalGISGestorFipDeterminaciones",
												"gestorFip.determinaciones.validacion.noeliminar.asociada.determinacionReguladora"));
								return  true;
							}
	    				}
	    			}
	    						
	    		}
	    		if(lstDeter[i].getLstDeterminacionesHijas() != null && 
						lstDeter[i].getLstDeterminacionesHijas().length != 0){
					determinacionAsociada = buscarSiDeterminacionEstaAsociadaDeterminacionReguladora( lstDeter[i].getLstDeterminacionesHijas(), idDeter);
					
				}
    		}
    		
    	}
	
	return determinacionAsociada;   
    }
    
    private boolean buscarSiDeterminacionEstaAsociadaGrupoAplicacion(DeterminacionPanelBean[] lstDeter, int idDeter){
    	boolean determinacionAsociada = false;
    	for(int i=0; i<lstDeter.length; i++){
    		
    		if(!determinacionAsociada){
	    		if(lstDeter[i] != null && lstDeter[i].getGrupoAplicacionPanelBean() != null && 
						lstDeter[i].getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc() != null && 
						lstDeter[i].getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc().length != 0 &&
						lstDeter[i].getId() != idDeter){
	    			
	    			
	    			for(int j=0; j<lstDeter[i].getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc().length; j++){
	    				
	    				if( lstDeter[i].getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc()[j] != null){
		    				if(lstDeter[i].getGrupoAplicacionPanelBean().getLstDeterminacionesAsoc()[j].getId() == idDeter){
								
								JOptionPane.showMessageDialog(this, 
										I18N.get("LocalGISGestorFipDeterminaciones",
												"gestorFip.determinaciones.validacion.noeliminar.asociada.grupoAplicacion"));
								return  true;
							}
	    				}
	    			}
	    						
	    			
	    		}
	    		if(lstDeter[i].getLstDeterminacionesHijas() != null && 
						lstDeter[i].getLstDeterminacionesHijas().length != 0){
					determinacionAsociada = buscarSiDeterminacionEstaAsociadaGrupoAplicacion( lstDeter[i].getLstDeterminacionesHijas(), idDeter);
					
				}
    		}
    		
    	}
	
	return determinacionAsociada;   
    }
    
    
    public void modifObject(Object node, DeterminacionPanelBean det) {
    	
    	if(node instanceof DeterminacionPanelBean){
    		    		
    		((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).setUserObject(det);	  
    		tree.expandPath(tree.getSelectionPath());
    		tree.repaint();
    		
    		buscarDeterminacionYModifDeterminacion(listDeterminaciones,
    				((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject(), det);
    		
    	}
    	
    }
	
    private void buscarDeterminacionYModifDeterminacion(DeterminacionPanelBean[] lstDeter,
            Object userObject, Object child){

		DeterminacionPanelBean userObjectDeterminacion = (DeterminacionPanelBean)userObject;
		
		for(int i=0; i<lstDeter.length; i++){
		
			if(lstDeter[i] != null){
				if(lstDeter[i].getId() == userObjectDeterminacion.getId()){
					//se ha encontrado la determinacion
					if(!lstDeter[i].isNueva()){
						((DeterminacionPanelBean)child).setModificada(true);
					}
					else{
						((DeterminacionPanelBean)child).setNueva(true);
					}
					lstDeter[i] =(DeterminacionPanelBean)child;
				}
				else if(lstDeter[i].getLstDeterminacionesHijas() != null &&
					lstDeter[i].getLstDeterminacionesHijas().length != 0){
					buscarDeterminacionYModifDeterminacion(lstDeter[i].getLstDeterminacionesHijas(), userObject, child);
				
				}
			}
		}
	
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
    
	public  DeterminacionPanelBean[] getListDeterminaciones() {
		return listDeterminaciones;
	}

	public void setListDeterminaciones(
			DeterminacionPanelBean[] listDeterminaciones) {
		this.listDeterminaciones = listDeterminaciones;
	}
	
	public int getValorSecuencia() {
		return valorSecuencia;
	}

	public void setValorSecuencia(int valorSecuencia) {
		this.valorSecuencia = valorSecuencia;
	}
	
	 public void selectPaths(TreePath[] paths){
	    	tree.setSelectionPaths(paths);
	    }
	    
	    public ArrayList searchTree(JTree tree, TreePath path, String strFind, ArrayList lstPath) 
	    {
	    	ArrayList lstPaths = lstPath;
	    	TreeNode node = (TreeNode)path.getLastPathComponent();
	    	if(((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject() instanceof DeterminacionPanelBean){
	    		DeterminacionPanelBean epb = (DeterminacionPanelBean)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
	    		
	    		//if(node==null) return;
	    		if(epb.getNombre().toLowerCase().contains(strFind.toLowerCase()))
			    {
			    	tree.setScrollsOnExpand(true);
			    	lstPaths.add(path);
			    	//tree.setSelectionPath(path); 
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
}
