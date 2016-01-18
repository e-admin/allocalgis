/**
 * DatosGeneralesInmuebleJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DatosGenerales.java
 *
 * Created on 12 de julio de 2006, 14:45
 */

package com.geopista.app.inventario.panel;

import javax.swing.JFormattedTextField;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InmuebleRusticoBean;
import com.geopista.protocol.inventario.InmuebleUrbanoBean;

/**
 *
 * @author  charo
 */
public class DatosGeneralesInmuebleJPanel extends javax.swing.JPanel {
    private AppContext aplicacion;
    private String locale;
    private String tipo;
    /**
     * Método que genera el panel de los datos Generales para los bienes inmuebles
     * @param locale
     */
    public DatosGeneralesInmuebleJPanel(String locale, String tipo) {
        this.locale= locale;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.tipo = tipo;
        initComponents();
        renombrarComponentes();
    }
    
	private void initComponents() {
    	claseJLabel = new InventarioLabel();
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        direccionJLabel = new InventarioLabel();
        manzanaJLabel = new InventarioLabel();
        refCatastralJLabel = new InventarioLabel();
        propiedadJLabel = new InventarioLabel();
        linderoNJLabel = new InventarioLabel();
        linderoSJLabel = new InventarioLabel();
        linderoEJLabel = new InventarioLabel();
        linderoOJLabel = new InventarioLabel();
        patrimonioJCBox = new javax.swing.JCheckBox();
        adquisicionEJCBox= new ComboBoxEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
        destinoJTField= new InventarioTextField(254);
        direccionJTField= new InventarioTextField(254);
        manzanaJTField= new InventarioTextField(14);
        refCatastralJTField= new InventarioTextField(20);
        propiedadEJCBox= new ComboBoxEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
        linderoNJTField =  new InventarioTextField(254);
        linderoSJTField =  new InventarioTextField(254);
        linderoEJTField =  new InventarioTextField(254);
        linderoOJTField =  new InventarioTextField(254);
        fAdquisicionJLabel = new InventarioLabel();
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        aprovechamientoJLabel = new InventarioLabel();
        aprovechamientoEJCBox=  new ComboBoxEstructuras(Estructuras.getListaAprovechamiento(), null, locale, true);
        poligonoJLabel = new InventarioLabel();
        poligonoJTField = new com.geopista.app.utilidades.TextField(3);
        parajeJLabel = new InventarioLabel();
        parajeJTField = new com.geopista.app.utilidades.TextField(50);
        numeroOrdenJLabel = new InventarioLabel();
        numeroPropiedadJLabel = new InventarioLabel();
        numeroOrdenJTField = new com.geopista.app.utilidades.TextField(25);
        numeroPropiedadJTField = new com.geopista.app.utilidades.TextField(25);
        if (tipo != null && tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
        	claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseUrbana(), null, locale, true);
        }else{
        	if (tipo != null && tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
        		claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseRustica(), null, locale, true);
        	}        
        }

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

          
        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(direccionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 20));
        add(manzanaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 110, 20));
        add(refCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 110, 20));
        add(propiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 110, 20));
        add(linderoNJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(linderoSJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 110, 20));

        add(linderoEJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 20));

        add(linderoOJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 110, 20));

        add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 380, -1));

        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 160, -1));

        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 20, -1));

        add(direccionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 310, -1));

        add(manzanaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, -1));

        add(refCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 160, -1));

        add(propiedadEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 160, -1));

        add(linderoNJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 310, -1));

        add(linderoSJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 310, -1));

        add(linderoEJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 310, -1));

        add(linderoOJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 310, -1));

        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 110, -1));

        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, 310, -1));

        add(aprovechamientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 110, -1));

        add(aprovechamientoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 160, -1));

        add(poligonoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 110, -1));

        add(poligonoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, -1));

        add(parajeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 110, -1));

        add(parajeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 160, -1));
        
        add(numeroOrdenJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 110, -1));
        
        add(numeroOrdenJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130,290, 160, -1));
        
        add(numeroPropiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, -1));
        
        add(numeroPropiedadJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 160, -1));
        
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 110, 20));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 330, 160, 20));

    }
    /**
     * Método que habilita/deshabilita los componentes propios de un bien inmueble rustico,
     * debido a que todos los inmuebles (urbanos, rusticos) comparten el mismo panel
     * @param b
     */
    public void componentesRuralesSetEnabled(boolean b){
        /** Habilitamos/Deshabilitamos los componentes propios de RUSTICA */
        aprovechamientoJLabel.setEnabled(b);
        poligonoJLabel.setEnabled(b);
        parajeJLabel.setEnabled(b);
        aprovechamientoEJCBox.setEnabled(b);
        poligonoJTField.setEnabled(b);
        parajeJTField.setEnabled(b);
    }
    /**
     * Método que habilita/deshabilita los componentes propios de un bien inmueble urbano,
     * debido a que todos los inmuebles (urbanos, rusticos) comparten el mismo panel
     * @param b
     */
    public void componentesUrbanosSetEnabled(boolean b){
        /** Habilitamos/Deshabilitamos los componentes propios de URBANA */
        manzanaJLabel.setEnabled(b);
        manzanaJTField.setEnabled(b);
    }


    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        aprovechamientoEJCBox.setEnabled(b);
        destinoJTField.setEnabled(b);
        direccionJTField.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        linderoEJTField.setEnabled(b);
        linderoNJTField.setEnabled(b);
        linderoOJTField.setEnabled(b);
        linderoSJTField.setEnabled(b);
        manzanaJTField.setEnabled(b);
        refCatastralJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
        poligonoJTField.setEnabled(b);
        propiedadEJCBox.setEnabled(b);
        fAdquisicionJButton.setEnabled(b);
        parajeJTField.setEnabled(b);
        numeroOrdenJTField.setEnabled(b);
        numeroPropiedadJTField.setEnabled(b);
        claseEJCBox.setEnabled(b);

    }

    /**
     * Método que carga en el panel un bien inmueble
     * @param inmueble a cargar
     */
    public void load(InmuebleBean inmueble){
        if (inmueble==null || inmueble.getTipo() == null) return;
        adquisicionEJCBox.setSelectedPatron(inmueble.getAdquisicion()!=null?inmueble.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(inmueble.getClase()!=null?inmueble.getClase():"");
        destinoJTField.setText(inmueble.getDestino()!=null?inmueble.getDestino():"");
        direccionJTField.setText(inmueble.getDireccion()!=null?inmueble.getDireccion():"");
        if (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            aprovechamientoEJCBox.setSelectedPatron(inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getAprovechamiento()!=null?inmueble.getInmuebleRustico().getAprovechamiento():""):"");
            poligonoJTField.setText(inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getPoligono()!=null?inmueble.getInmuebleRustico().getPoligono():""):"");
            parajeJTField.setText(inmueble.getInmuebleRustico()!=null?(inmueble.getInmuebleRustico().getParaje()!=null?inmueble.getInmuebleRustico().getParaje():""):"");
        }else if (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            manzanaJTField.setText(inmueble.getInmuebleUrbano()!=null?(inmueble.getInmuebleUrbano().getManzana()!=null?inmueble.getInmuebleUrbano().getManzana():""):"");
        }
        propiedadEJCBox.setSelectedPatron(inmueble.getPropiedad()!=null?inmueble.getPropiedad():"");
        linderoNJTField.setText(inmueble.getLinderoNorte()!=null?inmueble.getLinderoNorte():"");
        linderoSJTField.setText(inmueble.getLinderoSur()!=null?inmueble.getLinderoSur():"");
        linderoEJTField.setText(inmueble.getLinderoEste()!=null?inmueble.getLinderoEste():"");
        linderoOJTField.setText(inmueble.getLinderoOeste()!=null?inmueble.getLinderoOeste():"");
        refCatastralJTField.setText(inmueble.getRefCatastral()!=null?inmueble.getRefCatastral():"");
        patrimonioJCBox.setSelected(inmueble.getPatrimonioMunicipalSuelo()?true:false);
        numeroOrdenJTField.setText(inmueble.getNumeroOrden()!=null?inmueble.getNumeroOrden():"");
        numeroPropiedadJTField.setText(inmueble.getNumeroPropiedad()!=null?inmueble.getNumeroPropiedad():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(inmueble.getFechaAdquisicion()));}catch(Exception e){}
    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de un bien inmueble
     * @param inmueble a actualizar
     */
    public void actualizarDatosGenerales(InmuebleBean inmueble){
        if (inmueble==null || inmueble.getTipo() == null) return;
        inmueble.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        inmueble.setClase(claseEJCBox.getSelectedPatron());
        inmueble.setDestino(destinoJTField.getText().trim());
        inmueble.setDireccion(direccionJTField.getText().trim());
        if (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            InmuebleRusticoBean ir= new InmuebleRusticoBean();
            ir.setAprovechamiento(aprovechamientoEJCBox.getSelectedPatron());
            ir.setPoligono(poligonoJTField.getText().trim());
            ir.setParaje(parajeJTField.getText().trim());
            inmueble.setInmuebleRustico(ir);
        }else if (inmueble.getTipo().equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            InmuebleUrbanoBean iu= new InmuebleUrbanoBean();
            iu.setManzana(manzanaJTField.getText().trim());
            inmueble.setInmuebleUrbano(iu);
        }
        inmueble.setPropiedad(propiedadEJCBox.getSelectedPatron());
        inmueble.setLinderoNorte(linderoNJTField.getText().trim());
        inmueble.setLinderoSur(linderoSJTField.getText().trim());
        inmueble.setLinderoEste(linderoEJTField.getText().trim());
        inmueble.setLinderoOeste(linderoOJTField.getText().trim());
        inmueble.setRefCatastral(refCatastralJTField.getText().trim());
        inmueble.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        inmueble.setNumeroOrden(numeroOrdenJTField.getText().trim());
        inmueble.setNumeroPropiedad(numeroPropiedadJTField.getText().trim());
        try{inmueble.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
    }

    public void clear(){
    	claseEJCBox.setSelectedPatron(null);
        adquisicionEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        direccionJTField.setText("");
        aprovechamientoEJCBox.setSelectedPatron(null);
        manzanaJTField.setText("");
        propiedadEJCBox.setSelectedPatron(null);
        linderoNJTField.setText("");
        linderoSJTField.setText("");
        linderoEJTField.setText("");
        linderoOJTField.setText("");
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        numeroOrdenJTField.setText("");
        numeroPropiedadJTField.setText("");
    }

    private void renombrarComponentes(){
        try{numeroOrdenJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag23"));}catch(Exception e){}
        try{numeroPropiedadJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag24"));}catch(Exception e){}
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag5"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag9"));}catch(Exception e){}
        try{direccionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag10"));}catch(Exception e){}
        try{manzanaJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag11"));}catch(Exception e){}
        try{refCatastralJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag12"));}catch(Exception e){}
        try{propiedadJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag13"));}catch(Exception e){}
        try{linderoNJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag14"));}catch(Exception e){}
        try{linderoSJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag15"));}catch(Exception e){}
        try{linderoEJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag16"));}catch(Exception e){}
        try{linderoOJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag17"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.datosGenerales.tag18"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag19"));}catch(Exception e){}
        try{aprovechamientoJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag20"));}catch(Exception e){}
        try{poligonoJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag21"));}catch(Exception e){}
        try{parajeJLabel.setText(aplicacion.getI18nString("inventario.datosGenerales.tag22"));}catch(Exception e){}
        if (tipo!=null && tipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            try{claseJLabel.setText(aplicacion.getI18nString("inventario.vias.tag22"));}catch(Exception e){}
        }
        else{
        	try{claseJLabel.setText(aplicacion.getI18nString("inventario.vias.tag21"));}catch(Exception e){}
        }
    }

    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
    }

    public void setRevertible(){
        propiedadEJCBox.setSelectedPatron(Const.PATRON_CEDIDO);
        propiedadEJCBox.setEnabled(false);
    }

    private javax.swing.JLabel adquisicionJLabel;
    private ComboBoxEstructuras adquisicionEJCBox;
    private javax.swing.JLabel aprovechamientoJLabel;
    private ComboBoxEstructuras aprovechamientoEJCBox;
    private javax.swing.JLabel destinoJLabel;
    private com.geopista.app.utilidades.TextField destinoJTField;
    private javax.swing.JLabel direccionJLabel;
    private com.geopista.app.utilidades.TextField direccionJTField;
    private javax.swing.JLabel fAdquisicionJLabel;
    private JFormattedTextField fAdquisicionJTField;
    private javax.swing.JLabel linderoEJLabel;
    private com.geopista.app.utilidades.TextField linderoEJTField;
    private javax.swing.JLabel linderoNJLabel;
    private com.geopista.app.utilidades.TextField linderoNJTField;
    private javax.swing.JLabel linderoOJLabel;
    private com.geopista.app.utilidades.TextField linderoOJTField;
    private javax.swing.JLabel linderoSJLabel;
    private com.geopista.app.utilidades.TextField linderoSJTField;
    private javax.swing.JLabel manzanaJLabel;
    private com.geopista.app.utilidades.TextField manzanaJTField;
    private javax.swing.JLabel refCatastralJLabel;
    private com.geopista.app.utilidades.TextField refCatastralJTField;
    private javax.swing.JCheckBox patrimonioJCBox;
    private javax.swing.JLabel poligonoJLabel;
    private javax.swing.JTextField poligonoJTField;
    private javax.swing.JLabel propiedadJLabel;
    private ComboBoxEstructuras propiedadEJCBox;
    private CalendarButton fAdquisicionJButton;
    private javax.swing.JLabel parajeJLabel;
    private javax.swing.JTextField parajeJTField;
    private javax.swing.JLabel numeroOrdenJLabel;
    private javax.swing.JLabel numeroPropiedadJLabel;
    private javax.swing.JTextField numeroOrdenJTField;
    private javax.swing.JTextField numeroPropiedadJTField;
    private javax.swing.JLabel claseJLabel;
    private ComboBoxEstructuras claseEJCBox;

	/**
	 * @return the fAdquisicionJTField
	 */
	public JFormattedTextField getfAdquisicionJTField() {
		return fAdquisicionJTField;
	}
	
}
