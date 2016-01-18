/**
 * CheckBoxTableEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-abr-2004
 * Time: 12:15:16
 * To change this template use File | Settings | File Templates.
 */
 public class CheckBoxTableEditor extends DefaultCellEditor {

     private boolean _selected= false;

     public CheckBoxTableEditor(JCheckBox check)
     {
         super(check);
     }


     // This method is called when a cell value is edited by the user.
     public Component getTableCellEditorComponent(JTable table, Object value,
             boolean isSelected, int rowIndex, int vColIndex) {

         // 'value' is value contained in the cell located at (rowIndex, vColIndex)
         JCheckBox check = (JCheckBox)this.getComponent();

         if (isSelected) {
         }

         // Return the configured component
         return check;
     }

     // This method is called when editing is completed.
     // It must return the new value to be stored in the cell.
     public Object getCellEditorValue() {
         JCheckBox check = (JCheckBox)this.getComponent();
         /*
         System.out.println("ResultadosCheckBoxTableEditor.getCellEditorValue");
         System.out.println("check: "+check);
         System.out.println("isSelected= "+check.isSelected());
         */
         return new Boolean(check.isSelected());
     }

}
