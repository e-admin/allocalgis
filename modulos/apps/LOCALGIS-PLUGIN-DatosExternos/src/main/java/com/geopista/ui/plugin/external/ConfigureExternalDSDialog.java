/**
 * ConfigureExternalDSDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.geopista.app.AppContext;


public class ConfigureExternalDSDialog extends JDialog {
    
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
    private final static String NEW = aplicacion.getI18nString("Nuevo");
    private final static String MODIFY = aplicacion.getI18nString("Modificar");
    private final static String DELETE = aplicacion.getI18nString("ExternalDataSourcePlugin.Borrar");
    private final static String DATASOURCE = aplicacion.getI18nString("ExternalDataSourcePlugin.DataSource");
    private final static String CONFIRMMSG = aplicacion.getI18nString("ExternalDataSourcePlugin.MsgConfirmacion");
    private final static String CONFDIALOGTITLE= aplicacion.getI18nString("ConfigureExternalDataSource");
	

    public ConfigureExternalDSDialog (final Frame frame, boolean modal) {
        super(frame, CONFDIALOGTITLE, modal);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
                        
    private void jbInit() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jButton3 = new JButton();
        jButton2 = new JButton();
        jComboBox1 = new JComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        setSize(300,200);
        setResizable(false);
        jLabel1.setText(DATASOURCE);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jButton1.setText(NEW);
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(40, 20, 5, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jButton3.setText(DELETE);
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(40, 10, 5, 20);
        getContentPane().add(jButton3, gridBagConstraints);

        jButton2.setText(MODIFY);
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(40, 10, 5, 10);
        getContentPane().add(jButton2, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 0, 20);
        getContentPane().add(jComboBox1, gridBagConstraints);

    }                

    private void jButton3ActionPerformed(ActionEvent evt) {                                         
    	
    	if (JOptionPane.showConfirmDialog(this, CONFIRMMSG) == JOptionPane.OK_OPTION) {
    		setDeletebuttonPressed(true);
    		dispose();
    	}
    	
    }                                        

    private void jButton2ActionPerformed(ActionEvent evt) {                                         
    	setModifyButtonPressed(true);
    	dispose();
    }                                        

    private void jButton1ActionPerformed(ActionEvent evt) {                                         
    	setNewButtonPressed(true);
    	dispose();
    }
    
    public void setDataSourceList(String[] list) {
    	jComboBox1.setModel(new DefaultComboBoxModel(list));
    }
                 
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JComboBox jComboBox1;
    private JLabel jLabel1;
    
    private boolean isNewButtonPressed;
    private boolean isModifyButtonPressed;
    private boolean isDeletebuttonPressed;
	public boolean isNewButtonPressed() {
		return isNewButtonPressed;
	}

	public void setNewButtonPressed(boolean isNewButtonPressed) {
		this.isNewButtonPressed = isNewButtonPressed;
	}

	public boolean isModifyButtonPressed() {
		return isModifyButtonPressed;
	}

	public void setModifyButtonPressed(boolean isModifyButtonPressed) {
		this.isModifyButtonPressed = isModifyButtonPressed;
	}

	public boolean isDeletebuttonPressed() {
		return isDeletebuttonPressed;
	}

	public void setDeletebuttonPressed(boolean isDeletebuttonPressed) {
		this.isDeletebuttonPressed = isDeletebuttonPressed;
	}                  
    
	public String getSelectedDataSource() {
		return (String) jComboBox1.getSelectedItem();
	}
    
}



