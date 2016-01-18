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
 * ReportPropertiesFrame.java
 * 
 * Created on 8 febbraio 2003, 16.25
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.*;
import javax.swing.*;
import java.awt.Point;

/**
 *
 * @author  Administrator
 */
public class ReportPropertiesFrame extends javax.swing.JDialog  {

    private it.businesslogic.ireport.util.Unit[] units;

    /** Holds value of property dialogResult. */
    private int dialogResult;

    private int reportWidth;

    private int reportHeight;

    private int topMargin;

    private int leftMargin;

    private int bottomMargin;

    private int rightMargin;

    private String orientation;

    private String scriptletClass;

    private String resourceBundleBaseName;

    private String reportName;

    private int reportHandling = 0;

    private boolean floatColumnFooter = false;

    private String language = "java";

    private String whenResourceMissingType = "Null";

    private boolean ignorePagination = false;
    
    private String formatFactoryClass = "";

    /** Creates new form ReportPropertiesFrame */
    public ReportPropertiesFrame(java.awt.Frame parent, boolean modal) {
        super(parent,modal);
        initComponents();

        units = it.businesslogic.ireport.util.Unit.getStandardUnits();

        // Setting compobox contents...
        for (int i=0; i< units.length; ++i) {
            jComboBoxWUnit.addItem(units[i]);
            jComboBoxHUnit.addItem(units[i]);
            jComboBoxTopMarginUnit.addItem(units[i]);
            jComboBoxBottomMarginUnit.addItem(units[i]);
            jComboBoxLeftMarginUnit.addItem(units[i]);
            jComboBoxRightMarginUnit.addItem(units[i]);
            jComboBoxColumnsWidthUnit.addItem(units[i]);
            jComboBoxColumnsSpacingUnit.addItem(units[i]);
        }


        //Modified by Felix Firgau for I18n on Feb 8th 2006

        //jComboBoxScriptletHandling.addItem("Don't use scriptlet");
        jComboBoxScriptletHandling.addItem(it.businesslogic.ireport.util.I18n.getString("noScriptlet", "Don't use scriptlet"));
        //jComboBoxScriptletHandling.addItem("Use iReport internal scriptlet support");
        jComboBoxScriptletHandling.addItem(it.businesslogic.ireport.util.I18n.getString("iReportScriptlet", "Use iReport internal scriptlet support"));
        //jComboBoxScriptletHandling.addItem("Use this scriptlet class...");
        jComboBoxScriptletHandling.addItem(it.businesslogic.ireport.util.I18n.getString("customScriptlet", "Use this scriptlet class..."));
        //Modification end


        // Set right names to tabs...
        this.jTabbedPane1.setTitleAt(0,it.businesslogic.ireport.util.I18n.getString("pageMargin", "Page Margin"));
        this.jTabbedPane1.setTitleAt(1,it.businesslogic.ireport.util.I18n.getString("columns", "Columns"));
        this.jTabbedPane1.setTitleAt(2,it.businesslogic.ireport.util.I18n.getString("scriptlet", "Scriptlet"));
        this.jTabbedPane1.setTitleAt(3, it.businesslogic.ireport.util.I18n.getString("more", "More..."));

        this.jComboBoxSize.addItem("Custom");
        this.jComboBoxSize.addItem("LETTER");
        this.jComboBoxSize.addItem("NOTE");
        this.jComboBoxSize.addItem("LEGAL");
        this.jComboBoxSize.addItem("A0");
        this.jComboBoxSize.addItem("A1");
        this.jComboBoxSize.addItem("A2");
        this.jComboBoxSize.addItem("A3");
        this.jComboBoxSize.addItem("A4");
        this.jComboBoxSize.addItem("A5");
        this.jComboBoxSize.addItem("A6");
        this.jComboBoxSize.addItem("A7");
        this.jComboBoxSize.addItem("A8");
        this.jComboBoxSize.addItem("A9");
        this.jComboBoxSize.addItem("A10");
        this.jComboBoxSize.addItem("B0");
        this.jComboBoxSize.addItem("B1");
        this.jComboBoxSize.addItem("B2");
        this.jComboBoxSize.addItem("B3");
        this.jComboBoxSize.addItem("B4");
        this.jComboBoxSize.addItem("B5");
        this.jComboBoxSize.addItem("ARCH_E");
        this.jComboBoxSize.addItem("ARCH_D");
        this.jComboBoxSize.addItem("ARCH_C");
        this.jComboBoxSize.addItem("ARCH_B");
        this.jComboBoxSize.addItem("ARCH_A");
        this.jComboBoxSize.addItem("FLSA");
        this.jComboBoxSize.addItem("FLSE");
        this.jComboBoxSize.addItem("HALFLETTER");
        this.jComboBoxSize.addItem("11x17");
        this.jComboBoxSize.addItem("LEDGER");

        this.jComboBoxOrientation.addItem(new Tag("Portrait", it.businesslogic.ireport.util.I18n.getString("pageOrientation.Portrait","Portrait") ));
        this.jComboBoxOrientation.addItem(new Tag("Landscape", it.businesslogic.ireport.util.I18n.getString("pageOrientation.Landscape","Landscape") ));

        this.jComboBoxPrintOrder.addItem(new Tag("Vertical", it.businesslogic.ireport.util.I18n.getString("printOrder.Vertical","Vertical")));
        this.jComboBoxPrintOrder.addItem(new Tag("Horizontal", it.businesslogic.ireport.util.I18n.getString("printOrder.Horizontal","Horizontal")));

        this.jComboBoxWhenNoData.addItem(new Tag("NoPages", it.businesslogic.ireport.util.I18n.getString("whenNoData.NoPages","No pages")));
        this.jComboBoxWhenNoData.addItem(new Tag("BlankPage", it.businesslogic.ireport.util.I18n.getString("whenNoData.BlankPage","Blank page")));
        this.jComboBoxWhenNoData.addItem(new Tag("AllSectionsNoDetail", it.businesslogic.ireport.util.I18n.getString("whenNoData.AllSectionsNoDetail","All sections, no detail")));
        this.jComboBoxWhenNoData.addItem(new Tag("NoDataSection", it.businesslogic.ireport.util.I18n.getString("whenNoData.NoDataSection","No-data section")));
        
        
        
        this.jComboBoxXMLEncoding.addItem("UTF-8");
        this.jComboBoxXMLEncoding.addItem("ISO-8859-1");

        this.jComboBoxLanguage.addItem("java");
        this.jComboBoxLanguage.addItem("groovy");

        this.jComboBoxLanguage.setSelectedIndex(0);
        // ********** Setting defaults ***************

        Misc.setComboboxSelectedTagValue( jComboBoxPrintOrder, "Vertical" );
        this.jComboBoxWhenNoData.setSelectedItem("NoPages");
        this.jComboBoxXMLEncoding.setSelectedItem("UTF-8");

        this.jComboBoxWhenResourceMissingType.addItem(new Tag("Null",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Null","Null")));
        this.jComboBoxWhenResourceMissingType.addItem(new Tag("Empty",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Empty","Empty")));
        this.jComboBoxWhenResourceMissingType.addItem(new Tag("Key",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Key","Key")));
        this.jComboBoxWhenResourceMissingType.addItem(new Tag("Error",it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType.Error","Error")));

        this.setReportFormat("A4");
        this.setReportName(it.businesslogic.ireport.util.I18n.getString("untitledReport", "untitled_report_")+"1");//I18N on April 28th 2006
        this.setReportWidth(  PageSize.A4.x);
        this.setReportHeight( PageSize.A4.y);
        this.setTopMargin(20);
        this.setBottomMargin(20);
        this.setLeftMargin(30);
        this.setRightMargin(30);
        this.setColumns(1);
        this.setColumnsSpacing(0);
        this.setColumnsWidth(getReportWidth() - getLeftMargin() - getRightMargin());

        String unit = MainFrame.getMainInstance().getProperties().getProperty("DefaultUnit","cm");

        //jComboBoxWUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxWUnit,0,""));
        //jComboBoxHUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxHUnit,0,""));
        int index = Unit.getUnitIndex(unit);
        if (index < 0) index =2;
        setGlobalUnit( index );

        this.setLanguage( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("DefaultScriptingLanguage","java") );
        applyI18n();
        pack();
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCANCELActionPerformed(e);
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

        jPanel8 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCANCEL = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxSize = new javax.swing.JComboBox();
        jComboBoxWUnit = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxHUnit = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxOrientation = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jNumberFieldWidth = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldHeight = new it.businesslogic.ireport.gui.JNumberField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jComboBoxTopMarginUnit = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxBottomMarginUnit = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxLeftMarginUnit = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxRightMarginUnit = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jNumberFieldTopMargin = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldBottomMargin = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldLeftMargin = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldRightMargin = new it.businesslogic.ireport.gui.JNumberField();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxColumnsWidthUnit = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxColumnsSpacingUnit = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jNumberFieldColumnsWidth = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldColumnsSpacing = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldColumns = new it.businesslogic.ireport.gui.JNumberField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldScriptletClass = new javax.swing.JTextField();
        jComboBoxScriptletHandling = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jComboBoxLanguage = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jCheckBoxTitleOnNewPage = new javax.swing.JCheckBox();
        jCheckBoxSummaryOnNewPage = new javax.swing.JCheckBox();
        jComboBoxPrintOrder = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxWhenNoData = new javax.swing.JComboBox();
        jCheckBoxFloatColumnFooter = new javax.swing.JCheckBox();
        jCheckBoxIgnorePagination = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jLabelFormatFactoryClass = new javax.swing.JLabel();
        jTextFieldFormatFactoryClass = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldResourceBundleBaseName = new javax.swing.JTextField();
        jLabelWhenResourceMissingType = new javax.swing.JLabel();
        jComboBoxWhenResourceMissingType = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jComboBoxXMLEncoding = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Report properties");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Report properties");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jButtonOK.setText("OK");
        jButtonOK.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonOK.setMinimumSize(new java.awt.Dimension(150, 26));
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel8.add(jButtonOK, gridBagConstraints);

        jButtonCANCEL.setText("Cancel");
        jButtonCANCEL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCANCELActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel8.add(jButtonCANCEL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 3);
        getContentPane().add(jPanel8, gridBagConstraints);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Page size"));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Preset sizes:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel1.add(jLabel2, gridBagConstraints);

        jComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSizeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel1.add(jComboBoxSize, gridBagConstraints);

        jComboBoxWUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxWUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jComboBoxWUnit, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Width:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel3, gridBagConstraints);

        jComboBoxHUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jComboBoxHUnit, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Height:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel4, gridBagConstraints);

        jComboBoxOrientation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrientationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jComboBoxOrientation, gridBagConstraints);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Orientation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jLabel5, gridBagConstraints);

        jNumberFieldWidth.setColumns(5);
        jNumberFieldWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldWidth.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldWidthActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jNumberFieldWidth, gridBagConstraints);

        jNumberFieldHeight.setColumns(5);
        jNumberFieldHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldHeight.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldHeightActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel1.add(jNumberFieldHeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel10.add(jPanel1, gridBagConstraints);

        jTabbedPane1.setName("");
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setName("");
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Page margin"));
        jComboBoxTopMarginUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTopMarginUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jComboBoxTopMarginUnit, gridBagConstraints);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Top:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jLabel7, gridBagConstraints);

        jComboBoxBottomMarginUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBottomMarginUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jComboBoxBottomMarginUnit, gridBagConstraints);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Bottom:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jLabel8, gridBagConstraints);

        jComboBoxLeftMarginUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLeftMarginUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jComboBoxLeftMarginUnit, gridBagConstraints);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Left:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jLabel9, gridBagConstraints);

        jComboBoxRightMarginUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRightMarginUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jComboBoxRightMarginUnit, gridBagConstraints);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Right:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jLabel10, gridBagConstraints);

        jNumberFieldTopMargin.setColumns(4);
        jNumberFieldTopMargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldTopMargin.setText("0");
        jNumberFieldTopMargin.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldTopMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldTopMarginActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jNumberFieldTopMargin, gridBagConstraints);

        jNumberFieldBottomMargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldBottomMargin.setText("0");
        jNumberFieldBottomMargin.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldBottomMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldBottomMarginActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jNumberFieldBottomMargin, gridBagConstraints);

        jNumberFieldLeftMargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldLeftMargin.setText("0");
        jNumberFieldLeftMargin.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldLeftMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldLeftMarginActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jNumberFieldLeftMargin, gridBagConstraints);

        jNumberFieldRightMargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldRightMargin.setText("0");
        jNumberFieldRightMargin.setMinimumSize(new java.awt.Dimension(40, 20));
        jNumberFieldRightMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldRightMarginActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel6.add(jNumberFieldRightMargin, gridBagConstraints);

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("tab1", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Report columns"));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Columns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel7.add(jLabel11, gridBagConstraints);

        jComboBoxColumnsWidthUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColumnsWidthUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jComboBoxColumnsWidthUnit, gridBagConstraints);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Width:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jLabel12, gridBagConstraints);

        jComboBoxColumnsSpacingUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColumnsSpacingUnitActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jComboBoxColumnsSpacingUnit, gridBagConstraints);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Spacing:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jLabel13, gridBagConstraints);

        jNumberFieldColumnsWidth.setColumns(5);
        jNumberFieldColumnsWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldColumnsWidth.setText("0");
        jNumberFieldColumnsWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldColumnsWidthActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jNumberFieldColumnsWidth, gridBagConstraints);

        jNumberFieldColumnsSpacing.setColumns(5);
        jNumberFieldColumnsSpacing.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldColumnsSpacing.setText("0");
        jNumberFieldColumnsSpacing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldColumnsSpacingActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel7.add(jNumberFieldColumnsSpacing, gridBagConstraints);

        jNumberFieldColumns.setColumns(4);
        jNumberFieldColumns.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldColumns.setText("0");
        try {
            jNumberFieldColumns.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldColumnsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel7.add(jNumberFieldColumns, gridBagConstraints);

        jPanel3.add(jPanel7, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("tab2", jPanel3);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Scriptlet class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 3, 3);
        jPanel4.add(jLabel6, gridBagConstraints);

        jTextFieldScriptletClass.setEnabled(false);
        jTextFieldScriptletClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldScriptletClassActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 7);
        jPanel4.add(jTextFieldScriptletClass, gridBagConstraints);

        jComboBoxScriptletHandling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxScriptletHandlingActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 7);
        jPanel4.add(jComboBoxScriptletHandling, gridBagConstraints);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("Language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 3, 3);
        jPanel4.add(jLabel18, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 7);
        jPanel4.add(jComboBoxLanguage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel13, gridBagConstraints);

        jTabbedPane1.addTab("tab3", jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jCheckBoxTitleOnNewPage.setText("Title on a new page");
        jCheckBoxTitleOnNewPage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxTitleOnNewPage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxTitleOnNewPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTitleOnNewPageActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel5.add(jCheckBoxTitleOnNewPage, gridBagConstraints);

        jCheckBoxSummaryOnNewPage.setText("Summary on a new page");
        jCheckBoxSummaryOnNewPage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxSummaryOnNewPage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxSummaryOnNewPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSummaryOnNewPageActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel5.add(jCheckBoxSummaryOnNewPage, gridBagConstraints);

        jComboBoxPrintOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPrintOrderActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel5.add(jComboBoxPrintOrder, gridBagConstraints);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Print order:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel5.add(jLabel14, gridBagConstraints);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("When no data:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel5.add(jLabel15, gridBagConstraints);

        jComboBoxWhenNoData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxWhenNoDataActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel5.add(jComboBoxWhenNoData, gridBagConstraints);

        jCheckBoxFloatColumnFooter.setText("Floating column footer");
        jCheckBoxFloatColumnFooter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxFloatColumnFooter.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxFloatColumnFooter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFloatColumnFooterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        jPanel5.add(jCheckBoxFloatColumnFooter, gridBagConstraints);

        jCheckBoxIgnorePagination.setText("Ignore pagination");
        jCheckBoxIgnorePagination.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxIgnorePagination.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBoxIgnorePagination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnorePaginationActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel5.add(jCheckBoxIgnorePagination, gridBagConstraints);

        jTabbedPane1.addTab("tab4", jPanel5);

        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabelFormatFactoryClass.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelFormatFactoryClass.setText("Format factory class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 3, 3);
        jPanel11.add(jLabelFormatFactoryClass, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 7);
        jPanel11.add(jTextFieldFormatFactoryClass, gridBagConstraints);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Resource bundle base name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 3, 3);
        jPanel11.add(jLabel17, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 4, 7);
        jPanel11.add(jTextFieldResourceBundleBaseName, gridBagConstraints);

        jLabelWhenResourceMissingType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelWhenResourceMissingType.setText("When resource missing type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 3, 3);
        jPanel11.add(jLabelWhenResourceMissingType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 16, 7);
        jPanel11.add(jComboBoxWhenResourceMissingType, gridBagConstraints);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("XML encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 3, 3);
        jPanel11.add(jLabel16, gridBagConstraints);

        jComboBoxXMLEncoding.setEditable(true);
        jComboBoxXMLEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxXMLEncodingActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 5, 7);
        jPanel11.add(jComboBoxXMLEncoding, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel11.add(jPanel12, gridBagConstraints);

        jTabbedPane1.addTab("i18n", jPanel11);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel10.add(jTabbedPane1, gridBagConstraints);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Report name:");
        jLabel1.setMaximumSize(new java.awt.Dimension(2000, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jLabel1, gridBagConstraints);

        jTextFieldName.setMinimumSize(new java.awt.Dimension(300, 20));
        jTextFieldName.setPreferredSize(new java.awt.Dimension(300, 20));
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel9.add(jTextFieldName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel10.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1000.0;
        getContentPane().add(jPanel10, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxIgnorePaginationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnorePaginationActionPerformed
        ignorePagination = jCheckBoxIgnorePagination.isSelected();
    }//GEN-LAST:event_jCheckBoxIgnorePaginationActionPerformed

    private void jCheckBoxFloatColumnFooterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFloatColumnFooterActionPerformed
        floatColumnFooter = jCheckBoxFloatColumnFooter.isSelected();
    }//GEN-LAST:event_jCheckBoxFloatColumnFooterActionPerformed

    private void jComboBoxScriptletHandlingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxScriptletHandlingActionPerformed
        if (jComboBoxScriptletHandling.getSelectedIndex() == 0) {
            jTextFieldScriptletClass.setText("");
            jTextFieldScriptletClass.setEnabled( false );
        }
        else if (jComboBoxScriptletHandling.getSelectedIndex() == 1) {
            jTextFieldScriptletClass.setText("");
            jTextFieldScriptletClass.setEnabled( false );
        }
        else if (jComboBoxScriptletHandling.getSelectedIndex() == 2) {
            jTextFieldScriptletClass.setEnabled( true );
        }
        this.scriptletHandling = jComboBoxScriptletHandling.getSelectedIndex();

    }//GEN-LAST:event_jComboBoxScriptletHandlingActionPerformed

    private void jCheckBoxSummaryOnNewPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSummaryOnNewPageActionPerformed
        this.summaryOnNewPage = jCheckBoxSummaryOnNewPage.isSelected();
    }//GEN-LAST:event_jCheckBoxSummaryOnNewPageActionPerformed

    private void jComboBoxOrientationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrientationActionPerformed
        
        
        orientation = (String)Misc.getComboboxSelectedValue(jComboBoxOrientation);
        int reportSwitch = 0;
        adjustSizes();
        recalcColumnWidth();
    }//GEN-LAST:event_jComboBoxOrientationActionPerformed

    private void jComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSizeActionPerformed
        reportFormat = (String)this.jComboBoxSize.getSelectedItem();
        if( reportFormat!=null && !reportFormat.equalsIgnoreCase( "CUSTOM") ) {
            // set the Size fields.
            Point p = PageSize.getFormatSize(reportFormat);

            setReportWidth(p.x);
            setReportHeight(p.y);

            // reshuffle Height and Width according to Orientation
            adjustSizes();
            recalcColumnWidth();
        }
    }//GEN-LAST:event_jComboBoxSizeActionPerformed

    private void jComboBoxXMLEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxXMLEncodingActionPerformed
        this.xmlEncoding = jComboBoxXMLEncoding.getSelectedItem()+"";
    }//GEN-LAST:event_jComboBoxXMLEncodingActionPerformed

    private void jComboBoxWhenNoDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxWhenNoDataActionPerformed
        this.whenNoDataType = Misc.getComboboxSelectedValue( jComboBoxWhenNoData )+"";
    }//GEN-LAST:event_jComboBoxWhenNoDataActionPerformed

    private void jComboBoxPrintOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPrintOrderActionPerformed
        this.printOrder =  Misc.getComboboxSelectedValue(jComboBoxPrintOrder)+"";
    }//GEN-LAST:event_jComboBoxPrintOrderActionPerformed

    private void jNumberFieldColumnsSpacingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldColumnsSpacingActionPerformed

        double convert = 1.0;
        convert = this.getConversionUnit(this.jComboBoxColumnsSpacingUnit);
        //int old_spacing = getColumnsSpacing();
        this.columnsSpacing  = (int)Unit.convertToPixels(jNumberFieldColumnsSpacing.getValue(),convert);

        int avail = getReportWidth() - getLeftMargin() - getRightMargin();
        int dim = avail;

        // calculate space...
        if ((this.columnsSpacing*(getColumns()-1)) > avail) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString("messages.reportPropertiesFrame.invalidColumnSpacing",
                    "Column spacing too big!"), I18n.getString("message.title.error","Error"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
        }
        while (dim>0 && (dim*getColumns())+(getColumnsSpacing()*(getColumns()-1)) > avail) {
            dim--;
        }

        this.setColumnsWidth(dim);
    }//GEN-LAST:event_jNumberFieldColumnsSpacingActionPerformed

    private void jNumberFieldColumnsWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldColumnsWidthActionPerformed
        int newColumnsWidth = (int)Unit.convertToPixels(this.jNumberFieldColumnsWidth.getValue(),this.getConversionUnit(jComboBoxColumnsWidthUnit));
        int total =  (newColumnsWidth * (int)jNumberFieldColumns.getValue()) + ((int)Unit.convertToPixels(this.jNumberFieldColumnsSpacing.getValue(),this.getConversionUnit(jComboBoxColumnsWidthUnit)) * ((int)jNumberFieldColumnsSpacing.getValue()-1));
        if (total > this.getReportWidth() - this.getLeftMargin() - this.getRightMargin())
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString("messages.reportPropertiesFrame.invalidColumnSize",
                    "The column size is too big. Enlarge the report width first.") );
            recalcColumnWidth();
        }
    }//GEN-LAST:event_jNumberFieldColumnsWidthActionPerformed

    private void jComboBoxColumnsWidthUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColumnsWidthUnitActionPerformed
        this.adjustValue(jComboBoxColumnsWidthUnit,jNumberFieldColumnsWidth, this.getColumnsWidth() );
    }//GEN-LAST:event_jComboBoxColumnsWidthUnitActionPerformed

    private void jNumberFieldColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldColumnsActionPerformed
        recalcColumnWidth();
    }//GEN-LAST:event_jNumberFieldColumnsActionPerformed




    private void jComboBoxColumnsSpacingUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColumnsSpacingUnitActionPerformed
        this.adjustValue(jComboBoxColumnsSpacingUnit,jNumberFieldColumnsSpacing, this.getColumnsSpacing() );
    }//GEN-LAST:event_jComboBoxColumnsSpacingUnitActionPerformed

    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNameActionPerformed
        this.reportName = jTextFieldName.getText().trim();

    }//GEN-LAST:event_jTextFieldNameActionPerformed

    private void jTextFieldScriptletClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldScriptletClassActionPerformed
        this.scriptletClass = jTextFieldScriptletClass.getText().trim();
    }//GEN-LAST:event_jTextFieldScriptletClassActionPerformed

    private void jComboBoxTopMarginUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTopMarginUnitActionPerformed
        this.adjustValue(jComboBoxTopMarginUnit,jNumberFieldTopMargin, this.getTopMargin());
    }//GEN-LAST:event_jComboBoxTopMarginUnitActionPerformed

    private void jComboBoxBottomMarginUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBottomMarginUnitActionPerformed
        this.adjustValue(jComboBoxBottomMarginUnit,jNumberFieldBottomMargin, this.getBottomMargin());
    }//GEN-LAST:event_jComboBoxBottomMarginUnitActionPerformed

    private void jComboBoxLeftMarginUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLeftMarginUnitActionPerformed
        this.adjustValue(jComboBoxLeftMarginUnit,jNumberFieldLeftMargin, this.getLeftMargin());
    }//GEN-LAST:event_jComboBoxLeftMarginUnitActionPerformed

    private void jComboBoxRightMarginUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRightMarginUnitActionPerformed
        this.adjustValue(jComboBoxRightMarginUnit,jNumberFieldRightMargin, this.getRightMargin());
    }//GEN-LAST:event_jComboBoxRightMarginUnitActionPerformed

    private void jNumberFieldRightMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldRightMarginActionPerformed
        rightMargin = (int)Unit.convertToPixels(this.jNumberFieldRightMargin.getValue(),this.getConversionUnit(jComboBoxRightMarginUnit));
        recalcColumnWidth();
    }//GEN-LAST:event_jNumberFieldRightMarginActionPerformed

    private void jNumberFieldLeftMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldLeftMarginActionPerformed
        leftMargin = (int)Unit.convertToPixels(this.jNumberFieldLeftMargin.getValue(),
                      this.getConversionUnit(jComboBoxLeftMarginUnit));
        recalcColumnWidth();
    }//GEN-LAST:event_jNumberFieldLeftMarginActionPerformed

    private void jNumberFieldBottomMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldBottomMarginActionPerformed
        bottomMargin = (int)Unit.convertToPixels(this.jNumberFieldBottomMargin.getValue(),this.getConversionUnit(jComboBoxBottomMarginUnit));
    }//GEN-LAST:event_jNumberFieldBottomMarginActionPerformed

    private void jNumberFieldTopMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldTopMarginActionPerformed
        topMargin = (int)Unit.convertToPixels(this.jNumberFieldTopMargin.getValue(),this.getConversionUnit(jComboBoxTopMarginUnit));
    }//GEN-LAST:event_jNumberFieldTopMarginActionPerformed

    private void jComboBoxHUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHUnitActionPerformed
        this.adjustValue(jComboBoxHUnit,jNumberFieldHeight, this.getReportHeight());
    }//GEN-LAST:event_jComboBoxHUnitActionPerformed

    private void jNumberFieldHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldHeightActionPerformed
        this.jComboBoxSize.setSelectedItem("Custom");
        reportHeight = (int)Unit.convertToPixels(this.jNumberFieldHeight.getValue(),this.getConversionUnit(jComboBoxHUnit));
        adjustOrientation();
    }//GEN-LAST:event_jNumberFieldHeightActionPerformed

    private void jNumberFieldWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldWidthActionPerformed
        this.jComboBoxSize.setSelectedItem("Custom");
        reportWidth = (int)Unit.convertToPixels(this.jNumberFieldWidth.getValue(),this.getConversionUnit(jComboBoxWUnit));
        adjustOrientation();
        recalcColumnWidth();
    }//GEN-LAST:event_jNumberFieldWidthActionPerformed

    private void jComboBoxWUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxWUnitActionPerformed
        this.adjustValue(jComboBoxWUnit,jNumberFieldWidth, this.getReportWidth());

    }//GEN-LAST:event_jComboBoxWUnitActionPerformed

    private void jCheckBoxTitleOnNewPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTitleOnNewPageActionPerformed
        this.titleOnNewPage = jCheckBoxTitleOnNewPage.isSelected();
    }//GEN-LAST:event_jCheckBoxTitleOnNewPageActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        this.reportName = jTextFieldName.getText();
        this.scriptletClass = jTextFieldScriptletClass.getText();
                this.setResourceBundleBaseName(jTextFieldResourceBundleBaseName.getText());

        this.language = this.jComboBoxLanguage.getSelectedItem() + "";
        this.whenResourceMissingType = ((Tag)jComboBoxWhenResourceMissingType.getSelectedItem()).getValue()+"";
        this.setFormatFactoryClass(jTextFieldFormatFactoryClass.getText());
        
        //System.out.println("Il nome :"+this.getReportName());
        this.setDialogResult( JOptionPane.OK_OPTION );
        this.setVisible(false);

        //this.dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void jButtonCANCELActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCANCELActionPerformed
        this.setDialogResult( JOptionPane.CANCEL_OPTION );
        this.dispose();
    }//GEN-LAST:event_jButtonCANCELActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.setDialogResult(  JOptionPane.CLOSED_OPTION );
        this.dispose();
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ReportPropertiesFrame(null,true).setVisible(true);
    }

    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return this.dialogResult;
    }

    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }


    /**
     *  This method convert the old value in new value according with the Unit selected
     */
    public void adjustValue(javax.swing.JComboBox unitCombo, JNumberField numberField, int store_value) {
        try {
            if (unitCombo.getSelectedIndex() == 0) {
                numberField.setDecimals(0);
                numberField.setInteger(true);
                numberField.setValue(store_value);
            }
            else if (unitCombo.getSelectedIndex() == 1) {
                numberField.setDecimals(3);
                numberField.setInteger(false);
                numberField.setValue( Unit.convertPixelsToInches(store_value) );
            }
            else if (unitCombo.getSelectedIndex() == 2) {
                numberField.setDecimals(3);
                numberField.setInteger(false);
                numberField.setValue( Unit.convertPixelsToCentimeters( store_value) );
            }
            else if (unitCombo.getSelectedIndex() == 3) {
                numberField.setDecimals(3);
                numberField.setInteger(false);
                numberField.setValue( Unit.convertPixelsToMillimeters( store_value) );
            }
        } catch (Exception ex) {}
    }


    private double getConversionUnit(javax.swing.JComboBox comboBox) {
        double convert = 1.0;
        if (comboBox.getSelectedIndex() == 1) convert = Unit.INCHES;
        else if (comboBox.getSelectedIndex() == 2) convert = Unit.CENTIMETERS;
        else if (comboBox.getSelectedIndex() == 3) convert = Unit.MILLIMETERS;
        return convert;
    }

    /** Getter for property reportHeight.
     * @return Value of property reportHeight.
     *
     */
    public int getReportHeight() {
        return reportHeight;
    }

    /** Setter for property reportHeight.
     * @param reportHeight New value of property reportHeight.
     *
     */
    public void setReportHeight(int reportHeight) {
        this.reportHeight = reportHeight;
        jComboBoxHUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxHUnit,0,""));
    }

    /** Getter for property reportWidth.
     * @return Value of property reportWidth.
     *
     */
    public int getReportWidth() {
        return reportWidth;
    }

    /** Setter for property reportWidth.
     * @param reportWidth New value of property reportWidth.
     *
     */
    public void setReportWidth(int reportWidth) {
        this.reportWidth = reportWidth;
        jComboBoxWUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxWUnit,0,""));
    }

    /** Getter for property rightMargin.
     * @return Value of property rightMargin.
     *
     */
    public int getRightMargin() {
        return rightMargin;
    }

    /** Setter for property rightMargin.
     * @param rightMargin New value of property rightMargin.
     *
     */
    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        jComboBoxRightMarginUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxRightMarginUnit,0,""));
    }

    /** Getter for property topMargin.
     * @return Value of property topMargin.
     *
     */
    public int getTopMargin() {
        return topMargin;
    }

    /** Setter for property topMargin.
     * @param topMargin New value of property topMargin.
     *
     */
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        jComboBoxTopMarginUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxTopMarginUnit,0,""));
    }

    /** Getter for property leftMargin.
     * @return Value of property leftMargin.
     *
     */
    public int getLeftMargin() {
        return leftMargin;
    }

    /** Setter for property leftMargin.
     * @param leftMargin New value of property leftMargin.
     *
     */
    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        jComboBoxLeftMarginUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxLeftMarginUnit,0,""));
    }

    /** Getter for property bottomMargin.
     * @return Value of property bottomMargin.
     *
     */
    public int getBottomMargin() {
        return bottomMargin;
    }

    /** Setter for property bottomMargin.
     * @param bottomMargin New value of property bottomMargin.
     *
     */
    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        jComboBoxBottomMarginUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxBottomMarginUnit,0,""));
    }

    /** Getter for property orientation.
     * @return Value of property orientation.
     *
     */
    public java.lang.String getOrientation() {
        return orientation;
    }

    /** Setter for property orientation.
     * @param orientation New value of property orientation.
     *
     */
    public void setOrientation(java.lang.String orientation) {
        
        Misc.setComboboxSelectedTagValue(jComboBoxOrientation,orientation);
        this.orientation = orientation;
    }

    /** Getter for property scriptletClass.
     * @return Value of property scriptletClass.
     *
     */
    public java.lang.String getScriptletClass() {
        return scriptletClass;
    }

    /** Setter for property scriptletClass.
     * @param scriptletClass New value of property scriptletClass.
     *
     */
    public void setScriptletClass(java.lang.String scriptletClass) {
        jTextFieldScriptletClass.setText(Misc.nvl(scriptletClass,""));
        this.scriptletClass = scriptletClass;

    }

    /** Getter for property reportName.
     * @return Value of property reportName.
     *
     */
    public java.lang.String getReportName() {
        return reportName;
    }

    /** Setter for property reportName.
     * @param reportName New value of property reportName.
     *
     */
    public void setReportName(java.lang.String reportName) {
        this.reportName = reportName;
        this.jTextFieldName.setText(reportName);
    }

    /** Getter for property columns.
     * @return Value of property columns.
     *
     */
    public int getColumns() {
        return columns;
    }

    /** Setter for property columns.
     * @param columns New value of property columns.
     *
     */
    public void setColumns(int columns) {
        this.columns = columns;
        try {
            jNumberFieldColumns.setValue(columns);
        } catch (Exception ex) {}
    }

    /** Getter for property columnsSpacing.
     * @return Value of property columnsSpacing.
     *
     */
    public int getColumnsSpacing() {

        return columnsSpacing;
    }

    /** Setter for property columnsSpacing.
     * @param columnsSpacing New value of property columnsSpacing.
     *
     */
    public void setColumnsSpacing(int columnsSpacing) {
        this.columnsSpacing = columnsSpacing;
        jComboBoxColumnsSpacingUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxColumnsSpacingUnit,0,""));
    }

    /** Getter for property columnsWidth.
     * @return Value of property columnsWidth.
     *
     */
    public int getColumnsWidth() {
        return columnsWidth;
    }

    /** Setter for property columnsWidth.
     * @param columnsWidth New value of property columnsWidth.
     *
     */
    public void setColumnsWidth(int columnsWidth) {
        this.columnsWidth = columnsWidth;
        jComboBoxColumnsWidthUnitActionPerformed(new java.awt.event.ActionEvent(jComboBoxColumnsWidthUnit,0,""));
        //System.out.println("Column width = " + this.getColumnsWidth());
    }

    /** Getter for property summaryOnNewPage.
     * @return Value of property summaryOnNewPage.
     *
     */
    public boolean isSummaryOnNewPage() {
        return summaryOnNewPage;
    }

    /** Setter for property summaryOnNewPage.
     * @param summaryOnNewPage New value of property summaryOnNewPage.
     *
     */
    public void setSummaryOnNewPage(boolean summaryOnNewPage) {
        jCheckBoxSummaryOnNewPage.setSelected(summaryOnNewPage);
        this.summaryOnNewPage = summaryOnNewPage;
    }

    /** Getter for property titleOnNewPage.
     * @return Value of property titleOnNewPage.
     *
     */
    public boolean isTitleOnNewPage() {
        return titleOnNewPage;
    }

    /** Setter for property titleOnNewPage.
     * @param titleOnNewPage New value of property titleOnNewPage.
     *
     */
    public void setTitleOnNewPage(boolean titleOnNewPage) {
        jCheckBoxTitleOnNewPage.setSelected(titleOnNewPage);
        this.titleOnNewPage = titleOnNewPage;
    }

    /** Getter for property whenNoDataType.
     * @return Value of property whenNoDataType.
     *
     */
    public java.lang.String getWhenNoDataType() {
        return whenNoDataType;
    }

    /** Setter for property whenNoDataType.
     * @param whenNoDataType New value of property whenNoDataType.
     *
     */
    public void setWhenNoDataType(java.lang.String whenNoDataType) {
        Misc.setComboboxSelectedTagValue(jComboBoxWhenNoData, whenNoDataType);
        this.whenNoDataType = whenNoDataType;
    }

    /** Getter for property xmlEncoding.
     * @return Value of property xmlEncoding.
     *
     */
    public java.lang.String getXmlEncoding() {
        return xmlEncoding;
    }

    /** Setter for property xmlEncoding.
     * @param xmlEncoding New value of property xmlEncoding.
     *
     */
    public void setXmlEncoding(java.lang.String xmlEncoding) {
        this.jComboBoxXMLEncoding.setSelectedItem(xmlEncoding);
        this.xmlEncoding = xmlEncoding;
    }

    /** Getter for property globalUnit.
     * @return Value of property globalUnit.
     *
     */
    public int getGlobalUnit() {
        return globalUnit;
    }

    /** Setter for property globalUnit.
     * @param globalUnit New value of property globalUnit.
     *
     */
    public void setGlobalUnit(int globalUnit) {
        if ( this.globalUnit != globalUnit) {
            this.globalUnit = globalUnit;
            jComboBoxWUnit.setSelectedIndex(globalUnit);
            jComboBoxHUnit.setSelectedIndex(globalUnit);
            jComboBoxTopMarginUnit.setSelectedIndex(globalUnit);
            jComboBoxBottomMarginUnit.setSelectedIndex(globalUnit);
            jComboBoxLeftMarginUnit.setSelectedIndex(globalUnit);
            jComboBoxRightMarginUnit.setSelectedIndex(globalUnit);
            jComboBoxColumnsSpacingUnit.setSelectedIndex(globalUnit);
            jComboBoxColumnsWidthUnit.setSelectedIndex(globalUnit);

        }
    }

    /** Getter for property printOrder.
     * @return Value of property printOrder.
     *
     */
    public java.lang.String getPrintOrder() {
        return printOrder;
    }

    /** Setter for property printOrder.
     * @param printOrder New value of property printOrder.
     *
     */
    public void setPrintOrder(java.lang.String printOrder) {

        Misc.setComboboxSelectedTagValue( jComboBoxPrintOrder, printOrder );
        this.printOrder = printOrder;
    }

    /** Getter for property reportFormat.
     * @return Value of property reportFormat.
     *
     */
    public java.lang.String getReportFormat() {
        return reportFormat;
    }

    /** Setter for property reportFormat.
     * @param reportFormat New value of property reportFormat.
     *
     */
    public void setReportFormat(java.lang.String reportFormat) {
        this.reportFormat = reportFormat;
        this.jComboBoxSize.setSelectedItem(reportFormat);
    }

    /** Getter for property scriptletHandling.
     * @return Value of property scriptletHandling.
     *
     */
    public int getScriptletHandling() {
        return scriptletHandling;
    }

    /** Setter for property scriptletHandling.
     * @param scriptletHandling New value of property scriptletHandling.
     *
     */
    public void setScriptletHandling(int scriptletHandling) {
        this.scriptletHandling = scriptletHandling;
        jComboBoxScriptletHandling.setSelectedIndex( scriptletHandling );
    }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxFloatColumnFooter.setText(I18n.getString("reportPropertiesFrame.checkBoxFloatColumnFooter","Floating column footer"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButtonOK.setText(I18n.getString("reportPropertiesFrame.buttonOK","OK"));
                jLabel18.setText(I18n.getString("reportPropertiesFrame.label18","Language"));
                // End autogenerated code ----------------------
        jLabel1.setText(it.businesslogic.ireport.util.I18n.getString("reportName","Report name")+":");
        jLabel2.setText(it.businesslogic.ireport.util.I18n.getString("presetSizes","Preset sizes")+":");
        jLabel3.setText(it.businesslogic.ireport.util.I18n.getString("width","Width")+":");
        jLabel4.setText(it.businesslogic.ireport.util.I18n.getString("height","Height")+":");
        jLabel5.setText(it.businesslogic.ireport.util.I18n.getString("orientation","Orientation")+":");
        jButtonCANCEL.setText(it.businesslogic.ireport.util.I18n.getString("cancel","Cancel"));
        jLabel7.setText(it.businesslogic.ireport.util.I18n.getString("top","Top")+":");
        jLabel8.setText(it.businesslogic.ireport.util.I18n.getString("bottom","Bottom")+":");
        jLabel9.setText(it.businesslogic.ireport.util.I18n.getString("left","Left")+":");
        jLabel10.setText(it.businesslogic.ireport.util.I18n.getString("right","Right")+":");
        jLabel11.setText(it.businesslogic.ireport.util.I18n.getString("columns","Columns")+":");
        jLabel12.setText(it.businesslogic.ireport.util.I18n.getString("width","Width")+":");
        jLabel13.setText(it.businesslogic.ireport.util.I18n.getString("spacing","Spacing")+":");
        jLabel6.setText(it.businesslogic.ireport.util.I18n.getString("scriptlet","Scriptlet class"));
        jCheckBoxTitleOnNewPage.setText(it.businesslogic.ireport.util.I18n.getString("titleOnANewPage","Title on a new page"));
        jCheckBoxSummaryOnNewPage.setText(it.businesslogic.ireport.util.I18n.getString("summaryInANewPage","Summary on a new page"));
        jLabel14.setText(it.businesslogic.ireport.util.I18n.getString("printOrder","Print order")+":");
        jLabel15.setText(it.businesslogic.ireport.util.I18n.getString("whenNoData","When no data")+":");
        jLabel16.setText(it.businesslogic.ireport.util.I18n.getString("xmlEncoding","XML encoding")+":");
        jLabel17.setText(it.businesslogic.ireport.util.I18n.getString("resourceBundleBaseName","Resource bundle base name"));
        jLabelWhenResourceMissingType.setText(it.businesslogic.ireport.util.I18n.getString("whenResourceMissingType","When resource missing type"));
        jCheckBoxIgnorePagination.setText(it.businesslogic.ireport.util.I18n.getString("ignorePagination", "Ignore pagination"));
        jLabelFormatFactoryClass.setText(it.businesslogic.ireport.util.I18n.getString("formatFactoryClass","Format factory class"));
    
    
        this.jTabbedPane1.setTitleAt(0,it.businesslogic.ireport.util.I18n.getString("pageMargin", "Page Margin"));
        this.jTabbedPane1.setTitleAt(1,it.businesslogic.ireport.util.I18n.getString("columns", "Columns"));
        this.jTabbedPane1.setTitleAt(2,it.businesslogic.ireport.util.I18n.getString("scriptlet", "Scriptlet"));
        this.jTabbedPane1.setTitleAt(3, it.businesslogic.ireport.util.I18n.getString("more", "More..."));
        
        ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.PageSize","Page size") );
        ((javax.swing.border.TitledBorder)jPanel6.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.PageMargin","Page margin") );
        ((javax.swing.border.TitledBorder)jPanel7.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("reportPropertiesFrame.panelBorder.ReportColumns","Report columns") );
        this.setTitle(I18n.getString("reportPropertiesFrame.title","Report properties"));
        jButtonCANCEL.setMnemonic(I18n.getString("reportPropertiesFrame.buttonCANCELMnemonic","c").charAt(0));
        jButtonOK.setMnemonic(I18n.getString("reportPropertiesFrame.buttonOKMnemonic","o").charAt(0));
        
    }
    public void languageChanged(LanguageChangedEvent evt) {

        this.applyI18n();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCANCEL;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JCheckBox jCheckBoxFloatColumnFooter;
    private javax.swing.JCheckBox jCheckBoxIgnorePagination;
    private javax.swing.JCheckBox jCheckBoxSummaryOnNewPage;
    private javax.swing.JCheckBox jCheckBoxTitleOnNewPage;
    private javax.swing.JComboBox jComboBoxBottomMarginUnit;
    private javax.swing.JComboBox jComboBoxColumnsSpacingUnit;
    private javax.swing.JComboBox jComboBoxColumnsWidthUnit;
    private javax.swing.JComboBox jComboBoxHUnit;
    private javax.swing.JComboBox jComboBoxLanguage;
    private javax.swing.JComboBox jComboBoxLeftMarginUnit;
    private javax.swing.JComboBox jComboBoxOrientation;
    private javax.swing.JComboBox jComboBoxPrintOrder;
    private javax.swing.JComboBox jComboBoxRightMarginUnit;
    private javax.swing.JComboBox jComboBoxScriptletHandling;
    private javax.swing.JComboBox jComboBoxSize;
    private javax.swing.JComboBox jComboBoxTopMarginUnit;
    private javax.swing.JComboBox jComboBoxWUnit;
    private javax.swing.JComboBox jComboBoxWhenNoData;
    private javax.swing.JComboBox jComboBoxWhenResourceMissingType;
    private javax.swing.JComboBox jComboBoxXMLEncoding;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFormatFactoryClass;
    private javax.swing.JLabel jLabelWhenResourceMissingType;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldBottomMargin;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldColumns;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldColumnsSpacing;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldColumnsWidth;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldHeight;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldLeftMargin;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldRightMargin;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldTopMargin;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldWidth;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldFormatFactoryClass;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldResourceBundleBaseName;
    private javax.swing.JTextField jTextFieldScriptletClass;
    // End of variables declaration//GEN-END:variables

    private int columns;

    private int columnsWidth;

    private int columnsSpacing;

    private boolean titleOnNewPage;

    private boolean summaryOnNewPage;

    private String printOrder;

    private String whenNoDataType;

    private String xmlEncoding;

    private int globalUnit = 0;

    private String reportFormat;

    private int scriptletHandling;



    /**
     * This method sets height and width according to the orientation.
     * @return Void
     * @since July 3, 2004
     * @author Robert Lamping
     */
    public void adjustSizes() {
        int reportSwitch = 0;
        if (orientation == "Landscape") {
            // set Width to largest value of ReportWidth, ReportHeight
            if ( getReportWidth() < getReportHeight() ) {
                reportSwitch = getReportHeight();
                setReportHeight( getReportWidth() );
                setReportWidth( reportSwitch );
            }
        } else {
            // Orientation = Portrait
            if ( getReportWidth() > getReportHeight() ) {
                reportSwitch = getReportHeight();
                setReportHeight( getReportWidth() );
                setReportWidth( reportSwitch );
            }
        }
    }

    public void adjustOrientation() {
        if ( getReportWidth() <= getReportHeight() ) {
            setOrientation("Portrait");
        } else {
            setOrientation("Landscape");
        }
    }

    public String getResourceBundleBaseName() {
        return resourceBundleBaseName;
    }

    public void setResourceBundleBaseName(String resourceBundleBaseName) {
        this.resourceBundleBaseName = resourceBundleBaseName;
        jTextFieldResourceBundleBaseName.setText(resourceBundleBaseName);
    }

    public boolean isFloatColumnFooter() {
        return floatColumnFooter;

    }

    public void setFloatColumnFooter(boolean floatColumnFooter) {
        this.floatColumnFooter = floatColumnFooter;
        jCheckBoxFloatColumnFooter.setSelected(floatColumnFooter);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.jComboBoxLanguage.setSelectedItem(language);
    }

    void recalcColumnWidth() {

        this.columns = (int)jNumberFieldColumns.getValue();
        // calculate space...
        int avail = getReportWidth() - getLeftMargin() - getRightMargin() - (this.getColumns()-1)*getColumnsSpacing();
        int dim = avail;
        dim = (int)( (double)dim/(double)this.getColumns());

        while ( (dim*this.getColumns()) > avail) {
            dim--;
        }
        this.setColumnsWidth(dim);
    }

    public String getWhenResourceMissingType() {
        return whenResourceMissingType;
    }

    public void setWhenResourceMissingType(String whenResourceMissingType) {
        this.whenResourceMissingType = whenResourceMissingType;
        Misc.setComboboxSelectedTagValue(jComboBoxWhenResourceMissingType, whenResourceMissingType);
    }

    public boolean isIgnorePagination() {
        return ignorePagination;
    }

    public void setIgnorePagination(boolean ignorePagination) {
        jCheckBoxIgnorePagination.setSelected(ignorePagination);
        this.ignorePagination = ignorePagination;
    }

    public String getFormatFactoryClass() {
        return formatFactoryClass;
    }

    public void setFormatFactoryClass(String formatFactoryClass) {
        this.formatFactoryClass = formatFactoryClass;
        jTextFieldFormatFactoryClass.setText(formatFactoryClass);
    }
    
    

}
