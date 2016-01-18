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
 * InternalFrameActivatedEvent.java
 * 
 * Created on 7 febbraio 2003, 23.11
 *
 */

package it.businesslogic.ireport.gui.event;
import java.util.*;
/**
 *
 * @author  Administrator
 */
public class InternalFrameActivatedEvent {
    
    public static final int ACTIVATED = 1;
    public static final int CLOSED = -1;
    
    private it.businesslogic.ireport.gui.JMDIFrame jMDIFrame = null;
    private int action = 1;
    
    
    /** Creates a new instance of InternalFrameActivatedEvent */
    public InternalFrameActivatedEvent( it.businesslogic.ireport.gui.JMDIFrame jMDIFrame) {
        this(jMDIFrame,ACTIVATED);
    }
    
    public InternalFrameActivatedEvent( it.businesslogic.ireport.gui.JMDIFrame jMDIFrame, int action) {
        this.action = action;
        this.jMDIFrame = jMDIFrame;
    }
    
    /** Getter for property jMDIFrame.
     * @return Value of property jMDIFrame.
     *
     */
    public it.businesslogic.ireport.gui.JMDIFrame getJMDIFrame() {
        return jMDIFrame;
    }
    
    /** Setter for property jMDIFrame.
     * @param jMDIFrame New value of property jMDIFrame.
     *
     */
    public void setJMDIFrame(it.businesslogic.ireport.gui.JMDIFrame jMDIFrame) {
        this.jMDIFrame = jMDIFrame;
    }
    
    /** Getter for property action.
     * @return Value of property action.
     *
     */
    public int getAction() {
        return action;
    }
    
    /** Setter for property action.
     * @param action New value of property action.
     *
     */
    public void setAction(int action) {
        this.action = action;
    }
    
}
