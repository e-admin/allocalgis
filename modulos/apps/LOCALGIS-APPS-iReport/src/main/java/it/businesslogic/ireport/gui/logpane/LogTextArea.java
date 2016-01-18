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
 * LogTextArea.java
 * 
 * Created on 22 agosto 2005, 15.03
 *
 */

package it.businesslogic.ireport.gui.logpane;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.TextFieldReportElement;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;


/**
 *
 * @author  Administrator
 */
public class LogTextArea extends javax.swing.JPanel implements LanguageChangedListener {

    private StringBuffer outputBuffer;
    private String title = null;
    private LogPane logPane = null;
    private boolean removable = false;
    private Properties properties;
    
    private int maxlines = 5000;

    /** Creates new form LogTextArea */
    public LogTextArea(String title) {
        initComponents();

        outputBuffer = new StringBuffer();
        this.setTitle(title);

        this.jEditorPaneOutput.setContentType("text/html");

        this.jEditorPaneOutput.addHyperlinkListener( new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
                if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                    if (!parseError(e.getURL()))
                        JOptionPane.showMessageDialog(MainFrame.getMainInstance()  ," HyperlinkEvent " + e.getURL() );
                }
            }
        });

        try {
            maxlines = Integer.parseInt( System.getProperty("ireport.maxoutputlines", "5000"));
        } catch (Exception ex)
        {
            maxlines = 5000;
        }
        clearConsole();
    }

     /** This method parse an error encoded using an url.
     *  The url must be in the form:
     *  http://{error|warinig}:{JReportFrame ID}/{expression}
     *
     *  error: referred to an expression field
     *  warning: referred to an element position warning
     *
     *  Possible expressions for error:
     *  variableInitialValue_<VARIABLE_NAME>
     *  textField_<TEXTFIELD NUMBER>
     *  printWhen_<PRINT WHEN EXPRESSION NUMBER>(*)
     *  parameterDefaultValue_<PARAMETER NAME>
     *  parameter_<PARAMETER NAME>
     *
     *  Possible expressions for warning:
     *  y=<Y>,height=<Height>,band-height=<Band-Height>
     *
     *  (*) The printWhen expression are considered only if not blank.
     *  The search order is this: bands, elements
     */
    public static boolean  parseError(java.net.URL url) {
        if (url == null) return false;

        // First of all activate the right frame...
        JInternalFrame[] frames = MainFrame.getMainInstance().getJMDIDesktopPane().getAllFrames();
        JReportFrame jrf = null;
        for (int k=0; k< frames.length; k++) {
            if (frames[k] instanceof JReportFrame) {
                if ( ((JReportFrame)frames[k]).getWindowID() == url.getPort()) {
                    jrf = (JReportFrame)frames[k];
                    if ( !frames[k].isSelected() ) {
                        try {
                            frames[k].setSelected(true);
                        } catch (Exception ex)
                        {}
                    }
                }
            }
        }
        if (jrf == null) return false;

        try {
            if (url.getHost().equalsIgnoreCase("error")) {
                String expression = url.getFile();

                
                
                
                if (expression!=null && expression.length() > 0) {
                    // parse expression...
                    if (expression.startsWith("/variable")) {
                        
                        
                    }
                    
                    if (expression.startsWith("/textField_")) {
                        int index = 0;
                        int number = Integer.parseInt( expression.substring(("/textField_").length()).trim());
                        Enumeration e = jrf.getReport().getElements().elements();
                        while (e.hasMoreElements()) {
                            ReportElement re = (ReportElement)e.nextElement();
                            if (re instanceof TextFieldReportElement) {
                                index++;
                                if (index == number) {
                                    jrf.setSelectedElement(re);
                                    MainFrame.getMainInstance().getElementPropertiesDialog().setVisible(true);
                                    MainFrame.getMainInstance().getElementPropertiesDialog().gotoTab( MainFrame.getMainInstance().getElementPropertiesDialog().TEXTFIELD_TAB );
                                    return true;
                                }
                            }
                        }
                    }
                    else if (expression.startsWith("/parameterDefaultValue_")) {
                        int index = 0;
                        String name = expression.substring(("/parameterDefaultValue_").length()).trim();
                        Enumeration e = jrf.getReport().getParameters().elements();
                        while (e.hasMoreElements()) {
                            it.businesslogic.ireport.JRParameter param = (it.businesslogic.ireport.JRParameter)e.nextElement();
                            if (param.getName().equals(name)) {
                                MainFrame.getMainInstance().getValuesDialog().setVisible(true);
                                MainFrame.getMainInstance().getValuesDialog().modifyErrorParameter(param );
                                return true;
                            }
                        }

                    }
                }
            }
            else if (url.getHost().equals("warning")) {
            }
        } catch (Exception ex)
        {}
        return false;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPopupMenuLog = new javax.swing.JPopupMenu();
        jMenuItemClearLog = new javax.swing.JMenuItem();
        jMenuItemCloseLog = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemSelectAll = new javax.swing.JMenuItem();
        jScrollPaneOutput = new javax.swing.JScrollPane();
        jEditorPaneOutput = new javax.swing.JEditorPane();

        jMenuItemClearLog.setText("Clear log");
        jMenuItemClearLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClearLogActionPerformed(evt);
            }
        });

        jPopupMenuLog.add(jMenuItemClearLog);

        jMenuItemCloseLog.setText("Close log");
        jMenuItemCloseLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCloseLogActionPerformed(evt);
            }
        });

        jPopupMenuLog.add(jMenuItemCloseLog);

        jPopupMenuLog.add(jSeparator1);

        jMenuItemCopy.setText("Copy");
        jMenuItemCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCopyActionPerformed(evt);
            }
        });

        jPopupMenuLog.add(jMenuItemCopy);

        jMenuItemSelectAll.setText("Select all");
        jMenuItemSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSelectAllActionPerformed(evt);
            }
        });

        jPopupMenuLog.add(jMenuItemSelectAll);

        setLayout(new java.awt.BorderLayout());

        jScrollPaneOutput.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneOutput.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneOutput.setMinimumSize(new java.awt.Dimension(22, 75));
        jScrollPaneOutput.setPreferredSize(new java.awt.Dimension(3, 100));
        jEditorPaneOutput.setBackground(new java.awt.Color(204, 204, 204));
        jEditorPaneOutput.setEditable(false);
        jEditorPaneOutput.setFont(new java.awt.Font("Courier New", 0, 12));
        jEditorPaneOutput.setOpaque(false);
        jEditorPaneOutput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jEditorPaneOutputMouseClicked(evt);
            }
        });

        jScrollPaneOutput.setViewportView(jEditorPaneOutput);

        add(jScrollPaneOutput, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSelectAllActionPerformed
        jEditorPaneOutput.selectAll();
    }//GEN-LAST:event_jMenuItemSelectAllActionPerformed

    private void jMenuItemCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCopyActionPerformed
        jEditorPaneOutput.copy();
    }//GEN-LAST:event_jMenuItemCopyActionPerformed

    private void jMenuItemCloseLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseLogActionPerformed

        this.getLogPane().removeLog( this );
    }//GEN-LAST:event_jMenuItemCloseLogActionPerformed

    private void jMenuItemClearLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClearLogActionPerformed
        this.clearConsole();
    }//GEN-LAST:event_jMenuItemClearLogActionPerformed

    private void jEditorPaneOutputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jEditorPaneOutputMouseClicked

        if (evt.getButton() == evt.BUTTON3 && evt.getClickCount() == 1) {
            this.jPopupMenuLog.show(this, evt.getPoint().x, evt.getPoint().y);
        }

    }//GEN-LAST:event_jEditorPaneOutputMouseClicked

    public void logOnConsole(String noHTML) {
        
        noHTML = tail(noHTML, maxlines);
        logOnConsole(noHTML,false);
    }

    /**
     *  Print a message. If the messages starts with &lt;html&gt; the message is considered in HTML format.
     *  Otherwise it is formatted using the default font.
     *  
     *  Message types come from JOptionPane.... :
     * 
     *  JOptionPane.PLAIN_MESSAGE = No icon is used
     *  JOptionPane.INFORMATION_MESSAGE:
     *  JOptionPane.WARNING_MESSAGE:
     *  JOptionPane.ERROR_MESSAGE:
     *  JOptionPane.QUESTION_MESSAGE:
     *  
     * @param text The text to print
     * @param messageType The type of icon to put in front of the message
     */
    public void logOnConsole(String text, int messageType) {
        
        
        if (text == null) return;
        if (!text.startsWith("<html>"))
        {
            // clean the html text..
            text = text.replaceAll("\\>","&gt;");
            text = text.replaceAll("\\<","&lt;");
            text = text.replaceAll("\\t","&nbsp;&nbsp;&nbsp;&nbsp;");
            text = text.replaceAll("\\n","<br>");
            text = text.replaceAll("\\s","&nbsp;");
            
            text = "<font face=\"SansSerif\" size=\"3\" color=\"#000000\">" + text + "</font>";
        }
        else 
        {
            // Strip out the <html> prefix
            text =  text.substring(6);
        }
        
        URL img_url = null;
        
        try {
        switch (messageType)
        {
            case JOptionPane.INFORMATION_MESSAGE:
                img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/log/information.png");
                break;
            case JOptionPane.WARNING_MESSAGE:
                img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/log/warning.png");
                break;
            case JOptionPane.ERROR_MESSAGE:
                img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/log/error.png");
                break;
            case JOptionPane.QUESTION_MESSAGE:
                img_url = this.getClass().getResource("/it/businesslogic/ireport/icons/log/question.png");
                break;
            case JOptionPane.PLAIN_MESSAGE:
            default: 
                img_url = null;
        }
        } catch (Exception ex)
        {
            // image not found....
        }
        
        if (img_url != null)
        {
            text = "<img align=\"right\" src=\""+  img_url  +"\">" + text;
        }

        logOnConsole(text,true);
    }
     
    public void logOnConsole(String s, boolean isHTML) {
        try {

            //String text = Misc.string_replace("","</body></html>",this.jEditorPaneOutput.getText());

            //s =  + s+"</body></html>";
            if ( this.jEditorPaneOutput == null) {
                outputBuffer.append(s);
                //System.out.println(outputBuffer);
            }
            else {
                this.jEditorPaneOutput.setContentType("text/html");
                if (!isHTML) {
                    
                    s = s.replaceAll("\\>","&gt;");
                    s = s.replaceAll("\\<","&lt;");
                    s = s.replaceAll("\\t","&nbsp;&nbsp;&nbsp;&nbsp;");
                    s = s.replaceAll("\\n","<br>");
                    s = s.replaceAll("\\s","&nbsp;");
                    
                   
                    //s = Misc.string_replace("&gt;",">",s);
                    //s = Misc.string_replace("&lt;","<",s);
                    //s = Misc.string_replace("&nbsp;"," ",s);
                    //s = Misc.string_replace("&nbsp;&nbsp;&nbsp;&nbsp;","\t",s);
                    //s = Misc.string_replace("<br>", "\n", s);
                }

                /*
                outputBuffer.append(s);
                this.jEditorPaneOutput.setText("<html><body><table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td nowrap><font face=\"Courier New\" size=\"3\">" + outputBuffer + "</font></td></tr></table></body></html>");
                */
                // Perform an append instead...
                javax.swing.text.html.HTMLDocument doc = (javax.swing.text.html.HTMLDocument)jEditorPaneOutput.getDocument();
                javax.swing.text.html.HTMLEditorKit editorKit = (javax.swing.text.html.HTMLEditorKit)jEditorPaneOutput.getEditorKit();

                //javax.swing.text.Element ele = doc.get getElement("inserthere");

                if (!s.startsWith("<")) 
                {
                    s = "<font face=\"Courier New\" size=\"3\" >" + s + "</font>";
                }

                //if (ele != null)
                //{
                    //doc.insertBeforeEnd(ele, s);     //- "".length()
                    editorKit.insertHTML(doc, doc.getLength(), s, 0, 0, null);

                //}
                //else
                //{
                //    this.jEditorPaneOutput.setText("<body top=0><p align=\"left\" id=\"inserthere\">"+ s + "</p></body>");
                //}


                logPane.setActiveLog( this );
            }
            //this.jEditorPaneOutput.getDocument().insertString(this.jEditorPaneOutput.getDocument().getLength(), s ,  null);

        } catch (Exception exsx) {
            JOptionPane.showMessageDialog(this,""+exsx.getMessage());
        }
    }

    /**
     * Clear console
     */
    public void clearConsole()
    {
        this.jEditorPaneOutput.setContentType("text/html");
            outputBuffer.setLength(0);
            this.jEditorPaneOutput.setText("");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        fireActionListenerActionPerformed( new java.awt.event.ActionEvent(this,0,title));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPaneOutput;
    private javax.swing.JMenuItem jMenuItemClearLog;
    private javax.swing.JMenuItem jMenuItemCloseLog;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemSelectAll;
    private javax.swing.JPopupMenu jPopupMenuLog;
    private javax.swing.JScrollPane jScrollPaneOutput;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

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
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener)listeners[i+1]).actionPerformed (event);
            }
        }
    }

    public LogPane getLogPane() {
        return logPane;
    }

    public void setLogPane(LogPane logPane) {
        this.logPane = logPane;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
    //Added by Felix Firgau for I18n on Feb 10th 2006
public void applyI18n() {
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
  jMenuItemClearLog.setText(it.businesslogic.ireport.util.I18n.getString("clearLog"));
  jMenuItemCloseLog.setText(it.businesslogic.ireport.util.I18n.getString("closeLog"));
  jMenuItemCopy.setText(it.businesslogic.ireport.util.I18n.getString("copy"));
  jMenuItemSelectAll.setText(it.businesslogic.ireport.util.I18n.getString("selectAll"));

}
public void languageChanged(LanguageChangedEvent evt) {
  this.applyI18n();
}//End


public static int countMatches(String str, String sub) {
        if (str == null || str.length() == 0 || sub == null || sub.length()==0) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }



public static String  tail(String str, int lines) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int total = countMatches(str,"\n");
        int splitTo = total - lines;
        if (splitTo <= 0) return str;
        
        int count = 0;
        int idx = 0;
        while (count < splitTo && (idx = str.indexOf("\n", idx)) != -1) {
                count++;
                idx += 1;
        }
        
        return str.substring(idx);
    }

}
