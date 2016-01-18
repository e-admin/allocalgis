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
 * ElementPropertiesDialog.java
 * 
 * Created on 28 aprile 2003, 23.53
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.HyperLinkableReportElement;
import it.businesslogic.ireport.IReportFont;
import it.businesslogic.ireport.JRSubreportParameter;
import it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.gui.event.*;
import java.util.*;
import java.awt.*;

import com.geopista.reports.gui.MapImageReportElement;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.gui.box.*;
import it.businesslogic.ireport.gui.sheet.*;

/**
 *
 * @author <a href="mailto:gt78@users.sourceforge.net">Giulio Toffoli</a>
 * @author <a href="mailto:phenderson@users.sourceforge.net">Peter Henderson</a>
 */
public class ElementPropertiesDialog extends javax.swing.JDialog {
    
    public static final int GRAPHICS_TAB = 1;
    public static final int RECTANGLE_TAB = 2;
    public static final int LINE_TAB = 3;
    public static final int IMAGE_TAB = 4;
    public static final int FONT_TAB = 5;
    public static final int STATITEXT_TAB = 6;
    public static final int TEXTFIELD_TAB = 7;
    public static final int HYPERLINK_TAB = 8;
    public static final int SUBREPORT1_TAB = 9;
    public static final int SUBREPORT2_TAB = 10;
    public static final int CHART_TAB = 11;
    public static final int COMMON_TAB = 12;
    public static final int BARCODE_TAB = 13;
    
    
    private JReportFrame jrf = null;
    //private String lastSelectedPaneName = "Common";
    private javax.swing.JPanel lastSelectedPanel = null;
    private BoxPanel boxPanel = null;
    private ReportElementSheetPanel sheetPanel = null;
    
    
    /** Creates new form ElementPropertiesDialog */
    public ElementPropertiesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        //System.out.println(">>> 1");
        
        jTabbedPane.removeAll();
        
        
        this.setSize(370, 450);
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
        
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);
        
        // Image Expression Classes...
        jComboBoxImageExpressionClass.addItem("java.lang.String");
        jComboBoxImageExpressionClass.addItem("java.io.File");
        jComboBoxImageExpressionClass.addItem("java.net.URL");
        jComboBoxImageExpressionClass.addItem("java.io.InputStream");
        jComboBoxImageExpressionClass.addItem("java.awt.Image");
        jComboBoxImageExpressionClass.addItem("net.sf.jasperreports.engine.JRRenderable");
        
        jNumberComboBoxSize.addEntry("3",3);
        jNumberComboBoxSize.addEntry("5",5);
        jNumberComboBoxSize.addEntry("8",8);
        jNumberComboBoxSize.addEntry("10",10);
        jNumberComboBoxSize.addEntry("12",12);
        jNumberComboBoxSize.addEntry("14",14);
        jNumberComboBoxSize.addEntry("18",18);
        jNumberComboBoxSize.addEntry("24",24);
        jNumberComboBoxSize.addEntry("36",36);
        jNumberComboBoxSize.addEntry("48",48);
        
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1250","CP1250 (Central European)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1251","CP1251 (Cyrillic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1252","CP1252 (Western European ANSI aka WinAnsi)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1253","CP1253 (Greek)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1254","CP1254 (Turkish)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1255","CP1255 (Hebrew)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1256","CP1256 (Arabic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1257","CP1257 (Baltic)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Cp1258","CP1258 (Vietnamese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniGB-UCS2-H","UniGB-UCS2-H (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniGB-UCS2-V","UniGB-UCS2-V (Chinese Simplified)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniCNS-UCS2-H","UniCNS-UCS2-H (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniCNS-UCS2-V","UniCNS-UCS2-V (Chinese traditional)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-H","UniJIS-UCS2-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-V","UniJIS-UCS2-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-H (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniJIS-UCS2-HW-V","UniJIS-UCS2-HW-V (Japanese)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniKS-UCS2-H","UniKS-UCS2-H (Korean)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("UniKS-UCS2-V","UniKS-UCS2-V (Korean)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Identity-H","Identity-H (Unicode with horizontal writing)"));
        jComboBoxPdfEncoding.addItem(new PdfEncoding("Identity-V","Identity-V (Unicode with vertical writing)"));
        
        jComboBoxPattern.addItem(new PdfEncoding("#,##0.00"," #,##0.00 (example 1,234.56)"));
        
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Boolean");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Byte");
        jComboBoxTextFieldExpressionClass.addItem("java.util.Date");
        jComboBoxTextFieldExpressionClass.addItem("java.sql.Timestamp");
        jComboBoxTextFieldExpressionClass.addItem("java.sql.Time");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Double");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Float");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Integer");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Long");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Short");
        jComboBoxTextFieldExpressionClass.addItem("java.math.BigDecimal");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.Number");
        jComboBoxTextFieldExpressionClass.addItem("java.lang.String");
        
        jComboBoxSubreportExpressionClass.addItem("java.lang.String");
        jComboBoxSubreportExpressionClass.addItem("java.io.File");
        jComboBoxSubreportExpressionClass.addItem("java.net.URL");
        jComboBoxSubreportExpressionClass.addItem("java.io.InputStream");
        jComboBoxSubreportExpressionClass.addItem("net.sf.jasperreports.engine.JasperReport");
        
        jComboBoxLinkType.addItem("None");
        jComboBoxLinkType.addItem("Reference");
        jComboBoxLinkType.addItem("LocalAnchor");
        jComboBoxLinkType.addItem("LocalPage");
        jComboBoxLinkType.addItem("RemoteAnchor");
        jComboBoxLinkType.addItem("RemotePage");
        
        // Barcode Evaluation Time...
        jComboBoxLinkTarget.addItem("Self");
        jComboBoxLinkTarget.addItem("Blank");
        
        jComboBoxBarcodeType.addItem(new Tag("1","2of7"));
        jComboBoxBarcodeType.addItem(new Tag("2","3of9"));
        jComboBoxBarcodeType.addItem(new Tag("3","Bookland"));
        jComboBoxBarcodeType.addItem(new Tag("4","Codabar"));
        jComboBoxBarcodeType.addItem(new Tag("5","Code128"));
        jComboBoxBarcodeType.addItem(new Tag("6","Code128A"));
        jComboBoxBarcodeType.addItem(new Tag("7","Code128B"));
        jComboBoxBarcodeType.addItem(new Tag("8","Code128C"));
        jComboBoxBarcodeType.addItem(new Tag("9","Code39"));
        jComboBoxBarcodeType.addItem(new Tag("26","Code39 (Extended)"));
        jComboBoxBarcodeType.addItem(new Tag("10","EAN128"));
        jComboBoxBarcodeType.addItem(new Tag("11","EAN13"));
        jComboBoxBarcodeType.addItem(new Tag("12","GlobalTradeItemNumber"));
        jComboBoxBarcodeType.addItem(new Tag("13","Int2of5"));
        jComboBoxBarcodeType.addItem(new Tag("14","Monarch"));
        jComboBoxBarcodeType.addItem(new Tag("15","NW7"));
        jComboBoxBarcodeType.addItem(new Tag("16","PDF417"));
        jComboBoxBarcodeType.addItem(new Tag("17","SCC14ShippingCode"));
        jComboBoxBarcodeType.addItem(new Tag("18","ShipmentIdentificationNumber"));
        jComboBoxBarcodeType.addItem(new Tag("19","SSCC18"));
        jComboBoxBarcodeType.addItem(new Tag("20","Std2of5"));
        jComboBoxBarcodeType.addItem(new Tag("21","UCC128"));
        jComboBoxBarcodeType.addItem(new Tag("22","UPCA"));
        jComboBoxBarcodeType.addItem(new Tag("23","USD3"));
        jComboBoxBarcodeType.addItem(new Tag("24","USD4"));
        jComboBoxBarcodeType.addItem(new Tag("25","USPS"));
                
        jSpinnerBookmarkLevel.setModel( new javax.swing.SpinnerNumberModel(0,0,10000,1));
        
        // Load Fonts...
        String[] fontFamilies = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i=0; i<fontFamilies.length; ++i) {
            jComboBoxFontName.addItem(fontFamilies[i]);
        }
        
        MainFrame.getMainInstance().addFontsListChangedListener( new FontsListChangedListener() {
            public void fontsListChanged(FontsListChangedEvent evt) {
                updateFonts();
            }
        } );
        
        this.jRTextExpressionAreaPrintWhenExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPrintWhenExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPrintWhenExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPrintWhenExpressionTextChanged();
            }
        });
        
        
        
        this.jTextAreaText.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jTextAreaTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jTextAreaTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jTextAreaTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTextFieldExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextFieldExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextFieldExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextFieldExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaImageExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaImageExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaImageExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaImageExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaSubreportExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportExpressionTextChanged();
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
        
        this.jRTextExpressionAreaCrosstabParametersMapExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaCrosstabParametersMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaCrosstabParametersMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaCrosstabParametersMapExpressionTextChanged();
            }
        });
        
        this.jRTextExpressionAreaSubreportMapExpression.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportMapExpressionTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportMapExpressionTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaSubreportMapExpressionTextChanged();
            }
        });
        
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableSubreportParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableSubreportParametersListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableSubreportReturnValues.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableSubreportReturnValuesListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableCrosstabParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableCrosstabParametersListSelectionValueChanged(e);
            }
        });
        
        dlsm =  (javax.swing.DefaultListSelectionModel)this.jTableLinkParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableLinkParametersListSelectionValueChanged(e);
            }
        });
        
        
        this.jRTextExpressionAreaAnchorName.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorNameTextChanged();
            }
        });
        
        this.jRTextExpressionAreaAnchor.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaAnchorTextChanged();
            }
        });
        
        this.jRTextExpressionAreaPage.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaPageTextChanged();
            }
        });
        
        this.jRTextExpressionAreaReference.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaReferenceTextChanged();
            }
        });
        
        this.jRTextExpressionAreaTooltip.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTooltipTextChanged();
            }
        });
        
        
                /*this.jComboBoxChartFactory.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
                        public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                                chartFactoryChanged();
                        }
                        public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                                chartFactoryChanged();
                        }
                        public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                                chartFactoryChanged();
                        }
                });*/
        
        this.jTextFieldName.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldNameChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldNameChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldNameChanged();
            }
        });
        
        this.jBarcodeExpressionAreaAppIdentifier.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldAppIdentifierChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldAppIdentifierChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jTextFieldAppIdentifierChanged();
            }
        });
        
        this.jBarcodeExpressionArea.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                BarcodeActionPerformed(null);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                BarcodeActionPerformed(null);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                BarcodeActionPerformed(null);
            }
        });
        
        boxPanel = new BoxPanel();
        boxPanel.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxActionPerformed(evt);
            }
        });
        
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = gridBagConstraints.BOTH;
        jPanelBorder.add(boxPanel, gridBagConstraints);
        
        gridBagConstraints.insets = new java.awt.Insets(0, -1, -1, -1);
        sheetPanel = new ReportElementSheetPanel();
        MainFrame.getMainInstance().removeReportListener(sheetPanel);
        MainFrame.getMainInstance().removeReportFrameActivatedListener(sheetPanel);
        jPanelSheet.add(sheetPanel, gridBagConstraints);
        
        javax.swing.JTextField editor = (javax.swing.JTextField) jComboBoxPattern.getEditor().getEditorComponent();
        editor.addFocusListener(new java.awt.event.FocusAdapter(){
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxPattern.setSelectedItem(
                        ((javax.swing.JTextField) jComboBoxPattern.getEditor().getEditorComponent()).getText());
            }
        });
        
        I18n.addOnLanguageChangedListener( new LanguageChangedListener() {
            public void languageChanged(LanguageChangedEvent evt) {
                applyI18n();
            }
        } );
        
        applyI18n();
    }
    
    
    public void  boxActionPerformed(java.awt.event.ActionEvent evt) {
        if (jrf == null || getElementSelection().size()==0) return;
        
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            Box box = null;
            
            box = ((BoxElement)element).getBox();
            
            if (box != null && evt.getActionCommand().equals("LeftPadding")) {
                box.setLeftPadding( boxPanel.getBox().getLeftPadding() );
            } else if (box != null && evt.getActionCommand().equals("RightPadding")) {
                box.setRightPadding( boxPanel.getBox().getRightPadding() );
            } else if (box != null && evt.getActionCommand().equals("TopPadding")) {
                box.setTopPadding( boxPanel.getBox().getTopPadding() );
            } else if (box != null && evt.getActionCommand().equals("BottomPadding")) {
                box.setBottomPadding( boxPanel.getBox().getBottomPadding() );
            } else if (box != null && evt.getActionCommand().equals("LeftBorder")) {
                box.setLeftBorder( boxPanel.getBox().getLeftBorder() );
            } else if (box != null && evt.getActionCommand().equals("RightBorder")) {
                box.setRightBorder( boxPanel.getBox().getRightBorder() );
            } else if (box != null && evt.getActionCommand().equals("TopBorder")) {
                box.setTopBorder( boxPanel.getBox().getTopBorder() );
            } else if (box != null && evt.getActionCommand().equals("BottomBorder")) {
                box.setBottomBorder( boxPanel.getBox().getBottomBorder() );
            } else if (box != null && evt.getActionCommand().equals("LeftBorderColor")) {
                box.setLeftBorderColor( boxPanel.getBox().getLeftBorderColor() );
            } else if (box != null && evt.getActionCommand().equals("RightBorderColor")) {
                box.setRightBorderColor( boxPanel.getBox().getRightBorderColor() );
            } else if (box != null && evt.getActionCommand().equals("TopBorderColor")) {
                box.setTopBorderColor( boxPanel.getBox().getTopBorderColor() );
            } else if (box != null && evt.getActionCommand().equals("BottomBorderColor")) {
                box.setBottomBorderColor( boxPanel.getBox().getBottomBorderColor() );
            }
        }
        repaintEditor();;
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jTableSubreportParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableSubreportParameters.getSelectedRowCount() > 0) {
            this.jButtonModParameter.setEnabled(true);
            this.jButtonRemParameter.setEnabled(true);
        } else {
            this.jButtonModParameter.setEnabled(false);
            this.jButtonRemParameter.setEnabled(false);
        }
    }
    
    public void jTableSubreportReturnValuesListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableSubreportReturnValues.getSelectedRowCount() > 0) {
            this.jButtonModReturnValue.setEnabled(true);
            this.jButtonRemReturnValue.setEnabled(true);
        } else {
            this.jButtonModReturnValue.setEnabled(false);
            this.jButtonRemReturnValue.setEnabled(false);
        }
    }
    
    public void jTableCrosstabParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableCrosstabParameters.getSelectedRowCount() > 0) {
            this.jButtonModCrosstabParameter.setEnabled(true);
            this.jButtonRemCrosstabParameter.setEnabled(true);
        } else {
            this.jButtonModCrosstabParameter.setEnabled(false);
            this.jButtonRemCrosstabParameter.setEnabled(false);
        }
    }
    
    public void jTableLinkParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTableLinkParameters.getSelectedRowCount() > 0) {
            this.jButtonModLinkParameter.setEnabled(true);
            this.jButtonRemLinkParameter.setEnabled(true);
        } else {
            this.jButtonModLinkParameter.setEnabled(false);
            this.jButtonRemLinkParameter.setEnabled(false);
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroupCharts = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanelCommon = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jNumberFieldLeft = new it.businesslogic.ireport.gui.JNumberField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxBand = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jNumberFieldWidth = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldTop = new it.businesslogic.ireport.gui.JNumberField();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jNumberFieldHeight = new it.businesslogic.ireport.gui.JNumberField();
        jButtonBackground = new javax.swing.JButton();
        jButtonForeground = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxTransparent = new javax.swing.JCheckBox();
        jCheckBoxRemoveLineWhenBlank = new javax.swing.JCheckBox();
        jCheckBoxPrintInFirstWholeBand = new javax.swing.JCheckBox();
        jCheckBoxPrintWhenDetailOverflows = new javax.swing.JCheckBox();
        jCheckBoxPrintRepeatedValues = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBoxPositionType = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxGroups = new javax.swing.JComboBox();
        jLabel46 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jRTextExpressionAreaPrintWhenExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxStretchType = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        jLabelStyle = new javax.swing.JLabel();
        jComboBoxStyle = new javax.swing.JComboBox();
        jPanelGraphicselement = new javax.swing.JPanel();
        jComboBoxFill = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jComboBoxPen = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanelRectangle = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jNumberFieldRadius = new it.businesslogic.ireport.gui.JNumberField();
        jPanelLine = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxLineDirection = new javax.swing.JComboBox();
        jPanelMap = new javax.swing.JPanel();
        mapImagePropertiesPanel1 = new com.geopista.reports.gui.MapImagePropertiesPanel();
        jPanelImage = new javax.swing.JPanel();
        jRTextExpressionAreaImageExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel16 = new javax.swing.JLabel();
        jButtonFindImage = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jComboBoxImageExpressionClass = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jComboBoxScale = new javax.swing.JComboBox();
        jCheckBoxImageCache = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        jComboBoxVerticalAlignment = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jComboBoxHorizontalAlignment = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jComboBoxEvaluationTime = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jComboBoxImageGroup = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jCheckBoxImageIsLazy = new javax.swing.JCheckBox();
        jLabel34 = new javax.swing.JLabel();
        jComboBoxImageOnError = new javax.swing.JComboBox();
        jPanelText = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaText = new javax.swing.JTextArea();
        jPanelFont = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jComboBoxReportFont = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jComboBoxFontName = new javax.swing.JComboBox();
        jNumberComboBoxSize = new it.businesslogic.ireport.gui.JNumberComboBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBoxPDFFontName = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jCheckBoxBold = new javax.swing.JCheckBox();
        jCheckBoxItalic = new javax.swing.JCheckBox();
        jCheckBoxUnderline = new javax.swing.JCheckBox();
        jCheckBoxStrokeTrough = new javax.swing.JCheckBox();
        jCheckBoxStyledText = new javax.swing.JCheckBox();
        jCheckBoxPDFEmbedded = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel32 = new javax.swing.JLabel();
        jComboBoxPdfEncoding = new javax.swing.JComboBox();
        jComboBoxHAlign = new javax.swing.JComboBox();
        jComboBoxVAlign = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jComboBoxLineSpacing = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jComboBoxRotation = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanelTextField = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jComboBoxTextFieldExpressionClass = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jComboBoxTextFieldEvaluationTime = new javax.swing.JComboBox();
        jLabel38 = new javax.swing.JLabel();
        jComboBoxTextFieldGroup = new javax.swing.JComboBox();
        jCheckBoxStretchWithOverflow = new javax.swing.JCheckBox();
        jCheckBoxBlankWhenNull = new javax.swing.JCheckBox();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jRTextExpressionAreaTextFieldExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel2 = new javax.swing.JPanel();
        jComboBoxPattern = new javax.swing.JComboBox();
        jButtonCreatePattern = new javax.swing.JButton();
        jPanelSubreport1 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jRTextExpressionAreaSubreportMapExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jComboBoxSubreportConnectionType = new javax.swing.JComboBox();
        jRTextExpressionAreaTextConnectionExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jCheckBoxSubreportCache = new javax.swing.JCheckBox();
        jPanelSubreport2 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jRTextExpressionAreaSubreportExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel33 = new javax.swing.JLabel();
        jComboBoxSubreportExpressionClass = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSubreportParameters = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jButtonAddParameter = new javax.swing.JButton();
        jButtonModParameter = new javax.swing.JButton();
        jButtonRemParameter = new javax.swing.JButton();
        jButtonCopyParamsFromMaster = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanelSubreportReturnValues = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableSubreportReturnValues = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jButtonAddReturnValue = new javax.swing.JButton();
        jButtonModReturnValue = new javax.swing.JButton();
        jButtonRemReturnValue = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanelHyperLink = new javax.swing.JPanel();
        jRTextExpressionAreaAnchorName = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel35 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jComboBoxLinkType = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jLabelTarget = new javax.swing.JLabel();
        jComboBoxLinkTarget = new javax.swing.JComboBox();
        jLabelTarget1 = new javax.swing.JLabel();
        jSpinnerBookmarkLevel = new javax.swing.JSpinner();
        jPanel24 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanelReference = new javax.swing.JPanel();
        jRTextExpressionAreaReference = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabelReference = new javax.swing.JLabel();
        jPanelAnchor = new javax.swing.JPanel();
        jRTextExpressionAreaAnchor = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabelAnchor = new javax.swing.JLabel();
        jPanelPage = new javax.swing.JPanel();
        jLabelPage = new javax.swing.JLabel();
        jRTextExpressionAreaPage = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelLinkParams = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableLinkParameters = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jButtonAddLinkParameter = new javax.swing.JButton();
        jButtonModLinkParameter = new javax.swing.JButton();
        jButtonRemLinkParameter = new javax.swing.JButton();
        jPanelTooltip = new javax.swing.JPanel();
        jLabelTooltip = new javax.swing.JLabel();
        jRTextExpressionAreaTooltip = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanelBarcode = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jComboBoxBarcodeType = new javax.swing.JComboBox();
        jBarcodeExpressionArea = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel48 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jComboBoxBarcodeGroup = new javax.swing.JComboBox();
        jLabel43 = new javax.swing.JLabel();
        jComboBoxEvaluationTimeBarcode = new javax.swing.JComboBox();
        jLabel50 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jComboBoxImageOnError1 = new javax.swing.JComboBox();
        jComboBoxScale1 = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jComboBoxVerticalAlignment1 = new javax.swing.JComboBox();
        jComboBoxHorizontalAlignment1 = new javax.swing.JComboBox();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel22 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jNumberFieldBCWidth = new it.businesslogic.ireport.gui.JNumberField();
        jNumberFieldBCHeight = new it.businesslogic.ireport.gui.JNumberField();
        jBarcodeExpressionAreaAppIdentifier = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jLabel59 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jCheckBoxBarcodeCheckSum = new javax.swing.JCheckBox();
        jCheckBoxBarcodeShowText = new javax.swing.JCheckBox();
        jPanelChart = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel44 = new javax.swing.JLabel();
        jComboBoxEvaluationTime1 = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        jComboBoxImageGroup1 = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanelBorder = new javax.swing.JPanel();
        jPanelSheet = new javax.swing.JPanel();
        jPanelCrosstab = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jRTextExpressionAreaCrosstabParametersMapExpression = new it.businesslogic.ireport.gui.JRTextExpressionArea();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableCrosstabParameters = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jButtonAddCrosstabParameter = new javax.swing.JButton();
        jButtonModCrosstabParameter = new javax.swing.JButton();
        jButtonRemCrosstabParameter = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jCheckBoxRepeatColumnHeaders = new javax.swing.JCheckBox();
        jCheckBoxRepeatRowHeaders = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jNumberFieldColumnBreakOffset = new it.businesslogic.ireport.gui.JNumberField();
        jLabel53 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jTabbedPane.setOpaque(true);
        jTabbedPane.setPreferredSize(new java.awt.Dimension(312, 48));
        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });

        jPanelCommon.setLayout(new java.awt.GridBagLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(300, 74));
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 74));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Height ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel3.add(jLabel5, gridBagConstraints);

        jNumberFieldLeft.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldLeft.setText("0");
        try {
            jNumberFieldLeft.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldLeft.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldLeft.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldLeft.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldLeftActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 2, 0);
        jPanel3.add(jNumberFieldLeft, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Width");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel3.add(jLabel4, gridBagConstraints);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Background ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel3.add(jLabel7, gridBagConstraints);

        jComboBoxBand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBandActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 6);
        jPanel3.add(jComboBoxBand, gridBagConstraints);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Band ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel3.add(jLabel1, gridBagConstraints);

        jNumberFieldWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldWidth.setText("0");
        try {
            jNumberFieldWidth.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldWidth.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldWidth.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldWidth.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldWidthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel3.add(jNumberFieldWidth, gridBagConstraints);

        jNumberFieldTop.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldTop.setText("0");
        try {
            jNumberFieldTop.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldTop.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldTop.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldTop.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldTopActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        jPanel3.add(jNumberFieldTop, gridBagConstraints);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Top ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel3.add(jLabel2, gridBagConstraints);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Foreground ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel3.add(jLabel6, gridBagConstraints);

        jNumberFieldHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldHeight.setText("0");
        try {
            jNumberFieldHeight.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldHeight.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldHeight.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldHeight.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldHeightActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jNumberFieldHeight, gridBagConstraints);

        jButtonBackground.setBackground(new java.awt.Color(255, 255, 255));
        jButtonBackground.setMaximumSize(new java.awt.Dimension(45, 20));
        jButtonBackground.setMinimumSize(new java.awt.Dimension(45, 20));
        jButtonBackground.setPreferredSize(new java.awt.Dimension(45, 20));
        jButtonBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackgroundActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jButtonBackground, gridBagConstraints);

        jButtonForeground.setBackground(new java.awt.Color(0, 0, 0));
        jButtonForeground.setMaximumSize(new java.awt.Dimension(45, 20));
        jButtonForeground.setMinimumSize(new java.awt.Dimension(45, 20));
        jButtonForeground.setPreferredSize(new java.awt.Dimension(45, 20));
        jButtonForeground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonForegroundActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jButtonForeground, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Left ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel3.add(jLabel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 4, 6);
        jPanelCommon.add(jPanel3, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(170, 120));
        jPanel4.setPreferredSize(new java.awt.Dimension(190, 120));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jCheckBoxTransparent.setText("Transparent");
        jCheckBoxTransparent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTransparentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jCheckBoxTransparent, gridBagConstraints);

        jCheckBoxRemoveLineWhenBlank.setText("Remove line when blank");
        jCheckBoxRemoveLineWhenBlank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveLineWhenBlankActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jCheckBoxRemoveLineWhenBlank, gridBagConstraints);

        jCheckBoxPrintInFirstWholeBand.setText("Print in first whole band");
        jCheckBoxPrintInFirstWholeBand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPrintInFirstWholeBandActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jCheckBoxPrintInFirstWholeBand, gridBagConstraints);

        jCheckBoxPrintWhenDetailOverflows.setText("Print when detail overflows");
        jCheckBoxPrintWhenDetailOverflows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPrintWhenDetailOverflowsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jCheckBoxPrintWhenDetailOverflows, gridBagConstraints);

        jCheckBoxPrintRepeatedValues.setText("Print repeated values");
        jCheckBoxPrintRepeatedValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPrintRepeatedValuesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jCheckBoxPrintRepeatedValues, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 0, 0);
        jPanelCommon.add(jPanel4, gridBagConstraints);

        jPanel5.setMinimumSize(new java.awt.Dimension(150, 120));
        jPanel5.setPreferredSize(new java.awt.Dimension(150, 120));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Position type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jLabel8, gridBagConstraints);

        jComboBoxPositionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPositionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jComboBoxPositionType, gridBagConstraints);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Print when group changes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jLabel9, gridBagConstraints);

        jComboBoxGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroupsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jComboBoxGroups, gridBagConstraints);

        jLabel46.setText("Key");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jLabel46, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jTextFieldName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 4);
        jPanelCommon.add(jPanel5, gridBagConstraints);

        jPanel6.setMinimumSize(new java.awt.Dimension(300, 40));
        jPanel6.setPreferredSize(new java.awt.Dimension(300, 40));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Print when expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel6.add(jLabel10, gridBagConstraints);

        jRTextExpressionAreaPrintWhenExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaPrintWhenExpression.setElectricScroll(0);
        jRTextExpressionAreaPrintWhenExpression.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionAreaPrintWhenExpression.setPreferredSize(new java.awt.Dimension(10, 10));
        jRTextExpressionAreaPrintWhenExpression.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jRTextExpressionAreaPrintWhenExpressionInputMethodTextChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jRTextExpressionAreaPrintWhenExpression, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 6, 6);
        jPanelCommon.add(jPanel6, gridBagConstraints);

        jPanel11.setMinimumSize(new java.awt.Dimension(170, 46));
        jPanel11.setPreferredSize(new java.awt.Dimension(170, 46));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Stretch Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel11.add(jLabel12, gridBagConstraints);

        jComboBoxStretchType.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxStretchType.setPreferredSize(new java.awt.Dimension(120, 20));
        jComboBoxStretchType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStretchTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        jPanel11.add(jComboBoxStretchType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanelCommon.add(jPanel11, gridBagConstraints);

        jPanel17.setMinimumSize(new java.awt.Dimension(150, 46));
        jPanel17.setPreferredSize(new java.awt.Dimension(150, 46));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabelStyle.setText("Style");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel17.add(jLabelStyle, gridBagConstraints);

        jComboBoxStyle.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxStyle.setPreferredSize(new java.awt.Dimension(120, 20));
        jComboBoxStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStyleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel17.add(jComboBoxStyle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanelCommon.add(jPanel17, gridBagConstraints);

        jTabbedPane.addTab("Common", jPanelCommon);

        jPanelGraphicselement.setLayout(new java.awt.GridBagLayout());

        jComboBoxFill.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxFill.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFillActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 170, 6);
        jPanelGraphicselement.add(jComboBoxFill, gridBagConstraints);

        jLabel11.setText("Pen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 8, 0, 0);
        jPanelGraphicselement.add(jLabel11, gridBagConstraints);

        jComboBoxPen.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxPen.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxPen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelGraphicselement.add(jComboBoxPen, gridBagConstraints);

        jLabel13.setText("Fill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        jPanelGraphicselement.add(jLabel13, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 83;
        gridBagConstraints.gridy = 82;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelGraphicselement.add(jSeparator7, gridBagConstraints);

        jTabbedPane.addTab("Graphics Element", jPanelGraphicselement);

        jPanelRectangle.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Rectangle radius"));
        jPanel1.setMinimumSize(new java.awt.Dimension(302, 55));
        jPanel1.setPreferredSize(new java.awt.Dimension(302, 55));
        jPanel1.setLayout(null);

        jLabel14.setText("Radius");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(18, 28, 44, 14);

        jNumberFieldRadius.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        try {
            jNumberFieldRadius.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldRadius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldRadiusActionPerformed(evt);
            }
        });
        jPanel1.add(jNumberFieldRadius);
        jNumberFieldRadius.setBounds(64, 26, 40, 19);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 6, 0, 8);
        jPanelRectangle.add(jPanel1, gridBagConstraints);

        jTabbedPane.addTab("Rectangle", jPanelRectangle);

        jPanelLine.setLayout(new java.awt.GridBagLayout());

        jLabel15.setText("Line direction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 8, 0, 0);
        jPanelLine.add(jLabel15, gridBagConstraints);

        jComboBoxLineDirection.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxLineDirection.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxLineDirection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLineDirectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelLine.add(jComboBoxLineDirection, gridBagConstraints);

        jTabbedPane.addTab("Line", jPanelLine);

        jPanelMap.add(mapImagePropertiesPanel1);

        jTabbedPane.addTab("Mapa", jPanelMap);

        jPanelImage.setLayout(new java.awt.GridBagLayout());

        jRTextExpressionAreaImageExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaImageExpression.setElectricScroll(0);
        jRTextExpressionAreaImageExpression.setMinimumSize(new java.awt.Dimension(300, 47));
        jRTextExpressionAreaImageExpression.setPreferredSize(new java.awt.Dimension(300, 47));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelImage.add(jRTextExpressionAreaImageExpression, gridBagConstraints);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Image Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 0);
        jPanelImage.add(jLabel16, gridBagConstraints);

        jButtonFindImage.setText("Find...");
        jButtonFindImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindImageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 6);
        jPanelImage.add(jButtonFindImage, gridBagConstraints);

        jLabel17.setText("Image Expression Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel17, gridBagConstraints);

        jComboBoxImageExpressionClass.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxImageExpressionClass.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxImageExpressionClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxImageExpressionClassActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelImage.add(jComboBoxImageExpressionClass, gridBagConstraints);

        jLabel18.setText("Scale Image");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel18, gridBagConstraints);

        jComboBoxScale.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxScale.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxScaleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelImage.add(jComboBoxScale, gridBagConstraints);

        jCheckBoxImageCache.setText("Using cache");
        jCheckBoxImageCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxImageCacheActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelImage.add(jCheckBoxImageCache, gridBagConstraints);

        jLabel19.setText("Vertical alignment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel19, gridBagConstraints);

        jComboBoxVerticalAlignment.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxVerticalAlignment.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxVerticalAlignment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVerticalAlignmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelImage.add(jComboBoxVerticalAlignment, gridBagConstraints);

        jLabel20.setText("Horizontal alignment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel20, gridBagConstraints);

        jComboBoxHorizontalAlignment.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxHorizontalAlignment.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxHorizontalAlignment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHorizontalAlignmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelImage.add(jComboBoxHorizontalAlignment, gridBagConstraints);

        jLabel21.setText("Evaluation time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel21, gridBagConstraints);

        jComboBoxEvaluationTime.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTime.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEvaluationTimeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 0);
        jPanelImage.add(jComboBoxEvaluationTime, gridBagConstraints);

        jLabel22.setText("Evaluation group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel22, gridBagConstraints);

        jComboBoxImageGroup.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxImageGroup.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxImageGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxImageGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 6);
        jPanelImage.add(jComboBoxImageGroup, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(300, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(300, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 6);
        jPanelImage.add(jSeparator1, gridBagConstraints);

        jCheckBoxImageIsLazy.setText("Is Lazy");
        jCheckBoxImageIsLazy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxImageIsLazyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelImage.add(jCheckBoxImageIsLazy, gridBagConstraints);

        jLabel34.setText("On error type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelImage.add(jLabel34, gridBagConstraints);

        jComboBoxImageOnError.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxImageOnError.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxImageOnError.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxImageOnErrorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelImage.add(jComboBoxImageOnError, gridBagConstraints);

        jTabbedPane.addTab("Image", jPanelImage);

        jPanelText.setLayout(new java.awt.BorderLayout(15, 15));

        jTextAreaText.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextAreaTextInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTextAreaText);

        jPanelText.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Text", jPanelText);

        jPanelFont.setLayout(new java.awt.GridBagLayout());

        jPanel7.setMinimumSize(new java.awt.Dimension(300, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 120));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel23.setText("Report font");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel7.add(jLabel23, gridBagConstraints);

        jComboBoxReportFont.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxReportFont.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxReportFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxReportFontActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(jComboBoxReportFont, gridBagConstraints);

        jLabel24.setText("Font name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel7.add(jLabel24, gridBagConstraints);

        jComboBoxFontName.setMinimumSize(new java.awt.Dimension(244, 20));
        jComboBoxFontName.setPreferredSize(new java.awt.Dimension(244, 20));
        jComboBoxFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFontNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(jComboBoxFontName, gridBagConstraints);

        jNumberComboBoxSize.setMinimumSize(new java.awt.Dimension(50, 20));
        jNumberComboBoxSize.setPreferredSize(new java.awt.Dimension(50, 20));
        jNumberComboBoxSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jNumberComboBoxSizeItemStateChanged(evt);
            }
        });
        jNumberComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberComboBoxSizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel7.add(jNumberComboBoxSize, gridBagConstraints);

        jLabel27.setText("Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        jPanel7.add(jLabel27, gridBagConstraints);

        jLabel25.setText("PDF font name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel7.add(jLabel25, gridBagConstraints);

        jComboBoxPDFFontName.setEditable(true);
        jComboBoxPDFFontName.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxPDFFontName.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxPDFFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPDFFontNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(jComboBoxPDFFontName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanelFont.add(jPanel7, gridBagConstraints);

        jPanel8.setMinimumSize(new java.awt.Dimension(300, 150));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 170));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel12.setLayout(new java.awt.GridBagLayout());

        jCheckBoxBold.setText("Bold");
        jCheckBoxBold.setMinimumSize(new java.awt.Dimension(100, 20));
        jCheckBoxBold.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBoxBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel12.add(jCheckBoxBold, gridBagConstraints);

        jCheckBoxItalic.setText("Italic");
        jCheckBoxItalic.setMinimumSize(new java.awt.Dimension(100, 20));
        jCheckBoxItalic.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBoxItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxItalicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel12.add(jCheckBoxItalic, gridBagConstraints);

        jCheckBoxUnderline.setText("Underline");
        jCheckBoxUnderline.setMinimumSize(new java.awt.Dimension(100, 20));
        jCheckBoxUnderline.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBoxUnderline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUnderlineActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel12.add(jCheckBoxUnderline, gridBagConstraints);

        jCheckBoxStrokeTrough.setText("Strike Trough");
        jCheckBoxStrokeTrough.setMinimumSize(new java.awt.Dimension(100, 20));
        jCheckBoxStrokeTrough.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBoxStrokeTrough.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStrokeTroughActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel12.add(jCheckBoxStrokeTrough, gridBagConstraints);

        jCheckBoxStyledText.setText("Is styled text");
        jCheckBoxStyledText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStyledTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel12.add(jCheckBoxStyledText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel8.add(jPanel12, gridBagConstraints);

        jCheckBoxPDFEmbedded.setText("PDF Embedded");
        jCheckBoxPDFEmbedded.setMaximumSize(new java.awt.Dimension(100, 20));
        jCheckBoxPDFEmbedded.setMinimumSize(new java.awt.Dimension(100, 20));
        jCheckBoxPDFEmbedded.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBoxPDFEmbedded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPDFEmbeddedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jCheckBoxPDFEmbedded, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel8.add(jSeparator2, gridBagConstraints);

        jLabel32.setText("PDF Encoding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel8.add(jLabel32, gridBagConstraints);

        jComboBoxPdfEncoding.setEditable(true);
        jComboBoxPdfEncoding.setMinimumSize(new java.awt.Dimension(200, 20));
        jComboBoxPdfEncoding.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBoxPdfEncoding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPdfEncodingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jComboBoxPdfEncoding, gridBagConstraints);

        jComboBoxHAlign.setMinimumSize(new java.awt.Dimension(95, 20));
        jComboBoxHAlign.setPreferredSize(new java.awt.Dimension(95, 20));
        jComboBoxHAlign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHAlignActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jComboBoxHAlign, gridBagConstraints);

        jComboBoxVAlign.setMinimumSize(new java.awt.Dimension(95, 20));
        jComboBoxVAlign.setPreferredSize(new java.awt.Dimension(95, 20));
        jComboBoxVAlign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVAlignActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel8.add(jComboBoxVAlign, gridBagConstraints);

        jLabel30.setText("Horizontal align");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel8.add(jLabel30, gridBagConstraints);

        jLabel31.setText("Vertical align");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel8.add(jLabel31, gridBagConstraints);

        jComboBoxLineSpacing.setMinimumSize(new java.awt.Dimension(200, 20));
        jComboBoxLineSpacing.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBoxLineSpacing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLineSpacingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jComboBoxLineSpacing, gridBagConstraints);

        jLabel29.setText("Line spacing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel8.add(jLabel29, gridBagConstraints);

        jLabel49.setText("Rotation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jLabel49, gridBagConstraints);

        jComboBoxRotation.setMinimumSize(new java.awt.Dimension(200, 20));
        jComboBoxRotation.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBoxRotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRotationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jComboBoxRotation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = -13;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanelFont.add(jPanel8, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 99;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanelFont.add(jSeparator4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.weighty = 1.0;
        jPanelFont.add(jSeparator5, gridBagConstraints);

        jTabbedPane.addTab("Font", jPanelFont);

        jPanelTextField.setLayout(new java.awt.GridBagLayout());

        jLabel28.setText("Textfield Expression Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 0);
        jPanelTextField.add(jLabel28, gridBagConstraints);

        jComboBoxTextFieldExpressionClass.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxTextFieldExpressionClass.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxTextFieldExpressionClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTextFieldExpressionClassActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelTextField.add(jComboBoxTextFieldExpressionClass, gridBagConstraints);

        jLabel37.setText("Evaluation time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanelTextField.add(jLabel37, gridBagConstraints);

        jComboBoxTextFieldEvaluationTime.setMinimumSize(new java.awt.Dimension(147, 20));
        jComboBoxTextFieldEvaluationTime.setPreferredSize(new java.awt.Dimension(147, 20));
        jComboBoxTextFieldEvaluationTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTextFieldEvaluationTimeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelTextField.add(jComboBoxTextFieldEvaluationTime, gridBagConstraints);

        jLabel38.setText("Evaluation group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanelTextField.add(jLabel38, gridBagConstraints);

        jComboBoxTextFieldGroup.setMinimumSize(new java.awt.Dimension(147, 20));
        jComboBoxTextFieldGroup.setPreferredSize(new java.awt.Dimension(147, 20));
        jComboBoxTextFieldGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTextFieldGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelTextField.add(jComboBoxTextFieldGroup, gridBagConstraints);

        jCheckBoxStretchWithOverflow.setText("Stretch with overflow");
        jCheckBoxStretchWithOverflow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStretchWithOverflowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanelTextField.add(jCheckBoxStretchWithOverflow, gridBagConstraints);

        jCheckBoxBlankWhenNull.setText("Blank when null");
        jCheckBoxBlankWhenNull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBlankWhenNullActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 8);
        jPanelTextField.add(jCheckBoxBlankWhenNull, gridBagConstraints);

        jLabel39.setText("Pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        jPanelTextField.add(jLabel39, gridBagConstraints);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setText("Textfield expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 142);
        jPanelTextField.add(jLabel40, gridBagConstraints);

        jRTextExpressionAreaTextFieldExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTextFieldExpression.setAutoscrolls(true);
        jRTextExpressionAreaTextFieldExpression.setElectricScroll(0);
        jRTextExpressionAreaTextFieldExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaTextFieldExpression.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 6);
        jPanelTextField.add(jRTextExpressionAreaTextFieldExpression, gridBagConstraints);

        jPanel2.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 43));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jComboBoxPattern.setEditable(true);
        jComboBoxPattern.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxPattern.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPatternActionPerformed(evt);
            }
        });
        jComboBoxPattern.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxPatternFocusLost(evt);
            }
        });
        jComboBoxPattern.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jComboBoxPatternInputMethodTextChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel2.add(jComboBoxPattern, gridBagConstraints);

        jButtonCreatePattern.setText("Create...");
        jButtonCreatePattern.setPreferredSize(new java.awt.Dimension(80, 23));
        jButtonCreatePattern.setMaximumSize(new java.awt.Dimension(300, 23));
        jButtonCreatePattern.setMinimumSize(new java.awt.Dimension(80, 23));
        jButtonCreatePattern.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonCreatePattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreatePatternActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jButtonCreatePattern, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanelTextField.add(jPanel2, gridBagConstraints);

        jTabbedPane.addTab("Text Field", jPanelTextField);

        jPanelSubreport1.setLayout(new java.awt.GridBagLayout());

        jLabel41.setText("Connection / Datasource Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelSubreport1.add(jLabel41, gridBagConstraints);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel42.setText("Parameters Map Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        jPanelSubreport1.add(jLabel42, gridBagConstraints);

        jRTextExpressionAreaSubreportMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaSubreportMapExpression.setElectricScroll(0);
        jRTextExpressionAreaSubreportMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaSubreportMapExpression.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelSubreport1.add(jRTextExpressionAreaSubreportMapExpression, gridBagConstraints);

        jComboBoxSubreportConnectionType.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxSubreportConnectionType.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxSubreportConnectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubreportConnectionTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelSubreport1.add(jComboBoxSubreportConnectionType, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelSubreport1.add(jRTextExpressionAreaTextConnectionExpression, gridBagConstraints);

        jCheckBoxSubreportCache.setSelected(true);
        jCheckBoxSubreportCache.setText("Using cache");
        jCheckBoxSubreportCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSubreportCacheActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelSubreport1.add(jCheckBoxSubreportCache, gridBagConstraints);

        jTabbedPane.addTab("Subreport", jPanelSubreport1);

        jPanelSubreport2.setLayout(new java.awt.GridBagLayout());

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Subreport Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanelSubreport2.add(jLabel26, gridBagConstraints);

        jRTextExpressionAreaSubreportExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaSubreportExpression.setElectricScroll(0);
        jRTextExpressionAreaSubreportExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaSubreportExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelSubreport2.add(jRTextExpressionAreaSubreportExpression, gridBagConstraints);

        jLabel33.setText("Subreport Expression Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanelSubreport2.add(jLabel33, gridBagConstraints);

        jComboBoxSubreportExpressionClass.setMinimumSize(new java.awt.Dimension(300, 20));
        jComboBoxSubreportExpressionClass.setPreferredSize(new java.awt.Dimension(300, 20));
        jComboBoxSubreportExpressionClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSubreportExpressionClassActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelSubreport2.add(jComboBoxSubreportExpressionClass, gridBagConstraints);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableSubreportParameters.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableSubreportParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSubreportParametersMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableSubreportParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel16.add(jScrollPane2, gridBagConstraints);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jButtonAddParameter.setText("Add");
        jButtonAddParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel10.add(jButtonAddParameter, gridBagConstraints);

        jButtonModParameter.setText("Modify");
        jButtonModParameter.setEnabled(false);
        jButtonModParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel10.add(jButtonModParameter, gridBagConstraints);

        jButtonRemParameter.setText("Remove");
        jButtonRemParameter.setEnabled(false);
        jButtonRemParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel10.add(jButtonRemParameter, gridBagConstraints);

        jButtonCopyParamsFromMaster.setText("Copy from master");
        jButtonCopyParamsFromMaster.setToolTipText("Pass all the not-builtin parameters to the subreport");
        jButtonCopyParamsFromMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyParamsFromMasterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel10.add(jButtonCopyParamsFromMaster, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.weightx = 1.0;
        jPanel10.add(jPanel13, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel16.add(jPanel10, gridBagConstraints);

        jTabbedPane1.addTab("Subreport parameters", jPanel16);

        jPanelSubreportReturnValues.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableSubreportReturnValues.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subreport variable", "Destination variable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableSubreportReturnValues.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableSubreportReturnValuesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableSubreportReturnValues);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelSubreportReturnValues.add(jScrollPane3, gridBagConstraints);

        jPanel14.setLayout(new java.awt.GridBagLayout());

        jButtonAddReturnValue.setText("Add");
        jButtonAddReturnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddReturnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel14.add(jButtonAddReturnValue, gridBagConstraints);

        jButtonModReturnValue.setText("Modify");
        jButtonModReturnValue.setEnabled(false);
        jButtonModReturnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModReturnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel14.add(jButtonModReturnValue, gridBagConstraints);

        jButtonRemReturnValue.setText("Remove");
        jButtonRemReturnValue.setEnabled(false);
        jButtonRemReturnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemReturnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel14.add(jButtonRemReturnValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.weightx = 1.0;
        jPanel14.add(jPanel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelSubreportReturnValues.add(jPanel14, gridBagConstraints);

        jTabbedPane1.addTab("Subreport return values", jPanelSubreportReturnValues);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 99;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelSubreport2.add(jTabbedPane1, gridBagConstraints);

        jTabbedPane.addTab("Subreport (Other)", jPanelSubreport2);

        jPanelHyperLink.setLayout(new java.awt.GridBagLayout());

        jRTextExpressionAreaAnchorName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaAnchorName.setElectricScroll(0);
        jRTextExpressionAreaAnchorName.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaAnchorName.setPreferredSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaAnchorName.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 300;
        gridBagConstraints.ipady = 46;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelHyperLink.add(jRTextExpressionAreaAnchorName, gridBagConstraints);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Anchor Name Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 6);
        jPanelHyperLink.add(jLabel35, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 300;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 2);
        jPanelHyperLink.add(jSeparator3, gridBagConstraints);

        jComboBoxLinkType.setEditable(true);
        jComboBoxLinkType.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelHyperLink.add(jComboBoxLinkType, gridBagConstraints);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Hyperlink type");
        jLabel36.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel36.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel36.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 5, 0);
        jPanelHyperLink.add(jLabel36, gridBagConstraints);

        jLabelTarget.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTarget.setText("Hyperlink target");
        jLabelTarget.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabelTarget.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabelTarget.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 5, 0);
        jPanelHyperLink.add(jLabelTarget, gridBagConstraints);

        jComboBoxLinkTarget.setMinimumSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxLinkTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLinkTargetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 2);
        jPanelHyperLink.add(jComboBoxLinkTarget, gridBagConstraints);

        jLabelTarget1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTarget1.setText("Bookmark level");
        jLabelTarget1.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabelTarget1.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabelTarget1.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 5, 0);
        jPanelHyperLink.add(jLabelTarget1, gridBagConstraints);

        jSpinnerBookmarkLevel.setFont(new java.awt.Font("Default", 0, 11));
        jSpinnerBookmarkLevel.setMinimumSize(new java.awt.Dimension(80, 20));
        jSpinnerBookmarkLevel.setPreferredSize(new java.awt.Dimension(80, 20));
        jSpinnerBookmarkLevel.setRequestFocusEnabled(false);
        jSpinnerBookmarkLevel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerBookmarkLevelStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 2);
        jPanelHyperLink.add(jSpinnerBookmarkLevel, gridBagConstraints);

        jPanel24.setMinimumSize(new java.awt.Dimension(457, 163));
        jPanel24.setPreferredSize(new java.awt.Dimension(321, 163));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(445, 163));
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(309, 163));

        jPanelReference.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanelReference.setLayout(new java.awt.GridBagLayout());

        jRTextExpressionAreaReference.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaReference.setEnabled(false);
        jRTextExpressionAreaReference.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaReference.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaReference.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jRTextExpressionAreaReference, gridBagConstraints);

        jLabelReference.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelReference.setText("Hyperlink Reference Expression");
        jLabelReference.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelReference.add(jLabelReference, gridBagConstraints);

        jTabbedPane2.addTab("Reference", jPanelReference);

        jPanelAnchor.setMinimumSize(new java.awt.Dimension(440, 100));
        jPanelAnchor.setLayout(new java.awt.GridBagLayout());

        jRTextExpressionAreaAnchor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaAnchor.setEnabled(false);
        jRTextExpressionAreaAnchor.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaAnchor.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaAnchor.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jRTextExpressionAreaAnchor, gridBagConstraints);

        jLabelAnchor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelAnchor.setText("Hyperlink Anchor Expression");
        jLabelAnchor.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelAnchor.add(jLabelAnchor, gridBagConstraints);

        jTabbedPane2.addTab("Anchor", jPanelAnchor);

        jPanelPage.setMinimumSize(new java.awt.Dimension(428, 100));
        jPanelPage.setLayout(new java.awt.GridBagLayout());

        jLabelPage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelPage.setText("Hyperlink Page Expression");
        jLabelPage.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jLabelPage, gridBagConstraints);

        jRTextExpressionAreaPage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaPage.setEnabled(false);
        jRTextExpressionAreaPage.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaPage.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaPage.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelPage.add(jRTextExpressionAreaPage, gridBagConstraints);

        jTabbedPane2.addTab("Page", jPanelPage);

        jPanelLinkParams.setLayout(new java.awt.GridBagLayout());

        jTableLinkParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Link parameter", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLinkParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLinkParametersMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTableLinkParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelLinkParams.add(jScrollPane5, gridBagConstraints);

        jPanel25.setLayout(new java.awt.GridBagLayout());

        jButtonAddLinkParameter.setText("Add");
        jButtonAddLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel25.add(jButtonAddLinkParameter, gridBagConstraints);

        jButtonModLinkParameter.setText("Modify");
        jButtonModLinkParameter.setEnabled(false);
        jButtonModLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel25.add(jButtonModLinkParameter, gridBagConstraints);

        jButtonRemLinkParameter.setText("Remove");
        jButtonRemLinkParameter.setEnabled(false);
        jButtonRemLinkParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemLinkParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel25.add(jButtonRemLinkParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanelLinkParams.add(jPanel25, gridBagConstraints);

        jTabbedPane2.addTab("Link parameters", jPanelLinkParams);

        jPanelTooltip.setLayout(new java.awt.GridBagLayout());

        jLabelTooltip.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelTooltip.setText("Hyperlink Tooltip Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jLabelTooltip, gridBagConstraints);

        jRTextExpressionAreaTooltip.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaTooltip.setMinimumSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTooltip.setPreferredSize(new java.awt.Dimension(300, 50));
        jRTextExpressionAreaTooltip.setViewScrollbars(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanelTooltip.add(jRTextExpressionAreaTooltip, gridBagConstraints);

        jTabbedPane2.addTab("Tooltip", jPanelTooltip);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.5;
        gridBagConstraints.weighty = 1.5;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 2, 6);
        jPanel24.add(jTabbedPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.5;
        jPanelHyperLink.add(jPanel24, gridBagConstraints);

        jTabbedPane.addTab("Hyper Link", jPanelHyperLink);

        jPanelBarcode.setLayout(new java.awt.GridBagLayout());

        jLabel47.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelBarcode.add(jLabel47, gridBagConstraints);

        jComboBoxBarcodeType.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxBarcodeType.setPreferredSize(new java.awt.Dimension(200, 20));
        jComboBoxBarcodeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBarcodeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelBarcode.add(jComboBoxBarcodeType, gridBagConstraints);

        jBarcodeExpressionArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jBarcodeExpressionArea.setAutoscrolls(true);
        jBarcodeExpressionArea.setElectricScroll(0);
        jBarcodeExpressionArea.setMinimumSize(new java.awt.Dimension(0, 21));
        jBarcodeExpressionArea.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 6, 6);
        jPanelBarcode.add(jBarcodeExpressionArea, gridBagConstraints);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("Barcode expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelBarcode.add(jLabel48, gridBagConstraints);

        jSeparator8.setMinimumSize(new java.awt.Dimension(300, 2));
        jSeparator8.setPreferredSize(new java.awt.Dimension(300, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 6);
        jPanelBarcode.add(jSeparator8, gridBagConstraints);

        jComboBoxBarcodeGroup.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxBarcodeGroup.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxBarcodeGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBarcodeGroupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 6);
        jPanelBarcode.add(jComboBoxBarcodeGroup, gridBagConstraints);

        jLabel43.setText("Evaluation group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel43, gridBagConstraints);

        jComboBoxEvaluationTimeBarcode.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTimeBarcode.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTimeBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEvaluationTimeBarcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 0);
        jPanelBarcode.add(jComboBoxEvaluationTimeBarcode, gridBagConstraints);

        jLabel50.setText("Evaluation time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel50, gridBagConstraints);

        jLabel54.setText("Scale Barcode Image");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel54, gridBagConstraints);

        jLabel55.setText("On error type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel55, gridBagConstraints);

        jComboBoxImageOnError1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxImageOnError1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxImageOnError1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxImageOnErrorActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelBarcode.add(jComboBoxImageOnError1, gridBagConstraints);

        jComboBoxScale1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxScale1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxScale1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxScaleActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelBarcode.add(jComboBoxScale1, gridBagConstraints);

        jLabel56.setText("Horizontal alignment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel56, gridBagConstraints);

        jLabel57.setText("Vertical alignment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelBarcode.add(jLabel57, gridBagConstraints);

        jComboBoxVerticalAlignment1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxVerticalAlignment1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxVerticalAlignment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVerticalAlignmentActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        jPanelBarcode.add(jComboBoxVerticalAlignment1, gridBagConstraints);

        jComboBoxHorizontalAlignment1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxHorizontalAlignment1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxHorizontalAlignment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHorizontalAlignmentActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 6);
        jPanelBarcode.add(jComboBoxHorizontalAlignment1, gridBagConstraints);

        jSeparator10.setMinimumSize(new java.awt.Dimension(300, 2));
        jSeparator10.setPreferredSize(new java.awt.Dimension(300, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 6);
        jPanelBarcode.add(jSeparator10, gridBagConstraints);

        jPanel22.setLayout(new java.awt.GridBagLayout());

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Bar height");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 16, 0, 0);
        jPanel22.add(jLabel58, gridBagConstraints);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("(0 = default)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel22.add(jLabel60, gridBagConstraints);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel61.setText("Bar width");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel22.add(jLabel61, gridBagConstraints);

        jNumberFieldBCWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldBCWidth.setText("0");
        jNumberFieldBCWidth.setToolTipText("Sets the desired width for the bars in the barcode (in pixels).");
        try {
            jNumberFieldBCWidth.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldBCWidth.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldBCWidth.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldBCWidth.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldBCWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldBCHeightjNumberFieldTopActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 0);
        jPanel22.add(jNumberFieldBCWidth, gridBagConstraints);

        jNumberFieldBCHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldBCHeight.setText("0");
        jNumberFieldBCHeight.setToolTipText("Sets the desired height for the bars in the barcode (in pixels)");
        try {
            jNumberFieldBCHeight.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldBCHeight.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldBCHeight.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldBCHeight.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldBCHeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldTopActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 8);
        jPanel22.add(jNumberFieldBCHeight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelBarcode.add(jPanel22, gridBagConstraints);

        jBarcodeExpressionAreaAppIdentifier.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jBarcodeExpressionAreaAppIdentifier.setAutoscrolls(true);
        jBarcodeExpressionAreaAppIdentifier.setElectricScroll(0);
        jBarcodeExpressionAreaAppIdentifier.setMinimumSize(new java.awt.Dimension(0, 21));
        jBarcodeExpressionAreaAppIdentifier.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 6, 6);
        jPanelBarcode.add(jBarcodeExpressionAreaAppIdentifier, gridBagConstraints);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel59.setText("Application identifier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanelBarcode.add(jLabel59, gridBagConstraints);

        jPanel23.setLayout(new java.awt.GridBagLayout());

        jCheckBoxBarcodeCheckSum.setText("Checksum");
        jCheckBoxBarcodeCheckSum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel23.add(jCheckBoxBarcodeCheckSum, gridBagConstraints);

        jCheckBoxBarcodeShowText.setText("Show Text");
        jCheckBoxBarcodeShowText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        jPanel23.add(jCheckBoxBarcodeShowText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        jPanelBarcode.add(jPanel23, gridBagConstraints);

        jTabbedPane.addTab("Barcode", jPanelBarcode);

        jPanelChart.setLayout(new java.awt.GridBagLayout());
        jPanelChart.add(jSeparator6, new java.awt.GridBagConstraints());

        jSeparator9.setMinimumSize(new java.awt.Dimension(300, 2));
        jSeparator9.setPreferredSize(new java.awt.Dimension(300, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 6);
        jPanelChart.add(jSeparator9, gridBagConstraints);

        jLabel44.setText("Evaluation time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelChart.add(jLabel44, gridBagConstraints);

        jComboBoxEvaluationTime1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTime1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxEvaluationTime1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEvaluationTime1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 0);
        jPanelChart.add(jComboBoxEvaluationTime1, gridBagConstraints);

        jLabel45.setText("Evaluation group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        jPanelChart.add(jLabel45, gridBagConstraints);

        jComboBoxImageGroup1.setMinimumSize(new java.awt.Dimension(150, 20));
        jComboBoxImageGroup1.setPreferredSize(new java.awt.Dimension(150, 20));
        jComboBoxImageGroup1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxImageGroup1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 6, 6);
        jPanelChart.add(jComboBoxImageGroup1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelChart.add(jPanel9, gridBagConstraints);

        jButton1.setLabel("Edit chart properties");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        jPanelChart.add(jButton1, gridBagConstraints);

        jTabbedPane.addTab("Chart", jPanelChart);

        jPanelBorder.setLayout(new java.awt.GridBagLayout());
        jTabbedPane.addTab("Border", jPanelBorder);

        jPanelSheet.setLayout(new java.awt.GridBagLayout());
        jTabbedPane.addTab("All", jPanelSheet);

        jPanelCrosstab.setLayout(new java.awt.GridBagLayout());

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Parameters Map Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanelCrosstab.add(jLabel51, gridBagConstraints);

        jRTextExpressionAreaCrosstabParametersMapExpression.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionAreaCrosstabParametersMapExpression.setElectricScroll(0);
        jRTextExpressionAreaCrosstabParametersMapExpression.setMinimumSize(new java.awt.Dimension(0, 0));
        jRTextExpressionAreaCrosstabParametersMapExpression.setPreferredSize(new java.awt.Dimension(300, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 8, 6);
        jPanelCrosstab.add(jRTextExpressionAreaCrosstabParametersMapExpression, gridBagConstraints);

        jPanel18.setLayout(new java.awt.GridBagLayout());

        jScrollPane4.setMinimumSize(new java.awt.Dimension(300, 50));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(300, 50));

        jTableCrosstabParameters.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableCrosstabParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCrosstabParametersMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableCrosstabParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanel18.add(jScrollPane4, gridBagConstraints);

        jPanel19.setLayout(new java.awt.GridBagLayout());

        jButtonAddCrosstabParameter.setText("Add");
        jButtonAddCrosstabParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCrosstabParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 6, 0);
        jPanel19.add(jButtonAddCrosstabParameter, gridBagConstraints);

        jButtonModCrosstabParameter.setText("Modify");
        jButtonModCrosstabParameter.setEnabled(false);
        jButtonModCrosstabParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModCrosstabParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel19.add(jButtonModCrosstabParameter, gridBagConstraints);

        jButtonRemCrosstabParameter.setText("Remove");
        jButtonRemCrosstabParameter.setEnabled(false);
        jButtonRemCrosstabParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemCrosstabParameterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 6, 0);
        jPanel19.add(jButtonRemCrosstabParameter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.weightx = 1.0;
        jPanel19.add(jPanel20, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 6);
        jPanel18.add(jPanel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelCrosstab.add(jPanel18, gridBagConstraints);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("Crosstab parameters");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 8, 0, 0);
        jPanelCrosstab.add(jLabel52, gridBagConstraints);

        jCheckBoxRepeatColumnHeaders.setText("Repeat column headers");
        jCheckBoxRepeatColumnHeaders.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxRepeatColumnHeaders.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxRepeatColumnHeaders.setMaximumSize(new java.awt.Dimension(300, 15));
        jCheckBoxRepeatColumnHeaders.setMinimumSize(new java.awt.Dimension(150, 15));
        jCheckBoxRepeatColumnHeaders.setPreferredSize(new java.awt.Dimension(150, 15));
        jCheckBoxRepeatColumnHeaders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRepeatColumnHeadersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        jPanelCrosstab.add(jCheckBoxRepeatColumnHeaders, gridBagConstraints);

        jCheckBoxRepeatRowHeaders.setText("Repeat row headers");
        jCheckBoxRepeatRowHeaders.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxRepeatRowHeaders.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxRepeatRowHeaders.setMaximumSize(new java.awt.Dimension(150, 15));
        jCheckBoxRepeatRowHeaders.setMinimumSize(new java.awt.Dimension(150, 15));
        jCheckBoxRepeatRowHeaders.setPreferredSize(new java.awt.Dimension(150, 15));
        jCheckBoxRepeatRowHeaders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRepeatRowHeadersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        jPanelCrosstab.add(jCheckBoxRepeatRowHeaders, gridBagConstraints);

        jButton2.setText("Edit crosstab properties");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 0, 6);
        jPanelCrosstab.add(jButton2, gridBagConstraints);

        jPanel21.setLayout(new java.awt.GridBagLayout());

        jNumberFieldColumnBreakOffset.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNumberFieldColumnBreakOffset.setText("0");
        try {
            jNumberFieldColumnBreakOffset.setDecimals(0);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            jNumberFieldColumnBreakOffset.setInteger(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jNumberFieldColumnBreakOffset.setMinimumSize(new java.awt.Dimension(45, 20));
        jNumberFieldColumnBreakOffset.setPreferredSize(new java.awt.Dimension(45, 20));
        jNumberFieldColumnBreakOffset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNumberFieldColumnBreakOffsetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 6);
        jPanel21.add(jNumberFieldColumnBreakOffset, gridBagConstraints);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Column break offset");
        jLabel53.setMaximumSize(new java.awt.Dimension(200, 15));
        jLabel53.setMinimumSize(new java.awt.Dimension(110, 15));
        jLabel53.setPreferredSize(new java.awt.Dimension(130, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 20, 0, 0);
        jPanel21.add(jLabel53, gridBagConstraints);

        jPanelCrosstab.add(jPanel21, new java.awt.GridBagConstraints());

        jTabbedPane.addTab("Crosstab", jPanelCrosstab);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-442)/2, (screenSize.height-408)/2, 442, 408);
    }// </editor-fold>//GEN-END:initComponents
    
    private void jTableLinkParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLinkParametersMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            if (jTableLinkParameters.getSelectedRowCount() > 0) {
                jButtonModLinkParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableLinkParametersMouseClicked
    
    private void jButtonRemLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemLinkParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
        int[]  rows= jTableLinkParameters.getSelectedRows();
        for (int i=rows.length-1; i>=0; --i) {
            element.getLinkParameters().remove( jTableLinkParameters.getValueAt( rows[i], 0) );
            dtm.removeRow(rows[i]);
        }
        
    }//GEN-LAST:event_jButtonRemLinkParameterActionPerformed
    
    private void jButtonModLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModLinkParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        modifyLinkParameter(JRLinkParameterDialog.COMPONENT_NONE);
    }//GEN-LAST:event_jButtonModLinkParameterActionPerformed
    
    private void jButtonAddLinkParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddLinkParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRLinkParameter parameter = jrpd.getParameter();
            element.getLinkParameters().add( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getExpression()});
        }
    }//GEN-LAST:event_jButtonAddLinkParameterActionPerformed
    
    private void jButtonCopyParamsFromMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyParamsFromMasterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportParameters.getModel();
        
        Vector parameters = jrf.getReport().getParameters();
        
        for (int i=0; i<parameters.size(); ++i) {
            JRParameter jrParameter = (JRParameter)parameters.get(i);
            if (jrParameter.isBuiltin()) continue;
            
            // Check if a similar parameter already exists...
            boolean found = false;
            for (int k=0; k<element.getSubreportParameters().size(); ++k) {
                JRSubreportParameter sp = (JRSubreportParameter)element.getSubreportParameters().get(k);
                if (sp.getName().equals( jrParameter.getName())) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                JRSubreportParameter parameter = new JRSubreportParameter(jrParameter.getName(), "$P{" + jrParameter.getName() + "}");
                element.getSubreportParameters().addElement( parameter );
                dtm.addRow(new Object[]{parameter, parameter.getExpression()});
            }
            
        }
        
    }//GEN-LAST:event_jButtonCopyParamsFromMasterActionPerformed
    
    private void jNumberFieldBCHeightjNumberFieldTopActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldBCHeightjNumberFieldTopActionPerformed1
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            BarcodeReportElement element = (BarcodeReportElement)e.nextElement();
            // 1. Set element properties...
            // For each panel, search
            element.setImageWidth((int)jNumberFieldBCWidth.getValue() );
        }
        repaintEditor();
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jNumberFieldBCHeightjNumberFieldTopActionPerformed1
    
    private void jNumberFieldTopActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldTopActionPerformed1
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            BarcodeReportElement element = (BarcodeReportElement)e.nextElement();
            // 1. Set element properties...
            // For each panel, search
            element.setImageHeight((int)jNumberFieldBCHeight.getValue() );
        }
        repaintEditor();
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jNumberFieldTopActionPerformed1
    
    private void jComboBoxHorizontalAlignmentActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHorizontalAlignmentActionPerformed1
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment1) == null || (Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment1)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setHorizontalAlignment(""+ Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment1));
        }
        
        this.jComboBoxHorizontalAlignment1.removeItem("");
        repaintEditor();
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxHorizontalAlignmentActionPerformed1
    
    private void jComboBoxVerticalAlignmentActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVerticalAlignmentActionPerformed1
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment1) == null || (Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment1)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setVerticalAlignment(""+ Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment1));
        }
        
        this.jComboBoxVerticalAlignment1.removeItem("");
        repaintEditor();
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxVerticalAlignmentActionPerformed1
    
    private void jComboBoxScaleActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxScaleActionPerformed1
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue( jComboBoxScale1 ) == null || (Misc.getComboboxSelectedValue( jComboBoxScale1 )+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setScaleImage(""+ Misc.getComboboxSelectedValue( jComboBoxScale1 ));
        }
        
        this.jComboBoxScale1.removeItem("");
        repaintEditor();
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxScaleActionPerformed1
    
    private void jComboBoxImageOnErrorActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxImageOnErrorActionPerformed1
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxImageOnError1) == null || (Misc.getComboboxSelectedValue(jComboBoxImageOnError1)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setOnErrorType(""+ Misc.getComboboxSelectedValue(jComboBoxImageOnError1));
        }
        
        this.jComboBoxImageOnError1.removeItem("");
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxImageOnErrorActionPerformed1
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        if (getElementSelection().size() > 0) {
            ReportElement re = (ReportElement)getElementSelection().elementAt(0);
            
            if (re instanceof CrosstabReportElement ) {
                it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog cpd = new it.businesslogic.ireport.crosstab.gui.CrosstabPropertiesDialog(MainFrame.getMainInstance(),true);
                cpd.setCurrentCrosstabReportElement( (CrosstabReportElement)re);
                cpd.setVisible(true);
            }
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jTableCrosstabParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCrosstabParametersMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            if (jTableCrosstabParameters.getSelectedRowCount() > 0) {
                jButtonModCrosstabParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableCrosstabParametersMouseClicked
    
    private void jButtonRemCrosstabParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemCrosstabParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
        
        jTableCrosstabParameters.getSelectedRows();
        int[]  rows= jTableCrosstabParameters.getSelectedRows();
        while (jTableCrosstabParameters.getSelectedRowCount() > 0) {
            int i = jTableCrosstabParameters.getSelectedRow();
            element.getCrosstabParameters().removeElement( jTableCrosstabParameters.getValueAt( i, 0) );
            ((javax.swing.table.DefaultTableModel)jTableCrosstabParameters.getModel()).removeRow(i);
            
        }
        jTableCrosstabParameters.updateUI();
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        
    }//GEN-LAST:event_jButtonRemCrosstabParameterActionPerformed
    
    private void jButtonModCrosstabParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModCrosstabParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        modifyCrosstabParameter(CrosstabParameterDialog.COMPONENT_NONE);
        
    }//GEN-LAST:event_jButtonModCrosstabParameterActionPerformed
    
    private void jButtonAddCrosstabParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCrosstabParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
        
        it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog jrpd = new it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.crosstab.CrosstabParameter parameter = jrpd.getParameter();
            element.getCrosstabParameters().addElement( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableCrosstabParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getParameterValueExpression()});
            jTableCrosstabParameters.updateUI();
            
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            
        }
    }//GEN-LAST:event_jButtonAddCrosstabParameterActionPerformed
    
    private void jNumberFieldColumnBreakOffsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldColumnBreakOffsetActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        
        while (e.hasMoreElements()) {
            CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
            element.setColumnBreakOffset( (int)jNumberFieldColumnBreakOffset.getValue());
            
        }
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jNumberFieldColumnBreakOffsetActionPerformed
    
    private void jCheckBoxRepeatRowHeadersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRepeatRowHeadersActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
            element.setRepeatRowHeaders(jCheckBoxRepeatRowHeaders.isSelected());
        }
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jCheckBoxRepeatRowHeadersActionPerformed
    
    private void jCheckBoxRepeatColumnHeadersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRepeatColumnHeadersActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
            element.setRepeatColumnHeaders(jCheckBoxRepeatColumnHeaders.isSelected());
        }
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jCheckBoxRepeatColumnHeadersActionPerformed
    
    private void jComboBoxStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStyleActionPerformed
        
        if (this.isInit()) return;
        
        // Set the new value for all selected elements...
        Style s = null;
        if (jComboBoxStyle.getSelectedIndex() <= 0) {
            s = null;
        } else {
            s = (Style)jComboBoxStyle.getSelectedItem();
        }
        
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setStyle(s);
            
        }
        
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        // Update coords...
        repaintEditor();
        
        
        
    }//GEN-LAST:event_jComboBoxStyleActionPerformed
    
    private void jTableSubreportReturnValuesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSubreportReturnValuesMouseClicked
        
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            if (jTableSubreportReturnValues.getSelectedRowCount() > 0) {
                jButtonModReturnValueActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jTableSubreportReturnValuesMouseClicked
    
    private void jTableSubreportParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSubreportParametersMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1) {
            if (jTableSubreportParameters.getSelectedRowCount() > 0) {
                jButtonModParameterActionPerformed(null);
            }
        }
    }//GEN-LAST:event_jTableSubreportParametersMouseClicked
    
    private void jComboBoxImageOnErrorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxImageOnErrorActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxImageOnError) == null || (Misc.getComboboxSelectedValue(jComboBoxImageOnError)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setOnErrorType(""+ Misc.getComboboxSelectedValue(jComboBoxImageOnError));
        }
        
        this.jComboBoxImageOnError.removeItem("");
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxImageOnErrorActionPerformed
    
    private void jSpinnerBookmarkLevelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerBookmarkLevelStateChanged
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0 || jComboBoxLinkTarget.getSelectedItem() == null || (jComboBoxLinkTarget.getSelectedItem()+"").equals("")) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setBookmarkLevel( ((Integer)jSpinnerBookmarkLevel.getValue()).intValue() );
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jSpinnerBookmarkLevelStateChanged
    
    private void jButtonRemReturnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemReturnValueActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        //jTableSubreportReturnValues.getSelectedRows();
        //int[]  rows= jTableSubreportReturnValues.getSelectedRows();
        //for (int i=rows.length-1; i>=0; --i) {
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportReturnValues.getModel();
        while (jTableSubreportReturnValues.getSelectedRow() >= 0 ) {
            element.getReturnValues().removeElement( jTableSubreportReturnValues.getValueAt( jTableSubreportReturnValues.getSelectedRow(), 0) );
            dtm.removeRow( jTableSubreportReturnValues.getSelectedRow() );
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jButtonRemReturnValueActionPerformed
    
    private void jButtonModReturnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModReturnValueActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        JRSubreportReturnValue returnValue = (JRSubreportReturnValue)jTableSubreportReturnValues.getValueAt( jTableSubreportReturnValues.getSelectedRow(), 0);
        
        JRSubreportReturnValueDialog jrpd = new JRSubreportReturnValueDialog(this, true);
        jrpd.setSubreportReturnValue( returnValue );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            returnValue.setSubreportVariable( jrpd.getSubreportReturnValue().getSubreportVariable() );
            returnValue.setToVariable( jrpd.getSubreportReturnValue().getToVariable() );
            returnValue.setCalculation( jrpd.getSubreportReturnValue().getCalculation() );
            returnValue.setIncrementFactoryClass( jrpd.getSubreportReturnValue().getIncrementFactoryClass() );
            
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportReturnValues.getModel();
            dtm.setValueAt(returnValue, jTableSubreportReturnValues.getSelectedRow(), 0);
            dtm.setValueAt(returnValue.getToVariable(), jTableSubreportReturnValues.getSelectedRow(), 1);
            
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            
        }
    }//GEN-LAST:event_jButtonModReturnValueActionPerformed
    
    private void jButtonAddReturnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddReturnValueActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        Vector variables = it.businesslogic.ireport.gui.MainFrame.getMainInstance().getActiveReportFrame().getReport().getVariables();
        int var_count = 0;
        for (int i=0; i<variables.size(); ++i) {
            JRVariable var = (JRVariable)variables.elementAt(i);
            if (!var.isBuiltin()) {
                var_count++;
            }
        }
        
        if (var_count == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("messages.elementPropertiesDialgo.noVariableAvailable","No variables are available in this report to store a return value.\nPlease define a variable first."),
                    I18n.getString("messages.elementPropertiesDialgo.noVariableAvailableCaption","No variables"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JRSubreportReturnValueDialog jrpd = new JRSubreportReturnValueDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRSubreportReturnValue returnValue = jrpd.getSubreportReturnValue();
            element.getReturnValues().addElement( returnValue );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportReturnValues.getModel();
            dtm.addRow(new Object[]{returnValue, returnValue.getToVariable()});
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            
        }
    }//GEN-LAST:event_jButtonAddReturnValueActionPerformed
    
    private void jCheckBoxImageIsLazyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxImageIsLazyActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setIsLazy(jCheckBoxImageIsLazy.isSelected() );
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jCheckBoxImageIsLazyActionPerformed
    
    private void jComboBoxPatternFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxPatternFocusLost
        //jComboBoxPatternActionPerformed(new java.awt.event.ActionEvent(evt.getSource(),0,""));
    }//GEN-LAST:event_jComboBoxPatternFocusLost
    
    private void jComboBoxLinkTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTargetActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0 || jComboBoxLinkTarget.getSelectedItem() == null || (jComboBoxLinkTarget.getSelectedItem()+"").equals("")) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setHyperlinkTarget( ""+jComboBoxLinkTarget.getSelectedItem());
        }
        jComboBoxLinkTarget.removeItem("");
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }//GEN-LAST:event_jComboBoxLinkTargetActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if (getElementSelection().elementAt(0) instanceof ChartReportElement) {
            it.businesslogic.ireport.chart.gui.IReportChartDialog icd = new it.businesslogic.ireport.chart.gui.IReportChartDialog(this,true);
            icd.setJReportFrame( jrf );
            ChartReportElement cre =  (ChartReportElement)getElementSelection().elementAt(0);
            icd.setProperties(cre.getProps());
            icd.setVisible(true);
            if (icd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                // Integrate properties...
                Properties props = icd.getProperties();
                Iterator keys = props.keySet().iterator();
                while (keys.hasNext()) {
                    String key = ""+keys.next();
                    String val = props.getProperty(key);
                    cre.getProps().setProperty(key, val);
                    
                }
                
                cre.updateChartImage();
                jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
                
                //setImg( it.businesslogic.ireport.chart.AvailableCharts.getChartIcon( props.getProperty("chartName")).getImage());
                repaintEditor();
            }
        } else if (getElementSelection().elementAt(0) instanceof ChartReportElement2) {
            ChartReportElement2 cre =  (ChartReportElement2)getElementSelection().elementAt(0);
            it.businesslogic.ireport.chart.gui.ChartPropertiesDialog cpd = new it.businesslogic.ireport.chart.gui.ChartPropertiesDialog(this,true);
            
            try {
            cpd.setJReportFrame( jrf );
            cpd.setChartElement(cre);
            cpd.setVisible(true);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
            if (cpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
                //repaintEditor();
            }
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jComboBoxImageGroup1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxImageGroup1ActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxImageGroup1.getSelectedItem() == null || (jComboBoxImageGroup1.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            if (element instanceof ImageReportElement) {
                ((ImageReportElement)element).setEvaluationGroup(""+ jComboBoxImageGroup1.getSelectedItem());
            } else if (element instanceof ChartReportElement2) {
                ((ChartReportElement2)element).setEvaluationGroup(""+ jComboBoxImageGroup1.getSelectedItem());
            }
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        this.jComboBoxImageGroup1.removeItem("");
    }//GEN-LAST:event_jComboBoxImageGroup1ActionPerformed
    
    private void jComboBoxEvaluationTime1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEvaluationTime1ActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxEvaluationTime1) == null || (Misc.getComboboxSelectedValue(jComboBoxEvaluationTime1)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            if (element instanceof ImageReportElement) {
                ((ImageReportElement)element).setEvaluationTime(""+ Misc.getComboboxSelectedValue(jComboBoxEvaluationTime1));
            } else if (element instanceof ChartReportElement2) {
                
                ((ChartReportElement2)element).setEvaluationTime(""+ Misc.getComboboxSelectedValue(jComboBoxEvaluationTime1));
            }
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        this.jComboBoxEvaluationTime1.removeItem("");
    }//GEN-LAST:event_jComboBoxEvaluationTime1ActionPerformed
    
    private void jButtonCreatePatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreatePatternActionPerformed
        
        FieldPatternDialog fpd = new FieldPatternDialog(MainFrame.getMainInstance() ,true);
        fpd.setVisible(true);
        if (fpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            jComboBoxPattern.setSelectedItem( fpd.getPattern() );
        }
        
    }//GEN-LAST:event_jButtonCreatePatternActionPerformed
    
    private void jComboBoxBarcodeGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBarcodeGroupActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxBarcodeGroup.getSelectedItem() == null || (jComboBoxBarcodeGroup.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setEvaluationGroup(""+ jComboBoxBarcodeGroup.getSelectedItem());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        this.jComboBoxBarcodeGroup.removeItem("");
    }//GEN-LAST:event_jComboBoxBarcodeGroupActionPerformed
    
    private void jComboBoxEvaluationTimeBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEvaluationTimeBarcodeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxEvaluationTimeBarcode) == null || (Misc.getComboboxSelectedValue(jComboBoxEvaluationTimeBarcode)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setEvaluationTime(""+ Misc.getComboboxSelectedValue(jComboBoxEvaluationTimeBarcode));
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        this.jComboBoxEvaluationTimeBarcode.removeItem("");
    }//GEN-LAST:event_jComboBoxEvaluationTimeBarcodeActionPerformed
    
    private void jCheckBoxStyledTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStyledTextActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setIsStyledText(jCheckBoxStyledText.isSelected());
            element.setFont(null);
            /* Adjust PDF font...*/
        }
        fireReportElementsChangedEvent("styledText", ""+jCheckBoxStyledText.isSelected());
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxStyledTextActionPerformed
    
    private void jComboBoxRotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRotationActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxRotation) == null || (Misc.getComboboxSelectedValue(jComboBoxRotation)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setRotate(""+ Misc.getComboboxSelectedValue(jComboBoxRotation));
        }
        fireReportElementsChangedEvent("rotate", ""+Misc.getComboboxSelectedValue(jComboBoxRotation));
        this.jComboBoxRotation.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxRotationActionPerformed
    
	private void BarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarcodeActionPerformed
            if (this.isInit()) return;
            
            // Set band to all....
            if (jrf == null || getElementSelection().size()==0) return;
            // Set the new value for all selected elements...
            Enumeration e = getElementSelection().elements();
            while (e.hasMoreElements()) {
                BarcodeReportElement element = (BarcodeReportElement) e.nextElement();
                
                if (jComboBoxBarcodeType.getSelectedItem() != null && jComboBoxBarcodeType.getSelectedItem() instanceof Tag) {
                    element.setType( Integer.parseInt(""+((Tag)jComboBoxBarcodeType.getSelectedItem()).getValue()) );
                }
                
                element.setText(jBarcodeExpressionArea.getText());
                element.setShowText(jCheckBoxBarcodeShowText.isSelected());
                element.setCheckSum(jCheckBoxBarcodeCheckSum.isSelected());
                repaintEditor();
                
                //show last error in console
                if(element.getLastError() != null){
                    jrf.getMainFrame().logOnConsole("Error BarcodeReportElement(" + element.getName() + "): " + element.getLastError()+"\n");
                }
            }
	}//GEN-LAST:event_BarcodeActionPerformed
        
        private void jComboBoxBarcodeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBarcodeTypeActionPerformed
            BarcodeActionPerformed(evt);
        }//GEN-LAST:event_jComboBoxBarcodeTypeActionPerformed
        
    private void jNumberFieldLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldLeftActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            if (element instanceof BreakReportElement) continue;
            int normalization = 0;
            if (element.getParentElement() != null) {
                normalization = (int)(element.getParentElement().getPosition().getX()) - 10;
            } else if (element.getCell() != null) {
                normalization = element.getCell().getLeft();
            } else {
                normalization = jrf.getReport().getLeftMargin();
            }
            
            element.trasform( new Point( (int)jNumberFieldLeft.getValue()- (element.getPosition().x-10- normalization) ,0 ), TransformationType.TRANSFORMATION_MOVE);
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
        }
        repaintEditor();
    }//GEN-LAST:event_jNumberFieldLeftActionPerformed
    
    private void jNumberFieldTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldTopActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        Band last_band = null;
        int y_location = 0;
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            // 1. Set element properties...
            // For each panel, search
            if (element.getBand() != null && (last_band == null || last_band != element.getBand())) {
                y_location = this.jrf.getReport().getBandYLocation(element.getBand());
                last_band = element.getBand();
            }
            
            if (element.getBand() == null && element.getCell() !=null) {
                y_location = element.getCell().getTop();
            }
            
            if (element.getParentElement() != null) {
                y_location = (int)(element.getParentElement().getPosition().getY())-10;
            }
            
            element.trasform( new Point(0, (int)jNumberFieldTop.getValue()- (element.getPosition().y - 10 - y_location) ) , TransformationType.TRANSFORMATION_MOVE);
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
            
        }
        repaintEditor();
    }//GEN-LAST:event_jNumberFieldTopActionPerformed
    
    private void jComboBoxBandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBandActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxBand.getSelectedItem() == null || (jComboBoxBand.getSelectedIndex() == 0 && (jComboBoxBand.getSelectedItem()+"").trim().equals("") )) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            
            if (jrf.getSelectedCrosstabEditorPanel() == null) {
                element.setBand((Band) jComboBoxBand.getSelectedItem() ) ;
            } else {
                element.setCell((CrosstabCell) jComboBoxBand.getSelectedItem() ) ;
                element.setRelativePosition( new Point( element.getPosition().x - element.getCell().getLeft() - 10, element.getPosition().y - element.getCell().getTop() - 10 ));
            }
            
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection(), ReportElementChangedEvent.CHANGED));
        
        // Update coords...
        repaintEditor();
        
        e = getElementSelection().elements();
        boolean isTheFirstElement = true;
        if (jrf.getSelectedCrosstabEditorPanel() == null) {
            Band last_band = null;
            int y_location = 0;
            while (e.hasMoreElements()) {
                ReportElement re = (ReportElement)e.nextElement();
                // 1. Set element properties...
                // For each panel, search
                if (last_band == null || last_band != re.getBand()) {
                    y_location = this.jrf.getReport().getBandYLocation(re.getBand());
                    last_band = re.getBand();
                }
                
                this.setElementNumber(isTheFirstElement, re.getWidth(), jNumberFieldWidth);
                this.setElementNumber(isTheFirstElement, re.getHeight(), jNumberFieldHeight);
                this.setElementNumber(isTheFirstElement, re.getPosition().x - 10 - this.jrf.getReport().getLeftMargin(), jNumberFieldLeft);
                this.setElementNumber(isTheFirstElement, re.getPosition().y - y_location - 10, jNumberFieldTop);
                
                this.setCheckBox(isTheFirstElement, re.getTransparent().equals("Transparent"),jCheckBoxTransparent);
                isTheFirstElement=false;
            }
        } else {
            CrosstabCell cell = null;
            while (e.hasMoreElements()) {
                ReportElement re = (ReportElement)e.nextElement();
                cell = re.getCell();
                if (cell != null) {
                    this.setElementNumber(isTheFirstElement, re.getPosition().x - 10 - cell.getLeft(), jNumberFieldLeft);
                    this.setElementNumber(isTheFirstElement, re.getPosition().y - 10 - cell.getTop(), jNumberFieldTop);
                    isTheFirstElement=false;
                }
            }
        }
        
        if (!(""+this.jComboBoxBand.getSelectedItem()).equals("")) {
            this.jComboBoxBand.removeItem("");
        }
    }//GEN-LAST:event_jComboBoxBandActionPerformed
    
    private void jNumberFieldHeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldHeightActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            if (element instanceof BreakReportElement) continue;
            element.trasform( new Point(0, (int)jNumberFieldHeight.getValue()- element.getHeight() ), TransformationType.TRANSFORMATION_RESIZE_S);
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
        }
        repaintEditor();
    }//GEN-LAST:event_jNumberFieldHeightActionPerformed
    
    private void jNumberFieldWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldWidthActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            if (element instanceof BreakReportElement) continue;
            element.trasform( new Point( (int)jNumberFieldWidth.getValue()- element.getWidth(),0 ), TransformationType.TRANSFORMATION_RESIZE_E);
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
        }
        
        repaintEditor();
    }//GEN-LAST:event_jNumberFieldWidthActionPerformed
    
    private void jButtonForegroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonForegroundActionPerformed
        java.awt.Color color = HexColorChooserPanel.showDialog( this, "Select foreground color...",
                this.jButtonForeground.getBackground());
        if (color  != null) {
            this.jButtonForeground.setBackground(color);
            
            if (jrf == null || getElementSelection().size()==0) return;
            // Set the new value for all selected elements...
            Enumeration e = getElementSelection().elements();
            while (e.hasMoreElements()) {
                ReportElement element = (ReportElement)e.nextElement();
                element.setFgcolor(color);
            }
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            repaintEditor();
        }
    }//GEN-LAST:event_jButtonForegroundActionPerformed
    
    private void jButtonBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackgroundActionPerformed
        
        java.awt.Color color = HexColorChooserPanel.showDialog( null, "Select background color...",
                this.jButtonBackground.getBackground());
        
        if (color  != null) {
            this.jButtonBackground.setBackground(color);
            if (jrf == null || getElementSelection().size()==0) return;
            // Set the new value for all selected elements...
            Enumeration e = getElementSelection().elements();
            while (e.hasMoreElements()) {
                ReportElement element = (ReportElement)e.nextElement();
                element.setBgcolor( color );
                
                // Adaptation by Robert Lamping
                // RFE: 704101 [B]
                // setting a background color, resets transparency to false.
                if (!element.getTransparent().equals("Opaque")) {
                    element.setTransparent("Opaque");
                }
                // end of adaptation
            }
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            // Adaptation by Robert Lamping
            // RFE: 704101 [B]
            // setting a background color, resets transparency to false.
            setInit(true);
            this.jCheckBoxTransparent.setSelected( false );
            setInit(false);
            jrf.getMainFrame().logOnConsole("Transparency switched off! \n");
            // End of adaptation
            
            repaintEditor();
        }
        
        
        
        
        
    }//GEN-LAST:event_jButtonBackgroundActionPerformed
        
    private void jComboBoxReportFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxReportFontActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxReportFont.getSelectedItem() == null) return;
        
        
        // Set the new value for all selected elements...
        // Looking for the selected report font...
        Enumeration fonts = jrf.getReport().getFonts().elements();
        IReportFont font = null;
        while (fonts.hasMoreElements()) {
            font = (IReportFont)fonts.nextElement();
            if (font != null && font.getReportFont().equals(""+ jComboBoxReportFont.getSelectedItem() ) ) {
                break;
            } else
                font = null;
        }
        
        String reportFontName = "";
        if (font==null)
            reportFontName = "";
        else
            reportFontName = font.getReportFont();
        
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setReportFont(reportFontName);
            if (font != null) {
                element.setFontName( font.getFontName());
                element.setBold( font.isBold());
                element.setItalic( font.isItalic());
                element.setUnderline( font.isUnderline());
                element.setStrikeTrought( font.isStrikeTrought());
                element.setPDFFontName( font.getPDFFontName());
                element.setPdfEmbedded( font.isPdfEmbedded());
                element.setPdfEncoding( font.getPdfEncoding());
                element.setTTFFont( font.getTTFFont());
                element.setFontSize( font.getFontSize());
            }
        }
        
        this.updateSelection();
        this.jrf.getReportPanel().repaint();
        
        this.jComboBoxFontName.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxReportFontActionPerformed
    
    private void jComboBoxLinkTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLinkTypeActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0 || jComboBoxLinkType.getSelectedItem() == null || (jComboBoxLinkType.getSelectedItem()+"").equals("")) return;
        Enumeration e = getElementSelection().elements();
        
        jTabbedPane2.removeAll();
        
        if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("None")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                element.getLinkParameters().clear();
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
                //this.jPanelAnchor.setVisible(false);
                //this.jPanelPage.setVisible(false);
                //this.jPanelReference.setVisible(false);
            }
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("Reference")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);
                
                
                //this.jPanelAnchor.setVisible(false);
                //this.jPanelPage.setVisible(false);
                
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"), this.jPanelReference);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalAnchor")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
                
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalPage")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
                
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                
                
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
            
        }
        
        else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemoteAnchor")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);
                
                
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
        } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemotePage")) {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                element.setHyperlinkReferenceExpression("");
                element.setHyperlinkAnchorExpression("");
                element.setHyperlinkPageExpression("");
                this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);
                
                
                //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
        } else {
            // Set to blank all link fields...
            while (e.hasMoreElements()) {
                HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
                element.setHyperlinkType( ""+jComboBoxLinkType.getSelectedItem());
                //element.setHyperlinkReferenceExpression("");
                //element.setHyperlinkAnchorExpression("");
                //element.setHyperlinkPageExpression("");
                //this.jRTextExpressionAreaAnchor.setText("");
                this.jRTextExpressionAreaAnchor.setEnabled(true);
                this.jLabelAnchor.setEnabled(true);
                //this.jRTextExpressionAreaPage.setText("");
                this.jRTextExpressionAreaPage.setEnabled(true);
                this.jLabelPage.setEnabled(true);
                //this.jRTextExpressionAreaReference.setText("");
                this.jRTextExpressionAreaReference.setEnabled(true);
                this.jLabelReference.setEnabled(true);
                
                
            }
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
            if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"),this.jPanelLinkParams);
            jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
        }
        
        jComboBoxLinkType.removeItem("");
    }//GEN-LAST:event_jComboBoxLinkTypeActionPerformed
    
    private void jButtonRemParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        jTableSubreportParameters.getSelectedRows();
        int[]  rows= jTableSubreportParameters.getSelectedRows();
        for (int i=rows.length-1; i>=0; --i) {
            element.getSubreportParameters().removeElement( jTableSubreportParameters.getValueAt( rows[i], 0) );
            //this.jTableParameters.removeRowSelectionInterval( rows[i],rows[i]);
        }
        
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportParameters.getModel();
        dtm.setRowCount(0);
        
        java.util.Enumeration enum_parameters = element.getSubreportParameters().elements();
        while (enum_parameters.hasMoreElements()) {
            it.businesslogic.ireport.JRSubreportParameter parameter = (it.businesslogic.ireport.JRSubreportParameter)enum_parameters.nextElement();
            Vector row = new Vector();
            row.addElement(parameter);
            row.addElement(parameter.getExpression());
            dtm.addRow(row);
        }
    }//GEN-LAST:event_jButtonRemParameterActionPerformed
    
    private void jButtonModParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModParameterActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        modifySubreportParameter(JRSubreportParameterDialog.COMPONENT_NONE);
    }//GEN-LAST:event_jButtonModParameterActionPerformed
    
    private void jButtonAddParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddParameterActionPerformed
        
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            it.businesslogic.ireport.JRSubreportParameter parameter = jrpd.getParameter();
            element.getSubreportParameters().addElement( parameter );
            javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportParameters.getModel();
            dtm.addRow(new Object[]{parameter, parameter.getExpression()});
        }
    }//GEN-LAST:event_jButtonAddParameterActionPerformed
    
    private void jComboBoxSubreportExpressionClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubreportExpressionClassActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxSubreportExpressionClass.getSelectedItem() == null || (jComboBoxSubreportExpressionClass.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            SubReportElement element = (SubReportElement)e.nextElement();
            element.setSubreportExpressionClass(""+ jComboBoxSubreportExpressionClass.getSelectedItem());
        }
        
        this.jComboBoxSubreportExpressionClass.removeItem("");
    }//GEN-LAST:event_jComboBoxSubreportExpressionClassActionPerformed
    
    private void jCheckBoxSubreportCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSubreportCacheActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            SubReportElement element = (SubReportElement)e.nextElement();
            element.setIsUsingCache(jCheckBoxSubreportCache.isSelected());
        }
    }//GEN-LAST:event_jCheckBoxSubreportCacheActionPerformed
    
    private void jComboBoxSubreportConnectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSubreportConnectionTypeActionPerformed
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        
        if (jComboBoxSubreportConnectionType.getSelectedIndex() == 0) {
            element.setUseConnection(false);
            element.setConnectionExpression("");
            element.setDataSourceExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
        } else if (jComboBoxSubreportConnectionType.getSelectedIndex() == 1) {
            element.setUseConnection(true);
            element.setDataSourceExpression("");
            element.setConnectionExpression("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{REPORT_CONNECTION}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        } else if (jComboBoxSubreportConnectionType.getSelectedIndex() == 2) {
            element.setUseConnection(false);
            element.setDataSourceExpression("$P{MyDatasource}");
            element.setConnectionExpression("");
            jRTextExpressionAreaTextConnectionExpression.setText("$P{MyDatasource}");
            jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
            jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
        }
        
    }//GEN-LAST:event_jComboBoxSubreportConnectionTypeActionPerformed
    
    private void jComboBoxPatternInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jComboBoxPatternInputMethodTextChanged
        
        
        
    }//GEN-LAST:event_jComboBoxPatternInputMethodTextChanged
    
    private void jComboBoxPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPatternActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null ||
                ((getElementSelection().size()==0 || jComboBoxPdfEncoding.getSelectedItem() == null || (jComboBoxPdfEncoding.getSelectedItem()+"").equals("") )  && ((javax.swing.JTextField) jComboBoxPattern.getEditor().getEditorComponent()).getText().trim().length() == 0)) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        
        Object pattern = jComboBoxPattern.getSelectedItem();
        if (pattern == null || pattern.equals("")) {
            pattern = ((javax.swing.JTextField) jComboBoxPattern.getEditor().getEditorComponent()).getText().trim();
        } else if (pattern instanceof PdfEncoding) {
            pattern = ""+ ((PdfEncoding)pattern).getEncoding();
            jComboBoxPattern.setSelectedItem(pattern);
        } else
            pattern = ""+ pattern+"";
        
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setPattern(""+ pattern+"");
        }
        fireReportElementsChangedEvent("pattern", ""+ pattern);
    }//GEN-LAST:event_jComboBoxPatternActionPerformed
    
    private void jCheckBoxBlankWhenNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBlankWhenNullActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setBlankWhenNull(jCheckBoxBlankWhenNull.isSelected());
        }
        fireReportElementsChangedEvent("blankWhenNull", ""+ jCheckBoxStretchWithOverflow.isSelected());
    }//GEN-LAST:event_jCheckBoxBlankWhenNullActionPerformed
    
    private void jCheckBoxStretchWithOverflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStretchWithOverflowActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setStretchWithOverflow(jCheckBoxStretchWithOverflow.isSelected());
        }
        fireReportElementsChangedEvent("stretchWithOverflow", ""+ jCheckBoxStretchWithOverflow.isSelected());
    }//GEN-LAST:event_jCheckBoxStretchWithOverflowActionPerformed
    
    private void jComboBoxTextFieldGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTextFieldGroupActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxTextFieldGroup.getSelectedItem() == null || (jComboBoxTextFieldGroup.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setGroup(""+ jComboBoxTextFieldGroup.getSelectedItem());
        }
        fireReportElementsChangedEvent("textfieldEvaluationGroup", ""+ jComboBoxTextFieldGroup.getSelectedItem());
        this.jComboBoxTextFieldGroup.removeItem("");
    }//GEN-LAST:event_jComboBoxTextFieldGroupActionPerformed
    
    private void jComboBoxTextFieldEvaluationTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTextFieldEvaluationTimeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxTextFieldEvaluationTime) == null || (Misc.getComboboxSelectedValue(jComboBoxTextFieldEvaluationTime)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setEvaluationTime(""+ Misc.getComboboxSelectedValue(jComboBoxTextFieldEvaluationTime));
            if (element.getEvaluationTime().equals("Group")) {
                jComboBoxTextFieldGroup.setEnabled(true);
            } else {
                element.setGroup("");
                jComboBoxTextFieldGroup.setEnabled(false);
            }
        }
        fireReportElementsChangedEvent("textfieldEvaluationTime", ""+ Misc.getComboboxSelectedValue(jComboBoxTextFieldEvaluationTime));
        this.jComboBoxTextFieldEvaluationTime.removeItem("");
    }//GEN-LAST:event_jComboBoxTextFieldEvaluationTimeActionPerformed
    
    private void jComboBoxTextFieldExpressionClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTextFieldExpressionClassActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxTextFieldExpressionClass.getSelectedItem() == null || (jComboBoxTextFieldExpressionClass.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setClassExpression(""+ jComboBoxTextFieldExpressionClass.getSelectedItem());
        }
        fireReportElementsChangedEvent("textfieldExpressionClass", ""+ jComboBoxTextFieldExpressionClass.getSelectedItem());
        this.jComboBoxTextFieldExpressionClass.removeItem("");
    }//GEN-LAST:event_jComboBoxTextFieldExpressionClassActionPerformed
    
    private void jCheckBoxPDFEmbeddedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPDFEmbeddedActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setPdfEmbedded(jCheckBoxPDFEmbedded.isSelected());
        }
        fireReportElementsChangedEvent("pdfEmbedded", ""+jCheckBoxPDFEmbedded.isSelected());
    }//GEN-LAST:event_jCheckBoxPDFEmbeddedActionPerformed
    
    private void jComboBoxPdfEncodingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPdfEncodingActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxPdfEncoding.getSelectedItem() == null || (jComboBoxPdfEncoding.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        String newValue = null;
        Object encoding = jComboBoxPdfEncoding.getSelectedItem();
        if (encoding instanceof PdfEncoding) {
            newValue = ""+ ((PdfEncoding)encoding).getEncoding();
        } else {
            newValue = encoding+"";
        }
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setPdfEncoding( newValue);
        }
        fireReportElementsChangedEvent("pdfEncoding", newValue);
    }//GEN-LAST:event_jComboBoxPdfEncodingActionPerformed
    
    private void jComboBoxVAlignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVAlignActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxVAlign) == null || (Misc.getComboboxSelectedValue(jComboBoxVAlign)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setVerticalAlign(""+ Misc.getComboboxSelectedValue(jComboBoxVAlign));
        }
        fireReportElementsChangedEvent("vAlign", ""+Misc.getComboboxSelectedValue(jComboBoxVAlign));
        this.jComboBoxVAlign.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxVAlignActionPerformed
    
    private void jComboBoxHAlignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHAlignActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxHAlign) == null || (Misc.getComboboxSelectedValue(jComboBoxHAlign)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setAlign(""+ Misc.getComboboxSelectedValue(jComboBoxHAlign));
        }
        fireReportElementsChangedEvent("hAlign", ""+Misc.getComboboxSelectedValue(jComboBoxHAlign));
        this.jComboBoxHAlign.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxHAlignActionPerformed
    
    private void jComboBoxLineSpacingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLineSpacingActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxLineSpacing) == null || (Misc.getComboboxSelectedValue(jComboBoxLineSpacing)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setLineSpacing(""+ Misc.getComboboxSelectedValue(jComboBoxLineSpacing));
        }
        
        fireReportElementsChangedEvent("lineSpacing", ""+Misc.getComboboxSelectedValue(jComboBoxLineSpacing));
        this.jComboBoxLineSpacing.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxLineSpacingActionPerformed
    
    private void jCheckBoxStrokeTroughActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStrokeTroughActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setStrikeTrought(jCheckBoxStrokeTrough.isSelected());
            element.setFont(null);
            /* Adjust PDF font...*/
        }
        fireReportElementsChangedEvent("strikethrough", ""+jCheckBoxStrokeTrough.isSelected());
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxStrokeTroughActionPerformed
    
    private void jCheckBoxUnderlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUnderlineActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setUnderline(jCheckBoxUnderline.isSelected());
            element.setFont(null);
            /* Adjust PDF font...*/
        }
        fireReportElementsChangedEvent("underline", ""+jCheckBoxUnderline.isSelected());
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxUnderlineActionPerformed
    
    private void jCheckBoxItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxItalicActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        boolean adjusted = false;
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setItalic(jCheckBoxItalic.isSelected());
            element.setFont(null);
            /* Adjust PDF font...*/
            if (IReportFont.adjustPdfFontName(element)) {
                adjusted = true;
            }
        }
        if (adjusted) pdfFontUpdated();
        fireReportElementsChangedEvent(null, null);
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxItalicActionPerformed
    
    private void jCheckBoxBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBoldActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        boolean adjusted = false;
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setBold(jCheckBoxBold.isSelected());
            element.setFont(null);
            /* Adjust PDF font...*/
            
            if (IReportFont.adjustPdfFontName(element)) {
                adjusted = true;
            }
        }
        if (adjusted) pdfFontUpdated();
        fireReportElementsChangedEvent(null, null);
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxBoldActionPerformed
    
    public void pdfFontUpdated() {
        if (this.isInit()) return;
        if (jrf == null || getElementSelection().size()==0) return;
        
        boolean samePDFFontName = true;
        boolean sameTTFFont = true;
        
        boolean isTheFirstElement = true;
        
        Enumeration e = getElementSelection().elements();
        boolean adjusted = false;
        while (e.hasMoreElements()) {
            TextReportElement tre = (TextReportElement)e.nextElement();
            
            if (samePDFFontName) {
                if (tre.getPDFFontName().toUpperCase().endsWith(".TTF") || tre.getPDFFontName().toUpperCase().indexOf(".TTC,") >= 0)
                    samePDFFontName = this.setComboBoxText(isTheFirstElement, "External TTF font..." , jComboBoxPDFFontName );
                else
                    samePDFFontName = this.setComboBoxText(isTheFirstElement, tre.getPDFFontName() , jComboBoxPDFFontName );
            }
            
            
            
            isTheFirstElement = false;
        }
        
        
    }
    
    private void jNumberComboBoxSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeItemStateChanged
        jNumberComboBoxSizeActionPerformed(new java.awt.event.ActionEvent(jNumberComboBoxSize,0,""));
    }//GEN-LAST:event_jNumberComboBoxSizeItemStateChanged
    
    private void jTextAreaTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextAreaTextInputMethodTextChanged
        
    }//GEN-LAST:event_jTextAreaTextInputMethodTextChanged
    
    private void jComboBoxFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFontNameActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxFontName.getSelectedItem() == null || (jComboBoxFontName.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setFontName(""+ jComboBoxFontName.getSelectedItem());
        }
        fireReportElementsChangedEvent("fontName", ""+ jComboBoxFontName.getSelectedItem());
        this.jComboBoxFontName.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxFontNameActionPerformed
    
    private void jNumberComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberComboBoxSizeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jNumberComboBoxSize.getValue() ==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setFontSize((int)jNumberComboBoxSize.getValue());
        }
        fireReportElementsChangedEvent("fontSize", ""+(int)jNumberComboBoxSize.getValue());
        repaintEditor();
    }//GEN-LAST:event_jNumberComboBoxSizeActionPerformed
    
    private void jComboBoxPDFFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPDFFontNameActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxPDFFontName.getSelectedItem() == null || (jComboBoxPDFFontName.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Object obj = jComboBoxPDFFontName.getSelectedItem();
        String fontName = ""+ obj;
        if (obj instanceof Tag) {
            fontName = ""+((Tag)obj).getValue();
        } else {
            fontName = ""+obj;
        }
        
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.getIReportFont().setPropertyValue( IReportFont.PDF_FONT_NAME, (fontName.length() > 0) ? fontName : null);
        }
        fireReportElementsChangedEvent(null, null);
        //repaintEditor();
    }//GEN-LAST:event_jComboBoxPDFFontNameActionPerformed
    
    private void jButtonFindImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindImageActionPerformed
        String fileName = "";
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( this.jrf.getMainFrame().getCurrentDirectory()  );
        jfc.setDialogTitle("Load XML jasperreports file....");
        jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File file) {
                String filename = file.getName();
                return (filename.endsWith(".jpg") ||
                        filename.endsWith(".jpeg") ||
                        filename.endsWith(".gif") ||
                        file.isDirectory()) ;
            }
            public String getDescription() {
                return "Image *.gif|*.jpg";
            }
        });
        
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( null) == javax.swing.JOptionPane.OK_OPTION) {
            this.jRTextExpressionAreaImageExpression.setText("\""+ Misc.string_replace("\\\\","\\",jfc.getSelectedFile().getPath() +"\""));
            this.jrf.getMainFrame().setCurrentDirectory(jfc.getSelectedFile(), true);
        }
    }//GEN-LAST:event_jButtonFindImageActionPerformed
    
    private void jCheckBoxImageCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxImageCacheActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setIsUsingCache( jCheckBoxImageCache.isSelected() );
        }
    }//GEN-LAST:event_jCheckBoxImageCacheActionPerformed
    
    private void jComboBoxImageGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxImageGroupActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxImageGroup.getSelectedItem() == null || (jComboBoxImageGroup.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setEvaluationGroup(""+ jComboBoxImageGroup.getSelectedItem());
        }
        
        this.jComboBoxImageGroup.removeItem("");
    }//GEN-LAST:event_jComboBoxImageGroupActionPerformed
    
    private void jComboBoxEvaluationTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEvaluationTimeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxEvaluationTime) == null || (Misc.getComboboxSelectedValue(jComboBoxEvaluationTime)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setEvaluationTime(""+ Misc.getComboboxSelectedValue(jComboBoxEvaluationTime));
        }
        
        this.jComboBoxEvaluationTime.removeItem("");
    }//GEN-LAST:event_jComboBoxEvaluationTimeActionPerformed
    
    private void jComboBoxImageExpressionClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxImageExpressionClassActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || jComboBoxImageExpressionClass.getSelectedItem() == null || (jComboBoxImageExpressionClass.getSelectedItem()+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setImageClass(""+ jComboBoxImageExpressionClass.getSelectedItem());
        }
        
        this.jComboBoxImageExpressionClass.removeItem("");
    }//GEN-LAST:event_jComboBoxImageExpressionClassActionPerformed
    
    private void jComboBoxHorizontalAlignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHorizontalAlignmentActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment) == null || (Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setHorizontalAlignment(""+ Misc.getComboboxSelectedValue(jComboBoxHorizontalAlignment));
        }
        
        this.jComboBoxHorizontalAlignment.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxHorizontalAlignmentActionPerformed
    
    private void jComboBoxVerticalAlignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVerticalAlignmentActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment) == null || (Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setVerticalAlignment(""+ Misc.getComboboxSelectedValue(jComboBoxVerticalAlignment));
        }
        
        this.jComboBoxVerticalAlignment.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxVerticalAlignmentActionPerformed
    
    private void jComboBoxScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxScaleActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue( jComboBoxScale ) == null || (Misc.getComboboxSelectedValue( jComboBoxScale )+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setScaleImage(""+ Misc.getComboboxSelectedValue( jComboBoxScale ));
        }
        
        this.jComboBoxScale.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxScaleActionPerformed
    
    private void jComboBoxLineDirectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLineDirectionActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxLineDirection) == null || (Misc.getComboboxSelectedValue(jComboBoxLineDirection)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            LineReportElement element = (LineReportElement)e.nextElement();
            element.direction = ""+ Misc.getComboboxSelectedValue(jComboBoxLineDirection);
        }
        fireReportElementsChangedEvent("direction", ""+ Misc.getComboboxSelectedValue(jComboBoxLineDirection));
        this.jComboBoxLineDirection.removeItem("");
        repaintEditor();
    }//GEN-LAST:event_jComboBoxLineDirectionActionPerformed
    
    private void jNumberFieldRadiusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNumberFieldRadiusActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            RectangleReportElement element = (RectangleReportElement)e.nextElement();
            element.setRadius( (int)jNumberFieldRadius.getValue() );
        }
        fireReportElementsChangedEvent("radius", ""+ (int)jNumberFieldRadius.getValue());
        repaintEditor();
    }//GEN-LAST:event_jNumberFieldRadiusActionPerformed
    
    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneStateChanged
        if (this.isInit()) return;
        
        if (jTabbedPane.getSelectedComponent() == jPanelSheet)
        {
            sheetPanel.updateSelection( this.jrf );
        }
        
        if (jTabbedPane.getSelectedComponent() != null) {
            lastSelectedPanel = (javax.swing.JPanel)jTabbedPane.getSelectedComponent();
            //lastSelectedPaneName = jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex());
        }
    }//GEN-LAST:event_jTabbedPaneStateChanged
    
    private void jComboBoxFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFillActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxFill) == null || (Misc.getComboboxSelectedValue(jComboBoxFill)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            GraphicReportElement element = (GraphicReportElement)e.nextElement();
            element.setFill(""+ Misc.getComboboxSelectedValue(jComboBoxFill));
        }
        fireReportElementsChangedEvent("fill", ""+ Misc.getComboboxSelectedValue(jComboBoxFill));
        this.jComboBoxFill.removeItem("");
    }//GEN-LAST:event_jComboBoxFillActionPerformed
    
    private void jComboBoxStretchTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStretchTypeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxStretchType) == null || (Misc.getComboboxSelectedValue(jComboBoxStretchType)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setStretchType(""+ Misc.getComboboxSelectedValue(jComboBoxStretchType));
        }
        String value = ""+ Misc.getComboboxSelectedValue(jComboBoxStretchType);
        if (value.length() == 0) value = null;
        fireReportElementsChangedEvent("stretchType", value);
        this.jComboBoxStretchType.removeItem("");
    }//GEN-LAST:event_jComboBoxStretchTypeActionPerformed
    
    private void jComboBoxPenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPenActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue( jComboBoxPen ) == null || ( Misc.getComboboxSelectedValue( jComboBoxPen )+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            GraphicReportElement element = (GraphicReportElement)e.nextElement();
            element.setGraphicElementPen(""+  Misc.getComboboxSelectedValue( jComboBoxPen ));
        }
        fireReportElementsChangedEvent("pen", ""+  Misc.getComboboxSelectedValue( jComboBoxPen ));
        this.jComboBoxPen.removeItem("");
        repaintEditor();
        
    }//GEN-LAST:event_jComboBoxPenActionPerformed
    
    private void jComboBoxGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroupsActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.printWhenGroupChanges = ""+ jComboBoxGroups.getSelectedItem();
        }
        
        fireReportElementsChangedEvent("printWhenGroupChanges", ""+ jComboBoxGroups.getSelectedItem());
    }//GEN-LAST:event_jComboBoxGroupsActionPerformed
    
    private void jRTextExpressionAreaPrintWhenExpressionInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jRTextExpressionAreaPrintWhenExpressionInputMethodTextChanged
        
    }//GEN-LAST:event_jRTextExpressionAreaPrintWhenExpressionInputMethodTextChanged
    
    private void jComboBoxPositionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPositionTypeActionPerformed
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0 || Misc.getComboboxSelectedValue(jComboBoxPositionType) == null || (Misc.getComboboxSelectedValue(jComboBoxPositionType)+"").equals("")) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setPositionType(  ""+ Misc.getComboboxSelectedValue(jComboBoxPositionType) );
        }
        
        fireReportElementsChangedEvent("positionType", ""+ Misc.getComboboxSelectedValue(jComboBoxPositionType));
        this.jComboBoxPositionType.removeItem("");
    }//GEN-LAST:event_jComboBoxPositionTypeActionPerformed
    
    private void jCheckBoxPrintRepeatedValuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPrintRepeatedValuesActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setIsPrintRepeatedValues(jCheckBoxPrintRepeatedValues.isSelected() );
        }
        
        ReportElementChangedEvent rece = new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED);
        rece.setNewValue( new Boolean(jCheckBoxPrintRepeatedValues.isSelected()));
        rece.setPropertyChanged("printRepeatedValues");
        jrf.fireReportListenerReportElementsChanged(rece);
    }//GEN-LAST:event_jCheckBoxPrintRepeatedValuesActionPerformed
    
    private void jCheckBoxPrintWhenDetailOverflowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPrintWhenDetailOverflowsActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setIsPrintWhenDetailOverflows(jCheckBoxPrintWhenDetailOverflows.isSelected());
        }
        ReportElementChangedEvent rece = new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED);
        rece.setNewValue( new Boolean(jCheckBoxPrintWhenDetailOverflows.isSelected()));
        rece.setPropertyChanged("printWhenDetailOverflows");
        jrf.fireReportListenerReportElementsChanged(rece);
    }//GEN-LAST:event_jCheckBoxPrintWhenDetailOverflowsActionPerformed
    
    private void jCheckBoxPrintInFirstWholeBandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPrintInFirstWholeBandActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setIsPrintInFirstWholeBand(jCheckBoxPrintInFirstWholeBand.isSelected());
        }
        ReportElementChangedEvent rece = new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED);
        rece.setNewValue( new Boolean(jCheckBoxPrintInFirstWholeBand.isSelected()));
        rece.setPropertyChanged("printInFirstWholeBand");
        jrf.fireReportListenerReportElementsChanged(rece);
    }//GEN-LAST:event_jCheckBoxPrintInFirstWholeBandActionPerformed
    
    private void jCheckBoxRemoveLineWhenBlankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveLineWhenBlankActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setIsRemoveLineWhenBlank(jCheckBoxRemoveLineWhenBlank.isSelected());
        }
        ReportElementChangedEvent rece = new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED);
        rece.setNewValue( new Boolean(jCheckBoxRemoveLineWhenBlank.isSelected()));
        rece.setPropertyChanged("removeLineWhenBlank");
        jrf.fireReportListenerReportElementsChanged(rece);
        //repaintEditor();
    }//GEN-LAST:event_jCheckBoxRemoveLineWhenBlankActionPerformed
    
    private void jCheckBoxTransparentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTransparentActionPerformed
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.setTransparent(( (jCheckBoxTransparent.isSelected()) ? "Transparent" : "Opaque") );
        }
        ReportElementChangedEvent rece = new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED);
        rece.setNewValue( (jCheckBoxTransparent.isSelected()) ? "Transparent" : "Opaque");
        rece.setPropertyChanged("mode");
        jrf.fireReportListenerReportElementsChanged(rece);
        repaintEditor();
    }//GEN-LAST:event_jCheckBoxTransparentActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        if (mapImagePropertiesPanel1 != null) {
        	mapImagePropertiesPanel1.validateFields();
        }
    	setVisible(false);
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ElementPropertiesDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupCharts;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jBarcodeExpressionArea;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jBarcodeExpressionAreaAppIdentifier;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAddCrosstabParameter;
    private javax.swing.JButton jButtonAddLinkParameter;
    private javax.swing.JButton jButtonAddParameter;
    private javax.swing.JButton jButtonAddReturnValue;
    private javax.swing.JButton jButtonBackground;
    private javax.swing.JButton jButtonCopyParamsFromMaster;
    private javax.swing.JButton jButtonCreatePattern;
    private javax.swing.JButton jButtonFindImage;
    private javax.swing.JButton jButtonForeground;
    private javax.swing.JButton jButtonModCrosstabParameter;
    private javax.swing.JButton jButtonModLinkParameter;
    private javax.swing.JButton jButtonModParameter;
    private javax.swing.JButton jButtonModReturnValue;
    private javax.swing.JButton jButtonRemCrosstabParameter;
    private javax.swing.JButton jButtonRemLinkParameter;
    private javax.swing.JButton jButtonRemParameter;
    private javax.swing.JButton jButtonRemReturnValue;
    private javax.swing.JCheckBox jCheckBoxBarcodeCheckSum;
    private javax.swing.JCheckBox jCheckBoxBarcodeShowText;
    private javax.swing.JCheckBox jCheckBoxBlankWhenNull;
    private javax.swing.JCheckBox jCheckBoxBold;
    private javax.swing.JCheckBox jCheckBoxImageCache;
    private javax.swing.JCheckBox jCheckBoxImageIsLazy;
    private javax.swing.JCheckBox jCheckBoxItalic;
    private javax.swing.JCheckBox jCheckBoxPDFEmbedded;
    private javax.swing.JCheckBox jCheckBoxPrintInFirstWholeBand;
    private javax.swing.JCheckBox jCheckBoxPrintRepeatedValues;
    private javax.swing.JCheckBox jCheckBoxPrintWhenDetailOverflows;
    private javax.swing.JCheckBox jCheckBoxRemoveLineWhenBlank;
    private javax.swing.JCheckBox jCheckBoxRepeatColumnHeaders;
    private javax.swing.JCheckBox jCheckBoxRepeatRowHeaders;
    private javax.swing.JCheckBox jCheckBoxStretchWithOverflow;
    private javax.swing.JCheckBox jCheckBoxStrokeTrough;
    private javax.swing.JCheckBox jCheckBoxStyledText;
    private javax.swing.JCheckBox jCheckBoxSubreportCache;
    private javax.swing.JCheckBox jCheckBoxTransparent;
    private javax.swing.JCheckBox jCheckBoxUnderline;
    private javax.swing.JComboBox jComboBoxBand;
    private javax.swing.JComboBox jComboBoxBarcodeGroup;
    private javax.swing.JComboBox jComboBoxBarcodeType;
    private javax.swing.JComboBox jComboBoxEvaluationTime;
    private javax.swing.JComboBox jComboBoxEvaluationTime1;
    private javax.swing.JComboBox jComboBoxEvaluationTimeBarcode;
    private javax.swing.JComboBox jComboBoxFill;
    private javax.swing.JComboBox jComboBoxFontName;
    private javax.swing.JComboBox jComboBoxGroups;
    private javax.swing.JComboBox jComboBoxHAlign;
    private javax.swing.JComboBox jComboBoxHorizontalAlignment;
    private javax.swing.JComboBox jComboBoxHorizontalAlignment1;
    private javax.swing.JComboBox jComboBoxImageExpressionClass;
    private javax.swing.JComboBox jComboBoxImageGroup;
    private javax.swing.JComboBox jComboBoxImageGroup1;
    private javax.swing.JComboBox jComboBoxImageOnError;
    private javax.swing.JComboBox jComboBoxImageOnError1;
    private javax.swing.JComboBox jComboBoxLineDirection;
    private javax.swing.JComboBox jComboBoxLineSpacing;
    private javax.swing.JComboBox jComboBoxLinkTarget;
    private javax.swing.JComboBox jComboBoxLinkType;
    private javax.swing.JComboBox jComboBoxPDFFontName;
    private javax.swing.JComboBox jComboBoxPattern;
    private javax.swing.JComboBox jComboBoxPdfEncoding;
    private javax.swing.JComboBox jComboBoxPen;
    private javax.swing.JComboBox jComboBoxPositionType;
    private javax.swing.JComboBox jComboBoxReportFont;
    private javax.swing.JComboBox jComboBoxRotation;
    private javax.swing.JComboBox jComboBoxScale;
    private javax.swing.JComboBox jComboBoxScale1;
    private javax.swing.JComboBox jComboBoxStretchType;
    private javax.swing.JComboBox jComboBoxStyle;
    private javax.swing.JComboBox jComboBoxSubreportConnectionType;
    private javax.swing.JComboBox jComboBoxSubreportExpressionClass;
    private javax.swing.JComboBox jComboBoxTextFieldEvaluationTime;
    private javax.swing.JComboBox jComboBoxTextFieldExpressionClass;
    private javax.swing.JComboBox jComboBoxTextFieldGroup;
    private javax.swing.JComboBox jComboBoxVAlign;
    private javax.swing.JComboBox jComboBoxVerticalAlignment;
    private javax.swing.JComboBox jComboBoxVerticalAlignment1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAnchor;
    private javax.swing.JLabel jLabelPage;
    private javax.swing.JLabel jLabelReference;
    private javax.swing.JLabel jLabelStyle;
    private javax.swing.JLabel jLabelTarget;
    private javax.swing.JLabel jLabelTarget1;
    private javax.swing.JLabel jLabelTooltip;
    private it.businesslogic.ireport.gui.JNumberComboBox jNumberComboBoxSize;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldBCHeight;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldBCWidth;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldColumnBreakOffset;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldHeight;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldLeft;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldRadius;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldTop;
    private it.businesslogic.ireport.gui.JNumberField jNumberFieldWidth;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAnchor;
    private javax.swing.JPanel jPanelBarcode;
    private javax.swing.JPanel jPanelBorder;
    private javax.swing.JPanel jPanelChart;
    private javax.swing.JPanel jPanelCommon;
    private javax.swing.JPanel jPanelCrosstab;
    private javax.swing.JPanel jPanelFont;
    private javax.swing.JPanel jPanelGraphicselement;
    private javax.swing.JPanel jPanelHyperLink;
    private javax.swing.JPanel jPanelImage;
    private javax.swing.JPanel jPanelLine;
    private javax.swing.JPanel jPanelLinkParams;
    private javax.swing.JPanel jPanelMap;
    private javax.swing.JPanel jPanelPage;
    private javax.swing.JPanel jPanelRectangle;
    private javax.swing.JPanel jPanelReference;
    private javax.swing.JPanel jPanelSheet;
    private javax.swing.JPanel jPanelSubreport1;
    private javax.swing.JPanel jPanelSubreport2;
    private javax.swing.JPanel jPanelSubreportReturnValues;
    private javax.swing.JPanel jPanelText;
    private javax.swing.JPanel jPanelTextField;
    private javax.swing.JPanel jPanelTooltip;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaAnchor;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaAnchorName;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaCrosstabParametersMapExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaImageExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaPage;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaPrintWhenExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaReference;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaSubreportExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaSubreportMapExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTextConnectionExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTextFieldExpression;
    private it.businesslogic.ireport.gui.JRTextExpressionArea jRTextExpressionAreaTooltip;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinnerBookmarkLevel;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableCrosstabParameters;
    private javax.swing.JTable jTableLinkParameters;
    private javax.swing.JTable jTableSubreportParameters;
    private javax.swing.JTable jTableSubreportReturnValues;
    private javax.swing.JTextArea jTextAreaText;
    private javax.swing.JTextField jTextFieldName;
    private com.geopista.reports.gui.MapImagePropertiesPanel mapImagePropertiesPanel1;
    // End of variables declaration//GEN-END:variables
    
    private boolean init;
    
    /**
     *  Set the report form to work with...
     */
    public void setJReportFrame(JReportFrame jrf) {
        this.jrf = jrf;
        if (jrf == null) {
            setVisible(false);
            this.jTabbedPane.removeAll();
            this.jTabbedPane.updateUI();
            return;
        }
        
        this.setTitle(jrf.getReport().getName());
        updateBands();
        updateGroups();
        updateStyles();
        updateReportFonts();
        updateSelection();
        
        initExpressionArea(jRTextExpressionAreaSubreportExpression);
        initExpressionArea(jRTextExpressionAreaAnchor);
        initExpressionArea(jRTextExpressionAreaAnchorName);
        initExpressionArea(jRTextExpressionAreaImageExpression);
        initExpressionArea(jRTextExpressionAreaPage);
        initExpressionArea(jRTextExpressionAreaPrintWhenExpression);
        initExpressionArea(jRTextExpressionAreaReference);
        initExpressionArea(jRTextExpressionAreaSubreportExpression);
        initExpressionArea(jRTextExpressionAreaSubreportMapExpression);
        initExpressionArea(jRTextExpressionAreaTextConnectionExpression);
        initExpressionArea(jRTextExpressionAreaTextFieldExpression);
        initExpressionArea(jBarcodeExpressionArea);
    }
    
    
    /**
     * This method removes all references by the JRTextExpressionArea fields
     * to be sure that if needed a fresh context is used
     *
     */
    public void clearExpressionEditorContext() {
        jRTextExpressionAreaSubreportExpression.setSubDataset(null);
        jRTextExpressionAreaAnchor.setSubDataset(null);
        jRTextExpressionAreaAnchorName.setSubDataset(null);
        jRTextExpressionAreaImageExpression.setSubDataset(null);
        jRTextExpressionAreaPage.setSubDataset(null);
        jRTextExpressionAreaPrintWhenExpression.setSubDataset(null);
        jRTextExpressionAreaReference.setSubDataset(null);
        jRTextExpressionAreaSubreportExpression.setSubDataset(null);
        jRTextExpressionAreaSubreportMapExpression.setSubDataset(null);
        jRTextExpressionAreaTextConnectionExpression.setSubDataset(null);
        jRTextExpressionAreaTextFieldExpression.setSubDataset(null);
        jBarcodeExpressionArea.setSubDataset(null);
        
        jRTextExpressionAreaSubreportExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaAnchor.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaAnchorName.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaImageExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaPage.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaPrintWhenExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaReference.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaSubreportExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaSubreportMapExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaTextConnectionExpression.getCrosstabElements().removeAllElements();
        jRTextExpressionAreaTextFieldExpression.getCrosstabElements().removeAllElements();
        jBarcodeExpressionArea.getCrosstabElements().removeAllElements();
        
    }
    
    /**
     *  This method responde to a virtual event like "element selection changed"
     */
    public void updateSelection() {
        updateSelection(false);
        
    }
    
    public void updateSelection(boolean force) {
        
        
        if (!force && !this.isVisible()) return;
        
        
        
        if (jTabbedPane.getTabCount() > 0) {
            jTabbedPane.removeAll();
            this.jTabbedPane.updateUI();
        }
        if (jrf == null || getElementSelection().size() == 0) {
            return;
        } else {
            
            
            updateBands();
            updateGroups();
            updateStyles();
            updateReportFonts();
            
            this.setInit(true);
            
            
            Enumeration e = getElementSelection().elements();
            
            boolean isTheFirstElement = true;
            Band last_band = null;
            int y_location = 0;
            
            //______ COMMON ATTRIBUTES __________
            boolean sameTransparent = true;
            boolean sameWidth = true;
            boolean sameHeight = true;
            boolean sameTop = true;
            boolean sameLeft = true;
            boolean sameBand = true;
            boolean sameRemoveLineWhenBlank = true;
            boolean samePrintInFirstWholeBand = true;
            boolean samePrintWhenDetailOverflows = true;
            boolean samePrintRepeatedValues = true;
            boolean samePositionType = true;
            boolean sameBackgroud = true;
            boolean sameForecolor = true;
            boolean samePrintWhenGroupChanges = true;
            boolean samePrintWhenExpression = true;
            boolean sameKey = true;
            boolean sameStretchWithOverflow = true;
            boolean sameStyle = true;
            
            //______ GRAPHIC ELEMENT ATTRIBUTES __________
            boolean areAllGraphicsElements = true;
            boolean samePen = true;
            boolean sameStretchType = true;
            boolean sameFill = true;
            //______ RECTANGLE ELEMENT ATTRIBUTES __________
            boolean areAllRectangleElements = true;
            boolean sameRadius = true;
            //______ LINE ELEMENT ATTRIBUTES __________
            boolean areAllLineElements = true;
            boolean sameLineDirection = true;
            //______ IMAGE ELEMENT ATTRIBUTES __________
            boolean areAllImageElements = true;
            boolean sameScaleImage = true;
            boolean sameImageExpression = true;
            boolean sameImageGroup = true;
            boolean sameEvaluationTime = true;
            boolean sameVerticalAlignment = true;
            boolean sameHorizontalAlignment = true;
            boolean sameImageCache = true;
            boolean sameImageLazy = true;
            boolean sameImageExpressionClass = true;
            boolean sameImageOnError = true;
            //______ GEOPISTA MAP IMAGE ELEMENT ATRIBUTES _________
            boolean areAllMapImageElements = true;
            //______ TEXT ELEMENT ATTRIBUTES __________
            boolean areAllTextElements = true;
            boolean sameReportFont = true;
            boolean sameFontName = true;
            boolean samePDFFontName = true;
            boolean sameTTFFont = true;
            boolean sameFontSize = true;
            boolean sameIsBold = true;
            boolean sameIsItalic = true;
            boolean sameIsUnderline = true;
            boolean sameIsStrokeTrough = true;
            boolean sameTextVAlign = true;
            boolean sameTextHAlign = true;
            boolean sameTextLineSpacing = true;
            boolean sameIsPdfEmbedded = true;
            boolean samePdfEncoding = true;
            boolean sameRotation = true;
            boolean sameIsStyledTexdt = true;
            //______ STATIC ELEMENT ATTRIBUTES __________
            boolean areAllStaticTextElements = true;
            boolean sameText = true;
            //______ TEXTFIELD ELEMENT ATTRIBUTES __________
            boolean areAllTextFieldElements = true;
            boolean sameTextExpressionClass = true;
            boolean sameTextExpression = true;
            boolean sameTextEvaluationTime = true;
            boolean sameTextGroup = true;
            boolean sameTextStretchWithOverflow = true;
            boolean sameTextBlankWhenNull = true;
            boolean sameTextPattern = true;
            //______ HyperLinkable ELEMENT ATTRIBUTES __________
            boolean areAllHyperLinkableElements = true;
            boolean sameHyperLinkAnchorName = true;
            boolean sameHyperLinkType = true;
            boolean sameHyperLinkTarget = true;
            boolean sameHyperLinkAnchor = true;
            boolean sameHyperLinkPage = true;
            boolean sameHyperLinkReference = true;
            boolean sameBookmarkLevel = true;
            boolean sameTooltip = true;
            
            //______ BARCODE ELEMENT ATTRIBUTES __________
            boolean areAllBarcodeElements = true;
            boolean sameBarcodeExpression = true;
            boolean sameBarcodeGroup = true;
            boolean sameBarcodeEvaluationTime = true;
            boolean sameBarcodeShowText = true;
            boolean sameBarcodeChecksum = true;
            boolean sameBarcodeText = true;
            boolean sameBarcodeType = true;
            boolean sameBCVerticalAlignment = true;
            boolean sameBCHorizontalAlignment = true;
            boolean sameBCImageOnError = true;
            boolean sameBCScaleImage = true;
            boolean sameBCHeight = true;
            boolean sameBCWidth = true;
            boolean sameBCAppIdentifier = true;
            
            boolean isSubreportElement = false;
            boolean isCrosstabElement = false;
            boolean isChartElement = false;
            
            // ______ BOX _____________
            boolean areAllBorder = true;
            
            
            //boolean isBarcodeElement = false;
            
            if (!jComboBoxPositionType.getItemAt(0).equals(""))
                this.jComboBoxPositionType.insertItemAt("",0);
            if (jComboBoxStyle.getItemCount() == 0 ||  !jComboBoxStyle.getItemAt(0).equals(""))
                this.jComboBoxStyle.insertItemAt("",0);
            if (!jComboBoxPen.getItemAt(0).equals(""))
                this.jComboBoxPen.insertItemAt("",0);
            if (!jComboBoxStretchType.getItemAt(0).equals(""))
                this.jComboBoxStretchType.insertItemAt("",0);
            if (!jComboBoxFill.getItemAt(0).equals(""))
                this.jComboBoxFill.insertItemAt("",0);
            if (!jComboBoxLineDirection.getItemAt(0).equals(""))
                this.jComboBoxLineDirection.insertItemAt("",0);
            if (!jComboBoxScale.getItemAt(0).equals(""))
                this.jComboBoxScale.insertItemAt("",0);
            if (!jComboBoxImageExpressionClass.getItemAt(0).equals(""))
                this.jComboBoxImageExpressionClass.insertItemAt("",0);
            if (!jComboBoxVerticalAlignment.getItemAt(0).equals(""))
                this.jComboBoxVerticalAlignment.insertItemAt("",0);
            if (!jComboBoxHorizontalAlignment.getItemAt(0).equals(""))
                this.jComboBoxHorizontalAlignment.insertItemAt("",0);
            if (!jComboBoxEvaluationTime.getItemAt(0).equals(""))
                this.jComboBoxEvaluationTime.insertItemAt("",0);
            if (jComboBoxReportFont.getItemCount() == 0 || !jComboBoxReportFont.getItemAt(0).equals(""))
                this.jComboBoxReportFont.insertItemAt("",0);
            if (jComboBoxHAlign.getItemCount() == 0 || !jComboBoxHAlign.getItemAt(0).equals(""))
                this.jComboBoxHAlign.insertItemAt("",0);
            if (jComboBoxVAlign.getItemCount() == 0 || !jComboBoxVAlign.getItemAt(0).equals(""))
                this.jComboBoxVAlign.insertItemAt("",0);
            if (jComboBoxLineSpacing.getItemCount() == 0 || !jComboBoxLineSpacing.getItemAt(0).equals(""))
                this.jComboBoxLineSpacing.insertItemAt("",0);
            if (jComboBoxPdfEncoding.getItemCount() == 0 || !jComboBoxPdfEncoding.getItemAt(0).equals(""))
                this.jComboBoxPdfEncoding.insertItemAt("",0);
            if (jComboBoxTextFieldEvaluationTime.getItemCount() == 0 || !jComboBoxTextFieldEvaluationTime.getItemAt(0).equals(""))
                this.jComboBoxTextFieldEvaluationTime.insertItemAt("",0);
            if (jComboBoxTextFieldExpressionClass.getItemCount() == 0 || !jComboBoxTextFieldExpressionClass.getItemAt(0).equals(""))
                this.jComboBoxTextFieldExpressionClass.insertItemAt("",0);
            if (jComboBoxTextFieldGroup.getItemCount() == 0 || !jComboBoxTextFieldGroup.getItemAt(0).equals(""))
                this.jComboBoxTextFieldGroup.insertItemAt("",0);
            if (jComboBoxPattern.getItemCount() == 0 || !jComboBoxPattern.getItemAt(0).equals(""))
                this.jComboBoxPattern.insertItemAt("",0);
            if (jComboBoxLinkType.getItemCount() == 0 || !jComboBoxLinkType.getItemAt(0).equals(""))
                this.jComboBoxLinkType.insertItemAt("",0);
            if (!jComboBoxRotation.getItemAt(0).equals(""))
                this.jComboBoxRotation.insertItemAt("",0);
            if (jComboBoxEvaluationTimeBarcode.getItemCount() == 0 || !jComboBoxEvaluationTimeBarcode.getItemAt(0).equals(""))
                this.jComboBoxEvaluationTimeBarcode.insertItemAt("",0);
            if (jComboBoxBarcodeGroup.getItemCount() == 0 || !jComboBoxBarcodeGroup.getItemAt(0).equals(""))
                this.jComboBoxBarcodeGroup.insertItemAt("",0);
            if (jComboBoxBarcodeType.getItemCount() == 0 || !jComboBoxBarcodeType.getItemAt(0).equals(""))
                this.jComboBoxBarcodeType.insertItemAt("",0);
            if (jComboBoxLinkTarget.getItemCount() == 0 || !jComboBoxLinkTarget.getItemAt(0).equals(""))
                this.jComboBoxLinkTarget.insertItemAt("",0);
            if (jComboBoxImageOnError.getItemCount() == 0 || !jComboBoxImageOnError.getItemAt(0).equals(""))
                this.jComboBoxImageOnError.insertItemAt("",0);
            
            jComboBoxPattern.setSelectedIndex(0);
            ((javax.swing.JTextField) jComboBoxPattern.getEditor().getEditorComponent()).setText("");
            
            
            while (e.hasMoreElements()) {
                ReportElement re = (ReportElement)e.nextElement();
                // 1. Set element properties...
                // For each panel, search
                if (re.getBand() != null && (last_band == null || last_band != re.getBand())) {
                    y_location = this.jrf.getReport().getBandYLocation(re.getBand());
                    last_band = re.getBand();
                }
                
                int x_location = this.jrf.getReport().getLeftMargin();
                
                if (re.getBand() == null && re.getCell() != null) {
                    y_location = re.getCell().getTop();
                    x_location = re.getCell().getLeft();
                }
                
                
                
                if (sameStretchType) sameStretchType = this.setComboBoxTag(isTheFirstElement, re.getStretchType() , jComboBoxStretchType );
                if (sameKey)  sameKey = this.setTextField(isTheFirstElement, re.getKey(), jTextFieldName);
                if (sameWidth)  sameWidth = this.setElementNumber(isTheFirstElement, re.getWidth(), jNumberFieldWidth);
                if (sameHeight)  sameHeight = this.setElementNumber(isTheFirstElement, re.getHeight(), jNumberFieldHeight);
                
                int position_x = re.getPosition().x;
                if (re.getParentElement() != null) position_x -= re.getParentElement().getPosition().x;
                else position_x = position_x - 10 - x_location;
                
                if (sameLeft)  sameLeft = this.setElementNumber(isTheFirstElement,position_x, jNumberFieldLeft);
                
                int position_y = re.getPosition().y;
                if (re.getParentElement() != null) position_y -= re.getParentElement().getPosition().y;
                else position_y = position_y - y_location - 10;
                
                if (sameTop)  sameTop = this.setElementNumber(isTheFirstElement, position_y, jNumberFieldTop);
                
                if (sameTransparent) sameTransparent = this.setCheckBox(isTheFirstElement, re.getTransparent().equals("Transparent"), jCheckBoxTransparent);
                if (sameBand) sameBand = this.setComboBox(isTheFirstElement, (re.getCell() != null) ? (Object)re.getCell() : (Object)re.getBand(), jComboBoxBand );
                if (sameRemoveLineWhenBlank) sameRemoveLineWhenBlank = this.setCheckBox(isTheFirstElement, re.isIsRemoveLineWhenBlank(), jCheckBoxRemoveLineWhenBlank);
                if (samePrintInFirstWholeBand) samePrintInFirstWholeBand = this.setCheckBox(isTheFirstElement, re.isIsPrintInFirstWholeBand(), jCheckBoxPrintInFirstWholeBand);
                if (samePrintWhenDetailOverflows) samePrintWhenDetailOverflows = this.setCheckBox(isTheFirstElement, re.isIsPrintWhenDetailOverflows(), jCheckBoxPrintWhenDetailOverflows);
                if (samePrintRepeatedValues) samePrintRepeatedValues = this.setCheckBox(isTheFirstElement, re.isIsPrintRepeatedValues(), jCheckBoxPrintRepeatedValues);
                if (samePositionType) samePositionType = this.setComboBoxTag(isTheFirstElement, re.getPositionType(), jComboBoxPositionType );
                if (sameBackgroud) sameBackgroud= setElementColor(isTheFirstElement, re.getBgcolor(), jButtonBackground);
                if (sameForecolor) sameForecolor = setElementColor(isTheFirstElement, re.getFgcolor(), jButtonForeground);
                if (samePrintWhenExpression) samePrintWhenExpression = setTextArea(isTheFirstElement, re.printWhenExpression, jRTextExpressionAreaPrintWhenExpression);
                if (samePrintWhenGroupChanges) samePrintWhenGroupChanges = this.setComboBoxText(isTheFirstElement, Misc.nvl( re.printWhenGroupChanges,""), jComboBoxGroups);
                if (sameStyle) sameStyle = this.setComboBox(isTheFirstElement, re.getStyle(), jComboBoxStyle );
                // Tab Graphics Element
                if (areAllGraphicsElements && (re instanceof GraphicReportElement)) {
                    GraphicReportElement gre = (GraphicReportElement)re;
                    if (samePen) samePen = this.setComboBoxTag(isTheFirstElement, gre.getGraphicElementPen() , jComboBoxPen );
                    
                    if (sameFill) sameFill = this.setComboBoxTag(isTheFirstElement, gre.getFill() , jComboBoxFill );
                } else {
                    areAllGraphicsElements = false;
                }
                
                // Tab Rectangle Element
                if (areAllRectangleElements && (re instanceof RectangleReportElement)) {
                    RectangleReportElement rre = (RectangleReportElement)re;
                    if (sameRadius)  sameRadius = this.setElementNumber(isTheFirstElement, rre.getRadius(), jNumberFieldRadius);
                    
                } else {
                    areAllRectangleElements = false;
                }
                
                // Tab Line Element
                if (areAllLineElements && (re instanceof LineReportElement)) {
                    LineReportElement lre = (LineReportElement)re;
                    if (sameLineDirection) samePen = this.setComboBoxTag(isTheFirstElement, lre.direction , jComboBoxLineDirection );
                } else {
                    areAllLineElements = false;
                }
                
                // Tab Image Element
                if (areAllBarcodeElements && re instanceof BarcodeReportElement) {
                    BarcodeReportElement bre = (BarcodeReportElement)re;
                    if (sameBarcodeExpression) sameBarcodeExpression = setTextArea(isTheFirstElement, bre.getText(), jBarcodeExpressionArea);
                    if (sameBarcodeGroup) sameBarcodeGroup = this.setComboBoxText(isTheFirstElement, Misc.nvl( bre.getEvaluationGroup(),""), jComboBoxBarcodeGroup);
                    if (sameBarcodeType) sameBarcodeType = this.setTagComboBox(isTheFirstElement, bre.getType()+"", jComboBoxBarcodeType);
                    if (sameBarcodeEvaluationTime) sameBarcodeEvaluationTime = this.setComboBoxTag(isTheFirstElement, Misc.nvl( bre.getEvaluationTime(),""), jComboBoxEvaluationTimeBarcode);
                    if (sameBarcodeShowText) sameBarcodeShowText = this.setCheckBox(isTheFirstElement, bre.isShowText(), jCheckBoxBarcodeShowText );
                    if (sameBarcodeChecksum) sameBarcodeChecksum = this.setCheckBox(isTheFirstElement, bre.isCheckSum(), jCheckBoxBarcodeCheckSum );
                    if (sameBCScaleImage) sameBCScaleImage = this.setComboBoxTag(isTheFirstElement, bre.getScaleImage() , jComboBoxScale1 );
                    if (sameBCImageOnError) sameBCImageOnError = this.setComboBoxTag(isTheFirstElement, Misc.nvl( bre.getOnErrorType(),""), jComboBoxImageOnError1);
                    if (sameBCVerticalAlignment) sameBCVerticalAlignment = this.setComboBoxTag(isTheFirstElement, Misc.nvl( bre.getVerticalAlignment(),""), jComboBoxVerticalAlignment1);
                    if (sameBCHorizontalAlignment) sameBCHorizontalAlignment = this.setComboBoxTag(isTheFirstElement, Misc.nvl( bre.getHorizontalAlignment(),""), jComboBoxHorizontalAlignment1);
                    if (sameBCHeight)  sameBCHeight = this.setElementNumber(isTheFirstElement, bre.getImageHeight(), jNumberFieldBCHeight);
                    if (sameBCAppIdentifier)  sameBCAppIdentifier = this.setTextArea(isTheFirstElement, bre.getApplicationIdentifier(), jBarcodeExpressionAreaAppIdentifier);
                    if (sameBCWidth)  sameBCWidth = this.setElementNumber(isTheFirstElement, bre.getImageWidth(), jNumberFieldBCWidth);
                } else {
                    areAllBarcodeElements = false;
                }
                
                // Modificado para geopista
                if (areAllMapImageElements && (re instanceof MapImageReportElement)){
                	MapImageReportElement mire = (MapImageReportElement) re;
                	
                	//TODO CAMBIAR
                	/*com.geopista.reports.gui.MapImagePropertiesPanel mapImagePropertiesPanel2 = new com.geopista.reports.gui.MapImagePropertiesPanel();
                	mapImagePropertiesPanel2.loadMapImagePropertiesData(mire);
                	jPanelMap.add(mapImagePropertiesPanel2);
                	jPanelMap.remove(mapImagePropertiesPanel1);*/
                	mapImagePropertiesPanel1.loadMapImagePropertiesData(mire);
                }
                else {
                	areAllMapImageElements = false;
                }                
                
                if (areAllImageElements && (re instanceof ImageReportElement ) && !(re instanceof MapImageReportElement) && !(re instanceof ChartReportElement )  && !(re instanceof BarcodeReportElement )) {
                    ImageReportElement ire = (ImageReportElement)re;
                    if (sameScaleImage) sameScaleImage = this.setComboBoxTag(isTheFirstElement, ire.getScaleImage() , jComboBoxScale );
                    if (sameImageExpression) sameImageExpression = setTextArea(isTheFirstElement, ire.getImageExpression(), jRTextExpressionAreaImageExpression);
                    if (sameImageGroup) sameImageGroup = this.setComboBoxText(isTheFirstElement, Misc.nvl( ire.getEvaluationGroup(),""), jComboBoxImageGroup);
                    if (sameEvaluationTime) sameEvaluationTime = this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getEvaluationTime(),""), jComboBoxEvaluationTime);
                    if (sameVerticalAlignment) sameVerticalAlignment = this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getVerticalAlignment(),""), jComboBoxVerticalAlignment);
                    if (sameHorizontalAlignment) sameHorizontalAlignment = this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getHorizontalAlignment(),""), jComboBoxHorizontalAlignment);
                    if (sameImageCache) sameImageCache = this.setCheckBox(isTheFirstElement, ire.isIsUsingCache(), jCheckBoxImageCache );
                    if (sameImageLazy) sameImageLazy = this.setCheckBox(isTheFirstElement, ire.isIsLazy(), jCheckBoxImageIsLazy );
                    if (sameImageExpressionClass) sameImageExpressionClass = this.setComboBoxText(isTheFirstElement, Misc.nvl( ire.getImageClass(),""), jComboBoxImageExpressionClass);
                    if (sameImageOnError) sameImageOnError = this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getOnErrorType(),""), jComboBoxImageOnError);
                    
                } else {
                    areAllImageElements = false;
                }
                // Fin modificacion
                
                // Tab Font Element
                if (areAllTextElements && (re instanceof TextReportElement)) {
                    TextReportElement tre = (TextReportElement)re;
                    if (sameReportFont) sameReportFont = this.setComboBoxText(isTheFirstElement, tre.getReportFont() , jComboBoxReportFont );
                    if (sameFontName) sameFontName = setComboBoxText(isTheFirstElement, tre.getFontName() , jComboBoxFontName);
                    if (samePDFFontName) {
                        samePDFFontName = this.setMixedTagComboBox(isTheFirstElement, tre.getPDFFontName() , jComboBoxPDFFontName );
                    }
                    if (sameFontSize) sameFontSize = this.setElementComboNumber(isTheFirstElement, tre.getFontSize() , jNumberComboBoxSize );
                    if (sameIsBold) sameIsBold = this.setCheckBox(isTheFirstElement, tre.isBold(), jCheckBoxBold );
                    if (sameIsStyledTexdt) sameIsStyledTexdt = this.setCheckBox(isTheFirstElement, tre.isIsStyledText(), jCheckBoxStyledText );
                    if (sameIsItalic) sameIsItalic = this.setCheckBox(isTheFirstElement, tre.isItalic(), jCheckBoxItalic );
                    if (sameIsUnderline) sameIsUnderline = this.setCheckBox(isTheFirstElement, tre.isUnderline(), jCheckBoxUnderline );
                    if (sameIsStrokeTrough) sameIsStrokeTrough = this.setCheckBox(isTheFirstElement, tre.isStrikeTrought(),  jCheckBoxStrokeTrough );
                    if (sameTextVAlign) sameTextVAlign = this.setComboBoxTag(isTheFirstElement, tre.getVerticalAlign(), jComboBoxVAlign );
                    if (sameTextHAlign) sameTextHAlign = this.setComboBoxTag(isTheFirstElement, tre.getAlign() , jComboBoxHAlign );
                    if (sameTextLineSpacing) sameTextHAlign = this.setComboBoxTag(isTheFirstElement, tre.getLineSpacing() , jComboBoxLineSpacing );
                    if (sameIsPdfEmbedded) sameIsPdfEmbedded = this.setCheckBox(isTheFirstElement, tre.isPdfEmbedded(),  jCheckBoxPDFEmbedded );
                    if (samePdfEncoding) samePdfEncoding = this.setPdfEncodingComboBox(isTheFirstElement, tre.getPdfEncoding() , jComboBoxPdfEncoding );
                    
                    if (sameRotation) sameRotation = this.setComboBoxTag(isTheFirstElement, tre.getRotate(), jComboBoxRotation);
                    
                } else {
                    areAllTextElements = false;
                }
                
                // Tab StaticText Element
                if (areAllStaticTextElements && (re instanceof StaticTextReportElement)) {
                    TextReportElement tre = (TextReportElement)re;
                    if (sameText) sameText = this.setJTextArea(isTheFirstElement, tre.getText() , jTextAreaText );
                } else {
                    areAllStaticTextElements = false;
                }
                
                
                
                if (areAllBorder && (re instanceof BoxElement)) {
                    if (isTheFirstElement) {
                        boxPanel.setBox( ((BoxElement)re).getBox().cloneMe() );
                    }
                } else {
                    areAllBorder = false;
                }
                
                // Tab TextField Element
                if (areAllTextFieldElements && (re instanceof TextFieldReportElement)) {
                    TextFieldReportElement tre = (TextFieldReportElement)re;
                    if (sameTextExpression) sameText = this.setTextArea(isTheFirstElement, tre.getText() , jRTextExpressionAreaTextFieldExpression );
                    if (sameTextExpressionClass) sameTextExpressionClass  = this.setComboBoxText(isTheFirstElement, tre.getClassExpression(), jComboBoxTextFieldExpressionClass );
                    if (sameTextEvaluationTime) sameTextEvaluationTime = this.setComboBoxTag(isTheFirstElement, tre.getEvaluationTime(), jComboBoxTextFieldEvaluationTime );
                    if (sameTextGroup) sameTextGroup = this.setComboBoxText(isTheFirstElement, tre.getGroup(), jComboBoxTextFieldGroup );
                    if (sameTextStretchWithOverflow)  sameTextStretchWithOverflow = this.setCheckBox(isTheFirstElement, tre.isStretchWithOverflow(),  jCheckBoxStretchWithOverflow);
                    if (sameTextBlankWhenNull) sameTextBlankWhenNull = this.setCheckBox(isTheFirstElement, tre.isBlankWhenNull(),  jCheckBoxBlankWhenNull);
                    if (sameTextPattern) sameTextPattern = this.setWritableComboBoxText(isTheFirstElement, tre.getPattern(),  jComboBoxPattern);
                } else {
                    areAllTextFieldElements = false;
                }
                
                if (areAllHyperLinkableElements && (re instanceof HyperLinkableReportElement)) {
                    HyperLinkableReportElement hlre = (HyperLinkableReportElement)re;
                    if (sameHyperLinkAnchorName) sameHyperLinkAnchorName = setTextArea(isTheFirstElement, hlre.getAnchorNameExpression(), jRTextExpressionAreaAnchorName);
                    if (sameHyperLinkType) sameHyperLinkType = this.setComboBoxText(isTheFirstElement, hlre.getHyperlinkType(), jComboBoxLinkType);
                    if (sameHyperLinkTarget) sameHyperLinkTarget = this.setComboBoxText(isTheFirstElement, hlre.getHyperlinkTarget(), jComboBoxLinkTarget);
                    if (sameBookmarkLevel) sameBookmarkLevel = setSpinnerLevel(isTheFirstElement, hlre.getBookmarkLevel(), jSpinnerBookmarkLevel);
                    
                    if (sameHyperLinkType) {
                        if (sameHyperLinkAnchor) sameHyperLinkAnchor = setTextArea(isTheFirstElement, hlre.getHyperlinkAnchorExpression(), jRTextExpressionAreaAnchor);
                        if (sameHyperLinkPage) sameHyperLinkPage = setTextArea(isTheFirstElement, hlre.getHyperlinkPageExpression(), jRTextExpressionAreaPage);
                        if (sameHyperLinkReference) sameHyperLinkReference = setTextArea(isTheFirstElement, hlre.getHyperlinkReferenceExpression(), jRTextExpressionAreaReference);
                        if (sameTooltip) sameTooltip = setTextArea(isTheFirstElement, hlre.getTooltipExpression(), jRTextExpressionAreaTooltip);
                    } else {
                        jRTextExpressionAreaAnchor.setText("");
                        jRTextExpressionAreaAnchor.setText("");
                        jRTextExpressionAreaPage.setText("");
                        jRTextExpressionAreaReference.setText("");
                    }
                    
                } else {
                    areAllHyperLinkableElements = false;
                }
                
                if (isTheFirstElement && !e.hasMoreElements() && re instanceof SubReportElement) {
                    isSubreportElement = true;
                    SubReportElement sre = (SubReportElement)re;
                    this.setTextArea(isTheFirstElement, sre.getParametersMapExpression(), jRTextExpressionAreaSubreportMapExpression );
                    this.setComboBoxText(isTheFirstElement, sre.getSubreportExpressionClass(), jComboBoxSubreportExpressionClass);
                    this.setTextArea(isTheFirstElement, sre.getSubreportExpression(), jRTextExpressionAreaSubreportExpression);
                    if (!sre.isUseConnection() &&  Misc.nvl( sre.getDataSourceExpression(),"").trim().equals("")) {
                        this.jComboBoxSubreportConnectionType.setSelectedIndex(0);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(false);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.LIGHT_GRAY);
                        this.jRTextExpressionAreaTextConnectionExpression.setText("");
                    } else if (sre.isUseConnection()) {
                        this.jComboBoxSubreportConnectionType.setSelectedIndex(1);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( sre.getConnectionExpression());
                    } else {
                        this.jComboBoxSubreportConnectionType.setSelectedIndex(2);
                        this.jRTextExpressionAreaTextConnectionExpression.setEnabled(true);
                        this.jRTextExpressionAreaTextConnectionExpression.setBackground(Color.WHITE);
                        this.jRTextExpressionAreaTextConnectionExpression.setText( sre.getDataSourceExpression());
                    }
                    
                    jCheckBoxSubreportCache.setSelected( sre.isIsUsingCache());
                    
                    //Add parameters...
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableSubreportParameters.getModel();
                    dtm.setRowCount(0);
                    
                    java.util.Enumeration enum_parameters = sre.getSubreportParameters().elements();
                    while (enum_parameters.hasMoreElements()) {
                        it.businesslogic.ireport.JRSubreportParameter parameter = (it.businesslogic.ireport.JRSubreportParameter)enum_parameters.nextElement();
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement(parameter.getExpression());
                        dtm.addRow(row);
                    }
                    
                    dtm = (javax.swing.table.DefaultTableModel)jTableSubreportReturnValues.getModel();
                    dtm.setRowCount(0);
                    
                    java.util.Enumeration enum_returnValues = sre.getReturnValues().elements();
                    while (enum_returnValues.hasMoreElements()) {
                        it.businesslogic.ireport.JRSubreportReturnValue returnValue2 = (it.businesslogic.ireport.JRSubreportReturnValue)enum_returnValues.nextElement();
                        Vector row = new Vector();
                        row.addElement(returnValue2);
                        row.addElement(returnValue2.getToVariable());
                        dtm.addRow(row);
                    }
                    
                } else
                    isSubreportElement = false;
                
                if (isTheFirstElement && !e.hasMoreElements() && (re instanceof ChartReportElement || re instanceof ChartReportElement2)) {
                    isChartElement = true;
                    
                    if (re instanceof ChartReportElement) {
                        ImageReportElement ire = (ImageReportElement)re;
                        this.setComboBoxText(isTheFirstElement, Misc.nvl( ire.getEvaluationGroup(),""), jComboBoxImageGroup1);
                        this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getEvaluationTime(),""), jComboBoxEvaluationTime1);
                    } else {
                        ChartReportElement2 ire = (ChartReportElement2)re;
                        this.setComboBoxText(isTheFirstElement, Misc.nvl( ire.getEvaluationGroup(),""), jComboBoxImageGroup1);
                        this.setComboBoxTag(isTheFirstElement, Misc.nvl( ire.getEvaluationTime(),""), jComboBoxEvaluationTime1);
                    }
                } else
                    isChartElement = false;
                
                
                if (isTheFirstElement && !e.hasMoreElements() && re instanceof CrosstabReportElement) {
                    isCrosstabElement = true;
                    CrosstabReportElement cre = (CrosstabReportElement)re;
                    this.setTextArea(isTheFirstElement, cre.getParametersMapExpression(), jRTextExpressionAreaCrosstabParametersMapExpression );
                    
                    jCheckBoxRepeatColumnHeaders.setSelected( cre.isRepeatColumnHeaders());
                    jCheckBoxRepeatRowHeaders.setSelected( cre.isRepeatRowHeaders());
                    
                    this.setElementNumber(isTheFirstElement, cre.getColumnBreakOffset(), jNumberFieldColumnBreakOffset);
                    
                    //Add parameters...
                    javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel)jTableCrosstabParameters.getModel();
                    dtm.setRowCount(0);
                    
                    java.util.Enumeration enum_parameters = cre.getCrosstabParameters().elements();
                    while (enum_parameters.hasMoreElements()) {
                        it.businesslogic.ireport.crosstab.CrosstabParameter parameter = (it.businesslogic.ireport.crosstab.CrosstabParameter)enum_parameters.nextElement();
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement(parameter.getParameterValueExpression());
                        dtm.addRow(row);
                    }
                } else
                    isCrosstabElement = false;
                
                
                isTheFirstElement=false;
            }
            
            if (jComboBoxTextFieldEvaluationTime.getSelectedItem().equals("Group")) {
                jComboBoxTextFieldGroup.setEnabled(true);
            } else {
                jComboBoxTextFieldGroup.setEnabled(false);
            }
            
            // If not useful, remove blank combo entries...
            if (this.jComboBoxBand.getSelectedIndex() != 0) {
                this.jComboBoxBand.removeItemAt(0);
            }
            if (this.jComboBoxPositionType.getSelectedIndex() != 0) {
                this.jComboBoxPositionType.removeItemAt(0);
            }
            
            if (this.jComboBoxLineDirection.getSelectedIndex() != 0)  this.jComboBoxLineDirection.removeItemAt(0);
            if (this.jComboBoxPen.getSelectedIndex() != 0)  this.jComboBoxPen.removeItemAt(0);
            if (this.jComboBoxStretchType.getSelectedIndex() != 0)  this.jComboBoxStretchType.removeItemAt(0);
            if (this.jComboBoxFill.getSelectedIndex() != 0)  this.jComboBoxFill.removeItemAt(0);
            if (this.jComboBoxScale.getSelectedIndex() != 0)  this.jComboBoxScale.removeItemAt(0);
            if (this.jComboBoxVerticalAlignment.getSelectedIndex() != 0)  this.jComboBoxVerticalAlignment.removeItemAt(0);
            if (this.jComboBoxHorizontalAlignment.getSelectedIndex() != 0)  this.jComboBoxHorizontalAlignment.removeItemAt(0);
            if (this.jComboBoxImageExpressionClass.getSelectedIndex() != 0)  this.jComboBoxImageExpressionClass.removeItemAt(0);
            if (this.jComboBoxEvaluationTime.getSelectedIndex() != 0)  this.jComboBoxEvaluationTime.removeItemAt(0);
            //if (this.jComboBoxReportFont.getSelectedIndex() != 0)  this.jComboBoxReportFont.removeItemAt(0);
            if (this.jComboBoxHAlign.getSelectedIndex() != 0)  this.jComboBoxHAlign.removeItemAt(0);
            if (this.jComboBoxVAlign.getSelectedIndex() != 0)  this.jComboBoxVAlign.removeItemAt(0);
            if (this.jComboBoxLineSpacing.getSelectedIndex() != 0)  this.jComboBoxLineSpacing.removeItemAt(0);
            if (this.jComboBoxTextFieldEvaluationTime.getSelectedIndex() != 0)  this.jComboBoxTextFieldEvaluationTime.removeItemAt(0);
            if (this.jComboBoxTextFieldExpressionClass.getSelectedIndex() != 0)  this.jComboBoxTextFieldExpressionClass.removeItemAt(0);
            if (this.jComboBoxLinkTarget.getSelectedIndex() != 0)  this.jComboBoxLinkTarget.removeItemAt(0);
            //if (this.jComboBoxSubreportExpressionClass.getSelectedIndex() != 0)  this.jComboBoxTextFieldExpressionClass.removeItemAt(0);
            
            jTabbedPane2.removeAll();
            javax.swing.table.DefaultTableModel dtmLinkParams = (javax.swing.table.DefaultTableModel)jTableLinkParameters.getModel();
            dtmLinkParams.setRowCount(0);
            
            if (this.jComboBoxLinkType.getSelectedIndex() != 0) {
                this.jComboBoxLinkType.removeItemAt(0);
                
                if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("None")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(false);
                    this.jLabelAnchor.setEnabled(false);
                    this.jRTextExpressionAreaPage.setEnabled(false);
                    this.jLabelPage.setEnabled(false);
                    this.jRTextExpressionAreaReference.setEnabled(false);
                    this.jLabelReference.setEnabled(false);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    
                } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("Reference")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(false);
                    this.jLabelAnchor.setEnabled(false);
                    this.jRTextExpressionAreaPage.setEnabled(false);
                    this.jLabelPage.setEnabled(false);
                    this.jRTextExpressionAreaReference.setEnabled(true);
                    this.jLabelReference.setEnabled(true);
                    
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalAnchor")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(true);
                    this.jLabelAnchor.setEnabled(true);
                    this.jRTextExpressionAreaPage.setEnabled(false);
                    this.jLabelPage.setEnabled(false);
                    this.jRTextExpressionAreaReference.setEnabled(false);
                    this.jLabelReference.setEnabled(false);
                    
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("LocalPage")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(false);
                    this.jLabelAnchor.setEnabled(false);
                    this.jRTextExpressionAreaPage.setEnabled(true);
                    this.jLabelPage.setEnabled(true);
                    this.jRTextExpressionAreaReference.setEnabled(false);
                    this.jLabelReference.setEnabled(false);
                    
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                }
                
                else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemoteAnchor")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(true);
                    this.jLabelAnchor.setEnabled(true);
                    this.jRTextExpressionAreaPage.setEnabled(false);
                    this.jLabelPage.setEnabled(false);
                    this.jRTextExpressionAreaReference.setEnabled(true);
                    this.jLabelReference.setEnabled(true);
                    
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                } else if (((String)this.jComboBoxLinkType.getSelectedItem()).equals("RemotePage")) {
                    this.jRTextExpressionAreaAnchor.setEnabled(false);
                    this.jLabelAnchor.setEnabled(false);
                    this.jRTextExpressionAreaPage.setEnabled(true);
                    this.jLabelPage.setEnabled(true);
                    this.jRTextExpressionAreaReference.setEnabled(true);
                    this.jLabelReference.setEnabled(true);
                    
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    //jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                } else {
                    this.jRTextExpressionAreaAnchor.setEnabled(true);
                    this.jLabelAnchor.setEnabled(true);
                    this.jRTextExpressionAreaPage.setEnabled(true);
                    this.jLabelPage.setEnabled(true);
                    this.jRTextExpressionAreaReference.setEnabled(true);
                    this.jLabelReference.setEnabled(true);
                    
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Reference","Reference"),this.jPanelReference);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Anchor","Anchor"), this.jPanelAnchor);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.Page","Page"),this.jPanelPage);
                    if (getElementSelection().size()==1) jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.LinkParameters","Link parameters"), this.jPanelLinkParams);
                    jTabbedPane2.addTab(I18n.getString("sectionItemHyperlinkPanel.tab.tooltip","Tooltip"), this.jPanelTooltip);
                }
                
                if (getElementSelection().size()==1 && getElementSelection().get(0) instanceof HyperLinkableReportElement) {
                    //Add parameters...
                    HyperLinkableReportElement hre = (HyperLinkableReportElement)getElementSelection().get(0);
                    
                    java.util.Iterator enum_parameters = hre.getLinkParameters().iterator();
                    while (enum_parameters.hasNext()) {
                        it.businesslogic.ireport.JRLinkParameter parameter = (it.businesslogic.ireport.JRLinkParameter)enum_parameters.next();
                        Vector row = new Vector();
                        row.addElement(parameter);
                        row.addElement(parameter.getExpression());
                        dtmLinkParams.addRow(row);
                    }
                }
            } else {
                this.jRTextExpressionAreaAnchor.setEnabled(false);
                this.jLabelAnchor.setEnabled(false);
                this.jRTextExpressionAreaPage.setEnabled(false);
                this.jLabelPage.setEnabled(false);
                this.jRTextExpressionAreaReference.setEnabled(false);
                this.jLabelReference.setEnabled(false);
                
            }
            jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Common","Common"), jPanelCommon);
            if (areAllGraphicsElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.GraphicsElement","Graphics Element"), jPanelGraphicselement);
            if (areAllRectangleElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Rectangle","Rectangle"), jPanelRectangle);
            if (areAllLineElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Line","Line"), jPanelLine );
            // Modificacion para geopista
            if (areAllMapImageElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.MapImage","Mapa"), jPanelMap );
            // Fin de modificacion
            if (areAllImageElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Image","Image"), jPanelImage );
            if (areAllTextElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Font","Font"), jPanelFont);
            if (areAllStaticTextElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.StaticText","Static Text"), jPanelText);
            if (areAllTextFieldElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.TextField","Text Field"), jPanelTextField);
            if (areAllHyperLinkableElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.HyperLink","Hyper Link"), jPanelHyperLink);
            if (isSubreportElement) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Subreport","Subreport"), jPanelSubreport1);
            if (isSubreportElement) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Subreport2","Subreport (Other)"), jPanelSubreport2);
            if (isCrosstabElement) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Crosstab","Crosstab"), jPanelCrosstab);
            if (isChartElement) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Chart","Chart"), jPanelChart);
            if (areAllBarcodeElements) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Barcode","Barcode"), jPanelBarcode);
            if (areAllBorder) jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.Border","Border"), jPanelBorder);
            jTabbedPane.add(I18n.getString("elementPropertiesDialog.tab.All","All"), jPanelSheet);
            
            
            if (lastSelectedPanel != null)
                
                try {
                    jTabbedPane.setSelectedComponent( lastSelectedPanel);
                } catch (Exception ex) {
                    lastSelectedPanel = jPanelCommon;
                }
            jTabbedPane.updateUI();
            
            if (jTabbedPane.getSelectedComponent() == jPanelSheet)
            {
                sheetPanel.updateSelection( this.jrf );
            }
            
            this.setInit(false);
        }
    }
    
    /**
     *  This method populate the groups comboboxes...
     */
    public void updateGroups() {
        this.setInit(true);
        // Use the name of the group and not the group object....
        Vector group_names = new Vector();
        Enumeration e = jrf.getReport().getGroups().elements();
        while (e.hasMoreElements()) {
            group_names.addElement(""+e.nextElement());
        }
        Misc.updateComboBox( jComboBoxGroups, group_names,true);
        Misc.updateComboBox( jComboBoxImageGroup , group_names,true);
        Misc.updateComboBox( jComboBoxImageGroup1 , group_names,true);
        Misc.updateComboBox( jComboBoxTextFieldGroup , group_names,true);
        Misc.updateComboBox( jComboBoxBarcodeGroup , group_names,true);
        this.setInit(false);
    }
    
    
    /**
     *  This method populate the bands combobox...
     */
    public void updateBands() {
        this.setInit(true);
        if (jrf != null) {
            if (jrf.getSelectedCrosstabEditorPanel() == null) {
                jLabel1.setText(I18n.getString("elementPropertiesDialog.label1","Band") );
                Misc.updateComboBox( jComboBoxBand, jrf.getReport().getBands(), true);
            } else {
                jLabel1.setText(I18n.getString("elementPropertiesDialog.label1Cell","Cell"));
                Misc.updateComboBox( jComboBoxBand, jrf.getSelectedCrosstabEditorPanel().getCrosstabElement().getCells(), true);
            }
        }
        this.setInit(false);
    }
    
    
    
    
    public void updateStyles() {
        this.setInit(true);
        Misc.updateComboBox( jComboBoxStyle, jrf.getReport().getStyles(), true);
        this.setInit(false);
    }
    
    
    /**
     *  This method populate the report fonts comboboxes...
     */
    public void updateReportFonts() {
        this.setInit(true);
        // Use the name of the group and not the group object....
        Vector font_names = new Vector();
        Enumeration e = jrf.getReport().getFonts().elements();
        while (e.hasMoreElements()) {
            font_names.addElement(""+e.nextElement());
        }
        Misc.updateStringComboBox(jComboBoxReportFont, font_names,true);
        this.setInit(false);
    }
    
    
    public void updateFonts() {
        
        boolean localInit = this.isInit();
        this.setInit(true);
        // Add here the other fonts...
        Vector fontsVec = new Vector();
        fontsVec.addElement(new Tag("Helvetica"));
        fontsVec.addElement(new Tag("Helvetica-Bold"));
        fontsVec.addElement(new Tag("Helvetica-BoldOblique"));
        fontsVec.addElement(new Tag("Helvetica-Oblique"));
        fontsVec.addElement(new Tag("Courier"));
        fontsVec.addElement(new Tag("Courier-Bold"));
        fontsVec.addElement(new Tag("Courier-BoldOblique"));
        fontsVec.addElement(new Tag("Courier-Oblique"));
        fontsVec.addElement(new Tag("Symbol"));
        fontsVec.addElement(new Tag("Times-Roman"));
        fontsVec.addElement(new Tag("Times-Bold"));
        fontsVec.addElement(new Tag("Times-BoldItalic"));
        fontsVec.addElement(new Tag("Times-Italic"));
        fontsVec.addElement(new Tag("ZapfDingbats"));
        fontsVec.addElement(new Tag("STSong-Light"));
        fontsVec.addElement(new Tag("MHei-Medium"));
        fontsVec.addElement(new Tag("MSung-Light"));
        fontsVec.addElement(new Tag("HeiseiKakuGo-W5"));
        fontsVec.addElement(new Tag("HeiseiMin-W3"));
        fontsVec.addElement(new Tag("HYGoThic-Medium"));
        fontsVec.addElement(new Tag("HYSMyeongJo-Medium"));
        Vector iRfonts = MainFrame.getMainInstance().getTtfFonts();
        for (int i_f=0; i_f<iRfonts.size(); ++i_f) {
            fontsVec.addElement(new Tag( ((IRFont)iRfonts.elementAt(i_f)).getFile(),
                    iRfonts.elementAt(i_f)+""));
        }
        Misc.updateComboBox(jComboBoxPDFFontName,fontsVec);
        this.setInit(localInit);
    }
    
    
    
    
    protected boolean setElementNumber( boolean firstTime, double value, JNumberField numberField ) {
        if (( ! firstTime ) && (!(numberField.getValue() == value))) {
            numberField.setText("");
            return false;
        } else {
            try {
                numberField.setValue( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    protected boolean setSpinnerLevel( boolean firstTime, int value, javax.swing.JSpinner spinner ) {
        javax.swing.SpinnerNumberModel snm = (javax.swing.SpinnerNumberModel)spinner.getModel();
        if (( ! firstTime ) && (!( ((Number)snm.getNumber()).intValue() == value))) {
            snm.setValue(new Integer(0));
            return false;
        } else {
            try {
                snm.setValue(new Integer(value) );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    public static boolean setElementComboNumber( boolean firstTime, double value, JNumberComboBox numberField ) {
        if (( ! firstTime ) && (!(numberField.getValue() == value))) {
            numberField.setSelectedItem("");
            return false;
        } else {
            try {
                numberField.setValue( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    
    protected boolean setCheckBox( boolean firstTime, boolean value, javax.swing.JCheckBox checkField ) {
        if (( ! firstTime ) && (!(checkField.isSelected() == value))) {
            checkField.setSelected(false);
            return false;
        } else {
            try {
                checkField.setSelected( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    
    protected boolean setComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!(comboField.getSelectedItem() == value))) {
            comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                comboField.setSelectedItem( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    protected boolean setFontComboBox( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!(comboField.getSelectedItem()!=null && comboField.getSelectedItem() instanceof IRFont && ((IRFont)comboField.getSelectedItem()).getFile().equals(value)))) {
            if (comboField.getItemCount() > 0)
                comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof IRFont && ((IRFont)comboField.getItemAt(i)).getFile().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
                if (comboField.getItemCount() > 0)
                    comboField.setSelectedIndex(0);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    protected boolean setPdfEncodingComboBox( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!(comboField.getSelectedItem()!=null && ( (comboField.getSelectedItem() instanceof PdfEncoding && ((PdfEncoding)comboField.getSelectedItem()).getEncoding().equalsIgnoreCase(value)) || (comboField.getSelectedItem()+"").equals(value) )))) {
            if (comboField.getItemCount() > 0)
                comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof PdfEncoding && ((PdfEncoding)comboField.getItemAt(i)).getEncoding().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
                comboField.setSelectedItem(value);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    public static boolean setComboBoxText( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!( Misc.nvl(comboField.getSelectedItem(),"").equalsIgnoreCase(value)))) {
            comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                comboField.setSelectedItem( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    public static boolean setComboBoxTag( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if ((!firstTime ) && (!( Misc.nvl( Misc.getComboboxSelectedValue(comboField),"").equalsIgnoreCase(value)))) {
            comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                Misc.setComboboxSelectedTagValue(comboField, value);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    protected boolean setTagComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (( ! firstTime ) && (!(
                comboField.getSelectedItem() != null &&
                comboField.getSelectedItem() instanceof Tag &&
                ((Tag)comboField.getSelectedItem()).getValue().equals(value)))) {
            comboField.setSelectedIndex(0);
            return false;
        } else {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof Tag && ((Tag)comboField.getItemAt(i)).getValue().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setMixedTagComboBox( boolean firstTime, Object value, javax.swing.JComboBox comboField ) {
        if (firstTime) {
            try {
                for (int i=0; i<comboField.getItemCount(); ++i) {
                    if (comboField.getItemAt(i) instanceof Tag && ((Tag)comboField.getItemAt(i)).getValue().equals(value) ) {
                        comboField.setSelectedIndex(i);
                        return true;
                    }
                }
                // No tag found...
                comboField.setSelectedItem(value);
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        } else {
            Object selectedValue = comboField.getSelectedItem();
            if (selectedValue == null && value == null) return true;
            if (selectedValue == null) return false;
            if (selectedValue instanceof Tag) {
                selectedValue = ((Tag)selectedValue).getValue();
            }
            
            if (selectedValue.equals(value)) return true;
        }
        return false;
    }
    
    
    
    
    protected boolean setTextField( boolean firstTime, String value, javax.swing.JTextField textField ) {
        if (( ! firstTime ) && (!textField.getText().equalsIgnoreCase(value))) {
            textField.setText("");
            return false;
        } else {
            try {
                textField.setText( (value == null) ? "" : value+"");
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    
    
    
    public void setVisible(boolean visible) {
        try {
            
            if (visible == isVisible()) return;
            
            
            if (visible == true) {
                this.updateSelection(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.setVisible(visible);
        
    }
    
    /** Getter for property init.
     * @return Value of property init.
     *
     */
    public boolean isInit() {
        return init;
    }
    
    /** Setter for property init.
     * @param init New value of property init.
     *
     */
    public void setInit(boolean init) {
        this.init = init;
    }
    
    
    protected boolean setElementColor( boolean firstTime, Color value, javax.swing.JButton colorField ) {
        
        if (firstTime ) {
            colorField.setBackground( value );
            return true;
        } else {
            java.awt.Color c1 = colorField.getBackground();
            if (c1 == value) {
                return true;
            }
            if (c1 != null && value != null && c1.getRGB() == value.getRGB()) {
                return true;
            }
            
            colorField.setBackground( Color.GRAY);
        }
        
        return false;
    }
    
    protected boolean setTextArea( boolean firstTime, String value, JRTextExpressionArea textField ) {
        
        
        if (( ! firstTime ) && (!(textField.getText().equals(value) ))) {
            textField.setText("");
            ((org.syntax.jedit.SyntaxDocument)textField.getDocument()).getUM().discardAllEdits();
            return false;
        } else {
            try {
                textField.setText( value );
                ((org.syntax.jedit.SyntaxDocument)textField.getDocument()).getUM().discardAllEdits();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setJTextArea( boolean firstTime, String value, javax.swing.JTextArea textField ) {
        
        if (( ! firstTime ) && (!(textField.getText().equals(value) ))) {
            textField.setText("");
            return false;
        } else {
            try {
                textField.setText( value );
                
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setPatternComboBox( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        if (( !firstTime) && (!((comboField.getSelectedItem()+"").equals(value) ))) {
            comboField.setSelectedItem("");
            return false;
        } else {
            try {
                comboField.addItem(value);
                comboField.setSelectedItem( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    protected boolean setWritableComboBoxText( boolean firstTime, String value, javax.swing.JComboBox comboField ) {
        
        if (( !firstTime) && (!((comboField.getSelectedItem()+"").equals(value) ))) {
            comboField.setSelectedItem("");
            return false;
        } else {
            try {
                if (value == null || value.equals("") ) comboField.setSelectedIndex(0);
                comboField.setSelectedItem( value );
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return true;
    }
    
    public void jRTextExpressionAreaPrintWhenExpressionTextChanged() {
        if (this.isInit()) return;
        
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ReportElement element = (ReportElement)e.nextElement();
            element.printWhenExpression = jRTextExpressionAreaPrintWhenExpression.getText();
            //System.out.println("associato testo: "+element.printWhenExpression);
        }
        
        fireReportElementsChangedEvent("printWhenExpression", jRTextExpressionAreaPrintWhenExpression.getText());
    }
    
    public void jRTextExpressionAreaImageExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            ImageReportElement element = (ImageReportElement)e.nextElement();
            element.setImageExpression( jRTextExpressionAreaImageExpression.getText());
        }
        repaintEditor();
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    public void jTextAreaTextChanged() {
        if (this.isInit()) return;
        
        // Set band to all....
        if (jrf == null || getElementSelection().size()==0) return;
        // Set the new value for all selected elements...
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextReportElement element = (TextReportElement)e.nextElement();
            element.setText(""+ jTextAreaText.getText());
        }
        fireReportElementsChangedEvent("text", jTextAreaText.getText());
        repaintEditor();
    }
    
    public void jRTextExpressionAreaTextFieldExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            TextFieldReportElement element = (TextFieldReportElement)e.nextElement();
            element.setText(""+jRTextExpressionAreaTextFieldExpression.getText());
        }
        fireReportElementsChangedEvent("textfieldExpression", jRTextExpressionAreaTextFieldExpression.getText());
        repaintEditor();
    }
    
    public void jRTextExpressionAreaSubreportExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            SubReportElement element = (SubReportElement)e.nextElement();
            element.setSubreportExpression(""+jRTextExpressionAreaSubreportExpression.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        repaintEditor();
    }
    
    public void jRTextExpressionAreaTextConnectionExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            SubReportElement element = (SubReportElement)e.nextElement();
            if (element.isUseConnection())
                element.setConnectionExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
            else
                element.setDataSourceExpression(""+jRTextExpressionAreaTextConnectionExpression.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        repaintEditor();
    }
    
    public void jRTextExpressionAreaCrosstabParametersMapExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
            element.setParametersMapExpression(""+jRTextExpressionAreaCrosstabParametersMapExpression.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
        repaintEditor();
    }
    
    public void jRTextExpressionAreaSubreportMapExpressionTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            SubReportElement element = (SubReportElement)e.nextElement();
            element.setParametersMapExpression(""+jRTextExpressionAreaSubreportMapExpression.getText());
            repaintEditor();
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jRTextExpressionAreaAnchorNameTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setAnchorNameExpression(""+jRTextExpressionAreaAnchorName.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jRTextExpressionAreaAnchorTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setHyperlinkAnchorExpression(""+jRTextExpressionAreaAnchor.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jRTextExpressionAreaPageTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setHyperlinkPageExpression(""+jRTextExpressionAreaPage.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jRTextExpressionAreaReferenceTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setHyperlinkReferenceExpression(""+jRTextExpressionAreaReference.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    public void jRTextExpressionAreaTooltipTextChanged() {
        if (this.isInit()) return;
        Enumeration e = getElementSelection().elements();
        while (e.hasMoreElements()) {
            HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
            element.setTooltipExpression(""+jRTextExpressionAreaTooltip.getText());
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
        
    }
    
    
    public JRTextExpressionArea getJRTextExpressionArea(javax.swing.JComponent c, int x, int y) {
        JRTextExpressionArea xxx= null;
        //System.out.println(">>> " + c);
        if (c.getClass().isAssignableFrom(it.businesslogic.ireport.gui.JRTextExpressionArea.class) ) {
            if ( ((JRTextExpressionArea)c).isRequestFocusEnabled() == true && c.contains(x,y)) {
                //System.out.println( ((JRTextExpressionArea)c).getText() );
                return (JRTextExpressionArea)c;
            }
            return null;
        } else {
            for (int i=0; i< c.getComponentCount(); ++i) {
                Component child = c.getComponent(i);
                if (child instanceof javax.swing.JComponent) {
                    xxx = getJRTextExpressionArea((javax.swing.JComponent)child, x, y );
                    if (xxx != null) return xxx;
                }
            }
            
        }
        return null;
    }
    
    
    
    
    public void gotoTab(int tabCode ) {
        try {
            switch (tabCode) {
                case GRAPHICS_TAB:
                    jTabbedPane.setSelectedComponent( jPanelGraphicselement );
                    break;
                case RECTANGLE_TAB:
                    jTabbedPane.setSelectedComponent( jPanelRectangle);
                    break;
                case LINE_TAB:
                    jTabbedPane.setSelectedComponent( jPanelLine );
                    break;
                case IMAGE_TAB:
                    jTabbedPane.setSelectedComponent( jPanelImage );
                    break;
                case FONT_TAB:
                    jTabbedPane.setSelectedComponent( jPanelFont );
                    break;
                case STATITEXT_TAB:
                    jTabbedPane.setSelectedComponent( jPanelText );
                    break;
                case TEXTFIELD_TAB:
                    jTabbedPane.setSelectedComponent( jPanelTextField );
                    break;
                case HYPERLINK_TAB:
                    jTabbedPane.setSelectedComponent( jPanelHyperLink );
                    try {
                        jTabbedPane2.setSelectedComponent(jPanelLinkParams);
                    } catch (Exception ex){ }
                    break;
                case SUBREPORT1_TAB:
                    jTabbedPane.setSelectedComponent( jPanelSubreport1 );
                    break;
                case SUBREPORT2_TAB:
                    jTabbedPane.setSelectedComponent( jPanelSubreport2 );
                    break;
                case CHART_TAB:
                    jTabbedPane.setSelectedComponent( jPanelChart );
                    break;
                case BARCODE_TAB:
                    jTabbedPane.setSelectedComponent( jPanelBarcode );
                    break;
                case COMMON_TAB:
                default:
                    jTabbedPane.setSelectedComponent( jPanelCommon );
                    break;
            }
            
        } catch (Exception ex) {}
    }
    
    /** Initalize the TextExpressionArea with keywords (variables, parameters
     *    and fields) from the report.
     */
    private void initExpressionArea( JRTextExpressionArea area) {
        area.getTokenMarker().setKeywordLookup(jrf.getReport().getKeywordLookup());
    }
    
    public void jTextFieldNameChanged() {
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        ReportElement element = (ReportElement)e.nextElement();
        
        element.setKey(  jTextFieldName.getText() );
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
        repaintEditor();
    }
    
    public void jTextFieldAppIdentifierChanged() {
        if (this.isInit()) return;
        
        if (jrf == null || getElementSelection().size()==0) return;
        
        Enumeration e = getElementSelection().elements();
        BarcodeReportElement element = (BarcodeReportElement)e.nextElement();
        
        element.setApplicationIdentifier(  jBarcodeExpressionAreaAppIdentifier.getText() );
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , element , ReportElementChangedEvent.CHANGED));
        repaintEditor();
    }
    
    public Vector getElementSelection() {
        if (jrf.getSelectedCrosstabEditorPanel() == null) {
            return jrf.getSelectedElements( );
        } else {
            return jrf.getSelectedCrosstabEditorPanel().getSelectedElements();
        }
    }
    
    public void repaintEditor() {
        
        if (jrf.getSelectedCrosstabEditorPanel() == null) {
            jrf.getJPanelReport().repaint( );
        } else {
            jrf.getSelectedCrosstabEditorPanel().repaint();
        }
    }
    
    /**
     * Property name refers to the name of the property in in the ReportElementSheetProperty
     * if propertyname is null, a general event is fired
     */
    public void fireReportElementsChangedEvent(String propertyName, Object newValue) {
        if (jrf == null) return;
        ReportElementChangedEvent changedEvent = new ReportElementChangedEvent(jrf ,(jrf.getSelectedCrosstabEditorPanel() != null)  ? jrf.getSelectedCrosstabEditorPanel().getCrosstabElement() : null , getElementSelection() , ReportElementChangedEvent.CHANGED);
        changedEvent.setEventSource( this );
        changedEvent.setPropertyChanged( propertyName );
        changedEvent.setNewValue(newValue);
        jrf.fireReportListenerReportElementsChanged(changedEvent);
    }
    
    private static Locale lastLocale = null;
    public void applyI18n(){
                    
                if (lastLocale != null && lastLocale == I18n.getCurrentLocale()) return;
                lastLocale = I18n.getCurrentLocale();
        
                this.setInit(true);
                // Start autogenerated code ----------------------
                jCheckBoxBarcodeCheckSum.setText(I18n.getString("elementPropertiesDialog.checkBoxBarcodeCheckSum","Checksum"));
                jCheckBoxBarcodeShowText.setText(I18n.getString("elementPropertiesDialog.checkBoxBarcodeShowText","Show Text"));
                jCheckBoxBlankWhenNull.setText(I18n.getString("elementPropertiesDialog.checkBoxBlankWhenNull","Blank when null"));
                jCheckBoxBold.setText(I18n.getString("elementPropertiesDialog.checkBoxBold","Bold"));
                jCheckBoxImageCache.setText(I18n.getString("elementPropertiesDialog.checkBoxImageCache","Using cache"));
                jCheckBoxImageIsLazy.setText(I18n.getString("elementPropertiesDialog.checkBoxImageIsLazy","Is Lazy"));
                jCheckBoxItalic.setText(I18n.getString("elementPropertiesDialog.checkBoxItalic","Italic"));
                jCheckBoxPDFEmbedded.setText(I18n.getString("elementPropertiesDialog.checkBoxPDFEmbedded","PDF Embedded"));
                jCheckBoxPrintInFirstWholeBand.setText(I18n.getString("elementPropertiesDialog.checkBoxPrintInFirstWholeBand","Print in first whole band"));
                jCheckBoxPrintRepeatedValues.setText(I18n.getString("elementPropertiesDialog.checkBoxPrintRepeatedValues","Print repeated values"));
                jCheckBoxPrintWhenDetailOverflows.setText(I18n.getString("elementPropertiesDialog.checkBoxPrintWhenDetailOverflows","Print when detail overflows"));
                jCheckBoxRemoveLineWhenBlank.setText(I18n.getString("elementPropertiesDialog.checkBoxRemoveLineWhenBlank","Remove line when blank"));
                jCheckBoxRepeatColumnHeaders.setText(I18n.getString("elementPropertiesDialog.checkBoxRepeatColumnHeaders","Repeat column headers"));
                jCheckBoxRepeatRowHeaders.setText(I18n.getString("elementPropertiesDialog.checkBoxRepeatRowHeaders","Repeat row headers"));
                jCheckBoxStretchWithOverflow.setText(I18n.getString("elementPropertiesDialog.checkBoxStretchWithOverflow","Stretch with overflow"));
                jCheckBoxStrokeTrough.setText(I18n.getString("elementPropertiesDialog.checkBoxStrokeTrough","Strike Trough"));
                jCheckBoxStyledText.setText(I18n.getString("elementPropertiesDialog.checkBoxStyledText","Is styled text"));
                jCheckBoxSubreportCache.setText(I18n.getString("elementPropertiesDialog.checkBoxSubreportCache","Using cache"));
                jCheckBoxTransparent.setText(I18n.getString("elementPropertiesDialog.checkBoxTransparent","Transparent"));
                jCheckBoxUnderline.setText(I18n.getString("elementPropertiesDialog.checkBoxUnderline","Underline"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                jButton2.setText(I18n.getString("elementPropertiesDialog.button2","Edit crosstab properties"));
                jButtonAddCrosstabParameter.setText(I18n.getString("elementPropertiesDialog.buttonAddCrosstabParameter","Add"));
                jButtonAddLinkParameter.setText(I18n.getString("elementPropertiesDialog.buttonAddLinkParameter","Add"));
                jButtonAddParameter.setText(I18n.getString("elementPropertiesDialog.buttonAddParameter","Add"));
                jButtonAddReturnValue.setText(I18n.getString("elementPropertiesDialog.buttonAddReturnValue","Add"));
                jButtonCopyParamsFromMaster.setText(I18n.getString("elementPropertiesDialog.buttonCopyParamsFromMaster","Copy from master"));
                jButtonCreatePattern.setText(I18n.getString("elementPropertiesDialog.buttonCreatePattern","Create..."));
                jButtonFindImage.setText(I18n.getString("elementPropertiesDialog.buttonFindImage","Find..."));
                jButtonModCrosstabParameter.setText(I18n.getString("elementPropertiesDialog.buttonModCrosstabParameter","Modify"));
                jButtonModLinkParameter.setText(I18n.getString("elementPropertiesDialog.buttonModLinkParameter","Modify"));
                jButtonModParameter.setText(I18n.getString("elementPropertiesDialog.buttonModParameter","Modify"));
                jButtonModReturnValue.setText(I18n.getString("elementPropertiesDialog.buttonModReturnValue","Modify"));
                jButtonRemCrosstabParameter.setText(I18n.getString("elementPropertiesDialog.buttonRemCrosstabParameter","Remove"));
                jButtonRemLinkParameter.setText(I18n.getString("elementPropertiesDialog.buttonRemLinkParameter","Remove"));
                jButtonRemParameter.setText(I18n.getString("elementPropertiesDialog.buttonRemParameter","Remove"));
                jButtonRemReturnValue.setText(I18n.getString("elementPropertiesDialog.buttonRemReturnValue","Remove"));
                jLabel1.setText(I18n.getString("elementPropertiesDialog.label1","Band "));
                jLabel10.setText(I18n.getString("elementPropertiesDialog.label10","Print when expression"));
                jLabel11.setText(I18n.getString("elementPropertiesDialog.label11","Pen"));
                jLabel12.setText(I18n.getString("elementPropertiesDialog.label12","Stretch Type"));
                jLabel13.setText(I18n.getString("elementPropertiesDialog.label13","Fill"));
                jLabel14.setText(I18n.getString("elementPropertiesDialog.label14","Radius"));
                jLabel15.setText(I18n.getString("elementPropertiesDialog.label15","Line direction"));
                jLabel16.setText(I18n.getString("elementPropertiesDialog.label16","Image Expression"));
                jLabel17.setText(I18n.getString("elementPropertiesDialog.label17","Image Expression Class"));
                jLabel18.setText(I18n.getString("elementPropertiesDialog.label18","Scale Image"));
                jLabel19.setText(I18n.getString("elementPropertiesDialog.label19","Vertical alignment"));
                jLabel2.setText(I18n.getString("elementPropertiesDialog.label2","Top "));
                jLabel20.setText(I18n.getString("elementPropertiesDialog.label20","Horizontal alignment"));
                jLabel21.setText(I18n.getString("elementPropertiesDialog.label21","Evaluation time"));
                jLabel22.setText(I18n.getString("elementPropertiesDialog.label22","Evaluation group"));
                jLabel23.setText(I18n.getString("elementPropertiesDialog.label23","Report font"));
                jLabel24.setText(I18n.getString("elementPropertiesDialog.label24","Font name"));
                jLabel25.setText(I18n.getString("elementPropertiesDialog.label25","PDF font name"));
                jLabel26.setText(I18n.getString("elementPropertiesDialog.label26","Subreport Expression"));
                jLabel27.setText(I18n.getString("elementPropertiesDialog.label27","Size"));
                jLabel28.setText(I18n.getString("elementPropertiesDialog.label28","Textfield Expression Class"));
                jLabel29.setText(I18n.getString("elementPropertiesDialog.label29","Line spacing"));
                jLabel3.setText(I18n.getString("elementPropertiesDialog.label3","Left "));
                jLabel30.setText(I18n.getString("elementPropertiesDialog.label30","Horizontal align"));
                jLabel31.setText(I18n.getString("elementPropertiesDialog.label31","Vertical align"));
                jLabel32.setText(I18n.getString("elementPropertiesDialog.label32","PDF Encoding"));
                jLabel33.setText(I18n.getString("elementPropertiesDialog.label33","Subreport Expression Class"));
                jLabel34.setText(I18n.getString("elementPropertiesDialog.label34","On error type"));
                jLabel35.setText(I18n.getString("elementPropertiesDialog.label35","Anchor Name Expression"));
                jLabel36.setText(I18n.getString("elementPropertiesDialog.label36","Hyperlink type"));
                jLabel37.setText(I18n.getString("elementPropertiesDialog.label37","Evaluation time"));
                jLabel38.setText(I18n.getString("elementPropertiesDialog.label38","Evaluation group"));
                jLabel39.setText(I18n.getString("elementPropertiesDialog.label39","Pattern"));
                jLabel4.setText(I18n.getString("elementPropertiesDialog.label4","Width"));
                jLabel40.setText(I18n.getString("elementPropertiesDialog.label40","Textfield expression"));
                jLabel41.setText(I18n.getString("elementPropertiesDialog.label41","Connection / Datasource Expression"));
                jLabel42.setText(I18n.getString("elementPropertiesDialog.label42","Parameters Map Expression"));
                jLabel43.setText(I18n.getString("elementPropertiesDialog.label43","Evaluation group"));
                jLabel44.setText(I18n.getString("elementPropertiesDialog.label44","Evaluation time"));
                jLabel45.setText(I18n.getString("elementPropertiesDialog.label45","Evaluation group"));
                jLabel46.setText(I18n.getString("elementPropertiesDialog.label46","Key"));
                jLabel47.setText(I18n.getString("elementPropertiesDialog.label47","Type"));
                jLabel48.setText(I18n.getString("elementPropertiesDialog.label48","Barcode expression"));
                jLabel49.setText(I18n.getString("elementPropertiesDialog.label49","Rotation"));
                jLabel5.setText(I18n.getString("elementPropertiesDialog.label5","Height "));
                jLabel50.setText(I18n.getString("elementPropertiesDialog.label50","Evaluation time"));
                jLabel51.setText(I18n.getString("elementPropertiesDialog.label51","Parameters Map Expression"));
                jLabel52.setText(I18n.getString("elementPropertiesDialog.label52","Crosstab parameters"));
                jLabel53.setText(I18n.getString("elementPropertiesDialog.label53","Column break offset"));
                jLabel54.setText(I18n.getString("elementPropertiesDialog.label54","Scale Barcode Image"));
                jLabel55.setText(I18n.getString("elementPropertiesDialog.label55","On error type"));
                jLabel56.setText(I18n.getString("elementPropertiesDialog.label56","Horizontal alignment"));
                jLabel57.setText(I18n.getString("elementPropertiesDialog.label57","Vertical alignment"));
                jLabel58.setText(I18n.getString("elementPropertiesDialog.label58","Bar height"));
                jLabel59.setText(I18n.getString("elementPropertiesDialog.label59","Application identifier"));
                jLabel6.setText(I18n.getString("elementPropertiesDialog.label6","Foreground "));
                jLabel60.setText(I18n.getString("elementPropertiesDialog.label60","(0 = default)"));
                jLabel61.setText(I18n.getString("elementPropertiesDialog.label61","Bar width"));
                jLabel7.setText(I18n.getString("elementPropertiesDialog.label7","Background "));
                jLabel8.setText(I18n.getString("elementPropertiesDialog.label8","Position type"));
                jLabel9.setText(I18n.getString("elementPropertiesDialog.label9","Print when group changes"));
                jLabelAnchor.setText(I18n.getString("elementPropertiesDialog.labelAnchor","Hyperlink Anchor Expression"));
                jLabelPage.setText(I18n.getString("elementPropertiesDialog.labelPage","Hyperlink Page Expression"));
                jLabelReference.setText(I18n.getString("elementPropertiesDialog.labelReference","Hyperlink Reference Expression"));
                jLabelStyle.setText(I18n.getString("elementPropertiesDialog.labelStyle","Style"));
                jLabelTarget.setText(I18n.getString("elementPropertiesDialog.labelTarget","Hyperlink target"));
                jLabelTarget1.setText(I18n.getString("elementPropertiesDialog.labelTarget1","Bookmark level"));
                // End autogenerated code ----------------------
                
                jTableSubreportParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("elementPropertiesDialog.subreportParameters.parameter","Parameter") );
                jTableSubreportParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("elementPropertiesDialog.subreportParameters.expression","Expression") );
                
                jTableSubreportReturnValues.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("elementPropertiesDialog.subreportReturnValues.subreportVariable","Subreport variable") );
                jTableSubreportReturnValues.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("elementPropertiesDialog.subreportReturnValues.destinationVariable","Destination variable") );
                
                jTableLinkParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("elementPropertiesDialog.linkParameters.linkParameter","Link parameter") );
                jTableLinkParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("elementPropertiesDialog.linkParameters.expression","Expression") );
                
                jTableCrosstabParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("elementPropertiesDialog.crosstabParameters.parameter","Parameter") );
                jTableCrosstabParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("elementPropertiesDialog.crosstabParameters.expression","Expression") );
                
    
                // Position types...     
                jComboBoxPositionType.removeAllItems();
                jComboBoxPositionType.addItem(new Tag("FixRelativeToTop", I18n.getString("gui.elementpropertiessheet.positionType.FixRelativeToTop","FixRelativeToTop")));
                jComboBoxPositionType.addItem(new Tag("Float", I18n.getString("gui.elementpropertiessheet.positionType.Float","Float")));
                jComboBoxPositionType.addItem(new Tag("FixRelativeToBottom", I18n.getString("gui.elementpropertiessheet.positionType.FixRelativeToBottom","FixRelativeToBottom")));
        
                // Pens...
                jComboBoxPen.removeAllItems();
                jComboBoxPen.addItem(new Tag("None", I18n.getString("gui.elementpropertiessheet.pen.None","None")) );
                jComboBoxPen.addItem(new Tag("Thin", I18n.getString("gui.elementpropertiessheet.pen.Thin","Thin")));
                jComboBoxPen.addItem(new Tag("1Point", I18n.getString("gui.elementpropertiessheet.pen.1Point","1Point")) );
                jComboBoxPen.addItem(new Tag("2Point", I18n.getString("gui.elementpropertiessheet.pen.2Point","2Point")) );
                jComboBoxPen.addItem(new Tag("4Point", I18n.getString("gui.elementpropertiessheet.pen.4Point","4Point")) );
                jComboBoxPen.addItem(new Tag("Dotted", I18n.getString("gui.elementpropertiessheet.pen.Dotted","Dotted")) );
        
                // StretchTypes...
                jComboBoxStretchType.removeAllItems();
                jComboBoxStretchType.addItem(new Tag("NoStretch", I18n.getString("gui.elementpropertiessheet.stretchType.NoStretch","No stretch")) );
                jComboBoxStretchType.addItem(new Tag("RelativeToTallestObject", I18n.getString("gui.elementpropertiessheet.stretchType.RelativeToTallestObject","Relative to tallest object")) );
                jComboBoxStretchType.addItem(new Tag("RelativeToBandHeight", I18n.getString("gui.elementpropertiessheet.stretchType.RelativeToBandHeight","Relative to band height")) );

                // Fill...
                jComboBoxFill.removeAllItems();
                jComboBoxFill.addItem(new Tag("Solid", I18n.getString("gui.elementpropertiessheet.fill.Solid","Solid")));

                // Line...
                jComboBoxLineDirection.removeAllItems();
                jComboBoxLineDirection.addItem(new Tag("TopDown", I18n.getString("gui.elementpropertiessheet.direction.TopDown","Top-down (\\)")));
                jComboBoxLineDirection.addItem(new Tag("BottomUp", I18n.getString("gui.elementpropertiessheet.direction.BottomUp","Bottom-up (/)")));

                // Rotate
                jComboBoxRotation.removeAllItems();
                jComboBoxRotation.addItem(new Tag("None", I18n.getString("gui.elementpropertiessheet.rotate.None","None")));
                jComboBoxRotation.addItem(new Tag("Left", I18n.getString("gui.elementpropertiessheet.rotate.Left","Left")));
                jComboBoxRotation.addItem(new Tag("Right", I18n.getString("gui.elementpropertiessheet.rotate.Right","Right")));
                jComboBoxRotation.addItem(new Tag("UpsideDown", I18n.getString("gui.elementpropertiessheet.rotate.UpsideDown","Upside down")));


                // Scale Image...
                jComboBoxScale.removeAllItems();
                jComboBoxScale.addItem(new Tag("Clip", I18n.getString("gui.elementpropertiessheet.imageScale.Clip","Clip")));
                jComboBoxScale.addItem(new Tag("FillFrame", I18n.getString("gui.elementpropertiessheet.imageScale.FillFrame","Fill frame")));
                jComboBoxScale.addItem(new Tag("RetainShape", I18n.getString("gui.elementpropertiessheet.imageScale.RetainShape","Retain shape")));

                // bARCODE Scale Image...
                jComboBoxScale1.removeAllItems();
                jComboBoxScale1.addItem(new Tag("Clip", I18n.getString("gui.elementpropertiessheet.imageScale.Clip","Clip")));
                jComboBoxScale1.addItem(new Tag("FillFrame", I18n.getString("gui.elementpropertiessheet.imageScale.FillFrame","Fill frame")));
                jComboBoxScale1.addItem(new Tag("RetainShape", I18n.getString("gui.elementpropertiessheet.imageScale.RetainShape","Retain shape")));

                // On error...
                jComboBoxImageOnError.removeAllItems();
                jComboBoxImageOnError.addItem(new Tag("Error", I18n.getString("gui.elementpropertiessheet.imageScale.Error","Error")));
                jComboBoxImageOnError.addItem(new Tag("Blank", I18n.getString("gui.elementpropertiessheet.imageScale.Blank","Blank")));
                jComboBoxImageOnError.addItem(new Tag("Icon", I18n.getString("gui.elementpropertiessheet.imageScale.Icon","Icon")));

                // BARCODE On error...
                jComboBoxImageOnError1.removeAllItems();
                jComboBoxImageOnError1.addItem(new Tag("Error", I18n.getString("gui.elementpropertiessheet.imageScale.Error","Error")));
                jComboBoxImageOnError1.addItem(new Tag("Blank", I18n.getString("gui.elementpropertiessheet.imageScale.Blank","Blank")));
                jComboBoxImageOnError1.addItem(new Tag("Icon", I18n.getString("gui.elementpropertiessheet.imageScale.Icon","Icon")));

                // Image Evaluation Time...
                jComboBoxEvaluationTime.removeAllItems();
                jComboBoxEvaluationTime.addItem(new Tag("Now", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
                jComboBoxEvaluationTime.addItem(new Tag("Report", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
                jComboBoxEvaluationTime.addItem(new Tag("Page", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
                jComboBoxEvaluationTime.addItem(new Tag("Column", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
                jComboBoxEvaluationTime.addItem(new Tag("Group", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
                jComboBoxEvaluationTime.addItem(new Tag("Band", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Band")));
                jComboBoxEvaluationTime.addItem(new Tag("Auto", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));

                // Image Evaluation Time chart...
                jComboBoxEvaluationTime1.removeAllItems();
                jComboBoxEvaluationTime1.addItem(new Tag("Now", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
                jComboBoxEvaluationTime1.addItem(new Tag("Report", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
                jComboBoxEvaluationTime1.addItem(new Tag("Page", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
                jComboBoxEvaluationTime1.addItem(new Tag("Column", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
                jComboBoxEvaluationTime1.addItem(new Tag("Group", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
                jComboBoxEvaluationTime1.addItem(new Tag("Band", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Band")));
                jComboBoxEvaluationTime1.addItem(new Tag("Auto", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));        



                // Image Vertical Alignments...    
                jComboBoxVerticalAlignment.removeAllItems();
                jComboBoxVerticalAlignment.addItem(new Tag("Top", I18n.getString("gui.elementpropertiessheet.valign.Top","Top")));
                jComboBoxVerticalAlignment.addItem(new Tag("Middle", I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")));
                jComboBoxVerticalAlignment.addItem(new Tag("Bottom", I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom")));

                // Image Horizontal Alignments...
                jComboBoxHorizontalAlignment.removeAllItems();
                jComboBoxHorizontalAlignment.addItem(new Tag("Left", I18n.getString("gui.elementpropertiessheet.halign.Left","Left")));
                jComboBoxHorizontalAlignment.addItem(new Tag("Center", I18n.getString("gui.elementpropertiessheet.halign.Center","Center")));
                jComboBoxHorizontalAlignment.addItem(new Tag("Right", I18n.getString("gui.elementpropertiessheet.halign.Right","Right")));


                // bARCODE Vertical Alignments...
                jComboBoxVerticalAlignment1.removeAllItems();
                jComboBoxVerticalAlignment1.addItem(new Tag("Top", I18n.getString("gui.elementpropertiessheet.valign.Top","Top")));
                jComboBoxVerticalAlignment1.addItem(new Tag("Middle", I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")));
                jComboBoxVerticalAlignment1.addItem(new Tag("Bottom", I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom")));

                // bARCODE Horizontal Alignments...
                jComboBoxHorizontalAlignment1.removeAllItems();
                jComboBoxHorizontalAlignment1.addItem(new Tag("Left", I18n.getString("gui.elementpropertiessheet.halign.Left","Left")));
                jComboBoxHorizontalAlignment1.addItem(new Tag("Center", I18n.getString("gui.elementpropertiessheet.halign.Center","Center")));
                jComboBoxHorizontalAlignment1.addItem(new Tag("Right", I18n.getString("gui.elementpropertiessheet.halign.Right","Right")));

                //jComboBoxPDFFontName.addItem("External TTF font...");

                // Text Vertical Alignments...
                jComboBoxVAlign.removeAllItems();
                jComboBoxVAlign.addItem(new Tag("Top", I18n.getString("gui.elementpropertiessheet.valign.Top","Top")));
                jComboBoxVAlign.addItem(new Tag("Middle", I18n.getString("gui.elementpropertiessheet.valign.Middle","Middle")));
                jComboBoxVAlign.addItem(new Tag("Bottom", I18n.getString("gui.elementpropertiessheet.valign.Bottom","Bottom")));


                // Image Horizontal Alignments...
                jComboBoxHAlign.removeAllItems();
                jComboBoxHAlign.addItem(new Tag("Left", I18n.getString("gui.elementpropertiessheet.halign.Left","Left")));
                jComboBoxHAlign.addItem(new Tag("Center", I18n.getString("gui.elementpropertiessheet.halign.Center","Center")));
                jComboBoxHAlign.addItem(new Tag("Right", I18n.getString("gui.elementpropertiessheet.halign.Right","Right")));
                jComboBoxHAlign.addItem(new Tag("Justified", I18n.getString("gui.elementpropertiessheet.halign.Justified","Justified")));


                jComboBoxLineSpacing.removeAllItems();
                jComboBoxLineSpacing.addItem(new Tag("Single", I18n.getString("gui.elementpropertiessheet.lineSpacing.Single","Single")));
                jComboBoxLineSpacing.addItem(new Tag("1_1_2", I18n.getString("gui.elementpropertiessheet.lineSpacing.1_1_2","1-1-2")));
                jComboBoxLineSpacing.addItem(new Tag("Double", I18n.getString("gui.elementpropertiessheet.lineSpacing.Double","Double")));

                // Text field Evaluation Time...
                jComboBoxTextFieldEvaluationTime.removeAllItems();
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Now", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Report", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Page", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Column", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Group", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Band", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Band")));
                jComboBoxTextFieldEvaluationTime.addItem(new Tag("Auto", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));        

                jComboBoxSubreportConnectionType.removeAllItems();
                jComboBoxSubreportConnectionType.addItem(I18n.getString("ConnectionType.1","Don't use connection or datasource"));
                jComboBoxSubreportConnectionType.addItem(I18n.getString("ConnectionType.2","Use connection expression"));
                jComboBoxSubreportConnectionType.addItem(I18n.getString("ConnectionType.3","Use datasource expression"));

                // Barcode Evaluation Time...
                jComboBoxEvaluationTimeBarcode.removeAllItems();
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Now", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Now","Now")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Report", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Report","Report")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Page", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Page","Page")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Column", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Column","Column")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Group", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Group","Group")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Band", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Band","Band")));
                jComboBoxEvaluationTimeBarcode.addItem(new Tag("Auto", I18n.getString("gui.elementpropertiessheet.textfieldEvaluationTime.Auto","Auto")));
                
                jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("elementPropertiesDialog.tab.SubreportParameters","Subreport parameters"));
                jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("elementPropertiesDialog.tab.SubreportReturnValues","Subreport return values"));
                        
                ((javax.swing.border.TitledBorder)jPanel1.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("elementPropertiesDialog.panelBorder.RectangleRadius","Rectangle radius") );
                
                this.updateSelection();
    }
    
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_ANCHORNAME_EXPRESSION=1;
    public static final int COMPONENT_HYPERLINK_REFERENCE_EXPRESSION=2;
    public static final int COMPONENT_HYPERLINK_ANCHOR_EXPRESSION=3;
    public static final int COMPONENT_HYPERLINK_PAGE_EXPRESSION=4;
    public static final int COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION=5;
    public static final int COMPONENT_HYPERLINK_PARAMETERS=6;
    public static final int COMPONENT_SUBREPORT_MAP_EXPRESSION=7;
    public static final int COMPONENT_SUBREPORT_DS_CONN_EXPRESSION=8;
    public static final int COMPONENT_SUBREPORT_EXPRESSION=9;
    public static final int COMPONENT_SUBREPORT_PARAMETERS=10;
    public static final int COMPONENT_SUBREPORT_RETURN_VALUES=11;
    public static final int COMPONENT_CROSSTAB_PARAMETERS=12;
    public static final int COMPONENT_CROSSTAB_MAP_EXPRESSION=13;
    
    /**
     * This method set the focus on a specific component.
     * Valid constants are something like:
     * COMPONENT_NONE, COMPONENT_ANCHORNAME_EXPRESSION, ...
     *
     */
    public void setFocusedExpression(int expID, int paramIdex)
    {
        try {
            switch (expID)
            {
                case COMPONENT_ANCHORNAME_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaAnchorName);
                    break;
                case COMPONENT_HYPERLINK_REFERENCE_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    this.jTabbedPane2.setSelectedComponent(  jPanelReference );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaReference);
                    break;  
                case COMPONENT_HYPERLINK_ANCHOR_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    this.jTabbedPane2.setSelectedComponent(  jPanelAnchor );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaAnchor);
                    break;  
                case COMPONENT_HYPERLINK_PAGE_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    this.jTabbedPane2.setSelectedComponent(  jPanelPage );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaPage);
                    break;  
                case COMPONENT_HYPERLINK_TOOLTIP_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    this.jTabbedPane2.setSelectedComponent(  jPanelTooltip );
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaTooltip);
                    break;
                case COMPONENT_HYPERLINK_PARAMETERS:
                    this.jTabbedPane.setSelectedComponent(this.jPanelHyperLink);
                    this.jTabbedPane2.setSelectedComponent(  jPanelLinkParams );
                    if (paramIdex >= 0 && jTableLinkParameters.getRowCount() > paramIdex)
                    {
                        jTableLinkParameters.setRowSelectionInterval(paramIdex,paramIdex);
                    }
                    break;
                 case COMPONENT_SUBREPORT_MAP_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelSubreport1);
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaSubreportMapExpression);
                    break;
                 case COMPONENT_SUBREPORT_DS_CONN_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelSubreport1);
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaTextConnectionExpression);
                    break;
                 case COMPONENT_SUBREPORT_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelSubreport2);
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaSubreportExpression);
                    break;
                 case COMPONENT_SUBREPORT_PARAMETERS:
                    this.jTabbedPane.setSelectedComponent(this.jPanelSubreport2);
                    this.jTabbedPane1.setSelectedComponent(  jPanel16 );
                    if (paramIdex >= 0 && jTableSubreportParameters.getRowCount() > paramIdex)
                    {
                        jTableSubreportParameters.setRowSelectionInterval(paramIdex,paramIdex);
                    }
                    break;
                 case COMPONENT_SUBREPORT_RETURN_VALUES:
                    this.jTabbedPane.setSelectedComponent(this.jPanelSubreport2);
                    this.jTabbedPane1.setSelectedComponent(  jPanelSubreportReturnValues );
                    if (paramIdex >= 0 && jTableSubreportReturnValues.getRowCount() > paramIdex)
                    {
                        jTableSubreportReturnValues.setRowSelectionInterval(paramIdex,paramIdex);
                    }
                    break;
                  case COMPONENT_CROSSTAB_PARAMETERS:
                    this.jTabbedPane.setSelectedComponent(this.jPanelCrosstab);
                    if (paramIdex >= 0 && jTableCrosstabParameters.getRowCount() > paramIdex)
                    {
                        jTableCrosstabParameters.setRowSelectionInterval(paramIdex,paramIdex);
                    }
                    break;
                  case COMPONENT_CROSSTAB_MAP_EXPRESSION:
                    this.jTabbedPane.setSelectedComponent(this.jPanelCrosstab);
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaCrosstabParametersMapExpression);
                    break;
            }
        } catch (Exception ex) { }
    }    
    
    /**
     * this method modify the parameter selected in the parameters list.<br>
     * An element must be selected.<br>
     * If <b>paramExpressionId</b> is specified, that expression is highlighted 
     */
    public void modifyLinkParameter(int paramExpressionId)
    {
        //setFocusedExpression(COMPONENT_HYPERLINK_PARAMETERS);
        
        
        Enumeration e = getElementSelection().elements();
        HyperLinkableReportElement element = (HyperLinkableReportElement)e.nextElement();
        if (jTableLinkParameters.getSelectedRow() < 0) return;
        JRLinkParameter parameter = (JRLinkParameter)jTableLinkParameters.getValueAt( jTableLinkParameters.getSelectedRow(), 0);
        
        JRLinkParameterDialog jrpd = new JRLinkParameterDialog(this, true);
        jrpd.setParameter( parameter );
        jrpd.setFocusedExpression( paramExpressionId );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            
            jTableLinkParameters.setValueAt(parameter, jTableSubreportParameters.getSelectedRow(), 0);
            jTableLinkParameters.setValueAt(parameter.getExpression(), jTableSubreportParameters.getSelectedRow(), 1);
            jTableLinkParameters.updateUI();
            
            MainFrame.getMainInstance().getActiveReportFrame().getReport().incrementReportChanges();
        }
        // TODO open the parameter expression...
    }
    
    
    /**
     * this method modify the parameter selected in the parameters list.<br>
     * An element must be selected.<br>
     * If <b>paramExpressionId</b> is specified, that expression is highlighted 
     */
    public void modifySubreportParameter(int paramExpressionId)
    {
    
        Enumeration e = getElementSelection().elements();
        SubReportElement element = (SubReportElement)e.nextElement();
        if (jTableSubreportParameters.getSelectedRow() < 0) return;
        JRSubreportParameter parameter = (JRSubreportParameter)jTableSubreportParameters.getValueAt( jTableSubreportParameters.getSelectedRow(), 0);
        
        JRSubreportParameterDialog jrpd = new JRSubreportParameterDialog(this, true);
        jrpd.setParameter( parameter );
        jrpd.setFocusedExpression( paramExpressionId );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setExpression( jrpd.getParameter().getExpression());
            
            jTableSubreportParameters.setValueAt(parameter, jTableSubreportParameters.getSelectedRow(), 0);
            jTableSubreportParameters.setValueAt(parameter.getExpression(), jTableSubreportParameters.getSelectedRow(), 1);
            jTableSubreportParameters.updateUI();
                
            MainFrame.getMainInstance().getActiveReportFrame().getReport().incrementReportChanges();
        }
    }
    
    
    /**
     * this method modify the parameter selected in the parameters list.<br>
     * An element must be selected.<br>
     * If <b>paramExpressionId</b> is specified, that expression is highlighted 
     */
    public void modifyCrosstabParameter(int paramExpressionId)
    {
        Enumeration e = getElementSelection().elements();
        CrosstabReportElement element = (CrosstabReportElement)e.nextElement();
        
        if (jTableCrosstabParameters.getSelectedRow() < 0) return;
        it.businesslogic.ireport.crosstab.CrosstabParameter parameter = (it.businesslogic.ireport.crosstab.CrosstabParameter)jTableCrosstabParameters.getValueAt( jTableCrosstabParameters.getSelectedRow(), 0);
        
        it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog jrpd = new it.businesslogic.ireport.crosstab.gui.CrosstabParameterDialog(this, true);
        jrpd.setParameter( parameter );
        jrpd.setFocusedExpression( paramExpressionId );
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            parameter.setName( jrpd.getParameter().getName() );
            parameter.setParameterValueExpression( jrpd.getParameter().getParameterValueExpression());
            
            jTableCrosstabParameters.setValueAt(parameter, jTableCrosstabParameters.getSelectedRow(), 0);
            jTableCrosstabParameters.setValueAt(parameter.getParameterValueExpression(), jTableCrosstabParameters.getSelectedRow(), 1);
            jTableCrosstabParameters.updateUI();
            
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf , getElementSelection() , ReportElementChangedEvent.CHANGED));
            
        }
    }
}
