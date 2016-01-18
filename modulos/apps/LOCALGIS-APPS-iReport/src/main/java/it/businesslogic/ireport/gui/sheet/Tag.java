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
 * Tag.java
 * 
 * Created on 4 ottobre 2004, 22.17
 *
 */

package it.businesslogic.ireport.gui.sheet;

/**
 *
 * @author  Administrator
 */
public class Tag {
    
    private Object value;
    private String name = "";
    
    public Tag(Object value, String name) {
        setName( name );
        setValue(value);
    }
    
    public Tag(String value) {
        setName( value );
        setValue(value);
    }
    
    public Tag(Object value) {
        setName( value+"");
        setValue(value);
    }
    
    public Tag(int value, String name) {
        setName( name );
        setValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = ""+value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString()
    {
        return getName();
    }
    
}
