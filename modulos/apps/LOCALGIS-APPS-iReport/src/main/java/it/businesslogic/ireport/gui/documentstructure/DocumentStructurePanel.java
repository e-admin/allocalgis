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
 * DocumentStructurePanel.java
 * 
 * Created on 25 novembre 2005, 11.34
 *
 */

package it.businesslogic.ireport.gui.documentstructure;
import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.ChartReportElement2;
import it.businesslogic.ireport.ElementGroup;
import it.businesslogic.ireport.FrameReportElement;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.chart.gui.ChartPropertiesDialog;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.ValuesDialog;
import it.businesslogic.ireport.gui.docking.GenericDragTargetListener;
import it.businesslogic.ireport.gui.event.ReportBandChangedEvent;
import it.businesslogic.ireport.gui.event.ReportBandsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;
import it.businesslogic.ireport.gui.event.ReportElementsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedListener;
import it.businesslogic.ireport.gui.event.ReportListener;
import it.businesslogic.ireport.gui.event.ReportObjectsSelectionEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedEvent;
import it.businesslogic.ireport.gui.event.ReportSubDatasetChangedListener;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedEvent;
import it.businesslogic.ireport.gui.event.SubDatasetObjectChangedListener;
import it.businesslogic.ireport.gui.subdataset.SubDatasetDialog;
import it.businesslogic.ireport.gui.wizard.ReportGroupWizard;
import it.businesslogic.ireport.undo.UnGroupEmentsOperation;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;

import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
/**
 *
 * @author  Administrator
 */
public class DocumentStructurePanel extends javax.swing.JPanel 
       implements ReportListener, 
                  SubDatasetObjectChangedListener,
                  ReportSubDatasetChangedListener,
                  LanguageChangedListener,
                  ReportFrameActivatedListener {

    private boolean dontHandleEvent = false;
    
    ReportObjectTreeNode fieldsNode = null;
    ReportObjectTreeNode variablesNode = null;
    ReportObjectTreeNode parametersNode = null;

    /**
     * This variable is used to test when apply a new set of extended path... 
     */
    private JReportFrame oldJReportFrame = null;
    
    private JReportFrame jReportFrame = null;

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    /**
     * Set the current report frame and update the document structure.... 
     * @param jReportFrame the active report frame
     */
    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
        updateDocumentStructureTree();
        if (jReportFrame != null)
        {
            addObjectsToSelection(jReportFrame.getSelectedObjects());
        }
    }

    /** Creates new form DocumentStructurePanel */
    public DocumentStructurePanel() {
        initComponents();

        DefaultTreeSelectionModel dtsm = (DefaultTreeSelectionModel)jTreeDocument.getSelectionModel();
        dtsm.setSelectionMode( DefaultTreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );

        jTreeDocument.setDropTarget(new DropTarget(this, new GenericDragTargetListener()));

        //Modified by Felix Firgau for I18n on Feb 9th 2006
        DocumentStructureTreeNode rootDoc = new DocumentStructureTreeNode(it.businesslogic.ireport.util.I18n.getString("document","Document"));
        //End

        fieldsNode = new ReportObjectTreeNode("Fields",true);
        variablesNode = new ReportObjectTreeNode("Variables",true);
        parametersNode = new ReportObjectTreeNode("Parameters",true);
        javax.swing.tree.DefaultTreeModel modelDoc = new javax.swing.tree.DefaultTreeModel(rootDoc);

        jTreeDocument.setModel( modelDoc );
        jTreeDocument.setCellRenderer( new DocumentStructureTreeCellRenderer());
        
        //I18n.addOnLanguageChangedListener( this );
        
        MainFrame.getMainInstance().addReportFrameActivatedListener(this);
        //MainFrame.getMainInstance().addReportListener(this);
        
        jTreeDocument.setTransferHandler(new DocumentStructureExportHandler());
        jTreeDocument.setDragEnabled(true);
        
        jToggleButtonFilterBuiltinParameters.setSelected( MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinParameters","true").equals("true") );
        jToggleButtonFilterBuiltinVariables.setSelected( MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinVariables","true").equals("true") );

        I18n.addOnLanguageChangedListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPopupMenuDocumentStructure = new javax.swing.JPopupMenu();
        jMenuItemDS_properties = new javax.swing.JMenuItem();
        jMenuItemDS_chartProperties = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JSeparator();
        jMenuItemMoveUp = new javax.swing.JMenuItem();
        jMenuItemMoveDown = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JSeparator();
        jMenuItemDS_cut = new javax.swing.JMenuItem();
        jMenuItemDS_copy = new javax.swing.JMenuItem();
        jMenuItemDS_paste = new javax.swing.JMenuItem();
        jMenuItemDS_delete = new javax.swing.JMenuItem();
        jPopupMenuDocumentStructureGroup = new javax.swing.JPopupMenu();
        jMenuItemUngroup = new javax.swing.JMenuItem();
        jPopupMenuFields = new javax.swing.JPopupMenu();
        jMenuItemEdit = new javax.swing.JMenuItem();
        jMenuAdd = new javax.swing.JMenu();
        jMenuItemParameter = new javax.swing.JMenuItem();
        jMenuItemNewField = new javax.swing.JMenuItem();
        jMenuItemNewVariable = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemNewSubDataset = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuItemMoveUpObj = new javax.swing.JMenuItem();
        jMenuItemMoveDownObj = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemAddGroup = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuItemRefresh = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTreeDocument = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jLabelFilters = new javax.swing.JLabel();
        jToggleButtonFilterBuiltinParameters = new javax.swing.JToggleButton();
        jToggleButtonFilterBuiltinVariables = new javax.swing.JToggleButton();

        jMenuItemDS_properties.setText("Properties");
        jMenuItemDS_properties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_propertiesActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_properties);

        jMenuItemDS_chartProperties.setText("Chart properties");
        jMenuItemDS_chartProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_chartPropertiesActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_chartProperties);
        jPopupMenuDocumentStructure.add(jSeparator22);

        jMenuItemMoveUp.setText("Move up");
        jMenuItemMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveUpActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemMoveUp);

        jMenuItemMoveDown.setText("Move down");
        jMenuItemMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveDownActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemMoveDown);
        jPopupMenuDocumentStructure.add(jSeparator23);

        jMenuItemDS_cut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png"))); // NOI18N
        jMenuItemDS_cut.setText("Cut");
        jMenuItemDS_cut.setEnabled(false);
        jMenuItemDS_cut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_cutActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_cut);

        jMenuItemDS_copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png"))); // NOI18N
        jMenuItemDS_copy.setText("Copy");
        jMenuItemDS_copy.setEnabled(false);
        jMenuItemDS_copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_copyActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_copy);

        jMenuItemDS_paste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png"))); // NOI18N
        jMenuItemDS_paste.setText("Paste");
        jMenuItemDS_paste.setEnabled(false);
        jMenuItemDS_paste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_pasteActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_paste);

        jMenuItemDS_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png"))); // NOI18N
        jMenuItemDS_delete.setText("Delete");
        jMenuItemDS_delete.setEnabled(false);
        jMenuItemDS_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDS_deleteActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructure.add(jMenuItemDS_delete);

        jMenuItemUngroup.setText("Ungroup");
        jMenuItemUngroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUngroupActionPerformed(evt);
            }
        });
        jPopupMenuDocumentStructureGroup.add(jMenuItemUngroup);

        jPopupMenuFields.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenuFieldsPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemEdit.setText("Edit");
        jMenuItemEdit.setEnabled(false);
        jMenuItemEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemEdit);

        jMenuAdd.setText("Add...");

        jMenuItemParameter.setText("Parameter");
        jMenuItemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemParameterActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemParameter);

        jMenuItemNewField.setText("Field");
        jMenuItemNewField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewFieldActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemNewField);

        jMenuItemNewVariable.setText("Variable");
        jMenuItemNewVariable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewVariableActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemNewVariable);
        jMenuAdd.add(jSeparator3);

        jMenuItemNewSubDataset.setText("Sub dataset");
        jMenuItemNewSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewSubDatasetActionPerformed(evt);
            }
        });
        jMenuAdd.add(jMenuItemNewSubDataset);

        jPopupMenuFields.add(jMenuAdd);
        jPopupMenuFields.add(jSeparator5);

        jMenuItemMoveUpObj.setText("Move Up");
        jMenuItemMoveUpObj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveUpObjActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemMoveUpObj);

        jMenuItemMoveDownObj.setText("Move Down");
        jMenuItemMoveDownObj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveDownObjActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemMoveDownObj);
        jPopupMenuFields.add(jSeparator1);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png"))); // NOI18N
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setEnabled(false);
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemCut);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png"))); // NOI18N
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setEnabled(false);
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemCopy);

        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png"))); // NOI18N
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setEnabled(false);
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemPaste);

        jMenuItemDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png"))); // NOI18N
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.setEnabled(false);
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemDelete);
        jPopupMenuFields.add(jSeparator2);

        jMenuItemAddGroup.setText("Add report group");
        jMenuItemAddGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddGroupActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemAddGroup);
        jPopupMenuFields.add(jSeparator4);

        jMenuItemRefresh.setText("Refresh");
        jMenuItemRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRefreshActionPerformed(evt);
            }
        });
        jPopupMenuFields.add(jMenuItemRefresh);

        setLayout(new java.awt.BorderLayout());

        jTreeDocument.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTreeDocumentMousePressed(evt);
            }
        });
        jTreeDocument.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
                jTreeDocumentTreeCollapsed(evt);
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jTreeDocumentTreeExpanded(evt);
            }
        });
        jTreeDocument.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeDocumentValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTreeDocument);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelFilters.setText("Filters:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel1.add(jLabelFilters, gridBagConstraints);

        jToggleButtonFilterBuiltinParameters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/builtin_parameter.png"))); // NOI18N
        jToggleButtonFilterBuiltinParameters.setSelected(true);
        jToggleButtonFilterBuiltinParameters.setToolTipText("Show / Hide Built-in Parameters");
        jToggleButtonFilterBuiltinParameters.setBorder(null);
        jToggleButtonFilterBuiltinParameters.setFocusPainted(false);
        jToggleButtonFilterBuiltinParameters.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButtonFilterBuiltinParameters.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinParameters.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinParameters.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinParameters.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButtonFilterBuiltinParametersItemStateChanged(evt);
            }
        });
        jToggleButtonFilterBuiltinParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonFilterBuiltinParametersActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButtonFilterBuiltinParameters, new java.awt.GridBagConstraints());

        jToggleButtonFilterBuiltinVariables.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/builtin_variable.png"))); // NOI18N
        jToggleButtonFilterBuiltinVariables.setSelected(true);
        jToggleButtonFilterBuiltinVariables.setToolTipText("Show / Hide Built-in Variables");
        jToggleButtonFilterBuiltinVariables.setBorder(null);
        jToggleButtonFilterBuiltinVariables.setFocusPainted(false);
        jToggleButtonFilterBuiltinVariables.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButtonFilterBuiltinVariables.setMaximumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinVariables.setMinimumSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinVariables.setPreferredSize(new java.awt.Dimension(22, 22));
        jToggleButtonFilterBuiltinVariables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonFilterBuiltinVariablesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jToggleButtonFilterBuiltinVariables, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

private void jMenuItemMoveDownObjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMoveDownObjActionPerformed
    TreePath tp = jTreeDocument.getSelectionPath();
    if (tp == null) return;
    if (getJReportFrame() == null) return; // just in case
    
    
    Object selectedObject = ((DefaultMutableTreeNode)tp.getLastPathComponent()).getUserObject();
    SubDataset sd = Misc.getObjectSubDataset(getJReportFrame().getReport() , selectedObject);
    
    if (sd == null) return;
    
    Vector list = null;
    int objType = 0;
    
    if (selectedObject instanceof JRField)
    {
        list = sd.getFields();
        objType = SubDatasetObjectChangedEvent.FIELD;
    }
    else if (selectedObject instanceof JRParameter)
    {
        list = sd.getParameters();
        objType = SubDatasetObjectChangedEvent.PARAMETER;
    }
    else if (selectedObject instanceof JRVariable)
    {
        list = sd.getVariables();
        objType = SubDatasetObjectChangedEvent.VARIABLE;
    }
    
    if (list != null)
    {
        int oldIndex = list.indexOf(selectedObject);
        if (oldIndex < list.size()-1 )
        {
            list.removeElement(selectedObject);
            list.insertElementAt(selectedObject, oldIndex+1);
        
            sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(
                        new SubDatasetObjectChangedEvent(
                            sd,
                            objType, 
                            SubDatasetObjectChangedEvent.ORDER_CHANGED,
                            selectedObject, selectedObject));
        }
        getJReportFrame().getReport().incrementReportChanges();
    }
}//GEN-LAST:event_jMenuItemMoveDownObjActionPerformed

private void jMenuItemMoveUpObjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMoveUpObjActionPerformed
    
    TreePath tp = jTreeDocument.getSelectionPath();
    if (tp == null) return;
    if (getJReportFrame() == null) return; // just in case
    
    
    Object selectedObject = ((DefaultMutableTreeNode)tp.getLastPathComponent()).getUserObject();
    SubDataset sd = Misc.getObjectSubDataset(getJReportFrame().getReport() , selectedObject);
    
    if (sd == null) return;
    
    Vector list = null;
    int objType = 0;
    
    if (selectedObject instanceof JRField)
    {
        list = sd.getFields();
        objType = SubDatasetObjectChangedEvent.FIELD;
    }
    else if (selectedObject instanceof JRParameter)
    {
        list = sd.getParameters();
        objType = SubDatasetObjectChangedEvent.PARAMETER;
    }
    else if (selectedObject instanceof JRVariable)
    {
        list = sd.getVariables();
        objType = SubDatasetObjectChangedEvent.VARIABLE;
    }
    
    if (list != null)
    {
        int oldIndex = list.indexOf(selectedObject);
        if (oldIndex > 0)
        {
            list.removeElement(selectedObject);
            list.insertElementAt(selectedObject, oldIndex-1);
        
            sd.fireSubDatasetObjectChangedListenerSubDatasetObjectChanged(
                        new SubDatasetObjectChangedEvent(
                            sd,
                            objType, 
                            SubDatasetObjectChangedEvent.ORDER_CHANGED,
                            selectedObject, selectedObject));
        }
        getJReportFrame().getReport().incrementReportChanges();
    }
    
    
}//GEN-LAST:event_jMenuItemMoveUpObjActionPerformed

private void jToggleButtonFilterBuiltinParametersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButtonFilterBuiltinParametersItemStateChanged
    MainFrame.getMainInstance().getProperties().setProperty("documentStructure.showBuiltinParameters",jToggleButtonFilterBuiltinParameters.isSelected() + "" );
        List openedPaths = getOpenedPaths();
        updateReportObjects(getJReportFrame(), openedPaths);
        updateSubDatasets(openedPaths);
        jTreeDocument.updateUI();
}//GEN-LAST:event_jToggleButtonFilterBuiltinParametersItemStateChanged

private void jToggleButtonFilterBuiltinVariablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonFilterBuiltinVariablesActionPerformed
    MainFrame.getMainInstance().getProperties().setProperty("documentStructure.showBuiltinVariables",jToggleButtonFilterBuiltinVariables.isSelected() + "" );
    List openedPaths = getOpenedPaths();
    updateReportObjects(getJReportFrame(), openedPaths);
    updateSubDatasets(openedPaths);
    jTreeDocument.updateUI();
}//GEN-LAST:event_jToggleButtonFilterBuiltinVariablesActionPerformed

    private void jToggleButtonFilterBuiltinParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonFilterBuiltinParametersActionPerformed
    
        
}//GEN-LAST:event_jToggleButtonFilterBuiltinParametersActionPerformed

private void jMenuItemAddGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddGroupActionPerformed
    
    SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                 ReportGroupWizard rgw = new ReportGroupWizard();
                 rgw.startWizard();
            }
    });
       
    
}//GEN-LAST:event_jMenuItemAddGroupActionPerformed

private void jPopupMenuFieldsPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenuFieldsPopupMenuWillBecomeVisible
    JReportFrame jrf = this.getJReportFrame();
        
        jMenuItemMoveUpObj.setVisible(false);
        jMenuItemMoveDownObj.setVisible(false);
        jSeparator5.setVisible(false);
        
        if (jrf != null)
        {
            jMenuAdd.setEnabled( true );
            jMenuItemRefresh.setEnabled( true );
        }
        else
        {
            jMenuItemCut.setEnabled(false);
            jMenuItemCopy.setEnabled(false);
            jMenuItemDelete.setEnabled(false);
            jMenuItemPaste.setEnabled(false);
            jMenuItemEdit.setEnabled(false);
            jMenuItemRefresh.setEnabled(false);
            jMenuAdd.setEnabled( false );
            return;
        }

        // Edit is able only if a single valid object is selected...
        TreePath[] paths = jTreeDocument.getSelectionPaths();

        if (paths == null || paths.length == 0)
        {
            jMenuItemCut.setEnabled(false);
            jMenuItemCopy.setEnabled(false);
            jMenuItemDelete.setEnabled(false);
            jMenuItemPaste.setEnabled(false);
            jMenuItemEdit.setEnabled(false);
            return;
        }

        if (paths.length == 1 &&
            paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode &&
            ( (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof JRField ) ||
              (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof JRVariable ) ||
              (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof JRParameter ) ||
              (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof SubDataset )) )
        {
                  jMenuItemEdit.setEnabled(true);
                  
                  Object obj = ((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject();
                  if ( (obj instanceof JRVariable && !((JRVariable)obj).isBuiltin()) ||
                       (obj instanceof JRField) ||
                       (obj instanceof JRParameter && !((JRParameter)obj).isBuiltin()) )
                  {
                      jSeparator5.setVisible(true);
                      jMenuItemMoveUpObj.setVisible(true);
                      jMenuItemMoveDownObj.setVisible(true);
                      jMenuItemMoveUpObj.setEnabled(false);
                      jMenuItemMoveDownObj.setEnabled(false);
                   
                      // Look what other childs there are here
                      DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
                      if (node.getNextSibling() != null)
                      {
                          jMenuItemMoveDownObj.setEnabled(true);
                      }
                      
                      if (node.getPreviousSibling() != null)
                      {
                          Object previous = ((DefaultMutableTreeNode)node.getPreviousSibling()).getUserObject();
                          
                          if ( (previous instanceof JRVariable && !((JRVariable)previous).isBuiltin()) ||
                               (previous instanceof JRField) ||
                               (previous instanceof JRParameter && !((JRParameter)previous).isBuiltin()) )
                          {
                              jMenuItemMoveUpObj.setEnabled(true);
                          }
                      }
                  }
        }
        else
        {
                  jMenuItemEdit.setEnabled( false );
        }

         if (paths.length > 0)
         {
             jMenuItemCut.setEnabled(true);
             jMenuItemCopy.setEnabled(true);
             jMenuItemDelete.setEnabled(true);
         }
         else
         {
             jMenuItemCut.setEnabled(false);
             jMenuItemCopy.setEnabled(false);
             jMenuItemDelete.setEnabled(false);
         }

        jMenuItemPaste.setEnabled(false);

         // If the only selection is the node "parameters"...
        if (paths.length == 1)
        {
           if (paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode)
           {
               DefaultMutableTreeNode dmn = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields")) || dmn.getUserObject() instanceof JRField) && jrf.getMainFrame().getValuesDialog().getClipboardFields().size() > 0)
               {
                   jMenuItemPaste.setEnabled(true);
               }
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables")) || dmn.getUserObject() instanceof JRVariable) && jrf.getMainFrame().getValuesDialog().getClipboardVariables().size() > 0)
               {
                   jMenuItemPaste.setEnabled(true);
               }
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters")) || dmn.getUserObject() instanceof JRParameter) && jrf.getMainFrame().getValuesDialog().getClipboardParameters().size() > 0)
               {
                   jMenuItemPaste.setEnabled(true);
               }
           }
        }
}//GEN-LAST:event_jPopupMenuFieldsPopupMenuWillBecomeVisible

private void jMenuItemRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRefreshActionPerformed
    updateReportObjects(getJReportFrame(),  getOpenedPaths());
}//GEN-LAST:event_jMenuItemRefreshActionPerformed

private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        TreePath[] paths = jTreeDocument.getSelectionPaths();

        for (int i=0; i<paths.length && jrf != null; ++i)
        {
            Report report = jrf.getReport();
            SubDataset sd = report;

            try {
            if (paths[i].getPath().length == 4)
            {
                Object osd = ((DefaultMutableTreeNode)paths[i].getPath()[1]).getUserObject();
                if (osd instanceof SubDataset)
                {
                    sd = (SubDataset)osd;
                }
            }
            if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode)
            {
                Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
                if (obj instanceof JRVariable ) { sd.removeVariable((JRVariable)obj ); }
                if (obj instanceof JRField ) {  sd.removeField((JRField)obj ); }
                if (obj instanceof JRParameter ) {  sd.removeParameter((JRParameter)obj ); }
                if (obj instanceof SubDataset ) {

                    if (JOptionPane.showConfirmDialog(jTreeDocument,
                            I18n.getFormattedString("messages.libraryPanel.removingSubdataset","Do you really want remove subDataset {0} ?", new Object[]{obj}) ) == JOptionPane.OK_OPTION)
                    {
                        jrf.getReport().removeSubDataset((SubDataset)obj);
                    }
                }
            }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_jMenuItemDeleteActionPerformed

private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        TreePath[] paths = jTreeDocument.getSelectionPaths();

        if (paths.length == 1)
        {
           if (paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode)
           {

                SubDataset sd = jrf.getReport();

                if (paths[0].getPath().length >= 3)
                {
                    Object osd = ((DefaultMutableTreeNode)paths[0].getPath()[1]).getUserObject();
                    if (osd instanceof SubDataset)
                    {
                        sd = (SubDataset)osd;
                    }
                }

               ValuesDialog vd = MainFrame.getMainInstance().getValuesDialog();
               DefaultMutableTreeNode dmn = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields")) || dmn.getUserObject() instanceof JRField) && jrf.getMainFrame().getValuesDialog().getClipboardFields().size() > 0)
               {
                   vd.getValuesPanel().duplicateFields( vd.getClipboardFields(), sd );
               }
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables")) || dmn.getUserObject() instanceof JRVariable) && jrf.getMainFrame().getValuesDialog().getClipboardVariables().size() > 0)
               {
                   vd.getValuesPanel().duplicateVariables( vd.getClipboardVariables(), sd );
               }
               if ((dmn.getUserObject().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters")) || dmn.getUserObject() instanceof JRParameter) && jrf.getMainFrame().getValuesDialog().getClipboardParameters().size() > 0)
               {
                   vd.getValuesPanel().duplicateParameters( vd.getClipboardParameters(), sd );
               }
           }
        }
}//GEN-LAST:event_jMenuItemPasteActionPerformed

private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
    JReportFrame jrf = this.getJReportFrame();
       if (jrf == null) return;
        
       TreePath[] paths = jTreeDocument.getSelectionPaths();

       boolean clearedVariablesClipboard = false;
       boolean clearedParametersClipboard = false;
       boolean clearedFieldsClipboard = false;

        for (int i=0; i<paths.length && jrf != null; ++i)
        {
            ValuesDialog vd = MainFrame.getMainInstance().getValuesDialog();
            Report report = jrf.getReport();
            if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode)
            {
                Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
                if (obj instanceof JRVariable )
                {
                        if (!clearedVariablesClipboard)
                        {
                            clearedVariablesClipboard = true;
                            vd.getClipboardVariables().removeAllElements();
                        }
                        vd.getClipboardVariables().add( ((JRVariable)obj).cloneMe() );
                }
                if (obj instanceof JRField )
                {
                        if (!clearedFieldsClipboard)
                        {
                            clearedFieldsClipboard = true;
                            vd.getClipboardFields().removeAllElements();
                        }
                        vd.getClipboardFields().add( ((JRField)obj).cloneMe() );
                }
                if (obj instanceof JRParameter )
                {
                        if (!clearedParametersClipboard)
                        {
                            clearedParametersClipboard = true;
                            vd.getClipboardParameters().removeAllElements();
                        }
                        vd.getClipboardParameters().add( ((JRParameter)obj).cloneMe() );
                }
            }
        }
}//GEN-LAST:event_jMenuItemCopyActionPerformed

private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
    JReportFrame jrf = this.getJReportFrame();
       if (jrf == null) return;
        
       TreePath[] paths = jTreeDocument.getSelectionPaths();

       boolean clearedVariablesClipboard = false;
       boolean clearedParametersClipboard = false;
       boolean clearedFieldsClipboard = false;

        for (int i=0; i<paths.length && jrf != null; ++i)
        {
            ValuesDialog vd = MainFrame.getMainInstance().getValuesDialog();
            Report report = jrf.getReport();

            SubDataset sd = report;

            if (paths[i].getPath().length == 4)
            {
                Object osd = ((DefaultMutableTreeNode)paths[i].getPath()[1]).getUserObject();
                if (osd instanceof SubDataset)
                {
                    sd = (SubDataset)osd;
                }
            }

            if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode)
            {
                Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
                if (obj instanceof JRVariable )
                {
                        if (!clearedVariablesClipboard)
                        {
                            clearedVariablesClipboard = true;
                            vd.getClipboardVariables().removeAllElements();
                        }
                        vd.getClipboardVariables().add( ((JRVariable)obj).cloneMe() );
                        sd.removeVariable((JRVariable)obj);
                }
                if (obj instanceof JRField )
                {
                        if (!clearedFieldsClipboard)
                        {
                            clearedFieldsClipboard = true;
                            vd.getClipboardFields().removeAllElements();
                        }
                        vd.getClipboardFields().add( ((JRField)obj).cloneMe() );
                        sd.removeField((JRField)obj);
                }
                if (obj instanceof JRParameter )
                {
                        if (!clearedParametersClipboard)
                        {
                            clearedParametersClipboard = true;
                            vd.getClipboardParameters().removeAllElements();
                        }
                        vd.getClipboardParameters().add( ((JRParameter)obj).cloneMe() );
                        sd.removeParameter((JRParameter)obj);
                }
            }
        }
}//GEN-LAST:event_jMenuItemCutActionPerformed

private void jMenuItemNewSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewSubDatasetActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        SubDatasetDialog sdd = new SubDatasetDialog(Misc.frameFromComponent(this),true);
        sdd.setVisible(true);
}//GEN-LAST:event_jMenuItemNewSubDatasetActionPerformed

private void jMenuItemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemParameterActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        MainFrame.getMainInstance().getValuesDialog().getValuesPanel().newParameter(getSelectedSubDataset());
}//GEN-LAST:event_jMenuItemParameterActionPerformed

private void jMenuItemNewVariableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewVariableActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        // We have to use the same method used by ValuesDialog...
        MainFrame.getMainInstance().getValuesDialog().getValuesPanel().newVariable(getSelectedSubDataset());
}//GEN-LAST:event_jMenuItemNewVariableActionPerformed

private void jMenuItemNewFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewFieldActionPerformed
     JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        MainFrame.getMainInstance().getValuesDialog().getValuesPanel().newField(getSelectedSubDataset());
}//GEN-LAST:event_jMenuItemNewFieldActionPerformed

private void jMenuItemEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditActionPerformed
    JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return;
        
        // We have to see what type of item we have selected...
        TreePath[] paths = jTreeDocument.getSelectionPaths();

        if (paths == null || paths.length == 0)
        {
            return;
        }

        if (paths.length == 1 && paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode)
        {
            Object obj = ((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject();
            if (obj instanceof JRVariable ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyVariable((JRVariable)obj, this.getSelectedSubDataset() ); }
            if (obj instanceof JRField ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyField((JRField)obj, this.getSelectedSubDataset() ); }
            if (obj instanceof JRParameter ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyParameter((JRParameter)obj, this.getSelectedSubDataset() ); }
            if (obj instanceof SubDataset ) {
                java.awt.Frame parent = Misc.frameFromComponent(this);
                SubDatasetDialog cd = new SubDatasetDialog(parent,true);
                cd.setSubDataset( (SubDataset)obj );
                cd.setVisible(true);
            }
        }
}//GEN-LAST:event_jMenuItemEditActionPerformed

    /**
     * We do a lot of assumptions here:
     * 1. the selected node is a field, a parameter, a variable, or a a subdataset, or has this classes
     * as ancestor
     * 2. Of course the selection must not be null and the report frame is not null...  
     */
    private void jMenuItemUngroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUngroupActionPerformed

        if (jTreeDocument.getSelectionCount() > 0) {
            DocumentStructureTreeNode elementNode = (DocumentStructureTreeNode)jTreeDocument.getSelectionPath().getLastPathComponent();
            if (elementNode.getUserObject() instanceof ElementGroup) {
                String elementGroupName = ((ElementGroup)elementNode.getUserObject()).getName();
                while ( ((DocumentStructureTreeNode)elementNode.getParent()).getUserObject() instanceof ElementGroup)
                {
                    elementNode = (DocumentStructureTreeNode)elementNode.getParent();
                    elementGroupName = ((ElementGroup)elementNode.getUserObject()).getName() + "." + elementGroupName;
                }

                String newElementGroupName = elementGroupName;
                if (newElementGroupName.lastIndexOf(".")>=0)
                {
                    newElementGroupName = newElementGroupName.substring(0,newElementGroupName.lastIndexOf("."));
                }
                else
                {
                    newElementGroupName = "";
                }

                /************************/
                UnGroupEmentsOperation undoOp = new UnGroupEmentsOperation(MainFrame.getMainInstance().getActiveReportFrame());
                Vector elements = MainFrame.getMainInstance().getActiveReportFrame().getReport().getElements();
                for (int i=0; i<elements.size(); ++i)
                {
                    ReportElement element = (ReportElement)elements.elementAt(i);
                    String oldElementGroupName = element.getElementGroup();
                    if (element.getElementGroup().startsWith(elementGroupName+".") || element.getElementGroup().equals(elementGroupName))
                    {
                         String tmpElementGroupName = element.getElementGroup().substring(elementGroupName.length());
                         if (tmpElementGroupName.length() == 0) tmpElementGroupName = newElementGroupName;
                         tmpElementGroupName = newElementGroupName + tmpElementGroupName;
                         if (tmpElementGroupName.startsWith(".")) tmpElementGroupName = tmpElementGroupName.substring(1);

                        element.setElementGroup(tmpElementGroupName);
                        undoOp.addElement(element, i,i, oldElementGroupName, tmpElementGroupName);
                    }
                }

                MainFrame.getMainInstance().getActiveReportFrame().addUndoOperation(undoOp);

                MainFrame.getMainInstance().getActiveReportFrame().fireReportListenerReportElementsChanged(new ReportElementChangedEvent(MainFrame.getMainInstance().getActiveReportFrame(), new Vector() , ReportElementChangedEvent.REMOVED ));
            }
        }
        // Get group name...


    }//GEN-LAST:event_jMenuItemUngroupActionPerformed

    private void jTreeDocumentTreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTreeDocumentTreeCollapsed
           //System.out.println("collapsing..." + evt.getPath());
    }//GEN-LAST:event_jTreeDocumentTreeCollapsed

    private void jTreeDocumentTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTreeDocumentTreeExpanded
           // System.out.println("expanding..." + evt.getPath());


    }//GEN-LAST:event_jTreeDocumentTreeExpanded

    private void jMenuItemDS_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_deleteActionPerformed
MainFrame.getMainInstance().getActiveReportFrame().deleteSelectedElements();
    }//GEN-LAST:event_jMenuItemDS_deleteActionPerformed

    private void jMenuItemDS_pasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_pasteActionPerformed
MainFrame.getMainInstance().getActiveReportFrame().paste();
    }//GEN-LAST:event_jMenuItemDS_pasteActionPerformed

    private void jMenuItemDS_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_copyActionPerformed
MainFrame.getMainInstance().getActiveReportFrame().copy();
    }//GEN-LAST:event_jMenuItemDS_copyActionPerformed

    private void jMenuItemDS_cutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_cutActionPerformed
MainFrame.getMainInstance().getActiveReportFrame().cut();
    }//GEN-LAST:event_jMenuItemDS_cutActionPerformed

    private void jMenuItemMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMoveDownActionPerformed
    if (MainFrame.getMainInstance().getActiveReportFrame() != null) {
            JReportFrame jrf = MainFrame.getMainInstance().getActiveReportFrame();
            jrf.moveDown();
            this.jTreeDocument.updateUI();
            if (jrf.getSelectedElements().size()>0)
                updateDocumentStructureTree(jrf);

        }
    }//GEN-LAST:event_jMenuItemMoveDownActionPerformed

    private void jMenuItemMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMoveUpActionPerformed
 if (MainFrame.getMainInstance().getActiveReportFrame() != null) {
         JReportFrame jrf = MainFrame.getMainInstance().getActiveReportFrame();
         jrf.moveUp();
         this.jTreeDocument.updateUI();
         
         if (jrf.getSelectedElements().size()>0)
             updateDocumentStructureTree(jrf);
        }

    }//GEN-LAST:event_jMenuItemMoveUpActionPerformed

    private void jMenuItemDS_chartPropertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_chartPropertiesActionPerformed
 if (MainFrame.getMainInstance().getActiveReportFrame() != null) {
                JReportFrame jrf = MainFrame.getMainInstance().getActiveReportFrame();
                if (jTreeDocument.getSelectionCount() > 0) {
                    DocumentStructureTreeNode elementNode = (DocumentStructureTreeNode)jTreeDocument.getSelectionPath().getLastPathComponent();
                    if (elementNode.getUserObject() instanceof ChartReportElement2 )
                    {
                        ChartPropertiesDialog cpd = new ChartPropertiesDialog(MainFrame.getMainInstance(),true);
                        cpd.setChartElement( (ChartReportElement2)elementNode.getUserObject());
                        cpd.setVisible(true);
                    }

                }
        }
    }//GEN-LAST:event_jMenuItemDS_chartPropertiesActionPerformed

    public void languageChanged(LanguageChangedEvent evt) {

    this.applyI18n();
}


    private void jMenuItemDS_propertiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDS_propertiesActionPerformed
      MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
      MainFrame.getMainInstance().getElementPropertiesDialog().updateSelection();
    }//GEN-LAST:event_jMenuItemDS_propertiesActionPerformed

    private void jTreeDocumentValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeDocumentValueChanged
          if (isDontHandleEvent()) return;

          
                // Get active jrf...
                if (MainFrame.getMainInstance().getActiveReportFrame() != null) {
                    //dontHandleEvent = true;
                    try {
                        JReportFrame jrf = MainFrame.getMainInstance().getActiveReportFrame();
                        Vector elementsToSelect = new Vector();
                        Vector bandsToSelect = new Vector();
                        Vector objectsToSelect = new Vector();

                        TreePath[] path = jTreeDocument.getSelectionPaths();

                        //jrf.setSelectedBands(new Vector());
                        
                                  
                        
                        for (int i=0; path != null && i<path.length; ++i)
                        {
                            if ( !(path[i].getLastPathComponent() instanceof ReportObjectTreeNode))
                            {
                                continue;
                            }
                            ReportObjectTreeNode elementNode = (ReportObjectTreeNode)path[i].getLastPathComponent();
                            
                            if (elementNode.getUserObject() instanceof Band) {
                                bandsToSelect.addElement( (Band)elementNode.getUserObject());
                            }
                            
                            if (elementNode.getUserObject() instanceof JRParameter ||
                                elementNode.getUserObject() instanceof JRField ||
                                elementNode.getUserObject() instanceof JRVariable
                                //elementNode.getUserObject() instanceof SubDataset
                                )
                            {
                                objectsToSelect.addElement(elementNode.getUserObject());
                            }
                            
                            if (elementNode.getUserObject() instanceof ReportElement) {
                                elementsToSelect.addElement( (ReportElement)elementNode.getUserObject());
                            }
                            
                        }
                        
//                        System.out.println()
                                               
//                        for (int i=0; i<path.length; ++i) {
//                            
//                            /*
//                            if ( !(path[i].getLastPathComponent() instanceof ReportObjectTreeNode)) 
//                            {
//                                jrf.setSelectedObjects(objectsToSelect);
//                                jrf.setSelectedBands(bandsToSelect);
//                                jrf.setSelectedElement(null);
//                                return;
//                            }
//                            */
//                            
//                            ReportObjectTreeNode elementNode = (ReportObjectTreeNode)path[i].getLastPathComponent();
//                            if (elementNode.getUserObject() instanceof ReportElement) {
//                                elementsToSelect.addElement( (ReportElement)elementNode.getUserObject());
//                            }
//                            else if (path.length == 1) {
//                                
//                                jrf.setSelectedObjects(objectsToSelect);
//                                jrf.setSelectedBands(bandsToSelect);
//                                jrf.setSelectedElement(null);
//                                
//                                return;
//                            }
//                            else if (elementNode.getUserObject() instanceof ElementGroup)
//                            {
//                                jrf.setSelectedObjects(objectsToSelect);
//                                jrf.setSelectedBands(bandsToSelect);
//                                jrf.setSelectedElement(null);
//                                return;
//                            }
//                        }
  
                        jrf.getSelectedElements().clear();
                        Enumeration e = elementsToSelect.elements();
                        while (e.hasMoreElements()) {
                            jrf.addSelectedElement((ReportElement)e.nextElement(), false);
                        }
                        
                        if (elementsToSelect.size() > 0)
                        {
                            jrf.setSelectedBands(new Vector());
                            jrf.setSelectedObjects(new Vector());
                        }
                        else
                        {
                            jrf.setSelectedObjects(objectsToSelect);
                            jrf.setSelectedBands(bandsToSelect);
                        }
                        
                        jrf.fireSelectionChangedEvent();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    //
                }
    }//GEN-LAST:event_jTreeDocumentValueChanged

    private void jTreeDocumentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeDocumentMousePressed
 if (evt.getButton() == evt.BUTTON3) {
            if (MainFrame.getMainInstance().getActiveReportFrame() != null) {
                JReportFrame jrf = MainFrame.getMainInstance().getActiveReportFrame();
                Misc.ensurePathIsSelected(jTreeDocument.getPathForLocation(evt.getX(), evt.getY() ), jTreeDocument);
                //jTreeDocument.setSelectionPath( jTreeDocument.getPathForLocation(evt.getX(), evt.getY() ) );
                if (jTreeDocument.getSelectionCount() > 0 &&
                    jTreeDocument.getSelectionPath().getLastPathComponent() instanceof DocumentStructureTreeNode &&
                    jTreeDocument.getModel().getRoot() != jTreeDocument.getSelectionPath().getLastPathComponent()) {
                    DocumentStructureTreeNode elementNode = (DocumentStructureTreeNode)jTreeDocument.getSelectionPath().getLastPathComponent();
                    if (elementNode.getUserObject() instanceof ReportElement) {
                        // (ReportElement)elementNode.getUserObject());
                        jMenuItemDS_chartProperties.setVisible( elementNode.getUserObject() instanceof ChartReportElement2  );
                        this.jPopupMenuDocumentStructure.show(jTreeDocument, evt.getPoint().x, evt.getPoint().y);

                    } else if (elementNode.getUserObject() instanceof Band) {
                        jPopupMenuFields.show( jTreeDocument, evt.getPoint().x, evt.getPoint().y);
                    } else if (elementNode.getUserObject() instanceof ElementGroup) {
                        // (ReportElement)elementNode.getUserObject());
                        this.jPopupMenuDocumentStructureGroup.show(jTreeDocument, evt.getPoint().x, evt.getPoint().y);

                    } 
                }
                else
                {
                    jPopupMenuFields.show( jTreeDocument, evt.getPoint().x, evt.getPoint().y);
                }
            }

        }
        else if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2) {

            // We have to see what type of item we have selected...
            TreePath[] paths = jTreeDocument.getSelectionPaths();

            if (getJReportFrame() == null || paths == null || paths.length == 0)
            {
                return;
            }
            
            if (paths.length == 1 && paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode)
            {
                Object obj = ((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject();
                if (obj instanceof JRVariable ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyVariable((JRVariable)obj, this.getSelectedSubDataset() ); }
                if (obj instanceof JRField ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyField((JRField)obj, this.getSelectedSubDataset() ); }
                if (obj instanceof JRParameter ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyParameter((JRParameter)obj, this.getSelectedSubDataset() ); }
                if (obj instanceof SubDataset ) {
                    java.awt.Frame parent = Misc.frameFromComponent(this);
                    SubDatasetDialog cd = new SubDatasetDialog(parent,true);
                    cd.setSubDataset( (SubDataset)obj );
                    cd.setVisible(true);
                }
            }
            
            JReportFrame jrf = getJReportFrame();
            if (jrf.getSelectedElements().size() > 0) {
                MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
            }
        }
    }//GEN-LAST:event_jTreeDocumentMousePressed

    public void printSelectedPaths(String prefix)
    {
        try {
                       Enumeration enum_extended_paths = jTreeDocument.getExpandedDescendants(new TreePath(new Object[]{ jTreeDocument.getModel().getRoot() }));
                       if (enum_extended_paths != null)
                       {
                                  while (enum_extended_paths.hasMoreElements())
                                  {
                                   TreePath path = (TreePath)enum_extended_paths.nextElement();
                                   //System.out.println(prefix+ " "+path);
                               }
                       }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelFilters;
    private javax.swing.JMenu jMenuAdd;
    private javax.swing.JMenuItem jMenuItemAddGroup;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDS_chartProperties;
    private javax.swing.JMenuItem jMenuItemDS_copy;
    private javax.swing.JMenuItem jMenuItemDS_cut;
    private javax.swing.JMenuItem jMenuItemDS_delete;
    private javax.swing.JMenuItem jMenuItemDS_paste;
    private javax.swing.JMenuItem jMenuItemDS_properties;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemEdit;
    private javax.swing.JMenuItem jMenuItemMoveDown;
    private javax.swing.JMenuItem jMenuItemMoveDownObj;
    private javax.swing.JMenuItem jMenuItemMoveUp;
    private javax.swing.JMenuItem jMenuItemMoveUpObj;
    private javax.swing.JMenuItem jMenuItemNewField;
    private javax.swing.JMenuItem jMenuItemNewSubDataset;
    private javax.swing.JMenuItem jMenuItemNewVariable;
    private javax.swing.JMenuItem jMenuItemParameter;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemRefresh;
    private javax.swing.JMenuItem jMenuItemUngroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenuDocumentStructure;
    private javax.swing.JPopupMenu jPopupMenuDocumentStructureGroup;
    private javax.swing.JPopupMenu jPopupMenuFields;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToggleButton jToggleButtonFilterBuiltinParameters;
    private javax.swing.JToggleButton jToggleButtonFilterBuiltinVariables;
    private javax.swing.JTree jTreeDocument;
    // End of variables declaration//GEN-END:variables

    
    /** 
     *  @return a list of node ids of the current selected nodes. 
     */
    private List getOpenedPaths()
    {
        List openedPaths = null;
        Object root = jTreeDocument.getModel().getRoot();
        jTreeDocument.updateUI();

        try {
                openedPaths = new ArrayList();
                Enumeration enum_extended_paths = jTreeDocument.getExpandedDescendants(new TreePath(new Object[]{ jTreeDocument.getModel().getRoot() }));
                if (enum_extended_paths!=null)
                {
                    while (enum_extended_paths.hasMoreElements())
                    {
                        TreePath path = (TreePath)enum_extended_paths.nextElement();
                     
                        if (!path.getPath()[0].equals(root))
                        {
                            continue;
                        }
                        
                        if (path.getLastPathComponent() instanceof ReportObjectTreeNode)
                        {
                            // Check parent integrity (sometimes the integrity in not garanteed...)
                            Object[] objects = path.getPath();
                            for (int i=0; i<objects.length-1; ++i)
                            {
                                ((ReportObjectTreeNode)objects[i+1]).setParent( (ReportObjectTreeNode)objects[i] );
                            }
                            
                            String nodePath = ((ReportObjectTreeNode)path.getLastPathComponent()).getNodeId();
                            
                            if (!openedPaths.contains(nodePath))
                            {
                                openedPaths.add(nodePath);
                            }
                        }
                    }
                }
           } catch (Exception ex)
           {
                ex.printStackTrace();
           }
        return openedPaths;
    }

    public void updateDocumentStructureTree()
    {
        updateDocumentStructureTree(getJReportFrame());
    }
    
    
    public void updateDocumentStructureTree(JReportFrame jrf)
    {
        List openedPaths = getOpenedPaths();

        if (jrf != oldJReportFrame)
        {
            if (oldJReportFrame != null) oldJReportFrame.setOpenedNodesDocumentStructure( openedPaths );
            if (jrf != null) openedPaths = jrf.getOpenedNodesDocumentStructure();
        }

        setDontHandleEvent(true);
        // Update the document tree structure...
        ((DocumentStructureTreeNode)this.jTreeDocument.getModel().getRoot()).removeAllChildren();
        ((javax.swing.tree.DefaultTreeModel)(this.jTreeDocument.getModel())).reload();

        if (jrf != null)
        {

            updateReportObjects(jrf, openedPaths);
            updateSubDatasets(openedPaths);

            Enumeration bands = jrf.getReport().getBands().elements();
            while (bands.hasMoreElements())
            {
                Band band = (Band)bands.nextElement();
                DocumentStructureTreeNode bandNode = new DocumentStructureTreeNode(band);
                ((DocumentStructureTreeNode)this.jTreeDocument.getModel().getRoot()).add( bandNode);

                Enumeration elements = jrf.getReport().getElements().elements();
                while (elements.hasMoreElements())
                {
                    ReportElement element = (ReportElement)elements.nextElement();
                    if (element.getBand() == band)
                    {
                        String elementGroup = element.getElementGroup();
                        try {
                            if (element.getParentElement() != null)
                            {
                                DocumentStructureTreeNode parentNode = findElementTreeNode(element.getParentElement(),true);
                                if (parentNode != null)
                                {
                                    addElementToGroup(parentNode, elementGroup, element, openedPaths);
                                    
                                    
                                    if (openedPaths != null && openedPaths.contains(parentNode.getNodeId()))
                                    {
                                        this.expandPath(parentNode);
                                    }
                                    
                                }

                            }
                            else
                            {
                                addElementToGroup(bandNode, elementGroup, element, openedPaths);
                            }

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
                if (openedPaths != null && openedPaths.contains(bandNode.getNodeId()))
                {
                    this.expandPath(bandNode);
                }
            }
                        
            setDontHandleEvent(false);
            this.reportElementsSelectionChanged(new ReportElementsSelectionEvent(jrf,  jrf.getSelectedElements()));
        }

        oldJReportFrame = jrf;

        jTreeDocument.updateUI();
    }

    public void addElementToGroup(DocumentStructureTreeNode parentNode, String subGroup, ReportElement element, List openedPaths)
    {
        // 1. We have to find the group node...
        if (subGroup.equals(""))
        {
            DocumentStructureTreeNode newNode = new DocumentStructureTreeNode(element);
            parentNode.add( newNode);

            if (openedPaths != null && openedPaths.contains(newNode.getNodeId()))
            {
                expandPath( (DocumentStructureTreeNode)newNode);
            }

            return;
        }
        String levelGroupName = "";
        if (subGroup.indexOf(".")>=0)
        {
            levelGroupName = subGroup.substring(0, subGroup.indexOf("."));
            subGroup = subGroup.substring(subGroup.indexOf(".")+1);
        }
        else
        {
            levelGroupName = subGroup;
            subGroup = "";
        }

        // 2. Look for the node named levelGroupName
        for (int i =0; i<parentNode.getChildCount(); ++i)
        {
            DocumentStructureTreeNode dmtn = (DocumentStructureTreeNode)parentNode.getChildAt(i);
            if (dmtn.getUserObject() != null && dmtn.getUserObject() instanceof ElementGroup)
            {
                ElementGroup ge = (ElementGroup)dmtn.getUserObject();
                if (ge.getName().equals( levelGroupName ))
                {
                    addElementToGroup(dmtn, subGroup, element, openedPaths);
                    return;
                }
            }
        }

        // Node doesn't exists....
        DocumentStructureTreeNode dmtn = new DocumentStructureTreeNode(new ElementGroup(levelGroupName));
        parentNode.add( dmtn );
        addElementToGroup(dmtn, subGroup, element, openedPaths);

        if (openedPaths != null && openedPaths.contains(dmtn.getNodeId()))
        {
            expandPath( (DocumentStructureTreeNode)dmtn);
        }


    }

    public void expandPath(ReportObjectTreeNode node)
    {
       //if (node.getChildCount() == 0) return;
        try {
            if (node.getParent() != null)
            {
                expandPath((ReportObjectTreeNode)node.getParent());
            }
            jTreeDocument.expandPath(new TreePath(((ReportObjectTreeNode)node).getPath() ));
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
     }


    public void reportBandChanged(ReportBandChangedEvent evt) {
        this.updateDocumentStructureTree( evt.getJReportFrame());
    }

    public void reportElementsChanged(ReportElementChangedEvent evt) {

        if (evt.getType() == ReportElementChangedEvent.CHANGED) {
            for (int ir=0; ir<evt.getElements().size(); ++ir)
            {
                ReportElement re = (ReportElement)evt.getElements().elementAt(ir);

                DocumentStructureTreeNode node = findElementTreeNode(re, true);
                if (node == null) {
                    // This node has changed your band....
                    // Find the node based on objects availables....
                    node = findElementTreeNode(re, false);
                    if (node == null) return; // This should not happen....
                    // Remove from your parent....
                    node.removeFromParent();
                    // Add to the new parent...
                    DocumentStructureTreeNode root = (DocumentStructureTreeNode)jTreeDocument.getModel().getRoot();
                    for (int i=0; i<root.getChildCount(); ++i) {
                        if (!(root.getChildAt(i) instanceof DocumentStructureTreeNode)) continue;
                        DocumentStructureTreeNode bandNode = (DocumentStructureTreeNode)root.getChildAt(i);
                        if (bandNode.getUserObject() == re.band) {
                            bandNode.add( node );
                            setDontHandleEvent(true);
                            if (evt.getJReportFrame().getSelectedElements().contains( re )) {
                                jTreeDocument.getSelectionModel().addSelectionPath(new TreePath(new Object[]
                                {root, bandNode,node}));
                            }
                            setDontHandleEvent(false);
                            break;
                        }
                    }
                }
            }
            jTreeDocument.updateUI();
        }
        else if (evt.getType() == ReportElementChangedEvent.REMOVED) {

            /*
            for (int i=0; i<evt.getElements().size(); ++i)
            {
                ReportElement re = (ReportElement)evt.getElements().elementAt(i);
                // find this element....
                DocumentStructureTreeNode node = findElementTreeNode(re,true);
                if (node != null) {
                    node.removeFromParent();
                }
            }
            */
            updateDocumentStructureTree( MainFrame.getMainInstance().getActiveReportFrame() );
            jTreeDocument.updateUI();
        }
        else if (evt.getType() == ReportElementChangedEvent.ADDED) {

            updateDocumentStructureTree( MainFrame.getMainInstance().getActiveReportFrame() );

            jTreeDocument.updateUI();
        }


    }//end reportElementsChanged



    public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt) {

        if (evt.getCrosstabReportElement() != null) return;
        
        if (isDontHandleEvent()) return;
        setDontHandleEvent(true);

        // ((DocumentStructureTreeNode) path[0].getLastPathComponent()) == jTreeDocument.getModel().getRoot()
            
        TreePath[] path = jTreeDocument.getSelectionPaths();
        if (path != null && evt.getSelectedElements() != null && evt.getSelectedElements().size() == 0 &&
         (  !(path[0].getLastPathComponent() instanceof DocumentStructureTreeNode)
            || ((DocumentStructureTreeNode) path[0].getLastPathComponent()).getUserObject() instanceof Band
            || ((DocumentStructureTreeNode) path[0].getLastPathComponent()).getUserObject() instanceof ElementGroup
            || ((DocumentStructureTreeNode) path[0].getLastPathComponent()) == jTreeDocument.getModel().getRoot()) )
        {   
                setDontHandleEvent(false);
                return;
        }

        jTreeDocument.getSelectionModel().clearSelection();


        java.util.Vector v = evt.getSelectedElements();
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            ReportElement re = (ReportElement)e.nextElement();
            // looking for it in the tree...
            //1. Search the band...
            DocumentStructureTreeNode enode = findElementTreeNode( re, true);
            //TreePath treePath = findReportElement((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot(), re);
            if (enode != null)
            {
                jTreeDocument.getSelectionModel().addSelectionPath(new TreePath(enode.getPath()));
            }

        }
        
        
        
        if (v.size() == 0)
        {
            v = MainFrame.getMainInstance().getActiveReportFrame().getSelectedBands();
            
            e = v.elements();
            while (e.hasMoreElements()) {
                Band band = (Band)e.nextElement();
                // looking for it in the tree...
                //1. Search the band...
                DocumentStructureTreeNode enode = findBandTreeNode( band );
                if (enode != null)
                {
                    jTreeDocument.getSelectionModel().addSelectionPath(new TreePath(enode.getPath()));
                }

            }
        }
        
        setDontHandleEvent(false);

    }

    /*
    private TreePath findReportElement(DocumentStructureTreeNode root, ReportElement re)
    {
            List nodes = new ArrayList();
            nodes.add(root);
            for (int i=0; i<root.getChildCount(); ++i) {
                DocumentStructureTreeNode bandNode = (DocumentStructureTreeNode)root.getChildAt(i);
                if (bandNode.getUserObject() == re.band) {
                    String elementGroup = re.getElementGroup();
                    nodes.add(bandNode);
                    while ( elementGroup.length() > 0)
                    {
                        String localGroup = "";
                        if (elementGroup.indexOf(".") > 0)
                        {
                            localGroup = elementGroup.substring(0, elementGroup.indexOf("."));
                            elementGroup = elementGroup.substring(elementGroup.indexOf(".")+1);
                        }
                        else
                        {
                            localGroup = elementGroup;
                            elementGroup = "";
                        }

                        boolean found = false;
                        for (int k=0; k<bandNode.getChildCount(); ++k)
                        {
                            DocumentStructureTreeNode childNode = (DocumentStructureTreeNode)bandNode.getChildAt(k);
                            if (childNode.getUserObject() instanceof ElementGroup &&
                                ((ElementGroup)childNode.getUserObject()).getName().equals(localGroup)) {

                                nodes.add( childNode );
                                bandNode = childNode;
                                found = true;
                                break;
                            }
                        }


                    }

                    for (int k=0; k<bandNode.getChildCount(); ++k) {
                        DocumentStructureTreeNode child = (DocumentStructureTreeNode)bandNode.getChildAt(k);
                        if (child.getUserObject() == re) {
                            nodes.add(child);

                            return new TreePath(nodes.toArray());
                        }
                    }
                }
            }
            return null;
    }
     */

    /**
     *     If searchInYourBandOnly == true
     *     This method Look for the userObject re in elements contained in the same band
     *     of the element. This method MUST CHECK that the report IS in the band
     *     assigned. If not it must return null. The reason is that if the element
     *     is not found in your band, it has changed the band attribute and must be updated.
     * @param re 
     * @param searchInYourBandOnly 
     * @return the node representing re
     */
    public DocumentStructureTreeNode findElementTreeNode( ReportElement re, boolean searchInYourBandOnly) {
        // looking for it in the tree...
        //1. Search the band...
        DocumentStructureTreeNode root = (DocumentStructureTreeNode)jTreeDocument.getModel().getRoot();
        for (int i=0; i<root.getChildCount(); ++i) {
            if (root.getChildAt(i) instanceof DocumentStructureTreeNode)
            {
                DocumentStructureTreeNode bandNode = (DocumentStructureTreeNode)root.getChildAt(i);
                if (!searchInYourBandOnly || bandNode.getUserObject() == re.band) {

                DocumentStructureTreeNode newchild = findElementInTreeNode(bandNode,re);
                if (newchild != null) return newchild;
                }    
            }
                
        }
        return null;
    }
    
    public DocumentStructureTreeNode findBandTreeNode( Band band) {
        // looking for it in the tree...
        //1. Search the band...
        DocumentStructureTreeNode root = (DocumentStructureTreeNode)jTreeDocument.getModel().getRoot();
        for (int i=0; i<root.getChildCount(); ++i) {
            if (root.getChildAt(i) instanceof DocumentStructureTreeNode)
            {
                DocumentStructureTreeNode bandNode = (DocumentStructureTreeNode)root.getChildAt(i);
                if (bandNode.getUserObject() == band) {

                    return bandNode;
                }
            }
        }
        return null;
    }

    public DocumentStructureTreeNode findElementInTreeNode(DocumentStructureTreeNode parentNode, ReportElement re)
    {
        for (int k=0; k<parentNode.getChildCount(); ++k) {
            DocumentStructureTreeNode child = (DocumentStructureTreeNode)parentNode.getChildAt(k);
            if (child.getUserObject() == re) {
                return child;
            }
            if (!re.getElementGroup().equals("") || re.getParentElement() != null)
            {
                if (child.getUserObject() instanceof ElementGroup ||
                    child.getUserObject() instanceof FrameReportElement)
                {
                    DocumentStructureTreeNode newchild = findElementInTreeNode(child,re);
                    if (newchild != null) return newchild;
                }
            }
        }
        return null;
    }

    public boolean isDontHandleEvent() {
        return dontHandleEvent;
    }

    public void setDontHandleEvent(boolean dontHandleEvent) {
        this.dontHandleEvent = dontHandleEvent;
    }

    public void applyI18n(){

                // Start autogenerated code ----------------------
                jMenuItemDS_chartProperties.setText(I18n.getString("documentStructurePanel.menuItemDS_chartProperties","Chart properties"));
                jMenuItemUngroup.setText(I18n.getString("documentStructurePanel.menuItemUngroup","Ungroup"));
                // End autogenerated code ----------------------
        jMenuItemDS_cut.setText(it.businesslogic.ireport.util.I18n.getString("cut","Cut"));
        jMenuItemDS_copy.setText(it.businesslogic.ireport.util.I18n.getString("copy","Copy"));
        jMenuItemDS_paste.setText(it.businesslogic.ireport.util.I18n.getString("paste","Paste"));
        jMenuItemDS_delete.setText(it.businesslogic.ireport.util.I18n.getString("delete","Delete"));
        jMenuItemMoveUp.setText(it.businesslogic.ireport.util.I18n.getString("moveUp","Move up"));
        jMenuItemMoveDown.setText(it.businesslogic.ireport.util.I18n.getString("moveDown","Move down"));
       // jMenuItemDSB_properties.setText(it.businesslogic.ireport.util.I18n.getString("bandProperties","Band properties"));
        jMenuItemDS_properties.setText(it.businesslogic.ireport.util.I18n.getString("properties","Properties"));
        jMenuItemDS_cut.setText(it.businesslogic.ireport.util.I18n.getString("cut","Cut"));
        jMenuItemDS_copy.setText(it.businesslogic.ireport.util.I18n.getString("copy","Copy"));
        jMenuItemDS_paste.setText(it.businesslogic.ireport.util.I18n.getString("paste","Paste"));
        jMenuItemDS_delete.setText(it.businesslogic.ireport.util.I18n.getString("delete","Delete"));
        jMenuItemMoveUp.setText(it.businesslogic.ireport.util.I18n.getString("moveUp","Move up"));
        jMenuItemMoveDown.setText(it.businesslogic.ireport.util.I18n.getString("moveDown","Move down"));
        jMenuItemDS_properties.setText(it.businesslogic.ireport.util.I18n.getString("properties","Properties"));

        ((DefaultMutableTreeNode)this.jTreeDocument.getModel().getRoot()).setUserObject( it.businesslogic.ireport.util.I18n.getString("document","Document"));
        fieldsNode.setUserObject(  it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields"));
        variablesNode.setUserObject(  it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables"));
        parametersNode.setUserObject(  it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters"));
        
        jMenuItemAddGroup.setText(it.businesslogic.ireport.util.I18n.getString("newReportGroupWizard","New report group wizard"));
        
        jLabelFilters.setText(it.businesslogic.ireport.util.I18n.getString("documentStructurePanel.filters","Filters:"));
        
        jMenuItemMoveUpObj.setText(it.businesslogic.ireport.util.I18n.getString("moveUp","Move up"));
        jMenuItemMoveDownObj.setText(it.businesslogic.ireport.util.I18n.getString("moveDown","Move down"));
        
        jToggleButtonFilterBuiltinParameters.setToolTipText(it.businesslogic.ireport.util.I18n.getString("documentStructurePanel.tooltipParametersFilter","Show / Hide Built-in Parameters"));
        jToggleButtonFilterBuiltinVariables.setToolTipText(it.businesslogic.ireport.util.I18n.getString("documentStructurePanel.tooltipVariablesFilter","Show / Hide Built-in Variables"));
        
        jTreeDocument.updateUI();
    }

    public void setCutCopyEnabled(boolean enabled) {
        this.jMenuItemDS_cut.setEnabled(enabled);
        this.jMenuItemDS_copy.setEnabled(enabled);
        this.jMenuItemDS_delete.setEnabled(enabled);
    }

    public void setPasteEnebled(boolean enabled) {
        this.jMenuItemDS_paste.setEnabled(enabled);
    }

    public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt) {
    }
    
    public void setSelectedObject( Object obj)
    {
        DefaultMutableTreeNode mtd = Misc.findNodeWithUserObject( obj, (TreeNode)this.jTreeDocument.getModel().getRoot() );
    
        if (mtd != null)
        {
            TreePath path = new TreePath( mtd.getPath() );
            jTreeDocument.setSelectionPath( path );
            jTreeDocument.scrollPathToVisible( path );
        }
    }
    
    
    public void updateReportObjects(JReportFrame jrf, List openedNodeIds)
    {
       this.fieldsNode.removeAllChildren();
       this.parametersNode.removeAllChildren();
       this.variablesNode.removeAllChildren();

       if (jrf == null) {
           return;
       }

       java.util.Enumeration e = jrf.getReport().getFields().elements();
       
       // Add the nodes if they are not already there...
       
       if (((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).getIndex(parametersNode)  == -1) 
       {
           ((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).add( parametersNode );
       }
       
       if (((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).getIndex(fieldsNode)  == -1) 
       {
           ((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).add( fieldsNode );
       }
       
       if (((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).getIndex(variablesNode)  == -1) 
       {
           ((DocumentStructureTreeNode)jTreeDocument.getModel().getRoot()).add( variablesNode );
       }
       
       while (e.hasMoreElements())
       {
             fieldsNode.add(new ReportObjectTreeNode(e.nextElement()));
       }
       
       if (openedNodeIds != null && openedNodeIds.contains(fieldsNode.getNodeId()))
       {
            this.expandPath(fieldsNode);
       }
       
        e = jrf.getReport().getParameters().elements();
        
       boolean showBuiltInParams = MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinParameters","true").equals("true");
       while (e.hasMoreElements())
       {
             JRParameter param = (JRParameter)e.nextElement();
             if (param.isBuiltin() && !showBuiltInParams)
             {
                 continue;
             }
             parametersNode.add(new ReportObjectTreeNode(param));
       }
       if (openedNodeIds != null && openedNodeIds.contains(parametersNode.getNodeId()))
       {
            this.expandPath(parametersNode);
       }
        

        e = jrf.getReport().getVariables().elements();
        
       boolean showBuiltInPVars = MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinVariables","true").equals("true");
       while (e.hasMoreElements())
       {
             JRVariable var = (JRVariable)e.nextElement();
             if (var.isBuiltin() && !showBuiltInPVars)
             {
                 continue;
             }
             variablesNode.add(new ReportObjectTreeNode(var));
       }
       if (openedNodeIds != null && openedNodeIds.contains(variablesNode.getNodeId()))
       {
            this.expandPath(variablesNode);
       }
    }

    
    /**
     *   return the dataset to wich refer the current selection in the tree
     **/
    private SubDataset getSelectedSubDataset()
    {
        JReportFrame jrf = this.getJReportFrame();
        if (jrf == null) return null;

        SubDataset sd = jrf.getReport();
        TreePath[] paths = jTreeDocument.getSelectionPaths();
        if (paths != null && paths.length == 1)
        {
           if (paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode)
           {
                if (paths[0].getPath().length >= 2)
                {
                    Object osd = ((DefaultMutableTreeNode)paths[0].getPath()[1]).getUserObject();
                    if (osd instanceof SubDataset)
                    {
                        sd = (SubDataset)osd;
                    }
                }
           }
        }
        
        return sd;
    }
    
    
    
   /**
    * Implements the  SubDatasetObjectChangedListener 
    * @param evt 
    */ 
    public void subDatasetObjectChanged(SubDatasetObjectChangedEvent evt)
     {
         if (evt != null && evt.getAction() == evt.MODIFIED)
         {
                jTreeDocument.updateUI();
                return;
         }
         
         // Get selected elements...
         java.util.List list = getSelectedObjects();
         
         if (evt != null && evt.getSource() instanceof Report)
         {
            this.updateReportObjects(getJReportFrame(), getOpenedPaths());
         }
         else
         {
             updateSubDatasetNode( evt.getSource(), getOpenedPaths() );
         }
         
         addObjectsToSelection(list);
         
         jTreeDocument.updateUI();
         if (evt.getAction() == evt.DELETED || evt.getAction() == evt.ORDER_CHANGED)
         {
           // Remove ghost selections...
           TreePath[] paths = jTreeDocument.getSelectionPaths();
           java.util.List pp = new java.util.ArrayList();
           for (int i=0; paths != null && i<paths.length; ++i)
           {
               Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
               
               
               DefaultMutableTreeNode uNode = Misc.findNodeWithUserObject(obj, (TreeNode)this.jTreeDocument.getModel().getRoot());
               
               if (uNode == null) pp.add(paths[i]);
               else
               {
                    // Check the entire path....
                   TreeNode[] realPath = ((DefaultTreeModel)jTreeDocument.getModel()).getPathToRoot(uNode);
                   Object[] assumedPath = paths[i].getPath();
               
                   if (realPath == null || realPath.length != assumedPath.length) pp.add(paths[i]);
                   for (int k=0; k<realPath.length; ++k)
                   {
                       if (realPath[k] != assumedPath[k])
                       {
                           pp.add(paths[i]);
                           break;
                       }
                   }
               }
           }
           
           if (pp.size() > 0)
           {
               TreePath[] delPaths = new TreePath[pp.size()];
               for (int i=0; i<pp.size(); ++i)
               {
                   
                   delPaths[i] = (TreePath)pp.get(i);
               }
               jTreeDocument.getSelectionModel().removeSelectionPaths(delPaths);
           }
           
           jTreeDocumentValueChanged(null);
         }
    }
    
    /**
     * Return the leaf objects selected.
     * 
     */
    public java.util.List getSelectedObjects()
    {
        TreePath[] paths = jTreeDocument.getSelectionPaths();
        java.util.List list = new java.util.ArrayList();
        for (int i=0; paths!=null && i<paths.length; ++i)
        {
            list.add( ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject() );
        }
        
        return list;
    
    }
    
    /**
     * Return the leaf objects selected.
     * 
     */
     public void addObjectsToSelection( java.util.List list)
    {
         
        for (int i=0; i<list.size(); ++i)
        {
            Object obj = list.get(i);
            DefaultMutableTreeNode mtd = Misc.findNodeWithUserObject( obj, (TreeNode)this.jTreeDocument.getModel().getRoot() );
    
            if (mtd != null)
            {
                jTreeDocument.addSelectionPath( new TreePath( mtd.getPath() ));
            }
        }
        return;
    
    }
    
    
    /**
    * Implements the  reportSubDatasetObjectChangedListener 
    * @param evt 
    */ 
    public void reportSubDatasetChanged(ReportSubDatasetChangedEvent evt) {

        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTreeDocument.getModel().getRoot();
        if (evt.getAction() == evt.ADDED &&
           evt.getObjectType() == evt.OBJECT_SUBDATASET)
       {
           int lastDatasourceIndex = 3;
           for (lastDatasourceIndex=3; root.getChildCount() > lastDatasourceIndex; lastDatasourceIndex++)
           {
               if ( ((DefaultMutableTreeNode)root.getChildAt(lastDatasourceIndex)).getUserObject() instanceof SubDataset) continue;
               break;
           }

           for (int i=0; i<evt.getElements().size(); ++i)
           {
            ReportObjectTreeNode subDatasetNode = new ReportObjectTreeNode(evt.getElements().elementAt(i));
            root.insert(subDatasetNode, lastDatasourceIndex);
            updateSubDatasetNode( subDatasetNode, null );
            lastDatasourceIndex++;
           }
           
            jTreeDocument.updateUI();
           
       } else if (evt.getAction() == evt.REMOVED &&
           evt.getObjectType() == evt.OBJECT_SUBDATASET)
       {

            DefaultMutableTreeNode subDatasetNode = null;
             for (int i=0; i<evt.getElements().size(); ++i)
             {
                // 1. Find the datasource...
                 for (int k=0; k< root.getChildCount(); ++k)
                 {
                     if (((DefaultMutableTreeNode)root.getChildAt(k)).getUserObject() == evt.getElements().elementAt(i))
                     {
                         root.remove(k);
                         break;
                     }
                 }
             }

           jTreeDocument.updateUI();

       } else if (evt.getAction() == evt.CHANGED &&
           evt.getObjectType() == evt.OBJECT_SUBDATASET)
       {
           jTreeDocument.updateUI();
       }
        
        
       if (evt.getAction() == evt.REMOVED)
       {
           jTreeDocumentValueChanged(null);
       }
    }


    public void updateSubDatasetNode(SubDataset subDataset, List openedNodeIds)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTreeDocument.getModel().getRoot();
        for (int k=0; k< root.getChildCount(); ++k)
        {
             if (((DefaultMutableTreeNode)root.getChildAt(k)).getUserObject() == subDataset)
             {
                 ReportObjectTreeNode rotn = (ReportObjectTreeNode)root.getChildAt(k);
                 updateSubDatasetNode(rotn, openedNodeIds);
                 
                 if (openedNodeIds != null && openedNodeIds.contains(rotn.getNodeId()))
                 {
                    this.expandPath(rotn);
                 }
                 return;
             }
        }

    }

    public void updateSubDatasetNode(ReportObjectTreeNode subDatasetNode, List openedNodeIds )
    {
        ReportObjectTreeNode dsFieldsNode = null;
        ReportObjectTreeNode dsVariablesNode = null;
        ReportObjectTreeNode dsParametersNode = null;

        SubDataset subDataset = (SubDataset)subDatasetNode.getUserObject();

        if (subDatasetNode.getChildCount() == 0)
        {
            dsParametersNode = new ReportObjectTreeNode(it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters"),true);
            subDatasetNode.add( dsParametersNode );
            
            dsFieldsNode = new ReportObjectTreeNode(it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields"),true);
            subDatasetNode.add( dsFieldsNode );

            dsVariablesNode = new ReportObjectTreeNode(it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables"),true);
            subDatasetNode.add( dsVariablesNode );
        }
        else
        {
            dsParametersNode = (ReportObjectTreeNode)subDatasetNode.getChildAt(0);
            dsFieldsNode = (ReportObjectTreeNode)subDatasetNode.getChildAt(1);
            dsVariablesNode = (ReportObjectTreeNode)subDatasetNode.getChildAt(2);
        }

        dsFieldsNode.removeAllChildren();
        dsVariablesNode.removeAllChildren();
        dsParametersNode.removeAllChildren();

        
        java.util.Enumeration e = subDataset.getFields().elements();
        while (e.hasMoreElements())
        {
             dsFieldsNode.add(new ReportObjectTreeNode(e.nextElement()));
        }
        if (openedNodeIds != null && openedNodeIds.contains(dsFieldsNode.getNodeId()))
        {
            this.expandPath(dsFieldsNode);
        }
        
        e = subDataset.getParameters().elements();
        
        boolean showBuiltInParams = MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinParameters","true").equals("true");
        while (e.hasMoreElements())
        {
             JRParameter param = (JRParameter)e.nextElement();
             if (param.isBuiltin() && !showBuiltInParams)
             {
                 continue;
             }
             dsParametersNode.add(new ReportObjectTreeNode(param));
        }

         if (openedNodeIds != null && openedNodeIds.contains(dsParametersNode.getNodeId()))
       {
           this.expandPath(dsParametersNode);
       }
                 
       e = subDataset.getVariables().elements();
        
       boolean showBuiltInPVars = MainFrame.getMainInstance().getProperties().getProperty("documentStructure.showBuiltinVariables","true").equals("true");
       while (e.hasMoreElements())
       {
             JRVariable var = (JRVariable)e.nextElement();
             if (var.isBuiltin() && !showBuiltInPVars)
             {
                 continue;
             }
             dsVariablesNode.add(new ReportObjectTreeNode(var));
       }
        

        if (openedNodeIds != null && openedNodeIds.contains(dsVariablesNode.getNodeId()))
        {
            this.expandPath(dsVariablesNode);
        }

    }

    public void updateSubDatasets(java.util.List openedPaths)
    {
      
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTreeDocument.getModel().getRoot();
        for (int k=0; k< root.getChildCount(); ++k)
        {
             if (((DefaultMutableTreeNode)root.getChildAt(k)).getUserObject() instanceof SubDataset)
             {
                 root.remove(k);
                 k--;
             }
        }

        JReportFrame jrf = getJReportFrame();
        if (jrf != null)
        {
            int lastDatasourceIndex = 3;
            for (int i=0; i< jrf.getReport().getSubDatasets().size(); ++i)
            {
                ReportObjectTreeNode subDatasetNode = new ReportObjectTreeNode(jrf.getReport().getSubDatasets().elementAt(i));
                root.insert(subDatasetNode, lastDatasourceIndex);
                updateSubDatasetNode( subDatasetNode, openedPaths);
                lastDatasourceIndex++;
                
                if (openedPaths != null && openedPaths.contains(subDatasetNode.getNodeId()))
                {
                    this.expandPath(subDatasetNode);
                }    
            }
        }
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
        setJReportFrame(evt.getReportFrame() );
    }
    
    public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt) {
    }

    
}
