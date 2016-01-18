
package com.geopista.app.eiel.panels;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.utils.HideableNode;
import com.geopista.app.eiel.utils.HideableTreeModel;
import com.geopista.feature.Table;
import com.geopista.protocol.administrador.dominios.DomainNode;
import javax.swing.tree.DefaultTreeModel;

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
                
        this.locale= AppContext.getApplicationContext().getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
        
        try {
        	logger.info("Recuperando dominios de la EIEL");
        	
        	EIELDominioPanelTree.lstReferencias = ConstantesLocalGISEIEL.
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
