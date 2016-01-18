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
 * ReportSubDatasetChangedEvent.java
 * 
 * Created on 17 giugno 2003, 1.12
 *
 */

package it.businesslogic.ireport.gui.event;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.*;
import java.util.*;
/**
 * Save all info required by a ReportListenerElementsSelectionEvent
 * Return the vector of selected elements (taked by the JReportFrame...)
 * @author  Administrator
 */
public class ReportSubDatasetChangedEvent {
    
    private SubDataset subDataset;
    
    /**
     *  Object removed
     */
    public static final int REMOVED = 1; // 0001
    
    /**
     *  Object added
     */
    public static final int ADDED   = 2; // 0010
    
    /**
     *  Object changed
     */
    public static final int CHANGED = 8; // 0100

    public static final int OBJECT_SUBDATASET = 1;
    public static final int OBJECT_FIELD =      2;
    public static final int OBJECT_PARAMETER =  3;
    public static final int OBJECT_VARIABLE =   4;
    public static final int OBJECT_QUERY =      5;
    public static final int OBJECT_GROUP =      6;

    /**
     *  Objects changed/removed/added
     */
    private Vector elements = new Vector();

    /**
     *  The type of the element
     */
    private int action = 0;
    private int objectType = 0;
    /** Creates a new instance of ReportListenerElementChangedEvent */
    public ReportSubDatasetChangedEvent(SubDataset subDataset, Object object, int action, int objectType) {
        this.subDataset = subDataset;
        this.setElements(new Vector());
        this.getElements().add(object);
        this.action = action;
        this.objectType = objectType;
    }
    
    public ReportSubDatasetChangedEvent(JReportFrame jReportFrame, Vector objects, int objectType) {
        this.subDataset = subDataset;
        this.setElements(objects);
        this.action = action;
        this.objectType = objectType;
    }
        
    public SubDataset getSubDataset() {
        return subDataset;
    }

    public void setSubDataset(SubDataset subDataset) {
        this.subDataset = subDataset;
    }

   /** Getter for property Action.
     *  Action can assume 3 values:
     *  REMOVED, ADDED, CHANGED 
     *  What is removed is specified in the objectType attribute
     *  @return Value of property type.
     *  
     */

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public Vector getElements() {
        return elements;
    }

    public void setElements(Vector elements) {
        this.elements = elements;
    }
    
}
