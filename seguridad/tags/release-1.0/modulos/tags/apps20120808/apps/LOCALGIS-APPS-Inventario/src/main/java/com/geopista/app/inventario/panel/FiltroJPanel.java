package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.inventario.*;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.inventario.CampoFiltro;
import com.geopista.protocol.inventario.Const;

import javax.swing.*;
import java.util.Collection;
import java.util.ArrayList;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-oct-2006
 * Time: 10:14:04
 * To change this template use File | Settings | File Templates.
 */
public class FiltroJPanel extends JPanel{
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String tipo;
    private String subTipo;

    private JPanel filtroJPanel;
    private ArrayList operadores;
    private ArrayList campos;
    private ListaPane filtroListaPane;
    private CampoFiltro campoFiltroSelected;

    /**
     * Método que genera el panel de filtro
     */
    public FiltroJPanel(JFrame desktop, String tipo, String subTipo, String locale, boolean showAplicarFiltro) {
    	   aplicacion= (AppContext) AppContext.getApplicationContext();
           this.desktop= desktop;
           this.locale= locale;
           this.tipo= tipo;
           this.subTipo = subTipo;
           initAllCampos();
           initComponents(showAplicarFiltro);
           renombrarComponentes();
    }
    /**
     * Método que genera el panel de filtro
     */
    public FiltroJPanel(JFrame desktop, String tipo, String subTipo, String locale) {
    	this(desktop, tipo, subTipo, locale, true); 
    }

    private void initComponents(boolean showAplicarFiltro) {
        valorJTField = new com.geopista.app.utilidades.TextField(254);
        valorJTField.setEnabled(false);
        valorJTField.setVisible(true);

        valorComponent= valorJTField;

        valorFJTField= new JFormattedTextField(Constantes.df);
        valorFJTField.setEnabled(false);
        valorFJTField.setVisible(false);
        fechaJButton= new CalendarButton(valorFJTField);
        fechaJButton.setEnabled(false);
        fechaJButton.setVisible(false);

        valorEJCBox= new ComboBoxEstructuras(/*con blanco*/true);
        valorEJCBox.setEnabled(false);
        valorEJCBox.setVisible(false);

        valorDJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999999), true, 2);
        valorDJTField.setEnabled(false);
        valorDJTField.setVisible(false);

        valorNJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(999999999), true, 2);
        valorNJTField.setEnabled(false);
        valorNJTField.setVisible(false);

        valorBJCBox= new JComboBox();
        valorBJCBox.addItem("");
        valorBJCBox.addItem("true");
        valorBJCBox.addItem("false");
        valorBJCBox.setEnabled(false);
        valorBJCBox.setVisible(false);

        filtroListaPane= new ListaPane(null);
        filtroListaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtroListaPaneActionPerformed();
            }
        });

        addJButton= new JButton();
        addJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed();
            }
        });
        addJButton.setEnabled(true);

        removeJButton= new JButton();
        removeJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBorrar);
        removeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJButtonActionPerformed();
            }
        });
        removeJButton.setEnabled(false);

        clearJButton= new JButton();
        clearJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoClear);
        clearJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearJButtonActionPerformed();
            }
        });
        clearJButton.setEnabled(true);


        campoJCBox= new CamposFiltroJComboBox(campos.toArray(), null, true);
        campoJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoJCBoxActionPerformed();
            }
        });
        campoJCBox.setKeySelectionManager(new ComboBoxSelectionManager());
        campoJCBox.setFocusCycleRoot(true);

        operadorJCBox= new JComboBox();
        operadorJCBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                operadorJCBoxActionPerformed();
            }
        });
        operadorJCBox.setEnabled(false);

        aplicarFiltroJCBox= new javax.swing.JCheckBox();
        aplicarFiltroJCBox.setSelected(showAplicarFiltro);

        filtroJPanel= new JPanel();
        filtroJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        filtroJPanel.add(campoJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 20));
        filtroJPanel.add(operadorJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 160, 20));
        /** en la misma posicion. En funcion del campo mostramos el componente correspondiente */
        filtroJPanel.add(valorEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(valorFJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(valorDJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(valorNJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(valorBJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 160, 20));
        filtroJPanel.add(fechaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 20, 20));
        filtroJPanel.add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 20, 20));

        JPanel filtroListaPaneJPanel= new JPanel();
        filtroListaPaneJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        filtroListaPaneJPanel.add(filtroListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 620, 250));
        filtroListaPaneJPanel.add(removeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 20, 20));
        filtroListaPaneJPanel.add(clearJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 20, 20));
        if (showAplicarFiltro)
        	filtroListaPaneJPanel.add(aplicarFiltroJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 620, 20));

        setLayout(new java.awt.BorderLayout());
        add(filtroJPanel, BorderLayout.NORTH);
        add(filtroListaPaneJPanel, BorderLayout.CENTER);

    }

    private void filtroListaPaneActionPerformed(){
        campoFiltroSelected= (CampoFiltro)filtroListaPane.getSelected();
        if (campoFiltroSelected != null){
            removeJButton.setEnabled(true);
        }
    }


    private void addJButtonActionPerformed(){
        if (campoJCBox.getSelectedIndex() == 0){
            campoJCBox.requestFocusInWindow();
            return;
        }
        if (operadorJCBox.getSelectedIndex() == 0){
            operadorJCBox.requestFocusInWindow();
            return;
        }

        filtroListaPane.clearSeleccion();
        removeJButton.setEnabled(false);

        campoFiltroSelected= (CampoFiltro)campoJCBox.getSelectedItem();
        campoFiltroSelected.setOperador((String)operadorJCBox.getSelectedItem());

        if (valorComponent instanceof ComboBoxEstructuras) {
            if (((ComboBoxEstructuras)valorComponent).getSelectedPatron() == null){
                valorComponent.requestFocusInWindow();
                return;
            }
            campoFiltroSelected.setValorVarchar(((ComboBoxEstructuras)valorComponent).getSelectedPatron());
            
            campoFiltroSelected.setValorTerm(getTerm());
           
        }else if (valorComponent instanceof JFormattedTextField){
            if (((JFormattedTextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                valorComponent.requestFocusInWindow();
                return;
            };
            try{campoFiltroSelected.setValorDate(Constantes.df.parse(((JFormattedTextField)valorComponent).getText().trim()));}catch(java.text.ParseException e){}
            campoFiltroSelected.setValorVarchar(((JFormattedTextField)valorComponent).getText().trim());
        }else if (valorComponent instanceof JNumberTextField){
            if (((JNumberTextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                valorComponent.requestFocusInWindow();
                return;
            };
            if (campoFiltroSelected.isDouble()){
                try{campoFiltroSelected.setValorDouble(((Double)((JNumberTextField)valorComponent).getNumber()).doubleValue());}catch(Exception e){}
            }else try{campoFiltroSelected.setValorNumeric(((Long)((JNumberTextField)valorComponent).getNumber()).longValue());}catch(Exception e){}
            campoFiltroSelected.setValorVarchar(((JNumberTextField)valorComponent).getText().trim());
        }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
            if (((com.geopista.app.utilidades.TextField)valorComponent).getText().trim().equalsIgnoreCase("")){
                valorComponent.requestFocusInWindow();
                return;
            };
            if (campoFiltroSelected.getOperador().equalsIgnoreCase("LIKE"))
                campoFiltroSelected.setValorVarchar("%"+((com.geopista.app.utilidades.TextField)valorComponent).getText().trim()+"%");
            else campoFiltroSelected.setValorVarchar(((com.geopista.app.utilidades.TextField)valorComponent).getText().trim());
        }else if (valorComponent instanceof JComboBox){
            if (((JComboBox)valorComponent).getSelectedIndex() == 0){
                valorComponent.requestFocusInWindow();
                return;
            }
            campoFiltroSelected.setValorVarchar((String)((JComboBox)valorComponent).getSelectedItem());
            campoFiltroSelected.setValorBoolean(((JComboBox)valorComponent).getSelectedIndex()==1?true:false);
        }

        /* añadimos el filtro a la lista */
        filtroListaPane.addOnlyIfNotExist(campoFiltroSelected.clone(), /*no seleccionar*/false);

        clear();
        operadorJCBox.setEnabled(false);
        valorComponent.setEnabled(false);
        fechaJButton.setEnabled(false);

    }

    private String getTerm(){
    	DomainNode nodoSelected=(DomainNode)((ComboBoxEstructuras)valorComponent).getSelectedItem();
    	return nodoSelected.getTerm(locale);
    	
    }

    private void removeJButtonActionPerformed(){
        if (filtroListaPane.getSelected() == null) return;

        filtroListaPane.borrar();
        removeJButton.setEnabled(false);
        addJButton.setEnabled(true);
    }


    private void clearJButtonActionPerformed(){
        filtroListaPane.setCollection(null);
        filtroListaPane.actualizarModelo();
    }

    private void campoJCBoxActionPerformed(){
        if (campoJCBox.getSelectedIndex()!=0){
            removeJButton.setEnabled(false);
            filtroListaPane.clearSeleccion();

            campoFiltroSelected= (CampoFiltro)campoJCBox.getSelectedItem();
            /** cargamos los operadores y el campo valor segun el campo seleccionado. */
            if (!campoFiltroSelected.isDate()){
                fechaJButton.setVisible(false);
                fechaJButton.setEnabled(false);

                if (campoFiltroSelected.isVarchar()) {
                   valorComponent= valorJTField;
                   cargarOperadoresVarchar();
                }else if (campoFiltroSelected.isBoolean()){
                   valorComponent= valorBJCBox;
                   cargarOperadoresBoolean();
                }else if (campoFiltroSelected.isDominio()){
                   valorComponent= valorEJCBox;
                   cargarDominio(campoFiltroSelected.getDominio());
                   cargarOperadoresDominio();
                }else if (campoFiltroSelected.isDouble()){
                   valorComponent= valorDJTField;
                   cargarOperadoresNumericDouble();
                }else if (campoFiltroSelected.isNumeric()){
                    valorComponent= valorNJTField;
                    cargarOperadoresNumericDouble();
                }
            }else{
                valorComponent= valorFJTField;
                fechaJButton.setVisible(true);
                cargarOperadoresDate();
            }

            setVisible();
            filtroJPanel.repaint();
            filtroJPanel.invalidate();
            filtroJPanel.validate();

            operadorJCBox.setEnabled(true);
            operadorJCBox.requestFocusInWindow();

        }
    }

    private void setVisible(){
        if (valorComponent instanceof ComboBoxEstructuras) {
            valorEJCBox.setVisible(true);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof JFormattedTextField){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(true);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof JNumberTextField){
            if (campoFiltroSelected.isNumeric()){
                valorNJTField.setVisible(true);
                valorDJTField.setVisible(false);
            }else{
                valorNJTField.setVisible(false);
                valorDJTField.setVisible(true);
            }
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(false);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(true);
        }else if (valorComponent instanceof JComboBox){
            valorEJCBox.setVisible(false);
            valorBJCBox.setVisible(true);
            valorNJTField.setVisible(false);
            valorDJTField.setVisible(false);
            valorFJTField.setVisible(false);
            valorJTField.setVisible(false);
        }
    }

    private void operadorJCBoxActionPerformed(){
        if (operadorJCBox.getSelectedIndex()!=0 && campoFiltroSelected!=null){
            addJButton.setEnabled(true);
            if (!campoFiltroSelected.isDate()){
                valorComponent.setEnabled(true);
                valorComponent.requestFocusInWindow();
            }else{
                fechaJButton.setEnabled(true);
            }
        }
    }

    private void clear(){
        campoJCBox.setSelectedIndex(0);
        try{operadorJCBox.setSelectedIndex(0);}catch(Exception e){}

        if (valorComponent instanceof ComboBoxEstructuras) {
            ((ComboBoxEstructuras)valorComponent).setSelectedPatron(null);
        }else if (valorComponent instanceof JFormattedTextField){
            ((JFormattedTextField)valorComponent).setText("");
        }else if (valorComponent instanceof JNumberTextField){
            ((JNumberTextField)valorComponent).setText("");
        }else if (valorComponent instanceof com.geopista.app.utilidades.TextField){
            ((com.geopista.app.utilidades.TextField)valorComponent).setText("");
        }else if (valorComponent instanceof JComboBox){
            try{((JComboBox)valorComponent).setSelectedIndex(0);}catch(Exception e){}
        }

    }


    private void renombrarComponentes(){
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.filtrar.tag4"));}catch(Exception e){}
        try{removeJButton.setToolTipText(aplicacion.getI18nString("inventario.filtrar.tag5"));}catch(Exception e){}
        try{aplicarFiltroJCBox.setText(aplicacion.getI18nString("inventario.filtrar.tag6"));}catch(Exception e){}
        try{clearJButton.setToolTipText(aplicacion.getI18nString("inventario.filtrar.tag7"));}catch(Exception e){}

    }

    private void initOperadoresVarchar(){
        operadores= new ArrayList();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add("LIKE");
    }

    private void initOperadoresNumericDouble(){
        operadores= new ArrayList();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add(">");
        operadores.add(">=");
        operadores.add("<");
        operadores.add("<=");
    }

    private void initOperadoresDate(){
        operadores= new ArrayList();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
        operadores.add(">");
        operadores.add(">=");
        operadores.add("<");
        operadores.add("<=");
        //operadores.add("BETWEEN");
    }

    private void initOperadoresBoolean(){
        operadores= new ArrayList();
        operadores.add("");
        operadores.add("=");
    }

    private void initOperadoresDominio(){
        operadores= new ArrayList();
        operadores.add("");
        operadores.add("=");
        operadores.add("<>");
    }


    private void cargarOperadoresNumericDouble(){
        initOperadoresNumericDouble();
        operadorJCBox.removeAllItems();
        operadorJCBox.addItem((String)operadores.get(0));
        operadorJCBox.addItem((String)operadores.get(1));
        operadorJCBox.addItem((String)operadores.get(2));
        operadorJCBox.addItem((String)operadores.get(3));
        operadorJCBox.addItem((String)operadores.get(4));
        operadorJCBox.addItem((String)operadores.get(5));
        operadorJCBox.addItem((String)operadores.get(6));
    }

    private void cargarOperadoresVarchar(){
        initOperadoresVarchar();
        operadorJCBox.removeAllItems();
        operadorJCBox.addItem((String)operadores.get(0));
        operadorJCBox.addItem((String)operadores.get(1));
        operadorJCBox.addItem((String)operadores.get(2));
        operadorJCBox.addItem((String)operadores.get(3));
    }

    private void cargarOperadoresDominio(){
        initOperadoresDominio();
        operadorJCBox.removeAllItems();
        operadorJCBox.addItem((String)operadores.get(0));
        operadorJCBox.addItem((String)operadores.get(1));
        operadorJCBox.addItem((String)operadores.get(2));
    }

    private void cargarDominio(int dominio){
        valorEJCBox.removeAllItems();
        switch (dominio){
            case CamposBusquedaFiltro.UsoJuridico:
                valorEJCBox.setEstructuras(Estructuras.getListaUsoJuridico(), null, locale, true);
                break;
            case CamposBusquedaFiltro.FormaAdquisicion:
                valorEJCBox.setEstructuras(Estructuras.getListaFormaAdquisicion(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Propiedad:
                valorEJCBox.setEstructuras(Estructuras.getListaPropiedadPatrimonial(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Aprovechamiento:
                valorEJCBox.setEstructuras(Estructuras.getListaAprovechamiento(), null, locale, true);
                break;
            case CamposBusquedaFiltro.EstadoConservacion:
                valorEJCBox.setEstructuras(Estructuras.getListaEstadoConservacion(), null, locale, true);
                break;
            case CamposBusquedaFiltro.TipoConstruccion:
                valorEJCBox.setEstructuras(Estructuras.getListaTipoConstruccion(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Fachada:
                valorEJCBox.setEstructuras(Estructuras.getListaTipoFachada(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Cubierta:
                valorEJCBox.setEstructuras(Estructuras.getListaTipoCubierta(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Carpinteria:
                valorEJCBox.setEstructuras(Estructuras.getListaTipoCarpinteria(), null, locale, true);
                break;
            case CamposBusquedaFiltro.TipoAmortizacion:
                valorEJCBox.setEstructuras(Estructuras.getListaTipoAmortizacion(), null, locale, true);
                break;
            case CamposBusquedaFiltro.UsoFuncional:
                valorEJCBox.setEstructuras(Estructuras.getListaUsosFuncionales(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Documentos:
                valorEJCBox.setEstructuras(Estructuras.getListaDocumentos(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Concepto:
                valorEJCBox.setEstructuras(Estructuras.getListaConceptosCreditosDerechos(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Raza:
                valorEJCBox.setEstructuras(Estructuras.getListaRazaSemoviente(), null, locale, true);
                break;
            case CamposBusquedaFiltro.CodigoVia:
                valorEJCBox.setEstructuras(Estructuras.getListaTiposViaINE(), null, locale, true);
                break;
            case CamposBusquedaFiltro.TipoVehiculo:
                valorEJCBox.setEstructuras(Estructuras.getListaTiposVehiculo(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Traccion:
                valorEJCBox.setEstructuras(Estructuras.getListaTraccion(), null, locale, true);
                break;
            case CamposBusquedaFiltro.ClaseValorMobiliario:
                valorEJCBox.setEstructuras(Estructuras.getListaClasesValorMobiliario(), null, locale, true);
                break;
            case CamposBusquedaFiltro.CatTransmision:
                valorEJCBox.setEstructuras(Estructuras.getListaTransmision(), null, locale, true);
                break;
            case CamposBusquedaFiltro.Arrendamiento:
            	DomainNode siDn= new DomainNode();
            	siDn.setPatron("1");
            	siDn.addTerm("es_ES","Si");
            	DomainNode noDn= new DomainNode();
            	noDn.setPatron("0");
            	noDn.addTerm("es_ES","No");
            	valorEJCBox.addItem(new DomainNode());
            	valorEJCBox.addItem(siDn);
            	valorEJCBox.addItem(noDn);
            	break;
            case CamposBusquedaFiltro.ClaseCredito:
                valorEJCBox.setEstructuras(Estructuras.getListaClaseCredito(), null, locale, true);
                break;
            case CamposBusquedaFiltro.SubclaseCredito:
                valorEJCBox.setEstructuras(Estructuras.getListaSubclaseCredito(), null, locale, true);
                break;
            case CamposBusquedaFiltro.ClaseDchoReales:
                valorEJCBox.setEstructuras(Estructuras.getListaClaseDerechosReales(), null, locale, true);
                break;
            case CamposBusquedaFiltro.ClaseMuebles:
                valorEJCBox.setEstructuras(Estructuras.getListaClaseMuebles(), null, locale, true);
                break;
            case CamposBusquedaFiltro.ClaseUrbano:
                valorEJCBox.setEstructuras(Estructuras.getListaClaseUrbana(), null, locale, true);
                break;
            case CamposBusquedaFiltro.ClaseRustico:
                valorEJCBox.setEstructuras(Estructuras.getListaClaseRustica(), null, locale, true);
                break;
        }
    }


    private void cargarOperadoresDate(){
        initOperadoresDate();
        operadorJCBox.removeAllItems();
        operadorJCBox.addItem((String)operadores.get(0));
        operadorJCBox.addItem((String)operadores.get(1));
        operadorJCBox.addItem((String)operadores.get(2));
        operadorJCBox.addItem((String)operadores.get(3));
        operadorJCBox.addItem((String)operadores.get(4));
        operadorJCBox.addItem((String)operadores.get(5));
        operadorJCBox.addItem((String)operadores.get(6));
        //operadorJCBox.addItem((String)operadores.get(7));
    }

    private void cargarOperadoresBoolean(){
        initOperadoresBoolean();
        operadorJCBox.removeAllItems();
        operadorJCBox.addItem((String)operadores.get(0));
        operadorJCBox.addItem((String)operadores.get(1));
    }


    private void initAllCampos(){
    	if (tipo==null) return;
    	if (tipo.equals(Const.SUPERPATRON_REVERTIBLES))
    	{
    	    campos= CamposBusquedaFiltro.getCamposBienesRevertibles();
            return;
    	}
    	if (tipo.equals(Const.SUPERPATRON_LOTES)){
      	    campos= CamposBusquedaFiltro.getCamposLotes();
    		return;
    	}
    	
    	if (subTipo==null) return;
        if (subTipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_URBANOS)){
            campos= CamposBusquedaFiltro.getCamposInmuebleUrbano();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_INMUEBLES_RUSTICOS)){
            campos= CamposBusquedaFiltro.getCamposInmuebleRustico();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_BIENES_MUEBLES)){
            campos= CamposBusquedaFiltro.getCamposBienesMueble();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_MUEBLES_HISTORICOART)){
            campos= CamposBusquedaFiltro.getCamposMuebleHistArt();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_DERECHOS_REALES)){
            campos= CamposBusquedaFiltro.getCamposDerechosReales();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
            campos= CamposBusquedaFiltro.getCamposCreditosDerechos();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_SEMOVIENTES)){
            campos= CamposBusquedaFiltro.getCamposSemovientes();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_URBANAS)){
            campos= CamposBusquedaFiltro.getCamposViaUrbana();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
            campos= CamposBusquedaFiltro.getCamposViaRustica();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_VEHICULOS)){
            campos= CamposBusquedaFiltro.getCamposVehiculo();
        }else if (subTipo.equalsIgnoreCase(Const.PATRON_VALOR_MOBILIARIO)){
            campos= CamposBusquedaFiltro.getCamposValorMobiliario();
        }
    }

    public void load(Collection filtro, boolean aplicar){
        filtroListaPane.setCollection(filtro);
        filtroListaPane.actualizarModelo();
        aplicarFiltroJCBox.setSelected(aplicar);
    }

    public void setVisibleAplicarFiltro(boolean b) {
        aplicarFiltroJCBox.setVisible(b);
    }

    public Collection getFiltro(){
        return filtroListaPane.getCollection();
    }

    public boolean getAplicarFiltro() {
        return aplicarFiltroJCBox.isSelected();
    }

    private CamposFiltroJComboBox campoJCBox;
    private javax.swing.JComboBox operadorJCBox;
    private Component valorComponent;
    private javax.swing.JComboBox valorBJCBox;
    private ComboBoxEstructuras valorEJCBox;
    private JFormattedTextField valorFJTField;
    private com.geopista.app.utilidades.JNumberTextField valorNJTField;
    private com.geopista.app.utilidades.JNumberTextField valorDJTField;
    private com.geopista.app.utilidades.TextField valorJTField;
    private javax.swing.JButton addJButton;
    private javax.swing.JButton removeJButton;
    private CalendarButton fechaJButton;
    private javax.swing.JCheckBox aplicarFiltroJCBox;
    private javax.swing.JButton clearJButton;


}
