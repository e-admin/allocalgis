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
 * CrosstabPropertiesDialog.java
 * 
 * Created on 4 gennaio 2006, 9.35
 *
 */

package it.businesslogic.ireport.crosstab.gui;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.JRSubreportParameter;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.chart.Dataset;
import it.businesslogic.ireport.chart.gui.ChartPropertiesDialog;
import it.businesslogic.ireport.crosstab.CrosstabColumnGroup;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabRowGroup;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.JRSubreportParameterDialog;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
/**
 *
 * @author  Administrator
 */
public class CrosstabPropertiesDialog extends javax.swing.JDialog {
    
    private CrosstabReportElement currentCrosstabReportElement = null;
    private int dialogResult = javax.swing.JOptionPane.OK_OPTION;
    private JReportFrame jReportFrame = null;
    private boolean init = false;
    
    /** Creates new form CrosstabPropertiesDialog */
    public CrosstabPropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    /** Creates new form CrosstabPropertiesDialog */
    public CrosstabPropertiesDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    public void initAll()
    {
        initComponents();
        
        applyI18n();
        
        this.jComboBoxResetType.addItem(new Tag("None",it.businesslogic.ireport.util.I18n.getString("resetType.None","None")));
        this.jComboBoxResetType.addItem(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("resetType.Report","Report")));
        this.jComboBoxResetType.addItem(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("resetType.Page","Page")));
        this.jComboBoxResetType.addItem(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("resetType.Column","Column")));
        this.jComboBoxResetType.addItem(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("resetType.Group","Group")));

        this.jComboBoxIncrementType.addItem(new Tag("None",it.businesslogic.ireport.util.I18n.getString("incrementType.None","None")));
        this.jComboBoxIncrementType.addItem(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("incrementType.Report","Report")));
        this.jComboBoxIncrementType.addItem(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("incrementType.Page","Page")));
        this.jComboBoxIncrementType.addItem(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("incrementType.Column","Column")));
        this.jComboBoxIncrementType.addItem(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("incrementType.Group","Group")));
         
        jComboBoxDatasetConnectionType.addItem(new Tag("Don't use connection or datasource",it.businesslogic.ireport.util.I18n.getString("ConnectionType.1","Don't use connection or datasource")));
        jComboBoxDatasetConnectionType.addItem(new Tag("Use connection expression",it.businesslogic.ireport.util.I18n.getString("ConnectionType.2","Use connection expression")));
        jComboBoxDatasetConnectionType.addItem(new Tag("Use datasource expression",it.businesslogic.ireport.util.I18n.getString("ConnectionType.3","Use datasource expression")));
        
        this.jComboBoxrunDirection.addItem(new Tag("LTR",it.businesslogic.ireport.util.I18n.getString("runDirection.LTR","Left to Right")));
        this.jComboBoxrunDirection.addItem(new Tag("RTL",it.businesslogic.ireport.util.I18n.getString("runDirection.RTL","Right to Left")));
        
        
        this.jRTextExpressionAreaMapExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaFilterExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTextConnectionExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableMeasures.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableMeasuresListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableRowGroups.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableRowGroupsListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableColumnGroups.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableColumnGroupsListSelectionValueChanged(e);
            }
        });
        
        this.jRTextExpressionAreaMapExpression.setCrosstabElements( new Vector());
        this.jRTextExpressionAreaTextConnectionExpression.setCrosstabElements( new Vector());
        if (MainFrame.getMainInstance().getActiveReportFrame() != null)
        {
            this.jRTextExpressionAreaMapExpression.setSubDataset( MainFrame.getMainInstance().getActiveReportFrame().getReport());
            this.jRTextExpressionAreaTextConnectionExpression.setSubDataset( MainFrame.getMainInstance().getActiveReportFrame().getReport());
        }
        this.pack();
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        
    }
    
    
    
    public void applyI18n()
    {
                // Start autogenerated code ----------------------
                jCheckBoxPreSorted.setText(I18n.getString("crosstabPropertiesDialog.checkBoxPreSorted","Data is PreSorted"));
                jCheckBoxUseDataset.setText(I18n.getString("crosstabPropertiesDialog.checkBoxUseDataset","Use a dataset to fill the crosstab"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButtonAddColumnGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonAddColumnGroup","Add"));
                jButtonAddMeasure.setText(I18n.getString("crosstabPropertiesDialog.buttonAddMeasure","Add"));
                jButtonAddParameter.setText(I18n.getString("crosstabPropertiesDialog.buttonAddParameter","Add"));
                jButtonAddRowGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonAddRowGroup","Add"));
                jButtonDownColumnGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonDownColumnGroup","Down"));
                jButtonDownRowGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonDownRowGroup","Down"));
                jButtonModColumnGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonModColumnGroup","Modify"));
                jButtonModMeasure.setText(I18n.getString("crosstabPropertiesDialog.buttonModMeasure","Modify"));
                jButtonModParameter.setText(I18n.getString("crosstabPropertiesDialog.buttonModParameter","Modify"));
                jButtonModRowGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonModRowGroup","Modify"));
                jButtonRemColumnGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonRemColumnGroup","Remove"));
                jButtonRemMeasure.setText(I18n.getString("crosstabPropertiesDialog.buttonRemMeasure","Remove"));
                jButtonRemParameter.setText(I18n.getString("crosstabPropertiesDialog.buttonRemParameter","Remove"));
                jButtonRemRowGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonRemRowGroup","Remove"));
                jButtonUpColumnGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonUpColumnGroup","Up"));
                jButtonUpRowGroup.setText(I18n.getString("crosstabPropertiesDialog.buttonUpRowGroup","Up"));
                jLabel1.setText(I18n.getString("crosstabPropertiesDialog.label1","Row groups"));
                jLabel2.setText(I18n.getString("crosstabPropertiesDialog.label2","Column groups"));
                jLabel26.setText(I18n.getString("crosstabPropertiesDialog.label26","Parameters Map Expression"));
                jLabel41.setText(I18n.getString("crosstabPropertiesDialog.label41","Connection / Datasource Expression"));
                jLabelIncrementType1.setText(I18n.getString("crosstabPropertiesDialog.labelIncrementType1","Sub dataset"));
                jLabelIncrementWhenExpression.setText(I18n.getString("crosstabPropertiesDialog.labelIncrementWhenExpression","Increment When expression"));
                
                jLabelRunDirection.setText(I18n.getString("crosstabPropertiesDialog.labelRunDirection","Run direction"));
                // End autogenerated code ----------------------

        jLabelResetType.setText( it.businesslogic.ireport.util.I18n.getString("resetType","Reset type"));
        jLabelResetGroup.setText( it.businesslogic.ireport.util.I18n.getString("resetGroup","Reset group"));
        jLabelIncrementType.setText( it.businesslogic.ireport.util.I18n.getString("incrementType","Increment type"));
        jLabelIncrementGroup.setText( it.businesslogic.ireport.util.I18n.getString("incrementGroup","Increment group"));

        jButtonCopy.setText( it.businesslogic.ireport.util.I18n.getString("charts.copyDataset","Copy dataset"));
        jButtonPaste.setText( it.businesslogic.ireport.util.I18n.getString("charts.pasteDataset","Paste dataset"));

        jTableDatasetParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.parameterName","Parameter name") );
        jTableDatasetParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.expression","Expression") );
        jTableRowGroups.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.rowGroup","Row group") );
        jTableColumnGroups.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.columnGroup","Column group") );
        jTableMeasures.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.measure","Measure") );
        jTableMeasures.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("crosstabPropertiesDialog.tablecolumn.class","Class") );

        jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("crosstabPropertiesDialog.tab.CrosstabData","Crosstab data"));
        jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("crosstabPropertiesDialog.tab.RowAndColumnGroups","Row and column groups"));
        jTabbedPane1.setTitleAt(2, it.businesslogic.ireport.util.I18n.getString("crosstabPropertiesDialog.tab.Measures","Measures"));
        
        
        jTabbedPane2.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.Parameters","Parameters"));
        jTabbedPane2.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ParametersMapExp","Parameters map exp."));
        jTabbedPane2.setTitleAt(2, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ConnectionDatasourceExp","Connection/Datasource exp."));
        
        ((javax.swing.border.TitledBorder)jPanelDataset.getBorder()).setTitle( I18n.getString("datasetRun.panelBorder.Bucket","Dataset") );
        ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( I18n.getString("datasetRun.panelBorder.DatasetRun","Dataset Run") );
        
        this.setTitle(it.businesslogic.ireport.util.I18n.getString("gui.CrosstabPropertiesDialog.title","Crosstab properties"));
        this.getRootPane().updateUI();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanelDataset = new javax.swing.JPanel();
        jLabelResetType = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jLabelResetGroup = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabelIncrementType = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jLabelIncrementGroup = new javax.swing.JLabel();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabelIncrementType1 = new javax.swing.JLabel();
        jComboBoxSubDataset = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaMapExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxDatasetConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel2 = new javax.swing.JPanel();
        jButtonCopy = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxPreSorted = new javax.swing.JCheckBox();
        jLabelIncrementWhenExpression = new javax.swing.JLabel();
        jRTextExpressionAreaFilterExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jCheckBoxUseDataset = new javax.swing.JCheckBox();
        jPanel23 = new javax.swing.JPanel();
        jLabelRunDirection = new javax.swing.JLabel();
        jComboBoxrunDirection = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableRowGroups = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jButtonAddRowGroup = new javax.swing.JButton();
        jButtonModRowGroup = new javax.swing.JButton();
        jButtonRemRowGroup = new javax.swing.JButton();
        jButtonUpRowGroup = new javax.swing.JButton();
        jButtonDownRowGroup = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableColumnGroups = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jButtonAddColumnGroup = new javax.swing.JButton();
        jButtonModColumnGroup = new javax.swing.JButton();
        jButtonRemColumnGroup = new javax.swing.JButton();
        jButtonUpColumnGroup = new javax.swing.JButton();
        jButtonDownColumnGroup = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableMeasures = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jButtonAddMeasure = new javax.swing.JButton();
        jButtonModMeasure = new javax.swing.JButton();
        jButtonRemMeasure = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crosstab properties");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanelDataset.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dataset", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11)));
        jPanelDataset.setPreferredSize(new java.awt.Dimension(460, 420));
        jPanelDataset.setLayout(new java.awt.GridBagLayout());

        jLabelResetType.setText("Reset type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetType, gridBagConstraints);

        jComboBoxResetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetType, gridBagConstraints);

        jLabelResetGroup.setText("Reset group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetGroup, gridBagConstraints);

        jComboBoxResetGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetGroup, gridBagConstraints);

        jLabelIncrementType.setText("Increment type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType, gridBagConstraints);

        jComboBoxIncrementType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementType, gridBagConstraints);

        jLabelIncrementGroup.setText("Increment group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementGroup, gridBagConstraints);

        jComboBoxIncrementGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementGroup, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dataset run", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11)));
        jPanel1.setMinimumSize(new java.awt.Dimension(329, 180));
        jPanel1.setPreferredSize(new java.awt.Dimension(430, 180));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelIncrementType1.setText("Sub dataset");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel1.add(jLabelIncrementType1, gridBagConstraints);

        jComboBoxSubDataset.setMinimumSize(new java.awt.Dimension(23, 22));
        jComboBoxSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubDatasetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jComboBoxSubDataset, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDatasetParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDatasetParametersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDatasetParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText("Add");
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonAddParameter, gridBagConstraints);
        jButtonAddParameter.getAccessibleContext().setAccessibleName("");

        jButtonModParameter.setText("Modify");
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonModParameter, gridBagConstraints);
        jButtonModParameter.getAccessibleContext().setAccessibleName("");

        jButtonRemParameter.setText("Remove");
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonRemParameter, gridBagConstraints);
        jButtonRemParameter.getAccessibleContext().setAccessibleName("");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jPanel13, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel16.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel16, gridBagConstraints);

        jTabbedPane2.addTab("Parameters", jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Parameters Map Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanel5.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaMapExpression.setElectricScroll(0);
        jRTextExpressionAreaMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel5.add(jRTextExpressionAreaMapExpression, gridBagConstraints);

        jTabbedPane2.addTab("Parameters map exp", jPanel5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText("Connection / Datasource Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel6.add(jLabel41, gridBagConstraints);

        jComboBoxDatasetConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetConnectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel6.add(jComboBoxDatasetConnectionType, gridBagConstraints);

        jRTextExpressionAreaTextConnectionExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
        jRTextExpressionAreaTextConnectionExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTextConnectionExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel6.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jTabbedPane2.addTab("Connection/Datasource exp", jPanel6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel7.add(jTabbedPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonCopy.setText("Copy dataset");
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel2.add(jButtonCopy, gridBagConstraints);

        jButtonPaste.setText("Paste dataset");
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPaste, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 101;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelDataset.add(jPanel2, gridBagConstraints);

        jCheckBoxPreSorted.setText("Data is PreSorted");
        jCheckBoxPreSorted.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxPreSorted.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxPreSorted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPreSortedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jCheckBoxPreSorted, gridBagConstraints);

        jLabelIncrementWhenExpression.setText("Increment When expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementWhenExpression, gridBagConstraints);

        jRTextExpressionAreaFilterExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaFilterExpression.setCaretVisible(false);
        jRTextExpressionAreaFilterExpression.setElectricScroll(0);
        jRTextExpressionAreaFilterExpression.setMinimumSize(new java.awt.Dimension(400, 50));
        jRTextExpressionAreaFilterExpression.setPreferredSize(new java.awt.Dimension(400, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelDataset.add(jRTextExpressionAreaFilterExpression, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel8.add(jPanelDataset, gridBagConstraints);

        jCheckBoxUseDataset.setText("Use a dataset to fill the crosstab");
        jCheckBoxUseDataset.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxUseDataset.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxUseDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseDatasetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 4);
        jPanel8.add(jCheckBoxUseDataset, gridBagConstraints);

        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabelRunDirection.setText("Run direction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel23.add(jLabelRunDirection, gridBagConstraints);

        jComboBoxrunDirection.setMinimumSize(new java.awt.Dimension(300, 18));
        jComboBoxrunDirection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxrunDirectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel23.add(jComboBoxrunDirection, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel8.add(jPanel23, gridBagConstraints);

        jTabbedPane1.addTab("Crosstab data", jPanel8);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Row groups");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 6);
        jPanel9.add(jLabel1, gridBagConstraints);

        jPanel18.setLayout(new java.awt.GridBagLayout());

        jScrollPane4.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableRowGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Row group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableRowGroups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRowGroupsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableRowGroups);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel18.add(jScrollPane4, gridBagConstraints);

        jPanel15.setLayout(new java.awt.GridBagLayout());

        jButtonAddRowGroup.setText("Add");
        jButtonAddRowGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddRowGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel15.add(jButtonAddRowGroup, gridBagConstraints);

        jButtonModRowGroup.setText("Modify");
        jButtonModRowGroup.setEnabled(false);
        jButtonModRowGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModRowGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel15.add(jButtonModRowGroup, gridBagConstraints);

        jButtonRemRowGroup.setText("Remove");
        jButtonRemRowGroup.setEnabled(false);
        jButtonRemRowGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemRowGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel15.add(jButtonRemRowGroup, gridBagConstraints);

        jButtonUpRowGroup.setText("Up");
        jButtonUpRowGroup.setEnabled(false);
        jButtonUpRowGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpRowGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel15.add(jButtonUpRowGroup, gridBagConstraints);

        jButtonDownRowGroup.setText("Down");
        jButtonDownRowGroup.setEnabled(false);
        jButtonDownRowGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownRowGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel15.add(jButtonDownRowGroup, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 99;
        gridBagConstraints.weightx = 1.0;
        jPanel15.add(jPanel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel18.add(jPanel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel9.add(jPanel18, gridBagConstraints);

        jPanel20.setLayout(new java.awt.GridBagLayout());

        jScrollPane5.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableColumnGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Column group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableColumnGroups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableColumnGroupsMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableColumnGroups);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel20.add(jScrollPane5, gridBagConstraints);

        jPanel21.setLayout(new java.awt.GridBagLayout());

        jButtonAddColumnGroup.setText("Add");
        jButtonAddColumnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddColumnGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel21.add(jButtonAddColumnGroup, gridBagConstraints);

        jButtonModColumnGroup.setText("Modify");
        jButtonModColumnGroup.setEnabled(false);
        jButtonModColumnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModColumnGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel21.add(jButtonModColumnGroup, gridBagConstraints);

        jButtonRemColumnGroup.setText("Remove");
        jButtonRemColumnGroup.setEnabled(false);
        jButtonRemColumnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemColumnGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel21.add(jButtonRemColumnGroup, gridBagConstraints);

        jButtonUpColumnGroup.setText("Up");
        jButtonUpColumnGroup.setEnabled(false);
        jButtonUpColumnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpColumnGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel21.add(jButtonUpColumnGroup, gridBagConstraints);

        jButtonDownColumnGroup.setText("Down");
        jButtonDownColumnGroup.setEnabled(false);
        jButtonDownColumnGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownColumnGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel21.add(jButtonDownColumnGroup, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 99;
        gridBagConstraints.weightx = 1.0;
        jPanel21.add(jPanel22, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel20.add(jPanel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel9.add(jPanel20, gridBagConstraints);

        jLabel2.setText("Column groups");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 6);
        jPanel9.add(jLabel2, gridBagConstraints);

        jTabbedPane1.addTab("Row and column groups", jPanel9);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jPanel17.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableMeasures.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Measure", "Class"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMeasures.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMeasuresMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableMeasures);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel17.add(jScrollPane3, gridBagConstraints);

        jPanel12.setLayout(new java.awt.GridBagLayout());

        jButtonAddMeasure.setText("Add");
        jButtonAddMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddMeasureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel12.add(jButtonAddMeasure, gridBagConstraints);

        jButtonModMeasure.setText("Modify");
        jButtonModMeasure.setEnabled(false);
        jButtonModMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModMeasureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel12.add(jButtonModMeasure, gridBagConstraints);

        jButtonRemMeasure.setText("Remove");
        jButtonRemMeasure.setEnabled(false);
        jButtonRemMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemMeasureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel12.add(jButtonRemMeasure, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.weightx = 1.0;
        jPanel12.add(jPanel14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel17.add(jPanel12, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel11.add(jPanel17, gridBagConstraints);

        jTabbedPane1.addTab("Measures", jPanel11);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jComboBoxrunDirectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxrunDirectionActionPerformed
    if (isInit() || currentCrosstabReportElement == null) return;
    
    currentCrosstabReportElement.setRunDirection( ((Tag)jComboBoxrunDirection.getSelectedItem()).getValue()+"" );
    notifyChange();
}//GEN-LAST:event_jComboBoxrunDirectionActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        openExtraWindows();
        
    }//GEN-LAST:event_formWindowOpened

    private void jButtonDownColumnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownColumnGroupActionPerformed

        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();

        int index = jTableColumnGroups.getSelectedRow();
        
        if (index < currentCrosstabReportElement.getColumnGroups().size()-1)
        {
            CrosstabColumnGroup group = (CrosstabColumnGroup)currentCrosstabReportElement.getColumnGroups().elementAt(index);
            currentCrosstabReportElement.getColumnGroups().removeElementAt(index);
            currentCrosstabReportElement.getColumnGroups().insertElementAt( group, index+1);
            
            dtm.removeRow(index);
            dtm.insertRow( index+1, new Object[]{group});
            currentCrosstabReportElement.modifyGroup(group.getName(), group);
        }
        
    }//GEN-LAST:event_jButtonDownColumnGroupActionPerformed

    private void jButtonUpColumnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpColumnGroupActionPerformed
        
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();

        int index = jTableColumnGroups.getSelectedRow();
        
        if (index >0)
        {
            CrosstabColumnGroup group = (CrosstabColumnGroup)currentCrosstabReportElement.getColumnGroups().elementAt(index);
            currentCrosstabReportElement.getColumnGroups().removeElementAt(index);
            currentCrosstabReportElement.getColumnGroups().insertElementAt( group, index-1);
            
            dtm.removeRow(index);
            dtm.insertRow( index-1, new Object[]{group});
            currentCrosstabReportElement.modifyGroup(group.getName(), group);
        }
    }//GEN-LAST:event_jButtonUpColumnGroupActionPerformed

    private void jButtonDownRowGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownRowGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();

        int index = jTableRowGroups.getSelectedRow();
        
        if (index < currentCrosstabReportElement.getRowGroups().size()-1)
        {
            CrosstabRowGroup group = (CrosstabRowGroup)currentCrosstabReportElement.getRowGroups().elementAt(index);
            currentCrosstabReportElement.getRowGroups().removeElementAt(index);
            currentCrosstabReportElement.getRowGroups().insertElementAt( group, index+1);
            
            dtm.removeRow(index);
            dtm.insertRow( index+1, new Object[]{group});
            currentCrosstabReportElement.modifyGroup(group.getName(), group);
        }
    }//GEN-LAST:event_jButtonDownRowGroupActionPerformed

    private void jButtonUpRowGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpRowGroupActionPerformed

        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();

        int index = jTableRowGroups.getSelectedRow();
        
        if (index >0)
        {
            CrosstabRowGroup group = (CrosstabRowGroup)currentCrosstabReportElement.getRowGroups().elementAt(index);
            currentCrosstabReportElement.getRowGroups().removeElementAt(index);
            currentCrosstabReportElement.getRowGroups().insertElementAt( group, index-1);
            
            dtm.removeRow(index);
            dtm.insertRow( index-1, new Object[]{group});
            currentCrosstabReportElement.modifyGroup(group.getName(), group);
        }
        
    }//GEN-LAST:event_jButtonUpRowGroupActionPerformed

    private void jButtonRemColumnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemColumnGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();
        
        Vector groupsToRemove = new Vector();
        while (jTableColumnGroups.getSelectedRowCount() > 0) {
            int i=jTableColumnGroups.getSelectedRow();
            groupsToRemove.add( jTableColumnGroups.getValueAt( i, 0) );
            dtm.removeRow(i);
        }
        currentCrosstabReportElement.removeGroups(groupsToRemove);
    }//GEN-LAST:event_jButtonRemColumnGroupActionPerformed

    private void jButtonModColumnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModColumnGroupActionPerformed
        if (this.isInit()) return;
    
        if (currentCrosstabReportElement == null) return;
        int rowNumber = jTableColumnGroups.getSelectedRow();
        CrosstabColumnGroup rg = (CrosstabColumnGroup)jTableColumnGroups.getValueAt( jTableColumnGroups.getSelectedRow(), 0);
        
        CrosstabGroupDialog jrpd = new CrosstabGroupDialog(CrosstabGroupDialog.COLUMNGROUP, this, true);
        jrpd.setGroup( rg );
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        if (evt != null && evt.getID() > 0)
        {
            jrpd.setFocusedExpression(evt.getID());
        }
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            CrosstabGroup group = jrpd.getGroup();
            String oldName = rg.getName();
            rg.setName( group.getName() );
            rg.setBucketComparatorExpression( group.getBucketComparatorExpression() );
            rg.setBucketExpression( group.getBucketExpression() );
            rg.setBucketExpressionClass( group.getBucketExpressionClass() );
            rg.setBucketOrder( group.getBucketOrder() );
            rg.setHeaderPosition( group.getHeaderPosition() );
            rg.setTotalPosition( group.getTotalPosition() );
            rg.setName( group.getName() );
            rg.setSize( group.getSize() );
            rg.setHasHeader( group.isHasHeader() );
            rg.setHasTotal( group.isHasTotal() );
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();
            dtm.setValueAt(rg, rowNumber, 0);
            jTableColumnGroups.updateUI();
            currentCrosstabReportElement.modifyGroup(oldName, rg);
        }
    }//GEN-LAST:event_jButtonModColumnGroupActionPerformed

    private void jButtonAddColumnGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddColumnGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        // Set the new value for all selected elements...
                
        CrosstabGroupDialog jrpd = new CrosstabGroupDialog(CrosstabGroupDialog.COLUMNGROUP,this, true);
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            CrosstabGroup group = jrpd.getGroup();
            CrosstabColumnGroup rg = new CrosstabColumnGroup();
            
            rg.setName( group.getName() );
            rg.setBucketComparatorExpression( group.getBucketComparatorExpression() );
            rg.setBucketExpression( group.getBucketExpression() );
            rg.setBucketExpressionClass( group.getBucketExpressionClass() );
            rg.setBucketOrder( group.getBucketOrder() );
            rg.setHeaderPosition( group.getHeaderPosition() );
            rg.setTotalPosition( group.getTotalPosition() );
            rg.setName( group.getName() );
            rg.setSize( group.getSize() );
            rg.setHasHeader( group.isHasHeader() );
            rg.setHasTotal( group.isHasTotal() );

            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();
            dtm.addRow(new Object[]{rg});
            currentCrosstabReportElement.addGroup(rg);
        }
    }//GEN-LAST:event_jButtonAddColumnGroupActionPerformed

    private void jTableColumnGroupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableColumnGroupsMouseClicked
    if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTableColumnGroups.getSelectedRowCount() > 0)
            {
                jButtonModColumnGroupActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableColumnGroupsMouseClicked

    private void jTableRowGroupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRowGroupsMouseClicked
if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTableRowGroups.getSelectedRowCount() > 0)
            {
                jButtonModRowGroupActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableRowGroupsMouseClicked

    private void jButtonRemRowGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemRowGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();
        
        ArrayList groupsToRemove = new ArrayList();
            
        while (jTableRowGroups.getSelectedRowCount() > 0) {
            int i=jTableRowGroups.getSelectedRow();
            groupsToRemove.add( jTableRowGroups.getValueAt( i, 0) );
            dtm.removeRow(i);
        }
        currentCrosstabReportElement.removeGroups(groupsToRemove);
        
    }//GEN-LAST:event_jButtonRemRowGroupActionPerformed

    private void jButtonModRowGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModRowGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        int rowNumber = jTableRowGroups.getSelectedRow();
        CrosstabRowGroup rg = (CrosstabRowGroup)jTableRowGroups.getValueAt( jTableRowGroups.getSelectedRow(), 0);
        
        CrosstabGroupDialog jrpd = new CrosstabGroupDialog(CrosstabGroupDialog.ROWGROUP, this, true);
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setGroup( rg );
        if (evt != null && evt.getID() > 0)
        {
            jrpd.setFocusedExpression(evt.getID());
        }
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            CrosstabGroup group = jrpd.getGroup();
            String oldName = rg.getName();
            rg.setName( group.getName() );
            rg.setBucketComparatorExpression( group.getBucketComparatorExpression() );
            rg.setBucketExpression( group.getBucketExpression() );
            rg.setBucketExpressionClass( group.getBucketExpressionClass() );
            rg.setBucketOrder( group.getBucketOrder() );
            rg.setHeaderPosition( group.getHeaderPosition() );
            rg.setTotalPosition( group.getTotalPosition() );
            rg.setName( group.getName() );
            rg.setSize( group.getSize() );
            rg.setHasHeader( group.isHasHeader() );
            rg.setHasTotal( group.isHasTotal() );
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();
            dtm.setValueAt(rg, rowNumber, 0);
            jTableRowGroups.updateUI();
            currentCrosstabReportElement.modifyGroup(oldName, rg);
        }
    }//GEN-LAST:event_jButtonModRowGroupActionPerformed

    private void jButtonAddRowGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddRowGroupActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        // Set the new value for all selected elements...
                
        CrosstabGroupDialog jrpd = new CrosstabGroupDialog(CrosstabGroupDialog.ROWGROUP,this, true);
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            CrosstabGroup group = jrpd.getGroup();
            CrosstabRowGroup rg = new CrosstabRowGroup();
            
            rg.setName( group.getName() );
            rg.setBucketComparatorExpression( group.getBucketComparatorExpression() );
            rg.setBucketExpression( group.getBucketExpression() );
            rg.setBucketExpressionClass( group.getBucketExpressionClass() );
            rg.setBucketOrder( group.getBucketOrder() );
            rg.setHeaderPosition( group.getHeaderPosition() );
            rg.setTotalPosition( group.getTotalPosition() );
            rg.setName( group.getName() );
            rg.setSize( group.getSize() );
            rg.setHasHeader( group.isHasHeader() );
            rg.setHasTotal( group.isHasTotal() );
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();
            dtm.addRow(new Object[]{rg});
            currentCrosstabReportElement.addGroup(rg);
        }
    }//GEN-LAST:event_jButtonAddRowGroupActionPerformed

    private void jTableMeasuresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMeasuresMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTableMeasures.getSelectedRowCount() > 0)
            {
                jButtonModMeasureActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableMeasuresMouseClicked

    private void jButtonRemMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemMeasureActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableMeasures.getModel();
        
        while (jTableMeasures.getSelectedRowCount() > 0) {
            int i=jTableMeasures.getSelectedRow();
            
            currentCrosstabReportElement.removeMeasure( (Measure)jTableMeasures.getValueAt( i, 0) ); 
            dtm.removeRow(i);
        }
    }//GEN-LAST:event_jButtonRemMeasureActionPerformed

    private void jButtonModMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModMeasureActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        int rowNumber = jTableMeasures.getSelectedRow();
        Measure measure = (Measure)jTableMeasures.getValueAt( jTableMeasures.getSelectedRow(), 0);
        
        MeasureDialog jrpd = new MeasureDialog(null, this, true);
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setMeasure( measure );
        if (evt != null && evt.getID() > 0)
        {
            jrpd.setFocusedExpression(evt.getID());
        }
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            measure.setName( jrpd.getMeasure().getName() );
            measure.setExpression( jrpd.getMeasure().getExpression());
            measure.setClassType( jrpd.getMeasure().getClassType());
            measure.setCalculation( jrpd.getMeasure().getCalculation());
            measure.setIncrementerFactoryClass( jrpd.getMeasure().getIncrementerFactoryClass());
            measure.setPercentageCalculatorClass( jrpd.getMeasure().getPercentageCalculatorClass());
            measure.setPercentageOf( jrpd.getMeasure().getPercentageOf());
            
            currentCrosstabReportElement.measureModified( measure );  
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableMeasures.getModel();
            dtm.setValueAt(measure, rowNumber, 0);
            dtm.setValueAt(measure.getExpression(), rowNumber, 1);
            jTableMeasures.updateUI();
        }
    }//GEN-LAST:event_jButtonModMeasureActionPerformed

    private void jButtonAddMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddMeasureActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        // Set the new value for all selected elements...
                
        MeasureDialog jrpd = new MeasureDialog(null,this, true);
        jrpd.setExpressionContext( ((this.getCurrentCrosstabReportElement().isUseDataset() && this.getCurrentCrosstabReportElement().getDataset() != null) ? this.getCurrentCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Measure measure = jrpd.getMeasure();
            currentCrosstabReportElement.addMeasure( measure );  
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableMeasures.getModel();
            dtm.addRow(new Object[]{measure, measure.getClassType()});
        }
    }//GEN-LAST:event_jButtonAddMeasureActionPerformed

    private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed

        if (currentCrosstabReportElement == null) return;
        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard() == null) return;
        
        Dataset theDataset = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard();
        
        currentCrosstabReportElement.getDataset().setIncrementType( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getIncrementType());
        currentCrosstabReportElement.getDataset().setIncrementGroup( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getIncrementGroup());
        currentCrosstabReportElement.getDataset().setResetType( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getResetType());
        currentCrosstabReportElement.getDataset().setResetGroup( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getResetGroup());
        
        currentCrosstabReportElement.getDataset().setUseConnection( theDataset.isUseConnection());
        currentCrosstabReportElement.getDataset().setSubDataset( theDataset.getSubDataset());
        currentCrosstabReportElement.getDataset().setParametersMapExpression( theDataset.getParametersMapExpression());
        currentCrosstabReportElement.getDataset().setConnectionExpression( theDataset.getConnectionExpression());
        currentCrosstabReportElement.getDataset().setDataSourceExpression( theDataset.getDataSourceExpression());
        
        for (int i=0; i< theDataset.getSubreportParameters().size(); ++i)
        {
            currentCrosstabReportElement.getDataset().getSubreportParameters().add( ((JRSubreportParameter)theDataset.getSubreportParameters().elementAt(i)).cloneMe() );
        }
               
        init = true;
        
        loadDataset();
        
        init = false;
        notifyChange();
    }//GEN-LAST:event_jButtonPasteActionPerformed

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed
    
        if (currentCrosstabReportElement == null) return;
        it.businesslogic.ireport.gui.MainFrame.getMainInstance().setChartDatasetClipBoard( currentCrosstabReportElement.getDataset().cloneMe() );
        jButtonPaste.setEnabled(true);
    }//GEN-LAST:event_jButtonCopyActionPerformed

    private void jComboBoxIncrementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementGroupActionPerformed
        if (isInit() || currentCrosstabReportElement == null) return;
        currentCrosstabReportElement.getDataset().setIncrementGroup( ""+jComboBoxIncrementGroup.getSelectedItem());
        notifyChange();
    }//GEN-LAST:event_jComboBoxIncrementGroupActionPerformed

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed
        
        if (isInit() || currentCrosstabReportElement == null) return;
        currentCrosstabReportElement.getDataset().setIncrementType( ((Tag)jComboBoxIncrementType.getSelectedItem()).getValue()+"");
        
        if (currentCrosstabReportElement.getDataset().getIncrementType().equals("Group"))
        {
            // We have to removethe null entry...
            if (jComboBoxIncrementGroup.getItemAt(0).equals(""))
            {
                jComboBoxIncrementGroup.removeItemAt(0);
            }
            jComboBoxIncrementGroup.setSelectedIndex(0);
        }
        else
        {
            if (!jComboBoxIncrementGroup.getItemAt(0).equals(""))
            {
                jComboBoxIncrementGroup.insertItemAt("",0);
            }
            jComboBoxIncrementGroup.setSelectedIndex(0);
        }
        notifyChange();
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxResetGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetGroupActionPerformed
        
        if (isInit() || currentCrosstabReportElement == null) return;
        currentCrosstabReportElement.getDataset().setResetGroup( ""+jComboBoxResetGroup.getSelectedItem());
        notifyChange();
    }//GEN-LAST:event_jComboBoxResetGroupActionPerformed

    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed
        if (isInit() || currentCrosstabReportElement == null) return;
        currentCrosstabReportElement.getDataset().setResetType( ((Tag)jComboBoxResetType.getSelectedItem()).getValue()+"" );
        
        if (currentCrosstabReportElement.getDataset().getResetType().equals("Group"))
        {
            // We have to remove the null entry...
            if (jComboBoxResetGroup.getItemAt(0).equals(""))
            {
                jComboBoxResetGroup.removeItemAt(0);
            }
            jComboBoxResetGroup.setSelectedIndex(0);
        }
        else
        {
            if (!jComboBoxResetGroup.getItemAt(0).equals(""))
            {
                jComboBoxResetGroup.insertItemAt("",0);
            }
            jComboBoxResetGroup.setSelectedIndex(0);
        }
        
        notifyChange();
    }//GEN-LAST:event_jComboBoxResetTypeActionPerformed

    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
        
        while (jTableDatasetParameters.getSelectedRowCount() > 0) {
            int i=jTableDatasetParameters.getSelectedRow();
            currentCrosstabReportElement.getDataset().getSubreportParameters().removeElement( jTableDatasetParameters.getValueAt( i, 0) );
            dtm.removeRow(i);
        }
        notifyChange();
    }//GEN-LAST:event_jButtonRemParameterActionPerformed

    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
                int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRSubreportParameter parameter = (JRSubreportParameter)jTableDatasetParameters.getValueAt( jTableDatasetParameters.getSelectedRow(), 0);
        
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setParameter( parameter );
        if (subdatasetParameterHighlightExpression != null)
        {
            jrpd.setFocusedExpression( ((Integer)subdatasetParameterHighlightExpression[0]).intValue() );
        }
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.setValueAt(parameter, rowNumber, 0);
            dtm.setValueAt(parameter.getExpression(), rowNumber, 1);
            jTableDatasetParameters.updateUI();
            notifyChange();
        }
    }//GEN-LAST:event_jButtonModParameterActionPerformed

    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentCrosstabReportElement == null) return;
        // Set the new value for all selected elements...
                
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRSubreportParameter parameter = jrpd.getParameter();
            currentCrosstabReportElement.getDataset().getSubreportParameters().addElement( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getExpression()});
            notifyChange();
        }
    }//GEN-LAST:event_jButtonAddParameterActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTableDatasetParameters.getSelectedRowCount() > 0)
            {
                jButtonModParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
       if (isInit() || currentCrosstabReportElement == null) return;
                
        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            currentCrosstabReportElement.getDataset().setUseConnection(false);
            currentCrosstabReportElement.getDataset().setConnectionExpression("");
            currentCrosstabReportElement.getDataset().setDataSourceExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {
            currentCrosstabReportElement.getDataset().setUseConnection(true);
            currentCrosstabReportElement.getDataset().setDataSourceExpression("");
            currentCrosstabReportElement.getDataset().setConnectionExpression("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {
            currentCrosstabReportElement.getDataset().setUseConnection(false);
            currentCrosstabReportElement.getDataset().setDataSourceExpression("$P{MyDataource}");
            currentCrosstabReportElement.getDataset().setConnectionExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{MyDataource}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        }
       notifyChange();
    }//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    private void jCheckBoxPreSortedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPreSortedActionPerformed
         if (this.isInit()) return; 

        if (currentCrosstabReportElement != null)
        {
            currentCrosstabReportElement.setPreSorted( jCheckBoxPreSorted.isSelected() );
        }

    }//GEN-LAST:event_jCheckBoxPreSortedActionPerformed

    private void jCheckBoxUseDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseDatasetActionPerformed

        if (this.isInit()) return; 
        
        if (currentCrosstabReportElement != null)
        {
            currentCrosstabReportElement.setUseDataset( jCheckBoxUseDataset.isSelected() );
        }

        setDatasetEnabled( currentCrosstabReportElement.isUseDataset()  );

        
    }//GEN-LAST:event_jCheckBoxUseDatasetActionPerformed

    private void jComboBoxSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubDatasetActionPerformed
         if (this.isInit()) return; 

       
            if (currentCrosstabReportElement != null)
            {
                if (this.jComboBoxSubDataset.getSelectedIndex() > 0)
                {
                        if ( currentCrosstabReportElement.getDataset().getSubDataset() != jComboBoxSubDataset.getSelectedItem())
                        {
                            currentCrosstabReportElement.getDataset().setSubDataset( (SubDataset)jComboBoxSubDataset.getSelectedItem() );
                            jTabbedPane2.setVisible(true);
                        }
                        jRTextExpressionAreaFilterExpression.setSubDataset( (SubDataset)jComboBoxSubDataset.getSelectedItem() );
                // Check subdataset parameters.... (TODO)
                }
                else
                {
                    jRTextExpressionAreaFilterExpression.setSubDataset(null);
                    currentCrosstabReportElement.getDataset().setSubDataset( null );
                    currentCrosstabReportElement.getDataset().getSubreportParameters().removeAllElements();
                    currentCrosstabReportElement.getDataset().setParametersMapExpression("");
                    currentCrosstabReportElement.getDataset().setConnectionExpression("");
                    currentCrosstabReportElement.getDataset().setDataSourceExpression("");
                    currentCrosstabReportElement.getDataset().setUseConnection(false);
                    
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    jRTextExpressionAreaMapExpression.setText("");
                
                    jTabbedPane2.setVisible(false);
                    jTabbedPane2.updateUI();
                }
        }
        notifyChange();
    }//GEN-LAST:event_jComboBoxSubDatasetActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrosstabPropertiesDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddColumnGroup;
    private javax.swing.JButton jButtonAddMeasure;
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonAddRowGroup;
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonDownColumnGroup;
    private javax.swing.JButton jButtonDownRowGroup;
    private javax.swing.JButton jButtonModColumnGroup;
    private javax.swing.JButton jButtonModMeasure;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonModRowGroup;
    private javax.swing.JButton jButtonPaste;
    private javax.swing.JButton jButtonRemColumnGroup;
    private javax.swing.JButton jButtonRemMeasure;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JButton jButtonRemRowGroup;
    private javax.swing.JButton jButtonUpColumnGroup;
    private javax.swing.JButton jButtonUpRowGroup;
    private javax.swing.JCheckBox jCheckBoxPreSorted;
    private javax.swing.JCheckBox jCheckBoxUseDataset;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JComboBox jComboBoxrunDirection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementGroup;
    private javax.swing.JLabel jLabelIncrementType;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JLabel jLabelIncrementWhenExpression;
    private javax.swing.JLabel jLabelResetGroup;
    private javax.swing.JLabel jLabelResetType;
    private javax.swing.JLabel jLabelRunDirection;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelDataset;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaFilterExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaMapExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableColumnGroups;
    private javax.swing.JTable jTableDatasetParameters;
    private javax.swing.JTable jTableMeasures;
    private javax.swing.JTable jTableRowGroups;
    // End of variables declaration//GEN-END:variables
    
    
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    
    public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
    }
    
    public void jTableMeasuresListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableMeasures.getSelectedRowCount() > 0) {
            this.jButtonModMeasure.setEnabled(true);
            this.jButtonRemMeasure.setEnabled(true);
        }
        else {
            this.jButtonModMeasure.setEnabled(false);
            this.jButtonRemMeasure.setEnabled(false);
        }
    }
    
    public void jTableRowGroupsListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableRowGroups.getSelectedRowCount() > 0) {
            this.jButtonModRowGroup.setEnabled(true);
            this.jButtonRemRowGroup.setEnabled(true);
            this.jButtonUpRowGroup.setEnabled(true);
            this.jButtonDownRowGroup.setEnabled(true);
        }
        else {
            this.jButtonModRowGroup.setEnabled(false);
            this.jButtonRemRowGroup.setEnabled(false);
            this.jButtonUpRowGroup.setEnabled(false);
            this.jButtonDownRowGroup.setEnabled(false);
        }
    }
    
    public void jTableColumnGroupsListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableColumnGroups.getSelectedRowCount() > 0) {
            this.jButtonModColumnGroup.setEnabled(true);
            this.jButtonRemColumnGroup.setEnabled(true);
            this.jButtonUpColumnGroup.setEnabled(true);
            this.jButtonDownColumnGroup.setEnabled(true);
        }
        else {
            this.jButtonModColumnGroup.setEnabled(false);
            this.jButtonRemColumnGroup.setEnabled(false);
            this.jButtonUpColumnGroup.setEnabled(false);
            this.jButtonDownColumnGroup.setEnabled(false);
        }
    }
    
    public void jRTextExpressionAreaMapExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentCrosstabReportElement != null)
        {
            currentCrosstabReportElement.getDataset().setParametersMapExpression(""+jRTextExpressionAreaMapExpression.getText());
            notifyChange();
        }
    }
    
    
    public CrosstabReportElement getCurrentCrosstabReportElement() {
        return currentCrosstabReportElement;
    }

    public void setCurrentCrosstabReportElement(CrosstabReportElement currentCrosstabReportElement) {
        
        setInit(true);
        
        try {
            
            this.currentCrosstabReportElement = currentCrosstabReportElement;

            jButtonPaste.setEnabled( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard() != null ) ;

            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableMeasures.getModel();
            dtm.setRowCount(0);
            
            javax.swing.table.DefaultTableModel dtmRG = (javax.swing.table.DefaultTableModel)jTableRowGroups.getModel();
            dtmRG.setRowCount(0);
            
            javax.swing.table.DefaultTableModel dtmCG = (javax.swing.table.DefaultTableModel)jTableColumnGroups.getModel();
            dtmCG.setRowCount(0);

            if (currentCrosstabReportElement == null)
            {
                this.jTabbedPane1.setVisible(false);
            }
            else
            {
                updateGroups();
                updateSubDatasets();

                loadDataset();

                Enumeration enum_measures = currentCrosstabReportElement.getMeasures().elements();

                while (enum_measures.hasMoreElements())
                {
                    Measure measure = (Measure)enum_measures.nextElement();
                    dtm.addRow(new Object[]{measure, measure.getClassType()});
                }
                
                Enumeration enum_groups = currentCrosstabReportElement.getRowGroups().elements();
                while (enum_groups.hasMoreElements())
                {
                    dtmRG.addRow(new Object[]{enum_groups.nextElement()});
                }
                
                enum_groups = currentCrosstabReportElement.getColumnGroups().elements();
                while (enum_groups.hasMoreElements())
                {
                    dtmCG.addRow(new Object[]{enum_groups.nextElement()});
                }
            }
          
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        setInit(false);
    }
    
    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentCrosstabReportElement != null)
        {
            if (currentCrosstabReportElement.getDataset().isUseConnection())
                currentCrosstabReportElement.getDataset().setConnectionExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
            else
                currentCrosstabReportElement.getDataset().setDataSourceExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
                
            notifyChange();
        }
    }
    
    public void jRTextExpressionAreaFilterExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentCrosstabReportElement != null)
        {
            currentCrosstabReportElement.getDataset().setIncrementWhenExpression(""+jRTextExpressionAreaFilterExpression.getText());
            notifyChange();
        }
    }
    
     /**
     * This method update the comboboxes where is present the goup list.
     */
    public void updateGroups()
    {

        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame() == null)
	{
            jComboBoxResetGroup.removeAllItems();
            jComboBoxResetGroup.addItem("");
        }	
        else
        {
            Misc.updateStringComboBox(jComboBoxResetGroup, 
                                      it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getGroups(), true);
            Misc.updateStringComboBox(jComboBoxIncrementGroup, 
                                      it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getGroups(), true);
        }
        
    }
    
    
    public void updateSubDatasets()
    {

        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame() == null)
	{
            jComboBoxSubDataset.removeAllItems();
            jComboBoxSubDataset.addItem("");
        }	
        else
        {
            Misc.updateComboBox(jComboBoxSubDataset, it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getSubDatasets(), true);
        }
    }
    
    /**
     * Enable/disable the dataset gui (used when isUseDataset changes)
     */
    public void setDatasetEnabled(boolean enabled)
    {
        jCheckBoxPreSorted.setEnabled(enabled); 
        jLabelIncrementGroup.setEnabled(enabled);
        jLabelIncrementType.setEnabled(enabled);
        jLabelResetType.setEnabled(enabled);
        jLabelResetGroup.setEnabled(enabled);
        jComboBoxResetType.setEnabled(enabled);
        jComboBoxResetGroup.setEnabled(enabled);
        jComboBoxIncrementType.setEnabled(enabled);
        jComboBoxIncrementGroup.setEnabled(enabled);
        jComboBoxIncrementType.setEnabled(enabled);
        jComboBoxSubDataset.setEnabled(enabled);   
        jTabbedPane2.setVisible(enabled && jComboBoxSubDataset.getSelectedIndex() > 0);
        jButtonCopy.setEnabled(enabled);
        jButtonPaste.setEnabled(enabled);
        
        jLabelIncrementWhenExpression.setEnabled(enabled);
        jRTextExpressionAreaFilterExpression.setEnabled(enabled);
        //Color c = javax.swing.UIManager.getDefaults().getColor("Panel.background");
        //if (c == null) c = jPanel1.getBackground();
        //jRTextExpressionAreaFilterExpression.setBackground((enabled) ? Color.WHITE : c);
    }
    
    
    /**
     * Load the dataset into GUI. Assume init = true.
     */
    public void loadDataset()
    {
        if (currentCrosstabReportElement == null) return;
        // Set general dataset data...
            Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentCrosstabReportElement.getDataset().getIncrementType());
            jComboBoxIncrementGroup.setSelectedItem( currentCrosstabReportElement.getDataset().getIncrementGroup() );
            Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentCrosstabReportElement.getDataset().getResetType());
            jComboBoxResetGroup.setSelectedItem( currentCrosstabReportElement.getDataset().getResetGroup() );
            setDatasetEnabled( currentCrosstabReportElement.isUseDataset() );
            jCheckBoxUseDataset.setSelected(currentCrosstabReportElement.isUseDataset());
            Misc.setComboboxSelectedTagValue( jComboBoxrunDirection, currentCrosstabReportElement.getRunDirection() );
            jCheckBoxPreSorted.setSelected(currentCrosstabReportElement.isPreSorted() );
            
            jRTextExpressionAreaFilterExpression.setText( currentCrosstabReportElement.getDataset().getIncrementWhenExpression() );
            
            if (currentCrosstabReportElement.getDataset().getSubDataset() != null)
            {
                jComboBoxSubDataset.setSelectedItem(currentCrosstabReportElement.getDataset().getSubDataset());
                jTabbedPane2.setVisible(true);
            
                jRTextExpressionAreaMapExpression.setText( currentCrosstabReportElement.getDataset().getParametersMapExpression()  );
                if (!currentCrosstabReportElement.getDataset().isUseConnection() &&  Misc.nvl( currentCrosstabReportElement.getDataset().getDataSourceExpression(),"").trim().equals("")) {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                }
                else if (currentCrosstabReportElement.getDataset().isUseConnection()) {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                    this.jRTextExpressionAreaTextConnectionExpression.setText( currentCrosstabReportElement.getDataset().getConnectionExpression());
                }
                else {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                    this.jRTextExpressionAreaTextConnectionExpression.setText( currentCrosstabReportElement.getDataset().getDataSourceExpression());
                }

                //Add parameters...
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);

                java.util.Enumeration enum_parameters = currentCrosstabReportElement.getDataset().getSubreportParameters().elements();
                while (enum_parameters.hasMoreElements()) {
                    it.businesslogic.ireport.JRSubreportParameter parameter = (it.businesslogic.ireport.JRSubreportParameter)enum_parameters.nextElement();
                    Vector row = new Vector();
                    row.addElement(parameter);
                    row.addElement(parameter.getExpression());
                    dtm.addRow(row);
                }            
            
                jRTextExpressionAreaFilterExpression.setSubDataset(currentCrosstabReportElement.getDataset().getSubDataset());
            }
            else
            {
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");
                jRTextExpressionAreaMapExpression.setText("");
                
                jComboBoxSubDataset.setSelectedIndex(0);
                jTabbedPane2.setVisible(false);
            }
    }
    
    public void notifyChange()
    {
        if (getCurrentCrosstabReportElement() != null)
        {
            getCurrentCrosstabReportElement().notifyChange();
        }
    }
    
    private int openRowGroupWhenVisible = -1;
    private int openColumnGroupWhenVisible = -1;
    private int openMeasureWhenVisible = -1;
    private int highlightThisGroupExpWhenVisible = 0;
    
    public void openRowBucket(int index, int expId)
    {
       openRowGroupWhenVisible = index;
       openColumnGroupWhenVisible = -1;
       openMeasureWhenVisible = -1;
       highlightThisGroupExpWhenVisible = expId;
       
       if (isVisible())
       {
            openExtraWindows();
       }
    }
    
    public void openColumnBucket(int index, int expId)
    {
       openRowGroupWhenVisible = -1;
       openColumnGroupWhenVisible = index;
       openMeasureWhenVisible = -1;
       highlightThisGroupExpWhenVisible = expId;
       
       if (isVisible())
       {
            openExtraWindows();
       }
    }
    
    public void openMeasure(int index, int expId)
    {
       openRowGroupWhenVisible = -1;
       openColumnGroupWhenVisible = -1;
       openMeasureWhenVisible = index;
       highlightThisGroupExpWhenVisible = expId;
       
       if (isVisible())
       {
            openExtraWindows();
       }
    }
    
    public void openExtraWindows()
    {
        
        if (openRowGroupWhenVisible >= 0)
        {
            if (jTableRowGroups.getRowCount() > openRowGroupWhenVisible)
            {
                jTabbedPane1.setSelectedComponent(jPanel9);
                jTableRowGroups.setRowSelectionInterval(openRowGroupWhenVisible,openRowGroupWhenVisible);
                jButtonModRowGroupActionPerformed(new ActionEvent(jButtonModRowGroup, highlightThisGroupExpWhenVisible,""));
            }
        } else if (openColumnGroupWhenVisible >= 0)
        {
            if (jTableColumnGroups.getRowCount() > openColumnGroupWhenVisible)
            {
                jTabbedPane1.setSelectedComponent(jPanel9);
                jTableColumnGroups.setRowSelectionInterval(openColumnGroupWhenVisible,openColumnGroupWhenVisible);
                jButtonModColumnGroupActionPerformed(new ActionEvent(jButtonModColumnGroup, highlightThisGroupExpWhenVisible,""));
            }
        } else if (openMeasureWhenVisible >= 0)
        {
            if (jTableMeasures.getRowCount() > openMeasureWhenVisible)
            {
                jTabbedPane1.setSelectedComponent(jPanel11);
                jTableMeasures.setRowSelectionInterval(openMeasureWhenVisible,openMeasureWhenVisible);
                jButtonModMeasureActionPerformed(new ActionEvent(jButtonModColumnGroup, highlightThisGroupExpWhenVisible,""));
            }
        }
        else if (subdatasetParameterHighlightExpression != null)
        {
            jButtonModParameterActionPerformed(new ActionEvent(jButtonModParameter,0,""));
        }
        highlightThisGroupExpWhenVisible = -1;
        subdatasetParameterHighlightExpression = null;
    }
    
    private Object[] subdatasetParameterHighlightExpression = null; 
    
    /**
     * This method set the focus on a specific component.
     * 
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        switch (expID)
        {
            case ChartPropertiesDialog.COMPONENT_INCREMENT_WHEN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanel8 );
                Misc.selectTextAndFocusArea(jRTextExpressionAreaFilterExpression);
                break;
            case ChartPropertiesDialog.COMPONENT_DATASETRUN_PARAMETERS:
                jTabbedPane1.setSelectedComponent( jPanel8 );
                jTabbedPane2.setSelectedComponent(jPanel4);
                
                int index = ((Integer)expressionInfo[1]).intValue();
                
                if (index >=0 && jTableDatasetParameters.getRowCount() > index )
                {
                    jTableDatasetParameters.setRowSelectionInterval(index,index);
                    subdatasetParameterHighlightExpression = new Object[expressionInfo.length-2];
                    for (int i=2; i< expressionInfo.length; ++i) subdatasetParameterHighlightExpression[i-2] = expressionInfo[i];
                    break;
                }
                
                break;
            case ChartPropertiesDialog.COMPONENT_DATASETRUN_MAP_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanel8 );
                jTabbedPane2.setSelectedComponent(jPanel5);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaMapExpression);
                break;
            case ChartPropertiesDialog.COMPONENT_DATASETRUN_DS_CONN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanel8 );
                jTabbedPane2.setSelectedComponent(jPanel6);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaTextConnectionExpression);
                break;
        }
    }
}
