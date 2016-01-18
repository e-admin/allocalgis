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
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


/**
 * @author seilagamo
 *
 */
public class JPanelNuevoMunicipio extends JPanel {
    
    private boolean modoEdicion = false;
    private JFrame framePadre;
    
    private JLabel codigoMunicipioJLabel;
    private JTextField codigoMunicipioJText;
    private JButton aniadirMunicipioListaJButton;
//    private JButton cancelarAniadirMunicipioJButton;
    
    public JPanelNuevoMunicipio(ResourceBundle messages, JFrame framePadre, boolean modoEdicion) {
        this.framePadre = framePadre;
        this.modoEdicion = modoEdicion;
        initialize();
        changeScreenLang(messages);
//        editable(false);
    }
    
    private void initialize(){
        this.setPreferredSize(new Dimension(174, 100));
        this.setMaximumSize(new Dimension(174, 100));
        this.setLayout(new GridBagLayout());
        codigoMunicipioJLabel = new JLabel();
        codigoMunicipioJText = new JTextField(5);
        this.add(codigoMunicipioJLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                    0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                            5, 5), 0, 0) );
        this.add(codigoMunicipioJText, new GridBagConstraints(1, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );
        aniadirMunicipioListaJButton = new JButton();
        this.add(aniadirMunicipioListaJButton, new GridBagConstraints(0, 1, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );
        
//        this.add(getCancelarAniadirMunicipioJButton(), new GridBagConstraints(1, 1, 1, 1, 0.0,
//                0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
//                        5, 5), 0, 0) );        
    }
    
    public void changeScreenLang(ResourceBundle messages) {
        this.setBorder(new TitledBorder(messages.getString("JPanelNuevoMunicipio.nuevoMunicipioPanel")));
        codigoMunicipioJLabel.setText(messages.getString("JPanelNuevoMunicipio.codigoMunicipioJLabel"));
        aniadirMunicipioListaJButton.setText(messages.getString("JPanelNuevoMunicipio.aniadirMunicipioListaJButton"));
        aniadirMunicipioListaJButton.setToolTipText(messages.getString("JPanelNuevoMunicipio.aniadirMunicipioListaJButton"));
//        cancelarAniadirMunicipioJButton.setText(messages.getString("JPanelNuevoMunicipio.cancelarAniadirMunicipioJButton"));
//        cancelarAniadirMunicipioJButton.setToolTipText(messages.getString("JPanelNuevoMunicipio.cancelarAniadirMunicipioJButton"));
    }
    
//    private JButton getCancelarAniadirMunicipioJButton(){
//        if (cancelarAniadirMunicipioJButton == null) {            
//            cancelarAniadirMunicipioJButton = new JButton();
//            cancelarAniadirMunicipioJButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    editable(false);
//                }
//            });
//        }
//        return cancelarAniadirMunicipioJButton;
//    }
    
    public void editable(boolean b){        
        this.setEnabled(false);
        this.modoEdicion = b;
        codigoMunicipioJLabel.setEnabled(b);
        codigoMunicipioJText.setEnabled(b);
        aniadirMunicipioListaJButton.setEnabled(b);
//        cancelarAniadirMunicipioJButton.setEnabled(b);                
    }
    
    JButton getBotonAniadir() {
        return aniadirMunicipioListaJButton;
    }
    
    String getIdMunicipio() {
        return codigoMunicipioJText.getText();
    }

}
