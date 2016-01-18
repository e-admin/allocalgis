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
 * SubDatasetObjectChangedEvent.java
 * 
 * Created on 10 febbraio 2003, 2.04
 *
 */

package it.businesslogic.ireport.gui.event;

import it.businesslogic.ireport.*;

/**
 *
 * @author  Administrator
 */
public class SubDatasetObjectChangedEvent {
       
    public static final int FIELD = 0;
    public static final int VARIABLE = 1;
    public static final int PARAMETER = 2;
    public static final int QUERY = 3;
    public static final int QUERY_LANGUAGE = 4;
    public static final int SORTFIELD = 5;

    public static final int ADDED = 0;
    public static final int MODIFIED = 1;
    public static final int DELETED = 2;
    public static final int ORDER_CHANGED = 3;
    
    private Object oldValue;    
    private Object newValue;  
    private int type = 0;
    private int action = 0;
    private SubDataset source = null;
    
    /** Creates a new instance of ValueChangedEvent */
    public SubDatasetObjectChangedEvent(SubDataset source, int objectType, int action ,Object oldValue, Object newValue) {
        
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.action = action;
        this.type = objectType;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public SubDataset getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(Report source) {
        this.source = source;
    }
    
    /** Getter for property newValue.
     * @return Value of property newValue.
     *
     */
    public Object getNewValue() {
        return newValue;
    }
    
    /** Setter for property newValue.
     * @param newValue New value of property newValue.
     *
     */
    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
    
    /** Getter for property oldValue.
     * @return Value of property oldValue.
     *
     */
    public Object getOldValue() {
        return oldValue;
    }
    
    /** Setter for property oldValue.
     * @param oldValue New value of property oldValue.
     *
     */
    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
    
}
