/**
 * DatosGeneralesBienMuebleJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.MuebleBean;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-sep-2006
 * Time: 16:40:00
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesBienMuebleJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;

    /**
     * Método que genera el panel de los datos Generales para los bienes muebles
     * @param locale
     */
    public DatosGeneralesBienMuebleJPanel(String locale) throws Exception{
        this.locale= locale;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{
        adquisicionJLabel = new InventarioLabel();
        claseJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        localizacionJLabel = new InventarioLabel();
        marcaJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        propiedadJLabel = new InventarioLabel();
        modeloJLabel = new InventarioLabel();
        estadoConservacionJLabel = new InventarioLabel();
        caracteristicasJLabel = new InventarioLabel();
        numSerieJLabel = new InventarioLabel();
        costeAdquisicionJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();
        descFrutosJLabel = new InventarioLabel();
        valorFrutosJLabel = new InventarioLabel();
        fFinGarantiaJLabel = new InventarioLabel();

        adquisicionEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseMuebles(), null, locale, true);
        destinoJTField= new InventarioTextField(254);        
        localizacionJTField= new InventarioTextField(254);
        marcaJTField= new InventarioTextField(254);
        propiedadEJCBox= new ComboBoxEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        estadoConservacionEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);
        modeloJTField= new com.geopista.app.utilidades.TextField(254);
        caracteristicasJScrollPane = new javax.swing.JScrollPane();
        caracteristicasJTArea = new InventarioTextPane(254);
        numSerieJTField= new InventarioTextField(24);
        costeAdquisicionJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        costeAdquisicionJTField.setSignAllowed(false);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);
        descFrutosJScrollPane= new javax.swing.JScrollPane();
        descFrutosJTArea= new InventarioTextPane(254);
        valorFrutosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorFrutosJTField.setSignAllowed(false);
        fFinGarantiaJTField = new JFormattedTextField(Constantes.df);
        fFinGarantiaJButton= new CalendarButton(fFinGarantiaJTField);


        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(marcaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(modeloJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(numSerieJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 20));
        add(fFinGarantiaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(localizacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(propiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));
        add(estadoConservacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));
        add(caracteristicasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));
        add(costeAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 110, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 110, 20));
        add(descFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 110, 20));
        add(valorFrutosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 110, 20));
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 110, 20));
        
        add(marcaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 310, 20));
        add(modeloJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 310, 20));
        add(numSerieJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 160, 20));
        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, 20));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 140, 20));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 90, 20, 20));
        add(fFinGarantiaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 140, 20));
        add(fFinGarantiaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 20, 20));
        add(localizacionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 310, 20));
        add(propiedadEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 160, 20));
        add(estadoConservacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 160, 20));
        caracteristicasJScrollPane.setViewportView(caracteristicasJTArea);
        add(caracteristicasJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 430, 40));
        add(costeAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, 20));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 160, 20));
        descFrutosJScrollPane.setViewportView(descFrutosJTArea);
        add(descFrutosJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 430, 40));
        add(valorFrutosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 350, 160, 20));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 310, 20));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 390, 160, 20));
    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        claseEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        marcaJTField.setEnabled(b);
        localizacionJTField.setEnabled(b);
        propiedadEJCBox.setEnabled(b);
        modeloJTField.setEnabled(b);
        estadoConservacionEJCBox.setEnabled(b);
        caracteristicasJTArea.setEnabled(b);
        numSerieJTField.setEnabled(b);
        costeAdquisicionJTField.setEnabled(b);
        valorActualJTField.setEnabled(b);
        descFrutosJTArea.setEnabled(b);
        valorFrutosJTField.setEnabled(b);
        destinoJTField.setEnabled(b);
        fFinGarantiaJTField.setEnabled(false);
        fFinGarantiaJButton.setEnabled(b);
    }

    /**
     * Método que carga en el panel un bien mueble
     * @param mueble mueble a cargar
     */
    public void load(MuebleBean mueble){
        if (mueble==null || mueble.getTipo() == null) return;
        adquisicionEJCBox.setSelectedPatron(mueble.getAdquisicion()!=null?mueble.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(mueble.getClase()!=null?mueble.getClase():"");
        localizacionJTField.setText(mueble.getDireccion()!=null?mueble.getDireccion():"");
        marcaJTField.setText(mueble.getMarca()!=null?mueble.getMarca():"");
        propiedadEJCBox.setSelectedPatron(mueble.getPropiedad()!=null?mueble.getPropiedad():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(mueble.getFechaAdquisicion()));}catch(Exception e){}
        modeloJTField.setText(mueble.getModelo()!=null?mueble.getModelo():"");
        estadoConservacionEJCBox.setSelectedPatron(mueble.getEstadoConservacion()!=null?mueble.getEstadoConservacion():"");
        caracteristicasJTArea.setText(mueble.getCaracteristicas()!=null?mueble.getCaracteristicas():"");
        numSerieJTField.setText(mueble.getNumSerie()!=null?mueble.getNumSerie():"");
        if (mueble.getCosteAdquisicion()!=null && mueble.getCosteAdquisicion()!=-1)
            try{costeAdquisicionJTField.setNumber(new Double(mueble.getCosteAdquisicion()));}catch(Exception e){}
        else costeAdquisicionJTField.setText("");
        if (mueble.getValorActual()!=null && mueble.getValorActual()!=-1)
            try{valorActualJTField.setNumber(new Double(mueble.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        descFrutosJTArea.setText(mueble.getFrutos()!=null?mueble.getFrutos():"");
        if (mueble.getImporteFrutos()!=null && mueble.getImporteFrutos()!=-1)
            try{valorFrutosJTField.setNumber(new Double(mueble.getImporteFrutos()));}catch(Exception e){}
        else valorFrutosJTField.setText("");
        destinoJTField.setText(mueble.getDestino()!=null?mueble.getDestino():"");
        try{fFinGarantiaJTField.setText(Constantes.df.format(mueble.getFechaFinGarantia()));}catch(Exception e){}
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
        mueble.setDireccion(localizacionJTField.getText().trim());
        mueble.setMarca(marcaJTField.getText().trim());
        mueble.setModelo(modeloJTField.getText().trim());
        mueble.setEstadoConservacion(estadoConservacionEJCBox.getSelectedPatron());
        mueble.setCaracteristicas(caracteristicasJTArea.getText().trim());
        mueble.setNumSerie(numSerieJTField.getText().trim());
        try{mueble.setCosteAdquisicion(((Double)costeAdquisicionJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{mueble.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        mueble.setFrutos(descFrutosJTArea.getText().trim());
        try{mueble.setImporteFrutos(((Double)valorFrutosJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{mueble.setFechaFinGarantia(Constantes.df.parse(fFinGarantiaJTField.getText().trim()));}catch(java.text.ParseException e){}
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        claseEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        propiedadEJCBox.setSelectedPatron(null);
        fAdquisicionJTField.setText("");
        marcaJTField.setText("");
        localizacionJTField.setText("");
        modeloJTField.setText("");
        estadoConservacionEJCBox.setSelectedPatron(null);
        caracteristicasJTArea.setText("");
        numSerieJTField.setText("");
        costeAdquisicionJTField.setText("");
        valorActualJTField.setText("");
        descFrutosJTArea.setText("");
        valorFrutosJTField.setText("");
        fFinGarantiaJTField.setText("");
    }


    private void renombrarComponentes(){
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag3"));}catch(Exception e){}
        try{claseJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag22"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag1"));}catch(Exception e){}
        try{propiedadJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag5"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag2"));}catch(Exception e){}
        try{localizacionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag10"));}catch(Exception e){}
        try{estadoConservacionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag7"));}catch(Exception e){}
        try{caracteristicasJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag8"));}catch(Exception e){}
        try{costeAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag12"));}catch(Exception e){}
        try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag13"));}catch(Exception e){}
        try{descFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag14"));}catch(Exception e){}
        try{valorFrutosJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag15"));}catch(Exception e){}
        try{marcaJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag18"));}catch(Exception e){}
        try{modeloJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag19"));}catch(Exception e){}
        try{numSerieJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag20"));}catch(Exception e){}
        try{fFinGarantiaJLabel.setText(aplicacion.getI18nString("inventario.datosGeneralesMuebles.tag21"));}catch(Exception e){}
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
    private javax.swing.JLabel marcaJLabel;
    private com.geopista.app.utilidades.TextField marcaJTField;
    private javax.swing.JLabel localizacionJLabel;
    private com.geopista.app.utilidades.TextField localizacionJTField;
    private javax.swing.JLabel propiedadJLabel;
    private ComboBoxEstructuras propiedadEJCBox;
    private javax.swing.JLabel modeloJLabel;
    private com.geopista.app.utilidades.TextField modeloJTField;
    private javax.swing.JLabel estadoConservacionJLabel;
    private ComboBoxEstructuras estadoConservacionEJCBox;

    private javax.swing.JLabel caracteristicasJLabel;
    private javax.swing.JScrollPane caracteristicasJScrollPane;
    private TextPane caracteristicasJTArea;
    private javax.swing.JLabel numSerieJLabel;
    private com.geopista.app.utilidades.TextField numSerieJTField;
    private javax.swing.JLabel costeAdquisicionJLabel;
    private com.geopista.app.utilidades.JNumberTextField costeAdquisicionJTField;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;
    private javax.swing.JLabel descFrutosJLabel;
    private javax.swing.JScrollPane descFrutosJScrollPane;
    private TextPane descFrutosJTArea;
    private javax.swing.JLabel valorFrutosJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorFrutosJTField;
    private javax.swing.JLabel fFinGarantiaJLabel;
    private JFormattedTextField fFinGarantiaJTField;
    private CalendarButton fFinGarantiaJButton;

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
