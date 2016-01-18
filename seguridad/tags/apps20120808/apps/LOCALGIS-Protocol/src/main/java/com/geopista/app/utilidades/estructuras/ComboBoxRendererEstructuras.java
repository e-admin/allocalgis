package com.geopista.app.utilidades.estructuras;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 06-oct-2004
 * Time: 16:44:09
 * To change this template use File | Settings | File Templates.
 */
 public class ComboBoxRendererEstructuras extends ComboBoxEstructuras implements TableCellRenderer {

        public ComboBoxRendererEstructuras(com.geopista.protocol.ListaEstructuras lista, java.awt.event.ActionListener accion, String locale, boolean conBlanco)
        {
            super(lista, accion, locale,conBlanco) ;
        }


        public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (isSelected) {
                    setForeground(table.getSelectionForeground());
                    super.setBackground(table.getSelectionBackground());
                } else {
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }

                // Select the current value
                setSelectedItem(value);
                return this;
        }
    }
