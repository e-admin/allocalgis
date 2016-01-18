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
 * MessageBox.java
 * 
 * Created on 11 febbraio 2003, 16.07
 *
 */

package it.businesslogic.ireport.gui;

import javax.swing.*;
/**
 *
 * @author  Administrator
 */
public class MessageBox {
    
    // Buttons
    /** Indicates that the message box should display an OK button */
    public static final int OK = 1;
    /** Indicates that the message box should display OK and Cancel buttons */
    public static final int OKCANCEL = 2;
    /** Indicates that the message box should display Yes and No buttons */
    public static final int YESNO = 4;
    /** Indicates that the message box should display Yes, No, and Cancel buttons */
    public static final int YESNOCANCEL = 8;
    
    // ICONS
    /** Indicates that the message box should display an error icon */
    public static final int ICONERROR = 16;
    /** Indicates that the message box should display an exclamation (!) icon */
    public static final int ICONINFORMATION = 32;
    /** Indicates that the message box should display a question mark (?) icon */
    public static final int ICONQUESTION = 64;
    /** Indicates that the message box should display a warning icon */
    public static final int ICONWARNING = 128;
   
    /** Creates a new instance of MessageBox */
    public MessageBox() {
    }
    
    public static int show(String msg, String title, int flags)
    {
        return MessageBox.show(null, msg,title,flags);
    }
    
    public static int show(String msg)
    {
        return MessageBox.show(null, msg,"",MessageBox.OK);
    }
        
    public static int show(JComponent parent, String msg)
    {
        return MessageBox.show(parent, msg,"",MessageBox.OK);
    }
    
    public static int show(JComponent parent, String msg, String title, int flags)
    {
         
        int messageType = JOptionPane.PLAIN_MESSAGE;
        if ( (flags & MessageBox.ICONERROR) != 0)
            messageType = JOptionPane.ERROR_MESSAGE;
        else if ( (flags & MessageBox.ICONINFORMATION) != 0)
            messageType = JOptionPane.INFORMATION_MESSAGE;
        else if ( (flags & MessageBox.ICONQUESTION) != 0)
            messageType = JOptionPane.QUESTION_MESSAGE;
        else if ( (flags & MessageBox.ICONWARNING) != 0)
            messageType = JOptionPane.WARNING_MESSAGE;
        
        int buttonType = JOptionPane.DEFAULT_OPTION;
        if ( (flags & MessageBox.YESNO) != 0)
            buttonType = JOptionPane.YES_NO_OPTION;
        else if ( (flags & MessageBox.YESNOCANCEL) != 0)
            buttonType = JOptionPane.YES_NO_CANCEL_OPTION;
        else if ( (flags & MessageBox.OKCANCEL) != 0)
            buttonType = JOptionPane.OK_CANCEL_OPTION;
      
         return JOptionPane.showConfirmDialog( parent,msg,title,buttonType, messageType);
    }
}
