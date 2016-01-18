/**
 * EIELDominioPanelTree.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.eiel.panels;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.app.eiel.utils.HideableTreeModel;
import com.geopista.feature.Table;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.util.config.UserPreferenceStore;

public class EIELDominioPanelTree extends JPanel  {
    
    private JTree tree;    
    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";    
   
    private static ArrayList lstReferencias = null;
    private boolean referenciasTraidas[];
    private ArrayList lstReferenciasBorradas = new ArrayList();
    
    DefaultMutableTreeNode parent = null;

	private String locale = null;
	
	static Logger logger = Logger.getLogger(EIELDominioPanelTree.class);
	

    public EIELDominioPanelTree(int modoSeleccion) {
    	
        super(new GridLayout(1,0));
                
        this.locale= UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
        
        try {
        	logger.info("Recuperando dominios de la EIEL");
        	
        	EIELDominioPanelTree.lstReferencias = InitEIEL.
        			clienteLocalGISEIEL.getDominiosEIEL(ConstantesLocalGISEIEL.DOMAIN_NAME,locale); 	
        	
        	referenciasTraidas = new boolean[lstReferencias.size()];

        	for(int i=0;i< referenciasTraidas.length;i++)
        	{
        		referenciasTraidas[i] = false;
        	}

        	DefaultMutableTreeNode top = new DefaultMutableTreeNode();

        	//Crea los nodos
        	createNodes(top);

        	//Crea un arbol que permite la seleccion indicada en modoSeleccion
        	HideableTreeModel ml = new HideableTreeModel( top );
        	ml.activateFilter(true);
        	tree = new JTree( ml );

        	tree.getSelectionModel().setSelectionMode(modoSeleccion);

        	//Aspecto del arbol (renderer)
        	tree.setCellRenderer(new TreeRendereEIELDomains(locale));        

        	tree.setName("Información de la EIEL");
        	tree.setEditable(false);        	

        	if (playWithLineStyle) {
        		System.out.println("line style = " + lineStyle);
        		tree.putClientProperty("JTree.lineStyle", lineStyle);
        	}
        	
    	   
        	
        	add(tree, null);
        	tree.setSelectionRow(0);

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    
    
    
 
    private void createNodes(DefaultMutableTreeNode top) 
    {   
       
        Iterator it = lstReferencias.iterator();
        while(it.hasNext())
        {
            Object o = it.next();
            if (o instanceof DomainNode){            	
               top.add(new HideableNode((DomainNode)o));           
            }               
        }           
    }
 
    public JTree addNode (Table table)
    {       
        HideableNode root = (HideableNode)tree.getModel().getRoot();
        root.add(new HideableNode(table));
        HideableTreeModel ml = new HideableTreeModel( root );
        ml.activateFilter(true);
        tree = new JTree( ml );
        return tree;        
    }
    
        
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
       
        JFrame frame = new JFrame("Arbol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        
        EIELDominioPanelTree newContentPane =  new EIELDominioPanelTree(TreeSelectionModel.SINGLE_TREE_SELECTION);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        
        //mostrar la ventana
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
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
     * @param enabled True si está habilitado
     */
    public void setEnabled (boolean enabled)
    {
        tree.setEnabled(enabled);        
    }
    
    /**
     * 
     * @param child
     * @return
     */
    public DefaultMutableTreeNode addObject(Object child) {
        HideableNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            //No hay selección. Por defecto se marca el nodo raíz
            parentNode = (HideableNode)tree.getModel().getRoot();
        } else {
            parentNode = (HideableNode)
                         (parentPath.getLastPathComponent());            
        }

        return addObject(parentNode, child, true);
    }
    /**
     * 
     * @param parent
     * @param child
     * @param shouldBeVisible
     * @return
     */
    public HideableNode addObject(HideableNode parent,
            Object child,
            boolean shouldBeVisible) {
        HideableNode childNode =
        	new HideableNode(child);

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

    public DomainNode fillObject(DefaultMutableTreeNode nodoPath)
    {
    	DomainNode resource = null;
       
    	if (nodoPath.getUserObject() instanceof DomainNode)
    	{  
    		resource = (DomainNode)nodoPath.getUserObject();


    		if(nodoPath.children()==DefaultMutableTreeNode.EMPTY_ENUMERATION){

    			try {

    				DomainNode childResource = (DomainNode) nodoPath.getUserObject();

    				Enumeration enumeration = childResource.getlHijos().gethDom().elements();
    				TreeMap hDomTree =new TreeMap();

    				while(enumeration.hasMoreElements()){
    					Object obj=enumeration.nextElement();
    					if (obj instanceof DomainNode){
	    					DomainNode domainNode = (DomainNode) obj;
	    					if ((domainNode.getPatron()!=null && domainNode.getFirstTerm().equals("Infraestructura Alumbrado")))
    							continue;
	    					hDomTree.put(domainNode.getFirstTerm(),domainNode);
	    					
    					}
    				}
    				
    				for (Iterator iterator = hDomTree.keySet().iterator(); iterator
							.hasNext();) {
						String key= (String) iterator.next();

							HideableNode nodoHijo = new HideableNode (hDomTree.get(key));
							nodoPath.add(nodoHijo);
						
					}
    				
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}

    	}
        return resource;
    }

    private void rellenaListRefer(Object resource)
    {
        for(int i =0; i<lstReferencias.size();i++)
        {
            if(resource instanceof DomainNode)
            {
                lstReferencias.set(i, resource);
                referenciasTraidas[i] = true;
            }
            
        }
    }
    
    private void collapseAll(JTree tree)
    {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }


    public static ArrayList getLstReferencias() {
        return lstReferencias;
    }

    public void annadeResourceDescriptor(Object resource)
    {
        lstReferencias.add(resource);
        boolean aux[] = new boolean[lstReferencias.size()];
        for(int i = 0; i<referenciasTraidas.length;i++)
        {
            aux[i] = referenciasTraidas[i];
        }
        aux[aux.length-1] = true;
        referenciasTraidas = aux;
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


    public ArrayList getLstReferenciasBorradas()
    {
        return lstReferenciasBorradas;
    }

    public void setLstReferenciasBorradas(ArrayList lstReferenciasBorradas)
    {
        this.lstReferenciasBorradas = lstReferenciasBorradas;
    }

    public ArrayList getResourcesEnMemoria()
    {
        ArrayList lstResourcesMem = new ArrayList();
        for(int i =0;i<lstReferencias.size();i++)
        {
            if(referenciasTraidas[i])
            {
            	lstResourcesMem.add(lstReferencias.get(i));
            }
        }
        return lstResourcesMem;
    }
    
    
  
}
