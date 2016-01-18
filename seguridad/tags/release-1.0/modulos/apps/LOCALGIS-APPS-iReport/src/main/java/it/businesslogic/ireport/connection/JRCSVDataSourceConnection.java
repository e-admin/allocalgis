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
 * JRCSVDataSourceConnection.java
 * 
 * Created on 4 giugno 2003, 18.15
 *
 */

package it.businesslogic.ireport.connection;
import it.businesslogic.ireport.IReportConnectionEditor;
import it.businesslogic.ireport.connection.gui.CSVDataSourceConnectionEditor;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.util.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.data.JRCsvDataSource;

/**
 *
 * @author  Administrator
 */
public class JRCSVDataSourceConnection extends it.businesslogic.ireport.IReportConnection {
    
    private String name;
    private String recordDelimiter = "\n";
    private String fieldDelimiter = ",";
    private boolean useFirstRowAsHeader = false;
    private String customDateFormat = "";
        
    private String filename;
    
    private Vector columnNames = new Vector();
    
    /** Creates a new instance of JDBCConnection */   
    public JRCSVDataSourceConnection() {
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
    
    /*
     *  This method return all properties used by this connection
     */
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("Filename", Misc.nvl(this.getFilename() ,"") );    
        map.put("recordDelimiter", Misc.nvl(this.getRecordDelimiter(),"\n") );
        map.put("fieldDelimiter", Misc.nvl(this.getFieldDelimiter() ,"") );
        map.put("useFirstRowAsHeader", Misc.nvl(""+this.isUseFirstRowAsHeader() ,"") );
        map.put("customDateFormat", Misc.nvl(this.getCustomDateFormat() ,"") );
        
        for (int i=0; i< getColumnNames().size(); ++i)
        {
            map.put("COLUMN_" + i,getColumnNames().elementAt(i) );
        }
        return map;
    }
    
    public void loadProperties(java.util.HashMap map)
    {
        this.setFilename( (String)map.get("Filename"));
        this.setRecordDelimiter( (String)map.get("recordDelimiter"));
        this.setFieldDelimiter( (String)map.get("fieldDelimiter"));
        this.setUseFirstRowAsHeader( ((String)map.get("useFirstRowAsHeader")).equals("true"));
        this.setCustomDateFormat( (String)map.get("customDateFormat"));
        
        int i = 0;
        while (map.containsKey("COLUMN_" + i))
        {
           getColumnNames().add( map.get("COLUMN_" + i));
           i++;
        }
        
    }
    
    /**
     * Getter for property filename.
     * @return Value of property filename.
     */
    public java.lang.String getFilename() {
        return filename;
    }    
   
    /**
     * Setter for property filename.
     * @param filename New value of property filename.
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
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
        
        try {
        JRCsvDataSource ds = new JRCsvDataSource(new File(getFilename()));
        if (this.getCustomDateFormat() != null && this.getCustomDateFormat().length() > 0)
        {
            ds.setDateFormat(new SimpleDateFormat(this.getCustomDateFormat()));
        }
        
        ds.setFieldDelimiter( getFieldDelimiter().charAt(0) );
        ds.setRecordDelimiter( getRecordDelimiter());
        ds.setUseFirstRowAsHeader( isUseFirstRowAsHeader());
        
        if (!isUseFirstRowAsHeader())
        {
            String[] names = new String[getColumnNames().size()];
            for (int i=0; i<names.length; ++i )
            {
                names[i] = ""+getColumnNames().elementAt(i);
            }
            ds.setColumnNames( names );
        }
        
        return ds;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }

    public boolean isUseFirstRowAsHeader() {
        return useFirstRowAsHeader;
    }

    public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader) {
        this.useFirstRowAsHeader = useFirstRowAsHeader;
    }

    public String getRecordDelimiter() {
        return recordDelimiter;
    }

    public void setRecordDelimiter(String recordDelimiter) {
        this.recordDelimiter = recordDelimiter;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getCustomDateFormat() {
        return customDateFormat;
    }

    public void setCustomDateFormat(String customDateFormat) {
        this.customDateFormat = customDateFormat;
    }

    public Vector getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Vector columnNames) {
        this.columnNames = columnNames;
    }
    
    public String getDescription(){ return I18n.getString("connectionType.csv", "File CSV datasource"); }
    
        public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new CSVDataSourceConnectionEditor();
    }
        
    public void test() throws Exception 
    {
            String csv_file = getFilename();
            
            try {
                
                JRCSVDataSourceConnection con = new JRCSVDataSourceConnection();
                java.io.File f = new java.io.File(csv_file);
                if (!f.exists())
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),
                         I18n.getFormattedString("messages.connectionDialog.fileNotFound","File {0} not found", new Object[]{csv_file}),
                         I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                
                con.setFilename( csv_file );
                if (con.getJRDataSource() != null)
                {
                    JOptionPane.showMessageDialog(MainFrame.getMainInstance(),I18n.getString("messages.connectionDialog.connectionTestSuccessful","Connection test successful!"),"",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(MainFrame.getMainInstance(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
                return;	
            }        
    }
    
}

