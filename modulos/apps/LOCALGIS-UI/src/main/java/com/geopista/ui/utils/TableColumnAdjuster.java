/**
 * TableColumnAdjuster.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.utils;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JTable;

public class TableColumnAdjuster implements ComponentListener {

  private JTable table;
  int oldWidth = -1;
  int oldHeight = -1;

  public TableColumnAdjuster(JTable table) {
    if (table.getParent() == null) {
      throw new IllegalStateException(
          "add table to JScrollPane before constructing TableColumnAdjuster");
    }
    table.getParent().getParent().addComponentListener(this);
    this.table = table;
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
  }

  public void componentResized(@SuppressWarnings("unused")ComponentEvent e) {
    if (oldWidth == table.getParent().getWidth()
        && oldHeight == table.getParent().getHeight())
      return;
    adjustColumns();
    oldWidth = table.getParent().getWidth();
    oldHeight = table.getParent().getHeight();
  }

  public void componentHidden(@SuppressWarnings("unused")ComponentEvent e) {
  }

  public void componentMoved(@SuppressWarnings("unused")ComponentEvent e) {
  }

  public void componentShown(@SuppressWarnings("unused")ComponentEvent e) {
  }

  public void adjustColumns() {
    // set the widths
    int averageWidth = table.getParent().getWidth()
        / table.getColumnCount();
    for (int c = 0; c < table.getColumnCount(); c++) {
      table.getColumnModel().getColumn(c).setPreferredWidth(averageWidth);
      table.getColumnModel().getColumn(c).setWidth(averageWidth);
    }
  }
  
}