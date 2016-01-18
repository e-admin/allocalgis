package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.inventario.*;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 13-sep-2006
 * Time: 9:11:05
 */
public class JPanelDatosGeneralesDerechos extends javax.swing.JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;
    private javax.swing.JFrame desktop;
    private CuentaContable cuentaContable=null;

    /**
     * Método que genera el panel de los datos Generales para los bienes inmuebles
     * @param locale
     */
    public JPanelDatosGeneralesDerechos(JFrame desktop, String locale) {
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        destinoJLabel = new InventarioLabel();
        destinoJTField= new InventarioTextField(254);
        adquisicionJLabel = new InventarioLabel();
        claseJLabel = new InventarioLabel();
        adquisicionEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseDerechosReales(), null, locale, true);
        fAdquisicionJLabel = new InventarioLabel();
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        jLabelBien = new InventarioLabel();
        jTextFieldBien= new InventarioTextField(254);

        costeAdquisicionJLabel = new InventarioLabel();
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);

        valorActualJLabel = new InventarioLabel();
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);

        descFrutosJLabel = new InventarioLabel();
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);

        valorFrutosJLabel = new InventarioLabel();
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);

        cuentaContableJLabel = new InventarioLabel();
        cuentaContableJTField= new InventarioTextField();
        cuentaContableJButton= new JButton();
        cuentaContableJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        cuentaContableJButton.addActionListener(new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                       abrirDialogo();
                   }
               });

        cuentaContableJTField.setEnabled(false);
        InventarioClient inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
        		Constantes.INVENTARIO_SERVLET_NAME);
        try{initCContableCBox(inventarioClient.getCuentasContables());}catch(Exception ex){}
        patrimonioJCBox = new javax.swing.JCheckBox();



        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, -1));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 310, -1));

        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, -1));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, -1));

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 160, 20));

        JPanel jPanelDescripcion= new JPanel();
        jPanelDescripcion.setBorder(new TitledBorder(aplicacion.getI18nString("inventario.datosGenerales.tag8")));
        jPanelDescripcion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(jPanelDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 450, 300));

        jPanelDescripcion.add(jLabelBien, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 110, -1));
        jPanelDescripcion.add(jTextFieldBien, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 25, 310, -1));

        jPanelDescripcion.add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, -1));
        jPanelDescripcion.add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 200, -1));

        jPanelDescripcion.add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 75, 110, -1));
        jPanelDescripcion.add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 75, 200, -1));

        jPanelDescripcion.add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 250, -1));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        jPanelDescripcion.add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 430, 60));

        jPanelDescripcion.add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 185, 110, -1));
        jPanelDescripcion.add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 185, 160, -1));

        jPanelDescripcion.add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, -1));
        jPanelDescripcion.add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 290, -1));
        jPanelDescripcion.add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 210, 20, 20));
        jPanelDescripcion.add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 310, -1));

        jPanelDescripcion.add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 110, 20));
        jPanelDescripcion.add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, 20));
        
        jPanelDescripcion.add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 380, -1));


    }



    public void setEnabled(boolean b){
        destinoJTField.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        adquisicionEJCBox.setEnabled(b);
        claseEJCBox.setEnabled(b);
        jTextFieldBien.setEnabled(b);
        costeAdquisicionJTField.setEnabled(b);
        valorActualJTField.setEnabled(b);
        descFrutosJTArea.setEnabled(b);
        valorFrutosJTField.setEnabled(b);
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJButton.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
    }

    /**
     * Método que carga en el panel un bien inmueble
     * @param derecho a cargar
     */
    public void load(DerechoRealBean derecho){
        if (derecho==null ) return;
        destinoJTField.setText(derecho.getDestino()!=null?derecho.getDestino():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(derecho.getFechaAdquisicion()));}catch(Exception e){}
        adquisicionEJCBox.setSelectedPatron(derecho.getAdquisicion()!=null?derecho.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(derecho.getClase()!=null?derecho.getClase():"");
        jTextFieldBien.setText(derecho.getBien()!=null?derecho.getBien():"");

        if (derecho.getCosteAdquisicion()!=null && derecho.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(derecho.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (derecho.getValorActual()!=null && derecho.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(derecho.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(derecho.getFrutos()!=null?derecho.getFrutos():"");
        if (derecho.getImporteFrutos()!=null && derecho.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(derecho.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        if ((derecho).getCuentaContable() != null){
            cuentaContableJCBox.setSelected((derecho).getCuentaContable().getId());
        }
        patrimonioJCBox.setSelected(derecho.isPatrimonioMunicipal());
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un bien inmueble
     * @param derecho a actualizar
     */
    public void actualizarDatosGenerales(DerechoRealBean derecho){
        if (derecho==null) return;
        derecho.setDestino(destinoJTField.getText().trim());
        try{derecho.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        derecho.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        derecho.setClase(claseEJCBox.getSelectedPatron());
        derecho.setBien(jTextFieldBien.getText().trim());
        try{derecho.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{derecho.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        derecho.setFrutos(descFrutosJTArea.getText().trim());
        try{derecho.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        derecho.setCuentaContable(cuentaContable);
        derecho.setPatrimonioMunicipal(patrimonioJCBox.isSelected());
    }
    private void initCContableCBox(Collection c){
           cuentaContableJCBox= new com.geopista.app.inventario.CuentasJComboBox(c.toArray(), null, true);
           cuentaContableJCBox.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                   cuentaContableBoxActionPerformed();
               }
           });
       }

    private void cuentaContableBoxActionPerformed(){
            if (cuentaContableJCBox.getSelectedIndex()!=0){
                cuentaContable= (CuentaContable)cuentaContableJCBox.getSelected();
                cuentaContableJTField.setText("");
            }else
                cuentaContable=null;
    }

    private void abrirDialogo() {
        final CuentaJDialog cuentaJDialog= new CuentaJDialog(desktop, aplicacion.getI18nString("inventario.cuentaJDialog.tag4"),
                                             aplicacion.getI18nString("inventario.datosAmortizacion.tag2"), CuentaJDialog.CUENTA_CONTABLE);
        cuentaJDialog.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				cuentaJDialog_actionPerformed(cuentaJDialog);
			}
		});
        cuentaJDialog.setVisible(true);
    }
    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
    }

    private void cuentaJDialog_actionPerformed(CuentaJDialog cuentaJDialog){
            if (cuentaJDialog.getCuenta()!=null) cuentaContableJCBox.setSelectedIndex(0);
            if (cuentaJDialog.getCuenta() instanceof CuentaContable){
                cuentaContable= (CuentaContable)cuentaJDialog.getCuenta();
                cuentaContableJTField.setText(cuentaContable.toString());
            }
       cuentaJDialog.dispose();
    }
    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        claseEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");

    }

    private void renombrarComponentes(){
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag5"));}catch(Exception e){}
        try{claseJLabel.setText(aplicacion.getI18nString("inventario.derechosReales.tag1"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag9"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.datosGenerales.tag18"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag19"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag12"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag13"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag14"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag15"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag16"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
        try{jLabelBien.setText(aplicacion.getI18nString("inventario.datosGenerales.bien"));}catch(Exception e){}

    }

    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel adquisicionJLabel;    
    private ComboBoxEstructuras adquisicionEJCBox;
    private javax.swing.JLabel claseJLabel;
    private ComboBoxEstructuras claseEJCBox;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelBien;
    private com.geopista.app.utilidades.TextField jTextFieldBien;
    private javax.swing.JLabel costeAdquisicionJLabel;
    private com.geopista.app.utilidades.JNumberTextField costeAdquisicionJTField;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;
    private javax.swing.JLabel descFrutosJLabel;
    private javax.swing.JScrollPane descFrutosJScrollPane;
    private TextPane descFrutosJTArea;
    private javax.swing.JLabel valorFrutosJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorFrutosJTField;
    private javax.swing.JLabel cuentaContableJLabel;
    private com.geopista.app.inventario.CuentasJComboBox cuentaContableJCBox;
    private javax.swing.JTextField cuentaContableJTField;
    private JButton cuentaContableJButton;
    private javax.swing.JCheckBox patrimonioJCBox;

}