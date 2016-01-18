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
 * JRFontDialog.java
 * 
 * Created on 9 maggio 2003, 17.25
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.sheet.Tag;

import java.util.*;

/**
 *
 * @author  Administrator
 */
public class JRFontDialog extends javax.swing.JDialog {
    /** Creates new form JRParameterDialog */
    private IReportFont iReportFont = new IReportFont();
    private boolean init = false;
    private boolean reportFontMode = true;
    
    public JRFontDialog(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
        initComponents();
        this.setSize(420, 344);
        applyI18n();
        Misc.centerFrame(this);
        
        jNumberComboBoxSize.addEntry("3",3);
        jNumberComboBoxSize.addEntry("5",5);
        jNumberComboBoxSize.addEntry("8",8);
        jNumberComboBoxSize.addEntry("10",10);
        jNumberComboBoxSize.addEntry("12",12);
        jNumberComboBoxSize.addEntry("14",14);
        jNumberComboBoxSize.addEntry("18",18);
        jNumberComboBoxSize.addEntry("24",24);
        jNumberComboBoxSize.addEntry("36",36);
        jNumberComboBoxSize.addEntry("48",48);
        
        jNumberComboBoxSize.setSelectedIndex(3);
                
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1250","CP1250 (Central European)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1251","CP1251 (Cyrillic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1252","CP1252 (Western European ANSI aka WinAnsi)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1253","CP1253 (Greek)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1254","CP1254 (Turkish)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1255","CP1255 (Hebrew)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1256","CP1256 (Arabic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1257","CP1257 (Baltic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1258","CP1258 (Vietnamese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniGB-UCS2-H","UniGB-UCS2-H (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniGB-UCS2-V","UniGB-UCS2-V (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniCNS-UCS2-H","UniCNS-UCS2-H (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniCNS-UCS2-V","UniCNS-UCS2-V (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-H","UniJIS-UCS2-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-V","UniJIS-UCS2-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-HW-V","UniJIS-UCS2-HW-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniKS-UCS2-H","UniKS-UCS2-H (Korean)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniKS-UCS2-V","UniKS-UCS2-V (Korean)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Identity-H","Identity-H (Unicode with horizontal writing)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Identity-V","Identity-V (Unicode with vertical writing)"));
        
        // Load Fonts...
        String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i=0; i<fontFamilies.length; ++i)
        {
            jComboBoxFontName.addItem(fontFamilies[i]); 
        }
        
        jComboBoxReportFonts.setVisible(false);
        
        
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

        jPanelFont = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextFieldReportFont = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jComboBoxFontName = new javax.swing.JComboBox();
        jNumberComboBoxSize = new it.businesslogic.ireport.gui.JNumberComboBox();
        jLabel25 = new javax.swing.JLabel();
        jComboBoxPDFFontName = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxBold = new javax.swing.JCheckBox();
        jCheckBoxUnderline = new javax.swing.JCheckBox();
        jCheckBoxItalic = new javax.swing.JCheckBox();
        jCheckBoxStrokeTrough = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxPDFEmbedded = new javax.swing.JCheckBox();
        jCheckBoxDefaultFont = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jComboBoxPdfEncoding = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jButtonResetAllToDefault = new javax.swing.JButton();
        jComboBoxReportFonts = new javax.swing.JComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Add/modify report font");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelFont.setLayout(new java.awt.GridBagLayout());

        jLabel23.setText("Report font");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jLabel23, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jTextFieldReportFont, gridBagConstraints);

        jLabel24.setText("Font name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jLabel24, gridBagConstraints);

        jLabel27.setText("Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelFont.add(jLabel27, gridBagConstraints);

        jComboBoxFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFontNameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFont.add(jComboBoxFontName, gridBagConstraints);

        jNumberComboBoxSize.setMinimumSize(new java.awt.Dimension(70, 22));
        jNumberComboBoxSize.setPreferredSize(new java.awt.Dimension(70, 22));
        jNumberComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberComboBoxSizeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelFont.add(jNumberComboBoxSize, gridBagConstraints);

        jLabel25.setText("PDF font name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jLabel25, gridBagConstraints);

        jComboBoxPDFFontName.setEditable(true);
        jComboBoxPDFFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPDFFontNameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jComboBoxPDFFontName, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jCheckBoxBold.setText("Bold");
        jCheckBoxBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBoldActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jCheckBoxBold, gridBagConstraints);

        jCheckBoxUnderline.setText("Underline");
        jCheckBoxUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUnderlineActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jCheckBoxUnderline, gridBagConstraints);

        jCheckBoxItalic.setText("Italic");
        jCheckBoxItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxItalicActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jCheckBoxItalic, gridBagConstraints);

        jCheckBoxStrokeTrough.setText("Strike Trough");
        jCheckBoxStrokeTrough.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStrokeTroughActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jCheckBoxStrokeTrough, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFont.add(jSeparator2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jCheckBoxPDFEmbedded.setText("PDF Embedded");
        jCheckBoxPDFEmbedded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPDFEmbeddedActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jCheckBoxPDFEmbedded, gridBagConstraints);

        jCheckBoxDefaultFont.setLabel("Default");
        jCheckBoxDefaultFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDefaultFontActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jCheckBoxDefaultFont, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel32.setText("PDF Encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jLabel32, gridBagConstraints);

        jComboBoxPdfEncoding.setEditable(true);
        jComboBoxPdfEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPdfEncodingActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jComboBoxPdfEncoding, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        jPanel3.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanelFont.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelFont.add(jSeparator3, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel7, gridBagConstraints);

        jButtonOK.setMnemonic('o');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jButtonOK, gridBagConstraints);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel6.add(jButtonCancel, gridBagConstraints);

        jButtonResetAllToDefault.setMnemonic('d');
        jButtonResetAllToDefault.setText("Set all to default");
        jButtonResetAllToDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jButtonResetAllToDefault, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelFont.add(jPanel6, gridBagConstraints);

        jComboBoxReportFonts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxReportFontsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jComboBoxReportFonts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanelFont, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxPdfEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPdfEncodingActionPerformed
        if (init) return;
        if (this.jComboBoxPdfEncoding.getSelectedItem() instanceof PdfEncoding)
        {
            iReportFont.setPdfEncoding( ((PdfEncoding)this.jComboBoxPdfEncoding.getSelectedItem()).getEncoding());
        }
        
       // else
       //     iReportFont.setPdfEncoding( Misc.nvl(this.jComboBoxPdfEncoding.getSelectedItem(),"CP1251"));
    }//GEN-LAST:event_jComboBoxPdfEncodingActionPerformed

    private void jCheckBoxPDFEmbeddedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPDFEmbeddedActionPerformed
        if (init) return;
        iReportFont.setPdfEmbedded( jCheckBoxPDFEmbedded.isSelected() );
    }//GEN-LAST:event_jCheckBoxPDFEmbeddedActionPerformed

    private void jCheckBoxDefaultFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDefaultFontActionPerformed
        if (init) return;
        iReportFont.setDefaultFont( jCheckBoxDefaultFont.isSelected() );
    }//GEN-LAST:event_jCheckBoxDefaultFontActionPerformed

    private void jCheckBoxStrokeTroughActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStrokeTroughActionPerformed
        if (init) return;
        iReportFont.setStrikeTrought( jCheckBoxStrokeTrough.isSelected() );
    }//GEN-LAST:event_jCheckBoxStrokeTroughActionPerformed

    private void jCheckBoxItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxItalicActionPerformed
        if (init) return;
        iReportFont.setItalic( jCheckBoxItalic.isSelected() );
    }//GEN-LAST:event_jCheckBoxItalicActionPerformed

    private void jCheckBoxUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUnderlineActionPerformed
        if (init) return;
        iReportFont.setUnderline( jCheckBoxUnderline.isSelected() );
    }//GEN-LAST:event_jCheckBoxUnderlineActionPerformed

    private void jCheckBoxBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBoldActionPerformed
        if (init) return;
        iReportFont.setBold( jCheckBoxBold.isSelected() );
    }//GEN-LAST:event_jCheckBoxBoldActionPerformed

    private void jNumberComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeActionPerformed
        if (init) return;
        iReportFont.setFontSize( (int)jNumberComboBoxSize.getValue());
    }//GEN-LAST:event_jNumberComboBoxSizeActionPerformed

    private void jComboBoxFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFontNameActionPerformed
        if (init) return;
        getIReportFont().setFontName( jComboBoxFontName.getSelectedItem()+"");
    }//GEN-LAST:event_jComboBoxFontNameActionPerformed

    private void jButtonOKActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed1
        
        IReportFont ifont = new IReportFont();
        setIReportFont(ifont, jTextFieldReportFont.isVisible());
        
        
        
    }//GEN-LAST:event_jButtonOKActionPerformed1

    private void jComboBoxReportFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxReportFontsActionPerformed
        
        if (init) return;
        if (jComboBoxReportFonts.getSelectedItem() != null && jComboBoxReportFonts.getSelectedItem() instanceof IReportFont)
        {
            // Set all fields to the value of the report font...
            IReportFont ifont = (IReportFont)((IReportFont)jComboBoxReportFonts.getSelectedItem()).clone();
            
            setIReportFont(ifont, false);
            // Remove all values...
            ifont.getBeanProperties().clear();
            ifont.setReportFont(  jComboBoxReportFonts.getSelectedItem()+"" );
        }
    }//GEN-LAST:event_jComboBoxReportFontsActionPerformed

    private void jComboBoxPDFFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPDFFontNameActionPerformed
        
        if (init) return;
        // Set band to all....
        if (jComboBoxPDFFontName.getSelectedItem() == null || (jComboBoxPDFFontName.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Object obj = jComboBoxPDFFontName.getSelectedItem();
        String fontName = ""+ obj;
        if (obj instanceof Tag)
        {
            fontName = ""+((Tag)obj).getValue();
        }
        else
        {
            fontName = ""+obj;
        }
        
        iReportFont.setPropertyValue( IReportFont.PDF_FONT_NAME, (fontName.length() > 0) ? fontName : null);        
    }//GEN-LAST:event_jComboBoxPDFFontNameActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        
        if (this.jTextFieldReportFont.getText().trim().length() <= 0 && isReportFontMode())
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString( "messages.jRFontDialog.notValidReportFontName","Please insert a valid report font name!"),
                    I18n.getString( "messages.jRFontDialog.notValidReportFontNameCaption","Invalid report font name!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        //iReportFont = getIReportFont();
        if (isReportFontMode())
        {
            iReportFont.setReportFont( this.jTextFieldReportFont.getText() );
        }
        else if (this.jComboBoxReportFonts.isVisible() && this.jComboBoxReportFonts.getSelectedItem() != null)
        {
            iReportFont.setReportFont( this.jComboBoxReportFonts.getSelectedItem() +"");
        }
        
        //iReportFont.setBold( this.jCheckBoxBold.isSelected());
        
        //iReportFont.setDefaultFont( jCheckBoxDefaultFont.isSelected());
        //iReportFont.setFontName(""+ this.jComboBoxFontName.getSelectedItem());
        //iReportFont.setFontSize( (int)jNumberComboBoxSize.getValue());
        //iReportFont.setItalic( this.jCheckBoxItalic.isSelected());        
        //iReportFont.setPDFFontName( this.jComboBoxFontName.getSelectedItem()+"");
        
        //iReportFont.setPDFFontName(this.jComboBoxPDFFontName.getSelectedItem()+"");
        //if (iReportFont.getPDFFontName().equals("External TTF font..."))
        //{
        //    if (this.jComboBoxTTFFont.getSelectedItem() != null &&
        //        this.jComboBoxTTFFont.getSelectedItem() instanceof IRFont)
        //        iReportFont.setPDFFontName( ((IRFont) this.jComboBoxTTFFont.getSelectedItem()).getFile().getName() +"");   
        // }
        
        
        //iReportFont.setPdfEmbedded( this.jCheckBoxPDFEmbedded.isSelected());
        //if (this.jComboBoxPdfEncoding.getSelectedItem() instanceof PdfEncoding)
        //    iReportFont.setPdfEncoding( ((PdfEncoding)this.jComboBoxPdfEncoding.getSelectedItem()).getEncoding());
        //else
        //    iReportFont.setPdfEncoding( Misc.nvl(this.jComboBoxPdfEncoding.getSelectedItem(),"CP1251"));
        
        //iReportFont.setStrikeTrought( this.jCheckBoxStrokeTrough.isSelected());
        //iReportFont.setTTFFont( Misc.nvl(this.jComboBoxTTFFont.getSelectedItem(),"") );
        //iReportFont.setUnderline( this.jCheckBoxUnderline.isSelected());
        /*
        tmpParameter = new JRParameter( this.jTextFieldName.getText(), "java.lang.String",
                                        this.jCheckBoxIsForPrompting.isSelected(),
                                        this.jTextAreaDescription.getText());
        if (this.jComboBoxType.getSelectedItem().toString().trim().length() != 0)
        {
            tmpParameter.setClassType( this.jComboBoxType.getSelectedItem().toString().trim() );
        }
        tmpParameter.setDefaultValueExpression( jRTextExpressionAreaDefaultExpression.getText());
*/

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
    
    /** Getter for property iReportFont.
     * @return Value of property iReportFont.
     *
     */
    public it.businesslogic.ireport.IReportFont getIReportFont() {
        return iReportFont;
    }
    
    
    public void setIReportFont(it.businesslogic.ireport.IReportFont iReportFont)
    {
       setIReportFont(iReportFont, true);
    }
    /** Setter for property iReportFont.
     * @param iReportFont New value of property iReportFont.
     *
     */
    public void setIReportFont(it.businesslogic.ireport.IReportFont iReportFont, boolean reportFontToo) {
       
        init = true;
        
        if (iReportFont == null) iReportFont = new it.businesslogic.ireport.IReportFont();
        this.iReportFont = iReportFont;
        
        if (reportFontToo)
        {
            this.jTextFieldReportFont.setText( new String(iReportFont.getReportFont()) );
            if (iReportFont.getReportFont() != null && iReportFont.getReportFont().length()>0)
            {
                for (int i=0; i<jComboBoxReportFonts.getItemCount(); ++i)
                {
                    if ((jComboBoxReportFonts.getItemAt(i) + "").equals(iReportFont.getReportFont()))
                    {
                        jComboBoxReportFonts.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
        
        this.jCheckBoxBold.setSelected( iReportFont.isBold());
        this.jCheckBoxItalic.setSelected( iReportFont.isItalic());
        this.jCheckBoxStrokeTrough.setSelected( iReportFont.isStrikeTrought());
        this.jCheckBoxPDFEmbedded.setSelected( iReportFont.isPdfEmbedded() );
        this.jCheckBoxUnderline.setSelected( iReportFont.isUnderline());
        this.jCheckBoxDefaultFont.setSelected( iReportFont.isDefaultFont());

        setComboBoxText(true, iReportFont.getFontName() , jComboBoxFontName);
        
        this.setMixedTagComboBox(true, iReportFont.getPDFFontName() , jComboBoxPDFFontName );
                
        this.setElementComboNumber(true, iReportFont.getFontSize() , jNumberComboBoxSize );
        this.setPdfEncodingComboBox(true, iReportFont.getPdfEncoding() , jComboBoxPdfEncoding );
        
        init = false;
    }
    
     public void updateFonts(Vector iRFonts)
    {
        Vector fontsVec = new Vector();
        fontsVec.addElement(new Tag("Helvetica"));
        fontsVec.addElement(new Tag("Helvetica-Bold"));
        fontsVec.addElement(new Tag("Helvetica-BoldOblique"));
        fontsVec.addElement(new Tag("Helvetica-Oblique"));
        fontsVec.addElement(new Tag("Courier"));
        fontsVec.addElement(new Tag("Courier-Bold"));
        fontsVec.addElement(new Tag("Courier-BoldOblique"));
        fontsVec.addElement(new Tag("Courier-Oblique"));
        fontsVec.addElement(new Tag("Symbol"));
        fontsVec.addElement(new Tag("Times-Roman"));
        fontsVec.addElement(new Tag("Times-Bold"));
        fontsVec.addElement(new Tag("Times-BoldItalic"));
        fontsVec.addElement(new Tag("Times-Italic"));
        fontsVec.addElement(new Tag("ZapfDingbats"));
        fontsVec.addElement(new Tag("STSong-Light"));
        fontsVec.addElement(new Tag("MHei-Medium"));
        fontsVec.addElement(new Tag("MSung-Light"));
        fontsVec.addElement(new Tag("HeiseiKakuGo-W5"));
        fontsVec.addElement(new Tag("HeiseiMin-W3"));
        fontsVec.addElement(new Tag("HYGoThic-Medium"));
        fontsVec.addElement(new Tag("HYSMyeongJo-Medium"));
        Vector iRfonts = MainFrame.getMainInstance().getTtfFonts();
        for (int i_f=0; i_f<iRfonts.size(); ++i_f)
        {
            fontsVec.addElement(new Tag( ((IRFont)iRfonts.elementAt(i_f)).getFile(), 
                                   iRfonts.elementAt(i_f)+""));
        }
        Misc.updateComboBox(jComboBoxPDFFontName,fontsVec);
    } 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonResetAllToDefault;
    private javax.swing.JCheckBox jCheckBoxBold;
    private javax.swing.JCheckBox jCheckBoxDefaultFont;
    private javax.swing.JCheckBox jCheckBoxItalic;
    private javax.swing.JCheckBox jCheckBoxPDFEmbedded;
    private javax.swing.JCheckBox jCheckBoxStrokeTrough;
    private javax.swing.JCheckBox jCheckBoxUnderline;
    private javax.swing.JComboBox jComboBoxFontName;
    private javax.swing.JComboBox jComboBoxPDFFontName;
    private javax.swing.JComboBox jComboBoxPdfEncoding;
    private javax.swing.JComboBox jComboBoxReportFonts;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private it.businesslogic.ireport.gui.JNumberComboBox jNumberComboBoxSize;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelFont;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextFieldReportFont;
    // End of variables declaration//GEN-END:variables

    private int dialogResult;    
    
    protected boolean setComboBoxText( boolean firstTime, String value, javax.swing.JComboBox comboField )
    {       
      if (( ! firstTime ) && (!( Misc.nvl(comboField.getSelectedItem(),"").equalsIgnoreCase(value))))
      {
        comboField.setSelectedIndex(0);
        return false;
      }
      else
      {
          try {
            comboField.setSelectedItem( value );		 
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }
    
    
     protected boolean setMixedTagComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (firstTime)
        {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof Tag && ((Tag)comboField.getItemAt(i)).getValue().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
                // No tag found...
                comboField.setSelectedItem(value);
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }
        else
        {
            Object selectedValue = comboField.getSelectedItem();
            if (selectedValue == null && value == null) return true;
            if (selectedValue == null) return false;
            if (selectedValue instanceof Tag)
            {
                selectedValue = ((Tag)selectedValue).getValue();
            }
            
            if (selectedValue.equals(value)) return true;
        }
        return false;
    }
    
    protected boolean setPdfEncodingComboBox( boolean firstTime, String value, javax.swing.JComboBox comboField )
    {       
      if (( ! firstTime ) && (!(comboField.getSelectedItem()!=null && ( (comboField.getSelectedItem() instanceof PdfEncoding && ((PdfEncoding)comboField.getSelectedItem()).getEncoding().equalsIgnoreCase(value)) || (comboField.getSelectedItem()+"").equals(value) ))))
      {
        if (comboField.getItemCount() > 0)
            comboField.setSelectedIndex(0);
        return false;
      }
      else
      {
          try {
              for (int i=0; i<comboField.getItemCount(); ++i)
              {
                  if (comboField.getItemAt(i) instanceof PdfEncoding && ((PdfEncoding)comboField.getItemAt(i)).getEncoding().equals(value) )
                  {
                      comboField.setSelectedIndex(i);
                      return true;
                   }
              }	 
              comboField.setSelectedItem(value);   
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }
    
     protected boolean setElementComboNumber( boolean firstTime, double value, JNumberComboBox numberField )
    {
      if (( ! firstTime ) && (!(numberField.getValue() == value)))
      {
        numberField.setSelectedItem("");
        return false;
      }
      else
      {
          try {
            numberField.setValue( value );		 
          } catch (Exception ex){
            ex.printStackTrace();
          }
      }
      return true;
    }

    public boolean isReportFontMode() {
        return reportFontMode;
    }

    public void setReportFontMode(boolean reportFontMode) {
        setReportFontMode( reportFontMode ? 1 : 0);
    }
    
    public void setReportFontMode(int reportFontMode) {
        this.reportFontMode = reportFontMode == 1;
        
        jComboBoxReportFonts.setVisible( reportFontMode == 0 );
        jTextFieldReportFont.setVisible( reportFontMode == 1 );
        jCheckBoxDefaultFont.setEnabled( reportFontMode == 1 );
    
        jLabel23.setVisible(reportFontMode != 3);
        
        if (jComboBoxReportFonts.getItemCount() == 0)
        {
            // We have to populate the combobox...
            if ( MainFrame.getMainInstance().getActiveReportFrame() != null)
            {
                Misc.updateComboBox( jComboBoxReportFonts, MainFrame.getMainInstance().getActiveReportFrame().getReport().getFonts(), true);
            }
        }
    }
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxBold.setText(I18n.getString("jRFontDialog.checkBoxBold","Bold"));
                jCheckBoxItalic.setText(I18n.getString("jRFontDialog.checkBoxItalic","Italic"));
                jCheckBoxPDFEmbedded.setText(I18n.getString("jRFontDialog.checkBoxPDFEmbedded","PDF Embedded"));
                jCheckBoxStrokeTrough.setText(I18n.getString("jRFontDialog.checkBoxStrokeTrough","Strike Trough"));
                jCheckBoxUnderline.setText(I18n.getString("jRFontDialog.checkBoxUnderline","Underline"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRFontDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRFontDialog.buttonOK","OK"));
                jButtonResetAllToDefault.setText(I18n.getString("jRFontDialog.buttonResetAllToDefault","Set all to default"));
                jLabel23.setText(I18n.getString("jRFontDialog.label23","Report font"));
                jLabel24.setText(I18n.getString("jRFontDialog.label24","Font name"));
                jLabel25.setText(I18n.getString("jRFontDialog.label25","PDF font name"));
                jLabel27.setText(I18n.getString("jRFontDialog.label27","Size"));
                jLabel32.setText(I18n.getString("jRFontDialog.label32","PDF Encoding"));
                // End autogenerated code ----------------------
                
                this.setTitle(I18n.getString("jRFontDialog.title","Add/modify report font"));
                jButtonCancel.setMnemonic(I18n.getString("jRFontDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRFontDialog.buttonOKMnemonic","o").charAt(0));
                jCheckBoxDefaultFont.setText(I18n.getString("jRFontDialog.checkBoxDefaultFont","Default"));
    }
}
