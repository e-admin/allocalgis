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
 * MeterIntervalTableCellRenderer.java
 * 
 * Created on September 29, 2006, 11:05 AM
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.chart.MeterInterval;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class MeterIntervalTableCellRenderer extends DefaultTableCellRenderer {
    
    /** Creates a new instance of TableCellRenderer */
    public MeterIntervalTableCellRenderer() {
        super();
    }
    
    public Component getTableCellRendererComponent(JTable table, 
                                            Object value,
                                            boolean isSelected,
                                            boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
        
        if (value instanceof MeterInterval && value != null)
        {
            setText( ((MeterInterval)value).getLabel() );

            setIcon( new ColorIcon( ( (MeterInterval)value).getColor() ));
            
        }
        
        return this;
    }
}
