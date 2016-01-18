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
 * CrosstabParameter.java
 * 
 */

package it.businesslogic.ireport.crosstab;

public class CrosstabParameter
{
	private String name;
	private String classType;
	private String parameterValueExpression="";
    
	public  CrosstabParameter(String name, String classType, String parameterValueExpression)
	{
		this.name = name;
		this.setClassType(classType);
		this.setParameterValueExpression(parameterValueExpression);
	}
        
	public  CrosstabParameter(String name, String classType)
	{
                this(name, classType, "");
	}
        
        public  CrosstabParameter(String name)
	{
                this(name, "java.lang.String", "");
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
        
        public CrosstabParameter cloneMe()
        {
        	CrosstabParameter jrp = new CrosstabParameter(name, getClassType(), getParameterValueExpression());
        	return jrp;
        }

    public String getParameterValueExpression() {
        return parameterValueExpression;
    }

    public void setParameterValueExpression(String parameterValueExpression) {
        this.parameterValueExpression = parameterValueExpression;
    }
        
}
