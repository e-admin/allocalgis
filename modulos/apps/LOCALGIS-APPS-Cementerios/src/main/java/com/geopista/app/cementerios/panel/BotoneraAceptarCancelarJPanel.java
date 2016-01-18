/**
 * BotoneraAceptarCancelarJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * BotoneraAceptarCancelarJPanel.java
 *
 * Created on 13 de julio de 2006, 9:12
 */

package com.geopista.app.cementerios.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.AppContext;

/**
 *
 * @author  charo
 */
public class BotoneraAceptarCancelarJPanel extends javax.swing.JPanel {
    private AppContext aplicacion;

    private boolean aceptarPressed= false;
    private ArrayList actionListeners= new ArrayList();


    /**
     * Método que genera un panel con una botonera Aceptar/Cancelar
     */
    public BotoneraAceptarCancelarJPanel() {
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }
    
    /**
     * Método que inicializa el panel
     */
    private void initComponents() {
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setLayout(new java.awt.FlowLayout());

        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });

        add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));
        add(aceptarJButton);

        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, -1));
        add(cancelarJButton);

    }

    private void cancelarJButtonActionPerformed() {
        aceptarPressed= false;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed() {
        aceptarPressed= true;
        fireActionPerformed();
    }

    /**
     * @return true si el boton de Aceptar ha sido pulsado
     * @return false en caso contrario
     */
    public boolean aceptarPressed() {
        return aceptarPressed;
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

    /**
     * Método que renombra los componenetes del panel segun el locale
     */
    private void renombrarComponentes(){
        try{aceptarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag6"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag7"));}catch(Exception e){}
   }

    public void setEnabled(boolean b){
        aceptarJButton.setEnabled(b);
        cancelarJButton.setEnabled(b);
    }

    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;

}
