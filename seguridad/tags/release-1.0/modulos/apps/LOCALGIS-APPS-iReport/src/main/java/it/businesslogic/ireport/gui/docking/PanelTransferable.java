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
 * PanelTransferable.java
 * 
 * Created on January 26, 2006, 9:29 AM
 *
 */

package it.businesslogic.ireport.gui.docking;
import java.awt.datatransfer.*;

/**
 *
 * @author gtoffoli
 */
public class PanelTransferable implements Transferable, ClipboardOwner {
    
    Object obj;
    java.awt.datatransfer.DataFlavor thisFlavor;
    /** Creates a new instance of TransferableObject */
    public PanelTransferable(Object obj) {
        this.obj = obj;
        thisFlavor = new java.awt.datatransfer.DataFlavor(obj.getClass(), obj.getClass().getName());
    }
    
    public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException {
        if (flavor.equals( thisFlavor ))
        {
            return obj;
       }
       // GDN new code start
       else
       if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor )) {
            return new String();    // anything non-null
       }
       else
       // GDN new code end
            return null;
    }
    
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        // GDN new code start
        java.awt.datatransfer.DataFlavor stringFlavor = java.awt.datatransfer.DataFlavor.stringFlavor;
        return new java.awt.datatransfer.DataFlavor[] { thisFlavor,
                                                        stringFlavor };
        // GDN new code end
        
        // GDN comment out-->return new java.awt.datatransfer.DataFlavor[]{thisFlavor};
    }
    
    public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
// GDN comment out
//        if (flavor != null && flavor.equals( thisFlavor ))
//        {
//            return true;
//        }
//        
//        return false;
// GDN comment out
        // GDN new code begin
        if (flavor == null)
            return false;
        else
        if (flavor.equals( thisFlavor ))
            return true;
        else
        if (flavor.equals( java.awt.datatransfer.DataFlavor.stringFlavor ))
            return true;
        else
            return false;
        // GDN new code end
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
