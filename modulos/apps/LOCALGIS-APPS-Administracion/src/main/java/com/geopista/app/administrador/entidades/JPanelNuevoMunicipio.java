/**
 * JPanelNuevoMunicipio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.app.administrador.entidades;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

    private JLabel nombreMunicipioJLabel;
    private JTextField nombreMunicipioJText;

    private JLabel sridMunicipioJLabel;
    private JTextField sridMunicipioJText;

    private JButton aniadirMunicipioListaJButton;
    
    public JPanelNuevoMunicipio(ResourceBundle messages, JFrame framePadre, boolean modoEdicion) {
        this.framePadre = framePadre;
        this.modoEdicion = modoEdicion;
        initialize();
        changeScreenLang(messages);
    }
    
    public JPanelNuevoMunicipio(ResourceBundle messages, String id_municipio) {
        initialize2(id_municipio);
        changeScreenLang(messages);
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
        
 
    }
    
    private void initialize2(String id_municipio){
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
        codigoMunicipioJText.setText(id_municipio);
        
        
        nombreMunicipioJLabel = new JLabel();
        nombreMunicipioJText = new JTextField(20);
        this.add(nombreMunicipioJLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
                    0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                            5, 5), 0, 0) );
        this.add(nombreMunicipioJText, new GridBagConstraints(1, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );

        sridMunicipioJLabel = new JLabel();
        sridMunicipioJText = new JTextField(20);
        this.add(sridMunicipioJLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
                    0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                            5, 5), 0, 0) );
        this.add(sridMunicipioJText, new GridBagConstraints(1, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );

        
        aniadirMunicipioListaJButton = new JButton();
        this.add(aniadirMunicipioListaJButton, new GridBagConstraints(0, 3, 2, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5,
                        5, 5), 0, 0) );
        
 
    }
    
    public void changeScreenLang(ResourceBundle messages) {
        this.setBorder(new TitledBorder(messages.getString("JPanelNuevoMunicipio.nuevoMunicipioPanel")));
        codigoMunicipioJLabel.setText(messages.getString("JPanelNuevoMunicipio.codigoMunicipioJLabel"));
        aniadirMunicipioListaJButton.setText(messages.getString("JPanelNuevoMunicipio.aniadirMunicipioListaJButton"));
        aniadirMunicipioListaJButton.setToolTipText(messages.getString("JPanelNuevoMunicipio.aniadirMunicipioListaJButton"));
        
        if (nombreMunicipioJLabel!=null)
        	nombreMunicipioJLabel.setText(messages.getString("CUsuariosFrame.col1"));
        if (sridMunicipioJLabel!=null)
        	sridMunicipioJLabel.setText("SRID");
    }
    
    
    public void editable(boolean b){        
        this.setEnabled(false);
        this.modoEdicion = b;
        codigoMunicipioJLabel.setEnabled(b);
        codigoMunicipioJText.setEnabled(b);
        aniadirMunicipioListaJButton.setEnabled(b);
    }
    
    JButton getBotonAniadir() {
        return aniadirMunicipioListaJButton;
    }
    
    String getIdMunicipio() {
        return codigoMunicipioJText.getText();
    }

    String getNombreMunicipio() {
        return nombreMunicipioJText.getText();
    }
    
    String getSridMunicipio() {
    	if (sridMunicipioJText!=null)
    		return sridMunicipioJText.getText();
    	else
    		return null;
    }
    
    
}
