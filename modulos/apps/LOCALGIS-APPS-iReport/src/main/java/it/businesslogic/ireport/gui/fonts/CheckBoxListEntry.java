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
 * CheckBoxListEntry.java
 * 
 * Created on October 5, 2006, 10:19 AM
 *
 */

package it.businesslogic.ireport.gui.fonts;

import javax.swing.JCheckBox;

/**
 *
 * @author gtoffoli
 */
public class CheckBoxListEntry extends JCheckBox {
    
    private Object value = null;
    private boolean red = false;
    
    public CheckBoxListEntry(Object itemValue, boolean selected) {
        super(itemValue == null ? "" : ""+itemValue, selected);
        setValue( itemValue );
    }

    public boolean isSelected() {
        return super.isSelected();
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }
    
}
