/*
 * BotoneraAceptarCancelarJPanel.java
 *
 * Created on 13 de julio de 2006, 9:12
 */

package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;

import java.util.Iterator;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * @author  charo
 */
public class BotoneraAceptarCancelarJPanel extends javax.swing.JPanel {
    private AppContext aplicacion;

    private boolean aceptarPressed= false;
    private ArrayList actionListeners= new ArrayList();


    /**
     * Método que genera un panel con una botonera Aceptar/Cancelar
     */
    public BotoneraAceptarCancelarJPanel() {
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }
    
    /**
     * Método que inicializa el panel
     */
    private void initComponents() {
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();

        //setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setLayout(new java.awt.FlowLayout());

        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });

        //add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));
        add(aceptarJButton);

        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        //add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, -1));
        add(cancelarJButton);

    }

    private void cancelarJButtonActionPerformed() {
        aceptarPressed= false;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed() {
        aceptarPressed= true;
        fireActionPerformed();
    }

    /**
     * @return true si el boton de Aceptar ha sido pulsado
     * @return false en caso contrario
     */
    public boolean aceptarPressed() {
        return aceptarPressed;
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

    /**
     * Método que renombra los componenetes del panel segun el locale
     */
    private void renombrarComponentes(){
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag6"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag7"));}catch(Exception e){}
   }

    public void setEnabled(boolean b){
        aceptarJButton.setEnabled(b);
        cancelarJButton.setEnabled(b);
    }

    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;

}
