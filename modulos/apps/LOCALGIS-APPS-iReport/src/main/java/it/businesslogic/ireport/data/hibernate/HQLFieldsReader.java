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
 * HQLFieldsReader.java
 * 
 * Created on February 22, 2006, 1:27 PM
 *
 */

package it.businesslogic.ireport.data.hibernate;

import it.businesslogic.ireport.IReportConnection;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.connection.JRHibernateConnection;
import it.businesslogic.ireport.data.FieldClassWrapper;
import it.businesslogic.ireport.gui.MainFrame;
import it.businesslogic.ireport.gui.ReportQueryDialog;
import it.businesslogic.ireport.util.Misc;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import bsh.Interpreter;

/**
 *
 * @author gtoffoli
 */
public class HQLFieldsReader {
    
    private static final Map hibernateTypeMap;
    
	
    static
    {
            hibernateTypeMap = new HashMap();
            hibernateTypeMap.put(Boolean.class, Hibernate.BOOLEAN);
            hibernateTypeMap.put(Byte.class, Hibernate.BYTE);
            hibernateTypeMap.put(Double.class, Hibernate.DOUBLE);
            hibernateTypeMap.put(Float.class, Hibernate.FLOAT);
            hibernateTypeMap.put(Integer.class, Hibernate.INTEGER);
            hibernateTypeMap.put(Long.class, Hibernate.LONG);
            hibernateTypeMap.put(Short.class, Hibernate.SHORT);
            hibernateTypeMap.put(java.math.BigDecimal.class, Hibernate.BIG_DECIMAL);
            hibernateTypeMap.put(java.math.BigInteger.class, Hibernate.BIG_INTEGER);
            hibernateTypeMap.put(Character.class, Hibernate.CHARACTER);
            hibernateTypeMap.put(String.class, Hibernate.STRING);
            hibernateTypeMap.put(java.util.Date.class, Hibernate.DATE);
            hibernateTypeMap.put(java.sql.Timestamp.class, Hibernate.TIMESTAMP);
            hibernateTypeMap.put(java.sql.Time.class, Hibernate.TIME);
    }
    
    private Interpreter interpreter = null;
    private Vector  reportParameters = null;
    private String queryString = "";
    private HashMap queryParameters = new HashMap();
    
    private Vector notScalars = new Vector();
        
    /** Creates a new instance of HQLFieldsReader */
    public HQLFieldsReader(String query, Vector reportParameters) {
        
        
        this.setQueryString(query);
        this.setReportParameters(reportParameters);
    }
    
    
    public String prepareQuery() throws Exception
    {
        System.out.println(getReportParameters());
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
                       
               queryString = Misc.string_replace(":"+parameterReplacement, "$P{" + parameterName + "}", queryString);
               
               System.out.println( queryString );
           }
        } 
       return queryString;
    }
    
    
    public Vector readFields() throws Exception
    {
        prepareQuery();
        
        
        SessionFactory hb_sessionFactory = null;
        Session hb_session = null;
        Transaction transaction = null;
        
        notScalars.clear();
        
        try {
                IReportConnection conn = (IReportConnection)MainFrame.getMainInstance().getProperties().get("DefaultConnection");
               if (!(conn instanceof JRHibernateConnection))
               {
                   throw new Exception("No Hibernate connection selected.");
               }
               
               hb_session = ((JRHibernateConnection)conn).createSession();

                if (hb_session == null)
                {
                    throw new Exception("Problem creating the Session object for Hibernate");
                }

                transaction = hb_session.beginTransaction();
                Query q = hb_session.createQuery(getQueryString());
                
                Iterator paramIterator = queryParameters.keySet().iterator();
                
                while (paramIterator.hasNext())
                {
                    String hqlParamName = ""+paramIterator.next();
                    setParameter(hb_session,q, hqlParamName, queryParameters.get(hqlParamName));
                }

                q.setFetchSize(1);
                java.util.Iterator iterator = q.iterate();
                // this is a stupid thing: iterator.next();

                String[] aliases = q.getReturnAliases();
                Type[] types = q.getReturnTypes();

                
                Vector fields = new Vector();
                
                for (int i =0; i<types.length; ++i)
                {
                    if (types[i].isComponentType() ||
                        types[i].isEntityType())
                    {
                        
                        // look for alias...
                        String aliasName = null;
                        if (aliases != null && aliases.length > i && !aliases[i].equals(i+""))
                        {
                            aliasName = aliases[i];
                            JRField field = new JRField(aliases[i], types[i].getReturnedClass().getName());
                            field.setDescription(aliases[i]);
                            fields.add(field);
                            
                            
                        }
                        
                        // look for fields like for a javabean...
                        java.beans.PropertyDescriptor[] pd = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(types[i].getReturnedClass());
                        
                        if (aliasName != null)
                        {
                            notScalars.add( new FieldClassWrapper(aliasName, types[i].getReturnedClass().getName()));
                        }
                        else
                        {
                            notScalars.add( types[i].getReturnedClass().getName());
                        }
                        
                        for (int nd =0; nd < pd.length; ++nd)
                        {
                               String fieldName = pd[nd].getName();
                               if (pd[nd].getPropertyType() != null && pd[nd].getReadMethod() != null)
                               {
                                   if (fieldName.equals("class")) continue;
                                   
                                   String returnType =  pd[nd].getPropertyType().getName();
                                   
                                   it.businesslogic.ireport.JRField field = new it.businesslogic.ireport.JRField(fieldName, returnType);
                                   if (types.length > 1 && aliasName != null)
                                   {
                                       fieldName = aliasName+"."+fieldName;
                                       field.setDescription(fieldName); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
                                       field.setName(fieldName);
                                   }
                                   fields.add(field);
                               }
                        }
                        
                        
                    }
                    else
                    {
                        String fieldName = types[i].getName();
                        if (aliases != null && 
                            aliases.length > i &&
                            !aliases[i].equals(""+i)) fieldName = aliases[i];
                        JRField field = new JRField(fieldName, types[i].getReturnedClass().getName());
                        field.setDescription("");
                        fields.add(field);
                    }
                }
                /*
                else
                {
                    for (int i =0; i<types.length; ++i)
                    {
                        if (aliases != null && aliases.length > 0 && !aliases[0].equals(""+i))
                        {
                            JRField field = new JRField(aliases[i], types[i].getReturnedClass().getName());
                            field.setDescription("The whole entity/component object");
                            fields.add(field);
                            
                            
                        }
                        
                        
                    //    out.println(types[i].getName() + " " + types[i].getReturnedClass().getName() +  "<br>");
                    }
                }
                 */
                
                return fields;

           } catch (Exception ex)
           {
               ex.printStackTrace();
               throw ex;
            } finally {
               
                if (transaction != null) try {  transaction.rollback(); } catch (Exception ex) { }
               if (hb_session != null) try {  hb_session.close(); } catch (Exception ex) { }
           }
    }
    
    /**
     * Binds a paramter value to a query paramter.
     * 
     * @param parameter the report parameter
     */
    protected void setParameter(Session session, Query query, String hqlParamName, Object parameterValue) throws Exception
    {
            //ClassLoader cl = MainFrame.getMainInstance().getReportClassLoader();
            
            if (parameterValue == null)
            {
                System.out.println("Parameter: " + hqlParamName);
                query.setParameter(hqlParamName, parameterValue);
                return;
            }
            
            Class clazz = parameterValue.getClass();
            Type type = (Type) hibernateTypeMap.get(clazz);

            if (type != null)
            {
                    query.setParameter(hqlParamName, parameterValue, type);
            }
            else if (Collection.class.isAssignableFrom(clazz))
            {
                    query.setParameterList(hqlParamName, (Collection) parameterValue);
            }
            else
            {
                    if (session.getSessionFactory().getClassMetadata(clazz) != null) //param type is a hibernate mapped entity
                    {
                            query.setEntity(hqlParamName, parameterValue);
                    }
                    else //let hibernate try to guess the type
                    {
                            query.setParameter(hqlParamName, parameterValue);
                    }
            }
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

    public Vector getNotScalars() {
        return notScalars;
    }

    public void setNotScalars(Vector notScalars) {
        this.notScalars = notScalars;
    }

}
