/*
 * Created on 02-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro.registroExpedientes.configuracion.paneles;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
/**
 * @author dbaeza
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AyudaImportacionJDialog extends JDialog
{
    private ApplicationContext aplicacion =  AppContext.getApplicationContext();
       
	private JPanel jContentPane = null;

	private JPanel botoneraPanel;
	private JPanel passwordPanel;
	private JLabel lblPassword = null;
	private JPasswordField txtJPassword = null;
	private JButton cancelarjButton = null;
	private JButton aceptarjButton = null;

	private JPanel datosSolicitantePanel;

	private JTextArea ayudaInfoField;
	

	/**
	 * This is the default constructor
	 */
	public AyudaImportacionJDialog() {
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 180);
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
			
			jContentPane  = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			
			jContentPane.add(getPasswordPanel(), 
					new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    		
			jContentPane.add(getDatosSolicitantePanel(), 
					new GridBagConstraints(0,1,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
						
			jContentPane.add(getBotoneraPanel(), 
					new GridBagConstraints(0,2,1,1, 0.1, 1,GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));

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

		}
		return txtJPassword;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelarJButton() {
		if (cancelarjButton == null) {
			cancelarjButton = new JButton();
			//jButton.setBounds(168, 68, 110, 29);
			cancelarjButton.setText(I18N.get("RegistroExpedientes","Catastro.webservices.password.cancelar"));
		
			cancelarjButton.setName("btnCancelar");
			cancelarjButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    
					getTxtJPassword().setText(null);
					setVisible(false);
					dispose();
				}
			});
		}
		return cancelarjButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getAceptarJButton() {
		if (aceptarjButton == null) {
			aceptarjButton = new JButton();
			aceptarjButton.setName("btnAceptar");
			aceptarjButton.setText(I18N.get("RegistroExpedientes","Catastro.webservices.password.aceptar"));

			aceptarjButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {   
					if(getTxtJPassword().getText().equals("")){
						String msg = I18N.get("RegistroExpedientes","Catastro.webservices.password.noIntroducido.password");
			    		JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
					}else{
						getDatosSolicitantePanel().setVisible(true);
						passwordPanel.setVisible(false);
						setVisible(false);
						setSize(500, 350);
					}
				}
			});
		}
		return aceptarjButton;
	}
   
    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return getTxtJPassword().getText();
    }

   	
	public JPanel getDatosSolicitantePanel() {
		if(datosSolicitantePanel == null){
			datosSolicitantePanel  = new JPanel();
			datosSolicitantePanel.setLayout(new GridBagLayout());
    		
			datosSolicitantePanel.setBorder(
					new TitledBorder(I18N.get("RegistroExpedientes","Catastro.webservices.datos.solicitante")));
//			
//			nombreLabel = new JLabel();
//			nombreLabel.setText(I18N.get("RegistroExpedientes","Catastro.webservices.nombre"));
//    		
//    		dniLabel = new JLabel();
//    		dniLabel.setText(I18N.get("RegistroExpedientes","Catastro.webservices.nif"));
//    		
			
//    		datosSolicitantePanel.add(nombreLabel, 
//					new GridBagConstraints(0,0,1,1, 0.01, 1,GridBagConstraints.WEST,
//						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
//    		
//    		datosSolicitantePanel.add(getNombreField(), 
//					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.WEST,
//						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    		
//			datosSolicitantePanel.add(dniLabel, 
//					new GridBagConstraints(0,1,1,1, 0.01, 1,GridBagConstraints.WEST,
//						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
			
			datosSolicitantePanel.add(getAyudaInfoField(), 
					new GridBagConstraints(0,0,1,1, 1, 1,GridBagConstraints.CENTER,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			datosSolicitantePanel.setVisible(false);

    	}
		return datosSolicitantePanel;
	}
	public void setDatosSolicitantePanel(JPanel datosSolicitantePanel) {
		this.datosSolicitantePanel = datosSolicitantePanel;
	}


	public JTextArea getAyudaInfoField() {
		if(ayudaInfoField == null){
			ayudaInfoField = new JTextArea();
			ayudaInfoField.setEditable(false);			
		}
		
		
		return ayudaInfoField;
	}
	public void setAyudaInfoField(JTextArea ayudaInfoField) {
		this.ayudaInfoField = ayudaInfoField;
	}
	

	public JPanel getBotoneraPanel() {
		if(botoneraPanel == null){
			botoneraPanel  = new JPanel();
			botoneraPanel.setLayout(new GridBagLayout());
			
			botoneraPanel.add(getAceptarJButton(), 
					new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			botoneraPanel.add(getCancelarJButton(), 
					new GridBagConstraints(1,0,1,1, 0.1, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
			
			

    	}
		return botoneraPanel;
	}
	public void setBotoneraPanel(JPanel botoneraPanel) {
		this.botoneraPanel = botoneraPanel;
	}
	

	public JPanel getPasswordPanel() {
		if(passwordPanel == null){
			passwordPanel  = new JPanel();
			passwordPanel.setLayout(new GridBagLayout());
			passwordPanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes","Catastro.webservices.password")));
			
			lblPassword = new JLabel();
			lblPassword.setText(I18N.get("RegistroExpedientes","Catastro.webservices.password.certificado"));

			passwordPanel.add(lblPassword, 
					new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			passwordPanel.add(getTxtJPassword(), 
					new GridBagConstraints(0,1,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(10,5,0,5),0,0));
		}
		return passwordPanel;
	}
	public void setPasswordPanel(JPanel passwordPanel) {
		this.passwordPanel = passwordPanel;
	}
	
   }  
