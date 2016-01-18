/**
 * AttributeEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.schema.attribute;


/**
 * Editor para los componentes de tipo Attribute
 * 
 * @author cotesa
 *
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.exception.DataException;
import com.geopista.app.layerutil.layer.LayerOperations;
import com.geopista.app.layerutil.util.JDialogTranslations;
import com.geopista.feature.Attribute;


public class AttributeEditor extends AbstractCellEditor implements TableCellEditor, ActionListener 
{    
    
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    Attribute currentAttribute;
    Hashtable traducciones = new Hashtable();
    JButton button;
    JDialogTranslations jDiccionario;
    
    
    protected static final String EDIT = "edit";
    
    /**
     * Constructor de la clase
     *
     */
    public AttributeEditor() {
        //Set up the editor (from the table's point of view),
        //which is a button.
        //This button brings up the color chooser dialog,
        //which is the editor from the user's point of view.
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
    }
    
    /**
     * Handles events from the editor button and from
     * the dialog's OK button.
     */
    public void actionPerformed(ActionEvent e) {        
        
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            //button.setBackground(currentColor);
            
            LayerOperations operaciones = new LayerOperations();
            
            if (currentAttribute.getHtTraducciones()==null)
            {
                try
                {
                    traducciones = operaciones.buscarTraduccionAtributos(currentAttribute);
                } catch (DataException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            else
            {
                traducciones = currentAttribute.getHtTraducciones();
            }                
            
            jDiccionario = new JDialogTranslations(aplicacion.getMainFrame(), true, traducciones,true);
            jDiccionario.setSize(600,500);
            jDiccionario.show();  
            Hashtable diccionario = jDiccionario.getDiccionario();
            if (diccionario !=null)
                currentAttribute.setHtTraducciones(diccionario);
            
            //Make the renderer reappear.
            fireEditingStopped();
        } 
        else 
        { 
            //User pressed dialog's "OK" button.
            currentAttribute.setHtTraducciones(currentAttribute.getHtTraducciones());
        }
    }
    
    /** 
     * Implements the one CellEditor method that AbstractCellEditor doesn't.
     */
    public Object getCellEditorValue() {
        return currentAttribute;
    }
    
    /**
     * Implements the one method defined by TableCellEditor.
     */
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        
        currentAttribute = (Attribute)value;       
        return button;
    }
}

