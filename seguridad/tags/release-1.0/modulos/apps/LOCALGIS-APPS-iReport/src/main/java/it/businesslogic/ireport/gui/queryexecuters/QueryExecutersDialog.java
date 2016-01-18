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
 * QueryExecutersDialog.java
 * 
 * Created on 7 maggio 2003, 23.43
 *
 */

package it.businesslogic.ireport.gui.queryexecuters;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.table.SortChangedEvent;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.table.CustomColumnControlButton;
import it.businesslogic.ireport.gui.table.JDragTable;
import it.businesslogic.ireport.gui.table.SortChangedListener;
import it.businesslogic.ireport.util.*;

import java.util.*;
import org.jdesktop.swingx.icon.ColumnControlIcon;
/**
 *
 * @author  Administrator
 */
public class QueryExecutersDialog extends javax.swing.JDialog {
    
    /** Creates new form ValuesDialog */
    public QueryExecutersDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    public QueryExecutersDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    public void initAll()
    {
        initComponents();
        this.setSize(420, 250);
        DefaultTableColumnModel dtcm = (DefaultTableColumnModel)jTableProperties.getColumnModel();
        jTableProperties.setColumnControl(new CustomColumnControlButton(jTableProperties, new ColumnControlIcon() ) );
        
        jTableProperties.addSortChangedListener(new SortChangedListener() {

            public void sortChanged(SortChangedEvent evt) {
            
                //System.out.println("Sort: " + evt.getSortColumn() + " " + evt.getSortType());
                tableSortChanged(evt);
            }
        });
        
                
        dtcm.getColumn(0).setWidth(150);
        dtcm.getColumn(0).setPreferredWidth(150);
        
        DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTableProperties.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTablePropertiesListSelectionValueChanged(e);
            }
        });     
            
	applyI18n();
        // Open in center...
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonSaveActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonSave);
        
        this.updateQueryExecuters();
        
    }
    
    public void tableSortChanged(SortChangedEvent evt)
    {
        jTablePropertiesListSelectionValueChanged(null);
    }
    
    public void jTablePropertiesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {
         if (this.jTableProperties.getSelectedRowCount() > 0) {
            this.jButtonModifyProperty.setEnabled(true);
            this.jButtonDeleteProperty.setEnabled(true);
            
            if (jTableProperties.getSortedColumn()==null)
            {
                this.jButtonMoveUp.setEnabled(true);
                this.jButtonMoveDown.setEnabled(true);
            }
        }
        else {
            this.jButtonModifyProperty.setEnabled(false);
            this.jButtonDeleteProperty.setEnabled(false);
            this.jButtonMoveUp.setEnabled(false);
            this.jButtonMoveDown.setEnabled(false);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelFields = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableProperties = new it.businesslogic.ireport.gui.table.JDragTable();
        jPanelButtons2 = new javax.swing.JPanel();
        jButtonNewProperty = new javax.swing.JButton();
        jButtonModifyProperty = new javax.swing.JButton();
        jButtonDeleteProperty = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setTitle("Values");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelFields.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        jTableProperties.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Language", "Factory class", "Fields Provider"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableProperties.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePropertiesMouseClicked(evt);
            }
        });
        jTableProperties.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTablePropertiesPropertyChange(evt);
            }
        });
        jScrollPane3.setViewportView(jTableProperties);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelFields.add(jScrollPane3, gridBagConstraints);

        jPanelButtons2.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelButtons2.setPreferredSize(new java.awt.Dimension(130, 100));
        jPanelButtons2.setLayout(new java.awt.GridBagLayout());

        jButtonNewProperty.setText("New");
        jButtonNewProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelButtons2.add(jButtonNewProperty, gridBagConstraints);

        jButtonModifyProperty.setText("Modify");
        jButtonModifyProperty.setEnabled(false);
        jButtonModifyProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyPropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 4);
        jPanelButtons2.add(jButtonModifyProperty, gridBagConstraints);

        jButtonDeleteProperty.setText("Delete");
        jButtonDeleteProperty.setEnabled(false);
        jButtonDeleteProperty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelButtons2.add(jButtonDeleteProperty, gridBagConstraints);

        jButtonSave.setMnemonic('c');
        jButtonSave.setText("Close");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelButtons2.add(jButtonSave, gridBagConstraints);

        jButtonMoveUp.setText("Move up");
        jButtonMoveUp.setEnabled(false);
        jButtonMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelButtons2.add(jButtonMoveUp, gridBagConstraints);

        jButtonMoveDown.setText("Move down");
        jButtonMoveDown.setEnabled(false);
        jButtonMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePropertyActionPerformed2(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelButtons2.add(jButtonMoveDown, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.weighty = 1.0;
        jPanelButtons2.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelFields.add(jPanelButtons2, gridBagConstraints);

        getContentPane().add(jPanelFields, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jTablePropertiesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTablePropertiesPropertyChange
    
}//GEN-LAST:event_jTablePropertiesPropertyChange

    private void jButtonDeletePropertyActionPerformed2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed2

        if (jTableProperties.getSortedColumn() != null) return;
        if (jTableProperties.getSelectedRowCount() > 0)
        {
            DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
            int[] indices = jTableProperties.getSelectedRows();
            for (int i=indices.length-1; i>=0; --i)
            {
                if (indices[i] >= dtm.getRowCount() -1 ) continue;
                Object val = dtm.getValueAt( indices[i], 0);
                dtm.removeRow( indices[i]);
                dtm.insertRow( indices[i]+1, new Object[]{val, ((QueryExecuterDef)val).getClassName(),((QueryExecuterDef)val).getFieldsProvider() });
                indices[i]++;
            }
            for (int i=0; i<indices.length; ++i)
            {
                jTableProperties.addRowSelectionInterval(indices[i], indices[i]);
            }

        }
        
    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed2

    private void jButtonDeletePropertyActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed1

        if (jTableProperties.getSortedColumn() != null) return;
        if (jTableProperties.getSelectedRowCount() > 0)
        {
            DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
            int[] indices = jTableProperties.getSelectedRows();
            for (int i=0; i<indices.length; ++i)
            {
                if (indices[i] == 0) continue;
                Object val = dtm.getValueAt( indices[i], 0);
                dtm.removeRow( indices[i]);
                dtm.insertRow( indices[i]-1, new Object[]{val, ((QueryExecuterDef)val).getClassName(),((QueryExecuterDef)val).getFieldsProvider()});
                indices[i]--;
            }
            for (int i=0; i<indices.length; ++i)
            {
                jTableProperties.addRowSelectionInterval(indices[i], indices[i]);
            }

        }
        
    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed1

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        closeDialog(null);
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jTablePropertiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePropertiesMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 &&  jTableProperties.getSelectedRow() >=0 ) {
            jButtonModifyPropertyActionPerformed(new java.awt.event.ActionEvent( jButtonModifyProperty,0, ""));
        }
    }//GEN-LAST:event_jTablePropertiesMouseClicked

    private void jButtonNewPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewPropertyActionPerformed
        java.awt.Frame parent = Misc.frameFromComponent(this);
        QueryExecuterDialog jrpd = new QueryExecuterDialog( parent, true);
        jrpd.setVisible(true);
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            QueryExecuterDef qe = jrpd.getQueryExecuterDef();
            ((DefaultTableModel)jTableProperties.getModel()).addRow(new Object[]{qe, qe.getClassName(),qe.getFieldsProvider()});
        }
        
        
    }//GEN-LAST:event_jButtonNewPropertyActionPerformed

    private void jButtonModifyPropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyPropertyActionPerformed
        
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        int index = jTableProperties.convertRowIndexToModel(jTableProperties.getSelectedRow());
          
        QueryExecuterDef qe = (QueryExecuterDef)dtm.getValueAt(index, 0);
        
        java.awt.Frame parent = Misc.frameFromComponent(this);
        QueryExecuterDialog jrpd = new QueryExecuterDialog( parent, true);
        jrpd.setQueryExecuterDef( qe );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            dtm.setValueAt( jrpd.getQueryExecuterDef(),  index, 0);
            dtm.setValueAt( jrpd.getQueryExecuterDef().getClassName(),  index, 1);
            dtm.setValueAt( jrpd.getQueryExecuterDef().getFieldsProvider(),  index, 2);
        }
    }//GEN-LAST:event_jButtonModifyPropertyActionPerformed

    private void jButtonDeletePropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePropertyActionPerformed
        
        int[]  rows= jTableProperties.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        for (int i=rows.length-1; i>=0; --i) {
            int index = jTableProperties.convertRowIndexToModel(rows[i]);
            Object obj = dtm.getValueAt(index, 0);
            dtm.removeRow(index);
            // table update done getting an event...dtm.removeRow( index );
        }

    }//GEN-LAST:event_jButtonDeletePropertyActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        saveAll();
        setVisible(false);
    }//GEN-LAST:event_closeDialog
    
    
    public void updateQueryExecuters() {
        
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        dtm.setRowCount(0);
    
        Enumeration e = MainFrame.getMainInstance().getQueryExecuters().elements();
        
        while (e.hasMoreElements())
        {
            QueryExecuterDef qe = (QueryExecuterDef)e.nextElement();
            Vector row = new Vector();
            row.addElement( qe );
            row.addElement( qe.getClassName());
            row.addElement( qe.getFieldsProvider());
            
            dtm.addRow(row);           
        }    
    }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
        jButtonNewProperty.setText(it.businesslogic.ireport.util.I18n.getString("new","New"));
        jButtonModifyProperty.setText(it.businesslogic.ireport.util.I18n.getString("modify","Modify"));
        jButtonDeleteProperty.setText(it.businesslogic.ireport.util.I18n.getString("delete","Delete"));
        jButtonMoveUp.setText(it.businesslogic.ireport.util.I18n.getString("moveUp","Move up"));
        jButtonMoveDown.setText(it.businesslogic.ireport.util.I18n.getString("moveDown","Move down"));
        jButtonSave.setText(it.businesslogic.ireport.util.I18n.getString("close","Close"));
        
        jTableProperties.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("queryExecutersDialog.tablecolumn.language","Language") );
        jTableProperties.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("queryExecutersDialog.tablecolumn.factoryClass","Factory class") );
        jTableProperties.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("queryExecutersDialog.tablecolumn.fieldsProvider","Fields Provider") );
             
        this.setTitle(I18n.getString("queryExecutersDialog.title","Query executers"));
        jButtonSave.setMnemonic(I18n.getString("queryExecutersDialog.buttonSaveMnemonic","s").charAt(0));
    }      
    public void languageChanged(LanguageChangedEvent evt) {
            
        this.applyI18n();
    }
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteProperty;
    private javax.swing.JButton jButtonModifyProperty;
    private javax.swing.JButton jButtonMoveDown;
    private javax.swing.JButton jButtonMoveUp;
    private javax.swing.JButton jButtonNewProperty;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelButtons2;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JScrollPane jScrollPane3;
    private it.businesslogic.ireport.gui.table.JDragTable jTableProperties;
    // End of variables declaration//GEN-END:variables
   
    
    public void saveAll()
    {
        Vector v = MainFrame.getMainInstance().getQueryExecuters();
        v.clear();
        DefaultTableModel dtm = (DefaultTableModel)jTableProperties.getModel();
        
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            QueryExecuterDef qe = (QueryExecuterDef)dtm.getValueAt(i,0);
            v.add(qe);
        }
        
        MainFrame.getMainInstance().saveiReportConfiguration();
    }
   }
