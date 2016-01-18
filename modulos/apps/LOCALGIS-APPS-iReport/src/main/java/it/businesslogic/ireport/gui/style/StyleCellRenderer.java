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
 * StyleCellRenderer.java
 * 
 * Created on March 22, 2006, 4:44 PM
 *
 */

package it.businesslogic.ireport.gui.style;

import it.businesslogic.ireport.JasperTemplate;
import it.businesslogic.ireport.Style;
import it.businesslogic.ireport.Template;
import it.businesslogic.ireport.UndefinedStyle;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class StyleCellRenderer extends DefaultTreeCellRenderer {
    
    static ImageIcon styleIcon;
    static ImageIcon undefinedStyleIcon;
    static ImageIcon templateIcon;
    static ImageIcon linkTemplateIcon;
      
    /** Creates a new instance of StyleCellRenderer */
    public StyleCellRenderer() {
        super();
         if (styleIcon == null) styleIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/style.png"));
         if (undefinedStyleIcon == null) undefinedStyleIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/undefinedStyle.png"));
         if (templateIcon == null) templateIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/template.png"));
         if (linkTemplateIcon == null) linkTemplateIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/styles/linktemplate.png"));
        
    }
    
    public Component getTreeCellRendererComponent(
         JTree tree,
         Object node,
         boolean isSelected,
         boolean isExpanded,
         boolean isLeaf,
         int row,
         boolean cellHasFocus)
     {
         JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, node, isSelected, isExpanded, isLeaf, row, cellHasFocus);
         //c.setOpaque(false);
         
         ImageIcon icon = getElementIcon(node);
         if (icon != null) c.setIcon(icon);
         return c;
     }
   
    
    protected ImageIcon getElementIcon(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        
        this.setForeground( Color.BLACK);
        
        if (node.getUserObject() instanceof UndefinedStyle)
            this.setForeground( Color.GRAY );
        
        if (node.getUserObject() instanceof UndefinedStyle) return undefinedStyleIcon;
        if (node.getUserObject() instanceof Style ) return styleIcon;
        if (node.getUserObject() instanceof JasperTemplate )
        {
            if ( ((JasperTemplate)node.getUserObject()).getParent() == null)
                return templateIcon;
            else
                return linkTemplateIcon;
        }
        if (node.getUserObject() instanceof Template ) return linkTemplateIcon;
        return undefinedStyleIcon;
    }
    
    public Color getBackgroundNonSelectionColor() {
          return null;
    }
  
    public Color getBackground() {
              return null;
    }

}
