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
 * DeleteElementsOperation.java
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
public class DeleteElementsOperation implements it.businesslogic.ireport.UndoOperation {
    
    /*
     *  The report elements that was inserted.
     */
    private Vector elements = null; 
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of InsertElementOperation */
    public DeleteElementsOperation(JReportFrame jrf) {
        this.elements = new Vector();
        this.jrf = jrf;
    }
    
    public DeleteElementsOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
        this.elements = new Vector();
        this.jrf = jrf;
    }
    
    public void redo()
    {
        // We must remove our element...
        if (jrf == null && crosstabReportElement == null) return;
        Enumeration e = this.getElements().elements();
        Vector changed_elements = new Vector();
        while ( e.hasMoreElements() )
        {
            PositionedElement pe = (PositionedElement)e.nextElement();
            ReportElement element = pe.getElement();
            
            if (crosstabReportElement != null)
            {
                jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().getSelectedElements().remove( element );
                crosstabReportElement.getElements().remove( element );
                jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().repaint();
            }
            else
            {
                jrf.getSelectedElements().remove( element );
                jrf.getReport().getElements().remove( element );
                if (element instanceof CrosstabReportElement)
                {
                    jrf.removeCrosstabEditor((CrosstabReportElement)element);
                }
                jrf.getReportPanel().repaint();
            }
            changed_elements.add(element);
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, changed_elements , ReportElementChangedEvent.REMOVED));          
        
    }
    
    public void undo()
    {
        // We must add our element...
        if (jrf == null && crosstabReportElement == null) return;
        Vector changed_elements = new Vector();
        for (int i= this.getElements().size()-1; i>=0; --i)
        {
            PositionedElement pe = (PositionedElement)getElements().get(i);
            ReportElement element = pe.getElement();
            // Add element....
            
            if (crosstabReportElement != null)
            {
                
                crosstabReportElement.getElements().insertElementAt(element, pe.getOldPosition());
            }
            else
            {
                jrf.getReport().getElements().insertElementAt(element, pe.getOldPosition());
                //jrf.addSelectedElement( element, false );
                if (element instanceof CrosstabReportElement)
                {
                    jrf.addCrosstabEditor((CrosstabReportElement)element);
                }
            }
            changed_elements.add( element );
            //jrf.getSelectedElements().remove( element );
            //jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.REMOVED));          
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, crosstabReportElement, changed_elements , ReportElementChangedEvent.ADDED)); 
        
        if (crosstabReportElement != null)
        {
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().setSelectedElements( changed_elements );
            jrf.getCrosstabEditor(crosstabReportElement).getPanelEditor().repaint();
        }
        else
        {
            jrf.setSelectedElements( changed_elements );    
            jrf.getReportPanel().repaint();
        }
    }   
    
    public String toString()
    {
        return "delete element(s)";
    }
    
    /** Getter for property elements.
     * @return Value of property elements.
     *
     *  To add an element, use the addElement method. Don't access directly
     *  addElement from the vector elements 
     *  
     */
    public java.util.Vector getElements() {
        return elements;
    }
    
    /** Setter for property elements.
     * @param elements New value of property elements.
     *
     */
    public void setElements(java.util.Vector elements) {
        this.elements = elements;
    }
    
    /*
     *  Add an element to the list of elements handled by this
     *  undo operation. The position is relative to the z position
     *  in the elements vector.
     *  If the undo mechanism works fine, the requested position is
     *  ever available.
     *  The position value should be retrived when the element
     *  is added.
     */
     public void addElement(ReportElement element, int position) {
         
        PositionedElement pe = new PositionedElement(element, position, position);
        getElements().add(pe);
    }
    
}




