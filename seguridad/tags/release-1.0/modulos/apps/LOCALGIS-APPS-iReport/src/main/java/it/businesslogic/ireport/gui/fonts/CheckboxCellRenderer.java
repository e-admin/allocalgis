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
 * CheckboxCellRenderer.java
 * 
 * Created on October 5, 2006, 10:03 AM
 *
 */

package it.businesslogic.ireport.gui.fonts;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.*;

/**
 *
 * @author gtoffoli
 */
public class CheckboxCellRenderer extends DefaultListCellRenderer
{
      protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
            
      public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         if (value instanceof CheckBoxListEntry)
         {
             CheckBoxListEntry checkbox = (CheckBoxListEntry) value;
             checkbox.setBackground(isSelected ?
                     list.getSelectionBackground() : list.getBackground());
             if (checkbox.isRed())
             {
                 checkbox.setForeground( Color.red );
             }
             else
             {
             checkbox.setForeground(isSelected ?
                     list.getSelectionForeground() : list.getForeground());
             }
             checkbox.setEnabled(isEnabled());
             checkbox.setFont(getFont());
             checkbox.setFocusPainted(false);
             checkbox.setBorderPainted(true);
             checkbox.setBorder(isSelected ?
              UIManager.getBorder(
               "List.focusCellHighlightBorder") : noFocusBorder);
             
             return checkbox;
         }
         else
         {
            return super.getListCellRendererComponent(list, value.getClass().getName(), index, isSelected, cellHasFocus);
         }
   }
}
