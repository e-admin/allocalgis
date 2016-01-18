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
 * WizardDialog.java
 *
 * Created on 30 giugno 2003, 18.26
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.IReportTemplate;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.connection.JRCSVDataSourceConnection;
import it.businesslogic.ireport.connection.JRDataSourceProviderConnection;
import it.businesslogic.ireport.connection.JRHibernateConnection;
import it.businesslogic.ireport.connection.JRXMLADataSourceConnection;
import it.businesslogic.ireport.connection.JavaBeanDataSourceConnection;
import it.businesslogic.ireport.connection.NullConnection;
import it.businesslogic.ireport.data.CincomMDXFieldsProvider;
import it.businesslogic.ireport.data.MDXFieldsProvider;
import it.businesslogic.ireport.data.SQLFieldsProvider;
import it.businesslogic.ireport.gui.wizard.UserChoicesWizardTemplate;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;
import it.businesslogic.ireport.util.ReportGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XBoolean;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author  Administrator
 */
public class WizardDialog extends javax.swing.JDialog implements Runnable, LanguageChangedListener {
//Modified by Felix Firgau on Feb 8th 2006
    
    Thread t = null;
    Vector templates = null;
    org.syntax.jedit.JEditTextArea jRSQLExpressionArea1 = null;
    
    UserChoicesWizardTemplate userTemplate = null;
    int maxStepVisited = 0;
    
    /** Creates new form WizardDialog */
    public WizardDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        applyI18n();
        
        templates = new Vector();
        
        jRSQLExpressionArea1 = new org.syntax.jedit.JEditTextArea();
        jRSQLExpressionArea1.getPainter().setLineHighlightColor(java.awt.Color.WHITE);
        jRSQLExpressionArea1.setDocument(new org.syntax.jedit.SyntaxDocument());
        jRSQLExpressionArea1.setTokenMarker(new org.syntax.jedit.tokenmarker.TSQLTokenMarker());
        jRSQLExpressionArea1.setAutoscrolls(true);
        
        java.awt.GridBagConstraints gridBagConstraints= null;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        //gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        
        DefaultListSelectionModel dlsm = (DefaultListSelectionModel) this.jTableFields.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)
            {
                jTableFieldsListSelectionValueChanged(e);
            }
        });
        
        jPanel1.add(jRSQLExpressionArea1, gridBagConstraints);
        
        wzStep = 1;
        this.setStep(wzStep,wzStep);
        this.setSize(720, 450);
        this.setResizable(true);
        Misc.centerFrame(this);
        
        
        
        this.jRSQLExpressionArea1.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
        });
        
        this.jTextFieldBean.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRSQLExpressionArea1TextConnectionExpressionTextChanged();
            }
        });
        
        this.jRSQLExpressionArea1.requestFocusInWindow();
        jList1.setModel(new javax.swing.DefaultListModel());
        jList2.setModel(new javax.swing.DefaultListModel());
        jList3.setModel(new javax.swing.DefaultListModel());
        
        String templates_dir = MainFrame.IREPORT_HOME_DIR;  //System.getProperty("ireport.home",".");
        templates_dir +=  File.separator + "templates";
        //System.out.println("Templates: " + templates_dir);
        //C:\\documenti\\progetti\\ireport\\iReport2\\templates
        try {
            File f = new File(templates_dir);
            File[] templates_files = null;
            if (f.exists() && f.isDirectory()) {
                templates_files  = f.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        if (name.endsWith("T.xml") || name.endsWith("C.xml")) {
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                templates_dir = MainFrame.IREPORT_HOME_DIR;  //System.getProperty("ireport.home",".");
                templates_dir +=  File.separator + "etc" + File.separator + "templates";
                f = new File(templates_dir);
                if (f.exists() && f.isDirectory()) {
                    templates_files  = f.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            if (name.endsWith("T.xml") || name.endsWith("C.xml")) {
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
            
            for (int i=0;templates_files != null && i<templates_files.length; ++i) {
                IReportTemplate itemplate = new IReportTemplate();
                itemplate.setXmlFile(templates_files[i] +"");
                itemplate.setName(templates_files[i].getName());
                itemplate.setType( templates_files[i].getName().toLowerCase().endsWith("c.xml") ? 0 : 1);
                try {
                    String iconname = templates_files[i]+"";
                    iconname = Misc.changeFileExtension(iconname,".gif");
                    itemplate.setIcon( new javax.swing.ImageIcon( iconname) );
                } catch (Exception ex){}
                
                templates.add( itemplate );
            }
            
        } catch (Exception ex) {
        }
        
        
        // try to look in the classpath...
        try {
            Vector xml_template_files = new Vector();
            Enumeration enum_pl = this.getClass().getClassLoader().getResources("ireport/template.xml");
            while (enum_pl.hasMoreElements()) {
                Object oobj = enum_pl.nextElement();
                xml_template_files.add(oobj);
            }
            
            for (int i=0; i<xml_template_files.size(); ++i) {
                
                Object source = xml_template_files.elementAt(i);
                //  Create a Xerces DOM Parser
                DOMParser parser = new DOMParser();
                //  Parse the Document
                //  and traverse the DOM
                try {
                    
                    parser.setEntityResolver( new org.xml.sax.EntityResolver() {
                        /* Code by Teodor Danciu */
                        public org.xml.sax.InputSource resolveEntity(
                                String publicId,
                                String systemId
                                ) throws SAXException//, java.io.IOException
                        {
                            org.xml.sax.InputSource inputSource = null;
                            
                            if (systemId != null) {
                                String dtd = null;
                                
                                if ( systemId.equals("http://ireport.sourceforge.net/dtds/iReportTemplate.dtd") ) {
                                    dtd = "it/businesslogic/ireport/dtds/iReportTemplate.dtd";
                                } else {
                                    return new org.xml.sax.InputSource(systemId);
                                }
                                
                                
                                ClassLoader classLoader = this.getClass().getClassLoader();
                                
                                java.io.InputStream is = classLoader.getResourceAsStream(dtd);
                                if (is != null) {
                                    inputSource = new org.xml.sax.InputSource(is);
                                }
                                
                            }
                            
                            return inputSource;
                        }
                    });
                    /* End Code by Teodor Danciu */
                    InputStream input_source = null;
                    if ( source instanceof java.io.File ) {
                        input_source = new FileInputStream((java.io.File)source);
                        
                    } else if ( source instanceof java.net.URL){
                        
                        input_source = ((java.net.URL)source).openStream();
                        
                    }
                    
                    parser.parse(new org.xml.sax.InputSource( input_source ));
                    Document document = parser.getDocument();
                    
                    //System.out.println("traverse");
                    Node node = document.getDocumentElement();
                    
                    NodeList list = XPathAPI.selectNodeList(node, "/iReportTemplateSet/iReportTemplate");
                    Node childnode = null;
                    
                    for (int n=0; n < list.getLength(); n++) {
                        IReportTemplate ireportTemplate = new IReportTemplate();
                        childnode = XPathAPI.selectSingleNode(list.item(n), "@name");
                        if (childnode != null) ireportTemplate.setName(childnode.getNodeValue());
                        if (XPathAPI.eval(list.item(n), "@type = 'Tabular'").equals(XBoolean.S_TRUE)) {
                            ireportTemplate.setType(IReportTemplate.TABULAR);
                        } else {
                            ireportTemplate.setType(IReportTemplate.COLUMNAR);
                        }
                        childnode = XPathAPI.selectSingleNode(list.item(n), "XmlFile/text()");
                        if (childnode != null) ireportTemplate.setXmlFile(childnode.getNodeValue());
                        childnode = XPathAPI.selectSingleNode(list.item(n), "IconFile/text()");
                        if (childnode != null) {
                            try {
                                ireportTemplate.setIcon(
                                        new ImageIcon(getClass().getClassLoader().getResource(childnode.getNodeValue())));
                            } catch (DOMException e) {
                                // Don't care
                                e.printStackTrace();
                            }
                        }
                        
                        templates.add(ireportTemplate);
                    }
                } catch (SAXException e) {
                    System.err.println(e);
                } catch (java.io.IOException e) {
                    System.err.println(e);
                }
            } // End cycle on iReport plugin files...
            
        } catch (Exception ex) {
            MainFrame.getMainInstance().logOnConsole("Error searching ireport/template.xml resources\n");
        }
        
        updateTemplatesList();
        
        jComboBoxTemplates.removeAllItems();
        
        jComboBoxTemplates.addItem(I18n.getString("noTemplate","None"));
        java.util.List ucTemplates = MainFrame.getMainInstance().getUserChoicesWizardTemplates();
        
        java.util.List tnames= new java.util.ArrayList();
        
        for (int i=0; i<ucTemplates.size(); ++i)
        {
            jComboBoxTemplates.addItem(ucTemplates.get(i));
            tnames.add( ""+ucTemplates.get(i) );
        }
        
        for (int i=1; i<1000 ;i++)
        {
            String s = I18n.getFormattedString("MyTemplate","My template {0}", new Object[]{new Integer(i)});
            if (!tnames.contains(s))
            {
                jTextFieldTemplateName.setText(s);
                break;
            }
        }
        //jRSQLExpressionArea1.setText("00000000000000000000000000000000000000000000000000000\n\r");
        

        //Added by Felix Firgau for I18n on Feb 8th 2006
        //it.businesslogic.ireport.util.I18n.setCurrentLocale( System.getProperty("Language"), System.getProperty("Country") );
        //I18n.addOnLanguageChangedListener( this );
        //this.pack();
        updateConnections();
        
    }
    
    public void updateConnections() {
        Object ircDefault =  MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        jComboBoxConnection.removeAllItems();
        
        Enumeration e = MainFrame.getMainInstance().getConnections().elements();
        
        jComboBoxConnection.addItem(new NullConnection());
        while (e.hasMoreElements()) {
            IReportConnection irc = (IReportConnection)e.nextElement();
            jComboBoxConnection.addItem(irc);
        }
        
        if (ircDefault != null) {
            jComboBoxConnection.setSelectedItem(ircDefault);
        }
    }
    
    private int wzStep=0;
    
    
    public void updateTemplatesList() {
        if (templates == null) return;
        ((javax.swing.DefaultListModel)jList3.getModel()).removeAllElements();
        
        for (int i=0;  i< templates.size(); ++i) {
            IReportTemplate itemplate = (IReportTemplate)templates.elementAt(i);
            
            if (jRadioButtonTabularLayout.isSelected() && itemplate.getType() != IReportTemplate.TABULAR) continue;
            if (jRadioButtonColumnarLayout.isSelected() && itemplate.getType() != IReportTemplate.COLUMNAR) continue;
            //if (jComboBoxTemplates.getItemCount()<2 || (jComboBoxTemplates.getSelectedIndex() == 0 && itemplate.getType() != IReportTemplate.COLUMNAR) )  continue;
            //if (jComboBoxTemplates.getSelectedIndex() == 1 && itemplate.getType() != IReportTemplate.TABULAR)  continue;
            /*
            TemplateFile tf = new TemplateFile();
            tf.file = templates[i];
             
            tf.name = templates[i].getName().substring(0, templates[i].getName().length()-4);
             */
            ((javax.swing.DefaultListModel)jList3.getModel()).addElement(itemplate);
        }
        if ( ((javax.swing.DefaultListModel)jList3.getModel()).getSize()>0)
            jList3.setSelectedIndex(0);
        jList3.updateUI();
    }
    
    public void jRSQLExpressionArea1TextConnectionExpressionTextChanged() {
        jComboBoxConnectionActionPerformed(null);
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanelSteps = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelStep1 = new javax.swing.JLabel();
        jLabelStep2 = new javax.swing.JLabel();
        jLabelStep3 = new javax.swing.JLabel();
        jLabelStep4 = new javax.swing.JLabel();
        jLabelStep5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanelStepCaption = new javax.swing.JPanel();
        jLabelStepCaption = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jPanelRoot = new javax.swing.JPanel();
        jPanelStep1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jComboBoxTemplates = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxConnection = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButtonNewConnection = new javax.swing.JButton();
        jTextFieldBean = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButtonDesignQuery = new javax.swing.JButton();
        jButtonLoadQuery = new javax.swing.JButton();
        jButtonSaveQuery = new javax.swing.JButton();
        jPanelStep2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanelStep3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableFields = new javax.swing.JTable();
        jButtonModifyField = new javax.swing.JButton();
        jPanelStep4 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabelG1 = new javax.swing.JLabel();
        jLabelG2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabelG3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabelG4 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanelStep5 = new javax.swing.JPanel();
        jRadioButtonColumnarLayout = new javax.swing.JRadioButton();
        jRadioButtonTabularLayout = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jLabelImage = new javax.swing.JLabel();
        jCheckBoxSetHeader = new javax.swing.JCheckBox();
        jPanelStep6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jCheckBoxSaveTemplate = new javax.swing.JCheckBox();
        jTextFieldTemplateName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("iReport Wizard");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanelSteps.setBackground(new java.awt.Color(165, 172, 181));
        jPanelSteps.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel1.setText("Steps");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 0);
        jPanelSteps.add(jLabel1, gridBagConstraints);

        jSeparator2.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setMaximumSize(new java.awt.Dimension(32767, 1));
        jSeparator2.setMinimumSize(new java.awt.Dimension(98, 1));
        jSeparator2.setPreferredSize(new java.awt.Dimension(1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelSteps.add(jSeparator2, gridBagConstraints);

        jLabelStep1.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabelStep1.setText("1. Query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 63;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        jPanelSteps.add(jLabelStep1, gridBagConstraints);

        jLabelStep2.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabelStep2.setText("2. Fields selection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelSteps.add(jLabelStep2, gridBagConstraints);

        jLabelStep3.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabelStep3.setText("3. Group by ...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 38;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelSteps.add(jLabelStep3, gridBagConstraints);

        jLabelStep4.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabelStep4.setText("4. Layout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 64;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelSteps.add(jLabelStep4, gridBagConstraints);

        jLabelStep5.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabelStep5.setText("5. Finish");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelSteps.add(jLabelStep5, gridBagConstraints);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/wizard.jpg"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        jPanelSteps.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanelSteps, gridBagConstraints);

        jPanelStepCaption.setLayout(new java.awt.GridBagLayout());

        jLabelStepCaption.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabelStepCaption.setText("Step 4: choose layout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 8, 0, 0);
        jPanelStepCaption.add(jLabelStepCaption, gridBagConstraints);

        jSeparator8.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator8.setMaximumSize(new java.awt.Dimension(32767, 1));
        jSeparator8.setMinimumSize(new java.awt.Dimension(98, 1));
        jSeparator8.setPreferredSize(new java.awt.Dimension(10, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelStepCaption.add(jSeparator8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanelStepCaption, gridBagConstraints);

        jPanelRoot.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanelRoot.setLayout(new java.awt.BorderLayout());

        jPanelStep1.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText("Use the following template...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(74, 0, 0, 0);
        jPanelStep1.add(jLabel13, gridBagConstraints);

        jComboBoxTemplates.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxTemplates.setMinimumSize(new java.awt.Dimension(51, 22));
        jComboBoxTemplates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConnectionActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelStep1.add(jComboBoxTemplates, gridBagConstraints);

        jLabel12.setText("Connection / Datasource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanelStep1.add(jLabel12, gridBagConstraints);

        jComboBoxConnection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxConnection.setMinimumSize(new java.awt.Dimension(51, 22));
        jComboBoxConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelStep1.add(jComboBoxConnection, gridBagConstraints);

        jLabel2.setText("Query string");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 240;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 0, 0);
        jPanelStep1.add(jLabel2, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1000.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelStep1.add(jPanel1, gridBagConstraints);

        jButtonNewConnection.setText("New");
        jButtonNewConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        jPanelStep1.add(jButtonNewConnection, gridBagConstraints);

        jTextFieldBean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBeanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanelStep1.add(jTextFieldBean, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButtonDesignQuery.setText("Design query");
        jButtonDesignQuery.setMaximumSize(new java.awt.Dimension(95, 25));
        jButtonDesignQuery.setMinimumSize(new java.awt.Dimension(95, 25));
        jButtonDesignQuery.setPreferredSize(new java.awt.Dimension(95, 25));
        jButtonDesignQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDesignQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jButtonDesignQuery, gridBagConstraints);

        jButtonLoadQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/folder_database.png"))); // NOI18N
        jButtonLoadQuery.setText("Load query");
        jButtonLoadQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonLoadQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadQueryActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel3.add(jButtonLoadQuery, gridBagConstraints);

        jButtonSaveQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/database_save.png"))); // NOI18N
        jButtonSaveQuery.setText("Save query");
        jButtonSaveQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonSaveQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveQueryActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanel3.add(jButtonSaveQuery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanelStep1.add(jPanel3, gridBagConstraints);

        jPanelRoot.add(jPanelStep1, java.awt.BorderLayout.CENTER);

        jPanelStep2.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(50, 130));
        jScrollPane2.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelStep2.add(jScrollPane2, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(50, 130));
        jScrollPane1.setViewportView(jList2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelStep2.add(jScrollPane1, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14));
        jButton4.setText(">");
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jButton4, gridBagConstraints);

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14));
        jButton5.setText(">>");
        jButton5.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jButton5, gridBagConstraints);

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 14));
        jButton6.setText("<");
        jButton6.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jButton6, gridBagConstraints);

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14));
        jButton7.setText("<<");
        jButton7.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel5.add(jButton7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelStep2.add(jPanel5, gridBagConstraints);

        jPanelRoot.add(jPanelStep2, java.awt.BorderLayout.CENTER);

        jPanelStep3.setLayout(new java.awt.GridBagLayout());

        jTableFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Field name", "Classe"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFieldsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableFields);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelStep3.add(jScrollPane4, gridBagConstraints);

        jButtonModifyField.setText("Modify");
        jButtonModifyField.setEnabled(false);
        jButtonModifyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelStep3.add(jButtonModifyField, gridBagConstraints);

        jPanelRoot.add(jPanelStep3, java.awt.BorderLayout.NORTH);

        jPanelStep4.setLayout(new java.awt.GridBagLayout());

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelStep4.add(jComboBox1, gridBagConstraints);

        jLabelG1.setText("Group 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 4, 0, 0);
        jPanelStep4.add(jLabelG1, gridBagConstraints);

        jLabelG2.setText("Group 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanelStep4.add(jLabelG2, gridBagConstraints);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelStep4.add(jComboBox2, gridBagConstraints);

        jLabelG3.setText("Group 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanelStep4.add(jLabelG3, gridBagConstraints);

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelStep4.add(jComboBox3, gridBagConstraints);

        jLabelG4.setText("Group 4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanelStep4.add(jLabelG4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelStep4.add(jComboBox4, gridBagConstraints);

        jPanelRoot.add(jPanelStep4, java.awt.BorderLayout.CENTER);

        jPanelStep5.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jRadioButtonColumnarLayout);
        jRadioButtonColumnarLayout.setSelected(true);
        jRadioButtonColumnarLayout.setText("Columnar layout");
        jRadioButtonColumnarLayout.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonColumnarLayout.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonColumnarLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonColumnarLayoutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(70, 10, 0, 0);
        jPanelStep5.add(jRadioButtonColumnarLayout, gridBagConstraints);

        buttonGroup1.add(jRadioButtonTabularLayout);
        jRadioButtonTabularLayout.setText("Tabular layout");
        jRadioButtonTabularLayout.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonTabularLayout.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonTabularLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonTabularLayoutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        jPanelStep5.add(jRadioButtonTabularLayout, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(0, 0));

        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList3ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 8, 0, 0);
        jPanelStep5.add(jScrollPane3, gridBagConstraints);

        jLabelImage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(13, 8, 0, 30);
        jPanelStep5.add(jLabelImage, gridBagConstraints);

        jCheckBoxSetHeader.setSelected(true);
        jCheckBoxSetHeader.setText("jCheckBox1");
        jCheckBoxSetHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxSetHeader.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 5, 0);
        jPanelStep5.add(jCheckBoxSetHeader, gridBagConstraints);

        jPanelRoot.add(jPanelStep5, java.awt.BorderLayout.CENTER);

        jPanelStep6.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel4.setText("Congratulation!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(40, 80, 4, 4);
        jPanelStep6.add(jLabel4, gridBagConstraints);

        jLabel10.setText("You have succesfully created a new report.");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 0, 4);
        jPanelStep6.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Press 'Finish' to generate it.");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 80, 4, 4);
        jPanelStep6.add(jLabel11, gridBagConstraints);

        jCheckBoxSaveTemplate.setText("Save choices as template...");
        jCheckBoxSaveTemplate.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxSaveTemplate.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxSaveTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSaveTemplateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(40, 80, 0, 4);
        jPanelStep6.add(jCheckBoxSaveTemplate, gridBagConstraints);

        jTextFieldTemplateName.setText("My Template 1");
        jTextFieldTemplateName.setEnabled(false);
        jTextFieldTemplateName.setMinimumSize(new java.awt.Dimension(250, 19));
        jTextFieldTemplateName.setPreferredSize(new java.awt.Dimension(250, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 100, 0, 0);
        jPanelStep6.add(jTextFieldTemplateName, gridBagConstraints);

        jPanelRoot.add(jPanelStep6, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.ipady = 277;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 5);
        jPanel2.add(jPanelRoot, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jSeparator1, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButtonCancel.setMnemonic('f');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel4.add(jButtonCancel, gridBagConstraints);

        jButtonNext.setMnemonic('n');
        jButtonNext.setText("Next >");
        jButtonNext.setEnabled(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel4.add(jButtonNext, gridBagConstraints);

        jButtonPrev.setMnemonic('p');
        jButtonPrev.setText("< Prev");
        jButtonPrev.setEnabled(false);
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel4.add(jButtonPrev, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxConnectionActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConnectionActionPerformed1

        if (jComboBoxTemplates.getSelectedIndex() <= 0)
        {
            userTemplate = null;
        }
        else
        {
            userTemplate = (UserChoicesWizardTemplate)jComboBoxTemplates.getSelectedItem();
            this.jRSQLExpressionArea1.setText( userTemplate.getQuery() );
            
            if (userTemplate.getIRDatasourceName() != null && userTemplate.getIRDatasourceName().length() >0)
            {
                for (int i=0; i<jComboBoxConnection.getItemCount(); ++i)
                {
                    IReportConnection c = (IReportConnection)jComboBoxConnection.getItemAt(i);
                    if (c.getName().equals(userTemplate.getIRDatasourceName()  ))
                    {
                        jComboBoxConnection.setSelectedIndex(i);
                    }
                }
            }
            this.jRSQLExpressionArea1.setText( userTemplate.getQuery() );
        }
        
    }//GEN-LAST:event_jComboBoxConnectionActionPerformed1

    private void jCheckBoxSaveTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSaveTemplateActionPerformed

        jTextFieldTemplateName.setEnabled( jCheckBoxSaveTemplate.isSelected() );
        
    }//GEN-LAST:event_jCheckBoxSaveTemplateActionPerformed

    private void jButtonModifyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyFieldActionPerformed

        if (jTableFields.getSelectedRow() >=0)
        {
            int row = jTableFields.getSelectedRow();
            JRField field = (JRField)jTableFields.getValueAt(row,0);
            java.awt.Frame parent = Misc.frameFromComponent(this);
            JRFieldDialog jrpd = new JRFieldDialog(parent, true);
            jrpd.setField(field);
            jrpd.setVisible(true);

            if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                field.setName(jrpd.getField().getName());
                field.setClassType(jrpd.getField().getClassType());
                field.setProperties(jrpd.getField().getProperties());
                
                jTableFields.setValueAt(field.getClassType(), row,1);
                jTableFields.updateUI();
            }
        }
        
    }//GEN-LAST:event_jButtonModifyFieldActionPerformed

    
    private void jTableFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFieldsMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 &&
                jTableFields.getSelectedRow() >= 0) {
                jButtonModifyFieldActionPerformed( null );
        }
        
        //if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3) {
        //    jPopupMenuFields.show(jTableFields, evt.getPoint().x,
        //            evt.getPoint().y);
        //}
        
    }//GEN-LAST:event_jTableFieldsMouseClicked
    
    private void jButtonDesignQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDesignQueryActionPerformed
        
        
        IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
        
        String query = jRSQLExpressionArea1.getText();
        
        try {
            if (irc.isJDBCConnection()) {
                SQLFieldsProvider sfp = new SQLFieldsProvider();
                query = sfp.designQuery(irc, query, null);
            } else if (irc instanceof JRXMLADataSourceConnection) {
                
                CincomMDXFieldsProvider mfp = new CincomMDXFieldsProvider();
                query = mfp.designQuery(irc, query, null);
            } else {
                return;
            }
            
            if (query != null && query.length() > 0) {
                jRSQLExpressionArea1.setText(query);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }//GEN-LAST:event_jButtonDesignQueryActionPerformed
    
    private void jButtonSaveQueryActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveQueryActionPerformed1
        Misc.saveSQLQuery( jRSQLExpressionArea1.getText(), this );
    }//GEN-LAST:event_jButtonSaveQueryActionPerformed1
    
    private void jButtonLoadQueryActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadQueryActionPerformed1
        String query = Misc.loadSQLQuery(this);
        
        if (query != null) {
            jRSQLExpressionArea1.setText(query);
        }
    }//GEN-LAST:event_jButtonLoadQueryActionPerformed1
    
    private void jTextFieldBeanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBeanActionPerformed
        jComboBoxConnectionActionPerformed(null);
    }//GEN-LAST:event_jTextFieldBeanActionPerformed
    
    private void jButtonNewConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewConnectionActionPerformed
        
        
        it.businesslogic.ireport.connection.gui.ConnectionDialog cd = new it.businesslogic.ireport.connection.gui.ConnectionDialog(this,true);
        
        cd.setVisible(true);
        
        IReportConnection con = null;
        if (cd.getDialogResult() == JOptionPane.OK_OPTION) {
            con = cd.getIReportConnection();
            MainFrame.getMainInstance().getConnections().addElement(con);
            
            if (MainFrame.getMainInstance().getConnections().size() == 1) {
                MainFrame.getMainInstance().setActiveConnection(con);
            } else {
                MainFrame.getMainInstance().saveiReportConfiguration();
            }
            
            this.updateConnections();
            this.jComboBoxConnection.setSelectedItem(con);
        }
        
        
        
    }//GEN-LAST:event_jButtonNewConnectionActionPerformed
    
    private void jComboBoxConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConnectionActionPerformed
        
        boolean canNext = false;
        if (jComboBoxConnection.getSelectedItem() != null) {
            IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
            
            jButtonDesignQuery.setVisible(false);
            if (irc.isJDBCConnection()) {
                //jLabel2.setText("SQL query");
                jLabel2.setText(it.businesslogic.ireport.util.I18n.getString("sqlQuery","SQL query"));
                jPanel1.setVisible(true);
                jButtonLoadQuery.setVisible(true);
                jButtonSaveQuery.setVisible(true);
                jButtonDesignQuery.setVisible( SQLFieldsProvider.useVisualDesigner  );
                jTextFieldBean.setVisible(false);
                jButtonDesignQuery.setEnabled(true);
                
                if (jRSQLExpressionArea1.getText().length() > 0) canNext = true;
            } else if (irc instanceof JRHibernateConnection) {
                //jLabel2.setText("HQL query");
                jLabel2.setText(it.businesslogic.ireport.util.I18n.getString("hqlQuery","HQL query"));
                jPanel1.setVisible(true);
                jButtonLoadQuery.setVisible(true);
                jButtonSaveQuery.setVisible(true);
                jTextFieldBean.setVisible(false);
                if (jRSQLExpressionArea1.getText().length() > 0) canNext = true;
            } else if (irc instanceof JRXMLADataSourceConnection) {
                //jLabel2.setText("HQL query");
                jLabel2.setText(it.businesslogic.ireport.util.I18n.getString("mdxQuery","MDX query"));
                jPanel1.setVisible(true);
                jButtonLoadQuery.setVisible(true);
                jButtonSaveQuery.setVisible(true);
                jTextFieldBean.setVisible(false);
                // Activate the designer button...
                jButtonDesignQuery.setVisible( MDXFieldsProvider.useVisualDesigner  );
                if (jRSQLExpressionArea1.getText().length() > 0) canNext = true;
                
            } else if (irc instanceof it.businesslogic.ireport.connection.JRDataSourceProviderConnection) {
                jLabel2.setText("");
                jPanel1.setVisible(false);
                jButtonLoadQuery.setVisible(false);
                jButtonSaveQuery.setVisible(false);
                jTextFieldBean.setVisible(false);
                canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.JavaBeanDataSourceConnection) {
                jLabel2.setText("JavaBean class");
                jPanel1.setVisible(false);
                jButtonLoadQuery.setVisible(false);
                jButtonSaveQuery.setVisible(false);
                jTextFieldBean.setVisible(true);
                if (jTextFieldBean.getText().length() > 0) canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.JRCSVDataSourceConnection) {
                jLabel2.setText("");
                jPanel1.setVisible(false);
                jButtonLoadQuery.setVisible(false);
                jButtonSaveQuery.setVisible(false);
                jTextFieldBean.setVisible(false);
                canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.NullConnection) {
                jLabel2.setText("");
                jPanel1.setVisible(false);
                jButtonLoadQuery.setVisible(false);
                jButtonSaveQuery.setVisible(false);
                jTextFieldBean.setVisible(false);
                canNext = true;
            } else {
                jLabel2.setText("You can't use the wizard with this kind of datasource");
                jPanel1.setVisible(false);
                jButtonLoadQuery.setVisible(false);
                jButtonSaveQuery.setVisible(false);
                jTextFieldBean.setVisible(false);
                canNext = false;
            }
            
        } else {
            jLabel2.setText("Please create a new connection / datasource.");
            jPanel1.setVisible(false);
            jButtonLoadQuery.setVisible(false);
            jButtonSaveQuery.setVisible(false);
            jTextFieldBean.setVisible(false);
        }
        
        jButtonNext.setEnabled(canNext);
        
    }//GEN-LAST:event_jComboBoxConnectionActionPerformed
    
    
    private void jRadioButtonColumnarLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonColumnarLayoutActionPerformed
        updateTemplatesList();
    }//GEN-LAST:event_jRadioButtonColumnarLayoutActionPerformed
    
    private void jRadioButtonTabularLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonTabularLayoutActionPerformed
        updateTemplatesList();
    }//GEN-LAST:event_jRadioButtonTabularLayoutActionPerformed
    
    private void jList3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList3ValueChanged
        
        if (jList3.getSelectedIndex() >= 0) {
            IReportTemplate tf = (IReportTemplate)jList3.getSelectedValue();
            // Take the image...
            
            this.jLabelImage.setIcon( tf.getIcon() );
            
            this.jLabelImage.updateUI();
            this.jButtonNext.setEnabled(true);
        } else {
            this.jLabelImage.setIcon( null );
            this.jButtonNext.setEnabled(false);
        }
    }//GEN-LAST:event_jList3ValueChanged
    
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // Add to combobox 2 all not selected items...
        Object obj1 = jComboBox1.getSelectedItem();
        Object obj2 = jComboBox2.getSelectedItem();
        Object obj3 = jComboBox3.getSelectedItem();
        if (jComboBox3.getSelectedIndex() <= 0) {
            disableGroups( 4 );
        } else {
            jComboBox4.setEnabled(true);
            jLabelG4.setEnabled(true);
            javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jList2.getModel();
            jComboBox4.removeAllItems();
            jComboBox4.addItem("");
            for (int i=0; i<dlm.getSize(); ++i) {
                Object obj = dlm.getElementAt(i);
                if (obj != obj1 && obj != obj2 && obj != obj3)
                    jComboBox4.addItem( obj );
            }
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed
    
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // Add to combobox 2 all not selected items...
        Object obj1 = jComboBox1.getSelectedItem();
        Object obj2 = jComboBox2.getSelectedItem();
        if (jComboBox2.getSelectedIndex() <= 0) {
            disableGroups( 3 );
        } else {
            disableGroups( 4 );
            jComboBox3.setEnabled(true);
            jLabelG3.setEnabled(true);
            javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jList2.getModel();
            jComboBox3.removeAllItems();
            jComboBox3.addItem("");
            for (int i=0; i<dlm.getSize(); ++i) {
                Object obj = dlm.getElementAt(i);
                if (obj != obj1 && obj != obj2)
                    jComboBox3.addItem( obj );
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed
    
    public void disableGroups(int fromIndex) {
        switch (fromIndex) {
            case 2:
                jComboBox2.removeAllItems();
                jComboBox2.setEnabled(false);
                jLabelG2.setEnabled(false);
            case 3:
                jComboBox3.removeAllItems();
                jComboBox3.setEnabled(false);
                jLabelG3.setEnabled(false);
            case 4:
                jComboBox4.removeAllItems();
                jComboBox4.setEnabled(false);
                jLabelG4.setEnabled(false);
        }
    }
    
    
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        
        // Add to combobox 2 all not selected items...
        Object obj1 = jComboBox1.getSelectedItem();
        if (jComboBox1.getSelectedIndex() <= 0) {
            disableGroups( 2 );
        } else {
            disableGroups( 3 );
            jComboBox2.setEnabled(true);
            jLabelG2.setEnabled(true);
            javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jList2.getModel();
            jComboBox2.removeAllItems();
            jComboBox2.addItem("");
            for (int i=0; i<dlm.getSize(); ++i) {
                Object obj = dlm.getElementAt(i);
                if (obj != obj1)
                    jComboBox2.addItem( obj );
            }
        }
        
        
    }//GEN-LAST:event_jComboBox1ActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        
        javax.swing.DefaultListModel list1 = (javax.swing.DefaultListModel)jList1.getModel();
        javax.swing.DefaultListModel list2 = (javax.swing.DefaultListModel)jList2.getModel();
        
        for (int i=0; i< list1.getSize(); ++i) {
            list2.addElement(list1.getElementAt(i));
        }
        list1.removeAllElements();
        if (  ((javax.swing.DefaultListModel)jList2.getModel()).getSize() == 0 )
            jButtonNext.setEnabled(false);
        else
            jButtonNext.setEnabled(true);
    }//GEN-LAST:event_jButton5ActionPerformed
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int index = jList2.getSelectedIndex();
        if (index <0) return;
        
        Object[] objs = jList2.getSelectedValues();
        for (int i=0; i< objs.length; ++i) {
            Object obj = objs[i];
            //Object obj = jList2.getSelectedValue();
            ((javax.swing.DefaultListModel)jList1.getModel()).addElement(obj);
            jButtonNext.setEnabled(true);
            ((javax.swing.DefaultListModel)jList2.getModel()).removeElement(obj);
            /*
            if ( index < ((javax.swing.DefaultListModel)jList2.getModel()).getSize() )
                jList2.setSelectedIndex(index);
            else if (index-1 >= 0)
            {
                jList2.setSelectedIndex(index-1);
            }
             */
            if (  ((javax.swing.DefaultListModel)jList2.getModel()).getSize() == 0 )
                jButtonNext.setEnabled(false);
            else
                jButtonNext.setEnabled(true);
        }
    }//GEN-LAST:event_jButton6ActionPerformed
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        int index = jList1.getSelectedIndex();
        if (index <0) return;
        
        Object[] objs = jList1.getSelectedValues();
        for (int i=0; i< objs.length; ++i) {
            Object obj = objs[i];
            ((javax.swing.DefaultListModel)jList2.getModel()).addElement(obj);
            jButtonNext.setEnabled(true);
            ((javax.swing.DefaultListModel)jList1.getModel()).removeElement(obj);
            /*
            if ( index < ((javax.swing.DefaultListModel)jList1.getModel()).getSize() )
                jList1.setSelectedIndex(index);
            else if (index-1 >= 0)
            {
                jList1.setSelectedIndex(index-1);
            }
             */
            if (  ((javax.swing.DefaultListModel)jList2.getModel()).getSize() == 0 )
                jButtonNext.setEnabled(false);
            else
                jButtonNext.setEnabled(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
        
        if (wzStep == 6) {
            //javax.swing.JOptionPane.showMessageDialog(null, ((TemplateFile)jList3.getSelectedValue()).file.getAbsolutePath()+"" );
            String templateName = "";
            if (jCheckBoxSaveTemplate.isSelected() && jTextFieldTemplateName.getText().length() == 0)
            {
                javax.swing.JOptionPane.showMessageDialog(getParent(),
                        I18n.getString("message.invalidTemplateName","Invalid template name!"),
                        I18n.getString("message.title.error","Error"), javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                Report report = null;
                
                UserChoicesWizardTemplate ucwt = new UserChoicesWizardTemplate();
                ucwt.setName( jTextFieldTemplateName.getText().trim() );
                ucwt.setQuery( jRSQLExpressionArea1.getText());
                ucwt.setQuery_language( getQueryLanguage() );
                ucwt.setTemplateFile( ((IReportTemplate)jList3.getSelectedValue()).getXmlFile() );

                ucwt.setSaveFieldDescriptions( jCheckBoxSetHeader.isSelected() );

                for (int i=0; i<jList2.getModel().getSize(); ++i) {
                    // FIELD
                    JRField f = (JRField)jList2.getModel().getElementAt(i);
                    ucwt.getDisplayFields().add(f);
                }

                if (jComboBoxConnection.getSelectedItem() != null)
                {
                    ucwt.setIRDatasourceName("" + jComboBoxConnection.getSelectedItem());
                }

                if (jComboBox1.getSelectedIndex() > 0)
                {
                    ucwt.getGroupExpressions().add("" + jComboBox1.getSelectedItem());
                }
                if (jComboBox2.getSelectedIndex() > 0)
                {
                    ucwt.getGroupExpressions().add("" + jComboBox2.getSelectedItem());
                }
                if (jComboBox3.getSelectedIndex() > 0)
                {
                    ucwt.getGroupExpressions().add("" + jComboBox3.getSelectedItem());
                }
                if (jComboBox4.getSelectedIndex() > 0)
                {
                    ucwt.getGroupExpressions().add("" + jComboBox4.getSelectedItem());
                }
                
                report =  ReportGenerator.createReport(ucwt);
                
                if (jCheckBoxSaveTemplate.isSelected())
                {
                    MainFrame.getMainInstance().getUserChoicesWizardTemplates().add(ucwt);
                    UserChoicesWizardTemplate.storeWizardTemplates( MainFrame.getMainInstance().getUserChoicesWizardTemplates() );
                }
                
                this.setReport(report);
                MainFrame.getMainInstance().setActiveConnection( (IReportConnection)jComboBoxConnection.getSelectedItem());
                this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
            } catch (Exception ex) {
                java.io.StringWriter sw = new java.io.StringWriter();
                ex.printStackTrace(new java.io.PrintWriter( sw ));
                javax.swing.JOptionPane.showMessageDialog(getParent(),sw.getBuffer()+"",
                        I18n.getString("message.title.error","Error"), javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        } else
            this.setDialogResult(javax.swing.JOptionPane.CANCEL_OPTION);
        
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        this.nextStep();
    }//GEN-LAST:event_jButtonNextActionPerformed
    
    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        this.prevStep();
    }//GEN-LAST:event_jButtonPrevActionPerformed
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        javax.swing.DefaultListModel list2 = (javax.swing.DefaultListModel)jList1.getModel();
        javax.swing.DefaultListModel list1 = (javax.swing.DefaultListModel)jList2.getModel();
        
        for (int i=0; i< list1.getSize(); ++i) {
            list2.addElement(list1.getElementAt(i));
        }
        list1.removeAllElements();
        if (  ((javax.swing.DefaultListModel)jList2.getModel()).getSize() == 0 )
            jButtonNext.setEnabled(false);
        else
            jButtonNext.setEnabled(true);
    }//GEN-LAST:event_jButton7ActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new WizardDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDesignQuery;
    private javax.swing.JButton jButtonLoadQuery;
    private javax.swing.JButton jButtonModifyField;
    private javax.swing.JButton jButtonNewConnection;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JButton jButtonSaveQuery;
    private javax.swing.JCheckBox jCheckBoxSaveTemplate;
    private javax.swing.JCheckBox jCheckBoxSetHeader;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBoxConnection;
    private javax.swing.JComboBox jComboBoxTemplates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelG1;
    private javax.swing.JLabel jLabelG2;
    private javax.swing.JLabel jLabelG3;
    private javax.swing.JLabel jLabelG4;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JLabel jLabelStep1;
    private javax.swing.JLabel jLabelStep2;
    private javax.swing.JLabel jLabelStep3;
    private javax.swing.JLabel jLabelStep4;
    private javax.swing.JLabel jLabelStep5;
    private javax.swing.JLabel jLabelStepCaption;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelRoot;
    private javax.swing.JPanel jPanelStep1;
    private javax.swing.JPanel jPanelStep2;
    private javax.swing.JPanel jPanelStep3;
    private javax.swing.JPanel jPanelStep4;
    private javax.swing.JPanel jPanelStep5;
    private javax.swing.JPanel jPanelStep6;
    private javax.swing.JPanel jPanelStepCaption;
    private javax.swing.JPanel jPanelSteps;
    private javax.swing.JRadioButton jRadioButtonColumnarLayout;
    private javax.swing.JRadioButton jRadioButtonTabularLayout;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTableFields;
    private javax.swing.JTextField jTextFieldBean;
    private javax.swing.JTextField jTextFieldTemplateName;
    // End of variables declaration//GEN-END:variables
    
    private int status;
    
    private Report report;
    
    private int dialogResult;
    
    public void nextStep() {
        this.setStep( wzStep+1, wzStep);
    }
    
    public void prevStep() {
        this.setStep(wzStep-1, wzStep);
    }
    
    
    public void setStep(int step, int oldStep) {
        jButtonCancel.setText(it.businesslogic.ireport.util.I18n.getString("cancel","Cancel"));
        
        switch (step) {
            case 1:
                setViewedPanel(1);
                if (jRSQLExpressionArea1.getText().trim().length() > 0 ||
                        jComboBoxConnection.getSelectedIndex() == 0) {
                    jButtonNext.setEnabled(true);
                } else
                    jButtonNext.setEnabled(false);
                
                jButtonPrev.setEnabled(false);
                break;
            case 2:
                // Read fields....
                if (oldStep < step && jComboBoxConnection.getSelectedIndex() > 0) {
                    // We must retrive fields...
                    jButtonNext.setEnabled(false);
                    t = new Thread(this);
                    t.start();
                    return;
                }
                setViewedPanel(2);
                
                if (  ((javax.swing.DefaultListModel)jList2.getModel()).getSize() == 0
                        && jComboBoxConnection.getSelectedIndex() > 0)
                    jButtonNext.setEnabled(false);
                else
                    jButtonNext.setEnabled(true);
                
                jButton4.setEnabled(jComboBoxConnection.getSelectedIndex() != 0);
                jButton5.setEnabled(jComboBoxConnection.getSelectedIndex() != 0);
                jButton6.setEnabled(jComboBoxConnection.getSelectedIndex() != 0);
                jButton7.setEnabled(jComboBoxConnection.getSelectedIndex() != 0);
                
                jButtonPrev.setEnabled(true);
                break;
            case 3:
                
                // If we are using an sql query, skip this step...
                if (jComboBoxConnection.getSelectedItem() != null) {
                    IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
                    if (irc.isJDBCConnection()) {
                        wzStep=3;
                        if (oldStep < step)
                        {
                            // Go to step 4...
                            nextStep();
                        }
                        else
                        {
                            prevStep();
                        }
                        return;
                    }
                 }
                // Review fields....
                jButtonPrev.setEnabled(true);
                jButtonNext.setEnabled(true);
                
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableFields.getModel();
                dtm.setRowCount(0);
                javax.swing.DefaultListModel dlmFields = (javax.swing.DefaultListModel)jList2.getModel();
                for (int i=0; i<dlmFields.getSize(); ++i) {
                
                    JRField f = (JRField)(dlmFields.getElementAt(i));
                    dtm.addRow(new Object[]{f, f.getClassType()});
                }
                
                setViewedPanel(3);
                break;
            case 4:
                if (oldStep < step) {
                    jComboBox1.removeAllItems();
                    jComboBox2.removeAllItems();
                    jComboBox2.setEnabled(false);
                    jLabelG2.setEnabled(false);
                    jComboBox3.removeAllItems();
                    jComboBox3.setEnabled(false);
                    jLabelG3.setEnabled(false);
                    jComboBox4.removeAllItems();
                    jComboBox4.setEnabled(false);
                    jLabelG4.setEnabled(false);
                    
                    javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jList2.getModel();
                    jComboBox1.addItem("");
                    for (int i=0; i<dlm.getSize(); ++i) {
                        jComboBox1.addItem( dlm.getElementAt(i));
                    }
                    
                    if (this.userTemplate != null)
                    {
                        boolean found = false;
                        for (int k=0; k<userTemplate.getGroupExpressions().size() && k<4; ++k)
                        {
                            String exp = ""+userTemplate.getGroupExpressions().get(k);
                            if (exp.length() == 0) break;
                            JComboBox combo =  jComboBox1;
                            if (k==1) combo = jComboBox2;
                            if (k==2) combo = jComboBox3;
                            if (k==3) combo = jComboBox4;
                            
                            System.out.println("Combo " + k + " items: " + combo.getItemCount());
                            for (int i=0; i<combo.getItemCount(); ++i) {
                                String s = ""+combo.getItemAt(i);
                                if (s.equals(exp))
                                {
                                    combo.setSelectedIndex(i);
                                    
                                    if (k==0) jComboBox1ActionPerformed(null);
                                    else if (k==1) jComboBox2ActionPerformed(null);
                                    else if (k==2) jComboBox3ActionPerformed(null);
                                    //else if (k==3) jComboBox4ActionPerformed(null);
                                    break;
                                }
                            }
                        }
                        
                        jCheckBoxSetHeader.setSelected( userTemplate.isSaveFieldDescriptions() );
                    }
                }
                setViewedPanel(4);
                break;
            case 5:
                
                // Find the right report template file
                if (this.userTemplate != null && oldStep < step)
                {
                    for (int i=0; i<this.templates.size(); ++i)
                    {
                        IReportTemplate itemplate = (IReportTemplate)templates.elementAt(i);
                        if (itemplate.getXmlFile().equals( userTemplate.getTemplateFile()))
                        {
                            jRadioButtonColumnarLayout.setSelected( itemplate.getType() == itemplate.COLUMNAR);
                            jRadioButtonTabularLayout.setSelected( itemplate.getType() == itemplate.TABULAR);
                            
                            updateTemplatesList();
                            jList3.setSelectedValue(itemplate, true);
                        }
                    }
                }
                
                jButtonNext.setEnabled( jList3.getSelectedIndex()>=0 );
                setViewedPanel(5);
                break;
            case 6:
                jButtonNext.setEnabled( false );
                jButtonCancel.setText(it.businesslogic.ireport.util.I18n.getString("finish", "Finish"));
                setViewedPanel(6);
                break;
        }
        
        if (maxStepVisited < step) maxStepVisited = step;
        wzStep = step;
    }
    
    public void setViewedPanel(int panel) {
        this.jPanelRoot.removeAll();
        java.awt.Font f = new java.awt.Font( jLabelStep1.getFont().getName(), java.awt.Font.PLAIN, 11);
        java.awt.Font f2 = new java.awt.Font( jLabelStep1.getFont().getName(), java.awt.Font.BOLD, 11);
        this.jLabelStep1.setFont(f);
        this.jLabelStep2.setFont(f);
        this.jLabelStep3.setFont(f);
        this.jLabelStep4.setFont(f);
        this.jLabelStep5.setFont(f);
        
        switch (panel) {
            case 1:
                this.jPanelRoot.add(jPanelStep1);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep1.1","1. Query"));
                this.jLabelStep1.setFont(f2);
                break;
            case 2:
                this.jPanelRoot.add(jPanelStep2);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep2.1","2. Fields selection"));
                this.jLabelStep2.setFont(f2);
                break;
            case 3:
                this.jPanelRoot.add(jPanelStep3);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep2.1","2. Fields selection"));
                this.jLabelStep2.setFont(f2);
                break;
            case 4:
                this.jPanelRoot.add(jPanelStep4);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep3.1","3. Group by..."));
                this.jLabelStep3.setFont(f2);
                break;
            case 5:
                this.jPanelRoot.add(jPanelStep5);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep4.1","4. Layout"));
                this.jLabelStep4.setFont(f2);
                break;
            case 6:
                this.jPanelRoot.add(jPanelStep6);
                this.jLabelStepCaption.setText(it.businesslogic.ireport.util.I18n.getString("wizStep5.1","5. Finish"));
                this.jLabelStep5.setFont(f2);
                break;
        }
        
        jPanelRoot.updateUI();
    }
    
    
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     *
     */
    public void run() {
        
        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
        // This method is invoked to read fields....
        try {
            IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
            
            
            // Get fields....
            List fields = readFields(irc,jRSQLExpressionArea1.getText(), jTextFieldBean.getText());
            
            if (fields == null) return;
            DefaultListModel dlm1 = (DefaultListModel)jList1.getModel();
            DefaultListModel dlm2 = (DefaultListModel)jList2.getModel();
            
            dlm1.removeAllElements();
            dlm2.removeAllElements();
            
            for (int i=0; i <fields.size(); ++i) {
                it.businesslogic.ireport.JRField field = (JRField)fields.get(i);
                // Check if parameter already exists...
                boolean found = false;
                for (int k=0; k< dlm1.size(); ++k) {
                    it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)dlm1.getElementAt(k);
                    if (f.getName().equalsIgnoreCase(field.getName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //this.getJReportFrame().getReport().getFields().addElement(field);
                    dlm1.addElement(field);
                }
            }
            
            // Move the selected fields...
            if (this.userTemplate != null)
            {
                for (int k=0; k<userTemplate.getDisplayFields().size(); ++k)
                {
                    JRField f_t = (JRField)userTemplate.getDisplayFields().get(k);
                    for (int i=0; i <fields.size(); ++i) {
                        JRField field = (JRField)fields.get(i);
                        if (field.getName().equals( f_t.getName()))
                        {
                            field.setClassType( f_t.getClassType());
                            dlm1.removeElement(field);
                            dlm2.addElement(field);
                        }
                    }
                }
            }
            
            jList1.updateUI();
            jList2.updateUI();
            
            setStep(2, 2);
            
        } catch (Exception ex) {
            java.io.StringWriter sw = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter( sw ));
            javax.swing.JOptionPane.showMessageDialog(getParent(),sw.getBuffer()+"",
                    I18n.getString("message.title.error","Error"), javax.swing.JOptionPane.ERROR_MESSAGE);
            this.cancelElaborationStep1();
            return;
        }
        
        
    }
    
    public void cancelElaborationStep1() {
        this.jButtonNext.setEnabled(true);
        this.jButtonPrev.setEnabled(false);
    }
    
    
   
    
    private String getQueryLanguage()
    {
        if (jComboBoxConnection.getSelectedItem() == null)
        {
            return "sql";
        }
        else if (jComboBoxConnection.getSelectedItem() instanceof JRHibernateConnection) {
            return "hql";
        } 
        else if (jComboBoxConnection.getSelectedItem() instanceof JRXMLADataSourceConnection)
        {
            return "xmla-mdx";
        }
        return "sql";
        
    }
    
    
    
    
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
    
    /** Getter for property report.
     * @return Value of property report.
     *
     */
    public it.businesslogic.ireport.Report getReport() {
        return report;
    }
    
    /** Setter for property report.
     * @param report New value of property report.
     *
     */
    public void setReport(it.businesslogic.ireport.Report report) {
        this.report = report;
    }
    
    
    
    /**
     * Return a list of JRField
     *
     */
    public static List readFields(IReportConnection conn, String query, String className) {
        String error_msg = null;
        if (conn.isJDBCConnection()) {
            Connection con = null;
            Statement ps = null;
            
            try {
                
                
                con = conn.getConnection();
                
                ps = con.createStatement();
                
                // Some JDBC drivers don't supports this method...
                try { ps.setFetchSize(0); } catch(Exception e ) {}
                
                ResultSet rs = ps.executeQuery(query);
                
                
                ResultSetMetaData rsmd = rs.getMetaData();
                
                
                List columns = new ArrayList();
                for (int i=1; i <=rsmd.getColumnCount(); ++i) {
                    it.businesslogic.ireport.JRField field =
                            new it.businesslogic.ireport.JRField(
                            rsmd.getColumnLabel(i),
                            Misc.getJdbcTypeClass(rsmd, i) );
                    columns.add( field );
                }
                
                
                return columns;
            } catch( IllegalArgumentException ie ) {
                error_msg = ie.getMessage();
            } catch (NoClassDefFoundError ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.noClassDefFoundError","{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                        new Object[]{"", ex.getMessage() }
                );
                
            } catch (java.sql.SQLException ex) {
                error_msg = I18n.getFormattedString(
                        "messages.connection.sqlError","{0}\nSQL problems: {1}\n{2}",
                        new Object[]{"", ex.getMessage() }
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.generalError","{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                        new Object[]{"", ex.getMessage() }
                );
            } finally {
                if(ps!=null) try { ps.close(); } catch(Exception e ) {}
                if(con !=null) try { con.close(); } catch(Exception e ) {}
            }
        } else if ( conn instanceof JRHibernateConnection) {
            it.businesslogic.ireport.data.hibernate.HQLFieldsReader reader = new it.businesslogic.ireport.data.hibernate.HQLFieldsReader(query,new Vector());
            try {
                return reader.readFields();
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.generalError","{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                        new Object[]{"", ex.getMessage() }
                );
            }
        } else if ( conn instanceof JRDataSourceProviderConnection) {
            
            try {
                net.sf.jasperreports.engine.JRField[] jrfields = ((JRDataSourceProviderConnection)conn).getDataSourceProvider().getFields(null);
                
                java.util.ArrayList v_fields = new java.util.ArrayList();
                for (int i=0; i<jrfields.length; ++i) {
                    it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(jrfields[i].getName(), jrfields[i].getValueClassName());
                    field.setDescription( it.businesslogic.ireport.util.Misc.nvl( jrfields[i].getDescription(),""));
                    v_fields.add( field );
                }
                
                return v_fields;
                
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.generalError","{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                        new Object[]{"", ex.getMessage() }
                );
            }
        } else if ( conn instanceof JRXMLADataSourceConnection) {
            
            try {
                
                CincomMDXFieldsProvider mfp = new CincomMDXFieldsProvider();
                java.util.ArrayList v_fields = mfp.getFields( query,  (JRXMLADataSourceConnection)conn);
                return v_fields;
                
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.generalError","{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                        new Object[]{"", ex.getMessage() }
                );
            }
            
        } else if ( conn instanceof JavaBeanDataSourceConnection) {
            java.util.ArrayList v_fields = new java.util.ArrayList();
            try {
                
                Class clazz = Class.forName(className);
                
                java.beans.PropertyDescriptor[] pd = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(clazz);
                for (int nd =0; nd < pd.length; ++nd) {
                    String fieldName = pd[nd].getName();
                    if (pd[nd].getPropertyType() != null && pd[nd].getReadMethod() != null) {
                        String returnType =  pd[nd].getPropertyType().getName();
                        it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, Misc.getJRFieldType(returnType));
                        field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                        v_fields.add( field );
                    }
                }
                return v_fields;
                
            } catch (ClassNotFoundException cnf) {


                error_msg = I18n.getFormattedString(
                        "messages.connection.classNotFoundError","{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                        new Object[]{"", cnf.getMessage(), "" }
                );
                
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = I18n.getFormattedString(
                        "messages.connection.generalError","{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                        new Object[]{"", ex.getMessage() }
                );
            }
            
        } else if ( conn instanceof JRCSVDataSourceConnection) {
            java.util.ArrayList v_fields = new java.util.ArrayList();
            try {
                
                Vector names = ((JRCSVDataSourceConnection)conn).getColumnNames();
                
                for (int nd =0; nd < names.size(); ++nd) {
                    String fieldName = ""+names.elementAt(nd);
                    it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, "java.lang.String");
                    field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                    v_fields.add( field );
                }
                return v_fields;
                
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = "" + ex.getMessage();
            }
            
        } else if ( conn instanceof NullConnection) {
            
            java.util.ArrayList v_fields = new java.util.ArrayList();
            try {
                
                
                return v_fields;
                
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = "" + ex.getMessage();
            }
            
        }
        
        if (error_msg != null) {
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),error_msg,
                    I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    //added by Felix Firgau on Feb 8th 2006
    public void applyI18n(){
        // Start autogenerated code ----------------------
        jRadioButtonColumnarLayout.setText(I18n.getString("wizardDialog.radioButtonColumnarLayout","Columnar layout"));
        jRadioButtonTabularLayout.setText(I18n.getString("wizardDialog.radioButtonTabularLayout","Tabular layout"));
        // End autogenerated code ----------------------
        // Start autogenerated code ----------------------
        jButton4.setText(I18n.getString("wizardDialog.button4",">"));
        jButton5.setText(I18n.getString("wizardDialog.button5",">>"));
        jButton6.setText(I18n.getString("wizardDialog.button6","<"));
        jButton7.setText(I18n.getString("wizardDialog.button7","<<"));
        // End autogenerated code ----------------------
        
        jLabel13.setText(it.businesslogic.ireport.util.I18n.getString("connectionDialog.labelTemplate","Use the following template..."));
        jLabel1.setText(it.businesslogic.ireport.util.I18n.getString("wizLabel", "Steps"));
        jLabelStep1.setText(it.businesslogic.ireport.util.I18n.getString("wizStep1", "Step 1"));
        jLabelStep2.setText(it.businesslogic.ireport.util.I18n.getString("wizStep2", "Step 2"));
        jLabelStep3.setText(it.businesslogic.ireport.util.I18n.getString("wizStep3", "Step 3"));
        jLabelStep4.setText(it.businesslogic.ireport.util.I18n.getString("wizStep4","Step 4"));
        jLabelStep5.setText(it.businesslogic.ireport.util.I18n.getString("wizStep5","Step 5"));
        jLabel2.setText(it.businesslogic.ireport.util.I18n.getString("queryString","Query string"));
        jLabelG1.setText(it.businesslogic.ireport.util.I18n.getString("group","Group")+" 1");
        jLabelG2.setText(it.businesslogic.ireport.util.I18n.getString("group","Group")+" 2");
        jLabelG3.setText(it.businesslogic.ireport.util.I18n.getString("group","Group")+" 3");
        jLabelG4.setText(it.businesslogic.ireport.util.I18n.getString("group","Group")+" 4");
        jLabel4.setText(it.businesslogic.ireport.util.I18n.getString("congratulation","Congratulation!"));
        jLabel10.setText(it.businesslogic.ireport.util.I18n.getString("wizardSuccess","You have succesfully created a new report."));
        jLabel11.setText(it.businesslogic.ireport.util.I18n.getString("wizardFinished","Press 'Finish' to generate it."));
        jLabel12.setText(it.businesslogic.ireport.util.I18n.getString("connectionsDatasources", "Connection / Datasource"));
        jButtonPrev.setText(it.businesslogic.ireport.util.I18n.getString("prev","< Prev"));
        jButtonNext.setText(it.businesslogic.ireport.util.I18n.getString("next","Next >"));
        jButtonCancel.setText(it.businesslogic.ireport.util.I18n.getString("cancel","Cancel"));
        jButtonNewConnection.setText(it.businesslogic.ireport.util.I18n.getString("new","New"));
        jButtonLoadQuery.setText(it.businesslogic.ireport.util.I18n.getString("loadQuery","Load query"));
        jButtonSaveQuery.setText(it.businesslogic.ireport.util.I18n.getString("saveQuery","Save query"));
        jCheckBoxSetHeader.setText(it.businesslogic.ireport.util.I18n.getString("setHeader","Get column descriptions from datasource"));
        jCheckBoxSaveTemplate.setText(it.businesslogic.ireport.util.I18n.getString("wizardDialog.checkBoxSaveTemplate","Save choices as template..."));
        
        jButtonModifyField.setText( it.businesslogic.ireport.util.I18n.getString("modify","Modify") );
        jTableFields.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("valuesPanel.tableFields.fieldName","Field name") );
        jTableFields.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("valuesPanel.tableFields.classType","Class type") );
        
        this.setTitle(I18n.getString("wizardDialog.title","iReport Wizard"));
        jButtonPrev.setMnemonic(I18n.getString("wizardDialog.buttonPrevMnemonic","p").charAt(0));
        jButtonNext.setMnemonic(I18n.getString("wizardDialog.buttonNextMnemonic","n").charAt(0));
        jButtonCancel.setMnemonic(I18n.getString("wizardDialog.buttonCancelMnemonic","c").charAt(0));
        
    }
    
    public void languageChanged(LanguageChangedEvent evt) {
        applyI18n();
    }
    //Modification end

    
    public void jTableFieldsListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {

        if (this.jTableFields.getSelectedRowCount() > 0)
        {
            this.jButtonModifyField.setEnabled(true);
        }
        else
        {
            this.jButtonModifyField.setEnabled(false);
        }
    }
}

class TemplateFile {
    
    public File file = null;
    public String name = "";
    public String toString() { return name; }
}

