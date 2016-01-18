package com.geopista.ui.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.geopista.ui.images.IconLoader;
import javax.net.ssl.KeyManagerFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import com.geopista.app.AppContext;
import com.geopista.security.dnie.DNIeManager;
import com.geopista.security.dnie.dialogs.CertificateLoginDialog;
import com.geopista.security.dnie.dialogs.PasswordDialog;
import com.geopista.security.dnie.utils.CertificateOperations;
import com.geopista.security.dnie.utils.CertificateUtils;
import java.awt.Dimension;
import java.security.cert.X509Certificate;

public class LoginDialog extends javax.swing.JDialog {
	private JButton cancelButton;
	private JButton loginButton;
	private JButton sinconButton;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JTextField loginNameText; // @jve:decl-index=0:visual-constraint="35,142"
	private JLabel loginLabel;
	private static String lastUserName = ""; //$NON-NLS-1$
	private boolean canceled = true;

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;

	private String rememberPassword = null;
	// private JCheckBox chkbRemember = null;
	private static String lastPassword = ""; //$NON-NLS-1$
	// private static boolean rememberPassword=false;
	private JLabel errorLabel = null;
	private boolean noconectar=false;
	private String idApp;

	// ----NUEVO---->
	private static final String IMAGE_BTN_DNIE = "btn_dnie.gif";
	// private JButton dnieButton;
	private JButton certificateButton;
	private boolean autenticated = false;
	private JPanel jPanel2 = null;
	private Frame owner = null;
	private CertificateLoginDialog certificateLoginDialog = null;
	private PasswordDialog passwordDialog = null;
	private String infoMessageTitle = null;
	private String errorMessageTitle = null;
	// ----FIN NUEVO---->

	public LoginDialog() {
		initialize(false);
	}
	
	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public LoginDialog(Frame owner) throws HeadlessException {
		super(owner);
		initialize(false);
	}

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public LoginDialog(Frame owner, boolean noconectar) throws HeadlessException {
		super(owner);
		this.owner = owner;
		this.noconectar = noconectar;
		initialize(noconectar);
	}

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public LoginDialog(Frame owner, String idApp, boolean noconectar) throws HeadlessException {
		super(owner);
		this.owner = owner;
		this.idApp = idApp;
		this.noconectar = noconectar;
		initialize(noconectar);
	}

	private void initialize(boolean noconectar) {
	
		preInitGUI();

		loginLabel = new JLabel();
		loginNameText = new JTextField();
		passwordLabel = new JLabel();
		passwordField = new JPasswordField();
		loginButton = new JButton();
		cancelButton = new JButton();
		sinconButton = new JButton();
		// ----NUEVO---->
		certificateButton = new JButton();
		// --FIN NUEVO-->
	
		sinconButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e)
	        { 
	        	sinconButton(e);
	        }
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton(e);
			}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptButton(e);
			}
		});
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setName(getExternalizedString("LoginDialog")); //$NON-NLS-1$
		if(noconectar) setSize(new java.awt.Dimension(320,167));
		else setSize(new java.awt.Dimension(216,167));
		
		
		this.setContentPane(getJPanel());
		this.setResizable(false); // Generated

		loginLabel.setText(getExternalizedString("loginName")); //$NON-NLS-1$
		loginLabel.setPreferredSize(new Dimension(50, 15));

		loginNameText.setNextFocusableComponent(passwordField); // Generated
		loginNameText.setFocusCycleRoot(false); // Generated

		passwordLabel.setText(getExternalizedString("password")); //$NON-NLS-1$
		passwordLabel.setPreferredSize(new Dimension(50, 15));

		passwordField.setNextFocusableComponent(loginButton); // Generated
		passwordField.setPreferredSize(new java.awt.Dimension(6, 20)); // Generated
		passwordField.setMinimumSize(new Dimension(6, 20)); // Generated

		loginButton.setText(getExternalizedString("loginButtonText")); //$NON-NLS-1$
		loginButton.setNextFocusableComponent(cancelButton); // Generated

		cancelButton.setText(getExternalizedString("cancelButtonText")); //$NON-NLS-1$
		cancelButton.setNextFocusableComponent(loginNameText); // Generated
		cancelButton.setSelected(true); // Generated
		sinconButton.setText("Sin conexión");
		
		// ----NUEVO---->
		if (CertificateUtils.isDNIeActive()) {
			
			errorMessageTitle = getExternalizedString("dnie.errorMessage.title");
			infoMessageTitle = getExternalizedString("dnie.infoMessage.title");
			
			cancelButton.setNextFocusableComponent(certificateButton);

			certificateButton.setText(getExternalizedString("dnie.certButton.text"));
			certificateButton.setPreferredSize(new Dimension(175, 50));
			certificateButton.setNextFocusableComponent(loginNameText);
			certificateButton.setIcon(IconLoader.icon(IMAGE_BTN_DNIE));

			certificateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					certificateButtonClick(e);
				}
			});
		}
		// ----FIN NUEVO---->
		
		postInitGUI();

	}

	/** Add your pre-init code in here */
	public void preInitGUI() {
		// rememberPassword=
		// !("".equals(AppContext.getApplicationContext().getUserPreference("LAST_PASS",null,false)));
	}

	/** Add your post-init code in here */
	public void postInitGUI() {

		rememberPassword = AppContext.getApplicationContext()
				.getUserPreference("localgis.remember.password", "", false);

		lastUserName = AppContext.getApplicationContext().getUserPreference(
				"LAST_LOGIN", lastUserName, false); //$NON-NLS-1$
		if ((rememberPassword != null && !rememberPassword.equals("")) 
				&& (rememberPassword.equalsIgnoreCase("YES"))){
			lastPassword = AppContext.getApplicationContext()
					.getUserPreference("LAST_PASS", lastPassword, false); //$NON-NLS-1$			
			setPassword(lastPassword);
		}
		setLogin(lastUserName);
		// getRememberChkBox().setSelected(rememberPassword);
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			errorLabel = new JLabel();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 4;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 5);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 5;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 0); // Generated
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 4;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0, 0, 0, 5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 6;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.insets = new java.awt.Insets(10, 0, 0, 0);
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 6;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 7;
			gridBagConstraints11.gridwidth = 6;
			gridBagConstraints11.gridheight = 60;
			errorLabel.setText("");

			// ----NUEVO---->
			if (CertificateUtils.isDNIeActive()) {
				if(noconectar) setSize(new Dimension(320,225));
				else setSize(new Dimension(250,225));				
				java.awt.GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
				gridBagConstraints1.gridy = 6;
				gridBagConstraints3.gridy = 7;
				gridBagConstraints4.gridy = 7;
				gridBagConstraints5.gridy = 6;
				gridBagConstraints7.gridy = 8;
				gridBagConstraints8.gridx = 0;
				gridBagConstraints8.gridy = 0;
				gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints8.anchor = java.awt.GridBagConstraints.PAGE_START;
				gridBagConstraints8.ipady = 10;
				gridBagConstraints8.gridwidth = 2;
				gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 0);
				gridBagConstraints9.gridy = 8;
				gridBagConstraints11.gridy = 9;
				jPanel.add(getJPanel2(), gridBagConstraints8); // Generated
			}
			// ----FIN NUEVO---->

			jPanel.add(loginLabel, gridBagConstraints5);
			jPanel.add(loginNameText, gridBagConstraints1);
			jPanel.add(passwordLabel, gridBagConstraints3);
			jPanel.add(passwordField, gridBagConstraints4); // Generated
			jPanel.add(getJPanel1(), gridBagConstraints7); // Generated
			// jPanel.add(getRememberChkBox(), gridBagConstraints9);
			jPanel.add(errorLabel, gridBagConstraints11);

		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			java.awt.FlowLayout flowLayout8 = new FlowLayout();
			jPanel1 = new JPanel();
			jPanel1.setLayout(flowLayout8);
			flowLayout8.setAlignment(java.awt.FlowLayout.CENTER);
			jPanel1.add(loginButton, null);
			jPanel1.add(cancelButton, null);
			if(noconectar) jPanel1.add(sinconButton, null);
		}
		return jPanel1;
	}

	// ----NUEVO---->
	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			java.awt.FlowLayout flowLayout0 = new FlowLayout();
			jPanel2 = new JPanel();
			jPanel2.setLayout(flowLayout0);
			flowLayout0.setAlignment(java.awt.FlowLayout.CENTER);
			jPanel2.add(certificateButton, null);
		}
		return jPanel2;
	}
	// ----FIN NUEVO---->
	
	/**
	 * This method initializes jCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/*
	 * private JCheckBox getRememberChkBox() { if (chkbRemember == null) {
	 * chkbRemember = new JCheckBox();
	 * chkbRemember.setText(AppContext.getApplicationContext
	 * ().getI18nString("LoginDialog.Remember")); //$NON-NLS-1$ } return
	 * chkbRemember; }
	 */
	/** Auto-generated main method */
	public static void main(String[] args) {
		showGUI();
	}

	/**
	 * This static method creates a new instance of this class and shows it
	 * inside a new JFrame, (unless it is already a JFrame).
	 * 
	 * It is a convenience method for showing the GUI, but it can be copied and
	 * used as a basis for your own code. * It is auto-generated code - the body
	 * of this method will be re-generated after any changes are made to the
	 * GUI. However, if you delete this method it will not be re-created.
	 */
	public static void showGUI() {
		try {
			LoginDialog inst = new LoginDialog();
			inst.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getExternalizedString(String key) {
		return AppContext.getMessage(key);
	}

	public String getPassword() {
		return passwordField.getText();
	}

	public void setPassword(String passw) {
		passwordField.setText(passw);
	}

	public void setLogin(String login) {
		loginNameText.setText(login);
	}

	public String getLogin() {
		return loginNameText.getText().toLowerCase();
	}

	private void cancelButton(ActionEvent e) {
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	private void acceptButton(ActionEvent e) {
		canceled = false;
		this.setVisible(false);
		// rememberPassword=getRememberChkBox().isSelected();
		// if (rememberPassword)
		// {
		// lastUserName=getLogin();
		// lastPassword=getPassword();
		//   AppContext.getApplicationContext().setUserPreference("LAST_LOGIN",lastUserName); //$NON-NLS-1$
		// TODO: Sustituir el almacenamiento por un hash o eliminar el almacen
		// del password
		//   AppContext.getApplicationContext().setUserPreference("LAST_PASS",lastPassword); //$NON-NLS-1$
		// }
		// else
		// {
		//lastPassword=""; //$NON-NLS-1$
		// lastPassword=getPassword();
		//AppContext.getApplicationContext().setUserPreference("LAST_PASS",lastPassword); //$NON-NLS-1$

		// }
		rememberPassword = AppContext.getApplicationContext()
		.getUserPreference("localgis.remember.password", "", false);
		if ((rememberPassword != null && !rememberPassword.equals("")) 
				&& (rememberPassword.equalsIgnoreCase("YES"))){
		lastPassword = getPassword();
//		AppContext.getApplicationContext().setUserPreference(
//				"LAST_PASS", lastPassword);
		}
		else{
			lastPassword = "";
//			AppContext.getApplicationContext().setUserPreference(
//					"LAST_PASS", lastPassword); //$NON-NLS-1$ 
		}
		
		lastUserName = getLogin();
		AppContext.getApplicationContext().setUserPreference("LAST_LOGIN",
				lastUserName);

		//NUEVO
		AppContext.getApplicationContext().setUserPreference(
				"LAST_PASS", getPassword());
		//FIN NUEVO
		
		canceled = false;
		this.dispose();
	}
		
	public boolean isSinCon(){
		return this.noconectar;
	}
	
	private void sinconButton(ActionEvent e)
	{		
		canceled = false;
		autenticated = true;
		this.setVisible(false);
	}
	
	//----NUEVO---->
	private void certificateButtonClick(ActionEvent e) {
		DNIeManager dnieManager = new DNIeManager();
		if (dnieManager.isDNIe()) {
			passwordDialog = new PasswordDialog(
					this,
					getExternalizedString("dnie.loginDialog.certificateButtonClick.passwordDialog.title"));
			String pin = passwordDialog.showDialog();
			if (!passwordDialog.isCanceled()) {
				if (dnieManager.accessPin(pin)) {
					passwordDialog.setVisible(false);
					certificateLogin(dnieManager.getClientCert(),
							CertificateUtils.generateKeyManagerFactory(
									dnieManager.getKeyStore(),
									pin.toCharArray()));
					// certificateLogin(dnieManager.getClientCert(),
					// dnieManager.generateKeyManagerFactory(pin,CertificateUtils.KEYSTORETYPE_JKS));
				} else
					JOptionPane
							.showMessageDialog(
									this,
									getExternalizedString("dnie.loginDialog.certificateButtonClick.pin.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
			}
		} else {
			certificateLoginDialog = new CertificateLoginDialog(this, idApp);
			certificateLoginDialog.showDialog();
			if (!certificateLoginDialog.isCanceled()
					&& certificateLoginDialog.isAutenticated()) {
				autenticatedClose();
			}
		}
	}

	private void autenticatedClose() {
		canceled = false;
		autenticated = true;
		this.setVisible(false);
	}

	private void certificateLogin(X509Certificate certificate,
			KeyManagerFactory kmf) {
		if (CertificateOperations.certificateLogin(certificate, kmf, idApp)) {
			JOptionPane.showMessageDialog(this,
					getExternalizedString("dnie.sessionCreated.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
			autenticatedClose();
		} else
			JOptionPane.showMessageDialog(this,
					getExternalizedString("dnie.autentication.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
	}

	public boolean isAutenticated() {
		return this.autenticated;
	}
	//--FIN NUEVO-->

	public boolean isCanceled() {
		return this.canceled;
	}

	/**
	 * @return Returns the errorLabel.
	 */
	public String getErrorLabel() {
		return errorLabel.getText();
	}

	/**
	 * @param errorLabel
	 *            The errorLabel to set.
	 */
	public void setErrorLabel(String errorLabelText) {
		cleanPassword();
		this.errorLabel.setText(errorLabelText);
	}
	
	public void cleanPassword(){
    	rememberPassword = AppContext.getApplicationContext()
		.getUserPreference("localgis.remember.password", "", false);
		if (!((rememberPassword != null && !rememberPassword.equals("")) 
				&& (rememberPassword.equalsIgnoreCase("YES")))){    			
    		setPassword("");
		}
	}
	
	
} // @jve:decl-index=0:visual-constraint="13,11"

