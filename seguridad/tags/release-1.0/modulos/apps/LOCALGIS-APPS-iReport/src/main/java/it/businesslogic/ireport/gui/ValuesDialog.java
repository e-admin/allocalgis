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
 * ValuesDialog.java
 * 
 * Created on 7 maggio 2003, 23.43
 *
 */

package it.businesslogic.ireport.gui;

import it.businesslogic.ireport.*;
import it.businesslogic.ireport.gui.event.*;
import it.businesslogic.ireport.util.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


/**
 * @author Administrator
 */
public class ValuesDialog
    extends javax.swing.JDialog
    implements SubDatasetObjectChangedListener
{


    private ValuesPanel valuesPanel = null;

    /**
     * Creates new form ValuesDialog
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public ValuesDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initAll();
    }
    
    public ValuesDialog(java.awt.Dialog parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        initAll();
    }
    
    public void initAll()
    {
        valuesPanel = new ValuesPanel();
        this.getContentPane().add("Center", valuesPanel);

        applyI18n();
        
        I18n.addOnLanguageChangedListener( new LanguageChangedListener() {
            public void languageChanged(LanguageChangedEvent evt) {
                applyI18n();
            }
        } );
        
        this.setSize(500, 300);

        // Open in center...
        it.businesslogic.ireport.util.Misc.centerFrame(this);
        
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", escapeAction);

        
    }


    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setTitle("Values");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     * @param evt DOCUMENT ME!
     */
    private void closeDialog(java.awt.event.WindowEvent evt)//GEN-FIRST:event_closeDialog
    {
        setVisible(false);
    }//GEN-LAST:event_closeDialog

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new ValuesDialog(new javax.swing.JFrame(), true).setVisible(true);
    }

    /**
     * Getter for property jReportFrame.
     * 
     * @return Value of property jReportFrame.
     */
    public it.businesslogic.ireport.gui.JReportFrame getJReportFrame()
    {
        return jReportFrame;
    }

    /**
     * Setter for property jReportFrame.
     * 
     * @param jReportFrame New value of property jReportFrame.
     */
    public void setJReportFrame(it.businesslogic.ireport.gui.JReportFrame jReportFrame)
    {
        this.jReportFrame = jReportFrame;

        // Update all...
        if (jReportFrame == null)
        {
            setVisible(false);
        }
        else if (isVisible())
        {
            this.setTitle(jReportFrame.getReport().getName() + " values...");
            this.getValuesPanel().setSubDataset( getJReportFrame().getReport() );
        }
        if( jReportFrame != null )
        {
            this.getValuesPanel().setSubDataset( getJReportFrame().getReport() );
        }
    }

   

    /**
     * DOCUMENT ME!
     */
    public void applyI18n()
    {
        this.getValuesPanel().applyI18n();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param evt DOCUMENT ME!
     */
    public void languageChanged(LanguageChangedEvent evt)
    {
        this.applyI18n();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private JReportFrame jReportFrame;

    /**
     * DOCUMENT ME!
     * 
     * @param visible DOCUMENT ME!
     */
    public void setVisible(boolean visible)
    {

        if (visible == isVisible())
        {

            return;
        }

        super.setVisible(visible);

        if (visible == true)
        {
            this.setJReportFrame(jReportFrame);
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param p DOCUMENT ME!
     */
    public void modifyErrorParameter(it.businesslogic.ireport.JRParameter p)
    {

        this.getValuesPanel().modifyErrorParameter(p);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param tab DOCUMENT ME!
     */
    public void gotoTab(String tab)
    {

        this.getValuesPanel().gotoTab(tab);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardFields()
    {
        return getValuesPanel().getClipboardFields();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardFields DOCUMENT ME!
     */
    public void setClipboardFields(Vector clipboardFields)
    {
        getValuesPanel().setClipboardFields(clipboardFields);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardVariables()
    {

        return getValuesPanel().getClipboardVariables();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardVariables DOCUMENT ME!
     */
    public void setClipboardVariables(Vector clipboardVariables)
    {
        getValuesPanel().setClipboardVariables(clipboardVariables);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME! 
     */
    public Vector getClipboardParameters()
    {

        return getValuesPanel().getClipboardParameters();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param clipboardParameters DOCUMENT ME!
     */
    public void setClipboardParameters(Vector clipboardParameters)
    {
        getValuesPanel().setClipboardParameters(clipboardParameters);
    }


    /**
     * DOCUMENT ME!
     * 
     * @param evt DOCUMENT ME!
     */
    public void subDatasetObjectChanged(SubDatasetObjectChangedEvent evt)
    {
        if ( evt.getSource() == getValuesPanel().getSubDataset() )
        {
            if (evt.getType() == evt.PARAMETER)
            {
                getValuesPanel().updateParameters();
            }
            else if (evt.getType() == evt.VARIABLE)
            {
                getValuesPanel().updateVariables();
            }
            else if (evt.getType() == evt.FIELD)
            {
                getValuesPanel().updateFields();
            }
        }
    }

    public ValuesPanel getValuesPanel() {
        return valuesPanel;
    }

    public void setValuesPanel(ValuesPanel valuesPanel) {
        this.valuesPanel = valuesPanel;
    }
}
