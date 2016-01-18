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
 * JRSubreportReturnValueDialog.java
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
public class JRSubreportReturnValueDialog
    extends javax.swing.JDialog
{

    /** Creates new form JRParameterDialog */
    private JRSubreportReturnValue subreportReturnValue = null;

    /**
     * Creates a new JRVariableDialog object.
     * 
     * @param report DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public JRSubreportReturnValueDialog(java.awt.Dialog parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        applyI18n();
        setTypes();
        updateVariables();
        jComboBoxSubreportVariable.setSelectedItem("");
        this.setSize(380, 260);
        Misc.centerFrame(this);
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }

    /**
     * DOCUMENT ME!
     */
    public void setTypes()
    {
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
    }

    /**
     * Read all available variables from the active report and popute the combobox
     */
    public void updateVariables()
    {
        try {
            Misc.updateComboBox(jComboBoxVariable,
                    it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getVariables(), false);
            
            // Remove builtin variables...
            for (int i=0; i<jComboBoxVariable.getItemCount(); ++i)
            {
                
                Object var = jComboBoxVariable.getItemAt(i);
                if (var instanceof JRVariable && ((JRVariable)var).isBuiltin())
                {
                    jComboBoxSubreportVariable.addItem( var+"");
                    jComboBoxVariable.removeItem(var);
                    --i;
                }
            }
            
        } catch (Exception ex)
        {
            
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
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxCalculationType = new javax.swing.JComboBox();
        jComboBoxVariable = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldNameIncrementerFactoryClass = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jComboBoxSubreportVariable = new javax.swing.JComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Add/modify variable");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Subreport variable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Calculation type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel5.setText("Local destination variable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxCalculationType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxVariable, gridBagConstraints);

        jLabel8.setText("Custom Incrementer Factory Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel8, gridBagConstraints);

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

        jComboBoxSubreportVariable.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jComboBoxSubreportVariable, gridBagConstraints);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonCancelActionPerformed
    {
        setVisible(false);
        this.setDialogResult(javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonOKActionPerformed
    {

        if ((this.jComboBoxSubreportVariable.getSelectedItem()+"").trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString( "messages.jRSubreportReturnValueDialog.notValidName","Please insert a valid subreport variable name!"),
                    I18n.getString( "messages.jRSubreportReturnValueDialog.notValidNameCaption","Invalid variable!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        subreportReturnValue = new JRSubreportReturnValue();
        
        subreportReturnValue.setSubreportVariable( this.jComboBoxSubreportVariable.getSelectedItem()+"" );
        
        
        if (this.jComboBoxVariable.getSelectedItem() == null)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString( "messages.jRSubreportReturnValueDialog.notValidLocalVariable","Please insert a valid local variable! If no variables are available, please close this dialog and create one."),
                    I18n.getString( "messages.jRSubreportReturnValueDialog.notValidLocalVariableCaption","Invalid variable!"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }
        subreportReturnValue.setToVariable( jComboBoxVariable.getSelectedItem()+"");

        Object t = this.jComboBoxCalculationType.getSelectedItem();
        if (t != null && t instanceof Tag) subreportReturnValue.setCalculation(  ((Tag)t).getValue()+"" );
        subreportReturnValue.setIncrementFactoryClass( this.jTextFieldNameIncrementerFactoryClass.getText());
        
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
     * Getter for property tmpParameter.
     * 
     * @return Value of property tmpParameter.
     */
    public it.businesslogic.ireport.JRSubreportReturnValue getSubreportReturnValue()
    {

        return subreportReturnValue;
    }

    /**
     * Setter for property tmpParameter.
     * 
     * @param tmpVariable New value of property tmpParameter.
     */
    public void setSubreportReturnValue(it.businesslogic.ireport.JRSubreportReturnValue tmpSubreportReturnValue)
    {
        this.jComboBoxSubreportVariable.setSelectedItem(new String(tmpSubreportReturnValue.getSubreportVariable() ));
        for (int i=0; i<jComboBoxVariable.getItemCount(); ++i)
        {

            Object var = jComboBoxVariable.getItemAt(i);
            if ((var+"").equals( tmpSubreportReturnValue.getToVariable() ))
            {
                jComboBoxVariable.setSelectedIndex(i);
                break;
            }
        }
        
        Misc.setComboboxSelectedTagValue( jComboBoxCalculationType, tmpSubreportReturnValue.getCalculation() );
        this.jTextFieldNameIncrementerFactoryClass.setText(tmpSubreportReturnValue.getIncrementFactoryClass());
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
    private javax.swing.JComboBox jComboBoxSubreportVariable;
    private javax.swing.JComboBox jComboBoxVariable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldNameIncrementerFactoryClass;
    // End of variables declaration//GEN-END:variables
    private int dialogResult;

    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRSubreportReturnValueDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRSubreportReturnValueDialog.buttonOK","OK"));
                jLabel1.setText(I18n.getString("jRSubreportReturnValueDialog.label1","Subreport variable"));
                jLabel2.setText(I18n.getString("jRSubreportReturnValueDialog.label2","Calculation type"));
                jLabel5.setText(I18n.getString("jRSubreportReturnValueDialog.label5","Local destination variable"));
                jLabel8.setText(I18n.getString("jRSubreportReturnValueDialog.label8","Custom Incrementer Factory Class"));
                // End autogenerated code ----------------------
                this.setTitle(I18n.getString("jRSubreportReturnValueDialog.title","Add/modify variable"));
                jButtonCancel.setMnemonic(I18n.getString("jRSubreportReturnValueDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRSubreportReturnValueDialog.buttonOKMnemonic","o").charAt(0));
    }
}
