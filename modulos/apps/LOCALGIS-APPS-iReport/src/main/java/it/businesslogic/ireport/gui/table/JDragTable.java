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
 * JDragTable.java
 * 
 * Created on 14 novembre 2003, 0.43
 *
 */

package it.businesslogic.ireport.gui.table;
import it.businesslogic.ireport.gui.dnd.TransferableObject;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 * @author  Administrator
 */
public class JDragTable extends org.jdesktop.swingx.JXTable implements DragGestureListener,
                DragSourceListener {    
   
   public JDragTable() {
        
       super();
       
        DragSource dragSource = DragSource.getDefaultDragSource();

        // creating the recognizer is all that's necessary - it
        // does not need to be manipulated after creation
        dragSource.createDefaultDragGestureRecognizer(
         this, // component where drag originates
         DnDConstants.ACTION_COPY, // actions
         this); // drag gesture listener

        setColumnControlVisible(true); 
        
      }
      
       public void dragGestureRecognized(DragGestureEvent e) {
         // drag anything ...
         
         TransferableObject  to =   new TransferableObject(this.getValueAt(this.getSelectedRow(), this.getSelectedColumn() ));
         
         try{
         if (to != null)
         e.startDrag(DragSource.DefaultCopyDrop , // cursor
            to); //, // transferable
            //this); // drag source listener
         } catch (Exception ex) {

            ex.printStackTrace();

         }

      }

      public void dragDropEnd(DragSourceDropEvent e) {}
      public void dragEnter(DragSourceDragEvent e) {}
      public void dragExit(DragSourceEvent e) {}
      public void dragOver(DragSourceDragEvent e) {}
      public void dropActionChanged(DragSourceDragEvent e) {}

      
    public void toggleSortOrder(int columnIndex) {
        super.toggleSortOrder(columnIndex);
        
        int index = convertColumnIndexToModel(columnIndex);
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }

        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so) );
    }

    public void toggleSortOrder(Object identifier) {
        super.toggleSortOrder(identifier);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }

    public void setSortOrder(Object identifier, SortOrder arg1) {
        super.setSortOrder(identifier, arg1);
        
        TableColumnExt columnExt = getColumnExt(identifier);
        int index = columnExt.getModelIndex();
        
        SortOrder so = SortOrder.UNSORTED;
        SortController sortController = getSortController();
        if (sortController != null)
        {
            so = sortController.getSortOrder(index);
        }
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, index, so ) );
    }
    
    
    public void resetSortOrder() {
        super.resetSortOrder();
        
        fireSortChangedListenerSortChanged(new SortChangedEvent(this, -1, SortOrder.UNSORTED ) );
    }

      
      /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    /**
     * Registers TabPaneChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSortChangedListener(SortChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (SortChangedListener.class, listener);
    }

    /**
     * Removes TabPaneChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSortChangedListener(SortChangedListener listener) {

        listenerList.remove (SortChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireSortChangedListenerSortChanged(SortChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==SortChangedListener.class) {
                ((SortChangedListener)listeners[i+1]).sortChanged (event);
            }
        }
    }
}
