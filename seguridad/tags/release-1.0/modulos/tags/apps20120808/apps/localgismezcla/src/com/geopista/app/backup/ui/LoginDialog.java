package com.geopista.app.backup.ui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;



public class LoginDialog extends JDialog{
	private JButton cancelButton;
	private JButton loginButton;
	private JPasswordField passwordField;
	private JLabel passwordLabel;
	private JTextField loginNameText;  
	private JLabel loginLabel;
	private boolean canceled = true;

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel errorLabel = null;
	
	private I18N i18n = I18N.getInstance();
	
	public LoginDialog(Frame f)  {
		super(f);		
		setTitle("Login");
		initialize();
	}

	private void initialize(){
		
		loginLabel = new JLabel();
		loginNameText = new JTextField();
		passwordLabel = new JLabel();
		passwordField = new JPasswordField();
		loginButton = new JButton();
		cancelButton = new JButton();

		passwordField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) 
					acceptButton();
				
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {

			}

		});
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton();
			}
		});

		loginButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				acceptButton();
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setName(getExternalizedString("Login Dialog")); 
		setSize(new java.awt.Dimension(216,167));

		this.setContentPane(getJPanel());
		this.setResizable(false);  


		loginLabel.setText(getExternalizedString("User")); 
		loginLabel.setPreferredSize(new java.awt.Dimension(50,15));
		loginNameText.setFocusCycleRoot(false);  
		passwordLabel.setText(getExternalizedString("Password")); 
		passwordLabel.setPreferredSize(new java.awt.Dimension(50,15));
		passwordField.setPreferredSize(new java.awt.Dimension(6,20)); 
		passwordField.setMinimumSize(new java.awt.Dimension(6,20));  
		loginButton.setText(getExternalizedString("Aceptar")); 
		cancelButton.setText(getExternalizedString("Cancelar"));
		cancelButton.setSelected(true); 
	}

 
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
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 3;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(5,0,0,0);
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,5);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 5;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.insets = new java.awt.Insets(10,0,0,0);
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 4;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.gridwidth = 6;
			errorLabel.setText("");
			jPanel.add(loginNameText, gridBagConstraints1);
			jPanel.add(passwordLabel, gridBagConstraints3);
			jPanel.add(passwordField, gridBagConstraints4); 
			jPanel.add(loginLabel, gridBagConstraints5);
			jPanel.add(getJPanel1(), gridBagConstraints7); 
			//jPanel.add(getRememberChkBox(), gridBagConstraints9);
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
			flowLayout8.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel1.add(loginButton, null);
			jPanel1.add(cancelButton, null);
		}
		return jPanel1;
	}
	
	public String getPassword()
	{
		return new String(passwordField.getPassword());
	}
	public void setPassword(String passw)
	{
		passwordField.setText(passw);
	}
	public void setLogin(String login)
	{
		loginNameText.setText(login);
	}
	public String getLogin()
	{
		return loginNameText.getText();
	}


	private void cancelButton()
	{
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	private void acceptButton()
	{
		canceled=false;  
		this.setVisible(false);
		canceled=false;
		this.dispose();
	}

	public boolean isCanceled()
	{
		return this.canceled;
	}


	/**
	 * @return Returns the errorLabel.
	 */
	
	 public String getErrorLabel()
	 {
		 return errorLabel.getText();
	 }
	 
	 /**
	  * @param errorLabelText The errorLabel to set.
	  */
	 
	 public void setErrorLabel(String errorLabelText)
	 {
		 this.errorLabel.setText(errorLabelText);
	 }



	 private String getExternalizedString(String key) {
		 return i18n.getExternalizadedString(key);
	 }
}
