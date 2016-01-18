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
 * IconedString.java
 * 
 * Created on 2 ottobre 2004, 10.51
 *
 */

package it.businesslogic.ireport.gui;

/**
 *
 * @author  Administrator
 */
public class IconedString {
    
    private String str ="";
    private javax.swing.ImageIcon icon = null;
    
    /** Creates a new instance of IconedString */
    public IconedString(String str, String icon) {
       this.setStr(str);
       this.setIcon( new javax.swing.ImageIcon( this.getClass().getResource(icon) ));
       
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public javax.swing.ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(javax.swing.ImageIcon icon) {
        this.icon = icon;
    }
    
    public String toString()
    {
        return getStr();
    }
}
