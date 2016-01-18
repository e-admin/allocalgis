package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 31-jul-2006
 * Time: 11:53:44
 * To change this template use File | Settings | File Templates.
 */
public class CuentaJDialog extends JDialog{
    private AppContext aplicacion;

    private Object cuenta;
    private int tipo;
    private ArrayList actionListeners= new ArrayList();

    public static int CUENTA_CONTABLE=1;
    public static int CUENTA_AMORTIZACION=2;

    /**
     * Método que genera el dialogo de insercción de número de cuenta
     * @param title del dialogo
     * @param labeltext
     */
    public CuentaJDialog(JFrame desktop, String title, String labeltext, int tipo) {
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.tipo= tipo;
        initComponents();
        renombrarComponentes(title, labeltext);
    }


    private void initComponents() {
        valorJLabel = new javax.swing.JLabel();
        descripcionJLabel = new javax.swing.JLabel();
        descripcionJTField= new com.geopista.app.utilidades.TextField(100);
        valorIdentidadBancariaJTField= new com.geopista.app.utilidades.TextField(4, true);
        valorOficinaJTField= new com.geopista.app.utilidades.TextField(4, true);
        valorDCJTField= new com.geopista.app.utilidades.TextField(2, true);
        valorCCJTField=  new com.geopista.app.utilidades.TextField(10, true);
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
        getContentPane().add(valorIdentidadBancariaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 60, -1));
        getContentPane().add(valorOficinaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 60, -1));
        getContentPane().add(valorDCJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 30, -1));
        getContentPane().add(valorCCJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 150, -1));
        getContentPane().add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        getContentPane().add(descripcionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 330, 20));

        getContentPane().add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, -1, -1));
        getContentPane().add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        setSize(490, 170);
       // setLocation(200, 140);
        GUIUtil.centreOnWindow(this);

    }

    private void renombrarComponentes(String title, String labeltext){
        setTitle(title);
        valorJLabel.setText(labeltext);
        try{descripcionJLabel.setText(aplicacion.getI18nString("inventario.cuentaJDialog.tag1"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.cuentaJDialog.tag2"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.cuentaJDialog.tag3"));}catch(Exception e){}

    }

    private void cancelarJButtonActionPerformed(){
        cuenta= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        if (valorIdentidadBancariaJTField.getText().trim().equalsIgnoreCase("") ||
            valorOficinaJTField.getText().trim().equalsIgnoreCase("") ||
            valorDCJTField.getText().trim().equalsIgnoreCase("") ||
            valorCCJTField.getText().trim().equalsIgnoreCase("") ) cuenta= null;
        else{
            String numcuenta= valorIdentidadBancariaJTField.getText().trim()+" "+valorOficinaJTField.getText().trim()+" "+valorDCJTField.getText().trim()+" "+valorCCJTField.getText().trim();
            if (tipo==CUENTA_CONTABLE){
                CuentaContable cc= new CuentaContable();
                cc.setCuenta(numcuenta);
                cc.setDescripcion(descripcionJTField.getText().trim());
                cuenta= cc;
            }else if (tipo==CUENTA_AMORTIZACION){
                CuentaAmortizacion ca= new CuentaAmortizacion();
                ca.setCuenta(numcuenta);
                ca.setDescripcion(descripcionJTField.getText().trim());
                cuenta= ca;
            }
        }
        fireActionPerformed();
    }

    public Object getCuenta(){
        return cuenta;
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
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JTextField valorIdentidadBancariaJTField;
    private javax.swing.JTextField valorOficinaJTField;
    private javax.swing.JTextField valorDCJTField;
    private javax.swing.JTextField valorCCJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JTextField descripcionJTField;



}
