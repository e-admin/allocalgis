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
 * DTListener.java
 * 
 * Created on June 1, 2006, 11:32 AM
 *
 */

package it.businesslogic.ireport.gui;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
/**
 *
 * @author gtoffoli
 */
  /**
   * DTListener
   * a listener that tracks the state of the operation
   * @see java.awt.dnd.DropTargetListener
   * @see java.awt.dnd.DropTarget
   */
 public class DTListener implements DropTargetListener {


    /**
     * start "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragEnter(DropTargetDragEvent e) {
      e.acceptDrag(e.getDropAction());        
    }

    /**
     * continue "drag under" feedback on component
     * invoke acceptDrag or rejectDrag based on isDragOk
     */
    public void dragOver(DropTargetDragEvent e) {
      e.acceptDrag(e.getDropAction());    
    }
    
    public void dropActionChanged(DropTargetDragEvent e) {
    }
    
    public void dragExit(DropTargetEvent e) {
    }

    /**
     * perform action from getSourceActions on
     * the transferrable
     * invoke acceptDrop or rejectDrop
     * invoke dropComplete
     * if its a local (same JVM) transfer, use StringTransferable.localStringFlavor
     * find a match for the flavor
     * check the operation
     * get the transferable according to the chosen flavor
     * do the transfer
     */
    public void drop(DropTargetDropEvent dtde) {
        try {
           
            DropTargetContext context = dtde.getDropTargetContext();

            Transferable tr = dtde.getTransferable();
            if (tr.isDataFlavorSupported (DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop (
                        DnDConstants.ACTION_COPY_OR_MOVE);
                java.util.List fileList = (java.util.List)tr.getTransferData(DataFlavor.javaFileListFlavor);

                MainFrame.getMainInstance().openFiles(fileList);
            }
           
            context.dropComplete(true);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
  }

