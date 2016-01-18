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
 * StylesView.java
 * 
 * Created on March 20, 2006, 5:15 PM
 *
 */

package it.businesslogic.ireport.gui.style;

import it.businesslogic.ireport.JasperTemplate;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.Template;
import it.businesslogic.ireport.TemplateStyle;
import it.businesslogic.ireport.UndefinedStyle;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.dnd.JListTransfertHandler;
import it.businesslogic.ireport.gui.docking.GenericDragTargetListener;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedEvent;
import it.businesslogic.ireport.gui.event.ReportFrameActivatedListener;
import it.businesslogic.ireport.gui.event.StyleChangedEvent;
import it.businesslogic.ireport.gui.event.StyleChangedListener;
import it.businesslogic.ireport.gui.event.TemplateChangedEvent;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import it.businesslogic.ireport.util.Misc;
import java.awt.BorderLayout;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;


/**
 *
 * @author  gtoffoli
 */
public class StylesView extends javax.swing.JPanel implements LanguageChangedListener, ReportFrameActivatedListener, StyleChangedListener 
{
    private JReportFrame activeReportFrame = null;
    private DefaultTreeModel libraryTreeModel = null;
    private DefaultTreeModel reportTreeModel = null;

    
    
    private ArrayList<JasperTemplate> openedTemplates = new ArrayList<JasperTemplate>();
    
    /** Creates new form StylesView */
    public StylesView() {
        initComponents();
        applyI18n();
        
        reportTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("report"));
        jListStyles.setDropTarget(new DropTarget(this, DnDConstants.ACTION_MOVE, new GenericDragTargetListener(), true));
        jListStyles.setTransferHandler(new JListTransfertHandler());
        jListStyles.setRootVisible(false);
        jListStyles.setModel(reportTreeModel);
        
        libraryTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("library"));
        jListStylesLibrary.setTransferHandler(new JListTransfertHandler());
        jListStylesLibrary.setRootVisible(false);
        jListStylesLibrary.setModel(libraryTreeModel);
        
        jComboBoxStyleType.addItem(new Tag("reportStyles", it.businesslogic.ireport.util.I18n.getString("stylesView.reportStyles","Report styles")));
        jComboBoxStyleType.addItem(new Tag("libraryStyles", it.businesslogic.ireport.util.I18n.getString("stylesView.stylesLibrary","Styles library")));
        
        languageChanged(null);
        
        MainFrame.getMainInstance().addReportFrameActivatedListener( this );
        
        updateStyles();
        
        jComboBoxStyleType.setSelectedIndex(0);
    }
    
    
    public void loadLibraryStyles()
    {
        Vector v = MainFrame.getMainInstance().getStyleLibrarySet();
        for (int i=0; i<v.size(); ++i)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode( v.elementAt(i) );
            ((DefaultMutableTreeNode)libraryTreeModel.getRoot()).add(node);
        }
        jListStylesLibrary.clearSelection();
        jListStylesLibrary.updateUI();
    }
    
    public void refreshReportStyles()
    {
        ((DefaultMutableTreeNode)reportTreeModel.getRoot()).removeAllChildren();
            
        if (getActiveReportFrame() != null)
        {
            Vector v = getActiveReportFrame().getReport().getStyles();
            for (int i=0; i<v.size(); ++i)
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode( v.elementAt(i) );
                ((DefaultMutableTreeNode)reportTreeModel.getRoot()).add(node);
            }
            v = getActiveReportFrame().getReport().getTemplates();
            for (int i=0; i<v.size(); ++i)
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode( v.elementAt(i) );
                ((DefaultMutableTreeNode)reportTreeModel.getRoot()).add(node);
            }
        }
        jListStyles.clearSelection();
        jListStyles.updateUI();
        
        if (isListingReportStyles())
        {
            jListStylesValueChanged(null);
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemNewStyle = new javax.swing.JMenuItem();
        jMenuItemNewStyleTemplate = new javax.swing.JMenuItem();
        jMenuItemEditStyle = new javax.swing.JMenuItem();
        jMenuItemEditTemplate = new javax.swing.JMenuItem();
        jMenuItemRemove = new javax.swing.JMenuItem();
        jMenuItemCloseTemplate = new javax.swing.JMenuItem();
        jMenuItemAddTemplateToJT = new javax.swing.JMenuItem();
        jMenuItemAddStyleToJT = new javax.swing.JMenuItem();
        jMenuItemNewTemplateItem = new javax.swing.JMenuItem();
        jMenuItemOpenStyleTemplate = new javax.swing.JMenuItem();
        jScrollPaneLibraryList = new javax.swing.JScrollPane();
        jListStylesLibrary = new it.businesslogic.ireport.gui.style.JBGTree();
        jMenuItemEditJTemplate = new javax.swing.JMenuItem();
        jScrollPaneReportList = new javax.swing.JScrollPane();
        jListStyles = new it.businesslogic.ireport.gui.style.JBGTree();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxStyleType = new javax.swing.JComboBox();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonAddStyle = new javax.swing.JButton();
        jButtonEditStyle = new javax.swing.JButton();
        jButtonDeleteStyle = new javax.swing.JButton();

        jPopupMenu1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jPopupMenu1PopupMenuWillBecomeVisible(evt);
            }
        });

        jMenuItemNewStyle.setText("New style");
        jMenuItemNewStyle.setActionCommand("New Style");
        jMenuItemNewStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewStyleActionPerformed(evt);
            }
        });

        jMenuItemNewStyleTemplate.setText("New Jasper Template");
        jMenuItemNewStyleTemplate.setActionCommand("New Style Template");
        jMenuItemNewStyleTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewStyleTemplateActionPerformed(evt);
            }
        });

        jMenuItemEditStyle.setText("Edit Style");
        jMenuItemEditStyle.setActionCommand("New Style Template");
        jMenuItemEditStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditStyleActionPerformed(evt);
            }
        });

        jMenuItemEditTemplate.setText("Edit Template Reference");
        jMenuItemEditTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditTemplateActionPerformed(evt);
            }
        });

        jMenuItemRemove.setText("Delete");
        jMenuItemRemove.setActionCommand("New Style Template");
        jMenuItemRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRemoveActionPerformed(evt);
            }
        });

        jMenuItemCloseTemplate.setText("Close Jasper Template");
        jMenuItemCloseTemplate.setActionCommand("New Style Template");
        jMenuItemCloseTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseTemplateActionPerformed(evt);
            }
        });

        jMenuItemAddTemplateToJT.setText("Add Template to Jasper Template");
        jMenuItemAddTemplateToJT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddTemplateToJTActionPerformed(evt);
            }
        });

        jMenuItemAddStyleToJT.setText("Add Style to Jasper Template");
        jMenuItemAddStyleToJT.setActionCommand("New Style");
        jMenuItemAddStyleToJT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddStyleToJTActionPerformed(evt);
            }
        });

        jMenuItemNewTemplateItem.setText("New Template Item");
        jMenuItemNewTemplateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewTemplateItemActionPerformed(evt);
            }
        });

        jMenuItemOpenStyleTemplate.setText("Open Jasper Template");
        jMenuItemOpenStyleTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenStyleTemplateActionPerformed(evt);
            }
        });

        jListStylesLibrary.setComponentPopupMenu(jPopupMenu1);
        jListStylesLibrary.setDragEnabled(true);
        jListStylesLibrary.setOpaque(false);
        jListStylesLibrary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListStylesLibraryMouseClicked(evt);
            }
        });
        jListStylesLibrary.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jListStylesLibraryValueChanged(evt);
            }
        });
        jScrollPaneLibraryList.setViewportView(jListStylesLibrary);

        jMenuItemEditJTemplate.setText("Edit Template Reference");
        jMenuItemEditJTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEditJTemplateActionPerformed(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        jScrollPaneReportList.setBackground(new java.awt.Color(255, 255, 255));

        jListStyles.setComponentPopupMenu(jPopupMenu1);
        jListStyles.setDragEnabled(true);
        jListStyles.setOpaque(false);
        jListStyles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListStylesMouseClicked(evt);
            }
        });
        jListStyles.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jListStylesValueChanged(evt);
            }
        });
        jScrollPaneReportList.setViewportView(jListStyles);

        add(jScrollPaneReportList, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jComboBoxStyleType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStyleTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        jPanel1.add(jComboBoxStyleType, gridBagConstraints);

        jToolBar1.setFloatable(false);

        jButtonAddStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/style_add.png"))); // NOI18N
        jButtonAddStyle.setToolTipText("Style add");
        jButtonAddStyle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonAddStyle.setFocusPainted(false);
        jButtonAddStyle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonAddStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddStyleActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonAddStyle);

        jButtonEditStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/style_edit.png"))); // NOI18N
        jButtonEditStyle.setToolTipText("Style add");
        jButtonEditStyle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonEditStyle.setFocusPainted(false);
        jButtonEditStyle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonEditStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditStyleActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonEditStyle);

        jButtonDeleteStyle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/style_delete.png"))); // NOI18N
        jButtonDeleteStyle.setToolTipText("Style add");
        jButtonDeleteStyle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonDeleteStyle.setFocusPainted(false);
        jButtonDeleteStyle.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonDeleteStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteStyleActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDeleteStyle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jToolBar1, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemEditJTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditJTemplateActionPerformed
    
    Object selectedObject = getActiveList().getSelectedItem();
    if (selectedObject == null || !(selectedObject instanceof JasperTemplate)) return;
    
    
    JasperTemplate template = (JasperTemplate)selectedObject;
    if (template.getParent() != null) return;
    
    TemplateDataDialog td = new TemplateDataDialog(MainFrame.getMainInstance(),true);
    String exp = template.getExpression();
    td.setTemplate(exp);
    td.setVisible(true);
    
    if (td.getDialogResult() == JOptionPane.OK_OPTION)
        {
            template.setExpression( td.getTemplate());
                    try {
                        
                        template.getParent().saveTemplateFile();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
            getActiveList().updateUI();
    }
}//GEN-LAST:event_jMenuItemEditJTemplateActionPerformed

private void jMenuItemAddTemplateToJTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddTemplateToJTActionPerformed
    
    Object selectedObject = getActiveList().getSelectedItem();
    if (selectedObject != null && selectedObject instanceof JasperTemplate)
    {
            JasperTemplate jt = (JasperTemplate)selectedObject;
            if (jt.getParent() != null) return;
            if (jt.canWrite())
            {
                TemplateDataDialog tdd = new TemplateDataDialog(MainFrame.getMainInstance(), true);
                tdd.setVisible(true);
                
                if (tdd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    String templateStr = tdd.getTemplate();
                    JasperTemplate newTemplate = new JasperTemplate(templateStr);
                    newTemplate.setParent(jt);
                    jt.getTemplates().add(newTemplate); 
                    
                    try {
                        
                        jt.saveTemplateFile();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    
                     DefaultMutableTreeNode node = Misc.findNodeWithUserObject(selectedObject, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
                     DefaultMutableTreeNode child = new DefaultMutableTreeNode(newTemplate);
                     node.add(child);
                     
                     getActiveList().expandPath(new TreePath(node.getPath()));
                     getActiveList().updateUI();
                }
            }
    }
    
    
}//GEN-LAST:event_jMenuItemAddTemplateToJTActionPerformed

    private void jMenuItemAddStyleToJTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddStyleToJTActionPerformed
    Object selectedObject = getActiveList().getSelectedItem();
    if (selectedObject != null && selectedObject instanceof JasperTemplate)
    {
            JasperTemplate jt = (JasperTemplate)selectedObject;
            if (jt.getParent() != null) return;
            if (jt.canWrite())
            {
                StyleDialog sd = new StyleDialog(MainFrame.getMainInstance(), true);
                sd.setLibraryStyle(true);
                sd.setJasperTemplate( jt );
                sd.setVisible(true);
                
                if (sd.getDialogResult() == JOptionPane.OK_OPTION)
                {
                    TemplateStyle templateStyle = new TemplateStyle(sd.getStyle());
                    
                    templateStyle.setJasperTemplate(jt);
                    jt.getStyles().add(templateStyle); 
                    
                    try {
                        
                        jt.saveTemplateFile();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    
                     DefaultMutableTreeNode node = Misc.findNodeWithUserObject(selectedObject, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
                     DefaultMutableTreeNode child = new DefaultMutableTreeNode(templateStyle);
                     node.add(child);
                     
                     getActiveList().expandPath(new TreePath(node.getPath()));
                     getActiveList().updateUI();
                }
            }
    }
}//GEN-LAST:event_jMenuItemAddStyleToJTActionPerformed

private void jMenuItemCloseTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseTemplateActionPerformed
    Object selectedObject = getActiveList().getSelectedItem();
    if (selectedObject != null && selectedObject instanceof JasperTemplate)
        {
            JasperTemplate jt = (JasperTemplate)selectedObject;
            if (jt.getParent() != null)
            {
                jt = jt.getParent();
            }
            DefaultMutableTreeNode node = Misc.findNodeWithUserObject(jt, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
           ((DefaultTreeModel)getActiveList().getModel()).removeNodeFromParent(node);
            getActiveList().updateUI();
        }
}//GEN-LAST:event_jMenuItemCloseTemplateActionPerformed

private void jListStylesValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jListStylesValueChanged
     Object selectedObject = jListStyles.getSelectedItem();
        if (selectedObject == null ||
            selectedObject instanceof TemplateStyle ||
            selectedObject instanceof Template ||
            selectedObject instanceof JasperTemplate)
        {
            jButtonEditStyle.setEnabled(false);
            jButtonDeleteStyle.setEnabled(false);
        }
        else if (selectedObject instanceof Style)
        {
            jButtonEditStyle.setEnabled(true);
            jButtonDeleteStyle.setEnabled(true);
        }
        else
        {
            jButtonEditStyle.setEnabled(false);
            jButtonDeleteStyle.setEnabled(false);
        }
     
     jButtonAddStyle.setEnabled( !isListingReportStyles() || getActiveReportFrame() != null  );
     
}//GEN-LAST:event_jListStylesValueChanged

private void jListStylesLibraryValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jListStylesLibraryValueChanged
    Object selectedObject = jListStylesLibrary.getSelectedItem();
        if (selectedObject == null ||
            selectedObject instanceof TemplateStyle ||
            selectedObject instanceof Template ||
            selectedObject instanceof JasperTemplate)
        {
            jButtonEditStyle.setEnabled(false);
            jButtonDeleteStyle.setEnabled(false);
        }
        else if (selectedObject instanceof Style)
        {
            jButtonEditStyle.setEnabled(true);
            jButtonDeleteStyle.setEnabled(true);
        }
        else
        {
            jButtonEditStyle.setEnabled(false);
            jButtonDeleteStyle.setEnabled(false);
        }
    
    jButtonAddStyle.setEnabled( true );
}//GEN-LAST:event_jListStylesLibraryValueChanged

    private void jListStylesLibraryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListStylesLibraryMouseClicked
        
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            Object selectedObject = jListStylesLibrary.getSelectedItem();
            if (selectedObject != null && selectedObject instanceof Style)
            {
                jButtonEditStyleActionPerformed(null);
            }
            else if (selectedObject != null && selectedObject instanceof Template)
            {
                jMenuItemEditTemplateActionPerformed(null);
            }
        }
}//GEN-LAST:event_jListStylesLibraryMouseClicked
  
private void jMenuItemNewStyleTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewStyleTemplateActionPerformed
    
    try {
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( MainFrame.getMainInstance().getCurrentDirectory());

        jfc.setDialogTitle("New JasperTemplate");
        jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("style.title.newJasperTemplate", "New JasperTemplate"));//I18N

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( jfc.FILES_ONLY );
        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".jrtx") || file.isDirectory() ) ;
            }
            public String getDescription() {
                return "*.jrtx";
            }
        });

        jfc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File file = jfc.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".jrtx"))
            {
                file = new File( file + ".jrtx");
            } 
            
            JasperTemplate jt = new JasperTemplate();
            jt.setFilename(file+"");
            System.out.println("Saved to " + jt.getFilename() );
            jt.saveTemplateFile();
            
            openedTemplates.add(jt);
            
            ((DefaultMutableTreeNode)libraryTreeModel.getRoot()).add( createTemplateNode(jt) );
            
            getActiveList().updateUI();
        }
    } catch (Exception ex)
    {
        ex.printStackTrace();
    }
    
}//GEN-LAST:event_jMenuItemNewStyleTemplateActionPerformed

private DefaultMutableTreeNode createTemplateNode(JasperTemplate jt)
{
    DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(jt);
    jt.reloadTemplate();
    
    for (JasperTemplate template : jt.getTemplates())
    {
        DefaultMutableTreeNode templateNode = new DefaultMutableTreeNode(template);
        dmtn.add(templateNode);
    }
    
    for (TemplateStyle style : jt.getStyles())
    {
        DefaultMutableTreeNode styleNode = new DefaultMutableTreeNode(style);
        dmtn.add(styleNode);
    }
    
    return dmtn;
}


private void jMenuItemNewStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewStyleActionPerformed
    jButtonAddStyleActionPerformed(evt);
}//GEN-LAST:event_jMenuItemNewStyleActionPerformed

private void jMenuItemEditStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditStyleActionPerformed
    
    jButtonEditStyleActionPerformed(evt);
    
}//GEN-LAST:event_jMenuItemEditStyleActionPerformed

private void jMenuItemRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRemoveActionPerformed
    
    if (isListingReportStyles())
    {
        if (getActiveReportFrame() == null) return;

        Object selectedObject = getActiveList().getSelectedItem();
        if (selectedObject == null) return;
        if (selectedObject instanceof Template)
        {
            Template template = (Template)selectedObject;
            getActiveReportFrame().getReport().removeTemplate(template);
            //getActiveReportFrame().getReport().incrementReportChanges();
            
            // Remove from the list...
            DefaultMutableTreeNode node = Misc.findNodeWithUserObject(selectedObject, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
           ((DefaultTreeModel)getActiveList().getModel()).removeNodeFromParent(node);
            getActiveList().updateUI();
        } else if (selectedObject instanceof Style)
        {
            jButtonDeleteStyleActionPerformed(null);
        }
    }
    else
    {
        Object selectedObject = getActiveList().getSelectedItem();
        if (selectedObject == null) return;
        if (selectedObject instanceof JasperTemplate)
        {
            JasperTemplate jt = (JasperTemplate)selectedObject;
            if (jt.getParent() != null)
            {
                JasperTemplate parent = jt.getParent();
                parent.getTemplates().remove(jt);
                try {
                    parent.saveTemplateFile();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            DefaultMutableTreeNode node = Misc.findNodeWithUserObject(selectedObject, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
           ((DefaultTreeModel)getActiveList().getModel()).removeNodeFromParent(node);
            getActiveList().updateUI();
        }
        else if (selectedObject instanceof TemplateStyle)
        {
            TemplateStyle ts = (TemplateStyle)selectedObject;
            if (ts.getJasperTemplate() != null)
            {
                JasperTemplate parent = ts.getJasperTemplate();
                parent.getStyles().remove(ts);
                try {
                    parent.saveTemplateFile();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            DefaultMutableTreeNode node = Misc.findNodeWithUserObject(selectedObject, (DefaultMutableTreeNode)getActiveList().getModel().getRoot());
           ((DefaultTreeModel)getActiveList().getModel()).removeNodeFromParent(node);
            getActiveList().updateUI();
        }
        else
        {
            jButtonDeleteStyleActionPerformed(null);
        }
    }
    
    
}//GEN-LAST:event_jMenuItemRemoveActionPerformed

private void jMenuItemEditTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditTemplateActionPerformed
    
    if (getActiveReportFrame() == null) return;
    
    Object selectedObject = getActiveList().getSelectedItem();
    if (selectedObject == null || !(selectedObject instanceof Template)) return;
    
    Template template = (Template)selectedObject;
    
    JRTemplateDialog td = new JRTemplateDialog(MainFrame.getMainInstance(),true);
    td.setTemplate(template);
    td.setVisible(true);
    
    if (td.getDialogResult() == JOptionPane.OK_OPTION)
        {
            boolean modified = false;
            modified = modified || (!template.getExpression().equals(td.getTemplate().getExpression()));
            template.setExpression( td.getTemplate().getExpression() );
            
            modified = modified || (!template.getExpressionClass().equals(td.getTemplate().getExpressionClass()));
            template.setExpressionClass( td.getTemplate().getExpressionClass() );
            
            if (modified)
            {
                getActiveReportFrame().getReport().incrementReportChanges();
            }
            
            getActiveList().updateUI();
    }
    
}//GEN-LAST:event_jMenuItemEditTemplateActionPerformed

private void jMenuItemNewTemplateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewTemplateItemActionPerformed
    
    if (getActiveReportFrame() == null) return;
    
    JRTemplateDialog td = new JRTemplateDialog(MainFrame.getMainInstance(),true);

    td.setVisible(true);
    
    if (td.getDialogResult() == JOptionPane.OK_OPTION)
        {
            Template template = td.getTemplate();
            // Now we have an old and a new object.
            // 1. Adjust table...
            try {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(template);
                ((DefaultMutableTreeNode)jListStyles.getModel().getRoot()).add(node);
                jListStyles.updateUI();
            } catch (Exception ex) { return; } 
            
            getActiveReportFrame().getReport().addTemplate(template);
            getActiveReportFrame().getReport().incrementReportChanges();
        }
}//GEN-LAST:event_jMenuItemNewTemplateItemActionPerformed


private void jMenuItemOpenStyleTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenStyleTemplateActionPerformed
    try {
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( MainFrame.getMainInstance().getCurrentDirectory());

        jfc.setDialogTitle("Open JasperTemplate");
        jfc.setDialogTitle(it.businesslogic.ireport.util.I18n.getString("style.title.openJasperTemplate", "Open JasperTemplate"));//I18N

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( jfc.FILES_ONLY );
        jfc.addChoosableFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.toLowerCase().endsWith(".jrtx") || file.isDirectory() ) ;
            }
            public String getDescription() {
                return "*.jrtx";
            }
        });

        jfc.setMultiSelectionEnabled(true);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File[] files = jfc.getSelectedFiles();

            for (int i=0; i<files.length; ++i)
            {
                JasperTemplate jt = new JasperTemplate();
                jt.setFilename(files[i]+"");
                openedTemplates.add(jt);
                ((DefaultMutableTreeNode)libraryTreeModel.getRoot()).add( createTemplateNode(jt) );
            }
            getActiveList().updateUI();
        }
    } catch (Exception ex)
    {
        ex.printStackTrace();
    }
}//GEN-LAST:event_jMenuItemOpenStyleTemplateActionPerformed

private void jPopupMenu1PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jPopupMenu1PopupMenuWillBecomeVisible
    
    
    Object selectedObject = getActiveList().getSelectedItem();
    
    jPopupMenu1.removeAll();
            
    if (this.isListingReportStyles())
    {
        
        jPopupMenu1.add(jMenuItemNewStyle);
        jMenuItemNewStyle.setEnabled(getActiveReportFrame() != null);
        jPopupMenu1.add(jMenuItemNewTemplateItem);
        jMenuItemNewTemplateItem.setEnabled(getActiveReportFrame() != null);
        
        if (selectedObject != null)
        {
            if (selectedObject instanceof Style)
            {
                jMenuItemEditStyle.setEnabled(true);
                jPopupMenu1.add(jMenuItemEditStyle);
            } 
            else if (selectedObject instanceof Template)
            {
                jPopupMenu1.add(jMenuItemEditTemplate);
            }
            
            jMenuItemRemove.setEnabled(true);
            jPopupMenu1.add(jMenuItemRemove);
        }
    }
    else
    {
        jMenuItemNewStyle.setEnabled(true);
        jPopupMenu1.add(jMenuItemNewStyle);
        jPopupMenu1.add(jMenuItemNewStyleTemplate);
        jPopupMenu1.add(jMenuItemOpenStyleTemplate);
        
        if (selectedObject != null)
        {
            if (selectedObject instanceof TemplateStyle)
            {
                TemplateStyle ts = (TemplateStyle)selectedObject;
                jPopupMenu1.add(new JSeparator());
                jPopupMenu1.add( jMenuItemEditStyle );
                jMenuItemEditStyle.setEnabled(ts.getJasperTemplate().canWrite());
                jPopupMenu1.add(jMenuItemRemove);
                jMenuItemRemove.setEnabled(ts.getJasperTemplate().canWrite());
            }
            else if (selectedObject instanceof Style)
            {
                jPopupMenu1.add(jMenuItemEditStyle);
                jPopupMenu1.add(jMenuItemRemove);
            }
            else if (selectedObject instanceof JasperTemplate)
            {
                JasperTemplate jt = (JasperTemplate)selectedObject;
                jPopupMenu1.add(jMenuItemCloseTemplate);
                
                
                if (jt.getParent() == null)
                {
                    jPopupMenu1.add(new JSeparator());
                    jPopupMenu1.add(jMenuItemAddStyleToJT);
                    jPopupMenu1.add(jMenuItemAddTemplateToJT);
                }
                else
                {
                    jPopupMenu1.add(new JSeparator());
                    jPopupMenu1.add(jMenuItemEditJTemplate);
                    jMenuItemEditJTemplate.setEnabled(jt.getParent().canWrite());
                    jPopupMenu1.add(jMenuItemRemove);
                    jMenuItemRemove.setEnabled(jt.getParent().canWrite());
                }
                
               // jPopupMenu1.add(jMenuItemEditStyleTemplate);
                
            }
       }
    }
    
    
}//GEN-LAST:event_jPopupMenu1PopupMenuWillBecomeVisible

    private void jComboBoxStyleTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStyleTypeActionPerformed

        updateStyles();
        
    }//GEN-LAST:event_jComboBoxStyleTypeActionPerformed

    private void jListStylesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListStylesMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            Object selectedObject = jListStyles.getSelectedItem();
            if (selectedObject != null && selectedObject instanceof Style)
            {
                jButtonEditStyleActionPerformed(null);
            }
            else if (selectedObject != null && selectedObject instanceof Template)
            {
                jMenuItemEditTemplateActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jListStylesMouseClicked

    private void jButtonDeleteStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteStyleActionPerformed
            
        Object selectedObject = getActiveList().getSelectedItem();
        if (selectedObject == null) return;
        if (selectedObject instanceof TemplateStyle) return;
        if (selectedObject instanceof Template) return;
        
        if (selectedObject instanceof Style)
        {
        
            Style irc = null;
            try {
                irc = (Style)selectedObject;
                
            } catch (Exception ex) { return; }
                       
            
            if (javax.swing.JOptionPane.showConfirmDialog(this,I18n.getString("messages.stylesDialog.removingStyle","Do you want really remove this style?"),"",
                    javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION)
            {   
                if (!isListingReportStyles())
                {
                    MainFrame.getMainInstance().getStyleLibrarySet().remove(irc);
                    MainFrame.getMainInstance().saveStyleLibrary();
                    DefaultTreeModel dtm = (DefaultTreeModel)getActiveList().getModel();
                    dtm.removeNodeFromParent( (MutableTreeNode)getActiveList().getSelectionPath().getLastPathComponent() );
                }
                else
                {
                    Report report = MainFrame.getMainInstance().getActiveReportFrame().getReport();
                    report.removeStyle( irc );
                }
            }
        }
    }//GEN-LAST:event_jButtonDeleteStyleActionPerformed

    private void jButtonEditStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditStyleActionPerformed

       Object selectedObject = getActiveList().getSelectedItem();
       if (selectedObject == null) return;
       if (!(selectedObject instanceof Style)) return;
        
        StyleDialog cd = new StyleDialog(MainFrame.getMainInstance(),true);
        
        // Take the selected style from the table...
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)getActiveList().getSelectionPath().getLastPathComponent();
         
        Style irc = (Style)selectedObject; //jListStyles.getSelectedValue();
        cd.setStyle( irc );
        
        cd.setLibraryStyle(!isListingReportStyles());

        if (irc instanceof TemplateStyle)
        {
            TemplateStyle ts = (TemplateStyle)irc;
            if (ts.getJasperTemplate().getParent() != null || !ts.getJasperTemplate().canWrite())
            {
                cd.setReadOnly(true);
            }
        }
        
        if (irc instanceof UndefinedStyle)
        {
            cd.setLibraryStyle(true);
            cd.setReadOnly(true);
        }
        
        cd.setVisible(true);
        
        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            Style sub = cd.getStyle();
            // Now we have an old and a new object.
            // 1. Adjust table...
            try {
                // getListModel().setElementAt(sub, index);
                selectedNode.setUserObject(sub);
                getActiveList().updateUI();
            } catch (Exception ex) { return; } 
            
            if (selectedObject instanceof TemplateStyle)
            {
                try {
                    ((it.businesslogic.ireport.TemplateStyle) selectedObject).getJasperTemplate().saveTemplateFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (!isListingReportStyles())
            {
                MainFrame.getMainInstance().saveStyleLibrary();
            }
            getActiveList().updateUI();
        }
        
    }//GEN-LAST:event_jButtonEditStyleActionPerformed

    private void jButtonAddStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddStyleActionPerformed

        StyleDialog cd = new StyleDialog(MainFrame.getMainInstance(),true);
        
        if (!isListingReportStyles())
        {
            cd.setLibraryStyle(true);
        }
        else
        {
            if (getActiveReportFrame() == null) return;
        }
        
        
        
        cd.setVisible(true);
        
        if (cd.getDialogResult() == JOptionPane.OK_OPTION && cd.isLibraryStyle())
        {
            // If we are editing/adding a library style, no events are fire, so we have
            // to handle the new style by our self...
            // The new style should be already present in library.
            //getListModel().addElement( cd.getStyle() );
            DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode( cd.getStyle() );
            ((DefaultMutableTreeNode)getActiveList().getModel().getRoot()).add(dmtn );
            getActiveList().updateUI();
            MainFrame.getMainInstance().saveStyleLibrary();
        }
        
    }//GEN-LAST:event_jButtonAddStyleActionPerformed

    public void languageChanged(LanguageChangedEvent evt) {
    
          jButtonAddStyle.setToolTipText( I18n.getString("gui.style.newStyle","New Style") );
          jButtonEditStyle.setToolTipText( I18n.getString("gui.style.editStyle","Edit Style")  );
          jButtonDeleteStyle.setToolTipText( I18n.getString("gui.style.deleteStyle","Delete Style")  );
          
          jMenuItemNewStyle.setText( I18n.getString("gui.style.newStyle","New Style") );
          jMenuItemEditStyle.setText( I18n.getString("gui.style.editStyle","Edit Style")  );
          jMenuItemRemove.setText( I18n.getString("gui.style.remove","Remove")  );
          
          jMenuItemNewTemplateItem.setText( I18n.getString("gui.style.newTemaplateItem","New Template Item") );
          jMenuItemEditTemplate.setText( I18n.getString("gui.style.editTemaplateItem","Edit Template Item")  );
          
          for (int i=0; i<jComboBoxStyleType.getItemCount(); ++i)
          {
              Tag t = (Tag)jComboBoxStyleType.getItemAt(i);
              if ( t.getName().equals("reportStyles"))
                  t.setName(  I18n.getString("gui.style.types.reportStyles","Report Styles") );
              else if ( t.getName().equals("libraryStyles"))
                  t.setName(  I18n.getString("gui.style.types.libraryStyles","Styles Library") );
              
          }
    
    }

    public void reportFrameActivated(ReportFrameActivatedEvent evt) {
        setActiveReportFrame( evt.getReportFrame() );
    }

    public JReportFrame getActiveReportFrame() {
        return activeReportFrame;
    }

    public void setActiveReportFrame(JReportFrame newActiveReportFrame) {
        
        if (newActiveReportFrame != this.activeReportFrame)
        {
            if (this.activeReportFrame != null)
            {
                this.activeReportFrame.getReport().removeStyleChangedListener( this );
            }
        }
        else
        {
            return;
        }
        
        this.activeReportFrame  = newActiveReportFrame;
        
        if (this.activeReportFrame != null)
        {
            this.activeReportFrame.getReport().addStyleChangedListener( this );
        }
        
        refreshReportStyles(); 
        
    }
    
    
    public void updateStyles()
    {
        //((DefaultMutableTreeNode)jListStyles.getModel().getRoot()).removeAllChildren();
        
        jListStyles.setShowLibrary( !isListingReportStyles() );
        
        
        if ( ((Tag)jComboBoxStyleType.getSelectedItem()).getValue().equals("libraryStyles"))
        {
            remove(jScrollPaneReportList);
            add(jScrollPaneLibraryList, BorderLayout.CENTER);
            
            jListStylesLibraryValueChanged(null);
            
            //jListStyles.setModel(libraryTreeModel);
            // load library styles....
            
            /*
            for (JasperTemplate jt : openedTemplates)
            {
               DefaultMutableTreeNode node = new DefaultMutableTreeNode( jt );
                ((DefaultMutableTreeNode)jListStyles.getModel().getRoot()).add(node); 
            }
            */
        }
        else if ( ((Tag)jComboBoxStyleType.getSelectedItem()).getValue().equals("reportStyles"))
        {
            remove(jScrollPaneLibraryList);
            add(jScrollPaneReportList, BorderLayout.CENTER);
            
            jListStylesValueChanged(null);
        }
        
        updateUI();
    }
    
    

    /*
    protected DefaultListModel getListModel() {
        return listModel;
    }

    protected void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }
    */

    public void styleChanged(StyleChangedEvent evt) {
        
        if (evt.getSource() == this) return;
        refreshReportStyles();
    }
    
    
    public void templateChanged(TemplateChangedEvent evt) {
        
        if (evt.getSource() == this) return;
        refreshReportStyles();
    }
    
    
    
    /**
     * Return true if the currently displayed list refers to the report styles
     * False = displaying library styles (currently not modifiables...)
     */
    public boolean isListingReportStyles()
    {
        Tag t = (Tag)jComboBoxStyleType.getSelectedItem();
        
        if (t != null && t.getValue().equals("reportStyles"))
        {
            return true;
        }
        
        return false;
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddStyle;
    private javax.swing.JButton jButtonDeleteStyle;
    private javax.swing.JButton jButtonEditStyle;
    private javax.swing.JComboBox jComboBoxStyleType;
    private it.businesslogic.ireport.gui.style.JBGTree jListStyles;
    private it.businesslogic.ireport.gui.style.JBGTree jListStylesLibrary;
    private javax.swing.JMenuItem jMenuItemAddStyleToJT;
    private javax.swing.JMenuItem jMenuItemAddTemplateToJT;
    private javax.swing.JMenuItem jMenuItemCloseTemplate;
    private javax.swing.JMenuItem jMenuItemEditJTemplate;
    private javax.swing.JMenuItem jMenuItemEditStyle;
    private javax.swing.JMenuItem jMenuItemEditTemplate;
    private javax.swing.JMenuItem jMenuItemNewStyle;
    private javax.swing.JMenuItem jMenuItemNewStyleTemplate;
    private javax.swing.JMenuItem jMenuItemNewTemplateItem;
    private javax.swing.JMenuItem jMenuItemOpenStyleTemplate;
    private javax.swing.JMenuItem jMenuItemRemove;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPaneLibraryList;
    private javax.swing.JScrollPane jScrollPaneReportList;
    private javax.swing.JScrollPane jScrollPaneReportList1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
    }
    
    public JBGTree getActiveList()
    {
        return (isListingReportStyles()) ? jListStyles : jListStylesLibrary;
    }
}
