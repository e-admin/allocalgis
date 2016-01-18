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
 * FormatElementsOperation.java
 * 
 * Created on 17 settembre 2003, 17.20
 *
 */

package it.businesslogic.ireport.undo;

import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.OperationType;
import it.businesslogic.ireport.gui.JReportFrame;
/**
 *
 * @author  Administrator
 */
public class FormatElementsOperation extends it.businesslogic.ireport.undo.TransformElementsOperation {
    
    public int operationType=0;
    /** Creates a new instance of FormatElementsOperation */
    public FormatElementsOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement, int operationType) {
        super(jrf,crosstabReportElement);
        this.operationType = operationType;
    }
    
  
     public String toString()
    {
        switch (operationType)
        {
            case OperationType.ALIGN_LEFT: return " align to left";
            default: return " elements format";
        }
    }
  
}
