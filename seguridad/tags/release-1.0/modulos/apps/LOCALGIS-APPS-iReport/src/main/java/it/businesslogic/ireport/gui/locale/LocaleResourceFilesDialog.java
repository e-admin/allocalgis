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
 * LocaleResourceFilesDialog.java
 * 
 * Created on 29 settembre 2004, 0.56
 *
 */

package it.businesslogic.ireport.gui.locale;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.*;
import it.businesslogic.ireport.chart.*;
import java.util.*;
import java.io.*;
import javax.swing.event.*;
import it.businesslogic.ireport.util.I18n;
/**
 *
 * @author  Administrator
 */
public class LocaleResourceFilesDialog extends javax.swing.JDialog {
   
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private JReportFrame jReportFrame = null;
    
    /** Creates new form IReportChartDialog */
    public LocaleResourceFilesDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFrame();  
    }
    
    public void initFrame()
    {
        javax.swing.DefaultListModel dlm =  new javax.swing.DefaultListModel() ;
     
        this.setSize(500,400);
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
         DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTable1.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTable1ListSelectionValueChanged(e);
            }
        });
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCloseActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        applyI18n();
        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonClose);
    }
    
    public void jTable1ListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {
        if (this.jTable1.getSelectedRowCount() > 0) {
            this.jButtonModifyFile.setEnabled(true);
            this.jButtonDeleteFile.setEnabled(true);
        }
        else {
            this.jButtonModifyFile.setEnabled(false);
            this.jButtonDeleteFile.setEnabled(false);
        }
    }
    
    public LocaleResourceFilesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initFrame();       
    }
    
    public void updateFileList()
    {       
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.setRowCount(0);
        if (getJReportFrame() == null) {  jTable1.updateUI(); return; }
        
        Report report = getJReportFrame().getReport();
        // Looking for locale files for this report...
        if (report.getFilename() == null ||
            report.getFilename().trim().equals("") )
        {
            return;
        }
        
        if (report.getResourceBundleBaseName() == null ||
            report.getResourceBundleBaseName().trim().equals(""))
        {
            return;
        }
        else
        {
            String basename = report.getResourceBundleBaseName();
            File f = new File(report.getFilename());
            File dir = f.getParentFile();
            String[] files = dir.list();
            for (int i=0; i<files.length; ++i)
            {
                if (files[i].startsWith(basename) &&
                    files[i].endsWith(".properties"))
                {
                    dtm.addRow(new Object[]{getLocaleName(basename, files[i]),files[i]});
                }
            }
        }
        
        jTable1.updateUI();
    }

    public JReportFrame getJReportFrame() {
        
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {

        this.jReportFrame = jReportFrame;
        updateFileList();
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelData = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButtonCreateNewLocale = new javax.swing.JButton();
        jButtonModifyFile = new javax.swing.JButton();
        jButtonDeleteFile = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report Imports");
        jPanelData.setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Locale", "File name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelData.add(jScrollPane3, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(140, 151));
        jPanel2.setPreferredSize(new java.awt.Dimension(180, 100));
        jButtonCreateNewLocale.setText("Create new locale");
        jButtonCreateNewLocale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateNewLocaleActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonCreateNewLocale, gridBagConstraints);

        jButtonModifyFile.setText(" Modify file");
        jButtonModifyFile.setActionCommand("Modify locale");
        jButtonModifyFile.setEnabled(false);
        jButtonModifyFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyFileActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jButtonModifyFile, gridBagConstraints);

        jButtonDeleteFile.setText("Delete file");
        jButtonDeleteFile.setEnabled(false);
        jButtonDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteFileActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jButtonDeleteFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jButtonClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelData.add(jPanel2, gridBagConstraints);

        getContentPane().add(jPanelData, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            jButtonModifyFileActionPerformed(null);
            
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButtonDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteFileActionPerformed
        
        if (jTable1.getSelectedRow() >= 0)
        {
            Report report = getJReportFrame().getReport();
            File f = new File(report.getFilename());
            File dir = f.getParentFile();
            File localeFile = new File(dir,""+jTable1.getValueAt( jTable1.getSelectedRow(), 1)  );
            try {
                localeFile.delete();
            } catch (Exception ex) {}
            updateFileList();
        }
        
    }//GEN-LAST:event_jButtonDeleteFileActionPerformed

    private void jButtonModifyFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyFileActionPerformed
        
        if (jTable1.getSelectedRow() >= 0)
        {
            Report report = getJReportFrame().getReport();
            File f = new File(report.getFilename());
            File dir = f.getParentFile();
            File localeFile = new File(dir,""+jTable1.getValueAt( jTable1.getSelectedRow(), 1)  );
            LocaleEditorDialog led = new LocaleEditorDialog(this.getJReportFrame().getMainFrame(), false);
            led.setFile( localeFile );
            led.setVisible(true);
            try {
                led.setTitle( localeFile.getName() );
            } catch (Exception ex) {}
        }
    }//GEN-LAST:event_jButtonModifyFileActionPerformed

    private void jButtonCreateNewLocaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateNewLocaleActionPerformed
        
        String suffix = "";
        
        Report report = getJReportFrame().getReport();
        if (report.getFilename() == null ||
            report.getFilename().trim().equals("") )
        {
           
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("messages.localeResourceFilesDialog.saveFile","To create a new locale file please save your report first.")
                    );
            
            return;
        }
        
        File f = new File(report.getFilename());
        
        
        NewLocaleFileDialog fd = new NewLocaleFileDialog(this, true);
        fd.setVisible(true);
        File localeFile = null;
        if (fd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            try   {
            suffix = fd.getText();
        
            if (report.getResourceBundleBaseName() == null ||
                report.getResourceBundleBaseName().trim().equals("") )
	    {
	            String filename =  f.getName();
	            int index = filename.lastIndexOf(".");
	            if (index >= 0) {
	                        filename = filename.substring(0,index);
	            }
	            report.setResourceBundleBaseName(filename);
	    }
            
            File dir = f.getParentFile();
            localeFile = new File(dir, report.getResourceBundleBaseName() + ".properties");
            // 1. Try to load default...
            String str = "# Locale " + suffix + " for report "  + f.getName() +"\n";
            try {
                FileInputStream fis = new FileInputStream(localeFile);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = fis.read(buffer)) > 0)
                {
                    str += new String(buffer,0,read);
                }
                fis.close();
            } catch (Exception ex){}
                
            localeFile = new File(dir, report.getResourceBundleBaseName() + suffix +".properties");
            FileOutputStream fos = new FileOutputStream( localeFile );
            fos.write( str.getBytes());
            fos.close();
            
            updateFileList();
            
            } catch (Exception ex) {
              
              javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getFormattedString("messages.localeResourceFilesDialog.errorCreatingNewLocale","Error creating new locale {0}:\n{1}",
                    new Object[]{localeFile.getName(),ex.getMessage()})
                    );
            }
        }
    }//GEN-LAST:event_jButtonCreateNewLocaleActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
       
        this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCreateNewLocale;
    private javax.swing.JButton jButtonDeleteFile;
    private javax.swing.JButton jButtonModifyFile;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    public static String getLocaleName(String baseName, String propertiesName)
    {
        String language = "";
        String country = "";
        String variant = "";
        Locale locale = Locale.getDefault();
        
        if (!propertiesName.endsWith(".properties")) return "";
        if (!propertiesName.startsWith(baseName)) return "";
        
        propertiesName = propertiesName.substring(baseName.length(), propertiesName.length() - ".properties".length());
        
        if (propertiesName.length() == 0) return "Default";
        if (propertiesName.startsWith("_")) propertiesName = propertiesName.substring(1);
        if (propertiesName.indexOf("_") > 0) 
        {
            language = propertiesName.substring(0,propertiesName.indexOf("_"));
            propertiesName = propertiesName.substring(propertiesName.indexOf("_")+1);
            
            if (propertiesName.indexOf("_") > 0) 
            {
                country = propertiesName.substring(0,propertiesName.indexOf("_"));
                propertiesName = propertiesName.substring(propertiesName.indexOf("_")+1);
                
                if (propertiesName.indexOf("_") > 0) 
                {
                    variant = propertiesName.substring(0,propertiesName.indexOf("_"));
                    propertiesName = propertiesName.substring(propertiesName.indexOf("_")+1);
                }
                else
                {
                    variant = propertiesName;
                }
            }
            else
            {
                country = propertiesName;
            }
        }
        else
        {
            language = propertiesName;
        }
        
        locale = new Locale(language,country,variant);
        
        return locale.getDisplayName();
        
     }
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonClose.setText(I18n.getString("localeResourceFilesDialog.buttonClose","Close"));
                jButtonCreateNewLocale.setText(I18n.getString("localeResourceFilesDialog.buttonCreateNewLocale","Create new locale"));
                jButtonDeleteFile.setText(I18n.getString("localeResourceFilesDialog.buttonDeleteFile","Delete file"));
                jButtonModifyFile.setText(I18n.getString("localeResourceFilesDialog.buttonModifyFile"," Modify file"));
                // End autogenerated code ----------------------
                
                jTable1.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("localeResourceFilesDialog.tablecolumn.locale","Locale") );
                jTable1.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("localeResourceFilesDialog.tablecolumn.fileName","File name") );
                this.setTitle(I18n.getString("localeResourceFilesDialog.title","Locale resource bundles"));
    }
}
