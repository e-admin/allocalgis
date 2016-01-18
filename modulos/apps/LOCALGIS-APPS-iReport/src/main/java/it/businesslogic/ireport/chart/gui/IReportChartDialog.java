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
 * IReportChartDialog.java
 * 
 * Created on 29 settembre 2004, 0.56
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.chart.AvailableCharts;
import it.businesslogic.ireport.chart.ChartDefinition;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.listview.JListView;
import it.businesslogic.ireport.gui.sheet.SheetProperty;
import it.businesslogic.ireport.util.I18n;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author  Administrator
 */
public class IReportChartDialog extends javax.swing.JDialog {
    
    private java.util.Properties properties = null;
    private int dialogResult = javax.swing.JOptionPane.OK_OPTION;
    private JReportFrame jReportFrame = null;
    private it.businesslogic.ireport.gui.sheet.SheetPanel sheetPanel = null;
    private it.businesslogic.ireport.gui.listview.JListView jListView = null;
    private JList jList1 = null;
    
    /** Creates new form IReportChartDialog */
    public IReportChartDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
        
    }
    
    public IReportChartDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
        
    }
    
    
    public void initAll()
    {
        initComponents();
        
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        jListView = new JListView();
        jList1 = (JList)jListView.getList();
        
        
        jPanelChartType.add(jListView, java.awt.BorderLayout.CENTER);
        
        javax.swing.DefaultListModel dlm =  new javax.swing.DefaultListModel() ;
        jList1.setModel(dlm );
        jList1.setCellRenderer(new ChartCellRenderer());
        
        
        
        
        sheetPanel = new it.businesslogic.ireport.gui.sheet.SheetPanel();
        /*
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        DefaultTreeModel dtm = new DefaultTreeModel( root );
        jTree1.setModel( dtm );
        jTree1.setRootVisible(false);
        jTree1.setCellRenderer(new IconedStringTreeCellRenderer());
        root.add(new DefaultMutableTreeNode("All"));
        root.add(new DefaultMutableTreeNode( new IconedString("Pie charts","/it/businesslogic/ireport/icons/charts/pie_charts.png")));
        root.add(new DefaultMutableTreeNode("Bar charts"));
        root.add(new DefaultMutableTreeNode("Line charts"));
        root.add(new DefaultMutableTreeNode("Area charts"));
        root.add(new DefaultMutableTreeNode("Bar charts"));
        jTree1.expandPath(new TreePath(root));
        jTree1.updateUI();
        */
        java.util.Vector charts = AvailableCharts.getAvailableCharts();
        
        for (int i=0; i<charts.size(); ++i)
        {
            dlm.addElement(charts.elementAt(i));
        }    
        jList1.updateUI();
        
        String[] values = new String[]{};
        
        TableColumn col = jTable1.getColumnModel().getColumn(1);
        col.setCellEditor(new ComboCellEditor(values));
        col.setCellRenderer(new ComboBoxRenderer(values));

        jTabbedPane1.updateUI();
        
        this.jTable1.setRowHeight(24);
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDetails.add(sheetPanel, gridBagConstraints);
        
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
                jButton7ActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        applyI18n();
        
        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButton6);
    }
    
    public void updateSeriesComboBoxes()
    {
        
        Vector values = new Vector();
        if (getJReportFrame() == null) return;
        
        values.add( "" );
        // Look for variables that starts with SERIE_
        
        Enumeration var_enum = getJReportFrame().getReport().getVariables().elements();
        while (var_enum.hasMoreElements())
        {
            JRVariable var = (JRVariable)var_enum.nextElement();
            if (var.getName().startsWith("SERIE_"))
            {
                values.add( var.getName() );
            }
        }
        
        TableColumn col = jTable1.getColumnModel().getColumn(1);
        col.setCellEditor(new ComboCellEditor(values));
        col.setCellRenderer(new ComboBoxRenderer(values));

        jTabbedPane1.updateUI();
    }

    public JReportFrame getJReportFrame() {
        
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {

        this.jReportFrame = jReportFrame;
        updateSeriesComboBoxes();
    }
    
    public void setProperties(java.util.Properties props)
    {
         this.properties = props;
        // 1. Set the selected chart...
        String chartName = props.getProperty("chartName","pie");
        DefaultListModel dlm = (DefaultListModel)jList1.getModel();
        boolean found  = false;
        for (int i=0; i<dlm.getSize(); i++ )
        {
            ChartDefinition cd = (ChartDefinition)dlm.getElementAt(i);
            if (cd.getChartName().equals( chartName ))
            {
                jList1.setSelectedIndex(i);
                found = true;
                break;
            }
        }
        

         if (!found) jList1.setSelectedIndex(0);;
        
        for (int i=0; ; i++)
        {
            if ( props.getProperty("serie"+i) != null)
            {
                String var =  props.getProperty("serie"+i);
                    
                if (jTable1.getRowCount() < i)
                {
                   ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{"Serie", var});                
                }
                else
                {
                    DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
                    try {
                    dtm.setValueAt(var,i,1);
                    } catch (Exception ex){
                        props.remove("serie"+i); 
                    }
                }
            }
            else
            {
                break;
            }
        }
        
       
       
       
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public java.util.Properties getProperties() {
        return properties;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelChartType = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabelChartName = new javax.swing.JLabel();
        jLabelChartNameVal = new javax.swing.JLabel();
        jLabelSeries = new javax.swing.JLabel();
        jLabelSeriesVal = new javax.swing.JLabel();
        jLabelFactory = new javax.swing.JLabel();
        jLabelFactoryVal = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanelData = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButtonAddDataSerie = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanelDetails = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chart properties");
        jPanelChartType.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Chart info"));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 100));
        jLabelChartName.setText("Chart type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelChartName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelChartNameVal, gridBagConstraints);

        jLabelSeries.setText("Series");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelSeries, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelSeriesVal, gridBagConstraints);

        jLabelFactory.setText("Factory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelFactory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel5.add(jLabelFactoryVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        jPanelChartType.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Chart type", jPanelChartType);

        jPanelData.setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serie name", "Variable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelData.add(jScrollPane3, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(100, 151));
        jPanel2.setPreferredSize(new java.awt.Dimension(140, 100));
        jButton1.setText("Report series");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jButton1, gridBagConstraints);

        jButtonAddDataSerie.setText("Add serie");
        jButtonAddDataSerie.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jButtonAddDataSerie, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelData.add(jPanel2, gridBagConstraints);

        jTabbedPane1.addTab("Data", jPanelData);

        jPanelDetails.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.addTab("Chart details", jPanelDetails);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 34));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 34));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        jButton6.setText("OK");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButton6, gridBagConstraints);

        jButton7.setText("Cancel");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jButton7, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       ReportSeriesDialog rsd = new ReportSeriesDialog(this, true);
       rsd.setJReportFrame( this.getJReportFrame() );
       rsd.setVisible(true);
       
       this.updateSeriesComboBoxes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
         this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
          this.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       
        if (jList1.getSelectedIndex() < 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("messages.selectChartFirst","Please select a chart type first!")
                    );
            return;
        }
        
        this.properties = sheetPanel.getPropertiesValues();
        
        ChartDefinition cd = (ChartDefinition)jList1.getSelectedValue();
        for (int i=0; i< jTable1.getRowCount(); ++i)
        {
            String s = ""+jTable1.getValueAt(i,1);
            
            if (s == null || s.equals("null") || s.length() == 0)
            {
                //javax.swing.JOptionPane.showMessageDialog(this,"Please select a value for serie " + ""+jTable1.getValueAt(i,0));
                //return;
                s = "X"+i;
            }
            properties.setProperty("serie" + i, ""+jTable1.getValueAt(i,1) ); 
        }

        getProperties().setProperty("chartName",  cd.getChartName() );
        
        this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        this.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed
    

    void selectedChart()
    {
          if (jList1.getSelectedIndex() >= 0)
        {
            ChartDefinition cd = (ChartDefinition)jList1.getSelectedValue();
            this.jLabelChartNameVal.setText(cd.getChartName());
            String series =  "";
            
            DefaultTableModel dtm =  (javax.swing.table.DefaultTableModel)this.jTable1.getModel();
            dtm.setRowCount(0);
            
            String[] series_a = cd.getNeededSeries();
            for (int i=0; i<series_a.length; ++i)
            {
                if (i>0) series += ", ";
                series += series_a[i];
                
                dtm.addRow(new Object[]{ series_a[i],"" });
            }
        
            this.jLabelSeriesVal.setText(series);
            this.jLabelFactoryVal.setText( cd.getFactory());
            
            this.sheetPanel.removeAllProperties();
            for (int i=0; i< cd.getSheetProperties().size(); ++i)
            {
                SheetProperty sp = (SheetProperty)cd.getSheetProperties().elementAt(i);
                sheetPanel.addSheetProperty(sp); 
                if (sp.getDefaultValue() != null && this.getProperties().getProperty(sp.getKeyName())== null )
                {
                    this.getProperties().setProperty(sp.getKeyName(), ""+sp.getDefaultValue());
                }
            }
            
            sheetPanel.setPropertiesValues( this.getProperties() );
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonAddDataSerie;
    private javax.swing.JLabel jLabelChartName;
    private javax.swing.JLabel jLabelChartNameVal;
    private javax.swing.JLabel jLabelFactory;
    private javax.swing.JLabel jLabelFactoryVal;
    private javax.swing.JLabel jLabelSeries;
    private javax.swing.JLabel jLabelSeriesVal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelChartType;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JPanel jPanelDetails;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButton1.setText(I18n.getString("iReportChartDialog.button1","Report series"));
                jButton6.setText(I18n.getString("iReportChartDialog.button6","OK"));
                jButton7.setText(I18n.getString("iReportChartDialog.button7","Cancel"));
                jButtonAddDataSerie.setText(I18n.getString("iReportChartDialog.buttonAddDataSerie","Add serie"));
                jLabelChartName.setText(I18n.getString("iReportChartDialog.labelChartName","Chart type"));
                jLabelFactory.setText(I18n.getString("iReportChartDialog.labelFactory","Factory"));
                jLabelSeries.setText(I18n.getString("iReportChartDialog.labelSeries","Series"));
                // End autogenerated code ----------------------
                
                jTable1.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("iReportChartDialog.tablecolumn.serieName","Serie name") );
                jTable1.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("iReportChartDialog.tablecolumn.variable","Variable") );
                ((javax.swing.border.TitledBorder)jPanel5.getBorder()).setTitle( I18n.getString("iReportChartDialog.panelBorder.ChartInfo","Chart info") );
    
                this.setTitle(I18n.getString("iReportChartDialog.title","Chart properties"));
                jButton6.setMnemonic(I18n.getString("iReportChartDialog.button6Mnemonic","o").charAt(0));
                jButton7.setMnemonic(I18n.getString("iReportChartDialog.button7Mnemonic","c").charAt(0));
    }
}
