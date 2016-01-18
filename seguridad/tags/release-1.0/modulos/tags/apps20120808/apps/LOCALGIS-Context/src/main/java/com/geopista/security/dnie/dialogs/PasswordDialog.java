package com.geopista.security.dnie.dialogs;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;



//VS4E -- DO NOT REMOVE THIS LINE!
public class PasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final String PREFERRED_LOOK_AND_FEEL = null;
	private boolean canceled = false;
	
	private JPasswordField passwordField = null;
	private JButton acceptButton = null;
	private JButton cancelButton = null;
	
	private String returnValue = null;	
	private String errorMessageTitle = null;
	
	public PasswordDialog() {
		initComponents();
	}

	public PasswordDialog(Frame parent) {
		super(parent, true);
		initComponents();
	}

	public PasswordDialog(Frame parent, String title) {
		super(parent, title, true);
		initComponents();
	}

	public PasswordDialog(Dialog parent) {
		super(parent, true);
		initComponents();
	}
	
	public PasswordDialog(Dialog parent, String title) {
		super(parent, title, true);
		initComponents();
	}	
	
	private void initComponents() {
		setSize(320, 129);
		getContentPane().setLayout(null);
		setResizable(false);		
		setModal(true);
		
		errorMessageTitle = getExternalizedString("dnie.errorMessage.title");
		
		passwordField = new JPasswordField();
		passwordField.setBounds(66, 18, 179, 20);
		//passwordField.setHorizontalAlignment(passwordField.CENTER);
		getContentPane().add(passwordField);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 49, 294, 33);
		getContentPane().add(panel);
		FlowLayout fl_panel = new FlowLayout();
		fl_panel.setAlignment(FlowLayout.CENTER);
		panel.setLayout(fl_panel);
				
		acceptButton = new JButton();
		acceptButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				acceptButtonClick(e);
			}
		});
		acceptButton.setText(getExternalizedString("loginButtonText"));
		acceptButton.setNextFocusableComponent(cancelButton);	
		panel.add(acceptButton);
		
		cancelButton = new JButton();
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancelButtonClick(e);
			}
		});
		cancelButton.setText(getExternalizedString("cancelButtonText"));
		cancelButton.setNextFocusableComponent(passwordField);  
		cancelButton.setSelected(true);	
		panel.add(cancelButton);
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PasswordDialog dialog = new PasswordDialog();
				dialog.setDefaultCloseOperation(PasswordDialog.DISPOSE_ON_CLOSE);
				dialog.setTitle("passwordDialog");
				dialog.setLocationRelativeTo(null);
				dialog.getContentPane().setPreferredSize(dialog.getSize());
				dialog.pack();
				dialog.setVisible(true);
			}
		});
	}
	
	public String getExternalizedString(String key){
		return AppContext.getMessage(key);
	}
	
	private void acceptButtonClick(MouseEvent e){
		if(passwordField.getText().trim().length()>0){
			returnValue=passwordField.getText().trim();			
			this.setVisible(false);
		}
		else JOptionPane.showMessageDialog(this, getExternalizedString("dnie.passwordDialog.passwordEmpty.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
	}
	
	private void cancelButtonClick(MouseEvent e){
		returnValue="";
		canceled = true;
		this.setVisible(false);
	}
	
	public boolean isCanceled(){
		return canceled;
	}
	
	public String showDialog(){
		GUIUtil.centreOnWindow(this);
		this.setVisible(true);
		return returnValue;
	}
	
}
