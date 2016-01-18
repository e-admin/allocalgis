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
 * JRLinkParameter.java
 * 
 * Created on 24 maggio 2003, 15.20
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class JRLinkParameter {
    
    private String expression;
    
    private String name;
    
    /** Creates a new instance of JRSubreportParameter */
    public JRLinkParameter() {
    }
    
    /** Creates a new instance of JRSubreportParameter */
    public JRLinkParameter(String name, String expression) {
           this.name = name;
           this.expression = expression;
    }
    
    /** Getter for property expression.
     * @return Value of property expression.
     *
     */
    public java.lang.String getExpression() {
        return expression;
    }
    
    /** Setter for property expression.
     * @param expression New value of property expression.
     *
     */
    public void setExpression(java.lang.String expression) {
        this.expression = expression;
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
    
    public String toString()
    {
        return name+"";
    }
    
    public JRSubreportParameter cloneMe()
	{
		JRSubreportParameter p =  new JRSubreportParameter( this.getName(), this.getExpression() );
                
                return p;
        }
}
