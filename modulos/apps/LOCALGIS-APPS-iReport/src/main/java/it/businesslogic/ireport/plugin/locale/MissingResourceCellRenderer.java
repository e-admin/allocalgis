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
 * MissingResourceCellRenderer.java
 * 
 * Created on March 27, 2006, 8:39 PM
 *
 */

package it.businesslogic.ireport.plugin.locale;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class MissingResourceCellRenderer extends JLabel implements TableCellRenderer {
    
    /** Creates a new instance of ProgressBarCellRenderer */
    public MissingResourceCellRenderer() {
        super();
        
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        boolean isMissing = false;
        if (value instanceof ResourceKey)
        {
            ResourceKey rk = (ResourceKey)value;
            isMissing = rk.isMissing();            
        }
        if (!isSelected) {
            setForeground(((isMissing) ? Color.RED : table.getSelectionForeground()));
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(((isMissing) ? Color.RED : table.getForeground()));
            setBackground(table.getBackground());
        }
        
        if (value == null) setText("");
        else setText(""+value);
        this.updateUI();
        return this;
    }
    
}
