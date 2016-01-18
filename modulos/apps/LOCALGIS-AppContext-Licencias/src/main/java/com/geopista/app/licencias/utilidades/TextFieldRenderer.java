/**
 * TextFieldRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.utilidades;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 03-jun-2004
 * Time: 18:24:03
 * To change this template use File | Settings | File Templates.
 */
    public class TextFieldRenderer extends JTextField implements TableCellRenderer {

            public TextFieldRenderer() {
                super();
            }


            public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    if (isSelected) {
                        /*
                        setForeground(table.getSelectionForeground());
                        super.setBackground(table.getSelectionBackground());
                        */
                        setForeground(Color.white);
                        setBackground(Color.blue);
                        
                    } else {
                        setForeground(table.getForeground());
                        setBackground(table.getBackground());
                    }

                    // Select the current value
                    setText((String)value);
                    return this;
            }
    }
