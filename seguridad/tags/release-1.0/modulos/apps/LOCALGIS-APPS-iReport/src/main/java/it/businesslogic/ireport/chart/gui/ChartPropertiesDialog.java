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
 * ChartPropertiesDialog.java
 * 
 * Created on 15 agosto 2005, 10.19
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.chart.Axis;
import it.businesslogic.ireport.gui.JRLinkParameterDialog;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.sheet.*;
import it.businesslogic.ireport.chart.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.JRSubreportParameterDialog;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import it.businesslogic.ireport.util.Misc;
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import it.businesslogic.ireport.util.I18n;
/**
 *
 * @author  Administrator
 */
public class ChartPropertiesDialog extends javax.swing.JDialog implements SheetPropertyValueChangedListener  {
    
    private CategorySheetPanel sheetPanel = null;
    private ChartReportElement2 currentSelectedChartElement = null;
    private int dialogResult = javax.swing.JOptionPane.OK_OPTION;
    private JReportFrame jReportFrame = null;
    private boolean init = false;
       
    public void setChartElement(ChartReportElement2 chartReportElement)
    {
        setInit(true);
        
        try {
            
        currentSelectedChartElement = chartReportElement;

        jButtonPaste.setEnabled( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard() != null ) ;
        
        if (currentSelectedChartElement == null)
        {
            // We have to clean all the gui....
            sheetPanel.removeAllProperties();
            jComboBoxTypeOfData.removeAllItems();
            jPanelDataDefinition.removeAll();
        }
        else
        {
            updateGroups();
            updateSubDatasets();
            // Case by case...
            addCommonChartProperties();
            
            while (jTabbedPane1.getTabCount() > 1) jTabbedPane1.remove(1);
            if (currentSelectedChartElement.getChart() instanceof MultiAxisChart)
            {
                jTabbedPane1.add("Multiaxis charts", jPanelMultiAxis );
                jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartMultiaxis","Chart Multiaxis"));
            
                DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
                dtm.setRowCount(0);
                
                java.util.List axes = ((MultiAxisPlot)currentSelectedChartElement.getChart().getPlot() ).getAxis();
                for (int i=0; i<axes.size(); ++i)
                {
                    Axis axis = (Axis)axes.get(i);
                    dtm.addRow(new Object[]{ axis, axis.getPosition() });
                }
            
            }
            else
            {
                jTabbedPane1.add("Chart data", jPanelData );
                jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartData","Chart data"));
                
                // Set general dataset data...
                Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedChartElement.getChart().getDataset().getIncrementType());
                jComboBoxIncrementGroup.setSelectedItem( currentSelectedChartElement.getChart().getDataset().getIncrementGroup() );
                Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedChartElement.getChart().getDataset().getResetType());
                jComboBoxResetGroup.setSelectedItem( currentSelectedChartElement.getChart().getDataset().getResetGroup() );
                jRTextExpressionAreaFilterExpression.setText( currentSelectedChartElement.getChart().getDataset().getIncrementWhenExpression() );

                if (currentSelectedChartElement.getChart().getDataset().getSubDataset() != null)
                {
                    jComboBoxSubDataset.setSelectedItem(currentSelectedChartElement.getChart().getDataset().getSubDataset());
                    jTabbedPaneSubDataset.setVisible(true);

                    jRTextExpressionAreaMapExpression.setText( currentSelectedChartElement.getChart().getDataset().getParametersMapExpression()  );
                    if (!currentSelectedChartElement.getChart().getDataset().isUseConnection() &&  Misc.nvl( currentSelectedChartElement.getChart().getDataset().getDataSourceExpression(),"").trim().equals("")) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    }
                    else if (currentSelectedChartElement.getChart().getDataset().isUseConnection()) {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( currentSelectedChartElement.getChart().getDataset().getConnectionExpression());
                    }
                    else {
                        this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( currentSelectedChartElement.getChart().getDataset().getDataSourceExpression());
                    }

                    //Add parameters...
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);

                    java.util.Enumeration enum_parameters = currentSelectedChartElement.getChart().getDataset().getSubreportParameters().elements();
                    while (enum_parameters.hasMoreElements()) {
                        it.businesslogic.ireport.JRSubreportParameter parameter = (it.businesslogic.ireport.JRSubreportParameter)enum_parameters.nextElement();
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement(parameter.getExpression());
                        dtm.addRow(row);
                    }            

                    jRTextExpressionAreaFilterExpression.setSubDataset(currentSelectedChartElement.getChart().getDataset().getSubDataset());

                }
                else
                {
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    jRTextExpressionAreaMapExpression.setText("");

                    jComboBoxSubDataset.setSelectedIndex(0);
                    jTabbedPaneSubDataset.setVisible(false);
                }

                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                gridBagConstraints.fill = gridBagConstraints.BOTH;
                gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);

                if (currentSelectedChartElement.getChart() instanceof PieChart ||
                    currentSelectedChartElement.getChart() instanceof Pie3DChart)
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.PieDataset", "Pie dataset"));
                }
                else if (currentSelectedChartElement.getChart() instanceof BarChart ||
                         currentSelectedChartElement.getChart() instanceof Bar3DChart ||
                         currentSelectedChartElement.getChart() instanceof StackedBarChart ||
                         currentSelectedChartElement.getChart() instanceof StackedBar3DChart ||
                         currentSelectedChartElement.getChart() instanceof LineChart ||
                         currentSelectedChartElement.getChart() instanceof AreaChart ||
                         currentSelectedChartElement.getChart() instanceof StackedAreaChart )
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.CategoryDataset", "Category dataset"));
                }
                else if (currentSelectedChartElement.getChart() instanceof XYBarChart)
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.TimePeriodDataset", "Time period dataset"));
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.TimeSeriesDataset", "Time series dataset"));
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.XYDataset", "XY dataset"));
                }
                else if ( currentSelectedChartElement.getChart() instanceof XYLineChart ||
                          currentSelectedChartElement.getChart() instanceof XYAreaChart || 
                          currentSelectedChartElement.getChart() instanceof ScatterChart )
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.XYDataset", "XY dataset"));
                }
                else if ( currentSelectedChartElement.getChart() instanceof BubbleChart )
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.XYZDataset", "XYZ dataset"));
                }
                else if ( currentSelectedChartElement.getChart() instanceof TimeSeriesChart )
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.TimeSeriesDataset", "Time series dataset"));
                }
                else if ( currentSelectedChartElement.getChart() instanceof HighLowChart ||
                          currentSelectedChartElement.getChart() instanceof CandlestickChart)
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.HighLowDataset", "High low dataset"));
                }
                else if ( currentSelectedChartElement.getChart() instanceof MeterChart ||
                          currentSelectedChartElement.getChart() instanceof ThermometerChart)
                {
                    jComboBoxTypeOfData.addItem(new Tag("it.businesslogic.ireport.chart.ValueDataset", "Value dataset"));
                }

                setDatasetPanel( currentSelectedChartElement.getChart().getDataset() );
            }
            
            setPlotSheetProperties( currentSelectedChartElement.getChart().getPlot() );
        
        }
        
        sheetPanel.setShowResetButton(false);
        sheetPanel.recreateSheet();
        
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        setInit(false);
    }
         
    /** Creates new dialog ChartPropertiesDialog */
    public ChartPropertiesDialog(java.awt.Dialog parent, boolean modal) {
        super(parent,modal);
        initAll();
    }
     
    /** Creates new dialog ChartPropertiesDialog */
    public ChartPropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent,modal);
        initAll();
    }
     
    public void initAll()
    {
        initComponents();
        
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -1, -1, -1);
        sheetPanel = new CategorySheetPanel();
        sheetPanel.addSheetPropertyValueChangedListener(this);
        
        jPanelProperties.add(sheetPanel, gridBagConstraints);
        
        Vector values = new Vector();
        values.add( new Tag("leftOrTop", it.businesslogic.ireport.util.I18n.getString("leftOrTop","Left or top")));
        values.add( new Tag("rightOrBottom", it.businesslogic.ireport.util.I18n.getString("rightOrBottom","Right or bottom")));
        TableColumn col = jTable1.getColumnModel().getColumn(1);
        jTable1.setRowHeight(21);
        TagComboCellEditor comboEditor = new TagComboCellEditor(values);
        col.setCellEditor(comboEditor);
        col.setCellRenderer(new TagComboBoxRenderer(values));
        comboEditor.addCellEditorListener( new CellEditorListener()
            {
                public void editingCanceled(ChangeEvent e) 
                {

                }

                public void editingStopped(ChangeEvent e)  
                {
                    jTable1editingStopped(e);
                }
            });
        
        applyI18n();
        
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
         
        jComboBoxDatasetConnectionType.addItem(new Tag("Don't use connection or datasource",it.businesslogic.ireport.util.I18n.getString("ConnectionType.1","Don't use connection or datasource")));
        jComboBoxDatasetConnectionType.addItem(new Tag("Use connection expression",it.businesslogic.ireport.util.I18n.getString("ConnectionType.2","Use connection expression")));
        jComboBoxDatasetConnectionType.addItem(new Tag("Use datasource expression",it.businesslogic.ireport.util.I18n.getString("ConnectionType.3","Use datasource expression")));
        
        this.jRTextExpressionAreaMapExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaMapExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaFilterExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaFilterExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTextConnectionExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextConnectionExpressionTextChanged();
            }
        });
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableDatasetParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableDatasetParametersListSelectionValueChanged(e);
            }
        });
        
        
        jTable1.getSelectionModel().addListSelectionListener( new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e) 
                {
                    jTable1ValueChanged(e);
                }
            });
        
        this.pack();
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCloseActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        
        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonClose);
    }
    
    private void jTable1ValueChanged(javax.swing.event.ListSelectionEvent evt) {                                    

        
        if (jTable1.getSelectedRowCount() > 0)
        {
            jButtonDelete.setEnabled(true);
            jButtonModify.setEnabled(true);
            jButtonMoveUp.setEnabled(jTable1.getSelectedRow() > 0);
            jButtonMoveDown.setEnabled(jTable1.getSelectedRow() < jTable1.getRowCount()-1);
        }
        else
        {
            jButtonModify.setEnabled(false);
            jButtonDelete.setEnabled(false);
            jButtonMoveUp.setEnabled(false);
            jButtonMoveDown.setEnabled(false);
        }
    
    }
    
    public void jRTextExpressionAreaMapExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            currentSelectedChartElement.getChart().getDataset().setParametersMapExpression(""+jRTextExpressionAreaMapExpression.getText());
        }
    }
    
    public void jRTextExpressionAreaFilterExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            currentSelectedChartElement.getChart().getDataset().setIncrementWhenExpression(""+jRTextExpressionAreaFilterExpression.getText());
        }
    }

    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return; 
        if (currentSelectedChartElement != null)
        {
            if (currentSelectedChartElement.getChart().getDataset().isUseConnection())
                currentSelectedChartElement.getChart().getDataset().setConnectionExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
            else
                currentSelectedChartElement.getChart().getDataset().setDataSourceExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
        }
    }
    
    public void jTableDatasetParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableDatasetParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        }
        else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
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
        jPanelProperties = new javax.swing.JPanel();
        jPanelData = new javax.swing.JPanel();
        jLabelTypeOfData = new javax.swing.JLabel();
        jComboBoxTypeOfData = new javax.swing.JComboBox();
        jTabbedPaneData = new javax.swing.JTabbedPane();
        jPanelDataset = new javax.swing.JPanel();
        jLabelResetType = new javax.swing.JLabel();
        jComboBoxResetType = new javax.swing.JComboBox();
        jLabelResetGroup = new javax.swing.JLabel();
        jComboBoxResetGroup = new javax.swing.JComboBox();
        jLabelIncrementType = new javax.swing.JLabel();
        jComboBoxIncrementType = new javax.swing.JComboBox();
        jLabelIncrementGroup = new javax.swing.JLabel();
        jComboBoxIncrementGroup = new javax.swing.JComboBox();
        jLabelIncrementType2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelIncrementType1 = new javax.swing.JLabel();
        jComboBoxSubDataset = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPaneSubDataset = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDatasetParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaMapExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jComboBoxDatasetConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel2 = new javax.swing.JPanel();
        jButtonCopy = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jRTextExpressionAreaFilterExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelDataDefinition = new javax.swing.JPanel();
        jPanelMultiAxis = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chart details");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(450, 454));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(450, 500));
        jPanelProperties.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.addTab("Chart properties", jPanelProperties);

        jPanelData.setLayout(new java.awt.GridBagLayout());

        jLabelTypeOfData.setText("Type of dataset");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelData.add(jLabelTypeOfData, gridBagConstraints);

        jComboBoxTypeOfData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTypeOfDataActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanelData.add(jComboBoxTypeOfData, gridBagConstraints);

        jPanelDataset.setLayout(new java.awt.GridBagLayout());

        jLabelResetType.setText("Reset type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetType, gridBagConstraints);

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
        jPanelDataset.add(jComboBoxResetType, gridBagConstraints);

        jLabelResetGroup.setText("Reset group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelResetGroup, gridBagConstraints);

        jComboBoxResetGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxResetGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxResetGroup, gridBagConstraints);

        jLabelIncrementType.setText("Increment type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType, gridBagConstraints);

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
        jPanelDataset.add(jComboBoxIncrementType, gridBagConstraints);

        jLabelIncrementGroup.setText("Increment group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementGroup, gridBagConstraints);

        jComboBoxIncrementGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIncrementGroupActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelDataset.add(jComboBoxIncrementGroup, gridBagConstraints);

        jLabelIncrementType2.setText("Increment When expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanelDataset.add(jLabelIncrementType2, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dataset run"));
        jPanel1.setPreferredSize(new java.awt.Dimension(329, 192));
        jLabelIncrementType1.setText("Sub dataset");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel1.add(jLabelIncrementType1, gridBagConstraints);

        jComboBoxSubDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubDatasetActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(jComboBoxSubDataset, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));
        jTableDatasetParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDatasetParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDatasetParametersMouseClicked(evt);
            }
        });

        jScrollPane2.setViewportView(jTableDatasetParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel10.setMinimumSize(new java.awt.Dimension(150, 33));
        jButtonAddParameter.setText("Add");
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonAddParameter, gridBagConstraints);

        jButtonModParameter.setText("Modify");
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jButtonModParameter, gridBagConstraints);

        jButtonRemParameter.setText("Remove");
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel10.add(jButtonRemParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel16.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jPanel16, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Parameters", jPanel4);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Parameters Map Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanel5.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaMapExpression.setElectricScroll(0);
        jRTextExpressionAreaMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel5.add(jRTextExpressionAreaMapExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Parameters map exp", jPanel5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText("Connection / Datasource Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel6.add(jLabel41, gridBagConstraints);

        jComboBoxDatasetConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxDatasetConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetConnectionTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel6.add(jComboBoxDatasetConnectionType, gridBagConstraints);

        jRTextExpressionAreaTextConnectionExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
        jRTextExpressionAreaTextConnectionExpression.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTextConnectionExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanel6.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jTabbedPaneSubDataset.addTab("Connection/Datasource exp", jPanel6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel7.add(jTabbedPaneSubDataset, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelDataset.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButtonCopy.setText("Copy dataset");
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel2.add(jButtonCopy, gridBagConstraints);

        jButtonPaste.setText("Paste dataset");
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteActionPerformed(evt);
            }
        });

        jPanel2.add(jButtonPaste, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 101;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        jPanelDataset.add(jPanel2, gridBagConstraints);

        jRTextExpressionAreaFilterExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaFilterExpression.setCaretVisible(false);
        jRTextExpressionAreaFilterExpression.setElectricScroll(0);
        jRTextExpressionAreaFilterExpression.setMinimumSize(new java.awt.Dimension(400, 50));
        jRTextExpressionAreaFilterExpression.setPreferredSize(new java.awt.Dimension(400, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanelDataset.add(jRTextExpressionAreaFilterExpression, gridBagConstraints);

        jTabbedPaneData.addTab("Dataset", jPanelDataset);

        jPanelDataDefinition.setLayout(new java.awt.GridBagLayout());

        jTabbedPaneData.addTab("Details", jPanelDataDefinition);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        jPanelData.add(jTabbedPaneData, gridBagConstraints);

        jTabbedPane1.addTab("Chart data", jPanelData);

        jPanelMultiAxis.setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Chart", "Axis position"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked1(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelMultiAxis.add(jScrollPane1, gridBagConstraints);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText("Add");
        jButtonAdd.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel9.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText("Edit chart");
        jButtonModify.setEnabled(false);
        jButtonModify.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel9.add(jButtonModify, gridBagConstraints);

        jButtonDelete.setText("Delete");
        jButtonDelete.setEnabled(false);
        jButtonDelete.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel9.add(jButtonDelete, gridBagConstraints);

        jButtonMoveUp.setText("Move up");
        jButtonMoveUp.setEnabled(false);
        jButtonMoveUp.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel9.add(jButtonMoveUp, gridBagConstraints);

        jButtonMoveDown.setText("Move down");
        jButtonMoveDown.setEnabled(false);
        jButtonMoveDown.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButtonMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 14, 4);
        jPanel9.add(jButtonMoveDown, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelMultiAxis.add(jPanel9, gridBagConstraints);

        jTabbedPane1.addTab("Multiaxis charts", jPanelMultiAxis);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        jPanel8.add(jButtonClose, gridBagConstraints);

        getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        try {
        ((ChartDatasetPanel)jPanelDataDefinition.getComponent(0)).containerWindowOpened();
        } catch (Exception ex) {}
        
        if (meterIntervalsHilightExpression != null)
        {
            MeterIntervalsSheetProperty misp = (MeterIntervalsSheetProperty)this.sheetPanel.getSheetProperty( "meterIntervalsMeterPlot" );
             if (misp != null)
             {
                misp.setFocusedExpression( meterIntervalsHilightExpression );
             }
        }
        else if (subdatasetParameterHighlightExpression != null)
        {
            jButtonModParameterActionPerformed(new ActionEvent(jButtonModParameter,0,""));
        }
        
    }//GEN-LAST:event_formWindowOpened

    private void jTable1MouseClicked1(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked1

        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            jButtonModifyActionPerformed(null);
        }
    }//GEN-LAST:event_jTable1MouseClicked1

    private void jButtonMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveDownActionPerformed

        if (jTable1.getSelectedRowCount() > 0) {
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
            int[] indices = jTable1.getSelectedRows();
            java.util.List axes = ((MultiAxisPlot)currentSelectedChartElement.getChart().getPlot() ).getAxis();
            
            for (int i=indices.length-1; i>=0; --i) {
                if (indices[i] >= (jTable1.getRowCount() -1)) continue;
                
                Axis axis = (Axis)jTable1.getValueAt( indices[i], 0);
                dtm.removeRow(indices[i]);
                axes.remove(indices[i]);
                dtm.insertRow(indices[i]+1, new Object[] { axis, axis.getPosition() } );
                axes.add(indices[i]+1, axis);
                indices[i]++;
            }
            
            DefaultListSelectionModel dlsm = (DefaultListSelectionModel)jTable1.getSelectionModel();
            dlsm.setValueIsAdjusting(true);
            dlsm.clearSelection();
            for (int i=0; i<indices.length; ++i) {
                dlsm.addSelectionInterval(indices[i],  indices[i]);
            }
            dlsm.setValueIsAdjusting( false );
            jTable1.updateUI();
        }
    }//GEN-LAST:event_jButtonMoveDownActionPerformed

    private void jButtonMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMoveUpActionPerformed
        if (jTable1.getSelectedRow() > 0) {
            DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
            java.util.List axes = ((MultiAxisPlot)currentSelectedChartElement.getChart().getPlot() ).getAxis();
            
            int[] indices = jTable1.getSelectedRows();
            for (int i=0;  i < indices.length; ++i) {
                if (indices[i] == 0) continue;
                
                Axis axis = (Axis)jTable1.getValueAt( indices[i], 0);
                dtm.removeRow(indices[i]);
                axes.remove(indices[i]);
                dtm.insertRow(indices[i]-1, new Object[] { axis, axis.getPosition() }  );
                axes.add(indices[i]-1, axis);
                indices[i]--;
            }
            
            DefaultListSelectionModel dlsm = (DefaultListSelectionModel)jTable1.getSelectionModel();
            dlsm.setValueIsAdjusting(true);
            dlsm.clearSelection();
            for (int i=0; i<indices.length; ++i) {
                dlsm.addSelectionInterval(indices[i],  indices[i]);
            }
            dlsm.setValueIsAdjusting( false );
        }
    }//GEN-LAST:event_jButtonMoveUpActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        int[] indexes = jTable1.getSelectedRows();
        java.util.List axes = ((MultiAxisPlot)currentSelectedChartElement.getChart().getPlot() ).getAxis();
        
        for (int i=indexes.length-1;  i>=0; --i) {
            ((DefaultTableModel)jTable1.getModel()).removeRow(indexes[i]);
            axes.remove(indexes[i]);
        }
        jTable1.updateUI();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed
        int index = jTable1.getSelectedRow();
        if (index >=0) {
            Axis c = (Axis)jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            ChartPropertiesDialog cpd = new ChartPropertiesDialog(this, true);
            cpd.setChartElement( c.getChartReportElement() );
            cpd.setVisible(true);
        }
    }//GEN-LAST:event_jButtonModifyActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        ChartSelectionJDialog chartSelector = new ChartSelectionJDialog(this, true);
        chartSelector.setMultiAxisMode(true);
        chartSelector.setVisible(true);
        
        if (chartSelector.getDialogResult() == JOptionPane.OK_OPTION) {
            
            Axis axis = new Axis();
            axis.getChartReportElement().setChart( chartSelector.getChart());
        
            ((DefaultTableModel)jTable1.getModel()).addRow(new Object[] { axis, axis.getPosition() });
            
            java.util.List axes = ((MultiAxisPlot)currentSelectedChartElement.getChart().getPlot() ).getAxis();
            axes.add(axis);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.notifyChange();
        
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        
        this.notifyChange();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jTableDatasetParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDatasetParametersMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
        {
            if (jTableDatasetParameters.getSelectedRowCount() > 0)
            {
                jButtonModParameterActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jTableDatasetParametersMouseClicked

    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
        
        while (jTableDatasetParameters.getSelectedRowCount() > 0) {
            int i=jTableDatasetParameters.getSelectedRow();
            currentSelectedChartElement.getChart().getDataset().getSubreportParameters().removeElement( jTableDatasetParameters.getValueAt( i, 0) );
            dtm.removeRow(i);
        }
    }//GEN-LAST:event_jButtonRemParameterActionPerformed

    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed
        
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
                int rowNumber = jTableDatasetParameters.getSelectedRow();
        JRSubreportParameter parameter = (JRSubreportParameter)jTableDatasetParameters.getValueAt( jTableDatasetParameters.getSelectedRow(), 0);
        
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setParameter( parameter );
        if (subdatasetParameterHighlightExpression != null)
        {
            jrpd.setFocusedExpression( ((Integer)subdatasetParameterHighlightExpression[0]).intValue() );
        }
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.setValueAt(parameter, rowNumber, 0);
            dtm.setValueAt(parameter.getExpression(), rowNumber, 1);
            jTableDatasetParameters.updateUI();
        }
    }//GEN-LAST:event_jButtonModParameterActionPerformed

    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        if (this.isInit()) return;
        
        if (currentSelectedChartElement == null) return;
        // Set the new value for all selected elements...
                
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRSubreportParameter parameter = jrpd.getParameter();
            currentSelectedChartElement.getChart().getDataset().getSubreportParameters().addElement( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getExpression()});
        }
    }//GEN-LAST:event_jButtonAddParameterActionPerformed

    private void jComboBoxSubDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubDatasetActionPerformed
        
        if (this.isInit()) return; 

       
            if (currentSelectedChartElement != null)
            {
                if (this.jComboBoxSubDataset.getSelectedIndex() > 0)
                {
                        if ( currentSelectedChartElement.getChart().getDataset().getSubDataset() != jComboBoxSubDataset.getSelectedItem())
                        {
                            currentSelectedChartElement.getChart().getDataset().setSubDataset( (SubDataset)jComboBoxSubDataset.getSelectedItem() );
                            setDatasetSubDataset( currentSelectedChartElement.getChart().getDataset().getSubDataset() );
                            
                            jTabbedPaneSubDataset.setVisible(true);
                        }
                        jRTextExpressionAreaFilterExpression.setSubDataset( (SubDataset)jComboBoxSubDataset.getSelectedItem() );
                // Check subdataset parameters.... (TODO)
                }
                else
                {
                    jRTextExpressionAreaFilterExpression.setSubDataset(null);
                    currentSelectedChartElement.getChart().getDataset().setSubDataset( null );
                    currentSelectedChartElement.getChart().getDataset().getSubreportParameters().removeAllElements();
                    currentSelectedChartElement.getChart().getDataset().setParametersMapExpression("");
                    currentSelectedChartElement.getChart().getDataset().setConnectionExpression("");
                    currentSelectedChartElement.getChart().getDataset().setDataSourceExpression("");
                    currentSelectedChartElement.getChart().getDataset().setUseConnection(false);
                    
                    setDatasetSubDataset( null );
                    
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                    dtm.setRowCount(0);
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    jRTextExpressionAreaMapExpression.setText("");
                
                    jTabbedPaneSubDataset.setVisible(false);
                    jTabbedPaneSubDataset.updateUI();
                }
        }
    }//GEN-LAST:event_jComboBoxSubDatasetActionPerformed

    private void jComboBoxDatasetConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetConnectionTypeActionPerformed
        if (isInit() || currentSelectedChartElement == null) return;
                
        if (jComboBoxDatasetConnectionType.getSelectedIndex() == 0) {
            currentSelectedChartElement.getChart().getDataset().setUseConnection(false);
            currentSelectedChartElement.getChart().getDataset().setConnectionExpression("");
            currentSelectedChartElement.getChart().getDataset().setDataSourceExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 1) {
            currentSelectedChartElement.getChart().getDataset().setUseConnection(true);
            currentSelectedChartElement.getChart().getDataset().setDataSourceExpression("");
            currentSelectedChartElement.getChart().getDataset().setConnectionExpression("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        }
        else if (jComboBoxDatasetConnectionType.getSelectedIndex() == 2) {
            currentSelectedChartElement.getChart().getDataset().setUseConnection(false);
            currentSelectedChartElement.getChart().getDataset().setDataSourceExpression("$P{MyDataource}");
            currentSelectedChartElement.getChart().getDataset().setConnectionExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{MyDataource}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_jComboBoxDatasetConnectionTypeActionPerformed

    private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed

        
        if (currentSelectedChartElement == null) return;
        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard() == null) return;
        
        Dataset theDataset = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard();
        
        currentSelectedChartElement.getChart().getDataset().setIncrementType( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getIncrementType());
        currentSelectedChartElement.getChart().getDataset().setIncrementGroup( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getIncrementGroup());
        currentSelectedChartElement.getChart().getDataset().setResetType( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getResetType());
        currentSelectedChartElement.getChart().getDataset().setResetGroup( it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getResetGroup());
        
        currentSelectedChartElement.getChart().getDataset().setUseConnection( theDataset.isUseConnection());
        currentSelectedChartElement.getChart().getDataset().setSubDataset( theDataset.getSubDataset());
        currentSelectedChartElement.getChart().getDataset().setParametersMapExpression( theDataset.getParametersMapExpression());
        currentSelectedChartElement.getChart().getDataset().setConnectionExpression( theDataset.getConnectionExpression());
        currentSelectedChartElement.getChart().getDataset().setDataSourceExpression( theDataset.getDataSourceExpression());
        
        for (int i=0; i< theDataset.getSubreportParameters().size(); ++i)
        {
            currentSelectedChartElement.getChart().getDataset().getSubreportParameters().add( ((JRSubreportParameter)theDataset.getSubreportParameters().elementAt(i)).cloneMe() );
        }
            
        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().getClass().getName().equals(
            currentSelectedChartElement.getChart().getDataset().getClass().getName() ) )
        {
            currentSelectedChartElement.getChart().setDataset(  it.businesslogic.ireport.gui.MainFrame.getMainInstance().getChartDatasetClipBoard().cloneMe() );
            this.setDatasetPanel( currentSelectedChartElement.getChart().getDataset() );
        }
        
        init = true;
        
        Misc.setComboboxSelectedTagValue(jComboBoxIncrementType, currentSelectedChartElement.getChart().getDataset().getIncrementType());
        jComboBoxIncrementGroup.setSelectedItem( currentSelectedChartElement.getChart().getDataset().getIncrementGroup() );
        Misc.setComboboxSelectedTagValue(jComboBoxResetType, currentSelectedChartElement.getChart().getDataset().getResetType());
        jComboBoxResetGroup.setSelectedItem( currentSelectedChartElement.getChart().getDataset().getResetGroup() );
        
        if (currentSelectedChartElement.getChart().getDataset().getSubDataset() != null)
            {
                jComboBoxSubDataset.setSelectedItem(currentSelectedChartElement.getChart().getDataset().getSubDataset());
                jTabbedPaneSubDataset.setVisible(true);
            
                jRTextExpressionAreaMapExpression.setText( currentSelectedChartElement.getChart().getDataset().getParametersMapExpression()  );
                if (!currentSelectedChartElement.getChart().getDataset().isUseConnection() &&  Misc.nvl( currentSelectedChartElement.getChart().getDataset().getDataSourceExpression(),"").trim().equals("")) {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                    this.jRTextExpressionAreaTextConnectionExpression.setText("");
                }
                else if (currentSelectedChartElement.getChart().getDataset().isUseConnection()) {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(1);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                    this.jRTextExpressionAreaTextConnectionExpression.setText( currentSelectedChartElement.getChart().getDataset().getConnectionExpression());
                }
                else {
                    this.jComboBoxDatasetConnectionType.setSelectedIndex(2);
                    this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                    this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                    this.jRTextExpressionAreaTextConnectionExpression.setText( currentSelectedChartElement.getChart().getDataset().getDataSourceExpression());
                }

                //Add parameters...
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);

                java.util.Enumeration enum_parameters = currentSelectedChartElement.getChart().getDataset().getSubreportParameters().elements();
                while (enum_parameters.hasMoreElements()) {
                    it.businesslogic.ireport.JRSubreportParameter parameter = (it.businesslogic.ireport.JRSubreportParameter)enum_parameters.nextElement();
                    Vector row = new Vector();
                    row.addElement(parameter);
                    row.addElement(parameter.getExpression());
                    dtm.addRow(row);
                }            
            
            }
            else
            {
                javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableDatasetParameters.getModel();
                dtm.setRowCount(0);
                this.jComboBoxDatasetConnectionType.setSelectedIndex(0);
                this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                this.jRTextExpressionAreaTextConnectionExpression.setText("");
                jRTextExpressionAreaMapExpression.setText("");
                
                jComboBoxSubDataset.setSelectedIndex(0);
                jTabbedPaneSubDataset.setVisible(false);
            }
        
        init = false;
        
    }//GEN-LAST:event_jButtonPasteActionPerformed

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed

        if (currentSelectedChartElement == null) return;
        it.businesslogic.ireport.gui.MainFrame.getMainInstance().setChartDatasetClipBoard( currentSelectedChartElement.getChart().getDataset().cloneMe() );
        jButtonPaste.setEnabled(true);
    }//GEN-LAST:event_jButtonCopyActionPerformed

    private void jComboBoxTypeOfDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTypeOfDataActionPerformed

        if (isInit() || currentSelectedChartElement == null || !(currentSelectedChartElement.getChart() instanceof XYBarChart )) return;
        
        if (jComboBoxTypeOfData.getSelectedItem() != null)
        {
            String typeOfDataset = ((Tag)jComboBoxTypeOfData.getSelectedItem()).getValue()+"";
            if (currentSelectedChartElement.getChart().getDataset().getClass().getName().equals(typeOfDataset)) return;
            
            try {
                jPanelDataDefinition.removeAll();
                Dataset dataset = (Dataset)this.getClass().forName( typeOfDataset ).newInstance();
                currentSelectedChartElement.getChart().setDataset(dataset);
                
            
            } catch (Exception ex)
            {
                 currentSelectedChartElement.getChart().setDataset(new Dataset());
            }
            setDatasetPanel(currentSelectedChartElement.getChart().getDataset());
            
            this.notifyChange();
        }
        
    }//GEN-LAST:event_jComboBoxTypeOfDataActionPerformed

    
    
    
    
    private void jComboBoxIncrementGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementGroupActionPerformed
        if (isInit() || currentSelectedChartElement == null) return;
        currentSelectedChartElement.getChart().getDataset().setIncrementGroup( ""+jComboBoxIncrementGroup.getSelectedItem());
    }//GEN-LAST:event_jComboBoxIncrementGroupActionPerformed

    private void jComboBoxIncrementTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIncrementTypeActionPerformed
        
        if (isInit() || currentSelectedChartElement == null) return;
        currentSelectedChartElement.getChart().getDataset().setIncrementType( ((Tag)jComboBoxIncrementType.getSelectedItem()).getValue()+"");
        
        if (currentSelectedChartElement.getChart().getDataset().getIncrementType().equals("Group"))
        {
            // We have to removethe null entry...
            if (jComboBoxIncrementGroup.getItemAt(0).equals(""))
            {
                jComboBoxIncrementGroup.removeItemAt(0);
            }
            jComboBoxIncrementGroup.setSelectedIndex(0);
        }
        else
        {
            if (!jComboBoxIncrementGroup.getItemAt(0).equals(""))
            {
                jComboBoxIncrementGroup.insertItemAt("",0);
            }
            jComboBoxIncrementGroup.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jComboBoxIncrementTypeActionPerformed

    private void jComboBoxResetGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetGroupActionPerformed

        if (isInit() || currentSelectedChartElement == null) return;
        currentSelectedChartElement.getChart().getDataset().setResetGroup( ""+jComboBoxResetGroup.getSelectedItem());
        
    }//GEN-LAST:event_jComboBoxResetGroupActionPerformed

    private void jComboBoxResetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxResetTypeActionPerformed

        if (isInit() || currentSelectedChartElement == null) return;
        currentSelectedChartElement.getChart().getDataset().setResetType( ((Tag)jComboBoxResetType.getSelectedItem()).getValue()+"" );
        
        if (currentSelectedChartElement.getChart().getDataset().getResetType().equals("Group"))
        {
            // We have to remove the null entry...
            if (jComboBoxResetGroup.getItemAt(0).equals(""))
            {
                jComboBoxResetGroup.removeItemAt(0);
            }
            jComboBoxResetGroup.setSelectedIndex(0);
        }
        else
        {
            if (!jComboBoxResetGroup.getItemAt(0).equals(""))
            {
                jComboBoxResetGroup.insertItemAt("",0);
            }
            jComboBoxResetGroup.setSelectedIndex(0);
        }
        
    }//GEN-LAST:event_jComboBoxResetTypeActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JButton jButtonMoveDown;
    private javax.swing.JButton jButtonMoveUp;
    private javax.swing.JButton jButtonPaste;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JComboBox jComboBoxDatasetConnectionType;
    private javax.swing.JComboBox jComboBoxIncrementGroup;
    private javax.swing.JComboBox jComboBoxIncrementType;
    private javax.swing.JComboBox jComboBoxResetGroup;
    private javax.swing.JComboBox jComboBoxResetType;
    private javax.swing.JComboBox jComboBoxSubDataset;
    private javax.swing.JComboBox jComboBoxTypeOfData;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabelIncrementGroup;
    private javax.swing.JLabel jLabelIncrementType;
    private javax.swing.JLabel jLabelIncrementType1;
    private javax.swing.JLabel jLabelIncrementType2;
    private javax.swing.JLabel jLabelResetGroup;
    private javax.swing.JLabel jLabelResetType;
    private javax.swing.JLabel jLabelTypeOfData;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelData;
    private javax.swing.JPanel jPanelDataDefinition;
    private javax.swing.JPanel jPanelDataset;
    private javax.swing.JPanel jPanelMultiAxis;
    private javax.swing.JPanel jPanelProperties;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaFilterExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaMapExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTextConnectionExpression;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPaneData;
    private javax.swing.JTabbedPane jTabbedPaneSubDataset;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableDatasetParameters;
    // End of variables declaration//GEN-END:variables
    
    /**
     * This method add entries in the properties sheet to edit all common chart properties.
     * The common chart properties are all the ones listed in the chart tag:
     * isShowLegend
     * evaluationTime
     * 
     */
    public void addCommonChartProperties()
    {
        if (currentSelectedChartElement != null)
        {
            ExpressionSheetProperty chartTitleProperty = new ExpressionSheetProperty("chartTitle",I18n.getString("chartPropertiesDialog.property.chartTitle","Chart title expression"));
            chartTitleProperty.setValue( currentSelectedChartElement.getChart().getTitle().getTitleExpression());
            
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartTitleProperty);
            
            FontSheetProperty chartTitleFontProperty = new FontSheetProperty("chartTitleFont",I18n.getString("chartPropertiesDialog.property.chartTitleFont","Chart title font"));
            chartTitleFontProperty.setValue(  currentSelectedChartElement.getChart().getTitle().getFont());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartTitleFontProperty);
            
            SheetProperty chartTitleColorProperty = new SheetProperty("chartTitleColor",I18n.getString("chartPropertiesDialog.property.chartTitleColor","Chart title color"), SheetProperty.COLOR);
            chartTitleColorProperty.setValue(  currentSelectedChartElement.getChart().getTitle().getColor());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartTitleColorProperty);
            
            SheetProperty chartTitlePositionProperty = new SheetProperty("chartTitlePosition",I18n.getString("chartPropertiesDialog.property.chartTitlePosition","Chart title position"), SheetProperty.COMBOBOX);
            chartTitlePositionProperty.setTags( new Tag[]{ new Tag("Top","Top"),
                                                           new Tag("Bottom","Bottom"),
                                                           new Tag("Left","Left"),
                                                           new Tag("Right","Right")});
            chartTitlePositionProperty.setValue(  currentSelectedChartElement.getChart().getTitle().getPosition());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartTitlePositionProperty);
            
            ExpressionSheetProperty chartSubTitleProperty = new ExpressionSheetProperty("chartSubTitle",I18n.getString("chartPropertiesDialog.property.chartSubTitle","Chart subtitle expression"));
            chartSubTitleProperty.setValue( currentSelectedChartElement.getChart().getSubTitle().getTitleExpression());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartSubTitleProperty);
            
            FontSheetProperty chartSubTitleFontProperty = new FontSheetProperty("chartSubTitleFont",I18n.getString("chartPropertiesDialog.property.chartSubTitleFont","Chart subtitle font"));
            chartSubTitleFontProperty.setValue(  currentSelectedChartElement.getChart().getSubTitle().getFont());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartSubTitleFontProperty);
            
            SheetProperty chartSubTitleColorProperty = new SheetProperty("chartSubTitleColor",I18n.getString("chartPropertiesDialog.property.chartSubTitleColor","Chart subtitle color"), SheetProperty.COLOR);
            chartSubTitleColorProperty.setValue( currentSelectedChartElement.getChart().getSubTitle().getColor());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartSubTitleColorProperty);

            SheetProperty isShowLegendProperty = new SheetProperty("isShowLegend",I18n.getString("chartPropertiesDialog.property.isShowLegend","Show legend"), SheetProperty.BOOLEAN);
            isShowLegendProperty.setValue(new Boolean( currentSelectedChartElement.getChart().isShowLegend() ));
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), isShowLegendProperty);
            
            // From jr 1.2.7 -------
            FontSheetProperty chartLegendFontProperty = new FontSheetProperty("chartLegendFont",I18n.getString("chartPropertiesDialog.property.chartLegendFont","Legend font"));
            chartLegendFontProperty.setShowResetButton(true);
            chartLegendFontProperty.setValue(  currentSelectedChartElement.getChart().getLegend().getFont());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartLegendFontProperty);
            
            SheetProperty chartLegendTextColorProperty = new SheetProperty("chartLegendTextColor",I18n.getString("chartPropertiesDialog.property.chartLegendTextColor","Legend text color"), SheetProperty.COLOR);
            chartLegendTextColorProperty.setValue(  currentSelectedChartElement.getChart().getLegend().getTextColor());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartLegendTextColorProperty);
            
            SheetProperty chartLegendBackgroundColorProperty = new SheetProperty("chartLegendBackgroundColor",I18n.getString("chartPropertiesDialog.property.chartLegendBackgroundColor","Legend background color"), SheetProperty.COLOR);
            chartLegendBackgroundColorProperty.setValue(  currentSelectedChartElement.getChart().getLegend().getBackgroundColor());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartLegendBackgroundColorProperty);
            // end from jr 1.2.7 ------
            
            SheetProperty plotOrientationProperty = new SheetProperty("orientation",I18n.getString("chartPropertiesDialog.property.orientation","Orientation"), SheetProperty.COMBOBOX);
            plotOrientationProperty.setTags( new Tag[]{ new Tag("Vertical","Vertical"),
                                                        new Tag("Horizontal","Horizontal")});
            plotOrientationProperty.setValue(  currentSelectedChartElement.getChart().getPlot().getOrientation());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), plotOrientationProperty);
            
            SheetProperty chartBackcolorProperty = new SheetProperty("chartBackcolor",I18n.getString("chartPropertiesDialog.property.chartBackcolor","Backcolor"), SheetProperty.COLOR);
            chartBackcolorProperty.setValue( currentSelectedChartElement.getChart().getPlot().getBackcolor() );
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), chartBackcolorProperty);
            
            SheetProperty backgroundAlphaProperty = new DoubleSheetProperty("backgroundAlpha",I18n.getString("chartPropertiesDialog.property.backgroundAlpha","Background Alpha (%)"));
            backgroundAlphaProperty.setValue( new Double(currentSelectedChartElement.getChart().getPlot().getBackgroundAlpha() ));
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), backgroundAlphaProperty);
            
            SheetProperty foregroundAlphaProperty = new DoubleSheetProperty("foregroundAlpha",I18n.getString("chartPropertiesDialog.property.foregroundAlpha","Foreground Alpha (%)"));
            foregroundAlphaProperty.setValue( new Double(currentSelectedChartElement.getChart().getPlot().getForegroundAlpha() ));
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), foregroundAlphaProperty);
            
            SheetProperty labelRotationProperty = new DoubleSheetProperty("labelRotation",I18n.getString("chartPropertiesDialog.property.labelRotation","Label rotation"));
            labelRotationProperty.setValue( new Double(currentSelectedChartElement.getChart().getPlot().getLabelRotation() ));
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), labelRotationProperty);
            
            SheetProperty seriesColorsProperty = new SeriesColorsSheetProperty("seriesColors",I18n.getString("chartPropertiesDialog.property.seriesColors","Series colors"));
            seriesColorsProperty.setValue( currentSelectedChartElement.getChart().getPlot().getSeriesColors() );
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), seriesColorsProperty);
            
            SheetProperty customizerClassProperty = new SheetProperty("customizerClass",I18n.getString("chartPropertiesDialog.property.customizerClass","Customizer class"), SheetProperty.STRING);
            customizerClassProperty.setValue( currentSelectedChartElement.getChart().getCustomizerClass());
            sheetPanel.addSheetProperty(I18n.getString("chartPropertiesDialog.propertySet.Common","Common"), customizerClassProperty);
            
            
        }
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public JReportFrame getJReportFrame() {
        return jReportFrame;
    }

    public void setJReportFrame(JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
        
    }
    
     public void sheetPropertyValueChanged(SheetPropertyValueChangedEvent evt)
     {
         if (currentSelectedChartElement == null) return;
        
         String property = evt.getPropertyName();
         Plot currentPlot = currentSelectedChartElement.getChart().getPlot();

         if (property.equals("chartTitle")) currentSelectedChartElement.getChart().getTitle().setTitleExpression( ""+evt.getNewValue() );
         else if (property.equals("chartTitleFont")) currentSelectedChartElement.getChart().getTitle().setFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("chartTitleColor")) currentSelectedChartElement.getChart().getTitle().setColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("chartTitlePosition")) currentSelectedChartElement.getChart().getTitle().setPosition( ""+evt.getNewValue() );
         else if (property.equals("chartSubTitle")) currentSelectedChartElement.getChart().getSubTitle().setTitleExpression( ""+evt.getNewValue() );
         else if (property.equals("chartSubTitleFont")) currentSelectedChartElement.getChart().getSubTitle().setFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("chartSubTitleColor")) currentSelectedChartElement.getChart().getSubTitle().setColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("isShowLegend")) currentSelectedChartElement.getChart().setShowLegend( ((Boolean)evt.getNewValue()).booleanValue() );
         else if (property.equals("chartLegendFont")) currentSelectedChartElement.getChart().getLegend().setFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("chartLegendTextColor")) currentSelectedChartElement.getChart().getLegend().setTextColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("chartLegendBackgroundColor")) currentSelectedChartElement.getChart().getLegend().setBackgroundColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("chartBackcolor")) currentPlot.setBackcolor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("orientation")) currentPlot.setOrientation( ""+evt.getNewValue() );
         else if (property.equals("seriesColors")) currentPlot.setSeriesColors( (java.util.List)evt.getNewValue() );
         else if (property.equals("customizerClass")) currentSelectedChartElement.getChart().setCustomizerClass( ""+evt.getNewValue() );
         else if (property.equals("backgroundAlpha")) {
             double newval = ((Double)evt.getNewValue()).doubleValue();
             if (newval < 0.0 || newval > 1.0)
             {
                 JOptionPane.showMessageDialog(this,
                         I18n.getString("messages.error.backGroundAlphaInvalidValue",
                         "Background Alpha must be a number between 0 and 1."),
                         I18n.getString("messages.error.valueNotValid","Value not valid"),JOptionPane.ERROR_MESSAGE);
                 sheetPanel.setPropertyValue(evt.getPropertyName(), evt.getOldValue() );
             }
             else
             {
                currentPlot.setBackgroundAlpha( ((Double)evt.getNewValue()).doubleValue());
             }
         }
         else if (property.equals("foregroundAlpha")){
             double newval = ((Double)evt.getNewValue()).doubleValue();
             if (newval < 0.0 || newval > 1.0)
             {
                 JOptionPane.showMessageDialog(this,
                         I18n.getString("messages.error.foreGroundAlphaInvalidValue","Foreground Alpha must be a number between 0 and 1."),
                         I18n.getString("messages.error.valueNotValid","Value not valid"),JOptionPane.ERROR_MESSAGE);
                 sheetPanel.setPropertyValue(evt.getPropertyName(), evt.getOldValue() );
             }
             else
             {
                currentPlot.setForegroundAlpha( ((Double)evt.getNewValue()).doubleValue());
             }
         }
         else if (property.equals("labelRotation")){
             double newval = ((Double)evt.getNewValue()).doubleValue();
             //if (newval < 0.0 || newval > 365)
             //{
             //    JOptionPane.showMessageDialog(this,"Label rotation must be a number between 0 and 365.","Value not valid",JOptionPane.ERROR_MESSAGE);
             //    sheetPanel.setPropertyValue(evt.getPropertyName(), evt.getOldValue() );
             //}
             //else
             //{
                currentPlot.setLabelRotation( ((Double)evt.getNewValue()).doubleValue());
             //}
         }
         else if (property.equals("isCircular") && currentPlot instanceof PiePlot) ((PiePlot)currentPlot).setCircular( ((Boolean)evt.getNewValue()).booleanValue());
         else if (property.equals("isCircular") && currentPlot instanceof Pie3DPlot) ((Pie3DPlot)currentPlot).setCircular( ((Boolean)evt.getNewValue()).booleanValue() );
         else if (property.equals("depthFactor") && currentPlot instanceof Pie3DPlot) ((Pie3DPlot)currentPlot).setDepthFactor( ((Double)evt.getNewValue()).doubleValue() );
         
         else if (property.equals("isShowLabelsBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).setShowLabels(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowTickMarksBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).setShowTickMarks(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowTickLabelsBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).setShowTickLabels(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("categoryAxisLabelExpressionBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).setCategoryAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // Bar plot > categoryAxisFormat
         else if (property.equals("categoryAxisLabelColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelMaskBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("categoryAxisLineColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisLabelFontBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("categoryAxisTickLabelFontBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getCategoryAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Bar plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontBarPlot") && currentPlot instanceof BarPlot) ((BarPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         else if (property.equals("isShowLabelsBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).setShowLabels(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("xOffsetBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).setXOffset( ((Double)evt.getNewValue()).doubleValue() );
         else if (property.equals("yOffsetBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).setYOffset( ((Double)evt.getNewValue()).doubleValue() );
         else if (property.equals("categoryAxisLabelExpressionBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).setCategoryAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // Bar3D plot > categoryAxisFormat
         else if (property.equals("categoryAxisLabelColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelMaskBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("categoryAxisLineColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisLabelFontBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("categoryAxisTickLabelFontBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getCategoryAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Bar3D plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontBar3DPlot") && currentPlot instanceof Bar3DPlot) ((Bar3DPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         
         else if (property.equals("isShowLinesLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).setShowLines(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowShapesLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).setShowShapes(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("categoryAxisLabelExpressionLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).setCategoryAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // Line plot > categoryAxisFormat
         else if (property.equals("categoryAxisLabelColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelMaskLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("categoryAxisLineColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisLabelFontLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("categoryAxisTickLabelFontLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getCategoryAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Line plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontLinePlot") && currentPlot instanceof LinePlot) ((LinePlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("categoryAxisLabelExpressionAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).setCategoryAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // Area plot > categoryAxisFormat
         else if (property.equals("categoryAxisLabelColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisTickLabelMaskAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("categoryAxisLineColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("categoryAxisLabelFontAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("categoryAxisTickLabelFontAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getCategoryAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Area plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontAreaPlot") && currentPlot instanceof AreaPlot) ((AreaPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("isShowLinesScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).setShowLines(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowShapesScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).setShowShapes(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("xAxisLabelExpressionScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).setXAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("yAxisLabelExpressionScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).setYAxisLabelExpression( ""+evt.getNewValue()  );
         // Scatter plot > categoryAxisFormat
         else if (property.equals("xAxisLabelColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisTickLabelColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisTickLabelMaskScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("xAxisLineColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisLabelFontScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("xAxisTickLabelFontScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getXAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Scatter plot > valueAxisFormat
         else if (property.equals("yAxisLabelColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisTickLabelColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisTickLabelMaskScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("yAxisLineColorScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisLabelFontScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("yAxisTickLabelFontScatterPlot") && currentPlot instanceof ScatterPlot) ((ScatterPlot)currentPlot).getYAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("scaleTypeBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).setScaleType( ""+evt.getNewValue()  );
         else if (property.equals("xAxisLabelExpressionBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).setXAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("yAxisLabelExpressionBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).setYAxisLabelExpression( ""+evt.getNewValue()  );
         // Bubble plot > categoryAxisFormat
         else if (property.equals("xAxisLabelColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisTickLabelColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisTickLabelMaskBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("xAxisLineColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("xAxisLabelFontBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("xAxisTickLabelFontBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getXAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // Bubble plot > valueAxisFormat
         else if (property.equals("yAxisLabelColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisTickLabelColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisTickLabelMaskBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("yAxisLineColorBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("yAxisLabelFontBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("yAxisTickLabelFontBubblePlot") && currentPlot instanceof BubblePlot) ((BubblePlot)currentPlot).getYAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("isShowLinesTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).setShowLines(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowShapesTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).setShowShapes(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("timeAxisLabelExpressionTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).setTimeAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         	// TimeSeriesPlot plot > categoryAxisFormat
         else if (property.equals("timeAxisLabelColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelMaskTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("timeAxisLineColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisLabelFontTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("timeAxisTickLabelFontTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getTimeAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // TimeSeriesPlot plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontTimeSeriesPlot") && currentPlot instanceof TimeSeriesPlot) ((TimeSeriesPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("isShowOpenTicksHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).setShowOpenTicks(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("isShowCloseTicksHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).setShowCloseTicks(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("timeAxisLabelExpressionHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).setTimeAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // HighLowPlot plot > categoryAxisFormat
         else if (property.equals("timeAxisLabelColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelMaskHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("timeAxisLineColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisLabelFontHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("timeAxisTickLabelFontHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getTimeAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // HighLowPlot plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontHighLowPlot") && currentPlot instanceof HighLowPlot) ((HighLowPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         
         else if (property.equals("isShowVolumeCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).setShowVolume(  ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("timeAxisLabelExpressionCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).setTimeAxisLabelExpression( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLabelExpressionCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).setValueAxisLabelExpression( ""+evt.getNewValue()  );
         // CandlestickPlot plot > categoryAxisFormat
         else if (property.equals("timeAxisLabelColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisTickLabelMaskCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("timeAxisLineColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("timeAxisLabelFontCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("timeAxisTickLabelFontCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getTimeAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         // CandlestickPlot plot > valueAxisFormat
         else if (property.equals("valueAxisLabelColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setTickLabelColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisTickLabelMaskCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setTickLabelMask( ""+evt.getNewValue()  );
         else if (property.equals("valueAxisLineColorCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setAxisLineColor( (java.awt.Color) ColorSelectorPanel.parseColorString( ""+evt.getNewValue()) );
         else if (property.equals("valueAxisLabelFontCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("valueAxisTickLabelFontCandlestickPlot") && currentPlot instanceof CandlestickPlot) ((CandlestickPlot)currentPlot).getValueAxisFormat().setTickLabelFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         
         else if (property.equals("shapeMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setShape( "" + evt.getNewValue() );
         else if (property.equals("angleMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setAngle( ((Integer)evt.getNewValue()).intValue() );
         else if (property.equals("unitsMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setUnits( "" + evt.getNewValue() );
         else if (property.equals("tickIntervalMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setTickInterval( ((Double)evt.getNewValue()).doubleValue()  );
         else if (property.equals("meterColorMaterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setMeterColor( (java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("needleColorMaterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setNeedleColor((java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("tickColorMaterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setTickColor( (java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("vdColorMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).getValueDisplay().setColor( (java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("vdMaskMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).getValueDisplay().setMask( "" + evt.getNewValue() );
         else if (property.equals("vdFontMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).getValueDisplay().setFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("dataRangeLowExpressionMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).getDataRange().setLowExpression( "" + evt.getNewValue() );
         else if (property.equals("dataRangeHighExpressionMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).getDataRange().setHighExpression( "" + evt.getNewValue() );
         else if (property.equals("meterIntervalsMeterPlot") && currentPlot instanceof MeterPlot) ((MeterPlot)currentPlot).setMeterIntervals( (java.util.List)evt.getNewValue() );
        
         else if (property.equals("valueLocationThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).setValueLocation( "" + evt.getNewValue() );
         else if (property.equals("showValueLinesThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).setShowValueLines( ((Boolean)evt.getNewValue()).booleanValue()  );
         else if (property.equals("mercuryColorThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).setMercuryColor( (java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("vdColorThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getValueDisplay().setColor( (java.awt.Color)ColorSelectorPanel.parseColorString( ""+evt.getNewValue())   );
         else if (property.equals("vdMaskThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getValueDisplay().setMask( "" + evt.getNewValue() );
         else if (property.equals("vdFontThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getValueDisplay().setFont( (it.businesslogic.ireport.IReportFont)evt.getNewValue() );
         else if (property.equals("dataRangeLowExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getDataRange().setLowExpression( "" + evt.getNewValue() );
         else if (property.equals("dataRangeHighExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getDataRange().setHighExpression( "" + evt.getNewValue() );
         else if (property.equals("lowRangeLowExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getLowRange().setLowExpression( "" + evt.getNewValue() );
         else if (property.equals("lowRangeHighExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getLowRange().setHighExpression( "" + evt.getNewValue() );
         else if (property.equals("mediumRangeLowExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getMediumRange().setLowExpression( "" + evt.getNewValue() );
         else if (property.equals("mediumRangeHighExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getMediumRange().setHighExpression( "" + evt.getNewValue() );
         else if (property.equals("highRangeLowExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getHighRange().setLowExpression( "" + evt.getNewValue() );
         else if (property.equals("highRangeHighExpressionThermometerPlot") && currentPlot instanceof ThermometerPlot) ((ThermometerPlot)currentPlot).getHighRange().setHighExpression( "" + evt.getNewValue() );
     
     
         if (getJReportFrame() != null){
             this.getJReportFrame().getReport().incrementReportChanges();
         }
     }
     
     
     /**
     * This method update the comboboxes where is present the goup list.
     */
    public void updateGroups()
    {

        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame() == null)
	{
            jComboBoxResetGroup.removeAllItems();
            jComboBoxResetGroup.addItem("");
        }	
        else
        {
            Misc.updateStringComboBox(jComboBoxResetGroup, 
                                      it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getGroups(), true);
            Misc.updateStringComboBox(jComboBoxIncrementGroup, 
                                      it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getGroups(), true);
        }
        
    }
    
    
    public void updateSubDatasets()
    {

        if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame() == null)
	{
            jComboBoxSubDataset.removeAllItems();
            jComboBoxSubDataset.addItem("");
        }	
        else
        {
            Misc.updateComboBox(jComboBoxSubDataset, it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getSubDatasets(), true);
        }
    }
    
    public void applyI18n()
        {
                // Start autogenerated code ----------------------
                jButtonAdd.setText(I18n.getString("chartPropertiesDialog.buttonAdd","Add"));
                jButtonAddParameter.setText(I18n.getString("chartPropertiesDialog.buttonAddParameter","Add"));
                jButtonClose.setText(I18n.getString("chartPropertiesDialog.buttonClose","Close"));
                jButtonDelete.setText(I18n.getString("chartPropertiesDialog.buttonDelete","Delete"));
                jButtonModParameter.setText(I18n.getString("chartPropertiesDialog.buttonModParameter","Modify"));
                jButtonModify.setText(I18n.getString("chartPropertiesDialog.buttonModify","Edit chart"));
                jButtonMoveDown.setText(I18n.getString("chartPropertiesDialog.buttonMoveDown","Move down"));
                jButtonMoveUp.setText(I18n.getString("chartPropertiesDialog.buttonMoveUp","Move up"));
                jButtonRemParameter.setText(I18n.getString("chartPropertiesDialog.buttonRemParameter","Remove"));
                jLabel26.setText(I18n.getString("chartPropertiesDialog.label26","Parameters Map Expression"));
                jLabel41.setText(I18n.getString("chartPropertiesDialog.label41","Connection / Datasource Expression"));
                jLabelIncrementType1.setText(I18n.getString("chartPropertiesDialog.labelIncrementType1","Sub dataset"));
                jLabelIncrementType2.setText(I18n.getString("chartPropertiesDialog.labelIncrementType2","Increment When expression"));
                // End autogenerated code ----------------------
            jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartProperties","Chart properties"));
            jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabChartData","Chart data"));
            jLabelTypeOfData.setText( it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.LabelTypeOfDataset","Type of dataset"));
            jTabbedPaneData.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabDataset","Dataset"));
            jTabbedPaneData.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.TabDatasetDetails","Details"));
            
            jLabelResetType.setText( it.businesslogic.ireport.util.I18n.getString("resetType","Reset type"));
            jLabelResetGroup.setText( it.businesslogic.ireport.util.I18n.getString("resetGroup","Reset group"));
            jLabelIncrementType.setText( it.businesslogic.ireport.util.I18n.getString("incrementType","Increment type"));
            jLabelIncrementGroup.setText( it.businesslogic.ireport.util.I18n.getString("incrementGroup","Increment group"));
            
            jButtonCopy.setText( it.businesslogic.ireport.util.I18n.getString("charts.copyDataset","Copy dataset"));
            jButtonPaste.setText( it.businesslogic.ireport.util.I18n.getString("charts.pasteDataset","Paste dataset"));
            
            this.setTitle(it.businesslogic.ireport.util.I18n.getString("gui.ChartPropertiesDialog.title","Chart properties"));
            
            jTableDatasetParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.parameter","Parameter") );
            jTableDatasetParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.expression","Expression") );
            
            jTable1.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.chart","Chart") );
            jTable1.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("chartPropertiesDialog.tablecolumn.axisPosition","Axis position") );
                   
            jTabbedPaneSubDataset.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.Parameters","Parameters"));
            jTabbedPaneSubDataset.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ParametersMapExp","Parameters map exp."));
            jTabbedPaneSubDataset.setTitleAt(2, it.businesslogic.ireport.util.I18n.getString("datasetRun.tab.ConnectionDatasourceExp","Connection/Datasource exp."));
            
            ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( I18n.getString("datasetRun.panelBorder.DatasetRun","Dataset run") );
            
            this.getRootPane().updateUI();
        }
    
    private void setDatasetPanel( Dataset dataset )
    {
        jPanelDataDefinition.removeAll();
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
                
        if (dataset instanceof TimePeriodDataset)
        {
             TimePeriodDatasetPanel pdp = new TimePeriodDatasetPanel();
             pdp.setTimePeriodDataset((TimePeriodDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof CategoryDataset)
        {
             CategoryDatasetPanel pdp = new CategoryDatasetPanel();
             pdp.setCategoryDataset((CategoryDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof PieDataset)
        {
             PieDatasetPanel pdp = new PieDatasetPanel();
             pdp.setPieDataset((PieDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof TimeSeriesDataset)
        {
             TimeSeriesDatasetPanel pdp = new TimeSeriesDatasetPanel();
             pdp.setTimeSeriesDataset((TimeSeriesDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof XYDataset)
        {
             XYDatasetPanel pdp = new XYDatasetPanel();
             pdp.setXYDataset((XYDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof XYZDataset)
        {
             XYZDatasetPanel pdp = new XYZDatasetPanel();
             pdp.setXYZDataset((XYZDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof HighLowDataset)
        {
            HighLowDatasetPanel pdp = new HighLowDatasetPanel();
             pdp.setHighLowDataset((HighLowDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        else if (dataset instanceof ValueDataset)
        {
            ValueDatasetPanel pdp = new ValueDatasetPanel();
             pdp.setValueDataset((ValueDataset)dataset);
             this.jPanelDataDefinition.add(pdp, gridBagConstraints);
        }
        
        Misc.setComboboxSelectedTagValue(jComboBoxTypeOfData, dataset.getClass().getName());
        
        if (dataset != null && dataset.getSubDataset() != null)
        {
            setDatasetSubDataset( dataset.getSubDataset() );
        }
        
        jPanelDataDefinition.updateUI();
    }
    
    
    /** 
     * this method notifies a chart dataset panel the subdataset used to fill it 
     */
    public void setDatasetSubDataset(SubDataset subdataset)
    {
        Component c = this.jPanelDataDefinition.getComponent(0);
        
        ((ChartDatasetPanel)c).setSubDataset( subdataset );
       
    }
    
    private void setPlotSheetProperties( Plot plot )
    {
        if (plot instanceof PiePlot)
        {
             SheetProperty isCircularProperty = new SheetProperty("isCircular",it.businesslogic.ireport.util.I18n.getString("charts.isCircular","Circular"), SheetProperty.BOOLEAN);
             isCircularProperty.setValue( new Boolean(((PiePlot)plot).isCircular()) );
             sheetPanel.addSheetProperty("Pie plot", isCircularProperty);
        }
        else if (plot instanceof Pie3DPlot)
        {
             SheetProperty isCircularProperty = new SheetProperty("isCircular",it.businesslogic.ireport.util.I18n.getString("charts.isCircular","Circular"), SheetProperty.BOOLEAN);
             isCircularProperty.setValue( new Boolean(((Pie3DPlot)plot).isCircular()) );
             sheetPanel.addSheetProperty("Pie3D plot", isCircularProperty);
             
             DoubleSheetProperty chartDepthFactorProperty = new DoubleSheetProperty("depthFactor",it.businesslogic.ireport.util.I18n.getString("charts.depthFactor","Depth factor"));
             chartDepthFactorProperty.setValue( new Double(((Pie3DPlot)plot).getDepthFactor()));
             sheetPanel.addSheetProperty("Pie3D plot", chartDepthFactorProperty);
        }
        else if (plot instanceof BarPlot)
        {
             SheetProperty isShowLabelsProperty = new SheetProperty("isShowLabelsBarPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowLabelsBarPlot","Show labels"), SheetProperty.BOOLEAN);
                isShowLabelsProperty.setValue( new Boolean(((BarPlot)plot).isShowLabels()) );
                sheetPanel.addSheetProperty("Bar plot", isShowLabelsProperty);
                
                SheetProperty isShowTickMarksProperty = new SheetProperty("isShowTickMarksBarPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowTickMarksBarPlot","Show tick marks"), SheetProperty.BOOLEAN);
                isShowTickMarksProperty.setValue( new Boolean(((BarPlot)plot).isShowTickMarks()) );
                sheetPanel.addSheetProperty("Bar plot", isShowTickMarksProperty);
                
                SheetProperty isShowTickLabelsProperty = new SheetProperty("isShowTickLabelsBarPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowTickLabelsBarPlot","Show tick labels"), SheetProperty.BOOLEAN);
                isShowTickLabelsProperty.setValue( new Boolean(((BarPlot)plot).isShowTickLabels()) );
                sheetPanel.addSheetProperty("Bar plot", isShowTickLabelsProperty);
                
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("categoryAxisLabelExpressionBarPlot",it.businesslogic.ireport.util.I18n.getString("charts.categoryAxisLabelExpressionPlot","Category axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((BarPlot)plot).getCategoryAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bar plot", categoryAxisLabelExpressionProperty);
            
                addAxisFormatEntries("category", ((BarPlot)plot).getCategoryAxisFormat(), "Bar plot", "BarPlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionBarPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((BarPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bar plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("value", ((BarPlot)plot).getValueAxisFormat(), "Bar plot", "BarPlot");
        }
        else if (plot instanceof Bar3DPlot)
        {
                SheetProperty isShowLabelsProperty = new SheetProperty("isShowLabelsBar3DPlot","Show labels", SheetProperty.BOOLEAN);
                isShowLabelsProperty.setValue( new Boolean(((Bar3DPlot)plot).isShowLabels()) );
                sheetPanel.addSheetProperty("Bar3D plot", isShowLabelsProperty);
                
                DoubleSheetProperty yOffsetProperty = new DoubleSheetProperty("xOffsetBar3DPlot","X offset");
                yOffsetProperty.setValue( new Double(((Bar3DPlot)plot).getXOffset()));
                sheetPanel.addSheetProperty("Bar3D plot", yOffsetProperty);
                
                DoubleSheetProperty xOffsetProperty = new DoubleSheetProperty("yOffsetBar3DPlot","Y offset");
                xOffsetProperty.setValue( new Double(((Bar3DPlot)plot).getYOffset()));
                sheetPanel.addSheetProperty("Bar3D plot", xOffsetProperty);
                
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("categoryAxisLabelExpressionBar3DPlot",it.businesslogic.ireport.util.I18n.getString("charts.categoryAxisLabelExpressionPlot","Category axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((Bar3DPlot)plot).getCategoryAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bar3D plot", categoryAxisLabelExpressionProperty);
            
                addAxisFormatEntries("category", ((Bar3DPlot)plot).getCategoryAxisFormat(), "Bar3D plot", "Bar3DPlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionBar3DPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((Bar3DPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bar3D plot", valueAxisLabelExpressionProperty);
                
                addAxisFormatEntries("value", ((Bar3DPlot)plot).getValueAxisFormat(), "Bar3D plot", "Bar3DPlot");
        }
        else if (plot instanceof LinePlot)
        {
                SheetProperty isShowLinesProperty = new SheetProperty("isShowLinesLinePlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowLinesPlot","Show lines"), SheetProperty.BOOLEAN);
                isShowLinesProperty.setValue( new Boolean(((LinePlot)plot).isShowLines()) );
                sheetPanel.addSheetProperty("Line plot", isShowLinesProperty);
                
                SheetProperty isShowShapesProperty = new SheetProperty("isShowShapesLinePlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowShapesPlot","Show shapes"), SheetProperty.BOOLEAN);
                isShowShapesProperty.setValue( new Boolean(((LinePlot)plot).isShowShapes()) );
                sheetPanel.addSheetProperty("Line plot", isShowShapesProperty);
                
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("categoryAxisLabelExpressionLinePlot",it.businesslogic.ireport.util.I18n.getString("charts.categoryAxisLabelExpressionPlot","Category axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((LinePlot)plot).getCategoryAxisLabelExpression() );
                sheetPanel.addSheetProperty("Line plot", categoryAxisLabelExpressionProperty);
            
                addAxisFormatEntries("category", ((LinePlot)plot).getCategoryAxisFormat(), "Line plot", "LinePlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionLinePlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((LinePlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Line plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("value", ((LinePlot)plot).getValueAxisFormat(), "Line plot", "LinePlot");
                
                
                
        }
        else if (plot instanceof AreaPlot)
        {
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("categoryAxisLabelExpressionAreaPlot",it.businesslogic.ireport.util.I18n.getString("charts.categoryAxisLabelExpressionPlot","Category axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((AreaPlot)plot).getCategoryAxisLabelExpression() );
                sheetPanel.addSheetProperty("Area plot", categoryAxisLabelExpressionProperty);
                
                addAxisFormatEntries("category", ((AreaPlot)plot).getCategoryAxisFormat(), "Area plot", "AreaPlot");
            
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionAreaPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((AreaPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Area plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("value", ((AreaPlot)plot).getValueAxisFormat(), "Area plot", "AreaPlot");
        }
        else if (plot instanceof ScatterPlot)
        {
                SheetProperty isShowLinesProperty = new SheetProperty("isShowLinesScatterPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowLinesPlot","Show lines"), SheetProperty.BOOLEAN);
                isShowLinesProperty.setValue( new Boolean(((ScatterPlot)plot).isShowLines()) );
                sheetPanel.addSheetProperty("Scatter plot", isShowLinesProperty);
                
                SheetProperty isShowShapesProperty = new SheetProperty("isShowShapesScatterPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowShapesPlot","Show shapes"), SheetProperty.BOOLEAN);
                isShowShapesProperty.setValue( new Boolean(((ScatterPlot)plot).isShowShapes()) );
                sheetPanel.addSheetProperty("Scatter plot", isShowShapesProperty);
                
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("xAxisLabelExpressionScatterPlot",it.businesslogic.ireport.util.I18n.getString("charts.xAxisLabelExpressionPlot","X axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((ScatterPlot)plot).getXAxisLabelExpression() );
                sheetPanel.addSheetProperty("Scatter plot", categoryAxisLabelExpressionProperty);
            
                addAxisFormatEntries("x", ((ScatterPlot)plot).getXAxisFormat(), "Scatter plot", "ScatterPlot");
            
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("yAxisLabelExpressionScatterPlot",it.businesslogic.ireport.util.I18n.getString("charts.yAxisLabelExpressionPlot","Y axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((ScatterPlot)plot).getYAxisLabelExpression() );
                sheetPanel.addSheetProperty("Scatter plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("y", ((ScatterPlot)plot).getYAxisFormat(), "Scatter plot", "ScatterPlot");
            
        }
        else if (plot instanceof BubblePlot)
        {
                //BothAxes | DomainAxis | RangeAxis
                SheetProperty scaleTypeProperty = new SheetProperty("scaleTypeBubblePlot",it.businesslogic.ireport.util.I18n.getString("charts.scaleTypeBubblePlot","Scale type"), SheetProperty.COMBOBOX);
                scaleTypeProperty.setTags( new Tag[]{ new Tag("BothAxes",it.businesslogic.ireport.util.I18n.getString("charts.scaleType.BothAxes","Both axes")),
                                                           new Tag("DomainAxis",it.businesslogic.ireport.util.I18n.getString("charts.scaleType.DomainAxis","Domain axis")),
                                                           new Tag("RangeAxis",it.businesslogic.ireport.util.I18n.getString("charts.scaleType.RangeAxis","Range axis"))});
               scaleTypeProperty.setValue(  ((BubblePlot)plot).getScaleType() );
               sheetPanel.addSheetProperty("Bubble plot", scaleTypeProperty);
                
                ExpressionSheetProperty categoryAxisLabelExpressionProperty = new ExpressionSheetProperty("xAxisLabelExpressionBubblePlot",it.businesslogic.ireport.util.I18n.getString("charts.xAxisLabelExpressionPlot","X axis label expression"));
                categoryAxisLabelExpressionProperty.setValue( ((BubblePlot)plot).getXAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bubble plot", categoryAxisLabelExpressionProperty);
                
                addAxisFormatEntries("x", ((BubblePlot)plot).getXAxisFormat(), "Bubble plot", "BubblePlot");
            
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("yAxisLabelExpressionBubblePlot",it.businesslogic.ireport.util.I18n.getString("charts.yAxisLabelExpressionPlot","Y axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((BubblePlot)plot).getYAxisLabelExpression() );
                sheetPanel.addSheetProperty("Bubble plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("y", ((BubblePlot)plot).getYAxisFormat(), "Bubble plot", "BubblePlot");
            
        }
        else if (plot instanceof TimeSeriesPlot)
        {
                SheetProperty isShowLinesProperty = new SheetProperty("isShowLinesTimeSeriesPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowLinesPlot","Show lines"), SheetProperty.BOOLEAN);
                isShowLinesProperty.setValue( new Boolean(((TimeSeriesPlot)plot).isShowLines()) );
                sheetPanel.addSheetProperty("Time series plot", isShowLinesProperty);
                
                SheetProperty isShowShapesProperty = new SheetProperty("isShowShapesTimeSeriesPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowShapesPlot","Show shapes"), SheetProperty.BOOLEAN);
                isShowShapesProperty.setValue( new Boolean(((TimeSeriesPlot)plot).isShowShapes()) );
                sheetPanel.addSheetProperty("Time series plot", isShowShapesProperty);
                
                ExpressionSheetProperty timeAxisLabelExpressionProperty = new ExpressionSheetProperty("timeAxisLabelExpressionTimeSeriesPlot",it.businesslogic.ireport.util.I18n.getString("charts.timeAxisLabelExpressionPlot","Time axis label expression"));
                timeAxisLabelExpressionProperty.setValue( ((TimeSeriesPlot)plot).getTimeAxisLabelExpression() );
                sheetPanel.addSheetProperty("Time series plot", timeAxisLabelExpressionProperty);
            
                addAxisFormatEntries("time", ((TimeSeriesPlot)plot).getTimeAxisFormat(), "Time series plot", "TimeSeriesPlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionTimeSeriesPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((TimeSeriesPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Time series plot", valueAxisLabelExpressionProperty);  
                
                addAxisFormatEntries("value", ((TimeSeriesPlot)plot).getValueAxisFormat(), "Time series plot", "TimeSeriesPlot");
        
        }
        else if (plot instanceof HighLowPlot)
        {
                SheetProperty isShowLinesProperty = new SheetProperty("isShowCloseTicksHighLowPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowCloseTicksPlot","Show close ticks"), SheetProperty.BOOLEAN);
                isShowLinesProperty.setValue( new Boolean(((HighLowPlot)plot).isShowCloseTicks()) );
                sheetPanel.addSheetProperty("High low plot", isShowLinesProperty);
                
                SheetProperty isShowShapesProperty = new SheetProperty("isShowOpenTicksHighLowPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowOpenTicksPlot","Show open ticks"), SheetProperty.BOOLEAN);
                isShowShapesProperty.setValue( new Boolean(((HighLowPlot)plot).isShowOpenTicks()) );
                sheetPanel.addSheetProperty("High low plot", isShowShapesProperty);
                
                ExpressionSheetProperty timeAxisLabelExpressionProperty = new ExpressionSheetProperty("timeAxisLabelExpressionHighLowPlot",it.businesslogic.ireport.util.I18n.getString("charts.timeAxisLabelExpressionPlot","Time axis label expression"));
                timeAxisLabelExpressionProperty.setValue( ((HighLowPlot)plot).getTimeAxisLabelExpression() );
                sheetPanel.addSheetProperty("High low plot", timeAxisLabelExpressionProperty);
            
                addAxisFormatEntries("time", ((HighLowPlot)plot).getTimeAxisFormat(), "High low plot", "HighLowPlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionHighLowPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((HighLowPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("High low plot", valueAxisLabelExpressionProperty);  
        
                addAxisFormatEntries("value", ((HighLowPlot)plot).getValueAxisFormat(), "High low plot", "HighLowPlot");
                
        }
        else if (plot instanceof CandlestickPlot)
        {
                SheetProperty isShowVolumeProperty = new SheetProperty("isShowVolumeCandlestickPlot",it.businesslogic.ireport.util.I18n.getString("charts.isShowVolumePlot","Show volume"), SheetProperty.BOOLEAN);
                isShowVolumeProperty.setValue( new Boolean(((CandlestickPlot)plot).isShowVolume()) );
                sheetPanel.addSheetProperty("Candlestick plot", isShowVolumeProperty);
                
                ExpressionSheetProperty timeAxisLabelExpressionProperty = new ExpressionSheetProperty("timeAxisLabelExpressionCandlestickPlot",it.businesslogic.ireport.util.I18n.getString("charts.timeAxisLabelExpressionPlot","Time axis label expression"));
                timeAxisLabelExpressionProperty.setValue( ((CandlestickPlot)plot).getTimeAxisLabelExpression() );
                sheetPanel.addSheetProperty("Candlestick plot", timeAxisLabelExpressionProperty);
            
                addAxisFormatEntries("time", ((CandlestickPlot)plot).getTimeAxisFormat(), "Candlestick plot", "CandlestickPlot");
                
                ExpressionSheetProperty valueAxisLabelExpressionProperty = new ExpressionSheetProperty("valueAxisLabelExpressionCandlestickPlot",it.businesslogic.ireport.util.I18n.getString("charts.valueAxisLabelExpressionPlot","Value axis label expression"));
                valueAxisLabelExpressionProperty.setValue(  ((CandlestickPlot)plot).getValueAxisLabelExpression() );
                sheetPanel.addSheetProperty("Candlestick plot", valueAxisLabelExpressionProperty);  
        
                addAxisFormatEntries("value", ((CandlestickPlot)plot).getValueAxisFormat(), "Candlestick plot", "CandlestickPlot");
        }
        else if (plot instanceof MeterPlot)
        {
                SheetProperty shapeProperty = new SheetProperty("shapeMeterPlot",it.businesslogic.ireport.util.I18n.getString("charts.shapeMeterPlot","Shape"), SheetProperty.COMBOBOX);
                shapeProperty.setTags( new Tag[]{ new Tag("chord",it.businesslogic.ireport.util.I18n.getString("charts.shape.Chord","Chord")),
                                                           new Tag("circle",it.businesslogic.ireport.util.I18n.getString("charts.shape.Circle","Circle")),
                                                           new Tag("pie",it.businesslogic.ireport.util.I18n.getString("charts.shape.Pie","Pie"))});
                shapeProperty.setValue(  ((MeterPlot)plot).getShape() );
                sheetPanel.addSheetProperty("Meter plot", shapeProperty);
                
                SheetProperty angleProperty = new SheetProperty("angleMeterPlot",it.businesslogic.ireport.util.I18n.getString("charts.angle","Angle"), SheetProperty.INTEGER);
                angleProperty.setValue( new Integer(((MeterPlot)plot).getAngle()));
                sheetPanel.addSheetProperty("Meter plot", angleProperty);
                
                SheetProperty unitsProperty = new SheetProperty("unitsMeterPlot",it.businesslogic.ireport.util.I18n.getString("charts.units","Units"), SheetProperty.STRING);
                unitsProperty.setValue( ((MeterPlot)plot).getUnits() );
                sheetPanel.addSheetProperty("Meter plot", unitsProperty);
                
                DoubleSheetProperty tickIntervalProperty = new DoubleSheetProperty("tickIntervalMeterPlot",it.businesslogic.ireport.util.I18n.getString("charts.tickInterval","Tick interval"));
                tickIntervalProperty.setValue( new Double(((MeterPlot)plot).getTickInterval()));
                sheetPanel.addSheetProperty("Meter plot", tickIntervalProperty);
                
                SheetProperty meterColorProperty = new SheetProperty("meterColorMaterPlot",it.businesslogic.ireport.util.I18n.getString("charts.meterColor","Meter color"), SheetProperty.COLOR);
                meterColorProperty.setValue(  ((MeterPlot)plot).getMeterColor() );
                sheetPanel.addSheetProperty("Meter plot", meterColorProperty);
                
                SheetProperty needleColorProperty = new SheetProperty("needleColorMaterPlot",it.businesslogic.ireport.util.I18n.getString("charts.needleColor","Needle color"), SheetProperty.COLOR);
                needleColorProperty.setValue(  ((MeterPlot)plot).getNeedleColor() );
                sheetPanel.addSheetProperty("Meter plot", needleColorProperty);
                
                SheetProperty tickColorProperty = new SheetProperty("tickColorMaterPlot",it.businesslogic.ireport.util.I18n.getString("charts.tickColor","Tick color"), SheetProperty.COLOR);
                tickColorProperty.setValue(  ((MeterPlot)plot).getTickColor() );
                sheetPanel.addSheetProperty("Meter plot", tickColorProperty);
                
                addValueDisplayEntries(((MeterPlot)plot).getValueDisplay(),"Meter plot", "MeterPlot");
                
                addDataRangeEntries("data",  ((MeterPlot)plot).getDataRange(), "Meter plot", "MeterPlot");
                
                SheetProperty meterIntervalsSheetProperty = new MeterIntervalsSheetProperty("meterIntervalsMeterPlot",it.businesslogic.ireport.util.I18n.getString("charts.meterIntervals","Meter intervals"));
                meterIntervalsSheetProperty.setValue( ((MeterPlot)plot).getMeterIntervals() );
                sheetPanel.addSheetProperty("Meter plot", meterIntervalsSheetProperty);
            
                
        }
        else if (plot instanceof ThermometerPlot)
        {
                SheetProperty shapeProperty = new SheetProperty("valueLocationThermometerPlot",it.businesslogic.ireport.util.I18n.getString("charts.valuelocation","Value location"), SheetProperty.COMBOBOX);
                shapeProperty.setTags( new Tag[]{ new Tag("none",it.businesslogic.ireport.util.I18n.getString("charts.valuelocation.none","None")),
                                                           new Tag("left",it.businesslogic.ireport.util.I18n.getString("charts.valuelocation.left","Left")),
                                                           new Tag("right",it.businesslogic.ireport.util.I18n.getString("charts.valuelocation.right","Right")),
                                                           new Tag("bulb",it.businesslogic.ireport.util.I18n.getString("charts.valuelocation.bulb","Bulb"))});
                shapeProperty.setValue(  ((ThermometerPlot)plot).getValueLocation() );
                sheetPanel.addSheetProperty("Thermometer plot", shapeProperty);
                
                SheetProperty showValueLinesProperty = new SheetProperty("showValueLinesThermometerPlot",it.businesslogic.ireport.util.I18n.getString("charts.showValueLines","Show value lines"), SheetProperty.BOOLEAN);
                showValueLinesProperty.setValue( new Boolean( ((ThermometerPlot)plot).isShowValueLines() ) );
                sheetPanel.addSheetProperty("Thermometer plot", showValueLinesProperty);
                
                SheetProperty mercuryColorProperty = new SheetProperty("mercuryColorThermometerPlot",it.businesslogic.ireport.util.I18n.getString("charts.mercuryColor","Mercury color"), SheetProperty.COLOR);
                mercuryColorProperty.setValue(  ((ThermometerPlot)plot).getMercuryColor() );
                sheetPanel.addSheetProperty("Thermometer plot", mercuryColorProperty);
                
                addValueDisplayEntries(((ThermometerPlot)plot).getValueDisplay(),"Thermometer plot", "ThermometerPlot");
                
                addDataRangeEntries("data",  ((ThermometerPlot)plot).getDataRange(), "Thermometer plot", "ThermometerPlot");
                
                addDataRangeEntries("low",  ((ThermometerPlot)plot).getLowRange(), "Thermometer plot", "ThermometerPlot");
                
                addDataRangeEntries("medium",  ((ThermometerPlot)plot).getMediumRange(), "Thermometer plot", "ThermometerPlot");
                
                addDataRangeEntries("high",  ((ThermometerPlot)plot).getHighRange(), "Thermometer plot", "ThermometerPlot");
                
        }

    }

    public boolean isInit() {
        return init || (currentSelectedChartElement == null);
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
 /**
 * Quick way to add a set of properties related to a ValueDisplay.<br>
 * Property name:   property[propertyNamePostfix]
 *
 * @param  axisFormat AxisFormat, the AxisFormat object from which take values
 * @param  categoryName String, the sheet category name. I.e. Line Plot
 * @param  propertyNamePostfix String, the value that complets the property name, i.e. LinePlot, BarPlot, ....
 */
   public void addValueDisplayEntries(ValueDisplay valueDisplay, String categoryName, String propertyNamePostfix)
    {
                SheetProperty vdColorProperty = new SheetProperty("vdColor" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts.valueColor","Value color"), SheetProperty.COLOR);
                vdColorProperty.setValue(  valueDisplay.getColor() );
                sheetPanel.addSheetProperty(categoryName, vdColorProperty);
                
                SheetProperty vdMaskProperty = new SheetProperty("vdMask" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts.valueMask","Value mask"), SheetProperty.STRING);
                vdMaskProperty.setValue( valueDisplay.getMask());
                sheetPanel.addSheetProperty(categoryName, vdMaskProperty);
            
                FontSheetProperty vdFontProperty = new FontSheetProperty("vdFont" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts.valueFont","Value font"));
                vdFontProperty.setValue(  valueDisplay.getFont() );
                sheetPanel.addSheetProperty(categoryName, vdFontProperty);           
    }
   
   /**
 * Quick way to add a set of properties related to an AxisFormat.<br>
 * Property name:   [axis]Property[propertyNamePostfix]
 *
 * @param  axisType  String, a name in {category, value, x, y, time}
 * @param  axisFormat AxisFormat, the AxisFormat object from which take values
 * @param  categoryName String, the sheet category name. I.e. Line Plot
 * @param  propertyNamePostfix String, the value that complets the property name, i.e. LinePlot, BarPlot, ....
 */
   public void addAxisFormatEntries(String axisType, AxisFormat axisFormat, String categoryName, String propertyNamePostfix)
    {
        // ---- Category Axis Format -----
                
                SheetProperty categoryAxisLabelColorPlot = new SheetProperty(axisType + "AxisLabelColor" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisLabelColorPlot","Category axis label color"), SheetProperty.COLOR);
                categoryAxisLabelColorPlot.setValue(  axisFormat.getLabelColor() );
                sheetPanel.addSheetProperty(categoryName, categoryAxisLabelColorPlot);
                
                SheetProperty categoryAxisTickLabelColorPlot = new SheetProperty(axisType + "AxisTickLabelColor" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisTickLabelColorPlot","Category axis tick label color"), SheetProperty.COLOR);
                categoryAxisTickLabelColorPlot.setValue(  axisFormat.getTickLabelColor() );
                sheetPanel.addSheetProperty(categoryName, categoryAxisTickLabelColorPlot);
                
                SheetProperty categoryAxisLabelMaskPlot = new SheetProperty(axisType + "AxisTickLabelMask" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisTickLabelMaskPlot","Category axis tick label mask"), SheetProperty.STRING);
                categoryAxisLabelMaskPlot.setValue( axisFormat.getTickLabelMask());
                sheetPanel.addSheetProperty(categoryName, categoryAxisLabelMaskPlot);
            
                SheetProperty categoryAxisLineColorPlot = new SheetProperty(axisType + "AxisLineColor" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisLineColorPlot","Category axis line color"), SheetProperty.COLOR);
                categoryAxisLineColorPlot.setValue(  axisFormat.getAxisLineColor() );
                sheetPanel.addSheetProperty(categoryName, categoryAxisLineColorPlot);
                
                FontSheetProperty categoryAxisLableFontPlot = new FontSheetProperty(axisType + "AxisLabelFont" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisLabelFontPlot","Category axis label font"));
                categoryAxisLableFontPlot.setValue(  axisFormat.getLabelFont() );
                sheetPanel.addSheetProperty(categoryName, categoryAxisLableFontPlot);
                
                FontSheetProperty categoryAxisTickLableFontPlot = new FontSheetProperty(axisType + "AxisTickLabelFont" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + axisType + "AxisTickLabelFontPlot","Category axis tick label font"));
                categoryAxisTickLableFontPlot.setValue(  axisFormat.getTickLabelFont() );
                sheetPanel.addSheetProperty(categoryName, categoryAxisTickLableFontPlot);
            
                // ---- End Category Axis Format -----
    }
   
   
    /**
 * Quick way to add a set of properties related to an AxisFormat.<br>
 * Property name:   [axis]Property[propertyNamePostfix]
 *
 * @param  rangeType  String, a name in {data, low, medium, high}
 * @param  range DataRange, the DataRange object from which take values
 * @param  categoryName String, the sheet category name. I.e. Line Plot
 * @param  propertyNamePostfix String, the value that complets the property name, i.e. LinePlot, BarPlot, ....
 */
   public void addDataRangeEntries(String rangeType, DataRange range, String categoryName, String propertyNamePostfix)
    {
                ExpressionSheetProperty rangeLowExpressionProperty = new ExpressionSheetProperty(rangeType + "RangeLowExpression" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + rangeType + "RangeLow","Data range low"));
                rangeLowExpressionProperty.setValue( range.getLowExpression());
                sheetPanel.addSheetProperty(categoryName, rangeLowExpressionProperty);
                
                ExpressionSheetProperty rangeHighExpressionProperty = new ExpressionSheetProperty(rangeType + "RangeHighExpression" + propertyNamePostfix,it.businesslogic.ireport.util.I18n.getString("charts." + rangeType + "RangeHigh","Data range high"));
                rangeHighExpressionProperty.setValue( range.getHighExpression());
                sheetPanel.addSheetProperty(categoryName, rangeHighExpressionProperty);           
    }
    
   
   public void jTable1editingStopped(ChangeEvent e)
   {
       int row = jTable1.getSelectedRow();
       
       //System.out.println("position changed!: " + jTable1.getValueAt(row,0) + " " + jTable1.getValueAt(row,1) + " " + jTable1.getValueAt(row,1).getClass().getName());
       
       ((Axis)jTable1.getValueAt(row,0)).setPosition( ""+((Tag)jTable1.getValueAt(row,1)).getValue() );
       
   }
   
   public void notifyChange()
    {
        if (getJReportFrame() != null)
        {
            getJReportFrame().getReport().incrementReportChanges();
        }
    }

   /**
    * Set the property label error
    */
    public void setPropertyLabelError(String propertyName, String error) {
        this.sheetPanel.setPropertyLabelError(propertyName, error);
    }
    
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_DATASET_SPECIFIC_EXPRESSION=1;
    public static final int COMPONENT_METER_INTERVALS=2;
    public static final int COMPONENT_INCREMENT_WHEN_EXPRESSION=70;
    public static final int COMPONENT_DATASETRUN_PARAMETERS=71;
    public static final int COMPONENT_DATASETRUN_MAP_EXPRESSION=72;
    public static final int COMPONENT_DATASETRUN_DS_CONN_EXPRESSION=73;
    
    private Object[] meterIntervalsHilightExpression = null; 
    private Object[] subdatasetParameterHighlightExpression = null; 
    
    /**
     * This method set the focus on a specific component.
     * 
     * expressionInfo[0] can be something like:
     * COMPONENT_DATASET_SPECIFIC_EXPRESSION, ...
     *
     * If it is COMPONENT_DATASET_SPECIFIC_EXPRESSION, other parameters are expected in the array...
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        switch (expID)
        {
            case COMPONENT_INCREMENT_WHEN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                Misc.selectTextAndFocusArea(jRTextExpressionAreaFilterExpression);
                break;
            case COMPONENT_DATASETRUN_PARAMETERS:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel4);
                
                int index = ((Integer)expressionInfo[1]).intValue();
                
                if (index >=0 && jTableDatasetParameters.getRowCount() > index )
                {
                    jTableDatasetParameters.setRowSelectionInterval(index,index);
                    subdatasetParameterHighlightExpression = new Object[expressionInfo.length-2];
                    for (int i=2; i< expressionInfo.length; ++i) subdatasetParameterHighlightExpression[i-2] = expressionInfo[i];
                    break;
                }
                
                break;
            case COMPONENT_DATASETRUN_MAP_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel5);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaMapExpression);
                break;
            case COMPONENT_DATASETRUN_DS_CONN_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataset );
                jTabbedPaneSubDataset.setSelectedComponent(jPanel6);
                Misc.selectTextAndFocusArea(jRTextExpressionAreaTextConnectionExpression);
                break;
            case COMPONENT_DATASET_SPECIFIC_EXPRESSION:
                jTabbedPane1.setSelectedComponent( jPanelData );
                jTabbedPaneData.setSelectedComponent( jPanelDataDefinition );
                if (jPanelDataDefinition.getComponentCount() > 0 )
                {
                    Object newInfo[] = new Object[expressionInfo.length -1 ];
                    for (int i=1; i< expressionInfo.length; ++i) newInfo[i-1] = expressionInfo[i];
                    
                    ((ChartDatasetPanel)jPanelDataDefinition.getComponent(0)).setFocusedExpression( newInfo );
                }
                break;
            case COMPONENT_METER_INTERVALS:
                this.setPropertyLabelError("meterIntervalsMeterPlot",(String)expressionInfo[1]);
                meterIntervalsHilightExpression = new Object[expressionInfo.length-2];
                for (int i=2; i< expressionInfo.length; ++i) meterIntervalsHilightExpression[i-2] = expressionInfo[i];
                break;
        }
    }
}
