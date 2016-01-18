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
 * DocumentExpressionEditorTreeCellRenderer.java
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
public class DocumentExpressionEditorTreeCellRenderer extends DefaultTreeCellRenderer {

    //static ImageIcon fieldIcon;
    //static ImageIcon variableIcon;
    //static ImageIcon parameterIcon;
    static ImageIcon folderFieldsIcon;
    static ImageIcon folderVariablesIcon;
    static ImageIcon folderParametersIcon;
    static ImageIcon customFolderIcon;
    
    static ImageIcon fieldsIcon;
    static ImageIcon variablesIcon;
    static ImageIcon parametersIcon;

    public DocumentExpressionEditorTreeCellRenderer() {
        super();
        //if (fieldIcon == null) fieldIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/field.gif"));
        //if (variableIcon == null) variableIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variable.gif"));
        //if (parameterIcon == null) parameterIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parameter.gif"));
        if (folderFieldsIcon == null) folderFieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/fieldsfolder.png"));
        if (folderVariablesIcon == null) folderVariablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variablesfolder.png"));
        if (folderParametersIcon == null) folderParametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parametersfolder.png"));
        
        if (fieldsIcon == null) fieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/field.png"));
        if (variablesIcon == null) variablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variable.png"));
        if (parametersIcon == null) parametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parameter.png"));
        
        if (customFolderIcon == null) customFolderIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/customfolder.gif"));

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
            this.setForeground( Color.BLACK);
            ImageIcon icon = getElementIcon(value);
            setIcon(icon);

            setToolTipText(null);

        return this;
    }

    protected ImageIcon getElementIcon(Object value) {
        
        if (!(value instanceof DefaultMutableTreeNode)) return customFolderIcon;
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        this.setForeground( Color.BLACK);
               
        if (node.getUserObject() instanceof IconedString)
        {
            IconedString iconedString = (IconedString)node.getUserObject();
            setText(  iconedString.getStr() );
            return iconedString.getIcon();
        }
        
        if (node.getUserObject().toString().equalsIgnoreCase("Variables")) return folderVariablesIcon;
        if (node.getUserObject().toString().equalsIgnoreCase("Fields")) return folderFieldsIcon;
        if (node.getUserObject().toString().equalsIgnoreCase("Parameters")) return folderParametersIcon;
        
        if (node.getParent() != null && node.getParent() instanceof DefaultMutableTreeNode) 
        {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if (parentNode.getUserObject().toString().equalsIgnoreCase("Variables")) return variablesIcon;
            if (parentNode.getUserObject().toString().equalsIgnoreCase("Fields")) return fieldsIcon;
            if (parentNode.getUserObject().toString().equalsIgnoreCase("Parameters")) return parametersIcon;
        }
        return customFolderIcon;
    }
}

