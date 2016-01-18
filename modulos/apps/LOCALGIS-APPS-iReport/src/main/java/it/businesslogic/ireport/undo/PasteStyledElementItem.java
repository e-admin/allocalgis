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
 * PasteStyledElementItem.java
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
public class PasteStyledElementItem {
    
   private ReportElement originalStyle = null;
   private ReportElement newStyle = null;
   private ReportElement element = null;
    
   public PasteStyledElementItem(ReportElement element, ReportElement originalStyle, ReportElement newStyle) {
       this.newStyle = newStyle;
       this.originalStyle = originalStyle;
       this.element = element;
   }   
   
   /**
    * Getter for property element.
    * @return Value of property element.
    */
   public it.businesslogic.ireport.ReportElement getElement() {
       return element;
   }   
 
   /**
    * Setter for property element.
    * @param element New value of property element.
    */
   public void setElement(it.businesslogic.ireport.ReportElement element) {
       this.element = element;
   }
   
   /**
    * Getter for property newStyle.
    * @return Value of property newStyle.
    */
   public it.businesslogic.ireport.ReportElement getNewStyle() {
       return newStyle;
   }
   
   /**
    * Setter for property newStyle.
    * @param newStyle New value of property newStyle.
    */
   public void setNewStyle(it.businesslogic.ireport.ReportElement newStyle) {
       this.newStyle = newStyle;
   }
   
   /**
    * Getter for property originalStyle.
    * @return Value of property originalStyle.
    */
   public it.businesslogic.ireport.ReportElement getOriginalStyle() {
       return originalStyle;
   }
   
   /**
    * Setter for property originalStyle.
    * @param originalStyle New value of property originalStyle.
    */
   public void setOriginalStyle(it.businesslogic.ireport.ReportElement originalStyle) {
       this.originalStyle = originalStyle;
   }
   
}
