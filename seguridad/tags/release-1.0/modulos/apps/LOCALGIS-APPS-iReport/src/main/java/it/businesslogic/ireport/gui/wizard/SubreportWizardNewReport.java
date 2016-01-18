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
 * SubreportWizardNewReport.java
 * 
 * Created on March 22, 2006, 8:52 PM
 *
 */

package it.businesslogic.ireport.gui.wizard;

import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.IReportTemplate;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.StaticTextReportElement;
import it.businesslogic.ireport.SubReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.TransformationType;
import it.businesslogic.ireport.connection.JDBCConnection;
import it.businesslogic.ireport.connection.JRHibernateConnection;
import it.businesslogic.ireport.connection.NullConnection;
import it.businesslogic.ireport.gui.ConnectionDialog;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.WizardDialog;
import it.businesslogic.ireport.util.Misc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XBoolean;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class SubreportWizardNewReport extends javax.swing.JPanel implements GenericWizard, Runnable {
    
    private String reportFileName = null;
    private SubReportElement subReportElement = null;
    private BaseWizardPanel wizardPanel = null;
    private javax.swing.JDialog wizardDialog = null;
    
    Vector templates = null;
    
    private Thread t = null;
  
    /** Creates new form SubreportWizardPanes */
    public SubreportWizardNewReport() {
        initComponents();
        applyI18n();
        jRSQLExpressionArea1.getDocument().addDocumentListener( new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
            public void insertUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
            public void removeUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
        });
        
        jTextFieldBean.getDocument().addDocumentListener( new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
            public void insertUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
            public void removeUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
            }
        });
        
        jTextFieldReportFileName.getDocument().addDocumentListener( new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
                updateExpressionLabels();
            }
            public void insertUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
                updateExpressionLabels();
            }
            public void removeUpdate(DocumentEvent e) {
                if (wizardPanel != null) wizardPanel.updateButtons();
                updateExpressionLabels();
            }
        });
        
        // These are the combobox values
        Vector values = new Vector();
        
        Report report = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        
        values.addAll( report.getFields());
        values.addAll( report.getVariables());
        values.addAll(report.getParameters());
        
        jList1.setModel( new DefaultListModel());
        jList2.setModel( new DefaultListModel());
        jList3.setModel( new DefaultListModel());
        
            
        // If the cell should appear like a combobox in its
        // non-editing state, also set the combobox renderer
        //col.setCellRenderer(new TableComboBoxRenderer(values));
        
        updateTemplatesList();
        
        String fileName = report.getFilename();
        File f = new File(fileName);
        String baseFileName = f.getName();
        String pathFile = f.getParent();
        if (pathFile.length() > 0 && !pathFile.endsWith(File.separator))
        {
            pathFile += File.separator;
        }
        
        if (baseFileName.indexOf(".") > 0)
            baseFileName = baseFileName.substring(0, baseFileName.lastIndexOf("."));
        
        File f2 = null;
        for (int i = 0; ; ++i)
        {
            fileName = baseFileName + "_subreport" + i + ".jrxml";
        
            f2 = new File(pathFile, fileName);
            if (!f2.exists()) break;
        }
        
        jTextFieldReportFileName.setText(f2.getName());

    }   
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel0 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxConnection = new javax.swing.JComboBox();
        jButtonNewConnection = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jRSQLExpressionArea1 = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jTextFieldBean = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jButtonLoadQuery = new javax.swing.JButton();
        jButtonSaveQuery = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanelButtons = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jRadioButtonColumnarLayout = new javax.swing.JRadioButton();
        jRadioButtonTabularLayout = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jLabelImage = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldReportFileName = new javax.swing.JTextField();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel2e = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        buttonGroup2 = new javax.swing.ButtonGroup();

        jPanel0.setLayout(new java.awt.GridBagLayout());

        jPanel0.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jPanel0PropertyChange(evt);
            }
        });

        jPanel61.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel61.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel0.add(jPanel61, new java.awt.GridBagConstraints());

        jLabel12.setText("Connection / Datasource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 2, 0);
        jPanel0.add(jLabel12, gridBagConstraints);

        jComboBoxConnection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxConnection.setMinimumSize(new java.awt.Dimension(51, 22));
        jComboBoxConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxConnectionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel0.add(jComboBoxConnection, gridBagConstraints);

        jButtonNewConnection.setText("New");
        jButtonNewConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewConnectionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 20);
        jPanel0.add(jButtonNewConnection, gridBagConstraints);

        jLabel2.setText("Query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(30, 20, 2, 0);
        jPanel0.add(jLabel2, gridBagConstraints);

        jPanel41.setLayout(new java.awt.GridBagLayout());

        jPanel41.setMinimumSize(new java.awt.Dimension(391, 120));
        jPanel41.setPreferredSize(new java.awt.Dimension(391, 120));
        jRSQLExpressionArea1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRSQLExpressionArea1.setElectricScroll(0);
        jRSQLExpressionArea1.setMinimumSize(new java.awt.Dimension(300, 100));
        jRSQLExpressionArea1.setPreferredSize(new java.awt.Dimension(300, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel41.add(jRSQLExpressionArea1, gridBagConstraints);

        jTextFieldBean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBeanActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel41.add(jTextFieldBean, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButtonLoadQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/folder_database.png")));
        jButtonLoadQuery.setText("Load query");
        jButtonLoadQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonLoadQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadQueryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jButtonLoadQuery, gridBagConstraints);

        jButtonSaveQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/database_save.png")));
        jButtonSaveQuery.setText("Save query");
        jButtonSaveQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonSaveQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveQueryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel5.add(jButtonSaveQuery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel41.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        jPanel0.add(jPanel41, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel0.add(jPanel6, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setViewportView(jList2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 10);
        jPanel1.add(jScrollPane2, gridBagConstraints);

        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jButton1.setText(">");
        jButton1.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton1.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton1.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        jPanelButtons.add(jButton1, gridBagConstraints);

        jButton2.setText(">>");
        jButton2.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton2.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton2.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        jPanelButtons.add(jButton2, gridBagConstraints);

        jButton3.setText("<<");
        jButton3.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton3.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton3.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        jPanelButtons.add(jButton3, gridBagConstraints);

        jButton4.setText("<");
        jButton4.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jButton4.setMinimumSize(new java.awt.Dimension(28, 28));
        jButton4.setPreferredSize(new java.awt.Dimension(28, 28));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        jPanelButtons.add(jButton4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel1.add(jPanelButtons, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(30, 20, 0, 0);
        jPanel2.add(jRadioButtonColumnarLayout, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 0, 0);
        jPanel2.add(jRadioButtonTabularLayout, gridBagConstraints);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel7.setMinimumSize(new java.awt.Dimension(150, 23));
        jPanel7.setPreferredSize(new java.awt.Dimension(150, 0));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(100, 23));
        jList3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList3ValueChanged(evt);
            }
        });

        jScrollPane3.setViewportView(jList3);

        jPanel7.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 10, 0);
        jPanel2.add(jPanel7, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabelImage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelImage.setText("  ");
        jLabelImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabelImage.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jLabelImage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 10, 10);
        jPanel2.add(jPanel4, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Save the subreport as...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 0);
        jPanel3.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel3.add(jTextFieldReportFileName, gridBagConstraints);

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Store the directory name in a parameter");
        jRadioButton4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton4.setFocusPainted(false);
        jRadioButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton4.setMinimumSize(new java.awt.Dimension(115, 20));
        jRadioButton4.setPreferredSize(new java.awt.Dimension(115, 20));
        jRadioButton4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged2(evt);
            }
        });
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(40, 20, 0, 0);
        jPanel3.add(jRadioButton4, gridBagConstraints);

        jLabel1.setText("$P{SUBREPORT_DIR} + /name.jasper");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 40, 0, 0);
        jPanel3.add(jLabel1, gridBagConstraints);

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Use a static absolute path reference");
        jRadioButton5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton5.setFocusPainted(false);
        jRadioButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButton5.setMinimumSize(new java.awt.Dimension(115, 20));
        jRadioButton5.setPreferredSize(new java.awt.Dimension(115, 20));
        jRadioButton5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2jRadioButton1ItemStateChanged1(evt);
            }
        });
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2jRadioButton1ActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
        jPanel3.add(jRadioButton5, gridBagConstraints);

        jLabel2e.setText("path reference");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 40, 0, 0);
        jPanel3.add(jLabel2e, gridBagConstraints);

        jButton5.setText("Browse...");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 10);
        jPanel3.add(jButton5, gridBagConstraints);

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveQueryActionPerformed
        Misc.saveSQLQuery( jRSQLExpressionArea1.getText(), this );
    }//GEN-LAST:event_jButtonSaveQueryActionPerformed

    private void jButtonLoadQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadQueryActionPerformed
        String query = Misc.loadSQLQuery(this);
        
        if (query != null) {
            jRSQLExpressionArea1.setText(query);
        }
    }//GEN-LAST:event_jButtonLoadQueryActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        
        // jfilechooser...
	    JFileChooser jfc = new JFileChooser();
            File original_file = null;
            try {
                original_file = new File(MainFrame.getMainInstance().getActiveReportFrame().getReport().getFilename());
                if (original_file!= null && original_file.getParentFile() != null)
                {
                    jfc.setCurrentDirectory( original_file.getParentFile());
                }
            } catch (Exception ex)
            {
                
            }
            
	    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".jrxml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "JasperReports Report file *jrxml";
		    }
	    });
            
	    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                
                    if (original_file != null && original_file.equals(jfc.getSelectedFile()))
                    {
                        JOptionPane.showMessageDialog(this,
                                I18n.getString("messages.subreportWizardNewReport.duplicatedFile",
                                "Master and report files can not be the same file!") );
                        return;
                    }
                    
                    String s = jfc.getSelectedFile().getPath();
                    if (original_file != null && original_file.getParentFile() != null)
                    {
                        if (original_file.getParentFile().equals( jfc.getSelectedFile().getParentFile()))
                        {
                            s = jfc.getSelectedFile().getName();
                        }
                    }
                    if (!s.toLowerCase().endsWith(".jrxml"))
                    {
                        if (!s.endsWith(".")) s += ".";
                        s += "jrmxl";
                    }
                    jTextFieldReportFileName.setText( s );
	    }
        if (wizardPanel != null) wizardPanel.updateButtons();
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jList3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList3ValueChanged
        if (jList3.getSelectedIndex() >= 0) {
            IReportTemplate tf = (IReportTemplate)jList3.getSelectedValue();
            // Take the image...
            
            this.jLabelImage.setIcon( tf.getIcon() );
            if (tf.getIcon() == null)
            {
                this.jLabelImage.setText(" ");
            }
            else
            {
                this.jLabelImage.setText("");
            }
            
            this.jLabelImage.updateUI();
            if (wizardPanel != null) wizardPanel.updateButtons();
        }
    }//GEN-LAST:event_jList3ValueChanged

    private void jRadioButtonTabularLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonTabularLayoutActionPerformed
        updateTemplatesList();
    }//GEN-LAST:event_jRadioButtonTabularLayoutActionPerformed

    private void jRadioButtonColumnarLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonColumnarLayoutActionPerformed
        updateTemplatesList();
    }//GEN-LAST:event_jRadioButtonColumnarLayoutActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int index = jList2.getSelectedIndex();
        if (index <0) return;
        
        Object[] objs = jList2.getSelectedValues();
        for (int i=0; i< objs.length; ++i) {
            Object obj = objs[i];
            ((javax.swing.DefaultListModel)jList1.getModel()).addElement(obj);
            ((javax.swing.DefaultListModel)jList2.getModel()).removeElement(obj);
        }
        if (jList1.getModel().getSize()>0) jList1.setSelectedIndex(0);
        wizardPanel.updateButtons();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        javax.swing.DefaultListModel list2 = (javax.swing.DefaultListModel)jList2.getModel();
        javax.swing.DefaultListModel list1 = (javax.swing.DefaultListModel)jList1.getModel();
        
        for (int i=0; i< list2.getSize(); ++i) {
            list1.addElement(list2.getElementAt(i));
        }
        list2.removeAllElements();
        if (list1.size()>0) jList1.setSelectedIndex(0);
        wizardPanel.updateButtons();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        javax.swing.DefaultListModel list1 = (javax.swing.DefaultListModel)jList1.getModel();
        javax.swing.DefaultListModel list2 = (javax.swing.DefaultListModel)jList2.getModel();
        
        for (int i=0; i< list1.getSize(); ++i) {
            list2.addElement(list1.getElementAt(i));
        }
        list1.removeAllElements();
        wizardPanel.updateButtons();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index = jList1.getSelectedIndex();
        if (index <0) return;
        
        Object[] objs = jList1.getSelectedValues();
        for (int i=0; i< objs.length; ++i) {
            Object obj = objs[i];
            ((javax.swing.DefaultListModel)jList2.getModel()).addElement(obj);
            ((javax.swing.DefaultListModel)jList1.getModel()).removeElement(obj);
        }
        if (jList1.getModel().getSize()>0) jList1.setSelectedIndex(0);
        wizardPanel.updateButtons();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldBeanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBeanActionPerformed
        jComboBoxConnectionActionPerformed(null);
    }//GEN-LAST:event_jTextFieldBeanActionPerformed

    private void jButtonNewConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewConnectionActionPerformed
        it.businesslogic.ireport.connection.gui.ConnectionDialog cd = new it.businesslogic.ireport.connection.gui.ConnectionDialog(wizardDialog,true);
        
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

    public void updateConnections() {
        Object ircDefault =  MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        jComboBoxConnection.removeAllItems();
        
        jComboBoxConnection.addItem(new NullConnection());
        
        Enumeration e = MainFrame.getMainInstance().getConnections().elements();
        while (e.hasMoreElements()) {
            IReportConnection irc = (IReportConnection)e.nextElement();
            jComboBoxConnection.addItem(irc);
        }
        
        if (ircDefault != null) {
            jComboBoxConnection.setSelectedItem(ircDefault);
        }
    }
    
    private void jComboBoxConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConnectionActionPerformed
        boolean canNext = false;
        if (jComboBoxConnection.getSelectedItem() != null) {
            IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
            
            if (irc.isJDBCConnection()) {
                jLabel2.setText("SQL query");
                jTextFieldBean.setVisible(false);
                jRSQLExpressionArea1.setVisible(true);
                if (jRSQLExpressionArea1.getText().length() > 0) canNext = true;
            } else if (irc instanceof JRHibernateConnection) {
                jLabel2.setText("HQL query");
                jTextFieldBean.setVisible(false);
                jRSQLExpressionArea1.setVisible(true);
                if (jRSQLExpressionArea1.getText().length() > 0) canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.JRDataSourceProviderConnection) {
                jLabel2.setText(" ");
                jTextFieldBean.setVisible(false);
                jRSQLExpressionArea1.setVisible(false);
                canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.JavaBeanDataSourceConnection) {
                jLabel2.setText("JavaBean class");
                jRSQLExpressionArea1.setVisible(false);
                jTextFieldBean.setVisible(true);
                if (jTextFieldBean.getText().length() > 0) canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.JRCSVDataSourceConnection) {
                jLabel2.setText(" ");
                jTextFieldBean.setVisible(false);
                jRSQLExpressionArea1.setVisible(false);
                canNext = true;
            } else if (irc instanceof it.businesslogic.ireport.connection.NullConnection) {
                jLabel2.setText(" ");
                jTextFieldBean.setVisible(false);
                jRSQLExpressionArea1.setVisible(false);
                canNext = true;
            } else {
                jLabel2.setText("You can't use the wizard with this kind of datasource");
                jRSQLExpressionArea1.setVisible(false);
                jTextFieldBean.setVisible(false);
                canNext = false;
            }
        } else {
            jLabel2.setText("Please create a new connection / datasource.");
            jRSQLExpressionArea1.setVisible(false);
            jTextFieldBean.setVisible(false);
        }
        jPanel5.setVisible(jRSQLExpressionArea1.isVisible());
        if (wizardPanel != null) wizardPanel.updateButtons();
    }//GEN-LAST:event_jComboBoxConnectionActionPerformed

    private void jRadioButton2jRadioButton1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2jRadioButton1ActionPerformed1
// TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2jRadioButton1ActionPerformed1

    private void jRadioButton2jRadioButton1ItemStateChanged1(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2jRadioButton1ItemStateChanged1
// TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2jRadioButton1ItemStateChanged1

    private void jRadioButton1ActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed2
// TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed2

    private void jRadioButton1ItemStateChanged2(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged2
// TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ItemStateChanged2

    private void jPanel0PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanel0PropertyChange

        
        
    }//GEN-LAST:event_jPanel0PropertyChange
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonLoadQuery;
    private javax.swing.JButton jButtonNewConnection;
    private javax.swing.JButton jButtonSaveQuery;
    private javax.swing.JComboBox jComboBoxConnection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel2e;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel0;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelButtons;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRSQLExpressionArea1;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButtonColumnarLayout;
    private javax.swing.JRadioButton jRadioButtonTabularLayout;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextFieldBean;
    private javax.swing.JTextField jTextFieldReportFileName;
    // End of variables declaration//GEN-END:variables


    public String[] getStepsNames() {
        
        String[] names = new String[4];
        //
        names[0] = I18n.getString("subreportWizardNewReport.stepsnames.query","Query/datasource");
        names[1] = I18n.getString("subreportWizardNewReport.stepsnames.template","Template");
        names[2] = I18n.getString("subreportWizardNewReport.stepsnames.expression","Expression");
        //names[3] = I18n.getString("subreportWizardNewReport.stepsnames.finish","Finish");
        //names[0] = "Query/datasource";
        //names[1] = "Fields";
        //names[2] = "Template";
        //names[3] = "Expression";
        //names[3] = "Finish";
        //
        
        return names;
    }

    public String getStepDescription(int step) {
        
    	//
        if (step==0) return
        		I18n.getString("subreportWizardExistingReport.stepdescription.step0","How fill the subreport");
        		//"How fill the subreport";        
        if (step==1) return
        		I18n.getString("subreportWizardExistingReport.stepdescription.step0","Select the subreport fields");
        		//"Select the subreport fields";
        if (step==2) return
        		I18n.getString("subreportWizardExistingReport.stepdescription.step0","Choose a template");
        		//"Choose a template";
        if (step==3) return
        		I18n.getString("subreportWizardExistingReport.stepdescription.step0","Subreport expession");
        		//"Subreport expession";
        //
        
        return "";
    }

    public void initWizard() {
        updateConnections();
    }

    public void finish(int currentStep) {
        if (currentStep < 0)
        {
             if (t != null && t.isAlive()) {
                t.interrupt();
                return;
            }
        }
        if (currentStep >= 0)
        {
            try {
                Report finalReport = createReport(((IReportTemplate)jList3.getSelectedValue()).getXmlFile(), jRadioButtonColumnarLayout.isSelected() ? 1 : 0);
                
                if (jRadioButton4.isSelected())
                {
                    Report report = MainFrame.getMainInstance().getActiveReportFrame().getReport();
                    Vector v = report.getParameters();
                    boolean found = false;
                    for (int i=0; i<report.getParameters().size(); ++i)
                    {
                        it.businesslogic.ireport.JRParameter param = (it.businesslogic.ireport.JRParameter)report.getParameters().elementAt(i);

                        if (param.getName().equals("SUBREPORT_DIR"))
                        {
                            found = true;

                            break;
                        }
                    }
                    if (!found)
                    {
                        it.businesslogic.ireport.JRParameter param = new it.businesslogic.ireport.JRParameter("SUBREPORT_DIR","java.lang.String");
                        //File f = new File( getReportFileName() );
                        
                        String s = MainFrame.getMainInstance().getTranslatedCompileDirectory();
                    
                        if (s.length() > 0 && !s.endsWith(File.separator)) s += File.separator;
                    
                        s = Misc.string_replace("\\\\","\\",s);
                        s = "\"" + s + "\"";
                        
                        /*
                        String s = f.getParent(); //Path();
                        if (s!= null)
                        {
                            if (s.length() > 0 && !s.endsWith(File.separator))
                            {
                                s += File.separator;
                            }
                        } 
                        else 
                        {
                            s = "";
                        }
                        s = Misc.string_replace("\\\\","\\",s);
                        s = "\"" + s + "\"";
                        */
                        param.setDefaultValueExpression(s);
                        report.addParameter( param );
                    }
                    getSubReportElement().setSubreportExpression(jLabel1.getText());
                }
                else
                {
                    getSubReportElement().setSubreportExpression(jLabel2e.getText());
                }
                getSubReportElement().setSubreportExpressionClass("java.lang.String");
                
                MainFrame.getMainInstance().openNewReportWindow(finalReport);
            
            } catch (Exception ex)
            {
                javax.swing.JOptionPane.showMessageDialog(getParent(),ex.getMessage()+"",
                        I18n.getString("message.title.error","Error"), javax.swing.JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        this.getWizardDialog().setVisible(false);
        this.getWizardDialog().dispose();
    }
    
    public boolean nextStep(int nextStep) {
        
        if (nextStep == 0) // First step == 0
        {
           
        } 
        else if (nextStep == 1) 
        {
           // We must retrive fields...
           wizardPanel.getJButtonNext().setEnabled(false);
           wizardPanel.getJButtonPrev().setEnabled(false);
           wizardPanel.getJButtonFinish().setEnabled(false);
           t = new Thread(this);
           t.start();
           return false;
        }
        else if (nextStep == 2) 
        {
            
           updateExpressionLabels();
        }
        return true;
    }

    public boolean previousStep(int previousStep) {
        return true;
    }

    public boolean canFinish(int currentStep) {
        
        if (currentStep<3) return false;
        if (jTextFieldReportFileName.getText().trim().length() > 0)
        {
            String s = jTextFieldReportFileName.getText().trim();
            String s2 = MainFrame.getMainInstance().getActiveReportFrame().getReport().getFilename();
            if (s.indexOf(File.separator) >=0) 
            {
                return (!s.equals(s2));
            }
            else
            {
                File f = new File(s2);
                return (!f.getName().equals(s));
            }
        }
        return false;
    }

    public boolean canNext(int currentStep) {
        
        if (currentStep == 0)
        {
             if (jComboBoxConnection.getSelectedItem() != null) {
                IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
            
                if (irc.isJDBCConnection()) {
                    if (jRSQLExpressionArea1.getText().length() > 0) return true;
                } else if (irc instanceof JRHibernateConnection) {
                    jLabel2.setText("HQL query");
                    jTextFieldBean.setVisible(false);
                    jRSQLExpressionArea1.setVisible(true);
                    if (jRSQLExpressionArea1.getText().length() > 0) return true;
                } else if (irc instanceof it.businesslogic.ireport.connection.JRDataSourceProviderConnection) {
                    return true;
                } else if (irc instanceof it.businesslogic.ireport.connection.JavaBeanDataSourceConnection) {
                    if (jTextFieldBean.getText().length() > 0) return  true;
                } else if (irc instanceof it.businesslogic.ireport.connection.JRCSVDataSourceConnection) {
                    return  true;
                } else if (irc instanceof it.businesslogic.ireport.connection.NullConnection) {
                    return  true;
                } else {
                    return false;
                }
            }
            return false;
        }
        else if (currentStep == 1)
        {
                 IReportConnection irc = (IReportConnection)jComboBoxConnection.getSelectedItem();
                 if (irc instanceof it.businesslogic.ireport.connection.NullConnection) return true;
                 return jList2.getModel().getSize() > 0;
        }
        else if (currentStep == 2)
        {
            return jList3.getSelectedIndex() >= 0;
        }
        return false;
    }

    public boolean canPrevious(int currentStep) {
        return (currentStep > 0);
    }

    public JPanel getStepPanel(int step) {

       if (step == 0) return jPanel0;
       if (step == 1) return jPanel1;
       if (step == 2) return jPanel2;
       if (step == 3) return jPanel3;
       //if (step == 4) return jPanel4;
       return  null;
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public SubReportElement getSubReportElement() {
        return subReportElement;
    }

    public void setSubReportElement(SubReportElement subReportElement) {
        this.subReportElement = subReportElement;
    }

    public BaseWizardPanel getWizardPanel() {
        return wizardPanel;
    }

    public void setWizardPanel(BaseWizardPanel wizardPanel) {
        this.wizardPanel = wizardPanel;
    }

    public javax.swing.JDialog getWizardDialog() {
        return wizardDialog;
    }

    public void setWizardDialog(javax.swing.JDialog wizardDialog) {
        this.wizardDialog = wizardDialog;
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
            
            DefaultListModel dlm1 = (DefaultListModel)jList1.getModel();
            dlm1.removeAllElements();
            
            DefaultListModel dlm2 = (DefaultListModel)jList2.getModel();
            dlm2.removeAllElements();
            // Get fields....
            java.util.List fields = WizardDialog.readFields(irc,jRSQLExpressionArea1.getText(), jTextFieldBean.getText());
            
            if (fields == null) 
            {
                cancelElaborationStep1();
                return;
            }
            
            for (int i=0; i <fields.size(); ++i) {
                it.businesslogic.ireport.JRField field = (JRField)fields.get(i);
                // Check if parameter already exists...
                boolean found = false;
                for (int k=0; i< dlm1.size(); ++k) {
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
                if (jList1.getModel().getSize()>0) jList1.setSelectedIndex(0);
            }
            
            wizardPanel.goStep(1);
            
        } catch (Exception ex) {
            java.io.StringWriter sw = new java.io.StringWriter();
            ex.printStackTrace(new java.io.PrintWriter( sw ));
            javax.swing.JOptionPane.showMessageDialog(getParent(),sw.getBuffer()+"",
                    I18n.getString("message.title.error","Error"), javax.swing.JOptionPane.ERROR_MESSAGE);
            cancelElaborationStep1();
            return;
        }
        
    }
    
    public void cancelElaborationStep1() {
            wizardPanel.updateButtons();
    }
    
    
    public void updateTemplatesList() {
        if (templates == null)
        {
            loadTemplateFiles();
             if (templates == null) return;
        }
        
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
    
    
    public void loadTemplateFiles()
    {
        templates = new Vector();
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
        
        
    }
    
    public void updateExpressionLabels()
    {
        String fileName = jTextFieldReportFileName.getText().trim();
        
        if (fileName.length() == 0)
        {
            jLabel1.setText("");
            jLabel2e.setText("");
        }
        else
        {
           File f = new File(fileName);

           String s = f.getName();
           if (!s.toLowerCase().endsWith(".jasper"))
           {
               if (s.lastIndexOf(".") > 0)
               {
                   s = s.substring(0,s.lastIndexOf("."));
               }
               s += ".jasper";
           }

           jLabel1.setText("$P{SUBREPORT_DIR} + \"" + s + "\"");

           String s2 = MainFrame.getMainInstance().getTranslatedCompileDirectory();
           f = new File( fileName );
           
           f = new File(s2, Misc.string_replace(".jasper",".jrxml",f.getName()));
           s2 = Misc.string_replace("\\\\","\\","" + f);
           jLabel2e.setText("\"" + s2 + "\"");
        }
        
        
    }
    
    /*
      *
      *  type=1 -> TABULAR
      *  type=2 -> COLUMNAR
      */
    public Report createReport(String templateFileName, int reportType)
    throws Exception {
        
        Report template = new Report(templateFileName);
        
        template.incrementReportChanges();
        
        String s = jTextFieldReportFileName.getText().trim();
        String s2 = MainFrame.getMainInstance().getActiveReportFrame().getReport().getFilename();
        if (s.indexOf(File.separator) >=0) 
        {
            template.setFilename(s);
        }
        else
        {
            File f = new File(s2);
            s2= f.getParent();
            if (s2.length() >0 && !s2.endsWith(File.separator)) s2 = s2+File.separator;
            s2 += s;
            template.setFilename(s2);
        }
        
        // remove all bands except column header and detail...
        
        //2. Find detail and column header bands...
        Band detail=null;
        Band columns=null;
        
        Enumeration e = template.getBands().elements();
        while (e.hasMoreElements()) {
            Band band = (Band)e.nextElement();
            if (band.getName().equals("detail")) {
                detail = band;
            } else if (band.getName().equals("columnHeader")) {
                columns = band;
            }
        }

        // 1. Normalize position to band...
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(0,- template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        if (reportType == 0) // TABULAR
        {
                //1. Adding fields...
                int currentx = template.getLeftMargin()+10;
                int currenty = 10;
                e = template.getElements().elements();
                StaticTextReportElement detailLabel = null;
                TextFieldReportElement detailField = null;
                while (e.hasMoreElements() && (detailLabel==null || detailField == null) ) {
                    ReportElement rElement = (ReportElement)e.nextElement();
                    if (rElement instanceof StaticTextReportElement) {
                        StaticTextReportElement stre = (StaticTextReportElement)rElement;
                        if (stre.getText().equalsIgnoreCase("DetailLabel"))
                            detailLabel =stre;
                    } else if (rElement instanceof TextFieldReportElement) {
                        TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                        if (tfre.getText().equalsIgnoreCase("DetailField"))
                            detailField = tfre;
                    }
                }

                if (detailField != null)
                    template.getElements().removeElement(detailField);
                if (detailLabel != null)
                    template.getElements().removeElement(detailLabel);

                if (detailField == null)
                    detailField = new TextFieldReportElement(0,0,100,20);
                if (detailLabel == null)
                    detailLabel = new StaticTextReportElement(0,0,100,18);

                int nfields = jList2.getModel().getSize();
                int fwidth = template.getColumnWidth()/ ( (nfields <= 0) ? 1 : nfields);

                for (int i=0; i<jList2.getModel().getSize(); ++i) {
                    // FIELD
                    it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)jList2.getModel().getElementAt(i);
                    template.addField(f);

                    TextFieldReportElement re = (TextFieldReportElement)detailField.cloneMe();
                    re.setPosition(new java.awt.Point(currentx,detailField.getPosition().y));
                    re.setWidth( fwidth);
                    re.updateBounds();
                    re.setText("$F{"+ f.getName() +"}");

                    re.setBand(detail);
                    re.setMatchingClassExpression( f.getClassType(), true );
                    template.getElements().addElement(re);

                    // COLUMN LABEL...
                    StaticTextReportElement sre = (StaticTextReportElement)detailLabel.cloneMe();
                    sre.setPosition(new java.awt.Point(currentx,detailLabel.getPosition().y));
                    sre.setWidth(fwidth);
                    sre.updateBounds();
                    sre.setText(""+ f.getName() +"");
                    sre.setBand(columns);
                    template.getElements().addElement(sre);

                    currentx += fwidth;
                    currentx = Math.min(template.getWidth() - template.getRightMargin()+10-30, currentx);
                }
        }
        else
        {
            //1. Adding fields...
                int currentx = template.getLeftMargin()+10;
                int currenty = 10;
                e = template.getElements().elements();
                StaticTextReportElement detailLabel = null;
                TextFieldReportElement detailField = null;
                while (e.hasMoreElements() && (detailLabel==null || detailField == null) ) {
                    ReportElement rElement = (ReportElement)e.nextElement();
                    if (rElement instanceof StaticTextReportElement) {
                        StaticTextReportElement stre = (StaticTextReportElement)rElement;
                        if (stre.getText().equalsIgnoreCase("DetailLabel"))
                            detailLabel =stre;
                    } else if (rElement instanceof TextFieldReportElement) {
                        TextFieldReportElement tfre = (TextFieldReportElement)rElement;
                        if (tfre.getText().equalsIgnoreCase("DetailField"))
                            detailField = tfre;
                    }
                }

                if (detailField != null)
                    template.getElements().removeElement(detailField);
                if (detailLabel != null)
                    template.getElements().removeElement(detailLabel);

                if (detailField == null)
                    detailField = new TextFieldReportElement(0,0,100,20);
                if (detailLabel == null)
                    detailLabel = new StaticTextReportElement(0,0,100,18);

                int nfields = jList2.getModel().getSize();

                int fwidth = template.getColumnWidth()/ ( (nfields <= 0) ? 1 : nfields);
                int ffheight =  detailField.getPosition().y;

                for (int i=0; i<jList2.getModel().getSize(); ++i) {
                    // FIELD
                    it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)jList2.getModel().getElementAt(i);
                    template.addField(f);

                    TextFieldReportElement re = (TextFieldReportElement)detailField.cloneMe();
                    re.setPosition(new java.awt.Point(detailField.getPosition().x, ffheight));
                    re.updateBounds();
                    re.setText("$F{"+ f.getName() +"}");

                    re.setBand(detail);
                    re.setMatchingClassExpression( f.getClassType(), true );

                    template.getElements().addElement(re);

                    // COLUMN LABEL...
                    StaticTextReportElement sre = (StaticTextReportElement)detailLabel.cloneMe();
                    sre.setPosition(new java.awt.Point(detailLabel.getPosition().x,ffheight));
                    sre.setWidth(fwidth);
                    sre.updateBounds();
                    sre.setText(""+ f.getName() +"");
                    sre.setBand(detail);
                    template.getElements().addElement(sre);

                    ffheight +=  detailField.position.y+detailField.height-10;
                }
                if (nfields != 0) detail.setHeight(ffheight);
        
        }
    
        
        // Remove template groups...****************
        e = template.getElements().elements();
        Vector elements_to_delete = new Vector();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            if (rElement.getBand() != detail && rElement.getBand() != columns)
            {
                elements_to_delete.addElement(rElement);
            }
        }
        
        
        
        e =elements_to_delete.elements();
        while (e.hasMoreElements()) {
            template.getElements().removeElement(e.nextElement());
        }
        
        Group g;
        if ((g=template.getGroupByName("Group1"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group2"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        if ((g=template.getGroupByName("Group3"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        if ((g=template.getGroupByName("Group4"))!=null) {
            template.getBands().removeElement(g.getGroupFooter());
            template.getBands().removeElement(g.getGroupHeader());
            template.getGroups().removeElement(g);
        }
        
        e = template.getBands().elements();
        while (e.hasMoreElements()) {
            Band band = (Band)e.nextElement();
            if (band != detail && band != columns)
            {
                band.setHeight(0);
            }
        }
        
        
                // Remove margins...

        int leftMargin = template.getLeftMargin();
        template.setWidth( template.getWidth() - template.getLeftMargin() - template.getRightMargin() );
        template.setLeftMargin(0);
        template.setRightMargin(0);
        template.setHeight( template.getHeight() - template.getTopMargin() - template.getBottomMargin() );
        template.setTopMargin(0);
        template.setBottomMargin(0);

        
        e = template.getElements().elements();
        while (e.hasMoreElements()) {
            ReportElement rElement = (ReportElement)e.nextElement();
            rElement.trasform(new java.awt.Point(- leftMargin, template.getBandYLocation( rElement.getBand() )),TransformationType.TRANSFORMATION_MOVE );
        }
        
        
        
        
        if (jComboBoxConnection.getSelectedItem() instanceof JDBCConnection)
        {
            getSubReportElement().setConnectionExpression("$P{REPORT_CONNECTION}");
            getSubReportElement().setUseConnection(true);
            template.setQuery( jRSQLExpressionArea1.getText() );
        
        }
        else if (jComboBoxConnection.getSelectedItem() instanceof JRHibernateConnection)
        {
            template.setQuery( jRSQLExpressionArea1.getText() );
            template.setQueryLanguage("hql");
        }
        else if (jComboBoxConnection.getSelectedItem() instanceof NullConnection)
        {
            template.setWhenNoDataType("AllSectionsNoDetail");
        }
        
        //System.out.println("Saving: " + template.getFilename());
        template.saveXMLFile();
        return template;
    }
    
 
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jRadioButton4.setText(I18n.getString("subreportWizardNewReport.radioButton4","Store the directory name in a parameter"));
                jRadioButton5.setText(I18n.getString("subreportWizardNewReport.radioButton5","Use a static absolute path reference"));
                jRadioButtonColumnarLayout.setText(I18n.getString("subreportWizardNewReport.radioButtonColumnarLayout","Columnar layout"));
                jRadioButtonTabularLayout.setText(I18n.getString("subreportWizardNewReport.radioButtonTabularLayout","Tabular layout"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButton1.setText(I18n.getString("subreportWizardNewReport.button1",">"));
                jButton2.setText(I18n.getString("subreportWizardNewReport.button2",">>"));
                jButton3.setText(I18n.getString("subreportWizardNewReport.button3","<<"));
                jButton4.setText(I18n.getString("subreportWizardNewReport.button4","<"));
                jButton5.setText(I18n.getString("subreportWizardNewReport.button5","Browse..."));
                jButtonLoadQuery.setText(I18n.getString("subreportWizardNewReport.buttonLoadQuery","Load query"));
                jButtonNewConnection.setText(I18n.getString("subreportWizardNewReport.buttonNewConnection","New"));
                jButtonSaveQuery.setText(I18n.getString("subreportWizardNewReport.buttonSaveQuery","Save query"));
                jLabel1.setText(I18n.getString("subreportWizardNewReport.label1","$P{SUBREPORT_DIR} + /name.jasper"));
                jLabel12.setText(I18n.getString("subreportWizardNewReport.label12","Connection / Datasource"));
                jLabel2.setText(I18n.getString("subreportWizardNewReport.label2","Query"));
                jLabel2e.setText(I18n.getString("subreportWizardNewReport.label2e","path reference"));
                jLabel3.setText(I18n.getString("subreportWizardNewReport.label3","Save the subreport as..."));
                jLabelImage.setText(I18n.getString("subreportWizardNewReport.labelImage","  "));
                // End autogenerated code ----------------------
    }
}


class TemplateFile {
    
    public File file = null;
    public String name = "";
    public String toString() { return name; }
}
