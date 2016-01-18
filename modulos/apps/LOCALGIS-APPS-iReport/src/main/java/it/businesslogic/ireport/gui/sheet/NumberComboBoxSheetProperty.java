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
 * NumberComboBoxSheetProperty.java
 * 
 * Created on 16 febbraio 2005, 19.13
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.gui.JNumberComboBox;
import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.util.Misc;

import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;

public class NumberComboBoxSheetProperty extends SheetProperty
{
    
    JNumberComboBox jNumberComboBox=null;   
    
    public NumberComboBoxSheetProperty(String key, String name) {
        super( key, name, SheetProperty.COMBOBOX);        
    }
    
    public Object getEditorValue(JComponent component)
    {
        return ""+(int)jNumberComboBox.getValue();
    }
    
    public void setValue(Object value)
    {
        //if (this.value == value || (this.value+"").equals(value+"")) return;
        
        this.value = value;
        this.setEditorValue(getEditor(), (value == null) ? getDefaultValue() : value);
        updateLabel();
    }
    
    public void setEditorValue(JComponent component, Object str)
    {
        if (str == null || str.equals(""))
        {
            str = getDefaultValue();
        }
        if (str == null) str = "";
   
        boolean setting = isSetting();
        this.setSetting(true);
        try {
            ((JComboBox)component).setSelectedItem(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.setSetting(setting);
    }
    
    public void addEntry(String name, int value)
    {
        ((JNumberComboBox)getEditor()).addEntry(name, (double)value);
    }
    
    public JComponent getEditor()
    {
        if (jNumberComboBox != null) return jNumberComboBox;
        
        jNumberComboBox = new JNumberComboBox();
        jNumberComboBox.setEditable(true);

        /*
        for (int i=0; i<this.getTags().size(); ++i)
        {
            Tag t = (Tag)getTags().elementAt(i);
            jNumberComboBox.addEntry( t.getName(), Double.parseDouble( t.getValue()+""));
        }
        */
        
        jNumberComboBox.setBorder(null);
        
        /*
        ((JComboBox)jNumberComboBox).setEditor(new SimpleComboBoxEditor() );
        for (int i=0; i< jNumberComboBox.getComponentCount(); ++i)
        {
                try {
                        Object obj = jNumberComboBox.getComponent(i);
                        //System.out.println( obj );                    
                        if (obj != null && obj.getClass().getMethod("setBorder", new Class[]{javax.swing.border.Border.class}) != null)
                        {
                            java.lang.reflect.Method mtd = obj.getClass().getMethod("setBorder", new Class[]{javax.swing.border.Border.class});
                            mtd.invoke(obj, new Object[]{null});
                        }
                } catch (Exception ex) { }
            }
       
        */    
       jNumberComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                
                if (evt.getStateChange() == evt.SELECTED)
                {
                    actionPerformed(null);
                }
            }
        });
        jNumberComboBox.addActionListener(this);
        
        return jNumberComboBox;    
    }
    
     public void actionPerformed(java.awt.event.ActionEvent event)
     {
        if (isSetting()) return;
        
        //System.out.println("Value changed!" + evt.getOldValue() + " --> " +  evt.getNewValue());
        

        Object new_value = getEditorValue( this.getEditor() );
        if (new_value != null && new_value.equals(value)) return;
        if (new_value != null)
        {
            try {
                Double.parseDouble(new_value+"");
            } catch (Exception ex)
            {
                return;
            }
        }
        
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
