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
 * BoxPanel.java
 * 
 * Created on 29 novembre 2004, 18.32
 *
 */

package it.businesslogic.ireport.gui.box;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.Misc;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.sheet.HexColorChooserPanel;
import it.businesslogic.ireport.util.I18n;
/**
 *
 * @author  Administrator
 */
public class BoxPanel extends javax.swing.JPanel  {
    
    private boolean init = false;
    
    SampleBoxPanel samplePanel = null;
    private Box box = null;
    
    /** Creates new form BorderPanel */
    public BoxPanel() {
        initComponents();
        
        applyI18n();
        
        samplePanel = new SampleBoxPanel();
        init = true;
        
        jComboBoxTop.addItem( new Tag("None",it.businesslogic.ireport.util.I18n.getString("lineType.None","None")));
        jComboBoxTop.addItem( new Tag("Thin",it.businesslogic.ireport.util.I18n.getString("lineType.Thin","Thin")));
        jComboBoxTop.addItem( new Tag("1Point",it.businesslogic.ireport.util.I18n.getString("lineType.1Point","1Point")));
        jComboBoxTop.addItem( new Tag("2Point",it.businesslogic.ireport.util.I18n.getString("lineType.2Point","2Point")));
        jComboBoxTop.addItem( new Tag("4Point",it.businesslogic.ireport.util.I18n.getString("lineType.4Point","4Point")));
        jComboBoxTop.addItem( new Tag("Dotted",it.businesslogic.ireport.util.I18n.getString("lineType.Dotted","Dotted")));
        jComboBoxTop.setSelectedIndex(0);
        
        jComboBoxLeft.addItem( new Tag("None",it.businesslogic.ireport.util.I18n.getString("lineType.None","None")));
        jComboBoxLeft.addItem( new Tag("Thin",it.businesslogic.ireport.util.I18n.getString("lineType.Thin","Thin")));
        jComboBoxLeft.addItem( new Tag("1Point",it.businesslogic.ireport.util.I18n.getString("lineType.1Point","1Point")));
        jComboBoxLeft.addItem( new Tag("2Point",it.businesslogic.ireport.util.I18n.getString("lineType.2Point","2Point")));
        jComboBoxLeft.addItem( new Tag("4Point",it.businesslogic.ireport.util.I18n.getString("lineType.4Point","4Point")));
        jComboBoxLeft.addItem( new Tag("Dotted",it.businesslogic.ireport.util.I18n.getString("lineType.Dotted","Dotted")));
        jComboBoxLeft.setSelectedIndex(0);
        
        jComboBoxRight.addItem( new Tag("None",it.businesslogic.ireport.util.I18n.getString("lineType.None","None")));
        jComboBoxRight.addItem( new Tag("Thin",it.businesslogic.ireport.util.I18n.getString("lineType.Thin","Thin")));
        jComboBoxRight.addItem( new Tag("1Point",it.businesslogic.ireport.util.I18n.getString("lineType.1Point","1Point")));
        jComboBoxRight.addItem( new Tag("2Point",it.businesslogic.ireport.util.I18n.getString("lineType.2Point","2Point")));
        jComboBoxRight.addItem( new Tag("4Point",it.businesslogic.ireport.util.I18n.getString("lineType.4Point","4Point")));
        jComboBoxRight.addItem( new Tag("Dotted",it.businesslogic.ireport.util.I18n.getString("lineType.Dotted","Dotted")));
        jComboBoxRight.setSelectedIndex(0);
        
        jComboBoxBottom.addItem( new Tag("None",it.businesslogic.ireport.util.I18n.getString("lineType.None","None")));
        jComboBoxBottom.addItem( new Tag("Thin",it.businesslogic.ireport.util.I18n.getString("lineType.Thin","Thin")));
        jComboBoxBottom.addItem( new Tag("1Point",it.businesslogic.ireport.util.I18n.getString("lineType.1Point","1Point")));
        jComboBoxBottom.addItem( new Tag("2Point",it.businesslogic.ireport.util.I18n.getString("lineType.2Point","2Point")));
        jComboBoxBottom.addItem( new Tag("4Point",it.businesslogic.ireport.util.I18n.getString("lineType.4Point","4Point")));
        jComboBoxBottom.addItem( new Tag("Dotted",it.businesslogic.ireport.util.I18n.getString("lineType.Dotted","Dotted")));
        jComboBoxBottom.setSelectedIndex(0);
        
        SpinnerNumberModel snmTop = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerTop.setModel(snmTop);
        snmTop.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerTopStateChanged(evt);
            }
        });

	SpinnerNumberModel snmBottom = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerBottom.setModel(snmBottom);
        snmBottom.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerBottomStateChanged(evt);
            }
        });
        
        SpinnerNumberModel snmRight = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerRight.setModel(snmRight);
        snmRight.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerRightStateChanged(evt);
            }
        });
        
        SpinnerNumberModel snmLeft = new SpinnerNumberModel(0,0,10000,1);
        jSpinnerLeft.setModel(snmLeft);
        snmLeft.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSpinnerLeftStateChanged(evt);
            }
        });
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        jPanel3.add(samplePanel, gridBagConstraints);
        
        init = false;
    }

    
     
    private void jSpinnerTopStateChanged(ChangeEvent evt)
    {
        box.setTopPadding( Integer.parseInt(jSpinnerTop.getValue()+""));
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"TopPadding")); 
    }
    
    private void jSpinnerLeftStateChanged(ChangeEvent evt)
    {
        box.setLeftPadding( Integer.parseInt(jSpinnerLeft.getValue()+""));
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"LeftPadding")); 
    }
    
    private void jSpinnerRightStateChanged(ChangeEvent evt)
    {
        box.setRightPadding( Integer.parseInt(jSpinnerRight.getValue()+""));
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"RightPadding")); 
    }
 
       
    private void jSpinnerBottomStateChanged(ChangeEvent evt)
    {
        box.setBottomPadding( Integer.parseInt(jSpinnerBottom.getValue()+""));
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"BottomPadding")); 
    }
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerLeft = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerTop = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerRight = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerBottom = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jComboBoxTop = new javax.swing.JComboBox();
        jButtonBoxColorTop = new javax.swing.JButton();
        jButtonBoxColorLeft = new javax.swing.JButton();
        jComboBoxLeft = new javax.swing.JComboBox();
        jButtonBoxColorBottom = new javax.swing.JButton();
        jButtonBoxColorRight = new javax.swing.JButton();
        jComboBoxRight = new javax.swing.JComboBox();
        jComboBoxBottom = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(309, 80));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Padding"));
        jLabel3.setText("Left");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel3, gridBagConstraints);

        jSpinnerLeft.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerLeft.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerLeft.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSpinnerLeftPropertyChange(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerLeft, gridBagConstraints);

        jLabel4.setText("Top");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel4, gridBagConstraints);

        jSpinnerTop.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerTop.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerTop, gridBagConstraints);

        jLabel5.setText("Right");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);

        jSpinnerRight.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerRight.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerRight, gridBagConstraints);

        jLabel6.setText("Bottom");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(jLabel6, gridBagConstraints);

        jSpinnerBottom.setMinimumSize(new java.awt.Dimension(50, 20));
        jSpinnerBottom.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jSpinnerBottom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Border"));
        jPanel1.setMinimumSize(new java.awt.Dimension(360, 230));
        jPanel1.setPreferredSize(new java.awt.Dimension(360, 230));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jPanel3, gridBagConstraints);

        jComboBoxTop.setMinimumSize(new java.awt.Dimension(100, 24));
        jComboBoxTop.setPreferredSize(new java.awt.Dimension(100, 24));
        jComboBoxTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTopActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jComboBoxTop, gridBagConstraints);

        jButtonBoxColorTop.setBackground(new java.awt.Color(0, 0, 0));
        jButtonBoxColorTop.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonBoxColorTop.setMaximumSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorTop.setMinimumSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorTop.setPreferredSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoxColorTopActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonBoxColorTop, gridBagConstraints);

        jButtonBoxColorLeft.setBackground(new java.awt.Color(0, 0, 0));
        jButtonBoxColorLeft.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonBoxColorLeft.setMaximumSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorLeft.setMinimumSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorLeft.setPreferredSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoxColorLeftActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonBoxColorLeft, gridBagConstraints);

        jComboBoxLeft.setMinimumSize(new java.awt.Dimension(100, 24));
        jComboBoxLeft.setPreferredSize(new java.awt.Dimension(100, 24));
        jComboBoxLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLeftActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(jComboBoxLeft, gridBagConstraints);

        jButtonBoxColorBottom.setBackground(new java.awt.Color(0, 0, 0));
        jButtonBoxColorBottom.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonBoxColorBottom.setMaximumSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorBottom.setMinimumSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorBottom.setPreferredSize(new java.awt.Dimension(100, 10));
        jButtonBoxColorBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoxColorBottomActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonBoxColorBottom, gridBagConstraints);

        jButtonBoxColorRight.setBackground(new java.awt.Color(0, 0, 0));
        jButtonBoxColorRight.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonBoxColorRight.setMaximumSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorRight.setMinimumSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorRight.setPreferredSize(new java.awt.Dimension(10, 100));
        jButtonBoxColorRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBoxColorRightActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButtonBoxColorRight, gridBagConstraints);

        jComboBoxRight.setMinimumSize(new java.awt.Dimension(100, 24));
        jComboBoxRight.setPreferredSize(new java.awt.Dimension(100, 24));
        jComboBoxRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRightActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        jPanel1.add(jComboBoxRight, gridBagConstraints);

        jComboBoxBottom.setMinimumSize(new java.awt.Dimension(100, 24));
        jComboBoxBottom.setPreferredSize(new java.awt.Dimension(100, 24));
        jComboBoxBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBottomActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel1.add(jComboBoxBottom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 6, 6);
        add(jPanel1, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerLeftPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSpinnerLeftPropertyChange
         
    }//GEN-LAST:event_jSpinnerLeftPropertyChange

    private void jComboBoxLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLeftActionPerformed
        if (init) return;
        box.setLeftBorder( Misc.getComboboxSelectedValue( jComboBoxLeft )+"");
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"LeftBorder")); 
    }//GEN-LAST:event_jComboBoxLeftActionPerformed

    private void jComboBoxBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBottomActionPerformed
        if (init) return;
        box.setBottomBorder( Misc.getComboboxSelectedValue( jComboBoxBottom )+"");
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"BottomBorder")); 
    }//GEN-LAST:event_jComboBoxBottomActionPerformed

    private void jComboBoxRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRightActionPerformed
        if (init) return;
        box.setRightBorder( Misc.getComboboxSelectedValue( jComboBoxRight )+"");
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"RightBorder")); 
    }//GEN-LAST:event_jComboBoxRightActionPerformed

    private void jComboBoxTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTopActionPerformed
        if (init) return;
        box.setTopBorder( Misc.getComboboxSelectedValue( jComboBoxTop)+"");
        fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"TopBorder")); 
    }//GEN-LAST:event_jComboBoxTopActionPerformed

    private void jButtonBoxColorLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoxColorLeftActionPerformed
        java.awt.Color color = HexColorChooserPanel.showDialog( null,
                I18n.getString("messages.boxPanel.selectColor","Select color..."),
                //"Select color...",
         this.jButtonBoxColorLeft.getBackground());        
         if (color  != null) {
             box.setLeftBorderColor(color);
            this.jButtonBoxColorLeft.setBackground(color);
            fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"LeftBorderColor")); 
         }
    }//GEN-LAST:event_jButtonBoxColorLeftActionPerformed

    private void jButtonBoxColorBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoxColorBottomActionPerformed
        java.awt.Color color = HexColorChooserPanel.showDialog( null, 
        		I18n.getString("messages.boxPanel.selectColor","Select color..."),                
                        //"Select color...",
         this.jButtonBoxColorBottom.getBackground());        
         if (color  != null) {
             box.setBottomBorderColor(color);
            this.jButtonBoxColorBottom.setBackground(color);
            fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"BottomBorderColor")); 
         }
    }//GEN-LAST:event_jButtonBoxColorBottomActionPerformed

    private void jButtonBoxColorRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoxColorRightActionPerformed
        java.awt.Color color = HexColorChooserPanel.showDialog( null, 
        		I18n.getString("messages.boxPanel.selectColor","Select color..."),                
                        //"Select color...",
         this.jButtonBoxColorRight.getBackground());        
         if (color  != null) {
             box.setRightBorderColor(color);
            this.jButtonBoxColorRight.setBackground(color);
            fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"RightBorderColor")); 
         }
    }//GEN-LAST:event_jButtonBoxColorRightActionPerformed

    private void jButtonBoxColorTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBoxColorTopActionPerformed
         java.awt.Color color = HexColorChooserPanel.showDialog( null, 
        		I18n.getString("messages.boxPanel.selectColor","Select color..."),                
                        //"Select color...",
         this.jButtonBoxColorTop.getBackground());        
         if (color  != null) {
             box.setTopBorderColor(color);
            this.jButtonBoxColorTop.setBackground(color);
            fireActionListenerActionPerformed(new java.awt.event.ActionEvent(this,0,"TopBorderColor")); 
         }
    }//GEN-LAST:event_jButtonBoxColorTopActionPerformed

    /**
     * Registers ActionListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Removes ActionListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {

        listenerList.remove (java.awt.event.ActionListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireActionListenerActionPerformed(java.awt.event.ActionEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener)listeners[i+1]).actionPerformed (event);
            }
        }
        
        if (samplePanel != null) samplePanel.setBox(box);
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
        if (box == null) box = new Box();
        init = true;
        
        java.awt.Color c = box.getLeftBorderColor();
        this.jButtonBoxColorLeft.setBackground( c );
        this.jButtonBoxColorLeft.updateUI();
        
        c = box.getTopBorderColor();
        this.jButtonBoxColorTop.setBackground( c );
        this.jButtonBoxColorTop.updateUI();
        
        c = box.getRightBorderColor();
        this.jButtonBoxColorRight.setBackground( c );
        this.jButtonBoxColorRight.updateUI();
        
        c = box.getBottomBorderColor();
        this.jButtonBoxColorBottom.setBackground( c );
        this.jButtonBoxColorBottom.updateUI();
        
        Misc.setComboboxSelectedTagValue( jComboBoxTop, box.getTopBorder() );
        Misc.setComboboxSelectedTagValue( jComboBoxLeft, box.getLeftBorder() );
        Misc.setComboboxSelectedTagValue( jComboBoxRight, box.getRightBorder() );
        Misc.setComboboxSelectedTagValue( jComboBoxBottom, box.getBottomBorder() );
        
        jSpinnerBottom.setValue( new Integer(box.getBottomPadding() ));
        jSpinnerTop.setValue( new Integer(box.getTopPadding() ));
        jSpinnerLeft.setValue( new Integer(box.getLeftPadding() ));
        jSpinnerRight.setValue( new Integer(box.getRightPadding() ));
        
        if (samplePanel != null) samplePanel.setBox(box);
        init = false;
        
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBoxColorBottom;
    private javax.swing.JButton jButtonBoxColorLeft;
    private javax.swing.JButton jButtonBoxColorRight;
    private javax.swing.JButton jButtonBoxColorTop;
    private javax.swing.JComboBox jComboBoxBottom;
    private javax.swing.JComboBox jComboBoxLeft;
    private javax.swing.JComboBox jComboBoxRight;
    private javax.swing.JComboBox jComboBoxTop;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSpinner jSpinnerBottom;
    private javax.swing.JSpinner jSpinnerLeft;
    private javax.swing.JSpinner jSpinnerRight;
    private javax.swing.JSpinner jSpinnerTop;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jLabel3.setText(I18n.getString("boxPanel.label3","Left"));
                jLabel4.setText(I18n.getString("boxPanel.label4","Top"));
                jLabel5.setText(I18n.getString("boxPanel.label5","Right"));
                jLabel6.setText(I18n.getString("boxPanel.label6","Bottom"));
                // End autogenerated code ----------------------
                
                ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("boxPanel.panelBorder.Border","Border") );
                ((javax.swing.border.TitledBorder)jPanel2.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("boxPanel.panelBorder.Padding","Padding") );
    }
}
