package com.geopista.app.ocupaciones.utilidades;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

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
