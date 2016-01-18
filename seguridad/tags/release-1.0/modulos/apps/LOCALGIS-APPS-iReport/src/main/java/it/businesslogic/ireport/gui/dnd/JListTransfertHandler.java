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
 * JListTransfertHandler.java
 * 
 * Created on 15 settembre 2004, 2.19
 *
 */

package it.businesslogic.ireport.gui.dnd;
import it.businesslogic.ireport.gui.MainFrame;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author  Administrator
 */
public class JListTransfertHandler extends javax.swing.TransferHandler
{

    public int getSourceActions(JComponent c) 
    {    
        return COPY;       
    }    
    
    protected Transferable createTransferable(JComponent c) 
    {

        if (c instanceof JList)
        {
            JList list = (JList)c;

            return new it.businesslogic.ireport.gui.dnd.TransferableObject(list.getSelectedValue()); 
        }
        if (c instanceof JTree)
        {
            Object obj = null;
            JTree tree = (JTree)c;

            if (tree.getSelectionPath() != null &&
                tree.getSelectionPath().getLastPathComponent() instanceof DefaultMutableTreeNode)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
                obj = selectedNode.getUserObject();
            }
            
            if (obj != null)
            return new it.businesslogic.ireport.gui.dnd.TransferableObject(obj); 
        }
        
        return new it.businesslogic.ireport.gui.dnd.TransferableObject(c);
    }


}
