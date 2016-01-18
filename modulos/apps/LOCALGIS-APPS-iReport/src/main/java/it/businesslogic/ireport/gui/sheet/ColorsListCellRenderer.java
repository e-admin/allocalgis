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
 * ColorsListCellRenderer.java
 * 
 * Created on September 27, 2006, 9:13 AM
 *
 */

package it.businesslogic.ireport.gui.sheet;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
/**
 *
 * @author gtoffoli
 */

/**
 *
 * @author gtoffoli
 */
public class ColorsListCellRenderer extends DefaultListCellRenderer  {
           
    /** Creates a new instance of ColorsListCellRenderer */
    public ColorsListCellRenderer() {
        super();
    }
    
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof java.awt.Color)
        {
            java.awt.Color color = (java.awt.Color)value;
            
            label.setText( HexColorChooserPanel.getEncodedColor(color) );
            //"[" + color.getRed() + "," + color.getGreen() + "," +  color.getBlue()+ "]" 
            label.setIcon(new ColorIcon(color));
        }
        else
        {
            label.setIcon(new ColorIcon(java.awt.Color.WHITE));
        }
        
        //setBackground(isSelected ? selectionBackground : background);
        return label;
    }
   
}

