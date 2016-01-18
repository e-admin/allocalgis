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
 * JRParameter.java
 * 
 */

package it.businesslogic.ireport;

import java.util.Iterator;

public class JRParameter
{
	private String name;
	private String classType;
	private String description="";
	private String defaultValueExpression="";
	private boolean isForPrompting = true;
	private boolean builtin = false;
        private Object lastDefaultValue = null;
        
        private java.util.List properties = new java.util.ArrayList();
    
	public  JRParameter(String name, String classType, boolean isForPrompting, String description, boolean builtin)
	{
		this.name = name;
		this.classType = classType;
		this.isForPrompting = isForPrompting;
		this.description = description;
                this.builtin = builtin;
	}
        
	public  JRParameter(String name, String classType, boolean isForPrompting, String description)
	{
                this(name, classType, isForPrompting, description, false);
	}
	
	public  JRParameter(String name, String classType, boolean isForPrompting)
	{
		this(name, classType, isForPrompting, "", false);
	}
	
	public  JRParameter(String name, String classType)
	{
                this(name, classType, true, "", false);
	}
		
	public String toString()
	{
		return name;
	}
        
        /** Getter for property classType.
         * @return Value of property classType.
         *
         */
        public java.lang.String getClassType() {
            return classType;
        }
        
        /** Setter for property classType.
         * @param classType New value of property classType.
         *
         */
        public void setClassType(java.lang.String classType) {
            this.classType = classType;
        }
        
        /** Getter for property defaultValueExpression.
         * @return Value of property defaultValueExpression.
         *
         */
        public java.lang.String getDefaultValueExpression() {
            return defaultValueExpression;
        }
        
        /** Setter for property defaultValueExpression.
         * @param defaultValueExpression New value of property defaultValueExpression.
         *
         */
        public void setDefaultValueExpression(java.lang.String defaultValueExpression) {
            this.defaultValueExpression = defaultValueExpression;
        }
        
        /** Getter for property description.
         * @return Value of property description.
         *
         */
        public java.lang.String getDescription() {
            return description;
        }
        
        /** Setter for property description.
         * @param description New value of property description.
         *
         */
        public void setDescription(java.lang.String description) {
            this.description = description;
        }
        
        /** Getter for property isForPrompting.
         * @return Value of property isForPrompting.
         *
         */
        public boolean isIsForPrompting() {
            return isForPrompting;
        }
        
        /** Setter for property isForPrompting.
         * @param isForPrompting New value of property isForPrompting.
         *
         */
        public void setIsForPrompting(boolean isForPrompting) {
            this.isForPrompting = isForPrompting;
        }
        
        /** Getter for property name.
         * @return Value of property name.
         *
         */
        public java.lang.String getName() {
            return name;
        }
        
        /** Setter for property name.
         * @param name New value of property name.
         *
         */
        public void setName(java.lang.String name) {
            this.name = name;
        }
        
        public JRParameter cloneMe()
        {
        	JRParameter jrp = new JRParameter(name, classType, isForPrompting, description);
        	
        	jrp.setDefaultValueExpression( defaultValueExpression );
		jrp.setBuiltin( isBuiltin());
        	
                Iterator iter = getProperties().iterator();
                while (iter.hasNext())
                {
                    JRProperty p = (JRProperty)iter.next();
                    jrp.getProperties().add( p.cloneMe() );
                }
                
        	return jrp;
        }

    public boolean isBuiltin() {
        return builtin;
    }

    public void setBuiltin(boolean builtin) {
        this.builtin = builtin;
    }
    
    public Object getLastDefaultValue() {
        return lastDefaultValue;
    }

    public void setLastDefaultValue(Object lastDefaultValue) {
        this.lastDefaultValue = lastDefaultValue;
    }

    public java.util.List getProperties() {
        return properties;
    }

    public void setProperties(java.util.List properties) {
        this.properties = properties;
    }
        
}
