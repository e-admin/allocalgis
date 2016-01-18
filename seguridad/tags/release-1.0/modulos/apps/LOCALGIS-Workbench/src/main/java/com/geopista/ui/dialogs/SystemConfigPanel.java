/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 22-nov-2004 by juacas
 *
 * 
 */
package com.geopista.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.utilidades.ProcessCancel;
import com.geopista.security.dnie.DNIeManager;
import com.geopista.security.dnie.dialogs.PasswordDialog;
import com.geopista.security.dnie.global.CertificateConstants;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.geopista.security.dnie.utils.OperativeSystemConfigList;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaFunctionUtils;
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
	private ApplicationContext aplicacion = AppContext.getApplicationContext();  //  @jve:decl-index=0:
	private JPanel jPanel = null;
	private JButton generarTexturasButton = null;
/*	private JLabel codigoMunicipioLabel = null;
	private JFormattedTextField idMunicioValue = null;*/
	private JTextField updateURLTextField = null;
	private JLabel updateURLJLabel = null;
	private JButton generateTemplatesButton = null;
	private JTextField dataBaseAddress = null;
	private JLabel serverDBAddressLabel = null;
	
	private JTextField reportpasswordTextField = null;
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
		
	
		
		urlTomcatJLabel = new JLabel();
		urlTomcatJLabel.setText(I18N.get("SystemConfigPanel.urlTomcat"));
		urlMapServerJLabel = new JLabel();
		urlMapServerJLabel.setText(I18N.get("SystemConfigPanel.urlMapServer"));
		serverAddress=new JLabel();
		texturesFolder= new JLabel();
		dataFolderLabel = new JLabel();
		preferredLocale= new JLabel();
        reportPasswordLabel = new JLabel();
        reportUrlJasperLabel = new JLabel();
		serverDBAddressLabel = new JLabel();
		updateURLJLabel = new JLabel();
/*		codigoMunicipioLabel = new JLabel();
		codigoMunicipioLabel.setText(I18N.get("SystemConfigPanel.munCode")); //$NON-NLS-1$*/
		updateURLJLabel.setText(I18N.get("SystemConfigPanel.UpdateDataURL"));  // Generated //$NON-NLS-1$
		reportPasswordLabel.setText(I18N.get("SystemConfigPanel.ReportUserPass"));  // Generated //$NON-NLS-1$
        reportUrlJasperLabel.setText(I18N.get("SystemConfigPanel.ReportUrlJasper"));  // Generated //$NON-NLS-1$
		serverDBAddressLabel.setText(I18N.get("SystemConfigPanel.DB_Address"));  // Generated //$NON-NLS-1$
		dataFolderLabel.setText(I18N.get("SystemConfigPanel.dataFolderLabel")); //$NON-NLS-1$
		texturesFolder.setText(I18N.get("SystemConfigPanel.texturesFolderLabel")); //$NON-NLS-1$
		serverAddress.setText(I18N.get("SystemConfigPanel.serverAddress")); //$NON-NLS-1$
		preferredLocale.setText(I18N.get("SystemConfigPanel.preferredLocale")); //$NON-NLS-1$
		
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.insets = new Insets(0, 5, 5, 5);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
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
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 9;
			gridBagConstraints19.insets = new java.awt.Insets(0,5,5,5);
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.weightx = 1.0D;  // Generated
			gridBagConstraints20.gridx = 1;
			gridBagConstraints20.gridy = 9;
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(0, 5, 5, 5);
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
			gridBagConstraints11.gridx = 1;  // Generated
			gridBagConstraints11.gridy = 5;  // Generated
			gridBagConstraints11.weightx = 1.0;  // Generated
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints11.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints23.gridx = 0;  // Generated
			gridBagConstraints23.gridy = 5;  // Generated
			gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints23.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints32.gridx = 1;  // Generated
			gridBagConstraints32.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints32.gridy = 8;  // Generated
			gridBagConstraints32.weightx = 1.0;  // Generated
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints12.gridx = 1;  // Generated
			gridBagConstraints12.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints12.gridy = 7;  // Generated
			gridBagConstraints12.weightx = 1.0;  // Generated
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints24.gridx = 0;  // Generated
			gridBagConstraints24.gridy = 7;  // Generated
			gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints24.insets = new Insets(0, 5, 5, 5);  // Generated
			gridBagConstraints24.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
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
			jPanel.add(preferredLocale, gridBagConstraints19);
			jPanel.add(getJComboBox(), gridBagConstraints20);
			jPanel.add(dataFolderLabel, gridBagConstraints13);
			jPanel.add(serverAddress, gridBagConstraints17);  // Generated
			jPanel.add(getServerAddressField(), gridBagConstraints18);
			jPanel.add(getDataFolderField(), gridBagConstraints14);
			jPanel.add(texturesFolder, gridBagConstraints15);
			jPanel.add(getTexturesFolderField(), gridBagConstraints16);
			jPanel.add(getGenerarTexturasButton(), gridBagConstraints2);
			jPanel.add(getUpdateURLTextField(), gridBagConstraints1);
			jPanel.add(updateURLJLabel, gridBagConstraints22);
			jPanel.add(getGenerateTemplatesButton(), gridBagConstraints31);  // Generated
			jPanel.add(getDataBaseAddress(), gridBagConstraints11);
			jPanel.add(serverDBAddressLabel, gridBagConstraints23);
//			jPanel.add(getIdMunicipioField(), gridBagConstraints32);
            jPanel.add(getReportpasswordTextField(), gridBagConstraints12);
            jPanel.add(getReportUrlJasperTextField(), gridBagConstraints34);
			gridBagConstraints110.gridx = 2;  // Generated
			gridBagConstraints110.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints110.gridy = 1;  // Generated
			jPanel.add(reportPasswordLabel, gridBagConstraints24);
            jPanel.add(reportUrlJasperLabel, gridBagConstraints33);
			jPanel.add(getDataFolderButton(), gridBagConstraints29);
			jPanel.add(getTexturesButton(), gridBagConstraints110);
			jPanel.add(urlTomcatJLabel, gridBagConstraints);
			jPanel.add(getUrlTomcatJTextField(), gridBagConstraints4);
			jPanel.add(urlMapServerJLabel, gridBagConstraintsMapServerLabel);
			jPanel.add(getUrlMapServerJTextField(), gridBagConstraintsMapServerText);
						
			//----NUEVO---->
			if(CertificateUtils.isDNIeActive()){				
				confirmMessageTitle = I18N.get("dnie.confirmMessage.title");
				errorMessageTitle = I18N.get("dnie.errorMessage.title");
				infoMessageTitle = I18N.get("dnie.infoMessage.title");	
				
				operativeSystemLabel = new JLabel();
				operativeSystemLabel.setText(I18N.get("dnie.systemConfigPanel.operativeSystem.text"));
				systemCertStoreLabel = new JLabel();
				systemCertStoreLabel.setText(I18N.get("dnie.systemConfigPanel.systemCertStore.text"));	
				dnieLibraryLabel = new JLabel();
				dnieLibraryLabel.setText(I18N.get("dnie.systemConfigPanel.dnieLibrary.text"));					
	         
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
	               
	            gridBagConstraints35.gridx = 0;
	            gridBagConstraints35.gridy = 13;
	            gridBagConstraints35.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints35.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints35.weightx = 1.0D;  // Generated
	    		gridBagConstraints36.gridx = 1;
	    		gridBagConstraints36.gridy = 13;
				gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints36.insets = new Insets(0, 5, 0, 5);
	            
	            gridBagConstraints37.gridx = 0;
	            gridBagConstraints37.gridy = 14;
	            gridBagConstraints37.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints37.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints37.weightx = 1.0D;  // Generated
	    		gridBagConstraints38.gridx = 1;
	    		gridBagConstraints38.gridy = 14;
				gridBagConstraints38.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints38.insets = new Insets(0, 5, 0, 5);
				gridBagConstraints39.gridx = 2;
				gridBagConstraints39.gridy = 14;
				gridBagConstraints39.insets = new Insets(0, 5, 5, 5);
				gridBagConstraints40.gridx = 3;
				gridBagConstraints40.gridy = 14;
				gridBagConstraints40.insets = new Insets(0, 5, 5, 5);
			
	            gridBagConstraints41.gridy = 15;
	            gridBagConstraints41.insets = new Insets(0, 5, 5, 5);
	            gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;  // Generated
	            gridBagConstraints41.weightx = 1.0D;  // Generated
	    		gridBagConstraints42.gridx = 1;
	    		gridBagConstraints42.gridy = 15;
				gridBagConstraints42.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints42.insets = new Insets(0, 5, 0, 5);
				gridBagConstraints43.gridx = 2;
				gridBagConstraints43.gridy = 15;
				gridBagConstraints43.insets = new Insets(0, 5, 5, 5);
				gridBagConstraints44.gridx = 3;
				gridBagConstraints44.gridy = 15;
				gridBagConstraints44.insets = new Insets(0, 5, 5, 5);
	            
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
            }
            //--FIN NUEVO-->
			
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
			//operativeSystemComboBox.setFont(new Font());
			//operativeSystemComboBox.addItem("Windows");
			//operativeSystemComboBox.addItem("Linux");
			
			
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
					}else{
						getOperativeSystemComboBox().setSelectedItem(AppContext.getApplicationContext().getUserPreference(CertificateConstants.OPERATIVE_SYSTEM, CertificateConstants.DEFAULT_WINDOWS_XP_NAME, true));
						
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
						JFrame systemConfigPanelAbsolutParent = (JFrame) SwingUtilities.getWindowAncestor(systemConfigPanel);
						String password = "";
						if(!CertificateUtils.isDefaultWindowsCertificateStore(getSystemCertStoreTextField().getText().trim())){
							passwordDialog = new PasswordDialog(systemConfigPanelAbsolutParent);
							password = passwordDialog.showDialog();
						}
						if(password.equals("") || !passwordDialog.isCanceled()){					
							if(CertificateUtils.getSystemKeyStore(getSystemCertStoreTextField().getText().trim(),password.toCharArray())!=null) JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.systemCertStoreTest.certStoreValidate.info")); 
							else JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.systemCertStoreTest.certStoreValidate.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
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
						if(dnieManager.setConfig(getDNIeLibraryTextField().getText().trim())) JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.dnieLibraryTest.dnieLibraryValidate.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
						else JOptionPane.showMessageDialog(systemConfigPanel, I18N.get("dnie.systemConfigPanel.dnieLibraryTest.dnieLibraryeValidate.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE); 
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

				        progressDialog.setTitle(aplicacion.getI18nString(I18N.get("SystemConfigPanel.CopyingTextures"))); //$NON-NLS-1$
				        progressDialog.report(aplicacion.getI18nString(I18N.get("SystemConfigPanel.CopyingTextures"))); //$NON-NLS-1$
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
				        				File destino = new File(texturesFolderField
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
                                        File destinoIconos = new File(texturesFolderField.getText()+"/iconlib");
                                        if(!destinoIconos.isDirectory()) destinoIconos.delete();
                                        GeopistaFunctionUtils
                                        .extractResources(
                                                ".*", //$NON-NLS-1$
                                                destinoIconos, new URL(aplicacion.getUserPreference(AppContext.UPDATE_SERVER_URL,AppContext.DEFAULT_UPDATE_SERVER,false)+"/iconlib.jar"), //$NON-NLS-1$
                                                progressDialog);
                                        GeopistaFunctionUtils
										.extractResources(
												".*/com/geopista/ui/images/textures/.*", //$NON-NLS-1$
												destino, new URL(aplicacion.getUserPreference(AppContext.UPDATE_SERVER_URL,AppContext.DEFAULT_UPDATE_SERVER,false)+"/geopista_images.jar"), //$NON-NLS-1$
												progressDialog);
				        				
				        				processCancel.terminateProcess();
				        				
				        				
				        				}
				        				catch (MalformedURLException e1)
				        				{
				        				JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantConnectToAddress")); //$NON-NLS-1$
				        				logger
										.error(
												"actionPerformed(java.awt.event.ActionEvent)", //$NON-NLS-1$
												e1);
				        				}
				        				catch (IOException e1)
				        				{
				        				JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantDownloadFromAddress")); //$NON-NLS-1$
				        				
				        				logger
										.error(
												"actionPerformed(java.awt.event.ActionEvent)", //$NON-NLS-1$
												e1);
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

				        progressDialog.setTitle(aplicacion.getI18nString(I18N.get("SystemConfigPanel.CopyingTemplates"))); //$NON-NLS-1$
				        progressDialog.report(aplicacion.getI18nString(I18N.get("SystemConfigPanel.CopyingTemplates"))); //$NON-NLS-1$
				        
				        
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
				        				
				        				GeopistaFunctionUtils
										.extractResources(
												".*", //$NON-NLS-1$
												destino, new URL(aplicacion.getUserPreference(AppContext.UPDATE_SERVER_URL,AppContext.DEFAULT_UPDATE_SERVER,false)+"/plantillas.jar"), //$NON-NLS-1$
												progressDialog);
				        				processCancel.terminateProcess();
				        				
				        				//System.out.println(aplicacion.getUserPreference(AppContext.UPDATE_SERVER_URL,AppContext.DEFAULT_UPDATE_SERVER,false));
				        				}
				        				catch (MalformedURLException e1)
				        				{
				        				JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantConnectToAddress")); //$NON-NLS-1$
				        				logger
										.error(
												"actionPerformed(java.awt.event.ActionEvent)", //$NON-NLS-1$
												e1);
				        				}
				        				catch (IOException e1)
				        				{
				        					JOptionPane.showMessageDialog(SystemConfigPanel.this,I18N.get("SystemConfigPanel.CantDownloadFromAddress")); //$NON-NLS-1$
				        				
				        					logger.error("actionPerformed(java.awt.event.ActionEvent)", //$NON-NLS-1$
												e1);
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
	private JTextField getDataBaseAddress() {
		if (dataBaseAddress == null) {
			dataBaseAddress = new JTextField();
		}
		return dataBaseAddress;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getReportpasswordTextField() {
		if (reportpasswordTextField == null) {
			reportpasswordTextField = new JTextField();
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
        
        private JTextField getUrlMapServerJTextField()
        {
            if (urlMapServerJTextField == null)
            {
                urlMapServerJTextField = new JTextField();
            }
            return urlMapServerJTextField;
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
    		fr.setSize(500,500);
    		fr.getContentPane().add(new SystemConfigPanel());
    		fr.setVisible(true);
    		
	}
	/**
	 * This is the default constructor
	 */
	public SystemConfigPanel() {
		super();
		
		initialize();
		setupFields();
	}
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
		String localeString=aplicacion.getUserPreference(AppContext.PREFERENCES_LOCALE_KEY,
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
						    textLocale.setText("Valencià (España)");
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
		
				
		getServerAddressField().setText(aplicacion.getUserPreference(AppContext.HOST_ADMCAR,"",true)); //$NON-NLS-1$
		getDataFolderField().setText(aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH,false));
		getTexturesFolderField().setText(aplicacion.getUserPreference(Texture.TEXTURES_DIRECTORY_PARAMETER,AppContext.DEFAULT_DATA_PATH+"textures",false)); //$NON-NLS-1$
		getUpdateURLTextField().setText(aplicacion.getString(AppContext.UPDATE_SERVER_URL, AppContext.DEFAULT_UPDATE_SERVER));
//		getIdMunicipioField().setText(aplicacion.getUserPreference(AppContext.CODIGOMUNICIPIO,"",true)); //$NON-NLS-1$
        getReportpasswordTextField().setText(aplicacion.getUserPreference(AppContext.REPORTPASS_KEY,"",true)); //$NON-NLS-1$
        getReportUrlJasperTextField().setText(aplicacion.getString(AppContext.REPORTURLJASPER_KEY,"")); //$NON-NLS-1$
		getDataBaseAddress().setText(aplicacion.getString(AppContext.DDBB_SERVER_URL,""));
        getUrlTomcatJTextField().setText(aplicacion.getUserPreference(AppContext.URL_TOMCAT,"",true));
        getUrlMapServerJTextField().setText(aplicacion.getUserPreference(AppContext.URL_MAPSERVER,"http://localhost",true));
        
        //----NUEVO---->
        if(CertificateUtils.isDNIeActive()){
	        getOperativeSystemComboBox().setSelectedItem(aplicacion.getUserPreference(CertificateConstants.OPERATIVE_SYSTEM,CertificateConstants.DEFAULT_WINDOWS_XP_NAME,true));
	        if(CertificateUtils.isDefaultWindowsCertificateStore()){
				getSystemCertStoreTextField().setEnabled(false);
				getSystemCertStoreButton().setEnabled(false);        	
	       }
	       else{
	        	getSystemCertStoreTextField().setEnabled(true);
				getSystemCertStoreButton().setEnabled(true);     
	       }
	       getSystemCertStoreTextField().setText(aplicacion.getUserPreference(CertificateConstants.SYSTEM_CERT_STORE,CertificateUtils.getDefaultWindows7JksSystemCertStore(),true));   
	        
	       getDNIeLibraryTextField().setText(aplicacion.getUserPreference(CertificateConstants.DNIE_LIBRARY,CertificateConstants.DEFAULT_WINDOWS_DNIE_LIBRARY,true));
	       osSelected = true;
		}
        //--FIN NUEVO-->
	}
	/**
	 * @return
	 */
	private Locale[] getAvailableLocales()
	{
	String [] locales= aplicacion.getString(AppContext.PREFERENCES_AVAILABLE_LANGUAGES_KEY,"es_ES").split(";"); //$NON-NLS-1$ //$NON-NLS-2$
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
		//this.setSize(new Dimension(269, 333));  // Generated
		//this.setPreferredSize(new java.awt.Dimension(300,300));  // Generated
		this.setSize(new Dimension(569, 533));  // Generated
		this.setPreferredSize(new java.awt.Dimension(600,600));  // Generated
		
		
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
		aplicacion.setUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,getDataFolderField().getText());
		aplicacion.setUserPreference(AppContext.PREFERENCES_LOCALE_KEY,getJComboBox().getSelectedItem().toString());
		aplicacion.setUserPreference(AppContext.HOST_ADMCAR,getServerAddressField().getText());
		aplicacion.setUserPreference(Texture.TEXTURES_DIRECTORY_PARAMETER,getTexturesFolderField().getText());
//		aplicacion.setUserPreference(AppContext.CODIGOMUNICIPIO,getIdMunicipioField().getText());
		aplicacion.setUserPreference(AppContext.UPDATE_SERVER_URL,getUpdateURLTextField().getText());
		aplicacion.setUserPreference(AppContext.DDBB_SERVER_URL,getDataBaseAddress().getText());
        aplicacion.setUserPreference(AppContext.REPORTPASS_KEY,getReportpasswordTextField().getText());
        aplicacion.setUserPreference(AppContext.REPORTURLJASPER_KEY,getReportUrlJasperTextField().getText());
        aplicacion.setUserPreference(AppContext.URL_TOMCAT,getUrlTomcatJTextField().getText());
        aplicacion.setUserPreference(AppContext.URL_MAPSERVER,getUrlMapServerJTextField().getText());
        
        //----NUEVO---->
        if(CertificateUtils.isDNIeActive()){
        	aplicacion.setUserPreference(CertificateConstants.OPERATIVE_SYSTEM,getOperativeSystemComboBox().getSelectedItem().toString());
        	aplicacion.setUserPreference(CertificateConstants.SYSTEM_CERT_STORE,getSystemCertStoreTextField().getText());
        	aplicacion.setUserPreference(CertificateConstants.DNIE_LIBRARY,getDNIeLibraryTextField().getText());        
        }
        //--FIN NUEVO-->
        StringTokenizer st = new StringTokenizer(aplicacion.getString(AppContext.HOST_ADMCAR), ":");
        if (st.countTokens() == 3){
        	aplicacion.setUserPreference(AppContext.PROTOCOL, st.nextToken());
        	st.nextToken();
        	aplicacion.setUserPreference(AppContext.PORT_ADMCAR,st.nextToken());
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
