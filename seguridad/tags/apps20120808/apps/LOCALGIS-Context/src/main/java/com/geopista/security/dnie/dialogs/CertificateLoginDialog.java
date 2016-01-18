package com.geopista.security.dnie.dialogs;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.geopista.app.AppContext;
import com.geopista.security.dnie.CertificateManager;
import com.geopista.security.dnie.beans.KeyValue;
import com.geopista.security.dnie.utils.CertificateOperations;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

public class CertificateLoginDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = -3639136067461904021L;

	private boolean canceled = true;
	private boolean autenticated = false;

	private JButton selectSystemPathButton = null;
	private JButton acceptSystemPathButton = null;
	private JButton acceptCertificateStoreButton = null;
	private JButton cancelButton = null;

	private JLabel systemPathLabel = null;
	private JLabel certificateStoreLabel = null;

	private JPanel jPanel = null;
	private JPanel jPanelCertificateStore = null;
	private JPanel jPanelSystemPath = null;
	private JPanel jPanelCancel = null;

	private JTextField systemPathTextBox = null;
	private JComboBox certificateStoreComboBox = null;

	private PasswordDialog passwordDialog = null;
	private JFileChooser systemPathFileChooser = null;
	private File systemPathFile = null;

	private String storePassword = null;
	private String infoMessageTitle = null;
	private String errorMessageTitle = null;
	
	//private Dialog owner = null;
	private String idApp = null;

	public CertificateLoginDialog() {
		initialize();
	}

	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public CertificateLoginDialog(Dialog owner) throws HeadlessException {
		super(owner, true);
		//this.owner = owner;
		initialize();
	}
	
	/**
	 * @param owner
	 * @param idApp 
	 * @throws java.awt.HeadlessException
	 */
	public CertificateLoginDialog(Dialog owner,String idApp) throws HeadlessException {
		super(owner, true);
		//this.owner = owner;
		this.idApp = idApp;
		initialize();
	}
	
	private void initialize() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setName(getExternalizedString("CertificateLoginDialog")); //$NON-NLS-1$
		setSize(new Dimension(580, 175));
		setResizable(false);
		setTitle(getExternalizedString("dnie.certificateLoginDialog.title"));

		infoMessageTitle = getExternalizedString("dnie.infoMessage.title");
		errorMessageTitle = getExternalizedString("dnie.errorMessage.title");
		
		jPanel = new JPanel();
		jPanel.setLayout(null);

		// ---jPanelCertificateStore-----
		jPanelSystemPath = new JPanel();
		FlowLayout flowLayout = (FlowLayout) jPanelSystemPath.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		jPanelSystemPath.setBounds(10, 11, 555, 33);
		systemPathLabel = new JLabel();
		//systemPathLabel.setText((String) null);
		systemPathLabel.setPreferredSize(new Dimension(50, 15));
		systemPathLabel.setText(getExternalizedString("dnie.certificateLoginDialog.systemPathLabel.text") + ": "); 
		systemPathLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		systemPathLabel.setPreferredSize(new Dimension(120, 15));
		jPanelSystemPath.add(systemPathLabel);
		systemPathTextBox = new JTextField();
		systemPathTextBox.setEditable(false);
		systemPathTextBox.setColumns(27);
		jPanelSystemPath.add(systemPathTextBox);
		selectSystemPathButton = new JButton();
		selectSystemPathButton.setPreferredSize(new Dimension(90, 23));
		selectSystemPathButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectSystemPathButtonClick();
			}
		});
		selectSystemPathButton.setText(getExternalizedString("dnie.selectButton.text"));
		jPanelSystemPath.add(selectSystemPathButton);
		acceptSystemPathButton = new JButton();
		acceptSystemPathButton.setEnabled(false);
		acceptSystemPathButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (acceptSystemPathButton.isEnabled()) {
					acceptSystemPathButtonClick();
				}
			}
		});
		acceptSystemPathButton.setText(getExternalizedString("dnie.acceptButton.text"));
		jPanelSystemPath.add(acceptSystemPathButton);
		jPanel.add(jPanelSystemPath);
		// ------------------------------

		// ---jPanelCertificateStore-----
		jPanelCertificateStore = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) jPanelCertificateStore
				.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		jPanelCertificateStore.setBounds(10, 55, 555, 33);
		certificateStoreLabel = new JLabel();
		certificateStoreLabel.setText(getExternalizedString("dnie.certificateLoginDialog.systemCertStoreLabel.text") + ": ");
		certificateStoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		certificateStoreLabel.setPreferredSize(new Dimension(145, 15));
		jPanelCertificateStore.add(certificateStoreLabel);
		certificateStoreComboBox = new JComboBox();
		certificateStoreComboBox.setPreferredSize(new Dimension(297, 20));
		jPanelCertificateStore.add(certificateStoreComboBox);
		acceptCertificateStoreButton = new JButton();
		acceptCertificateStoreButton.setPreferredSize(new Dimension(90, 23));
		acceptCertificateStoreButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				acceptCertificateStoreButtonClick();
			}
		});
		acceptCertificateStoreButton.setText(getExternalizedString("dnie.acceptButton.text"));
		jPanelCertificateStore.add(acceptCertificateStoreButton);
		
		storePassword = "";
		if(!CertificateUtils.isDefaultWindowsCertificateStore()){
			passwordDialog = new PasswordDialog((JDialog)this.getParent(),getExternalizedString("dnie.certificateLoginDialog.systemCertStore.passwordDialog.title"));
			storePassword = passwordDialog.showDialog();
		}
		if(storePassword.equals("") || !passwordDialog.isCanceled()){
			List<KeyValue> certificateList = CertificateUtils
					.getPersonalCertificatesFromStore(storePassword);
			if (certificateList.size() > 0) {
					certificateStoreComboBox.setModel(new DefaultComboBoxModel(
							(KeyValue[]) certificateList
									.toArray(new KeyValue[certificateList.size()])));
			} else {
				certificateStoreComboBox.setEnabled(false);
				acceptCertificateStoreButton.setEnabled(false);
			}		
		} else {
			certificateStoreComboBox.setEnabled(false);
			acceptCertificateStoreButton.setEnabled(false);
		}		
		
		jPanel.add(jPanelCertificateStore);
		// -------------------------------

		// ---jPanelCancel-----
		jPanelCancel = new JPanel();
		jPanelCancel.setBounds(244, 99, 97, 33);
		cancelButton = new JButton();
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton(e);
			}
		});
		cancelButton.setText(getExternalizedString("dnie.cancelButton.text"));
		cancelButton.setSelected(true);
		jPanelCancel.add(cancelButton, null);
		jPanel.add(jPanelCancel);
		// --------------------

		this.setContentPane(jPanel);

	}

	public void showDialog() {
		GUIUtil.centreOnWindow(this);
		this.setVisible(true);
	}

	public String getExternalizedString(String key) {
		return AppContext.getMessage(key);
	}

	private void cancelButton(ActionEvent e) {
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	private void selectSystemPathButtonClick() {
		systemPathFileChooser = new JFileChooser();
		int returnVal = systemPathFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			acceptSystemPathButton.setEnabled(true);
			systemPathFile = systemPathFileChooser.getSelectedFile();
			systemPathTextBox.setText(systemPathFileChooser.getSelectedFile()
					.getPath());
		}
	}

	private void acceptSystemPathButtonClick() {
		if (systemPathFile != null) {
			passwordDialog = new PasswordDialog(this,getExternalizedString("dnie.certificateLoginDialog.systemPath.passwordDialog.title"));
			String filePassword = passwordDialog.showDialog();
			if (!passwordDialog.isCanceled()) {				
				CertificateManager certificateManager = new CertificateManager(
						systemPathFileChooser.getSelectedFile(),
						filePassword);
				if (certificateManager.isCertificateFNMT()) {
					// if(certificateManager.isValid()){	
					certificateLogin(certificateManager.getClientCert(), CertificateUtils.generateKeyManagerFactory(certificateManager.getKeyStore(),CertificateUtils.getPersonalCertificateStorePassword()));
						//certificateLogin(certificateManager.getClientCert(), certificateManager.generateKeyManagerFactory(password.toCharArray(), CertificateUtils.KEYSTORETYPE_JKS));
					// }					
				} else
					JOptionPane
							.showMessageDialog(this,
									getExternalizedString("dnie.certificateLoginDialog.certFNMT.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
			}
		} else
			JOptionPane.showMessageDialog(this,
					getExternalizedString("dnie.certificateLoginDialog.acceptSystemPath.filePath.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
	}

	private void acceptCertificateStoreButtonClick() {
		if (certificateStoreComboBox.getSelectedItem() != null) {
			CertificateManager certificateManager = new CertificateManager(
					((KeyValue)certificateStoreComboBox.getSelectedItem()).getValue(),storePassword);
			if (certificateManager.isCertificateFNMT()) {
				//JOptionPane.showMessageDialog(this, "El fichero es un certificado expedido por la FNMT");
				if (certificateManager.isValidNonExpired()) {			
					certificateLogin(certificateManager.getClientCert(), CertificateUtils.generateKeyManagerFactory(certificateManager.getKeyStore(),CertificateUtils.getPersonalCertificateStorePassword()));
				} else
					JOptionPane.showMessageDialog(this,
							getExternalizedString("dnie.certificateLoginDialog.acceptSystemCertStore.expiredCert.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
			} else
				JOptionPane.showMessageDialog(this,
						getExternalizedString("dnie.certificateLoginDialog.certFNMT.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
		}
	}

	private void certificateLogin(X509Certificate certificate, KeyManagerFactory kmf){
		if(CertificateOperations.certificateLogin(certificate,kmf,idApp)){
			autenticatedClose();
		} else
			JOptionPane
					.showMessageDialog(this,
							getExternalizedString("dnie.autentication.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
	}
	
	private void autenticatedClose(){
		this.canceled = false;
		this.autenticated = true;
		JOptionPane.showMessageDialog(this,
				getExternalizedString("dnie.sessionCreated.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
		this.setVisible(false);
	}
	
	public boolean isCanceled() {
		return this.canceled;
	}

	public boolean isAutenticated() {
		return this.autenticated;
	}
}
