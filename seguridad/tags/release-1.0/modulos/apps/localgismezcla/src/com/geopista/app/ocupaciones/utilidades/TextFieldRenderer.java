package com.geopista.app.ocupaciones.utilidades;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

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
                        setBackground(table.getSelectionBackground());
                        */
                        setForeground(Color.white);
                        setBackground(Color.blue);
                        //setBackground(new Color(20,56,139)); // dark blue
                        //setBackground(new Color(113,183,225));// light blue

                    } else {
                        setForeground(table.getForeground());
                        setBackground(table.getBackground());
                    }

                    // Select the current value
                    setText((String)value);
                    return this;
            }
    }
