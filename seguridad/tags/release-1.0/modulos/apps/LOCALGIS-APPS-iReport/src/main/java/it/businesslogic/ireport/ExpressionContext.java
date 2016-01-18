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
 * ExpressionContext.java
 * 
 * Created on March 11, 2006, 6:09 PM
 *
 */

package it.businesslogic.ireport;

import java.util.Vector;

/**
 *
 * @author gtoffoli
 */
public class ExpressionContext {
    
    
    private Vector crosstabReportElements = new Vector();
    private SubDataset subDataset = null;
    
    /** Creates a new instance of ExpressionContext */
    public ExpressionContext() {
    }

    public Vector getCrosstabReportElements() {
        return crosstabReportElements;
    }

    public void setCrosstabReportElements(Vector crosstabReportElements) {
        this.crosstabReportElements = crosstabReportElements;
    }

    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
    }
    
    public void addCrosstabReportElement(CrosstabReportElement re)
    {
        if (!this.getCrosstabReportElements().contains(re))
        {
            getCrosstabReportElements().add(re);
        }
    }
    
}
