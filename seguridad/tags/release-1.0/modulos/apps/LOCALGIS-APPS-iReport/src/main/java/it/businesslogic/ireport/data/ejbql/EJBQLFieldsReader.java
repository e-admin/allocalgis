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
 * EJBQLFieldsReader.java
 * 
 * Created on February 22, 2006, 1:27 PM
 *
 */

package it.businesslogic.ireport.data.ejbql;

import bsh.Interpreter;
import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.connection.EJBQLConnection;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.ReportQueryDialog;
import it.businesslogic.ireport.util.Misc;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 *
 * @author gtoffoli
 */
public class EJBQLFieldsReader {
    
    private Interpreter interpreter = null;
    private Vector  reportParameters = null;
    private String queryString = "";
    private HashMap queryParameters = new HashMap();
    private String singleClassName = null;
        
    /** Creates a new instance of HQLFieldsReader */
    public EJBQLFieldsReader(String queryStr, Vector reportParameters) {
        
        this.setQueryString(queryStr);
        this.setReportParameters(reportParameters);
    }
    
    public String prepareQuery() throws Exception
    {
       Enumeration enumParams = getReportParameters().elements();
       
       while( enumParams.hasMoreElements() ) {
           
           JRParameter param = (JRParameter)enumParams.nextElement();
           String parameterName = param.getName();
           
           if (queryString.indexOf("$P!{" + parameterName + "}") > 0)
           {
                Object paramVal = ReportQueryDialog.recursiveInterpreter( getInterpreter(), param.getDefaultValueExpression(),getReportParameters());
           
                if (paramVal == null)
                {
                    paramVal = "";
                }
                
                queryString = Misc.string_replace(""+paramVal, "$P!{" + parameterName + "}", queryString);
           }
           else if (getQueryString().indexOf("$P{" + parameterName + "}") > 0)
           {
               Object paramVal = ReportQueryDialog.recursiveInterpreter( getInterpreter(), param.getDefaultValueExpression(),getReportParameters());
               String parameterReplacement = "_" + getLiteral(parameterName);
               
               queryParameters.put(parameterReplacement, paramVal);

           }
        } 
       return queryString;
    }
    
    
    /**
     * Binds a paramter value to a query paramter.
     * 
     * @param parameter the report parameter
     */
    protected void setParameter(Query query, String hqlParamName, Object parameterValue) throws Exception
    {
            //ClassLoader cl = MainFrame.getMainInstance().getReportClassLoader();
            
            if (parameterValue == null)
            {
                query.setParameter( getLiteral( hqlParamName ), parameterValue);
                return;
            }
            
            query.setParameter(hqlParamName, parameterValue);
    } 
    
    private Interpreter prepareExpressionEvaluator() throws bsh.EvalError {
        
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(interpreter.getClass().getClassLoader());
        return interpreter;
    }
    
    
    /**
	 * Takes a name and returns the same if it is a Java identifier;
	 * else it substitutes the illegal characters so that it can be an identifier
	 *
	 * @param name
	 */
	public static String getLiteral(String name)
	{
		if (isValidLiteral(name))
		{
			return name;
		}

		StringBuffer buffer = new StringBuffer(name.length() + 5);
		
		char[] literalChars = new char[name.length()];
		name.getChars(0, literalChars.length, literalChars, 0);
		
		for (int i = 0; i < literalChars.length; i++)
		{
			if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
			{
				buffer.append((int)literalChars[i]);
			}
			else if (i != 0 && !Character.isJavaIdentifierPart(literalChars[i]))
			{
				buffer.append((int)literalChars[i]);
			}
			else
			{
				buffer.append(literalChars[i]);
			}
		}
		
		return buffer.toString();
	}
	
	
	/**
	 * Checks if the input is a valid Java literal
	 * @param literal
	 * @author Gaganis Giorgos (gaganis@users.sourceforge.net) 
	 */
	private static boolean isValidLiteral(String literal)
	{
		boolean result = true;
		
		char[] literalChars = new char[literal.length()];
		literal.getChars(0, literalChars.length, literalChars, 0);
		
		for (int i = 0; i < literalChars.length; i++)
		{
			if (i == 0 && !Character.isJavaIdentifierStart(literalChars[i]))
			{
				result = false;
				break;
			}
			
			if (i != 0 && !Character.isJavaIdentifierPart(literalChars[i]))
			{
				result = false;
				break;
			}
		}
		
		return result;
	}

    public Interpreter getInterpreter() {
        
        if (interpreter == null)
        {
            try {
            interpreter = prepareExpressionEvaluator();
            } catch (Exception ex)
            {
            
            }
        }
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Vector getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(Vector reportParameters) {
        this.reportParameters = reportParameters;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public HashMap getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(HashMap queryParameters) {
        this.queryParameters = queryParameters;
    }
    

    public Vector readFields() throws Exception
    {
        prepareQuery();
        
        Vector fields = new Vector();
        EntityManager em = null;
        Query query = null;
        setSingleClassName(null);
        try {
            
               IReportConnection conn = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
               if (!(conn instanceof EJBQLConnection))
               {
                   throw new Exception("No EJBQL connection selected.");
               }
               
               em = ((EJBQLConnection)conn).getEntityManager();
               
               query = em.createQuery(queryString);
               
               for (Iterator iter = queryParameters.keySet().iterator(); iter.hasNext();) {
                      String parameterName = (String)iter.next();
                      query.setParameter( parameterName, queryParameters.get(parameterName ));
                }
                
               query.setMaxResults(1);
	       List list = query.getResultList();
                        
               
               if (list.size() > 0)
               {
                   Object obj = list.get(0);
                   
                   if (obj != null &&
                       obj.getClass().isArray())
                   {
                       // Fields array...
                       Object[] fiels_obj = (Object[])obj;
                       for (int i=0; i<fiels_obj.length; ++i)
                       {
                          fields.add( createField( fiels_obj[i], i)); 
                       }
                   }
                   else
                   {
                       setSingleClassName(obj.getClass().getName());
                        fields = getFields(obj); 
                   }
               }
                
                return fields;

           } catch (Exception ex)
           {
               ex.printStackTrace();
               throw ex;
            } finally {
               
              
           }
    }
    
    public Vector getFields(Object obj)
    {
        
        Vector fields = new Vector();
        java.beans.PropertyDescriptor[] pd = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(obj.getClass());
        for (int nd =0; nd < pd.length; ++nd)
        {
                   String fieldName = pd[nd].getName();
                   if (pd[nd].getPropertyType() != null && pd[nd].getReadMethod() != null)
                   {
                       String returnType =  pd[nd].getPropertyType().getName();
                       it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, Misc.getJRFieldType(returnType));
                       field.setDescription(""); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                       fields.addElement(field);
                   }
        }
        
        return fields;
        
        
    }
    
    public JRField createField(Object obj, int pos)
    {
        String fieldName = "COLUMN_" + (pos+1);
        if (pos < 0)
        {
            fieldName = obj.getClass().getName();
            if (fieldName.indexOf(".") > 0)
            {
                fieldName = fieldName.substring(fieldName.indexOf(".")+1);
            }
            if (fieldName.length() == 0) fieldName = "COLUMN_1";
        }
        JRField field = new JRField(fieldName, obj.getClass().getName());
        field.setDescription("");
        
        return field;
    }

    public String getSingleClassName() {
        return singleClassName;
    }

    public void setSingleClassName(String singleClassName) {
        this.singleClassName = singleClassName;
    }

}
