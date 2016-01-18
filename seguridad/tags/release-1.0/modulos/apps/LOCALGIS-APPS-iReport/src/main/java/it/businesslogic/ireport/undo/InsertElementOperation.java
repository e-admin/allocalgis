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
 * InsertElementOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.gui.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;

import java.util.*;

/**
 *  This class handle the Insert operation.
 *  As all operations, the costructor take the JReportFrame (owner of the element)
 *  The ReportElement is not cloned, this can be a problem if not all undo operations
 *  are correctly logged and handled.
 * @author  Giulio Toffoli
 */
public class InsertElementOperation  implements it.businesslogic.ireport.UndoOperation {
    
    /*
     *  The report element that was inserted.
     */
    private ReportElement element = null; 
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of InsertElementOperation */
    public InsertElementOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement, ReportElement element) {
        this.crosstabReportElement = crosstabReportElement;
        this.element = element;
        this.jrf = jrf;
    }
    
    /** Creates a new instance of InsertElementOperation */
    public InsertElementOperation(JReportFrame jrf, ReportElement element) {
        this(jrf, null, element);
    }
    
    public void undo()
    {
        // We must remove our element...
        if (jrf == null && crosstabReportElement == null) return;
        if (crosstabReportElement != null)
        {
            crosstabReportElement.getElements().remove( element );
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().getSelectedElements().remove( element );
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().repaint();
        }
        else
        {
            jrf.getReport().getElements().remove( element );
            jrf.getSelectedElements().remove( element );
            if (element instanceof CrosstabReportElement)
            {
                jrf.removeCrosstabEditor((CrosstabReportElement)element);
            }
            jrf.getReportPanel().repaint();
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, element , ReportElementChangedEvent.REMOVED));  
        
    }
    
    public void redo()
    {
        if (jrf == null && crosstabReportElement == null) return;
        if (crosstabReportElement != null)
        {
            crosstabReportElement.getElements().addElement( element );
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().setSelectedElement( element );
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().repaint();
        }
        else
        {
            jrf.getReport().getElements().addElement( element );
            if (element instanceof CrosstabReportElement)
            {
                jrf.addCrosstabEditor((CrosstabReportElement)element);
            }
            jrf.setSelectedElement(element);
            jrf.getReportPanel().repaint();
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, element , ReportElementChangedEvent.ADDED));  
    }   
    
    public String toString()
    {
        return "insert element";
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }
}

