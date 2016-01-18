/*
 * Copyright (C) 2005 - 2007 JasperSoft Corporation.  All rights reserved. 
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from JasperSoft,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 *
 *
 * ComboBoxSheetProperty.java
 * 
 * Created on 16 febbraio 2005, 19.13
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import java.util.*;
import javax.swing.*;
import it.businesslogic.ireport.util.*;

public class ComboBoxSheetProperty extends SheetProperty {
    
    public ComboBoxSheetProperty(String key, String name) {
        super( key, name, SheetProperty.COMBOBOX);
    }
    
    public Object getEditorValue(JComponent component)
    {
        return ((JComboBox)component).getSelectedItem();
    }
    
    public void setEditorValue(JComponent component, Object str)
    {
        try {
            ((JComboBox)component).setSelectedItem(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public void actionPerformed(java.awt.event.ActionEvent event)
    {
        if (isSetting()) return;
        Object new_value = getEditorValue( this.getEditor() );
        if (new_value != null && new_value.equals(value)) return;
        
        Object oldValue = value;
        value = new_value;
        //System.out.println(this.getName() + ": " + getValue());
        if(oldValue == null || value == null)
        {
            updateLabel();
        }
        
        fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(
            new SheetPropertyValueChangedEvent(getKeyName(),oldValue,value, this));
    }
     
     public void updateValues(Vector values, boolean addNullEntry){
     
         
        Misc.updateComboBox( (JComboBox)getEditor(), values, addNullEntry);
     
     }
}
