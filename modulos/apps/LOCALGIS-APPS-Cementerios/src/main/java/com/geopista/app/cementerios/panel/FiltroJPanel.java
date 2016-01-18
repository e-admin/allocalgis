/**
 * FiltroJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.CamposFiltroJComboBox;
import com.geopista.app.cementerios.ComboBoxSelectionManager;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.cementerios.Estructuras;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.cementerios.CampoFiltro;
import com.geopista.protocol.cementerios.Const;


public class FiltroJPanel extends JPanel {
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String tipo;
    private String subtipo;
    private JPanel filtroJPanel;
    private ArrayList operadores;
    private ArrayList campos;
    private ListaPane filtroListaPane;
    private CampoFiltro campoFiltroSelected;
    

    /**
     * Método que genera el panel de filtro
     */
    public FiltroJPanel(JFrame desktop, String tipo, String subtipo, ArrayList campos,  String locale) {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= desktop;
        this.locale= locale;
        this.tipo= tipo;
        this.subtipo=subtipo;
        this.campos = campos;
        initComponents();
        renombrarComponentes();
        
       
    }

    private void initComponents() {
        valorJTField = new com.geopista.app.utilidades.TextField(254);
        valorJTField.setEnabled(false);
        valorJTField.setVisible(true);

        valorComponent= valorJTField;

        valorFJTField= new JFormattedTextField(Constantes.df);
        valorFJTField.setText(Constantes.df.format(new Date()));
        valorFJTField.setEnabled(false);
        valorFJTField.setVisible(false);
        fechaJButton= new CalendarButton(valorFJTField);
        fechaJButton.setEnabled(false);
        fechaJButton.setVisible(false);

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
        addJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed();
            }
        });
        addJButton.setEnabled(true);

        removeJButton= new JButton();
        removeJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoBorrar);
        removeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJButtonActionPerformed();
            }
        });
        removeJButton.setEnabled(false);

        clearJButton= new JButton();
        clearJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoClear);
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
        aplicarFiltroJCBox.setSelected(true);

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
        String term= "";
        switch (campoFiltroSelected.getDominio()){
	        case Const.unidadEnterramiento:
	            term= Estructuras.getListaComboTipoUnidades().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	            break;
	    	case Const.bloque:
	    		term= Estructuras.getListaComboTipoUnidades().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	    	case Const.concesion:
	    		term= Estructuras.getListaComboTipoConcesiones().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	    	case Const.inhumacion:
	    		term= Estructuras.getListaComboTipoInhumaciones().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	    	case Const.exhumacion:
	    		term= Estructuras.getListaComboTipoExhumaciones().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	    	case Const.contenedor:
	    		term= Estructuras.getListaComboTipoContenedores().getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	    	case Const.servicios:
	    		term= Estructuras.getListaCombosServicios(locale).getDomainNode(((ComboBoxEstructuras)valorComponent).getSelectedPatron()).getTerm(locale);
	        break;
	        	        
        }
        
        return term;
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
        if (operadorJCBox.getSelectedIndex()!=0){
            addJButton.setEnabled(true);
            if (!campoFiltroSelected.isDate()){
                valorComponent.setEnabled(true);
                valorComponent.requestFocusInWindow();
            }
            else{
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
        try{addJButton.setToolTipText(aplicacion.getI18nString("cementerio.filtrar.tag4"));}catch(Exception e){}
        try{removeJButton.setToolTipText(aplicacion.getI18nString("cementerio.filtrar.tag5"));}catch(Exception e){}
        try{aplicarFiltroJCBox.setText(aplicacion.getI18nString("cementerio.filtrar.tag6"));}catch(Exception e){}
        try{clearJButton.setToolTipText(aplicacion.getI18nString("cementerio.filtrar.tag7"));}catch(Exception e){}

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
        case Const.unidadEnterramiento:
        	valorEJCBox.setEstructuras(Estructuras.getListaComboTipoUnidades(), null, locale, true);
        	break;
    	case Const.bloque:
    		valorEJCBox.setEstructuras(Estructuras.getListaCombosBloque(locale), null, locale, true);
    	break;
    	case Const.concesion:
    		valorEJCBox.setEstructuras(Estructuras.getListaComboTipoConcesiones(), null, locale, true);
    	break;
    	case Const.contenedor:
    		valorEJCBox.setEstructuras(Estructuras.getListaComboTipoContenedores(), null, locale, true);    		
    	break;
    	case Const.inhumacion:
    		valorEJCBox.setEstructuras(Estructuras.getListaComboTipoInhumaciones(), null, locale, true);    		
    	break;
    	case Const.exhumacion:
    		valorEJCBox.setEstructuras(Estructuras.getListaComboTipoExhumaciones(), null, locale, true);    		
    	break;
    	case Const.servicios:
    		valorEJCBox.setEstructuras(Estructuras.getListaCombosServicios(locale), null, locale, true);    		
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
