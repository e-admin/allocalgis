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
 * ReplacedElementItem.java
 * 
 * Created on 18 giugno 2004, 14.30
 *
 */

package it.businesslogic.ireport.undo;
import it.businesslogic.ireport.ReportElement;
/**
 *
 * @author  Administrator
 */
public class ReplacedElementItem {
    
   private ReportElement oldElement = null;
   private ReportElement newElement = null;
    
   public ReplacedElementItem(ReportElement oldElement, ReportElement newElement) {
       this.newElement = newElement;
       this.oldElement = oldElement;
   }   
   
   /**
    * Getter for property newElement.
    * @return Value of property newElement.
    */
   public it.businesslogic.ireport.ReportElement getNewElement() {
       return newElement;
   }
   
   /**
    * Setter for property newElement.
    * @param newElement New value of property newElement.
    */
   public void setNewElement(it.businesslogic.ireport.ReportElement newElement) {
       this.newElement = newElement;
   }
   
   /**
    * Getter for property oldElement.
    * @return Value of property oldElement.
    */
   public it.businesslogic.ireport.ReportElement getOldElement() {
       return oldElement;
   }
   
   /**
    * Setter for property oldElement.
    * @param oldElement New value of property oldElement.
    */
   public void setOldElement(it.businesslogic.ireport.ReportElement oldElement) {
       this.oldElement = oldElement;
   }
   
}
