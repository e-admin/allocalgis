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
 * ImageCellRenderer.java
 * 
 * Created on 20 maggio 2004, 8.49
 *
 */

package it.businesslogic.ireport.plugin.massivecompiler;

/**
 *
 * @author  Administrator
 */
public class ImageCellRenderer implements javax.swing.table.TableCellRenderer {
    
    javax.swing.Icon icon1 = null;
    javax.swing.Icon icon2 = null;
    javax.swing.Icon icon3 = null;
    
    javax.swing.JLabel label;
    /** Creates a new instance of ImageCellRenderer */
    public ImageCellRenderer() {
        label = new javax.swing.JLabel();
        icon1 = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/docDirty.gif") );
        icon2 = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/doc.gif") );
        icon3 = new javax.swing.ImageIcon(getClass().getResource("/it/businesslogic/ireport/icons/tree/warning.gif") );
        
        label.setIcon( icon1 );
        label.setText("");
    }
    
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    
        if (value instanceof FileEntry)
        {
            if ( ((FileEntry)value).getStatus() == FileEntry.STATUS_COMPILED  || ((FileEntry)value).getStatus() == FileEntry.STATUS_COMPILED_GROOVY)
            {
                label.setIcon( icon2 );
            } 
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_ERROR_COMPILING)
            {
                label.setIcon( icon3 );
            } 
            else if (((FileEntry)value).getStatus() == FileEntry.STATUS_NOT_COMPILED || ((FileEntry)value).getStatus() == FileEntry.STATUS_COMPILING)
            {
                label.setIcon( icon1 );
            }
        }
        else
        {
             label.setIcon( icon1 );
        }
        return label;
    
    }
    
}
