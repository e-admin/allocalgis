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
 * TranslationStatusDialog.java
 * 
 * Created on March 27, 2006, 8:06 PM
 *
 */

package it.businesslogic.ireport.plugin.locale;


import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.LocaleAdapter;
import it.businesslogic.ireport.util.Misc;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import it.businesslogic.ireport.util.I18n;
import java.sql.PreparedStatement;
import java.util.Enumeration;

/**
 *
 * @author  gtoffoli
 */
public class TranslationStatusDialog extends javax.swing.JDialog {
    
    /** Creates new form TranslationStatusDialog */
    public TranslationStatusDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        TableColumnModel tcm = jTableLanguages.getColumnModel();
        TableColumn tc = tcm.getColumn(1);
        tc.setCellRenderer(new ProgressBarCellRenderer());
        
        DefaultListSelectionModel dsm = new DefaultListSelectionModel();
        dsm.setSelectionMode( dsm.SINGLE_SELECTION );
        dsm.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                jButtonDetails.setEnabled( jTableLanguages.getSelectedRow() >= 0);
            }
        });
        jTableLanguages.setSelectionModel( dsm );
        
        
        applyI18n();
        
        updateStatus();
        
        this.jButtonInsertDb.setVisible(false);
        
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
    
    
    public void updateStatus()
    {
        
        List listOfLanguages = it.businesslogic.ireport.util.I18n.getListOfAvailLanguages();
        
        DefaultTableModel dtm = (DefaultTableModel)jTableLanguages.getModel();
        dtm.setRowCount(0);
        
        //dtm.addRow(new Object[]{"Default",null,null});
        
        Properties props_default = new Properties();
        try {
            props_default.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/Ireport.properties") );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        jLabelLocalizableString.setText( I18n.getFormattedString("translationStatusDialog.localizableStrings","Localizable strings: {0}", new Object[]{new Integer( props_default.size() - 1 )} ));
        
        
        for (int i=0; i< listOfLanguages.size(); ++i)
        {
            Locale lang = (Locale)listOfLanguages.get(i);
            LocaleAdapter adapter = new LocaleAdapter(lang);
            String language = lang.getLanguage();
            String country = lang.getCountry();
            String variant = lang.getVariant();
        
            String language_name = (language.length() > 0) ? "_" + language : "";
            language_name += (country.length() > 0) ? "_" + country : "";
            language_name += (variant.length() > 0) ? "_" + variant : "";
            
            language_name = "Ireport" + language_name + ".properties";
            
            int percentage_language = getLanguageCompletation(props_default,language_name);
            /*
            if( lang.getCountry().equals(selectedCountry) && 
                lang.getLanguage().equals(selectedLanguage) &&
                lang.getVariant().equals(selectedVariant) ) {
                        def_language = "" + adapter;
            }
            */
            
            dtm.addRow(new Object[]{adapter,new Integer(percentage_language),language_name});
        }
        
        
        
    }
    
    public int getLanguageCompletation(Properties defaultTranslations, String language)
    {
        Properties props2 = new Properties();
        try {
        props2.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/" + language ) );
        } catch (Exception ex){}
        
        
        
        double size = 0;
        Iterator namesIterator = defaultTranslations.keySet().iterator();
        size = defaultTranslations.size();
        double translated = 0;
        while (namesIterator.hasNext())
        {
            String name = ""+namesIterator.next();
            if (name.equals("defaultFont"))
            {
                size--;
                continue;
            }
            if (props2.getProperty(name) != null)
            {
                translated++;
            }
        }
        if (translated == 0) return 0;
        return (int)((translated/size)*100);
    }
    
    /** This method is called from within the constructor to
     * initialize the form
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelLocalizableString = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableLanguages = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonDetails = new javax.swing.JButton();
        jButtonInsertDb = new javax.swing.JButton();
        jButtonExportToJB = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabelLocalizableString.setText("Localizable strings:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabelLocalizableString, gridBagConstraints);

        jTableLanguages.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLanguages.setGridColor(new java.awt.Color(204, 204, 204));
        jTableLanguages.setShowVerticalLines(false);
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

        jButtonDetails.setText("Details...");
        jButtonDetails.setEnabled(false);
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonDetails, gridBagConstraints);

        jButtonInsertDb.setText("InsertDB");
        jButtonInsertDb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertDbActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonInsertDb, new java.awt.GridBagConstraints());

        jButtonExportToJB.setText("Import from JasperBabylon");
        jButtonExportToJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportToJBjButtonDetailsActionPerformed2(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel1.add(jButtonExportToJB, gridBagConstraints);

        jButtonOk.setText("Close");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonOk, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportToJBjButtonDetailsActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportToJBjButtonDetailsActionPerformed2

        QueryLocalesDialog qld = new QueryLocalesDialog(this, true);
        qld.setVisible(true);
        if (qld.getDialogResult() == JOptionPane.OK_OPTION)
        {
            Misc.saveTemporaryLocale(qld.getLocaleProperties(),"Ireport_" + qld.getLocale_code() + ".properties");
            this.updateStatus();
        }
        
    }//GEN-LAST:event_jButtonExportToJBjButtonDetailsActionPerformed2

    private void jButtonInsertDbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertDbActionPerformed

        IReportConnection c = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        java.sql.Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (c == null || !( c instanceof it.businesslogic.ireport.connection.JDBCConnection) ) {
                javax.swing.JOptionPane.showMessageDialog(this, "Not a JDBC connection","",javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn = c.getConnection();
            conn.createStatement().execute("delete from translations where app_id=1");
            
            stmt = conn.prepareStatement("insert into translations (app_id, language_id, s_key, s_value) values(1,?,?,?);");

            // 1. Add default keys...
            Properties props_default = new Properties();
            try {
                props_default.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/Ireport.properties") );
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
            Enumeration enum_keys = props_default.keys();
            while (enum_keys.hasMoreElements())
            {
                String key = (""+enum_keys.nextElement()).trim();
                String val = props_default.getProperty(key);
            
                stmt.setString(1,"");
                stmt.setString(2,key);
                stmt.setString(3,val);
                stmt.executeUpdate();
            }
            
            List listOfLanguages = it.businesslogic.ireport.util.I18n.getListOfAvailLanguages();
            
            System.out.println("Languages: " + listOfLanguages.size());
            for (int k=0; k<listOfLanguages.size(); ++k)
            {
                Locale lang = (Locale)listOfLanguages.get(k);
                LocaleAdapter adapter = new LocaleAdapter(lang);
                String language = lang.getLanguage();
                String country = lang.getCountry();
                String variant = lang.getVariant();
        
                String language_id =  language;
                language_id += (country.length() > 0) ? "_" + country : "";
                language_id += (variant.length() > 0) ? "_" + variant : "";
            
                String language_name = "Ireport" + ( (language.length() > 0) ? "_" : "") + language_id + ".properties"; 
                
                System.out.println("Working on " + language_name);
                
                // 2. Add keys for each langauge
                Properties props2 = new Properties();
                try {
                    props2.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/" + language_name ) );
                } catch (Exception ex){
                    ex.printStackTrace();
                }

                enum_keys = props2.keys();
                while (enum_keys.hasMoreElements())
                {
                    String key = (""+enum_keys.nextElement()).trim();
                    String val = props2.getProperty(key);
                    if (props_default.containsKey(key))
                    {
                        stmt.setString(1,language_id);
                        stmt.setString(2,key);
                        stmt.setString(3, val);
                        stmt.executeUpdate();
                    }
                }
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (conn != null) try {conn.close();} catch (Exception ex) { }
        }
        
        
    }//GEN-LAST:event_jButtonInsertDbActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jTableLanguagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLanguagesMouseClicked
        if (evt.getClickCount() == 2 &&
            evt.getButton() == evt.BUTTON1)
        {
            jButtonDetailsActionPerformed(null);
        }

    }//GEN-LAST:event_jTableLanguagesMouseClicked

    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed

        if (jTableLanguages.getSelectedRow() < 0) return;
        int row = jTableLanguages.getSelectedRow();
        
        LocaleAdapter adapter = (LocaleAdapter)jTableLanguages.getValueAt(jTableLanguages.getSelectedRow(), 0);
        
        TranslationStatusDetailDialog tsdd = new TranslationStatusDetailDialog(this,true);
        
        Properties props_default = new Properties();
        try {
            props_default.load( MainFrame.getMainInstance().getReportClassLoader().getResourceAsStream("it/businesslogic/ireport/locale/Ireport.properties") );
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        if (props_default != null)
        {
            tsdd.setLanguage( ""+ jTableLanguages.getValueAt(jTableLanguages.getSelectedRow(), 2),props_default);
            tsdd.setVisible(true);
        } 
        
        
    }//GEN-LAST:event_jButtonDetailsActionPerformed
    
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDetails;
    private javax.swing.JButton jButtonExportToJB;
    private javax.swing.JButton jButtonInsertDb;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelLocalizableString;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableLanguages;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonDetails.setText(I18n.getString("translationStatusDialog.buttonDetails","Details..."));
                jButtonOk.setText(I18n.getString("translationStatusDialog.buttonOk","Close"));
                jLabelLocalizableString.setText( I18n.getFormattedString("translationStatusDialog.buttonOk","Close", new Object[]{new Integer(0)} ));
                // End autogenerated code ----------------------
                
                jTableLanguages.getColumnModel().getColumn(0).setHeaderValue(I18n.getString("translationStatusDialog.table.Language","Language") );
                jTableLanguages.getColumnModel().getColumn(1).setHeaderValue(I18n.getString("translationStatusDialog.table.Status","Status") );
                jTableLanguages.getColumnModel().getColumn(2).setHeaderValue(I18n.getString("translationStatusDialog.table.File","File name") );
    
                jButtonExportToJB.setText(I18n.getString("translationStatusDialog.buttonExportToJB","Import from JasperBabylon"));
    }
}
