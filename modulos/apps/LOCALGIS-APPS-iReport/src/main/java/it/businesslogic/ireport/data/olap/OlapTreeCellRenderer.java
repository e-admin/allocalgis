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
 * OlapTreeCellRenderer.java
 * 
 * Created on 1 giugno 2003, 16.04
 *
 */

package it.businesslogic.ireport.data.olap;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import mondrian.olap.QueryAxis;
/**
 *
 * @author  Administrator
 */
public class OlapTreeCellRenderer extends DefaultTreeCellRenderer {

    
    static ImageIcon measureIcon;
    static ImageIcon dimensionIcon;
    static ImageIcon hierarchyIcon;
    
    public OlapTreeCellRenderer() {
        super();

        if (measureIcon == null) measureIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/olap/measure.png"));
        if (dimensionIcon == null) dimensionIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/olap/axis.png"));
        if (hierarchyIcon == null) hierarchyIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/olap/hierarchy.png"));
        
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
            this.setForeground( Color.BLACK);
            ImageIcon icon = getElementIcon(value);
            setIcon(icon);

            setToolTipText(null);

        return this;
    }

    protected ImageIcon getElementIcon(Object value) {
        
        if (!(value instanceof DefaultMutableTreeNode)) return null; // ?!!!
        
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;

        try {
            WalkableWrapper ww = (WalkableWrapper)node.getUserObject();

            if (node.getParent() != null && node.getParent() instanceof DefaultMutableTreeNode) 
            {
                if (ww.isMeasure()) return measureIcon;
                if (ww.getWalkable() instanceof QueryAxis) return dimensionIcon;
                return hierarchyIcon;
            }
            
        } catch (Exception ex)
        {
            
        }
        return null;
    }
}

