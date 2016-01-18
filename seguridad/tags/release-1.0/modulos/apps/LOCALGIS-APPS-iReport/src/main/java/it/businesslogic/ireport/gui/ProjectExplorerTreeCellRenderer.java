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
 * ProjectExplorerTreeCellRenderer.java
 * 
 * Created on 1 giugno 2003, 16.04
 *
 */

package it.businesslogic.ireport.gui;
import it.businesslogic.ireport.*;
import  javax.swing.tree.*;
import  javax.swing.*;
import  java.awt.*;
/**
 *
 * @author  Administrator
 */
public class ProjectExplorerTreeCellRenderer extends DefaultTreeCellRenderer {
    static ImageIcon folderClosedIcon;
    static ImageIcon folderOpenedIcon;
    
    static ImageIcon dirtyDocumentIcon;
    static ImageIcon documentIcon;

    public ProjectExplorerTreeCellRenderer() {
        super();
        if (folderClosedIcon == null) 
            folderClosedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderClosed.gif"));
        if (folderOpenedIcon == null) 
            folderOpenedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderOpened.gif"));
        if (dirtyDocumentIcon == null) 
            dirtyDocumentIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/docDirty.gif"));
        if (documentIcon == null) 
            documentIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/doc.gif"));
        
        /*
        this.setOpenIcon(folderOpenedIcon);
        this.setClosedIcon(folderClosedIcon);
        this.setLeafIcon(documentIcon);
         */
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
             
        if (!((DefaultMutableTreeNode)value).isRoot() && leaf && isDirty(value)) {
            setIcon(dirtyDocumentIcon);
            setToolTipText(null);
        } else if (!((DefaultMutableTreeNode)value).isRoot() && leaf && !isDirty(value)) {
            setIcon(documentIcon);
            setToolTipText(null);
        } 
        else if (( ((DefaultMutableTreeNode)value).isRoot() || !leaf) && expanded)
        {
             setIcon(folderOpenedIcon);
            setToolTipText(null); //no tool tip
        }
        else if (( ((DefaultMutableTreeNode)value).isRoot() || !leaf) && !expanded)
        {
            setIcon(folderClosedIcon);
            setToolTipText(null); //no tool tip
        } 

        return this;
    }

    protected boolean isDirty(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        
        if (node.getUserObject() instanceof DocumentTreeEntry)
        {
            DocumentTreeEntry nodeInfo =
                (DocumentTreeEntry)(node.getUserObject());
            if (nodeInfo.getJrf() != null)
                return nodeInfo.getJrf().getReport().isModified();
        }
        return false;
    }
}

