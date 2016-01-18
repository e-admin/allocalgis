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
 * SheetProperty.java
 * 
 * Created on 4 ottobre 2004, 22.08
 *
 */

package it.businesslogic.ireport.gui.sheet;
import com.lowagie.text.Font;
import java.awt.event.FocusEvent;
import java.util.*;
import javax.swing.*;
import it.businesslogic.ireport.gui.event.*;
import java.awt.Color;
import java.awt.event.FocusListener;
/**
 *
 * @author  Administrator
 */
public class SheetProperty implements java.awt.event.ActionListener {
    
    public static final int STRING = 0;
    public static final int NUMBER = 1;
    public static final int INTEGER = 6;
    public static final int COMBOBOX = 2;
    public static final int COLOR = 3;
    public static final int BOOLEAN = 4;
    public static final int COMBOBOX_NK = 5; // Combobox number key
    public static final int PASSWORD = 7;
    public static final int CATEGORY_LABEL = 99;
    public static final int TEXT = 8;
        
    protected String name = "";
    protected String keyName = "";
    protected String group = "";
    protected int type;
    private java.util.Vector tags = null;
    protected Object defaultValue = null;
    protected Object value = null;
    
    private boolean readOnly = false;
    
    private boolean setting = false;
    
    protected JComponent component = null;
    
    private JComponent labelComponent = null;
    
    private boolean showResetButton = true;
    
    private java.awt.Color labelColor = java.awt.Color.BLACK;
    
    private Vector currentSelection = new Vector();
    
    private String labelError = null;
    private static final Color errorColor = Color.RED.darker();
    private static final ImageIcon errorIcon = new javax.swing.ImageIcon(SheetProperty.class.getResource("/it/businesslogic/ireport/icons/problems/error.png"));
   
    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;
    
    public SheetProperty(String keyName, String name, int type) {
       this(keyName,name,type,null);
    }
    /** Creates a new instance of SheetProperty */
    public SheetProperty(String keyName, String name, int type, Object defaultValue) {
        this.setName( name );
        this.setKeyName( keyName );
        this.setDefaultValue( defaultValue );
        setType(type);
        tags =  new Vector();
    }
    
    public SheetProperty(String keyName, String name,String groupname, int type, Object defaultValue) {
        this.setName( name );
        this.setKeyName( keyName );
        this.setDefaultValue( defaultValue );
        setType(type);
        tags =  new Vector();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public java.util.Vector getTags() {
        return tags;
    }

    public void setTags(java.util.Vector tags) {
        this.tags = tags;
    }
    
    public void setTags(Tag[] tags_array) {
         
        this.tags = new Vector();
        
        for (int i=0; i<tags_array.length; ++i)
        {
            tags.add(tags_array[i]);
        }
    }
    
    public JComponent getEditor()
    {
        
        if (component != null) return component;
        
        if (this.getType() == NUMBER)
        {
            component = new it.businesslogic.ireport.gui.JNumberField();
            component.setBorder(null);
            
            ((it.businesslogic.ireport.gui.JNumberField)component).addActionListener( this );
        
        }
        else if (this.getType() == INTEGER)
        {
            component = new it.businesslogic.ireport.gui.JNumberField();
            component.setBorder(null);
            try {
            ((it.businesslogic.ireport.gui.JNumberField)component).setInteger(true);
            } catch (Exception ex){}
            
            ((it.businesslogic.ireport.gui.JNumberField)component).addActionListener( this );
        
        }
        else if (this.getType() == COMBOBOX || this.getType() == COMBOBOX_NK)
        {
            component = new JComboBox(this.getTags());
            component.setBorder(null);
            
            //((JComboBox)component).setEditor(new SimpleComboBoxEditor() );
            
            for (int i=0; i< component.getComponentCount(); ++i)
            {
                try {
                        Object obj = component.getComponent(i);
                        //System.out.println( obj );                    
                        if (obj != null && obj.getClass().getMethod("setBorder", new Class[]{javax.swing.border.Border.class}) != null)
                        {
                            java.lang.reflect.Method mtd = obj.getClass().getMethod("setBorder", new Class[]{javax.swing.border.Border.class});
                            mtd.invoke(obj, new Object[]{null});
                        }
                } catch (Exception ex) { }
            }
            
            ((JComboBox)component).addActionListener( this );            
        }
        else if (this.getType() == BOOLEAN)
        {
            component = new JCheckBox("");
            ((JCheckBox)component).addActionListener( this );
        }
        else if (this.getType() == COLOR)
        {
            component = new ColorSelectorPanel();
            ((ColorSelectorPanel)component).addActionListener( this );
        }
        else if (this.getType() == CATEGORY_LABEL)
        {
            component = new JPanel();
            component.setBackground( java.awt.Color.LIGHT_GRAY);
        }
        else if (this.getType() == PASSWORD)
        {
            component = new JPasswordField();
            component.setBorder(null);
            ((JTextField)component).getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
        });
        }
        else if (this.getType() == TEXT)
        {
            component = new JTextField();
            component.setBorder(null);
            // Value changed of return and focusLost...
            ((JTextField)component).addActionListener(this);
            ((JTextField)component).addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent e) {
                        ((JTextField)e.getComponent()).selectAll();
                    }

                    public void focusLost(FocusEvent e) {
                        actionPerformed(null);
                    }
            });
                    
        }
        else
        {
            // default (STRING)
            component = new JTextField();
            component.setBorder(null);
            ((JTextField)component).getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                actionPerformed(null);
            }
        });
        }
        
        
        return  component;
        
    }
    
    public Object getEditorValue(JComponent component)
    {
       if (this.getType() == NUMBER)
        {
           return new Double(((it.businesslogic.ireport.gui.JNumberField)component).getValue());
        }
       else if (this.getType() == INTEGER)
        {
            return new Integer( (int)((it.businesslogic.ireport.gui.JNumberField)component).getValue());
        }
        else if (this.getType() == COMBOBOX)
        {
           Object obj = ((JComboBox)component).getSelectedItem();
           if (obj != null)
           {
               if (obj instanceof Tag)
               {
                    return ((Tag)obj).getValue();
               }
               else
               {
                   return obj;
               }
           }
           return null;
        }
       else if (this.getType() == COMBOBOX_NK)
        {
           Object obj = ((JComboBox)component).getSelectedItem();
           if (obj != null)
           {
               return new Integer(((Tag)obj).getValue()+"");
           }
           return null;
        }
        else if (this.getType() == BOOLEAN)
        {
            return  new Boolean(((JCheckBox)component).isSelected());
        }
        else if (this.getType() == BOOLEAN)
        {
            return  new String( ((JPasswordField)component).getPassword() );
        }
        else if (this.getType() == COLOR)
        {
            return ((ColorSelectorPanel)component).getValue();
        }
       
        return ((JTextField)component).getText();
    }
    
    public synchronized void  setEditorValue(JComponent component, Object obj)
    {
        
        String str = ""+obj;
        
        component.setForeground(Color.BLACK );
        
        try {
            
        setSetting(true);
        if (this.getType() == NUMBER)
        {
           if (obj == null) str ="0";
            ((it.businesslogic.ireport.gui.JNumberField)component).setValue(Double.parseDouble(str) );
            
           if (obj == null) ((it.businesslogic.ireport.gui.JNumberField)component).setForeground( Color.LIGHT_GRAY);
           
        }
       else if (this.getType() == INTEGER)
        {
           if (obj == null) str ="0";
            ((it.businesslogic.ireport.gui.JNumberField)component).setValue(Integer.parseInt(str) );
            if (obj == null) ((it.businesslogic.ireport.gui.JNumberField)component).setForeground( Color.LIGHT_GRAY);
        }
        else if (this.getType() == COMBOBOX)
        {
           if (obj == null) { 
                setSetting(false);
                if (getTags().size() > 0 && ((Tag)getTags().elementAt(0)).getValue() == null)
                {
                    ((JComboBox)component).setSelectedIndex(0);
                }
                return;
           }
           boolean found = false;
           for (int i=0; i<getTags().size(); ++i)
           {
                
                Tag t = (Tag)getTags().elementAt(i);
                if (t.getValue() != null && t.getValue().equals(str))
                {
                    found = true;
                    ((JComboBox)component).setSelectedIndex(i);
                    break;
                }
           }
           if (!found && ((JComboBox)component).isEditable())
           {
               ((JComboBox)component).setSelectedItem(obj);
           }
        }
        else if (this.getType() == COMBOBOX_NK)
        {
            if (obj == null) { setSetting(false); 
                if (getTags().size() > 0)
                {
                    ((JComboBox)component).setSelectedIndex(0);
                }
                return;
            }
            for (int i=0; i<getTags().size(); ++i)
            {
                Tag t = (Tag)getTags().elementAt(i);
                if ((t.getValue()+"").equals(str))
                {
                    ((JComboBox)component).setSelectedIndex(i);
                    break;
                }
            }
        }
        else if (this.getType() == BOOLEAN)
        {
            if (obj == null) {  ((JCheckBox)component).setSelected(false); setSetting(false); return;}
            ((JCheckBox)component).setSelected(str.equals("true"));
        }
        else if (this.getType() == COLOR)
        {
            ((ColorSelectorPanel)component).setValue(obj);
        }
        else if (this.getType() == PASSWORD)
        {
            if (obj == null) { ((JPasswordField)component).setText("");  setSetting(false);  return;}
            ((JPasswordField)component).setText(str);
        }
        else
        {
            if (obj == null) { ((JTextField)component).setText(""); setSetting(false); return;}
            ((JTextField)component).setText(str);
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            setSetting(false);
        }
        
    }

    public String getKeyName() {
        return keyName; 
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        
        this.value = value;
        this.setEditorValue(getEditor(), (value == null) ? getDefaultValue() : value);
        //updateLabel();
        
    }
    
    public void updateLabel()
    {
        
        if (this.getType() != CATEGORY_LABEL)
        {
            try {
                if (getLabelComponent() != null)
                {

                    //String bold = (value == null || this.isReadOnly() || !isShowResetButton()) ? "" : "<html><b>";
                    
                    boolean needBold = !(value == null || this.isReadOnly() || !isShowResetButton());
                    
                    java.awt.Font f = getLabelComponent().getFont();
                    
                    if (f.isBold() && !needBold)
                    {
                        ((JLabel)getLabelComponent()).setFont( f.deriveFont( Font.NORMAL) );
                    }
                    else if (!f.isBold() && needBold)
                    {
                        ((JLabel)getLabelComponent()).setFont( f.deriveFont( Font.BOLD ) );
                    }
                    
                    // The table is used to avoid word wrap
                    //((JLabel)getLabelComponent()).setText("<html><table><tr><td nowrap>" + bold + this.getName() + "</td></tr></table></html>");
                    ((JLabel)getLabelComponent()).setText(this.getName());
                    
                    
                    if (this.isReadOnly())
                    {
                        getLabelComponent().setEnabled( false );
                    }
                    else
                    {
                         getLabelComponent().setEnabled( true );
                    }
                    
                    if (labelError != null)
                    {
                        ((JLabel)getLabelComponent()).setIcon(errorIcon);
                        getLabelComponent().setForeground( errorColor);
                        getLabelComponent().setToolTipText( labelError );
                    }
                    else
                    {
                        ((JLabel)getLabelComponent()).setIcon(null);
                        getLabelComponent().setForeground( getLabelColor());
                        getLabelComponent().setToolTipText( getName() );
                    }
                    
                    
                    getLabelComponent().updateUI();
                }
            } catch (Exception ex)
            {

            }
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
        if (isSetting()) return;
        getEditor().setForeground( Color.BLACK);
        Object new_value = getEditorValue( this.getEditor() );
        if (new_value != null && new_value.equals(value)) return;
                
        Object oldValue = value;
        value = new_value;
        
        if(oldValue == null || new_value == null)
        {
            updateLabel();
        }
        
        fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(
            new SheetPropertyValueChangedEvent(getKeyName(),oldValue,new_value, this));
        
        if (getType() == TEXT || getType() == NUMBER)
        {
            FocusManager.getCurrentManager().clearGlobalFocusOwner();
        }
    }

    /**
     * Registers SheetPropertyValueChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addSheetPropertyValueChangedListener(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class, listener);
    }

    /**
     * Removes SheetPropertyValueChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeSheetPropertyValueChangedListener(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    public void fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.SheetPropertyValueChangedListener)listeners[i+1]).sheetPropertyValueChanged (event);
            }
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        getEditor().setEnabled(!readOnly);
    }

    public JComponent getLabelComponent() {
        return labelComponent;
    }

    public void setLabelComponent(JComponent labelComponent) {
        this.labelComponent = labelComponent;
    }

    public boolean isSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    public boolean isShowResetButton() {
        return showResetButton;
    }

    public void setShowResetButton(boolean showResetButton) {
        this.showResetButton = showResetButton;
    }

    public java.awt.Color getLabelColor() {
        return labelColor;
    }
    

    public void setLabelColor(java.awt.Color labelColor) {
        this.labelColor = labelColor;
    }

    public Vector getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(Vector currentSelection) {
        this.currentSelection = currentSelection;
    }
    


    public String getLabelError() {
        return labelError;
    }

    public void setLabelError(String labelError) {
        this.labelError = labelError;
    }
}
