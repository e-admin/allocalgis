/**
 * InformesJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 17-oct-2006
 * Time: 15:53:51
 * To change this template use File | Settings | File Templates.
 */
public class InformesJDialog extends JDialog{
    private AppContext aplicacion;
    private javax.swing.JFrame desktop;
    private String locale;
    private String superpatron;
    private String patron;
    private InformesJPanel informesJPanel;
    private ArrayList actionListeners= new ArrayList();


    /**
     * Método que genera el dialogo de filtro
     */
    public InformesJDialog(JFrame desktop, String superpatron, String patron, String locale) throws Exception{
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        this.locale= locale;
        this.superpatron= superpatron;
        this.patron= patron;

        initComponents();
        renombrarComponentes();
        addAyudaOnline();
    }

    private void initComponents() throws Exception{
        informesJPanel= new InformesJPanel(desktop, superpatron, patron, locale);
        informesJPanel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e){
                informesJPanel_actionPerformed();
            }
        });


        getContentPane().setLayout(new BorderLayout());
        setModal(true);

        getContentPane().add(informesJPanel, BorderLayout.CENTER);

        setSize(760, 650);
        setLocation(100, 0);

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
                String uriRelativa = "/Geocuenca:Inventario:Informes";
                GeopistaBrowser.openURL(aplicacion
                        .getString("ayuda.geopista.web")
                        + uriRelativa);
            }
        });
    }


    private void renombrarComponentes(){
        try{setTitle(aplicacion.getI18nString("cementerio.informes.tag1"));}catch(Exception e){}
    }    

    private void informesJPanel_actionPerformed(){
        fireActionPerformed();
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



}
