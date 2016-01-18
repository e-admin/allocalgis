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
 * ValueDisplay.java
 * 
 * Created on September 28, 2006, 4:52 PM
 *
 */

package it.businesslogic.ireport.chart;

import it.businesslogic.ireport.IReportFont;
import java.awt.Color;

/**
 *
 * @author gtoffoli
 */
public class ValueDisplay {
    
    private IReportFont font = null;
    private java.awt.Color color = null;
    private String mask = "";
    
    /** Creates a new instance of ValueDisplay */
    public ValueDisplay() {
    }
    
    public ValueDisplay cloneMe()
    {
        ValueDisplay obj = new ValueDisplay();
        if (font != null) obj.setFont( (IReportFont)font.clone() );
        if (color != null) obj.setColor( new Color( color.getRGB() ));
        obj.setMask( new String(getMask()) );
        return obj;
    }

    public IReportFont getFont() {
        return font;
    }

    public void setFont(IReportFont font) {
        this.font = font;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
    
}
