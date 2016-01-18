/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Panel que muestra todas las tablas contenidas en la Base de Datos geopista
 * 
 * @author cotesa
 *
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.GestorCapas;
import com.geopista.app.layerutil.schema.table.JPanelTables;
import com.geopista.feature.Table;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import java.awt.GridLayout;



public class JPanelDBTables extends JPanel  implements TreeSelectionListener {
    
    private JTree tree;
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    private static boolean useSystemLookAndFeel = true;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    private String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, GestorCapas.DEFAULT_LOCALE, false);
    
    
    /**
     * Constructor de la clase
     * 
     * @param modoSeleccion Modo de selección de los elementos del árbol cargado en el panel
     */
    public JPanelDBTables(int modoSeleccion) {
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
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(I18N.get("GestorCapas","tablasBD.arbol.titulo"));
        createNodes(top);
        
        //Crea un arbol que permite la seleccion indicada en modoSeleccion
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(modoSeleccion);
        
        //Aspecto del arbol (renderer)
        tree.setCellRenderer(new TablesDBTreeRenderer());      
        
        //Listener de los cambios en el arbol
        tree.addTreeSelectionListener(this);
        
        tree.setName("TablasBD");
        tree.setEditable(false);
        
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }        
        
        //Crea el scroll pane donde se mostrará el arbol
        JScrollPane treeView = new JScrollPane(tree);
        
        add(treeView, null);
    }
    
    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        
    }
    /**
     * Crea los nodos del árbol de tablas de Base de Datos
     * 
     * @param top Nodo padre
     */
    private void createNodes(DefaultMutableTreeNode top) {       
        
        DefaultMutableTreeNode category = null;       
        
        try{
            
            List lstTablesBD = (List)Identificadores.get("TablasBD");
            
            if (lstTablesBD==null)
            {
                TablesDBOperations operaciones = new TablesDBOperations();                
                lstTablesBD = operaciones.obtenerListaTablasBD();
                
                Identificadores.put ("TablasBD",lstTablesBD);
            }
            
            for (int i=0; i< lstTablesBD.size(); i++)
            {
                Table tab = (Table) lstTablesBD.get(i);
                
                category = new DefaultMutableTreeNode(tab); 
                top.add(category);
            }           
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Añade un nodo al árbol 
     * @param table Tabla a añadir como nodo
     * @return Árbol con el nodo añadido
     */
    public JTree addNode (Table table){
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        root.add(new DefaultMutableTreeNode(table));
        tree = new JTree(root);
        
        return tree;         
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
        
        JPanelTables newContentPane = new JPanelTables(TreeSelectionModel.SINGLE_TREE_SELECTION);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        
        //mostrar la ventana
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Método principal de la aplicación
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
     * Habilita y deshabilita el árbol
     * @param enabled Verdadero para habilitar
     */
    public void setEnabled (boolean enabled)
    {
        tree.setEnabled(enabled);
        
    }
    /**
     * Añade un nodo al árbol
     * 
     * @param child Nodo a añadir
     * @return Árbol con el nodo añadido
     */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode child) {
        DefaultMutableTreeNode parentNode = 
            (DefaultMutableTreeNode)tree.getModel().getRoot();
        
        return addObject(parentNode, child, true);
    }
    /**
     * Añade un nodo al árbol
     * 
     * @param parent Padre del nodo a añadir
     * @param childNode Nodo a añadir
     * @param shouldBeVisible Verdadero si se desea desplazar la visibilidad del 
     * árbol hasta el nodo añadido
     * @return Nodo añadido
     */
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
            DefaultMutableTreeNode childNode,
            boolean shouldBeVisible) {
        
        
        ((DefaultTreeModel)tree.getModel()).insertNodeInto(childNode, parent,
                getAlphabeticalPosition(parent, childNode));        
        
        
        //Asegura que el usuario pueda ver el nuevo nodo
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));            
        }        
        
        return childNode;
    }
    
    /**
     * Obtiene la posición en la que incluir un nodo hijo dentro de un nodo padre, teniendo
     * en cuenta un orden alfabético.
     * 
     * @param parent Nodo padre
     * @param child Nodo hijo
     * @return Posición 
     */
    private int getAlphabeticalPosition(DefaultMutableTreeNode parent,
            DefaultMutableTreeNode child)
    {
        int tam = parent.getChildCount();
        List col = new ArrayList();
        for (int i=0; i<tam; i++)
        {
            col.add(parent.getChildAt(i));
        }
        col.add(child);        
        
        
        Comparator nodesComparator = new Comparator(){
            public int compare(Object o1, Object o2) {
                
                Table tab1 = (Table)((DefaultMutableTreeNode)o1).getUserObject();
                Table tab2 = (Table)((DefaultMutableTreeNode)o2).getUserObject();                
                
                Collator myCollator=Collator.getInstance(new Locale(locale));
                myCollator.setStrength(Collator.PRIMARY);
                return myCollator.compare(tab1.getDescription(), tab2.getDescription());                
                
            }
        };    
        
        
        Collections.sort(col, nodesComparator);
        
        return col.indexOf(child);
        
    }
    
    
    /**
     * Elimina un nodo dentro del árbol
     * 
     * @param child Nodo a eliminar
     */
    public void removeObject(Object child) {
        
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getModel().getRoot();        
        parentNode.remove((DefaultMutableTreeNode)child);
        ((DefaultTreeModel)tree.getModel()).reload();        
        return;
    }
    
    
    
}

