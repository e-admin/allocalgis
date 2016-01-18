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
 * ArithmeticOperationHelper.java
 *
 * Created on September 26, 2006, 9:46 AM
 *
 */

package it.businesslogic.ireport.gui.expbuilder;


import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author  gtoffoli
 */
public class ArithmeticOperationHelper extends javax.swing.JPanel {

    JDialog dialog = null;
    private String expression = null;
    private String language = "java";
    private int dialogResult = JOptionPane.CANCEL_OPTION;

    /** Creates new form BasicOperationWizard */
    public ArithmeticOperationHelper() {
        initComponents();
        applyI18n();
        jList1.setModel(new DefaultListModel());
        jList2.setModel(new DefaultListModel());

        jList1.setCellRenderer( new ExpObjectCellRenderer(jList1));
        jList2.setCellRenderer( new ExpObjectCellRenderer(jList2));


        jComboBoxType1.addItem( new Tag("auto", I18n.getString("autoType", "Automatic") ));
        jComboBoxType1.addItem( new Tag("int", I18n.getString("intType", "Integer number (i.e. 123)") ));
        jComboBoxType1.addItem( new Tag("double", I18n.getString("realType", "Real number (i.e. 123.456)") ));

        jComboBoxType2.addItem( new Tag("auto", I18n.getString("autoType", "Automatic") ));
        jComboBoxType2.addItem( new Tag("int", I18n.getString("intType", "Integer number (i.e. 123)") ));
        jComboBoxType2.addItem( new Tag("double", I18n.getString("realType", "Real number (i.e. 123.456)") ));

        jComboBoxReturnType.addItem(I18n.getString("autoType", "Automatic") );
        jComboBoxReturnType.addItem("BigDecimal");
        jComboBoxReturnType.addItem("BigInteger");
        jComboBoxReturnType.addItem("Byte");
        jComboBoxReturnType.addItem("Double");
        jComboBoxReturnType.addItem("Float");
        jComboBoxReturnType.addItem("Integer");
        jComboBoxReturnType.addItem("Long");
        jComboBoxReturnType.addItem("Short");

        updateAdvancedOptionsStatus(false);
    }

    public String getFinalReturnType()
    {
        jComboBoxReturnType.removeAllItems();
        if (jComboBoxType1.getSelectedIndex() == 0 &&
            jComboBoxType1.getSelectedIndex() == 0)
        {
            return "Integer";
        }
        else
        {
            return "Double";
        }
    }



    // Values can be a set of variables, parameters and fields
    public void setValues( java.util.List values )
    {
        if (values == null) return;
        int valids = 0;

        for (int i=0; i<values.size(); ++i)
        {
            ExpObject obj = (ExpObject)values.get(i);
            String type = obj.getClassType();
            if (type == null) continue;

            if (type.equals( "java.lang.Number") ||
                type.equals( "java.lang.BigDecimal") ||
                type.equals( "java.lang.BigInteger") ||
                type.equals( "java.lang.Byte") ||
                type.equals( "java.lang.Double") ||
                type.equals( "java.lang.Float") ||
                type.equals( "java.lang.Integer") ||
                type.equals( "java.lang.Long") ||
                type.equals( "java.lang.Short") )
            {
                ((DefaultListModel)jList1.getModel()).addElement(obj);
                ((DefaultListModel)jList2.getModel()).addElement(obj);
                valids++;
            }
        }

        if (valids > 0)
        {
            jList1.setSelectedIndex(0);
            jList2.setSelectedIndex(0);
        }
    }

    public void setOperation(String op)
    {
        jComboBoxOperation.setSelectedItem(op);
    }
    /**
     * Show the dialog.
     * if c is null, the dialog will not have a parent.
     */
    public int showDialog(Component c)
    {
        Window topLevel = null;
        if (c != null)
        {
           topLevel = SwingUtilities.getWindowAncestor(c);
        }

        if (topLevel == null)
        {
            dialog = new JDialog();
            dialog.setModal(true);
        }
        else if (topLevel instanceof Frame) {
            dialog = new JDialog((Frame)topLevel, true);
        }
        else if (topLevel instanceof Dialog) {
            dialog = new JDialog((Dialog)topLevel, true);
        }

        dialog.getContentPane().add(this);
        dialog.setDefaultCloseOperation( dialog.DISPOSE_ON_CLOSE );

        dialog.setTitle("Arithmetic operation" );
        dialog.pack();
        Misc.centerFrame(dialog);

        dialog.setVisible(true);

        return getDialogResult();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelValue1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabelAdv1 = new javax.swing.JLabel();
        jComboBoxType1 = new javax.swing.JComboBox();
        jComboBoxOperation = new javax.swing.JComboBox();
        jPanelValue2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabelAdv2 = new javax.swing.JLabel();
        jComboBoxType2 = new javax.swing.JComboBox();
        jPanelReturnType = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelReturnType = new javax.swing.JLabel();
        jComboBoxReturnType = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(673, 300));
        jPanelValue1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Value 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelValue1.add(jLabel1, gridBagConstraints);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelValue1.add(jScrollPane1, gridBagConstraints);

        jLabelAdv1.setText("Consider this number as:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelValue1.add(jLabelAdv1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelValue1.add(jComboBoxType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanelValue1, gridBagConstraints);

        jComboBoxOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "+", "-", "/", "*" }));
        jComboBoxOperation.setMinimumSize(new java.awt.Dimension(50, 18));
        jComboBoxOperation.setPreferredSize(new java.awt.Dimension(50, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(jComboBoxOperation, gridBagConstraints);

        jPanelValue2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Value 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelValue2.add(jLabel2, gridBagConstraints);

        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(jList2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelValue2.add(jScrollPane2, gridBagConstraints);

        jLabelAdv2.setText("Consider this number as:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelValue2.add(jLabelAdv2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanelValue2.add(jComboBoxType2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanelValue2, gridBagConstraints);

        jPanelReturnType.setLayout(new java.awt.GridBagLayout());

        jSeparator2.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator2.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelReturnType.add(jSeparator2, gridBagConstraints);

        jLabelReturnType.setText("Return type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelReturnType.add(jLabelReturnType, gridBagConstraints);

        jComboBoxReturnType.setMinimumSize(new java.awt.Dimension(200, 18));
        jComboBoxReturnType.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelReturnType.add(jComboBoxReturnType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanelReturnType, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jToggleButton1.setText("Advanced options");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jToggleButton1, gridBagConstraints);

        jButtonOk.setText("Ok");
        jButtonOk.setActionCommand("OK");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonOk, gridBagConstraints);

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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged

    }//GEN-LAST:event_jList2ValueChanged

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        String exp = "";
        if (jList1.getSelectedIndex() < 0)
        {
            JOptionPane.showMessageDialog(this, I18n.getString("ArithmeticOperationHelper.msg1","Please select an object in the first list"));
            return;
        }
        else if (jList2.getSelectedIndex() < 0)
        {
            JOptionPane.showMessageDialog(this, I18n.getString("ArithmeticOperationHelper.msg2","Please select an object from the second list"));
            return;
        }


        ExpObject expObj1 = (ExpObject)jList1.getSelectedValue();
        ExpObject expObj2 = (ExpObject)jList2.getSelectedValue();

        if (getLanguage().equals("groovy"))
        {
            this.setExpression( expObj1.getExpression() + " " + jComboBoxOperation.getSelectedItem() + " " + expObj2.getExpression() );
        }
        else
        {
            exp = expObj1.getExpression();

            if (jComboBoxType1.getSelectedIndex() == 0)
            {
                String type = expObj1.getClassType();
                if (type.equals( "java.lang.Number")) exp +=".doubleValue()";
                if (type.equals( "java.lang.BigDecimal")) exp +=".doubleValue()";
                if (type.equals( "java.lang.BigInteger")) exp +=".longValue()";
                if (type.equals( "java.lang.Byte")) exp +=".byteValue()";
                if (type.equals( "java.lang.Double")) exp +=".doubleValue()";
                if (type.equals( "java.lang.Float")) exp +=".floatValue()";
                if (type.equals( "java.lang.Integer")) exp +=".intValue()";
                if (type.equals( "java.lang.Long")) exp +=".longValue()";
                if (type.equals( "java.lang.Short")) exp +=".shortValue()";
            }
            else if (jComboBoxType1.getSelectedIndex() == 1)
            {
                exp +=".intValue()";
            }
            else if (jComboBoxType1.getSelectedIndex() == 2)
            {
                exp +=".doubleValue()";
            }

            exp = " (" + exp + ") " + jComboBoxOperation.getSelectedItem();


            exp += "(" + expObj2.getExpression();

            if (jComboBoxType2.getSelectedIndex() == 0)
            {
                String type = expObj2.getClassType();
                if (type.equals( "java.lang.Number")) exp +=".doubleValue()";
                if (type.equals( "java.lang.BigDecimal")) exp +=".doubleValue()";
                if (type.equals( "java.lang.BigInteger")) exp +=".longValue()";
                if (type.equals( "java.lang.Byte")) exp +=".byteValue()";
                if (type.equals( "java.lang.Double")) exp +=".doubleValue()";
                if (type.equals( "java.lang.Float")) exp +=".floatValue()";
                if (type.equals( "java.lang.Integer")) exp +=".intValue()";
                if (type.equals( "java.lang.Long")) exp +=".longValue()";
                if (type.equals( "java.lang.Short")) exp +=".shortValue()";
            }
            else if (jComboBoxType2.getSelectedIndex() == 1)
            {
                exp +=".intValue()";
            }
            else if (jComboBoxType2.getSelectedIndex() == 2)
            {
                exp +=".doubleValue()";
            }

            exp +=")";

            if (jComboBoxReturnType.getSelectedIndex() != 0)
            {

                exp = "new " + jComboBoxReturnType.getSelectedItem() + "( " + exp + " )";
            }
            else if (expObj2.getClassType().equals( expObj1.getClassType()) &&
                !expObj2.getClassType().equals("java.lang.Number"))
            {
                exp = "new " + expObj2.getClassType() + "( " + exp + " )";
            }
            else
            {
                if (isInteger(expObj2.getClassType()) && isInteger(expObj1.getClassType()))
                {
                    exp = "new Integer( " + exp + " )";
                }
                else
                {
                    exp = "new Double( " + exp + " )";
                }
            }


            this.setExpression( exp );
        }


        this.setDialogResult( JOptionPane.OK_OPTION);
        this.dialog.setVisible(false);
        this.dialog.dispose();


    }//GEN-LAST:event_jButtonOkActionPerformed

    private boolean isInteger(String type)
    {
        if (type.equals( "java.lang.Number")) return true;
        if (type.equals( "java.lang.BigInteger")) return true;
        if (type.equals( "java.lang.Byte")) return true;
        if (type.equals( "java.lang.Integer")) return true;
        if (type.equals( "java.lang.Long")) return true;
        if (type.equals( "java.lang.Short")) return true;

        return false;
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed

        if (dialog != null)
        {
            dialog.setVisible(false);
            dialog.dispose();
        }

    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged


    }//GEN-LAST:event_jList1ValueChanged

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed

        updateAdvancedOptionsStatus( jToggleButton1.isSelected() );

    }//GEN-LAST:event_jToggleButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBoxOperation;
    private javax.swing.JComboBox jComboBoxReturnType;
    private javax.swing.JComboBox jComboBoxType1;
    private javax.swing.JComboBox jComboBoxType2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAdv1;
    private javax.swing.JLabel jLabelAdv2;
    private javax.swing.JLabel jLabelReturnType;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelReturnType;
    private javax.swing.JPanel jPanelValue1;
    private javax.swing.JPanel jPanelValue2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables


    public void updateAdvancedOptionsStatus(boolean b)
    {
        jPanelReturnType.setVisible(b);
        jLabelAdv1.setVisible(b);
        jLabelAdv2.setVisible(b);
        jComboBoxType1.setVisible(b);
        jComboBoxType2.setVisible(b);

        if (b)
        {
            jToggleButton1.setText( I18n.getString("FewerOptions", "Fewer options"));
        }
        else
        {
            jToggleButton1.setText( I18n.getString("MoreOptions", "More options"));
        }
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public JDialog getDialog() {
        
        return dialog;
    }

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
        if (dialog != null)
        {
            dialog.setTitle(I18n.getString("arithmeticOperationHelper.title","Arithmetic operation"));
        }
    }


    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("arithmeticOperationHelper.buttonCancel","Cancel"));
                jButtonOk.setText(I18n.getString("arithmeticOperationHelper.buttonOk","Ok"));
                jLabel1.setText(I18n.getString("arithmeticOperationHelper.label1","Value 1"));
                jLabel2.setText(I18n.getString("arithmeticOperationHelper.label2","Value 2"));
                jLabelAdv1.setText(I18n.getString("arithmeticOperationHelper.labelAdv1","Consider this number as:"));
                jLabelAdv2.setText(I18n.getString("arithmeticOperationHelper.labelAdv2","Consider this number as:"));
                jLabelReturnType.setText(I18n.getString("arithmeticOperationHelper.labelReturnType","Return type:"));
                // End autogenerated code ----------------------
                if (dialog != null)
                {
                    dialog.setTitle(I18n.getString("arithmeticOperationHelper.title","Arithmetic operation"));
                }
                jButtonCancel.setMnemonic(I18n.getString("arithmeticOperationHelper.buttonCancelMnemonic","c").charAt(0));
                jButtonOk.setMnemonic(I18n.getString("arithmeticOperationHelper.buttonOkMnemonic","o").charAt(0));
    }
}
