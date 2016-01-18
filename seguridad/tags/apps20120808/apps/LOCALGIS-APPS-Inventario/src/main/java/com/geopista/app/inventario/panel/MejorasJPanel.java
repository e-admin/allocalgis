package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.TextPane;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.inventario.Mejora;
import com.geopista.protocol.inventario.BienBean;

import javax.swing.*;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 09-ago-2006
 * Time: 11:53:18
 * To change this template use File | Settings | File Templates.
 */
public class MejorasJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AppContext aplicacion;

    private ListaPane mejorasListaPane;
    private Mejora selected;
    private String operacion;

    /**
     * Método que genera el panel de las Mejoras de un bien
     */
    public MejorasJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    public void initComponents() throws Exception{

        fechaEjecucionJLabel= new InventarioLabel();
        mejorasJLabel= new InventarioLabel();
        importeJLabel= new InventarioLabel();

        mejorasJScrollPane= new javax.swing.JScrollPane();
        mejorasJTArea= new InventarioTextPane(999);
        mejorasJTArea.setEnabled(false);
        fechaEjecucionJTField= new JFormattedTextField(Constantes.df);
        fechaEjecucionJTField.setEnabled(false);
        fechaEjecucionJButton= new CalendarButton(fechaEjecucionJTField);
        fechaEjecucionJButton.setEnabled(false);
        importeJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.CURRENCY, new Long(999999999), true, 2);
        importeJTField.setSignAllowed(false);
        importeJTField.setEnabled(false);

        addJButton= new JButton();
        addJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed();
            }
        });

        deleteJButton= new JButton();
        deleteJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBorrar);
        deleteJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJButtonActionPerformed();
            }
        });
        deleteJButton.setEnabled(false);

        editJButton= new JButton();
        editJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoEditar);
        editJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJButtonActionPerformed();
            }
        });
        editJButton.setEnabled(false);

        okJButton= new JButton();
        okJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoOK);
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed();
            }
        });
        okJButton.setEnabled(false);

        mejorasListaPane= new ListaPane(null);
        mejorasListaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(mejorasListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 400, 350));
        add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        add(deleteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 20, 20));
        add(editJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, 20));
        add(fechaEjecucionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 150, 20));
        add(importeJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 150, 20));
        add(mejorasJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 300, 20));

        add(fechaEjecucionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 140, 20));
        add(fechaEjecucionJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 370, 20, 20));
        add(importeJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 160, 20));
        mejorasJScrollPane.setViewportView(mejorasJTArea);
        add(mejorasJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 400, 60));
        add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 430, 20, 20));

    }

    /**
     * Método que carga la lista de mejoras de un bien
     * @param bien
     */
    public void load(BienBean bien){
        if (bien==null) return;
        mejorasListaPane.setCollection(bien.getMejoras());
        mejorasListaPane.actualizarModelo();
    }

    /**
     * Método que visualiza la mejora seleccionada de la lista
     * @param mejora seleccionada
     */
    public void load(Mejora mejora){
        if (mejora==null) return;
        if (mejora.getImporte()!=-1)
            try{importeJTField.setNumber(new Double(mejora.getImporte()));}catch(Exception e){}
        else importeJTField.setText("");
        try{fechaEjecucionJTField.setText(Constantes.df.format(mejora.getFechaEjecucion()));}catch(Exception e){}
        mejorasJTArea.setText(mejora.getDescripcion());
    }

    public void actualizarDatos(BienBean bien){
        if (bien==null) return;
        bien.setMejoras(mejorasListaPane.getCollection());
    }

    private void listaPaneActionPerformed(){
        clear();
        camposEnabled(false);
        Mejora mejora= (Mejora)mejorasListaPane.getSelected();
        if (mejora!=null && operacion!=null && !operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            editJButton.setEnabled(true);
            deleteJButton.setEnabled(true);
        }
        load(mejora);
    }

    public void clear(){
        fechaEjecucionJTField.setText("");
        mejorasJTArea.setText("");
        importeJTField.setText("");
    }

    public void setEnabled(boolean b){
        addJButton.setEnabled(b);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(false);
        editJButton.setEnabled(false);
    }

    public void renombrarComponentes(){
        renombrarJListComponent();
        try{fechaEjecucionJLabel.setText(aplicacion.getI18nString("inventario.mejoras.tag2"));}catch(Exception e){}
        try{mejorasJLabel.setText(aplicacion.getI18nString("inventario.mejoras.tag3"));}catch(Exception e){}
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.mejoras.tag4"));}catch(Exception e){}
        try{deleteJButton.setToolTipText(aplicacion.getI18nString("inventario.mejoras.tag5"));}catch(Exception e){}
        try{editJButton.setToolTipText(aplicacion.getI18nString("inventario.mejoras.tag6"));}catch(Exception e){}
        try{okJButton.setToolTipText(aplicacion.getI18nString("inventario.mejoras.tag7"));}catch(Exception e){}
        try{importeJLabel.setText(aplicacion.getI18nString("inventario.mejoras.tag10"));}catch(Exception e){}
    }

    /**
     * Método que renombra el panel de la lista
     */
    public void renombrarJListComponent(){
        try{mejorasListaPane.renombrar(aplicacion.getI18nString("inventario.mejoras.tag1"));}catch(Exception e){};
    }

    /**
     * Método que recoge la mejora añadida por el usuario
     */
    private Mejora getMejora(){
        Mejora mejora= new Mejora();
        return updateMejora(mejora);
    }

    /**
     * Método que recoge la mejora añadida o modificada por el usuario
     */
    private Mejora updateMejora(Mejora mejora){
        if (mejora==null) return null;
        if (mejorasJTArea.getText().trim().equalsIgnoreCase("")){
            clear();
            return null;
        }
        try{mejora.setFechaEjecucion(Constantes.df.parse(fechaEjecucionJTField.getText().trim()));}catch(java.text.ParseException e){}
        try{mejora.setImporte(((Double)importeJTField.getNumber()).doubleValue());}catch(Exception e){}
        mejora.setDescripcion(mejorasJTArea.getText().trim());
        return mejora;
    }

    private void addJButtonActionPerformed(){
        mejorasListaPane.clearSeleccion();
        clear();
        selected= null;
        editJButton.setEnabled(false);
        deleteJButton.setEnabled(false);
        camposEnabled(true);
        mejorasJTArea.requestFocusInWindow();
    }

    private void deleteJButtonActionPerformed(){
        if (mejorasListaPane.getSelected() == null) return;
        if (confirmDelete()){
            mejorasListaPane.borrar();
            editJButton.setEnabled(false);
            camposEnabled(false);
        }
    }


    private void editJButtonActionPerformed(){
        selected= (Mejora)mejorasListaPane.getSelected();
        if (selected!=null){
            camposEnabled(true);
            mejorasJTArea.requestFocusInWindow();
        }

    }

    private void okJButtonActionPerformed(){
        if (selected == null){
            mejorasListaPane.add(getMejora());
        }else{
            mejorasListaPane.update(updateMejora(selected));
            mejorasListaPane.seleccionar(selected);
        }
        selected= null;
        camposEnabled(false);
    }

    private void camposEnabled(boolean b){
        fechaEjecucionJButton.setEnabled(b);
        mejorasJTArea.setEnabled(b);
        importeJTField.setEnabled(b);
        okJButton.setEnabled(b);
    }

    /**
     * Método que abre una ventana de confirmacion de borrado de un elemento de la lista
     * @return false si la confirmacion es negativa, true en caso contrario
     */
    private boolean confirmDelete(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.mejoras.tag8"), aplicacion.getI18nString("inventario.mejoras.tag9"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    private javax.swing.JLabel fechaEjecucionJLabel;
    private javax.swing.JLabel importeJLabel;
    private javax.swing.JLabel mejorasJLabel;

    private JFormattedTextField fechaEjecucionJTField;
    private com.geopista.app.utilidades.JNumberTextField importeJTField;
    private javax.swing.JScrollPane mejorasJScrollPane;
    private TextPane mejorasJTArea;
    private JButton addJButton;
    private JButton deleteJButton;
    private JButton editJButton;
    private JButton okJButton;
    private JButton fechaEjecucionJButton;



}
