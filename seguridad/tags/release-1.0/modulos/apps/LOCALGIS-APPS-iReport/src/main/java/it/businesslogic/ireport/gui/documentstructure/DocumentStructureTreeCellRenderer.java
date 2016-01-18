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
 * DocumentStructureTreeCellRenderer.java
 * 
 * Created on 1 giugno 2003, 16.04
 *
 */

package it.businesslogic.ireport.gui.documentstructure;
import it.businesslogic.ireport.*;
import it.businesslogic.ireport.crosstab.CrosstabCell;
import it.businesslogic.ireport.crosstab.CrosstabGroup;
import it.businesslogic.ireport.crosstab.CrosstabParameter;
import it.businesslogic.ireport.crosstab.Measure;
import it.businesslogic.ireport.gui.library.CustomExpression;
import  javax.swing.tree.*;
import  javax.swing.*;
import  java.awt.*;

/**
 *
 * @author  Administrator
 */
public class DocumentStructureTreeCellRenderer extends DefaultTreeCellRenderer {

    static ImageIcon docIcon;
    static ImageIcon bandIcon;
    
    static ImageIcon groupHeaderIcon;
    static ImageIcon groupFooterIcon;
    
    static ImageIcon textfieldIcon;
    static ImageIcon statictextIcon;
    static ImageIcon imageIcon;
    static ImageIcon ellipseIcon;
    static ImageIcon rectangleIcon;
    static ImageIcon subreportIcon;
    static ImageIcon lineIcon;
    static ImageIcon chartIcon;
    static ImageIcon elementGroupIcon;
    static ImageIcon frameIcon;
    static ImageIcon crosstabIcon;
    static ImageIcon cellIcon;
    static ImageIcon crosstabrootIcon;
    static ImageIcon breakIcon;
    
    static ImageIcon fieldsIcon;
    static ImageIcon variablesIcon;
    static ImageIcon parametersIcon;
    static ImageIcon builtinParametersIcon;
    static ImageIcon builtinVariablesIcon;
    static ImageIcon folderFieldsIcon;
    static ImageIcon folderVariablesIcon;
    static ImageIcon folderParametersIcon;
    static ImageIcon datasetIcon;
    
    
    static ImageIcon objectIcon;
    static ImageIcon typeIcon;
    
    static ImageIcon folderClosedIcon;
    static ImageIcon folderOpenedIcon;
    
    static ImageIcon expressionIcon;

    public DocumentStructureTreeCellRenderer() {
        super();
        if (docIcon == null) docIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/doc.gif"));
        if (bandIcon == null) bandIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/band.png"));
        if (textfieldIcon == null) textfieldIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/textfield.gif"));
        if (statictextIcon == null) statictextIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/static.gif"));
        if (imageIcon == null) imageIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/image.gif"));
        if (ellipseIcon == null) ellipseIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/ellipse.png"));
        if (rectangleIcon == null) rectangleIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/rectangle.gif"));
        if (subreportIcon == null) subreportIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/subreport.gif"));
        if (lineIcon == null) lineIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/line.gif"));
        if (chartIcon == null) chartIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/chart.gif"));
        if (elementGroupIcon == null) elementGroupIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/elementgroup.png"));
        if (frameIcon == null) frameIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/frame.png"));
        if (crosstabIcon == null) crosstabIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/crosstab.png"));
        if (cellIcon == null) cellIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/cell.png"));
        if (crosstabrootIcon == null) crosstabrootIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/crosstabroot.png"));
        if (breakIcon == null) breakIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/break.png"));
        
        if (folderFieldsIcon == null) folderFieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/fieldsfolder.png"));
        if (folderVariablesIcon == null) folderVariablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variablesfolder.png"));
        if (folderParametersIcon == null) folderParametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parametersfolder.png"));
        
        if (fieldsIcon == null) fieldsIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/field.png"));
        if (variablesIcon == null) variablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/variable.png"));
        if (parametersIcon == null) parametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/parameter.png"));
        if (datasetIcon == null) datasetIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/library/datasource.png"));
        
        if (builtinParametersIcon == null) builtinParametersIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/builtin_parameter.png"));
        if (builtinVariablesIcon == null) builtinVariablesIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/builtin_variable.png"));
        
        if (groupHeaderIcon == null) groupHeaderIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/group_header.png"));
        if (groupFooterIcon == null) groupFooterIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/editor/group_footer.png"));
        
        
        if (objectIcon == null) objectIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/javabean/object.png"));
        if (typeIcon == null) typeIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/javabean/type.png"));
       if (folderClosedIcon == null) 
            folderClosedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderClosed.gif"));
        if (folderOpenedIcon == null) 
            folderOpenedIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/folderOpened.gif"));
        
        if (expressionIcon == null) 
            expressionIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/elements/exp.png"));
        
        
        
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
            
            //if (value != null && value instanceof ReportObjectTreeNode)
            //{
            //    setText(getText() + " (" + ((ReportObjectTreeNode)value).getNodeId() + ")");
            //}

        return this;
    }

    protected ImageIcon getElementIcon(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;
        this.setForeground( Color.BLACK);
        
        if (node.getUserObject() instanceof ReportElement &&
            !((ReportElement)node.getUserObject()).insideBandReal())
            this.setForeground( Color.RED);
        
        if ((node.getUserObject()+"").equals("Crosstab")) return crosstabrootIcon;
        if (node.getUserObject() instanceof Band)
        {
            if (  ((Band)node.getUserObject()).getHeight() == 0 )
            {
                this.setForeground( Color.GRAY );
            }
            
            if (((Band)node.getUserObject()).isGroupHeader())
            {
                return groupHeaderIcon;
            }
            
            if (((Band)node.getUserObject()).isGroupFooter())
            {
                return groupFooterIcon;
            }
            
            return bandIcon;
        }
        if (node.getUserObject() instanceof CrosstabCell) return cellIcon;
        if (node.getUserObject() instanceof TextFieldReportElement ) return textfieldIcon;
        if (node.getUserObject() instanceof StaticTextReportElement ) return statictextIcon;
        if (node.getUserObject() instanceof ChartReportElement) return chartIcon;
        if (node.getUserObject() instanceof ChartReportElement2) return chartIcon;
        if (node.getUserObject() instanceof ImageReportElement) return imageIcon;
        if (node.getUserObject() instanceof EllipseReportElement) return ellipseIcon;
        if (node.getUserObject() instanceof RectangleReportElement) return rectangleIcon;
        if (node.getUserObject() instanceof SubReportElement) return subreportIcon;
        if (node.getUserObject() instanceof LineReportElement) return lineIcon;
        if (node.getUserObject() instanceof ElementGroup) return elementGroupIcon;
        if (node.getUserObject() instanceof FrameReportElement) return frameIcon;
        if (node.getUserObject() instanceof CrosstabReportElement) return crosstabIcon;
        if (node.getUserObject() instanceof BreakReportElement) return breakIcon;
        
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRField || node.getUserObject() instanceof CrosstabGroup))  return fieldsIcon;
          
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRParameter || node.getUserObject() instanceof CrosstabParameter))
        {
            if (node.getUserObject() instanceof JRParameter)
            {
                if ( ((JRParameter)node.getUserObject()).isBuiltin()) return builtinParametersIcon;
            }
            return parametersIcon;
        }
        
        if (node.getUserObject() != null && (node.getUserObject() instanceof JRVariable || node.getUserObject() instanceof Measure))
        {
            if (node.getUserObject() instanceof JRVariable)
            {
                if ( ((JRVariable)node.getUserObject()).isBuiltin()) return builtinVariablesIcon;
            }
            return variablesIcon;
        }
       
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.fields","Fields"))) return folderFieldsIcon;
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.variables","Variables"))) return folderVariablesIcon;
        if (node.getUserObject().toString().equals(it.businesslogic.ireport.util.I18n.getString("gui.library.parameters","Parameters"))) return folderParametersIcon;
        if (node.getUserObject() != null && node.getUserObject() instanceof SubDataset)  return datasetIcon;
        
        if (node.getUserObject() != null && node.getUserObject() instanceof CustomExpression)  return expressionIcon;
        
        return docIcon;
    }
}

