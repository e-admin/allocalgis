package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;
import com.geopista.protocol.inventario.*;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.util.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 02-ago-2006
 * Time: 11:57:35
 * To change this template use File | Settings | File Templates.
 */
public class DatosSegurosJPanel  extends JPanel{
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;
    private Seguro seguro;
    private SegurosJDialog segurosJDialog;
    private String operacion;
    /**
     * Método que genera el panel de los datos de Seguros de un bien inmueble
     */
    public DatosSegurosJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

	public void setOperacion(String operacion) {
		this.operacion = operacion;
		if (operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)) 
        	setEnabledBotonera(false);
        else{
            addJButtonSetEnabled(true);
            buscarJButtonSetEnabled(true);
            editarJButtonSetEnabled(seguro!=null);
            borrarJButtonSetEnabled(seguro!=null);
        }
        
	}

	private void initComponents(){
        companiaJLabel= new InventarioLabel();
        descripcionSeguroJLabel= new InventarioLabel();
        primaJLabel= new InventarioLabel();
        fechaInicioJLabel= new InventarioLabel();
        polizaJLabel= new InventarioLabel();
        fechaVencimientoJLabel= new InventarioLabel();
        descripcionCompanniaJLabel= new InventarioLabel();

        fechaInicioJTField= new JFormattedTextField(Constantes.df);
        fechaInicioJTField.setEnabled(false);
        fechaInicioJButton= new CalendarButton(fechaInicioJTField);
        companiaJTField= new com.geopista.app.utilidades.TextField(254);
        descSeguroJScrollPane= new javax.swing.JScrollPane();
        descripcionSeguroJTArea= new InventarioTextPane(254);
        descCompanniaJScrollPane= new javax.swing.JScrollPane();
        descripcionCompanniaJTArea= new InventarioTextPane(254);
        primaJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        primaJTField.setSignAllowed(false);
 //       polizaJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        polizaJTField = new com.geopista.app.utilidades.TextField(14, true);

//        polizaJTField.setSignAllowed(false);
        fechaVencimientoJTField= new JFormattedTextField(Constantes.df);
        fechaVencimientoJTField.setEnabled(false);
        fechaVencimientoJButton= new CalendarButton(fechaVencimientoJTField);
        buscarJButton= new JButton();
        buscarJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBuscar);
        buscarJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirDialogoJButtonActionPerformed();
            }
        });
        addJButton= new JButton();
        addJButton.setEnabled(false);
        addJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCompaniaJButtonActionPerformed();
            }
        });

        borrarJButton= new JButton();
        borrarJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBorrar);
        borrarJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCompaniaJButtonActionPerformed();
            }
        });

        editarJButton= new JButton();
        editarJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoEditar);
        editarJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCompaniaJButtonActionPerformed();
            }
        });
        editarJButton.setEnabled(false);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(companiaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(descripcionCompanniaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 430, 20));
        add(descripcionSeguroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 430, 20));
        add(fechaInicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 110, 20));
        add(fechaVencimientoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 110, 20));
        add(primaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 110, 20));
        add(polizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 110, 20));


        add(companiaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 230, -1));
        //add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 30, 20, 20));
        add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 20, 20));
        add(editarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 20, 20));
        add(borrarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        descCompanniaJScrollPane.setViewportView(descripcionCompanniaJTArea);
        add(descCompanniaJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 430, 40));

        descSeguroJScrollPane.setViewportView(descripcionSeguroJTArea);
        add(descSeguroJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 430, 40));
        add(fechaInicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 140, -1));
        add(fechaInicioJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 20, 20));
        add(fechaVencimientoJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 140, -1));
        add(fechaVencimientoJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 20, 20));
        add(primaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 160, -1));
        add(polizaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 160, -1));

    }


    public void addCompaniaJButtonActionPerformed(){
        clear();
        setEnabledDatos(true);
        companiaJTField.requestFocusInWindow();
        borrarJButtonSetEnabled(true);
        editarJButtonSetEnabled(false);
    }

    public void deleteCompaniaJButtonActionPerformed(){
        seguro= null;
        editarJButtonSetEnabled(false);
        borrarJButtonSetEnabled(false);
        clear();
    }

    public void editarCompaniaJButtonActionPerformed(){
        descripcionSeguroJTArea.requestFocusInWindow();
        setEnabledDatos(true);
    }


    private void abrirDialogoJButtonActionPerformed() {
        try{
            segurosJDialog= new SegurosJDialog();
            segurosJDialog.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    segurosJDialog_actionPerformed();
                }
            });
            segurosJDialog.setVisible(true);
        }catch(Exception e){
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"), aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
        }
    }

    private void segurosJDialog_actionPerformed(){
        load(segurosJDialog.getCompannia());
        if (seguro.getCompannia() != null){
            setEnabledDatos(false);
            setEnabledBotonera(true);
        }

        segurosJDialog.dispose();
    }

    public void load(CompanniaSeguros compania){
    	seguro.setCompannia(compania);
        if (compania==null) return;
        companiaJTField.setText(compania.getNombre()!=null?compania.getNombre().trim():"");
        descripcionCompanniaJTArea.setText(compania.getDescripcion()!=null?compania.getDescripcion().trim():"");

    }

    public void load(BienBean bien){
        if (bien == null) return;
        load(bien.getSeguro());
    }
    
    public void load(BienRevertible bien){
        if (bien == null) return;
        load(bien.getSeguro());
    }
    
    public void load(Lote bien){
        if (bien == null) return;
        load(bien.getSeguro());
    }
    
    public void load (Seguro seguro){
        if (seguro == null){
        	this.seguro=new Seguro();
        	return;
        }
    	this.seguro= seguro;
        try{polizaJTField.setText(seguro.getPoliza().toString());}catch(Exception e){}
        try{primaJTField.setNumber(seguro.getPrima());}catch(Exception e){}
        try{fechaInicioJTField.setText(Constantes.df.format(seguro.getFechaInicio()));}catch(Exception e){fechaInicioJTField.setText("");}
        try{fechaVencimientoJTField.setText(Constantes.df.format(seguro.getFechaVencimiento()));}catch(Exception e){fechaVencimientoJTField.setText("");}
        descripcionSeguroJTArea.setText(seguro.getDescripcion()!=null?seguro.getDescripcion().trim():"");
        load(seguro.getCompannia());
    }

    /**
     * Método que actualiza los datos de los seguros de un bien de inventario
     * @param obj a actualizar sus datos de seguros
     */
    public void actualizarDatos(Object obj){
        if (obj == null) return;
        if (obj instanceof BienBean){
            if (seguro==null && !companiaJTField.getText().trim().equalsIgnoreCase("")) seguro= new Seguro();
            ((BienBean)obj).setSeguro(seguro);
        }
        if (obj instanceof BienRevertible){
            if (seguro==null && !companiaJTField.getText().trim().equalsIgnoreCase("")) seguro= new Seguro();
            ((BienRevertible)obj).setSeguro(seguro);
        }
        if (obj instanceof Lote){
            if (seguro==null && !companiaJTField.getText().trim().equalsIgnoreCase("")) seguro= new Seguro();
            ((Lote)obj).setSeguro(seguro);
        }
        if (seguro != null){
            if (seguro.getCompannia()== null) seguro.setCompannia(new CompanniaSeguros());
            seguro.getCompannia().setNombre(companiaJTField.getText().trim());
            seguro.getCompannia().setDescripcion(descripcionCompanniaJTArea.getText().trim());
            try{seguro.setPoliza(Long.parseLong(polizaJTField.getText()));}catch(Exception e){seguro.setPoliza(null);}
            try{seguro.setPrima(((Double)primaJTField.getNumber()).doubleValue());}catch(Exception e){seguro.setPrima(null);}
            try{seguro.setFechaInicio(Constantes.df.parse(fechaInicioJTField.getText().trim()));}catch(java.text.ParseException e){seguro.setFechaInicio(null);}
            try{seguro.setFechaVencimiento(Constantes.df.parse(fechaVencimientoJTField.getText().trim()));}catch(java.text.ParseException e){seguro.setFechaVencimiento(null);}
            seguro.setDescripcion(descripcionSeguroJTArea.getText().trim());
        }
    }


    public void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosSeguros.tag1")));}catch(Exception e){}
        try{companiaJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag2"));}catch(Exception e){}
        try{descripcionSeguroJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag3"));}catch(Exception e){}
        try{primaJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag4"));}catch(Exception e){}
        try{fechaInicioJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag5"));}catch(Exception e){}
        try{polizaJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag6"));}catch(Exception e){}
        try{fechaVencimientoJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag7"));}catch(Exception e){}
        try{buscarJButton.setToolTipText(aplicacion.getI18nString("inventario.datosSeguros.tag8"));}catch(Exception e){}
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.datosSeguros.tag9"));}catch(Exception e){}
        try{borrarJButton.setToolTipText(aplicacion.getI18nString("inventario.datosSeguros.tag10"));}catch(Exception e){}
        try{editarJButton.setToolTipText(aplicacion.getI18nString("inventario.datosSeguros.tag11"));}catch(Exception e){}
        try{descripcionCompanniaJLabel.setText(aplicacion.getI18nString("inventario.datosSeguros.tag12"));}catch(Exception e){}

    }

    public void clearDatosCompannia(){
        companiaJTField.setText("");
        descripcionCompanniaJTArea.setText("");
    }

    public void clearDatosSeguro(){
        descripcionSeguroJTArea.setText("");
        fechaInicioJTField.setText("");
        fechaVencimientoJTField.setText("");
        primaJTField.setText("");
        polizaJTField.setText("");
    }

    public void clear(){
        clearDatosCompannia();
        clearDatosSeguro();
    }

    public void setEnabled(boolean b){
        setEnabledDatos(b);
        setEnabledBotonera(b);
    }


    public void setEnabledDatos(boolean b){
        companiaJTField.setEnabled(b);
        descripcionSeguroJTArea.setEnabled(b);
        descripcionCompanniaJTArea.setEnabled(b);
        primaJTField.setEnabled(b);
        polizaJTField.setEnabled(b);
        fechaInicioJTField.setEnabled(false);
        fechaVencimientoJTField.setEnabled(false);
        fechaInicioJButton.setEnabled(b);
        fechaVencimientoJButton.setEnabled(b);

    }

    public void setEnabledBotonera(boolean b){
        borrarJButton.setEnabled(b);
        editarJButton.setEnabled(b);
        addJButton.setEnabled(b);
        buscarJButton.setEnabled(b);
    }

    public void addJButtonSetEnabled(boolean b){
        addJButton.setEnabled(b);
    }
    public void buscarJButtonSetEnabled(boolean b){
        buscarJButton.setEnabled(b);
    }
    public void borrarJButtonSetEnabled(boolean b){
        borrarJButton.setEnabled(b);
    }
    public void editarJButtonSetEnabled(boolean b){
        editarJButton.setEnabled(b);
    }

    private javax.swing.JLabel companiaJLabel;
    private javax.swing.JLabel descripcionSeguroJLabel;
    private javax.swing.JLabel primaJLabel;
    private javax.swing.JLabel fechaInicioJLabel;
    private javax.swing.JLabel polizaJLabel;
    private javax.swing.JLabel fechaVencimientoJLabel;
    private javax.swing.JLabel descripcionCompanniaJLabel;

    private javax.swing.JTextField companiaJTField;
    private javax.swing.JScrollPane descSeguroJScrollPane;
    private TextPane descripcionSeguroJTArea;
    private TextPane descripcionCompanniaJTArea;
    private javax.swing.JScrollPane descCompanniaJScrollPane;
    private com.geopista.app.utilidades.JNumberTextField primaJTField;
    private JFormattedTextField fechaInicioJTField;
    private JButton fechaInicioJButton;
    private JTextField polizaJTField;
    private JFormattedTextField fechaVencimientoJTField;
    private JButton fechaVencimientoJButton;
    private JButton buscarJButton;
    private JButton addJButton;
    private JButton borrarJButton;
    private JButton editarJButton;



}
