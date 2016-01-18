/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ConnectionDialog.java
 * 
 * Created on 9 maggio 2003, 17.25
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.connection.DriverPool;
import it.businesslogic.ireport.connection.EJBQLConnection;
import it.businesslogic.ireport.connection.JDBCConnection;
import it.businesslogic.ireport.connection.JRCSVDataSourceConnection;
import it.businesslogic.ireport.connection.JRCsvDataSourceInspector;
import it.businesslogic.ireport.connection.JRCustomConnection;
import it.businesslogic.ireport.connection.JRCustomDataSourceConnection;
import it.businesslogic.ireport.connection.JRDataSourceProviderConnection;
import it.businesslogic.ireport.connection.JREmptyDatasourceConnection;
import it.businesslogic.ireport.connection.JRHibernateConnection;
import it.businesslogic.ireport.connection.JRSpringLoadedHibernateConnection;
import it.businesslogic.ireport.connection.JRXMLADataSourceConnection;
import it.businesslogic.ireport.connection.JRXMLDataSourceConnection;
import it.businesslogic.ireport.connection.JavaBeanDataSourceConnection;
import it.businesslogic.ireport.connection.MondrianConnection;
import it.businesslogic.ireport.connection.QueryExecuterConnection;
import it.businesslogic.ireport.data.olap.CustomHTTPAuthenticator;
import it.businesslogic.ireport.gui.locale.LocaleSelectorDialog;
import it.businesslogic.ireport.gui.locale.TimeZoneDialog;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import java.awt.Component;
import java.io.File;
import java.net.Authenticator;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import rex.graphics.datasourcetree.elements.DataSourceTreeElement;

/**
 *
 * @author  Administrator
 */
public class ConnectionDialog extends javax.swing.JDialog {
    /** Creates new form JRParameterDialog */
    
    private boolean init = false;
    private Locale tmpXMLLocale = null;
    private TimeZone tmpXMLTimeZone = null;
    
    
       
     public ConnectionDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
     }
     
    public ConnectionDialog(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
        initAll();
    }
        
    public void initAll()
    {
        initComponents();
//        this.setSize(400, 300);
        applyI18n();
        this.pack();
        Misc.centerFrame(this);
        init = true;
                
        jComboBoxConnectionType.addItem(new Tag("0", I18n.getString("connectionType.jdbc", "Database JDBC connection")  )); // Type 0
        jComboBoxConnectionType.addItem(new Tag("1", I18n.getString("connectionType.xml", "XML file datasource"))); // Type 1
        jComboBoxConnectionType.addItem(new Tag("2", I18n.getString("connectionType.javabeans", "JavaBeans set datasource"))); // Type 2
        jComboBoxConnectionType.addItem(new Tag("3", I18n.getString("connectionType.customDataSource", "Custom JRDataSource"))); // Type 3        
        jComboBoxConnectionType.addItem(new Tag("4", I18n.getString("connectionType.csv", "File CSV datasource"))); // Type 4        
        jComboBoxConnectionType.addItem(new Tag("5", I18n.getString("connectionType.datasourceProvider", "JRDataSourceProvider"))); // Type 5 
        jComboBoxConnectionType.addItem(new Tag("6", I18n.getString("connectionType.hibernate", "Hibernate connection"))); // Type 6 
        jComboBoxConnectionType.addItem(new Tag("9", I18n.getString("connectionType.hibernateSpring", "Spring loaded Hibernate connection"))); // Type 6 
        jComboBoxConnectionType.addItem(new Tag("7", I18n.getString("connectionType.ejbql", "EJBQL connection"))); // Type 7 
        jComboBoxConnectionType.addItem(new Tag("8", I18n.getString("connectionType.olap", "Mondrian OLAP connection"))); // Type 8 
        jComboBoxConnectionType.addItem(new Tag("10", I18n.getString("connectionType.queryExecuter", "Query Executer mode"))); // Type 10 
        jComboBoxConnectionType.addItem(new Tag("11", I18n.getString("connectionType.emptyDataSource", "Empty data source"))); // Type 11 
        jComboBoxConnectionType.addItem(new Tag("12", I18n.getString("connectionType.customIReportConnection", "Custom iReport connection"))); // Type 12 
        /**   
         * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
         * All Rights Reserved
         * www.cincom.com
         *
         */
        /* Adding XMLA Server Connection Type */
        jComboBoxConnectionType.addItem(new Tag("13", I18n.getString("connectionType.xmlaServer", "XMLA Server"))); // Type 13 
        
        jComboBoxJDBCDriver.addItem("com.mysql.jdbc.Driver");
        jComboBoxJDBCDriver.addItem("org.gjt.mm.mysql.Driver");
        jComboBoxJDBCDriver.addItem("com.internetcds.jdbc.tds.Driver");
	jComboBoxJDBCDriver.addItem("net.sourceforge.jtds.jdbc.Driver");
        jComboBoxJDBCDriver.addItem("com.microsoft.jdbc.sqlserver.SQLServerDriver");
	jComboBoxJDBCDriver.addItem("sun.jdbc.odbc.JdbcOdbcDriver");
	jComboBoxJDBCDriver.addItem("com.ms.jdbc.odbc.JdbcOdbcDriver");
	jComboBoxJDBCDriver.addItem("oracle.jdbc.driver.OracleDriver");
	jComboBoxJDBCDriver.addItem("COM.ibm.db2.jdbc.app.DB2Driver");
	jComboBoxJDBCDriver.addItem("com.informix.jdbc.IfxDriver");
	jComboBoxJDBCDriver.addItem("com.sybase.jdbc2.jdbc.SybDriver");
	jComboBoxJDBCDriver.addItem("com.merant.datadirect.jdbc.sqlserver.SQLServerDriver");
	jComboBoxJDBCDriver.addItem("com.inet.tds.TdsDriver");
	jComboBoxJDBCDriver.addItem("org.postgresql.Driver");
	jComboBoxJDBCDriver.addItem("org.hsqldb.jdbcDriver");
	jComboBoxJDBCDriver.addItem("COM.cloudscape.JDBCDriver");
        
        Vector conns = MainFrame.getMainInstance().getConnections();
        for (int i=0; i<conns.size(); ++i)
        {
            IReportConnection con = (IReportConnection)conns.elementAt(i);
            if (con instanceof JDBCConnection)
            {
                jComboBoxMondrianJdbc.addItem( con.getName() );
            }
        }
        
        if (jComboBoxMondrianJdbc.getItemCount() > 0)
        {
            jComboBoxMondrianJdbc.setSelectedIndex(0);
        }
        
        // All languages, countries and time zones....
        
        init = false;
        
        jComboBoxConnectionType.setSelectedIndex(0);
        //System.out.println(jListCVSColumns);
        jListCVSColumns.setModel( new DefaultListModel());
        jTextFieldCVSDateFormat.setText( new SimpleDateFormat().toPattern());
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupBeansSetType = new javax.swing.ButtonGroup();
        buttonGroupCVSFieldSeparator = new javax.swing.ButtonGroup();
        buttonGroupCVSFieldSeparator1 = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanelConnectionType = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxConnectionType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jPanelConnectionRoot = new javax.swing.JPanel();
        jPanelXML = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldXMLFile = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jRadioButtonXML_connection = new javax.swing.JRadioButton();
        jRadioButtonXML_datasource = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        jLabelXMLRecordPath = new javax.swing.JLabel();
        jTextFieldRecordPath = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabelXMLDatePattern = new javax.swing.JLabel();
        jTextFieldXMLDatePattern = new javax.swing.JTextField();
        jButtonXMLDatePattern = new javax.swing.JButton();
        jLabelXMLNumberPattern = new javax.swing.JLabel();
        jTextFieldXMLNumberPattern = new javax.swing.JTextField();
        jButtonXMLNumberPattern = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelXMLLocale = new javax.swing.JLabel();
        jTextFieldXMLLocaleValue = new javax.swing.JTextField();
        jLabelXMLTimeZone = new javax.swing.JLabel();
        jButtonXMLLocale = new javax.swing.JButton();
        jTextFieldXMLTimeZoneValue = new javax.swing.JTextField();
        jButtonXMLTimeZone = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jPanelBeansSet = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldJBSetFactoryClass = new javax.swing.JTextField();
        jRadioButtonJBSetCollection = new javax.swing.JRadioButton();
        jRadioButtonJBSetArray = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldJBSetMethodToCall = new javax.swing.JTextField();
        jCheckBoxisUseFieldDescription = new javax.swing.JCheckBox();
        jSeparator6 = new javax.swing.JSeparator();
        jPanelJDBC = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxJDBCDriver = new javax.swing.JComboBox();
        jTextFieldJDBCUrl = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDBName = new javax.swing.JTextField();
        jTextFieldServerAddress = new javax.swing.JTextField();
        jButtonWizard = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JPasswordField();
        jTextFieldUsername = new javax.swing.JTextField();
        jCheckBoxSavePassword = new javax.swing.JCheckBox();
        jTextArea1 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanelCustomDataSourceFactory = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldJRCustomDataSourceFactoryClass = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldJRCustomDataSourceMethod = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelCSV = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldCSVFilename = new javax.swing.JTextField();
        jButtonCSVFilename = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCVSColumns = new javax.swing.JList();
        jPanel9 = new javax.swing.JPanel();
        jButtonNewParameter = new javax.swing.JButton();
        jButtonModifyParameter = new javax.swing.JButton();
        jButtonDeleteParameter = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButtonNewParameter1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jCheckBoxCVSDateFormat = new javax.swing.JCheckBox();
        jTextFieldCVSDateFormat = new javax.swing.JTextField();
        jButtonCVSDateFormat = new javax.swing.JButton();
        jCheckBoxCVSFirstRowAsHeader = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonCVSSeparatorComma = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorSpace = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorTab = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorSemicolon = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorOther = new javax.swing.JRadioButton();
        jTextFieldCVSSeparatorText = new javax.swing.JTextField();
        jRadioButtonCVSSeparatorNewLine = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jRadioButtonCVSSeparatorComma1 = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorSpace1 = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorTab1 = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorSemicolon1 = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorNewLine1 = new javax.swing.JRadioButton();
        jRadioButtonCVSSeparatorOther1 = new javax.swing.JRadioButton();
        jTextFieldCVSSeparatorText1 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanelDatasourceProvider = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldJRDataSourceProvider = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jPanelHibernate = new javax.swing.JPanel();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jPanelEJBQL = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldPersistanceUnit = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanelMondrian = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxMondrianJdbc = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldCatalogURI = new javax.swing.JTextField();
        jButtonBrowseCatalog = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanelSpringLoadedHibernate = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextFieldSLHSpringConfig = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldSLHSessionFactory = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanelQueryExecutorMode = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanelEmptyDataSource = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jSpinnerNumRecords = new javax.swing.JSpinner();
        jPanelCustomConnection = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldIReportConnectionClassName = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabelPropertiesTable = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableCustomProperties = new javax.swing.JTable();
        jButtonAddProp = new javax.swing.JButton();
        jButtonRemoveProp = new javax.swing.JButton();
        jPanelXMLA = new javax.swing.JPanel();
        jLabelXMLAUrl = new javax.swing.JLabel();
        jTextFieldXMLAUrl = new javax.swing.JTextField();
        jButtonGetXMLAMetadata = new javax.swing.JButton();
        jLabelXMLADatasource = new javax.swing.JLabel();
        jComboBoxXMLADatasource = new javax.swing.JComboBox();
        jLabelXMLACatalog = new javax.swing.JLabel();
        jComboBoxXMLACatalog = new javax.swing.JComboBox();
        jSeparator8 = new javax.swing.JSeparator();
        jLabelXMLACube = new javax.swing.JLabel();
        jComboBoxXMLACube = new javax.swing.JComboBox();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel16 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextFieldUsername1 = new javax.swing.JTextField();
        jTextFieldPassword1 = new javax.swing.JPasswordField();
        jCheckBoxSavePassword1 = new javax.swing.JCheckBox();
        jLabelAttention = new javax.swing.JLabel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonOK1 = new javax.swing.JButton();

        setTitle("Connections properties");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanelConnectionType.setLayout(new java.awt.GridBagLayout());

        jPanelConnectionType.setMinimumSize(new java.awt.Dimension(164, 90));
        jPanelConnectionType.setPreferredSize(new java.awt.Dimension(10, 90));
        jLabel4.setText("Type of connection / datasource");
        jLabel4.setMaximumSize(new java.awt.Dimension(157, 21));
        jLabel4.setPreferredSize(new java.awt.Dimension(157, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 3);
        jPanelConnectionType.add(jLabel4, gridBagConstraints);

        jComboBoxConnectionType.setMinimumSize(new java.awt.Dimension(25, 21));
        jComboBoxConnectionType.setPreferredSize(new java.awt.Dimension(25, 21));
        jComboBoxConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConnectionTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 5);
        jPanelConnectionType.add(jComboBoxConnectionType, gridBagConstraints);

        jLabel1.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 3);
        jPanelConnectionType.add(jLabel1, gridBagConstraints);

        jTextFieldName.setMinimumSize(new java.awt.Dimension(6, 21));
        jTextFieldName.setPreferredSize(new java.awt.Dimension(11, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 4, 5);
        jPanelConnectionType.add(jTextFieldName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelConnectionType.add(jSeparator2, gridBagConstraints);

        jPanel1.add(jPanelConnectionType, java.awt.BorderLayout.NORTH);

        jPanelConnectionRoot.setLayout(new java.awt.BorderLayout());

        jPanelConnectionRoot.setPreferredSize(new java.awt.Dimension(400, 350));
        jPanelXML.setLayout(new java.awt.GridBagLayout());

        jPanelXML.setPreferredSize(new java.awt.Dimension(1, 30));
        jLabel9.setText("XML file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelXML.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelXML.add(jTextFieldXMLFile, gridBagConstraints);

        jButton2.setText("Browse");
        jButton2.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButton2.setMaximumSize(new java.awt.Dimension(100, 25));
        jButton2.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton2.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        jPanelXML.add(jButton2, gridBagConstraints);

        buttonGroup1.add(jRadioButtonXML_connection);
        jRadioButtonXML_connection.setText("Use the report XPath expression when filling the report");
        jRadioButtonXML_connection.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonXML_connection.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonXML_connection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonXML_connectionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        jPanelXML.add(jRadioButtonXML_connection, gridBagConstraints);

        buttonGroup1.add(jRadioButtonXML_datasource);
        jRadioButtonXML_datasource.setSelected(true);
        jRadioButtonXML_datasource.setText("Create a datasource using this expression");
        jRadioButtonXML_datasource.setActionCommand("Create a datasource using the following expression");
        jRadioButtonXML_datasource.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonXML_datasource.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonXML_datasource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonXML_datasourceActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        jPanelXML.add(jRadioButtonXML_datasource, gridBagConstraints);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        jLabelXMLRecordPath.setText("Select Expression");
        jLabelXMLRecordPath.setMaximumSize(new java.awt.Dimension(2000, 14));
        jLabelXMLRecordPath.setMinimumSize(new java.awt.Dimension(2, 14));
        jLabelXMLRecordPath.setPreferredSize(new java.awt.Dimension(60, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel13.add(jLabelXMLRecordPath, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        jPanel13.add(jTextFieldRecordPath, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        jPanelXML.add(jPanel13, gridBagConstraints);

        jSeparator9.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 8, 4);
        jPanelXML.add(jSeparator9, gridBagConstraints);

        jLabelXMLDatePattern.setText("Date pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelXML.add(jLabelXMLDatePattern, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelXML.add(jTextFieldXMLDatePattern, gridBagConstraints);

        jButtonXMLDatePattern.setText("Create");
        jButtonXMLDatePattern.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonXMLDatePattern.setMaximumSize(new java.awt.Dimension(100, 25));
        jButtonXMLDatePattern.setMinimumSize(new java.awt.Dimension(70, 25));
        jButtonXMLDatePattern.setPreferredSize(new java.awt.Dimension(70, 25));
        jButtonXMLDatePattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 7);
        jPanelXML.add(jButtonXMLDatePattern, gridBagConstraints);

        jLabelXMLNumberPattern.setText("Number pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 3);
        jPanelXML.add(jLabelXMLNumberPattern, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 3);
        jPanelXML.add(jTextFieldXMLNumberPattern, gridBagConstraints);

        jButtonXMLNumberPattern.setText("Create");
        jButtonXMLNumberPattern.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonXMLNumberPattern.setMaximumSize(new java.awt.Dimension(100, 25));
        jButtonXMLNumberPattern.setMinimumSize(new java.awt.Dimension(70, 25));
        jButtonXMLNumberPattern.setPreferredSize(new java.awt.Dimension(70, 25));
        jButtonXMLNumberPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXMLDatePatternjButton2ActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 7);
        jPanelXML.add(jButtonXMLNumberPattern, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Locale / Time zone"));
        jLabelXMLLocale.setText("Locale");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jLabelXMLLocale, gridBagConstraints);

        jTextFieldXMLLocaleValue.setEditable(false);
        jTextFieldXMLLocaleValue.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel3.add(jTextFieldXMLLocaleValue, gridBagConstraints);

        jLabelXMLTimeZone.setText("Time zone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 24;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel3.add(jLabelXMLTimeZone, gridBagConstraints);

        jButtonXMLLocale.setText("Select...");
        jButtonXMLLocale.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonXMLLocale.setMaximumSize(new java.awt.Dimension(100, 25));
        jButtonXMLLocale.setMinimumSize(new java.awt.Dimension(70, 25));
        jButtonXMLLocale.setPreferredSize(new java.awt.Dimension(70, 25));
        jButtonXMLLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXMLDatePatternjButton2ActionPerformed11(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 8);
        jPanel3.add(jButtonXMLLocale, gridBagConstraints);

        jTextFieldXMLTimeZoneValue.setEditable(false);
        jTextFieldXMLTimeZoneValue.setText("Default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel3.add(jTextFieldXMLTimeZoneValue, gridBagConstraints);

        jButtonXMLTimeZone.setText("Select...");
        jButtonXMLTimeZone.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonXMLTimeZone.setMaximumSize(new java.awt.Dimension(100, 25));
        jButtonXMLTimeZone.setMinimumSize(new java.awt.Dimension(70, 25));
        jButtonXMLTimeZone.setPreferredSize(new java.awt.Dimension(70, 25));
        jButtonXMLTimeZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXMLDatePattern1jButtonXMLDatePatternjButton2ActionPerformed11(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 8);
        jPanel3.add(jButtonXMLTimeZone, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 4);
        jPanelXML.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelXML.add(jSeparator7, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelXML, java.awt.BorderLayout.CENTER);

        jPanelBeansSet.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Factory class (the class that will produce the set)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelBeansSet.add(jLabel12, gridBagConstraints);

        jTextFieldJBSetFactoryClass.setText("it.businesslogic.ireport.connection.SampleJRDataSourceFactory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelBeansSet.add(jTextFieldJBSetFactoryClass, gridBagConstraints);

        buttonGroupBeansSetType.add(jRadioButtonJBSetCollection);
        jRadioButtonJBSetCollection.setSelected(true);
        jRadioButtonJBSetCollection.setText(" Collection of javaBeans");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelBeansSet.add(jRadioButtonJBSetCollection, gridBagConstraints);

        buttonGroupBeansSetType.add(jRadioButtonJBSetArray);
        jRadioButtonJBSetArray.setText("Array of javaBeans");
        jRadioButtonJBSetArray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJBSetArrayActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelBeansSet.add(jRadioButtonJBSetArray, gridBagConstraints);

        jLabel13.setText("The static method to call to retrive the array or the the collection of javaBeans");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelBeansSet.add(jLabel13, gridBagConstraints);

        jTextFieldJBSetMethodToCall.setText("createBeanCollection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelBeansSet.add(jTextFieldJBSetMethodToCall, gridBagConstraints);

        jCheckBoxisUseFieldDescription.setText("Use field description");
        jCheckBoxisUseFieldDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxisUseFieldDescription.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelBeansSet.add(jCheckBoxisUseFieldDescription, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelBeansSet.add(jSeparator6, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelBeansSet, java.awt.BorderLayout.CENTER);

        jPanelJDBC.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("JDBC Driver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel2, gridBagConstraints);

        jLabel3.setText("JDBC URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel3, gridBagConstraints);

        jComboBoxJDBCDriver.setEditable(true);
        jComboBoxJDBCDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJDBCDriverActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jComboBoxJDBCDriver, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jTextFieldJDBCUrl, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("JDBC URL Wizard"));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 100));
        jLabel14.setText("Server Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jLabel14, gridBagConstraints);

        jLabel5.setText("Database");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jTextFieldDBName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jTextFieldServerAddress, gridBagConstraints);

        jButtonWizard.setText("Wizard");
        jButtonWizard.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonWizard.setMaximumSize(new java.awt.Dimension(60, 23));
        jButtonWizard.setMinimumSize(new java.awt.Dimension(60, 23));
        jButtonWizard.setPreferredSize(new java.awt.Dimension(60, 23));
        jButtonWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWizardActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 0);
        jPanel2.add(jButtonWizard, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelJDBC.add(jPanel2, gridBagConstraints);

        jLabel6.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel7, gridBagConstraints);

        jTextFieldPassword.setFont(new java.awt.Font("Tahoma", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jTextFieldPassword, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jTextFieldUsername, gridBagConstraints);

        jCheckBoxSavePassword.setText("Save password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jCheckBoxSavePassword, gridBagConstraints);

        jTextArea1.setText("<html>ATTENTION! Passwords are stored in clear text. If you dont specify a password now, iReport will ask you for one only when required and will not save it.");
        jTextArea1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextArea1.setPreferredSize(new java.awt.Dimension(739, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelJDBC.add(jTextArea1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelJDBC.add(jSeparator5, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelJDBC, java.awt.BorderLayout.CENTER);

        jPanelCustomDataSourceFactory.setLayout(new java.awt.GridBagLayout());

        jLabel10.setText("Factory class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelCustomDataSourceFactory.add(jLabel10, gridBagConstraints);

        jTextFieldJRCustomDataSourceFactoryClass.setText("it.businesslogic.ireport.connection.SampleJRDataSourceFactory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelCustomDataSourceFactory.add(jTextFieldJRCustomDataSourceFactoryClass, gridBagConstraints);

        jLabel11.setText("The static method to call to retrive the JRDataSource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelCustomDataSourceFactory.add(jLabel11, gridBagConstraints);

        jTextFieldJRCustomDataSourceMethod.setText("createDatasource");
        jTextFieldJRCustomDataSourceMethod.setPreferredSize(new java.awt.Dimension(314, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelCustomDataSourceFactory.add(jTextFieldJRCustomDataSourceMethod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelCustomDataSourceFactory.add(jSeparator1, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelCustomDataSourceFactory, java.awt.BorderLayout.CENTER);

        jPanelCSV.setLayout(new java.awt.GridBagLayout());

        jPanelCSV.setPreferredSize(new java.awt.Dimension(1, 30));
        jLabel15.setText("CSV file");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelCSV.add(jLabel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelCSV.add(jTextFieldCSVFilename, gridBagConstraints);

        jButtonCSVFilename.setText("Browse");
        jButtonCSVFilename.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonCSVFilename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCSVFilenameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelCSV.add(jButtonCSVFilename, gridBagConstraints);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Column names"));
        jListCVSColumns.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCVSColumnsValueChanged(evt);
            }
        });
        jListCVSColumns.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListCVSColumnsMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jListCVSColumns);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel8.add(jScrollPane1, gridBagConstraints);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(71, 200));
        jButtonNewParameter.setText("New");
        jButtonNewParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewParameterActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jButtonNewParameter, gridBagConstraints);

        jButtonModifyParameter.setText("Modify");
        jButtonModifyParameter.setEnabled(false);
        jButtonModifyParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyParameterActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel9.add(jButtonModifyParameter, gridBagConstraints);

        jButtonDeleteParameter.setText("Delete");
        jButtonDeleteParameter.setEnabled(false);
        jButtonDeleteParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteParameterActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel9.add(jButtonDeleteParameter, gridBagConstraints);

        jPanel10.setLayout(null);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel8.add(jPanel9, gridBagConstraints);

        jButtonNewParameter1.setText("Get columns name from the first row of the file");
        jButtonNewParameter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewParameterActionPerformed11(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel8.add(jButtonNewParameter1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel11.add(jPanel8, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other"));
        jCheckBoxCVSDateFormat.setText("Use custom date format");
        jCheckBoxCVSDateFormat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxCVSDateFormat.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxCVSDateFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCVSDateFormatActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel6.add(jCheckBoxCVSDateFormat, gridBagConstraints);

        jTextFieldCVSDateFormat.setEnabled(false);
        jTextFieldCVSDateFormat.setPreferredSize(new java.awt.Dimension(100, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel6.add(jTextFieldCVSDateFormat, gridBagConstraints);

        jButtonCVSDateFormat.setText("Date format");
        jButtonCVSDateFormat.setEnabled(false);
        jButtonCVSDateFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCVSDateFormatActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel6.add(jButtonCVSDateFormat, gridBagConstraints);

        jCheckBoxCVSFirstRowAsHeader.setText("Skip the first line (the column names will be read from the first line)");
        jCheckBoxCVSFirstRowAsHeader.setActionCommand("Skip the first line (column names will be read from the first line)");
        jCheckBoxCVSFirstRowAsHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxCVSFirstRowAsHeader.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxCVSFirstRowAsHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCVSDateFormatActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel6.add(jCheckBoxCVSFirstRowAsHeader, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel11.add(jPanel6, gridBagConstraints);

        jTabbedPane1.addTab("Columns", jPanel11);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Field separator (char)"));
        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorComma);
        jRadioButtonCVSSeparatorComma.setSelected(true);
        jRadioButtonCVSSeparatorComma.setText("Comma");
        jRadioButtonCVSSeparatorComma.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorComma.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorComma, gridBagConstraints);

        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorSpace);
        jRadioButtonCVSSeparatorSpace.setText("Space");
        jRadioButtonCVSSeparatorSpace.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorSpace.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorSpace, gridBagConstraints);

        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorTab);
        jRadioButtonCVSSeparatorTab.setText("Tab");
        jRadioButtonCVSSeparatorTab.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorTab.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorTab, gridBagConstraints);

        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorSemicolon);
        jRadioButtonCVSSeparatorSemicolon.setText("Semicolon");
        jRadioButtonCVSSeparatorSemicolon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorSemicolon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorSemicolon, gridBagConstraints);

        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorOther);
        jRadioButtonCVSSeparatorOther.setText("Other");
        jRadioButtonCVSSeparatorOther.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorOther.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorOther, gridBagConstraints);

        jTextFieldCVSSeparatorText.setPreferredSize(new java.awt.Dimension(30, 19));
        jTextFieldCVSSeparatorText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCVSSeparatorTextActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(jTextFieldCVSSeparatorText, gridBagConstraints);

        buttonGroupCVSFieldSeparator.add(jRadioButtonCVSSeparatorNewLine);
        jRadioButtonCVSSeparatorNewLine.setText("New line");
        jRadioButtonCVSSeparatorNewLine.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorNewLine.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel4.add(jRadioButtonCVSSeparatorNewLine, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Row separator"));
        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorComma1);
        jRadioButtonCVSSeparatorComma1.setText("Comma");
        jRadioButtonCVSSeparatorComma1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorComma1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorComma1, gridBagConstraints);

        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorSpace1);
        jRadioButtonCVSSeparatorSpace1.setText("Space");
        jRadioButtonCVSSeparatorSpace1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorSpace1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorSpace1, gridBagConstraints);

        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorTab1);
        jRadioButtonCVSSeparatorTab1.setText("Tab");
        jRadioButtonCVSSeparatorTab1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorTab1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorTab1, gridBagConstraints);

        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorSemicolon1);
        jRadioButtonCVSSeparatorSemicolon1.setText("Semicolon");
        jRadioButtonCVSSeparatorSemicolon1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorSemicolon1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorSemicolon1, gridBagConstraints);

        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorNewLine1);
        jRadioButtonCVSSeparatorNewLine1.setSelected(true);
        jRadioButtonCVSSeparatorNewLine1.setText("New line");
        jRadioButtonCVSSeparatorNewLine1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorNewLine1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorNewLine1, gridBagConstraints);

        buttonGroupCVSFieldSeparator1.add(jRadioButtonCVSSeparatorOther1);
        jRadioButtonCVSSeparatorOther1.setText("Other");
        jRadioButtonCVSSeparatorOther1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonCVSSeparatorOther1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jRadioButtonCVSSeparatorOther1, gridBagConstraints);

        jTextFieldCVSSeparatorText1.setPreferredSize(new java.awt.Dimension(30, 19));
        jTextFieldCVSSeparatorText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCVSSeparatorText1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel5.add(jTextFieldCVSSeparatorText1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel7.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jPanel12, gridBagConstraints);

        jTabbedPane1.addTab("Separators", jPanel7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelCSV.add(jTabbedPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelCSV.add(jSeparator4, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelCSV, java.awt.BorderLayout.CENTER);

        jPanelDatasourceProvider.setLayout(new java.awt.GridBagLayout());

        jLabel17.setText("JasperReports DataSource Provider class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 3);
        jPanelDatasourceProvider.add(jLabel17, gridBagConstraints);

        jTextFieldJRDataSourceProvider.setText("it.businesslogic.ireport.examples.PersonBeansDataSource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelDatasourceProvider.add(jTextFieldJRDataSourceProvider, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDatasourceProvider.add(jSeparator3, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelDatasourceProvider, java.awt.BorderLayout.CENTER);

        jPanelHibernate.setLayout(new java.awt.GridBagLayout());

        jTextPane1.setText("Press the test button.\\n\\niReport will look in the classpath for a valid hibernate configuration.");
        jTextPane1.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelHibernate.add(jTextPane1, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/hibernate.png")));
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelHibernate.add(jLabel8, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelHibernate, java.awt.BorderLayout.CENTER);

        jPanelEJBQL.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Persistance Unit Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelEJBQL.add(jLabel18, gridBagConstraints);

        jTextFieldPersistanceUnit.setMargin(new java.awt.Insets(0, 5, 2, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 4);
        jPanelEJBQL.add(jTextFieldPersistanceUnit, gridBagConstraints);

        jScrollPane2.setBorder(null);
        jScrollPane2.setFocusable(false);
        jScrollPane2.setRequestFocusEnabled(false);
        jScrollPane2.setVerifyInputWhenFocusTarget(false);
        jTextPane2.setBorder(null);
        jTextPane2.setEditable(false);
        jTextPane2.setText("iReport will search for persistence.xml files within the META-INF directory of any CLASSPATH element");
        jTextPane2.setOpaque(false);
        jScrollPane2.setViewportView(jTextPane2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
        jPanelEJBQL.add(jScrollPane2, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelEJBQL, java.awt.BorderLayout.CENTER);

        jPanelMondrian.setLayout(new java.awt.GridBagLayout());

        jLabel20.setText("Jdbc Connection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelMondrian.add(jLabel20, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelMondrian.add(jComboBoxMondrianJdbc, gridBagConstraints);

        jLabel19.setText("Catalog URI (i.e. file:/path/schema.xml)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelMondrian.add(jLabel19, gridBagConstraints);

        jTextFieldCatalogURI.setMargin(new java.awt.Insets(0, 5, 2, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 3, 4);
        jPanelMondrian.add(jTextFieldCatalogURI, gridBagConstraints);

        jButtonBrowseCatalog.setText("Browse...");
        jButtonBrowseCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseCatalogActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        jPanelMondrian.add(jButtonBrowseCatalog, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanelMondrian.add(jPanel14, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelMondrian, java.awt.BorderLayout.CENTER);

        jPanelSpringLoadedHibernate.setLayout(new java.awt.GridBagLayout());

        jLabel21.setText("Spring configuration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelSpringLoadedHibernate.add(jLabel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelSpringLoadedHibernate.add(jTextFieldSLHSpringConfig, gridBagConstraints);

        jLabel22.setText("Session Factory Bean ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelSpringLoadedHibernate.add(jLabel22, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelSpringLoadedHibernate.add(jTextFieldSLHSessionFactory, gridBagConstraints);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/hibernate.png")));
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelSpringLoadedHibernate.add(jLabel16, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelSpringLoadedHibernate, java.awt.BorderLayout.CENTER);

        jPanelQueryExecutorMode.setLayout(new java.awt.GridBagLayout());

        jPanelQueryExecutorMode.setMinimumSize(new java.awt.Dimension(125, 204));
        jPanelQueryExecutorMode.setPreferredSize(new java.awt.Dimension(20, 20));
        jLabel23.setText("   ");
        jPanelQueryExecutorMode.add(jLabel23, new java.awt.GridBagConstraints());

        jPanelConnectionRoot.add(jPanelQueryExecutorMode, java.awt.BorderLayout.CENTER);

        jPanelEmptyDataSource.setLayout(new java.awt.GridBagLayout());

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Number of empty records");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 20, 0, 4);
        jPanelEmptyDataSource.add(jLabel24, gridBagConstraints);

        jSpinnerNumRecords.setPreferredSize(new java.awt.Dimension(70, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 20);
        jPanelEmptyDataSource.add(jSpinnerNumRecords, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelEmptyDataSource, java.awt.BorderLayout.CENTER);

        jPanelCustomConnection.setLayout(new java.awt.GridBagLayout());

        jLabel25.setText("Class name of the IReportConnection implementation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 3);
        jPanelCustomConnection.add(jLabel25, gridBagConstraints);

        jTextFieldIReportConnectionClassName.setText("it.businesslogic.ireport.connection.JREmptyDatasourceConnection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 3);
        jPanelCustomConnection.add(jTextFieldIReportConnectionClassName, gridBagConstraints);

        jPanel15.setLayout(new java.awt.GridBagLayout());

        jLabelPropertiesTable.setText("IReportConnection properties");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 3);
        jPanel15.add(jLabelPropertiesTable, gridBagConstraints);

        jTableCustomProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableCustomProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCustomPropertiesMouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(jTableCustomProperties);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel15.add(jScrollPane3, gridBagConstraints);

        jButtonAddProp.setText("Add");
        jButtonAddProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPropActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel15.add(jButtonAddProp, gridBagConstraints);

        jButtonRemoveProp.setText("Remove");
        jButtonRemoveProp.setEnabled(false);
        jButtonRemoveProp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemovePropActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel15.add(jButtonRemoveProp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelCustomConnection.add(jPanel15, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelCustomConnection, java.awt.BorderLayout.NORTH);

        jPanelXMLA.setLayout(new java.awt.GridBagLayout());

        jLabelXMLAUrl.setText("Url of XML/A server");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelXMLA.add(jLabelXMLAUrl, gridBagConstraints);

        jTextFieldXMLAUrl.setText("http://localhost:8080/mondrian/xmla");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelXMLA.add(jTextFieldXMLAUrl, gridBagConstraints);

        jButtonGetXMLAMetadata.setText("Get metadata");
        jButtonGetXMLAMetadata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetXMLAMetadataActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelXMLA.add(jButtonGetXMLAMetadata, gridBagConstraints);

        jLabelXMLADatasource.setText("Datasource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelXMLA.add(jLabelXMLADatasource, gridBagConstraints);

        jComboBoxXMLADatasource.setMinimumSize(new java.awt.Dimension(23, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelXMLA.add(jComboBoxXMLADatasource, gridBagConstraints);

        jLabelXMLACatalog.setText("Catalog");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelXMLA.add(jLabelXMLACatalog, gridBagConstraints);

        jComboBoxXMLACatalog.setMinimumSize(new java.awt.Dimension(23, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelXMLA.add(jComboBoxXMLACatalog, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelXMLA.add(jSeparator8, gridBagConstraints);

        jLabelXMLACube.setText("Cube");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        jPanelXMLA.add(jLabelXMLACube, gridBagConstraints);

        jComboBoxXMLACube.setMinimumSize(new java.awt.Dimension(23, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelXMLA.add(jComboBoxXMLACube, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelXMLA.add(jSeparator10, gridBagConstraints);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabel26.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel16.add(jLabel26, gridBagConstraints);

        jLabel27.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel16.add(jLabel27, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel16.add(jTextFieldUsername1, gridBagConstraints);

        jTextFieldPassword1.setFont(new java.awt.Font("Tahoma", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel16.add(jTextFieldPassword1, gridBagConstraints);

        jCheckBoxSavePassword1.setText("Save password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel16.add(jCheckBoxSavePassword1, gridBagConstraints);

        jLabelAttention.setText("<html>ATTENTION! Passwords are stored in clear text. If you dont specify a password now, iReport will ask you for one only when required and will not save it.");
        jLabelAttention.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jLabelAttention, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelXMLA.add(jPanel16, gridBagConstraints);

        jPanelConnectionRoot.add(jPanelXMLA, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanelConnectionRoot, java.awt.BorderLayout.CENTER);

        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonOK.setMnemonic('s');
        jButtonOK.setText("Save");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonOK);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonCancel);

        jButtonOK1.setMnemonic('t');
        jButtonOK1.setText("Test");
        jButtonOK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOK1ActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonOK1);

        jPanel1.add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-500)/2, (screenSize.height-531)/2, 500, 531);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonXML_datasourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonXML_datasourceActionPerformed
        XMLDataSourceCheckBoxesChanged();
    }//GEN-LAST:event_jRadioButtonXML_datasourceActionPerformed

    private void jButtonXMLDatePattern1jButtonXMLDatePatternjButton2ActionPerformed11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXMLDatePattern1jButtonXMLDatePatternjButton2ActionPerformed11

            TimeZoneDialog lsd = new TimeZoneDialog(this, true);
            if (this.tmpXMLTimeZone != null)
            {
                lsd.setReportTimeZoneId( tmpXMLTimeZone.getID() );
            }
            
            lsd.setVisible(true);
            
            if (lsd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                if (lsd.getReportTimeZoneId() == null)
                {
                    tmpXMLTimeZone = null;
                    jTextFieldXMLTimeZoneValue.setText( I18n.getString("timezone.default","Default") );
                }
                else
                {
                    tmpXMLTimeZone = TimeZone.getTimeZone( lsd.getReportTimeZoneId() );
                    jTextFieldXMLTimeZoneValue.setText( tmpXMLTimeZone.getDisplayName( I18n.getCurrentLocale()) ); 
                }
            }
    }//GEN-LAST:event_jButtonXMLDatePattern1jButtonXMLDatePatternjButton2ActionPerformed11

    private void jButtonXMLDatePatternjButton2ActionPerformed11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXMLDatePatternjButton2ActionPerformed11

            LocaleSelectorDialog lsd = new LocaleSelectorDialog(this, true);
            if (this.tmpXMLLocale != null)
            {
                lsd.setSelectedLocale( this.tmpXMLLocale );
            }
            
            lsd.setVisible(true);
            
            if (lsd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                tmpXMLLocale = lsd.getSelectedLocale();
                jTextFieldXMLLocaleValue.setText( 
                        (tmpXMLLocale == null) ? I18n.getString("timezone.default","Default") :
                        tmpXMLLocale.getDisplayName( I18n.getCurrentLocale()) ); 
            }
        
    }//GEN-LAST:event_jButtonXMLDatePatternjButton2ActionPerformed11

    private void jButtonXMLDatePatternjButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXMLDatePatternjButton2ActionPerformed1

            FieldPatternDialog fpd = new FieldPatternDialog(this, true);
            fpd.setOnlyNumbers(true);
            if (jTextFieldXMLNumberPattern.getText().length() >0)
            {
                fpd.setPattern( jTextFieldXMLNumberPattern.getText() );
            }
            
            fpd.setVisible(true);
            
            if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                jTextFieldXMLNumberPattern.setText( fpd.getPattern() );
            }
        
        
    }//GEN-LAST:event_jButtonXMLDatePatternjButton2ActionPerformed1

    private void jButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed1
            
            FieldPatternDialog fpd = new FieldPatternDialog(this, true);
            fpd.setOnlyDate(true);
            if (jTextFieldXMLDatePattern.getText().length() >0)
            {
                fpd.setPattern( jTextFieldXMLDatePattern.getText() );
            }
            
            fpd.setVisible(true);
            
            if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                jTextFieldXMLDatePattern.setText( fpd.getPattern() );
            }
    }//GEN-LAST:event_jButton2ActionPerformed1

    private void jButtonGetXMLAMetadataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetXMLAMetadataActionPerformed
        jComboBoxXMLADatasource.removeActionListener(dsListener);
        jComboBoxXMLACatalog.removeActionListener(catListener);
        
        String urlstr = this.jTextFieldXMLAUrl.getText().trim();
        
        Authenticator.setDefault(new CustomHTTPAuthenticator( jTextFieldUsername1.getText(), new String(jTextFieldPassword1.getPassword()) ));

        
        rex.metadata.ServerMetadata smd = new rex.metadata.ServerMetadata(urlstr,(Component)getParent());
       
        if (smd.isValidUrl() == false) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    I18n.getString("messages.connectionDialog.xmla.invalidUrl","Unable to connect to XMLA server.") ,"",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.jComboBoxXMLADatasource.removeAllItems();
        this.jComboBoxXMLACatalog.removeAllItems();
        this.jComboBoxXMLACube.removeAllItems(); 
        rex.graphics.datasourcetree.elements.DataSourceTreeElement dste[] = smd.discoverDataSources();
        if (dste == null) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    "No Datasources found.","",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (int i=0;i<dste.length;i++){
            this.jComboBoxXMLADatasource.addItem(dste[i].getDataSourceInfo());
        }
        rex.graphics.datasourcetree.elements.DataSourceTreeElement catalogs[] = dste[0].getChildren();
        if (catalogs == null || catalogs.length == 0) {
            return;
        }
        this.jComboBoxXMLACatalog.removeAllItems();
        for (int i = 0;i<catalogs.length;i++){
            this.jComboBoxXMLACatalog.addItem(((rex.graphics.datasourcetree.elements.CatalogElement)catalogs[i]).toString());
        }
         //get Cube information for the selected catalog...
         rex.graphics.datasourcetree.elements.DataSourceTreeElement cubes[] = catalogs[0].getChildren();
        if(cubes ==null || cubes.length==0){
            return;
        }
         this.jComboBoxXMLACube.removeAllItems(); 
         for(int i=0;i<cubes.length; i++){
             this.jComboBoxXMLACube.addItem(((rex.graphics.datasourcetree.elements.CubeElement)cubes[i]).toString());
         }       
        jComboBoxXMLADatasource.addActionListener(dsListener);
        jComboBoxXMLACatalog.addActionListener(catListener);
    }//GEN-LAST:event_jButtonGetXMLAMetadataActionPerformed

    private void jTableCustomPropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCustomPropertiesMouseClicked

       jButtonRemoveProp.setEnabled( jTableCustomProperties.getSelectedRow() >= 0 );
        
    }//GEN-LAST:event_jTableCustomPropertiesMouseClicked

    private void jButtonRemovePropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemovePropActionPerformed

        if (jTableCustomProperties.getSelectedRow() >= 0)
        {
            ((DefaultTableModel)jTableCustomProperties.getModel()).removeRow( jTableCustomProperties.getSelectedRow() );
            jButtonRemoveProp.setEnabled( jTableCustomProperties.getSelectedRow() >= 0 );
        }
    }//GEN-LAST:event_jButtonRemovePropActionPerformed

    private void jButtonAddPropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPropActionPerformed

        ((DefaultTableModel)jTableCustomProperties.getModel()).addRow(new Object[]{"name","value"});
        
    }//GEN-LAST:event_jButtonAddPropActionPerformed

    private void jRadioButtonXML_connectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonXML_connectionActionPerformed

        XMLDataSourceCheckBoxesChanged();
        
    }//GEN-LAST:event_jRadioButtonXML_connectionActionPerformed

    
    public void XMLDataSourceCheckBoxesChanged()
    {  
        jTextFieldRecordPath.setEnabled( jRadioButtonXML_datasource.isSelected()  );
        jLabelXMLRecordPath.setEnabled( jRadioButtonXML_datasource.isSelected()  );   
    }
    private void jButtonBrowseCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseCatalogActionPerformed
            
            String fileName = "";
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( MainFrame.getMainInstance().getCurrentDirectory());
	    
	    jfc.setDialogTitle("Select file....");
	    
	    jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName();
			    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "XML *.xml";
		    }
	    });
	    
	    jfc.setMultiSelectionEnabled(false);
	    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
	    if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
		    java.io.File file = jfc.getSelectedFile();		
                    try {
                        jTextFieldCatalogURI.setText( file.toURI() + "");
                    } catch (Exception ex){}
	    }        
        
        
    }//GEN-LAST:event_jButtonBrowseCatalogActionPerformed

    private void jListCVSColumnsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCVSColumnsValueChanged

        if (jListCVSColumns.getSelectedIndex() >= 0)
        {
            jButtonModifyParameter.setEnabled(true);
            jButtonDeleteParameter.setEnabled(true);
        }
        else
        {
            jButtonModifyParameter.setEnabled(false);
            jButtonDeleteParameter.setEnabled(false);
        }
        
    }//GEN-LAST:event_jListCVSColumnsValueChanged

    private void jListCVSColumnsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListCVSColumnsMouseClicked

            if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
            {
                jButtonModifyParameterActionPerformed1(null);
            }
        
    }//GEN-LAST:event_jListCVSColumnsMouseClicked

    private void jCheckBoxCVSDateFormatActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCVSDateFormatActionPerformed1
// TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxCVSDateFormatActionPerformed1

    private void jButtonNewParameterActionPerformed11(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewParameterActionPerformed11

        try {
            JRCsvDataSourceInspector ds = new JRCsvDataSourceInspector(new File( jTextFieldCSVFilename.getText()));
        
            if (jRadioButtonCVSSeparatorComma.isSelected()) ds.setFieldDelimiter(',');
            if (jRadioButtonCVSSeparatorTab.isSelected()) ds.setFieldDelimiter('\t');
            if (jRadioButtonCVSSeparatorSpace.isSelected()) ds.setFieldDelimiter(' ');
            if (jRadioButtonCVSSeparatorSemicolon.isSelected()) ds.setFieldDelimiter(';');
            if (jRadioButtonCVSSeparatorNewLine.isSelected()) ds.setFieldDelimiter('\n');
            if (jRadioButtonCVSSeparatorOther.isSelected()) ds.setFieldDelimiter((jTextFieldCVSSeparatorText.getText()+" ").charAt(0));
            
            if (jRadioButtonCVSSeparatorComma1.isSelected()) ds.setRecordDelimiter(",");
            if (jRadioButtonCVSSeparatorTab1.isSelected()) ds.setRecordDelimiter("\t");
            if (jRadioButtonCVSSeparatorSpace1.isSelected()) ds.setRecordDelimiter(" ");
            if (jRadioButtonCVSSeparatorSemicolon1.isSelected()) ds.setRecordDelimiter(";");
            if (jRadioButtonCVSSeparatorNewLine1.isSelected()) ds.setRecordDelimiter("\n");
            if (jRadioButtonCVSSeparatorOther1.isSelected()) ds.setRecordDelimiter(jTextFieldCVSSeparatorText1.getText());
            
            DefaultListModel dlm = (DefaultListModel)jListCVSColumns.getModel();
            dlm.removeAllElements();
            
            Vector names = ds.getColumnNames();
            for (int i=0; i < names.size(); ++i )
            {
                String fname = (names.elementAt(i)+"").trim();
                if (fname.length() > 0)
                  dlm.addElement(fname);
            }
            
            if (names.size() > 0)
            {
                jListCVSColumns.setSelectedIndex(0);
            }
        
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,ex.getMessage(), I18n.getString("message.title.exception","Exception"), JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButtonNewParameterActionPerformed11

    private void jButtonDeleteParameterActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteParameterActionPerformed1
        
        // Get the selected connection...
        DefaultListModel dlm = (DefaultListModel)jListCVSColumns.getModel();
        Object[] values = jListCVSColumns.getSelectedValues();

        for (int i=0; i<values.length; ++i)
        {
            dlm.removeElement(values[i]);
        }
    }//GEN-LAST:event_jButtonDeleteParameterActionPerformed1

    private void jButtonModifyParameterActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyParameterActionPerformed1
        
        int index = jListCVSColumns.getSelectedIndex();
        
        if (index < 0) return;
        
        DefaultListModel dlm = (DefaultListModel)jListCVSColumns.getModel();
        String oldName = (String)dlm.getElementAt(index); 
        
        String name = JOptionPane.showInputDialog(this, I18n.getString("connectionDialog.input.columnName","Column name" ), oldName);
        
        if (name != null)
        {
            dlm.setElementAt(name, index);
        }
    }//GEN-LAST:event_jButtonModifyParameterActionPerformed1

    private void jButtonNewParameterActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewParameterActionPerformed1
        
        DefaultListModel dlm = (DefaultListModel)jListCVSColumns.getModel();
        String name = JOptionPane.showInputDialog(this, I18n.getString("connectionDialog.input.columnName","Column name" ), "COLUMN_" + dlm.getSize());
        
        if (name != null)
        {
            dlm.addElement(name);
        }
        
        if (dlm.size() == 1)
        {
            jListCVSColumns.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jButtonNewParameterActionPerformed1

    private void jTextFieldCVSSeparatorText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCVSSeparatorText1ActionPerformed
    
        if (this.jTextFieldCVSSeparatorText1.getText().length() > 0)
        this.jRadioButtonCVSSeparatorOther1.setSelected(true);
    }//GEN-LAST:event_jTextFieldCVSSeparatorText1ActionPerformed

    private void jTextFieldCVSSeparatorTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCVSSeparatorTextActionPerformed

        if (this.jTextFieldCVSSeparatorText.getText().length() > 0)
        this.jRadioButtonCVSSeparatorOther.setSelected(true);
        
    }//GEN-LAST:event_jTextFieldCVSSeparatorTextActionPerformed

    private void jButtonCVSDateFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCVSDateFormatActionPerformed

            FieldPatternDialog fpd = new FieldPatternDialog(this, true);
            fpd.setOnlyDate(true);
            
            fpd.setVisible(true);
            if (fpd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                jTextFieldCVSDateFormat.setText( fpd.getPattern() );
            }
        
    }//GEN-LAST:event_jButtonCVSDateFormatActionPerformed

    private void jCheckBoxCVSDateFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCVSDateFormatActionPerformed
        
        jTextFieldCVSDateFormat.setEnabled( this.jCheckBoxCVSDateFormat.isSelected());
        jButtonCVSDateFormat.setEnabled(  this.jCheckBoxCVSDateFormat.isSelected() );
        
        if (!this.jCheckBoxCVSDateFormat.isSelected())
        {
            jTextFieldCVSDateFormat.setText( new SimpleDateFormat().toPattern());
        }
        
    }//GEN-LAST:event_jCheckBoxCVSDateFormatActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String fileName = "";
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( MainFrame.getMainInstance().getCurrentDirectory());
	    
	    jfc.setDialogTitle("Select XML file....");
	    
	    jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName();
			    return (filename.toLowerCase().endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "XML *.xml";
		    }
	    });
	    
	    jfc.setMultiSelectionEnabled(false);
	    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
	    if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
		    java.io.File file = jfc.getSelectedFile();		
                    try {
                        jTextFieldXMLFile.setText( file.getAbsolutePath() );
                    } catch (Exception ex){}
	    }        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonCSVFilenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCSVFilenameActionPerformed
            String fileName = "";
	    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( MainFrame.getMainInstance().getCurrentDirectory());
	    
	    jfc.setDialogTitle("Select CSV file....");
	    
	    jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName();
			    return (filename.toLowerCase().endsWith(".csv") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "CSV *.csv";
		    }
	    });
	    
	    jfc.setMultiSelectionEnabled(false);
	    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
	    if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
		    java.io.File file = jfc.getSelectedFile();		
                    try {
                        jTextFieldCSVFilename.setText( file.getAbsolutePath() );
                    } catch (Exception ex){}
	    }        
        
        
        
    }//GEN-LAST:event_jButtonCSVFilenameActionPerformed

    private void jComboBoxJDBCDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxJDBCDriverActionPerformed
        // Run the wizard to populate Connection URL
        jButtonWizardActionPerformed(null);
    }//GEN-LAST:event_jComboBoxJDBCDriverActionPerformed

    private void jRadioButtonJBSetArrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonJBSetArrayActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jRadioButtonJBSetArrayActionPerformed

    private void jButtonOK1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOK1ActionPerformed
        //java.awt.Frame parent = Misc.frameFromComponent(this);
        // 
        MainFrame.getMainInstance().getReportClassLoader().rescanLibDirectory();
        
        int selectedConnectionType = Integer.parseInt( ""+ ((Tag)jComboBoxConnectionType.getSelectedItem()).getValue() );
        
        if (selectedConnectionType == 0)
        {
            
            // Try the java connection...
            Connection conn = null;
            Statement stmt = null;
            try {
		    try {
                    	DriverPool.registerDriver( (String)jComboBoxJDBCDriver.getSelectedItem(), MainFrame.getMainInstance().getReportClassLoader() );
                    } catch (Exception ex)
                    {
                    	DriverPool.registerDriver( (String)jComboBoxJDBCDriver.getSelectedItem(), this.getClass().getClassLoader() );
                    }
                    
                    java.sql.Driver driver = DriverPool.getDriver( this.jTextFieldJDBCUrl.getText() );
                    
                    java.util.Properties connectProps = new java.util.Properties();
                    connectProps.setProperty("user", this.jTextFieldUsername.getText());
                    connectProps.setProperty("password", this.jTextFieldPassword.getText());
                    
                    conn = driver.connect( this.jTextFieldJDBCUrl.getText(), connectProps); 
				stmt = conn.createStatement();
                                
			}catch (NoClassDefFoundError ex)
                        {
                                JOptionPane.showMessageDialog((Component)getParent(),
                                        I18n.getFormattedString("messages.connection.noClassDefFoundError",
                                        "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                                        new Object[]{"", ""+ex.getMessage()}),
                                        I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                                return;					
                        } 
                        catch (ClassNotFoundException ex)
                        {
                                JOptionPane.showMessageDialog((Component)getParent(),
                                        I18n.getFormattedString("messages.connection.classNotFoundError",
                                        "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                                        new Object[]{"", ""+ex.getMessage(), "" + jComboBoxJDBCDriver.getSelectedItem()}),
                                        I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                                return;				
                        } 
                        catch (java.sql.SQLException ex)
                        {
                                JOptionPane.showMessageDialog((Component)getParent(),
                                        I18n.getFormattedString("messages.connection.sqlError",
                                        "{0}\nSQL problems: {1}\n{2}",
                                        new Object[]{"", ""+ex.getMessage(), "" + this.jTextFieldJDBCUrl.getText()}),
                                        I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                                return;					
                        } 
                        catch (Exception ex)
			{
			JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.connection.generalError",
                                "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                                new Object[]{"", ""+ex.getMessage()}),
                                I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                                return;									
			} finally {
                // Clean up
                if( stmt!=null ) try{ stmt.close(); } catch(Exception e) { /* anyone really care? */ }
                if( conn!=null ) try{ conn.close(); } catch(Exception e) { /* anyone really care? */ }
            }
			JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
			return;
        }
        else if (selectedConnectionType == 1)
        {
            String xml_file = jTextFieldXMLFile.getText().trim();
            
            try {
                
                java.io.File f = new java.io.File(xml_file);
                if (!f.exists())
                {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connectionDialog.fileNotFound","File {0} not found", new Object[]{xml_file}),
                            I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                
                JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog((Component)getParent(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
                return;	
            }

        }
        else if (selectedConnectionType == 3)
        {
        	
        	
            try {
                Object obj = Class.forName((String)jTextFieldJRCustomDataSourceFactoryClass.getText().trim(), true, MainFrame.getMainInstance().getReportClassLoader()).newInstance();
                obj.getClass().getMethod( (String)jTextFieldJRCustomDataSourceMethod.getText().trim(), new Class[0]).invoke(obj,new Object[0]);                
            }catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + jTextFieldJRCustomDataSourceFactoryClass.getText().trim()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
            	
            	
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                    I18n.getFormattedString("messages.connection.generalError",
                    "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                    new Object[]{"", ""+ex.getMessage()}),
                    I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;									
            }
            JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
	    return;
        }
        else if (selectedConnectionType == 2)
        {
            try {
                Object obj = Class.forName((String)jTextFieldJBSetFactoryClass.getText().trim(), true, MainFrame.getMainInstance().getReportClassLoader()).newInstance();
                Object ret_obj = obj.getClass().getMethod( (String)jTextFieldJBSetMethodToCall.getText().trim(), new Class[0]).invoke(null,new Object[0]);                
            
                if (ret_obj != null && !jRadioButtonJBSetArray.isSelected() && (ret_obj instanceof  java.util.Collection))
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                }
                else if (ret_obj != null  && jRadioButtonJBSetArray.isSelected() && (ret_obj instanceof  Object[]))
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.notValidValueReturned",
                            "The method don't return a valid Array or java.util.Collection!\n"),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                }
                
            }catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + jTextFieldJBSetFactoryClass.getText().trim()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
            	ex.printStackTrace();
            	
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                    I18n.getFormattedString("messages.connection.generalError",
                    "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                    new Object[]{"", ""+ex.getMessage()}),
                    I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;									
            }
            
	    return;
        }
        else if (selectedConnectionType == 4)
        {
            String csv_file = jTextFieldCSVFilename.getText().trim();
            
            try {
                
                JRCSVDataSourceConnection con = new JRCSVDataSourceConnection();
                java.io.File f = new java.io.File(csv_file);
                if (!f.exists())
                {
                    JOptionPane.showMessageDialog((Component)getParent(),
                         I18n.getFormattedString("messages.connectionDialog.fileNotFound","File {0} not found", new Object[]{csv_file}),
                         I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                
                con.setFilename( csv_file );
                if (con.getJRDataSource() != null)
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog((Component)getParent(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
                return;	
            }
            
	    return;
        }
        else if (selectedConnectionType == 5)
        {
            try {
                
                Object obj = Class.forName((String)jTextFieldJRDataSourceProvider.getText().trim(), true, MainFrame.getMainInstance().getReportClassLoader()).newInstance();
                if (!(obj instanceof net.sf.jasperreports.engine.JRDataSourceProvider))
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getFormattedString("messages.connectionDialog.invalidJRDataSourceProvider","\"{0}\" is not a subclass of\nnet.sf.jasperreports.engine.JRDataSourceProvider.", new Object[]{jTextFieldJRDataSourceProvider.getText()}),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                else
                {
                    JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
            } catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + jTextFieldJBSetFactoryClass.getText().trim()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                    I18n.getFormattedString("messages.connection.generalError",
                    "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                    new Object[]{"", ""+ex.getMessage()}),
                    I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;									
            }
        }
        else if (selectedConnectionType == 6)
        {
            try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
                        SessionFactory hb_sessionFactory = null;
                        try {

                            hb_sessionFactory = new Configuration().configure().buildSessionFactory();

                            JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog((Component)getParent(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                    }
                });
            } catch (Exception ex)
            {}
        }
        else if (selectedConnectionType == 7)
        {
            try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        
                        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
                        SessionFactory hb_sessionFactory = null;
                        try {

                              EJBQLConnection  iReportConn = new EJBQLConnection();
                              if (jTextFieldPersistanceUnit.getText().trim().length() != 0)
                              {
                                    iReportConn.getProperties().put("PersistenceUnit", jTextFieldPersistanceUnit.getText().trim());
                              }

                              iReportConn.getEntityManager();
                              iReportConn.closeEntityManager();
                              JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog((Component)getParent(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                        
                    }
                });
            } catch (Exception ex)
            {}
        }
        else if (selectedConnectionType == 8)
        {
            try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        
                        
                        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
                        try {

                              MondrianConnection  iReportConn = new MondrianConnection();
                              iReportConn.getProperties().put(MondrianConnection.CATALOG_URI, jTextFieldCatalogURI.getText().trim());
                              iReportConn.getProperties().put(MondrianConnection.CONNECTION_NAME, jComboBoxMondrianJdbc.getSelectedItem()+"");
            

                              iReportConn.getMondrianConnection();
                              iReportConn.closeMondrianConnection();
                              JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog((Component)getParent(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                    }
                });
            } catch (Exception ex)
            {}
        }
        else if (selectedConnectionType == 9) {
        	try {
        		Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
        		JRSpringLoadedHibernateConnection iReportConn = new JRSpringLoadedHibernateConnection();
        		iReportConn.setSessionFactoryBeanId(jTextFieldSLHSessionFactory.getText().trim());
        		iReportConn.setSpringConfig(jTextFieldSLHSpringConfig.getText().trim());
 
        		SessionFactory sf = iReportConn.getSessionFactory();
        		if (sf == null) {
        			JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.noSessionFactoryReturned","No session factory returned.  Check your session factory bean id against the spring configuration."),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
            		
        		}
        		JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.hibernateConnectionTestSuccessful","iReport successfully created a Hibernate session factory from your Spring configuration."),"",JOptionPane.INFORMATION_MESSAGE);
        	} catch (Exception e) {
        		e.printStackTrace();
        		JOptionPane.showMessageDialog((Component)getParent(),e.getMessage(), I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
        		
        	}
        }
        else if (selectedConnectionType == 10)
        {
            JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
	    return;
        }
        else if (selectedConnectionType == 11)
        {
            JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
	    return;
        }
        else if (selectedConnectionType == 13)
        {
            /**   
             * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
             * All Rights Reserved
             * www.cincom.com
             *
             */
            String urlstr = this.jTextFieldXMLAUrl.getText().trim();
            Authenticator.setDefault(new CustomHTTPAuthenticator( jTextFieldUsername1.getText(), new String(jTextFieldPassword1.getPassword()) ));
        
                String dataSource = (String)this.jComboBoxXMLADatasource.getSelectedItem();
                String catalog = (String)this.jComboBoxXMLACatalog.getSelectedItem();
                rex.metadata.ServerMetadata smd = new rex.metadata.ServerMetadata(urlstr,(Component)getParent());
                if (smd.isValidUrl() == false) {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getString("messages.connectionDialog.connectionXMLATestFailed.InvalidUrl","Connection test failed! Unable to connect to url."),
                            "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                rex.graphics.datasourcetree.elements.DataSourceTreeElement dste[] = smd.discoverDataSources();
                if (dste == null || dste.length == 0) {
                    JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getString("messages.connectionDialog.connectionXMLATestFailed.NoDatasources","Connection test failed! No datasources found."),
                            "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                boolean found = false;
                int intI =0;
                if (dataSource != null && dataSource.length() > 0) {
                    while (!found && intI < dste.length) {
                        if (dataSource.compareTo(dste[intI].getDataSourceInfo()) == 0){
                            found = true;
                        } else {
                            intI++;
                        }
                    }
                    if (!found) {
                        JOptionPane.showMessageDialog((Component)getParent(),
                            I18n.getFormattedString("messages.connectionDialog.connectionXMLATestFailed.NoDatasourceFound","Connection test failed! Datasource {0} not found.", new Object[]{dataSource+""} ),
                            "",JOptionPane.INFORMATION_MESSAGE);
                        return;                        
                    }
                    if (catalog != null && catalog.length() > 0) {
                        found = false;
                        rex.graphics.datasourcetree.elements.DataSourceTreeElement cats[] = dste[intI].getChildren();
                        if (cats == null || cats.length == 0) {
                            JOptionPane.showMessageDialog((Component)getParent(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestFailed.NoCatalogs","Connection test failed! No catalogs found in datasource."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                            return;                                                    
                        }
                        intI = 0;
                        while (!found && intI < cats.length) {
                            if (catalog.compareTo(
                              ((rex.graphics.datasourcetree.elements.CatalogElement)cats[intI]).toString()) == 0){
                                found = true;
                            }
                            else{
                                intI++;
                            }
                        }
                        if (!found) {
                            JOptionPane.showMessageDialog((Component)getParent(),
                                I18n.getFormattedString("messages.connectionDialog.connectionXMLATestFailed.NoCatalogFound","Connection test failed! Catalog {0} not found in datasource.", new Object[]{catalog+""} ),
                                "",JOptionPane.INFORMATION_MESSAGE);
                            return;                                                                                
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog((Component)getParent(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful.NoCatalog","Connection test successful! Connected to server and found datasource, but no catalog specified."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                else {
                    JOptionPane.showMessageDialog((Component)getParent(),
                                I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful.NoDatasource","Connection test successful! Connected to server, but no datasource specified."),
                                "",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog((Component)getParent(),
                        I18n.getString("messages.connectionDialog.connectionXMLATestSuccessful","Connection test successful! Catalog found in datasource on xmla server."),
                        "",JOptionPane.INFORMATION_MESSAGE);                    
                /* end of modification */
                
//            } catch (Exception ex)
//            {}
        }
        else 
        {
            // try to perform a test...
            try {
                IReportConnection connection = createConnection(selectedConnectionType);
                if (connection == null) throw new Exception(I18n.getString("messages.connectionDialog.connectionTestError","unable to instance the connecion class to test!"));
            
                connection.test();
                JOptionPane.showMessageDialog((Component)getParent(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
            } catch (Throwable e) {
                e.printStackTrace();
        	JOptionPane.showMessageDialog((Component)getParent(),e.getMessage(), I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
    }//GEN-LAST:event_jButtonOK1ActionPerformed

    private void jButtonWizardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWizardActionPerformed
          
        if (jComboBoxJDBCDriver.getSelectedIndex() < 0) return;
        String driver = ""+jComboBoxJDBCDriver.getSelectedItem();
        driver = driver.trim();
        if (driver.equals("")) return;
        
        String server = jTextFieldServerAddress.getText().trim();
        if( server.length()==0 ) {
            server = "localhost";
        }
        
        String databaseName = jTextFieldDBName.getText().trim();
        
        if (driver.equalsIgnoreCase("org.gjt.mm.mysql.Driver")) {
            String url = "jdbc:mysql://" + server + "/";
            if ( databaseName.length()>0 )
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.mysql.jdbc.Driver")) {
            String url = "jdbc:mysql://" + server + "/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.internetcds.jdbc.tds.Driver")) {
            String url = "jdbc:freetds:sqlserver://localhost/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.microsoft.jdbc.sqlserver.SQLServerDriver")) {
            String url = "jdbc:microsoft:sqlserver://" + server + ":1433;DatabaseName=";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("sun.jdbc.odbc.JdbcOdbcDriver")) {
            String url = "jdbc:odbc:";
            if (databaseName.length()>0)
                url += jTextFieldDBName.getText();
            else
                url += "DSNAME";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.ms.jdbc.odbc.JdbcOdbcDriver")) {
            String url = "jdbc:odbc:";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "DSNAME";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
            String url = "jdbc:oracle:thin:@" + server + ":1521:";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("COM.ibm.db2.jdbc.app.DB2Driver")) {
            String url = "jdbc:db2:";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.informix.jdbc.IfxDriver")) {
            String url = "jdbc:informix-sqli://" + server + ":port/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            
            url += ":informixserver=SERVERNAME";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.sybase.jdbc2.jdbc.SybDriver")) {
            String url = "jdbc:sybase:Tds:" + server + ":2638/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.mysql.jdbc.Driver")) {
            String url = "jdbc:mysql://"+server+"/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.merant.datadirect.jdbc.sqlserver.SQLServerDriver")) {
            String url = "jdbc:sqlserver://" + server + ":1433/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("com.inet.tds.TdsDriver")) {
            String url = "jdbc:inetdae7:"+server+":1433/";
            if (databaseName.length()>0)
                url += jTextFieldDBName.getText();
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("org.postgresql.Driver")) {
            String url = "jdbc:postgresql://" + server + ":5432/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("org.hsqldb.jdbcDriver")) {
            String url = "jdbc:hsqldb:[PATH_TO_DB_FILES]/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        }
        else if (driver.equalsIgnoreCase("COM.cloudscape.JDBCDriver ")) {
            String url = "jdbc:cloudscape:/cloudscape/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            jTextFieldJDBCUrl.setText(url);
        } 
        else if (driver.equalsIgnoreCase("net.sourceforge.jtds.jdbc.Driver"))
        {
            String url = "jdbc:jtds:sqlserver://" +
            server + "/";
            if (databaseName.length()>0)
                url += databaseName;
            else
                url += "MYDATABASE";
            url += ";instance=";
            jTextFieldJDBCUrl.setText(url);
        }

    }//GEN-LAST:event_jButtonWizardActionPerformed

    private void jComboBoxConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConnectionTypeActionPerformed
        if (init) return;
        jPanelConnectionRoot.removeAll();
        int selectedConnectionType = Integer.parseInt( ""+ ((Tag)jComboBoxConnectionType.getSelectedItem()).getValue() );
        
        if (selectedConnectionType == 0)
        {
            jPanelConnectionRoot.add("Center", jPanelJDBC);
        }
        else if (selectedConnectionType == 1)
        {
            jPanelConnectionRoot.add("Center", jPanelXML);
        }
        else if (selectedConnectionType == 2)
        {
            jPanelConnectionRoot.add("Center", jPanelBeansSet);
        }
        else if (selectedConnectionType == 3)
        {
            jPanelConnectionRoot.add("Center", jPanelCustomDataSourceFactory);
        }
        else if (selectedConnectionType == 4)
        {
            jPanelConnectionRoot.add("Center", jPanelCSV);
        }
        else if (selectedConnectionType == 5)
        {
            jPanelConnectionRoot.add("Center", jPanelDatasourceProvider);
        }
        else if (selectedConnectionType == 6)
        {
            jPanelConnectionRoot.add("Center", jPanelHibernate);
        }
        else if (selectedConnectionType == 7)
        {
            jPanelConnectionRoot.add("Center", jPanelEJBQL);
        }
        else if (selectedConnectionType == 8)
        {
            jPanelConnectionRoot.add("Center", jPanelMondrian);
        }
        else if (selectedConnectionType == 9)
        {
            jPanelConnectionRoot.add("Center", jPanelSpringLoadedHibernate);
        }
        else if (selectedConnectionType == 10)
        {
            jPanelConnectionRoot.add("Center", jPanelQueryExecutorMode);
        }
        else if (selectedConnectionType == 11)
        {
            jPanelConnectionRoot.add("Center", jPanelEmptyDataSource);
        }
        else if (selectedConnectionType == 12)
        {
            jPanelConnectionRoot.add("Center", jPanelCustomConnection);
        }
        else if (selectedConnectionType == 13)
        {
            jPanelConnectionRoot.add("Center", jPanelXMLA);
        }
        
        
        jPanelConnectionRoot.updateUI();
    }//GEN-LAST:event_jComboBoxConnectionTypeActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        if (this.jTextFieldName.getText().trim().length() <= 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("messages.connectionDialog.invalidName","Please insert a valid connection name!"),
                    I18n.getString("messages.connectionDialog.invalidNameCaption","Invalid connection name!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }
        int selectedConnectionType = Integer.parseInt( ""+ ((Tag)jComboBoxConnectionType.getSelectedItem()).getValue() );
        
        iReportConnection = createConnection( selectedConnectionType);
        if (iReportConnection == null) return;
        
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return dialogResult;
    }
    
    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    /** Getter for property iReportConnection.
     * @return Value of property iReportConnection.
     *
     */
    public it.businesslogic.ireport.IReportConnection getIReportConnection() {
        return iReportConnection;
    }
    
    /** Setter for property iReportConnection.
     * @param iReportConnection New value of property iReportConnection.
     *
     */
    public void setIReportConnection(it.businesslogic.ireport.IReportConnection iReportConnection) {
        this.iReportConnection = iReportConnection;
        
        this.jTextFieldName.setText( iReportConnection.getName());
        if (iReportConnection instanceof JDBCConnection)
        {
            JDBCConnection con = (JDBCConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "0");
            this.jComboBoxJDBCDriver.setSelectedItem(con.getJDBCDriver());
            this.jTextFieldJDBCUrl.setText( con.getUrl());
            this.jTextFieldServerAddress.setText( con.getServerAddress() );
            this.jTextFieldDBName.setText( con.getDatabase() );
            this.jTextFieldUsername.setText( con.getUsername());
            if (con.isSavePassword())
                this.jTextFieldPassword.setText( con.getPassword());
            else 
                this.jTextFieldPassword.setText( "");
            this.jCheckBoxSavePassword.setSelected( con.isSavePassword());
        }
        else if (iReportConnection instanceof JRCustomDataSourceConnection)
        {
            JRCustomDataSourceConnection con = (JRCustomDataSourceConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "3");
            this.jTextFieldJRCustomDataSourceFactoryClass.setText( con.getFactoryClass());
            this.jTextFieldJRCustomDataSourceMethod.setText(con.getMethodToCall());
        }
        else if (iReportConnection instanceof JavaBeanDataSourceConnection)
        {
            JavaBeanDataSourceConnection con = (JavaBeanDataSourceConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "2");
            this.jTextFieldJBSetFactoryClass.setText( con.getFactoryClass());
            this.jTextFieldJBSetMethodToCall.setText(con.getMethodToCall());
            this.jCheckBoxisUseFieldDescription.setSelected(con.isUseFieldDescription());
            if (con.getType().equals(con.BEAN_ARRAY)  )
            {
                jRadioButtonJBSetArray.setSelected(true);
                jRadioButtonJBSetCollection.setSelected(false);
            }
            else
            {
                jRadioButtonJBSetArray.setSelected(false);
                jRadioButtonJBSetCollection.setSelected(true);
            }
        }
        else if (iReportConnection instanceof JRCSVDataSourceConnection)
        {
            JRCSVDataSourceConnection con = (JRCSVDataSourceConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "4");
            this.jTextFieldCSVFilename.setText( con.getFilename() );
            
            if (con.getCustomDateFormat().length() > 0)
            {
                this.jCheckBoxCVSDateFormat.setSelected(true);
                this.jTextFieldCVSDateFormat.setText(con.getCustomDateFormat());
                this.jTextFieldCVSDateFormat.setEnabled(true);
                this.jButtonCVSDateFormat.setEnabled(true);
            }
            
            this.jCheckBoxCVSFirstRowAsHeader.setSelected( con.isUseFirstRowAsHeader() );
            
            String fieldSeparator = con.getFieldDelimiter();
            if (fieldSeparator.equals(",")) this.jRadioButtonCVSSeparatorComma.setSelected(true);
            else if (fieldSeparator.equals("\t")) this.jRadioButtonCVSSeparatorTab.setSelected(true);
            else if (fieldSeparator.equals(" ")) this.jRadioButtonCVSSeparatorSpace.setSelected(true);
            else if (fieldSeparator.equals(";")) this.jRadioButtonCVSSeparatorSemicolon.setSelected(true);
            else if (fieldSeparator.equals("\n")) this.jRadioButtonCVSSeparatorNewLine.setSelected(true);
            else {
                this.jRadioButtonCVSSeparatorOther.setSelected(true);
                this.jTextFieldCVSSeparatorText.setText(fieldSeparator);
            }
            
            String rowSeparator = con.getRecordDelimiter();
            if (rowSeparator.equals(",")) this.jRadioButtonCVSSeparatorComma1.setSelected(true);
            else if (rowSeparator.equals("\t")) this.jRadioButtonCVSSeparatorTab1.setSelected(true);
            else if (rowSeparator.equals(" ")) this.jRadioButtonCVSSeparatorSpace1.setSelected(true);
            else if (rowSeparator.equals(";")) this.jRadioButtonCVSSeparatorSemicolon1.setSelected(true);
            else if (rowSeparator.equals("\n")) this.jRadioButtonCVSSeparatorNewLine1.setSelected(true);
            else {
                this.jRadioButtonCVSSeparatorOther1.setSelected(true);
                this.jTextFieldCVSSeparatorText1.setText(rowSeparator);
            }
            
            DefaultListModel dlm = (DefaultListModel)this.jListCVSColumns.getModel();

            for (int i=0; i< con.getColumnNames().size(); ++i)
            {
                dlm.addElement(con.getColumnNames().elementAt(i)+"" );
            }
            
            if (dlm.size() > 0)
            {
                jListCVSColumns.setSelectedIndex(0);
            }
            
        }
        else if (iReportConnection instanceof JRXMLDataSourceConnection)
        {
            JRXMLDataSourceConnection con = (JRXMLDataSourceConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "1");
            this.jTextFieldXMLFile.setText( con.getFilename() );
            this.jTextFieldRecordPath.setText( con.getSelectExpression() );     
            this.jRadioButtonXML_connection.setSelected( con.isUseConnection() );
            this.jRadioButtonXML_datasource.setSelected( !con.isUseConnection() );
            
            this.jTextFieldXMLDatePattern.setText( Misc.nvl(con.getDatePattern() ,"") );
            this.jTextFieldXMLNumberPattern.setText( Misc.nvl(con.getNumberPattern() ,"") );
            
            this.tmpXMLLocale = con.getLocale();
            if (this.tmpXMLLocale != null )
            {
                jTextFieldXMLLocaleValue.setText(  this.tmpXMLLocale.getDisplayName( I18n.getCurrentLocale() ) );
            }
            else
            {
                jTextFieldXMLLocaleValue.setText( I18n.getString("timezone.default","Default") );
            }
            
            this.tmpXMLTimeZone = con.getTimeZone();
            if (this.tmpXMLLocale != null)
            {
                jTextFieldXMLTimeZoneValue.setText(  tmpXMLTimeZone.getDisplayName( I18n.getCurrentLocale() ) );
            }
            else
            {
                jTextFieldXMLTimeZoneValue.setText( I18n.getString("timezone.default","Default") );
            }
            
            
            XMLDataSourceCheckBoxesChanged();
        }
         else if (iReportConnection instanceof JRDataSourceProviderConnection)
        {
            JRDataSourceProviderConnection con = (JRDataSourceProviderConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "5");
            this.jTextFieldJRDataSourceProvider.setText( it.businesslogic.ireport.util.Misc.nvl(con.getProperties().get("JRDataSourceProvider"), "") );            
        }
        else if (iReportConnection instanceof JRSpringLoadedHibernateConnection) {
         	
         	Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "9");
         	this.jTextFieldSLHSpringConfig.setText( ((JRSpringLoadedHibernateConnection)iReportConnection).getSpringConfig() );
         	this.jTextFieldSLHSessionFactory.setText( ((JRSpringLoadedHibernateConnection)iReportConnection).getSessionFactoryBeanId() );
        }
        else if (iReportConnection instanceof JRHibernateConnection)
        {
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "6");
        }
        else if (iReportConnection instanceof EJBQLConnection)
        {
            EJBQLConnection con = (EJBQLConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "7");
            this.jTextFieldPersistanceUnit.setText( it.businesslogic.ireport.util.Misc.nvl(con.getProperties().get("PersistenceUnit"), "") );            
        }
        else if (iReportConnection instanceof MondrianConnection)
        {
            MondrianConnection con = (MondrianConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "8");
            this.jTextFieldCatalogURI.setText( it.businesslogic.ireport.util.Misc.nvl(con.getProperties().get(MondrianConnection.CATALOG_URI), "") );            
            jComboBoxMondrianJdbc.setSelectedItem( con.getConnectionName() );
        }
        else if (iReportConnection instanceof QueryExecuterConnection)
        {
            QueryExecuterConnection con = (QueryExecuterConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "10");
        }
        else if (iReportConnection instanceof JREmptyDatasourceConnection)
        {
            JREmptyDatasourceConnection con = (JREmptyDatasourceConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "11");
            jSpinnerNumRecords.getModel().setValue(new Integer(con.getRecords()));
        }
        else if (iReportConnection instanceof JRCustomConnection)
        {
            JRCustomConnection con = (JRCustomConnection)iReportConnection;
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "12");
            jTextFieldIReportConnectionClassName.setText( con.getIReportConnectionClassName() );
            HashMap map = con.getProperties();
            DefaultTableModel dtm = (DefaultTableModel)jTableCustomProperties.getModel();
            dtm.setRowCount(0);
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext())
            {
                String key = ""+iterator.next();
                Object value = map.get(key);
                if (key.startsWith(JRCustomConnection.PROPERTY_PREFIX))
                {
                    dtm.addRow(new Object[]{  key.substring(JRCustomConnection.PROPERTY_PREFIX.length()), value+""});
                }
            }
        }
        else if (iReportConnection instanceof JRXMLADataSourceConnection)
        {
            /**   
             * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
             * All Rights Reserved
             * www.cincom.com
             *
             */
            JRXMLADataSourceConnection con = (JRXMLADataSourceConnection)iReportConnection;
            //this.jComboBoxConnectionType.setSelectedIndex(9); // referencing XMLA Server
            Misc.setComboboxSelectedTagValue(jComboBoxConnectionType, "13");
            this.jTextFieldXMLAUrl.setText( con.getUrl() );  
            this.jComboBoxXMLADatasource.removeAllItems();
            this.jComboBoxXMLADatasource.addItem(con.getDatasource());
            this.jComboBoxXMLACatalog.removeAllItems();
            this.jComboBoxXMLACatalog.addItem(con.getCatalog());
            this.jComboBoxXMLACube.removeAllItems();
            this.jComboBoxXMLACube.addItem(con.getCube());
            
            this.jTextFieldUsername1.setText( con.getUsername());
            if (con.isSavePassword())
                this.jTextFieldPassword1.setText( con.getPassword());
            else 
                this.jTextFieldPassword1.setText( "");
            this.jCheckBoxSavePassword1.setSelected( con.isSavePassword());
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroupBeansSetType;
    private javax.swing.ButtonGroup buttonGroupCVSFieldSeparator;
    private javax.swing.ButtonGroup buttonGroupCVSFieldSeparator1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAddProp;
    private javax.swing.JButton jButtonBrowseCatalog;
    private javax.swing.JButton jButtonCSVFilename;
    private javax.swing.JButton jButtonCVSDateFormat;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDeleteParameter;
    private javax.swing.JButton jButtonGetXMLAMetadata;
    private javax.swing.JButton jButtonModifyParameter;
    private javax.swing.JButton jButtonNewParameter;
    private javax.swing.JButton jButtonNewParameter1;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonOK1;
    private javax.swing.JButton jButtonRemoveProp;
    private javax.swing.JButton jButtonWizard;
    private javax.swing.JButton jButtonXMLDatePattern;
    private javax.swing.JButton jButtonXMLLocale;
    private javax.swing.JButton jButtonXMLNumberPattern;
    private javax.swing.JButton jButtonXMLTimeZone;
    private javax.swing.JCheckBox jCheckBoxCVSDateFormat;
    private javax.swing.JCheckBox jCheckBoxCVSFirstRowAsHeader;
    private javax.swing.JCheckBox jCheckBoxSavePassword;
    private javax.swing.JCheckBox jCheckBoxSavePassword1;
    private javax.swing.JCheckBox jCheckBoxisUseFieldDescription;
    private javax.swing.JComboBox jComboBoxConnectionType;
    private javax.swing.JComboBox jComboBoxJDBCDriver;
    private javax.swing.JComboBox jComboBoxMondrianJdbc;
    private javax.swing.JComboBox jComboBoxXMLACatalog;
    private javax.swing.JComboBox jComboBoxXMLACube;
    private javax.swing.JComboBox jComboBoxXMLADatasource;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAttention;
    private javax.swing.JLabel jLabelPropertiesTable;
    private javax.swing.JLabel jLabelXMLACatalog;
    private javax.swing.JLabel jLabelXMLACube;
    private javax.swing.JLabel jLabelXMLADatasource;
    private javax.swing.JLabel jLabelXMLAUrl;
    private javax.swing.JLabel jLabelXMLDatePattern;
    private javax.swing.JLabel jLabelXMLLocale;
    private javax.swing.JLabel jLabelXMLNumberPattern;
    private javax.swing.JLabel jLabelXMLRecordPath;
    private javax.swing.JLabel jLabelXMLTimeZone;
    private javax.swing.JList jListCVSColumns;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelBeansSet;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCSV;
    private javax.swing.JPanel jPanelConnectionRoot;
    private javax.swing.JPanel jPanelConnectionType;
    private javax.swing.JPanel jPanelCustomConnection;
    private javax.swing.JPanel jPanelCustomDataSourceFactory;
    private javax.swing.JPanel jPanelDatasourceProvider;
    private javax.swing.JPanel jPanelEJBQL;
    private javax.swing.JPanel jPanelEmptyDataSource;
    private javax.swing.JPanel jPanelHibernate;
    private javax.swing.JPanel jPanelJDBC;
    private javax.swing.JPanel jPanelMondrian;
    private javax.swing.JPanel jPanelQueryExecutorMode;
    private javax.swing.JPanel jPanelSpringLoadedHibernate;
    private javax.swing.JPanel jPanelXML;
    private javax.swing.JPanel jPanelXMLA;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorComma;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorComma1;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorNewLine;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorNewLine1;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorOther;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorOther1;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorSemicolon;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorSemicolon1;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorSpace;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorSpace1;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorTab;
    private javax.swing.JRadioButton jRadioButtonCVSSeparatorTab1;
    private javax.swing.JRadioButton jRadioButtonJBSetArray;
    private javax.swing.JRadioButton jRadioButtonJBSetCollection;
    private javax.swing.JRadioButton jRadioButtonXML_connection;
    private javax.swing.JRadioButton jRadioButtonXML_datasource;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinnerNumRecords;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableCustomProperties;
    private javax.swing.JLabel jTextArea1;
    private javax.swing.JTextField jTextFieldCSVFilename;
    private javax.swing.JTextField jTextFieldCVSDateFormat;
    private javax.swing.JTextField jTextFieldCVSSeparatorText;
    private javax.swing.JTextField jTextFieldCVSSeparatorText1;
    private javax.swing.JTextField jTextFieldCatalogURI;
    private javax.swing.JTextField jTextFieldDBName;
    private javax.swing.JTextField jTextFieldIReportConnectionClassName;
    private javax.swing.JTextField jTextFieldJBSetFactoryClass;
    private javax.swing.JTextField jTextFieldJBSetMethodToCall;
    private javax.swing.JTextField jTextFieldJDBCUrl;
    private javax.swing.JTextField jTextFieldJRCustomDataSourceFactoryClass;
    private javax.swing.JTextField jTextFieldJRCustomDataSourceMethod;
    private javax.swing.JTextField jTextFieldJRDataSourceProvider;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JPasswordField jTextFieldPassword;
    private javax.swing.JPasswordField jTextFieldPassword1;
    private javax.swing.JTextField jTextFieldPersistanceUnit;
    private javax.swing.JTextField jTextFieldRecordPath;
    private javax.swing.JTextField jTextFieldSLHSessionFactory;
    private javax.swing.JTextField jTextFieldSLHSpringConfig;
    private javax.swing.JTextField jTextFieldServerAddress;
    private javax.swing.JTextField jTextFieldUsername;
    private javax.swing.JTextField jTextFieldUsername1;
    private javax.swing.JTextField jTextFieldXMLAUrl;
    private javax.swing.JTextField jTextFieldXMLDatePattern;
    private javax.swing.JTextField jTextFieldXMLFile;
    private javax.swing.JTextField jTextFieldXMLLocaleValue;
    private javax.swing.JTextField jTextFieldXMLNumberPattern;
    private javax.swing.JTextField jTextFieldXMLTimeZoneValue;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables

    private int dialogResult;    
    
    private IReportConnection iReportConnection;
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxCVSDateFormat.setText(I18n.getString("connectionDialog.checkBoxCVSDateFormat","Use custom date format"));
                jCheckBoxCVSFirstRowAsHeader.setText(I18n.getString("connectionDialog.checkBoxCVSFirstRowAsHeader","Skip the first line (the column names will be read from the first line)"));
                jCheckBoxSavePassword.setText(I18n.getString("connectionDialog.checkBoxSavePassword","Save password"));
                jCheckBoxisUseFieldDescription.setText(I18n.getString("connectionDialog.checkBoxisUseFieldDescription","Use field description"));
                jRadioButtonCVSSeparatorComma.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorComma","Comma"));
                jRadioButtonCVSSeparatorComma1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorComma1","Comma"));
                jRadioButtonCVSSeparatorNewLine.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorNewLine","New line"));
                jRadioButtonCVSSeparatorNewLine1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorNewLine1","New line"));
                jRadioButtonCVSSeparatorOther.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorOther","Other"));
                jRadioButtonCVSSeparatorOther1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorOther1","Other"));
                jRadioButtonCVSSeparatorSemicolon.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorSemicolon","Semicolon"));
                jRadioButtonCVSSeparatorSemicolon1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorSemicolon1","Semicolon"));
                jRadioButtonCVSSeparatorSpace.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorSpace","Space"));
                jRadioButtonCVSSeparatorSpace1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorSpace1","Space"));
                jRadioButtonCVSSeparatorTab.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorTab","Tab"));
                jRadioButtonCVSSeparatorTab1.setText(I18n.getString("connectionDialog.radioButtonCVSSeparatorTab1","Tab"));
                jRadioButtonJBSetArray.setText(I18n.getString("connectionDialog.radioButtonJBSetArray","Array of javaBeans"));
                jRadioButtonJBSetCollection.setText(I18n.getString("connectionDialog.radioButtonJBSetCollection"," Collection of javaBeans"));
                jRadioButtonXML_connection.setText(I18n.getString("connectionDialog.radioButtonXML_connection","Use the report XPath expression when filling the report"));
                jRadioButtonXML_datasource.setText(I18n.getString("connectionDialog.radioButtonXML_datasource","Create a datasource using this expression"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButton2.setText(I18n.getString("connectionDialog.button2","Browse"));
                jButtonBrowseCatalog.setText(I18n.getString("connectionDialog.buttonBrowseCatalog","Browse..."));
                jButtonCSVFilename.setText(I18n.getString("connectionDialog.buttonCSVFilename","Browse"));
                jButtonCVSDateFormat.setText(I18n.getString("connectionDialog.buttonCVSDateFormat","Date format"));
                jButtonCancel.setText(I18n.getString("connectionDialog.buttonCancel","Cancel"));
                jButtonDeleteParameter.setText(I18n.getString("connectionDialog.buttonDeleteParameter","Delete"));
                jButtonModifyParameter.setText(I18n.getString("connectionDialog.buttonModifyParameter","Modify"));
                jButtonNewParameter.setText(I18n.getString("connectionDialog.buttonNewParameter","New"));
                jButtonNewParameter1.setText(I18n.getString("connectionDialog.buttonNewParameter1","Get columns name from the first row of the file"));
                jButtonOK.setText(I18n.getString("connectionDialog.buttonOK","Save"));
                jButtonOK1.setText(I18n.getString("connectionDialog.buttonOK1","Test"));
                jButtonWizard.setText(I18n.getString("connectionDialog.buttonWizard","Wizard"));
                jLabel1.setText(I18n.getString("connectionDialog.label1","Name"));
                jLabel10.setText(I18n.getString("connectionDialog.label10","Factory class"));
                jLabel11.setText(I18n.getString("connectionDialog.label11","The static method to call to retrive the JRDataSource"));
                jLabel12.setText(I18n.getString("connectionDialog.label12","Factory class (the class that will produce the set)"));
                jLabel13.setText(I18n.getString("connectionDialog.label13","The static method to call to retrive the array or the the collection of javaBeans"));
                jLabel14.setText(I18n.getString("connectionDialog.label14","Server Address"));
                jLabel15.setText(I18n.getString("connectionDialog.label15","CSV file"));
                jLabel17.setText(I18n.getString("connectionDialog.label17","JasperReports DataSource Provider class"));
                jLabel18.setText(I18n.getString("connectionDialog.label18","Persistance Unit Name"));
                jLabel19.setText(I18n.getString("connectionDialog.label19","Catalog URI (i.e. file:/path/schema.xml)"));
                jLabel2.setText(I18n.getString("connectionDialog.label2","JDBC Driver"));
                jLabel20.setText(I18n.getString("connectionDialog.label20","Jdbc Connection"));
                jLabel21.setText(I18n.getString("connectionDialog.label21","Spring configuration"));
                jLabel22.setText(I18n.getString("connectionDialog.label22","Session Factory Bean ID"));
                jLabel24.setText(I18n.getString("connectionDialog.label24","Number of empty records"));
                jLabel3.setText(I18n.getString("connectionDialog.label3","JDBC URL"));
                jLabel4.setText(I18n.getString("connectionDialog.label4","Type of connection / datasource"));
                jLabel5.setText(I18n.getString("connectionDialog.label5","Database"));
                jLabel6.setText(I18n.getString("connectionDialog.label6","Username"));
                jLabel7.setText(I18n.getString("connectionDialog.label7","Password"));
                jLabel9.setText(I18n.getString("connectionDialog.label9","XML file"));
                jLabelXMLRecordPath.setText(I18n.getString("connectionDialog.labelXMLRecordPath","Select Expression"));
                // End autogenerated code ----------------------
                
                jLabelXMLAUrl.setText(I18n.getString("connectionDialog.labelXMLAUrl","Url of XML/A server"));
                jButtonGetXMLAMetadata.setText(I18n.getString("connectionDialog.buttonGetXMLAMetadata","Get metadata"));
                jLabelXMLADatasource.setText(I18n.getString("connectionDialog.labelXMLADatasource","Datasource"));
                jLabelXMLACatalog.setText(I18n.getString("connectionDialog.labelXMLACatalog","Catalog"));
                jLabelXMLACube.setText(I18n.getString("connectionDialog.labelXMLACube","Cube"));
                
                jTabbedPane1.setTitleAt(0,I18n.getString("connectionDialog.tab.Columns","Columns") );
                jTabbedPane1.setTitleAt(0,I18n.getString("connectionDialog.tab.Separators","Separators") );
                
                ((javax.swing.border.TitledBorder)jPanel2.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.jdbcUrlWizard","JDBC URL Wizard") );
                ((javax.swing.border.TitledBorder)jPanel8.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.ColumnNames","Column names") );
                ((javax.swing.border.TitledBorder)jPanel6.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.Other","Other") );
                ((javax.swing.border.TitledBorder)jPanel4.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.FieldSeparatorChat","Field separator (char)") );
                ((javax.swing.border.TitledBorder)jPanel5.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.RowSeparator","Row separator") );
                
                jTableCustomProperties.getColumn("Name").setHeaderValue( I18n.getString("connectionDialog.tablecolumn.Name","Name") );
                jTableCustomProperties.getColumn("Value").setHeaderValue( I18n.getString("connectionDialog.tablecolumn.Value","Value") );
                
                jButtonAddProp.setText(I18n.getString("connectionDialog.buttonAddProp","Add"));
                jButtonRemoveProp.setText(I18n.getString("connectionDialog.buttonRemoveProp","Remove"));
                
                setTitle(I18n.getString("connectionDialog.title","Connections properties"));
                jButtonCancel.setMnemonic(I18n.getString("connectionDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("connectionDialog.buttonOKMnemonic","o").charAt(0));
                jButtonOK1.setMnemonic(I18n.getString("connectionDialog.buttonOK1Mnemonic","t").charAt(0));
                I18n.getString("connectionDialog.textPane1","Press the test button.\\n\\niReport will look in the classpath for a valid hibernate configuration.");
                I18n.getString("connectionDialog.textPane2","iReport will search for persistence.xml files within the META-INF directory of any CLASSPATH element");
                I18n.getString("connectionDialog.textArea1","ATTENTION! Passwords are stored in clear text. If you dont specify a password now, iReport will ask you for one only when required and will not save it.");
                
                jTextFieldXMLTimeZoneValue.setText( I18n.getString("timezone.default","Default") );
                jTextFieldXMLLocaleValue.setText( I18n.getString("timezone.default","Default") );
                
                ((javax.swing.border.TitledBorder)jPanel3.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.borderXMLLocaleTimeZone","Locale / Time zone") );
                
                jLabelXMLLocale.setText( I18n.getString("connectionDialog.labelXMLLocale","Locale") );
                jLabelXMLTimeZone.setText( I18n.getString("connectionDialog.labelXMLTimeZone","Time zone") );
                jLabelXMLDatePattern.setText( I18n.getString("connectionDialog.labelXMLDatePattern","Date pattern") );
                jLabelXMLNumberPattern.setText( I18n.getString("connectionDialog.labelXMLNumberPattern","Number pattern") );
                jButtonXMLDatePattern.setText( I18n.getString("connectionDialog.buttonXMLDatePattern","Create...") );
                jButtonXMLNumberPattern.setText( I18n.getString("connectionDialog.buttonXMLNumberPattern","Create...") );
                jButtonXMLLocale.setText( I18n.getString("connectionDialog.buttonXMLLocale","Select...") );
                jButtonXMLTimeZone.setText( I18n.getString("connectionDialog.buttonXMLTimeZone","Select...") );
    }
    
    
    public IReportConnection createConnection(int selectedConnectionType)
    {
        IReportConnection irConn = null;
        
        if (selectedConnectionType == 0) {
            irConn = new JDBCConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JDBCConnection)irConn).setServerAddress( this.jTextFieldServerAddress.getText().trim() );
            ((JDBCConnection)irConn).setDatabase( this.jTextFieldDBName.getText().trim() );
            ((JDBCConnection)irConn).setUsername( this.jTextFieldUsername.getText().trim() );
            if (jCheckBoxSavePassword.isSelected())
                ((JDBCConnection)irConn).setPassword( this.jTextFieldPassword.getText());
            else
                ((JDBCConnection)irConn).setPassword("");
            ((JDBCConnection)irConn).setSavePassword( jCheckBoxSavePassword.isSelected() );
            ((JDBCConnection)irConn).setJDBCDriver( (this.jComboBoxJDBCDriver.getSelectedItem()+"").trim() );
            if ((this.jComboBoxJDBCDriver.getSelectedItem()+"").trim().length() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.jdbc.invalidDriver","Please insert a valid JDBC driver!"),
                        I18n.getString("messages.connectionDialog.jdbc.invalidDriverCaption","Invalid driver!"),
                        javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            if (this.jTextFieldJDBCUrl.getText().trim().length() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.jdbc.invalidUrl","Please insert a valid JDBC URL!"),
                        I18n.getString("messages.connectionDialog.jdbc.invalidUrlCaption","Invalid url!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            ((JDBCConnection)irConn).setUrl(this.jTextFieldJDBCUrl.getText().trim());
        }
        else if (selectedConnectionType == 2) {
            irConn = new JavaBeanDataSourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JavaBeanDataSourceConnection)irConn).setFactoryClass( this.jTextFieldJBSetFactoryClass.getText().trim() );
            ((JavaBeanDataSourceConnection)irConn).setMethodToCall( this.jTextFieldJBSetMethodToCall.getText().trim() );
            ((JavaBeanDataSourceConnection)irConn).setUseFieldDescription( this.jCheckBoxisUseFieldDescription.isSelected() );
            if (jRadioButtonJBSetArray.isSelected())
            {
                ((JavaBeanDataSourceConnection)irConn).setType( JavaBeanDataSourceConnection.BEAN_ARRAY );
            }
            else
            {
               ((JavaBeanDataSourceConnection)irConn).setType( JavaBeanDataSourceConnection.BEAN_COLLECTION );
            }
        }
        else if (selectedConnectionType == 3) {
            irConn = new JRCustomDataSourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRCustomDataSourceConnection)irConn).setFactoryClass( this.jTextFieldJRCustomDataSourceFactoryClass.getText().trim() );
            ((JRCustomDataSourceConnection)irConn).setMethodToCall( this.jTextFieldJRCustomDataSourceMethod.getText().trim() );
        }
        else if (selectedConnectionType == 4) {
            
            irConn = new JRCSVDataSourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRCSVDataSourceConnection)irConn).setFilename( this.jTextFieldCSVFilename.getText().trim() );
            
            if (jRadioButtonCVSSeparatorComma.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter(",");
            if (jRadioButtonCVSSeparatorTab.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter("\t");
            if (jRadioButtonCVSSeparatorSpace.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter(" ");
            if (jRadioButtonCVSSeparatorSemicolon.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter(";");
            if (jRadioButtonCVSSeparatorNewLine.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter("\n");
            if (jRadioButtonCVSSeparatorOther.isSelected()) ((JRCSVDataSourceConnection)irConn).setFieldDelimiter(jTextFieldCVSSeparatorText.getText());
            
            if (jRadioButtonCVSSeparatorComma1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter(",");
            if (jRadioButtonCVSSeparatorTab1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter("\t");
            if (jRadioButtonCVSSeparatorSpace1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter(" ");
            if (jRadioButtonCVSSeparatorSemicolon1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter(";");
            if (jRadioButtonCVSSeparatorNewLine1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter("\n");
            if (jRadioButtonCVSSeparatorOther1.isSelected()) ((JRCSVDataSourceConnection)irConn).setRecordDelimiter(jTextFieldCVSSeparatorText1.getText());
            
            ((JRCSVDataSourceConnection)irConn).setCustomDateFormat( jCheckBoxCVSDateFormat.isSelected() ?  jTextFieldCVSDateFormat.getText() : "");
            ((JRCSVDataSourceConnection)irConn).setUseFirstRowAsHeader( jCheckBoxCVSFirstRowAsHeader.isSelected() );
            
            DefaultListModel dlm = (DefaultListModel)this.jListCVSColumns.getModel();
            Vector columnsNames = new Vector();
            for (int k=0; k< dlm.size(); ++k)
            {
                columnsNames.add(dlm.elementAt(k)+"");
            }
            
            ((JRCSVDataSourceConnection)irConn).setColumnNames( columnsNames );
            
            if (columnsNames.size() == 0)
            {
                if (JOptionPane.showConfirmDialog(this,I18n.getString("messages.connectionDialog.notAllColumnsDefined","You have not defined any column for your CSV file. Continue anyway?"),"",JOptionPane.INFORMATION_MESSAGE) != JOptionPane.OK_OPTION)
                {
                    return null;
                }
            }
            if (((JRCSVDataSourceConnection)irConn).getFieldDelimiter().equals( ((JRCSVDataSourceConnection)irConn).getRecordDelimiter() ))
            {
                if (JOptionPane.showConfirmDialog(this,I18n.getString("messages.connectionDialog.duplicatedDelimiter","Field delimiter char is the same as the record delimiter. Continue anyway?"),"",JOptionPane.INFORMATION_MESSAGE) != JOptionPane.OK_OPTION)
                {
                    return null;
                } 
            }
        }
        else if (selectedConnectionType == 1) {
            irConn = new JRXMLDataSourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRXMLDataSourceConnection)irConn).setFilename( this.jTextFieldXMLFile.getText().trim() );
            ((JRXMLDataSourceConnection)irConn).setSelectExpression( this.jTextFieldRecordPath.getText().trim() );
            ((JRXMLDataSourceConnection)irConn).setUseConnection( jRadioButtonXML_connection.isSelected() );
            
            ((JRXMLDataSourceConnection)irConn).setDatePattern( jTextFieldXMLDatePattern.getText());
            ((JRXMLDataSourceConnection)irConn).setNumberPattern( jTextFieldXMLNumberPattern.getText());
            ((JRXMLDataSourceConnection)irConn).setLocale( this.tmpXMLLocale );
            ((JRXMLDataSourceConnection)irConn).setTimeZone( this.tmpXMLTimeZone );
        }
        else if (selectedConnectionType == 5) {
            irConn = new JRDataSourceProviderConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRDataSourceProviderConnection)irConn).getProperties().put("JRDataSourceProvider", this.jTextFieldJRDataSourceProvider.getText().trim() );
        }
        else if (selectedConnectionType == 6) {
            irConn = new JRHibernateConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
        }
        else if (selectedConnectionType == 7) {
            irConn = new EJBQLConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            if (jTextFieldPersistanceUnit.getText().trim().length() != 0)
            {
                irConn.getProperties().put("PersistenceUnit", jTextFieldPersistanceUnit.getText().trim());
            }
        }
        else if (selectedConnectionType == 8) {
            
            if (this.jComboBoxMondrianJdbc.getSelectedIndex() < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.setJDBCConnection","You have to choose a JDBC connection in order to configure the Mondrian OLAP connection.\nIf JDBC connection are not yet available, please create one before creating this connection."),
                        I18n.getString("messages.connectionDialog.setJDBCConnectionCaption","Invalid JDBC connection!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            if (this.jTextFieldCatalogURI.getText().trim().length() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.setCatalogUri","Please set the Catalog URI"),
                        I18n.getString("messages.connectionDialog.setCatalogUriCaption","Invalid URI!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            irConn = new MondrianConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            irConn.getProperties().put(MondrianConnection.CATALOG_URI, jTextFieldCatalogURI.getText().trim());
            irConn.getProperties().put(MondrianConnection.CONNECTION_NAME, jComboBoxMondrianJdbc.getSelectedItem()+"");
            
        }
        else if (selectedConnectionType == 9) {
            irConn = new JRSpringLoadedHibernateConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRSpringLoadedHibernateConnection)irConn).setSessionFactoryBeanId(jTextFieldSLHSessionFactory.getText());
            ((JRSpringLoadedHibernateConnection)irConn).setSpringConfig(jTextFieldSLHSpringConfig.getText());
        }
        else if (selectedConnectionType == 10) {
            irConn = new QueryExecuterConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
        }
        else if (selectedConnectionType == 11) {
            irConn = new JREmptyDatasourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JREmptyDatasourceConnection)irConn).setRecords( ((javax.swing.SpinnerNumberModel)jSpinnerNumRecords.getModel()).getNumber().intValue() );
        }
        else if (selectedConnectionType == 12) {

            irConn = new JRCustomConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            ((JRCustomConnection)irConn).setIReportConnectionClassName( jTextFieldIReportConnectionClassName.getText() );
            HashMap map = new HashMap();
            DefaultTableModel dtm = (DefaultTableModel)jTableCustomProperties.getModel();
            for (int i=0; i<dtm.getRowCount(); ++i)
            {
                String key = "" + dtm.getValueAt(i,0);
                String value = "" + dtm.getValueAt(i,1);
                map.put(key, value);
            }
            ((JRCustomConnection)irConn).setInnerConnectionProperties( map );
        }
        else if (selectedConnectionType == 13) {

            /**   
             * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
             * All Rights Reserved
             * www.cincom.com
             *
             */
            irConn = new JRXMLADataSourceConnection();
            irConn.setName( this.jTextFieldName.getText().trim() );
            
            if (this.jTextFieldXMLAUrl.getText().trim().length() == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.xmla.invalidURL","Please specify a valid server URL"),
                        I18n.getString("messages.connectionDialog.xmla.invalidProperty","Invalid property!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            ((JRXMLADataSourceConnection)irConn).setUrl( this.jTextFieldXMLAUrl.getText().trim());
            
            if (this.jComboBoxXMLADatasource.getSelectedIndex() < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.xmla.invalidDatasource","Please specify a valid Datasource"),
                        I18n.getString("messages.connectionDialog.setJDBCConnectionCaption","Invalid JDBC connection!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            ((JRXMLADataSourceConnection)irConn).setDatasource( 
                    ((String)this.jComboBoxXMLADatasource.getSelectedItem()).trim());
            
            if (this.jComboBoxXMLACatalog.getSelectedIndex() < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.xmla.invalidCatalog","Please specify a valid Catalog"),
                        I18n.getString("messages.connectionDialog.setJDBCConnectionCaption","Invalid JDBC connection!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            ((JRXMLADataSourceConnection)irConn).setCatalog( 
                    ((String)this.jComboBoxXMLACatalog.getSelectedItem()).trim());
           
            if (this.jComboBoxXMLACube.getSelectedIndex() < 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        I18n.getString("messages.connectionDialog.xmla.invalidCube","Please specify a valid Cube"),
                        I18n.getString("messages.connectionDialog.setJDBCConnectionCaption","Invalid JDBC connection!"),javax.swing.JOptionPane.WARNING_MESSAGE );
                return null;
            }
            
            ((JRXMLADataSourceConnection)irConn).setCube( 
                    ((String)this.jComboBoxXMLACube.getSelectedItem()).trim());
            
            ((JRXMLADataSourceConnection)irConn).setUsername( this.jTextFieldUsername1.getText().trim() );
            if (jCheckBoxSavePassword1.isSelected())
                ((JRXMLADataSourceConnection)irConn).setPassword( this.jTextFieldPassword1.getText());
            else
                ((JRXMLADataSourceConnection)irConn).setPassword("");
            ((JRXMLADataSourceConnection)irConn).setSavePassword( jCheckBoxSavePassword1.isSelected() );
            
            /* end of modification */
        }
                 
        return irConn;
    }
    
    
    
    /**   
     * Copyright (C) 2005, 2006 CINCOM SYSTEMS, INC.
     * All Rights Reserved
     * www.cincom.com
     *
     */
    private class jComboBoxCatListener implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCatActionPerformed(evt);
            }
    }
    jComboBoxCatListener catListener=new jComboBoxCatListener();
    
    private class jComboBoxdsListener implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasourceActionPerformed(evt);
            }
    }    
    jComboBoxdsListener dsListener=new jComboBoxdsListener();
    
    private void jComboBoxDatasourceActionPerformed(java.awt.event.ActionEvent evt) {                                                    

            jComboBoxXMLACatalog.removeActionListener(catListener);
            if (this.jComboBoxXMLADatasource.getItemCount() == 0 || this.jComboBoxXMLADatasource.getSelectedIndex() == -1) {
              return;
            }
            String urlstr = this.jTextFieldXMLAUrl.getText().trim();
            rex.metadata.ServerMetadata smd = new rex.metadata.ServerMetadata(urlstr,(Component)getParent());
            if (smd.isValidUrl() == false) {
                JOptionPane.showMessageDialog((Component)getParent(),
                        I18n.getString("messages.connectionDialog.xmla.invalidUrl","Unable to connect to XMLA server."),"",JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            this.jComboBoxXMLACatalog.removeAllItems();
            this.jComboBoxXMLACube.removeAllItems();
          
            DataSourceTreeElement dste[] = smd.discoverDataSources();
            
            if (dste == null || dste.length == 0) {
                JOptionPane.showMessageDialog((Component)getParent(),
                        I18n.getString("messages.connectionDialog.xmla.noDatasource","No Datasources found."),"",JOptionPane.INFORMATION_MESSAGE);
                return;
            } 
            //filling catalogs
            boolean found = false;
            int intI = 0;
            while (!found && intI < dste.length) {
                if (dste[intI].getDataSourceInfo().compareTo((String)this.jComboBoxXMLADatasource.getSelectedItem()) == 0){
                    found = true;
                }
                else{
                    intI++;
                }
            }
            if (!found) {
                return;
            }
            rex.graphics.datasourcetree.elements.DataSourceTreeElement catalogs[] = dste[intI].getChildren();
            if (catalogs == null || catalogs.length == 0) {
                JOptionPane.showMessageDialog((Component)getParent(),
                        I18n.getString("messages.connectionDialog.xmla.noCatalogs","No catalogs found for selected datasource."),"",JOptionPane.INFORMATION_MESSAGE);            
                return;
            }
            this.jComboBoxXMLACatalog.removeAllItems();
   
            for (intI = 0;intI<catalogs.length;intI++){
                this.jComboBoxXMLACatalog.addItem(((rex.graphics.datasourcetree.elements.CatalogElement)catalogs[intI]).toString());            
            }
        
            found = false;
             intI= 0;
            while (!found && intI < catalogs.length) {
                if (catalogs[intI].toString().trim().compareTo((String)this.jComboBoxXMLACatalog.getSelectedItem()) == 0){
                    found = true;
                }
                else{
                    intI++;
                }
            }    
            if (!found) {
             return;
            }
            rex.graphics.datasourcetree.elements.DataSourceTreeElement cubes[] = catalogs[intI].getChildren();
            if (cubes == null || cubes.length == 0) {
                return;
            }
            this.jComboBoxXMLACube.removeAllItems();
            for (intI = 0;intI<cubes.length;intI++){
                this.jComboBoxXMLACube.addItem(((rex.graphics.datasourcetree.elements.CubeElement)cubes[intI]).toString());           
            }     
            jComboBoxXMLACatalog.addActionListener(catListener);
            
        }
    
    
    private void jComboBoxCatActionPerformed(java.awt.event.ActionEvent evt) {                                             
      
        jComboBoxXMLADatasource.removeActionListener(dsListener);
         
       if (this.jComboBoxXMLACatalog.getItemCount() == 0 || this.jComboBoxXMLACatalog.getSelectedIndex() == -1 ) {
            return;
       }
        String urlstr = this.jTextFieldXMLAUrl.getText().trim();
        rex.metadata.ServerMetadata smd = new rex.metadata.ServerMetadata(urlstr,(Component)getParent());
        if (smd.isValidUrl() == false) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    I18n.getString("messages.connectionDialog.xmla.invalidUrl","Unable to connect to XMLA server."),"",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        this.jComboBoxXMLACube.removeAllItems();
        
        rex.graphics.datasourcetree.elements.DataSourceTreeElement dste[] = smd.discoverDataSources();
        if (dste == null || dste.length == 0) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    I18n.getString("messages.connectionDialog.xmla.noDatasource","No Datasources found."),"",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        boolean found = false;
        int intI = 0;
        while (!found && intI < dste.length) {
            if (dste[intI].getDataSourceInfo().compareTo((String)this.jComboBoxXMLADatasource.getSelectedItem()) == 0){
                found = true;
            } else{
                intI++;
            }
        }if (!found) {
            return;
        }
        rex.graphics.datasourcetree.elements.DataSourceTreeElement catalogs[] = dste[intI].getChildren();
        if (catalogs == null || catalogs.length == 0) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    I18n.getString("messages.connectionDialog.xmla.noCatalogs","No catalogs found for selected datasource."),"",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //add catalogs
        this.jComboBoxXMLACatalog.removeAllItems();
        for (intI = 0;intI<catalogs.length;intI++){
            this.jComboBoxXMLACatalog.addItem(((rex.graphics.datasourcetree.elements.CatalogElement)catalogs[intI]).toString());
        }
        found = false;
        intI = 0;
        while (!found && intI < catalogs.length) {
            if (catalogs[intI].toString().compareTo((String)this.jComboBoxXMLACatalog.getSelectedItem()) == 0){
                found = true;
            }
            else{
                intI++;
                found=false;
            }
        }
        if (!found) {return;
        }
        rex.graphics.datasourcetree.elements.DataSourceTreeElement cubes[] = catalogs[intI].getChildren();
        if (cubes == null || cubes.length == 0) {
            JOptionPane.showMessageDialog((Component)getParent(),
                    I18n.getString("messages.connectionDialog.xmla.noCubes","No cubes found for selected datasource."),"",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.jComboBoxXMLACube.removeAllItems();
        for (intI = 0;intI<cubes.length;intI++){
            this.jComboBoxXMLACube.addItem(((rex.graphics.datasourcetree.elements.CubeElement)cubes[intI]).toString());
        }   
        jComboBoxXMLADatasource.addActionListener(dsListener);
        
    }  
}
