/**
 * FiltrarJDialog.java
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
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 04-oct-2006
 * Time: 15:07:33
 * To change this template use File | Settings | File Templates.
 */
public class FiltrarJDialog extends JDialog implements ActionListener {

    private AppContext aplicacion;
    private String locale;
    private String tipo;
    private String subTipo;
    private int operacion;
    private javax.swing.JFrame desktop;


    private Collection filtro;
    private ArrayList actionListeners= new ArrayList();
    private boolean aplicarFiltro;
    private FiltroJPanel filtroJPanel;

    public static final int Aceptar=0;
    public static final int Cancelar=1;


    /**
     * Método que genera el dialogo de filtro
     */
    public FiltrarJDialog(JFrame desktop, String tipo, String subTipo, String locale) {
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.locale= locale;
        this.tipo= tipo;
        this.subTipo= subTipo;

        initComponents();
        renombrarComponentes();
        addAyudaOnline();
    }

    private void initComponents() {

        filtroJPanel= new FiltroJPanel(desktop, tipo, subTipo, locale);
        filtroJPanel.setVisibleAplicarFiltro(true);

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

        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        JPanel botoneraJPanel= new JPanel();
        botoneraJPanel.setLayout(new java.awt.FlowLayout());
        botoneraJPanel.add(aceptarJButton);
        botoneraJPanel.add(cancelarJButton);

        getContentPane().add(filtroJPanel, BorderLayout.CENTER);
        getContentPane().add(botoneraJPanel, BorderLayout.SOUTH);

        setSize(690, 450);
        //setLocation(200, 140);
        GUIUtil.centreOnWindow(this);

    }

    /**
     * Ayuda Online
     *
     */
    private void addAyudaOnline() {
        getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("F1"), "action F1");

        getRootPane().getActionMap().put("action F1", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                String uriRelativa = "/Geocuenca:Inventario:Filtrar";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }


    private void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("inventario.filtrar.tag1"));}catch(Exception e){}
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.filtrar.tag2"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.filtrar.tag3"));}catch(Exception e){}
    }

    private void cancelarJButtonActionPerformed(){
        operacion= Cancelar;
        aplicarFiltro= filtroJPanel.getAplicarFiltro();
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        operacion= Aceptar;
        filtro= filtroJPanel.getFiltro();
        aplicarFiltro= filtroJPanel.getAplicarFiltro();
        fireActionPerformed();
    }

    public void load(Collection filtro, boolean aplicar){
        filtroJPanel.load(filtro, aplicar);
    }

    public boolean aceptar() {
        return operacion==Aceptar?true:false;
    }

    public boolean getAplicarFiltro() {
        return aplicarFiltro;
    }

    public ArrayList getFiltro(){
        if (filtro == null) return null;
        return new ArrayList(filtro);
    }

    public void actionPerformed(ActionEvent e){
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
