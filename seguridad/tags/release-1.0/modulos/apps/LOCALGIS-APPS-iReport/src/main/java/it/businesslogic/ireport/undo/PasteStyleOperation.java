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
 * PasteStyleOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.gui.event.*;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.*;
import it.businesslogic.ireport.util.*;
import java.awt.*;

import java.util.*;

/**

 * @author  Giulio Toffoli
 */
public class PasteStyleOperation implements it.businesslogic.ireport.UndoOperation {
    
    private Vector transformations = null; 
    
    private JReportFrame jrf = null;
    private CrosstabReportElement crosstabReportElement = null;
    
    /** Creates a new instance of InsertElementOperation */
    public PasteStyleOperation(JReportFrame jrf, CrosstabReportElement crosstabReportElement) {
        this.setCrosstabReportElement(crosstabReportElement);
        this.transformations = new Vector();
        this.jrf = jrf;
    }
    
    /** Creates a new instance of InsertElementOperation */
    public PasteStyleOperation(JReportFrame jrf) {
        this(jrf, null);
    }
    
    public void redo()
    {
        if (jrf == null) return;

        //jrf.setSelectedElement(null);
        Enumeration e = this.getTransformations().elements();
        while ( e.hasMoreElements() )
        {
            PasteStyledElementItem ri = (PasteStyledElementItem)e.nextElement();
            ReportElement element = ri.getElement();
            JReportFrame.applyStyle( element,  ri.getNewStyle() );

            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), element , ReportElementChangedEvent.CHANGED)); 
            //jrf.getSelectedElements().remove( element );
            //jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.REMOVED));          
        }
        jrf.getMainFrame().getElementPropertiesDialog().updateSelection();
        if (getCrosstabReportElement() != null) jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
        else jrf.getReportPanel().repaint();
    }
    
    public void undo()
    {
        if (jrf == null) return;

        //jrf.setSelectedElement(null);
        Enumeration e = this.getTransformations().elements();
        while ( e.hasMoreElements() )
        {
            PasteStyledElementItem ri = (PasteStyledElementItem)e.nextElement();
            ReportElement element = ri.getElement();
            JReportFrame.applyStyle( element,  ri.getOriginalStyle() );

            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, getCrosstabReportElement(), element , ReportElementChangedEvent.CHANGED)); 
            //jrf.getSelectedElements().remove( element );
            //jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.REMOVED));          
        }
        jrf.getMainFrame().getElementPropertiesDialog().updateSelection();
        if (getCrosstabReportElement() != null) jrf.getCrosstabEditor(getCrosstabReportElement()).getPanelEditor().repaint();
        else jrf.getReportPanel().repaint();
    }   
    
    public String toString()
    {
        return "paste style";
    }
    
    /** Getter for property elements.
     * @return Value of property elements.
     *
     *  To add an element, use the addElement method. Don't access directly
     *  addElement from the vector elements 
     *  
     */
    public java.util.Vector getTransformations() {
        return transformations;
    }
    
    /** Setter for property elements.
     * @param elements New value of property elements.
     *
     */
    public void setTransformations(java.util.Vector transformations) {
        this.transformations = transformations;
    }
    
    /*
     *  Add an element to the list of elements handled by this
     *  undo operation. 
     *  You must supply the old and the new bound of you report.
     *  This make more simple the undo/redo operation...
     *  
     */
     public void addElement(ReportElement element, ReportElement originalStyle, ReportElement newStyle) {
         
        PasteStyledElementItem et = new PasteStyledElementItem(element,originalStyle,newStyle);
        getTransformations().add(et);
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }
    
}

