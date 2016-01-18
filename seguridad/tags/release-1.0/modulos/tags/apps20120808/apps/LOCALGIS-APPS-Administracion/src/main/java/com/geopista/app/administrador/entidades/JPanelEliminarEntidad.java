/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.Entidad;
import com.geopista.protocol.administrador.ListaEntidades;
import com.geopista.protocol.administrador.OperacionesAdministrador;


/**
 * @author seilagamo
 *
 */
public class JPanelEliminarEntidad extends JPanel {
    private boolean modoEdicion = false;
    private JFrame framePadre;
    
    private JLabel codigoEntidadJLabel;
    
    private JComboBox jComboBoxEntidades;
    
    private JButton eliminarEntidadListaJButton;
//    private JButton cancelarEliminarEntidadJButton;
    
    private ListaEntidades listaEntidades;
    
    public JPanelEliminarEntidad(ResourceBundle messages, JFrame framePadre, boolean modoEdicion){
        
        this.framePadre = framePadre;
        this.modoEdicion = modoEdicion;
        initialize();
        changeScreenLang(messages);
        editable(false);
        
    }
    
    
    private void initialize(){
        this.setPreferredSize(new Dimension(238, 100));
        this.setMaximumSize(new Dimension(238, 100));
        this.setLayout(new GridBagLayout());
        codigoEntidadJLabel = new JLabel();
        this.add(codigoEntidadJLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                    0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                            5, 5), 0, 0) );
        
        this.add(getJComboBoxEntidad(), new GridBagConstraints(1, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );
        eliminarEntidadListaJButton = new JButton();
        this.add(eliminarEntidadListaJButton, new GridBagConstraints(0, 1, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );
        
//        this.add(getCancelarEliminarEntidadJButton(), new GridBagConstraints(1, 1, 1, 1, 0.0,
//                0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
//                        5, 5), 0, 0) );
    }
    
    public void changeScreenLang(ResourceBundle messages) {
        this.setBorder(new TitledBorder(messages.getString("JPanelEliminarEntidad.eliminarEntidadPanel")));
        codigoEntidadJLabel.setText(messages.getString("JPanelEliminarEntidad.codigoEntidadJLabel"));        
        eliminarEntidadListaJButton.setText(messages.getString("JPanelEliminarEntidad.eliminarEntidadListaJButton"));
        eliminarEntidadListaJButton.setToolTipText(messages.getString("JPanelEliminarEntidad.eliminarEntidadListaJButton"));
//        cancelarEliminarEntidadJButton.setText(messages.getString("JPanelEliminarEntidad.cancelarEliminarEntidadJButton"));
//        cancelarEliminarEntidadJButton.setToolTipText(messages.getString("JPanelEliminarEntidad.cancelarEliminarEntidadJButton"));
    }
    
    
    
    private JComboBox getJComboBoxEntidad() {
        if (jComboBoxEntidades == null) {
            jComboBoxEntidades = new JComboBox();
            jComboBoxEntidades.setPreferredSize(new Dimension(120, 20));
            jComboBoxEntidades.setMaximumSize(new Dimension(120, 20));
        }
        return jComboBoxEntidades;
    }
    
//    private JButton getCancelarEliminarEntidadJButton(){
//        if (cancelarEliminarEntidadJButton == null) {            
//            cancelarEliminarEntidadJButton = new JButton();
//            cancelarEliminarEntidadJButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    editable(false);
//                }
//            });
//        }
//        return cancelarEliminarEntidadJButton;
//    }
    
    public void editable(boolean b){        
        this.setEnabled(false);
        this.modoEdicion = b;
        codigoEntidadJLabel.setEnabled(b);
        jComboBoxEntidades.setEnabled(b);
        eliminarEntidadListaJButton.setEnabled(b);
//        cancelarEliminarEntidadJButton.setEnabled(b);
        if (b) {
            refrescarListaEntidades();            
        }
    }
    
    public void refrescarListaEntidades() {
        listaEntidades = new OperacionesAdministrador(Constantes.url).getListaEntidades();
        Hashtable hEntidades = listaEntidades.gethEntidades();        
        jComboBoxEntidades.removeAllItems();
        jComboBoxEntidades.addItem(new Entidad("", "Seleccione", ""));
        jComboBoxEntidades = listaEntidades.cargarJComboBox(jComboBoxEntidades);
        jComboBoxEntidades.setRenderer(new ComboBoxRendererEntidad());
        jComboBoxEntidades.setSelectedIndex(0);
    }
    
    JButton getBotonEliminar() {
        return eliminarEntidadListaJButton;
    }
    
    String getCodigoEntidad() {     
        return ((Entidad)jComboBoxEntidades.getSelectedItem()).getId();
    }
}
