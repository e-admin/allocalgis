/**
 * DatosGeneralesSemovienteJPanel.java
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

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioPanel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.SemovienteBean;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 25-sep-2006
 * Time: 16:05:34
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesSemovienteJPanel extends InventarioPanel{
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


    /**
     * Método que genera el panel de los datos Generales para un semoviente
     * @param locale
     */
    public DatosGeneralesSemovienteJPanel(JFrame desktop, String locale) throws Exception{
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        descripcionJLabel = new InventarioLabel();
        cantidadJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        razaJLabel = new InventarioLabel();
        especieJLabel = new InventarioLabel();
        identificacionJLabel = new InventarioLabel();
        costeAdquisicionJLabel = new InventarioLabel();
        conservacionJLabel = new InventarioLabel();
        descFrutosJLabel = new InventarioLabel();
        valorFrutosJLabel = new InventarioLabel();
        cuentaContableJLabel = new InventarioLabel();
        propiedadJLabel = new InventarioLabel();
        fNacimientoJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();

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
        destinoJTField= new InventarioTextField(254);
        descripcionJScrollPane= new javax.swing.JScrollPane();
        descripcionJTArea= new InventarioTextPane(254);
        especieJTField= new InventarioTextField(254);
        razaEJCBox= new ComboBoxEstructuras(Estructuras.getListaRazaSemoviente(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        fNacimientoJTField= new JFormattedTextField(Constantes.df);
        fNacimientoJButton= new CalendarButton(fNacimientoJTField);
        identificacionJTField= new InventarioTextField(24);
        especieJTField= new InventarioTextField(254);
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);
        cantidadJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(999999999), true, 2);
        cantidadJTField.setSignAllowed(false);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);
        propiedadEJCBox= new ComboBoxEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
        conservacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 310, 20));
        add(fNacimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(especieJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(razaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(identificacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));
        add(cantidadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));
        add(propiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, 20));
        add(conservacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 110, 20));
        add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 110, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 110, 20));
        add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 310, 20));
        add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 295, 110, 20));
        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 335, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 355, 110, 20));

        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, -1));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, 20));
        descripcionJScrollPane.setViewportView(descripcionJTArea);
        add(descripcionJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 430, 40));
        add(fNacimientoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 140, 20));
        add(fNacimientoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 20, 20));
        add(especieJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 310, 20));
        add(razaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 160, 20));
        add(identificacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 160, 20));
        add(cantidadJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, 20));
        add(propiedadEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 160, 20));
        add(conservacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 160, 20));
        add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, 20));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 250, 160, 20));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 310, 20));
        add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 160, 20));
        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 315, 290, -1));
        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 335, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 335, 310, -1));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 355, 310, -1));

        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 380, -1));
    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        fNacimientoJTField.setEnabled(false);
        fNacimientoJButton.setEnabled(b);
        especieJTField.setEnabled(b);
        identificacionJTField.setEnabled(b);
        razaEJCBox.setEnabled(b);
        propiedadEJCBox.setEnabled(b);
        conservacionEJCBox.setEnabled(b);
        descripcionJTArea.setEnabled(b);
        costeAdquisicionJTField.setEnabled(b);
        cantidadJTField.setEnabled(b);
        descFrutosJTArea.setEnabled(b);
        valorFrutosJTField.setEnabled(b);
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJButton.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
        valorActualJTField.setEnabled(b);

    }

    /**
     * Método que carga en el panel un semoviente
     * @param semoviente a cargar
     */
    public void load(SemovienteBean semoviente){
        if (semoviente==null) return;
        adquisicionEJCBox.setSelectedPatron(semoviente.getAdquisicion()!=null?semoviente.getAdquisicion():"");
        especieJTField.setText(semoviente.getEspecie()!=null?semoviente.getEspecie():"");
        descripcionJTArea.setText(semoviente.getCaracteristicas()!=null?semoviente.getCaracteristicas():"");
        razaEJCBox.setSelectedPatron(semoviente.getRaza()!=null?semoviente.getRaza():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(semoviente.getFechaAdquisicion()));}catch(Exception e){}
        try{fNacimientoJTField.setText(Constantes.df.format(semoviente.getFechaNacimiento()));}catch(Exception e){}
        identificacionJTField.setText(semoviente.getIdentificacion()!=null?semoviente.getIdentificacion():"");
        conservacionEJCBox.setSelectedPatron(semoviente.getConservacion()!=null?semoviente.getConservacion():"");
        propiedadEJCBox.setSelectedPatron(semoviente.getPropiedad()!=null?semoviente.getPropiedad():"");
        if (semoviente.getCosteAdquisicion()!=null && semoviente.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(semoviente.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (semoviente.getValorActual()!=null && semoviente.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(semoviente.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(semoviente.getFrutos()!=null?semoviente.getFrutos():"");
        if (semoviente.getImporteFrutos()!=null && semoviente.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(semoviente.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        if (semoviente.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(semoviente.getCuentaContable().getId());
        }
        if (semoviente.getCantidad()!=null && semoviente.getCantidad().longValue()!=-1)
            cantidadJTField.setNumber(new Long(semoviente.getCantidad()));
        else cantidadJTField.setText("");
        destinoJTField.setText(semoviente.getDestino()!=null?semoviente.getDestino():"");
        patrimonioJCBox.setSelected(semoviente.getPatrimonioMunicipalSuelo()?true:false);
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un semoviente
     * @param semoviente a actualizar
     */
    public void actualizarDatosGenerales(SemovienteBean semoviente){
        if (semoviente==null) return;
        semoviente.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        try{semoviente.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        semoviente.setDestino(destinoJTField.getText().trim());
        semoviente.setRaza(razaEJCBox.getSelectedPatron());
        semoviente.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        semoviente.setCaracteristicas(descripcionJTArea.getText().trim());
        semoviente.setEspecie(especieJTField.getText().trim());
        semoviente.setIdentificacion(identificacionJTField.getText().trim());
        semoviente.setPropiedad(propiedadEJCBox.getSelectedPatron());
        semoviente.setConservacion(conservacionEJCBox.getSelectedPatron());
        try{semoviente.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{semoviente.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        semoviente.setFrutos(descFrutosJTArea.getText().trim());
        try{semoviente.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        semoviente.setCuentaContable(cuentaContable);
        try{semoviente.setCantidad(((Long)cantidadJTField.getNumber()).longValue());}catch(Exception e){}
        try{semoviente.setFechaNacimiento(Constantes.df.parse(fNacimientoJTField.getText().trim()));}catch(java.text.ParseException e){}
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        razaEJCBox.setSelectedPatron(null);
        propiedadEJCBox.setSelectedPatron(null);
        conservacionEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        descripcionJTArea.setText("");
        especieJTField.setText("");
        identificacionJTField.setText("");
        cantidadJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");
        fNacimientoJTField.setText("");
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
        cuentaJDialog.setVisible(true);
    }

    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
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
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag3"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag1"));}catch(Exception e){}
        try{razaJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag13"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.semovientes.tag9"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag2"));}catch(Exception e){}
        try{descripcionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag10"));}catch(Exception e){}
        try{especieJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag12"));}catch(Exception e){}
        try{identificacionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag14"));}catch(Exception e){}
        try{cantidadJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag11"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag5"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag6"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag7"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag8"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag4"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
        try{fNacimientoJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag15"));}catch(Exception e){}
        try{conservacionJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag16"));}catch(Exception e){}
        try{propiedadJLabel.setText(aplicacion.getI18nString("inventario.semovientes.tag17"));}catch(Exception e){}
    }

    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JScrollPane descripcionJScrollPane;
    private TextPane descripcionJTArea;
    private javax.swing.JLabel especieJLabel;
    private com.geopista.app.utilidades.TextField especieJTField;
    private javax.swing.JLabel razaJLabel;
    private ComboBoxEstructuras razaEJCBox;
    private javax.swing.JLabel identificacionJLabel;
    private com.geopista.app.utilidades.TextField identificacionJTField;
    private javax.swing.JLabel cantidadJLabel;
    private com.geopista.app.utilidades.JNumberTextField cantidadJTField;
    private javax.swing.JLabel fNacimientoJLabel;
    private JFormattedTextField fNacimientoJTField;
    private javax.swing.JLabel propiedadJLabel;
    private ComboBoxEstructuras propiedadEJCBox;
    private javax.swing.JLabel conservacionJLabel;
    private ComboBoxEstructuras conservacionEJCBox;
    private javax.swing.JLabel costeAdquisicionJLabel;
    private com.geopista.app.utilidades.JNumberTextField costeAdquisicionJTField;
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
    private CalendarButton fNacimientoJButton;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;






}
