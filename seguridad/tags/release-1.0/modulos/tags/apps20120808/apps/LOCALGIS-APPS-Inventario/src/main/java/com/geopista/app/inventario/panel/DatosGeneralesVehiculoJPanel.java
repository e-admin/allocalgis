package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.protocol.inventario.VehiculoBean;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 27-sep-2006
 * Time: 10:51:35
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesVehiculoJPanel extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;

    /**
     * Método que genera el panel de los datos Generales para los bienes vehiculo
     * @param locale
     */
    public DatosGeneralesVehiculoJPanel(String locale) throws Exception{
        this.locale= locale;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        matriculaViejaJLabel = new InventarioLabel();
        matriculaNuevaJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        numBastidorJLabel = new InventarioLabel();
        marcaJLabel = new InventarioLabel();
        motorJLabel = new InventarioLabel();
        costeAdquisicionJLabel = new InventarioLabel();
        fuerzaJLabel = new InventarioLabel();
        descFrutosJLabel = new InventarioLabel();
        valorFrutosJLabel = new InventarioLabel();
        servicioJLabel = new InventarioLabel();
        tipoVehiculoJLabel = new InventarioLabel();
        traccionJLabel = new InventarioLabel();
        propiedadJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();
        conservacionJLabel = new InventarioLabel();

        patrimonioJCBox = new javax.swing.JCheckBox();
        adquisicionEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        destinoJTField= new InventarioTextField(254);
        marcaJTField= new InventarioTextField(254);
        motorJTField= new InventarioTextField(254);
        fuerzaJTField= new InventarioTextField(24);
        servicioJTField= new InventarioTextField(254);
        tipoVehiculoEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposVehiculo(), null, locale, true);
        conservacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);
        traccionEJCBox= new ComboBoxEstructuras(Estructuras.getListaTraccion(), null, locale, true);
        propiedadEJCBox= new ComboBoxEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        numBastidorJTField= new InventarioTextField(24);
        matriculaViejaJTField= new InventarioTextField(24);
        matriculaNuevaJTField= new InventarioTextField(24);
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(matriculaViejaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(matriculaNuevaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(numBastidorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 20));
        add(marcaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(motorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(fuerzaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(servicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));
        add(tipoVehiculoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));
        add(conservacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, 20));
        add(traccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 220, 20));
        add(propiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 240, 20));
        add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 110, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 270, 110, 20));
        add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 320, 20));
        add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 338, 360, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 110, 20));


        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, 20));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, 20));
        add(matriculaViejaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 160, -1));
        add(matriculaNuevaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, -1));
        add(numBastidorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, -1));
        add(marcaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 310, -1));
        add(motorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 310, -1));
        add(fuerzaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 160, -1));
        add(servicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 310,-1));
        add(tipoVehiculoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, -1));
        add(conservacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 160, -1));
        add(traccionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 160, -1));
        add(propiedadEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, -1));
        add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 80, -1));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 80, -1));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 292, 310, 42));
        add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 338, 160, 20));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 310, -1));

        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 360, -1));

    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        matriculaViejaJTField.setEnabled(b);
        matriculaNuevaJTField.setEnabled(b);
        tipoVehiculoEJCBox.setEnabled(b);
        traccionEJCBox.setEnabled(b);
        propiedadEJCBox.setEnabled(b);
        conservacionEJCBox.setEnabled(b);
        numBastidorJTField.setEnabled(b);
        marcaJTField.setEnabled(b);
        costeAdquisicionJTField.setEnabled(b);
        motorJTField.setEnabled(b);
        fuerzaJTField.setEnabled(b);
        servicioJTField.setEnabled(b);
        descFrutosJTArea.setEnabled(b);
        valorFrutosJTField.setEnabled(b);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
        valorActualJTField.setEnabled(b);

    }

    /**
     * Método que carga en el panel un vehiculo
     * @param vehiculo a cargar
     */
    public void load(VehiculoBean vehiculo){
        if (vehiculo==null) return;
        adquisicionEJCBox.setSelectedPatron(vehiculo.getAdquisicion()!=null?vehiculo.getAdquisicion():"");
        matriculaViejaJTField.setText(vehiculo.getMatriculaVieja()!=null?vehiculo.getMatriculaVieja():"");
        matriculaNuevaJTField.setText(vehiculo.getMatriculaNueva()!=null?vehiculo.getMatriculaNueva():"");
        tipoVehiculoEJCBox.setSelectedPatron(vehiculo.getTipoVehiculo()!=null?vehiculo.getTipoVehiculo():"");
        traccionEJCBox.setSelectedPatron(vehiculo.getTraccion()!=null?vehiculo.getTraccion():"");
        conservacionEJCBox.setSelectedPatron(vehiculo.getEstadoConservacion()!=null?vehiculo.getEstadoConservacion():"");
        propiedadEJCBox.setSelectedPatron(vehiculo.getPropiedad()!=null?vehiculo.getPropiedad():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(vehiculo.getFechaAdquisicion()));}catch(Exception e){}
        numBastidorJTField.setText(vehiculo.getNumBastidor()!=null?vehiculo.getNumBastidor():"");
        marcaJTField.setText(vehiculo.getMarca()!=null?vehiculo.getMarca():"");
        motorJTField.setText(vehiculo.getMotor()!=null?vehiculo.getMotor():"");
        fuerzaJTField.setText(vehiculo.getFuerza()!=null?vehiculo.getFuerza():"");
        servicioJTField.setText(vehiculo.getServicio()!=null?vehiculo.getServicio():"");
        if (vehiculo.getCosteAdquisicion()!=null && vehiculo.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(vehiculo.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (vehiculo.getValorActual()!=null && vehiculo.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(vehiculo.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(vehiculo.getFrutos()!=null?vehiculo.getFrutos():"");
        if (vehiculo.getImporteFrutos()!=null && vehiculo.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(vehiculo.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        destinoJTField.setText(vehiculo.getDestino()!=null?vehiculo.getDestino():"");
        patrimonioJCBox.setSelected(vehiculo.getPatrimonioMunicipalSuelo()?true:false);
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un vehiculo
     * @param vehiculo a actualizar
     */
    public void actualizarDatosGenerales(VehiculoBean vehiculo){
        if (vehiculo==null) return;
        vehiculo.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        try{vehiculo.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        vehiculo.setDestino(destinoJTField.getText().trim());
        vehiculo.setTipoVehiculo(tipoVehiculoEJCBox.getSelectedPatron());
        vehiculo.setTraccion(traccionEJCBox.getSelectedPatron());
        vehiculo.setEstadoConservacion(conservacionEJCBox.getSelectedPatron());
        vehiculo.setPropiedad(propiedadEJCBox.getSelectedPatron());
        vehiculo.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        vehiculo.setMatriculaVieja(matriculaViejaJTField.getText().trim());
        vehiculo.setMatriculaNueva(matriculaNuevaJTField.getText().trim());
        vehiculo.setNumBastidor(numBastidorJTField.getText().trim());
        vehiculo.setMarca(marcaJTField.getText().trim());
        vehiculo.setMotor(motorJTField.getText().trim());
        vehiculo.setFuerza(fuerzaJTField.getText().trim());
        vehiculo.setServicio(servicioJTField.getText().trim());
        try{vehiculo.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{vehiculo.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        vehiculo.setFrutos(descFrutosJTArea.getText().trim());
        try{vehiculo.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        tipoVehiculoEJCBox.setSelectedPatron(null);
        traccionEJCBox.setSelectedPatron(null);
        conservacionEJCBox.setSelectedPatron(null);
        propiedadEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        matriculaNuevaJTField.setText("");
        matriculaViejaJTField.setText("");
        numBastidorJTField.setText("");
        marcaJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
        motorJTField.setText("");
        fuerzaJTField.setText("");
        servicioJTField.setText("");
    }


    private void renombrarComponentes(){
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag3"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag1"));}catch(Exception e){}
        try{matriculaViejaJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag4"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.vehiculo.tag19"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag2"));}catch(Exception e){}
        try{matriculaNuevaJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag5"));}catch(Exception e){}
        try{numBastidorJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag6"));}catch(Exception e){}
        try{marcaJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag7"));}catch(Exception e){}
        try{motorJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag8"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag15"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag16"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag17"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag18"));}catch(Exception e){}
        try{fuerzaJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag9"));}catch(Exception e){}
        try{servicioJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag10"));}catch(Exception e){}
        try{tipoVehiculoJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag11"));}catch(Exception e){}
        try{conservacionJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag12"));}catch(Exception e){}
        try{traccionJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag13"));}catch(Exception e){}
        try{propiedadJLabel.setText(aplicacion.getI18nString("inventario.vehiculo.tag14"));}catch(Exception e){}
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
    private javax.swing.JLabel matriculaViejaJLabel;
    private com.geopista.app.utilidades.TextField matriculaViejaJTField;
    private javax.swing.JLabel matriculaNuevaJLabel;
    private com.geopista.app.utilidades.TextField matriculaNuevaJTField;
    private javax.swing.JLabel numBastidorJLabel;
    private com.geopista.app.utilidades.TextField numBastidorJTField;
    private javax.swing.JLabel marcaJLabel;
    private com.geopista.app.utilidades.TextField marcaJTField;
    private javax.swing.JLabel motorJLabel;
    private com.geopista.app.utilidades.TextField motorJTField;
    private javax.swing.JLabel fuerzaJLabel;
    private com.geopista.app.utilidades.TextField fuerzaJTField;
    private javax.swing.JLabel servicioJLabel;
    private com.geopista.app.utilidades.TextField servicioJTField;
    private javax.swing.JLabel tipoVehiculoJLabel;
    private ComboBoxEstructuras tipoVehiculoEJCBox;
    private javax.swing.JLabel conservacionJLabel;
    private ComboBoxEstructuras conservacionEJCBox;
    private javax.swing.JLabel traccionJLabel;
    private ComboBoxEstructuras traccionEJCBox;
    private javax.swing.JLabel propiedadJLabel;
    private ComboBoxEstructuras propiedadEJCBox;
    private javax.swing.JLabel costeAdquisicionJLabel;
    private com.geopista.app.utilidades.JNumberTextField costeAdquisicionJTField;
    private javax.swing.JLabel descFrutosJLabel;
    private javax.swing.JScrollPane descFrutosJScrollPane;
    private TextPane descFrutosJTArea;
    private javax.swing.JLabel valorFrutosJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorFrutosJTField;
    private javax.swing.JCheckBox patrimonioJCBox;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;

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
