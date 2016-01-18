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
 * JRadioButtonMenuItemMDIFrame.java
 * 
 * Created on 10 giugno 2003, 23.26
 *
 */

package it.businesslogic.ireport.gui;

/**
 *
 * @author  Administrator
 */
public class JRadioButtonMenuItemMDIFrame extends javax.swing.JRadioButtonMenuItem {
    
    private JMDIFrame frame;
    
    /** Creates a new instance of JRadioButtonMenuItemMDIFrame */
    public JRadioButtonMenuItemMDIFrame( JMDIFrame frame,String text) {
        super(text);
        this.frame = frame;
    }
    
    /** Getter for property frame.
     * @return Value of property frame.
     *
     */
    public JMDIFrame getFrame() {
        return frame;
    }
    
    /** Setter for property frame.
     * @param frame New value of property frame.
     *
     */
    public void setFrame(JMDIFrame frame) {
        this.frame = frame;
    }
    
}
