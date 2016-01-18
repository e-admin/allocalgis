package com.geopista.app.ocupaciones.utilidades;

import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.app.ocupaciones.CMainOcupaciones;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-abr-2004
 * Time: 12:08:02
 * To change this template use File | Settings | File Templates.
 */

public class ComboBoxRenderer extends JComboBox implements TableCellRenderer {

        public ComboBoxRenderer(String[] items) {
            super(items);
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
