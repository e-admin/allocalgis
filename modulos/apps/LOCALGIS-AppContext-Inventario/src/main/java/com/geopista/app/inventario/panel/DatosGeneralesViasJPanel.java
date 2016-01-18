/**
 * DatosGeneralesViasJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.CuentaContable;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.ViaBean;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-sep-2006
 * Time: 12:57:20
 * To change this template use File | Settings | File Templates.
 */
public class DatosGeneralesViasJPanel extends JPanel{
    /**
	 * 
	 */
    Logger logger= Logger.getLogger(DatosGeneralesViasJPanel.class);

	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private String locale;
    private javax.swing.JFrame desktop;

    private CuentaJDialog cuentaJDialog;
    private CuentaContable cuentaContable;
    private InventarioClient inventarioClient= null;
    private String tipo;
	private static double valorMetro = 0;
    
	private boolean urb ;
	private boolean rus ;


    /**
     * Método que genera el panel de los datos Generales para las vias urbanas y rusticas
     * @param locale
     */
    public DatosGeneralesViasJPanel(JFrame desktop, String locale, String tipo) throws Exception{
        this.locale= locale;
        this.desktop= desktop;
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.tipo = tipo;
        initComponents();
        renombrarComponentes(tipo);
        
    }


    private void initComponents() throws Exception{
    	claseJLabel = new InventarioLabel();
        adquisicionJLabel = new InventarioLabel();
        destinoJLabel = new InventarioLabel();
        categoriaJLabel = new InventarioLabel();
        codigoJLabel = new InventarioLabel();
        fAdquisicionJLabel = new InventarioLabel();
        nombreJLabel = new InventarioLabel();
        numApliquesJLabel = new InventarioLabel();
        numBancosJLabel = new InventarioLabel();
        numPapelerasJLabel = new InventarioLabel();
        metrosPavimentadosJLabel = new InventarioLabel();
        metrosNoPavimentadosJLabel = new InventarioLabel();
        inicioJLabel = new InventarioLabel();
        cuentaContableJLabel = new InventarioLabel();
        finJLabel = new InventarioLabel();
        longitudJLabel = new InventarioLabel();
        anchoJLabel = new InventarioLabel();
        valorActualJLabel = new InventarioLabel();
        zonaVerdeJLabel = new InventarioLabel();

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
        if (tipo.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS)){
        	claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseUrbana(), null, locale, true);
        }else{
        	if (tipo.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
        		claseEJCBox= new ComboBoxEstructuras(Estructuras.getListaClaseRustica(), null, locale, true);
        	}
        	
        }
        
        destinoJTField= new InventarioTextField(254);
        categoriaJTField= new  InventarioTextField(254);
        inicioJTField= new  InventarioTextField(254);
        finJTField= new  InventarioTextField(254);
        codigoEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposViaINE(), null, locale, true);
        fAdquisicionJTField = new JFormattedTextField(Constantes.df);
        fAdquisicionJButton= new CalendarButton(fAdquisicionJTField);
        nombreJTField= new  InventarioTextField(254);
        metrosPavimentadosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        metrosPavimentadosJTField.setSignAllowed(false);
        metrosNoPavimentadosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        metrosNoPavimentadosJTField.setSignAllowed(false);
        zonaVerdeJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        zonaVerdeJTField.setSignAllowed(false);
        longitudJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        longitudJTField.setSignAllowed(false);
        anchoJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        anchoJTField.setSignAllowed(false);
        numApliquesJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(9999), true, 2);
        numApliquesJTField.setSignAllowed(false);
        numBancosJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(9999), true, 2);
        numBancosJTField.setSignAllowed(false);
        numPapelerasJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(9999), true, 2);
        numPapelerasJTField.setSignAllowed(false);
        valorActualJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        valorActualJTField.setSignAllowed(false);

        //Se añaden listener, para que cuando cambie algun campo, se recalcule el valor de regulacion
        longitudJTField.addFocusListener(new MyFocusListenerAncho());
        anchoJTField.addFocusListener(new MyFocusListenerAlto());
        
        metrosPavimentadosJTField.addFocusListener(new MyFocusListenerPav());
        metrosNoPavimentadosJTField.addFocusListener(new MyFocusListenerPav());
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(adquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 20));
        add(fAdquisicionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 110, 20));
        add(categoriaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 110, 20));
        add(codigoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 110, 20));
        add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 110, 20));
        add(numApliquesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 110, 20));
        add(numBancosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 110, 20));
        add(numPapelerasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 110, 20));
        add(metrosPavimentadosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 110, 20));
        add(metrosNoPavimentadosJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 110, 20));
        add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 110, 20));
        add(finJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 110, 20));
        add(valorActualJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 110, 20));
        add(zonaVerdeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 310, 20));
        add(longitudJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 110, 20));
        add(anchoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, 110, 20));
        add(cuentaContableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 110, 20));
        add(claseJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 110, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 110, 20));

        add(adquisicionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 160, 20));
        add(fAdquisicionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 140, -1));
        add(fAdquisicionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 20, 20));
        add(categoriaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 310, -1));
        add(codigoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 160, -1));
        add(nombreJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 310, -1));
        add(numApliquesJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 160, -1));
        add(numBancosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 160, -1));
        add(numPapelerasJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 160, -1));
        add(metrosPavimentadosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 160, -1));
        add(metrosNoPavimentadosJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 160, -1));
        add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 310, -1));
        add(finJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 310, -1));
        add(valorActualJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 160, -1));
        add(zonaVerdeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 160, -1));
        add(longitudJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 160, -1));
        add(anchoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, 160, -1));
        add(cuentaContableJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 290, -1));
        add(cuentaContableJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 320, 20, 20));
        add(cuentaContableJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 310, -1));
        add(claseEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 160, 20));
        add(destinoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 310, -1));

    //    add(patrimonioJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 380, -1));

    }

    public void setEnabled(boolean b){
        adquisicionEJCBox.setEnabled(b);
        claseEJCBox.setEnabled(b);
        fAdquisicionJTField.setEnabled(false);
        fAdquisicionJButton.setEnabled(b);
        categoriaJTField.setEnabled(b);
        codigoEJCBox.setEnabled(b);
        nombreJTField.setEnabled(b);
        numApliquesJTField.setEnabled(b);
        numBancosJTField.setEnabled(b);
        numPapelerasJTField.setEnabled(b);
        metrosPavimentadosJTField.setEnabled(b);
        metrosNoPavimentadosJTField.setEnabled(b);
        inicioJTField.setEnabled(b);
        finJTField.setEnabled(b);
        longitudJTField.setEnabled(b);
        anchoJTField.setEnabled(b);
        cuentaContableJCBox.setEnabled(b);
        cuentaContableJButton.setEnabled(b);
        cuentaContableJTField.setEnabled(false);
        destinoJTField.setEnabled(b);
        patrimonioJCBox.setEnabled(b);
        valorActualJTField.setEnabled(b);
        zonaVerdeJTField.setEnabled(b);

    }

    /**
     * Método que carga en el panel una via urbana y/o rustica
     * @param via a cargar
     */
    public void load(ViaBean via){
        if (via==null) return;
        adquisicionEJCBox.setSelectedPatron(via.getAdquisicion()!=null?via.getAdquisicion():"");
        claseEJCBox.setSelectedPatron(via.getClase()!=null?via.getClase():"");
        categoriaJTField.setText(via.getCategoria()!=null?via.getCategoria():"");
        nombreJTField.setText(via.getNombreVia()!=null?via.getNombreVia():"");
        codigoEJCBox.setSelectedPatron(via.getCodigo()!=null?via.getCodigo():"");
        try{fAdquisicionJTField.setText(Constantes.df.format(via.getFechaAdquisicion()));}catch(Exception e){}
        inicioJTField.setText(via.getInicioVia()!=null?via.getInicioVia():"");
        finJTField.setText(via.getFinVia()!=null?via.getFinVia():"");
        if (via.getNumApliques()!=null && via.getNumApliques().longValue()!=-1)
            try{numApliquesJTField.setNumber(new Long(via.getNumApliques()));}catch(Exception e){}
        else numApliquesJTField.setText("");
        if (via.getNumBancos()!=null && via.getNumBancos().longValue()!=-1)
            try{numBancosJTField.setNumber(new Long(via.getNumBancos()));}catch(Exception e){}
        else numBancosJTField.setText("");
        if (via.getNumPapeleras()!=null && via.getNumPapeleras().longValue()!=-1)
            try{numPapelerasJTField.setNumber(new Long(via.getNumPapeleras()));}catch(Exception e){}
        else numPapelerasJTField.setText("");
        if (via.getValorActual()!=null && via.getValorActual().doubleValue()!=-1)
            try{valorActualJTField.setNumber(new Double(via.getValorActual()));}catch(Exception e){}
        else valorActualJTField.setText("");
        if (via.getMetrosPavimentados()!=null && via.getMetrosPavimentados().doubleValue()!=-1)
            try{metrosPavimentadosJTField.setNumber(new Double(via.getMetrosPavimentados()));}catch(Exception e){}
        else metrosPavimentadosJTField.setText("");
        if (via.getMetrosNoPavimentados()!=null && via.getMetrosNoPavimentados().doubleValue()!=-1)
            try{metrosNoPavimentadosJTField.setNumber(new Double(via.getMetrosNoPavimentados()));}catch(Exception e){}
        else metrosNoPavimentadosJTField.setText("");
        if (via.getZonasVerdes()!=null && via.getZonasVerdes().doubleValue()!=-1)
            try{zonaVerdeJTField.setNumber(new Double(via.getZonasVerdes()));}catch(Exception e){}
        else zonaVerdeJTField.setText("");
        if (via.getLongitud()!=null && via.getLongitud().doubleValue()!=-1)
            try{longitudJTField.setNumber(new Double(via.getLongitud()));}catch(Exception e){}
        else longitudJTField.setText("");
        if (via.getAncho()!=null && via.getAncho().doubleValue()!=-1)
            try{anchoJTField.setNumber(new Double(via.getAncho()));}catch(Exception e){}
        else anchoJTField.setText("");
        if (via.getCuentaContable() != null){
            cuentaContableJCBox.setSelected(via.getCuentaContable().getId());
        }
        destinoJTField.setText(via.getDestino()!=null?via.getDestino():"");
        patrimonioJCBox.setSelected(via.getPatrimonioMunicipalSuelo()?true:false);
        
        urb = via.isUrbana();
		rus = via.isRustica();   
		
//		 Si es inmueble urbano
		if (urb	&& ((via.getMetrosPavimentados() != null && via.getMetrosPavimentados() > 0) || (via.getMetrosNoPavimentados() != null && via.getMetrosNoPavimentados()> 0))) {

			try {
				anchoJTField.setNumber(via.getAncho());
				longitudJTField.setNumber(via.getLongitud());
				metrosPavimentadosJTField.setNumber(via.getMetrosPavimentados());
				metrosNoPavimentadosJTField.setNumber(via.getMetrosNoPavimentados());
			} catch (Exception e) {
				logger.error("No se ha podido cargar ciertas propiedades del panel de Datos de Valoracion.");
				e.printStackTrace();
			}
			calculaRegularizado(via.getMetrosPavimentados(),via.getMetrosNoPavimentados());
		}

		if (rus&&((via.getMetrosPavimentados() != null && via.getMetrosPavimentados() > 0) || (via.getMetrosNoPavimentados() != null && via.getMetrosNoPavimentados()> 0))) {
			try {
				anchoJTField.setNumber(via.getAncho());
				longitudJTField.setNumber(via.getLongitud());
				metrosPavimentadosJTField.setNumber(via.getMetrosPavimentados());
				metrosNoPavimentadosJTField.setNumber(via.getMetrosNoPavimentados());
			} catch (Exception e) {
				logger.error("No se ha podido cargar ciertas propiedades del panel de Datos de Valoracion.");
				e.printStackTrace();
			}
			calculaRegularizado(via.getMetrosPavimentados(),via.getMetrosNoPavimentados());

		}

    }

    public void test(){

    }


    /**
     * Método que actualiza los datos generales de una via urbana y/o rustica
     * @param via a actualizar
     */
    public void actualizarDatosGenerales(ViaBean via){
        if (via==null) return;
        via.setAdquisicion(adquisicionEJCBox.getSelectedPatron());
        via.setClase(claseEJCBox.getSelectedPatron());
        try{via.setFechaAdquisicion(Constantes.df.parse(fAdquisicionJTField.getText().trim()));}catch(java.text.ParseException e){}
        via.setDestino(destinoJTField.getText().trim());
        via.setCodigo(codigoEJCBox.getSelectedPatron());
        via.setPatrimonioMunicipalSuelo(patrimonioJCBox.isSelected()?"1":"0");
        via.setCategoria(categoriaJTField.getText().trim());
        via.setNombreVia(nombreJTField.getText().trim());
        via.setInicioVia(inicioJTField.getText().trim());
        via.setFinVia(finJTField.getText().trim());
        try{via.setNumApliques(((Long)numApliquesJTField.getNumber()).longValue());}catch(Exception e){}
        try{via.setNumBancos(((Long)numBancosJTField.getNumber()).longValue());}catch(Exception e){}
        try{via.setNumPapeleras((((Long)numPapelerasJTField.getNumber()).longValue()));}catch(Exception e){}
        try{via.setValorActual(((Double)valorActualJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{via.setMetrosPavimentados(((Double)metrosPavimentadosJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{via.setMetrosNoPavimentados(((Double)metrosNoPavimentadosJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{via.setZonasVerdes(((Double)zonaVerdeJTField.getNumber()).doubleValue());}catch(Exception e){}
        try{via.setLongitud(((Double)longitudJTField.getNumber()).doubleValue());}catch(Exception e){}
        via.setCuentaContable(cuentaContable);
        try{via.setAncho(((Double)anchoJTField.getNumber()).doubleValue());}catch(Exception e){}
    }

    public void clear(){
        adquisicionEJCBox.setSelectedPatron(null);
        claseEJCBox.setSelectedPatron(null);
        destinoJTField.setText("");
        codigoEJCBox.setSelectedPatron(null);
        patrimonioJCBox.setSelected(false);
        fAdquisicionJTField.setText("");
        categoriaJTField.setText("");
        nombreJTField.setText("");
        inicioJTField.setText("");
        finJTField.setText("");
        numApliquesJTField.setText("");
        numBancosJTField.setText("");
        numPapelerasJTField.setText("");
        valorActualJTField.setText("");
        longitudJTField.setText("");
        anchoJTField.setText("");
        cuentaContableJCBox.setSelectedIndex(0);
        cuentaContableJTField.setText("");
        zonaVerdeJTField.setText("");
        metrosPavimentadosJTField.setText("");
        metrosNoPavimentadosJTField.setText("");
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


    private void renombrarComponentes(String tipo){
        try{adquisicionJLabel.setText(aplicacion.getI18nString("inventario.vias.tag3"));}catch(Exception e){}
        try{destinoJLabel.setText(aplicacion.getI18nString("inventario.vias.tag1"));}catch(Exception e){}
        try{categoriaJLabel.setText(aplicacion.getI18nString("inventario.vias.tag5"));}catch(Exception e){}
        try{patrimonioJCBox.setText(aplicacion.getI18nString("inventario.vias.tag20"));}catch(Exception e){}
        try{fAdquisicionJLabel.setText(aplicacion.getI18nString("inventario.vias.tag2"));}catch(Exception e){}
        try{codigoJLabel.setText(aplicacion.getI18nString("inventario.vias.tag6"));}catch(Exception e){}
        try{nombreJLabel.setText(aplicacion.getI18nString("inventario.vias.tag7"));}catch(Exception e){}
        try{numApliquesJLabel.setText(aplicacion.getI18nString("inventario.vias.tag8"));}catch(Exception e){}
        try{numBancosJLabel.setText(aplicacion.getI18nString("inventario.vias.tag10"));}catch(Exception e){}
        try{numPapelerasJLabel.setText(aplicacion.getI18nString("inventario.vias.tag9"));}catch(Exception e){}
        if (tipo!=null && tipo.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
            try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.vias.tag16"));}catch(Exception e){}
            try{claseJLabel.setText(aplicacion.getI18nString("inventario.vias.tag22"));}catch(Exception e){}
        }
        else{
        	try{valorActualJLabel.setText(aplicacion.getI18nString("inventario.vias.tag15"));}catch(Exception e){}
        	try{claseJLabel.setText(aplicacion.getI18nString("inventario.vias.tag21"));}catch(Exception e){}
        }
        try{metrosPavimentadosJLabel.setText(aplicacion.getI18nString("inventario.vias.tag11"));}catch(Exception e){}
        try{metrosNoPavimentadosJLabel.setText(aplicacion.getI18nString("inventario.vias.tag12"));}catch(Exception e){}
        try{cuentaContableJLabel.setText(aplicacion.getI18nString("inventario.vias.tag4"));}catch(Exception e){}
        try{cuentaContableJButton.setToolTipText(aplicacion.getI18nString("inventario.datosAmortizacion.tag8"));}catch(Exception e){}
        try{inicioJLabel.setText(aplicacion.getI18nString("inventario.vias.tag13"));}catch(Exception e){}
        try{finJLabel.setText(aplicacion.getI18nString("inventario.vias.tag14"));}catch(Exception e){}
        try{zonaVerdeJLabel.setText(aplicacion.getI18nString("inventario.vias.tag17"));}catch(Exception e){}
        try{longitudJLabel.setText(aplicacion.getI18nString("inventario.vias.tag18"));}catch(Exception e){}
        try{anchoJLabel.setText(aplicacion.getI18nString("inventario.vias.tag19"));}catch(Exception e){}
    }
    public void patrimonioChecked(){
        patrimonioJCBox.setSelected(true);
        patrimonioJCBox.setEnabled(false);
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
    private javax.swing.JLabel categoriaJLabel;
    private com.geopista.app.utilidades.TextField categoriaJTField;
    private javax.swing.JLabel nombreJLabel;
    private com.geopista.app.utilidades.TextField nombreJTField;
    private javax.swing.JLabel codigoJLabel;
    private ComboBoxEstructuras codigoEJCBox;
    private javax.swing.JLabel inicioJLabel;
    private com.geopista.app.utilidades.TextField inicioJTField;
    private javax.swing.JLabel finJLabel;
    private com.geopista.app.utilidades.TextField finJTField;
    private javax.swing.JLabel numApliquesJLabel;
    private com.geopista.app.utilidades.JNumberTextField numApliquesJTField;
    private javax.swing.JLabel numPapelerasJLabel;
    private com.geopista.app.utilidades.JNumberTextField numPapelerasJTField;
    private javax.swing.JLabel numBancosJLabel;
    private com.geopista.app.utilidades.JNumberTextField numBancosJTField;
    private javax.swing.JLabel metrosPavimentadosJLabel;
    private com.geopista.app.utilidades.JNumberTextField metrosPavimentadosJTField;
    private javax.swing.JLabel metrosNoPavimentadosJLabel;
    private com.geopista.app.utilidades.JNumberTextField metrosNoPavimentadosJTField;
    private javax.swing.JLabel zonaVerdeJLabel;
    private com.geopista.app.utilidades.JNumberTextField zonaVerdeJTField;
    private javax.swing.JLabel longitudJLabel;
    private com.geopista.app.utilidades.JNumberTextField longitudJTField;
    private javax.swing.JLabel anchoJLabel;
    private com.geopista.app.utilidades.JNumberTextField anchoJTField;
    private javax.swing.JLabel cuentaContableJLabel;
    private com.geopista.app.inventario.CuentasJComboBox cuentaContableJCBox;
    private javax.swing.JTextField cuentaContableJTField;
    private JButton cuentaContableJButton;
    private javax.swing.JCheckBox patrimonioJCBox;
    private javax.swing.JLabel valorActualJLabel;
    private com.geopista.app.utilidades.JNumberTextField valorActualJTField;

	class MyFocusListenerAlto implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			// Si el ancho introducido es válido
			double largo = 0;
			try {
				largo = (Double) longitudJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del largo");
//				exc.printStackTrace();
			}
			if (largo > 0) {

				// Si long!=null, calculo los metros Pav
				double ancho = 0;
				try {
					ancho = (Double) anchoJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura del ancho");
//					exc.printStackTrace();
				}
				double pav = 0;
				try {
					pav = (Double) metrosPavimentadosJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros pavimentados");
//					exc.printStackTrace();
				}
				double noPav = 0;
				try {
					noPav = (Double) metrosNoPavimentadosJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros no pavimentados");
//					exc.printStackTrace();
				}

				if (ancho > 0) {
					// Si es urbano ajustamos el Pavimentado
					if (urb) {
						pav = calculaPav(ancho, largo, noPav);
						if (pav > 0) {
							metrosPavimentadosJTField.setNumber(pav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							metrosNoPavimentadosJTField.setNumber(0);
							noPav = 0;
							pav = calculaPav(ancho, largo, noPav);
							metrosPavimentadosJTField.setNumber(pav);
						}
					}
					// Si es rustico ajustamos el NoPavimentado
					if (rus) {
						noPav = calculaPav(ancho, largo, pav);
						if (noPav > 0) {
							metrosNoPavimentadosJTField.setNumber(noPav);
							;
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							metrosPavimentadosJTField.setNumber(0);
							pav = 0;
							noPav = calculaPav(ancho, largo, pav);
							metrosNoPavimentadosJTField.setNumber(noPav);
						}

					}

					calculaRegularizado(pav, noPav);
				} else if (pav > 0 || noPav > 0) {
					ancho = calculaLong(largo, pav, noPav);
					anchoJTField.setNumber(ancho);
				}
			} else {
				longitudJTField.setNumber(largo);
			}

		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}
	class MyFocusListenerAncho implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			// Si el ancho introducido es válido
			double ancho=0;
			try {
				ancho = (Double) anchoJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del ancho");
//				exc.printStackTrace();
			}
			
			if (ancho > 0) {
				// Si long!=null, calculo los metros Pav
				double largo=0;
				try {
					largo = (Double)longitudJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura del largo");
//					exc.printStackTrace();
				}
				double pav=0;
				try {
					pav = (Double) metrosPavimentadosJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los metros pavimentados");
//					exc.printStackTrace();
				}
				double noPav=0;
				try {
					noPav = (Double) metrosNoPavimentadosJTField.getNumber();
				} catch (ParseException exc) {
					logger.error("Fallo en la lectura de los no metros pavimentados");
//					exc.printStackTrace();
				}
				if (largo > 0) {
					// Si es urbano ajustamos el Pavimentado
					if (urb) {
						pav = calculaPav(ancho, largo, noPav);
						if (pav > 0) {
							metrosPavimentadosJTField.setNumber(pav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							metrosNoPavimentadosJTField.setNumber(0);
							noPav=0;
							pav = calculaPav(ancho, largo, noPav);
							metrosPavimentadosJTField.setNumber(pav);
						}
					}
					// Si es rustico ajustamos el NoPavimentado
					if (rus) {
						noPav = calculaPav(ancho, largo, pav);
						if (noPav > 0) {
							metrosNoPavimentadosJTField.setNumber(noPav);
						} else {
							// Si es negativo, ponemos el no pavimentado a 0, y
							// recalculamos el pavimentado
							metrosPavimentadosJTField.setNumber(0);
							pav=0;
							noPav = calculaPav(ancho, largo, pav);
							metrosNoPavimentadosJTField.setNumber(noPav);
						}

					}

					calculaRegularizado(pav, noPav);
				} else if (pav > 0 || noPav > 0) {
					largo = calculaLong(ancho, pav, noPav);
					longitudJTField.setNumber(largo);
				}
			} else {
				anchoJTField.setNumber(ancho);
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}
	

	class MyFocusListenerPav implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {

			double ancho = 0;
			try {
				ancho = (Double) anchoJTField.getNumber();
			} catch (ParseException exc) {
				logger.error("Fallo en la lectura del ancho");
//				exc.printStackTrace();
			}
			double largo = 0;
			try {
				largo = (Double) longitudJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura del largo");
//				e1.printStackTrace();
			}
			double pav = 0;
			try {
				pav = (Double) metrosPavimentadosJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura de los metros pavimentados");
//				e1.printStackTrace();
			}
			double noPav = 0;
			try {
				noPav = (Double) metrosNoPavimentadosJTField.getNumber();
			} catch (ParseException e1) {
				logger.error("Fallo en la lectura de los no metros pavimentados");
//				e1.printStackTrace();
			}

			if (ancho > 0) {
				if (largo > 0) {
					ancho = calculaLong(largo, pav, noPav);
					anchoJTField.setNumber(ancho);
				} else {
					largo = calculaLong(ancho, pav, noPav);
					longitudJTField.setNumber(largo);
				}
			} else if (largo > 0) {
				ancho = calculaLong(largo, pav, noPav);
				anchoJTField.setNumber(ancho);
			}

			calculaRegularizado(pav, noPav);

		}

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public double calculaLong(double ancho, double pav, double noPav) {
		double num = 0;
		num = (pav + noPav) / ancho;

		return num;
	}

	public double calculaPav(double ancho, double largo, double metros) {
		double num = 0;
		num = (ancho * largo) - metros;
		return num;
	}

	private void calculaRegularizado(double dato1, double dato2) {
		if (valorMetro == 0)
			calculaValorMetro();
		double res = (dato1 + dato2) * valorMetro;
		valorActualJTField.setNumber(res);
	}
	
	 private void calculaValorMetro() {
	    	try{
	    		if (urb) //Valor del metro Urbano
	    			valorMetro= Double.valueOf(aplicacion.getI18nString("inventario.datosValoracion.tag26"));
	    		else if (rus)//Valor del metro Rustico
	    			valorMetro= Double.valueOf(aplicacion.getI18nString("inventario.datosValoracion.tag27"));
	    		else
	    			valorMetro=0;
	    	}catch(Exception e){
	    		logger.error("No se ha podido convertir el valor del metro cuadrado"+e.getMessage());
	    		e.printStackTrace();
	    	}
			
		}

}
