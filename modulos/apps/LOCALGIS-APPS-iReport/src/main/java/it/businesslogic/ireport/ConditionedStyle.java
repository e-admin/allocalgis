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
 * ConditionedStyle.java
 * 
 * Created on February 17, 2006, 5:23 PM
 *
 */

package it.businesslogic.ireport;


/**
 *
 * @author gtoffoli
 */
public class ConditionedStyle extends Style {
    
    /** Creates a new instance of ConditionedStyle */
    public ConditionedStyle() {
    }
    
    /**
         * Create a new instance of ConditionedStyle with the same values of s1
         * isDefault is reset to false.
         */
    public ConditionedStyle(Style s1) {
        super(s1);
    }
    
    /**
     * this is a shortcut for  getAttribute( this.ATTRIBUTE_condition )
     * If no condition is set, the method return null;
     */
    public String getCondition()
    {
        return (String)this.getAttribute( this.ATTRIBUTE_condition );
    }
    
    /**
     * this is a shortcut for  getAttribute( this.ATTRIBUTE_condition )
     * If no condition is set, the method return null;
     */
    public void setCondition(String condition)
    {
        if (condition != null && condition.trim().length() > 0)
        {
            this.getAttributes().put( this.ATTRIBUTE_condition, condition);
        }
    }
    
    public String toString()
    {
        return  getCondition();
    }
}
