package com.geopista.app.licencias.utilidades;

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
