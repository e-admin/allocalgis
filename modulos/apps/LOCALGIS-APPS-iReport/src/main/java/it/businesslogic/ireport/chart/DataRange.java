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
 * DataRange.java
 * 
 * Created on September 28, 2006, 4:58 PM
 *
 */

package it.businesslogic.ireport.chart;

/**
 *
 * @author gtoffoli
 */
public class DataRange {
    
    private String lowExpression = "";
    private String highExpression = "";
    
    /** Creates a new instance of DataRange */
    public DataRange() {
    }
    
    public DataRange cloneMe()
    {
        DataRange obj = new DataRange();
        obj.setHighExpression( new String( highExpression ));
        obj.setLowExpression( new String( lowExpression ));
        
        return obj;
    }
    

    public String getHighExpression() {
        return highExpression;
    }

    public void setHighExpression(String highExpression) {
        this.highExpression = highExpression;
    }

    public String getLowExpression() {
        return lowExpression;
    }

    public void setLowExpression(String lowExpression) {
        this.lowExpression = lowExpression;
    }
    
}
