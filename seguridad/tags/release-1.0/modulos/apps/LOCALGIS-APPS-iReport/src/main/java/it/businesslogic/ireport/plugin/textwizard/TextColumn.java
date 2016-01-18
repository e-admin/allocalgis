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
 * TextColumn.java
 * 
 * Created on 30 luglio 2004, 17.37
 *
 */

package it.businesslogic.ireport.plugin.textwizard;

/**
 *
 * @author  Administrator
 */
public class TextColumn {
    
    private String name = "";
    private int size = 0;
    
    public TextColumn(String name, int size)
    {
           this.name = name;
           this.size = size;
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
     * Getter for property size.
     * @return Value of property size.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Setter for property size.
     * @param size New value of property size.
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    public String toString()
    {
        return ""+getName();
        
    }
    
}
