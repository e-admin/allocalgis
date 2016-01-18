/*
 * Created on 02-sep-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro.registroExpedientes.configuracion.paneles;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import com.geopista.feature.GeopistaFeature;
import com.geopista.protocol.document.DocumentBean;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * @author fercam
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GeopistaImportacionCertificadoJDialog extends JDialog {
	private ApplicationContext aplicacion = AppContext.getApplicationContext();
	private boolean okPressed = false;
	private boolean aceptar = false;
	private JPanel jContentPane = null;
	private JPanel filePanel;
	private JFileChooser fileCertificado = null;
	private JFileChooser fileCertificadoJava = null;
	private JPanel botoneraPanel;
	private JLabel ficheroCertificadoLabel;
	private JButton cancelarjButton = null;
	private JButton aceptarjButton = null;

	private JTextField certificadoField = null;
	private JButton certificadojButton = null;
	private JButton certificadoJavaJButton = null;
	private JButton ayudaJButton = null;
	private File keystoreFile;

	private JLabel directorioJavaLabel;
	private JTextField nombreDirectorioJavaField = null;
	private JTextField aliasField = null;
	private JTextField storePassField = null;
	private JLabel aliasLabel;
	private JLabel storePassLabel;
	private JPanel datosJavaDirectorioPanel;
	private JLabel ficheroCertificadoPFXLabel;
	private JTextField certificadoPFXField;
	private JButton certificadoPFXjButton;
	private AyudaImportacionJDialog ayudaDialogo;

	/**
	 * This is the default constructor
	 */
	public GeopistaImportacionCertificadoJDialog() {
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(650, 350);
		this.setContentPane(getJContentPane());
		this.setTitle(aplicacion.getI18nString("certificados.importacion"));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {

			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());

			jContentPane.add(getJavaFilePanel(),// getDatosSolicitantePanel(),
					new GridBagConstraints(0, 0, 1, 1, 0.1, 1,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jContentPane.add(getCertificadoFilePanel(),
					new GridBagConstraints(0, 1, 1, 1, 0.1, 1,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0,
									5), 0, 0));

			jContentPane.add(getBotoneraPanel(), new GridBagConstraints(0, 2,
					1, 1, 0.1, 1, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelarJButton() {
		if (cancelarjButton == null) {
			cancelarjButton = new JButton();
			// jButton.setBounds(168, 68, 110, 29);
			cancelarjButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.cancelar"));

			cancelarjButton.setName("btnCancelar");
			cancelarjButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent e) {
					setOkPressed(false);
					setVisible(false);
					dispose();// TODO:remove line
					
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
			aceptarjButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.aceptar"));

			aceptarjButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent e) {
					if (ayudaDialogo!=null)
						ayudaDialogo.dispose();

					if (getFileJavaCertField().getText().equals("")
							|| getCertificadoField().getText().equals("")
							|| getAliasField().getText().equals("")
							|| getStorePassField().getText().equals("")) {
						String msg = I18N.get("RegistroExpedientes",
								"Catastro.webservices.datos.java.certificados");
						JOptionPane.showMessageDialog(((AppContext) AppContext
								.getApplicationContext()).getMainFrame(), msg);
					} else {
						String cmd = "keytool -import -keystore \""
								+ getNombreDirectorioJavaField().getText()
								+ "\" -file \""
								+ getCertificadoField().getText()
								+ "\" -alias \"" + getAliasField().getText()
								+ "\" -storepass "
								+ getStorePassField().getText();
						String result=ejecutarCmdImportarCertificado();
						JOptionPane.showMessageDialog(((AppContext) AppContext
								.getApplicationContext()).getMainFrame(), result);
						

					}
				}
			});
		}
		return aceptarjButton;
	}

	/**
	 * @return Returns the aceptar.
	 */
	public boolean isAceptar() {
		return aceptar;
	}

	/**
	 * @param aceptar
	 *            The aceptar to set.
	 */
	public void setAceptar(boolean aceptar) {
		this.aceptar = aceptar;
	}

	/**
	 * @return Returns the okPressed.
	 */
	public boolean isOkPressed() {
		return okPressed;
	}

	/**
	 * @param okPressed
	 *            The okPressed to set.
	 */
	public void setOkPressed(boolean okPressed) {
		this.okPressed = okPressed;
	}

	public JPanel getCertificadoFilePanel() {
		if (filePanel == null) {
			filePanel = new JPanel();
			filePanel.setLayout(new GridBagLayout());
			filePanel
					.setBorder(new TitledBorder(I18N.get("RegistroExpedientes",
							"Catastro.webservices.certificado")));

			ficheroCertificadoLabel = new JLabel();
			ficheroCertificadoLabel.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.seleccione.certificado"));

			ficheroCertificadoPFXLabel = new JLabel();
			ficheroCertificadoPFXLabel.setText(I18N.get("RegistroExpedientes",
			"Catastro.webservices.password.seleccione.certificadopfx"));
			
			aliasLabel = new JLabel();
			aliasLabel.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.datos.alias"));
			
			
			filePanel.add(ficheroCertificadoPFXLabel, new GridBagConstraints(0, 0,
					1, 1, 0.01, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));

			filePanel.add(getCertificadoPFXField(), new GridBagConstraints(1, 0,
					1, 1, 1, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));

			filePanel.add(getCertificadoPFXjButton(), new GridBagConstraints(2, 0,
					1, 1, 0.01, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
			

			filePanel.add(ficheroCertificadoLabel, new GridBagConstraints(0, 1,
					1, 1, 0.01, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(10, 5, 0, 5), 0, 0));

			filePanel.add(getCertificadoField(), new GridBagConstraints(1, 1,
					1, 1, 1, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(10, 5, 0, 5), 0, 0));

			filePanel.add(getCertificadojButton(), new GridBagConstraints(2,1,
					1, 1, 0.01, 1, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(10, 5, 0, 5), 0, 0));

			filePanel.add(aliasLabel, new GridBagConstraints(0, 2, 1, 1, 0.01,
					1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(10, 5, 0, 5), 0, 0));

			filePanel.add(getAliasField(), new GridBagConstraints(1, 2, 1, 1,
					1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(10, 5, 0, 5), 0, 0));

			 filePanel.add(getAyudaJButton(), //Buscar
			 new GridBagConstraints(2,2,1,1, 0.01, 1,GridBagConstraints.WEST,
			 GridBagConstraints.BOTH, new Insets(10,5,0,5),0,0));
			

		}
		return filePanel;
	}

	public void setFilePAnel(JPanel filePanel) {
		this.filePanel = filePanel;
	}

	public JPanel getJavaFilePanel() {
		if (datosJavaDirectorioPanel == null) {
			datosJavaDirectorioPanel = new JPanel();
			datosJavaDirectorioPanel.setLayout(new GridBagLayout());

			datosJavaDirectorioPanel.setBorder(new TitledBorder(I18N.get(
					"RegistroExpedientes",
					"Catastro.webservices.datos.javadirectorystore")));

			directorioJavaLabel = new JLabel();
			directorioJavaLabel.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.passwordstore"));

			storePassLabel = new JLabel();
			storePassLabel.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.passStore"));

			datosJavaDirectorioPanel.add(directorioJavaLabel,
					new GridBagConstraints(0, 0, 1, 1, 0.01, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));

			datosJavaDirectorioPanel.add(getFileJavaCertField(),
					new GridBagConstraints(1, 0, 1, 1, 1, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));

			datosJavaDirectorioPanel.add(getCertificadoJavajButton(), // Buscar
					new GridBagConstraints(2, 0, 1, 1, 0.01, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(0, 5, 0, 5), 0, 0));

			datosJavaDirectorioPanel.add(storePassLabel,
					new GridBagConstraints(0, 1, 1, 1, 0.01, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(10, 5, 0, 5), 0, 0));

			datosJavaDirectorioPanel.add(getStorePassField(),
					new GridBagConstraints(1, 1, 1, 1, 1, 1,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(10, 5, 0, 5), 0, 0));

		}
		return datosJavaDirectorioPanel;
	}

	public void setJavaDirectorioPanel(JPanel javaDirectorioPanel) {
		this.datosJavaDirectorioPanel = javaDirectorioPanel;
	}

	public JTextField getFileJavaCertField() {
		if (nombreDirectorioJavaField == null) {
			nombreDirectorioJavaField = new JTextField();
		}
		return nombreDirectorioJavaField;
	}

	public void setDirectorioJavaField(JTextField directorioJavaField) {
		this.nombreDirectorioJavaField = directorioJavaField;
	}

	public JPanel getBotoneraPanel() {
		if (botoneraPanel == null) {
			botoneraPanel = new JPanel();
			botoneraPanel.setLayout(new GridBagLayout());

			botoneraPanel.add(getAceptarJButton(), new GridBagConstraints(0, 0,
					1, 1, 0.1, 1, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

			botoneraPanel.add(getCancelarJButton(), new GridBagConstraints(1,
					0, 1, 1, 0.1, 1, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

		}
		return botoneraPanel;
	}

	public void setBotoneraPanel(JPanel botoneraPanel) {
		this.botoneraPanel = botoneraPanel;
	}

	public JTextField getCertificadoField() {
		if (certificadoField == null) {
			certificadoField = new JTextField();
		}
		return certificadoField;
	}
	
	public JTextField getCertificadoPFXField() {
		if (certificadoPFXField == null) {
			certificadoPFXField = new JTextField();
		}
		return certificadoPFXField;
	}
	

	public JTextField getAliasField() {
		if (aliasField == null) {
			aliasField = new JTextField();
		}
		return aliasField;
	}

	public JTextField getStorePassField() {
		if (storePassField == null) {
			storePassField = new JTextField("changeit");
		}
		return storePassField;
	}

	public void setCertificadoField(JTextField certificadoField) {
		this.certificadoField = certificadoField;
	}

	public JButton getCertificadoPFXjButton() {
		if (certificadoPFXjButton == null) {
			certificadoPFXjButton = new JButton();

			certificadoPFXjButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.trustStore.buscar"));

			certificadoPFXjButton
					.addMouseListener(new java.awt.event.MouseAdapter() {
						private JFileChooser fileCertificadoPFX;

						public void mousePressed(java.awt.event.MouseEvent e) {

							fileCertificadoPFX = new JFileChooser();

							GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
							filter.addExtension("pfx");
							filter.addExtension("p12");
							filter.setDescription(I18N.get(
									"RegistroExpedientes",
									"Catastro.webservices.certificado.pkcs12"));
							fileCertificadoPFX.setFileFilter(filter);
							fileCertificadoPFX.setFileSelectionMode(0);
							fileCertificadoPFX.setAcceptAllFileFilterUsed(false);

							int returnVal = fileCertificadoPFX
									.showOpenDialog(aplicacion.getMainFrame());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								setKeystoreFile(new File(fileCertificadoPFX
										.getSelectedFile().getPath()));
								certificadoPFXField.setText(getKeystoreFile()
										.getPath());
								 ayudaJButton.setEnabled(true);
							} else {
								certificadoPFXField.setText("");
								 ayudaJButton.setEnabled(false);
							}

						}
					});
		}
		return certificadoPFXjButton;
	}
	
	public JButton getCertificadojButton() {
		if (certificadojButton == null) {
			certificadojButton = new JButton();

			certificadojButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.trustStore.buscar"));

			certificadojButton
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mousePressed(java.awt.event.MouseEvent e) {

							fileCertificado = new JFileChooser();

							GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
							filter.addExtension("cer");
							filter.setDescription(I18N.get(
									"RegistroExpedientes",
									"Catastro.webservices.certificado.pkcs7"));
							fileCertificado.setFileFilter(filter);
							fileCertificado.setFileSelectionMode(0);
							fileCertificado.setAcceptAllFileFilterUsed(false);

							int returnVal = fileCertificado
									.showOpenDialog(aplicacion.getMainFrame());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								setKeystoreFile(new File(fileCertificado
										.getSelectedFile().getPath()));
								certificadoField.setText(getKeystoreFile()
										.getPath());
							} else {
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

	public JButton getCertificadoJavajButton() {
		if (certificadoJavaJButton == null) {
			certificadoJavaJButton = new JButton();

			certificadoJavaJButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.trustStore.buscar"));

			certificadoJavaJButton
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mousePressed(java.awt.event.MouseEvent e) {

							fileCertificadoJava = new JFileChooser();

							// GeopistaFiltroFicheroFilter filter = new
							// GeopistaFiltroFicheroFilter();
							// filter.addExtension("pfx");
							// filter.setDescription(I18N.get("RegistroExpedientes","Catastro.webservices.certificado.pkcs12"));
							// fileCertificado.setFileFilter(filter);
							fileCertificadoJava.setFileSelectionMode(0);
							fileCertificadoJava
									.setAcceptAllFileFilterUsed(false);

							int returnVal = fileCertificadoJava
									.showOpenDialog(aplicacion.getMainFrame());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								setKeystoreFile(new File(fileCertificadoJava
										.getSelectedFile().getPath()));
								nombreDirectorioJavaField
										.setText(getKeystoreFile().getPath());
							} else {
								nombreDirectorioJavaField.setText("");
							}

						}
					});
		}
		return certificadoJavaJButton;
	}

	public void setTrustStoreJavajButton(JButton trustStorejButton) {
		this.certificadoJavaJButton = trustStorejButton;
	}

	public File getKeystoreFile() {
		return keystoreFile;
	}

	public void setKeystoreFile(File keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	public JButton getAyudaJButton() {
		if (ayudaJButton == null) {
			ayudaJButton = new JButton();
			ayudaJButton.setEnabled(false);

			ayudaJButton.setText(I18N.get("RegistroExpedientes",
					"Catastro.webservices.password.trustStore.ayuda"));

			ayudaJButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mousePressed(java.awt.event.MouseEvent e) {

					String result=ejecutarCmdGetAliasWithPasswordRequest();
					if(!result.equals("")){
						ayudaDialogo.getAyudaInfoField().setText(result);
						ayudaDialogo.setSize(800, 300);
						ayudaDialogo.setLocation(170,290);
						ayudaDialogo.setVisible(true);
					}else
						ayudaDialogo.setVisible(false);
						
					

				}
			});
		}
		return ayudaJButton;
	}

	private String ejecutarCmdImportarCertificado() {
		String msg="";//remove cert -delete -alias mykey  -keystore /etc/j2se/1.4/security/cacerts
		String cmd = "keytool -import -keystore \""
			+ getNombreDirectorioJavaField().getText()
			+ "\" -file \""
			+ getCertificadoField().getText()
			+ "\" -alias \"" + getAliasField().getText()
			+ "\" -storepass "
			+ getStorePassField().getText();
		try {
			
			Process proc = Runtime.getRuntime().exec(cmd);
			
			BufferedReader brStdErr = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			 final BufferedWriter brStdIn=new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())); //Intento meter la clave
			  
			 brStdIn.write("si");	
			 brStdIn.write("\n");
			 //lanza lo que haya en el buffer
			 brStdIn.flush();
			 
			String str = null;
			BufferedReader brStdOut = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			while ((str = brStdOut.readLine()) != null) {
				System.out.println(str);
				msg+=str+"\n";
			}
			
			brStdOut.close();
			brStdErr.close();
			
			if(proc.exitValue()==0)
				msg=I18N.get("RegistroExpedientes","Catastro.webservices.certificado.exito");
				
				
		} catch (IOException eproc) {
			System.out.println("Error to execute the command : " + eproc);
		}
		return msg;
	}
	
	private String ejecutarCmdGetAliasWithPasswordRequest() {
		String msg="";
		String cmd="keytool -list -storetype pkcs12 -keystore \""
			+ getCertificadoPFXField().getText() + "\"";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader brStdOut = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			BufferedReader brStdErr = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			 final BufferedWriter brStdIn=new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())); //Intento meter la clave
			 
			 String password=requestPassword();
			 if (password!=null && !password.equals(""))
			 {
				 brStdIn.write(password);				
				 brStdIn.write("\n");
				 //lanza lo que haya en el buffer
				 brStdIn.flush();
				String str = null;
				while ((str = brStdOut.readLine()) != null) {
					System.out.println(str);
					msg+=str+"\n";
				}
				
			 }
			brStdOut.close();
			brStdErr.close();
		} catch (IOException eproc) {
			System.out.println("Error to execute the command : " + eproc);
		}
		return msg;
	}

	private String requestPassword() {
		//Crear el Panel
		ayudaDialogo = new AyudaImportacionJDialog();
		ayudaDialogo.setModal(true);
		ayudaDialogo.setLocationRelativeTo(this.getParent());
		ayudaDialogo.setSize(300,180);
		ayudaDialogo.setResizable(false);
		ayudaDialogo.show();	
		
		
		return ayudaDialogo.getPassword();
	}

	public JTextField getNombreDirectorioJavaField() {
		return nombreDirectorioJavaField;
	}
}
