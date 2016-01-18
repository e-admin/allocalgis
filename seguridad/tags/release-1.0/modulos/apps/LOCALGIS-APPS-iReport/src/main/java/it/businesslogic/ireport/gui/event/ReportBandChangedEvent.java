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
 * ReportBandChangedEvent.java
 * 
 * Created on 17 giugno 2003, 1.12
 *
 */

package it.businesslogic.ireport.gui.event;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.*;
/**
 * Save all info required by a ReportElementChangedEvent
 * Return the band changed/removed/added
 * @author  Administrator
 */
public class ReportBandChangedEvent {
    
    private JReportFrame jReportFrame;
    /**
     *  The band was removed
     */
    public static final int REMOVED = 1;
    /**
     *  An band was added
     */
    public static final int ADDED   = 2;
    /**
     *  The band was changed
     */
    public static final int CHANGED = 3;

    /**
     *  The band that is changed/removed/added
     */
    private Band band;
    
    /**
     *  Event source. This field can be null!
     */
    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
    /**
     *  The type of the element
     */
    private int type = 0;
    /** Creates a new instance of ReportBandChangedEvent */
    public ReportBandChangedEvent(JReportFrame jReportFrame, Band band, int type, Object source) {
        this.jReportFrame =jReportFrame;
        this.band = band;
        this.type = type;
        this.source = source;
    }
    
    public ReportBandChangedEvent(JReportFrame jReportFrame, Band band, int type) {
        this(jReportFrame, band,type, null);
    }
    /** Getter for property band.
     * @return Value of property band.
     *
     */
    public Band getBand() {
        return band;
    }
    
    /** Setter for property selectedElements.
     * @param selectedElements New value of property selectedElements.
     *
     */
    public void setBand(Band band) {
        this.band = band;
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
    
}
