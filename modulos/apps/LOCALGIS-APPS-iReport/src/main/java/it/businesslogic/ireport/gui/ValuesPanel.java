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
 * ValuesPanel.java
 * 
 * Created on 28 settembre 2005, 15.52
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.gui.table.CustomColumnControlButton;
import it.businesslogic.ireport.gui.table.JDragTable;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.icon.ColumnControlIcon;
/**
 *
 * @author  Administrator
 */
public class ValuesPanel extends javax.swing.JPanel {
    
    private Vector clipboardFields = null;
    private Vector clipboardVariables = null;
    private Vector clipboardParameters = null;
    
    /**
     * Dataset with wich the dialog deals
     */
    private SubDataset subDataset;
    
    /** Creates new form ValuesPanel */
    public ValuesPanel() {
        initComponents();
        
        setClipboardFields(new Vector());
        setClipboardVariables(new Vector());
        setClipboardParameters(new Vector());

        jTableFields.setColumnControl(new CustomColumnControlButton(jTableFields, new ColumnControlIcon() ) );
        jTableVariables.setColumnControl(new CustomColumnControlButton(jTableVariables, new ColumnControlIcon() ) );
        jTableParameters.setColumnControl(new CustomColumnControlButton(jTableParameters, new ColumnControlIcon() ) );
        //DefaultTableColumnModel dtcm = (DefaultTableColumnModel)jTableParameters.getColumnModel();
        //DefaultTableCellRenderer tcr = (DefaultTableCellRenderer)dtcm.getColumn(0).getHeaderRenderer();

        //new DefaultTableCellRenderer();
        //tcr.setFont(jTableParameters.getFont());
        //tcr.setBackground(this.getBackground());
        //tcr.setBorder( new javax.swing.border.BevelBorder( javax.swing.border.BevelBorder.RAISED));
        //dtcm.getColumn(0).setHeaderRenderer(tcr);
        DefaultListSelectionModel dlsm = (DefaultListSelectionModel) this.jTableParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)
            {
                jTableParametersListSelectionValueChanged(e);
            }
        });

        
        
        dlsm = (DefaultListSelectionModel) this.jTableVariables.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)
            {
                jTableVariablesListSelectionValueChanged(e);
            }
        });

        dlsm = (DefaultListSelectionModel) this.jTableFields.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)
            {
                jTableFieldsListSelectionValueChanged(e);
            }
        });

        applyI18n();
        
        I18n.addOnLanguageChangedListener( new LanguageChangedListener() {
            public void languageChanged(LanguageChangedEvent evt) {
                applyI18n();
            }
        } );
    }
    
    /**
     * DOCUMENT ME!
     * 
     * @param e DOCUMENT ME!
     */
    public void jTableParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {

        if (this.jTableParameters.getSelectedRowCount() > 0)
        {
            this.jButtonModifyParameter.setEnabled(true);
            this.jButtonDeleteParameter.setEnabled(true);

            // if more than one row is selected and one of them is a built-in parameter then
            // the buttons must be disabled
            List list = getSelectedObjects(jTableParameters);
            
            for (int i = list.size() - 1; i >= 0; --i)
            {

                if (((JRParameter) list.get(i)).isBuiltin())
                {
                    this.jButtonModifyVariable.setEnabled(false);
                    this.jButtonDeleteVariable.setEnabled(false);
                }
            }
           
        }
        else
        {
            this.jButtonModifyParameter.setEnabled(false);
            this.jButtonDeleteParameter.setEnabled(false);
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @param e DOCUMENT ME!
     */
    public void jTableVariablesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {

        if (this.jTableVariables.getSelectedRowCount() > 0)
        {
            this.jButtonModifyVariable.setEnabled(true);
            this.jButtonDeleteVariable.setEnabled(true);

            // if more than one row is selected and one of them is a built-in variable then
            // the buttons must be disabled
            List list = getSelectedObjects(jTableVariables);
            
            for (int i = list.size() - 1; i >= 0; --i)
            {

                if (((JRVariable) list.get(i)).isBuiltin())
                {
                    this.jButtonModifyVariable.setEnabled(false);
                    this.jButtonDeleteVariable.setEnabled(false);
                }
            }

        }
        else
        {
            this.jButtonModifyVariable.setEnabled(false);
            this.jButtonDeleteVariable.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param e DOCUMENT ME!
     */
    public void jTableFieldsListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {

        if (this.jTableFields.getSelectedRowCount() > 0)
        {
            this.jButtonModifyField.setEnabled(true);
            this.jButtonDeleteField.setEnabled(true);
        }
        else
        {
            this.jButtonModifyField.setEnabled(false);
            this.jButtonDeleteField.setEnabled(false);
        }
    }

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
        
        // Update all...
        if (subDataset != null)
        {
            updateParameters();
            updateFields();
            updateVariables();
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

        jPopupMenuFields = new javax.swing.JPopupMenu();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jMenuItemDuplicate = new javax.swing.JMenuItem();
        jPopupMenuVariables = new javax.swing.JPopupMenu();
        jMenuItemCut1 = new javax.swing.JMenuItem();
        jMenuItemCopy1 = new javax.swing.JMenuItem();
        jMenuItemPaste1 = new javax.swing.JMenuItem();
        jMenuItemDelete1 = new javax.swing.JMenuItem();
        jMenuItemDuplicate1 = new javax.swing.JMenuItem();
        jPopupMenuParameters = new javax.swing.JPopupMenu();
        jMenuItemCut2 = new javax.swing.JMenuItem();
        jMenuItemCopy2 = new javax.swing.JMenuItem();
        jMenuItemPaste2 = new javax.swing.JMenuItem();
        jMenuItemDelete2 = new javax.swing.JMenuItem();
        jMenuItemDuplicate2 = new javax.swing.JMenuItem();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelFields = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableFields = new it.businesslogic.ireport.gui.table.JDragTable();
        jPanelButtons2 = new javax.swing.JPanel();
        jButtonNewField = new javax.swing.JButton();
        jButtonModifyField = new javax.swing.JButton();
        jButtonDeleteField = new javax.swing.JButton();
        jPanelVariables = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableVariables = new it.businesslogic.ireport.gui.table.JDragTable();
        jPanelButtons1 = new javax.swing.JPanel();
        jButtonNewVariable = new javax.swing.JButton();
        jButtonModifyVariable = new javax.swing.JButton();
        jButtonDeleteVariable = new javax.swing.JButton();
        jPanelParameters = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableParameters = new it.businesslogic.ireport.gui.table.JDragTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonNewParameter = new javax.swing.JButton();
        jButtonModifyParameter = new javax.swing.JButton();
        jButtonDeleteParameter = new javax.swing.JButton();

        jPopupMenuFields.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenuFieldsPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemCut.setText("Item");
        jMenuItemCut.setActionCommand("Item");
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });

        jPopupMenuFields.add(jMenuItemCut);

        jMenuItemCopy.setText("Item");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });

        jPopupMenuFields.add(jMenuItemCopy);

        jMenuItemPaste.setText("Item");
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });

        jPopupMenuFields.add(jMenuItemPaste);

        jMenuItemDelete.setText("Item");
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });

        jPopupMenuFields.add(jMenuItemDelete);

        jMenuItemDuplicate.setText("Item");
        jMenuItemDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDuplicateActionPerformed(evt);
            }
        });

        jPopupMenuFields.add(jMenuItemDuplicate);

        jPopupMenuVariables.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenuVariablesPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemCut1.setText("Item");
        jMenuItemCut1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCut1ActionPerformed(evt);
            }
        });

        jPopupMenuVariables.add(jMenuItemCut1);

        jMenuItemCopy1.setText("Item");
        jMenuItemCopy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopy1ActionPerformed(evt);
            }
        });

        jPopupMenuVariables.add(jMenuItemCopy1);

        jMenuItemPaste1.setText("Item");
        jMenuItemPaste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPaste1ActionPerformed(evt);
            }
        });

        jPopupMenuVariables.add(jMenuItemPaste1);

        jMenuItemDelete1.setText("Item");
        jMenuItemDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDelete1ActionPerformed(evt);
            }
        });

        jPopupMenuVariables.add(jMenuItemDelete1);

        jMenuItemDuplicate1.setText("Item");
        jMenuItemDuplicate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDuplicate1ActionPerformed(evt);
            }
        });

        jPopupMenuVariables.add(jMenuItemDuplicate1);

        jPopupMenuParameters.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenuParametersPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemCut2.setText("Item");
        jMenuItemCut2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCut2ActionPerformed(evt);
            }
        });

        jPopupMenuParameters.add(jMenuItemCut2);

        jMenuItemCopy2.setText("Item");
        jMenuItemCopy2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopy2ActionPerformed(evt);
            }
        });

        jPopupMenuParameters.add(jMenuItemCopy2);

        jMenuItemPaste2.setText("Item");
        jMenuItemPaste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPaste2ActionPerformed(evt);
            }
        });

        jPopupMenuParameters.add(jMenuItemPaste2);

        jMenuItemDelete2.setText("Item");
        jMenuItemDelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDelete2ActionPerformed(evt);
            }
        });

        jPopupMenuParameters.add(jMenuItemDelete2);

        jMenuItemDuplicate2.setText("Item");
        jMenuItemDuplicate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDuplicate2ActionPerformed(evt);
            }
        });

        jPopupMenuParameters.add(jMenuItemDuplicate2);

        setLayout(new java.awt.BorderLayout());

        jTabbedPane.setName("");
        jPanelFields.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });

        jTableFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Field name", "Class type"
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

        jScrollPane3.setViewportView(jTableFields);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelFields.add(jScrollPane3, gridBagConstraints);

        jPanelButtons2.setLayout(null);

        jPanelButtons2.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanelButtons2.setOpaque(false);
        jPanelButtons2.setPreferredSize(new java.awt.Dimension(100, 100));
        jButtonNewField.setText("New");
        jButtonNewField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewFieldActionPerformed(evt);
            }
        });

        jPanelButtons2.add(jButtonNewField);
        jButtonNewField.setBounds(4, 5, 92, 22);

        jButtonModifyField.setText("Modify");
        jButtonModifyField.setEnabled(false);
        jButtonModifyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyFieldActionPerformed(evt);
            }
        });

        jPanelButtons2.add(jButtonModifyField);
        jButtonModifyField.setBounds(4, 30, 92, 22);

        jButtonDeleteField.setText("Delete");
        jButtonDeleteField.setEnabled(false);
        jButtonDeleteField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteFieldActionPerformed(evt);
            }
        });

        jPanelButtons2.add(jButtonDeleteField);
        jButtonDeleteField.setBounds(4, 56, 92, 22);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelFields.add(jPanelButtons2, gridBagConstraints);

        jTabbedPane.addTab("Fields", jPanelFields);

        jPanelVariables.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        jTableVariables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable name", "Builtin"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableVariables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVariablesMouseClicked(evt);
            }
        });

        jScrollPane2.setViewportView(jTableVariables);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelVariables.add(jScrollPane2, gridBagConstraints);

        jPanelButtons1.setLayout(null);

        jPanelButtons1.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanelButtons1.setPreferredSize(new java.awt.Dimension(100, 100));
        jButtonNewVariable.setText("New");
        jButtonNewVariable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewVariableActionPerformed(evt);
            }
        });

        jPanelButtons1.add(jButtonNewVariable);
        jButtonNewVariable.setBounds(4, 5, 92, 22);

        jButtonModifyVariable.setText("Modify");
        jButtonModifyVariable.setEnabled(false);
        jButtonModifyVariable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyVariableActionPerformed(evt);
            }
        });

        jPanelButtons1.add(jButtonModifyVariable);
        jButtonModifyVariable.setBounds(4, 30, 92, 22);

        jButtonDeleteVariable.setText("Delete");
        jButtonDeleteVariable.setEnabled(false);
        jButtonDeleteVariable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteVariableActionPerformed(evt);
            }
        });

        jPanelButtons1.add(jButtonDeleteVariable);
        jButtonDeleteVariable.setBounds(4, 56, 92, 22);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelVariables.add(jPanelButtons1, gridBagConstraints);

        jTabbedPane.addTab("Variables", jPanelVariables);

        jPanelParameters.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        jTableParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter name", "Class type", "Is for prompting", "Built-in"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableParametersMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTableParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelParameters.add(jScrollPane1, gridBagConstraints);

        jPanelButtons.setLayout(null);

        jPanelButtons.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanelButtons.setPreferredSize(new java.awt.Dimension(100, 100));
        jButtonNewParameter.setText("New");
        jButtonNewParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewParameterActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonNewParameter);
        jButtonNewParameter.setBounds(4, 5, 92, 22);

        jButtonModifyParameter.setText("Modify");
        jButtonModifyParameter.setEnabled(false);
        jButtonModifyParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyParameterActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonModifyParameter);
        jButtonModifyParameter.setBounds(4, 30, 92, 22);

        jButtonDeleteParameter.setText("Delete");
        jButtonDeleteParameter.setEnabled(false);
        jButtonDeleteParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteParameterActionPerformed(evt);
            }
        });

        jPanelButtons.add(jButtonDeleteParameter);
        jButtonDeleteParameter.setBounds(4, 56, 92, 22);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanelParameters.add(jPanelButtons, gridBagConstraints);

        jTabbedPane.addTab("Parameters", jPanelParameters);

        add(jTabbedPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDeleteFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteFieldActionPerformed
        int[]  rows= jTableFields.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        for (int i=rows.length-1; i>=0; --i) {
            int index = jTableFields.convertRowIndexToModel(rows[i]);
            JRField obj = (JRField)dtm.getValueAt(index, 0);
            this.getSubDataset().removeField(obj);
            // table update done getting an event...dtm.removeRow( index );
        }

    }//GEN-LAST:event_jButtonDeleteFieldActionPerformed

    private void jPopupMenuParametersPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenuParametersPopupMenuWillBecomeVisible
        if (getClipboardParameters().size() > 0)
        {
            jMenuItemPaste2.setEnabled(true);
        }
        else
        {
            jMenuItemPaste2.setEnabled(false);
        }

        if (jTableParameters.getSelectedRowCount() > 0)
        {
            jMenuItemCut2.setEnabled(true);
            jMenuItemCopy2.setEnabled(true);
            jMenuItemDelete2.setEnabled(true);
            jMenuItemDuplicate2.setEnabled(true);
        }
        else
        {
            jMenuItemCut2.setEnabled(false);
            jMenuItemCopy2.setEnabled(false);
            jMenuItemDelete2.setEnabled(false);
            jMenuItemDuplicate2.setEnabled(false);
        }
    }//GEN-LAST:event_jPopupMenuParametersPopupMenuWillBecomeVisible

    private void jPopupMenuVariablesPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenuVariablesPopupMenuWillBecomeVisible

        if (getClipboardVariables().size() > 0)
        {
            jMenuItemPaste1.setEnabled(true);
        }
        else
        {
            jMenuItemPaste1.setEnabled(false);
        }

        if (jTableVariables.getSelectedRowCount() > 0)
        {
            jMenuItemCut1.setEnabled(true);
            jMenuItemCopy1.setEnabled(true);
            jMenuItemDelete1.setEnabled(true);
            jMenuItemDuplicate1.setEnabled(true);
        }
        else
        {
            jMenuItemCut1.setEnabled(false);
            jMenuItemCopy1.setEnabled(false);
            jMenuItemDelete1.setEnabled(false);
            jMenuItemDuplicate1.setEnabled(false);
        }
    }//GEN-LAST:event_jPopupMenuVariablesPopupMenuWillBecomeVisible

    private void jPopupMenuFieldsPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenuFieldsPopupMenuWillBecomeVisible
        if (getClipboardFields().size() > 0)
        {
            jMenuItemPaste.setEnabled(true);
        }
        else
        {
            jMenuItemPaste.setEnabled(false);
        }

        if (jTableFields.getSelectedRowCount() > 0)
        {
            jMenuItemCut.setEnabled(true);
            jMenuItemCopy.setEnabled(true);
            jMenuItemDelete.setEnabled(true);
            jMenuItemDuplicate.setEnabled(true);
        }
        else
        {
            jMenuItemCut.setEnabled(false);
            jMenuItemCopy.setEnabled(false);
            jMenuItemDelete.setEnabled(false);
            jMenuItemDuplicate.setEnabled(false);
        }
    }//GEN-LAST:event_jPopupMenuFieldsPopupMenuWillBecomeVisible

    private void jMenuItemDuplicate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDuplicate2ActionPerformed
        
        Vector selectedParameters = new Vector();
        
        int[] rows = jTableParameters.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableParameters.convertRowIndexToModel(rows[i]);
            JRParameter obj = (JRParameter)dtm.getValueAt(index, 0);
            selectedParameters.add(obj.cloneMe());
        }

        duplicateParameters(selectedParameters);
    }//GEN-LAST:event_jMenuItemDuplicate2ActionPerformed

    private void jMenuItemDelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDelete2ActionPerformed
        jButtonDeleteParameterActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemDelete2ActionPerformed

    private void jMenuItemPaste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPaste2ActionPerformed
        duplicateParameters(getClipboardParameters());
    }//GEN-LAST:event_jMenuItemPaste2ActionPerformed

    private void jMenuItemCopy2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopy2ActionPerformed
        
        int[] rows = jTableParameters.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableParameters.convertRowIndexToModel(rows[i]);
            JRParameter parameter = (JRParameter)dtm.getValueAt(index, 0);
            if (!parameter.isBuiltin())
            {
                getClipboardParameters().add(parameter.cloneMe());
            }
        }
    }//GEN-LAST:event_jMenuItemCopy2ActionPerformed

    private void jMenuItemCut2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCut2ActionPerformed
        // Put selected elements in clipboard and call delete...
        
        jMenuItemCopy2ActionPerformed(evt);
        jButtonDeleteParameterActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemCut2ActionPerformed

    private void jMenuItemDuplicate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDuplicate1ActionPerformed
        
        Vector selectedVariables = new Vector();
        
        int[] rows = jTableVariables.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableVariables.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableVariables.convertRowIndexToModel(rows[i]);
            JRVariable obj = (JRVariable)dtm.getValueAt(index, 0);
            selectedVariables.add(obj.cloneMe());
        }

        duplicateVariables(selectedVariables);
    }//GEN-LAST:event_jMenuItemDuplicate1ActionPerformed

    private void jMenuItemDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDelete1ActionPerformed
        jButtonDeleteVariableActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemDelete1ActionPerformed

    private void jMenuItemPaste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPaste1ActionPerformed
        duplicateVariables(getClipboardVariables());
    }//GEN-LAST:event_jMenuItemPaste1ActionPerformed

    private void jMenuItemCopy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopy1ActionPerformed
        
        getClipboardVariables().removeAllElements();
        int[] rows = jTableVariables.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableVariables.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableVariables.convertRowIndexToModel(rows[i]);
            JRVariable variable = (JRVariable)dtm.getValueAt(index, 0);
            if (!variable.isBuiltin())
            {
                getClipboardVariables().add(variable.cloneMe());
            }
        }
        
    }//GEN-LAST:event_jMenuItemCopy1ActionPerformed

    private void jMenuItemCut1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCut1ActionPerformed
        jMenuItemCopy1ActionPerformed(evt);
        jButtonDeleteVariableActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemCut1ActionPerformed

    private void jMenuItemDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDuplicateActionPerformed
        Vector selectedFields = new Vector();
        
        int[] rows = jTableFields.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableFields.convertRowIndexToModel(rows[i]);
            JRField obj = (JRField)dtm.getValueAt(index, 0);
            selectedFields.add(obj.cloneMe());
        }

        duplicateFields(selectedFields);
    }//GEN-LAST:event_jMenuItemDuplicateActionPerformed

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        jButtonDeleteFieldActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        duplicateFields(getClipboardFields());
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        
        getClipboardFields().removeAllElements();
        int[] rows = jTableFields.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableFields.convertRowIndexToModel(rows[i]);
            JRField obj = (JRField)dtm.getValueAt(index, 0);
            getClipboardFields().add(obj.cloneMe());
        }
        
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
        // Put selected elements in clipboard and call delete...
        jMenuItemCopyActionPerformed(evt);
        jButtonDeleteFieldActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jTableParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableParametersMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 && 
            jTableParameters.getSelectedRow() >= 0)
        {
            jButtonModifyParameterActionPerformed(new java.awt.event.ActionEvent(
                                                          jButtonModifyParameter, 
                                                          0, ""));
        }

        if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3)
        {
            jPopupMenuParameters.show(jTableParameters, evt.getPoint().x, 
                                      evt.getPoint().y);
        }
    }//GEN-LAST:event_jTableParametersMouseClicked

    private void jTableVariablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVariablesMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 && 
            jTableVariables.getSelectedRow() >= 0)
        {
            jButtonModifyVariableActionPerformed(new java.awt.event.ActionEvent(
                                                         jButtonModifyVariable, 
                                                         0, ""));
        }

        if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3)
        {
            jPopupMenuVariables.show(jTableVariables, evt.getPoint().x, 
                                     evt.getPoint().y);
        }
    }//GEN-LAST:event_jTableVariablesMouseClicked

    private void jTableFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFieldsMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1 && 
            jTableFields.getSelectedRow() >= 0)
        {
            jButtonModifyFieldActionPerformed(new java.awt.event.ActionEvent(
                                                      jButtonModifyField, 0, 
                                                      ""));
        }

        if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3)
        {
            jPopupMenuFields.show(jTableFields, evt.getPoint().x, 
                                  evt.getPoint().y);
        }
    }//GEN-LAST:event_jTableFieldsMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jButtonDeleteParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteParameterActionPerformed
        int[] rows = jTableParameters.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = jTableParameters.convertRowIndexToModel(rows[i]);
        
            JRParameter parameter = (JRParameter) dtm.getValueAt(index, 0);

            if (!parameter.isBuiltin())
            {
                getSubDataset().removeParameter(parameter);
            }
            else
            {
                javax.swing.JOptionPane.showMessageDialog(this, 
                        I18n.getString( "messages.valuesPanel.cantModifyBuiltInParameters","You can't modify or delete builtin parameters!"),
                        I18n.getString( "messages.valuesPanel.cantModifyBuiltInParametersCaption","Invalid operation!"),
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                return;
            }
        }
    }//GEN-LAST:event_jButtonDeleteParameterActionPerformed

    private void jButtonModifyParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyParameterActionPerformed
        
        if (jTableParameters.getSelectedRow() < 0) return;
        
        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        
        int index = jTableParameters.convertRowIndexToModel( jTableParameters.getSelectedRow() );
        JRParameter parameter = (JRParameter) dtm.getValueAt(index,0);

        modifyParameter(parameter);
    }//GEN-LAST:event_jButtonModifyParameterActionPerformed

    private void jButtonNewParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewParameterActionPerformed
        // create a new parameter....
        newParameter();
    }//GEN-LAST:event_jButtonNewParameterActionPerformed

    private void jButtonDeleteVariableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteVariableActionPerformed
        
        int[]  rows= jTableVariables.getSelectedRows();
        DefaultTableModel dtm = (DefaultTableModel)jTableVariables.getModel();
        for (int i=rows.length-1; i>=0; --i) {
            int index = jTableVariables.convertRowIndexToModel(rows[i]);
            JRVariable variable = (JRVariable)dtm.getValueAt(index, 0);
            if (!variable.isBuiltin())
            {
                this.getSubDataset().removeVariable(variable);
            }
            // table update done getting an event...dtm.removeRow( index );
        }
    }//GEN-LAST:event_jButtonDeleteVariableActionPerformed

    private void jButtonModifyVariableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyVariableActionPerformed
        
        int index = jTableVariables.convertRowIndexToModel(jTableVariables.getSelectedRow());
        DefaultTableModel dtm = (DefaultTableModel)jTableVariables.getModel();
        JRVariable variable = (JRVariable) dtm.getValueAt(index, 0);
        
        modifyVariable(variable);
    }//GEN-LAST:event_jButtonModifyVariableActionPerformed

    private void jButtonNewVariableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewVariableActionPerformed
        newVariable();
    }//GEN-LAST:event_jButtonNewVariableActionPerformed

    private void jButtonModifyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyFieldActionPerformed
        
        int index = jTableFields.convertRowIndexToModel(jTableFields.getSelectedRow());
        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        JRField field = (JRField) dtm.getValueAt(index, 0);
                                                          
        modifyField(field);
    }//GEN-LAST:event_jButtonModifyFieldActionPerformed

    private void jButtonNewFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewFieldActionPerformed

        newField();
    }//GEN-LAST:event_jButtonNewFieldActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteField;
    private javax.swing.JButton jButtonDeleteParameter;
    private javax.swing.JButton jButtonDeleteVariable;
    private javax.swing.JButton jButtonModifyField;
    private javax.swing.JButton jButtonModifyParameter;
    private javax.swing.JButton jButtonModifyVariable;
    private javax.swing.JButton jButtonNewField;
    private javax.swing.JButton jButtonNewParameter;
    private javax.swing.JButton jButtonNewVariable;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCopy1;
    private javax.swing.JMenuItem jMenuItemCopy2;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemCut1;
    private javax.swing.JMenuItem jMenuItemCut2;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemDelete1;
    private javax.swing.JMenuItem jMenuItemDelete2;
    private javax.swing.JMenuItem jMenuItemDuplicate;
    private javax.swing.JMenuItem jMenuItemDuplicate1;
    private javax.swing.JMenuItem jMenuItemDuplicate2;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemPaste1;
    private javax.swing.JMenuItem jMenuItemPaste2;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelButtons1;
    private javax.swing.JPanel jPanelButtons2;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelParameters;
    private javax.swing.JPanel jPanelVariables;
    private javax.swing.JPopupMenu jPopupMenuFields;
    private javax.swing.JPopupMenu jPopupMenuParameters;
    private javax.swing.JPopupMenu jPopupMenuVariables;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane;
    private it.businesslogic.ireport.gui.table.JDragTable jTableFields;
    private it.businesslogic.ireport.gui.table.JDragTable jTableParameters;
    private it.businesslogic.ireport.gui.table.JDragTable jTableVariables;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * DOCUMENT ME!
     */
    public void updateFields()
    {

        DefaultTableModel dtm = (DefaultTableModel) jTableFields.getModel();
        List selectedObjects = getSelectedObjects(jTableFields);
        dtm.setRowCount(0);
        
        if (getSubDataset() == null) return;

        Enumeration e = getSubDataset().getFields().elements();

        int index = 0;
        while (e.hasMoreElements())
        {

            it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField) e.nextElement();
            Vector row = new Vector();
            row.addElement(field);
            row.addElement(field.getClassType());

            dtm.addRow(row);
            if (selectedObjects.contains(field))
            {
                int view_index = jTableFields.convertRowIndexToView(index);
                jTableFields.addRowSelectionInterval(view_index, view_index);
            }
            index++;
        }

    }

    /**
     * DOCUMENT ME!
     */
    public void updateVariables()
    {

        DefaultTableModel dtm = (DefaultTableModel) jTableVariables.getModel();
        List selectedObjects = getSelectedObjects(jTableVariables);
        dtm.setRowCount(0);
        
        if (getSubDataset() == null) return;

        Enumeration e = getSubDataset().getVariables().elements();
        int index = 0;
        while (e.hasMoreElements())
        {

            it.businesslogic.ireport.JRVariable variable = (it.businesslogic.ireport.JRVariable) e.nextElement();
            Vector row = new Vector();
            row.addElement(variable);
            row.addElement((variable.isBuiltin()
                                ? "yes"
                                : "no"));

            dtm.addRow(row);
            if (selectedObjects.contains(variable))
            {
                int view_index = jTableVariables.convertRowIndexToView(index);
                jTableVariables.addRowSelectionInterval(view_index, view_index);
            }
            index++;
        }
    }

    public java.util.List getSelectedObjects(JDragTable table)
    {
        java.util.List list = new java.util.ArrayList();
        
        int[] rows = table.getSelectedRows();

        DefaultTableModel dtm = (DefaultTableModel)table.getModel();
        
        for (int i = rows.length - 1; i >= 0; --i)
        {
            int index = table.convertRowIndexToModel(rows[i]);
            Object obj = dtm.getValueAt(index, 0);
            list.add(obj);
        }
        
        return list;
    }
    
    /**
     * DOCUMENT ME!
     */
    public void updateParameters()
    {

        DefaultTableModel dtm = (DefaultTableModel) jTableParameters.getModel();
        List selectedObjects = getSelectedObjects(jTableParameters);
        dtm.setRowCount(0);
        
        if (getSubDataset() == null) return;

        //Enumeration e = sortParametersByName( getSubDataset().getParameters().elements() );
        Enumeration e = getSubDataset().getParameters().elements();
                //sortParametersByName(getSubDataset().getParameters()).elements();

        int index = 0;
        while (e.hasMoreElements())
        {

            it.businesslogic.ireport.JRParameter parameter = 
                    (it.businesslogic.ireport.JRParameter) e.nextElement();
            Vector row = new Vector();
            row.addElement(parameter);
            row.addElement(parameter.getClassType());
            row.addElement(
                    (parameter.isIsForPrompting()
                         ? "yes"
                         : "no"));
            row.addElement((parameter.isBuiltin()
                                ? "yes"
                                : "no"));

            dtm.addRow(row);
            if (selectedObjects.contains(parameter))
            {
                int view_index = jTableParameters.convertRowIndexToView(index);
                jTableParameters.addRowSelectionInterval(view_index, view_index);
            }
            index++;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void applyI18n()
    {
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
        jButtonNewField.setText(it.businesslogic.ireport.util.I18n.getString(
                                        "new", "New"));
        jButtonModifyField.setText(it.businesslogic.ireport.util.I18n.getString(
                                           "modify", "Modify"));
        jButtonDeleteField.setText(it.businesslogic.ireport.util.I18n.getString(
                                           "delete", "Delete"));
        jButtonNewVariable.setText(it.businesslogic.ireport.util.I18n.getString(
                                           "new", "New"));
        jButtonModifyVariable.setText(it.businesslogic.ireport.util.I18n.getString(
                                              "modify", "Modify"));
        jButtonDeleteVariable.setText(it.businesslogic.ireport.util.I18n.getString(
                                              "delete", "Delete"));
        jButtonNewParameter.setText(it.businesslogic.ireport.util.I18n.getString(
                                            "new", "New"));
        jButtonModifyParameter.setText(it.businesslogic.ireport.util.I18n.getString(
                                               "modify", "Modify"));
        jButtonDeleteParameter.setText(it.businesslogic.ireport.util.I18n.getString(
                                               "delete", "Delete"));

        jMenuItemCut.setText(it.businesslogic.ireport.util.I18n.getString(
                                     "cut", "Cut"));
        jMenuItemCopy.setText(it.businesslogic.ireport.util.I18n.getString(
                                      "copy", "Copy"));
        jMenuItemPaste.setText(it.businesslogic.ireport.util.I18n.getString(
                                       "paste", "Paste"));
        jMenuItemDelete.setText(it.businesslogic.ireport.util.I18n.getString(
                                        "delete", "Delete"));
        jMenuItemDuplicate.setText(it.businesslogic.ireport.util.I18n.getString(
                                           "duplicate", "Duplicate"));

        jMenuItemCut1.setText(it.businesslogic.ireport.util.I18n.getString(
                                      "cut", "Cut"));
        jMenuItemCopy1.setText(it.businesslogic.ireport.util.I18n.getString(
                                       "copy", "Copy"));
        jMenuItemPaste1.setText(it.businesslogic.ireport.util.I18n.getString(
                                        "paste", "Paste"));
        jMenuItemDelete1.setText(it.businesslogic.ireport.util.I18n.getString(
                                         "delete", "Delete"));
        jMenuItemDuplicate1.setText(it.businesslogic.ireport.util.I18n.getString(
                                            "duplicate", "Duplicate"));

        jMenuItemCut2.setText(it.businesslogic.ireport.util.I18n.getString(
                                      "cut", "Cut"));
        jMenuItemCopy2.setText(it.businesslogic.ireport.util.I18n.getString(
                                       "copy", "Copy"));
        jMenuItemPaste2.setText(it.businesslogic.ireport.util.I18n.getString(
                                        "paste", "Paste"));
        jMenuItemDelete2.setText(it.businesslogic.ireport.util.I18n.getString(
                                         "delete", "Delete"));
        jMenuItemDuplicate2.setText(it.businesslogic.ireport.util.I18n.getString(
                                            "duplicate", "Duplicate"));
        
        
        jTableFields.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("valuesPanel.tableFields.fieldName","Field name") );
        jTableFields.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("valuesPanel.tableFields.classType","Class type") );
        
        jTableVariables.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("valuesPanel.tableVariables.variableName","Variable name") );
        jTableVariables.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("valuesPanel.tableVariables.builtin","Builtin") );
        
        jTableParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("valuesPanel.tableParameters.parameterName","Parameter name") );
        jTableParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("valuesPanel.tableParameters.classType","Class type") );
        jTableParameters.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("valuesPanel.tableParameters.isForPrompting","Is for prompting") );
        jTableParameters.getColumnModel().getColumn(3).setHeaderValue( I18n.getString("valuesPanel.tableParameters.builtin","Builtin") );
        
        jTabbedPane.setTitleAt(0,I18n.getString("valuesPanel.tab.Fields", "Fields"));
        jTabbedPane.setTitleAt(1,I18n.getString("valuesPanel.tab.Variables", "Variables"));
        jTabbedPane.setTitleAt(2,I18n.getString("valuesPanel.tab.Parameters", "Parameters"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param evt DOCUMENT ME!
     */
    public void languageChanged(LanguageChangedEvent evt)
    {
        this.applyI18n();
    }
    
//        private Vector sortFieldsByName(Vector vector)
//    {
//
//        Vector myElements = new Vector();
//        boolean inserted;
//
//        Enumeration enum2 = vector.elements();
//
//        while (enum2.hasMoreElements())
//        {
//
//            JRField elem = (JRField) enum2.nextElement();
//
//            // insert this element in the right position...
//            if (myElements.size() == 0)
//            {
//                myElements.add(elem);
//            }
//            else
//            {
//                inserted = false;
//
//                for (int i = 0; i < myElements.size(); ++i)
//                {
//
//                    JRField elem2 = (JRField) myElements.elementAt(i);
//
//                    if (elem.getName().compareTo(elem2.getName()) < 0)
//                    {
//                        myElements.insertElementAt(elem, i);
//                        inserted = true;
//
//                        break;
//                    }
//
//                }
//
//                if (!inserted)
//                {
//                    myElements.addElement(elem);
//                }
//            }
//        }
//
//        return myElements;
//    }
//
//    private Vector sortVariablesByName(Vector vector)
//    {
//
//        Vector myElements = new Vector();
//        boolean inserted;
//        JRVariable elem2;
//
//        Enumeration enum2 = vector.elements();
//
//        while (enum2.hasMoreElements())
//        {
//
//            JRVariable elem = (JRVariable) enum2.nextElement();
//
//            // insert this element in the right position...
//            if (myElements.size() == 0)
//            {
//                myElements.add(elem);
//            }
//            else
//            {
//                inserted = false;
//
//                for (int i = 0; i < myElements.size(); ++i)
//                {
//                    elem2 = (JRVariable) myElements.elementAt(i);
//
//                    // list builtin parameters first and then the others
//                    if (elem.isBuiltin())
//                    {
//
//                        if (elem.isBuiltin() == elem2.isBuiltin())
//                        {
//
//                            if (elem.getName().compareTo(elem2.getName()) < 0)
//                            {
//                                myElements.insertElementAt(elem, i);
//                                inserted = true;
//
//                                break;
//                            }
//
//                        }
//
//                    }
//                    else
//                    {
//
//                        if (elem.isBuiltin() == elem2.isBuiltin())
//                        {
//
//                            if (elem.getName().compareTo(elem2.getName()) < 0)
//                            {
//                                myElements.insertElementAt(elem, i);
//                                inserted = true;
//
//                                break;
//                            }
//
//                        }
//
//                    }
//                }
//
//                if (!inserted)
//                {
//                    myElements.addElement(elem);
//                }
//
//            }
//        }
//
//        return myElements;
//    }
//
//    
//    private Vector sortParametersByName(Vector vector)
//    {
//
//        Vector myElements = new Vector();
//        boolean inserted;
//        JRParameter elem2;
//        Enumeration enum2 = vector.elements();
//
//        while (enum2.hasMoreElements())
//        {
//
//            JRParameter elem = (JRParameter) enum2.nextElement();
//
//            // insert this element in the right position...
//            if (myElements.size() == 0)
//            {
//                myElements.add(elem);
//            }
//            else
//            {
//                inserted = false;
//
//                for (int i = 0; i < myElements.size(); ++i)
//                {
//                    elem2 = (JRParameter) myElements.elementAt(i);
//
//                    // list builtin parameters first and then the others
//                    if (elem.isBuiltin())
//                    {
//
//                        if (elem.isBuiltin() == elem2.isBuiltin())
//                        {
//
//                            if (elem.getName().compareTo(elem2.getName()) < 0)
//                            {
//                                myElements.insertElementAt(elem, i);
//                                inserted = true;
//
//                                break;
//                            }
//
//                        }
//
//                    }
//                    else
//                    {
//
//                        if (elem.isBuiltin() == elem2.isBuiltin())
//                        {
//
//                            if (elem.getName().compareTo(elem2.getName()) < 0)
//                            {
//                                myElements.insertElementAt(elem, i);
//                                inserted = true;
//
//                                break;
//                            }
//
//                        }
//
//                    }
//                }
//
//                if (!inserted)
//                {
//                    myElements.addElement(elem);
//                }
//            }
//        }
//
//        return myElements;
//    }
   
    
    
    
        /**
     * DOCUMENT ME!
     * 
     * @param p DOCUMENT ME!
     */
    public void modifyErrorParameter(it.businesslogic.ireport.JRParameter p)
    {

        // 1. Switch to parameters panel.
        this.jTabbedPane.setSelectedComponent(jPanelParameters);

        // 2. Fine the parameter and select it
        DefaultTableModel dtm = (DefaultTableModel) jTableParameters.getModel();

        for (int i = 0; i < dtm.getRowCount(); ++i)
        {

            if (dtm.getValueAt(i, 0) == p)
            {
                int index = jTableParameters.convertRowIndexToView(i);
                jTableParameters.setRowSelectionInterval(index, index);
                this.jButtonModifyParameterActionPerformed(new java.awt.event.ActionEvent(
                                                                   jButtonModifyParameter, 
                                                                   0, 
                                                                   jButtonModifyParameter.getName()));

                return;
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param tab DOCUMENT ME!
     */
    public void gotoTab(String tab)
    {

        if (tab.equals("Parameters"))
        {
            this.jTabbedPane.setSelectedIndex(2);
        }
        else if (tab.equals("Fields"))
        {
            this.jTabbedPane.setSelectedIndex(0);
        }
        else if (tab.equals("Variables"))
        {
            this.jTabbedPane.setSelectedIndex(1);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardFields()
    {

        return clipboardFields;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardFields DOCUMENT ME!
     */
    public void setClipboardFields(Vector clipboardFields)
    {
        this.clipboardFields = clipboardFields;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardVariables()
    {

        return clipboardVariables;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardVariables DOCUMENT ME!
     */
    public void setClipboardVariables(Vector clipboardVariables)
    {
        this.clipboardVariables = clipboardVariables;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardParameters()
    {

        return clipboardParameters;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardParameters DOCUMENT ME!
     */
    public void setClipboardParameters(Vector clipboardParameters)
    {
        this.clipboardParameters = clipboardParameters;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param fieldsToDuplicate DOCUMENT ME!
     */
    public void duplicateFields(Vector fieldsToDuplicate)
    {
         duplicateFields(fieldsToDuplicate, getSubDataset());
    }
     
    public void duplicateFields(Vector fieldsToDuplicate, SubDataset dataset)
    {

        for (int i = 0; i < fieldsToDuplicate.size(); ++i)
        {

            JRField field = ((JRField) fieldsToDuplicate.elementAt(i)).cloneMe();
            String base_name = field.getName();
            Vector fields = dataset.getFields();

            for (int j = 0;; ++j)
            {

                boolean found = false;

                for (int k = 0; k < fields.size(); ++k)
                {

                    JRField field1 = (JRField) fields.elementAt(k);

                    if (j == 0)
                    {

                        if (field1.getName().equals(base_name))
                        {
                            found = true;

                            break;
                        }
                    }
                    else
                    {

                        if (field1.getName().equals(base_name + "_" + j))
                        {
                            found = true;

                            break;
                        }
                    }
                }

                if (!found)
                {
                    field.setName(base_name + 
                                  ((j == 0)
                                       ? ""
                                       : "_" + j));

                    break;
                }
            }

            dataset.addField(field);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param variablesToDuplicate DOCUMENT ME!
     */
    public void duplicateVariables(Vector variablesToDuplicate)
    {
        duplicateVariables(variablesToDuplicate, getSubDataset() );
    }
    
    public void duplicateVariables(Vector variablesToDuplicate, SubDataset dataset)
    {

        for (int i = 0; i < variablesToDuplicate.size(); ++i)
        {

            JRVariable variable = ((JRVariable) variablesToDuplicate.elementAt(
                                           i)).cloneMe();
            String base_name = variable.getName();
            Vector variables = dataset.getVariables();

            for (int j = 0;; ++j)
            {

                boolean found = false;

                for (int k = 0; k < variables.size(); ++k)
                {

                    JRVariable variable1 = (JRVariable) variables.elementAt(k);

                    if (j == 0)
                    {

                        if (variable1.getName().equals(base_name))
                        {
                            found = true;

                            break;
                        }
                    }
                    else
                    {

                        if (variable1.getName().equals(base_name + "_" + j))
                        {
                            found = true;

                            break;
                        }
                    }
                }

                if (!found)
                {
                    variable.setName(base_name + 
                                     ((j == 0)
                                          ? ""
                                          : "_" + j));

                    break;
                }
            }

            if (variable.isBuiltin())
            {
                JOptionPane.showMessageDialog(this, 
                        I18n.getString( "messages.valuesPanel.duplicatingBuiltinVariable","It's not possible duplicate built-in variables!"));
            }
            else
            {
                dataset.addVariable(variable);
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param parametersToDuplicate DOCUMENT ME!
     */
    public void duplicateParameters(Vector parametersToDuplicate)
    {
         duplicateParameters(parametersToDuplicate, getSubDataset());
    }
     
    public void duplicateParameters(Vector parametersToDuplicate, SubDataset dataset)
    {

        boolean msg_disp = false;

        for (int i = 0; i < parametersToDuplicate.size(); ++i)
        {

            JRParameter parameter = ((JRParameter) parametersToDuplicate.elementAt(
                                             i)).cloneMe();
            String base_name = parameter.getName();
            Vector parameters = dataset.getParameters();

            for (int j = 0;; ++j)
            {

                boolean found = false;

                for (int k = 0; k < parameters.size(); ++k)
                {

                    JRParameter parameter1 = (JRParameter) parameters.elementAt(
                                                     k);

                    if (j == 0)
                    {

                        if (parameter1.getName().equals(base_name))
                        {
                            found = true;

                            break;
                        }
                    }
                    else
                    {

                        if (parameter1.getName().equals(base_name + "_" + j))
                        {
                            found = true;

                            break;
                        }
                    }
                }

                if (!found)
                {
                    parameter.setName(base_name + 
                                      ((j == 0)
                                           ? ""
                                           : "_" + j));

                    break;
                }
            }

            if (parameter.isBuiltin())
            {

                if (!msg_disp)
                {
                    JOptionPane.showMessageDialog(this, 
                            I18n.getString( "messages.valuesPanel.duplicatingBuiltinParameters","It's not possible duplicate built-in parameters!"));
                                                  
                    msg_disp = true;
                }
            }
            else
            {
                dataset.addParameter(parameter);
            }
        }
    }
    
        /**
     * DOCUMENT ME!
     * 
     * @param field DOCUMENT ME!
     */
    public void modifyField(JRField field)
    {
        modifyField(field, getSubDataset());
    }
    
    public void modifyField(JRField field, SubDataset ds)
    {
        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRFieldDialog jrpd = new JRFieldDialog(parent, true);
        jrpd.setSubDataset( ds );
        jrpd.setField(field);
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            JRField oldFile = field.cloneMe();
            field.setName(jrpd.getField().getName());
            field.setClassType(jrpd.getField().getClassType());

            field.setDescription(jrpd.getField().getDescription());
            field.setProperties(jrpd.getField().getProperties());

            ds.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(ds, 
                                                                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.FIELD, 
                                                                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                                                                       oldFile, 
                                                                                                                                       field));
        }
    }
    
        /**
     * DOCUMENT ME!
     */
    public void newField()
    {
        newField(getSubDataset());
    }
    
    
    public void newField(SubDataset sd)
    {

        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRFieldDialog jrpd = new JRFieldDialog(parent, true);
        jrpd.setSubDataset( sd );
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {

            JRField field = jrpd.getField();
            sd.addField(field);
        }
    }
    
    
        /**
     * DOCUMENT ME!
     * 
     * @param variable DOCUMENT ME!
     */
    public void modifyVariable(JRVariable variable)
    {
        modifyVariable(variable, getSubDataset(), JRVariableDialog.COMPONENT_NONE);
    }
    
    public void modifyVariable(JRVariable variable, SubDataset sd)
    {
        modifyVariable(variable, sd, JRVariableDialog.COMPONENT_NONE);
    }
    
    public void modifyVariable(JRVariable variable, SubDataset sd, int elementToFocus)
    {

        if (variable.isBuiltin())
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString( "messages.valuesPanel.cantModifyBuiltInVariables","You can't modify or delete builtin variables!"),
                    I18n.getString( "messages.valuesPanel.cantModifyBuiltInVariablesCaption","Invalid operation!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRVariableDialog jrpd = new JRVariableDialog(sd, parent, true);
        jrpd.setVariable(variable);
        jrpd.setFocusedExpression( elementToFocus );
        jrpd.setVisible(true);
        

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            JRVariable oldVar = variable.cloneMe();
            sd.getKeywordLookup().removeKeyword(
                    "$V{" + variable.getName() + "}");
            variable.setName(jrpd.getVariable().getName());
            sd.getKeywordLookup().addKeyword(
                    "$V{" + variable.getName() + "}");
            variable.setClassType(jrpd.getVariable().getClassType());
            variable.setCalculation(jrpd.getVariable().getCalculation());
            variable.setResetType(jrpd.getVariable().getResetType());
            variable.setResetGroup(jrpd.getVariable().getResetGroup());
            variable.setExpression(jrpd.getVariable().getExpression());
            variable.setInitialValueExpression(jrpd.getVariable().getInitialValueExpression());
            variable.setIncrementerFactoryClass(jrpd.getVariable().getIncrementerFactoryClass());
            variable.setIncrementType(jrpd.getVariable().getIncrementType());
            variable.setIncrementGroup(jrpd.getVariable().getIncrementGroup());

            sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(sd, 
                                                                                                                                                           it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.VARIABLE, 
                                                                                                                                                           it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                                                                                           oldVar, 
                                                                                                                                                           variable));
        }
    }

    
    /**
     * DOCUMENT ME!
     */
    public void newVariable()
    {
        newVariable(getSubDataset());
    }
    
    
    public void newVariable(SubDataset sd)
    {
        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRVariableDialog jrpd = new JRVariableDialog(sd, parent, true);
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {

            JRVariable variable = jrpd.getVariable();
            sd.addVariable(variable);
        }
    }
    
    
    /**
     * Quick way to open the parameter dialog to edit the parameter.
     * The methods takes care to notify who is listening to change events.
     * 
     * @param parameter DOCUMENT ME!
     */
    public void modifyParameter(JRParameter parameter)
    {
        modifyParameter(parameter, getSubDataset(), JRParameterDialog.COMPONENT_NONE);
    }
    
    public void modifyParameter(JRParameter parameter, SubDataset sd)
    {
        modifyParameter(parameter, sd, JRParameterDialog.COMPONENT_NONE);
    }
    
    public void modifyParameter(JRParameter parameter, SubDataset sd, int elementToFocus)
    {

        if (parameter.isBuiltin())
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString( "messages.valuesPanel.cantModifyBuiltInParameters","You can't modify or delete builtin parameters!"),
                    I18n.getString( "messages.valuesPanel.cantModifyBuiltInParametersCaption","Invalid operation!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRParameterDialog jrpd = new JRParameterDialog(parent, true);
        jrpd.setSubDataset( sd );
        jrpd.setParameter(parameter);
        jrpd.setFocusedExpression(elementToFocus);
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {
            JRParameter oldParam = parameter.cloneMe();
            sd.getKeywordLookup().removeKeyword(
                    "$P{" + parameter.getName() + "}");
            parameter.setName(jrpd.getParameter().getName());
            sd.getKeywordLookup().addKeyword(
                    "$P{" + parameter.getName() + "}");
            parameter.setClassType(jrpd.getParameter().getClassType());
            parameter.setDescription(jrpd.getParameter().getDescription());
            parameter.setDefaultValueExpression(jrpd.getParameter().getDefaultValueExpression());
            parameter.setIsForPrompting(jrpd.getParameter().isIsForPrompting());
            parameter.setProperties(jrpd.getParameter().getProperties() );
            sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(new it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent(sd, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.PARAMETER, 
                                                                                       it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent.MODIFIED, 
                                                                                       oldParam, 
                                                                                       parameter));
        }
    }
    
    /**
     * DOCUMENT ME!
     */
    public void newParameter()
    {
        newParameter(getSubDataset());
    }
    
    
    public void newParameter(SubDataset sd)
    {

        java.awt.Frame parent = Misc.frameFromComponent(this);
        JRParameterDialog jrpd = new JRParameterDialog(parent, true);
        jrpd.setSubDataset( sd );
        jrpd.setVisible(true);

        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION)
        {

            JRParameter parameter = jrpd.getParameter();
            sd.addParameter(parameter);
        }
    }
    
    
}
