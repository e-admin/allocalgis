package com.geopista.app.inventario.renderer;

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
