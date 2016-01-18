/**
 * SystemConfigPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.mantis.MantisConstants;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.security.dnie.DNIeManager;
import com.geopista.security.dnie.beans.OperativeSystemConfig;
import com.geopista.security.dnie.dialogs.PasswordDialog;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.geopista.security.dnie.utils.OperativeSystemConfigList;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OptionsPanel;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**  Panel for configuring several system-wide properties. <br> Many of the properties are made persistent via AppContext.setUserPreferences
 * @author juacas
 * @see AppContext.setUserPreferences() 
 *  */
public class SystemConfigPanel extends JPanel  implements OptionsPanel
{
	/**
	 * Logger for this class
	 */
	//private static final Log logger = LogFactory
	//		.getLog(SystemConfigPanel.class);

	 private static final Log logger;
	    static {
	       createDir();
	       logger  = LogFactory.getLog(SystemConfigPanel.class);
	    }
	//----NUEVO---->
	private static final String IMAGE_BTN_TEST = "btn_test.gif";    
	private static final String IMAGE_BTN_TEST_ERROR = "btn_test_error.gif";    
	    
	private JLabel operativeSystemLabel = null;
	private JComboBox operativeSystemComboBox = null;	    
	private JLabel systemCertStoreLabel = null;	
	private JTextField systemCertStoreTextField = null;
	private JButton systemCertStoreButton = null;
	private JButton systemCertStoreTestButton = null;
	private JLabel dnieLibraryLabel = null;  
	private JTextField dnieLibraryTextField = null; 	
	private JButton dnieLibraryButton = null;
	private JButton dnieLibraryTestButton = null;	
	private PasswordDialog passwordDialog = null;
	private JLabel dniePortLabel = null;  
	private JTextField dniePortTextField = null; 
	private JTextField urlMantis = null;   
	private JTextField userMantis = null;   
	private JPasswordField passwordMantis = null;
	private JLabel urlMantisLabel = null;   
	private JLabel userMantisLabel = null;   
	private JLabel passwordMantisLabel = null;
	
	
	private OperativeSystemConfigList operativeSystemConfigList;
	private boolean osSelected = false;
	
	private String confirmMessageTitle = null;
	private String errorMessageTitle = null;
	private String infoMessageTitle = null;		
	//--FIN NUEVO-->
	
	private JLabel dataFolderLabel = null;
	private JTextField dataFolderField = null;
	private JLabel texturesFolder = null;
	private JTextField texturesFolderField = null;
	private JLabel serverAddress = null;
	private JTextField serverAddressField = null;
	private JLabel preferredLocale = null;
	private JComboBox jComboBox = null;
	private ApplicationContext aplicacion = AppContext.getApplicationContext(false);  //  @jve:decl-index=0:
	private JPanel jPanel = null;
	private JButton generarTexturasButton = null;
/*	private JLabel codigoMunicipioLabel = null;
	private JFormattedTextField idMunicioValue = null;*/
	private JTextField updateURLTextField = null;
	private JLabel updateURLJLabel = null;
	private JButton generateTemplatesButton = null;
	//private JTextField dataBaseAddress = null;
	//private JLabel serverDBAddressLabel = null;

	private JTextField reportUserTextField = null;
	private JLabel reportUserLabel = null;
	
	private JPasswordField reportpasswordTextField = null;
	private JLabel reportPasswordLabel = null;

	private JTextField reportUrlJasperTextField = null;
	private JLabel reportUrlJasperLabel = null;

	private JButton dataFolderButton = null;

	private JButton texturesButton = null;

    private JLabel urlTomcatJLabel = null;

    private JTextField urlTomcatJTextField = null;

    private JLabel urlMapServerJLabel = null;

    private JTextField urlMapServerJTextField = null;
    
    private String sistemaOperativo;
    
    private JLabel urlConsoleUerWSJLabel = null;
    private JTextField urlConsoleUeRWSJTextField = null;
    
    private JLabel urlIgnWSJLabel = null;
	private JTextField urlIgnWSJTextField = null;
	
	
	private JLabel urlOpenAM = null;
	private JTextField urlOpenAMTextField = null;

	public static void createDir(){
    	File file = new File("logs");

    	if (! file.exists()) {
    		file.mkdirs();
    	}
    }     
	/**
	 * This method initializes dataFolderField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getDataFolderField() {
		if (dataFolderField == null) {
			dataFolderField = new JTextField();
			dataFolderField.setColumns(12);
		}
		return dataFolderField;
	}
	
	/**
	 * This method initializes texturesFolderField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTexturesFolderField() {
		if (texturesFolderField == null) {
			texturesFolderField = new JTextField();
			texturesFolderField.setColumns(12);
		}
		return texturesFolderField;
	}
	/**
	 * This method initializes serverAddressField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getServerAddressField() {
		if (serverAddressField == null) {
			serverAddressField = new JTextField();
			serverAddressField.setColumns(12);
		}
		return serverAddressField;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
		}
		return jComboBox;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
		
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.BOTH;
		gridBagConstraints4.gridy = 10;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints.gridy = 10;

		GridBagConstraints gridBagConstraintsMapServerLabel = new GridBagConstraints();
		gridBagConstraintsMapServerLabel.gridx = 0;
		gridBagConstraintsMapServerLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsMapServerLabel.gridwidth = 1;
		gridBagConstraintsMapServerLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsMapServerLabel.gridy = 11;
		GridBagConstraints gridBagConstraintsMapServerText= new GridBagConstraints();
		gridBagConstraintsMapServerText.gridx = 1;
		gridBagConstraintsMapServerText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsMapServerText.gridwidth = 1;
		gridBagConstraintsMapServerText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsMapServerText.gridy = 11;
		
		GridBagConstraints gridBagConstraintsURLMantisLabel = new GridBagConstraints();
		gridBagConstraintsURLMantisLabel.gridx = 0;
		gridBagConstraintsURLMantisLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsURLMantisLabel.gridwidth = 1;
		gridBagConstraintsURLMantisLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsURLMantisLabel.gridy = 12;
		GridBagConstraints gridBagConstraintsURLMantisText= new GridBagConstraints();
		gridBagConstraintsURLMantisText.gridx = 1;
		gridBagConstraintsURLMantisText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsURLMantisText.gridwidth = 1;
		gridBagConstraintsURLMantisText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsURLMantisText.gridy = 12;	
		
		GridBagConstraints gridBagConstraintsUserMantisLabel = new GridBagConstraints();
		gridBagConstraintsUserMantisLabel.gridx = 0;
		gridBagConstraintsUserMantisLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUserMantisLabel.gridwidth = 1;
		gridBagConstraintsUserMantisLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUserMantisLabel.gridy = 13;
		GridBagConstraints gridBagConstraintsUserMantisText= new GridBagConstraints();
		gridBagConstraintsUserMantisText.gridx = 1;
		gridBagConstraintsUserMantisText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUserMantisText.gridwidth = 1;
		gridBagConstraintsUserMantisText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUserMantisText.gridy = 13;	
		
		GridBagConstraints gridBagConstraintsPasswordMantisLabel = new GridBagConstraints();
		gridBagConstraintsPasswordMantisLabel.gridx = 0;
		gridBagConstraintsPasswordMantisLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsPasswordMantisLabel.gridwidth = 1;
		gridBagConstraintsPasswordMantisLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsPasswordMantisLabel.gridy = 14;
		GridBagConstraints gridBagConstraintsPasswordMantisText= new GridBagConstraints();
		gridBagConstraintsPasswordMantisText.gridx = 1;
		gridBagConstraintsPasswordMantisText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsPasswordMantisText.gridwidth = 1;
		gridBagConstraintsPasswordMantisText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsPasswordMantisText.gridy = 14;	
		
		
		GridBagConstraints gridBagConstraintsUrlConsoleGestorFipLabel = new GridBagConstraints();
		gridBagConstraintsUrlConsoleGestorFipLabel.gridx = 0;
		gridBagConstraintsUrlConsoleGestorFipLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUrlConsoleGestorFipLabel.gridwidth = 1;
		gridBagConstraintsUrlConsoleGestorFipLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUrlConsoleGestorFipLabel.gridy = 20;
		GridBagConstraints gridBagConstraintsUrlConsoleGestorFipText= new GridBagConstraints();
		gridBagConstraintsUrlConsoleGestorFipText.gridx = 1;
		gridBagConstraintsUrlConsoleGestorFipText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUrlConsoleGestorFipText.gridwidth = 1;
		gridBagConstraintsUrlConsoleGestorFipText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUrlConsoleGestorFipText.gridy = 20;	
		
		GridBagConstraints gridBagConstraintsUrlIgnLabel = new GridBagConstraints();
		gridBagConstraintsUrlIgnLabel.gridx = 0;
		gridBagConstraintsUrlIgnLabel.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUrlIgnLabel.gridwidth = 1;
		gridBagConstraintsUrlIgnLabel.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUrlIgnLabel.gridy = 21;
		GridBagConstraints gridBagConstraintsUrlIgnText= new GridBagConstraints();
		gridBagConstraintsUrlIgnText.gridx = 1;
		gridBagConstraintsUrlIgnText.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsUrlIgnText.gridwidth = 1;
		gridBagConstraintsUrlIgnText.insets = new Insets(0, 5, 5, 5);
		gridBagConstraintsUrlIgnText.gridy = 21;
		
		
		urlTomcatJLabel = new JLabel();
		urlTomcatJLabel.setText(I18N.get("SystemConfigPanel.urlTomcat"));
		urlMapServerJLabel = new JLabel();
		urlMapServerJLabel.setText(I18N.get("SystemConfigPanel.urlMapServer"));
		urlMantisLabel = new JLabel();
		urlMantisLabel.setText(I18N.get("SystemConfigPanel.urlMantis"));
		userMantisLabel = new JLabel();
		userMantisLabel.setText(I18N.get("SystemConfigPanel.userMantis"));
		passwordMantisLabel = new JLabel();
		passwordMantisLabel.setText(I18N.get("SystemConfigPanel.paswwordMantis"));
		
		urlConsoleUerWSJLabel = new JLabel();
		urlConsoleUerWSJLabel.setText(I18N.get("SystemConfigPanel.urlConsoleUerWS"));
		
		urlIgnWSJLabel = new JLabel();
		urlIgnWSJLabel.setText(I18N.get("SystemConfigPanel.urlIgnWS"));
		
		
		serverAddress=new JLabel();
		texturesFolder= new JLabel();
		dataFolderLabel = new JLabel();
		preferredLocale= new JLabel();
        reportUserLabel = new JLabel();
        reportPasswordLabel = new JLabel();
        reportUrlJasperLabel = new JLabel();
		//serverDBAddressLabel = new JLabel();
		updateURLJLabel = new JLabel();
/*		codigoMunicipioLabel = new JLabel();
		codigoMunicipioLabel.setText(I18N.get("SystemConfigPanel.munCode")); //$NON-NLS-1$*/
		updateURLJLabel.setText(I18N.get("SystemConfigPanel.UpdateDataURL"));  // Generated //$NON-NLS-1$
		reportUserLabel.setText(I18N.get("SystemConfigPanel.ReportUser"));  // Generated //$NON-NLS-1$
		reportPasswordLabel.setText(I18N.get("SystemConfigPanel.ReportUserPass"));  // Generated //$NON-NLS-1$
        reportUrlJasperLabel.setText(I18N.get("SystemConfigPanel.ReportUrlJasper"));  // Generated //$NON-NLS-1$
		//serverDBAddressLabel.setText(I18N.get("SystemConfigPanel.DB_Address"));  // Generated //$NON-NLS-1$
		dataFolderLabel.setText(I18N.get("SystemConfigPanel.dataFolderLabel")); //$NON-NLS-1$
		texturesFolder.setText(I18N.get("SystemConfigPanel.texturesFolderLabel")); //$NON-NLS-1$
		serverAddress.setText(I18N.get("SystemConfigPanel.serverAddress")); //$NON-NLS-1$
		preferredLocale.setText(I18N.get("SystemConfigPanel.preferredLocale")); //$NON-NLS-1$
		
			GridBagConstraints gridBagConstraints_TextField_ReportPass = new GridBagConstraints();
			GridBagConstraints gridBagConstraints_TextField_ReportUser = new GridBagConstraints();
			GridBagConstraints gridBagConstraints_Label_ReportUser = new GridBagConstraints();
			GridBagConstraints gridBagConstraints_Label_ReportPass = new GridBagConstraints();
		
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			//GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.insets = new Insets(0, 5, 5, 5);
			//GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			GridBagConstraints gridBagConstraints_Label_Locale = new GridBagConstraints();
			GridBagConstraints gridBagConstraints_TextField_Locale = new GridBagConstraints();
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints34 = new GridBagConstraints();

            
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints13.weightx = 1.0D;  // Generated
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints14.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints15.weightx = 1.0D;  // Generated
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.gridy = 1;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.gridy = 4;
			gridBagConstraints17.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.CENTER;  // Generated
			gridBagConstraints17.weightx = 1.0D;  // Generated
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 4;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.insets = new Insets(0, 5, 5, 5);
			
			gridBagConstraints_TextField_Locale.gridx = 0;
			gridBagConstraints_TextField_Locale.gridy = 9;
			gridBagConstraints_TextField_Locale.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints_TextField_Locale.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints_TextField_Locale.weightx = 1.0D;  // Generated
			gridBagConstraints_Label_Locale.gridx = 1;
			gridBagConstraints_Label_Locale.gridy = 9;
			gridBagConstraints_Label_Locale.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints_Label_Locale.insets = new Insets(0, 5, 5, 5);
			
			
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints2.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 8;
			gridBagConstraints21.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 7;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(0,0,5,10);
			gridBagConstraints1.gridx = 1;  // Generated
			gridBagConstraints1.gridy = 2;  // Generated
			gridBagConstraints1.weightx = 1.0;  // Generated
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints1.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints22.gridx = 0;  // Generated
			gridBagConstraints22.gridy = 2;  // Generated
			gridBagConstraints22.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints31.gridx = 0;  // Generated
			gridBagConstraints31.gridy = 3;  // Generated
			gridBagConstraints31.insets = new java.awt.Insets(0,5,5,5);  // Generated
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.EAST;  // Generated
			
			//gridBagConstraints11.gridx = 1;  // Generated
			//gridBagConstraints11.gridy = 5;  // Generated
			//gridBagConstraints11.weightx = 1.0;  // Generated
			//gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			//gridBagConstraints11.insets = new Insets(0, 5, 5, 5);  // Generated
			//gridBagConstraints23.gridx = 0;  // Generated
			//gridBagConstraints23.gridy = 5;  // Generated
			//gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			//gridBagConstraints23.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints32.gridx = 1;  // Generated
			gridBagConstraints32.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints32.gridy = 8;  // Generated
			gridBagConstraints32.weightx = 1.0;  // Generated
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			
			
			
			gridBagConstraints_TextField_ReportUser.gridx = 1;  // Generated
			gridBagConstraints_TextField_ReportUser.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints_TextField_ReportUser.gridy = 7;  // Generated
			gridBagConstraints_TextField_ReportUser.weightx = 1.0;  // Generated
			gridBagConstraints_TextField_ReportUser.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			
			gridBagConstraints_TextField_ReportPass.gridx = 1;  // Generated
			gridBagConstraints_TextField_ReportPass.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints_TextField_ReportPass.gridy = gridBagConstraints_TextField_ReportUser.gridy+1;  // Generated
			gridBagConstraints_TextField_ReportPass.weightx = 1.0;  // Generated
			gridBagConstraints_TextField_ReportPass.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			
			
			gridBagConstraints_Label_ReportUser.gridx = 0;  // Generated
			gridBagConstraints_Label_ReportUser.gridy = 7;  // Generated
			gridBagConstraints_Label_ReportUser.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints_Label_ReportUser.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints_Label_ReportUser.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			
			gridBagConstraints_Label_ReportPass.gridx = 0;  // Generated
			gridBagConstraints_Label_ReportPass.gridy = gridBagConstraints_Label_ReportUser.gridy+1;  // Generated
			gridBagConstraints_Label_ReportPass.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints_Label_ReportPass.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints_Label_ReportPass.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
		
			
			
			
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			
            gridBagConstraints33.gridx = 0;  // Generated
            gridBagConstraints33.gridy = 6;  // Generated
            gridBagConstraints33.anchor = java.awt.GridBagConstraints.WEST;  // Generated
            gridBagConstraints33.insets = new Insets(0, 5, 5, 5);  // Generated
            gridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
            
            gridBagConstraints34.gridx = 1;  // Generated
            gridBagConstraints34.insets = new Insets(0, 5, 5, 5);
            gridBagConstraints34.gridy = 6;  // Generated
            gridBagConstraints34.weightx = 1.0;  // Generated
            gridBagConstraints34.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated

            
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, AppContext.getMessage("SystemConfigPanel.PanelTitleSystemConfig"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$
//			jPanel.add(codigoMunicipioLabel, gridBagConstraints21);  // Generated
			jPanel.add(preferredLocale, gridBagConstraints_TextField_Locale);
			jPanel.add(getJComboBox(), gridBagConstraints_Label_Locale);
			jPanel.add(dataFolderLabel, gridBagConstraints13);
			jPanel.add(serverAddress, gridBagConstraints17);  // Generated
			jPanel.add(getServerAddressField(), gridBagConstraints18);
			jPanel.add(getDataFolderField(), gridBagConstraints14);
			jPanel.add(texturesFolder, gridBagConstraints15);
			jPanel.add(getTexturesFolderField(), gridBagConstraints16);
			
			//Plantillas y texturas se agrupan
			jPanel.add(getGenerarTexturasButton(), gridBagConstraints2);
			jPanel.add(getUpdateURLTextField(), gridBagConstraints1);
			jPanel.add(updateURLJLabel, gridBagConstraints22);
			jPanel.add(getGenerateTemplatesButton(), gridBagConstraints31);  // Generated
			//jPanel.add(getDataBaseAddress(), gridBagConstraints11);
			//jPanel.add(serverDBAddressLabel, gridBagConstraints23);
//			jPanel.add(getIdMunicipioField(), gridBagConstraints32);
            jPanel.add(getReportUserTextField(), gridBagConstraints_TextField_ReportUser);
            jPanel.add(getReportpasswordTextField(), gridBagConstraints_TextField_ReportPass);
            jPanel.add(getReportUrlJasperTextField(), gridBagConstraints34);
			gridBagConstraints110.gridx = 2;  // Generated
			gridBagConstraints110.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints110.gridy = 1;  // Generated
			
			jPanel.add(reportUserLabel, gridBagConstraints_Label_ReportUser);
			jPanel.add(reportPasswordLabel, gridBagConstraints_Label_ReportPass);
			
            jPanel.add(reportUrlJasperLabel, gridBagConstraints33);
			jPanel.add(getDataFolderButton(), gridBagConstraints29);
			jPanel.add(getTexturesButton(), gridBagConstraints110);
			jPanel.add(urlTomcatJLabel, gridBagConstraints);
			jPanel.add(getUrlTomcatJTextField(), gridBagConstraints4);
			jPanel.add(urlMapServerJLabel, gridBagConstraintsMapServerLabel);
			jPanel.add(getUrlMapServerJTextField(), gridBagConstraintsMapServerText);
			jPanel.add(urlMantisLabel, gridBagConstraintsURLMantisLabel);
			jPanel.add(getUrlMantisJTextField(), gridBagConstraintsURLMantisText);
			jPanel.add(userMantisLabel, gridBagConstraintsUserMantisLabel);
			jPanel.add(getUserMantisJTextField(), gridBagConstraintsUserMantisText);
			jPanel.add(passwordMantisLabel, gridBagConstraintsPasswordMantisLabel);
			jPanel.add(getPasswordMantisJTextField(), gridBagConstraintsPasswordMantisText);
			
			jPanel.add(urlConsoleUerWSJLabel, gridBagConstraintsUrlConsoleGestorFipLabel);
			jPanel.add(getUrlConsoleUeRWSJTextField(), gridBagConstraintsUrlConsoleGestorFipText);
		
			jPanel.add(urlIgnWSJLabel, gridBagConstraintsUrlIgnLabel);
			jPanel.add(getUrlIgnWSJTextField(), gridBagConstraintsUrlIgnText);
			
			
				confirmMessageTitle = I18N.get("dnie.confirmMessage.title");
				errorMessageTitle = I18N.get("dnie.errorMessage.title");
				infoMessageTitle = I18N.get("dnie.infoMessage.title");	
				
				operativeSystemLabel = new JLabel();
				operativeSystemLabel.setText(I18N.get("dnie.systemConfigPanel.operativeSystem.text"));
				systemCertStoreLabel = new JLabel();
				systemCertStoreLabel.setText(I18N.get("dnie.systemConfigPanel.systemCertStore.text"));	
				dnieLibraryLabel = new JLabel();
				dnieLibraryLabel.setText(I18N.get("dnie.systemConfigPanel.dnieLibrary.text"));		
				dniePortLabel = new JLabel();
				dniePortLabel.setText(I18N.get("dnie.systemConfigPanel.dniePort.text"));	
	         
	            GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
	            
	            GridBagConstraints gridBagConstraints37 = new GridBagConstraints();	            
	            GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
	            
	            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();	            
	            GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
	            
	            GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
	            GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
	               
	            gridBagConstraints35.gridx = 0;
	            gridBagConstraints35.gridy = 16;
	            gridBagConstraints35.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints35.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints35.weightx = 1.0D;  // Generated
	    		gridBagConstraints36.gridx = 1;
	    		gridBagConstraints36.gridy = 16;
				gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints36.insets = new Insets(0, 5, 0, 5);
	            
	            gridBagConstraints37.gridx = 0;
	            gridBagConstraints37.gridy = 17;
	            gridBagConstraints37.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints37.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints37.weightx = 1.0D;  // Generated
	    		gridBagConstraints38.gridx = 1;
	    		gridBagConstraints38.gridy = 17;
				gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints38.insets = new Insets(0, 5, 0, 5);
				gridBagConstraints39.gridx = 2;
				gridBagConstraints39.gridy = 17;
				gridBagConstraints39.insets = new Insets(0, 5, 5, 5);
				gridBagConstraints40.gridx = 3;
				gridBagConstraints40.gridy = 17;
				gridBagConstraints40.insets = new Insets(0, 5, 5, 5);
			
	            gridBagConstraints41.gridy = 18;
	            gridBagConstraints41.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints41.weightx = 1.0D;  // Generated
	    		gridBagConstraints42.gridx = 1;
	    		gridBagConstraints42.gridy = 18;
				gridBagConstraints42.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints42.insets = new Insets(0, 5, 0, 5);
				gridBagConstraints43.gridx = 2;
				gridBagConstraints43.gridy = 18;
				gridBagConstraints43.insets = new Insets(0, 5, 5, 5);
				gridBagConstraints44.gridx = 3;
				gridBagConstraints44.gridy = 18;
				gridBagConstraints44.insets = new Insets(0, 5, 5, 5);
	            
				gridBagConstraints45.gridx = 0;
				gridBagConstraints45.gridy = 19;
				gridBagConstraints45.fill = GridBagConstraints.HORIZONTAL; 
				gridBagConstraints45.weightx = 1.0;  // Generated
				gridBagConstraints45.insets = new Insets(0, 5, 5, 5);
				gridBagConstraints46.gridx = 1;
				gridBagConstraints46.gridy = 19;
				gridBagConstraints46.gridwidth = 1;
				gridBagConstraints46.fill = GridBagConstraints.HORIZONTAL; 
				gridBagConstraints46.insets = new Insets(0, 5, 5, 5);
				
				jPanel.add(operativeSystemLabel, gridBagConstraints35);
				jPanel.add(getOperativeSystemComboBox(), gridBagConstraints36);
				jPanel.add(systemCertStoreLabel, gridBagConstraints37);
				jPanel.add(getSystemCertStoreTextField(), gridBagConstraints38);
				jPanel.add(getSystemCertStoreButton(), gridBagConstraints39);	
				jPanel.add(getSystemCertStoreTestButton(), gridBagConstraints40);
				jPanel.add(dnieLibraryLabel, gridBagConstraints41);
				jPanel.add(getDNIeLibraryTextField(), gridBagConstraints42);
				jPanel.add(getDNIeLibraryButton(), gridBagConstraints43);
				jPanel.add(getDNIeLibraryTestButton(), gridBagConstraints44);
				jPanel.add(dniePortLabel, gridBagConstraints45);
				jPanel.add(getDNIePortTextField(), gridBagConstraints46);
			
				
				//Configuracion para OpenAM (Solo Disponible para Arrecife)
				
				urlOpenAM = new JLabel();
				urlOpenAM.setText("Open AM");
				
				GridBagConstraints gridBagConstraintsUrlOpenAMLabel = new GridBagConstraints();
				gridBagConstraintsUrlOpenAMLabel.gridx = 0;
				gridBagConstraintsUrlOpenAMLabel.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraintsUrlOpenAMLabel.gridwidth = 1;
				gridBagConstraintsUrlOpenAMLabel.insets = new Insets(0, 5, 5, 5);
				gridBagConstraintsUrlOpenAMLabel.gridy = 22;
				GridBagConstraints gridBagConstraintsUrlOpenAMText= new GridBagConstraints();
				gridBagConstraintsUrlOpenAMText.gridx = 1;
				gridBagConstraintsUrlOpenAMText.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraintsUrlOpenAMText.gridwidth = 1;
				gridBagConstraintsUrlOpenAMText.insets = new Insets(0, 5, 5, 5);
				gridBagConstraintsUrlOpenAMText.gridy = 22;

			
				getUrlOpenAMTextField().setText(UserPreferenceStore.getUserPreference("openam.url","openam.url",true));

				
				if (showOpenAmSettings){
					jPanel.add(urlOpenAM, gridBagConstraintsUrlOpenAMLabel);
					jPanel.add(getUrlOpenAMTextField(), gridBagConstraintsUrlOpenAMText);
				}
			
		}
		return jPanel;
	}
	
	

	//----NUEVO---->
	/**
	 * This method initializes operativeSystemComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getOperativeSystemComboBox() {
		if (operativeSystemComboBox == null) {
			operativeSystemComboBox = new JComboBox();
			operativeSystemComboBox.setPreferredSize(new Dimension(100, 25));
			
			
			operativeSystemConfigList = new OperativeSystemConfigList();				
			for (Enumeration e=operativeSystemConfigList.getOperativeSystemConfigList().keys();e.hasMoreElements();) {
				String keyName = (String) e.nextElement();      			
				operativeSystemComboBox.addItem(keyName);				
			}
			
			final SystemConfigPanel systemConfigPanel=this;
			operativeSystemComboBox.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e){
					int result = 0;
					
					String sistemaOperativoElegido=getOperativeSystemComboBox().getSelectedItem().toString();
					if ((sistemaOperativo!=null) && (!sistemaOperativoElegido.equals(sistemaOperativo)))
					//if(osSelected) 
						result = JOptionPane.showConfirmDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.operativeSystem.confirmDialog.text"),confirmMessageTitle,JOptionPane.YES_NO_CANCEL_OPTION); 
					if(result==0){
						getSystemCertStoreTextField().setText(operativeSystemConfigList.getOperativeSystemConfig(getOperativeSystemComboBox().getSelectedItem().toString()).getDefaultSystemCertStore());
						getDNIeLibraryTextField().setText(operativeSystemConfigList.getOperativeSystemConfig(getOperativeSystemComboBox().getSelectedItem().toString()).getDefaultDNIeLibrary());
						if(CertificateUtils.isDefaultWindowsCertificateStore(operativeSystemConfigList.getOperativeSystemConfig(getOperativeSystemComboBox().getSelectedItem().toString()).getDefaultSystemCertStore())){
							systemCertStoreTextField.setEnabled(false);
							systemCertStoreButton.setEnabled(false);
						}
						else{
							systemCertStoreTextField.setEnabled(true);
							systemCertStoreButton.setEnabled(true);
						}
					   //Verificamos si la libreria de DNI Electronico por defecto existe, ya que en la version 6 se llamaba usrpkcs11.dll y en la nueva version 10 se llama DNIe_P11_priv.dll
				       verificarLibreriaDNI();
					     
					}else{
						getOperativeSystemComboBox().setSelectedItem(sistemaOperativo);
						
					}
					sistemaOperativo=getOperativeSystemComboBox().getSelectedItem().toString();
				}
			});
			
		}
		return operativeSystemComboBox;
	}
	
	/**
	 * This method initializes systemCertStoreTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getSystemCertStoreTextField() {
		if (systemCertStoreTextField == null) {
			systemCertStoreTextField = new JTextField();
			systemCertStoreTextField.setColumns(12);
		}
		return systemCertStoreTextField;
	}
	/**
	 * This method initializes systemCertStoreButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSystemCertStoreButton()
	{
	if (systemCertStoreButton == null)
		{
		systemCertStoreButton = new JButton();
		systemCertStoreButton.setIcon(IconLoader.icon("abrir.gif"));
		systemCertStoreButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				File file=new File(getSystemCertStoreTextField().getText());
				JFileChooser fch=new JFileChooser(file);
				fch.setMultiSelectionEnabled(false);
				fch.setFileSelectionMode(JFileChooser.FILES_ONLY); 
			
				int resp=fch.showOpenDialog(SystemConfigPanel.this);
				File selFil= fch.getSelectedFile();
				if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
					{
					getSystemCertStoreTextField().setText(selFil.getAbsolutePath());
					}

				}
			});
		}
	return systemCertStoreButton;
	}
	/**
	 * This method initializes systemCertStoreTestButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSystemCertStoreTestButton()
	{
	if (systemCertStoreTestButton == null)
		{
		systemCertStoreTestButton = new JButton();
		systemCertStoreTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST));
		systemCertStoreTestButton.setToolTipText(I18N.get("dnie.systemConfigPanel.systemCertStoreTestButton.toolTipText"));
		final SystemConfigPanel systemConfigPanel=this;
		systemCertStoreTestButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e){					
					if(!getSystemCertStoreTextField().getText().trim().equals("")){
						JDialog systemConfigPanelAbsolutParent=(JDialog) SwingUtilities.getWindowAncestor(systemConfigPanel);
						String password = "";
						if(!CertificateUtils.isDefaultWindowsCertificateStore(getSystemCertStoreTextField().getText().trim())){
							passwordDialog = new PasswordDialog(systemConfigPanelAbsolutParent, I18N.get("dnie.certificateLoginDialog.systemCertStore.passwordDialog.title"));
							password = passwordDialog.showDialog();
						}
						if(password.equals("") || !passwordDialog.isCanceled()){					
							if(CertificateUtils.getSystemKeyStore(getSystemCertStoreTextField().getText().trim(),password.toCharArray())!=null) {
								JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.systemCertStoreTest.certStoreValidate.info"));
								systemCertStoreTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST)); 
							}
							else {
								JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.systemCertStoreTest.certStoreValidate.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
								systemCertStoreTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST_ERROR)); 
							}
						}
					}else JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.selectPath.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE); 
				}
			});
		}
	return systemCertStoreTestButton;
	}
	
	/**
	 * This method initializes dnieLibraryTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getDNIeLibraryTextField() {
		if (dnieLibraryTextField == null) {
			dnieLibraryTextField = new JTextField();
			dnieLibraryTextField.setColumns(12);
		}
		return dnieLibraryTextField;
	}	
	/**
	 * This method initializes dniePortTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getDNIePortTextField() {
		if (dniePortTextField == null) {
			dniePortTextField = new JTextField();
			dniePortTextField.setColumns(12);
		}
		return dniePortTextField;
	}	
	/**
	 * This method initializes dnieLibraryButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDNIeLibraryButton()
	{
	if (dnieLibraryButton == null)
		{
		dnieLibraryButton = new JButton();
		dnieLibraryButton.setIcon(IconLoader.icon("abrir.gif"));
		dnieLibraryButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				File file=new File(getDNIeLibraryTextField().getText());
				JFileChooser fch=new JFileChooser(file);
				fch.setMultiSelectionEnabled(false);
				fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
				int resp=fch.showOpenDialog(SystemConfigPanel.this);
				File selFil= fch.getSelectedFile();
				if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
					{
					getDNIeLibraryTextField().setText(selFil.getAbsolutePath());
					}

				}
			});
		}
	return dnieLibraryButton;
	}
	/**
	 * This method initializes dnieLibraryDefaultButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDNIeLibraryTestButton()
	{
	if (dnieLibraryTestButton == null)
		{
		dnieLibraryTestButton = new JButton();
		dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST));
		dnieLibraryTestButton.setToolTipText(I18N.get("dnie.systemConfigPanel.dnieLibraryTestButton.toolTipText")); 
		final SystemConfigPanel systemConfigPanel=this;
		dnieLibraryTestButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e){
					if(!getDNIeLibraryTextField().getText().trim().equals("")){
						DNIeManager dnieManager = new DNIeManager();		
						String arquitectura=System.getProperty("sun.arch.data.model");
						String so=UserPreferenceStore.getUserPreference(CertificateConstants.OPERATIVE_SYSTEM,CertificateConstants.DEFAULT_WINDOWS_XP_NAME,true);
						//En el caso de maquina virtual de 64 bits vamos a utilizar CSP en lugar de PKCS11
						if ((arquitectura!=null) && (arquitectura.equals("64")) && (so.contains("Windows"))){
							dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST));  
							JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.dnieLibraryTest.dnieLibraryValidate.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
					
						}
						else{
							if(dnieManager.setConfig(getDNIeLibraryTextField().getText().trim())){ 
								dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST));  
								JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.dnieLibraryTest.dnieLibraryValidate.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
							}
							else{ 
								dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST_ERROR));  
								JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.dnieLibraryTest.dnieLibraryValidate.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
							}
						}
					}else JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.selectPath.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE); 
				}
			});
		}
	return dnieLibraryTestButton;
	}
	//--FIN NUEVO-->
	
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getGenerarTexturasButton() {
		if (generarTexturasButton == null) {
			generarTexturasButton = new JButton();
			generarTexturasButton.setText(I18N.get("SystemConfigPanel.GenerateTextures")); //$NON-NLS-1$
			generarTexturasButton.setEnabled(true);
			generarTexturasButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					if (logger.isDebugEnabled())
					{
					logger
							.debug(I18N.get("SystemConfigPanel.SavingTextureFiles")); //$NON-NLS-1$
					}
					
						final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
				                .getMainFrame(), null);

				        progressDialog.setTitle("SystemConfigPanel.CopyingTextures"); //$NON-NLS-1$
				        progressDialog.report(I18N.get("SystemConfigPanel.CopyingTextures")); //$NON-NLS-1$
				        progressDialog.addComponentListener(new ComponentAdapter()
				        		{
				        		public void componentShown(ComponentEvent e)
				        		{
				        		
				        		// Wait for the dialog to appear before starting the
				        		// task. Otherwise
				        		// the task might possibly finish before the dialog
				        		// appeared and the
				        		// dialog would never close. [Jon Aquino]
				        		new Thread(new Runnable()
				        				{
				        				public void run()
				        				{
				        				try
				        				{
				        				/*File destino = new File(texturesFolderField
				        						.getText());*/
				        				File destino = new File(texturesFolderField.getText());
				        				File destinoTexturas = new File(texturesFolderField.getText()+File.separator+"textures");
				        				File destinoTexturas_old = new File(texturesFolderField.getText()+File.separator+"textures.old");
				        				File destinoIconlib_old = new File(texturesFolderField.getText()+File.separator+"iconlib");

//				        				GeopistaUtil
//										.extractResources(
//												".*/com/geopista/ui/images/textures/.*",
//												destino, IconLoader.class,
//												progressDialog);
                                        
				        				ProcessCancel processCancel=null;			
				        				if (progressDialog!=null){				
				        					if (progressDialog!=null){
				        						processCancel=new ProcessCancel(progressDialog);
				        						processCancel.start();
				        					}
				        				}
                                        File destinoIconos = new File(texturesFolderField.getText()+"/iconlib");
                                        if(!destinoIconos.isDirectory()) destinoIconos.delete();
//                                        GeopistaFunctionUtils
//                                        .extractResources(
//                                                ".*", //$NON-NLS-1$
//                                                destinoIconos, new URL(UserPreferenceStore.getUserPreference(AppContext.HOST_ADMCAR,AppContext.HOST_ADMCAR,false) + "/" + WebAppConstants.RESOURCES_WEBAPP_NAME + ServletConstants.GETTEXTURAS), //$NON-NLS-1$
//                                                progressDialog);
                                        GeopistaFunctionUtils.getZipFiles(destino, GeopistaFunctionUtils.getZipFromServlet(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,UserPreferenceConstants.LOCALGIS_SERVER_URL,false) + WebAppConstants.PRINCIPAL_WEBAPP_NAME + ServletConstants.GETTEXTURES_SERVLET), progressDialog);
                                        
//                                        GeopistaFunctionUtils
//										.extractResources(
//												".*/textures/.*", //$NON-NLS-1$
//												destino, new URL(UserPreferenceStore.getUserPreference(AppContext.HOST_ADMCAR,AppContext.HOST_ADMCAR,false) + "/" + WebAppConstants.RESOURCES_WEBAPP_NAME + ServletConstants.GETTEXTURAS), //$NON-NLS-1$
//												progressDialog);
				        				
				        				processCancel.terminateProcess();
				        				
				        				
				        				}
				        				catch (Exception e1)
				        				{
				        				JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantDownloadFromAddress")); //$NON-NLS-1$
				        				
				        				logger
										.error(e1);
				        				}
				        				finally
										{
										progressDialog.setVisible(false);
										}
				        				}
				        				}).start();
				        		}
				        		}); 
						
					progressDialog.setVisible(true);
					
				// TODO: Extraer ficheros del JAR de imagenes al disco local.
				}
			});
		}
		return generarTexturasButton;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUpdateURLTextField() {
		if (updateURLTextField == null) {
			updateURLTextField = new JTextField();
		}
		return updateURLTextField;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getGenerateTemplatesButton() {
		if (generateTemplatesButton == null) {
			generateTemplatesButton = new JButton();
			generateTemplatesButton.setText(I18N.get("SystemConfigPanel.generateTemplates"));  // Generated //$NON-NLS-1$
			//generateTemplatesButton.setEnabled(false);  // Generated
			
			
			
			
			generateTemplatesButton.setEnabled(true);
			generateTemplatesButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					if (logger.isDebugEnabled())
					{
					logger
							.debug(I18N.get("SystemConfigPanel.SavingTemplateFiles")); //$NON-NLS-1$
					}
					
						final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
				                .getMainFrame(), null);

				        progressDialog.setTitle("SystemConfigPanel.CopyingTemplates"); //$NON-NLS-1$
				        progressDialog.report(I18N.get("SystemConfigPanel.CopyingTemplates")); //$NON-NLS-1$
				        
				        
				        progressDialog.addComponentListener(new ComponentAdapter()
				        		{
				        		public void componentShown(ComponentEvent e)
				        		{
				        		
				        		// Wait for the dialog to appear before starting the
				        		// task. Otherwise
				        		// the task might possibly finish before the dialog
				        		// appeared and the
				        		// dialog would never close. [Jon Aquino]
				        		new Thread(new Runnable()
				        				{
				        				public void run()
				        				{
				        				try
				        				{
				        				File destino = new File(dataFolderField
				        						.getText());
//				        				GeopistaUtil
//										.extractResources(
//												".*/com/geopista/ui/images/textures/.*",
//												destino, IconLoader.class,
//												progressDialog);
				        				
				        				ProcessCancel processCancel=null;			
				        				if (progressDialog!=null){				
				        					if (progressDialog!=null){
				        						processCancel=new ProcessCancel(progressDialog);
				        						processCancel.start();
				        					}
				        				}
				        				GeopistaFunctionUtils.getZipFiles(destino, GeopistaFunctionUtils.getZipFromServlet(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,UserPreferenceConstants.LOCALGIS_SERVER_URL,false) + WebAppConstants.PRINCIPAL_WEBAPP_NAME + ServletConstants.GETTEMPLATES_SERVLET), progressDialog);
				        				processCancel.terminateProcess();
//				        				GeopistaFunctionUtils
//										.extractResources(
//												".*", //$NON-NLS-1$
//												destino, getZip(url), //$NON-NLS-1$
//												progressDialog);
//				        				processCancel.terminateProcess();
				        				
				        				//System.out.println(UserPreferenceStore.getUserPreference(AppContext.UPDATE_SERVER_URL,AppContext.DEFAULT_UPDATE_SERVER,false));
				        				}
				        				catch (Exception e1)
				        				{
				        					JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantDownloadFromAddress")); //$NON-NLS-1$
				        					logger.error(e1);
				        				}
				        				finally
										{
										progressDialog.setVisible(false);
										}
				        				}
				        				}).start();
				        		}
				        		}); 
						
					progressDialog.setVisible(true);
					
				// TODO: Extraer ficheros del JAR de imagenes al disco local.
				}
			});
		}

		return generateTemplatesButton;
	}
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	/*private JTextField getDataBaseAddress() {
		if (dataBaseAddress == null) {
			dataBaseAddress = new JTextField();
		}
		return dataBaseAddress;
	}*/

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getReportUserTextField() {
		if (reportUserTextField == null) {
			reportUserTextField = new JTextField();
		}
		return reportUserTextField;
	}
	
	private JPasswordField getReportpasswordTextField() {
		if (reportpasswordTextField == null) {
			reportpasswordTextField = new JPasswordField();
		}
		return reportpasswordTextField;
	}

    /**
     * This method initializes jTextField   
     *  
     * @return javax.swing.JTextField   
     */    
    private JTextField getReportUrlJasperTextField() {
        if (reportUrlJasperTextField == null) {
            reportUrlJasperTextField = new JTextField();
        }
        return reportUrlJasperTextField;
    }

    /**
	 * This method initializes dataFolderButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDataFolderButton()
	{
	if (dataFolderButton == null)
		{
		dataFolderButton = new JButton();
		dataFolderButton.setIcon(IconLoader.icon("abrir.gif"));
		dataFolderButton.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
				File file=new File(getDataFolderField().getText());
				JFileChooser fch=new JFileChooser(file);
				fch.setMultiSelectionEnabled(false);
				fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
				int resp=fch.showOpenDialog(SystemConfigPanel.this);
				File selFil= fch.getSelectedFile();
				if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
					{
					getDataFolderField().setText(selFil.getAbsolutePath());
					}

				}
			});
		}
	return dataFolderButton;
	}
		/**
		 * This method initializes jButton	
		 * 	
		 * @return javax.swing.JButton	
		 */
		private JButton getTexturesButton()
		{
		if (texturesButton == null)
			{
			texturesButton = new JButton();
			texturesButton.setIcon(IconLoader.icon("abrir.gif"));
			texturesButton.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(java.awt.event.ActionEvent e)
					{
					File file=new File(getTexturesFolderField().getText());
					JFileChooser fch=new JFileChooser(file);
					fch.setMultiSelectionEnabled(false);
					fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int resp=fch.showOpenDialog(SystemConfigPanel.this);
					File selFil= fch.getSelectedFile();
					if (resp==JFileChooser.APPROVE_OPTION && selFil!=null)
						{
						getTexturesFolderField().setText(selFil.getAbsolutePath());
						}
	}
				});
			}
		return texturesButton;
		}
		/**
         * This method initializes urlTomcatJTextField	
         * 	
         * @return javax.swing.JTextField	
         */
        private JTextField getUrlTomcatJTextField()
        {
            if (urlTomcatJTextField == null)
            {
                urlTomcatJTextField = new JTextField();
            }
            return urlTomcatJTextField;
        }
        
        private JTextField getUrlMantisJTextField()
        {
            if (urlMantis == null)
            {
            	urlMantis = new JTextField();
            }
            return urlMantis;
        }
        
        private JTextField getUserMantisJTextField()
        {
            if (userMantis == null)
            {
            	userMantis = new JTextField();
            }
            return userMantis;
        }
        
        private JPasswordField getPasswordMantisJTextField()
        {
            if (passwordMantis == null)
            {
            	passwordMantis = new JPasswordField();
            }
            return passwordMantis;
        }
        
        private JTextField getUrlMapServerJTextField()
        {
            if (urlMapServerJTextField == null)
            {
                urlMapServerJTextField = new JTextField();
            }
            return urlMapServerJTextField;
        }       
       
    	public JTextField getUrlConsoleUeRWSJTextField() {
    		 if (urlConsoleUeRWSJTextField == null)
             {
    			 urlConsoleUeRWSJTextField = new JTextField();
             }
    		return urlConsoleUeRWSJTextField;
    	}

    	public JTextField getUrlIgnWSJTextField() {
          	if (urlIgnWSJTextField == null)
          	{
          		urlIgnWSJTextField = new JTextField();
          	}
          	return urlIgnWSJTextField;
          }

    	public JTextField getUrlOpenAMTextField() {    		
          	if (urlOpenAMTextField == null)
          	{
          		urlOpenAMTextField = new JTextField();
          	}
          	return urlOpenAMTextField;
          }

        /**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */    
	
          	public static void main(String[] args)
	{
    		JFrame fr = new JFrame("Test"); //$NON-NLS-1$
    		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		fr.setSize(600,650);
    		fr.getContentPane().add(new SystemConfigPanel());
    		fr.setVisible(true);
    		
	}
	/**
	 * This is the default constructor
	 */
	public SystemConfigPanel(boolean showOpenAmSetting) {
		super();
		
		this.showOpenAmSettings=showOpenAmSetting;
		
		initialize();
		setupFields();
	}
	
	public SystemConfigPanel() {
		this(false);
	}
	
	boolean showOpenAmSettings=false;
	
	
	
/*	private JFormattedTextField getIdMunicipioField()
	{
	if (idMunicioValue==null)
		{
	DecimalFormat idMunicipioFormat = new DecimalFormat();
	idMunicipioFormat.setGroupingUsed(false);
	idMunicioValue = new JFormattedTextField(idMunicipioFormat); 
	
		}
	return idMunicioValue;
	}*/
	private void setupFields()
	{
		//Locale[] locales =Locale.getAvailableLocales();
	Locale[] locales=getAvailableLocales();
		getJComboBox().setModel(new DefaultComboBoxModel(locales));
		String localeString=UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY,
				Locale.getDefault().toString(),
				true);
		Locale current = new Locale(localeString.substring(0,2),localeString.substring(3,5));

		if (logger.isDebugEnabled())
		{
		logger
				.debug("setupFields() - Generando lista de idiomas. : Locale[] locales = " //$NON-NLS-1$
						+ locales + ", String localeString = " + localeString); //$NON-NLS-1$
		}

		// busca el actual
		for (int i = 0; i < locales.length; i++)
		{
		Locale locale = locales[i];
		if (locale.equals(current))
		{
		//getJComboBox().getModel().setSelectedItem(locale);
		getJComboBox().setSelectedItem(locale);
		}
		}
		getJComboBox().setFocusable(false);
		getJComboBox().setRenderer(new ListCellRenderer()
				{
					JLabel textLocale;
					public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						textLocale=new JLabel();
						textLocale.setText(I18N.get("SystemConfigPanel.empty")); //$NON-NLS-1$
						
						if (value instanceof Locale) 
						{
						Locale loc=(Locale)value;
						if(loc.getLanguage().equals("va"))
						{
						    textLocale.setText("Valenciï¿½ (Espaï¿½a)");
						}
						else
						{
						    textLocale.setText(loc.getDisplayName());
						}
						
						}
						else
						{
						textLocale.setText("desconocido"+value.toString()); //$NON-NLS-1$
						
						}
						
						textLocale.setBackground(cellHasFocus?Color.black:Color.white);
						textLocale.setForeground(Color.BLACK);
						textLocale.setIcon(IconLoader.icon("country.gif")); //$NON-NLS-1$
						
						return textLocale;
					}
			});
		
				
		getServerAddressField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,"",true)); //$NON-NLS-1$
		getDataFolderField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,UserPreferenceConstants.DEFAULT_DATA_PATH,false));
		getTexturesFolderField().setText(UserPreferenceStore.getUserPreference(Texture.TEXTURES_DIRECTORY_PARAMETER,UserPreferenceConstants.DEFAULT_DATA_PATH+"textures",false)); //$NON-NLS-1$
		getUpdateURLTextField().setText(aplicacion.getString(UserPreferenceConstants.UPDATE_SERVER_URL, UserPreferenceConstants.UPDATE_SERVER_URL));
//		getIdMunicipioField().setText(UserPreferenceStore.getUserPreference(AppContext.CODIGOMUNICIPIO,"",true)); //$NON-NLS-1$
        getReportUserTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER,"",true)); //$NON-NLS-1$

        

        String passwordToRecover=UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",true);
        String passwordToShow=passwordToRecover;
        try {
			EncriptarPassword ec=new EncriptarPassword(passwordToRecover);
			passwordToShow=ec.desencriptar();
		} catch (Exception e) {
			passwordToShow=new String(passwordToRecover);
		}
        getReportpasswordTextField().setText(passwordToShow); //$NON-NLS-1$
        
        getReportUrlJasperTextField().setText(aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL,"")); //$NON-NLS-1$
		//getDataBaseAddress().setText(aplicacion.getString(UserPreferenceConstants.LOCALGIS_DATABASE_URL,""));
        
		getUrlTomcatJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.TOMCAT_URL,"",true));
        getUrlMapServerJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.MAPSERVER_URL,"http://localhost",true));
        getUrlMantisJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.MANTIS_URL, MantisConstants.DEFAULT_URL_MANTIS,true));
        getUserMantisJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.MANTIS_USERNAME,MantisConstants.DEFAULT_USER_MANTIS,true));
        getUrlConsoleUeRWSJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.URL_CONSOLEUER_WS,UserPreferenceConstants.URL_CONSOLEUER_WS,true));
        getUrlIgnWSJTextField().setText(UserPreferenceStore.getUserPreference(UserPreferenceConstants.URL_IGN_WS,UserPreferenceConstants.URL_IGN_WS,true));
        
        passwordToRecover=UserPreferenceStore.getUserPreference(UserPreferenceConstants.MANTIS_PASSWORD,MantisConstants.DEFAULT_PASSWORD_MANTIS,true);
        passwordToShow=passwordToRecover;
        try {
			EncriptarPassword ec=new EncriptarPassword(passwordToRecover);
			passwordToShow=ec.desencriptar();
		} catch (Exception e) {
			passwordToShow=new String(passwordToRecover);
		}
        getPasswordMantisJTextField().setText(passwordToShow);

        getOperativeSystemComboBox().setSelectedItem(UserPreferenceStore.getUserPreference(CertificateConstants.OPERATIVE_SYSTEM,CertificateConstants.DEFAULT_WINDOWS_XP_NAME,true));
        if(CertificateUtils.isDefaultWindowsCertificateStore()){
			getSystemCertStoreTextField().setEnabled(false);
			getSystemCertStoreButton().setEnabled(false);        	
       }
       else{
        	getSystemCertStoreTextField().setEnabled(true);
			getSystemCertStoreButton().setEnabled(true);     
       }
       getSystemCertStoreTextField().setText(UserPreferenceStore.getUserPreference(CertificateConstants.SYSTEM_CERT_STORE,CertificateUtils.getDefaultWindows7JksSystemCertStore(),true));   
        
       getDNIeLibraryTextField().setText(UserPreferenceStore.getUserPreference(CertificateConstants.DNIE_LIBRARY,CertificateConstants.DEFAULT_WINDOWS_7_DNIE_LIBRARY,true));
       
       //Verificamos si la libreria de DNI Electronico por defecto existe, ya que en la version 6 se llamaba usrpkcs11.dll y en la nueva version 10 se llama DNIe_P11_priv.dll
       verificarLibreriaDNI();
       
       
       getDNIePortTextField().setText(UserPreferenceStore.getUserPreference(CertificateConstants.DNIE_SAFE_PORT,"9443",true));
       osSelected = true;
	}

	private void verificarLibreriaDNI(){
		 try {
			File f = new File( getDNIeLibraryTextField().getText());
			   if(!f.exists()){
				   String so=UserPreferenceStore.getUserPreference(CertificateConstants.OPERATIVE_SYSTEM,CertificateConstants.DEFAULT_WINDOWS_XP_NAME,true);
				   if (so.equals("Windows 7 (JKS)")){
					   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_7_DNIE_v10_LIBRARY_64);
					   if (!validateLibrary()){
						   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_7_DNIE_v10_LIBRARY_32);
						   validateLibrary();
					   }
				   }
				   else if (so.equals("Windows Vista (JKS)")){
					   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_VISTA_DNIE_v10_LIBRARY_64); 
					   if (!validateLibrary()){
						   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_VISTA_DNIE_v10_LIBRARY_32);
						   validateLibrary();
					   }
					   
				   }
				   else if (so.equals("Windows XP (JKS)")){
					   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_XP_DNIE_V10_LIBRARY_64);  
					   if (!validateLibrary()){
						   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_WINDOWS_XP_DNIE_V10_LIBRARY_32);
						   validateLibrary();
					   }

				   }
				   else if (so.equals("Linux (JKS)")){
					   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_LINUX_DNIE_v10_LIBRARY_64);  
					   if (!validateLibrary()){
						   getDNIeLibraryTextField().setText(CertificateConstants.DEFAULT_LINUX_DNIE_v10_LIBRARY_32);
						   validateLibrary();
					   }
	 
				   }
				}
			} catch (Exception e) {
		
			}
	}
	
	private boolean validateLibrary() {
		File f1 = new File(getDNIeLibraryTextField().getText());
		if (!f1.exists()) {
			logger.error("La libreria "+getDNIeLibraryTextField().getText()+" no existe. Intentando otra localizacion");
			dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST_ERROR));
			return false;
		}
		logger.info("La libreria "+getDNIeLibraryTextField().getText()+" existe.");
		dnieLibraryTestButton.setIcon(IconLoader.icon(IMAGE_BTN_TEST));
		return true;
	}
	/**
	 * @return
	 */
	private Locale[] getAvailableLocales()
	{
	String [] locales= aplicacion.getString(UserPreferenceConstants.PREFERENCES_AVAILABLE_LANGUAGES_KEY,"es_ES").split(";"); //$NON-NLS-1$ //$NON-NLS-2$
	Locale[] locs=new Locale[locales.length];
	for (int i = 0; i < locales.length; i++)
		{
		String string = locales[i];
		locs[i]=new Locale(string.substring(0,2),string.substring(3,5));
		}
	return locs;
	}
	private  void initialize() {
		
		
		this.setLayout(new BorderLayout());
		
		this.setPreferredSize(new java.awt.Dimension(600,650));  // Generated
		
		
		this.add(getJPanel(), java.awt.BorderLayout.CENTER);
		}
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.OptionsPanel#validateInput()
	 */
	public String validateInput()
	{
		if (!getServerAddressField().getText().startsWith("http")){
			return I18N.get("SystemConfigPanel.urlAdmcar");	
		}else{
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.OptionsPanel#okPressed()
	 */
	public void okPressed()
	{
		UserPreferenceStore.setUserPreference(UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,getDataFolderField().getText());
		UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOCALE_KEY,getJComboBox().getSelectedItem().toString());
		UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_URL,getServerAddressField().getText());
		UserPreferenceStore.setUserPreference(Texture.TEXTURES_DIRECTORY_PARAMETER,getTexturesFolderField().getText());
//		UserPreferenceStore.setUserPreference(AppContext.CODIGOMUNICIPIO,getIdMunicipioField().getText());
		UserPreferenceStore.setUserPreference(UserPreferenceConstants.UPDATE_SERVER_URL,getUpdateURLTextField().getText());
		//UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_URL,getDataBaseAddress().getText());
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER,getReportUserTextField().getText());


        String passwordToStore=null;
        char[] recoverPassword=getReportpasswordTextField().getPassword();
        try {
			EncriptarPassword ec=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
			passwordToStore=ec.encriptar(new String(recoverPassword));
		} catch (Exception e) {
			passwordToStore=new String(recoverPassword);
		}
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,passwordToStore);
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL,getReportUrlJasperTextField().getText());
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.TOMCAT_URL,getUrlTomcatJTextField().getText());
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.MAPSERVER_URL,getUrlMapServerJTextField().getText());
        UserPreferenceStore.setUserPreference(MantisConstants.URL_MANTIS,getUrlMantisJTextField().getText());
        UserPreferenceStore.setUserPreference(MantisConstants.USER_MANTIS,getUserMantisJTextField().getText());
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.URL_CONSOLEUER_WS,getUrlConsoleUeRWSJTextField().getText());
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.URL_IGN_WS,getUrlIgnWSJTextField().getText());
       
        UserPreferenceStore.setUserPreference("openam.url",getUrlOpenAMTextField().getText());

        
        recoverPassword=getPasswordMantisJTextField().getPassword();
        try {
			EncriptarPassword ec=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
			passwordToStore=ec.encriptar(new String(recoverPassword));
		} catch (Exception e) {
			passwordToStore=new String(recoverPassword);
		}
        UserPreferenceStore.setUserPreference(MantisConstants.PASSWORD_MANTIS,passwordToStore);

    	UserPreferenceStore.setUserPreference(CertificateConstants.OPERATIVE_SYSTEM,getOperativeSystemComboBox().getSelectedItem().toString());
    	UserPreferenceStore.setUserPreference(CertificateConstants.SYSTEM_CERT_STORE,getSystemCertStoreTextField().getText());
    	UserPreferenceStore.setUserPreference(CertificateConstants.DNIE_LIBRARY,getDNIeLibraryTextField().getText());        
    	UserPreferenceStore.setUserPreference(CertificateConstants.DNIE_SAFE_PORT,getDNIePortTextField().getText());        

        StringTokenizer st = new StringTokenizer(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL), ":");
        if (st.countTokens() == 3){
        	UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_PROTOCOL, st.nextToken());
        	st.nextToken();
        	UserPreferenceStore.setUserPreference(UserPreferenceConstants.LOCALGIS_SERVER_PORT,st.nextToken());
        }
	} 
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.workbench.ui.OptionsPanel#init()
	 */
	public void init()
	{
		setupFields();		
	}
	
	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
