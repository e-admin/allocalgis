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
 * ResetButton.java
 * 
 * Created on March 7, 2006, 4:15 PM
 *
 */

package it.businesslogic.ireport.gui.sheet;

import it.businesslogic.ireport.gui.event.SheetPropertyValueChangedEvent;
import it.businesslogic.ireport.util.I18n;
import it.businesslogic.ireport.util.LanguageChangedEvent;
import it.businesslogic.ireport.util.LanguageChangedListener;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

/**
 *
 * @author gtoffoli
 */
public class ResetButton extends javax.swing.JButton implements LanguageChangedListener {
    
    private SheetProperty sheetProperty = null;
    public static final javax.swing.ImageIcon reset_icon = new javax.swing.ImageIcon( CategorySheetPanel.class.getResource("/it/businesslogic/ireport/icons/reset.png"));
    
            
    /** Creates a new instance of ResetButton */
    public ResetButton(SheetProperty sheetProperty) {
        
        setText("");
        setIcon(reset_icon);
        setMargin( new java.awt.Insets(0, 0, 0, 0));
        setMaximumSize(new java.awt.Dimension(16, 18));
        setMinimumSize(new java.awt.Dimension(16, 18));
        setPreferredSize(new java.awt.Dimension(16, 18));
        setBorder(new javax.swing.border.AbstractBorder() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
            {   
                g.setColor( java.awt.Color.GRAY);   
                g.drawLine(x,y,x,y+height);
            }
        }  );
        
        
        
        setFocusPainted(false);
        setFocusable(false);
        setBackground(java.awt.Color.WHITE);
        setToolTipText(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.tooltip.resetToDefault","Reset to default"));
        
        this.sheetProperty = sheetProperty;
        
        this.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SheetProperty sp = ResetButton.this.getSheetProperty();
                if (sp != null && !sp.isReadOnly())
                {
                    Object oldValue = sp.getValue();
                    sp.setValue(null);
                    sp.updateLabel();
                    sp.fireSheetPropertyValueChangedListenerSheetPropertyValueChanged(
                        new SheetPropertyValueChangedEvent(sp.getKeyName(),oldValue,null,sp) );
                }
            }
        });
        
        I18n.addOnLanguageChangedListener(this);
    }

    public void languageChanged(LanguageChangedEvent evt) {
        setToolTipText(it.businesslogic.ireport.util.I18n.getString("gui.elementpropertiessheet.tooltip.resetToDefault","Reset to default"));
    }

    public SheetProperty getSheetProperty() {
        return sheetProperty;
    }

    public void setSheetProperty(SheetProperty sheetProperty) {
        this.sheetProperty = sheetProperty;
    }
    
}
