package com.geopista.app.contaminantes;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 21-abr-2004
 * Time: 16:54:56
 * To change this template use File | Settings | File Templates.
 */

public class TextFieldTableEditor extends AbstractCellEditor implements TableCellEditor {

        private String _text= "";
    
        // Componente que maneja la edicion del valor de la celda
        JComponent component = new JTextField();

        // Este metodo se llama cuando el valor de una celda es editada por el usuario
        // 'value' es el valor contenido en la celda localizada en (rowIndex, vColIndex)
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int rowIndex, int vColIndex) {

            if (isSelected) {
                // La celda ha sido seleccionada
                ((JTextField)component).requestFocus();
            }
            ((JTextField)component).requestFocus();

            // Se asocia al editor con el valor
            ((JTextField)component).setText((String)value);

            return component;
        }

        // This method is called when editing is completed.
        // It must return the new value to be stored in the cell.
        public Object getCellEditorValue() {
            return ((JTextField)component).getText();
        }

    public void setText(String text){
        _text= text;
    }

    public String getText(){
        return _text;
    }


}

