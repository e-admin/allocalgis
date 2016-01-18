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
 * ChangeEmentsOrderOperation.java
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
public class ChangeEmentsOrderOperation implements it.businesslogic.ireport.UndoOperation {
    
    /*
     *  The report elements that was inserted.
     */
    private Vector elements = null; 
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
        
    public ChangeEmentsOrderOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
         this.elements = new Vector();
        this.jrf = jrf;
    }
    
    public ChangeEmentsOrderOperation(JReportFrame jrf) {
        this(jrf,null);
    }
    
    public void redo()
    {
        // We must remove our element...
        if (jrf == null) return;
        Enumeration e = this.getElements().elements();

        Vector changed_elements = new Vector();
        while ( e.hasMoreElements() )
        {
            PositionedElement pe = (PositionedElement)e.nextElement();
            ReportElement element = pe.getElement();
            // Add element....
            changed_elements.add(element);
            if (getCrosstabReportElement() != null)
            {
                getCrosstabReportElement().getElements().remove( pe.getOldPosition() );
                getCrosstabReportElement().getElements().insertElementAt(element, pe.getNewPosition());
                jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().addSelectedElement( element , false);
                jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
            }
            else
            {
                jrf.getReport().getElements().remove( pe.getOldPosition() );
                jrf.getReport().getElements().insertElementAt(element, pe.getNewPosition());
                jrf.addSelectedElement( element , false);
                jrf.getReportPanel().repaint();
            }
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), changed_elements , ReportElementChangedEvent.CHANGED));

    }
    
    public void undo()
    {
        // We must add our element...
        if (jrf == null) return;
        
        jrf.setSelectedElement(null);
        Vector changed_elements = new Vector();
        
        for (int i= this.getElements().size()-1; i>=0; --i)
        {
            PositionedElement pe = (PositionedElement)getElements().get(i);
            ReportElement element = pe.getElement();
            changed_elements.add(element);
            // Add element....
            if (getCrosstabReportElement() != null)
            {
                getCrosstabReportElement().getElements().remove( pe.getNewPosition() );
                getCrosstabReportElement().getElements().insertElementAt(element, pe.getOldPosition());
                jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().addSelectedElement( element , false);
                jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
            }
            else
            {
                jrf.getReport().getElements().remove( pe.getNewPosition() );
                jrf.getReport().getElements().insertElementAt(element, pe.getOldPosition());
                jrf.addSelectedElement( element , false);
                jrf.getReportPanel().repaint();
            }
        }
        jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), changed_elements , ReportElementChangedEvent.CHANGED));
        
    }   
    
    public String toString()
    {
        return "change element(s) order";
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
     public void addElement(ReportElement element, int oldPosition, int newPostion) {
         
        PositionedElement pe = new PositionedElement(element, oldPosition, newPostion);
        getElements().add(pe);
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }
    
}

