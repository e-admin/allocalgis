/**
 * TableAttributesCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.schema.attribute;


/**
 * Define el aspecto de la lista de atributos de una capa
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

public class TableAttributesCellRenderer extends JComponent implements TableCellRenderer
{   
    /**
     * Constructor por defecto
     */
    public TableAttributesCellRenderer()
    {
        super();
        setOpaque(true);           
    }
    
    /**
     * Obtiene el componente a pintar
     * 
     * @param table La tabla en la que está el componente
     * @param value Atributo a pintar
     * @param sel Verdadero si el componente está seleccionado
     * @param hasFocus Verdadero si el componente tiene el foco
     * @param rowIndex Índice de la fila
     * @param vColIndex Índice de la columna
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean sel, boolean hasFocus, int rowIndex, int vColIndex) {
       
        // Configure the component with the specified value
        if (value instanceof Column)
        {    
            
            Column col = (Column)value;  
            JComponent comp;
            
            if (col.getTable()!=null && col.getName()!=null)
                comp = new JLabel(col.getTable().getName() + "." + col.getName());
            else
                comp = new JLabel();
            
            
            if (sel)
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
