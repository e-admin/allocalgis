package com.geopista.app.cementerios.panel;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;
import com.geopista.app.inventario.panel.ListaPane;
import com.geopista.app.utilidades.TextPane;
import com.geopista.protocol.inventario.Observacion;
import com.geopista.protocol.inventario.BienBean;

import javax.swing.*;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 07-ago-2006
 * Time: 12:51:08
 * To change this template use File | Settings | File Templates.
 */
public class ObservacionesJPanel extends JPanel{
    private AppContext aplicacion;

    private ListaPane observacionesListaPane;
    private Observacion selected;
    private String operacion;

    /**
     * Método que genera el panel de las Observaciones de un bien
     */
    public ObservacionesJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    public void initComponents() throws Exception{

        fechaJLabel= new javax.swing.JLabel();
        observacionesJLabel= new javax.swing.JLabel();

        observacionesJScrollPane= new javax.swing.JScrollPane();
        observacionesJTArea= new TextPane(999);
        observacionesJTArea.setEnabled(false);
        fechaJTField= new JTextField();
        fechaJTField.setEnabled(false);

        addJButton= new JButton();
        addJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed();
            }
        });

        deleteJButton= new JButton();
        deleteJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoBorrar);
        deleteJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJButtonActionPerformed();
            }
        });
        deleteJButton.setEnabled(false);

        editJButton= new JButton();
        editJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoEditar);
        editJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJButtonActionPerformed();
            }
        });
        editJButton.setEnabled(false);

        okJButton= new JButton();
        okJButton.setIcon(com.geopista.app.cementerios.UtilidadesComponentes.iconoOK);
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed();
            }
        });
        okJButton.setEnabled(false);

        observacionesListaPane= new ListaPane(null);
        observacionesListaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(observacionesListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 400, 350));
        add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        add(deleteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 20, 20));
        add(editJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, 20));
        add(fechaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 150, 20));
        add(observacionesJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, 20));

        add(fechaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 160, 20));
        observacionesJScrollPane.setViewportView(observacionesJTArea);
        add(observacionesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 400, 60));
        add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 20, 20));

    }

    /**
     * Método que carga la lista de Observaciones de un bien
     * @param bien
     */
    public void load(BienBean bien){
        if (bien==null) return;
        observacionesListaPane.setCollection(bien.getObservaciones());
        observacionesListaPane.actualizarModelo();
    }

    /**
     * Método que visualiza la observacion seleccionada de la lista
     * @param obs seleccionada
     */
    public void load(Observacion obs){
        if (obs==null) return;
        try{fechaJTField.setText(Constantes.df.format(obs.getFecha()));}catch(Exception e){}
        observacionesJTArea.setText(obs.getDescripcion());
    }

    public void actualizarDatos(BienBean bien){
        if (bien==null) return;
        bien.setObservaciones(observacionesListaPane.getCollection());
    }

    private void listaPaneActionPerformed(){
        clear();
        observacionesJTArea.setEnabled(false);        
        Observacion obs= (Observacion)observacionesListaPane.getSelected();
        if (obs!=null && operacion!=null && !operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            editJButton.setEnabled(true);
            deleteJButton.setEnabled(true);
        }
        load(obs);
    }

    public void clear(){
        fechaJTField.setText("");
        observacionesJTArea.setText("");

    }

    public void setEnabled(boolean b){
        addJButton.setEnabled(b);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(false);
        editJButton.setEnabled(false);

    }

    public void renombrarComponentes(){
        renombrarJListComponent();
        try{fechaJLabel.setText(aplicacion.getI18nString("inventario.observaciones.tag2"));}catch(Exception e){}
        try{observacionesJLabel.setText(aplicacion.getI18nString("inventario.observaciones.tag3"));}catch(Exception e){}
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.observaciones.tag4"));}catch(Exception e){}
        try{deleteJButton.setToolTipText(aplicacion.getI18nString("inventario.observaciones.tag5"));}catch(Exception e){}
        try{editJButton.setToolTipText(aplicacion.getI18nString("inventario.observaciones.tag6"));}catch(Exception e){}
        try{okJButton.setToolTipText(aplicacion.getI18nString("inventario.observaciones.tag7"));}catch(Exception e){}
    }

    /**
     * Método que renombra el panel de la lista
     */
    public void renombrarJListComponent(){
        try{observacionesListaPane.renombrar(aplicacion.getI18nString("inventario.observaciones.tag1"));}catch(Exception e){};
    }

    /**
     * Método que recoge la observacion añadida por el usuario
     */
    private Observacion getObservacion(){
        return updateObservacion(new Observacion());
    }

    /**
     * Método que recoge la observacion modificada por el usuario
     */
    private Observacion updateObservacion(Observacion obs){
        if (obs==null) return null;
        if (observacionesJTArea.getText().trim().equalsIgnoreCase("")){
            clear();
            return null;
        }
        obs.setFecha((java.util.Date)new Timestamp(new java.util.Date().getTime()));
        obs.setDescripcion(observacionesJTArea.getText().trim());
        return obs;
    }

    private void addJButtonActionPerformed(){
        observacionesListaPane.clearSeleccion();
        clear();
        selected= null;
        editJButton.setEnabled(false);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(true);
        observacionesJTArea.setEnabled(true);
        observacionesJTArea.requestFocusInWindow();
        try{fechaJTField.setText(Constantes.df.format((java.util.Date)new Timestamp(new java.util.Date().getTime())));}catch(Exception e){}

    }

    private void deleteJButtonActionPerformed(){
        if (observacionesListaPane.getSelected() == null) return;
        if (confirmDelete()){
            observacionesListaPane.borrar();
            editJButton.setEnabled(false);
            okJButton.setEnabled(false);
        }
    }


    private void editJButtonActionPerformed(){
        selected= (Observacion)observacionesListaPane.getSelected();
        if (selected!=null){
            observacionesJTArea.setEnabled(true);
            observacionesJTArea.requestFocusInWindow();
            okJButton.setEnabled(true);
        }

    }

    private void okJButtonActionPerformed(){
        if (selected == null){
            observacionesListaPane.add(getObservacion());
        }else{
            observacionesListaPane.update(updateObservacion(selected));
            observacionesListaPane.seleccionar(selected);
        }
        selected= null;
        okJButton.setEnabled(false);
        observacionesJTArea.setEnabled(false);
    }

    /**
     * Método que abre una ventana de confirmacion de borrado de un elemento de la lista
     * @return false si la confirmacion es negativa, true en caso contrario
     */
    private boolean confirmDelete(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.observaciones.tag8"), aplicacion.getI18nString("inventario.observaciones.tag9"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    private javax.swing.JLabel fechaJLabel;
    private javax.swing.JLabel observacionesJLabel;

    private javax.swing.JTextField fechaJTField;
    private javax.swing.JScrollPane observacionesJScrollPane;
    private TextPane observacionesJTArea;
    private JButton addJButton;
    private JButton deleteJButton;
    private JButton editJButton;
    private JButton okJButton;


}
