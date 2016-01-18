/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ProblemIconTableCellRenderer.java
 *
 * Created on February 27, 2007, 1:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.logpane;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author gtoffoli
 */
public class ProblemIconTableCellRenderer extends DefaultTableCellRenderer {
    
    static ImageIcon errorIcon;
    static ImageIcon infoIcon;
    static ImageIcon warningIcon;
    
    public ProblemIconTableCellRenderer()
    {
        super();
        if (errorIcon == null) errorIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/problems/error.png"));
        if (infoIcon == null) infoIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/problems/information.png"));
        if (warningIcon == null) warningIcon = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/problems/warning.png"));
    } 

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        java.awt.Component retValue;
        
        retValue = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null && value instanceof ProblemItem)
        {
            ProblemItem pi = (ProblemItem)value;
            ((JLabel)retValue).setText("");
            if (pi.getProblemType() == pi.ERROR) ((JLabel)retValue).setIcon( errorIcon );
            if (pi.getProblemType() == pi.INFORMATION) ((JLabel)retValue).setIcon( infoIcon );
            if (pi.getProblemType() == pi.WARNING) ((JLabel)retValue).setIcon( warningIcon );
            
        }
        
        return retValue;
    }
}
