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
 * IRFont.java
 * 
 * Created on 22 maggio 2003, 23.45
 *
 */

package it.businesslogic.ireport;

/**
 *
 * @author  Administrator
 */
public class IRFont {
    
    private java.awt.Font font;
    
    private java.lang.String file;
    
    /** Creates a new instance of IRFont */
    public IRFont() {
    }
    
     public IRFont(java.awt.Font font, java.lang.String file) {
         this.font = font;
         this.file = file;
    }
    
    /** Getter for property file.
     * @return Value of property file.
     *
     */
    public java.lang.String getFile() {
        return file;
    }
    
    /** Setter for property file.
     * @param file New value of property file.
     *
     */
    public void setFile(java.lang.String file) {
        this.file = file;
    }
    
    /** Getter for property font.
     * @return Value of property font.
     *
     */
    public java.awt.Font getFont() {
        return font;
    }
    
    /** Setter for property font.
     * @param font New value of property font.
     *
     */
    public void setFont(java.awt.Font font) {
        this.font = font;
    }
    
    
    public String toString()
    {
        if (font == null || file == null) return "Not initialized font";
        return font.getFontName()+" ("+file+")";
    }    
}
