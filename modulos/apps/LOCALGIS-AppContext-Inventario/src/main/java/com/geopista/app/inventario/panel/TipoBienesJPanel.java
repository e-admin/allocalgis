/**
 * TipoBienesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.inventario.Const;
import com.geopista.util.exception.CancelException;

public class TipoBienesJPanel extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(TipoBienesJPanel.class);

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private DefaultMutableTreeNode top;
    private String locale;
    private Object[] seleccion= new Object[2];
    private ArrayList actionListeners= new ArrayList();
    private DomainNode nodoSelected;
    
    TreeSelectionListener treeSelectionListener;
    
    private boolean eventsDisabled=false;
    private boolean modoSeleccion=true;


    /**
     * Método que genera el panel con el arbol referente al inventario del patrimonio para las aplicaciones cliente
     * @param desktop
     * @param locale
     */
    public TipoBienesJPanel(javax.swing.JFrame desktop, String locale) {
        this.desktop= desktop;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.locale= locale;
        try{
	        initComponents();
	        renombrarComponentes();
        }
        catch (CancelException e){
        	
        }
    }

    /**
     * Método llamado por el constructor para inicializar el formulario
     */
    private void initComponents() throws CancelException{
        top= new DefaultMutableTreeNode(aplicacion.getI18nString("inventario.epigrafes.tag1"));

        initArbol();
        bienesJTree= new javax.swing.JTree(top);
        bienesJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        bienesJTree.setCellRenderer(new TreeCellRendererTiposBienes(locale));

        treeJScrollPane= new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        treeSelectionListener= new TreeSelectionListener() {
              public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                  bienesJTreeValueChanged(treeSelectionEvent);
              }
            };
        bienesJTree.addTreeSelectionListener(treeSelectionListener);


        treeJScrollPane.setViewportView(bienesJTree);
        add(treeJScrollPane, BorderLayout.CENTER);

    }
    
    public void disableEvents(){
    	eventsDisabled=true;
    	 /*bienesJTree.removeTreeSelectionListener(treeSelectionListener);
    	 treeSelectionListener= new TreeSelectionListener() {
             public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            	 bienesJTreeValueChangedEspecial(treeSelectionEvent);
             }
           };
    	 treeSelectionListener=null;*/
    }
    public void enableEvents(){
    	eventsDisabled=false;
    	/*
    	 bienesJTree.removeTreeSelectionListener(treeSelectionListener);
    	treeSelectionListener= new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                bienesJTreeValueChanged(treeSelectionEvent);
            }
          };
      bienesJTree.addTreeSelectionListener(treeSelectionListener);
	*/
    }

    public void bienesJTreeValueChanged(TreeSelectionEvent e) {
        if (e==null || !(e.getSource() instanceof JTree)) return;
        JTree arbol= (JTree)e.getSource();
        DefaultMutableTreeNode node= (DefaultMutableTreeNode)arbol.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo= node.getUserObject();
        nodoSelected= null;
        if (nodeInfo == null) return;
        if (nodeInfo instanceof DomainNode) {
            DomainNode dn= (DomainNode)nodeInfo;
            if (node.isLeaf()){
                /** se trata de un subtipo de bienes de patrimonio. Cargamos los bienes existentes para ese tipo y ese subtipo. */
                DefaultMutableTreeNode treeNode= (DefaultMutableTreeNode)node.getParent();
                if (treeNode == null) return;
                Object ninfo= treeNode.getUserObject();
                if (ninfo instanceof DomainNode){
                    setSeleccion(((DomainNode)ninfo).getPatron(), dn.getPatron());
                    nodoSelected= (DomainNode)ninfo;
                    if (!eventsDisabled){
                    	//setModoSeleccion(false);
                    	fireActionPerformed();
                    }
                }
            }else{
                /** tanto la busqueda como el filtrado parte de la seleccion de un tipo de inventario */
            	/*ASO cambia para saber el tipo elegido*/
                setSeleccion(dn.getPatron(), null);
                if (!eventsDisabled){
                	//setModoSeleccion(false);
                	fireActionPerformed();
                }
            }
        }else{
            /** seleccionada la raiz del arbol */
            if (arbol.getSelectionPath().getParentPath() == null){
                setSeleccion(null, null);
                if (!eventsDisabled){
                	//setModoSeleccion(false);
                	fireActionPerformed();
                }
            }

        }

    }
    
    private void initArbol() throws CancelException{

        if (top == null) return;

        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        DefaultMutableTreeNode tipo= null;
        DefaultMutableTreeNode subtipo= null;

        ListaEstructuras listaTipos= Estructuras.getListaClasificacionBienesPatrimonio();
        ListaEstructuras listaSubtipos= Estructuras.getListaSubtipoBienesPatrimonio();
        ListaEstructuras listaSubtipoLotes= Estructuras.getListaSubtipoLotes();

        if ((listaTipos == null) || (listaSubtipos == null) || 
        		listaSubtipoLotes ==null )return;
        
        Vector vDomainNodesTipos= listaTipos.getListaSortedByPatron();
        Vector vDomainNodesSubtipos= listaSubtipos.getListaSortedByPatron();
        Vector vDomainNodesSuptipoLotes= listaSubtipoLotes.getListaSortedByPatron();
        
        if ((vDomainNodesTipos != null) && (vDomainNodesSubtipos != null)){
            for (Enumeration e=vDomainNodesTipos.elements();e.hasMoreElements();){
                DomainNode dnodeTipo= (DomainNode)e.nextElement();
                tipo= new DefaultMutableTreeNode(dnodeTipo);
                top.add(tipo);
                
                if (!dnodeTipo.getPatron().equalsIgnoreCase(Const.SUPERPATRON_LOTES)) {
                	for (Enumeration<DomainNode> e2=vDomainNodesSubtipos.elements();e2.hasMoreElements();){
                		DomainNode dnodeSubtipo= (DomainNode)e2.nextElement();
                		subtipo= new DefaultMutableTreeNode(dnodeSubtipo);
                		tipo.add(subtipo);
                	}
                }else{
                	for (Enumeration<DomainNode> e2=vDomainNodesSuptipoLotes.elements();e2.hasMoreElements();){
                		DomainNode dnodeSubtipo= (DomainNode)e2.nextElement();
                		subtipo= new DefaultMutableTreeNode(dnodeSubtipo);
                		tipo.add(subtipo);
                	}
                }
            }
        }

    }


    public void setSeleccion(String tipo, String subtipo){
    	
    	//Si cambiamos de un epigrafe hacia el nodo raiz marcamos
    	//que no debemos seleccionar los elementos.(modoSeleccion=false)
    	if ((seleccion[0]!=null) && (seleccion[1]!=null) && 
    			((tipo==null) || (subtipo==null)))
    		modoSeleccion=false;
    	else
    		modoSeleccion=true;
    	
        seleccion= new Object[2];
        seleccion[0]= tipo;
        seleccion[1]= subtipo;
    }

    public boolean isModoSeleccion(){
    	return modoSeleccion;
    }
    public void setModoSeleccion(boolean modoSeleccion){
    	this.modoSeleccion=modoSeleccion;
    }
    
    public String getTipoSeleccionado(){
        return (String)seleccion[0];
    }

    public String getSubtipoSeleccionado(){
        return (String)seleccion[1];
    }
    
    public void setSubtipoSeleccionado(String tipo){
    	seleccion[1]=tipo;
    }


    public void renombrarComponentes() throws CancelException{
        setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.epigrafes.tag1")));
        initArbol();

        if (nodoSelected == null) return;
        top.setUserObject(nodoSelected);
   }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    public void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    public javax.swing.JTree bienesJTree;
    private javax.swing.JScrollPane treeJScrollPane;
    
}
