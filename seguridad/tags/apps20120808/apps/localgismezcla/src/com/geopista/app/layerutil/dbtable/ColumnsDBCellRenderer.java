/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.dbtable;


/**
 * Define el aspecto de la lista de columnas de Base de Datos
 * 
 * @author cotesa
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.geopista.feature.Column;



public class ColumnsDBCellRenderer extends JComponent implements TableCellRenderer
{   
    /**
     * Constructor por defecto de la clase
     *
     */
    public ColumnsDBCellRenderer()
    {
        super();
        setOpaque(true);           
    }
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param table La tabla en la que está el componente
     * @param value Atributo a pintar
     * @param isSelected Verdadero si el componente está seleccionado
     * @param hasFocus Verdadero si el componente tiene el foco
     * @param rowIndex Índice de la fila
     * @param vColIndex Índice de la columna
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at
        // (rowIndex, vColIndex)
        
        
        // Configure the component with the specified value
        if (value instanceof ColumnDB)
        {               
            ColumnDB col = (ColumnDB)value;  
            JComponent comp;
            
            if (col.getName()!=null)
                comp = new JLabel(col.getName());
            else
                comp = new JLabel("");
            
            
            if (isSelected)
            {
                comp.setBackground(SystemColor.activeCaption);
                comp.setForeground(SystemColor.activeCaptionText);
                comp.setOpaque(true);
            }
            else
            {
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);
                comp.setOpaque(true);
            }

            return comp;
        }
        
        else if (value instanceof Column)
        {               
            Column col = (Column)value;  
            JComponent comp;
            
            if (col.getName()!=null)
                comp = new JLabel(col.getName());
            else
                comp = new JLabel("");
            
            if (isSelected)
            {
                comp.setBackground(SystemColor.activeCaption);
                comp.setForeground(SystemColor.activeCaptionText);
                comp.setOpaque(true);
            }
            else
            {
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);
                comp.setOpaque(true);
            }

            
            return comp;
        }
        
        // Since the renderer is a component, return itself
        return this;
    }
    
}
