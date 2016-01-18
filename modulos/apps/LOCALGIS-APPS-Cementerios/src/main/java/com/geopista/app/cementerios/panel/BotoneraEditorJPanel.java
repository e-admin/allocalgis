/**
 * BotoneraEditorJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * BotoneraJPanel.java
 *
 * Created on 7 de julio de 2006, 9:48
 */

package com.geopista.app.cementerios.panel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.cementerios.Constantes;


public class BotoneraEditorJPanel extends javax.swing.JPanel {
    Logger logger= Logger.getLogger(BotoneraEditorJPanel.class);

    private AppContext aplicacion;
    private javax.swing.JFrame desktop;

    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;


    /**
     * Método que genera un panel con la botonera que se muestra en el modulo de inventario para el Editor de Cartografia
     * @param desktop
     */
    public BotoneraEditorJPanel(javax.swing.JFrame desktop) {
        this.desktop= desktop;
        this.aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        annadirJButton = new javax.swing.JButton();
        modificarJButton = new javax.swing.JButton();
        anexarJButton = new javax.swing.JButton();
        eliminarJButton = new javax.swing.JButton();

        setLayout(new java.awt.FlowLayout(FlowLayout.CENTER));

        annadirJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annadirJButtonActionPerformed();
            }
        });
        add(annadirJButton);

        modificarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarJButtonActionPerformed();
            }
        });
        add(modificarJButton);

        anexarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anexarJButtonActionPerformed();
            }
        });
        add(anexarJButton);

        eliminarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarJButtonActionPerformed();
            }
        });
        add(eliminarJButton);

    }

    private void eliminarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ELIMINAR;
        fireActionPerformed();
    }

    private void anexarJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ANEXAR;
        fireActionPerformed();
    }

    private void annadirJButtonActionPerformed() {
        botonPressed= Constantes.OPERACION_ANNADIR;
        fireActionPerformed();
    }

    public boolean modificarJButtonActionPerformed() {
    	if (modificarJButton.isEnabled()){
    		botonPressed= Constantes.OPERACION_MODIFICAR;
    		fireActionPerformed();
    		return true;
    	}
    	return false;
    }


    public void renombrarComponentes(){
        try{annadirJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag1"));}catch(Exception e){}
        try{modificarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag2"));}catch(Exception e){}
        try{anexarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag5"));}catch(Exception e){}
        try{eliminarJButton.setText(aplicacion.getI18nString("cementerio.botonera.tag4"));}catch(Exception e){}
    }


    public void setEnabled(boolean b){
        annadirJButton.setEnabled(b);
        modificarJButton.setEnabled(b);
        anexarJButton.setEnabled(b);
        eliminarJButton.setEnabled(b);
    }

    public void annadirJButtonSetEnabled(boolean b){
       annadirJButton.setEnabled(b); 
    }

    public void eliminarJButtonSetEnabled(boolean b){
        eliminarJButton.setEnabled(b); 
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

    public String getBotonPressed(){
        return botonPressed;
    }

    public void setBotonPressed(String s){
        botonPressed= s;
    }

    private javax.swing.JButton annadirJButton;
    private javax.swing.JButton eliminarJButton;
    private javax.swing.JButton modificarJButton;
    private javax.swing.JButton anexarJButton;
    
}
