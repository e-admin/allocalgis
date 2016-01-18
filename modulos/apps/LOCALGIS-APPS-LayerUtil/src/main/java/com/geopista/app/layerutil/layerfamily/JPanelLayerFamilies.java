/**
 * JPanelLayerFamilies.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.layerfamily;


/**
 * Panel que muestra todas las LayerFamilies del sistema 
 * 
 * @author cotesa
 *
 */

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.layer.LayerTable;


public class JPanelLayerFamilies extends JPanel  //implements TreeSelectionListener 
{
    private JTree tree;
    private static boolean DEBUG = true;
    
    protected static int UP = 0;
    protected static int DOWN = 1;
    
    private static boolean useSystemLookAndFeel = true;
    
    protected DefaultTreeModel treeModel;
    protected DefaultMutableTreeNode top = new DefaultMutableTreeNode("LayerFamilies del sistema");
    
    
    /**
     * Constructor de la clase
     * @param modoSeleccion Modo de selección de los elementos del árbol que se carga en el panel
     */
    public JPanelLayerFamilies(int modoSeleccion) {
        super(new GridLayout(1,0));
        
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't use system look and feel.");
        }
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Crea los nodos
        //top.removeAllChildren();
        createNodes(top);
        
        //Crea un arbol que permite la seleccion indicada en modoSeleccion
        treeModel = new DefaultTreeModel(top);
        treeModel.addTreeModelListener(new LayerFamilyTreeModelListener());
        
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(modoSeleccion);
        
        //Aspecto del arbol (renderer)
        tree.setCellRenderer(new LayerFamilyTreeRenderer());
        tree.setCellEditor(new LayerFamilyTreeCellEditor());
          
        tree.setName("LayerFamilies");
        tree.setShowsRootHandles(false);
        
        
        //Crea el scroll pane donde se mostrará el arbol
        JScrollPane treeView = new JScrollPane(tree);
        
        add(treeView, null);
    }
    
    /**
     * Crea los nodos del árbol 
     * @param top Nodo padre bajo el que se añaden los nodos
     */
    private void createNodes(DefaultMutableTreeNode top) {
        
        try{      
            
            LayerOperations operaciones = new LayerOperations();  
            LayerFamilyTable[] lstLayerFamilies = operaciones.obtenerLayerFamilyTable();            
            LayerTable[] lstCapas = operaciones.obtenerLayerTable(true);
            HashMap hmRelaciones = operaciones.obtenerRelacionesLayerFamiliesLayers();
            
            LayerFamilyTable lftab = null;
            for (int i=0; i< lstLayerFamilies.length; i++)
            {
                DefaultMutableTreeNode category = null;
                DefaultMutableTreeNode nodo = null;
                
                lftab = (LayerFamilyTable) lstLayerFamilies[i];
                
                category = new DefaultMutableTreeNode(lftab); 
                top.add(category);
                
                List lstLayers = (ArrayList)hmRelaciones.get(new Integer(lftab.getIdLayerFamily()));
                
                LayerTable capa = null;
                
                if (lstLayers!=null)
                {
                    Iterator it = lstLayers.iterator();
                    HashMap hm = lftab.getHmPosLayers();
                    if (hm== null)
                        hm = new HashMap();
                    int indice = 0;
                    while (it.hasNext())
                    {                    
                        capa = buscarLayerFromId(((Integer)it.next()).intValue(), lstCapas);
                        if (capa!=null)
                        {
                            indice++;
                            hm.put(new Integer(indice), capa);
                        }
                        
                        nodo = new DefaultMutableTreeNode(capa);
                        category.add(nodo);
                    }
                    
                    lftab.setHmPosLayers(hm);
                }
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't use system look and feel.");
            }
        }
        
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        
        JFrame frame = new JFrame("Arbol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        JPanelLayerFamilies newContentPane = new JPanelLayerFamilies(TreeSelectionModel.SINGLE_TREE_SELECTION);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        
        //mostrar la ventana
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Método principal de la clase
     * @param args
     */
    public static void main(String[] args) {
        
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
     * Busca una LayerTable dentro de una lista de LayerTables a partir del identificador
     * de la capa (Layer)
     * @param idLayer Identificador de la Layer
     * @param lstLayers Lista de objetos LayerTable en la que buscar
     * @return LayerTable cuya Layer tiene el identificador especificado
     */
    public LayerTable buscarLayerFromId(int idLayer, LayerTable[] lstLayers)
    {
        for (int i=0; i< lstLayers.length; i++)
        {
            if(lstLayers[i].getIdLayer() == idLayer)
                return lstLayers[i];
        }
        return null;
        
    }
    
    /**
     * Habilita y deshabilita el árbol de LayerFamilies
     */
    public void setEnabled (boolean enabled)
    {
        tree.setEnabled(enabled);
        
    }
    
    
    /** Remove the currently selected node. */
    public void removeCurrentNode() 
    {
        TreePath currentSelection = tree.getSelectionPath();
        
        int[] pos = tree.getSelectionRows();
        
        if (currentSelection != null) 
        {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
            (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) 
            {
                treeModel.removeNodeFromParent(currentNode);
                tree.setSelectionRow(pos[0]);
                return;
            }
            
        }        
        
    }
    
    /**
     * Modifica la posición de un nodo, subiéndolo o bajándolo en el árbol
     * 
     * @param sentido JPanelLayerFamilies.UP para subir el nodo, 
     * y JPanelLayerFamilies.DOWN para bajarlo
     */
    public void moveCurrentNode (int sentido)
    {
        TreePath currentSelection = tree.getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
        DefaultMutableTreeNode nodoCambio = new DefaultMutableTreeNode();
        
        if (sentido==UP)
        {
            nodoCambio = currentNode.getPreviousSibling();
        }
        else if (sentido == DOWN)
        {
            nodoCambio = currentNode.getNextSibling();
        }
        
        LayerTable actual = (LayerTable)(currentNode.getUserObject());
        LayerTable objCambio = (LayerTable)nodoCambio.getUserObject();
        TreePath pathCambio = getPath(nodoCambio);
        
        
        treeModel.valueForPathChanged(currentSelection, objCambio);
        treeModel.valueForPathChanged(pathCambio, actual);
        
        tree.setSelectionPath(pathCambio);
        
    }
    
    /**
     * Selecciona el último nodo hijo de un nodo padre
     * @param padre Nodo padre
     */
    public void selectLastNode (DefaultMutableTreeNode padre)
    {
        DefaultMutableTreeNode lastNode = padre.getLastLeaf();
        TreePath pathCambio = getPath(lastNode);
        tree.setSelectionPath(pathCambio);
        
    }
    
    
    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child, Object elementoAnterior)
    {
        return addObject(child, elementoAnterior, -1);
    }
    
    /**
     * Añade un nodo 
     * @param child Nodo a añadir
     * @param elementoAnterior Elemento tras el cual insertar el nodo a añadir
     * @param posicion Posición en la que añadir el nodo
     * @return Nodo hijo añadido
     */
    public DefaultMutableTreeNode addObject(Object child, Object elementoAnterior, int posicion)
    {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        
        
        if (elementoAnterior instanceof LayerFamilyTable)
        {
            parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
            
        }
        else if (elementoAnterior instanceof LayerTable)
        {
            parentNode = (DefaultMutableTreeNode)(parentPath.getParentPath().getLastPathComponent());
        }
        
        return addObject(parentNode, child, posicion, true);
    }
    
    
    /**
     * Añade un nodo hijo a un nodo padre
     * 
     * @param parent Nodo padre
     * @param child Nodo hijo a añadir
     * @param posicion Posición del nodo hijo dentro de los nodos del padre
     * @param shouldBeVisible Verdadero si se desea desplazar el arbol para permitir
     * la visibilidad del nodo añadido
     * @return Nodo añadido
     */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child, int posicion, boolean shouldBeVisible) 
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        
        if (parent == null) {
            parent = top;
        }
        
        if (posicion < 0)
        {
            posicion = parent.getChildCount();
        }
        
        treeModel.insertNodeInto(childNode, parent, posicion);
        
        
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }
    
    /**
     * Obtiene el path de un nodo
     * 
     * @param node Nodo del que averiguar el path
     * @return TreePath del nodo
     */
    public TreePath getPath(TreeNode node) {
        List list = new ArrayList();
        
        
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);    
        
        return new TreePath(list.toArray());
    }
    
    
}

