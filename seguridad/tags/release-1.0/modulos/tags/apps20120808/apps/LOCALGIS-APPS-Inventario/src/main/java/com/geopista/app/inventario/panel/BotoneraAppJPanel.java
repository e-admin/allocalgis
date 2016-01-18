package com.geopista.app.inventario.panel;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 31-ago-2006
 * Time: 15:44:36
 * To change this template use File | Settings | File Templates.
 */
public class BotoneraAppJPanel  extends JPanel{

    Logger logger= Logger.getLogger(BotoneraEditorJPanel.class);

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;

    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;


    /**
     * Método que genera un panel con la botonera que se muestra en el modulo de inventario para las aplicaciones cliente
     * @param desktop
     */
    public BotoneraAppJPanel(javax.swing.JFrame desktop){
        this.desktop= desktop;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        consultarJButton = new javax.swing.JButton();
        buscarJButton = new javax.swing.JButton();
        filtrarJButton = new javax.swing.JButton();
        informesJButton = new javax.swing.JButton();
        recuperarJButton = new javax.swing.JButton();

        //setLayout(new java.awt.FlowLayout(0, 0, 0));
        setLayout(new java.awt.FlowLayout());

        consultarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarJButtonActionPerformed();
            }
        });
        add(consultarJButton);

        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });
        add(buscarJButton);

        filtrarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtrarJButtonActionPerformed();
            }
        });
        add(filtrarJButton);

        informesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informesJButtonActionPerformed();
            }
        });
        add(informesJButton);


        recuperarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recuperarJButtonActionPerformed();
            }
        });
        add(recuperarJButton);
        this.setEnabledRecuperarJButton(false);
    }

    public boolean consultarJButtonActionPerformed() {
    	if (consultarJButton.isEnabled()){
    		botonPressed= Constantes.OPERACION_CONSULTAR;
    		fireActionPerformed();
    		return true;
    	}
    	return false;
    }

    private void buscarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_BUSCAR;
        fireActionPerformed();
    }

    private void filtrarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_FILTRAR;
        fireActionPerformed();
    }

    private void informesJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_INFORMES;
        fireActionPerformed();
    }


    private void recuperarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_RECUPERAR;
        fireActionPerformed();
    }

    public void renombrarComponentes(){
        try{consultarJButton.setText(aplicacion.getI18nString("inventario.botoneraApp.tag1"));}catch(Exception e){}
        try{buscarJButton.setText(aplicacion.getI18nString("inventario.botoneraApp.tag2"));}catch(Exception e){}
        try{filtrarJButton.setText(aplicacion.getI18nString("inventario.botoneraApp.tag3"));}catch(Exception e){}
        try{informesJButton.setText(aplicacion.getI18nString("inventario.botoneraApp.tag4"));}catch(Exception e){}
        try{recuperarJButton.setText(aplicacion.getI18nString("inventario.botoneraApp.tag5"));}catch(Exception e){}
    }

    public void renombrarFiltrarJButton(boolean b){
        filtrarJButton.setIcon(b?com.geopista.app.inventario.UtilidadesComponentes.iconoOK:null);
    }

    public void setEnabled(boolean b){
        recuperarJButton.setEnabled(b);
        consultarJButton.setEnabled(b);
        buscarJButton.setEnabled(b);
        filtrarJButton.setEnabled(b);
        informesJButton.setEnabled(b);
    }

    public void setEnabledBuscarFiltrarInf(){
        buscarJButton.setEnabled(true);
        filtrarJButton.setEnabled(true);
        informesJButton.setEnabled(true);
    }

    public void setEnabledRecuperarJButton(boolean flag){
        recuperarJButton.setEnabled(flag);
    }

    public void buscarJButtonSetEnabled(boolean b){
        buscarJButton.setEnabled(b);
    }

    public void filtrarJButtonSetEnabled(boolean b){
        filtrarJButton.setEnabled(b);
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

    private javax.swing.JButton consultarJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton filtrarJButton;
    private javax.swing.JButton informesJButton;
    private javax.swing.JButton recuperarJButton;


}
