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
 * TreeJRField.java
 * 
 * Created on 2 agosto 2004, 23.48
 *
 */

package it.businesslogic.ireport.gui;

/**
 *
 * @author  Administrator
 */
public /**
 * This class is used in extended datasource tree.
 */
class TreeJRField
{
    private it.businesslogic.ireport.JRField field= null;
    private Class obj = null;
   
    public String toString()
    {
        if (field != null)
        {
            return field.getName() + " (" + this.getObj().getName() +")";
        }
        return "???";
    }
    
    /**
     * Getter for property field.
     * @return Value of property field.
     */
    public it.businesslogic.ireport.JRField getField() {
        return field;
    }
    
    /**
     * Setter for property field.
     * @param field New value of property field.
     */
    public void setField(it.businesslogic.ireport.JRField field) {
        this.field = field;
    }
    
    /**
     * Getter for property obj.
     * @return Value of property obj.
     */
    public java.lang.Class getObj() {
        return obj;
    }
    
    /**
     * Setter for property obj.
     * @param obj New value of property obj.
     */
    public void setObj(java.lang.Class obj) {
        this.obj = obj;
    }
    
}
