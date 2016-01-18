/**
 * SegurosJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.protocol.inventario.CompanniaSeguros;
import com.geopista.protocol.inventario.InventarioClient;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 02-ago-2006
 * Time: 14:46:48
 * To change this template use File | Settings | File Templates.
 */
public class SegurosJDialog extends JDialog{
    private AppContext aplicacion;

    private CompanniaSeguros compannia;
    private ArrayList actionListeners= new ArrayList();
    private ListaPane listaPane;

    private InventarioClient inventarioClient = null;

    /**
     * Método que genera el dialogo de inserccion de otro valor
     */
    public SegurosJDialog() throws Exception{
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() throws Exception{

        inventarioClient= new InventarioClient(aplicacion.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL) +
        		Constantes.INVENTARIO_SERVLET_NAME);

        listaPane= new ListaPane(inventarioClient.getCompanniasSeguros());
        listaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        aceptarJButton= new javax.swing.JButton();
        cancelarJButton= new javax.swing.JButton();
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed();
            }
        });
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        setModal(true);
        getContentPane().setLayout(new java.awt.BorderLayout());
        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout());
        botoneraJPanel.add(aceptarJButton);
        botoneraJPanel.add(cancelarJButton);
        getContentPane().add(listaPane, BorderLayout.CENTER);
        getContentPane().add(botoneraJPanel, BorderLayout.SOUTH);

        //setSize(470, 500);
        setSize(570, 710);
        setLocation(200, 140);
        //GUIUtil.centreOnWindow(this);
    }

    private void listaPaneActionPerformed(){
        compannia= (CompanniaSeguros)listaPane.getSelected();
    }

    private void renombrarComponentes(){
        String title= "";
        try{
            title= aplicacion.getI18nString("inventario.listaSeguros.tag1");
        }catch(Exception e){};

        listaPane.renombrar(title);
        try{setTitle(aplicacion.getI18nString("inventario.segurosJDialog.tag1"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.segurosJDialog.tag2"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.segurosJDialog.tag3"));}catch(Exception e){}
    }

    private void cancelarJButtonActionPerformed(){
        compannia= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        compannia= (CompanniaSeguros)listaPane.getSelected();
        fireActionPerformed();
    }

    public CompanniaSeguros getCompannia(){
        return compannia;
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


    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;




}
