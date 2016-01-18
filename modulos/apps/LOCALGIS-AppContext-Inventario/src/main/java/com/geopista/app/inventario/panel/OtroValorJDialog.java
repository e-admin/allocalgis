/**
 * OtroValorJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 26-jul-2006
 * Time: 9:25:57
 * To change this template use File | Settings | File Templates.
 */
public class OtroValorJDialog extends javax.swing.JDialog{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AppContext aplicacion;

    private String valor;
    private ArrayList actionListeners= new ArrayList();

    /**
     * Método que genera el dialogo de inserccion de otro valor
     * @param title del dialogo
     * @param labeltext
     */
    public OtroValorJDialog(JFrame desktop, String title, String labeltext) {
        super(desktop);
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes(title, labeltext);
    }

    private void initComponents() {
        valorJLabel = new javax.swing.JLabel();
        valorJTField = new com.geopista.app.utilidades.TextField(254);
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

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setModal(true);

        getContentPane().add(valorJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 20));
        getContentPane().add(valorJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 310, -1));
        getContentPane().add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));
        getContentPane().add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        //setSize(470, 150);
        setSize(570, 710);
        //setLocation(200, 140);
        GUIUtil.centreOnWindow(this);
    }

    private void renombrarComponentes(String title, String labeltext){
        setTitle(title);
        valorJLabel.setText(labeltext);
        try{aceptarJButton.setText(aplicacion.getI18nString("inventario.otroValorJDialog.tag1"));}catch(Exception e){}
        try{cancelarJButton.setText(aplicacion.getI18nString("inventario.otroValorJDialog.tag2"));}catch(Exception e){}

    }

    private void cancelarJButtonActionPerformed(){
        valor= null;
        fireActionPerformed();
    }

    private void aceptarJButtonActionPerformed(){
        if (valorJTField.getText().trim().equalsIgnoreCase("")) valor= null;
        else valor= valorJTField.getText().trim();
        fireActionPerformed();
    }

    public String getValor(){
        return valor;
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


    private javax.swing.JLabel valorJLabel;
    private javax.swing.JTextField valorJTField;
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton cancelarJButton;


}
