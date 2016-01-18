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
 * CrosstabMeasuresView.java
 * 
 * Created on January 24, 2006, 10:42 AM
 *
 */

package it.businesslogic.ireport.crosstab.gui;

import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.crosstab.CrosstabColumnGroup;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.crosstab.CrosstabRowGroup;
import it.businesslogic.ireport.crosstab.GroupTotal;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.docking.GenericDragTargetListener;
import it.businesslogic.ireport.gui.event.CrosstabLayoutChangedEvent;
import it.businesslogic.ireport.gui.library.LibraryTreeCellRenderer;
import it.businesslogic.ireport.gui.library.TreeTransfertHandler;
import java.awt.dnd.DropTarget;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class CrosstabMeasuresView extends javax.swing.JPanel implements it.businesslogic.ireport.gui.event.CrosstabLayoutChangedListener {
    
    
    private CrosstabReportElement crosstabReportElement = null;
    
    
    /** Creates new form CrosstabMeasuresView */
    public CrosstabMeasuresView() {
        initComponents();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("measures");
        
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        
        jTree.setModel( dtm );
        jTree.setDragEnabled(true);
        jTree.setRootVisible( false );
        jTree.setTransferHandler(new TreeTransfertHandler());
        jTree.setCellRenderer( new LibraryTreeCellRenderer() );
        jTree.expandPath( new TreePath(root));
        
        jTree.setDropTarget(new DropTarget(this, new GenericDragTargetListener()));
        
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        
        // Stop to listen to the old crosstab (if not null)
        if (this.crosstabReportElement != null && this.crosstabReportElement != crosstabReportElement)
        {
            this.crosstabReportElement.removeCrosstabLayoutChangedListener( this );
        }
        
        // Start to listen to the new crosstab (if not null)
        if (crosstabReportElement != null && this.crosstabReportElement != crosstabReportElement)
        {
            crosstabReportElement.addCrosstabLayoutChangedListener( this );
        }
        
        this.crosstabReportElement = crosstabReportElement;
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTree.getModel().getRoot();
        root.removeAllChildren();
        
        if (crosstabReportElement != null)
            {
            
            
            for (int i=0; i<crosstabReportElement.getRowGroups().size(); ++i)
            {
                CrosstabGroup group = (CrosstabGroup)crosstabReportElement.getRowGroups().elementAt(i);
                root.add(new DefaultMutableTreeNode( group ));
            }
            
            for (int i=0; i<crosstabReportElement.getColumnGroups().size(); ++i)
            {
                CrosstabGroup group = (CrosstabGroup)crosstabReportElement.getColumnGroups().elementAt(i);
                root.add(new DefaultMutableTreeNode( group ));
            }
            
            for (int i=0; i<crosstabReportElement.getMeasures().size(); ++i)
            {
                Measure measure = (Measure)crosstabReportElement.getMeasures().elementAt(i);
                root.add(new DefaultMutableTreeNode( measure ));
                for (int j=0; j<crosstabReportElement.getRowGroups().size(); ++j)
                {
                    CrosstabGroup group = (CrosstabGroup)crosstabReportElement.getRowGroups().elementAt(j);
                    root.add(new DefaultMutableTreeNode( new GroupTotal(measure.getName(), group.getName(), measure.getClassType())));
                }
                for (int j=0; j<crosstabReportElement.getColumnGroups().size(); ++j)
                {
                    CrosstabGroup group = (CrosstabGroup)crosstabReportElement.getColumnGroups().elementAt(j);
                    root.add(new DefaultMutableTreeNode( new GroupTotal(measure.getName(), group.getName(), measure.getClassType())));
                }
            }
            
            for (int i=0; i<crosstabReportElement.getCrosstabParameters().size(); ++i)
            {
                CrosstabParameter parameter = (CrosstabParameter)crosstabReportElement.getCrosstabParameters().elementAt(i);
                root.add(new DefaultMutableTreeNode( parameter ));
            }
            
        }
        
        jTree.expandPath( new TreePath(root));
        jTree.updateUI();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPopupMenuMeasures = new javax.swing.JPopupMenu();
        jMenuItemEditMeasure = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuItemNewMeasure = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemCrosstabProperties = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemRefresh = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree = new javax.swing.JTree();

        jPopupMenuMeasures.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenuMeasuresPopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemEditMeasure.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemEditMeasure.setText("Edit");
        jMenuItemEditMeasure.setActionCommand("Modify measure");
        jMenuItemEditMeasure.setEnabled(false);
        jMenuItemEditMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditMeasureActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemEditMeasure);

        jPopupMenuMeasures.add(jSeparator4);

        jMenuItemNewMeasure.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemNewMeasure.setText("New measure");
        jMenuItemNewMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewMeasureActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemNewMeasure);

        jPopupMenuMeasures.add(jSeparator3);

        jMenuItemCrosstabProperties.setIcon(new javax.swing.ImageIcon(""));
        jMenuItemCrosstabProperties.setText("Crosstab properties");
        jMenuItemCrosstabProperties.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditActionPerformed1(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemCrosstabProperties);

        jPopupMenuMeasures.add(jSeparator1);

        jMenuItemCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/cut.png")));
        jMenuItemCut.setText("Cut");
        jMenuItemCut.setEnabled(false);
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCutActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemCut);

        jMenuItemCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/copy.png")));
        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.setEnabled(false);
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemCopy);

        jMenuItemPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/paste.png")));
        jMenuItemPaste.setText("Paste");
        jMenuItemPaste.setEnabled(false);
        jMenuItemPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPasteActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemPaste);

        jMenuItemDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/delete.png")));
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.setEnabled(false);
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemDelete);

        jPopupMenuMeasures.add(jSeparator2);

        jMenuItemRefresh.setText("Refresh");
        jMenuItemRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRefreshActionPerformed(evt);
            }
        });

        jPopupMenuMeasures.add(jMenuItemRefresh);

        setLayout(new java.awt.BorderLayout());

        jTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTreeMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTree);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void jTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTreeMouseClicked

        if (evt.getClickCount() == 1 && evt.getButton() == evt.BUTTON3)
        {
            jPopupMenuMeasures.show(jTree, evt.getX(), evt.getY());
        }
        else if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            jMenuItemEditMeasureActionPerformed(null);
        }
    }//GEN-LAST:event_jTreeMouseClicked

    private void jMenuItemEditActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditActionPerformed1
        it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
        cpd.setCurrentCrosstabReportElement( getCrosstabReportElement());
        cpd.setVisible(true);
    }//GEN-LAST:event_jMenuItemEditActionPerformed1

    private void jPopupMenuMeasuresPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenuMeasuresPopupMenuWillBecomeVisible
        
        jMenuItemNewMeasure.setEnabled( true );
        jMenuItemRefresh.setEnabled( true );
        
        // Edit is able only if a single valid object is selected...
        TreePath[] paths = jTree.getSelectionPaths();
        
        if (paths == null || paths.length == 0) {
            jMenuItemCut.setEnabled(false);
            jMenuItemCopy.setEnabled(false);
            jMenuItemDelete.setEnabled(false);
            jMenuItemPaste.setEnabled(false);
            jMenuItemEditMeasure.setEnabled(false);
            return;
        }
        
        if (paths.length == 1 &&
                paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode &&
                ( (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof Measure )) ||
                ( (((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject() instanceof CrosstabGroup ))) {
            jMenuItemEditMeasure.setEnabled(true);
            jMenuItemCut.setEnabled(true);
            jMenuItemCopy.setEnabled(true);
            jMenuItemDelete.setEnabled(true);
        } else {
            jMenuItemEditMeasure.setEnabled( false );
            jMenuItemCut.setEnabled(false);
            jMenuItemCopy.setEnabled(false);
            jMenuItemDelete.setEnabled(false);
        }
        
        jMenuItemPaste.setEnabled(false);

        if (MainFrame.getMainInstance().getMeasuresClipBoard().size() > 0)
        {
            jMenuItemPaste.setEnabled(true);
        }
        
    }//GEN-LAST:event_jPopupMenuMeasuresPopupMenuWillBecomeVisible

    private void jMenuItemRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRefreshActionPerformed
        this.setCrosstabReportElement(this.crosstabReportElement);
    }//GEN-LAST:event_jMenuItemRefreshActionPerformed

    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeleteActionPerformed
        TreePath[] paths = jTree.getSelectionPaths();
                
        for (int i=0; i<paths.length; ++i) 
        {
            try {
                if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode) {
                    Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
                    if (obj instanceof Measure ) { 
                        if (getCrosstabReportElement().getMeasures().size() > 0)
                        {
                            getCrosstabReportElement().removeMeasure((Measure)obj);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_jMenuItemDeleteActionPerformed

    private void jMenuItemPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPasteActionPerformed
        TreePath[] paths = jTree.getSelectionPaths();
        
        if (paths.length == 1) {
            if (paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode) {
                
                for (int i=0; i< MainFrame.getMainInstance().getMeasuresClipBoard().size(); ++i)
                {
                    Measure measure = (Measure)MainFrame.getMainInstance().getMeasuresClipBoard().elementAt(i);
                    measure = measure.cloneMe();
                    
                    // ---------------
                    String base_name = measure.getName();
                    java.util.Vector measures = getCrosstabReportElement().getMeasures();

                    for (int j = 0;; ++j)
                    {

                        boolean found = false;

                        for (int k = 0; k < measures.size(); ++k)
                        {

                            Measure measure1 = (Measure) measures.elementAt(
                                                             k);

                            if (j == 0)
                            {

                                if (measure1.getName().equals(base_name))
                                {
                                    found = true;

                                    break;
                                }
                            }
                            else
                            {

                                if (measure1.getName().equals(base_name + "_" + j))
                                {
                                    found = true;

                                    break;
                                }
                            }
                        }

                        if (!found)
                        {
                            measure.setName(base_name + 
                                              ((j == 0)
                                                   ? ""
                                                   : "_" + j));

                            break;
                        }
                    }        
                    //----------------------
                    
                    getCrosstabReportElement().addMeasure(measure);
                }
            }
        }
    }//GEN-LAST:event_jMenuItemPasteActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        TreePath[] paths = jTree.getSelectionPaths();
        
        boolean clearedMeasuresClipboard = false;
        
        for (int i=0; i<paths.length; ++i) {
           
            if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode) {
                Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
 
                if (obj instanceof Measure ) {
                    if (!clearedMeasuresClipboard) {
                        clearedMeasuresClipboard = true;
                        MainFrame.getMainInstance().getMeasuresClipBoard().removeAllElements();
                    }
                    MainFrame.getMainInstance().getMeasuresClipBoard().add( ((Measure)obj).cloneMe() );
                }
            }
        }
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCutActionPerformed
        TreePath[] paths = jTree.getSelectionPaths();
        
        boolean clearedMeasuresClipboard = false;
        
        for (int i=0; i<paths.length; ++i) {

            if (paths[i].getLastPathComponent() instanceof DefaultMutableTreeNode) {
                Object obj = ((DefaultMutableTreeNode)paths[i].getLastPathComponent()).getUserObject();
                if (obj instanceof Measure ) {
                    if (!clearedMeasuresClipboard) {
                        clearedMeasuresClipboard = true;
                        MainFrame.getMainInstance().getMeasuresClipBoard().removeAllElements();
                    }
                    MainFrame.getMainInstance().getMeasuresClipBoard().add( ((Measure)obj).cloneMe() );
                    getCrosstabReportElement().removeMeasure((Measure)obj);
                }
            }
        }
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jMenuItemNewMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewMeasureActionPerformed
        
        MeasureDialog jrpd = new MeasureDialog(null,MainFrame.getMainInstance(), true);
        jrpd.setExpressionContext( ((this.getCrosstabReportElement().isUseDataset() && this.getCrosstabReportElement().getDataset() != null) ? this.getCrosstabReportElement().getDataset().getSubDataset() : null));
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            Measure measure = jrpd.getMeasure();
            getCrosstabReportElement().addMeasure( measure );  
        }
        
    }//GEN-LAST:event_jMenuItemNewMeasureActionPerformed

    private void jMenuItemEditMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditMeasureActionPerformed
// We have to see what type of item we have selected...
        TreePath[] paths = jTree.getSelectionPaths();
        
        if (paths == null || paths.length == 0) {
            return;
        }
        
        if (paths.length == 1 && paths[0].getLastPathComponent() instanceof DefaultMutableTreeNode) {
            Object obj = ((DefaultMutableTreeNode)paths[0].getLastPathComponent()).getUserObject();
            if (obj instanceof Measure)
            { 
                MeasureDialog jrpd = new MeasureDialog(null,MainFrame.getMainInstance(), true);
                jrpd.setExpressionContext( ((this.getCrosstabReportElement().isUseDataset() && this.getCrosstabReportElement().getDataset() != null) ? this.getCrosstabReportElement().getDataset().getSubDataset() : null));
                jrpd.setMeasure((Measure)obj );
                jrpd.setVisible(true);

                if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                    Measure measure = (Measure)obj;
                    measure.setName( jrpd.getMeasure().getName() );
                    measure.setExpression( jrpd.getMeasure().getExpression());
                    measure.setClassType( jrpd.getMeasure().getClassType());
                    measure.setCalculation( jrpd.getMeasure().getCalculation());
                    measure.setIncrementerFactoryClass( jrpd.getMeasure().getIncrementerFactoryClass());
                    measure.setPercentageCalculatorClass( jrpd.getMeasure().getPercentageCalculatorClass());
                    measure.setPercentageOf( jrpd.getMeasure().getPercentageOf());
                    getCrosstabReportElement().measureModified( measure );
                    setCrosstabReportElement(this.crosstabReportElement);
                }
            }
            else if (obj instanceof CrosstabGroup)
            { 
                CrosstabGroup rg = (CrosstabGroup)obj;
                CrosstabGroupDialog jrpd = new CrosstabGroupDialog(
                        (rg instanceof CrosstabColumnGroup) ? CrosstabGroupDialog.COLUMNGROUP : CrosstabGroupDialog.ROWGROUP, MainFrame.getMainInstance(), true);
                jrpd.setExpressionContext( ((this.getCrosstabReportElement().isUseDataset() && this.getCrosstabReportElement().getDataset() != null) ? this.getCrosstabReportElement().getDataset().getSubDataset() : null));
                jrpd.setGroup( rg );
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
                    if (rg instanceof CrosstabColumnGroup) getCrosstabReportElement().modifyGroup(oldName, (CrosstabColumnGroup)rg);
                    if (rg instanceof CrosstabRowGroup) getCrosstabReportElement().modifyGroup(oldName, (CrosstabRowGroup)rg);
                }
            }
            
            /*
            if (obj instanceof JRField ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyField((JRField)obj ); }
            if (obj instanceof JRParameter ) { MainFrame.getMainInstance().getValuesDialog().getValuesPanel().modifyParameter((JRParameter)obj ); }
            if (obj instanceof SubDataset ) {
                java.awt.Frame parent = Misc.frameFromComponent(this);
                SubDatasetDialog cd = new SubDatasetDialog(parent,true);
                cd.setSubDataset( (SubDataset)obj );
                cd.setVisible(true);
            }
            */
        }
    }//GEN-LAST:event_jMenuItemEditMeasureActionPerformed

    public void crosstabLayoutChanged(CrosstabLayoutChangedEvent evt) {
        setCrosstabReportElement(crosstabReportElement);
    }

    public void crosstabMeasureChanged(CrosstabLayoutChangedEvent evt) {
        setCrosstabReportElement(crosstabReportElement);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCrosstabProperties;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemEditMeasure;
    private javax.swing.JMenuItem jMenuItemNewMeasure;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemRefresh;
    private javax.swing.JPopupMenu jPopupMenuMeasures;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTree jTree;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jMenuItemCopy.setText(I18n.getString("crosstabMeasuresView.menuItemCopy","Copy"));
                jMenuItemCrosstabProperties.setText(I18n.getString("crosstabMeasuresView.menuItemCrosstabProperties","Crosstab properties"));
                jMenuItemCut.setText(I18n.getString("crosstabMeasuresView.menuItemCut","Cut"));
                jMenuItemDelete.setText(I18n.getString("crosstabMeasuresView.menuItemDelete","Delete"));
                jMenuItemEditMeasure.setText(I18n.getString("crosstabMeasuresView.menuItemEditMeasure","Edit"));
                jMenuItemNewMeasure.setText(I18n.getString("crosstabMeasuresView.menuItemNewMeasure","New measure"));
                jMenuItemPaste.setText(I18n.getString("crosstabMeasuresView.menuItemPaste","Paste"));
                jMenuItemRefresh.setText(I18n.getString("crosstabMeasuresView.menuItemRefresh","Refresh"));
                // End autogenerated code ----------------------
    }
}
