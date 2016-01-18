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
 * CrosstabReportElementWrapper.java
 * 
 * Created on February 14, 2006, 11:18 AM
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.CrosstabReportElement;

/**
 * This class is used to override the toString method in
 * the expression editor of a crosstab element...
 * @author gtoffoli
 */
public class CrosstabReportElementWrapper {
    
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of CrosstabReportElementWrapper */
    public CrosstabReportElementWrapper(CrosstabReportElement crosstabReportElement) {
        this.setCrosstabReportElement(crosstabReportElement);
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }
 
    public String toString()
    {
        if (getCrosstabReportElement() != null)
        {
            return getCrosstabReportElement().getName();
        }
        return "N/A";
    }
    
}
