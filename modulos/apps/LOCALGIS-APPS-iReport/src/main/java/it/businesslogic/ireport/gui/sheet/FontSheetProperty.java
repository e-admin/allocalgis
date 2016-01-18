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
 * FontSheetProperty.java
 * 
 * Created on 16 febbraio 2005, 19.13
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.IReportFont;

import java.util.Iterator;

import javax.swing.JComponent;

public class FontSheetProperty extends SheetProperty {
    
    private FontSheetPropertyComponent editor = null;
    
    public FontSheetProperty(String key, String name) {
        super( key, name, SheetProperty.STRING, "");
    }
    
    public JComponent getEditor()
    {
        if (editor != null) return editor;
        editor = new FontSheetPropertyComponent();
        editor.addActionListener( this );
        return editor;
    }
    
    public Object getEditorValue(JComponent component)
    {
        return editor.getIreportFont();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
        super.actionPerformed(event);
        updateLabel();
    }
    
    
    public void setEditorValue(JComponent component, Object value)
    {
        try {
            getEditor(); // In this way we are sure that editor is not null.
            editor.setIreportFont((it.businesslogic.ireport.IReportFont)value);
        } catch (Exception ex) {}
    }
    
    public void setFontMode( int mode)
    {
        try {
            getEditor(); // In this way we are sure that editor is not null.
            editor.setFontMode( mode );
        } catch (Exception ex) {}
    }
    
    
    /**
     * The font propery is ever not null. We should check in in the IReportFont there is something
     * of set or not...
     */
    public void updateLabel()
    {
        try {
                if (getLabelComponent() != null)
                {
                    boolean allNullValue = true;
                    
                    IReportFont ifont = (IReportFont)getValue();
                    
                    if (ifont != null)
                    {
                        Iterator i_keys = ifont.getBeanProperties().keySet().iterator();

                        while (i_keys.hasNext())
                        {
                            Object key = i_keys.next();
                            if (ifont.getBeanProperties().get(key) != null)
                            {
                                allNullValue = false;
                                break;
                            }
                        }
                    }
                    
                    java.awt.Font f = getLabelComponent().getFont();
                    java.awt.Font f2 = new java.awt.Font( f.getName(), (allNullValue || this.isReadOnly() || !isShowResetButton()) ? 0 : java.awt.Font.BOLD, f.getSize());
                    getLabelComponent().setFont(f2);
                    if (this.isReadOnly())
                    {
                        getLabelComponent().setEnabled( false );
                    }
                    else
                    {
                         getLabelComponent().setEnabled( true );
                    }
                    
                    
                    getLabelComponent().updateUI();
                }
            } catch (Exception ex)
            {

            }
        }

}
