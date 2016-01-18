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
 * MeasureDialog.java
 * 
 * Created on 9 maggio 2003, 17.25
 *
 */

package it.businesslogic.ireport.crosstab.gui;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.JRParameterDialog;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.Misc;
import it.businesslogic.ireport.util.I18n;


/**
 * @author Administrator
 */
public class MeasureDialog
    extends javax.swing.JDialog
{

    /** Creates new form JRParameterDialog */
    Measure tmpVariable = null;
    private SubDataset subDataset = null;

    /**
     * Creates a new JRVariableDialog object.
     * 
     * @param subDataset DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public MeasureDialog(SubDataset report, java.awt.Frame parent, 
                            boolean modal)
    {
        super(parent, modal);
        initAll(report);
    }
    
    public MeasureDialog(SubDataset report, java.awt.Dialog parent, 
                            boolean modal)
    {
        super(parent, modal);
        initAll(report);
    }

    public void initAll(SubDataset report)
    {
        initComponents();

        this.setSubDataset(report);
        setTypes();
        this.jComboBoxClassType.setSelectedItem("java.lang.String");
        this.jComboBoxCalculationType.setSelectedItem("Nothing");
        this.jComboBoxPercentageOf.setSelectedIndex(0); // "None"
        pack();
        
        Misc.centerFrame(this);
        
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

        this.jComboBoxPercentageOf.addItem(new Tag("None", I18n.getString("measure.percentageOf.None", "None") ));
        this.jComboBoxPercentageOf.addItem(new Tag("GrandTotal", I18n.getString("measure.percentageOf.GrandTotal", "Grand total")));
        
        this.jComboBoxCalculationType.addItem(new Tag("Nothing", I18n.getString("measure.calculationType.Nothing", "Nothing") ));
        this.jComboBoxCalculationType.addItem(new Tag("Count", I18n.getString("measure.calculationType.Count", "Count")));
        this.jComboBoxCalculationType.addItem(new Tag("DistinctCount", I18n.getString("measure.calculationType.DistinctCount", "Distinct count")));
        this.jComboBoxCalculationType.addItem(new Tag("Sum", I18n.getString("measure.calculationType.Sum", "Sum")));
        this.jComboBoxCalculationType.addItem(new Tag("Average", I18n.getString("measure.calculationType.Average", "Average")));
        this.jComboBoxCalculationType.addItem(new Tag("Lowest", I18n.getString("measure.calculationType.Lowest", "Lowest")));
        this.jComboBoxCalculationType.addItem(new Tag("Highest", I18n.getString("measure.calculationType.Highest", "Highest")));
        this.jComboBoxCalculationType.addItem(new Tag("StandardDeviation", I18n.getString("measure.calculationType.StandardDeviation", "Standard deviation")));
        this.jComboBoxCalculationType.addItem(new Tag("Variance", I18n.getString("measure.calculationType.Variance", "Variance") ));
        this.jComboBoxCalculationType.addItem(new Tag("First", I18n.getString("measure.calculationType.First", "First") ));
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
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxCalculationType = new javax.swing.JComboBox();
        jComboBoxClassType = new javax.swing.JComboBox();
        jRTextExpressionAreaExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldNameIncrementerFactoryClass = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxPercentageOf = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldNamePercentageCalculatorClass = new javax.swing.JTextField();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Add/modify measure");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("Measure name");
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

        jLabel3.setText("Measure expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel5.setText("Measure class type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxCalculationType, gridBagConstraints);

        jComboBoxClassType.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxClassType, gridBagConstraints);

        jRTextExpressionAreaExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaExpression.setCaretVisible(false);
        jRTextExpressionAreaExpression.setElectricScroll(0);
        jRTextExpressionAreaExpression.setMinimumSize(new java.awt.Dimension(657, 100));
        jRTextExpressionAreaExpression.setPreferredSize(new java.awt.Dimension(325, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jRTextExpressionAreaExpression, gridBagConstraints);

        jLabel8.setText("Custom Incrementer Factory Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel8, gridBagConstraints);

        jTextFieldNameIncrementerFactoryClass.setNextFocusableComponent(jRTextExpressionAreaExpression);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
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
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jPanel1, gridBagConstraints);

        jLabel9.setText("Percentage of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jComboBoxPercentageOf, gridBagConstraints);

        jLabel10.setText("Custom Percentage Calculator Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jLabel10, gridBagConstraints);

        jTextFieldNamePercentageCalculatorClass.setNextFocusableComponent(jRTextExpressionAreaExpression);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(jTextFieldNamePercentageCalculatorClass, gridBagConstraints);

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

        if (this.jTextFieldName.getText().trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                                                      I18n.getString("messages.measureDialog.invalidMeasureName","Please insert a valid measure name!"), 
                                                      I18n.getString("messages.measureDialog.invalidMeasureNameCaption","Invalid measure!"), 
                                                      javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }
        
        if (this.jComboBoxClassType.getSelectedIndex() < 0 ||
            (""+this.jComboBoxClassType.getSelectedItem()).trim().length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                                                      I18n.getString("messages.measureDialog.invalidClassName","Please insert a valid class name!"), 
                                                      I18n.getString("messages.measureDialog.invalidClassNameCaption","Invalid class!"), 
                                                      javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        tmpVariable = new Measure(this.jTextFieldName.getText());
        
        tmpVariable.setClassType(
                this.jComboBoxClassType.getSelectedItem() + "");
        
        tmpVariable.setCalculation(((Tag)this.jComboBoxCalculationType.getSelectedItem()).getValue()+"");
        
        tmpVariable.setPercentageOf(((Tag)this.jComboBoxPercentageOf.getSelectedItem()).getValue()+"" );

        //System.out.println(this.jComboBoxIncrementType.getSelectedItem()+"  "+ tmpVariable.getIncrementType());
        tmpVariable.setIncrementerFactoryClass(this.jTextFieldNameIncrementerFactoryClass.getText());
        tmpVariable.setPercentageCalculatorClass(this.jTextFieldNamePercentageCalculatorClass.getText());

        tmpVariable.setExpression(jRTextExpressionAreaExpression.getText());

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
    public it.businesslogic.ireport.crosstab.Measure getMeasure()
    {

        return tmpVariable;
    }

    /**
     * Setter for property tmpParameter.
     * 
     * @param tmpVariable New value of property tmpParameter.
     */
    public void setMeasure(it.businesslogic.ireport.crosstab.Measure tmpVariable)
    {
        this.jTextFieldName.setText(new String(tmpVariable.getName()));

        this.jComboBoxClassType.setSelectedItem(new String(tmpVariable.getClassType()));

        this.jRTextExpressionAreaExpression.setText(new String(tmpVariable.getExpression()));
        this.jTextFieldNameIncrementerFactoryClass.setText(new String(tmpVariable.getIncrementerFactoryClass()));
        
        Misc.setComboboxSelectedTagValue(jComboBoxCalculationType, tmpVariable.getCalculation());
        Misc.setComboboxSelectedTagValue(jComboBoxPercentageOf, tmpVariable.getPercentageOf() );
        
        this.jTextFieldNamePercentageCalculatorClass.setText(new String(tmpVariable.getPercentageCalculatorClass()));
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
    
    public void setExpressionContext(SubDataset sds)
    {
        if (sds == null)
        {
            sds = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        }
        
        this.jRTextExpressionAreaExpression.setSubDataset( sds);
        this.jRTextExpressionAreaExpression.getCrosstabElements().removeAllElements();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBoxCalculationType;
    private javax.swing.JComboBox jComboBoxClassType;
    private javax.swing.JComboBox jComboBoxPercentageOf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaExpression;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldNameIncrementerFactoryClass;
    private javax.swing.JTextField jTextFieldNamePercentageCalculatorClass;
    // End of variables declaration//GEN-END:variables
    private int dialogResult;

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
        if (subDataset == null) return;
        jRTextExpressionAreaExpression.setSubDataset( subDataset);
        jRTextExpressionAreaExpression.getTokenMarker().setKeywordLookup(subDataset.getKeywordLookup());
    }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("measureDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("measureDialog.buttonOK","OK"));
                jLabel1.setText(I18n.getString("measureDialog.label1","Measure name"));
                jLabel10.setText(I18n.getString("measureDialog.label10","Custom Percentage Calculator Class"));
                jLabel2.setText(I18n.getString("measureDialog.label2","Calculation type"));
                jLabel3.setText(I18n.getString("measureDialog.label3","Measure expression"));
                jLabel5.setText(I18n.getString("measureDialog.label5","Measure class type"));
                jLabel8.setText(I18n.getString("measureDialog.label8","Custom Incrementer Factory Class"));
                jLabel9.setText(I18n.getString("measureDialog.label9","Percentage of"));
                // End autogenerated code ----------------------
                
                this.setTitle(I18n.getString("measureDialog.title","Add/modify measure"));
                jButtonCancel.setMnemonic(I18n.getString("measureDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("measureDialog.buttonOKMnemonic","o").charAt(0));
    }
    
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_MEASURE_EXPRESSION=1;

    /**
     * This method set the focus on a specific component.
     * Valid constants are:<br>
     * COMPONENT_MEASURE_EXPRESSION
     */
    public void setFocusedExpression(int expID )
    {
        switch (expID)
        {
            case COMPONENT_MEASURE_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionAreaExpression);
                break;
        }
        
    }
}
