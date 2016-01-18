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
 * ProgressBarCellRenderer.java
 * 
 * Created on March 27, 2006, 8:39 PM
 *
 */

package it.businesslogic.ireport.plugin.locale;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class ProgressBarCellRenderer extends JProgressBar implements TableCellRenderer {
    
    /** Creates a new instance of ProgressBarCellRenderer */
    public ProgressBarCellRenderer() {
        super(0,100);
        this.setStringPainted(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        double val = 0;
        try {
            val = Double.parseDouble(""+value);
        } catch (Exception ex)
        {
            
        }
        this.setString(""+(int)val+"%");
        this.setValue((int)val);
        return this;
    }
    
}
