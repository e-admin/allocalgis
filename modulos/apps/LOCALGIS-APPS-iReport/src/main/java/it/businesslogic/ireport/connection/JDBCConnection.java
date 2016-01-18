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
 * JDBCConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JDBCConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.Misc;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
/**
 *
 * @author  Administrator
 */
public class JDBCConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String JDBCDriver;
    
    private String username;
    
    private String password = null;
    
    private String url;
    
    private String database;
    
    private boolean savePassword;
    
    private String name;
    
    /**
     * Holds value of property serverAddress.
     */
    private String serverAddress;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JDBCConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    public java.sql.Connection getConnection() {
        
            // Try the java connection...
            try {
                
                    it.businesslogic.ireport.gui.MainFrame.getMainInstance().getReportClassLoader().rescanLibDirectory();
                    
                    try {
                    	DriverPool.registerDriver( this.getJDBCDriver(), it.businesslogic.ireport.gui.MainFrame.getMainInstance().getReportClassLoader() );
                    } catch (Exception ex)
                    {
                    	DriverPool.registerDriver( this.getJDBCDriver(), this.getClass().getClassLoader() );
                    }
                    
                    java.sql.Driver driver = DriverPool.getDriver( url );
                    
                    java.util.Properties connectProps = new java.util.Properties();
                    
                    if ((password == null || password.equals("") ) && !isSavePassword())
                    {
                        password = getPassword();
                    }
                    
                    connectProps.setProperty("user", username);
                    connectProps.setProperty("password", password);
                    
                    Connection conn = driver.connect( url, connectProps); 
                    
                    if ( (this.getJDBCDriver().toLowerCase().indexOf("oracle") >= 0) && (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_language","").trim().length() > 0 ||
                        it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_territory","").trim().length() > 0) )
                    {
	                    Statement stmt = null;
	                    try {
		                    stmt = conn.createStatement();
		                    if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_language","").trim().length() > 0)
				    stmt.execute("ALTER SESSION SET NLS_LANGUAGE = '" + it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_language","").trim() + "'");
				    if (it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_territory","").trim().length() > 0)
				    stmt.execute("ALTER SESSION SET NLS_TERRITORY='" + it.businesslogic.ireport.gui.MainFrame.getMainInstance().getProperties().getProperty("oracle_territory","").trim() + "'");
				    
	                    } catch (Exception ex)
	                    {
	                    	//ex.printStackTrace();
	        	    }
	        	    finally {
		        	    if (stmt != null) stmt.close();
	        	    }
                    }
        
                    return conn;
			
            }catch (NoClassDefFoundError ex)
		{
                    showErrorMessage(I18n.getFormattedString("messages.connection.noClassDefFoundError",
                                "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                                new Object[]{""+ this.getName(), ""+ex.getMessage()}),
                                I18n.getString("message.title.exception","Exception"), ex);
                    
		    return null;					
		} 
		catch (ClassNotFoundException ex)
		{
		    showErrorMessage(I18n.getFormattedString("messages.connection.classNotFoundError",
                                "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                                new Object[]{""+ this.getName(), ""+ex.getMessage(), "" + this.getJDBCDriver()}),
                                I18n.getString("message.title.exception","Exception"), ex);
			return null;				
		} 
		catch (java.sql.SQLException ex)
		{
			if (!savePassword) password = null;
			showErrorMessage(I18n.getFormattedString("messages.connection.sqlError",
                                "{0}\nSQL problems: {1}\n{2}",
                                new Object[]{""+ this.getName(), ""+ex.getMessage(), "" + url}),
                                I18n.getString("message.title.exception","Exception"), ex);
			return null;					
		} 
		catch (Exception ex)
		{
			showErrorMessage(I18n.getFormattedString("messages.connection.generalError",
                                "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                                new Object[]{""+ this.getName(), ""+ex.getMessage()}),
                                I18n.getString("message.title.exception","Exception"), ex);
			return null;					
		}
    }    
    
    private void  showErrorMessage(String errorMsg, String title, Throwable theException)
    {
        
        final JXErrorPane pane = new JXErrorPane();
        pane.setLocale(I18n.getCurrentLocale());
       
        String[] lines = errorMsg.split("\r\n|\n|\r");

        String shortMessage = errorMsg;
        if (lines.length > 4)
        {
            shortMessage = "";
            for (int i=0; i<4; ++i)
            {
                shortMessage += lines[i]+"\n";
            }
            shortMessage = shortMessage.trim() + "\n...";
        }
      
        ErrorInfo ei = new ErrorInfo(title,
                 shortMessage,
                 null, //"<html><pre>" + errorMsg + "</pre>"
                 null,
                 theException,
                 null,
                 null);
         pane.setErrorInfo(ei);
        
        /*
        
        
        final String fErrorMsg = errorMsg;
        */
        Runnable r = new Runnable() {
                public void run() {
                   // JOptionPane.showMessageDialog(MainFrame.getMainInstance(),fErrorMsg,title,JOptionPane.ERROR_MESSAGE);
                
                   JXErrorPane.showDialog(MainFrame.getMainInstance(), pane);
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
    
    
    /*  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     *
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() {
        return  new net.sf.jasperreports.engine.JREmptyDataSource();
    }
    
    public boolean isJDBCConnection() {
        return true;
    }
    
    /** Getter for property database.
     * @return Value of property database.
     *
     */
    public java.lang.String getDatabase() {
        return database;
    }
    
    /** Setter for property database.
     * @param database New value of property database.
     *
     */
    public void setDatabase(java.lang.String database) {
        this.database = database;
    }
    
    /** Getter for property JDBCDriver.
     * @return Value of property JDBCDriver.
     *
     */
    public java.lang.String getJDBCDriver() {
        return JDBCDriver;
    }
    
    /** Setter for property JDBCDriver.
     * @param JDBCDriver New value of property JDBCDriver.
     *
     */
    public void setJDBCDriver(java.lang.String JDBCDriver) {
        this.JDBCDriver = JDBCDriver;
    }
    
    /** Getter for property password.
     * @return Value of property password.
     *
     */
    public java.lang.String getPassword() {
        
        if (isSavePassword()) return password;
        else
        {
            // Ask for password...
            try {
                return it.businesslogic.ireport.gui.PasswordDialog.askPassword();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }
    
    /** Setter for property password.
     * @param password New value of property password.
     *
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
    /** Getter for property savePassword.
     * @return Value of property savePassword.
     *
     */
    public boolean isSavePassword() {
        return savePassword;
    }
    
    /** Setter for property savePassword.
     * @param savePassword New value of property savePassword.
     *
     */
    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }
    
    /** Getter for property url.
     * @return Value of property url.
     *
     */
    public java.lang.String getUrl() {
        return url;
    }
    
    /** Setter for property url.
     * @param url New value of property url.
     *
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }
    
    /** Getter for property username.
     * @return Value of property username.
     *
     */
    public java.lang.String getUsername() {
        return username;
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     *
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("JDBCDriver", Misc.nvl(this.getJDBCDriver(),"") );
        map.put("Url", Misc.nvl(this.getUrl(),""));
        map.put("Database", Misc.nvl(this.getDatabase(),""));
        map.put("Username", Misc.nvl(this.getUsername(),""));
        if (this.isSavePassword())
            map.put("Password", Misc.nvl(this.getPassword(),""));
        else map.put("Password","");
        map.put("SavePassword", ""+this.isSavePassword());
        map.put("ServerAddress", Misc.nvl(this.getServerAddress(),"") );
        
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setJDBCDriver( (String)map.get("JDBCDriver"));
        this.setUrl( (String)map.get("Url"));
        this.setDatabase( (String)map.get("Database"));
        this.setUsername( (String)map.get("Username"));
        this.setSavePassword(  (""+map.get("SavePassword")).equals("true") );
        if (this.isSavePassword())
            this.setPassword( Misc.nvl((String)map.get("Password"),""));
        this.setServerAddress( Misc.nvl((String)map.get("ServerAddress"),"") );
        
    }
    
    /**
     * Getter for property serverAddress.
     * @return Value of property serverAddress.
     */
    public String getServerAddress() {
        return this.serverAddress;
    }
    
    /**
     * Setter for property serverAddress.
     * @param serverAddress New value of property serverAddress.
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
    
    
    public String getDescription(){ return I18n.getString("connectionType.jdbc", "Database JDBC connection"); }
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JDBCConnectionEditor();
    }
    
    
    public void test() throws Exception
    {
        // Try the java connection...
        Connection conn = null;
        try {
                conn = getConnection();
                if (conn == null) throw new Exception("");
        } finally {
            // Clean up
            if( conn!=null ) try{ conn.close(); } catch(Exception e) { /* anyone really care? */ }
        }
        JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
        return;
    }
}
