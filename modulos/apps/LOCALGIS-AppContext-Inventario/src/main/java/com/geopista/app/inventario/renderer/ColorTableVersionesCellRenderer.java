/**
 * ColorTableVersionesCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 19-may-2005
 * Time: 17:19:16
 * To change this template use File | Settings | File Templates.
 */
public class ColorTableVersionesCellRenderer  extends JTextField implements TableCellRenderer {
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		// The current color to display
        Color curColor;

        public ColorTableVersionesCellRenderer(){
            super();
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

            if (isSelected) {
               
                setForeground(Color.white);
                setBackground(new Color(49,196,197));

            } 
            else{
                setForeground(curColor);
                setBackground(table.getBackground());
            }
          
            if (value instanceof java.util.Date)
            	setText(com.geopista.app.inventario.Constantes.df.format((java.util.Date)value));
            else
            	setText(String.valueOf(value));
            setBorder(new javax.swing.border.EmptyBorder(getBorder().getBorderInsets(this)));

            return this;
        }

}
