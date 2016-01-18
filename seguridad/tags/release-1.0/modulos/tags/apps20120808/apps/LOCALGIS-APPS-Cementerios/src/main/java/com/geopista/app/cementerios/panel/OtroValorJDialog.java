package com.geopista.app.cementerios.panel;

import com.geopista.app.AppContext;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Iterator;
import java.util.ArrayList;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-jul-2006
 * Time: 9:25:57
 * To change this template use File | Settings | File Templates.
 */
public class OtroValorJDialog extends javax.swing.JDialog{
    private AppContext aplicacion;

    private String valor;
    private ArrayList actionListeners= new ArrayList();

    /**
     * Método que genera el dialogo de inserccion de otro valor
     * @param title del dialogo
     * @param labeltext
     */
    public OtroValorJDialog(JFrame desktop, String title, String labeltext) {
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes(title, labeltext);
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

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setModal(true);

        getContentPane().add(valorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 20));
        getContentPane().add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 310, -1));
        getContentPane().add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));
        getContentPane().add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        setSize(470, 150);
        setLocation(200, 140);

    }

    private void renombrarComponentes(String title, String labeltext){
        setTitle(title);
        valorJLabel.setText(labeltext);
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.otroValorJDialog.tag1"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.otroValorJDialog.tag2"));}catch(Exception e){}

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
