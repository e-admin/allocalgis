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
 * ConnectionsDialog.java
 * 
 * Created on 7 maggio 2003, 23.43
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.Report;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  Administrator
 */
public class ConnectionsDialog extends javax.swing.JDialog {

    /** Creates new form ValuesDialog */
    public ConnectionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        applyI18n();
        this.setSize(490,500);
        //DefaultTableColumnModel dtcm = (DefaultTableColumnModel)jTableParameters.getColumnModel();
        //DefaultTableCellRenderer tcr = (DefaultTableCellRenderer)dtcm.getColumn(0).getHeaderRenderer();

        //new DefaultTableCellRenderer();
        //tcr.setFont(jTableParameters.getFont());
        //tcr.setBackground(this.getBackground());
        //tcr.setBorder( new javax.swing.border.BevelBorder( javax.swing.border.BevelBorder.RAISED));
        //dtcm.getColumn(0).setHeaderRenderer(tcr);

        DefaultListSelectionModel dlsm =  (DefaultListSelectionModel)this.jTableParameters.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                jTableParametersListSelectionValueChanged(e);
            }
        });

        // Open in center...
        it.businesslogic.ireport.util.Misc.centerFrame(this);

        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();

        Enumeration e = MainFrame.getMainInstance().getConnections().elements();
        Object defaultConnection = MainFrame.getMainInstance().getProperties().get("DefaultConnection");


        IReportConnection default_irc = null;
        if (defaultConnection != null && defaultConnection instanceof IReportConnection)
        {
            default_irc = (IReportConnection)defaultConnection;
        }
        else
        {
            // Default connection not set!!!
            if (((MainFrame)parent).getConnections().size() > 0)
            {
                default_irc = (IReportConnection)MainFrame.getMainInstance().getConnections().elementAt(0);
                MainFrame.getMainInstance().setActiveConnection(default_irc);
            }
        }

        while (e.hasMoreElements())
        {
            IReportConnection con = (IReportConnection)e.nextElement();
            dtm.addRow( new Object[]{con, con.getDescription(), new Boolean(default_irc == con) });
        }
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        

        //to make the default button ...
        //this.getRootPane().setDefaultButton(this.jButtonOK);
    }


    public void jTableParametersListSelectionValueChanged(javax.swing.event.ListSelectionEvent e)
    {
         if (this.jTableParameters.getSelectedRowCount() > 0) {
            this.jButtonModifyParameter.setEnabled(true);
            this.jButtonDeleteParameter.setEnabled(true);
            this.jButtonDefault.setEnabled(true);
        }
        else {
            this.jButtonModifyParameter.setEnabled(false);
            this.jButtonDeleteParameter.setEnabled(false);
            this.jButtonDefault.setEnabled(false);
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

        jPanelParameters = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableParameters = new javax.swing.JTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonNewParameter = new javax.swing.JButton();
        jButtonModifyParameter = new javax.swing.JButton();
        jButtonDeleteParameter = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Connections / Datasources");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelParameters.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTableParameters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Datasource type", "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableParameters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableParametersMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTableParameters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelParameters.add(jScrollPane1, gridBagConstraints);

        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jPanelButtons.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanelButtons.setPreferredSize(new java.awt.Dimension(100, 10));
        jButtonNewParameter.setText("New");
        jButtonNewParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelButtons.add(jButtonNewParameter, gridBagConstraints);

        jButtonModifyParameter.setText("Modify");
        jButtonModifyParameter.setEnabled(false);
        jButtonModifyParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonModifyParameter, gridBagConstraints);

        jButtonDeleteParameter.setText("Delete");
        jButtonDeleteParameter.setEnabled(false);
        jButtonDeleteParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteParameterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonDeleteParameter, gridBagConstraints);

        jButtonDefault.setText("Set as default");
        jButtonDefault.setActionCommand("Set as active");
        jButtonDefault.setEnabled(false);
        jButtonDefault.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteParameterActionPerformed1(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 5, 3);
        jPanelButtons.add(jButtonDefault, gridBagConstraints);

        jButtonImport.setText("Import...");
        jButtonImport.setActionCommand("Set as active");
        jButtonImport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 5, 3);
        jPanelButtons.add(jButtonImport, gridBagConstraints);

        jButtonExport.setText("Export...");
        jButtonExport.setActionCommand("Set as active");
        jButtonExport.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 3);
        jPanelButtons.add(jButtonExport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelParameters.add(jPanelButtons, gridBagConstraints);

        getContentPane().add(jPanelParameters, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed

            if (jTableParameters.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                        I18n.getString("messages.connectionsDialog.noConnectionToExport" ,"No connections to export"),
                        "",JOptionPane.INFORMATION_MESSAGE);
                return;
            }    
        
            JFileChooser jfc = new JFileChooser();
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "iReport connection/datasource definition (*.xml)";
		    }
	    });

	    if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                try {
                    java.io.File f = jfc.getSelectedFile();
                    String fileName = f.getName();
                    if (fileName.indexOf(".") < 0)
                    {
                        fileName += ".xml";
                    }
                    f = new java.io.File(f.getParent(), fileName);
                    
                    if (f.exists())
                    {
                        if (JOptionPane.showConfirmDialog(this,
                                I18n.getFormattedString("messages.connectionsDialog.overwrite" ,"{0} already exists.\nDo you want overwrite it?", new Object[]{""+f}),
                                "",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION)
                        {
                            return;
                        }
                    }
                    
                    java.io.PrintWriter pw = new java.io.PrintWriter( new java.io.OutputStreamWriter( new java.io.FileOutputStream( f  ), "UTF8" )); //UTF8
                    pw.print("<?xml version=\"1.0\"?>");
                    pw.println("<!-- iReport connections -->");
                    pw.println("<iReportConnectionSet>");

                    Enumeration con_enum = MainFrame.getMainInstance().getConnections().elements();
                    int i = 0;
                    
                    while (con_enum.hasMoreElements())
                    {
                        i++;
                        IReportConnection con = (IReportConnection)con_enum.nextElement();
                        con.save(pw);
                    }
                    
                    pw.println("</iReportConnectionSet>");

                    pw.close();
                    
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connectionsDialog.connectionsExported" ,"{0,number,integer} connection(s) succesfully exported.", new Object[]{new Integer(i)}),
                            "",JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connectionsDialog.errorSavingConnections" ,"Error saving connections:\n{0}", new Object[]{ex.getMessage()})
                            );
                    ex.printStackTrace();
                }
                    
	    }
        
        
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed

        
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(false);
            jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
		    public boolean accept(java.io.File file) {
			    String filename = file.getName().toLowerCase();
			    return (filename.endsWith(".xml") || file.isDirectory()) ;
		    }
		    public String getDescription() {
			    return "iReport connection/datasource definition (*.xml)";
		    }
	    });

	    if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                    Vector new_cons = loadConnections( jfc.getSelectedFile() );
                    if (new_cons != null)
                    {
                        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
                        int i = 0;
                        for (i=0; i<new_cons.size(); ++i)
                        {
                            IReportConnection con = (IReportConnection)new_cons.elementAt(i);
                            dtm.addRow( new Object[]{con, con.getDescription() });
                            MainFrame.getMainInstance().getConnections().addElement(con);
                        }
                        MainFrame.getMainInstance().saveiReportConfiguration();
                        
                        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.connectionsDialog.connectionsImported" ,"{0,number,integer} connection(s) succesfully imported.", new Object[]{new Integer(i)}),
                                "",JOptionPane.INFORMATION_MESSAGE);
                    }
	    }        
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jTableParametersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableParametersMouseClicked

            if (evt.getClickCount() == 2 && evt.getButton() == evt.BUTTON1)
            {
                jButtonModifyParameterActionPerformed(null);
            }

    }//GEN-LAST:event_jTableParametersMouseClicked

    private void jButtonDeleteParameterActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteParameterActionPerformed1

        if (jTableParameters.getSelectedRowCount() > 0)
        {
            IReportConnection irc = null;
            try {
                irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
                MainFrame.getMainInstance().setActiveConnection(irc);
                jTableParameters.getModel().setValueAt(new Boolean(true) ,jTableParameters.getSelectedRow(), 2);
                for (int i=0; i<jTableParameters.getRowCount(); ++i)
                {
                    if (i != jTableParameters.getSelectedRow())
                    {
                        jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                    }
                }
            } catch (Exception ex) { return; }
        }
    }//GEN-LAST:event_jButtonDeleteParameterActionPerformed1

    private void jButtonDeleteParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteParameterActionPerformed
        // Get the selected connection...
        if (jTableParameters.getSelectedRowCount() > 0)
        {
            while (jTableParameters.getSelectedRowCount() > 0)
            {
                IReportConnection irc = null;
                try {
                    irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
                } catch (Exception ex) { return; }


                if ( MainFrame.getMainInstance().getProperties().get( "DefaultConnection") == irc)
                {
                    MainFrame.getMainInstance().getProperties().remove("DefaultConnection");
                }

                MainFrame.getMainInstance().getConnections().removeElement(irc);
                ((DefaultTableModel)jTableParameters.getModel()).removeRow(jTableParameters.getSelectedRow());
                jTableParameters.updateUI();
            }
            
            MainFrame.getMainInstance().saveiReportConfiguration();
        }

    }//GEN-LAST:event_jButtonDeleteParameterActionPerformed

    private void jButtonModifyParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyParameterActionPerformed
        java.awt.Frame parent = Misc.frameFromComponent(this);
        it.businesslogic.ireport.connection.gui.ConnectionDialog cd = new it.businesslogic.ireport.connection.gui.ConnectionDialog(parent,false);

        // Take the selected connection from the table...
        if (jTableParameters.getSelectedRow() < 0) return;
        IReportConnection irc = null;
        try {
            irc = (IReportConnection)jTableParameters.getModel().getValueAt(jTableParameters.getSelectedRow(), 0);
        } catch (Exception ex) { return; }

        if (irc == null) return;

        cd.setIReportConnection(irc);

        cd.setVisible(true);

        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            IReportConnection con = cd.getIReportConnection();
            // Now we have an old and a new object.
            // 1. Adjust table...
            try {
                jTableParameters.getModel().setValueAt(con ,jTableParameters.getSelectedRow(), 0);
                jTableParameters.getModel().setValueAt(con.getDescription() ,jTableParameters.getSelectedRow(), 1);
                jTableParameters.getModel().setValueAt(new Boolean(true) ,jTableParameters.getSelectedRow(), 2);
            } catch (Exception ex) { return; }

            for (int i=0; i<jTableParameters.getRowCount(); ++i)
            {
                if (i != jTableParameters.getSelectedRow())
                {
                    jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                }
            }
            
            MainFrame.getMainInstance().getConnections().setElementAt(con,
            MainFrame.getMainInstance().getConnections().indexOf(irc));
            MainFrame.getMainInstance().setActiveConnection(con);
            MainFrame.getMainInstance().saveiReportConfiguration();
        }
    }//GEN-LAST:event_jButtonModifyParameterActionPerformed

    private void jButtonNewParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewParameterActionPerformed
        java.awt.Frame parent = Misc.frameFromComponent(this);
        
        
        //ConnectionDialog cd = new ConnectionDialog(parent,false);
        it.businesslogic.ireport.connection.gui.ConnectionDialog cd = 
                    new it.businesslogic.ireport.connection.gui.ConnectionDialog(this, true);
        
        cd.setVisible(true);

        if (cd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            
            IReportConnection con = cd.getIReportConnection();
            DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
            dtm.addRow( new Object[]{con, con.getDescription(), new Boolean(true) });
            
            for (int i=0; i<jTableParameters.getRowCount(); ++i)
            {
                if (i != dtm.getRowCount()-1)
                {
                    jTableParameters.getModel().setValueAt(new Boolean(false) ,i, 2);
                }
            }
            
            MainFrame.getMainInstance().getConnections().addElement(con);
            MainFrame.getMainInstance().setActiveConnection(con);
            MainFrame.getMainInstance().saveiReportConfiguration();
        }
         
    }//GEN-LAST:event_jButtonNewParameterActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ValuesDialog(new javax.swing.JFrame(), true).setVisible(true);
    }


    public void updateConnections() {
       /*
        DefaultTableModel dtm = (DefaultTableModel)jTableParameters.getModel();
        dtm.setRowCount(0);

        Enumeration enum = jReportFrame.getReport().getParameters().elements();
        while (enum.hasMoreElements())
        {
            it.businesslogic.ireport.JRParameter parameter = (it.businesslogic.ireport.JRParameter)enum.nextElement();
            Vector row = new Vector();
            row.addElement( parameter);
            row.addElement( parameter.getClassType());
            row.addElement( parameter.isIsForPrompting()+"");

            dtm.addRow(row);
        }
        */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDefault;
    private javax.swing.JButton jButtonDeleteParameter;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JButton jButtonModifyParameter;
    private javax.swing.JButton jButtonNewParameter;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelParameters;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableParameters;
    // End of variables declaration//GEN-END:variables

       public void setVisible(boolean visible)
    {

       updateConnections();
        super.setVisible(visible);
    }
       
    /**
     * Load a set of connections from a file. The connection are renamed if already present
     * in the connection list. See getAvailableConnectionName() for details about the new name.
     */
    public Vector loadConnections(java.io.File xmlfile)
     {
         Vector v = new Vector();
         try {
             DOMParser parser = new DOMParser();
             java.io.FileInputStream fis = new java.io.FileInputStream(xmlfile);
             org.xml.sax.InputSource input_sss  = new org.xml.sax.InputSource(fis);
             //input_sss.setSystemId(filename);
             parser.parse( input_sss );

             Document document = parser.getDocument();
             Node node = document.getDocumentElement();


             NodeList list_child = node.getChildNodes(); // The root is iReportConnections
             for (int ck=0; ck< list_child.getLength(); ck++) {
                 Node connectionNode = (Node)list_child.item(ck);
                 if (connectionNode.getNodeName() != null && connectionNode.getNodeName().equals("iReportConnection"))
                 {
                    // Take the CDATA...
                        String connectionName = "";
                        String connectionClass = "";
                        HashMap hm = new HashMap();
                        NamedNodeMap nnm = connectionNode.getAttributes();
                        if ( nnm.getNamedItem("name") != null) connectionName = nnm.getNamedItem("name").getNodeValue();
                        if ( nnm.getNamedItem("connectionClass") != null) connectionClass = nnm.getNamedItem("connectionClass").getNodeValue();

                        // Get all connections parameters...
                        NodeList list_child2 = connectionNode.getChildNodes();
                        for (int ck2=0; ck2< list_child2.getLength(); ck2++) {
                            String parameterName = "";
                            Node child_child = (Node)list_child2.item(ck2);
                            if (child_child.getNodeType() == Node.ELEMENT_NODE && child_child.getNodeName().equals("connectionParameter")) {

                                NamedNodeMap nnm2 = child_child.getAttributes();
                                if ( nnm2.getNamedItem("name") != null)
                                    parameterName = nnm2.getNamedItem("name").getNodeValue();
                                hm.put( parameterName,Report.readPCDATA(child_child));
                            }
                        }
                        
                        // If the name exists, rename it as "name (2)"
                        connectionName = getAvailableConnectionName(connectionName);
                        
                        try {
                            IReportConnection con = (IReportConnection) Class.forName(connectionClass).newInstance();
                            con.loadProperties(hm);
                            con.setName(connectionName);
                            v.addElement( con );
                        } catch (Exception ex) {
                                
                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.connectionsDialog.errorLoadingConnection" ,"Error loading  {0}", new Object[]{connectionName}),
                                I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
                        }
                }
             }
         } catch (Exception ex)
         {
             JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                                I18n.getFormattedString("messages.connectionsDialog.errorLoadingConnections" ,"Error loading connections.\n{0}", new Object[]{ex.getMessage()}),
                                I18n.getString("message.title.error","Error"), JOptionPane.ERROR_MESSAGE);
              ex.printStackTrace();
         }

         return v;
     }
    
     // If the name exists, rename it as "name (2)"
     /**
      * This method take a proposed connection name. Check for duplicates names. If the 
      * proposed name is already present, the name "proposed (2)" is checked and so
      * on up to when a valid name is found....
      */
     public static String getAvailableConnectionName(String proposedConnectionName)
     {
        return getAvailableConnectionName(proposedConnectionName, 0);
     }
     
     private static String getAvailableConnectionName(String proposedConnectionName, int testNumber)
     {
        String name = proposedConnectionName;
        if (testNumber != 0) name += " (" + testNumber + ")";
        
        Enumeration con_enum = MainFrame.getMainInstance().getConnections().elements();
        while (con_enum.hasMoreElements())
        {
            // toString for an iReportConnection is the getName method...
            String conName = "" + con_enum.nextElement();
            if (name.equals(conName))
            {
                return getAvailableConnectionName(proposedConnectionName, testNumber+1);
            }
        }
        return name;
     }
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonDefault.setText(I18n.getString("connectionsDialog.buttonDefault","Set as default"));
                jButtonDeleteParameter.setText(I18n.getString("connectionsDialog.buttonDeleteParameter","Delete"));
                jButtonExport.setText(I18n.getString("connectionsDialog.buttonExport","Export..."));
                jButtonImport.setText(I18n.getString("connectionsDialog.buttonImport","Import..."));
                jButtonModifyParameter.setText(I18n.getString("connectionsDialog.buttonModifyParameter","Modify"));
                jButtonNewParameter.setText(I18n.getString("connectionsDialog.buttonNewParameter","New"));
                // End autogenerated code ----------------------
                setTitle(I18n.getString("connectionsDialog.title","Connections / Datasources"));
                jTableParameters.getColumnModel().getColumn(0).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.name","Name") );
                jTableParameters.getColumnModel().getColumn(1).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.datasourceType","Datasource type") );
                jTableParameters.getColumnModel().getColumn(2).setHeaderValue( I18n.getString("connectionsDialog.tablecolumn.default","Default") );
    }
}
