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
 * FieldClassWrapper.java
 * 
 * Created on June 20, 2006, 12:03 PM
 *
 */

package it.businesslogic.ireport.data;

/**
 *
 * @author gtoffoli
 */
public class FieldClassWrapper {
    
    public String classType = null;
    public String fieldName = null;
    
    /** Creates a new instance of FieldClassWrapper */
    public FieldClassWrapper(String fieldName, String classType) {
        this.fieldName = fieldName;
        this.classType = classType;
    }
    
    public String toString()
    {
       return  fieldName + " (" + classType + ")";
    }
    
    public String getClassType()
    {
        return classType;
    }
    
    public void setClassType(String s)
    {
        classType = s;
    }
    
    public String getFieldName()
    {
        return fieldName;
    }
    
    public void setFieldName(String s)
    {
        fieldName = s;
    }
    
}
