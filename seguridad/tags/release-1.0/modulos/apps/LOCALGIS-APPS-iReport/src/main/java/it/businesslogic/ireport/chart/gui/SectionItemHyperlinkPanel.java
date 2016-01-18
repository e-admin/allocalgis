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
 * SectionItemHyperlinkPanel.java
 * 
 * Created on September 1, 2006, 1:44 PM
 *
 */

package it.businesslogic.ireport.chart.gui;

import it.businesslogic.ireport.JRLinkParameter;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.chart.SectionItemHyperlink;
import it.businesslogic.ireport.gui.JRLinkParameterDialog;
import it.businesslogic.ireport.util.Misc;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.SwingUtilities;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class SectionItemHyperlinkPanel extends javax.swing.JPanel {
    
    private SectionItemHyperlink sectionItemHyperlink = null;
    
    private boolean init = false;
    
    
    /** Creates new form SectionItemHyperlink */
    public SectionItemHyperlinkPanel() {
        initComponents();
        sectionItemHyperlink = new SectionItemHyperlink();
        
        jComboBoxLinkType.addItem("None");
        jComboBoxLinkType.addItem("Reference");
        jComboBoxLinkType.addItem("LocalAnchor");
        jComboBoxLinkType.addItem("LocalPage");
        jComboBoxLinkType.addItem("RemoteAnchor");
        jComboBoxLinkType.addItem("RemotePage");
       
        // Barcode Evaluation Time...
        jComboBoxLinkTarget.addItem("Self");
        jComboBoxLinkTarget.addItem("Blank");
        
        this.jRTextExpressionAreaAnchor.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
        });
        
        this.jRTextExpressionAreaPage.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
        });
        
        this.jRTextExpressionAreaReference.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTooltip.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableLinkParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableLinkParametersListSelectionValueChanged(e);
            }
        });
        
        applyI18n();
    }

    public SectionItemHyperlink getSectionItemHyperlink() {
        return sectionItemHyperlink;
    }
    
    
    public void setSubDataset( SubDataset sds )
    {
        jRTextExpressionAreaReference.setSubDataset(sds);
        jRTextExpressionAreaAnchor.setSubDataset(sds);
        jRTextExpressionAreaPage.setSubDataset(sds);
        jRTextExpressionAreaTooltip.setSubDataset(sds);
    }

    public void setSectionItemHyperlink(SectionItemHyperlink sectionItemHyperlink) {
        try {
            setInit(true);
        
            this.sectionItemHyperlink = sectionItemHyperlink;
       
        // Fill the hyperlink panel...
            jComboBoxLinkTarget.setSelectedItem( sectionItemHyperlink.getHyperlinkTarget() );
            jComboBoxLinkType.setSelectedItem( sectionItemHyperlink.getHyperlinkType() );
            jRTextExpressionAreaReference.setText( sectionItemHyperlink.getHyperlinkReferenceExpression() );
            jRTextExpressionAreaAnchor.setText( sectionItemHyperlink.getHyperlinkAnchorExpression() );
            jRTextExpressionAreaPage.setText( sectionItemHyperlink.getHyperlinkPageExpression() );
            jRTextExpressionAreaTooltip.setText( sectionItemHyperlink.getHyperlinkTooltipExpression() );
            
            jTabbedPane2.removeAll();
            
            //Adjusting hyperlink combinations...
            if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("None")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
             } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("Reference")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalAnchor")) {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);

                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalPage")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);

                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            }
            else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemoteAnchor")) {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemotePage")) {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            } else {
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);

                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            }
            
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Tooltip","Tooltip"), this.jPanelTooltip);
            
            // Adding parameters...  
            java.util.Iterator enum_parameters = sectionItemHyperlink.getHyperlinkParameters().iterator();
            
            javax.swing.table.DefaultTableModel dtmLinkParams = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtmLinkParams.setRowCount(0);
            
            while (enum_parameters.hasNext()) {
                it.businesslogic.ireport.JRLinkParameter parameter = (it.businesslogic.ireport.JRLinkParameter)enum_parameters.next();
                Vector row = new Vector();
                row.addElement(parameter);
                row.addElement(parameter.getExpression());
                dtmLinkParams.addRow(row);
            }
        } finally {
            
            setInit(false);
        }
    }

    
    public void jTableLinkParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableLinkParameters.getSelectedRowCount() > 0) {
            this.jButtonModLinkParameter.setEnabled(true);
            this.jButtonRemLinkParameter.setEnabled(true);
        } else {
            this.jButtonModLinkParameter.setEnabled(false);
            this.jButtonRemLinkParameter.setEnabled(false);
        }
    }
    
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelTarget = new javax.swing.JLabel();
        jComboBoxLinkTarget = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jComboBoxLinkType = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelReference = new javax.swing.JPanel();
        jLabelReference = new javax.swing.JLabel();
        jRTextExpressionAreaReference = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelAnchor = new javax.swing.JPanel();
        jLabelAnchor = new javax.swing.JLabel();
        jRTextExpressionAreaAnchor = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelPage = new javax.swing.JPanel();
        jLabelPage = new javax.swing.JLabel();
        jRTextExpressionAreaPage = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelLinkParams = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLinkParameters = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButtonAddLinkParameter = new javax.swing.JButton();
        jButtonModLinkParameter = new javax.swing.JButton();
        jButtonRemLinkParameter = new javax.swing.JButton();
        jPanelTooltip = new javax.swing.JPanel();
        jLabelReference1 = new javax.swing.JLabel();
        jRTextExpressionAreaTooltip = new it.businesslogic.ireport.gui.JRTextExpressionArea();

        setLayout(new java.awt.GridBagLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabelTarget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTarget.setText("Hyperlink target");
        jLabelTarget.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabelTarget.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabelTarget.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 5, 0);
        add(jLabelTarget, gridBagConstraints);

        jComboBoxLinkTarget.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTargetActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 2);
        add(jComboBoxLinkTarget, gridBagConstraints);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Hyperlink type");
        jLabel36.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel36.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel36.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 0);
        add(jLabel36, gridBagConstraints);

        jComboBoxLinkType.setEditable(true);
        jComboBoxLinkType.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTypeActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        add(jComboBoxLinkType, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanelReference.setLayout(new java.awt.GridBagLayout());

        jLabelReference.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference.setText("Hyperlink Reference Expression");
        jLabelReference.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jLabelReference, gridBagConstraints);

        jRTextExpressionAreaReference.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaReference.setEnabled(false);
        jRTextExpressionAreaReference.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaReference.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaReference.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jRTextExpressionAreaReference, gridBagConstraints);

        jTabbedPane2.addTab("Reference", jPanelReference);

        jPanelAnchor.setLayout(new java.awt.GridBagLayout());

        jLabelAnchor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAnchor.setText("Hyperlink Anchor Expression");
        jLabelAnchor.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jLabelAnchor, gridBagConstraints);

        jRTextExpressionAreaAnchor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaAnchor.setEnabled(false);
        jRTextExpressionAreaAnchor.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaAnchor.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaAnchor.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jRTextExpressionAreaAnchor, gridBagConstraints);

        jTabbedPane2.addTab("Anchor", jPanelAnchor);

        jPanelPage.setLayout(new java.awt.GridBagLayout());

        jLabelPage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelPage.setText("Hyperlink Page Expression");
        jLabelPage.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jLabelPage, gridBagConstraints);

        jRTextExpressionAreaPage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaPage.setEnabled(false);
        jRTextExpressionAreaPage.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaPage.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaPage.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jRTextExpressionAreaPage, gridBagConstraints);

        jTabbedPane2.addTab("Page", jPanelPage);

        jPanelLinkParams.setLayout(new java.awt.GridBagLayout());

        jTableLinkParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter name", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLinkParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLinkParametersMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTableLinkParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelLinkParams.add(jScrollPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jButtonAddLinkParameter.setText("Add");
        jButtonAddLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLinkParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel3.add(jButtonAddLinkParameter, gridBagConstraints);

        jButtonModLinkParameter.setText("Modify");
        jButtonModLinkParameter.setEnabled(false);
        jButtonModLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModLinkParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel3.add(jButtonModLinkParameter, gridBagConstraints);

        jButtonRemLinkParameter.setText("Remove");
        jButtonRemLinkParameter.setEnabled(false);
        jButtonRemLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemLinkParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel3.add(jButtonRemLinkParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelLinkParams.add(jPanel3, gridBagConstraints);

        jTabbedPane2.addTab("Link parameters", jPanelLinkParams);

        jPanelTooltip.setLayout(new java.awt.GridBagLayout());

        jLabelReference1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference1.setText("Tooltip Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jLabelReference1, gridBagConstraints);

        jRTextExpressionAreaTooltip.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTooltip.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTooltip.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTooltip.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jRTextExpressionAreaTooltip, gridBagConstraints);

        jTabbedPane2.addTab("Tooltip", jPanelTooltip);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jTabbedPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

        
        
        
    }//GEN-LAST:event_formComponentShown

    private void jTableLinkParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLinkParametersMouseClicked

        
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 && jTableLinkParameters.getSelectedRowCount() > 0)
        {
            jButtonModLinkParameterActionPerformed(null);
        }
        
    }//GEN-LAST:event_jTableLinkParametersMouseClicked

    private void jButtonRemLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemLinkParameterActionPerformed
        if (this.isInit()) return;
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
        int[]  rows= jTableLinkParameters.getSelectedRows();
        for (int i=rows.length-1; i>=0; --i) {
            getSectionItemHyperlink().getHyperlinkParameters().remove( jTableLinkParameters.getValueAt( rows[i], 0) );
            dtm.removeRow(rows[i]);
        }
    }//GEN-LAST:event_jButtonRemLinkParameterActionPerformed

    private void jButtonModLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModLinkParameterActionPerformed
        if (this.isInit()) return;
        
        if (jTableLinkParameters.getSelectedRowCount() <= 0) return;
        
        JRLinkParameter parameter = (JRLinkParameter)jTableLinkParameters.getValueAt( jTableLinkParameters.getSelectedRow(), 0);
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog((javax.swing.JDialog)SwingUtilities.getWindowAncestor(this), true);
        jrpd.setParameter( parameter );
        if (evt != null && evt.getID() > 0)
        {
            jrpd.setFocusedExpression(evt.getID());
        }
        
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            
            dtm.setValueAt(parameter,jTableLinkParameters.getSelectedRow(),0);
            dtm.setValueAt(parameter.getExpression(),jTableLinkParameters.getSelectedRow(),1);
            
            jTableLinkParameters.updateUI();
        }
    }//GEN-LAST:event_jButtonModLinkParameterActionPerformed

    private void jButtonAddLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLinkParameterActionPerformed
        if (this.isInit()) return;
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog((javax.swing.JDialog)SwingUtilities.getWindowAncestor(this), true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRLinkParameter parameter = jrpd.getParameter();
            
            getSectionItemHyperlink().getHyperlinkParameters().add( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getExpression()});
        }
    }//GEN-LAST:event_jButtonAddLinkParameterActionPerformed

    private void jComboBoxLinkTypeActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTypeActionPerformed1
        if (this.isInit()) return;
        
        jTabbedPane2.removeAll();
        
        if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("None")) {
            // Set to blank all link fields...
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            getSectionItemHyperlink().getHyperlinkParameters().clear();
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            //this.jPanelAnchor.setVisible(false);
            //this.jPanelPage.setVisible(false);
            //this.jPanelReference.setVisible(false);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("Reference")) {
            // Set to blank all link fields...
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"), this.jPanelReference);
            //this.jPanelAnchor.setVisible(false);
            //this.jPanelPage.setVisible(false);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            
            
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalAnchor")) {
            // Set to blank all link fields...
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalPage")) {
            // Set to blank all link fields...
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(false);
            this.jLabelReference.setEnabled(false);
            
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
        }
        
        else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemoteAnchor")) {
            // Set to blank all link fields...
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(false);
            this.jLabelPage.setEnabled(false);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemotePage")) {
            // Set to blank all link fields...
            
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            getSectionItemHyperlink().setHyperlinkReferenceExpression("");
            getSectionItemHyperlink().setHyperlinkAnchorExpression("");
            getSectionItemHyperlink().setHyperlinkPageExpression("");
            this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(false);
            this.jLabelAnchor.setEnabled(false);
            this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
        } else {
            // Set to blank all link fields...
            
            getSectionItemHyperlink().setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
            //element.setHyperlinkReferenceExpression("");
            //element.setHyperlinkAnchorExpression("");
            //element.setHyperlinkPageExpression("");
            //this.jRTextExpressionAreaAnchor.setText("");
            this.jRTextExpressionAreaAnchor.setEnabled(true);
            this.jLabelAnchor.setEnabled(true);
            //this.jRTextExpressionAreaPage.setText("");
            this.jRTextExpressionAreaPage.setEnabled(true);
            this.jLabelPage.setEnabled(true);
            //this.jRTextExpressionAreaReference.setText("");
            this.jRTextExpressionAreaReference.setEnabled(true);
            this.jLabelReference.setEnabled(true);
            
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"),this.jPanelLinkParams);
        }
        
        jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Tooltip","Tooltip"), this.jPanelTooltip);
    }//GEN-LAST:event_jComboBoxLinkTypeActionPerformed1

    private void jComboBoxLinkTargetActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTargetActionPerformed1
        if (this.isInit()) return;
        
        getSectionItemHyperlink().setHyperlinkTarget( ""+jComboBoxLinkTarget.getSelectedItem());
    }//GEN-LAST:event_jComboBoxLinkTargetActionPerformed1
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddLinkParameter;
    private javax.swing.JButton jButtonModLinkParameter;
    private javax.swing.JButton jButtonRemLinkParameter;
    private javax.swing.JComboBox jComboBoxLinkTarget;
    private javax.swing.JComboBox jComboBoxLinkType;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabelAnchor;
    private javax.swing.JLabel jLabelPage;
    private javax.swing.JLabel jLabelReference;
    private javax.swing.JLabel jLabelReference1;
    private javax.swing.JLabel jLabelTarget;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAnchor;
    private javax.swing.JPanel jPanelLinkParams;
    private javax.swing.JPanel jPanelPage;
    private javax.swing.JPanel jPanelReference;
    private javax.swing.JPanel jPanelTooltip;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaAnchor;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaPage;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaReference;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTooltip;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableLinkParameters;
    // End of variables declaration//GEN-END:variables
    
    
     public void jRTextExpressionAreaTooltipTextChanged() {
        if (this.isInit()) return;
        getSectionItemHyperlink().setHyperlinkTooltipExpression( ""+jRTextExpressionAreaTooltip.getText());
    }
    
    public void jRTextExpressionAreaReferenceTextChanged() {
        if (this.isInit()) return;
        getSectionItemHyperlink().setHyperlinkReferenceExpression( ""+jRTextExpressionAreaReference.getText());
    }
    
    public void jRTextExpressionAreaAnchorTextChanged() {
        if (this.isInit()) return;
        getSectionItemHyperlink().setHyperlinkAnchorExpression( ""+jRTextExpressionAreaAnchor.getText());
    }
    
    public void jRTextExpressionAreaPageTextChanged() {
        if (this.isInit()) return;
        getSectionItemHyperlink().setHyperlinkPageExpression( ""+jRTextExpressionAreaPage.getText());
    }
    public void applyI18n(){
            
            // Start autogenerated code ----------------------
            jButtonAddLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonAddLinkParameter","Add"));
            jButtonModLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonModLinkParameter","Modify"));
            jButtonRemLinkParameter.setText(I18n.getString("sectionItemHyperlinkPanel.buttonRemLinkParameter","Remove"));
            jLabel36.setText(I18n.getString("sectionItemHyperlinkPanel.label36","Hyperlink type"));
            jLabelAnchor.setText(I18n.getString("sectionItemHyperlinkPanel.labelAnchor","Hyperlink Anchor Expression"));
            jLabelPage.setText(I18n.getString("sectionItemHyperlinkPanel.labelPage","Hyperlink Page Expression"));
            jLabelReference.setText(I18n.getString("sectionItemHyperlinkPanel.labelReference","Hyperlink Reference Expression"));
            jLabelReference1.setText(I18n.getString("sectionItemHyperlinkPanel.labelReference1","Tooltip Expression"));
            jLabelTarget.setText(I18n.getString("sectionItemHyperlinkPanel.labelTarget","Hyperlink target"));
            // End autogenerated code ----------------------

            jTableLinkParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("sectionItemHyperlinkPanel.tablecolumn.parameterName","Parameter name") );
            jTableLinkParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("sectionItemHyperlinkPanel.tablecolumn.expression","Expression") );

    }
    
    public static final int COMPONENT_NONE=0;
    //public static final int COMPONENT_ANCHORNAME_EXPRESSION=1;
    public static final int COMPONENT_HYPERLINK_REFERENCE_EXPRESSION=2;
    public static final int COMPONENT_HYPERLINK_ANCHOR_EXPRESSION=3;
    public static final int COMPONENT_HYPERLINK_PAGE_EXPRESSION=4;
    public static final int COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION=5;
    public static final int COMPONENT_HYPERLINK_PARAMETERS=6;
        
    
    public int openParameterWithThisExpression = -1;
    /**
     * This method set the focus on a specific component.
     * 
     * For this kind of datasource otherInfo must be in the format suitable for sectionItemHyperlinkPanel1
     * expressionInfo[0] must be an Integer holding the expression ID<br>
     * if (expressionInfo[0] == COMPONENT_HYPERLINK_PARAMETERS) then expressionInfo[1] must contain an Integer
     * with the index of the parameter, and expressionInfo[2] the index of the expression to highlight for
     * that parameter. The window will open as soon this component will become "shown". 
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        try {
            switch (expID)
            {
                case COMPONENT_HYPERLINK_REFERENCE_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelReference );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaReference);
                    break;  
                case COMPONENT_HYPERLINK_ANCHOR_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelAnchor );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaAnchor);
                    break;  
                case COMPONENT_HYPERLINK_PAGE_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelPage );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaPage);
                    break;  
                case COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION:
                    this.jTabbedPane2.setSelectedComponent(  jPanelTooltip );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaTooltip);
                    break;
                case COMPONENT_HYPERLINK_PARAMETERS:
                    this.jTabbedPane2.setSelectedComponent(  jPanelLinkParams );
                    int paramIdex = ((Integer)expressionInfo[1]).intValue();
                    int paramExpId = ((Integer)expressionInfo[2]).intValue();
                    if (paramIdex >= 0 && jTableLinkParameters.getRowCount() > paramIdex)
                    {
                        jTableLinkParameters.setRowSelectionInterval(paramIdex,paramIdex);
                        openParameterWithThisExpression = paramExpId;
                    }
                    break;
            }
        } catch (Exception ex) { }
    }    
    
    public void openExtraWindows()
    {
        if (openParameterWithThisExpression >= 0)
        {
            jButtonModLinkParameterActionPerformed(new ActionEvent(jButtonModLinkParameter,openParameterWithThisExpression,""));
        }
        openParameterWithThisExpression = -1;
    }
}
