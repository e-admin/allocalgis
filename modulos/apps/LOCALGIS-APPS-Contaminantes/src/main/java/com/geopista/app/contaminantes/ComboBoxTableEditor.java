/**
 * ComboBoxTableEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.contaminantes;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.Vector;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-abr-2004
 * Time: 12:15:16
 * To change this template use File | Settings | File Templates.
 */
 public class ComboBoxTableEditor extends DefaultCellEditor {

     private int _selectedIndex= -1;

     public ComboBoxTableEditor(JComboBox combo)
     {
         super(combo);
     }


     // This method is called when a cell value is edited by the user.
     public Component getTableCellEditorComponent(JTable table, Object value,
             boolean isSelected, int rowIndex, int vColIndex) {

         // 'value' is value contained in the cell located at (rowIndex, vColIndex)
          JComboBox combo = (JComboBox)this.getComponent();

         if (isSelected) {
             // cell (and perhaps other cells) are selected
             combo.setSelectedIndex(((JComboBox)this.getComponent()).getSelectedIndex());
         }

         // Configure the component with the specified value
         combo.setSelectedItem(value);

         // Return the configured component
         return combo;
     }

    public Component getEditorComponent(){
        return (JComboBox)this.getComponent();
    }


     // This method is called when editing is completed.
     // It must return the new value to be stored in the cell.
     public Object getCellEditorValue() {
         JComboBox combo = (JComboBox)this.getComponent();
         setSelectedIndex(combo.getSelectedIndex());
         /*
         System.out.println("ListaAnexosComboBoxTableEditor.getCellEditorValue");
         System.out.println("combo: "+combo);
         System.out.println("Value= "+combo.getSelectedItem());
         System.out.println("Index= "+getSelectedIndex());
         */
         return combo.getSelectedItem();
     }


    public void setSelectedIndex(int index){
        _selectedIndex= index;
    }

    public int getSelectedIndex(){
        return _selectedIndex;
    }

}
