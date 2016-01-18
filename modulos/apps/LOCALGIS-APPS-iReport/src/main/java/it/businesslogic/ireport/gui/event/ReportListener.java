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
 * ReportListener.java
 * 
 * Created on 17 giugno 2003, 1.03
 *
 */

package it.businesslogic.ireport.gui.event;

/**
 * This listener handle all report events regarding selection and changes.
 * A single event (excluse reportElementsSelectionChanged) can mark 
 * a document as "dirty".
 *
 * @author  Giulio Toffoli
 */
public interface ReportListener extends java.util.EventListener  {
    
    /**
      *     This method is called when a new element is selected,
      *     or deselected.
      */
     public void reportElementsSelectionChanged(ReportElementsSelectionEvent evt);
   
     /*
      *     This method is called when an element is removed, changed or added.
      */
      public void reportElementsChanged(ReportElementChangedEvent evt);
      
     /*
      *     This method is called when a band is removed, changed or added.
      */ 
     public void reportBandChanged(ReportBandChangedEvent evt );
     
      /**
      *     This method is called when a new element is selected,
      *     or deselected.
      */
     public void reportBandsSelectionChanged(ReportBandsSelectionEvent evt);
     
     /**
      *     This method is called when a new object is selected,
      *     or deselected.
      */
     public void reportObjectsSelectionChanged(ReportObjectsSelectionEvent evt);
}
