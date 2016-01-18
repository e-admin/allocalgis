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
 * SheetPropertyValueChangedEvent.java
 * 
 * Created on 6 maggio 2005, 1.46
 *
 */

package it.businesslogic.ireport.gui.event;

/**
 *
 * @author  Administrator
 */
public class SheetPropertyValueChangedEvent {
    
    private String propertyName = null;
    private Object oldValue = null;
    private Object newValue = null;
    private Object source = null;
    /** Creates a new instance of SheetPropertyValueChanged */
    public SheetPropertyValueChangedEvent(String propertyName,
                                          Object oldValue,
                                          Object newValue,
                                          Object source) {
                                          
                                                   
         this.setPropertyName(propertyName);
                  this.setNewValue(newValue);
                  this.setOldValue(oldValue);
                  this.setSource(source);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
}
