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
 * JRVariableDialog.java
 * 
 * Created on 9 maggio 2003, 17.25
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.*;


/**
 * @author Administrator
 */
public class JRVariableDialog
    extends javax.swing.JDialog
{

    /** Creates new form JRParameterDialog */
    JRVariable tmpVariable = null;
    private SubDataset subDataset = null;
    private String originalName = null;

    /**
     * Creates a new JRVariableDialog object.
     * 
     * @param subDataset DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public JRVariableDialog(SubDataset report, java.awt.Frame parent, 
                            boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.setSubDataset(report);
        updateGroups();

        this.jRTextExpressionAreaExpression.setText("");
        this.jRTextExpressionAreaInitialExpression.setText("");
        setTypes();
        this.jComboBoxClassType.setSelectedItem("java.lang.String");
        
        Misc.setComboboxSelectedTagValue( jComboBoxCalculationType, "Nothing" );
        Misc.setComboboxSelectedTagValue( jComboBoxResetType, "Report" );
        Misc.setComboboxSelectedTagValue( jComboBoxIncrementType, "None" );

        updateGroups();
        
        
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
        
        pack();
    }

    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_VARIABLE_EXPRESSION=1;
    public static final int COMPONENT_VARIABLE_INIT_EXPRESSION=2;

    /**
     * This method set the focus on a specific component.
     * Valid constants are something like:
     * FIELD_XXX
     */
    public void setFocusedExpression(int expID )
    {
        switch (expID)
        {
            case COMPONENT_VARIABLE_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionAreaExpression);
                break;
            case COMPONENT_VARIABLE_INIT_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionAreaInitialExpression);
                break;
        }
        
    }
    /**
     * DOCUMENT ME!
     */
    public void setTypes()
    {
        this.jComboBoxClassType.addItem("java.lang.String");
        this.jComboBoxClassType.addItem("java.lang.Object");
        this.jComboBoxClassType.addItem("java.lang.Boolean");
        this.jComboBoxClassType.addItem("java.lang.Byte");
        this.jComboBoxClassType.addItem("java.util.Date");
        this.jComboBoxClassType.addItem("java.sql.Timestamp");
        this.jComboBoxClassType.addItem("java.sql.Time");
        this.jComboBoxClassType.addItem("java.lang.Double");
        this.jComboBoxClassType.addItem("java.lang.Float");
        this.jComboBoxClassType.addItem("java.lang.Integer");
        this.jComboBoxClassType.addItem("java.io.InputStream");
        this.jComboBoxClassType.addItem("java.lang.Long");
        this.jComboBoxClassType.addItem("java.lang.Short");
        this.jComboBoxClassType.addItem("java.math.BigDecimal");
        this.jComboBoxClassType.addItem(
                "net.sf.jasperreports.engine.JREmptyDataSource");

        this.jComboBoxCalculationType.addItem(new Tag("Nothing", I18n.getString("variable.calculationType.Nothing", "Nothing") ));
        this.jComboBoxCalculationType.addItem(new Tag("Count", I18n.getString("variable.calculationType.Count", "Count")));
        this.jComboBoxCalculationType.addItem(new Tag("DistinctCount", I18n.getString("variable.calculationType.DistinctCount", "Distinct count")));
        this.jComboBoxCalculationType.addItem(new Tag("Sum", I18n.getString("variable.calculationType.Sum", "Sum")));
        this.jComboBoxCalculationType.addItem(new Tag("Average", I18n.getString("variable.calculationType.Average", "Average")));
        this.jComboBoxCalculationType.addItem(new Tag("Lowest", I18n.getString("variable.calculationType.Lowest", "Lowest")));
        this.jComboBoxCalculationType.addItem(new Tag("Highest", I18n.getString("variable.calculationType.Highest", "Highest")));
        this.jComboBoxCalculationType.addItem(new Tag("StandardDeviation", I18n.getString("variable.calculationType.StandardDeviation", "Standard deviation")));
        this.jComboBoxCalculationType.addItem(new Tag("Variance", I18n.getString("variable.calculationType.Variance", "Variance") ));
        this.jComboBoxCalculationType.addItem(new Tag("System", I18n.getString("variable.calculationType.System", "System") ));
        this.jComboBoxCalculationType.addItem(new Tag("First", I18n.getString("variable.calculationType.First", "First") ));
        
        this.jComboBoxResetType.addItem(new Tag("None",it.businesslogic.ireport.util.I18n.getString("resetType.None","None")));
        this.jComboBoxResetType.addItem(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("resetType.Report","Report")));
        this.jComboBoxResetType.addItem(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("resetType.Page","Page")));
        this.jComboBoxResetType.addItem(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("resetType.Column","Column")));
        this.jComboBoxResetType.addItem(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("resetType.Group","Group")));

        this.jComboBoxIncrementType.addItem(new Tag("None",it.businesslogic.ireport.util.I18n.getString("incrementType.None","None")));
        this.jComboBoxIncrementType.addItem(new Tag("Report",it.businesslogic.ireport.util.I18n.getString("incrementType.Report","Report")));
        this.jComboBoxIncrementType.addItem(new Tag("Page",it.businesslogic.ireport.util.I18n.getString("incrementType.Page","Page")));
        this.jComboBoxIncrementType.addItem(new Tag("Column",it.businesslogic.ireport.util.I18n.getString("incrementType.Column","Column")));
        this.jComboBoxIncrementType.addItem(new Tag("Group",it.businesslogic.ireport.util.I18n.getString("incrementType.Group","Group")));
        
    }

    /**
     * DOCUMENT ME!
     */
    public void updateGroups()
    {

        if (getSubDataset() == null)
	{
            jComboBoxResetGroup.removeAllItems();
            jComboBoxResetGroup.addItem("");
        }	
        else
        {
            Misc.updateStringComboBox(jComboBoxResetGroup, 
                                      getSubDataset().getGroups(), false);
            Misc.updateStringComboBox(jComboBoxIncrementGroup, 
                                      getSubDataset().getGroups(), false);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */


    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jComboBoxCalculationType = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxClassType = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jRTextExpressionAreaExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jRTextExpressionAreaInitialExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldNameIncrementerFactoryClass = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Add/modify variable");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Variable name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jTextFieldName, gridBagConstraints);

        jLabel2.setText("Calculation type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxResetGroup, gridBagConstraints);

        jLabel3.setText("Variable expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Initial value expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText("Variable class type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel5, gridBagConstraints);

        jComboBoxResetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxResetType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxCalculationType, gridBagConstraints);

        jLabel6.setText("Reset group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel6, gridBagConstraints);

        jComboBoxClassType.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxClassType, gridBagConstraints);

        jLabel7.setText("Reset type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel7, gridBagConstraints);

        jRTextExpressionAreaExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaExpression.setCaretVisible(false);
        jRTextExpressionAreaExpression.setElectricScroll(0);
        jRTextExpressionAreaExpression.setMinimumSize(new java.awt.Dimension(657, 100));
        jRTextExpressionAreaExpression.setNextFocusableComponent(jRTextExpressionAreaInitialExpression);
        jRTextExpressionAreaExpression.setPreferredSize(new java.awt.Dimension(325, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jRTextExpressionAreaExpression, gridBagConstraints);

        jRTextExpressionAreaInitialExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaInitialExpression.setCaretVisible(false);
        jRTextExpressionAreaInitialExpression.setElectricScroll(0);
        jRTextExpressionAreaInitialExpression.setFont(new java.awt.Font("Tahoma", 0, 12));
        jRTextExpressionAreaInitialExpression.setMinimumSize(new java.awt.Dimension(657, 100));
        jRTextExpressionAreaInitialExpression.setPreferredSize(new java.awt.Dimension(325, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jRTextExpressionAreaInitialExpression, gridBagConstraints);

        jLabel8.setText("Custom Incrementer Factory Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel8, gridBagConstraints);

        jTextFieldNameIncrementerFactoryClass.setNextFocusableComponent(jRTextExpressionAreaExpression);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jTextFieldNameIncrementerFactoryClass, gridBagConstraints);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonOK.setMnemonic('o');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonOK);

        jButtonCancel.setText("Cancel");
        jButtonCancel.setMnemonic('c');
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanel1.add(jButtonCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel9.setText("Increment type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel9, gridBagConstraints);

        jLabel10.setText("Increment group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel10, gridBagConstraints);

        jComboBoxIncrementType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxIncrementType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxIncrementGroup, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed

        Object t = jComboBoxIncrementType.getSelectedItem();
        String val = t+"";
        
        if (t != null && t instanceof Tag)
        {
            val = ""+((Tag)t).getValue();
        }
        
        jComboBoxIncrementGroup.setEnabled( val.equals("Group") );
        
        if (jComboBoxIncrementGroup.isEnabled() &&
            jComboBoxIncrementGroup.getSelectedItem() == null &&
            jComboBoxIncrementGroup.getItemCount() > 0)
        {
            jComboBoxIncrementGroup.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed

        Object t = jComboBoxResetType.getSelectedItem();
        String val = t+"";
        
        if (t != null && t instanceof Tag)
        {
            val = ""+((Tag)t).getValue();
        }
        
        
        jComboBoxResetGroup.setEnabled( val.equals("Group") );
        
        if (jComboBoxResetGroup.isEnabled() &&
            jComboBoxResetGroup.getSelectedItem() == null &&
            jComboBoxResetGroup.getItemCount() > 0)
        {
            jComboBoxResetGroup.setSelectedIndex(0);
        }
        
    }//GEN-LAST:event_jComboBoxResetTypeActionPerformed
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonCancelActionPerformed
    {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonOKActionPerformed
    {

        if (this.jTextFieldName.getText().trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString( "messages.jRVariableDialog.notValidName","Please insert a valid variable name!"),
                    I18n.getString( "messages.jRVariableDialog.notValidNameCaption","Invalid variable!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        tmpVariable = new JRVariable(this.jTextFieldName.getText(), false);
        tmpVariable.setClassType(
                this.jComboBoxClassType.getSelectedItem() + "");
        
        Object t = this.jComboBoxCalculationType.getSelectedItem();
        if (t != null && t instanceof Tag) tmpVariable.setCalculation(  ((Tag)t).getValue()+"" );

        t = this.jComboBoxResetType.getSelectedItem();
        if (t != null && t instanceof Tag) tmpVariable.setResetType(  ((Tag)t).getValue()+"" );
        

        t = this.jComboBoxIncrementType.getSelectedItem();
        if (t != null && t instanceof Tag) tmpVariable.setIncrementType(  ((Tag)t).getValue()+"" );


        //System.out.println(this.jComboBoxIncrementType.getSelectedItem()+"  "+ tmpVariable.getIncrementType());
        tmpVariable.setIncrementerFactoryClass(this.jTextFieldNameIncrementerFactoryClass.getText());

        if (this.jComboBoxClassType.getSelectedItem().toString().trim().length() == 0)
	{
            tmpVariable.setClassType("java.lang.String");
        }

        else
        {
            tmpVariable.setClassType(this.jComboBoxClassType.getSelectedItem().toString().trim());
        }
        
        if (tmpVariable.getResetType().equals("Group"))
        {

            if (this.jComboBoxResetGroup.getSelectedItem() == null || 
                this.jComboBoxResetGroup.getSelectedItem().equals(""))
            {
                javax.swing.JOptionPane.showMessageDialog(this, 
                        I18n.getString( "messages.jRVariableDialog.notValidGroup","Please choose a valid group for the reset type selected!"),
                        I18n.getString( "messages.jRVariableDialog.notValidGroupCaption","Invalid group!"),
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                return;
            }

            tmpVariable.setResetGroup(
                    this.jComboBoxResetGroup.getSelectedItem() + "");
        }

        if (tmpVariable.getIncrementType().equals("Group"))
        {

            if (this.jComboBoxIncrementGroup.getSelectedItem() == null || 
                this.jComboBoxIncrementGroup.getSelectedItem().equals(""))
            {
                javax.swing.JOptionPane.showMessageDialog(this, 
                        I18n.getString( "messages.jRVariableDialog.notValidGroupIncrementType","Please choose a valid group for the increment type selected!"),
                        I18n.getString( "messages.jRVariableDialog.notValidGroupIncrementTypeCaption","Invalid group!"),
                        javax.swing.JOptionPane.WARNING_MESSAGE);

                return;
            }

            tmpVariable.setIncrementGroup(
                    this.jComboBoxIncrementGroup.getSelectedItem() + "");
        }
        
        if (getSubDataset() != null)
            {
                 //check unique name...
                 String newName = this.jTextFieldName.getText();
                 if (getOriginalName() == null || !getOriginalName().equals(newName))
                 {
                     for (int i=0; i< getSubDataset().getVariables().size(); ++i)
                     {
                         JRVariable f = (JRVariable)getSubDataset().getVariables().get(i);
                         if (f.getName().equals(newName))
                         {
                             javax.swing.JOptionPane.showMessageDialog(this,
                                I18n.getString( "messages.JRVariableDialog.DuplicatedVariableName","A variable with this name already exists!"),
                                I18n.getString( "messages.JRVariableDialog.notValidVariableNameCaption","Invalid variable!"),
                                javax.swing.JOptionPane.WARNING_MESSAGE );
                             return;
                         }
                     }
                 }
            }

        tmpVariable.setExpression(jRTextExpressionAreaExpression.getText());

        tmpVariable.setInitialValueExpression(jRTextExpressionAreaInitialExpression.getText());

        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed

    /**
     * Closes the dialog
     * @param evt DOCUMENT ME!
     */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new JRParameterDialog(new javax.swing.JFrame(), true).setVisible(true);
    }

    /**
     * Getter for property tmpParameter.
     * 
     * @return Value of property tmpParameter.
     */
    public it.businesslogic.ireport.JRVariable getVariable()
    {

        return tmpVariable;
    }

    /**
     * Setter for property tmpParameter.
     * 
     * @param tmpVariable New value of property tmpParameter.
     */
    public void setVariable(it.businesslogic.ireport.JRVariable tmpVariable)
    {
        originalName = tmpVariable.getName();
        this.jTextFieldName.setText(new String(tmpVariable.getName()));

        this.jComboBoxClassType.setSelectedItem(new String(tmpVariable.getClassType()));
        
        Misc.setComboboxSelectedTagValue( jComboBoxResetType, tmpVariable.getResetType() );
        Misc.setComboboxSelectedTagValue( jComboBoxIncrementType, tmpVariable.getIncrementType() );
        Misc.setComboboxSelectedTagValue( jComboBoxCalculationType, tmpVariable.getCalculation() );
        
        this.jComboBoxResetGroup.setSelectedItem(tmpVariable.getResetGroup());
        this.jComboBoxIncrementGroup.setSelectedItem(tmpVariable.getIncrementGroup());

        this.jRTextExpressionAreaExpression.setText(new String(tmpVariable.getExpression()));
        this.jRTextExpressionAreaInitialExpression.setText(new String(tmpVariable.getInitialValueExpression()));
        this.jTextFieldNameIncrementerFactoryClass.setText(new String(tmpVariable.getIncrementerFactoryClass()));
    }

    /**
     * Getter for property dialogResult.
     * 
     * @return Value of property dialogResult.
     */
    public int getDialogResult()
    {

        return dialogResult;
    }

    /**
     * Setter for property dialogResult.
     * 
     * @param dialogResult New value of property dialogResult.
     */
    public void setDialogResult(int dialogResult)
    {
        this.dialogResult = dialogResult;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBoxCalculationType;
    private javax.swing.JComboBox jComboBoxClassType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaInitialExpression;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldNameIncrementerFactoryClass;
    // End of variables declaration//GEN-END:variables
    private int dialogResult;

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
        jRTextExpressionAreaExpression.setSubDataset( subDataset);
        jRTextExpressionAreaInitialExpression.setSubDataset( subDataset);
        jRTextExpressionAreaExpression.getTokenMarker().setKeywordLookup(subDataset.getKeywordLookup());
        jRTextExpressionAreaInitialExpression.getTokenMarker().setKeywordLookup(subDataset.getKeywordLookup());
    }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRVariableDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRVariableDialog.buttonOK","OK"));
                jLabel1.setText(I18n.getString("jRVariableDialog.label1","Variable name"));
                jLabel10.setText(I18n.getString("jRVariableDialog.label10","Increment group"));
                jLabel2.setText(I18n.getString("jRVariableDialog.label2","Calculation type"));
                jLabel3.setText(I18n.getString("jRVariableDialog.label3","Variable expression"));
                jLabel4.setText(I18n.getString("jRVariableDialog.label4","Initial value expression"));
                jLabel5.setText(I18n.getString("jRVariableDialog.label5","Variable class type"));
                jLabel6.setText(I18n.getString("jRVariableDialog.label6","Reset group"));
                jLabel7.setText(I18n.getString("jRVariableDialog.label7","Reset type"));
                jLabel8.setText(I18n.getString("jRVariableDialog.label8","Custom Incrementer Factory Class"));
                jLabel9.setText(I18n.getString("jRVariableDialog.label9","Increment type"));
                // End autogenerated code ----------------------
                this.setTitle(I18n.getString("jRVariableDialog.title","Add/modify variable"));
                jButtonCancel.setMnemonic(I18n.getString("jRVariableDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRVariableDialog.buttonOKMnemonic","o").charAt(0));
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
