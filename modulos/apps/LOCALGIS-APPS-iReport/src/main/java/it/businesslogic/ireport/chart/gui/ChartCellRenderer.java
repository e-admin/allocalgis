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
 * ChartCellRenderer.java
 * 
 * Created on 29 settembre 2004, 1.37
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.chart.ChartDefinition;
import it.businesslogic.ireport.chart.ChartDescriptor;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
/**
 *
 * @author  Administrator
 */
public class ChartCellRenderer extends javax.swing.DefaultListCellRenderer {
    
    public static javax.swing.ImageIcon icon;
    /** Creates a new instance of ChartCellRenderer */
    public ChartCellRenderer() {
        super();
        if (icon == null)
        {
            icon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/charts/pie3D.gif"));  
        }
    }
    
    public Component getListCellRendererComponent(
         JList list,
         Object value,
         int index,
         boolean isSelected,
         boolean cellHasFocus)
     {
         JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected, cellHasFocus);
         
         if (value instanceof ChartDefinition)
         {
                  label.setText("");
                  label.setIcon(((ChartDefinition)value).getIcon());
                  
         }
         else if (value instanceof ChartDescriptor)
         {
                  label.setText("");
                  label.setIcon(((ChartDescriptor)value).getIcon());
                  
         }
         return this;
     }
 }

