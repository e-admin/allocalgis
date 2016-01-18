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
 * StyleChangedEvent.java
 * 
 * Created on 10 febbraio 2003, 2.04
 *
 */

package it.businesslogic.ireport.gui.table;

import org.jdesktop.swingx.decorator.SortOrder;

/**
 *
 * @author  Administrator
 */
public class SortChangedEvent {
       
    
    private Object source = null;
    private int sortColumn = -1;
    private SortOrder sortType = SortOrder.UNSORTED;

    /**
     * Get the sorted column.
     * UNSORTED means no column is sorted currently
     * @return the sorted column
     */ 
    public int getSortColumn() {
        return sortColumn;
    }

    /**
     * Sort type can be 
     * UP, DOWN or UNSORTED
     * @return the sort type
     */ 
    public SortOrder getSortType() {
        return sortType;
    }
    
    
    /** Creates a new instance of ValueChangedEvent 
     * @param source 
     * @param sortColumn 
     * @param sortType 
     */
    public SortChangedEvent(Object source, int sortColumn, SortOrder sortType) {
        
        this.source = source;
        this.sortColumn = sortColumn;
        this.sortType = sortType;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public Object getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(Object source) {
        this.source = source;
    }
    
     
}
