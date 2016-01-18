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
 * ExpObject.java
 * 
 * Created on September 26, 2006, 11:40 AM
 *
 */

package it.businesslogic.ireport.gui.expbuilder;

/**
 *
 * @author gtoffoli
 */
public class ExpObject {
    
    public static final int TYPE_FIELD = 0;
    public static final int TYPE_VARIABLE = 1;
    public static final int TYPE_PARAM = 2;
    
    private String name = "";
    private int type = TYPE_FIELD;
    private String  classType = "java.lang.String";
    
    /** Creates a new instance of ExpObject */
    public ExpObject(String name, int type, String classType) {
        
        this.setName(name);
        this.setType(type);
        this.setClassType(classType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
    
    public String getExpression()
    {
        if (getType() == TYPE_FIELD) return "$F{" + getName() + "}";
        if (getType() == TYPE_VARIABLE) return "$V{" + getName() + "}";
        if (getType() == TYPE_PARAM) return "$P{" + getName() + "}";
        return getName();
    }
    
    
    
}
