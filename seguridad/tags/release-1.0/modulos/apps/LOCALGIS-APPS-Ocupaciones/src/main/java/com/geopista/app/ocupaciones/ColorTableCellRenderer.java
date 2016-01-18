package com.geopista.app.ocupaciones;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

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
                setBackground(Color.blue);
                
            } else {
                if (redRows != null){
                    if (redRows.size()>0 && redRows.get(rowIndex) != null){
                        curColor= Color.red;
                    }else curColor= Color.black;
                }else  curColor= Color.black;
                setForeground(curColor);
                setBackground(table.getBackground());
            }

            // Select the current value
            setText((String)value);
            setBorder(new javax.swing.border.EmptyBorder(getBorder().getBorderInsets(this)));

            return this;
        }

}
