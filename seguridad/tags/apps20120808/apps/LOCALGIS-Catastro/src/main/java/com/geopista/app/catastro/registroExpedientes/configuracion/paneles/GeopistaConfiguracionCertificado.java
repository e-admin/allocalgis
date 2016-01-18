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
public class GeopistaConfiguracionCertificado extends JDialog
{
    private ApplicationContext aplicacion =  AppContext.getApplicationContext();
    private boolean okPressed=false;
    private String password="";
    private boolean aceptar=false;
	private JPanel jContentPane = null;
	private JPanel filePanel;
	private JFileChooser fileCertificado = null;
	private JPanel botoneraPanel;
	private JPanel passwordPanel;
	private JLabel ficheroCertificadoLabel;
	private JLabel lblPassword = null;
	private JPasswordField txtJPassword = null;
	private JButton cancelarjButton = null;
	private JButton aceptarjButton = null;
	
	private JTextField certificadoField = null;
	private JButton certificadojButton = null;
	private File keystoreFile;
	
	private JLabel nombreLabel;
	private JLabel dniLabel;
	private JTextField nombreField = null;
	private JTextField dniField = null;
	private JPanel datosSolicitantePanel;
	

	/**
	 * This is the default constructor
	 */
	public GeopistaConfiguracionCertificado() {
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 330);
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
    		
			jContentPane.add(getDatosSolicitantePanel(), 
					new GridBagConstraints(0,0,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			jContentPane.add(getFilePanel(), 
					new GridBagConstraints(0,1,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			jContentPane.add(getPasswordPanel(), 
					new GridBagConstraints(0,2,1,1, 0.1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
			
			jContentPane.add(getBotoneraPanel(), 
					new GridBagConstraints(0,3,1,1, 0.1, 1,GridBagConstraints.EAST,
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
					setPassword(null);
					setOkPressed(false);
					setVisible(false);
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
				 
					if(getNombreField().getText().equals("") || getDniField().getText().equals("")){
						String msg = I18N.get("RegistroExpedientes","Catastro.webservices.datos.solicitante.establecer");
			    		JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
					}
					else{
						if(getDniField().getText().length() != 9){
							String msg = I18N.get("RegistroExpedientes","Catastro.webservices.datos.solicitante.nif.novalido");
				    		JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
						}
						else if(getKeystoreFile() != null && 
				    		!getKeystoreFile().getPath().equals("")){
					    	if(!getTxtJPassword().getText().equals("")){
					    			
					    		KeyStore keyStore;
								try {
									keyStore = KeyStore.getInstance("pkcs12");
									keyStore.load(new FileInputStream(keystoreFile), getTxtJPassword().getText().toCharArray());
									
									
								    File keystoreFile = new File(fileCertificado.getSelectedFile().getPath());
									
						    		HashMap certificadoHashMap = new HashMap();
						    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_NOMBRE_SOLICITANTE, getNombreField().getText().toUpperCase());
						    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_DNI_SOLICITANTE, getDniField().getText().toUpperCase());
						    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_KEYSTOREFILE, keystoreFile);
						    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_PASSWORD, getTxtJPassword().getText());
						    		
						    		AppContext.getApplicationContext().getBlackboard().put(ConstantesRegistroExp.CERTIFICADO_DATOS, certificadoHashMap);
//						    		System.setProperty("javax.net.ssl.trustStore", getKeystoreFile().getPath());
//									System.setProperty("javax.net.ssl.trustStorePassword", getTxtJPassword().getText());
//									System.setProperty("javax.net.debug", "all");
						    		setVisible(false);
									
								} catch (KeyStoreException e5) {
									String msg = I18N.get("RegistroExpedientes","Catastro.webservices.certificado.novalido");
									JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
									e5.printStackTrace();
								} catch (NoSuchAlgorithmException e1) {
									String msg = I18N.get("RegistroExpedientes","Catastro.webservices.certificado.novalido");
									JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
									e1.printStackTrace();
								} catch (CertificateException e2) {
									String msg = I18N.get("RegistroExpedientes","Catastro.webservices.certificado.novalido");
									JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
									e2.printStackTrace();
								} catch (FileNotFoundException e3) {
									String msg = I18N.get("RegistroExpedientes","Catastro.webservices.certificado.novalido");
									JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
									e3.printStackTrace();
								} catch (IOException e4) {
									String msg = I18N.get("RegistroExpedientes","Catastro.webservices.certificado.pass.novalida");
									JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
									e4.printStackTrace();
								}
					    	   
					    		
					    		
					    		
					    		
					    		
//					    		
//							    File keystoreFile = new File(fileCertificado.getSelectedFile().getPath());
//								
//					    		HashMap certificadoHashMap = new HashMap();
//					    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_NOMBRE_SOLICITANTE, getNombreField().getText().toUpperCase());
//					    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_DNI_SOLICITANTE, getDniField().getText().toUpperCase());
//					    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_KEYSTOREFILE, keystoreFile);
//					    		certificadoHashMap.put(ConstantesRegistroExp.CERTIFICADO_PASSWORD, getTxtJPassword().getText());
//					    		
//					    		AppContext.getApplicationContext().getBlackboard().put(ConstantesRegistroExp.CERTIFICADO_DATOS, certificadoHashMap);
////					    		System.setProperty("javax.net.ssl.trustStore", getKeystoreFile().getPath());
////								System.setProperty("javax.net.ssl.trustStorePassword", getTxtJPassword().getText());
////								System.setProperty("javax.net.debug", "all");
//					    		setVisible(false);
					    	}
					    	else{
					    		String msg = I18N.get("RegistroExpedientes","Catastro.webservices.password.noIntroducido.password");
					    		JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
					    	}
					    }
					    else{
					    	String msg = I18N.get("RegistroExpedientes","Catastro.webservices.password.noSeleccion.certificado");
				    		JOptionPane.showMessageDialog(((AppContext) AppContext.getApplicationContext()).getMainFrame(),msg );
					    	
					    }
					}
				}
			});
		}
		return aceptarjButton;
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
    
    public JPanel getFilePanel() {
    	if(filePanel == null){
    		filePanel  = new JPanel();
    		filePanel.setLayout(new GridBagLayout());
    		filePanel.setBorder(new TitledBorder(I18N.get("RegistroExpedientes","Catastro.webservices.certificado")));
    		
    		ficheroCertificadoLabel = new JLabel();
    		ficheroCertificadoLabel.setText(I18N.get("RegistroExpedientes","Catastro.webservices.password.seleccione.certificado"));
    		
    		
    		filePanel.add(ficheroCertificadoLabel, 
					new GridBagConstraints(0,0,2,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    		
    		filePanel.add(getCertificadoField(), 
					new GridBagConstraints(0,1,1,1, 1, 1,GridBagConstraints.NORTH,
						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
    		
    		filePanel.add(getCertificadojButton(), 
					new GridBagConstraints(1,1,1,1, 0.01, 1,GridBagConstraints.EAST,
						GridBagConstraints.NONE, new Insets(10,5,0,5),0,0));
    		

    	}
		return filePanel;
	}
	public void setFilePAnel(JPanel filePanel) {
		this.filePanel = filePanel;
	}
	
	
	public JPanel getDatosSolicitantePanel() {
		if(datosSolicitantePanel == null){
			datosSolicitantePanel  = new JPanel();
			datosSolicitantePanel.setLayout(new GridBagLayout());
    		
			datosSolicitantePanel.setBorder(
					new TitledBorder(I18N.get("RegistroExpedientes","Catastro.webservices.datos.solicitante")));
			
			nombreLabel = new JLabel();
			nombreLabel.setText(I18N.get("RegistroExpedientes","Catastro.webservices.nombre"));
    		
    		dniLabel = new JLabel();
    		dniLabel.setText(I18N.get("RegistroExpedientes","Catastro.webservices.nif"));
    		
			
    		datosSolicitantePanel.add(nombreLabel, 
					new GridBagConstraints(0,0,1,1, 0.01, 1,GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    		
    		datosSolicitantePanel.add(getNombreField(), 
					new GridBagConstraints(1,0,1,1, 1, 1,GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
    		
			datosSolicitantePanel.add(dniLabel, 
					new GridBagConstraints(0,1,1,1, 0.01, 1,GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
			
			datosSolicitantePanel.add(getDniField(), 
					new GridBagConstraints(1,1,1,1, 1, 1,GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));

    	}
		return datosSolicitantePanel;
	}
	public void setDatosSolicitantePanel(JPanel datosSolicitantePanel) {
		this.datosSolicitantePanel = datosSolicitantePanel;
	}

	public JTextField getDniField() {
		if(dniField == null){
			dniField = new JTextField();
		}
		return dniField;
	}
	public void setDniField(JTextField dniField) {
		this.dniField = dniField;
	}

	public JTextField getNombreField() {
		if(nombreField == null){
			nombreField = new JTextField();
		}
		return nombreField;
	}
	public void setNombreField(JTextField nombreField) {
		this.nombreField = nombreField;
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
	
	public JTextField getCertificadoField() {
		if(certificadoField == null){
			certificadoField = new JTextField();
		}
		return certificadoField;
	}
	public void setCertificadoField(JTextField certificadoField) {
		this.certificadoField = certificadoField;
	}
	public JButton getCertificadojButton() {
		if (certificadojButton == null) {
			certificadojButton = new JButton();

			certificadojButton.setText(I18N.get("RegistroExpedientes","Catastro.webservices.password.trustStore.buscar"));

			certificadojButton.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mousePressed(java.awt.event.MouseEvent e) {    

					fileCertificado = new JFileChooser();
				
					GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
					filter.addExtension("pfx");
					filter.setDescription(I18N.get("RegistroExpedientes","Catastro.webservices.certificado.pkcs12"));
					fileCertificado.setFileFilter(filter);
				    fileCertificado.setFileSelectionMode(0);
				    fileCertificado.setAcceptAllFileFilterUsed(false);
					 
				    int returnVal = fileCertificado.showOpenDialog(aplicacion.getMainFrame());
				    if (returnVal == JFileChooser.APPROVE_OPTION)
				    {
				    	setKeystoreFile( new File(fileCertificado.getSelectedFile().getPath()));
				    	certificadoField.setText(getKeystoreFile().getPath());
					}
					else{
						certificadoField.setText("");
					 }
   
				}
			});
		}
		return certificadojButton;
	}
	public void setTrustStorejButton(JButton trustStorejButton) {
		this.certificadojButton = trustStorejButton;
	}
	
	
	public File getKeystoreFile() {
		return keystoreFile;
	}
	public void setKeystoreFile(File keystoreFile) {
		this.keystoreFile = keystoreFile;
	}
   }  
