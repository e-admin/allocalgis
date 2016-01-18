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
 * ReportQueryDialog.java
 * 
 * Created on 27 maggio 2003, 19.47
 *
 */

package it.businesslogic.ireport.gui;
import bsh.EvalError;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.data.BeanInspectorPanel;
import it.businesslogic.ireport.data.CincomMDXFieldsProvider;
import it.businesslogic.ireport.data.EJBQLFieldsProvider;
import it.businesslogic.ireport.data.HQLFieldsProvider;
import it.businesslogic.ireport.data.MDXFieldsProvider;
import it.businesslogic.ireport.data.SQLFieldsProvider;
import it.businesslogic.ireport.gui.queryexecuters.QueryExecuterDef;
import it.businesslogic.ireport.gui.subdataset.FilterExpressionDialog;
import it.businesslogic.ireport.gui.subdataset.SortFieldsDialog;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.table.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;
import bsh.Interpreter;
import it.businesslogic.ireport.data.XMLFieldsProvider;
import it.businesslogic.ireport.gui.dnd.FieldsContainer;
import it.businesslogic.ireport.gui.dnd.FieldsContainerTransferHandler;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.gui.table.CustomColumnControlButton;

import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.datatransfer.*;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.table.ColumnControlButton;

/** 
 * A dialog which allows the user to enter a SQL query and then choose the
 * fields to use in the report.
 *
 * @author <a href="mailto:gt78@users.sourceforge.net">Giulio Toffoli</a>
 * @author <a href="mailto:phenderson@users.sourceforge.net">Peter Henderson</a>
 */
public class ReportQueryDialog extends javax.swing.JDialog implements ClipboardOwner, FieldsContainer {
    
    private BeanInspectorPanel bip1 = null;
    
    private FieldsProvider fieldsProvider = null;
        
    protected static String standard_types[]= new String[]{
        "java.lang.String",
	"java.lang.Object",
	"java.lang.Boolean",
	"java.lang.Byte",
	"java.util.Date",
	"java.sql.Timestamp",
	"java.sql.Time",
	"java.lang.Double",
	"java.lang.Float",
	"java.lang.Integer",
	"java.io.InputStream",
	"java.lang.Long",
	"java.lang.Short",
	"java.math.BigDecimal"    
    };
    
    public FieldReader readerThread = null;
    public static int num = 1;
    
    public JLabel getJLabelStatusSQL()
    {
        return this.jLabelStatusSQL;
    }
    
    /** Creates new form ReportQueryFrame */
    public ReportQueryDialog(java.awt.Frame parent, boolean modal) {
        
        super(parent, modal);
        initComponents();
        this.setSize(800, 550);
        Misc.centerFrame(this);
        
        stoppedChanging.setRepeats(false);
        
        jRSQLExpressionArea1.getDocument().addDocumentListener( new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
            public void insertUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
            public void removeUpdate(DocumentEvent e) {
                if(isSettingSQLExpression)return;
                //okButton.setEnabled(false);
                stoppedChanging.restart();
            }
        } );
        
        setColumnsError( "Please open a report." );
        if (MainFrame.getMainInstance().getProperties().getProperty("beanClass") != null)
        {
            jTextFieldBeanClass1.setText( MainFrame.getMainInstance().getProperties().getProperty("beanClass") +"");
        }
        
        jComboBoxQueryType.addItem( new Tag("sql","SQL"));
        jComboBoxQueryType.addItem( new Tag("hql","Hibernate Query Language (HQL)"));
        jComboBoxQueryType.addItem( new Tag("xPath","XPath"));
        jComboBoxQueryType.addItem( new Tag("ejbql","EJBQL"));
        jComboBoxQueryType.addItem( new Tag("mdx","MDX"));
        jComboBoxQueryType.addItem( new Tag("xmla-mdx","XMLA-MDX"));
        
        Enumeration e = MainFrame.getMainInstance().getQueryExecuters().elements();
        while (e.hasMoreElements())
        {
            QueryExecuterDef qe = (QueryExecuterDef)e.nextElement();
            jComboBoxQueryType.addItem( new Tag(qe,qe.getLanguage()));
        } 
        
        okButton.setEnabled(false);
        
        bip1 = new BeanInspectorPanel();
        bip1.setComboVisible(false);
        bip1.setJTableFields( jTableFields );
        bip1.setPathOnDescription(true);
        jPanel11.add(bip1, BorderLayout.CENTER);
                
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        
        applyI18n();
        
        I18n.addOnLanguageChangedListener( new LanguageChangedListener() {
            public void languageChanged(LanguageChangedEvent evt) {
                applyI18n();
            }
        } );

        FieldsContainerTransferHandler fcth = new FieldsContainerTransferHandler(this);
        jTableFields.setTransferHandler( fcth );
        columnsErrorMsgLabel.setTransferHandler( fcth);
        columnsScrollPane.setTransferHandler(fcth);
        columnsErrorScrollPane.setTransferHandler( fcth);
        //to make the default button ...
        //this.getRootPane().setDefaultButton(this.jButtonOK);
        
        
        jTableFields.setColumnControl(new CustomColumnControlButton(jTableFields, new ColumnControlIcon() ));
        
        
    }
    
    /**
     * A timer to detect when the SQL expression area has not been changed, for
     * a short moment. This is to prevent the database being hit with every
     * with every key press.
     */
    javax.swing.Timer stoppedChanging = new javax.swing.Timer( 500, new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if( automaticlyReadFieldsCheckBox.isSelected() ) {
                processQueryChanged( jRSQLExpressionArea1.getText().trim() );
            }
        }
    } );
    
    
    /**
     * Given a database query string, extract the database columns, then display
     * them. If there was a problem loading the list of columns, show an error
     * panel which contains the reason why.
     *
     * @param query The SQL query string, which can contain JasperReports parameters. 
     */
    private void processQueryChanged( String query ) {
        
        if (isSettingSQLExpression) return;
        
        //System.out.println("processQueryChanged");
        //Thread.currentThread().dumpStack();
        
        if (subDataset == null) {
            setColumnsError( "Please open a report." );
            return;
        }
        
        if (query.length() == 0) {
            setColumnsError( "You must insert a valid query first" );
            return;
        }

        IReportConnection conn = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        
        Object obj = jComboBoxQueryType.getSelectedItem();
        String queryLanguage = "sql";
        if (obj != null && obj instanceof Tag) 
        {
            queryLanguage = ""+((Tag)obj).getValue();
        }
        else
        {
            queryLanguage = ""+obj;
        }      
        
        try {
            // Run the query in the backgroud as it is not quick.
            if (readerThread != null && readerThread.isAlive())
            {
                readerThread.interrupt();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();

        }

        readerThread = new FieldReader(query, conn, queryLanguage);
        readerThread.start();
    }
    
    public static int elaborationSequence = 0;
    
    /**
     * A Thread class to extract field names from a SQL query.
     *
     */
    class FieldReader extends Thread {
        String src_query;
        String src_query_language = "sql";
        IReportConnection conn;
       
        int elaborationID = 0;
        
        /**
         * ctor.
         * @param query The query to read the field from
         * @param conn The IRport DB connection to use.
         */
        public FieldReader(String query, IReportConnection conn) {
            this(query, conn, "sql");
        }
        
        /**
         * ctor.
         * @param query The query to read the field from
         * @param conn The IRport DB connection to use.
         */
        public FieldReader(String query, IReportConnection conn, String query_language) {
            src_query=query;
            this.conn=conn;
            this.src_query_language = query_language;
            ReportQueryDialog.elaborationSequence++;
            elaborationID = ReportQueryDialog.elaborationSequence;
        }
        
        
        
        /**
         * Set the fields table data to the supplied data.
         * This is called from a none swing thread, hence all the invoke and
         * wait magic. If the current thread is the AWT Event Dispacher, no
         * invoke and wait is call.
         * The columns are only set if the query string matches the one the 
         * results are for.
         *
         * @param columns The list of columns to set.
         */
        public void setColumnsFromWorker( final List columns ) {
            try {
                
                Runnable r = new Runnable() {
                        public void run() {
                            String str = jRSQLExpressionArea1.getText().trim();
                            if( str.compareTo(src_query)==0 ) {
                                setColumns( columns );
                            }
                        }
                    };
                    
                if (!SwingUtilities.isEventDispatchThread())
                {
                    SwingUtilities.invokeAndWait( r );
                }
                else
                {
                        r.run();
                }
                    
            } catch(Exception e) {
                // oh well we got interrupted.
            }
        }
        
        /**
         * Set the columns error message.
         * This is called from a none swing thread, hence all the invoke and
         * wait magic.
         * The message is only set if the query string matches the one the 
         * error message is for.
         *
         * @param columns The list of columns to set.
         */
        public void setColumnErrorFromWork( final String error_msg ) {
            try {
                
                Runnable r = new Runnable() {
                    public void run() {
                        String str = jRSQLExpressionArea1.getText().trim();
                        if( str.compareTo(src_query)==0 ) {
                            setColumnsError( error_msg );
                            jRSQLExpressionArea1.requestFocusInWindow();
                        }
                        
                    }
                };
                
                if (SwingUtilities.isEventDispatchThread())
                {
                    r.run();
                }
                else
                {
                    SwingUtilities.invokeAndWait( r );
                }
            } catch(Exception e) {
                // oh well we got interrupted.
            }
        }
        
        public String interpretQuery()
        {
            
            String query = this.src_query;
            try {
                Interpreter interpreter = prepareExpressionEvaluator();

                // look for parameters in the query and replace them with default values.
                // parameters look something like 
                // $P{QuoteGroupID}
                // or 
                // $P!{OrderByClause}
                java.util.List queryParams = new ArrayList();
                Enumeration enumParams = subDataset.getParameters().elements();
                while( enumParams.hasMoreElements() ) {
                    it.businesslogic.ireport.JRParameter parameter = (it.businesslogic.ireport.JRParameter)enumParams.nextElement();

                    String p1 = "$P{" + parameter.getName() + "}";
                    String p2 = "$P!{" + parameter.getName() + "}";

                    // evaluate the Default expression value
                    
                    // Integer expID = (Integer)parameterNameToExpressionID.get(parameter.getName());
                    
                    Object defValue;
                    if(  parameter.getDefaultValueExpression() != null &&  !parameter.getDefaultValueExpression().equals("") ) {
                        defValue = recursiveInterpreter( interpreter, parameter.getDefaultValueExpression(), subDataset.getParameters(),0,parameter.getName());
                        // interpreter.eval("bshCalculator.evaluate(" + expID.intValue() + ")");
                    } else {
                        // this param does not have a default value.
                        defValue = null;
                    }


                    int ip1 = query.indexOf(p1);
                    while( ip1!=-1 ) {
                        // String replacement, Altering the SQL statement.
                        if( defValue==null ) {
                            throw new IllegalArgumentException("Please set a " +
                                "default value for the parameter '" 
                                + parameter.getName() + "'" );
                        }

                        String before = query.substring(0, ip1);
                        String after = query.substring(ip1+p1.length());
                        if (parameter.getClassType().equals("java.lang.String"))
                        {
                            String stt = defValue.toString();
                            stt = it.businesslogic.ireport.util.Misc.string_replace("''","'", stt);
                            query = before + "'" + stt + "'" + after;
                        }
                        else query = before + "" + defValue.toString() + "" + after;
                        
                        ip1 = query.indexOf(p1);
                    }

                    int ip2 = query.indexOf(p2);
                    while( ip2!=-1 ) {
                        // String replacement, Altering the SQL statement.
                        if( defValue==null ) {
                            throw new IllegalArgumentException("Please set a " +
                                "default value for the parameter '" 
                                + parameter.getName() + "'" );
                        }

                        String before = query.substring(0, ip2);
                        String after = query.substring(ip2+p2.length());
                        query = before + "" + defValue.toString() + "" + after;
                        ip2 = query.indexOf(p2);
                    }
                }
            
                return query;
            } catch (Exception ex)
            {
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
                return "";
            }
        }
        
        public void run() {
            
            ((JXBusyLabel)ReportQueryDialog.this.getJLabelStatusSQL()).setBusy(true);
            
            try {
                String error_msg = "";
                num++;
                int in = num;

                FieldsProvider fp = getFieldsProvider();
                if (fp == null) return;

                if (fp.hasEditorComponent())
                {
                    FieldsProviderEditor editor = (FieldsProviderEditor)fp.getEditorComponent(ReportQueryDialog.this);
                    editor.queryChanged(this.src_query);
                }

                if (in < num) return; //Abort, new execution requested

                if (fp.supportsGetFieldsOperation())
                {
                    try {

                        // Create the JasperReport object
                        Report rep = MainFrame.getMainInstance().getActiveReportFrame().getReport();

                        net.sf.jasperreports.engine.design.JasperDesign report = new net.sf.jasperreports.engine.design.JasperDesign();
                        JRDesignQuery queryDes = new JRDesignQuery();
                        queryDes.setText( this.src_query );
                        queryDes.setLanguage( this.src_query_language );
                        report.setQuery( queryDes);

                        for (int i=0; i< rep.getJRproperties().size(); ++i)
                        {
                            JRProperty property = (JRProperty)rep.getJRproperties().elementAt(i);
                            report.setProperty(property.getName(), property.getValue());
                        }

                        Interpreter interpreter = prepareExpressionEvaluator();
                        HashMap parametersValues = new HashMap();

                        for (int i=0; i< rep.getParameters().size(); ++i)
                        {
                            JRParameter ireportParam = (JRParameter)rep.getParameters().elementAt(i);
                            if (ireportParam.isBuiltin()) continue;
                            net.sf.jasperreports.engine.design.JRDesignParameter  param = new net.sf.jasperreports.engine.design.JRDesignParameter();
                            param.setName(  ireportParam.getName() );
                            JRDesignExpression de = new JRDesignExpression();
                            de.setText( ireportParam.getDefaultValueExpression() );
                            de.setValueClassName( ireportParam.getClassType() );
                            param.setDefaultValueExpression( de );
                            param.setForPrompting(ireportParam.isIsForPrompting() );
                            param.setValueClassName(  ireportParam.getClassType() );
                            report.addParameter( param );

                            Object defValue;
                            if(  ireportParam.getDefaultValueExpression() != null &&  !ireportParam.getDefaultValueExpression().equals("") ) {

                                defValue = recursiveInterpreter( interpreter, ireportParam.getDefaultValueExpression(), subDataset.getParameters(),0, ireportParam.getName());
                                //defValue = interpreter.eval( parameter.getDefaultValueExpression() );
                                // interpreter.eval("bshCalculator.evaluate(" + expID.intValue() + ")");
                            } else {
                                // this param does not have a default value.
                                defValue = null;
                            }

                            parametersValues.put(ireportParam.getName(), defValue);
                        }

                        try {
                            report.setName(rep.getName());
                        } catch (Exception ex)
                        {}

                        if (in < num) return; //Abort, new execution requested

                        // Create a temporary JasperReports object...
                        net.sf.jasperreports.engine.JRField[] jrFields =  fp.getFields( this.conn, report.getMainDataset() , parametersValues);

                        if (in < num) return; //Abort, new execution requested

                        List columns = new ArrayList();
                        for (int i=0; i<jrFields.length; ++i)
                        {
                            JRField field = new JRField( jrFields[i].getName(),jrFields[i].getValueClassName() );
                            field.setDescription( jrFields[i].getDescription() );
                            columns.add( new Object[]{field, field.getClassType(), field.getDescription()} );
                        }

                        if (in < num) return; //Abort, new execution requested

                        setColumnsFromWorker(columns);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        setColumnErrorFromWork( "Error: " +  ex.getMessage() );
                    }
                    getJLabelStatusSQL().setText("Ready");
                }

                /////////////////////
            }
            finally
            {
                if (elaborationID == ReportQueryDialog.elaborationSequence)
                {
                    ((JXBusyLabel)ReportQueryDialog.this.getJLabelStatusSQL()).setBusy(false);
                }
            }
        }
    } // Class end
    
    
    
     
    /**
     * Shows the list of columns.
     * If the column error message label is visible remove it first.
     *
     * @param cols A List Object[], for the fields.
     */
    public void setColumns( final List cols ) {
        
        columnsErrorMsgLabel.setText( "" );
        jPanel2.remove( columnsErrorScrollPane );
        jPanel2.add( columnsScrollPane, java.awt.BorderLayout.CENTER );
        jPanel2.revalidate();
        
        
        javax.swing.table.DefaultTableModel dtm =  (javax.swing.table.DefaultTableModel)jTableFields.getModel();
        dtm.getDataVector().clear();
        for(int i=0; i<cols.size(); i++) {
            Object [] row = (Object[])cols.get(i);
            dtm.addRow( row );
        }
        
        // Select all the fields so the new user does not get confused, when
        // they press OK. As only the selected fields are actually saved to the
        // report
        jTableFields.selectAll();
        
        okButton.setEnabled( true );
    }
    
    /**
     * Replace the columns list with a label that contains the reason why
     * columns cannot be loaded.
     *
     * @param msg The error message to display, can be in HTML.
     */
    public void setColumnsError( final String msg ) {
        columnsErrorMsgLabel.setText( msg );
        jPanel2.remove( columnsScrollPane );
        jPanel2.add( columnsErrorScrollPane, java.awt.BorderLayout.CENTER );
        jPanel2.revalidate();
        columnsErrorMsgLabel.repaint();
        
        //okButton.setEnabled(false);
    }

    private FieldsProvider getFieldsProvider() {
        return fieldsProvider;
    }

    private void setFieldsProvider(FieldsProvider fieldsProvider) {
        this.fieldsProvider = fieldsProvider;
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelSQL = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxQueryType = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jButtonLoadQuery = new javax.swing.JButton();
        jButtonSaveQuery = new javax.swing.JButton();
        jPanelQueryArea = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel7 = new javax.swing.JPanel();
        jRSQLExpressionArea1 = new it.businesslogic.ireport.gui.JRSQLExpressionArea();
        jLabelStatusSQL = new org.jdesktop.swingx.JXBusyLabel();
        automaticlyReadFieldsCheckBox = new javax.swing.JCheckBox();
        readFieldsButton = new javax.swing.JButton();
        jButtonOpenDesigner = new javax.swing.JButton();
        exportQueryButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jTextFieldBeanClass1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonReadBeanAttributes3 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        columnsErrorScrollPane = new javax.swing.JScrollPane();
        columnsErrorMsgLabel = new javax.swing.JLabel();
        columnsScrollPane = new javax.swing.JScrollPane();
        jTableFields = new org.jdesktop.swingx.JXTable();

        setTitle("Report query");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);

        jPanel1.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 350));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(154, 350));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(154, 350));

        jPanelSQL.setMinimumSize(new java.awt.Dimension(1, 100));
        jPanelSQL.setPreferredSize(new java.awt.Dimension(1, 350));
        jPanelSQL.setLayout(new java.awt.GridBagLayout());

        jPanel14.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Query language");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel14.add(jLabel1, gridBagConstraints);

        jComboBoxQueryType.setEditable(true);
        jComboBoxQueryType.setMinimumSize(new java.awt.Dimension(200, 18));
        jComboBoxQueryType.setPreferredSize(new java.awt.Dimension(200, 22));
        jComboBoxQueryType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxQueryTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        jPanel14.add(jComboBoxQueryType, gridBagConstraints);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        jButtonLoadQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/folder_database.png"))); // NOI18N
        jButtonLoadQuery.setText("Load query");
        jButtonLoadQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonLoadQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel16.add(jButtonLoadQuery, gridBagConstraints);

        jButtonSaveQuery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/menu/database_save.png"))); // NOI18N
        jButtonSaveQuery.setText("Save query");
        jButtonSaveQuery.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonSaveQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel16.add(jButtonSaveQuery, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel14.add(jPanel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelSQL.add(jPanel14, gridBagConstraints);

        jPanelQueryArea.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerSize(6);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jRSQLExpressionArea1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRSQLExpressionArea1.setMinimumSize(new java.awt.Dimension(50, 200));
        jRSQLExpressionArea1.setPreferredSize(new java.awt.Dimension(661, 340));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jRSQLExpressionArea1, gridBagConstraints);

        jLabelStatusSQL.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel7.add(jLabelStatusSQL, gridBagConstraints);

        automaticlyReadFieldsCheckBox.setSelected(true);
        automaticlyReadFieldsCheckBox.setText("Automatically Retrieve Fields");
        automaticlyReadFieldsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticlyReadFieldsCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel7.add(automaticlyReadFieldsCheckBox, gridBagConstraints);

        readFieldsButton.setText("Read Fields");
        readFieldsButton.setEnabled(false);
        readFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readFieldsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(readFieldsButton, gridBagConstraints);

        jButtonOpenDesigner.setText("Query designer");
        jButtonOpenDesigner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenDesignerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(jButtonOpenDesigner, gridBagConstraints);

        exportQueryButton.setText("Send to clipboard");
        exportQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportQueryButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        jPanel7.add(exportQueryButton, gridBagConstraints);

        jSplitPane2.setLeftComponent(jPanel7);

        jPanelQueryArea.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        jPanelSQL.add(jPanelQueryArea, gridBagConstraints);

        jTabbedPane1.addTab("Report query ", jPanelSQL);

        jPanel8.setMinimumSize(new java.awt.Dimension(235, 30));
        jPanel8.setPreferredSize(new java.awt.Dimension(215, 30));
        jPanel8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel8.add(jTextFieldBeanClass1, gridBagConstraints);

        jLabel3.setText("Class name");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 5, 3);
        jPanel8.add(jLabel3, gridBagConstraints);

        jButtonReadBeanAttributes3.setText("Read attributes");
        jButtonReadBeanAttributes3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadBeanAttributes3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanel8.add(jButtonReadBeanAttributes3, gridBagConstraints);

        jPanel11.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        jPanel8.add(jPanel11, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(0, 4));
        jSeparator1.setPreferredSize(new java.awt.Dimension(0, 4));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel8.add(jSeparator1, gridBagConstraints);

        jTabbedPane1.addTab("JavaBean Datasource", jPanel8);

        jButton2.setText("Get fields from datasource");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2);

        jTabbedPane1.addTab("DataSource Provider", jPanel6);

        jButton3.setText("Get fields from datasource");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed1(evt);
            }
        });
        jPanel15.add(jButton3);

        jTabbedPane1.addTab("CSV Datasource", jPanel15);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(453, 150));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(10, 34));
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 34));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(150, 10));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        okButton.setText("OK");
        okButton.setEnabled(false);
        okButton.setMaximumSize(new java.awt.Dimension(67, 25));
        okButton.setMinimumSize(new java.awt.Dimension(67, 25));
        okButton.setPreferredSize(new java.awt.Dimension(67, 25));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel4.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(cancelButton, gridBagConstraints);

        jPanel3.add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel9.setMinimumSize(new java.awt.Dimension(100, 20));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Filter expression...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel9.add(jButton1, gridBagConstraints);

        jButton4.setText("Sort options...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel9.add(jButton4, gridBagConstraints);

        jPanel3.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        columnsErrorMsgLabel.setText("jLabel1");
        columnsErrorMsgLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        columnsErrorMsgLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        columnsErrorScrollPane.setViewportView(columnsErrorMsgLabel);

        jPanel2.add(columnsErrorScrollPane, java.awt.BorderLayout.CENTER);

        jTableFields.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Field name", "Field type", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableFields.setColumnControlVisible(true);
        jTableFields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFieldsMouseClicked(evt);
            }
        });
        jTableFields.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableFieldsKeyReleased(evt);
            }
        });
        columnsScrollPane.setViewportView(jTableFields);

        jPanel2.add(columnsScrollPane, java.awt.BorderLayout.LINE_START);

        jSplitPane1.setBottomComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jTableFieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFieldsMouseClicked
    if (SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() == 2)
    {
        if (this.jTableFields.getSelectedRow() >= 0)
        {
            JRField field = (JRField)this.jTableFields.getValueAt( this.jTableFields.getSelectedRow(), 0);
            
            JRFieldDialog jrpd = new JRFieldDialog(MainFrame.getMainInstance(), true);
            jrpd.setSubDataset( this.getSubDataset() );
            jrpd.setField(field);
            jrpd.setVisible(true);
            
            
            
            if (jrpd.getDialogResult() == JOptionPane.OK_OPTION)
            {
                field = jrpd.getField();
                DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                int index = jTableFields.convertRowIndexToModel( this.jTableFields.getSelectedRow() );
                dtm.setValueAt(field,  index, 0);
                dtm.setValueAt(field.getClassType(),  index, 1);
                dtm.setValueAt(field.getDescription(),  index, 2);
                this.jTableFields.updateUI();
            }
        }
    }
}//GEN-LAST:event_jTableFieldsMouseClicked

private void jTableFieldsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableFieldsKeyReleased
    //javax.swing.JOptionPane.showMessageDialog(null,"Key: "+evt.getKeyCode() + " (" + java.awt.event.KeyEvent.VK_DELETE + ")");
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)
        {
             javax.swing.table.DefaultTableModel dtm =  (javax.swing.table.DefaultTableModel)jTableFields.getModel();
             //int[] selectedRows = jTableFields.getSelectedRows();
             //for (int i= selectedRows.length-1; i>=0; --i) 
             //{
             //    it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField)this.jTableFields.getValueAt( i, 0);
                 //this.subDataset.removeField(field);
                 //this.jTableFields.removeRowSelectionInterval(i,i);
             //}
             while (jTableFields.getSelectedRow() >=0)
             {
                 dtm.removeRow(jTableFields.getSelectedRow());
             }
        }
}//GEN-LAST:event_jTableFieldsKeyReleased

    private void jButtonOpenDesignerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenDesignerActionPerformed

        if (getFieldsProvider() != null && 
            getFieldsProvider().hasQueryDesigner())
        {
            try {
                String newQuery = getFieldsProvider().designQuery((IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection"), jRSQLExpressionArea1.getText(), this );
                if (newQuery != null && !jRSQLExpressionArea1.getText().equals(newQuery))
                {
                    jRSQLExpressionArea1.setText( newQuery );
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        
        
    }//GEN-LAST:event_jButtonOpenDesignerActionPerformed

    private void jButton1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed1

        SortFieldsDialog sfd = new SortFieldsDialog( this, true);
        sfd.setSubDataset( this.getSubDataset() );
        sfd.setVisible(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed1

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        openFilterExpressionDialog(false);    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonSaveQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveQueryActionPerformed

       Misc.saveSQLQuery( jRSQLExpressionArea1.getText(), this );
        
    }//GEN-LAST:event_jButtonSaveQueryActionPerformed

    private void jButtonLoadQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadQueryActionPerformed

        
            String query = Misc.loadSQLQuery(this);
            
            if (query != null)
            {
                jRSQLExpressionArea1.setText(query);
            }
            
    }//GEN-LAST:event_jButtonLoadQueryActionPerformed

    private void jComboBoxQueryTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxQueryTypeActionPerformed
// TODO add your handling code here:
        boolean autoReadFields = automaticlyReadFieldsCheckBox.isSelected();
        
        readFieldsButton.setEnabled(false);
        automaticlyReadFieldsCheckBox.setSelected(false);
        readFieldsButton.setEnabled(false);
        
        String language = "sql";
        
        if (jComboBoxQueryType.getSelectedItem() != null &&
            jComboBoxQueryType.getSelectedItem() instanceof Tag)
        {
            language = ""+((Tag)jComboBoxQueryType.getSelectedItem()).getValue();
        }
        else if (jComboBoxQueryType.getSelectedItem() != null)
        {
            language = "" + jComboBoxQueryType.getSelectedItem();
        }
        
        
        // 1. Look for a special FieldsProvider....
        getJLabelStatusSQL().setText("Looking for a valid Fields provider for "  +  language + " queries....");
        /////////////////////////////
        setFieldsProvider(null);
            
        Enumeration enum_qe = MainFrame.getMainInstance().getQueryExecuters().elements();
        while (enum_qe.hasMoreElements())
        {
            QueryExecuterDef qed = (QueryExecuterDef)enum_qe.nextElement();
            if (qed.getLanguage().equals(language) && qed.getFieldsProvider() != null && qed.getFieldsProvider().length() > 0)
            {
                try {

                    // We have to set the query executer class...
                    net.sf.jasperreports.engine.util.JRProperties.setProperty("net.sf.jasperreports.query.executer.factory." + language, qed.getClassName());
                    setFieldsProvider( (FieldsProvider)Class.forName( qed.getFieldsProvider(),true,MainFrame.getMainInstance().getReportClassLoader()).newInstance() );
                } catch (Throwable t)
                {
                    getJLabelStatusSQL().setText("Error creating the fields provider "  +  t.getMessage());
                }
            }
        }
        
        exportQueryButton.setEnabled(language.equals("sql"));
        
        if (getFieldsProvider() == null && language.equals("sql"))
        {
            setFieldsProvider( new SQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && (language.toLowerCase().equals("xpath") || language.toLowerCase().equals("xpath2")))
        {
            setFieldsProvider( new XMLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("hql"))
        {
            setFieldsProvider( new HQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("ejbql"))
        {
            setFieldsProvider( new EJBQLFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("mdx"))
        {
            setFieldsProvider( new MDXFieldsProvider());
        }
        else if (getFieldsProvider() == null && language.equals("xmla-mdx"))
        {
            setFieldsProvider( new CincomMDXFieldsProvider());
        }
        
        boolean isSettingSQLExpressionOldValue = isSettingSQLExpression;
        isSettingSQLExpression = true;
        
        if (getFieldsProvider() == null)
        {
            setSpecialLanguageComponent( null );
            readFieldsButton.setEnabled(false );
            jButtonOpenDesigner.setEnabled( false );
            automaticlyReadFieldsCheckBox.setEnabled(false);
            automaticlyReadFieldsCheckBox.setSelected(autoReadFields);
            getJLabelStatusSQL().setText("No Fields Provider available for "  +  language + " queries!");
        }
        else
        {
            readFieldsButton.setEnabled( getFieldsProvider().supportsGetFieldsOperation() );
            automaticlyReadFieldsCheckBox.setEnabled( getFieldsProvider().supportsAutomaticQueryExecution() );
            automaticlyReadFieldsCheckBox.setSelected( autoReadFields );
            jButtonOpenDesigner.setEnabled( getFieldsProvider().hasQueryDesigner());
            if (getFieldsProvider().hasEditorComponent())
            {
                FieldsProviderEditor fpe = getFieldsProvider().getEditorComponent(this);
                if (fpe instanceof Component)
                {
                    setSpecialLanguageComponent( (Component)fpe );
                }
            }
            else
            {
                setSpecialLanguageComponent( null );
            }
            getJLabelStatusSQL().setText("Fields provider for "  +  language + " queries ready.");
        }
        isSettingSQLExpression = isSettingSQLExpressionOldValue;
        
    }//GEN-LAST:event_jComboBoxQueryTypeActionPerformed

    
    public void setSpecialLanguageComponent(Component c)
    {
        if (c == null)
        {
            if (jPanelQueryArea.getComponent(0) != jPanel7)
            {
                jPanelQueryArea.removeAll();
                jPanelQueryArea.add(jPanel7, BorderLayout.CENTER);
            }
        }
        else
        {
         
            if (jPanelQueryArea.getComponent(0) != jSplitPane2 ||
                jSplitPane2.getRightComponent() != c)
            {
                jPanelQueryArea.removeAll();
                jSplitPane2.setLeftComponent(jPanel7);
                jSplitPane2.setRightComponent( c );
                jPanelQueryArea.add(jSplitPane2, BorderLayout.CENTER);
            }
        }
        jPanelQueryArea.updateUI();
        jRSQLExpressionArea1.requestFocusInWindow();
        jRSQLExpressionArea1.requestFocus();
    }
    
    
    private void jButton2ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed1
        IReportConnection conn = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        if (conn == null || !(conn instanceof it.businesslogic.ireport.connection.JRCSVDataSourceConnection)) {
            setColumnsError( "The active connection is not a JasperReports CSV DataSource." );
            return;
        }
        else
        {
            it.businesslogic.ireport.connection.JRCSVDataSourceConnection ic = (it.businesslogic.ireport.connection.JRCSVDataSourceConnection)conn;
            try {
                Vector names = ic.getColumnNames();
                DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
                dtm.setRowCount(0);
            
                for (int nd =0; nd < names.size(); ++nd) {
                    String fieldName = ""+names.elementAt(nd);
                    it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, "java.lang.String");
                    field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                    
                    Vector row = new Vector();
                    row.addElement(field);
                    row.addElement(field.getClassType());
                    row.addElement(field.getDescription());
                    dtm.addRow(row);
                }
                jTableFields.setRowSelectionInterval(0, names.size()-1);
            } catch (Exception ex)
            {
                setColumnsError( "" + ex.getMessage() );
            
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed1

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
        IReportConnection conn = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
        if (conn == null || !(conn instanceof it.businesslogic.ireport.connection.JRDataSourceProviderConnection)) {
            setColumnsError( "The active connection is not a JasperReports DataSource provider." );
            return;
        }
        else
        {
            it.businesslogic.ireport.connection.JRDataSourceProviderConnection ic = (it.businesslogic.ireport.connection.JRDataSourceProviderConnection)conn;
            try {
                Report rep = MainFrame.getMainInstance().getActiveReportFrame().getReport();
                
                net.sf.jasperreports.engine.design.JasperDesign report = new net.sf.jasperreports.engine.design.JasperDesign();
                JRDesignQuery queryDes = new JRDesignQuery();
                queryDes.setText(jRSQLExpressionArea1.getText());
                 String queryLanguage = "sql";
                 Object obj = jComboBoxQueryType.getSelectedItem();
                if (obj != null && obj instanceof Tag) 
                {
                    queryLanguage = ""+((Tag)obj).getValue();
                }
                else
                {
                    queryLanguage = ""+obj;
                }     
                queryDes.setLanguage( queryLanguage );
                report.setQuery( queryDes);
                
                for (int i=0; i< rep.getJRproperties().size(); ++i)
                {
                    JRProperty property = (JRProperty)rep.getJRproperties().elementAt(i);
                    report.setProperty(property.getName(), property.getValue());
                }
                
                for (int i=0; i< rep.getParameters().size(); ++i)
                {
                    JRParameter ireportParam = (JRParameter)rep.getParameters().elementAt(i);
                    if (ireportParam.isBuiltin()) continue;
                    net.sf.jasperreports.engine.design.JRDesignParameter  param = new net.sf.jasperreports.engine.design.JRDesignParameter();
                    param.setName(  ireportParam.getName() );
                    JRDesignExpression de = new JRDesignExpression();
                    de.setText( ireportParam.getDefaultValueExpression() );
                    de.setValueClassName( ireportParam.getClassType() );
                    param.setDefaultValueExpression( de );
                    param.setForPrompting(ireportParam.isIsForPrompting() );
                    param.setValueClassName(  ireportParam.getClassType() );
                    report.addParameter( param );
                }
                
                try {
                    
                    report.setName(rep.getName());
                } catch (Exception ex)
                {
                    
                }
                
                // Create a temporary JasperReports object...
                net.sf.jasperreports.engine.JasperReport jr = new net.sf.jasperreports.engine.JasperReport(report,"",null,null,"");
                
            net.sf.jasperreports.engine.JRField[] jrfields = ic.getDataSourceProvider().getFields( jr );
            DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
            dtm.setRowCount(0);
            for (int i=0; i< jrfields.length; ++i)
            {
                it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(jrfields[i].getName(), jrfields[i].getValueClassName());
                field.setDescription( it.businesslogic.ireport.util.Misc.nvl( jrfields[i].getDescription(),""));
                Vector row = new Vector();
                row.addElement(field);
                row.addElement(field.getClassType());
                row.addElement(field.getDescription());
                dtm.addRow(row);
            }
            } catch (Exception ex)
            {
                setColumnsError( "" + ex.getMessage() );
            
            }
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void exportQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportQueryButtonActionPerformed
       
        FieldReader fr = new FieldReader(jRSQLExpressionArea1.getText(), null);
        String query = fr.interpretQuery();
        
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection fieldContent = new StringSelection (query);
        
     	clipboard.setContents (fieldContent, this);

        
        // TODO add your handling code here:
    }//GEN-LAST:event_exportQueryButtonActionPerformed

    private void jButtonReadBeanAttributes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadBeanAttributes3ActionPerformed
              
        bip1.setClassName(jTextFieldBeanClass1.getText().trim());
    }//GEN-LAST:event_jButtonReadBeanAttributes3ActionPerformed

    protected void getFieldsFromClass(Class clazz, String path) throws Exception
    {
         DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
            
         java.lang.reflect.Method[] methods = clazz.getMethods();
         java.lang.reflect.Field[] fields = clazz.getFields();
         // for any method, looking for get<FieldName> ....
            
            
         for (int i=0; i<methods.length; ++i)
         {
               
                if ( java.lang.reflect.Modifier.isPublic( methods[i].getModifiers() ) &&
                     methods[i].getDeclaringClass().getName().equals(clazz.getName() ) &&
                     !java.lang.reflect.Modifier.isNative( methods[i].getModifiers() )     
                     && methods[i].getName().startsWith("get")
                        && !methods[i].getReturnType().isPrimitive() 
                        && !methods[i].getReturnType().isArray())
                {
                   String fieldName = methods[i].getName().substring(3);
                   // Looking for the field...
                   for (int f=0; f<fields.length; ++f)
                   {
                       if (fields[f].getName().equalsIgnoreCase( fieldName ))
                       {
                           
                           fieldName = fields[f].getName();
                           break;
                       }
                   }
                   
                   String returnType =  methods[i].getReturnType().getName();
                   boolean found = false;
                   for (int cc=0; cc<standard_types.length; ++cc)
                   {
                        if ( returnType.equalsIgnoreCase(standard_types[cc]))
                        {                       
                            it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, returnType);
                            field.setDescription(path + "" + fieldName);
                            Vector row = new Vector();
                            row.addElement(field);
                            row.addElement(field.getClassType());
                            row.addElement(field.getDescription());
                            dtm.addRow(row);
                            found = true;
                            break;
                        }
                  }
                  if (!found)
                  {
                        it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, "java.lang.Object");
                        field.setDescription(path + "" + fieldName);
                        Vector row = new Vector();
                        row.addElement(field);
                        row.addElement(field.getClassType());
                        row.addElement(field.getDescription());
                        dtm.addRow(row);
                        Class subClazz = Class.forName(returnType);
                        getFieldsFromClass( subClazz , path + fieldName + ".");
                  }
                }
            }                    
    }
    


    
    private void automaticlyReadFieldsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticlyReadFieldsCheckBoxActionPerformed
        
        
        if( automaticlyReadFieldsCheckBox.isSelected() ) {
            // Automagically get quiery fields.
            // User has just enabled this so get field list.
            readFieldsButton.setEnabled(false);
            if (!isSettingSQLExpression)
            {
                processQueryChanged( jRSQLExpressionArea1.getText().trim() );
            }
        } else {
            // Turn off automagic field reading. User will have to press the 
            // Read Fields button
            //okButton.setEnabled(false);
            readFieldsButton.setEnabled(true);
            //setColumnsError( "Enter your query above. Then use the Read " +
            //        "Fields button to retrieve the list of fields." );
            this.jLabelStatusSQL.setText("Enter your query above. Then use the Read " +
                    "Fields button to retrieve the list of fields." );
        }
        
        MainFrame.getMainInstance().getProperties().setProperty("UseAutoRegiesterFields", "" + automaticlyReadFieldsCheckBox.isSelected());
        
    }//GEN-LAST:event_automaticlyReadFieldsCheckBoxActionPerformed

    private void readFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readFieldsButtonActionPerformed
        processQueryChanged( jRSQLExpressionArea1.getText().trim() );
    }//GEN-LAST:event_readFieldsButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // No changes.
        num++;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        
        try {
        if (stoppedChanging != null) stoppedChanging.stop();
  
        if ( this.getSubDataset() != null)
        {       
            num++; // avoid syncronization problems....
            
            Object obj = jComboBoxQueryType.getSelectedItem();
            if (obj != null && obj instanceof Tag) 
            {
                this.subDataset.setQueryLanguage(""+((Tag)obj).getValue());
            }
            else
            {
                this.subDataset.setQueryLanguage(""+obj);
            }
            // save the query to the report.
            this.subDataset.setQuery( jRSQLExpressionArea1.getText());                
            
            if ( jTableFields.getRowCount() > 0)
            {
                // Clear all the existing fields.
                this.subDataset.getFields().clear();
    
                // Add the new fields.

                //int[] selectedRows = jTableFields.getSelectedRows();
                //for (int i=0; i<selectedRows.length  ; ++i) {
                //    if (selectedRows[i] > jTableFields.getRowCount()) continue;

                //    it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField)this.jTableFields.getValueAt(selectedRows[i], 0);
                for (int i=0; i<jTableFields.getRowCount(); ++i)
                {
                    it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField)this.jTableFields.getValueAt(i, 0);
                    Enumeration e = this.subDataset.getFields().elements();
                    boolean found = false;
                    while (e.hasMoreElements()) {
                        it.businesslogic.ireport.JRField f = (it.businesslogic.ireport.JRField)e.nextElement();
                        if (f.getName().equalsIgnoreCase(field.getName())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        this.subDataset.addField(field);
                    }
                }
                if (subDataset instanceof Report)
                {
                    MainFrame.getMainInstance().getValuesDialog().getValuesPanel().updateFields();
                }
            } 
       }
       
       } catch (Throwable ex)
            {
                ex.printStackTrace();
            }
       
       this.setVisible(false);
        
    }//GEN-LAST:event_okButtonActionPerformed
    
    
    
    
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ReportQueryDialog(new javax.swing.JFrame(), true).setVisible(true);
    }
        
    Map parameterNameToExpressionID = null;
    
    /**
     * Create an expression evaluator for report parameters.
     *
     */
    private Interpreter prepareExpressionEvaluator() throws bsh.EvalError {
        
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(interpreter.getClass().getClassLoader());
        
        // Staring patch from rp4
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
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.fill.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import java.math.*;");
        interpreter.eval("import java.text.*;");
        interpreter.eval("import java.io.*;");
        interpreter.eval("import java.net.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.data.*;");
        
        Enumeration imps = MainFrame.getMainInstance().getActiveReportFrame().getReport().getImports().elements();
        while ( imps.hasMoreElements() )
        {
            String var = (String)imps.nextElement();
            interpreter.eval("import " + var + ";");
        }
        // End patch from rp4
        /*
        interpreter.eval(new StringReader(bshScript));

        interpreter.eval("bshCalculator = createBshCalculator()");
        */
        return interpreter;
         
        // return null;
    }

    /**
     * Convert a class name string into its class object.
     * There must be a function in JasperReports that does this somewhere.
     *
     *
     */
    private Class classStringToClass(String classType) {
        Class c = null;
        
        
        if ( classType.equals("java.lang.String") ) {
            c = java.lang.String.class;
        } else if ( classType.equals("java.lang.Integer") ) {
            c = java.lang.Integer.class;
        } else if ( classType.equals("java.lang.Boolean") ) {
            c = java.lang.Boolean.class;
        } else if ( classType.equals("java.lang.Byte") ) {
            c = java.lang.Byte.class;
        } else if ( classType.equals("java.util.Date") ) {
            c = java.util.Date.class;
        } else if ( classType.equals("java.sql.Timestamp") ) {
            c = java.sql.Timestamp.class;
        } else if ( classType.equals("java.sql.Time") ) {
            c = java.sql.Time.class;
        } else if ( classType.equals("java.lang.Double") ) {
            c = java.lang.Double.class;
        } else if ( classType.equals("java.lang.Float") ) {
            c = java.lang.Float.class;
        } else if ( classType.equals("java.lang.Long") ) {
            c = java.lang.Long.class;
        } else if ( classType.equals("java.lang.Short") ) {
            c = java.lang.Short.class;
        } else if ( classType.equals("java.math.BigDecimal") ) {
            c = java.math.BigDecimal.class;
        }
        
        return c;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox automaticlyReadFieldsCheckBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel columnsErrorMsgLabel;
    private javax.swing.JScrollPane columnsErrorScrollPane;
    private javax.swing.JScrollPane columnsScrollPane;
    private javax.swing.JButton exportQueryButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonLoadQuery;
    private javax.swing.JButton jButtonOpenDesigner;
    private javax.swing.JButton jButtonReadBeanAttributes3;
    private javax.swing.JButton jButtonSaveQuery;
    private javax.swing.JComboBox jComboBoxQueryType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private org.jdesktop.swingx.JXBusyLabel jLabelStatusSQL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelQueryArea;
    private javax.swing.JPanel jPanelSQL;
    private it.businesslogic.ireport.gui.JRSQLExpressionArea jRSQLExpressionArea1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.jdesktop.swingx.JXTable jTableFields;
    private javax.swing.JTextField jTextFieldBeanClass1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton readFieldsButton;
    // End of variables declaration//GEN-END:variables
    
    private boolean isSettingSQLExpression = false;
    
    
    
    public void lostOwnership (Clipboard parClipboard, Transferable parTransferable) { }

    public static Object recursiveInterpreter(Interpreter interpreter, String expression, Vector parameters) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, Vector parameters, int recursion_level) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0, null);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, Vector parameters, int recursion_level, String this_param_name) throws EvalError
    {
        ++recursion_level;
        
        if (expression == null || expression.length() == 0) return null;
        
        //System.out.println("Valuto ["+ recursion_level +"]: " + expression);
        if (recursion_level > 100) return null;
        if (expression != null && expression.trim().length() > 0)
        {
            // for each parameter, we have to calc the real value...
            while (expression.indexOf("$P{") >= 0)
            {
                int start_index = expression.indexOf("$P{")+3;
                String param_name = expression.substring(start_index, expression.indexOf("}", start_index) );
                String param_expression = "";
                for (int i=0; i<parameters.size(); ++i)
                {
                    JRParameter p = (JRParameter)parameters.elementAt(i);
                    if (p.getName().equals( param_name))
                    {
                        param_expression = p.getDefaultValueExpression();
                        break;
                    }
                }
                
                String param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getLiteral(param_name); 
                
                expression = Misc.string_replace( param_name_literal, "$P{"+param_name+"}", expression);
                //interpreter.set( param_name_literal, recursiveInterpreter(interpreter, param_expression, parameters, recursion_level));
            
                // If the parameter was never evaluated before, that can happen is some cases,
                // evaluate it now!
                if (interpreter.get(param_name_literal) == null)
                {
                    Object paramValue = recursiveInterpreter(interpreter, param_expression, parameters, recursion_level, this_param_name);
                    interpreter.set(param_name_literal, paramValue);
                }
            }
            
            String this_param_name_literal = "param_unknow";
            
            if (this_param_name!= null) 
            {
                this_param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getLiteral(this_param_name);
            } 
            //System.out.println("interpreto ["+ recursion_level +"]: " + expression);
            //System.out.flush();
            Object res = interpreter.eval(expression);
            interpreter.set(this_param_name_literal, res);
            //System.out.println("Result: " + res);
            //System.out.flush();
            return res;
        }
        return null;
    }

    private SubDataset subDataset;

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        
        isSettingSQLExpression = true;
        try { // Used only to perform a finally op
            this.subDataset = subDataset;

            DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
            dtm.setRowCount(0); 

            num++;
            jLabelStatusSQL.setText( "" );

            jRSQLExpressionArea1.setText("");

            try {
                automaticlyReadFieldsCheckBox.setSelected( Boolean.valueOf( MainFrame.getMainInstance().getProperties().getProperty("UseAutoRegiesterFields", "true") ).booleanValue());
                automaticlyReadFieldsCheckBoxActionPerformed(null);
            } catch (Exception ex)
            {

            }
            // Load query...
            if (subDataset == null)
                this.jRSQLExpressionArea1.setText("");
            else
            {
                // Use query, and use existing field list. ie Dont load from DB
                this.jRSQLExpressionArea1.setText( this.subDataset.getQuery() );

                List columns = new ArrayList();
                Iterator i = subDataset.getFields().iterator();
                while( i.hasNext() ) {
                    it.businesslogic.ireport.JRField field = (it.businesslogic.ireport.JRField)i.next();
                    columns.add( new Object[]{field, field.getClassType(), field.getDescription()} );
                }
                setColumns( columns );

                for (int ix=0; ix<jComboBoxQueryType.getItemCount(); ++ix)
                {
                   if (!(jComboBoxQueryType.getItemAt(ix) instanceof Tag))
                   {
                    jComboBoxQueryType.removeItemAt(ix);
                    ix--;
                   }
                }

                boolean found = false;
                for (int ix=0; ix<jComboBoxQueryType.getItemCount(); ++ix)
                {
                   Tag t = (Tag)jComboBoxQueryType.getItemAt(ix);
                   if (t.getValue().equals(subDataset.getQueryLanguage()))
                   {
                       found = true;
                       jComboBoxQueryType.setSelectedIndex(ix);
                       break;
                   }
                }
                if (!found) // Default is sql...
                {
                    jComboBoxQueryType.setEditable(true);
                    jComboBoxQueryType.setSelectedItem(subDataset.getQueryLanguage());
                }

                jLabelStatusSQL.setText("");
 
            }
        
        } finally {
            
            isSettingSQLExpression = false;
        }
    }
    
    public void applyI18n(){
                // Start autogenerated code ----------------------
                automaticlyReadFieldsCheckBox.setText(I18n.getString("reportQueryDialog.utomaticlyReadFieldsCheckBox","Automatically Retrieve Fields"));
                // End autogenerated code ----------------------
                // Start autogenerated code ----------------------
                cancelButton.setText(I18n.getString("reportQueryDialog.ancelButton","Cancel"));
                exportQueryButton.setText(I18n.getString("reportQueryDialog.xportQueryButton","Send to clipboard"));
                jButton1.setText(I18n.getString("reportQueryDialog.button1","Filter expression..."));
                jButton2.setText(I18n.getString("reportQueryDialog.button2","Get fields from datasource"));
                jButton3.setText(I18n.getString("reportQueryDialog.button3","Get fields from datasource"));
                jButton4.setText(I18n.getString("reportQueryDialog.button4","Sort options..."));
                jButtonLoadQuery.setText(I18n.getString("reportQueryDialog.buttonLoadQuery","Load query"));
                jButtonReadBeanAttributes3.setText(I18n.getString("reportQueryDialog.buttonReadBeanAttributes3","Read attributes"));
                jButtonSaveQuery.setText(I18n.getString("reportQueryDialog.buttonSaveQuery","Save query"));
                jLabel1.setText(I18n.getString("reportQueryDialog.label1","Query language"));
                jLabel3.setText(I18n.getString("reportQueryDialog.label3","Class name"));
                okButton.setText(I18n.getString("reportQueryDialog.kButton","OK"));
                readFieldsButton.setText(I18n.getString("reportQueryDialog.eadFieldsButton","Read Fields"));
                // End autogenerated code ----------------------
                
                jTableFields.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.fieldName","Field name") );
                jTableFields.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.fieldType","Field type") );
                jTableFields.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("reportQueryDialog.tablecolumn.description","Description") );
    
                jTabbedPane1.setTitleAt(0,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.ReportQuery", "Report query"));
                jTabbedPane1.setTitleAt(1,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.JavaBeanDatasource", "JavaBean Datasource"));
                jTabbedPane1.setTitleAt(2,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.DataSourceProvider", "DataSource Provider"));
                jTabbedPane1.setTitleAt(3,it.businesslogic.ireport.util.I18n.getString("reportQueryDialog.tab.CSVDatasource", "CSV Datasource"));
    
                this.setTitle(I18n.getString("reportQueryDialog.title","Report query"));
                cancelButton.setMnemonic(I18n.getString("reportQueryDialog.cancelButtonMnemonic","c").charAt(0));
                okButton.setMnemonic(I18n.getString("reportQueryDialog.okButtonMnemonic","o").charAt(0));
    }
    
    
    /**
     * Thie method can be useful for a CustomQueryEditor
     */
    public JRSQLExpressionArea getSQLExpressionArea()
    {
        return jRSQLExpressionArea1;
    }
    
    /**
     * Thie method can be useful for a CustomQueryEditor
     * Return the table containing all the fields.
     */
    public JTable getFieldsTable()
    {
        return jTableFields;
    }

    /**
     * This method is used to open the filter expression in case of errors...
     */
    public void openFilterExpressionDialog(boolean showAsError) {
        if (getSubDataset() == null) return;
        FilterExpressionDialog fed = new FilterExpressionDialog(this, true);
        fed.setFilterExpression( this.getSubDataset().getFilterExpression(), getSubDataset());
        if (showAsError)
        {
            fed.setFocusedExpression( FilterExpressionDialog.COMPONENT_EXPRESSION);
        }
        
        fed.setVisible(true);
        if (fed.getDialogResult() == JOptionPane.OK_OPTION)
        {
            getSubDataset().setFilterExpression( fed.getFilterExpression() );
            System.out.println("Set filter expression to " + getSubDataset() + " (" + getSubDataset().getFilterExpression()  + ")");
        }
    }

    public void addField(JRField field) {
        
        // Add the field if there is not already a fiels with the same name...
        if (field == null) return;
        
        if (columnsErrorMsgLabel.isVisible())
        {
            columnsErrorMsgLabel.setText( "" );
            jPanel2.remove( columnsErrorScrollPane );
            jPanel2.add( columnsScrollPane, java.awt.BorderLayout.CENTER );
            jPanel2.revalidate();
        }
        
        DefaultTableModel dtm = (DefaultTableModel)jTableFields.getModel();
        for (int i=0; i<dtm.getRowCount(); ++i)
        {
            JRField tmpField = (JRField)dtm.getValueAt(i, 0);
            if (tmpField.getName().equals(field.getName())) return;
        }
        Vector row = new Vector();
        row.addElement(field);
        row.addElement(field.getClassType());
        row.addElement(field.getDescription());
        dtm.addRow(row);
        
        jTableFields.addRowSelectionInterval(jTableFields.getRowCount()-1, jTableFields.getRowCount()-1);
  
        jTableFields.updateUI();
        
        
    }
    
    
   
}


