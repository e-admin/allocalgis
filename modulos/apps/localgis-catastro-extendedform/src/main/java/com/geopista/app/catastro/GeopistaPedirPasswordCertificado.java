/**
 * GeopistaPedirPasswordCertificado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 02-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
/**
 * @author dbaeza
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeopistaPedirPasswordCertificado extends JDialog
{
    private ApplicationContext aplicacion =  AppContext.getApplicationContext();
    private boolean okPressed=false;
    private String password="";
    private boolean aceptar=false;
	private javax.swing.JPanel jContentPane = null;

	private JLabel lblPassword = null;
	private JPasswordField txtJPassword = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	/**
	 * This is the default constructor
	 */
	public GeopistaPedirPasswordCertificado(JFrame fondo) {
		super(fondo,true);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(301, 135);
		this.setContentPane(getJContentPane());
		this.setTitle(aplicacion.getI18nString("certificados.password"));
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			lblPassword = new JLabel();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			lblPassword.setText(aplicacion.getI18nString("certificados.pedir.password"));
			lblPassword.setBounds(0, 0, 292, 16);
			jContentPane.add(lblPassword, null);
			jContentPane.add(getTxtJPassword(), null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jPasswordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */    
	private JPasswordField getTxtJPassword() {
		if (txtJPassword == null) {
			txtJPassword = new JPasswordField();
			txtJPassword.setBounds(11, 22, 265, 38);
		}
		return txtJPassword;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(168, 68, 110, 29);
			jButton.setText(aplicacion.getI18nString("certificados.cancelar"));
			jButton.setName("btnCancelar");
			jButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
					setPassword(null);
					setOkPressed(false);
					setVisible(false);
				}
			});
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(63, 69, 102, 28);
			jButton1.setName("btnAceptar");
			jButton1.setText(aplicacion.getI18nString("certificados.aceptar"));

			jButton1.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
				    setPassword(txtJPassword.getText());
				    setOkPressed(true);
				    setVisible(false);
				}
			});
		}
		return jButton1;
	}
    /**
     * @return Returns the aceptar.
     */
    public boolean isAceptar()
    {
        return aceptar;
    }
    /**
     * @param aceptar The aceptar to set.
     */
    public void setAceptar(boolean aceptar)
    {
        this.aceptar = aceptar;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    /**
     * @return Returns the okPressed.
     */
    public boolean isOkPressed()
    {
        return okPressed;
    }
    /**
     * @param okPressed The okPressed to set.
     */
    public void setOkPressed(boolean okPressed)
    {
        this.okPressed = okPressed;
    }
   }  //  @jve:decl-index=0:visual-constraint="10,10"
