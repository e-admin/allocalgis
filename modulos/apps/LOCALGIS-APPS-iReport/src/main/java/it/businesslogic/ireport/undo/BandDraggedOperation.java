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
 * BandDraggedOperation.java
 * 
 * Created on 19 giugno 2003, 23.23
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.Band;
import it.businesslogic.ireport.ReportElement;
import it.businesslogic.ireport.gui.JReportFrame;
import it.businesslogic.ireport.gui.event.ReportElementChangedEvent;

import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

/**
 *  This class handle the band dragged operation.
 *  As all operations, the costructor take the JReportFrame (owner of the element)
 *  The ReportElement is not cloned, this can be a problem if not all undo operations
 *  are correctly logged and handled.
 *  This undo operation contains the band dragged and all elements that was repositioned
 *  after the draging.
 *  It reuse a lot of code of TransformElementsOperation.
 * @author  Giulio Toffoli
 */
public class BandDraggedOperation implements it.businesslogic.ireport.UndoOperation {
    
    /*
     *  The report elements that was inserted.
     */
    private Vector transformations = null; 
    private int bandDelta = 0;
    
    private JReportFrame jrf = null;
    
    private Band band;
    
    /** Creates a new instance of InsertElementOperation */
    public BandDraggedOperation(JReportFrame jrf,  Band band) {
        this.transformations = new Vector();
        this.band = band;
        this.jrf = jrf;
    }
    
    public void redo()
    {
        if (jrf == null) return;

        band.setHeight( band.getHeight() + bandDelta);
        
        //jrf.setSelectedElement(null);
        Enumeration e = this.getTransformations().elements();
        while ( e.hasMoreElements() )
        {
            ElementTransformation pe = (ElementTransformation)e.nextElement();
            ReportElement element = pe.element;
            // Add element....
            //jrf.addSelectedElement( element );
            
            element.getPosition().x = pe.newBounds.x;
            element.getPosition().y = pe.newBounds.y;
            element.setWidth(pe.newBounds.width);
            element.setHeight(pe.newBounds.height);
            
            element.updateBounds();
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.CHANGED)); 
            //jrf.getSelectedElements().remove( element );
            //jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.REMOVED));          
        }
        jrf.setIsDocDirty(true);
        jrf.getReportPanel().repaint();
    }
    
    public void undo()
    {
        if (jrf == null) return;

        band.setHeight( band.getHeight() - bandDelta );
        
        //jrf.setSelectedElement(null);
        for (int i= this.getTransformations().size()-1; i>=0; --i)
        {
            ElementTransformation pe = (ElementTransformation)getTransformations().get(i);
            ReportElement element = pe.element;
            // Add element....
            //jrf.addSelectedElement( element );
            
            element.getPosition().x = pe.oldBounds.x;
            element.getPosition().y = pe.oldBounds.y;
            element.setWidth(pe.oldBounds.width);
            element.setHeight(pe.oldBounds.height);
            
            element.updateBounds();
            jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.CHANGED)); 
            //jrf.getSelectedElements().remove( element );
            //jrf.fireReportListenerReportElementsChanged(new ReportElementChangedEvent(jrf, element , ReportElementChangedEvent.REMOVED));          
        }
        jrf.setIsDocDirty(true);
        jrf.getReportPanel().repaint();
    }   
    
    public String toString()
    {
        return "band resize";
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
     public void addElement(ReportElement element, Rectangle oldBounds, Rectangle newBounds) {
         
        ElementTransformation et = new ElementTransformation();
        et.element = element;
        et.oldBounds = oldBounds;
        et.newBounds = newBounds;
        getTransformations().add(et);
    }
    
     /** Getter for property band.
      * @return Value of property band.
      *
      */
     public it.businesslogic.ireport.Band getBand() {
         return band;
     }
     
     /** Setter for property band.
      * @param band New value of property band.
      *
      */
     public void setBand(it.businesslogic.ireport.Band band) {
         this.band = band;
     }
     
     /** Getter for property bandDelta.
      * @return Value of property bandDelta.
      *
      */
     public int getBandDelta() {
         return bandDelta;
     }
     
     /** Setter for property bandDelta.
      * @param bandDelta New value of property bandDelta.
      *
      */
     public void setBandDelta(int bandDelta) {
         this.bandDelta = bandDelta;
     }
     
}

