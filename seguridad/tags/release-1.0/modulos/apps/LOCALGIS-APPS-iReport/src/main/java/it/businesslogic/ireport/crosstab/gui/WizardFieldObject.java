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
 * WizardFieldObject.java
 * 
 * Created on January 27, 2006, 3:42 PM
 *
 */

package it.businesslogic.ireport.crosstab.gui;

import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import java.util.Vector;

/**
 *
 * @author gtoffoli
 */
public class WizardFieldObject {
    
    private Object obj = null;
    
    /** Creates a new instance of WizardFieldObject */
    public WizardFieldObject(Object obj) {
        
        this.obj = obj;
        
    }
    
    public Vector getGroupByValues()
    {
        String classtype = "java.lang.String";
        Vector groupByValues = new Vector();
        
        groupByValues.add("Unique");
        
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            classtype = ((JRField)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            classtype = ((JRParameter)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            classtype = ((JRVariable)obj).getClassType();
        }
        
        try {
            Class clazz = this.getClass().forName( classtype );
            if ( java.util.Date.class.isAssignableFrom( clazz ))
            {
                groupByValues.add("Year");
                groupByValues.add("Month");
                groupByValues.add("Week");
                groupByValues.add("Day");
            }
        } catch (Exception ex)
        {}
        
      return groupByValues;
    }
    
    public Vector getFunctions()
    {
        String classtype = "java.lang.String";
        Vector functions = new Vector();
        
        functions.add("Count");
        
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            classtype = ((JRField)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            classtype = ((JRParameter)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            classtype = ((JRVariable)obj).getClassType();
        }
        
        try {
            Class clazz = this.getClass().forName( classtype );
            
            if (java.lang.Number.class.isAssignableFrom( clazz ))
            {
                functions.add("Sum");
                functions.add("Count");
                functions.add("DistinctCount");
                functions.add("Average");
                functions.add("StandardDeviation");
                functions.add("Variance");
            }
            
            if ( java.util.Date.class.isAssignableFrom( clazz ) || 
                 java.lang.Number.class.isAssignableFrom( clazz ))
            {
                functions.add("Lowest");
                functions.add("Highest");
            }
            
            
        } catch (Exception ex)
        {}
        
        functions.add("First");
        functions.add("Nothing");
        
      return functions;
    }
    
    public String toString()
    {
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            return ((JRField)obj).getName() + " (field)";
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            return ((JRParameter)obj).getName() + " (parameter)";
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            return ((JRVariable)obj).getName() + " (variable)";
        }
        
        return ""+obj;
    }
    
    public String getName()
    {
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            return ((JRField)obj).getName();
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            return ((JRParameter)obj).getName();
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            return ((JRVariable)obj).getName();
        }
        
        return ""+obj;
    }
    
    public String getExpression(String groupByType)
    {
        String expression = "";
        
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            expression = "$F{" + ((JRField)obj).getName() + "}";
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            expression = "$P{" + ((JRParameter)obj).getName() + "}";
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            expression = "$V{" + ((JRVariable)obj).getName() + "}";
        }
        
        if (groupByType.equals("Year"))
        {
            return "(new SimpleDateFormat(\"yyyy\")).format("+expression+")";
        }
        else if (groupByType.equals("Month"))
        {
            return "(new SimpleDateFormat(\"yyyy-MM\")).format("+expression+")";
        }
        else if (groupByType.equals("Week"))
        {
            return "(new SimpleDateFormat(\"yyyy-ww\")).format("+expression+")";
        }
        else if (groupByType.equals("Day"))
        {
            return "(new SimpleDateFormat(\"yyyy-MM-dd\")).format("+expression+")";
        }
        
        return expression;
    }
    
     public String getExpressionClass(String groupByType)
    {
        if (groupByType !=null && !groupByType.equals("Unique") )
        {
            return "java.lang.String";
        }
        
        if (obj instanceof it.businesslogic.ireport.JRField)
        {
            return ((JRField)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRParameter)
        {
            return ((JRParameter)obj).getClassType();
        }
        else if (obj instanceof it.businesslogic.ireport.JRVariable)
        {
            return ((JRVariable)obj).getClassType();
        }
        
        return "java.lang.String";
     }
}
