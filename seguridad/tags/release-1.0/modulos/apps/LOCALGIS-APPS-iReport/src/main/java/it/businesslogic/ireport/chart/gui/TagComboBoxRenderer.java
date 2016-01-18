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
 * TagComboBoxRenderer.java
 * 
 * Created on 2 ottobre 2004, 16.59
 *
 */

package it.businesslogic.ireport.chart.gui;
import it.businesslogic.ireport.gui.sheet.Tag;
import it.businesslogic.ireport.util.Misc;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author  Administrator
 */
public class TagComboBoxRenderer extends JComboBox implements TableCellRenderer {
        
         /*
          * Create a TagComboBoxRenderer with a set of Tag elements
          */
         public TagComboBoxRenderer(java.util.Vector tags) {
            super(tags);
            setEditable(true);
            setMinimumSize(new java.awt.Dimension(1,23));
            setPreferredSize(new java.awt.Dimension(1,23));
        
         }
    
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
    
            // Select the current value
            if (value instanceof Tag)
            {
                setSelectedItem(value);
            }
            else
            {
                Misc.setComboboxSelectedTagValue( this,  "" + value);
            }
            return this;
        }
    }

