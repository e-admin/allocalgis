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
 * BaseWizardPanel.java
 * 
 * Created on March 22, 2006, 8:34 PM
 *
 */

package it.businesslogic.ireport.gui.wizard;

import javax.swing.JLabel;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  gtoffoli
 */
public class BaseWizardPanel extends javax.swing.JPanel {
    
    private int currentStep = -1;
    private GenericWizard genericWizard = null;
    
    private int dialogResult = javax.swing.JOptionPane.CLOSED_OPTION;
    
    /** Creates new form BaseWizardPanel */
    public BaseWizardPanel() {
        initComponents();
        applyI18n();
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public GenericWizard getGenericWizard() {
        return genericWizard;
    }

    public void setGenericWizard(GenericWizard genericWizard) {
        this.genericWizard = genericWizard;
        
        genericWizard.initWizard(); 
        setCurrentStep(-1);
        dialogResult = javax.swing.JOptionPane.CLOSED_OPTION;
        
        String[] stepsNames = genericWizard.getStepsNames();
        jPanelLabels.removeAll();
        
        for (int i=0; i<stepsNames.length; ++i)
        {
            JLabel jLabelStep = new JLabel();
            jLabelStep.setFont(new java.awt.Font(  jLabelStep.getFont().getName() , 0,  jLabelStep.getFont().getSize()));
            jLabelStep.setText(stepsNames[i]);
            java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
            jPanelLabels.add(jLabelStep, gridBagConstraints);
        }        
        goNext();
    }
    
    public void goNext()
    {
        if (getGenericWizard().nextStep(getCurrentStep()+1) )
        {
            setCurrentStep(getCurrentStep() + 1);  // 0 = first step..
            setCurrentStepLabel(getGenericWizard().getStepDescription(getCurrentStep()));        
            jPanelStepContainer.removeAll();
            jPanelStepContainer.add( getGenericWizard().getStepPanel(getCurrentStep()), java.awt.BorderLayout.CENTER);
            jPanelStepContainer.updateUI();
            updateButtons();
        }
    }
    
    public void goStep(int x)
    {
        setCurrentStep( x );  // 0 = first step..
        setCurrentStepLabel(getGenericWizard().getStepDescription(getCurrentStep()));        
        jPanelStepContainer.removeAll();
        jPanelStepContainer.add( getGenericWizard().getStepPanel(getCurrentStep()), java.awt.BorderLayout.CENTER);
        jPanelStepContainer.updateUI();
        updateButtons();
    }
    
    public void goPrevious()
    {
        if (getCurrentStep() > 0)
        {
            if (getGenericWizard().previousStep(getCurrentStep()-1) )
            {
                setCurrentStep(getCurrentStep() - 1);
                setCurrentStepLabel(getGenericWizard().getStepDescription(getCurrentStep()));        
                jPanelStepContainer.removeAll();
                jPanelStepContainer.add( getGenericWizard().getStepPanel(getCurrentStep()), java.awt.BorderLayout.CENTER);
                jPanelStepContainer.updateUI();

                updateButtons();
            }
        }
    }
    
    
    public void updateButtons()
    {

        getJButtonFinish().setEnabled( getGenericWizard().canFinish( getCurrentStep() ));
        getJButtonPrev().setEnabled( getGenericWizard().canPrevious( getCurrentStep()));
        getJButtonNext().setEnabled( getGenericWizard().canNext( getCurrentStep()) );
    }
    
    
    private void setCurrentStepLabel(String label)
    {
        jLabelStepDescription.setText( label );
        
        for (int i=0; i<jPanelLabels.getComponentCount(); ++i)
        {
            if (jPanelLabels.getComponent(i) instanceof JLabel)
            {
                JLabel jLabel =  (JLabel)jPanelLabels.getComponent(i);
                jLabel.setFont( new java.awt.Font(  jLabel.getFont().getName() , (i == getCurrentStep()) ? 1 : 0,  jLabel.getFont().getSize()) );
                jLabel.updateUI();
            }
        }
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public javax.swing.JButton getJButtonCancel() {
        return jButtonCancel;
    }

    public void setJButtonCancel(javax.swing.JButton jButtonCancel) {
        this.jButtonCancel = jButtonCancel;
    }

    public javax.swing.JButton getJButtonFinish() {
        return jButtonFinish;
    }

    public void setJButtonFinish(javax.swing.JButton jButtonFinish) {
        this.jButtonFinish = jButtonFinish;
    }

    public javax.swing.JButton getJButtonNext() {
        return jButtonNext;
    }

    public void setJButtonNext(javax.swing.JButton jButtonNext) {
        this.jButtonNext = jButtonNext;
    }

    public javax.swing.JButton getJButtonPrev() {
        return jButtonPrev;
    }

    public void setJButtonPrev(javax.swing.JButton jButtonPrev) {
        this.jButtonPrev = jButtonPrev;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelSteps = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanelLabels = new javax.swing.JPanel();
        jLabelStep1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanelContent = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelStepDescription = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanelStepContainer = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanelContentButtons = new javax.swing.JPanel();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonFinish = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jPanelSteps.setLayout(new java.awt.GridBagLayout());

        jPanelSteps.setBackground(new java.awt.Color(207, 217, 231));
        jPanelSteps.setPreferredSize(new java.awt.Dimension(150, 280));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Steps");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        jPanelSteps.add(jLabel1, gridBagConstraints);

        jSeparator2.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setMaximumSize(new java.awt.Dimension(32767, 2));
        jSeparator2.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator2.setPreferredSize(new java.awt.Dimension(1, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanelSteps.add(jSeparator2, gridBagConstraints);

        jPanelLabels.setLayout(new java.awt.GridBagLayout());

        jPanelLabels.setOpaque(false);
        jLabelStep1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelStep1.setText("1. SubDataset selection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        jPanelLabels.add(jLabelStep1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanelSteps.add(jPanelLabels, gridBagConstraints);

        jLabel7.setBackground(new java.awt.Color(207, 217, 231));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/wizard.jpg")));
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setOpaque(true);
        jLabel7.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelSteps.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        add(jPanelSteps, gridBagConstraints);

        jPanelContent.setLayout(new java.awt.GridBagLayout());

        jPanelContent.setMinimumSize(new java.awt.Dimension(40, 40));
        jPanelContent.setPreferredSize(new java.awt.Dimension(380, 40));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(380, 40));
        jPanel2.setPreferredSize(new java.awt.Dimension(380, 40));
        jLabelStepDescription.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabelStepDescription.setText("Step description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel2.add(jLabelStepDescription, gridBagConstraints);

        jSeparator4.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator4.setMaximumSize(new java.awt.Dimension(32767, 2));
        jSeparator4.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator4.setPreferredSize(new java.awt.Dimension(1, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jSeparator4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelContent.add(jPanel2, gridBagConstraints);

        jPanelStepContainer.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanelContent.add(jPanelStepContainer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanelContent, gridBagConstraints);

        jSeparator3.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator3.setMaximumSize(new java.awt.Dimension(32767, 2));
        jSeparator3.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator3.setPreferredSize(new java.awt.Dimension(1, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator3, gridBagConstraints);

        jPanelContentButtons.setLayout(new java.awt.GridBagLayout());

        jButtonPrev.setText("< Prev");
        jButtonPrev.setEnabled(false);
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 8, 2);
        jPanelContentButtons.add(jButtonPrev, gridBagConstraints);

        jButtonNext.setText("Next >");
        jButtonNext.setEnabled(false);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 8, 2);
        jPanelContentButtons.add(jButtonNext, gridBagConstraints);

        jButtonFinish.setText("Finish");
        jButtonFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 8, 2);
        jPanelContentButtons.add(jButtonFinish, gridBagConstraints);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 8, 4);
        jPanelContentButtons.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        add(jPanelContentButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setDialogResult(javax.swing.JOptionPane.CLOSED_OPTION);
        getGenericWizard().finish(-1);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishActionPerformed
        setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        getGenericWizard().finish(getCurrentStep());
    }//GEN-LAST:event_jButtonFinishActionPerformed

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        this.goNext();
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        this.goPrevious();
    }//GEN-LAST:event_jButtonPrevActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonFinish;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabelStep1;
    private javax.swing.JLabel jLabelStepDescription;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelContentButtons;
    private javax.swing.JPanel jPanelLabels;
    private javax.swing.JPanel jPanelStepContainer;
    private javax.swing.JPanel jPanelSteps;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("baseWizardPanel.buttonCancel","Cancel"));
                jButtonFinish.setText(I18n.getString("baseWizardPanel.buttonFinish","Finish"));
                jButtonNext.setText(I18n.getString("baseWizardPanel.buttonNext","Next >"));
                jButtonPrev.setText(I18n.getString("baseWizardPanel.buttonPrev","< Prev"));
                jLabel1.setText(I18n.getString("baseWizardPanel.label1","Steps"));
                jLabelStep1.setText(I18n.getString("baseWizardPanel.labelStep1","1. SubDataset selection"));
                jLabelStepDescription.setText(I18n.getString("baseWizardPanel.labelStepDescription","Step description"));
                // End autogenerated code ----------------------
    }
}
