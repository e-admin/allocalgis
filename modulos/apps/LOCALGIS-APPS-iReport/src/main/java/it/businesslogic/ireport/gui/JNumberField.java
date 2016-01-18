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
 * JNumberField.java
 * 
 * Created on 10 febbraio 2003, 0.22
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.gui.event.ValueChangedEvent;
import it.businesslogic.ireport.gui.event.ValueChangedListener;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.text.NumberFormat;

import javax.swing.FocusManager;
import javax.swing.text.Document;

/**
 *
 * @author  Administrator
 */
public class JNumberField extends javax.swing.JTextField implements java.awt.event.FocusListener, ValueChangedListener, KeyListener {
    
    /** Holds value of property decimals. */
    private int decimals=2;
    
    /** Holds value of property integer. */
    private boolean integer=false;
    
    /** Holds value of property value. */
    private double value = 0.0;
    private double oldValue = 0.0;
    
    private java.text.NumberFormat numberFormat=null;
    
    /** Holds value of property grouping. */
    private boolean grouping=true;
    
    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList =  null;
    
    private boolean init = false;
    
    private NumberDocument nd = new NumberDocument();
    
    /** Creates a new instance of JNumberField */
    public JNumberField() {
        
        super();
        this.addFocusListener(this);
        numberFormat=NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);
        this.setText(numberFormat.format(0));
         
        nd.addValueChangedListener( this );
        
        this.addKeyListener(this);
        
        this.setDocument( nd );
        
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
                FocusManager.getCurrentManager().clearGlobalFocusOwner();
                e.consume();
        }
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
                FocusManager.getCurrentManager().clearGlobalFocusOwner();
                e.consume();
        }
    }
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
                FocusManager.getCurrentManager().clearGlobalFocusOwner();
                e.consume();
        }
    }
    
    public void valueChanged(ValueChangedEvent evt) {
        
        if (isInit()) return;       
        value = evt.getNewValue();
        //System.out.println("Changed value: " + value);        
    }
    
    public void textChanged(javax.swing.event.DocumentEvent evt) {
        
        
    }
    
    
    /** Creates the default implementation of the model
     * to be used at construction if one isn't explicitly
     * given.  An instance of <code>PlainDocument</code> is returned.
     *
     * @return the default model implementation
     *
     */
    protected Document createDefaultModel() {
        //Document retValue;
        
        //retValue = super.createDefaultModel();
        return new NumberDocument();
    }
    
    /** Getter for property decimals.
     * @return Value of property decimals.
     *
     */
    public int getDecimals() {
        return this.decimals;
    }
    
    /** Setter for property decimals.
     * @param decimals New value of property decimals.
     *
     * @throws PropertyVetoException
     *
     */
    public void setDecimals(int decimals) throws java.beans.PropertyVetoException {
        this.decimals = decimals;
        if (!isInteger()) {
            numberFormat.setMaximumFractionDigits(decimals);
            numberFormat.setMinimumFractionDigits(decimals);
        }
        this.setText( numberFormat.format(getValue()));
    }
    
    /** Getter for property integer.
     * @return Value of property integer.
     *
     */
    public boolean isInteger() {
        return this.integer;
    }
    
    /** Setter for property integer.
     * @param integer New value of property integer.
     *
     * @throws PropertyVetoException
     *
     */
    public void setInteger(boolean integer) throws java.beans.PropertyVetoException {
        if (integer) {
            numberFormat.setMaximumFractionDigits(0);
            numberFormat.setMinimumFractionDigits(0);
        } else {
            
            if (getDecimals() == -1) {
                numberFormat.setMinimumFractionDigits(1);
                numberFormat.setMaximumFractionDigits(100);
            } else {
                numberFormat.setMinimumFractionDigits(getDecimals());
                numberFormat.setMaximumFractionDigits(getDecimals());
            }
        }
        this.integer = integer;
        this.setText( numberFormat.format(getValue()));
    }
    
    /** Getter for property value.
     * @return Value of property value.
     *
     */
    public double getValue() {
        return this.value;
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     *
     * @throws PropertyVetoException
     *
     */
    public void setValue(double value) throws java.beans.PropertyVetoException {
        this.setInit(true);
        this.value = value;
        this.oldValue = value;
        nd.setValue(value);
        this.setText( numberFormat.format(getValue()) );
        this.setInit(false);
    }
    
    public void setValue(int value) throws java.beans.PropertyVetoException {
        setValue((double)value);
    }
    
    
    public void setText(String text) {
        this.setInit(true);
        super.setText(text);
        this.setInit(false);
    }
    
    /** Invoked when a component gains the keyboard focus.
     *
     */
    public void focusGained(FocusEvent e) {
        
        if (getText().length() == 0) return;
        this.oldValue = value;
        numberFormat.setGroupingUsed(false);
        setText( numberFormat.format(value));
        numberFormat.setGroupingUsed(this.isGrouping());
        selectAll();
    }
    
    /** Invoked when a component loses the keyboard focus.
     *
     */
    public void focusLost(FocusEvent e) {
        
        if (getText().length() == 0) return;
        setText( numberFormat.format(value));
        if (oldValue != value)
            this.fireActionListenerActionPerformed(new java.awt.event.ActionEvent( this, 0, "") );
    }
    
    /** Getter for property grouping.
     * @return Value of property grouping.
     *
     */
    public boolean isGrouping() {
        return this.grouping;
    }
    
    /** Setter for property grouping.
     * @param grouping New value of property grouping.
     *
     * @throws PropertyVetoException
     *
     */
    public void setGrouping(boolean grouping) throws java.beans.PropertyVetoException {
        this.grouping = grouping;
    }
    
    /** Registers ActionListener to receive events.
     * @param listener The listener to register.
     *
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(java.awt.event.ActionListener.class, listener);
    }
    
    /** Removes ActionListener from the list of listeners.
     * @param listener The listener to remove.
     *
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {
        listenerList.remove(java.awt.event.ActionListener.class, listener);
    }
    
    /** Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     *
     */
    private void fireActionListenerActionPerformed(java.awt.event.ActionEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener)listeners[i+1]).actionPerformed(event);
            }
        }
    }
    
    public boolean isInit() {
        return init;
    }
    
    public void setInit(boolean init) {
        this.init = init;
    }
    
}

