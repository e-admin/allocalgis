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
 * JRProperty.java
 * 
 * Created on 10 marzo 2004, 18.08
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class JRProperty {
    
    private String name="";
    
    private String value="";
    
    /** Creates a new instance of JRProperty */
    public JRProperty() {
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
    
    /** Getter for property value.
     * @return Value of property value.
     *
     */
    public java.lang.String getValue() {
        return value;
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     *
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }
    
    public String toString()
    {
        return (name == null) ? "" : name;
    }
    
    public JRProperty cloneMe()
    {
        it.businesslogic.ireport.JRProperty property2 = new it.businesslogic.ireport.JRProperty();
            property2.setName(getName() );
            property2.setValue( getValue());
            
        return property2;
    }
}
