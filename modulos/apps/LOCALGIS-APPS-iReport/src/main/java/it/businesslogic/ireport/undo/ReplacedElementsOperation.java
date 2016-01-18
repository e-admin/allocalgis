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
 * ReplacedElementsOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;

import java.util.Enumeration;
import java.util.Vector;

/**
 *  This class handle the Insert operation.
 *  As all operations, the costructor take the JReportFrame (owner of the element)
 *  The ReportElement is not cloned, this can be a problem if not all undo operations
 *  are correctly logged and handled.
 * @author  Giulio Toffoli
 */
public class ReplacedElementsOperation implements it.businesslogic.ireport.UndoOperation {
    
    /*
     *  The report elements that was replaced.
     */
    private Vector elements = null; 
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of InsertElementOperation */
    public ReplacedElementsOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement) {
        this.setCrosstabReportElement(crosstabReportElement);
        this.elements = new Vector();
        this.jrf = jrf;
    }
    
    public ReplacedElementsOperation(JReportFrame jrf) {
        this(jrf,null);
    }
    
    public void redo()
    {
        // We must remove our element...
        if (jrf == null) return;
        Enumeration e = this.getElements().elements();
        Vector added_elements = new Vector();
        Vector removed_elements = new Vector();
        
        while ( e.hasMoreElements() )
        {
            ReplacedElementItem ei = (ReplacedElementItem)e.nextElement();
            ReportElement newElement = ei.getNewElement();
            ReportElement oldElement = ei.getOldElement();
              
            if (getCrosstabReportElement() != null)
            {
                int index = getCrosstabReportElement().getElements().indexOf( oldElement );
                getCrosstabReportElement().getElements().remove( oldElement );
                getCrosstabReportElement().getElements().add(index, newElement);
                added_elements.add(newElement);
                removed_elements.add(oldElement);
            }
            else
            {
                int index = jrf.getReport().getElements().indexOf( oldElement );
                jrf.getReport().getElements().remove( oldElement );
                jrf.getReport().getElements().add(index, newElement);
                added_elements.add(newElement);
                removed_elements.add(oldElement);
            }
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(),removed_elements , ReportElementChangedEvent.REMOVED));   
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), added_elements , ReportElementChangedEvent.ADDED)); 
        if (getCrosstabReportElement() != null) jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
        else jrf.getReportPanel().repaint();
    }
    
    public void undo()
    {
        // We must add our element...
        if (jrf == null) return;

        Vector added_elements = new Vector();
        Vector removed_elements = new Vector();
        
        for (int i= this.getElements().size()-1; i>=0; --i)
        {
            ReplacedElementItem ei = (ReplacedElementItem)getElements().get(i);
            ReportElement newElement = ei.getNewElement();
            ReportElement oldElement = ei.getOldElement();
              
            if (getCrosstabReportElement() != null)
            {
                int index = getCrosstabReportElement().getElements().indexOf( newElement );
                getCrosstabReportElement().getElements().remove( newElement );
                getCrosstabReportElement().getElements().add(index, oldElement);
                added_elements.add( oldElement );
                removed_elements.add(newElement);
            }
            else
            {
                int index = jrf.getReport().getElements().indexOf( newElement );
            jrf.getReport().getElements().remove( newElement );
            jrf.getReport().getElements().add(index, oldElement);
            added_elements.add( oldElement );
            removed_elements.add(newElement);
            }
            
        }
         jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), removed_elements , ReportElementChangedEvent.REMOVED));   
         jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), added_elements , ReportElementChangedEvent.ADDED));          
        
        if (getCrosstabReportElement() != null) jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
        else jrf.getReportPanel().repaint();
    }   
    
    public String toString()
    {
        return "Transformed element(s)";
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
     public void addElement(ReportElement oldEelement,ReportElement newElement ) {
         ReplacedElementItem ei = new ReplacedElementItem(oldEelement, newElement );
         getElements().add(ei);
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }
    
}




