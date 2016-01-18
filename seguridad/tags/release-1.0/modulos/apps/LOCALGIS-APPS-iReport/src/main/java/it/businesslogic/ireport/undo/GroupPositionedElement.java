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
 * GroupPositionedElement.java
 * 
 * Created on 5 luglio 2003, 13.17
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.*;
/**
 *
 * @author  Administrator
 */
public class GroupPositionedElement {
    
    private ReportElement element;    
    
    private int oldPosition;
    
    private int newPosition;
    
    private String oldElementGroup = "";
    private String newElementGroup = "";
    
    public GroupPositionedElement(ReportElement element, int oldPosition, int newPosition, String oldElementGroup, String newElementGroup)
    {
        this.element = element;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.oldElementGroup = oldElementGroup;
        this.newElementGroup = newElementGroup;
    }   
    
    /** Getter for property element.
     * @return Value of property element.
     *
     */
    public it.businesslogic.ireport.ReportElement getElement() {
        return element;
    }    
    
    /** Setter for property element.
     * @param element New value of property element.
     *
     */
    public void setElement(it.businesslogic.ireport.ReportElement element) {
        this.element = element;
    }
    
    /** Getter for property newPosition.
     * @return Value of property newPosition.
     *
     */
    public int getNewPosition() {
        return newPosition;
    }
    
    /** Setter for property newPosition.
     * @param newPosition New value of property newPosition.
     *
     */
    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
    }
    
    /** Getter for property oldPosition.
     * @return Value of property oldPosition.
     *
     */
    public int getOldPosition() {
        return oldPosition;
    }
    
    /** Setter for property oldPosition.
     * @param oldPosition New value of property oldPosition.
     *
     */
    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getOldElementGroup() {
        return oldElementGroup;
    }

    public void setOldElementGroup(String oldElementGroup) {
        this.oldElementGroup = oldElementGroup;
    }

    public String getNewElementGroup() {
        return newElementGroup;
    }

    public void setNewElementGroup(String newElementGroup) {
        this.newElementGroup = newElementGroup;
    }
    
}
