package com.geopista.app.inventario.panel;

import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.AppContext;
import com.geopista.protocol.inventario.InmuebleBean;

import javax.swing.*;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-ago-2006
 * Time: 10:13:09
 * To change this template use File | Settings | File Templates.
 */
public class DatosFrutosJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;

    /**
     * Método que genera el panel de los Datos Frutos de un bien inmueble
     */
    public DatosFrutosJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents(){
        derechosRealesFavorJLabel= new InventarioLabel();
        derechosRealesContraJLabel= new InventarioLabel();
        derechosPersonalesJLabel= new InventarioLabel();
        importeDerechosFavorJLabel= new InventarioLabel();
        importeDerechosContraJLabel= new InventarioLabel();
        frutosJLabel= new InventarioLabel();
        importeFrutosJLabel= new InventarioLabel();

        derechosRealesFavorJScrollPane= new javax.swing.JScrollPane();
        derechosRealesFavorJTArea= new InventarioTextPane(254);

        derechosRealesContraJScrollPane= new javax.swing.JScrollPane();
        derechosRealesContraJTArea= new InventarioTextPane(254);

        derechosPersonalesJScrollPane= new javax.swing.JScrollPane();
        derechosPersonalesJTArea= new InventarioTextPane(254);

        importeDerechosFavorJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeDerechosFavorJTField.setSignAllowed(false);

        importeDerechosContraJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeDerechosContraJTField.setSignAllowed(false);

        frutosJScrollPane= new javax.swing.JScrollPane();
        frutosJTArea= new InventarioTextPane(254);

        importeFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeFrutosJTField.setSignAllowed(false);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(derechosRealesFavorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 300, 20));
        add(derechosRealesContraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 20));
        add(derechosPersonalesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 300, 20));

        add(importeDerechosFavorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 150, 20));
        add(importeDerechosContraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 150, 20));
        add(frutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 300, 20));
        add(importeFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 150, 20));

        derechosRealesFavorJScrollPane.setViewportView(derechosRealesFavorJTArea);
        add(derechosRealesFavorJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 430, 40));
        derechosRealesContraJScrollPane.setViewportView(derechosRealesContraJTArea);
        add(derechosRealesContraJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 430, 40));
        derechosPersonalesJScrollPane.setViewportView(derechosPersonalesJTArea);
        add(derechosPersonalesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 430, 40));

        add(importeDerechosFavorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 160, -1));
        add(importeDerechosContraJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 160, -1));
        add(frutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 430, 40));
        frutosJScrollPane.setViewportView(frutosJTArea);
        add(importeFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 160, -1));

    }

    public void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosFrutos.tag1")));}catch(Exception e){}
        try{derechosRealesFavorJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag2"));}catch(Exception e){}
        try{derechosRealesContraJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag3"));}catch(Exception e){}
        try{derechosPersonalesJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag4"));}catch(Exception e){}
        try{importeDerechosFavorJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag5"));}catch(Exception e){}
        try{importeDerechosContraJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag6"));}catch(Exception e){}
        try{frutosJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag7"));}catch(Exception e){}
        try{importeFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosFrutos.tag8"));}catch(Exception e){}
    }

    public void load(Object obj){
        if (obj instanceof InmuebleBean){
            derechosRealesFavorJTArea.setText(((InmuebleBean)obj).getDerechosRealesFavor()!=null?((InmuebleBean)obj).getDerechosRealesFavor():"");
            derechosRealesContraJTArea.setText(((InmuebleBean)obj).getDerechosRealesContra()!=null?((InmuebleBean)obj).getDerechosRealesContra():"");
            derechosPersonalesJTArea.setText(((InmuebleBean)obj).getDerechosPersonales()!=null?((InmuebleBean)obj).getDerechosPersonales():"");
            if (((InmuebleBean)obj).getValorDerechosFavor()!=-1)
                try{importeDerechosFavorJTField.setNumber(new Double(((InmuebleBean)obj).getValorDerechosFavor()));}catch(Exception e){}
            else importeDerechosFavorJTField.setText("");
            if (((InmuebleBean)obj).getValorDerechosContra()!=-1)
                try{importeDerechosContraJTField.setNumber(new Double(((InmuebleBean)obj).getValorDerechosContra()));}catch(Exception e){}
            else importeDerechosContraJTField.setText("");
            frutosJTArea.setText(((InmuebleBean)obj).getFrutos()!=null?((InmuebleBean)obj).getFrutos():"");
            if (((InmuebleBean)obj).getImporteFrutos()!=null && ((InmuebleBean)obj).getImporteFrutos()!=-1)
                try{importeFrutosJTField.setNumber(new Double(((InmuebleBean)obj).getImporteFrutos()));}catch(Exception e){}
            else importeFrutosJTField.setText("");
        }
    }

    public void actualizarDatos(Object obj){
        if (obj instanceof InmuebleBean){
            ((InmuebleBean)obj).setDerechosRealesFavor(derechosRealesFavorJTArea.getText().trim());
            ((InmuebleBean)obj).setDerechosRealesContra(derechosRealesContraJTArea.getText().trim());
            ((InmuebleBean)obj).setDerechosPersonales(derechosPersonalesJTArea.getText().trim());
            try{((InmuebleBean)obj).setValorDerechosFavor(((Double)importeDerechosFavorJTField.getNumber()).doubleValue());}catch(Exception e){}
            try{((InmuebleBean)obj).setValorDerechosContra(((Double)importeDerechosContraJTField.getNumber()).doubleValue());}catch(Exception e){}
            ((InmuebleBean)obj).setFrutos(frutosJTArea.getText().trim());
            try{((InmuebleBean)obj).setImporteFrutos(((Double)importeFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        }
    }

    public void setEnabled(boolean b){
        derechosRealesFavorJTArea.setEnabled(b);
        derechosRealesContraJTArea.setEnabled(b);
        derechosPersonalesJTArea.setEnabled(b);
        importeDerechosFavorJTField.setEnabled(b);
        importeDerechosContraJTField.setEnabled(b);
        frutosJTArea.setEnabled(b);
        importeFrutosJTField.setEnabled(b);
    }

    public void clear(){
        derechosRealesFavorJTArea.setText("");
        derechosRealesContraJTArea.setText("");
        derechosPersonalesJTArea.setText("");
        importeDerechosFavorJTField.setText("");
        importeDerechosContraJTField.setText("");
        frutosJTArea.setText("");
        importeFrutosJTField.setText("");
    }



    private javax.swing.JLabel derechosRealesFavorJLabel;
    private javax.swing.JScrollPane derechosRealesFavorJScrollPane;
    private TextPane derechosRealesFavorJTArea;

    private javax.swing.JLabel derechosRealesContraJLabel;
    private javax.swing.JScrollPane derechosRealesContraJScrollPane;
    private TextPane derechosRealesContraJTArea;

    private javax.swing.JLabel derechosPersonalesJLabel;
    private javax.swing.JScrollPane derechosPersonalesJScrollPane;
    private TextPane derechosPersonalesJTArea;

    private javax.swing.JLabel importeDerechosFavorJLabel;
    private com.geopista.app.utilidades.JNumberTextField importeDerechosFavorJTField;
    private javax.swing.JLabel importeDerechosContraJLabel;
    private com.geopista.app.utilidades.JNumberTextField importeDerechosContraJTField;

    private javax.swing.JLabel frutosJLabel;
    private javax.swing.JScrollPane frutosJScrollPane;
    private TextPane frutosJTArea;

    private javax.swing.JLabel importeFrutosJLabel;
    private com.geopista.app.utilidades.JNumberTextField importeFrutosJTField;



}
