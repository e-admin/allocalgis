/**
 * CheckBoxRenderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 10-may-2004
 * Time: 11:37:17
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

  public CheckBoxRenderer() {
    setHorizontalAlignment(JLabel.CENTER);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                         boolean isSelected, boolean hasFocus,
                          int row, int column) {

    if (isSelected) {
      setForeground(table.getSelectionForeground());
      //super.setBackground(table.getSelectionBackground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }

    setSelected(value != null && ((Boolean)value).booleanValue());
    return this;
  }
}
