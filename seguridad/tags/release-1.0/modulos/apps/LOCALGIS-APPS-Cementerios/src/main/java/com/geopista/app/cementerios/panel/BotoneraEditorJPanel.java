/*
 * BotoneraJPanel.java
 *
 * Created on 7 de julio de 2006, 9:48
 */

package com.geopista.app.cementerios.panel;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.panel.BotoneraEditorJPanel;

import javax.swing.*;


public class BotoneraEditorJPanel extends javax.swing.JPanel {
    Logger logger= Logger.getLogger(BotoneraEditorJPanel.class);

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;

    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;


    /**
     * Método que genera un panel con la botonera que se muestra en el modulo de inventario para el Editor de Cartografia
     * @param desktop
     */
    public BotoneraEditorJPanel(javax.swing.JFrame desktop) {
        this.desktop= desktop;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        annadirJButton = new javax.swing.JButton();
        modificarJButton = new javax.swing.JButton();
        anexarJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(FlowLayout.CENTER));

        annadirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirJButtonActionPerformed();
            }
        });
        add(annadirJButton);

        modificarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarJButtonActionPerformed();
            }
        });
        add(modificarJButton);

        anexarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anexarJButtonActionPerformed();
            }
        });
        add(anexarJButton);

        eliminarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarJButtonActionPerformed();
            }
        });
        add(eliminarJButton);

    }

    private void eliminarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ELIMINAR;
        fireActionPerformed();
    }

    private void anexarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ANEXAR;
        fireActionPerformed();
    }

    private void annadirJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ANNADIR;
        fireActionPerformed();
    }

    public boolean modificarJButtonActionPerformed() {
    	if (modificarJButton.isEnabled()){
    		botonPressed= Constantes.OPERACION_MODIFICAR;
    		fireActionPerformed();
    		return true;
    	}
    	return false;
    }


    public void renombrarComponentes(){
        try{annadirJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag1"));}catch(Exception e){}
        try{modificarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag2"));}catch(Exception e){}
        try{anexarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag5"));}catch(Exception e){}
        try{eliminarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag4"));}catch(Exception e){}
    }


    public void setEnabled(boolean b){
        annadirJButton.setEnabled(b);
        modificarJButton.setEnabled(b);
        anexarJButton.setEnabled(b);
        eliminarJButton.setEnabled(b);
    }

    public void annadirJButtonSetEnabled(boolean b){
       annadirJButton.setEnabled(b); 
    }

    public void eliminarJButtonSetEnabled(boolean b){
        eliminarJButton.setEnabled(b); 
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

    public String getBotonPressed(){
        return botonPressed;
    }

    public void setBotonPressed(String s){
        botonPressed= s;
    }

    private javax.swing.JButton annadirJButton;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JButton modificarJButton;
    private javax.swing.JButton anexarJButton;
    
}
