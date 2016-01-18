/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * SQLFieldsProvider.java
 *
 * Created on December 7, 2006, 9:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.data;

import bsh.Interpreter;
import it.businesslogic.ireport.FieldsProvider;
import it.businesslogic.ireport.FieldsProviderEditor;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.gui.ReportQueryDialog;
import it.businesslogic.ireport.util.Misc;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public class SQLFieldsProvider implements FieldsProvider {
    
    public static boolean useVisualDesigner = true;
    
    static {
        
        java.util.Properties p = new java.util.Properties();
        try {
            //java.io.InputStream is = SQLFieldsProvider.class.getClass().getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
            java.io.InputStream is = SQLFieldsProvider.class.getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
            
            p.load(  is );

            if (p.getProperty("sql").equals("0"))
            {
                useVisualDesigner = false;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    
    /** Creates a new instance of SQLFieldsProvider */
    public SQLFieldsProvider() {
        
        
    }
    
    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation() {
        return true;
    }

    public JRField[] getFields(IReportConnection irConn, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        
        if (irConn == null || !irConn.isJDBCConnection()) {
             throw new JRException("The active connection is not of type JDBC. Activate a JDBC connection first.");
        }
        
        String query = reportDataset.getQuery().getText();
        String error_msg = "";
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
                // look for parameters in the query and replace them with default values.
                // parameters look something like 
                // $P{QuoteGroupID}
                // or 
                // $P!{OrderByClause}
                java.util.List queryParams = new ArrayList();
                JRParameter[] jrParams = reportDataset.getParameters();
                for (int k=0; k<jrParams.length; ++k)
                {
                    JRParameter parameter = jrParams[k];

                    String p1 = "$P{" + parameter.getName() + "}";
                    String p2 = "$P!{" + parameter.getName() + "}";

                    Object defValue = parameters.get(parameter.getName());
                    
                    int ip1 = query.indexOf(p1);
                    while( ip1!=-1 ) {
                        // add a query parameter
                        if( defValue==null ) {
                            throw new IllegalArgumentException("Please set a " +
                            "default value for the parameter '" + 
                            parameter.getName() + "'" );
                        }

                        String before = query.substring(0, ip1);
                        String after = query.substring(ip1+p1.length());
                        query = before + " ? " + after;
                        queryParams.add( defValue );
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
                
                con = irConn.getConnection();
                
                ps = con.prepareStatement( query );
//                for(int pc=0; pc<queryParams.size(); pc++ ) {
//                    ps.setObject(pc+1, queryParams.get(pc) );
//                }
                
                
                for(int pc=0; pc<queryParams.size(); pc++ ) {
                	Class parameterType = queryParams.get(pc).getClass();
                	
                	if (java.lang.Boolean.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.BIT);
            			}
            			else
            			{
            				ps.setBoolean(pc+1, ((Boolean)queryParams.get(pc)).booleanValue());
            			}
            		}
            		else if (java.lang.Byte.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.TINYINT);
            			}
            			else
            			{
            				ps.setByte(pc+1, ((Byte)queryParams.get(pc)).byteValue());
            			}
            		}
            		else if (java.lang.Double.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.DOUBLE);
            			}
            			else
            			{
            				ps.setDouble(pc+1, ((Double)queryParams.get(pc)).doubleValue());
            			}
            		}
            		else if (java.lang.Float.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.FLOAT);
            			}
            			else
            			{
            				ps.setFloat(pc+1, ((Float)queryParams.get(pc)).floatValue());
            			}
            		}
            		else if (java.lang.Integer.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.INTEGER);
            			}
            			else
            			{
            				ps.setInt(pc+1, ((Integer)queryParams.get(pc)).intValue());
            			}
            		}
            		else if (java.lang.Long.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.BIGINT);
            			}
            			else
            			{
            				ps.setLong(pc+1, ((Long)queryParams.get(pc)).longValue());
            			}
            		}
            		else if (java.lang.Short.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.SMALLINT);
            			}
            			else
            			{
            				ps.setShort(pc+1, ((Short)queryParams.get(pc)).shortValue());
            			}
            		}
            		else if (java.math.BigDecimal.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.DECIMAL);
            			}
            			else
            			{
            				ps.setBigDecimal(pc+1, (BigDecimal)queryParams.get(pc));
            			}
            		}
            		else if (java.lang.String.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.VARCHAR);
            			}
            			else
            			{
            				ps.setString(pc+1, queryParams.get(pc).toString());
            			}
            		}
            		else if (java.sql.Timestamp.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.TIMESTAMP);
            			}
            			else
            			{
            				ps.setTimestamp( pc+1, (java.sql.Timestamp)queryParams.get(pc) );
            			}
            		}
            		else if (java.sql.Time.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.TIME);
            			}
            			else
            			{
            				ps.setTime( pc+1, (java.sql.Time)queryParams.get(pc) );
            			}
            		}
            		else if (java.util.Date.class.isAssignableFrom(parameterType))
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.DATE);
            			}
            			else
            			{
            				ps.setDate( pc+1, new java.sql.Date( ((java.util.Date)queryParams.get(pc)).getTime() ) );
            			}
            		}
            		else
            		{
            			if (queryParams.get(pc) == null)
            			{
            				ps.setNull(pc+1, Types.JAVA_OBJECT);
            			}
            			else
            			{
            				ps.setObject(pc+1, queryParams.get(pc));
            			}
            		}
                }
                
                
                
                // Some JDBC drivers don't supports this method...
                try { ps.setFetchSize(0); } catch(Exception e ) {}
                
                
                ResultSet rs = ps.executeQuery();
                
                //if (in < num) return;
                
                ResultSetMetaData rsmd = rs.getMetaData();

                //if (in < num) return;
                
                List columns = new ArrayList();
                for (int i=1; i <=rsmd.getColumnCount(); ++i) {
                    JRDesignField field = new JRDesignField();
                    field.setName( rsmd.getColumnLabel(i) );
                    field.setValueClassName( Misc.getJdbcTypeClass(rsmd, i) );
                    field.setDescription("");
                    columns.add( field );
                }

                JRField[] final_fields = new JRField[columns.size()];
                for (int i=0; i<final_fields.length; ++i)
                {
                    final_fields[i] = (JRField)columns.get(i);
                }
                
                return final_fields;
                
            } catch( IllegalArgumentException ie ) {
                throw new JRException( ie.getMessage() );
            } catch (NoClassDefFoundError ex) {
            	ex.printStackTrace();
                error_msg = "NoClassDefFoundError!!\nCheck your classpath!";
                throw new JRException( error_msg );
            } catch (java.sql.SQLException ex) {
                error_msg = "SQL problems:\n"+ex.getMessage();
                throw new JRException( error_msg );
            } catch (Exception ex) {
                ex.printStackTrace();
                error_msg = "General problem:\n"+ex.getMessage()+
                    "\n\nCheck username and password; is the DBMS active ?!";
                throw new JRException( error_msg );
            } catch (Throwable t)
            {
              throw new JRException( t.getMessage() );
            } finally {
                if(ps!=null) try { ps.close(); } catch(Exception e ) {}
                if(con !=null) try { con.close(); } catch(Exception e ) {}
            }            
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return useVisualDesigner;
    }

    public boolean hasEditorComponent() {
        return false;
    }

    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        // Start FREE QUERY BUILDER....
            QueryBuilderDialog qbd = new QueryBuilderDialog( (reportQueryDialog != null) ? reportQueryDialog : new JDialog(), true);

            if (con.isJDBCConnection())
            {
                qbd.setConnection(  con.getConnection() );
            }
        
        try {
            
            if (query != null && query.length() > 0)
            {
                qbd.setQuery(query);
            }
        } catch (Throwable ex)
        {
            if (reportQueryDialog != null)
            {
                reportQueryDialog.getJLabelStatusSQL().setText("I'm sorry, I'm unable to parse the query...");
                ex.printStackTrace();
            }
            ex.printStackTrace();
            return null;
        }
        qbd.setVisible(true);
        
        if (qbd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            return qbd.getQuery();
        }
        return null;
    }

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {
        return null;
    }
    
}
