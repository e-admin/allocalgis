/**
 * DatosGeneralesMuebleArtisticoJPanel.java
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
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.MuebleBean;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 28-ago-2006
 * Time: 9:47:13
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesMuebleArtisticoJPanel extends JPanel{
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
     * Método que genera el panel de los datos Generales para los bienes muebles historico artisticos
     * @param locale
     */
    public DatosGeneralesMuebleArtisticoJPanel(JFrame desktop, String locale) throws Exception{
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        adquisicionJLabel = new InventarioLabel();
        claseJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        localizacionJLabel = new InventarioLabel();
        ubicacionJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        propiedadJLabel = new InventarioLabel();
        autorJLabel = new InventarioLabel();
        estadoConservacionJLabel = new InventarioLabel();
        caracteristicasJLabel = new InventarioLabel();
        materialJLabel = new InventarioLabel();
        costeAdquisicionJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();
        descFrutosJLabel = new InventarioLabel();
        valorFrutosJLabel = new InventarioLabel();
        cuentaContableJLabel = new InventarioLabel();

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
        claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseMuebles(), null, locale, true);
        destinoJTField= new InventarioTextField(254);
        localizacionJTField= new InventarioTextField(254);
        ubicacionJTField= new InventarioTextField(254);
        propiedadEJCBox= new ComboBoxEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        estadoConservacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);
        autorJTField= new InventarioTextField(254);
        caracteristicasJScrollPane = new javax.swing.JScrollPane();
        caracteristicasJTArea = new InventarioTextPane(254);
        materialJTField= new InventarioTextField(254);
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(ubicacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(localizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(propiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 20));
        add(autorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(estadoConservacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(caracteristicasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(materialJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 110, 20));
        add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 100, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 220, 110, 20));
        add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 110, 20));
        add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 110, 20));
        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 110, 20));

        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, -1));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, 20));
        add(ubicacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 310, -1));
        add(localizacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 310, -1));
        add(propiedadEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, -1));
        add(autorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 310, -1));
        add(estadoConservacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 160, -1));
        caracteristicasJScrollPane.setViewportView(caracteristicasJTArea);
        add(caracteristicasJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 310, 48));
        add(materialJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 310, -1));
        add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 100, -1));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 220, 100, -1));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 310, 37));
        add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 160, -1));
        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 290, -1));
        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 310, -1));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 310, -1));
        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 380, -1));
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 110, 20));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 160, -1));
    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        claseEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        ubicacionJTField.setEnabled(b);
        localizacionJTField.setEnabled(b);
        propiedadEJCBox.setEnabled(b);
        autorJTField.setEnabled(b);
        estadoConservacionEJCBox.setEnabled(b);
        caracteristicasJTArea.setEnabled(b);
        materialJTField.setEnabled(b);
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
     * Método que carga en el panel un bien mueble Historico Artistico
     * @param mueble a cargar
     */
    public void load(MuebleBean mueble){
        if (mueble==null || mueble.getTipo() == null) return;
        adquisicionEJCBox.setSelectedPatron(mueble.getAdquisicion()!=null?mueble.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(mueble.getClase()!=null?mueble.getClase():"");
        localizacionJTField.setText(mueble.getDireccion()!=null?mueble.getDireccion():"");
        ubicacionJTField.setText(mueble.getUbicacion()!=null?mueble.getUbicacion():"");
        propiedadEJCBox.setSelectedPatron(mueble.getPropiedad()!=null?mueble.getPropiedad():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(mueble.getFechaAdquisicion()));}catch(Exception e){}
        autorJTField.setText(mueble.getArtista()!=null?mueble.getArtista():"");
        estadoConservacionEJCBox.setSelectedPatron(mueble.getEstadoConservacion()!=null?mueble.getEstadoConservacion():"");
        caracteristicasJTArea.setText(mueble.getCaracteristicas()!=null?mueble.getCaracteristicas():"");
        materialJTField.setText(mueble.getMaterial()!=null?mueble.getMaterial():"");
        if (mueble.getCosteAdquisicion()!=null &&mueble.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(mueble.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (mueble.getValorActual()!=null && mueble.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(mueble.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(mueble.getFrutos()!=null?mueble.getFrutos():"");
        if (mueble.getImporteFrutos()!=null && mueble.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(mueble.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        if (mueble.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(mueble.getCuentaContable().getId());
        }
        destinoJTField.setText(mueble.getDestino()!=null?mueble.getDestino():"");
        patrimonioJCBox.setSelected(mueble.getPatrimonioMunicipalSuelo()?true:false);
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un bien mueble Historico Artistico
     * @param mueble a actualizar
     */
    public void actualizarDatosGenerales(MuebleBean mueble){
        if (mueble==null || mueble.getTipo() == null) return;
        mueble.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        mueble.setClase(claseEJCBox.getSelectedPatron());
        try{mueble.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        mueble.setDestino(destinoJTField.getText().trim());
        mueble.setPropiedad(propiedadEJCBox.getSelectedPatron());
        mueble.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        mueble.setDireccion(localizacionJTField.getText().trim());
        mueble.setUbicacion(ubicacionJTField.getText().trim());
        mueble.setArtista(autorJTField.getText().trim());
        mueble.setEstadoConservacion(estadoConservacionEJCBox.getSelectedPatron());
        mueble.setCaracteristicas(caracteristicasJTArea.getText().trim());
        mueble.setMaterial(materialJTField.getText().trim());
        try{mueble.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{mueble.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        mueble.setFrutos(descFrutosJTArea.getText().trim());
        try{mueble.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        mueble.setCuentaContable(cuentaContable);
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        claseEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        propiedadEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        ubicacionJTField.setText("");
        localizacionJTField.setText("");
        autorJTField.setText("");
        estadoConservacionEJCBox.setSelectedPatron(null);
        caracteristicasJTArea.setText("");
        materialJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
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
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag3"));}catch(Exception e){}
        try{claseJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag22"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag1"));}catch(Exception e){}
        try{propiedadJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag5"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag17"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag2"));}catch(Exception e){}
        try{ubicacionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag4"));}catch(Exception e){}
        try{localizacionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag10"));}catch(Exception e){}
        try{autorJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag6"));}catch(Exception e){}
        try{estadoConservacionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag7"));}catch(Exception e){}
        try{caracteristicasJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag8"));}catch(Exception e){}
        try{materialJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag11"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag12"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag13"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag14"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag15"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag16"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
    }
    

    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;
    private javax.swing.JLabel claseJLabel;
    private ComboBoxEstructuras claseEJCBox;
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel ubicacionJLabel;
    private com.geopista.app.utilidades.TextField ubicacionJTField;
    private javax.swing.JLabel localizacionJLabel;
    private com.geopista.app.utilidades.TextField localizacionJTField;
    private javax.swing.JLabel propiedadJLabel;
    private ComboBoxEstructuras propiedadEJCBox;
    private javax.swing.JLabel autorJLabel;
    private com.geopista.app.utilidades.TextField autorJTField;
    private javax.swing.JLabel estadoConservacionJLabel;
    private ComboBoxEstructuras estadoConservacionEJCBox;

    private javax.swing.JLabel caracteristicasJLabel;
    private javax.swing.JScrollPane caracteristicasJScrollPane;
    private TextPane caracteristicasJTArea;
    private javax.swing.JLabel materialJLabel;
    private com.geopista.app.utilidades.TextField materialJTField;
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

    
    /**
	 * @param costeAdquisicionJTField the costeAdquisicionJTField to set
	 */
	public void setCosteAdquisicionJTField(JNumberTextField costeAdquisicionJTField) {
		this.costeAdquisicionJTField = costeAdquisicionJTField;
	}

	/**
	 * @return the costeAdquisicionJTField
	 */
	public JNumberTextField getCosteAdquisicionJTField() {
		return costeAdquisicionJTField;
	}

	/**
	 * @param fAdquisicionJTField the fAdquisicionJTField to set
	 */
	public void setfAdquisicionJTField(JFormattedTextField fAdquisicionJTField) {
		this.fAdquisicionJTField = fAdquisicionJTField;
	}

	/**
	 * @return the fAdquisicionJTField
	 */
	public JFormattedTextField getfAdquisicionJTField() {
		return fAdquisicionJTField;
	}

}
