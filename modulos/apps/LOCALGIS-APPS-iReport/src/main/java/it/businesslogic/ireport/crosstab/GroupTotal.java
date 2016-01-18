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
 * GroupTotal.java
 * 
 * Created on February 13, 2006, 6:02 PM
 *
 */

package it.businesslogic.ireport.crosstab;

/**
 *
 * @author gtoffoli
 */
public class GroupTotal {
    
    private String name = "";
    private String groupName = "";
    private String classType = "java.lang.Double";
    
    /** Creates a new instance of GroupTotal */
    public GroupTotal(String name, String groupName, String classType) {
        this.setGroupName(groupName);
        this.setName(name);
        this.setClassType(classType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
    
    public String toString()
    {
        return name + " (total by " + groupName + ")";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getVarName() {
        return name + "_" + groupName + "_ALL";
    }
    
}
