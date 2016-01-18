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
 * ValueChangedEvent.java
 * 
 * Created on 10 febbraio 2003, 2.04
 *
 */

package it.businesslogic.ireport.gui.event;

import it.businesslogic.ireport.gui.*;
import javax.swing.*;
/**
 *
 * @author  Administrator
 */
public class ValueChangedEvent {
    
    private JComponent source;    
    
    private double oldValue;    
    private double newValue;  
    /** Creates a new instance of ValueChangedEvent */
    public ValueChangedEvent(JComponent source, double oldValue, double newValue) {
        
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public JComponent getSource() {
        return source;
    }
    
    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(JComponent source) {
        this.source = source;
    }
    
    /** Getter for property newValue.
     * @return Value of property newValue.
     *
     */
    public double getNewValue() {
        return newValue;
    }
    
    /** Setter for property newValue.
     * @param newValue New value of property newValue.
     *
     */
    public void setNewValue(double newValue) {
        this.newValue = newValue;
    }
    
    /** Getter for property oldValue.
     * @return Value of property oldValue.
     *
     */
    public double getOldValue() {
        return oldValue;
    }
    
    /** Setter for property oldValue.
     * @param oldValue New value of property oldValue.
     *
     */
    public void setOldValue(double oldValue) {
        this.oldValue = oldValue;
    }
    
}
