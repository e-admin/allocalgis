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
 * ReportElementChangedEvent.java
 * 
 * Created on 17 giugno 2003, 1.12
 *
 */

package it.businesslogic.ireport.gui.event;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.CrosstabReportElement;
import it.businesslogic.ireport.gui.*;
import java.util.*;
/**
 * Save all info required by a ReportListenerElementsSelectionEvent
 * Return the vector of selected elements (taked by the JReportFrame...)
 * @author  Administrator
 */
public class ReportElementChangedEvent {
    
    private JReportFrame jReportFrame;
    private CrosstabReportElement crosstabReportElement;
    
    
    private Object eventSource = null;
    private String propertyChanged = null;
    private Object newValue = null;
    
    /**
     *  The element was removed
     */
    public static final int REMOVED = 1;
    /**
     *  An element was added
     */
    public static final int ADDED   = 2;
    /**
     *  The element was changed
     */
    public static final int CHANGED = 3;

    /**
     *  The element that is changed/removed/added
     */
    private Vector elements;
    
    /**
     *  The type of the element
     */
    private int type = 0;
    /** Creates a new instance of ReportListenerElementChangedEvent */
    public ReportElementChangedEvent(JReportFrame jReportFrame, CrosstabReportElement crosstabReportElement, ReportElement element, int type) {
        this.jReportFrame = jReportFrame;
        this.crosstabReportElement = crosstabReportElement;
        this.elements = new Vector();
        this.elements.add(element);
        this.type = type;
    }
    
    public ReportElementChangedEvent(JReportFrame jReportFrame, CrosstabReportElement crosstabReportElement, Vector elements, int type) {
        this.jReportFrame = jReportFrame;
        this.crosstabReportElement = crosstabReportElement;
        this.elements = elements;
        this.type = type;
    }
    
    public ReportElementChangedEvent(JReportFrame jReportFrame, ReportElement element, int type) {
        this(jReportFrame, null, element, type);
    }
    
    public ReportElementChangedEvent(JReportFrame jReportFrame, Vector elements, int type) {
        this(jReportFrame, null, elements, type);
    }
    
    /** Getter for property selectedElements.
     * @return Value of property selectedElements.
     *
     */
    public ReportElement getElement() {
        if (elements.size() > 0) return (ReportElement)elements.elementAt(0);
        return null;
    }
    
    /** Setter for property selectedElements.
     * @param selectedElements New value of property selectedElements.
     *
     */
    public void setElement(ReportElement element) {
        elements.removeAllElements();
        
        this.elements.add(element);
    }
    
    /** Getter for property type.
     *  Type can assume 3 values:
     *  REMOVED, ADDED, CHANGED 
     *  @return Value of property type.
     *  
     */
    public int getType() {
        return type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /** Getter for property jReportFrame.
     * @return Value of property jReportFrame.
     *
     */
    public it.businesslogic.ireport.gui.JReportFrame getJReportFrame() {
        return jReportFrame;
    }
    
    /** Setter for property jReportFrame.
     * @param jReportFrame New value of property jReportFrame.
     *
     */
    public void setJReportFrame(it.businesslogic.ireport.gui.JReportFrame jReportFrame) {
        this.jReportFrame = jReportFrame;
    }

    public Vector getElements() {
        return elements;
    }

    public void setElements(Vector elements) {
        this.elements = elements;
    }

    public CrosstabReportElement getCrosstabReportElement() {
        return crosstabReportElement;
    }

    public void setCrosstabReportElement(CrosstabReportElement crosstabReportElement) {
        this.crosstabReportElement = crosstabReportElement;
    }

    public Object getEventSource() {
        return eventSource;
    }

    public void setEventSource(Object eventSource) {
        this.eventSource = eventSource;
    }

    public String getPropertyChanged() {
        return propertyChanged;
    }

    public void setPropertyChanged(String propertyChanged) {
        this.propertyChanged = propertyChanged;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
    
}
