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
 * PatternSheetProperty.java
 * 
 * Created on 16 febbraio 2005, 19.13
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.ExpressionContext;
import javax.swing.*;

public class PatternSheetProperty extends SheetProperty {
    
    private PatternSheetPropertyComponent editor = null;

    public PatternSheetProperty(String key, String name) {
        super( key, name, SheetProperty.STRING, "");
    }
    
    public JComponent getEditor()
    {
        if (editor != null) return editor;
        editor = new PatternSheetPropertyComponent();
        editor.addActionListener( this );
        return editor;
    }
    
    public Object getEditorValue(JComponent component)
    {
        return editor.getExpression();
    }
    
    public void setEditorValue(JComponent component, Object str)
    {
        try {
            getEditor(); // In this way we are sure that editor is not null.
            
            if (str != null)
            {
                editor.setExpression(""+str);
            }
            else
            {
                editor.setExpression("");
            }
        } catch (Exception ex) {}
    }
}
