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
 * FieldPatternDialog.java
 * 
 * Created on 10 luglio 2004, 0.50
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.util.I18n;

/**
 *
 * @author  Administrator
 */
public class FieldPatternDialog extends javax.swing.JDialog {
    
    private String pattern = "";
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private int selectedCategory = -1;
    private static String[] dateFormats = new String[]{
        "dd/MM/yyyy",
        "MM/dd/yyyy",
        "yyyy/MM/dd",
        "EEEEE dd MMMMM yyyy",   
        "MMMMM dd, yyyy", 
        "dd/MM",
        "dd/MM/yy",
        "dd-MMM",
        "dd-MMM-yy",
        "MMM-yy",
        "MMMMM-yy",
        "dd MMMMM yyyy", 
        "dd/MM/yyyy h.mm a",
        "dd/MM/yyyy HH.mm.ss",
        "MMM",
        "d/M/yyyy",
        "dd-MMM-yyyy",
        "yyyy.MM.dd G 'at' HH:mm:ss z",
        "EEE, MMM d, ''yy",
        "yyyy.MMMMM.dd GGG hh:mm aaa",
        "EEE, d MMM yyyy HH:mm:ss Z",
        "yyMMddHHmmssZ"  
    };
    
    private static String[] timeFormats = new String[]{
        "HH.mm",
        "h.mm a",
        "HH.mm.ss",
        "h.mm.ss a",
        "mm.ss,S",
        "hh 'o''clock' a, zzzz",
        "K:mm a, z",
        "yyyy.MMMMM.dd GGG hh:mm aaa",
        "yyyy.MM.dd G 'at' HH:mm:ss z",
        "EEE, d MMM yyyy HH:mm:ss Z",
        "yyMMddHHmmssZ"  
    };
    
    /** Creates new form FieldPatternDialog */
    public FieldPatternDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    public FieldPatternDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    public void initAll()
    {
        initComponents();
        applyI18n();
        this.setSize(520, 350);
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        javax.swing.DefaultListModel dlm = new javax.swing.DefaultListModel();
        javax.swing.DefaultListModel dlm2 = new javax.swing.DefaultListModel();
        javax.swing.DefaultListModel dlm3 = new javax.swing.DefaultListModel();
        javax.swing.DefaultListModel dlm4 = new javax.swing.DefaultListModel();
        
        jListCategory.setModel( dlm );
        dlm.addElement( "Number" );      // 0
        dlm.addElement( "Date" );        // 1
        dlm.addElement( "Time" );        // 2
        dlm.addElement( "Currency" );    // 3
        dlm.addElement( "Percentage" );  // 4
        dlm.addElement( "Scientific" );  // 5
        //dlm.addElement( "Custom" );      // 7
        
        jListNegatives.setModel( dlm2 );
        jListDateTypes.setModel( dlm3 );
        jListTimeTypes.setModel( dlm4 );

        javax.swing.SpinnerNumberModel sm = new javax.swing.SpinnerNumberModel(2,0,100,1);
        jSpinnerNumberDecimals.setModel( sm );
        jSpinnerNumberDecimals1.setModel( sm );
        jSpinnerNumberDecimals2.setModel( sm );
        jSpinnerNumberDecimals3.setModel( sm );
        
        jPanelSheets.removeAll();
        ((javax.swing.DefaultComboBoxModel)jComboBoxPercentage.getModel()).addElement("%");
        ((javax.swing.DefaultComboBoxModel)jComboBoxPercentage.getModel()).addElement("\u2030");
        
        jPanelSheets.updateUI();
        this.jListCategory.setSelectedIndex(0);
        updateListNegatives();
        updateListDateTypes();
        updateListTimeTypes();
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButton2ActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButton1);
        
    }
    
    public void setOnlyDate(boolean b)
    {
        if (b == true)
        {
            javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jListCategory.getModel();
            dlm.removeAllElements();
            dlm.addElement( "Date" );
            dlm.addElement( "Time" );        // 2
            selectedCategory = -1;
            jListCategory.setSelectedIndex(0);
            //jListCategoryValueChanged(null);
        }
    }
    
    public void setOnlyNumbers(boolean b)
    {
        if (b == true)
        {
            javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jListCategory.getModel();
            dlm.removeAllElements();
            dlm.addElement( "Number" );      // 0
            dlm.addElement( "Scientific" );  // 5
            selectedCategory = -1;
            jListCategory.setSelectedIndex(0);
            //jListCategoryValueChanged(null);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCategory = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jPanelSheets = new javax.swing.JPanel();
        jPanelNumber = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerNumberDecimals = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListNegatives = new javax.swing.JList();
        jPanelDate = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListDateTypes = new javax.swing.JList();
        jPanelTime = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListTimeTypes = new javax.swing.JList();
        jPanelCurrency = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jSpinnerNumberDecimals1 = new javax.swing.JSpinner();
        jPanel9 = new javax.swing.JPanel();
        jPanelPercentage = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerNumberDecimals2 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jComboBoxPercentage = new javax.swing.JComboBox();
        jPanelScientific = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSpinnerNumberDecimals3 = new javax.swing.JSpinner();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabelSample = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabelPattern = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pattern editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(150, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 50));
        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel1.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabel1, gridBagConstraints);

        jListCategory.setFont(new java.awt.Font("SansSerif", 0, 11));
        jListCategory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCategory.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCategoryValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(jListCategory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanelSheets.setLayout(new java.awt.BorderLayout());

        jPanelNumber.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Decimal places:");
        jLabel2.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel2.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel2.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelNumber.add(jLabel2, gridBagConstraints);

        jSpinnerNumberDecimals.setFont(new java.awt.Font("SansSerif", 0, 11));
        jSpinnerNumberDecimals.setMinimumSize(new java.awt.Dimension(27, 22));
        jSpinnerNumberDecimals.setPreferredSize(new java.awt.Dimension(100, 22));
        jSpinnerNumberDecimals.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSpinnerNumberDecimalsPropertyChange(evt);
            }
        });
        jSpinnerNumberDecimals.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNumberDecimalsStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelNumber.add(jSpinnerNumberDecimals, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Use 1000 separator");
        jLabel3.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel3.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel3.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelNumber.add(jLabel3, gridBagConstraints);

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanelNumber.add(jCheckBox1, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Negative numbers:");
        jLabel4.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel4.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel4.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelNumber.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelNumber.add(jPanel7, gridBagConstraints);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 120));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(100, 50));
        jListNegatives.setFont(new java.awt.Font("SansSerif", 0, 11));
        jListNegatives.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListNegatives.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListNegativesValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(jListNegatives);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelNumber.add(jScrollPane2, gridBagConstraints);

        jPanelSheets.add(jPanelNumber, java.awt.BorderLayout.CENTER);

        jPanelDate.setLayout(new java.awt.GridBagLayout());

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Type:");
        jLabel7.setPreferredSize(new java.awt.Dimension(100, 22));
        jLabel7.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel7.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelDate.add(jLabel7, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 120));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 50));
        jListDateTypes.setFont(new java.awt.Font("SansSerif", 0, 11));
        jListDateTypes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListDateTypes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListDateTypesValueChanged(evt);
            }
        });

        jScrollPane3.setViewportView(jListDateTypes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelDate.add(jScrollPane3, gridBagConstraints);

        jPanelSheets.add(jPanelDate, java.awt.BorderLayout.CENTER);

        jPanelTime.setLayout(new java.awt.GridBagLayout());

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Type:");
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 22));
        jLabel8.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel8.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelTime.add(jLabel8, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(300, 120));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(200, 50));
        jListTimeTypes.setFont(new java.awt.Font("SansSerif", 0, 11));
        jListTimeTypes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListTimeTypes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListTimeTypesValueChanged(evt);
            }
        });

        jScrollPane4.setViewportView(jListTimeTypes);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        jPanelTime.add(jScrollPane4, gridBagConstraints);

        jPanelSheets.add(jPanelTime, java.awt.BorderLayout.CENTER);

        jPanelCurrency.setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Decimal places:");
        jLabel5.setPreferredSize(new java.awt.Dimension(130, 22));
        jLabel5.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel5.setMinimumSize(new java.awt.Dimension(75, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelCurrency.add(jLabel5, gridBagConstraints);

        jSpinnerNumberDecimals1.setFont(new java.awt.Font("SansSerif", 0, 11));
        jSpinnerNumberDecimals1.setPreferredSize(new java.awt.Dimension(100, 22));
        jSpinnerNumberDecimals1.setMinimumSize(new java.awt.Dimension(27, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelCurrency.add(jSpinnerNumberDecimals1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelCurrency.add(jPanel9, gridBagConstraints);

        jPanelSheets.add(jPanelCurrency, java.awt.BorderLayout.CENTER);

        jPanelPercentage.setLayout(new java.awt.GridBagLayout());

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Decimal places:");
        jLabel6.setPreferredSize(new java.awt.Dimension(130, 22));
        jLabel6.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel6.setMinimumSize(new java.awt.Dimension(75, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelPercentage.add(jLabel6, gridBagConstraints);

        jSpinnerNumberDecimals2.setFont(new java.awt.Font("SansSerif", 0, 11));
        jSpinnerNumberDecimals2.setPreferredSize(new java.awt.Dimension(100, 22));
        jSpinnerNumberDecimals2.setMinimumSize(new java.awt.Dimension(27, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelPercentage.add(jSpinnerNumberDecimals2, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Type:");
        jLabel9.setPreferredSize(new java.awt.Dimension(130, 22));
        jLabel9.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel9.setMinimumSize(new java.awt.Dimension(75, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelPercentage.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelPercentage.add(jPanel3, gridBagConstraints);

        jComboBoxPercentage.setFont(new java.awt.Font("SansSerif", 0, 12));
        jComboBoxPercentage.setPreferredSize(new java.awt.Dimension(100, 20));
        jComboBoxPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPercentageActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelPercentage.add(jComboBoxPercentage, gridBagConstraints);

        jPanelSheets.add(jPanelPercentage, java.awt.BorderLayout.CENTER);

        jPanelScientific.setLayout(new java.awt.GridBagLayout());

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Decimal places:");
        jLabel10.setPreferredSize(new java.awt.Dimension(130, 22));
        jLabel10.setMaximumSize(new java.awt.Dimension(75, 50));
        jLabel10.setMinimumSize(new java.awt.Dimension(75, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanelScientific.add(jLabel10, gridBagConstraints);

        jSpinnerNumberDecimals3.setFont(new java.awt.Font("SansSerif", 0, 11));
        jSpinnerNumberDecimals3.setPreferredSize(new java.awt.Dimension(100, 22));
        jSpinnerNumberDecimals3.setMinimumSize(new java.awt.Dimension(27, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanelScientific.add(jSpinnerNumberDecimals3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelScientific.add(jPanel10, gridBagConstraints);

        jPanelSheets.add(jPanelScientific, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(jPanelSheets, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Sample"));
        jLabelSample.setFont(new java.awt.Font("SansSerif", 0, 12));
        jLabelSample.setPreferredSize(new java.awt.Dimension(34, 20));
        jLabelSample.setMaximumSize(new java.awt.Dimension(34, 50));
        jLabelSample.setMinimumSize(new java.awt.Dimension(34, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel4.add(jLabelSample, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Pattern"));
        jLabelPattern.setFont(new java.awt.Font("SansSerif", 0, 12));
        jLabelPattern.setPreferredSize(new java.awt.Dimension(34, 20));
        jLabelPattern.setMaximumSize(new java.awt.Dimension(34, 50));
        jLabelPattern.setMinimumSize(new java.awt.Dimension(34, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel8.add(jLabelPattern, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel2.add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel6, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 11));
        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel5.add(jButton1, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("SansSerif", 0, 11));
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel5.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        getContentPane().add(jPanel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setPattern( jLabelPattern.getText() );
        setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBoxPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPercentageActionPerformed
       updateSample();
    }//GEN-LAST:event_jComboBoxPercentageActionPerformed

    private void jListTimeTypesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListTimeTypesValueChanged
        updateSample();
    }//GEN-LAST:event_jListTimeTypesValueChanged

    private void jListDateTypesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListDateTypesValueChanged
        updateSample();
    }//GEN-LAST:event_jListDateTypesValueChanged

    private void jListNegativesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListNegativesValueChanged
        
        updateSample();
        
    }//GEN-LAST:event_jListNegativesValueChanged

    private void jSpinnerNumberDecimalsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerNumberDecimalsStateChanged
        updateSample();
        updateListNegatives();
    }//GEN-LAST:event_jSpinnerNumberDecimalsStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        updateSample();
        updateListNegatives();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jListCategoryValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCategoryValueChanged
        
        int i = getSelectedCategory();
        
        if (i != selectedCategory)
        {
            selectedCategory = i;
            jPanelSheets.removeAll();
            if (i == 0)
            {
                jPanelSheets.add(jPanelNumber);
                updateSample();
            }
            else if (i == 1)
            {
                jPanelSheets.add(jPanelDate);
                updateSample();
            }
            else if (i == 2)
            {
                jPanelSheets.add(jPanelTime);
                updateSample();
            }
            else if (i == 3)
            {
                jPanelSheets.add(jPanelCurrency);
                updateSample();
            }
            else if (i == 4)
            {
                jPanelSheets.add(jPanelPercentage);
                updateSample();
            }
            else if (i == 5)
            {
                jPanelSheets.add(jPanelScientific);
                updateSample();
            }
            else
            {
                updateSample();
            }
            jPanelSheets.updateUI();
        }
        
    }//GEN-LAST:event_jListCategoryValueChanged

    private void jSpinnerNumberDecimalsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSpinnerNumberDecimalsPropertyChange
        
        
        
    }//GEN-LAST:event_jSpinnerNumberDecimalsPropertyChange
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new FieldPatternDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBoxPercentage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPattern;
    private javax.swing.JLabel jLabelSample;
    private javax.swing.JList jListCategory;
    private javax.swing.JList jListDateTypes;
    private javax.swing.JList jListNegatives;
    private javax.swing.JList jListTimeTypes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCurrency;
    private javax.swing.JPanel jPanelDate;
    private javax.swing.JPanel jPanelNumber;
    private javax.swing.JPanel jPanelPercentage;
    private javax.swing.JPanel jPanelScientific;
    private javax.swing.JPanel jPanelSheets;
    private javax.swing.JPanel jPanelTime;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerNumberDecimals;
    private javax.swing.JSpinner jSpinnerNumberDecimals1;
    private javax.swing.JSpinner jSpinnerNumberDecimals2;
    private javax.swing.JSpinner jSpinnerNumberDecimals3;
    // End of variables declaration//GEN-END:variables
    
    
    private void updateSample()
    {
        String format = createPattern();
        
        int i = getSelectedCategory();
        if (i == 0)
        {
            java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
            jLabelSample.setText( nf.format(1234.43210));
        }
        else if (i == 1)
        {
             if (jListDateTypes.getSelectedIndex() >= 0)
             {
                jLabelSample.setText( ""+jListDateTypes.getSelectedValue() );
             }
             else jLabelSample.setText("");
        }
        else if (i == 2)
        {
             if (jListTimeTypes.getSelectedIndex() >= 0)
             {
                jLabelSample.setText( ""+jListTimeTypes.getSelectedValue() );
             }
             else jLabelSample.setText("");
        }
        else if (i == 3)
        {
            java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
            jLabelSample.setText( nf.format(1234.43210));
            
        }
        else if (i == 4)
        {
            java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
            jLabelSample.setText( nf.format(1234.43210));
            
        }
        else if (i == 5)
        {
            java.text.DecimalFormat nf = new java.text.DecimalFormat(format);
            jLabelSample.setText( nf.format(1234.43210));
            
        }
        else
        {
            jLabelSample.setText("");
        }
    }
    
    private String createPattern()
    {
        String format = "";
        int cat = getSelectedCategory();
        // NUMBER FORMATS
        if (cat == 0)
        {
            //format = "###0";
            if (jCheckBox1.isSelected())
            {
                format = "#,##0";
            }
            else
            {
                format = "###0";
            }
            int decimals = ((javax.swing.SpinnerNumberModel)jSpinnerNumberDecimals.getModel()).getNumber().intValue();
            if (decimals > 0)
            {
                format += ".";
                for (int i=0; i<decimals; ++i)
                {
                    format += "0";
                }                
            }
            if (jListNegatives.getSelectedIndex() >= 0)
            {
                int selectedIndex = jListNegatives.getSelectedIndex();
                if (selectedIndex == 0) format += ";-"+ format + "";
                if (selectedIndex == 1) format += ";" + format + "-";
                if (selectedIndex == 2) format += ";(" + format + ")";
                if (selectedIndex == 3) format += ";(-" + format + ")";
                if (selectedIndex == 4) format += ";(" + format + "-)";
            }
        }
        else if (cat == 1)
        {
            if (jListDateTypes.getSelectedIndex() >= 0)
            {
                format = dateFormats[jListDateTypes.getSelectedIndex()];
            }
        }
        else if (cat == 2)
        {
            if (jListTimeTypes.getSelectedIndex() >= 0)
            {
                format = timeFormats[jListTimeTypes.getSelectedIndex()];
            }
        }
        else if (cat == 3)
        {
            format = "\u00A4 #,##0";
            int decimals = ((javax.swing.SpinnerNumberModel)jSpinnerNumberDecimals1.getModel()).getNumber().intValue();
            if (decimals > 0)
            {
                format += ".";
                for (int i=0; i<decimals; ++i)
                {
                    format += "0";
                }                
            }
        }
        else if (cat == 4)
        {
            format = "#,##0";
            int decimals = ((javax.swing.SpinnerNumberModel)jSpinnerNumberDecimals2.getModel()).getNumber().intValue();
            if (decimals > 0)
            {
                format += ".";
                for (int i=0; i<decimals; ++i)
                {
                    format += "0";
                }                
            }
            format += " " + jComboBoxPercentage.getSelectedItem();
        }
        else if (cat == 5)
        {
            format = "0";
            int decimals = ((javax.swing.SpinnerNumberModel)jSpinnerNumberDecimals3.getModel()).getNumber().intValue();
            if (decimals > 0)
            {
                format += ".0";
                for (int i=1; i<decimals; ++i)
                {
                    format += "#";
                }                
            }
            format += "E0";
        }
        
        jLabelPattern.setText( format );
        return format;
    }
    
    private void updateListNegatives()
    {
        String format = createPattern();
        javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jListNegatives.getModel();
        int selected = jListNegatives.getSelectedIndex();
        dlm.removeAllElements();
        if (format.indexOf(";") >= 0)
        {
            format = format.substring(0,format.indexOf(";"));
        }
        String[] formats = new String[5];
        
        formats[0] = format + ";-"+ format + "";
        formats[1] = format + ";" + format + "-";
        formats[2] = format + ";(" + format + ")";
        formats[3] = format + ";(-" + format + ")";
        formats[4] = format + ";(" + format + "-)";
    
        for (int i=0; i<formats.length; ++i)
        {
            java.text.DecimalFormat nf = new java.text.DecimalFormat(formats[i]);
            dlm.addElement( nf.format(-1234.43210) );
        }
        if (selected>=0)
        {
            jListNegatives.setSelectedIndex(selected);
        }
        
    }
    
    private void updateListDateTypes()
    {
        javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jListDateTypes.getModel();
        
        for (int i=0; i<dateFormats.length; ++i)
        {
            java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat(dateFormats[i]);
            dlm.addElement( nf.format(new java.util.Date()) );
        }
        jListDateTypes.setSelectedIndex(0);
        
    }
    
     private void updateListTimeTypes()
    {
        javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)jListTimeTypes.getModel();
        
        for (int i=0; i<timeFormats.length; ++i)
        {
            java.text.SimpleDateFormat nf = new java.text.SimpleDateFormat(timeFormats[i]);
            dlm.addElement( nf.format(new java.util.Date()) );
        }
        jListTimeTypes.setSelectedIndex(0);
        
    }
     
     /**
      * Getter for property dialogResult.
      * @return Value of property dialogResult.
      */
     public int getDialogResult() {
         return dialogResult;
     }
     
     /**
      * Setter for property dialogResult.
      * @param dialogResult New value of property dialogResult.
      */
     public void setDialogResult(int dialogResult) {
         this.dialogResult = dialogResult;
     }

     
     /**
      * Getter for property pattern.
      * @return Value of property pattern.
      */
     public java.lang.String getPattern() {
         return pattern;
     }
     
     /**
      * Setter for property pattern.
      * @param pattern New value of property pattern.
      */
     public void setPattern(java.lang.String pattern) {
         this.pattern = pattern;
     }
     
     public int getSelectedCategory()
     {
         int i =0;
        String category = ""+jListCategory.getSelectedValue();
        
        if (category.equals("Number" )) i = 0;  // 0
        if (category.equals("Date" )) i = 1;  // 0
        if (category.equals("Time" )) i = 2;  // 0
        if (category.equals("Currency" )) i = 3;  // 0
        if (category.equals("Percentage" )) i = 4;  // 0
        if (category.equals("Scientific" )) i = 5;  // 0
        
        return i;
     }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButton1.setText(I18n.getString("fieldPatternDialog.button1","Apply"));
                jButton2.setText(I18n.getString("fieldPatternDialog.button2","Cancel"));
                jLabel1.setText(I18n.getString("fieldPatternDialog.label1","Category"));
                jLabel10.setText(I18n.getString("fieldPatternDialog.label10","Decimal places:"));
                jLabel2.setText(I18n.getString("fieldPatternDialog.label2","Decimal places:"));
                jLabel3.setText(I18n.getString("fieldPatternDialog.label3","Use 1000 separator"));
                jLabel4.setText(I18n.getString("fieldPatternDialog.label4","Negative numbers:"));
                jLabel5.setText(I18n.getString("fieldPatternDialog.label5","Decimal places:"));
                jLabel6.setText(I18n.getString("fieldPatternDialog.label6","Decimal places:"));
                jLabel7.setText(I18n.getString("fieldPatternDialog.label7","Type:"));
                jLabel8.setText(I18n.getString("fieldPatternDialog.label8","Type:"));
                jLabel9.setText(I18n.getString("fieldPatternDialog.label9","Type:"));
                // End autogenerated code ----------------------
                ((javax.swing.border.TitledBorder)jPanel4.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("fieldPatternDialog.panelBorder.Sample","Sample") );
                ((javax.swing.border.TitledBorder)jPanel8.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("fieldPatternDialog.panelBorder.Pattern","Pattern") );
                setTitle(I18n.getString("fieldPatternDialog.title","Pattern editor"));            
    }
}
