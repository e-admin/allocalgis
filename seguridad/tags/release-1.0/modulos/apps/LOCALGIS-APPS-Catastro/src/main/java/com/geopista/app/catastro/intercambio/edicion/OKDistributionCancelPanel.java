
/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI 
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */

package com.geopista.app.catastro.intercambio.edicion;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;


public class OKDistributionCancelPanel extends JPanel {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  

    FlowLayout flowLayout1 = new FlowLayout();
    GridLayout gridLayout1 = new GridLayout();
    JPanel innerButtonPanel = new JPanel();
    private JButton jButtonReparto = null;
    private boolean isEditable = false;
    JButton cancelButton = new JButton() {
        {
            setMnemonic('C');
        }
    };
    JButton okButton = new JButton() {
        {
            setMnemonic('O');
        }
    };
    private boolean okPressed = false;
    
    private ArrayList actionListeners = new ArrayList();

    public OKDistributionCancelPanel(boolean isEditable) {
    	this.isEditable=isEditable;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public OKDistributionCancelPanel() {        
        this(false);        
    }
    public boolean isEditable()
    {
        return isEditable;
    }

    /**
     * @param isEditable The isEditable to set.
     */
    public void setEditable(boolean isEditable)
    {
        this.isEditable = isEditable;
    }

    void jbInit() throws Exception {
        innerButtonPanel.setLayout(gridLayout1);
        gridLayout1.setVgap(5);
        gridLayout1.setHgap(5);
        this.setLayout(flowLayout1);
        cancelButton.setText(aplicacion.getI18nString("OKCancelPanel.Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
        okButton.setText(aplicacion.getI18nString("OKCancelPanel.OK"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
        this.add(innerButtonPanel, null);
        innerButtonPanel.add(okButton, null);
        innerButtonPanel.add(getJButtonReparto(),null);
        innerButtonPanel.add(cancelButton, null);
    }
    
    public JButton getJButtonReparto() {
        if (jButtonReparto == null) {
            jButtonReparto = new JButton();
            jButtonReparto.setText(I18N.get("Expedientes","finca.boton.cultivo.reparto"));            
        }
        return jButtonReparto;
    }   

    public boolean wasOKPressed() {
        return okPressed;
    }

    public void setOKPressed(boolean okPressed) {
        this.okPressed = okPressed;
    }

    void okButton_actionPerformed(ActionEvent e) {
        okPressed = true;
        fireActionPerformed();
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        okPressed = false;
        fireActionPerformed();
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    public void setOKEnabled(boolean okEnabled) {
        okButton.setEnabled(okEnabled);
    }

    public void setCancelVisible(boolean cancelVisible) {
        if (cancelVisible && !innerButtonPanel.isAncestorOf(cancelButton)) {
            innerButtonPanel.add(cancelButton, null);
        }

        if (!cancelVisible && innerButtonPanel.isAncestorOf(cancelButton)) {
            innerButtonPanel.remove(cancelButton);
        }
    }
}
