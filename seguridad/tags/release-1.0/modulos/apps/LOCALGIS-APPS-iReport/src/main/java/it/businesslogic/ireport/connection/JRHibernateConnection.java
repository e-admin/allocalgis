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
 * JRHibernateConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.JRHibernateConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.I18n;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
/**
 *
 * @author  Administrator
 */
public class JRHibernateConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String name;
    
    /** Creates a new instance of JRHibernateConnection */   
    public JRHibernateConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    public java.sql.Connection getConnection() {       
            return null;
    }
    
    public boolean isJDBCConnection() {
        return false;
    }
    
    public boolean isJRDataSource() {
        return false;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
    }
    
    
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        return null;
    }
    
    public Session createSession() throws org.hibernate.HibernateException
    {
         return getSessionFactory().openSession(); 
    }

    public SessionFactory getSessionFactory() throws org.hibernate.HibernateException {
        
          return new Configuration ().configure().buildSessionFactory();
    }
    
    public String getDescription(){ return I18n.getString("connectionType.hibernate", "Hibernate connection"); }
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRHibernateConnectionEditor();
    }
    
    public void test() throws Exception
    {
        try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        Thread.currentThread().setContextClassLoader( MainFrame.getMainInstance().getReportClassLoader() );
                        SessionFactory hb_sessionFactory = null;
                        try {

                            hb_sessionFactory = new Configuration().configure().buildSessionFactory();

                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.getMainInstance(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                    }
                });
            } catch (Exception ex)
            {}
    }
}

