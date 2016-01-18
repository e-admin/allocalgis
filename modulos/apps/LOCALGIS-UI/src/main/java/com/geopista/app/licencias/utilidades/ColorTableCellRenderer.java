/**
 * ColorTableCellRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.utilidades;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

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
public class ColorTableCellRenderer  extends JTextField implements TableCellRenderer {
        // The current color to display
        Color curColor;
        Vector redRows;

        public ColorTableCellRenderer(){
            super();
        }

        public ColorTableCellRenderer(Vector v){
            super();
            redRows= v;
        }


        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

            if (isSelected) {
                // NOTA: al ejecutarlo desde el instalador v05, la fila seleccionada aparecia
                // oculta(al seleccionarla se ponia en blanco).
                /*
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
                */
                setForeground(Color.white);
                //setBackground(new Color(49,106,197));
                setBackground(new Color(49,196,197));

            } else {
                if (redRows != null){

                    if (redRows.size()>rowIndex && redRows.get(rowIndex) != null){

                        curColor= Color.red;
                    }else curColor= Color.black;
                }else  curColor= Color.black;
                setForeground(curColor);
                setBackground(table.getBackground());
            }

            // Select the current value            		
            setText(value==null?"":value.toString());
            //setText(String.valueOf(value));

            setBorder(new javax.swing.border.EmptyBorder(getBorder().getBorderInsets(this)));

            return this;
        }

}
