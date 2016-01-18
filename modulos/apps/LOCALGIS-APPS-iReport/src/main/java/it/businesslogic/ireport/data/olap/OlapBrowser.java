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
 * OlapBrowser.java
 * 
 * Created on June 20, 2006, 4:36 PM
 *
 */

package it.businesslogic.ireport.data.olap;

import it.businesslogic.ireport.FieldsProviderEditor;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.ReportQueryDialog;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.Axis;
import mondrian.olap.AxisOrdinal;
import mondrian.olap.Hierarchy;
import mondrian.olap.Level;
import mondrian.olap.Query;
import mondrian.olap.QueryAxis;
import mondrian.olap.QueryPart;
import mondrian.olap.Result;
/**
 *
 * @author  gtoffoli
 */
public class OlapBrowser extends javax.swing.JPanel implements FieldsProviderEditor {
    
    private JTable jTableFields = null; 
    private ReportQueryDialog reportQueryDialog = null;
    
    /** Creates new form OlapBrowser */
    public OlapBrowser() {
        initComponents();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        jTree1.setModel(new DefaultTreeModel(root));
        jTree1.setCellRenderer( new OlapTreeCellRenderer());
        
        jComboBoxType.addItem(new Tag("java.lang.String",I18n.getString("mdx.type.Text", "Text") ));
        jComboBoxType.addItem(new Tag("java.lang.Number", I18n.getString("mdx.type.Numeric", "Numeric") ));
        jComboBoxType.addItem(new Tag("java.util.Date", I18n.getString("mdx.type.Date", "Date")));
        jComboBoxType.addItem(new Tag("java.util.Date", I18n.getString("mdx.type.Boolean", "Boolean") ));
        
        jComboBoxType.setSelectedIndex(0);
        
        applyI18n();
        
        ((DefaultTreeSelectionModel)jTree1.getSelectionModel()).setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION );
    }
    
    
    public void setOlapQuery(Query query)
    {
        
        
        DefaultTreeModel dtm = (DefaultTreeModel)jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
        root.removeAllChildren();
        
        if (query == null) 
        {
            jTree1.updateUI();
            return;
        }
        
	QueryAxis[] axes = query.getAxes();
        
        Hierarchy[][] queryHierarchies = new Hierarchy[axes.length][];
        int[][] fieldsMaxDepths = new int[axes.length][];
        int[][] maxDepths = new int[axes.length][];
        
        int hCount = 0;
        for (int i = 0; i < axes.length; i++)
        {
                queryHierarchies[i] = query.getMdxHierarchiesOnAxis(AxisOrdinal.forLogicalOrdinal(i));
                
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(axes[i],root));
                
                if ( queryHierarchies[i].length == 1 &&
                     queryHierarchies[i][0].getDimension().isMeasures())
                {
                    addChildren(childNode, axes[i].getChildren());
                }
                else
                {
                    addChildren(childNode, queryHierarchies[i]);
                }
                
                root.add(childNode );
        
                hCount += queryHierarchies[i].length;
                fieldsMaxDepths[i] = new int[queryHierarchies[i].length];
                maxDepths[i] = new int[queryHierarchies[i].length];
        }
        
        jTree1.updateUI();
    }
    
    public void setOlapResult2(Result result)
    {
        DefaultTreeModel dtm = (DefaultTreeModel)jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
        root.removeAllChildren();
        
        if (result == null) 
        {
            jTree1.updateUI();
            return;
        }
        
        // Main axes...
        
        
        QueryAxis[] axis = result.getQuery().getAxes();
        Axis[] raxis = result.getAxes();
        
        for (int i=0; i<axis.length; ++i)
        {   
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(axis[i], root));
            addChildren(childNode, axis[i].getChildren());
            root.add(childNode );
        }
        
        jTree1.updateUI();
    }
    
    public void setOlapResultDataXXX(Result result)
    {
        DefaultTreeModel dtm = (DefaultTreeModel)jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
        root.removeAllChildren();
        
        if (result == null) 
        {
            jTree1.updateUI();
            return;
        }
        
        // Main axes...
        
        
        QueryAxis[] axis = result.getQuery().getAxes();
        Axis[] raxis = result.getAxes();
        
        for (int i=0; i<axis.length; ++i)
        {   
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(axis[i], root));
            Object[] positions = raxis[i].getPositions().toArray();
            addChildren(childNode, positions);
            root.add(childNode );
        }
        
        jTree1.updateUI();
    }
    

    String r_level = "";
    public void addChildren(DefaultMutableTreeNode node, Object[] children)
    {
        
        if (children == null) return;
        
        r_level = r_level + "__";
        for (int i=0; i<children.length; ++i)
        {
            System.out.println(r_level + children[i].getClass().getName() + " > " + children[i] + " Parent: " + node);
            
            if (children[i] instanceof mondrian.mdx.MemberExpr)
            {
                
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(((mondrian.mdx.MemberExpr)children[i]).getMember(), node));
                node.add(childNode );
                System.out.println("Adding MemberExpr: " + childNode);
            }
            else if (children[i] instanceof ResolvedFunCall) // FunCall interface no longes has the method getChildren...
            {
                addChildren( node, ((ResolvedFunCall)children[i]).getChildren());
            }
            else if (children[i] instanceof QueryPart)
	    {
                addChildren( node, ((QueryPart)children[i]).getChildren());
	    }
            else if (children[i] instanceof QueryPart)
	    {
                addChildren( node, ((QueryPart)children[i]).getChildren());
	    }
            else if (children[i] instanceof Hierarchy)
            {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(children[i], node));
                addChildren(childNode, ((Hierarchy)children[i]).getLevels());
                node.add(childNode );
            }
            else if (children[i] instanceof Level)
            {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(children[i], node));
                node.add(childNode );
            }
            //else
            /*
            if (children[i] instanceof Position) 
            {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new WalkableWrapper(children[i], node));
                if (children[i] instanceof Position)
                {
                    addChildren(childNode, ((Position)children[i]).getMembers());
                }
                else if (children[i] instanceof Hierarchy)
                {
                    addChildren(childNode, ((Hierarchy)children[i]).getLevels());
                }
                
                node.add(childNode );
            }
            
            if (children[i] instanceof Member)
            {
                addChildren(childNode, ((Member)children[i]).getChildren());
            }
            */
           
        }
        
        r_level = r_level.substring(0, r_level.length()-2);
    }

    public JTable getJTableFields() {
        return jTableFields;
    }

    public void setJTableFields(JTable jTableFields) {
        this.jTableFields = jTableFields;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldExpression = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(250, 85));
        setPreferredSize(new java.awt.Dimension(405, 384));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 322));
        jTree1.setRootVisible(false);
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        add(jScrollPane1, gridBagConstraints);

        jLabel4.setForeground(new java.awt.Color(0, 51, 255));
        jLabel4.setText("Double click on a tree element the get it's expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jLabel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Field name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jTextFieldName, gridBagConstraints);

        jLabel3.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jComboBoxType.setMinimumSize(new java.awt.Dimension(30, 19));
        jComboBoxType.setPreferredSize(new java.awt.Dimension(50, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 4);
        jPanel1.add(jComboBoxType, gridBagConstraints);

        jLabel2.setText("Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel1.add(jTextFieldExpression, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        jPanel2.add(jButton2, gridBagConstraints);

        jButton1.setText("Add field");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        jPanel2.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        jTextFieldName.setText("");
        jComboBoxType.setSelectedIndex(0);
        jTextFieldExpression.setText("");
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jTextFieldName.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this, I18n.getString("message.olapBrowser.notValidFieldName","Field name not valid"),I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
            return;
            
        }
        if (jTextFieldExpression.getText().length() == 0)
        {
            JOptionPane.showMessageDialog(this,I18n.getString("message.olapBrowser.notValidFieldExpression","Field expression not valid"), I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JRField field = new JRField(jTextFieldName.getText().trim(), ""+((Tag)jComboBoxType.getSelectedItem()).getValue() );
        field.setDescription( jTextFieldExpression.getText() );
        
       /*  
        try
        {
                MappingLexer lexer = new MappingLexer(new StringReader(jTextFieldExpression.getText()));
                
                //MappingParser parser = new MappingParser(lexer);
                //parser.setMappingMetadata(this);
                parser.mapping();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,"Field expression not valid:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        */                
        
        
        if (fieldAlreadyExists(field))
        {
            JOptionPane.showMessageDialog(this,I18n.getString("message.olapBrowser.fieldDuplicated","A field with this name is already present"), I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Vector row = new Vector();
        row.addElement(field);
        row.addElement(field.getClassType());
        row.addElement(field.getDescription());
        
        DefaultTableModel dtm = (DefaultTableModel)getJTableFields().getModel();
        dtm.addRow(row);
        getJTableFields().getSelectionModel().addSelectionInterval( getJTableFields().getRowCount()-1, getJTableFields().getRowCount()-1 );
            
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            TreePath tp = jTree1.getSelectionPath();
            if (tp != null)
            {
                WalkableWrapper ww = (WalkableWrapper)((DefaultMutableTreeNode)tp.getLastPathComponent()).getUserObject();
                
                String exp = ww.getExpression();
                if (exp != null)
                {
                    if (ww.isMeasure())
                    {
                        jTextFieldName.setText( ww+"");
                        jTextFieldExpression.setText( exp);
                    }
                    else
                    {
                        String newexp = jTextFieldExpression.getText();
                        int ss = ( jTextFieldExpression.getSelectionStart() >= 0 ) ? jTextFieldExpression.getSelectionStart() : jTextFieldExpression.getCaretPosition();
                        int se = ( jTextFieldExpression.getSelectionEnd() >= 0 ) ? jTextFieldExpression.getSelectionEnd() : jTextFieldExpression.getCaretPosition();
                        newexp = newexp.substring(0,ss) + exp + newexp.substring(se);
                        jTextFieldExpression.setText( newexp);
                        jTextFieldExpression.requestFocusInWindow();
                    }
                }
            }
            
            
            
        }
    }//GEN-LAST:event_jTree1MouseClicked
    
    
    private boolean fieldAlreadyExists(JRField field)
    {
        boolean found = false;
            for (int j=0; j<getJTableFields().getRowCount(); ++j)
            {
               Object ff = getJTableFields().getValueAt(j, 0);
               if ( ff instanceof it.businesslogic.ireport.JRField )
               {
                   if ( ((it.businesslogic.ireport.JRField)ff).getName().equals(field.getName()))
                   {
                       found = true;
                       break;
                   }
               }
            }
        
        return found;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldExpression;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButton1.setText(I18n.getString("olapBrowser.button1","Add field"));
                jButton2.setText(I18n.getString("olapBrowser.button2","Clear"));
                jLabel1.setText(I18n.getString("olapBrowser.label1","Field name"));
                jLabel2.setText(I18n.getString("olapBrowser.label2","Expression"));
                jLabel3.setText(I18n.getString("olapBrowser.label3","Type"));
                jLabel4.setText(I18n.getString("olapBrowser.label4","Double click on a tree element the get it's expression"));
                // End autogenerated code ----------------------
    }

    
    int lastExecution = 0;
    
    public void queryChanged(String newQuery) {
    
        lastExecution++;
        int thisExecution = lastExecution;
        // Execute a thread to perform the query change...
        
        String error_msg = "";
        lastExecution++;
            
        int in = lastExecution;
            
        getReportQueryDialog().getJLabelStatusSQL().setText("Executing MDX query....");
        /////////////////////////////
            
        try {
        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
            
        if (in < lastExecution) return; //Abort, new execution requested
        
        OLAPQueryExecuter olapQE = new OLAPQueryExecuter(newQuery, getReportQueryDialog().getSubDataset().getParameters());
        
            String lastError = "";
            try {
                
                //final mondrian.olap.Result result = olapQE.executeOlapQuery();
                final mondrian.olap.Query query = olapQE.createOlapQuery();
                
                if (in < lastExecution) return; //Abort, new execution requested
                SwingUtilities.invokeAndWait( new Runnable() {
                    public void run() {
                        setOlapQuery( query );
                    }
                } );
                
            } catch (Exception ex)
            {
                ex.printStackTrace();
                setOlapQuery( null );
                lastError = " (Last error was " +  ex.getMessage() +")";
            }
        
        getReportQueryDialog().getJLabelStatusSQL().setText("Ready");
    }

    public ReportQueryDialog getReportQueryDialog() {
        return reportQueryDialog;
    }

    public void setReportQueryDialog(ReportQueryDialog reportQueryDialog) {
        this.reportQueryDialog = reportQueryDialog;
    }
}
