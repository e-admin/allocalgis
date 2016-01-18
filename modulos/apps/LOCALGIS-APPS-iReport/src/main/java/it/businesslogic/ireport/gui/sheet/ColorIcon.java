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
 * ColorIcon.java
 * 
 * Created on September 27, 2006, 10:35 AM
 *
 */

package it.businesslogic.ireport.gui.sheet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.SwingConstants;

public class ColorIcon implements Icon, SwingConstants {
    private int width = 32;
    private int height = 14;
    private java.awt.Color theColor = java.awt.Color.WHITE;

    public ColorIcon(java.awt.Color c) {
    	theColor = c;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        
        Color c1 = theColor;
        Color c2 = Color.BLACK;
        
        if (theColor == null)
        {
            c1 = Color.WHITE;
            c2 = Color.GRAY;
        }
        g.setColor(c1);
        
        g.translate(x, y);
        g.fillRect(0, 0, width-1, height-1);
        g.setColor(c2);
        g.drawRect(0, 0, width-1, height-1);
        g.translate(-x, -y);   //Restore graphics object
    }
}
