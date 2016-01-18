/**
 * LoginDBDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.dbtable;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;

public class LoginDBDialog extends javax.swing.JDialog {
    
    private JButton cancelButton;
    private JButton loginButton;
    //private JLabel infoLabel;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JTextField loginNameText;  
    private JLabel loginLabel;
    private static String lastUserName=""; //$NON-NLS-1$
    private boolean canceled = true;
    
    private JPanel jPanel = null;
    private JPanel jPanel1 = null;
    //private JCheckBox chkbRemember = null;
    //private static String lastPassword=""; //$NON-NLS-1$
    private static boolean rememberPassword=true;
    private JLabel errorLabel = null;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard Identificadores = aplicacion.getBlackboard();
   
    
    public LoginDBDialog() {
        initialize();
    }
    
    /**
     * @param owner
     * @throws java.awt.HeadlessException
     */
    public LoginDBDialog(Frame owner) throws HeadlessException {
        super(owner);
        initialize();
    }
    
    private void initialize(){
        
        
        preInitGUI();
        
        this.setTitle(I18N.get("GestorCapas","acceso.basedatos"));
        
        loginLabel = new JLabel();
        loginNameText = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        loginButton = new JButton();
        cancelButton = new JButton();
        
        cancelButton.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                cancelButton(e);
            }
                });
        
        loginButton.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                acceptButton(e);
            }
                });
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setSize(new java.awt.Dimension(216,167));
        
        this.setContentPane(getJPanel());
        this.setResizable(false);  // Generated
        
        
        //infoLabel.setText(getExternalizedString("passwordBD")); //$NON-NLS-1$
        //infoLabel.setPreferredSize(new java.awt.Dimension(50,15));
        
        
        loginLabel.setText(getExternalizedString("loginName")); //$NON-NLS-1$
        loginLabel.setPreferredSize(new java.awt.Dimension(50,15));
        
        
        
        
        loginNameText.setNextFocusableComponent(passwordField);  // Generated
        loginNameText.setFocusCycleRoot(false);  // Generated
        
        
        
        passwordLabel.setText(getExternalizedString("password")); //$NON-NLS-1$
        passwordLabel.setPreferredSize(new java.awt.Dimension(50,15));
        
        
        
        passwordField.setNextFocusableComponent(loginButton);  // Generated
        passwordField.setPreferredSize(new java.awt.Dimension(6,20));  // Generated
        passwordField.setMinimumSize(new java.awt.Dimension(6,20));  // Generated
        
        
        loginButton.setText(getExternalizedString("loginButtonText")); //$NON-NLS-1$
        loginButton.setNextFocusableComponent(cancelButton);  // Generated
        
        cancelButton.setText(getExternalizedString("cancelButtonText")); //$NON-NLS-1$
        cancelButton.setNextFocusableComponent(loginNameText);  // Generated
        cancelButton.setSelected(true);  // Generated
        
        postInitGUI();
        
        
    }
    /** Add your pre-init code in here  */
    public void preInitGUI(){
        rememberPassword= !("".equals(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,null,false)));
    }
    
    /** Add your post-init code in here     */
    public void postInitGUI(){
        //lastUserName=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME,lastUserName,false); //$NON-NLS-1$
        //lastPassword=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,lastPassword,false); //$NON-NLS-1$
        setLogin("");
        setPassword("");
        //getRememberChkBox().setSelected(rememberPassword);
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
            //java.awt.GridBagConstraints gridBagConstraints0 = new GridBagConstraints();
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            
            //gridBagConstraints0.gridx = 0;
            //gridBagConstraints0.gridy = 1;
            //gridBagConstraints0.fill = java.awt.GridBagConstraints.HORIZONTAL;
            
            
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
            gridBagConstraints4.insets = new java.awt.Insets(5,0,0,0);  // Generated
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
            //jPanel.add(infoLabel, gridBagConstraints0);
            jPanel.add(loginNameText, gridBagConstraints1);
            jPanel.add(passwordLabel, gridBagConstraints3);
            jPanel.add(passwordField, gridBagConstraints4);  // Generated
            jPanel.add(loginLabel, gridBagConstraints5);
            jPanel.add(getJPanel1(), gridBagConstraints7);  // Generated
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
    /**
     * This method initializes jCheckBox    
     *  
     * @return javax.swing.JCheckBox    
     */    
    /*private JCheckBox getRememberChkBox() {
        if (chkbRemember == null) {
            chkbRemember = new JCheckBox();
            chkbRemember.setText(AppContext.getApplicationContext().getI18nString("LoginDialog.Remember")); //$NON-NLS-1$
        }
        return chkbRemember;
    }*/
    /** Auto-generated main method */
    public static void main(String[] args){
        showGUI();
    }
    
    /**
     * This static method creates a new instance of this class and shows
     * it inside a new JFrame, (unless it is already a JFrame).
     *
     * It is a convenience method for showing the GUI, but it can be
     * copied and used as a basis for your own code. *
     * It is auto-generated code - the body of this method will be
     * re-generated after any changes are made to the GUI.
     * However, if you delete this method it will not be re-created. */
    public static void showGUI(){
        try {
            LoginDBDialog inst = new LoginDBDialog();
            inst.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getExternalizedString(String key){
        return AppContext.getMessage(key);
    }
    public String getPassword()
    {
        return passwordField.getText();
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
    
    
    private void cancelButton(ActionEvent e)
    {
        canceled = true;
        this.setVisible(false);
        this.dispose();
    }
    
    private void acceptButton(ActionEvent e)
    {
        canceled=false;  
        this.setVisible(false);
        
        //rememberPassword=getRememberChkBox().isSelected();
        /*if (rememberPassword)
        {
            lastUserName=getLogin();
            lastPassword=getPassword();
            //UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME,lastUserName); //$NON-NLS-1$
            //TODO: Sustituir el almacenamiento por un hash o eliminar el almacen del password
            //UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,lastPassword); //$NON-NLS-1$
        }
        else
        {
            lastPassword=""; //$NON-NLS-1$
            //UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,lastPassword); //$NON-NLS-1$
            
        }*/
        
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
     * @param errorLabel The errorLabel to set.
     */
    public void setErrorLabel(String errorLabelText)
    {
        this.errorLabel.setText(errorLabelText);
    }
}  //  @jve:decl-index=0:visual-constraint="13,11"
