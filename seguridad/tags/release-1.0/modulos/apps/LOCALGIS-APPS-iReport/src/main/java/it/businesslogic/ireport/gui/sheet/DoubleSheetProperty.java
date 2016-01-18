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
 * DoubleSheetProperty.java
 * 
 * Created on 16 febbraio 2005, 19.13
 *
 */

package it.businesslogic.ireport.gui.sheet;

import java.util.*;
import javax.swing.*;
import it.businesslogic.ireport.util.*;
import it.businesslogic.ireport.gui.*;

public class DoubleSheetProperty extends SheetProperty {
    
    private JNumberField editor = null;
    
    public DoubleSheetProperty(String key, String name) {
        super( key, name, SheetProperty.STRING, "");
    }
    
    public JComponent getEditor()
    {
        if (editor != null) return editor;
        editor = new JNumberField();
        try {
        editor.setDecimals(-1);
        editor.setGrouping(false);
        editor.setInteger(false);
        } catch (Exception ex) {}
        editor.addActionListener( this );
        editor.setBorder(null);
        return editor;
    }
    
    public Object getEditorValue(JComponent component)
    {
        try {
            return new Double(editor.getValue());
        } catch (Exception ex)
        {
            return new Double(0);
        }
    }
    
    public void setEditorValue(JComponent component, Object str)
    {
        
        try {
            getEditor(); // In this way we are sure that editor is not null.
            
            if (str != null && str instanceof Double )
            {
                editor.setValue(((Double)str).doubleValue() );
            }
            else if (str != null && ( (""+str).trim().length() > 0) )
            {
                editor.setValue( Double.parseDouble(""+str));
            }
        } catch (Exception ex) {}
    }
}
