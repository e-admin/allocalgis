/*
 * Copyright (C) 2006 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * MaskedPlainDocument.java
 *
 * Created on May 7, 2007, 11:46 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.gui.sheet;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author gtoffoli
 */
public class MaskedPlainDocument extends  PlainDocument {
    
    public static final int NO_MAX_LENGTH = 0;
    
    public static final String COLOR_MASK = "(#?)(([0-9]|[a-f]|[A-F]){0,6})";
    private String mask = null;
    private int maxLength = 0;
    
    /**
     * Create a MaskedPlainDocument based on the given regex espression
     */
    public MaskedPlainDocument(String mask, int maxLength)
    {
        this.mask = mask;
        this.maxLength = maxLength;
    }
    
    /**
     * Same as MaskedPlainDocument(String mask, NO_MAX_LENGTH)
     *
     */
    public MaskedPlainDocument(String mask)
    {
        this(mask, NO_MAX_LENGTH);
    }
    
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            
            if (str == null)
                    return;
            if (maxLength > 0 && offset >= maxLength) {
                    return;
            }
            // does the insertion exceed the max length
            if (maxLength > 0 && str.length() > maxLength) {
                    str = str.substring(0, maxLength);
            }
            
            // Create the final string...
            try {
                
            String currentString = getText(0, offset);
            currentString += str;
            
            //System.out.println("Getting text " + offset +  "  ==> " + getLength());
            
            if (offset < getLength())
            {
                currentString +=  getText(offset, getLength()-offset);
            }
            // remove the last '\n' if any...
            //if (currentString.endsWith("\n")) currentString = currentString.substring(0, currentString.length()-1);
            currentString = currentString.trim();
            //System.out.println(currentString + " " + currentString.matches(mask) +  "  ==> " + mask);
            
            if (currentString.matches(mask))
            {
                str = str.toUpperCase();
                super.insertString(offset, str, attr);
            }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
}
