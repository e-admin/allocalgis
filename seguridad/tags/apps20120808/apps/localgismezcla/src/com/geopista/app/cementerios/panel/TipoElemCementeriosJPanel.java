package com.geopista.app.cementerios.panel;

import org.apache.log4j.Logger;

import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.app.cementerios.panel.TipoElemCementeriosJPanel;
import com.geopista.app.cementerios.panel.TreeCellRendererTiposElem;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class TipoElemCementeriosJPanel extends javax.swing.JPanel {
	

	private static final long serialVersionUID = 1L;

	Logger logger= Logger.getLogger(TipoElemCementeriosJPanel.class);

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private DefaultMutableTreeNode top;
    private String locale;
    private Object[] seleccion= new Object[2];
    private ArrayList actionListeners= new ArrayList();
    private DomainNode nodoSelected;


    /**
     * Método que genera el panel con el arbol referente al cementerio  para las aplicaciones cliente
     * @param desktop
     * @param locale
     */
    public TipoElemCementeriosJPanel(javax.swing.JFrame desktop, String locale) {
        this.desktop= desktop;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        this.locale= locale;
        
        initComponents();
        renombrarComponentes();
    }

    /**
     * Método llamado por el constructor para inicializar el formulario
     */
    private void initComponents() {
    	
    	//se guarda el top del arbol: Gestión de Cementerios
        top= new DefaultMutableTreeNode(aplicacion.getI18nString("cementerio.epigrafes.tag1"));

        //Se inicializa el arbol de dominios
        initArbol();
        elemCementeriosJTree= new javax.swing.JTree(top);
        elemCementeriosJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        elemCementeriosJTree.setCellRenderer(new TreeCellRendererTiposElem(locale));

        treeJScrollPane= new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        TreeSelectionListener treeSelectionListener= new TreeSelectionListener() {
              public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                  elementsJTreeValueChanged(treeSelectionEvent);
              }
            };
        elemCementeriosJTree.addTreeSelectionListener(treeSelectionListener);


        treeJScrollPane.setViewportView(elemCementeriosJTree);
        add(treeJScrollPane, BorderLayout.CENTER);

    }

    public void elementsJTreeValueChanged(TreeSelectionEvent e) {
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
                /** se trata de un subtipo de elementos de cementerio . Cargamos los elementos existentes para ese tipo y ese subtipo. */
                DefaultMutableTreeNode treeNode= (DefaultMutableTreeNode)node.getParent();
                if (treeNode == null) return;
                Object ninfo= treeNode.getUserObject();
                if (ninfo instanceof DomainNode){
                    setSeleccion(((DomainNode)ninfo).getPatron(), dn.getPatron());
                    nodoSelected= (DomainNode)ninfo;
                    fireActionPerformed();
                }
            }else{
                /** tanto la busqueda como el filtrado parte de la seleccion de un tipo de elemento */
                setSeleccion(/*dn.getPatron()*/null, null);
                fireActionPerformed();
            }
        }else{
            /** seleccionada la raiz del arbol */
            if (arbol.getSelectionPath().getParentPath() == null){
                setSeleccion(null, null);
                fireActionPerformed();
            }

        }

    }

    private void initArbol(){

        if (top == null) return;
        
        //cargarmos las estructuras..
        while (!Estructuras.isCargada()){
            if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
            try {Thread.sleep(500);}catch(Exception e){}
        }

        DefaultMutableTreeNode tipo= null;
        DefaultMutableTreeNode subtipo= null;

        Vector vDomainNodesTipos= Estructuras.getListaTiposSorted(locale);
        Vector vDomainNodesSubtipos = Estructuras.getListaSubtiposSorted(locale);

        if ((vDomainNodesTipos == null) || (vDomainNodesSubtipos == null)) return;
        if ((vDomainNodesTipos != null) && (vDomainNodesSubtipos != null)){
            for (Enumeration e=vDomainNodesTipos.elements();e.hasMoreElements();){
            	 DomainNode dnodeTipo= (DomainNode)e.nextElement();
                tipo= new DefaultMutableTreeNode(dnodeTipo);
                top.add(tipo);
                for (Enumeration e2=vDomainNodesSubtipos.elements();e2.hasMoreElements();){
                    DomainNode dnodeSubtipo= (DomainNode)e2.nextElement();
                   //solo se lo añadimos al nodo domain padre..
                    subtipo= new DefaultMutableTreeNode(dnodeSubtipo);
                    
                	if (dnodeSubtipo.getIdParent().equals(dnodeTipo.getIdNode())){
                		subtipo= new DefaultMutableTreeNode(dnodeSubtipo);
                		tipo.add(subtipo);
                	}
                }
            }
        }
    }


    public void setSeleccion(String tipo, String subtipo){
        seleccion= new Object[2];
        seleccion[0]= tipo;
        seleccion[1]= subtipo;
    }

    public String getTipoSeleccionado(){
        return (String)seleccion[0];
    }

    public String getSubtipoSeleccionado(){
        return (String)seleccion[1];
    }


    public void renombrarComponentes(){
        setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("cementerio.epigrafes.tag1")));
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

    public void setEnabled(boolean b){
    	elemCementeriosJTree.setEnabled(b);

    }
    
    private javax.swing.JTree elemCementeriosJTree;
    private javax.swing.JScrollPane treeJScrollPane;
    
}
