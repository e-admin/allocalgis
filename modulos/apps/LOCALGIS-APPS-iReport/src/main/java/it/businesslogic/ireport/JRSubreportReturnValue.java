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
 * JRSubreportReturnValue.java
 * 
 * Created on 20 agosto 2005, 15.43
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author Administrator
 */
public class JRSubreportReturnValue {
    
    private String subreportVariable = "";
    private String toVariable = "";
    private String calculation = "Nothing";
    private String incrementFactoryClass = "";
    
    /**
     * Creates a new instance of JRSubreportReturnValue 
     */
    public JRSubreportReturnValue() {
    }

    public String getSubreportVariable() {
        return subreportVariable;
    }

    public void setSubreportVariable(String subreportVariable) {
        this.subreportVariable = subreportVariable;
    }

    public String getToVariable() {
        return toVariable;
    }

    public void setToVariable(String toVariable) {
        this.toVariable = toVariable;
    }

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public String getIncrementFactoryClass() {
        return incrementFactoryClass;
    }

    public void setIncrementFactoryClass(String incrementFactoryClass) {
        this.incrementFactoryClass = incrementFactoryClass;
    }
    
    public String toString()
    {
        return ""+subreportVariable;
    }
    
}
