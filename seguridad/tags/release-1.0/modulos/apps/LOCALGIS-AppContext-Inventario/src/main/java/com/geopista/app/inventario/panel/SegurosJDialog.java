package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.CompanniaSeguros;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 02-ago-2006
 * Time: 14:46:48
 * To change this template use File | Settings | File Templates.
 */
public class SegurosJDialog extends JDialog{
    private AppContext aplicacion;

    private CompanniaSeguros compannia;
    private ArrayList actionListeners= new ArrayList();
    private ListaPane listaPane;

    private InventarioClient inventarioClient = null;

    /**
     * Método que genera el dialogo de inserccion de otro valor
     */
    public SegurosJDialog() throws Exception{
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{

        inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);

        listaPane= new ListaPane(inventarioClient.getCompanniasSeguros());
        listaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        aceptarJButton= new javax.swing.JButton();
        cancelarJButton= new javax.swing.JButton();
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        setModal(true);
        getContentPane().setLayout(new java.awt.BorderLayout());
        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout());
        botoneraJPanel.add(aceptarJButton);
        botoneraJPanel.add(cancelarJButton);
        getContentPane().add(listaPane, BorderLayout.CENTER);
        getContentPane().add(botoneraJPanel, BorderLayout.SOUTH);

        setSize(470, 500);
        setLocation(200, 140);
        //GUIUtil.centreOnWindow(this);
    }

    private void listaPaneActionPerformed(){
        compannia= (CompanniaSeguros)listaPane.getSelected();
    }

    private void renombrarComponentes(){
        String title= "";
        try{
            title= aplicacion.getI18nString("inventario.listaSeguros.tag1");
        }catch(Exception e){};

        listaPane.renombrar(title);
        try{setTitle(aplicacion.getI18nString("inventario.segurosJDialog.tag1"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.segurosJDialog.tag2"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.segurosJDialog.tag3"));}catch(Exception e){}
    }

    private void cancelarJButtonActionPerformed(){
        compannia= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        compannia= (CompanniaSeguros)listaPane.getSelected();
        fireActionPerformed();
    }

    public CompanniaSeguros getCompannia(){
        return compannia;
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }


    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;




}
