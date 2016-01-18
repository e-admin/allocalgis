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
 * JRDataSourceProviderConnection.java
 * 
 * Created on 17 febbraio 2005, 7.26
 *
 */

package it.businesslogic.ireport.connection;


import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JRDataSourceProviderConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JasperReport;
/**
 *
 * @author  Administrator
 */
public class JRDataSourceProviderConnection extends it.businesslogic.ireport.IReportConnection {
    
    
    private net.sf.jasperreports.engine.JRDataSourceProvider dsp;
    private net.sf.jasperreports.engine.JRDataSource ds;
    private java.util.HashMap properties = new java.util.HashMap();
    
    public  net.sf.jasperreports.engine.JRDataSourceProvider getDataSourceProvider() {
        
        if (dsp == null && this.getProperties().get("JRDataSourceProvider") != null)
        {
            try {
                dsp = (JRDataSourceProvider)(Class.forName( (String)this.getProperties().get("JRDataSourceProvider"), true, it.businesslogic.ireport.gui.MainFrame.getMainInstance().getReportClassLoader()).newInstance());
            } catch (NoClassDefFoundError ex)
	    {
                showErrorMessage(
                                I18n.getString("messages.JRDataSourceProviderConnection.noClassDefFoundError",
                                "No class definition found error!!\nCheck your classpath!"),
                                I18n.getString("message.title.exception","Exception"));
            } 
	    catch (ClassNotFoundException ex)
	    {
		showErrorMessage(
                        I18n.getString("messages.JRDataSourceProviderConnection.classNotFoundError",
                                "Class not found error!!\nCheck your classpath!"),
                        I18n.getString("message.title.exception","Exception"));
            } 
            catch (Exception ex)
            {
                showErrorMessage("" + ex.getMessage(),
                        I18n.getString("message.title.exception","Exception"));
            }
        }
        
        return dsp;
    }
    
    private void  showErrorMessage(final String errorMsg, final String title)
    {
        Runnable r = new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),errorMsg,title,JOptionPane.ERROR_MESSAGE);
                }
            };

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait( r );
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
                r.run();
        }
    }
     
    
    /** Creates a new instance of JRDataSourceProviderConnection */
    public JRDataSourceProviderConnection() {
    }
    
        /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        
        return getJRDataSource(null);
    }
    
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource(JasperReport jasper) { 
        
        if (ds != null)
        {
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.JRDataSourceProviderConnection.datasourceInUse","This datasource is already in use by another filling process!!"),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            ds = getDataSourceProvider().create(jasper);
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getFormattedString("messages.JRDataSourceProviderConnection.problemsCreatingDatasource","Problems occurred creating the new datasource!!\n{0}", new Object[]{""+ex.getMessage()}),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
        }
        
        return ds;
    }
    
    public void disposeDataSource()
    {
            if (dsp != null)
            {
                try {
                    dsp.dispose(ds);
                } catch (Exception ex) {}
                ds = null;
            }
    }
    
    public java.util.HashMap getProperties()
    {
        return properties;
    }
    
    /** All properties of a IReportConnection are stored in a XML file as Pair key/value
     *  This HashMap contains alla properties found for this IReportConnection in the
     *  XML. You must use this hashMap to initialize all attributes of your IReprotConnection
     */
    public void loadProperties(java.util.HashMap map)
    {
        properties = map;
    }
    
    /** Redefine this method is not useful (and not raccomended)
     *  It just write a portion of XML for save properties a IReportConnection name
     */
    public void save(java.io.PrintWriter pw)
    {
        java.util.HashMap hm = this.getProperties();
        pw.println("\t<iReportConnection name=\""+ this.getName() +"\" connectionClass=\"" + this.getClass().getName() +"\">");
        java.util.Iterator iterator = hm.keySet().iterator();
        
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            pw.println("\t\t<connectionParameter name=\""  +  key + "\"><![CDATA[" + hm.get(key) + "]]></connectionParameter>");
        }
        pw.println("\t</iReportConnection>");
    }    
    
    public String getDescription(){ return I18n.getString("connectionType.datasourceProvider", "JRDataSourceProvider"); }
    
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRDataSourceProviderConnectionEditor();
    }
    
    public void test() throws Exception
    {
        try {
                
                Class c = Class.forName((String)this.getProperties().get("JRDataSourceProvider") , true, MainFrame.getMainInstance().getReportClassLoader());

                if (!(net.sf.jasperreports.engine.JRDataSourceProvider.class.isAssignableFrom(c)))
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getFormattedString("messages.connectionDialog.invalidJRDataSourceProvider","\"{0}\" is not a subclass of\nnet.sf.jasperreports.engine.JRDataSourceProvider.", new Object[]{this.getProperties().get("JRDataSourceProvider")}),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                else
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
            } catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                            I18n.getFormattedString("messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + this.getProperties().get("JRDataSourceProvider")}),
                            I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                    I18n.getFormattedString("messages.connection.generalError2",
                    "{0}\nGeneral problem:\n {1}",
                    new Object[]{"", ""+ex.getMessage()}),
                    I18n.getString("message.title.exception","Exception"),JOptionPane.ERROR_MESSAGE);
                    return;									
            }
    }
}
