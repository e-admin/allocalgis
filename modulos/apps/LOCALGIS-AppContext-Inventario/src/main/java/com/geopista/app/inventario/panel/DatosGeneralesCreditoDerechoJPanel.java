/**
 * DatosGeneralesCreditoDerechoJPanel.java
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
import com.geopista.protocol.inventario.CreditoDerechoBean;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InventarioClient;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 22-sep-2006
 * Time: 9:46:13
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesCreditoDerechoJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;
    private javax.swing.JFrame desktop;

    private CuentaJDialog cuentaJDialog;
    private CuentaContable cuentaContable;
    private InventarioClient inventarioClient= null;

    private OtroValorJDialog otroValorJDialog;

    /**
     * Método que genera el panel de los datos Generales para los bienes creditos y derechos
     * @param locale
     */
    public DatosGeneralesCreditoDerechoJPanel(JFrame desktop, String locale) throws Exception{
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
    	claseJLabel = new InventarioLabel();
    	subClaseJLabel = new InventarioLabel();
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        importeJLabel = new InventarioLabel();
        arrendamientoJLabel = new InventarioLabel();
        deudorJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        conceptoJLabel = new InventarioLabel();
        caracteristicasJLabel = new InventarioLabel();
        cuentaContableJLabel = new InventarioLabel();
        fVencimientoJLabel = new InventarioLabel();

        inventarioClient= new InventarioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		Constantes.INVENTARIO_SERVLET_NAME);


        initCContableCBox(inventarioClient.getCuentasContables());
        cuentaContableJTField= new InventarioTextField();
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
        claseEJCBox = new ComboBoxEstructuras(Estructuras.getListaClaseCredito(), null, locale, true);
        subClaseEJCBox = new ComboBoxEstructuras(Estructuras.getListaSubclaseCredito(), null, locale, true);
        destinoJTField= new InventarioTextField(254);
        conceptoDescJTField= new InventarioTextField(254);
        deudorJTField= new InventarioTextField(254);
        conceptoEJCBox= new ComboBoxEstructuras(Estructuras.getListaConceptosCreditosDerechos(), null, locale, true);
        conceptoEJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conceptoBox_ActionPerformed();
            }
        });

        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        fVencimientoJTField = new JFormattedTextField(Constantes.df);
        fVencimientoJButton= new CalendarButton(fVencimientoJTField);
        caracteristicasJScrollPane = new javax.swing.JScrollPane();
        caracteristicasJTArea = new InventarioTextPane(254);
        importeJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeJTField.setSignAllowed(false);

        conceptoJButton= new JButton();
        conceptoJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        conceptoJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoOtroValorJButtonActionPerformed();
            }
        });
        String[]opciones={"Si","No"};
        arrendamientoJComboBox = new javax.swing.JComboBox(opciones);
 
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(fVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(conceptoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(deudorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 110, 20));
        add(caracteristicasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 310, 20));
        add(importeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));
        add(arrendamientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, 20));
        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 110, 20));
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 110, 20));
        add(subClaseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 20));
        
        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, -1));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, 20));
        add(fVencimientoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 140, -1));
        add(fVencimientoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 20, 20));
        add(conceptoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 290, -1));
        add(conceptoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, -1));
        add(conceptoDescJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 310, -1));
        add(deudorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 310, -1));
        caracteristicasJScrollPane.setViewportView(caracteristicasJTArea);
        add(caracteristicasJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 430, 40));
        add(importeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, -1));
        add(arrendamientoJComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, -1, -1));
        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 290, -1));
        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 230, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 310, -1));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 310, -1));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 160, -1));
        add(subClaseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 160, -1));
        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 380, -1));
    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        claseEJCBox.setEnabled(b);
        subClaseEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        fVencimientoJTField.setEnabled(false);
        fVencimientoJButton.setEnabled(b);
        deudorJTField.setEnabled(b);
        importeJTField.setEnabled(b);
        arrendamientoJComboBox.setEnabled(b);
        conceptoEJCBox.setEnabled(b);
        conceptoDescJTField.setEnabled(false);
        conceptoJButton.setEnabled(b);
        caracteristicasJTArea.setEnabled(b);
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJButton.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
    }

    /**
     * Método que carga en el panel un bien credito y derecho
     * @param credito a cargar
     */
    public void load(CreditoDerechoBean credito){
        if (credito==null) return;
        adquisicionEJCBox.setSelectedPatron(credito.getAdquisicion()!=null?credito.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(credito.getClase()!=null?credito.getClase():"");
        subClaseEJCBox.setSelectedPatron(credito.getSubClase()!=null?credito.getSubClase():"");
        deudorJTField.setText(credito.getDeudor()!=null?credito.getDeudor():"");
        conceptoDescJTField.setText(credito.getConceptoDesc()!=null?credito.getConceptoDesc():"");
        conceptoEJCBox.setSelectedPatron(credito.getConcepto()!=null?credito.getConcepto():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(credito.getFechaAdquisicion()));}catch(Exception e){}
        try{fVencimientoJTField.setText(Constantes.df.format(credito.getFechaVencimiento()));}catch(Exception e){}
        caracteristicasJTArea.setText(credito.getCaracteristicas()!=null?credito.getCaracteristicas():"");
        if (credito.getImporte()!=null && credito.getImporte()!=-1)
            try{importeJTField.setNumber(new Double(credito.getImporte()));}catch(Exception e){}
        else importeJTField.setText("");
        arrendamientoJComboBox.setSelectedIndex(credito.isArrendamiento()?0:1);
        if (credito.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(credito.getCuentaContable().getId());
        }
        destinoJTField.setText(credito.getDestino()!=null?credito.getDestino():"");
        patrimonioJCBox.setSelected(credito.getPatrimonioMunicipalSuelo()?true:false);
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un bien credito y derecho
     * @param credito a actualizar
     */
    public void actualizarDatosGenerales(CreditoDerechoBean credito){
        if (credito==null) return;
        credito.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        credito.setClase(claseEJCBox.getSelectedPatron());
        credito.setSubClase(subClaseEJCBox.getSelectedPatron());
        try{credito.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        credito.setConcepto(conceptoEJCBox.getSelectedPatron());
        credito.setConceptoDesc(conceptoDescJTField.getText().trim());
        try{credito.setFechaVencimiento(Constantes.df.parse(fVencimientoJTField.getText().trim()));}catch(java.text.ParseException e){}
        credito.setDestino(destinoJTField.getText().trim());
        credito.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        credito.setDeudor(deudorJTField.getText().trim());
        credito.setCaracteristicas(caracteristicasJTArea.getText().trim());
        try{credito.setImporte(((Double)importeJTField.getNumber()).doubleValue());}catch(Exception e){}
        credito.setArrendamiento(arrendamientoJComboBox.getSelectedIndex()==0);
        credito.setCuentaContable(cuentaContable);
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        claseEJCBox.setSelectedPatron(null);
        subClaseEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        conceptoEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        fVencimientoJTField.setText("");
        deudorJTField.setText("");
        conceptoDescJTField.setText("");
        caracteristicasJTArea.setText("");
        importeJTField.setText("");
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");
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

    private void abrirDialogoOtroValorJButtonActionPerformed() {
        otroValorJDialog= new OtroValorJDialog(desktop, aplicacion.getI18nString("inventario.creditosDerechos.tag11"),
                                               aplicacion.getI18nString("inventario.creditosDerechos.tag5"));
        otroValorJDialog.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
				otroValorJDialog_actionPerformed();
			}
		});
        otroValorJDialog.show();
    }

    private void otroValorJDialog_actionPerformed(){
        if (otroValorJDialog.getValor()!=null) conceptoEJCBox.setSelectedPatron(null);
        conceptoDescJTField.setText(otroValorJDialog.getValor()!=null?otroValorJDialog.getValor():"");

       otroValorJDialog.dispose();
    }

    private void conceptoBox_ActionPerformed(){
        if (conceptoEJCBox.getSelectedIndex()!=0){
            conceptoDescJTField.setText("");
        }
    }

    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
    }

    private void renombrarComponentes(){
    	try{subClaseJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag15"));}catch(Exception e){}
    	try{claseJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag14"));}catch(Exception e){}
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag3"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag1"));}catch(Exception e){}
        try{conceptoJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag5"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag10"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag2"));}catch(Exception e){}
        try{importeJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag6"));}catch(Exception e){}
        try{deudorJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag4"));}catch(Exception e){}
        try{caracteristicasJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag8"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag9"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
        try{conceptoJButton.setToolTipText(aplicacion.getI18nString("inventario.creditosDerechos.tag12"));}catch(Exception e){}
        try{fVencimientoJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag7"));}catch(Exception e){}
        try{arrendamientoJLabel.setText(aplicacion.getI18nString("inventario.creditosDerechos.tag13"));}catch(Exception e){}
    }
    private javax.swing.JLabel claseJLabel;
    private ComboBoxEstructuras claseEJCBox;
    private javax.swing.JLabel subClaseJLabel;
    private ComboBoxEstructuras subClaseEJCBox;
    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;    
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel conceptoJLabel;
    private ComboBoxEstructuras conceptoEJCBox;
    private com.geopista.app.utilidades.TextField conceptoDescJTField;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField deudorJTField;
    private javax.swing.JLabel deudorJLabel;
    private javax.swing.JLabel caracteristicasJLabel;
    private javax.swing.JScrollPane caracteristicasJScrollPane;
    private TextPane caracteristicasJTArea;
    private javax.swing.JLabel importeJLabel;
    private com.geopista.app.utilidades.JNumberTextField importeJTField;
    private javax.swing.JLabel cuentaContableJLabel;
    private com.geopista.app.inventario.CuentasJComboBox cuentaContableJCBox;
    private javax.swing.JTextField cuentaContableJTField;
    private JButton cuentaContableJButton;
    private javax.swing.JCheckBox patrimonioJCBox;
    private javax.swing.JLabel fVencimientoJLabel;
    private JFormattedTextField fVencimientoJTField;
    private CalendarButton fVencimientoJButton;
    private JButton conceptoJButton;
    private javax.swing.JComboBox arrendamientoJComboBox;
    private javax.swing.JLabel arrendamientoJLabel;




}
