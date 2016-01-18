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
 * SerieDialog.java
 * 
 * Created on 21 settembre 2004, 20.34
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.Group;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  Administrator
 */
public class SerieDialog extends javax.swing.JDialog {
    
    int dialogResult = 0;
    
    private JReportFrame jrf = null;
    private String  serieExpression = null;
    private String serieName = null;
    private String serieReset = null;
    private String serieType = null;
    
    /** Creates new form TotalObjectDialog */
    public SerieDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
         initFrame();
    }
    
    public void initFrame()
    {
        
        this.setSize(400, 450);
      
        this.setModal(true);
        
        
        org.syntax.jedit.SyntaxDocument sd = new org.syntax.jedit.SyntaxDocument();
        sd.setTokenMarker(new org.syntax.jedit.tokenmarker.JavaTokenMarker() );
      
        this.jRTextExpressionAreaDefaultExpression.setDocument( sd );
        
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
     
        //jLabelTitle.setText( it.businesslogic.ireport.util.I18n.getString("gui.library.totalobject.title","Select object to sum") );
        
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
    
    public void setVariable( it.businesslogic.ireport.JRVariable var)
    {
        String var_name = var.getName();
        var_name = var_name.substring("SERIE_".length());
        if (var_name.startsWith("G_"))
        {
            var_name = var_name.substring(2);
            // Search the right group...
             String grp_name  ="";
            java.util.Enumeration enum_groups = getJrf().getReport().getGroups().elements();
            while (enum_groups.hasMoreElements())
            {
                Group g = (Group)enum_groups.nextElement();
                grp_name = g.getName();
                if (var_name.startsWith(grp_name +"_"))
                {
                    break;
                }
            }
            
            this.jComboBox1.setSelectedItem(grp_name);
            var_name = var_name.substring(grp_name.length()+1);
        }
        else
        {
           this.jComboBox1.setSelectedIndex(0);
            
        }
        this.jTextFieldName.setText( var_name );
        this.jComboBoxClassType.setSelectedItem( var.getClassType() );
        this.jRTextExpressionAreaDefaultExpression.setText( var.getExpression() );
        
        
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
        jRTextExpressionAreaDefaultExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jTextFieldName = new javax.swing.JTextField();
        jLabelName = new javax.swing.JLabel();
        jLabelName1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabelName2 = new javax.swing.JLabel();
        jComboBoxClassType = new javax.swing.JComboBox();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabelTitle.setText("Serie expression (the single object of this serie)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 4);
        getContentPane().add(jLabelTitle, gridBagConstraints);

        jRTextExpressionAreaDefaultExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaDefaultExpression.setElectricScroll(0);
        jRTextExpressionAreaDefaultExpression.setMinimumSize(new java.awt.Dimension(0, 60));
        jRTextExpressionAreaDefaultExpression.setPreferredSize(new java.awt.Dimension(310, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.75;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jTextFieldName, gridBagConstraints);

        jLabelName.setText("Serie name");
        jLabelName.setMaximumSize(new java.awt.Dimension(40, 16));
        jLabelName.setMinimumSize(new java.awt.Dimension(40, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jLabelName, gridBagConstraints);

        jLabelName1.setText("Reset when");
        jLabelName1.setMaximumSize(new java.awt.Dimension(40, 16));
        jLabelName1.setMinimumSize(new java.awt.Dimension(40, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jLabelName1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jComboBox1, gridBagConstraints);

        jLabelName2.setText("Exp Class");
        jLabelName2.setMaximumSize(new java.awt.Dimension(40, 16));
        jLabelName2.setMinimumSize(new java.awt.Dimension(40, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jLabelName2, gridBagConstraints);

        jComboBoxClassType.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        getContentPane().add(jComboBoxClassType, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        
        if (this.jRTextExpressionAreaDefaultExpression.getText().trim().length() == 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this,it.businesslogic.ireport.util.I18n.getString("gui.chart.SerieDialog.selectexpression","Please specify an expression"));
            return;
        }
        
        if (this.jTextFieldName.getText().trim().length() == 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this,it.businesslogic.ireport.util.I18n.getString("gui.chart.SerieDialog.selectname","Please specify an name for this serie"));
            return;
        }
        
        this.serieExpression = jRTextExpressionAreaDefaultExpression.getText();
        this.serieName = jTextFieldName.getText();
        this.serieReset = "";
                this.setSerieType(""+jComboBoxClassType.getSelectedItem()) ;
        if (jComboBox1.getSelectedIndex() > 0)
        {
            this.serieReset += ""+jComboBox1.getSelectedItem();
        }
        
        this.dialogResult = javax.swing.JOptionPane.OK_OPTION;
        this.setVisible(false);
        dispose();
        
        
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SerieDialog(null, true).setVisible(true);
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
        
        jComboBox1.removeAllItems();
        jComboBox1.addItem(I18n.getString("serieDialog.none","<None>") );     
        if (jrf == null) {
           jComboBox1.updateUI();
           return;
        }
       
       java.util.Enumeration e = jrf.getReport().getGroups().elements();
       while (e.hasMoreElements())
       {
           jComboBox1.addItem( ""+e.nextElement() );
       }
        
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

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public String getSerieReset() {
        return serieReset;
    }

    public void setSerieReset(String serieReset) {
        this.serieReset = serieReset;
    }

    public String getSerieType() {
        return serieType;
    }

    public void setSerieType(String serieType) {
        this.serieType = serieType;
    }

    public String getSerieExpression() {
        return serieExpression;
    }

    public void setSerieExpression(String serieExpression) {
        this.serieExpression = serieExpression;
    }

  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBoxClassType;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelName1;
    private javax.swing.JLabel jLabelName2;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaDefaultExpression;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("serieDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("serieDialog.buttonOK","OK"));
                jLabelName.setText(I18n.getString("serieDialog.labelName","Serie name"));
                jLabelName1.setText(I18n.getString("serieDialog.labelName1","Reset when"));
                jLabelName2.setText(I18n.getString("serieDialog.labelName2","Exp Class"));
                jLabelTitle.setText(I18n.getString("serieDialog.labelTitle","Serie expression (the single object of this serie)"));
                // End autogenerated code ----------------------
                
                jButtonCancel.setMnemonic(I18n.getString("serieDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("serieDialog.buttonOKMnemonic","o").charAt(0));
    }
}
