package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
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
 * Date: 02-oct-2006
 * Time: 9:16:25
 * To change this template use File | Settings | File Templates.
 */
public class BuscarJDialog extends JDialog{
    private AppContext aplicacion;

    private String valor;
    private ArrayList actionListeners= new ArrayList();

    /**
     * Método que genera el dialogo de busqueda
     */
    public BuscarJDialog(JFrame desktop) {
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
        addAyudaOnline();
    }

    private void initComponents() {
        valorJLabel = new javax.swing.JLabel();
        valorJTField = new com.geopista.app.utilidades.TextField(254);
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

        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        JPanel buscarJPanel= new JPanel();
        buscarJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        buscarJPanel.add(valorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 320, 20));
        buscarJPanel.add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 320, -1));

        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout());
        botoneraJPanel.add(aceptarJButton);
        botoneraJPanel.add(cancelarJButton);

        getContentPane().add(buscarJPanel, BorderLayout.CENTER);
        getContentPane().add(botoneraJPanel, BorderLayout.SOUTH);

        setSize(350, 150);
       // setLocation(200, 140);
        GUIUtil.centreOnWindow(this);

    }

    /**
     * Ayuda Online
     *
     */
    private void addAyudaOnline() {
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("F1"), "action F1");

        getRootPane().getActionMap().put("action F1", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                String uriRelativa = "/Geocuenca:Inventario:Buscar";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }    

    private void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("inventario.buscar.tag1"));}catch(Exception e){}
        try{valorJLabel.setText(aplicacion.getI18nString("inventario.buscar.tag2"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.buscar.tag3"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.buscar.tag4"));}catch(Exception e){}

    }


    private void cancelarJButtonActionPerformed(){
        valor= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        if (valorJTField.getText().trim().equalsIgnoreCase("")) valor= null;
        else valor= valorJTField.getText().trim();
        fireActionPerformed();
    }

    public String getValor(){
        return valor;
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


    private javax.swing.JLabel valorJLabel;
    private javax.swing.JTextField valorJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;



}
