/**
 * TextFieldTableEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.utilidades;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextField;

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
        JComponent component;

        public TextFieldTableEditor(){
            component = new JTextField();
        }

        public TextFieldTableEditor(int tamanno){
            component= new TextField(tamanno);
        }

        public TextFieldTableEditor(boolean numerico, int tamanno){
            if (numerico){
                component= new JNumberTextField(JNumberTextField.NUMBER, new Integer(tamanno), true);
            }else{
                component= new TextField(tamanno);
            }
        }


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

    /** Nuevo */
    public Component getEditorComponent(){
        return (JTextField)this.getEditorComponent();
    }


    public void setText(String text){
        _text= text;
    }

    public String getText(){
        return _text;
    }

    public void setEditable(boolean b){
        JTextField textField= (JTextField)component;
        textField.setEditable(b);
    }

    public void setEnabled(boolean b){
        JTextField textField= (JTextField)component;
        textField.setEnabled(b);
    }



}

