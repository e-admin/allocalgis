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
 * TotalObjectDialog.java
 * 
 * Created on 21 settembre 2004, 20.34
 *
 */

package it.businesslogic.ireport.gui.library.objects;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.gui.DocumentExpressionEditorTreeCellRenderer;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.library.CustomExpression;
import it.businesslogic.ireport.util.I18n;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author  Administrator
 */
public class TotalObjectDialog extends javax.swing.JDialog {
    
    int dialogResult = 0;
    DefaultMutableTreeNode fieldsNode = null;
    DefaultMutableTreeNode variablesNode = null;
    DefaultMutableTreeNode parametersNode = null; 
    
    private boolean onlyFields = false;
    
    private JReportFrame jrf = null;
    private Object selectedObject = null;
    /** Creates new form TotalObjectDialog */
    public TotalObjectDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.setSize(400, 450);
      
        this.setModal(true);
        
        
        org.syntax.jedit.SyntaxDocument sd = new org.syntax.jedit.SyntaxDocument();
        sd.setTokenMarker(new org.syntax.jedit.tokenmarker.JavaTokenMarker() );
      
        this.jRTextExpressionAreaDefaultExpression.setDocument( sd );
      
        jTree1.setCellRenderer( new DocumentExpressionEditorTreeCellRenderer());
        updateAllTree();

        this.dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
        
        it.businesslogic.ireport.util.Misc.centerFrame(this);     
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        applyI18n();
        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }
    
    
    public void updateAllTree()
    {
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Libs");
        
        DefaultTreeModel dtm = new DefaultTreeModel(root);
        
        jTree1.setModel( dtm );
        jTree1.setDragEnabled(true);
        jTree1.setRootVisible( false );
        
        jTree1.expandPath( new TreePath(root));

        fieldsNode = new DefaultMutableTreeNode( it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields"),true);
        root.add( fieldsNode );
        
        if (!isOnlyFields())
        {
            variablesNode = new DefaultMutableTreeNode( it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables"),true);
            root.add( variablesNode );
        
            parametersNode = new DefaultMutableTreeNode( it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters"),true);
            root.add( parametersNode );
            root.add( new DefaultMutableTreeNode( new it.businesslogic.ireport.gui.library.CustomExpression( it.businesslogic.ireport.util.I18n.getString("gui.library.customexpression","Custom expression")), true));
        }
        jLabelTitle.setText( it.businesslogic.ireport.util.I18n.getString("gui.library.totalobject.title","Select object to sum") );
        
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jRTextExpressionAreaDefaultExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabelTitle.setText("Select object to sum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jLabelTitle, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(81, 180));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jRTextExpressionAreaDefaultExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaDefaultExpression.setElectricScroll(0);
        jRTextExpressionAreaDefaultExpression.setMinimumSize(new java.awt.Dimension(0, 60));
        jRTextExpressionAreaDefaultExpression.setPreferredSize(new java.awt.Dimension(310, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jRTextExpressionAreaDefaultExpression, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonOK, new java.awt.GridBagConstraints());

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        
        if (selectedObject == null)
        {
            javax.swing.JOptionPane.showMessageDialog(this,it.businesslogic.ireport.util.I18n.getString("gui.library.totalobject.selectanobject","Select an object to sum first"));
            return;
        }
        
        this.dialogResult = javax.swing.JOptionPane.OK_OPTION;
        this.setVisible(false);
        dispose();
        
        
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // TODO add your handling code here:
        
        selectedObject = null;
        
        if (jTree1.getLastSelectedPathComponent() != null)
        {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
            Object obj = dmtn.getUserObject();
            if (!dmtn.isLeaf())
            {
                return;
            }
            
            if (obj instanceof CustomExpression)
            {
                //jRTextExpressionAreaDefaultExpression.setText( obj+"");
                jRTextExpressionAreaDefaultExpression.setEditable( true );
                jRTextExpressionAreaDefaultExpression.setBackground(java.awt.Color.WHITE);
                jRTextExpressionAreaDefaultExpression.setOpaque(true);
            }
            else 
            {
                if (obj instanceof JRParameter) jRTextExpressionAreaDefaultExpression.setText("$P{" + obj + "}");
                if (obj instanceof JRVariable) jRTextExpressionAreaDefaultExpression.setText("$V{" + obj + "}");
                if (obj instanceof JRField) jRTextExpressionAreaDefaultExpression.setText("$F{" + obj + "}");
                jRTextExpressionAreaDefaultExpression.setEditable(false);
                
                jRTextExpressionAreaDefaultExpression.setBackground(java.awt.Color.GRAY);
                jRTextExpressionAreaDefaultExpression.setOpaque(true);
            }
            selectedObject = obj;
            
        }
        
    }//GEN-LAST:event_jTree1ValueChanged
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TotalObjectDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    /**
     * Getter for property jrf.
     * @return Value of property jrf.
     */
    public JReportFrame getJrf() {

        return this.jrf;
    }

    /**
     * Setter for property jrf.
     * @param jrf New value of property jrf.
     */
    public void setJrf(JReportFrame jrf) {

        this.jrf = jrf;
        jRTextExpressionAreaDefaultExpression.getTokenMarker().setKeywordLookup(
        jrf.getReport().getKeywordLookup());
        
        this.fieldsNode.removeAllChildren();
       this.parametersNode.removeAllChildren();
       this.variablesNode.removeAllChildren();
       
       
       if (jrf == null) {
           
           jTree1.updateUI();
           return;
       }
       
       java.util.Enumeration e = jrf.getReport().getFields().elements();
       while (e.hasMoreElements())
       {
             fieldsNode.add(new DefaultMutableTreeNode(e.nextElement()));
       }
       
       if (!isOnlyFields())
       {
            e = jrf.getReport().getParameters().elements();
           while (e.hasMoreElements())
           {
                 parametersNode.add(new DefaultMutableTreeNode(e.nextElement()));
           }

            e = jrf.getReport().getVariables().elements();
           while (e.hasMoreElements())
           {
                 variablesNode.add(new DefaultMutableTreeNode(e.nextElement()));
           }
       }
        
       jTree1.updateUI();
    }

    /**
     * Getter for property dialogResult.
     * @return Value of property dialogResult.
     */
    public int getDialogResult() {

        return this.dialogResult;
    }

    /**
     * Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     */
    public void setDialogResult(int dialogResult) {

        this.dialogResult = dialogResult;
    }

    /**
     * Getter for property selectedObject.
     * @return Value of property selectedObject.
     */
    public Object getSelectedObject() {

        return this.selectedObject;
    }

    /**
     * Setter for property selectedObject.
     * @param selectedObject New value of property selectedObject.
     */
    public void setSelectedObject(Object selectedObject) {

        this.selectedObject = selectedObject;
    }

    public boolean isOnlyFields() {
        return onlyFields;
    }

    public void setOnlyFields(boolean onlyFields) {
        this.onlyFields = onlyFields;
        updateAllTree();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaDefaultExpression;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("totalObjectDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("totalObjectDialog.buttonOK","OK"));
                // End autogenerated code ----------------------
                jButtonCancel.setMnemonic(I18n.getString("totalObjectDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("totalObjectDialog.buttonOKMnemonic","o").charAt(0));
    }
}
