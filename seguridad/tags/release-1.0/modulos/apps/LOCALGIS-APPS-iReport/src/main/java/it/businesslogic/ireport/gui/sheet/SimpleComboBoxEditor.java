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
 * SimpleComboBoxEditor.java
 * 
 * Created on 15 agosto 2005, 17.05
 *
 */

package it.businesslogic.ireport.gui.sheet;

import javax.swing.ComboBoxEditor;
import javax.swing.JLabel;

/**
 *
 * @author Administrator
 */
public class SimpleComboBoxEditor implements ComboBoxEditor {
    
    private JLabel jlable = null;
    private Object item = null;
    
    /** Creates a new instance of SimpleComboBoxEditor */
    public SimpleComboBoxEditor() {
       
    }

    public void addActionListener(java.awt.event.ActionListener actionListener) {
    }

    public java.awt.Component getEditorComponent() {
        
        if (jlable == null){
            jlable = new JLabel();
            jlable.setBorder( null );
        }
    
        return jlable;
    }

    public Object getItem() {
        return item;
    }

    public void removeActionListener(java.awt.event.ActionListener actionListener) {
    }

    public void selectAll() {
    }

    public void setItem(Object obj) {
        item = obj;
        jlable.setText( item + "");
    }
    
}
