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
 * GroupsDialog.java
 * 
 * Created on 10 maggio 2003, 9.51
 *
 */

package it.businesslogic.ireport.gui.subdataset;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.util.*;

import java.util.*;

/**
 *
 * @author  Administrator
 */
public class GroupsDialog extends javax.swing.JDialog {
    
    
    private SubDataset subDataset=null;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteGroup;
    private javax.swing.JButton jButtonModifyGroup;
    private javax.swing.JButton jButtonMoveDownGroup;
    private javax.swing.JButton jButtonMoveUpGroup;
    private javax.swing.JButton jButtonNewGroup;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableGroups;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Creates new form GroupsDialog
     */
    public GroupsDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        applyI18n();
        this.setSize(420, 250);
        Misc.centerFrame(this);
        
        DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTableGroups.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTableGroupsListSelectionValueChanged(e);
            }
        });
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
            javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            };

            getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
            getRootPane().getActionMap().put("ESCAPE", escapeAction);

    }
    
    public void jTableGroupsListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        
        /* Buttons Move Up and Move Down */
        if (this.jTableGroups.getSelectedRowCount() == 1 && this.jTableGroups.getRowCount() > 1) {
            int row= jTableGroups.getSelectedRows()[0];
            this.jButtonMoveDownGroup.setEnabled( row != this.jTableGroups.getRowCount()-1);
            this.jButtonMoveUpGroup.setEnabled( row != 0);
        } else {
            // Moving of multiple groups not allowed
            this.jButtonMoveDownGroup.setEnabled(false);
            this.jButtonMoveUpGroup.setEnabled(false);
        }
        
        /* Other buttons*/
        if (this.jTableGroups.getSelectedRowCount() > 0) {
            this.jButtonModifyGroup.setEnabled(true);
            this.jButtonDeleteGroup.setEnabled(true);
        }
        else {
            this.jButtonModifyGroup.setEnabled(false);
            this.jButtonDeleteGroup.setEnabled(false);
        }
    }
    
    public void updateGroups() {
        
        if (getSubDataset() == null) {
            return;
        }
        
        int[] selectedRows = jTableGroups.getSelectedRows();
        List selectedObjects = new ArrayList();
        for (int i=0; i<selectedRows.length; ++i)
        {
            selectedObjects.add( jTableGroups.getValueAt(selectedRows[i],0) );
        }
        
        DefaultTableModel dtm = (DefaultTableModel)jTableGroups.getModel();
        dtm.setRowCount(0);
        
        int k=0;
        for (Iterator i = getSubDataset().getGroups().iterator(); i.hasNext(); ) {
            it.businesslogic.ireport.Group group = (it.businesslogic.ireport.Group) i.next();
            Vector row = new Vector();
            row.addElement( group );
            
            dtm.addRow(row);
            if (selectedObjects.contains(group))
            {
                jTableGroups.getSelectionModel().addSelectionInterval(k,k);
            }
            k++;
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
        jTableGroups = new javax.swing.JTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonNewGroup = new javax.swing.JButton();
        jButtonModifyGroup = new javax.swing.JButton();
        jButtonMoveUpGroup = new javax.swing.JButton();
        jButtonMoveDownGroup = new javax.swing.JButton();
        jButtonDeleteGroup = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelFields.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jTableGroups.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Group name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableGroups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableGroupsMouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(jTableGroups);

        jPanelFields.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jPanelButtons.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanelButtons.setMinimumSize(new java.awt.Dimension(100, 10));
        jButtonNewGroup.setText("New");
        jButtonNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanelButtons.add(jButtonNewGroup, gridBagConstraints);

        jButtonModifyGroup.setText("Modify");
        jButtonModifyGroup.setEnabled(false);
        jButtonModifyGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonModifyGroup, gridBagConstraints);

        jButtonMoveUpGroup.setText("Move Up");
        jButtonMoveUpGroup.setEnabled(false);
        jButtonMoveUpGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonMoveUpGroup, gridBagConstraints);

        jButtonMoveDownGroup.setText("Move Down");
        jButtonMoveDownGroup.setEnabled(false);
        jButtonMoveDownGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonMoveDownGroup, gridBagConstraints);

        jButtonDeleteGroup.setText("Delete");
        jButtonDeleteGroup.setEnabled(false);
        jButtonDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonDeleteGroup, gridBagConstraints);

        jPanelFields.add(jPanelButtons, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanelFields, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableGroupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableGroupsMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            jButtonModifyGroupActionPerformed(null);
        }
        
    }//GEN-LAST:event_jTableGroupsMouseClicked
    
    private void jButtonMoveDownGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveDownGroupActionPerformed
        if ( jTableGroups.getSelectedRows().length == 1 ) {
            int[] rows= jTableGroups.getSelectedRows();
            Group grp = (Group) jTableGroups.getValueAt( rows[0], 0) ;
            
            // Now move the groups within the group array
            int groupPos = getSubDataset().getGroups().indexOf( grp );
            // groupTwo is always after grpOne
            if (groupPos < getSubDataset().getGroups().size()-1)
            {
                getSubDataset().getGroups().remove(groupPos);
                getSubDataset().getGroups().insertElementAt( grp, groupPos + 1);
                updateGroups();
                jTableGroupsListSelectionValueChanged(null);
            }
            
            //((MainFrame)this.getParent()).reportBandChanged( new ReportBandChangedEvent(this.getJReportFrame(), null, ReportBandChangedEvent.CHANGED ));
        }
    }//GEN-LAST:event_jButtonMoveDownGroupActionPerformed
    
    private void jButtonMoveUpGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveUpGroupActionPerformed
        
        if ( jTableGroups.getSelectedRows().length == 1 ) {
            int[] rows= jTableGroups.getSelectedRows();
            Group grp = (Group) jTableGroups.getValueAt( rows[0], 0) ;
            // Now move the groups within the group array
            int groupPos = getSubDataset().getGroups().indexOf( grp );
            // groupTwo is always after grpOne
            if (groupPos > 0)
            {
                getSubDataset().getGroups().remove(groupPos);
                getSubDataset().getGroups().insertElementAt( grp, groupPos - 1);
                updateGroups();
                jTableGroupsListSelectionValueChanged(null);
            }
            
            // ((MainFrame)this.getParent()).reportBandChanged( new ReportBandChangedEvent(this.getJReportFrame(), null, ReportBandChangedEvent.CHANGED ));
        }
        
    }//GEN-LAST:event_jButtonMoveUpGroupActionPerformed
    
    private void jButtonDeleteGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteGroupActionPerformed
        // jTableGroups.getSelectedRows();  disabled by Robert, July 31, 2004
        
        int[]  rows= jTableGroups.getSelectedRows();
        for (int i=rows.length-1; i>=0; --i) {
            
            getSubDataset().getGroups().remove( jTableGroups.getValueAt( rows[i], 0));
        }
        updateGroups();
        //((MainFrame)this.getParent()).reportBandChanged( new ReportBandChangedEvent(this.getJReportFrame(), null, ReportBandChangedEvent.REMOVED ));
    }//GEN-LAST:event_jButtonDeleteGroupActionPerformed
    
    private void jButtonModifyGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyGroupActionPerformed
        it.businesslogic.ireport.Group group = (it.businesslogic.ireport.Group)jTableGroups.getValueAt( jTableGroups.getSelectedRow(), 0);
        
        group = modifyGroup(getSubDataset(), group, this);
        
        if (group != null)
        {
            jTableGroups.setValueAt( group, jTableGroups.getSelectedRow(), 0);
        }
    }//GEN-LAST:event_jButtonModifyGroupActionPerformed
    
    private void jButtonNewGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewGroupActionPerformed
        GroupDialog jrpd = new GroupDialog(this, true);
        jrpd.setSubDataset( getSubDataset());
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Group g = new it.businesslogic.ireport.Group(getSubDataset(), jrpd.getGroupName());
            g.setGroupExpression( jrpd.getExpression());
            //g.setIsReprintHeaderOnEachPage(jrpd.isPrintHeaderEachPage() );
            //g.setIsResetPageNumber( jrpd.isResetPageNumber()  );
            //g.setIsStartNewColumn( jrpd.isStartNewColumn());
            //g.setIsStartNewPage( jrpd.isStartNewPage());
            //g.setMinHeightToStartNewPage( jrpd.getMinHeightStartNewPage() );
            
            //g.getGroupHeader().setHeight(  jrpd.getHeaderBandHeight());
            //g.getGroupFooter().setHeight(  jrpd.getFooterBandHeight());
            
            getSubDataset().getGroups().add(g);
            this.updateGroups();
            
            // refersh bands frame....
            //((MainFrame)this.getParent()).reportBandChanged( new ReportBandChangedEvent(this.getJReportFrame(), null, ReportBandChangedEvent.ADDED ));
        }
    }//GEN-LAST:event_jButtonNewGroupActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Setter for property jReportFrame.
     * @param jReportFrame New value of property jReportFrame.
     *
     */
    public void setSubDataset(it.businesslogic.ireport.SubDataset subDataset) {
        this.subDataset = subDataset;
        if (subDataset == null) return;
        //
		this.setTitle(subDataset.getName()+" "+I18n.getString("groupsDialog.groups"," groups..."));
        //this.setTitle(subDataset.getName()+" groups...");
		//
        updateGroups();
    }
    
    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void applyI18n(){
                // Start autogenerated code ----------------------
    			jButtonDeleteGroup.setText(I18n.getString("groupsDialog.buttonDeleteGroup","Delete"));
                jButtonModifyGroup.setText(I18n.getString("groupsDialog.buttonModifyGroup","Modify"));
                jButtonMoveDownGroup.setText(I18n.getString("groupsDialog.buttonMoveDownGroup","Move Down"));
                jButtonMoveUpGroup.setText(I18n.getString("groupsDialog.buttonMoveUpGroup","Move Up"));
                jButtonNewGroup.setText(I18n.getString("groupsDialog.buttonNewGroup","New"));
                // End autogenerated code ----------------------
                jTableGroups.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("groupsDialog.tablecolumn.groupName","Group name") );
                
    }
    
    
    public static Group modifyGroup(SubDataset subDataset, Group group, Component parent)
    {
        return modifyGroup(subDataset, group, parent, GroupDialog.COMPONENT_NONE);
    
    }
    
    public static Group modifyGroup(SubDataset subDataset, Group group, Component parent, int focusComponent)
    {
        Object obj = null;
        if (parent != null)
        {
            obj = SwingUtilities.getWindowAncestor(parent);
        }
        GroupDialog jrpd = null;
        
        if (obj != null && obj instanceof Dialog)
        {
            jrpd = new GroupDialog( (Dialog)obj, true);
        }
        else
        {
            jrpd = new GroupDialog( (Frame)obj, true);
        }
        jrpd.setGroup( group );
        jrpd.setSubDataset( subDataset);
        jrpd.setFocusedExpression(focusComponent);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            String oldName = group.getName();
            
            group.setName( jrpd.getGroupName() );
            group.setGroupExpression( jrpd.getExpression());
            //group.setIsReprintHeaderOnEachPage(jrpd.isPrintHeaderEachPage() );
            //group.setIsResetPageNumber( jrpd.isResetPageNumber()  );
            //group.setIsStartNewColumn( jrpd.isStartNewColumn());
            //group.setIsStartNewPage( jrpd.isStartNewPage());
            //group.setMinHeightToStartNewPage( jrpd.getMinHeightStartNewPage() );
            
            //group.getGroupHeader().setName( group.getName()+"Header");
            //group.getGroupFooter().setName( group.getName()+"Footer");
            //group.getGroupHeader().setHeight(  jrpd.getHeaderBandHeight());
            //group.getGroupFooter().setHeight(  jrpd.getFooterBandHeight());
            
            for (int k=subDataset.getVariables().size()-1; k>=0; --k)
            {
                JRVariable var = (JRVariable)subDataset.getVariables().elementAt(k);
                if (var.isBuiltin() && var.getName().equalsIgnoreCase(oldName+"_COUNT"))
                {
                    subDataset.removeVariable(var);
                    break;
                }
            }
            subDataset.addVariable(new it.businesslogic.ireport.JRVariable(group.getName()+"_COUNT","java.lang.Integer", true));
            
            
            // Update All variables...
            Enumeration e = subDataset.getVariables().elements();
            while (e.hasMoreElements()) {
                JRVariable var = (JRVariable)e.nextElement();
                if (var.getResetGroup() != null &&
                var.getResetGroup().equals(oldName)) {
                    var.setResetGroup( group.getName() );
                }
            }
            
            
            //this.updateGroups();
            //((MainFrame)this.getParent()).reportBandChanged( new ReportBandChangedEvent(this.getJReportFrame(), null, ReportBandChangedEvent.CHANGED ));
        }
        
        return group;
    }
}
