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
 * LibraryTreeCellRenderer.java
 * 
 * Created on 1 giugno 2003, 16.04
 *
 */

package it.businesslogic.ireport.gui.library;
import it.businesslogic.ireport.JRField;
import it.businesslogic.ireport.JRParameter;
import it.businesslogic.ireport.JRVariable;
import it.businesslogic.ireport.SubDataset;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.crosstab.GroupTotal;
import it.businesslogic.ireport.crosstab.Measure;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
/**
 *
 * @author  Administrator
 */
public class LibraryTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon objectIcon;
    static ImageIcon typeIcon;
    
    static ImageIcon folderClosedIcon;
    static ImageIcon folderOpenedIcon;
    
    static ImageIcon folderFieldsIcon;
    static ImageIcon folderVariablesIcon;
    static ImageIcon folderParametersIcon;
    
    static ImageIcon fieldsIcon;
    static ImageIcon variablesIcon;
    static ImageIcon parametersIcon;
    static ImageIcon expressionIcon;
    static ImageIcon grouptotalIcon;
    
    static ImageIcon datasetIcon;
    
    public LibraryTreeCellRenderer() {
        super();
        if (objectIcon == null) objectIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/javabean/object.png"));
        if (typeIcon == null) typeIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/javabean/type.png"));
       if (folderClosedIcon == null) 
            folderClosedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderClosed.gif"));
        if (folderOpenedIcon == null) 
            folderOpenedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderOpened.gif"));
        
        if (expressionIcon == null) 
            expressionIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/exp.png"));
        
        
        if (folderFieldsIcon == null) folderFieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/fieldsfolder.gif"));
        if (folderVariablesIcon == null) folderVariablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variablesfolder.gif"));
        if (folderParametersIcon == null) folderParametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parametersfolder.gif"));
       
        if (fieldsIcon == null) fieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/field.png"));
        if (variablesIcon == null) variablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variable.png"));
        if (parametersIcon == null) parametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parameter.png"));
        if (datasetIcon == null) datasetIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/library/datasource.png"));
        if (grouptotalIcon == null) grouptotalIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/grouptotal.png"));
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
            if (icon != null) setIcon(icon);
            else
            {
                if ( expanded)
                {
                     setIcon(folderOpenedIcon);
                     setToolTipText(null); //no tool tip
                }
                else 
                {
                     setIcon(folderClosedIcon);
                     setToolTipText(null); //no tool tip
                } 
            }

            setToolTipText(null);

        return this;
    }

    protected ImageIcon getElementIcon(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        this.setForeground( Color.BLACK);
           
        if (node.getUserObject() != null && node.getUserObject() instanceof AbstractLibraryObject ) 
        {
            return ((AbstractLibraryObject)node.getUserObject()).getIcon();
        }
        
        if (node.getUserObject() != null && (node.getUserObject() instanceof GroupTotal))  return grouptotalIcon;
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRField || node.getUserObject() instanceof CrosstabGroup))  return fieldsIcon;
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRVariable || node.getUserObject() instanceof Measure))  return variablesIcon;
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRParameter || node.getUserObject() instanceof CrosstabParameter))  return parametersIcon;
        if (node.getUserObject() != null && node.getUserObject() instanceof CustomExpression)  return expressionIcon;
        
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields"))) return folderFieldsIcon;
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables"))) return folderVariablesIcon;
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters"))) return folderParametersIcon;
        
        if (node.getUserObject() != null && node.getUserObject() instanceof SubDataset)  return datasetIcon;
        
        return null;
        
       
        //return objectIcon;
    }
    
    
}

