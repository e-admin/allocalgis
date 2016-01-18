/**
 * DatosGeneralesValorMobiliarioJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.ValorMobiliarioBean;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 20-sep-2006
 * Time: 13:10:49
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesValorMobiliarioJPanel extends JPanel{
    private AppContext aplicacion;
    private String locale;
    private javax.swing.JFrame desktop;

    private CuentaJDialog cuentaJDialog;
    private CuentaContable cuentaContable;
    private InventarioClient inventarioClient= null;


    /**
     * Método que genera el panel de los datos Generales para los valores mobiliarios
     * @param locale
     */
    public DatosGeneralesValorMobiliarioJPanel(JFrame desktop, String locale) throws Exception{
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        emitidoPorJLabel = new InventarioLabel();
        depositadoEnJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        claseJLabel = new InventarioLabel();
        numeroJLabel = new InventarioLabel();
        serieJLabel = new InventarioLabel();
        costeAdquisicionJLabel = new InventarioLabel();
        numTitulosJLabel = new InventarioLabel();
        descFrutosJLabel = new InventarioLabel();
        valorFrutosJLabel = new InventarioLabel();
        cuentaContableJLabel = new InventarioLabel();
        capitalJLabel = new InventarioLabel();
        precioJLabel = new InventarioLabel();
        fAcuerdoJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();

        inventarioClient= new InventarioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		Constantes.INVENTARIO_SERVLET_NAME);


        initCContableCBox(inventarioClient.getCuentasContables());
        cuentaContableJTField= new JTextField();
        cuentaContableJButton= new JButton();
        cuentaContableJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        cuentaContableJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed(CuentaJDialog.CUENTA_CONTABLE);
            }
        });

        cuentaContableJTField.setEnabled(false);

        patrimonioJCBox = new javax.swing.JCheckBox();
        adquisicionEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        destinoJTField= new InventarioTextField(254);
        emitidoPorJTField= new InventarioTextField(254);
        depositadoEnJTField= new InventarioTextField(254);
        claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClasesValorMobiliario(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        numeroJTField= new InventarioTextField(24);
        serieJTField= new InventarioTextField(254);
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);
        precioJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        precioJTField.setSignAllowed(false);
        capitalJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        capitalJTField.setSignAllowed(false);
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);
        numTitulosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(99999999), true, 2);
        numTitulosJTField.setSignAllowed(false);
        fAcuerdoJTField = new JFormattedTextField(Constantes.df);
        fAcuerdoJButton= new CalendarButton(fAcuerdoJTField);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 110, 20));
        add(depositadoEnJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 110, 20));
        add(emitidoPorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 110, 20));
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 110, 20));
        add(numeroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 110, 20));
        add(serieJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 110, 20));
        add(numTitulosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 110, 20));
        add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 110, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 110, 20));
        add(precioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 110, 20));
        add(capitalJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 110, 20));
        add(fAcuerdoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 110, 20));
        add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 310, 20));
        add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 110, 20));
        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 110, 20));


        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 160, -1));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 20, -1));
        add(depositadoEnJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 310, -1));
        add(emitidoPorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 310, -1));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 310, -1));
        add(numeroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 160, -1));
        add(serieJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 310, -1));
        add(numTitulosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 160, -1));
        add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 160, -1));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 160, -1));
        add(precioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 160, -1));
        add(capitalJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 160, -1));
        add(fAcuerdoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 140, -1));
        add(fAcuerdoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 20, -1));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 430, 40));
        add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 160, -1));
        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 290, -1));
        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 360, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 310, -1));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 400, 310, -1));

        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 380, -1));

    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        depositadoEnJTField.setEnabled(b);
        emitidoPorJTField.setEnabled(b);
        claseEJCBox.setEnabled(b);
        numeroJTField.setEnabled(b);
        serieJTField.setEnabled(b);
        costeAdquisicionJTField.setEnabled(b);
        precioJTField.setEnabled(b);
        capitalJTField.setEnabled(b);
        numTitulosJTField.setEnabled(b);
        descFrutosJTArea.setEnabled(b);
        valorFrutosJTField.setEnabled(b);
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJButton.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
        fAcuerdoJTField.setEnabled(false);
        fAcuerdoJButton.setEnabled(b);
        valorActualJTField.setEnabled(b);

    }

    /**
     * Método que carga en el panel un valor mobiliario
     * @param valor mobiliario cargar
     */
    public void load(ValorMobiliarioBean valor){
        if (valor==null) return;
        adquisicionEJCBox.setSelectedPatron(valor.getAdquisicion()!=null?valor.getAdquisicion():"");
        emitidoPorJTField.setText(valor.getEmitidoPor()!=null?valor.getEmitidoPor():"");
        depositadoEnJTField.setText(valor.getDepositadoEn()!=null?valor.getDepositadoEn():"");
        claseEJCBox.setSelectedPatron(valor.getClase()!=null?valor.getClase():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(valor.getFechaAdquisicion()));}catch(Exception e){}
        numeroJTField.setText(valor.getNumero()!=null?valor.getNumero():"");
        serieJTField.setText(valor.getSerie()!=null?valor.getSerie():"");
        if (valor.getCosteAdquisicion()!=null && valor.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(valor.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (valor.getValorActual()!=null && valor.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(valor.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(valor.getFrutos()!=null?valor.getFrutos():"");
        if (valor.getImporteFrutos()!=null && valor.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(valor.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        if (valor.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(valor.getCuentaContable().getId());
        }
        if (valor.getPrecio()!=null && valor.getPrecio().doubleValue()!=-1)
            try{precioJTField.setNumber(new Double(valor.getPrecio()));}catch(Exception e){}
        else precioJTField.setText("");
        if (valor.getCapital()!=null && valor.getCapital().doubleValue()!=-1)
            try{capitalJTField.setNumber(new Double(valor.getCapital()));}catch(Exception e){}
        else capitalJTField.setText("");
        if (valor.getNumTitulos()!=null && valor.getNumTitulos().intValue()!=-1)
            numTitulosJTField.setNumber(new Integer(valor.getNumTitulos()));
        else numTitulosJTField.setText("");
        destinoJTField.setText(valor.getDestino()!=null?valor.getDestino():"");
        patrimonioJCBox.setSelected(valor.getPatrimonioMunicipalSuelo()?true:false);
        try{fAcuerdoJTField.setText(Constantes.df.format(valor.getFechaAcuerdo()));}catch(Exception e){}
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un valor mobiliario
     * @param valor a actualizar
     */
    public void actualizarDatosGenerales(ValorMobiliarioBean valor){
        if (valor==null) return;
        valor.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        try{valor.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        valor.setDestino(destinoJTField.getText().trim());
        valor.setClase(claseEJCBox.getSelectedPatron());
        valor.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        valor.setEmitidoPor(emitidoPorJTField.getText().trim());
        valor.setDepositadoEn(depositadoEnJTField.getText().trim());
        valor.setNumero(numeroJTField.getText().trim());
        valor.setSerie(serieJTField.getText().trim());
        try{valor.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{valor.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        valor.setFrutos(descFrutosJTArea.getText().trim());
        try{valor.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        valor.setCuentaContable(cuentaContable);
        try{valor.setPrecio(((Double)precioJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{valor.setCapital(((Double)capitalJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{valor.setNumTitulos(numTitulosJTField.getNumber().intValue());}catch(Exception e){}
        try{valor.setFechaAcuerdo(Constantes.df.parse(fAcuerdoJTField.getText().trim()));}catch(java.text.ParseException e){}
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        claseEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        depositadoEnJTField.setText("");
        emitidoPorJTField.setText("");
        numeroJTField.setText("");
        serieJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");
        numTitulosJTField.setText("");
        precioJTField.setText("");
        capitalJTField.setText("");
        fAcuerdoJTField.setText("");
    }

    private void initCContableCBox(Collection c){
        cuentaContableJCBox= new com.geopista.app.inventario.CuentasJComboBox(c.toArray(), null, true);
        cuentaContableJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuentaContableBoxActionPerformed(CuentaJDialog.CUENTA_CONTABLE);
            }
        });
    }

    private void cuentaContableBoxActionPerformed(int i){
        if (i==CuentaJDialog.CUENTA_CONTABLE){
            if (cuentaContableJCBox.getSelectedIndex()!=0){
                cuentaContable= (CuentaContable)cuentaContableJCBox.getSelected();
                cuentaContableJTField.setText("");
            }else
                cuentaContable=null;

        }
    }

    private void abrirDialogoJButtonActionPerformed(final int tipo) {
        if (tipo==CuentaJDialog.CUENTA_CONTABLE){
            cuentaJDialog= new CuentaJDialog(desktop, aplicacion.getI18nString("inventario.cuentaJDialog.tag4"),
                                             aplicacion.getI18nString("inventario.datosAmortizacion.tag2"), CuentaJDialog.CUENTA_CONTABLE);
        }

        cuentaJDialog.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				cuentaJDialog_actionPerformed(tipo);
			}
		});
        cuentaJDialog.show();
    }


    private void cuentaJDialog_actionPerformed(int tipo){
        if (tipo==CuentaJDialog.CUENTA_CONTABLE){
            if (cuentaJDialog.getCuenta()!=null) cuentaContableJCBox.setSelectedIndex(0);
            if (cuentaJDialog.getCuenta() instanceof CuentaContable){
                cuentaContable= (CuentaContable)cuentaJDialog.getCuenta();
                cuentaContableJTField.setText(cuentaContable.getCuenta());
            }
        }
       cuentaJDialog.dispose();
    }





    private void renombrarComponentes(){
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag3"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag1"));}catch(Exception e){}
        try{claseJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag5"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag17"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag2"));}catch(Exception e){}
        try{depositadoEnJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag6"));}catch(Exception e){}
        try{emitidoPorJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag4"));}catch(Exception e){}
        try{numeroJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag7"));}catch(Exception e){}
        try{serieJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag8"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag12"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag13"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag14"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag15"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag16"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
        try{precioJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag9"));}catch(Exception e){}
        try{capitalJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag10"));}catch(Exception e){}
        try{numTitulosJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag18"));}catch(Exception e){}
        try{fAcuerdoJLabel.setText(aplicacion.getI18nString("inventario.datosValorMobiliario.tag11"));}catch(Exception e){}
    }
    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
    }
    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel depositadoEnJLabel;
    private com.geopista.app.utilidades.TextField depositadoEnJTField;
    private javax.swing.JLabel emitidoPorJLabel;
    private com.geopista.app.utilidades.TextField emitidoPorJTField;
    private javax.swing.JLabel claseJLabel;
    private ComboBoxEstructuras claseEJCBox;
    private javax.swing.JLabel numeroJLabel;
    private com.geopista.app.utilidades.TextField numeroJTField;
    private javax.swing.JLabel numTitulosJLabel;
    private com.geopista.app.utilidades.JNumberTextField numTitulosJTField;
    private javax.swing.JLabel serieJLabel;
    private com.geopista.app.utilidades.TextField serieJTField;
    private javax.swing.JLabel costeAdquisicionJLabel;
    private com.geopista.app.utilidades.JNumberTextField costeAdquisicionJTField;
    private javax.swing.JLabel capitalJLabel;
    private com.geopista.app.utilidades.JNumberTextField capitalJTField;
    private javax.swing.JLabel precioJLabel;
    private com.geopista.app.utilidades.JNumberTextField precioJTField;
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
    private javax.swing.JLabel fAcuerdoJLabel;
    private JFormattedTextField fAcuerdoJTField;
    private CalendarButton fAcuerdoJButton;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;





}
