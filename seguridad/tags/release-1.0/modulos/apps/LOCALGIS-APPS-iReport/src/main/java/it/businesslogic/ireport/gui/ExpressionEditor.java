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
 * ExpressionEditor.java
 * 
 * Created on 17 novembre 2003, 22.03
 *
 */

package it.businesslogic.ireport.gui;
import bsh.EvalError;
import bsh.ParseException;
import bsh.TargetError;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ExpressionContext;
import it.businesslogic.ireport.IRParsingException;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.expbuilder.ArithmeticOperationHelper;
import it.businesslogic.ireport.gui.expbuilder.ExpObject;
import it.businesslogic.ireport.gui.expbuilder.ExpObjectCellRenderer;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.JRFakeObject;
import it.businesslogic.ireport.util.Misc;
import java.awt.Color;
import java.io.StringReader;
import java.text.MessageFormat;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.tree.*;
import javax.swing.*;
import java.util.*;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.util.JRStringUtil;
import org.codehaus.groovy.control.CompilationFailedException;
import javax.swing.text.DefaultStyledDocument;
/**
 *
 * @author  Administrator
 */
public class ExpressionEditor extends javax.swing.JDialog implements CaretListener
{
   
    //public final static int CONTEXT_REPORT_DATASET = 0;
    //public final static int CONTEXT_BUCKET_MEASURE = 1;
    //public final static int CONTEXT_DATASET_RUN = 2;
    //public final static int CONTEXT_STYLE = 3;
    
    public static final int VARIABLE = 1;
    public static final int PARAMETER = 2;
    public static final int FIELD = 3;
    
    
    private SubDataset subDataset = null;
    private Vector crosstabElements = new Vector();
    
    public static Vector defaultExpressions = null;
    public boolean init = false;
    
    public static Vector recentExpressions = new Vector();
    
    
    Style errorStyle = null;
    Style okStyle = null;
    DefaultStyledDocument doc = null;

    
    static {
        defaultExpressions = new Vector();
        defaultExpressions.add("( <condition> ? exp1 : exp2 )");
        defaultExpressions.add("msg(<pattern>, <arg0>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>)");
        defaultExpressions.add("msg(<pattern>, <arg0>, <arg1>, <arg2>)");
        defaultExpressions.add("str(<key>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).subDataSource(<select expression>)");
        defaultExpressions.add("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).dataSource(<select expression>)");
    }
        
   /** Creates new form ExpressionEditor */
   public ExpressionEditor()
   {
      super( MainFrame.getMainInstance(), true);
      initComponents();
      this.setSize(750, 450);
      
      this.setModal(true);
      //this.setModal(true);
      org.syntax.jedit.SyntaxDocument sd = new org.syntax.jedit.SyntaxDocument();
      sd.setTokenMarker(new org.syntax.jedit.tokenmarker.JavaTokenMarker() );
      
      this.jEditTextArea1.setDocument( sd );
      this.jEditTextArea1.getPainter().setEOLMarkersPainted(false);
      this.jEditTextArea1.getPainter().setInvalidLinesPainted(false);
      this.jEditTextArea1.getPainter().setLineHighlightEnabled(false);
        
      
      DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode("root");
      
      this.jTree1.setCellRenderer(new DocumentExpressionEditorTreeCellRenderer());
      this.jTree1.setModel(new javax.swing.tree.DefaultTreeModel(dmtn));
      
      it.businesslogic.ireport.util.Misc.centerFrame(this);
      
      jList1.setModel(new DefaultListModel());
      jList2.setModel(new DefaultListModel());

      jEditTextArea1.requestFocusInWindow();
      
      jEditTextArea1.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextChanged();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextChanged();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                jRTextExpressionAreaTextChanged();
            }
        });
        
        jEditTextArea1.addCaretListener( this );
        
      
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        jList1.getSelectionModel().setSelectionMode( jList1.getSelectionModel().SINGLE_SELECTION );
        jList1.setCellRenderer(new ExpObjectCellRenderer(jList1));
        jList2.setCellRenderer(new TextAreaCellRenderer(jList2));
        
        //to make the default button ...
        //this.getRootPane().setDefaultButton(this.jButtonOK);
        jSplitPane3.updateUI();
        applyI18n();
        caretUpdate(null);
        
        jTabbedPane1.addChangeListener( new ChangeListener() {
            public void stateChanged(ChangeEvent e) 
            {
                if (jTabbedPane1.getSelectedIndex() == 1)
                {
                    SwingUtilities.invokeLater( new Runnable() {
                    
                        public void run()
                        {
                            try {
                                validateExpression();
                            } catch (Exception ex)
                            {
                                setErrorText("Exception occurred during the expression validation", true);
                            }
                        }
                    });
                }
            }
        });
        
        doc = new DefaultStyledDocument();
        
        jTextPaneErrors.setDocument( doc );
        errorStyle = doc.addStyle("errorStyle", null);
        StyleConstants.setForeground(errorStyle, Color.red);
        okStyle = doc.addStyle("okStyle", null);
        StyleConstants.setForeground(okStyle, Color.blue);
        
        this.pack();
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
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jEditTextArea1 = new org.syntax.jedit.JEditTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabelCaretPosition = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPaneOAE = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPaneErrors = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jButtonLoadExpression = new javax.swing.JButton();
        jButtonSaveExpression = new javax.swing.JButton();
        jButtonCheckExpression = new javax.swing.JButton();
        jButtonApply = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setTitle("Expression editor...");
        setModal(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(752, 416));
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jEditTextArea1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jEditTextArea1.setFocusCycleRoot(true);
        jEditTextArea1.setFont(new java.awt.Font("Courier New", 0, 11));
        jEditTextArea1.setMinimumSize(new java.awt.Dimension(20, 20));
        jEditTextArea1.setPreferredSize(new java.awt.Dimension(750, 150));
        jEditTextArea1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jEditTextArea1ComponentResized(evt);
            }
        });
        jEditTextArea1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jEditTextArea1InputMethodTextChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(jEditTextArea1, gridBagConstraints);

        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText("   ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel6.add(jLabel1, gridBagConstraints);

        jLabelCaretPosition.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelCaretPosition.setText("Line: 1   Column: 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel6.add(jLabelCaretPosition, gridBagConstraints);

        jSplitPane1.setTopComponent(jPanel6);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jSplitPaneOAE.setBorder(null);
        jSplitPaneOAE.setDividerSize(4);
        jSplitPaneOAE.setResizeWeight(0.1);
        jSplitPaneOAE.setAutoscrolls(true);
        jSplitPaneOAE.setPreferredSize(new java.awt.Dimension(361, 160));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(100, 100));
        jSplitPane3.setBorder(null);
        jSplitPane3.setDividerSize(4);
        jSplitPane3.setResizeWeight(0.8);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jSplitPane3.setLeftComponent(jScrollPane1);

        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(jList2);

        jSplitPane3.setRightComponent(jScrollPane3);

        jPanel4.add(jSplitPane3, java.awt.BorderLayout.CENTER);

        jSplitPaneOAE.setRightComponent(jPanel4);
        jPanel4.getAccessibleContext().setAccessibleParent(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 100));
        jTree1.setRootVisible(false);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });

        jScrollPane2.setViewportView(jTree1);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPaneOAE.setLeftComponent(jPanel3);
        jPanel3.getAccessibleContext().setAccessibleParent(jPanel1);

        jTabbedPane1.addTab("Objects and expressions", jSplitPaneOAE);

        jTextPaneErrors.setEditable(false);
        jScrollPane4.setViewportView(jTextPaneErrors);

        jTabbedPane1.addTab("Validation errors", jScrollPane4);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jTabbedPane1, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel5);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 30));
        jButtonLoadExpression.setLabel("Import...");
        jButtonLoadExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadExpressionActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jButtonLoadExpression, gridBagConstraints);

        jButtonSaveExpression.setText("Export...");
        jButtonSaveExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveExpressionActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jButtonSaveExpression, gridBagConstraints);

        jButtonCheckExpression.setText("Check expression");
        jButtonCheckExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckExpressionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jButtonCheckExpression, gridBagConstraints);

        jButtonApply.setMnemonic('a');
        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jButtonApply, gridBagConstraints);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jButtonCancel, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCheckExpressionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckExpressionActionPerformed

        SwingUtilities.invokeLater( new Runnable() {
                    
                        public void run()
                        {
                            try {
                                validateExpression();
                            } catch (Exception ex)
                            {
                                setErrorText("Exception occurred during the expression validation", true);
                            }
                        }
                    });
    }//GEN-LAST:event_jButtonCheckExpressionActionPerformed

    private void jButtonSaveExpressionActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveExpressionActionPerformed1
        Misc.saveExpression( jEditTextArea1.getText(), this );
    }//GEN-LAST:event_jButtonSaveExpressionActionPerformed1

    private void jButtonLoadExpressionActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadExpressionActionPerformed1
        String expression = Misc.loadExpression(this);
        
        if (expression != null) {
            jEditTextArea1.setText(expression);
        }
    }//GEN-LAST:event_jButtonLoadExpressionActionPerformed1

    static int xxx = 0;
    public void jRTextExpressionAreaTextChanged()
    {
        if (init) return;
        jLabel1.setText(" ");
        SwingUtilities.invokeLater(new Runnable(){
           public void run()
            {
            try {
                bsh.Parser parser = new bsh.Parser(new StringReader(jEditTextArea1.getText()+";") );
                while(!parser.Line()) {
                        //SimpleNode node = parser.popNode();
                        // use the node, etc. (See bsh.BSH* classes)
                    }

                } catch (Throwable ex)
                {
                    jLabel1.setText("" + ex.getMessage());
                }
            }
        });
        
    }
    private void jEditTextArea1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jEditTextArea1InputMethodTextChanged

        
    }//GEN-LAST:event_jEditTextArea1InputMethodTextChanged

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked

        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            if (jList1.getSelectedValue() != null && jList2.getSelectedValue() != null)
            {
                try {
                    
                    String objName = "";
                    if (jList1.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList1.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList1.getSelectedValue();
                    }
                    
                    String method = (jList2.getSelectedValue()+"");
                    method = method.substring(0, method.lastIndexOf(")")+1);
                    jEditTextArea1.getDocument().replace(jEditTextArea1.getSelectionStart(), 
                                             jEditTextArea1.getSelectionEnd()-jEditTextArea1.getSelectionStart(), 
                                             objName+"."+method, null);
            } catch (Exception ex){}
            }
        }
    }//GEN-LAST:event_jList2MouseClicked

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        
        if (evt.getButton() == evt.BUTTON1 && evt.getClickCount() == 2)
        {
            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if ((""+dmtn.getUserObject()).equalsIgnoreCase(I18n.getString("Wizards","Wizards")))
            {
                ArithmeticOperationHelper aoe = new ArithmeticOperationHelper();

                aoe.setLanguage( MainFrame.getMainInstance().getActiveReportFrame().getReport().getLanguage() );
                
                if ((jList1.getSelectedValue()+"").equals( I18n.getString("Addition","Addition (+)")))
                {
                    aoe.setOperation("+");
                }
                if ((jList1.getSelectedValue()+"").equals( I18n.getString("Subtraction","Subtraction (-)")))
                {
                    aoe.setOperation("-");
                }
                if ((jList1.getSelectedValue()+"").equals( I18n.getString("Division","Division (/)")))
                {
                    aoe.setOperation("/");
                }
                if ((jList1.getSelectedValue()+"").equals( I18n.getString("Multiplication","Multiplication (*)")))
                {
                    aoe.setOperation("*");
                }
                
                // Collect all objects...
                aoe.setValues( getAllObjects() );
                
                if ( aoe.showDialog(this) == JOptionPane.OK_OPTION)
                {

                    String exp = aoe.getExpression();
                    try {
                    jEditTextArea1.getDocument().replace(jEditTextArea1.getSelectionStart(), 
                                                 jEditTextArea1.getSelectionEnd()-jEditTextArea1.getSelectionStart(), 
                                                 exp, null);
                    } catch (Exception ex){}
                }
            }
            else
            {
                try {
                    String objName = "";
                    if (jList1.getSelectedValue() instanceof ExpObject)
                    {
                        objName = ((ExpObject)jList1.getSelectedValue()).getExpression();
                    }
                    else
                    {
                        objName = ""+jList1.getSelectedValue();
                    }
                    
                    jEditTextArea1.getDocument().replace(jEditTextArea1.getSelectionStart(), 
                                                     jEditTextArea1.getSelectionEnd()-jEditTextArea1.getSelectionStart(), 
                                                     objName+"", null);
                    } catch (Exception ex){}
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        
        DefaultListModel dlm = (DefaultListModel)jList2.getModel();
        dlm.removeAllElements();
        
        Class clazz = null; //getSelectedObjectClass();
        
        if (jList1.getSelectedValue() instanceof ExpObject)
        {
            try {
                clazz = this.getClass().getClassLoader().loadClass( ((ExpObject)jList1.getSelectedValue()).getClassType());
        
            } catch (Throwable ex)
            {
                
            }
        }
        
        
        
        if (clazz != null)
        {
            java.lang.reflect.Method[] methods = clazz.getMethods();
            for (int i=0; i<methods.length; ++i)
            {
                if ((methods[i].getModifiers() & java.lang.reflect.Modifier.PUBLIC) != 0 )
                {
                    String method_firm = methods[i].getName() + "(";
                    Class[] params = methods[i].getParameterTypes();
                    int j=0;
                    for (j=0; j<params.length; ++j)
                    {
                        
                        if (j > 0) method_firm +=", ";
                        else method_firm +=" ";
                        method_firm +=  getPrintableTypeName( params[j].getName() );
                    }
                    if (j>0) method_firm+=" ";
                    method_firm += ") ";

                    String rname = methods[i].getReturnType().getName();
                    if (rname.equals("void")) continue; // we have to return something always!
                    method_firm += getPrintableTypeName( rname);
                    dlm.addElement( method_firm );
                }
            }
        }
        
        
    }//GEN-LAST:event_jList1ValueChanged
        
    public String getPrintableTypeName( String type )
    {
            if (type == null) return "void";

            if (type.endsWith(";")) type = type.substring(0,type.length()-1);
    
            while (type.startsWith("["))
            {
                type = type.substring(1) + "[]";
                if (type.startsWith("[")) continue;
                if (type.startsWith("L")) type = type.substring(1);
                if (type.startsWith("Z")) type = "boolean" + type.substring(1);
                if (type.startsWith("B")) type = "byte" + type.substring(1);
                if (type.startsWith("C")) type = "char" + type.substring(1);
                if (type.startsWith("D")) type = "double" + type.substring(1);
                if (type.startsWith("F")) type = "float" + type.substring(1);
                if (type.startsWith("I")) type = "int" + type.substring(1);
                if (type.startsWith("J")) type = "long" + type.substring(1);
                if (type.startsWith("S")) type = "short" + type.substring(1);
            }
            
            if (type.startsWith("java.lang."))
            {
                type = type.substring("java.lang.".length());
                if (type.indexOf(".") > 0)
                {
                    type = "java.lang." + type;
                }
            }
            return type;
    }
    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
       
       ( (DefaultListModel)jList1.getModel()).removeAllElements();
       
       //if (getSubDataset() == null) return;
       DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
       if (dmtn.getUserObject() instanceof CrosstabReportElementWrapper)
       {
           CrosstabReportElement cr = ((CrosstabReportElementWrapper)dmtn.getUserObject()).getCrosstabReportElement();
           Enumeration e = cr.getMeasures().elements();
           while (e.hasMoreElements())
           {
             Measure measure = (Measure)e.nextElement();
             //( (DefaultListModel)jList1.getModel()).addElement( "$V{"+measure+"}" );
             ( (DefaultListModel)jList1.getModel()).addElement( new ExpObject(measure.getName(), ExpObject.TYPE_VARIABLE, measure.getClassType() ));
             
             for (int j=0; j<cr.getRowGroups().size(); ++j)
             {
                CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(j);
                //((DefaultListModel)jList1.getModel()).addElement( "$V{"+measure+"_"+group.getName()+"_"+"ALL}");
                ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(measure.getName() + "_"+group.getName()+"_"+"ALL", ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
             }
             for (int j=0; j<cr.getColumnGroups().size(); ++j)
             {
                CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(j);
                //((DefaultListModel)jList1.getModel()).addElement( "$V{"+measure+"_"+group.getName()+"_"+"ALL}");
                ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(measure.getName() + "_"+group.getName()+"_"+"ALL", ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
             }
           }
           
           for (int i=0; i<cr.getRowGroups().size(); ++i)
           {
                CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(i);
                //( (DefaultListModel)jList1.getModel()).addElement( "$V{"+group +"}" );
                ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(group.getName(), ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
           }
            
           for (int i=0; i<cr.getColumnGroups().size(); ++i)
           {
                CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(i);
                //( (DefaultListModel)jList1.getModel()).addElement( "$V{"+group +"}" );
                ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(group.getName(), ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
           }
           

           for (int i=0; i<cr.getCrosstabParameters().size(); ++i)
           {
               CrosstabParameter parameter = (CrosstabParameter)cr.getCrosstabParameters().elementAt(i);
               //( (DefaultListModel)jList1.getModel()).addElement( "$P{"+parameter +"}" );
               ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(parameter.getName(), ExpObject.TYPE_PARAM, parameter.getClassType() ));
           }

       }
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Fields") && getSubDataset() != null)
       {
          Enumeration e = getSubDataset().getFields().elements();
          while (e.hasMoreElements())
          {
              JRField field = (JRField)e.nextElement();
             //( (DefaultListModel)jList1.getModel()).addElement( "$F{"+e.nextElement() +"}" );
             ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(field.getName(), ExpObject.TYPE_FIELD, field.getClassType() ));
          }
       }
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Parameters") && getSubDataset() != null)
       {
          Enumeration e = getSubDataset().getParameters().elements();
          while (e.hasMoreElements())
          {
              JRParameter parameter = (JRParameter)e.nextElement();
             //( (DefaultListModel)jList1.getModel()).addElement( "$P{"+ e.nextElement()+"}" );
             ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(parameter.getName(), ExpObject.TYPE_PARAM, parameter.getClassType() ));
             
          }
       }
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Variables") && getSubDataset() != null)
       {
          Enumeration e = getSubDataset().getVariables().elements();
          while (e.hasMoreElements())
          {
             JRVariable variable = (JRVariable)e.nextElement();
             //( (DefaultListModel)jList1.getModel()).addElement( "$V{"+e.nextElement() +"}" );
             ((DefaultListModel)jList1.getModel()).addElement( new ExpObject(variable.getName(), ExpObject.TYPE_VARIABLE, variable.getClassType() ));
          }
       }
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase(I18n.getString("Formulas","Formulas" )))
       {
           for (int i=0; i<defaultExpressions.size(); ++i)
           {
               ( (DefaultListModel)jList1.getModel()).addElement(defaultExpressions.elementAt(i));
           }
       } 
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase(I18n.getString("RecentExpressions","Recent expressions")))
       {
           for (int i=0; i<recentExpressions.size(); ++i)
           {
               ( (DefaultListModel)jList1.getModel()).addElement(recentExpressions.elementAt(i));
           }
       }
       else if ((""+dmtn.getUserObject()).equalsIgnoreCase(I18n.getString("Wizards","Wizards")))
       {
           // Adding wizards...
           ( (DefaultListModel)jList1.getModel()).addElement(I18n.getString("Addition","Addition (+)"));
           ( (DefaultListModel)jList1.getModel()).addElement(I18n.getString("Subtraction","Subtraction (-)"));
           ( (DefaultListModel)jList1.getModel()).addElement(I18n.getString("Division","Division (/)"));
           ( (DefaultListModel)jList1.getModel()).addElement(I18n.getString("Multiplication","Multiplication (*)"));
       }
    }//GEN-LAST:event_jTree1ValueChanged
    
    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
       //this.setExpression( this.jEditTextArea1.getText() );
       this.setDialogResult( javax.swing.JOptionPane.NO_OPTION );
       this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    private void jButtonApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
       this.setExpression( this.jEditTextArea1.getText() );
       
       recentExpressions.add( this.jEditTextArea1.getText() );
       
       this.setDialogResult( javax.swing.JOptionPane.OK_OPTION );
       this.dispose();
    }//GEN-LAST:event_jButtonApplyActionPerformed
    
    private void jEditTextArea1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jEditTextArea1ComponentResized
       // Add your handling code here:
    }//GEN-LAST:event_jEditTextArea1ComponentResized
    
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
       //this.jEditTextArea1.recalculateVisibleLines();
       this.jEditTextArea1.updateScrollBars();
    }//GEN-LAST:event_formComponentResized
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
       
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
       new ExpressionEditor().setVisible(true);
    }
    

    /** Getter for property Expression.
     * @return Value of property Expression.
     *
     */
    public java.lang.String getExpression()
    {
       return Expression;
    }
    
    /** Setter for property Expression.
     * @param Expression New value of property Expression.
     *
     */
    public void setExpression(java.lang.String Expression)
    {
        init = true;
       this.jEditTextArea1.setText( Expression );
       this.Expression = Expression;
       init = false;
    }
    
    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult()
    {
       return dialogResult;
    }
    
    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult)
    {
       this.dialogResult = dialogResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonApply;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCheckExpression;
    private javax.swing.JButton jButtonLoadExpression;
    private javax.swing.JButton jButtonSaveExpression;
    private org.syntax.jedit.JEditTextArea jEditTextArea1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCaretPosition;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPaneOAE;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPaneErrors;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
    
   
    private String Expression;
    
    private int dialogResult;

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
        
        // Selezioniamo il campo fields....
        if (subDataset != null)
        {
            jEditTextArea1.getTokenMarker().setKeywordLookup(subDataset.getKeywordLookup());
        }
        updateTreeEntries();
        
    }
    
    public void updateTreeEntries() {
        
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTree1.getModel().getRoot();
        
        root.removeAllChildren();
        
        if (getSubDataset() != null)
        {
            root.add( new DefaultMutableTreeNode("Fields"));
            root.add( new DefaultMutableTreeNode("Variables"));
            root.add( new DefaultMutableTreeNode("Parameters"));
        }
        
        for (int i=0; i<getCrosstabElements().size(); ++i)
        {
            root.add( new DefaultMutableTreeNode( new CrosstabReportElementWrapper( (CrosstabReportElement)getCrosstabElements().get(i))));
        }        
        root.add( new DefaultMutableTreeNode(I18n.getString("Formulas","Formulas" )));
        
        root.add( new DefaultMutableTreeNode(I18n.getString("RecentExpressions","Recent expressions")));
        
        root.add( new DefaultMutableTreeNode(new IconedString(I18n.getString("Wizards","Wizards"),"/it/businesslogic/ireport/icons/wand.png" )));
        
        jTree1.expandPath(new TreePath(root));
        jTree1.updateUI();    
        jTree1.setSelectionRow(0);
    }

    public Vector getCrosstabElements() {
        return crosstabElements;
    }

    public void setCrosstabElements(Vector crosstabElements) {
        this.crosstabElements = crosstabElements;
    }
    
    
    public void addCrosstabReportElement(CrosstabReportElement re)
    {
        if (!this.getCrosstabElements().contains(re))
        {
            getCrosstabElements().add(re);
            updateTreeEntries();
        }
    }
    
    /**
     * Set an expression contex. If the passes expression context is null,
     * nothing is done...
     * The context replace all previos settings
     */
    public void setExpressionContext(ExpressionContext ec)
    {
        if ( ec == null) return;
        setCrosstabElements(ec.getCrosstabReportElements());
        
        setSubDataset(ec.getSubDataset());
    }
    
    /*
    private Class getSelectedObjectClass()
    {
        try {
            if (jList1.getSelectedValue() == null) return null;
            String s = (String)jList1.getSelectedValue();

            if (s == null) return null;

            DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)jTree1.getSelectionPath().getLastPathComponent();
            if (dmtn.getUserObject() instanceof CrosstabReportElementWrapper)
            {
                    // look for the object here....
                    CrosstabReportElement cr = ((CrosstabReportElementWrapper)dmtn.getUserObject()).getCrosstabReportElement();
                   Enumeration e = cr.getMeasures().elements();
                   while (e.hasMoreElements())
                   {
                     Measure measure = (Measure)e.nextElement();
                     if (s.equals( "$V{"+measure+"}" ))
                     {
                         return this.getClass().getClassLoader().loadClass( measure.getClassType() );
                     }

                     for (int j=0; j<cr.getRowGroups().size(); ++j)
                     {
                        CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(j);
                        if (s.equals( "$V{"+measure+"_"+group.getName()+"_"+"ALL}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( measure.getClassType() );
                        }
                     }
                     for (int j=0; j<cr.getColumnGroups().size(); ++j)
                     {
                        CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(j);
                        if (s.equals( "$V{"+group+"_"+group.getName()+"_"+"ALL}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( group.getBucketExpressionClass());
                        }
                     }
                   }

                   for (int i=0; i<cr.getRowGroups().size(); ++i)
                   {
                        CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(i);
                        if (s.equals( "$V{"+group+"}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( group.getBucketExpressionClass());
                        }
                   }

                   for (int i=0; i<cr.getColumnGroups().size(); ++i)
                   {
                        CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(i);
                        if (s.equals( "$V{"+group+"}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( group.getBucketExpressionClass());
                        }
                   }

                   for (int i=0; i<cr.getCrosstabParameters().size(); ++i)
                   {
                       CrosstabParameter parameter = (CrosstabParameter)cr.getCrosstabParameters().elementAt(i);
                       if (s.equals( "$P{"+parameter +"}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( parameter.getClassType());
                        }
                   }
            }
            else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Fields") && getSubDataset() != null)
           {
              Enumeration e = getSubDataset().getFields().elements();
              while (e.hasMoreElements())
              {
                  JRField f = (JRField)e.nextElement();
                  if (s.equals( "$F{"+ f +"}"))
                        {
                            // should be always a number?
                             return this.getClass().getClassLoader().loadClass( f.getClassType());
                        }
              }
           }
           else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Parameters") && getSubDataset() != null)
           {
              Enumeration e = getSubDataset().getParameters().elements();
              while (e.hasMoreElements())
              {
                 JRParameter p = (JRParameter)e.nextElement();
                  if (s.equals( "$P{"+ p +"}"))
                    {
                        // should be always a number?
                         return this.getClass().getClassLoader().loadClass( p.getClassType());
                    }
              }
           }
           else if ((""+dmtn.getUserObject()).equalsIgnoreCase("Variables") && getSubDataset() != null)
           {
              Enumeration e = getSubDataset().getVariables().elements();
              while (e.hasMoreElements())
              {
                 JRVariable v = (JRVariable)e.nextElement();
                  if (s.equals( "$V{"+ v +"}"))
                    {
                        // should be always a number?
                         return this.getClass().getClassLoader().loadClass(v.getClassType());
                    }
              }
           }
        
        } catch (Throwable ex)
        {
            ex.printStackTrace();
            return null;
        }
        
        return null;
    }
 */
    public void caretUpdate(CaretEvent e) {
        
       MessageFormat formatter = new MessageFormat("");
       formatter.applyPattern( I18n.getString("LineColumn","Line {0,number,integer}, Column {1,number,integer}" ));

       if (jEditTextArea1.getCaretLine() < 0)
       {
           jLabelCaretPosition.setText(formatter.format(new Object[]{new Integer(0), new Integer(0)}));
       }
       String s = jEditTextArea1.getText();
       
       int pos = jEditTextArea1.getCaretPosition();
       int newLineStart = s.substring(0,jEditTextArea1.getCaretPosition()).lastIndexOf('\n')+1;
       pos = pos - newLineStart;
       pos++;
       
       jLabelCaretPosition.setText(formatter.format(new Object[]{new Integer((jEditTextArea1.getCaretLine()+1)), new Integer(pos)}));
    }
   
    
    public void applyI18n()
    {
                // Start autogenerated code ----------------------
                jLabelCaretPosition.setText(I18n.getString("expressionEditor.labelCaretPosition","Line: 1   Column: 1"));
                // End autogenerated code ----------------------
        jButtonLoadExpression.setText(it.businesslogic.ireport.util.I18n.getString("import", "Import..."));
        jButtonSaveExpression.setText(it.businesslogic.ireport.util.I18n.getString("export", "Export..."));
        jButtonCheckExpression.setText(it.businesslogic.ireport.util.I18n.getString("checkExpression", "Check expression"));
        jButtonApply.setText(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ButtonApply", "Apply"));
        jButtonCancel.setText(it.businesslogic.ireport.util.I18n.getString("gui.OptionsDialog.ButtonCancel", "Cancel"));
        jTabbedPane1.setTitleAt(0,it.businesslogic.ireport.util.I18n.getString("ObjectsAndExpressions", "Objects and expressions") );
        jTabbedPane1.setTitleAt(1,it.businesslogic.ireport.util.I18n.getString("ValidationErrors", "Validation errors") );
    
    
        jTabbedPane1.setTitleAt(0, it.businesslogic.ireport.util.I18n.getString("expressionEditor.tab.ObjectsAndExpressions","Objects and expressions"));
        jTabbedPane1.setTitleAt(1, it.businesslogic.ireport.util.I18n.getString("expressionEditor.tab.ValidationErrors","Validation errors"));
                this.setTitle(I18n.getString("expressionEditor.title","Expression editor..."));
                jButtonCancel.setMnemonic(I18n.getString("expressionEditor.buttonCancelMnemonic","c").charAt(0));
                jButtonApply.setMnemonic(I18n.getString("expressionEditor.buttonApplyMnemonic","a").charAt(0));                  
    }    
    
    public void validateExpression()
    {
        String expression_to_validate = jEditTextArea1.getText();
        
        // 1. replace all $Parameters, $Variables, $Fields and $R expressions 
        JRDesignExpression jrExpression = new JRDesignExpression();
        jrExpression.setText( expression_to_validate );
        jTextPaneErrors.setText("");
        
        try {
            
            java.util.List paramsObjects = new java.util.ArrayList();
            java.util.List varsObjects = new java.util.ArrayList();
            java.util.List fieldsObjects = new java.util.ArrayList();
            
            String final_espression = generateExpression(jrExpression, paramsObjects, fieldsObjects, varsObjects);

            if ( MainFrame.getMainInstance().getActiveReportFrame().getReport().getLanguage().equals("groovy"))
            {
                Binding binding = new Binding();
                for (int i=0; i<paramsObjects.size(); ++i)
                {
                    String name = ""+paramsObjects.get(i);
                    binding.setVariable(name, new JRFakeObject());
                }
                for (int i=0; i<varsObjects.size(); ++i)
                {
                    String name = ""+varsObjects.get(i);
                    binding.setVariable(name, new JRFakeObject());
                }
                for (int i=0; i<fieldsObjects.size(); ++i)
                {
                    String name = ""+fieldsObjects.get(i);
                    binding.setVariable(name, new JRFakeObject());
                }
                GroovyShell shell = new GroovyShell(binding);
                
                shell.parse(final_espression);

            }
            else if ( MainFrame.getMainInstance().getActiveReportFrame().getReport().getLanguage().equals("java"))
            {
                bsh.Interpreter interpreter = new bsh.Interpreter();
            
                StringTokenizer  st = new StringTokenizer( MainFrame.getMainInstance().getProperties().getProperty("classpath",""),"\n");
                interpreter.eval("String tmp;");
                while (st.hasMoreTokens())
                {
                    String token = st.nextToken();
                    if (token != null && token.trim().length() > 0)
                    {
                        interpreter.set("tmp", token.trim());
                        interpreter.eval("addClassPath(tmp);");
                    }
                }        

                // Add report import directives to the bsh interpreter
                Enumeration imps = MainFrame.getMainInstance().getActiveReportFrame().getReport().getImports().elements();
                while ( imps.hasMoreElements() )
                {
                    String var = (String)imps.nextElement();
                    interpreter.eval("import " + var + ";");
                }

                interpreter.eval("import java.util.*;");
                interpreter.eval("import java.math.*;");
                interpreter.eval("import java.text.*;");
                interpreter.eval("import java.io.*;");
                interpreter.eval("import java.net.*;");
                interpreter.setStrictJava(true);

                for (int i=0; i<paramsObjects.size(); ++i)
                {
                    String name = ""+paramsObjects.get(i);
                    interpreter.set(name, new JRFakeObject());
                }
                for (int i=0; i<varsObjects.size(); ++i)
                {
                    String name = ""+varsObjects.get(i);
                    interpreter.set(name, new JRFakeObject());
                }
                for (int i=0; i<fieldsObjects.size(); ++i)
                {
                    String name = ""+fieldsObjects.get(i);
                    interpreter.set(name, new JRFakeObject());
                }

                interpreter.eval(final_espression);
            }
            setErrorText(it.businesslogic.ireport.util.I18n.getString("ValidationOK", "Expression successfully validated."),false);
        
        
        } catch (CompilationFailedException ex)
        {
            setErrorText( ex.getMessage() , true);
        } catch (ParseException ex)
        {
            setErrorText(ex.getMessage() + "\n" + ex.getErrorText() + "\nLine: " + ex.getErrorLineNumber(), true );
            
        } catch (TargetError ex)
        {
            // What to say... we tried!
            setErrorText(it.businesslogic.ireport.util.I18n.getString("ValidationOK", "Expression successfully validated."), true);
        
        } catch (EvalError ex)
        {
            setErrorText(ex.getMessage() + "\n" + ex.getErrorText() + "\nLine: " + ex.getErrorLineNumber(), true );
        } catch (IRParsingException ex)
        {
            setErrorText(  ex.getMessage() , true);
            if (ex.getChunk() != null)
            {
                int pos = expression_to_validate.indexOf( ex.getChunk()  );
                if (pos >=0)
                {
                    jEditTextArea1.setSelectionStart(pos);
                    jEditTextArea1.setSelectionEnd(pos);
                    jEditTextArea1.setSelectionEnd(pos + ex.getChunk().length());
                }
            }
        } catch (Exception ex)
        {
            setErrorText(ex.getMessage(), true);
            ex.printStackTrace();
        }
        
        jTabbedPane1.setSelectedIndex(1);
    }
    
    private String generateExpression(JRExpression expression, 
                                      java.util.List paramsObjects, 
                                      java.util.List fieldsObjects, 
                                      java.util.List varsObjects) throws IRParsingException
	{
		net.sf.jasperreports.engine.JRParameter jrParameter = null;
		net.sf.jasperreports.engine.JRField jrField = null;
		net.sf.jasperreports.engine.JRVariable jrVariable = null;

		StringBuffer sb = new StringBuffer();

		JRExpressionChunk[] chunks = expression.getChunks();
		JRExpressionChunk chunk = null;
		String chunkText = null;
		if (chunks != null && chunks.length > 0)
		{
			for(int i = 0; i < chunks.length; i++)
			{
				chunk = chunks[i];

				chunkText = chunk.getText();
				if (chunkText == null)
				{
					chunkText = "";
				}
				
				switch (chunk.getType())
				{
					case JRExpressionChunk.TYPE_TEXT :
					{
						sb.append(chunkText);
						break;
					}
					case JRExpressionChunk.TYPE_PARAMETER :
					{
                                                // Look for the given parameter...
						JRParameter param = (JRParameter)getExpressionObject(chunkText, PARAMETER);
	
						sb.append("((");
						sb.append(param.getClassType());
						sb.append(")");
                                                String vname = "parameter_" + JRStringUtil.getLiteral(chunkText);
						sb.append(vname);
                                                paramsObjects.add(vname);
						sb.append(".getValue())");
	
						break;
					}
					case JRExpressionChunk.TYPE_FIELD :
					{
                                                JRField field = (JRField)getExpressionObject(chunkText, FIELD);
						
						sb.append("((");
						sb.append(field.getClassType());
						sb.append(")");
                                                String vname = "field_" + JRStringUtil.getLiteral(chunkText);
						sb.append(vname);
                                                fieldsObjects.add(vname);
						sb.append(".get");
						//sb.append((String)fieldPrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_VARIABLE :
					{
						JRVariable variable = (JRVariable)getExpressionObject(chunkText, VARIABLE);
	
						sb.append("((");
						sb.append(variable.getClassType());
						sb.append(")"); 
						String vname = "variable_" + JRStringUtil.getLiteral(chunkText);
						sb.append(vname);
                                                varsObjects.add(vname);
						sb.append(".get");
						//sb.append((String)variablePrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_RESOURCE :
					{
						sb.append("str(\"");
						sb.append(chunkText);
						sb.append("\")");
	
						break;
					}
				}
			}
		}
		
		if (sb.length() == 0)
		{
			sb.append("null");
		}

		return sb.toString();
	}
        
    private Object getExpressionObject(String name, int type) throws IRParsingException
    {
        if (name == null)  throw new IRParsingException("Object not found!");
        
        SubDataset subDataset = getSubDataset();
        if (getSubDataset() == null && getCrosstabElements().size() == 0)
        {
            subDataset = MainFrame.getMainInstance().getActiveReportFrame().getReport();
        }
        
        if (type == PARAMETER)
        {
            if (subDataset != null)
            {
                Enumeration e = subDataset.getParameters().elements();
                while (e.hasMoreElements())
                {
                    JRParameter p = (JRParameter)e.nextElement();
                    if (p.getName().equals(name)) return p;
                }
            }
            
            // Try to look in some crosstab...
            for (int k=0; k<crosstabElements.size(); ++k)
            {
                //----------------------------------------------
                CrosstabReportElement cr = (CrosstabReportElement)crosstabElements.get(k);
   
                   for (int i=0; i<cr.getCrosstabParameters().size(); ++i)
                   {
                       CrosstabParameter parameter = (CrosstabParameter)cr.getCrosstabParameters().elementAt(i);
                       if (name.equals( ""+parameter) )
                       {
                            // should be always a number?
                           return new JRParameter(parameter.getName(), parameter.getClassType());
                       }
                   }
            //-----------------------------------------------------    
       
            }
            // Parameter not found!!!
            
            throw new IRParsingException("Parameter " + name + " not found!", "$P{" + name + "}");
        }
        else if (type == FIELD)
        {
            if (subDataset != null)
            {
                Enumeration e = subDataset.getFields().elements();
                while (e.hasMoreElements())
                {
                    JRField p = (JRField)e.nextElement();
                    if (p.getName().equals(name)) return p;
                }
            }
            // Parameter not found!!!
            throw new IRParsingException("Field " + name + " not found!", "$F{" + name + "}");
        }
        else if (type == VARIABLE)
        {
            if (subDataset != null)
            {
                Enumeration e = subDataset.getVariables().elements();
                while (e.hasMoreElements())
                {
                    JRVariable p = (JRVariable)e.nextElement();
                    if (p.getName().equals(name)) return p;
                }
            }
            
            // Try to look in some crosstab...
            for (int k=0; k<crosstabElements.size(); ++k)
            {
                //----------------------------------------------
                CrosstabReportElement cr = (CrosstabReportElement)crosstabElements.get(k);
                Enumeration e = cr.getMeasures().elements();
                   while (e.hasMoreElements())
                   {
                     Measure measure = (Measure)e.nextElement();
                     if (name.equals( measure.getName() ))
                     {
                         return new JRVariable(measure.getName(), measure.getClassType(),false);
                     }

                     for (int j=0; j<cr.getRowGroups().size(); ++j)
                     {
                        CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(j);
                        if (name.equals( ""+measure+"_"+group.getName()+"_"+"ALL"))
                        {
                            // should be always a number?
                            return new JRVariable(measure+"_"+group.getName(), group.getBucketExpressionClass(),false);
                        }
                     }
                     for (int j=0; j<cr.getColumnGroups().size(); ++j)
                     {
                        CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(j);
                        if (name.equals( ""+group+"_"+group.getName()+"_"+"ALL"))
                        {
                            // should be always a number?
                             return new JRVariable(measure+"_"+group.getName(), group.getBucketExpressionClass(),false);
                        }
                     }
                   }

                   for (int i=0; i<cr.getRowGroups().size(); ++i)
                   {
                        CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(i);
                        if (name.equals( ""+group))
                        {
                            // should be always a number?
                             return new JRVariable(""+group, group.getBucketExpressionClass(),false);
                        }
                   }

                   for (int i=0; i<cr.getColumnGroups().size(); ++i)
                   {
                        CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(i);
                        if (name.equals( ""+group))
                        {
                            // should be always a number?
                             return new JRVariable(""+group, group.getBucketExpressionClass(),false);
                        }
                   }

                   
            //-----------------------------------------------------    
       
            }
            // Parameter not found!!!
            throw new IRParsingException("Variable " + name + " not found!", "$F{" + name + "}");
        }
        
        throw new IRParsingException("Object " + name + " not found!", name);
    }
    
    public void setErrorText(String msg, boolean isError)
    {
        jTextPaneErrors.setText("");
        try {
        doc.insertString(doc.getLength(), msg, (isError) ? errorStyle : okStyle);
        } catch (Exception ex){}
    }
    
    
    public java.util.List getAllObjects()
    {
        java.util.List list = new java.util.ArrayList();
        if (getSubDataset() != null)
        {
            Enumeration e = getSubDataset().getFields().elements();
            while (e.hasMoreElements())
            {
                JRField f = (JRField)e.nextElement();
                list.add(new ExpObject(f.getName(), ExpObject.TYPE_FIELD, f.getClassType() ));
            }
            
            e = getSubDataset().getVariables().elements();
            while (e.hasMoreElements())
            {
                JRVariable f = (JRVariable)e.nextElement();
                list.add(new ExpObject(f.getName(), ExpObject.TYPE_VARIABLE, f.getClassType() ));
            }
            
            e = getSubDataset().getParameters().elements();
            while (e.hasMoreElements())
            {
                JRParameter f = (JRParameter)e.nextElement();
                list.add(new ExpObject(f.getName(), ExpObject.TYPE_PARAM, f.getClassType() ));
            }
        }
        for (int ct=0; ct<getCrosstabElements().size(); ++ct)
        {
             CrosstabReportElement cr = (CrosstabReportElement)getCrosstabElements().get(ct);
             Enumeration e = cr.getMeasures().elements();
             while (e.hasMoreElements())
             {
                Measure measure = (Measure)e.nextElement();
                list.add(new ExpObject(measure.getName(), ExpObject.TYPE_VARIABLE, measure.getClassType() ));
                
                for (int j=0; j<cr.getRowGroups().size(); ++j)
                {
                    CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(j);
                    list.add(new ExpObject(measure.getName()+"_"+group.getName()+"_"+"ALL", ExpObject.TYPE_VARIABLE, measure.getClassType() ));
                }
                for (int j=0; j<cr.getColumnGroups().size(); ++j)
                {
                    CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(j);
                    list.add(new ExpObject(measure.getName()+"_"+group.getName()+"_"+"ALL", ExpObject.TYPE_VARIABLE, measure.getClassType() ));
                }
            }
             
            for (int i=0; i<cr.getRowGroups().size(); ++i)
           {
                CrosstabGroup group = (CrosstabGroup)cr.getRowGroups().elementAt(i);
                list.add(new ExpObject(group.getName(), ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
           }
            
           for (int i=0; i<cr.getColumnGroups().size(); ++i)
           {
                CrosstabGroup group = (CrosstabGroup)cr.getColumnGroups().elementAt(i);
                list.add(new ExpObject(group.getName(), ExpObject.TYPE_VARIABLE, group.getBucketExpressionClass() ));
           }
           

           for (int i=0; i<cr.getCrosstabParameters().size(); ++i)
           {
               CrosstabParameter parameter = (CrosstabParameter)cr.getCrosstabParameters().elementAt(i);
               list.add(new ExpObject(parameter.getName(), ExpObject.TYPE_PARAM, parameter.getClassType() ));
           }
        }
        
        return list;
    }
    
}

