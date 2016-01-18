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
 * JRFakeObject.java
 * 
 * Created on September 21, 2006, 4:43 PM
 *
 */

package it.businesslogic.ireport.util;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRVariable;

/**
 *
 * @author gtoffoli
 */
public class JRFakeObject implements JRParameter, JRVariable, JRField {
    
    /** Creates a new instance of JRFakeParameter */
    public JRFakeObject() {
    }

    public String getName() {
        return null;
    }
    
    public Object getValue() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String string) {
    }

    public Class getValueClass() {
         return null;
    }

    public String getValueClassName() {
        return null;
    }

    public boolean isSystemDefined() {
        return false;
    }

    public boolean isForPrompting() {
        return false;
    }

    public JRExpression getDefaultValueExpression() {
        return null;
    }

    public Class getIncrementerFactoryClass() {
        return null;
    }

    public String getIncrementerFactoryClassName() {
        return null;
    }

    public byte getResetType() {
        return 0;
    }

    public byte getIncrementType() {
        return 0;
    }

    public byte getCalculation() {
        return 0;
    }

    public JRExpression getExpression() {
        return null;
    }

    public JRExpression getInitialValueExpression() {
        return null;
    }

    public JRGroup getResetGroup() {
        return null;
    }

    public JRGroup getIncrementGroup() {
        return null;
    }

    public JRPropertiesMap getPropertiesMap() {
        return null;
    }

	
    
}
