/*
 * BotoneraJPanel.java
 *
 * Created on 7 de julio de 2006, 9:48
 */

package com.geopista.app.inventario.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;

/**
 *
 * @author  charo
 */
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
        borrarJButton = new javax.swing.JButton();
        modificarJButton = new javax.swing.JButton();
        anexarJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();
        asociarFeaturesABienesJButton = new javax.swing.JButton();


        //setLayout(new java.awt.FlowLayout(0, 0, 0));
        setLayout(new java.awt.FlowLayout());

        annadirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirJButtonActionPerformed();
            }
        });
        add(annadirJButton);

        borrarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarJButtonActionPerformed();
            }
        });
        add(borrarJButton);

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
        
        asociarFeaturesABienesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	asociarFeaturesABienesJButtonActionPerformed();
            }
        });
        add(asociarFeaturesABienesJButton);

    }

    
    protected void asociarFeaturesABienesJButtonActionPerformed() {
    	botonPressed= Constantes.OPERACION_ASOCIAR_FEATURES;
        fireActionPerformed();
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

    private void borrarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_BORRAR;
        fireActionPerformed();
    }

    public void renombrarComponentes(){
        try{annadirJButton.setText(aplicacion.getI18nString("inventario.botonera.tag1"));}catch(Exception e){}
        try{borrarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag3"));}catch(Exception e){}
        try{modificarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag2"));}catch(Exception e){}
        try{anexarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag5"));}catch(Exception e){}
        try{eliminarJButton.setText(aplicacion.getI18nString("inventario.botonera.tag4"));}catch(Exception e){}
        try{asociarFeaturesABienesJButton.setText(aplicacion.getI18nString("inventario.botonera.tag8"));}catch(Exception e){}
    }

    public void setEnabledModificacion(){
    	annadirJButton.setEnabled(true);
        modificarJButton.setEnabled(true);
        eliminarJButton.setEnabled(true);
        asociarFeaturesABienesJButton.setEnabled(true);
     }
    public void setEnabled(boolean b){
        annadirJButton.setEnabled(b);
        borrarJButton.setEnabled(b);
        modificarJButton.setEnabled(b);
        anexarJButton.setEnabled(b);
        eliminarJButton.setEnabled(b);
        asociarFeaturesABienesJButton.setEnabled(b);
    }

    public void annadirJButtonSetEnabled(boolean b){
       annadirJButton.setEnabled(b); 
    }

    public void eliminarJButtonSetEnabled(boolean b){
        eliminarJButton.setEnabled(b);
     }
    
    public void asociarFeaturesABienesJButtonSetEnabled(boolean b) {
		asociarFeaturesABienesJButton.setEnabled(b);
		
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
    private javax.swing.JButton borrarJButton;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JButton modificarJButton;
    private javax.swing.JButton anexarJButton;
    private javax.swing.JButton asociarFeaturesABienesJButton;


	 
}

