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
 * NumberDocument.java
 * 
 * Created on 10 febbraio 2003, 0.17
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.gui.event.ValueChangedEvent;
import it.businesslogic.ireport.util.Misc;

import java.text.DecimalFormatSymbols;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
/**
 *
 * @author  Administrator
 */
public class NumberDocument extends javax.swing.text.PlainDocument {
    private char groupingSeparator = ',';
    private char decimalSeparator = '.';
    
    private double value = 0;
    
    /** Creates a new instance of NumberDocument */
    public NumberDocument() {
        
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        groupingSeparator = dfs.getGroupingSeparator();
        decimalSeparator = dfs.getDecimalSeparator();
     }
    
    public boolean checkNumber(String proposedResult)
    {
        // We allow the use of comma like . if no decimalSeparator is present...
        if (groupingSeparator == '.' && proposedResult.indexOf(decimalSeparator) < 0)
        {
            //proposedResult = proposedResult.replace(groupingSeparator, '.');
        }
        else
        {
            proposedResult = Misc.string_replace( "", ""+groupingSeparator , proposedResult);
        }
        proposedResult = proposedResult.replace(decimalSeparator, '.');
        
        try {
            //Number n = format.parse(proposedResult);
            if (proposedResult.trim().length() == 0) return true;
            
            double oldValue = getValue();
            
            double newValue = 0;
            if (proposedResult.length()> 0) newValue = Double.parseDouble(proposedResult);

            fireValueChangedListenerValueChanged(new ValueChangedEvent(null, oldValue,newValue));
           
            return true;
        } catch (NumberFormatException e) {
            //System.err.println("insertString: could not parse: "
            //                   + proposedResult);
        }
        return false;
    }
    
    /** Inserts some content into the document.
     * Inserting content causes a write lock to be held while the
     * actual changes are taking place, followed by notification
     * to the observers on the thread that grabbed the write lock.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.
     *
     * @param offs the starting offset >= 0
     * @param str the string to insert; does nothing with null/empty strings
     * @param a the attributes for the inserted content
     * @exception BadLocationException  the given insert position is not a valid
     *   position within the document
     * @see Document#insertString
     *
     */
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        if (str == null || str.length() == 0) {
            super.insertString(offs, str, a);
            return;
        }
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs, currentText.length());
        String proposedResult = beforeOffset + str + afterOffset;
        if (checkNumber(proposedResult))
        {
            super.insertString(offs, str, a);
        }
    }
            
     public void remove(int offs, int len) throws BadLocationException {
         
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(len + offs,
                                                   currentText.length());
        String proposedResult = beforeOffset + afterOffset;
        if (checkNumber(proposedResult))
        {
             super.remove(offs, len);
        }
    }
     
     public void replace(int offset, int length, String str, AttributeSet attrs)  throws BadLocationException {
        
         if (str == null || str.length() == 0) {
             super.replace(offset, length, str, attrs);
             return;
         }
         
         String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offset);
        String afterOffset = currentText.substring(length + offset,
                                                   currentText.length());
        String proposedResult = beforeOffset + str + afterOffset;
        
        
        if (checkNumber(proposedResult))
        {
             super.replace(offset, length, str, attrs);
        }
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList =  null;

    /**
     * Registers ValueChangedListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addValueChangedListener(it.businesslogic.ireport.gui.event.ValueChangedListener listener) {

        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add (it.businesslogic.ireport.gui.event.ValueChangedListener.class, listener);
    }

    /**
     * Removes ValueChangedListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeValueChangedListener(it.businesslogic.ireport.gui.event.ValueChangedListener listener) {

        listenerList.remove (it.businesslogic.ireport.gui.event.ValueChangedListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireValueChangedListenerValueChanged(it.businesslogic.ireport.gui.event.ValueChangedEvent event) {

        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i]==it.businesslogic.ireport.gui.event.ValueChangedListener.class) {
                ((it.businesslogic.ireport.gui.event.ValueChangedListener)listeners[i+1]).valueChanged (event);
            }
        }
    }
          
}
