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
 * ChartSelectionJDialog.java
 * 
 * Created on 8 luglio 2005, 23.26
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.chart.Chart;
import it.businesslogic.ireport.chart.ChartDescriptor;
import it.businesslogic.ireport.gui.listview.JListView;
import it.businesslogic.ireport.util.I18n;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
/**
 *
 * @author  Administrator
 */
public class ChartSelectionJDialog extends javax.swing.JDialog {
    
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private it.businesslogic.ireport.gui.listview.JListView jListView = null;
    private JList jList1 = null;
    
    private Chart chart = null;
    
    private boolean multiAxisMode = false;
    
    
    /** Creates new form ChartSelectionJDialog */
    public ChartSelectionJDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    /** Creates new form ChartSelectionJDialog */
    public ChartSelectionJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    

    
    public void updateCharts()
    {
        
        javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jList1.getModel();
        
        dlm.removeAllElements();
        
        if (!isMultiAxisMode())  dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/pie.png","Pie", "it.businesslogic.ireport.chart.PieChart"));
        if (!isMultiAxisMode())dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/pie3d.png","Pie 3D", "it.businesslogic.ireport.chart.Pie3DChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/bar.png","Bar", "it.businesslogic.ireport.chart.BarChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/bar3d.png","Bar 3D", "it.businesslogic.ireport.chart.Bar3DChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/xybar.png","YX Bar", "it.businesslogic.ireport.chart.XYBarChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/stackedbar.png","Stacked Bar", "it.businesslogic.ireport.chart.StackedBarChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/stackedbar3d.png","Stacked Bar 3D", "it.businesslogic.ireport.chart.StackedBar3DChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/line.png","Line", "it.businesslogic.ireport.chart.LineChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/xyline.png","XY Line", "it.businesslogic.ireport.chart.XYLineChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/area.png","Area", "it.businesslogic.ireport.chart.AreaChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/xyarea.png","YX Area", "it.businesslogic.ireport.chart.XYAreaChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/stackedarea.png","Stacked Area", "it.businesslogic.ireport.chart.StackedAreaChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/scatter.png","Scatter", "it.businesslogic.ireport.chart.ScatterChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/bubble.png","Bubble", "it.businesslogic.ireport.chart.BubbleChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/timeseries.png","Time Series", "it.businesslogic.ireport.chart.TimeSeriesChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/highlow.png","High Low", "it.businesslogic.ireport.chart.HighLowChart"));
        dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/candlestick.png","Candlestick", "it.businesslogic.ireport.chart.CandlestickChart"));
        if (!isMultiAxisMode()) dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/meter.png","Meter", "it.businesslogic.ireport.chart.MeterChart"));
        if (!isMultiAxisMode()) dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/thermometer.png","Thermometer", "it.businesslogic.ireport.chart.ThermometerChart"));
        if (!isMultiAxisMode()) dlm.addElement(new ChartDescriptor("/it/businesslogic/ireport/icons/charts/multiaxis.png","Multi Axis", "it.businesslogic.ireport.chart.MultiAxisChart"));
        
        jList1.updateUI();
        
    }
    
    public void initAll()
    {
        initComponents();
        applyI18n();
        
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        jListView = new JListView();
        jList1 = (JList)jListView.getList();
        
        
        jPanelChartType.add(jListView, java.awt.BorderLayout.CENTER);
        
        javax.swing.DefaultListModel dlm =  new javax.swing.DefaultListModel() ;
        jList1.setModel(dlm );
        jList1.setCellRenderer(new ChartCellRenderer());
        
        updateCharts();
        
        
        jList1.setLayoutOrientation( JList.HORIZONTAL_WRAP);
        this.setSize(400,400);
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        jList1.setSelectionMode( DefaultListSelectionModel.SINGLE_SELECTION );
        jList1.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) 
            {
                selectedChart();
            }
            
        });
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOk);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelChartType = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabelChartNameVal = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanelChartType.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), "Chart info"));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelChartNameVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        jPanelChartType.add(jPanel5, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelChartType, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jButtonOk.setText("OK");
        jButtonOk.setEnabled(false);
        jButtonOk.setMaximumSize(new java.awt.Dimension(200, 25));
        jButtonOk.setPreferredSize(new java.awt.Dimension(100, 25));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        jPanel1.add(jButtonOk, gridBagConstraints);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        jPanel1.add(jButtonCancel, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        
        setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION );
        this.setVisible(false);
        this.dispose();    
        
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        setDialogResult( javax.swing.JOptionPane.OK_OPTION );
        
        ChartDescriptor cd = (ChartDescriptor)jList1.getSelectedValue();
        try {
            this.setChart( (Chart)(getClass().forName(cd.getImplementationClass())).newInstance() );
        } catch (Exception ex)
        {
            
        }
        this.setVisible(true);
        this.dispose();    
        
    }//GEN-LAST:event_jButtonOkActionPerformed
    
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JLabel jLabelChartNameVal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelChartType;
    // End of variables declaration//GEN-END:variables

      void selectedChart()
    {
          if (jList1.getSelectedIndex() >= 0)
          {
            ChartDescriptor cd = (ChartDescriptor)jList1.getSelectedValue();
            this.jLabelChartNameVal.setText(cd.getName());
            
            this.jButtonOk.setEnabled(true);
          }
          else
          {
              this.jButtonOk.setEnabled(false);
          }
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public boolean isMultiAxisMode() {
        return multiAxisMode;
    }

    public void setMultiAxisMode(boolean multiAxisMode) {
        this.multiAxisMode = multiAxisMode;
        
        updateCharts();
    }
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("chartSelectionJDialog.buttonCancel","Cancel"));
                jButtonOk.setText(I18n.getString("chartSelectionJDialog.buttonOk","OK"));
                // End autogenerated code ----------------------
                ((javax.swing.border.TitledBorder)jPanel5.getBorder()).setTitle( I18n.getString("chartSelectionJDialog.panelBorder.ChartInfo","Chart info") );
                            
                jButtonCancel.setMnemonic(I18n.getString("chartSelectionJDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOk.setMnemonic(I18n.getString("chartSelectionJDialog.buttonOkMnemonic","o").charAt(0));
                
    }
}
