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
 * HighLowDatasetPanel.java
 * 
 * Created on 15 agosto 2005, 17.55
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

/**
 *
 * @author  Administrator
 */
public class HighLowDatasetPanel extends javax.swing.JPanel implements ChartDatasetPanel {
    
    private HighLowDataset highLowDataset = null;
    
    /** Creates new form PieDatasetPanel */
    public HighLowDatasetPanel() {
        initComponents();
        
        applyI18n();
        
        this.jRTextExpressionSeries.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionSeriesTextChanged();
            }
        });
        
        
        this.jRTextExpressionDate.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionDateTextChanged();
            }
        });
        
        this.jRTextExpressionVolume.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionVolumeTextChanged();
            }
        });
        
        this.jRTextExpressionHigh.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionHighTextChanged();
            }
        });
        
        this.jRTextExpressionLow.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionLowTextChanged();
            }
        });
        
        this.jRTextExpressionOpen.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionOpenTextChanged();
            }
        });
        
        this.jRTextExpressionClose.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionCloseTextChanged();
            }
        });
    }

    public HighLowDataset getHighLowDataset() {
        return highLowDataset;
    }
    
    /**
     * this method is used to pass the correct subdataset to the expression editor
     */
    public void setSubDataset( SubDataset sds )
    {
        jRTextExpressionSeries.setSubDataset(sds);
        jRTextExpressionDate.setSubDataset(sds);
        jRTextExpressionHigh.setSubDataset(sds);
        jRTextExpressionLow.setSubDataset(sds);
        jRTextExpressionOpen.setSubDataset(sds);
        jRTextExpressionClose.setSubDataset(sds);
        jRTextExpressionVolume.setSubDataset(sds);
        sectionItemHyperlinkPanel1.setSubDataset(sds);
    }

    public void setHighLowDataset(HighLowDataset highLowDataset) {
        this.highLowDataset = highLowDataset;
        jRTextExpressionSeries.setText( highLowDataset.getSeriesExpression() );
        jRTextExpressionDate.setText( highLowDataset.getDateExpression() );
        jRTextExpressionHigh.setText( highLowDataset.getHighExpression() );
        jRTextExpressionLow.setText( highLowDataset.getLowExpression() );
        jRTextExpressionOpen.setText( highLowDataset.getOpenExpression() );
        jRTextExpressionClose.setText( highLowDataset.getCloseExpression() );
        jRTextExpressionVolume.setText( highLowDataset.getVolumeExpression() );
        sectionItemHyperlinkPanel1.setSectionItemHyperlink(highLowDataset.getItemHyperLink());
    }
    
    public void jRTextExpressionSeriesTextChanged()
    {
        highLowDataset.setSeriesExpression( jRTextExpressionSeries.getText() );
    }
    
    public void jRTextExpressionDateTextChanged()
    {
        highLowDataset.setDateExpression( jRTextExpressionDate.getText() );
    }
    
    public void jRTextExpressionHighTextChanged()
    {
        highLowDataset.setHighExpression( jRTextExpressionHigh.getText() );
    }
    
    public void jRTextExpressionLowTextChanged()
    {
        highLowDataset.setLowExpression( jRTextExpressionLow.getText() );
    }
    
    public void jRTextExpressionOpenTextChanged()
    {
        highLowDataset.setOpenExpression( jRTextExpressionOpen.getText() );
    }
    
    public void jRTextExpressionCloseTextChanged()
    {
        highLowDataset.setCloseExpression( jRTextExpressionClose.getText() );
    }
    
    public void jRTextExpressionVolumeTextChanged()
    {
        highLowDataset.setVolumeExpression( jRTextExpressionVolume.getText() );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabelSeriesExpression = new javax.swing.JLabel();
        jLabelDateExpression = new javax.swing.JLabel();
        jRTextExpressionSeries = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jRTextExpressionDate = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelHL = new javax.swing.JPanel();
        jLabelHighExpression = new javax.swing.JLabel();
        jRTextExpressionHigh = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabelLowExpression = new javax.swing.JLabel();
        jRTextExpressionLow = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelOC = new javax.swing.JPanel();
        jLabelOpenExpression = new javax.swing.JLabel();
        jRTextExpressionOpen = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabelCloseExpression = new javax.swing.JLabel();
        jRTextExpressionClose = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel1 = new javax.swing.JPanel();
        jLabelVolumeExpression = new javax.swing.JLabel();
        jRTextExpressionVolume = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        sectionItemHyperlinkPanel1 = new it.businesslogic.ireport.chart.gui.SectionItemHyperlinkPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelSeriesExpression.setText("Series expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabelSeriesExpression, gridBagConstraints);

        jLabelDateExpression.setText("Date expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jLabelDateExpression, gridBagConstraints);

        jRTextExpressionSeries.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionSeries.setElectricScroll(0);
        jRTextExpressionSeries.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionSeries.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel2.add(jRTextExpressionSeries, gridBagConstraints);

        jRTextExpressionDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionDate.setElectricScroll(0);
        jRTextExpressionDate.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionDate.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        jPanel2.add(jRTextExpressionDate, gridBagConstraints);

        jPanelHL.setLayout(new java.awt.GridBagLayout());

        jLabelHighExpression.setText("High expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelHL.add(jLabelHighExpression, gridBagConstraints);

        jRTextExpressionHigh.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionHigh.setElectricScroll(0);
        jRTextExpressionHigh.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionHigh.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelHL.add(jRTextExpressionHigh, gridBagConstraints);

        jLabelLowExpression.setText("Low expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelHL.add(jLabelLowExpression, gridBagConstraints);

        jRTextExpressionLow.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionLow.setElectricScroll(0);
        jRTextExpressionLow.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionLow.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        jPanelHL.add(jRTextExpressionLow, gridBagConstraints);

        jTabbedPane1.addTab("High/Low", jPanelHL);

        jPanelOC.setLayout(new java.awt.GridBagLayout());

        jLabelOpenExpression.setText("Open expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelOC.add(jLabelOpenExpression, gridBagConstraints);

        jRTextExpressionOpen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionOpen.setElectricScroll(0);
        jRTextExpressionOpen.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionOpen.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelOC.add(jRTextExpressionOpen, gridBagConstraints);

        jLabelCloseExpression.setText("Close expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanelOC.add(jLabelCloseExpression, gridBagConstraints);

        jRTextExpressionClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionClose.setElectricScroll(0);
        jRTextExpressionClose.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionClose.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelOC.add(jRTextExpressionClose, gridBagConstraints);

        jTabbedPane1.addTab("Open/Close", jPanelOC);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelVolumeExpression.setText("Volume expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabelVolumeExpression, gridBagConstraints);

        jRTextExpressionVolume.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionVolume.setElectricScroll(0);
        jRTextExpressionVolume.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionVolume.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionVolume, gridBagConstraints);

        jTabbedPane1.addTab("Volume", jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jTabbedPane1, gridBagConstraints);

        jTabbedPane2.addTab("Data", jPanel2);

        jTabbedPane2.addTab("Item hyperlink", sectionItemHyperlinkPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jTabbedPane2, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelCloseExpression;
    private javax.swing.JLabel jLabelDateExpression;
    private javax.swing.JLabel jLabelHighExpression;
    private javax.swing.JLabel jLabelLowExpression;
    private javax.swing.JLabel jLabelOpenExpression;
    private javax.swing.JLabel jLabelSeriesExpression;
    private javax.swing.JLabel jLabelVolumeExpression;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelHL;
    private javax.swing.JPanel jPanelOC;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionClose;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionDate;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionHigh;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionLow;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionOpen;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionSeries;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionVolume;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private it.businesslogic.ireport.chart.gui.SectionItemHyperlinkPanel sectionItemHyperlinkPanel1;
    // End of variables declaration//GEN-END:variables
    
    public void applyI18n()
    {
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
        jLabelSeriesExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.seriesExpression", "Series expression"));
        jLabelDateExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.dateExpression", "Date expression"));
        jLabelHighExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.highExpression", "High expression"));
        jLabelLowExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.lowExpression", "Low expression"));
        jLabelOpenExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.openExpression", "Open expression"));
        jLabelCloseExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.closeExpression", "Close expression"));
        jLabelVolumeExpression.setText(it.businesslogic.ireport.util.I18n.getString("charts.volumeExpression", "Volume expression"));
        
        jTabbedPane1.setTitleAt(0,I18n.getString("chartSeries.tab.HighLow","High/Low"));
        jTabbedPane1.setTitleAt(1,I18n.getString("chartSeries.tab.OpenClose","Open/Close"));
        jTabbedPane1.setTitleAt(2,I18n.getString("chartSeries.tab.Volume","Volume"));
        
        jTabbedPane2.setTitleAt(0,I18n.getString("chartSeries.tab.Data","Data"));
        jTabbedPane2.setTitleAt(1,I18n.getString("chartSeries.tab.ItemHyperlink","Item hyperlink"));
        
        this.updateUI();
        
    }
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_SERIES_EXPRESSION=1;
    public static final int COMPONENT_DATA_EXPRESSION=2;
    public static final int COMPONENT_HIGH_EXPRESSION=3;
    public static final int COMPONENT_LOW_EXPRESSION=4;
    public static final int COMPONENT_OPEN_EXPRESSION=5;
    public static final int COMPONENT_CLOSE_EXPRESSION=6;
    public static final int COMPONENT_VOLUME_EXPRESSION=7;
    public static final int COMPONENT_HYPERLINK=100;
    
    /**
     * This method set the focus on a specific component.
     * Valid constants are something like:
     * COMPONENT_KEY_EXPRESSION, COMPONENT_VALUE_EXPRESSION, ...
     * otherInfo is used here only for COMPONENT_HYPERLINK
     * otherInfo[0] = expression ID
     * otherInfo[1] = parameter #
     * otherInfo[2] = parameter expression ID
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        switch (expID)
        {
            case COMPONENT_SERIES_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionSeries);
                break;
            case COMPONENT_DATA_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionDate);
                break;
            case COMPONENT_HIGH_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelHL );
                Misc.selectTextAndFocusArea(jRTextExpressionHigh);
                break;
            case COMPONENT_LOW_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelHL );
                Misc.selectTextAndFocusArea(jRTextExpressionLow);
                break;
            case COMPONENT_OPEN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelOC );
                Misc.selectTextAndFocusArea(jRTextExpressionOpen);
                break;
            case COMPONENT_CLOSE_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelOC );
                Misc.selectTextAndFocusArea(jRTextExpressionClose);
                break;
            case COMPONENT_VOLUME_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanel1 );
                Misc.selectTextAndFocusArea(jRTextExpressionVolume);
                break;
            case COMPONENT_HYPERLINK:
                jTabbedPane2.setSelectedComponent( sectionItemHyperlinkPanel1 );
                Object newInfo[] = new Object[expressionInfo.length -1 ];
                for (int i=1; i< expressionInfo.length; ++i) newInfo[i-1] = expressionInfo[i];
                sectionItemHyperlinkPanel1.setFocusedExpression(newInfo);
                break;   
        }
    }
    
    public void containerWindowOpened() {
        sectionItemHyperlinkPanel1.openExtraWindows();
    }
         
}
