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


package com.geopista.app.layerutil.schema.table;


/**
 * Panel que muestra todas las tablas de sistema de geopista, dadas de alta
 * en la tabla <code>tables</code>
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
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.feature.Column;
import com.geopista.feature.Table;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

import java.util.ArrayList;
import java.util.List;


import java.awt.GridLayout;


public class JPanelTables extends JPanel  implements TreeSelectionListener {
    
    private JTree tree;
    private static boolean DEBUG = false;
    
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
    
    private static boolean useSystemLookAndFeel = true;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    /**
     * Constructor de la clase
     * 
     * @param modoSeleccion Modo de selección de los elementos contenidos en el árbol de tablas
     */
    public JPanelTables(int modoSeleccion) {
        super(new GridLayout(1,0));
        
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't use system look and feel.");
        }
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        
        // Internacionalizacion
        //aplicacion.loadI18NResource("GestorCapasi18n");
        
        //Crea los nodos
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(I18N.get("GestorCapas","tablas.arbol.titulo"));
        createNodes(top);

        //Crea un arbol que permite la seleccion indicada en modoSeleccion
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(modoSeleccion);
        
        //Aspecto del arbol (renderer)
        tree.setCellRenderer(new TablesTreeRenderer());
        
        //Listener de los cambios en el arbol
        tree.addTreeSelectionListener(this);
        
        tree.setName("Tablas");
        
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
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        
        if (node == null) return;
        
        Object nodeInfo = node.getUserObject();
        if (nodeInfo instanceof Column) {
            Column col = (Column)nodeInfo;
            
            if (DEBUG) {
                System.out.println("NOMBRE COL "+col.getName() + ":  \n    ");
            }
        } else {
            
        }
        if (DEBUG) {
            System.out.println(nodeInfo.toString());
        }
    }
    
    
    
    /**
     * Crea los nodos del árbol
     * 
     * @param top Elemento padre de los nodos creados
     */
    private void createNodes(DefaultMutableTreeNode top) {
        
    	DefaultMutableTreeNode tablasSistema = null;
    	DefaultMutableTreeNode tablasExternal = null;
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode nodo = null;
        
        try{

        	tablasSistema = new DefaultMutableTreeNode(I18N.get("GestorCapas","tablas.arbol.titulo.tablaSistema"));
        	tablasExternal = new DefaultMutableTreeNode(I18N.get("GestorCapas","tablas.arbol.titulo.tablaExterna"));
            top.add(tablasSistema);
        	top.add(tablasExternal);
        	
            List lstTables = (List)Identificadores.get("Tablas");
            ArrayList lstIdTablasExt = new ArrayList();
            
            if (lstTables==null)
            {
                LayerOperations operaciones = new LayerOperations();                
                lstTables = operaciones.obtenerListaTablas();
                
                Identificadores.put("Tablas",lstTables);
            }
            
            for (int i=0; i< lstTables.size(); i++)
            {
                Table tab = (Table) lstTables.get(i);
                
                category = new DefaultMutableTreeNode(tab); 
                if (tab.getExternal()==0)
                	tablasSistema.add(category);
                else{
                	tablasExternal.add(category);
                	lstIdTablasExt.add(tab.getIdTabla());
                }
            }
            
            
            List lstColumns = (List)Identificadores.get("Columnas");
            
            if (lstColumns==null)
            {
                LayerOperations operaciones = new LayerOperations();
                
                lstColumns = operaciones.obtenerListaColumnas();
                
                Identificadores.put("Columnas", lstColumns);
            }
            
            
            for (int i=0; i< lstColumns.size(); i++) 
            {                
                
                Column col = (Column)lstColumns.get(i);
                nodo = new DefaultMutableTreeNode(col);
                
                DefaultMutableTreeNode padre;
                //buscar la tabla a la que corresponde la columna
                if (!lstIdTablasExt.contains(col.getTable().getIdTabla()))
                	padre = buscarHojaTabla(col, tablasSistema);
                else
                	padre = buscarHojaTabla(col, tablasExternal);
                
                padre.add(nodo);
                
            }

        }catch(Exception e){
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
        
        
        JPanelTables newContentPane = new JPanelTables(TreeSelectionModel.SINGLE_TREE_SELECTION);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        
        //mostrar la ventana
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Método principal de la clase
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
     * Busca el nodo donde añadir un hijo
     * @param col Column que representa el nodo hijo a añadir
     * @param top Nodo a recorrer para encontrar la posición del nodo hijo
     * @return Nodo donde añadir el nodo hijo
     */
    public DefaultMutableTreeNode buscarHojaTabla(Column col, DefaultMutableTreeNode top)
    {
        DefaultMutableTreeNode hojaTabla=null;
        for (int i=0;i<top.getChildCount()&&hojaTabla==null;i++)
        {
            DefaultMutableTreeNode aux =(DefaultMutableTreeNode)top.getChildAt(i);
            Object nodeInfo = aux.getUserObject();
            if (nodeInfo instanceof Table)
            {
                if(((Table)nodeInfo).getIdTabla() == col.getTable().getIdTabla())
                {
                    col.setTable((Table)nodeInfo);
                    return aux;
                }
                
                
                
            }
        }
        return hojaTabla;
    }
    
    /**
     * Habilita y deshabilita el árbol que se muestra en el panel
     * @param enabled Verdadero si está habilitado y Falso si está deshabilitado
     */
    public void setEnabled (boolean enabled)
    {
        tree.setEnabled(enabled);
        
    }
    
    
}

