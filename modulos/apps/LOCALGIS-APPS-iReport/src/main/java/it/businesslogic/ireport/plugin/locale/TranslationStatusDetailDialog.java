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
 * TranslationStatusDetailDialog.java
 * 
 * Created on March 27, 2006, 8:06 PM
 *
 */

package it.businesslogic.ireport.plugin.locale;


import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author  gtoffoli
 */
public class TranslationStatusDetailDialog extends javax.swing.JDialog implements JasperBabylonRunnerListener {
    
    private java.util.Properties properties = null;
    private String fileName = "";
    
    /** Creates new form TranslationStatusDialog */
    public TranslationStatusDetailDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    /**
     */
    public TranslationStatusDetailDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    private void initAll()
    {
        initComponents();
        
        TableColumnModel tcm = jTableLanguages.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        tc.setCellRenderer(new MissingResourceCellRenderer());
        
        DefaultListSelectionModel dsm = new DefaultListSelectionModel();
        dsm.setSelectionMode( dsm.SINGLE_SELECTION );
        dsm.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                jButtonDetails.setEnabled( jTableLanguages.getSelectedRow() >= 0);
            }
        });
        jTableLanguages.setSelectionModel( dsm );
        
        applyI18n();
        pack();
        Misc.centerFrame(this);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
            javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jButtonOkActionPerformed(e);
                }
            };

            getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
            getRootPane().getActionMap().put("ESCAPE", escapeAction);


            //to make the default button ...
            this.getRootPane().setDefaultButton(this.jButtonOk);
    }
    
    /*
     * languageFile (es. ireport_it.properties)
     */
    public void setLanguage(String language, Properties defaultTranslations)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTableLanguages.getModel();
        dtm.setRowCount(0);
        properties = new Properties();
        this.fileName = language;
        try {
        properties.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/" + language ) );
        } catch (Exception ex){}
        
        Iterator namesIterator = defaultTranslations.keySet().iterator();
        String[] names = new String[defaultTranslations.keySet().size()];
        for (int i=0; i<names.length; ++i)
        {
            names[i] = (String)namesIterator.next();
            
        }
        java.util.Arrays.sort(names);
        
        
        double translated = 0;
        for (int i=0; i<names.length; ++i)
        {
            String name = ""+names[i];
            ResourceKey rk = new ResourceKey(name);
            if (properties.getProperty(name) == null)
            {
                rk.setMissing(true);
            }
            dtm.addRow(new Object[]{rk, ((!rk.isMissing()) ? properties.getProperty(name) : ""), defaultTranslations.getProperty(name) });
            
        }
        
        jTableLanguages.updateUI();
    }
    
       
    /** This method is called from within the constructor to
     * initialize the form
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jButtonImportFromJB = new javax.swing.JButton();
        jButtonExportToJB = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLanguages = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonDetails = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonImportFromJB.setText("Import from JasperBabylon");
        jButtonImportFromJB.setActionCommand("Import from JasperBabylon");
        jButtonImportFromJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportToJBjButtonDetailsActionPerformed2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel2.add(jButtonImportFromJB, gridBagConstraints);

        jButtonExportToJB.setText("Export to JasperBabylon");
        jButtonExportToJB.setActionCommand("Export to JasperBabylon");
        jButtonExportToJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel2.add(jButtonExportToJB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jTableLanguages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Key", "Translation", "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLanguages.setGridColor(new java.awt.Color(204, 204, 204));
        jTableLanguages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLanguagesMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTableLanguages);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel1.add(jButtonSave, gridBagConstraints);

        jButtonDetails.setLabel("Export to file");
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel1.add(jButtonDetails, gridBagConstraints);

        jButtonOk.setText("Close");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel1.add(jButtonOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportToJBjButtonDetailsActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportToJBjButtonDetailsActionPerformed2

        
        setCursor(Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR));
        JasperBabylonRunner jbr = new JasperBabylonRunner();
        jbr.setRunnerListener(this);
        String code = this.getFileName();
        if (code.startsWith("Ireport_"))
        {
            code = code.substring(8);
        }
        if (code.endsWith(".properties"))
        {
            code = code.substring(0, code.length() - 11);
        }
        jbr.getLocale( code );
        
    }//GEN-LAST:event_jButtonExportToJBjButtonDetailsActionPerformed2

    private void jButtonDetailsActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed2

        if (JOptionPane.showConfirmDialog(this,"Are you sure you want send your translation on JasperBabylon?") == JOptionPane.YES_OPTION)
        {
            java.util.Properties p = new java.util.Properties();
            for (int i=0; i<jTableLanguages.getRowCount(); ++i)
            {
                ResourceKey rk = (ResourceKey)jTableLanguages.getValueAt(i,0);
                String value = (String)jTableLanguages.getValueAt(i,1);
                if (value == null || value.length() == 0) continue;
                p.setProperty( rk.getKey(), value );
            }
            
            setCursor(Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR));
            JasperBabylonRunner jbr = new JasperBabylonRunner();
            jbr.setRunnerListener(this);
            String code = this.getFileName();
            if (code.startsWith("Ireport_"))
            {
                code = code.substring(8);
            }
            if (code.endsWith(".properties"))
            {
                code = code.substring(0, code.length() - 11);
            }
            jbr.putLocale( code,  p);
        }
        
        
    }//GEN-LAST:event_jButtonDetailsActionPerformed2

    private void jButtonDetailsActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed1

        java.util.Properties p = new java.util.Properties();
        for (int i=0; i<jTableLanguages.getRowCount(); ++i)
        {
            ResourceKey rk = (ResourceKey)jTableLanguages.getValueAt(i,0);
            String value = (String)jTableLanguages.getValueAt(i,1);
            if (value == null || value.length() == 0) continue;
            p.setProperty( rk.getKey(), value );
        }
        Misc.saveTemporaryLocale(p, this.getFileName());
        if (this.getParent() != null &&
            this.getParent() instanceof TranslationStatusDialog)
        {
            ((TranslationStatusDialog)this.getParent()).updateStatus();
        }
        jButtonOkActionPerformed(null);        
        
    }//GEN-LAST:event_jButtonDetailsActionPerformed1

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed
// jfilechooser...
        try {
	    JFileChooser jfc = new JFileChooser();          
            jfc.setSelectedFile(new File(getFileName()));
	    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                
                    FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
                    this.getProperties().store(fos, "");
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(fos));
                    pw.println("\n\n# ---------- Missing translations -------------");
                    
                    for (int i=0; i<jTableLanguages.getRowCount(); ++i)
                    {
                        ResourceKey rk = (ResourceKey)jTableLanguages.getValueAt(i,0);
                        if (rk.isMissing())
                        {
                            pw.println("#"+rk+"="+jTableLanguages.getValueAt(i,2));
                        }
                    }
                    
                    pw.close();
	    }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_jButtonDetailsActionPerformed

    private void jTableLanguagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLanguagesMouseClicked
        
    }//GEN-LAST:event_jTableLanguagesMouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TranslationStatusDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public java.util.Properties getProperties() {
        return properties;
    }

    public void setProperties(java.util.Properties properties) {
        this.properties = properties;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDetails;
    private javax.swing.JButton jButtonExportToJB;
    private javax.swing.JButton jButtonImportFromJB;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLanguages;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonDetails.setText(I18n.getString("translationStatusDetailDialog.buttonDetails","Export"));
                jButtonOk.setText(I18n.getString("translationStatusDetailDialog.buttonOk","Close"));
                // End autogenerated code ----------------------
                
                jTableLanguages.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("translationStatusDetailDialog.tablecolumn.key","Key") );
                jTableLanguages.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("translationStatusDetailDialog.tablecolumn.translation","Translation") );
                jTableLanguages.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("translationStatusDetailDialog.tablecolumn.default","Default") );
                
                jButtonExportToJB.setText(I18n.getString("translationStatusDetailDialog.buttonExportToJB","Export to JasperBabylon"));
                jButtonImportFromJB.setText(I18n.getString("translationStatusDetailDialog.buttonImportFromJB","Import from JasperBabylon"));
		jButtonSave.setText(I18n.getString("translationStatusDetailDialog.buttonSave","Save"));
    }

    public void listLocalesComplete(List list) {
    }

    public void getLocaleComplete(Properties props) {
        
        
        for (int i=0; i<jTableLanguages.getRowCount(); ++i)
        {
            ResourceKey rk = (ResourceKey)jTableLanguages.getValueAt(i,0);
            if (props.getProperty( rk.getKey() ) != null)
            {
                rk.setMissing(false);
                jTableLanguages.setValueAt(props.getProperty(rk.getKey()),i,1);
            }
        }
        
        try {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                setCursor(Cursor.getDefaultCursor());
                jTableLanguages.updateUI();
            }
        });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void putLocaleComplete(String returnMessage) {
        
        
        try {
                final String msg = returnMessage;
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(TranslationStatusDetailDialog.this,msg);
                    }
                });
            } catch (Exception ex)
            {}
    }

    public void operationError(int operation, String message) {
        try {
                final String msg = message;
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(TranslationStatusDetailDialog.this,msg);
                    }
                });
            } catch (Exception ex)
            {}
    }
}
